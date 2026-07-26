package com.google.security.cryptauth.lib.securegcm.ukey2;

/* JADX INFO: loaded from: classes3.dex */
public class AlertException extends java.lang.Exception {
    private final byte[] alertMessageToSend;

    public AlertException(java.lang.String message, byte[] alertMessageToSend) {
        super(message);
        this.alertMessageToSend = alertMessageToSend;
    }

    public byte[] getAlertMessageToSend() {
        return this.alertMessageToSend;
    }
}
