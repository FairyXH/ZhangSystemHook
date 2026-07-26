package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IUserDataPreparerExt {
    default void prepareUserData(int userId, int userSerial, int flags) {
    }

    default void destroyUserData(int userId, int flags) {
    }

    default void reconcileUsers(java.lang.String volumeUuid, java.util.List<android.content.pm.UserInfo> validUsersList, java.util.List<java.io.File> files) {
    }
}
