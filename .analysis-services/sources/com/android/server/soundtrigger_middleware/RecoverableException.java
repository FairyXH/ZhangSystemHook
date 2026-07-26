package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class RecoverableException extends java.lang.RuntimeException {
    public final int errorCode;

    public RecoverableException(int errorCode, java.lang.String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public RecoverableException(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override // java.lang.Throwable
    public java.lang.String toString() {
        return super.toString() + " (code " + this.errorCode + ")";
    }
}
