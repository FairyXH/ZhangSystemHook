package com.android.server.display.color;

/* JADX INFO: loaded from: classes2.dex */
abstract class TintController {
    private static final long TRANSITION_DURATION = 3000;
    private android.animation.ValueAnimator mAnimator;
    private java.lang.Boolean mIsActivated;

    public abstract int getLevel();

    public abstract float[] getMatrix();

    public abstract boolean isAvailable(android.content.Context context);

    public abstract void setMatrix(int i);

    public abstract void setUp(android.content.Context context, boolean z);

    TintController() {
    }

    public android.animation.ValueAnimator getAnimator() {
        return this.mAnimator;
    }

    public void setAnimator(android.animation.ValueAnimator animator) {
        this.mAnimator = animator;
    }

    public void cancelAnimator() {
        if (this.mAnimator != null) {
            this.mAnimator.cancel();
        }
    }

    public void endAnimator() {
        if (this.mAnimator != null) {
            this.mAnimator.end();
            this.mAnimator = null;
        }
    }

    public void setActivated(java.lang.Boolean isActivated) {
        this.mIsActivated = isActivated;
    }

    public boolean isActivated() {
        return this.mIsActivated != null && this.mIsActivated.booleanValue();
    }

    public boolean isActivatedStateNotSet() {
        return this.mIsActivated == null;
    }

    public long getTransitionDurationMilliseconds() {
        return 3000L;
    }

    public long getTransitionDurationMilliseconds(boolean direction) {
        return 3000L;
    }

    public void dump(java.io.PrintWriter pw) {
    }

    static java.lang.String matrixToString(float[] matrix, int columns) {
        if (matrix == null || columns <= 0) {
            android.util.Slog.e("ColorDisplayService", "Invalid arguments when formatting matrix to string, matrix is null: " + (matrix == null) + " columns: " + columns);
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder("");
        for (int i = 0; i < matrix.length; i++) {
            if (i % columns == 0) {
                sb.append("\n      ");
            }
            sb.append(java.lang.String.format("%9.6f", java.lang.Float.valueOf(matrix[i])));
        }
        return sb.toString();
    }
}
