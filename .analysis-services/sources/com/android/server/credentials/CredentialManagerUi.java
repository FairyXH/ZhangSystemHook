package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public class CredentialManagerUi {
    private final com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback mCallbacks;
    private final android.content.Context mContext;
    private final java.util.Set<android.content.ComponentName> mEnabledProviders;
    private final android.os.ResultReceiver mResultReceiver = new android.os.ResultReceiver(new android.os.Handler(android.os.Looper.getMainLooper())) { // from class: com.android.server.credentials.CredentialManagerUi.1
        @Override // android.os.ResultReceiver
        protected void onReceiveResult(int resultCode, android.os.Bundle resultData) {
            com.android.server.credentials.CredentialManagerUi.this.handleUiResult(resultCode, resultData);
        }
    };
    private com.android.server.credentials.CredentialManagerUi.UiStatus mStatus = com.android.server.credentials.CredentialManagerUi.UiStatus.IN_PROGRESS;
    private final int mUserId;

    public interface CredentialManagerUiCallback {
        void onUiCancellation(boolean z);

        void onUiSelection(android.credentials.selection.UserSelectionDialogResult userSelectionDialogResult);

        void onUiSelectorInvocationFailure();
    }

    enum UiStatus {
        IN_PROGRESS,
        USER_INTERACTION,
        NOT_STARTED,
        TERMINATED
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUiResult(int resultCode, android.os.Bundle resultData) {
        switch (resultCode) {
            case 0:
                this.mStatus = com.android.server.credentials.CredentialManagerUi.UiStatus.TERMINATED;
                this.mCallbacks.onUiCancellation(true);
                break;
            case 1:
                this.mStatus = com.android.server.credentials.CredentialManagerUi.UiStatus.TERMINATED;
                this.mCallbacks.onUiCancellation(false);
                break;
            case 2:
                this.mStatus = com.android.server.credentials.CredentialManagerUi.UiStatus.IN_PROGRESS;
                android.credentials.selection.UserSelectionDialogResult selection = android.credentials.selection.UserSelectionDialogResult.fromResultData(resultData);
                if (selection != null) {
                    this.mCallbacks.onUiSelection(selection);
                }
                break;
            case 3:
                this.mStatus = com.android.server.credentials.CredentialManagerUi.UiStatus.TERMINATED;
                this.mCallbacks.onUiSelectorInvocationFailure();
                break;
            default:
                this.mStatus = com.android.server.credentials.CredentialManagerUi.UiStatus.IN_PROGRESS;
                this.mCallbacks.onUiSelectorInvocationFailure();
                break;
        }
    }

    public android.content.Intent createCancelIntent(android.os.IBinder requestId, java.lang.String packageName) {
        return android.credentials.selection.IntentFactory.createCancelUiIntent(this.mContext, requestId, true, packageName);
    }

    public CredentialManagerUi(android.content.Context context, int userId, com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback callbacks, java.util.Set<android.content.ComponentName> enabledProviders) {
        this.mContext = context;
        this.mUserId = userId;
        this.mCallbacks = callbacks;
        this.mEnabledProviders = enabledProviders;
    }

    public void setStatus(com.android.server.credentials.CredentialManagerUi.UiStatus status) {
        this.mStatus = status;
    }

    public com.android.server.credentials.CredentialManagerUi.UiStatus getStatus() {
        return this.mStatus;
    }

    public android.app.PendingIntent createPendingIntent(android.credentials.selection.RequestInfo requestInfo, java.util.ArrayList<android.credentials.selection.ProviderData> providerDataList, com.android.server.credentials.metrics.RequestSessionMetric requestSessionMetric) {
        java.util.List<android.credentials.CredentialProviderInfo> allProviders = android.service.credentials.CredentialProviderInfoFactory.getCredentialProviderServices(this.mContext, this.mUserId, 2, this.mEnabledProviders, new java.util.HashSet());
        java.util.List<android.credentials.selection.DisabledProviderData> disabledProviderDataList = allProviders.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.credentials.CredentialManagerUi$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.credentials.CredentialManagerUi.lambda$createPendingIntent$0((android.credentials.CredentialProviderInfo) obj);
            }
        }).map(new java.util.function.Function() { // from class: com.android.server.credentials.CredentialManagerUi$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.credentials.CredentialManagerUi.lambda$createPendingIntent$1((android.credentials.CredentialProviderInfo) obj);
            }
        }).toList();
        android.credentials.selection.IntentCreationResult intentCreationResult = android.credentials.selection.IntentFactory.createCredentialSelectorIntentForCredMan(this.mContext, requestInfo, providerDataList, new java.util.ArrayList(disabledProviderDataList), this.mResultReceiver);
        requestSessionMetric.collectUiConfigurationResults(this.mContext, intentCreationResult, this.mUserId);
        android.content.Intent intent = intentCreationResult.getIntent();
        intent.setAction(java.util.UUID.randomUUID().toString());
        return android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, 33554432, null, android.os.UserHandle.of(this.mUserId));
    }

    static /* synthetic */ boolean lambda$createPendingIntent$0(android.credentials.CredentialProviderInfo provider) {
        return !provider.isEnabled();
    }

    static /* synthetic */ android.credentials.selection.DisabledProviderData lambda$createPendingIntent$1(android.credentials.CredentialProviderInfo disabledProvider) {
        return new android.credentials.selection.DisabledProviderData(disabledProvider.getComponentName().flattenToString());
    }

    public android.content.Intent createIntentForAutofill(android.credentials.selection.RequestInfo requestInfo, com.android.server.credentials.metrics.RequestSessionMetric requestSessionMetric) {
        android.credentials.selection.IntentCreationResult intentCreationResult = android.credentials.selection.IntentFactory.createCredentialSelectorIntentForAutofill(this.mContext, requestInfo, new java.util.ArrayList(), this.mResultReceiver);
        requestSessionMetric.collectUiConfigurationResults(this.mContext, intentCreationResult, this.mUserId);
        return intentCreationResult.getIntent();
    }
}
