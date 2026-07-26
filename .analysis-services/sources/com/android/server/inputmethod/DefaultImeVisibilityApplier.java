package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class DefaultImeVisibilityApplier implements com.android.server.inputmethod.ImeVisibilityApplier {
    private static boolean DEBUG = com.android.server.inputmethod.InputMethodManagerService.DEBUG;
    private static final java.lang.String TAG = "DefaultImeVisibilityApplier";
    private com.android.server.inputmethod.InputMethodManagerService mService;
    private final com.android.server.wm.WindowManagerInternal mWindowManagerInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
    private final com.android.server.wm.ImeTargetVisibilityPolicy mImeTargetVisibilityPolicy = (com.android.server.wm.ImeTargetVisibilityPolicy) com.android.server.LocalServices.getService(com.android.server.wm.ImeTargetVisibilityPolicy.class);

    DefaultImeVisibilityApplier(com.android.server.inputmethod.InputMethodManagerService service) {
        this.mService = service;
    }

    @Override // com.android.server.inputmethod.ImeVisibilityApplier
    public void performShowIme(android.os.IBinder showInputToken, android.view.inputmethod.ImeTracker.Token statsToken, int showFlags, android.os.ResultReceiver resultReceiver, int reason) {
        com.android.server.inputmethod.IInputMethodInvoker curMethod = this.mService.getCurMethodLocked();
        if (curMethod != null) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Calling " + curMethod + ".showSoftInput(" + showInputToken + ", " + showFlags + ", " + resultReceiver + ") for reason: " + com.android.internal.inputmethod.InputMethodDebug.softInputDisplayReasonToString(reason));
            }
            this.mService.getWrapper().getExtImpl().setAsyncBinderUxFlag(true);
            boolean showSoftInputResult = curMethod.showSoftInput(showInputToken, statsToken, showFlags, resultReceiver);
            this.mService.getWrapper().getExtImpl().setAsyncBinderUxFlag(false);
            if (showSoftInputResult) {
                if (android.view.inputmethod.ImeTracker.DEBUG_IME_VISIBILITY) {
                    android.util.EventLog.writeEvent(com.android.server.EventLogTags.IMF_SHOW_IME, statsToken != null ? statsToken.getTag() : "TOKEN_NONE", java.util.Objects.toString(this.mService.mImeBindingState.mFocusedWindow), com.android.internal.inputmethod.InputMethodDebug.softInputDisplayReasonToString(reason), com.android.internal.inputmethod.InputMethodDebug.softInputModeToString(this.mService.mImeBindingState.mFocusedWindowSoftInputMode));
                }
                this.mService.onShowHideSoftInputRequested(true, showInputToken, reason, statsToken);
            }
        }
    }

    @Override // com.android.server.inputmethod.ImeVisibilityApplier
    public void performHideIme(android.os.IBinder hideInputToken, android.view.inputmethod.ImeTracker.Token statsToken, android.os.ResultReceiver resultReceiver, int reason) {
        com.android.server.inputmethod.IInputMethodInvoker curMethod = this.mService.getCurMethodLocked();
        if (curMethod != null) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Calling " + curMethod + ".hideSoftInput(0, " + hideInputToken + ", " + resultReceiver + ") for reason: " + com.android.internal.inputmethod.InputMethodDebug.softInputDisplayReasonToString(reason));
            }
            this.mService.getWrapper().getExtImpl().setAsyncBinderUxFlag(true);
            boolean hideSoftInputResult = curMethod.hideSoftInput(hideInputToken, statsToken, 0, resultReceiver);
            this.mService.getWrapper().getExtImpl().setAsyncBinderUxFlag(false);
            if (hideSoftInputResult) {
                if (android.view.inputmethod.ImeTracker.DEBUG_IME_VISIBILITY) {
                    android.util.EventLog.writeEvent(com.android.server.EventLogTags.IMF_HIDE_IME, statsToken != null ? statsToken.getTag() : "TOKEN_NONE", java.util.Objects.toString(this.mService.mImeBindingState.mFocusedWindow), com.android.internal.inputmethod.InputMethodDebug.softInputDisplayReasonToString(reason), com.android.internal.inputmethod.InputMethodDebug.softInputModeToString(this.mService.mImeBindingState.mFocusedWindowSoftInputMode));
                }
                this.mService.onShowHideSoftInputRequested(false, hideInputToken, reason, statsToken);
            }
        }
    }

    @Override // com.android.server.inputmethod.ImeVisibilityApplier
    public void applyImeVisibility(android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, int state, int userId) {
        applyImeVisibility(windowToken, statsToken, state, 0, userId);
    }

    void applyImeVisibility(android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, int state, int reason, int userId) {
        com.android.server.inputmethod.InputMethodBindingController bindingController = this.mService.getInputMethodBindingController(userId);
        int displayIdToShowIme = bindingController.getDisplayIdToShowIme();
        switch (state) {
            case 0:
                if (!android.view.inputmethod.Flags.refactorInsetsController()) {
                    if (this.mService.hasAttachedClient()) {
                        android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 17);
                        this.mWindowManagerInternal.hideIme(windowToken, displayIdToShowIme, statsToken);
                        return;
                    } else {
                        android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 17);
                        return;
                    }
                }
                return;
            case 1:
                if (!android.view.inputmethod.Flags.refactorInsetsController()) {
                    android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 17);
                    this.mWindowManagerInternal.showImePostLayout(windowToken, statsToken);
                    return;
                }
                return;
            case 2:
            case 3:
            default:
                throw new java.lang.IllegalArgumentException("Invalid IME visibility state: " + state);
            case 4:
                showImeScreenshot(windowToken, displayIdToShowIme);
                return;
            case 5:
                if (android.view.inputmethod.Flags.refactorInsetsController()) {
                    setImeVisibilityOnFocusedWindowClient(false);
                    return;
                } else {
                    this.mService.hideCurrentInputLocked(windowToken, statsToken, 0, null, reason);
                    return;
                }
            case 6:
                if (android.view.inputmethod.Flags.refactorInsetsController()) {
                    setImeVisibilityOnFocusedWindowClient(false);
                    return;
                } else {
                    this.mService.hideCurrentInputLocked(windowToken, statsToken, 2, null, reason);
                    return;
                }
            case 7:
                if (android.view.inputmethod.Flags.refactorInsetsController()) {
                    setImeVisibilityOnFocusedWindowClient(true);
                    return;
                } else {
                    this.mService.showCurrentInputLocked(windowToken, statsToken, 1, 0, null, reason);
                    return;
                }
            case 8:
                removeImeScreenshot(displayIdToShowIme);
                return;
        }
    }

    @Override // com.android.server.inputmethod.ImeVisibilityApplier
    public boolean showImeScreenshot(android.os.IBinder imeTarget, int displayId) {
        if (this.mService.getWrapper().getExtImpl().isCarDisplayId(displayId) || !this.mImeTargetVisibilityPolicy.showImeScreenshot(imeTarget, displayId)) {
            return false;
        }
        this.mService.onShowHideSoftInputRequested(false, imeTarget, 34, null);
        return true;
    }

    @Override // com.android.server.inputmethod.ImeVisibilityApplier
    public boolean removeImeScreenshot(int displayId) {
        if (!this.mImeTargetVisibilityPolicy.removeImeScreenshot(displayId)) {
            return false;
        }
        this.mService.onShowHideSoftInputRequested(false, this.mService.mImeBindingState.mFocusedWindow, 35, null);
        return true;
    }

    private void setImeVisibilityOnFocusedWindowClient(boolean visibility) {
        if (this.mService.mImeBindingState != null && this.mService.mImeBindingState.mFocusedWindowClient != null && this.mService.mImeBindingState.mFocusedWindowClient.mClient != null) {
            this.mService.mImeBindingState.mFocusedWindowClient.mClient.setImeVisibility(visibility);
        }
    }
}
