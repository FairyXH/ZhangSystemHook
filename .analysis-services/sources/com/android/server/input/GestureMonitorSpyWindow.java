package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
class GestureMonitorSpyWindow {
    final android.view.InputApplicationHandle mApplicationHandle;
    final android.view.InputChannel mClientChannel;
    final android.view.SurfaceControl mInputSurface;
    final android.os.IBinder mMonitorToken;
    final android.view.InputWindowHandle mWindowHandle;

    GestureMonitorSpyWindow(android.os.IBinder token, java.lang.String name, int displayId, int pid, int uid, android.view.SurfaceControl sc, android.view.InputChannel inputChannel) {
        this.mMonitorToken = token;
        this.mClientChannel = inputChannel;
        this.mInputSurface = sc;
        this.mApplicationHandle = new android.view.InputApplicationHandle((android.os.IBinder) null, name, android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS);
        this.mWindowHandle = new android.view.InputWindowHandle(this.mApplicationHandle, displayId);
        this.mWindowHandle.name = name;
        this.mWindowHandle.token = this.mClientChannel.getToken();
        this.mWindowHandle.layoutParamsType = 2015;
        this.mWindowHandle.dispatchingTimeoutMillis = android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        this.mWindowHandle.ownerPid = pid;
        this.mWindowHandle.ownerUid = uid;
        this.mWindowHandle.scaleFactor = 1.0f;
        this.mWindowHandle.replaceTouchableRegionWithCrop((android.view.SurfaceControl) null);
        this.mWindowHandle.inputConfig = 16388;
        android.view.SurfaceControl.Transaction t = new android.view.SurfaceControl.Transaction();
        this.mWindowHandle.setTrustedOverlay(t, this.mInputSurface, true);
        t.setInputWindowInfo(this.mInputSurface, this.mWindowHandle);
        t.setLayer(this.mInputSurface, 1);
        t.setPosition(this.mInputSurface, 0.0f, 0.0f);
        t.setCrop(this.mInputSurface, null);
        t.show(this.mInputSurface);
        t.apply();
    }

    void remove() {
        android.view.SurfaceControl.Transaction t = new android.view.SurfaceControl.Transaction();
        t.hide(this.mInputSurface);
        t.remove(this.mInputSurface);
        t.apply();
        this.mClientChannel.dispose();
    }

    java.lang.String dump() {
        return "name='" + this.mWindowHandle.name + "', inputChannelToken=" + this.mClientChannel.getToken() + " displayId=" + this.mWindowHandle.displayId;
    }
}
