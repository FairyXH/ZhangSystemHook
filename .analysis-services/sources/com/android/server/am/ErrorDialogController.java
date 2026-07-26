package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class ErrorDialogController {
    private android.app.AnrController mAnrController;
    private java.util.List<com.android.server.am.AppNotRespondingDialog> mAnrDialogs;
    private final com.android.server.am.ProcessRecord mApp;
    private java.util.List<com.android.server.am.AppErrorDialog> mCrashDialogs;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    private final com.android.server.am.ActivityManagerService mService;
    private java.util.List<com.android.server.am.StrictModeViolationDialog> mViolationDialogs;
    private com.android.server.am.AppWaitingForDebuggerDialog mWaitDialog;

    boolean hasCrashDialogs() {
        return this.mCrashDialogs != null;
    }

    java.util.List<com.android.server.am.AppErrorDialog> getCrashDialogs() {
        return this.mCrashDialogs;
    }

    boolean hasAnrDialogs() {
        return this.mAnrDialogs != null;
    }

    java.util.List<com.android.server.am.AppNotRespondingDialog> getAnrDialogs() {
        return this.mAnrDialogs;
    }

    boolean hasViolationDialogs() {
        return this.mViolationDialogs != null;
    }

    boolean hasDebugWaitingDialog() {
        return this.mWaitDialog != null;
    }

    void clearAllErrorDialogs() {
        clearCrashDialogs();
        clearAnrDialogs();
        clearViolationDialogs();
        clearWaitingDialog();
        clearAnrErrorDialogs();
        clearAnrErrorProgressDialogs();
    }

    void clearCrashDialogs() {
        clearCrashDialogs(true);
    }

    void clearCrashDialogs(boolean needDismiss) {
        if (this.mCrashDialogs == null) {
            return;
        }
        if (needDismiss) {
            scheduleForAllDialogs(this.mCrashDialogs, new com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda0());
        }
        this.mCrashDialogs = null;
    }

    void clearAnrDialogs() {
        if (this.mAnrDialogs == null) {
            return;
        }
        scheduleForAllDialogs(this.mAnrDialogs, new com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda0());
        this.mAnrDialogs = null;
        this.mAnrController = null;
    }

    void clearViolationDialogs() {
        if (this.mViolationDialogs == null) {
            return;
        }
        scheduleForAllDialogs(this.mViolationDialogs, new com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda0());
        this.mViolationDialogs = null;
    }

    void clearWaitingDialog() {
        if (this.mWaitDialog == null) {
            return;
        }
        final com.android.server.am.BaseErrorDialog dialog = this.mWaitDialog;
        android.os.Handler handler = this.mService.mUiHandler;
        java.util.Objects.requireNonNull(dialog);
        handler.post(new java.lang.Runnable() { // from class: com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                dialog.dismiss();
            }
        });
        this.mWaitDialog = null;
    }

    void scheduleForAllDialogs(final java.util.List<? extends com.android.server.am.BaseErrorDialog> dialogs, final java.util.function.Consumer<com.android.server.am.BaseErrorDialog> c) {
        this.mService.mUiHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleForAllDialogs$0(dialogs, c);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleForAllDialogs$0(java.util.List dialogs, java.util.function.Consumer c) {
        if (dialogs != null) {
            forAllDialogs(dialogs, c);
        }
    }

    void forAllDialogs(java.util.List<? extends com.android.server.am.BaseErrorDialog> dialogs, java.util.function.Consumer<com.android.server.am.BaseErrorDialog> c) {
        for (int i = dialogs.size() - 1; i >= 0; i--) {
            c.accept(dialogs.get(i));
        }
    }

    void showCrashDialogs(com.android.server.am.AppErrorDialog.Data data) {
        java.util.List<android.content.Context> contexts = getDisplayContexts(false);
        this.mCrashDialogs = new java.util.ArrayList();
        for (int i = contexts.size() - 1; i >= 0; i--) {
            android.content.Context c = contexts.get(i);
            this.mCrashDialogs.add(new com.android.server.am.AppErrorDialog(c, this.mService, data));
        }
        this.mService.mUiHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showCrashDialogs$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCrashDialogs$1() {
        java.util.List<com.android.server.am.AppErrorDialog> dialogs;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                dialogs = this.mCrashDialogs;
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        if (dialogs != null) {
            forAllDialogs(dialogs, new com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda4());
        }
    }

    void showAnrDialogs(com.android.server.am.AppNotRespondingDialog.Data data) {
        java.util.List<android.content.Context> contexts = getDisplayContexts(this.mApp.mErrorState.isSilentAnr());
        this.mAnrDialogs = new java.util.ArrayList();
        for (int i = contexts.size() - 1; i >= 0; i--) {
            android.content.Context c = contexts.get(i);
            this.mAnrDialogs.add(new com.android.server.am.AppNotRespondingDialog(this.mService, c, data));
        }
        scheduleForAllDialogs(this.mAnrDialogs, new com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda4());
    }

    void showViolationDialogs(com.android.server.am.AppErrorResult res) {
        java.util.List<android.content.Context> contexts = getDisplayContexts(false);
        this.mViolationDialogs = new java.util.ArrayList();
        for (int i = contexts.size() - 1; i >= 0; i--) {
            android.content.Context c = contexts.get(i);
            this.mViolationDialogs.add(new com.android.server.am.StrictModeViolationDialog(c, this.mService, res, this.mApp));
        }
        scheduleForAllDialogs(this.mViolationDialogs, new com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda4());
    }

    void showDebugWaitingDialogs() {
        java.util.List<android.content.Context> contexts = getDisplayContexts(true);
        android.content.Context c = contexts.get(0);
        this.mWaitDialog = new com.android.server.am.AppWaitingForDebuggerDialog(this.mService, c, this.mApp);
        this.mService.mUiHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.ErrorDialogController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showDebugWaitingDialogs$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showDebugWaitingDialogs$2() {
        android.app.Dialog dialog;
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                dialog = this.mWaitDialog;
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        if (dialog != null) {
            dialog.show();
        }
    }

    void showAnrErrorDialogs(int aboveSystem) {
        java.util.List<android.content.Context> contexts = getDisplayContexts(this.mApp.mErrorState.isSilentAnr());
        this.mApp.mErrorState.mProcessErrorStateRecordExt.showAnrErrorDialogs(this.mService, contexts, this.mApp, aboveSystem);
    }

    void showAnrProgressDialogs() {
        java.util.List<android.content.Context> contexts = getDisplayContexts(this.mApp.mErrorState.isSilentAnr());
        this.mApp.mErrorState.mProcessErrorStateRecordExt.showAnrErrorProgressDialogs(this.mService, contexts, this.mApp);
    }

    void clearAnrErrorDialogs() {
        this.mApp.mErrorState.mProcessErrorStateRecordExt.clearAnrErrorDialogs(this.mService, this.mApp);
    }

    void clearAnrErrorProgressDialogs() {
        this.mApp.mErrorState.mProcessErrorStateRecordExt.clearAnrErrorProgressDialogs(this.mService, this.mApp);
    }

    android.app.AnrController getAnrController() {
        return this.mAnrController;
    }

    void setAnrController(android.app.AnrController controller) {
        this.mAnrController = controller;
    }

    private java.util.List<android.content.Context> getDisplayContexts(boolean lastUsedOnly) {
        android.content.Context topFocusedDisplayUiContext;
        java.util.List<android.content.Context> displayContexts = new java.util.ArrayList<>();
        if (!lastUsedOnly) {
            this.mApp.getWindowProcessController().getDisplayContextsWithErrorDialogs(displayContexts);
        }
        if (displayContexts.isEmpty() || lastUsedOnly) {
            if (this.mService.mWmInternal != null) {
                topFocusedDisplayUiContext = this.mService.mWmInternal.getTopFocusedDisplayUiContext();
            } else {
                topFocusedDisplayUiContext = this.mService.mUiContext;
            }
            displayContexts.add(topFocusedDisplayUiContext);
        }
        return displayContexts;
    }

    ErrorDialogController(com.android.server.am.ProcessRecord app) {
        this.mApp = app;
        this.mService = app.mService;
        this.mProcLock = this.mService.mProcLock;
    }
}
