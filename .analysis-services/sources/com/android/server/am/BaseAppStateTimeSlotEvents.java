package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
class BaseAppStateTimeSlotEvents extends com.android.server.am.BaseAppStateEvents<java.lang.Integer> {
    static final boolean DEBUG_BASE_APP_TIME_SLOT_EVENTS = false;
    long[] mCurSlotStartTime;
    final long mTimeSlotSize;

    BaseAppStateTimeSlotEvents(int uid, java.lang.String packageName, int numOfEventTypes, long timeslotSize, java.lang.String tag, com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig) {
        super(uid, packageName, numOfEventTypes, tag, maxTrackingDurationConfig);
        this.mTimeSlotSize = timeslotSize;
        this.mCurSlotStartTime = new long[numOfEventTypes];
    }

    BaseAppStateTimeSlotEvents(com.android.server.am.BaseAppStateTimeSlotEvents other) {
        super(other);
        this.mTimeSlotSize = other.mTimeSlotSize;
        this.mCurSlotStartTime = new long[other.mCurSlotStartTime.length];
        for (int i = 0; i < this.mCurSlotStartTime.length; i++) {
            this.mCurSlotStartTime[i] = other.mCurSlotStartTime[i];
        }
    }

    @Override // com.android.server.am.BaseAppStateEvents
    java.util.LinkedList<java.lang.Integer> add(java.util.LinkedList<java.lang.Integer> events, java.util.LinkedList<java.lang.Integer> otherEvents) {
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.am.BaseAppStateEvents
    void add(com.android.server.am.BaseAppStateEvents otherObj) {
        if (otherObj == null || !(otherObj instanceof com.android.server.am.BaseAppStateTimeSlotEvents)) {
            return;
        }
        com.android.server.am.BaseAppStateTimeSlotEvents other = (com.android.server.am.BaseAppStateTimeSlotEvents) otherObj;
        if (this.mEvents.length != other.mEvents.length) {
            return;
        }
        for (int i = 0; i < this.mEvents.length; i++) {
            java.util.LinkedList linkedList = other.mEvents[i];
            if (linkedList != null && linkedList.size() != 0) {
                java.util.LinkedList linkedList2 = this.mEvents[i];
                if (linkedList2 == null || linkedList2.size() == 0) {
                    java.util.LinkedList linkedList3 = linkedList;
                    this.mEvents[i] = new java.util.LinkedList(linkedList3);
                    this.mCurSlotStartTime[i] = other.mCurSlotStartTime[i];
                } else {
                    java.util.LinkedList<java.lang.Integer> dest = new java.util.LinkedList<>();
                    java.util.Iterator<java.lang.Integer> itl = linkedList2.iterator();
                    java.util.Iterator<java.lang.Integer> itr = linkedList.iterator();
                    long maxl = this.mCurSlotStartTime[i];
                    long maxr = other.mCurSlotStartTime[i];
                    com.android.server.am.BaseAppStateTimeSlotEvents other2 = other;
                    long minl = maxl - (this.mTimeSlotSize * ((long) (linkedList2.size() - 1)));
                    long minr = maxr - (this.mTimeSlotSize * ((long) (linkedList.size() - 1)));
                    long latest = java.lang.Math.max(maxl, maxr);
                    long earliest = java.lang.Math.min(minl, minr);
                    long start = earliest;
                    while (start <= latest) {
                        int iIntValue = 0;
                        int iIntValue2 = (start < minl || start > maxl) ? 0 : itl.next().intValue();
                        if (start >= minr && start <= maxr) {
                            iIntValue = itr.next().intValue();
                        }
                        dest.add(java.lang.Integer.valueOf(iIntValue2 + iIntValue));
                        start += this.mTimeSlotSize;
                        minl = minl;
                    }
                    this.mEvents[i] = dest;
                    if (maxl >= maxr) {
                        other = other2;
                    } else {
                        other = other2;
                        this.mCurSlotStartTime[i] = other.mCurSlotStartTime[i];
                    }
                    trimEvents(getEarliest(this.mCurSlotStartTime[i]), i);
                }
            }
        }
    }

    @Override // com.android.server.am.BaseAppStateEvents
    int getTotalEventsSince(long since, long now, int index) {
        java.util.LinkedList linkedList = this.mEvents[index];
        if (linkedList == null || linkedList.size() == 0) {
            return 0;
        }
        long start = getSlotStartTime(since);
        if (start <= this.mCurSlotStartTime[index]) {
            long end = java.lang.Math.min(getSlotStartTime(now), this.mCurSlotStartTime[index]);
            java.util.Iterator<java.lang.Integer> it = linkedList.descendingIterator();
            int count = 0;
            long time = this.mCurSlotStartTime[index];
            while (time >= start && it.hasNext()) {
                int val = it.next().intValue();
                if (time <= end) {
                    count += val;
                }
                time -= this.mTimeSlotSize;
            }
            return count;
        }
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    void addEvent(long now, int index) {
        long slot = getSlotStartTime(now);
        java.util.LinkedList linkedList = this.mEvents[index];
        if (linkedList == null) {
            linkedList = new java.util.LinkedList();
            this.mEvents[index] = linkedList;
        }
        if (linkedList.size() == 0) {
            linkedList.add(1);
        } else {
            long start = this.mCurSlotStartTime[index];
            while (start < slot) {
                linkedList.add(0);
                start += this.mTimeSlotSize;
            }
            linkedList.offerLast(java.lang.Integer.valueOf(((java.lang.Integer) linkedList.pollLast()).intValue() + 1));
        }
        this.mCurSlotStartTime[index] = slot;
        trimEvents(getEarliest(now), index);
    }

    @Override // com.android.server.am.BaseAppStateEvents
    void trimEvents(long earliest, int index) {
        java.util.LinkedList linkedList = this.mEvents[index];
        if (linkedList == null || linkedList.size() == 0) {
            return;
        }
        long slot = getSlotStartTime(earliest);
        long time = this.mCurSlotStartTime[index] - (this.mTimeSlotSize * ((long) (linkedList.size() - 1)));
        while (time < slot && linkedList.size() > 0) {
            linkedList.pop();
            time += this.mTimeSlotSize;
        }
    }

    long getSlotStartTime(long timestamp) {
        return timestamp - (timestamp % this.mTimeSlotSize);
    }

    long getCurrentSlotStartTime(int index) {
        return this.mCurSlotStartTime[index];
    }
}
