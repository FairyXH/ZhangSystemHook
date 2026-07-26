package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IUserManagerServiceExt {
    default void init(android.content.Context context) {
    }

    default void systemReady() {
    }

    default void onCreateUserInternal(android.content.pm.UserInfo user) {
    }

    default java.lang.String[] hookDisallowedPackages(int userId, int flags, java.lang.String[] disallowedPackages) {
        return disallowedPackages;
    }

    default void createUserEnter(long functionStartTime, java.lang.String name, java.lang.String userType, int flags, boolean preCreate, int userId) {
    }

    default void createUserExit(long functionStartTime, java.lang.String name, java.lang.String userType, int flags, boolean preCreate, int userId, long totalTime, long smCreateKeyCost, long upPrepareCost, long pmCreateUserCost, long pmOnUserCreatedCost) {
    }

    default void onBeforeStartUserExit(int userId, long totalTime, long prepareUserDataTime, long reconcileAppsDataTime) {
    }

    default void onRemoveUserUnchecked(int userId) {
    }

    default void onRemoveUserState(int userId) {
    }

    default boolean isCustomUser(int flags) {
        return false;
    }

    default boolean skipCustomUserId(int i) {
        return false;
    }

    default boolean isMultiAppUser(int userId) {
        return false;
    }

    default int getNextAvailableId(int flags) {
        return 0;
    }

    default void setUserIsMultiSystem(int userId, int flags) {
    }

    default void normalizeExternalStorageData(int userId) {
    }

    default void hookUsersUpgraded(android.util.SparseArray<com.android.server.pm.UserManagerService.UserData> mUsers) {
    }

    default java.util.Set<java.lang.Integer> hookUsersIdToWrite(java.util.Set<java.lang.Integer> userIdsToWrite) {
        return userIdsToWrite;
    }

    default void onMultiAppUserRemoved(android.content.Context context, android.util.SparseBooleanArray removingUserIds, int userId) {
    }

    default boolean checkUserIfNeed(int userId) {
        return false;
    }

    default void ormsCreateUserBoost(int timeout) {
    }

    default void ensureCanCreateStudyUserOrThrowIfNeeded(int flag) throws android.os.UserManager.CheckedUserOperationException {
    }
}
