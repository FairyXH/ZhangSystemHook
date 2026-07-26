package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class EmulatorDisplayOverlay {
    private static final java.lang.String TAG = "WindowManager";
    private static final java.lang.String TITLE = "EmulatorDisplayOverlay";
    private final android.graphics.BLASTBufferQueue mBlastBufferQueue;
    private boolean mDrawNeeded;
    private int mLastDH;
    private int mLastDW;
    private final android.graphics.drawable.Drawable mOverlay;
    private int mRotation;
    private android.graphics.Point mScreenSize;
    private final android.view.Surface mSurface;
    private final android.view.SurfaceControl mSurfaceControl;
    private boolean mVisible;

    EmulatorDisplayOverlay(android.content.Context context, com.android.server.wm.DisplayContent dc, int zOrder, android.view.SurfaceControl.Transaction t) {
        android.view.Display display = dc.getDisplay();
        this.mScreenSize = new android.graphics.Point();
        display.getSize(this.mScreenSize);
        android.view.SurfaceControl ctrl = null;
        try {
            ctrl = dc.makeOverlay().setName(TITLE).setBLASTLayer().setFormat(-3).setCallsite(TITLE).build();
            t.setLayer(ctrl, zOrder);
            t.setPosition(ctrl, 0.0f, 0.0f);
            t.show(ctrl);
            com.android.server.wm.InputMonitor.setTrustedOverlayInputInfo(ctrl, t, dc.getDisplayId(), TITLE);
        } catch (android.view.Surface.OutOfResourcesException e) {
        }
        this.mSurfaceControl = ctrl;
        this.mDrawNeeded = true;
        this.mOverlay = context.getDrawable(android.R.drawable.divider_vertical_dark);
        this.mBlastBufferQueue = new android.graphics.BLASTBufferQueue(TITLE, this.mSurfaceControl, this.mScreenSize.x, this.mScreenSize.y, 1);
        this.mSurface = this.mBlastBufferQueue.createSurface();
    }

    private void drawIfNeeded(android.view.SurfaceControl.Transaction t) {
        if (!this.mDrawNeeded || !this.mVisible) {
            return;
        }
        this.mDrawNeeded = false;
        android.graphics.Canvas c = null;
        try {
            c = this.mSurface.lockCanvas(null);
        } catch (android.view.Surface.OutOfResourcesException | java.lang.IllegalArgumentException e) {
        }
        if (c != null) {
            c.drawColor(0, android.graphics.PorterDuff.Mode.SRC);
            t.setPosition(this.mSurfaceControl, 0.0f, 0.0f);
            int size = java.lang.Math.max(this.mScreenSize.x, this.mScreenSize.y);
            this.mOverlay.setBounds(0, 0, size, size);
            this.mOverlay.draw(c);
            this.mSurface.unlockCanvasAndPost(c);
        }
    }

    public void setVisibility(boolean on, android.view.SurfaceControl.Transaction t) {
        if (this.mSurfaceControl == null) {
            return;
        }
        this.mVisible = on;
        drawIfNeeded(t);
        if (on) {
            t.show(this.mSurfaceControl);
        } else {
            t.hide(this.mSurfaceControl);
        }
    }

    void positionSurface(int dw, int dh, int rotation, android.view.SurfaceControl.Transaction t) {
        if (this.mLastDW == dw && this.mLastDH == dh && this.mRotation == rotation) {
            return;
        }
        this.mLastDW = dw;
        this.mLastDH = dh;
        this.mDrawNeeded = true;
        this.mRotation = rotation;
        drawIfNeeded(t);
    }
}
