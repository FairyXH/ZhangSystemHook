package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public class ProviderRegistryGetSession extends com.android.server.credentials.ProviderSession<android.credentials.CredentialOption, java.util.Set<com.android.server.credentials.CredentialDescriptionRegistry.FilterResult>> {
    static final java.lang.String CREDENTIAL_ENTRY_KEY = "credential_key";
    private static final java.lang.String TAG = "CredentialManager";
    private final android.service.credentials.CallingAppInfo mCallingAppInfo;
    private final com.android.server.credentials.CredentialDescriptionRegistry mCredentialDescriptionRegistry;
    java.util.List<android.service.credentials.CredentialEntry> mCredentialEntries;
    private final java.lang.String mCredentialProviderPackageName;
    private final java.util.Set<java.lang.String> mElementKeys;
    private final java.util.Map<java.lang.String, android.service.credentials.CredentialEntry> mUiCredentialEntries;

    public static com.android.server.credentials.ProviderRegistryGetSession createNewSession(android.content.Context context, int userId, com.android.server.credentials.GetRequestSession getRequestSession, android.service.credentials.CallingAppInfo callingAppInfo, java.lang.String credentialProviderPackageName, android.credentials.CredentialOption requestOption) {
        return new com.android.server.credentials.ProviderRegistryGetSession(context, userId, getRequestSession, callingAppInfo, credentialProviderPackageName, requestOption);
    }

    public static com.android.server.credentials.ProviderRegistryGetSession createNewSession(android.content.Context context, int userId, com.android.server.credentials.PrepareGetRequestSession getRequestSession, android.service.credentials.CallingAppInfo callingAppInfo, java.lang.String credentialProviderPackageName, android.credentials.CredentialOption requestOption) {
        return new com.android.server.credentials.ProviderRegistryGetSession(context, userId, getRequestSession, callingAppInfo, credentialProviderPackageName, requestOption);
    }

    protected ProviderRegistryGetSession(android.content.Context context, int userId, com.android.server.credentials.GetRequestSession session, android.service.credentials.CallingAppInfo callingAppInfo, java.lang.String servicePackageName, android.credentials.CredentialOption requestOption) {
        super(context, requestOption, session, new android.content.ComponentName(servicePackageName, java.util.UUID.randomUUID().toString()), userId, null);
        this.mUiCredentialEntries = new java.util.HashMap();
        this.mCredentialDescriptionRegistry = com.android.server.credentials.CredentialDescriptionRegistry.forUser(userId);
        this.mCallingAppInfo = callingAppInfo;
        this.mCredentialProviderPackageName = servicePackageName;
        this.mElementKeys = new java.util.HashSet(requestOption.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS"));
        this.mStatus = com.android.server.credentials.ProviderSession.Status.PENDING;
    }

    protected ProviderRegistryGetSession(android.content.Context context, int userId, com.android.server.credentials.PrepareGetRequestSession session, android.service.credentials.CallingAppInfo callingAppInfo, java.lang.String servicePackageName, android.credentials.CredentialOption requestOption) {
        super(context, requestOption, session, new android.content.ComponentName(servicePackageName, java.util.UUID.randomUUID().toString()), userId, null);
        this.mUiCredentialEntries = new java.util.HashMap();
        this.mCredentialDescriptionRegistry = com.android.server.credentials.CredentialDescriptionRegistry.forUser(userId);
        this.mCallingAppInfo = callingAppInfo;
        this.mCredentialProviderPackageName = servicePackageName;
        this.mElementKeys = new java.util.HashSet(requestOption.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS"));
        this.mStatus = com.android.server.credentials.ProviderSession.Status.PENDING;
    }

    private java.util.List<android.credentials.selection.Entry> prepareUiCredentialEntries(java.util.List<android.service.credentials.CredentialEntry> credentialEntries) {
        java.util.List<android.credentials.selection.Entry> credentialUiEntries = new java.util.ArrayList<>();
        for (android.service.credentials.CredentialEntry credentialEntry : credentialEntries) {
            java.lang.String entryId = generateUniqueId();
            this.mUiCredentialEntries.put(entryId, credentialEntry);
            credentialUiEntries.add(new android.credentials.selection.Entry("credential_key", entryId, credentialEntry.getSlice(), setUpFillInIntent()));
        }
        return credentialUiEntries;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private android.content.Intent setUpFillInIntent() {
        android.content.Intent intent = new android.content.Intent();
        intent.putExtra("android.service.credentials.extra.GET_CREDENTIAL_REQUEST", new android.service.credentials.GetCredentialRequest(this.mCallingAppInfo, java.util.List.of((android.credentials.CredentialOption) this.mProviderRequest)));
        return intent;
    }

    @Override // com.android.server.credentials.ProviderSession
    /* JADX INFO: renamed from: prepareUiData */
    protected android.credentials.selection.ProviderData mo3091prepareUiData() {
        if (!com.android.server.credentials.ProviderSession.isUiInvokingStatus(getStatus())) {
            android.util.Slog.i(TAG, "No date for UI coming from: " + this.mComponentName.flattenToString());
            return null;
        }
        if (this.mProviderResponse == 0) {
            android.util.Slog.w(TAG, "response is null when preparing ui data. This is strange.");
            return null;
        }
        return new android.credentials.selection.GetCredentialProviderData.Builder(this.mComponentName.flattenToString()).setActionChips(java.util.Collections.EMPTY_LIST).setAuthenticationEntries(java.util.Collections.EMPTY_LIST).setCredentialEntries(prepareUiCredentialEntries((java.util.List) ((java.util.Set) this.mProviderResponse).stream().flatMap(new java.util.function.Function() { // from class: com.android.server.credentials.ProviderRegistryGetSession$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.credentials.CredentialDescriptionRegistry.FilterResult) obj).mCredentialEntries.stream();
            }
        }).collect(java.util.stream.Collectors.toList()))).build();
    }

    @Override // com.android.server.credentials.ProviderSession
    protected void onUiEntrySelected(java.lang.String entryType, java.lang.String entryKey, android.credentials.selection.ProviderPendingIntentResponse providerPendingIntentResponse) {
        byte b;
        switch (entryType.hashCode()) {
            case 1208398455:
                if (entryType.equals("credential_key")) {
                    b = 0;
                    break;
                }
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                android.service.credentials.CredentialEntry credentialEntry = this.mUiCredentialEntries.get(entryKey);
                if (credentialEntry == null) {
                    android.util.Slog.i(TAG, "Unexpected credential entry key");
                } else {
                    onCredentialEntrySelected(credentialEntry, providerPendingIntentResponse);
                }
                break;
            default:
                android.util.Slog.i(TAG, "Unsupported entry type selected");
                break;
        }
    }

    private void onCredentialEntrySelected(android.service.credentials.CredentialEntry credentialEntry, android.credentials.selection.ProviderPendingIntentResponse providerPendingIntentResponse) {
        if (providerPendingIntentResponse != null) {
            android.credentials.GetCredentialException exception = maybeGetPendingIntentException(providerPendingIntentResponse);
            if (exception != null) {
                invokeCallbackWithError(exception.getType(), exception.getMessage());
                return;
            }
            android.credentials.GetCredentialResponse getCredentialResponse = com.android.server.credentials.PendingIntentResultHandler.extractGetCredentialResponse(providerPendingIntentResponse.getResultData());
            if (getCredentialResponse != null) {
                if (this.mCallbacks != null) {
                    ((com.android.server.credentials.GetRequestSession) this.mCallbacks).onFinalResponseReceived(this.mComponentName, getCredentialResponse);
                    return;
                }
                return;
            }
        }
        android.util.Slog.w(TAG, "CredentialEntry does not have a credential or a pending intent result");
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderResponseSuccess(java.util.Set<com.android.server.credentials.CredentialDescriptionRegistry.FilterResult> response) {
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderResponseFailure(int internalErrorCode, java.lang.Exception e) {
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderServiceDied(com.android.server.credentials.RemoteCredentialService service) {
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderCancellable(android.os.ICancellationSignal cancellation) {
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [R, java.util.Set] */
    @Override // com.android.server.credentials.ProviderSession
    protected void invokeSession() {
        startCandidateMetrics();
        this.mProviderResponse = this.mCredentialDescriptionRegistry.getFilteredResultForProvider(this.mCredentialProviderPackageName, this.mElementKeys);
        this.mCredentialEntries = (java.util.List) ((java.util.Set) this.mProviderResponse).stream().flatMap(new java.util.function.Function() { // from class: com.android.server.credentials.ProviderRegistryGetSession$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.credentials.CredentialDescriptionRegistry.FilterResult) obj).mCredentialEntries.stream();
            }
        }).collect(java.util.stream.Collectors.toList());
        updateStatusAndInvokeCallback(com.android.server.credentials.ProviderSession.Status.CREDENTIALS_RECEIVED, com.android.server.credentials.ProviderSession.CredentialsSource.REGISTRY);
        this.mProviderSessionMetric.collectCandidateEntryMetrics(this.mCredentialEntries);
    }

    protected android.credentials.GetCredentialException maybeGetPendingIntentException(android.credentials.selection.ProviderPendingIntentResponse pendingIntentResponse) {
        if (pendingIntentResponse == null) {
            return null;
        }
        if (com.android.server.credentials.PendingIntentResultHandler.isValidResponse(pendingIntentResponse)) {
            android.credentials.GetCredentialException exception = com.android.server.credentials.PendingIntentResultHandler.extractGetCredentialException(pendingIntentResponse.getResultData());
            if (exception == null) {
                return null;
            }
            android.util.Slog.i(TAG, "Pending intent contains provider exception");
            return exception;
        }
        if (com.android.server.credentials.PendingIntentResultHandler.isCancelledResponse(pendingIntentResponse)) {
            return new android.credentials.GetCredentialException("android.credentials.GetCredentialException.TYPE_USER_CANCELED");
        }
        return new android.credentials.GetCredentialException("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL");
    }
}
