package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public interface IRemoteFillServiceWrapper {
    default com.android.server.autofill.IRemoteFillServiceExt getRemoteFillServiceExt() {
        return null;
    }

    default void delayCancelRequest(java.util.List<android.service.autofill.FillContext> fillContexts) {
    }
}
