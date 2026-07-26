package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IStagingManagerExt {
    public static final java.lang.String NONE = "none";

    default java.lang.String getSotaAppState() {
        return "none";
    }

    default boolean isSotaAppSession(com.android.server.pm.StagingManager.StagedSession session) {
        return false;
    }

    default boolean isBootFromSotaAppUpdate() {
        return false;
    }
}
