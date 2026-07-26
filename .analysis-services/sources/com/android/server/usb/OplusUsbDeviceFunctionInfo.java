package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public class OplusUsbDeviceFunctionInfo {
    private java.lang.String mContentStr;

    public OplusUsbDeviceFunctionInfo(java.lang.String functions, java.lang.String oemFunctions, java.lang.String currentFunctions, boolean currentFunctionsApplied, boolean forceRestart, java.lang.String currentOemFunctions) {
        this.mContentStr = null;
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("UsbFunc[").append("fun:").append(functions);
        sb.append(", oemFun:").append(oemFunctions).append(", curFun:").append(currentFunctions);
        sb.append(", curFunApplied:").append(currentFunctionsApplied);
        sb.append(", forceRestart:").append(forceRestart);
        sb.append(", curOemFun:").append(currentOemFunctions);
        this.mContentStr = sb.toString();
    }

    public java.lang.String toString() {
        return this.mContentStr;
    }
}
