package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class HalException extends java.lang.RuntimeException {
    public final int errorCode;

    public HalException(int errorCode, java.lang.String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public HalException(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override // java.lang.Throwable
    public java.lang.String toString() {
        return super.toString() + " (code " + this.errorCode + ")";
    }
}
