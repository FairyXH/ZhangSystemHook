package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public class SaveEventLogger {
    public static final int NO_SAVE_REASON_DATASET_MATCH = 7;
    public static final int NO_SAVE_REASON_FIELD_VALIDATION_FAILED = 6;
    public static final int NO_SAVE_REASON_HAS_EMPTY_REQUIRED = 4;
    public static final int NO_SAVE_REASON_NONE = 1;
    public static final int NO_SAVE_REASON_NO_SAVE_INFO = 2;
    public static final int NO_SAVE_REASON_NO_VALUE_CHANGED = 5;
    public static final int NO_SAVE_REASON_SCREEN_HAS_CREDMAN_FIELD = 10;
    public static final int NO_SAVE_REASON_SESSION_DESTROYED = 9;
    public static final int NO_SAVE_REASON_UNKNOWN = 0;
    public static final int NO_SAVE_REASON_WITH_DELAY_SAVE_FLAG = 3;
    public static final int NO_SAVE_REASON_WITH_DONT_SAVE_ON_FINISH_FLAG = 8;
    public static final int SAVE_UI_SHOWN_REASON_OPTIONAL_ID_CHANGE = 2;
    public static final int SAVE_UI_SHOWN_REASON_REQUIRED_ID_CHANGE = 1;
    public static final int SAVE_UI_SHOWN_REASON_TRIGGER_ID_SET = 3;
    public static final int SAVE_UI_SHOWN_REASON_UNKNOWN = 0;
    private static final java.lang.String TAG = "SaveEventLogger";
    public static final long UNINITIATED_TIMESTAMP = Long.MIN_VALUE;
    private java.util.Optional<com.android.server.autofill.SaveEventLogger.SaveEventInternal> mEventInternal = java.util.Optional.of(new com.android.server.autofill.SaveEventLogger.SaveEventInternal());
    private final int mSessionId;
    private final long mSessionStartTimestamp;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface SaveUiNotShownReason {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface SaveUiShownReason {
    }

    private SaveEventLogger(int sessionId, long sessionStartTimestamp) {
        this.mSessionId = sessionId;
        this.mSessionStartTimestamp = sessionStartTimestamp;
    }

    public static com.android.server.autofill.SaveEventLogger forSessionId(int sessionId, long sessionStartTimestamp) {
        return new com.android.server.autofill.SaveEventLogger(sessionId, sessionStartTimestamp);
    }

    public void maybeSetRequestId(final int requestId) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mRequestId = requestId;
            }
        });
    }

    public void maybeSetAppPackageUid(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mAppPackageUid = val;
            }
        });
    }

    public void maybeSetSaveUiTriggerIds(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mSaveUiTriggerIds = val;
            }
        });
    }

    public void maybeSetFlag(final int val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mFlag = val;
            }
        });
    }

    public void maybeSetIsNewField(final boolean val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mIsNewField = val;
            }
        });
    }

    public void maybeSetSaveUiShownReason(final int reason) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mSaveUiShownReason = reason;
            }
        });
    }

    public void maybeSetSaveUiNotShownReason(final int reason) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mSaveUiNotShownReason = reason;
            }
        });
    }

    public void maybeSetSaveButtonClicked(final boolean val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mSaveButtonClicked = val;
            }
        });
    }

    public void maybeSetCancelButtonClicked(final boolean val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mCancelButtonClicked = val;
            }
        });
    }

    public void maybeSetDialogDismissed(final boolean val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mDialogDismissed = val;
            }
        });
    }

    public void maybeSetIsSaved(final boolean val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mIsSaved = val;
            }
        });
    }

    private long getElapsedTime() {
        return android.os.SystemClock.elapsedRealtime() - this.mSessionStartTimestamp;
    }

    public void maybeSetLatencySaveUiDisplayMillis(final long timestamp) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mLatencySaveUiDisplayMillis = timestamp;
            }
        });
    }

    public void maybeSetLatencySaveUiDisplayMillis() {
        maybeSetLatencySaveUiDisplayMillis(getElapsedTime());
    }

    public void maybeSetLatencySaveRequestMillis(final long timestamp) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mLatencySaveRequestMillis = timestamp;
            }
        });
    }

    public void maybeSetLatencySaveRequestMillis() {
        maybeSetLatencySaveRequestMillis(getElapsedTime());
    }

    public void maybeSetLatencySaveFinishMillis(final long timestamp) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mLatencySaveFinishMillis = timestamp;
            }
        });
    }

    public void maybeSetLatencySaveFinishMillis() {
        maybeSetLatencySaveFinishMillis(getElapsedTime());
    }

    public void maybeSetIsFrameworkCreatedSaveInfo(final boolean val) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mIsFrameworkCreatedSaveInfo = val;
            }
        });
    }

    public void maybeSetAutofillServiceUid(final int uid) {
        this.mEventInternal.ifPresent(new java.util.function.Consumer() { // from class: com.android.server.autofill.SaveEventLogger$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.SaveEventLogger.SaveEventInternal) obj).mServiceUid = uid;
            }
        });
    }

    public void logAndEndEvent() {
        if (!this.mEventInternal.isPresent()) {
            android.util.Slog.w(TAG, "Shouldn't be logging AutofillSaveEventReported again for same event");
            return;
        }
        com.android.server.autofill.SaveEventLogger.SaveEventInternal event = this.mEventInternal.get();
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Log AutofillSaveEventReported: requestId=" + event.mRequestId + " sessionId=" + this.mSessionId + " mAppPackageUid=" + event.mAppPackageUid + " mSaveUiTriggerIds=" + event.mSaveUiTriggerIds + " mFlag=" + event.mFlag + " mIsNewField=" + event.mIsNewField + " mSaveUiShownReason=" + event.mSaveUiShownReason + " mSaveUiNotShownReason=" + event.mSaveUiNotShownReason + " mSaveButtonClicked=" + event.mSaveButtonClicked + " mCancelButtonClicked=" + event.mCancelButtonClicked + " mDialogDismissed=" + event.mDialogDismissed + " mIsSaved=" + event.mIsSaved + " mLatencySaveUiDisplayMillis=" + event.mLatencySaveUiDisplayMillis + " mLatencySaveRequestMillis=" + event.mLatencySaveRequestMillis + " mLatencySaveFinishMillis=" + event.mLatencySaveFinishMillis + " mIsFrameworkCreatedSaveInfo=" + event.mIsFrameworkCreatedSaveInfo + " mServiceUid=" + event.mServiceUid);
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.AUTOFILL_SAVE_EVENT_REPORTED, event.mRequestId, this.mSessionId, event.mAppPackageUid, event.mSaveUiTriggerIds, event.mFlag, event.mIsNewField, event.mSaveUiShownReason, event.mSaveUiNotShownReason, event.mSaveButtonClicked, event.mCancelButtonClicked, event.mDialogDismissed, event.mIsSaved, event.mLatencySaveUiDisplayMillis, event.mLatencySaveRequestMillis, event.mLatencySaveFinishMillis, event.mIsFrameworkCreatedSaveInfo, event.mServiceUid);
        this.mEventInternal = java.util.Optional.empty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class SaveEventInternal {
        int mRequestId;
        int mAppPackageUid = -1;
        int mSaveUiTriggerIds = -1;
        long mFlag = -1;
        boolean mIsNewField = false;
        int mSaveUiShownReason = 0;
        int mSaveUiNotShownReason = 0;
        boolean mSaveButtonClicked = false;
        boolean mCancelButtonClicked = false;
        boolean mDialogDismissed = false;
        boolean mIsSaved = false;
        long mLatencySaveUiDisplayMillis = Long.MIN_VALUE;
        long mLatencySaveRequestMillis = Long.MIN_VALUE;
        long mLatencySaveFinishMillis = Long.MIN_VALUE;
        boolean mIsFrameworkCreatedSaveInfo = false;
        int mServiceUid = -1;

        SaveEventInternal() {
        }
    }
}
