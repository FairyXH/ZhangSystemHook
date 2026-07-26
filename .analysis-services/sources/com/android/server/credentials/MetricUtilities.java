package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public class MetricUtilities {
    public static final int DEFAULT_INT_32 = -1;
    public static final java.lang.String DEFAULT_STRING = "";
    public static final int DELTA_EXCEPTION_CUT = 30;
    public static final int DELTA_RESPONSES_CUT = 20;
    private static final boolean LOG_FLAG = true;
    public static final int MIN_EMIT_WAIT_TIME_MS = 10;
    private static final java.lang.String TAG = "CredentialManager";
    public static final int UNIT = 1;
    public static final java.lang.String USER_CANCELED_SUBSTRING = "TYPE_USER_CANCELED";
    public static final int ZERO = 0;
    public static final int[] DEFAULT_REPEATED_INT_32 = new int[0];
    public static final java.lang.String[] DEFAULT_REPEATED_STR = new java.lang.String[0];
    public static final boolean[] DEFAULT_REPEATED_BOOL = new boolean[0];

    protected static int getPackageUid(android.content.Context context, android.content.ComponentName componentName, int userId) {
        if (componentName == null) {
            return -1;
        }
        return getPackageUid(context, componentName.getPackageName(), userId);
    }

    public static int getPackageUid(android.content.Context context, java.lang.String packageName, int userId) {
        if (packageName == null) {
            return -1;
        }
        try {
            return context.getPackageManager().getPackageUidAsUser(packageName, android.content.pm.PackageManager.PackageInfoFlags.of(0L), userId);
        } catch (java.lang.Throwable t) {
            android.util.Slog.i(TAG, "Couldn't find uid for " + packageName + ": " + t);
            return -1;
        }
    }

    public static int getHighlyUniqueInteger() {
        return new java.security.SecureRandom().nextInt();
    }

    protected static int getMetricTimestampDifferenceMicroseconds(long t2, long t1) {
        if (t2 - t1 > 2147483647L) {
            android.util.Slog.i(TAG, "Input timestamps are too far apart and unsupported, falling back to default int");
            return -1;
        }
        if (t2 < t1) {
            android.util.Slog.i(TAG, "The timestamps aren't in expected order, falling back to default int");
            return -1;
        }
        return (int) ((t2 - t1) / 1000);
    }

    public static java.lang.String generateMetricKey(java.lang.String classtype, int deltaFromEnd) {
        return classtype.substring(classtype.length() - deltaFromEnd);
    }

    public static void logApiCalledFinalPhase(com.android.server.credentials.metrics.ChosenProviderFinalPhaseMetric finalPhaseMetric, java.util.List<com.android.server.credentials.metrics.CandidateBrowsingPhaseMetric> browsingPhaseMetrics, int apiStatus, int emitSequenceId) {
        try {
            int browsedSize = browsingPhaseMetrics.size();
            int[] browsedClickedEntries = new int[browsedSize];
            int[] browsedProviderUid = new int[browsedSize];
            int index = 0;
            for (com.android.server.credentials.metrics.CandidateBrowsingPhaseMetric metric : browsingPhaseMetrics) {
                browsedClickedEntries[index] = metric.getEntryEnum();
                browsedProviderUid[index] = metric.getProviderUid();
                index++;
            }
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CREDENTIAL_MANAGER_FINAL_PHASE_REPORTED, finalPhaseMetric.getSessionIdProvider(), emitSequenceId, finalPhaseMetric.isUiReturned(), finalPhaseMetric.getChosenUid(), finalPhaseMetric.getTimestampFromReferenceStartMicroseconds(finalPhaseMetric.getQueryStartTimeNanoseconds()), finalPhaseMetric.getTimestampFromReferenceStartMicroseconds(finalPhaseMetric.getQueryEndTimeNanoseconds()), finalPhaseMetric.getTimestampFromReferenceStartMicroseconds(finalPhaseMetric.getUiCallStartTimeNanoseconds()), finalPhaseMetric.getTimestampFromReferenceStartMicroseconds(finalPhaseMetric.getUiCallEndTimeNanoseconds()), finalPhaseMetric.getTimestampFromReferenceStartMicroseconds(finalPhaseMetric.getFinalFinishTimeNanoseconds()), finalPhaseMetric.getChosenProviderStatus(), finalPhaseMetric.isHasException(), DEFAULT_REPEATED_INT_32, -1, -1, -1, -1, -1, browsedClickedEntries, browsedProviderUid, apiStatus, finalPhaseMetric.getResponseCollective().getUniqueEntries(), finalPhaseMetric.getResponseCollective().getUniqueEntryCounts(), finalPhaseMetric.getResponseCollective().getUniqueResponseStrings(), finalPhaseMetric.getResponseCollective().getUniqueResponseCounts(), finalPhaseMetric.getFrameworkException(), finalPhaseMetric.isPrimary());
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Unexpected error during final provider uid emit: " + e);
        }
    }

    public static void logApiCalledAuthenticationMetric(com.android.server.credentials.metrics.BrowsedAuthenticationMetric authenticationMetric, int emitSequenceId) {
        try {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CREDENTIAL_MANAGER_AUTH_CLICK_REPORTED, authenticationMetric.getSessionIdProvider(), emitSequenceId, authenticationMetric.getProviderUid(), authenticationMetric.getAuthEntryCollective().getUniqueResponseStrings(), authenticationMetric.getAuthEntryCollective().getUniqueResponseCounts(), authenticationMetric.getAuthEntryCollective().getUniqueEntries(), authenticationMetric.getAuthEntryCollective().getUniqueEntryCounts(), authenticationMetric.getFrameworkException(), authenticationMetric.isHasException(), authenticationMetric.getProviderStatus(), authenticationMetric.isAuthReturned());
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Unexpected error during candidate auth metric logging: " + e);
        }
    }

    public static void logApiCalledCandidateGetMetric(java.util.Map<java.lang.String, com.android.server.credentials.ProviderSession> providers, int emitSequenceId) {
        try {
            java.util.Collection<com.android.server.credentials.ProviderSession> sessions = providers.values();
            for (com.android.server.credentials.ProviderSession session : sessions) {
                com.android.server.credentials.metrics.CandidatePhaseMetric metric = session.getProviderSessionMetric().getCandidatePhasePerProviderMetric();
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CREDENTIAL_MANAGER_GET_REPORTED, metric.getSessionIdProvider(), emitSequenceId, metric.getCandidateUid(), metric.getResponseCollective().getUniqueResponseStrings(), metric.getResponseCollective().getUniqueResponseCounts());
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Unexpected error during candidate get metric logging: " + e);
        }
    }

    public static void logApiCalledCandidatePhase(java.util.Map<java.lang.String, com.android.server.credentials.ProviderSession> providers, int emitSequenceId, com.android.server.credentials.metrics.InitialPhaseMetric initialPhaseMetric) {
        try {
            java.util.Collection<com.android.server.credentials.ProviderSession> providerSessions = providers.values();
            int providerSize = providerSessions.size();
            int sessionId = -1;
            boolean queryReturned = false;
            int[] candidateUidList = new int[providerSize];
            int[] candidateQueryStartTimeStampList = new int[providerSize];
            int[] candidateQueryEndTimeStampList = new int[providerSize];
            int[] candidateStatusList = new int[providerSize];
            boolean[] candidateHasExceptionList = new boolean[providerSize];
            int[] candidateTotalEntryCountList = new int[providerSize];
            int[] candidateCredentialEntryCountList = new int[providerSize];
            int[] candidateCredentialTypeCountList = new int[providerSize];
            int[] candidateActionEntryCountList = new int[providerSize];
            int[] candidateAuthEntryCountList = new int[providerSize];
            int[] candidateRemoteEntryCountList = new int[providerSize];
            java.lang.String[] frameworkExceptionList = new java.lang.String[providerSize];
            boolean[] candidatePrimaryProviderList = new boolean[providerSize];
            int index = 0;
            for (com.android.server.credentials.ProviderSession session : providerSessions) {
                com.android.server.credentials.metrics.CandidatePhaseMetric metric = session.mProviderSessionMetric.getCandidatePhasePerProviderMetric();
                if (sessionId == -1) {
                    sessionId = metric.getSessionIdProvider();
                }
                if (!queryReturned) {
                    queryReturned = metric.isQueryReturned();
                }
                candidateUidList[index] = metric.getCandidateUid();
                candidateQueryStartTimeStampList[index] = metric.getTimestampFromReferenceStartMicroseconds(metric.getStartQueryTimeNanoseconds());
                candidateQueryEndTimeStampList[index] = metric.getTimestampFromReferenceStartMicroseconds(metric.getQueryFinishTimeNanoseconds());
                candidateStatusList[index] = metric.getProviderQueryStatus();
                candidateHasExceptionList[index] = metric.isHasException();
                candidateTotalEntryCountList[index] = metric.getResponseCollective().getNumEntriesTotal();
                candidateCredentialEntryCountList[index] = metric.getResponseCollective().getCountForEntry(com.android.server.credentials.metrics.EntryEnum.CREDENTIAL_ENTRY);
                candidateCredentialTypeCountList[index] = metric.getResponseCollective().getUniqueResponseStrings().length;
                candidateActionEntryCountList[index] = metric.getResponseCollective().getCountForEntry(com.android.server.credentials.metrics.EntryEnum.ACTION_ENTRY);
                candidateAuthEntryCountList[index] = metric.getResponseCollective().getCountForEntry(com.android.server.credentials.metrics.EntryEnum.AUTHENTICATION_ENTRY);
                candidateRemoteEntryCountList[index] = metric.getResponseCollective().getCountForEntry(com.android.server.credentials.metrics.EntryEnum.REMOTE_ENTRY);
                frameworkExceptionList[index] = metric.getFrameworkException();
                candidatePrimaryProviderList[index] = metric.isPrimary();
                index++;
            }
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CREDENTIAL_MANAGER_CANDIDATE_PHASE_REPORTED, sessionId, emitSequenceId, queryReturned, candidateUidList, candidateQueryStartTimeStampList, candidateQueryEndTimeStampList, candidateStatusList, candidateHasExceptionList, candidateTotalEntryCountList, candidateActionEntryCountList, candidateCredentialEntryCountList, candidateCredentialTypeCountList, candidateRemoteEntryCountList, candidateAuthEntryCountList, frameworkExceptionList, initialPhaseMetric.isOriginSpecified(), initialPhaseMetric.getUniqueRequestStrings(), initialPhaseMetric.getUniqueRequestCounts(), initialPhaseMetric.getApiName(), candidatePrimaryProviderList);
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Unexpected error during candidate provider uid metric emit: " + e);
        }
    }

    public static void logApiCalledSimpleV2(com.android.server.credentials.metrics.ApiName apiName, com.android.server.credentials.metrics.ApiStatus apiStatus, int callingUid) {
        try {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CREDENTIAL_MANAGER_APIV2_CALLED, apiName.getMetricCode(), callingUid, apiStatus.getMetricCode());
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Unexpected error during simple v2 metric logging: " + e);
        }
    }

    public static void logApiCalledInitialPhase(com.android.server.credentials.metrics.InitialPhaseMetric initialPhaseMetric, int sequenceNum) {
        try {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CREDENTIAL_MANAGER_INIT_PHASE_REPORTED, initialPhaseMetric.getApiName(), initialPhaseMetric.getCallerUid(), initialPhaseMetric.getSessionIdCaller(), sequenceNum, initialPhaseMetric.getCredentialServiceStartedTimeNanoseconds(), initialPhaseMetric.getCountRequestClassType(), initialPhaseMetric.getUniqueRequestStrings(), initialPhaseMetric.getUniqueRequestCounts(), initialPhaseMetric.isOriginSpecified(), initialPhaseMetric.getAutofillSessionId(), initialPhaseMetric.getAutofillRequestId());
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Unexpected error during initial metric emit: " + e);
        }
    }

    public static void logApiCalledAggregateCandidate(com.android.server.credentials.metrics.CandidateAggregateMetric candidateAggregateMetric, int sequenceNum) {
        try {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CREDENTIAL_MANAGER_TOTAL_REPORTED, candidateAggregateMetric.getSessionIdProvider(), sequenceNum, candidateAggregateMetric.isQueryReturned(), candidateAggregateMetric.getNumProviders(), getMetricTimestampDifferenceMicroseconds(candidateAggregateMetric.getMinProviderTimestampNanoseconds(), candidateAggregateMetric.getServiceBeganTimeNanoseconds()), getMetricTimestampDifferenceMicroseconds(candidateAggregateMetric.getMaxProviderTimestampNanoseconds(), candidateAggregateMetric.getServiceBeganTimeNanoseconds()), candidateAggregateMetric.getAggregateCollectiveQuery().getUniqueResponseStrings(), candidateAggregateMetric.getAggregateCollectiveQuery().getUniqueResponseCounts(), candidateAggregateMetric.getAggregateCollectiveQuery().getUniqueEntries(), candidateAggregateMetric.getAggregateCollectiveQuery().getUniqueEntryCounts(), candidateAggregateMetric.getTotalQueryFailures(), candidateAggregateMetric.getUniqueExceptionStringsQuery(), candidateAggregateMetric.getUniqueExceptionCountsQuery(), candidateAggregateMetric.getAggregateCollectiveAuth().getUniqueResponseStrings(), candidateAggregateMetric.getAggregateCollectiveAuth().getUniqueResponseCounts(), candidateAggregateMetric.getAggregateCollectiveAuth().getUniqueEntries(), candidateAggregateMetric.getAggregateCollectiveAuth().getUniqueEntryCounts(), candidateAggregateMetric.getTotalAuthFailures(), candidateAggregateMetric.getUniqueExceptionStringsAuth(), candidateAggregateMetric.getUniqueExceptionCountsAuth(), candidateAggregateMetric.getNumAuthEntriesTapped(), candidateAggregateMetric.isAuthReturned());
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Unexpected error during total candidate metric logging: " + e);
        }
    }

    public static void logApiCalledNoUidFinal(com.android.server.credentials.metrics.ChosenProviderFinalPhaseMetric finalPhaseMetric, java.util.List<com.android.server.credentials.metrics.CandidateBrowsingPhaseMetric> browsingPhaseMetrics, int apiStatus, int emitSequenceId) {
        try {
            int browsedSize = browsingPhaseMetrics.size();
            int[] browsedClickedEntries = new int[browsedSize];
            int[] browsedProviderUid = new int[browsedSize];
            int index = 0;
            for (com.android.server.credentials.metrics.CandidateBrowsingPhaseMetric metric : browsingPhaseMetrics) {
                browsedClickedEntries[index] = metric.getEntryEnum();
                browsedProviderUid[index] = -1;
                index++;
            }
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CREDENTIAL_MANAGER_FINALNOUID_REPORTED, finalPhaseMetric.getSessionIdCaller(), emitSequenceId, finalPhaseMetric.isUiReturned(), finalPhaseMetric.getTimestampFromReferenceStartMicroseconds(finalPhaseMetric.getQueryStartTimeNanoseconds()), finalPhaseMetric.getTimestampFromReferenceStartMicroseconds(finalPhaseMetric.getQueryEndTimeNanoseconds()), finalPhaseMetric.getTimestampFromReferenceStartMicroseconds(finalPhaseMetric.getUiCallStartTimeNanoseconds()), finalPhaseMetric.getTimestampFromReferenceStartMicroseconds(finalPhaseMetric.getUiCallEndTimeNanoseconds()), finalPhaseMetric.getTimestampFromReferenceStartMicroseconds(finalPhaseMetric.getFinalFinishTimeNanoseconds()), finalPhaseMetric.getChosenProviderStatus(), finalPhaseMetric.isHasException(), finalPhaseMetric.getResponseCollective().getUniqueEntries(), finalPhaseMetric.getResponseCollective().getUniqueEntryCounts(), finalPhaseMetric.getResponseCollective().getUniqueResponseStrings(), finalPhaseMetric.getResponseCollective().getUniqueResponseCounts(), finalPhaseMetric.getFrameworkException(), browsedClickedEntries, browsedProviderUid, apiStatus, finalPhaseMetric.isPrimary(), finalPhaseMetric.getOemUiUid(), finalPhaseMetric.getFallbackUiUid(), finalPhaseMetric.getOemUiUsageStatus());
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Unexpected error during final no uid metric logging: " + e);
        }
    }
}
