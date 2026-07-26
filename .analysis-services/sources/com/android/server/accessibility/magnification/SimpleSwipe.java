package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
class SimpleSwipe extends com.android.server.accessibility.gestures.GestureMatcher {
    private final int mDetectionDurationMillis;
    private android.view.MotionEvent mLastDown;
    private final int mSwipeMinDistance;

    SimpleSwipe(android.content.Context context) {
        super(102, new android.os.Handler(context.getMainLooper()), null);
        this.mSwipeMinDistance = android.view.ViewConfiguration.get(context).getScaledTouchSlop();
        this.mDetectionDurationMillis = com.android.server.accessibility.magnification.MagnificationGestureMatcher.getMagnificationMultiTapTimeout(context);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        this.mLastDown = android.view.MotionEvent.obtain(event);
        cancelAfter(this.mDetectionDurationMillis, event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        cancelGesture(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onMove(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (gestureMatched(event, rawEvent, policyFlags)) {
            completeGesture(event, rawEvent, policyFlags);
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (gestureMatched(event, rawEvent, policyFlags)) {
            completeGesture(event, rawEvent, policyFlags);
        } else {
            cancelGesture(event, rawEvent, policyFlags);
        }
    }

    private boolean gestureMatched(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        return this.mLastDown != null && com.android.server.accessibility.gestures.GestureUtils.distance(this.mLastDown, event) > ((double) this.mSwipeMinDistance);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public void clear() {
        if (this.mLastDown != null) {
            this.mLastDown.recycle();
        }
        this.mLastDown = null;
        super.clear();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected java.lang.String getGestureName() {
        return getClass().getSimpleName();
    }
}
