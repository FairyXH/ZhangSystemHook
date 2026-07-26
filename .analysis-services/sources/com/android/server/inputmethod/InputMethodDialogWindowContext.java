package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
public final class InputMethodDialogWindowContext {
    private android.content.Context mDialogWindowContext;

    public android.content.Context get(int displayId) {
        if (this.mDialogWindowContext == null || this.mDialogWindowContext.getDisplayId() != displayId) {
            android.content.Context windowContext = android.app.ActivityThread.currentActivityThread().getSystemUiContext(displayId).createWindowContext(2012, null);
            this.mDialogWindowContext = new android.view.ContextThemeWrapper(windowContext, android.R.style.Theme.DeviceDefault.Settings);
        }
        android.content.Context systemUiContext = this.mDialogWindowContext;
        return systemUiContext;
    }
}
