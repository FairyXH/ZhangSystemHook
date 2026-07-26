package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public final class ProviderGetSession extends com.android.server.credentials.ProviderSession<android.service.credentials.BeginGetCredentialRequest, android.service.credentials.BeginGetCredentialResponse> implements com.android.server.credentials.RemoteCredentialService.ProviderCallbacks<android.service.credentials.BeginGetCredentialResponse> {
    public static final java.lang.String ACTION_ENTRY_KEY = "action_key";
    public static final java.lang.String AUTHENTICATION_ACTION_ENTRY_KEY = "authentication_action_key";
    public static final java.lang.String CREDENTIAL_ENTRY_KEY = "credential_key";
    public static final java.lang.String REMOTE_ENTRY_KEY = "remote_entry_key";
    private static final java.lang.String TAG = "CredentialManager";
    private final java.util.Map<java.lang.String, android.credentials.CredentialOption> mBeginGetOptionToCredentialOptionMap;
    private final android.service.credentials.CallingAppInfo mCallingAppInfo;
    private final android.credentials.GetCredentialRequest mCompleteRequest;
    private android.credentials.GetCredentialException mProviderException;
    private final com.android.server.credentials.ProviderGetSession.ProviderResponseDataHandler mProviderResponseDataHandler;

    /* JADX WARN: Multi-variable type inference failed */
    public static com.android.server.credentials.ProviderGetSession createNewSession(android.content.Context context, int userId, android.credentials.CredentialProviderInfo providerInfo, com.android.server.credentials.GetRequestSession getRequestSession, com.android.server.credentials.RemoteCredentialService remoteCredentialService) {
        android.credentials.GetCredentialRequest filteredRequest = filterOptions(providerInfo.getCapabilities(), (android.credentials.GetCredentialRequest) getRequestSession.mClientRequest, providerInfo, getRequestSession.mHybridService);
        if (filteredRequest != null) {
            java.util.Map<java.lang.String, android.credentials.CredentialOption> beginGetOptionToCredentialOptionMap = new java.util.HashMap<>();
            return new com.android.server.credentials.ProviderGetSession(context, providerInfo, getRequestSession, userId, remoteCredentialService, constructQueryPhaseRequest(filteredRequest, getRequestSession.mClientAppInfo, ((android.credentials.GetCredentialRequest) getRequestSession.mClientRequest).alwaysSendAppInfoToProvider(), beginGetOptionToCredentialOptionMap), filteredRequest, getRequestSession.mClientAppInfo, beginGetOptionToCredentialOptionMap, getRequestSession.mHybridService);
        }
        android.util.Slog.i(TAG, "Unable to create provider session for: " + providerInfo.getComponentName());
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static com.android.server.credentials.ProviderGetSession createNewSession(android.content.Context context, int userId, android.credentials.CredentialProviderInfo providerInfo, com.android.server.credentials.GetCandidateRequestSession getRequestSession, com.android.server.credentials.RemoteCredentialService remoteCredentialService) {
        android.credentials.GetCredentialRequest filteredRequest = filterOptions(providerInfo.getCapabilities(), (android.credentials.GetCredentialRequest) getRequestSession.mClientRequest, providerInfo, getRequestSession.mHybridService);
        if (filteredRequest != null) {
            java.util.Map<java.lang.String, android.credentials.CredentialOption> beginGetOptionToCredentialOptionMap = new java.util.HashMap<>();
            return new com.android.server.credentials.ProviderGetSession(context, providerInfo, getRequestSession, userId, remoteCredentialService, constructQueryPhaseRequest(filteredRequest, getRequestSession.mClientAppInfo, ((android.credentials.GetCredentialRequest) getRequestSession.mClientRequest).alwaysSendAppInfoToProvider(), beginGetOptionToCredentialOptionMap), filteredRequest, getRequestSession.mClientAppInfo, beginGetOptionToCredentialOptionMap, getRequestSession.mHybridService);
        }
        android.util.Slog.i(TAG, "Unable to create provider session for: " + providerInfo.getComponentName());
        return null;
    }

    private static android.service.credentials.BeginGetCredentialRequest constructQueryPhaseRequest(android.credentials.GetCredentialRequest filteredRequest, android.service.credentials.CallingAppInfo callingAppInfo, boolean propagateToProvider, final java.util.Map<java.lang.String, android.credentials.CredentialOption> beginGetOptionToCredentialOptionMap) {
        final android.service.credentials.BeginGetCredentialRequest.Builder builder = new android.service.credentials.BeginGetCredentialRequest.Builder();
        filteredRequest.getCredentialOptions().forEach(new java.util.function.Consumer() { // from class: com.android.server.credentials.ProviderGetSession$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.credentials.ProviderGetSession.lambda$constructQueryPhaseRequest$0(builder, beginGetOptionToCredentialOptionMap, (android.credentials.CredentialOption) obj);
            }
        });
        if (propagateToProvider) {
            builder.setCallingAppInfo(callingAppInfo);
        }
        return builder.build();
    }

    static /* synthetic */ void lambda$constructQueryPhaseRequest$0(android.service.credentials.BeginGetCredentialRequest.Builder builder, java.util.Map beginGetOptionToCredentialOptionMap, android.credentials.CredentialOption option) {
        java.lang.String id = generateUniqueId();
        builder.addBeginGetCredentialOption(new android.service.credentials.BeginGetCredentialOption(id, option.getType(), option.getCandidateQueryData()));
        beginGetOptionToCredentialOptionMap.put(id, option);
    }

    private static android.credentials.GetCredentialRequest filterOptions(java.util.List<java.lang.String> providerCapabilities, android.credentials.GetCredentialRequest clientRequest, android.credentials.CredentialProviderInfo info, java.lang.String hybridService) {
        android.util.Slog.i(TAG, "Filtering request options for: " + info.getComponentName());
        if (com.android.internal.hidden_from_bootclasspath.android.credentials.flags.Flags.hybridFilterOptFixEnabled()) {
            android.content.ComponentName hybridComponentName = android.content.ComponentName.unflattenFromString(hybridService);
            if (hybridComponentName != null && hybridComponentName.equals(info.getComponentName())) {
                android.util.Slog.i(TAG, "Skipping filtering of options for hybrid service");
                return clientRequest;
            }
            android.util.Slog.w(TAG, "Could not parse hybrid service while filtering options");
        }
        java.util.List<android.credentials.CredentialOption> filteredOptions = new java.util.ArrayList<>();
        for (android.credentials.CredentialOption option : clientRequest.getCredentialOptions()) {
            if (providerCapabilities.contains(option.getType()) && isProviderAllowed(option, info) && checkSystemProviderRequirement(option, info.isSystemProvider())) {
                android.util.Slog.i(TAG, "Option of type: " + option.getType() + " meets all filteringconditions");
                filteredOptions.add(option);
            }
        }
        if (!filteredOptions.isEmpty()) {
            return new android.credentials.GetCredentialRequest.Builder(clientRequest.getData()).setCredentialOptions(filteredOptions).build();
        }
        android.util.Slog.i(TAG, "No options filtered");
        return null;
    }

    private static boolean isProviderAllowed(android.credentials.CredentialOption option, android.credentials.CredentialProviderInfo providerInfo) {
        if (providerInfo.isSystemProvider() || option.getAllowedProviders().isEmpty() || option.getAllowedProviders().contains(providerInfo.getComponentName())) {
            return true;
        }
        android.util.Slog.i(TAG, "Provider allow list specified but does not contain this provider");
        return false;
    }

    private static boolean checkSystemProviderRequirement(android.credentials.CredentialOption option, boolean isSystemProvider) {
        if (option.isSystemProviderRequired() && !isSystemProvider) {
            android.util.Slog.i(TAG, "System provider required, but this service is not a system provider");
            return false;
        }
        return true;
    }

    public ProviderGetSession(android.content.Context context, android.credentials.CredentialProviderInfo info, com.android.server.credentials.ProviderSession.ProviderInternalCallback callbacks, int userId, com.android.server.credentials.RemoteCredentialService remoteCredentialService, android.service.credentials.BeginGetCredentialRequest beginGetRequest, android.credentials.GetCredentialRequest completeGetRequest, android.service.credentials.CallingAppInfo callingAppInfo, java.util.Map<java.lang.String, android.credentials.CredentialOption> beginGetOptionToCredentialOptionMap, java.lang.String hybridService) {
        super(context, beginGetRequest, callbacks, info.getComponentName(), userId, remoteCredentialService);
        this.mCompleteRequest = completeGetRequest;
        this.mCallingAppInfo = callingAppInfo;
        setStatus(com.android.server.credentials.ProviderSession.Status.PENDING);
        this.mBeginGetOptionToCredentialOptionMap = new java.util.HashMap(beginGetOptionToCredentialOptionMap);
        this.mProviderResponseDataHandler = new com.android.server.credentials.ProviderGetSession.ProviderResponseDataHandler(android.content.ComponentName.unflattenFromString(hybridService));
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderResponseSuccess(android.service.credentials.BeginGetCredentialResponse response) {
        android.util.Slog.i(TAG, "Remote provider responded with a valid response: " + this.mComponentName);
        onSetInitialRemoteResponse(response);
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderResponseFailure(int errorCode, java.lang.Exception exception) {
        if (exception instanceof android.credentials.GetCredentialException) {
            this.mProviderException = (android.credentials.GetCredentialException) exception;
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

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0054  */
    @Override // com.android.server.credentials.ProviderSession
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onUiEntrySelected(java.lang.String r5, java.lang.String r6, android.credentials.selection.ProviderPendingIntentResponse r7) {
        /*
            Method dump skipped, instruction units count: 268
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.credentials.ProviderGetSession.onUiEntrySelected(java.lang.String, java.lang.String, android.credentials.selection.ProviderPendingIntentResponse):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.credentials.ProviderSession
    protected void invokeSession() {
        if (this.mRemoteCredentialService != null) {
            startCandidateMetrics();
            this.mRemoteCredentialService.setCallback(this);
            this.mRemoteCredentialService.onBeginGetCredential((android.service.credentials.BeginGetCredentialRequest) this.mProviderRequest);
        }
    }

    protected java.util.Set<java.lang.String> getCredentialEntryTypes() {
        return this.mProviderResponseDataHandler.getCredentialEntryTypes();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.credentials.ProviderSession
    /* JADX INFO: renamed from: prepareUiData, reason: merged with bridge method [inline-methods] */
    public android.credentials.selection.GetCredentialProviderData mo3091prepareUiData() throws java.lang.IllegalArgumentException {
        if (!com.android.server.credentials.ProviderSession.isUiInvokingStatus(getStatus())) {
            android.util.Slog.i(TAG, "No data for UI from: " + this.mComponentName.flattenToString());
            return null;
        }
        if (this.mProviderResponse == 0 || this.mProviderResponseDataHandler.isEmptyResponse()) {
            return null;
        }
        return this.mProviderResponseDataHandler.toGetCredentialProviderData();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.Intent setUpFillInIntentWithFinalRequest(java.lang.String id) {
        android.content.Intent intent = new android.content.Intent();
        android.credentials.CredentialOption credentialOption = this.mBeginGetOptionToCredentialOptionMap.get(id);
        if (credentialOption == null) {
            android.util.Slog.w(TAG, "Id from Credential Entry does not resolve to a valid option");
            return intent;
        }
        return intent.putExtra("android.service.credentials.extra.GET_CREDENTIAL_REQUEST", new android.service.credentials.GetCredentialRequest(this.mCallingAppInfo, java.util.List.of(credentialOption)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.Intent setUpFillInIntentWithQueryRequest() {
        android.content.Intent intent = new android.content.Intent();
        intent.putExtra("android.service.credentials.extra.BEGIN_GET_CREDENTIAL_REQUEST", (android.os.Parcelable) this.mProviderRequest);
        return intent;
    }

    private void onRemoteEntrySelected(android.credentials.selection.ProviderPendingIntentResponse providerPendingIntentResponse) {
        onCredentialEntrySelected(providerPendingIntentResponse);
    }

    private void onCredentialEntrySelected(android.credentials.selection.ProviderPendingIntentResponse providerPendingIntentResponse) {
        if (providerPendingIntentResponse == null) {
            invokeCallbackOnInternalInvalidState();
            return;
        }
        android.credentials.GetCredentialException exception = maybeGetPendingIntentException(providerPendingIntentResponse);
        if (exception != null) {
            invokeCallbackWithError(exception.getType(), exception.getMessage());
            return;
        }
        android.credentials.GetCredentialResponse getCredentialResponse = com.android.server.credentials.PendingIntentResultHandler.extractGetCredentialResponse(providerPendingIntentResponse.getResultData());
        if (getCredentialResponse != null) {
            this.mCallbacks.onFinalResponseReceived(this.mComponentName, getCredentialResponse);
        } else {
            android.util.Slog.i(TAG, "Pending intent response contains no credential, or error for a credential entry");
            invokeCallbackOnInternalInvalidState();
        }
    }

    private android.credentials.GetCredentialException maybeGetPendingIntentException(android.credentials.selection.ProviderPendingIntentResponse pendingIntentResponse) {
        if (pendingIntentResponse == null) {
            return null;
        }
        if (com.android.server.credentials.PendingIntentResultHandler.isValidResponse(pendingIntentResponse)) {
            android.credentials.GetCredentialException exception = com.android.server.credentials.PendingIntentResultHandler.extractGetCredentialException(pendingIntentResponse.getResultData());
            if (exception == null) {
                return null;
            }
            return exception;
        }
        if (com.android.server.credentials.PendingIntentResultHandler.isCancelledResponse(pendingIntentResponse)) {
            return new android.credentials.GetCredentialException("android.credentials.GetCredentialException.TYPE_USER_CANCELED");
        }
        return new android.credentials.GetCredentialException("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL");
    }

    private boolean onAuthenticationEntrySelected(android.credentials.selection.ProviderPendingIntentResponse providerPendingIntentResponse) {
        if (providerPendingIntentResponse == null) {
            return false;
        }
        android.credentials.GetCredentialException exception = maybeGetPendingIntentException(providerPendingIntentResponse);
        if (exception != null) {
            this.mProviderSessionMetric.collectAuthenticationExceptionStatus(true);
            invokeCallbackWithError(exception.getType(), exception.getMessage());
            return true;
        }
        android.service.credentials.BeginGetCredentialResponse response = com.android.server.credentials.PendingIntentResultHandler.extractResponseContent(providerPendingIntentResponse.getResultData());
        this.mProviderSessionMetric.collectCandidateEntryMetrics(response, true, null);
        if (response == null || this.mProviderResponseDataHandler.isEmptyResponse(response)) {
            return false;
        }
        addToInitialRemoteResponse(response, false);
        return true;
    }

    private void addToInitialRemoteResponse(android.service.credentials.BeginGetCredentialResponse content, boolean isInitialResponse) {
        if (content == null) {
            return;
        }
        this.mProviderResponseDataHandler.addResponseContent(content.getCredentialEntries(), content.getActions(), content.getAuthenticationActions(), content.getRemoteCredentialEntry(), isInitialResponse);
    }

    private void onActionEntrySelected(android.credentials.selection.ProviderPendingIntentResponse providerPendingIntentResponse) {
        android.util.Slog.i(TAG, "onActionEntrySelected");
        onCredentialEntrySelected(providerPendingIntentResponse);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onSetInitialRemoteResponse(android.service.credentials.BeginGetCredentialResponse beginGetCredentialResponse) {
        this.mProviderResponse = beginGetCredentialResponse;
        addToInitialRemoteResponse(beginGetCredentialResponse, true);
        if (this.mProviderResponseDataHandler.isEmptyResponse(beginGetCredentialResponse)) {
            this.mProviderSessionMetric.collectCandidateEntryMetrics(beginGetCredentialResponse, false, null);
            updateStatusAndInvokeCallback(com.android.server.credentials.ProviderSession.Status.EMPTY_RESPONSE, com.android.server.credentials.ProviderSession.CredentialsSource.REMOTE_PROVIDER);
        } else {
            this.mProviderSessionMetric.collectCandidateEntryMetrics(beginGetCredentialResponse, false, null);
            updateStatusAndInvokeCallback(com.android.server.credentials.ProviderSession.Status.CREDENTIALS_RECEIVED, com.android.server.credentials.ProviderSession.CredentialsSource.REMOTE_PROVIDER);
        }
    }

    private void invokeCallbackOnInternalInvalidState() {
        this.mCallbacks.onFinalErrorReceived(this.mComponentName, "android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL", null);
    }

    public void updateAuthEntriesStatusFromAnotherSession() {
        this.mProviderResponseDataHandler.updateAuthEntryWithNoCredentialsReceived(null);
    }

    public boolean containsEmptyAuthEntriesOnly() {
        return this.mProviderResponseDataHandler.mUiCredentialEntries.isEmpty() && this.mProviderResponseDataHandler.mUiRemoteEntry == null && this.mProviderResponseDataHandler.mUiAuthenticationEntries.values().stream().allMatch(new java.util.function.Predicate() { // from class: com.android.server.credentials.ProviderGetSession$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.credentials.ProviderGetSession.lambda$containsEmptyAuthEntriesOnly$1((android.util.Pair) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$containsEmptyAuthEntriesOnly$1(android.util.Pair e) {
        return ((android.credentials.selection.AuthenticationEntry) e.second).getStatus() == 1 || ((android.credentials.selection.AuthenticationEntry) e.second).getStatus() == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ProviderResponseDataHandler {
        private final android.content.ComponentName mExpectedRemoteEntryProviderService;
        private final java.util.Map<java.lang.String, android.util.Pair<android.service.credentials.CredentialEntry, android.credentials.selection.Entry>> mUiCredentialEntries = new java.util.HashMap();
        private final java.util.Map<java.lang.String, android.util.Pair<android.service.credentials.Action, android.credentials.selection.Entry>> mUiActionsEntries = new java.util.HashMap();
        private final java.util.Map<java.lang.String, android.util.Pair<android.service.credentials.Action, android.credentials.selection.AuthenticationEntry>> mUiAuthenticationEntries = new java.util.HashMap();
        private final java.util.Set<java.lang.String> mCredentialEntryTypes = new java.util.HashSet();
        private android.util.Pair<java.lang.String, android.util.Pair<android.service.credentials.RemoteEntry, android.credentials.selection.Entry>> mUiRemoteEntry = null;

        ProviderResponseDataHandler(android.content.ComponentName expectedRemoteEntryProviderService) {
            this.mExpectedRemoteEntryProviderService = expectedRemoteEntryProviderService;
        }

        public void addResponseContent(java.util.List<android.service.credentials.CredentialEntry> credentialEntries, java.util.List<android.service.credentials.Action> actions, java.util.List<android.service.credentials.Action> authenticationActions, android.service.credentials.RemoteEntry remoteEntry, boolean isInitialResponse) {
            credentialEntries.forEach(new java.util.function.Consumer() { // from class: com.android.server.credentials.ProviderGetSession$ProviderResponseDataHandler$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.addCredentialEntry((android.service.credentials.CredentialEntry) obj);
                }
            });
            actions.forEach(new java.util.function.Consumer() { // from class: com.android.server.credentials.ProviderGetSession$ProviderResponseDataHandler$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.addAction((android.service.credentials.Action) obj);
                }
            });
            authenticationActions.forEach(new java.util.function.Consumer() { // from class: com.android.server.credentials.ProviderGetSession$ProviderResponseDataHandler$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$addResponseContent$0((android.service.credentials.Action) obj);
                }
            });
            if (remoteEntry != null || !isInitialResponse) {
                setRemoteEntry(remoteEntry);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addResponseContent$0(android.service.credentials.Action authenticationAction) {
            addAuthenticationAction(authenticationAction, 0);
        }

        public void addCredentialEntry(android.service.credentials.CredentialEntry credentialEntry) {
            java.lang.String id = com.android.server.credentials.ProviderSession.generateUniqueId();
            android.credentials.selection.Entry entry = new android.credentials.selection.Entry(com.android.server.credentials.ProviderGetSession.CREDENTIAL_ENTRY_KEY, id, credentialEntry.getSlice(), com.android.server.credentials.ProviderGetSession.this.setUpFillInIntentWithFinalRequest(credentialEntry.getBeginGetCredentialOptionId()));
            this.mUiCredentialEntries.put(id, new android.util.Pair<>(credentialEntry, entry));
            this.mCredentialEntryTypes.add(credentialEntry.getType());
        }

        public void addAction(android.service.credentials.Action action) {
            java.lang.String id = com.android.server.credentials.ProviderSession.generateUniqueId();
            android.credentials.selection.Entry entry = new android.credentials.selection.Entry(com.android.server.credentials.ProviderGetSession.ACTION_ENTRY_KEY, id, action.getSlice(), com.android.server.credentials.ProviderGetSession.this.setUpFillInIntentWithQueryRequest());
            this.mUiActionsEntries.put(id, new android.util.Pair<>(action, entry));
        }

        public void addAuthenticationAction(android.service.credentials.Action authenticationAction, int status) {
            java.lang.String id = com.android.server.credentials.ProviderSession.generateUniqueId();
            android.credentials.selection.AuthenticationEntry entry = new android.credentials.selection.AuthenticationEntry(com.android.server.credentials.ProviderGetSession.AUTHENTICATION_ACTION_ENTRY_KEY, id, authenticationAction.getSlice(), status, com.android.server.credentials.ProviderGetSession.this.setUpFillInIntentWithQueryRequest());
            this.mUiAuthenticationEntries.put(id, new android.util.Pair<>(authenticationAction, entry));
        }

        public void removeAuthenticationAction(java.lang.String id) {
            this.mUiAuthenticationEntries.remove(id);
        }

        public void setRemoteEntry(android.service.credentials.RemoteEntry remoteEntry) {
            if (!com.android.server.credentials.ProviderGetSession.this.enforceRemoteEntryRestrictions(this.mExpectedRemoteEntryProviderService)) {
                android.util.Slog.w(com.android.server.credentials.ProviderGetSession.TAG, "Remote entry being dropped as it does not meet the restriction checks.");
            } else {
                if (remoteEntry == null) {
                    this.mUiRemoteEntry = null;
                    return;
                }
                java.lang.String id = com.android.server.credentials.ProviderSession.generateUniqueId();
                android.credentials.selection.Entry entry = new android.credentials.selection.Entry(com.android.server.credentials.ProviderGetSession.REMOTE_ENTRY_KEY, id, remoteEntry.getSlice(), com.android.server.credentials.ProviderGetSession.this.setUpFillInIntentForRemoteEntry());
                this.mUiRemoteEntry = new android.util.Pair<>(id, new android.util.Pair(remoteEntry, entry));
            }
        }

        public android.credentials.selection.GetCredentialProviderData toGetCredentialProviderData() {
            return new android.credentials.selection.GetCredentialProviderData.Builder(com.android.server.credentials.ProviderGetSession.this.mComponentName.flattenToString()).setActionChips(prepareActionEntries()).setCredentialEntries(prepareCredentialEntries()).setAuthenticationEntries(prepareAuthenticationEntries()).setRemoteEntry(prepareRemoteEntry()).build();
        }

        private java.util.List<android.credentials.selection.Entry> prepareActionEntries() {
            java.util.List<android.credentials.selection.Entry> actionEntries = new java.util.ArrayList<>();
            for (java.lang.String key : this.mUiActionsEntries.keySet()) {
                actionEntries.add((android.credentials.selection.Entry) this.mUiActionsEntries.get(key).second);
            }
            return actionEntries;
        }

        private java.util.List<android.credentials.selection.AuthenticationEntry> prepareAuthenticationEntries() {
            java.util.List<android.credentials.selection.AuthenticationEntry> authEntries = new java.util.ArrayList<>();
            for (java.lang.String key : this.mUiAuthenticationEntries.keySet()) {
                authEntries.add((android.credentials.selection.AuthenticationEntry) this.mUiAuthenticationEntries.get(key).second);
            }
            return authEntries;
        }

        private java.util.List<android.credentials.selection.Entry> prepareCredentialEntries() {
            java.util.List<android.credentials.selection.Entry> credEntries = new java.util.ArrayList<>();
            for (java.lang.String key : this.mUiCredentialEntries.keySet()) {
                credEntries.add((android.credentials.selection.Entry) this.mUiCredentialEntries.get(key).second);
            }
            return credEntries;
        }

        private android.credentials.selection.Entry prepareRemoteEntry() {
            if (this.mUiRemoteEntry == null || this.mUiRemoteEntry.first == null || this.mUiRemoteEntry.second == null) {
                return null;
            }
            return (android.credentials.selection.Entry) ((android.util.Pair) this.mUiRemoteEntry.second).second;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEmptyResponse() {
            return this.mUiCredentialEntries.isEmpty() && this.mUiActionsEntries.isEmpty() && this.mUiAuthenticationEntries.isEmpty() && this.mUiRemoteEntry == null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEmptyResponse(android.service.credentials.BeginGetCredentialResponse response) {
            return response.getCredentialEntries().isEmpty() && response.getActions().isEmpty() && response.getAuthenticationActions().isEmpty() && response.getRemoteCredentialEntry() == null;
        }

        public java.util.Set<java.lang.String> getCredentialEntryTypes() {
            return this.mCredentialEntryTypes;
        }

        public android.service.credentials.Action getAuthenticationAction(java.lang.String entryKey) {
            if (this.mUiAuthenticationEntries.get(entryKey) == null) {
                return null;
            }
            return (android.service.credentials.Action) this.mUiAuthenticationEntries.get(entryKey).first;
        }

        public android.service.credentials.Action getActionEntry(java.lang.String entryKey) {
            if (this.mUiActionsEntries.get(entryKey) == null) {
                return null;
            }
            return (android.service.credentials.Action) this.mUiActionsEntries.get(entryKey).first;
        }

        public android.service.credentials.RemoteEntry getRemoteEntry(java.lang.String entryKey) {
            if (!((java.lang.String) this.mUiRemoteEntry.first).equals(entryKey) || this.mUiRemoteEntry.second == null) {
                return null;
            }
            return (android.service.credentials.RemoteEntry) ((android.util.Pair) this.mUiRemoteEntry.second).first;
        }

        public android.service.credentials.CredentialEntry getCredentialEntry(java.lang.String entryKey) {
            if (this.mUiCredentialEntries.get(entryKey) == null) {
                return null;
            }
            return (android.service.credentials.CredentialEntry) this.mUiCredentialEntries.get(entryKey).first;
        }

        public void updateAuthEntryWithNoCredentialsReceived(java.lang.String entryKey) {
            if (entryKey == null) {
                updatePreviousMostRecentAuthEntry();
            } else {
                updatePreviousMostRecentAuthEntry();
                updateMostRecentAuthEntry(entryKey);
            }
        }

        private void updateMostRecentAuthEntry(java.lang.String entryKey) {
            android.credentials.selection.AuthenticationEntry previousAuthenticationEntry = (android.credentials.selection.AuthenticationEntry) this.mUiAuthenticationEntries.get(entryKey).second;
            android.service.credentials.Action previousAuthenticationAction = (android.service.credentials.Action) this.mUiAuthenticationEntries.get(entryKey).first;
            this.mUiAuthenticationEntries.put(entryKey, new android.util.Pair<>(previousAuthenticationAction, copyAuthEntryAndChangeStatus(previousAuthenticationEntry, 2)));
        }

        private void updatePreviousMostRecentAuthEntry() {
            java.util.Optional<java.util.Map.Entry<java.lang.String, android.util.Pair<android.service.credentials.Action, android.credentials.selection.AuthenticationEntry>>> previousMostRecentAuthEntry = this.mUiAuthenticationEntries.entrySet().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.credentials.ProviderGetSession$ProviderResponseDataHandler$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.credentials.ProviderGetSession.ProviderResponseDataHandler.lambda$updatePreviousMostRecentAuthEntry$1((java.util.Map.Entry) obj);
                }
            }).findFirst();
            if (previousMostRecentAuthEntry.isEmpty()) {
                return;
            }
            java.lang.String id = previousMostRecentAuthEntry.get().getKey();
            this.mUiAuthenticationEntries.remove(id);
            this.mUiAuthenticationEntries.put(id, new android.util.Pair<>((android.service.credentials.Action) previousMostRecentAuthEntry.get().getValue().first, copyAuthEntryAndChangeStatus((android.credentials.selection.AuthenticationEntry) previousMostRecentAuthEntry.get().getValue().second, 1)));
        }

        static /* synthetic */ boolean lambda$updatePreviousMostRecentAuthEntry$1(java.util.Map.Entry e) {
            return ((android.credentials.selection.AuthenticationEntry) ((android.util.Pair) e.getValue()).second).getStatus() == 2;
        }

        private android.credentials.selection.AuthenticationEntry copyAuthEntryAndChangeStatus(android.credentials.selection.AuthenticationEntry from, java.lang.Integer toStatus) {
            return new android.credentials.selection.AuthenticationEntry(com.android.server.credentials.ProviderGetSession.AUTHENTICATION_ACTION_ENTRY_KEY, from.getSubkey(), from.getSlice(), toStatus.intValue(), from.getFrameworkExtrasIntent());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.Intent setUpFillInIntentForRemoteEntry() {
        return new android.content.Intent().putExtra("android.service.credentials.extra.GET_CREDENTIAL_REQUEST", new android.service.credentials.GetCredentialRequest(this.mCallingAppInfo, this.mCompleteRequest.getCredentialOptions()));
    }
}
