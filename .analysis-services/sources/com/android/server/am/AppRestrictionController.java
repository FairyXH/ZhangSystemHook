package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class AppRestrictionController {
    private static final java.lang.String APP_RESTRICTION_SETTINGS_DIRNAME = "apprestriction";
    private static final java.lang.String APP_RESTRICTION_SETTINGS_FILENAME = "settings.xml";
    private static final java.lang.String ATTR_CUR_LEVEL = "curlevel";
    private static final java.lang.String ATTR_LEVEL_TS = "levelts";
    private static final java.lang.String ATTR_PACKAGE = "package";
    private static final java.lang.String ATTR_REASON = "reason";
    private static final java.lang.String ATTR_UID = "uid";
    static final boolean DEBUG_BG_RESTRICTION_CONTROLLER = false;
    static final java.lang.String DEVICE_CONFIG_SUBNAMESPACE_PREFIX = "bg_";
    private static final boolean ENABLE_SHOW_FGS_MANAGER_ACTION_ON_BG_RESTRICTION = false;
    private static final boolean ENABLE_SHOW_FOREGROUND_SERVICE_MANAGER = true;
    private static final java.lang.String[] ROLES_IN_INTEREST = {"android.app.role.DIALER", "android.app.role.EMERGENCY"};
    static final int STOCK_PM_FLAGS = 819200;
    static final java.lang.String TAG = "ActivityManager";
    private static final java.lang.String TAG_SETTINGS = "settings";
    static final int TRACKER_TYPE_BATTERY = 1;
    static final int TRACKER_TYPE_BATTERY_EXEMPTION = 2;
    static final int TRACKER_TYPE_BIND_SERVICE_EVENTS = 7;
    static final int TRACKER_TYPE_BROADCAST_EVENTS = 6;
    static final int TRACKER_TYPE_FGS = 3;
    static final int TRACKER_TYPE_MEDIA_SESSION = 4;
    static final int TRACKER_TYPE_PERMISSION = 5;
    static final int TRACKER_TYPE_UNKNOWN = 0;
    private final android.util.SparseArrayMap<java.lang.String, java.lang.Runnable> mActiveUids;
    final com.android.server.am.ActivityManagerService mActivityManagerService;
    private final com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener mAppIdleStateChangeListener;
    private com.android.server.am.IAppRestrictionControllerExt mAppRestrictionControllerExt;
    private com.android.server.am.AppRestrictionController.AppRestrictionControllerWrapper mAppRestrictionControllerWrapper;
    private final java.util.ArrayList<com.android.server.am.BaseAppStateTracker> mAppStateTrackers;
    private final com.android.server.AppStateTracker.BackgroundRestrictedAppListener mBackgroundRestrictionListener;
    private final android.os.HandlerExecutor mBgExecutor;
    private final com.android.server.am.AppRestrictionController.BgHandler mBgHandler;
    private final android.os.HandlerThread mBgHandlerThread;
    android.util.ArraySet<java.lang.String> mBgRestrictionExemptioFromSysConfig;
    private final android.content.BroadcastReceiver mBootReceiver;
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private final android.util.SparseArray<java.util.Set<java.lang.String>> mCarrierPrivilegedApps;
    private final java.lang.Object mCarrierPrivilegedLock;
    private volatile java.util.ArrayList<com.android.server.am.AppRestrictionController.PhoneCarrierPrivilegesCallback> mCarrierPrivilegesCallbacks;
    private final com.android.server.am.AppRestrictionController.ConstantsObserver mConstantsObserver;
    private final android.content.Context mContext;
    private int[] mDeviceIdleAllowlist;
    private int[] mDeviceIdleExceptIdleAllowlist;
    private final com.android.server.am.AppRestrictionController.TrackerInfo mEmptyTrackerInfo;
    private final android.os.HandlerExecutor mExecutor;
    private final com.android.server.am.AppRestrictionController.Injector mInjector;
    private final java.lang.Object mLock;
    private volatile boolean mLockedBootCompleted;
    private final com.android.server.am.AppRestrictionController.NotificationHelper mNotificationHelper;
    private final java.util.concurrent.CopyOnWriteArraySet<android.app.ActivityManagerInternal.AppBackgroundRestrictionListener> mRestrictionListeners;
    final com.android.server.am.AppRestrictionController.RestrictionSettings mRestrictionSettings;
    private final java.util.concurrent.atomic.AtomicBoolean mRestrictionSettingsXmlLoaded;
    private final android.app.role.OnRoleHoldersChangedListener mRoleHolderChangedListener;
    private final java.lang.Object mSettingsLock;
    private com.android.server.am.IAppRestrictionControllerExt.IStaticExt mStaticExt;
    private final android.util.ArraySet<java.lang.Integer> mSystemDeviceIdleAllowlist;
    private final android.util.ArraySet<java.lang.Integer> mSystemDeviceIdleExceptIdleAllowlist;
    private final java.util.HashMap<java.lang.String, java.lang.Boolean> mSystemModulesCache;
    private final java.util.ArrayList<java.lang.Runnable> mTmpRunnables;
    private final android.app.IUidObserver mUidObserver;
    private final android.util.SparseArray<java.util.ArrayList<java.lang.String>> mUidRolesMapping;

    @interface TrackerType {
    }

    interface UidBatteryUsageProvider {
        com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage getUidBatteryUsage(int i);
    }

    final class RestrictionSettings {
        final android.util.SparseArrayMap<java.lang.String, com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings> mRestrictionLevels = new android.util.SparseArrayMap<>();

        RestrictionSettings() {
        }

        final class PkgSettings {
            private long[] mLastNotificationShownTime;
            private long mLevelChangeTime;
            private int[] mNotificationId;
            private final java.lang.String mPackageName;
            private int mReason;
            private final int mUid;
            private int mLastRestrictionLevel = 0;
            private int mCurrentRestrictionLevel = 0;

            PkgSettings(java.lang.String packageName, int uid) {
                this.mPackageName = packageName;
                this.mUid = uid;
            }

            int update(int level, int reason, int subReason) {
                if (level != this.mCurrentRestrictionLevel) {
                    this.mLastRestrictionLevel = this.mCurrentRestrictionLevel;
                    this.mCurrentRestrictionLevel = level;
                    this.mLevelChangeTime = com.android.server.am.AppRestrictionController.this.mInjector.currentTimeMillis();
                    this.mReason = (65280 & reason) | (subReason & 255);
                    com.android.server.am.AppRestrictionController.this.mBgHandler.obtainMessage(1, this.mUid, level, this.mPackageName).sendToTarget();
                }
                return this.mLastRestrictionLevel;
            }

            public java.lang.String toString() {
                java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
                sb.append("RestrictionLevel{");
                sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
                sb.append(':');
                sb.append(this.mPackageName);
                sb.append('/');
                sb.append(android.os.UserHandle.formatUid(this.mUid));
                sb.append('}');
                sb.append(' ');
                sb.append(android.app.ActivityManager.restrictionLevelToName(this.mCurrentRestrictionLevel));
                sb.append('(');
                sb.append(android.app.usage.UsageStatsManager.reasonToString(this.mReason));
                sb.append(')');
                return sb.toString();
            }

            void dump(java.io.PrintWriter pw, long now) {
                synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                    pw.print(toString());
                    if (this.mLastRestrictionLevel != 0) {
                        pw.print('/');
                        pw.print(android.app.ActivityManager.restrictionLevelToName(this.mLastRestrictionLevel));
                    }
                    pw.print(" levelChange=");
                    android.util.TimeUtils.formatDuration(this.mLevelChangeTime - now, pw);
                    if (this.mLastNotificationShownTime != null) {
                        for (int i = 0; i < this.mLastNotificationShownTime.length; i++) {
                            if (this.mLastNotificationShownTime[i] > 0) {
                                pw.print(" lastNoti(");
                                com.android.server.am.AppRestrictionController.NotificationHelper unused = com.android.server.am.AppRestrictionController.this.mNotificationHelper;
                                pw.print(com.android.server.am.AppRestrictionController.NotificationHelper.notificationTypeToString(i));
                                pw.print(")=");
                                android.util.TimeUtils.formatDuration(this.mLastNotificationShownTime[i] - now, pw);
                            }
                        }
                    }
                }
                pw.print(" effectiveExemption=");
                pw.print(android.os.PowerExemptionManager.reasonCodeToString(com.android.server.am.AppRestrictionController.this.getBackgroundRestrictionExemptionReason(this.mUid)));
            }

            java.lang.String getPackageName() {
                return this.mPackageName;
            }

            int getUid() {
                return this.mUid;
            }

            int getCurrentRestrictionLevel() {
                return this.mCurrentRestrictionLevel;
            }

            int getLastRestrictionLevel() {
                return this.mLastRestrictionLevel;
            }

            int getReason() {
                return this.mReason;
            }

            long getLastNotificationTime(int notificationType) {
                if (this.mLastNotificationShownTime == null) {
                    return 0L;
                }
                return this.mLastNotificationShownTime[notificationType];
            }

            void setLastNotificationTime(int notificationType, long timestamp) {
                setLastNotificationTime(notificationType, timestamp, true);
            }

            void setLastNotificationTime(int notificationType, long timestamp, boolean persist) {
                if (this.mLastNotificationShownTime == null) {
                    this.mLastNotificationShownTime = new long[2];
                }
                this.mLastNotificationShownTime[notificationType] = timestamp;
                if (persist && com.android.server.am.AppRestrictionController.this.mRestrictionSettingsXmlLoaded.get()) {
                    com.android.server.am.AppRestrictionController.RestrictionSettings.this.schedulePersistToXml(android.os.UserHandle.getUserId(this.mUid));
                }
            }

            int getNotificationId(int notificationType) {
                if (this.mNotificationId == null) {
                    return 0;
                }
                return this.mNotificationId[notificationType];
            }

            void setNotificationId(int notificationType, int notificationId) {
                if (this.mNotificationId == null) {
                    this.mNotificationId = new int[2];
                }
                this.mNotificationId[notificationType] = notificationId;
            }

            void setLevelChangeTime(long timestamp) {
                this.mLevelChangeTime = timestamp;
            }

            public java.lang.Object clone() {
                com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings newObj = com.android.server.am.AppRestrictionController.RestrictionSettings.this.new PkgSettings(this.mPackageName, this.mUid);
                newObj.mCurrentRestrictionLevel = this.mCurrentRestrictionLevel;
                newObj.mLastRestrictionLevel = this.mLastRestrictionLevel;
                newObj.mLevelChangeTime = this.mLevelChangeTime;
                newObj.mReason = this.mReason;
                if (this.mLastNotificationShownTime != null) {
                    newObj.mLastNotificationShownTime = java.util.Arrays.copyOf(this.mLastNotificationShownTime, this.mLastNotificationShownTime.length);
                }
                if (this.mNotificationId != null) {
                    newObj.mNotificationId = java.util.Arrays.copyOf(this.mNotificationId, this.mNotificationId.length);
                }
                return newObj;
            }

            public boolean equals(java.lang.Object other) {
                if (other == this) {
                    return true;
                }
                if (other == null || !(other instanceof com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings)) {
                    return false;
                }
                com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings otherSettings = (com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) other;
                if (otherSettings.mUid == this.mUid && otherSettings.mCurrentRestrictionLevel == this.mCurrentRestrictionLevel && otherSettings.mLastRestrictionLevel == this.mLastRestrictionLevel && otherSettings.mLevelChangeTime == this.mLevelChangeTime && otherSettings.mReason == this.mReason && android.text.TextUtils.equals(otherSettings.mPackageName, this.mPackageName) && java.util.Arrays.equals(otherSettings.mLastNotificationShownTime, this.mLastNotificationShownTime) && java.util.Arrays.equals(otherSettings.mNotificationId, this.mNotificationId)) {
                    return true;
                }
                return false;
            }
        }

        int update(java.lang.String packageName, int uid, int level, int reason, int subReason) {
            int iUpdate;
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings settings = getRestrictionSettingsLocked(uid, packageName);
                if (settings == null) {
                    settings = new com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings(packageName, uid);
                    this.mRestrictionLevels.add(uid, packageName, settings);
                }
                iUpdate = settings.update(level, reason, subReason);
            }
            return iUpdate;
        }

        int getReason(java.lang.String packageName, int uid) {
            int reason;
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings settings = (com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) this.mRestrictionLevels.get(uid, packageName);
                reason = settings != null ? settings.getReason() : 256;
            }
            return reason;
        }

        int getRestrictionLevel(int uid) {
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                int uidKeyIndex = this.mRestrictionLevels.indexOfKey(uid);
                if (uidKeyIndex < 0) {
                    return 0;
                }
                int numPackages = this.mRestrictionLevels.numElementsForKeyAt(uidKeyIndex);
                if (numPackages == 0) {
                    return 0;
                }
                int level = 0;
                for (int i = 0; i < numPackages; i++) {
                    com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings setting = (com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) this.mRestrictionLevels.valueAt(uidKeyIndex, i);
                    if (setting != null) {
                        int l = setting.getCurrentRestrictionLevel();
                        level = level == 0 ? l : java.lang.Math.min(level, l);
                    }
                }
                return level;
            }
        }

        int getRestrictionLevel(int uid, java.lang.String packageName) {
            int restrictionLevel;
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings settings = getRestrictionSettingsLocked(uid, packageName);
                restrictionLevel = settings == null ? getRestrictionLevel(uid) : settings.getCurrentRestrictionLevel();
            }
            return restrictionLevel;
        }

        int getRestrictionLevel(java.lang.String packageName, int userId) {
            android.content.pm.PackageManagerInternal pm = com.android.server.am.AppRestrictionController.this.mInjector.getPackageManagerInternal();
            int uid = pm.getPackageUid(packageName, 819200L, userId);
            return getRestrictionLevel(uid, packageName);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getLastRestrictionLevel(int uid, java.lang.String packageName) {
            int lastRestrictionLevel;
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings settings = (com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) this.mRestrictionLevels.get(uid, packageName);
                lastRestrictionLevel = settings == null ? 0 : settings.getLastRestrictionLevel();
            }
            return lastRestrictionLevel;
        }

        void forEachPackageInUidLocked(int uid, com.android.internal.util.function.TriConsumer<java.lang.String, java.lang.Integer, java.lang.Integer> consumer) {
            int uidKeyIndex = this.mRestrictionLevels.indexOfKey(uid);
            if (uidKeyIndex < 0) {
                return;
            }
            int numPackages = this.mRestrictionLevels.numElementsForKeyAt(uidKeyIndex);
            for (int i = 0; i < numPackages; i++) {
                com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings settings = (com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) this.mRestrictionLevels.valueAt(uidKeyIndex, i);
                consumer.accept((java.lang.String) this.mRestrictionLevels.keyAt(uidKeyIndex, i), java.lang.Integer.valueOf(settings.getCurrentRestrictionLevel()), java.lang.Integer.valueOf(settings.getReason()));
            }
        }

        void forEachUidLocked(java.util.function.Consumer<java.lang.Integer> consumer) {
            for (int i = this.mRestrictionLevels.numMaps() - 1; i >= 0; i--) {
                consumer.accept(java.lang.Integer.valueOf(this.mRestrictionLevels.keyAt(i)));
            }
        }

        com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings getRestrictionSettingsLocked(int uid, java.lang.String packageName) {
            return (com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) this.mRestrictionLevels.get(uid, packageName);
        }

        void removeUser(int userId) {
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                for (int i = this.mRestrictionLevels.numMaps() - 1; i >= 0; i--) {
                    int uid = this.mRestrictionLevels.keyAt(i);
                    if (android.os.UserHandle.getUserId(uid) == userId) {
                        this.mRestrictionLevels.deleteAt(i);
                    }
                }
            }
        }

        void removePackage(java.lang.String pkgName, int uid) {
            removePackage(pkgName, uid, true);
        }

        void removePackage(java.lang.String pkgName, int uid, boolean persist) {
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                int keyIndex = this.mRestrictionLevels.indexOfKey(uid);
                this.mRestrictionLevels.delete(uid, pkgName);
                if (keyIndex >= 0 && this.mRestrictionLevels.numElementsForKeyAt(keyIndex) == 0) {
                    this.mRestrictionLevels.deleteAt(keyIndex);
                }
            }
            if (persist && com.android.server.am.AppRestrictionController.this.mRestrictionSettingsXmlLoaded.get()) {
                schedulePersistToXml(android.os.UserHandle.getUserId(uid));
            }
        }

        void removeUid(int uid) {
            removeUid(uid, true);
        }

        void removeUid(int uid, boolean persist) {
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                this.mRestrictionLevels.delete(uid);
            }
            if (persist && com.android.server.am.AppRestrictionController.this.mRestrictionSettingsXmlLoaded.get()) {
                schedulePersistToXml(android.os.UserHandle.getUserId(uid));
            }
        }

        void reset() {
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                for (int i = this.mRestrictionLevels.numMaps() - 1; i >= 0; i--) {
                    this.mRestrictionLevels.deleteAt(i);
                }
            }
        }

        void resetToDefault() {
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                this.mRestrictionLevels.forEach(new java.util.function.Consumer() { // from class: com.android.server.am.AppRestrictionController$RestrictionSettings$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.am.AppRestrictionController.RestrictionSettings.lambda$resetToDefault$0((com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) obj);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$resetToDefault$0(com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings settings) {
            settings.mCurrentRestrictionLevel = 0;
            settings.mLastRestrictionLevel = 0;
            settings.mLevelChangeTime = 0L;
            settings.mReason = 256;
            if (settings.mLastNotificationShownTime != null) {
                for (int i = 0; i < settings.mLastNotificationShownTime.length; i++) {
                    settings.mLastNotificationShownTime[i] = 0;
                }
            }
        }

        void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            final java.util.ArrayList<com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings> settings = new java.util.ArrayList<>();
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                this.mRestrictionLevels.forEach(new java.util.function.Consumer() { // from class: com.android.server.am.AppRestrictionController$RestrictionSettings$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        settings.add((com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) obj);
                    }
                });
            }
            java.util.Collections.sort(settings, java.util.Comparator.comparingInt(new java.util.function.ToIntFunction() { // from class: com.android.server.am.AppRestrictionController$RestrictionSettings$$ExternalSyntheticLambda1
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(java.lang.Object obj) {
                    return ((com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) obj).getUid();
                }
            }));
            long now = com.android.server.am.AppRestrictionController.this.mInjector.currentTimeMillis();
            int size = settings.size();
            for (int i = 0; i < size; i++) {
                pw.print(prefix);
                pw.print('#');
                pw.print(i);
                pw.print(' ');
                settings.get(i).dump(pw, now);
                pw.println();
            }
        }

        void schedulePersistToXml(int userId) {
            com.android.server.am.AppRestrictionController.this.mBgHandler.obtainMessage(11, userId, 0).sendToTarget();
        }

        void scheduleLoadFromXml() {
            com.android.server.am.AppRestrictionController.this.mBgHandler.sendEmptyMessage(10);
        }

        java.io.File getXmlFileNameForUser(int userId) {
            java.io.File dir = new java.io.File(com.android.server.am.AppRestrictionController.this.mInjector.getDataSystemDeDirectory(userId), com.android.server.am.AppRestrictionController.APP_RESTRICTION_SETTINGS_DIRNAME);
            return new java.io.File(dir, com.android.server.am.AppRestrictionController.APP_RESTRICTION_SETTINGS_FILENAME);
        }

        void loadFromXml(boolean applyLevel) {
            int[] allUsers = com.android.server.am.AppRestrictionController.this.mInjector.getUserManagerInternal().getUserIds();
            for (int userId : allUsers) {
                loadFromXml(userId, applyLevel);
            }
            com.android.server.am.AppRestrictionController.this.mRestrictionSettingsXmlLoaded.set(true);
        }

        void loadFromXml(int userId, boolean applyLevel) {
            java.io.File file = getXmlFileNameForUser(userId);
            if (!file.exists()) {
                return;
            }
            long[] ts = new long[2];
            try {
                try {
                    java.io.InputStream in = new java.io.FileInputStream(file);
                    try {
                        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                        long now = android.os.SystemClock.elapsedRealtime();
                        while (true) {
                            int type = parser.next();
                            if (type != 1) {
                                if (type == 2) {
                                    java.lang.String tagName = parser.getName();
                                    if (!com.android.server.am.AppRestrictionController.TAG_SETTINGS.equals(tagName)) {
                                        android.util.Slog.w("ActivityManager", "Unexpected tag name: " + tagName);
                                    } else {
                                        loadOneFromXml(parser, now, ts, applyLevel);
                                    }
                                }
                            } else {
                                in.close();
                                return;
                            }
                        }
                    } catch (java.lang.Throwable th) {
                        try {
                            in.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    android.util.Slog.e("ActivityManager", "loadFromXml trigger ArrayIndexOutOfBoundsException");
                }
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e2) {
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:11:0x0035  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void loadOneFromXml(com.android.modules.utils.TypedXmlPullParser r22, long r23, long[] r25, boolean r26) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 350
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppRestrictionController.RestrictionSettings.loadOneFromXml(com.android.modules.utils.TypedXmlPullParser, long, long[], boolean):void");
        }

        void persistToXml(int userId) {
            java.io.File file = getXmlFileNameForUser(userId);
            java.io.File dir = file.getParentFile();
            if (!dir.isDirectory() && !dir.mkdirs()) {
                android.util.Slog.w("ActivityManager", "Failed to create folder for " + userId);
                return;
            }
            android.util.AtomicFile atomicFile = new android.util.AtomicFile(file);
            java.io.FileOutputStream stream = null;
            try {
                stream = atomicFile.startWrite();
                stream.write(toXmlByteArray(userId));
                atomicFile.finishWrite(stream);
            } catch (java.lang.Exception e) {
                android.util.Slog.e("ActivityManager", "Failed to write file " + file, e);
                if (stream != null) {
                    atomicFile.failWrite(stream);
                }
            }
        }

        private byte[] toXmlByteArray(int userId) {
            try {
                java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
                try {
                    com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(os);
                    serializer.startDocument((java.lang.String) null, true);
                    synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                        for (int i = this.mRestrictionLevels.numMaps() - 1; i >= 0; i--) {
                            for (int j = this.mRestrictionLevels.numElementsForKeyAt(i) - 1; j >= 0; j--) {
                                com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings settings = (com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) this.mRestrictionLevels.valueAt(i, j);
                                int uid = settings.getUid();
                                if (android.os.UserHandle.getUserId(uid) == userId) {
                                    serializer.startTag((java.lang.String) null, com.android.server.am.AppRestrictionController.TAG_SETTINGS);
                                    serializer.attributeInt((java.lang.String) null, "uid", uid);
                                    serializer.attribute((java.lang.String) null, "package", settings.getPackageName());
                                    serializer.attributeInt((java.lang.String) null, com.android.server.am.AppRestrictionController.ATTR_CUR_LEVEL, settings.mCurrentRestrictionLevel);
                                    serializer.attributeLong((java.lang.String) null, com.android.server.am.AppRestrictionController.ATTR_LEVEL_TS, settings.mLevelChangeTime);
                                    serializer.attributeInt((java.lang.String) null, "reason", settings.mReason);
                                    for (int k = 0; k < 2; k++) {
                                        serializer.attributeLong((java.lang.String) null, com.android.server.am.AppRestrictionController.NotificationHelper.notificationTypeToTimeAttr(k), settings.getLastNotificationTime(k));
                                    }
                                    serializer.endTag((java.lang.String) null, com.android.server.am.AppRestrictionController.TAG_SETTINGS);
                                }
                            }
                        }
                    }
                    serializer.endDocument();
                    serializer.flush();
                    byte[] byteArray = os.toByteArray();
                    os.close();
                    return byteArray;
                } finally {
                }
            } catch (java.io.IOException e) {
                return null;
            }
        }

        void removeXml() {
            int[] allUsers = com.android.server.am.AppRestrictionController.this.mInjector.getUserManagerInternal().getUserIds();
            for (int userId : allUsers) {
                getXmlFileNameForUser(userId).delete();
            }
        }

        public java.lang.Object clone() {
            com.android.server.am.AppRestrictionController.RestrictionSettings newObj = com.android.server.am.AppRestrictionController.this.new RestrictionSettings();
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                for (int i = this.mRestrictionLevels.numMaps() - 1; i >= 0; i--) {
                    for (int j = this.mRestrictionLevels.numElementsForKeyAt(i) - 1; j >= 0; j--) {
                        com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings settings = (com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) this.mRestrictionLevels.valueAt(i, j);
                        newObj.mRestrictionLevels.add(this.mRestrictionLevels.keyAt(i), (java.lang.String) this.mRestrictionLevels.keyAt(i, j), (com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) settings.clone());
                    }
                }
            }
            return newObj;
        }

        public boolean equals(java.lang.Object other) {
            if (other == this) {
                return true;
            }
            if (other == null || !(other instanceof com.android.server.am.AppRestrictionController.RestrictionSettings)) {
                return false;
            }
            android.util.SparseArrayMap<java.lang.String, com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings> otherSettings = ((com.android.server.am.AppRestrictionController.RestrictionSettings) other).mRestrictionLevels;
            synchronized (com.android.server.am.AppRestrictionController.this.mSettingsLock) {
                if (otherSettings.numMaps() == this.mRestrictionLevels.numMaps()) {
                    for (int i = this.mRestrictionLevels.numMaps() - 1; i >= 0; i--) {
                        int uid = this.mRestrictionLevels.keyAt(i);
                        if (otherSettings.numElementsForKey(uid) == this.mRestrictionLevels.numElementsForKeyAt(i)) {
                            for (int j = this.mRestrictionLevels.numElementsForKeyAt(i) - 1; j >= 0; j--) {
                                com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings settings = (com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings) this.mRestrictionLevels.valueAt(i, j);
                                if (!settings.equals(otherSettings.get(uid, settings.getPackageName()))) {
                                    return false;
                                }
                            }
                        } else {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
        }
    }

    final class ConstantsObserver implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        static final long DEFAULT_BG_ABUSIVE_NOTIFICATION_MINIMAL_INTERVAL_MS = 2592000000L;
        static final boolean DEFAULT_BG_AUTO_RESTRICTED_BUCKET_ON_BG_RESTRICTION = false;
        static final boolean DEFAULT_BG_AUTO_RESTRICT_ABUSIVE_APPS = true;
        static final long DEFAULT_BG_LONG_FGS_NOTIFICATION_MINIMAL_INTERVAL_MS = 2592000000L;
        static final boolean DEFAULT_BG_PROMPT_FGS_ON_LONG_RUNNING = false;
        static final boolean DEFAULT_BG_PROMPT_FGS_WITH_NOTIFICATION_ON_LONG_RUNNING = false;
        static final java.lang.String KEY_BG_ABUSIVE_NOTIFICATION_MINIMAL_INTERVAL = "bg_abusive_notification_minimal_interval";
        static final java.lang.String KEY_BG_AUTO_RESTRICTED_BUCKET_ON_BG_RESTRICTION = "bg_auto_restricted_bucket_on_bg_restricted";
        static final java.lang.String KEY_BG_AUTO_RESTRICT_ABUSIVE_APPS = "bg_auto_restrict_abusive_apps";
        static final java.lang.String KEY_BG_LONG_FGS_NOTIFICATION_MINIMAL_INTERVAL = "bg_long_fgs_notification_minimal_interval";
        static final java.lang.String KEY_BG_PROMPT_ABUSIVE_APPS_TO_BG_RESTRICTED = "bg_prompt_abusive_apps_to_bg_restricted";
        static final java.lang.String KEY_BG_PROMPT_FGS_ON_LONG_RUNNING = "bg_prompt_fgs_on_long_running";
        static final java.lang.String KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_ON_LONG_RUNNING = "bg_prompt_fgs_with_noti_on_long_running";
        static final java.lang.String KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_TO_BG_RESTRICTED = "bg_prompt_fgs_with_noti_to_bg_restricted";
        static final java.lang.String KEY_BG_RESTRICTION_EXEMPTED_PACKAGES = "bg_restriction_exempted_packages";
        volatile long mBgAbusiveNotificationMinIntervalMs;
        volatile boolean mBgAutoRestrictAbusiveApps;
        volatile boolean mBgAutoRestrictedBucket;
        volatile long mBgLongFgsNotificationMinIntervalMs;
        volatile boolean mBgPromptAbusiveAppsToBgRestricted;
        volatile boolean mBgPromptFgsOnLongRunning;
        volatile boolean mBgPromptFgsWithNotiOnLongRunning;
        volatile boolean mBgPromptFgsWithNotiToBgRestricted;
        volatile java.util.Set<java.lang.String> mBgRestrictionExemptedPackages = java.util.Collections.emptySet();
        final boolean mDefaultBgPromptAbusiveAppToBgRestricted;
        final boolean mDefaultBgPromptFgsWithNotiToBgRestricted;

        ConstantsObserver(android.os.Handler handler, android.content.Context context) {
            this.mDefaultBgPromptFgsWithNotiToBgRestricted = context.getResources().getBoolean(android.R.bool.config_bg_current_drain_high_threshold_by_bg_location);
            this.mDefaultBgPromptAbusiveAppToBgRestricted = context.getResources().getBoolean(android.R.bool.config_bg_current_drain_event_duration_based_threshold_enabled);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:40:0x0083  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onPropertiesChanged(android.provider.DeviceConfig.Properties r4) {
            /*
                Method dump skipped, instruction units count: 242
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppRestrictionController.ConstantsObserver.onPropertiesChanged(android.provider.DeviceConfig$Properties):void");
        }

        public void start() {
            updateDeviceConfig();
        }

        void updateDeviceConfig() {
            updateBgAutoRestrictedBucketChanged();
            updateBgAutoRestrictAbusiveApps();
            updateBgAbusiveNotificationMinimalInterval();
            updateBgLongFgsNotificationMinimalInterval();
            updateBgPromptFgsWithNotiToBgRestricted();
            updateBgPromptFgsWithNotiOnLongRunning();
            updateBgPromptFgsOnLongRunning();
            updateBgPromptAbusiveAppToBgRestricted();
            updateBgRestrictionExemptedPackages();
        }

        private void updateBgAutoRestrictedBucketChanged() {
            boolean oldValue = this.mBgAutoRestrictedBucket;
            this.mBgAutoRestrictedBucket = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_BG_AUTO_RESTRICTED_BUCKET_ON_BG_RESTRICTION, false);
            if (oldValue != this.mBgAutoRestrictedBucket) {
                com.android.server.am.AppRestrictionController.this.dispatchAutoRestrictedBucketFeatureFlagChanged(this.mBgAutoRestrictedBucket);
            }
        }

        private void updateBgAutoRestrictAbusiveApps() {
            this.mBgAutoRestrictAbusiveApps = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_BG_AUTO_RESTRICT_ABUSIVE_APPS, true);
        }

        private void updateBgAbusiveNotificationMinimalInterval() {
            this.mBgAbusiveNotificationMinIntervalMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_BG_ABUSIVE_NOTIFICATION_MINIMAL_INTERVAL, com.android.server.usage.UnixCalendar.MONTH_IN_MILLIS);
        }

        private void updateBgLongFgsNotificationMinimalInterval() {
            this.mBgLongFgsNotificationMinIntervalMs = android.provider.DeviceConfig.getLong("activity_manager", KEY_BG_LONG_FGS_NOTIFICATION_MINIMAL_INTERVAL, com.android.server.usage.UnixCalendar.MONTH_IN_MILLIS);
        }

        private void updateBgPromptFgsWithNotiToBgRestricted() {
            this.mBgPromptFgsWithNotiToBgRestricted = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_TO_BG_RESTRICTED, this.mDefaultBgPromptFgsWithNotiToBgRestricted);
        }

        private void updateBgPromptFgsWithNotiOnLongRunning() {
            this.mBgPromptFgsWithNotiOnLongRunning = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_ON_LONG_RUNNING, false);
        }

        private void updateBgPromptFgsOnLongRunning() {
            this.mBgPromptFgsOnLongRunning = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_BG_PROMPT_FGS_ON_LONG_RUNNING, false);
        }

        private void updateBgPromptAbusiveAppToBgRestricted() {
            this.mBgPromptAbusiveAppsToBgRestricted = android.provider.DeviceConfig.getBoolean("activity_manager", KEY_BG_PROMPT_ABUSIVE_APPS_TO_BG_RESTRICTED, this.mDefaultBgPromptAbusiveAppToBgRestricted);
        }

        private void updateBgRestrictionExemptedPackages() {
            java.lang.String settings = android.provider.DeviceConfig.getString("activity_manager", KEY_BG_RESTRICTION_EXEMPTED_PACKAGES, (java.lang.String) null);
            if (settings == null) {
                this.mBgRestrictionExemptedPackages = java.util.Collections.emptySet();
                return;
            }
            java.lang.String[] settingsList = settings.split(",");
            android.util.ArraySet<java.lang.String> packages = new android.util.ArraySet<>();
            for (java.lang.String pkg : settingsList) {
                packages.add(pkg);
            }
            this.mBgRestrictionExemptedPackages = java.util.Collections.unmodifiableSet(packages);
        }

        void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.print(prefix);
            pw.println("BACKGROUND RESTRICTION POLICY SETTINGS:");
            java.lang.String prefix2 = "  " + prefix;
            pw.print(prefix2);
            pw.print(KEY_BG_AUTO_RESTRICTED_BUCKET_ON_BG_RESTRICTION);
            pw.print('=');
            pw.println(this.mBgAutoRestrictedBucket);
            pw.print(prefix2);
            pw.print(KEY_BG_AUTO_RESTRICT_ABUSIVE_APPS);
            pw.print('=');
            pw.println(this.mBgAutoRestrictAbusiveApps);
            pw.print(prefix2);
            pw.print(KEY_BG_ABUSIVE_NOTIFICATION_MINIMAL_INTERVAL);
            pw.print('=');
            pw.println(this.mBgAbusiveNotificationMinIntervalMs);
            pw.print(prefix2);
            pw.print(KEY_BG_LONG_FGS_NOTIFICATION_MINIMAL_INTERVAL);
            pw.print('=');
            pw.println(this.mBgLongFgsNotificationMinIntervalMs);
            pw.print(prefix2);
            pw.print(KEY_BG_PROMPT_FGS_ON_LONG_RUNNING);
            pw.print('=');
            pw.println(this.mBgPromptFgsOnLongRunning);
            pw.print(prefix2);
            pw.print(KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_ON_LONG_RUNNING);
            pw.print('=');
            pw.println(this.mBgPromptFgsWithNotiOnLongRunning);
            pw.print(prefix2);
            pw.print(KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_TO_BG_RESTRICTED);
            pw.print('=');
            pw.println(this.mBgPromptFgsWithNotiToBgRestricted);
            pw.print(prefix2);
            pw.print(KEY_BG_PROMPT_ABUSIVE_APPS_TO_BG_RESTRICTED);
            pw.print('=');
            pw.println(this.mBgPromptAbusiveAppsToBgRestricted);
            pw.print(prefix2);
            pw.print(KEY_BG_RESTRICTION_EXEMPTED_PACKAGES);
            pw.print('=');
            pw.println(this.mBgRestrictionExemptedPackages.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class TrackerInfo {
        final byte[] mInfo;
        final int mType;

        TrackerInfo() {
            this.mType = 0;
            this.mInfo = null;
        }

        TrackerInfo(int type, byte[] info) {
            this.mType = type;
            this.mInfo = info;
        }
    }

    public void addAppBackgroundRestrictionListener(android.app.ActivityManagerInternal.AppBackgroundRestrictionListener listener) {
        this.mRestrictionListeners.add(listener);
    }

    AppRestrictionController(android.content.Context context, com.android.server.am.ActivityManagerService service) {
        this(new com.android.server.am.AppRestrictionController.Injector(context), service);
    }

    AppRestrictionController(com.android.server.am.AppRestrictionController.Injector injector, com.android.server.am.ActivityManagerService service) {
        this.mAppStateTrackers = new java.util.ArrayList<>();
        this.mRestrictionSettings = new com.android.server.am.AppRestrictionController.RestrictionSettings();
        this.mRestrictionListeners = new java.util.concurrent.CopyOnWriteArraySet<>();
        this.mActiveUids = new android.util.SparseArrayMap<>();
        this.mTmpRunnables = new java.util.ArrayList<>();
        this.mDeviceIdleAllowlist = new int[0];
        this.mDeviceIdleExceptIdleAllowlist = new int[0];
        this.mSystemDeviceIdleAllowlist = new android.util.ArraySet<>();
        this.mSystemDeviceIdleExceptIdleAllowlist = new android.util.ArraySet<>();
        this.mLock = new java.lang.Object();
        this.mSettingsLock = new java.lang.Object();
        this.mRoleHolderChangedListener = new android.app.role.OnRoleHoldersChangedListener() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda8
            public final void onRoleHoldersChanged(java.lang.String str, android.os.UserHandle userHandle) {
                this.f$0.onRoleHoldersChanged(str, userHandle);
            }
        };
        this.mUidRolesMapping = new android.util.SparseArray<>();
        this.mSystemModulesCache = new java.util.HashMap<>();
        this.mCarrierPrivilegedLock = new java.lang.Object();
        this.mCarrierPrivilegedApps = new android.util.SparseArray<>();
        this.mRestrictionSettingsXmlLoaded = new java.util.concurrent.atomic.AtomicBoolean();
        this.mLockedBootCompleted = false;
        this.mEmptyTrackerInfo = new com.android.server.am.AppRestrictionController.TrackerInfo();
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.am.AppRestrictionController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) throws java.lang.Throwable {
                byte b;
                int uid;
                java.lang.String ssp;
                int uid2;
                intent.getAction();
                java.lang.String action = intent.getAction();
                switch (action.hashCode()) {
                    case -2061058799:
                        b = !action.equals("android.intent.action.USER_REMOVED") ? (byte) -1 : (byte) 6;
                        break;
                    case -1749672628:
                        b = !action.equals("android.intent.action.UID_REMOVED") ? (byte) -1 : (byte) 2;
                        break;
                    case -755112654:
                        b = !action.equals("android.intent.action.USER_STARTED") ? (byte) -1 : (byte) 4;
                        break;
                    case -742246786:
                        b = !action.equals("android.intent.action.USER_STOPPED") ? (byte) -1 : (byte) 5;
                        break;
                    case 1093296680:
                        b = !action.equals("android.telephony.action.MULTI_SIM_CONFIG_CHANGED") ? (byte) -1 : (byte) 7;
                        break;
                    case 1121780209:
                        b = !action.equals("android.intent.action.USER_ADDED") ? (byte) -1 : (byte) 3;
                        break;
                    case 1544582882:
                        b = !action.equals("android.intent.action.PACKAGE_ADDED") ? (byte) -1 : (byte) 0;
                        break;
                    case 1580442797:
                        b = !action.equals("android.intent.action.PACKAGE_FULLY_REMOVED") ? (byte) -1 : (byte) 1;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        if (!intent.getBooleanExtra("android.intent.extra.REPLACING", false) && (uid = intent.getIntExtra("android.intent.extra.UID", -1)) >= 0) {
                            com.android.server.am.AppRestrictionController.this.onUidAdded(uid);
                            break;
                        }
                        break;
                    case 1:
                        int uid3 = intent.getIntExtra("android.intent.extra.UID", -1);
                        android.net.Uri data = intent.getData();
                        if (uid3 >= 0 && data != null && (ssp = data.getSchemeSpecificPart()) != null) {
                            com.android.server.am.AppRestrictionController.this.onPackageRemoved(ssp, uid3);
                            break;
                        }
                        break;
                    case 2:
                        if (!intent.getBooleanExtra("android.intent.extra.REPLACING", false) && (uid2 = intent.getIntExtra("android.intent.extra.UID", -1)) >= 0) {
                            com.android.server.am.AppRestrictionController.this.onUidRemoved(uid2);
                            break;
                        }
                        break;
                    case 3:
                        int userId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                        if (userId >= 0) {
                            com.android.server.am.AppRestrictionController.this.onUserAdded(userId);
                        }
                        break;
                    case 4:
                        int userId2 = intent.getIntExtra("android.intent.extra.user_handle", -1);
                        if (userId2 >= 0) {
                            com.android.server.am.AppRestrictionController.this.onUserStarted(userId2);
                        }
                        break;
                    case 5:
                        int userId3 = intent.getIntExtra("android.intent.extra.user_handle", -1);
                        if (userId3 >= 0) {
                            com.android.server.am.AppRestrictionController.this.onUserStopped(userId3);
                        }
                        break;
                    case 6:
                        int userId4 = intent.getIntExtra("android.intent.extra.user_handle", -1);
                        if (userId4 >= 0) {
                            com.android.server.am.AppRestrictionController.this.onUserRemoved(userId4);
                        }
                        break;
                    case 7:
                        com.android.server.am.AppRestrictionController.this.unregisterCarrierPrivilegesCallbacks();
                        com.android.server.am.AppRestrictionController.this.registerCarrierPrivilegesCallbacks();
                        break;
                }
            }
        };
        this.mBootReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.am.AppRestrictionController.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                byte b;
                intent.getAction();
                java.lang.String action = intent.getAction();
                switch (action.hashCode()) {
                    case -905063602:
                        if (action.equals("android.intent.action.LOCKED_BOOT_COMPLETED")) {
                            b = 0;
                            break;
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        com.android.server.am.AppRestrictionController.this.onLockedBootCompleted();
                        break;
                }
            }
        };
        this.mBackgroundRestrictionListener = new com.android.server.AppStateTracker.BackgroundRestrictedAppListener() { // from class: com.android.server.am.AppRestrictionController.3
            public void updateBackgroundRestrictedForUidPackage(int i, java.lang.String str, boolean z) {
                com.android.server.am.AppRestrictionController.this.mBgHandler.obtainMessage(0, i, z ? 1 : 0, str).sendToTarget();
            }
        };
        this.mAppIdleStateChangeListener = new com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener() { // from class: com.android.server.am.AppRestrictionController.4
            public void onAppIdleStateChanged(java.lang.String packageName, int userId, boolean idle, int bucket, int reason) {
                com.android.server.am.AppRestrictionController.this.mBgHandler.obtainMessage(2, userId, bucket, packageName).sendToTarget();
            }

            public void onUserInteractionStarted(java.lang.String packageName, int userId) {
                com.android.server.am.AppRestrictionController.this.mBgHandler.obtainMessage(3, userId, 0, packageName).sendToTarget();
            }
        };
        this.mUidObserver = new android.app.UidObserver() { // from class: com.android.server.am.AppRestrictionController.5
            public void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) {
                com.android.server.am.AppRestrictionController.this.mBgHandler.obtainMessage(8, uid, procState).sendToTarget();
            }

            public void onUidIdle(int i, boolean z) {
                com.android.server.am.AppRestrictionController.this.mBgHandler.obtainMessage(5, i, z ? 1 : 0).sendToTarget();
            }

            public void onUidGone(int i, boolean z) {
                com.android.server.am.AppRestrictionController.this.mBgHandler.obtainMessage(7, i, z ? 1 : 0).sendToTarget();
            }

            public void onUidActive(int uid) {
                com.android.server.am.AppRestrictionController.this.mBgHandler.obtainMessage(6, uid, 0).sendToTarget();
            }
        };
        this.mAppRestrictionControllerWrapper = new com.android.server.am.AppRestrictionController.AppRestrictionControllerWrapper();
        this.mAppRestrictionControllerExt = (com.android.server.am.IAppRestrictionControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IAppRestrictionControllerExt.class).base(this).create();
        this.mStaticExt = (com.android.server.am.IAppRestrictionControllerExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IAppRestrictionControllerExt.IStaticExt.class).base(this).create();
        this.mInjector = injector;
        this.mContext = injector.getContext();
        this.mActivityManagerService = service;
        this.mBgHandlerThread = new android.os.HandlerThread("bgres-controller", 10);
        this.mBgHandlerThread.start();
        this.mBgHandler = new com.android.server.am.AppRestrictionController.BgHandler(this.mBgHandlerThread.getLooper(), injector);
        this.mBgExecutor = new android.os.HandlerExecutor(this.mBgHandler);
        this.mConstantsObserver = new com.android.server.am.AppRestrictionController.ConstantsObserver(this.mBgHandler, this.mContext);
        this.mNotificationHelper = new com.android.server.am.AppRestrictionController.NotificationHelper(this);
        injector.initAppStateTrackers(this);
        this.mExecutor = new android.os.HandlerExecutor(injector.getDefaultHandler());
    }

    void onSystemReady() throws java.lang.Throwable {
        android.provider.DeviceConfig.addOnPropertiesChangedListener("activity_manager", this.mBgExecutor, this.mConstantsObserver);
        this.mConstantsObserver.start();
        initBgRestrictionExemptioFromSysConfig();
        initRestrictionStates();
        initSystemModuleNames();
        initRolesInInterest();
        registerForUidObservers();
        registerForSystemBroadcasts();
        registerCarrierPrivilegesCallbacks();
        this.mNotificationHelper.onSystemReady();
        this.mInjector.getAppStateTracker().addBackgroundRestrictedAppListener(this.mBackgroundRestrictionListener);
        this.mInjector.getAppStandbyInternal().addListener(this.mAppIdleStateChangeListener);
        this.mInjector.getRoleManager().addOnRoleHoldersChangedListenerAsUser(this.mExecutor, this.mRoleHolderChangedListener, android.os.UserHandle.ALL);
        this.mInjector.scheduleInitTrackers(this.mBgHandler, new java.lang.Runnable() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSystemReady$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$0() {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onSystemReady();
        }
    }

    void resetRestrictionSettings() throws java.lang.Throwable {
        synchronized (this.mSettingsLock) {
            this.mRestrictionSettings.reset();
        }
        initRestrictionStates();
    }

    void tearDown() {
        android.provider.DeviceConfig.removeOnPropertiesChangedListener(this.mConstantsObserver);
        unregisterForUidObservers();
        unregisterForSystemBroadcasts();
        this.mRestrictionSettings.removeXml();
    }

    private void initBgRestrictionExemptioFromSysConfig() {
        com.android.server.SystemConfig sysConfig = com.android.server.SystemConfig.getInstance();
        this.mBgRestrictionExemptioFromSysConfig = sysConfig.getBgRestrictionExemption();
        loadAppIdsFromPackageList(sysConfig.getAllowInPowerSaveExceptIdle(), this.mSystemDeviceIdleExceptIdleAllowlist);
        loadAppIdsFromPackageList(sysConfig.getAllowInPowerSave(), this.mSystemDeviceIdleAllowlist);
    }

    private void loadAppIdsFromPackageList(android.util.ArraySet<java.lang.String> packages, android.util.ArraySet<java.lang.Integer> apps) {
        android.content.pm.PackageManager pm = this.mInjector.getPackageManager();
        for (int i = packages.size() - 1; i >= 0; i--) {
            java.lang.String pkg = packages.valueAt(i);
            try {
                android.content.pm.ApplicationInfo ai = pm.getApplicationInfo(pkg, 1048576);
                if (ai != null) {
                    apps.add(java.lang.Integer.valueOf(android.os.UserHandle.getAppId(ai.uid)));
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }
    }

    private boolean isExemptedFromSysConfig(java.lang.String packageName) {
        return this.mBgRestrictionExemptioFromSysConfig != null && this.mBgRestrictionExemptioFromSysConfig.contains(packageName);
    }

    private void initRestrictionStates() throws java.lang.Throwable {
        int[] allUsers = this.mInjector.getUserManagerInternal().getUserIds();
        for (int userId : allUsers) {
            refreshAppRestrictionLevelForUser(userId, 1024, 2);
        }
        if (!this.mInjector.isTest()) {
            this.mRestrictionSettings.scheduleLoadFromXml();
            for (int userId2 : allUsers) {
                this.mRestrictionSettings.schedulePersistToXml(userId2);
            }
        }
    }

    private void initSystemModuleNames() {
        android.content.pm.PackageManager pm = this.mInjector.getPackageManager();
        java.util.List<android.content.pm.ModuleInfo> moduleInfos = pm.getInstalledModules(0);
        if (moduleInfos == null) {
            return;
        }
        synchronized (this.mLock) {
            for (android.content.pm.ModuleInfo info : moduleInfos) {
                this.mSystemModulesCache.put(info.getPackageName(), java.lang.Boolean.TRUE);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0044  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean isSystemModule(java.lang.String r8) {
        /*
            r7 = this;
            java.lang.Object r0 = r7.mLock
            monitor-enter(r0)
            java.util.HashMap<java.lang.String, java.lang.Boolean> r1 = r7.mSystemModulesCache     // Catch: java.lang.Throwable -> L5a
            java.lang.Object r1 = r1.get(r8)     // Catch: java.lang.Throwable -> L5a
            java.lang.Boolean r1 = (java.lang.Boolean) r1     // Catch: java.lang.Throwable -> L5a
            if (r1 == 0) goto L13
            boolean r2 = r1.booleanValue()     // Catch: java.lang.Throwable -> L5a
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L5a
            return r2
        L13:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L5a
            com.android.server.am.AppRestrictionController$Injector r0 = r7.mInjector
            android.content.pm.PackageManager r1 = r0.getPackageManager()
            r0 = 0
            r2 = 1
            r3 = 0
            android.content.pm.ModuleInfo r4 = r1.getModuleInfo(r8, r3)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L28
            if (r4 == 0) goto L25
            r4 = r2
            goto L26
        L25:
            r4 = r3
        L26:
            r0 = r4
            goto L29
        L28:
            r4 = move-exception
        L29:
            if (r0 != 0) goto L48
            android.content.pm.PackageInfo r4 = r1.getPackageInfo(r8, r3)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L47
            if (r4 == 0) goto L44
            android.content.pm.ApplicationInfo r5 = r4.applicationInfo     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L47
            java.lang.String r5 = r5.sourceDir     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L47
            java.io.File r6 = android.os.Environment.getApexDirectory()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L47
            java.lang.String r6 = r6.getAbsolutePath()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L47
            boolean r5 = r5.startsWith(r6)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L47
            if (r5 == 0) goto L44
            goto L45
        L44:
            r2 = r3
        L45:
            r0 = r2
            goto L49
        L47:
            r2 = move-exception
        L48:
            r2 = r0
        L49:
            java.lang.Object r3 = r7.mLock
            monitor-enter(r3)
            java.util.HashMap<java.lang.String, java.lang.Boolean> r0 = r7.mSystemModulesCache     // Catch: java.lang.Throwable -> L57
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r2)     // Catch: java.lang.Throwable -> L57
            r0.put(r8, r4)     // Catch: java.lang.Throwable -> L57
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L57
            return r2
        L57:
            r0 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L57
            throw r0
        L5a:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L5a
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppRestrictionController.isSystemModule(java.lang.String):boolean");
    }

    private void registerForUidObservers() {
        try {
            this.mInjector.getIActivityManager().registerUidObserver(this.mUidObserver, 15, 4, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
        } catch (android.os.RemoteException e) {
        }
    }

    private void unregisterForUidObservers() {
        try {
            this.mInjector.getIActivityManager().unregisterUidObserver(this.mUidObserver);
        } catch (android.os.RemoteException e) {
        }
    }

    private void refreshAppRestrictionLevelForUser(int userId, int reason, int subReason) throws java.lang.Throwable {
        java.util.List<android.app.usage.AppStandbyInfo> appStandbyInfos = this.mInjector.getAppStandbyInternal().getAppStandbyBuckets(userId);
        if (com.android.internal.util.ArrayUtils.isEmpty(appStandbyInfos)) {
            return;
        }
        android.content.pm.PackageManagerInternal pm = this.mInjector.getPackageManagerInternal();
        for (android.app.usage.AppStandbyInfo info : appStandbyInfos) {
            int uid = pm.getPackageUid(info.mPackageName, 819200L, userId);
            if (uid < 0) {
                android.util.Slog.e("ActivityManager", "Unable to find " + info.mPackageName + "/u" + userId);
            } else {
                android.util.Pair<java.lang.Integer, com.android.server.am.AppRestrictionController.TrackerInfo> levelTypePair = calcAppRestrictionLevel(userId, uid, info.mPackageName, info.mStandbyBucket, false, false);
                applyRestrictionLevel(info.mPackageName, uid, ((java.lang.Integer) levelTypePair.first).intValue(), (com.android.server.am.AppRestrictionController.TrackerInfo) levelTypePair.second, info.mStandbyBucket, true, reason, subReason);
            }
        }
    }

    void refreshAppRestrictionLevelForUid(int uid, int reason, int subReason, boolean allowRequestBgRestricted) {
        java.lang.String[] packages = this.mInjector.getPackageManager().getPackagesForUid(uid);
        if (com.android.internal.util.ArrayUtils.isEmpty(packages)) {
            return;
        }
        com.android.server.usage.AppStandbyInternal appStandbyInternal = this.mInjector.getAppStandbyInternal();
        int userId = android.os.UserHandle.getUserId(uid);
        long now = android.os.SystemClock.elapsedRealtime();
        int i = 0;
        for (int length = packages.length; i < length; length = length) {
            java.lang.String pkg = packages[i];
            int curBucket = appStandbyInternal.getAppStandbyBucket(pkg, userId, now, false);
            android.util.Pair<java.lang.Integer, com.android.server.am.AppRestrictionController.TrackerInfo> levelTypePair = calcAppRestrictionLevel(userId, uid, pkg, curBucket, allowRequestBgRestricted, true);
            applyRestrictionLevel(pkg, uid, ((java.lang.Integer) levelTypePair.first).intValue(), (com.android.server.am.AppRestrictionController.TrackerInfo) levelTypePair.second, curBucket, true, reason, subReason);
            i++;
        }
    }

    private android.util.Pair<java.lang.Integer, com.android.server.am.AppRestrictionController.TrackerInfo> calcAppRestrictionLevel(int userId, int uid, java.lang.String packageName, int standbyBucket, boolean allowRequestBgRestricted, boolean calcTrackers) {
        int level;
        if (this.mInjector.getAppHibernationInternal().isHibernatingForUser(packageName, userId)) {
            return new android.util.Pair<>(60, this.mEmptyTrackerInfo);
        }
        com.android.server.am.AppRestrictionController.TrackerInfo trackerInfo = null;
        switch (standbyBucket) {
            case 5:
                level = 20;
                return new android.util.Pair<>(java.lang.Integer.valueOf(level), trackerInfo);
            case 50:
                if (!android.app.Flags.appRestrictionsApi()) {
                    level = 50;
                }
                return new android.util.Pair<>(java.lang.Integer.valueOf(level), trackerInfo);
            default:
                if (this.mInjector.getAppStateTracker().isAppBackgroundRestricted(uid, packageName)) {
                    return new android.util.Pair<>(50, this.mEmptyTrackerInfo);
                }
                if (standbyBucket == 45) {
                    level = 40;
                } else {
                    level = 30;
                }
                if (calcTrackers) {
                    android.util.Pair<java.lang.Integer, com.android.server.am.AppRestrictionController.TrackerInfo> levelTypePair = calcAppRestrictionLevelFromTackers(uid, packageName, 100);
                    int l = ((java.lang.Integer) levelTypePair.first).intValue();
                    if (l == 20) {
                        return new android.util.Pair<>(20, (com.android.server.am.AppRestrictionController.TrackerInfo) levelTypePair.second);
                    }
                    if (l > level) {
                        level = l;
                        trackerInfo = (com.android.server.am.AppRestrictionController.TrackerInfo) levelTypePair.second;
                    }
                    if (level == 50) {
                        if (allowRequestBgRestricted) {
                            this.mBgHandler.obtainMessage(4, uid, 0, packageName).sendToTarget();
                        }
                        android.util.Pair<java.lang.Integer, com.android.server.am.AppRestrictionController.TrackerInfo> levelTypePair2 = calcAppRestrictionLevelFromTackers(uid, packageName, 50);
                        level = ((java.lang.Integer) levelTypePair2.first).intValue();
                        trackerInfo = (com.android.server.am.AppRestrictionController.TrackerInfo) levelTypePair2.second;
                    }
                }
                return new android.util.Pair<>(java.lang.Integer.valueOf(level), trackerInfo);
        }
    }

    private android.util.Pair<java.lang.Integer, com.android.server.am.AppRestrictionController.TrackerInfo> calcAppRestrictionLevelFromTackers(int uid, java.lang.String packageName, int maxLevel) {
        com.android.server.am.AppRestrictionController.TrackerInfo trackerInfo;
        int level = 0;
        int prevLevel = 0;
        com.android.server.am.BaseAppStateTracker resultTracker = null;
        for (int i = this.mAppStateTrackers.size() - 1; i >= 0; i--) {
            int l = this.mAppStateTrackers.get(i).getPolicy().getProposedRestrictionLevel(packageName, uid, maxLevel);
            level = java.lang.Math.max(level, l);
            if (level != prevLevel) {
                com.android.server.am.BaseAppStateTracker resultTracker2 = this.mAppStateTrackers.get(i);
                resultTracker = resultTracker2;
                prevLevel = level;
            }
        }
        if (resultTracker == null) {
            trackerInfo = this.mEmptyTrackerInfo;
        } else {
            trackerInfo = new com.android.server.am.AppRestrictionController.TrackerInfo(resultTracker.getType(), resultTracker.getTrackerInfoForStatsd(uid));
        }
        return new android.util.Pair<>(java.lang.Integer.valueOf(level), trackerInfo);
    }

    private static int standbyBucketToRestrictionLevel(int standbyBucket) {
        switch (standbyBucket) {
            case 5:
                return 20;
            case 10:
            case 20:
            case 30:
            case 40:
                return 30;
            case 45:
                return 40;
            case 50:
                if (!android.app.Flags.appRestrictionsApi()) {
                    return 50;
                }
                return 30;
            default:
                return 0;
        }
    }

    int getRestrictionLevel(int uid) {
        return this.mRestrictionSettings.getRestrictionLevel(uid);
    }

    int getRestrictionLevel(int uid, java.lang.String packageName) {
        return this.mRestrictionSettings.getRestrictionLevel(uid, packageName);
    }

    int getRestrictionLevel(java.lang.String packageName, int userId) {
        return this.mRestrictionSettings.getRestrictionLevel(packageName, userId);
    }

    boolean isAutoRestrictAbusiveAppEnabled() {
        return this.mConstantsObserver.mBgAutoRestrictAbusiveApps;
    }

    long getForegroundServiceTotalDurations(java.lang.String packageName, int uid, long now, int serviceType) {
        return this.mInjector.getAppFGSTracker().getTotalDurations(packageName, uid, now, com.android.server.am.AppFGSTracker.foregroundServiceTypeToIndex(serviceType));
    }

    long getForegroundServiceTotalDurations(int uid, long now, int serviceType) {
        return this.mInjector.getAppFGSTracker().getTotalDurations(uid, now, com.android.server.am.AppFGSTracker.foregroundServiceTypeToIndex(serviceType));
    }

    long getForegroundServiceTotalDurationsSince(java.lang.String packageName, int uid, long since, long now, int serviceType) {
        return this.mInjector.getAppFGSTracker().getTotalDurationsSince(packageName, uid, since, now, com.android.server.am.AppFGSTracker.foregroundServiceTypeToIndex(serviceType));
    }

    long getForegroundServiceTotalDurationsSince(int uid, long since, long now, int serviceType) {
        return this.mInjector.getAppFGSTracker().getTotalDurationsSince(uid, since, now, com.android.server.am.AppFGSTracker.foregroundServiceTypeToIndex(serviceType));
    }

    long getMediaSessionTotalDurations(java.lang.String packageName, int uid, long now) {
        return this.mInjector.getAppMediaSessionTracker().getTotalDurations(packageName, uid, now);
    }

    long getMediaSessionTotalDurations(int uid, long now) {
        return this.mInjector.getAppMediaSessionTracker().getTotalDurations(uid, now);
    }

    long getMediaSessionTotalDurationsSince(java.lang.String packageName, int uid, long since, long now) {
        return this.mInjector.getAppMediaSessionTracker().getTotalDurationsSince(packageName, uid, since, now);
    }

    long getMediaSessionTotalDurationsSince(int uid, long since, long now) {
        return this.mInjector.getAppMediaSessionTracker().getTotalDurationsSince(uid, since, now);
    }

    long getCompositeMediaPlaybackDurations(java.lang.String packageName, int uid, long now, long window) {
        long since = java.lang.Math.max(0L, now - window);
        long mediaPlaybackDuration = java.lang.Math.max(getMediaSessionTotalDurationsSince(packageName, uid, since, now), getForegroundServiceTotalDurationsSince(packageName, uid, since, now, 2));
        return mediaPlaybackDuration;
    }

    long getCompositeMediaPlaybackDurations(int uid, long now, long window) {
        long since = java.lang.Math.max(0L, now - window);
        long mediaPlaybackDuration = java.lang.Math.max(getMediaSessionTotalDurationsSince(uid, since, now), getForegroundServiceTotalDurationsSince(uid, since, now, 2));
        return mediaPlaybackDuration;
    }

    boolean hasForegroundServices(java.lang.String packageName, int uid) {
        return this.mInjector.getAppFGSTracker().hasForegroundServices(packageName, uid);
    }

    boolean hasForegroundServices(int uid) {
        return this.mInjector.getAppFGSTracker().hasForegroundServices(uid);
    }

    boolean hasForegroundServiceNotifications(java.lang.String packageName, int uid) {
        return this.mInjector.getAppFGSTracker().hasForegroundServiceNotifications(packageName, uid);
    }

    boolean hasForegroundServiceNotifications(int uid) {
        return this.mInjector.getAppFGSTracker().hasForegroundServiceNotifications(uid);
    }

    com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage getUidBatteryExemptedUsageSince(int uid, long since, long now, int types) {
        return this.mInjector.getAppBatteryExemptionTracker().getUidBatteryExemptedUsageSince(uid, since, now, types);
    }

    com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage getUidBatteryUsage(int uid) {
        return this.mInjector.getUidBatteryUsageProvider().getUidBatteryUsage(uid);
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.println("APP BACKGROUND RESTRICTIONS");
        java.lang.String prefix2 = "  " + prefix;
        pw.print(prefix2);
        pw.println("BACKGROUND RESTRICTION LEVEL SETTINGS");
        this.mRestrictionSettings.dump(pw, "  " + prefix2);
        this.mConstantsObserver.dump(pw, "  " + prefix2);
        this.mAppRestrictionControllerWrapper.getExtImpl().dump(pw, "  " + prefix2);
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            pw.println();
            this.mAppStateTrackers.get(i).dump(pw, prefix2);
        }
    }

    void dumpAsProto(android.util.proto.ProtoOutputStream proto, int uid) {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).dumpAsProto(proto, uid);
        }
    }

    private int getRestrictionLevelStatsd(int level) {
        switch (level) {
            case 0:
                break;
            case 10:
                break;
            case 20:
                break;
            case 30:
                break;
            case 40:
                break;
            case 50:
                break;
            case 60:
                break;
        }
        return 0;
    }

    private int getThresholdStatsd(int reason) {
        switch (reason) {
            case 1024:
                return 2;
            case 1536:
                return 1;
            default:
                return 0;
        }
    }

    private int getTrackerTypeStatsd(int type) {
        switch (type) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            default:
                return 0;
        }
    }

    private int getExemptionReasonStatsd(int uid, int level) {
        if (level != 20) {
            return 1;
        }
        int reasonCode = getBackgroundRestrictionExemptionReason(uid);
        return android.os.PowerExemptionManager.getExemptionReasonForStatsd(reasonCode);
    }

    private int getOptimizationLevelStatsd(int level) {
        switch (level) {
            case 0:
                break;
            case 10:
                break;
            case 30:
                break;
            case 50:
                break;
        }
        return 0;
    }

    private int getTargetSdkStatsd(java.lang.String packageName) {
        android.content.pm.PackageManager pm = this.mInjector.getPackageManager();
        if (pm == null) {
            return 0;
        }
        try {
            android.content.pm.PackageInfo pkg = pm.getPackageInfo(packageName, 0);
            if (pkg != null && pkg.applicationInfo != null) {
                int targetSdk = pkg.applicationInfo.targetSdkVersion;
                if (targetSdk < 31) {
                    return 1;
                }
                if (targetSdk < 33) {
                    return 2;
                }
                if (targetSdk != 33) {
                    return 0;
                }
                return 3;
            }
            return 0;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    void applyRestrictionLevel(final java.lang.String pkgName, final int uid, final int level, com.android.server.am.AppRestrictionController.TrackerInfo trackerInfo, int curBucket, boolean allowUpdateBucket, int reason, int subReason) throws java.lang.Throwable {
        com.android.server.am.AppRestrictionController.TrackerInfo trackerInfo2;
        int reason2;
        int subReason2;
        int curLevel;
        int curLevel2;
        java.lang.Object obj;
        int curLevel3;
        com.android.server.usage.AppStandbyInternal appStandbyInternal;
        int bucketReason;
        final com.android.server.usage.AppStandbyInternal appStandbyInternal2 = this.mInjector.getAppStandbyInternal();
        if (trackerInfo != null) {
            trackerInfo2 = trackerInfo;
        } else {
            trackerInfo2 = this.mEmptyTrackerInfo;
        }
        synchronized (this.mSettingsLock) {
            try {
                final int curLevel4 = getRestrictionLevel(uid, pkgName);
                try {
                    if (curLevel4 == level) {
                        return;
                    }
                    int levelOfBucket = standbyBucketToRestrictionLevel(curBucket);
                    if (levelOfBucket == level && (bucketReason = appStandbyInternal2.getAppStandbyBucketReason(pkgName, android.os.UserHandle.getUserId(uid), android.os.SystemClock.elapsedRealtime())) != 0) {
                        int reason3 = bucketReason & 65280;
                        reason2 = reason3;
                        subReason2 = bucketReason & 255;
                    } else {
                        reason2 = reason;
                        subReason2 = subReason;
                    }
                    try {
                        int prevReason = this.mRestrictionSettings.getReason(pkgName, uid);
                        final int subReason3 = subReason2;
                        int subReason4 = reason2;
                        final int reason4 = reason2;
                        try {
                            this.mRestrictionSettings.update(pkgName, uid, level, subReason4, subReason3);
                            if (!android.app.Flags.appRestrictionsApi() && (!allowUpdateBucket || curBucket == 5)) {
                                return;
                            }
                            boolean doItNow = true;
                            if (level >= 40 && curLevel4 < 40) {
                                if (curBucket != 45) {
                                    if (this.mConstantsObserver.mBgAutoRestrictedBucket || level == 40) {
                                        java.lang.Object obj2 = this.mSettingsLock;
                                        synchronized (obj2) {
                                            try {
                                                int index = this.mActiveUids.indexOfKey(uid, pkgName);
                                                if (index >= 0) {
                                                    final com.android.server.am.AppRestrictionController.TrackerInfo localTrackerInfo = trackerInfo2;
                                                    try {
                                                        obj = obj2;
                                                        curLevel3 = curLevel4;
                                                        appStandbyInternal = appStandbyInternal2;
                                                    } catch (java.lang.Throwable th) {
                                                        th = th;
                                                        obj = obj2;
                                                    }
                                                    try {
                                                        this.mActiveUids.add(uid, pkgName, new java.lang.Runnable() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda6
                                                            @Override // java.lang.Runnable
                                                            public final void run() {
                                                                this.f$0.lambda$applyRestrictionLevel$1(appStandbyInternal2, pkgName, uid, reason4, subReason3, curLevel4, level, localTrackerInfo);
                                                            }
                                                        });
                                                        doItNow = false;
                                                    } catch (java.lang.Throwable th2) {
                                                        th = th2;
                                                        while (true) {
                                                            try {
                                                                throw th;
                                                            } catch (java.lang.Throwable th3) {
                                                                th = th3;
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    obj = obj2;
                                                    curLevel3 = curLevel4;
                                                    appStandbyInternal = appStandbyInternal2;
                                                }
                                                try {
                                                    if (doItNow) {
                                                        curLevel = reason4;
                                                        appStandbyInternal.restrictApp(pkgName, android.os.UserHandle.getUserId(uid), curLevel, subReason3);
                                                        if (!android.app.Flags.appRestrictionsApi()) {
                                                            logAppBackgroundRestrictionInfo(pkgName, uid, curLevel3, level, trackerInfo2, curLevel);
                                                        }
                                                    } else {
                                                        curLevel = reason4;
                                                    }
                                                    curLevel2 = curLevel3;
                                                } catch (java.lang.Throwable th4) {
                                                    th = th4;
                                                    while (true) {
                                                        throw th;
                                                    }
                                                }
                                            } catch (java.lang.Throwable th5) {
                                                th = th5;
                                                obj = obj2;
                                            }
                                        }
                                    } else {
                                        curLevel2 = curLevel4;
                                        curLevel = reason4;
                                    }
                                } else {
                                    curLevel = reason4;
                                    curLevel2 = curLevel4;
                                }
                            } else {
                                curLevel = reason4;
                                curLevel2 = curLevel4;
                                if (curLevel2 >= 40 && level < 40) {
                                    synchronized (this.mSettingsLock) {
                                        try {
                                            int index2 = this.mActiveUids.indexOfKey(uid, pkgName);
                                            if (index2 >= 0) {
                                                try {
                                                    this.mActiveUids.add(uid, pkgName, (java.lang.Object) null);
                                                } catch (java.lang.Throwable th6) {
                                                    th = th6;
                                                    while (true) {
                                                        try {
                                                            throw th;
                                                        } catch (java.lang.Throwable th7) {
                                                            th = th7;
                                                        }
                                                    }
                                                }
                                            }
                                            appStandbyInternal2.maybeUnrestrictApp(pkgName, android.os.UserHandle.getUserId(uid), prevReason & 65280, prevReason & 255, curLevel, subReason3);
                                            if (!android.app.Flags.appRestrictionsApi()) {
                                                logAppBackgroundRestrictionInfo(pkgName, uid, curLevel2, level, trackerInfo2, curLevel);
                                            }
                                        } catch (java.lang.Throwable th8) {
                                            th = th8;
                                        }
                                    }
                                }
                            }
                            if (doItNow && android.app.Flags.appRestrictionsApi() && curLevel2 != 0) {
                                logAppBackgroundRestrictionInfo(pkgName, uid, curLevel2, level, trackerInfo2, curLevel);
                                return;
                            }
                            return;
                        } catch (java.lang.Throwable th9) {
                            th = th9;
                        }
                    } catch (java.lang.Throwable th10) {
                        th = th10;
                    }
                } catch (java.lang.Throwable th11) {
                    th = th11;
                }
            } catch (java.lang.Throwable th12) {
                th = th12;
            }
            while (true) {
                try {
                    throw th;
                } catch (java.lang.Throwable th13) {
                    th = th13;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyRestrictionLevel$1(com.android.server.usage.AppStandbyInternal appStandbyInternal, java.lang.String pkgName, int uid, int localReason, int localSubReason, int curLevel, int level, com.android.server.am.AppRestrictionController.TrackerInfo localTrackerInfo) {
        appStandbyInternal.restrictApp(pkgName, android.os.UserHandle.getUserId(uid), localReason, localSubReason);
        logAppBackgroundRestrictionInfo(pkgName, uid, curLevel, level, localTrackerInfo, localReason);
    }

    private void logAppBackgroundRestrictionInfo(java.lang.String pkgName, int uid, int prevLevel, int level, com.android.server.am.AppRestrictionController.TrackerInfo trackerInfo, int reason) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO, uid, getRestrictionLevelStatsd(level), getThresholdStatsd(reason), getTrackerTypeStatsd(trackerInfo.mType), trackerInfo.mType == 3 ? trackerInfo.mInfo : null, trackerInfo.mType == 1 ? trackerInfo.mInfo : null, trackerInfo.mType == 6 ? trackerInfo.mInfo : null, trackerInfo.mType == 7 ? trackerInfo.mInfo : null, getExemptionReasonStatsd(uid, level), getOptimizationLevelStatsd(level), getTargetSdkStatsd(pkgName), android.app.ActivityManager.isLowRamDeviceStatic(), getRestrictionLevelStatsd(prevLevel));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBackgroundRestrictionChanged(int uid, java.lang.String pkgName, boolean restricted) throws java.lang.Throwable {
        int tentativeBucket;
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onBackgroundRestrictionChanged(uid, pkgName, restricted);
        }
        com.android.server.usage.AppStandbyInternal appStandbyInternal = this.mInjector.getAppStandbyInternal();
        int userId = android.os.UserHandle.getUserId(uid);
        long now = android.os.SystemClock.elapsedRealtime();
        int curBucket = appStandbyInternal.getAppStandbyBucket(pkgName, userId, now, false);
        if (restricted) {
            applyRestrictionLevel(pkgName, uid, 50, this.mEmptyTrackerInfo, curBucket, true, 1024, 2);
            this.mBgHandler.obtainMessage(9, uid, 0, pkgName).sendToTarget();
            return;
        }
        int lastLevel = this.mRestrictionSettings.getLastRestrictionLevel(uid, pkgName);
        if (curBucket == 5) {
            tentativeBucket = 5;
        } else {
            int i2 = 40;
            if (lastLevel == 40) {
                i2 = 45;
            }
            tentativeBucket = i2;
        }
        android.util.Pair<java.lang.Integer, com.android.server.am.AppRestrictionController.TrackerInfo> levelTypePair = calcAppRestrictionLevel(android.os.UserHandle.getUserId(uid), uid, pkgName, tentativeBucket, false, true);
        applyRestrictionLevel(pkgName, uid, ((java.lang.Integer) levelTypePair.first).intValue(), (com.android.server.am.AppRestrictionController.TrackerInfo) levelTypePair.second, curBucket, true, 768, 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchAppRestrictionLevelChanges(final int uid, final java.lang.String pkgName, final int newLevel) {
        this.mRestrictionListeners.forEach(new java.util.function.Consumer() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((android.app.ActivityManagerInternal.AppBackgroundRestrictionListener) obj).onRestrictionLevelChanged(uid, pkgName, newLevel);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchAutoRestrictedBucketFeatureFlagChanged(final boolean newValue) {
        final com.android.server.usage.AppStandbyInternal appStandbyInternal = this.mInjector.getAppStandbyInternal();
        final java.util.ArrayList<java.lang.Runnable> pendingTasks = new java.util.ArrayList<>();
        synchronized (this.mSettingsLock) {
            this.mRestrictionSettings.forEachUidLocked(new java.util.function.Consumer() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$dispatchAutoRestrictedBucketFeatureFlagChanged$6(pendingTasks, newValue, appStandbyInternal, (java.lang.Integer) obj);
                }
            });
        }
        for (int i = 0; i < pendingTasks.size(); i++) {
            pendingTasks.get(i).run();
        }
        this.mRestrictionListeners.forEach(new java.util.function.Consumer() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((android.app.ActivityManagerInternal.AppBackgroundRestrictionListener) obj).onAutoRestrictedBucketFeatureFlagChanged(newValue);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchAutoRestrictedBucketFeatureFlagChanged$6(final java.util.ArrayList pendingTasks, final boolean newValue, final com.android.server.usage.AppStandbyInternal appStandbyInternal, final java.lang.Integer uid) {
        this.mRestrictionSettings.forEachPackageInUidLocked(uid.intValue(), new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda3
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                com.android.server.am.AppRestrictionController.lambda$dispatchAutoRestrictedBucketFeatureFlagChanged$5(pendingTasks, newValue, appStandbyInternal, uid, (java.lang.String) obj, (java.lang.Integer) obj2, (java.lang.Integer) obj3);
            }
        });
    }

    static /* synthetic */ void lambda$dispatchAutoRestrictedBucketFeatureFlagChanged$5(java.util.ArrayList pendingTasks, boolean newValue, final com.android.server.usage.AppStandbyInternal appStandbyInternal, final java.lang.Integer uid, final java.lang.String pkgName, java.lang.Integer level, final java.lang.Integer reason) {
        java.lang.Runnable runnable;
        if (level.intValue() == 50) {
            if (newValue) {
                runnable = new java.lang.Runnable() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.usage.AppStandbyInternal appStandbyInternal2 = appStandbyInternal;
                        java.lang.String str = pkgName;
                        java.lang.Integer num = uid;
                        java.lang.Integer num2 = reason;
                        appStandbyInternal2.restrictApp(str, android.os.UserHandle.getUserId(num.intValue()), num2.intValue() & 65280, num2.intValue() & 255);
                    }
                };
            } else {
                runnable = new java.lang.Runnable() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.usage.AppStandbyInternal appStandbyInternal2 = appStandbyInternal;
                        java.lang.String str = pkgName;
                        java.lang.Integer num = uid;
                        java.lang.Integer num2 = reason;
                        appStandbyInternal2.maybeUnrestrictApp(str, android.os.UserHandle.getUserId(num.intValue()), num2.intValue() & 65280, num2.intValue() & 255, 768, 6);
                    }
                };
            }
            pendingTasks.add(runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAppStandbyBucketChanged(int bucket, java.lang.String packageName, int userId) throws java.lang.Throwable {
        if (!android.app.Flags.appRestrictionsApi() || this.mLockedBootCompleted) {
            int uid = this.mInjector.getPackageManagerInternal().getPackageUid(packageName, 819200L, userId);
            android.util.Pair<java.lang.Integer, com.android.server.am.AppRestrictionController.TrackerInfo> levelTypePair = calcAppRestrictionLevel(userId, uid, packageName, bucket, false, false);
            applyRestrictionLevel(packageName, uid, ((java.lang.Integer) levelTypePair.first).intValue(), (com.android.server.am.AppRestrictionController.TrackerInfo) levelTypePair.second, bucket, false, 256, 0);
        }
    }

    void handleRequestBgRestricted(java.lang.String packageName, int uid) {
        this.mNotificationHelper.postRequestBgRestrictedIfNecessary(packageName, uid);
    }

    void handleCancelRequestBgRestricted(java.lang.String packageName, int uid) {
        this.mNotificationHelper.cancelRequestBgRestrictedIfNecessary(packageName, uid);
    }

    void handleUidProcStateChanged(int uid, int procState) {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onUidProcStateChanged(uid, procState);
        }
    }

    void handleUidGone(int uid) {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onUidGone(uid);
        }
    }

    public void noteAppRestrictionEnabled(java.lang.String packageName, int uid, int restrictionType, boolean enabled, int reason, java.lang.String subReason, int source, long threshold) {
        java.lang.String subReason2 = subReason;
        if (subReason2 != null && subReason.length() > 16) {
            android.util.Slog.e("ActivityManager", "subReason is too long, truncating " + subReason2);
            subReason2 = subReason2.substring(0, 16);
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.APP_RESTRICTION_STATE_CHANGED, uid, getRestrictionTypeStatsd(restrictionType), enabled, getRestrictionChangeReasonStatsd(reason, subReason2), subReason2, threshold, source);
    }

    private int getRestrictionTypeStatsd(int level) {
        switch (level) {
            case 0:
                return 0;
            case 10:
                return 1;
            case 20:
                return 2;
            case 30:
                return 3;
            case 40:
                return 4;
            case 50:
                return 5;
            case 60:
                return 6;
            case 70:
                return 7;
            default:
                return 8;
        }
    }

    private int getRestrictionChangeReasonStatsd(int reason, java.lang.String subReason) {
        switch (reason) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 6;
            case 6:
                return 7;
            default:
                return 8;
        }
    }

    static class NotificationHelper {
        static final java.lang.String ACTION_FGS_MANAGER_TRAMPOLINE = "com.android.server.am.ACTION_FGS_MANAGER_TRAMPOLINE";
        static final java.lang.String GROUP_KEY = "com.android.app.abusive_bg_apps";
        static final int NOTIFICATION_TYPE_ABUSIVE_CURRENT_DRAIN = 0;
        static final int NOTIFICATION_TYPE_LAST = 2;
        static final int NOTIFICATION_TYPE_LONG_RUNNING_FGS = 1;
        static final java.lang.String PACKAGE_SCHEME = "package";
        static final int SUMMARY_NOTIFICATION_ID = 203105544;
        private final com.android.server.am.AppRestrictionController mBgController;
        private final android.content.Context mContext;
        private final com.android.server.am.AppRestrictionController.Injector mInjector;
        private final java.lang.Object mLock;
        private final android.app.NotificationManager mNotificationManager;
        private final java.lang.Object mSettingsLock;
        static final java.lang.String[] NOTIFICATION_TYPE_STRINGS = {"Abusive current drain", "Long-running FGS"};
        static final java.lang.String ATTR_LAST_BATTERY_NOTIFICATION_TIME = "last_batt_noti_ts";
        static final java.lang.String ATTR_LAST_LONG_FGS_NOTIFICATION_TIME = "last_long_fgs_noti_ts";
        static final java.lang.String[] NOTIFICATION_TIME_ATTRS = {ATTR_LAST_BATTERY_NOTIFICATION_TIME, ATTR_LAST_LONG_FGS_NOTIFICATION_TIME};
        private final android.content.BroadcastReceiver mActionButtonReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.am.AppRestrictionController.NotificationHelper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                byte b;
                intent.getAction();
                java.lang.String action = intent.getAction();
                switch (action.hashCode()) {
                    case -2048453630:
                        if (action.equals(com.android.server.am.AppRestrictionController.NotificationHelper.ACTION_FGS_MANAGER_TRAMPOLINE)) {
                            b = 0;
                            break;
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        java.lang.String packageName = intent.getStringExtra("android.intent.extra.PACKAGE_NAME");
                        int uid = intent.getIntExtra("android.intent.extra.UID", 0);
                        com.android.server.am.AppRestrictionController.NotificationHelper.this.cancelRequestBgRestrictedIfNecessary(packageName, uid);
                        android.content.Intent newIntent = new android.content.Intent("android.intent.action.SHOW_FOREGROUND_SERVICE_MANAGER");
                        newIntent.addFlags(16777216);
                        com.android.server.am.AppRestrictionController.NotificationHelper.this.mContext.sendBroadcastAsUser(newIntent, android.os.UserHandle.SYSTEM);
                        break;
                }
            }
        };
        private int mNotificationIDStepper = 203105545;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @interface NotificationType {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:11:0x0020  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        static int notificationTimeAttrToType(java.lang.String r3) {
            /*
                int r0 = r3.hashCode()
                r1 = 1
                r2 = 0
                switch(r0) {
                    case -1157017279: goto L15;
                    case 17543473: goto La;
                    default: goto L9;
                }
            L9:
                goto L20
            La:
                java.lang.String r0 = "last_batt_noti_ts"
                boolean r0 = r3.equals(r0)
                if (r0 == 0) goto L9
                r0 = r2
                goto L21
            L15:
                java.lang.String r0 = "last_long_fgs_noti_ts"
                boolean r0 = r3.equals(r0)
                if (r0 == 0) goto L9
                r0 = r1
                goto L21
            L20:
                r0 = -1
            L21:
                switch(r0) {
                    case 0: goto L2b;
                    case 1: goto L2a;
                    default: goto L24;
                }
            L24:
                java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
                r0.<init>()
                throw r0
            L2a:
                return r1
            L2b:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppRestrictionController.NotificationHelper.notificationTimeAttrToType(java.lang.String):int");
        }

        static java.lang.String notificationTypeToTimeAttr(int type) {
            return NOTIFICATION_TIME_ATTRS[type];
        }

        static java.lang.String notificationTypeToString(int notificationType) {
            return NOTIFICATION_TYPE_STRINGS[notificationType];
        }

        NotificationHelper(com.android.server.am.AppRestrictionController controller) {
            this.mBgController = controller;
            this.mInjector = controller.mInjector;
            this.mNotificationManager = this.mInjector.getNotificationManager();
            this.mLock = controller.mLock;
            this.mSettingsLock = controller.mSettingsLock;
            this.mContext = this.mInjector.getContext();
        }

        void onSystemReady() {
            this.mContext.registerReceiverForAllUsers(this.mActionButtonReceiver, new android.content.IntentFilter(ACTION_FGS_MANAGER_TRAMPOLINE), "android.permission.MANAGE_ACTIVITY_TASKS", this.mBgController.mBgHandler, 4);
            this.mBgController.getWrapper().getStaticExtImpl().registerReceiverForDeleteNotification(this.mContext, this.mBgController.mBgHandler);
        }

        void postRequestBgRestrictedIfNecessary(java.lang.String packageName, int uid) {
            if (!this.mBgController.mConstantsObserver.mBgPromptAbusiveAppsToBgRestricted) {
                return;
            }
            android.content.Intent intent = new android.content.Intent("android.settings.VIEW_ADVANCED_POWER_USAGE_DETAIL");
            intent.setData(android.net.Uri.fromParts("package", packageName, null));
            intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF);
            android.app.PendingIntent pendingIntent = android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD, null, android.os.UserHandle.of(android.os.UserHandle.getUserId(uid)));
            boolean hasForegroundServices = this.mBgController.hasForegroundServices(packageName, uid);
            boolean hasForegroundServiceNotifications = this.mBgController.hasForegroundServiceNotifications(packageName, uid);
            if (this.mBgController.mConstantsObserver.mBgPromptFgsWithNotiToBgRestricted || !hasForegroundServices || !hasForegroundServiceNotifications) {
                postNotificationIfNecessary(0, android.R.string.notification_channel_voice_mail, android.R.string.notification_channel_emergency_callback, pendingIntent, packageName, uid, null);
            }
        }

        void postLongRunningFgsIfNecessary(java.lang.String packageName, int uid) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO, uid, this.mBgController.getRestrictionLevel(uid), 0, 3, this.mInjector.getAppFGSTracker().getTrackerInfoForStatsd(uid), (byte[]) null, (byte[]) null, (byte[]) null, android.os.PowerExemptionManager.getExemptionReasonForStatsd(this.mBgController.getBackgroundRestrictionExemptionReason(uid)), 0, 0, android.app.ActivityManager.isLowRamDeviceStatic(), this.mBgController.getRestrictionLevel(uid));
            if (!this.mBgController.mConstantsObserver.mBgPromptFgsOnLongRunning) {
                return;
            }
            if (!this.mBgController.mConstantsObserver.mBgPromptFgsWithNotiOnLongRunning && this.mBgController.hasForegroundServiceNotifications(packageName, uid)) {
                return;
            }
            android.content.Intent intent = new android.content.Intent("android.intent.action.SHOW_FOREGROUND_SERVICE_MANAGER");
            intent.addFlags(16777216);
            android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD, android.os.UserHandle.SYSTEM);
            postNotificationIfNecessary(1, android.R.string.notification_channel_vpn, android.R.string.notification_channel_foreground_service, pendingIntent, packageName, uid, null);
        }

        long getNotificationMinInterval(int notificationType) {
            switch (notificationType) {
                case 0:
                    return this.mBgController.mConstantsObserver.mBgAbusiveNotificationMinIntervalMs;
                case 1:
                    return this.mBgController.mConstantsObserver.mBgLongFgsNotificationMinIntervalMs;
                default:
                    return 0L;
            }
        }

        int getNotificationIdIfNecessary(int notificationType, java.lang.String packageName, int uid) {
            synchronized (this.mSettingsLock) {
                com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings settings = this.mBgController.mRestrictionSettings.getRestrictionSettingsLocked(uid, packageName);
                if (settings == null) {
                    return 0;
                }
                long now = this.mInjector.currentTimeMillis();
                long lastNotificationShownTime = settings.getLastNotificationTime(notificationType);
                if (lastNotificationShownTime != 0 && getNotificationMinInterval(notificationType) + lastNotificationShownTime > now) {
                    return 0;
                }
                settings.setLastNotificationTime(notificationType, now);
                int notificationId = settings.getNotificationId(notificationType);
                if (notificationId <= 0) {
                    int i = this.mNotificationIDStepper;
                    this.mNotificationIDStepper = i + 1;
                    notificationId = i;
                    settings.setNotificationId(notificationType, notificationId);
                }
                if (notificationId > 0) {
                    this.mBgController.getWrapper().getStaticExtImpl().incrementCount();
                }
                return notificationId;
            }
        }

        void postNotificationIfNecessary(int notificationType, int titleRes, int messageRes, android.app.PendingIntent pendingIntent, java.lang.String packageName, int uid, android.app.Notification.Action[] actions) {
            int notificationId = getNotificationIdIfNecessary(notificationType, packageName, uid);
            if (notificationId <= 0) {
                return;
            }
            android.content.pm.PackageManagerInternal pmi = this.mInjector.getPackageManagerInternal();
            android.content.pm.PackageManager pm = this.mInjector.getPackageManager();
            android.content.pm.ApplicationInfo ai = pmi.getApplicationInfo(packageName, 819200L, 1000, android.os.UserHandle.getUserId(uid));
            java.lang.String title = this.mContext.getString(titleRes);
            java.lang.String message = this.mContext.getString(messageRes, ai != null ? ai.loadLabel(pm) : packageName);
            android.graphics.drawable.Icon icon = ai != null ? android.graphics.drawable.Icon.createWithResource(packageName, ai.icon) : null;
            postNotification(notificationId, packageName, uid, title, message, icon, pendingIntent, actions);
        }

        void postNotification(int notificationId, java.lang.String packageName, int uid, java.lang.String title, java.lang.String message, android.graphics.drawable.Icon icon, android.app.PendingIntent pendingIntent, android.app.Notification.Action[] actions) {
            android.os.UserHandle targetUser = android.os.UserHandle.of(android.os.UserHandle.getUserId(uid));
            postSummaryNotification(targetUser);
            android.app.Notification.Builder notificationBuilder = new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.ABUSIVE_BACKGROUND_APPS).setAutoCancel(true).setGroup(GROUP_KEY).setWhen(this.mInjector.currentTimeMillis()).setSmallIcon(android.R.drawable.stat_sys_warning).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color)).setContentTitle(title).setContentText(message).setContentIntent(pendingIntent);
            this.mBgController.getWrapper().getStaticExtImpl().setDeleteIntent(notificationBuilder, this.mContext);
            if (icon != null) {
                notificationBuilder.setLargeIcon(icon);
            }
            if (actions != null) {
                for (android.app.Notification.Action action : actions) {
                    notificationBuilder.addAction(action);
                }
            }
            android.app.Notification notification = notificationBuilder.build();
            notification.extras.putString("android.intent.extra.PACKAGE_NAME", packageName);
            this.mNotificationManager.notifyAsUser(null, notificationId, notification, targetUser);
        }

        private void postSummaryNotification(android.os.UserHandle targetUser) {
            android.app.Notification summary = new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.ABUSIVE_BACKGROUND_APPS).setGroup(GROUP_KEY).setGroupSummary(true).setStyle(new android.app.Notification.BigTextStyle()).setSmallIcon(android.R.drawable.stat_sys_warning).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color)).build();
            this.mNotificationManager.notifyAsUser(null, SUMMARY_NOTIFICATION_ID, summary, targetUser);
        }

        void cancelRequestBgRestrictedIfNecessary(java.lang.String packageName, int uid) {
            int notificationId;
            synchronized (this.mSettingsLock) {
                com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings settings = this.mBgController.mRestrictionSettings.getRestrictionSettingsLocked(uid, packageName);
                if (settings != null && (notificationId = settings.getNotificationId(0)) > 0) {
                    this.mNotificationManager.cancel(notificationId);
                    this.mBgController.getWrapper().getStaticExtImpl().decrementCount();
                    this.mBgController.getWrapper().getStaticExtImpl().cancelSummaryNotificationIfNecessary();
                }
            }
        }

        void cancelLongRunningFGSNotificationIfNecessary(java.lang.String packageName, int uid) {
            int notificationId;
            synchronized (this.mSettingsLock) {
                com.android.server.am.AppRestrictionController.RestrictionSettings.PkgSettings settings = this.mBgController.mRestrictionSettings.getRestrictionSettingsLocked(uid, packageName);
                if (settings != null && (notificationId = settings.getNotificationId(1)) > 0) {
                    this.mNotificationManager.cancel(notificationId);
                    this.mBgController.getWrapper().getStaticExtImpl().decrementCount();
                    this.mBgController.getWrapper().getStaticExtImpl().cancelSummaryNotificationIfNecessary();
                }
            }
        }
    }

    void handleUidInactive(int uid, boolean disabled) {
        java.util.ArrayList<java.lang.Runnable> pendingTasks = this.mTmpRunnables;
        synchronized (this.mSettingsLock) {
            int index = this.mActiveUids.indexOfKey(uid);
            if (index < 0) {
                return;
            }
            int numPackages = this.mActiveUids.numElementsForKeyAt(index);
            for (int i = 0; i < numPackages; i++) {
                java.lang.Runnable pendingTask = (java.lang.Runnable) this.mActiveUids.valueAt(index, i);
                if (pendingTask != null) {
                    pendingTasks.add(pendingTask);
                }
            }
            this.mActiveUids.deleteAt(index);
            int size = pendingTasks.size();
            for (int i2 = 0; i2 < size; i2++) {
                pendingTasks.get(i2).run();
            }
            pendingTasks.clear();
        }
    }

    void handleUidActive(final int uid) {
        synchronized (this.mSettingsLock) {
            final com.android.server.usage.AppStandbyInternal appStandbyInternal = this.mInjector.getAppStandbyInternal();
            final int userId = android.os.UserHandle.getUserId(uid);
            this.mRestrictionSettings.forEachPackageInUidLocked(uid, new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda9
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                    this.f$0.lambda$handleUidActive$9(uid, appStandbyInternal, userId, (java.lang.String) obj, (java.lang.Integer) obj2, (java.lang.Integer) obj3);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleUidActive$9(int uid, final com.android.server.usage.AppStandbyInternal appStandbyInternal, final int userId, final java.lang.String pkgName, java.lang.Integer level, final java.lang.Integer reason) {
        if (this.mConstantsObserver.mBgAutoRestrictedBucket && level.intValue() == 50) {
            this.mActiveUids.add(uid, pkgName, new java.lang.Runnable() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.usage.AppStandbyInternal appStandbyInternal2 = appStandbyInternal;
                    java.lang.String str = pkgName;
                    int i = userId;
                    java.lang.Integer num = reason;
                    appStandbyInternal2.restrictApp(str, i, num.intValue() & 65280, num.intValue() & 255);
                }
            });
        } else {
            this.mActiveUids.add(uid, pkgName, (java.lang.Object) null);
        }
    }

    boolean isOnDeviceIdleAllowlist(int uid) {
        int appId = android.os.UserHandle.getAppId(uid);
        return java.util.Arrays.binarySearch(this.mDeviceIdleAllowlist, appId) >= 0 || java.util.Arrays.binarySearch(this.mDeviceIdleExceptIdleAllowlist, appId) >= 0;
    }

    boolean isOnSystemDeviceIdleAllowlist(int uid) {
        int appId = android.os.UserHandle.getAppId(uid);
        return this.mSystemDeviceIdleAllowlist.contains(java.lang.Integer.valueOf(appId)) || this.mSystemDeviceIdleExceptIdleAllowlist.contains(java.lang.Integer.valueOf(appId));
    }

    void setDeviceIdleAllowlist(int[] allAppids, int[] exceptIdleAppids) {
        this.mDeviceIdleAllowlist = allAppids;
        this.mDeviceIdleExceptIdleAllowlist = exceptIdleAppids;
    }

    int getBackgroundRestrictionExemptionReason(int uid) {
        int reason = getPotentialSystemExemptionReason(uid);
        if (reason != -1) {
            return reason;
        }
        java.lang.String[] packages = this.mInjector.getPackageManager().getPackagesForUid(uid);
        if (packages != null) {
            for (java.lang.String pkg : packages) {
                int reason2 = getPotentialSystemExemptionReason(uid, pkg);
                if (reason2 != -1) {
                    return reason2;
                }
            }
            for (java.lang.String pkg2 : packages) {
                int reason3 = getPotentialUserAllowedExemptionReason(uid, pkg2);
                if (reason3 != -1) {
                    return reason3;
                }
            }
        }
        return -1;
    }

    int getPotentialSystemExemptionReason(int uid) {
        if (android.os.UserHandle.isCore(uid)) {
            return 51;
        }
        if (isOnSystemDeviceIdleAllowlist(uid)) {
            return 300;
        }
        if (android.os.UserManager.isDeviceInDemoMode(this.mContext)) {
            return 63;
        }
        int userId = android.os.UserHandle.getUserId(uid);
        if (this.mInjector.getUserManagerInternal().hasUserRestriction("no_control_apps", userId)) {
            return 323;
        }
        android.app.ActivityManagerInternal am = this.mInjector.getActivityManagerInternal();
        if (am.isDeviceOwner(uid)) {
            return 55;
        }
        if (am.isProfileOwner(uid)) {
            return 56;
        }
        int uidProcState = am.getUidProcessState(uid);
        if (uidProcState <= 0) {
            return 10;
        }
        if (uidProcState <= 1) {
            return 11;
        }
        return -1;
    }

    int getPotentialSystemExemptionReason(int uid, java.lang.String pkg) {
        android.content.pm.PackageManagerInternal pm = this.mInjector.getPackageManagerInternal();
        com.android.server.usage.AppStandbyInternal appStandbyInternal = this.mInjector.getAppStandbyInternal();
        android.app.AppOpsManager appOpsManager = this.mInjector.getAppOpsManager();
        com.android.server.am.ActivityManagerService activityManagerService = this.mInjector.getActivityManagerService();
        int userId = android.os.UserHandle.getUserId(uid);
        if (isSystemModule(pkg)) {
            return 320;
        }
        if (isCarrierApp(pkg)) {
            return 321;
        }
        if (isExemptedFromSysConfig(pkg) || this.mConstantsObserver.mBgRestrictionExemptedPackages.contains(pkg)) {
            return 300;
        }
        if (pm.isPackageStateProtected(pkg, userId)) {
            return 322;
        }
        if (appStandbyInternal.isActiveDeviceAdmin(pkg, userId)) {
            return com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_ACTIVE_DEVICE_ADMIN;
        }
        if (activityManagerService.mConstants.mFlagSystemExemptPowerRestrictionsEnabled && appOpsManager.checkOpNoThrow(128, uid, pkg) == 0) {
            return 327;
        }
        return -1;
    }

    int getPotentialUserAllowedExemptionReason(int uid, java.lang.String pkg) {
        android.app.AppOpsManager appOpsManager = this.mInjector.getAppOpsManager();
        if (appOpsManager.checkOpNoThrow(47, uid, pkg) == 0) {
            return 68;
        }
        if (appOpsManager.checkOpNoThrow(94, uid, pkg) == 0) {
            return 69;
        }
        if (isRoleHeldByUid("android.app.role.DIALER", uid)) {
            return com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_ROLE_DIALER;
        }
        if (isRoleHeldByUid("android.app.role.EMERGENCY", uid)) {
            return 319;
        }
        if (isOnDeviceIdleAllowlist(uid)) {
            return 65;
        }
        android.app.ActivityManagerInternal am = this.mInjector.getActivityManagerInternal();
        if (am.isAssociatedCompanionApp(android.os.UserHandle.getUserId(uid), uid)) {
            return 57;
        }
        return -1;
    }

    private boolean isCarrierApp(java.lang.String packageName) {
        synchronized (this.mCarrierPrivilegedLock) {
            if (this.mCarrierPrivilegedApps != null) {
                for (int i = this.mCarrierPrivilegedApps.size() - 1; i >= 0; i--) {
                    if (this.mCarrierPrivilegedApps.valueAt(i).contains(packageName)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerCarrierPrivilegesCallbacks() {
        android.telephony.TelephonyManager telephonyManager = this.mInjector.getTelephonyManager();
        if (telephonyManager == null) {
            return;
        }
        int numPhones = telephonyManager.getActiveModemCount();
        java.util.ArrayList<com.android.server.am.AppRestrictionController.PhoneCarrierPrivilegesCallback> callbacks = new java.util.ArrayList<>();
        for (int i = 0; i < numPhones; i++) {
            com.android.server.am.AppRestrictionController.PhoneCarrierPrivilegesCallback callback = new com.android.server.am.AppRestrictionController.PhoneCarrierPrivilegesCallback(i);
            callbacks.add(callback);
            telephonyManager.registerCarrierPrivilegesCallback(i, this.mExecutor, callback);
        }
        this.mCarrierPrivilegesCallbacks = callbacks;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterCarrierPrivilegesCallbacks() {
        java.util.ArrayList<com.android.server.am.AppRestrictionController.PhoneCarrierPrivilegesCallback> callbacks;
        android.telephony.TelephonyManager telephonyManager = this.mInjector.getTelephonyManager();
        if (telephonyManager != null && (callbacks = this.mCarrierPrivilegesCallbacks) != null) {
            for (int i = callbacks.size() - 1; i >= 0; i--) {
                telephonyManager.unregisterCarrierPrivilegesCallback(callbacks.get(i));
            }
            this.mCarrierPrivilegesCallbacks = null;
        }
    }

    private class PhoneCarrierPrivilegesCallback implements android.telephony.TelephonyManager.CarrierPrivilegesCallback {
        private final int mPhoneId;

        PhoneCarrierPrivilegesCallback(int phoneId) {
            this.mPhoneId = phoneId;
        }

        public void onCarrierPrivilegesChanged(java.util.Set<java.lang.String> privilegedPackageNames, java.util.Set<java.lang.Integer> privilegedUids) {
            synchronized (com.android.server.am.AppRestrictionController.this.mCarrierPrivilegedLock) {
                com.android.server.am.AppRestrictionController.this.mCarrierPrivilegedApps.put(this.mPhoneId, java.util.Collections.unmodifiableSet(privilegedPackageNames));
            }
        }
    }

    private boolean isRoleHeldByUid(java.lang.String roleName, int uid) {
        boolean z;
        synchronized (this.mLock) {
            java.util.ArrayList<java.lang.String> roles = this.mUidRolesMapping.get(uid);
            z = roles != null && roles.indexOf(roleName) >= 0;
        }
        return z;
    }

    private void initRolesInInterest() {
        int[] allUsers = this.mInjector.getUserManagerInternal().getUserIds();
        for (java.lang.String role : ROLES_IN_INTEREST) {
            if (this.mInjector.getRoleManager().isRoleAvailable(role)) {
                for (int userId : allUsers) {
                    android.os.UserHandle user = android.os.UserHandle.of(userId);
                    onRoleHoldersChanged(role, user);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRoleHoldersChanged(java.lang.String roleName, android.os.UserHandle user) {
        java.util.List<java.lang.String> rolePkgs = this.mInjector.getRoleManager().getRoleHoldersAsUser(roleName, user);
        android.util.ArraySet<java.lang.Integer> roleUids = new android.util.ArraySet<>();
        int userId = user.getIdentifier();
        if (rolePkgs != null) {
            android.content.pm.PackageManagerInternal pm = this.mInjector.getPackageManagerInternal();
            for (java.lang.String pkg : rolePkgs) {
                roleUids.add(java.lang.Integer.valueOf(pm.getPackageUid(pkg, 819200L, userId)));
            }
        }
        synchronized (this.mLock) {
            for (int i = this.mUidRolesMapping.size() - 1; i >= 0; i--) {
                int uid = this.mUidRolesMapping.keyAt(i);
                if (android.os.UserHandle.getUserId(uid) == userId) {
                    java.util.ArrayList<java.lang.String> roles = this.mUidRolesMapping.valueAt(i);
                    int index = roles.indexOf(roleName);
                    boolean isRole = roleUids.contains(java.lang.Integer.valueOf(uid));
                    if (index >= 0) {
                        if (!isRole) {
                            roles.remove(index);
                            if (roles.isEmpty()) {
                                this.mUidRolesMapping.removeAt(i);
                            }
                        }
                    } else if (isRole) {
                        roles.add(roleName);
                        roleUids.remove(java.lang.Integer.valueOf(uid));
                    }
                }
            }
            int i2 = roleUids.size();
            for (int i3 = i2 - 1; i3 >= 0; i3--) {
                java.util.ArrayList<java.lang.String> roles2 = new java.util.ArrayList<>();
                roles2.add(roleName);
                this.mUidRolesMapping.put(roleUids.valueAt(i3).intValue(), roles2);
            }
        }
    }

    android.os.Handler getBackgroundHandler() {
        return this.mBgHandler;
    }

    android.os.HandlerThread getBackgroundHandlerThread() {
        return this.mBgHandlerThread;
    }

    java.lang.Object getLock() {
        return this.mLock;
    }

    void addAppStateTracker(com.android.server.am.BaseAppStateTracker tracker) {
        this.mAppStateTrackers.add(tracker);
    }

    <T extends com.android.server.am.BaseAppStateTracker> T getAppStateTracker(java.lang.Class<T> trackerClass) {
        java.util.Iterator<com.android.server.am.BaseAppStateTracker> it = this.mAppStateTrackers.iterator();
        while (it.hasNext()) {
            T t = (T) it.next();
            if (trackerClass.isAssignableFrom(t.getClass())) {
                return t;
            }
        }
        return null;
    }

    void postLongRunningFgsIfNecessary(java.lang.String packageName, int uid) {
        this.mNotificationHelper.postLongRunningFgsIfNecessary(packageName, uid);
    }

    void cancelLongRunningFGSNotificationIfNecessary(java.lang.String packageName, int uid) {
        this.mNotificationHelper.cancelLongRunningFGSNotificationIfNecessary(packageName, uid);
    }

    java.lang.String getPackageName(int pid) {
        return this.mInjector.getPackageName(pid);
    }

    static class BgHandler extends android.os.Handler {
        static final int MSG_APP_RESTRICTION_LEVEL_CHANGED = 1;
        static final int MSG_APP_STANDBY_BUCKET_CHANGED = 2;
        static final int MSG_BACKGROUND_RESTRICTION_CHANGED = 0;
        static final int MSG_CANCEL_REQUEST_BG_RESTRICTED = 9;
        static final int MSG_LOAD_RESTRICTION_SETTINGS = 10;
        static final int MSG_PERSIST_RESTRICTION_SETTINGS = 11;
        static final int MSG_REQUEST_BG_RESTRICTED = 4;
        static final int MSG_UID_ACTIVE = 6;
        static final int MSG_UID_GONE = 7;
        static final int MSG_UID_IDLE = 5;
        static final int MSG_UID_PROC_STATE_CHANGED = 8;
        static final int MSG_USER_INTERACTION_STARTED = 3;
        private final com.android.server.am.AppRestrictionController.Injector mInjector;

        BgHandler(android.os.Looper looper, com.android.server.am.AppRestrictionController.Injector injector) {
            super(looper);
            this.mInjector = injector;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            com.android.server.am.AppRestrictionController c = this.mInjector.getAppRestrictionController();
            switch (msg.what) {
                case 0:
                    c.handleBackgroundRestrictionChanged(msg.arg1, (java.lang.String) msg.obj, msg.arg2 == 1);
                    break;
                case 1:
                    c.dispatchAppRestrictionLevelChanges(msg.arg1, (java.lang.String) msg.obj, msg.arg2);
                    break;
                case 2:
                    c.handleAppStandbyBucketChanged(msg.arg2, (java.lang.String) msg.obj, msg.arg1);
                    break;
                case 3:
                    c.onUserInteractionStarted((java.lang.String) msg.obj, msg.arg1);
                    break;
                case 4:
                    c.handleRequestBgRestricted((java.lang.String) msg.obj, msg.arg1);
                    break;
                case 5:
                    c.handleUidInactive(msg.arg1, msg.arg2 == 1);
                    break;
                case 6:
                    c.handleUidActive(msg.arg1);
                    break;
                case 7:
                    c.handleUidInactive(msg.arg1, msg.arg2 == 1);
                    c.handleUidGone(msg.arg1);
                    break;
                case 8:
                    c.handleUidProcStateChanged(msg.arg1, msg.arg2);
                    break;
                case 9:
                    c.handleCancelRequestBgRestricted((java.lang.String) msg.obj, msg.arg1);
                    break;
                case 10:
                    c.mRestrictionSettings.loadFromXml(true);
                    break;
                case 11:
                    c.mRestrictionSettings.persistToXml(msg.arg1);
                    break;
            }
        }
    }

    static class Injector {
        private android.app.ActivityManagerInternal mActivityManagerInternal;
        private com.android.server.am.AppBatteryExemptionTracker mAppBatteryExemptionTracker;
        private com.android.server.am.AppBatteryTracker mAppBatteryTracker;
        private com.android.server.am.AppFGSTracker mAppFGSTracker;
        private com.android.server.apphibernation.AppHibernationManagerInternal mAppHibernationInternal;
        private com.android.server.am.AppMediaSessionTracker mAppMediaSessionTracker;
        private android.app.AppOpsManager mAppOpsManager;
        private com.android.server.am.AppPermissionTracker mAppPermissionTracker;
        private com.android.server.am.AppRestrictionController mAppRestrictionController;
        private com.android.server.usage.AppStandbyInternal mAppStandbyInternal;
        private com.android.server.AppStateTracker mAppStateTracker;
        private final android.content.Context mContext;
        private android.app.IActivityManager mIActivityManager;
        private android.app.NotificationManager mNotificationManager;
        private android.content.pm.PackageManagerInternal mPackageManagerInternal;
        private android.app.role.RoleManager mRoleManager;
        private android.telephony.TelephonyManager mTelephonyManager;
        private com.android.server.pm.UserManagerInternal mUserManagerInternal;

        Injector(android.content.Context context) {
            this.mContext = context;
        }

        android.content.Context getContext() {
            return this.mContext;
        }

        void initAppStateTrackers(com.android.server.am.AppRestrictionController controller) {
            this.mAppRestrictionController = controller;
            this.mAppBatteryTracker = new com.android.server.am.AppBatteryTracker(this.mContext, controller);
            this.mAppBatteryExemptionTracker = new com.android.server.am.AppBatteryExemptionTracker(this.mContext, controller);
            this.mAppFGSTracker = new com.android.server.am.AppFGSTracker(this.mContext, controller);
            this.mAppMediaSessionTracker = new com.android.server.am.AppMediaSessionTracker(this.mContext, controller);
            this.mAppPermissionTracker = new com.android.server.am.AppPermissionTracker(this.mContext, controller);
            controller.mAppStateTrackers.add(this.mAppBatteryTracker);
            controller.mAppStateTrackers.add(this.mAppBatteryExemptionTracker);
            controller.mAppStateTrackers.add(this.mAppFGSTracker);
            controller.mAppStateTrackers.add(this.mAppMediaSessionTracker);
            controller.mAppStateTrackers.add(this.mAppPermissionTracker);
            controller.mAppStateTrackers.add(new com.android.server.am.AppBroadcastEventsTracker(this.mContext, controller));
            controller.mAppStateTrackers.add(new com.android.server.am.AppBindServiceEventsTracker(this.mContext, controller));
        }

        android.app.ActivityManagerInternal getActivityManagerInternal() {
            if (this.mActivityManagerInternal == null) {
                this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            }
            return this.mActivityManagerInternal;
        }

        com.android.server.am.AppRestrictionController getAppRestrictionController() {
            return this.mAppRestrictionController;
        }

        android.app.AppOpsManager getAppOpsManager() {
            if (this.mAppOpsManager == null) {
                this.mAppOpsManager = (android.app.AppOpsManager) getContext().getSystemService(android.app.AppOpsManager.class);
            }
            return this.mAppOpsManager;
        }

        com.android.server.usage.AppStandbyInternal getAppStandbyInternal() {
            if (this.mAppStandbyInternal == null) {
                this.mAppStandbyInternal = (com.android.server.usage.AppStandbyInternal) com.android.server.LocalServices.getService(com.android.server.usage.AppStandbyInternal.class);
            }
            return this.mAppStandbyInternal;
        }

        com.android.server.apphibernation.AppHibernationManagerInternal getAppHibernationInternal() {
            if (this.mAppHibernationInternal == null) {
                this.mAppHibernationInternal = (com.android.server.apphibernation.AppHibernationManagerInternal) com.android.server.LocalServices.getService(com.android.server.apphibernation.AppHibernationManagerInternal.class);
            }
            return this.mAppHibernationInternal;
        }

        com.android.server.AppStateTracker getAppStateTracker() {
            if (this.mAppStateTracker == null) {
                this.mAppStateTracker = (com.android.server.AppStateTracker) com.android.server.LocalServices.getService(com.android.server.AppStateTracker.class);
            }
            return this.mAppStateTracker;
        }

        android.app.IActivityManager getIActivityManager() {
            return android.app.ActivityManager.getService();
        }

        com.android.server.pm.UserManagerInternal getUserManagerInternal() {
            if (this.mUserManagerInternal == null) {
                this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            }
            return this.mUserManagerInternal;
        }

        android.content.pm.PackageManagerInternal getPackageManagerInternal() {
            if (this.mPackageManagerInternal == null) {
                this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            }
            return this.mPackageManagerInternal;
        }

        android.content.pm.PackageManager getPackageManager() {
            return getContext().getPackageManager();
        }

        android.app.NotificationManager getNotificationManager() {
            if (this.mNotificationManager == null) {
                this.mNotificationManager = (android.app.NotificationManager) getContext().getSystemService(android.app.NotificationManager.class);
            }
            return this.mNotificationManager;
        }

        android.app.role.RoleManager getRoleManager() {
            if (this.mRoleManager == null) {
                this.mRoleManager = (android.app.role.RoleManager) getContext().getSystemService(android.app.role.RoleManager.class);
            }
            return this.mRoleManager;
        }

        android.telephony.TelephonyManager getTelephonyManager() {
            if (this.mTelephonyManager == null) {
                this.mTelephonyManager = (android.telephony.TelephonyManager) getContext().getSystemService(android.telephony.TelephonyManager.class);
            }
            return this.mTelephonyManager;
        }

        com.android.server.am.AppFGSTracker getAppFGSTracker() {
            return this.mAppFGSTracker;
        }

        com.android.server.am.AppMediaSessionTracker getAppMediaSessionTracker() {
            return this.mAppMediaSessionTracker;
        }

        com.android.server.am.ActivityManagerService getActivityManagerService() {
            return this.mAppRestrictionController.mActivityManagerService;
        }

        com.android.server.am.AppRestrictionController.UidBatteryUsageProvider getUidBatteryUsageProvider() {
            return this.mAppBatteryTracker;
        }

        com.android.server.am.AppBatteryExemptionTracker getAppBatteryExemptionTracker() {
            return this.mAppBatteryExemptionTracker;
        }

        com.android.server.am.AppPermissionTracker getAppPermissionTracker() {
            return this.mAppPermissionTracker;
        }

        java.lang.String getPackageName(int pid) {
            android.content.pm.ApplicationInfo ai;
            com.android.server.am.ActivityManagerService am = getActivityManagerService();
            synchronized (am.mPidsSelfLocked) {
                com.android.server.am.ProcessRecord app = am.mPidsSelfLocked.get(pid);
                if (app != null && (ai = app.info) != null) {
                    return ai.packageName;
                }
                return null;
            }
        }

        void scheduleInitTrackers(android.os.Handler handler, java.lang.Runnable initializers) {
            handler.post(initializers);
        }

        java.io.File getDataSystemDeDirectory(int userId) {
            return android.os.Environment.getDataSystemDeDirectory(userId);
        }

        long currentTimeMillis() {
            return java.lang.System.currentTimeMillis();
        }

        android.os.Handler getDefaultHandler() {
            return this.mAppRestrictionController.mActivityManagerService.mHandler;
        }

        boolean isTest() {
            return false;
        }
    }

    private void registerForSystemBroadcasts() {
        android.content.IntentFilter packageFilter = new android.content.IntentFilter();
        packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
        packageFilter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
        packageFilter.addDataScheme("package");
        this.mContext.registerReceiverForAllUsers(this.mBroadcastReceiver, packageFilter, null, this.mBgHandler);
        android.content.IntentFilter userFilter = new android.content.IntentFilter();
        userFilter.addAction("android.intent.action.USER_ADDED");
        userFilter.addAction("android.intent.action.USER_REMOVED");
        userFilter.addAction("android.intent.action.UID_REMOVED");
        this.mContext.registerReceiverForAllUsers(this.mBroadcastReceiver, userFilter, null, this.mBgHandler);
        android.content.IntentFilter bootFilter = new android.content.IntentFilter();
        bootFilter.addAction("android.intent.action.LOCKED_BOOT_COMPLETED");
        this.mContext.registerReceiverAsUser(this.mBootReceiver, android.os.UserHandle.SYSTEM, bootFilter, null, this.mBgHandler);
        android.content.IntentFilter telFilter = new android.content.IntentFilter("android.telephony.action.MULTI_SIM_CONFIG_CHANGED");
        this.mContext.registerReceiverForAllUsers(this.mBroadcastReceiver, telFilter, null, this.mBgHandler);
    }

    private void unregisterForSystemBroadcasts() {
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        this.mContext.unregisterReceiver(this.mBootReceiver);
    }

    void forEachTracker(java.util.function.Consumer<com.android.server.am.BaseAppStateTracker> sink) {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            sink.accept(this.mAppStateTrackers.get(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserAdded(int userId) {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onUserAdded(userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserStarted(int userId) throws java.lang.Throwable {
        refreshAppRestrictionLevelForUser(userId, 1024, 2);
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onUserStarted(userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserStopped(int userId) {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onUserStopped(userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserRemoved(int userId) {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onUserRemoved(userId);
        }
        this.mRestrictionSettings.removeUser(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUidAdded(int uid) {
        refreshAppRestrictionLevelForUid(uid, 1536, 0, false);
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onUidAdded(uid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageRemoved(java.lang.String pkgName, int uid) {
        this.mRestrictionSettings.removePackage(pkgName, uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUidRemoved(int uid) {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onUidRemoved(uid);
        }
        this.mRestrictionSettings.removeUid(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLockedBootCompleted() {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onLockedBootCompleted();
        }
        this.mLockedBootCompleted = true;
    }

    boolean isBgAutoRestrictedBucketFeatureFlagEnabled() {
        return this.mConstantsObserver.mBgAutoRestrictedBucket;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPropertiesChanged(java.lang.String name) {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onPropertiesChanged(name);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserInteractionStarted(java.lang.String packageName, int userId) {
        int uid = this.mInjector.getPackageManagerInternal().getPackageUid(packageName, 819200L, userId);
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onUserInteractionStarted(packageName, uid);
        }
    }

    public com.android.server.am.IAppRestrictionControllerWrapper getWrapper() {
        return this.mAppRestrictionControllerWrapper;
    }

    private class AppRestrictionControllerWrapper implements com.android.server.am.IAppRestrictionControllerWrapper {
        private AppRestrictionControllerWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.am.IAppRestrictionControllerExt getExtImpl() {
            return com.android.server.am.AppRestrictionController.this.mAppRestrictionControllerExt;
        }

        @Override // com.android.server.am.IAppRestrictionControllerWrapper
        public com.android.server.am.IAppRestrictionControllerExt.IStaticExt getStaticExtImpl() {
            return com.android.server.am.AppRestrictionController.this.mStaticExt;
        }

        @Override // com.android.server.am.IAppRestrictionControllerWrapper
        public com.android.server.am.AppRestrictionController.Injector getInjector() {
            return com.android.server.am.AppRestrictionController.this.mInjector;
        }

        @Override // com.android.server.am.IAppRestrictionControllerWrapper
        public java.lang.Object getSettingsLock() {
            return com.android.server.am.AppRestrictionController.this.mSettingsLock;
        }
    }
}
