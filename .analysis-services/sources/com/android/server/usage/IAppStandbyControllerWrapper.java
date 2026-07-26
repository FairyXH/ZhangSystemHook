package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public interface IAppStandbyControllerWrapper {
    default void setAppStandbyBucket(java.lang.String packageName, int userId, int newBucket, int reason, long elapsedRealtime, boolean resetTimeout) {
    }
}
