package com.android.server.display.color;

/* JADX INFO: loaded from: classes2.dex */
class CctEvaluator implements android.animation.TypeEvaluator<java.lang.Integer> {
    private static final java.lang.String TAG = "CctEvaluator";
    private final int mIndexOffset;
    final int[] mSteppedCctsAtOffsetCcts;
    final int[] mStepsAtOffsetCcts;

    CctEvaluator(int min, int max, int[] cctRangeMinimums, int[] steps) {
        int delta = (max - min) + 1;
        this.mStepsAtOffsetCcts = new int[delta];
        this.mSteppedCctsAtOffsetCcts = new int[delta];
        this.mIndexOffset = min;
        int parallelArraysLength = cctRangeMinimums.length;
        if (cctRangeMinimums.length != steps.length) {
            android.util.Slog.e(TAG, "Parallel arrays cctRangeMinimums and steps are different lengths; setting step of 1");
            setStepOfOne();
            return;
        }
        if (parallelArraysLength == 0) {
            android.util.Slog.e(TAG, "No cctRangeMinimums or steps are set; setting step of 1");
            setStepOfOne();
            return;
        }
        int parallelArraysIndex = 0;
        int lastSteppedCct = Integer.MIN_VALUE;
        for (int index = 0; index < delta; index++) {
            int cct = this.mIndexOffset + index;
            for (int nextParallelArraysIndex = parallelArraysIndex + 1; nextParallelArraysIndex < parallelArraysLength && cct >= cctRangeMinimums[nextParallelArraysIndex]; nextParallelArraysIndex++) {
                parallelArraysIndex = nextParallelArraysIndex;
            }
            this.mStepsAtOffsetCcts[index] = steps[parallelArraysIndex];
            if (lastSteppedCct == Integer.MIN_VALUE || java.lang.Math.abs(lastSteppedCct - cct) >= steps[parallelArraysIndex]) {
                lastSteppedCct = cct;
            }
            this.mSteppedCctsAtOffsetCcts[index] = lastSteppedCct;
        }
    }

    @Override // android.animation.TypeEvaluator
    public java.lang.Integer evaluate(float fraction, java.lang.Integer startValue, java.lang.Integer endValue) {
        int cct = (int) (startValue.intValue() + ((endValue.intValue() - startValue.intValue()) * fraction));
        int index = cct - this.mIndexOffset;
        if (index < 0 || index >= this.mSteppedCctsAtOffsetCcts.length) {
            android.util.Slog.e(TAG, "steppedCctValueAt: returning same since invalid requested index=" + index);
            return java.lang.Integer.valueOf(cct);
        }
        return java.lang.Integer.valueOf(this.mSteppedCctsAtOffsetCcts[index]);
    }

    private void setStepOfOne() {
        java.util.Arrays.fill(this.mStepsAtOffsetCcts, 1);
        for (int i = 0; i < this.mSteppedCctsAtOffsetCcts.length; i++) {
            this.mSteppedCctsAtOffsetCcts[i] = this.mIndexOffset + i;
        }
    }
}
