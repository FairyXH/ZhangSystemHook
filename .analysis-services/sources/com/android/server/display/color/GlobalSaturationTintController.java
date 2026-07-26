package com.android.server.display.color;

/* JADX INFO: loaded from: classes2.dex */
final class GlobalSaturationTintController extends com.android.server.display.color.TintController {
    private final float[] mMatrixGlobalSaturation = new float[16];

    GlobalSaturationTintController() {
    }

    @Override // com.android.server.display.color.TintController
    public void setUp(android.content.Context context, boolean needsLinear) {
    }

    @Override // com.android.server.display.color.TintController
    public float[] getMatrix() {
        return java.util.Arrays.copyOf(this.mMatrixGlobalSaturation, this.mMatrixGlobalSaturation.length);
    }

    @Override // com.android.server.display.color.TintController
    public void setMatrix(int saturationLevel) {
        if (saturationLevel < 0) {
            saturationLevel = 0;
        } else if (saturationLevel > 100) {
            saturationLevel = 100;
        }
        android.util.Slog.d("ColorDisplayService", "Setting saturation level: " + saturationLevel);
        if (saturationLevel == 100) {
            setActivated(false);
            android.opengl.Matrix.setIdentityM(this.mMatrixGlobalSaturation, 0);
            return;
        }
        setActivated(true);
        float saturation = saturationLevel * 0.01f;
        float desaturation = 1.0f - saturation;
        float[] luminance = {0.231f * desaturation, 0.715f * desaturation, 0.072f * desaturation};
        this.mMatrixGlobalSaturation[0] = luminance[0] + saturation;
        this.mMatrixGlobalSaturation[1] = luminance[0];
        this.mMatrixGlobalSaturation[2] = luminance[0];
        this.mMatrixGlobalSaturation[4] = luminance[1];
        this.mMatrixGlobalSaturation[5] = luminance[1] + saturation;
        this.mMatrixGlobalSaturation[6] = luminance[1];
        this.mMatrixGlobalSaturation[8] = luminance[2];
        this.mMatrixGlobalSaturation[9] = luminance[2];
        this.mMatrixGlobalSaturation[10] = luminance[2] + saturation;
        this.mMatrixGlobalSaturation[15] = 1.0f;
    }

    @Override // com.android.server.display.color.TintController
    public int getLevel() {
        return 150;
    }

    @Override // com.android.server.display.color.TintController
    public boolean isAvailable(android.content.Context context) {
        return android.hardware.display.ColorDisplayManager.isColorTransformAccelerated(context);
    }
}
