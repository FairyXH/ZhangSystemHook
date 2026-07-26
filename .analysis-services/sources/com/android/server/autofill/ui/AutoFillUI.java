package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
public final class AutoFillUI {
    private static final java.lang.String TAG = "AutofillUI";
    private com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback mCallback;
    private final android.content.Context mContext;
    private java.lang.Runnable mCreateFillUiRunnable;
    private com.android.server.autofill.ui.DialogFillUi mFillDialog;
    private com.android.server.autofill.ui.FillUi mFillUi;
    private final com.android.server.autofill.ui.OverlayControl mOverlayControl;
    private com.android.server.autofill.ui.SaveUi mSaveUi;
    private com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback mSaveUiCallback;
    private final android.os.Handler mHandler = com.android.server.UiThread.getHandler();
    private final com.android.internal.logging.MetricsLogger mMetricsLogger = new com.android.internal.logging.MetricsLogger();
    private final com.android.server.UiModeManagerInternal mUiModeMgr = (com.android.server.UiModeManagerInternal) com.android.server.LocalServices.getService(com.android.server.UiModeManagerInternal.class);

    public interface AutoFillUiCallback {
        void authenticate(int i, int i2, android.content.IntentSender intentSender, android.os.Bundle bundle, int i3);

        void cancelSave();

        void cancelSession();

        void dispatchUnhandledKey(android.view.autofill.AutofillId autofillId, android.view.KeyEvent keyEvent);

        void fill(int i, int i2, android.service.autofill.Dataset dataset, int i3);

        void onShown(int i, int i2);

        void requestFallbackFromFillDialog();

        void requestHideFillUi(android.view.autofill.AutofillId autofillId);

        void requestHideFillUiWhenDestroyed(android.view.autofill.AutofillId autofillId);

        void requestShowFillUi(android.view.autofill.AutofillId autofillId, int i, int i2, android.view.autofill.IAutofillWindowPresenter iAutofillWindowPresenter);

        void requestShowSoftInput(android.view.autofill.AutofillId autofillId);

        void save();

        void startIntentSender(android.content.IntentSender intentSender, android.content.Intent intent);

        void startIntentSenderAndFinishSession(android.content.IntentSender intentSender);
    }

    public AutoFillUI(android.content.Context context) {
        this.mContext = context;
        this.mOverlayControl = new com.android.server.autofill.ui.OverlayControl(context);
    }

    public void setCallback(final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.AutoFillUI$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setCallback$0(callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCallback$0(com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        if (this.mCallback != callback) {
            if (this.mCallback != null) {
                if (isSaveUiShowing()) {
                    hideFillUiUiThread(callback, true);
                } else {
                    lambda$hideAll$10(this.mCallback);
                }
            }
            this.mCallback = callback;
        }
    }

    public void clearCallback(final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.AutoFillUI$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$clearCallback$1(callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearCallback$1(com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        if (this.mCallback == callback) {
            lambda$hideAll$10(callback);
            this.mCallback = null;
        }
    }

    public void showError(int resId, com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        showError(this.mContext.getString(resId), callback);
    }

    public void showError(final java.lang.CharSequence message, final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        android.util.Slog.w(TAG, "showError(): " + ((java.lang.Object) message));
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.AutoFillUI$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showError$2(callback, message);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showError$2(com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback, java.lang.CharSequence message) {
        if (this.mCallback != callback) {
            return;
        }
        lambda$hideAll$10(callback);
        if (!android.text.TextUtils.isEmpty(message)) {
            android.widget.Toast.makeText(this.mContext, message, 1).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideFillUi$3(com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        hideFillUiUiThread(callback, true);
    }

    public void hideFillUi(final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.AutoFillUI$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$hideFillUi$3(callback);
            }
        });
    }

    public void hideFillDialog(final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.AutoFillUI$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$hideFillDialog$4(callback);
            }
        });
    }

    public void filterFillUi(final java.lang.String filterText, final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.AutoFillUI$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$filterFillUi$5(callback, filterText);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$filterFillUi$5(com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback, java.lang.String filterText) {
        if (callback == this.mCallback && this.mFillUi != null) {
            this.mFillUi.setFilterText(filterText);
        }
    }

    public void showFillUi(final android.view.autofill.AutofillId focusedId, final android.service.autofill.FillResponse response, final java.lang.String filterText, java.lang.String servicePackageName, android.content.ComponentName componentName, final java.lang.CharSequence serviceLabel, final android.graphics.drawable.Drawable serviceIcon, final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback, final android.content.Context context, int sessionId, boolean compatMode, final int maxInputLengthForAutofill) {
        if (com.android.server.autofill.Helper.sDebug) {
            int size = filterText == null ? 0 : filterText.length();
            com.android.server.utils.Slogf.d(TAG, "showFillUi(): id=%s, filter=%d chars, displayId=%d", focusedId, java.lang.Integer.valueOf(size), java.lang.Integer.valueOf(context.getDisplayId()));
        }
        final android.metrics.LogMaker log = com.android.server.autofill.Helper.newLogMaker(910, componentName, servicePackageName, sessionId, compatMode).addTaggedData(911, java.lang.Integer.valueOf(filterText == null ? 0 : filterText.length())).addTaggedData(909, java.lang.Integer.valueOf(response.getDatasets() != null ? response.getDatasets().size() : 0));
        java.lang.Runnable createFillUiRunnable = new java.lang.Runnable() { // from class: com.android.server.autofill.ui.AutoFillUI$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showFillUi$6(callback, context, response, focusedId, filterText, serviceLabel, serviceIcon, maxInputLengthForAutofill, log);
            }
        };
        if (isSaveUiShowing()) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "postpone fill UI request..");
            }
            this.mCreateFillUiRunnable = createFillUiRunnable;
            return;
        }
        this.mHandler.post(createFillUiRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showFillUi$6(final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback, android.content.Context context, final android.service.autofill.FillResponse response, final android.view.autofill.AutofillId focusedId, java.lang.String filterText, java.lang.CharSequence serviceLabel, android.graphics.drawable.Drawable serviceIcon, int maxInputLengthForAutofill, final android.metrics.LogMaker log) {
        if (callback != this.mCallback) {
            return;
        }
        lambda$hideAll$10(callback);
        this.mFillUi = new com.android.server.autofill.ui.FillUi(context, response, focusedId, filterText, this.mOverlayControl, serviceLabel, serviceIcon, this.mUiModeMgr.isNightMode(), maxInputLengthForAutofill, new com.android.server.autofill.ui.FillUi.Callback() { // from class: com.android.server.autofill.ui.AutoFillUI.1
            @Override // com.android.server.autofill.ui.FillUi.Callback
            public void onResponsePicked(android.service.autofill.FillResponse response2) {
                log.setType(3);
                com.android.server.autofill.ui.AutoFillUI.this.hideFillUiUiThread(callback, true);
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.authenticate(response2.getRequestId(), 65535, response2.getAuthentication(), response2.getClientState(), 1);
                }
            }

            @Override // com.android.server.autofill.ui.FillUi.Callback
            public void onShown(int datasetSize) {
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.onShown(1, datasetSize);
                }
            }

            @Override // com.android.server.autofill.ui.FillUi.Callback
            public void onDatasetPicked(android.service.autofill.Dataset dataset) {
                log.setType(4);
                com.android.server.autofill.ui.AutoFillUI.this.hideFillUiUiThread(callback, true);
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    int datasetIndex = response.getDatasets().indexOf(dataset);
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.fill(response.getRequestId(), datasetIndex, dataset, 1);
                }
            }

            @Override // com.android.server.autofill.ui.FillUi.Callback
            public void onCanceled() {
                log.setType(5);
                com.android.server.autofill.ui.AutoFillUI.this.hideFillUiUiThread(callback, true);
            }

            @Override // com.android.server.autofill.ui.FillUi.Callback
            public void onDestroy() {
                if (log.getType() == 0) {
                    log.setType(2);
                }
                com.android.server.autofill.ui.AutoFillUI.this.mMetricsLogger.write(log);
            }

            @Override // com.android.server.autofill.ui.FillUi.Callback
            public void requestShowFillUi(int width, int height, android.view.autofill.IAutofillWindowPresenter windowPresenter) {
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.requestShowFillUi(focusedId, width, height, windowPresenter);
                }
            }

            @Override // com.android.server.autofill.ui.FillUi.Callback
            public void requestHideFillUi() {
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.requestHideFillUi(focusedId);
                }
            }

            @Override // com.android.server.autofill.ui.FillUi.Callback
            public void requestHideFillUiWhenDestroyed() {
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.requestHideFillUiWhenDestroyed(focusedId);
                }
            }

            @Override // com.android.server.autofill.ui.FillUi.Callback
            public void startIntentSender(android.content.IntentSender intentSender) {
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.startIntentSenderAndFinishSession(intentSender);
                }
            }

            @Override // com.android.server.autofill.ui.FillUi.Callback
            public void dispatchUnhandledKey(android.view.KeyEvent keyEvent) {
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.dispatchUnhandledKey(focusedId, keyEvent);
                }
            }

            @Override // com.android.server.autofill.ui.FillUi.Callback
            public void cancelSession() {
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.cancelSession();
                }
            }
        });
    }

    public void showSaveUi(final java.lang.CharSequence serviceLabel, final android.graphics.drawable.Drawable serviceIcon, final java.lang.String servicePackageName, final android.service.autofill.SaveInfo info, final android.service.autofill.ValueFinder valueFinder, final android.content.ComponentName componentName, final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback, final android.content.Context context, final com.android.server.autofill.ui.PendingUi pendingSaveUi, final boolean isUpdate, final boolean compatMode, final boolean showServiceIcon, final com.android.server.autofill.SaveEventLogger mSaveEventLogger) {
        if (com.android.server.autofill.Helper.sVerbose) {
            com.android.server.utils.Slogf.v(TAG, "showSaveUi(update=%b) for %s and display %d: %s", java.lang.Boolean.valueOf(isUpdate), componentName.toShortString(), java.lang.Integer.valueOf(context.getDisplayId()), info);
        }
        int numIds = 0 + (info.getRequiredIds() == null ? 0 : info.getRequiredIds().length);
        final android.metrics.LogMaker log = com.android.server.autofill.Helper.newLogMaker(916, componentName, servicePackageName, pendingSaveUi.sessionId, compatMode).addTaggedData(917, java.lang.Integer.valueOf(numIds + (info.getOptionalIds() != null ? info.getOptionalIds().length : 0)));
        if (isUpdate) {
            log.addTaggedData(1555, 1);
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.AutoFillUI$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showSaveUi$7(callback, context, pendingSaveUi, serviceLabel, serviceIcon, servicePackageName, componentName, info, valueFinder, log, mSaveEventLogger, isUpdate, compatMode, showServiceIcon);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSaveUi$7(final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback, android.content.Context context, final com.android.server.autofill.ui.PendingUi pendingSaveUi, java.lang.CharSequence serviceLabel, android.graphics.drawable.Drawable serviceIcon, java.lang.String servicePackageName, android.content.ComponentName componentName, android.service.autofill.SaveInfo info, android.service.autofill.ValueFinder valueFinder, final android.metrics.LogMaker log, final com.android.server.autofill.SaveEventLogger mSaveEventLogger, boolean isUpdate, boolean compatMode, boolean showServiceIcon) {
        if (callback != this.mCallback) {
            return;
        }
        lambda$hideAll$10(callback);
        this.mSaveUiCallback = callback;
        this.mSaveUi = new com.android.server.autofill.ui.SaveUi(context, pendingSaveUi, serviceLabel, serviceIcon, servicePackageName, componentName, info, valueFinder, this.mOverlayControl, new com.android.server.autofill.ui.SaveUi.OnSaveListener() { // from class: com.android.server.autofill.ui.AutoFillUI.2
            @Override // com.android.server.autofill.ui.SaveUi.OnSaveListener
            public void onSave() {
                log.setType(4);
                if (mSaveEventLogger != null) {
                    mSaveEventLogger.maybeSetSaveButtonClicked(true);
                }
                com.android.server.autofill.ui.AutoFillUI.this.hideSaveUiUiThread(callback);
                callback.save();
                com.android.server.autofill.ui.AutoFillUI.this.destroySaveUiUiThread(pendingSaveUi, true);
            }

            @Override // com.android.server.autofill.ui.SaveUi.OnSaveListener
            public void onCancel(android.content.IntentSender listener) {
                log.setType(5);
                if (mSaveEventLogger != null) {
                    mSaveEventLogger.maybeSetCancelButtonClicked(true);
                }
                com.android.server.autofill.ui.AutoFillUI.this.hideSaveUiUiThread(callback);
                if (listener != null) {
                    try {
                        listener.sendIntent(com.android.server.autofill.ui.AutoFillUI.this.mContext, 0, null, null, null);
                    } catch (android.content.IntentSender.SendIntentException e) {
                        android.util.Slog.e(com.android.server.autofill.ui.AutoFillUI.TAG, "Error starting negative action listener: " + listener, e);
                    }
                }
                callback.cancelSave();
                com.android.server.autofill.ui.AutoFillUI.this.destroySaveUiUiThread(pendingSaveUi, true);
            }

            @Override // com.android.server.autofill.ui.SaveUi.OnSaveListener
            public void onDestroy() {
                if (log.getType() == 0) {
                    log.setType(2);
                    callback.cancelSave();
                }
                com.android.server.autofill.ui.AutoFillUI.this.mMetricsLogger.write(log);
                if (mSaveEventLogger != null) {
                    mSaveEventLogger.maybeSetDialogDismissed(true);
                }
            }

            @Override // com.android.server.autofill.ui.SaveUi.OnSaveListener
            public void startIntentSender(android.content.IntentSender intentSender, android.content.Intent intent) {
                callback.startIntentSender(intentSender, intent);
            }
        }, this.mUiModeMgr.isNightMode(), isUpdate, compatMode, showServiceIcon);
        mSaveEventLogger.maybeSetLatencySaveUiDisplayMillis();
    }

    public void showFillDialog(final android.view.autofill.AutofillId focusedId, final android.service.autofill.FillResponse response, final java.lang.String filterText, final java.lang.String servicePackageName, final android.content.ComponentName componentName, final android.graphics.drawable.Drawable serviceIcon, final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback, int sessionId, boolean compatMode, final com.android.server.autofill.PresentationStatsEventLogger presentationStatsEventLogger, final java.lang.Object sessionLock) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "showFillDialog for " + componentName.toShortString() + ": " + response);
        }
        final android.metrics.LogMaker log = com.android.server.autofill.Helper.newLogMaker(910, componentName, servicePackageName, sessionId, compatMode).addTaggedData(911, java.lang.Integer.valueOf(filterText == null ? 0 : filterText.length())).addTaggedData(909, java.lang.Integer.valueOf(response.getDatasets() != null ? response.getDatasets().size() : 0));
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.AutoFillUI$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showFillDialog$8(callback, response, focusedId, filterText, serviceIcon, servicePackageName, componentName, sessionLock, presentationStatsEventLogger, log);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showFillDialog$8(final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback, final android.service.autofill.FillResponse response, final android.view.autofill.AutofillId focusedId, java.lang.String filterText, android.graphics.drawable.Drawable serviceIcon, java.lang.String servicePackageName, android.content.ComponentName componentName, final java.lang.Object sessionLock, final com.android.server.autofill.PresentationStatsEventLogger presentationStatsEventLogger, final android.metrics.LogMaker log) {
        if (callback != this.mCallback) {
            return;
        }
        lambda$hideAll$10(callback);
        this.mFillDialog = new com.android.server.autofill.ui.DialogFillUi(this.mContext, response, focusedId, filterText, serviceIcon, servicePackageName, componentName, this.mOverlayControl, this.mUiModeMgr.isNightMode(), new com.android.server.autofill.ui.DialogFillUi.UiCallback() { // from class: com.android.server.autofill.ui.AutoFillUI.3
            @Override // com.android.server.autofill.ui.DialogFillUi.UiCallback
            public void onResponsePicked(android.service.autofill.FillResponse response2) {
                log(3);
                com.android.server.autofill.ui.AutoFillUI.this.lambda$hideFillDialog$4(callback);
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.authenticate(response2.getRequestId(), 65535, response2.getAuthentication(), response2.getClientState(), 3);
                }
            }

            @Override // com.android.server.autofill.ui.DialogFillUi.UiCallback
            public void onShown() {
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.onShown(3, response.getDatasets().size());
                }
            }

            @Override // com.android.server.autofill.ui.DialogFillUi.UiCallback
            public void onDatasetPicked(android.service.autofill.Dataset dataset) {
                log(4);
                synchronized (sessionLock) {
                    if (presentationStatsEventLogger != null) {
                        presentationStatsEventLogger.maybeSetPositiveCtaButtonClicked(true);
                    }
                }
                com.android.server.autofill.ui.AutoFillUI.this.lambda$hideFillDialog$4(callback);
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    int datasetIndex = response.getDatasets().indexOf(dataset);
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.fill(response.getRequestId(), datasetIndex, dataset, 3);
                }
            }

            @Override // com.android.server.autofill.ui.DialogFillUi.UiCallback
            public void onDismissed() {
                log(5);
                synchronized (sessionLock) {
                    if (presentationStatsEventLogger != null) {
                        presentationStatsEventLogger.maybeSetDialogDismissed(true);
                    }
                }
                com.android.server.autofill.ui.AutoFillUI.this.lambda$hideFillDialog$4(callback);
                callback.requestShowSoftInput(focusedId);
                callback.requestFallbackFromFillDialog();
            }

            @Override // com.android.server.autofill.ui.DialogFillUi.UiCallback
            public void onCanceled() {
                log(2);
                synchronized (sessionLock) {
                    if (presentationStatsEventLogger != null) {
                        presentationStatsEventLogger.maybeSetNegativeCtaButtonClicked(true);
                    }
                }
                com.android.server.autofill.ui.AutoFillUI.this.lambda$hideFillDialog$4(callback);
                callback.requestShowSoftInput(focusedId);
                callback.requestFallbackFromFillDialog();
            }

            @Override // com.android.server.autofill.ui.DialogFillUi.UiCallback
            public void startIntentSender(android.content.IntentSender intentSender) {
                if (com.android.server.autofill.ui.AutoFillUI.this.mCallback != null) {
                    com.android.server.autofill.ui.AutoFillUI.this.mCallback.startIntentSenderAndFinishSession(intentSender);
                }
            }

            private void log(int type) {
                log.setType(type);
                com.android.server.autofill.ui.AutoFillUI.this.mMetricsLogger.write(log);
            }
        });
    }

    public void onPendingSaveUi(final int operation, final android.os.IBinder token) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.AutoFillUI$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onPendingSaveUi$9(operation, token);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPendingSaveUi$9(int operation, android.os.IBinder token) {
        if (this.mSaveUi != null) {
            this.mSaveUi.onPendingUi(operation, token);
        } else {
            android.util.Slog.w(TAG, "onPendingSaveUi(" + operation + "): no save ui");
        }
    }

    public void hideAll(final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.AutoFillUI$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$hideAll$10(callback);
            }
        });
    }

    public void destroyAll(final com.android.server.autofill.ui.PendingUi pendingSaveUi, final com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback, final boolean notifyClient) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.AutoFillUI$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$destroyAll$11(pendingSaveUi, callback, notifyClient);
            }
        });
    }

    public boolean isSaveUiShowing() {
        if (this.mSaveUi == null) {
            return false;
        }
        return this.mSaveUi.isShowing();
    }

    public boolean isFillDialogShowing() {
        if (this.mFillDialog == null) {
            return false;
        }
        return this.mFillDialog.isShowing();
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("Autofill UI");
        pw.print("  ");
        pw.print("Night mode: ");
        pw.println(this.mUiModeMgr.isNightMode());
        if (this.mFillUi != null) {
            pw.print("  ");
            pw.println("showsFillUi: true");
            this.mFillUi.dump(pw, "    ");
        } else {
            pw.print("  ");
            pw.println("showsFillUi: false");
        }
        if (this.mSaveUi != null) {
            pw.print("  ");
            pw.println("showsSaveUi: true");
            this.mSaveUi.dump(pw, "    ");
        } else {
            pw.print("  ");
            pw.println("showsSaveUi: false");
        }
        if (this.mFillDialog != null) {
            pw.print("  ");
            pw.println("showsFillDialog: true");
            this.mFillDialog.dump(pw, "    ");
        } else {
            pw.print("  ");
            pw.println("showsFillDialog: false");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideFillUiUiThread(com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback, boolean notifyClient) {
        if (this.mFillUi != null) {
            if (callback == null || callback == this.mCallback) {
                this.mFillUi.destroy(notifyClient);
                this.mFillUi = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.autofill.ui.PendingUi hideSaveUiUiThread(com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "hideSaveUiUiThread(): mSaveUi=" + this.mSaveUi + ", callback=" + callback + ", mCallback=" + this.mCallback);
        }
        if (this.mSaveUi != null && this.mSaveUiCallback == callback) {
            return this.mSaveUi.hide();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: hideFillDialogUiThread, reason: merged with bridge method [inline-methods] */
    public void lambda$hideFillDialog$4(com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        if (this.mFillDialog != null) {
            if (callback == null || callback == this.mCallback) {
                this.mFillDialog.destroy();
                this.mFillDialog = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroySaveUiUiThread(com.android.server.autofill.ui.PendingUi pendingSaveUi, boolean notifyClient) {
        if (this.mSaveUi == null) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "destroySaveUiUiThread(): already destroyed");
                return;
            }
            return;
        }
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "destroySaveUiUiThread(): " + pendingSaveUi);
        }
        this.mSaveUi.destroy();
        this.mSaveUi = null;
        this.mSaveUiCallback = null;
        if (pendingSaveUi != null && notifyClient) {
            try {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(TAG, "destroySaveUiUiThread(): notifying client");
                }
                pendingSaveUi.client.setSaveUiState(pendingSaveUi.sessionId, false);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Error notifying client to set save UI state to hidden: " + e);
            }
        }
        if (this.mCreateFillUiRunnable != null) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "start the pending fill UI request..");
            }
            this.mHandler.post(this.mCreateFillUiRunnable);
            this.mCreateFillUiRunnable = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: destroyAllUiThread, reason: merged with bridge method [inline-methods] */
    public void lambda$destroyAll$11(com.android.server.autofill.ui.PendingUi pendingSaveUi, com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback, boolean notifyClient) {
        hideFillUiUiThread(callback, notifyClient);
        lambda$hideFillDialog$4(callback);
        destroySaveUiUiThread(pendingSaveUi, notifyClient);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: hideAllUiThread, reason: merged with bridge method [inline-methods] */
    public void lambda$hideAll$10(com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback callback) {
        hideFillUiUiThread(callback, true);
        lambda$hideFillDialog$4(callback);
        com.android.server.autofill.ui.PendingUi pendingSaveUi = hideSaveUiUiThread(callback);
        if (pendingSaveUi != null && pendingSaveUi.getState() == 4) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "hideAllUiThread(): destroying Save UI because pending restoration is finished");
            }
            destroySaveUiUiThread(pendingSaveUi, true);
        }
    }
}
