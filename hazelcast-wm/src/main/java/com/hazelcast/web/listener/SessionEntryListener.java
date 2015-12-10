package com.hazelcast.web.listener;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryEvictedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.web.SessionState;
import com.hazelcast.web.WebFilter;

public class SessionEntryListener implements EntryEvictedListener<String, SessionState>,
        EntryRemovedListener<String, SessionState> {

    private final WebFilter filter;

    public SessionEntryListener(WebFilter filter) {
        this.filter = filter;
    }

    public void entryEvicted(EntryEvent<String, SessionState> event) {
        filter.destroySessionWithHazelcastSessionId(event.getKey());
    }

    public void entryRemoved(EntryEvent<String, SessionState> event) {
        filter.destroySessionWithHazelcastSessionId(event.getKey());
    }

}
