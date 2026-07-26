package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class AppFuseMountException extends java.lang.Exception {
    public AppFuseMountException(java.lang.String detailMessage) {
        super(detailMessage);
    }

    public AppFuseMountException(java.lang.String detailMessage, java.lang.Throwable throwable) {
        super(detailMessage, throwable);
    }

    public java.lang.IllegalArgumentException rethrowAsParcelableException() {
        throw new java.lang.IllegalStateException(getMessage(), this);
    }
}
