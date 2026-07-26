package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public final class PresentationStatsEventLogger {
    public static final int AUTHENTICATION_RESULT_FAILURE = 2;
    public static final int AUTHENTICATION_RESULT_SUCCESS = 1;
    public static final int AUTHENTICATION_RESULT_UNKNOWN = 0;
    public static final int AUTHENTICATION_TYPE_DATASET_AUTHENTICATION = 1;
    public static final int AUTHENTICATION_TYPE_FULL_AUTHENTICATION = 2;
    public static final int AUTHENTICATION_TYPE_UNKNOWN = 0;
    private static final int DEFAULT_VALUE_INT = -1;
    public static final int DETECTION_PREFER_AUTOFILL_PROVIDER = 1;
    public static final int DETECTION_PREFER_PCC = 2;
    public static final int DETECTION_PREFER_UNKNOWN = 0;
    public static final int NOT_SHOWN_REASON_ACTIVITY_FINISHED = 4;
    public static final int NOT_SHOWN_REASON_ANY_SHOWN = 1;
    public static final int NOT_SHOWN_REASON_NO_FOCUS = 8;
    public static final int NOT_SHOWN_REASON_REQUEST_FAILED = 7;
    public static final int NOT_SHOWN_REASON_REQUEST_TIMEOUT = 5;
    public static final int NOT_SHOWN_REASON_SESSION_COMMITTED_PREMATURELY = 6;
    public static final int NOT_SHOWN_REASON_UNKNOWN = 0;
    public static final int NOT_SHOWN_REASON_VIEW_CHANGED = 3;
    public static final int NOT_SHOWN_REASON_VIEW_FOCUSED_BEFORE_FILL_DIALOG_RESPONSE = 9;
    public static final int NOT_SHOWN_REASON_VIEW_FOCUS_CHANGED = 2;
    public static final int PICK_REASON_NO_PCC = 1;
    public static final int PICK_REASON_PCC_DETECTION_ONLY = 4;
    public static final int PICK_REASON_PCC_DETECTION_PREFERRED_WITH_PROVIDER = 5;
    public static final int PICK_REASON_PROVIDER_DETECTION_ONLY = 2;
    public static final int PICK_REASON_PROVIDER_DETECTION_PREFERRED_WITH_PCC = 3;
    public static final int PICK_REASON_UNKNOWN = 0;
    private static final java.lang.String TAG = "PresentationStatsEventLogger";
    private final int mCallingAppUid;
    private java.util.Optional<com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal> mEventInternal = java.util.Optional.empty();
    private final int mSessionId;
    private final long mSessionStartTimestamp;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AuthenticationResult {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AuthenticationType {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DatasetPickedReason {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DetectionPreference {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface NotShownReason {
    }

    private PresentationStatsEventLogger(int sessionId, int callingAppUid, long timestamp) {
        this.mSessionId = sessionId;
        this.mCallingAppUid = callingAppUid;
        this.mSessionStartTimestamp = timestamp;
    }

    public static com.android.server.autofill.PresentationStatsEventLogger createPresentationLog(int sessionId, int callingAppUid, long timestamp) {
        return new com.android.server.autofill.PresentationStatsEventLogger(sessionId, callingAppUid, timestamp);
    }

    public void startNewEvent() {
        if (this.mEventInternal.isPresent()) {
            android.util.Slog.e(TAG, "Failed to start new event because already have active event.");
        } else {
            this.mEventInternal = java.util.Optional.of(new com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal());
        }
    }

    public void maybeSetRequestId(final int requestId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda38
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mRequestId = requestId;
            }
        });
    }

    public void maybeSetIsCredentialRequest(final boolean isCredentialRequest) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda30
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mIsCredentialRequest = isCredentialRequest;
            }
        });
    }

    public void maybeSetWebviewRequestedCredential(final boolean webviewRequestedCredential) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mWebviewRequestedCredential = webviewRequestedCredential;
            }
        });
    }

    public void maybeSetNoPresentationEventReason(final int reason) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.PresentationStatsEventLogger.lambda$maybeSetNoPresentationEventReason$3(reason, (com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeSetNoPresentationEventReason$3(int reason, com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        if (event.mCountShown == 0) {
            event.mNoPresentationReason = reason;
        }
    }

    public void maybeSetNoPresentationEventReasonIfNoReasonExists(final int reason) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda28
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.PresentationStatsEventLogger.lambda$maybeSetNoPresentationEventReasonIfNoReasonExists$4(reason, (com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeSetNoPresentationEventReasonIfNoReasonExists$4(int reason, com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        if (event.mCountShown == 0 && event.mNoPresentationReason == 0) {
            event.mNoPresentationReason = reason;
        }
    }

    public void maybeSetAvailableCount(final java.util.List<android.service.autofill.Dataset> datasetList, final android.view.autofill.AutofillId currentViewId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda20
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.PresentationStatsEventLogger.lambda$maybeSetAvailableCount$5(datasetList, currentViewId, (com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeSetAvailableCount$5(java.util.List datasetList, android.view.autofill.AutofillId currentViewId, com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        com.android.server.autofill.PresentationStatsEventLogger.CountContainer container = getDatasetCountForAutofillId(datasetList, currentViewId);
        event.mAvailableCount = container.mAvailableCount;
        event.mAvailablePccCount = container.mAvailablePccCount;
        event.mAvailablePccOnlyCount = container.mAvailablePccOnlyCount;
        event.mIsDatasetAvailable = container.mAvailableCount > 0;
    }

    public void maybeIncrementCountShown() {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$maybeIncrementCountShown$6((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeIncrementCountShown$6(com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        if (event.shouldResetShownCount) {
            event.shouldResetShownCount = false;
            event.mCountShown = 0;
        }
        if (event.mCountShown == 0) {
            maybeSetSuggestionPresentedTimestampMs();
        }
        event.mCountShown++;
    }

    public void markShownCountAsResettable() {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda36
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).shouldResetShownCount = true;
            }
        });
    }

    public void maybeSetCountShown(final java.util.List<android.service.autofill.Dataset> datasetList, final android.view.autofill.AutofillId currentViewId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.PresentationStatsEventLogger.lambda$maybeSetCountShown$8(datasetList, currentViewId, (com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeSetCountShown$8(java.util.List datasetList, android.view.autofill.AutofillId currentViewId, com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        com.android.server.autofill.PresentationStatsEventLogger.CountContainer container = getDatasetCountForAutofillId(datasetList, currentViewId);
        event.mCountShown = container.mAvailableCount;
        if (container.mAvailableCount > 0) {
            event.mNoPresentationReason = 1;
        }
    }

    public void maybeSetCountShown(final int datasets) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mCountShown = datasets;
            }
        });
    }

    private static com.android.server.autofill.PresentationStatsEventLogger.CountContainer getDatasetCountForAutofillId(java.util.List<android.service.autofill.Dataset> datasetList, android.view.autofill.AutofillId currentViewId) {
        com.android.server.autofill.PresentationStatsEventLogger.CountContainer container = new com.android.server.autofill.PresentationStatsEventLogger.CountContainer();
        if (datasetList != null) {
            for (int i = 0; i < datasetList.size(); i++) {
                android.service.autofill.Dataset data = datasetList.get(i);
                if (data != null && data.getFieldIds() != null && data.getFieldIds().contains(currentViewId)) {
                    container.mAvailableCount++;
                    if (data.getEligibleReason() == 4) {
                        container.mAvailablePccOnlyCount++;
                        container.mAvailablePccCount++;
                    } else if (data.getEligibleReason() == 5) {
                        container.mAvailablePccCount++;
                    }
                }
            }
        }
        return container;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class CountContainer {
        int mAvailableCount;
        int mAvailablePccCount;
        int mAvailablePccOnlyCount;

        CountContainer() {
            this.mAvailableCount = 0;
            this.mAvailablePccCount = 0;
            this.mAvailablePccOnlyCount = 0;
        }

        CountContainer(int availableCount, int availablePccCount, int availablePccOnlyCount) {
            this.mAvailableCount = 0;
            this.mAvailablePccCount = 0;
            this.mAvailablePccOnlyCount = 0;
            this.mAvailableCount = availableCount;
            this.mAvailablePccCount = availablePccCount;
            this.mAvailablePccOnlyCount = availablePccOnlyCount;
        }
    }

    public void maybeSetCountFilteredUserTyping(final int countFilteredUserTyping) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mCountFilteredUserTyping = countFilteredUserTyping;
            }
        });
    }

    public void maybeSetCountNotShownImePresentationNotDrawn(final int countNotShownImePresentationNotDrawn) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda22
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mCountNotShownImePresentationNotDrawn = countNotShownImePresentationNotDrawn;
            }
        });
    }

    public void maybeSetCountNotShownImeUserNotSeen(final int countNotShownImeUserNotSeen) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda24
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mCountNotShownImeUserNotSeen = countNotShownImeUserNotSeen;
            }
        });
    }

    public void maybeSetDisplayPresentationType(final int uiType) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mDisplayPresentationType = com.android.server.autofill.PresentationStatsEventLogger.getDisplayPresentationType(uiType);
            }
        });
    }

    public void maybeSetFillRequestSentTimestampMs(final int timestamp) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda17
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mFillRequestSentTimestampMs = timestamp;
            }
        });
    }

    public void maybeSetFillRequestSentTimestampMs() {
        maybeSetFillRequestSentTimestampMs(getElapsedTime());
    }

    public void maybeSetFillResponseReceivedTimestampMs(final int timestamp) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mFillResponseReceivedTimestampMs = timestamp;
            }
        });
    }

    public void maybeSetFillResponseReceivedTimestampMs() {
        maybeSetFillResponseReceivedTimestampMs(getElapsedTime());
    }

    public void maybeSetSuggestionSentTimestampMs(final int timestamp) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mSuggestionSentTimestampMs = timestamp;
            }
        });
    }

    public void maybeSetSuggestionSentTimestampMs() {
        maybeSetSuggestionSentTimestampMs(getElapsedTime());
    }

    public void maybeSetSuggestionPresentedTimestampMs(final int timestamp) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda23
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.PresentationStatsEventLogger.lambda$maybeSetSuggestionPresentedTimestampMs$17(timestamp, (com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeSetSuggestionPresentedTimestampMs$17(int timestamp, com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        if (event.mSuggestionPresentedTimestampMs == -1) {
            event.mSuggestionPresentedTimestampMs = timestamp;
        }
        event.mSuggestionPresentedLastTimestampMs = timestamp;
    }

    public void maybeSetSuggestionPresentedTimestampMs() {
        maybeSetSuggestionPresentedTimestampMs(getElapsedTime());
    }

    public void maybeSetSelectedDatasetId(final int selectedDatasetId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda35
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mSelectedDatasetId = selectedDatasetId;
            }
        });
        setPresentationSelectedTimestamp();
    }

    public void maybeSetDialogDismissed(final boolean dialogDismissed) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda27
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mDialogDismissed = dialogDismissed;
            }
        });
    }

    public void maybeSetNegativeCtaButtonClicked(final boolean negativeCtaButtonClicked) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mNegativeCtaButtonClicked = negativeCtaButtonClicked;
            }
        });
    }

    public void maybeSetPositiveCtaButtonClicked(final boolean positiveCtaButtonClicked) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda32
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mPositiveCtaButtonClicked = positiveCtaButtonClicked;
            }
        });
    }

    public void maybeSetInlinePresentationAndSuggestionHostUid(final android.content.Context context, final int userId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.PresentationStatsEventLogger.lambda$maybeSetInlinePresentationAndSuggestionHostUid$22(context, userId, (com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeSetInlinePresentationAndSuggestionHostUid$22(android.content.Context context, int userId, com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        event.mDisplayPresentationType = 2;
        java.lang.String imeString = android.provider.Settings.Secure.getStringForUser(context.getContentResolver(), "default_input_method", userId);
        if (android.text.TextUtils.isEmpty(imeString)) {
            android.util.Slog.w(TAG, "No default IME found");
            return;
        }
        android.content.ComponentName imeComponent = android.content.ComponentName.unflattenFromString(imeString);
        if (imeComponent == null) {
            android.util.Slog.w(TAG, "No default IME found");
            return;
        }
        java.lang.String packageName = imeComponent.getPackageName();
        try {
            int imeUid = context.getPackageManager().getApplicationInfoAsUser(packageName, android.content.pm.PackageManager.ApplicationInfoFlags.of(0L), userId).uid;
            event.mInlineSuggestionHostUid = imeUid;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.w(TAG, "Couldn't find packageName: " + packageName);
        }
    }

    public void maybeSetAutofillServiceUid(final int uid) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda26
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mAutofillServiceUid = uid;
            }
        });
    }

    public void maybeSetIsNewRequest(final boolean isRequestTriggered) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda21
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mIsRequestTriggered = isRequestTriggered;
            }
        });
    }

    public void maybeSetAuthenticationType(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mAuthenticationType = val;
            }
        });
    }

    public void maybeSetAuthenticationResult(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda34
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mAuthenticationResult = val;
            }
        });
    }

    public void maybeSetLatencyAuthenticationUiDisplayMillis(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda31
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mLatencyAuthenticationUiDisplayMillis = val;
            }
        });
    }

    public void maybeSetLatencyAuthenticationUiDisplayMillis() {
        maybeSetLatencyAuthenticationUiDisplayMillis(getElapsedTime());
    }

    public void maybeSetLatencyDatasetDisplayMillis(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda25
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mLatencyDatasetDisplayMillis = val;
            }
        });
    }

    public void maybeSetLatencyDatasetDisplayMillis() {
        maybeSetLatencyDatasetDisplayMillis(getElapsedTime());
    }

    public void maybeSetAvailablePccCount(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mAvailablePccCount = val;
            }
        });
    }

    public void maybeSetAvailablePccOnlyCount(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda39
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mAvailablePccOnlyCount = val;
            }
        });
    }

    public void maybeSetSelectedDatasetPickReason(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$maybeSetSelectedDatasetPickReason$31(val, (com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeSetSelectedDatasetPickReason$31(int val, com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        event.mSelectedDatasetPickedReason = convertDatasetPickReason(val);
    }

    public void maybeSetDetectionPreference(final int detectionPreference) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mDetectionPreference = detectionPreference;
            }
        });
    }

    public void onFieldTextUpdated(final com.android.server.autofill.ViewState state, final int length) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda29
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onFieldTextUpdated$33(state, length, (com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFieldTextUpdated$33(com.android.server.autofill.ViewState state, int length, com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        int timestamp = getElapsedTime();
        if (state == null || state.id == null || state.id.getViewId() != event.mFocusedId) {
            android.util.Slog.w(TAG, "Bad view state for: " + event.mFocusedId);
            return;
        }
        if ((state.getState() & 4) != 0) {
            event.mAutofilledTimestampMs = timestamp;
            return;
        }
        if (event.mFieldFirstLength == -1) {
            event.mFieldFirstLength = length;
        }
        event.mFieldLastLength = length;
        if (event.mFieldModifiedFirstTimestampMs == -1) {
            event.mFieldModifiedFirstTimestampMs = timestamp;
        }
        event.mFieldModifiedLastTimestampMs = timestamp;
    }

    public void setPresentationSelectedTimestamp() {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$setPresentationSelectedTimestamp$34((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setPresentationSelectedTimestamp$34(com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        event.mSelectionTimestamp = getElapsedTime();
    }

    private int getElapsedTime() {
        return (int) (android.os.SystemClock.elapsedRealtime() - this.mSessionStartTimestamp);
    }

    private int convertDatasetPickReason(int val) {
        switch (val) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return val;
            default:
                return 0;
        }
    }

    public void maybeSetFieldClassificationRequestId(final int requestId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mFieldClassificationRequestId = requestId;
            }
        });
    }

    public void maybeSetViewFillablesAndCount(final java.util.List<android.view.autofill.AutofillId> autofillIds) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda37
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.PresentationStatsEventLogger.lambda$maybeSetViewFillablesAndCount$36(autofillIds, (com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeSetViewFillablesAndCount$36(java.util.List autofillIds, com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        event.mAutofillIdsAttemptedAutofill = new android.util.ArraySet<>(autofillIds);
        event.mViewFillableTotalCount = event.mAutofillIdsAttemptedAutofill.size();
    }

    public void maybeSetViewFillFailureCounts(final int failureCount) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj).mViewFillFailureCount = failureCount;
            }
        });
    }

    public void maybeSetFocusedId(final android.view.autofill.AutofillId id) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.PresentationStatsEventLogger.lambda$maybeSetFocusedId$38(id, (com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeSetFocusedId$38(android.view.autofill.AutofillId id, com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        event.mFocusedId = id.getViewId();
        if (id.isVirtualInt()) {
            event.mFocusedVirtualAutofillId = id.getVirtualChildIntId() % 100;
        }
    }

    public void maybeAddSuccessId(final android.view.autofill.AutofillId autofillId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.PresentationStatsEventLogger$$ExternalSyntheticLambda33
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.PresentationStatsEventLogger.lambda$maybeAddSuccessId$39(autofillId, (com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeAddSuccessId$39(android.view.autofill.AutofillId autofillId, com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event) {
        android.util.ArraySet<android.view.autofill.AutofillId> autofillIds = event.mAutofillIdsAttemptedAutofill;
        if (autofillIds == null) {
            android.util.Slog.w(TAG, "Attempted autofill ids is null, but received autofillId:" + autofillId + " successfully filled");
            event.mViewFilledButUnexpectedCount++;
            return;
        }
        if (autofillIds.contains(autofillId)) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "Logging autofill for id:" + autofillId);
            }
            event.mViewFillSuccessCount++;
            autofillIds.remove(autofillId);
            event.mAlreadyFilledAutofillIds.add(autofillId);
            return;
        }
        if (!event.mAlreadyFilledAutofillIds.contains(autofillId)) {
            android.util.Slog.w(TAG, "Successfully filled autofillId:" + autofillId + " not found in list of attempted autofill ids: " + autofillIds);
            event.mViewFilledButUnexpectedCount++;
        } else if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Successfully filled autofillId:" + autofillId + " already processed ");
        }
    }

    public void logAndEndEvent() {
        if (!this.mEventInternal.isPresent()) {
            android.util.Slog.w(TAG, "Shouldn't be logging AutofillPresentationEventReported again for same event");
            return;
        }
        com.android.server.autofill.PresentationStatsEventLogger.PresentationStatsEventInternal event = this.mEventInternal.get();
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Log AutofillPresentationEventReported: requestId=" + event.mRequestId + " sessionId=" + this.mSessionId + " mNoPresentationEventReason=" + event.mNoPresentationReason + " mAvailableCount=" + event.mAvailableCount + " mCountShown=" + event.mCountShown + " mCountFilteredUserTyping=" + event.mCountFilteredUserTyping + " mCountNotShownImePresentationNotDrawn=" + event.mCountNotShownImePresentationNotDrawn + " mCountNotShownImeUserNotSeen=" + event.mCountNotShownImeUserNotSeen + " mDisplayPresentationType=" + event.mDisplayPresentationType + " mAutofillServiceUid=" + event.mAutofillServiceUid + " mInlineSuggestionHostUid=" + event.mInlineSuggestionHostUid + " mIsRequestTriggered=" + event.mIsRequestTriggered + " mFillRequestSentTimestampMs=" + event.mFillRequestSentTimestampMs + " mFillResponseReceivedTimestampMs=" + event.mFillResponseReceivedTimestampMs + " mSuggestionSentTimestampMs=" + event.mSuggestionSentTimestampMs + " mSuggestionPresentedTimestampMs=" + event.mSuggestionPresentedTimestampMs + " mSelectedDatasetId=" + event.mSelectedDatasetId + " mDialogDismissed=" + event.mDialogDismissed + " mNegativeCtaButtonClicked=" + event.mNegativeCtaButtonClicked + " mPositiveCtaButtonClicked=" + event.mPositiveCtaButtonClicked + " mAuthenticationType=" + event.mAuthenticationType + " mAuthenticationResult=" + event.mAuthenticationResult + " mLatencyAuthenticationUiDisplayMillis=" + event.mLatencyAuthenticationUiDisplayMillis + " mLatencyDatasetDisplayMillis=" + event.mLatencyDatasetDisplayMillis + " mAvailablePccCount=" + event.mAvailablePccCount + " mAvailablePccOnlyCount=" + event.mAvailablePccOnlyCount + " mSelectedDatasetPickedReason=" + event.mSelectedDatasetPickedReason + " mDetectionPreference=" + event.mDetectionPreference + " mFieldClassificationRequestId=" + event.mFieldClassificationRequestId + " mAppPackageUid=" + this.mCallingAppUid + " mIsCredentialRequest=" + event.mIsCredentialRequest + " mWebviewRequestedCredential=" + event.mWebviewRequestedCredential + " mViewFillableTotalCount=" + event.mViewFillableTotalCount + " mViewFillFailureCount=" + event.mViewFillFailureCount + " mFocusedId=" + event.mFocusedId + " mViewFillSuccessCount=" + event.mViewFillSuccessCount + " mViewFilledButUnexpectedCount=" + event.mViewFilledButUnexpectedCount + " event.mSelectionTimestamp=" + event.mSelectionTimestamp + " event.mAutofilledTimestampMs=" + event.mAutofilledTimestampMs + " event.mFieldModifiedFirstTimestampMs=" + event.mFieldModifiedFirstTimestampMs + " event.mFieldModifiedLastTimestampMs=" + event.mFieldModifiedLastTimestampMs + " event.mSuggestionPresentedLastTimestampMs=" + event.mSuggestionPresentedLastTimestampMs + " event.mFocusedVirtualAutofillId=" + event.mFocusedVirtualAutofillId + " event.mFieldFirstLength=" + event.mFieldFirstLength + " event.mFieldLastLength=" + event.mFieldLastLength);
        }
        if (!event.mIsDatasetAvailable) {
            this.mEventInternal = java.util.Optional.empty();
        } else {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.AUTOFILL_PRESENTATION_EVENT_REPORTED, event.mRequestId, this.mSessionId, event.mNoPresentationReason, event.mAvailableCount, event.mCountShown, event.mCountFilteredUserTyping, event.mCountNotShownImePresentationNotDrawn, event.mCountNotShownImeUserNotSeen, event.mDisplayPresentationType, event.mAutofillServiceUid, event.mInlineSuggestionHostUid, event.mIsRequestTriggered, event.mFillRequestSentTimestampMs, event.mFillResponseReceivedTimestampMs, event.mSuggestionSentTimestampMs, event.mSuggestionPresentedTimestampMs, event.mSelectedDatasetId, event.mDialogDismissed, event.mNegativeCtaButtonClicked, event.mPositiveCtaButtonClicked, event.mAuthenticationType, event.mAuthenticationResult, event.mLatencyAuthenticationUiDisplayMillis, event.mLatencyDatasetDisplayMillis, event.mAvailablePccCount, event.mAvailablePccOnlyCount, event.mSelectedDatasetPickedReason, event.mDetectionPreference, event.mFieldClassificationRequestId, this.mCallingAppUid, event.mIsCredentialRequest, event.mWebviewRequestedCredential, event.mViewFillableTotalCount, event.mViewFillFailureCount, event.mFocusedId, event.mViewFillSuccessCount, event.mViewFilledButUnexpectedCount, event.mSelectionTimestamp, event.mAutofilledTimestampMs, event.mFieldModifiedFirstTimestampMs, event.mFieldModifiedLastTimestampMs, event.mSuggestionPresentedLastTimestampMs, event.mFocusedVirtualAutofillId, event.mFieldFirstLength, event.mFieldLastLength);
            this.mEventInternal = java.util.Optional.empty();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class PresentationStatsEventInternal {
        android.util.ArraySet<android.view.autofill.AutofillId> mAutofillIdsAttemptedAutofill;
        int mAvailableCount;
        int mCountFilteredUserTyping;
        int mCountNotShownImePresentationNotDrawn;
        int mCountNotShownImeUserNotSeen;
        boolean mIsDatasetAvailable;
        boolean mIsRequestTriggered;
        int mRequestId;
        int mNoPresentationReason = 0;
        int mCountShown = 0;
        int mDisplayPresentationType = 0;
        int mAutofillServiceUid = -1;
        int mInlineSuggestionHostUid = -1;
        int mFillRequestSentTimestampMs = -1;
        int mFillResponseReceivedTimestampMs = -1;
        int mSuggestionSentTimestampMs = -1;
        int mSuggestionPresentedTimestampMs = -1;
        int mSelectedDatasetId = -1;
        boolean mDialogDismissed = false;
        boolean mNegativeCtaButtonClicked = false;
        boolean mPositiveCtaButtonClicked = false;
        int mAuthenticationType = 0;
        int mAuthenticationResult = 0;
        int mLatencyAuthenticationUiDisplayMillis = -1;
        int mLatencyDatasetDisplayMillis = -1;
        int mAvailablePccCount = -1;
        int mAvailablePccOnlyCount = -1;
        int mSelectedDatasetPickedReason = 0;
        int mDetectionPreference = 0;
        int mFieldClassificationRequestId = -1;
        boolean mIsCredentialRequest = false;
        boolean mWebviewRequestedCredential = false;
        int mViewFillableTotalCount = -1;
        int mViewFillFailureCount = -1;
        int mFocusedId = -1;
        int mSelectionTimestamp = -1;
        int mAutofilledTimestampMs = -1;
        int mFieldModifiedFirstTimestampMs = -1;
        int mFieldModifiedLastTimestampMs = -1;
        int mSuggestionPresentedLastTimestampMs = -1;
        int mFocusedVirtualAutofillId = -1;
        int mFieldFirstLength = -1;
        int mFieldLastLength = -1;
        int mViewFillSuccessCount = 0;
        int mViewFilledButUnexpectedCount = 0;
        android.util.ArraySet<android.view.autofill.AutofillId> mAlreadyFilledAutofillIds = new android.util.ArraySet<>();
        boolean shouldResetShownCount = false;

        PresentationStatsEventInternal() {
        }
    }

    static int getNoPresentationEventReason(int commitReason) {
        switch (commitReason) {
            case 1:
                return 4;
            case 2:
                return 6;
            case 3:
            default:
                return 0;
            case 4:
                return 3;
        }
    }

    private static int getDisplayPresentationType(int uiType) {
        switch (uiType) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 0;
        }
    }
}
