package com.android.server.display.whitebalance;

/* JADX INFO: loaded from: classes2.dex */
class DisplayWhiteBalanceThrottler {
    protected static final java.lang.String TAG = "DisplayWhiteBalanceThrottler";
    private float[] mBaseThresholds;
    private int mDecreaseDebounce;
    private float mDecreaseThreshold;
    private float[] mDecreaseThresholds;
    private int mIncreaseDebounce;
    private float mIncreaseThreshold;
    private float[] mIncreaseThresholds;
    private long mLastTime;
    private float mLastValue;
    protected boolean mLoggingEnabled;

    DisplayWhiteBalanceThrottler(int increaseDebounce, int decreaseDebounce, float[] baseThresholds, float[] increaseThresholds, float[] decreaseThresholds) {
        validateArguments(increaseDebounce, decreaseDebounce, baseThresholds, increaseThresholds, decreaseThresholds);
        this.mLoggingEnabled = false;
        this.mIncreaseDebounce = increaseDebounce;
        this.mDecreaseDebounce = decreaseDebounce;
        this.mBaseThresholds = baseThresholds;
        this.mIncreaseThresholds = increaseThresholds;
        this.mDecreaseThresholds = decreaseThresholds;
        clear();
    }

    public boolean throttle(float value) {
        if (this.mLastTime != -1 && (tooSoon(value) || tooClose(value))) {
            return true;
        }
        computeThresholds(value);
        this.mLastTime = java.lang.System.currentTimeMillis();
        this.mLastValue = value;
        return false;
    }

    public void clear() {
        this.mLastTime = -1L;
        this.mIncreaseThreshold = -1.0f;
        this.mDecreaseThreshold = -1.0f;
        this.mLastValue = -1.0f;
    }

    public boolean setLoggingEnabled(boolean loggingEnabled) {
        if (this.mLoggingEnabled == loggingEnabled) {
            return false;
        }
        this.mLoggingEnabled = loggingEnabled;
        return true;
    }

    public void dump(java.io.PrintWriter writer) {
        writer.println("  DisplayWhiteBalanceThrottler");
        writer.println("    mLoggingEnabled=" + this.mLoggingEnabled);
        writer.println("    mIncreaseDebounce=" + this.mIncreaseDebounce);
        writer.println("    mDecreaseDebounce=" + this.mDecreaseDebounce);
        writer.println("    mLastTime=" + this.mLastTime);
        writer.println("    mBaseThresholds=" + java.util.Arrays.toString(this.mBaseThresholds));
        writer.println("    mIncreaseThresholds=" + java.util.Arrays.toString(this.mIncreaseThresholds));
        writer.println("    mDecreaseThresholds=" + java.util.Arrays.toString(this.mDecreaseThresholds));
        writer.println("    mIncreaseThreshold=" + this.mIncreaseThreshold);
        writer.println("    mDecreaseThreshold=" + this.mDecreaseThreshold);
        writer.println("    mLastValue=" + this.mLastValue);
    }

    private void validateArguments(float increaseDebounce, float decreaseDebounce, float[] baseThresholds, float[] increaseThresholds, float[] decreaseThresholds) {
        if (java.lang.Float.isNaN(increaseDebounce) || increaseDebounce < 0.0f) {
            throw new java.lang.IllegalArgumentException("increaseDebounce must be a non-negative number.");
        }
        if (java.lang.Float.isNaN(decreaseDebounce) || decreaseDebounce < 0.0f) {
            throw new java.lang.IllegalArgumentException("decreaseDebounce must be a non-negative number.");
        }
        if (!isValidMapping(baseThresholds, increaseThresholds)) {
            throw new java.lang.IllegalArgumentException("baseThresholds to increaseThresholds is not a valid mapping.");
        }
        if (!isValidMapping(baseThresholds, decreaseThresholds)) {
            throw new java.lang.IllegalArgumentException("baseThresholds to decreaseThresholds is not a valid mapping.");
        }
    }

    private static boolean isValidMapping(float[] x, float[] y) {
        if (x == null || y == null || x.length == 0 || y.length == 0 || x.length != y.length) {
            return false;
        }
        float prevX = -1.0f;
        for (int i = 0; i < x.length; i++) {
            if (java.lang.Float.isNaN(x[i]) || java.lang.Float.isNaN(y[i]) || x[i] < 0.0f || prevX >= x[i]) {
                return false;
            }
            prevX = x[i];
        }
        return true;
    }

    private boolean tooSoon(float value) {
        long earliestTime;
        long time = java.lang.System.currentTimeMillis();
        if (value > this.mLastValue) {
            earliestTime = this.mLastTime + ((long) this.mIncreaseDebounce);
        } else {
            long earliestTime2 = this.mLastTime;
            earliestTime = earliestTime2 + ((long) this.mDecreaseDebounce);
        }
        boolean tooSoon = time < earliestTime;
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, (tooSoon ? "too soon: " : "late enough: ") + time + (tooSoon ? " < " : " > ") + earliestTime);
        }
        return tooSoon;
    }

    private boolean tooClose(float value) {
        float threshold;
        boolean tooClose = true;
        if (value > this.mLastValue) {
            threshold = this.mIncreaseThreshold;
            if (value >= threshold) {
                tooClose = false;
            }
        } else {
            threshold = this.mDecreaseThreshold;
            if (value <= threshold) {
                tooClose = false;
            }
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, (tooClose ? "too close: " : "far enough: ") + value + (value > threshold ? " > " : " < ") + threshold);
        }
        return tooClose;
    }

    private void computeThresholds(float value) {
        int index = getHighestIndexBefore(value, this.mBaseThresholds);
        this.mIncreaseThreshold = (this.mIncreaseThresholds[index] + 1.0f) * value;
        this.mDecreaseThreshold = (1.0f - this.mDecreaseThresholds[index]) * value;
    }

    private int getHighestIndexBefore(float value, float[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] >= value) {
                return i;
            }
        }
        int i2 = values.length;
        return i2 - 1;
    }
}
