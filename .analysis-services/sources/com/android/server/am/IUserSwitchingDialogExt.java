package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IUserSwitchingDialogExt {
    default void startFreezingScreenInStartUser(int oldUserId, int userId) {
    }

    default void startUserInternalEnter(boolean foreground, int oldUserId, int newUserId, long functionStart, long freezingStart, long freezingCost, boolean screenFrozen) {
    }

    default java.lang.String fixSwitchingMessage(int defaultResId, java.lang.String defaultUserName, int newResId, android.content.res.Resources res) {
        return null;
    }
}
