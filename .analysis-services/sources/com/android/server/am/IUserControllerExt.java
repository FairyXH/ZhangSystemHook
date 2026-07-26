package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IUserControllerExt {
    default void startFreezingScreenInStartUser(int oldUserId, int userId) {
    }

    default android.os.Handler hookFgHandler(android.os.Handler defaultHandler) {
        return defaultHandler;
    }

    default void startUserInternalEnter(boolean foreground, int oldUserId, int newUserId, long functionStart, long freezingStart, long freezingCost, boolean screenFrozen) {
    }

    default void startUserInternalExit(long moveToForegroundCost, long updateConfigCost, int oldUserId, int newUserId, long functionStart) {
    }

    default boolean hookShowUserSwitchDialog(android.content.pm.UserInfo from, android.content.pm.UserInfo to) {
        return false;
    }

    default android.os.Handler hookGetUiHandler(android.os.Handler.Callback callback) {
        return null;
    }

    default void switchUser(boolean userSwitchUiEnabled, android.content.pm.UserInfo from, android.content.pm.UserInfo to, int callingUid) {
    }

    default void dispatchSwitch(com.android.server.am.UserState uss, int oldUserId, int newUserId) {
    }

    default void dispatchSwitchSendResult(long duration, java.lang.String serviceName, int oldUserId, int newUserId) {
    }

    default void continueUserSwitch(com.android.server.am.UserState uss, int oldUserId, int newUserId) {
    }

    default void timeoutUserSwitch(android.util.ArraySet<java.lang.String> callbacks, com.android.server.am.UserState uss, int oldUserId, int newUserId) {
    }

    default boolean checkUserIfNeed(int userId) {
        return false;
    }

    default void hookAgingUserUnlockedCompleted(int userId) {
    }

    default void hookAgingUserBoot(int userId) {
    }

    default void recordRootState() {
    }

    default int increaseCountIfNeed(int maxRunningUsers, int userId) {
        return maxRunningUsers;
    }

    default int decreaseCountIfNeed(int maxRunningUsers, int userId) {
        return maxRunningUsers;
    }

    default boolean hookHandleIncomingUser(int callingUid, int targetUserId) {
        return false;
    }

    default void setInjector(com.android.server.am.ActivityManagerService ams, java.lang.Object lock, android.util.SparseArray<com.android.server.am.UserState> startedUsers) {
    }

    default void sendOplusBootCompleteBroadcast() {
    }

    default void sendOplusBootCompleteBroadcastAsUser(int userId) {
    }

    default void triggerBootCompleteBroadcast(int userId) {
    }

    default void userStart(int userId) {
    }

    default void switchUser(int userId, int oldUserId) {
    }

    default void userRemoved(int userId) {
    }

    default void ormsUnlockUserBoost(int timeout) {
    }

    default void ormsSwitchUserBoost(int timeout) {
    }

    default void setUnlockedForDexopt() {
    }

    default void reUnlockMultiAppUser(int userId) {
    }

    default int modifyIfWorkProfileExist(int maxRunningUsers, java.util.List<android.content.pm.UserInfo> profilesToStart) {
        return maxRunningUsers;
    }

    default boolean isMultiSystemUserId(int userId) {
        return false;
    }

    default void startFreezingScreenIfNeeded(int oldUserId, int newUserId) {
    }

    default void stopFreezingScreenIfNeeded(int oldUserId, int newUserId) {
    }

    default boolean getWaitForKeyguardShown() {
        return true;
    }

    default void setWaitForKeyguardShown(boolean wait) {
    }

    public interface IStaticExt {
        default android.os.Handler hookFgHandler(android.os.Handler defaultHandler) {
            return defaultHandler;
        }

        default android.os.Handler hookGetUiHandler(android.os.Handler.Callback callback) {
            return null;
        }
    }
}
