package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
class LogFieldClassificationScoreOnResultListener implements android.os.RemoteCallback.OnResultListener {
    private static final java.lang.String TAG = "LogFieldClassificationScoreOnResultListener";
    private final android.view.autofill.AutofillId[] mAutofillIds;
    private final java.lang.String[] mCategoryIds;
    private final int mCommitReason;
    private final java.util.ArrayList<android.service.autofill.FieldClassification> mDetectedFieldClassifications;
    private final java.util.ArrayList<android.view.autofill.AutofillId> mDetectedFieldIds;
    private final int mSaveDialogNotShowReason;
    private com.android.server.autofill.Session mSession;
    private final java.lang.String[] mUserValues;
    private final int mViewsSize;

    LogFieldClassificationScoreOnResultListener(com.android.server.autofill.Session session, int saveDialogNotShowReason, int commitReason, int viewsSize, android.view.autofill.AutofillId[] autofillIds, java.lang.String[] userValues, java.lang.String[] categoryIds, java.util.ArrayList<android.view.autofill.AutofillId> detectedFieldIds, java.util.ArrayList<android.service.autofill.FieldClassification> detectedFieldClassifications) {
        this.mSession = session;
        this.mSaveDialogNotShowReason = saveDialogNotShowReason;
        this.mCommitReason = commitReason;
        this.mViewsSize = viewsSize;
        this.mAutofillIds = autofillIds;
        this.mUserValues = userValues;
        this.mCategoryIds = categoryIds;
        this.mDetectedFieldIds = detectedFieldIds;
        this.mDetectedFieldClassifications = detectedFieldClassifications;
    }

    public void onResult(android.os.Bundle result) {
        com.android.server.autofill.Session session = this.mSession;
        if (session == null) {
            android.util.Slog.wtf(TAG, "session is null when calling onResult()");
        } else {
            session.handleLogFieldClassificationScore(result, this.mSaveDialogNotShowReason, this.mCommitReason, this.mViewsSize, this.mAutofillIds, this.mUserValues, this.mCategoryIds, this.mDetectedFieldIds, this.mDetectedFieldClassifications);
            this.mSession = null;
        }
    }
}
