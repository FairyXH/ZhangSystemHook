package com.android.server.credentials.metrics;

/* JADX INFO: loaded from: classes.dex */
public class ProviderSessionMetric {
    private static final java.lang.String TAG = "ProviderSessionMetric";
    protected final java.util.List<com.android.server.credentials.metrics.BrowsedAuthenticationMetric> mBrowsedAuthenticationMetric = new java.util.ArrayList();
    protected final com.android.server.credentials.metrics.CandidatePhaseMetric mCandidatePhasePerProviderMetric;

    public ProviderSessionMetric(int sessionIdTrackTwo) {
        this.mCandidatePhasePerProviderMetric = new com.android.server.credentials.metrics.CandidatePhaseMetric(sessionIdTrackTwo);
        this.mBrowsedAuthenticationMetric.add(new com.android.server.credentials.metrics.BrowsedAuthenticationMetric(sessionIdTrackTwo));
    }

    public com.android.server.credentials.metrics.CandidatePhaseMetric getCandidatePhasePerProviderMetric() {
        return this.mCandidatePhasePerProviderMetric;
    }

    public java.util.List<com.android.server.credentials.metrics.BrowsedAuthenticationMetric> getBrowsedAuthenticationMetric() {
        return this.mBrowsedAuthenticationMetric;
    }

    public void collectCandidateExceptionStatus(boolean hasException) {
        try {
            this.mCandidatePhasePerProviderMetric.setHasException(hasException);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Error while setting candidate metric exception " + e);
        }
    }

    public void collectAuthenticationExceptionStatus(boolean hasException) {
        try {
            com.android.server.credentials.metrics.BrowsedAuthenticationMetric mostRecentAuthenticationMetric = getUsedAuthenticationMetric();
            mostRecentAuthenticationMetric.setHasException(hasException);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Error while setting authentication metric exception " + e);
        }
    }

    public void collectCandidateFrameworkException(java.lang.String exceptionType) {
        try {
            this.mCandidatePhasePerProviderMetric.setFrameworkException(exceptionType);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error during candidate exception metric logging: " + e);
        }
    }

    private void collectAuthEntryUpdate(boolean isFailureStatus, boolean isCompletionStatus, int providerSessionUid) {
        com.android.server.credentials.metrics.BrowsedAuthenticationMetric mostRecentAuthenticationMetric = getUsedAuthenticationMetric();
        mostRecentAuthenticationMetric.setProviderUid(providerSessionUid);
        if (isFailureStatus) {
            mostRecentAuthenticationMetric.setAuthReturned(false);
            mostRecentAuthenticationMetric.setProviderStatus(com.android.server.credentials.metrics.ProviderStatusForMetrics.QUERY_FAILURE.getMetricCode());
        } else if (isCompletionStatus) {
            mostRecentAuthenticationMetric.setAuthReturned(true);
            mostRecentAuthenticationMetric.setProviderStatus(com.android.server.credentials.metrics.ProviderStatusForMetrics.QUERY_SUCCESS.getMetricCode());
        }
    }

    private com.android.server.credentials.metrics.BrowsedAuthenticationMetric getUsedAuthenticationMetric() {
        return this.mBrowsedAuthenticationMetric.get(this.mBrowsedAuthenticationMetric.size() - 1);
    }

    public void collectCandidateMetricUpdate(boolean isFailureStatus, boolean isCompletionStatus, int providerSessionUid, boolean isAuthEntry, boolean isPrimary) {
        try {
            if (isAuthEntry) {
                collectAuthEntryUpdate(isFailureStatus, isCompletionStatus, providerSessionUid);
                return;
            }
            this.mCandidatePhasePerProviderMetric.setPrimary(isPrimary);
            this.mCandidatePhasePerProviderMetric.setCandidateUid(providerSessionUid);
            this.mCandidatePhasePerProviderMetric.setQueryFinishTimeNanoseconds(java.lang.System.nanoTime());
            if (isFailureStatus) {
                this.mCandidatePhasePerProviderMetric.setQueryReturned(false);
                this.mCandidatePhasePerProviderMetric.setProviderQueryStatus(com.android.server.credentials.metrics.ProviderStatusForMetrics.QUERY_FAILURE.getMetricCode());
            } else if (isCompletionStatus) {
                this.mCandidatePhasePerProviderMetric.setQueryReturned(true);
                this.mCandidatePhasePerProviderMetric.setProviderQueryStatus(com.android.server.credentials.metrics.ProviderStatusForMetrics.QUERY_SUCCESS.getMetricCode());
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error during candidate update metric logging: " + e);
        }
    }

    public void collectCandidateMetricSetupViaInitialMetric(com.android.server.credentials.metrics.InitialPhaseMetric initMetric) {
        try {
            this.mCandidatePhasePerProviderMetric.setServiceBeganTimeNanoseconds(initMetric.getCredentialServiceStartedTimeNanoseconds());
            this.mCandidatePhasePerProviderMetric.setStartQueryTimeNanoseconds(java.lang.System.nanoTime());
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error during candidate setup metric logging: " + e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <R> void collectCandidateEntryMetrics(R r, boolean isAuthEntry, com.android.server.credentials.metrics.InitialPhaseMetric initialPhaseMetric) {
        try {
            if (r instanceof android.service.credentials.BeginGetCredentialResponse) {
                beginGetCredentialResponseCollectionCandidateEntryMetrics((android.service.credentials.BeginGetCredentialResponse) r, isAuthEntry);
            } else if (!(r instanceof android.service.credentials.BeginCreateCredentialResponse)) {
                android.util.Slog.i(TAG, "Your response type is unsupported for candidate metric logging");
            } else {
                beginCreateCredentialResponseCollectionCandidateEntryMetrics((android.service.credentials.BeginCreateCredentialResponse) r, initialPhaseMetric);
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error during candidate entry metric logging: " + e);
        }
    }

    public void collectCandidateEntryMetrics(java.util.List<android.service.credentials.CredentialEntry> entries) {
        int numCredEntries = entries.size();
        java.util.Map<com.android.server.credentials.metrics.EntryEnum, java.lang.Integer> entryCounts = new java.util.LinkedHashMap<>();
        final java.util.Map<java.lang.String, java.lang.Integer> responseCounts = new java.util.LinkedHashMap<>();
        entryCounts.put(com.android.server.credentials.metrics.EntryEnum.REMOTE_ENTRY, 0);
        entryCounts.put(com.android.server.credentials.metrics.EntryEnum.CREDENTIAL_ENTRY, java.lang.Integer.valueOf(numCredEntries));
        entryCounts.put(com.android.server.credentials.metrics.EntryEnum.ACTION_ENTRY, 0);
        entryCounts.put(com.android.server.credentials.metrics.EntryEnum.AUTHENTICATION_ENTRY, 0);
        entries.forEach(new java.util.function.Consumer() { // from class: com.android.server.credentials.metrics.ProviderSessionMetric$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.credentials.metrics.ProviderSessionMetric.lambda$collectCandidateEntryMetrics$0(responseCounts, (android.service.credentials.CredentialEntry) obj);
            }
        });
        com.android.server.credentials.metrics.shared.ResponseCollective responseCollective = new com.android.server.credentials.metrics.shared.ResponseCollective(responseCounts, entryCounts);
        this.mCandidatePhasePerProviderMetric.setResponseCollective(responseCollective);
    }

    static /* synthetic */ void lambda$collectCandidateEntryMetrics$0(java.util.Map responseCounts, android.service.credentials.CredentialEntry entry) {
        java.lang.String entryKey = com.android.server.credentials.MetricUtilities.generateMetricKey(entry.getType(), 20);
        responseCounts.put(entryKey, java.lang.Integer.valueOf(((java.lang.Integer) responseCounts.getOrDefault(entryKey, 0)).intValue() + 1));
    }

    public void createAuthenticationBrowsingMetric() {
        com.android.server.credentials.metrics.BrowsedAuthenticationMetric browsedAuthenticationMetric = new com.android.server.credentials.metrics.BrowsedAuthenticationMetric(this.mCandidatePhasePerProviderMetric.getSessionIdProvider());
        this.mBrowsedAuthenticationMetric.add(browsedAuthenticationMetric);
    }

    private void beginCreateCredentialResponseCollectionCandidateEntryMetrics(android.service.credentials.BeginCreateCredentialResponse response, com.android.server.credentials.metrics.InitialPhaseMetric initialPhaseMetric) {
        java.util.Map<com.android.server.credentials.metrics.EntryEnum, java.lang.Integer> entryCounts = new java.util.LinkedHashMap<>();
        java.util.List<android.service.credentials.CreateEntry> createEntries = response.getCreateEntries();
        int numRemoteEntry = response.getRemoteCreateEntry() == null ? 0 : 1;
        int numCreateEntries = createEntries.size();
        entryCounts.put(com.android.server.credentials.metrics.EntryEnum.REMOTE_ENTRY, java.lang.Integer.valueOf(numRemoteEntry));
        entryCounts.put(com.android.server.credentials.metrics.EntryEnum.CREDENTIAL_ENTRY, java.lang.Integer.valueOf(numCreateEntries));
        java.util.Map<java.lang.String, java.lang.Integer> responseCounts = new java.util.LinkedHashMap<>();
        java.lang.String[] requestStrings = initialPhaseMetric == null ? new java.lang.String[0] : initialPhaseMetric.getUniqueRequestStrings();
        if (requestStrings.length > 0) {
            responseCounts.put(requestStrings[0], java.lang.Integer.valueOf(initialPhaseMetric.getUniqueRequestCounts()[0]));
        }
        com.android.server.credentials.metrics.shared.ResponseCollective responseCollective = new com.android.server.credentials.metrics.shared.ResponseCollective(responseCounts, entryCounts);
        this.mCandidatePhasePerProviderMetric.setResponseCollective(responseCollective);
    }

    private void beginGetCredentialResponseCollectionCandidateEntryMetrics(android.service.credentials.BeginGetCredentialResponse response, boolean isAuthEntry) {
        java.util.Map<com.android.server.credentials.metrics.EntryEnum, java.lang.Integer> entryCounts = new java.util.LinkedHashMap<>();
        final java.util.Map<java.lang.String, java.lang.Integer> responseCounts = new java.util.LinkedHashMap<>();
        int numCredEntries = response.getCredentialEntries().size();
        int numActionEntries = response.getActions().size();
        int numAuthEntries = response.getAuthenticationActions().size();
        int numRemoteEntry = response.getRemoteCredentialEntry() != null ? 0 : 1;
        entryCounts.put(com.android.server.credentials.metrics.EntryEnum.REMOTE_ENTRY, java.lang.Integer.valueOf(numRemoteEntry));
        entryCounts.put(com.android.server.credentials.metrics.EntryEnum.CREDENTIAL_ENTRY, java.lang.Integer.valueOf(numCredEntries));
        entryCounts.put(com.android.server.credentials.metrics.EntryEnum.ACTION_ENTRY, java.lang.Integer.valueOf(numActionEntries));
        entryCounts.put(com.android.server.credentials.metrics.EntryEnum.AUTHENTICATION_ENTRY, java.lang.Integer.valueOf(numAuthEntries));
        response.getCredentialEntries().forEach(new java.util.function.Consumer() { // from class: com.android.server.credentials.metrics.ProviderSessionMetric$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.credentials.metrics.ProviderSessionMetric.lambda$beginGetCredentialResponseCollectionCandidateEntryMetrics$1(responseCounts, (android.service.credentials.CredentialEntry) obj);
            }
        });
        com.android.server.credentials.metrics.shared.ResponseCollective responseCollective = new com.android.server.credentials.metrics.shared.ResponseCollective(responseCounts, entryCounts);
        if (!isAuthEntry) {
            this.mCandidatePhasePerProviderMetric.setResponseCollective(responseCollective);
        } else {
            com.android.server.credentials.metrics.BrowsedAuthenticationMetric browsedAuthenticationMetric = this.mBrowsedAuthenticationMetric.get(this.mBrowsedAuthenticationMetric.size() - 1);
            browsedAuthenticationMetric.setAuthEntryCollective(responseCollective);
        }
    }

    static /* synthetic */ void lambda$beginGetCredentialResponseCollectionCandidateEntryMetrics$1(java.util.Map responseCounts, android.service.credentials.CredentialEntry entry) {
        java.lang.String entryKey = com.android.server.credentials.MetricUtilities.generateMetricKey(entry.getType(), 20);
        responseCounts.put(entryKey, java.lang.Integer.valueOf(((java.lang.Integer) responseCounts.getOrDefault(entryKey, 0)).intValue() + 1));
    }
}
