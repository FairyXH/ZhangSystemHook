package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public final class SessionCommittedEventLogger {
    private static final java.lang.String TAG = "SessionCommittedEventLogger";
    private java.util.Optional<com.android.server.autofill.SessionCommittedEventLogger.SessionCommittedEventInternal> mEventInternal = java.util.Optional.of(new com.android.server.autofill.SessionCommittedEventLogger.SessionCommittedEventInternal());
    private final int mSessionId;

    private SessionCommittedEventLogger(int sessionId) {
        this.mSessionId = sessionId;
    }

    public static com.android.server.autofill.SessionCommittedEventLogger forSessionId(int sessionId) {
        return new com.android.server.autofill.SessionCommittedEventLogger(sessionId);
    }

    public void maybeSetComponentPackageUid(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SessionCommittedEventLogger$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SessionCommittedEventLogger.SessionCommittedEventInternal) obj).mComponentPackageUid = val;
            }
        });
    }

    public void maybeSetRequestCount(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SessionCommittedEventLogger$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SessionCommittedEventLogger.SessionCommittedEventInternal) obj).mRequestCount = val;
            }
        });
    }

    public void maybeSetCommitReason(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SessionCommittedEventLogger$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SessionCommittedEventLogger.SessionCommittedEventInternal) obj).mCommitReason = val;
            }
        });
    }

    public void maybeSetSessionDurationMillis(final long timestamp) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SessionCommittedEventLogger$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SessionCommittedEventLogger.SessionCommittedEventInternal) obj).mSessionDurationMillis = timestamp;
            }
        });
    }

    public void maybeSetAutofillServiceUid(final int uid) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SessionCommittedEventLogger$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SessionCommittedEventLogger.SessionCommittedEventInternal) obj).mServiceUid = uid;
            }
        });
    }

    public void maybeSetSaveInfoCount(final int saveInfoCount) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SessionCommittedEventLogger$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SessionCommittedEventLogger.SessionCommittedEventInternal) obj).mSaveInfoCount = saveInfoCount;
            }
        });
    }

    public void maybeSetSaveDataTypeCount(final int saveDataTypeCount) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SessionCommittedEventLogger$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SessionCommittedEventLogger.SessionCommittedEventInternal) obj).mSaveDataTypeCount = saveDataTypeCount;
            }
        });
    }

    public void maybeSetLastFillResponseHasSaveInfo(final boolean lastFillResponseHasSaveInfo) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SessionCommittedEventLogger$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SessionCommittedEventLogger.SessionCommittedEventInternal) obj).mLastFillResponseHasSaveInfo = lastFillResponseHasSaveInfo;
            }
        });
    }

    public void logAndEndEvent() {
        if (!this.mEventInternal.isPresent()) {
            android.util.Slog.w(TAG, "Shouldn't be logging AutofillSessionCommitted again for same session.");
            return;
        }
        com.android.server.autofill.SessionCommittedEventLogger.SessionCommittedEventInternal event = this.mEventInternal.get();
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Log AutofillSessionCommitted: sessionId=" + this.mSessionId + " mComponentPackageUid=" + event.mComponentPackageUid + " mRequestCount=" + event.mRequestCount + " mCommitReason=" + event.mCommitReason + " mSessionDurationMillis=" + event.mSessionDurationMillis + " mServiceUid=" + event.mServiceUid + " mSaveInfoCount=" + event.mSaveInfoCount + " mSaveDataTypeCount=" + event.mSaveDataTypeCount + " mLastFillResponseHasSaveInfo=" + event.mLastFillResponseHasSaveInfo);
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.AUTOFILL_SESSION_COMMITTED, this.mSessionId, event.mComponentPackageUid, event.mRequestCount, event.mCommitReason, event.mSessionDurationMillis, event.mServiceUid, event.mSaveInfoCount, event.mSaveDataTypeCount, event.mLastFillResponseHasSaveInfo);
        this.mEventInternal = java.util.Optional.empty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class SessionCommittedEventInternal {
        int mComponentPackageUid = -1;
        int mRequestCount = 0;
        int mCommitReason = 0;
        long mSessionDurationMillis = 0;
        int mSaveInfoCount = -1;
        int mSaveDataTypeCount = -1;
        boolean mLastFillResponseHasSaveInfo = false;
        int mServiceUid = -1;

        SessionCommittedEventInternal() {
        }
    }
}
