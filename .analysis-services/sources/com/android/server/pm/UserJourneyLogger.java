package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class UserJourneyLogger {
    public static final int ERROR_CODE_ABORTED = 3;
    public static final int ERROR_CODE_INCOMPLETE_OR_TIMEOUT = 2;
    public static final int ERROR_CODE_INVALID_SESSION_ID = 0;
    public static final int ERROR_CODE_NULL_USER_INFO = 4;
    public static final int ERROR_CODE_UNSPECIFIED = -1;
    public static final int ERROR_CODE_USER_ALREADY_AN_ADMIN = 5;
    public static final int ERROR_CODE_USER_IS_NOT_AN_ADMIN = 6;
    public static final int EVENT_STATE_BEGIN = 1;
    public static final int EVENT_STATE_CANCEL = 3;
    public static final int EVENT_STATE_ERROR = 4;
    public static final int EVENT_STATE_FINISH = 2;
    public static final int EVENT_STATE_NONE = 0;
    private static final int USER_ID_KEY_MULTIPLICATION = 100;
    public static final int USER_JOURNEY_GRANT_ADMIN = 7;
    public static final int USER_JOURNEY_REVOKE_ADMIN = 8;
    public static final int USER_JOURNEY_UNKNOWN = 0;
    public static final int USER_JOURNEY_USER_CREATE = 4;
    public static final int USER_JOURNEY_USER_LIFECYCLE = 9;
    public static final int USER_JOURNEY_USER_REMOVE = 6;
    public static final int USER_JOURNEY_USER_START = 3;
    public static final int USER_JOURNEY_USER_STOP = 5;
    public static final int USER_JOURNEY_USER_SWITCH_FG = 2;
    public static final int USER_JOURNEY_USER_SWITCH_UI = 1;
    public static final int USER_LIFECYCLE_EVENT_CREATE_USER = 3;
    public static final int USER_LIFECYCLE_EVENT_GRANT_ADMIN = 9;
    public static final int USER_LIFECYCLE_EVENT_REMOVE_USER = 8;
    public static final int USER_LIFECYCLE_EVENT_REVOKE_ADMIN = 10;
    public static final int USER_LIFECYCLE_EVENT_START_USER = 2;
    public static final int USER_LIFECYCLE_EVENT_STOP_USER = 7;
    public static final int USER_LIFECYCLE_EVENT_SWITCH_USER = 1;
    public static final int USER_LIFECYCLE_EVENT_UNKNOWN = 0;
    public static final int USER_LIFECYCLE_EVENT_UNLOCKED_USER = 6;
    public static final int USER_LIFECYCLE_EVENT_UNLOCKING_USER = 5;
    public static final int USER_LIFECYCLE_EVENT_USER_RUNNING_LOCKED = 4;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<com.android.server.pm.UserJourneyLogger.UserJourneySession> mUserIdToUserJourneyMap = new android.util.SparseArray<>();

    public @interface UserJourney {
    }

    public @interface UserJourneyErrorCode {
    }

    public @interface UserLifecycleEvent {
    }

    public @interface UserLifecycleEventState {
    }

    private static int journeyToEvent(int journey) {
        switch (journey) {
            case 1:
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 7;
            case 6:
                return 8;
            case 7:
                return 9;
            case 8:
                return 10;
            default:
                return 0;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:32:0x006d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int getUserTypeForStatsd(java.lang.String r10) {
        /*
            int r0 = r10.hashCode()
            r1 = 0
            r2 = 8
            r3 = 7
            r4 = 6
            r5 = 5
            r6 = 4
            r7 = 3
            r8 = 2
            r9 = 1
            switch(r0) {
                case -1309576832: goto L63;
                case -1103927049: goto L59;
                case -159818852: goto L4f;
                case 34001850: goto L45;
                case 485661392: goto L3b;
                case 942013715: goto L31;
                case 1711075452: goto L27;
                case 1765400260: goto L1d;
                case 1966344346: goto L13;
                default: goto L11;
            }
        L11:
            goto L6d
        L13:
            java.lang.String r0 = "android.os.usertype.profile.CLONE"
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L11
            r0 = r3
            goto L6e
        L1d:
            java.lang.String r0 = "android.os.usertype.full.DEMO"
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L11
            r0 = r7
            goto L6e
        L27:
            java.lang.String r0 = "android.os.usertype.full.RESTRICTED"
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L11
            r0 = r6
            goto L6e
        L31:
            java.lang.String r0 = "android.os.usertype.full.SECONDARY"
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L11
            r0 = r9
            goto L6e
        L3b:
            java.lang.String r0 = "android.os.usertype.full.SYSTEM"
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L11
            r0 = r1
            goto L6e
        L45:
            java.lang.String r0 = "android.os.usertype.system.HEADLESS"
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L11
            r0 = r4
            goto L6e
        L4f:
            java.lang.String r0 = "android.os.usertype.profile.MANAGED"
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L11
            r0 = r5
            goto L6e
        L59:
            java.lang.String r0 = "android.os.usertype.full.GUEST"
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L11
            r0 = r8
            goto L6e
        L63:
            java.lang.String r0 = "android.os.usertype.profile.PRIVATE"
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L11
            r0 = r2
            goto L6e
        L6d:
            r0 = -1
        L6e:
            switch(r0) {
                case 0: goto L7c;
                case 1: goto L7b;
                case 2: goto L7a;
                case 3: goto L79;
                case 4: goto L78;
                case 5: goto L77;
                case 6: goto L76;
                case 7: goto L75;
                case 8: goto L72;
                default: goto L71;
            }
        L71:
            return r1
        L72:
            r0 = 9
            return r0
        L75:
            return r2
        L76:
            return r3
        L77:
            return r4
        L78:
            return r5
        L79:
            return r6
        L7a:
            return r7
        L7b:
            return r8
        L7c:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.UserJourneyLogger.getUserTypeForStatsd(java.lang.String):int");
    }

    private static int errorToFinishState(int errorCode) {
        switch (errorCode) {
            case -1:
                return 2;
            case 3:
                return 3;
            default:
                return 4;
        }
    }

    public void logUserLifecycleJourneyReported(com.android.server.pm.UserJourneyLogger.UserJourneySession session, int journey, int originalUserId, int targetUserId, int userType, int userFlags, int errorCode) {
        if (session != null) {
            long elapsedTime = java.lang.System.currentTimeMillis() - session.mStartTimeInMills;
            writeUserLifecycleJourneyReported(session.mSessionId, journey, originalUserId, targetUserId, userType, userFlags, errorCode, elapsedTime);
        } else {
            writeUserLifecycleJourneyReported(-1L, journey, originalUserId, targetUserId, userType, userFlags, 0, -1L);
        }
    }

    public void writeUserLifecycleJourneyReported(long sessionId, int journey, int originalUserId, int targetUserId, int userType, int userFlags, int errorCode, long elapsedTime) {
        com.android.internal.util.FrameworkStatsLog.write(264, sessionId, journey, originalUserId, targetUserId, userType, userFlags, errorCode, elapsedTime);
    }

    public void logUserLifecycleEventOccurred(com.android.server.pm.UserJourneyLogger.UserJourneySession session, int targetUserId, int event, int state, int errorCode) {
        if (session == null) {
            writeUserLifecycleEventOccurred(-1L, targetUserId, event, 4, 0);
        } else {
            writeUserLifecycleEventOccurred(session.mSessionId, targetUserId, event, state, errorCode);
        }
    }

    public void writeUserLifecycleEventOccurred(long sessionId, int userId, int event, int state, int errorCode) {
        com.android.internal.util.FrameworkStatsLog.write(265, sessionId, userId, event, state, errorCode);
    }

    public void logUserLifecycleEvent(int userId, int event, int eventState) {
        com.android.server.pm.UserJourneyLogger.UserJourneySession userJourneySession = findUserJourneySession(userId);
        logUserLifecycleEventOccurred(userJourneySession, userId, event, eventState, -1);
    }

    private com.android.server.pm.UserJourneyLogger.UserJourneySession findUserJourneySession(int userId) {
        synchronized (this.mLock) {
            int keyMapSize = this.mUserIdToUserJourneyMap.size();
            for (int i = 0; i < keyMapSize; i++) {
                int key = this.mUserIdToUserJourneyMap.keyAt(i);
                if (key / 100 == userId) {
                    return this.mUserIdToUserJourneyMap.get(key);
                }
            }
            return null;
        }
    }

    private int getUserJourneyKey(int targetUserId, int journey) {
        return (targetUserId * 100) + journey;
    }

    public com.android.server.pm.UserJourneyLogger.UserJourneySession finishAndClearIncompleteUserJourney(int targetUserId, int journey) {
        synchronized (this.mLock) {
            int key = getUserJourneyKey(targetUserId, journey);
            com.android.server.pm.UserJourneyLogger.UserJourneySession userJourneySession = this.mUserIdToUserJourneyMap.get(key);
            if (userJourneySession != null) {
                logUserLifecycleEventOccurred(userJourneySession, targetUserId, journeyToEvent(userJourneySession.mJourney), 4, 2);
                logUserLifecycleJourneyReported(userJourneySession, journey, -1, targetUserId, getUserTypeForStatsd(""), -1, 2);
                this.mUserIdToUserJourneyMap.remove(key);
                return userJourneySession;
            }
            return null;
        }
    }

    public com.android.server.pm.UserJourneyLogger.UserJourneySession logUserJourneyFinish(int originalUserId, android.content.pm.UserInfo targetUser, int journey) {
        return logUserJourneyFinishWithError(originalUserId, targetUser, journey, -1);
    }

    public com.android.server.pm.UserJourneyLogger.UserJourneySession logUserSwitchJourneyFinish(int originalUserId, android.content.pm.UserInfo targetUser) {
        synchronized (this.mLock) {
            int key_fg = getUserJourneyKey(targetUser.id, 2);
            int key_ui = getUserJourneyKey(targetUser.id, 1);
            if (this.mUserIdToUserJourneyMap.contains(key_fg)) {
                return logUserJourneyFinish(originalUserId, targetUser, 2);
            }
            if (!this.mUserIdToUserJourneyMap.contains(key_ui)) {
                return null;
            }
            return logUserJourneyFinish(originalUserId, targetUser, 1);
        }
    }

    public com.android.server.pm.UserJourneyLogger.UserJourneySession logUserJourneyFinishWithError(int originalUserId, android.content.pm.UserInfo targetUser, int journey, int errorCode) throws java.lang.Throwable {
        if (targetUser == null) {
            return null;
        }
        synchronized (this.mLock) {
            try {
                try {
                    int state = errorToFinishState(errorCode);
                    int key = getUserJourneyKey(targetUser.id, journey);
                    com.android.server.pm.UserJourneyLogger.UserJourneySession userJourneySession = this.mUserIdToUserJourneyMap.get(key);
                    if (userJourneySession == null) {
                        return null;
                    }
                    logUserLifecycleEventOccurred(userJourneySession, targetUser.id, journeyToEvent(userJourneySession.mJourney), state, errorCode);
                    logUserLifecycleJourneyReported(userJourneySession, journey, originalUserId, targetUser.id, getUserTypeForStatsd(targetUser.userType), targetUser.flags, errorCode);
                    this.mUserIdToUserJourneyMap.remove(key);
                    return userJourneySession;
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    public com.android.server.pm.UserJourneyLogger.UserJourneySession logDelayedUserJourneyFinishWithError(int originalUserId, android.content.pm.UserInfo targetUser, int journey, int errorCode) {
        synchronized (this.mLock) {
            int key = getUserJourneyKey(targetUser.id, journey);
            com.android.server.pm.UserJourneyLogger.UserJourneySession userJourneySession = this.mUserIdToUserJourneyMap.get(key);
            if (userJourneySession != null) {
                logUserLifecycleJourneyReported(userJourneySession, journey, originalUserId, targetUser.id, getUserTypeForStatsd(targetUser.userType), targetUser.flags, errorCode);
                this.mUserIdToUserJourneyMap.remove(key);
                return userJourneySession;
            }
            return null;
        }
    }

    public com.android.server.pm.UserJourneyLogger.UserJourneySession logNullUserJourneyError(int journey, int currentUserId, int targetUserId, java.lang.String targetUserType, int targetUserFlags) {
        com.android.server.pm.UserJourneyLogger.UserJourneySession session;
        synchronized (this.mLock) {
            int key = getUserJourneyKey(targetUserId, journey);
            session = this.mUserIdToUserJourneyMap.get(key);
            logUserLifecycleEventOccurred(session, targetUserId, journeyToEvent(journey), 4, 4);
            logUserLifecycleJourneyReported(session, journey, currentUserId, targetUserId, getUserTypeForStatsd(targetUserType), targetUserFlags, 4);
            this.mUserIdToUserJourneyMap.remove(key);
        }
        return session;
    }

    public com.android.server.pm.UserJourneyLogger.UserJourneySession logUserCreateJourneyFinish(int originalUserId, android.content.pm.UserInfo targetUser) {
        synchronized (this.mLock) {
            int key = getUserJourneyKey(-1, 4);
            com.android.server.pm.UserJourneyLogger.UserJourneySession userJourneySession = this.mUserIdToUserJourneyMap.get(key);
            if (userJourneySession != null) {
                logUserLifecycleEventOccurred(userJourneySession, targetUser.id, 3, 2, -1);
                logUserLifecycleJourneyReported(userJourneySession, 4, originalUserId, targetUser.id, getUserTypeForStatsd(targetUser.userType), targetUser.flags, -1);
                this.mUserIdToUserJourneyMap.remove(key);
                return userJourneySession;
            }
            return null;
        }
    }

    public com.android.server.pm.UserJourneyLogger.UserJourneySession logUserJourneyBegin(int targetId, int journey) {
        com.android.server.pm.UserJourneyLogger.UserJourneySession userJourneySession;
        long newSessionId = java.util.concurrent.ThreadLocalRandom.current().nextLong(1L, Long.MAX_VALUE);
        synchronized (this.mLock) {
            int key = getUserJourneyKey(targetId, journey);
            userJourneySession = new com.android.server.pm.UserJourneyLogger.UserJourneySession(newSessionId, journey);
            this.mUserIdToUserJourneyMap.append(key, userJourneySession);
            logUserLifecycleEventOccurred(userJourneySession, targetId, journeyToEvent(userJourneySession.mJourney), 1, -1);
        }
        return userJourneySession;
    }

    public com.android.server.pm.UserJourneyLogger.UserJourneySession startSessionForDelayedJourney(int targetId, int journey, long startTime) {
        com.android.server.pm.UserJourneyLogger.UserJourneySession userJourneySession;
        long newSessionId = java.util.concurrent.ThreadLocalRandom.current().nextLong(1L, Long.MAX_VALUE);
        synchronized (this.mLock) {
            int key = getUserJourneyKey(targetId, journey);
            userJourneySession = new com.android.server.pm.UserJourneyLogger.UserJourneySession(newSessionId, journey, startTime);
            this.mUserIdToUserJourneyMap.append(key, userJourneySession);
        }
        return userJourneySession;
    }

    public static class UserJourneySession {
        public final int mJourney;
        public final long mSessionId;
        public final long mStartTimeInMills;

        public UserJourneySession(long sessionId, int journey) {
            this.mJourney = journey;
            this.mSessionId = sessionId;
            this.mStartTimeInMills = java.lang.System.currentTimeMillis();
        }

        public UserJourneySession(long sessionId, int journey, long startTimeInMills) {
            this.mJourney = journey;
            this.mSessionId = sessionId;
            this.mStartTimeInMills = startTimeInMills;
        }
    }
}
