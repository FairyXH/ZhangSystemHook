package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
public interface IInputManagerServiceWrapper {
    default com.android.server.input.NativeInputManagerService getNative() {
        return null;
    }

    default com.android.server.input.IInputManagerServiceExt getExtImpl() {
        return null;
    }

    default java.lang.Object getInputFilterLock() {
        return new java.lang.Object();
    }

    default com.android.server.input.InputManagerService.WindowManagerCallbacks getWindowManagerCallbacks() {
        return null;
    }
}
