package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public final class ClearRequestSession extends com.android.server.credentials.RequestSession<android.credentials.ClearCredentialStateRequest, android.credentials.IClearCredentialStateCallback, java.lang.Void> implements com.android.server.credentials.ProviderSession.ProviderInternalCallback<java.lang.Void> {
    private static final java.lang.String TAG = "CredentialManager";

    @Override // com.android.server.credentials.RequestSession
    public /* bridge */ /* synthetic */ void addProviderSession(android.content.ComponentName componentName, com.android.server.credentials.ProviderSession providerSession) {
        super.addProviderSession(componentName, providerSession);
    }

    @Override // com.android.server.credentials.RequestSession, com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public /* bridge */ /* synthetic */ void onUiSelection(android.credentials.selection.UserSelectionDialogResult userSelectionDialogResult) {
        super.onUiSelection(userSelectionDialogResult);
    }

    public ClearRequestSession(android.content.Context context, com.android.server.credentials.RequestSession.SessionLifetime sessionCallback, java.lang.Object lock, int userId, int callingUid, android.credentials.IClearCredentialStateCallback callback, android.credentials.ClearCredentialStateRequest request, android.service.credentials.CallingAppInfo callingAppInfo, java.util.Set<android.content.ComponentName> enabledProviders, android.os.CancellationSignal cancellationSignal, long startedTimestamp) {
        super(context, sessionCallback, lock, userId, callingUid, request, callback, "android.credentials.selection.TYPE_UNDEFINED", callingAppInfo, enabledProviders, cancellationSignal, startedTimestamp, true);
    }

    @Override // com.android.server.credentials.RequestSession
    public com.android.server.credentials.ProviderSession initiateProviderSession(android.credentials.CredentialProviderInfo providerInfo, com.android.server.credentials.RemoteCredentialService remoteCredentialService) {
        com.android.server.credentials.ProviderClearSession providerClearSession = com.android.server.credentials.ProviderClearSession.createNewSession(this.mContext, this.mUserId, providerInfo, this, remoteCredentialService);
        if (providerClearSession != null) {
            android.util.Slog.i(TAG, "Provider session created and being added for: " + providerInfo.getComponentName());
            this.mProviders.put(providerClearSession.getComponentName().flattenToString(), providerClearSession);
        }
        return providerClearSession;
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onProviderStatusChanged(com.android.server.credentials.ProviderSession.Status status, android.content.ComponentName componentName, com.android.server.credentials.ProviderSession.CredentialsSource source) {
        android.util.Slog.i(TAG, "Provider changed with status: " + status + ", and source: " + source);
        if (com.android.server.credentials.ProviderSession.isTerminatingStatus(status)) {
            android.util.Slog.i(TAG, "Provider terminating status");
            onProviderTerminated(componentName);
        } else if (com.android.server.credentials.ProviderSession.isCompletionStatus(status)) {
            android.util.Slog.i(TAG, "Provider has completion status");
            onProviderResponseComplete(componentName);
        }
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onFinalResponseReceived(android.content.ComponentName componentName, java.lang.Void response) {
        this.mRequestSessionMetric.collectUiResponseData(true, java.lang.System.nanoTime());
        this.mRequestSessionMetric.updateMetricsOnResponseReceived(this.mProviders, componentName, isPrimaryProviderViaProviderInfo(componentName));
        respondToClientWithResponseAndFinish(null);
    }

    protected void onProviderResponseComplete(android.content.ComponentName componentName) {
        if (!isAnyProviderPending()) {
            onFinalResponseReceived(componentName, (java.lang.Void) null);
        }
    }

    protected void onProviderTerminated(android.content.ComponentName componentName) {
        if (!isAnyProviderPending()) {
            processResponses();
        }
    }

    @Override // com.android.server.credentials.RequestSession
    protected void launchUiWithProviderData(java.util.ArrayList<android.credentials.selection.ProviderData> providerDataList) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.credentials.RequestSession
    public void invokeClientCallbackSuccess(java.lang.Void response) throws android.os.RemoteException {
        ((android.credentials.IClearCredentialStateCallback) this.mClientCallback).onSuccess();
    }

    @Override // com.android.server.credentials.RequestSession
    protected void invokeClientCallbackError(java.lang.String errorType, java.lang.String errorMsg) throws android.os.RemoteException {
        ((android.credentials.IClearCredentialStateCallback) this.mClientCallback).onError(errorType, errorMsg);
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onFinalErrorReceived(android.content.ComponentName componentName, java.lang.String errorType, java.lang.String message) {
    }

    private void processResponses() {
        for (com.android.server.credentials.ProviderSession session : this.mProviders.values()) {
            if (session.isProviderResponseSet().booleanValue()) {
                respondToClientWithResponseAndFinish(null);
                return;
            }
        }
        this.mRequestSessionMetric.collectFrameworkException("android.credentials.ClearCredentialStateException.TYPE_UNKNOWN");
        respondToClientWithErrorAndFinish("android.credentials.ClearCredentialStateException.TYPE_UNKNOWN", "All providers failed");
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiCancellation(boolean isUserCancellation) {
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiSelectorInvocationFailure() {
    }
}
