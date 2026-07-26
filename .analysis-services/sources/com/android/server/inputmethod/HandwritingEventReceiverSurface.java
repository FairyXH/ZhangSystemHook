package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class HandwritingEventReceiverSurface {
    private final android.view.InputChannel mClientChannel;
    private final android.view.SurfaceControl mInputSurface;
    private boolean mIsIntercepting;
    private final android.view.InputWindowHandle mWindowHandle;
    public static final java.lang.String TAG = com.android.server.inputmethod.HandwritingEventReceiverSurface.class.getSimpleName();
    static final boolean DEBUG = com.android.server.inputmethod.HandwritingModeController.DEBUG;

    HandwritingEventReceiverSurface(java.lang.String name, int displayId, android.view.SurfaceControl sc, android.view.InputChannel inputChannel) {
        this.mClientChannel = inputChannel;
        this.mInputSurface = sc;
        this.mWindowHandle = new android.view.InputWindowHandle(new android.view.InputApplicationHandle((android.os.IBinder) null, name, android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS), displayId);
        this.mWindowHandle.name = name;
        this.mWindowHandle.token = this.mClientChannel.getToken();
        this.mWindowHandle.layoutParamsType = 2015;
        this.mWindowHandle.dispatchingTimeoutMillis = android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        this.mWindowHandle.ownerPid = android.os.Process.myPid();
        this.mWindowHandle.ownerUid = android.os.Process.myUid();
        this.mWindowHandle.scaleFactor = 1.0f;
        this.mWindowHandle.inputConfig = 49164;
        this.mWindowHandle.replaceTouchableRegionWithCrop((android.view.SurfaceControl) null);
        android.view.SurfaceControl.Transaction t = new android.view.SurfaceControl.Transaction();
        this.mWindowHandle.setTrustedOverlay(t, this.mInputSurface, true);
        t.setInputWindowInfo(this.mInputSurface, this.mWindowHandle);
        t.setLayer(this.mInputSurface, 2);
        t.setPosition(this.mInputSurface, 0.0f, 0.0f);
        t.setCrop(this.mInputSurface, null);
        t.show(this.mInputSurface);
        t.apply();
        this.mIsIntercepting = false;
    }

    void startIntercepting(int imePid, int imeUid) {
        this.mWindowHandle.ownerPid = imePid;
        this.mWindowHandle.ownerUid = imeUid;
        this.mWindowHandle.inputConfig &= -16385;
        new android.view.SurfaceControl.Transaction().setInputWindowInfo(this.mInputSurface, this.mWindowHandle).apply();
        this.mIsIntercepting = true;
    }

    void setNotTouchable(boolean notTouchable) {
        if (notTouchable) {
            this.mWindowHandle.inputConfig |= 8;
        } else {
            this.mWindowHandle.inputConfig &= -9;
        }
        new android.view.SurfaceControl.Transaction().setInputWindowInfo(this.mInputSurface, this.mWindowHandle).apply();
    }

    boolean isIntercepting() {
        return this.mIsIntercepting;
    }

    void remove() {
        android.view.SurfaceControl.Transaction t = new android.view.SurfaceControl.Transaction();
        t.remove(this.mInputSurface);
        t.apply();
    }

    android.view.InputChannel getInputChannel() {
        return this.mClientChannel;
    }

    android.view.SurfaceControl getSurface() {
        return this.mInputSurface;
    }

    android.view.InputWindowHandle getInputWindowHandle() {
        return this.mWindowHandle;
    }
}
