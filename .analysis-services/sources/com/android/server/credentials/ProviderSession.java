package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public abstract class ProviderSession<T, R> implements com.android.server.credentials.RemoteCredentialService.ProviderCallbacks<R> {
    private static final java.lang.String TAG = "CredentialManager";
    protected final com.android.server.credentials.ProviderSession.ProviderInternalCallback mCallbacks;
    protected final android.content.ComponentName mComponentName;
    protected final android.content.Context mContext;
    protected android.credentials.Credential mFinalCredentialResponse;
    protected android.os.ICancellationSignal mProviderCancellationSignal;
    protected final T mProviderRequest;
    protected R mProviderResponse;
    protected final com.android.server.credentials.metrics.ProviderSessionMetric mProviderSessionMetric;
    private int mProviderSessionUid;
    protected final com.android.server.credentials.RemoteCredentialService mRemoteCredentialService;
    protected final int mUserId;
    protected com.android.server.credentials.ProviderSession.Status mStatus = com.android.server.credentials.ProviderSession.Status.NOT_STARTED;
    protected java.lang.Boolean mProviderResponseSet = false;
    protected final android.credentials.CredentialProviderInfo mProviderInfo = null;

    enum CredentialsSource {
        REMOTE_PROVIDER,
        REGISTRY,
        AUTH_ENTRY
    }

    public interface ProviderInternalCallback<V> {
        void onFinalErrorReceived(android.content.ComponentName componentName, java.lang.String str, java.lang.String str2);

        void onFinalResponseReceived(android.content.ComponentName componentName, V v);

        void onProviderStatusChanged(com.android.server.credentials.ProviderSession.Status status, android.content.ComponentName componentName, com.android.server.credentials.ProviderSession.CredentialsSource credentialsSource);
    }

    enum Status {
        NOT_STARTED,
        PENDING,
        CREDENTIALS_RECEIVED,
        SERVICE_DEAD,
        SAVE_ENTRIES_RECEIVED,
        CANCELED,
        EMPTY_RESPONSE,
        NO_CREDENTIALS_FROM_AUTH_ENTRY,
        COMPLETE
    }

    protected abstract void invokeSession();

    protected abstract void onUiEntrySelected(java.lang.String str, java.lang.String str2, android.credentials.selection.ProviderPendingIntentResponse providerPendingIntentResponse);

    /* JADX INFO: renamed from: prepareUiData */
    protected abstract android.credentials.selection.ProviderData mo3091prepareUiData();

    public static boolean isUiInvokingStatus(com.android.server.credentials.ProviderSession.Status status) {
        return status == com.android.server.credentials.ProviderSession.Status.CREDENTIALS_RECEIVED || status == com.android.server.credentials.ProviderSession.Status.SAVE_ENTRIES_RECEIVED || status == com.android.server.credentials.ProviderSession.Status.NO_CREDENTIALS_FROM_AUTH_ENTRY;
    }

    public static boolean isStatusWaitingForRemoteResponse(com.android.server.credentials.ProviderSession.Status status) {
        return status == com.android.server.credentials.ProviderSession.Status.PENDING;
    }

    public static boolean isTerminatingStatus(com.android.server.credentials.ProviderSession.Status status) {
        return status == com.android.server.credentials.ProviderSession.Status.CANCELED || status == com.android.server.credentials.ProviderSession.Status.SERVICE_DEAD;
    }

    public static boolean isCompletionStatus(com.android.server.credentials.ProviderSession.Status status) {
        return status == com.android.server.credentials.ProviderSession.Status.COMPLETE || status == com.android.server.credentials.ProviderSession.Status.EMPTY_RESPONSE;
    }

    public com.android.server.credentials.metrics.ProviderSessionMetric getProviderSessionMetric() {
        return this.mProviderSessionMetric;
    }

    protected ProviderSession(android.content.Context context, T providerRequest, com.android.server.credentials.ProviderSession.ProviderInternalCallback callbacks, android.content.ComponentName componentName, int userId, com.android.server.credentials.RemoteCredentialService remoteCredentialService) {
        this.mContext = context;
        this.mProviderRequest = providerRequest;
        this.mCallbacks = callbacks;
        this.mUserId = userId;
        this.mComponentName = componentName;
        this.mRemoteCredentialService = remoteCredentialService;
        this.mProviderSessionUid = com.android.server.credentials.MetricUtilities.getPackageUid(this.mContext, this.mComponentName, userId);
        this.mProviderSessionMetric = new com.android.server.credentials.metrics.ProviderSessionMetric(((com.android.server.credentials.RequestSession) this.mCallbacks).mRequestSessionMetric.getSessionIdTrackTwo());
    }

    protected static java.lang.String generateUniqueId() {
        return java.util.UUID.randomUUID().toString();
    }

    public android.credentials.Credential getFinalCredentialResponse() {
        return this.mFinalCredentialResponse;
    }

    public void cancelProviderRemoteSession() {
        try {
            if (this.mProviderCancellationSignal != null) {
                this.mProviderCancellationSignal.cancel();
            }
            setStatus(com.android.server.credentials.ProviderSession.Status.CANCELED);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Issue while cancelling provider session: ", e);
        }
    }

    protected void setStatus(com.android.server.credentials.ProviderSession.Status status) {
        this.mStatus = status;
    }

    protected com.android.server.credentials.ProviderSession.Status getStatus() {
        return this.mStatus;
    }

    protected android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    protected com.android.server.credentials.RemoteCredentialService getRemoteCredentialService() {
        return this.mRemoteCredentialService;
    }

    protected void updateStatusAndInvokeCallback(com.android.server.credentials.ProviderSession.Status status, com.android.server.credentials.ProviderSession.CredentialsSource source) {
        setStatus(status);
        boolean isPrimary = this.mProviderInfo != null && this.mProviderInfo.isPrimary();
        this.mProviderSessionMetric.collectCandidateMetricUpdate(isTerminatingStatus(status) || isStatusWaitingForRemoteResponse(status), isCompletionStatus(status) || isUiInvokingStatus(status), this.mProviderSessionUid, source == com.android.server.credentials.ProviderSession.CredentialsSource.AUTH_ENTRY, isPrimary);
        this.mCallbacks.onProviderStatusChanged(status, this.mComponentName, source);
    }

    protected void startCandidateMetrics() {
        this.mProviderSessionMetric.collectCandidateMetricSetupViaInitialMetric(((com.android.server.credentials.RequestSession) this.mCallbacks).mRequestSessionMetric.getInitialPhaseMetric());
    }

    protected T getProviderRequest() {
        return this.mProviderRequest;
    }

    protected java.lang.Boolean isProviderResponseSet() {
        return java.lang.Boolean.valueOf(this.mProviderResponse != null || this.mProviderResponseSet.booleanValue());
    }

    protected void invokeCallbackWithError(java.lang.String errorType, java.lang.String errorMessage) {
        this.mCallbacks.onFinalErrorReceived(this.mComponentName, errorType, errorMessage);
    }

    protected R getProviderResponse() {
        return this.mProviderResponse;
    }

    protected boolean enforceRemoteEntryRestrictions(android.content.ComponentName expectedRemoteEntryProviderService) {
        if (!this.mComponentName.equals(expectedRemoteEntryProviderService)) {
            android.util.Slog.w(TAG, "Remote entry being dropped as it is not from the service configured by the OEM.");
            return false;
        }
        try {
            android.content.pm.ApplicationInfo appInfo = this.mContext.getPackageManager().getApplicationInfo(this.mComponentName.getPackageName(), android.content.pm.PackageManager.ApplicationInfoFlags.of(1048576L));
            if (appInfo != null) {
                if (this.mContext.checkPermission("android.permission.PROVIDE_REMOTE_CREDENTIALS", -1, appInfo.uid) == 0) {
                    return true;
                }
            }
            return false;
        } catch (android.content.pm.PackageManager.NameNotFoundException | java.lang.SecurityException e) {
            android.util.Slog.e(TAG, "Error getting info for " + this.mComponentName.flattenToString(), e);
            return false;
        }
    }
}
