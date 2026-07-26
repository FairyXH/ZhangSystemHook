package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class InputConsumerImpl implements android.os.IBinder.DeathRecipient {
    final android.view.InputApplicationHandle mApplicationHandle;
    final android.view.InputChannel mClientChannel;
    final int mClientPid;
    final android.os.UserHandle mClientUser;
    final android.view.SurfaceControl mInputSurface;
    final java.lang.String mName;
    final com.android.server.wm.WindowManagerService mService;
    final android.os.IBinder mToken;
    final android.view.InputWindowHandle mWindowHandle;
    android.graphics.Rect mTmpClipRect = new android.graphics.Rect();
    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();
    private final android.graphics.Point mOldPosition = new android.graphics.Point();
    private final android.graphics.Rect mOldWindowCrop = new android.graphics.Rect();

    InputConsumerImpl(com.android.server.wm.WindowManagerService service, android.os.IBinder token, java.lang.String name, android.view.InputChannel inputChannel, int clientPid, android.os.UserHandle clientUser, int displayId, android.view.SurfaceControl.Transaction t) {
        this.mService = service;
        this.mToken = token;
        this.mName = name;
        this.mClientPid = clientPid;
        this.mClientUser = clientUser;
        this.mClientChannel = this.mService.mInputManager.createInputChannel(name);
        if (inputChannel != null) {
            this.mClientChannel.copyTo(inputChannel);
        }
        this.mApplicationHandle = new android.view.InputApplicationHandle(new android.os.Binder(), name, android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS);
        this.mWindowHandle = new android.view.InputWindowHandle(this.mApplicationHandle, displayId);
        this.mWindowHandle.name = name;
        this.mWindowHandle.token = this.mClientChannel.getToken();
        this.mWindowHandle.layoutParamsType = 2022;
        this.mWindowHandle.dispatchingTimeoutMillis = android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        this.mWindowHandle.ownerPid = com.android.server.wm.WindowManagerService.MY_PID;
        this.mWindowHandle.ownerUid = com.android.server.wm.WindowManagerService.MY_UID;
        this.mWindowHandle.scaleFactor = 1.0f;
        this.mWindowHandle.inputConfig = 4;
        this.mInputSurface = this.mService.makeSurfaceBuilder(this.mService.mRoot.getDisplayContent(displayId).getSession()).setContainerLayer().setName("Input Consumer " + name).setCallsite("InputConsumerImpl").build();
        this.mWindowHandle.setTrustedOverlay(t, this.mInputSurface, true);
    }

    void linkToDeathRecipient() {
        if (this.mToken == null) {
            return;
        }
        try {
            this.mToken.linkToDeath(this, 0);
        } catch (android.os.RemoteException e) {
        }
    }

    void unlinkFromDeathRecipient() {
        if (this.mToken == null) {
            return;
        }
        this.mToken.unlinkToDeath(this, 0);
    }

    void layout(android.view.SurfaceControl.Transaction t, int dw, int dh) {
        this.mTmpRect.set(0, 0, dw, dh);
        layout(t, this.mTmpRect);
    }

    void layout(android.view.SurfaceControl.Transaction t, android.graphics.Rect r) {
        this.mTmpClipRect.set(0, 0, r.width(), r.height());
        if (this.mOldPosition.equals(r.left, r.top) && this.mOldWindowCrop.equals(this.mTmpClipRect)) {
            return;
        }
        t.setPosition(this.mInputSurface, r.left, r.top);
        t.setWindowCrop(this.mInputSurface, this.mTmpClipRect);
        this.mOldPosition.set(r.left, r.top);
        this.mOldWindowCrop.set(this.mTmpClipRect);
    }

    void hide(android.view.SurfaceControl.Transaction t) {
        t.hide(this.mInputSurface);
    }

    void show(android.view.SurfaceControl.Transaction t, com.android.server.wm.WindowContainer w) {
        t.show(this.mInputSurface);
        t.setInputWindowInfo(this.mInputSurface, this.mWindowHandle);
        t.setRelativeLayer(this.mInputSurface, w.getSurfaceControl(), 1);
    }

    void show(android.view.SurfaceControl.Transaction t, int layer) {
        t.show(this.mInputSurface);
        t.setInputWindowInfo(this.mInputSurface, this.mWindowHandle);
        t.setLayer(this.mInputSurface, layer);
    }

    void reparent(android.view.SurfaceControl.Transaction t, com.android.server.wm.WindowContainer wc) {
        t.reparent(this.mInputSurface, wc.getSurfaceControl());
    }

    void disposeChannelsLw(android.view.SurfaceControl.Transaction t) {
        this.mService.mInputManager.removeInputChannel(this.mClientChannel.getToken());
        this.mClientChannel.dispose();
        t.remove(this.mInputSurface);
        unlinkFromDeathRecipient();
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        synchronized (this.mService.getWindowManagerLock()) {
            com.android.server.wm.DisplayContent dc = this.mService.mRoot.getDisplayContent(this.mWindowHandle.displayId);
            if (dc == null) {
                return;
            }
            dc.getInputMonitor().destroyInputConsumer(this.mToken);
            unlinkFromDeathRecipient();
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String name, java.lang.String prefix) {
        pw.println(prefix + "  name=" + name + " pid=" + this.mClientPid + " user=" + this.mClientUser);
    }
}
