package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IUserManagerServiceWrapper {
    default android.content.Context getContext() {
        return null;
    }

    default void setUserRestriction(java.lang.String key, boolean value, int userId) {
    }

    default void writeUserLP(com.android.server.pm.UserManagerService.UserData userData) {
    }

    default void writeUserListLP() {
    }

    default java.lang.Object getUsersLock() {
        return null;
    }
}
