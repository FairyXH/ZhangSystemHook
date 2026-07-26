package com.android.server.credentials.metrics;

/* JADX INFO: loaded from: classes.dex */
public class RequestSessionMetric {
    private static final java.lang.String TAG = "RequestSessionMetric";
    protected final com.android.server.credentials.metrics.CandidateAggregateMetric mCandidateAggregateMetric;
    protected final com.android.server.credentials.metrics.ChosenProviderFinalPhaseMetric mChosenProviderFinalPhaseMetric;
    protected final com.android.server.credentials.metrics.InitialPhaseMetric mInitialPhaseMetric;
    private final int mSessionIdTrackTwo;
    protected int mSequenceCounter = 0;
    protected java.util.List<com.android.server.credentials.metrics.CandidateBrowsingPhaseMetric> mCandidateBrowsingPhaseMetric = new java.util.ArrayList();

    public RequestSessionMetric(int sessionIdTrackOne, int sessionIdTrackTwo) {
        this.mSessionIdTrackTwo = sessionIdTrackTwo;
        this.mInitialPhaseMetric = new com.android.server.credentials.metrics.InitialPhaseMetric(sessionIdTrackOne);
        this.mCandidateAggregateMetric = new com.android.server.credentials.metrics.CandidateAggregateMetric(sessionIdTrackOne);
        this.mChosenProviderFinalPhaseMetric = new com.android.server.credentials.metrics.ChosenProviderFinalPhaseMetric(sessionIdTrackOne, sessionIdTrackTwo);
    }

    public int returnIncrementSequence() {
        int i = this.mSequenceCounter + 1;
        this.mSequenceCounter = i;
        return i;
    }

    public com.android.server.credentials.metrics.InitialPhaseMetric getInitialPhaseMetric() {
        return this.mInitialPhaseMetric;
    }

    public com.android.server.credentials.metrics.CandidateAggregateMetric getCandidateAggregateMetric() {
        return this.mCandidateAggregateMetric;
    }

    public void collectInitialPhaseMetricInfo(long timestampStarted, int mCallingUid, int metricCode) {
        try {
            this.mInitialPhaseMetric.setCredentialServiceStartedTimeNanoseconds(timestampStarted);
            this.mInitialPhaseMetric.setCallerUid(mCallingUid);
            this.mInitialPhaseMetric.setApiName(metricCode);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error collecting initial phase metric start info: " + e);
        }
    }

    public void collectUiReturnedFinalPhase(boolean uiReturned) {
        try {
            this.mChosenProviderFinalPhaseMetric.setUiReturned(uiReturned);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error collecting ui end time metric: " + e);
        }
    }

    public void collectUiCallStartTime(long uiCallStartTime) {
        try {
            this.mChosenProviderFinalPhaseMetric.setUiCallStartTimeNanoseconds(uiCallStartTime);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error collecting ui start metric: " + e);
        }
    }

    public void collectUiResponseData(boolean uiReturned, long uiEndTimestamp) {
        try {
            this.mChosenProviderFinalPhaseMetric.setUiReturned(uiReturned);
            this.mChosenProviderFinalPhaseMetric.setUiCallEndTimeNanoseconds(uiEndTimestamp);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error collecting ui response metric: " + e);
        }
    }

    public void collectChosenProviderStatus(int status) {
        try {
            this.mChosenProviderFinalPhaseMetric.setChosenProviderStatus(status);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error setting chosen provider status metric: " + e);
        }
    }

    public void collectCreateFlowInitialMetricInfo(boolean origin, android.credentials.CreateCredentialRequest request) {
        try {
            this.mInitialPhaseMetric.setOriginSpecified(origin);
            this.mInitialPhaseMetric.setRequestCounts(java.util.Map.of(com.android.server.credentials.MetricUtilities.generateMetricKey(request.getType(), 20), 1));
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error collecting create flow metric: " + e);
        }
    }

    private java.util.Map<java.lang.String, java.lang.Integer> getRequestCountMap(android.credentials.GetCredentialRequest request) {
        final java.util.Map<java.lang.String, java.lang.Integer> uniqueRequestCounts = new java.util.LinkedHashMap<>();
        try {
            request.getCredentialOptions().forEach(new java.util.function.Consumer() { // from class: com.android.server.credentials.metrics.RequestSessionMetric$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.credentials.metrics.RequestSessionMetric.lambda$getRequestCountMap$0(uniqueRequestCounts, (android.credentials.CredentialOption) obj);
                }
            });
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error during get request count map metric logging: " + e);
        }
        return uniqueRequestCounts;
    }

    static /* synthetic */ void lambda$getRequestCountMap$0(java.util.Map uniqueRequestCounts, android.credentials.CredentialOption option) {
        java.lang.String optionKey = com.android.server.credentials.MetricUtilities.generateMetricKey(option.getType(), 20);
        uniqueRequestCounts.put(optionKey, java.lang.Integer.valueOf(((java.lang.Integer) uniqueRequestCounts.getOrDefault(optionKey, 0)).intValue() + 1));
    }

    public void collectGetFlowInitialMetricInfo(android.credentials.GetCredentialRequest request) {
        try {
            this.mInitialPhaseMetric.setOriginSpecified(request.getOrigin() != null);
            this.mInitialPhaseMetric.setRequestCounts(getRequestCountMap(request));
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error collecting get flow initial metric: " + e);
        }
    }

    public void collectMetricPerBrowsingSelect(android.credentials.selection.UserSelectionDialogResult selection, com.android.server.credentials.metrics.CandidatePhaseMetric selectedProviderPhaseMetric) {
        try {
            com.android.server.credentials.metrics.CandidateBrowsingPhaseMetric browsingPhaseMetric = new com.android.server.credentials.metrics.CandidateBrowsingPhaseMetric();
            browsingPhaseMetric.setEntryEnum(com.android.server.credentials.metrics.EntryEnum.getMetricCodeFromString(selection.getEntryKey()));
            browsingPhaseMetric.setProviderUid(selectedProviderPhaseMetric.getCandidateUid());
            this.mCandidateBrowsingPhaseMetric.add(browsingPhaseMetric);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error collecting browsing metric: " + e);
        }
    }

    public void setHasExceptionFinalPhase(boolean exceptionBitFinalPhase) {
        try {
            this.mChosenProviderFinalPhaseMetric.setHasException(exceptionBitFinalPhase);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error setting final exception metric: " + e);
        }
    }

    public void collectFrameworkException(java.lang.String exception) {
        try {
            this.mChosenProviderFinalPhaseMetric.setFrameworkException(com.android.server.credentials.MetricUtilities.generateMetricKey(exception, 30));
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Unexpected error during metric logging: " + e);
        }
    }

    public void collectUiConfigurationResults(android.content.Context context, android.credentials.selection.IntentCreationResult result, int userId) {
        try {
            this.mChosenProviderFinalPhaseMetric.setOemUiUid(com.android.server.credentials.MetricUtilities.getPackageUid(context, result.getOemUiPackageName(), userId));
            this.mChosenProviderFinalPhaseMetric.setFallbackUiUid(com.android.server.credentials.MetricUtilities.getPackageUid(context, result.getFallbackUiPackageName(), userId));
            this.mChosenProviderFinalPhaseMetric.setOemUiUsageStatus(com.android.server.credentials.metrics.OemUiUsageStatus.createFrom(result.getOemUiUsageStatus()));
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Unexpected error during ui configuration result collection: " + e);
        }
    }

    public void collectFinalPhaseProviderMetricStatus(boolean hasException, com.android.server.credentials.metrics.ProviderStatusForMetrics finalStatus) {
        try {
            this.mChosenProviderFinalPhaseMetric.setHasException(hasException);
            this.mChosenProviderFinalPhaseMetric.setChosenProviderStatus(finalStatus.getMetricCode());
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error during final phase provider status metric logging: " + e);
        }
    }

    public void updateMetricsOnResponseReceived(java.util.Map<java.lang.String, com.android.server.credentials.ProviderSession> providers, android.content.ComponentName componentName, boolean isPrimary) {
        try {
            com.android.server.credentials.ProviderSession chosenProviderSession = providers.get(componentName.flattenToString());
            if (chosenProviderSession != null) {
                com.android.server.credentials.metrics.ProviderSessionMetric providerSessionMetric = chosenProviderSession.getProviderSessionMetric();
                collectChosenMetricViaCandidateTransfer(providerSessionMetric.getCandidatePhasePerProviderMetric(), isPrimary);
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Exception upon candidate to chosen metric transfer: " + e);
        }
    }

    public void collectChosenMetricViaCandidateTransfer(com.android.server.credentials.metrics.CandidatePhaseMetric candidatePhaseMetric, boolean isPrimary) {
        try {
            this.mChosenProviderFinalPhaseMetric.setChosenUid(candidatePhaseMetric.getCandidateUid());
            this.mChosenProviderFinalPhaseMetric.setPrimary(isPrimary);
            this.mChosenProviderFinalPhaseMetric.setQueryPhaseLatencyMicroseconds(candidatePhaseMetric.getQueryLatencyMicroseconds());
            this.mChosenProviderFinalPhaseMetric.setServiceBeganTimeNanoseconds(candidatePhaseMetric.getServiceBeganTimeNanoseconds());
            this.mChosenProviderFinalPhaseMetric.setQueryStartTimeNanoseconds(candidatePhaseMetric.getStartQueryTimeNanoseconds());
            this.mChosenProviderFinalPhaseMetric.setQueryEndTimeNanoseconds(candidatePhaseMetric.getQueryFinishTimeNanoseconds());
            this.mChosenProviderFinalPhaseMetric.setResponseCollective(candidatePhaseMetric.getResponseCollective());
            this.mChosenProviderFinalPhaseMetric.setFinalFinishTimeNanoseconds(java.lang.System.nanoTime());
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error during metric candidate to final transfer: " + e);
        }
    }

    public void logFailureOrUserCancel(boolean isUserCanceledError) {
        try {
            if (isUserCanceledError) {
                setHasExceptionFinalPhase(false);
                logApiCalledAtFinish(com.android.server.credentials.metrics.ApiStatus.USER_CANCELED.getMetricCode());
            } else {
                logApiCalledAtFinish(com.android.server.credentials.metrics.ApiStatus.FAILURE.getMetricCode());
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error during final metric failure emit: " + e);
        }
    }

    public void logCandidatePhaseMetrics(java.util.Map<java.lang.String, com.android.server.credentials.ProviderSession> providers) {
        try {
            int i = this.mSequenceCounter + 1;
            this.mSequenceCounter = i;
            com.android.server.credentials.MetricUtilities.logApiCalledCandidatePhase(providers, i, this.mInitialPhaseMetric);
            if (this.mInitialPhaseMetric.getApiName() == com.android.server.credentials.metrics.ApiName.GET_CREDENTIAL.getMetricCode() || this.mInitialPhaseMetric.getApiName() == com.android.server.credentials.metrics.ApiName.GET_CREDENTIAL_VIA_REGISTRY.getMetricCode()) {
                com.android.server.credentials.MetricUtilities.logApiCalledCandidateGetMetric(providers, this.mSequenceCounter);
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error during candidate metric emit: " + e);
        }
    }

    public void logCandidateAggregateMetrics(java.util.Map<java.lang.String, com.android.server.credentials.ProviderSession> providers) {
        try {
            this.mCandidateAggregateMetric.collectAverages(providers);
            com.android.server.credentials.metrics.CandidateAggregateMetric candidateAggregateMetric = this.mCandidateAggregateMetric;
            int i = this.mSequenceCounter + 1;
            this.mSequenceCounter = i;
            com.android.server.credentials.MetricUtilities.logApiCalledAggregateCandidate(candidateAggregateMetric, i);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error during aggregate candidate logging " + e);
        }
    }

    public void logAuthEntry(com.android.server.credentials.metrics.BrowsedAuthenticationMetric browsedAuthenticationMetric) {
        try {
            if (browsedAuthenticationMetric.getProviderUid() == -1) {
                android.util.Slog.v(TAG, "An authentication entry was not clicked");
                return;
            }
            int i = this.mSequenceCounter + 1;
            this.mSequenceCounter = i;
            com.android.server.credentials.MetricUtilities.logApiCalledAuthenticationMetric(browsedAuthenticationMetric, i);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error during auth entry metric emit: " + e);
        }
    }

    public void logApiCalledAtFinish(int apiStatus) {
        try {
            com.android.server.credentials.metrics.ChosenProviderFinalPhaseMetric chosenProviderFinalPhaseMetric = this.mChosenProviderFinalPhaseMetric;
            java.util.List<com.android.server.credentials.metrics.CandidateBrowsingPhaseMetric> list = this.mCandidateBrowsingPhaseMetric;
            int i = this.mSequenceCounter + 1;
            this.mSequenceCounter = i;
            com.android.server.credentials.MetricUtilities.logApiCalledFinalPhase(chosenProviderFinalPhaseMetric, list, apiStatus, i);
            com.android.server.credentials.metrics.ChosenProviderFinalPhaseMetric chosenProviderFinalPhaseMetric2 = this.mChosenProviderFinalPhaseMetric;
            java.util.List<com.android.server.credentials.metrics.CandidateBrowsingPhaseMetric> list2 = this.mCandidateBrowsingPhaseMetric;
            int i2 = this.mSequenceCounter + 1;
            this.mSequenceCounter = i2;
            com.android.server.credentials.MetricUtilities.logApiCalledNoUidFinal(chosenProviderFinalPhaseMetric2, list2, apiStatus, i2);
        } catch (java.lang.Exception e) {
            android.util.Slog.i(TAG, "Unexpected error during final metric emit: " + e);
        }
    }

    public int getSessionIdTrackTwo() {
        return this.mSessionIdTrackTwo;
    }
}
