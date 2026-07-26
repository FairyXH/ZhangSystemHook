package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IMultiDisplayRefreshRateManagerExt {
    default void init(android.content.Context context) {
    }

    default boolean isSupport() {
        return false;
    }
}
