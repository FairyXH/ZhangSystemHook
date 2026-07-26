package com.android.server.display.utils;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AmbientFilter {
    protected static final boolean DEBUG = false;
    private final com.android.server.display.utils.RollingBuffer mBuffer;
    private final int mHorizon;
    protected boolean mLoggingEnabled;
    protected final java.lang.String mTag;

    protected abstract float filter(long j, com.android.server.display.utils.RollingBuffer rollingBuffer);

    AmbientFilter(java.lang.String tag, int horizon) {
        validateArguments(horizon);
        this.mTag = tag;
        this.mLoggingEnabled = false;
        this.mHorizon = horizon;
        this.mBuffer = new com.android.server.display.utils.RollingBuffer();
    }

    public boolean addValue(long time, float value) {
        if (value < 0.0f) {
            return false;
        }
        truncateOldValues(time);
        if (this.mLoggingEnabled) {
            android.util.Slog.d(this.mTag, "add value: " + value + " @ " + time);
        }
        this.mBuffer.add(time, value);
        return true;
    }

    public float getEstimate(long time) {
        truncateOldValues(time);
        float value = filter(time, this.mBuffer);
        if (this.mLoggingEnabled) {
            android.util.Slog.d(this.mTag, "get estimate: " + value + " @ " + time);
        }
        return value;
    }

    public void clear() {
        this.mBuffer.clear();
    }

    public boolean setLoggingEnabled(boolean loggingEnabled) {
        if (this.mLoggingEnabled == loggingEnabled) {
            return false;
        }
        this.mLoggingEnabled = loggingEnabled;
        return true;
    }

    public void dump(java.io.PrintWriter writer) {
        writer.println("  " + this.mTag);
        writer.println("    mLoggingEnabled=" + this.mLoggingEnabled);
        writer.println("    mHorizon=" + this.mHorizon);
        writer.println("    mBuffer=" + this.mBuffer);
    }

    private void validateArguments(int horizon) {
        if (horizon <= 0) {
            throw new java.lang.IllegalArgumentException("horizon must be positive");
        }
    }

    private void truncateOldValues(long time) {
        long minTime = time - ((long) this.mHorizon);
        this.mBuffer.truncate(minTime);
    }

    static class WeightedMovingAverageAmbientFilter extends com.android.server.display.utils.AmbientFilter {
        private static final int PREDICTION_TIME = 100;
        private final float mIntercept;

        WeightedMovingAverageAmbientFilter(java.lang.String tag, int horizon, float intercept) {
            super(tag, horizon);
            validateArguments(intercept);
            this.mIntercept = intercept;
        }

        @Override // com.android.server.display.utils.AmbientFilter
        public void dump(java.io.PrintWriter writer) {
            super.dump(writer);
            writer.println("    mIntercept=" + this.mIntercept);
        }

        @Override // com.android.server.display.utils.AmbientFilter
        protected float filter(long time, com.android.server.display.utils.RollingBuffer buffer) {
            if (buffer.isEmpty()) {
                return -1.0f;
            }
            float total = 0.0f;
            float totalWeight = 0.0f;
            float[] weights = getWeights(time, buffer);
            for (int i = 0; i < weights.length; i++) {
                float value = buffer.getValue(i);
                float weight = weights[i];
                total += weight * value;
                totalWeight += weight;
            }
            if (totalWeight == 0.0f) {
                return buffer.getValue(buffer.size() - 1);
            }
            return total / totalWeight;
        }

        private void validateArguments(float intercept) {
            if (java.lang.Float.isNaN(intercept) || intercept < 0.0f) {
                throw new java.lang.IllegalArgumentException("intercept must be a non-negative number");
            }
        }

        private float[] getWeights(long time, com.android.server.display.utils.RollingBuffer buffer) {
            float[] weights = new float[buffer.size()];
            long startTime = buffer.getTime(0);
            float previousTime = 0.0f;
            for (int i = 1; i < weights.length; i++) {
                float currentTime = (buffer.getTime(i) - startTime) / 1000.0f;
                float weight = calculateIntegral(previousTime, currentTime);
                weights[i - 1] = weight;
                previousTime = currentTime;
            }
            float lastTime = ((100 + time) - startTime) / 1000.0f;
            float lastWeight = calculateIntegral(previousTime, lastTime);
            weights[weights.length - 1] = lastWeight;
            return weights;
        }

        private float calculateIntegral(float from, float to) {
            return antiderivative(to) - antiderivative(from);
        }

        private float antiderivative(float x) {
            return (0.5f * x * x) + (this.mIntercept * x);
        }
    }
}
