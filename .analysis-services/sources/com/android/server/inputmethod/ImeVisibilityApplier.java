package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
interface ImeVisibilityApplier {
    default void performShowIme(android.os.IBinder showInputToken, android.view.inputmethod.ImeTracker.Token statsToken, int showFlags, android.os.ResultReceiver resultReceiver, int reason) {
    }

    default void performHideIme(android.os.IBinder hideInputToken, android.view.inputmethod.ImeTracker.Token statsToken, android.os.ResultReceiver resultReceiver, int reason) {
    }

    default void applyImeVisibility(android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, int state, int userId) {
    }

    default void updateImeLayeringByTarget(android.os.IBinder windowToken) {
    }

    default boolean showImeScreenshot(android.os.IBinder windowToken, int displayId) {
        return false;
    }

    default boolean removeImeScreenshot(int displayId) {
        return false;
    }
}
