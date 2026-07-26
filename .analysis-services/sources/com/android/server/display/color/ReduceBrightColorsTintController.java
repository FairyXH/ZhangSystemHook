package com.android.server.display.color;

/* JADX INFO: loaded from: classes2.dex */
public class ReduceBrightColorsTintController extends com.android.server.display.color.TintController {
    private int mStrength;
    private final float[] mMatrix = new float[16];
    private final float[] mCoefficients = new float[3];

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ void cancelAnimator() {
        super.cancelAnimator();
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ void endAnimator() {
        super.endAnimator();
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ android.animation.ValueAnimator getAnimator() {
        return super.getAnimator();
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ long getTransitionDurationMilliseconds() {
        return super.getTransitionDurationMilliseconds();
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ long getTransitionDurationMilliseconds(boolean z) {
        return super.getTransitionDurationMilliseconds(z);
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ boolean isActivated() {
        return super.isActivated();
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ boolean isActivatedStateNotSet() {
        return super.isActivatedStateNotSet();
    }

    @Override // com.android.server.display.color.TintController
    public /* bridge */ /* synthetic */ void setAnimator(android.animation.ValueAnimator valueAnimator) {
        super.setAnimator(valueAnimator);
    }

    @Override // com.android.server.display.color.TintController
    public void setUp(android.content.Context context, boolean needsLinear) {
        java.lang.String[] coefficients = context.getResources().getStringArray(needsLinear ? android.R.array.config_primaryCredentialProviderService : android.R.array.config_priorityOnlyDndExemptPackages);
        for (int i = 0; i < 3 && i < coefficients.length; i++) {
            this.mCoefficients[i] = java.lang.Float.parseFloat(coefficients[i]);
        }
    }

    @Override // com.android.server.display.color.TintController
    public float[] getMatrix() {
        return isActivated() ? java.util.Arrays.copyOf(this.mMatrix, this.mMatrix.length) : com.android.server.display.color.ColorDisplayService.MATRIX_IDENTITY;
    }

    @Override // com.android.server.display.color.TintController
    public void setMatrix(int strengthLevel) {
        if (strengthLevel < 0) {
            strengthLevel = 0;
        } else if (strengthLevel > 100) {
            strengthLevel = 100;
        }
        android.util.Slog.d("ColorDisplayService", "Setting bright color reduction level: " + strengthLevel);
        this.mStrength = strengthLevel;
        android.opengl.Matrix.setIdentityM(this.mMatrix, 0);
        float componentValue = computeComponentValue(strengthLevel);
        this.mMatrix[0] = componentValue;
        this.mMatrix[5] = componentValue;
        this.mMatrix[10] = componentValue;
    }

    private float clamp(float value) {
        if (value > 1.0f) {
            return 1.0f;
        }
        if (value < 0.0f) {
            return 0.0f;
        }
        return value;
    }

    @Override // com.android.server.display.color.TintController
    public void dump(java.io.PrintWriter pw) {
        pw.println("    mStrength = " + this.mStrength);
    }

    @Override // com.android.server.display.color.TintController
    public int getLevel() {
        return 250;
    }

    @Override // com.android.server.display.color.TintController
    public boolean isAvailable(android.content.Context context) {
        return android.hardware.display.ColorDisplayManager.isColorTransformAccelerated(context);
    }

    @Override // com.android.server.display.color.TintController
    public void setActivated(java.lang.Boolean isActivated) {
        super.setActivated(isActivated);
        android.util.Slog.i("ColorDisplayService", (isActivated == null || !isActivated.booleanValue()) ? "Turning off reduce bright colors" : "Turning on reduce bright colors");
    }

    public int getStrength() {
        return this.mStrength;
    }

    public float getOffsetFactor() {
        return this.mCoefficients[0] + this.mCoefficients[1] + this.mCoefficients[2];
    }

    public float getAdjustedBrightness(float nits) {
        return computeComponentValue(this.mStrength) * nits;
    }

    private float computeComponentValue(int strengthLevel) {
        float percentageStrength = strengthLevel / 100.0f;
        float squaredPercentageStrength = percentageStrength * percentageStrength;
        return clamp((this.mCoefficients[0] * squaredPercentageStrength) + (this.mCoefficients[1] * percentageStrength) + this.mCoefficients[2]);
    }
}
