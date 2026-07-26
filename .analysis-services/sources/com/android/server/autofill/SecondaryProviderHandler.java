package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
final class SecondaryProviderHandler implements com.android.server.autofill.RemoteFillService.FillServiceCallbacks {
    private static final java.lang.String TAG = "SecondaryProviderHandler";
    private final com.android.server.autofill.SecondaryProviderHandler.SecondaryProviderCallback mCallback;
    private int mLastFlag;
    private final com.android.server.autofill.RemoteFillService mRemoteFillService;

    interface SecondaryProviderCallback {
        void onSecondaryFillResponse(android.service.autofill.FillResponse fillResponse, int i);
    }

    SecondaryProviderHandler(android.content.Context context, int userId, boolean bindInstantServiceAllowed, com.android.server.autofill.SecondaryProviderHandler.SecondaryProviderCallback callback, android.content.ComponentName componentName, android.content.ComponentName credentialAutofillService) {
        this.mRemoteFillService = new com.android.server.autofill.RemoteFillService(context, componentName, userId, this, bindInstantServiceAllowed, credentialAutofillService);
        this.mCallback = callback;
        android.util.Slog.v(TAG, "Creating a secondary provider handler with component name, " + componentName);
    }

    public void onServiceDied(com.android.server.autofill.RemoteFillService service) {
        this.mRemoteFillService.destroy();
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onFillRequestSuccess(int requestId, android.service.autofill.FillResponse response, java.lang.String servicePackageName, int requestFlags) {
        android.util.Slog.v(TAG, "Received a fill response: " + response);
        this.mCallback.onSecondaryFillResponse(response, this.mLastFlag);
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onFillRequestFailure(int requestId, java.lang.Throwable t) {
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onSaveRequestSuccess(java.lang.String servicePackageName, android.content.IntentSender intentSender) {
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onSaveRequestFailure(java.lang.CharSequence message, java.lang.String servicePackageName) {
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onConvertCredentialRequestSuccess(android.service.autofill.ConvertCredentialResponse convertCredentialResponse) {
    }

    public void onFillRequest(android.service.autofill.FillRequest pendingFillRequest, int flag, android.os.IBinder client) {
        android.util.Slog.v(TAG, "Requesting fill response to secondary provider.");
        this.mLastFlag = flag;
        if (this.mRemoteFillService != null && this.mRemoteFillService.isCredentialAutofillService()) {
            android.util.Slog.v(TAG, "About to call CredAutofill service as secondary provider");
            this.mRemoteFillService.onFillCredentialRequest(pendingFillRequest, client);
        } else {
            this.mRemoteFillService.onFillRequest(pendingFillRequest);
        }
    }

    public void destroy() {
        this.mRemoteFillService.destroy();
    }
}
