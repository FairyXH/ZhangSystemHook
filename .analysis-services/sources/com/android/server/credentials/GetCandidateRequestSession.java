package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public class GetCandidateRequestSession extends com.android.server.credentials.RequestSession<android.credentials.GetCredentialRequest, android.credentials.IGetCandidateCredentialsCallback, android.credentials.GetCandidateCredentialsResponse> implements com.android.server.credentials.ProviderSession.ProviderInternalCallback<android.credentials.GetCredentialResponse> {
    private static final java.lang.String REQUEST_ID_KEY = "autofill_request_id";
    private static final java.lang.String SESSION_ID_KEY = "autofill_session_id";
    private static final java.lang.String TAG = "CredentialManager";
    private final android.os.ResultReceiver mAutofillCallback;
    private final int mAutofillRequestId;
    private final int mAutofillSessionId;
    private final android.os.IBinder mClientBinder;
    private android.content.ComponentName mPrimaryProviderComponentName;

    @Override // com.android.server.credentials.RequestSession
    public /* bridge */ /* synthetic */ void addProviderSession(android.content.ComponentName componentName, com.android.server.credentials.ProviderSession providerSession) {
        super.addProviderSession(componentName, providerSession);
    }

    @Override // com.android.server.credentials.RequestSession, com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public /* bridge */ /* synthetic */ void onUiSelection(android.credentials.selection.UserSelectionDialogResult userSelectionDialogResult) {
        super.onUiSelection(userSelectionDialogResult);
    }

    public GetCandidateRequestSession(android.content.Context context, com.android.server.credentials.RequestSession.SessionLifetime sessionCallback, java.lang.Object lock, int userId, int callingUid, android.credentials.IGetCandidateCredentialsCallback callback, android.credentials.GetCredentialRequest request, android.service.credentials.CallingAppInfo callingAppInfo, java.util.Set<android.content.ComponentName> enabledProviders, android.os.CancellationSignal cancellationSignal, android.os.IBinder clientBinder) {
        super(context, sessionCallback, lock, userId, callingUid, request, callback, "android.credentials.selection.TYPE_GET", callingAppInfo, enabledProviders, cancellationSignal, 0L, false);
        this.mPrimaryProviderComponentName = null;
        this.mClientBinder = clientBinder;
        this.mAutofillSessionId = request.getData().getInt(SESSION_ID_KEY, -1);
        this.mAutofillRequestId = request.getData().getInt(REQUEST_ID_KEY, -1);
        this.mAutofillCallback = (android.os.ResultReceiver) request.getData().getParcelable("android.credentials.AUTOFILL_RESULT_RECEIVER", android.os.ResultReceiver.class);
        if (this.mClientBinder != null) {
            setUpClientCallbackListener(this.mClientBinder);
        }
    }

    @Override // com.android.server.credentials.RequestSession
    public com.android.server.credentials.ProviderSession initiateProviderSession(android.credentials.CredentialProviderInfo providerInfo, com.android.server.credentials.RemoteCredentialService remoteCredentialService) {
        com.android.server.credentials.ProviderGetSession providerGetCandidateSessions = com.android.server.credentials.ProviderGetSession.createNewSession(this.mContext, this.mUserId, providerInfo, this, remoteCredentialService);
        if (providerGetCandidateSessions != null) {
            android.util.Slog.d(TAG, "In startProviderSession - provider session created and being added for: " + providerInfo.getComponentName());
            android.content.ComponentName componentName = providerGetCandidateSessions.getComponentName();
            if (providerInfo.isPrimary()) {
                this.mPrimaryProviderComponentName = componentName;
            }
            this.mProviders.put(componentName.flattenToString(), providerGetCandidateSessions);
        }
        return providerGetCandidateSessions;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.credentials.RequestSession
    protected void launchUiWithProviderData(java.util.ArrayList<android.credentials.selection.ProviderData> providerDataList) {
        if (providerDataList == null || providerDataList.isEmpty()) {
            respondToClientWithErrorAndFinish("android.credentials.GetCandidateCredentialsException.TYPE_NO_CREDENTIAL", "No credentials found");
            return;
        }
        android.content.Intent intent = this.mCredentialManagerUi.createIntentForAutofill(android.credentials.selection.RequestInfo.newGetRequestInfo(this.mRequestId, (android.credentials.GetCredentialRequest) this.mClientRequest, this.mClientAppInfo.getPackageName(), android.service.credentials.PermissionUtils.hasPermission(this.mContext, this.mClientAppInfo.getPackageName(), "android.permission.CREDENTIAL_MANAGER_SET_ALLOWED_PROVIDERS"), true), this.mRequestSessionMetric);
        java.util.List<android.credentials.selection.GetCredentialProviderData> candidateProviderDataList = new java.util.ArrayList<>();
        java.util.Iterator<android.credentials.selection.ProviderData> it = providerDataList.iterator();
        while (it.hasNext()) {
            candidateProviderDataList.add((android.credentials.selection.ProviderData) it.next());
        }
        try {
            invokeClientCallbackSuccess(new android.credentials.GetCandidateCredentialsResponse(candidateProviderDataList, intent, this.mPrimaryProviderComponentName));
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Issue while responding to client with error : " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.credentials.RequestSession
    public void invokeClientCallbackSuccess(android.credentials.GetCandidateCredentialsResponse response) throws android.os.RemoteException {
        ((android.credentials.IGetCandidateCredentialsCallback) this.mClientCallback).onResponse(response);
    }

    @Override // com.android.server.credentials.RequestSession
    protected void invokeClientCallbackError(java.lang.String errorType, java.lang.String errorMsg) throws android.os.RemoteException {
        ((android.credentials.IGetCandidateCredentialsCallback) this.mClientCallback).onError(errorType, errorMsg);
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onFinalErrorReceived(android.content.ComponentName componentName, java.lang.String errorType, java.lang.String message) {
        android.util.Slog.d(TAG, "onFinalErrorReceived");
        if ("android.credentials.GetCredentialException.TYPE_USER_CANCELED".equals(errorType)) {
            android.util.Slog.d(TAG, "User canceled but session is not being terminated");
        } else {
            respondToFinalReceiverWithFailureAndFinish(errorType, message);
        }
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiCancellation(boolean isUserCancellation) {
        android.util.Slog.d(TAG, "User canceled but session is not being terminated");
    }

    private void respondToFinalReceiverWithFailureAndFinish(java.lang.String exception, java.lang.String message) {
        if (this.mRequestSessionStatus == com.android.server.credentials.RequestSession.RequestSessionStatus.COMPLETE) {
            android.util.Slog.w(TAG, "Request has already been completed. This is strange.");
            return;
        }
        if (this.mAutofillCallback != null) {
            android.os.Bundle resultData = new android.os.Bundle();
            resultData.putStringArray("android.service.credentials.extra.GET_CREDENTIAL_EXCEPTION", new java.lang.String[]{exception, message});
            this.mAutofillCallback.send(-1, resultData);
        } else {
            android.util.Slog.w(TAG, "onUiCancellation called but mAutofillCallback not found");
        }
        finishSession(false, com.android.server.credentials.metrics.ApiStatus.FAILURE.getMetricCode());
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiSelectorInvocationFailure() {
        this.mRequestSessionMetric.collectFrameworkException("android.credentials.GetCandidateCredentialsException.TYPE_NO_CREDENTIAL");
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onProviderStatusChanged(com.android.server.credentials.ProviderSession.Status status, android.content.ComponentName componentName, com.android.server.credentials.ProviderSession.CredentialsSource source) {
        android.util.Slog.d(TAG, "in onStatusChanged with status: " + status + ", and source: " + source);
        if (!isAnyProviderPending()) {
            if (isUiInvocationNeeded()) {
                android.util.Slog.d(TAG, "in onProviderStatusChanged - isUiInvocationNeeded");
                getProviderDataAndInitiateUi();
            } else {
                respondToClientWithErrorAndFinish("android.credentials.GetCandidateCredentialsException.TYPE_NO_CREDENTIAL", "No credentials available");
            }
        }
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onFinalResponseReceived(android.content.ComponentName componentName, android.credentials.GetCredentialResponse response) {
        android.util.Slog.d(TAG, "onFinalResponseReceived");
        if (this.mRequestSessionStatus == com.android.server.credentials.RequestSession.RequestSessionStatus.COMPLETE) {
            android.util.Slog.w(TAG, "Request has already been completed. This is strange.");
        } else {
            respondToFinalReceiverWithResponseAndFinish(response);
        }
    }

    private void respondToFinalReceiverWithResponseAndFinish(android.credentials.GetCredentialResponse response) {
        if (this.mRequestSessionStatus == com.android.server.credentials.RequestSession.RequestSessionStatus.COMPLETE) {
            android.util.Slog.w(TAG, "Request has already been completed. This is strange.");
            return;
        }
        if (this.mAutofillCallback != null) {
            android.util.Slog.d(TAG, "onFinalResponseReceived sending through final receiver");
            android.os.Bundle resultData = new android.os.Bundle();
            resultData.putParcelable("android.service.credentials.extra.GET_CREDENTIAL_RESPONSE", response);
            this.mAutofillCallback.send(0, resultData);
            finishSession(false, com.android.server.credentials.metrics.ApiStatus.SUCCESS.getMetricCode());
            return;
        }
        android.util.Slog.w(TAG, "onFinalResponseReceived result receiver not found for pinned entry");
        finishSession(false, com.android.server.credentials.metrics.ApiStatus.FAILURE.getMetricCode());
    }

    public int getAutofillSessionId() {
        return this.mAutofillSessionId;
    }

    public int getAutofillRequestId() {
        return this.mAutofillRequestId;
    }
}
