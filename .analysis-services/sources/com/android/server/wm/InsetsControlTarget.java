package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
interface InsetsControlTarget {
    default void notifyInsetsControlChanged(int displayId) {
    }

    default com.android.server.wm.WindowState getWindow() {
        return null;
    }

    default boolean isRequestedVisible(int types) {
        return (android.view.WindowInsets.Type.defaultVisible() & types) != 0;
    }

    default int getRequestedVisibleTypes() {
        return android.view.WindowInsets.Type.defaultVisible();
    }

    default void showInsets(int types, boolean fromIme, android.view.inputmethod.ImeTracker.Token statsToken) {
    }

    default void hideInsets(int types, boolean fromIme, android.view.inputmethod.ImeTracker.Token statsToken) {
    }

    default boolean canShowTransient() {
        return false;
    }

    default void setImeInputTargetRequestedVisibility(boolean visible) {
    }

    static com.android.server.wm.WindowState asWindowOrNull(com.android.server.wm.InsetsControlTarget target) {
        if (target != null) {
            return target.getWindow();
        }
        return null;
    }
}
