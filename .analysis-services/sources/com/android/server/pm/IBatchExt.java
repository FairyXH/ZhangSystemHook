package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IBatchExt {
    default void setIsAsyncJob(boolean isAsyncJob) {
    }

    default boolean isAsyncJob() {
        return false;
    }

    default void setUserId(int userId) {
    }

    default int getUserId() {
        return -10000;
    }

    default void setAmsRef(com.android.server.am.ActivityManagerService amsRef) {
    }

    default com.android.server.am.ActivityManagerService getAmsRef() {
        return null;
    }

    default boolean isUserRunningAndNotStopping() {
        return true;
    }
}
