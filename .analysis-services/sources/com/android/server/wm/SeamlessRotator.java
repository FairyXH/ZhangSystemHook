package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class SeamlessRotator {
    private final boolean mApplyFixedTransformHint;
    private final int mFixedTransformHint;
    private final int mNewRotation;
    private final int mOldRotation;
    private final android.graphics.Matrix mTransform = new android.graphics.Matrix();
    private final float[] mFloat9 = new float[9];

    public SeamlessRotator(int oldRotation, int newRotation, android.view.DisplayInfo info, boolean applyFixedTransformationHint) {
        this.mOldRotation = oldRotation;
        this.mNewRotation = newRotation;
        this.mApplyFixedTransformHint = applyFixedTransformationHint;
        this.mFixedTransformHint = oldRotation;
        boolean z = true;
        if (info.rotation != 1 && info.rotation != 3) {
            z = false;
        }
        boolean flipped = z;
        int pH = flipped ? info.logicalWidth : info.logicalHeight;
        int pW = flipped ? info.logicalHeight : info.logicalWidth;
        android.graphics.Matrix tmp = new android.graphics.Matrix();
        com.android.server.wm.utils.CoordinateTransforms.transformLogicalToPhysicalCoordinates(oldRotation, pW, pH, this.mTransform);
        com.android.server.wm.utils.CoordinateTransforms.transformPhysicalToLogicalCoordinates(newRotation, pW, pH, tmp);
        this.mTransform.postConcat(tmp);
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM) {
            android.util.Slog.d("SeamlessRotator", "Init seamless rotator from " + this.mOldRotation + " to " + this.mNewRotation + ", pH : " + pH + "; pW : " + pW);
        }
    }

    public void unrotate(android.view.SurfaceControl.Transaction transaction, com.android.server.wm.WindowContainer win) {
        if (win.getSurfaceControl() == null) {
            return;
        }
        applyTransform(transaction, win.getSurfaceControl());
        float[] winSurfacePos = {win.mLastSurfacePosition.x, win.mLastSurfacePosition.y};
        this.mTransform.mapPoints(winSurfacePos);
        transaction.setPosition(win.getSurfaceControl(), winSurfacePos[0], winSurfacePos[1]);
        if (this.mApplyFixedTransformHint) {
            transaction.setFixedTransformHint(win.mSurfaceControl, this.mFixedTransformHint);
        }
    }

    void applyTransform(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl sc) {
        t.setMatrix(sc, this.mTransform, this.mFloat9);
    }

    public int getOldRotation() {
        return this.mOldRotation;
    }

    void finish(android.view.SurfaceControl.Transaction t, com.android.server.wm.WindowContainer win) {
        if (win.mSurfaceControl == null || !win.mSurfaceControl.isValid()) {
            return;
        }
        setIdentityMatrix(t, win.mSurfaceControl);
        t.setPosition(win.mSurfaceControl, win.mLastSurfacePosition.x, win.mLastSurfacePosition.y);
        if (this.mApplyFixedTransformHint) {
            t.unsetFixedTransformHint(win.mSurfaceControl);
        }
    }

    void setIdentityMatrix(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl sc) {
        t.setMatrix(sc, android.graphics.Matrix.IDENTITY_MATRIX, this.mFloat9);
    }

    public void dump(java.io.PrintWriter pw) {
        pw.print("{old=");
        pw.print(this.mOldRotation);
        pw.print(", new=");
        pw.print(this.mNewRotation);
        pw.print("}");
    }

    public java.lang.String toString() {
        java.io.StringWriter sw = new java.io.StringWriter();
        dump(new java.io.PrintWriter(sw));
        return "ForcedSeamlessRotator" + sw.toString();
    }
}
