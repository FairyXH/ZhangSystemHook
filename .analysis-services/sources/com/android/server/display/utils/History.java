package com.android.server.display.utils;

/* JADX INFO: loaded from: classes2.dex */
public class History {
    private java.time.Clock mClock;
    private int mCount;
    private int mEnd;
    private int mSize;
    private int mStart;
    private long[] mTimes;
    private float[] mValues;

    public History(int size) {
        this(size, java.time.Clock.systemUTC());
    }

    public History(int size, java.time.Clock clock) {
        this.mSize = size;
        this.mCount = 0;
        this.mStart = 0;
        this.mEnd = 0;
        this.mTimes = new long[size];
        this.mValues = new float[size];
        this.mClock = clock;
    }

    public void add(float value) {
        this.mTimes[this.mEnd] = this.mClock.millis();
        this.mValues[this.mEnd] = value;
        if (this.mCount < this.mSize) {
            this.mCount++;
        } else {
            this.mStart = (this.mStart + 1) % this.mSize;
        }
        this.mEnd = (this.mEnd + 1) % this.mSize;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("[");
        for (int i = 0; i < this.mCount; i++) {
            int index = (this.mStart + i) % this.mSize;
            long time = this.mTimes[index];
            float value = this.mValues[index];
            sb.append(value + " @ " + time);
            if (i + 1 != this.mCount) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
