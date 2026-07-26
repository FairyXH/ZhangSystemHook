package com.android.server.accessibility.gestures;

/* JADX INFO: loaded from: classes.dex */
class Swipe extends com.android.server.accessibility.gestures.GestureMatcher {
    private static final float ANGLE_THRESHOLD = 0.0f;
    public static final int DOWN = 3;
    public static final int GESTURE_CONFIRM_CM = 1;
    public static final int LEFT = 0;
    public static final long MAX_TIME_TO_CONTINUE_SWIPE_MS = 350;
    public static final long MAX_TIME_TO_START_SWIPE_MS = 150;
    private static final float MIN_CM_BETWEEN_SAMPLES = 0.25f;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    private long mBaseTime;
    private float mBaseX;
    private float mBaseY;
    private int[] mDirections;
    private final float mGestureDetectionThresholdPixels;
    private final float mMinPixelsBetweenSamplesX;
    private final float mMinPixelsBetweenSamplesY;
    private float mPreviousGestureX;
    private float mPreviousGestureY;
    private final java.util.ArrayList<android.graphics.PointF> mStrokeBuffer;
    private int mTouchSlop;

    Swipe(android.content.Context context, int direction, int gesture, com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener listener) {
        this(context, new int[]{direction}, gesture, listener);
    }

    Swipe(android.content.Context context, int direction1, int direction2, int gesture, com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener listener) {
        this(context, new int[]{direction1, direction2}, gesture, listener);
    }

    private Swipe(android.content.Context context, int[] directions, int gesture, com.android.server.accessibility.gestures.GestureMatcher.StateChangeListener listener) {
        super(gesture, new android.os.Handler(context.getMainLooper()), listener);
        this.mStrokeBuffer = new java.util.ArrayList<>(100);
        this.mDirections = directions;
        android.util.DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.mGestureDetectionThresholdPixels = android.util.TypedValue.applyDimension(5, com.android.server.accessibility.gestures.GestureUtils.MM_PER_CM, displayMetrics) * 1.0f;
        float pixelsPerCmX = displayMetrics.xdpi / 2.54f;
        float pixelsPerCmY = displayMetrics.ydpi / 2.54f;
        this.mMinPixelsBetweenSamplesX = pixelsPerCmX * MIN_CM_BETWEEN_SAMPLES;
        this.mMinPixelsBetweenSamplesY = MIN_CM_BETWEEN_SAMPLES * pixelsPerCmY;
        this.mTouchSlop = android.view.ViewConfiguration.get(context).getScaledTouchSlop();
        clear();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public void clear() {
        this.mBaseX = Float.NaN;
        this.mBaseY = Float.NaN;
        this.mBaseTime = 0L;
        this.mPreviousGestureX = Float.NaN;
        this.mPreviousGestureY = Float.NaN;
        this.mStrokeBuffer.clear();
        super.clear();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (java.lang.Float.isNaN(this.mBaseX) && java.lang.Float.isNaN(this.mBaseY)) {
            this.mBaseX = rawEvent.getX();
            this.mBaseY = rawEvent.getY();
            this.mBaseTime = rawEvent.getEventTime();
            this.mPreviousGestureX = this.mBaseX;
            this.mPreviousGestureY = this.mBaseY;
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onMove(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        float x = rawEvent.getX();
        float y = rawEvent.getY();
        long time = rawEvent.getEventTime();
        float dX = java.lang.Math.abs(x - this.mPreviousGestureX);
        float dY = java.lang.Math.abs(y - this.mPreviousGestureY);
        double moveDelta = java.lang.Math.hypot(java.lang.Math.abs(x - this.mBaseX), java.lang.Math.abs(y - this.mBaseY));
        long timeDelta = time - this.mBaseTime;
        if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
            android.util.Slog.d(getGestureName(), "moveDelta:" + java.lang.Double.toString(moveDelta) + " mGestureDetectionThreshold: " + java.lang.Float.toString(this.mGestureDetectionThresholdPixels));
        }
        if (getState() == 0) {
            if (moveDelta < this.mTouchSlop) {
                return;
            }
            if (this.mStrokeBuffer.size() == 0) {
                int direction = toDirection(x - this.mBaseX, y - this.mBaseY);
                if (direction == this.mDirections[0]) {
                    this.mStrokeBuffer.add(new android.graphics.PointF(this.mBaseX, this.mBaseY));
                } else {
                    cancelGesture(event, rawEvent, policyFlags);
                    return;
                }
            }
        }
        if (moveDelta > this.mGestureDetectionThresholdPixels) {
            this.mBaseX = x;
            this.mBaseY = y;
            this.mBaseTime = time;
            startGesture(event, rawEvent, policyFlags);
        } else if (getState() == 0) {
            if (timeDelta > 150) {
                cancelGesture(event, rawEvent, policyFlags);
                return;
            }
        } else if (getState() == 1 && timeDelta > 350) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        if (dX >= this.mMinPixelsBetweenSamplesX || dY >= this.mMinPixelsBetweenSamplesY) {
            this.mPreviousGestureX = x;
            this.mPreviousGestureY = y;
            this.mStrokeBuffer.add(new android.graphics.PointF(x, y));
        }
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (getState() != 1) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        float x = rawEvent.getX();
        float y = rawEvent.getY();
        float dX = java.lang.Math.abs(x - this.mPreviousGestureX);
        float dY = java.lang.Math.abs(y - this.mPreviousGestureY);
        if (dX >= this.mMinPixelsBetweenSamplesX || dY >= this.mMinPixelsBetweenSamplesY) {
            this.mStrokeBuffer.add(new android.graphics.PointF(x, y));
        }
        recognizeGesture(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerDown(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        cancelGesture(event, rawEvent, policyFlags);
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    protected void onPointerUp(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        cancelGesture(event, rawEvent, policyFlags);
    }

    private void recognizeGesture(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags) {
        if (this.mStrokeBuffer.size() < 2) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        java.util.ArrayList<android.graphics.PointF> path = new java.util.ArrayList<>();
        android.graphics.PointF lastDelimiter = this.mStrokeBuffer.get(0);
        path.add(lastDelimiter);
        float dX = ANGLE_THRESHOLD;
        float dY = ANGLE_THRESHOLD;
        int count = 0;
        float length = ANGLE_THRESHOLD;
        android.graphics.PointF next = null;
        for (int i = 1; i < this.mStrokeBuffer.size(); i++) {
            android.graphics.PointF next2 = this.mStrokeBuffer.get(i);
            next = next2;
            if (count > 0) {
                float currentDX = dX / count;
                float currentDY = dY / count;
                android.graphics.PointF newDelimiter = new android.graphics.PointF((length * currentDX) + lastDelimiter.x, (length * currentDY) + lastDelimiter.y);
                float nextDX = next.x - newDelimiter.x;
                float nextDY = next.y - newDelimiter.y;
                float nextLength = (float) java.lang.Math.sqrt((nextDX * nextDX) + (nextDY * nextDY));
                float dot = (currentDX * (nextDX / nextLength)) + (currentDY * (nextDY / nextLength));
                if (dot < ANGLE_THRESHOLD) {
                    path.add(newDelimiter);
                    lastDelimiter = newDelimiter;
                    dX = ANGLE_THRESHOLD;
                    dY = ANGLE_THRESHOLD;
                    count = 0;
                }
            }
            float currentDX2 = next.x - lastDelimiter.x;
            float currentDY2 = next.y - lastDelimiter.y;
            length = (float) java.lang.Math.sqrt((currentDX2 * currentDX2) + (currentDY2 * currentDY2));
            count++;
            dX += currentDX2 / length;
            dY += currentDY2 / length;
        }
        path.add(next);
        if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
            android.util.Slog.d(getGestureName(), "path=" + path.toString());
        }
        recognizeGesturePath(event, rawEvent, policyFlags, path);
    }

    private void recognizeGesturePath(android.view.MotionEvent event, android.view.MotionEvent rawEvent, int policyFlags, java.util.ArrayList<android.graphics.PointF> path) {
        event.getDisplayId();
        if (path.size() != this.mDirections.length + 1) {
            cancelGesture(event, rawEvent, policyFlags);
            return;
        }
        for (int i = 0; i < path.size() - 1; i++) {
            android.graphics.PointF start = path.get(i);
            android.graphics.PointF end = path.get(i + 1);
            float dX = end.x - start.x;
            float dY = end.y - start.y;
            int direction = toDirection(dX, dY);
            if (direction != this.mDirections[i]) {
                if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
                    android.util.Slog.d(getGestureName(), "Found direction " + directionToString(direction) + " when expecting " + directionToString(this.mDirections[i]));
                }
                cancelGesture(event, rawEvent, policyFlags);
                return;
            }
        }
        if (com.android.server.accessibility.gestures.TouchExplorer.DEBUG) {
            android.util.Slog.d(getGestureName(), "Completed.");
        }
        completeGesture(event, rawEvent, policyFlags);
    }

    private static int toDirection(float dX, float dY) {
        return java.lang.Math.abs(dX) > java.lang.Math.abs(dY) ? dX < ANGLE_THRESHOLD ? 0 : 1 : dY < ANGLE_THRESHOLD ? 2 : 3;
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
        builder.append("Swipe ").append(directionToString(this.mDirections[0]));
        for (int i = 1; i < this.mDirections.length; i++) {
            builder.append(" and ").append(directionToString(this.mDirections[i]));
        }
        return builder.toString();
    }

    @Override // com.android.server.accessibility.gestures.GestureMatcher
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder(super.toString());
        if (getState() != 3) {
            builder.append(", mBaseX: ").append(this.mBaseX).append(", mBaseY: ").append(this.mBaseY).append(", mGestureDetectionThreshold:").append(this.mGestureDetectionThresholdPixels).append(", mMinPixelsBetweenSamplesX:").append(this.mMinPixelsBetweenSamplesX).append(", mMinPixelsBetweenSamplesY:").append(this.mMinPixelsBetweenSamplesY);
        }
        return builder.toString();
    }
}
