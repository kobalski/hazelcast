/*
 * Copyright (c) 2008-2014, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.web;

import com.hazelcast.core.EntryListener;
import com.hazelcast.core.IMap;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.query.Predicate;
import com.hazelcast.util.EmptyStatement;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class AbstractIMapWrapper<K, V> {
    final IMap imap;

    public AbstractIMapWrapper(IMap imap) {
        this.imap = imap;
    }

    public String addEntryListener(EntryListener listener, boolean includeValue) {
        return imap.addEntryListener(listener, includeValue);
    }

    public String addEntryListener(EntryListener listener, Object key, boolean includeValue) {
        try {
            return imap.addEntryListener(listener, key, includeValue);
        } catch (Exception e) {
            return null;
        }
    }

    public Object executeOnKey(Object key, EntryProcessor entryProcessor) {
        try {
            return imap.executeOnKey(key, entryProcessor);
        } catch (Exception e) {
            return null;
        }
    }

    public Map executeOnKeys(Set keys, EntryProcessor entryProcessor) {
        try {
            return imap.executeOnKeys(keys, entryProcessor);
        } catch (Exception e) {
            return null;
        }
    }

    public Map executeOnEntries(EntryProcessor entryProcessor) {
        try {
            return imap.executeOnEntries(entryProcessor);
        } catch (Exception e) {
            return null;
        }
    }

    public Map executeOnEntries(EntryProcessor entryProcessor, Predicate predicate) {
        try {
            return imap.executeOnEntries(entryProcessor, predicate);
        } catch (Exception e) {
            return null;
        }
    }

    public Set keySet() {
        try {
            return imap.keySet();
        } catch (Exception e) {
            return null;
        }
    }

    public Collection values() {
        try {
            return imap.values();
        } catch (Exception e) {
            return null;
        }
    }

    public Set<Map.Entry> entrySet() {
        try {
            return imap.entrySet();
        } catch (Exception e) {
            return null;
        }
    }

    public Set keySet(Predicate predicate) {
        try {
            return imap.keySet(predicate);
        } catch (Exception e) {
            return null;
        }
    }

    public Set<Map.Entry<String, Object>> entrySet(Predicate predicate) {
        try {
            return imap.entrySet(predicate);
        } catch (Exception e) {
            return null;
        }
    }

    public Collection values(Predicate predicate) {
        try {
            return imap.values(predicate);
        } catch (Exception e) {
            return null;
        }
    }

    public Object get(Object key) {
        try {
            return imap.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public Object put(Object key, Object value) {
        try {
            return imap.put(key, value);
        } catch (Exception e) {
            return null;
        }
    }

    public void set(Object key, Object value) {
        try {
            imap.set(key, value);
        } catch (Exception ignored) {
            EmptyStatement.ignore(ignored);
        }
    }

    public Object remove(Object key) {
        try {
            return imap.remove(key);
        } catch (Exception e) {
            return null;
        }
    }

    public int size() {
        try {
            return imap.size();
        } catch (Exception e) {
            return -1;
        }
    }

    public void delete(Object key) {
        try {
            imap.delete(key);
        } catch (Exception ignored) {
            EmptyStatement.ignore(ignored);
        }
    }
}
