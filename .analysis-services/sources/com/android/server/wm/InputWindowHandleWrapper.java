package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class InputWindowHandleWrapper {
    private boolean mChanged = true;
    private final android.view.InputWindowHandle mHandle;

    InputWindowHandleWrapper(android.view.InputWindowHandle handle) {
        this.mHandle = handle;
    }

    boolean isChanged() {
        return this.mChanged;
    }

    void forceChange() {
        this.mChanged = true;
    }

    void applyChangesToSurface(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl sc) {
        t.setInputWindowInfo(sc, this.mHandle);
        this.mChanged = false;
    }

    int getDisplayId() {
        return this.mHandle.displayId;
    }

    boolean isFocusable() {
        return (this.mHandle.inputConfig & 4) == 0;
    }

    boolean isPaused() {
        return (this.mHandle.inputConfig & 128) != 0;
    }

    boolean isTrustedOverlay() {
        return (this.mHandle.inputConfig & 256) != 0;
    }

    boolean hasWallpaper() {
        return (this.mHandle.inputConfig & 32) != 0;
    }

    android.view.InputApplicationHandle getInputApplicationHandle() {
        return this.mHandle.inputApplicationHandle;
    }

    void setInputApplicationHandle(android.view.InputApplicationHandle handle) {
        if (this.mHandle.inputApplicationHandle == handle) {
            return;
        }
        this.mHandle.inputApplicationHandle = handle;
        this.mChanged = true;
    }

    void setToken(android.os.IBinder token) {
        if (this.mHandle.token == token) {
            return;
        }
        this.mHandle.token = token;
        this.mChanged = true;
    }

    void setName(java.lang.String name) {
        if (java.util.Objects.equals(this.mHandle.name, name)) {
            return;
        }
        this.mHandle.name = name;
        this.mChanged = true;
    }

    void setLayoutParamsFlags(int flags) {
        if (this.mHandle.layoutParamsFlags == flags) {
            return;
        }
        this.mHandle.layoutParamsFlags = flags;
        this.mChanged = true;
    }

    void setLayoutParamsType(int type) {
        if (this.mHandle.layoutParamsType == type) {
            return;
        }
        this.mHandle.layoutParamsType = type;
        this.mChanged = true;
    }

    void setDispatchingTimeoutMillis(long timeout) {
        if (this.mHandle.dispatchingTimeoutMillis == timeout) {
            return;
        }
        this.mHandle.dispatchingTimeoutMillis = timeout;
        this.mChanged = true;
    }

    void setTouchableRegion(android.graphics.Region region) {
        if (this.mHandle.touchableRegion.equals(region)) {
            return;
        }
        this.mHandle.touchableRegion.set(region);
        this.mChanged = true;
    }

    void clearTouchableRegion() {
        if (this.mHandle.touchableRegion.isEmpty()) {
            return;
        }
        this.mHandle.touchableRegion.setEmpty();
        this.mChanged = true;
    }

    void setFocusable(boolean focusable) {
        if (isFocusable() == focusable) {
            return;
        }
        this.mHandle.setInputConfig(4, !focusable);
        this.mChanged = true;
    }

    void setTouchOcclusionMode(int mode) {
        if (this.mHandle.touchOcclusionMode == mode) {
            return;
        }
        this.mHandle.touchOcclusionMode = mode;
        this.mChanged = true;
    }

    void setHasWallpaper(boolean hasWallpaper) {
        if (hasWallpaper() == hasWallpaper) {
            return;
        }
        this.mHandle.setInputConfig(32, hasWallpaper);
        this.mChanged = true;
    }

    void setPaused(boolean paused) {
        if (isPaused() == paused) {
            return;
        }
        this.mHandle.setInputConfig(128, paused);
        this.mChanged = true;
    }

    void setOplusInputConfig(int inputConfig, boolean value) {
        if (((this.mHandle.inputConfig & inputConfig) != 0) == value) {
            return;
        }
        this.mHandle.setInputConfig(inputConfig, value);
        this.mChanged = true;
    }

    void setTrustedOverlay(boolean trustedOverlay) {
        if (isTrustedOverlay() == trustedOverlay) {
            return;
        }
        this.mHandle.setInputConfig(256, trustedOverlay);
        this.mChanged = true;
    }

    void setTrustedOverlay(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl sc, boolean trustedOverlay) {
        this.mHandle.setTrustedOverlay(t, sc, trustedOverlay);
    }

    void setOwnerPid(int pid) {
        if (this.mHandle.ownerPid == pid) {
            return;
        }
        this.mHandle.ownerPid = pid;
        this.mChanged = true;
    }

    void setOwnerUid(int uid) {
        if (this.mHandle.ownerUid == uid) {
            return;
        }
        this.mHandle.ownerUid = uid;
        this.mChanged = true;
    }

    void setPackageName(java.lang.String packageName) {
        if (java.util.Objects.equals(this.mHandle.packageName, packageName)) {
            return;
        }
        this.mHandle.packageName = packageName;
        this.mChanged = true;
    }

    void setDisplayId(int displayId) {
        if (this.mHandle.displayId == displayId) {
            return;
        }
        this.mHandle.displayId = displayId;
        this.mChanged = true;
    }

    void setSurfaceInset(int inset) {
        if (this.mHandle.surfaceInset == inset) {
            return;
        }
        this.mHandle.surfaceInset = inset;
        this.mChanged = true;
    }

    void setScaleFactor(float scale) {
        if (this.mHandle.scaleFactor == scale) {
            return;
        }
        this.mHandle.scaleFactor = scale;
        this.mChanged = true;
    }

    void setTouchableRegionCrop(android.view.SurfaceControl bounds) {
        if (this.mHandle.touchableRegionSurfaceControl.get() == bounds) {
            return;
        }
        this.mHandle.setTouchableRegionCrop(bounds);
        this.mChanged = true;
    }

    void setReplaceTouchableRegionWithCrop(boolean replace) {
        if (this.mHandle.replaceTouchableRegionWithCrop == replace) {
            return;
        }
        this.mHandle.replaceTouchableRegionWithCrop = replace;
        this.mChanged = true;
    }

    void setWindowToken(android.os.IBinder windowToken) {
        if (this.mHandle.getWindowToken() == windowToken) {
            return;
        }
        this.mHandle.setWindowToken(windowToken);
        this.mChanged = true;
    }

    void setInputConfigMasked(int inputConfig, int mask) {
        int inputConfigMasked = inputConfig & mask;
        if (inputConfigMasked == (this.mHandle.inputConfig & mask)) {
            return;
        }
        this.mHandle.inputConfig &= ~mask;
        this.mHandle.inputConfig |= inputConfigMasked;
        this.mChanged = true;
    }

    void setFocusTransferTarget(android.os.IBinder toToken) {
        if (this.mHandle.focusTransferTarget == toToken) {
            return;
        }
        this.mHandle.focusTransferTarget = toToken;
        this.mChanged = true;
    }

    public java.lang.String toString() {
        return this.mHandle + ", changed=" + this.mChanged;
    }
}
