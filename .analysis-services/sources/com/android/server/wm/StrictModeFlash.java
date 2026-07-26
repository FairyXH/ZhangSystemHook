package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class StrictModeFlash {
    private static final java.lang.String TAG = "WindowManager";
    private static final java.lang.String TITLE = "StrictModeFlash";
    private final android.graphics.BLASTBufferQueue mBlastBufferQueue;
    private boolean mDrawNeeded;
    private int mLastDH;
    private int mLastDW;
    private final android.view.Surface mSurface;
    private final android.view.SurfaceControl mSurfaceControl;
    private final int mThickness = 20;

    StrictModeFlash(com.android.server.wm.DisplayContent dc, android.view.SurfaceControl.Transaction t) {
        android.view.SurfaceControl ctrl = null;
        try {
            ctrl = dc.makeOverlay().setName(TITLE).setBLASTLayer().setFormat(-3).setCallsite(TITLE).build();
            t.setLayer(ctrl, 1010000);
            t.setPosition(ctrl, 0.0f, 0.0f);
            t.show(ctrl);
            com.android.server.wm.InputMonitor.setTrustedOverlayInputInfo(ctrl, t, dc.getDisplayId(), TITLE);
        } catch (android.view.Surface.OutOfResourcesException e) {
        }
        this.mSurfaceControl = ctrl;
        this.mDrawNeeded = true;
        this.mBlastBufferQueue = new android.graphics.BLASTBufferQueue(TITLE, this.mSurfaceControl, 1, 1, 1);
        this.mSurface = this.mBlastBufferQueue.createSurface();
    }

    private void drawIfNeeded() {
        if (!this.mDrawNeeded) {
            return;
        }
        this.mDrawNeeded = false;
        int dw = this.mLastDW;
        int dh = this.mLastDH;
        this.mBlastBufferQueue.update(this.mSurfaceControl, dw, dh, 1);
        android.graphics.Canvas c = null;
        try {
            c = this.mSurface.lockCanvas(null);
        } catch (android.view.Surface.OutOfResourcesException | java.lang.IllegalArgumentException e) {
        }
        if (c == null) {
            return;
        }
        c.save();
        c.clipRect(new android.graphics.Rect(0, 0, dw, 20));
        c.drawColor(-65536);
        c.restore();
        c.save();
        c.clipRect(new android.graphics.Rect(0, 0, 20, dh));
        c.drawColor(-65536);
        c.restore();
        c.save();
        c.clipRect(new android.graphics.Rect(dw - 20, 0, dw, dh));
        c.drawColor(-65536);
        c.restore();
        c.save();
        c.clipRect(new android.graphics.Rect(0, dh - 20, dw, dh));
        c.drawColor(-65536);
        c.restore();
        this.mSurface.unlockCanvasAndPost(c);
    }

    public void setVisibility(boolean on, android.view.SurfaceControl.Transaction t) {
        if (this.mSurfaceControl == null) {
            return;
        }
        drawIfNeeded();
        if (on) {
            t.show(this.mSurfaceControl);
        } else {
            t.hide(this.mSurfaceControl);
        }
    }

    void positionSurface(int dw, int dh, android.view.SurfaceControl.Transaction t) {
        if (this.mLastDW == dw && this.mLastDH == dh) {
            return;
        }
        this.mLastDW = dw;
        this.mLastDH = dh;
        t.setBufferSize(this.mSurfaceControl, dw, dh);
        this.mDrawNeeded = true;
    }
}
