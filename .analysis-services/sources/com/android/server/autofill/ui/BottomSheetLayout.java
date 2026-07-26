package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
public class BottomSheetLayout extends android.widget.LinearLayout {
    private static final java.lang.String TAG = "BottomSheetLayout";

    public BottomSheetLayout(android.content.Context context) {
        super(context);
    }

    public BottomSheetLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomSheetLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int widthSpec, int heightSpec) {
        if (getContext() == null || getContext().getResources() == null) {
            super.onMeasure(widthSpec, heightSpec);
            android.util.Slog.w(TAG, "onMeasure failed due to missing context or missing resources.");
            return;
        }
        if (getChildCount() == 0) {
            super.onMeasure(widthSpec, heightSpec);
            android.util.Slog.wtf(TAG, "onMeasure failed due to missing children views.");
            return;
        }
        android.util.DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int pxOffset = getContext().getResources().getDimensionPixelSize(android.R.dimen.alertDialog_material_text_size_title);
        int outerMargin = getContext().getResources().getDimensionPixelSize(android.R.dimen.app_header_height);
        boolean includeHorizontalSpace = getContext().getResources().getBoolean(android.R.bool.config_LTE_eri_for_network_name);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        int maxHeight = (screenHeight - pxOffset) - outerMargin;
        int maxWidth = screenWidth;
        if (includeHorizontalSpace) {
            maxWidth -= pxOffset * 2;
        }
        int maxWidth2 = java.lang.Math.min(maxWidth, getContext().getResources().getDimensionPixelSize(android.R.dimen.alertDialog_material_text_size_body_1));
        super.onMeasure(android.view.View.MeasureSpec.makeMeasureSpec(maxWidth2, 1073741824), android.view.View.MeasureSpec.makeMeasureSpec(maxHeight, Integer.MIN_VALUE));
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "onMeasure() values in dp: screenHeight: " + (screenHeight / displayMetrics.density) + ", screenWidth: " + (screenWidth / displayMetrics.density) + ", maxHeight: " + (maxHeight / displayMetrics.density) + ", maxWidth: " + (maxWidth2 / displayMetrics.density) + ", getMeasuredWidth(): " + (getMeasuredWidth() / displayMetrics.density) + ", getMeasuredHeight(): " + (getMeasuredHeight() / displayMetrics.density));
        }
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }
}
