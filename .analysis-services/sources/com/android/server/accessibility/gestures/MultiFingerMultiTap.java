package com.android.server.accessibility.gestures;

/* JADX INFO: loaded from: classes.dex */
public class MultiFingerMultiTap extends com.android.server.accessibility.gestures.GestureMatcher {
    private android.graphics.PointF[] mBases;
    protected int mCompletedTapCount;
    private int mDoubleTapSlop;
    private java.util.ArrayList<android.graphics.PointF> mExcludedPointsForDownSlopChecked;
    protected boolean mIsTargetFingerCountReached;
    final int mTargetFingerCount;
    final int mTargetTapCount;
    private int mTouchSlop;

    public MultiFingerMultiTap(android.content.Context context, int fingers, int taps, int gestureId, com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener listener) {
        super(gestureId, new android.os.Handler(context.getMainLooper()), listener);
        this.mIsTargetFingerCountReached = false;
        com.android.internal.util.Preconditions.checkArgument(fingers >= 2);
        com.android.internal.util.Preconditions.checkArgumentPositive(taps, "Tap count must greater than 0.");
        this.mTargetTapCount = taps;
        this.mTargetFingerCount = fingers;
        this.mDoubleTapSlop = android.view.ViewConfiguration.get(context).getScaledDoubleTapSlop() * fingers;
        this.mTouchSlop = android.view.ViewConfiguration.get(context).getScaledTouchSlop() * fingers;
        this.mBases = new android.graphics.PointF[this.mTargetFingerCount];
        for (int i = 0; i < this.mBases.length; i++) {
            this.mBases[i] = new android.graphics.PointF();
        }
        this.mExcludedPointsForDownSlopChecked = new java.util.ArrayList<>(this.mTargetFingerCount);
        clear();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public void clear() {
        this.mCompletedTapCount = 0;
        this.mIsTargetFingerCountReached = false;
        for (int i = 0; i < this.mBases.length; i++) {
            this.mBases[i].set(Float.NaN, Float.NaN);
        }
        this.mExcludedPointsForDownSlopChecked.clear();
        super.clear();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mCompletedTapCount == this.mTargetTapCount) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        cancelAfterTapTimeout(event, rawEvent, policyFlags);
        if (this.mCompletedTapCount == 0) {
            initBaseLocation(rawEvent);
            return;
        }
        android.graphics.PointF nearest = findNearestPoint(rawEvent, this.mDoubleTapSlop, true);
        if (nearest != null) {
            int index = event.getActionIndex();
            nearest.set(event.getX(index), event.getY(index));
        } else {
            cancelGesture(event, rawEvent, policyFlags);
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        cancelAfterDoubleTapTimeout(event, rawEvent, policyFlags);
        android.graphics.PointF nearest = findNearestPoint(rawEvent, this.mTouchSlop, false);
        if ((getState() == 1 || getState() == 0) && nearest != null) {
            if (this.mIsTargetFingerCountReached) {
                this.mCompletedTapCount++;
                this.mIsTargetFingerCountReached = false;
                this.mExcludedPointsForDownSlopChecked.clear();
            }
            if (this.mCompletedTapCount == 1) {
                startGesture(event, rawEvent, policyFlags);
            }
            if (this.mCompletedTapCount == this.mTargetTapCount) {
                completeAfterDoubleTapTimeout(event, rawEvent, policyFlags);
                return;
            }
            return;
        }
        cancelGesture(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onMove(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (findNearestPoint(rawEvent, this.mTouchSlop, false) == null) {
            cancelGesture(event, rawEvent, policyFlags);
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        android.graphics.PointF nearest;
        cancelAfterTapTimeout(event, rawEvent, policyFlags);
        int currentFingerCount = event.getPointerCount();
        if (currentFingerCount > this.mTargetFingerCount || this.mIsTargetFingerCountReached) {
            this.mIsTargetFingerCountReached = false;
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        if (this.mCompletedTapCount == 0) {
            nearest = initBaseLocation(rawEvent);
        } else {
            nearest = findNearestPoint(rawEvent, this.mDoubleTapSlop, true);
        }
        if ((getState() == 1 || getState() == 0) && nearest != null) {
            if (currentFingerCount == this.mTargetFingerCount) {
                this.mIsTargetFingerCountReached = true;
            }
            int index = event.getActionIndex();
            nearest.set(event.getX(index), event.getY(index));
            return;
        }
        cancelGesture(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (!this.mIsTargetFingerCountReached) {
            cancelGesture(event, rawEvent, policyFlags);
        } else if (getState() == 1 || getState() == 0) {
            cancelAfterTapTimeout(event, rawEvent, policyFlags);
        } else {
            cancelGesture(event, rawEvent, policyFlags);
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public java.lang.String getGestureName() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append(this.mTargetFingerCount).append("-Finger ");
        if (this.mTargetTapCount == 1) {
            builder.append("Single");
        } else if (this.mTargetTapCount == 2) {
            builder.append("Double");
        } else if (this.mTargetTapCount == 3) {
            builder.append("Triple");
        } else if (this.mTargetTapCount > 3) {
            builder.append(this.mTargetTapCount);
        }
        return builder.append(" Tap").toString();
    }

    private android.graphics.PointF initBaseLocation(android.view.MotionEvent event) {
        int index = event.getActionIndex();
        int baseIndex = event.getPointerCount() - 1;
        android.graphics.PointF p = this.mBases[baseIndex];
        if (java.lang.Float.isNaN(p.x) && java.lang.Float.isNaN(p.y)) {
            p.set(event.getX(index), event.getY(index));
        }
        return p;
    }

    private android.graphics.PointF findNearestPoint(android.view.MotionEvent event, float slop, boolean filterMatched) {
        float moveDelta = Float.MAX_VALUE;
        android.graphics.PointF nearest = null;
        for (int i = 0; i < this.mBases.length; i++) {
            android.graphics.PointF p = this.mBases[i];
            if ((!java.lang.Float.isNaN(p.x) || !java.lang.Float.isNaN(p.y)) && (!filterMatched || !this.mExcludedPointsForDownSlopChecked.contains(p))) {
                int index = event.getActionIndex();
                float dX = p.x - event.getX(index);
                float dY = p.y - event.getY(index);
                if (dX == 0.0f && dY == 0.0f) {
                    if (filterMatched) {
                        this.mExcludedPointsForDownSlopChecked.add(p);
                    }
                    return p;
                }
                float delta = (float) java.lang.Math.hypot(dX, dY);
                if (moveDelta > delta) {
                    moveDelta = delta;
                    nearest = p;
                }
            }
        }
        if (moveDelta < slop) {
            if (filterMatched) {
                this.mExcludedPointsForDownSlopChecked.add(nearest);
            }
            return nearest;
        }
        return null;
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder(super.toString());
        if (getState() != 3) {
            builder.append(", CompletedTapCount: ");
            builder.append(this.mCompletedTapCount);
            builder.append(", IsTargetFingerCountReached: ");
            builder.append(this.mIsTargetFingerCountReached);
            builder.append(", Bases: ");
            builder.append(java.util.Arrays.toString(this.mBases));
            builder.append(", ExcludedPointsForDownSlopChecked: ");
            builder.append(this.mExcludedPointsForDownSlopChecked.toString());
        }
        return builder.toString();
    }
}
