package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public final class ProviderCreateSession extends com.android.server.credentials.ProviderSession<android.service.credentials.BeginCreateCredentialRequest, android.service.credentials.BeginCreateCredentialResponse> {
    private static final java.lang.String REMOTE_ENTRY_KEY = "remote_entry_key";
    public static final java.lang.String SAVE_ENTRY_KEY = "save_entry_key";
    private static final java.lang.String TAG = "CredentialManager";
    private final android.service.credentials.CreateCredentialRequest mCompleteRequest;
    private android.credentials.CreateCredentialException mProviderException;
    private final com.android.server.credentials.ProviderCreateSession.ProviderResponseDataHandler mProviderResponseDataHandler;

    /* JADX WARN: Multi-variable type inference failed */
    public static com.android.server.credentials.ProviderCreateSession createNewSession(android.content.Context context, int userId, android.credentials.CredentialProviderInfo providerInfo, com.android.server.credentials.CreateRequestSession createRequestSession, com.android.server.credentials.RemoteCredentialService remoteCredentialService) {
        android.service.credentials.CreateCredentialRequest providerCreateRequest = createProviderRequest(providerInfo.getCapabilities(), (android.credentials.CreateCredentialRequest) createRequestSession.mClientRequest, createRequestSession.mClientAppInfo, providerInfo.isSystemProvider());
        if (providerCreateRequest != null) {
            return new com.android.server.credentials.ProviderCreateSession(context, providerInfo, createRequestSession, userId, remoteCredentialService, constructQueryPhaseRequest(((android.credentials.CreateCredentialRequest) createRequestSession.mClientRequest).getType(), ((android.credentials.CreateCredentialRequest) createRequestSession.mClientRequest).getCandidateQueryData(), createRequestSession.mClientAppInfo, ((android.credentials.CreateCredentialRequest) createRequestSession.mClientRequest).alwaysSendAppInfoToProvider()), providerCreateRequest, createRequestSession.mHybridService);
        }
        android.util.Slog.i(TAG, "Unable to create provider session for: " + providerInfo.getComponentName());
        return null;
    }

    private static android.service.credentials.BeginCreateCredentialRequest constructQueryPhaseRequest(java.lang.String type, android.os.Bundle candidateQueryData, android.service.credentials.CallingAppInfo callingAppInfo, boolean propagateToProvider) {
        if (propagateToProvider) {
            return new android.service.credentials.BeginCreateCredentialRequest(type, candidateQueryData, callingAppInfo);
        }
        return new android.service.credentials.BeginCreateCredentialRequest(type, candidateQueryData);
    }

    private static android.service.credentials.CreateCredentialRequest createProviderRequest(java.util.List<java.lang.String> providerCapabilities, android.credentials.CreateCredentialRequest clientRequest, android.service.credentials.CallingAppInfo callingAppInfo, boolean isSystemProvider) {
        if (clientRequest.isSystemProviderRequired() && !isSystemProvider) {
            return null;
        }
        java.lang.String capability = clientRequest.getType();
        if (providerCapabilities.contains(capability)) {
            return new android.service.credentials.CreateCredentialRequest(callingAppInfo, capability, clientRequest.getCredentialData());
        }
        return null;
    }

    private ProviderCreateSession(android.content.Context context, android.credentials.CredentialProviderInfo info, com.android.server.credentials.ProviderSession.ProviderInternalCallback<android.credentials.CreateCredentialResponse> callbacks, int userId, com.android.server.credentials.RemoteCredentialService remoteCredentialService, android.service.credentials.BeginCreateCredentialRequest beginCreateRequest, android.service.credentials.CreateCredentialRequest completeCreateRequest, java.lang.String hybridService) {
        super(context, beginCreateRequest, callbacks, info.getComponentName(), userId, remoteCredentialService);
        this.mCompleteRequest = completeCreateRequest;
        setStatus(com.android.server.credentials.ProviderSession.Status.PENDING);
        this.mProviderResponseDataHandler = new com.android.server.credentials.ProviderCreateSession.ProviderResponseDataHandler(android.content.ComponentName.unflattenFromString(hybridService));
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderResponseSuccess(android.service.credentials.BeginCreateCredentialResponse response) {
        android.util.Slog.i(TAG, "Remote provider responded with a valid response: " + this.mComponentName);
        onSetInitialRemoteResponse(response);
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderResponseFailure(int errorCode, java.lang.Exception exception) {
        if (exception instanceof android.credentials.CreateCredentialException) {
            this.mProviderException = (android.credentials.CreateCredentialException) exception;
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

    /* JADX WARN: Multi-variable type inference failed */
    private void onSetInitialRemoteResponse(android.service.credentials.BeginCreateCredentialResponse beginCreateCredentialResponse) {
        this.mProviderResponse = beginCreateCredentialResponse;
        this.mProviderResponseDataHandler.addResponseContent(beginCreateCredentialResponse.getCreateEntries(), beginCreateCredentialResponse.getRemoteCreateEntry());
        if (this.mProviderResponseDataHandler.isEmptyResponse(beginCreateCredentialResponse)) {
            this.mProviderSessionMetric.collectCandidateEntryMetrics(beginCreateCredentialResponse, false, ((com.android.server.credentials.RequestSession) this.mCallbacks).mRequestSessionMetric.getInitialPhaseMetric());
            updateStatusAndInvokeCallback(com.android.server.credentials.ProviderSession.Status.EMPTY_RESPONSE, com.android.server.credentials.ProviderSession.CredentialsSource.REMOTE_PROVIDER);
        } else {
            this.mProviderSessionMetric.collectCandidateEntryMetrics(beginCreateCredentialResponse, false, ((com.android.server.credentials.RequestSession) this.mCallbacks).mRequestSessionMetric.getInitialPhaseMetric());
            updateStatusAndInvokeCallback(com.android.server.credentials.ProviderSession.Status.SAVE_ENTRIES_RECEIVED, com.android.server.credentials.ProviderSession.CredentialsSource.REMOTE_PROVIDER);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.credentials.ProviderSession
    /* JADX INFO: renamed from: prepareUiData, reason: merged with bridge method [inline-methods] */
    public android.credentials.selection.CreateCredentialProviderData mo3091prepareUiData() throws java.lang.IllegalArgumentException {
        if (!com.android.server.credentials.ProviderSession.isUiInvokingStatus(getStatus())) {
            android.util.Slog.i(TAG, "No data for UI from: " + this.mComponentName.flattenToString());
            return null;
        }
        if (this.mProviderResponse == 0 || this.mProviderResponseDataHandler.isEmptyResponse()) {
            return null;
        }
        return this.mProviderResponseDataHandler.toCreateCredentialProviderData();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x001e  */
    @Override // com.android.server.credentials.ProviderSession
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onUiEntrySelected(java.lang.String r3, java.lang.String r4, android.credentials.selection.ProviderPendingIntentResponse r5) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            switch(r0) {
                case -1424551728: goto L13;
                case 1110515801: goto L8;
                default: goto L7;
            }
        L7:
            goto L1e
        L8:
            java.lang.String r0 = "remote_entry_key"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L7
            r0 = 1
            goto L1f
        L13:
            java.lang.String r0 = "save_entry_key"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L7
            r0 = 0
            goto L1f
        L1e:
            r0 = -1
        L1f:
            java.lang.String r1 = "CredentialManager"
            switch(r0) {
                case 0: goto L42;
                case 1: goto L2d;
                default: goto L24;
            }
        L24:
            java.lang.String r0 = "Unsupported entry type selected"
            android.util.Slog.i(r1, r0)
            r2.invokeCallbackOnInternalInvalidState()
            goto L57
        L2d:
            com.android.server.credentials.ProviderCreateSession$ProviderResponseDataHandler r0 = r2.mProviderResponseDataHandler
            android.service.credentials.RemoteEntry r0 = r0.getRemoteEntry(r4)
            if (r0 != 0) goto L3e
            java.lang.String r0 = "Unexpected remote entry key"
            android.util.Slog.i(r1, r0)
            r2.invokeCallbackOnInternalInvalidState()
            return
        L3e:
            r2.onRemoteEntrySelected(r5)
            goto L57
        L42:
            com.android.server.credentials.ProviderCreateSession$ProviderResponseDataHandler r0 = r2.mProviderResponseDataHandler
            android.service.credentials.CreateEntry r0 = r0.getCreateEntry(r4)
            if (r0 != 0) goto L53
            java.lang.String r0 = "Unexpected save entry key"
            android.util.Slog.i(r1, r0)
            r2.invokeCallbackOnInternalInvalidState()
            return
        L53:
            r2.onCreateEntrySelected(r5)
        L57:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.credentials.ProviderCreateSession.onUiEntrySelected(java.lang.String, java.lang.String, android.credentials.selection.ProviderPendingIntentResponse):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.credentials.ProviderSession
    protected void invokeSession() {
        if (this.mRemoteCredentialService != null) {
            startCandidateMetrics();
            this.mRemoteCredentialService.setCallback(this);
            this.mRemoteCredentialService.onBeginCreateCredential((android.service.credentials.BeginCreateCredentialRequest) this.mProviderRequest);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.Intent setUpFillInIntent() {
        android.content.Intent intent = new android.content.Intent();
        intent.putExtra("android.service.credentials.extra.CREATE_CREDENTIAL_REQUEST", this.mCompleteRequest);
        return intent;
    }

    private void onCreateEntrySelected(android.credentials.selection.ProviderPendingIntentResponse pendingIntentResponse) {
        android.credentials.CreateCredentialException exception = maybeGetPendingIntentException(pendingIntentResponse);
        if (exception != null) {
            invokeCallbackWithError(exception.getType(), exception.getMessage());
            return;
        }
        android.credentials.CreateCredentialResponse credentialResponse = com.android.server.credentials.PendingIntentResultHandler.extractCreateCredentialResponse(pendingIntentResponse.getResultData());
        if (credentialResponse != null) {
            this.mCallbacks.onFinalResponseReceived(this.mComponentName, credentialResponse);
        } else {
            android.util.Slog.i(TAG, "onSaveEntrySelected - no response or error found in pending intent response");
            invokeCallbackOnInternalInvalidState();
        }
    }

    private void onRemoteEntrySelected(android.credentials.selection.ProviderPendingIntentResponse pendingIntentResponse) {
        onCreateEntrySelected(pendingIntentResponse);
    }

    private android.credentials.CreateCredentialException maybeGetPendingIntentException(android.credentials.selection.ProviderPendingIntentResponse pendingIntentResponse) {
        if (pendingIntentResponse == null) {
            android.util.Slog.i(TAG, "pendingIntentResponse is null");
            return new android.credentials.CreateCredentialException("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS");
        }
        if (com.android.server.credentials.PendingIntentResultHandler.isValidResponse(pendingIntentResponse)) {
            android.credentials.CreateCredentialException exception = com.android.server.credentials.PendingIntentResultHandler.extractCreateCredentialException(pendingIntentResponse.getResultData());
            if (exception != null) {
                android.util.Slog.i(TAG, "Pending intent contains provider exception");
                return exception;
            }
            return null;
        }
        if (com.android.server.credentials.PendingIntentResultHandler.isCancelledResponse(pendingIntentResponse)) {
            return new android.credentials.CreateCredentialException("android.credentials.CreateCredentialException.TYPE_USER_CANCELED");
        }
        return new android.credentials.CreateCredentialException("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS");
    }

    private void invokeCallbackOnInternalInvalidState() {
        this.mCallbacks.onFinalErrorReceived(this.mComponentName, "android.credentials.CreateCredentialException.TYPE_UNKNOWN", null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ProviderResponseDataHandler {
        private final android.content.ComponentName mExpectedRemoteEntryProviderService;
        private final java.util.Map<java.lang.String, android.util.Pair<android.service.credentials.CreateEntry, android.credentials.selection.Entry>> mUiCreateEntries = new java.util.HashMap();
        private android.util.Pair<java.lang.String, android.util.Pair<android.service.credentials.RemoteEntry, android.credentials.selection.Entry>> mUiRemoteEntry = null;

        ProviderResponseDataHandler(android.content.ComponentName expectedRemoteEntryProviderService) {
            this.mExpectedRemoteEntryProviderService = expectedRemoteEntryProviderService;
        }

        public void addResponseContent(java.util.List<android.service.credentials.CreateEntry> createEntries, android.service.credentials.RemoteEntry remoteEntry) {
            createEntries.forEach(new java.util.function.Consumer() { // from class: com.android.server.credentials.ProviderCreateSession$ProviderResponseDataHandler$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.addCreateEntry((android.service.credentials.CreateEntry) obj);
                }
            });
            if (remoteEntry != null) {
                setRemoteEntry(remoteEntry);
            }
        }

        public void addCreateEntry(android.service.credentials.CreateEntry createEntry) {
            java.lang.String id = com.android.server.credentials.ProviderSession.generateUniqueId();
            android.credentials.selection.Entry entry = new android.credentials.selection.Entry(com.android.server.credentials.ProviderCreateSession.SAVE_ENTRY_KEY, id, createEntry.getSlice(), com.android.server.credentials.ProviderCreateSession.this.setUpFillInIntent());
            this.mUiCreateEntries.put(id, new android.util.Pair<>(createEntry, entry));
        }

        public void setRemoteEntry(android.service.credentials.RemoteEntry remoteEntry) {
            if (!com.android.server.credentials.ProviderCreateSession.this.enforceRemoteEntryRestrictions(this.mExpectedRemoteEntryProviderService)) {
                android.util.Slog.w(com.android.server.credentials.ProviderCreateSession.TAG, "Remote entry being dropped as it does not meet the restrictionchecks.");
            } else {
                if (remoteEntry == null) {
                    this.mUiRemoteEntry = null;
                    return;
                }
                java.lang.String id = com.android.server.credentials.ProviderSession.generateUniqueId();
                android.credentials.selection.Entry entry = new android.credentials.selection.Entry("remote_entry_key", id, remoteEntry.getSlice(), com.android.server.credentials.ProviderCreateSession.this.setUpFillInIntent());
                this.mUiRemoteEntry = new android.util.Pair<>(id, new android.util.Pair(remoteEntry, entry));
            }
        }

        public android.credentials.selection.CreateCredentialProviderData toCreateCredentialProviderData() {
            return new android.credentials.selection.CreateCredentialProviderData.Builder(com.android.server.credentials.ProviderCreateSession.this.mComponentName.flattenToString()).setSaveEntries(prepareUiCreateEntries()).setRemoteEntry(prepareRemoteEntry()).build();
        }

        private java.util.List<android.credentials.selection.Entry> prepareUiCreateEntries() {
            java.util.List<android.credentials.selection.Entry> createEntries = new java.util.ArrayList<>();
            for (java.lang.String key : this.mUiCreateEntries.keySet()) {
                createEntries.add((android.credentials.selection.Entry) this.mUiCreateEntries.get(key).second);
            }
            return createEntries;
        }

        private android.credentials.selection.Entry prepareRemoteEntry() {
            if (this.mUiRemoteEntry == null || this.mUiRemoteEntry.first == null || this.mUiRemoteEntry.second == null) {
                return null;
            }
            return (android.credentials.selection.Entry) ((android.util.Pair) this.mUiRemoteEntry.second).second;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEmptyResponse() {
            return this.mUiCreateEntries.isEmpty() && this.mUiRemoteEntry == null;
        }

        public android.service.credentials.RemoteEntry getRemoteEntry(java.lang.String entryKey) {
            if (this.mUiRemoteEntry == null || this.mUiRemoteEntry.first == null || !((java.lang.String) this.mUiRemoteEntry.first).equals(entryKey) || this.mUiRemoteEntry.second == null) {
                return null;
            }
            return (android.service.credentials.RemoteEntry) ((android.util.Pair) this.mUiRemoteEntry.second).first;
        }

        public android.service.credentials.CreateEntry getCreateEntry(java.lang.String entryKey) {
            if (this.mUiCreateEntries.get(entryKey) == null) {
                return null;
            }
            return (android.service.credentials.CreateEntry) this.mUiCreateEntries.get(entryKey).first;
        }

        public boolean isEmptyResponse(android.service.credentials.BeginCreateCredentialResponse response) {
            return (response.getCreateEntries() == null || response.getCreateEntries().isEmpty()) && response.getRemoteCreateEntry() == null;
        }
    }
}
