package com.android.server.tv;

/* JADX INFO: loaded from: classes3.dex */
public final class UinputBridge {
    private final dalvik.system.CloseGuard mCloseGuard = dalvik.system.CloseGuard.get();
    private long mPtr;
    private android.os.IBinder mToken;

    private static native void nativeClear(long j);

    private static native void nativeClose(long j);

    private static native long nativeGamepadOpen(java.lang.String str, java.lang.String str2);

    private static native long nativeOpen(java.lang.String str, java.lang.String str2, int i, int i2, int i3);

    private static native void nativeSendGamepadAxisValue(long j, int i, float f);

    private static native void nativeSendGamepadKey(long j, int i, boolean z);

    private static native void nativeSendKey(long j, int i, boolean z);

    private static native void nativeSendPointerDown(long j, int i, int i2, int i3);

    private static native void nativeSendPointerSync(long j);

    private static native void nativeSendPointerUp(long j, int i);

    public UinputBridge(android.os.IBinder token, java.lang.String name, int width, int height, int maxPointers) throws java.io.IOException {
        if (width < 1 || height < 1) {
            throw new java.lang.IllegalArgumentException("Touchpad must be at least 1x1.");
        }
        if (maxPointers < 1 || maxPointers > 32) {
            throw new java.lang.IllegalArgumentException("Touchpad must support between 1 and 32 pointers.");
        }
        if (token == null) {
            throw new java.lang.IllegalArgumentException("Token cannot be null");
        }
        this.mPtr = nativeOpen(name, token.toString(), width, height, maxPointers);
        if (this.mPtr == 0) {
            throw new java.io.IOException("Could not open uinput device " + name);
        }
        this.mToken = token;
        this.mCloseGuard.open("close");
    }

    private UinputBridge(android.os.IBinder token, long ptr) {
        this.mPtr = ptr;
        this.mToken = token;
        this.mCloseGuard.open("close");
    }

    public static com.android.server.tv.UinputBridge openGamepad(android.os.IBinder token, java.lang.String name) throws java.io.IOException {
        if (token == null) {
            throw new java.lang.IllegalArgumentException("Token cannot be null");
        }
        long ptr = nativeGamepadOpen(name, token.toString());
        if (ptr == 0) {
            throw new java.io.IOException("Could not open uinput device " + name);
        }
        return new com.android.server.tv.UinputBridge(token, ptr);
    }

    protected void finalize() throws java.lang.Throwable {
        try {
            this.mCloseGuard.warnIfOpen();
            close(this.mToken);
        } finally {
            this.mToken = null;
            super.finalize();
        }
    }

    public void close(android.os.IBinder token) {
        if (isTokenValid(token) && this.mPtr != 0) {
            clear(token);
            nativeClose(this.mPtr);
            this.mPtr = 0L;
            this.mCloseGuard.close();
        }
    }

    public android.os.IBinder getToken() {
        return this.mToken;
    }

    protected boolean isTokenValid(android.os.IBinder token) {
        return this.mToken.equals(token);
    }

    public void sendKeyDown(android.os.IBinder token, int keyCode) {
        if (isTokenValid(token)) {
            nativeSendKey(this.mPtr, keyCode, true);
        }
    }

    public void sendKeyUp(android.os.IBinder token, int keyCode) {
        if (isTokenValid(token)) {
            nativeSendKey(this.mPtr, keyCode, false);
        }
    }

    public void sendPointerDown(android.os.IBinder token, int pointerId, int x, int y) {
        if (isTokenValid(token)) {
            nativeSendPointerDown(this.mPtr, pointerId, x, y);
        }
    }

    public void sendPointerUp(android.os.IBinder token, int pointerId) {
        if (isTokenValid(token)) {
            nativeSendPointerUp(this.mPtr, pointerId);
        }
    }

    public void sendPointerSync(android.os.IBinder token) {
        if (isTokenValid(token)) {
            nativeSendPointerSync(this.mPtr);
        }
    }

    public void sendGamepadKey(android.os.IBinder token, int keyCode, boolean down) {
        if (isTokenValid(token)) {
            nativeSendGamepadKey(this.mPtr, keyCode, down);
        }
    }

    public void sendGamepadAxisValue(android.os.IBinder token, int axis, float value) {
        if (isTokenValid(token)) {
            nativeSendGamepadAxisValue(this.mPtr, axis, value);
        }
    }

    public void clear(android.os.IBinder token) {
        if (isTokenValid(token)) {
            nativeClear(this.mPtr);
        }
    }
}
