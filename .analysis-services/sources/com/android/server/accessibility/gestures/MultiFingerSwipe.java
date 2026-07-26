package com.android.server.accessibility.gestures;

/* JADX INFO: loaded from: classes.dex */
class MultiFingerSwipe extends com.android.server.accessibility.gestures.GestureMatcher {
    public static final int DOWN = 3;
    public static final int LEFT = 0;
    private static final float MIN_CM_BETWEEN_SAMPLES = 0.25f;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    private android.graphics.PointF[] mBase;
    private int mCurrentFingerCount;
    private int mDirection;
    private final float mMinPixelsBetweenSamplesX;
    private final float mMinPixelsBetweenSamplesY;
    private int[] mPointerIds;
    private android.graphics.PointF[] mPreviousGesturePoint;
    private final java.util.ArrayList<android.graphics.PointF>[] mStrokeBuffers;
    private int mTargetFingerCount;
    private boolean mTargetFingerCountReached;
    private int mTouchSlop;

    MultiFingerSwipe(android.content.Context context, int fingerCount, int direction, int gesture, com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener listener) {
        super(gesture, new android.os.Handler(context.getMainLooper()), listener);
        this.mTargetFingerCountReached = false;
        this.mTargetFingerCount = fingerCount;
        this.mPointerIds = new int[this.mTargetFingerCount];
        this.mBase = new android.graphics.PointF[this.mTargetFingerCount];
        this.mPreviousGesturePoint = new android.graphics.PointF[this.mTargetFingerCount];
        this.mStrokeBuffers = new java.util.ArrayList[this.mTargetFingerCount];
        this.mDirection = direction;
        android.util.DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float pixelsPerCmX = displayMetrics.xdpi / com.android.server.accessibility.gestures.GestureUtils.CM_PER_INCH;
        float pixelsPerCmY = displayMetrics.ydpi / com.android.server.accessibility.gestures.GestureUtils.CM_PER_INCH;
        this.mMinPixelsBetweenSamplesX = pixelsPerCmX * MIN_CM_BETWEEN_SAMPLES;
        this.mMinPixelsBetweenSamplesY = MIN_CM_BETWEEN_SAMPLES * pixelsPerCmY;
        this.mTouchSlop = android.view.ViewConfiguration.get(context).getScaledTouchSlop();
        clear();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public void clear() {
        this.mTargetFingerCountReached = false;
        this.mCurrentFingerCount = 0;
        for (int i = 0; i < this.mTargetFingerCount; i++) {
            this.mPointerIds[i] = -1;
            if (this.mBase[i] == null) {
                this.mBase[i] = new android.graphics.PointF();
            }
            this.mBase[i].x = Float.NaN;
            this.mBase[i].y = Float.NaN;
            if (this.mPreviousGesturePoint[i] == null) {
                this.mPreviousGesturePoint[i] = new android.graphics.PointF();
            }
            this.mPreviousGesturePoint[i].x = Float.NaN;
            this.mPreviousGesturePoint[i].y = Float.NaN;
            if (this.mStrokeBuffers[i] == null) {
                this.mStrokeBuffers[i] = new java.util.ArrayList<>(100);
            }
            this.mStrokeBuffers[i].clear();
        }
        super.clear();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mCurrentFingerCount > 0) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        this.mCurrentFingerCount = 1;
        int actionIndex = com.android.server.accessibility.gestures.GestureUtils.getActionIndex(rawEvent);
        int pointerId = rawEvent.getPointerId(actionIndex);
        int pointerIndex = rawEvent.getPointerCount() - 1;
        if (pointerId < 0) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        if (this.mPointerIds[pointerIndex] != -1) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        this.mPointerIds[pointerIndex] = pointerId;
        if (java.lang.Float.isNaN(this.mBase[pointerIndex].x) && java.lang.Float.isNaN(this.mBase[pointerIndex].y)) {
            float x = rawEvent.getX(actionIndex);
            float y = rawEvent.getY(actionIndex);
            if (x < 0.0f || y < 0.0f) {
                cancelGesture(event, rawEvent, policyFlags);
                return;
            }
            this.mBase[pointerIndex].x = x;
            this.mBase[pointerIndex].y = y;
            this.mPreviousGesturePoint[pointerIndex].x = x;
            this.mPreviousGesturePoint[pointerIndex].y = y;
            return;
        }
        cancelGesture(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (event.getPointerCount() > this.mTargetFingerCount) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        this.mCurrentFingerCount++;
        if (this.mCurrentFingerCount != rawEvent.getPointerCount()) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        if (this.mCurrentFingerCount == this.mTargetFingerCount) {
            this.mTargetFingerCountReached = true;
        }
        int actionIndex = com.android.server.accessibility.gestures.GestureUtils.getActionIndex(rawEvent);
        int pointerId = rawEvent.getPointerId(actionIndex);
        if (pointerId < 0) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        int pointerIndex = this.mCurrentFingerCount - 1;
        if (this.mPointerIds[pointerIndex] != -1) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        this.mPointerIds[pointerIndex] = pointerId;
        if (java.lang.Float.isNaN(this.mBase[pointerIndex].x) && java.lang.Float.isNaN(this.mBase[pointerIndex].y)) {
            float x = rawEvent.getX(actionIndex);
            float y = rawEvent.getY(actionIndex);
            if (x < 0.0f || y < 0.0f) {
                cancelGesture(event, rawEvent, policyFlags);
                return;
            }
            this.mBase[pointerIndex].x = x;
            this.mBase[pointerIndex].y = y;
            this.mPreviousGesturePoint[pointerIndex].x = x;
            this.mPreviousGesturePoint[pointerIndex].y = y;
            return;
        }
        cancelGesture(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (!this.mTargetFingerCountReached) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        this.mCurrentFingerCount--;
        int actionIndex = com.android.server.accessibility.gestures.GestureUtils.getActionIndex(event);
        int pointerId = event.getPointerId(actionIndex);
        if (pointerId < 0) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        int pointerIndex = java.util.Arrays.binarySearch(this.mPointerIds, pointerId);
        if (pointerIndex < 0) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        float x = rawEvent.getX(actionIndex);
        float y = rawEvent.getY(actionIndex);
        if (x < 0.0f || y < 0.0f) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        float dX = java.lang.Math.abs(x - this.mPreviousGesturePoint[pointerIndex].x);
        float dY = java.lang.Math.abs(y - this.mPreviousGesturePoint[pointerIndex].y);
        if (dX >= this.mMinPixelsBetweenSamplesX || dY >= this.mMinPixelsBetweenSamplesY) {
            this.mStrokeBuffers[pointerIndex].add(new android.graphics.PointF(x, y));
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onMove(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        for (int pointerIndex = 0; pointerIndex < this.mTargetFingerCount; pointerIndex++) {
            if (this.mPointerIds[pointerIndex] != -1) {
                if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                    android.util.Slog.d(getGestureName(), "Processing move on finger " + pointerIndex);
                }
                int index = rawEvent.findPointerIndex(this.mPointerIds[pointerIndex]);
                if (index < 0) {
                    if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                        android.util.Slog.d(getGestureName(), "Finger " + pointerIndex + " not found in this event. skipping.");
                    }
                } else {
                    float x = rawEvent.getX(index);
                    float y = rawEvent.getY(index);
                    if (x < 0.0f || y < 0.0f) {
                        cancelGesture(event, rawEvent, policyFlags);
                        return;
                    }
                    float dX = java.lang.Math.abs(x - this.mPreviousGesturePoint[pointerIndex].x);
                    float dY = java.lang.Math.abs(y - this.mPreviousGesturePoint[pointerIndex].y);
                    double moveDelta = java.lang.Math.hypot(java.lang.Math.abs(x - this.mBase[pointerIndex].x), java.lang.Math.abs(y - this.mBase[pointerIndex].y));
                    if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                        android.util.Slog.d(getGestureName(), "moveDelta:" + moveDelta);
                    }
                    if (getState() == 0) {
                        if (moveDelta < this.mTargetFingerCount * this.mTouchSlop) {
                            continue;
                        } else if (this.mCurrentFingerCount == this.mTargetFingerCount) {
                            int direction = toDirection(x - this.mBase[pointerIndex].x, y - this.mBase[pointerIndex].y);
                            if (direction != this.mDirection) {
                                cancelGesture(event, rawEvent, policyFlags);
                                return;
                            }
                            startGesture(event, rawEvent, policyFlags);
                            for (int i = 0; i < this.mTargetFingerCount; i++) {
                                this.mStrokeBuffers[i].add(new android.graphics.PointF(this.mBase[i]));
                            }
                        } else {
                            cancelGesture(event, rawEvent, policyFlags);
                            return;
                        }
                    } else if (getState() == 1) {
                        int direction2 = toDirection(x - this.mBase[pointerIndex].x, y - this.mBase[pointerIndex].y);
                        if (direction2 != this.mDirection) {
                            cancelGesture(event, rawEvent, policyFlags);
                            return;
                        } else if (dX >= this.mMinPixelsBetweenSamplesX || dY >= this.mMinPixelsBetweenSamplesY) {
                            this.mPreviousGesturePoint[pointerIndex].x = x;
                            this.mPreviousGesturePoint[pointerIndex].y = y;
                            this.mStrokeBuffers[pointerIndex].add(new android.graphics.PointF(x, y));
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (getState() != 1) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        this.mCurrentFingerCount = 0;
        int actionIndex = com.android.server.accessibility.gestures.GestureUtils.getActionIndex(event);
        int pointerId = event.getPointerId(actionIndex);
        int pointerIndex = java.util.Arrays.binarySearch(this.mPointerIds, pointerId);
        if (pointerIndex < 0) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        float x = rawEvent.getX(actionIndex);
        float y = rawEvent.getY(actionIndex);
        if (x < 0.0f || y < 0.0f) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        float dX = java.lang.Math.abs(x - this.mPreviousGesturePoint[pointerIndex].x);
        float dY = java.lang.Math.abs(y - this.mPreviousGesturePoint[pointerIndex].y);
        if (dX >= this.mMinPixelsBetweenSamplesX || dY >= this.mMinPixelsBetweenSamplesY) {
            this.mStrokeBuffers[pointerIndex].add(new android.graphics.PointF(x, y));
        }
        recognizeGesture(event, rawEvent, policyFlags);
    }

    private void recognizeGesture(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        for (int i = 0; i < this.mTargetFingerCount; i++) {
            if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                android.util.Slog.d(getGestureName(), "Recognizing finger: " + i);
            }
            if (this.mStrokeBuffers[i].size() < 2) {
                android.util.Slog.d(getGestureName(), "Too few points.");
                cancelGesture(event, rawEvent, policyFlags);
                return;
            }
            java.util.ArrayList<android.graphics.PointF> path = this.mStrokeBuffers[i];
            if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                android.util.Slog.d(getGestureName(), "path=" + path.toString());
            }
            if (!recognizeGesturePath(event, rawEvent, policyFlags, path)) {
                cancelGesture(event, rawEvent, policyFlags);
                return;
            }
        }
        completeGesture(event, rawEvent, policyFlags);
    }

    private boolean recognizeGesturePath(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags, java.util.ArrayList<android.graphics.PointF> path) {
        event.getDisplayId();
        for (int i = 0; i < path.size() - 1; i++) {
            android.graphics.PointF start = path.get(i);
            android.graphics.PointF end = path.get(i + 1);
            float dX = end.x - start.x;
            float dY = end.y - start.y;
            int direction = toDirection(dX, dY);
            if (direction != this.mDirection) {
                if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                    android.util.Slog.d(getGestureName(), "Found direction " + directionToString(direction) + " when expecting " + directionToString(this.mDirection));
                    return false;
                }
                return false;
            }
        }
        if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
            android.util.Slog.d(getGestureName(), "Completed.");
        }
        return true;
    }

    private static int toDirection(float dX, float dY) {
        return java.lang.Math.abs(dX) > java.lang.Math.abs(dY) ? dX < 0.0f ? 0 : 1 : dY < 0.0f ? 2 : 3;
    }

    public static java.lang.String directionToString(int direction) {
        switch (direction) {
            case 0:
                return "left";
            case 1:
                return "right";
            case 2:
                return android.net.INetd.IF_STATE_UP;
            case 3:
                return android.net.INetd.IF_STATE_DOWN;
            default:
                return "Unknown Direction";
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected java.lang.String getGestureName() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append(this.mTargetFingerCount).append("-finger ");
        builder.append("Swipe ").append(directionToString(this.mDirection));
        return builder.toString();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder(super.toString());
        if (getState() != 3) {
            builder.append(", mBase: ").append(java.util.Arrays.toString(this.mBase)).append(", mMinPixelsBetweenSamplesX:").append(this.mMinPixelsBetweenSamplesX).append(", mMinPixelsBetweenSamplesY:").append(this.mMinPixelsBetweenSamplesY);
        }
        return builder.toString();
    }
}
