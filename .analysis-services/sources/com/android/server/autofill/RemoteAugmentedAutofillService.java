package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
final class RemoteAugmentedAutofillService extends com.android.internal.infra.ServiceConnector.Impl<android.service.autofill.augmented.IAugmentedAutofillService> {
    private static final java.lang.String TAG = com.android.server.autofill.RemoteAugmentedAutofillService.class.getSimpleName();
    private final com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks mCallbacks;
    private final android.content.ComponentName mComponentName;
    private final int mIdleUnbindTimeoutMs;
    private final int mRequestTimeoutMs;
    private final com.android.server.autofill.AutofillUriGrantsManager mUriGrantsManager;

    public interface RemoteAugmentedAutofillServiceCallbacks extends com.android.internal.infra.AbstractRemoteService.VultureCallback<com.android.server.autofill.RemoteAugmentedAutofillService> {
        void logAugmentedAutofillAuthenticationSelected(int i, java.lang.String str, android.os.Bundle bundle);

        void logAugmentedAutofillSelected(int i, java.lang.String str, android.os.Bundle bundle);

        void logAugmentedAutofillShown(int i, android.os.Bundle bundle);

        void resetLastResponse();

        void setLastResponse(int i);
    }

    RemoteAugmentedAutofillService(android.content.Context context, int serviceUid, android.content.ComponentName serviceName, int userId, com.android.server.autofill.RemoteAugmentedAutofillService.RemoteAugmentedAutofillServiceCallbacks callbacks, boolean bindInstantServiceAllowed, boolean verbose, int idleUnbindTimeoutMs, int requestTimeoutMs) {
        super(context, new android.content.Intent("android.service.autofill.augmented.AugmentedAutofillService").setComponent(serviceName), bindInstantServiceAllowed ? 4194304 : 0, userId, new java.util.function.Function() { // from class: com.android.server.autofill.RemoteAugmentedAutofillService$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.service.autofill.augmented.IAugmentedAutofillService.Stub.asInterface((android.os.IBinder) obj);
            }
        });
        this.mIdleUnbindTimeoutMs = idleUnbindTimeoutMs;
        this.mRequestTimeoutMs = requestTimeoutMs;
        this.mComponentName = serviceName;
        this.mCallbacks = callbacks;
        this.mUriGrantsManager = new com.android.server.autofill.AutofillUriGrantsManager(serviceUid);
        connect();
    }

    static android.util.Pair<android.content.pm.ServiceInfo, android.content.ComponentName> getComponentName(java.lang.String componentName, int userId, boolean isTemporary) {
        int flags = isTemporary ? 128 : 128 | 1048576;
        try {
            android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(componentName);
            android.content.pm.ServiceInfo serviceInfo = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, flags, userId);
            if (serviceInfo == null) {
                android.util.Slog.e(TAG, "Bad service name for flags " + flags + ": " + componentName);
                return null;
            }
            return new android.util.Pair<>(serviceInfo, serviceComponent);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Error getting service info for '" + componentName + "': " + e);
            return null;
        }
    }

    public android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    public com.android.server.autofill.AutofillUriGrantsManager getAutofillUriGrantsManager() {
        return this.mUriGrantsManager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceConnectionStatusChanged(android.service.autofill.augmented.IAugmentedAutofillService service, boolean connected) {
        try {
            if (connected) {
                service.onConnected(com.android.server.autofill.Helper.sDebug, com.android.server.autofill.Helper.sVerbose);
            } else {
                service.onDisconnected();
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Exception calling onServiceConnectionStatusChanged(" + connected + "): ", e);
        }
    }

    protected long getAutoDisconnectTimeoutMs() {
        return this.mIdleUnbindTimeoutMs;
    }

    public void onRequestAutofillLocked(final int sessionId, final android.view.autofill.IAutoFillManagerClient client, final int taskId, final android.content.ComponentName activityComponent, final android.os.IBinder activityToken, final android.view.autofill.AutofillId focusedId, final android.view.autofill.AutofillValue focusedValue, final android.view.inputmethod.InlineSuggestionsRequest inlineSuggestionsRequest, final java.util.function.Function<com.android.server.autofill.ui.InlineFillUi, java.lang.Boolean> inlineSuggestionsCallback, final java.lang.Runnable onErrorCallback, final com.android.server.autofill.RemoteInlineSuggestionRenderService remoteRenderService, final int userId) {
        final long requestTime = android.os.SystemClock.elapsedRealtime();
        final java.util.concurrent.atomic.AtomicReference<android.os.ICancellationSignal> cancellationRef = new java.util.concurrent.atomic.AtomicReference<>();
        postAsync(new com.android.internal.infra.ServiceConnector.Job() { // from class: com.android.server.autofill.RemoteAugmentedAutofillService$$ExternalSyntheticLambda3
            public final java.lang.Object run(java.lang.Object obj) {
                return this.f$0.lambda$onRequestAutofillLocked$0(client, sessionId, taskId, activityComponent, focusedId, focusedValue, requestTime, inlineSuggestionsRequest, inlineSuggestionsCallback, onErrorCallback, remoteRenderService, userId, activityToken, cancellationRef, (android.service.autofill.augmented.IAugmentedAutofillService) obj);
            }
        }).orTimeout(this.mRequestTimeoutMs, java.util.concurrent.TimeUnit.MILLISECONDS).whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.autofill.RemoteAugmentedAutofillService$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$onRequestAutofillLocked$1(cancellationRef, activityComponent, sessionId, (java.lang.Void) obj, (java.lang.Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.concurrent.CompletableFuture lambda$onRequestAutofillLocked$0(final android.view.autofill.IAutoFillManagerClient client, final int sessionId, final int taskId, final android.content.ComponentName activityComponent, final android.view.autofill.AutofillId focusedId, final android.view.autofill.AutofillValue focusedValue, final long requestTime, final android.view.inputmethod.InlineSuggestionsRequest inlineSuggestionsRequest, final java.util.function.Function inlineSuggestionsCallback, final java.lang.Runnable onErrorCallback, final com.android.server.autofill.RemoteInlineSuggestionRenderService remoteRenderService, final int userId, final android.os.IBinder activityToken, final java.util.concurrent.atomic.AtomicReference cancellationRef, final android.service.autofill.augmented.IAugmentedAutofillService service) throws java.lang.Exception {
        final com.android.internal.infra.AndroidFuture<java.lang.Void> requestAutofill = new com.android.internal.infra.AndroidFuture<>();
        client.getAugmentedAutofillClient(new com.android.internal.os.IResultReceiver.Stub() { // from class: com.android.server.autofill.RemoteAugmentedAutofillService.1
            public void send(int resultCode, android.os.Bundle resultData) throws android.os.RemoteException {
                android.os.IBinder realClient = resultData.getBinder("android.view.autofill.extra.AUGMENTED_AUTOFILL_CLIENT");
                service.onFillRequest(sessionId, realClient, taskId, activityComponent, focusedId, focusedValue, requestTime, inlineSuggestionsRequest, new android.service.autofill.augmented.IFillCallback.Stub() { // from class: com.android.server.autofill.RemoteAugmentedAutofillService.1.1
                    public void onSuccess(java.util.List<android.service.autofill.Dataset> inlineSuggestionsData, android.os.Bundle clientState, boolean showingFillWindow) {
                        com.android.server.autofill.RemoteAugmentedAutofillService.this.mCallbacks.resetLastResponse();
                        com.android.server.autofill.RemoteAugmentedAutofillService.this.maybeRequestShowInlineSuggestions(sessionId, inlineSuggestionsRequest, inlineSuggestionsData, clientState, focusedId, focusedValue, inlineSuggestionsCallback, client, onErrorCallback, remoteRenderService, userId, activityComponent, activityToken);
                        if (!showingFillWindow) {
                            requestAutofill.complete((java.lang.Object) null);
                        }
                    }

                    public boolean isCompleted() {
                        return requestAutofill.isDone() && !requestAutofill.isCancelled();
                    }

                    public void onCancellable(android.os.ICancellationSignal cancellation) {
                        if (requestAutofill.isCancelled()) {
                            com.android.server.autofill.RemoteAugmentedAutofillService.this.dispatchCancellation(cancellation);
                        } else {
                            cancellationRef.set(cancellation);
                        }
                    }

                    public void cancel() {
                        requestAutofill.cancel(true);
                    }
                });
            }
        });
        return requestAutofill;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestAutofillLocked$1(java.util.concurrent.atomic.AtomicReference cancellationRef, android.content.ComponentName activityComponent, int sessionId, java.lang.Void res, java.lang.Throwable err) {
        if (err instanceof java.util.concurrent.CancellationException) {
            dispatchCancellation((android.os.ICancellationSignal) cancellationRef.get());
            return;
        }
        if (err instanceof java.util.concurrent.TimeoutException) {
            android.util.Slog.w(TAG, "PendingAutofillRequest timed out (" + this.mRequestTimeoutMs + "ms) for " + this);
            dispatchCancellation((android.os.ICancellationSignal) cancellationRef.get());
            if (this.mComponentName != null) {
                android.service.autofill.augmented.Helper.logResponse(15, this.mComponentName.getPackageName(), activityComponent, sessionId, this.mRequestTimeoutMs);
                return;
            }
            return;
        }
        if (err != null) {
            android.util.Slog.e(TAG, "exception handling getAugmentedAutofillClient() for " + sessionId + ": ", err);
        }
    }

    void dispatchCancellation(final android.os.ICancellationSignal cancellation) {
        if (cancellation == null) {
            return;
        }
        android.os.Handler.getMain().post(new java.lang.Runnable() { // from class: com.android.server.autofill.RemoteAugmentedAutofillService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.autofill.RemoteAugmentedAutofillService.lambda$dispatchCancellation$2(cancellation);
            }
        });
    }

    static /* synthetic */ void lambda$dispatchCancellation$2(android.os.ICancellationSignal cancellation) {
        try {
            cancellation.cancel();
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error requesting a cancellation", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeRequestShowInlineSuggestions(final int sessionId, android.view.inputmethod.InlineSuggestionsRequest request, java.util.List<android.service.autofill.Dataset> inlineSuggestionsData, final android.os.Bundle clientState, final android.view.autofill.AutofillId focusedId, android.view.autofill.AutofillValue focusedValue, final java.util.function.Function<com.android.server.autofill.ui.InlineFillUi, java.lang.Boolean> inlineSuggestionsCallback, final android.view.autofill.IAutoFillManagerClient client, final java.lang.Runnable onErrorCallback, com.android.server.autofill.RemoteInlineSuggestionRenderService remoteRenderService, final int userId, final android.content.ComponentName targetActivity, final android.os.IBinder targetActivityToken) {
        if (inlineSuggestionsData == null || inlineSuggestionsData.isEmpty() || inlineSuggestionsCallback == null || request == null || remoteRenderService == null) {
            java.util.function.Function<com.android.server.autofill.ui.InlineFillUi, java.lang.Boolean> function = inlineSuggestionsCallback;
            if (function != null && request != null) {
                function.apply(com.android.server.autofill.ui.InlineFillUi.emptyUi(focusedId));
                return;
            }
            return;
        }
        this.mCallbacks.setLastResponse(sessionId);
        java.lang.String filterText = (focusedValue == null || !focusedValue.isText()) ? null : focusedValue.getTextValue().toString();
        com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo = new com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo(request, focusedId, filterText, remoteRenderService, userId, sessionId);
        com.android.server.autofill.ui.InlineFillUi inlineFillUi = com.android.server.autofill.ui.InlineFillUi.forAugmentedAutofill(inlineFillUiInfo, inlineSuggestionsData, new com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback() { // from class: com.android.server.autofill.RemoteAugmentedAutofillService.2
            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void autofill(android.service.autofill.Dataset dataset, int datasetIndex) {
                boolean hideHighlight = true;
                if (dataset.getAuthentication() != null) {
                    com.android.server.autofill.RemoteAugmentedAutofillService.this.mCallbacks.logAugmentedAutofillAuthenticationSelected(sessionId, dataset.getId(), clientState);
                    android.content.IntentSender action = dataset.getAuthentication();
                    int authenticationId = android.view.autofill.AutofillManager.makeAuthenticationId(1, datasetIndex);
                    android.content.Intent fillInIntent = new android.content.Intent();
                    fillInIntent.putExtra("android.view.autofill.extra.CLIENT_STATE", clientState);
                    try {
                        client.authenticate(sessionId, authenticationId, action, fillInIntent, false);
                        return;
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(com.android.server.autofill.RemoteAugmentedAutofillService.TAG, "Error starting auth flow");
                        inlineSuggestionsCallback.apply(com.android.server.autofill.ui.InlineFillUi.emptyUi(focusedId));
                        return;
                    }
                }
                com.android.server.autofill.RemoteAugmentedAutofillService.this.mCallbacks.logAugmentedAutofillSelected(sessionId, dataset.getId(), clientState);
                try {
                    java.util.ArrayList<android.view.autofill.AutofillId> fieldIds = dataset.getFieldIds();
                    android.content.ClipData content = dataset.getFieldContent();
                    if (content != null) {
                        com.android.server.autofill.RemoteAugmentedAutofillService.this.mUriGrantsManager.grantUriPermissions(targetActivity, targetActivityToken, userId, content);
                        android.view.autofill.AutofillId fieldId = fieldIds.get(0);
                        if (com.android.server.autofill.Helper.sDebug) {
                            android.util.Slog.d(com.android.server.autofill.RemoteAugmentedAutofillService.TAG, "Calling client autofillContent(): id=" + fieldId + ", content=" + content);
                        }
                        client.autofillContent(sessionId, fieldId, content);
                    } else {
                        int size = fieldIds.size();
                        if (size != 1 || !fieldIds.get(0).equals(focusedId)) {
                            hideHighlight = false;
                        }
                        if (com.android.server.autofill.Helper.sDebug) {
                            android.util.Slog.d(com.android.server.autofill.RemoteAugmentedAutofillService.TAG, "Calling client autofill(): ids=" + fieldIds + ", values=" + dataset.getFieldValues());
                        }
                        client.autofill(sessionId, fieldIds, dataset.getFieldValues(), hideHighlight);
                    }
                    inlineSuggestionsCallback.apply(com.android.server.autofill.ui.InlineFillUi.emptyUi(focusedId));
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.w(com.android.server.autofill.RemoteAugmentedAutofillService.TAG, "Encounter exception autofilling the values");
                }
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void authenticate(int requestId, int datasetIndex) {
                android.util.Slog.e(com.android.server.autofill.RemoteAugmentedAutofillService.TAG, "authenticate not implemented for augmented autofill");
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void startIntentSender(android.content.IntentSender intentSender) {
                try {
                    client.startIntentSender(intentSender, new android.content.Intent());
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.autofill.RemoteAugmentedAutofillService.TAG, "RemoteException starting intent sender");
                }
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void onError() {
                onErrorCallback.run();
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void onInflate() {
            }
        });
        if (inlineSuggestionsCallback.apply(inlineFillUi).booleanValue()) {
            this.mCallbacks.logAugmentedAutofillShown(sessionId, clientState);
        }
    }

    public java.lang.String toString() {
        return "RemoteAugmentedAutofillService[" + android.content.ComponentName.flattenToShortString(this.mComponentName) + "]";
    }

    public void onDestroyAutofillWindowsRequest() {
        run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.autofill.RemoteAugmentedAutofillService$$ExternalSyntheticLambda2
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.autofill.augmented.IAugmentedAutofillService) obj).onDestroyAllFillWindowsRequest();
            }
        });
    }
}
