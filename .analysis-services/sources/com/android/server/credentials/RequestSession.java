package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
abstract class RequestSession<T, U, V> implements com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback {
    private static final java.lang.String TAG = "CredentialManager";
    private final int mCallingUid;
    protected final android.os.CancellationSignal mCancellationSignal;
    protected final android.service.credentials.CallingAppInfo mClientAppInfo;
    protected final U mClientCallback;
    protected final T mClientRequest;
    protected final android.content.Context mContext;
    protected final com.android.server.credentials.CredentialManagerUi mCredentialManagerUi;
    private final java.util.Set<android.content.ComponentName> mEnabledProviders;
    protected final java.lang.String mHybridService;
    protected final java.lang.Object mLock;
    protected android.app.PendingIntent mPendingIntent;
    protected final java.lang.String mRequestType;
    protected final com.android.server.credentials.RequestSession.SessionLifetime mSessionCallback;
    protected final int mUserId;
    protected final java.util.Map<java.lang.String, com.android.server.credentials.ProviderSession> mProviders = new java.util.concurrent.ConcurrentHashMap();
    private final com.android.server.credentials.RequestSession<T, U, V>.RequestSessionDeathRecipient mDeathRecipient = new com.android.server.credentials.RequestSession.RequestSessionDeathRecipient();
    protected com.android.server.credentials.RequestSession.RequestSessionStatus mRequestSessionStatus = com.android.server.credentials.RequestSession.RequestSessionStatus.IN_PROGRESS;
    protected final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper(), null, true);
    protected final android.os.IBinder mRequestId = new android.os.Binder();
    protected final int mUniqueSessionInteger = com.android.server.credentials.MetricUtilities.getHighlyUniqueInteger();
    protected final com.android.server.credentials.metrics.RequestSessionMetric mRequestSessionMetric = new com.android.server.credentials.metrics.RequestSessionMetric(this.mUniqueSessionInteger, com.android.server.credentials.MetricUtilities.getHighlyUniqueInteger());

    enum RequestSessionStatus {
        IN_PROGRESS,
        CANCELLED,
        COMPLETE
    }

    public interface SessionLifetime {
        void onFinishRequestSession(int i, android.os.IBinder iBinder);
    }

    public abstract com.android.server.credentials.ProviderSession initiateProviderSession(android.credentials.CredentialProviderInfo credentialProviderInfo, com.android.server.credentials.RemoteCredentialService remoteCredentialService);

    protected abstract void invokeClientCallbackError(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    protected abstract void invokeClientCallbackSuccess(V v) throws android.os.RemoteException;

    protected abstract void launchUiWithProviderData(java.util.ArrayList<android.credentials.selection.ProviderData> arrayList);

    protected RequestSession(android.content.Context context, com.android.server.credentials.RequestSession.SessionLifetime sessionCallback, java.lang.Object lock, int userId, int callingUid, T clientRequest, U clientCallback, java.lang.String requestType, android.service.credentials.CallingAppInfo callingAppInfo, java.util.Set<android.content.ComponentName> enabledProviders, android.os.CancellationSignal cancellationSignal, long timestampStarted, boolean shouldBindClientToDeath) {
        this.mContext = context;
        this.mLock = lock;
        this.mSessionCallback = sessionCallback;
        this.mUserId = userId;
        this.mCallingUid = callingUid;
        this.mClientRequest = clientRequest;
        this.mClientCallback = clientCallback;
        this.mRequestType = requestType;
        this.mClientAppInfo = callingAppInfo;
        this.mEnabledProviders = enabledProviders;
        this.mCancellationSignal = cancellationSignal;
        this.mCredentialManagerUi = new com.android.server.credentials.CredentialManagerUi(this.mContext, this.mUserId, this, this.mEnabledProviders);
        this.mHybridService = context.getResources().getString(android.R.string.config_defaultMusicRecognitionService);
        this.mRequestSessionMetric.collectInitialPhaseMetricInfo(timestampStarted, this.mCallingUid, com.android.server.credentials.metrics.ApiName.getMetricCodeFromRequestInfo(this.mRequestType));
        setCancellationListener();
        if (shouldBindClientToDeath && com.android.internal.hidden_from_bootclasspath.android.credentials.flags.Flags.clearSessionEnabled() && this.mClientCallback != null && (this.mClientCallback instanceof android.os.IInterface)) {
            setUpClientCallbackListener(((android.os.IInterface) this.mClientCallback).asBinder());
        }
    }

    protected void setUpClientCallbackListener(android.os.IBinder clientBinder) {
        if (this.mClientCallback != null && (this.mClientCallback instanceof android.os.IInterface)) {
            try {
                clientBinder.linkToDeath(this.mDeathRecipient, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, e.getMessage());
            }
        }
    }

    private void setCancellationListener() {
        this.mCancellationSignal.setOnCancelListener(new android.os.CancellationSignal.OnCancelListener() { // from class: com.android.server.credentials.RequestSession$$ExternalSyntheticLambda1
            @Override // android.os.CancellationSignal.OnCancelListener
            public final void onCancel() {
                this.f$0.lambda$setCancellationListener$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCancellationListener$0() {
        android.util.Slog.d(TAG, "Cancellation invoked from the client - clearing session");
        boolean isUiActive = maybeCancelUi();
        finishSession(!isUiActive, com.android.server.credentials.metrics.ApiStatus.CLIENT_CANCELED.getMetricCode());
    }

    private boolean maybeCancelUi() {
        if (this.mCredentialManagerUi.getStatus() == com.android.server.credentials.CredentialManagerUi.UiStatus.USER_INTERACTION) {
            long originalCallingUidToken = android.os.Binder.clearCallingIdentity();
            try {
                this.mContext.startActivityAsUser(this.mCredentialManagerUi.createCancelIntent(this.mRequestId, this.mClientAppInfo.getPackageName()).addFlags(268435456), android.os.UserHandle.of(this.mUserId));
                android.os.Binder.restoreCallingIdentity(originalCallingUidToken);
                return true;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(originalCallingUidToken);
                throw th;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUiWaitingForData() {
        return this.mCredentialManagerUi.getStatus() == com.android.server.credentials.CredentialManagerUi.UiStatus.IN_PROGRESS;
    }

    public void addProviderSession(android.content.ComponentName componentName, com.android.server.credentials.ProviderSession providerSession) {
        this.mProviders.put(componentName.flattenToString(), providerSession);
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiSelection(android.credentials.selection.UserSelectionDialogResult selection) {
        if (this.mRequestSessionStatus == com.android.server.credentials.RequestSession.RequestSessionStatus.COMPLETE) {
            android.util.Slog.w(TAG, "Request has already been completed. This is strange.");
            return;
        }
        if (isSessionCancelled()) {
            finishSession(true, com.android.server.credentials.metrics.ApiStatus.CLIENT_CANCELED.getMetricCode());
            return;
        }
        java.lang.String providerId = selection.getProviderId();
        com.android.server.credentials.ProviderSession providerSession = this.mProviders.get(providerId);
        if (providerSession == null) {
            android.util.Slog.w(TAG, "providerSession not found in onUiSelection. This is strange.");
            return;
        }
        com.android.server.credentials.metrics.ProviderSessionMetric providerSessionMetric = providerSession.mProviderSessionMetric;
        int initialAuthMetricsProvider = providerSessionMetric.getBrowsedAuthenticationMetric().size();
        this.mRequestSessionMetric.collectMetricPerBrowsingSelect(selection, providerSession.mProviderSessionMetric.getCandidatePhasePerProviderMetric());
        providerSession.onUiEntrySelected(selection.getEntryKey(), selection.getEntrySubkey(), selection.getPendingIntentProviderResponse());
        int numAuthPerProvider = providerSessionMetric.getBrowsedAuthenticationMetric().size();
        boolean authMetricLogged = numAuthPerProvider - initialAuthMetricsProvider == 1;
        if (authMetricLogged) {
            this.mRequestSessionMetric.logAuthEntry(providerSession.mProviderSessionMetric.getBrowsedAuthenticationMetric().get(numAuthPerProvider - 1));
        }
    }

    protected void finishSession(boolean propagateCancellation, int apiStatus) {
        android.util.Slog.i(TAG, "finishing session with propagateCancellation " + propagateCancellation);
        if (propagateCancellation) {
            this.mProviders.values().forEach(new java.util.function.Consumer() { // from class: com.android.server.credentials.RequestSession$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.credentials.ProviderSession) obj).cancelProviderRemoteSession();
                }
            });
        }
        this.mRequestSessionMetric.logApiCalledAtFinish(apiStatus);
        this.mRequestSessionStatus = com.android.server.credentials.RequestSession.RequestSessionStatus.COMPLETE;
        this.mProviders.clear();
        clearRequestSessionLocked();
    }

    void cancelExistingPendingIntent() {
        if (this.mPendingIntent != null) {
            try {
                this.mPendingIntent.cancel();
                this.mPendingIntent = null;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Unable to cancel existing pending intent", e);
            }
        }
    }

    private void clearRequestSessionLocked() {
        synchronized (this.mLock) {
            this.mSessionCallback.onFinishRequestSession(this.mUserId, this.mRequestId);
        }
    }

    protected boolean isAnyProviderPending() {
        for (com.android.server.credentials.ProviderSession session : this.mProviders.values()) {
            if (com.android.server.credentials.ProviderSession.isStatusWaitingForRemoteResponse(session.getStatus())) {
                return true;
            }
        }
        return false;
    }

    protected boolean isSessionCancelled() {
        return this.mCancellationSignal.isCanceled();
    }

    protected boolean isUiInvocationNeeded() {
        for (com.android.server.credentials.ProviderSession session : this.mProviders.values()) {
            if (com.android.server.credentials.ProviderSession.isUiInvokingStatus(session.getStatus())) {
                return true;
            }
            if (com.android.server.credentials.ProviderSession.isStatusWaitingForRemoteResponse(session.getStatus())) {
                return false;
            }
        }
        return false;
    }

    void getProviderDataAndInitiateUi() {
        java.util.ArrayList<android.credentials.selection.ProviderData> providerDataList = getProviderDataForUi();
        if (!providerDataList.isEmpty()) {
            launchUiWithProviderData(providerDataList);
        }
    }

    protected java.util.ArrayList<android.credentials.selection.ProviderData> getProviderDataForUi() {
        android.util.Slog.i(TAG, "For ui, provider data size: " + this.mProviders.size());
        java.util.ArrayList<android.credentials.selection.ProviderData> providerDataList = new java.util.ArrayList<>();
        this.mRequestSessionMetric.logCandidatePhaseMetrics(this.mProviders);
        if (isSessionCancelled()) {
            finishSession(true, com.android.server.credentials.metrics.ApiStatus.CLIENT_CANCELED.getMetricCode());
            return providerDataList;
        }
        for (com.android.server.credentials.ProviderSession session : this.mProviders.values()) {
            android.credentials.selection.ProviderData providerData = session.mo3091prepareUiData();
            if (providerData != null) {
                providerDataList.add(providerData);
            }
        }
        return providerDataList;
    }

    protected void respondToClientWithResponseAndFinish(V response) {
        this.mRequestSessionMetric.logCandidateAggregateMetrics(this.mProviders);
        this.mRequestSessionMetric.collectFinalPhaseProviderMetricStatus(false, com.android.server.credentials.metrics.ProviderStatusForMetrics.FINAL_SUCCESS);
        if (this.mRequestSessionStatus == com.android.server.credentials.RequestSession.RequestSessionStatus.COMPLETE) {
            android.util.Slog.w(TAG, "Request has already been completed. This is strange.");
            return;
        }
        if (isSessionCancelled()) {
            finishSession(true, com.android.server.credentials.metrics.ApiStatus.CLIENT_CANCELED.getMetricCode());
            return;
        }
        try {
            invokeClientCallbackSuccess(response);
            finishSession(false, com.android.server.credentials.metrics.ApiStatus.SUCCESS.getMetricCode());
        } catch (android.os.RemoteException e) {
            this.mRequestSessionMetric.collectFinalPhaseProviderMetricStatus(true, com.android.server.credentials.metrics.ProviderStatusForMetrics.FINAL_FAILURE);
            android.util.Slog.e(TAG, "Issue while responding to client with a response : " + e);
            finishSession(false, com.android.server.credentials.metrics.ApiStatus.FAILURE.getMetricCode());
        }
    }

    protected void respondToClientWithErrorAndFinish(java.lang.String errorType, java.lang.String errorMsg) {
        this.mRequestSessionMetric.logCandidateAggregateMetrics(this.mProviders);
        this.mRequestSessionMetric.collectFinalPhaseProviderMetricStatus(true, com.android.server.credentials.metrics.ProviderStatusForMetrics.FINAL_FAILURE);
        if (this.mRequestSessionStatus == com.android.server.credentials.RequestSession.RequestSessionStatus.COMPLETE) {
            android.util.Slog.w(TAG, "Request has already been completed. This is strange.");
            return;
        }
        if (isSessionCancelled()) {
            finishSession(true, com.android.server.credentials.metrics.ApiStatus.CLIENT_CANCELED.getMetricCode());
            return;
        }
        try {
            invokeClientCallbackError(errorType, errorMsg);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Issue while responding to client with error : " + e);
        }
        boolean isUserCanceled = errorType.contains(com.android.server.credentials.MetricUtilities.USER_CANCELED_SUBSTRING);
        if (isUserCanceled) {
            this.mRequestSessionMetric.setHasExceptionFinalPhase(false);
            finishSession(false, com.android.server.credentials.metrics.ApiStatus.USER_CANCELED.getMetricCode());
        } else {
            finishSession(false, com.android.server.credentials.metrics.ApiStatus.FAILURE.getMetricCode());
        }
    }

    protected boolean isPrimaryProviderViaProviderInfo(android.content.ComponentName componentName) {
        com.android.server.credentials.ProviderSession chosenProviderSession = this.mProviders.get(componentName.flattenToString());
        return (chosenProviderSession == null || chosenProviderSession.mProviderInfo == null || !chosenProviderSession.mProviderInfo.isPrimary()) ? false : true;
    }

    private class RequestSessionDeathRecipient implements android.os.IBinder.DeathRecipient {
        private RequestSessionDeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Slog.d(com.android.server.credentials.RequestSession.TAG, "Client binder died - clearing session");
            com.android.server.credentials.RequestSession.this.finishSession(com.android.server.credentials.RequestSession.this.isUiWaitingForData(), com.android.server.credentials.metrics.ApiStatus.CLIENT_CANCELED.getMetricCode());
        }
    }
}
