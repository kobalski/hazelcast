package com.hazelcast.web.listener;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryEvictedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.web.SessionState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEventPublisher;

public class SessionEntryListener implements EntryEvictedListener<String, SessionState>,
        EntryRemovedListener<String, SessionState> {

    private static final Log logger = LogFactory.getLog(SessionEntryListener.class);


    public SessionEntryListener(ApplicationEventPublisher eventPublisher) {

    }

    public void entryEvicted(EntryEvent<String, SessionState> event) {
        if(logger.isDebugEnabled()) {
            logger.debug("Session expired with id: " + event.getOldValue().get;
        }
    }

    public void entryRemoved(EntryEvent<String, SessionState> event) {
        if(logger.isDebugEnabled()) {
            logger.debug("Session deleted with id: " + event.getOldValue().getId());
        }
    }

}
