package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public final class FillResponseEventLogger {
    public static final int AUTHENTICATION_RESULT_FAILURE = 2;
    public static final int AUTHENTICATION_RESULT_SUCCESS = 1;
    public static final int AUTHENTICATION_RESULT_UNKNOWN = 0;
    public static final int AUTHENTICATION_TYPE_DATASET_AHTHENTICATION = 1;
    public static final int AUTHENTICATION_TYPE_FULL_AHTHENTICATION = 2;
    public static final int AUTHENTICATION_TYPE_UNKNOWN = 0;
    public static final int AVAILABLE_COUNT_WHEN_FILL_REQUEST_FAILED_OR_TIMEOUT = -1;
    public static final int DETECTION_PREFER_AUTOFILL_PROVIDER = 1;
    public static final int DETECTION_PREFER_PCC = 2;
    public static final int DETECTION_PREFER_UNKNOWN = 0;
    public static final int DISPLAY_PRESENTATION_TYPE_DIALOG = 3;
    public static final int DISPLAY_PRESENTATION_TYPE_INLINE = 2;
    public static final int DISPLAY_PRESENTATION_TYPE_MENU = 1;
    public static final int DISPLAY_PRESENTATION_TYPE_UNKNOWN = 0;
    public static final int HAVE_SAVE_TRIGGER_ID = 1;
    public static final int RESPONSE_STATUS_CANCELLED = 3;
    public static final int RESPONSE_STATUS_FAILURE = 1;
    public static final int RESPONSE_STATUS_SESSION_DESTROYED = 5;
    public static final int RESPONSE_STATUS_SUCCESS = 2;
    public static final int RESPONSE_STATUS_TIMEOUT = 4;
    public static final int RESPONSE_STATUS_TRANSACTION_TOO_LARGE = 6;
    public static final int RESPONSE_STATUS_UNKNOWN = 0;
    private static final java.lang.String TAG = "FillResponseEventLogger";
    private static final long UNINITIALIZED_TIMESTAMP = -1;
    private final int mSessionId;
    private long startResponseProcessingTimestamp = -1;
    private java.util.Optional<com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal> mEventInternal = java.util.Optional.empty();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AuthenticationResult {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AuthenticationType {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DetectionPreference {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DisplayPresentationType {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ResponseStatus {
    }

    private FillResponseEventLogger(int sessionId) {
        this.mSessionId = sessionId;
    }

    public static com.android.server.autofill.FillResponseEventLogger forSessionId(int sessionId) {
        return new com.android.server.autofill.FillResponseEventLogger(sessionId);
    }

    public void startLogForNewResponse() {
        if (!this.mEventInternal.isEmpty()) {
            android.util.Slog.w(TAG, "FillResponseEventLogger is not empty before starting for a new request");
        }
        this.mEventInternal = java.util.Optional.of(new com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal());
    }

    public void maybeSetRequestId(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mRequestId = val;
            }
        });
    }

    public void maybeSetAppPackageUid(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mAppPackageUid = val;
            }
        });
    }

    public void maybeSetDisplayPresentationType(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mDisplayPresentationType = val;
            }
        });
    }

    public void maybeSetAvailableCount(final java.util.List<android.service.autofill.Dataset> datasetList, final android.view.autofill.AutofillId currentViewId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.FillResponseEventLogger.lambda$maybeSetAvailableCount$3(datasetList, currentViewId, (com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeSetAvailableCount$3(java.util.List datasetList, android.view.autofill.AutofillId currentViewId, com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal event) {
        int availableCount = getDatasetCountForAutofillId(datasetList, currentViewId);
        event.mAvailableCount = availableCount;
    }

    public void maybeSetAvailableCount(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mAvailableCount = val;
            }
        });
    }

    public void maybeSetTotalDatasetsProvided(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.FillResponseEventLogger.lambda$maybeSetTotalDatasetsProvided$5(val, (com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeSetTotalDatasetsProvided$5(int val, com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal event) {
        if (event.mTotalDatasetsProvided == -1) {
            event.mTotalDatasetsProvided = val;
        }
    }

    private static int getDatasetCountForAutofillId(java.util.List<android.service.autofill.Dataset> datasetList, android.view.autofill.AutofillId currentViewId) {
        int availableCount = 0;
        if (datasetList != null) {
            for (int i = 0; i < datasetList.size(); i++) {
                android.service.autofill.Dataset data = datasetList.get(i);
                if (data != null && data.getFieldIds() != null && data.getFieldIds().contains(currentViewId)) {
                    availableCount++;
                }
            }
        }
        return availableCount;
    }

    public void maybeSetSaveUiTriggerIds(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mSaveUiTriggerIds = val;
            }
        });
    }

    public void maybeSetLatencyFillResponseReceivedMillis(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mLatencyFillResponseReceivedMillis = val;
            }
        });
    }

    public void maybeSetAuthenticationType(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mAuthenticationType = val;
            }
        });
    }

    public void maybeSetAuthenticationResult(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mAuthenticationResult = val;
            }
        });
    }

    public void maybeSetAuthenticationFailureReason(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mAuthenticationFailureReason = val;
            }
        });
    }

    public void maybeSetLatencyAuthenticationUiDisplayMillis(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mLatencyAuthenticationUiDisplayMillis = val;
            }
        });
    }

    public void maybeSetLatencyDatasetDisplayMillis(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda17
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mLatencyDatasetDisplayMillis = val;
            }
        });
    }

    public void maybeSetResponseStatus(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mResponseStatus = val;
            }
        });
    }

    public void startResponseProcessingTime() {
        this.startResponseProcessingTimestamp = android.os.SystemClock.elapsedRealtime();
    }

    public void maybeSetLatencyResponseProcessingMillis() {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$maybeSetLatencyResponseProcessingMillis$14((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeSetLatencyResponseProcessingMillis$14(com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal event) {
        if (this.startResponseProcessingTimestamp == -1 && com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "uninitialized startResponseProcessingTimestamp");
        }
        event.mLatencyResponseProcessingMillis = android.os.SystemClock.elapsedRealtime() - this.startResponseProcessingTimestamp;
    }

    public void maybeSetAvailablePccCount(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mAvailablePccCount = val;
            }
        });
    }

    public void maybeSetAvailablePccOnlyCount(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mAvailablePccOnlyCount = val;
            }
        });
    }

    public void maybeSetDatasetsCountAfterPotentialPccFiltering(final java.util.List<android.service.autofill.Dataset> datasetList) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.FillResponseEventLogger.lambda$maybeSetDatasetsCountAfterPotentialPccFiltering$17(datasetList, (com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeSetDatasetsCountAfterPotentialPccFiltering$17(java.util.List datasetList, com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal event) {
        int pccOnlyCount = 0;
        int pccCount = 0;
        int totalCount = 0;
        if (datasetList != null) {
            totalCount = datasetList.size();
            for (int i = 0; i < datasetList.size(); i++) {
                android.service.autofill.Dataset dataset = (android.service.autofill.Dataset) datasetList.get(i);
                if (dataset != null) {
                    if (dataset.getEligibleReason() == 4) {
                        pccOnlyCount++;
                        pccCount++;
                    } else if (dataset.getEligibleReason() == 5) {
                        pccCount++;
                    }
                }
            }
        }
        event.mAvailablePccOnlyCount = pccOnlyCount;
        event.mAvailablePccCount = pccCount;
        event.mAvailableCount = totalCount;
    }

    public void maybeSetDetectionPreference(final int detectionPreference) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillResponseEventLogger$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal) obj).mDetectionPref = detectionPreference;
            }
        });
    }

    public void logAndEndEvent() {
        if (!this.mEventInternal.isPresent()) {
            android.util.Slog.w(TAG, "Shouldn't be logging AutofillFillRequestReported again for same event");
            return;
        }
        com.android.server.autofill.FillResponseEventLogger.FillResponseEventInternal event = this.mEventInternal.get();
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Log AutofillFillResponseReported: requestId=" + event.mRequestId + " sessionId=" + this.mSessionId + " mAppPackageUid=" + event.mAppPackageUid + " mDisplayPresentationType=" + event.mDisplayPresentationType + " mAvailableCount=" + event.mAvailableCount + " mSaveUiTriggerIds=" + event.mSaveUiTriggerIds + " mLatencyFillResponseReceivedMillis=" + event.mLatencyFillResponseReceivedMillis + " mAuthenticationType=" + event.mAuthenticationType + " mAuthenticationResult=" + event.mAuthenticationResult + " mAuthenticationFailureReason=" + event.mAuthenticationFailureReason + " mLatencyAuthenticationUiDisplayMillis=" + event.mLatencyAuthenticationUiDisplayMillis + " mLatencyDatasetDisplayMillis=" + event.mLatencyDatasetDisplayMillis + " mResponseStatus=" + event.mResponseStatus + " mLatencyResponseProcessingMillis=" + event.mLatencyResponseProcessingMillis + " mAvailablePccCount=" + event.mAvailablePccCount + " mAvailablePccOnlyCount=" + event.mAvailablePccOnlyCount + " mTotalDatasetsProvided=" + event.mTotalDatasetsProvided + " mDetectionPref=" + event.mDetectionPref);
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.AUTOFILL_FILL_RESPONSE_REPORTED, event.mRequestId, this.mSessionId, event.mAppPackageUid, event.mDisplayPresentationType, event.mAvailableCount, event.mSaveUiTriggerIds, event.mLatencyFillResponseReceivedMillis, event.mAuthenticationType, event.mAuthenticationResult, event.mAuthenticationFailureReason, event.mLatencyAuthenticationUiDisplayMillis, event.mLatencyDatasetDisplayMillis, event.mResponseStatus, event.mLatencyResponseProcessingMillis, event.mAvailablePccCount, event.mAvailablePccOnlyCount, event.mTotalDatasetsProvided, event.mDetectionPref);
        this.mEventInternal = java.util.Optional.empty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class FillResponseEventInternal {
        int mRequestId = -1;
        int mAppPackageUid = -1;
        int mDisplayPresentationType = 0;
        int mAvailableCount = 0;
        int mSaveUiTriggerIds = -1;
        int mLatencyFillResponseReceivedMillis = -1;
        int mAuthenticationType = 0;
        int mAuthenticationResult = 0;
        int mAuthenticationFailureReason = -1;
        int mLatencyAuthenticationUiDisplayMillis = -1;
        int mLatencyDatasetDisplayMillis = -1;
        int mResponseStatus = 0;
        long mLatencyResponseProcessingMillis = -1;
        int mAvailablePccCount = -1;
        int mAvailablePccOnlyCount = -1;
        int mTotalDatasetsProvided = -1;
        int mDetectionPref = 0;

        FillResponseEventInternal() {
        }
    }
}
