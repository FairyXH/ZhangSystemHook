package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class ImeBindingState {
    final android.os.IBinder mFocusedWindow;
    final com.android.server.inputmethod.ClientState mFocusedWindowClient;
    final android.view.inputmethod.EditorInfo mFocusedWindowEditorInfo;
    final int mFocusedWindowSoftInputMode;
    final int mUserId;

    void dumpDebug(android.util.proto.ProtoOutputStream proto, com.android.server.wm.WindowManagerInternal windowManagerInternal) {
        proto.write(1138166333444L, windowManagerInternal.getWindowName(this.mFocusedWindow));
        proto.write(1138166333446L, com.android.internal.inputmethod.InputMethodDebug.softInputModeToString(this.mFocusedWindowSoftInputMode));
    }

    void dump(java.lang.String prefix, android.util.Printer p) {
        p.println(prefix + "mFocusedWindow()=" + this.mFocusedWindow);
        p.println(prefix + "softInputMode=" + com.android.internal.inputmethod.InputMethodDebug.softInputModeToString(this.mFocusedWindowSoftInputMode));
        p.println(prefix + "mFocusedWindowClient=" + this.mFocusedWindowClient);
    }

    static com.android.server.inputmethod.ImeBindingState newEmptyState() {
        return new com.android.server.inputmethod.ImeBindingState(-10000, null, 0, null, null);
    }

    ImeBindingState(int userId, android.os.IBinder focusedWindow, int focusedWindowSoftInputMode, com.android.server.inputmethod.ClientState focusedWindowClient, android.view.inputmethod.EditorInfo focusedWindowEditorInfo) {
        this.mUserId = userId;
        this.mFocusedWindow = focusedWindow;
        this.mFocusedWindowSoftInputMode = focusedWindowSoftInputMode;
        this.mFocusedWindowClient = focusedWindowClient;
        this.mFocusedWindowEditorInfo = focusedWindowEditorInfo;
    }
}
