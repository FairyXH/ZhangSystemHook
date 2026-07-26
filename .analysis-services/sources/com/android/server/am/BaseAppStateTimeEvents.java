package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
class BaseAppStateTimeEvents<T extends com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent> extends com.android.server.am.BaseAppStateEvents<T> {
    BaseAppStateTimeEvents(int uid, java.lang.String packageName, int numOfEventTypes, java.lang.String tag, com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig) {
        super(uid, packageName, numOfEventTypes, tag, maxTrackingDurationConfig);
    }

    BaseAppStateTimeEvents(com.android.server.am.BaseAppStateTimeEvents other) {
        super(other);
    }

    @Override // com.android.server.am.BaseAppStateEvents
    java.util.LinkedList<T> add(java.util.LinkedList<T> linkedList, java.util.LinkedList<T> linkedList2) {
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
        long timestamp2 = next.getTimestamp();
        long timestamp3 = next2.getTimestamp();
        while (true) {
            long timestamp4 = Long.MAX_VALUE;
            if (timestamp2 == Long.MAX_VALUE && timestamp3 == Long.MAX_VALUE) {
                return anonymousClass4;
            }
            if (timestamp2 == timestamp3) {
                anonymousClass4.add((com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent) next.clone());
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
                anonymousClass4.add((com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent) next.clone());
                if (it.hasNext()) {
                    T next5 = it.next();
                    next = next5;
                    timestamp4 = next5.getTimestamp();
                }
                timestamp2 = timestamp4;
            } else {
                anonymousClass4.add((com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent) next2.clone());
                if (it2.hasNext()) {
                    T next6 = it2.next();
                    next2 = next6;
                    timestamp4 = next6.getTimestamp();
                }
                timestamp3 = timestamp4;
            }
        }
    }

    @Override // com.android.server.am.BaseAppStateEvents
    int getTotalEventsSince(long since, long now, int index) {
        java.util.LinkedList linkedList = this.mEvents[index];
        if (linkedList == null || linkedList.size() == 0) {
            return 0;
        }
        int count = 0;
        java.util.Iterator it = linkedList.iterator();
        while (it.hasNext()) {
            if (((com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent) it.next()).getTimestamp() >= since) {
                count++;
            }
        }
        return count;
    }

    @Override // com.android.server.am.BaseAppStateEvents
    void trimEvents(long earliest, int index) {
        java.util.LinkedList linkedList = this.mEvents[index];
        if (linkedList == null) {
            return;
        }
        while (linkedList.size() > 0 && ((com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent) linkedList.peek()).getTimestamp() < earliest) {
            linkedList.pop();
        }
    }

    static class BaseTimeEvent implements java.lang.Cloneable {
        long mTimestamp;

        BaseTimeEvent(long timestamp) {
            this.mTimestamp = timestamp;
        }

        BaseTimeEvent(com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent other) {
            this.mTimestamp = other.mTimestamp;
        }

        void trimTo(long timestamp) {
            this.mTimestamp = timestamp;
        }

        long getTimestamp() {
            return this.mTimestamp;
        }

        public java.lang.Object clone() {
            return new com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent(this);
        }

        public boolean equals(java.lang.Object other) {
            return other != null && other.getClass() == com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent.class && ((com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent) other).mTimestamp == this.mTimestamp;
        }

        public int hashCode() {
            return java.lang.Long.hashCode(this.mTimestamp);
        }
    }
}
