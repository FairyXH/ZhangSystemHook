package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
class MotionEventDispatcherDelegate {
    private final com.android.server.accessibility.magnification.MotionEventDispatcherDelegate.EventDispatcher mEventDispatcher;
    private long mLastDelegatedDownEventTime;
    private final int mMultiTapMaxDelay;
    private static final java.lang.String TAG = com.android.server.accessibility.magnification.MotionEventDispatcherDelegate.class.getSimpleName();
    private static final boolean DBG = android.util.Log.isLoggable(TAG, 3);

    interface EventDispatcher {
        void dispatchMotionEvent(android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, int i);
    }

    MotionEventDispatcherDelegate(android.content.Context context, com.android.server.accessibility.magnification.MotionEventDispatcherDelegate.EventDispatcher eventDispatcher) {
        this.mEventDispatcher = eventDispatcher;
        this.mMultiTapMaxDelay = android.view.ViewConfiguration.getDoubleTapTimeout() + context.getResources().getInteger(android.R.integer.config_reduceBrightColorsStrengthMin);
    }

    void sendDelayedMotionEvents(java.util.List<com.android.server.accessibility.magnification.MotionEventInfo> delayedEventQueue, long lastDetectingDownEventTime) {
        if (delayedEventQueue == null) {
            return;
        }
        long offset = java.lang.Math.min(android.os.SystemClock.uptimeMillis() - lastDetectingDownEventTime, this.mMultiTapMaxDelay);
        for (com.android.server.accessibility.magnification.MotionEventInfo info : delayedEventQueue) {
            info.mEvent.setDownTime(info.mEvent.getDownTime() + offset);
            dispatchMotionEvent(info.mEvent, info.mRawEvent, info.mPolicyFlags);
            info.recycle();
        }
    }

    void dispatchMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (event.getActionMasked() == 0) {
            this.mLastDelegatedDownEventTime = event.getDownTime();
            if (DBG) {
                android.util.Log.d(TAG, "dispatchMotionEvent mLastDelegatedDownEventTime time = " + this.mLastDelegatedDownEventTime);
            }
        }
        if (DBG) {
            android.util.Log.d(TAG, "dispatchMotionEvent original down time = " + event.getDownTime());
        }
        event.setDownTime(this.mLastDelegatedDownEventTime);
        this.mEventDispatcher.dispatchMotionEvent(event, rawEvent, policyFlags);
    }
}
