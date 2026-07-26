package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class PreferencesHelper implements com.android.server.notification.RankingConfig {
    private static final java.lang.String ATT_ALLOW_BUBBLE = "allow_bubble";
    private static final java.lang.String ATT_APP_USER_LOCKED_FIELDS = "app_user_locked_fields";
    private static final java.lang.String ATT_CREATION_TIME = "creation_time";
    private static final java.lang.String ATT_ENABLED = "enabled";
    private static final java.lang.String ATT_HIDE_SILENT = "hide_gentle";
    private static final java.lang.String ATT_ID = "id";
    private static final java.lang.String ATT_IMPORTANCE = "importance";
    private static final java.lang.String ATT_NAME = "name";
    private static final java.lang.String ATT_PRIORITY = "priority";
    private static final java.lang.String ATT_SENT_INVALID_MESSAGE = "sent_invalid_msg";
    private static final java.lang.String ATT_SENT_VALID_BUBBLE = "sent_valid_bubble";
    private static final java.lang.String ATT_SENT_VALID_MESSAGE = "sent_valid_msg";
    private static final java.lang.String ATT_SHOW_BADGE = "show_badge";
    private static final java.lang.String ATT_UID = "uid";
    private static final java.lang.String ATT_USERID = "userid";
    private static final java.lang.String ATT_USER_DEMOTED_INVALID_MSG_APP = "user_demote_msg_app";
    private static final java.lang.String ATT_VERSION = "version";
    private static final java.lang.String ATT_VISIBILITY = "visibility";
    private static final boolean DEFAULT_APP_LOCKED_IMPORTANCE = false;
    static final boolean DEFAULT_BUBBLES_ENABLED = true;
    static final int DEFAULT_BUBBLE_PREFERENCE = 0;
    static final boolean DEFAULT_HIDE_SILENT_STATUS_BAR_ICONS = false;
    private static final int DEFAULT_IMPORTANCE = -1000;
    private static final int DEFAULT_LOCKED_APP_FIELDS = 0;
    private static final int DEFAULT_PRIORITY = 0;
    private static final boolean DEFAULT_SHOW_BADGE = true;
    private static final int DEFAULT_VISIBILITY = -1000;
    static final int NOTIFICATION_CHANNEL_COUNT_LIMIT = 5000;
    private static final int NOTIFICATION_CHANNEL_DELETION_RETENTION_DAYS = 30;
    static final int NOTIFICATION_CHANNEL_GROUP_COUNT_LIMIT = 6000;
    private static final int NOTIFICATION_CHANNEL_GROUP_PULL_LIMIT = 1000;
    private static final int NOTIFICATION_CHANNEL_PULL_LIMIT = 2000;
    private static final int NOTIFICATION_PREFERENCES_PULL_LIMIT = 1000;
    private static final int NOTIFICATION_UPDATE_LOG_SUBTYPE_FROM_APP = 0;
    private static final int NOTIFICATION_UPDATE_LOG_SUBTYPE_FROM_USER = 1;
    private static final long PREF_GRACE_PERIOD_MS = java.time.Duration.ofDays(2).toMillis();
    private static final java.lang.String TAG = "NotificationPrefHelper";
    private static final java.lang.String TAG_CHANNEL = "channel";
    private static final java.lang.String TAG_DELEGATE = "delegate";
    private static final java.lang.String TAG_GROUP = "channelGroup";
    private static final java.lang.String TAG_PACKAGE = "package";
    static final java.lang.String TAG_RANKING = "ranking";
    private static final java.lang.String TAG_STATUS_ICONS = "silent_status_icons";
    static final int UNKNOWN_UID = -10000;
    private static final int XML_VERSION_BUBBLES_UPGRADE = 1;
    private static final int XML_VERSION_NOTIF_PERMISSION = 3;
    private static final int XML_VERSION_REVIEW_PERMISSIONS_NOTIFICATION = 4;
    private final android.app.AppOpsManager mAppOps;
    private android.util.SparseBooleanArray mBadgingEnabled;
    private android.util.SparseBooleanArray mBubblesEnabled;
    java.time.Clock mClock;
    private final android.content.Context mContext;
    private boolean mCurrentUserHasChannelsBypassingDnd;
    private boolean mIsMediaNotificationFilteringEnabled;
    private android.util.SparseBooleanArray mLockScreenPrivateNotifications;
    private android.util.SparseBooleanArray mLockScreenShowNotifications;
    private final com.android.server.notification.NotificationChannelLogger mNotificationChannelLogger;
    private final com.android.server.notification.PermissionHelper mPermissionHelper;
    private final android.permission.PermissionManager mPermissionManager;
    private final android.content.pm.PackageManager mPm;
    private com.android.server.notification.IPreferencesHelperExt mPreferencesHelperExt;
    private final com.android.server.notification.RankingHandler mRankingHandler;
    private final boolean mShowReviewPermissionsNotification;
    private final com.android.server.notification.ManagedServices.UserProfiles mUserProfiles;
    private final com.android.server.notification.ZenModeHelper mZenModeHelper;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.ArrayMap<java.lang.String, com.android.server.notification.PreferencesHelper.PackagePreferences> mPackagePreferences = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, com.android.server.notification.PreferencesHelper.PackagePreferences> mRestoredWithoutUids = new android.util.ArrayMap<>();
    private boolean mHideSilentStatusBarIcons = false;
    private com.android.server.notification.IPreferencesHelperWrapper mPHWrapper = new com.android.server.notification.PreferencesHelper.PreferencesHelperWrapper();
    private final int XML_VERSION = 4;

    public @interface LockableAppFields {
        public static final int USER_LOCKED_BUBBLE = 2;
        public static final int USER_LOCKED_IMPORTANCE = 1;
    }

    public PreferencesHelper(android.content.Context context, android.content.pm.PackageManager pm, com.android.server.notification.RankingHandler rankingHandler, com.android.server.notification.ZenModeHelper zenHelper, com.android.server.notification.PermissionHelper permHelper, android.permission.PermissionManager permManager, com.android.server.notification.NotificationChannelLogger notificationChannelLogger, android.app.AppOpsManager appOpsManager, com.android.server.notification.ManagedServices.UserProfiles userProfiles, boolean showReviewPermissionsNotification, java.time.Clock clock) {
        this.mPreferencesHelperExt = (com.android.server.notification.IPreferencesHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.notification.IPreferencesHelperExt.class).base(this).create();
        this.mPreferencesHelperExt = (com.android.server.notification.IPreferencesHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.notification.IPreferencesHelperExt.class).base(this).create();
        this.mContext = context;
        this.mZenModeHelper = zenHelper;
        this.mRankingHandler = rankingHandler;
        this.mPermissionHelper = permHelper;
        this.mPermissionManager = permManager;
        this.mPm = pm;
        this.mNotificationChannelLogger = notificationChannelLogger;
        this.mAppOps = appOpsManager;
        this.mUserProfiles = userProfiles;
        this.mShowReviewPermissionsNotification = showReviewPermissionsNotification;
        this.mIsMediaNotificationFilteringEnabled = context.getResources().getBoolean(android.R.bool.config_notificationHeaderClickableForExpand);
        this.mClock = clock;
        updateBadgingEnabled();
        updateBubblesEnabled();
        updateMediaNotificationFilteringEnabled();
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x006d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void readXml(com.android.modules.utils.TypedXmlPullParser r20, boolean r21, int r22) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instruction units count: 255
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.PreferencesHelper.readXml(com.android.modules.utils.TypedXmlPullParser, boolean, int):void");
    }

    private void restorePackage(com.android.modules.utils.TypedXmlPullParser parser, boolean forRestore, int userId, java.lang.String name, boolean upgradeForBubbles, boolean migrateToPermission) {
        java.lang.String str;
        boolean hasSAWPermission;
        boolean skipWarningLogged;
        com.android.server.notification.PreferencesHelper.PackagePreferences r;
        boolean z;
        java.lang.String str2;
        boolean z2;
        try {
            int uid = parser.getAttributeInt((java.lang.String) null, "uid", -10000);
            if (forRestore) {
                try {
                    try {
                        uid = this.mPm.getPackageUidAsUser(name, userId);
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    } catch (java.lang.Exception e2) {
                        e = e2;
                        str = TAG;
                        android.util.Slog.w(str, "Failed to restore pkg", e);
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e3) {
                } catch (java.lang.Exception e4) {
                    e = e4;
                    str = TAG;
                }
            }
            java.lang.String name2 = this.mPHWrapper.getPreferencesHelperExt().getMigMappingPkgName(this.mContext, false, name);
            try {
                int uid2 = this.mPHWrapper.getPreferencesHelperExt().getMigMappingPkgUid(this.mContext, name2, uid);
                boolean skipGroupWarningLogged = false;
                if (!upgradeForBubbles || uid2 == -10000) {
                    hasSAWPermission = false;
                } else {
                    try {
                        boolean hasSAWPermission2 = this.mAppOps.noteOpNoThrow(24, uid2, name2, (java.lang.String) null, "check-notif-bubble") == 0;
                        hasSAWPermission = hasSAWPermission2;
                    } catch (java.lang.Exception e5) {
                        e = e5;
                        str = TAG;
                        android.util.Slog.w(str, "Failed to restore pkg", e);
                    }
                }
                int bubblePref = hasSAWPermission ? 1 : parser.getAttributeInt((java.lang.String) null, ATT_ALLOW_BUBBLE, 0);
                int appImportance = parser.getAttributeInt((java.lang.String) null, ATT_IMPORTANCE, -1000);
                int fixedUserId = userId;
                if (com.android.server.notification.Flags.persistIncompleteRestoreData() && !forRestore && uid2 == -10000) {
                    skipWarningLogged = false;
                    fixedUserId = parser.getAttributeInt((java.lang.String) null, ATT_USERID, 0);
                } else {
                    skipWarningLogged = false;
                }
                int attributeInt = parser.getAttributeInt((java.lang.String) null, ATT_PRIORITY, 0);
                int attributeInt2 = parser.getAttributeInt((java.lang.String) null, ATT_VISIBILITY, -1000);
                boolean attributeBoolean = parser.getAttributeBoolean((java.lang.String) null, ATT_SHOW_BADGE, true);
                long attributeLong = parser.getAttributeLong((java.lang.String) null, ATT_CREATION_TIME, this.mClock.millis());
                int i = fixedUserId;
                java.lang.String str3 = TAG;
                java.lang.String str4 = "uid";
                try {
                    r = getOrCreatePackagePreferencesLocked(name2, i, uid2, appImportance, attributeInt, attributeInt2, attributeBoolean, bubblePref, attributeLong);
                    r.bubblePreference = bubblePref;
                    r.priority = parser.getAttributeInt((java.lang.String) null, ATT_PRIORITY, 0);
                    r.visibility = parser.getAttributeInt((java.lang.String) null, ATT_VISIBILITY, -1000);
                    r.showBadge = parser.getAttributeBoolean((java.lang.String) null, ATT_SHOW_BADGE, true);
                    r.lockedAppFields = parser.getAttributeInt((java.lang.String) null, ATT_APP_USER_LOCKED_FIELDS, 0);
                    r.hasSentInvalidMessage = parser.getAttributeBoolean((java.lang.String) null, ATT_SENT_INVALID_MESSAGE, false);
                    r.hasSentValidMessage = parser.getAttributeBoolean((java.lang.String) null, ATT_SENT_VALID_MESSAGE, false);
                    r.userDemotedMsgApp = parser.getAttributeBoolean((java.lang.String) null, ATT_USER_DEMOTED_INVALID_MSG_APP, false);
                    r.hasSentValidBubble = parser.getAttributeBoolean((java.lang.String) null, ATT_SENT_VALID_BUBBLE, false);
                } catch (java.lang.Exception e6) {
                    e = e6;
                }
                try {
                    this.mPHWrapper.getPreferencesHelperExt().readXml(r.mPPWrapper.getPackagePreferencesExt(), parser);
                    int innerDepth = parser.getDepth();
                    while (true) {
                        int type = parser.next();
                        if (type == 1) {
                            z = true;
                            str = str3;
                            break;
                        }
                        if (type == 3 && parser.getDepth() <= innerDepth) {
                            z = true;
                            str = str3;
                            break;
                        }
                        if (type == 3) {
                            str = str3;
                            str2 = str4;
                        } else if (type == 4) {
                            str = str3;
                            str2 = str4;
                        } else {
                            java.lang.String tagName = parser.getName();
                            if (TAG_GROUP.equals(tagName)) {
                                try {
                                    if (r.groups.size() < 6000) {
                                        str = str3;
                                        java.lang.String id = parser.getAttributeValue((java.lang.String) null, ATT_ID);
                                        java.lang.CharSequence groupName = parser.getAttributeValue((java.lang.String) null, "name");
                                        if (!android.text.TextUtils.isEmpty(id)) {
                                            android.app.NotificationChannelGroup group = new android.app.NotificationChannelGroup(id, groupName);
                                            group.populateFromXml(parser);
                                            r.groups.put(id, group);
                                        }
                                    } else if (skipGroupWarningLogged) {
                                        str = str3;
                                        str2 = str4;
                                    } else {
                                        str = str3;
                                        try {
                                            android.util.Slog.w(str, "Skipping further groups for " + r.pkg);
                                            skipGroupWarningLogged = true;
                                            str3 = str;
                                        } catch (java.lang.Exception e7) {
                                            e = e7;
                                            android.util.Slog.w(str, "Failed to restore pkg", e);
                                        }
                                    }
                                } catch (java.lang.Exception e8) {
                                    e = e8;
                                    str = str3;
                                }
                            } else {
                                str = str3;
                            }
                            if (!TAG_CHANNEL.equals(tagName)) {
                                z2 = true;
                            } else if (r.channels.size() < 5000) {
                                z2 = true;
                                try {
                                    restoreChannel(parser, forRestore, r);
                                } catch (java.lang.Exception e9) {
                                    e = e9;
                                    android.util.Slog.w(str, "Failed to restore pkg", e);
                                }
                            } else if (skipWarningLogged) {
                                str2 = str4;
                            } else {
                                android.util.Slog.w(str, "Skipping further channels for " + r.pkg);
                                skipWarningLogged = true;
                                str3 = str;
                            }
                            if (TAG_DELEGATE.equals(tagName)) {
                                str2 = str4;
                                int delegateId = parser.getAttributeInt((java.lang.String) null, str2, -10000);
                                java.lang.String delegateName = com.android.internal.util.XmlUtils.readStringAttribute(parser, "name");
                                boolean delegateEnabled = parser.getAttributeBoolean((java.lang.String) null, "enabled", z2);
                                com.android.server.notification.PreferencesHelper.Delegate d = (delegateId == -10000 || android.text.TextUtils.isEmpty(delegateName)) ? null : new com.android.server.notification.PreferencesHelper.Delegate(delegateName, delegateId, delegateEnabled);
                                r.delegate = d;
                            } else {
                                str2 = str4;
                            }
                        }
                        str3 = str;
                        str4 = str2;
                    }
                    try {
                        deleteDefaultChannelIfNeededLocked(r);
                    } catch (android.content.pm.PackageManager.NameNotFoundException e10) {
                        android.util.Slog.e(str, "deleteDefaultChannelIfNeededLocked - Exception: " + e10);
                    }
                    if (migrateToPermission) {
                        r.importance = appImportance;
                        r.migrateToPm = z;
                    }
                } catch (java.lang.Exception e11) {
                    e = e11;
                    str = str3;
                    android.util.Slog.w(str, "Failed to restore pkg", e);
                }
            } catch (java.lang.Exception e12) {
                e = e12;
                str = TAG;
            }
        } catch (java.lang.Exception e13) {
            e = e13;
            str = TAG;
        }
    }

    private void restoreChannel(com.android.modules.utils.TypedXmlPullParser parser, boolean forRestore, com.android.server.notification.PreferencesHelper.PackagePreferences r) {
        try {
            java.lang.String id = parser.getAttributeValue((java.lang.String) null, ATT_ID);
            java.lang.String channelName = parser.getAttributeValue((java.lang.String) null, "name");
            int channelImportance = parser.getAttributeInt((java.lang.String) null, ATT_IMPORTANCE, -1000);
            if (!android.text.TextUtils.isEmpty(id) && !android.text.TextUtils.isEmpty(channelName)) {
                android.app.NotificationChannel channel = new android.app.NotificationChannel(id, channelName, channelImportance);
                boolean z = true;
                if (forRestore) {
                    boolean pkgInstalled = r.uid != -10000;
                    channel.populateFromXmlForRestore(parser, pkgInstalled, this.mContext);
                } else {
                    channel.populateFromXml(parser);
                }
                if (!r.defaultAppLockedImportance && !r.fixedImportance) {
                    z = false;
                }
                channel.setImportanceLockedByCriticalDeviceFunction(z);
                if (isShortcutOk(channel) && isDeletionOk(channel)) {
                    r.channels.put(id, channel);
                }
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "could not restore channel for " + r.pkg, e);
        }
    }

    private boolean hasUserConfiguredSettings(com.android.server.notification.PreferencesHelper.PackagePreferences p) {
        boolean hasChangedChannel = false;
        java.util.Iterator<android.app.NotificationChannel> it = p.channels.values().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            android.app.NotificationChannel channel = it.next();
            if (channel.getUserLockedFields() != 0) {
                hasChangedChannel = true;
                break;
            }
        }
        return hasChangedChannel || p.importance == 0;
    }

    private boolean isShortcutOk(android.app.NotificationChannel channel) {
        boolean isInvalidShortcutChannel = channel.getConversationId() != null && channel.getConversationId().contains(":placeholder_id");
        return !isInvalidShortcutChannel;
    }

    private boolean isDeletionOk(android.app.NotificationChannel nc) {
        if (!nc.isDeleted()) {
            return true;
        }
        long boundary = java.lang.System.currentTimeMillis() - com.android.server.usage.UnixCalendar.MONTH_IN_MILLIS;
        return nc.getDeletedTimeMs() > boundary;
    }

    private com.android.server.notification.PreferencesHelper.PackagePreferences getPackagePreferencesLocked(java.lang.String pkg, int uid) {
        java.lang.String key = packagePreferencesKey(pkg, uid);
        return this.mPackagePreferences.get(key);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.notification.PreferencesHelper.PackagePreferences getOrCreatePackagePreferencesLocked(java.lang.String pkg, int uid) {
        return getOrCreatePackagePreferencesLocked(pkg, android.os.UserHandle.getUserId(uid), uid, -1000, 0, -1000, true, 0, this.mClock.millis());
    }

    private com.android.server.notification.PreferencesHelper.PackagePreferences getOrCreatePackagePreferencesLocked(java.lang.String pkg, int userId, int uid, int importance, int priority, int visibility, boolean showBadge, int bubblePreference, long creationTime) {
        com.android.server.notification.PreferencesHelper.PackagePreferences r;
        java.lang.String key = packagePreferencesKey(pkg, uid);
        if (uid == -10000) {
            r = this.mRestoredWithoutUids.get(unrestoredPackageKey(pkg, userId));
        } else {
            r = this.mPackagePreferences.get(key);
        }
        if (r == null) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r2 = new com.android.server.notification.PreferencesHelper.PackagePreferences();
            r2.pkg = pkg;
            r2.uid = uid;
            r2.importance = importance;
            r2.priority = priority;
            r2.visibility = visibility;
            r2.showBadge = showBadge;
            r2.bubblePreference = bubblePreference;
            if (com.android.server.notification.Flags.persistIncompleteRestoreData() && r2.uid == -10000) {
                r2.creationTime = creationTime;
            }
            try {
                createDefaultChannelIfNeededLocked(r2);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.e(TAG, "createDefaultChannelIfNeededLocked - Exception: " + e);
            }
            if (r2.uid != -10000) {
                this.mPackagePreferences.put(key, r2);
            } else {
                if (com.android.server.notification.Flags.persistIncompleteRestoreData()) {
                    r2.userId = userId;
                }
                this.mRestoredWithoutUids.put(unrestoredPackageKey(pkg, userId), r2);
            }
            r = r2;
        }
        if (r.uid == -10000 && com.android.server.notification.Flags.persistIncompleteRestoreData() && PREF_GRACE_PERIOD_MS < this.mClock.millis() - r.creationTime) {
            this.mRestoredWithoutUids.remove(unrestoredPackageKey(pkg, userId));
        }
        return r;
    }

    private boolean shouldHaveDefaultChannel(com.android.server.notification.PreferencesHelper.PackagePreferences r) throws android.content.pm.PackageManager.NameNotFoundException {
        int userId = android.os.UserHandle.getUserId(r.uid);
        android.content.pm.ApplicationInfo applicationInfo = this.mPm.getApplicationInfoAsUser(r.pkg, 0, userId);
        if (applicationInfo.targetSdkVersion >= 26) {
            return false;
        }
        return true;
    }

    private boolean deleteDefaultChannelIfNeededLocked(com.android.server.notification.PreferencesHelper.PackagePreferences r) throws android.content.pm.PackageManager.NameNotFoundException {
        if (!r.channels.containsKey("miscellaneous") || shouldHaveDefaultChannel(r)) {
            return false;
        }
        r.channels.remove("miscellaneous");
        return true;
    }

    private boolean createDefaultChannelIfNeededLocked(com.android.server.notification.PreferencesHelper.PackagePreferences r) throws android.content.pm.PackageManager.NameNotFoundException {
        if (r.uid == -10000) {
            return false;
        }
        if (r.channels.containsKey("miscellaneous")) {
            r.channels.get("miscellaneous").setName(this.mContext.getString(android.R.string.demo_starting_message));
            return false;
        }
        if (!shouldHaveDefaultChannel(r)) {
            return false;
        }
        android.app.NotificationChannel channel = new android.app.NotificationChannel("miscellaneous", this.mContext.getString(android.R.string.demo_starting_message), r.importance);
        channel.setBypassDnd(r.priority == 2);
        channel.setLockscreenVisibility(r.visibility);
        if (r.importance != -1000) {
            channel.lockFields(4);
        }
        if (r.priority != 0) {
            channel.lockFields(1);
        }
        if (r.visibility != -1000) {
            channel.lockFields(2);
        }
        r.channels.put(channel.getId(), channel);
        return true;
    }

    public void writeXml(com.android.modules.utils.TypedXmlSerializer out, boolean forBackup, int userId) throws java.io.IOException {
        out.startTag((java.lang.String) null, TAG_RANKING);
        out.attributeInt((java.lang.String) null, ATT_VERSION, this.XML_VERSION);
        if (this.mHideSilentStatusBarIcons) {
            out.startTag((java.lang.String) null, TAG_STATUS_ICONS);
            out.attributeBoolean((java.lang.String) null, ATT_HIDE_SILENT, this.mHideSilentStatusBarIcons);
            out.endTag((java.lang.String) null, TAG_STATUS_ICONS);
        }
        android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> notifPermissions = new android.util.ArrayMap<>();
        if (forBackup) {
            notifPermissions = this.mPermissionHelper.getNotificationPermissionValues(userId);
        }
        synchronized (this.mLock) {
            int N = this.mPackagePreferences.size();
            for (int i = 0; i < N; i++) {
                com.android.server.notification.PreferencesHelper.PackagePreferences r = this.mPackagePreferences.valueAt(i);
                if (!forBackup || android.os.UserHandle.getUserId(r.uid) == userId) {
                    writePackageXml(r, out, notifPermissions, forBackup);
                }
            }
            if (com.android.server.notification.Flags.persistIncompleteRestoreData() && !forBackup) {
                int M = this.mRestoredWithoutUids.size();
                for (int i2 = 0; i2 < M; i2++) {
                    writePackageXml(this.mRestoredWithoutUids.valueAt(i2), out, notifPermissions, false);
                }
            }
        }
        if (!notifPermissions.isEmpty()) {
            for (android.util.Pair<java.lang.Integer, java.lang.String> app : notifPermissions.keySet()) {
                out.startTag((java.lang.String) null, "package");
                out.attribute((java.lang.String) null, "name", (java.lang.String) app.second);
                out.attributeInt((java.lang.String) null, ATT_IMPORTANCE, ((java.lang.Boolean) notifPermissions.get(app).first).booleanValue() ? 3 : 0);
                out.endTag((java.lang.String) null, "package");
            }
        }
        out.endTag((java.lang.String) null, TAG_RANKING);
    }

    public void writePackageXml(com.android.server.notification.PreferencesHelper.PackagePreferences r, com.android.modules.utils.TypedXmlSerializer out, android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> notifPermissions, boolean forBackup) throws java.io.IOException {
        out.startTag((java.lang.String) null, "package");
        out.attribute((java.lang.String) null, "name", r.pkg);
        if (!notifPermissions.isEmpty()) {
            android.util.Pair<java.lang.Integer, java.lang.String> app = new android.util.Pair<>(java.lang.Integer.valueOf(r.uid), r.pkg);
            android.util.Pair<java.lang.Boolean, java.lang.Boolean> permission = notifPermissions.get(app);
            out.attributeInt((java.lang.String) null, ATT_IMPORTANCE, (permission == null || !((java.lang.Boolean) permission.first).booleanValue()) ? 0 : 3);
            notifPermissions.remove(app);
        } else if (r.importance != -1000) {
            out.attributeInt((java.lang.String) null, ATT_IMPORTANCE, r.importance);
        }
        if (r.priority != 0) {
            out.attributeInt((java.lang.String) null, ATT_PRIORITY, r.priority);
        }
        if (r.visibility != -1000) {
            out.attributeInt((java.lang.String) null, ATT_VISIBILITY, r.visibility);
        }
        if (r.bubblePreference != 0) {
            out.attributeInt((java.lang.String) null, ATT_ALLOW_BUBBLE, r.bubblePreference);
        }
        out.attributeBoolean((java.lang.String) null, ATT_SHOW_BADGE, r.showBadge);
        out.attributeInt((java.lang.String) null, ATT_APP_USER_LOCKED_FIELDS, r.lockedAppFields);
        out.attributeBoolean((java.lang.String) null, ATT_SENT_INVALID_MESSAGE, r.hasSentInvalidMessage);
        out.attributeBoolean((java.lang.String) null, ATT_SENT_VALID_MESSAGE, r.hasSentValidMessage);
        out.attributeBoolean((java.lang.String) null, ATT_USER_DEMOTED_INVALID_MSG_APP, r.userDemotedMsgApp);
        out.attributeBoolean((java.lang.String) null, ATT_SENT_VALID_BUBBLE, r.hasSentValidBubble);
        if (com.android.server.notification.Flags.persistIncompleteRestoreData() && r.uid == -10000) {
            out.attributeLong((java.lang.String) null, ATT_CREATION_TIME, r.creationTime);
            out.attributeInt((java.lang.String) null, ATT_USERID, r.userId);
        }
        if (!forBackup) {
            out.attributeInt((java.lang.String) null, "uid", r.uid);
        }
        this.mPHWrapper.getPreferencesHelperExt().writeAttrbute(out, r.mPPWrapper.getPackagePreferencesExt());
        if (r.delegate != null) {
            out.startTag((java.lang.String) null, TAG_DELEGATE);
            out.attribute((java.lang.String) null, "name", r.delegate.mPkg);
            out.attributeInt((java.lang.String) null, "uid", r.delegate.mUid);
            if (!r.delegate.mEnabled) {
                out.attributeBoolean((java.lang.String) null, "enabled", r.delegate.mEnabled);
            }
            out.endTag((java.lang.String) null, TAG_DELEGATE);
        }
        for (android.app.NotificationChannelGroup group : r.groups.values()) {
            group.writeXml(out);
        }
        try {
            for (android.app.NotificationChannel channel : r.channels.values()) {
                if (channel != null) {
                    if (forBackup) {
                        if (!channel.isDeleted()) {
                            channel.writeXmlForBackup(out, this.mContext);
                        }
                    } else {
                        channel.writeXml(out);
                    }
                }
            }
        } catch (java.lang.ClassCastException e) {
        }
        out.endTag((java.lang.String) null, "package");
    }

    public void setBubblesAllowed(java.lang.String pkg, int uid, int bubblePreference) {
        boolean changed;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences p = getOrCreatePackagePreferencesLocked(pkg, uid);
            changed = p.bubblePreference != bubblePreference;
            p.bubblePreference = bubblePreference;
            p.lockedAppFields |= 2;
        }
        if (changed) {
            updateConfig();
        }
    }

    @Override // com.android.server.notification.RankingConfig
    public int getBubblePreference(java.lang.String pkg, int uid) {
        int i;
        synchronized (this.mLock) {
            i = getOrCreatePackagePreferencesLocked(pkg, uid).bubblePreference;
        }
        return i;
    }

    public int getAppLockedFields(java.lang.String pkg, int uid) {
        int i;
        synchronized (this.mLock) {
            i = getOrCreatePackagePreferencesLocked(pkg, uid).lockedAppFields;
        }
        return i;
    }

    @Override // com.android.server.notification.RankingConfig
    public boolean canShowBadge(java.lang.String packageName, int uid) {
        boolean z;
        synchronized (this.mLock) {
            z = getOrCreatePackagePreferencesLocked(packageName, uid).showBadge;
        }
        return z;
    }

    @Override // com.android.server.notification.RankingConfig
    public void setShowBadge(java.lang.String packageName, int uid, boolean showBadge) {
        boolean changed = false;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences pkgPrefs = getOrCreatePackagePreferencesLocked(packageName, uid);
            if (pkgPrefs.showBadge != showBadge) {
                pkgPrefs.showBadge = showBadge;
                changed = true;
            }
        }
        if (changed) {
            updateConfig();
        }
    }

    public boolean isInInvalidMsgState(java.lang.String packageName, int uid) {
        boolean z;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(packageName, uid);
            z = r.hasSentInvalidMessage && !r.hasSentValidMessage;
        }
        return z;
    }

    public boolean hasUserDemotedInvalidMsgApp(java.lang.String packageName, int uid) {
        boolean z;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(packageName, uid);
            z = isInInvalidMsgState(packageName, uid) ? r.userDemotedMsgApp : false;
        }
        return z;
    }

    public void setInvalidMsgAppDemoted(java.lang.String packageName, int uid, boolean isDemoted) {
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(packageName, uid);
            r.userDemotedMsgApp = isDemoted;
        }
    }

    public boolean setInvalidMessageSent(java.lang.String packageName, int uid) {
        boolean valueChanged;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(packageName, uid);
            valueChanged = !r.hasSentInvalidMessage;
            r.hasSentInvalidMessage = true;
        }
        return valueChanged;
    }

    public boolean setValidMessageSent(java.lang.String packageName, int uid) {
        boolean valueChanged;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(packageName, uid);
            valueChanged = !r.hasSentValidMessage;
            r.hasSentValidMessage = true;
        }
        return valueChanged;
    }

    boolean hasSentInvalidMsg(java.lang.String packageName, int uid) {
        boolean z;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(packageName, uid);
            z = r.hasSentInvalidMessage;
        }
        return z;
    }

    boolean hasSentValidMsg(java.lang.String packageName, int uid) {
        boolean z;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(packageName, uid);
            z = r.hasSentValidMessage;
        }
        return z;
    }

    boolean didUserEverDemoteInvalidMsgApp(java.lang.String packageName, int uid) {
        boolean z;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(packageName, uid);
            z = r.userDemotedMsgApp;
        }
        return z;
    }

    public boolean setValidBubbleSent(java.lang.String packageName, int uid) {
        boolean valueChanged;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(packageName, uid);
            valueChanged = !r.hasSentValidBubble;
            r.hasSentValidBubble = true;
        }
        return valueChanged;
    }

    boolean hasSentValidBubble(java.lang.String packageName, int uid) {
        boolean z;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(packageName, uid);
            z = r.hasSentValidBubble;
        }
        return z;
    }

    boolean isImportanceLocked(java.lang.String pkg, int uid) {
        boolean z;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(pkg, uid);
            z = r.fixedImportance || r.defaultAppLockedImportance;
        }
        return z;
    }

    @Override // com.android.server.notification.RankingConfig
    public boolean isGroupBlocked(java.lang.String packageName, int uid, java.lang.String groupId) {
        if (groupId == null) {
            return false;
        }
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(packageName, uid);
            android.app.NotificationChannelGroup group = r.groups.get(groupId);
            if (group == null) {
                return false;
            }
            return group.isBlocked();
        }
    }

    int getPackagePriority(java.lang.String pkg, int uid) {
        int i;
        synchronized (this.mLock) {
            i = getOrCreatePackagePreferencesLocked(pkg, uid).priority;
        }
        return i;
    }

    int getPackageVisibility(java.lang.String pkg, int uid) {
        int i;
        synchronized (this.mLock) {
            i = getOrCreatePackagePreferencesLocked(pkg, uid).visibility;
        }
        return i;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x007a A[Catch: all -> 0x00bd, TRY_LEAVE, TryCatch #2 {all -> 0x00bd, blocks: (B:23:0x0074, B:25:0x007a), top: B:65:0x0074 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00b6  */
    @Override // com.android.server.notification.RankingConfig
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void createNotificationChannelGroup(java.lang.String r14, int r15, android.app.NotificationChannelGroup r16, boolean r17, int r18, boolean r19) {
        /*
            Method dump skipped, instruction units count: 246
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.PreferencesHelper.createNotificationChannelGroup(java.lang.String, int, android.app.NotificationChannelGroup, boolean, int, boolean):void");
    }

    @Override // com.android.server.notification.RankingConfig
    public boolean createNotificationChannel(java.lang.String pkg, int uid, android.app.NotificationChannel channel, boolean fromTargetApp, boolean hasDndAccess, int callingUid, boolean fromSystemOrSystemUi) {
        boolean needsPolicyFileChange;
        boolean needsDndChange;
        boolean wasUndeleted;
        boolean needsDndChange2;
        boolean needsPolicyFileChange2;
        boolean bypassDnd;
        java.util.Objects.requireNonNull(pkg);
        java.util.Objects.requireNonNull(channel);
        java.util.Objects.requireNonNull(channel.getId());
        com.android.internal.util.Preconditions.checkArgument(!android.text.TextUtils.isEmpty(channel.getName()));
        com.android.internal.util.Preconditions.checkArgument(channel.getImportance() >= 0 && channel.getImportance() <= 5, "Invalid importance level");
        boolean needsPolicyFileChange3 = false;
        synchronized (this.mLock) {
            try {
                try {
                    com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(pkg, uid);
                    if (r == null) {
                        throw new java.lang.IllegalArgumentException("Invalid package");
                    }
                    if (channel.getGroup() != null && !r.groups.containsKey(channel.getGroup())) {
                        throw new java.lang.IllegalArgumentException("NotificationChannelGroup doesn't exist");
                    }
                    if ("miscellaneous".equals(channel.getId())) {
                        throw new java.lang.IllegalArgumentException("Reserved id");
                    }
                    android.app.NotificationChannel existing = r.channels.get(channel.getId());
                    if (existing != null && fromTargetApp) {
                        if (existing.isDeleted()) {
                            existing.setDeleted(false);
                            existing.setDeletedTimeMs(-1L);
                            needsPolicyFileChange3 = true;
                            com.android.internal.logging.MetricsLogger.action(getChannelLog(channel, pkg).setType(1));
                            this.mNotificationChannelLogger.logNotificationChannelCreated(channel, uid, pkg);
                            wasUndeleted = true;
                        } else {
                            wasUndeleted = false;
                        }
                        try {
                            if (!java.util.Objects.equals(channel.getName().toString(), existing.getName().toString())) {
                                existing.setName(channel.getName().toString());
                                needsPolicyFileChange3 = true;
                            }
                            if (!java.util.Objects.equals(channel.getDescription(), existing.getDescription())) {
                                existing.setDescription(channel.getDescription());
                                needsPolicyFileChange3 = true;
                            }
                            if (channel.isBlockable() != existing.isBlockable()) {
                                existing.setBlockable(channel.isBlockable());
                                needsPolicyFileChange3 = true;
                            }
                            if (channel.getGroup() != null && existing.getGroup() == null) {
                                existing.setGroup(channel.getGroup());
                                needsPolicyFileChange3 = true;
                            }
                            int previousExistingImportance = existing.getImportance();
                            int previousLoggingImportance = com.android.server.notification.NotificationChannelLogger.getLoggingImportance(existing);
                            if (existing.getUserLockedFields() == 0 && channel.getImportance() < existing.getImportance()) {
                                existing.setImportance(channel.getImportance());
                                needsPolicyFileChange3 = true;
                            }
                            if (existing.getUserLockedFields() == 0 && hasDndAccess && ((bypassDnd = channel.canBypassDnd()) != existing.canBypassDnd() || wasUndeleted)) {
                                existing.setBypassDnd(bypassDnd);
                                needsPolicyFileChange3 = true;
                                if (bypassDnd == this.mCurrentUserHasChannelsBypassingDnd) {
                                    if (previousExistingImportance == existing.getImportance()) {
                                        needsDndChange2 = false;
                                    }
                                }
                                needsDndChange2 = true;
                            } else {
                                needsDndChange2 = false;
                            }
                            try {
                                if (existing.getOriginalImportance() == -1000) {
                                    existing.setOriginalImportance(channel.getImportance());
                                    needsPolicyFileChange2 = true;
                                } else {
                                    needsPolicyFileChange2 = needsPolicyFileChange3;
                                }
                                if (needsPolicyFileChange2) {
                                    try {
                                        updateConfig();
                                    } catch (java.lang.Throwable th) {
                                        th = th;
                                        throw th;
                                    }
                                }
                                if (needsPolicyFileChange2 && !wasUndeleted) {
                                    this.mNotificationChannelLogger.logNotificationChannelModified(existing, uid, pkg, previousLoggingImportance, false);
                                }
                                needsPolicyFileChange = needsPolicyFileChange2;
                                needsDndChange = needsDndChange2;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                    } else {
                        if (r.channels.size() >= 5000) {
                            throw new java.lang.IllegalStateException("Limit exceed; cannot create more channels");
                        }
                        needsPolicyFileChange = true;
                        if (fromTargetApp && !hasDndAccess) {
                            channel.setBypassDnd(r.priority == 2);
                        }
                        if (fromTargetApp) {
                            channel.setLockscreenVisibility(r.visibility);
                            channel.setAllowBubbles(existing != null ? existing.getAllowBubbles() : -1);
                            channel.setImportantConversation(false);
                        }
                        clearLockedFieldsLocked(channel);
                        channel.setImportanceLockedByCriticalDeviceFunction(r.defaultAppLockedImportance || r.fixedImportance);
                        if (channel.getLockscreenVisibility() == 1) {
                            channel.setLockscreenVisibility(-1000);
                        }
                        if (!r.showBadge) {
                            boolean oldCanShowBadge = channel.canShowBadge();
                            channel.setShowBadge(false);
                            if (oldCanShowBadge) {
                                channel.getWrapper().getExtImpl().setTempShowBadge(true);
                            }
                        }
                        channel.setOriginalImportance(channel.getImportance());
                        if (channel.getParentChannelId() != null) {
                            com.android.internal.util.Preconditions.checkArgument(r.channels.containsKey(channel.getParentChannelId()), "Tried to create a conversation channel without a preexisting parent");
                        }
                        r.channels.put(channel.getId(), channel);
                        needsDndChange = channel.canBypassDnd() != this.mCurrentUserHasChannelsBypassingDnd;
                        com.android.internal.logging.MetricsLogger.action(getChannelLog(channel, pkg).setType(1));
                        this.mNotificationChannelLogger.logNotificationChannelCreated(channel, uid, pkg);
                    }
                    if (needsDndChange) {
                        updateCurrentUserHasChannelsBypassingDnd(callingUid, fromSystemOrSystemUi);
                    }
                    return needsPolicyFileChange;
                } catch (java.lang.Throwable th4) {
                    th = th4;
                }
            } catch (java.lang.Throwable th5) {
                th = th5;
            }
        }
    }

    void clearLockedFieldsLocked(android.app.NotificationChannel channel) {
        channel.unlockFields(channel.getUserLockedFields());
    }

    void unlockNotificationChannelImportance(java.lang.String pkg, int uid, java.lang.String updatedChannelId) {
        java.util.Objects.requireNonNull(updatedChannelId);
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(pkg, uid);
            if (r == null) {
                throw new java.lang.IllegalArgumentException("Invalid package");
            }
            android.app.NotificationChannel channel = r.channels.get(updatedChannelId);
            if (channel == null || channel.isDeleted()) {
                throw new java.lang.IllegalArgumentException("Channel does not exist");
            }
            channel.unlockFields(4);
        }
    }

    @Override // com.android.server.notification.RankingConfig
    public void updateNotificationChannel(java.lang.String pkg, int uid, android.app.NotificationChannel updatedChannel, boolean fromUser, int callingUid, boolean fromSystemOrSystemUi) {
        boolean changed;
        boolean changed2;
        java.util.Objects.requireNonNull(updatedChannel);
        java.util.Objects.requireNonNull(updatedChannel.getId());
        boolean needsDndChange = false;
        synchronized (this.mLock) {
            try {
                try {
                    com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(pkg, uid);
                    if (r == null) {
                        throw new java.lang.IllegalArgumentException("Invalid package");
                    }
                    android.app.NotificationChannel channel = r.channels.get(updatedChannel.getId());
                    if (channel == null || channel.isDeleted()) {
                        throw new java.lang.IllegalArgumentException("Channel does not exist");
                    }
                    int i = 1;
                    if (updatedChannel.getLockscreenVisibility() == 1) {
                        updatedChannel.setLockscreenVisibility(-1000);
                    }
                    if (fromUser) {
                        updatedChannel.lockFields(channel.getUserLockedFields());
                        lockFieldsForUpdateLocked(channel, updatedChannel);
                    } else {
                        updatedChannel.unlockFields(updatedChannel.getUserLockedFields());
                    }
                    if (channel.isImportanceLockedByCriticalDeviceFunction() && !channel.isBlockable() && channel.getImportance() != 0) {
                        updatedChannel.setImportance(channel.getImportance());
                    }
                    this.mPHWrapper.getPreferencesHelperExt().updateNotificationChannel(updatedChannel.getWrapper().getExtImpl(), r.mPPWrapper.getPackagePreferencesExt());
                    r.channels.put(updatedChannel.getId(), updatedChannel);
                    if (onlyHasDefaultChannel(pkg, uid)) {
                        r.priority = updatedChannel.canBypassDnd() ? 2 : 0;
                        r.visibility = updatedChannel.getLockscreenVisibility();
                        r.showBadge = updatedChannel.canShowBadge();
                        changed = true;
                    } else {
                        changed = false;
                    }
                    try {
                        boolean changed3 = channel.equals(updatedChannel);
                        if (changed3) {
                            changed2 = changed;
                        } else {
                            try {
                                android.metrics.LogMaker channelLog = getChannelLog(updatedChannel, pkg);
                                if (!fromUser) {
                                    i = 0;
                                }
                                com.android.internal.logging.MetricsLogger.action(channelLog.setSubtype(i));
                                this.mNotificationChannelLogger.logNotificationChannelModified(updatedChannel, uid, pkg, com.android.server.notification.NotificationChannelLogger.getLoggingImportance(channel), fromUser);
                                changed2 = true;
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                        if (fromUser) {
                            try {
                                if (com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.getResolver().isEnabled(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.PROPAGATE_CHANNEL_UPDATES_TO_CONVERSATIONS)) {
                                    updateChildrenConversationChannels(r, channel, updatedChannel);
                                }
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        }
                        if (updatedChannel.canBypassDnd() != this.mCurrentUserHasChannelsBypassingDnd || channel.getImportance() != updatedChannel.getImportance()) {
                            changed2 = true;
                            needsDndChange = true;
                        }
                        if (needsDndChange) {
                            updateCurrentUserHasChannelsBypassingDnd(callingUid, fromSystemOrSystemUi);
                        }
                        if (changed2) {
                            updateConfig();
                            return;
                        }
                        return;
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                }
            } catch (java.lang.Throwable th5) {
                th = th5;
            }
            throw th;
        }
    }

    private void updateChildrenConversationChannels(com.android.server.notification.PreferencesHelper.PackagePreferences packagePreferences, android.app.NotificationChannel oldParent, android.app.NotificationChannel updatedParent) {
        if (oldParent.equals(updatedParent) || oldParent.isConversation()) {
            return;
        }
        for (android.app.NotificationChannel channel : packagePreferences.channels.values()) {
            if (channel.isConversation() && oldParent.getId().equals(channel.getParentChannelId())) {
                maybeUpdateChildConversationChannel(packagePreferences.pkg, packagePreferences.uid, channel, oldParent, updatedParent);
            }
        }
    }

    private void maybeUpdateChildConversationChannel(java.lang.String pkg, int uid, android.app.NotificationChannel conversation, android.app.NotificationChannel oldParent, android.app.NotificationChannel updatedParent) {
        boolean changed = false;
        int oldLoggingImportance = com.android.server.notification.NotificationChannelLogger.getLoggingImportance(conversation);
        if ((conversation.getUserLockedFields() & 1) == 0 && oldParent.canBypassDnd() != updatedParent.canBypassDnd()) {
            conversation.setBypassDnd(updatedParent.canBypassDnd());
            changed = true;
        }
        if ((conversation.getUserLockedFields() & 2) == 0 && oldParent.getLockscreenVisibility() != updatedParent.getLockscreenVisibility()) {
            conversation.setLockscreenVisibility(updatedParent.getLockscreenVisibility());
            changed = true;
        }
        if ((conversation.getUserLockedFields() & 4) == 0 && oldParent.getImportance() != updatedParent.getImportance()) {
            conversation.setImportance(updatedParent.getImportance());
            changed = true;
        }
        if ((conversation.getUserLockedFields() & 8) == 0 && (oldParent.shouldShowLights() != updatedParent.shouldShowLights() || oldParent.getLightColor() != updatedParent.getLightColor())) {
            conversation.enableLights(updatedParent.shouldShowLights());
            conversation.setLightColor(updatedParent.getLightColor());
            changed = true;
        }
        if ((conversation.getUserLockedFields() & 32) == 0 && !java.util.Objects.equals(oldParent.getSound(), updatedParent.getSound())) {
            conversation.setSound(updatedParent.getSound(), updatedParent.getAudioAttributes());
            changed = true;
        }
        if ((conversation.getUserLockedFields() & 16) == 0 && (!java.util.Arrays.equals(oldParent.getVibrationPattern(), updatedParent.getVibrationPattern()) || !java.util.Objects.equals(oldParent.getVibrationEffect(), updatedParent.getVibrationEffect()) || oldParent.shouldVibrate() != updatedParent.shouldVibrate())) {
            conversation.setVibrationPattern(updatedParent.getVibrationPattern());
            conversation.enableVibration(updatedParent.shouldVibrate());
            changed = true;
        }
        if ((conversation.getUserLockedFields() & 128) == 0 && oldParent.canShowBadge() != updatedParent.canShowBadge()) {
            conversation.setShowBadge(updatedParent.canShowBadge());
            changed = true;
        }
        if ((conversation.getUserLockedFields() & 256) == 0 && oldParent.getAllowBubbles() != updatedParent.getAllowBubbles()) {
            conversation.setAllowBubbles(updatedParent.getAllowBubbles());
            changed = true;
        }
        if (changed) {
            com.android.internal.logging.MetricsLogger.action(getChannelLog(conversation, pkg).setSubtype(1));
            this.mNotificationChannelLogger.logNotificationChannelModified(conversation, uid, pkg, oldLoggingImportance, true);
        }
    }

    @Override // com.android.server.notification.RankingConfig
    public android.app.NotificationChannel getNotificationChannel(java.lang.String pkg, int uid, java.lang.String channelId, boolean includeDeleted) {
        java.util.Objects.requireNonNull(pkg);
        return getConversationNotificationChannel(pkg, uid, channelId, null, true, includeDeleted);
    }

    @Override // com.android.server.notification.RankingConfig
    public android.app.NotificationChannel getConversationNotificationChannel(java.lang.String pkg, int uid, java.lang.String channelId, java.lang.String conversationId, boolean returnParentIfNoConversationChannel, boolean includeDeleted) {
        android.app.NotificationChannel nc;
        com.android.internal.util.Preconditions.checkNotNull(pkg);
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return null;
            }
            if (channelId == null) {
                channelId = "miscellaneous";
            }
            android.app.NotificationChannel channel = null;
            if (conversationId != null) {
                channel = findConversationChannel(r, channelId, conversationId, includeDeleted);
            }
            return (channel != null || !returnParentIfNoConversationChannel || (nc = r.channels.get(channelId)) == null || (!includeDeleted && nc.isDeleted())) ? channel : nc;
        }
    }

    private android.app.NotificationChannel findConversationChannel(com.android.server.notification.PreferencesHelper.PackagePreferences p, java.lang.String parentId, java.lang.String conversationId, boolean includeDeleted) {
        for (android.app.NotificationChannel nc : p.channels.values()) {
            if (conversationId.equals(nc.getConversationId()) && parentId.equals(nc.getParentChannelId()) && (includeDeleted || !nc.isDeleted())) {
                return nc;
            }
        }
        return null;
    }

    public java.util.List<android.app.NotificationChannel> getNotificationChannelsByConversationId(java.lang.String pkg, int uid, java.lang.String conversationId) {
        com.android.internal.util.Preconditions.checkNotNull(pkg);
        com.android.internal.util.Preconditions.checkNotNull(conversationId);
        java.util.List<android.app.NotificationChannel> channels = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return channels;
            }
            for (android.app.NotificationChannel nc : r.channels.values()) {
                if (conversationId.equals(nc.getConversationId()) && !nc.isDeleted()) {
                    channels.add(nc);
                }
            }
            return channels;
        }
    }

    @Override // com.android.server.notification.RankingConfig
    public boolean deleteNotificationChannel(java.lang.String pkg, int uid, java.lang.String channelId, int callingUid, boolean fromSystemOrSystemUi) {
        boolean deletedChannel = false;
        boolean channelBypassedDnd = false;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return false;
            }
            android.app.NotificationChannel channel = r.channels.get(channelId);
            if (channel != null) {
                channelBypassedDnd = channel.canBypassDnd();
                deletedChannel = deleteNotificationChannelLocked(channel, pkg, uid);
            }
            if (channelBypassedDnd) {
                updateCurrentUserHasChannelsBypassingDnd(callingUid, fromSystemOrSystemUi);
            }
            return deletedChannel;
        }
    }

    private boolean deleteNotificationChannelLocked(android.app.NotificationChannel channel, java.lang.String pkg, int uid) {
        if (!channel.isDeleted()) {
            channel.setDeleted(true);
            channel.setDeletedTimeMs(java.lang.System.currentTimeMillis());
            android.metrics.LogMaker lm = getChannelLog(channel, pkg);
            lm.setType(2);
            com.android.internal.logging.MetricsLogger.action(lm);
            this.mNotificationChannelLogger.logNotificationChannelDeleted(channel, uid, pkg);
            return true;
        }
        return false;
    }

    @Override // com.android.server.notification.RankingConfig
    public void permanentlyDeleteNotificationChannel(java.lang.String pkg, int uid, java.lang.String channelId) {
        java.util.Objects.requireNonNull(pkg);
        java.util.Objects.requireNonNull(channelId);
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return;
            }
            r.channels.remove(channelId);
        }
    }

    @Override // com.android.server.notification.RankingConfig
    public void permanentlyDeleteNotificationChannels(java.lang.String pkg, int uid) {
        java.util.Objects.requireNonNull(pkg);
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return;
            }
            int N = r.channels.size() - 1;
            for (int i = N; i >= 0; i--) {
                java.lang.String key = r.channels.keyAt(i);
                if (!"miscellaneous".equals(key)) {
                    r.channels.remove(key);
                }
            }
        }
    }

    public boolean shouldHideSilentStatusIcons() {
        return this.mHideSilentStatusBarIcons;
    }

    public void setHideSilentStatusIcons(boolean hide) {
        this.mHideSilentStatusBarIcons = hide;
    }

    public void updateFixedImportance(java.util.List<android.content.pm.UserInfo> users) {
        for (android.content.pm.UserInfo user : users) {
            java.util.List<android.content.pm.PackageInfo> packages = this.mPm.getInstalledPackagesAsUser(0, user.getUserHandle().getIdentifier());
            for (android.content.pm.PackageInfo pi : packages) {
                boolean fixed = this.mPermissionHelper.isPermissionFixed(pi.packageName, user.getUserHandle().getIdentifier());
                if (fixed) {
                    synchronized (this.mLock) {
                        com.android.server.notification.PreferencesHelper.PackagePreferences p = getOrCreatePackagePreferencesLocked(pi.packageName, pi.applicationInfo.uid);
                        p.fixedImportance = true;
                        for (android.app.NotificationChannel channel : p.channels.values()) {
                            channel.setImportanceLockedByCriticalDeviceFunction(true);
                        }
                    }
                }
            }
        }
    }

    public void updateDefaultApps(int userId, android.util.ArraySet<java.lang.String> toRemove, android.util.ArraySet<android.util.Pair<java.lang.String, java.lang.Integer>> toAdd) {
        synchronized (this.mLock) {
            for (com.android.server.notification.PreferencesHelper.PackagePreferences p : this.mPackagePreferences.values()) {
                if (userId == android.os.UserHandle.getUserId(p.uid) && toRemove != null && toRemove.contains(p.pkg)) {
                    p.defaultAppLockedImportance = false;
                    if (!p.fixedImportance) {
                        for (android.app.NotificationChannel channel : p.channels.values()) {
                            channel.setImportanceLockedByCriticalDeviceFunction(false);
                        }
                    }
                }
            }
            if (toAdd != null) {
                for (android.util.Pair<java.lang.String, java.lang.Integer> approvedApp : toAdd) {
                    com.android.server.notification.PreferencesHelper.PackagePreferences p2 = getOrCreatePackagePreferencesLocked((java.lang.String) approvedApp.first, ((java.lang.Integer) approvedApp.second).intValue());
                    p2.defaultAppLockedImportance = true;
                    for (android.app.NotificationChannel channel2 : p2.channels.values()) {
                        channel2.setImportanceLockedByCriticalDeviceFunction(true);
                    }
                }
            }
        }
    }

    public android.app.NotificationChannelGroup getNotificationChannelGroupWithChannels(java.lang.String pkg, int uid, java.lang.String groupId, boolean includeDeleted) {
        java.util.Objects.requireNonNull(pkg);
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r != null && groupId != null && r.groups.containsKey(groupId)) {
                android.app.NotificationChannelGroup group = r.groups.get(groupId).clone();
                group.setChannels(new java.util.ArrayList());
                int N = r.channels.size();
                for (int i = 0; i < N; i++) {
                    android.app.NotificationChannel nc = r.channels.valueAt(i);
                    if ((includeDeleted || !nc.isDeleted()) && groupId.equals(nc.getGroup())) {
                        group.addChannel(nc);
                    }
                }
                return group;
            }
            return null;
        }
    }

    public android.app.NotificationChannelGroup getNotificationChannelGroup(java.lang.String groupId, java.lang.String pkg, int uid) {
        java.util.Objects.requireNonNull(pkg);
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return null;
            }
            return r.groups.get(groupId);
        }
    }

    public android.content.pm.ParceledListSlice<android.app.NotificationChannelGroup> getNotificationChannelGroups(java.lang.String pkg, int uid, boolean includeDeleted, boolean includeNonGrouped, boolean includeEmpty, boolean includeBlocked, java.util.Set<java.lang.String> activeChannelFilter) {
        java.util.Objects.requireNonNull(pkg);
        java.util.Map<java.lang.String, android.app.NotificationChannelGroup> groups = new android.util.ArrayMap<>();
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return android.content.pm.ParceledListSlice.emptyList();
            }
            android.app.NotificationChannelGroup nonGrouped = new android.app.NotificationChannelGroup(null, null);
            int N = r.channels.size();
            for (int i = 0; i < N; i++) {
                android.app.NotificationChannel nc = r.channels.valueAt(i);
                boolean includeChannel = (includeDeleted || !nc.isDeleted()) && (activeChannelFilter == null || ((includeBlocked && nc.getImportance() == 0) || activeChannelFilter.contains(nc.getId())));
                if (includeChannel) {
                    if (nc.getGroup() != null) {
                        if (r.groups.get(nc.getGroup()) != null) {
                            android.app.NotificationChannelGroup ncg = groups.get(nc.getGroup());
                            if (ncg == null) {
                                ncg = r.groups.get(nc.getGroup()).clone();
                                ncg.setChannels(new java.util.ArrayList());
                                groups.put(nc.getGroup(), ncg);
                            }
                            ncg.addChannel(nc);
                        }
                    } else {
                        nonGrouped.addChannel(nc);
                    }
                }
            }
            if (includeNonGrouped && nonGrouped.getChannels().size() > 0) {
                groups.put(null, nonGrouped);
            }
            if (includeEmpty) {
                for (android.app.NotificationChannelGroup group : r.groups.values()) {
                    if (!groups.containsKey(group.getId())) {
                        groups.put(group.getId(), group);
                    }
                }
            }
            return new android.content.pm.ParceledListSlice<>(new java.util.ArrayList(groups.values()));
        }
    }

    public java.util.List<android.app.NotificationChannel> deleteNotificationChannelGroup(java.lang.String pkg, int uid, java.lang.String groupId, int callingUid, boolean fromSystemOrSystemUi) {
        java.util.List<android.app.NotificationChannel> deletedChannels = new java.util.ArrayList<>();
        boolean groupBypassedDnd = false;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r != null && !android.text.TextUtils.isEmpty(groupId)) {
                android.app.NotificationChannelGroup channelGroup = r.groups.remove(groupId);
                if (channelGroup != null) {
                    this.mNotificationChannelLogger.logNotificationChannelGroupDeleted(channelGroup, uid, pkg);
                }
                int N = r.channels.size();
                for (int i = 0; i < N; i++) {
                    android.app.NotificationChannel nc = r.channels.valueAt(i);
                    if (groupId.equals(nc.getGroup())) {
                        groupBypassedDnd |= nc.canBypassDnd();
                        deleteNotificationChannelLocked(nc, pkg, uid);
                        deletedChannels.add(nc);
                    }
                }
                if (groupBypassedDnd) {
                    updateCurrentUserHasChannelsBypassingDnd(callingUid, fromSystemOrSystemUi);
                }
                return deletedChannels;
            }
            return deletedChannels;
        }
    }

    @Override // com.android.server.notification.RankingConfig
    public java.util.Collection<android.app.NotificationChannelGroup> getNotificationChannelGroups(java.lang.String pkg, int uid) {
        java.util.List<android.app.NotificationChannelGroup> groups = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return groups;
            }
            groups.addAll(r.groups.values());
            return groups;
        }
    }

    public android.app.NotificationChannelGroup getGroupForChannel(java.lang.String pkg, int uid, java.lang.String channelId) {
        android.app.NotificationChannel nc;
        android.app.NotificationChannelGroup group;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences p = getPackagePreferencesLocked(pkg, uid);
            if (p == null || (nc = p.channels.get(channelId)) == null || nc.isDeleted() || nc.getGroup() == null || (group = p.groups.get(nc.getGroup())) == null) {
                return null;
            }
            return group;
        }
    }

    public java.util.ArrayList<android.service.notification.ConversationChannelWrapper> getConversations(android.util.IntArray userIds, boolean onlyImportant) {
        java.util.ArrayList<android.service.notification.ConversationChannelWrapper> conversations;
        java.lang.CharSequence name;
        android.app.NotificationChannelGroup group;
        synchronized (this.mLock) {
            conversations = new java.util.ArrayList<>();
            for (com.android.server.notification.PreferencesHelper.PackagePreferences p : this.mPackagePreferences.values()) {
                if (userIds.binarySearch(android.os.UserHandle.getUserId(p.uid)) >= 0) {
                    int N = p.channels.size();
                    for (int i = 0; i < N; i++) {
                        android.app.NotificationChannel nc = p.channels.valueAt(i);
                        if (!android.text.TextUtils.isEmpty(nc.getConversationId()) && !nc.isDeleted() && !nc.isDemoted() && (nc.isImportantConversation() || !onlyImportant)) {
                            android.service.notification.ConversationChannelWrapper conversation = new android.service.notification.ConversationChannelWrapper();
                            conversation.setPkg(p.pkg);
                            conversation.setUid(p.uid);
                            conversation.setNotificationChannel(nc);
                            android.app.NotificationChannel parent = p.channels.get(nc.getParentChannelId());
                            if (parent == null) {
                                name = null;
                            } else {
                                name = parent.getName();
                            }
                            conversation.setParentChannelLabel(name);
                            boolean blockedByGroup = false;
                            if (nc.getGroup() != null && (group = p.groups.get(nc.getGroup())) != null) {
                                if (group.isBlocked()) {
                                    blockedByGroup = true;
                                } else {
                                    conversation.setGroupLabel(group.getName());
                                }
                            }
                            if (!blockedByGroup) {
                                conversations.add(conversation);
                            }
                        }
                    }
                }
            }
        }
        return conversations;
    }

    public java.util.ArrayList<android.service.notification.ConversationChannelWrapper> getConversations(java.lang.String pkg, int uid) {
        android.app.NotificationChannelGroup group;
        java.util.Objects.requireNonNull(pkg);
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return new java.util.ArrayList<>();
            }
            java.util.ArrayList<android.service.notification.ConversationChannelWrapper> conversations = new java.util.ArrayList<>();
            int N = r.channels.size();
            for (int i = 0; i < N; i++) {
                android.app.NotificationChannel nc = r.channels.valueAt(i);
                if (!android.text.TextUtils.isEmpty(nc.getConversationId()) && !nc.isDeleted() && !nc.isDemoted()) {
                    android.service.notification.ConversationChannelWrapper conversation = new android.service.notification.ConversationChannelWrapper();
                    conversation.setPkg(r.pkg);
                    conversation.setUid(r.uid);
                    conversation.setNotificationChannel(nc);
                    conversation.setParentChannelLabel(r.channels.get(nc.getParentChannelId()).getName());
                    boolean blockedByGroup = false;
                    if (nc.getGroup() != null && (group = r.groups.get(nc.getGroup())) != null) {
                        if (group.isBlocked()) {
                            blockedByGroup = true;
                        } else {
                            conversation.setGroupLabel(group.getName());
                        }
                    }
                    if (!blockedByGroup) {
                        conversations.add(conversation);
                    }
                }
            }
            return conversations;
        }
    }

    public java.util.List<java.lang.String> deleteConversations(java.lang.String pkg, int uid, java.util.Set<java.lang.String> conversationIds, int callingUid, boolean fromSystemOrSystemUi) {
        java.util.List<java.lang.String> deletedChannelIds = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return deletedChannelIds;
            }
            int N = r.channels.size();
            for (int i = 0; i < N; i++) {
                android.app.NotificationChannel nc = r.channels.valueAt(i);
                if (nc.getConversationId() != null && conversationIds.contains(nc.getConversationId())) {
                    nc.setDeleted(true);
                    nc.setDeletedTimeMs(java.lang.System.currentTimeMillis());
                    android.metrics.LogMaker lm = getChannelLog(nc, pkg);
                    lm.setType(2);
                    com.android.internal.logging.MetricsLogger.action(lm);
                    this.mNotificationChannelLogger.logNotificationChannelDeleted(nc, uid, pkg);
                    deletedChannelIds.add(nc.getId());
                }
            }
            if (!deletedChannelIds.isEmpty() && this.mCurrentUserHasChannelsBypassingDnd) {
                updateCurrentUserHasChannelsBypassingDnd(callingUid, fromSystemOrSystemUi);
            }
            return deletedChannelIds;
        }
    }

    @Override // com.android.server.notification.RankingConfig
    public android.content.pm.ParceledListSlice<android.app.NotificationChannel> getNotificationChannels(java.lang.String pkg, int uid, boolean includeDeleted) {
        java.util.Objects.requireNonNull(pkg);
        java.util.List<android.app.NotificationChannel> channels = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return android.content.pm.ParceledListSlice.emptyList();
            }
            int N = r.channels.size();
            for (int i = 0; i < N; i++) {
                android.app.NotificationChannel nc = r.channels.valueAt(i);
                if (includeDeleted || !nc.isDeleted()) {
                    channels.add(nc);
                }
            }
            return new android.content.pm.ParceledListSlice<>(channels);
        }
    }

    public android.content.pm.ParceledListSlice<android.app.NotificationChannel> getNotificationChannelsBypassingDnd(java.lang.String pkg, int uid) {
        java.util.List<android.app.NotificationChannel> channels = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = this.mPackagePreferences.get(packagePreferencesKey(pkg, uid));
            if (r != null) {
                for (android.app.NotificationChannel channel : r.channels.values()) {
                    if (channelIsLiveLocked(r, channel) && channel.canBypassDnd()) {
                        channels.add(channel);
                    }
                }
            }
        }
        return new android.content.pm.ParceledListSlice<>(channels);
    }

    public boolean onlyHasDefaultChannel(java.lang.String pkg, int uid) {
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getOrCreatePackagePreferencesLocked(pkg, uid);
            return r.channels.size() == 1 && r.channels.containsKey("miscellaneous");
        }
    }

    public int getDeletedChannelCount(java.lang.String pkg, int uid) {
        java.util.Objects.requireNonNull(pkg);
        int deletedCount = 0;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return 0;
            }
            int N = r.channels.size();
            for (int i = 0; i < N; i++) {
                android.app.NotificationChannel nc = r.channels.valueAt(i);
                if (nc.isDeleted()) {
                    deletedCount++;
                }
            }
            return deletedCount;
        }
    }

    public int getBlockedChannelCount(java.lang.String pkg, int uid) {
        java.util.Objects.requireNonNull(pkg);
        int blockedCount = 0;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = getPackagePreferencesLocked(pkg, uid);
            if (r == null) {
                return 0;
            }
            int N = r.channels.size();
            for (int i = 0; i < N; i++) {
                android.app.NotificationChannel nc = r.channels.valueAt(i);
                if (!nc.isDeleted() && nc.getImportance() == 0) {
                    blockedCount++;
                }
            }
            return blockedCount;
        }
    }

    void syncChannelsBypassingDnd() {
        this.mCurrentUserHasChannelsBypassingDnd = (this.mZenModeHelper.getNotificationPolicy().state & 1) != 0;
        updateCurrentUserHasChannelsBypassingDnd(1000, true);
    }

    private void updateCurrentUserHasChannelsBypassingDnd(int callingUid, boolean fromSystemOrSystemUi) {
        android.util.ArraySet<android.util.Pair<java.lang.String, java.lang.Integer>> candidatePkgs = new android.util.ArraySet<>();
        android.util.IntArray currentUserIds = this.mUserProfiles.getCurrentProfileIds();
        synchronized (this.mLock) {
            int numPackagePreferences = this.mPackagePreferences.size();
            for (int i = 0; i < numPackagePreferences; i++) {
                com.android.server.notification.PreferencesHelper.PackagePreferences r = this.mPackagePreferences.valueAt(i);
                if (currentUserIds.contains(android.os.UserHandle.getUserId(r.uid))) {
                    java.util.Iterator<android.app.NotificationChannel> it = r.channels.values().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        android.app.NotificationChannel channel = it.next();
                        if (channel != null) {
                            if (channelIsLiveLocked(r, channel) && channel.canBypassDnd()) {
                                candidatePkgs.add(new android.util.Pair<>(r.pkg, java.lang.Integer.valueOf(r.uid)));
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (int i2 = candidatePkgs.size() - 1; i2 >= 0; i2--) {
            android.util.Pair<java.lang.String, java.lang.Integer> app = candidatePkgs.valueAt(i2);
            if (!this.mPermissionHelper.hasPermission(((java.lang.Integer) app.second).intValue())) {
                candidatePkgs.removeAt(i2);
            }
        }
        int i3 = candidatePkgs.size();
        boolean haveBypassingApps = i3 > 0;
        if (this.mCurrentUserHasChannelsBypassingDnd != haveBypassingApps) {
            this.mCurrentUserHasChannelsBypassingDnd = haveBypassingApps;
            updateZenPolicy(this.mCurrentUserHasChannelsBypassingDnd, callingUid, fromSystemOrSystemUi);
        }
    }

    private boolean channelIsLiveLocked(com.android.server.notification.PreferencesHelper.PackagePreferences pkgPref, android.app.NotificationChannel channel) {
        return (isGroupBlocked(pkgPref.pkg, pkgPref.uid, channel.getGroup()) || channel.isDeleted() || channel.getImportance() == 0) ? false : true;
    }

    public void updateZenPolicy(boolean areChannelsBypassingDnd, int callingUid, boolean fromSystemOrSystemUi) {
        android.app.NotificationManager.Policy policy = this.mZenModeHelper.getNotificationPolicy();
        this.mZenModeHelper.setNotificationPolicy(new android.app.NotificationManager.Policy(policy.priorityCategories, policy.priorityCallSenders, policy.priorityMessageSenders, policy.suppressedVisualEffects, areChannelsBypassingDnd ? 1 : 0, policy.priorityConversationSenders), fromSystemOrSystemUi ? 5 : 4, callingUid);
    }

    public boolean areChannelsBypassingDnd() {
        return this.mCurrentUserHasChannelsBypassingDnd;
    }

    public void setAppImportanceLocked(java.lang.String packageName, int uid) {
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences prefs = getOrCreatePackagePreferencesLocked(packageName, uid);
            if ((prefs.lockedAppFields & 1) != 0) {
                return;
            }
            prefs.lockedAppFields |= 1;
            updateConfig();
        }
    }

    public java.lang.String getNotificationDelegate(java.lang.String sourcePkg, int sourceUid) {
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences prefs = getPackagePreferencesLocked(sourcePkg, sourceUid);
            if (prefs != null && prefs.delegate != null) {
                if (!prefs.delegate.mEnabled) {
                    return null;
                }
                return prefs.delegate.mPkg;
            }
            return null;
        }
    }

    public void setNotificationDelegate(java.lang.String sourcePkg, int sourceUid, java.lang.String delegatePkg, int delegateUid) {
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences prefs = getOrCreatePackagePreferencesLocked(sourcePkg, sourceUid);
            prefs.delegate = new com.android.server.notification.PreferencesHelper.Delegate(delegatePkg, delegateUid, true);
        }
    }

    public void revokeNotificationDelegate(java.lang.String sourcePkg, int sourceUid) {
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences prefs = getPackagePreferencesLocked(sourcePkg, sourceUid);
            if (prefs != null && prefs.delegate != null) {
                prefs.delegate.mEnabled = false;
            }
        }
    }

    public boolean isDelegateAllowed(java.lang.String sourcePkg, int sourceUid, java.lang.String potentialDelegatePkg, int potentialDelegateUid) {
        boolean z;
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences prefs = getPackagePreferencesLocked(sourcePkg, sourceUid);
            z = prefs != null && prefs.isValidDelegate(potentialDelegatePkg, potentialDelegateUid);
        }
        return z;
    }

    private void lockFieldsForUpdateLocked(android.app.NotificationChannel original, android.app.NotificationChannel update) {
        if (original.canBypassDnd() != update.canBypassDnd()) {
            update.lockFields(1);
        }
        if (original.getLockscreenVisibility() != update.getLockscreenVisibility()) {
            update.lockFields(2);
        }
        if (original.getImportance() != update.getImportance()) {
            update.lockFields(4);
        }
        if (original.shouldShowLights() != update.shouldShowLights() || original.getLightColor() != update.getLightColor()) {
            update.lockFields(8);
        }
        if (!java.util.Objects.equals(original.getSound(), update.getSound())) {
            update.lockFields(32);
        }
        if (!java.util.Arrays.equals(original.getVibrationPattern(), update.getVibrationPattern()) || !java.util.Objects.equals(original.getVibrationEffect(), update.getVibrationEffect()) || original.shouldVibrate() != update.shouldVibrate()) {
            update.lockFields(16);
        }
        if (original.canShowBadge() != update.canShowBadge()) {
            update.lockFields(128);
        }
        if (original.getAllowBubbles() != update.getAllowBubbles()) {
            update.lockFields(256);
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.notification.NotificationManagerService.DumpFilter filter, android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> pkgPermissions) {
        pw.print(prefix);
        pw.println("per-package config version: " + this.XML_VERSION);
        pw.println("PackagePreferences:");
        synchronized (this.mLock) {
            dumpPackagePreferencesLocked(pw, prefix, filter, this.mPackagePreferences, pkgPermissions);
            pw.println("Restored without uid:");
            dumpPackagePreferencesLocked(pw, prefix, filter, this.mRestoredWithoutUids, (android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>>) null);
        }
    }

    public void dump(android.util.proto.ProtoOutputStream proto, com.android.server.notification.NotificationManagerService.DumpFilter filter, android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> pkgPermissions) {
        synchronized (this.mLock) {
            dumpPackagePreferencesLocked(proto, 2246267895810L, filter, this.mPackagePreferences, pkgPermissions);
            dumpPackagePreferencesLocked(proto, 2246267895811L, filter, this.mRestoredWithoutUids, (android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>>) null);
        }
    }

    private void dumpPackagePreferencesLocked(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.notification.NotificationManagerService.DumpFilter filter, android.util.ArrayMap<java.lang.String, com.android.server.notification.PreferencesHelper.PackagePreferences> packagePreferences, android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> packagePermissions) {
        java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> pkgsWithPermissionsToHandle = packagePermissions != null ? packagePermissions.keySet() : null;
        int N = packagePreferences.size();
        int i = 0;
        while (true) {
            if (i >= N) {
                break;
            }
            com.android.server.notification.PreferencesHelper.PackagePreferences r = packagePreferences.valueAt(i);
            if (filter.matches(r.pkg)) {
                pw.print(prefix);
                pw.print("  AppSettings: ");
                pw.print(r.pkg);
                pw.print(" (");
                pw.print(r.uid != -10000 ? java.lang.Integer.toString(r.uid) : "UNKNOWN_UID");
                pw.print(')');
                android.util.Pair<java.lang.Integer, java.lang.String> key = new android.util.Pair<>(java.lang.Integer.valueOf(r.uid), r.pkg);
                if (packagePermissions != null && pkgsWithPermissionsToHandle.contains(key)) {
                    pw.print(" importance=");
                    pw.print(android.service.notification.NotificationListenerService.Ranking.importanceToString(((java.lang.Boolean) packagePermissions.get(key).first).booleanValue() ? 3 : 0));
                    pw.print(" userSet=");
                    pw.print(packagePermissions.get(key).second);
                    pkgsWithPermissionsToHandle.remove(key);
                }
                if (r.priority != 0) {
                    pw.print(" priority=");
                    pw.print(android.app.Notification.priorityToString(r.priority));
                }
                if (r.visibility != -1000) {
                    pw.print(" visibility=");
                    pw.print(android.app.Notification.visibilityToString(r.visibility));
                }
                if (!r.showBadge) {
                    pw.print(" showBadge=");
                    pw.print(r.showBadge);
                }
                if (r.defaultAppLockedImportance) {
                    pw.print(" defaultAppLocked=");
                    pw.print(r.defaultAppLockedImportance);
                }
                if (r.fixedImportance) {
                    pw.print(" fixedImportance=");
                    pw.print(r.fixedImportance);
                }
                pw.println();
                for (android.app.NotificationChannel channel : r.channels.values()) {
                    pw.print(prefix);
                    channel.dump(pw, "    ", filter.redact);
                }
                for (android.app.NotificationChannelGroup group : r.groups.values()) {
                    pw.print(prefix);
                    pw.print("  ");
                    pw.print("  ");
                    pw.println(group);
                }
            }
            i++;
        }
        if (pkgsWithPermissionsToHandle != null) {
            for (android.util.Pair<java.lang.Integer, java.lang.String> p : pkgsWithPermissionsToHandle) {
                if (filter.matches((java.lang.String) p.second)) {
                    pw.print(prefix);
                    pw.print("  AppSettings: ");
                    pw.print((java.lang.String) p.second);
                    pw.print(" (");
                    pw.print(((java.lang.Integer) p.first).intValue() == -10000 ? "UNKNOWN_UID" : java.lang.Integer.toString(((java.lang.Integer) p.first).intValue()));
                    pw.print(')');
                    pw.print(" importance=");
                    pw.print(android.service.notification.NotificationListenerService.Ranking.importanceToString(((java.lang.Boolean) packagePermissions.get(p).first).booleanValue() ? 3 : 0));
                    pw.print(" userSet=");
                    pw.print(packagePermissions.get(p).second);
                    pw.println();
                }
            }
        }
    }

    private void dumpPackagePreferencesLocked(android.util.proto.ProtoOutputStream proto, long fieldId, com.android.server.notification.NotificationManagerService.DumpFilter filter, android.util.ArrayMap<java.lang.String, com.android.server.notification.PreferencesHelper.PackagePreferences> packagePreferences, android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> packagePermissions) {
        java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> pkgsWithPermissionsToHandle = packagePermissions != null ? packagePermissions.keySet() : null;
        int N = packagePreferences.size();
        for (int i = 0; i < N; i++) {
            com.android.server.notification.PreferencesHelper.PackagePreferences r = packagePreferences.valueAt(i);
            if (filter.matches(r.pkg)) {
                long fToken = proto.start(fieldId);
                proto.write(1138166333441L, r.pkg);
                proto.write(1120986464258L, r.uid);
                android.util.Pair<java.lang.Integer, java.lang.String> key = new android.util.Pair<>(java.lang.Integer.valueOf(r.uid), r.pkg);
                if (packagePermissions != null && pkgsWithPermissionsToHandle.contains(key)) {
                    proto.write(1172526071811L, ((java.lang.Boolean) packagePermissions.get(key).first).booleanValue() ? 3 : 0);
                    pkgsWithPermissionsToHandle.remove(key);
                }
                proto.write(1120986464260L, r.priority);
                proto.write(1172526071813L, r.visibility);
                proto.write(1133871366150L, r.showBadge);
                for (android.app.NotificationChannel channel : r.channels.values()) {
                    channel.dumpDebug(proto, 2246267895815L);
                }
                for (android.app.NotificationChannelGroup group : r.groups.values()) {
                    group.dumpDebug(proto, 2246267895816L);
                }
                proto.end(fToken);
            }
        }
        if (pkgsWithPermissionsToHandle != null) {
            for (android.util.Pair<java.lang.Integer, java.lang.String> p : pkgsWithPermissionsToHandle) {
                if (filter.matches((java.lang.String) p.second)) {
                    long fToken2 = proto.start(fieldId);
                    proto.write(1138166333441L, (java.lang.String) p.second);
                    proto.write(1120986464258L, ((java.lang.Integer) p.first).intValue());
                    proto.write(1172526071811L, ((java.lang.Boolean) packagePermissions.get(p).first).booleanValue() ? 3 : 0);
                    proto.end(fToken2);
                }
            }
        }
    }

    int getFsiState(java.lang.String pkg, int uid, boolean requestedFSIPermission) {
        if (!requestedFSIPermission) {
            return 0;
        }
        android.content.AttributionSource attributionSource = new android.content.AttributionSource.Builder(uid).setPackageName(pkg).build();
        int result = this.mPermissionManager.checkPermissionForPreflight("android.permission.USE_FULL_SCREEN_INTENT", attributionSource);
        if (result == 0) {
            return 1;
        }
        return 2;
    }

    boolean isFsiPermissionUserSet(java.lang.String pkg, int uid, int fsiState, int currentPermissionFlags) {
        return (fsiState == 0 || (currentPermissionFlags & 1) == 0) ? false : true;
    }

    public void pullPackagePreferencesStats(java.util.List<android.util.StatsEvent> events, android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> pkgPermissions) {
        java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> pkgsWithPermissionsToHandle;
        if (pkgPermissions == null) {
            pkgsWithPermissionsToHandle = null;
        } else {
            java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> pkgsWithPermissionsToHandle2 = pkgPermissions.keySet();
            pkgsWithPermissionsToHandle = pkgsWithPermissionsToHandle2;
        }
        synchronized (this.mLock) {
            int pulledEvents = 0;
            int pulledEvents2 = 0;
            while (true) {
                try {
                    int i = 3;
                    if (pulledEvents2 >= this.mPackagePreferences.size() || pulledEvents > 1000) {
                        break;
                    }
                    int pulledEvents3 = pulledEvents + 1;
                    try {
                        com.android.server.notification.PreferencesHelper.PackagePreferences r = this.mPackagePreferences.valueAt(pulledEvents2);
                        boolean importanceIsUserSet = false;
                        int importance = -1000;
                        android.util.Pair<java.lang.Integer, java.lang.String> key = new android.util.Pair<>(java.lang.Integer.valueOf(r.uid), r.pkg);
                        if (pkgPermissions != null && pkgsWithPermissionsToHandle.contains(key)) {
                            android.util.Pair<java.lang.Boolean, java.lang.Boolean> permissionPair = pkgPermissions.get(key);
                            if (!((java.lang.Boolean) permissionPair.first).booleanValue()) {
                                i = 0;
                            }
                            importance = i;
                            importanceIsUserSet = ((java.lang.Boolean) permissionPair.second).booleanValue();
                            pkgsWithPermissionsToHandle.remove(key);
                        }
                        boolean requestedFSIPermission = this.mPermissionHelper.hasRequestedPermission("android.permission.USE_FULL_SCREEN_INTENT", r.pkg, r.uid);
                        int fsiState = getFsiState(r.pkg, r.uid, requestedFSIPermission);
                        android.content.pm.PackageManager packageManager = this.mPm;
                        java.lang.String str = r.pkg;
                        try {
                            int pulledEvents4 = r.uid;
                            int currentPermissionFlags = packageManager.getPermissionFlags("android.permission.USE_FULL_SCREEN_INTENT", str, android.os.UserHandle.getUserHandleForUid(pulledEvents4));
                            boolean fsiIsUserSet = isFsiPermissionUserSet(r.pkg, r.uid, fsiState, currentPermissionFlags);
                            events.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_PREFERENCES, r.uid, importance, r.visibility, r.lockedAppFields, importanceIsUserSet, fsiState, fsiIsUserSet));
                            pulledEvents2++;
                            pulledEvents = pulledEvents3;
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            }
            if (pkgPermissions != null) {
                for (android.util.Pair<java.lang.Integer, java.lang.String> p : pkgsWithPermissionsToHandle) {
                    if (pulledEvents <= 1000) {
                        pulledEvents++;
                        events.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_PREFERENCES, ((java.lang.Integer) p.first).intValue(), ((java.lang.Boolean) pkgPermissions.get(p).first).booleanValue() ? 3 : 0, -1000, 0, ((java.lang.Boolean) pkgPermissions.get(p).second).booleanValue(), 0, false));
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public void pullPackageChannelPreferencesStats(java.util.List<android.util.StatsEvent> events) {
        synchronized (this.mLock) {
            int totalChannelsPulled = 0;
            for (int i = 0; i < this.mPackagePreferences.size() && totalChannelsPulled <= 2000; i++) {
                try {
                    com.android.server.notification.PreferencesHelper.PackagePreferences r = this.mPackagePreferences.valueAt(i);
                    for (android.app.NotificationChannel channel : r.channels.values()) {
                        totalChannelsPulled++;
                        if (totalChannelsPulled > 2000) {
                            break;
                        } else {
                            events.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_CHANNEL_PREFERENCES, r.uid, channel.getId(), channel.getName().toString(), channel.getDescription(), channel.getImportance(), channel.getUserLockedFields(), channel.isDeleted(), channel.getConversationId() != null, channel.isDemoted(), channel.isImportantConversation()));
                        }
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                }
                try {
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        }
    }

    public void pullPackageChannelGroupPreferencesStats(java.util.List<android.util.StatsEvent> events) {
        synchronized (this.mLock) {
            int totalGroupsPulled = 0;
            for (int i = 0; i < this.mPackagePreferences.size() && totalGroupsPulled <= 1000; i++) {
                com.android.server.notification.PreferencesHelper.PackagePreferences r = this.mPackagePreferences.valueAt(i);
                for (android.app.NotificationChannelGroup groupChannel : r.groups.values()) {
                    totalGroupsPulled++;
                    if (totalGroupsPulled > 1000) {
                        break;
                    } else {
                        events.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_CHANNEL_GROUP_PREFERENCES, r.uid, groupChannel.getId(), groupChannel.getName().toString(), groupChannel.getDescription(), groupChannel.isBlocked(), groupChannel.getUserLockedFields()));
                    }
                }
            }
        }
    }

    public org.json.JSONObject dumpJson(com.android.server.notification.NotificationManagerService.DumpFilter filter, android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> pkgPermissions) {
        java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> pkgsWithPermissionsToHandle;
        org.json.JSONObject jSONObject = new org.json.JSONObject();
        org.json.JSONArray jSONArray = new org.json.JSONArray();
        synchronized (this.mLock) {
            try {
                jSONObject.put("noUid", this.mRestoredWithoutUids.size());
            } catch (org.json.JSONException e) {
            }
        }
        if (pkgPermissions == null) {
            pkgsWithPermissionsToHandle = null;
        } else {
            java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> pkgsWithPermissionsToHandle2 = pkgPermissions.keySet();
            pkgsWithPermissionsToHandle = pkgsWithPermissionsToHandle2;
        }
        synchronized (this.mLock) {
            int N = this.mPackagePreferences.size();
            int i = 0;
            while (true) {
                int i2 = 3;
                if (i >= N) {
                    break;
                }
                com.android.server.notification.PreferencesHelper.PackagePreferences r = this.mPackagePreferences.valueAt(i);
                if (filter == null || filter.matches(r.pkg)) {
                    org.json.JSONObject PackagePreferences2 = new org.json.JSONObject();
                    try {
                        PackagePreferences2.put("userId", android.os.UserHandle.getUserId(r.uid));
                        PackagePreferences2.put(com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, r.pkg);
                        android.util.Pair<java.lang.Integer, java.lang.String> key = new android.util.Pair<>(java.lang.Integer.valueOf(r.uid), r.pkg);
                        if (pkgPermissions != null && pkgsWithPermissionsToHandle.contains(key)) {
                            if (!((java.lang.Boolean) pkgPermissions.get(key).first).booleanValue()) {
                                i2 = 0;
                            }
                            PackagePreferences2.put(ATT_IMPORTANCE, android.service.notification.NotificationListenerService.Ranking.importanceToString(i2));
                            pkgsWithPermissionsToHandle.remove(key);
                        }
                        if (r.priority != 0) {
                            PackagePreferences2.put(ATT_PRIORITY, android.app.Notification.priorityToString(r.priority));
                        }
                        if (r.visibility != -1000) {
                            PackagePreferences2.put(ATT_VISIBILITY, android.app.Notification.visibilityToString(r.visibility));
                        }
                        if (!r.showBadge) {
                            PackagePreferences2.put("showBadge", java.lang.Boolean.valueOf(r.showBadge));
                        }
                        org.json.JSONArray channels = new org.json.JSONArray();
                        for (android.app.NotificationChannel channel : r.channels.values()) {
                            channels.put(channel.toJson());
                        }
                        PackagePreferences2.put("channels", channels);
                        org.json.JSONArray groups = new org.json.JSONArray();
                        for (android.app.NotificationChannelGroup group : r.groups.values()) {
                            groups.put(group.toJson());
                            key = key;
                        }
                        PackagePreferences2.put("groups", groups);
                    } catch (org.json.JSONException e2) {
                    }
                    jSONArray.put(PackagePreferences2);
                }
                i++;
            }
        }
        if (pkgsWithPermissionsToHandle != null) {
            for (android.util.Pair<java.lang.Integer, java.lang.String> p : pkgsWithPermissionsToHandle) {
                if (filter == null || filter.matches((java.lang.String) p.second)) {
                    org.json.JSONObject PackagePreferences3 = new org.json.JSONObject();
                    try {
                        PackagePreferences3.put("userId", android.os.UserHandle.getUserId(((java.lang.Integer) p.first).intValue()));
                        PackagePreferences3.put(com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, p.second);
                        PackagePreferences3.put(ATT_IMPORTANCE, android.service.notification.NotificationListenerService.Ranking.importanceToString(((java.lang.Boolean) pkgPermissions.get(p).first).booleanValue() ? 3 : 0));
                    } catch (org.json.JSONException e3) {
                    }
                    jSONArray.put(PackagePreferences3);
                }
            }
        }
        try {
            jSONObject.put("PackagePreferencess", jSONArray);
        } catch (org.json.JSONException e4) {
        }
        return jSONObject;
    }

    public org.json.JSONArray dumpBansJson(com.android.server.notification.NotificationManagerService.DumpFilter filter, android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> pkgPermissions) {
        org.json.JSONArray bans = new org.json.JSONArray();
        java.util.Map<java.lang.Integer, java.lang.String> packageBans = getPermissionBasedPackageBans(pkgPermissions);
        for (java.util.Map.Entry<java.lang.Integer, java.lang.String> ban : packageBans.entrySet()) {
            int userId = android.os.UserHandle.getUserId(ban.getKey().intValue());
            java.lang.String packageName = ban.getValue();
            if (filter == null || filter.matches(packageName)) {
                org.json.JSONObject banJson = new org.json.JSONObject();
                try {
                    banJson.put("userId", userId);
                    banJson.put(com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, packageName);
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                bans.put(banJson);
            }
        }
        return bans;
    }

    public java.util.Map<java.lang.Integer, java.lang.String> getPackageBans() {
        android.util.ArrayMap<java.lang.Integer, java.lang.String> packageBans;
        synchronized (this.mLock) {
            int N = this.mPackagePreferences.size();
            packageBans = new android.util.ArrayMap<>(N);
            for (int i = 0; i < N; i++) {
                com.android.server.notification.PreferencesHelper.PackagePreferences r = this.mPackagePreferences.valueAt(i);
                if (r.importance == 0) {
                    packageBans.put(java.lang.Integer.valueOf(r.uid), r.pkg);
                }
            }
        }
        return packageBans;
    }

    protected java.util.Map<java.lang.Integer, java.lang.String> getPermissionBasedPackageBans(android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> pkgPermissions) {
        android.util.ArrayMap<java.lang.Integer, java.lang.String> packageBans = new android.util.ArrayMap<>();
        if (pkgPermissions != null) {
            for (android.util.Pair<java.lang.Integer, java.lang.String> p : pkgPermissions.keySet()) {
                if (!((java.lang.Boolean) pkgPermissions.get(p).first).booleanValue()) {
                    packageBans.put((java.lang.Integer) p.first, (java.lang.String) p.second);
                }
            }
        }
        return packageBans;
    }

    public org.json.JSONArray dumpChannelsJson(com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        org.json.JSONArray channels = new org.json.JSONArray();
        java.util.Map<java.lang.String, java.lang.Integer> packageChannels = getPackageChannels();
        for (java.util.Map.Entry<java.lang.String, java.lang.Integer> channelCount : packageChannels.entrySet()) {
            java.lang.String packageName = channelCount.getKey();
            if (filter == null || filter.matches(packageName)) {
                org.json.JSONObject channelCountJson = new org.json.JSONObject();
                try {
                    channelCountJson.put(com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, packageName);
                    channelCountJson.put("channelCount", channelCount.getValue());
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                channels.put(channelCountJson);
            }
        }
        return channels;
    }

    private java.util.Map<java.lang.String, java.lang.Integer> getPackageChannels() {
        android.util.ArrayMap<java.lang.String, java.lang.Integer> packageChannels = new android.util.ArrayMap<>();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mPackagePreferences.size(); i++) {
                com.android.server.notification.PreferencesHelper.PackagePreferences r = this.mPackagePreferences.valueAt(i);
                int channelCount = 0;
                for (int j = 0; j < r.channels.size(); j++) {
                    if (!r.channels.valueAt(j).isDeleted()) {
                        channelCount++;
                    }
                }
                packageChannels.put(r.pkg, java.lang.Integer.valueOf(channelCount));
            }
        }
        return packageChannels;
    }

    public void onUserRemoved(int userId) {
        synchronized (this.mLock) {
            int N = this.mPackagePreferences.size();
            for (int i = N - 1; i >= 0; i--) {
                com.android.server.notification.PreferencesHelper.PackagePreferences PackagePreferences2 = this.mPackagePreferences.valueAt(i);
                if (android.os.UserHandle.getUserId(PackagePreferences2.uid) == userId) {
                    this.mPackagePreferences.removeAt(i);
                }
            }
        }
    }

    protected void onLocaleChanged(android.content.Context context, int userId) {
        synchronized (this.mLock) {
            int N = this.mPackagePreferences.size();
            for (int i = 0; i < N; i++) {
                com.android.server.notification.PreferencesHelper.PackagePreferences PackagePreferences2 = this.mPackagePreferences.valueAt(i);
                if (android.os.UserHandle.getUserId(PackagePreferences2.uid) == userId && PackagePreferences2.channels.containsKey("miscellaneous")) {
                    PackagePreferences2.channels.get("miscellaneous").setName(context.getResources().getString(android.R.string.demo_starting_message));
                }
            }
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:79:? -> B:60:0x0120). Please report as a decompilation issue!!! */
    public boolean onPackagesChanged(boolean removingPackage, int changeUserId, java.lang.String[] pkgList, int[] uidList) throws java.lang.Throwable {
        boolean updated;
        int i;
        java.lang.String[] strArr = pkgList;
        if (strArr == null || strArr.length == 0) {
            return false;
        }
        if (removingPackage) {
            int size = java.lang.Math.min(strArr.length, uidList.length);
            updated = false;
            for (int i2 = 0; i2 < size; i2++) {
                java.lang.String pkg = strArr[i2];
                int uid = uidList[i2];
                synchronized (this.mLock) {
                    this.mPackagePreferences.remove(packagePreferencesKey(pkg, uid));
                    this.mRestoredWithoutUids.remove(unrestoredPackageKey(pkg, changeUserId));
                }
                updated = true;
            }
        } else {
            int length = strArr.length;
            boolean updated2 = false;
            int i3 = 0;
            while (i3 < length) {
                java.lang.String pkg2 = strArr[i3];
                try {
                    int uid2 = this.mPm.getPackageUidAsUser(pkg2, changeUserId);
                    com.android.server.notification.PermissionHelper.PackagePermission p = null;
                    synchronized (this.mLock) {
                        try {
                            com.android.server.notification.PreferencesHelper.PackagePreferences r = this.mRestoredWithoutUids.get(unrestoredPackageKey(pkg2, changeUserId));
                            if (r == null) {
                                i = length;
                            } else {
                                r.uid = uid2;
                                this.mRestoredWithoutUids.remove(unrestoredPackageKey(pkg2, changeUserId));
                                this.mPackagePreferences.put(packagePreferencesKey(r.pkg, r.uid), r);
                                for (android.app.NotificationChannel channel : r.channels.values()) {
                                    if (channel.isSoundRestored()) {
                                        i = length;
                                    } else {
                                        android.net.Uri uri = channel.getSound();
                                        android.net.Uri restoredUri = channel.restoreSoundUri(this.mContext, uri, true, channel.getAudioAttributes().getUsage());
                                        if (!android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.equals(restoredUri)) {
                                            i = length;
                                        } else {
                                            i = length;
                                            try {
                                                android.util.Log.w(TAG, "Could not restore sound: " + uri + " for channel: " + channel);
                                            } catch (java.lang.Throwable th) {
                                                th = th;
                                                throw th;
                                            }
                                        }
                                        channel.setSound(restoredUri, channel.getAudioAttributes());
                                    }
                                    length = i;
                                }
                                i = length;
                                if (r.migrateToPm) {
                                    p = new com.android.server.notification.PermissionHelper.PackagePermission(r.pkg, android.os.UserHandle.getUserId(r.uid), r.importance != 0, hasUserConfiguredSettings(r));
                                }
                                updated2 = true;
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            i = length;
                            throw th;
                        }
                    }
                    if (p != null) {
                        try {
                            this.mPermissionHelper.setNotificationPermission(p);
                        } catch (java.lang.Exception e) {
                            e = e;
                            android.util.Slog.e(TAG, "could not restore " + pkg2, e);
                        }
                    }
                } catch (java.lang.Exception e2) {
                    e = e2;
                    i = length;
                }
                try {
                    com.android.server.notification.PreferencesHelper.PackagePreferences fullPackagePreferences = getPackagePreferencesLocked(pkg2, this.mPm.getPackageUidAsUser(pkg2, changeUserId));
                    if (fullPackagePreferences != null) {
                        updated2 = updated2 | createDefaultChannelIfNeededLocked(fullPackagePreferences) | deleteDefaultChannelIfNeededLocked(fullPackagePreferences);
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e3) {
                }
                i3++;
                strArr = pkgList;
                length = i;
            }
            updated = updated2;
        }
        if (updated) {
            updateConfig();
        }
        return updated;
    }

    public void clearData(java.lang.String pkg, int uid) {
        synchronized (this.mLock) {
            com.android.server.notification.PreferencesHelper.PackagePreferences p = getPackagePreferencesLocked(pkg, uid);
            if (p != null) {
                p.channels = new android.util.ArrayMap<>();
                p.groups = new android.util.ArrayMap();
                p.delegate = null;
                p.lockedAppFields = 0;
                p.bubblePreference = 0;
                p.importance = -1000;
                p.priority = 0;
                p.visibility = -1000;
                p.showBadge = true;
                p.mPPWrapper.getPackagePreferencesExt().reset();
            }
        }
    }

    private android.metrics.LogMaker getChannelLog(android.app.NotificationChannel channel, java.lang.String pkg) {
        return new android.metrics.LogMaker(856).setType(6).setPackageName(pkg).addTaggedData(857, channel.getId()).addTaggedData(858, java.lang.Integer.valueOf(channel.getImportance()));
    }

    private android.metrics.LogMaker getChannelGroupLog(java.lang.String groupId, java.lang.String pkg) {
        return new android.metrics.LogMaker(859).setType(6).addTaggedData(860, groupId).setPackageName(pkg);
    }

    public void updateMediaNotificationFilteringEnabled() {
        boolean newValue = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "qs_media_controls", 1) > 0 && this.mContext.getResources().getBoolean(android.R.bool.config_notificationHeaderClickableForExpand);
        if (newValue != this.mIsMediaNotificationFilteringEnabled) {
            this.mIsMediaNotificationFilteringEnabled = newValue;
            updateConfig();
        }
    }

    @Override // com.android.server.notification.RankingConfig
    public boolean isMediaNotificationFilteringEnabled() {
        return this.mIsMediaNotificationFilteringEnabled;
    }

    public void updateBadgingEnabled() {
        if (this.mBadgingEnabled == null) {
            this.mBadgingEnabled = new android.util.SparseBooleanArray();
        }
        boolean changed = false;
        for (int index = 0; index < this.mBadgingEnabled.size(); index++) {
            int userId = this.mBadgingEnabled.keyAt(index);
            boolean oldValue = this.mBadgingEnabled.get(userId);
            boolean z = true;
            boolean newValue = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "notification_badging", 1, userId) != 0;
            this.mBadgingEnabled.put(userId, newValue);
            if (oldValue == newValue) {
                z = false;
            }
            changed |= z;
        }
        if (changed) {
            updateConfig();
        }
    }

    @Override // com.android.server.notification.RankingConfig
    public boolean badgingEnabled(android.os.UserHandle userHandle) {
        int userId = userHandle.getIdentifier();
        if (userId == -1) {
            return false;
        }
        if (this.mBadgingEnabled.indexOfKey(userId) < 0) {
            this.mBadgingEnabled.put(userId, android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "notification_badging", 1, userId) != 0);
        }
        return this.mBadgingEnabled.get(userId, true);
    }

    public void updateBubblesEnabled() {
        if (this.mBubblesEnabled == null) {
            this.mBubblesEnabled = new android.util.SparseBooleanArray();
        }
        boolean changed = false;
        for (int index = 0; index < this.mBubblesEnabled.size(); index++) {
            int userId = this.mBubblesEnabled.keyAt(index);
            boolean oldValue = this.mBubblesEnabled.get(userId);
            boolean z = true;
            boolean newValue = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "notification_bubbles", 1, userId) != 0;
            this.mBubblesEnabled.put(userId, newValue);
            if (oldValue == newValue) {
                z = false;
            }
            changed |= z;
        }
        if (changed) {
            updateConfig();
        }
    }

    @Override // com.android.server.notification.RankingConfig
    public boolean bubblesEnabled(android.os.UserHandle userHandle) {
        int userId = userHandle.getIdentifier();
        if (userId == -1) {
            return false;
        }
        if (this.mBubblesEnabled.indexOfKey(userId) < 0) {
            this.mBubblesEnabled.put(userId, android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "notification_bubbles", 1, userId) != 0);
        }
        return this.mBubblesEnabled.get(userId, true);
    }

    public void updateLockScreenPrivateNotifications() {
        if (this.mLockScreenPrivateNotifications == null) {
            this.mLockScreenPrivateNotifications = new android.util.SparseBooleanArray();
        }
        boolean changed = false;
        for (int index = 0; index < this.mLockScreenPrivateNotifications.size(); index++) {
            int userId = this.mLockScreenPrivateNotifications.keyAt(index);
            boolean oldValue = this.mLockScreenPrivateNotifications.get(userId);
            boolean z = true;
            boolean newValue = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "lock_screen_allow_private_notifications", 1, userId) != 0;
            this.mLockScreenPrivateNotifications.put(userId, newValue);
            if (oldValue == newValue) {
                z = false;
            }
            changed |= z;
        }
        if (changed) {
            updateConfig();
        }
    }

    public void updateLockScreenShowNotifications() {
        if (this.mLockScreenShowNotifications == null) {
            this.mLockScreenShowNotifications = new android.util.SparseBooleanArray();
        }
        boolean changed = false;
        for (int index = 0; index < this.mLockScreenShowNotifications.size(); index++) {
            int userId = this.mLockScreenShowNotifications.keyAt(index);
            boolean oldValue = this.mLockScreenShowNotifications.get(userId);
            boolean z = true;
            boolean newValue = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "lock_screen_show_notifications", 1, userId) != 0;
            this.mLockScreenShowNotifications.put(userId, newValue);
            if (oldValue == newValue) {
                z = false;
            }
            changed |= z;
        }
        if (changed) {
            updateConfig();
        }
    }

    @Override // com.android.server.notification.RankingConfig
    public boolean canShowNotificationsOnLockscreen(int userId) {
        if (this.mLockScreenShowNotifications == null) {
            this.mLockScreenShowNotifications = new android.util.SparseBooleanArray();
        }
        return this.mLockScreenShowNotifications.get(userId, true);
    }

    @Override // com.android.server.notification.RankingConfig
    public boolean canShowPrivateNotificationsOnLockScreen(int userId) {
        if (this.mLockScreenPrivateNotifications == null) {
            this.mLockScreenPrivateNotifications = new android.util.SparseBooleanArray();
        }
        return this.mLockScreenPrivateNotifications.get(userId, true);
    }

    public void unlockAllNotificationChannels() {
        synchronized (this.mLock) {
            int numPackagePreferences = this.mPackagePreferences.size();
            for (int i = 0; i < numPackagePreferences; i++) {
                com.android.server.notification.PreferencesHelper.PackagePreferences r = this.mPackagePreferences.valueAt(i);
                for (android.app.NotificationChannel channel : r.channels.values()) {
                    channel.unlockFields(4);
                }
            }
        }
    }

    public void migrateNotificationPermissions(java.util.List<android.content.pm.UserInfo> users) {
        for (android.content.pm.UserInfo user : users) {
            java.util.List<android.content.pm.PackageInfo> packages = this.mPm.getInstalledPackagesAsUser(android.content.pm.PackageManager.PackageInfoFlags.of(131072L), user.getUserHandle().getIdentifier());
            for (android.content.pm.PackageInfo pi : packages) {
                synchronized (this.mLock) {
                    com.android.server.notification.PreferencesHelper.PackagePreferences p = getOrCreatePackagePreferencesLocked(pi.packageName, pi.applicationInfo.uid);
                    if (p.migrateToPm && p.uid != -10000) {
                        try {
                            com.android.server.notification.PermissionHelper.PackagePermission pkgPerm = new com.android.server.notification.PermissionHelper.PackagePermission(p.pkg, android.os.UserHandle.getUserId(p.uid), p.importance != 0, hasUserConfiguredSettings(p));
                            this.mPermissionHelper.setNotificationPermission(pkgPerm);
                        } catch (java.lang.Exception e) {
                            android.util.Slog.e(TAG, "could not migrate setting for " + p.pkg, e);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfig() {
        this.mRankingHandler.requestSort();
    }

    private static java.lang.String packagePreferencesKey(java.lang.String pkg, int uid) {
        return pkg + "|" + uid;
    }

    private static java.lang.String unrestoredPackageKey(java.lang.String pkg, int userId) {
        return pkg + "|" + userId;
    }

    private static class PackagePreferences {
        int bubblePreference;
        android.util.ArrayMap<java.lang.String, android.app.NotificationChannel> channels;
        long creationTime;
        boolean defaultAppLockedImportance;
        com.android.server.notification.PreferencesHelper.Delegate delegate;
        boolean fixedImportance;
        java.util.Map<java.lang.String, android.app.NotificationChannelGroup> groups;
        boolean hasSentInvalidMessage;
        boolean hasSentValidBubble;
        boolean hasSentValidMessage;
        int importance;
        int lockedAppFields;
        private com.android.server.notification.IPackagePreferencesWrapper mPPWrapper;
        private com.android.server.notification.IPackagePreferencesExt mPackagePreferencesExt;
        boolean migrateToPm;
        java.lang.String pkg;
        int priority;
        boolean showBadge;
        int uid;
        boolean userDemotedMsgApp;
        int userId;
        int visibility;

        private PackagePreferences() {
            this.uid = -10000;
            this.importance = -1000;
            this.priority = 0;
            this.visibility = -1000;
            this.showBadge = true;
            this.bubblePreference = 0;
            this.lockedAppFields = 0;
            this.defaultAppLockedImportance = false;
            this.fixedImportance = false;
            this.hasSentInvalidMessage = false;
            this.hasSentValidMessage = false;
            this.userDemotedMsgApp = false;
            this.hasSentValidBubble = false;
            this.migrateToPm = false;
            this.delegate = null;
            this.channels = new android.util.ArrayMap<>();
            this.groups = new java.util.concurrent.ConcurrentHashMap();
            this.mPPWrapper = new com.android.server.notification.PreferencesHelper.PackagePreferences.PackagePreferencesWrapper();
            this.mPackagePreferencesExt = (com.android.server.notification.IPackagePreferencesExt) system.ext.loader.core.ExtLoader.type(com.android.server.notification.IPackagePreferencesExt.class).create();
        }

        public boolean isValidDelegate(java.lang.String pkg, int uid) {
            return this.delegate != null && this.delegate.isAllowed(pkg, uid);
        }

        public com.android.server.notification.IPackagePreferencesWrapper getWrapper() {
            return this.mPPWrapper;
        }

        private class PackagePreferencesWrapper implements com.android.server.notification.IPackagePreferencesWrapper {
            private PackagePreferencesWrapper() {
            }

            @Override // com.android.server.notification.IPackagePreferencesWrapper
            public com.android.server.notification.IPackagePreferencesExt getPackagePreferencesExt() {
                return com.android.server.notification.PreferencesHelper.PackagePreferences.this.mPackagePreferencesExt;
            }

            @Override // com.android.server.notification.IPackagePreferencesWrapper
            public int getImportance() {
                return com.android.server.notification.PreferencesHelper.PackagePreferences.this.importance;
            }
        }
    }

    private static class Delegate {
        static final boolean DEFAULT_ENABLED = true;
        boolean mEnabled;
        final java.lang.String mPkg;
        final int mUid;

        Delegate(java.lang.String pkg, int uid, boolean enabled) {
            this.mPkg = pkg;
            this.mUid = uid;
            this.mEnabled = enabled;
        }

        public boolean isAllowed(java.lang.String pkg, int uid) {
            return pkg != null && uid != -10000 && pkg.equals(this.mPkg) && uid == this.mUid && this.mEnabled;
        }
    }

    public com.android.server.notification.IPreferencesHelperWrapper getWrapper() {
        return this.mPHWrapper;
    }

    private class PreferencesHelperWrapper implements com.android.server.notification.IPreferencesHelperWrapper {
        private PreferencesHelperWrapper() {
        }

        @Override // com.android.server.notification.IPreferencesHelperWrapper
        public com.android.server.notification.IPreferencesHelperExt getPreferencesHelperExt() {
            return com.android.server.notification.PreferencesHelper.this.mPreferencesHelperExt;
        }

        @Override // com.android.server.notification.IPreferencesHelperWrapper
        public void updateConfig() {
            com.android.server.notification.PreferencesHelper.this.updateConfig();
        }

        @Override // com.android.server.notification.IPreferencesHelperWrapper
        public com.android.server.notification.IPackagePreferencesExt getOrCreatePackagePreferencesExt(java.lang.String pkg, int uid) {
            com.android.server.notification.IPackagePreferencesExt packagePreferencesExt;
            synchronized (com.android.server.notification.PreferencesHelper.this.mLock) {
                packagePreferencesExt = com.android.server.notification.PreferencesHelper.this.getOrCreatePackagePreferencesLocked(pkg, uid).getWrapper().getPackagePreferencesExt();
            }
            return packagePreferencesExt;
        }

        @Override // com.android.server.notification.IPreferencesHelperWrapper
        public int getImportanceOfPackage(java.lang.String pkg, int uid) {
            int importance;
            synchronized (com.android.server.notification.PreferencesHelper.this.mLock) {
                importance = com.android.server.notification.PreferencesHelper.this.getOrCreatePackagePreferencesLocked(pkg, uid).getWrapper().getImportance();
            }
            return importance;
        }
    }
}
