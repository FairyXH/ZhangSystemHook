package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public class GetRequestSession extends com.android.server.credentials.RequestSession<android.credentials.GetCredentialRequest, android.credentials.IGetCredentialCallback, android.credentials.GetCredentialResponse> implements com.android.server.credentials.ProviderSession.ProviderInternalCallback<android.credentials.GetCredentialResponse> {
    private static final java.lang.String TAG = "CredentialManager";

    @Override // com.android.server.credentials.RequestSession
    public /* bridge */ /* synthetic */ void addProviderSession(android.content.ComponentName componentName, com.android.server.credentials.ProviderSession providerSession) {
        super.addProviderSession(componentName, providerSession);
    }

    @Override // com.android.server.credentials.RequestSession, com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public /* bridge */ /* synthetic */ void onUiSelection(android.credentials.selection.UserSelectionDialogResult userSelectionDialogResult) {
        super.onUiSelection(userSelectionDialogResult);
    }

    public GetRequestSession(android.content.Context context, com.android.server.credentials.RequestSession.SessionLifetime sessionCallback, java.lang.Object lock, int userId, int callingUid, android.credentials.IGetCredentialCallback callback, android.credentials.GetCredentialRequest request, android.service.credentials.CallingAppInfo callingAppInfo, java.util.Set<android.content.ComponentName> enabledProviders, android.os.CancellationSignal cancellationSignal, long startedTimestamp) {
        super(context, sessionCallback, lock, userId, callingUid, request, callback, getRequestInfoFromRequest(request), callingAppInfo, enabledProviders, cancellationSignal, startedTimestamp, true);
        this.mRequestSessionMetric.collectGetFlowInitialMetricInfo(request);
    }

    private static java.lang.String getRequestInfoFromRequest(android.credentials.GetCredentialRequest request) {
        for (android.credentials.CredentialOption option : request.getCredentialOptions()) {
            if (option.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS") != null) {
                return "android.credentials.selection.TYPE_GET_VIA_REGISTRY";
            }
        }
        return "android.credentials.selection.TYPE_GET";
    }

    @Override // com.android.server.credentials.RequestSession
    public com.android.server.credentials.ProviderSession initiateProviderSession(android.credentials.CredentialProviderInfo providerInfo, com.android.server.credentials.RemoteCredentialService remoteCredentialService) {
        com.android.server.credentials.ProviderGetSession providerGetSession = com.android.server.credentials.ProviderGetSession.createNewSession(this.mContext, this.mUserId, providerInfo, this, remoteCredentialService);
        if (providerGetSession != null) {
            android.util.Slog.i(TAG, "Provider session created and being added for: " + providerInfo.getComponentName());
            this.mProviders.put(providerGetSession.getComponentName().flattenToString(), providerGetSession);
        }
        return providerGetSession;
    }

    @Override // com.android.server.credentials.RequestSession
    protected void launchUiWithProviderData(final java.util.ArrayList<android.credentials.selection.ProviderData> providerDataList) {
        this.mRequestSessionMetric.collectUiCallStartTime(java.lang.System.nanoTime());
        this.mCredentialManagerUi.setStatus(com.android.server.credentials.CredentialManagerUi.UiStatus.USER_INTERACTION);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.credentials.GetRequestSession$$ExternalSyntheticLambda1
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$launchUiWithProviderData$0(providerDataList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$launchUiWithProviderData$0(java.util.ArrayList providerDataList) throws java.lang.Exception {
        try {
            cancelExistingPendingIntent();
            this.mPendingIntent = this.mCredentialManagerUi.createPendingIntent(android.credentials.selection.RequestInfo.newGetRequestInfo(this.mRequestId, (android.credentials.GetCredentialRequest) this.mClientRequest, this.mClientAppInfo.getPackageName(), android.service.credentials.PermissionUtils.hasPermission(this.mContext, this.mClientAppInfo.getPackageName(), "android.permission.CREDENTIAL_MANAGER_SET_ALLOWED_PROVIDERS"), false), providerDataList, this.mRequestSessionMetric);
            ((android.credentials.IGetCredentialCallback) this.mClientCallback).onPendingIntent(this.mPendingIntent);
        } catch (android.os.RemoteException e) {
            this.mRequestSessionMetric.collectUiReturnedFinalPhase(false);
            this.mCredentialManagerUi.setStatus(com.android.server.credentials.CredentialManagerUi.UiStatus.TERMINATED);
            this.mRequestSessionMetric.collectFrameworkException("android.credentials.GetCredentialException.TYPE_UNKNOWN");
            respondToClientWithErrorAndFinish("android.credentials.GetCredentialException.TYPE_UNKNOWN", "Unable to instantiate selector");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.credentials.RequestSession
    public void invokeClientCallbackSuccess(android.credentials.GetCredentialResponse response) throws android.os.RemoteException {
        ((android.credentials.IGetCredentialCallback) this.mClientCallback).onResponse(response);
    }

    @Override // com.android.server.credentials.RequestSession
    protected void invokeClientCallbackError(java.lang.String errorType, java.lang.String errorMsg) throws android.os.RemoteException {
        ((android.credentials.IGetCredentialCallback) this.mClientCallback).onError(errorType, errorMsg);
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onFinalResponseReceived(android.content.ComponentName componentName, android.credentials.GetCredentialResponse response) {
        android.util.Slog.i(TAG, "onFinalResponseReceived from: " + componentName.flattenToString());
        this.mRequestSessionMetric.collectUiResponseData(true, java.lang.System.nanoTime());
        this.mRequestSessionMetric.updateMetricsOnResponseReceived(this.mProviders, componentName, isPrimaryProviderViaProviderInfo(componentName));
        if (response != null) {
            this.mRequestSessionMetric.collectChosenProviderStatus(com.android.server.credentials.metrics.ProviderStatusForMetrics.FINAL_SUCCESS.getMetricCode());
            respondToClientWithResponseAndFinish(response);
        } else {
            this.mRequestSessionMetric.collectChosenProviderStatus(com.android.server.credentials.metrics.ProviderStatusForMetrics.FINAL_FAILURE.getMetricCode());
            this.mRequestSessionMetric.collectFrameworkException("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL");
            respondToClientWithErrorAndFinish("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL", "Invalid response from provider");
        }
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onFinalErrorReceived(android.content.ComponentName componentName, java.lang.String errorType, java.lang.String message) {
        respondToClientWithErrorAndFinish(errorType, message);
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiCancellation(boolean isUserCancellation) {
        java.lang.String exception = "android.credentials.GetCredentialException.TYPE_USER_CANCELED";
        java.lang.String message = "User cancelled the selector";
        if (!isUserCancellation) {
            exception = "android.credentials.GetCredentialException.TYPE_INTERRUPTED";
            message = "The UI was interrupted - please try again.";
        }
        this.mRequestSessionMetric.collectFrameworkException(exception);
        respondToClientWithErrorAndFinish(exception, message);
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiSelectorInvocationFailure() {
        this.mRequestSessionMetric.collectFrameworkException("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL");
        respondToClientWithErrorAndFinish("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL", "No credentials available.");
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onProviderStatusChanged(com.android.server.credentials.ProviderSession.Status status, android.content.ComponentName componentName, com.android.server.credentials.ProviderSession.CredentialsSource source) {
        android.util.Slog.i(TAG, "Status changed for: " + componentName + ", with status: " + status + ", and source: " + source);
        if (status == com.android.server.credentials.ProviderSession.Status.NO_CREDENTIALS_FROM_AUTH_ENTRY) {
            handleEmptyAuthenticationSelection(componentName);
            return;
        }
        if (!isAnyProviderPending()) {
            if (isUiInvocationNeeded()) {
                android.util.Slog.i(TAG, "Provider status changed - ui invocation is needed");
                getProviderDataAndInitiateUi();
            } else {
                this.mRequestSessionMetric.collectFrameworkException("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL");
                respondToClientWithErrorAndFinish("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL", "No credentials available");
            }
        }
    }

    protected void handleEmptyAuthenticationSelection(final android.content.ComponentName componentName) {
        this.mProviders.keySet().forEach(new java.util.function.Consumer() { // from class: com.android.server.credentials.GetRequestSession$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$handleEmptyAuthenticationSelection$1(componentName, (java.lang.String) obj);
            }
        });
        getProviderDataAndInitiateUi();
        if (providerDataContainsEmptyAuthEntriesOnly()) {
            this.mRequestSessionMetric.collectFrameworkException("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL");
            respondToClientWithErrorAndFinish("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL", "No credentials available");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEmptyAuthenticationSelection$1(android.content.ComponentName componentName, java.lang.String key) {
        com.android.server.credentials.ProviderGetSession session = (com.android.server.credentials.ProviderGetSession) this.mProviders.get(key);
        if (!session.mComponentName.equals(componentName)) {
            session.updateAuthEntriesStatusFromAnotherSession();
        }
    }

    private boolean providerDataContainsEmptyAuthEntriesOnly() {
        for (java.lang.String key : this.mProviders.keySet()) {
            com.android.server.credentials.ProviderGetSession session = (com.android.server.credentials.ProviderGetSession) this.mProviders.get(key);
            if (!session.containsEmptyAuthEntriesOnly()) {
                return false;
            }
        }
        return true;
    }
}
