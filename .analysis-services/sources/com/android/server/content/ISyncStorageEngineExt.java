package com.android.server.content;

/* JADX INFO: loaded from: classes.dex */
public interface ISyncStorageEngineExt {
    default boolean isDataSyncDisabled() {
        return false;
    }

    default void init(android.content.Context context) {
    }
}
