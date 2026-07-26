package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class HbmEvent {
    private long mEndTimeMillis;
    private long mStartTimeMillis;

    HbmEvent(long startTimeMillis, long endTimeMillis) {
        this.mStartTimeMillis = startTimeMillis;
        this.mEndTimeMillis = endTimeMillis;
    }

    public long getStartTimeMillis() {
        return this.mStartTimeMillis;
    }

    public long getEndTimeMillis() {
        return this.mEndTimeMillis;
    }

    public java.lang.String toString() {
        return "HbmEvent: {startTimeMillis:" + this.mStartTimeMillis + ", endTimeMillis: " + this.mEndTimeMillis + "}, total: " + ((this.mEndTimeMillis - this.mStartTimeMillis) / 1000) + "]";
    }
}
