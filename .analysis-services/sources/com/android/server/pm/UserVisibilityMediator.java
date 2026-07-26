package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class UserVisibilityMediator implements android.util.Dumpable {
    public static final int ALWAYS_VISIBLE_PROFILE_GROUP_ID = -1;
    static final int INITIAL_CURRENT_USER_ID = 0;
    private static final java.lang.String PREFIX_SECONDARY_DISPLAY_MAPPING = "SECONDARY_DISPLAY_MAPPING_";
    public static final int SECONDARY_DISPLAY_MAPPING_FAILED = -1;
    public static final int SECONDARY_DISPLAY_MAPPING_NEEDED = 1;
    public static final int SECONDARY_DISPLAY_MAPPING_NOT_NEEDED = 2;
    private static final boolean VERBOSE = false;
    private int mCurrentUserId;
    private final android.util.SparseIntArray mExtraDisplaysAssignedToUsers;
    private final android.os.Handler mHandler;
    final java.util.concurrent.CopyOnWriteArrayList<com.android.server.pm.UserManagerInternal.UserVisibilityListener> mListeners;
    private final java.lang.Object mLock;
    private final java.util.List<java.lang.Integer> mStartedInvisibleProfileUserIds;
    private final android.util.SparseIntArray mStartedVisibleProfileGroupIds;
    private final android.util.SparseIntArray mUsersAssignedToDisplayOnStart;
    private final boolean mVisibleBackgroundUserOnDefaultDisplayEnabled;
    private final boolean mVisibleBackgroundUsersEnabled;
    private static final java.lang.String TAG = com.android.server.pm.UserVisibilityMediator.class.getSimpleName();
    private static final boolean DBG = android.util.Log.isLoggable(TAG, 3);

    public @interface SecondaryDisplayMappingStatus {
    }

    UserVisibilityMediator(android.os.Handler handler) {
        this(android.os.UserManager.isVisibleBackgroundUsersEnabled(), android.os.UserManager.isVisibleBackgroundUsersOnDefaultDisplayEnabled(), handler);
    }

    UserVisibilityMediator(boolean visibleBackgroundUsersOnDisplaysEnabled, boolean visibleBackgroundUserOnDefaultDisplayEnabled, android.os.Handler handler) {
        this.mLock = new java.lang.Object();
        this.mCurrentUserId = 0;
        this.mStartedVisibleProfileGroupIds = new android.util.SparseIntArray();
        this.mListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.mVisibleBackgroundUsersEnabled = visibleBackgroundUsersOnDisplaysEnabled;
        if (visibleBackgroundUserOnDefaultDisplayEnabled && !visibleBackgroundUsersOnDisplaysEnabled) {
            throw new java.lang.IllegalArgumentException("Cannot have visibleBackgroundUserOnDefaultDisplayEnabled without visibleBackgroundUsersOnDisplaysEnabled");
        }
        this.mVisibleBackgroundUserOnDefaultDisplayEnabled = visibleBackgroundUserOnDefaultDisplayEnabled;
        if (this.mVisibleBackgroundUsersEnabled) {
            this.mUsersAssignedToDisplayOnStart = new android.util.SparseIntArray();
            this.mExtraDisplaysAssignedToUsers = new android.util.SparseIntArray();
        } else {
            this.mUsersAssignedToDisplayOnStart = null;
            this.mExtraDisplaysAssignedToUsers = null;
        }
        this.mStartedInvisibleProfileUserIds = DBG ? new java.util.ArrayList(4) : null;
        this.mHandler = handler;
        this.mStartedVisibleProfileGroupIds.put(0, 0);
        if (DBG) {
            com.android.server.utils.Slogf.i(TAG, "UserVisibilityMediator created with DBG on");
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00b1 A[Catch: all -> 0x0135, TryCatch #0 {, blocks: (B:7:0x0042, B:9:0x004a, B:15:0x0062, B:17:0x006a, B:19:0x007c, B:21:0x007e, B:22:0x0082, B:23:0x0085, B:34:0x00ca, B:35:0x00d8, B:36:0x00db, B:44:0x0107, B:45:0x0115, B:46:0x011a, B:37:0x00de, B:39:0x00e2, B:40:0x00ea, B:42:0x00ee, B:43:0x0101, B:24:0x0088, B:26:0x008c, B:28:0x0092, B:29:0x00ab, B:30:0x00ad, B:32:0x00b1, B:33:0x00c4, B:51:0x0133), top: B:56:0x0042 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int assignUserToDisplayOnStart(int r10, int r11, int r12, int r13, boolean r14) {
        /*
            Method dump skipped, instruction units count: 330
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.UserVisibilityMediator.assignUserToDisplayOnStart(int, int, int, int, boolean):int");
    }

    private int resolveProfileGroupId(int userId, int unResolvedProfileGroupId, boolean isAlwaysVisible) {
        if (isAlwaysVisible) {
            return -1;
        }
        if (unResolvedProfileGroupId == -10000) {
            return userId;
        }
        return unResolvedProfileGroupId;
    }

    private int getUserVisibilityOnStartLocked(int userId, int profileGroupId, int userStartMode, int displayId) {
        if (userStartMode == 2 && displayId != 0) {
            com.android.server.utils.Slogf.wtf(TAG, "cannot start user (%d) as BACKGROUND_USER on secondary display (%d) (it should be BACKGROUND_USER_VISIBLE", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
            return -1;
        }
        boolean visibleBackground = userStartMode == 3;
        if (displayId == 0 && visibleBackground) {
            if (this.mVisibleBackgroundUserOnDefaultDisplayEnabled && isCurrentUserLocked(userId)) {
                com.android.server.utils.Slogf.wtf(TAG, "trying to start current user (%d) visible in background on default display", java.lang.Integer.valueOf(userId));
                return 3;
            }
            if (!this.mVisibleBackgroundUserOnDefaultDisplayEnabled && !isProfile(userId, profileGroupId)) {
                com.android.server.utils.Slogf.wtf(TAG, "cannot start full user (%d) visible on default display", java.lang.Integer.valueOf(userId));
                return -1;
            }
        }
        boolean foreground = userStartMode == 1;
        if (displayId != 0) {
            if (foreground) {
                com.android.server.utils.Slogf.w(TAG, "getUserVisibilityOnStartLocked(%d, %d, %s, %d) failed: cannot start foreground user on secondary display", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(profileGroupId), com.android.server.pm.UserManagerInternal.userStartModeToString(userStartMode), java.lang.Integer.valueOf(displayId));
                return -1;
            }
            if (!this.mVisibleBackgroundUsersEnabled) {
                com.android.server.utils.Slogf.w(TAG, "getUserVisibilityOnStartLocked(%d, %d, %s, %d) failed: called on device that doesn't support multiple users on multiple displays", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(profileGroupId), com.android.server.pm.UserManagerInternal.userStartModeToString(userStartMode), java.lang.Integer.valueOf(displayId));
                return -1;
            }
        }
        if (isProfile(userId, profileGroupId)) {
            if (displayId != 0) {
                com.android.server.utils.Slogf.w(TAG, "canStartUserLocked(%d, %d, %s, %d) failed: cannot start profile user on secondary display", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(profileGroupId), com.android.server.pm.UserManagerInternal.userStartModeToString(userStartMode), java.lang.Integer.valueOf(displayId));
                return -1;
            }
            switch (userStartMode) {
                case 1:
                    com.android.server.utils.Slogf.w(TAG, "startUser(%d, %d, %s, %d) failed: cannot start profile user in foreground", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(profileGroupId), com.android.server.pm.UserManagerInternal.userStartModeToString(userStartMode), java.lang.Integer.valueOf(displayId));
                    return -1;
                case 2:
                    return 2;
                case 3:
                    if (isParentVisibleOnDisplay(profileGroupId, displayId)) {
                        return 1;
                    }
                    com.android.server.utils.Slogf.w(TAG, "getUserVisibilityOnStartLocked(%d, %d, %s, %d) failed: cannot start profile user visible when its parent is not visible in that display", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(profileGroupId), com.android.server.pm.UserManagerInternal.userStartModeToString(userStartMode), java.lang.Integer.valueOf(displayId));
                    return -1;
            }
        }
        if (this.mUsersAssignedToDisplayOnStart != null && isUserAssignedToDisplayOnStartLocked(userId, displayId)) {
            if (DBG) {
                com.android.server.utils.Slogf.d(TAG, "full user %d is already visible on display %d", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
            }
            return 3;
        }
        return (foreground || displayId != 0 || (visibleBackground && this.mVisibleBackgroundUserOnDefaultDisplayEnabled)) ? 1 : 2;
    }

    private int canAssignUserToDisplayLocked(int userId, int profileGroupId, int userStartMode, int displayId) {
        if (displayId == 0) {
            boolean mappingNeeded = false;
            if (this.mVisibleBackgroundUserOnDefaultDisplayEnabled && userStartMode == 3) {
                int userStartedOnDefaultDisplay = getUserStartedOnDisplay(0);
                if (userStartedOnDefaultDisplay != -10000 && userStartedOnDefaultDisplay != profileGroupId) {
                    com.android.server.utils.Slogf.w(TAG, "canAssignUserToDisplayLocked(): cannot start user %d visible on default display because user %d already did so", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(userStartedOnDefaultDisplay));
                    return -1;
                }
                mappingNeeded = true;
            }
            if (!mappingNeeded && this.mVisibleBackgroundUsersEnabled && isProfile(userId, profileGroupId)) {
                mappingNeeded = true;
            }
            if (!mappingNeeded) {
                if (DBG) {
                    com.android.server.utils.Slogf.d(TAG, "Ignoring mapping for default display for user %d starting as %s", java.lang.Integer.valueOf(userId), com.android.server.pm.UserManagerInternal.userStartModeToString(userStartMode));
                }
                return 2;
            }
        }
        if (userId == 0) {
            com.android.server.utils.Slogf.w(TAG, "Cannot assign system user to secondary display (%d)", java.lang.Integer.valueOf(displayId));
            return -1;
        }
        if (displayId == -1) {
            com.android.server.utils.Slogf.w(TAG, "Cannot assign to INVALID_DISPLAY (%d)", java.lang.Integer.valueOf(displayId));
            return -1;
        }
        if (userId == this.mCurrentUserId) {
            com.android.server.utils.Slogf.w(TAG, "Cannot assign current user (%d) to other displays", java.lang.Integer.valueOf(userId));
            return -1;
        }
        if (isProfile(userId, profileGroupId)) {
            if (displayId != 0) {
                com.android.server.utils.Slogf.w(TAG, "Profile user can only be started in the default display");
                return -1;
            }
            if (DBG) {
                com.android.server.utils.Slogf.d(TAG, "Don't need to map profile user %d to default display", java.lang.Integer.valueOf(userId));
            }
            return 2;
        }
        if (this.mUsersAssignedToDisplayOnStart == null) {
            com.android.server.utils.Slogf.wtf(TAG, "canAssignUserToDisplayLocked(%d, %d, %d, %d) is trying to check mUsersAssignedToDisplayOnStart when it's not set", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(profileGroupId), java.lang.Integer.valueOf(userStartMode), java.lang.Integer.valueOf(displayId));
            return -1;
        }
        for (int i = 0; i < this.mUsersAssignedToDisplayOnStart.size(); i++) {
            int assignedUserId = this.mUsersAssignedToDisplayOnStart.keyAt(i);
            int assignedDisplayId = this.mUsersAssignedToDisplayOnStart.valueAt(i);
            if (DBG) {
                com.android.server.utils.Slogf.d(TAG, "%d: assignedUserId=%d, assignedDisplayId=%d", java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(assignedUserId), java.lang.Integer.valueOf(assignedDisplayId));
            }
            if (displayId == assignedDisplayId) {
                com.android.server.utils.Slogf.w(TAG, "Cannot assign user %d to display %d because such display is already assigned to user %d", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(assignedUserId));
                return -1;
            }
            if (userId == assignedUserId) {
                com.android.server.utils.Slogf.w(TAG, "Cannot assign user %d to display %d because such user is as already assigned to display %d", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(assignedUserId));
                return -1;
            }
        }
        return 1;
    }

    public boolean assignUserToExtraDisplay(int userId, int displayId) {
        if (DBG) {
            com.android.server.utils.Slogf.d(TAG, "assignUserToExtraDisplay(%d, %d)", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
        }
        if (!this.mVisibleBackgroundUsersEnabled) {
            com.android.server.utils.Slogf.w(TAG, "assignUserToExtraDisplay(%d, %d): called when not supported", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
            return false;
        }
        if (displayId == -1) {
            com.android.server.utils.Slogf.w(TAG, "assignUserToExtraDisplay(%d, %d): called with INVALID_DISPLAY", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
            return false;
        }
        if (displayId == 0) {
            com.android.server.utils.Slogf.w(TAG, "assignUserToExtraDisplay(%d, %d): DEFAULT_DISPLAY is automatically assigned to current user", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
            return false;
        }
        synchronized (this.mLock) {
            if (!isUserVisible(userId)) {
                com.android.server.utils.Slogf.w(TAG, "assignUserToExtraDisplay(%d, %d): failed because user is not visible", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
                return false;
            }
            if (isStartedVisibleProfileLocked(userId)) {
                com.android.server.utils.Slogf.w(TAG, "assignUserToExtraDisplay(%d, %d): failed because user is a profile", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
                return false;
            }
            if (this.mExtraDisplaysAssignedToUsers.get(displayId, -10000) == userId) {
                com.android.server.utils.Slogf.w(TAG, "assignUserToExtraDisplay(%d, %d): failed because user is already assigned to that display", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
                return false;
            }
            int userAssignedToDisplay = getUserStartedOnDisplay(displayId);
            if (userAssignedToDisplay != -10000) {
                com.android.server.utils.Slogf.w(TAG, "assignUserToExtraDisplay(%d, %d): failed because display was assigned to user %d on start", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(userAssignedToDisplay));
                return false;
            }
            int userAssignedToDisplay2 = this.mExtraDisplaysAssignedToUsers.get(userId, -10000);
            if (userAssignedToDisplay2 != -10000) {
                com.android.server.utils.Slogf.w(TAG, "assignUserToExtraDisplay(%d, %d): failed because user %d was already assigned that extra display", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(userAssignedToDisplay2));
                return false;
            }
            if (DBG) {
                com.android.server.utils.Slogf.d(TAG, "addding %d -> %d to mExtraDisplaysAssignedToUsers", java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(userId));
            }
            this.mExtraDisplaysAssignedToUsers.put(displayId, userId);
            return true;
        }
    }

    public boolean unassignUserFromExtraDisplay(int userId, int displayId) {
        if (DBG) {
            com.android.server.utils.Slogf.d(TAG, "unassignUserFromExtraDisplay(%d, %d)", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
        }
        if (!this.mVisibleBackgroundUsersEnabled) {
            com.android.server.utils.Slogf.w(TAG, "unassignUserFromExtraDisplay(%d, %d): called when not supported", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
            return false;
        }
        synchronized (this.mLock) {
            int assignedUserId = this.mExtraDisplaysAssignedToUsers.get(displayId, -10000);
            if (assignedUserId == -10000) {
                com.android.server.utils.Slogf.w(TAG, "unassignUserFromExtraDisplay(%d, %d): not assigned to any user", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
                return false;
            }
            if (assignedUserId != userId) {
                com.android.server.utils.Slogf.w(TAG, "unassignUserFromExtraDisplay(%d, %d): was assigned to user %d", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(assignedUserId));
                return false;
            }
            if (DBG) {
                com.android.server.utils.Slogf.d(TAG, "removing %d from map", java.lang.Integer.valueOf(displayId));
            }
            this.mExtraDisplaysAssignedToUsers.delete(displayId);
            return true;
        }
    }

    public void unassignUserFromDisplayOnStop(int userId) {
        android.util.IntArray visibleUsersBefore;
        android.util.IntArray visibleUsersAfter;
        if (DBG) {
            com.android.server.utils.Slogf.d(TAG, "unassignUserFromDisplayOnStop(%d)", java.lang.Integer.valueOf(userId));
        }
        synchronized (this.mLock) {
            visibleUsersBefore = getVisibleUsers();
            unassignUserFromAllDisplaysOnStopLocked(userId);
            visibleUsersAfter = getVisibleUsers();
        }
        dispatchVisibilityChanged(visibleUsersBefore, visibleUsersAfter);
    }

    private void unassignUserFromAllDisplaysOnStopLocked(int userId) {
        if (DBG) {
            com.android.server.utils.Slogf.d(TAG, "Removing %d from mStartedVisibleProfileGroupIds (%s)", java.lang.Integer.valueOf(userId), this.mStartedVisibleProfileGroupIds);
        }
        this.mStartedVisibleProfileGroupIds.delete(userId);
        if (this.mStartedInvisibleProfileUserIds != null) {
            com.android.server.utils.Slogf.d(TAG, "Removing %d from list of invisible profiles", java.lang.Integer.valueOf(userId));
            this.mStartedInvisibleProfileUserIds.remove(java.lang.Integer.valueOf(userId));
        }
        if (!this.mVisibleBackgroundUsersEnabled) {
            return;
        }
        if (DBG) {
            com.android.server.utils.Slogf.d(TAG, "Removing user %d from mUsersOnDisplaysMap (%s)", java.lang.Integer.valueOf(userId), this.mUsersAssignedToDisplayOnStart);
        }
        this.mUsersAssignedToDisplayOnStart.delete(userId);
        for (int i = this.mExtraDisplaysAssignedToUsers.size() - 1; i >= 0; i--) {
            if (this.mExtraDisplaysAssignedToUsers.valueAt(i) == userId) {
                if (DBG) {
                    com.android.server.utils.Slogf.d(TAG, "Removing display %d from mExtraDisplaysAssignedToUsers (%s)", java.lang.Integer.valueOf(this.mExtraDisplaysAssignedToUsers.keyAt(i)), this.mExtraDisplaysAssignedToUsers);
                }
                this.mExtraDisplaysAssignedToUsers.removeAt(i);
            }
        }
    }

    public boolean isUserVisible(int userId) {
        int profileGroupId;
        if (isCurrentUserOrRunningProfileOfCurrentUser(userId)) {
            return true;
        }
        if (!this.mVisibleBackgroundUsersEnabled) {
            return false;
        }
        synchronized (this.mLock) {
            synchronized (this.mLock) {
                profileGroupId = this.mStartedVisibleProfileGroupIds.get(userId, -10000);
            }
            if (isProfile(userId, profileGroupId)) {
                return isUserAssignedToDisplayOnStartLocked(profileGroupId);
            }
            return isUserAssignedToDisplayOnStartLocked(userId);
        }
    }

    private boolean isUserAssignedToDisplayOnStartLocked(int userId) {
        return this.mUsersAssignedToDisplayOnStart.indexOfKey(userId) >= 0;
    }

    private boolean isUserAssignedToDisplayOnStartLocked(int userId, int displayId) {
        boolean z = false;
        if (this.mUsersAssignedToDisplayOnStart == null) {
            com.android.server.utils.Slogf.wtf(TAG, "isUserAssignedToDisplayOnStartLocked(%d, %d): called when mUsersAssignedToDisplayOnStart is null", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
            return false;
        }
        if (displayId != -1 && this.mUsersAssignedToDisplayOnStart.get(userId, -1) == displayId) {
            z = true;
        }
        boolean isIt = z;
        return isIt;
    }

    private boolean isParentVisibleOnDisplay(int profileGroupId, int displayId) {
        if (profileGroupId == -1) {
            return true;
        }
        return isUserVisible(profileGroupId, displayId);
    }

    public boolean isUserVisible(int userId, int displayId) {
        int profileGroupId;
        if (displayId == -1) {
            return false;
        }
        if (isCurrentUserOrRunningProfileOfCurrentUser(userId) && (displayId == 0 || !this.mVisibleBackgroundUsersEnabled)) {
            return true;
        }
        if (!this.mVisibleBackgroundUsersEnabled) {
            if (DBG) {
                com.android.server.utils.Slogf.d(TAG, "isUserVisible(%d, %d): returning false as device does not support visible background users", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(displayId));
            }
            return false;
        }
        synchronized (this.mLock) {
            synchronized (this.mLock) {
                profileGroupId = this.mStartedVisibleProfileGroupIds.get(userId, -10000);
            }
            if (isProfile(userId, profileGroupId)) {
                return isFullUserVisibleOnBackgroundLocked(profileGroupId, displayId);
            }
            return isFullUserVisibleOnBackgroundLocked(userId, displayId);
        }
    }

    private boolean isFullUserVisibleOnBackgroundLocked(int userId, int displayId) {
        return this.mUsersAssignedToDisplayOnStart.get(userId, -1) == displayId || this.mExtraDisplaysAssignedToUsers.get(displayId, -10000) == userId;
    }

    public int getMainDisplayAssignedToUser(int userId) {
        int i;
        int userStartedOnDefaultDisplay;
        if (isCurrentUserOrRunningProfileOfCurrentUser(userId)) {
            if (this.mVisibleBackgroundUserOnDefaultDisplayEnabled) {
                synchronized (this.mLock) {
                    userStartedOnDefaultDisplay = getUserStartedOnDisplay(0);
                }
                if (userStartedOnDefaultDisplay != -10000) {
                    if (DBG) {
                        com.android.server.utils.Slogf.d(TAG, "getMainDisplayAssignedToUser(%d): returning INVALID_DISPLAY for current user user %d was started on DEFAULT_DISPLAY", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(userStartedOnDefaultDisplay));
                    }
                    return -1;
                }
            }
            return 0;
        }
        if (!this.mVisibleBackgroundUsersEnabled) {
            return -1;
        }
        synchronized (this.mLock) {
            i = this.mUsersAssignedToDisplayOnStart.get(userId, -1);
        }
        return i;
    }

    public int[] getDisplaysAssignedToUser(int userId) {
        int mainDisplayId = getMainDisplayAssignedToUser(userId);
        if (mainDisplayId == -1) {
            if (DBG) {
                com.android.server.utils.Slogf.d(TAG, "getDisplaysAssignedToUser(): returning null because there is no display assigned to user %d", java.lang.Integer.valueOf(userId));
                return null;
            }
            return null;
        }
        synchronized (this.mLock) {
            if (this.mExtraDisplaysAssignedToUsers != null && this.mExtraDisplaysAssignedToUsers.size() != 0) {
                int[] displayIds = new int[this.mExtraDisplaysAssignedToUsers.size() + 1];
                int count = 0 + 1;
                displayIds[0] = mainDisplayId;
                for (int i = 0; i < this.mExtraDisplaysAssignedToUsers.size(); i++) {
                    if (this.mExtraDisplaysAssignedToUsers.valueAt(i) == userId) {
                        displayIds[count] = this.mExtraDisplaysAssignedToUsers.keyAt(i);
                        count++;
                    }
                }
                int i2 = displayIds.length;
                if (i2 == count) {
                    return displayIds;
                }
                int[] results = new int[count];
                java.lang.System.arraycopy(displayIds, 0, results, 0, count);
                return results;
            }
            return new int[]{mainDisplayId};
        }
    }

    public int getUserAssignedToDisplay(int displayId) {
        return getUserAssignedToDisplay(displayId, true);
    }

    private int getUserStartedOnDisplay(int displayId) {
        return getUserAssignedToDisplay(displayId, false);
    }

    private int getUserAssignedToDisplay(int displayId, boolean returnCurrentUserByDefault) {
        if (returnCurrentUserByDefault && ((displayId == 0 && !this.mVisibleBackgroundUserOnDefaultDisplayEnabled) || !this.mVisibleBackgroundUsersEnabled)) {
            return getCurrentUserId();
        }
        synchronized (this.mLock) {
            for (int i = 0; i < this.mUsersAssignedToDisplayOnStart.size(); i++) {
                if (this.mUsersAssignedToDisplayOnStart.valueAt(i) == displayId) {
                    int userId = this.mUsersAssignedToDisplayOnStart.keyAt(i);
                    if (!isStartedVisibleProfileLocked(userId)) {
                        return userId;
                    }
                    if (DBG) {
                        com.android.server.utils.Slogf.d(TAG, "getUserAssignedToDisplay(%d): skipping user %d because it's a profile", java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(userId));
                    }
                }
            }
            if (!returnCurrentUserByDefault) {
                if (DBG) {
                    com.android.server.utils.Slogf.d(TAG, "getUserAssignedToDisplay(%d): no user assigned to display, returning USER_NULL instead", java.lang.Integer.valueOf(displayId));
                    return -10000;
                }
                return -10000;
            }
            int currentUserId = getCurrentUserId();
            if (DBG) {
                com.android.server.utils.Slogf.d(TAG, "getUserAssignedToDisplay(%d): no user assigned to display, returning current user (%d) instead", java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(currentUserId));
            }
            return currentUserId;
        }
    }

    public android.util.IntArray getVisibleUsers() {
        android.util.IntArray visibleUsers = new android.util.IntArray();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mStartedVisibleProfileGroupIds.size(); i++) {
                int userId = this.mStartedVisibleProfileGroupIds.keyAt(i);
                if (isUserVisible(userId)) {
                    visibleUsers.add(userId);
                }
            }
        }
        return visibleUsers;
    }

    public void addListener(com.android.server.pm.UserManagerInternal.UserVisibilityListener listener) {
        if (DBG) {
            com.android.server.utils.Slogf.d(TAG, "adding listener %s", listener);
        }
        synchronized (this.mLock) {
            this.mListeners.add(listener);
        }
    }

    public void removeListener(com.android.server.pm.UserManagerInternal.UserVisibilityListener listener) {
        if (DBG) {
            com.android.server.utils.Slogf.d(TAG, "removing listener %s", listener);
        }
        synchronized (this.mLock) {
            this.mListeners.remove(listener);
        }
    }

    void onSystemUserVisibilityChanged(boolean visible) {
        dispatchVisibilityChanged(this.mListeners, 0, visible);
    }

    private void dispatchVisibilityChanged(android.util.IntArray visibleUsersBefore, android.util.IntArray visibleUsersAfter) {
        if (visibleUsersBefore == null) {
            if (DBG) {
                com.android.server.utils.Slogf.d(TAG, "dispatchVisibilityChanged(): ignoring, no listeners");
                return;
            }
            return;
        }
        java.util.concurrent.CopyOnWriteArrayList<com.android.server.pm.UserManagerInternal.UserVisibilityListener> listeners = this.mListeners;
        if (DBG) {
            com.android.server.utils.Slogf.d(TAG, "dispatchVisibilityChanged(): visibleUsersBefore=%s, visibleUsersAfter=%s, %d listeners (%s)", visibleUsersBefore, visibleUsersAfter, java.lang.Integer.valueOf(listeners.size()), listeners);
        }
        for (int i = 0; i < visibleUsersBefore.size(); i++) {
            int userId = visibleUsersBefore.get(i);
            if (visibleUsersAfter.indexOf(userId) == -1) {
                dispatchVisibilityChanged(listeners, userId, false);
            }
        }
        for (int i2 = 0; i2 < visibleUsersAfter.size(); i2++) {
            int userId2 = visibleUsersAfter.get(i2);
            if (visibleUsersBefore.indexOf(userId2) == -1) {
                dispatchVisibilityChanged(listeners, userId2, true);
            }
        }
    }

    private void dispatchVisibilityChanged(java.util.concurrent.CopyOnWriteArrayList<com.android.server.pm.UserManagerInternal.UserVisibilityListener> copyOnWriteArrayList, final int i, final boolean z) {
        android.util.EventLog.writeEvent(com.android.server.am.EventLogTags.UM_USER_VISIBILITY_CHANGED, java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(z ? 1 : 0));
        if (DBG) {
            com.android.server.utils.Slogf.d(TAG, "dispatchVisibilityChanged(%d -> %b): sending to %d listeners", java.lang.Integer.valueOf(i), java.lang.Boolean.valueOf(z), java.lang.Integer.valueOf(copyOnWriteArrayList.size()));
        }
        for (int i2 = 0; i2 < this.mListeners.size(); i2++) {
            final com.android.server.pm.UserManagerInternal.UserVisibilityListener userVisibilityListener = this.mListeners.get(i2);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.UserVisibilityMediator$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    userVisibilityListener.onUserVisibilityChanged(i, z);
                }
            });
        }
    }

    private void dump(android.util.IndentingPrintWriter ipw) {
        ipw.println("UserVisibilityMediator");
        ipw.increaseIndent();
        ipw.print("DBG: ");
        ipw.println(DBG);
        synchronized (this.mLock) {
            ipw.print("Current user id: ");
            ipw.println(this.mCurrentUserId);
            ipw.print("Visible users: ");
            ipw.println(getVisibleUsers());
            dumpSparseIntArray(ipw, this.mStartedVisibleProfileGroupIds, "started visible user / profile group", "u", "pg");
            if (this.mStartedInvisibleProfileUserIds != null) {
                ipw.print("Profiles started invisible: ");
                ipw.println(this.mStartedInvisibleProfileUserIds);
            }
            ipw.print("Supports visible background users on displays: ");
            ipw.println(this.mVisibleBackgroundUsersEnabled);
            ipw.print("Supports visible background users on default display: ");
            ipw.println(this.mVisibleBackgroundUserOnDefaultDisplayEnabled);
            dumpSparseIntArray(ipw, this.mUsersAssignedToDisplayOnStart, "user / display", "u", "d");
            dumpSparseIntArray(ipw, this.mExtraDisplaysAssignedToUsers, "extra display / user", "d", "u");
            int numberListeners = this.mListeners.size();
            ipw.print("Number of listeners: ");
            ipw.println(numberListeners);
            if (numberListeners > 0) {
                ipw.increaseIndent();
                for (int i = 0; i < numberListeners; i++) {
                    ipw.print(i);
                    ipw.print(": ");
                    ipw.println(this.mListeners.get(i));
                }
                ipw.decreaseIndent();
            }
        }
        ipw.decreaseIndent();
    }

    private static void dumpSparseIntArray(android.util.IndentingPrintWriter ipw, android.util.SparseIntArray array, java.lang.String arrayDescription, java.lang.String keyName, java.lang.String valueName) {
        if (array == null) {
            ipw.print("No ");
            ipw.print(arrayDescription);
            ipw.println(" mappings");
            return;
        }
        ipw.print("Number of ");
        ipw.print(arrayDescription);
        ipw.print(" mappings: ");
        ipw.println(array.size());
        if (array.size() <= 0) {
            return;
        }
        ipw.increaseIndent();
        for (int i = 0; i < array.size(); i++) {
            ipw.print(keyName);
            ipw.print(':');
            ipw.print(array.keyAt(i));
            ipw.print(" -> ");
            ipw.print(valueName);
            ipw.print(':');
            ipw.println(array.valueAt(i));
        }
        ipw.decreaseIndent();
    }

    @Override // android.util.Dumpable
    public void dump(java.io.PrintWriter pw, java.lang.String[] args) {
        if (pw instanceof android.util.IndentingPrintWriter) {
            dump((android.util.IndentingPrintWriter) pw);
        } else {
            dump(new android.util.IndentingPrintWriter(pw));
        }
    }

    private static boolean isSpecialUserId(int userId) {
        switch (userId) {
            case -10000:
            case -3:
            case -2:
            case -1:
                return true;
            default:
                return false;
        }
    }

    private static boolean isProfile(int userId, int profileGroupId) {
        return (profileGroupId == -10000 || profileGroupId == userId) ? false : true;
    }

    private int getCurrentUserId() {
        int i;
        synchronized (this.mLock) {
            i = this.mCurrentUserId;
        }
        return i;
    }

    private boolean isCurrentUserLocked(int userId) {
        return (userId == -10000 || this.mCurrentUserId == -10000 || this.mCurrentUserId != userId) ? false : true;
    }

    private boolean isCurrentUserOrRunningProfileOfCurrentUser(int userId) {
        synchronized (this.mLock) {
            if (userId != -10000) {
                if (this.mCurrentUserId != -10000) {
                    if (this.mCurrentUserId == userId) {
                        return true;
                    }
                    int profileGroupId = this.mStartedVisibleProfileGroupIds.get(userId, -10000);
                    return profileGroupId == this.mCurrentUserId || profileGroupId == -1;
                }
            }
            return false;
        }
    }

    private boolean isStartedVisibleProfileLocked(int userId) {
        int profileGroupId = this.mStartedVisibleProfileGroupIds.get(userId, -10000);
        return isProfile(userId, profileGroupId);
    }

    private void validateUserStartMode(int userStartMode) {
        switch (userStartMode) {
            case 1:
            case 2:
            case 3:
                return;
            default:
                throw new java.lang.IllegalArgumentException("Invalid user start mode: " + userStartMode);
        }
    }

    private static java.lang.String secondaryDisplayMappingStatusToString(int status) {
        return android.util.DebugUtils.constantToString(com.android.server.pm.UserVisibilityMediator.class, PREFIX_SECONDARY_DISPLAY_MAPPING, status);
    }
}
