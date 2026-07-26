package com.android.server.display.utils;

/* JADX INFO: loaded from: classes2.dex */
public class RollingBuffer {
    private static final int INITIAL_SIZE = 50;
    private int mCount;
    private int mEnd;
    private int mStart;
    private int mSize = 50;
    private long[] mTimes = new long[50];
    private float[] mValues = new float[50];

    public RollingBuffer() {
        clear();
    }

    public void add(long time, float value) {
        if (this.mCount >= this.mSize) {
            expandBuffer();
        }
        this.mTimes[this.mEnd] = time;
        this.mValues[this.mEnd] = value;
        this.mEnd = (this.mEnd + 1) % this.mSize;
        this.mCount++;
    }

    public int size() {
        return this.mCount;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public long getTime(int index) {
        return this.mTimes[offsetOf(index)];
    }

    public float getValue(int index) {
        return this.mValues[offsetOf(index)];
    }

    public void truncate(long minTime) {
        if (isEmpty() || getTime(0) >= minTime) {
            return;
        }
        int index = getLatestIndexBefore(minTime);
        this.mStart = offsetOf(index);
        this.mCount -= index;
        this.mTimes[this.mStart] = minTime;
    }

    public void clear() {
        this.mCount = 0;
        this.mStart = 0;
        this.mEnd = 0;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("[");
        for (int i = 0; i < this.mCount; i++) {
            int index = offsetOf(i);
            sb.append(this.mValues[index] + " @ " + this.mTimes[index]);
            if (i + 1 != this.mCount) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private int offsetOf(int index) {
        if (index < 0 || index >= this.mCount) {
            throw new java.lang.ArrayIndexOutOfBoundsException("invalid index: " + index + ", mCount= " + this.mCount);
        }
        return (this.mStart + index) % this.mSize;
    }

    private void expandBuffer() {
        int size = this.mSize * 2;
        long[] times = new long[size];
        float[] values = new float[size];
        java.lang.System.arraycopy(this.mTimes, this.mStart, times, 0, this.mCount - this.mStart);
        java.lang.System.arraycopy(this.mTimes, 0, times, this.mCount - this.mStart, this.mStart);
        java.lang.System.arraycopy(this.mValues, this.mStart, values, 0, this.mCount - this.mStart);
        java.lang.System.arraycopy(this.mValues, 0, values, this.mCount - this.mStart, this.mStart);
        this.mSize = size;
        this.mStart = 0;
        this.mEnd = this.mCount;
        this.mTimes = times;
        this.mValues = values;
    }

    private int getLatestIndexBefore(long time) {
        for (int i = 1; i < this.mCount; i++) {
            if (this.mTimes[offsetOf(i)] > time) {
                return i - 1;
            }
        }
        int i2 = this.mCount;
        return i2 - 1;
    }
}
