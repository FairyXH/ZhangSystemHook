package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public interface IAutofillManagerServiceImplExt {
    public static final int COMMIT_REASON_ACTIVITY_FINISHED = 1;
    public static final int COMMIT_REASON_OPLUS_AUTOFILL_ACTIVITY_FINISHED = Integer.MIN_VALUE;

    default int hookHandleCommitReason(int commitReason) {
        return commitReason;
    }
}
