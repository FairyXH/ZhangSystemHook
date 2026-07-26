package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWatermarkExt {
    default java.lang.String setTextAndUpdateDisplay(com.android.server.wm.DisplayContent dc, android.util.DisplayMetrics dm, java.lang.String[] tokens) {
        return "";
    }

    default int setWatermarkFontSize(java.lang.String[] tokens, android.util.DisplayMetrics dm) {
        return 0;
    }

    default void setWatermarkFontTypeFace(android.graphics.Paint paint) {
    }

    default int[] setWatermarkData(android.util.DisplayMetrics dm, java.lang.String[] tokens) {
        return null;
    }

    default boolean drawRotateWatermark(int dw, int dh, int mTextHeight, int mTextWidth, android.graphics.Canvas c, java.lang.String text, android.graphics.Paint paint, android.view.Surface mSurface) {
        return false;
    }
}
