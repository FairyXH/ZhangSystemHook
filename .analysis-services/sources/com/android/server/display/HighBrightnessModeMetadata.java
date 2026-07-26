package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class HighBrightnessModeMetadata {
    private final java.util.ArrayDeque<com.android.server.display.HbmEvent> mEvents = new java.util.ArrayDeque<>();
    private long mRunningStartTimeMillis = -1;

    HighBrightnessModeMetadata() {
    }

    public long getRunningStartTimeMillis() {
        return this.mRunningStartTimeMillis;
    }

    public void setRunningStartTimeMillis(long setTime) {
        this.mRunningStartTimeMillis = setTime;
    }

    public java.util.ArrayDeque<com.android.server.display.HbmEvent> getHbmEventQueue() {
        return this.mEvents;
    }

    public void addHbmEvent(com.android.server.display.HbmEvent hbmEvent) {
        this.mEvents.addFirst(hbmEvent);
    }
}
