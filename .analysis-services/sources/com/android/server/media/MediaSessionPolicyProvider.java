package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public abstract class MediaSessionPolicyProvider {
    static final int SESSION_POLICY_IGNORE_BUTTON_RECEIVER = 1;
    static final int SESSION_POLICY_IGNORE_BUTTON_SESSION = 2;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface SessionPolicy {
    }

    public MediaSessionPolicyProvider(android.content.Context context) {
    }

    int getSessionPoliciesForApplication(int uid, java.lang.String packageName) {
        return 0;
    }
}
