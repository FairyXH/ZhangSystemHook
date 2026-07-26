package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationManagerService extends com.android.server.SystemService {
    private static final java.lang.String ADSERVICES_MODULE_PKG_NAME = "com.android.adservices";
    private static final java.lang.String ATTR_VERSION = "version";
    private static final long CHANGE_BACKGROUND_CUSTOM_TOAST_BLOCK = 128611929;
    private static final int DB_VERSION = 1;
    static final float DEFAULT_MAX_NOTIFICATION_ENQUEUE_RATE = 5.0f;
    private static final int DEFAULT_NOTIFICATION_VIBRATION_STRENGTH = 1800;
    private static final long DELAY_FOR_ASSISTANT_TIME = 200;
    private static final int END_NOTIFICATION_VIBRATION_STRENGTH = 2400;
    static final long ENFORCE_NO_CLEAR_FLAG_ON_MEDIA_NOTIFICATION = 264179692;
    private static final int EVENTLOG_ENQUEUE_STATUS_IGNORED = 2;
    private static final int EVENTLOG_ENQUEUE_STATUS_NEW = 0;
    private static final int EVENTLOG_ENQUEUE_STATUS_UPDATE = 1;
    private static final java.lang.String EXTRA_KEY = "key";
    static final int FINISH_TOKEN_TIMEOUT = 11000;
    static final int INVALID_UID = -1;
    private static final java.lang.String LOCKSCREEN_ALLOW_SECURE_NOTIFICATIONS_TAG = "allow-secure-notifications-on-lockscreen";
    private static final java.lang.String LOCKSCREEN_ALLOW_SECURE_NOTIFICATIONS_VALUE = "value";
    static final int LONG_DELAY = 3500;
    static final long MANAGE_GLOBAL_ZEN_VIA_IMPLICIT_RULES = 308670109;
    static final int MATCHES_CALL_FILTER_CONTACTS_TIMEOUT_MS = 3000;
    static final float MATCHES_CALL_FILTER_TIMEOUT_AFFINITY = 1.0f;
    private static final int MAX_AMPLITUDE = 255;
    static final int MAX_PACKAGE_NOTIFICATIONS = 50;
    static final int MAX_PACKAGE_TOASTS = 5;
    static final int MESSAGE_DURATION_REACHED = 2;
    static final int MESSAGE_FINISH_TOKEN_TIMEOUT = 7;
    static final int MESSAGE_LISTENER_HINTS_CHANGED = 5;
    static final int MESSAGE_LISTENER_NOTIFICATION_FILTER_CHANGED = 6;
    static final int MESSAGE_ON_PACKAGE_CHANGED = 8;
    private static final int MESSAGE_RANKING_SORT = 1001;
    private static final int MESSAGE_RECONSIDER_RANKING = 1000;
    static final int MESSAGE_SEND_RANKING_UPDATE = 4;
    private static final long MIN_PACKAGE_OVERRATE_LOG_INTERVAL = 5000;
    private static final long NOTIFICATION_CANCELLATION_REASONS = 175319604;
    private static final int NOTIFICATION_INSTANCE_ID_MAX = 8192;
    private static final long NOTIFICATION_LOG_ASSISTANT_CANCEL = 195579280;
    private static final int NOTIFICATION_RAPID_CLEAR_THRESHOLD_MS = 5000;
    private static final long NOTIFICATION_TRAMPOLINE_BLOCK = 167676448;
    private static final long NOTIFICATION_TRAMPOLINE_BLOCK_FOR_EXEMPT_ROLES = 227752274;
    private static final long RATE_LIMIT_TOASTS = 174840628;
    public static final int REPORT_REMOTE_VIEWS = 1;
    private static final int REQUEST_CODE_TIMEOUT = 1;
    static final java.lang.String REVIEW_NOTIF_ACTION_CANCELED = "REVIEW_NOTIF_ACTION_CANCELED";
    static final java.lang.String REVIEW_NOTIF_ACTION_DISMISS = "REVIEW_NOTIF_ACTION_DISMISS";
    static final java.lang.String REVIEW_NOTIF_ACTION_REMIND = "REVIEW_NOTIF_ACTION_REMIND";
    static final int REVIEW_NOTIF_STATE_DISMISSED = 2;
    static final int REVIEW_NOTIF_STATE_RESHOWN = 3;
    static final int REVIEW_NOTIF_STATE_SHOULD_SHOW = 0;
    static final int REVIEW_NOTIF_STATE_UNKNOWN = -1;
    static final int REVIEW_NOTIF_STATE_USER_INTERACTED = 1;
    static final java.lang.String ROOT_PKG = "root";
    private static final java.lang.String SCHEME_TIMEOUT = "timeout";
    static final int SHORT_DELAY = 2000;
    static final long SNOOZE_UNTIL_UNSPECIFIED = -1;
    private static final int START_NOTIFICATION_VIBRATION_STRENGTH = 800;
    private static final java.lang.String SYSTEM_NOTIFICATION_VIBRATION_INTENSITY = "notification_stepless_vibration_intensity";
    private static final java.lang.String TAG_NOTIFICATION_POLICY = "notification-policy";
    static final java.lang.String TOAST_QUOTA_TAG = "toast_quota_tag";
    private static final int USER_ID_MULTI_APP = 999;
    private android.view.accessibility.AccessibilityManager mAccessibilityManager;
    private android.app.ActivityManager mActivityManager;
    private android.content.pm.ModuleInfo mAdservicesModuleInfo;
    private android.app.AlarmManager mAlarmManager;
    private com.android.internal.util.function.TriPredicate<java.lang.String, java.lang.Integer, java.lang.String> mAllowedManagedServicePackages;
    private android.app.IActivityManager mAm;
    private android.app.ActivityManagerInternal mAmi;
    private android.app.AppOpsManager mAppOps;
    private android.app.AppOpsManager.OnOpChangedListener mAppOpsListener;
    private android.app.usage.UsageStatsManagerInternal mAppUsageStats;
    private com.android.server.notification.NotificationManagerService.Archive mArchive;
    com.android.server.notification.NotificationManagerService.NotificationAssistants mAssistants;
    private com.android.server.wm.ActivityTaskManagerInternal mAtm;
    protected com.android.server.notification.NotificationAttentionHelper mAttentionHelper;
    private int mAutoGroupAtCount;
    final android.util.ArrayMap<java.lang.Integer, android.util.ArrayMap<java.lang.String, java.lang.String>> mAutobundledSummaries;
    final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.Integer, android.os.RemoteCallbackList<android.app.ICallNotificationEventCallback>>> mCallNotificationEventCallbacks;
    android.companion.ICompanionDeviceManager mCompanionManager;
    private com.android.server.notification.ConditionProviders mConditionProviders;
    private java.lang.String mDefaultSearchSelectorPkg;
    private android.os.DeviceIdleManager mDeviceIdleManager;
    private android.app.admin.DevicePolicyManagerInternal mDpm;
    private java.util.List<android.content.ComponentName> mEffectsSuppressors;
    final java.util.ArrayList<com.android.server.notification.NotificationRecord> mEnqueuedNotifications;
    private com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.FlagResolver mFlagResolver;
    final android.os.IBinder mForegroundToken;
    private com.android.server.notification.GroupHelper mGroupHelper;
    com.android.server.notification.NotificationManagerService.WorkerHandler mHandler;
    private com.android.server.notification.NotificationHistoryManager mHistoryManager;
    final android.util.ArrayMap<java.lang.String, com.android.server.notification.InlineReplyUriRecord> mInlineReplyRecordsByKey;
    private final android.content.BroadcastReceiver mIntentReceiver;
    private final com.android.server.notification.NotificationManagerInternal mInternalService;
    private int mInterruptionFilter;
    private boolean mIsCurrentToastShown;
    private boolean mIsTelevision;
    private long mLastOverRateLogTime;
    private int mListenerHints;
    private com.android.server.notification.NotificationManagerService.NotificationListeners mListeners;
    private final android.util.SparseArray<android.util.ArraySet<android.content.ComponentName>> mListenersDisablingEffects;
    protected final android.content.BroadcastReceiver mLocaleChangeReceiver;
    private boolean mLockScreenAllowSecureNotifications;
    private com.android.internal.widget.LockPatternUtils mLockUtils;
    private float mMaxPackageEnqueueRate;
    private com.android.internal.logging.MetricsLogger mMetricsLogger;
    private java.util.Set<java.lang.String> mMsgPkgsAllowedAsConvos;
    private com.android.server.notification.INotificationManagerServiceWrapper mNMSWrapper;
    private com.android.server.notification.NotificationChannelLogger mNotificationChannelLogger;
    final com.android.server.notification.NotificationDelegate mNotificationDelegate;
    private com.android.internal.logging.InstanceIdSequence mNotificationInstanceIdSequence;
    final java.util.ArrayList<com.android.server.notification.NotificationRecord> mNotificationList;
    final java.lang.Object mNotificationLock;
    com.android.server.notification.NotificationManagerPrivate mNotificationManagerPrivate;
    private com.android.server.notification.NotificationRecordLogger mNotificationRecordLogger;
    private final android.content.BroadcastReceiver mNotificationTimeoutReceiver;
    private int mNotificationVibrationIntensity;
    final android.util.ArrayMap<java.lang.String, com.android.server.notification.NotificationRecord> mNotificationsByKey;
    private final android.content.BroadcastReceiver mPackageIntentReceiver;
    android.content.pm.IPackageManager mPackageManager;
    private android.content.pm.PackageManager mPackageManagerClient;
    android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private com.android.server.notification.PermissionHelper mPermissionHelper;
    private android.permission.PermissionManager mPermissionManager;
    private com.android.server.policy.PermissionPolicyInternal mPermissionPolicyInternal;
    private com.android.internal.compat.IPlatformCompat mPlatformCompat;
    private android.util.AtomicFile mPolicyFile;
    private com.android.server.notification.NotificationManagerService.PostNotificationTrackerFactory mPostNotificationTrackerFactory;
    private android.os.PowerManager mPowerManager;
    com.android.server.notification.PreferencesHelper mPreferencesHelper;
    private com.android.server.notification.NotificationManagerService.StatsPullAtomCallbackImpl mPullAtomCallback;
    protected com.android.server.notification.RankingHandler mRankingHandler;
    com.android.server.notification.RankingHelper mRankingHelper;
    private final android.os.HandlerThread mRankingThread;
    private final android.content.BroadcastReceiver mRestoreReceiver;
    private com.android.server.notification.ReviewNotificationPermissionsReceiver mReviewNotificationPermissionsReceiver;
    private volatile com.android.server.notification.NotificationManagerService.RoleObserver mRoleObserver;
    private final com.android.server.notification.NotificationManagerService.SavePolicyFileRunnable mSavePolicyFile;
    final android.os.IBinder mService;
    private com.android.server.notification.NotificationManagerService.SettingsObserver mSettingsObserver;
    private com.android.server.notification.ShortcutHelper mShortcutHelper;
    private com.android.server.notification.ShortcutHelper.ShortcutListener mShortcutListener;
    protected boolean mShowReviewPermissionsNotification;
    protected com.android.server.notification.SnoozeHelper mSnoozeHelper;
    private android.app.StatsManager mStatsManager;
    com.android.server.statusbar.StatusBarManagerInternal mStatusBar;
    private int mStripRemoteViewsSizeBytes;
    private com.android.server.notification.NotificationManagerService.StrongAuthTracker mStrongAuthTracker;
    final android.util.ArrayMap<java.lang.String, com.android.server.notification.NotificationRecord> mSummaryByGroupKey;
    private android.telecom.TelecomManager mTelecomManager;
    final java.util.ArrayList<com.android.server.notification.toast.ToastRecord> mToastQueue;
    private com.android.server.utils.quota.MultiRateLimiter mToastRateLimiter;
    private final java.util.Set<java.lang.Integer> mToastRateLimitingDisabledUids;
    private com.android.server.notification.TimeToLiveHelper mTtlHelper;
    private com.oplus.uifirst.IOplusUIFirstManagerExt mUIFirstManagerExt;
    private android.app.IUriGrantsManager mUgm;
    private com.android.server.uri.UriGrantsManagerInternal mUgmInternal;
    private android.os.UserManager mUm;
    private com.android.server.pm.UserManagerInternal mUmInternal;
    private com.android.server.notification.NotificationUsageStats mUsageStats;
    private android.app.usage.UsageStatsManagerInternal mUsageStatsManagerInternal;
    private final com.android.server.notification.ManagedServices.UserProfiles mUserProfiles;
    private int mWarnRemoteViewsSizeBytes;
    private com.android.server.wm.WindowManagerInternal mWindowManagerInternal;
    protected com.android.server.notification.ZenModeHelper mZenModeHelper;
    private com.android.server.zenmode.IZenModeManagerExt mZenModeManagerExt;
    public static final java.lang.String TAG = "NotificationService";
    public static final boolean DBG = android.util.Log.isLoggable(TAG, 3);
    public static final boolean ENABLE_CHILD_NOTIFICATIONS = android.os.SystemProperties.getBoolean("debug.child_notifs", true);
    static final boolean DEBUG_INTERRUPTIVENESS = android.os.SystemProperties.getBoolean("debug.notification.interruptiveness", false);
    static final java.time.Duration BITMAP_DURATION = java.time.Duration.ofHours(24);
    static final java.lang.String[] ALLOWED_ADJUSTMENTS = {"key_people", "key_snooze_criteria", "key_user_sentiment", "key_contextual_actions", "key_text_replies", "key_importance", "key_importance_proposal", "key_sensitive_content", "key_ranking_score", "key_not_conversation"};
    static final java.lang.String[] NON_BLOCKABLE_DEFAULT_ROLES = {"android.app.role.DIALER", "android.app.role.EMERGENCY"};
    private static final com.android.server.utils.quota.MultiRateLimiter.RateLimit[] TOAST_RATE_LIMITS = {com.android.server.utils.quota.MultiRateLimiter.RateLimit.create(3, java.time.Duration.ofSeconds(20)), com.android.server.utils.quota.MultiRateLimiter.RateLimit.create(5, java.time.Duration.ofSeconds(42)), com.android.server.utils.quota.MultiRateLimiter.RateLimit.create(6, java.time.Duration.ofSeconds(68))};
    private static final java.lang.String ACTION_NOTIFICATION_TIMEOUT = com.android.server.notification.NotificationManagerService.class.getSimpleName() + ".TIMEOUT";
    private static final java.time.Duration POST_WAKE_LOCK_TIMEOUT = java.time.Duration.ofSeconds(30);
    static final long NOTIFICATION_TTL = java.time.Duration.ofDays(3).toMillis();
    static final long NOTIFICATION_MAX_AGE_AT_POST = java.time.Duration.ofDays(14).toMillis();
    private static final int MY_UID = android.os.Process.myUid();
    private static final int MY_PID = android.os.Process.myPid();
    static final android.os.IBinder ALLOWLIST_TOKEN = new android.os.Binder();
    private static com.android.server.notification.INotificationManagerServiceExt mNMSExt = (com.android.server.notification.INotificationManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.notification.INotificationManagerServiceExt.class).base((java.lang.Object) null).create();

    /* JADX INFO: Access modifiers changed from: private */
    interface FlagChecker {
        boolean apply(int i);
    }

    static class Archive {
        final int mBufferSize;
        final java.lang.Object mBufferLock = new java.lang.Object();
        final java.util.LinkedList<android.util.Pair<android.service.notification.StatusBarNotification, java.lang.Integer>> mBuffer = new java.util.LinkedList<>();
        final android.util.SparseArray<java.lang.Boolean> mEnabled = new android.util.SparseArray<>();

        public Archive(int size) {
            this.mBufferSize = size;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            int N = this.mBuffer.size();
            sb.append("Archive (");
            sb.append(N);
            sb.append(" notification");
            sb.append(N == 1 ? ")" : "s)");
            return sb.toString();
        }

        public void record(android.service.notification.StatusBarNotification sbn, int reason) {
            if (!this.mEnabled.get(sbn.getNormalizedUserId(), false).booleanValue()) {
                return;
            }
            synchronized (this.mBufferLock) {
                if (this.mBuffer.size() == this.mBufferSize) {
                    this.mBuffer.removeFirst();
                }
                this.mBuffer.addLast(new android.util.Pair<>(sbn.cloneLight(), java.lang.Integer.valueOf(reason)));
            }
        }

        public java.util.Iterator<android.util.Pair<android.service.notification.StatusBarNotification, java.lang.Integer>> descendingIterator() {
            return this.mBuffer.descendingIterator();
        }

        public android.service.notification.StatusBarNotification[] getArray(final android.os.UserManager um, int count, boolean includeSnoozed) {
            java.util.List<android.service.notification.StatusBarNotification> a;
            java.util.Iterator<android.util.Pair<android.service.notification.StatusBarNotification, java.lang.Integer>> iter;
            int i;
            android.service.notification.StatusBarNotification[] statusBarNotificationArr;
            android.util.Pair<android.service.notification.StatusBarNotification, java.lang.Integer> pair;
            final java.util.ArrayList<java.lang.Integer> currentUsers = new java.util.ArrayList<>();
            currentUsers.add(-1);
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$Archive$$ExternalSyntheticLambda0
                public final void runOrThrow() throws java.lang.Exception {
                    com.android.server.notification.NotificationManagerService.Archive.lambda$getArray$0(um, currentUsers);
                }
            });
            synchronized (this.mBufferLock) {
                if (count == 0) {
                    count = this.mBufferSize;
                    a = new java.util.ArrayList<>();
                    iter = descendingIterator();
                    i = 0;
                    while (iter.hasNext() && i < count) {
                        pair = iter.next();
                        if ((((java.lang.Integer) pair.second).intValue() == 18 || includeSnoozed) && currentUsers.contains(java.lang.Integer.valueOf(((android.service.notification.StatusBarNotification) pair.first).getUserId()))) {
                            i++;
                            a.add((android.service.notification.StatusBarNotification) pair.first);
                        }
                    }
                    statusBarNotificationArr = (android.service.notification.StatusBarNotification[]) a.toArray(new android.service.notification.StatusBarNotification[a.size()]);
                } else {
                    a = new java.util.ArrayList<>();
                    iter = descendingIterator();
                    i = 0;
                    while (iter.hasNext()) {
                        pair = iter.next();
                        if (((java.lang.Integer) pair.second).intValue() == 18) {
                            i++;
                            a.add((android.service.notification.StatusBarNotification) pair.first);
                        } else {
                            i++;
                            a.add((android.service.notification.StatusBarNotification) pair.first);
                        }
                    }
                    statusBarNotificationArr = (android.service.notification.StatusBarNotification[]) a.toArray(new android.service.notification.StatusBarNotification[a.size()]);
                }
            }
            return statusBarNotificationArr;
        }

        static /* synthetic */ void lambda$getArray$0(android.os.UserManager um, java.util.ArrayList currentUsers) throws java.lang.Exception {
            for (int user : um.getProfileIds(android.app.ActivityManager.getCurrentUser(), false)) {
                currentUsers.add(java.lang.Integer.valueOf(user));
            }
        }

        public void updateHistoryEnabled(int userId, boolean enabled) {
            this.mEnabled.put(userId, java.lang.Boolean.valueOf(enabled));
            if (userId == 0 && com.android.server.notification.NotificationManagerService.mNMSExt != null) {
                this.mEnabled.put(com.android.server.notification.NotificationManagerService.mNMSExt.getMultiAppUserId(), java.lang.Boolean.valueOf(enabled));
            }
            if (!enabled) {
                synchronized (this.mBufferLock) {
                    for (int i = this.mBuffer.size() - 1; i >= 0; i--) {
                        if (userId == ((android.service.notification.StatusBarNotification) this.mBuffer.get(i).first).getNormalizedUserId()) {
                            this.mBuffer.remove(i);
                        }
                    }
                }
            }
        }

        public void removeChannelNotifications(java.lang.String pkg, int userId, java.lang.String channelId) {
            synchronized (this.mBufferLock) {
                java.util.Iterator<android.util.Pair<android.service.notification.StatusBarNotification, java.lang.Integer>> bufferIter = descendingIterator();
                while (bufferIter.hasNext()) {
                    android.util.Pair<android.service.notification.StatusBarNotification, java.lang.Integer> pair = bufferIter.next();
                    if (pair.first != null && userId == ((android.service.notification.StatusBarNotification) pair.first).getNormalizedUserId() && pkg != null && pkg.equals(((android.service.notification.StatusBarNotification) pair.first).getPackageName()) && ((android.service.notification.StatusBarNotification) pair.first).getNotification() != null && java.util.Objects.equals(channelId, ((android.service.notification.StatusBarNotification) pair.first).getNotification().getChannelId())) {
                        bufferIter.remove();
                    }
                }
            }
        }

        public void removePackageNotifications(java.lang.String pkg, int userId) {
            synchronized (this.mBufferLock) {
                java.util.Iterator<android.util.Pair<android.service.notification.StatusBarNotification, java.lang.Integer>> bufferIter = descendingIterator();
                while (bufferIter.hasNext()) {
                    android.util.Pair<android.service.notification.StatusBarNotification, java.lang.Integer> pair = bufferIter.next();
                    if (pair.first != null && userId == ((android.service.notification.StatusBarNotification) pair.first).getNormalizedUserId() && pkg != null && pkg.equals(((android.service.notification.StatusBarNotification) pair.first).getPackageName()) && ((android.service.notification.StatusBarNotification) pair.first).getNotification() != null) {
                        bufferIter.remove();
                    }
                }
            }
        }

        void dumpImpl(java.io.PrintWriter pw, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
            synchronized (this.mBufferLock) {
                java.util.Iterator<android.util.Pair<android.service.notification.StatusBarNotification, java.lang.Integer>> iter = descendingIterator();
                int i = 0;
                while (true) {
                    if (!iter.hasNext()) {
                        break;
                    }
                    android.service.notification.StatusBarNotification sbn = (android.service.notification.StatusBarNotification) iter.next().first;
                    if (filter == null || filter.matches(sbn)) {
                        pw.println("    " + sbn);
                        i++;
                        if (i >= 5) {
                            if (iter.hasNext()) {
                                pw.println("    ...");
                            }
                        }
                    }
                }
            }
        }
    }

    void loadDefaultApprovedServices(int userId) {
        this.mListeners.loadDefaultsFromConfig();
        this.mConditionProviders.loadDefaultsFromConfig();
        this.mAssistants.loadDefaultsFromConfig();
    }

    protected void allowDefaultApprovedServices(int userId) {
        android.util.ArraySet<android.content.ComponentName> defaultListeners = this.mListeners.getDefaultComponents();
        for (int i = 0; i < defaultListeners.size(); i++) {
            android.content.ComponentName cn = defaultListeners.valueAt(i);
            allowNotificationListener(userId, cn);
        }
        allowDndPackages(userId);
        setDefaultAssistantForUser(userId);
    }

    void allowDndPackages(int userId) {
        android.util.ArraySet<java.lang.String> defaultDnds = this.mConditionProviders.getDefaultPackages();
        for (int i = 0; i < defaultDnds.size(); i++) {
            allowDndPackage(userId, defaultDnds.valueAt(i));
        }
        if (!isDNDMigrationDone(userId)) {
            setDNDMigrationDone(userId);
        }
    }

    boolean isDNDMigrationDone(int userId) {
        return android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "dnd_settings_migrated", 0, userId) == 1;
    }

    void setDNDMigrationDone(int userId) {
        android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "dnd_settings_migrated", 1, userId);
    }

    protected void migrateDefaultNAS() {
        java.util.List<android.content.pm.UserInfo> activeUsers = this.mUm.getUsers();
        for (android.content.pm.UserInfo userInfo : activeUsers) {
            int userId = userInfo.getUserHandle().getIdentifier();
            if (!isNASMigrationDone(userId) && !isProfileUser(userInfo)) {
                java.util.List<android.content.ComponentName> allowedComponents = this.mAssistants.getAllowedComponents(userId);
                if (allowedComponents.size() == 0) {
                    android.util.Slog.d(TAG, "NAS Migration: user set to none, disable new NAS setting");
                    setNASMigrationDone(userId);
                    this.mAssistants.clearDefaults();
                } else {
                    android.util.Slog.d(TAG, "Reset NAS setting and migrate to new default");
                    resetAssistantUserSet(userId);
                    this.mAssistants.resetDefaultAssistantsIfNecessary();
                }
            }
        }
    }

    void setNASMigrationDone(int baseUserId) {
        for (int profileId : this.mUm.getProfileIds(baseUserId, false)) {
            android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "nas_settings_updated", 1, profileId);
        }
    }

    boolean isNASMigrationDone(int userId) {
        return android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "nas_settings_updated", 0, userId) == 1;
    }

    boolean isProfileUser(android.content.pm.UserInfo userInfo) {
        return privateSpaceFlagsEnabled() ? userInfo.isProfile() && hasParent(userInfo) : userInfo.isManagedProfile() || userInfo.isCloneProfile();
    }

    boolean hasParent(android.content.pm.UserInfo profile) {
        return this.mUmInternal.getProfileParentId(profile.id) != profile.id;
    }

    protected void setDefaultAssistantForUser(int userId) {
        android.util.ArraySet<android.content.ComponentName> defaults = this.mAssistants.getDefaultComponents();
        for (int i = 0; i < defaults.size(); i++) {
            android.content.ComponentName cn = defaults.valueAt(i);
            if (allowAssistant(userId, cn)) {
                return;
            }
        }
    }

    protected void updateAutobundledSummaryLocked(int userId, java.lang.String pkg, com.android.server.notification.GroupHelper.NotificationAttributes summaryAttr, boolean isAppForeground) {
        java.lang.String summaryKey;
        com.android.server.notification.NotificationRecord summary;
        android.util.ArrayMap<java.lang.String, java.lang.String> summaries = this.mAutobundledSummaries.get(java.lang.Integer.valueOf(userId));
        if (summaries == null || (summaryKey = summaries.get(pkg)) == null || (summary = this.mNotificationsByKey.get(summaryKey)) == null) {
            return;
        }
        int oldFlags = summary.getSbn().getNotification().flags;
        boolean attributesUpdated = (summaryAttr.icon.sameAs(summary.getSbn().getNotification().getSmallIcon()) && summaryAttr.iconColor == summary.getSbn().getNotification().color && summaryAttr.visibility == summary.getSbn().getNotification().visibility) ? false : true;
        if (oldFlags != summaryAttr.flags || attributesUpdated) {
            summary.getSbn().getNotification().flags = summaryAttr.flags != -1 ? summaryAttr.flags : oldFlags;
            summary.getSbn().getNotification().setSmallIcon(summaryAttr.icon);
            summary.getSbn().getNotification().color = summaryAttr.iconColor;
            summary.getSbn().getNotification().visibility = summaryAttr.visibility;
            this.mHandler.post(new com.android.server.notification.NotificationManagerService.EnqueueNotificationRunnable(userId, summary, isAppForeground, this.mPostNotificationTrackerFactory.newTracker(null)));
        }
    }

    private void allowDndPackage(int userId, java.lang.String packageName) {
        try {
            getBinderService().setNotificationPolicyAccessGrantedForUser(packageName, userId, true);
        } catch (android.os.RemoteException e) {
            e.printStackTrace();
        }
    }

    private void allowNotificationListener(int userId, android.content.ComponentName cn) {
        try {
            getBinderService().setNotificationListenerAccessGrantedForUser(cn, userId, true, true);
        } catch (android.os.RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean allowAssistant(int userId, android.content.ComponentName candidate) {
        java.util.Set<android.content.ComponentName> validAssistants = this.mAssistants.queryPackageForServices(null, com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED, userId);
        if (candidate == null || !validAssistants.contains(candidate)) {
            return false;
        }
        setNotificationAssistantAccessGrantedForUserInternal(candidate, userId, true, false);
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x00d8 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0031 A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void readPolicyXml(java.io.InputStream r10, boolean r11, int r12) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, java.lang.NumberFormatException {
        /*
            Method dump skipped, instruction units count: 282
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationManagerService.readPolicyXml(java.io.InputStream, boolean, int):void");
    }

    void resetDefaultDndIfNecessary() {
        boolean removed = false;
        java.util.List<android.content.pm.UserInfo> activeUsers = this.mUm.getAliveUsers();
        for (android.content.pm.UserInfo userInfo : activeUsers) {
            int userId = userInfo.getUserHandle().getIdentifier();
            if (!isDNDMigrationDone(userId)) {
                removed |= this.mConditionProviders.removeDefaultFromConfig(userId);
                this.mConditionProviders.resetDefaultFromConfig();
                allowDndPackages(userId);
            }
        }
        if (removed) {
            handleSavePolicyFile();
        }
    }

    protected void loadPolicyFile() {
        if (DBG) {
            android.util.Slog.d(TAG, "loadPolicyFile");
        }
        synchronized (this.mPolicyFile) {
            java.io.InputStream infile = null;
            try {
                try {
                    try {
                        infile = this.mPolicyFile.openRead();
                        readPolicyXml(infile, false, -1);
                        boolean isWatch = this.mPackageManagerClient.hasSystemFeature("android.hardware.type.watch");
                        if (isWatch) {
                            resetDefaultDndIfNecessary();
                        }
                    } catch (java.lang.NumberFormatException e) {
                        android.util.Log.wtf(TAG, "Unable to parse notification policy", e);
                    } catch (org.xmlpull.v1.XmlPullParserException e2) {
                        android.util.Log.wtf(TAG, "Unable to parse notification policy", e2);
                    }
                } catch (java.io.FileNotFoundException e3) {
                    loadDefaultApprovedServices(0);
                    allowDefaultApprovedServices(0);
                } catch (java.io.IOException e4) {
                    android.util.Log.wtf(TAG, "Unable to read notification policy", e4);
                }
                libcore.io.IoUtils.closeQuietly(infile);
            } finally {
                libcore.io.IoUtils.closeQuietly(infile);
            }
        }
    }

    protected void handleSavePolicyFile() {
        if (!com.android.server.IoThread.getHandler().hasCallbacks(this.mSavePolicyFile)) {
            com.android.server.IoThread.getHandler().postDelayed(this.mSavePolicyFile, 250L);
        }
    }

    protected static boolean privateSpaceFlagsEnabled() {
        return com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enablePrivateSpaceFeatures();
    }

    private final class SavePolicyFileRunnable implements java.lang.Runnable {
        private SavePolicyFileRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (com.android.server.notification.NotificationManagerService.DBG) {
                android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, "handleSavePolicyFile");
            }
            synchronized (com.android.server.notification.NotificationManagerService.this.mPolicyFile) {
                try {
                    java.io.FileOutputStream stream = com.android.server.notification.NotificationManagerService.this.mPolicyFile.startWrite();
                    try {
                        com.android.server.notification.NotificationManagerService.this.writePolicyXml(stream, false, -1);
                        com.android.server.notification.NotificationManagerService.this.mPolicyFile.finishWrite(stream);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "Failed to save policy file, restoring backup", e);
                        com.android.server.notification.NotificationManagerService.this.mPolicyFile.failWrite(stream);
                    }
                } catch (java.io.IOException e2) {
                    android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "Failed to save policy file", e2);
                    return;
                }
            }
            android.app.backup.BackupManager.dataChanged(com.android.server.notification.NotificationManagerService.this.getContext().getPackageName());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writePolicyXml(java.io.OutputStream stream, boolean forBackup, int userId) throws java.io.IOException {
        com.android.modules.utils.TypedXmlSerializer out;
        if (forBackup) {
            out = android.util.Xml.newFastSerializer();
            out.setOutput(stream, java.nio.charset.StandardCharsets.UTF_8.name());
        } else {
            out = android.util.Xml.resolveSerializer(stream);
        }
        out.startDocument((java.lang.String) null, true);
        out.startTag((java.lang.String) null, TAG_NOTIFICATION_POLICY);
        out.attributeInt((java.lang.String) null, ATTR_VERSION, 1);
        this.mZenModeHelper.writeXml(out, forBackup, null, userId);
        this.mPreferencesHelper.writeXml(out, forBackup, userId);
        this.mListeners.writeXml(out, forBackup, userId);
        this.mAssistants.writeXml(out, forBackup, userId);
        this.mSnoozeHelper.writeXml(out);
        this.mConditionProviders.writeXml(out, forBackup, userId);
        if (!forBackup || userId == 0) {
            writeSecureNotificationsPolicy(out);
        }
        out.endTag((java.lang.String) null, TAG_NOTIFICATION_POLICY);
        out.endDocument();
    }

    /* JADX INFO: renamed from: com.android.server.notification.NotificationManagerService$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.server.notification.NotificationDelegate {
        AnonymousClass1() {
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void prepareForPossibleShutdown() {
            com.android.server.notification.NotificationManagerService.this.mHistoryManager.triggerWriteToDisk();
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onSetDisabled(int status) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationManagerService.this.mAttentionHelper.updateDisableNotificationEffectsLocked(status);
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onClearAll(int callingUid, int callingPid, int userId) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationManagerService.this.cancelAllLocked(callingUid, callingPid, userId, 3, null, true, 34);
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationClick(int callingUid, int callingPid, java.lang.String key, com.android.internal.statusbar.NotificationVisibility nv) {
            com.android.server.notification.NotificationManagerService.this.exitIdle();
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                if (r == null) {
                    android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "No notification with key: " + key);
                    return;
                }
                long now = java.lang.System.currentTimeMillis();
                com.android.internal.logging.MetricsLogger.action(r.getItemLogMaker().setType(4).addTaggedData(798, java.lang.Integer.valueOf(nv.rank)).addTaggedData(1395, java.lang.Integer.valueOf(nv.count)));
                com.android.server.notification.NotificationManagerService.this.mNotificationRecordLogger.log(com.android.server.notification.NotificationRecordLogger.NotificationEvent.NOTIFICATION_CLICKED, r);
                com.android.server.EventLogTags.writeNotificationClicked(key, r.getLifespanMs(now), r.getFreshnessMs(now), r.getExposureMs(now), nv.rank, nv.count);
                android.service.notification.StatusBarNotification sbn = r.getSbn();
                com.android.server.notification.NotificationManagerService.this.cancelNotification(callingUid, callingPid, sbn.getPackageName(), sbn.getTag(), sbn.getId(), 16, 36928, false, r.getUserId(), 1, nv.rank, nv.count, null);
                nv.recycle();
                com.android.server.notification.NotificationManagerService.this.reportUserInteraction(r);
                com.android.server.notification.NotificationManagerService.this.mAssistants.notifyAssistantNotificationClicked(r);
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationActionClick(int callingUid, int callingPid, java.lang.String key, int actionIndex, android.app.Notification.Action action, com.android.internal.statusbar.NotificationVisibility nv, boolean generatedByAssistant) throws java.lang.Throwable {
            com.android.server.notification.NotificationManagerService.this.exitIdle();
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                try {
                    try {
                        com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                        if (r == null) {
                            android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "No notification with key: " + key);
                            return;
                        }
                        long now = java.lang.System.currentTimeMillis();
                        int i = 1;
                        android.metrics.LogMaker logMakerAddTaggedData = r.getLogMaker(now).setCategory(129).setType(4).setSubtype(actionIndex).addTaggedData(798, java.lang.Integer.valueOf(nv.rank)).addTaggedData(1395, java.lang.Integer.valueOf(nv.count)).addTaggedData(1601, java.lang.Integer.valueOf(action.isContextual() ? 1 : 0));
                        if (!generatedByAssistant) {
                            i = 0;
                        }
                        com.android.internal.logging.MetricsLogger.action(logMakerAddTaggedData.addTaggedData(1600, java.lang.Integer.valueOf(i)).addTaggedData(1629, java.lang.Integer.valueOf(nv.location.toMetricsEventEnum())));
                        com.android.server.notification.NotificationManagerService.this.mNotificationRecordLogger.log(com.android.server.notification.NotificationRecordLogger.NotificationEvent.fromAction(actionIndex, generatedByAssistant, action.isContextual()), r);
                        com.android.server.EventLogTags.writeNotificationActionClicked(key, action.actionIntent.getTarget().toString(), action.actionIntent.getIntent().toString(), actionIndex, r.getLifespanMs(now), r.getFreshnessMs(now), r.getExposureMs(now), nv.rank, nv.count);
                        nv.recycle();
                        com.android.server.notification.NotificationManagerService.this.reportUserInteraction(r);
                        com.android.server.notification.NotificationManagerService.this.mAssistants.notifyAssistantActionClicked(r, action, generatedByAssistant);
                        if (android.app.Flags.lifetimeExtensionRefactor()) {
                            com.android.server.notification.NotificationManagerService.this.mHandler.scheduleCancelNotification(com.android.server.notification.NotificationManagerService.this.new CancelNotificationRunnable(callingUid, callingPid, r.getSbn().getPackageName(), r.getSbn().getTag(), r.getSbn().getId(), 65536, 8192, false, r.getUserId(), 1, -1, -1, null, android.os.SystemClock.elapsedRealtime()), 200);
                        }
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationClear(int callingUid, int callingPid, java.lang.String pkg, int userId, java.lang.String key, int dismissalSurface, int dismissalSentiment, com.android.internal.statusbar.NotificationVisibility nv) throws java.lang.Throwable {
            java.lang.String tag = null;
            int id = 0;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                try {
                    try {
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                    try {
                        com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                        if (r != null) {
                            try {
                                r.recordDismissalSurface(dismissalSurface);
                                r.recordDismissalSentiment(dismissalSentiment);
                                tag = r.getSbn().getTag();
                                id = r.getSbn().getId();
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                throw th;
                            }
                        }
                        com.android.server.notification.NotificationManagerService.this.cancelNotification(callingUid, callingPid, pkg, tag, id, 0, 8192, true, userId, 2, nv.rank, nv.count, null);
                        nv.recycle();
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                        throw th;
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onPanelRevealed(boolean clearEffects, int items) {
            com.android.internal.logging.MetricsLogger.visible(com.android.server.notification.NotificationManagerService.this.getContext(), 127);
            com.android.internal.logging.MetricsLogger.histogram(com.android.server.notification.NotificationManagerService.this.getContext(), "note_load", items);
            com.android.server.notification.NotificationManagerService.this.mNotificationRecordLogger.log(com.android.server.notification.NotificationRecordLogger.NotificationPanelEvent.NOTIFICATION_PANEL_OPEN);
            com.android.server.EventLogTags.writeNotificationPanelRevealed(items);
            if (clearEffects) {
                clearEffects();
            }
            com.android.server.notification.NotificationManagerService.this.mAssistants.onPanelRevealed(items);
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onPanelHidden() {
            com.android.internal.logging.MetricsLogger.hidden(com.android.server.notification.NotificationManagerService.this.getContext(), 127);
            com.android.server.notification.NotificationManagerService.this.mNotificationRecordLogger.log(com.android.server.notification.NotificationRecordLogger.NotificationPanelEvent.NOTIFICATION_PANEL_CLOSE);
            com.android.server.EventLogTags.writeNotificationPanelHidden();
            com.android.server.notification.NotificationManagerService.this.mAssistants.onPanelHidden();
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void clearEffects() {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                if (com.android.server.notification.NotificationManagerService.DBG) {
                    android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, "clearEffects");
                }
                com.android.server.notification.NotificationManagerService.this.mAttentionHelper.clearAttentionEffects();
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationError(int callingUid, int callingPid, final java.lang.String pkg, final java.lang.String tag, final int id, final int uid, final int initialPid, final java.lang.String message, int userId) {
            boolean fgService;
            boolean uiJob;
            final int exceptionTypeId;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.findNotificationLocked(pkg, tag, id, userId);
                boolean z = true;
                fgService = (r == null || (r.getNotification().flags & 64) == 0) ? false : true;
                if (r == null || (r.getNotification().flags & 32768) == 0) {
                    z = false;
                }
                uiJob = z;
            }
            com.android.server.notification.NotificationManagerService.this.cancelNotification(callingUid, callingPid, pkg, tag, id, 0, 0, false, userId, 4, null);
            if (fgService || uiJob) {
                if (fgService) {
                    exceptionTypeId = 3;
                } else {
                    exceptionTypeId = 6;
                }
                android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$1$$ExternalSyntheticLambda0
                    public final void runOrThrow() throws java.lang.Exception {
                        this.f$0.lambda$onNotificationError$0(uid, initialPid, pkg, tag, id, message, exceptionTypeId);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNotificationError$0(int uid, int initialPid, java.lang.String pkg, java.lang.String tag, int id, java.lang.String message, int exceptionTypeId) throws java.lang.Exception {
            com.android.server.notification.NotificationManagerService.this.mAm.crashApplicationWithType(uid, initialPid, pkg, -1, "Bad notification(tag=" + tag + ", id=" + id + ") posted from package " + pkg + ", crashing app(uid=" + uid + ", pid=" + initialPid + "): " + message, true, exceptionTypeId);
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationVisibilityChanged(com.android.internal.statusbar.NotificationVisibility[] newlyVisibleKeys, com.android.internal.statusbar.NotificationVisibility[] noLongerVisibleKeys) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                for (com.android.internal.statusbar.NotificationVisibility nv : newlyVisibleKeys) {
                    com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(nv.key);
                    if (r != null) {
                        if (!r.isSeen()) {
                            if (com.android.server.notification.NotificationManagerService.DBG) {
                                android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, "Marking notification as visible " + nv.key);
                            }
                            com.android.server.notification.NotificationManagerService.this.reportSeen(r);
                        }
                        boolean z = true;
                        r.setVisibility(true, nv.rank, nv.count, com.android.server.notification.NotificationManagerService.this.mNotificationRecordLogger);
                        if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                            com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().notifyRecordVisibilityChangedLocked(r, true, nv.rank, nv.count);
                        }
                        com.android.server.notification.NotificationManagerService.this.mAssistants.notifyAssistantVisibilityChangedLocked(r, true);
                        if (nv.location != com.android.internal.statusbar.NotificationVisibility.NotificationLocation.LOCATION_FIRST_HEADS_UP) {
                            z = false;
                        }
                        boolean isHun = z;
                        if (isHun || r.hasBeenVisiblyExpanded()) {
                            com.android.server.notification.NotificationManagerService.this.logSmartSuggestionsVisible(r, nv.location.toMetricsEventEnum());
                        }
                        com.android.server.notification.NotificationManagerService.this.maybeRecordInterruptionLocked(r);
                        nv.recycle();
                    }
                }
                for (com.android.internal.statusbar.NotificationVisibility nv2 : noLongerVisibleKeys) {
                    com.android.server.notification.NotificationRecord r2 = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(nv2.key);
                    if (r2 != null) {
                        r2.setVisibility(false, nv2.rank, nv2.count, com.android.server.notification.NotificationManagerService.this.mNotificationRecordLogger);
                        if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                            com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().notifyRecordVisibilityChangedLocked(r2, false, nv2.rank, nv2.count);
                        }
                        com.android.server.notification.NotificationManagerService.this.mAssistants.notifyAssistantVisibilityChangedLocked(r2, false);
                        nv2.recycle();
                    }
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationExpansionChanged(java.lang.String key, boolean userAction, boolean expanded, int notificationLocation) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                if (r != null) {
                    r.stats.onExpansionChanged(userAction, expanded);
                    if (r.hasBeenVisiblyExpanded()) {
                        com.android.server.notification.NotificationManagerService.this.logSmartSuggestionsVisible(r, notificationLocation);
                    }
                    if (userAction) {
                        com.android.internal.logging.MetricsLogger.action(r.getItemLogMaker().setType(expanded ? 3 : 14));
                        com.android.server.notification.NotificationManagerService.this.mNotificationRecordLogger.log(com.android.server.notification.NotificationRecordLogger.NotificationEvent.fromExpanded(expanded, userAction), r);
                    }
                    if (expanded && userAction) {
                        r.recordExpanded();
                        com.android.server.notification.NotificationManagerService.this.reportUserInteraction(r);
                    }
                    com.android.server.notification.NotificationManagerService.this.mAssistants.notifyAssistantExpansionChangedLocked(r.getSbn(), r.getNotificationType(), userAction, expanded);
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationDirectReplied(java.lang.String key) {
            int packageImportance;
            com.android.server.notification.NotificationManagerService.this.exitIdle();
            java.lang.String packageName = null;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                if (r != null) {
                    packageName = r.getSbn().getPackageName();
                }
            }
            if (android.app.Flags.lifetimeExtensionRefactor() && packageName != null) {
                packageImportance = com.android.server.notification.NotificationManagerService.this.getPackageImportanceWithIdentity(packageName);
            } else {
                packageImportance = 0;
            }
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r2 = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                if (r2 != null) {
                    if (android.app.Flags.lifetimeExtensionRefactor()) {
                        com.android.server.notification.NotificationManagerService.this.maybeNotifySystemUiListenerLifetimeExtendedLocked(r2, r2.getSbn().getPackageName(), packageImportance);
                    }
                    r2.recordDirectReplied();
                    com.android.server.notification.NotificationManagerService.this.mMetricsLogger.write(r2.getLogMaker().setCategory(1590).setType(4));
                    com.android.server.notification.NotificationManagerService.this.mNotificationRecordLogger.log(com.android.server.notification.NotificationRecordLogger.NotificationEvent.NOTIFICATION_DIRECT_REPLIED, r2);
                    com.android.server.notification.NotificationManagerService.this.reportUserInteraction(r2);
                    com.android.server.notification.NotificationManagerService.this.mAssistants.notifyAssistantNotificationDirectReplyLocked(r2);
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationSmartSuggestionsAdded(java.lang.String key, int smartReplyCount, int smartActionCount, boolean generatedByAssistant, boolean editBeforeSending) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                if (r != null) {
                    r.setNumSmartRepliesAdded(smartReplyCount);
                    r.setNumSmartActionsAdded(smartActionCount);
                    r.setSuggestionsGeneratedByAssistant(generatedByAssistant);
                    r.setEditChoicesBeforeSending(editBeforeSending);
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationSmartReplySent(java.lang.String key, int replyIndex, java.lang.CharSequence reply, int notificationLocation, boolean modifiedBeforeSending) {
            int packageImportance;
            java.lang.String packageName = null;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                if (r != null) {
                    packageName = r.getSbn().getPackageName();
                }
            }
            if (android.app.Flags.lifetimeExtensionRefactor() && packageName != null) {
                packageImportance = com.android.server.notification.NotificationManagerService.this.getPackageImportanceWithIdentity(packageName);
            } else {
                packageImportance = 0;
            }
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r2 = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                if (r2 != null) {
                    if (android.app.Flags.lifetimeExtensionRefactor()) {
                        com.android.server.notification.NotificationManagerService.this.maybeNotifySystemUiListenerLifetimeExtendedLocked(r2, r2.getSbn().getPackageName(), packageImportance);
                    }
                    r2.recordSmartReplied();
                    int i = 1;
                    android.metrics.LogMaker logMakerAddTaggedData = r2.getLogMaker().setCategory(1383).setSubtype(replyIndex).addTaggedData(1600, java.lang.Integer.valueOf(r2.getSuggestionsGeneratedByAssistant() ? 1 : 0)).addTaggedData(1629, java.lang.Integer.valueOf(notificationLocation)).addTaggedData(1647, java.lang.Integer.valueOf(r2.getEditChoicesBeforeSending() ? 1 : 0));
                    if (!modifiedBeforeSending) {
                        i = 0;
                    }
                    android.metrics.LogMaker logMaker = logMakerAddTaggedData.addTaggedData(1648, java.lang.Integer.valueOf(i));
                    com.android.server.notification.NotificationManagerService.this.mMetricsLogger.write(logMaker);
                    com.android.server.notification.NotificationManagerService.this.mNotificationRecordLogger.log(com.android.server.notification.NotificationRecordLogger.NotificationEvent.NOTIFICATION_SMART_REPLIED, r2);
                    com.android.server.notification.NotificationManagerService.this.reportUserInteraction(r2);
                    com.android.server.notification.NotificationManagerService.this.mAssistants.notifyAssistantSuggestedReplySent(r2.getSbn(), r2.getNotificationType(), reply, r2.getSuggestionsGeneratedByAssistant());
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationSettingsViewed(java.lang.String key) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                if (r != null) {
                    r.recordViewedSettings();
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationBubbleChanged(java.lang.String key, boolean isBubble, int bubbleFlags) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                if (r != null) {
                    if (!isBubble) {
                        r.getNotification().flags &= -4097;
                        r.setFlagBubbleRemoved(true);
                    } else {
                        r.getNotification().flags |= 8;
                        r.setFlagBubbleRemoved(false);
                        if (r.getNotification().getBubbleMetadata() != null) {
                            r.getNotification().getBubbleMetadata().setFlags(bubbleFlags);
                        }
                        com.android.server.notification.NotificationManagerService.this.mHandler.post(com.android.server.notification.NotificationManagerService.this.new EnqueueNotificationRunnable(r.getUser().getIdentifier(), r, true, com.android.server.notification.NotificationManagerService.this.mPostNotificationTrackerFactory.newTracker(null)));
                    }
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onBubbleMetadataFlagChanged(java.lang.String key, int flags) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                if (r != null) {
                    android.app.Notification.BubbleMetadata data = r.getNotification().getBubbleMetadata();
                    if (data == null) {
                        return;
                    }
                    if (flags != data.getFlags()) {
                        int changedFlags = data.getFlags() ^ flags;
                        if ((changedFlags & 2) != 0) {
                            com.android.server.notification.NotificationManagerService.this.mAttentionHelper.clearEffectsLocked(key, false);
                        }
                        data.setFlags(flags);
                        r.getNotification().flags |= 8;
                        com.android.server.notification.NotificationManagerService.this.mHandler.post(com.android.server.notification.NotificationManagerService.this.new EnqueueNotificationRunnable(r.getUser().getIdentifier(), r, true, com.android.server.notification.NotificationManagerService.this.mPostNotificationTrackerFactory.newTracker(null)));
                    }
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void grantInlineReplyUriPermission(java.lang.String key, android.net.Uri uri, android.os.UserHandle user, java.lang.String packageName, int callingUid) throws java.lang.Throwable {
            com.android.server.notification.InlineReplyUriRecord newRecord;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                try {
                    try {
                        com.android.server.notification.InlineReplyUriRecord r = com.android.server.notification.NotificationManagerService.this.mInlineReplyRecordsByKey.get(key);
                        if (r == null) {
                            com.android.server.notification.InlineReplyUriRecord newRecord2 = new com.android.server.notification.InlineReplyUriRecord(com.android.server.notification.NotificationManagerService.this.mUgmInternal.newUriPermissionOwner("INLINE_REPLY:" + key), user, packageName, key);
                            com.android.server.notification.NotificationManagerService.this.mInlineReplyRecordsByKey.put(key, newRecord2);
                            newRecord = newRecord2;
                        } else {
                            newRecord = r;
                        }
                        android.os.IBinder owner = newRecord.getPermissionOwner();
                        int uid = callingUid;
                        int userId = newRecord.getUserId();
                        if (android.os.UserHandle.getUserId(uid) != userId) {
                            try {
                                java.lang.String[] pkgs = com.android.server.notification.NotificationManagerService.this.mPackageManager.getPackagesForUid(callingUid);
                                if (pkgs == null) {
                                    android.util.Log.e(com.android.server.notification.NotificationManagerService.TAG, "Cannot grant uri permission to unknown UID: " + callingUid);
                                }
                                java.lang.String pkg = pkgs[0];
                                uid = com.android.server.notification.NotificationManagerService.this.mPackageManager.getPackageUid(pkg, 0L, userId);
                            } catch (android.os.RemoteException re) {
                                android.util.Log.e(com.android.server.notification.NotificationManagerService.TAG, "Cannot talk to package manager", re);
                            }
                        }
                        newRecord.addUri(uri);
                        com.android.server.notification.NotificationManagerService.this.grantUriPermission(owner, uri, uid, newRecord.getPackageName(), userId);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void clearInlineReplyUriPermissions(java.lang.String key, int callingUid) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.InlineReplyUriRecord uriRecord = com.android.server.notification.NotificationManagerService.this.mInlineReplyRecordsByKey.get(key);
                if (uriRecord != null) {
                    com.android.server.notification.NotificationManagerService.this.destroyPermissionOwner(uriRecord.getPermissionOwner(), uriRecord.getUserId(), "INLINE_REPLY: " + uriRecord.getKey());
                    com.android.server.notification.NotificationManagerService.this.mInlineReplyRecordsByKey.remove(key);
                }
            }
        }

        @Override // com.android.server.notification.NotificationDelegate
        public void onNotificationFeedbackReceived(java.lang.String key, android.os.Bundle feedback) {
            com.android.server.notification.NotificationManagerService.this.exitIdle();
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                if (r == null) {
                    if (com.android.server.notification.NotificationManagerService.DBG) {
                        android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "No notification with key: " + key);
                    }
                } else {
                    com.android.server.notification.NotificationManagerService.this.mAssistants.notifyAssistantFeedbackReceived(r, feedback);
                }
            }
        }
    }

    void logSmartSuggestionsVisible(com.android.server.notification.NotificationRecord notificationRecord, int i) {
        if ((notificationRecord.getNumSmartRepliesAdded() > 0 || notificationRecord.getNumSmartActionsAdded() > 0) && !notificationRecord.hasSeenSmartReplies()) {
            notificationRecord.setSeenSmartReplies(true);
            this.mMetricsLogger.write(notificationRecord.getLogMaker().setCategory(1382).addTaggedData(1384, java.lang.Integer.valueOf(notificationRecord.getNumSmartRepliesAdded())).addTaggedData(android.hardware.audio.common.V2_0.AudioChannelMask.OUT_7POINT1, java.lang.Integer.valueOf(notificationRecord.getNumSmartActionsAdded())).addTaggedData(1600, java.lang.Integer.valueOf(notificationRecord.getSuggestionsGeneratedByAssistant() ? 1 : 0)).addTaggedData(1629, java.lang.Integer.valueOf(i)).addTaggedData(1647, java.lang.Integer.valueOf(notificationRecord.getEditChoicesBeforeSending() ? 1 : 0)));
            this.mNotificationRecordLogger.log(com.android.server.notification.NotificationRecordLogger.NotificationEvent.NOTIFICATION_SMART_REPLY_VISIBLE, notificationRecord);
        }
    }

    protected void logSensitiveAdjustmentReceived(boolean hasPosted, boolean hasSensitiveContent, int lifespanMs) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SENSITIVE_NOTIFICATION_REDACTION, hasPosted, hasSensitiveContent, lifespanMs);
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        private final android.net.Uri LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS;
        private final android.net.Uri LOCK_SCREEN_SHOW_NOTIFICATIONS;
        private final android.net.Uri NOTIFICATION_BADGING_URI;
        private final android.net.Uri NOTIFICATION_BUBBLES_URI;
        private final android.net.Uri NOTIFICATION_HISTORY_ENABLED;
        private final android.net.Uri NOTIFICATION_RATE_LIMIT_URI;
        private final android.net.Uri NOTIFICATION_SHOW_MEDIA_ON_QUICK_SETTINGS_URI;
        private final android.net.Uri SHOW_NOTIFICATION_SNOOZE;

        SettingsObserver(android.os.Handler handler) {
            super(handler);
            this.NOTIFICATION_BADGING_URI = android.provider.Settings.Secure.getUriFor("notification_badging");
            this.NOTIFICATION_BUBBLES_URI = android.provider.Settings.Secure.getUriFor("notification_bubbles");
            this.NOTIFICATION_RATE_LIMIT_URI = android.provider.Settings.Global.getUriFor("max_notification_enqueue_rate");
            this.NOTIFICATION_HISTORY_ENABLED = android.provider.Settings.Secure.getUriFor("notification_history_enabled");
            this.NOTIFICATION_SHOW_MEDIA_ON_QUICK_SETTINGS_URI = android.provider.Settings.Global.getUriFor("qs_media_controls");
            this.LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS = android.provider.Settings.Secure.getUriFor("lock_screen_allow_private_notifications");
            this.LOCK_SCREEN_SHOW_NOTIFICATIONS = android.provider.Settings.Secure.getUriFor("lock_screen_show_notifications");
            this.SHOW_NOTIFICATION_SNOOZE = android.provider.Settings.Secure.getUriFor("show_notification_snooze");
        }

        void observe() {
            android.content.ContentResolver resolver = com.android.server.notification.NotificationManagerService.this.getContext().getContentResolver();
            resolver.registerContentObserver(this.NOTIFICATION_BADGING_URI, false, this, -1);
            resolver.registerContentObserver(this.NOTIFICATION_RATE_LIMIT_URI, false, this, -1);
            resolver.registerContentObserver(this.NOTIFICATION_BUBBLES_URI, false, this, -1);
            resolver.registerContentObserver(this.NOTIFICATION_HISTORY_ENABLED, false, this, -1);
            resolver.registerContentObserver(this.NOTIFICATION_SHOW_MEDIA_ON_QUICK_SETTINGS_URI, false, this, -1);
            resolver.registerContentObserver(this.LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS, false, this, -1);
            resolver.registerContentObserver(this.LOCK_SCREEN_SHOW_NOTIFICATIONS, false, this, -1);
            resolver.registerContentObserver(this.SHOW_NOTIFICATION_SNOOZE, false, this, -1);
            update(null);
        }

        void destroy() {
            com.android.server.notification.NotificationManagerService.this.getContext().getContentResolver().unregisterContentObserver(this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            update(uri);
        }

        public void update(android.net.Uri uri) {
            android.content.ContentResolver resolver = com.android.server.notification.NotificationManagerService.this.getContext().getContentResolver();
            if (uri == null || this.NOTIFICATION_RATE_LIMIT_URI.equals(uri)) {
                com.android.server.notification.NotificationManagerService.this.mMaxPackageEnqueueRate = android.provider.Settings.Global.getFloat(resolver, "max_notification_enqueue_rate", com.android.server.notification.NotificationManagerService.this.mMaxPackageEnqueueRate);
            }
            if (uri == null || this.NOTIFICATION_BADGING_URI.equals(uri)) {
                com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.updateBadgingEnabled();
            }
            if (uri == null || this.NOTIFICATION_BUBBLES_URI.equals(uri)) {
                com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.updateBubblesEnabled();
            }
            if (uri == null || this.NOTIFICATION_HISTORY_ENABLED.equals(uri)) {
                for (android.content.pm.UserInfo userInfo : com.android.server.notification.NotificationManagerService.this.mUm.getUsers()) {
                    update(uri, userInfo.id);
                }
            }
            if (uri == null || this.NOTIFICATION_SHOW_MEDIA_ON_QUICK_SETTINGS_URI.equals(uri)) {
                com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.updateMediaNotificationFilteringEnabled();
            }
            if (uri == null || this.LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS.equals(uri)) {
                com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.updateLockScreenPrivateNotifications();
            }
            if (uri == null || this.LOCK_SCREEN_SHOW_NOTIFICATIONS.equals(uri)) {
                com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.updateLockScreenShowNotifications();
            }
            if (this.SHOW_NOTIFICATION_SNOOZE.equals(uri)) {
                boolean snoozeEnabled = android.provider.Settings.Secure.getIntForUser(resolver, "show_notification_snooze", 0, -2) != 0;
                if (!snoozeEnabled) {
                    com.android.server.notification.NotificationManagerService.this.unsnoozeAll();
                }
            }
        }

        public void update(android.net.Uri uri, int userId) {
            android.content.ContentResolver resolver = com.android.server.notification.NotificationManagerService.this.getContext().getContentResolver();
            if (uri == null || this.NOTIFICATION_HISTORY_ENABLED.equals(uri)) {
                com.android.server.notification.NotificationManagerService.this.mArchive.updateHistoryEnabled(userId, android.provider.Settings.Secure.getIntForUser(resolver, "notification_history_enabled", 0, userId) == 1);
            }
        }
    }

    protected class StrongAuthTracker extends com.android.internal.widget.LockPatternUtils.StrongAuthTracker {
        android.util.SparseBooleanArray mUserInLockDownMode;

        StrongAuthTracker(android.content.Context context) {
            super(context);
            this.mUserInLockDownMode = new android.util.SparseBooleanArray();
        }

        private boolean containsFlag(int haystack, int needle) {
            return (haystack & needle) != 0;
        }

        public boolean isInLockDownMode(int userId) {
            return this.mUserInLockDownMode.get(userId, false);
        }

        public synchronized void onStrongAuthRequiredChanged(int userId) {
            boolean userInLockDownModeNext = containsFlag(getStrongAuthForUser(userId), 32);
            if (userInLockDownModeNext == isInLockDownMode(userId)) {
                return;
            }
            if (userInLockDownModeNext) {
                com.android.server.notification.NotificationManagerService.this.cancelNotificationsWhenEnterLockDownMode(userId);
            }
            this.mUserInLockDownMode.put(userId, userInLockDownModeNext);
            if (!userInLockDownModeNext) {
                com.android.server.notification.NotificationManagerService.this.postNotificationsWhenExitLockDownMode(userId);
            }
        }
    }

    public NotificationManagerService(android.content.Context context) {
        this(context, new com.android.server.notification.NotificationRecordLoggerImpl(), new com.android.internal.logging.InstanceIdSequence(8192));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public NotificationManagerService(android.content.Context context, com.android.server.notification.NotificationRecordLogger notificationRecordLogger, com.android.internal.logging.InstanceIdSequence instanceIdSequence) {
        super(context);
        this.mForegroundToken = new android.os.Binder();
        this.mRankingThread = new android.os.HandlerThread("ranker", 10);
        this.mListenersDisablingEffects = new android.util.SparseArray<>();
        this.mEffectsSuppressors = new java.util.ArrayList();
        this.mInterruptionFilter = 0;
        this.mNotificationLock = new java.lang.Object();
        this.mNotificationList = new java.util.ArrayList<>();
        this.mNotificationsByKey = new android.util.ArrayMap<>();
        this.mInlineReplyRecordsByKey = new android.util.ArrayMap<>();
        this.mEnqueuedNotifications = new java.util.ArrayList<>();
        this.mAutobundledSummaries = new android.util.ArrayMap<>();
        this.mToastQueue = new java.util.ArrayList<>();
        this.mToastRateLimitingDisabledUids = new android.util.ArraySet();
        this.mSummaryByGroupKey = new android.util.ArrayMap<>();
        this.mIsCurrentToastShown = false;
        this.mUserProfiles = new com.android.server.notification.ManagedServices.UserProfiles();
        this.mLockScreenAllowSecureNotifications = true;
        this.mCallNotificationEventCallbacks = new android.util.ArrayMap<>();
        this.mMaxPackageEnqueueRate = DEFAULT_MAX_NOTIFICATION_ENQUEUE_RATE;
        this.mSavePolicyFile = new com.android.server.notification.NotificationManagerService.SavePolicyFileRunnable();
        this.mMsgPkgsAllowedAsConvos = new java.util.HashSet();
        this.mUIFirstManagerExt = (com.oplus.uifirst.IOplusUIFirstManagerExt) system.ext.loader.core.ExtLoader.type(com.oplus.uifirst.IOplusUIFirstManagerExt.class).create();
        this.mNotificationDelegate = new com.android.server.notification.NotificationManagerService.AnonymousClass1();
        this.mNotificationManagerPrivate = new com.android.server.notification.NotificationManagerPrivate() { // from class: com.android.server.notification.NotificationManagerService.2
            @Override // com.android.server.notification.NotificationManagerPrivate
            public com.android.server.notification.NotificationRecord getNotificationByKey(java.lang.String key) {
                com.android.server.notification.NotificationRecord notificationRecord;
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    notificationRecord = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                }
                return notificationRecord;
            }

            @Override // com.android.server.notification.NotificationManagerPrivate
            public void timeoutNotification(java.lang.String key) throws java.lang.Throwable {
                boolean foundNotification = false;
                int uid = 0;
                int pid = 0;
                java.lang.String packageName = null;
                java.lang.String tag = null;
                int id = 0;
                int userId = 0;
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    try {
                        try {
                            com.android.server.notification.NotificationRecord record = com.android.server.notification.NotificationManagerService.this.findNotificationByKeyLocked(key);
                            if (record != null) {
                                foundNotification = true;
                                uid = record.getUid();
                                pid = record.getSbn().getInitialPid();
                                packageName = record.getSbn().getPackageName();
                                tag = record.getSbn().getTag();
                                id = record.getSbn().getId();
                                userId = record.getUserId();
                            }
                            if (foundNotification) {
                                if (android.app.Flags.lifetimeExtensionRefactor()) {
                                    com.android.server.notification.NotificationManagerService.this.cancelNotification(uid, pid, packageName, tag, id, 0, 98368, true, userId, 19, null);
                                } else {
                                    com.android.server.notification.NotificationManagerService.this.cancelNotification(uid, pid, packageName, tag, id, 0, 32832, true, userId, 19, null);
                                }
                            }
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
            }
        };
        this.mLocaleChangeReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.notification.NotificationManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if ("android.intent.action.LOCALE_CHANGED".equals(intent.getAction())) {
                    com.android.internal.notification.SystemNotificationChannels.createAll(context2);
                    com.android.server.notification.NotificationManagerService.this.mZenModeHelper.updateZenRulesOnLocaleChange();
                    com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.onLocaleChanged(context2, android.app.ActivityManager.getCurrentUser());
                }
            }
        };
        this.mRestoreReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.notification.NotificationManagerService.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if ("android.os.action.SETTING_RESTORED".equals(intent.getAction())) {
                    try {
                        java.lang.String element = intent.getStringExtra("setting_name");
                        java.lang.String newValue = intent.getStringExtra("new_value");
                        int restoredFromSdkInt = intent.getIntExtra("restored_from_sdk_int", 0);
                        com.android.server.notification.NotificationManagerService.this.mListeners.onSettingRestored(element, newValue, restoredFromSdkInt, getSendingUserId());
                        com.android.server.notification.NotificationManagerService.this.mConditionProviders.onSettingRestored(element, newValue, restoredFromSdkInt, getSendingUserId());
                    } catch (java.lang.Exception e) {
                        android.util.Slog.wtf(com.android.server.notification.NotificationManagerService.TAG, "Cannot restore managed services from settings", e);
                    }
                }
            }
        };
        this.mNotificationTimeoutReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.notification.NotificationManagerService.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) throws java.lang.Throwable {
                java.lang.String action = intent.getAction();
                if (action != null && com.android.server.notification.NotificationManagerService.ACTION_NOTIFICATION_TIMEOUT.equals(action)) {
                    synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                        try {
                            try {
                                com.android.server.notification.NotificationRecord record = com.android.server.notification.NotificationManagerService.this.findNotificationByKeyLocked(intent.getStringExtra(com.android.server.notification.NotificationManagerService.EXTRA_KEY));
                                if (record != null) {
                                    if (android.app.Flags.lifetimeExtensionRefactor()) {
                                        com.android.server.notification.NotificationManagerService.this.cancelNotification(record.getSbn().getUid(), record.getSbn().getInitialPid(), record.getSbn().getPackageName(), record.getSbn().getTag(), record.getSbn().getId(), 0, 98368, true, record.getUserId(), 19, null);
                                    } else {
                                        com.android.server.notification.NotificationManagerService.this.cancelNotification(record.getSbn().getUid(), record.getSbn().getInitialPid(), record.getSbn().getPackageName(), record.getSbn().getTag(), record.getSbn().getId(), 0, 32832, true, record.getUserId(), 19, null);
                                    }
                                }
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    }
                }
            }
        };
        this.mPackageIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.notification.NotificationManagerService.6
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                boolean queryRemove;
                boolean packageChanged;
                boolean removingPackage;
                int i;
                java.lang.String pkgName;
                boolean cancelNotifications;
                java.lang.String[] pkgList;
                int[] uidList;
                boolean hideNotifications;
                boolean unhideNotifications;
                boolean cancelNotifications2;
                boolean cancelNotifications3;
                int[] uidList2;
                int[] uidList3;
                boolean removingPackage2;
                int changeUserId;
                int i2;
                int i3;
                int[] uidList4;
                boolean removingPackage3;
                int changeUserId2;
                java.lang.String action = intent.getAction();
                if (action == null) {
                    return;
                }
                boolean packageChanged2 = false;
                boolean hideNotifications2 = false;
                boolean unhideNotifications2 = false;
                if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                    queryRemove = false;
                    packageChanged = false;
                } else {
                    boolean queryRemove2 = action.equals("android.intent.action.PACKAGE_REMOVED");
                    if (!queryRemove2 && !action.equals("android.intent.action.PACKAGE_RESTARTED")) {
                        boolean zEquals = action.equals("android.intent.action.PACKAGE_CHANGED");
                        packageChanged2 = zEquals;
                        if (!zEquals && !action.equals("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE") && !action.equals("android.intent.action.PACKAGES_SUSPENDED") && !action.equals("android.intent.action.PACKAGES_UNSUSPENDED") && !action.equals("android.intent.action.DISTRACTING_PACKAGES_CHANGED")) {
                            return;
                        }
                    }
                    queryRemove = queryRemove2;
                    packageChanged = packageChanged2;
                }
                int changeUserId3 = intent.getIntExtra("android.intent.extra.user_handle", -1);
                boolean removingPackage4 = queryRemove && !intent.getBooleanExtra("android.intent.extra.REPLACING", false);
                if (queryRemove && intent.getBooleanExtra("android.intent.extra.OPLUS_HIDE", false) && intent.getBooleanExtra("android.intent.extra.DONT_KILL_APP", false)) {
                    return;
                }
                if (com.android.server.notification.NotificationManagerService.DBG) {
                    removingPackage = removingPackage4;
                    android.util.Slog.i(com.android.server.notification.NotificationManagerService.TAG, "action=" + action + " removing=" + removingPackage);
                } else {
                    removingPackage = removingPackage4;
                }
                boolean cancelNotifications4 = true;
                if (action.equals("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE")) {
                    pkgList = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                    uidList = intent.getIntArrayExtra("android.intent.extra.changed_uid_list");
                    hideNotifications = false;
                    unhideNotifications = false;
                    cancelNotifications2 = true;
                    i = 0;
                } else if (action.equals("android.intent.action.PACKAGES_SUSPENDED")) {
                    pkgList = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                    int[] uidList5 = intent.getIntArrayExtra("android.intent.extra.changed_uid_list");
                    uidList = uidList5;
                    cancelNotifications2 = false;
                    hideNotifications = true;
                    unhideNotifications = false;
                    i = 0;
                } else if (action.equals("android.intent.action.PACKAGES_UNSUSPENDED")) {
                    pkgList = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                    int[] uidList6 = intent.getIntArrayExtra("android.intent.extra.changed_uid_list");
                    uidList = uidList6;
                    cancelNotifications2 = false;
                    hideNotifications = false;
                    unhideNotifications = true;
                    i = 0;
                } else if (action.equals("android.intent.action.DISTRACTING_PACKAGES_CHANGED")) {
                    i = 0;
                    int distractionRestrictions = intent.getIntExtra("android.intent.extra.distraction_restrictions", 0);
                    if ((distractionRestrictions & 2) != 0) {
                        java.lang.String[] pkgList2 = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                        int[] uidList7 = intent.getIntArrayExtra("android.intent.extra.changed_uid_list");
                        cancelNotifications3 = false;
                        hideNotifications2 = true;
                        pkgList = pkgList2;
                        uidList2 = uidList7;
                    } else {
                        java.lang.String[] pkgList3 = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                        int[] uidList8 = intent.getIntArrayExtra("android.intent.extra.changed_uid_list");
                        cancelNotifications3 = false;
                        unhideNotifications2 = true;
                        pkgList = pkgList3;
                        uidList2 = uidList8;
                    }
                    uidList = uidList2;
                    hideNotifications = hideNotifications2;
                    unhideNotifications = unhideNotifications2;
                    cancelNotifications2 = cancelNotifications3;
                } else {
                    i = 0;
                    android.net.Uri uri = intent.getData();
                    if (uri == null || (pkgName = uri.getSchemeSpecificPart()) == null) {
                        return;
                    }
                    if (packageChanged) {
                        try {
                            int enabled = com.android.server.notification.NotificationManagerService.this.mPackageManager.getApplicationEnabledSetting(pkgName, changeUserId3 != -1 ? changeUserId3 : 0);
                            if (enabled != 1 && enabled != 0) {
                                cancelNotifications = true;
                            } else {
                                cancelNotifications = false;
                            }
                            cancelNotifications4 = cancelNotifications;
                        } catch (android.os.RemoteException e) {
                        } catch (java.lang.IllegalArgumentException e2) {
                            if (com.android.server.notification.NotificationManagerService.DBG) {
                                android.util.Slog.i(com.android.server.notification.NotificationManagerService.TAG, "Exception trying to look up app enabled setting", e2);
                            }
                        }
                    }
                    pkgList = new java.lang.String[]{pkgName};
                    uidList = new int[]{intent.getIntExtra("android.intent.extra.UID", -1)};
                    hideNotifications = false;
                    unhideNotifications = false;
                    cancelNotifications2 = cancelNotifications4;
                }
                if (pkgList == null || pkgList.length <= 0) {
                    uidList3 = uidList;
                    removingPackage2 = removingPackage;
                    changeUserId = changeUserId3;
                } else if (cancelNotifications2) {
                    int length = pkgList.length;
                    int i4 = i;
                    while (i4 < length) {
                        java.lang.String pkgName2 = pkgList[i4];
                        if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                            i2 = i4;
                            i3 = length;
                            uidList4 = uidList;
                            com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().cancelAllNotificationsInt(action, com.android.server.notification.NotificationManagerService.MY_UID, com.android.server.notification.NotificationManagerService.MY_PID, pkgName2, null, 0, 0, changeUserId3, 5);
                            removingPackage3 = removingPackage;
                            changeUserId2 = changeUserId3;
                        } else {
                            i2 = i4;
                            i3 = length;
                            uidList4 = uidList;
                            removingPackage3 = removingPackage;
                            changeUserId2 = changeUserId3;
                            com.android.server.notification.NotificationManagerService.this.cancelAllNotificationsInt(com.android.server.notification.NotificationManagerService.MY_UID, com.android.server.notification.NotificationManagerService.MY_PID, pkgName2, null, 0, 0, changeUserId2, 5);
                        }
                        i4 = i2 + 1;
                        uidList = uidList4;
                        changeUserId3 = changeUserId2;
                        removingPackage = removingPackage3;
                        length = i3;
                    }
                    uidList3 = uidList;
                    removingPackage2 = removingPackage;
                    changeUserId = changeUserId3;
                } else {
                    uidList3 = uidList;
                    removingPackage2 = removingPackage;
                    changeUserId = changeUserId3;
                    if (hideNotifications && uidList3 != null && uidList3.length > 0) {
                        com.android.server.notification.NotificationManagerService.this.hideNotificationsForPackages(pkgList, uidList3);
                    } else if (unhideNotifications && uidList3 != null && uidList3.length > 0) {
                        com.android.server.notification.NotificationManagerService.this.unhideNotificationsForPackages(pkgList, uidList3);
                    }
                }
                com.android.server.notification.NotificationManagerService.this.mHandler.scheduleOnPackageChanged(removingPackage2, changeUserId, pkgList, uidList3);
            }
        };
        this.mIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.notification.NotificationManagerService.7
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                java.lang.String action = intent.getAction();
                if (action.equals("android.intent.action.USER_STOPPED")) {
                    int userHandle = intent.getIntExtra("android.intent.extra.user_handle", -1);
                    if (userHandle >= 0) {
                        com.android.server.notification.NotificationManagerService.this.cancelAllNotificationsInt(com.android.server.notification.NotificationManagerService.MY_UID, com.android.server.notification.NotificationManagerService.MY_PID, null, null, 0, 0, userHandle, 6);
                        return;
                    }
                    return;
                }
                if (isProfileUnavailable(action)) {
                    int userHandle2 = intent.getIntExtra("android.intent.extra.user_handle", -1);
                    if (userHandle2 >= 0) {
                        com.android.server.notification.NotificationManagerService.this.cancelAllNotificationsInt(com.android.server.notification.NotificationManagerService.MY_UID, com.android.server.notification.NotificationManagerService.MY_PID, null, null, 0, 0, userHandle2, 15);
                        com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.clearData(userHandle2);
                        return;
                    }
                    return;
                }
                if (action.equals("android.intent.action.USER_SWITCHED")) {
                    if (!com.android.server.notification.Flags.useSsmUserSwitchSignal()) {
                        int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                        com.android.server.notification.NotificationManagerService.this.mUserProfiles.updateCache(context2);
                        if (!com.android.server.notification.NotificationManagerService.this.mUserProfiles.isProfileUser(userId, context2)) {
                            com.android.server.notification.NotificationManagerService.this.mSettingsObserver.update(null);
                            com.android.server.notification.NotificationManagerService.this.mConditionProviders.onUserSwitched(userId);
                            com.android.server.notification.NotificationManagerService.this.mListeners.onUserSwitched(userId);
                            com.android.server.notification.NotificationManagerService.this.mZenModeHelper.onUserSwitched(userId);
                            com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.syncChannelsBypassingDnd();
                        }
                        com.android.server.notification.NotificationManagerService.this.mAssistants.onUserSwitched(userId);
                        return;
                    }
                    return;
                }
                if (action.equals("android.intent.action.USER_ADDED")) {
                    int userId2 = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                    if (userId2 != -10000) {
                        com.android.server.notification.NotificationManagerService.this.mUserProfiles.updateCache(context2);
                        if (!com.android.server.notification.NotificationManagerService.this.mUserProfiles.isProfileUser(userId2, context2)) {
                            com.android.server.notification.NotificationManagerService.this.allowDefaultApprovedServices(userId2);
                        }
                        com.android.server.notification.NotificationManagerService.this.mHistoryManager.onUserAdded(userId2);
                        com.android.server.notification.NotificationManagerService.this.mSettingsObserver.update(null, userId2);
                        return;
                    }
                    return;
                }
                if (action.equals("android.intent.action.USER_REMOVED")) {
                    int userId3 = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                    com.android.server.notification.NotificationManagerService.this.mUserProfiles.updateCache(context2);
                    com.android.server.notification.NotificationManagerService.this.mZenModeHelper.onUserRemoved(userId3);
                    com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.onUserRemoved(userId3);
                    com.android.server.notification.NotificationManagerService.this.mListeners.onUserRemoved(userId3);
                    com.android.server.notification.NotificationManagerService.this.mConditionProviders.onUserRemoved(userId3);
                    com.android.server.notification.NotificationManagerService.this.mAssistants.onUserRemoved(userId3);
                    com.android.server.notification.NotificationManagerService.this.mHistoryManager.onUserRemoved(userId3);
                    com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.syncChannelsBypassingDnd();
                    com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
                    return;
                }
                if (action.equals("android.intent.action.USER_UNLOCKED")) {
                    int userId4 = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                    com.android.server.notification.NotificationManagerService.this.mUserProfiles.updateCache(context2);
                    com.android.server.notification.NotificationManagerService.this.mAssistants.onUserUnlocked(userId4);
                    if (!com.android.server.notification.NotificationManagerService.this.mUserProfiles.isProfileUser(userId4, context2)) {
                        com.android.server.notification.NotificationManagerService.this.mConditionProviders.onUserUnlocked(userId4);
                        com.android.server.notification.NotificationManagerService.this.mListeners.onUserUnlocked(userId4);
                        if (!android.app.Flags.modesApi()) {
                            com.android.server.notification.NotificationManagerService.this.mZenModeHelper.onUserUnlocked(userId4);
                        }
                    }
                }
            }

            private boolean isProfileUnavailable(java.lang.String action) {
                if (com.android.server.notification.NotificationManagerService.privateSpaceFlagsEnabled()) {
                    return action.equals("android.intent.action.PROFILE_UNAVAILABLE");
                }
                return action.equals("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
            }
        };
        this.mService = new com.android.server.notification.NotificationManagerService.AnonymousClass12();
        this.mInternalService = new com.android.server.notification.NotificationManagerService.AnonymousClass13();
        this.mShortcutListener = new com.android.server.notification.ShortcutHelper.ShortcutListener() { // from class: com.android.server.notification.NotificationManagerService.14
            @Override // com.android.server.notification.ShortcutHelper.ShortcutListener
            public void onShortcutRemoved(java.lang.String key) {
                java.lang.String packageName;
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                    packageName = r != null ? r.getSbn().getPackageName() : null;
                }
                int packageImportance = com.android.server.notification.NotificationManagerService.this.getPackageImportanceWithIdentity(packageName);
                boolean isAppForeground = packageName != null && packageImportance == 100;
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.NotificationRecord r2 = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(key);
                    if (r2 != null) {
                        r2.setShortcutInfo(null);
                        r2.getNotification().flags |= 8;
                        com.android.server.notification.NotificationManagerService.this.mHandler.post(com.android.server.notification.NotificationManagerService.this.new EnqueueNotificationRunnable(r2.getUser().getIdentifier(), r2, isAppForeground, com.android.server.notification.NotificationManagerService.this.mPostNotificationTrackerFactory.newTracker(null)));
                    }
                }
            }
        };
        this.mNotificationVibrationIntensity = 1800;
        this.mNMSWrapper = new com.android.server.notification.NotificationManagerService.NotificationManagerServiceWrapper();
        this.mZenModeManagerExt = (com.android.server.zenmode.IZenModeManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.zenmode.IZenModeManagerExt.class).create();
        this.mNotificationRecordLogger = notificationRecordLogger;
        this.mNotificationInstanceIdSequence = instanceIdSequence;
        android.app.Notification.processAllowlistToken = ALLOWLIST_TOKEN;
        if (this.mNMSWrapper.getNMSExt() != null) {
            this.mNMSWrapper.getNMSExt().init(this, context, this.mNotificationRecordLogger, this.mNotificationInstanceIdSequence);
        }
    }

    void setStrongAuthTracker(com.android.server.notification.NotificationManagerService.StrongAuthTracker strongAuthTracker) {
        this.mStrongAuthTracker = strongAuthTracker;
    }

    void setLockPatternUtils(com.android.internal.widget.LockPatternUtils lockUtils) {
        this.mLockUtils = lockUtils;
    }

    com.android.server.notification.ShortcutHelper getShortcutHelper() {
        return this.mShortcutHelper;
    }

    void setShortcutHelper(com.android.server.notification.ShortcutHelper helper) {
        this.mShortcutHelper = helper;
    }

    int getNotificationRecordCount() {
        int count;
        synchronized (this.mNotificationLock) {
            count = this.mNotificationList.size() + this.mNotificationsByKey.size() + this.mSummaryByGroupKey.size() + this.mEnqueuedNotifications.size();
            for (com.android.server.notification.NotificationRecord posted : this.mNotificationList) {
                if (this.mNotificationsByKey.containsKey(posted.getKey())) {
                    count--;
                }
                if (posted.getSbn().isGroup() && posted.getNotification().isGroupSummary()) {
                    count--;
                }
            }
        }
        return count;
    }

    void clearNotifications() {
        synchronized (this.mNotificationLock) {
            this.mEnqueuedNotifications.clear();
            this.mNotificationList.clear();
            this.mNotificationsByKey.clear();
            this.mSummaryByGroupKey.clear();
        }
    }

    void addNotification(com.android.server.notification.NotificationRecord r) {
        synchronized (this.mNotificationLock) {
            this.mNotificationList.add(r);
            this.mNotificationsByKey.put(r.getSbn().getKey(), r);
            if (r.getSbn().isGroup()) {
                this.mSummaryByGroupKey.put(r.getGroupKey(), r);
            }
        }
    }

    void addEnqueuedNotification(com.android.server.notification.NotificationRecord r) {
        synchronized (this.mNotificationLock) {
            this.mEnqueuedNotifications.add(r);
        }
    }

    com.android.server.notification.NotificationRecord getNotificationRecord(java.lang.String key) {
        com.android.server.notification.NotificationRecord notificationRecord;
        synchronized (this.mNotificationLock) {
            notificationRecord = this.mNotificationsByKey.get(key);
        }
        return notificationRecord;
    }

    void setHandler(com.android.server.notification.NotificationManagerService.WorkerHandler handler) {
        this.mHandler = handler;
    }

    void setRankingHelper(com.android.server.notification.RankingHelper rankingHelper) {
        this.mRankingHelper = rankingHelper;
    }

    void setPreferencesHelper(com.android.server.notification.PreferencesHelper prefHelper) {
        this.mPreferencesHelper = prefHelper;
    }

    void setZenHelper(com.android.server.notification.ZenModeHelper zenHelper) {
        this.mZenModeHelper = zenHelper;
    }

    void setAttentionHelper(com.android.server.notification.NotificationAttentionHelper nah) {
        this.mAttentionHelper = nah;
    }

    void setIsTelevision(boolean isTelevision) {
        this.mIsTelevision = isTelevision;
    }

    void setTelecomManager(android.telecom.TelecomManager tm) {
        this.mTelecomManager = tm;
    }

    void init(com.android.server.notification.NotificationManagerService.WorkerHandler handler, com.android.server.notification.RankingHandler rankingHandler, android.content.pm.IPackageManager packageManager, android.content.pm.PackageManager packageManagerClient, com.android.server.lights.LightsManager lightsManager, com.android.server.notification.NotificationManagerService.NotificationListeners notificationListeners, com.android.server.notification.NotificationManagerService.NotificationAssistants notificationAssistants, com.android.server.notification.ConditionProviders conditionProviders, android.companion.ICompanionDeviceManager companionManager, com.android.server.notification.SnoozeHelper snoozeHelper, com.android.server.notification.NotificationUsageStats usageStats, android.util.AtomicFile policyFile, android.app.ActivityManager activityManager, com.android.server.notification.GroupHelper groupHelper, android.app.IActivityManager am, com.android.server.wm.ActivityTaskManagerInternal atm, android.app.usage.UsageStatsManagerInternal appUsageStats, android.app.admin.DevicePolicyManagerInternal dpm, android.app.IUriGrantsManager ugm, com.android.server.uri.UriGrantsManagerInternal ugmInternal, android.app.AppOpsManager appOps, android.os.UserManager userManager, com.android.server.notification.NotificationHistoryManager historyManager, android.app.StatsManager statsManager, android.app.ActivityManagerInternal ami, com.android.server.utils.quota.MultiRateLimiter toastRateLimiter, com.android.server.notification.PermissionHelper permissionHelper, android.app.usage.UsageStatsManagerInternal usageStatsManagerInternal, android.telecom.TelecomManager telecomManager, com.android.server.notification.NotificationChannelLogger channelLogger, com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.FlagResolver flagResolver, android.permission.PermissionManager permissionManager, android.os.PowerManager powerManager, com.android.server.notification.NotificationManagerService.PostNotificationTrackerFactory postNotificationTrackerFactory) {
        java.lang.String[] extractorNames;
        this.mHandler = handler;
        android.content.res.Resources resources = getContext().getResources();
        this.mMaxPackageEnqueueRate = android.provider.Settings.Global.getFloat(getContext().getContentResolver(), "max_notification_enqueue_rate", DEFAULT_MAX_NOTIFICATION_ENQUEUE_RATE);
        this.mAccessibilityManager = (android.view.accessibility.AccessibilityManager) getContext().getSystemService("accessibility");
        this.mAm = am;
        this.mAtm = atm;
        this.mAtm.setBackgroundActivityStartCallback(new com.android.server.notification.NotificationManagerService.NotificationTrampolineCallback());
        this.mUgm = ugm;
        this.mUgmInternal = ugmInternal;
        this.mPackageManager = packageManager;
        this.mPackageManagerClient = packageManagerClient;
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mPermissionManager = permissionManager;
        this.mPermissionPolicyInternal = (com.android.server.policy.PermissionPolicyInternal) com.android.server.LocalServices.getService(com.android.server.policy.PermissionPolicyInternal.class);
        this.mUmInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        this.mUsageStatsManagerInternal = usageStatsManagerInternal;
        this.mAppOps = appOps;
        this.mAppUsageStats = appUsageStats;
        this.mAlarmManager = (android.app.AlarmManager) getContext().getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        this.mCompanionManager = companionManager;
        this.mActivityManager = activityManager;
        this.mAmi = ami;
        this.mDeviceIdleManager = (android.os.DeviceIdleManager) getContext().getSystemService(android.os.DeviceIdleManager.class);
        this.mDpm = dpm;
        this.mUm = userManager;
        this.mTelecomManager = telecomManager;
        this.mPowerManager = powerManager;
        this.mPostNotificationTrackerFactory = postNotificationTrackerFactory;
        this.mPlatformCompat = com.android.internal.compat.IPlatformCompat.Stub.asInterface(android.os.ServiceManager.getService("platform_compat"));
        this.mStrongAuthTracker = new com.android.server.notification.NotificationManagerService.StrongAuthTracker(getContext());
        try {
            extractorNames = resources.getStringArray(android.R.array.config_notificationDefaultUnsupportedAdjustments);
        } catch (android.content.res.Resources.NotFoundException e) {
            extractorNames = new java.lang.String[0];
        }
        this.mUsageStats = usageStats;
        this.mMetricsLogger = new com.android.internal.logging.MetricsLogger();
        this.mRankingHandler = rankingHandler;
        this.mConditionProviders = conditionProviders;
        this.mZenModeHelper = new com.android.server.notification.ZenModeHelper(getContext(), this.mHandler.getLooper(), java.time.Clock.systemUTC(), this.mConditionProviders, flagResolver, new com.android.server.notification.ZenModeEventLogger(this.mPackageManagerClient));
        this.mZenModeHelper.addCallback(new com.android.server.notification.NotificationManagerService.AnonymousClass8());
        this.mPermissionHelper = permissionHelper;
        this.mNotificationChannelLogger = channelLogger;
        this.mUserProfiles.updateCache(getContext());
        this.mPreferencesHelper = new com.android.server.notification.PreferencesHelper(getContext(), this.mPackageManagerClient, this.mRankingHandler, this.mZenModeHelper, this.mPermissionHelper, this.mPermissionManager, this.mNotificationChannelLogger, this.mAppOps, this.mUserProfiles, this.mShowReviewPermissionsNotification, java.time.Clock.systemUTC());
        this.mRankingHelper = new com.android.server.notification.RankingHelper(getContext(), this.mRankingHandler, this.mPreferencesHelper, this.mZenModeHelper, this.mUsageStats, extractorNames, this.mPlatformCompat);
        this.mSnoozeHelper = snoozeHelper;
        this.mGroupHelper = groupHelper;
        this.mHistoryManager = historyManager;
        if (com.android.server.notification.Flags.allNotifsNeedTtl()) {
            this.mTtlHelper = new com.android.server.notification.TimeToLiveHelper(this.mNotificationManagerPrivate, getContext());
        }
        this.mListeners = notificationListeners;
        this.mAssistants = notificationAssistants;
        this.mAllowedManagedServicePackages = new com.android.internal.util.function.TriPredicate() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda3
            public final boolean test(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                return this.f$0.canUseManagedServices((java.lang.String) obj, (java.lang.Integer) obj2, (java.lang.String) obj3);
            }
        };
        this.mPolicyFile = policyFile;
        loadPolicyFile();
        this.mStatusBar = (com.android.server.statusbar.StatusBarManagerInternal) getLocalService(com.android.server.statusbar.StatusBarManagerInternal.class);
        if (this.mStatusBar != null) {
            this.mStatusBar.setNotificationDelegate(this.mNotificationDelegate);
        }
        this.mZenModeHelper.initZenMode();
        this.mInterruptionFilter = this.mZenModeHelper.getZenModeListenerInterruptionFilter();
        this.mSettingsObserver = new com.android.server.notification.NotificationManagerService.SettingsObserver(this.mHandler);
        this.mArchive = new com.android.server.notification.NotificationManagerService.Archive(resources.getInteger(android.R.integer.config_motionPredictionOffsetNanos));
        this.mIsTelevision = this.mPackageManagerClient.hasSystemFeature("android.software.leanback") || this.mPackageManagerClient.hasSystemFeature("android.hardware.type.television");
        this.mZenModeHelper.setPriorityOnlyDndExemptPackages(getContext().getResources().getStringArray(android.R.array.config_openDeviceStates));
        this.mWarnRemoteViewsSizeBytes = getContext().getResources().getInteger(android.R.integer.config_multiuserMaxRunningUsers);
        this.mStripRemoteViewsSizeBytes = getContext().getResources().getInteger(android.R.integer.config_mt_sms_polling_throttle_millis);
        this.mMsgPkgsAllowedAsConvos = java.util.Set.of((java.lang.Object[]) getStringArrayResource(android.R.array.config_nonPreemptibleInputMethods));
        this.mDefaultSearchSelectorPkg = getContext().getString(getContext().getResources().getIdentifier("config_defaultSearchSelectorPackageName", "string", com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME));
        this.mFlagResolver = flagResolver;
        this.mStatsManager = statsManager;
        this.mToastRateLimiter = toastRateLimiter;
        this.mAttentionHelper = new com.android.server.notification.NotificationAttentionHelper(getContext(), lightsManager, this.mAccessibilityManager, this.mPackageManagerClient, userManager, usageStats, this.mNotificationManagerPrivate, this.mZenModeHelper, flagResolver, this.mNMSWrapper, this.mPreferencesHelper);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.USER_STOPPED");
        if (!com.android.server.notification.Flags.useSsmUserSwitchSignal()) {
            filter.addAction("android.intent.action.USER_SWITCHED");
        }
        filter.addAction("android.intent.action.USER_ADDED");
        filter.addAction("android.intent.action.USER_REMOVED");
        filter.addAction("android.intent.action.USER_UNLOCKED");
        filter.addAction("android.intent.action.TIME_SET");
        filter.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        if (privateSpaceFlagsEnabled()) {
            filter.addAction("android.intent.action.PROFILE_UNAVAILABLE");
        }
        getContext().registerReceiverAsUser(this.mIntentReceiver, android.os.UserHandle.ALL, filter, null, null);
        android.content.IntentFilter pkgFilter = new android.content.IntentFilter();
        pkgFilter.addAction("android.intent.action.PACKAGE_ADDED");
        pkgFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        pkgFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        pkgFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=IGNORE_WM_COMP");
        pkgFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        pkgFilter.addDataScheme("package");
        getContext().registerReceiverAsUser(this.mPackageIntentReceiver, android.os.UserHandle.ALL, pkgFilter, null, null);
        android.content.IntentFilter suspendedPkgFilter = new android.content.IntentFilter();
        suspendedPkgFilter.addAction("android.intent.action.PACKAGES_SUSPENDED");
        suspendedPkgFilter.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
        suspendedPkgFilter.addAction("android.intent.action.DISTRACTING_PACKAGES_CHANGED");
        getContext().registerReceiverAsUser(this.mPackageIntentReceiver, android.os.UserHandle.ALL, suspendedPkgFilter, null, null);
        android.content.IntentFilter sdFilter = new android.content.IntentFilter("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
        getContext().registerReceiverAsUser(this.mPackageIntentReceiver, android.os.UserHandle.ALL, sdFilter, null, null);
        if (!com.android.server.notification.Flags.allNotifsNeedTtl()) {
            android.content.IntentFilter timeoutFilter = new android.content.IntentFilter(ACTION_NOTIFICATION_TIMEOUT);
            timeoutFilter.addDataScheme(SCHEME_TIMEOUT);
            getContext().registerReceiver(this.mNotificationTimeoutReceiver, timeoutFilter, 2);
        }
        android.content.IntentFilter settingsRestoredFilter = new android.content.IntentFilter("android.os.action.SETTING_RESTORED");
        getContext().registerReceiver(this.mRestoreReceiver, settingsRestoredFilter);
        android.content.IntentFilter localeChangedFilter = new android.content.IntentFilter("android.intent.action.LOCALE_CHANGED");
        getContext().registerReceiver(this.mLocaleChangeReceiver, localeChangedFilter);
        this.mReviewNotificationPermissionsReceiver = new com.android.server.notification.ReviewNotificationPermissionsReceiver();
        getContext().registerReceiver(this.mReviewNotificationPermissionsReceiver, com.android.server.notification.ReviewNotificationPermissionsReceiver.getFilter(), 4);
        this.mAppOpsListener = new com.android.server.notification.NotificationManagerService.AnonymousClass9();
        this.mAppOps.startWatchingMode(11, (java.lang.String) null, this.mAppOpsListener);
    }

    /* JADX INFO: renamed from: com.android.server.notification.NotificationManagerService$8, reason: invalid class name */
    class AnonymousClass8 extends com.android.server.notification.ZenModeHelper.Callback {
        AnonymousClass8() {
        }

        @Override // com.android.server.notification.ZenModeHelper.Callback
        public void onConfigChanged() {
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
        }

        @Override // com.android.server.notification.ZenModeHelper.Callback
        void onZenModeChanged() {
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$8$$ExternalSyntheticLambda2
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$onZenModeChanged$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onZenModeChanged$0() throws java.lang.Exception {
            com.android.server.notification.NotificationManagerService.this.sendRegisteredOnlyBroadcast("android.app.action.INTERRUPTION_FILTER_CHANGED");
            com.android.server.notification.NotificationManagerService.this.getContext().sendBroadcastAsUser(new android.content.Intent("android.app.action.INTERRUPTION_FILTER_CHANGED_INTERNAL").addFlags(67108864), android.os.UserHandle.ALL, "android.permission.MANAGE_NOTIFICATIONS");
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationManagerService.this.updateInterruptionFilterLocked();
            }
            com.android.server.notification.NotificationManagerService.this.mRankingHandler.requestSort();
        }

        @Override // com.android.server.notification.ZenModeHelper.Callback
        void onPolicyChanged(final android.app.NotificationManager.Policy newPolicy) {
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$8$$ExternalSyntheticLambda3
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$onPolicyChanged$1(newPolicy);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPolicyChanged$1(android.app.NotificationManager.Policy newPolicy) throws java.lang.Exception {
            android.content.Intent intent = new android.content.Intent("android.app.action.NOTIFICATION_POLICY_CHANGED");
            if (android.app.Flags.modesApi()) {
                intent.putExtra("android.app.extra.NOTIFICATION_POLICY", newPolicy);
            }
            com.android.server.notification.NotificationManagerService.this.sendRegisteredOnlyBroadcast(intent);
            com.android.server.notification.NotificationManagerService.this.mRankingHandler.requestSort();
        }

        @Override // com.android.server.notification.ZenModeHelper.Callback
        void onConsolidatedPolicyChanged(final android.app.NotificationManager.Policy newConsolidatedPolicy) {
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$8$$ExternalSyntheticLambda0
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$onConsolidatedPolicyChanged$2(newConsolidatedPolicy);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onConsolidatedPolicyChanged$2(android.app.NotificationManager.Policy newConsolidatedPolicy) throws java.lang.Exception {
            if (android.app.Flags.modesApi()) {
                android.content.Intent intent = new android.content.Intent("android.app.action.CONSOLIDATED_NOTIFICATION_POLICY_CHANGED");
                intent.putExtra("android.app.extra.NOTIFICATION_POLICY", newConsolidatedPolicy);
                com.android.server.notification.NotificationManagerService.this.sendRegisteredOnlyBroadcast(intent);
            }
            com.android.server.notification.NotificationManagerService.this.mRankingHandler.requestSort();
        }

        @Override // com.android.server.notification.ZenModeHelper.Callback
        void onAutomaticRuleStatusChanged(final int userId, final java.lang.String pkg, final java.lang.String id, final int status) {
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$8$$ExternalSyntheticLambda1
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$onAutomaticRuleStatusChanged$3(pkg, id, status, userId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAutomaticRuleStatusChanged$3(java.lang.String pkg, java.lang.String id, int status, int userId) throws java.lang.Exception {
            android.content.Intent intent = new android.content.Intent("android.app.action.AUTOMATIC_ZEN_RULE_STATUS_CHANGED");
            intent.setPackage(pkg);
            intent.putExtra("android.app.extra.AUTOMATIC_ZEN_RULE_ID", id);
            intent.putExtra("android.app.extra.AUTOMATIC_ZEN_RULE_STATUS", status);
            com.android.server.notification.NotificationManagerService.this.getContext().sendBroadcastAsUser(intent, android.os.UserHandle.of(userId));
        }
    }

    /* JADX INFO: renamed from: com.android.server.notification.NotificationManagerService$9, reason: invalid class name */
    class AnonymousClass9 extends android.app.AppOpsManager.OnOpChangedInternalListener {
        AnonymousClass9() {
        }

        public void onOpChanged(java.lang.String op, final java.lang.String packageName, final int userId) {
            com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$9$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onOpChanged$0(packageName, userId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onOpChanged$0(java.lang.String packageName, int userId) {
            com.android.server.notification.NotificationManagerService.this.handleNotificationPermissionChange(packageName, userId);
        }
    }

    public void onDestroy() {
        if (this.mIntentReceiver != null) {
            getContext().unregisterReceiver(this.mIntentReceiver);
        }
        if (this.mPackageIntentReceiver != null) {
            getContext().unregisterReceiver(this.mPackageIntentReceiver);
        }
        if (com.android.server.notification.Flags.allNotifsNeedTtl()) {
            if (this.mTtlHelper != null) {
                this.mTtlHelper.destroy();
            }
        } else if (this.mNotificationTimeoutReceiver != null) {
            getContext().unregisterReceiver(this.mNotificationTimeoutReceiver);
        }
        if (this.mRestoreReceiver != null) {
            getContext().unregisterReceiver(this.mRestoreReceiver);
        }
        if (this.mLocaleChangeReceiver != null) {
            getContext().unregisterReceiver(this.mLocaleChangeReceiver);
        }
        if (this.mSettingsObserver != null) {
            this.mSettingsObserver.destroy();
        }
        if (this.mRoleObserver != null) {
            this.mRoleObserver.destroy();
        }
        if (this.mShortcutHelper != null) {
            this.mShortcutHelper.destroy();
        }
        if (this.mStatsManager != null) {
            this.mStatsManager.clearPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_PREFERENCES);
            this.mStatsManager.clearPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_CHANNEL_PREFERENCES);
            this.mStatsManager.clearPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_CHANNEL_GROUP_PREFERENCES);
            this.mStatsManager.clearPullAtomCallback(com.android.internal.util.FrameworkStatsLog.DND_MODE_RULE);
        }
        if (this.mAppOps != null) {
            this.mAppOps.stopWatchingMode(this.mAppOpsListener);
        }
        if (this.mAlarmManager != null) {
            this.mAlarmManager.cancelAll();
        }
    }

    protected java.lang.String[] getStringArrayResource(int key) {
        return getContext().getResources().getStringArray(key);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        com.android.server.notification.SnoozeHelper snoozeHelper = new com.android.server.notification.SnoozeHelper(getContext(), new com.android.server.notification.SnoozeHelper.Callback() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda10
            @Override // com.android.server.notification.SnoozeHelper.Callback
            public final void repost(int i, com.android.server.notification.NotificationRecord notificationRecord, boolean z) {
                this.f$0.lambda$onStart$0(i, notificationRecord, z);
            }
        }, this.mUserProfiles);
        java.io.File systemDir = new java.io.File(android.os.Environment.getDataDirectory(), "system");
        this.mRankingThread.start();
        com.android.server.notification.NotificationManagerService.WorkerHandler handler = new com.android.server.notification.NotificationManagerService.WorkerHandler(android.os.Looper.myLooper());
        this.mShowReviewPermissionsNotification = getContext().getResources().getBoolean(android.R.bool.config_magnification_area);
        com.android.server.notification.NotificationManagerService.RankingHandlerWorker rankingHandlerWorker = new com.android.server.notification.NotificationManagerService.RankingHandlerWorker(this.mRankingThread.getLooper());
        android.content.pm.IPackageManager packageManager = android.app.AppGlobals.getPackageManager();
        android.content.pm.PackageManager packageManager2 = getContext().getPackageManager();
        com.android.server.lights.LightsManager lightsManager = (com.android.server.lights.LightsManager) getLocalService(com.android.server.lights.LightsManager.class);
        com.android.server.notification.NotificationManagerService.NotificationListeners notificationListeners = new com.android.server.notification.NotificationManagerService.NotificationListeners(this, getContext(), this.mNotificationLock, this.mUserProfiles, android.app.AppGlobals.getPackageManager());
        com.android.server.notification.NotificationManagerService.NotificationAssistants notificationAssistants = new com.android.server.notification.NotificationManagerService.NotificationAssistants(getContext(), this.mNotificationLock, this.mUserProfiles, android.app.AppGlobals.getPackageManager());
        com.android.server.notification.ConditionProviders conditionProviders = new com.android.server.notification.ConditionProviders(getContext(), this.mUserProfiles, android.app.AppGlobals.getPackageManager());
        com.android.server.notification.NotificationUsageStats notificationUsageStats = new com.android.server.notification.NotificationUsageStats(getContext());
        android.util.AtomicFile atomicFile = new android.util.AtomicFile(new java.io.File(systemDir, "notification_policy.xml"), TAG_NOTIFICATION_POLICY);
        android.app.ActivityManager activityManager = (android.app.ActivityManager) getContext().getSystemService(com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY);
        com.android.server.notification.GroupHelper groupHelper = getGroupHelper();
        android.app.IActivityManager service = android.app.ActivityManager.getService();
        com.android.server.wm.ActivityTaskManagerInternal activityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        android.app.usage.UsageStatsManagerInternal usageStatsManagerInternal = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
        android.app.admin.DevicePolicyManagerInternal devicePolicyManagerInternal = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        android.app.IUriGrantsManager service2 = android.app.UriGrantsManager.getService();
        com.android.server.uri.UriGrantsManagerInternal uriGrantsManagerInternal = (com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class);
        android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) getContext().getSystemService(android.app.AppOpsManager.class);
        android.os.UserManager userManager = (android.os.UserManager) getContext().getSystemService(android.os.UserManager.class);
        com.android.server.notification.NotificationHistoryManager notificationHistoryManager = new com.android.server.notification.NotificationHistoryManager(getContext(), handler);
        android.app.StatsManager statsManager = (android.app.StatsManager) getContext().getSystemService("stats");
        this.mStatsManager = statsManager;
        init(handler, rankingHandlerWorker, packageManager, packageManager2, lightsManager, notificationListeners, notificationAssistants, conditionProviders, null, snoozeHelper, notificationUsageStats, atomicFile, activityManager, groupHelper, service, activityTaskManagerInternal, usageStatsManagerInternal, devicePolicyManagerInternal, service2, uriGrantsManagerInternal, appOpsManager, userManager, notificationHistoryManager, statsManager, (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class), createToastRateLimiter(), new com.android.server.notification.PermissionHelper(getContext(), android.app.AppGlobals.getPackageManager(), android.app.AppGlobals.getPermissionManager()), (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class), (android.telecom.TelecomManager) getContext().getSystemService(android.telecom.TelecomManager.class), new com.android.server.notification.NotificationChannelLoggerImpl(), com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.getResolver(), (android.permission.PermissionManager) getContext().getSystemService(android.permission.PermissionManager.class), (android.os.PowerManager) getContext().getSystemService(android.os.PowerManager.class), new com.android.server.notification.NotificationManagerService.PostNotificationTrackerFactory() { // from class: com.android.server.notification.NotificationManagerService.10
        });
        publishBinderService("notification", this.mService, false, 5);
        publishLocalService(com.android.server.notification.NotificationManagerInternal.class, this.mInternalService);
        if (this.mNMSWrapper.getNMSExt() != null) {
            this.mNMSWrapper.getNMSExt().onStart();
            this.mNMSWrapper.getNMSExt().initGroupType(this.mHandler, this.mNotificationLock, this.mGroupHelper, this.mNotificationList, this.mAutobundledSummaries);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$0(int userId, com.android.server.notification.NotificationRecord r, boolean muteOnReturn) {
        try {
            if (DBG) {
                try {
                    android.util.Slog.d(TAG, "Reposting " + r.getKey() + " " + muteOnReturn);
                } catch (java.lang.Exception e) {
                    e = e;
                    android.util.Slog.e(TAG, "Cannot un-snooze notification", e);
                    return;
                }
            }
            enqueueNotificationInternal(r.getSbn().getPackageName(), r.getSbn().getOpPkg(), r.getSbn().getUid(), r.getSbn().getInitialPid(), r.getSbn().getTag(), r.getSbn().getId(), r.getSbn().getNotification(), userId, muteOnReturn, false);
        } catch (java.lang.Exception e2) {
            e = e2;
        }
    }

    private void registerNotificationPreferencesPullers() {
        this.mPullAtomCallback = new com.android.server.notification.NotificationManagerService.StatsPullAtomCallbackImpl();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_PREFERENCES, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mPullAtomCallback);
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_CHANNEL_PREFERENCES, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mPullAtomCallback);
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_CHANNEL_GROUP_PREFERENCES, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mPullAtomCallback);
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.DND_MODE_RULE, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mPullAtomCallback);
    }

    private class StatsPullAtomCallbackImpl implements android.app.StatsManager.StatsPullAtomCallback {
        private StatsPullAtomCallbackImpl() {
        }

        public int onPullAtom(int atomTag, java.util.List<android.util.StatsEvent> data) {
            switch (atomTag) {
                case com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_PREFERENCES /* 10071 */:
                case com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_CHANNEL_PREFERENCES /* 10072 */:
                case com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_CHANNEL_GROUP_PREFERENCES /* 10073 */:
                case com.android.internal.util.FrameworkStatsLog.DND_MODE_RULE /* 10084 */:
                    return com.android.server.notification.NotificationManagerService.this.pullNotificationStates(atomTag, data);
                default:
                    throw new java.lang.UnsupportedOperationException("Unknown tagId=" + atomTag);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullNotificationStates(int atomTag, java.util.List<android.util.StatsEvent> data) {
        switch (atomTag) {
            case com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_PREFERENCES /* 10071 */:
                this.mPreferencesHelper.pullPackagePreferencesStats(data, getAllUsersNotificationPermissions());
                break;
            case com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_CHANNEL_PREFERENCES /* 10072 */:
                this.mPreferencesHelper.pullPackageChannelPreferencesStats(data);
                break;
            case com.android.internal.util.FrameworkStatsLog.PACKAGE_NOTIFICATION_CHANNEL_GROUP_PREFERENCES /* 10073 */:
                this.mPreferencesHelper.pullPackageChannelGroupPreferencesStats(data);
                break;
            case com.android.internal.util.FrameworkStatsLog.DND_MODE_RULE /* 10084 */:
                this.mZenModeHelper.pullRules(data);
                break;
        }
        return 0;
    }

    private com.android.server.notification.GroupHelper getGroupHelper() {
        this.mAutoGroupAtCount = getContext().getResources().getInteger(android.R.integer.config_audio_ring_vol_steps);
        return new com.android.server.notification.GroupHelper(this.mNMSWrapper, getContext(), getContext().getPackageManager(), this.mAutoGroupAtCount, new com.android.server.notification.GroupHelper.Callback() { // from class: com.android.server.notification.NotificationManagerService.11
            @Override // com.android.server.notification.GroupHelper.Callback
            public void addAutoGroup(java.lang.String key, boolean requestSort) {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.NotificationManagerService.this.addAutogroupKeyLocked(key, requestSort);
                }
            }

            @Override // com.android.server.notification.GroupHelper.Callback
            public void removeAutoGroup(java.lang.String key) {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.NotificationManagerService.this.removeAutogroupKeyLocked(key);
                }
            }

            @Override // com.android.server.notification.GroupHelper.Callback
            public void addAutoGroupSummary(int userId, java.lang.String pkg, java.lang.String triggeringKey, com.android.server.notification.GroupHelper.NotificationAttributes summaryAttr) throws java.lang.Throwable {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.createAutoGroupSummary(userId, pkg, triggeringKey, summaryAttr.flags, summaryAttr.icon, summaryAttr.iconColor, summaryAttr.visibility);
                if (r != null) {
                    boolean isAppForeground = com.android.server.notification.NotificationManagerService.this.mActivityManager.getPackageImportance(pkg) == 100;
                    com.android.server.notification.NotificationManagerService.this.mHandler.post(com.android.server.notification.NotificationManagerService.this.new EnqueueNotificationRunnable(userId, r, isAppForeground, com.android.server.notification.NotificationManagerService.this.mPostNotificationTrackerFactory.newTracker(null)));
                }
            }

            @Override // com.android.server.notification.GroupHelper.Callback
            public void removeAutoGroupSummary(int userId, java.lang.String pkg) {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.NotificationManagerService.this.clearAutogroupSummaryLocked(userId, pkg);
                }
            }

            @Override // com.android.server.notification.GroupHelper.Callback
            public void updateAutogroupSummary(int userId, java.lang.String pkg, com.android.server.notification.GroupHelper.NotificationAttributes summaryAttr) {
                boolean isAppForeground = pkg != null && com.android.server.notification.NotificationManagerService.this.mActivityManager.getPackageImportance(pkg) == 100;
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.NotificationManagerService.this.updateAutobundledSummaryLocked(userId, pkg, summaryAttr, isAppForeground);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendRegisteredOnlyBroadcast(java.lang.String action) {
        sendRegisteredOnlyBroadcast(new android.content.Intent(action));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendRegisteredOnlyBroadcast(android.content.Intent baseIntent) {
        int[] userIds = this.mUmInternal.getProfileIds(this.mAmi.getCurrentUserId(), true);
        android.content.Intent intent = new android.content.Intent(baseIntent).addFlags(1073741824);
        for (int i : userIds) {
            getContext().sendBroadcastAsUser(intent, android.os.UserHandle.of(i), null);
        }
        for (int userId : userIds) {
            for (java.lang.String pkg : this.mConditionProviders.getAllowedPackages(userId)) {
                android.content.Intent pkgIntent = new android.content.Intent(baseIntent).setPackage(pkg).setFlags(67108864);
                getContext().sendBroadcastAsUser(pkgIntent, android.os.UserHandle.of(userId));
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        onBootPhase(phase, android.os.Looper.getMainLooper());
    }

    void onBootPhase(int phase, android.os.Looper mainLooper) {
        if (phase == 500) {
            this.mWindowManagerInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
            this.mZenModeHelper.onSystemReady();
            com.android.server.notification.NotificationManagerService.RoleObserver roleObserver = new com.android.server.notification.NotificationManagerService.RoleObserver(getContext(), (android.app.role.RoleManager) getContext().getSystemService(android.app.role.RoleManager.class), this.mPackageManager, mainLooper);
            roleObserver.init();
            this.mRoleObserver = roleObserver;
            android.content.pm.LauncherApps launcherApps = (android.content.pm.LauncherApps) getContext().getSystemService("launcherapps");
            android.os.UserManager userManager = (android.os.UserManager) getContext().getSystemService("user");
            this.mShortcutHelper = new com.android.server.notification.ShortcutHelper(launcherApps, this.mShortcutListener, (android.content.pm.ShortcutServiceInternal) getLocalService(android.content.pm.ShortcutServiceInternal.class), userManager);
            com.android.server.notification.BubbleExtractor bubbsExtractor = (com.android.server.notification.BubbleExtractor) this.mRankingHelper.findExtractor(com.android.server.notification.BubbleExtractor.class);
            if (bubbsExtractor != null) {
                bubbsExtractor.setShortcutHelper(this.mShortcutHelper);
            }
            registerNotificationPreferencesPullers();
            if (this.mLockUtils == null) {
                this.mLockUtils = new com.android.internal.widget.LockPatternUtils(getContext());
            }
            this.mLockUtils.registerStrongAuthTracker(this.mStrongAuthTracker);
            this.mAttentionHelper.onSystemReady();
        } else if (phase == 600) {
            this.mSettingsObserver.observe();
            this.mListeners.onBootPhaseAppsCanStart();
            this.mAssistants.onBootPhaseAppsCanStart();
            this.mConditionProviders.onBootPhaseAppsCanStart();
            this.mHistoryManager.onBootPhaseAppsCanStart();
            migrateDefaultNAS();
            maybeShowInitialReviewPermissionsNotification();
            if (android.app.Flags.modesApi()) {
                this.mZenModeHelper.setDeviceEffectsApplier(new com.android.server.notification.DefaultDeviceEffectsApplier(getContext()));
            }
            java.util.List<android.content.pm.ModuleInfo> moduleInfoList = this.mPackageManagerClient.getInstalledModules(268435456);
            for (android.content.pm.ModuleInfo mi : moduleInfoList) {
                if (java.util.Objects.equals(mi.getApexModuleName(), ADSERVICES_MODULE_PKG_NAME)) {
                    this.mAdservicesModuleInfo = mi;
                }
            }
        } else if (phase == 550) {
            this.mSnoozeHelper.scheduleRepostsForPersistedNotifications(java.lang.System.currentTimeMillis());
        } else if (phase == 520) {
            this.mPreferencesHelper.updateFixedImportance(this.mUm.getUsers());
            this.mPreferencesHelper.migrateNotificationPermissions(this.mUm.getUsers());
        } else if (phase == 1000) {
            if (this.mFlagResolver.isEnabled(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.DEBUG_SHORT_BITMAP_DURATION)) {
                new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onBootPhase$1();
                    }
                }).start();
            } else if (com.android.server.notification.Flags.expireBitmaps()) {
                com.android.server.notification.NotificationBitmapJobService.scheduleJob(getContext());
            }
        }
        if (this.mNMSWrapper.getNMSExt() != null) {
            this.mNMSWrapper.getNMSExt().onBootPhase(phase);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$1() {
        while (true) {
            try {
                java.lang.Thread.sleep(MIN_PACKAGE_OVERRATE_LOG_INTERVAL);
            } catch (java.lang.InterruptedException e) {
            }
            this.mInternalService.removeBitmaps();
        }
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocked(final com.android.server.SystemService.TargetUser user) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUserUnlocked$2(user);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserUnlocked$2(com.android.server.SystemService.TargetUser user) {
        android.os.Trace.traceBegin(524288L, "notifHistoryUnlockUser");
        try {
            this.mHistoryManager.onUserUnlocked(user.getUserIdentifier());
        } finally {
            android.os.Trace.traceEnd(524288L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAppBlockStateChangedBroadcast(final java.lang.String pkg, final int uid, final boolean blocked) {
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendAppBlockStateChangedBroadcast$3(blocked, pkg, uid);
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendAppBlockStateChangedBroadcast$3(boolean blocked, java.lang.String pkg, int uid) {
        try {
            getContext().sendBroadcastAsUser(new android.content.Intent("android.app.action.APP_BLOCK_STATE_CHANGED").putExtra("android.app.extra.BLOCKED_STATE", blocked).addFlags(268435456).setPackage(pkg), android.os.UserHandle.of(android.os.UserHandle.getUserId(uid)), null);
        } catch (java.lang.SecurityException e) {
            android.util.Slog.w(TAG, "Can't notify app about app block change", e);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        if (!com.android.server.notification.Flags.useSsmUserSwitchSignal()) {
            return;
        }
        int userId = to.getUserIdentifier();
        this.mUserProfiles.updateCache(getContext());
        if (!this.mUserProfiles.isProfileUser(userId, getContext())) {
            this.mSettingsObserver.update(null);
            this.mConditionProviders.onUserSwitched(userId);
            this.mListeners.onUserSwitched(userId);
            this.mZenModeHelper.onUserSwitched(userId);
            this.mPreferencesHelper.syncChannelsBypassingDnd();
        }
        this.mAssistants.onUserSwitched(userId);
    }

    @Override // com.android.server.SystemService
    public void onUserStopping(final com.android.server.SystemService.TargetUser user) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUserStopping$4(user);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserStopping$4(com.android.server.SystemService.TargetUser user) {
        android.os.Trace.traceBegin(524288L, "notifHistoryStopUser");
        try {
            this.mHistoryManager.onUserStopped(user.getUserIdentifier());
        } finally {
            android.os.Trace.traceEnd(524288L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateListenerHintsLocked() {
        int hints = calculateHints();
        if (hints == this.mListenerHints) {
            return;
        }
        com.android.server.notification.ZenLog.traceListenerHintsChanged(this.mListenerHints, hints, this.mEffectsSuppressors.size());
        this.mListenerHints = hints;
        scheduleListenerHintsChanged(hints);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEffectsSuppressorLocked() {
        long updatedSuppressedEffects = calculateSuppressedEffects();
        if (updatedSuppressedEffects == this.mZenModeHelper.getSuppressedEffects()) {
            return;
        }
        java.util.List<android.content.ComponentName> suppressors = getSuppressors();
        com.android.server.notification.ZenLog.traceEffectsSuppressorChanged(this.mEffectsSuppressors, suppressors, updatedSuppressedEffects);
        this.mEffectsSuppressors = suppressors;
        this.mZenModeHelper.setSuppressedEffects(updatedSuppressedEffects);
        sendRegisteredOnlyBroadcast("android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitIdle() {
        if (this.mDeviceIdleManager != null) {
            this.mDeviceIdleManager.endIdle("notification interaction");
        }
    }

    void updateNotificationChannelInt(java.lang.String pkg, int uid, android.app.NotificationChannel channel, boolean fromListener) {
        if (channel.getImportance() == 0) {
            cancelAllNotificationsInt(MY_UID, MY_PID, pkg, channel.getId(), 0, 64, android.os.UserHandle.getUserId(uid), 17);
            if (isUidSystemOrPhone(uid)) {
                android.util.IntArray profileIds = this.mUserProfiles.getCurrentProfileIds();
                int N = profileIds.size();
                for (int i = 0; i < N; i++) {
                    int profileId = profileIds.get(i);
                    cancelAllNotificationsInt(MY_UID, MY_PID, pkg, channel.getId(), 0, 0, profileId, 17);
                }
            }
        }
        android.app.NotificationChannel preUpdate = this.mPreferencesHelper.getNotificationChannel(pkg, uid, channel.getId(), true);
        this.mPreferencesHelper.updateNotificationChannel(pkg, uid, channel, true, android.os.Binder.getCallingUid(), isCallerSystemOrSystemUi());
        if (this.mPreferencesHelper.onlyHasDefaultChannel(pkg, uid)) {
            this.mPermissionHelper.setNotificationPermission(pkg, android.os.UserHandle.getUserId(uid), channel.getImportance() != 0, true);
        }
        maybeNotifyChannelOwner(pkg, uid, preUpdate, channel);
        if (!fromListener) {
            android.app.NotificationChannel modifiedChannel = this.mPreferencesHelper.getNotificationChannel(pkg, uid, channel.getId(), false);
            this.mListeners.notifyNotificationChannelChanged(pkg, android.os.UserHandle.getUserHandleForUid(uid), modifiedChannel, 2);
        }
        handleSavePolicyFile();
    }

    private void maybeNotifyChannelOwner(java.lang.String pkg, int uid, android.app.NotificationChannel preUpdate, android.app.NotificationChannel update) {
        try {
            if ((preUpdate.getImportance() == 0 && update.getImportance() != 0) || (preUpdate.getImportance() != 0 && update.getImportance() == 0)) {
                getContext().sendBroadcastAsUser(new android.content.Intent("android.app.action.NOTIFICATION_CHANNEL_BLOCK_STATE_CHANGED").putExtra("android.app.extra.NOTIFICATION_CHANNEL_ID", update.getId()).putExtra("android.app.extra.BLOCKED_STATE", update.getImportance() == 0).addFlags(268435456).setPackage(pkg), android.os.UserHandle.of(android.os.UserHandle.getUserId(uid)), null);
            }
        } catch (java.lang.SecurityException e) {
            android.util.Slog.w(TAG, "Can't notify app about channel change", e);
        }
    }

    void createNotificationChannelGroup(java.lang.String pkg, int uid, android.app.NotificationChannelGroup group, boolean fromApp, boolean fromListener) {
        java.util.Objects.requireNonNull(group);
        java.util.Objects.requireNonNull(pkg);
        android.app.NotificationChannelGroup preUpdate = this.mPreferencesHelper.getNotificationChannelGroup(group.getId(), pkg, uid);
        this.mPreferencesHelper.createNotificationChannelGroup(pkg, uid, group, fromApp, android.os.Binder.getCallingUid(), isCallerSystemOrSystemUi());
        if (!fromApp) {
            maybeNotifyChannelGroupOwner(pkg, uid, preUpdate, group);
        }
        if (!fromListener) {
            this.mListeners.notifyNotificationChannelGroupChanged(pkg, android.os.UserHandle.of(android.os.UserHandle.getCallingUserId()), group, 1);
        }
    }

    private void maybeNotifyChannelGroupOwner(java.lang.String pkg, int uid, android.app.NotificationChannelGroup preUpdate, android.app.NotificationChannelGroup update) {
        try {
            if (preUpdate.isBlocked() != update.isBlocked()) {
                getContext().sendBroadcastAsUser(new android.content.Intent("android.app.action.NOTIFICATION_CHANNEL_GROUP_BLOCK_STATE_CHANGED").putExtra("android.app.extra.NOTIFICATION_CHANNEL_GROUP_ID", update.getId()).putExtra("android.app.extra.BLOCKED_STATE", update.isBlocked()).addFlags(268435456).setPackage(pkg), android.os.UserHandle.of(android.os.UserHandle.getUserId(uid)), null);
            }
        } catch (java.lang.SecurityException e) {
            android.util.Slog.w(TAG, "Can't notify app about group change", e);
        }
    }

    private java.util.ArrayList<android.content.ComponentName> getSuppressors() {
        java.util.ArrayList<android.content.ComponentName> names = new java.util.ArrayList<>();
        for (int i = this.mListenersDisablingEffects.size() - 1; i >= 0; i--) {
            android.util.ArraySet<android.content.ComponentName> serviceInfoList = this.mListenersDisablingEffects.valueAt(i);
            for (android.content.ComponentName info : serviceInfoList) {
                names.add(info);
            }
        }
        return names;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeDisabledHints(com.android.server.notification.ManagedServices.ManagedServiceInfo info) {
        return removeDisabledHints(info, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeDisabledHints(com.android.server.notification.ManagedServices.ManagedServiceInfo info, int hints) {
        boolean removed = false;
        for (int i = this.mListenersDisablingEffects.size() - 1; i >= 0; i--) {
            int hint = this.mListenersDisablingEffects.keyAt(i);
            android.util.ArraySet<android.content.ComponentName> listeners = this.mListenersDisablingEffects.valueAt(i);
            if (hints == 0 || (hint & hints) == hint) {
                removed |= listeners.remove(info.component);
            }
        }
        return removed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addDisabledHints(com.android.server.notification.ManagedServices.ManagedServiceInfo info, int hints) {
        if ((hints & 1) != 0) {
            addDisabledHint(info, 1);
        }
        if ((hints & 2) != 0) {
            addDisabledHint(info, 2);
        }
        if ((hints & 4) != 0) {
            addDisabledHint(info, 4);
        }
    }

    private void addDisabledHint(com.android.server.notification.ManagedServices.ManagedServiceInfo info, int hint) {
        if (this.mListenersDisablingEffects.indexOfKey(hint) < 0) {
            this.mListenersDisablingEffects.put(hint, new android.util.ArraySet<>());
        }
        android.util.ArraySet<android.content.ComponentName> hintListeners = this.mListenersDisablingEffects.get(hint);
        hintListeners.add(info.component);
    }

    private int calculateHints() {
        int hints = 0;
        for (int i = this.mListenersDisablingEffects.size() - 1; i >= 0; i--) {
            int hint = this.mListenersDisablingEffects.keyAt(i);
            android.util.ArraySet<android.content.ComponentName> serviceInfoList = this.mListenersDisablingEffects.valueAt(i);
            if (!serviceInfoList.isEmpty()) {
                hints |= hint;
            }
        }
        return hints;
    }

    private long calculateSuppressedEffects() {
        int hints = calculateHints();
        long suppressedEffects = (hints & 1) != 0 ? 0 | 3 : 0L;
        if ((hints & 2) != 0) {
            suppressedEffects |= 1;
        }
        if ((hints & 4) != 0) {
            return suppressedEffects | 2;
        }
        return suppressedEffects;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInterruptionFilterLocked() {
        int interruptionFilter = this.mZenModeHelper.getZenModeListenerInterruptionFilter();
        if (interruptionFilter == this.mInterruptionFilter) {
            return;
        }
        this.mInterruptionFilter = interruptionFilter;
        scheduleInterruptionFilterChanged(interruptionFilter);
    }

    int correctCategory(int requestedCategoryList, int categoryType, int currentCategoryList) {
        if ((requestedCategoryList & categoryType) != 0 && (currentCategoryList & categoryType) == 0) {
            return requestedCategoryList & (~categoryType);
        }
        if ((requestedCategoryList & categoryType) == 0 && (currentCategoryList & categoryType) != 0) {
            return requestedCategoryList | categoryType;
        }
        return requestedCategoryList;
    }

    android.app.INotificationManager getBinderService() {
        return android.app.INotificationManager.Stub.asInterface(this.mService);
    }

    protected void reportSeen(com.android.server.notification.NotificationRecord r) {
        if (!r.isProxied()) {
            this.mAppUsageStats.reportEvent(r.getSbn().getPackageName(), getRealUserId(r.getSbn().getUserId()), 10);
        }
    }

    protected int calculateSuppressedVisualEffects(android.app.NotificationManager.Policy incomingPolicy, android.app.NotificationManager.Policy currPolicy, int targetSdkVersion) {
        if (incomingPolicy.suppressedVisualEffects == -1) {
            return incomingPolicy.suppressedVisualEffects;
        }
        int[] effectsIntroducedInP = {4, 8, 16, 32, 64, 128, 256};
        int newSuppressedVisualEffects = incomingPolicy.suppressedVisualEffects;
        if (targetSdkVersion < 28) {
            for (int i = 0; i < effectsIntroducedInP.length; i++) {
                newSuppressedVisualEffects = (newSuppressedVisualEffects & (~effectsIntroducedInP[i])) | (currPolicy.suppressedVisualEffects & effectsIntroducedInP[i]);
            }
            int i2 = newSuppressedVisualEffects & 1;
            if (i2 != 0) {
                newSuppressedVisualEffects = newSuppressedVisualEffects | 8 | 4;
            }
            if ((newSuppressedVisualEffects & 2) != 0) {
                return newSuppressedVisualEffects | 16;
            }
            return newSuppressedVisualEffects;
        }
        boolean hasNewEffects = (newSuppressedVisualEffects + (-2)) - 1 > 0;
        if (hasNewEffects) {
            int newSuppressedVisualEffects2 = newSuppressedVisualEffects & (-4);
            if ((newSuppressedVisualEffects2 & 16) != 0) {
                newSuppressedVisualEffects2 |= 2;
            }
            if ((newSuppressedVisualEffects2 & 8) != 0 && (newSuppressedVisualEffects2 & 4) != 0 && (newSuppressedVisualEffects2 & 128) != 0) {
                return newSuppressedVisualEffects2 | 1;
            }
            return newSuppressedVisualEffects2;
        }
        if ((newSuppressedVisualEffects & 1) != 0) {
            newSuppressedVisualEffects = newSuppressedVisualEffects | 8 | 4 | 128;
        }
        if ((newSuppressedVisualEffects & 2) != 0) {
            return newSuppressedVisualEffects | 16;
        }
        return newSuppressedVisualEffects;
    }

    protected void maybeRecordInterruptionLocked(com.android.server.notification.NotificationRecord r) {
        if (r.isInterruptive() && !r.hasRecordedInterruption()) {
            this.mAppUsageStats.reportInterruptiveNotification(r.getSbn().getPackageName(), r.getChannel().getId(), getRealUserId(r.getSbn().getUserId()));
            android.os.Trace.traceBegin(524288L, "notifHistoryAddItem");
            try {
                if (r.getNotification().getSmallIcon() != null) {
                    this.mHistoryManager.addNotification(new android.app.NotificationHistory.HistoricalNotification.Builder().setPackage(r.getSbn().getPackageName()).setUid(r.getSbn().getUid()).setUserId(r.getSbn().getNormalizedUserId()).setChannelId(r.getChannel().getId()).setChannelName(r.getChannel().getName().toString()).setPostedTimeMs(java.lang.System.currentTimeMillis()).setTitle(getHistoryTitle(r.getNotification())).setText(getHistoryText(r.getNotification())).setIcon(r.getNotification().getSmallIcon()).build());
                }
                android.os.Trace.traceEnd(524288L);
                r.setRecordedInterruption(true);
            } catch (java.lang.Throwable th) {
                android.os.Trace.traceEnd(524288L);
                throw th;
            }
        }
    }

    protected void reportForegroundServiceUpdate(final boolean shown, final android.app.Notification notification, final int id, final java.lang.String pkg, final int userId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$reportForegroundServiceUpdate$5(shown, notification, id, pkg, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportForegroundServiceUpdate$5(boolean shown, android.app.Notification notification, int id, java.lang.String pkg, int userId) {
        this.mAmi.onForegroundServiceNotificationUpdate(shown, notification, id, pkg, userId);
    }

    protected void maybeReportForegroundServiceUpdate(com.android.server.notification.NotificationRecord r, boolean shown) {
        if (r.isForegroundService()) {
            android.service.notification.StatusBarNotification sbn = r.getSbn();
            reportForegroundServiceUpdate(shown, sbn.getNotification(), sbn.getId(), sbn.getPackageName(), sbn.getUser().getIdentifier());
        }
    }

    private java.lang.String getHistoryTitle(android.app.Notification n) {
        java.lang.CharSequence title = null;
        if (n.extras != null && (title = n.extras.getCharSequence("android.title")) == null) {
            title = n.extras.getCharSequence("android.title.big");
        }
        return title == null ? getContext().getResources().getString(android.R.string.notification_channel_security) : java.lang.String.valueOf(title);
    }

    private java.lang.String getHistoryText(android.app.Notification n) {
        java.lang.CharSequence text = null;
        if (n.extras != null) {
            text = n.extras.getCharSequence("android.text");
            android.app.Notification.Builder nb = android.app.Notification.Builder.recoverBuilder(getContext(), n);
            if (nb.getStyle() instanceof android.app.Notification.BigTextStyle) {
                text = ((android.app.Notification.BigTextStyle) nb.getStyle()).getBigText();
            } else if (nb.getStyle() instanceof android.app.Notification.MessagingStyle) {
                android.app.Notification.MessagingStyle ms = (android.app.Notification.MessagingStyle) nb.getStyle();
                java.util.List<android.app.Notification.MessagingStyle.Message> messages = ms.getMessages();
                if (messages != null && messages.size() > 0) {
                    text = messages.get(messages.size() - 1).getText();
                }
            }
            if (android.text.TextUtils.isEmpty(text)) {
                text = n.extras.getCharSequence("android.text");
            }
        }
        if (text == null) {
            return null;
        }
        return java.lang.String.valueOf(text);
    }

    protected void maybeRegisterMessageSent(com.android.server.notification.NotificationRecord r) {
        if (r.isConversation()) {
            if (r.getShortcutInfo() != null) {
                if (this.mPreferencesHelper.setValidMessageSent(r.getSbn().getPackageName(), r.getUid())) {
                    handleSavePolicyFile();
                    return;
                } else {
                    if (r.getNotification().getBubbleMetadata() != null && this.mPreferencesHelper.setValidBubbleSent(r.getSbn().getPackageName(), r.getUid())) {
                        handleSavePolicyFile();
                        return;
                    }
                    return;
                }
            }
            if (this.mPreferencesHelper.setInvalidMessageSent(r.getSbn().getPackageName(), r.getUid())) {
                handleSavePolicyFile();
            }
        }
    }

    protected void reportUserInteraction(com.android.server.notification.NotificationRecord r) {
        this.mAppUsageStats.reportEvent(r.getSbn().getPackageName(), getRealUserId(r.getSbn().getUserId()), 7);
        if (com.android.server.notification.Flags.politeNotifications()) {
            this.mAttentionHelper.onUserInteraction(r);
        }
    }

    private int getRealUserId(int userId) {
        if (userId == -1) {
            return 0;
        }
        return userId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.notification.toast.ToastRecord getToastRecord(int uid, int pid, java.lang.String packageName, boolean isSystemToast, android.os.IBinder token, java.lang.CharSequence text, android.app.ITransientNotification callback, int duration, android.os.Binder windowToken, int displayId, android.app.ITransientNotificationCallback textCallback) {
        return callback == null ? new com.android.server.notification.toast.TextToastRecord(this, this.mStatusBar, uid, pid, packageName, isSystemToast, token, text, duration, windowToken, displayId, textCallback) : new com.android.server.notification.toast.CustomToastRecord(this, uid, pid, packageName, isSystemToast, token, callback, duration, windowToken, displayId);
    }

    com.android.server.notification.NotificationManagerInternal getInternalService() {
        return this.mInternalService;
    }

    private com.android.server.utils.quota.MultiRateLimiter createToastRateLimiter() {
        return new com.android.server.utils.quota.MultiRateLimiter.Builder(getContext()).addRateLimits(TOAST_RATE_LIMITS).build();
    }

    protected int checkComponentPermission(java.lang.String permission, int uid, int owningUid, boolean exported) {
        return android.app.ActivityManager.checkComponentPermission(permission, uid, owningUid, exported);
    }

    /* JADX INFO: renamed from: com.android.server.notification.NotificationManagerService$12, reason: invalid class name */
    class AnonymousClass12 extends android.app.INotificationManager.Stub {
        AnonymousClass12() {
        }

        public boolean enqueueTextToast(java.lang.String pkg, android.os.IBinder token, java.lang.CharSequence text, int duration, boolean isUiContext, int displayId, android.app.ITransientNotificationCallback textCallback) {
            return enqueueToast(pkg, token, text, null, duration, isUiContext, displayId, textCallback);
        }

        public boolean enqueueToast(java.lang.String pkg, android.os.IBinder token, android.app.ITransientNotification callback, int duration, boolean isUiContext, int displayId) {
            return enqueueToast(pkg, token, null, callback, duration, isUiContext, displayId, null);
        }

        /* JADX WARN: Code restructure failed: missing block: B:100:0x0257, code lost:
        
            if (r9 >= r29.this$0.mToastQueue.size()) goto L102;
         */
        /* JADX WARN: Code restructure failed: missing block: B:101:0x0259, code lost:
        
            r10 = r9;
            r29.this$0.mToastQueue.add(r10, r8);
         */
        /* JADX WARN: Code restructure failed: missing block: B:102:0x0262, code lost:
        
            r29.this$0.mToastQueue.add(r8);
            r10 = r29.this$0.mToastQueue.size() - 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:103:0x0273, code lost:
        
            r29.this$0.keepProcessAliveForToastIfNeededLocked(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:114:0x02c6, code lost:
        
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:116:0x02c8, code lost:
        
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:93:0x0207, code lost:
        
            r0 = r10;
         */
        /* JADX WARN: Code restructure failed: missing block: B:94:0x020e, code lost:
        
            r5 = new android.os.Binder();
            r29.this$0.mWindowManagerInternal.addWindowToken(r5, 2005, r1, null);
         */
        /* JADX WARN: Code restructure failed: missing block: B:96:0x023c, code lost:
        
            r8 = r29.this$0.getToastRecord(r9, r0, r30, r21, r31, r32, r33, r34, r5, r1, r37);
            r9 = r29.this$0.mToastQueue.size();
         */
        /* JADX WARN: Code restructure failed: missing block: B:97:0x0248, code lost:
        
            if (r21 == false) goto L99;
         */
        /* JADX WARN: Code restructure failed: missing block: B:98:0x024a, code lost:
        
            r9 = getInsertIndexForSystemToastLocked();
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v15 */
        /* JADX WARN: Type inference failed for: r0v17, types: [boolean, int] */
        /* JADX WARN: Type inference failed for: r0v21 */
        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private boolean enqueueToast(java.lang.String r30, android.os.IBinder r31, java.lang.CharSequence r32, android.app.ITransientNotification r33, int r34, boolean r35, int r36, android.app.ITransientNotificationCallback r37) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 791
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationManagerService.AnonymousClass12.enqueueToast(java.lang.String, android.os.IBinder, java.lang.CharSequence, android.app.ITransientNotification, int, boolean, int, android.app.ITransientNotificationCallback):boolean");
        }

        private int getInsertIndexForSystemToastLocked() {
            int idx = 0;
            for (com.android.server.notification.toast.ToastRecord r : com.android.server.notification.NotificationManagerService.this.mToastQueue) {
                if (idx == 0 && com.android.server.notification.NotificationManagerService.this.mIsCurrentToastShown) {
                    idx++;
                } else {
                    if (!r.isSystemToast) {
                        return idx;
                    }
                    idx++;
                }
            }
            return idx;
        }

        private boolean checkCanEnqueueToast(java.lang.String pkg, int callingUid, int displayId, boolean isAppRenderedToast, boolean isSystemToast) {
            boolean isPackageSuspended = isPackagePaused(pkg);
            boolean notificationsDisabledForPackage = !areNotificationsEnabledForPackage(pkg, callingUid);
            long callingIdentity = android.os.Binder.clearCallingIdentity();
            try {
                boolean appIsForeground = com.android.server.notification.NotificationManagerService.this.mActivityManager.getUidImportance(callingUid) == 100;
                android.os.Binder.restoreCallingIdentity(callingIdentity);
                if (!isSystemToast && ((notificationsDisabledForPackage && !appIsForeground) || isPackageSuspended)) {
                    android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "Suppressing toast from package " + pkg + (isPackageSuspended ? " due to package suspended." : " by user request."));
                    return false;
                }
                if (com.android.server.notification.NotificationManagerService.this.blockToast(callingUid, isSystemToast, isAppRenderedToast, com.android.server.notification.NotificationManagerService.this.isPackageInForegroundForToast(callingUid))) {
                    android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "Blocking custom toast from package " + pkg + " due to package not in the foreground at time the toast was posted");
                    return false;
                }
                int userId = android.os.UserHandle.getUserId(callingUid);
                if (!isSystemToast && !com.android.server.notification.NotificationManagerService.this.mUmInternal.isUserVisible(userId, displayId)) {
                    android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "Suppressing toast from package " + pkg + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + callingUid + " as user " + userId + " is not visible on display " + displayId);
                    return false;
                }
                return true;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(callingIdentity);
                throw th;
            }
        }

        public void cancelToast(java.lang.String pkg, android.os.IBinder token) {
            android.util.Slog.i(com.android.server.notification.NotificationManagerService.TAG, "cancelToast pkg=" + pkg + " token=" + token);
            if (pkg == null || token == null) {
                android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "Not cancelling notification. pkg=" + pkg + " token=" + token);
                return;
            }
            synchronized (com.android.server.notification.NotificationManagerService.this.mToastQueue) {
                long callingId = android.os.Binder.clearCallingIdentity();
                try {
                    int index = com.android.server.notification.NotificationManagerService.this.indexOfToastLocked(pkg, token);
                    if (index >= 0) {
                        com.android.server.notification.NotificationManagerService.this.cancelToastLocked(index);
                    } else {
                        android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "Toast already cancelled. pkg=" + pkg + " token=" + token);
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(callingId);
                }
            }
        }

        public void setToastRateLimitingEnabled(boolean enable) {
            super.setToastRateLimitingEnabled_enforcePermission();
            synchronized (com.android.server.notification.NotificationManagerService.this.mToastQueue) {
                int uid = android.os.Binder.getCallingUid();
                int userId = android.os.UserHandle.getUserId(uid);
                if (enable) {
                    com.android.server.notification.NotificationManagerService.this.mToastRateLimitingDisabledUids.remove(java.lang.Integer.valueOf(uid));
                    try {
                        java.lang.String[] packages = com.android.server.notification.NotificationManagerService.this.mPackageManager.getPackagesForUid(uid);
                        if (packages == null) {
                            android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "setToastRateLimitingEnabled method haven't found any packages for the  given uid: " + uid + ", toast rate limiter not reset for that uid.");
                            return;
                        }
                        for (java.lang.String pkg : packages) {
                            com.android.server.notification.NotificationManagerService.this.mToastRateLimiter.clear(userId, pkg);
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "Failed to reset toast rate limiter for given uid", e);
                    }
                } else {
                    com.android.server.notification.NotificationManagerService.this.mToastRateLimitingDisabledUids.add(java.lang.Integer.valueOf(uid));
                }
            }
        }

        public void finishToken(java.lang.String pkg, android.os.IBinder token) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mToastQueue) {
                long callingId = android.os.Binder.clearCallingIdentity();
                try {
                    int index = com.android.server.notification.NotificationManagerService.this.indexOfToastLocked(pkg, token);
                    if (index >= 0) {
                        com.android.server.notification.toast.ToastRecord record = com.android.server.notification.NotificationManagerService.this.mToastQueue.get(index);
                        com.android.server.notification.NotificationManagerService.this.finishWindowTokenLocked(record.windowToken, record.displayId);
                    } else {
                        android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "Toast already killed. pkg=" + pkg + " token=" + token);
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(callingId);
                }
            }
        }

        public void enqueueNotificationWithTag(java.lang.String pkg, java.lang.String opPkg, java.lang.String tag, int id, android.app.Notification notification, int userId) throws android.os.RemoteException {
            com.android.server.notification.NotificationManagerService.this.enqueueNotificationInternal(pkg, opPkg, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), tag, id, notification, userId, false);
        }

        public void cancelNotificationWithTag(java.lang.String pkg, java.lang.String opPkg, java.lang.String tag, int id, int userId) {
            int mustNotHaveFlags = com.android.server.notification.NotificationManagerService.this.isCallingUidSystem() ? 0 : 33856;
            if (android.app.Flags.lifetimeExtensionRefactor()) {
                mustNotHaveFlags |= 65536;
            }
            com.android.server.notification.NotificationManagerService.this.cancelNotificationInternal(pkg, opPkg, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), tag, id, userId, mustNotHaveFlags);
        }

        public void cancelAllNotifications(java.lang.String pkg, int userId) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
            int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, true, false, "cancelAllNotifications", pkg);
            if (android.app.Flags.lifetimeExtensionRefactor()) {
                com.android.server.notification.NotificationManagerService.this.cancelAllNotificationsInt(android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), pkg, null, 0, 98368, userId2, 9);
                int packageImportance = com.android.server.notification.NotificationManagerService.this.getPackageImportanceWithIdentity(pkg);
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.NotificationManagerService.this.maybeNotifySystemUiListenerLifetimeExtendedListLocked(com.android.server.notification.NotificationManagerService.this.mNotificationList, packageImportance);
                    com.android.server.notification.NotificationManagerService.this.maybeNotifySystemUiListenerLifetimeExtendedListLocked(com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications, packageImportance);
                }
                return;
            }
            com.android.server.notification.NotificationManagerService.this.cancelAllNotificationsInt(android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), pkg, null, 0, 32832, userId2, 9);
        }

        public void silenceNotificationSound() {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            com.android.server.notification.NotificationManagerService.this.mNotificationDelegate.clearEffects();
        }

        public void setNotificationsEnabledForPackage(java.lang.String str, int i, boolean z) {
            enforceSystemOrSystemUI("setNotificationsEnabledForPackage");
            if (com.android.server.notification.NotificationManagerService.this.mPermissionHelper.hasPermission(i) == z) {
                return;
            }
            com.android.server.notification.NotificationManagerService.this.mPermissionHelper.setNotificationPermission(str, android.os.UserHandle.getUserId(i), z, true);
            com.android.server.notification.NotificationManagerService.this.sendAppBlockStateChangedBroadcast(str, i, !z);
            com.android.server.notification.NotificationManagerService.this.mMetricsLogger.write(new android.metrics.LogMaker(147).setType(4).setPackageName(str).setSubtype(z ? 1 : 0));
            com.android.server.notification.NotificationManagerService.this.mNotificationChannelLogger.logAppNotificationsAllowed(i, str, z);
        }

        public void setNotificationsEnabledWithImportanceLockForPackage(java.lang.String pkg, int uid, boolean enabled) {
            setNotificationsEnabledForPackage(pkg, uid, enabled);
        }

        public boolean areNotificationsEnabled(java.lang.String pkg) {
            return areNotificationsEnabledForPackage(pkg, android.os.Binder.getCallingUid());
        }

        public boolean areNotificationsEnabledForPackage(java.lang.String pkg, int uid) {
            enforceSystemOrSystemUIOrSamePackage(pkg, "Caller not system or systemui or same package");
            if (android.os.UserHandle.getCallingUserId() != android.os.UserHandle.getUserId(uid)) {
                com.android.server.notification.NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS", "canNotifyAsPackage for uid " + uid);
            }
            return com.android.server.notification.NotificationManagerService.this.areNotificationsEnabledForPackageInt(pkg, uid);
        }

        public boolean areBubblesAllowed(java.lang.String pkg) {
            return getBubblePreferenceForPackage(pkg, android.os.Binder.getCallingUid()) == 1;
        }

        public boolean areBubblesEnabled(android.os.UserHandle user) {
            if (android.os.UserHandle.getCallingUserId() != user.getIdentifier()) {
                com.android.server.notification.NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS", "areBubblesEnabled for user " + user.getIdentifier());
            }
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.bubblesEnabled(user);
        }

        public int getBubblePreferenceForPackage(java.lang.String pkg, int uid) {
            enforceSystemOrSystemUIOrSamePackage(pkg, "Caller not system or systemui or same package");
            if (android.os.UserHandle.getCallingUserId() != android.os.UserHandle.getUserId(uid)) {
                com.android.server.notification.NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS", "getBubblePreferenceForPackage for uid " + uid);
            }
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getBubblePreference(pkg, uid);
        }

        public void setBubblesAllowed(java.lang.String pkg, int uid, int bubblePreference) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSystemUiOrShell("Caller not system or sysui or shell");
            com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.setBubblesAllowed(pkg, uid, bubblePreference);
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
        }

        public boolean shouldHideSilentStatusIcons(java.lang.String callingPkg) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSameApp(callingPkg);
            if (com.android.server.notification.NotificationManagerService.this.isCallerSystemOrPhone() || com.android.server.notification.NotificationManagerService.this.mListeners.isListenerPackage(callingPkg)) {
                return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.shouldHideSilentStatusIcons();
            }
            throw new java.lang.SecurityException("Only available for notification listeners");
        }

        public void setHideSilentStatusIcons(boolean hide) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.setHideSilentStatusIcons(hide);
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
            com.android.server.notification.NotificationManagerService.this.mListeners.onStatusBarIconsBehaviorChanged(hide);
        }

        public void deleteNotificationHistoryItem(java.lang.String pkg, int uid, long postedTime) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            com.android.server.notification.NotificationManagerService.this.mHistoryManager.deleteNotificationHistoryItem(pkg, uid, postedTime);
        }

        public android.service.notification.NotificationListenerFilter getListenerFilter(android.content.ComponentName cn, int userId) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            return com.android.server.notification.NotificationManagerService.this.mListeners.getNotificationListenerFilter(android.util.Pair.create(cn, java.lang.Integer.valueOf(userId)));
        }

        public void setListenerFilter(android.content.ComponentName cn, int userId, android.service.notification.NotificationListenerFilter nlf) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            com.android.server.notification.NotificationManagerService.this.mListeners.setNotificationListenerFilter(android.util.Pair.create(cn, java.lang.Integer.valueOf(userId)), nlf);
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
        }

        public int getPackageImportance(java.lang.String pkg) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
            if (com.android.server.notification.NotificationManagerService.this.mPermissionHelper.hasPermission(android.os.Binder.getCallingUid())) {
                return 3;
            }
            return 0;
        }

        public boolean isImportanceLocked(java.lang.String pkg, int uid) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.isImportanceLocked(pkg, uid);
        }

        public boolean canShowBadge(java.lang.String pkg, int uid) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.canShowBadge(pkg, uid);
        }

        public void setShowBadge(java.lang.String pkg, int uid, boolean showBadge) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.setShowBadge(pkg, uid, showBadge);
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
        }

        public boolean hasSentValidMsg(java.lang.String pkg, int uid) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.hasSentValidMsg(pkg, uid);
        }

        public boolean isInInvalidMsgState(java.lang.String pkg, int uid) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.isInInvalidMsgState(pkg, uid);
        }

        public boolean hasUserDemotedInvalidMsgApp(java.lang.String pkg, int uid) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.hasUserDemotedInvalidMsgApp(pkg, uid);
        }

        public void setInvalidMsgAppDemoted(java.lang.String pkg, int uid, boolean isDemoted) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.setInvalidMsgAppDemoted(pkg, uid, isDemoted);
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
        }

        public boolean hasSentValidBubble(java.lang.String pkg, int uid) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.hasSentValidBubble(pkg, uid);
        }

        public void setNotificationDelegate(java.lang.String callingPkg, java.lang.String delegate) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSameApp(callingPkg);
            int callingUid = android.os.Binder.getCallingUid();
            android.os.UserHandle user = android.os.UserHandle.getUserHandleForUid(callingUid);
            if (delegate == null) {
                com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.revokeNotificationDelegate(callingPkg, android.os.Binder.getCallingUid());
                com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
                return;
            }
            try {
                android.content.pm.ApplicationInfo info = com.android.server.notification.NotificationManagerService.this.mPackageManager.getApplicationInfo(delegate, 786432L, user.getIdentifier());
                if (info != null) {
                    com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.setNotificationDelegate(callingPkg, callingUid, delegate, info.uid);
                    com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
                }
            } catch (android.os.RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }

        public java.lang.String getNotificationDelegate(java.lang.String callingPkg) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(callingPkg);
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationDelegate(callingPkg, android.os.Binder.getCallingUid());
        }

        public boolean canNotifyAsPackage(java.lang.String callingPkg, java.lang.String targetPkg, int userId) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSameApp(callingPkg);
            int callingUid = android.os.Binder.getCallingUid();
            android.os.UserHandle user = android.os.UserHandle.getUserHandleForUid(callingUid);
            if (user.getIdentifier() != userId) {
                com.android.server.notification.NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS", "canNotifyAsPackage for user " + userId);
            }
            if (callingPkg.equals(targetPkg)) {
                return true;
            }
            try {
                android.content.pm.ApplicationInfo info = com.android.server.notification.NotificationManagerService.this.mPackageManager.getApplicationInfo(targetPkg, 786432L, userId);
                if (info != null) {
                    return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.isDelegateAllowed(targetPkg, info.uid, callingPkg, callingUid);
                }
                return false;
            } catch (android.os.RemoteException e) {
                return false;
            }
        }

        public boolean canUseFullScreenIntent(android.content.AttributionSource attributionSource) {
            java.lang.String packageName = attributionSource.getPackageName();
            int uid = attributionSource.getUid();
            int userId = android.os.UserHandle.getUserId(uid);
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSameApp(packageName, uid, userId);
            try {
                android.content.pm.ApplicationInfo applicationInfo = com.android.server.notification.NotificationManagerService.this.mPackageManagerClient.getApplicationInfoAsUser(packageName, 268435456, userId);
                return com.android.server.notification.NotificationManagerService.this.checkUseFullScreenIntentPermission(attributionSource, applicationInfo, false);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "Failed to getApplicationInfo() in canUseFullScreenIntent()", e);
                return false;
            }
        }

        public void updateNotificationChannelGroupForPackage(java.lang.String pkg, int uid, android.app.NotificationChannelGroup group) throws android.os.RemoteException {
            enforceSystemOrSystemUI("Caller not system or systemui");
            com.android.server.notification.NotificationManagerService.this.createNotificationChannelGroup(pkg, uid, group, false, false);
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
        }

        public void createNotificationChannelGroups(java.lang.String pkg, android.content.pm.ParceledListSlice channelGroupList) throws android.os.RemoteException {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
            java.util.List<android.app.NotificationChannelGroup> groups = channelGroupList.getList();
            int groupSize = groups.size();
            for (int i = 0; i < groupSize; i++) {
                android.app.NotificationChannelGroup group = groups.get(i);
                com.android.server.notification.NotificationManagerService.this.createNotificationChannelGroup(pkg, android.os.Binder.getCallingUid(), group, true, false);
            }
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
        }

        private void createNotificationChannelsImpl(java.lang.String pkg, int uid, android.content.pm.ParceledListSlice channelsList) {
            createNotificationChannelsImpl(pkg, uid, channelsList, -1);
        }

        private void createNotificationChannelsImpl(java.lang.String pkg, int uid, android.content.pm.ParceledListSlice channelsList, int startingTaskId) {
            boolean z;
            boolean z2;
            java.util.List<android.app.NotificationChannel> channels = channelsList.getList();
            int channelsSize = channels.size();
            if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().shouldLimitChannels(com.android.server.notification.NotificationManagerService.this.mPreferencesHelper, pkg, uid, channelsSize)) {
                return;
            }
            android.content.pm.ParceledListSlice<android.app.NotificationChannel> oldChannels = com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannels(pkg, uid, true);
            boolean z3 = false;
            if (oldChannels != null && !oldChannels.getList().isEmpty()) {
                z = true;
            } else {
                z = false;
            }
            boolean hadChannel = z;
            boolean needsPolicyFileChange = false;
            boolean hasRequestedNotificationPermission = false;
            int i = 0;
            while (i < channelsSize) {
                android.app.NotificationChannel channel = channels.get(i);
                java.util.Objects.requireNonNull(channel, "channel in list is null");
                int i2 = i;
                boolean z4 = z3;
                needsPolicyFileChange = com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.createNotificationChannel(pkg, uid, channel, true, com.android.server.notification.NotificationManagerService.this.mConditionProviders.isPackageOrComponentAllowed(pkg, android.os.UserHandle.getUserId(uid)), android.os.Binder.getCallingUid(), com.android.server.notification.NotificationManagerService.this.isCallerSystemOrSystemUi());
                if (!needsPolicyFileChange) {
                    z2 = true;
                } else {
                    com.android.server.notification.NotificationManagerService.this.mListeners.notifyNotificationChannelChanged(pkg, android.os.UserHandle.getUserHandleForUid(uid), com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannel(pkg, uid, channel.getId(), z4), 1);
                    boolean hasChannel = (hadChannel || hasRequestedNotificationPermission) ? true : z4;
                    if (hasChannel) {
                        z2 = true;
                    } else {
                        z2 = true;
                        android.content.pm.ParceledListSlice<android.app.NotificationChannel> currChannels = com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannels(pkg, uid, true);
                        hasChannel = (currChannels == null || currChannels.getList().isEmpty()) ? z4 : true;
                    }
                    if (!hadChannel && hasChannel && !hasRequestedNotificationPermission && startingTaskId != -1) {
                        if (com.android.server.notification.NotificationManagerService.this.mPermissionPolicyInternal == null) {
                            com.android.server.notification.NotificationManagerService.this.mPermissionPolicyInternal = (com.android.server.policy.PermissionPolicyInternal) com.android.server.LocalServices.getService(com.android.server.policy.PermissionPolicyInternal.class);
                        }
                        com.android.server.notification.NotificationManagerService.this.mHandler.post(new com.android.server.notification.NotificationManagerService.ShowNotificationPermissionPromptRunnable(pkg, android.os.UserHandle.getUserId(uid), startingTaskId, com.android.server.notification.NotificationManagerService.this.mPermissionPolicyInternal));
                        hasRequestedNotificationPermission = true;
                    }
                }
                i = i2 + 1;
                z3 = z4;
            }
            if (needsPolicyFileChange) {
                com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
            }
        }

        public void createNotificationChannels(java.lang.String pkg, android.content.pm.ParceledListSlice channelsList) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
            int taskId = -1;
            try {
                int uid = com.android.server.notification.NotificationManagerService.this.mPackageManager.getPackageUid(pkg, 0L, android.os.UserHandle.getUserId(android.os.Binder.getCallingUid()));
                taskId = com.android.server.notification.NotificationManagerService.this.mAtm.getTaskToShowPermissionDialogOn(pkg, uid);
            } catch (android.os.RemoteException e) {
            }
            createNotificationChannelsImpl(pkg, android.os.Binder.getCallingUid(), channelsList, taskId);
        }

        public void createNotificationChannelsForPackage(java.lang.String pkg, int uid, android.content.pm.ParceledListSlice channelsList) {
            enforceSystemOrSystemUI("only system can call this");
            createNotificationChannelsImpl(pkg, uid, channelsList);
        }

        public void createConversationNotificationChannelForPackage(java.lang.String pkg, int uid, android.app.NotificationChannel parentChannel, java.lang.String conversationId) {
            enforceSystemOrSystemUI("only system can call this");
            com.android.internal.util.Preconditions.checkNotNull(parentChannel);
            com.android.internal.util.Preconditions.checkNotNull(conversationId);
            java.lang.String parentId = parentChannel.getId();
            parentChannel.setId(java.lang.String.format("%1$s : %2$s", parentId, conversationId));
            parentChannel.setConversationId(parentId, conversationId);
            createNotificationChannelsImpl(pkg, uid, new android.content.pm.ParceledListSlice(java.util.Arrays.asList(parentChannel)));
            com.android.server.notification.NotificationManagerService.this.mRankingHandler.requestSort();
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
        }

        public android.app.NotificationChannel getNotificationChannel(java.lang.String callingPkg, int userId, java.lang.String targetPkg, java.lang.String channelId) {
            return getConversationNotificationChannel(callingPkg, userId, targetPkg, channelId, true, null);
        }

        public android.app.NotificationChannel getConversationNotificationChannel(java.lang.String callingPkg, int userId, java.lang.String targetPkg, java.lang.String channelId, boolean returnParentIfNoConversationChannel, java.lang.String conversationId) {
            if (canNotifyAsPackage(callingPkg, targetPkg, userId) || com.android.server.notification.NotificationManagerService.this.isCallerSystemOrSystemUiOrShell()) {
                int targetUid = -1;
                try {
                    targetUid = com.android.server.notification.NotificationManagerService.this.mPackageManagerClient.getPackageUidAsUser(targetPkg, userId);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                }
                if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && !com.android.server.notification.NotificationManagerService.this.isCallerSystemOrSystemUiOrShell()) {
                    return com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().getConversationNotificationChannel(targetPkg, targetUid, channelId, conversationId, returnParentIfNoConversationChannel, false);
                }
                return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getConversationNotificationChannel(targetPkg, targetUid, channelId, conversationId, returnParentIfNoConversationChannel, false);
            }
            throw new java.lang.SecurityException("Pkg " + callingPkg + " cannot read channels for " + targetPkg + " in " + userId);
        }

        public android.app.NotificationChannel getNotificationChannelForPackage(java.lang.String pkg, int uid, java.lang.String channelId, java.lang.String conversationId, boolean includeDeleted) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getConversationNotificationChannel(pkg, uid, channelId, conversationId, true, includeDeleted);
        }

        private void enforceDeletingChannelHasNoFgService(java.lang.String pkg, int userId, java.lang.String channelId) {
            if (com.android.server.notification.NotificationManagerService.this.mAmi.hasForegroundServiceNotification(pkg, userId, channelId)) {
                android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "Package u" + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + pkg + " may not delete notification channel '" + channelId + "' with fg service");
                throw new java.lang.SecurityException("Not allowed to delete channel " + channelId + " with a foreground service");
            }
        }

        private void enforceDeletingChannelHasNoUserInitiatedJob(java.lang.String pkg, int userId, java.lang.String channelId) {
            com.android.server.job.JobSchedulerInternal js = (com.android.server.job.JobSchedulerInternal) com.android.server.LocalServices.getService(com.android.server.job.JobSchedulerInternal.class);
            if (js != null && js.isNotificationChannelAssociatedWithAnyUserInitiatedJobs(channelId, userId, pkg)) {
                android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "Package u" + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + pkg + " may not delete notification channel '" + channelId + "' with user-initiated job");
                throw new java.lang.SecurityException("Not allowed to delete channel " + channelId + " with a user-initiated job");
            }
        }

        public void deleteNotificationChannel(java.lang.String pkg, java.lang.String channelId) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
            int callingUid = android.os.Binder.getCallingUid();
            boolean isSystemOrSystemUi = com.android.server.notification.NotificationManagerService.this.isCallerSystemOrSystemUi();
            int callingUser = android.os.UserHandle.getUserId(callingUid);
            if ("miscellaneous".equals(channelId)) {
                throw new java.lang.IllegalArgumentException("Cannot delete default channel");
            }
            enforceDeletingChannelHasNoFgService(pkg, callingUser, channelId);
            enforceDeletingChannelHasNoUserInitiatedJob(pkg, callingUser, channelId);
            com.android.server.notification.NotificationManagerService.this.cancelAllNotificationsInt(com.android.server.notification.NotificationManagerService.MY_UID, com.android.server.notification.NotificationManagerService.MY_PID, pkg, channelId, 0, 0, callingUser, 20);
            boolean previouslyExisted = com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.deleteNotificationChannel(pkg, callingUid, channelId, callingUid, isSystemOrSystemUi);
            if (previouslyExisted) {
                com.android.server.notification.NotificationManagerService.this.mArchive.removeChannelNotifications(pkg, callingUser, channelId);
                com.android.server.notification.NotificationManagerService.this.mHistoryManager.deleteNotificationChannel(pkg, callingUid, channelId);
                com.android.server.notification.NotificationManagerService.this.mListeners.notifyNotificationChannelChanged(pkg, android.os.UserHandle.getUserHandleForUid(callingUid), com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannel(pkg, callingUid, channelId, true), 3);
                com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
            }
        }

        public android.app.NotificationChannelGroup getNotificationChannelGroup(java.lang.String pkg, java.lang.String groupId) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
            if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && !com.android.server.notification.NotificationManagerService.this.isCallerSystemOrPhone()) {
                return com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().getNotificationChannelGroupWithChannels(pkg, android.os.Binder.getCallingUid(), groupId, false);
            }
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroupWithChannels(pkg, android.os.Binder.getCallingUid(), groupId, false);
        }

        public android.content.pm.ParceledListSlice<android.app.NotificationChannelGroup> getNotificationChannelGroups(java.lang.String pkg) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
            if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && !com.android.server.notification.NotificationManagerService.this.isCallerSystemOrPhone()) {
                return com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().getNotificationChannelGroups(pkg, android.os.Binder.getCallingUid(), false, false, true);
            }
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroups(pkg, android.os.Binder.getCallingUid(), false, false, true, true, null);
        }

        public void deleteNotificationChannelGroup(java.lang.String pkg, java.lang.String groupId) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
            int callingUid = android.os.Binder.getCallingUid();
            boolean isSystemOrSystemUi = com.android.server.notification.NotificationManagerService.this.isCallerSystemOrSystemUi();
            android.app.NotificationChannelGroup groupToDelete = com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroupWithChannels(pkg, callingUid, groupId, false);
            if (groupToDelete != null) {
                int userId = android.os.UserHandle.getUserId(callingUid);
                java.util.List<android.app.NotificationChannel> groupChannels = groupToDelete.getChannels();
                for (int i = 0; i < groupChannels.size(); i++) {
                    java.lang.String channelId = groupChannels.get(i).getId();
                    enforceDeletingChannelHasNoFgService(pkg, userId, channelId);
                    enforceDeletingChannelHasNoUserInitiatedJob(pkg, userId, channelId);
                }
                java.util.List<android.app.NotificationChannel> deletedChannels = com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.deleteNotificationChannelGroup(pkg, callingUid, groupId, callingUid, isSystemOrSystemUi);
                int i2 = 0;
                while (i2 < deletedChannels.size()) {
                    android.app.NotificationChannel deletedChannel = deletedChannels.get(i2);
                    com.android.server.notification.NotificationManagerService.this.cancelAllNotificationsInt(com.android.server.notification.NotificationManagerService.MY_UID, com.android.server.notification.NotificationManagerService.MY_PID, pkg, deletedChannel.getId(), 0, 0, userId, 20);
                    com.android.server.notification.NotificationManagerService.this.mListeners.notifyNotificationChannelChanged(pkg, android.os.UserHandle.getUserHandleForUid(callingUid), deletedChannel, 3);
                    i2++;
                    deletedChannels = deletedChannels;
                    groupChannels = groupChannels;
                }
                com.android.server.notification.NotificationManagerService.this.mListeners.notifyNotificationChannelGroupChanged(pkg, android.os.UserHandle.getUserHandleForUid(callingUid), groupToDelete, 3);
                com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
            }
        }

        public void updateNotificationChannelForPackage(java.lang.String pkg, int uid, android.app.NotificationChannel channel) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSystemUiOrShell("Caller not system or sysui or shell");
            java.util.Objects.requireNonNull(channel);
            com.android.server.notification.NotificationManagerService.this.updateNotificationChannelInt(pkg, uid, channel, false);
        }

        public void unlockNotificationChannel(java.lang.String pkg, int uid, java.lang.String channelId) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSystemUiOrShell("Caller not system or sysui or shell");
            com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.unlockNotificationChannelImportance(pkg, uid, channelId);
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
        }

        public void unlockAllNotificationChannels() {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.unlockAllNotificationChannels();
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
        }

        public android.content.pm.ParceledListSlice<android.app.NotificationChannel> getNotificationChannelsForPackage(java.lang.String pkg, int uid, boolean includeDeleted) {
            enforceSystemOrSystemUI("getNotificationChannelsForPackage");
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannels(pkg, uid, includeDeleted);
        }

        public int getNumNotificationChannelsForPackage(java.lang.String pkg, int uid, boolean includeDeleted) {
            enforceSystemOrSystemUI("getNumNotificationChannelsForPackage");
            return com.android.server.notification.NotificationManagerService.this.getNumNotificationChannelsForPackage(pkg, uid, includeDeleted);
        }

        public boolean onlyHasDefaultChannel(java.lang.String pkg, int uid) {
            enforceSystemOrSystemUI("onlyHasDefaultChannel");
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.onlyHasDefaultChannel(pkg, uid);
        }

        public int getDeletedChannelCount(java.lang.String pkg, int uid) {
            enforceSystemOrSystemUI("getDeletedChannelCount");
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getDeletedChannelCount(pkg, uid);
        }

        public int getBlockedChannelCount(java.lang.String pkg, int uid) {
            enforceSystemOrSystemUI("getBlockedChannelCount");
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getBlockedChannelCount(pkg, uid);
        }

        public android.content.pm.ParceledListSlice<android.service.notification.ConversationChannelWrapper> getConversations(boolean onlyImportant) {
            enforceSystemOrSystemUI("getConversations");
            android.util.IntArray userIds = com.android.server.notification.NotificationManagerService.this.mUserProfiles.getCurrentProfileIds();
            java.util.ArrayList<android.service.notification.ConversationChannelWrapper> conversations = com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getConversations(userIds, onlyImportant);
            for (android.service.notification.ConversationChannelWrapper conversation : conversations) {
                if (com.android.server.notification.NotificationManagerService.this.mShortcutHelper == null) {
                    conversation.setShortcutInfo((android.content.pm.ShortcutInfo) null);
                } else {
                    conversation.setShortcutInfo(com.android.server.notification.NotificationManagerService.this.mShortcutHelper.getValidShortcutInfo(conversation.getNotificationChannel().getConversationId(), conversation.getPkg(), android.os.UserHandle.of(android.os.UserHandle.getUserId(conversation.getUid()))));
                }
            }
            return new android.content.pm.ParceledListSlice<>(conversations);
        }

        public android.content.pm.ParceledListSlice<android.app.NotificationChannelGroup> getNotificationChannelGroupsForPackage(java.lang.String pkg, int uid, boolean includeDeleted) {
            enforceSystemOrSystemUI("getNotificationChannelGroupsForPackage");
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroups(pkg, uid, includeDeleted, true, false, true, null);
        }

        public android.content.pm.ParceledListSlice<android.app.NotificationChannelGroup> getRecentBlockedNotificationChannelGroupsForPackage(java.lang.String pkg, int uid) {
            java.lang.String channelId;
            enforceSystemOrSystemUI("getRecentBlockedNotificationChannelGroupsForPackage");
            java.util.Set<java.lang.String> recentlySentChannels = new java.util.HashSet<>();
            long now = java.lang.System.currentTimeMillis();
            long startTime = now - 1209600000;
            android.app.usage.UsageEvents events = com.android.server.notification.NotificationManagerService.this.mUsageStatsManagerInternal.queryEventsForUser(android.os.UserHandle.getUserId(uid), startTime, now, 0);
            if (events != null) {
                android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event();
                while (events.hasNextEvent()) {
                    events.getNextEvent(event);
                    if (event.getEventType() == 12 && pkg.equals(event.mPackage) && (channelId = event.mNotificationChannelId) != null) {
                        recentlySentChannels.add(channelId);
                    }
                }
            }
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroups(pkg, uid, false, true, false, true, recentlySentChannels);
        }

        public android.content.pm.ParceledListSlice<android.service.notification.ConversationChannelWrapper> getConversationsForPackage(java.lang.String pkg, int uid) {
            enforceSystemOrSystemUI("getConversationsForPackage");
            java.util.ArrayList<android.service.notification.ConversationChannelWrapper> conversations = com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getConversations(pkg, uid);
            for (android.service.notification.ConversationChannelWrapper conversation : conversations) {
                if (com.android.server.notification.NotificationManagerService.this.mShortcutHelper == null) {
                    conversation.setShortcutInfo((android.content.pm.ShortcutInfo) null);
                } else {
                    conversation.setShortcutInfo(com.android.server.notification.NotificationManagerService.this.mShortcutHelper.getValidShortcutInfo(conversation.getNotificationChannel().getConversationId(), pkg, android.os.UserHandle.of(android.os.UserHandle.getUserId(uid))));
                }
            }
            return new android.content.pm.ParceledListSlice<>(conversations);
        }

        public android.app.NotificationChannelGroup getPopulatedNotificationChannelGroupForPackage(java.lang.String pkg, int uid, java.lang.String groupId, boolean includeDeleted) {
            enforceSystemOrSystemUI("getPopulatedNotificationChannelGroupForPackage");
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroupWithChannels(pkg, uid, groupId, includeDeleted);
        }

        public android.app.NotificationChannelGroup getNotificationChannelGroupForPackage(java.lang.String groupId, java.lang.String pkg, int uid) {
            enforceSystemOrSystemUI("getNotificationChannelGroupForPackage");
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroup(groupId, pkg, uid);
        }

        public android.content.pm.ParceledListSlice<android.app.NotificationChannel> getNotificationChannels(java.lang.String callingPkg, java.lang.String targetPkg, int userId) {
            if (canNotifyAsPackage(callingPkg, targetPkg, userId) || com.android.server.notification.NotificationManagerService.this.isCallingUidSystem()) {
                int targetUid = -1;
                try {
                    targetUid = com.android.server.notification.NotificationManagerService.this.mPackageManagerClient.getPackageUidAsUser(targetPkg, userId);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                }
                if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && !com.android.server.notification.NotificationManagerService.this.isCallingUidSystem()) {
                    return com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().getNotificationChannels(targetPkg, targetUid, false);
                }
                return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannels(targetPkg, targetUid, false);
            }
            throw new java.lang.SecurityException("Pkg " + callingPkg + " cannot read channels for " + targetPkg + " in " + userId);
        }

        public android.content.pm.ParceledListSlice<android.app.NotificationChannel> getNotificationChannelsBypassingDnd(java.lang.String pkg, int uid) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            if (!areNotificationsEnabledForPackage(pkg, uid)) {
                return android.content.pm.ParceledListSlice.emptyList();
            }
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannelsBypassingDnd(pkg, uid);
        }

        public java.util.List<java.lang.String> getPackagesBypassingDnd(int userId, boolean includeConversationChannels) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            android.util.ArraySet<java.lang.String> packageNames = new android.util.ArraySet<>();
            for (int user : com.android.server.notification.NotificationManagerService.this.mUm.getProfileIds(userId, false)) {
                java.util.List<android.content.pm.PackageInfo> pkgs = com.android.server.notification.NotificationManagerService.this.mPackageManagerClient.getInstalledPackagesAsUser(0, user);
                for (android.content.pm.PackageInfo pi : pkgs) {
                    java.lang.String pkg = pi.packageName;
                    for (android.app.NotificationChannel channel : getNotificationChannelsBypassingDnd(pkg, pi.applicationInfo.uid).getList()) {
                        if (includeConversationChannels || android.text.TextUtils.isEmpty(channel.getConversationId()) || channel.isDemoted()) {
                            packageNames.add(pkg);
                        }
                    }
                }
            }
            return new java.util.ArrayList(packageNames);
        }

        public boolean areChannelsBypassingDnd() {
            if (android.app.Flags.modesApi()) {
                return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getConsolidatedNotificationPolicy().allowPriorityChannels() && com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.areChannelsBypassingDnd();
            }
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.areChannelsBypassingDnd();
        }

        public void clearData(java.lang.String packageName, int uid, boolean fromApp) throws android.os.RemoteException {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            int userId = android.os.UserHandle.getUserId(uid);
            if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().cancelAllNotificationsInt(null, com.android.server.notification.NotificationManagerService.MY_UID, com.android.server.notification.NotificationManagerService.MY_PID, packageName, null, 0, 0, userId == 999 ? userId : android.os.UserHandle.getUserId(android.os.Binder.getCallingUid()), 21);
            } else {
                com.android.server.notification.NotificationManagerService.this.cancelAllNotificationsInt(com.android.server.notification.NotificationManagerService.MY_UID, com.android.server.notification.NotificationManagerService.MY_PID, packageName, null, 0, 0, userId == 999 ? userId : android.os.UserHandle.getUserId(android.os.Binder.getCallingUid()), 21);
            }
            boolean packagesChanged = com.android.server.notification.NotificationManagerService.this.mConditionProviders.resetPackage(packageName, userId) | false;
            android.util.ArrayMap<java.lang.Boolean, java.util.ArrayList<android.content.ComponentName>> changedListeners = com.android.server.notification.NotificationManagerService.this.mListeners.resetComponents(packageName, userId);
            boolean packagesChanged2 = packagesChanged | (changedListeners.get(true).size() > 0 || changedListeners.get(false).size() > 0);
            for (int i = 0; i < changedListeners.get(true).size(); i++) {
                com.android.server.notification.NotificationManagerService.this.mConditionProviders.setPackageOrComponentEnabled(changedListeners.get(true).get(i).getPackageName(), userId, false, true);
            }
            android.util.ArrayMap<java.lang.Boolean, java.util.ArrayList<android.content.ComponentName>> changedAssistants = com.android.server.notification.NotificationManagerService.this.mAssistants.resetComponents(packageName, userId);
            boolean packagesChanged3 = packagesChanged2 | (changedAssistants.get(true).size() > 0 || changedAssistants.get(false).size() > 0);
            for (int i2 = 1; i2 < changedAssistants.get(true).size(); i2++) {
                com.android.server.notification.NotificationManagerService.this.mAssistants.setPackageOrComponentEnabled(changedAssistants.get(true).get(i2).flattenToString(), userId, true, false);
            }
            if (changedAssistants.get(true).size() > 0) {
                com.android.server.notification.NotificationManagerService.this.mConditionProviders.setPackageOrComponentEnabled(changedAssistants.get(true).get(0).getPackageName(), userId, false, true);
            }
            com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.clearData(android.os.UserHandle.getUserId(uid), packageName);
            if (!fromApp) {
                com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.clearData(packageName, uid);
            }
            if (packagesChanged3) {
                com.android.server.notification.NotificationManagerService.this.getContext().sendBroadcastAsUser(new android.content.Intent("android.app.action.NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED").setPackage(packageName).addFlags(67108864), android.os.UserHandle.of(userId), null);
            }
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
        }

        public java.util.List<java.lang.String> getAllowedAssistantAdjustments(java.lang.String pkg) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
            if (!com.android.server.notification.NotificationManagerService.this.isCallerSystemOrPhone() && !com.android.server.notification.NotificationManagerService.this.mAssistants.isPackageAllowed(pkg, android.os.UserHandle.getCallingUserId())) {
                throw new java.lang.SecurityException("Not currently an assistant");
            }
            return com.android.server.notification.NotificationManagerService.this.mAssistants.getAllowedAssistantAdjustments();
        }

        @java.lang.Deprecated
        public android.service.notification.StatusBarNotification[] getActiveNotifications(java.lang.String callingPkg) {
            return getActiveNotificationsWithAttribution(callingPkg, null);
        }

        public android.service.notification.StatusBarNotification[] getActiveNotificationsWithAttribution(java.lang.String callingPkg, java.lang.String callingAttributionTag) {
            getActiveNotificationsWithAttribution_enforcePermission();
            java.util.ArrayList<android.service.notification.StatusBarNotification> tmp = new java.util.ArrayList<>();
            int uid = android.os.Binder.getCallingUid();
            final java.util.ArrayList<java.lang.Integer> currentUsers = new java.util.ArrayList<>();
            currentUsers.add(-1);
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$12$$ExternalSyntheticLambda2
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$getActiveNotificationsWithAttribution$0(currentUsers);
                }
            });
            int mode = com.android.server.notification.NotificationManagerService.this.mAppOps.noteOpNoThrow(25, uid, callingPkg, callingAttributionTag, (java.lang.String) null);
            if (mode == 0 || mode == 3) {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    int N = com.android.server.notification.NotificationManagerService.this.mNotificationList.size();
                    for (int i = 0; i < N; i++) {
                        android.service.notification.StatusBarNotification sbn = com.android.server.notification.NotificationManagerService.this.mNotificationList.get(i).getSbn();
                        if (currentUsers.contains(java.lang.Integer.valueOf(sbn.getUserId()))) {
                            tmp.add(sbn);
                        }
                    }
                }
            }
            return (android.service.notification.StatusBarNotification[]) tmp.toArray(new android.service.notification.StatusBarNotification[tmp.size()]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getActiveNotificationsWithAttribution$0(java.util.ArrayList currentUsers) throws java.lang.Exception {
            for (int user : com.android.server.notification.NotificationManagerService.this.mUm.getProfileIds(android.app.ActivityManager.getCurrentUser(), false)) {
                currentUsers.add(java.lang.Integer.valueOf(user));
            }
        }

        public android.content.pm.ParceledListSlice<android.service.notification.StatusBarNotification> getAppActiveNotifications(java.lang.String pkg, int incomingUserId) {
            android.content.pm.ParceledListSlice<android.service.notification.StatusBarNotification> parceledListSlice;
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
            int userId = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), incomingUserId, true, false, "getAppActiveNotifications", pkg);
            com.android.server.notification.NotificationManagerService.mNMSExt.fixStopForegroundRemoveFlagSlow();
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                android.util.ArrayMap<java.lang.String, android.service.notification.StatusBarNotification> map = new android.util.ArrayMap<>(com.android.server.notification.NotificationManagerService.this.mNotificationList.size() + com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.size());
                int N = com.android.server.notification.NotificationManagerService.this.mNotificationList.size();
                for (int i = 0; i < N; i++) {
                    android.service.notification.StatusBarNotification sbn = sanitizeSbn(pkg, userId, com.android.server.notification.NotificationManagerService.this.mNotificationList.get(i).getSbn());
                    if (sbn != null) {
                        map.put(sbn.getKey(), sbn);
                    }
                }
                for (com.android.server.notification.NotificationRecord snoozed : com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.getSnoozed(userId, pkg)) {
                    android.service.notification.StatusBarNotification sbn2 = sanitizeSbn(pkg, userId, snoozed.getSbn());
                    if (sbn2 != null) {
                        map.put(sbn2.getKey(), sbn2);
                    }
                }
                int M = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.size();
                for (int i2 = 0; i2 < M; i2++) {
                    android.service.notification.StatusBarNotification sbn3 = sanitizeSbn(pkg, userId, com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.get(i2).getSbn());
                    if (sbn3 != null) {
                        map.put(sbn3.getKey(), sbn3);
                    }
                }
                java.util.ArrayList<android.service.notification.StatusBarNotification> list = new java.util.ArrayList<>(map.size());
                list.addAll(map.values());
                parceledListSlice = new android.content.pm.ParceledListSlice<>(list);
            }
            return parceledListSlice;
        }

        private android.service.notification.StatusBarNotification sanitizeSbn(java.lang.String pkg, int userId, android.service.notification.StatusBarNotification sbn) {
            if (sbn.getUserId() != userId || (!sbn.getPackageName().equals(pkg) && !sbn.getOpPkg().equals(pkg))) {
                return null;
            }
            android.app.Notification notification = sbn.getNotification().clone();
            notification.overrideAllowlistToken(null);
            return new android.service.notification.StatusBarNotification(sbn.getPackageName(), sbn.getOpPkg(), sbn.getId(), sbn.getTag(), sbn.getUid(), sbn.getInitialPid(), notification, sbn.getUser(), sbn.getOverrideGroupKey(), sbn.getPostTime());
        }

        @java.lang.Deprecated
        public android.service.notification.StatusBarNotification[] getHistoricalNotifications(java.lang.String callingPkg, int count, boolean includeSnoozed) {
            return getHistoricalNotificationsWithAttribution(callingPkg, null, count, includeSnoozed);
        }

        public android.service.notification.StatusBarNotification[] getHistoricalNotificationsWithAttribution(java.lang.String callingPkg, java.lang.String callingAttributionTag, int count, boolean includeSnoozed) {
            getHistoricalNotificationsWithAttribution_enforcePermission();
            android.service.notification.StatusBarNotification[] tmp = null;
            int uid = android.os.Binder.getCallingUid();
            int mode = com.android.server.notification.NotificationManagerService.this.mAppOps.noteOpNoThrow(25, uid, callingPkg, callingAttributionTag, (java.lang.String) null);
            if (mode == 0 || mode == 3) {
                synchronized (com.android.server.notification.NotificationManagerService.this.mArchive) {
                    tmp = com.android.server.notification.NotificationManagerService.this.mArchive.getArray(com.android.server.notification.NotificationManagerService.this.mUm, count, includeSnoozed);
                }
            }
            return tmp;
        }

        public android.app.NotificationHistory getNotificationHistory(java.lang.String callingPkg, java.lang.String callingAttributionTag) {
            getNotificationHistory_enforcePermission();
            int uid = android.os.Binder.getCallingUid();
            int mode = com.android.server.notification.NotificationManagerService.this.mAppOps.noteOpNoThrow(25, uid, callingPkg, callingAttributionTag, (java.lang.String) null);
            if (mode == 0 || mode == 3) {
                android.util.IntArray currentUserIds = com.android.server.notification.NotificationManagerService.this.mUserProfiles.getCurrentProfileIds();
                android.os.Trace.traceBegin(524288L, "notifHistoryReadHistory");
                try {
                    return com.android.server.notification.NotificationManagerService.this.mHistoryManager.readNotificationHistory(currentUserIds.toArray());
                } finally {
                    android.os.Trace.traceEnd(524288L);
                }
            }
            return new android.app.NotificationHistory();
        }

        public void registerCallNotificationEventListener(java.lang.String packageName, android.os.UserHandle userHandle, android.app.ICallNotificationEventCallback listener) {
            registerCallNotificationEventListener_enforcePermission();
            int userId = userHandle.getIdentifier() != -2 ? userHandle.getIdentifier() : com.android.server.notification.NotificationManagerService.this.mAmi.getCurrentUserId();
            synchronized (com.android.server.notification.NotificationManagerService.this.mCallNotificationEventCallbacks) {
                android.util.ArrayMap<java.lang.Integer, android.os.RemoteCallbackList<android.app.ICallNotificationEventCallback>> callbacksForPackage = com.android.server.notification.NotificationManagerService.this.mCallNotificationEventCallbacks.getOrDefault(packageName, new android.util.ArrayMap<>());
                android.os.RemoteCallbackList<android.app.ICallNotificationEventCallback> callbackList = callbacksForPackage.getOrDefault(java.lang.Integer.valueOf(userId), new android.os.RemoteCallbackList<>());
                if (callbackList.register(listener)) {
                    callbacksForPackage.put(java.lang.Integer.valueOf(userId), callbackList);
                    com.android.server.notification.NotificationManagerService.this.mCallNotificationEventCallbacks.put(packageName, callbacksForPackage);
                    synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                        for (com.android.server.notification.NotificationRecord r : com.android.server.notification.NotificationManagerService.this.mNotificationList) {
                            if (r.getNotification().isStyle(android.app.Notification.CallStyle.class) && com.android.server.notification.NotificationManagerService.this.notificationMatchesUserId(r, userId, false) && r.getSbn().getPackageName().equals(packageName)) {
                                try {
                                    listener.onCallNotificationPosted(packageName, r.getUser());
                                } catch (android.os.RemoteException e) {
                                    throw new java.lang.RuntimeException(e);
                                }
                            }
                        }
                    }
                    return;
                }
                android.util.Log.e(com.android.server.notification.NotificationManagerService.TAG, "registerCallNotificationEventListener failed to register listener: " + packageName + " " + userHandle + " " + listener);
            }
        }

        public void unregisterCallNotificationEventListener(java.lang.String packageName, android.os.UserHandle userHandle, android.app.ICallNotificationEventCallback listener) {
            unregisterCallNotificationEventListener_enforcePermission();
            synchronized (com.android.server.notification.NotificationManagerService.this.mCallNotificationEventCallbacks) {
                int userId = userHandle.getIdentifier() != -2 ? userHandle.getIdentifier() : com.android.server.notification.NotificationManagerService.this.mAmi.getCurrentUserId();
                android.util.ArrayMap<java.lang.Integer, android.os.RemoteCallbackList<android.app.ICallNotificationEventCallback>> callbacksForPackage = com.android.server.notification.NotificationManagerService.this.mCallNotificationEventCallbacks.get(packageName);
                if (callbacksForPackage == null) {
                    return;
                }
                android.os.RemoteCallbackList<android.app.ICallNotificationEventCallback> callbackList = callbacksForPackage.get(java.lang.Integer.valueOf(userId));
                if (callbackList == null) {
                    return;
                }
                if (!callbackList.unregister(listener)) {
                    android.util.Log.e(com.android.server.notification.NotificationManagerService.TAG, "unregisterCallNotificationEventListener listener not found for: " + packageName + " " + userHandle + " " + listener);
                }
            }
        }

        public void registerListener(android.service.notification.INotificationListener listener, android.content.ComponentName component, int userid) {
            enforceSystemOrSystemUI("INotificationManager.registerListener");
            com.android.server.notification.NotificationManagerService.this.mListeners.registerSystemService(listener, component, userid, android.os.Binder.getCallingUid());
        }

        public void unregisterListener(android.service.notification.INotificationListener token, int userid) {
            com.android.server.notification.NotificationManagerService.this.mListeners.unregisterService((android.os.IInterface) token, userid);
        }

        public void cancelNotificationsFromListener(android.service.notification.INotificationListener token, java.lang.String[] keys) throws java.lang.Throwable {
            com.android.server.notification.ManagedServices.ManagedServiceInfo info;
            java.lang.String pkg;
            java.lang.Object obj;
            java.lang.String pkg2;
            boolean notificationsRapidlyCleared;
            int userId;
            int i;
            boolean z;
            int N;
            int packageImportance;
            java.lang.String pkg3;
            com.android.server.notification.ManagedServices.ManagedServiceInfo info2;
            java.lang.String[] strArr = keys;
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            long identity = android.os.Binder.clearCallingIdentity();
            boolean notificationsRapidlyCleared2 = false;
            try {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    info = com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token);
                    pkg = info.component.getPackageName();
                }
                int packageImportance2 = android.app.Flags.lifetimeExtensionRefactor() ? com.android.server.notification.NotificationManagerService.this.getPackageImportanceWithIdentity(pkg) : 0;
                java.lang.Object obj2 = com.android.server.notification.NotificationManagerService.this.mNotificationLock;
                synchronized (obj2) {
                    try {
                        try {
                            int reason = com.android.server.notification.NotificationManagerService.this.mAssistants.isServiceTokenValidLocked(token) ? 22 : 10;
                            if (strArr != null) {
                                int N2 = strArr.length;
                                int i2 = 0;
                                while (i2 < N2) {
                                    com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(strArr[i2]);
                                    if (r != null) {
                                        int userId2 = r.getSbn().getUserId();
                                        try {
                                            try {
                                                if (userId2 != info.userid && userId2 != -1) {
                                                    try {
                                                        if (!com.android.server.notification.NotificationManagerService.this.mUserProfiles.isCurrentProfile(userId2)) {
                                                        }
                                                        i2 = i + 1;
                                                        packageImportance2 = packageImportance;
                                                        info = info2;
                                                        obj2 = obj;
                                                        pkg = pkg3;
                                                        N2 = N;
                                                        strArr = keys;
                                                    } catch (java.lang.Throwable th) {
                                                        th = th;
                                                        obj = obj2;
                                                        throw th;
                                                    }
                                                }
                                                if (notificationsRapidlyCleared2) {
                                                    userId = userId2;
                                                    i = i2;
                                                } else {
                                                    userId = userId2;
                                                    i = i2;
                                                    if (!com.android.server.notification.NotificationManagerService.this.isNotificationRecent(r.getUpdateTimeMs())) {
                                                        z = false;
                                                    }
                                                    boolean notificationsRapidlyCleared3 = z;
                                                    N = N2;
                                                    obj = obj2;
                                                    packageImportance = packageImportance2;
                                                    pkg3 = pkg;
                                                    info2 = info;
                                                    cancelNotificationFromListenerLocked(info, callingUid, callingPid, r.getSbn().getPackageName(), r.getSbn().getTag(), r.getSbn().getId(), userId, reason);
                                                    notificationsRapidlyCleared2 = notificationsRapidlyCleared3;
                                                    i2 = i + 1;
                                                    packageImportance2 = packageImportance;
                                                    info = info2;
                                                    obj2 = obj;
                                                    pkg = pkg3;
                                                    N2 = N;
                                                    strArr = keys;
                                                }
                                                cancelNotificationFromListenerLocked(info, callingUid, callingPid, r.getSbn().getPackageName(), r.getSbn().getTag(), r.getSbn().getId(), userId, reason);
                                                notificationsRapidlyCleared2 = notificationsRapidlyCleared3;
                                                i2 = i + 1;
                                                packageImportance2 = packageImportance;
                                                info = info2;
                                                obj2 = obj;
                                                pkg = pkg3;
                                                N2 = N;
                                                strArr = keys;
                                            } catch (java.lang.Throwable th2) {
                                                th = th2;
                                                throw th;
                                            }
                                            N = N2;
                                            obj = obj2;
                                            packageImportance = packageImportance2;
                                            pkg3 = pkg;
                                            info2 = info;
                                        } catch (java.lang.Throwable th3) {
                                            th = th3;
                                            obj = obj2;
                                        }
                                        z = true;
                                        boolean notificationsRapidlyCleared32 = z;
                                    }
                                    i = i2;
                                    N = N2;
                                    obj = obj2;
                                    packageImportance = packageImportance2;
                                    pkg3 = pkg;
                                    info2 = info;
                                    i2 = i + 1;
                                    packageImportance2 = packageImportance;
                                    info = info2;
                                    obj2 = obj;
                                    pkg = pkg3;
                                    N2 = N;
                                    strArr = keys;
                                }
                                obj = obj2;
                                pkg2 = pkg;
                                notificationsRapidlyCleared = notificationsRapidlyCleared2;
                            } else {
                                obj = obj2;
                                int packageImportance3 = packageImportance2;
                                pkg2 = pkg;
                                java.util.Iterator<com.android.server.notification.NotificationRecord> it = com.android.server.notification.NotificationManagerService.this.mNotificationList.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        notificationsRapidlyCleared = false;
                                        break;
                                    }
                                    com.android.server.notification.NotificationRecord notificationRecord = it.next();
                                    if (com.android.server.notification.NotificationManagerService.this.isNotificationRecent(notificationRecord.getUpdateTimeMs())) {
                                        notificationsRapidlyCleared = true;
                                        break;
                                    }
                                }
                                try {
                                    boolean notificationsRapidlyCleared4 = android.app.Flags.lifetimeExtensionRefactor();
                                    if (notificationsRapidlyCleared4) {
                                        com.android.server.notification.NotificationManagerService.this.cancelAllLocked(callingUid, callingPid, info.userid, 11, info, info.supportsProfiles(), 65570);
                                        com.android.server.notification.NotificationManagerService.this.maybeNotifySystemUiListenerLifetimeExtendedListLocked(com.android.server.notification.NotificationManagerService.this.mNotificationList, packageImportance3);
                                        com.android.server.notification.NotificationManagerService.this.maybeNotifySystemUiListenerLifetimeExtendedListLocked(com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications, packageImportance3);
                                    } else {
                                        com.android.server.notification.NotificationManagerService.this.cancelAllLocked(callingUid, callingPid, info.userid, 11, info, info.supportsProfiles(), 34);
                                    }
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                    throw th;
                                }
                            }
                            if (notificationsRapidlyCleared) {
                                try {
                                    com.android.server.notification.NotificationManagerService.this.mAppOps.noteOpNoThrow(142, callingUid, pkg2, (java.lang.String) null, (java.lang.String) null);
                                } catch (java.lang.Throwable th5) {
                                    th = th5;
                                    android.os.Binder.restoreCallingIdentity(identity);
                                    throw th;
                                }
                            }
                            android.os.Binder.restoreCallingIdentity(identity);
                        } catch (java.lang.Throwable th6) {
                            th = th6;
                        }
                    } catch (java.lang.Throwable th7) {
                        th = th7;
                        obj = obj2;
                    }
                }
            } catch (java.lang.Throwable th8) {
                th = th8;
            }
        }

        public void requestBindListener(android.content.ComponentName component) {
            com.android.server.notification.ManagedServices manager;
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(component.getPackageName());
            int uid = android.os.Binder.getCallingUid();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.server.notification.NotificationManagerService.this.mAssistants.isComponentEnabledForCurrentProfiles(component)) {
                    manager = com.android.server.notification.NotificationManagerService.this.mAssistants;
                } else {
                    manager = com.android.server.notification.NotificationManagerService.this.mListeners;
                }
                manager.setComponentState(component, android.os.UserHandle.getUserId(uid), true);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void requestUnbindListener(android.service.notification.INotificationListener token) {
            int uid = android.os.Binder.getCallingUid();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.ManagedServices.ManagedServiceInfo info = com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token);
                    info.getOwner().setComponentState(info.component, android.os.UserHandle.getUserId(uid), false);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void requestUnbindListenerComponent(android.content.ComponentName component) {
            com.android.server.notification.ManagedServices manager;
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSameApp(component.getPackageName());
            int uid = android.os.Binder.getCallingUid();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    if (com.android.server.notification.NotificationManagerService.this.mAssistants.isComponentEnabledForCurrentProfiles(component)) {
                        manager = com.android.server.notification.NotificationManagerService.this.mAssistants;
                    } else {
                        manager = com.android.server.notification.NotificationManagerService.this.mListeners;
                    }
                    if (manager.isPackageOrComponentAllowed(component.flattenToString(), android.os.UserHandle.getUserId(uid))) {
                        manager.setComponentState(component, android.os.UserHandle.getUserId(uid), false);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setNotificationsShownFromListener(android.service.notification.INotificationListener token, java.lang.String[] keys) {
            int userId;
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.ManagedServices.ManagedServiceInfo info = com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token);
                    if (keys == null) {
                        return;
                    }
                    java.util.ArrayList<com.android.server.notification.NotificationRecord> seen = new java.util.ArrayList<>();
                    int n = keys.length;
                    for (int i = 0; i < n; i++) {
                        com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(keys[i]);
                        if (r != null && ((userId = r.getSbn().getUserId()) == info.userid || userId == -1 || com.android.server.notification.NotificationManagerService.this.mUserProfiles.isCurrentProfile(userId))) {
                            seen.add(r);
                            if (!r.isSeen()) {
                                if (com.android.server.notification.NotificationManagerService.DBG) {
                                    android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, "Marking notification as seen " + keys[i]);
                                }
                                com.android.server.notification.NotificationManagerService.this.reportSeen(r);
                                r.setSeen();
                                com.android.server.notification.NotificationManagerService.this.maybeRecordInterruptionLocked(r);
                            }
                        }
                    }
                    if (!seen.isEmpty()) {
                        com.android.server.notification.NotificationManagerService.this.mAssistants.onNotificationsSeenLocked(seen);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private void cancelNotificationFromListenerLocked(com.android.server.notification.ManagedServices.ManagedServiceInfo info, int callingUid, int callingPid, java.lang.String pkg, java.lang.String tag, int id, int userId, int reason) {
            int mustNotHaveFlags = android.app.Flags.lifetimeExtensionRefactor() ? 2 | 65536 : 2;
            com.android.server.notification.NotificationManagerService.this.cancelNotification(callingUid, callingPid, pkg, tag, id, 0, mustNotHaveFlags, true, userId, reason, info);
        }

        public void snoozeNotificationUntilContextFromListener(android.service.notification.INotificationListener token, java.lang.String key, java.lang.String snoozeCriterionId) {
            int callingUid = android.os.Binder.getCallingUid();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.notification.NotificationManagerService.this.snoozeNotificationInt(callingUid, token, key, -1L, snoozeCriterionId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void snoozeNotificationUntilFromListener(android.service.notification.INotificationListener token, java.lang.String key, long duration) {
            int callingUid = android.os.Binder.getCallingUid();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.notification.NotificationManagerService.this.snoozeNotificationInt(callingUid, token, key, duration, null);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void unsnoozeNotificationFromAssistant(android.service.notification.INotificationListener token, java.lang.String key) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.ManagedServices.ManagedServiceInfo info = com.android.server.notification.NotificationManagerService.this.mAssistants.checkServiceTokenLocked(token);
                    com.android.server.notification.NotificationManagerService.this.unsnoozeNotificationInt(key, info, false);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void unsnoozeNotificationFromSystemListener(android.service.notification.INotificationListener token, java.lang.String key) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.ManagedServices.ManagedServiceInfo info = com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token);
                    if (!info.isSystem) {
                        throw new java.lang.SecurityException("Not allowed to unsnooze before deadline");
                    }
                    com.android.server.notification.NotificationManagerService.this.unsnoozeNotificationInt(key, info, true);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void migrateNotificationFilter(android.service.notification.INotificationListener token, int defaultTypes, java.util.List<java.lang.String> disallowedApps) throws java.lang.Throwable {
            android.service.notification.NotificationListenerFilter nlf;
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                        try {
                        } catch (java.lang.Throwable th) {
                            th = th;
                        }
                        try {
                            com.android.server.notification.ManagedServices.ManagedServiceInfo info = com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token);
                            android.util.Pair<android.content.ComponentName, java.lang.Integer> pairCreate = android.util.Pair.create(info.component, java.lang.Integer.valueOf(info.userid));
                            android.service.notification.NotificationListenerFilter nlf2 = com.android.server.notification.NotificationManagerService.this.mListeners.getNotificationListenerFilter(pairCreate);
                            if (nlf2 != null) {
                                nlf = nlf2;
                            } else {
                                nlf = new android.service.notification.NotificationListenerFilter();
                            }
                            if (nlf.getDisallowedPackages().isEmpty() && disallowedApps != null) {
                                for (java.lang.String pkg : disallowedApps) {
                                    for (int userId : com.android.server.notification.NotificationManagerService.this.mUm.getProfileIds(info.userid, false)) {
                                        try {
                                            int uid = getUidForPackageAndUser(pkg, android.os.UserHandle.of(userId));
                                            if (uid != -1) {
                                                android.content.pm.VersionedPackage vp = new android.content.pm.VersionedPackage(pkg, uid);
                                                nlf.addPackage(vp);
                                            }
                                        } catch (java.lang.Exception e) {
                                        }
                                    }
                                }
                            }
                            if (nlf.areAllTypesAllowed()) {
                                nlf.setTypes(defaultTypes);
                            }
                            com.android.server.notification.NotificationManagerService.this.mListeners.setNotificationListenerFilter(pairCreate, nlf);
                            android.os.Binder.restoreCallingIdentity(identity);
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            try {
                                throw th;
                            } catch (java.lang.Throwable th3) {
                                th = th3;
                                android.os.Binder.restoreCallingIdentity(identity);
                                throw th;
                            }
                        }
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                }
            } catch (java.lang.Throwable th5) {
                th = th5;
            }
        }

        public void cancelNotificationFromListener(android.service.notification.INotificationListener token, java.lang.String pkg, java.lang.String tag, int id) {
            android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "Ignoring deprecated cancelNotification(pkg, tag, id) use cancelNotification(key) instead.");
        }

        public android.content.pm.ParceledListSlice<android.service.notification.StatusBarNotification> getActiveNotificationsFromListener(android.service.notification.INotificationListener token, java.lang.String[] keys, int trim) {
            android.content.pm.ParceledListSlice<android.service.notification.StatusBarNotification> parceledListSlice;
            com.android.server.notification.NotificationRecord r;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.ManagedServices.ManagedServiceInfo info = com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token);
                boolean getKeys = keys != null;
                int N = getKeys ? keys.length : com.android.server.notification.NotificationManagerService.this.mNotificationList.size();
                java.util.ArrayList<android.service.notification.StatusBarNotification> list = new java.util.ArrayList<>(N);
                for (int i = 0; i < N; i++) {
                    if (getKeys) {
                        r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(keys[i]);
                    } else {
                        r = com.android.server.notification.NotificationManagerService.this.mNotificationList.get(i);
                    }
                    addToListIfNeeded(r, info, list, trim);
                }
                parceledListSlice = new android.content.pm.ParceledListSlice<>(list);
            }
            return parceledListSlice;
        }

        public android.content.pm.ParceledListSlice<android.service.notification.StatusBarNotification> getSnoozedNotificationsFromListener(android.service.notification.INotificationListener token, int trim) {
            android.content.pm.ParceledListSlice<android.service.notification.StatusBarNotification> parceledListSlice;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.ManagedServices.ManagedServiceInfo info = com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token);
                java.util.List<com.android.server.notification.NotificationRecord> snoozedRecords = com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.getSnoozed();
                int N = snoozedRecords.size();
                java.util.ArrayList<android.service.notification.StatusBarNotification> list = new java.util.ArrayList<>(N);
                for (int i = 0; i < N; i++) {
                    addToListIfNeeded(snoozedRecords.get(i), info, list, trim);
                }
                parceledListSlice = new android.content.pm.ParceledListSlice<>(list);
            }
            return parceledListSlice;
        }

        private void addToListIfNeeded(com.android.server.notification.NotificationRecord r, com.android.server.notification.ManagedServices.ManagedServiceInfo info, java.util.ArrayList<android.service.notification.StatusBarNotification> notifications, int trim) {
            if (r == null) {
                return;
            }
            android.service.notification.StatusBarNotification sbn = r.getSbn();
            if (com.android.server.notification.NotificationManagerService.this.isVisibleToListener(sbn, r.getNotificationType(), info)) {
                if (com.android.server.notification.NotificationManagerService.this.mListeners.hasSensitiveContent(r) && !com.android.server.notification.NotificationManagerService.this.mListeners.isUidTrusted(info.uid)) {
                    notifications.add(com.android.server.notification.NotificationManagerService.this.mListeners.redactStatusBarNotification(sbn));
                } else {
                    notifications.add(trim == 0 ? sbn : sbn.cloneLight());
                }
            }
        }

        public void clearRequestedListenerHints(android.service.notification.INotificationListener token) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.ManagedServices.ManagedServiceInfo info = com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token);
                    com.android.server.notification.NotificationManagerService.this.removeDisabledHints(info);
                    com.android.server.notification.NotificationManagerService.this.updateListenerHintsLocked();
                    com.android.server.notification.NotificationManagerService.this.updateEffectsSuppressorLocked();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void requestHintsFromListener(android.service.notification.INotificationListener token, int hints) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.ManagedServices.ManagedServiceInfo info = com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token);
                    boolean disableEffects = (hints & 7) != 0;
                    if (disableEffects) {
                        com.android.server.notification.NotificationManagerService.this.addDisabledHints(info, hints);
                    } else {
                        com.android.server.notification.NotificationManagerService.this.removeDisabledHints(info, hints);
                    }
                    com.android.server.notification.NotificationManagerService.this.updateListenerHintsLocked();
                    com.android.server.notification.NotificationManagerService.this.updateEffectsSuppressorLocked();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public int getHintsFromListener(android.service.notification.INotificationListener token) {
            int i;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                i = com.android.server.notification.NotificationManagerService.this.mListenerHints;
            }
            return i;
        }

        public int getHintsFromListenerNoToken() {
            int i;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                i = com.android.server.notification.NotificationManagerService.this.mListenerHints;
            }
            return i;
        }

        public void requestInterruptionFilterFromListener(android.service.notification.INotificationListener token, int interruptionFilter) throws android.os.RemoteException {
            final com.android.server.notification.ManagedServices.ManagedServiceInfo info;
            if (android.app.Flags.modesApi()) {
                final int callingUid = android.os.Binder.getCallingUid();
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    info = com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token);
                }
                final int zenMode = android.app.NotificationManager.zenModeFromInterruptionFilter(interruptionFilter, -1);
                if (zenMode == -1) {
                    return;
                }
                if (!canManageGlobalZenPolicy(info.component.getPackageName(), callingUid)) {
                    com.android.server.notification.NotificationManagerService.this.mZenModeHelper.applyGlobalZenModeAsImplicitZenRule(info.component.getPackageName(), callingUid, zenMode);
                    return;
                } else {
                    final int origin = computeZenOrigin(false);
                    android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$12$$ExternalSyntheticLambda1
                        public final void runOrThrow() throws java.lang.Exception {
                            this.f$0.lambda$requestInterruptionFilterFromListener$1(zenMode, origin, info, callingUid);
                        }
                    });
                    return;
                }
            }
            int callingUid2 = android.os.Binder.getCallingUid();
            boolean isSystemOrSystemUi = com.android.server.notification.NotificationManagerService.this.isCallerSystemOrSystemUi();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.NotificationManagerService.this.mZenModeHelper.requestFromListener(com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token).component, interruptionFilter, callingUid2, isSystemOrSystemUi);
                    com.android.server.notification.NotificationManagerService.this.updateInterruptionFilterLocked();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$requestInterruptionFilterFromListener$1(int zenMode, int origin, com.android.server.notification.ManagedServices.ManagedServiceInfo info, int callingUid) throws java.lang.Exception {
            com.android.server.notification.NotificationManagerService.this.mZenModeHelper.setManualZenMode(zenMode, null, origin, "listener:" + info.component.flattenToShortString(), info.component.getPackageName(), callingUid);
        }

        public int getInterruptionFilterFromListener(android.service.notification.INotificationListener token) throws android.os.RemoteException {
            int i;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                i = com.android.server.notification.NotificationManagerService.this.mInterruptionFilter;
            }
            return i;
        }

        public void setOnNotificationPostedTrimFromListener(android.service.notification.INotificationListener token, int trim) throws android.os.RemoteException {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.ManagedServices.ManagedServiceInfo info = com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token);
                if (info == null) {
                    return;
                }
                com.android.server.notification.NotificationManagerService.this.mListeners.setOnNotificationPostedTrimLocked(info, trim);
            }
        }

        public int getZenMode() {
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getZenMode();
        }

        public android.service.notification.ZenModeConfig getZenModeConfig() {
            enforceSystemOrSystemUI("INotificationManager.getZenModeConfig");
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getConfig();
        }

        public void setZenMode(int mode, android.net.Uri conditionId, java.lang.String reason, boolean fromUser) {
            java.lang.String pkg;
            enforceSystemOrSystemUI("INotificationManager.setZenMode");
            enforceUserOriginOnlyFromSystem(fromUser, "setZenMode");
            int callingUid = android.os.Binder.getCallingUid();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() == null) {
                    pkg = null;
                } else {
                    java.lang.String pkg2 = com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().getZenModePackageName(reason);
                    pkg = pkg2;
                }
                com.android.server.notification.NotificationManagerService.this.mZenModeHelper.setManualZenMode(mode, conditionId, computeZenOrigin(fromUser), reason, pkg, callingUid);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.util.List<android.service.notification.ZenModeConfig.ZenRule> getZenRules() throws android.os.RemoteException {
            enforcePolicyAccess(android.os.Binder.getCallingUid(), "getZenRules");
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getZenRules();
        }

        public java.util.Map<java.lang.String, android.app.AutomaticZenRule> getAutomaticZenRules() {
            if (!android.app.Flags.modesApi()) {
                throw new java.lang.IllegalStateException("getAutomaticZenRules called with flag off!");
            }
            enforcePolicyAccess(android.os.Binder.getCallingUid(), "getAutomaticZenRules");
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getAutomaticZenRules();
        }

        public android.app.AutomaticZenRule getAutomaticZenRule(java.lang.String id) throws android.os.RemoteException {
            java.util.Objects.requireNonNull(id, "Id is null");
            enforcePolicyAccess(android.os.Binder.getCallingUid(), "getAutomaticZenRule");
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getAutomaticZenRule(id);
        }

        public java.lang.String addAutomaticZenRule(android.app.AutomaticZenRule automaticZenRule, java.lang.String pkg, boolean fromUser) {
            validateAutomaticZenRule(null, automaticZenRule);
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSameApp(pkg);
            if (automaticZenRule.getZenPolicy() != null && automaticZenRule.getInterruptionFilter() != 2) {
                throw new java.lang.IllegalArgumentException("ZenPolicy is only applicable to INTERRUPTION_FILTER_PRIORITY filters");
            }
            enforcePolicyAccess(android.os.Binder.getCallingUid(), "addAutomaticZenRule");
            enforceUserOriginOnlyFromSystem(fromUser, "addAutomaticZenRule");
            java.lang.String rulePkg = pkg;
            if (com.android.server.notification.NotificationManagerService.this.isCallingAppIdSystem() && automaticZenRule.getOwner() != null) {
                rulePkg = automaticZenRule.getOwner().getPackageName();
            }
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.addAutomaticZenRule(rulePkg, automaticZenRule, computeZenOrigin(fromUser), "addAutomaticZenRule", android.os.Binder.getCallingUid());
        }

        public void setManualZenRuleDeviceEffects(android.service.notification.ZenDeviceEffects effects) throws android.os.RemoteException {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            com.android.server.notification.NotificationManagerService.this.mZenModeHelper.setManualZenRuleDeviceEffects(effects, computeZenOrigin(true), "Update manual mode non-policy settings", android.os.Binder.getCallingUid());
        }

        public boolean updateAutomaticZenRule(java.lang.String id, android.app.AutomaticZenRule automaticZenRule, boolean fromUser) throws android.os.RemoteException {
            validateAutomaticZenRule(id, automaticZenRule);
            enforcePolicyAccess(android.os.Binder.getCallingUid(), "updateAutomaticZenRule");
            enforceUserOriginOnlyFromSystem(fromUser, "updateAutomaticZenRule");
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.updateAutomaticZenRule(id, automaticZenRule, computeZenOrigin(fromUser), "updateAutomaticZenRule", android.os.Binder.getCallingUid());
        }

        private void validateAutomaticZenRule(java.lang.String updateId, android.app.AutomaticZenRule rule) {
            java.util.Objects.requireNonNull(rule, "automaticZenRule is null");
            java.util.Objects.requireNonNull(rule.getName(), "Name is null");
            rule.validate();
            if (android.app.Flags.modesApi()) {
                boolean isImplicitRuleUpdateFromSystem = updateId != null && com.android.server.notification.ZenModeHelper.isImplicitRuleId(updateId) && com.android.server.notification.NotificationManagerService.this.isCallerSystemOrSystemUi();
                if (!isImplicitRuleUpdateFromSystem && rule.getOwner() == null && rule.getConfigurationActivity() == null) {
                    throw new java.lang.NullPointerException("Rule must have a ConditionProviderService and/or configuration activity");
                }
            } else if (rule.getOwner() == null && rule.getConfigurationActivity() == null) {
                throw new java.lang.NullPointerException("Rule must have a ConditionProviderService and/or configuration activity");
            }
            java.util.Objects.requireNonNull(rule.getConditionId(), "ConditionId is null");
            if (!android.app.Flags.modesApi() || com.android.server.notification.NotificationManagerService.this.isCallerSystemOrSystemUi()) {
                return;
            }
            final int uid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserId(uid);
            if (rule.getType() == 7) {
                boolean isDeviceOwner = ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.notification.NotificationManagerService$12$$ExternalSyntheticLambda3
                    public final java.lang.Object getOrThrow() {
                        return this.f$0.lambda$validateAutomaticZenRule$2(uid);
                    }
                })).booleanValue();
                if (!isDeviceOwner) {
                    throw new java.lang.IllegalArgumentException("Only Device Owners can use AutomaticZenRules with TYPE_MANAGED");
                }
            } else if (rule.getType() == 3) {
                java.lang.String wellbeingPackage = com.android.server.notification.NotificationManagerService.this.getContext().getResources().getString(android.R.string.config_systemWellbeing);
                boolean isCallerWellbeing = !android.text.TextUtils.isEmpty(wellbeingPackage) && com.android.server.notification.NotificationManagerService.this.mPackageManagerInternal.isSameApp(wellbeingPackage, uid, userId);
                if (!isCallerWellbeing) {
                    throw new java.lang.IllegalArgumentException("Only the 'Wellbeing' package can use AutomaticZenRules with TYPE_BEDTIME");
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ java.lang.Boolean lambda$validateAutomaticZenRule$2(int uid) throws java.lang.Exception {
            return java.lang.Boolean.valueOf(com.android.server.notification.NotificationManagerService.this.mDpm.isActiveDeviceOwner(uid));
        }

        public boolean removeAutomaticZenRule(java.lang.String id, boolean fromUser) throws android.os.RemoteException {
            java.util.Objects.requireNonNull(id, "Id is null");
            enforcePolicyAccess(android.os.Binder.getCallingUid(), "removeAutomaticZenRule");
            enforceUserOriginOnlyFromSystem(fromUser, "removeAutomaticZenRule");
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.removeAutomaticZenRule(id, computeZenOrigin(fromUser), "removeAutomaticZenRule", android.os.Binder.getCallingUid());
        }

        public boolean removeAutomaticZenRules(java.lang.String packageName, boolean fromUser) throws android.os.RemoteException {
            java.util.Objects.requireNonNull(packageName, "Package name is null");
            enforceSystemOrSystemUI("removeAutomaticZenRules");
            enforceUserOriginOnlyFromSystem(fromUser, "removeAutomaticZenRules");
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.removeAutomaticZenRules(packageName, computeZenOrigin(fromUser), packageName + "|removeAutomaticZenRules", android.os.Binder.getCallingUid());
        }

        public int getRuleInstanceCount(android.content.ComponentName owner) throws android.os.RemoteException {
            java.util.Objects.requireNonNull(owner, "Owner is null");
            enforceSystemOrSystemUI("getRuleInstanceCount");
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getCurrentInstanceCount(owner);
        }

        public int getAutomaticZenRuleState(java.lang.String id) {
            java.util.Objects.requireNonNull(id, "id is null");
            enforcePolicyAccess(android.os.Binder.getCallingUid(), "getAutomaticZenRuleState");
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getAutomaticZenRuleState(id);
        }

        public void setAutomaticZenRuleState(java.lang.String id, android.service.notification.Condition condition) {
            java.util.Objects.requireNonNull(id, "id is null");
            java.util.Objects.requireNonNull(condition, "Condition is null");
            condition.validate();
            enforcePolicyAccess(android.os.Binder.getCallingUid(), "setAutomaticZenRuleState");
            boolean fromUser = condition.source == 1;
            com.android.server.notification.NotificationManagerService.this.mZenModeHelper.setAutomaticZenRuleState(id, condition, computeZenOrigin(fromUser), android.os.Binder.getCallingUid());
        }

        private int computeZenOrigin(boolean fromUser) {
            if (android.app.Flags.modesApi() && fromUser) {
                return 3;
            }
            if (com.android.server.notification.NotificationManagerService.this.isCallerSystemOrSystemUi()) {
                return 5;
            }
            return 4;
        }

        private void enforceUserOriginOnlyFromSystem(boolean fromUser, java.lang.String method) {
            if (android.app.Flags.modesApi() && fromUser && !com.android.server.notification.NotificationManagerService.this.isCallerSystemOrSystemUiOrShell()) {
                throw new java.lang.SecurityException(android.text.TextUtils.formatSimple("Calling %s with fromUser == true is only allowed for system", new java.lang.Object[]{method}));
            }
        }

        public void setInterruptionFilter(java.lang.String pkg, int filter, boolean fromUser) {
            enforcePolicyAccess(pkg, "setInterruptionFilter");
            int zen = android.app.NotificationManager.zenModeFromInterruptionFilter(filter, -1);
            if (zen == -1) {
                throw new java.lang.IllegalArgumentException("Invalid filter: " + filter);
            }
            int callingUid = android.os.Binder.getCallingUid();
            enforceUserOriginOnlyFromSystem(fromUser, "setInterruptionFilter");
            if (android.app.Flags.modesApi() && !canManageGlobalZenPolicy(pkg, callingUid)) {
                com.android.server.notification.NotificationManagerService.this.mZenModeHelper.applyGlobalZenModeAsImplicitZenRule(pkg, callingUid, zen);
                return;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.notification.NotificationManagerService.this.mZenModeHelper.setManualZenMode(zen, null, computeZenOrigin(fromUser), "setInterruptionFilter", pkg, callingUid);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void notifyConditions(final java.lang.String pkg, android.service.notification.IConditionProvider provider, final android.service.notification.Condition[] conditions) {
            final com.android.server.notification.ManagedServices.ManagedServiceInfo info = com.android.server.notification.NotificationManagerService.this.mConditionProviders.checkServiceToken(provider);
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
            com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService.12.1
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.notification.NotificationManagerService.this.mConditionProviders.notifyConditions(pkg, info, conditions);
                }
            });
        }

        public void requestUnbindProvider(android.service.notification.IConditionProvider provider) {
            int uid = android.os.Binder.getCallingUid();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.notification.ManagedServices.ManagedServiceInfo info = com.android.server.notification.NotificationManagerService.this.mConditionProviders.checkServiceToken(provider);
                info.getOwner().setComponentState(info.component, android.os.UserHandle.getUserId(uid), false);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void requestBindProvider(android.content.ComponentName component) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(component.getPackageName());
            int uid = android.os.Binder.getCallingUid();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.notification.NotificationManagerService.this.mConditionProviders.setComponentState(component, android.os.UserHandle.getUserId(uid), true);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private void enforceSystemOrSystemUI(java.lang.String message) {
            if (com.android.server.notification.NotificationManagerService.this.isCallerSystemOrPhone()) {
                return;
            }
            com.android.server.notification.NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.STATUS_BAR_SERVICE", message);
        }

        private void enforceSystemOrSystemUIOrSamePackage(java.lang.String pkg, java.lang.String message) {
            try {
                com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
            } catch (java.lang.SecurityException e) {
                com.android.server.notification.NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.STATUS_BAR_SERVICE", message);
            }
        }

        private void enforcePolicyAccess(int uid, java.lang.String method) {
            if (com.android.server.notification.NotificationManagerService.this.getContext().checkCallingPermission("android.permission.MANAGE_NOTIFICATIONS") == 0) {
                return;
            }
            boolean accessAllowed = false;
            java.lang.String[] packages = com.android.server.notification.NotificationManagerService.this.mPackageManagerClient.getPackagesForUid(uid);
            for (java.lang.String str : packages) {
                if (com.android.server.notification.NotificationManagerService.this.mConditionProviders.isPackageOrComponentAllowed(str, android.os.UserHandle.getUserId(uid))) {
                    accessAllowed = true;
                }
            }
            if (!accessAllowed) {
                android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "Notification policy access denied calling " + method);
                throw new java.lang.SecurityException("Notification policy access denied");
            }
        }

        private boolean canManageGlobalZenPolicy(java.lang.String callingPkg, final int callingUid) {
            boolean isCompatChangeEnabled = ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.notification.NotificationManagerService$12$$ExternalSyntheticLambda0
                public final java.lang.Object getOrThrow() {
                    return java.lang.Boolean.valueOf(android.app.compat.CompatChanges.isChangeEnabled(com.android.server.notification.NotificationManagerService.MANAGE_GLOBAL_ZEN_VIA_IMPLICIT_RULES, callingUid));
                }
            })).booleanValue();
            return !isCompatChangeEnabled || com.android.server.notification.NotificationManagerService.this.isCallerSystemOrSystemUi() || com.android.server.notification.NotificationManagerService.this.hasCompanionDevice(callingPkg, android.os.UserHandle.getUserId(callingUid), java.util.Set.of("android.app.role.COMPANION_DEVICE_WATCH", "android.app.role.SYSTEM_AUTOMOTIVE_PROJECTION"));
        }

        private void enforcePolicyAccess(java.lang.String pkg, java.lang.String method) {
            if (com.android.server.notification.NotificationManagerService.this.getContext().checkCallingPermission("android.permission.MANAGE_NOTIFICATIONS") == 0) {
                return;
            }
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSameApp(pkg);
            if (!checkPolicyAccess(pkg)) {
                android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "Notification policy access denied calling " + method);
                throw new java.lang.SecurityException("Notification policy access denied");
            }
        }

        private boolean checkPackagePolicyAccess(java.lang.String pkg) {
            return com.android.server.notification.NotificationManagerService.this.mConditionProviders.isPackageOrComponentAllowed(pkg, getCallingUserHandle().getIdentifier());
        }

        private boolean checkPolicyAccess(java.lang.String pkg) {
            try {
                int uid = com.android.server.notification.NotificationManagerService.this.getContext().getPackageManager().getPackageUidAsUser(pkg, android.os.UserHandle.getCallingUserId());
                if (com.android.server.notification.NotificationManagerService.this.checkComponentPermission("android.permission.MANAGE_NOTIFICATIONS", uid, -1, true) == 0) {
                    return true;
                }
                if (!checkPackagePolicyAccess(pkg) && !com.android.server.notification.NotificationManagerService.this.mListeners.isComponentEnabledForPackage(pkg)) {
                    if (com.android.server.notification.NotificationManagerService.this.mDpm == null) {
                        return false;
                    }
                    if (!com.android.server.notification.NotificationManagerService.this.mDpm.isActiveProfileOwner(uid) && !com.android.server.notification.NotificationManagerService.this.mDpm.isActiveDeviceOwner(uid)) {
                        return false;
                    }
                }
                return true;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return false;
            }
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(com.android.server.notification.NotificationManagerService.this.getContext(), com.android.server.notification.NotificationManagerService.TAG, pw)) {
                if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().dump(fd, pw, args)) {
                    return;
                }
                com.android.server.notification.NotificationManagerService.DumpFilter filter = com.android.server.notification.NotificationManagerService.DumpFilter.parseFromArguments(args);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> pkgPermissions = com.android.server.notification.NotificationManagerService.this.getAllUsersNotificationPermissions();
                    if (filter.stats) {
                        com.android.server.notification.NotificationManagerService.this.dumpJson(pw, filter, pkgPermissions);
                    } else if (filter.rvStats) {
                        com.android.server.notification.NotificationManagerService.this.dumpRemoteViewStats(pw, filter);
                    } else if (filter.proto) {
                        com.android.server.notification.NotificationManagerService.this.dumpProto(fd, filter, pkgPermissions);
                    } else if (filter.criticalPriority) {
                        com.android.server.notification.NotificationManagerService.this.dumpNotificationRecords(pw, filter);
                    } else {
                        com.android.server.notification.NotificationManagerService.this.dumpImpl(pw, filter, pkgPermissions);
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        }

        public android.content.ComponentName getEffectsSuppressor() {
            android.content.ComponentName suppressor;
            if (!com.android.server.notification.NotificationManagerService.this.mEffectsSuppressors.isEmpty()) {
                suppressor = (android.content.ComponentName) com.android.server.notification.NotificationManagerService.this.mEffectsSuppressors.get(0);
            } else {
                suppressor = null;
            }
            if (com.android.server.notification.NotificationManagerService.this.isCallerSystemOrSystemUiOrShell() || suppressor == null || com.android.server.notification.NotificationManagerService.this.mPackageManagerInternal.isSameApp(suppressor.getPackageName(), android.os.Binder.getCallingUid(), android.os.UserHandle.getUserId(android.os.Binder.getCallingUid()))) {
                return suppressor;
            }
            return null;
        }

        public boolean matchesCallFilter(android.os.Bundle extras) {
            boolean systemAccess = false;
            try {
                enforceSystemOrSystemUI("INotificationManager.matchesCallFilter");
                systemAccess = true;
            } catch (java.lang.SecurityException e) {
            }
            boolean listenerAccess = false;
            try {
                java.lang.String[] pkgNames = com.android.server.notification.NotificationManagerService.this.mPackageManager.getPackagesForUid(android.os.Binder.getCallingUid());
                for (java.lang.String str : pkgNames) {
                    listenerAccess |= com.android.server.notification.NotificationManagerService.this.mListeners.hasAllowedListener(str, android.os.Binder.getCallingUserHandle().getIdentifier());
                }
            } catch (android.os.RemoteException e2) {
                if (!systemAccess && !listenerAccess) {
                }
            } catch (java.lang.Throwable th) {
                if (!systemAccess && !listenerAccess) {
                    com.android.server.notification.NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.READ_CONTACTS", "matchesCallFilter requires listener permission, contacts read access, or system level access");
                }
                throw th;
            }
            if (!systemAccess && !listenerAccess) {
                com.android.server.notification.NotificationManagerService.this.getContext().enforceCallingPermission("android.permission.READ_CONTACTS", "matchesCallFilter requires listener permission, contacts read access, or system level access");
            }
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.matchesCallFilter(android.os.Binder.getCallingUserHandle(), extras, (com.android.server.notification.ValidateNotificationPeople) com.android.server.notification.NotificationManagerService.this.mRankingHelper.findExtractor(com.android.server.notification.ValidateNotificationPeople.class), 3000, 1.0f, android.os.Binder.getCallingUid());
        }

        public void cleanUpCallersAfter(long timeThreshold) {
            enforceSystemOrSystemUI("INotificationManager.cleanUpCallersAfter");
            com.android.server.notification.NotificationManagerService.this.mZenModeHelper.cleanUpCallersAfter(timeThreshold);
        }

        public boolean isSystemConditionProviderEnabled(java.lang.String path) {
            enforceSystemOrSystemUI("INotificationManager.isSystemConditionProviderEnabled");
            return com.android.server.notification.NotificationManagerService.this.mConditionProviders.isSystemProviderEnabled(path);
        }

        public byte[] getBackupPayload(int user) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            if (com.android.server.notification.NotificationManagerService.DBG) {
                android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, "getBackupPayload u=" + user);
            }
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            try {
                com.android.server.notification.NotificationManagerService.this.writePolicyXml(baos, true, user);
                return baos.toByteArray();
            } catch (java.io.IOException e) {
                android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "getBackupPayload: error writing payload for user " + user, e);
                return null;
            }
        }

        public void applyRestore(byte[] payload, int user) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            if (com.android.server.notification.NotificationManagerService.DBG) {
                android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, "applyRestore u=" + user + " payload=" + (payload != null ? new java.lang.String(payload, java.nio.charset.StandardCharsets.UTF_8) : null));
            }
            if (payload == null) {
                android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "applyRestore: no payload to restore for user " + user);
                return;
            }
            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(payload);
            try {
                com.android.server.notification.NotificationManagerService.this.readPolicyXml(bais, true, user);
                com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
            } catch (java.io.IOException | java.lang.NumberFormatException | org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, "applyRestore: error reading payload", e);
            }
        }

        public boolean isNotificationPolicyAccessGranted(java.lang.String pkg) {
            return checkPolicyAccess(pkg);
        }

        public boolean isNotificationPolicyAccessGrantedForPackage(java.lang.String pkg) {
            enforceSystemOrSystemUIOrSamePackage(pkg, "request policy access status for another package");
            return checkPolicyAccess(pkg);
        }

        public void setNotificationPolicyAccessGranted(java.lang.String pkg, boolean granted) throws android.os.RemoteException {
            setNotificationPolicyAccessGrantedForUser(pkg, getCallingUserHandle().getIdentifier(), granted);
        }

        public void setNotificationPolicyAccessGrantedForUser(java.lang.String pkg, int userId, boolean granted) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrShell();
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.server.notification.NotificationManagerService.this.mAllowedManagedServicePackages.test(pkg, java.lang.Integer.valueOf(userId), com.android.server.notification.NotificationManagerService.this.mConditionProviders.getRequiredPermission())) {
                    com.android.server.notification.NotificationManagerService.this.mConditionProviders.setPackageOrComponentEnabled(pkg, userId, true, granted);
                    com.android.server.notification.NotificationManagerService.this.getContext().sendBroadcastAsUser(new android.content.Intent("android.app.action.NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED").setPackage(pkg).addFlags(67108864), android.os.UserHandle.of(userId), null);
                    com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public android.app.NotificationManager.Policy getNotificationPolicy(java.lang.String pkg) {
            int callingUid = android.os.Binder.getCallingUid();
            if (android.app.Flags.modesApi() && !canManageGlobalZenPolicy(pkg, callingUid)) {
                return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getNotificationPolicyFromImplicitZenRule(pkg);
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getNotificationPolicy();
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public android.app.NotificationManager.Policy getConsolidatedNotificationPolicy() {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getConsolidatedNotificationPolicy();
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void setNotificationPolicy(java.lang.String pkg, android.app.NotificationManager.Policy policy, boolean fromUser) throws java.lang.Throwable {
            android.app.NotificationManager.Policy policy2 = policy;
            enforcePolicyAccess(pkg, "setNotificationPolicy");
            enforceUserOriginOnlyFromSystem(fromUser, "setNotificationPolicy");
            int callingUid = android.os.Binder.getCallingUid();
            int origin = computeZenOrigin(fromUser);
            boolean shouldApplyAsImplicitRule = android.app.Flags.modesApi() && !canManageGlobalZenPolicy(pkg, callingUid);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    android.content.pm.ApplicationInfo applicationInfo = com.android.server.notification.NotificationManagerService.this.mPackageManager.getApplicationInfo(pkg, 0L, android.os.UserHandle.getUserId(callingUid));
                    android.app.NotificationManager.Policy currPolicy = com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getNotificationPolicy();
                    if (applicationInfo.targetSdkVersion < 28) {
                        int priorityCategories = policy2.priorityCategories;
                        policy2 = new android.app.NotificationManager.Policy((priorityCategories & (-33) & (-65) & (-129)) | (currPolicy.priorityCategories & 32) | (currPolicy.priorityCategories & 64) | (currPolicy.priorityCategories & 128), policy2.priorityCallSenders, policy2.priorityMessageSenders, policy2.suppressedVisualEffects);
                    }
                    try {
                        int priorityCategories2 = applicationInfo.targetSdkVersion;
                        if (priorityCategories2 < 30) {
                            int priorityCategories3 = com.android.server.notification.NotificationManagerService.this.correctCategory(policy2.priorityCategories, 256, currPolicy.priorityCategories);
                            policy2 = new android.app.NotificationManager.Policy(priorityCategories3, policy2.priorityCallSenders, policy2.priorityMessageSenders, policy2.suppressedVisualEffects, currPolicy.priorityConversationSenders);
                        }
                        int newVisualEffects = com.android.server.notification.NotificationManagerService.this.calculateSuppressedVisualEffects(policy2, currPolicy, applicationInfo.targetSdkVersion);
                        android.app.NotificationManager.Policy policy3 = new android.app.NotificationManager.Policy(policy2.priorityCategories, policy2.priorityCallSenders, policy2.priorityMessageSenders, newVisualEffects, policy2.priorityConversationSenders);
                        if (shouldApplyAsImplicitRule) {
                            com.android.server.notification.NotificationManagerService.this.mZenModeHelper.applyGlobalPolicyAsImplicitZenRule(pkg, callingUid, policy3);
                        } else {
                            if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                                policy3 = com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().adjustNotificationPolicy(pkg, policy3);
                            }
                            com.android.server.notification.ZenLog.traceSetNotificationPolicy(pkg, applicationInfo.targetSdkVersion, policy3);
                            com.android.server.notification.NotificationManagerService.this.mZenModeHelper.setNotificationPolicy(policy3, origin, callingUid);
                        }
                    } catch (android.os.RemoteException e) {
                        e = e;
                        android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "Failed to set notification policy", e);
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    android.os.Binder.restoreCallingIdentity(identity);
                    throw th;
                }
            } catch (android.os.RemoteException e2) {
                e = e2;
            } catch (java.lang.Throwable th2) {
                th = th2;
                android.os.Binder.restoreCallingIdentity(identity);
                throw th;
            }
            android.os.Binder.restoreCallingIdentity(identity);
        }

        public android.service.notification.ZenPolicy getDefaultZenPolicy() {
            enforceSystemOrSystemUI("INotificationManager.getDefaultZenPolicy");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.notification.NotificationManagerService.this.mZenModeHelper.getDefaultZenPolicy();
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public java.util.List<java.lang.String> getEnabledNotificationListenerPackages() {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            return com.android.server.notification.NotificationManagerService.this.mListeners.getAllowedPackages(getCallingUserHandle().getIdentifier());
        }

        public java.util.List<android.content.ComponentName> getEnabledNotificationListeners(int userId) {
            com.android.server.notification.NotificationManagerService.this.checkNotificationListenerAccess();
            return com.android.server.notification.NotificationManagerService.this.mListeners.getAllowedComponents(userId);
        }

        public android.content.ComponentName getAllowedNotificationAssistantForUser(int userId) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSystemUiOrShell();
            java.util.List<android.content.ComponentName> allowedComponents = com.android.server.notification.NotificationManagerService.this.mAssistants.getAllowedComponents(userId);
            if (allowedComponents.size() > 1) {
                throw new java.lang.IllegalStateException("At most one NotificationAssistant: " + allowedComponents.size());
            }
            return (android.content.ComponentName) com.android.internal.util.CollectionUtils.firstOrNull(allowedComponents);
        }

        public android.content.ComponentName getAllowedNotificationAssistant() {
            return getAllowedNotificationAssistantForUser(getCallingUserHandle().getIdentifier());
        }

        public android.content.ComponentName getDefaultNotificationAssistant() {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            return com.android.server.notification.NotificationManagerService.this.mAssistants.getDefaultFromConfig();
        }

        public void setNASMigrationDoneAndResetDefault(int userId, boolean loadFromConfig) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            com.android.server.notification.NotificationManagerService.this.setNASMigrationDone(userId);
            if (loadFromConfig) {
                com.android.server.notification.NotificationManagerService.this.mAssistants.resetDefaultFromConfig();
            } else {
                com.android.server.notification.NotificationManagerService.this.mAssistants.clearDefaults();
            }
        }

        public boolean hasEnabledNotificationListener(java.lang.String packageName, int userId) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            return com.android.server.notification.NotificationManagerService.this.mListeners.isPackageAllowed(packageName, userId);
        }

        public boolean isNotificationListenerAccessGranted(android.content.ComponentName listener) {
            java.util.Objects.requireNonNull(listener);
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(listener.getPackageName());
            return com.android.server.notification.NotificationManagerService.this.mListeners.isPackageOrComponentAllowed(listener.flattenToString(), getCallingUserHandle().getIdentifier());
        }

        public boolean isNotificationListenerAccessGrantedForUser(android.content.ComponentName listener, int userId) {
            java.util.Objects.requireNonNull(listener);
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            return com.android.server.notification.NotificationManagerService.this.mListeners.isPackageOrComponentAllowed(listener.flattenToString(), userId);
        }

        public boolean isNotificationAssistantAccessGranted(android.content.ComponentName assistant) {
            java.util.Objects.requireNonNull(assistant);
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(assistant.getPackageName());
            return com.android.server.notification.NotificationManagerService.this.mAssistants.isPackageOrComponentAllowed(assistant.flattenToString(), getCallingUserHandle().getIdentifier());
        }

        public void setNotificationListenerAccessGranted(android.content.ComponentName listener, boolean granted, boolean userSet) throws android.os.RemoteException {
            setNotificationListenerAccessGrantedForUser(listener, getCallingUserHandle().getIdentifier(), granted, userSet);
        }

        public void setNotificationAssistantAccessGranted(android.content.ComponentName assistant, boolean granted) {
            setNotificationAssistantAccessGrantedForUser(assistant, getCallingUserHandle().getIdentifier(), granted);
        }

        public void setNotificationListenerAccessGrantedForUser(android.content.ComponentName listener, int userId, boolean granted, boolean userSet) {
            java.util.Objects.requireNonNull(listener);
            if (android.os.UserHandle.getCallingUserId() != userId) {
                com.android.server.notification.NotificationManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", "setNotificationListenerAccessGrantedForUser for user " + userId);
            }
            com.android.server.notification.NotificationManagerService.this.checkNotificationListenerAccess();
            if (granted && listener.flattenToString().length() > android.app.NotificationManager.MAX_SERVICE_COMPONENT_NAME_LENGTH) {
                throw new java.lang.IllegalArgumentException("Component name too long: " + listener.flattenToString());
            }
            if (!userSet && isNotificationListenerAccessUserSet(listener, userId)) {
                return;
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.server.notification.NotificationManagerService.this.mAllowedManagedServicePackages.test(listener.getPackageName(), java.lang.Integer.valueOf(userId), com.android.server.notification.NotificationManagerService.this.mListeners.getRequiredPermission())) {
                    com.android.server.notification.NotificationManagerService.this.mConditionProviders.setPackageOrComponentEnabled(listener.flattenToString(), userId, false, granted, userSet);
                    com.android.server.notification.NotificationManagerService.this.mListeners.setPackageOrComponentEnabled(listener.flattenToString(), userId, true, granted, userSet);
                    com.android.server.notification.NotificationManagerService.this.getContext().sendBroadcastAsUser(new android.content.Intent("android.app.action.NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED").setPackage(listener.getPackageName()).addFlags(1073741824), android.os.UserHandle.of(userId), null);
                    com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private boolean isNotificationListenerAccessUserSet(android.content.ComponentName listener, int userId) {
            return com.android.server.notification.NotificationManagerService.this.mListeners.isPackageOrComponentUserSet(listener.flattenToString(), userId);
        }

        public void setNotificationAssistantAccessGrantedForUser(android.content.ComponentName assistant, int userId, boolean granted) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSystemUiOrShell();
            for (android.content.pm.UserInfo ui : com.android.server.notification.NotificationManagerService.this.mUm.getEnabledProfiles(userId)) {
                com.android.server.notification.NotificationManagerService.this.mAssistants.setUserSet(ui.id, true);
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.notification.NotificationManagerService.this.setNotificationAssistantAccessGrantedForUserInternal(assistant, userId, granted, true);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void applyEnqueuedAdjustmentFromAssistant(android.service.notification.INotificationListener token, android.service.notification.Adjustment adjustment) {
            boolean foundEnqueued = false;
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.NotificationManagerService.this.mAssistants.checkServiceTokenLocked(token);
                    int N = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.size();
                    for (int i = 0; i < N; i++) {
                        com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.get(i);
                        if (java.util.Objects.equals(adjustment.getKey(), r.getKey()) && java.util.Objects.equals(java.lang.Integer.valueOf(adjustment.getUser()), java.lang.Integer.valueOf(r.getUserId())) && com.android.server.notification.NotificationManagerService.this.mAssistants.isSameUser(token, r.getUserId())) {
                            com.android.server.notification.NotificationManagerService.this.applyAdjustmentLocked(r, adjustment, false);
                            r.applyAdjustments();
                            r.calculateImportance();
                            foundEnqueued = true;
                        }
                    }
                    if (!foundEnqueued) {
                        applyAdjustmentsFromAssistant(token, java.util.List.of(adjustment));
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void applyAdjustmentFromAssistant(android.service.notification.INotificationListener token, android.service.notification.Adjustment adjustment) {
            java.util.List<android.service.notification.Adjustment> adjustments = new java.util.ArrayList<>();
            adjustments.add(adjustment);
            applyAdjustmentsFromAssistant(token, adjustments);
        }

        public void applyAdjustmentsFromAssistant(android.service.notification.INotificationListener token, java.util.List<android.service.notification.Adjustment> adjustments) {
            boolean needsSort = false;
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    com.android.server.notification.NotificationManagerService.this.mAssistants.checkServiceTokenLocked(token);
                    for (android.service.notification.Adjustment adjustment : adjustments) {
                        com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(adjustment.getKey());
                        if (r != null && com.android.server.notification.NotificationManagerService.this.mAssistants.isSameUser(token, r.getUserId())) {
                            com.android.server.notification.NotificationManagerService.this.applyAdjustmentLocked(r, adjustment, true);
                            if (adjustment.getSignals().containsKey("key_importance") && adjustment.getSignals().getInt("key_importance") == 0) {
                                cancelNotificationsFromListener(token, new java.lang.String[]{r.getKey()});
                            } else {
                                r.setPendingLogUpdate(true);
                                needsSort = true;
                            }
                        }
                    }
                }
                if (needsSort) {
                    com.android.server.notification.NotificationManagerService.this.mRankingHandler.requestSort();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public void updateNotificationChannelGroupFromPrivilegedListener(android.service.notification.INotificationListener token, java.lang.String pkg, android.os.UserHandle user, android.app.NotificationChannelGroup group) throws android.os.RemoteException {
            java.util.Objects.requireNonNull(user);
            verifyPrivilegedListener(token, user, false);
            com.android.server.notification.NotificationManagerService.this.createNotificationChannelGroup(pkg, getUidForPackageAndUser(pkg, user), group, false, true);
            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
        }

        public void updateNotificationChannelFromPrivilegedListener(android.service.notification.INotificationListener token, java.lang.String pkg, android.os.UserHandle user, android.app.NotificationChannel channel) throws android.os.RemoteException {
            java.util.Objects.requireNonNull(channel);
            java.util.Objects.requireNonNull(pkg);
            java.util.Objects.requireNonNull(user);
            verifyPrivilegedListener(token, user, false);
            android.app.NotificationChannel originalChannel = com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannel(pkg, getUidForPackageAndUser(pkg, user), channel.getId(), true);
            verifyPrivilegedListenerUriPermission(android.os.Binder.getCallingUid(), channel, originalChannel);
            com.android.server.notification.NotificationManagerService.this.updateNotificationChannelInt(pkg, getUidForPackageAndUser(pkg, user), channel, true);
        }

        public android.content.pm.ParceledListSlice<android.app.NotificationChannel> getNotificationChannelsFromPrivilegedListener(android.service.notification.INotificationListener token, java.lang.String pkg, android.os.UserHandle user) throws android.os.RemoteException {
            java.util.Objects.requireNonNull(pkg);
            java.util.Objects.requireNonNull(user);
            verifyPrivilegedListener(token, user, true);
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannels(pkg, getUidForPackageAndUser(pkg, user), false);
        }

        public android.content.pm.ParceledListSlice<android.app.NotificationChannelGroup> getNotificationChannelGroupsFromPrivilegedListener(android.service.notification.INotificationListener token, java.lang.String pkg, android.os.UserHandle user) throws android.os.RemoteException {
            java.util.Objects.requireNonNull(pkg);
            java.util.Objects.requireNonNull(user);
            verifyPrivilegedListener(token, user, true);
            java.util.List<android.app.NotificationChannelGroup> groups = new java.util.ArrayList<>();
            groups.addAll(com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannelGroups(pkg, getUidForPackageAndUser(pkg, user)));
            return new android.content.pm.ParceledListSlice<>(groups);
        }

        public boolean isInCall(java.lang.String pkg, int uid) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSystemUiOrShell();
            return com.android.server.notification.NotificationManagerService.this.isCallNotification(pkg, uid);
        }

        public void setPrivateNotificationsAllowed(boolean allow) {
            if (com.android.server.notification.NotificationManagerService.this.getContext().checkCallingPermission("android.permission.CONTROL_KEYGUARD_SECURE_NOTIFICATIONS") != 0) {
                throw new java.lang.SecurityException("Requires CONTROL_KEYGUARD_SECURE_NOTIFICATIONS permission");
            }
            if (allow != com.android.server.notification.NotificationManagerService.this.mLockScreenAllowSecureNotifications) {
                com.android.server.notification.NotificationManagerService.this.mLockScreenAllowSecureNotifications = allow;
                if (android.app.Flags.keyguardPrivateNotifications()) {
                    com.android.server.notification.NotificationManagerService.this.getContext().sendBroadcast(new android.content.Intent("android.app.action.KEYGUARD_PRIVATE_NOTIFICATIONS_CHANGED").putExtra("android.app.extra.KM_PRIVATE_NOTIFS_ALLOWED", com.android.server.notification.NotificationManagerService.this.mLockScreenAllowSecureNotifications), "android.permission.STATUS_BAR_SERVICE");
                }
                com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
            }
        }

        public boolean getPrivateNotificationsAllowed() {
            if (com.android.server.notification.NotificationManagerService.this.getContext().checkCallingPermission("android.permission.CONTROL_KEYGUARD_SECURE_NOTIFICATIONS") != 0) {
                throw new java.lang.SecurityException("Requires CONTROL_KEYGUARD_SECURE_NOTIFICATIONS permission");
            }
            return com.android.server.notification.NotificationManagerService.this.mLockScreenAllowSecureNotifications;
        }

        public boolean isPackagePaused(java.lang.String pkg) {
            java.util.Objects.requireNonNull(pkg);
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSameApp(pkg);
            return com.android.server.notification.NotificationManagerService.this.isPackagePausedOrSuspended(pkg, android.os.Binder.getCallingUid());
        }

        public boolean isPermissionFixed(java.lang.String pkg, int userId) {
            enforceSystemOrSystemUI("isPermissionFixed");
            return com.android.server.notification.NotificationManagerService.this.mPermissionHelper.isPermissionFixed(pkg, userId);
        }

        private void verifyPrivilegedListener(android.service.notification.INotificationListener token, android.os.UserHandle user, boolean assistantAllowed) {
            com.android.server.notification.ManagedServices.ManagedServiceInfo info;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                info = com.android.server.notification.NotificationManagerService.this.mListeners.checkServiceTokenLocked(token);
            }
            if (!com.android.server.notification.NotificationManagerService.this.hasCompanionDevice(info)) {
                synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                    if (assistantAllowed) {
                        if (com.android.server.notification.NotificationManagerService.this.mAssistants.isServiceTokenValidLocked(info.service)) {
                        }
                    }
                    throw new java.lang.SecurityException(info + " does not have access");
                }
            }
            if (!info.enabledAndUserMatches(user.getIdentifier())) {
                throw new java.lang.SecurityException(info + " does not have access");
            }
        }

        private void verifyPrivilegedListenerUriPermission(final int sourceUid, android.app.NotificationChannel updateChannel, android.app.NotificationChannel originalChannel) {
            final android.net.Uri soundUri = updateChannel.getSound();
            android.net.Uri originalSoundUri = originalChannel != null ? originalChannel.getSound() : null;
            if (soundUri != null && !java.util.Objects.equals(originalSoundUri, soundUri)) {
                android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$12$$ExternalSyntheticLambda4
                    public final void runOrThrow() throws java.lang.Exception {
                        this.f$0.lambda$verifyPrivilegedListenerUriPermission$4(sourceUid, soundUri);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$verifyPrivilegedListenerUriPermission$4(int sourceUid, android.net.Uri soundUri) throws java.lang.Exception {
            com.android.server.notification.NotificationManagerService.this.mUgmInternal.checkGrantUriPermission(sourceUid, null, android.content.ContentProvider.getUriWithoutUserId(soundUri), 1, android.content.ContentProvider.getUserIdFromUri(soundUri, android.os.UserHandle.getUserId(sourceUid)));
        }

        private int getUidForPackageAndUser(java.lang.String pkg, android.os.UserHandle user) throws android.os.RemoteException {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                int uid = com.android.server.notification.NotificationManagerService.this.mPackageManager.getPackageUid(pkg, 0L, user.getIdentifier());
                return uid;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
            new com.android.server.notification.NotificationShellCmd(com.android.server.notification.NotificationManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        public long pullStats(long startNs, int report, boolean doAgg, java.util.List<android.os.ParcelFileDescriptor> out) throws java.io.IOException {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrShell();
            long startMs = java.util.concurrent.TimeUnit.MILLISECONDS.convert(startNs, java.util.concurrent.TimeUnit.NANOSECONDS);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                switch (report) {
                    case 1:
                        try {
                            android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "pullStats REPORT_REMOTE_VIEWS from: " + startMs + "  with " + doAgg);
                            com.android.server.notification.PulledStats stats = com.android.server.notification.NotificationManagerService.this.mUsageStats.remoteViewStats(startMs, doAgg);
                            try {
                                if (stats == null) {
                                    android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "null stats for: " + report);
                                    android.os.Binder.restoreCallingIdentity(identity);
                                    android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "exiting pullStats: bad request");
                                    return 0L;
                                }
                                out.add(stats.toParcelFileDescriptor(report));
                                android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "exiting pullStats with: " + out.size());
                                long endNs = java.util.concurrent.TimeUnit.NANOSECONDS.convert(stats.endTimeMs(), java.util.concurrent.TimeUnit.MILLISECONDS);
                                android.os.Binder.restoreCallingIdentity(identity);
                                return endNs;
                            } catch (java.io.IOException e) {
                                e = e;
                            }
                        } catch (java.io.IOException e2) {
                            e = e2;
                        } catch (java.lang.Throwable th) {
                            e = th;
                            android.os.Binder.restoreCallingIdentity(identity);
                            throw e;
                        }
                        android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "exiting pullStats: on error", e);
                        android.os.Binder.restoreCallingIdentity(identity);
                        return 0L;
                    default:
                        android.os.Binder.restoreCallingIdentity(identity);
                        android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "exiting pullStats: bad request");
                        return 0L;
                }
            } catch (java.lang.Throwable th2) {
                e = th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNotificationPermissionChange(java.lang.String pkg, int userId) {
        if (!this.mUmInternal.isUserInitialized(userId)) {
            return;
        }
        int uid = this.mPackageManagerInternal.getPackageUid(pkg, 0L, userId);
        if (uid == -1) {
            android.util.Log.e(TAG, java.lang.String.format("No uid found for %s, %s!", pkg, java.lang.Integer.valueOf(userId)));
            return;
        }
        boolean hasPermission = this.mPermissionHelper.hasPermission(uid);
        if (!hasPermission) {
            cancelAllNotificationsInt(MY_UID, MY_PID, pkg, null, 0, 0, userId, 7);
        }
    }

    protected void checkNotificationListenerAccess() {
        if (!isCallerSystemOrPhone()) {
            getContext().enforceCallingPermission("android.permission.MANAGE_NOTIFICATION_LISTENERS", "Caller must hold android.permission.MANAGE_NOTIFICATION_LISTENERS");
        }
    }

    protected void setNotificationAssistantAccessGrantedForUserInternal(android.content.ComponentName assistant, int baseUserId, boolean granted, boolean userSet) {
        java.util.List<android.content.pm.UserInfo> users = this.mUm.getEnabledProfiles(baseUserId);
        if (users != null) {
            for (android.content.pm.UserInfo user : users) {
                int userId = user.id;
                if (assistant == null) {
                    android.content.ComponentName allowedAssistant = (android.content.ComponentName) com.android.internal.util.CollectionUtils.firstOrNull(this.mAssistants.getAllowedComponents(userId));
                    if (allowedAssistant != null) {
                        setNotificationAssistantAccessGrantedForUserInternal(allowedAssistant, userId, false, userSet);
                    }
                } else if (!granted || this.mAllowedManagedServicePackages.test(assistant.getPackageName(), java.lang.Integer.valueOf(userId), this.mAssistants.getRequiredPermission())) {
                    this.mConditionProviders.setPackageOrComponentEnabled(assistant.flattenToString(), userId, false, granted);
                    this.mAssistants.setPackageOrComponentEnabled(assistant.flattenToString(), userId, true, granted, userSet);
                    getContext().sendBroadcastAsUser(new android.content.Intent("android.app.action.NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED").setPackage(assistant.getPackageName()).addFlags(1073741824), android.os.UserHandle.of(userId), null);
                    handleSavePolicyFile();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyAdjustmentLocked(com.android.server.notification.NotificationRecord r, android.service.notification.Adjustment adjustment, boolean isPosted) {
        if (r != null && adjustment.getSignals() != null) {
            android.os.Bundle adjustments = adjustment.getSignals();
            android.os.Bundle.setDefusable(adjustments, true);
            java.util.List<java.lang.String> toRemove = new java.util.ArrayList<>();
            for (java.lang.String potentialKey : adjustments.keySet()) {
                if (!this.mAssistants.isAdjustmentAllowed(potentialKey)) {
                    if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isStowOptionKey(adjustment, potentialKey)) {
                        break;
                    } else {
                        toRemove.add(potentialKey);
                    }
                }
            }
            for (java.lang.String removeKey : toRemove) {
                adjustments.remove(removeKey);
            }
            r.addAdjustment(adjustment);
            if (adjustment.getSignals().containsKey("key_sensitive_content")) {
                logSensitiveAdjustmentReceived(isPosted, adjustment.getSignals().getBoolean("key_sensitive_content"), r.getLifespanMs(java.lang.System.currentTimeMillis()));
            }
        }
    }

    void addAutogroupKeyLocked(java.lang.String key, boolean requestSort) {
        com.android.server.notification.NotificationRecord r = this.mNotificationsByKey.get(key);
        if (r != null && r.getSbn().getOverrideGroupKey() == null) {
            java.lang.String overrideGroupKey = "ranker_group";
            if (this.mNMSWrapper.getNMSExt() != null) {
                overrideGroupKey = this.mNMSWrapper.getNMSExt().getGroupKey(r.getSbn());
            }
            addAutoGroupAdjustment(r, overrideGroupKey);
            com.android.server.EventLogTags.writeNotificationAutogrouped(key);
            if (!android.app.Flags.checkAutogroupBeforePost() || requestSort) {
                this.mRankingHandler.requestSort();
            }
        }
    }

    void removeAutogroupKeyLocked(java.lang.String key) {
        com.android.server.notification.NotificationRecord r = this.mNotificationsByKey.get(key);
        if (r == null) {
            android.util.Slog.w(TAG, "Failed to remove autogroup " + key);
        } else if (r.getSbn().getOverrideGroupKey() != null) {
            addAutoGroupAdjustment(r, null);
            com.android.server.EventLogTags.writeNotificationUnautogrouped(key);
            this.mRankingHandler.requestSort();
        }
    }

    private void addAutoGroupAdjustment(com.android.server.notification.NotificationRecord r, java.lang.String overrideGroupKey) {
        android.os.Bundle signals = new android.os.Bundle();
        signals.putString("key_group_key", overrideGroupKey);
        android.service.notification.Adjustment adjustment = new android.service.notification.Adjustment(r.getSbn().getPackageName(), r.getKey(), signals, "", r.getSbn().getUserId());
        r.addAdjustment(adjustment);
    }

    void clearAutogroupSummaryLocked(int userId, java.lang.String pkg) {
        com.android.server.notification.NotificationRecord removed;
        android.util.ArrayMap<java.lang.String, java.lang.String> summaries = this.mAutobundledSummaries.get(java.lang.Integer.valueOf(userId));
        if (summaries != null && summaries.containsKey(pkg) && (removed = findNotificationByKeyLocked(summaries.remove(pkg))) != null) {
            android.service.notification.StatusBarNotification sbn = removed.getSbn();
            cancelNotification(MY_UID, MY_PID, pkg, sbn.getTag(), sbn.getId(), 0, 0, false, userId, 16, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasAutoGroupSummaryLocked(android.service.notification.StatusBarNotification sbn) {
        android.util.ArrayMap<java.lang.String, java.lang.String> summaries = this.mAutobundledSummaries.get(java.lang.Integer.valueOf(sbn.getUserId()));
        return summaries != null && summaries.containsKey(sbn.getPackageName());
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00f0 A[Catch: all -> 0x016a, TryCatch #5 {all -> 0x016a, blocks: (B:23:0x00ac, B:25:0x00d5, B:27:0x00df, B:31:0x00f0, B:33:0x011a), top: B:75:0x00ac }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0177  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01a6  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x017d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0062 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    com.android.server.notification.NotificationRecord createAutoGroupSummary(int r36, java.lang.String r37, java.lang.String r38, int r39, android.graphics.drawable.Icon r40, int r41, int r42) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 445
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationManagerService.createAutoGroupSummary(int, java.lang.String, java.lang.String, int, android.graphics.drawable.Icon, int, int):com.android.server.notification.NotificationRecord");
    }

    protected android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> getAllUsersNotificationPermissions() {
        android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> allPermissions = new android.util.ArrayMap<>();
        java.util.List<android.content.pm.UserInfo> allUsers = this.mUm.getUsers();
        for (android.content.pm.UserInfo ui : allUsers) {
            android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> userPermissions = this.mPermissionHelper.getNotificationPermissionValues(ui.getUserHandle().getIdentifier());
            for (android.util.Pair<java.lang.Integer, java.lang.String> pair : userPermissions.keySet()) {
                allPermissions.put(pair, userPermissions.get(pair));
            }
        }
        return allPermissions;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpJson(java.io.PrintWriter pw, com.android.server.notification.NotificationManagerService.DumpFilter filter, android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> pkgPermissions) {
        org.json.JSONObject dump = new org.json.JSONObject();
        try {
            dump.put(com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE, "Notification Manager");
            dump.put("bans", this.mPreferencesHelper.dumpBansJson(filter, pkgPermissions));
            dump.put("ranking", this.mPreferencesHelper.dumpJson(filter, pkgPermissions));
            dump.put("stats", this.mUsageStats.dumpJson(filter));
            dump.put("channels", this.mPreferencesHelper.dumpChannelsJson(filter));
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        pw.println(dump);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpRemoteViewStats(java.io.PrintWriter pw, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        com.android.server.notification.PulledStats stats = this.mUsageStats.remoteViewStats(filter.since, true);
        if (stats == null) {
            pw.println("no remote view stats reported.");
        } else {
            stats.dump(1, pw, filter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpProto(java.io.FileDescriptor fd, com.android.server.notification.NotificationManagerService.DumpFilter filter, android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> pkgPermissions) throws java.lang.Throwable {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
        synchronized (this.mNotificationLock) {
            try {
                try {
                    int N = this.mNotificationList.size();
                    for (int i = 0; i < N; i++) {
                        com.android.server.notification.NotificationRecord nr = this.mNotificationList.get(i);
                        if (!filter.filtered || filter.matches(nr.getSbn())) {
                            nr.dump(proto, 2246267895809L, filter.redact, 1);
                        }
                    }
                    int N2 = this.mEnqueuedNotifications.size();
                    for (int i2 = 0; i2 < N2; i2++) {
                        com.android.server.notification.NotificationRecord nr2 = this.mEnqueuedNotifications.get(i2);
                        if (!filter.filtered || filter.matches(nr2.getSbn())) {
                            nr2.dump(proto, 2246267895809L, filter.redact, 0);
                        }
                    }
                    java.util.List<com.android.server.notification.NotificationRecord> snoozed = this.mSnoozeHelper.getSnoozed();
                    int N3 = snoozed.size();
                    for (int i3 = 0; i3 < N3; i3++) {
                        com.android.server.notification.NotificationRecord nr3 = snoozed.get(i3);
                        if (!filter.filtered || filter.matches(nr3.getSbn())) {
                            nr3.dump(proto, 2246267895809L, filter.redact, 2);
                        }
                    }
                    long zenLog = proto.start(1146756268034L);
                    this.mZenModeHelper.dump(proto);
                    for (android.content.ComponentName suppressor : this.mEffectsSuppressors) {
                        suppressor.dumpDebug(proto, 2246267895812L);
                    }
                    proto.end(zenLog);
                    long listenersToken = proto.start(1146756268035L);
                    this.mListeners.dump(proto, filter);
                    proto.end(listenersToken);
                    proto.write(1120986464260L, this.mListenerHints);
                    int i4 = 0;
                    while (i4 < this.mListenersDisablingEffects.size()) {
                        long effectsToken = proto.start(2246267895813L);
                        long zenLog2 = zenLog;
                        proto.write(1120986464257L, this.mListenersDisablingEffects.keyAt(i4));
                        android.util.ArraySet<android.content.ComponentName> listeners = this.mListenersDisablingEffects.valueAt(i4);
                        int j = 0;
                        while (j < listeners.size()) {
                            android.content.ComponentName componentName = listeners.valueAt(j);
                            componentName.dumpDebug(proto, 2246267895811L);
                            j++;
                            listenersToken = listenersToken;
                        }
                        proto.end(effectsToken);
                        i4++;
                        zenLog = zenLog2;
                        listenersToken = listenersToken;
                    }
                    long assistantsToken = proto.start(1146756268038L);
                    this.mAssistants.dump(proto, filter);
                    proto.end(assistantsToken);
                    long conditionsToken = proto.start(1146756268039L);
                    this.mConditionProviders.dump(proto, filter);
                    proto.end(conditionsToken);
                    long rankingToken = proto.start(1146756268040L);
                    this.mRankingHelper.dump(proto, filter);
                    this.mPreferencesHelper.dump(proto, filter, pkgPermissions);
                    proto.end(rankingToken);
                    proto.flush();
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpNotificationRecords(java.io.PrintWriter pw, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        synchronized (this.mNotificationLock) {
            int N = this.mNotificationList.size();
            if (N > 0) {
                pw.println("  Notification List:");
                for (int i = 0; i < N; i++) {
                    com.android.server.notification.NotificationRecord nr = this.mNotificationList.get(i);
                    if (!filter.filtered || filter.matches(nr.getSbn())) {
                        nr.dump(pw, "    ", getContext(), filter.redact);
                    }
                }
                pw.println("  ");
            }
        }
    }

    void dumpImpl(java.io.PrintWriter pw, com.android.server.notification.NotificationManagerService.DumpFilter filter, android.util.ArrayMap<android.util.Pair<java.lang.Integer, java.lang.String>, android.util.Pair<java.lang.Boolean, java.lang.Boolean>> pkgPermissions) {
        pw.print("Current Notification Manager state");
        if (filter.filtered) {
            pw.print(" (filtered to ");
            pw.print(filter);
            pw.print(")");
        }
        pw.println(':');
        boolean zenOnly = filter.filtered && filter.zen;
        if (!zenOnly) {
            synchronized (this.mToastQueue) {
                int N = this.mToastQueue.size();
                if (N > 0) {
                    pw.println("  Toast Queue:");
                    for (int i = 0; i < N; i++) {
                        this.mToastQueue.get(i).dump(pw, "    ", filter);
                    }
                    pw.println("  ");
                }
            }
        }
        synchronized (this.mNotificationLock) {
            if (!zenOnly) {
                try {
                    if (!filter.normalPriority) {
                        dumpNotificationRecords(pw, filter);
                    }
                    if (!filter.filtered) {
                        pw.println("  mMaxPackageEnqueueRate=" + this.mMaxPackageEnqueueRate);
                        pw.println("  hideSilentStatusBar=" + this.mPreferencesHelper.shouldHideSilentStatusIcons());
                        this.mAttentionHelper.dump(pw, "    ", filter);
                    }
                    pw.println("  mArchive=" + this.mArchive.toString());
                    this.mArchive.dumpImpl(pw, filter);
                    if (!zenOnly) {
                        int N2 = this.mEnqueuedNotifications.size();
                        if (N2 > 0) {
                            pw.println("  Enqueued Notification List:");
                            for (int i2 = 0; i2 < N2; i2++) {
                                com.android.server.notification.NotificationRecord nr = this.mEnqueuedNotifications.get(i2);
                                if (!filter.filtered || filter.matches(nr.getSbn())) {
                                    nr.dump(pw, "    ", getContext(), filter.redact);
                                }
                            }
                            pw.println("  ");
                        }
                        this.mSnoozeHelper.dump(pw, filter);
                    }
                } finally {
                }
            }
            if (!zenOnly) {
                pw.println("\n  Ranking Config:");
                this.mRankingHelper.dump(pw, "    ", filter);
                pw.println("\n Notification Preferences:");
                this.mPreferencesHelper.dump(pw, "    ", filter, pkgPermissions);
                pw.println("\n  Notification listeners:");
                this.mListeners.dump(pw, filter);
                pw.print("    mListenerHints: ");
                pw.println(this.mListenerHints);
                pw.print("    mListenersDisablingEffects: (");
                int N3 = this.mListenersDisablingEffects.size();
                for (int i3 = 0; i3 < N3; i3++) {
                    int hint = this.mListenersDisablingEffects.keyAt(i3);
                    if (i3 > 0) {
                        pw.print(';');
                    }
                    pw.print("hint[" + hint + "]:");
                    android.util.ArraySet<android.content.ComponentName> listeners = this.mListenersDisablingEffects.valueAt(i3);
                    int listenerSize = listeners.size();
                    for (int j = 0; j < listenerSize; j++) {
                        if (j > 0) {
                            pw.print(',');
                        }
                        android.content.ComponentName listener = listeners.valueAt(j);
                        if (listener != null) {
                            pw.print(listener);
                        }
                    }
                }
                pw.println(')');
                pw.println("\n  Notification assistant services:");
                this.mAssistants.dump(pw, filter);
            }
            if (!filter.filtered || zenOnly) {
                pw.println("\n  Zen Mode:");
                pw.print("    mInterruptionFilter=");
                pw.println(this.mInterruptionFilter);
                this.mZenModeHelper.dump(pw, "    ");
                pw.println("\n  Zen Log:");
                com.android.server.notification.ZenLog.dump(pw, "    ");
            }
            pw.println("\n  Condition providers:");
            this.mConditionProviders.dump(pw, filter);
            pw.println("\n  Group summaries:");
            for (java.util.Map.Entry<java.lang.String, com.android.server.notification.NotificationRecord> entry : this.mSummaryByGroupKey.entrySet()) {
                com.android.server.notification.NotificationRecord r = entry.getValue();
                pw.println("    " + entry.getKey() + " -> " + r.getKey());
                if (this.mNotificationsByKey.get(r.getKey()) != r) {
                    pw.println("!!!!!!LEAK: Record not found in mNotificationsByKey.");
                    r.dump(pw, "      ", getContext(), filter.redact);
                }
            }
            if (!zenOnly) {
                pw.println("\n  Usage Stats:");
                this.mUsageStats.dump(pw, "    ", filter);
                if (com.android.server.notification.Flags.allNotifsNeedTtl()) {
                    pw.println("\n  TimeToLive alarms:");
                    this.mTtlHelper.dump(pw, "    ");
                }
            }
            if (this.mNMSWrapper.getNMSExt() != null) {
                this.mNMSWrapper.getNMSExt().dumpImpl(pw);
            }
        }
    }

    /* JADX INFO: renamed from: com.android.server.notification.NotificationManagerService$13, reason: invalid class name */
    class AnonymousClass13 implements com.android.server.notification.NotificationManagerInternal {
        AnonymousClass13() {
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public android.app.NotificationChannel getNotificationChannel(java.lang.String pkg, int uid, java.lang.String channelId) {
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getNotificationChannel(pkg, uid, channelId, false);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public android.app.NotificationChannelGroup getNotificationChannelGroup(java.lang.String pkg, int uid, java.lang.String channelId) {
            return com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.getGroupForChannel(pkg, uid, channelId);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void enqueueNotification(java.lang.String pkg, java.lang.String opPkg, int callingUid, int callingPid, java.lang.String tag, int id, android.app.Notification notification, int userId) {
            com.android.server.notification.NotificationManagerService.this.enqueueNotificationInternal(pkg, opPkg, callingUid, callingPid, tag, id, notification, userId, false);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void enqueueNotification(java.lang.String pkg, java.lang.String opPkg, int callingUid, int callingPid, java.lang.String tag, int id, android.app.Notification notification, int userId, boolean byForegroundService) {
            com.android.server.notification.NotificationManagerService.this.enqueueNotificationInternal(pkg, opPkg, callingUid, callingPid, tag, id, notification, userId, byForegroundService);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void cancelNotification(java.lang.String pkg, java.lang.String opPkg, int callingUid, int callingPid, java.lang.String tag, int id, int userId) {
            int mustNotHaveFlags = com.android.server.notification.NotificationManagerService.this.isCallingUidSystem() ? 0 : 33856;
            com.android.server.notification.NotificationManagerService.this.cancelNotificationInternal(pkg, opPkg, callingUid, callingPid, tag, id, userId, mustNotHaveFlags);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public boolean isNotificationShown(java.lang.String pkg, java.lang.String tag, int notificationId, int userId) {
            return com.android.server.notification.NotificationManagerService.this.isNotificationShownInternal(pkg, tag, notificationId, userId);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void removeForegroundServiceFlagFromNotification(final java.lang.String pkg, final int notificationId, final int userId) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$13$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$removeForegroundServiceFlagFromNotification$0(pkg, notificationId, userId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$removeForegroundServiceFlagFromNotification$0(java.lang.String pkg, int notificationId, int userId) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                removeFlagFromNotificationLocked(pkg, notificationId, userId, 64);
            }
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void removeUserInitiatedJobFlagFromNotification(final java.lang.String pkg, final int notificationId, final int userId) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$13$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$removeUserInitiatedJobFlagFromNotification$1(pkg, notificationId, userId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$removeUserInitiatedJobFlagFromNotification$1(java.lang.String pkg, int notificationId, int userId) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                removeFlagFromNotificationLocked(pkg, notificationId, userId, 32768);
            }
        }

        private void removeFlagFromNotificationLocked(java.lang.String pkg, int notificationId, int userId, int flag) {
            boolean removeFlagFromNotification;
            int count = com.android.server.notification.NotificationManagerService.this.getNotificationCount(pkg, userId);
            if (count <= 50) {
                removeFlagFromNotification = false;
            } else {
                com.android.server.notification.NotificationManagerService.this.mUsageStats.registerOverCountQuota(pkg);
                removeFlagFromNotification = true;
            }
            if (!removeFlagFromNotification) {
                java.util.List<com.android.server.notification.NotificationRecord> enqueued = com.android.server.notification.NotificationManagerService.this.findNotificationsByListLocked(com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications, pkg, null, notificationId, userId);
                for (int i = 0; i < enqueued.size(); i++) {
                    com.android.server.notification.NotificationRecord r = enqueued.get(i);
                    if (r != null) {
                        android.service.notification.StatusBarNotification sbn = r.getSbn();
                        sbn.getNotification().flags = r.mOriginalFlags & (~flag);
                    }
                }
                com.android.server.notification.NotificationRecord r2 = com.android.server.notification.NotificationManagerService.this.findNotificationByListLocked(com.android.server.notification.NotificationManagerService.this.mNotificationList, pkg, null, notificationId, userId);
                if (r2 != null) {
                    android.service.notification.StatusBarNotification sbn2 = r2.getSbn();
                    sbn2.getNotification().flags = r2.mOriginalFlags & (~flag);
                    com.android.server.notification.NotificationManagerService.this.mRankingHelper.sort(com.android.server.notification.NotificationManagerService.this.mNotificationList);
                    com.android.server.notification.NotificationManagerService.this.mListeners.notifyPostedLocked(r2, r2);
                    return;
                }
                return;
            }
            com.android.server.notification.NotificationRecord r3 = com.android.server.notification.NotificationManagerService.this.findNotificationLocked(pkg, null, notificationId, userId);
            if (r3 != null) {
                if (com.android.server.notification.NotificationManagerService.DBG) {
                    java.lang.String type = flag == 64 ? "FGS" : "UIJ";
                    android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, "Remove " + type + " flag not allow. Cancel " + type + " notification");
                }
                com.android.server.notification.NotificationManagerService.this.removeFromNotificationListsLocked(r3);
                com.android.server.notification.NotificationManagerService.this.cancelNotificationLocked(r3, false, 8, true, null, android.os.SystemClock.elapsedRealtime());
            }
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void onConversationRemoved(java.lang.String pkg, int uid, java.util.Set<java.lang.String> shortcuts) {
            com.android.server.notification.NotificationManagerService.this.onConversationRemovedInternal(pkg, uid, shortcuts);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public int getNumNotificationChannelsForPackage(java.lang.String pkg, int uid, boolean includeDeleted) {
            return com.android.server.notification.NotificationManagerService.this.getNumNotificationChannelsForPackage(pkg, uid, includeDeleted);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public boolean areNotificationsEnabledForPackage(java.lang.String pkg, int uid) {
            return com.android.server.notification.NotificationManagerService.this.areNotificationsEnabledForPackageInt(pkg, uid);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void sendReviewPermissionsNotification() {
            if (!com.android.server.notification.NotificationManagerService.this.mShowReviewPermissionsNotification) {
                return;
            }
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            android.app.NotificationManager nm = (android.app.NotificationManager) com.android.server.notification.NotificationManagerService.this.getContext().getSystemService(android.app.NotificationManager.class);
            nm.notify(com.android.server.notification.NotificationManagerService.TAG, 71, com.android.server.notification.NotificationManagerService.this.createReviewPermissionsNotification());
            android.provider.Settings.Global.putInt(com.android.server.notification.NotificationManagerService.this.getContext().getContentResolver(), "review_permissions_notification_state", 3);
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void cleanupHistoryFiles() {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
            com.android.server.notification.NotificationManagerService.this.mHistoryManager.cleanupHistoryFiles();
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void removeBitmaps() {
            long bitmapDuration;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                for (com.android.server.notification.NotificationRecord r : com.android.server.notification.NotificationManagerService.this.mNotificationList) {
                    long timePostedMs = r.getSbn().getPostTime();
                    long timeNowMs = java.lang.System.currentTimeMillis();
                    if (com.android.server.notification.NotificationManagerService.this.mFlagResolver.isEnabled(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.DEBUG_SHORT_BITMAP_DURATION)) {
                        bitmapDuration = java.time.Duration.ofSeconds(5L).toMillis();
                    } else {
                        bitmapDuration = com.android.server.notification.NotificationManagerService.BITMAP_DURATION.toMillis();
                    }
                    if (com.android.server.notification.NotificationManagerService.isBitmapExpired(timePostedMs, timeNowMs, bitmapDuration)) {
                        com.android.server.notification.NotificationManagerService.this.removeBitmapAndRepost(r);
                    }
                }
            }
        }

        @Override // com.android.server.notification.NotificationManagerInternal
        public void setDeviceEffectsApplier(android.service.notification.DeviceEffectsApplier applier) {
            if (!android.app.Flags.modesApi()) {
                return;
            }
            if (com.android.server.notification.NotificationManagerService.this.mZenModeHelper == null) {
                throw new java.lang.IllegalStateException("ZenModeHelper is not yet ready!");
            }
            com.android.server.notification.NotificationManagerService.this.mZenModeHelper.setDeviceEffectsApplier(applier);
        }
    }

    private static boolean isBigPictureWithBitmapOrIcon(android.app.Notification n) {
        boolean isBigPicture = n.isStyle(android.app.Notification.BigPictureStyle.class);
        if (!isBigPicture) {
            return false;
        }
        boolean hasBitmap = n.extras.containsKey("android.picture") && n.extras.getParcelable("android.picture") != null;
        if (hasBitmap) {
            return true;
        }
        boolean hasIcon = n.extras.containsKey("android.pictureIcon") && n.extras.getParcelable("android.pictureIcon") != null;
        return hasIcon;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isBitmapExpired(long timePostedMs, long timeNowMs, long timeToLiveMs) {
        long timeDiff = timeNowMs - timePostedMs;
        return timeDiff > timeToLiveMs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeBitmapAndRepost(com.android.server.notification.NotificationRecord r) {
        if (!isBigPictureWithBitmapOrIcon(r.getNotification())) {
            return;
        }
        r.getNotification().extras.putParcelable("android.picture", null);
        r.getNotification().extras.putParcelable("android.pictureIcon", null);
        r.getNotification().flags |= 8;
        enqueueNotificationInternal(r.getSbn().getPackageName(), r.getSbn().getOpPkg(), r.getSbn().getUid(), r.getSbn().getInitialPid(), r.getSbn().getTag(), r.getSbn().getId(), r.getNotification(), r.getSbn().getUserId(), true, false);
    }

    int getNumNotificationChannelsForPackage(java.lang.String pkg, int uid, boolean includeDeleted) {
        return this.mPreferencesHelper.getNotificationChannels(pkg, uid, includeDeleted).getList().size();
    }

    java.lang.String encryptionNotificationKey(java.lang.Object obj) {
        java.lang.String key;
        if (obj == null) {
            return "";
        }
        if (obj instanceof java.lang.String) {
            key = (java.lang.String) obj;
        } else {
            key = obj.toString();
        }
        if (android.text.TextUtils.isEmpty(key)) {
            return "";
        }
        int indexOfAt = key.indexOf("@");
        boolean isAtInclude = indexOfAt > 0;
        boolean isPointInclude = key.indexOf(".", indexOfAt + 2) > -1;
        if (isAtInclude && isPointInclude) {
            return "****encryption****";
        }
        return key;
    }

    void cancelNotificationInternal(java.lang.String pkg, java.lang.String opPkg, int callingUid, int callingPid, java.lang.String tag, int id, int userId, int mustNotHaveFlags) {
        int uid;
        int userId2 = android.app.ActivityManager.handleIncomingUser(callingPid, callingUid, userId, true, false, "cancelNotificationWithTag", pkg);
        try {
            int uid2 = resolveNotificationUid(opPkg, pkg, callingUid, userId2);
            uid = uid2;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            uid = -1;
        }
        if (uid == -1) {
            android.util.Slog.w(TAG, opPkg + ":" + callingUid + " trying to cancel notification for nonexistent pkg " + pkg + " in user " + userId2);
            return;
        }
        if (!java.util.Objects.equals(pkg, opPkg)) {
            synchronized (this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = findNotificationLocked(pkg, tag, id, userId2);
                if (r != null && !java.util.Objects.equals(opPkg, r.getSbn().getOpPkg()) && callingUid != 1000) {
                    throw new java.lang.SecurityException(opPkg + " does not have permission to cancel a notification they did not post " + tag + " " + id);
                }
            }
        }
        if (com.android.server.notification.Flags.traceCancelEvents()) {
            android.os.Trace.instant(524288L, "cancelNotificationInternal: " + com.android.server.notification.SmallHash.hash(java.util.Objects.hashCode(tag) ^ id));
        }
        cancelNotification(uid, callingPid, pkg, tag, id, 0, mustNotHaveFlags, false, userId2, 8, null);
    }

    boolean isNotificationShownInternal(java.lang.String pkg, java.lang.String tag, int notificationId, int userId) {
        boolean z;
        synchronized (this.mNotificationLock) {
            z = findNotificationLocked(pkg, tag, notificationId, userId) != null;
        }
        return z;
    }

    void enqueueNotificationInternal(java.lang.String pkg, java.lang.String opPkg, int callingUid, int callingPid, java.lang.String tag, int id, android.app.Notification notification, int incomingUserId, boolean byForegroundService) {
        if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().interceptEnqueueNotificationInternal(pkg, opPkg, callingUid, callingPid, tag, id, notification, incomingUserId)) {
            return;
        }
        enqueueNotificationInternal(pkg, opPkg, callingUid, callingPid, tag, id, notification, incomingUserId, false, byForegroundService);
    }

    void enqueueNotificationInternal(java.lang.String pkg, java.lang.String opPkg, int callingUid, int callingPid, java.lang.String tag, int id, android.app.Notification notification, int incomingUserId, boolean postSilently, boolean byForegroundService) {
        com.android.server.notification.NotificationManagerService.PostNotificationTracker tracker = acquireWakeLockForPost(pkg, callingUid);
        try {
            boolean enqueued = enqueueNotificationInternal(pkg, opPkg, callingUid, callingPid, tag, id, notification, incomingUserId, postSilently, tracker, byForegroundService);
            if (!enqueued) {
                tracker.cancel();
            }
        } finally {
        }
    }

    private com.android.server.notification.NotificationManagerService.PostNotificationTracker acquireWakeLockForPost(final java.lang.String pkg, final int uid) {
        return (com.android.server.notification.NotificationManagerService.PostNotificationTracker) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda9
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$acquireWakeLockForPost$6(pkg, uid);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.notification.NotificationManagerService.PostNotificationTracker lambda$acquireWakeLockForPost$6(java.lang.String pkg, int uid) throws java.lang.Exception {
        android.os.PowerManager.WakeLock wakeLock = this.mPowerManager.newWakeLock(1, "NotificationManagerService:post:" + pkg);
        wakeLock.setWorkSource(new android.os.WorkSource(uid, pkg));
        wakeLock.acquire(POST_WAKE_LOCK_TIMEOUT.toMillis());
        return this.mPostNotificationTrackerFactory.newTracker(wakeLock);
    }

    /* JADX WARN: Removed duplicated region for block: B:168:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x012a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean enqueueNotificationInternal(java.lang.String r41, java.lang.String r42, int r43, int r44, java.lang.String r45, int r46, android.app.Notification r47, int r48, boolean r49, com.android.server.notification.NotificationManagerService.PostNotificationTracker r50, boolean r51) {
        /*
            Method dump skipped, instruction units count: 1370
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationManagerService.enqueueNotificationInternal(java.lang.String, java.lang.String, int, int, java.lang.String, int, android.app.Notification, int, boolean, com.android.server.notification.NotificationManagerService$PostNotificationTracker, boolean):boolean");
    }

    private android.app.NotificationChannel getNotificationChannelRestoreDeleted(java.lang.String pkg, int callingUid, int notificationUid, java.lang.String channelId, java.lang.String conversationId) {
        android.app.NotificationChannel channel = this.mPreferencesHelper.getConversationNotificationChannel(pkg, notificationUid, channelId, conversationId, true, !android.text.TextUtils.isEmpty(conversationId));
        if (channel != null && channel.isDeleted()) {
            if (!java.util.Objects.equals(conversationId, channel.getConversationId())) {
                return null;
            }
            boolean needsPolicyFileChange = this.mPreferencesHelper.createNotificationChannel(pkg, notificationUid, channel, true, this.mConditionProviders.isPackageOrComponentAllowed(pkg, android.os.UserHandle.getUserId(notificationUid)), callingUid, true);
            if (needsPolicyFileChange) {
                handleSavePolicyFile();
                return channel;
            }
            return channel;
        }
        return channel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConversationRemovedInternal(java.lang.String pkg, int uid, java.util.Set<java.lang.String> shortcuts) {
        checkCallerIsSystem();
        com.android.internal.util.Preconditions.checkStringNotEmpty(pkg);
        this.mHistoryManager.deleteConversations(pkg, uid, shortcuts);
        java.util.List<java.lang.String> deletedChannelIds = this.mPreferencesHelper.deleteConversations(pkg, uid, shortcuts, 1000, true);
        for (java.lang.String channelId : deletedChannelIds) {
            cancelAllNotificationsInt(MY_UID, MY_PID, pkg, channelId, 0, 0, android.os.UserHandle.getUserId(uid), 20);
        }
        handleSavePolicyFile();
    }

    private void makeStickyHun(android.app.Notification notification, java.lang.String pkg, int userId) {
        if (this.mPermissionHelper.hasRequestedPermission("android.permission.USE_FULL_SCREEN_INTENT", pkg, userId)) {
            notification.flags |= 16384;
        }
        if (notification.contentIntent == null) {
            notification.contentIntent = notification.fullScreenIntent;
        }
        notification.fullScreenIntent = null;
    }

    protected void fixNotification(android.app.Notification notification, java.lang.String pkg, java.lang.String tag, int id, int userId, int notificationUid, android.app.ActivityManagerInternal.ServiceNotificationPolicy fgsPolicy, boolean stripUijFlag) throws android.content.pm.PackageManager.NameNotFoundException, android.os.RemoteException {
        android.content.pm.ApplicationInfo ai = this.mPackageManagerClient.getApplicationInfoAsUser(pkg, 268435456, userId == -1 ? 0 : userId);
        android.app.Notification.addFieldsFromContext(ai, notification);
        if (notification.isForegroundService() && fgsPolicy == android.app.ActivityManagerInternal.ServiceNotificationPolicy.NOT_FOREGROUND_SERVICE) {
            notification.flags &= -65;
        }
        if (notification.isUserInitiatedJob() && stripUijFlag) {
            notification.flags &= -32769;
        }
        if (notification.isFgsOrUij()) {
            notification.flags &= -17;
        }
        if ((notification.flags & 2) > 0 && canBeNonDismissible(ai, notification)) {
            notification.flags |= 8192;
        } else {
            notification.flags &= -8193;
        }
        int canColorize = getContext().checkPermission("android.permission.USE_COLORIZED_NOTIFICATIONS", -1, notificationUid);
        if (canColorize == 0) {
            notification.flags |= 2048;
        } else {
            notification.flags &= -2049;
        }
        if (notification.extras.getBoolean("android.allowDuringSetup", false)) {
            int hasShowDuringSetupPerm = getContext().checkPermission("android.permission.NOTIFICATION_DURING_SETUP", -1, notificationUid);
            if (hasShowDuringSetupPerm != 0) {
                notification.extras.remove("android.allowDuringSetup");
                if (DBG) {
                    android.util.Slog.w(TAG, "warning: pkg " + pkg + " attempting to show during setup without holding perm android.permission.NOTIFICATION_DURING_SETUP");
                }
            }
        }
        int hasShowDuringSetupPerm2 = notification.flags;
        notification.flags = hasShowDuringSetupPerm2 & (-16385);
        if (android.app.Flags.lifetimeExtensionRefactor()) {
            notification.flags &= -65537;
        }
        if (notification.fullScreenIntent != null) {
            android.content.AttributionSource attributionSource = new android.content.AttributionSource.Builder(notificationUid).setPackageName(pkg).build();
            boolean canUseFullScreenIntent = checkUseFullScreenIntentPermission(attributionSource, ai, true);
            if (!canUseFullScreenIntent) {
                makeStickyHun(notification, pkg, userId);
            }
        }
        if (notification.actions != null) {
            boolean hasNullActions = false;
            int nActions = notification.actions.length;
            int i = 0;
            while (true) {
                if (i >= nActions) {
                    break;
                }
                if (notification.actions[i] != null) {
                    i++;
                } else {
                    hasNullActions = true;
                    break;
                }
            }
            if (hasNullActions) {
                java.util.ArrayList<android.app.Notification.Action> nonNullActions = new java.util.ArrayList<>();
                for (int i2 = 0; i2 < nActions; i2++) {
                    if (notification.actions[i2] != null) {
                        nonNullActions.add(notification.actions[i2]);
                    }
                }
                if (nonNullActions.size() != 0) {
                    notification.actions = (android.app.Notification.Action[]) nonNullActions.toArray(new android.app.Notification.Action[0]);
                } else {
                    notification.actions = null;
                }
            }
        }
        if (notification.isStyle(android.app.Notification.CallStyle.class)) {
            android.app.Notification.Builder builder = android.app.Notification.Builder.recoverBuilder(getContext(), notification);
            android.app.Notification.CallStyle style = (android.app.Notification.CallStyle) builder.getStyle();
            java.util.List<android.app.Notification.Action> actions = style.getActionsListWithSystemActions();
            notification.actions = new android.app.Notification.Action[actions.size()];
            actions.toArray(notification.actions);
        }
        if (notification.isStyle(android.app.Notification.MediaStyle.class) || notification.isStyle(android.app.Notification.DecoratedMediaCustomViewStyle.class)) {
            int hasMediaContentControlPermission = getContext().checkPermission("android.permission.MEDIA_CONTENT_CONTROL", -1, notificationUid);
            if (hasMediaContentControlPermission != 0) {
                notification.extras.remove("android.mediaRemoteDevice");
                notification.extras.remove("android.mediaRemoteIcon");
                notification.extras.remove("android.mediaRemoteIntent");
                if (DBG) {
                    android.util.Slog.w(TAG, "Package " + pkg + ": Use of setRemotePlayback requires the MEDIA_CONTENT_CONTROL permission");
                }
            }
            if (android.app.compat.CompatChanges.isChangeEnabled(ENFORCE_NO_CLEAR_FLAG_ON_MEDIA_NOTIFICATION, notificationUid)) {
                notification.flags |= 32;
            }
        }
        if (notification.extras.containsKey("android.substName")) {
            int hasSubstituteAppNamePermission = getContext().checkPermission("android.permission.SUBSTITUTE_NOTIFICATION_APP_NAME", -1, notificationUid);
            if (hasSubstituteAppNamePermission != 0) {
                notification.extras.remove("android.substName");
                if (DBG) {
                    android.util.Slog.w(TAG, "warning: pkg " + pkg + " attempting to substitute app name without holding perm android.permission.SUBSTITUTE_NOTIFICATION_APP_NAME");
                }
            }
        }
        checkRemoteViews(pkg, tag, id, notification);
        if (com.android.server.notification.Flags.allNotifsNeedTtl() && notification.getTimeoutAfter() == 0) {
            notification.setTimeoutAfter(NOTIFICATION_TTL);
        }
    }

    private boolean canBeNonDismissible(android.content.pm.ApplicationInfo ai, android.app.Notification notification) {
        return notification.isMediaNotification() || isEnterpriseExempted(ai) || notification.isStyle(android.app.Notification.CallStyle.class) || isDefaultSearchSelectorPackage(ai.packageName) || isDefaultAdservicesPackage(ai.packageName);
    }

    private boolean isDefaultSearchSelectorPackage(java.lang.String pkg) {
        return java.util.Objects.equals(this.mDefaultSearchSelectorPkg, pkg);
    }

    private boolean isDefaultAdservicesPackage(java.lang.String pkg) {
        if (this.mAdservicesModuleInfo == null) {
            return false;
        }
        for (java.lang.String apkName : this.mAdservicesModuleInfo.getApkInApexPackageNames()) {
            if (java.util.Objects.equals(apkName, pkg)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEnterpriseExempted(android.content.pm.ApplicationInfo ai) {
        if (this.mDpm == null || !(this.mDpm.isActiveProfileOwner(ai.uid) || this.mDpm.isActiveDeviceOwner(ai.uid))) {
            return ai.uid != 1000 && this.mAppOps.checkOpNoThrow(125, ai.uid, ai.packageName) == 0;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkUseFullScreenIntentPermission(android.content.AttributionSource attributionSource, android.content.pm.ApplicationInfo applicationInfo, boolean forDataDelivery) {
        if (applicationInfo.targetSdkVersion < 29) {
            return true;
        }
        int permissionResult = forDataDelivery ? this.mPermissionManager.checkPermissionForDataDelivery("android.permission.USE_FULL_SCREEN_INTENT", attributionSource, (java.lang.String) null) : this.mPermissionManager.checkPermissionForPreflight("android.permission.USE_FULL_SCREEN_INTENT", attributionSource);
        return permissionResult == 0;
    }

    private void checkRemoteViews(java.lang.String pkg, java.lang.String tag, int id, android.app.Notification notification) {
        if (android.app.Flags.removeRemoteViews()) {
            if (notification.contentView != null || notification.bigContentView != null || notification.headsUpContentView != null || (notification.publicVersion != null && (notification.publicVersion.contentView != null || notification.publicVersion.bigContentView != null || notification.publicVersion.headsUpContentView != null))) {
                android.util.Slog.i(TAG, "Removed customViews for " + pkg);
                this.mUsageStats.registerImageRemoved(pkg);
            }
            notification.contentView = null;
            notification.bigContentView = null;
            notification.headsUpContentView = null;
            if (notification.publicVersion != null) {
                notification.publicVersion.contentView = null;
                notification.publicVersion.bigContentView = null;
                notification.publicVersion.headsUpContentView = null;
                return;
            }
            return;
        }
        if (removeRemoteView(pkg, tag, id, notification.contentView)) {
            notification.contentView = null;
        }
        if (removeRemoteView(pkg, tag, id, notification.bigContentView)) {
            notification.bigContentView = null;
        }
        if (removeRemoteView(pkg, tag, id, notification.headsUpContentView)) {
            notification.headsUpContentView = null;
        }
        if (notification.publicVersion != null) {
            if (removeRemoteView(pkg, tag, id, notification.publicVersion.contentView)) {
                notification.publicVersion.contentView = null;
            }
            if (removeRemoteView(pkg, tag, id, notification.publicVersion.bigContentView)) {
                notification.publicVersion.bigContentView = null;
            }
            if (removeRemoteView(pkg, tag, id, notification.publicVersion.headsUpContentView)) {
                notification.publicVersion.headsUpContentView = null;
            }
        }
    }

    private boolean removeRemoteView(java.lang.String pkg, java.lang.String tag, int id, android.widget.RemoteViews contentView) {
        if (contentView == null) {
            return false;
        }
        int contentViewSize = contentView.estimateMemoryUsage();
        if (contentViewSize > this.mWarnRemoteViewsSizeBytes && contentViewSize < this.mStripRemoteViewsSizeBytes) {
            android.util.Slog.w(TAG, "RemoteViews too large on pkg: " + pkg + " tag: " + tag + " id: " + id + " this might be stripped in a future release");
        }
        if (contentViewSize < this.mStripRemoteViewsSizeBytes) {
            return false;
        }
        this.mUsageStats.registerImageRemoved(pkg);
        android.util.Slog.w(TAG, "Removed too large RemoteViews (" + contentViewSize + " bytes) on pkg: " + pkg + " tag: " + tag + " id: " + id);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNotificationBubbleFlags(com.android.server.notification.NotificationRecord r, boolean isAppForeground) {
        android.app.Notification notification = r.getNotification();
        android.app.Notification.BubbleMetadata metadata = notification.getBubbleMetadata();
        if (metadata == null) {
            return;
        }
        if (!isAppForeground) {
            int flags = metadata.getFlags();
            metadata.setFlags(flags & (-2));
        }
        if (!metadata.isBubbleSuppressable()) {
            int flags2 = metadata.getFlags();
            metadata.setFlags(flags2 & (-9));
        }
    }

    protected void doChannelWarningToast(int forUid, final java.lang.CharSequence toastText) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda11
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$doChannelWarningToast$7(toastText);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doChannelWarningToast$7(java.lang.CharSequence toastText) throws java.lang.Exception {
        boolean warningEnabled = android.provider.Settings.Global.getInt(getContext().getContentResolver(), "show_notification_channel_warnings", 0) != 0;
        if (warningEnabled) {
            android.widget.Toast toast = android.widget.Toast.makeText(getContext(), this.mHandler.getLooper(), toastText, 0);
            toast.show();
        }
    }

    int resolveNotificationUid(java.lang.String callingPkg, java.lang.String targetPkg, int callingUid, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        if (userId == -1) {
            userId = 0;
        }
        if (isCallerSameApp(targetPkg, callingUid, userId) && (android.text.TextUtils.equals(callingPkg, targetPkg) || isCallerSameApp(callingPkg, callingUid, userId))) {
            return callingUid;
        }
        int targetUid = this.mPackageManagerClient.getPackageUidAsUser(targetPkg, userId);
        if (isCallerAndroid(callingPkg, callingUid) || this.mPreferencesHelper.isDelegateAllowed(targetPkg, targetUid, callingPkg, callingUid)) {
            return targetUid;
        }
        throw new java.lang.SecurityException("Caller " + callingPkg + ":" + callingUid + " cannot post for pkg " + targetPkg + " in user " + userId);
    }

    public boolean hasFlag(int flags, int flag) {
        return (flags & flag) != 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:161:0x009e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x010d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean checkDisqualifyingFeatures(int r20, int r21, int r22, java.lang.String r23, com.android.server.notification.NotificationRecord r24, boolean r25, boolean r26) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 906
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationManagerService.checkDisqualifyingFeatures(int, int, int, java.lang.String, com.android.server.notification.NotificationRecord, boolean, boolean):boolean");
    }

    private boolean isCallNotification(java.lang.String pkg, int uid, android.app.Notification n) {
        if (n.isStyle(android.app.Notification.CallStyle.class)) {
            return isCallNotification(pkg, uid);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0025  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean isCallNotification(java.lang.String r6, int r7) {
        /*
            r5 = this;
            long r0 = android.os.Binder.clearCallingIdentity()
            android.content.pm.PackageManager r2 = r5.mPackageManagerClient     // Catch: java.lang.Throwable -> L35
            java.lang.String r3 = "android.software.telecom"
            boolean r2 = r2.hasSystemFeature(r3)     // Catch: java.lang.Throwable -> L35
            r3 = 0
            if (r2 == 0) goto L30
            android.telecom.TelecomManager r2 = r5.mTelecomManager     // Catch: java.lang.Throwable -> L35
            if (r2 == 0) goto L30
            android.telecom.TelecomManager r2 = r5.mTelecomManager     // Catch: java.lang.IllegalStateException -> L2a java.lang.Throwable -> L35
            boolean r2 = r2.isInManagedCall()     // Catch: java.lang.IllegalStateException -> L2a java.lang.Throwable -> L35
            if (r2 != 0) goto L25
            android.telecom.TelecomManager r2 = r5.mTelecomManager     // Catch: java.lang.IllegalStateException -> L2a java.lang.Throwable -> L35
            android.os.UserHandle r4 = android.os.UserHandle.ALL     // Catch: java.lang.IllegalStateException -> L2a java.lang.Throwable -> L35
            boolean r2 = r2.isInSelfManagedCall(r6, r4)     // Catch: java.lang.IllegalStateException -> L2a java.lang.Throwable -> L35
            if (r2 == 0) goto L26
        L25:
            r3 = 1
        L26:
            android.os.Binder.restoreCallingIdentity(r0)
            return r3
        L2a:
            r2 = move-exception
            android.os.Binder.restoreCallingIdentity(r0)
            return r3
        L30:
            android.os.Binder.restoreCallingIdentity(r0)
            return r3
        L35:
            r2 = move-exception
            android.os.Binder.restoreCallingIdentity(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationManagerService.isCallNotification(java.lang.String, int):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean areNotificationsEnabledForPackageInt(java.lang.String pkg, int uid) {
        return this.mPermissionHelper.hasPermission(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNotificationCount(java.lang.String pkg, int userId) {
        int count = 0;
        synchronized (this.mNotificationLock) {
            int numListSize = this.mNotificationList.size();
            for (int i = 0; i < numListSize; i++) {
                com.android.server.notification.NotificationRecord existing = this.mNotificationList.get(i);
                if (existing.getSbn().getPackageName().equals(pkg) && existing.getSbn().getUserId() == userId) {
                    count++;
                }
            }
            int numEnqSize = this.mEnqueuedNotifications.size();
            for (int i2 = 0; i2 < numEnqSize; i2++) {
                com.android.server.notification.NotificationRecord existing2 = this.mEnqueuedNotifications.get(i2);
                if (existing2.getSbn().getPackageName().equals(pkg) && existing2.getSbn().getUserId() == userId) {
                    count++;
                }
            }
        }
        return count;
    }

    protected int getNotificationCount(java.lang.String pkg, int userId, int excludedId, java.lang.String excludedTag) {
        int count = 0;
        synchronized (this.mNotificationLock) {
            int N = this.mNotificationList.size();
            for (int i = 0; i < N; i++) {
                com.android.server.notification.NotificationRecord existing = this.mNotificationList.get(i);
                if (existing.getSbn().getPackageName().equals(pkg) && existing.getSbn().getUserId() == userId && (existing.getSbn().getId() != excludedId || !android.text.TextUtils.equals(existing.getSbn().getTag(), excludedTag))) {
                    count++;
                }
            }
            int M = this.mEnqueuedNotifications.size();
            for (int i2 = 0; i2 < M; i2++) {
                com.android.server.notification.NotificationRecord existing2 = this.mEnqueuedNotifications.get(i2);
                if (existing2.getSbn().getPackageName().equals(pkg) && existing2.getSbn().getUserId() == userId) {
                    count++;
                }
            }
        }
        return count;
    }

    boolean isRecordBlockedLocked(com.android.server.notification.NotificationRecord r) {
        java.lang.String pkg = r.getSbn().getPackageName();
        int callingUid = r.getSbn().getUid();
        return this.mPreferencesHelper.isGroupBlocked(pkg, callingUid, r.getChannel().getGroup()) || r.getImportance() == 0;
    }

    protected class SnoozeNotificationRunnable implements java.lang.Runnable {
        private final long mDuration;
        private final java.lang.String mKey;
        private final java.lang.String mSnoozeCriterionId;

        SnoozeNotificationRunnable(java.lang.String key, long duration, java.lang.String snoozeCriterionId) {
            this.mKey = key;
            this.mDuration = duration;
            this.mSnoozeCriterionId = snoozeCriterionId;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.findInCurrentAndSnoozedNotificationByKeyLocked(this.mKey);
                if (r != null) {
                    snoozeLocked(r);
                }
            }
        }

        void snoozeLocked(com.android.server.notification.NotificationRecord r) {
            java.util.List<com.android.server.notification.NotificationRecord> recordsToSnooze = new java.util.ArrayList<>();
            if (r.getSbn().isGroup()) {
                java.util.List<com.android.server.notification.NotificationRecord> groupNotifications = com.android.server.notification.NotificationManagerService.this.findCurrentAndSnoozedGroupNotificationsLocked(r.getSbn().getPackageName(), r.getSbn().getGroupKey(), r.getSbn().getUserId());
                if (r.getNotification().isGroupSummary()) {
                    for (int i = 0; i < groupNotifications.size(); i++) {
                        if (!this.mKey.equals(groupNotifications.get(i).getKey())) {
                            recordsToSnooze.add(groupNotifications.get(i));
                        }
                    }
                } else if (com.android.server.notification.NotificationManagerService.this.mSummaryByGroupKey.containsKey(r.getSbn().getGroupKey()) && groupNotifications.size() == 2) {
                    for (int i2 = 0; i2 < groupNotifications.size(); i2++) {
                        if (!this.mKey.equals(groupNotifications.get(i2).getKey())) {
                            recordsToSnooze.add(groupNotifications.get(i2));
                        }
                    }
                }
            }
            recordsToSnooze.add(r);
            if (com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.canSnooze(recordsToSnooze.size())) {
                for (int i3 = 0; i3 < recordsToSnooze.size(); i3++) {
                    snoozeNotificationLocked(recordsToSnooze.get(i3));
                }
                return;
            }
            android.util.Log.w(com.android.server.notification.NotificationManagerService.TAG, "Cannot snooze " + r.getKey() + ": too many snoozed notifications");
        }

        void snoozeNotificationLocked(com.android.server.notification.NotificationRecord r) {
            com.android.internal.logging.MetricsLogger.action(r.getLogMaker().setCategory(com.android.internal.util.FrameworkStatsLog.SENSITIVE_NOTIFICATION_APP_PROTECTION_SESSION).setType(2).addTaggedData(1139, java.lang.Long.valueOf(this.mDuration)).addTaggedData(com.android.internal.util.FrameworkStatsLog.SENSITIVE_NOTIFICATION_APP_PROTECTION_APPLIED, java.lang.Integer.valueOf(this.mSnoozeCriterionId == null ? 0 : 1)));
            com.android.server.notification.NotificationManagerService.this.mNotificationRecordLogger.log(com.android.server.notification.NotificationRecordLogger.NotificationEvent.NOTIFICATION_SNOOZED, r);
            com.android.server.notification.NotificationManagerService.this.reportUserInteraction(r);
            boolean wasPosted = com.android.server.notification.NotificationManagerService.this.removeFromNotificationListsLocked(r);
            com.android.server.notification.NotificationManagerService.this.cancelNotificationLocked(r, false, 18, wasPosted, null, android.os.SystemClock.elapsedRealtime());
            com.android.server.notification.NotificationManagerService.this.mAttentionHelper.updateLightsLocked();
            if (isSnoozable(r)) {
                if (this.mSnoozeCriterionId != null) {
                    com.android.server.notification.NotificationManagerService.this.mAssistants.notifyAssistantSnoozedLocked(r, this.mSnoozeCriterionId);
                    com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.snooze(r, this.mSnoozeCriterionId);
                } else {
                    com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.snooze(r, this.mDuration);
                }
                r.recordSnoozed();
                com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
            }
        }

        private boolean isSnoozable(com.android.server.notification.NotificationRecord record) {
            return (record.getNotification().isGroupSummary() && "ranker_group".equals(record.getNotification().getGroup())) ? false : true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unsnoozeAll() {
        synchronized (this.mNotificationLock) {
            this.mSnoozeHelper.repostAll(this.mUserProfiles.getCurrentProfileIds());
            handleSavePolicyFile();
        }
    }

    protected class CancelNotificationRunnable implements java.lang.Runnable {
        private final int mCallingPid;
        private final int mCallingUid;
        private final long mCancellationElapsedTimeMs;
        private final int mCount;
        private final int mId;
        private final com.android.server.notification.ManagedServices.ManagedServiceInfo mListener;
        private final int mMustHaveFlags;
        private final int mMustNotHaveFlags;
        private final java.lang.String mPkg;
        private final int mRank;
        private final int mReason;
        private final boolean mSendDelete;
        private final java.lang.String mTag;
        private final int mUserId;

        CancelNotificationRunnable(int callingUid, int callingPid, java.lang.String pkg, java.lang.String tag, int id, int mustHaveFlags, int mustNotHaveFlags, boolean sendDelete, int userId, int reason, int rank, int count, com.android.server.notification.ManagedServices.ManagedServiceInfo listener, long cancellationElapsedTimeMs) {
            this.mCallingUid = callingUid;
            this.mCallingPid = callingPid;
            this.mPkg = pkg;
            this.mTag = tag;
            this.mId = id;
            this.mMustHaveFlags = mustHaveFlags;
            this.mMustNotHaveFlags = mustNotHaveFlags;
            this.mSendDelete = sendDelete;
            this.mUserId = userId;
            this.mReason = reason;
            this.mRank = rank;
            this.mCount = count;
            this.mListener = listener;
            this.mCancellationElapsedTimeMs = cancellationElapsedTimeMs;
        }

        @Override // java.lang.Runnable
        public void run() {
            int packageImportance;
            if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().isLoggable()) {
                    android.util.Slog.v(com.android.server.notification.NotificationManagerService.TAG, "Notification--cancelNotification: pkg=" + this.mPkg + ",callingUid:" + this.mCallingUid + ",callingPid=" + this.mCallingPid + ",tag:" + com.android.server.notification.NotificationManagerService.this.encryptionNotificationKey(this.mTag) + ",id:" + this.mId + ",userId:" + this.mUserId + ",reason:" + this.mReason);
                }
                com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().detectCancelAction(com.android.server.notification.NotificationManagerService.this.getContext(), this.mId, this.mPkg, this.mUserId);
            }
            java.lang.String listenerName = this.mListener == null ? null : this.mListener.component.toShortString();
            if (com.android.server.notification.NotificationManagerService.DBG) {
                com.android.server.EventLogTags.writeNotificationCancel(this.mCallingUid, this.mCallingPid, this.mPkg, this.mId, this.mTag, this.mUserId, this.mMustHaveFlags, this.mMustNotHaveFlags, this.mReason, listenerName);
            }
            if (android.app.Flags.lifetimeExtensionRefactor()) {
                int packageImportance2 = com.android.server.notification.NotificationManagerService.this.getPackageImportanceWithIdentity(this.mPkg);
                packageImportance = packageImportance2;
            } else {
                packageImportance = 0;
            }
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.this.findNotificationLocked(this.mPkg, this.mTag, this.mId, this.mUserId);
                boolean shouldUpdateNavigationMode = true;
                if (r != null) {
                    if (this.mReason == 1) {
                        com.android.server.notification.NotificationManagerService.this.mUsageStats.registerClickedByUser(r);
                    }
                    if ((this.mReason != 10 || !r.getNotification().isBubbleNotification()) && (this.mReason != 1 || !r.canBubble() || !r.isFlagBubbleRemoved())) {
                        if ((r.getNotification().flags & this.mMustHaveFlags) != this.mMustHaveFlags) {
                            return;
                        }
                        if ((r.getNotification().flags & this.mMustNotHaveFlags) != 0) {
                            if (android.app.Flags.lifetimeExtensionRefactor()) {
                                com.android.server.notification.NotificationManagerService.this.maybeNotifySystemUiListenerLifetimeExtendedLocked(r, this.mPkg, packageImportance);
                            }
                            return;
                        }
                        if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                            com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().setKeepAliveAppIfNeed(this.mPkg, this.mId, false);
                        }
                        com.android.server.notification.NotificationManagerService.FlagChecker childrenFlagChecker = new com.android.server.notification.NotificationManagerService.FlagChecker() { // from class: com.android.server.notification.NotificationManagerService$CancelNotificationRunnable$$ExternalSyntheticLambda0
                            @Override // com.android.server.notification.NotificationManagerService.FlagChecker
                            public final boolean apply(int i) {
                                return this.f$0.lambda$run$0(i);
                            }
                        };
                        boolean wasPosted = com.android.server.notification.NotificationManagerService.this.removeFromNotificationListsLocked(r);
                        com.android.server.notification.NotificationManagerService.this.cancelNotificationLocked(r, this.mSendDelete, this.mReason, this.mRank, this.mCount, wasPosted, listenerName, this.mCancellationElapsedTimeMs);
                        com.android.server.notification.NotificationManagerService.this.cancelGroupChildrenLocked(r, this.mCallingUid, this.mCallingPid, listenerName, this.mSendDelete, childrenFlagChecker, this.mReason, this.mCancellationElapsedTimeMs);
                        com.android.server.notification.NotificationManagerService.this.mAttentionHelper.updateLightsLocked();
                        if (com.android.server.notification.NotificationManagerService.this.mShortcutHelper != null) {
                            com.android.server.notification.NotificationManagerService.this.mShortcutHelper.maybeListenForShortcutChangesForBubbles(r, true, com.android.server.notification.NotificationManagerService.this.mHandler);
                        }
                    } else {
                        int flags = 0;
                        if (r.getNotification().getBubbleMetadata() != null) {
                            flags = r.getNotification().getBubbleMetadata().getFlags();
                        }
                        com.android.server.notification.NotificationManagerService.this.mNotificationDelegate.onBubbleMetadataFlagChanged(r.getKey(), flags | 2);
                    }
                } else {
                    if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() == null || !com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().shouldUpdateNavigationMode(this.mPkg, this.mId, this.mCallingUid)) {
                        shouldUpdateNavigationMode = false;
                    }
                    if (shouldUpdateNavigationMode && this.mReason == 8) {
                        com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().setKeepAliveAppIfNeed(this.mPkg, this.mId, false);
                    }
                    if (this.mReason != 18) {
                        boolean wasSnoozed = com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.cancel(this.mUserId, this.mPkg, this.mTag, this.mId);
                        if (wasSnoozed) {
                            com.android.server.notification.NotificationManagerService.this.handleSavePolicyFile();
                        }
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$run$0(int flags) {
            if (this.mReason == 2 || this.mReason == 1 || this.mReason == 3) {
                if ((flags & 4096) != 0) {
                    return false;
                }
            } else if (this.mReason == 8 && ((flags & 64) != 0 || (32768 & flags) != 0)) {
                return false;
            }
            return (this.mMustNotHaveFlags & flags) == 0;
        }
    }

    protected static class ShowNotificationPermissionPromptRunnable implements java.lang.Runnable {
        private final java.lang.String mPkgName;
        private final com.android.server.policy.PermissionPolicyInternal mPpi;
        private final int mTaskId;
        private final int mUserId;

        ShowNotificationPermissionPromptRunnable(java.lang.String pkg, int user, int task, com.android.server.policy.PermissionPolicyInternal pPi) {
            this.mPkgName = pkg;
            this.mUserId = user;
            this.mTaskId = task;
            this.mPpi = pPi;
        }

        public boolean equals(java.lang.Object o) {
            if (!(o instanceof com.android.server.notification.NotificationManagerService.ShowNotificationPermissionPromptRunnable)) {
                return false;
            }
            com.android.server.notification.NotificationManagerService.ShowNotificationPermissionPromptRunnable other = (com.android.server.notification.NotificationManagerService.ShowNotificationPermissionPromptRunnable) o;
            return java.util.Objects.equals(this.mPkgName, other.mPkgName) && this.mUserId == other.mUserId && this.mTaskId == other.mTaskId;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.mPkgName, java.lang.Integer.valueOf(this.mUserId), java.lang.Integer.valueOf(this.mTaskId));
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mPpi.showNotificationPromptIfNeeded(this.mPkgName, this.mUserId, this.mTaskId);
        }
    }

    protected class EnqueueNotificationRunnable implements java.lang.Runnable {
        private final boolean isAppForeground;
        private final com.android.server.notification.NotificationManagerService.PostNotificationTracker mTracker;
        private final com.android.server.notification.NotificationRecord r;
        private final int userId;

        EnqueueNotificationRunnable(int userId, com.android.server.notification.NotificationRecord r, boolean foreground, com.android.server.notification.NotificationManagerService.PostNotificationTracker tracker) {
            this.userId = userId;
            this.r = r;
            this.isAppForeground = foreground;
            this.mTracker = (com.android.server.notification.NotificationManagerService.PostNotificationTracker) com.android.internal.util.Preconditions.checkNotNull(tracker);
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean enqueued = false;
            try {
                enqueued = enqueueNotification();
            } finally {
                if (!enqueued) {
                    this.mTracker.cancel();
                }
            }
        }

        private boolean enqueueNotification() {
            java.lang.String pkg;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                this.r.getNotification().overrideAllowlistToken(com.android.server.notification.NotificationManagerService.ALLOWLIST_TOKEN);
                long snoozeAt = com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.getSnoozeTimeForUnpostedNotification(this.r.getUser().getIdentifier(), this.r.getSbn().getPackageName(), this.r.getSbn().getKey()).longValue();
                long currentTime = java.lang.System.currentTimeMillis();
                if (snoozeAt > currentTime) {
                    com.android.server.notification.NotificationManagerService.this.new SnoozeNotificationRunnable(this.r.getSbn().getKey(), snoozeAt - currentTime, null).snoozeLocked(this.r);
                    return false;
                }
                java.lang.String contextId = com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.getSnoozeContextForUnpostedNotification(this.r.getUser().getIdentifier(), this.r.getSbn().getPackageName(), this.r.getSbn().getKey());
                if (contextId == null) {
                    com.android.server.notification.NotificationRecord oldR = com.android.server.notification.NotificationManagerService.this.findNotificationByKeyLocked(this.r.getKey());
                    if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().onHandleEnqueuedNotification(this.r, oldR)) {
                        return false;
                    }
                    com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.add(this.r);
                    if (com.android.server.notification.Flags.allNotifsNeedTtl()) {
                        com.android.server.notification.NotificationManagerService.this.mTtlHelper.scheduleTimeoutLocked(this.r, android.os.SystemClock.elapsedRealtime());
                    } else {
                        com.android.server.notification.NotificationManagerService.this.scheduleTimeoutLocked(this.r);
                    }
                    android.service.notification.StatusBarNotification n = this.r.getSbn();
                    if (com.android.server.notification.NotificationManagerService.DBG) {
                        android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, "EnqueueNotificationRunnable.run for: " + n.getKey());
                    }
                    com.android.server.notification.NotificationRecord old = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(n.getKey());
                    if (old != null) {
                        this.r.copyRankingInformation(old);
                    }
                    int callingUid = n.getUid();
                    int callingPid = n.getInitialPid();
                    android.app.Notification notification = n.getNotification();
                    java.lang.String pkg2 = n.getPackageName();
                    int id = n.getId();
                    java.lang.String tag = n.getTag();
                    com.android.server.notification.NotificationManagerService.this.updateNotificationBubbleFlags(this.r, this.isAppForeground);
                    com.android.server.notification.NotificationManagerService.this.handleGroupedNotificationLocked(this.r, old, callingUid, callingPid);
                    if (n.isGroup() && notification.isGroupChild()) {
                        com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.repostGroupSummary(pkg2, this.r.getUserId(), n.getGroupKey());
                    }
                    if (!pkg2.equals("com.android.providers.downloads") || android.util.Log.isLoggable("DownloadManager", 2)) {
                        int enqueueStatus = 0;
                        if (old != null) {
                            enqueueStatus = 1;
                        }
                        pkg = pkg2;
                        com.android.server.EventLogTags.writeNotificationEnqueue(callingUid, callingPid, pkg, id, tag, this.userId, notification.toString(), enqueueStatus);
                    } else {
                        pkg = pkg2;
                    }
                    boolean isForwardToAssistants = false;
                    long mcsAssistantDelayTime = com.android.server.notification.NotificationManagerService.DELAY_FOR_ASSISTANT_TIME;
                    if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                        isForwardToAssistants = com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().isForwardToAssistants(this.r.getSbn());
                        mcsAssistantDelayTime = com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().getMcsAssistantDelayTime(com.android.server.notification.NotificationManagerService.DELAY_FOR_ASSISTANT_TIME);
                    }
                    if (isForwardToAssistants) {
                        android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, "This application enqueue notifications is need postDelayed mcsAssistantDelayTime: " + mcsAssistantDelayTime);
                    } else {
                        android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, "This application enqueue notifications is not need postDelayed");
                    }
                    if (com.android.server.notification.NotificationManagerService.this.mAssistants.isEnabled() && isForwardToAssistants) {
                        com.android.server.notification.NotificationManagerService.this.mAssistants.onNotificationEnqueuedLocked(this.r);
                        com.android.server.notification.NotificationManagerService.this.mHandler.postDelayed(com.android.server.notification.NotificationManagerService.this.new PostNotificationRunnable(this.r.getKey(), this.r.getSbn().getPackageName(), this.r.getUid(), this.mTracker), mcsAssistantDelayTime);
                    } else {
                        com.android.server.notification.NotificationManagerService.this.mHandler.post(com.android.server.notification.NotificationManagerService.this.new PostNotificationRunnable(this.r.getKey(), this.r.getSbn().getPackageName(), this.r.getUid(), this.mTracker));
                    }
                    return true;
                }
                com.android.server.notification.NotificationManagerService.this.new SnoozeNotificationRunnable(this.r.getSbn().getKey(), 0L, contextId).snoozeLocked(this.r);
                return false;
            }
        }
    }

    boolean isPackagePausedOrSuspended(java.lang.String pkg, int uid) {
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        int flags = pmi.getDistractingPackageRestrictions(pkg, android.os.Binder.getCallingUserHandle().getIdentifier());
        boolean isPaused = (flags & 2) != 0;
        return isPaused | isPackageSuspendedForUser(pkg, uid);
    }

    protected class PostNotificationRunnable implements java.lang.Runnable {
        private final java.lang.String key;
        private final com.android.server.notification.NotificationManagerService.PostNotificationTracker mTracker;
        private final java.lang.String pkg;
        private final int uid;

        PostNotificationRunnable(java.lang.String key, java.lang.String pkg, int uid, com.android.server.notification.NotificationManagerService.PostNotificationTracker tracker) {
            this.key = key;
            this.pkg = pkg;
            this.uid = uid;
            this.mTracker = (com.android.server.notification.NotificationManagerService.PostNotificationTracker) com.android.internal.util.Preconditions.checkNotNull(tracker);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                try {
                    boolean posted = postNotification();
                    if (posted) {
                        return;
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "Error posting", e);
                    if (0 != 0) {
                        return;
                    }
                }
                this.mTracker.cancel();
            } catch (java.lang.Throwable th) {
                if (0 == 0) {
                    this.mTracker.cancel();
                }
                throw th;
            }
        }

        private boolean postNotification() throws java.lang.Throwable {
            com.android.server.notification.NotificationRecord old;
            boolean appBanned = !com.android.server.notification.NotificationManagerService.this.areNotificationsEnabledForPackageInt(this.pkg, this.uid);
            boolean isCallNotification = com.android.server.notification.NotificationManagerService.this.isCallNotification(this.pkg, this.uid);
            boolean posted = false;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                try {
                    try {
                        com.android.server.notification.NotificationRecord r = com.android.server.notification.NotificationManagerService.findNotificationByListLocked(com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications, this.key);
                        try {
                            try {
                                if (r == null) {
                                    android.util.Slog.i(com.android.server.notification.NotificationManagerService.TAG, "Cannot find enqueued record for key: " + this.key);
                                    int N = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.size();
                                    int i = 0;
                                    while (true) {
                                        if (i >= N) {
                                            break;
                                        }
                                        com.android.server.notification.NotificationRecord enqueued = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.get(i);
                                        if (java.util.Objects.equals(this.key, enqueued.getKey())) {
                                            com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.remove(i);
                                            break;
                                        }
                                        i++;
                                    }
                                    return false;
                                }
                                if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().isInterceptNotification(r)) {
                                    android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, "Cannot post, assistantImportance is zero for key: " + this.key);
                                    int N2 = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.size();
                                    int i2 = 0;
                                    while (true) {
                                        if (i2 >= N2) {
                                            break;
                                        }
                                        com.android.server.notification.NotificationRecord enqueued2 = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.get(i2);
                                        if (java.util.Objects.equals(this.key, enqueued2.getKey())) {
                                            com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.remove(i2);
                                            break;
                                        }
                                        i2++;
                                    }
                                    return false;
                                }
                                final android.service.notification.StatusBarNotification n = r.getSbn();
                                android.app.Notification notification = n.getNotification();
                                boolean isCallNotificationAndCorrectStyle = isCallNotification && notification.isStyle(android.app.Notification.CallStyle.class);
                                if (!notification.isMediaNotification() && !isCallNotificationAndCorrectStyle && (appBanned || com.android.server.notification.NotificationManagerService.this.isRecordBlockedLocked(r))) {
                                    com.android.server.notification.NotificationManagerService.this.mUsageStats.registerBlocked(r);
                                    if (com.android.server.notification.NotificationManagerService.DBG) {
                                        android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "Suppressing notification from package " + this.pkg);
                                    }
                                    int N3 = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.size();
                                    int i3 = 0;
                                    while (true) {
                                        if (i3 >= N3) {
                                            break;
                                        }
                                        com.android.server.notification.NotificationRecord enqueued3 = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.get(i3);
                                        if (java.util.Objects.equals(this.key, enqueued3.getKey())) {
                                            com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.remove(i3);
                                            break;
                                        }
                                        i3++;
                                    }
                                    return false;
                                }
                                boolean isPackageSuspended = com.android.server.notification.NotificationManagerService.this.isPackagePausedOrSuspended(r.getSbn().getPackageName(), r.getUid());
                                r.setHidden(isPackageSuspended);
                                if (isPackageSuspended) {
                                    com.android.server.notification.NotificationManagerService.this.mUsageStats.registerSuspendedByAdmin(r);
                                }
                                com.android.server.notification.NotificationRecord old2 = com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.get(this.key);
                                if (old2 == null || old2.getSbn().getInstanceId() == null) {
                                    n.setInstanceId(com.android.server.notification.NotificationManagerService.this.mNotificationInstanceIdSequence.newInstanceId());
                                } else {
                                    n.setInstanceId(old2.getSbn().getInstanceId());
                                }
                                int index = com.android.server.notification.NotificationManagerService.this.indexOfNotificationLocked(n.getKey());
                                if (index < 0) {
                                    com.android.server.notification.NotificationManagerService.this.mNotificationList.add(r);
                                    com.android.server.notification.NotificationManagerService.this.mUsageStats.registerPostedByApp(r);
                                    com.android.server.notification.NotificationManagerService.this.mUsageStatsManagerInternal.reportNotificationPosted(r.getSbn().getOpPkg(), r.getSbn().getUser(), this.mTracker.getStartTime());
                                    boolean isInterruptive = com.android.server.notification.NotificationManagerService.this.isVisuallyInterruptive(null, r);
                                    r.setInterruptive(isInterruptive);
                                    r.setTextChanged(isInterruptive);
                                    old = old2;
                                } else {
                                    com.android.server.notification.NotificationRecord old3 = com.android.server.notification.NotificationManagerService.this.mNotificationList.get(index);
                                    com.android.server.notification.NotificationManagerService.this.mNotificationList.set(index, r);
                                    com.android.server.notification.NotificationManagerService.this.mUsageStats.registerUpdatedByApp(r, old3);
                                    try {
                                        com.android.server.notification.NotificationManagerService.this.mUsageStatsManagerInternal.reportNotificationUpdated(r.getSbn().getOpPkg(), r.getSbn().getUser(), this.mTracker.getStartTime());
                                        notification.flags |= old3.getNotification().flags & 64;
                                        r.isUpdate = true;
                                        boolean isInterruptive2 = com.android.server.notification.NotificationManagerService.this.isVisuallyInterruptive(old3, r);
                                        r.setTextChanged(isInterruptive2);
                                        if (android.app.Flags.sortSectionByTime() && isInterruptive2) {
                                            r.resetRankingTime();
                                        }
                                        old = old3;
                                    } catch (java.lang.Throwable th) {
                                        th = th;
                                    }
                                }
                                com.android.server.notification.NotificationManagerService.this.mNotificationsByKey.put(n.getKey(), r);
                                if ((notification.flags & 64) != 0) {
                                    notification.flags |= 32;
                                }
                                if ((notification.flags & 2) != 0 && com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                                    com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().updateNotification(r.getUid(), r.getSbn().getPackageName(), r.getSbn().getKey(), true);
                                }
                                if (android.app.Flags.checkAutogroupBeforePost() && notification.getSmallIcon() != null && !com.android.server.notification.NotificationManagerService.this.isCritical(r)) {
                                    android.service.notification.StatusBarNotification oldSbn = old != null ? old.getSbn() : null;
                                    if (oldSbn == null || !java.util.Objects.equals(oldSbn.getGroup(), n.getGroup()) || oldSbn.getNotification().flags != n.getNotification().flags) {
                                        synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                                            boolean willBeAutogrouped = com.android.server.notification.NotificationManagerService.this.mGroupHelper.onNotificationPosted(n, com.android.server.notification.NotificationManagerService.this.hasAutoGroupSummaryLocked(n));
                                            if (willBeAutogrouped) {
                                                com.android.server.notification.NotificationManagerService.this.addAutogroupKeyLocked(this.key, false);
                                            }
                                        }
                                    }
                                }
                                com.android.server.notification.NotificationManagerService.this.mRankingHelper.extractSignals(r);
                                com.android.server.notification.NotificationManagerService.this.mRankingHelper.sort(com.android.server.notification.NotificationManagerService.this.mNotificationList);
                                int position = com.android.server.notification.NotificationManagerService.this.mRankingHelper.indexOf(com.android.server.notification.NotificationManagerService.this.mNotificationList, r);
                                int buzzBeepBlinkLoggingCode = r.isHidden() ? 0 : com.android.server.notification.NotificationManagerService.this.mAttentionHelper.buzzBeepBlinkLocked(r, new com.android.server.notification.NotificationAttentionHelper.Signals(com.android.server.notification.NotificationManagerService.this.mUserProfiles.isCurrentProfile(r.getUserId()), com.android.server.notification.NotificationManagerService.this.mListenerHints));
                                if (notification.getSmallIcon() != null) {
                                    com.android.server.notification.NotificationRecordLogger.NotificationReported maybeReport = com.android.server.notification.NotificationManagerService.this.mNotificationRecordLogger.prepareToLogNotificationPosted(r, old, position, buzzBeepBlinkLoggingCode, com.android.server.notification.NotificationManagerService.this.getGroupInstanceId(r.getSbn().getGroupKey()));
                                    com.android.server.notification.NotificationManagerService.this.notifyListenersPostedAndLogLocked(r, old, this.mTracker, maybeReport);
                                    posted = true;
                                    if (!android.app.Flags.checkAutogroupBeforePost()) {
                                        android.service.notification.StatusBarNotification oldSbn2 = old != null ? old.getSbn() : null;
                                        if ((oldSbn2 == null || !java.util.Objects.equals(oldSbn2.getGroup(), n.getGroup()) || oldSbn2.getNotification().flags != n.getNotification().flags) && !com.android.server.notification.NotificationManagerService.this.isCritical(r)) {
                                            com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$PostNotificationRunnable$$ExternalSyntheticLambda0
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    this.f$0.lambda$postNotification$0(n);
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "Not posting notification without small icon: " + notification);
                                    if (old != null && !old.isCanceled) {
                                        com.android.server.notification.NotificationManagerService.this.mListeners.notifyRemovedLocked(r, 4, r.getStats());
                                        com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService.PostNotificationRunnable.1
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                com.android.server.notification.NotificationManagerService.this.mGroupHelper.onNotificationRemoved(n);
                                            }
                                        });
                                    }
                                    if (com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.callstyleCallbackApi()) {
                                        com.android.server.notification.NotificationManagerService.this.notifyCallNotificationEventListenerOnRemoved(r);
                                    }
                                    android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "WARNING: In a future release this will crash the app: " + n.getPackageName());
                                }
                                if (com.android.server.notification.NotificationManagerService.this.mShortcutHelper != null) {
                                    com.android.server.notification.NotificationManagerService.this.mShortcutHelper.maybeListenForShortcutChangesForBubbles(r, false, com.android.server.notification.NotificationManagerService.this.mHandler);
                                }
                                com.android.server.notification.NotificationManagerService.this.maybeRecordInterruptionLocked(r);
                                com.android.server.notification.NotificationManagerService.this.maybeRegisterMessageSent(r);
                                com.android.server.notification.NotificationManagerService.this.maybeReportForegroundServiceUpdate(r, true);
                                int N4 = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.size();
                                int i4 = 0;
                                while (true) {
                                    if (i4 >= N4) {
                                        break;
                                    }
                                    com.android.server.notification.NotificationRecord enqueued4 = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.get(i4);
                                    if (java.util.Objects.equals(this.key, enqueued4.getKey())) {
                                        com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.remove(i4);
                                        break;
                                    }
                                    i4++;
                                }
                                return posted;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                throw th;
                            }
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                    }
                    int N5 = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.size();
                    int i5 = 0;
                    while (true) {
                        if (i5 >= N5) {
                            break;
                        }
                        com.android.server.notification.NotificationRecord enqueued5 = com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.get(i5);
                        if (java.util.Objects.equals(this.key, enqueued5.getKey())) {
                            com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications.remove(i5);
                            break;
                        }
                        i5++;
                    }
                    throw th;
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$postNotification$0(android.service.notification.StatusBarNotification n) {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                com.android.server.notification.NotificationManagerService.this.mGroupHelper.onNotificationPosted(n, com.android.server.notification.NotificationManagerService.this.hasAutoGroupSummaryLocked(n));
            }
        }
    }

    com.android.internal.logging.InstanceId getGroupInstanceId(java.lang.String groupKey) {
        com.android.server.notification.NotificationRecord group;
        if (groupKey == null || (group = this.mSummaryByGroupKey.get(groupKey)) == null) {
            return null;
        }
        return group.getSbn().getInstanceId();
    }

    protected boolean isVisuallyInterruptive(com.android.server.notification.NotificationRecord old, com.android.server.notification.NotificationRecord r) {
        android.app.Notification.Builder oldB;
        android.app.Notification.Builder newB;
        if (r.getSbn().isGroup() && r.getSbn().getNotification().isGroupSummary()) {
            if (DEBUG_INTERRUPTIVENESS) {
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is not interruptive: summary");
            }
            return false;
        }
        if (old == null) {
            if (DEBUG_INTERRUPTIVENESS) {
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is interruptive: new notification");
            }
            return true;
        }
        android.app.Notification oldN = old.getSbn().getNotification();
        android.app.Notification newN = r.getSbn().getNotification();
        if (oldN.extras == null || newN.extras == null) {
            if (DEBUG_INTERRUPTIVENESS) {
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is not interruptive: no extras");
            }
            return false;
        }
        if (android.app.Flags.sortSectionByTime()) {
            if (r.getSbn().getNotification().isFgsOrUij()) {
                if (DEBUG_INTERRUPTIVENESS) {
                    android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is not interruptive: FGS/UIJ");
                }
                return false;
            }
        } else if ((r.getSbn().getNotification().flags & 64) != 0) {
            if (DEBUG_INTERRUPTIVENESS) {
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is not interruptive: foreground service");
            }
            return false;
        }
        java.lang.String oldTitle = java.lang.String.valueOf(oldN.extras.get("android.title"));
        java.lang.String newTitle = java.lang.String.valueOf(newN.extras.get("android.title"));
        if (!java.util.Objects.equals(oldTitle, newTitle)) {
            if (DEBUG_INTERRUPTIVENESS) {
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is interruptive: changed title");
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + java.lang.String.format("   old title: %s (%s@0x%08x)", oldTitle, oldTitle.getClass(), java.lang.Integer.valueOf(oldTitle.hashCode())));
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + java.lang.String.format("   new title: %s (%s@0x%08x)", newTitle, newTitle.getClass(), java.lang.Integer.valueOf(newTitle.hashCode())));
            }
            return true;
        }
        java.lang.String oldText = java.lang.String.valueOf(oldN.extras.get("android.text"));
        java.lang.String newText = java.lang.String.valueOf(newN.extras.get("android.text"));
        if (!java.util.Objects.equals(oldText, newText)) {
            if (DEBUG_INTERRUPTIVENESS) {
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is interruptive: changed text");
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + java.lang.String.format("   old text: %s (%s@0x%08x)", oldText, oldText.getClass(), java.lang.Integer.valueOf(oldText.hashCode())));
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + java.lang.String.format("   new text: %s (%s@0x%08x)", newText, newText.getClass(), java.lang.Integer.valueOf(newText.hashCode())));
            }
            return true;
        }
        if (oldN.hasCompletedProgress() != newN.hasCompletedProgress()) {
            if (DEBUG_INTERRUPTIVENESS) {
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is interruptive: completed progress");
            }
            return true;
        }
        if (android.app.Notification.areIconsDifferent(oldN, newN)) {
            if (DEBUG_INTERRUPTIVENESS) {
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is interruptive: icons differ");
            }
            return true;
        }
        if (r.canBubble()) {
            if (DEBUG_INTERRUPTIVENESS) {
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is not interruptive: bubble");
            }
            return false;
        }
        if (android.app.Notification.areActionsVisiblyDifferent(oldN, newN)) {
            if (DEBUG_INTERRUPTIVENESS) {
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is interruptive: changed actions");
            }
            return true;
        }
        try {
            oldB = android.app.Notification.Builder.recoverBuilder(getContext(), oldN);
            newB = android.app.Notification.Builder.recoverBuilder(getContext(), newN);
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "error recovering builder", e);
        }
        if (android.app.Notification.areStyledNotificationsVisiblyDifferent(oldB, newB)) {
            if (DEBUG_INTERRUPTIVENESS) {
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is interruptive: styles differ");
            }
            return true;
        }
        if (android.app.Notification.areRemoteViewsChanged(oldB, newB)) {
            if (DEBUG_INTERRUPTIVENESS) {
                android.util.Slog.v(TAG, "INTERRUPTIVENESS: " + r.getKey() + " is interruptive: remoteviews differ");
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCritical(com.android.server.notification.NotificationRecord record) {
        return record.getCriticality() < 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGroupedNotificationLocked(com.android.server.notification.NotificationRecord r, com.android.server.notification.NotificationRecord old, int callingUid, int callingPid) {
        com.android.server.notification.NotificationRecord removedSummary;
        android.service.notification.StatusBarNotification sbn = r.getSbn();
        android.app.Notification n = sbn.getNotification();
        if (n.isGroupSummary() && !sbn.isAppGroup()) {
            n.flags &= -513;
        }
        java.lang.String group = sbn.getGroupKey();
        boolean isSummary = n.isGroupSummary();
        android.app.Notification oldN = old != null ? old.getSbn().getNotification() : null;
        java.lang.String oldGroup = old != null ? old.getSbn().getGroupKey() : null;
        boolean oldIsSummary = old != null && oldN.isGroupSummary();
        if (oldIsSummary && (removedSummary = this.mSummaryByGroupKey.remove(oldGroup)) != old) {
            java.lang.String removedKey = removedSummary != null ? removedSummary.getKey() : "<null>";
            android.util.Slog.w(TAG, "Removed summary didn't match old notification: old=" + old.getKey() + ", removed=" + removedKey);
        }
        if (isSummary) {
            this.mSummaryByGroupKey.put(group, r);
        }
        com.android.server.notification.NotificationManagerService.FlagChecker childrenFlagChecker = new com.android.server.notification.NotificationManagerService.FlagChecker() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda5
            @Override // com.android.server.notification.NotificationManagerService.FlagChecker
            public final boolean apply(int i) {
                return com.android.server.notification.NotificationManagerService.lambda$handleGroupedNotificationLocked$8(i);
            }
        };
        if (oldIsSummary) {
            if (!isSummary || !oldGroup.equals(group)) {
                cancelGroupChildrenLocked(old, callingUid, callingPid, null, false, childrenFlagChecker, 8, android.os.SystemClock.elapsedRealtime());
            }
        }
    }

    static /* synthetic */ boolean lambda$handleGroupedNotificationLocked$8(int flags) {
        if ((flags & 64) != 0 || (32768 & flags) != 0) {
            return false;
        }
        return true;
    }

    private android.app.PendingIntent getNotificationTimeoutPendingIntent(com.android.server.notification.NotificationRecord record, int flags) {
        return android.app.PendingIntent.getBroadcast(getContext(), 1, new android.content.Intent(ACTION_NOTIFICATION_TIMEOUT).setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME).setData(new android.net.Uri.Builder().scheme(SCHEME_TIMEOUT).appendPath(record.getKey()).build()).addFlags(268435456).putExtra(EXTRA_KEY, record.getKey()), flags | 67108864);
    }

    void scheduleTimeoutLocked(com.android.server.notification.NotificationRecord record) {
        if (record.getNotification().getTimeoutAfter() > 0) {
            android.app.PendingIntent pi = getNotificationTimeoutPendingIntent(record, 134217728);
            this.mAlarmManager.setExactAndAllowWhileIdle(2, android.os.SystemClock.elapsedRealtime() + record.getNotification().getTimeoutAfter(), pi);
        }
    }

    void cancelScheduledTimeoutLocked(com.android.server.notification.NotificationRecord record) {
        android.app.PendingIntent pi = getNotificationTimeoutPendingIntent(record, 268435456);
        if (pi != null) {
            this.mAlarmManager.cancel(pi);
        }
    }

    private int[] calculateAmplitude(android.os.VibrationEffect effect, int[] amp) {
        if (amp == null || this.mNotificationVibrationIntensity < 800 || this.mNotificationVibrationIntensity > 2400) {
            return null;
        }
        int tmpAmp = (this.mNotificationVibrationIntensity - 800) * 255;
        int newAmp = tmpAmp / 1600;
        if (newAmp < 1) {
            newAmp = 1;
        }
        for (int i = 0; i < amp.length; i++) {
            if (amp[i] == -1) {
                amp[i] = newAmp;
            }
        }
        android.util.Slog.d(TAG, "new amplitude=" + java.util.Arrays.toString(amp));
        return amp;
    }

    void showNextToastLocked(boolean lastToastWasTextRecord) {
        if (this.mIsCurrentToastShown) {
            return;
        }
        com.android.server.notification.toast.ToastRecord record = this.mToastQueue.get(0);
        while (record != null) {
            int userId = android.os.UserHandle.getUserId(record.uid);
            boolean rateLimitingEnabled = !this.mToastRateLimitingDisabledUids.contains(java.lang.Integer.valueOf(record.uid));
            boolean isWithinQuota = this.mToastRateLimiter.isWithinQuota(userId, record.pkg, TOAST_QUOTA_TAG) || isExemptFromRateLimiting(record.pkg, userId);
            boolean isPackageInForeground = isPackageInForegroundForToast(record.uid);
            if (tryShowToast(record, rateLimitingEnabled, isWithinQuota, isPackageInForeground)) {
                scheduleDurationReachedLocked(record, lastToastWasTextRecord);
                this.mIsCurrentToastShown = true;
                if (this.mNMSWrapper.getNMSExt() != null) {
                    this.mNMSWrapper.getNMSExt().setCurrentShowTime(java.lang.System.currentTimeMillis());
                }
                if (rateLimitingEnabled && !isPackageInForeground) {
                    this.mToastRateLimiter.noteEvent(userId, record.pkg, TOAST_QUOTA_TAG);
                    return;
                }
                return;
            }
            int index = this.mToastQueue.indexOf(record);
            if (index >= 0) {
                com.android.server.notification.toast.ToastRecord toast = this.mToastQueue.remove(index);
                this.mWindowManagerInternal.removeWindowToken(toast.windowToken, true, toast.displayId);
            }
            record = this.mToastQueue.size() > 0 ? this.mToastQueue.get(0) : null;
        }
    }

    private boolean tryShowToast(com.android.server.notification.toast.ToastRecord record, boolean rateLimitingEnabled, boolean isWithinQuota, boolean isPackageInForeground) {
        if (rateLimitingEnabled && !isWithinQuota && !isPackageInForeground) {
            reportCompatRateLimitingToastsChange(record.uid);
            android.util.Slog.w(TAG, "Package " + record.pkg + " is above allowed toast quota, the following toast was blocked and discarded: " + record);
            return false;
        }
        if (blockToast(record.uid, record.isSystemToast, record.isAppRendered(), isPackageInForeground)) {
            android.util.Slog.w(TAG, "Blocking custom toast from package " + record.pkg + " due to package not in the foreground at the time of showing the toast");
            return false;
        }
        return record.show();
    }

    private boolean isExemptFromRateLimiting(java.lang.String pkg, int userId) {
        try {
            boolean isExemptFromRateLimiting = this.mPackageManager.checkPermission("android.permission.UNLIMITED_TOASTS", pkg, userId) == 0;
            return isExemptFromRateLimiting;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to connect with package manager");
            return false;
        }
    }

    private void reportCompatRateLimitingToastsChange(int uid) {
        long id = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mPlatformCompat.reportChangeByUid(RATE_LIMIT_TOASTS, uid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Unexpected exception while reporting toast was blocked due to rate limiting", e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(id);
        }
    }

    void cancelToastLocked(int index) {
        com.android.server.notification.toast.ToastRecord record = this.mToastQueue.get(index);
        record.hide();
        if (index == 0) {
            this.mIsCurrentToastShown = false;
        }
        com.android.server.notification.toast.ToastRecord lastToast = this.mToastQueue.remove(index);
        scheduleKillTokenTimeout(lastToast);
        keepProcessAliveForToastIfNeededLocked(record.pid);
        if (this.mToastQueue.size() > 0) {
            showNextToastLocked(lastToast instanceof com.android.server.notification.toast.TextToastRecord);
        }
    }

    void finishWindowTokenLocked(android.os.IBinder t, int displayId) {
        this.mHandler.removeCallbacksAndMessages(t);
        this.mWindowManagerInternal.removeWindowToken(t, true, displayId);
    }

    private void scheduleDurationReachedLocked(com.android.server.notification.toast.ToastRecord r, boolean lastToastWasTextRecord) {
        this.mHandler.removeCallbacksAndMessages(r);
        android.os.Message m = android.os.Message.obtain(this.mHandler, 2, r);
        int delay = this.mAccessibilityManager.getRecommendedTimeoutMillis(r.getDuration() == 1 ? LONG_DELAY : 2000, 2);
        if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().shouldContinuousShowToast(this.mToastQueue)) {
            delay = 100;
        }
        if (lastToastWasTextRecord) {
            delay += 250;
        }
        if (r instanceof com.android.server.notification.toast.TextToastRecord) {
            delay += com.android.internal.util.FrameworkStatsLog.DEVICE_ROTATED;
        }
        this.mHandler.sendMessageDelayed(m, delay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDurationReached(com.android.server.notification.toast.ToastRecord record) {
        if (DBG) {
            android.util.Slog.d(TAG, "Timeout pkg=" + record.pkg + " token=" + record.token);
        }
        synchronized (this.mToastQueue) {
            int index = indexOfToastLocked(record.pkg, record.token);
            if (index >= 0) {
                cancelToastLocked(index);
            }
        }
    }

    private void scheduleKillTokenTimeout(com.android.server.notification.toast.ToastRecord r) {
        this.mHandler.removeCallbacksAndMessages(r);
        android.os.Message m = android.os.Message.obtain(this.mHandler, 7, r);
        this.mHandler.sendMessageDelayed(m, 11000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKillTokenTimeout(com.android.server.notification.toast.ToastRecord record) {
        if (DBG) {
            android.util.Slog.d(TAG, "Kill Token Timeout token=" + record.windowToken);
        }
        synchronized (this.mToastQueue) {
            finishWindowTokenLocked(record.windowToken, record.displayId);
        }
    }

    int indexOfToastLocked(java.lang.String pkg, android.os.IBinder token) {
        java.util.ArrayList<com.android.server.notification.toast.ToastRecord> list = this.mToastQueue;
        int len = list.size();
        for (int i = 0; i < len; i++) {
            com.android.server.notification.toast.ToastRecord r = list.get(i);
            if (r.pkg.equals(pkg) && r.token == token) {
                return i;
            }
        }
        return -1;
    }

    public void keepProcessAliveForToastIfNeeded(int pid) {
        synchronized (this.mToastQueue) {
            keepProcessAliveForToastIfNeededLocked(pid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void keepProcessAliveForToastIfNeededLocked(int pid) {
        int toastCount = 0;
        java.util.ArrayList<com.android.server.notification.toast.ToastRecord> list = this.mToastQueue;
        int n = list.size();
        for (int i = 0; i < n; i++) {
            com.android.server.notification.toast.ToastRecord r = list.get(i);
            if (r.pid == pid && r.keepProcessAlive()) {
                toastCount++;
            }
        }
        try {
            this.mAm.setProcessImportant(this.mForegroundToken, pid, toastCount > 0, "toast");
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPackageInForegroundForToast(int callingUid) {
        return this.mAtm.hasResumedActivity(callingUid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean blockToast(int uid, boolean isSystemToast, boolean isAppRenderedToast, boolean isPackageInForeground) {
        return isAppRenderedToast && !isSystemToast && !isPackageInForeground && android.app.compat.CompatChanges.isChangeEnabled(CHANGE_BACKGROUND_CUSTOM_TOAST_BLOCK, uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRankingReconsideration(android.os.Message message) throws java.lang.Throwable {
        if (message.obj instanceof com.android.server.notification.RankingReconsideration) {
            com.android.server.notification.RankingReconsideration recon = (com.android.server.notification.RankingReconsideration) message.obj;
            recon.run();
            synchronized (this.mNotificationLock) {
                try {
                    try {
                        com.android.server.notification.NotificationRecord record = this.mNotificationsByKey.get(recon.getKey());
                        try {
                            if (record == null) {
                                return;
                            }
                            int indexBefore = findNotificationRecordIndexLocked(record);
                            boolean interceptBefore = record.isIntercepted();
                            int visibilityBefore = record.getPackageVisibilityOverride();
                            boolean interruptiveBefore = record.isInterruptive();
                            recon.applyChangesLocked(record);
                            applyZenModeLocked(record);
                            this.mRankingHelper.sort(this.mNotificationList);
                            boolean changed = true;
                            boolean indexChanged = indexBefore != findNotificationRecordIndexLocked(record);
                            boolean interceptChanged = interceptBefore != record.isIntercepted();
                            boolean visibilityChanged = visibilityBefore != record.getPackageVisibilityOverride();
                            boolean interruptiveChanged = record.canBubble() && interruptiveBefore != record.isInterruptive();
                            if (!indexChanged && !interceptChanged && !visibilityChanged && !interruptiveChanged) {
                                changed = false;
                            }
                            if (interceptBefore) {
                                if (!record.isIntercepted() && record.isNewEnoughForAlerting(java.lang.System.currentTimeMillis())) {
                                    com.android.server.notification.NotificationAttentionHelper notificationAttentionHelper = this.mAttentionHelper;
                                    com.android.server.notification.ManagedServices.UserProfiles userProfiles = this.mUserProfiles;
                                    int indexBefore2 = record.getUserId();
                                    notificationAttentionHelper.buzzBeepBlinkLocked(record, new com.android.server.notification.NotificationAttentionHelper.Signals(userProfiles.isCurrentProfile(indexBefore2), this.mListenerHints));
                                    com.android.server.notification.ZenLog.traceAlertOnUpdatedIntercept(record);
                                }
                            }
                            if (changed) {
                                this.mHandler.scheduleSendRankingUpdate();
                                return;
                            }
                            return;
                        } catch (java.lang.Throwable th) {
                            th = th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
                throw th;
            }
        }
    }

    void handleRankingSort() {
        if (this.mRankingHelper == null) {
            return;
        }
        synchronized (this.mNotificationLock) {
            int N = this.mNotificationList.size();
            android.util.ArrayMap<java.lang.String, com.android.server.notification.NotificationRecordExtractorData> extractorDataBefore = new android.util.ArrayMap<>(N);
            for (int i = 0; i < N; i++) {
                com.android.server.notification.NotificationRecord r = this.mNotificationList.get(i);
                com.android.server.notification.NotificationRecordExtractorData extractorData = new com.android.server.notification.NotificationRecordExtractorData(i, r.getPackageVisibilityOverride(), r.canShowBadge(), r.canBubble(), r.getNotification().isBubbleNotification(), r.getChannel(), r.getGroupKey(), r.getPeopleOverride(), r.getSnoozeCriteria(), java.lang.Integer.valueOf(r.getUserSentiment()), java.lang.Integer.valueOf(r.getSuppressedVisualEffects()), r.getSystemGeneratedSmartActions(), r.getSmartReplies(), r.getImportance(), r.getRankingScore(), r.isConversation(), r.getProposedImportance(), r.hasSensitiveContent());
                extractorDataBefore.put(r.getKey(), extractorData);
                this.mRankingHelper.extractSignals(r);
            }
            this.mRankingHelper.sort(this.mNotificationList);
            for (int i2 = 0; i2 < N; i2++) {
                com.android.server.notification.NotificationRecord r2 = this.mNotificationList.get(i2);
                if (extractorDataBefore.containsKey(r2.getKey())) {
                    if (extractorDataBefore.get(r2.getKey()).hasDiffForRankingLocked(r2, i2)) {
                        this.mHandler.scheduleSendRankingUpdate();
                    }
                    if (r2.hasPendingLogUpdate()) {
                        com.android.server.notification.NotificationRecordExtractorData prevData = extractorDataBefore.get(r2.getKey());
                        if (prevData.hasDiffForLoggingLocked(r2, i2)) {
                            this.mNotificationRecordLogger.logNotificationAdjusted(r2, i2, 0, getGroupInstanceId(r2.getSbn().getGroupKey()));
                        }
                        r2.setPendingLogUpdate(false);
                    }
                }
            }
        }
    }

    private void recordCallerLocked(com.android.server.notification.NotificationRecord record) {
        if (this.mZenModeHelper.isCall(record)) {
            this.mZenModeHelper.recordCaller(record);
        }
    }

    private void applyZenModeLocked(com.android.server.notification.NotificationRecord record) {
        record.setIntercepted(this.mZenModeHelper.shouldIntercept(record));
        if (record.isIntercepted()) {
            record.setSuppressedVisualEffects(this.mZenModeHelper.getConsolidatedNotificationPolicy().suppressedVisualEffects);
        } else {
            record.setSuppressedVisualEffects(0);
        }
    }

    private int findNotificationRecordIndexLocked(com.android.server.notification.NotificationRecord target) {
        return this.mRankingHelper.indexOf(this.mNotificationList, target);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSendRankingUpdate() {
        synchronized (this.mNotificationLock) {
            this.mListeners.notifyRankingUpdateLocked(null);
        }
    }

    private void scheduleListenerHintsChanged(int state) {
        if (!com.android.server.notification.Flags.notificationReduceMessagequeueUsage()) {
            this.mHandler.removeMessages(5);
        }
        this.mHandler.obtainMessage(5, state, 0).sendToTarget();
    }

    private void scheduleInterruptionFilterChanged(int listenerInterruptionFilter) {
        if (!com.android.server.notification.Flags.notificationReduceMessagequeueUsage()) {
            this.mHandler.removeMessages(6);
        }
        this.mHandler.obtainMessage(6, listenerInterruptionFilter, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleListenerHintsChanged(int hints) {
        synchronized (this.mNotificationLock) {
            this.mListeners.notifyListenerHintsChangedLocked(hints);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleListenerInterruptionFilterChanged(int interruptionFilter) {
        synchronized (this.mNotificationLock) {
            this.mListeners.notifyInterruptionFilterChanged(interruptionFilter);
        }
    }

    void handleOnPackageChanged(boolean removingPackage, int changeUserId, java.lang.String[] pkgList, int[] uidList) {
        this.mListeners.onPackagesChanged(removingPackage, pkgList, uidList);
        this.mAssistants.onPackagesChanged(removingPackage, pkgList, uidList);
        this.mConditionProviders.onPackagesChanged(removingPackage, pkgList, uidList);
        boolean preferencesChanged = removingPackage | this.mPreferencesHelper.onPackagesChanged(removingPackage, changeUserId, pkgList, uidList);
        if (removingPackage) {
            int size = java.lang.Math.min(pkgList.length, uidList.length);
            for (int i = 0; i < size; i++) {
                java.lang.String pkg = pkgList[i];
                int uid = uidList[i];
                int userHandle = android.os.UserHandle.getUserId(uid);
                this.mArchive.removePackageNotifications(pkg, userHandle);
                this.mHistoryManager.onPackageRemoved(userHandle, pkg);
            }
        }
        if (preferencesChanged) {
            handleSavePolicyFile();
        }
    }

    protected class WorkerHandler extends android.os.Handler {
        public WorkerHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 2:
                    com.android.server.notification.NotificationManagerService.this.handleDurationReached((com.android.server.notification.toast.ToastRecord) msg.obj);
                    break;
                case 4:
                    com.android.server.notification.NotificationManagerService.this.handleSendRankingUpdate();
                    break;
                case 5:
                    com.android.server.notification.NotificationManagerService.this.handleListenerHintsChanged(msg.arg1);
                    break;
                case 6:
                    com.android.server.notification.NotificationManagerService.this.handleListenerInterruptionFilterChanged(msg.arg1);
                    break;
                case 7:
                    com.android.server.notification.NotificationManagerService.this.handleKillTokenTimeout((com.android.server.notification.toast.ToastRecord) msg.obj);
                    break;
                case 8:
                    com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                    com.android.server.notification.NotificationManagerService.this.handleOnPackageChanged(((java.lang.Boolean) args.arg1).booleanValue(), args.argi1, (java.lang.String[]) args.arg2, (int[]) args.arg3);
                    args.recycle();
                    break;
            }
        }

        protected void scheduleSendRankingUpdate() {
            if (com.android.server.notification.Flags.notificationReduceMessagequeueUsage()) {
                android.os.Message m = android.os.Message.obtain(this, 4);
                sendMessage(m);
            } else if (!hasMessages(4)) {
                android.os.Message m2 = android.os.Message.obtain(this, 4);
                sendMessage(m2);
            }
        }

        protected void scheduleCancelNotification(com.android.server.notification.NotificationManagerService.CancelNotificationRunnable cancelRunnable, int delay) {
            if (android.app.Flags.lifetimeExtensionRefactor()) {
                sendMessageDelayed(android.os.Message.obtain(this, cancelRunnable), delay);
            } else if (com.android.server.notification.Flags.notificationReduceMessagequeueUsage()) {
                sendMessage(android.os.Message.obtain(this, cancelRunnable));
            } else if (!hasCallbacks(cancelRunnable)) {
                sendMessage(android.os.Message.obtain(this, cancelRunnable));
            }
        }

        protected void scheduleOnPackageChanged(boolean removingPackage, int changeUserId, java.lang.String[] pkgList, int[] uidList) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = java.lang.Boolean.valueOf(removingPackage);
            args.argi1 = changeUserId;
            args.arg2 = pkgList;
            args.arg3 = uidList;
            sendMessage(android.os.Message.obtain(this, 8, args));
        }
    }

    private final class RankingHandlerWorker extends android.os.Handler implements com.android.server.notification.RankingHandler {
        public RankingHandlerWorker(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            switch (msg.what) {
                case 1000:
                    com.android.server.notification.NotificationManagerService.this.handleRankingReconsideration(msg);
                    break;
                case 1001:
                    com.android.server.notification.NotificationManagerService.this.handleRankingSort();
                    break;
            }
        }

        @Override // com.android.server.notification.RankingHandler
        public void requestSort() {
            if (!com.android.server.notification.Flags.notificationReduceMessagequeueUsage()) {
                removeMessages(1001);
            }
            android.os.Message msg = android.os.Message.obtain();
            msg.what = 1001;
            sendMessage(msg);
        }

        @Override // com.android.server.notification.RankingHandler
        public void requestReconsideration(com.android.server.notification.RankingReconsideration recon) {
            android.os.Message m = android.os.Message.obtain(this, 1000, recon);
            long delay = recon.getDelay(java.util.concurrent.TimeUnit.MILLISECONDS);
            sendMessageDelayed(m, delay);
        }
    }

    static int clamp(int x, int low, int high) {
        return x < low ? low : x > high ? high : x;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeFromNotificationListsLocked(com.android.server.notification.NotificationRecord r) {
        boolean wasPosted = false;
        com.android.server.notification.NotificationRecord recordInList = findNotificationByListLocked(this.mNotificationList, r.getKey());
        if (recordInList != null) {
            this.mNotificationList.remove(recordInList);
            this.mNotificationsByKey.remove(recordInList.getSbn().getKey());
            wasPosted = true;
        }
        while (true) {
            com.android.server.notification.NotificationRecord recordInList2 = findNotificationByListLocked(this.mEnqueuedNotifications, r.getKey());
            if (recordInList2 != null) {
                this.mEnqueuedNotifications.remove(recordInList2);
            } else {
                return wasPosted;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelNotificationLocked(com.android.server.notification.NotificationRecord r, boolean sendDelete, int reason, boolean wasPosted, java.lang.String listenerName, long cancellationElapsedTimeMs) {
        cancelNotificationLocked(r, sendDelete, reason, -1, -1, wasPosted, listenerName, cancellationElapsedTimeMs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelNotificationLocked(final com.android.server.notification.NotificationRecord r, boolean sendDelete, int reason, int rank, int count, boolean wasPosted, java.lang.String listenerName, long cancellationElapsedTimeMs) {
        android.app.PendingIntent deleteIntent;
        java.lang.String canceledKey = r.getKey();
        if (com.android.server.notification.Flags.allNotifsNeedTtl()) {
            this.mTtlHelper.cancelScheduledTimeoutLocked(r);
        } else {
            cancelScheduledTimeoutLocked(r);
        }
        recordCallerLocked(r);
        if (r.getStats().getDismissalSurface() == -1) {
            r.recordDismissalSurface(0);
        }
        if (sendDelete && (deleteIntent = r.getNotification().deleteIntent) != null) {
            try {
                ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).clearPendingIntentAllowBgActivityStarts(deleteIntent.getTarget(), ALLOWLIST_TOKEN);
                deleteIntent.send();
            } catch (android.app.PendingIntent.CanceledException ex) {
                android.util.Slog.w(TAG, "canceled PendingIntent for " + r.getSbn().getPackageName(), ex);
            }
        }
        if (wasPosted) {
            if (r.getNotification().getSmallIcon() != null) {
                if (reason != 18) {
                    r.isCanceled = true;
                }
                this.mListeners.notifyRemovedLocked(r, reason, r.getStats());
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService.15
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.notification.NotificationManagerService.this.mGroupHelper.onNotificationRemoved(r.getSbn());
                    }
                });
                if (com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.callstyleCallbackApi()) {
                    notifyCallNotificationEventListenerOnRemoved(r);
                }
            }
            this.mAttentionHelper.clearEffectsLocked(canceledKey, mNMSExt.shouldForcePlayRedPackageRington(r));
        }
        switch (reason) {
            case 2:
            case 3:
            case 10:
            case 11:
                this.mUsageStats.registerDismissedByUser(r);
                break;
            case 8:
            case 9:
                this.mUsageStats.registerRemovedByApp(r);
                this.mUsageStatsManagerInternal.reportNotificationRemoved(r.getSbn().getOpPkg(), r.getUser(), cancellationElapsedTimeMs);
                break;
        }
        java.lang.String groupKey = r.getGroupKey();
        com.android.server.notification.NotificationRecord groupSummary = this.mSummaryByGroupKey.get(groupKey);
        if (groupSummary != null && groupSummary.getKey().equals(canceledKey)) {
            this.mSummaryByGroupKey.remove(groupKey);
        }
        android.util.ArrayMap<java.lang.String, java.lang.String> summaries = this.mAutobundledSummaries.get(java.lang.Integer.valueOf(r.getSbn().getUserId()));
        if (summaries != null && r.getSbn().getKey().equals(summaries.get(r.getSbn().getPackageName()))) {
            summaries.remove(r.getSbn().getPackageName());
        }
        if (reason != 20) {
            this.mArchive.record(r.getSbn(), reason);
        }
        long now = java.lang.System.currentTimeMillis();
        android.metrics.LogMaker logMaker = r.getItemLogMaker().setType(5).setSubtype(reason);
        if (rank != -1 && count != -1) {
            logMaker.addTaggedData(798, java.lang.Integer.valueOf(rank)).addTaggedData(1395, java.lang.Integer.valueOf(count));
        }
        com.android.internal.logging.MetricsLogger.action(logMaker);
        com.android.server.EventLogTags.writeNotificationCanceled(canceledKey, reason, r.getLifespanMs(now), r.getFreshnessMs(now), r.getExposureMs(now), rank, count, listenerName);
        if ((r.getSbn().getNotification().flags & 2) != 0 && this.mNMSWrapper.getNMSExt() != null) {
            this.mNMSWrapper.getNMSExt().updateNotification(r.getUid(), r.getSbn().getPackageName(), r.getSbn().getKey(), false);
        }
        if (wasPosted) {
            this.mNotificationRecordLogger.logNotificationCancelled(r, reason, r.getStats().getDismissalSurface());
        }
    }

    void updateUriPermissions(com.android.server.notification.NotificationRecord newRecord, com.android.server.notification.NotificationRecord oldRecord, java.lang.String targetPkg, int targetUserId) {
        updateUriPermissions(newRecord, oldRecord, targetPkg, targetUserId, false);
    }

    void updateUriPermissions(com.android.server.notification.NotificationRecord newRecord, com.android.server.notification.NotificationRecord oldRecord, java.lang.String targetPkg, int targetUserId, boolean onlyRevokeCurrentTarget) {
        android.os.IBinder permissionOwner;
        java.lang.String key = newRecord != null ? newRecord.getKey() : oldRecord.getKey();
        if (DBG) {
            android.util.Slog.d(TAG, key + ": updating permissions");
        }
        android.util.ArraySet<android.net.Uri> newUris = newRecord != null ? newRecord.getGrantableUris() : null;
        android.util.ArraySet<android.net.Uri> oldUris = oldRecord != null ? oldRecord.getGrantableUris() : null;
        if (newUris == null && oldUris == null) {
            return;
        }
        android.os.IBinder permissionOwner2 = null;
        if (newRecord != null && 0 == 0) {
            permissionOwner2 = newRecord.permissionOwner;
        }
        if (oldRecord != null && permissionOwner2 == null) {
            permissionOwner2 = oldRecord.permissionOwner;
        }
        if (newUris != null && permissionOwner2 == null) {
            if (DBG) {
                android.util.Slog.d(TAG, key + ": creating owner");
            }
            permissionOwner2 = this.mUgmInternal.newUriPermissionOwner("NOTIF:" + key);
        }
        if (newUris == null && permissionOwner2 != null && !onlyRevokeCurrentTarget) {
            destroyPermissionOwner(permissionOwner2, android.os.UserHandle.getUserId(oldRecord.getUid()), key);
            permissionOwner = null;
        } else {
            permissionOwner = permissionOwner2;
        }
        if (newUris != null && permissionOwner != null) {
            for (int i = 0; i < newUris.size(); i++) {
                android.net.Uri uri = newUris.valueAt(i);
                if (oldUris == null || !oldUris.contains(uri)) {
                    if (DBG) {
                        android.util.Slog.d(TAG, key + ": granting " + uri);
                    }
                    grantUriPermission(permissionOwner, uri, newRecord.getUid(), targetPkg, targetUserId);
                }
            }
        }
        if (oldUris != null && permissionOwner != null) {
            for (int i2 = 0; i2 < oldUris.size(); i2++) {
                android.net.Uri uri2 = oldUris.valueAt(i2);
                if (newUris == null || !newUris.contains(uri2)) {
                    if (DBG) {
                        android.util.Slog.d(TAG, key + ": revoking " + uri2);
                    }
                    if (onlyRevokeCurrentTarget) {
                        revokeUriPermission(permissionOwner, uri2, android.os.UserHandle.getUserId(oldRecord.getUid()), targetPkg, targetUserId);
                    } else {
                        revokeUriPermission(permissionOwner, uri2, android.os.UserHandle.getUserId(oldRecord.getUid()), null, -1);
                    }
                }
            }
        }
        if (newRecord != null) {
            newRecord.permissionOwner = permissionOwner;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantUriPermission(android.os.IBinder owner, android.net.Uri uri, int sourceUid, java.lang.String targetPkg, int targetUserId) {
        if (uri == null || !com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(uri.getScheme())) {
            return;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mUgm.grantUriPermissionFromOwner(owner, sourceUid, targetPkg, android.content.ContentProvider.getUriWithoutUserId(uri), 1, android.content.ContentProvider.getUserIdFromUri(uri, android.os.UserHandle.getUserId(sourceUid)), targetUserId);
            } catch (android.os.RemoteException e) {
            } catch (java.lang.SecurityException e2) {
                android.util.Slog.e(TAG, "Cannot grant uri access; " + sourceUid + " does not own " + uri);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void revokeUriPermission(android.os.IBinder owner, android.net.Uri uri, int sourceUserId, java.lang.String targetPkg, int targetUserId) {
        if (uri == null || !com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(uri.getScheme())) {
            return;
        }
        int userId = android.content.ContentProvider.getUserIdFromUri(uri, sourceUserId);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mUgmInternal.revokeUriPermissionFromOwner(owner, android.content.ContentProvider.getUriWithoutUserId(uri), 1, userId, targetPkg, targetUserId);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyPermissionOwner(android.os.IBinder owner, int userId, java.lang.String logKey) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (DBG) {
                android.util.Slog.d(TAG, logKey + ": destroying owner");
            }
            this.mUgmInternal.revokeUriPermissionFromOwner(owner, null, -1, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    void cancelNotification(int callingUid, int callingPid, java.lang.String pkg, java.lang.String tag, int id, int mustHaveFlags, int mustNotHaveFlags, boolean sendDelete, int userId, int reason, com.android.server.notification.ManagedServices.ManagedServiceInfo listener) {
        cancelNotification(callingUid, callingPid, pkg, tag, id, mustHaveFlags, mustNotHaveFlags, sendDelete, userId, reason, -1, -1, listener);
    }

    void cancelNotification(int callingUid, int callingPid, java.lang.String pkg, java.lang.String tag, int id, int mustHaveFlags, int mustNotHaveFlags, boolean sendDelete, int userId, int reason, int rank, int count, com.android.server.notification.ManagedServices.ManagedServiceInfo listener) {
        this.mHandler.scheduleCancelNotification(new com.android.server.notification.NotificationManagerService.CancelNotificationRunnable(callingUid, callingPid, pkg, tag, id, mustHaveFlags, mustNotHaveFlags, sendDelete, userId, reason, rank, count, listener, android.os.SystemClock.elapsedRealtime()), 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean notificationMatchesUserId(com.android.server.notification.NotificationRecord r, int userId, boolean isAutogroupSummary) {
        if (this.mNMSWrapper.getNMSExt() == null || !this.mNMSWrapper.getNMSExt().isNotificationForCurrentUser(r, userId)) {
            return isAutogroupSummary ? r.getUserId() == userId : userId == -1 || r.getUserId() == -1 || r.getUserId() == userId;
        }
        return true;
    }

    private boolean notificationMatchesCurrentProfiles(com.android.server.notification.NotificationRecord r, int userId) {
        return notificationMatchesUserId(r, userId, false) || this.mUserProfiles.isCurrentProfile(r.getUserId()) || (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isNotificationForCurrentUser(r, r.getUserId()));
    }

    void cancelAllNotificationsInt(int callingUid, int callingPid, java.lang.String pkg, java.lang.String channelId, int mustHaveFlags, int mustNotHaveFlags, int userId, int reason) {
        if (this.mNMSWrapper.getNMSExt() != null) {
            this.mNMSWrapper.getNMSExt().setNavigationStatus(pkg, channelId, callingUid, callingPid, reason);
        }
        long cancellationElapsedTimeMs = android.os.SystemClock.elapsedRealtime();
        this.mHandler.post(new com.android.server.notification.NotificationManagerService.AnonymousClass16(callingUid, callingPid, pkg, userId, mustHaveFlags, mustNotHaveFlags, reason, channelId, cancellationElapsedTimeMs));
    }

    /* JADX INFO: renamed from: com.android.server.notification.NotificationManagerService$16, reason: invalid class name */
    class AnonymousClass16 implements java.lang.Runnable {
        final /* synthetic */ int val$callingPid;
        final /* synthetic */ int val$callingUid;
        final /* synthetic */ long val$cancellationElapsedTimeMs;
        final /* synthetic */ java.lang.String val$channelId;
        final /* synthetic */ int val$mustHaveFlags;
        final /* synthetic */ int val$mustNotHaveFlags;
        final /* synthetic */ java.lang.String val$pkg;
        final /* synthetic */ int val$reason;
        final /* synthetic */ int val$userId;

        AnonymousClass16(int i, int i2, java.lang.String str, int i3, int i4, int i5, int i6, java.lang.String str2, long j) {
            this.val$callingUid = i;
            this.val$callingPid = i2;
            this.val$pkg = str;
            this.val$userId = i3;
            this.val$mustHaveFlags = i4;
            this.val$mustNotHaveFlags = i5;
            this.val$reason = i6;
            this.val$channelId = str2;
            this.val$cancellationElapsedTimeMs = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            com.android.server.EventLogTags.writeNotificationCancelAll(this.val$callingUid, this.val$callingPid, this.val$pkg, this.val$userId, this.val$mustHaveFlags, this.val$mustNotHaveFlags, this.val$reason, null);
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                final int i = this.val$mustHaveFlags;
                final int i2 = this.val$mustNotHaveFlags;
                com.android.server.notification.NotificationManagerService.FlagChecker flagChecker = new com.android.server.notification.NotificationManagerService.FlagChecker() { // from class: com.android.server.notification.NotificationManagerService$16$$ExternalSyntheticLambda0
                    @Override // com.android.server.notification.NotificationManagerService.FlagChecker
                    public final boolean apply(int i3) {
                        return com.android.server.notification.NotificationManagerService.AnonymousClass16.lambda$run$0(i, i2, i3);
                    }
                };
                com.android.server.notification.NotificationManagerService.this.cancelAllNotificationsByListLocked(com.android.server.notification.NotificationManagerService.this.mNotificationList, this.val$pkg, true, this.val$channelId, flagChecker, false, this.val$userId, false, this.val$reason, null, true, this.val$cancellationElapsedTimeMs);
                com.android.server.notification.NotificationManagerService.this.cancelAllNotificationsByListLocked(com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications, this.val$pkg, true, this.val$channelId, flagChecker, false, this.val$userId, false, this.val$reason, null, false, this.val$cancellationElapsedTimeMs);
                com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.cancel(this.val$userId, this.val$pkg);
            }
        }

        static /* synthetic */ boolean lambda$run$0(int mustHaveFlags, int mustNotHaveFlags, int flags) {
            return (flags & mustHaveFlags) == mustHaveFlags && (flags & mustNotHaveFlags) == 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0035  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void cancelAllNotificationsByListLocked(java.util.ArrayList<com.android.server.notification.NotificationRecord> r20, java.lang.String r21, boolean r22, java.lang.String r23, com.android.server.notification.NotificationManagerService.FlagChecker r24, boolean r25, int r26, boolean r27, int r28, java.lang.String r29, boolean r30, long r31) {
        /*
            Method dump skipped, instruction units count: 405
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationManagerService.cancelAllNotificationsByListLocked(java.util.ArrayList, java.lang.String, boolean, java.lang.String, com.android.server.notification.NotificationManagerService$FlagChecker, boolean, int, boolean, int, java.lang.String, boolean, long):void");
    }

    void snoozeNotificationInt(int callingUid, android.service.notification.INotificationListener token, java.lang.String key, long duration, java.lang.String snoozeCriterionId) throws java.lang.Throwable {
        synchronized (this.mNotificationLock) {
            try {
                try {
                    com.android.server.notification.ManagedServices.ManagedServiceInfo listener = this.mListeners.checkServiceTokenLocked(token);
                    if (listener == null) {
                        return;
                    }
                    java.lang.String packageName = listener.component.getPackageName();
                    java.lang.String listenerName = listener.component.toShortString();
                    if ((duration > 0 || snoozeCriterionId != null) && key != null) {
                        com.android.server.notification.NotificationRecord r = findInCurrentAndSnoozedNotificationByKeyLocked(key);
                        if (r == null) {
                            return;
                        }
                        if (listener.enabledAndUserMatches(r.getSbn().getNormalizedUserId())) {
                            long notificationUpdateTimeMs = r.getUpdateTimeMs();
                            if (DBG) {
                                android.util.Slog.d(TAG, java.lang.String.format("snooze event(%s, %d, %s, %s)", key, java.lang.Long.valueOf(duration), snoozeCriterionId, listenerName));
                            }
                            this.mHandler.post(new com.android.server.notification.NotificationManagerService.SnoozeNotificationRunnable(key, duration, snoozeCriterionId));
                            if (isNotificationRecent(notificationUpdateTimeMs)) {
                                this.mAppOps.noteOpNoThrow(142, callingUid, packageName, (java.lang.String) null, (java.lang.String) null);
                            }
                        }
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    void unsnoozeNotificationInt(java.lang.String key, com.android.server.notification.ManagedServices.ManagedServiceInfo listener, boolean muteOnReturn) {
        java.lang.String listenerName = listener == null ? null : listener.component.toShortString();
        if (DBG) {
            android.util.Slog.d(TAG, java.lang.String.format("unsnooze event(%s, %s)", key, listenerName));
        }
        this.mSnoozeHelper.repost(key, muteOnReturn);
        handleSavePolicyFile();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNotificationRecent(long notificationUpdateTimeMs) {
        return com.android.internal.hidden_from_bootclasspath.android.view.contentprotection.flags.Flags.rapidClearNotificationsByListenerAppOpEnabled() && java.lang.System.currentTimeMillis() - notificationUpdateTimeMs < MIN_PACKAGE_OVERRATE_LOG_INTERVAL;
    }

    /* JADX INFO: renamed from: com.android.server.notification.NotificationManagerService$17, reason: invalid class name */
    class AnonymousClass17 implements java.lang.Runnable {
        final /* synthetic */ int val$callingPid;
        final /* synthetic */ int val$callingUid;
        final /* synthetic */ long val$cancellationElapsedTimeMs;
        final /* synthetic */ boolean val$includeCurrentProfiles;
        final /* synthetic */ com.android.server.notification.ManagedServices.ManagedServiceInfo val$listener;
        final /* synthetic */ int val$mustNotHaveFlags;
        final /* synthetic */ int val$reason;
        final /* synthetic */ int val$userId;

        AnonymousClass17(com.android.server.notification.ManagedServices.ManagedServiceInfo managedServiceInfo, int i, int i2, int i3, int i4, int i5, boolean z, long j) {
            this.val$listener = managedServiceInfo;
            this.val$callingUid = i;
            this.val$callingPid = i2;
            this.val$userId = i3;
            this.val$reason = i4;
            this.val$mustNotHaveFlags = i5;
            this.val$includeCurrentProfiles = z;
            this.val$cancellationElapsedTimeMs = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                java.lang.String listenerName = this.val$listener == null ? null : this.val$listener.component.toShortString();
                com.android.server.EventLogTags.writeNotificationCancelAll(this.val$callingUid, this.val$callingPid, null, this.val$userId, 0, 0, this.val$reason, listenerName);
                final int i = this.val$mustNotHaveFlags;
                final int i2 = this.val$reason;
                com.android.server.notification.NotificationManagerService.FlagChecker flagChecker = new com.android.server.notification.NotificationManagerService.FlagChecker() { // from class: com.android.server.notification.NotificationManagerService$17$$ExternalSyntheticLambda0
                    @Override // com.android.server.notification.NotificationManagerService.FlagChecker
                    public final boolean apply(int i3) {
                        return com.android.server.notification.NotificationManagerService.AnonymousClass17.lambda$run$0(i, i2, i3);
                    }
                };
                com.android.server.notification.NotificationManagerService.this.cancelAllNotificationsByListLocked(com.android.server.notification.NotificationManagerService.this.mNotificationList, null, false, null, flagChecker, this.val$includeCurrentProfiles, this.val$userId, true, this.val$reason, listenerName, true, this.val$cancellationElapsedTimeMs);
                com.android.server.notification.NotificationManagerService.this.cancelAllNotificationsByListLocked(com.android.server.notification.NotificationManagerService.this.mEnqueuedNotifications, null, false, null, flagChecker, this.val$includeCurrentProfiles, this.val$userId, true, this.val$reason, listenerName, false, this.val$cancellationElapsedTimeMs);
                if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                    com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().clearNonCancelableSummaryKeys(this.val$reason);
                }
                com.android.server.notification.NotificationManagerService.this.mSnoozeHelper.cancel(this.val$userId, this.val$includeCurrentProfiles);
            }
        }

        static /* synthetic */ boolean lambda$run$0(int mustNotHaveFlags, int reason, int flags) {
            int flagsToCheck = mustNotHaveFlags;
            if (11 == reason || 3 == reason) {
                flagsToCheck |= 4096;
            }
            if ((flags & flagsToCheck) != 0) {
                return false;
            }
            return true;
        }
    }

    void cancelAllLocked(int callingUid, int callingPid, int userId, int reason, com.android.server.notification.ManagedServices.ManagedServiceInfo listener, boolean includeCurrentProfiles, int mustNotHaveFlags) {
        long cancellationElapsedTimeMs = android.os.SystemClock.elapsedRealtime();
        this.mHandler.post(new com.android.server.notification.NotificationManagerService.AnonymousClass17(listener, callingUid, callingPid, userId, reason, mustNotHaveFlags, includeCurrentProfiles, cancellationElapsedTimeMs));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelGroupChildrenLocked(com.android.server.notification.NotificationRecord r, int callingUid, int callingPid, java.lang.String listenerName, boolean sendDelete, com.android.server.notification.NotificationManagerService.FlagChecker flagChecker, int reason, long cancellationElapsedTimeMs) {
        android.app.Notification n = r.getNotification();
        if (!n.isGroupSummary()) {
            return;
        }
        java.lang.String pkg = r.getSbn().getPackageName();
        if (pkg == null) {
            if (DBG) {
                android.util.Slog.e(TAG, "No package for group summary: " + r.getKey());
            }
        } else {
            if (this.mNMSWrapper.getNMSExt() != null && this.mNMSWrapper.getNMSExt().isInterceptCancelGroupChildren(r.getSbn(), reason)) {
                return;
            }
            cancelGroupChildrenByListLocked(this.mNotificationList, r, callingUid, callingPid, listenerName, sendDelete, true, flagChecker, reason, cancellationElapsedTimeMs);
            cancelGroupChildrenByListLocked(this.mEnqueuedNotifications, r, callingUid, callingPid, listenerName, sendDelete, false, flagChecker, reason, cancellationElapsedTimeMs);
        }
    }

    private void cancelGroupChildrenByListLocked(java.util.ArrayList<com.android.server.notification.NotificationRecord> notificationList, com.android.server.notification.NotificationRecord parentNotification, int callingUid, int callingPid, java.lang.String listenerName, boolean sendDelete, boolean wasPosted, com.android.server.notification.NotificationManagerService.FlagChecker flagChecker, int reason, long cancellationElapsedTimeMs) {
        java.lang.String pkg = parentNotification.getSbn().getPackageName();
        int userId = parentNotification.getUserId();
        for (int i = notificationList.size() - 1; i >= 0; i--) {
            com.android.server.notification.NotificationRecord childR = notificationList.get(i);
            android.service.notification.StatusBarNotification childSbn = childR.getSbn();
            if (childSbn.isGroup() && !childSbn.getNotification().isGroupSummary()) {
                if (childR.getGroupKey().equals(parentNotification.getGroupKey()) && ((flagChecker == null || flagChecker.apply(childR.getFlags())) && (!childR.getChannel().isImportantConversation() || reason != 2))) {
                    com.android.server.EventLogTags.writeNotificationCancel(callingUid, callingPid, pkg, childSbn.getId(), childSbn.getTag(), userId, 0, 0, 12, listenerName);
                    notificationList.remove(i);
                    this.mNotificationsByKey.remove(childR.getKey());
                    cancelNotificationLocked(childR, sendDelete, 12, wasPosted, listenerName, cancellationElapsedTimeMs);
                }
            }
        }
    }

    java.util.List<com.android.server.notification.NotificationRecord> findCurrentAndSnoozedGroupNotificationsLocked(java.lang.String pkg, java.lang.String groupKey, int userId) {
        java.util.List<com.android.server.notification.NotificationRecord> records = this.mSnoozeHelper.getNotifications(pkg, groupKey, java.lang.Integer.valueOf(userId));
        records.addAll(findGroupNotificationsLocked(pkg, groupKey, userId));
        return records;
    }

    java.util.List<com.android.server.notification.NotificationRecord> findGroupNotificationsLocked(java.lang.String pkg, java.lang.String groupKey, int userId) {
        java.util.List<com.android.server.notification.NotificationRecord> records = new java.util.ArrayList<>();
        records.addAll(findGroupNotificationByListLocked(this.mNotificationList, pkg, groupKey, userId));
        records.addAll(findGroupNotificationByListLocked(this.mEnqueuedNotifications, pkg, groupKey, userId));
        return records;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.notification.NotificationRecord findInCurrentAndSnoozedNotificationByKeyLocked(java.lang.String key) {
        com.android.server.notification.NotificationRecord r = findNotificationByKeyLocked(key);
        if (r == null) {
            return this.mSnoozeHelper.getNotification(key);
        }
        return r;
    }

    private java.util.List<com.android.server.notification.NotificationRecord> findGroupNotificationByListLocked(java.util.ArrayList<com.android.server.notification.NotificationRecord> list, java.lang.String pkg, java.lang.String groupKey, int userId) {
        java.util.List<com.android.server.notification.NotificationRecord> records = new java.util.ArrayList<>();
        int len = list.size();
        for (int i = 0; i < len; i++) {
            com.android.server.notification.NotificationRecord r = list.get(i);
            if (notificationMatchesUserId(r, userId, false) && r.getGroupKey().equals(groupKey) && r.getSbn().getPackageName().equals(pkg)) {
                records.add(r);
            }
        }
        return records;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.notification.NotificationRecord findNotificationByKeyLocked(java.lang.String key) {
        com.android.server.notification.NotificationRecord r = findNotificationByListLocked(this.mNotificationList, key);
        if (r != null) {
            return r;
        }
        com.android.server.notification.NotificationRecord r2 = findNotificationByListLocked(this.mEnqueuedNotifications, key);
        if (r2 != null) {
            return r2;
        }
        return null;
    }

    com.android.server.notification.NotificationRecord findNotificationLocked(java.lang.String pkg, java.lang.String tag, int id, int userId) {
        com.android.server.notification.NotificationRecord r = findNotificationByListLocked(this.mNotificationList, pkg, tag, id, userId);
        if (r != null) {
            return r;
        }
        com.android.server.notification.NotificationRecord r2 = findNotificationByListLocked(this.mEnqueuedNotifications, pkg, tag, id, userId);
        if (r2 != null) {
            return r2;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.notification.NotificationRecord findNotificationByListLocked(java.util.ArrayList<com.android.server.notification.NotificationRecord> list, java.lang.String pkg, java.lang.String tag, int id, int userId) {
        int len = list.size();
        for (int i = 0; i < len; i++) {
            com.android.server.notification.NotificationRecord r = list.get(i);
            if (notificationMatchesUserId(r, userId, (r.getFlags() & 1792) != 0) && r.getSbn().getId() == id && android.text.TextUtils.equals(r.getSbn().getTag(), tag) && r.getSbn().getPackageName().equals(pkg)) {
                return r;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<com.android.server.notification.NotificationRecord> findNotificationsByListLocked(java.util.ArrayList<com.android.server.notification.NotificationRecord> list, java.lang.String pkg, java.lang.String tag, int id, int userId) {
        java.util.List<com.android.server.notification.NotificationRecord> matching = new java.util.ArrayList<>();
        int len = list.size();
        for (int i = 0; i < len; i++) {
            com.android.server.notification.NotificationRecord r = list.get(i);
            if (notificationMatchesUserId(r, userId, false) && r.getSbn().getId() == id && android.text.TextUtils.equals(r.getSbn().getTag(), tag) && r.getSbn().getPackageName().equals(pkg)) {
                matching.add(r);
            }
        }
        return matching;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.android.server.notification.NotificationRecord findNotificationByListLocked(java.util.ArrayList<com.android.server.notification.NotificationRecord> list, java.lang.String key) {
        int N = list.size();
        for (int i = 0; i < N; i++) {
            if (key.equals(list.get(i).getKey())) {
                return list.get(i);
            }
        }
        return null;
    }

    int indexOfNotificationLocked(java.lang.String key) {
        int N = this.mNotificationList.size();
        for (int i = 0; i < N; i++) {
            if (key.equals(this.mNotificationList.get(i).getKey())) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideNotificationsForPackages(java.lang.String[] pkgs, int[] uidList) {
        synchronized (this.mNotificationLock) {
            java.util.Set<java.lang.Integer> uidSet = (java.util.Set) java.util.Arrays.stream(uidList).boxed().collect(java.util.stream.Collectors.toSet());
            java.util.List<java.lang.String> pkgList = java.util.Arrays.asList(pkgs);
            java.util.List<com.android.server.notification.NotificationRecord> changedNotifications = new java.util.ArrayList<>();
            int numNotifications = this.mNotificationList.size();
            for (int i = 0; i < numNotifications; i++) {
                com.android.server.notification.NotificationRecord rec = this.mNotificationList.get(i);
                if (pkgList.contains(rec.getSbn().getPackageName()) && uidSet.contains(java.lang.Integer.valueOf(rec.getUid()))) {
                    rec.setHidden(true);
                    changedNotifications.add(rec);
                }
            }
            this.mListeners.notifyHiddenLocked(changedNotifications);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unhideNotificationsForPackages(java.lang.String[] pkgs, int[] uidList) {
        synchronized (this.mNotificationLock) {
            java.util.Set<java.lang.Integer> uidSet = (java.util.Set) java.util.Arrays.stream(uidList).boxed().collect(java.util.stream.Collectors.toSet());
            java.util.List<java.lang.String> pkgList = java.util.Arrays.asList(pkgs);
            java.util.List<com.android.server.notification.NotificationRecord> changedNotifications = new java.util.ArrayList<>();
            int numNotifications = this.mNotificationList.size();
            for (int i = 0; i < numNotifications; i++) {
                com.android.server.notification.NotificationRecord rec = this.mNotificationList.get(i);
                if (pkgList.contains(rec.getSbn().getPackageName()) && uidSet.contains(java.lang.Integer.valueOf(rec.getUid()))) {
                    rec.setHidden(false);
                    changedNotifications.add(rec);
                }
            }
            this.mListeners.notifyUnhiddenLocked(changedNotifications);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelNotificationsWhenEnterLockDownMode(int userId) {
        synchronized (this.mNotificationLock) {
            int numNotifications = this.mNotificationList.size();
            for (int i = 0; i < numNotifications; i++) {
                com.android.server.notification.NotificationRecord rec = this.mNotificationList.get(i);
                if (rec.getUser().getIdentifier() == userId) {
                    this.mListeners.notifyRemovedLocked(rec, 23, rec.getStats());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postNotificationsWhenExitLockDownMode(int userId) {
        synchronized (this.mNotificationLock) {
            int numNotifications = this.mNotificationList.size();
            long delay = 0;
            for (int i = 0; i < numNotifications; i++) {
                final com.android.server.notification.NotificationRecord rec = this.mNotificationList.get(i);
                if (rec.getUser().getIdentifier() == userId) {
                    this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$postNotificationsWhenExitLockDownMode$9(rec);
                        }
                    }, delay);
                    delay += 20;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postNotificationsWhenExitLockDownMode$9(com.android.server.notification.NotificationRecord rec) {
        synchronized (this.mNotificationLock) {
            this.mListeners.notifyPostedLocked(rec, rec);
        }
    }

    protected boolean isCallingUidSystem() {
        int uid = android.os.Binder.getCallingUid();
        return uid == 1000;
    }

    protected boolean isCallingAppIdSystem() {
        int uid = android.os.Binder.getCallingUid();
        int appid = android.os.UserHandle.getAppId(uid);
        return appid == 1000;
    }

    protected boolean isUidSystemOrPhone(int uid) {
        int appid = android.os.UserHandle.getAppId(uid);
        return appid == 1000 || appid == 1001 || uid == 0;
    }

    protected boolean isCallerSystemOrPhone() {
        return isUidSystemOrPhone(android.os.Binder.getCallingUid());
    }

    protected boolean isCallerSystemOrSystemUi() {
        return isCallerSystemOrPhone() || getContext().checkCallingPermission("android.permission.STATUS_BAR_SERVICE") == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCallerSystemOrSystemUiOrShell() {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid == 2000 || callingUid == 0) {
            return true;
        }
        return isCallerSystemOrSystemUi();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSystemOrShell() {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid == 2000 || callingUid == 0) {
            return;
        }
        checkCallerIsSystem();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSystem() {
        if (isCallerSystemOrPhone()) {
        } else {
            throw new java.lang.SecurityException("Disallowed call for uid " + android.os.Binder.getCallingUid());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSystemOrSystemUiOrShell() {
        checkCallerIsSystemOrSystemUiOrShell(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSystemOrSystemUiOrShell(java.lang.String message) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid == 2000 || callingUid == 0 || isCallerSystemOrPhone()) {
            return;
        }
        getContext().enforceCallingPermission("android.permission.STATUS_BAR_SERVICE", message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSystemOrSameApp(java.lang.String pkg) {
        if (isCallerSystemOrPhone()) {
            return;
        }
        checkCallerIsSameApp(pkg);
    }

    private boolean isCallerAndroid(java.lang.String callingPkg, int uid) {
        return isUidSystemOrPhone(uid) && callingPkg != null && com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(callingPkg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkRestrictedCategories(android.app.Notification notification) {
        try {
            if (!this.mPackageManager.hasSystemFeature("android.hardware.type.automotive", 0)) {
                return;
            }
        } catch (android.os.RemoteException e) {
            if (DBG) {
                android.util.Slog.e(TAG, "Unable to confirm if it's safe to skip category restrictions check thus the check will be done anyway");
            }
        }
        if ("car_emergency".equals(notification.category) || "car_warning".equals(notification.category) || "car_information".equals(notification.category)) {
            getContext().enforceCallingPermission("android.permission.SEND_CATEGORY_CAR_NOTIFICATIONS", java.lang.String.format("Notification category %s restricted", notification.category));
        }
    }

    boolean isCallerInstantApp(int callingUid, int userId) {
        if (isUidSystemOrPhone(callingUid)) {
            return false;
        }
        if (userId == -1) {
            userId = 0;
        }
        try {
            java.lang.String[] pkgs = this.mPackageManager.getPackagesForUid(callingUid);
            if (pkgs == null) {
                throw new java.lang.SecurityException("Unknown uid " + callingUid);
            }
            java.lang.String pkg = pkgs[0];
            this.mAppOps.checkPackage(callingUid, pkg);
            android.content.pm.ApplicationInfo ai = this.mPackageManager.getApplicationInfo(pkg, 0L, userId);
            if (ai == null) {
                throw new java.lang.SecurityException("Unknown package " + pkg);
            }
            return ai.isInstantApp();
        } catch (android.os.RemoteException re) {
            throw new java.lang.SecurityException("Unknown uid " + callingUid, re);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSameApp(java.lang.String pkg) {
        checkCallerIsSameApp(pkg, android.os.Binder.getCallingUid(), android.os.UserHandle.getCallingUserId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkCallerIsSameApp(java.lang.String pkg, int uid, int userId) {
        if ((uid != 0 || !ROOT_PKG.equals(pkg)) && !this.mPackageManagerInternal.isSameApp(pkg, uid, userId)) {
            throw new java.lang.SecurityException("Package " + pkg + " is not owned by uid " + uid);
        }
    }

    private boolean isCallerSameApp(java.lang.String pkg, int uid, int userId) {
        try {
            checkCallerIsSameApp(pkg, uid, userId);
            return true;
        } catch (java.lang.SecurityException e) {
            return false;
        }
    }

    private static java.lang.String callStateToString(int state) {
        switch (state) {
            case 0:
                return "CALL_STATE_IDLE";
            case 1:
                return "CALL_STATE_RINGING";
            case 2:
                return "CALL_STATE_OFFHOOK";
            default:
                return "CALL_STATE_UNKNOWN_" + state;
        }
    }

    android.service.notification.NotificationRankingUpdate makeRankingUpdateLocked(com.android.server.notification.ManagedServices.ManagedServiceInfo info) {
        java.util.ArrayList<android.app.Notification.Action> smartActions;
        java.util.ArrayList<java.lang.CharSequence> smartReplies;
        int i;
        int N = this.mNotificationList.size();
        java.util.ArrayList<android.service.notification.NotificationListenerService.Ranking> rankings = new java.util.ArrayList<>();
        for (int i2 = 0; i2 < N; i2++) {
            com.android.server.notification.NotificationRecord record = this.mNotificationList.get(i2);
            if (!isInLockDownMode(record.getUser().getIdentifier()) && isVisibleToListener(record.getSbn(), record.getNotificationType(), info)) {
                java.lang.String key = record.getSbn().getKey();
                android.service.notification.NotificationListenerService.Ranking ranking = new android.service.notification.NotificationListenerService.Ranking();
                java.util.ArrayList<android.app.Notification.Action> smartActions2 = record.getSystemGeneratedSmartActions();
                java.util.ArrayList<java.lang.CharSequence> smartReplies2 = record.getSmartReplies();
                if (com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.redactSensitiveNotificationsFromUntrustedListeners() && info != null && !this.mListeners.isUidTrusted(info.uid) && this.mListeners.hasSensitiveContent(record)) {
                    smartActions = null;
                    smartReplies = null;
                } else {
                    smartActions = smartActions2;
                    smartReplies = smartReplies2;
                }
                int size = rankings.size();
                boolean z = !record.isIntercepted();
                int packageVisibilityOverride = record.getPackageVisibilityOverride();
                int suppressedVisualEffects = record.getSuppressedVisualEffects();
                int importance = record.getImportance();
                java.lang.CharSequence importanceExplanation = record.getImportanceExplanation();
                java.lang.String overrideGroupKey = record.getSbn().getOverrideGroupKey();
                android.app.NotificationChannel channel = record.getChannel();
                java.util.ArrayList<java.lang.String> peopleOverride = record.getPeopleOverride();
                java.util.ArrayList<android.service.notification.SnoozeCriterion> snoozeCriteria = record.getSnoozeCriteria();
                boolean zCanShowBadge = record.canShowBadge();
                int userSentiment = record.getUserSentiment();
                boolean zIsHidden = record.isHidden();
                long lastAudiblyAlertedMs = record.getLastAudiblyAlertedMs();
                boolean z2 = (record.getSound() == null && record.getVibration() == null) ? false : true;
                boolean zCanBubble = record.canBubble();
                boolean zIsTextChanged = record.isTextChanged();
                boolean zIsConversation = record.isConversation();
                android.content.pm.ShortcutInfo shortcutInfo = record.getShortcutInfo();
                if (record.getRankingScore() != 0.0f) {
                    i = record.getRankingScore() > 0.0f ? 1 : -1;
                } else {
                    i = 0;
                }
                ranking.populate(key, size, z, packageVisibilityOverride, suppressedVisualEffects, importance, importanceExplanation, overrideGroupKey, channel, peopleOverride, snoozeCriteria, zCanShowBadge, userSentiment, zIsHidden, lastAudiblyAlertedMs, z2, smartActions, smartReplies, zCanBubble, zIsTextChanged, zIsConversation, shortcutInfo, i, record.getNotification().isBubbleNotification(), record.getProposedImportance(), record.hasSensitiveContent(), record.getRankingScore());
                rankings.add(ranking);
            }
        }
        return new android.service.notification.NotificationRankingUpdate((android.service.notification.NotificationListenerService.Ranking[]) rankings.toArray(new android.service.notification.NotificationListenerService.Ranking[0]));
    }

    boolean isInLockDownMode(int userId) {
        return this.mStrongAuthTracker.isInLockDownMode(userId);
    }

    boolean hasCompanionDevice(com.android.server.notification.ManagedServices.ManagedServiceInfo info) {
        return hasCompanionDevice(info.component.getPackageName(), info.userid, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasCompanionDevice(java.lang.String pkg, int userId, java.util.Set<java.lang.String> withDeviceProfiles) {
        if (this.mCompanionManager == null) {
            this.mCompanionManager = getCompanionManager();
        }
        if (this.mCompanionManager == null) {
            return false;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            try {
                java.util.List<android.companion.AssociationInfo> associations = this.mCompanionManager.getAssociations(pkg, userId);
                for (android.companion.AssociationInfo association : associations) {
                    if (withDeviceProfiles == null || withDeviceProfiles.contains(association.getDeviceProfile())) {
                        return true;
                    }
                }
                if (this.mNMSWrapper.getNMSExt() != null) {
                    if (this.mNMSWrapper.getNMSExt().canListenNotificationChannelChange(pkg)) {
                        return true;
                    }
                }
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(TAG, "Cannot reach companion device service", re);
            } catch (java.lang.SecurityException e) {
            } catch (java.lang.Exception e2) {
                android.util.Slog.e(TAG, "Cannot verify caller pkg=" + pkg + ", userId=" + userId, e2);
            }
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    protected android.companion.ICompanionDeviceManager getCompanionManager() {
        return android.companion.ICompanionDeviceManager.Stub.asInterface(android.os.ServiceManager.getService("companiondevice"));
    }

    boolean isVisibleToListener(android.service.notification.StatusBarNotification sbn, int notificationType, com.android.server.notification.ManagedServices.ManagedServiceInfo listener) {
        if ((this.mNMSWrapper.getNMSExt() != null && !this.mNMSWrapper.getNMSExt().enabledAndUserMatches(sbn, listener)) || !isInteractionVisibleToListener(listener, sbn.getUserId())) {
            return false;
        }
        android.service.notification.NotificationListenerFilter nls = this.mListeners.getNotificationListenerFilter(listener.mKey);
        if (nls != null) {
            return nls.isTypeAllowed(notificationType) && nls.isPackageAllowed(new android.content.pm.VersionedPackage(sbn.getPackageName(), sbn.getUid()));
        }
        return true;
    }

    boolean isInteractionVisibleToListener(com.android.server.notification.ManagedServices.ManagedServiceInfo info, int userId) {
        boolean isAssistantService = isServiceTokenValid(info.getService());
        return !isAssistantService || info.isSameUser(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isServiceTokenValid(android.os.IInterface service) {
        boolean zIsServiceTokenValidLocked;
        synchronized (this.mNotificationLock) {
            zIsServiceTokenValidLocked = this.mAssistants.isServiceTokenValidLocked(service);
        }
        return zIsServiceTokenValidLocked;
    }

    private boolean isPackageSuspendedForUser(java.lang.String pkg, int uid) {
        long identity = android.os.Binder.clearCallingIdentity();
        int userId = android.os.UserHandle.getUserId(uid);
        try {
            try {
                return this.mPackageManager.isPackageSuspendedForUser(pkg, userId);
            } catch (android.os.RemoteException e) {
                throw new java.lang.SecurityException("Could not talk to package manager service");
            } catch (java.lang.IllegalArgumentException e2) {
                android.os.Binder.restoreCallingIdentity(identity);
                return false;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    boolean canUseManagedServices(java.lang.String pkg, java.lang.Integer userId, java.lang.String requiredPermission) {
        if (requiredPermission == null) {
            return true;
        }
        try {
            if (this.mPackageManager.checkPermission(requiredPermission, pkg, userId.intValue()) == 0) {
                return true;
            }
            return false;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "can't talk to pm", e);
            return true;
        }
    }

    private class TrimCache {
        android.service.notification.StatusBarNotification heavy;
        android.service.notification.StatusBarNotification sbnClone;
        android.service.notification.StatusBarNotification sbnCloneLight;

        TrimCache(android.service.notification.StatusBarNotification sbn) {
            this.heavy = sbn;
        }

        android.service.notification.StatusBarNotification ForListener(com.android.server.notification.ManagedServices.ManagedServiceInfo info) {
            if (com.android.server.notification.NotificationManagerService.this.mListeners.getOnNotificationPostedTrim(info) == 1) {
                if (this.sbnCloneLight == null) {
                    this.sbnCloneLight = this.heavy.cloneLight();
                }
                return this.sbnCloneLight;
            }
            if (this.sbnClone == null) {
                this.sbnClone = this.heavy.clone();
            }
            return this.sbnClone;
        }
    }

    public class NotificationAssistants extends com.android.server.notification.ManagedServices {
        private static final java.lang.String ATT_TYPES = "types";
        static final java.lang.String TAG_ENABLED_NOTIFICATION_ASSISTANTS = "enabled_assistants";
        private java.util.Set<java.lang.String> mAllowedAdjustments;
        protected android.content.ComponentName mDefaultFromConfig;
        private final java.lang.Object mLock;

        @Override // com.android.server.notification.ManagedServices
        protected void loadDefaultsFromConfig() {
            loadDefaultsFromConfig(true);
        }

        protected void loadDefaultsFromConfig(boolean addToDefault) {
            android.util.ArraySet<java.lang.String> assistants = new android.util.ArraySet<>();
            assistants.addAll(java.util.Arrays.asList(this.mContext.getResources().getString(android.R.string.config_defaultContextualSearchKey).split(":")));
            for (int i = 0; i < assistants.size(); i++) {
                android.content.ComponentName assistantCn = android.content.ComponentName.unflattenFromString(assistants.valueAt(i));
                java.lang.String packageName = assistants.valueAt(i);
                if (assistantCn != null) {
                    packageName = assistantCn.getPackageName();
                }
                if (!android.text.TextUtils.isEmpty(packageName)) {
                    android.util.ArraySet<android.content.ComponentName> approved = queryPackageForServices(packageName, com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED, 0);
                    if (approved.contains(assistantCn)) {
                        if (addToDefault) {
                            addDefaultComponentOrPackage(assistantCn.flattenToString());
                        } else {
                            this.mDefaultFromConfig = assistantCn;
                        }
                    }
                }
            }
        }

        android.content.ComponentName getDefaultFromConfig() {
            if (this.mDefaultFromConfig == null) {
                loadDefaultsFromConfig(false);
            }
            return this.mDefaultFromConfig;
        }

        @Override // com.android.server.notification.ManagedServices
        protected void upgradeUserSet() {
            java.util.Iterator<java.lang.Integer> it = this.mApproved.keySet().iterator();
            while (it.hasNext()) {
                int userId = it.next().intValue();
                android.util.ArraySet<java.lang.String> userSetServices = this.mUserSetServices.get(java.lang.Integer.valueOf(userId));
                this.mIsUserChanged.put(java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(userSetServices != null && userSetServices.size() > 0));
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected void addApprovedList(java.lang.String approved, int userId, boolean isPrimary, java.lang.String userSet) {
            if (!android.text.TextUtils.isEmpty(approved)) {
                java.lang.String[] approvedArray = approved.split(":");
                if (approvedArray.length > 1) {
                    android.util.Slog.d(this.TAG, "More than one approved assistants");
                    approved = approvedArray[0];
                }
            }
            super.addApprovedList(approved, userId, isPrimary, userSet);
        }

        public NotificationAssistants(android.content.Context context, java.lang.Object lock, com.android.server.notification.ManagedServices.UserProfiles up, android.content.pm.IPackageManager pm) {
            super(context, lock, up, pm);
            this.mLock = new java.lang.Object();
            this.mAllowedAdjustments = new android.util.ArraySet();
            this.mDefaultFromConfig = null;
            for (int i = 0; i < com.android.server.notification.NotificationManagerService.ALLOWED_ADJUSTMENTS.length; i++) {
                this.mAllowedAdjustments.add(com.android.server.notification.NotificationManagerService.ALLOWED_ADJUSTMENTS[i]);
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected com.android.server.notification.ManagedServices.Config getConfig() {
            com.android.server.notification.ManagedServices.Config c = new com.android.server.notification.ManagedServices.Config();
            c.caption = "notification assistant";
            c.serviceInterface = "android.service.notification.NotificationAssistantService";
            c.xmlTag = TAG_ENABLED_NOTIFICATION_ASSISTANTS;
            c.secureSettingName = "enabled_notification_assistant";
            c.bindPermission = "android.permission.BIND_NOTIFICATION_ASSISTANT_SERVICE";
            c.settingsAction = "android.settings.MANAGE_DEFAULT_APPS_SETTINGS";
            c.clientLabel = android.R.string.notification_channel_system_time;
            return c;
        }

        @Override // com.android.server.notification.ManagedServices
        protected android.os.IInterface asInterface(android.os.IBinder binder) {
            return android.service.notification.INotificationListener.Stub.asInterface(binder);
        }

        @Override // com.android.server.notification.ManagedServices
        protected boolean checkType(android.os.IInterface service) {
            return service instanceof android.service.notification.INotificationListener;
        }

        @Override // com.android.server.notification.ManagedServices
        protected void onServiceAdded(com.android.server.notification.ManagedServices.ManagedServiceInfo info) {
            com.android.server.notification.NotificationManagerService.this.mListeners.registerGuestService(info);
        }

        @Override // com.android.server.notification.ManagedServices
        protected void ensureFilters(android.content.pm.ServiceInfo si, int userId) {
        }

        @Override // com.android.server.notification.ManagedServices
        protected void onServiceRemovedLocked(com.android.server.notification.ManagedServices.ManagedServiceInfo removed) {
            com.android.server.notification.NotificationManagerService.this.mListeners.unregisterService(removed.service, removed.userid);
        }

        @Override // com.android.server.notification.ManagedServices
        public void onUserUnlocked(int user) {
            if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().isLoggable()) {
                android.util.Slog.d(this.TAG, "onUserUnlocked u=" + user);
            }
            rebindServices(true, user);
            if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && user == com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().getMultiAppUserId()) {
                android.util.Slog.d(this.TAG, "notification assistant : return for multi app");
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected boolean allowRebindForParentUser() {
            return false;
        }

        @Override // com.android.server.notification.ManagedServices
        protected java.lang.String getRequiredPermission() {
            return "android.permission.REQUEST_NOTIFICATION_ASSISTANT_SERVICE";
        }

        protected java.util.List<java.lang.String> getAllowedAssistantAdjustments() {
            java.util.List<java.lang.String> types;
            synchronized (this.mLock) {
                types = new java.util.ArrayList<>();
                types.addAll(this.mAllowedAdjustments);
            }
            return types;
        }

        protected boolean isAdjustmentAllowed(java.lang.String type) {
            boolean zContains;
            synchronized (this.mLock) {
                zContains = this.mAllowedAdjustments.contains(type);
            }
            return zContains;
        }

        protected void onNotificationsSeenLocked(java.util.ArrayList<com.android.server.notification.NotificationRecord> records) {
            for (final com.android.server.notification.ManagedServices.ManagedServiceInfo info : getServices()) {
                final java.util.ArrayList<java.lang.String> keys = new java.util.ArrayList<>(records.size());
                for (com.android.server.notification.NotificationRecord r : records) {
                    boolean sbnVisible = com.android.server.notification.NotificationManagerService.this.isVisibleToListener(r.getSbn(), r.getNotificationType(), info) && info.isSameUser(r.getUserId());
                    if (sbnVisible) {
                        keys.add(r.getKey());
                    }
                }
                if (!keys.isEmpty()) {
                    com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda9
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onNotificationsSeenLocked$0(info, keys);
                        }
                    });
                }
            }
        }

        protected void onPanelRevealed(final int items) {
            for (final com.android.server.notification.ManagedServices.ManagedServiceInfo info : getServices()) {
                com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onPanelRevealed$1(info, items);
                    }
                });
            }
            com.android.server.notification.NotificationManagerService.this.mUIFirstManagerExt.onPanelRevealed(items);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPanelRevealed$1(com.android.server.notification.ManagedServices.ManagedServiceInfo info, int items) {
            android.service.notification.INotificationListener assistant = info.service;
            try {
                assistant.onPanelRevealed(items);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify assistant (panel revealed): " + info, ex);
            }
        }

        protected void onPanelHidden() {
            for (final com.android.server.notification.ManagedServices.ManagedServiceInfo info : getServices()) {
                com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onPanelHidden$2(info);
                    }
                });
            }
            com.android.server.notification.NotificationManagerService.this.mUIFirstManagerExt.onPanelHidden();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPanelHidden$2(com.android.server.notification.ManagedServices.ManagedServiceInfo info) {
            android.service.notification.INotificationListener assistant = info.service;
            try {
                assistant.onPanelHidden();
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify assistant (panel hidden): " + info, ex);
            }
        }

        boolean hasUserSet(int userId) {
            java.lang.Boolean userSet = this.mIsUserChanged.get(java.lang.Integer.valueOf(userId));
            return userSet != null && userSet.booleanValue();
        }

        void setUserSet(int userId, boolean set) {
            this.mIsUserChanged.put(java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(set));
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: notifySeen, reason: merged with bridge method [inline-methods] */
        public void lambda$onNotificationsSeenLocked$0(com.android.server.notification.ManagedServices.ManagedServiceInfo info, java.util.ArrayList<java.lang.String> keys) {
            android.service.notification.INotificationListener assistant = info.service;
            try {
                assistant.onNotificationsSeen(keys);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify assistant (seen): " + info, ex);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onNotificationEnqueuedLocked(com.android.server.notification.NotificationRecord r) {
            boolean debug = isVerboseLogEnabled();
            if (debug) {
                android.util.Slog.v(this.TAG, "onNotificationEnqueuedLocked() called with: r = [" + r + "]");
            }
            android.service.notification.StatusBarNotification sbn = r.getSbn();
            for (com.android.server.notification.ManagedServices.ManagedServiceInfo info : getServices()) {
                boolean sbnVisible = com.android.server.notification.NotificationManagerService.this.isVisibleToListener(sbn, r.getNotificationType(), info) && info.isSameUser(r.getUserId());
                if (sbnVisible) {
                    com.android.server.notification.NotificationManagerService.TrimCache trimCache = com.android.server.notification.NotificationManagerService.this.new TrimCache(sbn);
                    android.service.notification.INotificationListener assistant = info.service;
                    android.service.notification.StatusBarNotification sbnToPost = trimCache.ForListener(info);
                    com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder sbnHolder = new com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder(sbnToPost);
                    if (debug) {
                        try {
                            android.util.Slog.v(this.TAG, "calling onNotificationEnqueuedWithChannel " + sbnHolder);
                        } catch (android.os.RemoteException ex) {
                            android.util.Slog.e(this.TAG, "unable to notify assistant (enqueued): " + assistant, ex);
                            if ((ex instanceof android.os.TransactionTooLargeException) && com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                                com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().onClearAllNotifications(com.android.server.notification.NotificationManagerService.MY_UID, com.android.server.notification.NotificationManagerService.MY_PID, -1);
                            }
                        }
                    }
                    android.service.notification.NotificationRankingUpdate update = com.android.server.notification.NotificationManagerService.this.makeRankingUpdateLocked(info);
                    assistant.onNotificationEnqueuedWithChannel(sbnHolder, r.getChannel(), update);
                }
            }
        }

        void notifyAssistantVisibilityChangedLocked(com.android.server.notification.NotificationRecord r, final boolean isVisible) {
            final java.lang.String key = r.getSbn().getKey();
            if (com.android.server.notification.NotificationManagerService.DBG) {
                android.util.Slog.d(this.TAG, "notifyAssistantVisibilityChangedLocked: " + key);
            }
            notifyAssistantLocked(r.getSbn(), r.getNotificationType(), true, new java.util.function.BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda5
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$notifyAssistantVisibilityChangedLocked$3(key, isVisible, (android.service.notification.INotificationListener) obj, (com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantVisibilityChangedLocked$3(java.lang.String key, boolean isVisible, android.service.notification.INotificationListener assistant, com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder sbnHolder) {
            try {
                assistant.onNotificationVisibilityChanged(key, isVisible);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify assistant (visible): " + assistant, ex);
            }
        }

        void notifyAssistantExpansionChangedLocked(android.service.notification.StatusBarNotification sbn, int notificationType, final boolean isUserAction, final boolean isExpanded) {
            final java.lang.String key = sbn.getKey();
            notifyAssistantLocked(sbn, notificationType, true, new java.util.function.BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda3
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$notifyAssistantExpansionChangedLocked$4(key, isUserAction, isExpanded, (android.service.notification.INotificationListener) obj, (com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantExpansionChangedLocked$4(java.lang.String key, boolean isUserAction, boolean isExpanded, android.service.notification.INotificationListener assistant, com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder sbnHolder) {
            try {
                assistant.onNotificationExpansionChanged(key, isUserAction, isExpanded);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify assistant (expanded): " + assistant, ex);
            }
        }

        void notifyAssistantNotificationDirectReplyLocked(com.android.server.notification.NotificationRecord r) {
            final java.lang.String key = r.getKey();
            notifyAssistantLocked(r.getSbn(), r.getNotificationType(), true, new java.util.function.BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda7
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$notifyAssistantNotificationDirectReplyLocked$5(key, (android.service.notification.INotificationListener) obj, (com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantNotificationDirectReplyLocked$5(java.lang.String key, android.service.notification.INotificationListener assistant, com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder sbnHolder) {
            try {
                assistant.onNotificationDirectReply(key);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify assistant (expanded): " + assistant, ex);
            }
        }

        void notifyAssistantSuggestedReplySent(android.service.notification.StatusBarNotification sbn, int notificationType, final java.lang.CharSequence reply, final boolean generatedByAssistant) {
            final java.lang.String key = sbn.getKey();
            notifyAssistantLocked(sbn, notificationType, true, new java.util.function.BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda2
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$notifyAssistantSuggestedReplySent$6(key, reply, generatedByAssistant, (android.service.notification.INotificationListener) obj, (com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantSuggestedReplySent$6(java.lang.String key, java.lang.CharSequence reply, boolean generatedByAssistant, android.service.notification.INotificationListener assistant, com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder sbnHolder) {
            int i;
            if (generatedByAssistant) {
                i = 1;
            } else {
                i = 0;
            }
            try {
                assistant.onSuggestedReplySent(key, reply, i);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify assistant (snoozed): " + assistant, ex);
            }
        }

        void notifyAssistantActionClicked(com.android.server.notification.NotificationRecord r, final android.app.Notification.Action action, final boolean generatedByAssistant) {
            final java.lang.String key = r.getSbn().getKey();
            notifyAssistantLocked(r.getSbn(), r.getNotificationType(), true, new java.util.function.BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda6
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$notifyAssistantActionClicked$7(key, action, generatedByAssistant, (android.service.notification.INotificationListener) obj, (com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantActionClicked$7(java.lang.String key, android.app.Notification.Action action, boolean generatedByAssistant, android.service.notification.INotificationListener assistant, com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder sbnHolder) {
            int i;
            if (generatedByAssistant) {
                i = 1;
            } else {
                i = 0;
            }
            try {
                assistant.onActionClicked(key, action, i);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify assistant (snoozed): " + assistant, ex);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyAssistantSnoozedLocked(com.android.server.notification.NotificationRecord r, final java.lang.String snoozeCriterionId) {
            notifyAssistantLocked(r.getSbn(), r.getNotificationType(), true, new java.util.function.BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda8
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$notifyAssistantSnoozedLocked$8(snoozeCriterionId, (android.service.notification.INotificationListener) obj, (com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantSnoozedLocked$8(java.lang.String snoozeCriterionId, android.service.notification.INotificationListener assistant, com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder sbnHolder) {
            try {
                assistant.onNotificationSnoozedUntilContext(sbnHolder, snoozeCriterionId);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify assistant (snoozed): " + assistant, ex);
            }
        }

        void notifyAssistantNotificationClicked(com.android.server.notification.NotificationRecord r) {
            final java.lang.String key = r.getSbn().getKey();
            notifyAssistantLocked(r.getSbn(), r.getNotificationType(), true, new java.util.function.BiConsumer() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$notifyAssistantNotificationClicked$9(key, (android.service.notification.INotificationListener) obj, (com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder) obj2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyAssistantNotificationClicked$9(java.lang.String key, android.service.notification.INotificationListener assistant, com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder sbnHolder) {
            try {
                assistant.onNotificationClicked(key);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify assistant (clicked): " + assistant, ex);
            }
        }

        void notifyAssistantFeedbackReceived(com.android.server.notification.NotificationRecord r, android.os.Bundle feedback) {
            android.service.notification.StatusBarNotification sbn = r.getSbn();
            for (com.android.server.notification.ManagedServices.ManagedServiceInfo info : getServices()) {
                boolean sbnVisible = com.android.server.notification.NotificationManagerService.this.isVisibleToListener(sbn, r.getNotificationType(), info) && info.isSameUser(r.getUserId());
                if (sbnVisible) {
                    android.service.notification.INotificationListener assistant = info.service;
                    try {
                        android.service.notification.NotificationRankingUpdate update = com.android.server.notification.NotificationManagerService.this.makeRankingUpdateLocked(info);
                        assistant.onNotificationFeedbackReceived(sbn.getKey(), update, feedback);
                    } catch (android.os.RemoteException ex) {
                        android.util.Slog.e(this.TAG, "unable to notify assistant (feedback): " + assistant, ex);
                    }
                }
            }
        }

        private void notifyAssistantLocked(android.service.notification.StatusBarNotification sbn, int notificationType, boolean sameUserOnly, final java.util.function.BiConsumer<android.service.notification.INotificationListener, com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder> callback) {
            com.android.server.notification.NotificationManagerService.TrimCache trimCache = com.android.server.notification.NotificationManagerService.this.new TrimCache(sbn);
            boolean debug = isVerboseLogEnabled();
            if (debug) {
                android.util.Slog.v(this.TAG, "notifyAssistantLocked() called with: sbn = [" + sbn + "], sameUserOnly = [" + sameUserOnly + "], callback = [" + callback + "]");
            }
            for (com.android.server.notification.ManagedServices.ManagedServiceInfo info : getServices()) {
                boolean sbnVisible = com.android.server.notification.NotificationManagerService.this.isVisibleToListener(sbn, notificationType, info) && (!sameUserOnly || info.isSameUser(sbn.getUserId()));
                if (debug) {
                    android.util.Slog.v(this.TAG, "notifyAssistantLocked info=" + info + " snbVisible=" + sbnVisible);
                }
                if (sbnVisible) {
                    final android.service.notification.INotificationListener assistant = info.service;
                    android.service.notification.StatusBarNotification sbnToPost = trimCache.ForListener(info);
                    final com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder sbnHolder = new com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder(sbnToPost);
                    com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationAssistants$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            callback.accept(assistant, sbnHolder);
                        }
                    });
                }
            }
        }

        public boolean isEnabled() {
            return !getServices().isEmpty();
        }

        protected void resetDefaultAssistantsIfNecessary() {
            java.util.List<android.content.pm.UserInfo> activeUsers = this.mUm.getAliveUsers();
            for (android.content.pm.UserInfo userInfo : activeUsers) {
                int userId = userInfo.getUserHandle().getIdentifier();
                if (!hasUserSet(userId)) {
                    if (!com.android.server.notification.NotificationManagerService.this.isNASMigrationDone(userId)) {
                        resetDefaultFromConfig();
                        com.android.server.notification.NotificationManagerService.this.setNASMigrationDone(userId);
                    }
                    android.util.Slog.d(this.TAG, "Approving default notification assistant for user " + userId);
                    com.android.server.notification.NotificationManagerService.this.setDefaultAssistantForUser(userId);
                }
            }
        }

        protected void resetDefaultFromConfig() {
            clearDefaults();
            loadDefaultsFromConfig();
        }

        protected void clearDefaults() {
            this.mDefaultComponents.clear();
            this.mDefaultPackages.clear();
        }

        @Override // com.android.server.notification.ManagedServices
        protected void setPackageOrComponentEnabled(java.lang.String pkgOrComponent, int userId, boolean isPrimary, boolean enabled, boolean userSet) {
            if (enabled) {
                java.util.List<android.content.ComponentName> allowedComponents = getAllowedComponents(userId);
                if (!allowedComponents.isEmpty()) {
                    android.content.ComponentName currentComponent = (android.content.ComponentName) com.android.internal.util.CollectionUtils.firstOrNull(allowedComponents);
                    if (currentComponent.flattenToString().equals(pkgOrComponent)) {
                        return;
                    } else {
                        com.android.server.notification.NotificationManagerService.this.setNotificationAssistantAccessGrantedForUserInternal(currentComponent, userId, false, userSet);
                    }
                }
            }
            super.setPackageOrComponentEnabled(pkgOrComponent, userId, isPrimary, enabled, userSet);
        }

        private boolean isVerboseLogEnabled() {
            return android.util.Log.isLoggable("notification_assistant", 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListenersPostedAndLogLocked(com.android.server.notification.NotificationRecord r, com.android.server.notification.NotificationRecord old, final com.android.server.notification.NotificationManagerService.PostNotificationTracker tracker, final com.android.server.notification.NotificationRecordLogger.NotificationReported report) {
        final java.util.List<java.lang.Runnable> listenerCalls = this.mListeners.prepareNotifyPostedLocked(r, old, true);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyListenersPostedAndLogLocked$10(listenerCalls, tracker, report);
            }
        });
        if (com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.callstyleCallbackApi()) {
            notifyCallNotificationEventListenerOnPosted(r);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyListenersPostedAndLogLocked$10(java.util.List listenerCalls, com.android.server.notification.NotificationManagerService.PostNotificationTracker tracker, com.android.server.notification.NotificationRecordLogger.NotificationReported report) {
        java.util.Iterator it = listenerCalls.iterator();
        while (it.hasNext()) {
            java.lang.Runnable listenerCall = (java.lang.Runnable) it.next();
            listenerCall.run();
        }
        long postDurationMillis = tracker.finish();
        if (report != null) {
            report.post_duration_millis = postDurationMillis;
            this.mNotificationRecordLogger.logNotificationPosted(report);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeNotifySystemUiListenerLifetimeExtendedListLocked(java.util.List<com.android.server.notification.NotificationRecord> notificationList, int packageImportance) {
        for (int i = notificationList.size() - 1; i >= 0; i--) {
            com.android.server.notification.NotificationRecord record = notificationList.get(i);
            maybeNotifySystemUiListenerLifetimeExtendedLocked(record, record.getSbn().getPackageName(), packageImportance);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeNotifySystemUiListenerLifetimeExtendedLocked(com.android.server.notification.NotificationRecord record, java.lang.String pkg, int packageImportance) {
        if (record != null && (record.getSbn().getNotification().flags & 65536) > 0) {
            boolean isAppForeground = pkg != null && packageImportance == 100;
            record.setPostSilently(true);
            record.getNotification().flags |= 8;
            this.mHandler.post(new com.android.server.notification.NotificationManagerService.EnqueueNotificationRunnable(record.getUser().getIdentifier(), record, isAppForeground, this.mPostNotificationTrackerFactory.newTracker(null)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPackageImportanceWithIdentity(java.lang.String pkg) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            int packageImportance = this.mActivityManager.getPackageImportance(pkg);
            return packageImportance;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public class NotificationListeners extends com.android.server.notification.ManagedServices {
        static final java.lang.String ATT_COMPONENT = "component";
        static final java.lang.String ATT_PKG = "pkg";
        static final java.lang.String ATT_TYPES = "types";
        static final java.lang.String ATT_UID = "uid";
        static final java.lang.String FLAG_SEPARATOR = "\\|";
        static final java.lang.String TAG_APPROVED = "allowed";
        static final java.lang.String TAG_DISALLOWED = "disallowed";
        static final java.lang.String TAG_ENABLED_NOTIFICATION_LISTENERS = "enabled_listeners";
        static final java.lang.String TAG_REQUESTED_LISTENER = "listener";
        static final java.lang.String TAG_REQUESTED_LISTENERS = "request_listeners";
        static final java.lang.String XML_SEPARATOR = ",";
        private final boolean mIsHeadlessSystemUserMode;
        private final android.util.ArraySet<com.android.server.notification.ManagedServices.ManagedServiceInfo> mLightTrimListeners;
        private final android.util.ArrayMap<android.util.Pair<android.content.ComponentName, java.lang.Integer>, android.service.notification.NotificationListenerFilter> mRequestedNotificationListeners;
        private final android.util.ArraySet<java.lang.Integer> mTrustedListenerUids;

        public NotificationListeners(com.android.server.notification.NotificationManagerService this$0, android.content.Context context, java.lang.Object lock, com.android.server.notification.ManagedServices.UserProfiles userProfiles, android.content.pm.IPackageManager pm) {
            this(context, lock, userProfiles, pm, android.os.UserManager.isHeadlessSystemUserMode());
        }

        public NotificationListeners(android.content.Context context, java.lang.Object lock, com.android.server.notification.ManagedServices.UserProfiles userProfiles, android.content.pm.IPackageManager pm, boolean isHeadlessSystemUserMode) {
            super(context, lock, userProfiles, pm);
            this.mLightTrimListeners = new android.util.ArraySet<>();
            this.mTrustedListenerUids = new android.util.ArraySet<>();
            this.mRequestedNotificationListeners = new android.util.ArrayMap<>();
            this.mIsHeadlessSystemUserMode = isHeadlessSystemUserMode;
        }

        @Override // com.android.server.notification.ManagedServices
        protected void setPackageOrComponentEnabled(java.lang.String pkgOrComponent, int userId, boolean isPrimary, boolean enabled, boolean userSet) {
            super.setPackageOrComponentEnabled(pkgOrComponent, userId, isPrimary, enabled, userSet);
            java.lang.String pkgName = getPackageName(pkgOrComponent);
            if (com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.redactSensitiveNotificationsFromUntrustedListeners()) {
                int uid = com.android.server.notification.NotificationManagerService.this.mPackageManagerInternal.getPackageUid(pkgName, 0L, userId);
                if (!enabled && uid >= 0) {
                    synchronized (this.mTrustedListenerUids) {
                        this.mTrustedListenerUids.remove(java.lang.Integer.valueOf(uid));
                    }
                }
                if (enabled && uid >= 0 && isAppTrustedNotificationListenerService(uid, pkgName)) {
                    synchronized (this.mTrustedListenerUids) {
                        this.mTrustedListenerUids.add(java.lang.Integer.valueOf(uid));
                    }
                }
            }
            this.mContext.sendBroadcastAsUser(new android.content.Intent("android.app.action.NOTIFICATION_LISTENER_ENABLED_CHANGED").addFlags(1073741824), android.os.UserHandle.of(userId), null);
        }

        @Override // com.android.server.notification.ManagedServices
        protected void loadDefaultsFromConfig() {
            java.lang.String defaultListenerAccess = this.mContext.getResources().getString(android.R.string.config_defaultProfcollectReportUploaderApp);
            if (defaultListenerAccess != null) {
                java.lang.String[] listeners = defaultListenerAccess.split(":");
                for (int i = 0; i < listeners.length; i++) {
                    if (!android.text.TextUtils.isEmpty(listeners[i])) {
                        int packageQueryFlags = com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED;
                        if (this.mIsHeadlessSystemUserMode) {
                            packageQueryFlags = com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED + 4194304;
                        }
                        android.util.ArraySet<android.content.ComponentName> approvedListeners = queryPackageForServices(listeners[i], packageQueryFlags, 0);
                        for (int k = 0; k < approvedListeners.size(); k++) {
                            android.content.ComponentName cn = approvedListeners.valueAt(k);
                            addDefaultComponentOrPackage(cn.flattenToString());
                        }
                    }
                }
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected int getBindFlags() {
            return 83886337;
        }

        @Override // com.android.server.notification.ManagedServices
        protected com.android.server.notification.ManagedServices.Config getConfig() {
            com.android.server.notification.ManagedServices.Config c = new com.android.server.notification.ManagedServices.Config();
            c.caption = "notification listener";
            c.serviceInterface = "android.service.notification.NotificationListenerService";
            c.xmlTag = TAG_ENABLED_NOTIFICATION_LISTENERS;
            c.secureSettingName = "enabled_notification_listeners";
            c.bindPermission = "android.permission.BIND_NOTIFICATION_LISTENER_SERVICE";
            c.settingsAction = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
            c.clientLabel = android.R.string.notification_channel_sim_high_prio;
            return c;
        }

        @Override // com.android.server.notification.ManagedServices
        protected android.os.IInterface asInterface(android.os.IBinder binder) {
            return android.service.notification.INotificationListener.Stub.asInterface(binder);
        }

        @Override // com.android.server.notification.ManagedServices
        protected boolean checkType(android.os.IInterface service) {
            return service instanceof android.service.notification.INotificationListener;
        }

        @Override // com.android.server.notification.ManagedServices
        public void onServiceAdded(com.android.server.notification.ManagedServices.ManagedServiceInfo info) {
            android.service.notification.NotificationRankingUpdate update;
            if (android.app.Flags.lifetimeExtensionRefactor()) {
                info.isSystemUi = !com.android.server.notification.NotificationManagerService.this.isCallerSystemOrPhone() && com.android.server.notification.NotificationManagerService.this.getContext().checkPermission("android.permission.STATUS_BAR_SERVICE", -1, info.uid) == 0;
            }
            android.service.notification.INotificationListener listener = info.service;
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                update = com.android.server.notification.NotificationManagerService.this.makeRankingUpdateLocked(info);
                updateUriPermissionsForActiveNotificationsLocked(info, true);
            }
            if (com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.redactSensitiveNotificationsFromUntrustedListeners() && isAppTrustedNotificationListenerService(info.uid, info.component.getPackageName())) {
                synchronized (this.mTrustedListenerUids) {
                    this.mTrustedListenerUids.add(java.lang.Integer.valueOf(info.uid));
                }
            }
            try {
                listener.onListenerConnected(update);
            } catch (android.os.RemoteException e) {
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected void onServiceRemovedLocked(com.android.server.notification.ManagedServices.ManagedServiceInfo removed) {
            updateUriPermissionsForActiveNotificationsLocked(removed, false);
            if (com.android.server.notification.NotificationManagerService.this.removeDisabledHints(removed)) {
                com.android.server.notification.NotificationManagerService.this.updateListenerHintsLocked();
                com.android.server.notification.NotificationManagerService.this.updateEffectsSuppressorLocked();
            }
            if (com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.redactSensitiveNotificationsFromUntrustedListeners()) {
                synchronized (this.mTrustedListenerUids) {
                    this.mTrustedListenerUids.remove(java.lang.Integer.valueOf(removed.uid));
                }
            }
            this.mLightTrimListeners.remove(removed);
        }

        @Override // com.android.server.notification.ManagedServices
        public void onUserRemoved(int user) {
            super.onUserRemoved(user);
            synchronized (this.mRequestedNotificationListeners) {
                for (int i = this.mRequestedNotificationListeners.size() - 1; i >= 0; i--) {
                    if (((java.lang.Integer) this.mRequestedNotificationListeners.keyAt(i).second).intValue() == user) {
                        this.mRequestedNotificationListeners.removeAt(i);
                    }
                }
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected boolean allowRebindForParentUser() {
            return true;
        }

        @Override // com.android.server.notification.ManagedServices
        public void onPackagesChanged(boolean removingPackage, java.lang.String[] pkgList, int[] uidList) {
            super.onPackagesChanged(removingPackage, pkgList, uidList);
            synchronized (this.mRequestedNotificationListeners) {
                if (removingPackage) {
                    for (int i = 0; i < pkgList.length; i++) {
                        java.lang.String pkg = pkgList[i];
                        int userId = android.os.UserHandle.getUserId(uidList[i]);
                        for (int j = this.mRequestedNotificationListeners.size() - 1; j >= 0; j--) {
                            android.util.Pair<android.content.ComponentName, java.lang.Integer> key = this.mRequestedNotificationListeners.keyAt(j);
                            if (((java.lang.Integer) key.second).intValue() == userId && ((android.content.ComponentName) key.first).getPackageName().equals(pkg)) {
                                this.mRequestedNotificationListeners.removeAt(j);
                            }
                        }
                    }
                    for (int i2 = 0; i2 < pkgList.length; i2++) {
                        java.lang.String pkg2 = pkgList[i2];
                        for (int j2 = this.mRequestedNotificationListeners.size() - 1; j2 >= 0; j2--) {
                            android.service.notification.NotificationListenerFilter nlf = this.mRequestedNotificationListeners.valueAt(j2);
                            android.content.pm.VersionedPackage ai = new android.content.pm.VersionedPackage(pkg2, uidList[i2]);
                            nlf.removePackage(ai);
                        }
                    }
                }
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected java.lang.String getRequiredPermission() {
            return null;
        }

        @Override // com.android.server.notification.ManagedServices
        protected boolean shouldReflectToSettings() {
            return true;
        }

        @Override // com.android.server.notification.ManagedServices
        protected void readExtraTag(java.lang.String tag, com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            if (TAG_REQUESTED_LISTENERS.equals(tag)) {
                int listenersOuterDepth = parser.getDepth();
                while (com.android.internal.util.XmlUtils.nextElementWithin(parser, listenersOuterDepth)) {
                    if (TAG_REQUESTED_LISTENER.equals(parser.getName())) {
                        int userId = com.android.internal.util.XmlUtils.readIntAttribute(parser, "user");
                        android.content.ComponentName cn = android.content.ComponentName.unflattenFromString(com.android.internal.util.XmlUtils.readStringAttribute(parser, ATT_COMPONENT));
                        int approved = 15;
                        android.util.ArraySet<android.content.pm.VersionedPackage> disallowedPkgs = new android.util.ArraySet<>();
                        int listenerOuterDepth = parser.getDepth();
                        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, listenerOuterDepth)) {
                            if (TAG_APPROVED.equals(parser.getName())) {
                                approved = com.android.internal.util.XmlUtils.readIntAttribute(parser, ATT_TYPES);
                            } else if (TAG_DISALLOWED.equals(parser.getName())) {
                                java.lang.String pkg = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATT_PKG);
                                int uid = com.android.internal.util.XmlUtils.readIntAttribute(parser, "uid");
                                if (!android.text.TextUtils.isEmpty(pkg)) {
                                    android.content.pm.VersionedPackage ai = new android.content.pm.VersionedPackage(pkg, uid);
                                    disallowedPkgs.add(ai);
                                }
                            }
                        }
                        android.service.notification.NotificationListenerFilter nlf = new android.service.notification.NotificationListenerFilter(approved, disallowedPkgs);
                        synchronized (this.mRequestedNotificationListeners) {
                            this.mRequestedNotificationListeners.put(android.util.Pair.create(cn, java.lang.Integer.valueOf(userId)), nlf);
                        }
                    }
                }
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected void writeExtraXmlTags(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
            out.startTag((java.lang.String) null, TAG_REQUESTED_LISTENERS);
            synchronized (this.mRequestedNotificationListeners) {
                for (android.util.Pair<android.content.ComponentName, java.lang.Integer> listener : this.mRequestedNotificationListeners.keySet()) {
                    android.service.notification.NotificationListenerFilter nlf = this.mRequestedNotificationListeners.get(listener);
                    out.startTag((java.lang.String) null, TAG_REQUESTED_LISTENER);
                    com.android.internal.util.XmlUtils.writeStringAttribute(out, ATT_COMPONENT, ((android.content.ComponentName) listener.first).flattenToString());
                    com.android.internal.util.XmlUtils.writeIntAttribute(out, "user", ((java.lang.Integer) listener.second).intValue());
                    out.startTag((java.lang.String) null, TAG_APPROVED);
                    com.android.internal.util.XmlUtils.writeIntAttribute(out, ATT_TYPES, nlf.getTypes());
                    out.endTag((java.lang.String) null, TAG_APPROVED);
                    for (android.content.pm.VersionedPackage ai : nlf.getDisallowedPackages()) {
                        if (!android.text.TextUtils.isEmpty(ai.getPackageName())) {
                            out.startTag((java.lang.String) null, TAG_DISALLOWED);
                            com.android.internal.util.XmlUtils.writeStringAttribute(out, ATT_PKG, ai.getPackageName());
                            com.android.internal.util.XmlUtils.writeIntAttribute(out, "uid", ai.getVersionCode());
                            out.endTag((java.lang.String) null, TAG_DISALLOWED);
                        }
                    }
                    out.endTag((java.lang.String) null, TAG_REQUESTED_LISTENER);
                }
            }
            out.endTag((java.lang.String) null, TAG_REQUESTED_LISTENERS);
        }

        protected android.service.notification.NotificationListenerFilter getNotificationListenerFilter(android.util.Pair<android.content.ComponentName, java.lang.Integer> pair) {
            android.service.notification.NotificationListenerFilter notificationListenerFilter;
            synchronized (this.mRequestedNotificationListeners) {
                notificationListenerFilter = this.mRequestedNotificationListeners.get(pair);
            }
            return notificationListenerFilter;
        }

        protected void setNotificationListenerFilter(android.util.Pair<android.content.ComponentName, java.lang.Integer> pair, android.service.notification.NotificationListenerFilter nlf) {
            synchronized (this.mRequestedNotificationListeners) {
                this.mRequestedNotificationListeners.put(pair, nlf);
            }
        }

        @Override // com.android.server.notification.ManagedServices
        protected void ensureFilters(android.content.pm.ServiceInfo si, int userId) {
            int neverBridge;
            java.lang.String typeList;
            android.util.Pair<android.content.ComponentName, java.lang.Integer> listener = android.util.Pair.create(si.getComponentName(), java.lang.Integer.valueOf(userId));
            synchronized (this.mRequestedNotificationListeners) {
                android.service.notification.NotificationListenerFilter existingNlf = this.mRequestedNotificationListeners.get(listener);
                if (si.metaData != null) {
                    if (existingNlf == null && si.metaData.containsKey("android.service.notification.default_filter_types") && (typeList = si.metaData.get("android.service.notification.default_filter_types").toString()) != null) {
                        int types = getTypesFromStringList(typeList);
                        this.mRequestedNotificationListeners.put(listener, new android.service.notification.NotificationListenerFilter(types, new android.util.ArraySet()));
                    }
                    if (si.metaData.containsKey("android.service.notification.disabled_filter_types") && (neverBridge = getTypesFromStringList(si.metaData.get("android.service.notification.disabled_filter_types").toString())) != 0) {
                        android.service.notification.NotificationListenerFilter nlf = this.mRequestedNotificationListeners.getOrDefault(listener, new android.service.notification.NotificationListenerFilter());
                        nlf.setTypes(nlf.getTypes() & (~neverBridge));
                        this.mRequestedNotificationListeners.put(listener, nlf);
                    }
                }
            }
        }

        private int getTypesFromStringList(java.lang.String typeList) {
            int types = 0;
            if (typeList != null) {
                java.lang.String[] typeStrings = typeList.split(FLAG_SEPARATOR);
                for (java.lang.String typeString : typeStrings) {
                    if (!android.text.TextUtils.isEmpty(typeString)) {
                        if (typeString.equalsIgnoreCase("ONGOING")) {
                            types |= 8;
                        } else if (typeString.equalsIgnoreCase("CONVERSATIONS")) {
                            types |= 1;
                        } else if (typeString.equalsIgnoreCase("SILENT")) {
                            types |= 4;
                        } else if (typeString.equalsIgnoreCase("ALERTING")) {
                            types |= 2;
                        } else {
                            try {
                                types |= java.lang.Integer.parseInt(typeString);
                            } catch (java.lang.NumberFormatException e) {
                            }
                        }
                    }
                }
            }
            return types;
        }

        public void setOnNotificationPostedTrimLocked(com.android.server.notification.ManagedServices.ManagedServiceInfo info, int trim) {
            if (trim == 1) {
                this.mLightTrimListeners.add(info);
            } else {
                this.mLightTrimListeners.remove(info);
            }
        }

        public int getOnNotificationPostedTrim(com.android.server.notification.ManagedServices.ManagedServiceInfo managedServiceInfo) {
            return this.mLightTrimListeners.contains(managedServiceInfo) ? 1 : 0;
        }

        public void onStatusBarIconsBehaviorChanged(final boolean hideSilentStatusIcons) {
            for (final com.android.server.notification.ManagedServices.ManagedServiceInfo info : getServices()) {
                com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onStatusBarIconsBehaviorChanged$0(info, hideSilentStatusIcons);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStatusBarIconsBehaviorChanged$0(com.android.server.notification.ManagedServices.ManagedServiceInfo info, boolean hideSilentStatusIcons) {
            android.service.notification.INotificationListener listener = info.service;
            try {
                listener.onStatusBarIconsBehaviorChanged(hideSilentStatusIcons);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify listener (hideSilentStatusIcons): " + info, ex);
            }
        }

        void notifyPostedLocked(com.android.server.notification.NotificationRecord r, com.android.server.notification.NotificationRecord old) {
            notifyPostedLocked(r, old, true);
        }

        private void notifyPostedLocked(com.android.server.notification.NotificationRecord r, com.android.server.notification.NotificationRecord old, boolean notifyAllListeners) {
            for (java.lang.Runnable listenerCall : prepareNotifyPostedLocked(r, old, notifyAllListeners)) {
                com.android.server.notification.NotificationManagerService.this.mHandler.post(listenerCall);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:58:0x00f8, code lost:
        
            r7 = r31.this$0.makeRankingUpdateLocked(r12);
            r0.add(new com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda8(r31, r12, r15, r7));
         */
        /* JADX WARN: Removed duplicated region for block: B:44:0x00bd  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x0159  */
        /* JADX WARN: Removed duplicated region for block: B:78:0x015a A[Catch: Exception -> 0x01d5, TryCatch #0 {Exception -> 0x01d5, blocks: (B:7:0x0020, B:9:0x0026, B:11:0x002c, B:12:0x0046, B:14:0x004c, B:20:0x0068, B:26:0x0077, B:28:0x0085, B:37:0x00a1, B:39:0x00ab, B:42:0x00b7, B:46:0x00c1, B:49:0x00d3, B:51:0x00dc, B:54:0x00e4, B:56:0x00ea, B:58:0x00f8, B:50:0x00d8, B:62:0x0112, B:65:0x0120, B:70:0x012e, B:72:0x0135, B:74:0x013e, B:73:0x013a, B:75:0x0152, B:79:0x015e, B:78:0x015a), top: B:99:0x0020 }] */
        /* JADX WARN: Removed duplicated region for block: B:85:0x01a1  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x01a5 A[Catch: Exception -> 0x01c4, TryCatch #1 {Exception -> 0x01c4, blocks: (B:81:0x016e, B:84:0x0190, B:87:0x01a5, B:89:0x01ae, B:88:0x01aa), top: B:101:0x016e }] */
        /* JADX WARN: Removed duplicated region for block: B:88:0x01aa A[Catch: Exception -> 0x01c4, TryCatch #1 {Exception -> 0x01c4, blocks: (B:81:0x016e, B:84:0x0190, B:87:0x01a5, B:89:0x01ae, B:88:0x01aa), top: B:101:0x016e }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        java.util.List<java.lang.Runnable> prepareNotifyPostedLocked(com.android.server.notification.NotificationRecord r32, com.android.server.notification.NotificationRecord r33, boolean r34) {
            /*
                Method dump skipped, instruction units count: 501
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationManagerService.NotificationListeners.prepareNotifyPostedLocked(com.android.server.notification.NotificationRecord, com.android.server.notification.NotificationRecord, boolean):java.util.List");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$prepareNotifyPostedLocked$2(com.android.server.notification.ManagedServices.ManagedServiceInfo info, android.service.notification.StatusBarNotification oldSbnLightClone, android.service.notification.NotificationRankingUpdate update) {
            lambda$notifyRemovedLocked$4(info, oldSbnLightClone, update, null, 6);
        }

        boolean isAppTrustedNotificationListenerService(int uid, java.lang.String pkg) {
            if (!com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.redactSensitiveNotificationsFromUntrustedListeners()) {
                return true;
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                try {
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(this.TAG, "Failed to check trusted status of listener", e);
                }
                if (com.android.server.notification.NotificationManagerService.this.mPackageManager.checkUidPermission("android.permission.RECEIVE_SENSITIVE_NOTIFICATIONS", uid) != 0 && !com.android.server.notification.NotificationManagerService.this.mPackageManagerInternal.isPlatformSigned(pkg) && com.android.server.notification.NotificationManagerService.this.mAppOps.noteOpNoThrow(148, uid, pkg, (java.lang.String) null, (java.lang.String) null) != 0) {
                    java.util.List<android.companion.AssociationInfo> cdmAssocs = new java.util.ArrayList<>();
                    if (com.android.server.notification.NotificationManagerService.this.mCompanionManager == null) {
                        com.android.server.notification.NotificationManagerService.this.mCompanionManager = com.android.server.notification.NotificationManagerService.this.getCompanionManager();
                    }
                    if (com.android.server.notification.NotificationManagerService.this.mCompanionManager != null) {
                        cdmAssocs = com.android.server.notification.NotificationManagerService.this.mCompanionManager.getAllAssociationsForUser(android.os.UserHandle.getUserId(uid));
                    }
                    for (int i = 0; i < cdmAssocs.size(); i++) {
                        android.companion.AssociationInfo assocInfo = cdmAssocs.get(i);
                        if (!assocInfo.isRevoked() && pkg.equals(assocInfo.getPackageName()) && assocInfo.getUserId() == android.os.UserHandle.getUserId(uid)) {
                            return true;
                        }
                    }
                    android.os.Binder.restoreCallingIdentity(token);
                    return false;
                }
                return true;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        android.service.notification.StatusBarNotification redactStatusBarNotification(android.service.notification.StatusBarNotification sbn) {
            java.lang.String pkgLabel;
            if (!com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.redactSensitiveNotificationsFromUntrustedListeners()) {
                throw new java.lang.RuntimeException("redactStatusBarNotification called while flag is off");
            }
            android.content.pm.ApplicationInfo appInfo = (android.content.pm.ApplicationInfo) sbn.getNotification().extras.getParcelable("android.appInfo", android.content.pm.ApplicationInfo.class);
            if (appInfo != null) {
                pkgLabel = appInfo.loadLabel(com.android.server.notification.NotificationManagerService.this.mPackageManagerClient).toString();
            } else {
                java.lang.String pkgLabel2 = this.TAG;
                android.util.Slog.w(pkgLabel2, "StatusBarNotification " + sbn + " does not have ApplicationInfo. Did you pass in a 'cloneLight' notification?");
                pkgLabel = sbn.getPackageName();
            }
            java.lang.CharSequence redactedText = this.mContext.getString(android.R.string.private_profile_label_badge);
            android.app.Notification oldNotif = sbn.getNotification();
            android.app.Notification oldClone = new android.app.Notification();
            oldNotif.cloneInto(oldClone, false);
            android.app.Notification.Builder redactedNotifBuilder = new android.app.Notification.Builder(com.android.server.notification.NotificationManagerService.this.getContext(), oldClone);
            redactedNotifBuilder.setContentTitle(pkgLabel);
            redactedNotifBuilder.setContentText(redactedText);
            redactedNotifBuilder.setSubText(null);
            redactedNotifBuilder.setActions(new android.app.Notification.Action[0]);
            if (oldNotif.actions != null) {
                for (int i = 0; i < oldNotif.actions.length; i++) {
                    android.app.Notification.Action act = new android.app.Notification.Action.Builder(oldNotif.actions[i]).build();
                    act.title = this.mContext.getString(android.R.string.private_dns_broken_detailed);
                    redactedNotifBuilder.addAction(act);
                }
            }
            if (oldNotif.isStyle(android.app.Notification.MessagingStyle.class)) {
                android.app.Person empty = new android.app.Person.Builder().setName("").build();
                android.app.Notification.MessagingStyle messageStyle = new android.app.Notification.MessagingStyle(empty);
                messageStyle.addMessage(new android.app.Notification.MessagingStyle.Message(redactedText, java.lang.System.currentTimeMillis(), empty));
                redactedNotifBuilder.setStyle(messageStyle);
            }
            if (com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.redactSensitiveNotificationsBigTextStyle() && oldNotif.isStyle(android.app.Notification.BigTextStyle.class)) {
                android.app.Notification.BigTextStyle bigTextStyle = new android.app.Notification.BigTextStyle();
                bigTextStyle.bigText(this.mContext.getString(android.R.string.private_profile_label_badge));
                bigTextStyle.setBigContentTitle("");
                bigTextStyle.setSummaryText("");
                redactedNotifBuilder.setStyle(bigTextStyle);
            }
            android.app.Notification redacted = redactedNotifBuilder.build();
            if (redacted.extras.containsKey("android.title.big")) {
                redacted.extras.putString("android.title.big", pkgLabel);
            }
            redacted.extras.remove("android.subText");
            redacted.extras.remove("android.textLines");
            redacted.extras.remove("android.largeIcon.big");
            return sbn.cloneShallow(redacted);
        }

        boolean hasSensitiveContent(com.android.server.notification.NotificationRecord r) {
            if (r == null || !com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.redactSensitiveNotificationsFromUntrustedListeners()) {
                return false;
            }
            return r.hasSensitiveContent();
        }

        boolean isUidTrusted(int uid) {
            boolean z;
            synchronized (this.mTrustedListenerUids) {
                z = !com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.redactSensitiveNotificationsFromUntrustedListeners() || this.mTrustedListenerUids.contains(java.lang.Integer.valueOf(uid));
            }
            return z;
        }

        private void updateUriPermissionsForActiveNotificationsLocked(com.android.server.notification.ManagedServices.ManagedServiceInfo info, boolean grant) {
            try {
                for (com.android.server.notification.NotificationRecord r : com.android.server.notification.NotificationManagerService.this.mNotificationList) {
                    if (!grant || com.android.server.notification.NotificationManagerService.this.isVisibleToListener(r.getSbn(), r.getNotificationType(), info)) {
                        if (!r.isHidden() || info.targetSdkVersion >= 28) {
                            int targetUserId = info.userid == -1 ? 0 : info.userid;
                            if (grant) {
                                com.android.server.notification.NotificationManagerService.this.updateUriPermissions(r, null, info.component.getPackageName(), targetUserId);
                            } else {
                                com.android.server.notification.NotificationManagerService.this.updateUriPermissions(null, r, info.component.getPackageName(), targetUserId, true);
                            }
                        }
                    }
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e(this.TAG, "Could not " + (grant ? "grant" : "revoke") + " Uri permissions to " + info.component, e);
            }
        }

        public void notifyRemovedLocked(final com.android.server.notification.NotificationRecord r, final int reason, android.service.notification.NotificationStats notificationStats) {
            android.service.notification.StatusBarNotification redactedSbn;
            android.service.notification.StatusBarNotification sbn;
            int i = reason;
            if (com.android.server.notification.NotificationManagerService.this.isInLockDownMode(r.getUser().getIdentifier())) {
                return;
            }
            android.service.notification.StatusBarNotification sbn2 = r.getSbn();
            android.service.notification.StatusBarNotification sbnLight = sbn2.cloneLight();
            android.service.notification.StatusBarNotification redactedSbn2 = null;
            boolean hasSensitiveContent = hasSensitiveContent(r);
            for (final com.android.server.notification.ManagedServices.ManagedServiceInfo info : getServices()) {
                if (com.android.server.notification.NotificationManagerService.this.isVisibleToListener(sbn2, r.getNotificationType(), info) && (!r.isHidden() || i == 14 || info.targetSdkVersion >= 28)) {
                    if (i != 14 || info.targetSdkVersion < 28) {
                        boolean sendRedacted = com.android.internal.hidden_from_bootclasspath.android.service.notification.Flags.redactSensitiveNotificationsFromUntrustedListeners() && hasSensitiveContent && !isUidTrusted(info.uid);
                        if (sendRedacted && redactedSbn2 == null) {
                            android.service.notification.StatusBarNotification redactedSbn3 = redactStatusBarNotification(sbn2);
                            redactedSbn = redactedSbn3;
                        } else {
                            redactedSbn = redactedSbn2;
                        }
                        final android.service.notification.NotificationStats stats = com.android.server.notification.NotificationManagerService.this.mAssistants.isServiceTokenValidLocked(info.service) ? notificationStats : null;
                        final android.service.notification.StatusBarNotification sbnToSend = sendRedacted ? redactedSbn : sbnLight;
                        final android.service.notification.NotificationRankingUpdate update = com.android.server.notification.NotificationManagerService.this.makeRankingUpdateLocked(info);
                        if (com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null && com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().shouldDelayRemove(info, sbnToSend, r.getDelayRemoveReason())) {
                            android.util.Log.d(this.TAG, "notifyRemovedLocked: " + sbnToSend.getPackageName() + " shouldDelayRemove");
                            sbn = sbn2;
                        } else {
                            sbn = sbn2;
                            com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda5
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$notifyRemovedLocked$4(info, sbnToSend, update, stats, reason);
                                }
                            });
                        }
                        i = reason;
                        redactedSbn2 = redactedSbn;
                        sbn2 = sbn;
                    }
                }
            }
            com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyRemovedLocked$5(r);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyRemovedLocked$5(com.android.server.notification.NotificationRecord r) {
            com.android.server.notification.NotificationManagerService.this.updateUriPermissions(null, r, null, 0);
        }

        public void notifyRankingUpdateLocked(java.util.List<com.android.server.notification.NotificationRecord> changedHiddenNotifications) {
            boolean isHiddenRankingUpdate = changedHiddenNotifications != null && changedHiddenNotifications.size() > 0;
            for (final com.android.server.notification.ManagedServices.ManagedServiceInfo serviceInfo : getServices()) {
                if (serviceInfo.isEnabledForCurrentProfiles() && com.android.server.notification.NotificationManagerService.this.isInteractionVisibleToListener(serviceInfo, android.app.ActivityManager.getCurrentUser())) {
                    boolean notifyThisListener = false;
                    if (isHiddenRankingUpdate && serviceInfo.targetSdkVersion >= 28) {
                        java.util.Iterator<com.android.server.notification.NotificationRecord> it = changedHiddenNotifications.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            com.android.server.notification.NotificationRecord rec = it.next();
                            if (com.android.server.notification.NotificationManagerService.this.isVisibleToListener(rec.getSbn(), rec.getNotificationType(), serviceInfo)) {
                                notifyThisListener = true;
                                break;
                            }
                        }
                    }
                    if (notifyThisListener || !isHiddenRankingUpdate) {
                        final android.service.notification.NotificationRankingUpdate update = com.android.server.notification.NotificationManagerService.this.makeRankingUpdateLocked(serviceInfo);
                        com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$notifyRankingUpdateLocked$6(serviceInfo, update);
                            }
                        });
                    }
                }
            }
        }

        public void notifyListenerHintsChangedLocked(final int hints) {
            for (final com.android.server.notification.ManagedServices.ManagedServiceInfo serviceInfo : getServices()) {
                if (serviceInfo.isEnabledForCurrentProfiles() && com.android.server.notification.NotificationManagerService.this.isInteractionVisibleToListener(serviceInfo, android.app.ActivityManager.getCurrentUser())) {
                    com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$notifyListenerHintsChangedLocked$7(serviceInfo, hints);
                        }
                    });
                }
            }
        }

        public void notifyHiddenLocked(java.util.List<com.android.server.notification.NotificationRecord> changedNotifications) {
            if (changedNotifications == null || changedNotifications.size() == 0) {
                return;
            }
            notifyRankingUpdateLocked(changedNotifications);
            int numChangedNotifications = changedNotifications.size();
            for (int i = 0; i < numChangedNotifications; i++) {
                com.android.server.notification.NotificationRecord rec = changedNotifications.get(i);
                com.android.server.notification.NotificationManagerService.this.mListeners.notifyRemovedLocked(rec, 14, rec.getStats());
            }
        }

        public void notifyUnhiddenLocked(java.util.List<com.android.server.notification.NotificationRecord> changedNotifications) {
            if (changedNotifications == null || changedNotifications.size() == 0) {
                return;
            }
            notifyRankingUpdateLocked(changedNotifications);
            int numChangedNotifications = changedNotifications.size();
            for (int i = 0; i < numChangedNotifications; i++) {
                com.android.server.notification.NotificationRecord rec = changedNotifications.get(i);
                notifyPostedLocked(rec, rec, false);
            }
        }

        public void notifyInterruptionFilterChanged(final int interruptionFilter) {
            for (final com.android.server.notification.ManagedServices.ManagedServiceInfo serviceInfo : getServices()) {
                if (serviceInfo.isEnabledForCurrentProfiles() && com.android.server.notification.NotificationManagerService.this.isInteractionVisibleToListener(serviceInfo, android.app.ActivityManager.getCurrentUser())) {
                    com.android.server.notification.NotificationManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda7
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$notifyInterruptionFilterChanged$8(serviceInfo, interruptionFilter);
                        }
                    });
                }
            }
        }

        protected void notifyNotificationChannelChanged(final java.lang.String pkg, final android.os.UserHandle user, final android.app.NotificationChannel channel, final int modificationType) {
            if (channel == null) {
                return;
            }
            for (final com.android.server.notification.ManagedServices.ManagedServiceInfo info : getServices()) {
                if (info.enabledAndUserMatches(android.os.UserHandle.getCallingUserId()) && com.android.server.notification.NotificationManagerService.this.isInteractionVisibleToListener(info, android.os.UserHandle.getCallingUserId())) {
                    com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$notifyNotificationChannelChanged$9(info, pkg, user, channel, modificationType);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyNotificationChannelChanged$9(com.android.server.notification.ManagedServices.ManagedServiceInfo info, java.lang.String pkg, android.os.UserHandle user, android.app.NotificationChannel channel, int modificationType) {
            if (info.isSystem || com.android.server.notification.NotificationManagerService.this.hasCompanionDevice(info) || com.android.server.notification.NotificationManagerService.this.isServiceTokenValid(info.service)) {
                notifyNotificationChannelChanged(info, pkg, user, channel, modificationType);
            }
        }

        protected void notifyNotificationChannelGroupChanged(final java.lang.String pkg, final android.os.UserHandle user, final android.app.NotificationChannelGroup group, final int modificationType) {
            if (group == null) {
                return;
            }
            for (final com.android.server.notification.ManagedServices.ManagedServiceInfo info : getServices()) {
                if (info.enabledAndUserMatches(android.os.UserHandle.getCallingUserId()) && com.android.server.notification.NotificationManagerService.this.isInteractionVisibleToListener(info, android.os.UserHandle.getCallingUserId())) {
                    com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.notification.NotificationManagerService$NotificationListeners$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$notifyNotificationChannelGroupChanged$10(info, pkg, user, group, modificationType);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyNotificationChannelGroupChanged$10(com.android.server.notification.ManagedServices.ManagedServiceInfo info, java.lang.String pkg, android.os.UserHandle user, android.app.NotificationChannelGroup group, int modificationType) {
            if (info.isSystem() || com.android.server.notification.NotificationManagerService.this.hasCompanionDevice(info)) {
                notifyNotificationChannelGroupChanged(info, pkg, user, group, modificationType);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: notifyPosted, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
        public void lambda$prepareNotifyPostedLocked$3(com.android.server.notification.ManagedServices.ManagedServiceInfo info, android.service.notification.StatusBarNotification sbn, android.service.notification.NotificationRankingUpdate rankingUpdate) {
            android.service.notification.INotificationListener listener = info.service;
            com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder sbnHolder = new com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder(sbn);
            try {
                listener.onNotificationPosted(sbnHolder, rankingUpdate);
            } catch (android.os.DeadObjectException ex) {
                android.util.Slog.wtf(this.TAG, "unable to notify listener (posted): " + info, ex);
            } catch (android.os.RemoteException ex2) {
                android.util.Slog.e(this.TAG, "unable to notify listener (posted): " + info, ex2);
                if ((ex2 instanceof android.os.TransactionTooLargeException) && com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                    com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().onClearAllNotifications(com.android.server.notification.NotificationManagerService.MY_UID, com.android.server.notification.NotificationManagerService.MY_PID, -1);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: notifyRemoved, reason: merged with bridge method [inline-methods] */
        public void lambda$notifyRemovedLocked$4(com.android.server.notification.ManagedServices.ManagedServiceInfo info, android.service.notification.StatusBarNotification sbn, android.service.notification.NotificationRankingUpdate rankingUpdate, android.service.notification.NotificationStats stats, int reason) {
            android.service.notification.INotificationListener listener = info.service;
            com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder sbnHolder = new com.android.server.notification.NotificationManagerService.StatusBarNotificationHolder(sbn);
            try {
                if (!android.app.compat.CompatChanges.isChangeEnabled(com.android.server.notification.NotificationManagerService.NOTIFICATION_CANCELLATION_REASONS, info.uid) && (reason == 20 || reason == 21)) {
                    reason = 17;
                }
                if (!android.app.compat.CompatChanges.isChangeEnabled(com.android.server.notification.NotificationManagerService.NOTIFICATION_LOG_ASSISTANT_CANCEL, info.uid) && reason == 22) {
                    reason = 10;
                }
                listener.onNotificationRemoved(sbnHolder, rankingUpdate, stats, reason);
            } catch (android.os.DeadObjectException ex) {
                android.util.Slog.wtf(this.TAG, "unable to notify listener (removed): " + info, ex);
            } catch (android.os.RemoteException ex2) {
                android.util.Slog.e(this.TAG, "unable to notify listener (removed): " + info, ex2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: notifyRankingUpdate, reason: merged with bridge method [inline-methods] */
        public void lambda$notifyRankingUpdateLocked$6(com.android.server.notification.ManagedServices.ManagedServiceInfo info, android.service.notification.NotificationRankingUpdate rankingUpdate) {
            android.service.notification.INotificationListener listener = info.service;
            try {
                listener.onNotificationRankingUpdate(rankingUpdate);
            } catch (android.os.DeadObjectException ex) {
                android.util.Slog.wtf(this.TAG, "unable to notify listener (ranking update): " + info, ex);
            } catch (android.os.RemoteException ex2) {
                android.util.Slog.e(this.TAG, "unable to notify listener (ranking update): " + info, ex2);
                if ((ex2 instanceof android.os.TransactionTooLargeException) && com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt() != null) {
                    com.android.server.notification.NotificationManagerService.this.mNMSWrapper.getNMSExt().onClearAllNotifications(com.android.server.notification.NotificationManagerService.MY_UID, com.android.server.notification.NotificationManagerService.MY_PID, -1);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: notifyListenerHintsChanged, reason: merged with bridge method [inline-methods] */
        public void lambda$notifyListenerHintsChangedLocked$7(com.android.server.notification.ManagedServices.ManagedServiceInfo info, int hints) {
            android.service.notification.INotificationListener listener = info.service;
            try {
                listener.onListenerHintsChanged(hints);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify listener (listener hints): " + info, ex);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: notifyInterruptionFilterChanged, reason: merged with bridge method [inline-methods] */
        public void lambda$notifyInterruptionFilterChanged$8(com.android.server.notification.ManagedServices.ManagedServiceInfo info, int interruptionFilter) {
            android.service.notification.INotificationListener listener = info.service;
            try {
                listener.onInterruptionFilterChanged(interruptionFilter);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify listener (interruption filter): " + info, ex);
            }
        }

        void notifyNotificationChannelChanged(com.android.server.notification.ManagedServices.ManagedServiceInfo info, java.lang.String pkg, android.os.UserHandle user, android.app.NotificationChannel channel, int modificationType) {
            android.service.notification.INotificationListener listener = info.service;
            try {
                listener.onNotificationChannelModification(pkg, user, channel, modificationType);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify listener (channel changed): " + info, ex);
            }
        }

        private void notifyNotificationChannelGroupChanged(com.android.server.notification.ManagedServices.ManagedServiceInfo info, java.lang.String pkg, android.os.UserHandle user, android.app.NotificationChannelGroup group, int modificationType) {
            android.service.notification.INotificationListener listener = info.getService();
            try {
                listener.onNotificationChannelGroupModification(pkg, user, group, modificationType);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(this.TAG, "unable to notify listener (channel group changed): " + info, ex);
            }
        }

        public boolean isListenerPackage(java.lang.String packageName) {
            if (packageName == null) {
                return false;
            }
            synchronized (com.android.server.notification.NotificationManagerService.this.mNotificationLock) {
                for (com.android.server.notification.ManagedServices.ManagedServiceInfo serviceInfo : getServices()) {
                    if (packageName.equals(serviceInfo.component.getPackageName())) {
                        return true;
                    }
                }
                return false;
            }
        }

        boolean hasAllowedListener(java.lang.String packageName, int userId) {
            if (packageName == null) {
                return false;
            }
            java.util.List<android.content.ComponentName> allowedComponents = getAllowedComponents(userId);
            for (int i = 0; i < allowedComponents.size(); i++) {
                if (allowedComponents.get(i).getPackageName().equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

    private void broadcastToCallNotificationEventCallbacks(android.os.RemoteCallbackList<android.app.ICallNotificationEventCallback> callbackList, com.android.server.notification.NotificationRecord r, boolean isPosted) {
        if (callbackList != null) {
            int numCallbacks = callbackList.beginBroadcast();
            for (int i = 0; i < numCallbacks; i++) {
                if (isPosted) {
                    try {
                        callbackList.getBroadcastItem(i).onCallNotificationPosted(r.getSbn().getPackageName(), r.getUser());
                    } catch (android.os.RemoteException e) {
                        throw new java.lang.RuntimeException(e);
                    }
                } else {
                    callbackList.getBroadcastItem(i).onCallNotificationRemoved(r.getSbn().getPackageName(), r.getUser());
                }
            }
            callbackList.finishBroadcast();
        }
    }

    void notifyCallNotificationEventListenerOnPosted(com.android.server.notification.NotificationRecord r) {
        if (!r.getNotification().isStyle(android.app.Notification.CallStyle.class)) {
            return;
        }
        synchronized (this.mCallNotificationEventCallbacks) {
            android.util.ArrayMap<java.lang.Integer, android.os.RemoteCallbackList<android.app.ICallNotificationEventCallback>> callbacksForPackage = this.mCallNotificationEventCallbacks.get(r.getSbn().getPackageName());
            if (callbacksForPackage == null) {
                return;
            }
            if (!r.getUser().equals(android.os.UserHandle.ALL)) {
                broadcastToCallNotificationEventCallbacks(callbacksForPackage.get(java.lang.Integer.valueOf(r.getUser().getIdentifier())), r, true);
                broadcastToCallNotificationEventCallbacks(callbacksForPackage.get(-1), r, true);
            } else {
                for (android.os.RemoteCallbackList<android.app.ICallNotificationEventCallback> callbackList : callbacksForPackage.values()) {
                    broadcastToCallNotificationEventCallbacks(callbackList, r, true);
                }
            }
        }
    }

    void notifyCallNotificationEventListenerOnRemoved(com.android.server.notification.NotificationRecord r) {
        if (!r.getNotification().isStyle(android.app.Notification.CallStyle.class)) {
            return;
        }
        synchronized (this.mCallNotificationEventCallbacks) {
            android.util.ArrayMap<java.lang.Integer, android.os.RemoteCallbackList<android.app.ICallNotificationEventCallback>> callbacksForPackage = this.mCallNotificationEventCallbacks.get(r.getSbn().getPackageName());
            if (callbacksForPackage == null) {
                return;
            }
            if (!r.getUser().equals(android.os.UserHandle.ALL)) {
                broadcastToCallNotificationEventCallbacks(callbacksForPackage.get(java.lang.Integer.valueOf(r.getUser().getIdentifier())), r, false);
                broadcastToCallNotificationEventCallbacks(callbacksForPackage.get(-1), r, false);
            } else {
                for (android.os.RemoteCallbackList<android.app.ICallNotificationEventCallback> callbackList : callbacksForPackage.values()) {
                    broadcastToCallNotificationEventCallbacks(callbackList, r, false);
                }
            }
        }
    }

    class RoleObserver implements android.app.role.OnRoleHoldersChangedListener {
        private final java.util.concurrent.Executor mExecutor;
        private final android.os.Looper mMainLooper;
        private android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.Integer, android.util.ArraySet<java.lang.String>>> mNonBlockableDefaultApps;
        private final android.content.pm.IPackageManager mPm;
        private final android.app.role.RoleManager mRm;
        private volatile android.util.ArraySet<java.lang.Integer> mTrampolineExemptUids = new android.util.ArraySet<>();

        RoleObserver(android.content.Context context, android.app.role.RoleManager roleManager, android.content.pm.IPackageManager pkgMgr, android.os.Looper mainLooper) {
            this.mRm = roleManager;
            this.mPm = pkgMgr;
            this.mExecutor = context.getMainExecutor();
            this.mMainLooper = mainLooper;
        }

        public void init() {
            java.util.List<android.os.UserHandle> users = com.android.server.notification.NotificationManagerService.this.mUm.getUserHandles(true);
            this.mNonBlockableDefaultApps = new android.util.ArrayMap<>();
            for (int i = 0; i < com.android.server.notification.NotificationManagerService.NON_BLOCKABLE_DEFAULT_ROLES.length; i++) {
                android.util.ArrayMap<java.lang.Integer, android.util.ArraySet<java.lang.String>> userToApprovedList = new android.util.ArrayMap<>();
                this.mNonBlockableDefaultApps.put(com.android.server.notification.NotificationManagerService.NON_BLOCKABLE_DEFAULT_ROLES[i], userToApprovedList);
                for (int j = 0; j < users.size(); j++) {
                    java.lang.Integer userId = java.lang.Integer.valueOf(users.get(j).getIdentifier());
                    android.util.ArraySet<java.lang.String> approvedForUserId = new android.util.ArraySet<>(this.mRm.getRoleHoldersAsUser(com.android.server.notification.NotificationManagerService.NON_BLOCKABLE_DEFAULT_ROLES[i], android.os.UserHandle.of(userId.intValue())));
                    android.util.ArraySet<android.util.Pair<java.lang.String, java.lang.Integer>> approvedAppUids = new android.util.ArraySet<>();
                    for (java.lang.String pkg : approvedForUserId) {
                        approvedAppUids.add(new android.util.Pair<>(pkg, java.lang.Integer.valueOf(getUidForPackage(pkg, userId.intValue()))));
                    }
                    userToApprovedList.put(userId, approvedForUserId);
                    com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.updateDefaultApps(userId.intValue(), null, approvedAppUids);
                }
            }
            updateTrampolineExemptUidsForUsers((android.os.UserHandle[]) users.toArray(new android.os.UserHandle[0]));
            this.mRm.addOnRoleHoldersChangedListenerAsUser(this.mExecutor, this, android.os.UserHandle.ALL);
        }

        void destroy() {
            this.mRm.removeOnRoleHoldersChangedListenerAsUser(this, android.os.UserHandle.ALL);
        }

        public boolean isApprovedPackageForRoleForUser(java.lang.String role, java.lang.String pkg, int userId) {
            return this.mNonBlockableDefaultApps.get(role).get(java.lang.Integer.valueOf(userId)).contains(pkg);
        }

        public boolean isUidExemptFromTrampolineRestrictions(int uid) {
            return this.mTrampolineExemptUids.contains(java.lang.Integer.valueOf(uid));
        }

        public void onRoleHoldersChanged(java.lang.String roleName, android.os.UserHandle user) {
            onRoleHoldersChangedForNonBlockableDefaultApps(roleName, user);
            onRoleHoldersChangedForTrampolines(roleName, user);
        }

        private void onRoleHoldersChangedForNonBlockableDefaultApps(java.lang.String roleName, android.os.UserHandle user) {
            boolean relevantChange = false;
            int i = 0;
            while (true) {
                if (i >= com.android.server.notification.NotificationManagerService.NON_BLOCKABLE_DEFAULT_ROLES.length) {
                    break;
                }
                if (!com.android.server.notification.NotificationManagerService.NON_BLOCKABLE_DEFAULT_ROLES[i].equals(roleName)) {
                    i++;
                } else {
                    relevantChange = true;
                    break;
                }
            }
            if (!relevantChange) {
                return;
            }
            android.util.ArraySet<java.lang.String> roleHolders = new android.util.ArraySet<>(this.mRm.getRoleHoldersAsUser(roleName, user));
            android.util.ArrayMap<java.lang.Integer, android.util.ArraySet<java.lang.String>> prevApprovedForRole = this.mNonBlockableDefaultApps.getOrDefault(roleName, new android.util.ArrayMap<>());
            android.util.ArraySet<java.lang.String> previouslyApproved = prevApprovedForRole.getOrDefault(java.lang.Integer.valueOf(user.getIdentifier()), new android.util.ArraySet<>());
            android.util.ArraySet<java.lang.String> toRemove = new android.util.ArraySet<>();
            android.util.ArraySet<android.util.Pair<java.lang.String, java.lang.Integer>> toAdd = new android.util.ArraySet<>();
            for (java.lang.String previous : previouslyApproved) {
                if (!roleHolders.contains(previous)) {
                    toRemove.add(previous);
                }
            }
            for (java.lang.String nowApproved : roleHolders) {
                if (!previouslyApproved.contains(nowApproved)) {
                    toAdd.add(new android.util.Pair<>(nowApproved, java.lang.Integer.valueOf(getUidForPackage(nowApproved, user.getIdentifier()))));
                }
            }
            prevApprovedForRole.put(java.lang.Integer.valueOf(user.getIdentifier()), roleHolders);
            this.mNonBlockableDefaultApps.put(roleName, prevApprovedForRole);
            com.android.server.notification.NotificationManagerService.this.mPreferencesHelper.updateDefaultApps(user.getIdentifier(), toRemove, toAdd);
        }

        private void onRoleHoldersChangedForTrampolines(java.lang.String roleName, android.os.UserHandle user) {
            if (!"android.app.role.BROWSER".equals(roleName)) {
                return;
            }
            updateTrampolineExemptUidsForUsers(user);
        }

        private void updateTrampolineExemptUidsForUsers(android.os.UserHandle... users) {
            com.android.internal.util.Preconditions.checkState(this.mMainLooper.isCurrentThread());
            android.util.ArraySet<java.lang.Integer> oldUids = this.mTrampolineExemptUids;
            android.util.ArraySet<java.lang.Integer> newUids = new android.util.ArraySet<>();
            int n = oldUids.size();
            for (int i = 0; i < n; i++) {
                int uid = oldUids.valueAt(i).intValue();
                if (!com.android.internal.util.ArrayUtils.contains(users, android.os.UserHandle.of(android.os.UserHandle.getUserId(uid)))) {
                    newUids.add(java.lang.Integer.valueOf(uid));
                }
            }
            for (android.os.UserHandle user : users) {
                for (java.lang.String pkg : this.mRm.getRoleHoldersAsUser("android.app.role.BROWSER", user)) {
                    int uid2 = getUidForPackage(pkg, user.getIdentifier());
                    if (uid2 != -1) {
                        newUids.add(java.lang.Integer.valueOf(uid2));
                    } else {
                        android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "Bad uid (-1) for browser package " + pkg);
                    }
                }
            }
            this.mTrampolineExemptUids = newUids;
        }

        private int getUidForPackage(java.lang.String pkg, int userId) {
            try {
                return this.mPm.getPackageUid(pkg, 131072L, userId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, "role manager has bad default " + pkg + " " + userId);
                return -1;
            }
        }
    }

    public static final class DumpFilter {
        public java.lang.String pkgFilter;
        public boolean rvStats;
        public long since;
        public boolean stats;
        public boolean zen;
        public boolean filtered = false;
        public boolean redact = true;
        public boolean proto = false;
        public boolean criticalPriority = false;
        public boolean normalPriority = false;

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:50:0x00c2  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public static com.android.server.notification.NotificationManagerService.DumpFilter parseFromArguments(java.lang.String[] r8) {
            /*
                Method dump skipped, instruction units count: 268
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationManagerService.DumpFilter.parseFromArguments(java.lang.String[]):com.android.server.notification.NotificationManagerService$DumpFilter");
        }

        public boolean matches(android.service.notification.StatusBarNotification sbn) {
            if (this.filtered && !this.zen) {
                return sbn != null && (matches(sbn.getPackageName()) || matches(sbn.getOpPkg()));
            }
            return true;
        }

        public boolean matches(android.content.ComponentName component) {
            if (this.filtered && !this.zen) {
                return component != null && matches(component.getPackageName());
            }
            return true;
        }

        public boolean matches(java.lang.String pkg) {
            if (this.filtered && !this.zen) {
                return pkg != null && pkg.toLowerCase().contains(this.pkgFilter);
            }
            return true;
        }

        public java.lang.String toString() {
            return this.stats ? "stats" : this.zen ? "zen" : '\'' + this.pkgFilter + '\'';
        }
    }

    void resetAssistantUserSet(int userId) {
        checkCallerIsSystemOrShell();
        this.mAssistants.setUserSet(userId, false);
        handleSavePolicyFile();
    }

    android.content.ComponentName getApprovedAssistant(int userId) {
        checkCallerIsSystemOrShell();
        java.util.List<android.content.ComponentName> allowedComponents = this.mAssistants.getAllowedComponents(userId);
        return (android.content.ComponentName) com.android.internal.util.CollectionUtils.firstOrNull(allowedComponents);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class StatusBarNotificationHolder extends android.service.notification.IStatusBarNotificationHolder.Stub {
        private android.service.notification.StatusBarNotification mValue;

        public StatusBarNotificationHolder(android.service.notification.StatusBarNotification value) {
            this.mValue = value;
        }

        public android.service.notification.StatusBarNotification get() {
            android.service.notification.StatusBarNotification value = this.mValue;
            this.mValue = null;
            return value;
        }
    }

    private void writeSecureNotificationsPolicy(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
        out.startTag((java.lang.String) null, LOCKSCREEN_ALLOW_SECURE_NOTIFICATIONS_TAG);
        out.attributeBoolean((java.lang.String) null, LOCKSCREEN_ALLOW_SECURE_NOTIFICATIONS_VALUE, this.mLockScreenAllowSecureNotifications);
        out.endTag((java.lang.String) null, LOCKSCREEN_ALLOW_SECURE_NOTIFICATIONS_TAG);
    }

    protected android.app.Notification createReviewPermissionsNotification() {
        android.content.Intent tapIntent = new android.content.Intent("android.settings.ALL_APPS_NOTIFICATION_SETTINGS_FOR_REVIEW");
        if (this.mNMSWrapper.getNMSExt() != null) {
            android.content.Intent centerIntent = this.mNMSWrapper.getNMSExt().getNotificationCenterIntent();
            if (centerIntent != null) {
                tapIntent = centerIntent;
            } else {
                android.util.Slog.d(TAG, "center intent is null, we start the original notification setting activity instead.");
            }
        }
        android.content.Intent remindIntent = new android.content.Intent(REVIEW_NOTIF_ACTION_REMIND);
        android.content.Intent dismissIntent = new android.content.Intent(REVIEW_NOTIF_ACTION_DISMISS);
        android.content.Intent swipeIntent = new android.content.Intent(REVIEW_NOTIF_ACTION_CANCELED);
        android.app.Notification.Action remindMe = new android.app.Notification.Action.Builder((android.graphics.drawable.Icon) null, getContext().getResources().getString(android.R.string.relationTypeManager), android.app.PendingIntent.getBroadcast(getContext(), 0, remindIntent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD)).build();
        android.app.Notification.Action dismiss = new android.app.Notification.Action.Builder((android.graphics.drawable.Icon) null, getContext().getResources().getString(android.R.string.relationTypeFriend), android.app.PendingIntent.getBroadcast(getContext(), 0, dismissIntent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD)).build();
        return new android.app.Notification.Builder(getContext(), com.android.internal.notification.SystemNotificationChannels.SYSTEM_CHANGES).setSmallIcon(android.R.drawable.seekbar_thumb_pressed_to_unpressed_animation).setContentTitle(getContext().getResources().getString(android.R.string.relationTypeParent)).setContentText(getContext().getResources().getString(android.R.string.relationTypeMother)).setContentIntent(android.app.PendingIntent.getActivity(getContext(), 0, tapIntent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD)).setStyle(new android.app.Notification.BigTextStyle()).setFlag(32, true).setAutoCancel(true).addAction(remindMe).addAction(dismiss).setDeleteIntent(android.app.PendingIntent.getBroadcast(getContext(), 0, swipeIntent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD)).build();
    }

    protected void maybeShowInitialReviewPermissionsNotification() {
        if (!this.mShowReviewPermissionsNotification) {
            return;
        }
        int currentState = android.provider.Settings.Global.getInt(getContext().getContentResolver(), "review_permissions_notification_state", -1);
        if (currentState == 0 || currentState == 3) {
            android.app.NotificationManager nm = (android.app.NotificationManager) getContext().getSystemService(android.app.NotificationManager.class);
            nm.notify(TAG, 71, createReviewPermissionsNotification());
        }
    }

    boolean hasAnyNotification(java.lang.String pkg, int uid) {
        boolean z;
        synchronized (this.mNotificationLock) {
            z = hasAnyNotification(pkg, uid, this.mNotificationList) || hasAnyNotification(pkg, uid, this.mEnqueuedNotifications);
        }
        return z;
    }

    private boolean hasAnyNotification(java.lang.String pkg, int uid, java.util.ArrayList<com.android.server.notification.NotificationRecord> notificationList) {
        for (int i = notificationList.size() - 1; i >= 0; i--) {
            com.android.server.notification.NotificationRecord r = notificationList.get(i);
            if (pkg != null && r.getUid() == uid && r.getSbn().getPackageName().equals(pkg)) {
                return true;
            }
        }
        return false;
    }

    private class NotificationTrampolineCallback implements com.android.server.wm.BackgroundActivityStartCallback {
        private NotificationTrampolineCallback() {
        }

        @Override // com.android.server.wm.BackgroundActivityStartCallback
        public boolean isActivityStartAllowed(java.util.Collection<android.os.IBinder> tokens, int uid, java.lang.String packageName) {
            com.android.internal.util.Preconditions.checkArgument(!tokens.isEmpty());
            for (android.os.IBinder token : tokens) {
                if (token != com.android.server.notification.NotificationManagerService.ALLOWLIST_TOKEN) {
                    return true;
                }
            }
            java.lang.String logcatMessage = "Indirect notification activity start (trampoline) from " + packageName;
            if (blockTrampoline(uid)) {
                android.util.Slog.e(com.android.server.notification.NotificationManagerService.TAG, logcatMessage + " blocked");
                return false;
            }
            android.util.Slog.w(com.android.server.notification.NotificationManagerService.TAG, logcatMessage + ", this should be avoided for performance reasons");
            return true;
        }

        private boolean blockTrampoline(int uid) {
            if (com.android.server.notification.NotificationManagerService.this.mRoleObserver != null && com.android.server.notification.NotificationManagerService.this.mRoleObserver.isUidExemptFromTrampolineRestrictions(uid)) {
                return android.app.compat.CompatChanges.isChangeEnabled(com.android.server.notification.NotificationManagerService.NOTIFICATION_TRAMPOLINE_BLOCK_FOR_EXEMPT_ROLES, uid);
            }
            return android.app.compat.CompatChanges.isChangeEnabled(com.android.server.notification.NotificationManagerService.NOTIFICATION_TRAMPOLINE_BLOCK, uid);
        }

        @Override // com.android.server.wm.BackgroundActivityStartCallback
        public boolean canCloseSystemDialogs(java.util.Collection<android.os.IBinder> tokens, int uid) {
            return tokens.contains(com.android.server.notification.NotificationManagerService.ALLOWLIST_TOKEN) && !android.app.compat.CompatChanges.isChangeEnabled(com.android.server.notification.NotificationManagerService.NOTIFICATION_TRAMPOLINE_BLOCK, uid);
        }
    }

    public com.android.server.notification.INotificationManagerServiceWrapper getWrapper() {
        return this.mNMSWrapper;
    }

    private class NotificationManagerServiceWrapper implements com.android.server.notification.INotificationManagerServiceWrapper {
        private NotificationManagerServiceWrapper() {
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public com.android.server.notification.INotificationManagerServiceExt getNMSExt() {
            return com.android.server.notification.NotificationManagerService.mNMSExt;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public com.android.server.zenmode.IZenModeManagerExt getZenModeManagerExt() {
            return com.android.server.notification.NotificationManagerService.this.mZenModeManagerExt;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public android.content.pm.PackageManager getPackageManagerClient() {
            return com.android.server.notification.NotificationManagerService.this.mPackageManagerClient;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean isTelevision() {
            return com.android.server.notification.NotificationManagerService.this.mIsTelevision;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean notificationEffectsEnabledForAutomotive() {
            return false;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public com.android.server.notification.NotificationUsageStats getNotificationUsageStats() {
            return com.android.server.notification.NotificationManagerService.this.mUsageStats;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public com.android.server.notification.NotificationManagerService.NotificationListeners getNotificationListeners() {
            return com.android.server.notification.NotificationManagerService.this.mListeners;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public com.android.server.lights.LogicalLight getNotificationLight() {
            return com.android.server.notification.NotificationManagerService.this.mAttentionHelper.getNotificationLight();
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public com.android.server.notification.SnoozeHelper getSnoozeHelper() {
            return com.android.server.notification.NotificationManagerService.this.mSnoozeHelper;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public com.android.server.notification.ZenModeHelper getZenModeHelper() {
            return com.android.server.notification.NotificationManagerService.this.mZenModeHelper;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public android.app.AlarmManager getAlarmManager() {
            return com.android.server.notification.NotificationManagerService.this.mAlarmManager;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public com.android.server.notification.PermissionHelper getPermissionHelper() {
            return com.android.server.notification.NotificationManagerService.this.mPermissionHelper;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public com.android.server.notification.ShortcutHelper getShortcutHelper() {
            return com.android.server.notification.NotificationManagerService.this.mShortcutHelper;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public android.app.ActivityManager getActivityManager() {
            return com.android.server.notification.NotificationManagerService.this.mActivityManager;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public android.os.Handler getHandler() {
            return com.android.server.notification.NotificationManagerService.this.mHandler;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public android.os.IBinder getAllowListToken() {
            return com.android.server.notification.NotificationManagerService.ALLOWLIST_TOKEN;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public android.os.IBinder getService() {
            return com.android.server.notification.NotificationManagerService.this.mService;
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void checkCallerIsSameApp(java.lang.String pkg) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSameApp(pkg);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void checkCallerIsSystemOrSameApp(java.lang.String pkg) {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystemOrSameApp(pkg);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void checkCallerIsSystem() {
            com.android.server.notification.NotificationManagerService.this.checkCallerIsSystem();
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean isCallerSystemOrPhone() {
            return com.android.server.notification.NotificationManagerService.this.isCallerSystemOrPhone();
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean areNotificationsEnabledForPackageInt(java.lang.String pkg, int uid) {
            return com.android.server.notification.NotificationManagerService.this.areNotificationsEnabledForPackageInt(pkg, uid);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void doChannelWarningToast(int forUid, java.lang.CharSequence toastText) {
            com.android.server.notification.NotificationManagerService.this.doChannelWarningToast(forUid, toastText);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean checkDisqualifyingFeatures(int userId, int uid, int id, java.lang.String tag, com.android.server.notification.NotificationRecord r, boolean isAutogroup, boolean byForegroundService) {
            return com.android.server.notification.NotificationManagerService.this.checkDisqualifyingFeatures(userId, uid, id, tag, r, isAutogroup, byForegroundService);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean isNotificationForCurrentUser(com.android.server.notification.NotificationRecord record) {
            return com.android.server.notification.NotificationManagerService.this.mAttentionHelper.isNotificationForCurrentUser(record, new com.android.server.notification.NotificationAttentionHelper.Signals(com.android.server.notification.NotificationManagerService.this.mUserProfiles.isCurrentProfile(record.getUserId()), com.android.server.notification.NotificationManagerService.this.mListenerHints));
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean playSound(com.android.server.notification.NotificationRecord record, android.net.Uri soundUri) {
            return com.android.server.notification.NotificationManagerService.this.mAttentionHelper.playSound(record, soundUri);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean playVibration(com.android.server.notification.NotificationRecord record, android.os.VibrationEffect effect, boolean delayVibForSound) {
            return com.android.server.notification.NotificationManagerService.this.mAttentionHelper.playVibration(record, effect, delayVibForSound);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean removeFromNotificationListsLocked(com.android.server.notification.NotificationRecord r) {
            return com.android.server.notification.NotificationManagerService.this.removeFromNotificationListsLocked(r);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void cancelNotificationLocked(com.android.server.notification.NotificationRecord r, boolean sendDelete, int reason, boolean wasPosted, java.lang.String listenerName, long cancellationElapsedTimeMs) {
            com.android.server.notification.NotificationManagerService.this.cancelNotificationLocked(r, sendDelete, reason, wasPosted, listenerName, cancellationElapsedTimeMs);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void updateNotificationPulse() {
            com.android.server.notification.NotificationManagerService.this.mAttentionHelper.updateLightsLocked();
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void clearLightsLocked() {
            com.android.server.notification.NotificationManagerService.this.mAttentionHelper.clearLightsLocked();
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public void checkRestrictedCategories(android.app.Notification notification) {
            com.android.server.notification.NotificationManagerService.this.checkRestrictedCategories(notification);
        }

        @Override // com.android.server.notification.INotificationManagerServiceWrapper
        public boolean isInCall() {
            return com.android.server.notification.NotificationManagerService.this.mAttentionHelper.isInCall();
        }
    }

    interface PostNotificationTrackerFactory {
        default com.android.server.notification.NotificationManagerService.PostNotificationTracker newTracker(android.os.PowerManager.WakeLock optionalWakelock) {
            return new com.android.server.notification.NotificationManagerService.PostNotificationTracker(optionalWakelock);
        }
    }

    static class PostNotificationTracker {
        private final android.os.PowerManager.WakeLock mWakeLock;
        private final long mStartTime = android.os.SystemClock.elapsedRealtime();
        private boolean mOngoing = true;

        PostNotificationTracker(android.os.PowerManager.WakeLock wakeLock) {
            this.mWakeLock = wakeLock;
            if (com.android.server.notification.NotificationManagerService.DBG) {
                android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, "PostNotification: Started");
            }
        }

        long getStartTime() {
            return this.mStartTime;
        }

        boolean isOngoing() {
            return this.mOngoing;
        }

        void cancel() {
            if (!isOngoing()) {
                android.util.Log.wtfStack(com.android.server.notification.NotificationManagerService.TAG, "cancel() called on already-finished tracker");
                return;
            }
            this.mOngoing = false;
            if (this.mWakeLock != null) {
                android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$PostNotificationTracker$$ExternalSyntheticLambda0
                    public final void runOrThrow() throws java.lang.Exception {
                        this.f$0.lambda$cancel$0();
                    }
                });
            }
            if (com.android.server.notification.NotificationManagerService.DBG) {
                long elapsedTime = android.os.SystemClock.elapsedRealtime() - this.mStartTime;
                android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, android.text.TextUtils.formatSimple("PostNotification: Abandoned after %d ms", new java.lang.Object[]{java.lang.Long.valueOf(elapsedTime)}));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$cancel$0() throws java.lang.Exception {
            this.mWakeLock.release();
        }

        long finish() {
            long elapsedTime = android.os.SystemClock.elapsedRealtime() - this.mStartTime;
            if (!isOngoing()) {
                android.util.Log.wtfStack(com.android.server.notification.NotificationManagerService.TAG, "finish() called on already-finished tracker");
                return elapsedTime;
            }
            this.mOngoing = false;
            if (this.mWakeLock != null) {
                android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.NotificationManagerService$PostNotificationTracker$$ExternalSyntheticLambda1
                    public final void runOrThrow() throws java.lang.Exception {
                        this.f$0.lambda$finish$1();
                    }
                });
            }
            if (com.android.server.notification.NotificationManagerService.DBG) {
                android.util.Slog.d(com.android.server.notification.NotificationManagerService.TAG, android.text.TextUtils.formatSimple("PostNotification: Finished in %d ms", new java.lang.Object[]{java.lang.Long.valueOf(elapsedTime)}));
            }
            return elapsedTime;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$finish$1() throws java.lang.Exception {
            this.mWakeLock.release();
        }
    }
}
