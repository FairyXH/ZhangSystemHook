package com.android.server.location.provider;

/* JADX INFO: loaded from: classes2.dex */
public interface IAbstractLocationProviderWrapper {
    default com.android.server.location.provider.AbstractLocationProvider.State setListener(com.android.server.location.provider.AbstractLocationProvider.Listener listener) {
        return null;
    }

    default boolean isStarted() {
        return false;
    }

    default void start() {
    }

    default void stop() {
    }

    default void setRequest(android.location.provider.ProviderRequest request) {
    }

    default void flush(java.lang.Runnable listener) {
    }
}
