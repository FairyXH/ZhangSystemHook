package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
final class TwoFingersDownOrSwipe extends com.android.server.accessibility.gestures.GestureMatcher {
    private final int mDetectionDurationMillis;
    private final int mDoubleTapTimeout;
    private android.view.MotionEvent mFirstPointerDown;
    private android.view.MotionEvent mSecondPointerDown;
    private final int mSwipeMinDistance;

    TwoFingersDownOrSwipe(android.content.Context context) {
        super(101, new android.os.Handler(context.getMainLooper()), null);
        this.mDetectionDurationMillis = com.android.server.accessibility.magnification.MagnificationGestureMatcher.getMagnificationMultiTapTimeout(context);
        this.mDoubleTapTimeout = android.view.ViewConfiguration.getDoubleTapTimeout();
        this.mSwipeMinDistance = android.view.ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        this.mFirstPointerDown = android.view.MotionEvent.obtain(event);
        cancelAfter(this.mDetectionDurationMillis, event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mFirstPointerDown == null) {
            cancelGesture(event, rawEvent, policyFlags);
        }
        if (event.getPointerCount() == 2) {
            this.mSecondPointerDown = android.view.MotionEvent.obtain(event);
            completeAfter(this.mDoubleTapTimeout, event, rawEvent, policyFlags);
        } else {
            cancelGesture(event, rawEvent, policyFlags);
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onMove(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mFirstPointerDown == null || this.mSecondPointerDown == null) {
            return;
        }
        if (distance(this.mFirstPointerDown, event) > this.mSwipeMinDistance) {
            completeGesture(event, rawEvent, policyFlags);
        } else if (distance(this.mSecondPointerDown, event) > this.mSwipeMinDistance) {
            completeGesture(event, rawEvent, policyFlags);
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        cancelGesture(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        cancelGesture(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public void clear() {
        if (this.mFirstPointerDown != null) {
            this.mFirstPointerDown.recycle();
            this.mFirstPointerDown = null;
        }
        if (this.mSecondPointerDown != null) {
            this.mSecondPointerDown.recycle();
            this.mSecondPointerDown = null;
        }
        super.clear();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected java.lang.String getGestureName() {
        return getClass().getSimpleName();
    }

    private static double distance(android.view.MotionEvent downEvent, android.view.MotionEvent moveEvent) {
        int downActionIndex = downEvent.getActionIndex();
        int downPointerId = downEvent.getPointerId(downActionIndex);
        int moveActionIndex = moveEvent.findPointerIndex(downPointerId);
        if (moveActionIndex < 0) {
            return -1.0d;
        }
        return android.util.MathUtils.dist(downEvent.getX(downActionIndex), downEvent.getY(downActionIndex), moveEvent.getX(moveActionIndex), moveEvent.getY(moveActionIndex));
    }
}
