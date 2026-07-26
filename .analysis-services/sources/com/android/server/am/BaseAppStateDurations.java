package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
abstract class BaseAppStateDurations<T extends com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent> extends com.android.server.am.BaseAppStateTimeEvents<T> {
    static final boolean DEBUG_BASE_APP_STATE_DURATIONS = false;

    BaseAppStateDurations(int uid, java.lang.String packageName, int numOfEventTypes, java.lang.String tag, com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig) {
        super(uid, packageName, numOfEventTypes, tag, maxTrackingDurationConfig);
    }

    BaseAppStateDurations(com.android.server.am.BaseAppStateDurations other) {
        super(other);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void addEvent(boolean start, T event, int index) {
        if (this.mEvents[index] == null) {
            this.mEvents[index] = new java.util.LinkedList();
        }
        java.util.LinkedList linkedList = this.mEvents[index];
        linkedList.size();
        boolean active = isActive(index);
        if (start != active) {
            linkedList.add(event);
        }
        trimEvents(getEarliest(event.getTimestamp()), index);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.am.BaseAppStateTimeEvents, com.android.server.am.BaseAppStateEvents
    void trimEvents(long earliest, int index) {
        trimEvents(earliest, (java.util.LinkedList) this.mEvents[index]);
    }

    void trimEvents(long earliest, java.util.LinkedList<T> events) {
        if (events == null) {
            return;
        }
        while (events.size() > 1) {
            T current = events.peek();
            if (current.getTimestamp() >= earliest) {
                return;
            }
            if (events.get(1).getTimestamp() > earliest) {
                events.get(0).trimTo(earliest);
                return;
            } else {
                events.pop();
                events.pop();
            }
        }
        if (events.size() == 1) {
            events.get(0).trimTo(java.lang.Math.max(earliest, events.peek().getTimestamp()));
        }
    }

    @Override // com.android.server.am.BaseAppStateTimeEvents, com.android.server.am.BaseAppStateEvents
    java.util.LinkedList<T> add(java.util.LinkedList<T> linkedList, java.util.LinkedList<T> linkedList2) {
        T t;
        long timestamp;
        if (linkedList2 == null || linkedList2.size() == 0) {
            return linkedList;
        }
        if (linkedList == null || linkedList.size() == 0) {
            return (java.util.LinkedList) linkedList2.clone();
        }
        java.util.Iterator<T> it = linkedList.iterator();
        java.util.Iterator<T> it2 = linkedList2.iterator();
        T next = it.next();
        T next2 = it2.next();
        com.android.server.am.CachedAppOptimizer.AnonymousClass4 anonymousClass4 = (java.util.LinkedList<T>) new java.util.LinkedList();
        boolean z = false;
        boolean z2 = false;
        long timestamp2 = next.getTimestamp();
        long timestamp3 = next2.getTimestamp();
        while (true) {
            long timestamp4 = Long.MAX_VALUE;
            if (timestamp2 != Long.MAX_VALUE || timestamp3 != Long.MAX_VALUE) {
                boolean z3 = z || z2;
                if (timestamp2 == timestamp3) {
                    t = next;
                    z = !z;
                    z2 = !z2;
                    if (it.hasNext()) {
                        T next3 = it.next();
                        next = next3;
                        timestamp = next3.getTimestamp();
                    } else {
                        timestamp = Long.MAX_VALUE;
                    }
                    timestamp2 = timestamp;
                    if (it2.hasNext()) {
                        T next4 = it2.next();
                        next2 = next4;
                        timestamp4 = next4.getTimestamp();
                    }
                    timestamp3 = timestamp4;
                } else if (timestamp2 < timestamp3) {
                    t = next;
                    z = !z;
                    if (it.hasNext()) {
                        T next5 = it.next();
                        next = next5;
                        timestamp4 = next5.getTimestamp();
                    }
                    timestamp2 = timestamp4;
                } else {
                    t = next2;
                    z2 = !z2;
                    if (it2.hasNext()) {
                        T next6 = it2.next();
                        next2 = next6;
                        timestamp4 = next6.getTimestamp();
                    }
                    timestamp3 = timestamp4;
                }
                if (z3 != (z || z2)) {
                    anonymousClass4.add((com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent) t.clone());
                }
            } else {
                return anonymousClass4;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void subtract(com.android.server.am.BaseAppStateDurations otherDurations, int thisIndex, int otherIndex) {
        if (this.mEvents.length <= thisIndex || this.mEvents[thisIndex] == null || otherDurations.mEvents.length <= otherIndex || otherDurations.mEvents[otherIndex] == null) {
            return;
        }
        this.mEvents[thisIndex] = subtract((java.util.LinkedList) this.mEvents[thisIndex], (java.util.LinkedList) otherDurations.mEvents[otherIndex]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void subtract(com.android.server.am.BaseAppStateDurations otherDurations, int otherIndex) {
        if (otherDurations.mEvents.length <= otherIndex || otherDurations.mEvents[otherIndex] == null) {
            return;
        }
        for (int i = 0; i < this.mEvents.length; i++) {
            if (this.mEvents[i] != null) {
                this.mEvents[i] = subtract((java.util.LinkedList) this.mEvents[i], (java.util.LinkedList) otherDurations.mEvents[otherIndex]);
            }
        }
    }

    java.util.LinkedList<T> subtract(java.util.LinkedList<T> linkedList, java.util.LinkedList<T> linkedList2) {
        T t;
        long timestamp;
        if (linkedList2 == null || linkedList2.size() == 0 || linkedList == null || linkedList.size() == 0) {
            return linkedList;
        }
        java.util.Iterator<T> it = linkedList.iterator();
        java.util.Iterator<T> it2 = linkedList2.iterator();
        T next = it.next();
        T next2 = it2.next();
        com.android.server.am.CachedAppOptimizer.AnonymousClass4 anonymousClass4 = (java.util.LinkedList<T>) new java.util.LinkedList();
        boolean z = false;
        boolean z2 = false;
        long timestamp2 = next.getTimestamp();
        long timestamp3 = next2.getTimestamp();
        while (true) {
            long timestamp4 = Long.MAX_VALUE;
            if (timestamp2 != Long.MAX_VALUE || timestamp3 != Long.MAX_VALUE) {
                boolean z3 = z && !z2;
                if (timestamp2 == timestamp3) {
                    t = next;
                    z = !z;
                    z2 = !z2;
                    if (it.hasNext()) {
                        T next3 = it.next();
                        next = next3;
                        timestamp = next3.getTimestamp();
                    } else {
                        timestamp = Long.MAX_VALUE;
                    }
                    timestamp2 = timestamp;
                    if (it2.hasNext()) {
                        T next4 = it2.next();
                        next2 = next4;
                        timestamp4 = next4.getTimestamp();
                    }
                    timestamp3 = timestamp4;
                } else if (timestamp2 < timestamp3) {
                    t = next;
                    z = !z;
                    if (it.hasNext()) {
                        T next5 = it.next();
                        next = next5;
                        timestamp4 = next5.getTimestamp();
                    }
                    timestamp2 = timestamp4;
                } else {
                    t = next2;
                    z2 = !z2;
                    if (it2.hasNext()) {
                        T next6 = it2.next();
                        next2 = next6;
                        timestamp4 = next6.getTimestamp();
                    }
                    timestamp3 = timestamp4;
                }
                if (z3 != (z && !z2)) {
                    anonymousClass4.add((com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent) t.clone());
                }
            } else {
                return anonymousClass4;
            }
        }
    }

    long getTotalDurations(long now, int index) {
        return getTotalDurationsSince(getEarliest(0L), now, index);
    }

    long getTotalDurationsSince(long since, long now, int index) {
        java.util.LinkedList linkedList = this.mEvents[index];
        if (linkedList == null || linkedList.size() == 0) {
            return 0L;
        }
        boolean active = true;
        long last = 0;
        long duration = 0;
        java.util.Iterator it = linkedList.iterator();
        while (true) {
            boolean z = true;
            if (!it.hasNext()) {
                break;
            }
            com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent baseTimeEvent = (com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent) it.next();
            if (baseTimeEvent.getTimestamp() >= since && !active) {
                duration += java.lang.Math.max(0L, baseTimeEvent.getTimestamp() - java.lang.Math.max(last, since));
            } else {
                last = baseTimeEvent.getTimestamp();
            }
            if (active) {
                z = false;
            }
            active = z;
        }
        if ((linkedList.size() & 1) == 1) {
            return duration + java.lang.Math.max(0L, now - java.lang.Math.max(last, since));
        }
        return duration;
    }

    boolean isActive(int index) {
        return this.mEvents[index] != null && (this.mEvents[index].size() & 1) == 1;
    }

    @Override // com.android.server.am.BaseAppStateEvents
    java.lang.String formatEventSummary(long now, int index) {
        return android.util.TimeUtils.formatDuration(getTotalDurations(now, index));
    }

    @Override // com.android.server.am.BaseAppStateEvents
    public java.lang.String toString() {
        return this.mPackageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.UserHandle.formatUid(this.mUid) + " isActive[0]=" + isActive(0) + " totalDurations[0]=" + getTotalDurations(android.os.SystemClock.elapsedRealtime(), 0);
    }
}
