package com.android.server.accessibility.gestures;

/* JADX INFO: loaded from: classes.dex */
class SecondFingerMultiTap extends com.android.server.accessibility.gestures.GestureMatcher {
    float mBaseX;
    float mBaseY;
    int mCurrentTaps;
    int mDoubleTapSlop;
    int mDoubleTapTimeout;
    int mSecondFingerPointerId;
    int mTapTimeout;
    final int mTargetTaps;
    int mTouchSlop;

    SecondFingerMultiTap(android.content.Context context, int taps, int gesture, com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener listener) {
        super(gesture, new android.os.Handler(context.getMainLooper()), listener);
        this.mTargetTaps = taps;
        this.mDoubleTapSlop = android.view.ViewConfiguration.get(context).getScaledDoubleTapSlop();
        this.mTouchSlop = android.view.ViewConfiguration.get(context).getScaledTouchSlop();
        this.mTapTimeout = android.view.ViewConfiguration.getTapTimeout();
        this.mDoubleTapTimeout = android.view.ViewConfiguration.getDoubleTapTimeout();
        clear();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public void clear() {
        this.mCurrentTaps = 0;
        this.mBaseX = Float.NaN;
        this.mBaseY = Float.NaN;
        this.mSecondFingerPointerId = -1;
        super.clear();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (event.getPointerCount() > 2) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        int index = com.android.server.accessibility.gestures.GestureUtils.getActionIndex(event);
        this.mSecondFingerPointerId = event.getPointerId(index);
        cancelAfterTapTimeout(event, rawEvent, policyFlags);
        if (java.lang.Float.isNaN(this.mBaseX) && java.lang.Float.isNaN(this.mBaseY)) {
            this.mBaseX = event.getX();
            this.mBaseY = event.getY();
        }
        if (!isSecondFingerInsideSlop(rawEvent, this.mDoubleTapSlop)) {
            cancelGesture(event, rawEvent, policyFlags);
        }
        this.mBaseX = event.getX();
        this.mBaseY = event.getY();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (event.getPointerCount() > 2) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        cancelAfterDoubleTapTimeout(event, rawEvent, policyFlags);
        if (!isSecondFingerInsideSlop(rawEvent, this.mTouchSlop)) {
            cancelGesture(event, rawEvent, policyFlags);
        }
        if (getState() == 1 || getState() == 0) {
            this.mCurrentTaps++;
            if (this.mCurrentTaps == this.mTargetTaps) {
                completeGesture(event, rawEvent, policyFlags);
                return;
            } else {
                cancelAfterDoubleTapTimeout(event, rawEvent, policyFlags);
                return;
            }
        }
        cancelGesture(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onMove(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        switch (event.getPointerCount()) {
            case 1:
                break;
            case 2:
                if (!isSecondFingerInsideSlop(rawEvent, this.mTouchSlop)) {
                    cancelGesture(event, rawEvent, policyFlags);
                }
                break;
            default:
                cancelGesture(event, rawEvent, policyFlags);
                break;
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        cancelGesture(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public java.lang.String getGestureName() {
        switch (this.mTargetTaps) {
            case 2:
                return "Second Finger Double Tap";
            case 3:
                return "Second Finger Triple Tap";
            default:
                return "Second Finger " + java.lang.Integer.toString(this.mTargetTaps) + " Taps";
        }
    }

    private boolean isSecondFingerInsideSlop(android.view.MotionEvent rawEvent, int slop) {
        int pointerIndex = rawEvent.findPointerIndex(this.mSecondFingerPointerId);
        if (pointerIndex == -1) {
            return false;
        }
        float deltaX = this.mBaseX - rawEvent.getX(pointerIndex);
        float deltaY = this.mBaseY - rawEvent.getY(pointerIndex);
        if (deltaX == 0.0f && deltaY == 0.0f) {
            return true;
        }
        double moveDelta = java.lang.Math.hypot(deltaX, deltaY);
        return moveDelta <= ((double) slop);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public java.lang.String toString() {
        return super.toString() + ", Taps:" + this.mCurrentTaps + ", mBaseX: " + java.lang.Float.toString(this.mBaseX) + ", mBaseY: " + java.lang.Float.toString(this.mBaseY);
    }
}
