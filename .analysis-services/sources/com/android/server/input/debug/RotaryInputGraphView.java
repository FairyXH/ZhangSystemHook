package com.android.server.input.debug;

/* JADX INFO: loaded from: classes2.dex */
public class RotaryInputGraphView extends android.view.View {
    private static final float DEFAULT_FRAME_CENTER_POSITION = 0.0f;
    private static final int FRAME_BORDER_GAP_SP = 10;
    private static final int FRAME_COLOR = -1082909881;
    private static final int FRAME_TEXT_OFFSET_SP = 2;
    private static final int FRAME_TEXT_SIZE_SP = 10;
    private static final int FRAME_WIDTH_SP = 2;
    private static final int GRAPH_COLOR = -65281;
    private static final int GRAPH_LINE_WIDTH_SP = 1;
    private static final int GRAPH_POINT_RADIUS_SP = 4;
    private static final int MAX_GRAPH_VALUES_SIZE = 400;
    private final java.util.Locale mDefaultLocale;
    private final android.util.DisplayMetrics mDm;
    private float mFrameCenterPosition;
    private final float mFrameCenterToBorderDistance;
    private final android.graphics.Paint mFramePaint;
    private final android.graphics.Paint mFrameTextPaint;
    private final android.graphics.Paint mGraphLinePaint;
    private final android.graphics.Paint mGraphPointPaint;
    private final com.android.server.input.debug.RotaryInputGraphView.CyclicBuffer mGraphValues;
    private final float mScaledVerticalScrollFactor;
    private static final long MAX_SHOWN_TIME_INTERVAL = java.util.concurrent.TimeUnit.SECONDS.toMillis(5);
    private static final long MAX_GESTURE_TIME = java.util.concurrent.TimeUnit.SECONDS.toMillis(1);

    public RotaryInputGraphView(android.content.Context c) {
        super(c);
        this.mDefaultLocale = java.util.Locale.getDefault();
        this.mFramePaint = new android.graphics.Paint();
        this.mFrameTextPaint = new android.graphics.Paint();
        this.mGraphLinePaint = new android.graphics.Paint();
        this.mGraphPointPaint = new android.graphics.Paint();
        this.mGraphValues = new com.android.server.input.debug.RotaryInputGraphView.CyclicBuffer(400);
        this.mFrameCenterPosition = DEFAULT_FRAME_CENTER_POSITION;
        this.mDm = this.mContext.getResources().getDisplayMetrics();
        this.mFrameCenterToBorderDistance = this.mDm.heightPixels;
        this.mScaledVerticalScrollFactor = android.view.ViewConfiguration.get(c).getScaledVerticalScrollFactor();
        this.mFramePaint.setColor(FRAME_COLOR);
        this.mFramePaint.setStrokeWidth(applyDimensionSp(2, this.mDm));
        this.mFrameTextPaint.setColor(GRAPH_COLOR);
        this.mFrameTextPaint.setTextSize(applyDimensionSp(10, this.mDm));
        this.mGraphLinePaint.setColor(GRAPH_COLOR);
        this.mGraphLinePaint.setStrokeWidth(applyDimensionSp(1, this.mDm));
        this.mGraphLinePaint.setStrokeCap(android.graphics.Paint.Cap.ROUND);
        this.mGraphLinePaint.setStrokeJoin(android.graphics.Paint.Join.ROUND);
        this.mGraphPointPaint.setColor(GRAPH_COLOR);
        this.mGraphPointPaint.setStrokeWidth(applyDimensionSp(4, this.mDm));
        this.mGraphPointPaint.setStrokeCap(android.graphics.Paint.Cap.ROUND);
        this.mGraphPointPaint.setStrokeJoin(android.graphics.Paint.Join.ROUND);
    }

    public void addValue(float scrollAxisValue, long eventTime) {
        while (this.mGraphValues.getSize() > 0 && eventTime - this.mGraphValues.getFirst().mTime > MAX_SHOWN_TIME_INTERVAL) {
            this.mGraphValues.removeFirst();
        }
        if (this.mGraphValues.getSize() == 0) {
            this.mFrameCenterPosition = DEFAULT_FRAME_CENTER_POSITION;
        }
        float displacement = this.mScaledVerticalScrollFactor * scrollAxisValue;
        float prevPos = this.mGraphValues.getSize() == 0 ? 0.0f : this.mGraphValues.getLast().mPos;
        float pos = prevPos + displacement;
        this.mGraphValues.add(pos, eventTime);
        float verticalDiff = java.lang.Math.abs(pos - this.mFrameCenterPosition) - this.mFrameCenterToBorderDistance;
        if (verticalDiff > DEFAULT_FRAME_CENTER_POSITION) {
            int sign = pos - this.mFrameCenterPosition < DEFAULT_FRAME_CENTER_POSITION ? -1 : 1;
            this.mFrameCenterPosition += sign * verticalDiff;
        }
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(android.graphics.Canvas canvas) {
        int age;
        float coordX;
        com.android.server.input.debug.RotaryInputGraphView rotaryInputGraphView = this;
        super.onDraw(canvas);
        int verticalMargin = applyDimensionSp(10, rotaryInputGraphView.mDm);
        int bottomY = getHeight() - verticalMargin;
        int middleY = (verticalMargin + bottomY) / 2;
        int rightX = getWidth();
        canvas.drawLine(DEFAULT_FRAME_CENTER_POSITION, verticalMargin, rightX, verticalMargin, rotaryInputGraphView.mFramePaint);
        canvas.drawLine(DEFAULT_FRAME_CENTER_POSITION, middleY, rightX, middleY, rotaryInputGraphView.mFramePaint);
        canvas.drawLine(DEFAULT_FRAME_CENTER_POSITION, bottomY, rightX, bottomY, rotaryInputGraphView.mFramePaint);
        int frameTextOffset = applyDimensionSp(2, rotaryInputGraphView.mDm);
        android.graphics.Paint paint = rotaryInputGraphView.mFrameTextPaint;
        float coordY = DEFAULT_FRAME_CENTER_POSITION;
        canvas.drawText(java.lang.String.format(rotaryInputGraphView.mDefaultLocale, "%.1f", java.lang.Float.valueOf(rotaryInputGraphView.mFrameCenterPosition + rotaryInputGraphView.mFrameCenterToBorderDistance)), DEFAULT_FRAME_CENTER_POSITION, verticalMargin - frameTextOffset, paint);
        canvas.drawText(java.lang.String.format(rotaryInputGraphView.mDefaultLocale, "%.1f", java.lang.Float.valueOf(rotaryInputGraphView.mFrameCenterPosition)), DEFAULT_FRAME_CENTER_POSITION, middleY - frameTextOffset, rotaryInputGraphView.mFrameTextPaint);
        canvas.drawText(java.lang.String.format(rotaryInputGraphView.mDefaultLocale, "%.1f", java.lang.Float.valueOf(rotaryInputGraphView.mFrameCenterPosition - rotaryInputGraphView.mFrameCenterToBorderDistance)), DEFAULT_FRAME_CENTER_POSITION, bottomY - frameTextOffset, rotaryInputGraphView.mFrameTextPaint);
        if (rotaryInputGraphView.mGraphValues.getSize() == 0) {
            return;
        }
        long mostRecentTime = rotaryInputGraphView.mGraphValues.getLast().mTime;
        java.util.Iterator<com.android.server.input.debug.RotaryInputGraphView.GraphValue> iter = rotaryInputGraphView.mGraphValues.reverseIterator();
        float prevCoordX = 0.0f;
        float prevCoordY = 0.0f;
        float prevAge = 0.0f;
        while (iter.hasNext()) {
            com.android.server.input.debug.RotaryInputGraphView.GraphValue value = iter.next();
            int age2 = (int) (mostRecentTime - value.mTime);
            float pos = value.mPos;
            long mostRecentTime2 = mostRecentTime;
            float coordX2 = (((MAX_SHOWN_TIME_INTERVAL - ((long) age2)) / MAX_SHOWN_TIME_INTERVAL) * (rightX + 0)) + coordY;
            float coordY2 = middleY - (((pos - rotaryInputGraphView.mFrameCenterPosition) / rotaryInputGraphView.mFrameCenterToBorderDistance) * (middleY - verticalMargin));
            canvas.drawPoint(coordX2, coordY2, rotaryInputGraphView.mGraphPointPaint);
            if (age2 == 0) {
                age = age2;
                coordX = coordX2;
            } else if (age2 - prevAge > MAX_GESTURE_TIME) {
                age = age2;
                coordX = coordX2;
            } else {
                android.graphics.Paint paint2 = rotaryInputGraphView.mGraphLinePaint;
                coordX = coordX2;
                age = age2;
                canvas.drawLine(prevCoordX, prevCoordY, coordX2, coordY2, paint2);
            }
            prevCoordX = coordX;
            prevCoordY = coordY2;
            float prevAge2 = age;
            coordY = DEFAULT_FRAME_CENTER_POSITION;
            prevAge = prevAge2;
            mostRecentTime = mostRecentTime2;
            rotaryInputGraphView = this;
        }
    }

    public float getFrameCenterPosition() {
        return this.mFrameCenterPosition;
    }

    private static int applyDimensionSp(int dimensionSp, android.util.DisplayMetrics dm) {
        return (int) android.util.TypedValue.applyDimension(2, dimensionSp, dm);
    }

    private static class GraphValue {
        float mPos;
        long mTime;

        GraphValue(float pos, long time) {
            this.mPos = pos;
            this.mTime = time;
        }
    }

    private static class CyclicBuffer {
        private final int mCapacity;
        private int mIteratorCount;
        private int mIteratorIndex;
        private final com.android.server.input.debug.RotaryInputGraphView.GraphValue[] mValues;
        private int mSize = 0;
        private int mLastIndex = 0;
        private final java.util.Iterator<com.android.server.input.debug.RotaryInputGraphView.GraphValue> mReverseIterator = new java.util.Iterator<com.android.server.input.debug.RotaryInputGraphView.GraphValue>() { // from class: com.android.server.input.debug.RotaryInputGraphView.CyclicBuffer.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return com.android.server.input.debug.RotaryInputGraphView.CyclicBuffer.this.mIteratorCount <= com.android.server.input.debug.RotaryInputGraphView.CyclicBuffer.this.mSize;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public com.android.server.input.debug.RotaryInputGraphView.GraphValue next() {
                com.android.server.input.debug.RotaryInputGraphView.CyclicBuffer.this.mIteratorCount++;
                com.android.server.input.debug.RotaryInputGraphView.GraphValue[] graphValueArr = com.android.server.input.debug.RotaryInputGraphView.CyclicBuffer.this.mValues;
                com.android.server.input.debug.RotaryInputGraphView.CyclicBuffer cyclicBuffer = com.android.server.input.debug.RotaryInputGraphView.CyclicBuffer.this;
                int i = cyclicBuffer.mIteratorIndex;
                cyclicBuffer.mIteratorIndex = i - 1;
                return graphValueArr[(i + com.android.server.input.debug.RotaryInputGraphView.CyclicBuffer.this.mCapacity) % com.android.server.input.debug.RotaryInputGraphView.CyclicBuffer.this.mCapacity];
            }
        };

        CyclicBuffer(int capacity) {
            this.mCapacity = capacity;
            this.mValues = new com.android.server.input.debug.RotaryInputGraphView.GraphValue[capacity];
        }

        void add(float pos, long time) {
            this.mLastIndex = (this.mLastIndex + 1) % this.mCapacity;
            if (this.mValues[this.mLastIndex] == null) {
                this.mValues[this.mLastIndex] = new com.android.server.input.debug.RotaryInputGraphView.GraphValue(pos, time);
            } else {
                com.android.server.input.debug.RotaryInputGraphView.GraphValue oldValue = this.mValues[this.mLastIndex];
                oldValue.mPos = pos;
                oldValue.mTime = time;
            }
            if (this.mSize != this.mCapacity) {
                this.mSize++;
            }
        }

        int getSize() {
            return this.mSize;
        }

        com.android.server.input.debug.RotaryInputGraphView.GraphValue getFirst() {
            int distanceBetweenLastAndFirst = (this.mCapacity - this.mSize) + 1;
            int firstIndex = (this.mLastIndex + distanceBetweenLastAndFirst) % this.mCapacity;
            return this.mValues[firstIndex];
        }

        com.android.server.input.debug.RotaryInputGraphView.GraphValue getLast() {
            return this.mValues[this.mLastIndex];
        }

        void removeFirst() {
            this.mSize--;
        }

        java.util.Iterator<com.android.server.input.debug.RotaryInputGraphView.GraphValue> reverseIterator() {
            this.mIteratorIndex = this.mLastIndex;
            this.mIteratorCount = 1;
            return this.mReverseIterator;
        }
    }
}
