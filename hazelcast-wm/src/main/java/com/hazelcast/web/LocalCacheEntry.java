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

public class LocalCacheEntry {

    private volatile boolean reload;
    private boolean removed;
    private Object value;
    private volatile boolean dirty;
    private final boolean transientEntry;

    public LocalCacheEntry(boolean transientEntry) {
        this.transientEntry = transientEntry;
    }

    public boolean isTransient() {
        return transientEntry;
    }

    public boolean isDirty() {
        return !transientEntry && dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isReload() {
        return reload;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public boolean isTransientEntry() {
        return transientEntry;
    }
}
