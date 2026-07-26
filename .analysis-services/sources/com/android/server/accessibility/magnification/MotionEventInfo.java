package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
final class MotionEventInfo {
    public android.view.MotionEvent mEvent;
    public int mPolicyFlags;
    public android.view.MotionEvent mRawEvent;

    static com.android.server.accessibility.magnification.MotionEventInfo obtain(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        return new com.android.server.accessibility.magnification.MotionEventInfo(android.view.MotionEvent.obtain(event), android.view.MotionEvent.obtain(rawEvent), policyFlags);
    }

    MotionEventInfo(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        this.mEvent = event;
        this.mRawEvent = rawEvent;
        this.mPolicyFlags = policyFlags;
    }

    void recycle() {
        this.mEvent = recycleAndNullify(this.mEvent);
        this.mRawEvent = recycleAndNullify(this.mRawEvent);
    }

    public java.lang.String toString() {
        return android.view.MotionEvent.actionToString(this.mEvent.getAction()).replace("ACTION_", "");
    }

    private static android.view.MotionEvent recycleAndNullify(android.view.MotionEvent event) {
        if (event != null) {
            event.recycle();
            return null;
        }
        return null;
    }
}
