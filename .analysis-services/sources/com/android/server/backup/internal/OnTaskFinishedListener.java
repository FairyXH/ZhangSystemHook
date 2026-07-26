package com.android.server.backup.internal;

/* JADX INFO: loaded from: classes.dex */
public interface OnTaskFinishedListener {
    public static final com.android.server.backup.internal.OnTaskFinishedListener NOP = new com.android.server.backup.internal.OnTaskFinishedListener() { // from class: com.android.server.backup.internal.OnTaskFinishedListener$$ExternalSyntheticLambda0
        @Override // com.android.server.backup.internal.OnTaskFinishedListener
        public final void onFinished(java.lang.String str) {
            com.android.server.backup.internal.OnTaskFinishedListener.lambda$static$0(str);
        }
    };

    void onFinished(java.lang.String str);

    static /* synthetic */ void lambda$static$0(java.lang.String caller) {
    }
}
