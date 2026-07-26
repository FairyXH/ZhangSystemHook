package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public final class FieldClassificationEventLogger {
    public static final int STATUS_CANCELLED = 3;
    public static final int STATUS_FAIL = 2;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_UNKNOWN = 0;
    private static final java.lang.String TAG = "FieldClassificationEventLogger";
    private java.util.Optional<com.android.server.autofill.FieldClassificationEventLogger.FieldClassificationEventInternal> mEventInternal = java.util.Optional.empty();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface FieldClassificationStatus {
    }

    private FieldClassificationEventLogger() {
    }

    public static com.android.server.autofill.FieldClassificationEventLogger createLogger() {
        return new com.android.server.autofill.FieldClassificationEventLogger();
    }

    public void startNewLogForRequest() {
        if (!this.mEventInternal.isEmpty()) {
            android.util.Slog.w(TAG, "FieldClassificationEventLogger is not empty before starting for a new request");
        }
        this.mEventInternal = java.util.Optional.of(new com.android.server.autofill.FieldClassificationEventLogger.FieldClassificationEventInternal());
    }

    public void maybeSetLatencyMillis(final long timestamp) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FieldClassificationEventLogger$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FieldClassificationEventLogger.FieldClassificationEventInternal) obj).mLatencyClassificationRequestMillis = timestamp;
            }
        });
    }

    public void maybeSetCountClassifications(final int countClassifications) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FieldClassificationEventLogger$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FieldClassificationEventLogger.FieldClassificationEventInternal) obj).mCountClassifications = countClassifications;
            }
        });
    }

    public void maybeSetSessionId(final int sessionId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FieldClassificationEventLogger$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FieldClassificationEventLogger.FieldClassificationEventInternal) obj).mSessionId = sessionId;
            }
        });
    }

    public void maybeSetRequestId(final int requestId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FieldClassificationEventLogger$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FieldClassificationEventLogger.FieldClassificationEventInternal) obj).mRequestId = requestId;
            }
        });
    }

    public void maybeSetNextFillRequestId(final int nextFillRequestId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FieldClassificationEventLogger$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FieldClassificationEventLogger.FieldClassificationEventInternal) obj).mNextFillRequestId = nextFillRequestId;
            }
        });
    }

    public void maybeSetAppPackageUid(final int uid) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FieldClassificationEventLogger$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FieldClassificationEventLogger.FieldClassificationEventInternal) obj).mAppPackageUid = uid;
            }
        });
    }

    public void maybeSetRequestStatus(final int status) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FieldClassificationEventLogger$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FieldClassificationEventLogger.FieldClassificationEventInternal) obj).mStatus = status;
            }
        });
    }

    public void maybeSetSessionGc(final boolean isSessionGc) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.FieldClassificationEventLogger$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.FieldClassificationEventLogger.FieldClassificationEventInternal) obj).mIsSessionGc = isSessionGc;
            }
        });
    }

    public void logAndEndEvent() {
        if (!this.mEventInternal.isPresent()) {
            android.util.Slog.w(TAG, "Shouldn't be logging AutofillFieldClassificationEventInternal again for same event");
            return;
        }
        com.android.server.autofill.FieldClassificationEventLogger.FieldClassificationEventInternal event = this.mEventInternal.get();
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Log AutofillFieldClassificationEventReported: mLatencyClassificationRequestMillis=" + event.mLatencyClassificationRequestMillis + " mCountClassifications=" + event.mCountClassifications + " mSessionId=" + event.mSessionId + " mRequestId=" + event.mRequestId + " mNextFillRequestId=" + event.mNextFillRequestId + " mAppPackageUid=" + event.mAppPackageUid + " mStatus=" + event.mStatus + " mIsSessionGc=" + event.mIsSessionGc);
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.AUTOFILL_FIELD_CLASSIFICATION_EVENT_REPORTED, event.mLatencyClassificationRequestMillis, event.mCountClassifications, event.mSessionId, event.mRequestId, event.mNextFillRequestId, event.mAppPackageUid, event.mStatus, event.mIsSessionGc);
        this.mEventInternal = java.util.Optional.empty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class FieldClassificationEventInternal {
        boolean mIsSessionGc;
        int mStatus;
        long mLatencyClassificationRequestMillis = -1;
        int mCountClassifications = -1;
        int mSessionId = -1;
        int mRequestId = -1;
        int mNextFillRequestId = -1;
        int mAppPackageUid = -1;

        FieldClassificationEventInternal() {
        }
    }
}
