package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
class MagnificationGesturesObserver implements com.android.server.accessibility.magnification.GesturesObserver.Listener {
    private final com.android.server.accessibility.magnification.MagnificationGesturesObserver.Callback mCallback;
    private java.util.List<com.android.server.accessibility.magnification.MotionEventInfo> mDelayedEventQueue;
    private final com.android.server.accessibility.magnification.GesturesObserver mGesturesObserver;
    private long mLastDownEventTime = 0;
    private android.view.MotionEvent mLastEvent;
    private static final java.lang.String LOG_TAG = "MagnificationGesturesObserver";
    private static final boolean DBG = android.util.Log.isLoggable(LOG_TAG, 3);

    interface Callback {
        void onGestureCancelled(long j, java.util.List<com.android.server.accessibility.magnification.MotionEventInfo> list, android.view.MotionEvent motionEvent);

        void onGestureCompleted(int i, long j, java.util.List<com.android.server.accessibility.magnification.MotionEventInfo> list, android.view.MotionEvent motionEvent);

        boolean shouldStopDetection(android.view.MotionEvent motionEvent);
    }

    MagnificationGesturesObserver(com.android.server.accessibility.magnification.MagnificationGesturesObserver.Callback callback, com.android.server.accessibility.gestures.GestureMatcher... matchers) {
        this.mGesturesObserver = new com.android.server.accessibility.magnification.GesturesObserver(this, matchers);
        this.mCallback = callback;
    }

    boolean onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (DBG) {
            android.util.Slog.d(LOG_TAG, "DetectGesture: event = " + event);
        }
        cacheDelayedMotionEvent(event, rawEvent, policyFlags);
        if (this.mCallback.shouldStopDetection(event)) {
            notifyDetectionCancel();
            return false;
        }
        if (event.getActionMasked() == 0) {
            this.mLastDownEventTime = event.getDownTime();
        }
        return this.mGesturesObserver.onMotionEvent(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.magnification.GesturesObserver.Listener
    public void onGestureCompleted(int gestureId, android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (DBG) {
            android.util.Slog.d(LOG_TAG, "onGestureCompleted: " + com.android.server.accessibility.magnification.MagnificationGestureMatcher.gestureIdToString(gestureId) + " event = " + event);
        }
        java.util.List<com.android.server.accessibility.magnification.MotionEventInfo> delayEventQueue = this.mDelayedEventQueue;
        this.mDelayedEventQueue = null;
        this.mCallback.onGestureCompleted(gestureId, this.mLastDownEventTime, delayEventQueue, event);
        clear();
    }

    @Override // com.android.server.accessibility.magnification.GesturesObserver.Listener
    public void onGestureCancelled(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (DBG) {
            android.util.Slog.d(LOG_TAG, "onGestureCancelled:  event = " + event);
        }
        notifyDetectionCancel();
    }

    private void notifyDetectionCancel() {
        java.util.List<com.android.server.accessibility.magnification.MotionEventInfo> delayEventQueue = this.mDelayedEventQueue;
        this.mDelayedEventQueue = null;
        this.mCallback.onGestureCancelled(this.mLastDownEventTime, delayEventQueue, this.mLastEvent);
        clear();
    }

    private void clear() {
        if (DBG) {
            android.util.Slog.d(LOG_TAG, "clear:" + this.mDelayedEventQueue);
        }
        recycleLastEvent();
        this.mLastDownEventTime = 0L;
        if (this.mDelayedEventQueue != null) {
            for (com.android.server.accessibility.magnification.MotionEventInfo eventInfo2 : this.mDelayedEventQueue) {
                eventInfo2.recycle();
            }
            this.mDelayedEventQueue.clear();
            this.mDelayedEventQueue = null;
        }
    }

    private void recycleLastEvent() {
        if (this.mLastEvent == null) {
            return;
        }
        this.mLastEvent.recycle();
        this.mLastEvent = null;
    }

    private void cacheDelayedMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        this.mLastEvent = android.view.MotionEvent.obtain(event);
        com.android.server.accessibility.magnification.MotionEventInfo info = com.android.server.accessibility.magnification.MotionEventInfo.obtain(event, rawEvent, policyFlags);
        if (this.mDelayedEventQueue == null) {
            this.mDelayedEventQueue = new java.util.LinkedList();
        }
        this.mDelayedEventQueue.add(info);
    }

    public java.lang.String toString() {
        return "MagnificationGesturesObserver{mDelayedEventQueue=" + this.mDelayedEventQueue + '}';
    }
}
