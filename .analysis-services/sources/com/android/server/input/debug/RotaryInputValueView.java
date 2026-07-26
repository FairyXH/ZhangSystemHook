package com.android.server.input.debug;

/* JADX INFO: loaded from: classes2.dex */
public class RotaryInputValueView extends android.widget.TextView {
    private static final android.graphics.ColorFilter ACTIVE_BACKGROUND_FILTER = new android.graphics.ColorMatrixColorFilter(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 0.0f, 200.0f});
    private static final int ACTIVE_STATUS_DURATION = 250;
    private static final int ACTIVE_TEXT_COLOR = -12447960;
    private static final int INACTIVE_TEXT_COLOR = -65281;
    private static final int SIDE_PADDING_SP = 4;
    private static final int TEXT_SIZE_SP = 8;
    private final java.util.Locale mDefaultLocale;
    private final float mScaledVerticalScrollFactor;
    private final java.lang.Runnable mUpdateActivityStatusCallback;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateActivityStatus(false);
    }

    public RotaryInputValueView(android.content.Context c) {
        super(c);
        this.mUpdateActivityStatusCallback = new java.lang.Runnable() { // from class: com.android.server.input.debug.RotaryInputValueView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        };
        this.mDefaultLocale = java.util.Locale.getDefault();
        android.util.DisplayMetrics dm = this.mContext.getResources().getDisplayMetrics();
        this.mScaledVerticalScrollFactor = android.view.ViewConfiguration.get(c).getScaledVerticalScrollFactor();
        setText(getFormattedValue(0.0f));
        setTextColor(INACTIVE_TEXT_COLOR);
        setTextSize(applyDimensionSp(8, dm));
        setPaddingRelative(applyDimensionSp(4, dm), 0, applyDimensionSp(4, dm), 0);
        setTypeface(null, 1);
        setBackgroundResource(android.R.drawable.editbox_dropdown_background);
    }

    public void updateValue(float value) {
        removeCallbacks(this.mUpdateActivityStatusCallback);
        setText(getFormattedValue(this.mScaledVerticalScrollFactor * value));
        updateActivityStatus(true);
        postDelayed(this.mUpdateActivityStatusCallback, 250L);
    }

    public void updateActivityStatus(boolean active) {
        if (active) {
            setTextColor(ACTIVE_TEXT_COLOR);
            getBackground().setColorFilter(ACTIVE_BACKGROUND_FILTER);
        } else {
            setTextColor(INACTIVE_TEXT_COLOR);
            getBackground().clearColorFilter();
        }
    }

    private java.lang.String getFormattedValue(float value) {
        return java.lang.String.format(this.mDefaultLocale, "%s%.1f", value < 0.0f ? "-" : "+", java.lang.Float.valueOf(java.lang.Math.abs(value)));
    }

    private static int applyDimensionSp(int dimensionSp, android.util.DisplayMetrics dm) {
        return (int) android.util.TypedValue.applyDimension(2, dimensionSp, dm);
    }
}
