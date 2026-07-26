package com.android.server.accessibility.gestures;

/* JADX INFO: loaded from: classes.dex */
public abstract class GestureMatcher {
    public static final int STATE_CLEAR = 0;
    public static final int STATE_GESTURE_CANCELED = 3;
    public static final int STATE_GESTURE_COMPLETED = 2;
    public static final int STATE_GESTURE_STARTED = 1;
    private final int mGestureId;
    private final android.os.Handler mHandler;
    private com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener mListener;
    private int mState = 0;
    protected final com.android.server.accessibility.gestures.GestureMatcher.DelayedTransition mDelayedTransition = new com.android.server.accessibility.gestures.GestureMatcher.DelayedTransition();

    public @interface State {
    }

    public interface StateChangeListener {
        void onStateChanged(int i, int i2, android.view.MotionEvent motionEvent, android.view.MotionEvent motionEvent2, int i3);
    }

    protected abstract java.lang.String getGestureName();

    protected GestureMatcher(int gestureId, android.os.Handler handler, com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener listener) {
        this.mGestureId = gestureId;
        this.mHandler = handler;
        this.mListener = listener;
    }

    public void clear() {
        this.mState = 0;
        cancelPendingTransitions();
    }

    public final int getState() {
        return this.mState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setState(int state, android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        this.mState = state;
        cancelPendingTransitions();
        if (this.mListener != null) {
            this.mListener.onStateChanged(this.mGestureId, this.mState, event, rawEvent, policyFlags);
        }
    }

    protected final void startGesture(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        setState(1, event, rawEvent, policyFlags);
    }

    protected final void cancelGesture(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        setState(3, event, rawEvent, policyFlags);
    }

    protected final void completeGesture(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        setState(2, event, rawEvent, policyFlags);
    }

    public final void setListener(com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener listener) {
        this.mListener = listener;
    }

    public int getGestureId() {
        return this.mGestureId;
    }

    public final int onMotionEvent(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mState == 3 || this.mState == 2) {
            return this.mState;
        }
        switch (event.getActionMasked()) {
            case 0:
                onDown(event, rawEvent, policyFlags);
                break;
            case 1:
                onUp(event, rawEvent, policyFlags);
                break;
            case 2:
                onMove(event, rawEvent, policyFlags);
                break;
            case 3:
            case 4:
            default:
                setState(3, event, rawEvent, policyFlags);
                break;
            case 5:
                onPointerDown(event, rawEvent, policyFlags);
                break;
            case 6:
                onPointerUp(event, rawEvent, policyFlags);
                break;
        }
        return this.mState;
    }

    protected void onDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
    }

    protected void onPointerDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
    }

    protected void onMove(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
    }

    protected void onPointerUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
    }

    protected void onUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
    }

    protected void cancelAfterTapTimeout(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        cancelAfter(android.view.ViewConfiguration.getTapTimeout(), event, rawEvent, policyFlags);
    }

    protected final void cancelAfterDoubleTapTimeout(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        cancelAfter(android.view.ViewConfiguration.getDoubleTapTimeout(), event, rawEvent, policyFlags);
    }

    protected final void cancelAfter(long timeout, android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        this.mDelayedTransition.cancel();
        this.mDelayedTransition.post(3, timeout, event, rawEvent, policyFlags);
    }

    protected final void cancelPendingTransitions() {
        this.mDelayedTransition.cancel();
    }

    protected final void completeAfterLongPressTimeout(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        completeAfter(android.view.ViewConfiguration.getLongPressTimeout(), event, rawEvent, policyFlags);
    }

    protected final void completeAfterTapTimeout(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        completeAfter(android.view.ViewConfiguration.getTapTimeout(), event, rawEvent, policyFlags);
    }

    protected final void completeAfter(long timeout, android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        this.mDelayedTransition.cancel();
        this.mDelayedTransition.post(2, timeout, event, rawEvent, policyFlags);
    }

    protected final void completeAfterDoubleTapTimeout(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        completeAfter(android.view.ViewConfiguration.getDoubleTapTimeout(), event, rawEvent, policyFlags);
    }

    static java.lang.String getStateSymbolicName(int state) {
        switch (state) {
            case 0:
                return "STATE_CLEAR";
            case 1:
                return "STATE_GESTURE_STARTED";
            case 2:
                return "STATE_GESTURE_COMPLETED";
            case 3:
                return "STATE_GESTURE_CANCELED";
            default:
                return "Unknown state: " + state;
        }
    }

    public java.lang.String toString() {
        return getGestureName() + ":" + getStateSymbolicName(this.mState);
    }

    protected final class DelayedTransition implements java.lang.Runnable {
        private static final java.lang.String LOG_TAG = "GestureMatcher.DelayedTransition";
        android.view.MotionEvent mEvent;
        int mPolicyFlags;
        android.view.MotionEvent mRawEvent;
        int mTargetState;

        protected DelayedTransition() {
        }

        public void cancel() {
            if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG && isPending()) {
                android.util.Slog.d(LOG_TAG, com.android.server.accessibility.gestures.GestureMatcher.this.getGestureName() + ": canceling delayed transition to " + com.android.server.accessibility.gestures.GestureMatcher.getStateSymbolicName(this.mTargetState));
            }
            com.android.server.accessibility.gestures.GestureMatcher.this.mHandler.removeCallbacks(this);
            recycleEvent();
        }

        public void post(int state, long delay, android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
            recycleEvent();
            this.mTargetState = state;
            if (android.view.accessibility.Flags.copyEventsForGestureDetection()) {
                this.mEvent = event.copy();
                this.mRawEvent = rawEvent.copy();
            } else {
                this.mEvent = event;
                this.mRawEvent = rawEvent;
            }
            this.mPolicyFlags = policyFlags;
            com.android.server.accessibility.gestures.GestureMatcher.this.mHandler.postDelayed(this, delay);
            if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                android.util.Slog.d(LOG_TAG, com.android.server.accessibility.gestures.GestureMatcher.this.getGestureName() + ": posting delayed transition to " + com.android.server.accessibility.gestures.GestureMatcher.getStateSymbolicName(this.mTargetState));
            }
        }

        public boolean isPending() {
            return com.android.server.accessibility.gestures.GestureMatcher.this.mHandler.hasCallbacks(this);
        }

        public void forceSendAndRemove() {
            if (isPending()) {
                run();
                cancel();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                android.util.Slog.d(LOG_TAG, com.android.server.accessibility.gestures.GestureMatcher.this.getGestureName() + ": executing delayed transition to " + com.android.server.accessibility.gestures.GestureMatcher.getStateSymbolicName(this.mTargetState));
            }
            com.android.server.accessibility.gestures.GestureMatcher.this.setState(this.mTargetState, this.mEvent, this.mRawEvent, this.mPolicyFlags);
            recycleEvent();
        }

        private void recycleEvent() {
            if (!android.view.accessibility.Flags.copyEventsForGestureDetection() || this.mEvent == null || this.mRawEvent == null) {
                return;
            }
            this.mEvent.recycle();
            this.mRawEvent.recycle();
            this.mEvent = null;
            this.mRawEvent = null;
        }
    }
}
