package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public final class CreateRequestSession extends com.android.server.credentials.RequestSession<android.credentials.CreateCredentialRequest, android.credentials.ICreateCredentialCallback, android.credentials.CreateCredentialResponse> implements com.android.server.credentials.ProviderSession.ProviderInternalCallback<android.credentials.CreateCredentialResponse> {
    private static final java.lang.String TAG = "CredentialManager";
    private final java.util.Set<android.content.ComponentName> mPrimaryProviders;

    @Override // com.android.server.credentials.RequestSession
    public /* bridge */ /* synthetic */ void addProviderSession(android.content.ComponentName componentName, com.android.server.credentials.ProviderSession providerSession) {
        super.addProviderSession(componentName, providerSession);
    }

    @Override // com.android.server.credentials.RequestSession, com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public /* bridge */ /* synthetic */ void onUiSelection(android.credentials.selection.UserSelectionDialogResult userSelectionDialogResult) {
        super.onUiSelection(userSelectionDialogResult);
    }

    CreateRequestSession(android.content.Context context, com.android.server.credentials.RequestSession.SessionLifetime sessionCallback, java.lang.Object lock, int userId, int callingUid, android.credentials.CreateCredentialRequest request, android.credentials.ICreateCredentialCallback callback, android.service.credentials.CallingAppInfo callingAppInfo, java.util.Set<android.content.ComponentName> enabledProviders, java.util.Set<android.content.ComponentName> primaryProviders, android.os.CancellationSignal cancellationSignal, long startedTimestamp) {
        super(context, sessionCallback, lock, userId, callingUid, request, callback, "android.credentials.selection.TYPE_CREATE", callingAppInfo, enabledProviders, cancellationSignal, startedTimestamp, true);
        this.mRequestSessionMetric.collectCreateFlowInitialMetricInfo(request.getOrigin() != null, request);
        this.mPrimaryProviders = primaryProviders;
    }

    @Override // com.android.server.credentials.RequestSession
    public com.android.server.credentials.ProviderSession initiateProviderSession(android.credentials.CredentialProviderInfo providerInfo, com.android.server.credentials.RemoteCredentialService remoteCredentialService) {
        com.android.server.credentials.ProviderCreateSession providerCreateSession = com.android.server.credentials.ProviderCreateSession.createNewSession(this.mContext, this.mUserId, providerInfo, this, remoteCredentialService);
        if (providerCreateSession != null) {
            android.util.Slog.i(TAG, "Provider session created and being added for: " + providerInfo.getComponentName());
            this.mProviders.put(providerCreateSession.getComponentName().flattenToString(), providerCreateSession);
        }
        return providerCreateSession;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.credentials.RequestSession
    protected void launchUiWithProviderData(java.util.ArrayList<android.credentials.selection.ProviderData> providerDataList) {
        this.mRequestSessionMetric.collectUiCallStartTime(java.lang.System.nanoTime());
        this.mCredentialManagerUi.setStatus(com.android.server.credentials.CredentialManagerUi.UiStatus.USER_INTERACTION);
        cancelExistingPendingIntent();
        try {
            java.util.List<java.lang.String> flattenedPrimaryProviders = new java.util.ArrayList<>();
            for (android.content.ComponentName cn : this.mPrimaryProviders) {
                flattenedPrimaryProviders.add(cn.flattenToString());
            }
            this.mPendingIntent = this.mCredentialManagerUi.createPendingIntent(android.credentials.selection.RequestInfo.newCreateRequestInfo(this.mRequestId, (android.credentials.CreateCredentialRequest) this.mClientRequest, this.mClientAppInfo.getPackageName(), android.service.credentials.PermissionUtils.hasPermission(this.mContext, this.mClientAppInfo.getPackageName(), "android.permission.CREDENTIAL_MANAGER_SET_ALLOWED_PROVIDERS"), flattenedPrimaryProviders, false), providerDataList, this.mRequestSessionMetric);
            ((android.credentials.ICreateCredentialCallback) this.mClientCallback).onPendingIntent(this.mPendingIntent);
        } catch (android.os.RemoteException e) {
            this.mRequestSessionMetric.collectUiReturnedFinalPhase(false);
            this.mCredentialManagerUi.setStatus(com.android.server.credentials.CredentialManagerUi.UiStatus.TERMINATED);
            respondToClientWithErrorAndFinish("android.credentials.CreateCredentialException.TYPE_UNKNOWN", "Unable to invoke selector");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.credentials.RequestSession
    public void invokeClientCallbackSuccess(android.credentials.CreateCredentialResponse response) throws android.os.RemoteException {
        ((android.credentials.ICreateCredentialCallback) this.mClientCallback).onResponse(response);
    }

    @Override // com.android.server.credentials.RequestSession
    protected void invokeClientCallbackError(java.lang.String errorType, java.lang.String errorMsg) throws android.os.RemoteException {
        ((android.credentials.ICreateCredentialCallback) this.mClientCallback).onError(errorType, errorMsg);
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onFinalResponseReceived(android.content.ComponentName componentName, android.credentials.CreateCredentialResponse response) {
        android.util.Slog.i(TAG, "Final credential received from: " + componentName.flattenToString());
        this.mRequestSessionMetric.collectUiResponseData(true, java.lang.System.nanoTime());
        this.mRequestSessionMetric.updateMetricsOnResponseReceived(this.mProviders, componentName, isPrimaryProviderViaProviderInfo(componentName));
        if (response != null) {
            this.mRequestSessionMetric.collectChosenProviderStatus(com.android.server.credentials.metrics.ProviderStatusForMetrics.FINAL_SUCCESS.getMetricCode());
            respondToClientWithResponseAndFinish(response);
        } else {
            this.mRequestSessionMetric.collectChosenProviderStatus(com.android.server.credentials.metrics.ProviderStatusForMetrics.FINAL_FAILURE.getMetricCode());
            this.mRequestSessionMetric.collectFrameworkException("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS");
            respondToClientWithErrorAndFinish("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS", "Invalid response");
        }
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onFinalErrorReceived(android.content.ComponentName componentName, java.lang.String errorType, java.lang.String message) {
        respondToClientWithErrorAndFinish(errorType, message);
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiCancellation(boolean isUserCancellation) {
        java.lang.String exception = "android.credentials.CreateCredentialException.TYPE_USER_CANCELED";
        java.lang.String message = "User cancelled the selector";
        if (!isUserCancellation) {
            exception = "android.credentials.CreateCredentialException.TYPE_INTERRUPTED";
            message = "The UI was interrupted - please try again.";
        }
        this.mRequestSessionMetric.collectFrameworkException(exception);
        respondToClientWithErrorAndFinish(exception, message);
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiSelectorInvocationFailure() {
        this.mRequestSessionMetric.collectFrameworkException("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS");
        respondToClientWithErrorAndFinish("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS", "No create options available.");
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onProviderStatusChanged(com.android.server.credentials.ProviderSession.Status status, android.content.ComponentName componentName, com.android.server.credentials.ProviderSession.CredentialsSource source) {
        android.util.Slog.i(TAG, "Provider status changed: " + status + ", and source: " + source);
        if (!isAnyProviderPending()) {
            if (isUiInvocationNeeded()) {
                android.util.Slog.i(TAG, "Provider status changed - ui invocation is needed");
                getProviderDataAndInitiateUi();
            } else {
                this.mRequestSessionMetric.collectFrameworkException("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS");
                respondToClientWithErrorAndFinish("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS", "No create options available.");
            }
        }
    }
}
