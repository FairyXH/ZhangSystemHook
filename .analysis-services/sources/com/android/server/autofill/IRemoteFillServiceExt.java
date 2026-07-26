package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public interface IRemoteFillServiceExt {
    default long getOplusTimeoutMillis(android.content.ComponentName componentName, long nativeTimeoutMills) {
        return nativeTimeoutMills;
    }

    default long getOplusTimeoutIdleBindMillis(android.content.ComponentName componentName, long nativeTimeoutMills) {
        return nativeTimeoutMills;
    }

    default long getOplusRequestTimeoutMillis(android.content.ComponentName componentName, long nativeTimeoutMills) {
        return nativeTimeoutMills;
    }

    default void delayCancelRequest(java.util.List<android.service.autofill.FillContext> fillContexts, int requestId, java.util.concurrent.CompletableFuture<android.service.autofill.FillResponse> pendingRequest) {
    }

    default boolean hookIfNeedForceCallOnFillRequestSuccess(boolean requestIsNull, java.lang.String packageName, android.service.autofill.FillRequest fillRequest, android.service.autofill.FillResponse response) {
        return false;
    }

    default void setSessionDestroyedLocked(boolean destroyed) {
    }

    default boolean getSessionDestroyed() {
        return false;
    }
}
