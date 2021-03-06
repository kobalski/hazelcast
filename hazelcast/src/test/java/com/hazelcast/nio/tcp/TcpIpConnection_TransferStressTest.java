package com.hazelcast.nio.tcp;

import com.hazelcast.nio.Packet;
import com.hazelcast.test.AssertTask;
import com.hazelcast.test.TestThread;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;

/**
 * This test will concurrently write to a single connection and check if all the data transmitted, is received
 * on the other side.
 *
 * In the past we had some issues with packet not getting written. So this test will write various size packets (from small
 * to very large).
 */
public abstract class TcpIpConnection_TransferStressTest extends TcpIpConnection_AbstractTest {

    // total running time.
    private static final long DURATION_SECONDS = TimeUnit.MINUTES.toSeconds(2);
    // maximum number of pending packets
    private static final int maxPendingPacketCount = 10000;
    // we create the payloads up front and select randomly from them. This is the number of payloads we are creating.
    private static final int payloadCount = 10000;

    private final AtomicBoolean stop = new AtomicBoolean();
    private DummyPayload[] payloads;

    @Before
    public void setup() throws Exception {
        super.setup();
        startAllConnectionManagers();
    }

    @Test
    public void testTinyPackets() throws Exception {
        makePayloads(10);
        test();
    }

    @Test
    public void testSmallPackets() throws Exception {
        makePayloads(100);
        test();
    }

    @Test
    public void testMediumPackets() throws Exception {
        makePayloads(1000);
        test();
    }

    @Test
    public void testLargePackets() throws Exception {
        makePayloads(10000);
        test();
    }

    @Test
    public void testSemiRealisticPackets() throws Exception {
        makeSemiRealisticPayloads();
        test();
    }

    public void test() throws Exception {
        TcpIpConnection c = connect(connManagerA, addressB);

        WriteThread thread1 = new WriteThread(1, c);
        WriteThread thread2 = new WriteThread(2, c);

        logger.info("Starting");
        thread1.start();
        thread2.start();

        sleepAndStop(stop, DURATION_SECONDS);

        logger.info("Done");

        thread1.assertSucceedsEventually();
        thread2.assertSucceedsEventually();

        // there is always one packet extra for the bind-request
        final long expectedNormalPackets = thread1.normalPackets + thread2.normalPackets + 1;
        final long expectedUrgentPackets = thread1.urgentPackets + thread2.urgentPackets;

        logger.info("expected normal packets: " + expectedNormalPackets);
        logger.info("expected priority packets: " + expectedUrgentPackets);

        final ReadHandler readHandler = ((TcpIpConnection) connManagerB.getConnection(addressA)).getReadHandler();
        assertTrueEventually(new AssertTask() {
            @Override
            public void run() throws Exception {
                assertEquals(expectedNormalPackets, readHandler.getNormalPacketsReadCounter().get());
                assertEquals(expectedUrgentPackets, readHandler.getPriorityPacketsReadCounter().get());
            }
        });

    }

    private void makePayloads(int maxSize) {
        Random random = new Random();
        payloads = new DummyPayload[payloadCount];

        for (int k = 0; k < payloads.length; k++) {
            final byte[] bytes = new byte[random.nextInt(maxSize)];
            payloads[k] = new DummyPayload(bytes, false);
        }
    }

    private void makeSemiRealisticPayloads() {
        Random random = new Random();
        payloads = new DummyPayload[payloadCount];

        for (int k = 0; k < payloads.length; k++) {
            // one in every 100 packet we make big
            boolean largePacket = random.nextInt(100) == 1;
            boolean ultraLarge = false;
            if (largePacket) {
                ultraLarge = random.nextInt(10) == 1;
            }

            int byteCount;
            if (ultraLarge) {
                byteCount = random.nextInt(100000);
            } else if (largePacket) {
                byteCount = random.nextInt(10000);
            } else {
                byteCount = random.nextInt(300);
            }

            // one in every 100 packet we make urgent
            boolean urgentPacket = random.nextInt(100) == 1;

            payloads[k] = new DummyPayload(new byte[byteCount], urgentPacket);
        }
    }

    public class WriteThread extends TestThread {
        private final Random random = new Random();
        private final WriteHandler writeHandler;
        private long normalPackets;
        private long urgentPackets;

        public WriteThread(int id, TcpIpConnection c) {
            super("WriteThread-" + id);
            this.writeHandler = c.getWriteHandler();
        }

        @Override
        public void doRun() throws Throwable {
            long prev = System.currentTimeMillis();
            while (!stop.get()) {
                Packet packet = nextPacket();
                if (packet.isUrgent()) {
                    urgentPackets++;
                } else {
                    normalPackets++;
                }
                writeHandler.offer(packet);


                long now = System.currentTimeMillis();
                if (now > prev + 2000) {
                    prev = now;
                    logger.info("At normal-packets:" + normalPackets + " priority-packets:" + urgentPackets);
                }

                double usage = getUsage();
                if (usage < 90) {
                    continue;
                }

                for (; ; ) {
                    sleep(random.nextInt(5));
                    if (getUsage() < 10) {
                        //logger.info("Pausing");
                        break;
                    }
                }
            }

            logger.info("Finished, normal packets written: " + normalPackets + " urgent packets written:" + urgentPackets);
        }

        private double getUsage() {
            return 100d * writeHandler.totalPacketsPending() / maxPendingPacketCount;
        }

        public Packet nextPacket() {
            DummyPayload payload = payloads[random.nextInt(payloads.length)];
            Packet packet = new Packet(serializationService.toBytes(payload));
            if (payload.isUrgent()) {
                packet.setHeader(Packet.HEADER_URGENT);
            }
            return packet;
        }
    }
}
