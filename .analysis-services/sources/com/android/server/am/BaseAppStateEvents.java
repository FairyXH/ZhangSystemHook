package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
abstract class BaseAppStateEvents<E> {
    static final boolean DEBUG_BASE_APP_STATE_EVENTS = false;
    final java.util.LinkedList<E>[] mEvents;
    int mExemptReason = -1;
    final com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig mMaxTrackingDurationConfig;
    final java.lang.String mPackageName;
    final java.lang.String mTag;
    final int mUid;

    interface Factory<T extends com.android.server.am.BaseAppStateEvents> {
        T createAppStateEvents(int i, java.lang.String str);

        T createAppStateEvents(T t);
    }

    interface MaxTrackingDurationConfig {
        long getMaxTrackingDuration();
    }

    abstract java.util.LinkedList<E> add(java.util.LinkedList<E> linkedList, java.util.LinkedList<E> linkedList2);

    abstract int getTotalEventsSince(long j, long j2, int i);

    abstract void trimEvents(long j, int i);

    BaseAppStateEvents(int uid, java.lang.String packageName, int numOfEventTypes, java.lang.String tag, com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig) {
        this.mUid = uid;
        this.mPackageName = packageName;
        this.mTag = tag;
        this.mMaxTrackingDurationConfig = maxTrackingDurationConfig;
        this.mEvents = new java.util.LinkedList[numOfEventTypes];
    }

    BaseAppStateEvents(com.android.server.am.BaseAppStateEvents other) {
        this.mUid = other.mUid;
        this.mPackageName = other.mPackageName;
        this.mTag = other.mTag;
        this.mMaxTrackingDurationConfig = other.mMaxTrackingDurationConfig;
        this.mEvents = new java.util.LinkedList[other.mEvents.length];
        for (int i = 0; i < this.mEvents.length; i++) {
            if (other.mEvents[i] != null) {
                this.mEvents[i] = new java.util.LinkedList<>(other.mEvents[i]);
            }
        }
    }

    void addEvent(E event, long now, int index) {
        if (this.mEvents[index] == null) {
            this.mEvents[index] = new java.util.LinkedList<>();
        }
        java.util.LinkedList<E> events = this.mEvents[index];
        events.add(event);
        trimEvents(getEarliest(now), index);
    }

    void trim(long earliest) {
        for (int i = 0; i < this.mEvents.length; i++) {
            trimEvents(earliest, i);
        }
    }

    boolean isEmpty() {
        for (int i = 0; i < this.mEvents.length; i++) {
            if (this.mEvents[i] != null && !this.mEvents[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    boolean isEmpty(int index) {
        return this.mEvents[index] == null || this.mEvents[index].isEmpty();
    }

    void add(com.android.server.am.BaseAppStateEvents other) {
        if (this.mEvents.length != other.mEvents.length) {
            return;
        }
        for (int i = 0; i < this.mEvents.length; i++) {
            this.mEvents[i] = add(this.mEvents[i], other.mEvents[i]);
        }
    }

    java.util.LinkedList<E> getRawEvents(int index) {
        return this.mEvents[index];
    }

    int getTotalEvents(long now, int index) {
        return getTotalEventsSince(getEarliest(0L), now, index);
    }

    long getEarliest(long now) {
        return java.lang.Math.max(0L, now - this.mMaxTrackingDurationConfig.getMaxTrackingDuration());
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix, long nowElapsed) {
        for (int i = 0; i < this.mEvents.length; i++) {
            if (this.mEvents[i] != null) {
                pw.print(prefix);
                pw.print(formatEventTypeLabel(i));
                pw.println(formatEventSummary(nowElapsed, i));
            }
        }
    }

    java.lang.String formatEventSummary(long now, int index) {
        return java.lang.Integer.toString(getTotalEvents(now, index));
    }

    java.lang.String formatEventTypeLabel(int index) {
        return java.lang.Integer.toString(index) + ":";
    }

    public java.lang.String toString() {
        return this.mPackageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.UserHandle.formatUid(this.mUid) + " totalEvents[0]=" + formatEventSummary(android.os.SystemClock.elapsedRealtime(), 0);
    }
}
