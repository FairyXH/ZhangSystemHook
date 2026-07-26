package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public final class LegacyPermissionState {
    private final android.util.SparseArray<com.android.server.pm.permission.LegacyPermissionState.UserState> mUserStates = new android.util.SparseArray<>();
    private final android.util.SparseBooleanArray mMissing = new android.util.SparseBooleanArray();

    public void copyFrom(com.android.server.pm.permission.LegacyPermissionState other) {
        if (other == this) {
            return;
        }
        this.mUserStates.clear();
        int userStatesSize = other.mUserStates.size();
        for (int i = 0; i < userStatesSize; i++) {
            this.mUserStates.put(other.mUserStates.keyAt(i), new com.android.server.pm.permission.LegacyPermissionState.UserState(other.mUserStates.valueAt(i)));
        }
        this.mMissing.clear();
        int missingSize = other.mMissing.size();
        for (int i2 = 0; i2 < missingSize; i2++) {
            this.mMissing.put(other.mMissing.keyAt(i2), other.mMissing.valueAt(i2));
        }
    }

    public void reset() {
        this.mUserStates.clear();
        this.mMissing.clear();
    }

    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        com.android.server.pm.permission.LegacyPermissionState other = (com.android.server.pm.permission.LegacyPermissionState) object;
        int userStatesSize = this.mUserStates.size();
        if (userStatesSize != other.mUserStates.size()) {
            return false;
        }
        for (int i = 0; i < userStatesSize; i++) {
            int userId = this.mUserStates.keyAt(i);
            if (!java.util.Objects.equals(this.mUserStates.get(userId), other.mUserStates.get(userId))) {
                return false;
            }
        }
        return java.util.Objects.equals(this.mMissing, other.mMissing);
    }

    public com.android.server.pm.permission.LegacyPermissionState.PermissionState getPermissionState(java.lang.String permissionName, int userId) {
        checkUserId(userId);
        com.android.server.pm.permission.LegacyPermissionState.UserState userState = this.mUserStates.get(userId);
        if (userState == null) {
            return null;
        }
        return userState.getPermissionState(permissionName);
    }

    public void putPermissionState(com.android.server.pm.permission.LegacyPermissionState.PermissionState permissionState, int userId) {
        checkUserId(userId);
        com.android.server.pm.permission.LegacyPermissionState.UserState userState = this.mUserStates.get(userId);
        if (userState == null) {
            userState = new com.android.server.pm.permission.LegacyPermissionState.UserState();
            this.mUserStates.put(userId, userState);
        }
        userState.putPermissionState(permissionState);
    }

    public boolean hasPermissionState(java.util.Collection<java.lang.String> permissionNames) {
        int userStatesSize = this.mUserStates.size();
        for (int i = 0; i < userStatesSize; i++) {
            com.android.server.pm.permission.LegacyPermissionState.UserState userState = this.mUserStates.valueAt(i);
            for (java.lang.String permissionName : permissionNames) {
                if (userState.getPermissionState(permissionName) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public java.util.Collection<com.android.server.pm.permission.LegacyPermissionState.PermissionState> getPermissionStates(int userId) {
        checkUserId(userId);
        com.android.server.pm.permission.LegacyPermissionState.UserState userState = this.mUserStates.get(userId);
        if (userState == null) {
            return java.util.Collections.emptyList();
        }
        return userState.getPermissionStates();
    }

    public boolean isMissing(int userId) {
        checkUserId(userId);
        return this.mMissing.get(userId);
    }

    public void setMissing(boolean missing, int userId) {
        checkUserId(userId);
        if (missing) {
            this.mMissing.put(userId, true);
        } else {
            this.mMissing.delete(userId);
        }
    }

    private static void checkUserId(int userId) {
        if (userId < 0) {
            throw new java.lang.IllegalArgumentException("Invalid user ID " + userId);
        }
    }

    private static final class UserState {
        private final android.util.ArrayMap<java.lang.String, com.android.server.pm.permission.LegacyPermissionState.PermissionState> mPermissionStates = new android.util.ArrayMap<>();

        public UserState() {
        }

        public UserState(com.android.server.pm.permission.LegacyPermissionState.UserState other) {
            int permissionStatesSize = other.mPermissionStates.size();
            for (int i = 0; i < permissionStatesSize; i++) {
                this.mPermissionStates.put(other.mPermissionStates.keyAt(i), new com.android.server.pm.permission.LegacyPermissionState.PermissionState(other.mPermissionStates.valueAt(i)));
            }
        }

        public com.android.server.pm.permission.LegacyPermissionState.PermissionState getPermissionState(java.lang.String permissionName) {
            return this.mPermissionStates.get(permissionName);
        }

        public void putPermissionState(com.android.server.pm.permission.LegacyPermissionState.PermissionState permissionState) {
            this.mPermissionStates.put(permissionState.getName(), permissionState);
        }

        public java.util.Collection<com.android.server.pm.permission.LegacyPermissionState.PermissionState> getPermissionStates() {
            return java.util.Collections.unmodifiableCollection(this.mPermissionStates.values());
        }
    }

    public static final class PermissionState {
        private final int mFlags;
        private final boolean mGranted;
        private final java.lang.String mName;
        private final boolean mRuntime;

        public PermissionState(java.lang.String name, boolean runtime, boolean granted, int flags) {
            this.mName = name;
            this.mRuntime = runtime;
            this.mGranted = granted;
            this.mFlags = flags;
        }

        private PermissionState(com.android.server.pm.permission.LegacyPermissionState.PermissionState other) {
            this.mName = other.mName;
            this.mRuntime = other.mRuntime;
            this.mGranted = other.mGranted;
            this.mFlags = other.mFlags;
        }

        public java.lang.String getName() {
            return this.mName;
        }

        public boolean isRuntime() {
            return this.mRuntime;
        }

        public boolean isGranted() {
            return this.mGranted;
        }

        public int getFlags() {
            return this.mFlags;
        }

        public boolean equals(java.lang.Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            com.android.server.pm.permission.LegacyPermissionState.PermissionState that = (com.android.server.pm.permission.LegacyPermissionState.PermissionState) object;
            if (this.mRuntime == that.mRuntime && this.mGranted == that.mGranted && this.mFlags == that.mFlags && java.util.Objects.equals(this.mName, that.mName)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.mName, java.lang.Boolean.valueOf(this.mRuntime), java.lang.Boolean.valueOf(this.mGranted), java.lang.Integer.valueOf(this.mFlags));
        }
    }
}
