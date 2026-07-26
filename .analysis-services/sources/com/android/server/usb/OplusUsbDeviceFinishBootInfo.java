package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public class OplusUsbDeviceFinishBootInfo {
    private java.lang.String mContentStr;

    public OplusUsbDeviceFinishBootInfo(boolean connected, boolean bootCompleted, boolean currentUsbFunctionsReceived, boolean systemReady, boolean pendingBootBroadcast, boolean screenLocked, java.lang.String screenUnlockedFunctions, boolean isAdbEnabled) {
        this.mContentStr = null;
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("UsbBootInfo[").append("connected:").append(connected);
        sb.append(", bootComplete:").append(bootCompleted);
        sb.append(", curUsbFunRec:").append(currentUsbFunctionsReceived);
        sb.append(", systemReady:").append(systemReady);
        sb.append(", pendingBoot:").append(pendingBootBroadcast);
        sb.append(", screenLock:").append(screenLocked);
        sb.append(", screenUnlock:").append(screenUnlockedFunctions);
        sb.append(", adbEnable:").append(isAdbEnabled);
        this.mContentStr = sb.toString();
    }

    public java.lang.String toString() {
        return this.mContentStr;
    }
}
