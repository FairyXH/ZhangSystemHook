package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IUserControllerWrapper {
    default com.android.server.am.IUserControllerExt getExtImpl() {
        return new com.android.server.am.IUserControllerExt() { // from class: com.android.server.am.IUserControllerWrapper.1
        };
    }

    default android.util.SparseIntArray getUserProfileGroupIds() {
        return new android.util.SparseIntArray();
    }

    default void startUserInForeground(int targetUserId) {
    }

    default boolean maybeUnlockUser(int userId) {
        return false;
    }
}
