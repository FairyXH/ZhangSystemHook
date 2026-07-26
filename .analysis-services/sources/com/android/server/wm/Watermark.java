package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class Watermark {
    private static final java.lang.String TITLE = "WatermarkSurface";
    private final android.graphics.BLASTBufferQueue mBlastBufferQueue;
    private final int mDeltaX;
    private final int mDeltaY;
    private boolean mDrawNeeded;
    private int mLastDH;
    private int mLastDW;
    private final android.view.Surface mSurface;
    private final android.view.SurfaceControl mSurfaceControl;
    private java.lang.String mText;
    private final int mTextHeight;
    private final android.graphics.Paint mTextPaint;
    private final int mTextWidth;
    private final com.android.server.wm.IWatermarkExt mWatermarkExt = (com.android.server.wm.IWatermarkExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWatermarkExt.class).create();

    Watermark(com.android.server.wm.DisplayContent dc, android.util.DisplayMetrics dm, java.lang.String[] tokens, android.view.SurfaceControl.Transaction t) {
        int c1;
        int c2;
        java.lang.StringBuilder builder = new java.lang.StringBuilder(32);
        int len = tokens[0].length();
        int len2 = len & (-2);
        for (int i = 0; i < len2; i += 2) {
            int c12 = tokens[0].charAt(i);
            int c22 = tokens[0].charAt(i + 1);
            if (c12 < 97 || c12 > 102) {
                c1 = (c12 < 65 || c12 > 70) ? c12 - 48 : (c12 - 65) + 10;
            } else {
                c1 = (c12 - 97) + 10;
            }
            if (c22 < 97 || c22 > 102) {
                c2 = (c22 < 65 || c22 > 70) ? c22 - 48 : (c22 - 65) + 10;
            } else {
                c2 = (c22 - 97) + 10;
            }
            builder.append((char) (255 - ((c1 * 16) + c2)));
        }
        this.mText = builder.toString();
        if (this.mWatermarkExt != null) {
            this.mText = this.mWatermarkExt.setTextAndUpdateDisplay(dc, dm, tokens);
        }
        int fontSize = com.android.server.wm.WindowManagerService.getPropertyInt(tokens, 1, 1, 20, dm);
        fontSize = this.mWatermarkExt != null ? this.mWatermarkExt.setWatermarkFontSize(tokens, dm) : fontSize;
        this.mTextPaint = new android.graphics.Paint(1);
        this.mTextPaint.setTextSize(fontSize);
        if (this.mWatermarkExt != null) {
            this.mWatermarkExt.setWatermarkFontTypeFace(this.mTextPaint);
        }
        android.graphics.Paint.FontMetricsInt fm = this.mTextPaint.getFontMetricsInt();
        this.mTextWidth = (int) this.mTextPaint.measureText(this.mText);
        this.mTextHeight = fm.descent - fm.ascent;
        this.mDeltaX = com.android.server.wm.WindowManagerService.getPropertyInt(tokens, 2, 0, this.mTextWidth * 2, dm);
        this.mDeltaY = com.android.server.wm.WindowManagerService.getPropertyInt(tokens, 3, 0, this.mTextHeight * 3, dm);
        int shadowColor = com.android.server.wm.WindowManagerService.getPropertyInt(tokens, 4, 0, -1342177280, dm);
        int color = com.android.server.wm.WindowManagerService.getPropertyInt(tokens, 5, 0, 1627389951, dm);
        int shadowRadius = com.android.server.wm.WindowManagerService.getPropertyInt(tokens, 6, 0, 7, dm);
        int shadowDx = com.android.server.wm.WindowManagerService.getPropertyInt(tokens, 8, 0, 0, dm);
        int shadowDy = com.android.server.wm.WindowManagerService.getPropertyInt(tokens, 9, 0, 0, dm);
        if (this.mWatermarkExt != null) {
            int[] datas = this.mWatermarkExt.setWatermarkData(dm, tokens);
            shadowColor = datas[0];
            color = datas[1];
            shadowRadius = datas[2];
        }
        this.mTextPaint.setColor(color);
        this.mTextPaint.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
        android.view.SurfaceControl ctrl = null;
        try {
            ctrl = dc.makeOverlay().setName(TITLE).setBLASTLayer().setFormat(-3).setCallsite(TITLE).build();
            t.setLayer(ctrl, 1000000).setPosition(ctrl, 0.0f, 0.0f).show(ctrl);
            com.android.server.wm.InputMonitor.setTrustedOverlayInputInfo(ctrl, t, dc.getDisplayId(), TITLE);
        } catch (android.view.Surface.OutOfResourcesException e) {
        }
        this.mSurfaceControl = ctrl;
        this.mBlastBufferQueue = new android.graphics.BLASTBufferQueue(TITLE, this.mSurfaceControl, 1, 1, 1);
        this.mSurface = this.mBlastBufferQueue.createSurface();
    }

    void positionSurface(int dw, int dh, android.view.SurfaceControl.Transaction t) {
        if (this.mLastDW != dw || this.mLastDH != dh) {
            this.mLastDW = dw;
            this.mLastDH = dh;
            t.setBufferSize(this.mSurfaceControl, dw, dh);
            this.mDrawNeeded = true;
        }
    }

    void drawIfNeeded() {
        android.graphics.Canvas c;
        if (!this.mDrawNeeded) {
            return;
        }
        int dw = this.mLastDW;
        int dh = this.mLastDH;
        this.mDrawNeeded = false;
        this.mBlastBufferQueue.update(this.mSurfaceControl, dw, dh, 1);
        try {
            android.graphics.Canvas c2 = this.mSurface.lockCanvas(null);
            c = c2;
        } catch (android.view.Surface.OutOfResourcesException | java.lang.IllegalArgumentException e) {
            c = null;
        }
        if (c != null) {
            c.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            int deltaX = this.mDeltaX;
            int deltaY = this.mDeltaY;
            if (this.mWatermarkExt != null) {
                if (this.mWatermarkExt.drawRotateWatermark(dw, dh, this.mTextHeight, this.mTextWidth, c, this.mText, this.mTextPaint, this.mSurface)) {
                    return;
                }
            }
            int div = (this.mTextWidth + dw) / deltaX;
            int rem = (this.mTextWidth + dw) - (div * deltaX);
            int qdelta = deltaX / 4;
            if (rem < qdelta || rem > deltaX - qdelta) {
                deltaX += deltaX / 3;
            }
            int y = -this.mTextHeight;
            int x = -this.mTextWidth;
            while (y < this.mTextHeight + dh) {
                c.drawText(this.mText, x, y, this.mTextPaint);
                x += deltaX;
                if (x >= dw) {
                    x -= this.mTextWidth + dw;
                    y += deltaY;
                }
            }
            this.mSurface.unlockCanvasAndPost(c);
        }
    }
}
