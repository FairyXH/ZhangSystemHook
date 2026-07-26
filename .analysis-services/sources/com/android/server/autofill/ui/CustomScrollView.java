package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
public class CustomScrollView extends android.widget.ScrollView {
    public static final java.lang.String DEVICE_CONFIG_SAVE_DIALOG_LANDSCAPE_BODY_HEIGHT_MAX_PERCENT = "autofill_save_dialog_landscape_body_height_max_percent";
    public static final java.lang.String DEVICE_CONFIG_SAVE_DIALOG_PORTRAIT_BODY_HEIGHT_MAX_PERCENT = "autofill_save_dialog_portrait_body_height_max_percent";
    private static final java.lang.String TAG = "CustomScrollView";
    private int mAttrBasedMaxHeightPercent;
    private int mHeight;
    private int mMaxLandscapeBodyHeightPercent;
    private int mMaxPortraitBodyHeightPercent;
    private int mWidth;

    public CustomScrollView(android.content.Context context) {
        super(context);
        this.mWidth = -1;
        this.mHeight = -1;
        setMaxBodyHeightPercent(context);
    }

    public CustomScrollView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        this.mWidth = -1;
        this.mHeight = -1;
        setMaxBodyHeightPercent(context);
    }

    public CustomScrollView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mWidth = -1;
        this.mHeight = -1;
        setMaxBodyHeightPercent(context);
    }

    public CustomScrollView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mWidth = -1;
        this.mHeight = -1;
        setMaxBodyHeightPercent(context);
    }

    private void setMaxBodyHeightPercent(android.content.Context context) {
        this.mAttrBasedMaxHeightPercent = getAttrBasedMaxHeightPercent(context);
        this.mMaxPortraitBodyHeightPercent = android.provider.DeviceConfig.getInt("autofill", DEVICE_CONFIG_SAVE_DIALOG_PORTRAIT_BODY_HEIGHT_MAX_PERCENT, this.mAttrBasedMaxHeightPercent);
        this.mMaxLandscapeBodyHeightPercent = android.provider.DeviceConfig.getInt("autofill", DEVICE_CONFIG_SAVE_DIALOG_LANDSCAPE_BODY_HEIGHT_MAX_PERCENT, this.mAttrBasedMaxHeightPercent);
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            android.util.Slog.e(TAG, "no children");
            return;
        }
        this.mWidth = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        calculateDimensions();
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    private void calculateDimensions() {
        int maxHeight;
        if (this.mHeight != -1) {
            return;
        }
        android.graphics.Point point = new android.graphics.Point();
        android.content.Context context = getContext();
        context.getDisplayNoVerify().getSize(point);
        android.view.View content = getChildAt(0);
        int contentHeight = content.getMeasuredHeight();
        int displayHeight = point.y;
        if (getResources().getConfiguration().orientation == 2) {
            maxHeight = (this.mMaxLandscapeBodyHeightPercent * displayHeight) / 100;
        } else {
            maxHeight = (this.mMaxPortraitBodyHeightPercent * displayHeight) / 100;
        }
        this.mHeight = java.lang.Math.min(contentHeight, maxHeight);
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "calculateDimensions(): mMaxPortraitBodyHeightPercent=" + this.mMaxPortraitBodyHeightPercent + ", mMaxLandscapeBodyHeightPercent=" + this.mMaxLandscapeBodyHeightPercent + ", mAttrBasedMaxHeightPercent=" + this.mAttrBasedMaxHeightPercent + ", maxHeight=" + maxHeight + ", contentHeight=" + contentHeight + ", w=" + this.mWidth + ", h=" + this.mHeight);
        }
    }

    private int getAttrBasedMaxHeightPercent(android.content.Context context) {
        android.util.TypedValue maxHeightAttrTypedValue = new android.util.TypedValue();
        context.getTheme().resolveAttribute(android.R.^attr-private.autofillSaveCustomSubtitleMaxHeight, maxHeightAttrTypedValue, true);
        return (int) maxHeightAttrTypedValue.getFraction(100.0f, 100.0f);
    }
}
