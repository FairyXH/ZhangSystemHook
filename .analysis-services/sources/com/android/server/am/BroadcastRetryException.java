package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class BroadcastRetryException extends com.android.server.am.BroadcastDeliveryFailedException {
    public BroadcastRetryException(java.lang.String name) {
        super(name);
    }

    public BroadcastRetryException(java.lang.Exception cause) {
        super(cause);
    }
}
