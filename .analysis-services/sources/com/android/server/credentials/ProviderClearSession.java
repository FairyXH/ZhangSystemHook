package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public final class ProviderClearSession extends com.android.server.credentials.ProviderSession<android.service.credentials.ClearCredentialStateRequest, java.lang.Void> implements com.android.server.credentials.RemoteCredentialService.ProviderCallbacks<java.lang.Void> {
    private static final java.lang.String TAG = "CredentialManager";
    private android.credentials.ClearCredentialStateException mProviderException;

    /* JADX WARN: Multi-variable type inference failed */
    public static com.android.server.credentials.ProviderClearSession createNewSession(android.content.Context context, int userId, android.credentials.CredentialProviderInfo providerInfo, com.android.server.credentials.ClearRequestSession clearRequestSession, com.android.server.credentials.RemoteCredentialService remoteCredentialService) {
        android.service.credentials.ClearCredentialStateRequest providerRequest = createProviderRequest((android.credentials.ClearCredentialStateRequest) clearRequestSession.mClientRequest, clearRequestSession.mClientAppInfo);
        return new com.android.server.credentials.ProviderClearSession(context, providerInfo, clearRequestSession, userId, remoteCredentialService, providerRequest);
    }

    private static android.service.credentials.ClearCredentialStateRequest createProviderRequest(android.credentials.ClearCredentialStateRequest clientRequest, android.service.credentials.CallingAppInfo callingAppInfo) {
        return new android.service.credentials.ClearCredentialStateRequest(callingAppInfo, clientRequest.getData());
    }

    public ProviderClearSession(android.content.Context context, android.credentials.CredentialProviderInfo info, com.android.server.credentials.ProviderSession.ProviderInternalCallback callbacks, int userId, com.android.server.credentials.RemoteCredentialService remoteCredentialService, android.service.credentials.ClearCredentialStateRequest providerRequest) {
        super(context, providerRequest, callbacks, info.getComponentName(), userId, remoteCredentialService);
        setStatus(com.android.server.credentials.ProviderSession.Status.PENDING);
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderResponseSuccess(java.lang.Void response) {
        android.util.Slog.i(TAG, "Remote provider responded with a valid response: " + this.mComponentName);
        this.mProviderResponseSet = true;
        updateStatusAndInvokeCallback(com.android.server.credentials.ProviderSession.Status.COMPLETE, com.android.server.credentials.ProviderSession.CredentialsSource.REMOTE_PROVIDER);
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderResponseFailure(int errorCode, java.lang.Exception exception) {
        if (exception instanceof android.credentials.ClearCredentialStateException) {
            this.mProviderException = (android.credentials.ClearCredentialStateException) exception;
            this.mProviderSessionMetric.collectCandidateFrameworkException(this.mProviderException.getType());
        }
        this.mProviderSessionMetric.collectCandidateExceptionStatus(true);
        updateStatusAndInvokeCallback(com.android.server.credentials.ProviderSession.Status.CANCELED, com.android.server.credentials.ProviderSession.CredentialsSource.REMOTE_PROVIDER);
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderServiceDied(com.android.server.credentials.RemoteCredentialService service) {
        if (service.getComponentName().equals(this.mComponentName)) {
            updateStatusAndInvokeCallback(com.android.server.credentials.ProviderSession.Status.SERVICE_DEAD, com.android.server.credentials.ProviderSession.CredentialsSource.REMOTE_PROVIDER);
        } else {
            android.util.Slog.w(TAG, "Component names different in onProviderServiceDied - this should not happen");
        }
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderCancellable(android.os.ICancellationSignal cancellation) {
        this.mProviderCancellationSignal = cancellation;
    }

    @Override // com.android.server.credentials.ProviderSession
    /* JADX INFO: renamed from: prepareUiData */
    protected android.credentials.selection.ProviderData mo3091prepareUiData() {
        return null;
    }

    @Override // com.android.server.credentials.ProviderSession
    protected void onUiEntrySelected(java.lang.String entryType, java.lang.String entryId, android.credentials.selection.ProviderPendingIntentResponse providerPendingIntentResponse) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.credentials.ProviderSession
    protected void invokeSession() {
        if (this.mRemoteCredentialService != null) {
            startCandidateMetrics();
            this.mRemoteCredentialService.setCallback(this);
            this.mRemoteCredentialService.onClearCredentialState((android.service.credentials.ClearCredentialStateRequest) this.mProviderRequest);
        }
    }
}
