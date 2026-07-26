package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public class PrepareGetRequestSession extends com.android.server.credentials.GetRequestSession {
    private static final java.lang.String TAG = "CredentialManager";
    private final android.credentials.IPrepareGetCredentialCallback mPrepareGetCredentialCallback;

    public PrepareGetRequestSession(android.content.Context context, com.android.server.credentials.RequestSession.SessionLifetime sessionCallback, java.lang.Object lock, int userId, int callingUid, android.credentials.IGetCredentialCallback getCredCallback, android.credentials.GetCredentialRequest request, android.service.credentials.CallingAppInfo callingAppInfo, java.util.Set<android.content.ComponentName> enabledProviders, android.os.CancellationSignal cancellationSignal, long startedTimestamp, android.credentials.IPrepareGetCredentialCallback prepareGetCredentialCallback) {
        super(context, sessionCallback, lock, userId, callingUid, getCredCallback, request, callingAppInfo, enabledProviders, cancellationSignal, startedTimestamp);
        ((java.util.Set) request.getCredentialOptions().stream().map(new com.android.server.credentials.CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda3()).collect(java.util.stream.Collectors.toSet())).size();
        this.mRequestSessionMetric.collectGetFlowInitialMetricInfo(request);
        this.mPrepareGetCredentialCallback = prepareGetCredentialCallback;
    }

    @Override // com.android.server.credentials.GetRequestSession, com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onProviderStatusChanged(com.android.server.credentials.ProviderSession.Status status, android.content.ComponentName componentName, com.android.server.credentials.ProviderSession.CredentialsSource source) {
        android.util.Slog.i(TAG, "Provider Status changed with status: " + status + ", and source: " + source);
        switch (source) {
            case REMOTE_PROVIDER:
                if (!isAnyProviderPending()) {
                    boolean hasQueryCandidatePermission = android.service.credentials.PermissionUtils.hasPermission(this.mContext, this.mClientAppInfo.getPackageName(), "android.permission.CREDENTIAL_MANAGER_QUERY_CANDIDATE_CREDENTIALS");
                    if (isUiInvocationNeeded()) {
                        java.util.ArrayList<android.credentials.selection.ProviderData> providerData = getProviderDataForUi();
                        if (!providerData.isEmpty()) {
                            constructPendingResponseAndInvokeCallback(hasQueryCandidatePermission, getCredentialResultTypes(hasQueryCandidatePermission), hasAuthenticationResults(providerData, hasQueryCandidatePermission), hasRemoteResults(providerData, hasQueryCandidatePermission), getUiIntent());
                        }
                    }
                    constructEmptyPendingResponseAndInvokeCallback(hasQueryCandidatePermission);
                    break;
                }
                break;
            case AUTH_ENTRY:
                if (status == com.android.server.credentials.ProviderSession.Status.NO_CREDENTIALS_FROM_AUTH_ENTRY) {
                    super.handleEmptyAuthenticationSelection(componentName);
                } else if (status == com.android.server.credentials.ProviderSession.Status.CREDENTIALS_RECEIVED) {
                    getProviderDataAndInitiateUi();
                }
                break;
            default:
                android.util.Slog.w(TAG, "Unexpected source");
                break;
        }
    }

    private void constructPendingResponseAndInvokeCallback(boolean hasPermission, java.util.Set<java.lang.String> credentialTypes, boolean hasAuthenticationResults, boolean hasRemoteResults, android.app.PendingIntent uiIntent) {
        try {
            this.mPrepareGetCredentialCallback.onResponse(new android.credentials.PrepareGetCredentialResponseInternal(hasPermission, credentialTypes, hasAuthenticationResults, hasRemoteResults, uiIntent));
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "EXCEPTION while mPendingCallback.onResponse", e);
        }
    }

    private void constructEmptyPendingResponseAndInvokeCallback(boolean hasQueryCandidatePermission) {
        try {
            this.mPrepareGetCredentialCallback.onResponse(new android.credentials.PrepareGetCredentialResponseInternal(hasQueryCandidatePermission, (java.util.Set) null, false, false, (android.app.PendingIntent) null));
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "EXCEPTION while mPendingCallback.onResponse", e);
        }
    }

    private boolean hasRemoteResults(java.util.ArrayList<android.credentials.selection.ProviderData> providerData, boolean hasQueryCandidatePermission) {
        if (!hasQueryCandidatePermission) {
            return false;
        }
        return providerData.stream().map(new java.util.function.Function() { // from class: com.android.server.credentials.PrepareGetRequestSession$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.credentials.PrepareGetRequestSession.lambda$hasRemoteResults$0((android.credentials.selection.ProviderData) obj);
            }
        }).anyMatch(new java.util.function.Predicate() { // from class: com.android.server.credentials.PrepareGetRequestSession$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.credentials.PrepareGetRequestSession.lambda$hasRemoteResults$1((android.credentials.selection.GetCredentialProviderData) obj);
            }
        });
    }

    static /* synthetic */ android.credentials.selection.GetCredentialProviderData lambda$hasRemoteResults$0(android.credentials.selection.ProviderData data) {
        return (android.credentials.selection.GetCredentialProviderData) data;
    }

    static /* synthetic */ boolean lambda$hasRemoteResults$1(android.credentials.selection.GetCredentialProviderData getCredentialProviderData) {
        return getCredentialProviderData.getRemoteEntry() != null;
    }

    private boolean hasAuthenticationResults(java.util.ArrayList<android.credentials.selection.ProviderData> providerData, boolean hasQueryCandidatePermission) {
        if (!hasQueryCandidatePermission) {
            return false;
        }
        return providerData.stream().map(new java.util.function.Function() { // from class: com.android.server.credentials.PrepareGetRequestSession$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.credentials.PrepareGetRequestSession.lambda$hasAuthenticationResults$2((android.credentials.selection.ProviderData) obj);
            }
        }).anyMatch(new java.util.function.Predicate() { // from class: com.android.server.credentials.PrepareGetRequestSession$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.credentials.PrepareGetRequestSession.lambda$hasAuthenticationResults$3((android.credentials.selection.GetCredentialProviderData) obj);
            }
        });
    }

    static /* synthetic */ android.credentials.selection.GetCredentialProviderData lambda$hasAuthenticationResults$2(android.credentials.selection.ProviderData data) {
        return (android.credentials.selection.GetCredentialProviderData) data;
    }

    static /* synthetic */ boolean lambda$hasAuthenticationResults$3(android.credentials.selection.GetCredentialProviderData getCredentialProviderData) {
        return !getCredentialProviderData.getAuthenticationEntries().isEmpty();
    }

    private java.util.Set<java.lang.String> getCredentialResultTypes(boolean hasQueryCandidatePermission) {
        if (!hasQueryCandidatePermission) {
            return null;
        }
        return (java.util.Set) this.mProviders.values().stream().map(new java.util.function.Function() { // from class: com.android.server.credentials.PrepareGetRequestSession$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.credentials.PrepareGetRequestSession.lambda$getCredentialResultTypes$4((com.android.server.credentials.ProviderSession) obj);
            }
        }).flatMap(new java.util.function.Function() { // from class: com.android.server.credentials.PrepareGetRequestSession$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.credentials.ProviderGetSession) obj).getCredentialEntryTypes().stream();
            }
        }).collect(java.util.stream.Collectors.toSet());
    }

    static /* synthetic */ com.android.server.credentials.ProviderGetSession lambda$getCredentialResultTypes$4(com.android.server.credentials.ProviderSession session) {
        return (com.android.server.credentials.ProviderGetSession) session;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private android.app.PendingIntent getUiIntent() {
        java.util.ArrayList<android.credentials.selection.ProviderData> providerDataList = new java.util.ArrayList<>();
        for (com.android.server.credentials.ProviderSession session : this.mProviders.values()) {
            android.credentials.selection.ProviderData providerData = session.mo3091prepareUiData();
            if (providerData != null) {
                providerDataList.add(providerData);
            }
        }
        if (!providerDataList.isEmpty()) {
            return this.mCredentialManagerUi.createPendingIntent(android.credentials.selection.RequestInfo.newGetRequestInfo(this.mRequestId, (android.credentials.GetCredentialRequest) this.mClientRequest, this.mClientAppInfo.getPackageName(), android.service.credentials.PermissionUtils.hasPermission(this.mContext, this.mClientAppInfo.getPackageName(), "android.permission.CREDENTIAL_MANAGER_SET_ALLOWED_PROVIDERS"), false), providerDataList, this.mRequestSessionMetric);
        }
        return null;
    }
}
