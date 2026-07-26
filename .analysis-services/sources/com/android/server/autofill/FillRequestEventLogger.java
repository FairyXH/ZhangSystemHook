package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public final class FillRequestEventLogger {
    private static final java.lang.String TAG = "FillRequestEventLogger";
    public static final int TRIGGER_REASON_EXPLICITLY_REQUESTED = 1;
    public static final int TRIGGER_REASON_NORMAL_TRIGGER = 4;
    public static final int TRIGGER_REASON_PRE_TRIGGER = 3;
    public static final int TRIGGER_REASON_RETRIGGER = 2;
    public static final int TRIGGER_REASON_SERVED_FROM_CACHED_RESPONSE = 5;
    public static final int TRIGGER_REASON_UNKNOWN = 0;
    private java.util.Optional<com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal> mEventInternal = java.util.Optional.empty();
    private final int mSessionId;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface TriggerReason {
    }

    private FillRequestEventLogger(int sessionId) {
        this.mSessionId = sessionId;
    }

    public static com.android.server.autofill.FillRequestEventLogger forSessionId(int sessionId) {
        return new com.android.server.autofill.FillRequestEventLogger(sessionId);
    }

    public void startLogForNewRequest() {
        if (!this.mEventInternal.isEmpty()) {
            android.util.Slog.w(TAG, "FillRequestEventLogger is not empty before starting for a new request");
        }
        this.mEventInternal = java.util.Optional.of(new com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal());
    }

    public void maybeSetRequestId(final int requestId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillRequestEventLogger$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal) obj).mRequestId = requestId;
            }
        });
    }

    public void maybeSetAutofillServiceUid(final int uid) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillRequestEventLogger$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal) obj).mAutofillServiceUid = uid;
            }
        });
    }

    public void maybeSetInlineSuggestionHostUid(final android.content.Context context, final int userId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillRequestEventLogger$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.FillRequestEventLogger.lambda$maybeSetInlineSuggestionHostUid$2(context, userId, (com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal) obj);
            }
        });
    }

    static /* synthetic */ void lambda$maybeSetInlineSuggestionHostUid$2(android.content.Context context, int userId, com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal event) {
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

    public void maybeSetFlags(final int flags) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillRequestEventLogger$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal) obj).mFlags = flags;
            }
        });
    }

    public void maybeSetRequestTriggerReason(final int reason) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillRequestEventLogger$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal) obj).mRequestTriggerReason = reason;
            }
        });
    }

    public void maybeSetIsAugmented(final boolean val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillRequestEventLogger$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal) obj).mIsAugmented = val;
            }
        });
    }

    public void maybeSetIsClientSuggestionFallback(final boolean val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillRequestEventLogger$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal) obj).mIsClientSuggestionFallback = val;
            }
        });
    }

    public void maybeSetIsFillDialogEligible(final boolean val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillRequestEventLogger$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal) obj).mIsFillDialogEligible = val;
            }
        });
    }

    public void maybeSetLatencyFillRequestSentMillis(final int timestamp) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillRequestEventLogger$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal) obj).mLatencyFillRequestSentMillis = timestamp;
            }
        });
    }

    public void maybeSetAppPackageUid(final int uid) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FillRequestEventLogger$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal) obj).mAppPackageUid = uid;
            }
        });
    }

    public void logAndEndEvent() {
        if (!this.mEventInternal.isPresent()) {
            android.util.Slog.w(TAG, "Shouldn't be logging AutofillFillRequestReported again for same event");
            return;
        }
        com.android.server.autofill.FillRequestEventLogger.FillRequestEventInternal event = this.mEventInternal.get();
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Log AutofillFillRequestReported: requestId=" + event.mRequestId + " sessionId=" + this.mSessionId + " mAutofillServiceUid=" + event.mAutofillServiceUid + " mInlineSuggestionHostUid=" + event.mInlineSuggestionHostUid + " mIsAugmented=" + event.mIsAugmented + " mIsClientSuggestionFallback=" + event.mIsClientSuggestionFallback + " mIsFillDialogEligible=" + event.mIsFillDialogEligible + " mRequestTriggerReason=" + event.mRequestTriggerReason + " mFlags=" + event.mFlags + " mLatencyFillRequestSentMillis=" + event.mLatencyFillRequestSentMillis + " mAppPackageUid=" + event.mAppPackageUid);
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.AUTOFILL_FILL_REQUEST_REPORTED, event.mRequestId, this.mSessionId, event.mAutofillServiceUid, event.mInlineSuggestionHostUid, event.mIsAugmented, event.mIsClientSuggestionFallback, event.mIsFillDialogEligible, event.mRequestTriggerReason, event.mFlags, event.mLatencyFillRequestSentMillis, event.mAppPackageUid);
        this.mEventInternal = java.util.Optional.empty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class FillRequestEventInternal {
        int mRequestId;
        int mAppPackageUid = -1;
        int mAutofillServiceUid = -1;
        int mInlineSuggestionHostUid = -1;
        boolean mIsAugmented = false;
        boolean mIsClientSuggestionFallback = false;
        boolean mIsFillDialogEligible = false;
        int mRequestTriggerReason = 0;
        int mFlags = -1;
        int mLatencyFillRequestSentMillis = -1;

        FillRequestEventInternal() {
        }
    }
}
