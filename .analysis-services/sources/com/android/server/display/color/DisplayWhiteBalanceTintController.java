package com.android.server.display.color;

/* JADX INFO: loaded from: classes2.dex */
final class DisplayWhiteBalanceTintController extends com.android.server.display.color.ColorTemperatureTintController {
    private static final int COLORSPACE_MATRIX_LENGTH = 9;
    private static final int NUM_DISPLAY_PRIMARIES_VALS = 12;
    private static final int NUM_VALUES_PER_PRIMARY = 3;
    private int mAppliedCct;
    private com.android.server.display.color.CctEvaluator mCctEvaluator;
    private float[] mChromaticAdaptationMatrix;
    int mCurrentColorTemperature;
    private float[] mCurrentColorTemperatureXYZ;
    android.graphics.ColorSpace.Rgb mDisplayColorSpaceRGB;
    private final com.android.server.display.feature.DisplayManagerFlags mDisplayManagerFlags;
    private final android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    private int mDisplayNominalWhiteCct;
    private java.lang.Boolean mIsAvailable;
    private int mTargetCct;
    private int mTemperatureDefault;
    int mTemperatureMax;
    int mTemperatureMin;
    private long mTransitionDuration;
    private long mTransitionDurationDecrease;
    private long mTransitionDurationIncrease;
    private final java.lang.Object mLock = new java.lang.Object();
    float[] mDisplayNominalWhiteXYZ = new float[3];
    boolean mSetUp = false;
    private final float[] mMatrixDisplayWhiteBalance = new float[16];
    private boolean mIsAllowed = true;

    DisplayWhiteBalanceTintController(android.hardware.display.DisplayManagerInternal dm, com.android.server.display.feature.DisplayManagerFlags displayManagerFlags) {
        this.mDisplayManagerInternal = dm;
        this.mDisplayManagerFlags = displayManagerFlags;
    }

    @Override // com.android.server.display.color.TintController
    public void setUp(android.content.Context context, boolean needsLinear) {
        android.graphics.ColorSpace.Rgb displayColorSpaceRGB;
        this.mSetUp = false;
        android.content.res.Resources res = context.getResources();
        setAllowed(res.getBoolean(android.R.bool.config_displayWhiteBalanceAvailable));
        android.graphics.ColorSpace.Rgb displayColorSpaceRGB2 = getDisplayColorSpaceFromSurfaceControl();
        if (displayColorSpaceRGB2 != null) {
            displayColorSpaceRGB = displayColorSpaceRGB2;
        } else {
            android.util.Slog.w("ColorDisplayService", "Failed to get display color space from SurfaceControl, trying res");
            android.graphics.ColorSpace.Rgb displayColorSpaceRGB3 = getDisplayColorSpaceFromResources(res);
            if (displayColorSpaceRGB3 != null) {
                displayColorSpaceRGB = displayColorSpaceRGB3;
            } else {
                android.util.Slog.e("ColorDisplayService", "Failed to get display color space from resources");
                return;
            }
        }
        if (!isColorMatrixValid(displayColorSpaceRGB.getTransform())) {
            android.util.Slog.e("ColorDisplayService", "Invalid display color space RGB-to-XYZ transform");
            return;
        }
        if (!isColorMatrixValid(displayColorSpaceRGB.getInverseTransform())) {
            android.util.Slog.e("ColorDisplayService", "Invalid display color space XYZ-to-RGB transform");
            return;
        }
        java.lang.String[] nominalWhiteValues = res.getStringArray(android.R.array.config_displayWhiteBalanceDecreaseThresholds);
        float[] displayNominalWhiteXYZ = new float[3];
        for (int i = 0; i < nominalWhiteValues.length; i++) {
            displayNominalWhiteXYZ[i] = java.lang.Float.parseFloat(nominalWhiteValues[i]);
        }
        int displayNominalWhiteCct = res.getInteger(android.R.integer.config_deviceStateRearDisplay);
        int colorTemperatureMin = res.getInteger(android.R.integer.config_deskDockKeepsScreenOn);
        if (colorTemperatureMin <= 0) {
            android.util.Slog.e("ColorDisplayService", "Display white balance minimum temperature must be greater than 0");
            return;
        }
        int colorTemperatureMax = res.getInteger(android.R.integer.config_demo_pointing_not_aligned_duration_millis);
        if (colorTemperatureMax < colorTemperatureMin) {
            android.util.Slog.e("ColorDisplayService", "Display white balance max temp must be greater or equal to min");
            return;
        }
        int defaultTemperature = res.getInteger(android.R.integer.config_delay_for_ims_dereg_millis);
        this.mTransitionDuration = res.getInteger(android.R.integer.config_displayWhiteBalanceBrightnessSensorRate);
        if (!this.mDisplayManagerFlags.isAdaptiveTone2Enabled()) {
            this.mTransitionDurationDecrease = this.mTransitionDuration;
            this.mTransitionDurationIncrease = this.mTransitionDuration;
        } else {
            this.mTransitionDurationIncrease = res.getInteger(android.R.integer.config_displayWhiteBalanceColorTemperatureFilterHorizon);
            this.mTransitionDurationDecrease = res.getInteger(android.R.integer.config_displayWhiteBalanceColorTemperatureDefault);
        }
        int[] cctRangeMinimums = res.getIntArray(android.R.array.config_displayWhiteBalanceDisplayNominalWhite);
        int[] steps = res.getIntArray(android.R.array.config_displayWhiteBalanceDisplayPrimaries);
        synchronized (this.mLock) {
            this.mDisplayColorSpaceRGB = displayColorSpaceRGB;
            this.mDisplayNominalWhiteXYZ = displayNominalWhiteXYZ;
            this.mDisplayNominalWhiteCct = displayNominalWhiteCct;
            this.mTargetCct = this.mDisplayNominalWhiteCct;
            this.mAppliedCct = this.mDisplayNominalWhiteCct;
            this.mTemperatureMin = colorTemperatureMin;
            this.mTemperatureMax = colorTemperatureMax;
            this.mTemperatureDefault = defaultTemperature;
            this.mSetUp = true;
            this.mCctEvaluator = new com.android.server.display.color.CctEvaluator(this.mTemperatureMin, this.mTemperatureMax, cctRangeMinimums, steps);
        }
        setMatrix(this.mTemperatureDefault);
    }

    @Override // com.android.server.display.color.TintController
    public float[] getMatrix() {
        if (!this.mSetUp || !isActivated()) {
            return com.android.server.display.color.ColorDisplayService.MATRIX_IDENTITY;
        }
        computeMatrixForCct(this.mAppliedCct);
        return this.mMatrixDisplayWhiteBalance;
    }

    @Override // com.android.server.display.color.ColorTemperatureTintController
    public int getTargetCct() {
        return this.mTargetCct;
    }

    private static float[] mul3x3(float[] lhs, float[] rhs) {
        float[] r = {(lhs[0] * rhs[0]) + (lhs[3] * rhs[1]) + (lhs[6] * rhs[2]), (lhs[1] * rhs[0]) + (lhs[4] * rhs[1]) + (lhs[7] * rhs[2]), (lhs[2] * rhs[0]) + (lhs[5] * rhs[1]) + (lhs[8] * rhs[2]), (lhs[0] * rhs[3]) + (lhs[3] * rhs[4]) + (lhs[6] * rhs[5]), (lhs[1] * rhs[3]) + (lhs[4] * rhs[4]) + (lhs[7] * rhs[5]), (lhs[2] * rhs[3]) + (lhs[5] * rhs[4]) + (lhs[8] * rhs[5]), (lhs[0] * rhs[6]) + (lhs[3] * rhs[7]) + (lhs[6] * rhs[8]), (lhs[1] * rhs[6]) + (lhs[4] * rhs[7]) + (lhs[7] * rhs[8]), (lhs[2] * rhs[6]) + (lhs[5] * rhs[7]) + (lhs[8] * rhs[8])};
        return r;
    }

    @Override // com.android.server.display.color.TintController
    public void setMatrix(int cct) {
        setTargetCct(cct);
        computeMatrixForCct(this.mTargetCct);
    }

    @Override // com.android.server.display.color.ColorTemperatureTintController
    public void setTargetCct(int cct) {
        if (!this.mSetUp) {
            android.util.Slog.w("ColorDisplayService", "Can't set display white balance temperature: uninitialized");
            return;
        }
        if (cct < this.mTemperatureMin) {
            android.util.Slog.w("ColorDisplayService", "Requested display color temperature is below allowed minimum");
            this.mTargetCct = this.mTemperatureMin;
        } else if (cct > this.mTemperatureMax) {
            android.util.Slog.w("ColorDisplayService", "Requested display color temperature is above allowed maximum");
            this.mTargetCct = this.mTemperatureMax;
        } else {
            this.mTargetCct = cct;
        }
    }

    @Override // com.android.server.display.color.ColorTemperatureTintController
    public int getDisabledCct() {
        return this.mDisplayNominalWhiteCct;
    }

    @Override // com.android.server.display.color.ColorTemperatureTintController
    public float[] computeMatrixForCct(int cct) {
        float[] fArr;
        if (!this.mSetUp || cct == 0) {
            android.util.Slog.w("ColorDisplayService", "Couldn't compute matrix for cct=" + cct);
            return com.android.server.display.color.ColorDisplayService.MATRIX_IDENTITY;
        }
        synchronized (this.mLock) {
            this.mCurrentColorTemperature = cct;
            if (cct == this.mDisplayNominalWhiteCct && !isActivated()) {
                android.opengl.Matrix.setIdentityM(this.mMatrixDisplayWhiteBalance, 0);
            } else {
                computeMatrixForCctLocked(cct);
            }
            android.util.Slog.d("ColorDisplayService", "computeDisplayWhiteBalanceMatrix: cct =" + cct + " matrix =" + matrixToString(this.mMatrixDisplayWhiteBalance, 16));
            fArr = this.mMatrixDisplayWhiteBalance;
        }
        return fArr;
    }

    private void computeMatrixForCctLocked(int cct) {
        this.mCurrentColorTemperatureXYZ = android.graphics.ColorSpace.cctToXyz(cct);
        this.mChromaticAdaptationMatrix = android.graphics.ColorSpace.chromaticAdaptation(android.graphics.ColorSpace.Adaptation.BRADFORD, this.mDisplayNominalWhiteXYZ, this.mCurrentColorTemperatureXYZ);
        float[] result = mul3x3(this.mDisplayColorSpaceRGB.getInverseTransform(), mul3x3(this.mChromaticAdaptationMatrix, this.mDisplayColorSpaceRGB.getTransform()));
        float adaptedMaxR = result[0] + result[3] + result[6];
        float adaptedMaxG = result[1] + result[4] + result[7];
        float adaptedMaxB = result[2] + result[5] + result[8];
        float denum = java.lang.Math.max(java.lang.Math.max(adaptedMaxR, adaptedMaxG), adaptedMaxB);
        android.opengl.Matrix.setIdentityM(this.mMatrixDisplayWhiteBalance, 0);
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i] / denum;
            if (!isColorMatrixCoeffValid(result[i])) {
                android.util.Slog.e("ColorDisplayService", "Invalid DWB color matrix");
                return;
            }
        }
        java.lang.System.arraycopy(result, 0, this.mMatrixDisplayWhiteBalance, 0, 3);
        java.lang.System.arraycopy(result, 3, this.mMatrixDisplayWhiteBalance, 4, 3);
        java.lang.System.arraycopy(result, 6, this.mMatrixDisplayWhiteBalance, 8, 3);
    }

    @Override // com.android.server.display.color.ColorTemperatureTintController
    int getAppliedCct() {
        return this.mAppliedCct;
    }

    @Override // com.android.server.display.color.ColorTemperatureTintController
    void setAppliedCct(int cct) {
        this.mAppliedCct = cct;
    }

    @Override // com.android.server.display.color.ColorTemperatureTintController
    com.android.server.display.color.CctEvaluator getEvaluator() {
        return this.mCctEvaluator;
    }

    @Override // com.android.server.display.color.TintController
    public int getLevel() {
        return 125;
    }

    @Override // com.android.server.display.color.TintController
    public boolean isAvailable(android.content.Context context) {
        if (this.mIsAvailable == null) {
            this.mIsAvailable = java.lang.Boolean.valueOf(android.hardware.display.ColorDisplayManager.isDisplayWhiteBalanceAvailable(context));
        }
        return this.mIsAvailable.booleanValue();
    }

    @Override // com.android.server.display.color.TintController
    public long getTransitionDurationMilliseconds() {
        return this.mTransitionDuration;
    }

    @Override // com.android.server.display.color.TintController
    public long getTransitionDurationMilliseconds(boolean isIncreasing) {
        return isIncreasing ? this.mTransitionDurationIncrease : this.mTransitionDurationDecrease;
    }

    @Override // com.android.server.display.color.TintController
    public void dump(java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("    mSetUp = " + this.mSetUp);
            if (this.mSetUp) {
                pw.println("    mTemperatureMin = " + this.mTemperatureMin);
                pw.println("    mTemperatureMax = " + this.mTemperatureMax);
                pw.println("    mTemperatureDefault = " + this.mTemperatureDefault);
                pw.println("    mDisplayNominalWhiteCct = " + this.mDisplayNominalWhiteCct);
                pw.println("    mCurrentColorTemperature = " + this.mCurrentColorTemperature);
                pw.println("    mTargetCct = " + this.mTargetCct);
                pw.println("    mAppliedCct = " + this.mAppliedCct);
                pw.println("    mCurrentColorTemperatureXYZ = " + matrixToString(this.mCurrentColorTemperatureXYZ, 3));
                pw.println("    mDisplayColorSpaceRGB RGB-to-XYZ = " + matrixToString(this.mDisplayColorSpaceRGB.getTransform(), 3));
                pw.println("    mChromaticAdaptationMatrix = " + matrixToString(this.mChromaticAdaptationMatrix, 3));
                pw.println("    mDisplayColorSpaceRGB XYZ-to-RGB = " + matrixToString(this.mDisplayColorSpaceRGB.getInverseTransform(), 3));
                pw.println("    mMatrixDisplayWhiteBalance = " + matrixToString(this.mMatrixDisplayWhiteBalance, 4));
                pw.println("    mIsAllowed = " + this.mIsAllowed);
                pw.println("    mTransitionDuration = " + this.mTransitionDuration);
                pw.println("    mTransitionDurationIncrease = " + this.mTransitionDurationIncrease);
                pw.println("    mTransitionDurationDecrease = " + this.mTransitionDurationDecrease);
            }
        }
    }

    public float getLuminance() {
        synchronized (this.mLock) {
            if (this.mChromaticAdaptationMatrix == null || this.mChromaticAdaptationMatrix.length != 9) {
                return -1.0f;
            }
            return 1.0f / ((this.mChromaticAdaptationMatrix[1] + this.mChromaticAdaptationMatrix[4]) + this.mChromaticAdaptationMatrix[7]);
        }
    }

    public void setAllowed(boolean allowed) {
        this.mIsAllowed = allowed;
    }

    public boolean isAllowed() {
        return this.mIsAllowed;
    }

    private android.graphics.ColorSpace.Rgb makeRgbColorSpaceFromXYZ(float[] redGreenBlueXYZ, float[] whiteXYZ) {
        return new android.graphics.ColorSpace.Rgb("Display Color Space", redGreenBlueXYZ, whiteXYZ, 2.200000047683716d);
    }

    private android.graphics.ColorSpace.Rgb getDisplayColorSpaceFromSurfaceControl() {
        android.view.SurfaceControl.DisplayPrimaries primaries = this.mDisplayManagerInternal.getDisplayNativePrimaries(0);
        if (primaries == null || primaries.red == null || primaries.green == null || primaries.blue == null || primaries.white == null) {
            return null;
        }
        return makeRgbColorSpaceFromXYZ(new float[]{primaries.red.X, primaries.red.Y, primaries.red.Z, primaries.green.X, primaries.green.Y, primaries.green.Z, primaries.blue.X, primaries.blue.Y, primaries.blue.Z}, new float[]{primaries.white.X, primaries.white.Y, primaries.white.Z});
    }

    private android.graphics.ColorSpace.Rgb getDisplayColorSpaceFromResources(android.content.res.Resources res) {
        java.lang.String[] displayPrimariesValues = res.getStringArray(android.R.array.config_displayWhiteBalanceDisplayColorTemperatures);
        float[] displayRedGreenBlueXYZ = new float[9];
        float[] displayWhiteXYZ = new float[3];
        for (int i = 0; i < displayRedGreenBlueXYZ.length; i++) {
            displayRedGreenBlueXYZ[i] = java.lang.Float.parseFloat(displayPrimariesValues[i]);
        }
        for (int i2 = 0; i2 < displayWhiteXYZ.length; i2++) {
            displayWhiteXYZ[i2] = java.lang.Float.parseFloat(displayPrimariesValues[displayRedGreenBlueXYZ.length + i2]);
        }
        return makeRgbColorSpaceFromXYZ(displayRedGreenBlueXYZ, displayWhiteXYZ);
    }

    private boolean isColorMatrixCoeffValid(float coeff) {
        return (java.lang.Float.isNaN(coeff) || java.lang.Float.isInfinite(coeff)) ? false : true;
    }

    private boolean isColorMatrixValid(float[] matrix) {
        if (matrix == null || matrix.length != 9) {
            return false;
        }
        for (float value : matrix) {
            if (!isColorMatrixCoeffValid(value)) {
                return false;
            }
        }
        return true;
    }
}
