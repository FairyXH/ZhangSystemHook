package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IShortcutBitmapSaverExt {
    default boolean waitForAllSaves() throws android.util.AndroidException {
        throw new android.util.AndroidException("Undefined impl !!!");
    }

    default boolean processPendingItems(java.util.concurrent.ExecutorService executor, java.lang.Runnable r) {
        return false;
    }
}
