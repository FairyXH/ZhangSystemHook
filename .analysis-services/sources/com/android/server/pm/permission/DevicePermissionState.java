package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public final class DevicePermissionState {
    private final android.util.SparseArray<com.android.server.pm.permission.UserPermissionState> mUserStates = new android.util.SparseArray<>();

    public com.android.server.pm.permission.UserPermissionState getUserState(int userId) {
        return this.mUserStates.get(userId);
    }

    public com.android.server.pm.permission.UserPermissionState getOrCreateUserState(int userId) {
        com.android.server.pm.permission.UserPermissionState userState = this.mUserStates.get(userId);
        if (userState == null) {
            com.android.server.pm.permission.UserPermissionState userState2 = new com.android.server.pm.permission.UserPermissionState();
            this.mUserStates.put(userId, userState2);
            return userState2;
        }
        return userState;
    }

    public void removeUserState(int userId) {
        this.mUserStates.delete(userId);
    }

    public int[] getUserIds() {
        int userStatesSize = this.mUserStates.size();
        int[] userIds = new int[userStatesSize];
        for (int i = 0; i < userStatesSize; i++) {
            int userId = this.mUserStates.keyAt(i);
            userIds[i] = userId;
        }
        return userIds;
    }
}
