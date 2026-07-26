package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public class AlarmManagerService extends com.android.server.SystemService {
    static final int ACTIVE_INDEX = 0;
    static final boolean DEBUG_ALARM_CLOCK = false;
    static final boolean DEBUG_BATCH = false;
    static final boolean DEBUG_BG_LIMIT = false;
    static final boolean DEBUG_LISTENER_CALLBACK = false;
    static final boolean DEBUG_STANDBY = false;
    static final boolean DEBUG_WAKELOCK = false;
    private static final java.lang.String DST_OFFSET_PROPERTY = "persist.sys.time.dst_offset";
    private static final java.lang.String DST_TRANSITION_PROPERTY = "persist.sys.time.dst_transition";
    private static final int ELAPSED_REALTIME_WAKEUP_MASK = 4;
    static final int FREQUENT_INDEX = 2;
    static final long INDEFINITE_DELAY = 31536000000L;
    static final int IS_WAKEUP_MASK = 5;
    static final long MIN_FUZZABLE_INTERVAL = 10000;
    static final int NEVER_INDEX = 4;
    static final int PRIORITY_NORMAL = 2;
    static final int PRIORITY_SYSTEM = 0;
    static final int PRIORITY_WAKEUP = 1;
    static final int RARE_INDEX = 3;
    static final boolean RECORD_ALARMS_IN_HISTORY = true;
    static final boolean RECORD_DEVICE_IDLE_ALARMS = false;
    private static final int REMOVAL_HISTORY_SIZE_PER_UID = 10;
    private static final int RTC_WAKEUP_MASK = 1;
    static final java.lang.String TAG = "AlarmManager";
    private static final long TEMPORARY_QUOTA_DURATION = 86400000;
    static final int TICK_HISTORY_DEPTH = 10;
    private static final java.lang.String TIMEOFFSET_PROPERTY = "persist.sys.time.offset";
    static final int TIME_CHANGED_MASK = 65536;
    static final java.lang.String TIME_TICK_TAG = "TIME_TICK";
    static final int WORKING_INDEX = 1;
    static final boolean localLOGV = false;
    private android.app.ActivityManagerInternal mActivityManagerInternal;
    android.app.ActivityOptions mActivityOptsRestrictBal;
    private final java.lang.Runnable mAlarmClockUpdater;
    final java.util.Comparator<com.android.server.alarm.Alarm> mAlarmDispatchComparator;
    com.android.server.alarm.AlarmStore mAlarmStore;
    android.util.SparseIntArray mAlarmsPerUid;
    com.android.server.alarm.AlarmManagerService.AppWakeupHistory mAllowWhileIdleCompatHistory;
    final java.util.ArrayList<com.android.server.alarm.AlarmManagerService.IdleDispatchEntry> mAllowWhileIdleDispatches;
    com.android.server.alarm.AlarmManagerService.AppWakeupHistory mAllowWhileIdleHistory;
    private com.android.server.alarm.IAlarmManagerServiceExt mAmsExt;
    private com.android.server.alarm.AlarmManagerService.AlarmManagerServiceWrapper mAmsWrapper;
    android.app.AppOpsManager mAppOps;
    boolean mAppStandbyParole;
    private com.android.server.AppStateTrackerImpl mAppStateTracker;
    com.android.server.alarm.AlarmManagerService.AppWakeupHistory mAppWakeupHistory;
    private final android.content.Intent mBackgroundIntent;
    private android.os.BatteryStatsInternal mBatteryStatsInternal;
    android.app.BroadcastOptions mBroadcastOptsRestrictBal;
    int mBroadcastRefCount;
    final android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.alarm.AlarmManagerService.BroadcastStats>> mBroadcastStats;
    com.android.server.alarm.AlarmManagerService.ClockReceiver mClockReceiver;
    com.android.server.alarm.AlarmManagerService.Constants mConstants;
    android.app.PendingIntent mDateChangeSender;
    final com.android.server.alarm.AlarmManagerService.DeliveryTracker mDeliveryTracker;
    volatile java.util.Set<java.lang.Integer> mExactAlarmCandidates;
    private final com.android.server.AppStateTrackerImpl.Listener mForceAppStandbyListener;
    com.android.server.alarm.AlarmManagerService.AlarmHandler mHandler;
    private final android.util.SparseArray<android.app.AlarmManager.AlarmClockInfo> mHandlerSparseAlarmClockArray;
    java.util.ArrayList<com.android.server.alarm.AlarmManagerService.InFlight> mInFlight;
    private final java.util.ArrayList<com.android.server.AlarmManagerInternal.InFlightListener> mInFlightListeners;
    private final com.android.server.alarm.AlarmManagerService.Injector mInjector;
    boolean mInteractive;
    long mLastAlarmDeliveryTime;
    android.util.SparseIntArray mLastOpScheduleExactAlarm;
    private final android.util.SparseLongArray mLastPriorityAlarmDispatch;
    private long mLastTickReceived;
    private long mLastTickSet;
    long mLastTimeChangeClockTime;
    long mLastTimeChangeRealtime;
    private long mLastTrigger;
    private long mLastWakeup;
    private int mListenerCount;
    android.os.IBinder.DeathRecipient mListenerDeathRecipient;
    private int mListenerFinishCount;
    com.android.server.DeviceIdleInternal mLocalDeviceIdleController;
    private volatile com.android.server.pm.permission.PermissionManagerServiceInternal mLocalPermissionManager;
    final java.lang.Object mLock;
    final com.android.internal.util.LocalLog mLog;
    long mMaxDelayTime;
    com.android.server.alarm.MetricsHelper mMetricsHelper;
    private final android.util.SparseArray<android.app.AlarmManager.AlarmClockInfo> mNextAlarmClockForUser;
    private boolean mNextAlarmClockMayChange;
    private long mNextNonWakeUpSetAt;
    private long mNextNonWakeup;
    long mNextNonWakeupDeliveryTime;
    private int mNextTickHistory;
    com.android.server.alarm.Alarm mNextWakeFromIdle;
    private long mNextWakeUpSetAt;
    private long mNextWakeup;
    long mNonInteractiveStartTime;
    long mNonInteractiveTime;
    int mNumDelayedAlarms;
    int mNumTimeChanged;
    android.app.BroadcastOptions mOptsTimeBroadcast;
    android.app.BroadcastOptions mOptsWithFgs;
    android.app.BroadcastOptions mOptsWithFgsForAlarmClock;
    android.app.BroadcastOptions mOptsWithoutFgs;
    private android.content.pm.PackageManagerInternal mPackageManagerInternal;
    android.util.SparseArray<java.util.ArrayList<com.android.server.alarm.Alarm>> mPendingBackgroundAlarms;
    com.android.server.alarm.Alarm mPendingIdleUntil;
    java.util.ArrayList<com.android.server.alarm.Alarm> mPendingNonWakeupAlarms;
    private final android.util.SparseBooleanArray mPendingSendNextAlarmClockChangedForUser;
    private final android.util.SparseArray<com.android.internal.util.RingBuffer<com.android.server.alarm.AlarmManagerService.RemovedAlarm>> mRemovalHistory;
    private android.app.role.RoleManager mRoleManager;
    private int mSendCount;
    private int mSendFinishCount;
    private final android.os.IBinder mService;
    long mStartCurrentDelayTime;
    private boolean mStartUserBeforeScheduledAlarms;
    private final com.android.internal.util.StatLogger mStatLogger;
    int mSystemUiUid;
    com.android.server.alarm.AlarmManagerService.TemporaryQuotaReserve mTemporaryQuotaReserve;
    private final long[] mTickHistory;
    android.content.Intent mTimeTickIntent;
    android.os.Bundle mTimeTickOptions;
    android.app.IAlarmListener mTimeTickTrigger;
    private final android.util.SparseArray<android.app.AlarmManager.AlarmClockInfo> mTmpSparseAlarmClockArray;
    long mTotalDelayTime;
    private android.app.usage.UsageStatsManagerInternal mUsageStatsManagerInternal;
    boolean mUseFrozenStateToDropListenerAlarms;
    com.android.server.alarm.UserWakeupStore mUserWakeupStore;
    android.os.PowerManager.WakeLock mWakeLock;
    private static final android.content.Intent NEXT_ALARM_CLOCK_CHANGED_INTENT = new android.content.Intent("android.app.action.NEXT_ALARM_CLOCK_CHANGED").addFlags(android.hardware.audio.common.V2_0.AudioFormat.APTX_HD);
    private static boolean DEBUG_PANIC = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DispatchPriority {
    }

    interface Stats {
        public static final int HAS_SCHEDULE_EXACT_ALARM = 1;
        public static final int REORDER_ALARMS_FOR_STANDBY = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void close(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getNextAlarm(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long init();

    /* JADX INFO: Access modifiers changed from: private */
    public static native int set(long j, int i, long j2, long j3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int waitForAlarm(long j);

    static boolean isTimeTickAlarm(com.android.server.alarm.Alarm a) {
        return a.uid == 1000 && TIME_TICK_TAG.equals(a.listenerTag);
    }

    static final class IdleDispatchEntry {
        long argRealtime;
        long elapsedRealtime;
        java.lang.String op;
        java.lang.String pkg;
        java.lang.String tag;
        int uid;

        IdleDispatchEntry() {
        }
    }

    private static android.app.BroadcastOptions makeBasicAlarmBroadcastOptions() {
        android.app.BroadcastOptions b = android.app.BroadcastOptions.makeBasic();
        b.setAlarmBroadcast(true);
        return b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mNextAlarmClockMayChange = true;
    }

    static class TemporaryQuotaReserve {
        private long mMaxDuration;
        private final android.util.ArrayMap<android.content.pm.UserPackage, com.android.server.alarm.AlarmManagerService.TemporaryQuotaReserve.QuotaInfo> mQuotaBuffer = new android.util.ArrayMap<>();

        private static class QuotaInfo {
            public long expirationTime;
            public long lastUsage;
            public int remainingQuota;

            private QuotaInfo() {
            }
        }

        TemporaryQuotaReserve(long maxDuration) {
            this.mMaxDuration = maxDuration;
        }

        void replenishQuota(java.lang.String packageName, int userId, int quota, long nowElapsed) {
            if (quota <= 0) {
                return;
            }
            android.content.pm.UserPackage userPackage = android.content.pm.UserPackage.of(userId, packageName);
            com.android.server.alarm.AlarmManagerService.TemporaryQuotaReserve.QuotaInfo currentQuotaInfo = this.mQuotaBuffer.get(userPackage);
            if (currentQuotaInfo == null) {
                currentQuotaInfo = new com.android.server.alarm.AlarmManagerService.TemporaryQuotaReserve.QuotaInfo();
                this.mQuotaBuffer.put(userPackage, currentQuotaInfo);
            }
            currentQuotaInfo.remainingQuota = quota;
            currentQuotaInfo.expirationTime = this.mMaxDuration + nowElapsed;
        }

        boolean hasQuota(java.lang.String packageName, int userId, long triggerElapsed) {
            android.content.pm.UserPackage userPackage = android.content.pm.UserPackage.of(userId, packageName);
            com.android.server.alarm.AlarmManagerService.TemporaryQuotaReserve.QuotaInfo quotaInfo = this.mQuotaBuffer.get(userPackage);
            return quotaInfo != null && quotaInfo.remainingQuota > 0 && triggerElapsed <= quotaInfo.expirationTime;
        }

        void recordUsage(java.lang.String packageName, int userId, long nowElapsed) {
            android.content.pm.UserPackage userPackage = android.content.pm.UserPackage.of(userId, packageName);
            com.android.server.alarm.AlarmManagerService.TemporaryQuotaReserve.QuotaInfo quotaInfo = this.mQuotaBuffer.get(userPackage);
            if (quotaInfo == null) {
                android.util.Slog.wtf(com.android.server.alarm.AlarmManagerService.TAG, "Temporary quota being consumed at " + nowElapsed + " but not found for package: " + packageName + ", user: " + userId);
                return;
            }
            if (nowElapsed > quotaInfo.lastUsage) {
                if (quotaInfo.remainingQuota <= 0) {
                    android.util.Slog.wtf(com.android.server.alarm.AlarmManagerService.TAG, "Temporary quota being consumed at " + nowElapsed + " but remaining only " + quotaInfo.remainingQuota + " for package: " + packageName + ", user: " + userId);
                } else if (quotaInfo.expirationTime < nowElapsed) {
                    android.util.Slog.wtf(com.android.server.alarm.AlarmManagerService.TAG, "Temporary quota being consumed at " + nowElapsed + " but expired at " + quotaInfo.expirationTime + " for package: " + packageName + ", user: " + userId);
                } else {
                    quotaInfo.remainingQuota--;
                }
                quotaInfo.lastUsage = nowElapsed;
            }
        }

        void cleanUpExpiredQuotas(long nowElapsed) {
            for (int i = this.mQuotaBuffer.size() - 1; i >= 0; i--) {
                com.android.server.alarm.AlarmManagerService.TemporaryQuotaReserve.QuotaInfo quotaInfo = this.mQuotaBuffer.valueAt(i);
                if (quotaInfo.expirationTime < nowElapsed) {
                    this.mQuotaBuffer.removeAt(i);
                }
            }
        }

        void removeForUser(int userId) {
            for (int i = this.mQuotaBuffer.size() - 1; i >= 0; i--) {
                android.content.pm.UserPackage userPackageKey = this.mQuotaBuffer.keyAt(i);
                if (userPackageKey.userId == userId) {
                    this.mQuotaBuffer.removeAt(i);
                }
            }
        }

        void removeForPackage(java.lang.String packageName, int userId) {
            android.content.pm.UserPackage userPackage = android.content.pm.UserPackage.of(userId, packageName);
            this.mQuotaBuffer.remove(userPackage);
        }

        void dump(android.util.IndentingPrintWriter pw, long nowElapsed) {
            pw.increaseIndent();
            for (int i = 0; i < this.mQuotaBuffer.size(); i++) {
                android.content.pm.UserPackage userPackage = this.mQuotaBuffer.keyAt(i);
                com.android.server.alarm.AlarmManagerService.TemporaryQuotaReserve.QuotaInfo quotaInfo = this.mQuotaBuffer.valueAt(i);
                pw.print(userPackage.packageName);
                pw.print(", u");
                pw.print(userPackage.userId);
                pw.print(": ");
                if (quotaInfo == null) {
                    pw.print("--");
                } else {
                    pw.print("quota: ");
                    pw.print(quotaInfo.remainingQuota);
                    pw.print(", expiration: ");
                    android.util.TimeUtils.formatDuration(quotaInfo.expirationTime, nowElapsed, pw);
                    pw.print(" last used: ");
                    android.util.TimeUtils.formatDuration(quotaInfo.lastUsage, nowElapsed, pw);
                }
                pw.println();
            }
            pw.decreaseIndent();
        }
    }

    static class AppWakeupHistory {
        private final android.util.ArrayMap<android.content.pm.UserPackage, android.util.LongArrayQueue> mPackageHistory = new android.util.ArrayMap<>();
        private long mWindowSize;

        AppWakeupHistory(long windowSize) {
            this.mWindowSize = windowSize;
        }

        void recordAlarmForPackage(java.lang.String packageName, int userId, long nowElapsed) {
            android.content.pm.UserPackage userPackage = android.content.pm.UserPackage.of(userId, packageName);
            android.util.LongArrayQueue history = this.mPackageHistory.get(userPackage);
            if (history == null) {
                history = new android.util.LongArrayQueue();
                this.mPackageHistory.put(userPackage, history);
            }
            if (history.size() == 0 || history.peekLast() < nowElapsed) {
                history.addLast(nowElapsed);
            }
            snapToWindow(history);
        }

        void removeForUser(int userId) {
            for (int i = this.mPackageHistory.size() - 1; i >= 0; i--) {
                android.content.pm.UserPackage userPackageKey = this.mPackageHistory.keyAt(i);
                if (userPackageKey.userId == userId) {
                    this.mPackageHistory.removeAt(i);
                }
            }
        }

        void removeForPackage(java.lang.String packageName, int userId) {
            android.content.pm.UserPackage userPackage = android.content.pm.UserPackage.of(userId, packageName);
            this.mPackageHistory.remove(userPackage);
        }

        private void snapToWindow(android.util.LongArrayQueue history) {
            while (history.peekFirst() + this.mWindowSize < history.peekLast()) {
                history.removeFirst();
            }
        }

        int getTotalWakeupsInWindow(java.lang.String packageName, int userId) {
            android.util.LongArrayQueue history = this.mPackageHistory.get(android.content.pm.UserPackage.of(userId, packageName));
            if (history == null) {
                return 0;
            }
            return history.size();
        }

        long getNthLastWakeupForPackage(java.lang.String packageName, int userId, int n) {
            int i;
            android.util.LongArrayQueue history = this.mPackageHistory.get(android.content.pm.UserPackage.of(userId, packageName));
            if (history != null && (i = history.size() - n) >= 0) {
                return history.get(i);
            }
            return 0L;
        }

        void dump(android.util.IndentingPrintWriter pw, long nowElapsed) {
            pw.increaseIndent();
            for (int i = 0; i < this.mPackageHistory.size(); i++) {
                android.content.pm.UserPackage userPackage = this.mPackageHistory.keyAt(i);
                android.util.LongArrayQueue timestamps = this.mPackageHistory.valueAt(i);
                pw.print(userPackage.packageName);
                pw.print(", u");
                pw.print(userPackage.userId);
                pw.print(": ");
                int lastIdx = java.lang.Math.max(0, timestamps.size() - 100);
                for (int j = timestamps.size() - 1; j >= lastIdx; j--) {
                    android.util.TimeUtils.formatDuration(timestamps.get(j), nowElapsed, pw);
                    pw.print(", ");
                }
                pw.println();
            }
            pw.decreaseIndent();
        }
    }

    static class RemovedAlarm {
        static final int REMOVE_REASON_ALARM_CANCELLED = 1;
        static final int REMOVE_REASON_DATA_CLEARED = 3;
        static final int REMOVE_REASON_EXACT_PERMISSION_REVOKED = 2;
        static final int REMOVE_REASON_LISTENER_BINDER_DIED = 5;
        static final int REMOVE_REASON_LISTENER_CACHED = 6;
        static final int REMOVE_REASON_PI_CANCELLED = 4;
        static final int REMOVE_REASON_UNDEFINED = 0;
        final com.android.server.alarm.Alarm.Snapshot mAlarmSnapshot;
        final int mRemoveReason;
        final long mWhenRemovedElapsed;
        final long mWhenRemovedRtc;

        RemovedAlarm(com.android.server.alarm.Alarm a, int removeReason, long nowRtc, long nowElapsed) {
            this.mAlarmSnapshot = new com.android.server.alarm.Alarm.Snapshot(a);
            this.mRemoveReason = removeReason;
            this.mWhenRemovedRtc = nowRtc;
            this.mWhenRemovedElapsed = nowElapsed;
        }

        static final boolean isLoggable(int reason) {
            return reason != 0;
        }

        static final java.lang.String removeReasonToString(int reason) {
            switch (reason) {
                case 1:
                    return "alarm_cancelled";
                case 2:
                    return "exact_alarm_permission_revoked";
                case 3:
                    return "data_cleared";
                case 4:
                    return "pi_cancelled";
                case 5:
                    return "listener_binder_died";
                case 6:
                    return "listener_cached";
                default:
                    return "unknown:" + reason;
            }
        }

        void dump(android.util.IndentingPrintWriter pw, long nowElapsed, java.text.SimpleDateFormat sdf) {
            pw.increaseIndent();
            pw.print("Reason", removeReasonToString(this.mRemoveReason));
            pw.print("elapsed=");
            android.util.TimeUtils.formatDuration(this.mWhenRemovedElapsed, nowElapsed, pw);
            pw.print(" rtc=");
            pw.print(sdf.format(new java.util.Date(this.mWhenRemovedRtc)));
            pw.println();
            pw.println("Snapshot:");
            pw.increaseIndent();
            this.mAlarmSnapshot.dump(pw, nowElapsed);
            pw.decreaseIndent();
            pw.decreaseIndent();
        }
    }

    final class Constants implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        private static final long DEFAULT_ALLOW_WHILE_IDLE_ALLOWLIST_DURATION = 10000;
        private static final int DEFAULT_ALLOW_WHILE_IDLE_COMPAT_QUOTA = 7;
        private static final long DEFAULT_ALLOW_WHILE_IDLE_COMPAT_WINDOW = 3600000;
        private static final int DEFAULT_ALLOW_WHILE_IDLE_QUOTA = 72;
        private static final long DEFAULT_ALLOW_WHILE_IDLE_WINDOW = 3600000;
        private static final int DEFAULT_APP_STANDBY_RESTRICTED_QUOTA = 1;
        private static final long DEFAULT_APP_STANDBY_RESTRICTED_WINDOW = 86400000;
        private static final long DEFAULT_APP_STANDBY_WINDOW = 3600000;
        private static final long DEFAULT_CACHED_LISTENER_REMOVAL_DELAY = 10000;
        private static final boolean DEFAULT_DELAY_NONWAKEUP_ALARMS_WHILE_SCREEN_OFF = true;
        private static final long DEFAULT_LISTENER_TIMEOUT = 5000;
        private static final int DEFAULT_MAX_ALARMS_PER_UID = 500;
        private static final long DEFAULT_MAX_DEVICE_IDLE_FUZZ = 900000;
        private static final long DEFAULT_MAX_INTERVAL = 31536000000L;
        private static final long DEFAULT_MIN_DEVICE_IDLE_FUZZ = 120000;
        private static final long DEFAULT_MIN_FUTURITY = 5000;
        private static final long DEFAULT_MIN_INTERVAL = 60000;
        private static final long DEFAULT_MIN_WINDOW = 600000;
        private static final long DEFAULT_PRIORITY_ALARM_DELAY = 540000;
        private static final int DEFAULT_TEMPORARY_QUOTA_BUMP = 0;
        private static final boolean DEFAULT_TIME_TICK_ALLOWED_WHILE_IDLE = true;
        static final java.lang.String KEY_ALLOW_WHILE_IDLE_COMPAT_QUOTA = "allow_while_idle_compat_quota";
        static final java.lang.String KEY_ALLOW_WHILE_IDLE_COMPAT_WINDOW = "allow_while_idle_compat_window";
        static final java.lang.String KEY_ALLOW_WHILE_IDLE_QUOTA = "allow_while_idle_quota";
        static final java.lang.String KEY_ALLOW_WHILE_IDLE_WHITELIST_DURATION = "allow_while_idle_whitelist_duration";
        static final java.lang.String KEY_ALLOW_WHILE_IDLE_WINDOW = "allow_while_idle_window";
        private static final java.lang.String KEY_APP_STANDBY_RESTRICTED_QUOTA = "standby_quota_restricted";
        private static final java.lang.String KEY_APP_STANDBY_RESTRICTED_WINDOW = "app_standby_restricted_window";
        private static final java.lang.String KEY_APP_STANDBY_WINDOW = "app_standby_window";
        static final java.lang.String KEY_CACHED_LISTENER_REMOVAL_DELAY = "cached_listener_removal_delay";
        private static final java.lang.String KEY_DELAY_NONWAKEUP_ALARMS_WHILE_SCREEN_OFF = "delay_nonwakeup_alarms_while_screen_off";
        static final java.lang.String KEY_LISTENER_TIMEOUT = "listener_timeout";
        static final java.lang.String KEY_MAX_ALARMS_PER_UID = "max_alarms_per_uid";
        static final java.lang.String KEY_MAX_DEVICE_IDLE_FUZZ = "max_device_idle_fuzz";
        static final java.lang.String KEY_MAX_INTERVAL = "max_interval";
        static final java.lang.String KEY_MIN_DEVICE_IDLE_FUZZ = "min_device_idle_fuzz";
        static final java.lang.String KEY_MIN_FUTURITY = "min_futurity";
        static final java.lang.String KEY_MIN_INTERVAL = "min_interval";
        static final java.lang.String KEY_MIN_WINDOW = "min_window";
        private static final java.lang.String KEY_PREFIX_STANDBY_QUOTA = "standby_quota_";
        static final java.lang.String KEY_PRIORITY_ALARM_DELAY = "priority_alarm_delay";
        static final java.lang.String KEY_TEMPORARY_QUOTA_BUMP = "temporary_quota_bump";
        private static final java.lang.String KEY_TIME_TICK_ALLOWED_WHILE_IDLE = "time_tick_allowed_while_idle";
        final java.lang.String[] KEYS_APP_STANDBY_QUOTAS = {"standby_quota_active", "standby_quota_working", "standby_quota_frequent", "standby_quota_rare", "standby_quota_never"};
        private final int[] DEFAULT_APP_STANDBY_QUOTAS = {720, 10, 2, 1, 0};
        public long MIN_FUTURITY = 5000;
        public long MIN_INTERVAL = 60000;
        public long MAX_INTERVAL = 31536000000L;
        public long MIN_WINDOW = 600000;
        public long ALLOW_WHILE_IDLE_WHITELIST_DURATION = 10000;
        public long LISTENER_TIMEOUT = 5000;
        public int MAX_ALARMS_PER_UID = 500;
        public long APP_STANDBY_WINDOW = 3600000;
        public int[] APP_STANDBY_QUOTAS = new int[this.DEFAULT_APP_STANDBY_QUOTAS.length];
        public int APP_STANDBY_RESTRICTED_QUOTA = 1;
        public long APP_STANDBY_RESTRICTED_WINDOW = 86400000;
        public boolean TIME_TICK_ALLOWED_WHILE_IDLE = true;
        public int ALLOW_WHILE_IDLE_QUOTA = 72;
        public int ALLOW_WHILE_IDLE_COMPAT_QUOTA = 7;
        public long ALLOW_WHILE_IDLE_COMPAT_WINDOW = 3600000;
        public long ALLOW_WHILE_IDLE_WINDOW = 3600000;
        public long PRIORITY_ALARM_DELAY = DEFAULT_PRIORITY_ALARM_DELAY;
        public long MIN_DEVICE_IDLE_FUZZ = 120000;
        public long MAX_DEVICE_IDLE_FUZZ = DEFAULT_MAX_DEVICE_IDLE_FUZZ;
        public int TEMPORARY_QUOTA_BUMP = 0;
        public boolean DELAY_NONWAKEUP_ALARMS_WHILE_SCREEN_OFF = true;
        public long CACHED_LISTENER_REMOVAL_DELAY = 10000;
        private long mLastAllowWhileIdleWhitelistDuration = -1;
        private int mVersion = 0;

        Constants(android.os.Handler handler) {
            updateAllowWhileIdleWhitelistDurationLocked();
            for (int i = 0; i < this.APP_STANDBY_QUOTAS.length; i++) {
                this.APP_STANDBY_QUOTAS[i] = this.DEFAULT_APP_STANDBY_QUOTAS[i];
            }
        }

        public int getVersion() {
            int i;
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                i = this.mVersion;
            }
            return i;
        }

        public void start() {
            com.android.server.alarm.AlarmManagerService.this.mInjector.registerDeviceConfigListener(this);
            onPropertiesChanged(android.provider.DeviceConfig.getProperties("alarm_manager", new java.lang.String[0]));
        }

        public void updateAllowWhileIdleWhitelistDurationLocked() {
            if (this.mLastAllowWhileIdleWhitelistDuration != this.ALLOW_WHILE_IDLE_WHITELIST_DURATION) {
                this.mLastAllowWhileIdleWhitelistDuration = this.ALLOW_WHILE_IDLE_WHITELIST_DURATION;
                com.android.server.alarm.AlarmManagerService.this.mOptsWithFgs.setTemporaryAppAllowlist(this.ALLOW_WHILE_IDLE_WHITELIST_DURATION, 0, 302, "");
                com.android.server.alarm.AlarmManagerService.this.mOptsWithFgsForAlarmClock.setTemporaryAppAllowlist(this.ALLOW_WHILE_IDLE_WHITELIST_DURATION, 0, 301, "");
                com.android.server.alarm.AlarmManagerService.this.mOptsWithoutFgs.setTemporaryAppAllowlist(this.ALLOW_WHILE_IDLE_WHITELIST_DURATION, 1, -1, "");
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:12:0x0030  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onPropertiesChanged(android.provider.DeviceConfig.Properties r16) {
            /*
                Method dump skipped, instruction units count: 780
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.alarm.AlarmManagerService.Constants.onPropertiesChanged(android.provider.DeviceConfig$Properties):void");
        }

        private void updateDeviceIdleFuzzBoundaries() {
            android.provider.DeviceConfig.Properties properties = android.provider.DeviceConfig.getProperties("alarm_manager", new java.lang.String[]{KEY_MIN_DEVICE_IDLE_FUZZ, KEY_MAX_DEVICE_IDLE_FUZZ});
            this.MIN_DEVICE_IDLE_FUZZ = properties.getLong(KEY_MIN_DEVICE_IDLE_FUZZ, 120000L);
            this.MAX_DEVICE_IDLE_FUZZ = properties.getLong(KEY_MAX_DEVICE_IDLE_FUZZ, DEFAULT_MAX_DEVICE_IDLE_FUZZ);
            if (this.MAX_DEVICE_IDLE_FUZZ < this.MIN_DEVICE_IDLE_FUZZ) {
                android.util.Slog.w(com.android.server.alarm.AlarmManagerService.TAG, "max_device_idle_fuzz cannot be smaller than min_device_idle_fuzz! Increasing to " + this.MIN_DEVICE_IDLE_FUZZ);
                this.MAX_DEVICE_IDLE_FUZZ = this.MIN_DEVICE_IDLE_FUZZ;
            }
        }

        private void updateStandbyQuotasLocked() {
            android.provider.DeviceConfig.Properties properties = android.provider.DeviceConfig.getProperties("alarm_manager", this.KEYS_APP_STANDBY_QUOTAS);
            this.APP_STANDBY_QUOTAS[0] = properties.getInt(this.KEYS_APP_STANDBY_QUOTAS[0], this.DEFAULT_APP_STANDBY_QUOTAS[0]);
            for (int i = 1; i < this.KEYS_APP_STANDBY_QUOTAS.length; i++) {
                this.APP_STANDBY_QUOTAS[i] = properties.getInt(this.KEYS_APP_STANDBY_QUOTAS[i], java.lang.Math.min(this.APP_STANDBY_QUOTAS[i - 1], this.DEFAULT_APP_STANDBY_QUOTAS[i]));
            }
            this.APP_STANDBY_RESTRICTED_QUOTA = java.lang.Math.max(1, android.provider.DeviceConfig.getInt("alarm_manager", KEY_APP_STANDBY_RESTRICTED_QUOTA, 1));
        }

        private void updateStandbyWindowsLocked() {
            android.provider.DeviceConfig.Properties properties = android.provider.DeviceConfig.getProperties("alarm_manager", new java.lang.String[]{KEY_APP_STANDBY_WINDOW, KEY_APP_STANDBY_RESTRICTED_WINDOW});
            this.APP_STANDBY_WINDOW = properties.getLong(KEY_APP_STANDBY_WINDOW, 3600000L);
            if (this.APP_STANDBY_WINDOW > 3600000) {
                android.util.Slog.w(com.android.server.alarm.AlarmManagerService.TAG, "Cannot exceed the app_standby_window size of 3600000");
                this.APP_STANDBY_WINDOW = 3600000L;
            } else if (this.APP_STANDBY_WINDOW < 3600000) {
                android.util.Slog.w(com.android.server.alarm.AlarmManagerService.TAG, "Using a non-default app_standby_window of " + this.APP_STANDBY_WINDOW);
            }
            this.APP_STANDBY_RESTRICTED_WINDOW = java.lang.Math.max(this.APP_STANDBY_WINDOW, properties.getLong(KEY_APP_STANDBY_RESTRICTED_WINDOW, 86400000L));
        }

        void dump(android.util.IndentingPrintWriter pw) {
            pw.println("Settings:");
            pw.increaseIndent();
            pw.print("version", java.lang.Integer.valueOf(this.mVersion));
            pw.println();
            pw.print(KEY_MIN_FUTURITY);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MIN_FUTURITY, pw);
            pw.println();
            pw.print(KEY_MIN_INTERVAL);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MIN_INTERVAL, pw);
            pw.println();
            pw.print(KEY_MAX_INTERVAL);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MAX_INTERVAL, pw);
            pw.println();
            pw.print(KEY_MIN_WINDOW);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MIN_WINDOW, pw);
            pw.println();
            pw.print(KEY_LISTENER_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.LISTENER_TIMEOUT, pw);
            pw.println();
            pw.print(KEY_ALLOW_WHILE_IDLE_QUOTA, java.lang.Integer.valueOf(this.ALLOW_WHILE_IDLE_QUOTA));
            pw.println();
            pw.print(KEY_ALLOW_WHILE_IDLE_WINDOW);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.ALLOW_WHILE_IDLE_WINDOW, pw);
            pw.println();
            pw.print(KEY_ALLOW_WHILE_IDLE_COMPAT_QUOTA, java.lang.Integer.valueOf(this.ALLOW_WHILE_IDLE_COMPAT_QUOTA));
            pw.println();
            pw.print(KEY_ALLOW_WHILE_IDLE_COMPAT_WINDOW);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.ALLOW_WHILE_IDLE_COMPAT_WINDOW, pw);
            pw.println();
            pw.print(KEY_ALLOW_WHILE_IDLE_WHITELIST_DURATION);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.ALLOW_WHILE_IDLE_WHITELIST_DURATION, pw);
            pw.println();
            pw.print(KEY_MAX_ALARMS_PER_UID, java.lang.Integer.valueOf(this.MAX_ALARMS_PER_UID));
            pw.println();
            pw.print(KEY_APP_STANDBY_WINDOW);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.APP_STANDBY_WINDOW, pw);
            pw.println();
            for (int i = 0; i < this.KEYS_APP_STANDBY_QUOTAS.length; i++) {
                pw.print(this.KEYS_APP_STANDBY_QUOTAS[i], java.lang.Integer.valueOf(this.APP_STANDBY_QUOTAS[i]));
                pw.println();
            }
            int i2 = this.APP_STANDBY_RESTRICTED_QUOTA;
            pw.print(KEY_APP_STANDBY_RESTRICTED_QUOTA, java.lang.Integer.valueOf(i2));
            pw.println();
            pw.print(KEY_APP_STANDBY_RESTRICTED_WINDOW);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.APP_STANDBY_RESTRICTED_WINDOW, pw);
            pw.println();
            pw.print(KEY_TIME_TICK_ALLOWED_WHILE_IDLE, java.lang.Boolean.valueOf(this.TIME_TICK_ALLOWED_WHILE_IDLE));
            pw.println();
            pw.print(KEY_PRIORITY_ALARM_DELAY);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.PRIORITY_ALARM_DELAY, pw);
            pw.println();
            pw.print(KEY_MIN_DEVICE_IDLE_FUZZ);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MIN_DEVICE_IDLE_FUZZ, pw);
            pw.println();
            pw.print(KEY_MAX_DEVICE_IDLE_FUZZ);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MAX_DEVICE_IDLE_FUZZ, pw);
            pw.println();
            pw.print(KEY_TEMPORARY_QUOTA_BUMP, java.lang.Integer.valueOf(this.TEMPORARY_QUOTA_BUMP));
            pw.println();
            pw.print(KEY_DELAY_NONWAKEUP_ALARMS_WHILE_SCREEN_OFF, java.lang.Boolean.valueOf(this.DELAY_NONWAKEUP_ALARMS_WHILE_SCREEN_OFF));
            pw.println();
            pw.print(KEY_CACHED_LISTENER_REMOVAL_DELAY);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.CACHED_LISTENER_REMOVAL_DELAY, pw);
            pw.println();
            pw.decreaseIndent();
        }

        void dumpProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1112396529665L, this.MIN_FUTURITY);
            proto.write(1112396529666L, this.MIN_INTERVAL);
            proto.write(1112396529671L, this.MAX_INTERVAL);
            proto.write(1112396529667L, this.LISTENER_TIMEOUT);
            proto.write(1112396529670L, this.ALLOW_WHILE_IDLE_WHITELIST_DURATION);
            proto.end(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$new$1(com.android.server.alarm.Alarm lhs, com.android.server.alarm.Alarm rhs) {
        boolean idleUntil1 = (lhs.flags & 16) != 0;
        boolean idleUntil2 = (rhs.flags & 16) != 0;
        if (idleUntil1 != idleUntil2) {
            return idleUntil1 ? -1 : 1;
        }
        if (lhs.priorityClass < rhs.priorityClass) {
            return -1;
        }
        if (lhs.priorityClass > rhs.priorityClass) {
            return 1;
        }
        boolean timeTick1 = lhs.listener == this.mTimeTickTrigger;
        boolean timeTick2 = rhs.listener == this.mTimeTickTrigger;
        if (timeTick1 != timeTick2) {
            return timeTick1 ? -1 : 1;
        }
        if (lhs.getRequestedElapsed() < rhs.getRequestedElapsed()) {
            return -1;
        }
        return lhs.getRequestedElapsed() > rhs.getRequestedElapsed() ? 1 : 0;
    }

    void calculateDeliveryPriorities(java.util.ArrayList<com.android.server.alarm.Alarm> alarms) {
        int N = alarms.size();
        android.util.ArraySet<android.content.pm.UserPackage> wakeupPackages = new android.util.ArraySet<>(4);
        for (int i = 0; i < N; i++) {
            com.android.server.alarm.Alarm a = alarms.get(i);
            if (a.wakeup) {
                wakeupPackages.add(android.content.pm.UserPackage.of(android.os.UserHandle.getUserId(a.creatorUid), a.sourcePackage));
            }
        }
        for (int i2 = 0; i2 < N; i2++) {
            com.android.server.alarm.Alarm a2 = alarms.get(i2);
            if (a2.creatorUid == 1000 && com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(a2.sourcePackage)) {
                a2.priorityClass = 0;
            } else if (wakeupPackages.contains(android.content.pm.UserPackage.of(android.os.UserHandle.getUserId(a2.creatorUid), a2.sourcePackage))) {
                a2.priorityClass = 1;
            } else {
                a2.priorityClass = 2;
            }
        }
    }

    AlarmManagerService(android.content.Context context, com.android.server.alarm.AlarmManagerService.Injector injector) {
        super(context);
        this.mBackgroundIntent = new android.content.Intent().addFlags(4);
        this.mLog = new com.android.internal.util.LocalLog(TAG);
        this.mLock = new java.lang.Object();
        this.mExactAlarmCandidates = java.util.Collections.emptySet();
        this.mLastOpScheduleExactAlarm = new android.util.SparseIntArray();
        this.mPendingBackgroundAlarms = new android.util.SparseArray<>();
        this.mTickHistory = new long[10];
        this.mBroadcastRefCount = 0;
        this.mAlarmsPerUid = new android.util.SparseIntArray();
        this.mPendingNonWakeupAlarms = new java.util.ArrayList<>();
        this.mInFlight = new java.util.ArrayList<>();
        this.mInFlightListeners = new java.util.ArrayList<>();
        this.mLastPriorityAlarmDispatch = new android.util.SparseLongArray();
        this.mRemovalHistory = new android.util.SparseArray<>();
        this.mDeliveryTracker = new com.android.server.alarm.AlarmManagerService.DeliveryTracker();
        this.mInteractive = true;
        this.mAllowWhileIdleDispatches = new java.util.ArrayList<>();
        this.mStatLogger = new com.android.internal.util.StatLogger("Alarm manager stats", new java.lang.String[]{"REORDER_ALARMS_FOR_STANDBY", "HAS_SCHEDULE_EXACT_ALARM"});
        this.mOptsWithFgs = makeBasicAlarmBroadcastOptions();
        this.mOptsWithFgsForAlarmClock = makeBasicAlarmBroadcastOptions();
        this.mOptsWithoutFgs = makeBasicAlarmBroadcastOptions();
        this.mOptsTimeBroadcast = makeBasicAlarmBroadcastOptions();
        this.mActivityOptsRestrictBal = android.app.ActivityOptions.makeBasic();
        this.mBroadcastOptsRestrictBal = makeBasicAlarmBroadcastOptions();
        this.mNextAlarmClockForUser = new android.util.SparseArray<>();
        this.mTmpSparseAlarmClockArray = new android.util.SparseArray<>();
        this.mPendingSendNextAlarmClockChangedForUser = new android.util.SparseBooleanArray();
        this.mAlarmClockUpdater = new java.lang.Runnable() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        };
        this.mHandlerSparseAlarmClockArray = new android.util.SparseArray<>();
        this.mAlarmDispatchComparator = new java.util.Comparator() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda17
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return this.f$0.lambda$new$1((com.android.server.alarm.Alarm) obj, (com.android.server.alarm.Alarm) obj2);
            }
        };
        this.mPendingIdleUntil = null;
        this.mNextWakeFromIdle = null;
        this.mBroadcastStats = new android.util.SparseArray<>();
        this.mNumDelayedAlarms = 0;
        this.mTotalDelayTime = 0L;
        this.mMaxDelayTime = 0L;
        this.mService = new com.android.server.alarm.AlarmManagerService.AnonymousClass4();
        this.mForceAppStandbyListener = new com.android.server.alarm.AlarmManagerService.AnonymousClass7();
        this.mSendCount = 0;
        this.mSendFinishCount = 0;
        this.mListenerCount = 0;
        this.mListenerFinishCount = 0;
        this.mAmsWrapper = new com.android.server.alarm.AlarmManagerService.AlarmManagerServiceWrapper();
        this.mAmsExt = (com.android.server.alarm.IAlarmManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.alarm.IAlarmManagerServiceExt.class).base(this).create();
        this.mInjector = injector;
    }

    public AlarmManagerService(android.content.Context context) {
        this(context, new com.android.server.alarm.AlarmManagerService.Injector(context));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isRtc(int type) {
        return type == 1 || type == 0;
    }

    private long convertToElapsed(long when, int type) {
        if (isRtc(type)) {
            return when - (this.mInjector.getCurrentTimeMillis() - this.mInjector.getElapsedRealtimeMillis());
        }
        return when;
    }

    long getMinimumAllowedWindow(long nowElapsed, long triggerElapsed) {
        long futurity = triggerElapsed - nowElapsed;
        return java.lang.Math.min((long) (futurity * 0.75d), this.mConstants.MIN_WINDOW);
    }

    static long maxTriggerTime(long now, long triggerAtTime, long interval) {
        long futurity;
        if (interval == 0) {
            futurity = triggerAtTime - now;
        } else {
            futurity = interval;
        }
        if (futurity < 10000) {
            futurity = 0;
        }
        long maxElapsed = addClampPositive(triggerAtTime, (long) (futurity * 0.75d));
        if (interval == 0) {
            return java.lang.Math.min(maxElapsed, addClampPositive(triggerAtTime, 3600000L));
        }
        return maxElapsed;
    }

    void reevaluateRtcAlarms() {
        synchronized (this.mLock) {
            boolean changed = this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda13
                @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm) {
                    return this.f$0.lambda$reevaluateRtcAlarms$2(alarm);
                }
            });
            if (changed && this.mPendingIdleUntil != null && this.mNextWakeFromIdle != null && isRtc(this.mNextWakeFromIdle.type)) {
                boolean idleUntilUpdated = this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda14
                    @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                    public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm) {
                        return this.f$0.lambda$reevaluateRtcAlarms$3(alarm);
                    }
                });
                if (idleUntilUpdated) {
                    this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda15
                        @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                        public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm) {
                            return this.f$0.lambda$reevaluateRtcAlarms$4(alarm);
                        }
                    });
                }
            }
            if (changed) {
                rescheduleKernelAlarmsLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$reevaluateRtcAlarms$2(com.android.server.alarm.Alarm a) {
        if (!isRtc(a.type)) {
            return false;
        }
        return restoreRequestedTime(a);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$reevaluateRtcAlarms$3(com.android.server.alarm.Alarm a) {
        return a == this.mPendingIdleUntil && adjustIdleUntilTime(a);
    }

    boolean reorderAlarmsBasedOnStandbyBuckets(final android.util.ArraySet<android.content.pm.UserPackage> targetPackages) {
        long start = this.mStatLogger.getTime();
        boolean changed = this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda23
            @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
            public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm) {
                return this.f$0.lambda$reorderAlarmsBasedOnStandbyBuckets$5(targetPackages, alarm);
            }
        });
        this.mStatLogger.logDurationStat(0, start);
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$reorderAlarmsBasedOnStandbyBuckets$5(android.util.ArraySet targetPackages, com.android.server.alarm.Alarm a) {
        android.content.pm.UserPackage userPackage = android.content.pm.UserPackage.of(android.os.UserHandle.getUserId(a.creatorUid), a.sourcePackage);
        if (targetPackages != null && !targetPackages.contains(userPackage)) {
            return false;
        }
        return adjustDeliveryTimeBasedOnBucketLocked(a);
    }

    private boolean restoreRequestedTime(com.android.server.alarm.Alarm a) {
        return a.setPolicyElapsed(0, convertToElapsed(a.origWhen, a.type));
    }

    static long clampPositive(long val) {
        if (val >= 0) {
            return val;
        }
        return Long.MAX_VALUE;
    }

    static long addClampPositive(long val1, long val2) {
        long val = val1 + val2;
        if (val >= 0) {
            return val;
        }
        if (val1 < 0 || val2 < 0) {
            return 0L;
        }
        return Long.MAX_VALUE;
    }

    void sendPendingBackgroundAlarmsLocked(int uid, java.lang.String packageName) throws java.lang.Exception {
        java.util.ArrayList<com.android.server.alarm.Alarm> alarmsToDeliver;
        java.util.ArrayList<com.android.server.alarm.Alarm> alarmsForUid = this.mPendingBackgroundAlarms.get(uid);
        if (alarmsForUid == null || alarmsForUid.size() == 0) {
            return;
        }
        if (packageName != null) {
            if (this.mAmsExt.isDynamicLogEnabled()) {
                android.util.Slog.d(TAG, "Sending blocked alarms for uid " + uid + ", package " + packageName);
            }
            alarmsToDeliver = new java.util.ArrayList<>();
            for (int i = alarmsForUid.size() - 1; i >= 0; i--) {
                com.android.server.alarm.Alarm a = alarmsForUid.get(i);
                if (a.matches(packageName)) {
                    alarmsToDeliver.add(alarmsForUid.remove(i));
                }
            }
            int i2 = alarmsForUid.size();
            if (i2 == 0) {
                this.mPendingBackgroundAlarms.remove(uid);
            }
        } else {
            if (this.mAmsExt.isDynamicLogEnabled()) {
                android.util.Slog.d(TAG, "Sending blocked alarms for uid " + uid);
            }
            alarmsToDeliver = alarmsForUid;
            this.mPendingBackgroundAlarms.remove(uid);
        }
        deliverPendingBackgroundAlarmsLocked(alarmsToDeliver, this.mInjector.getElapsedRealtimeMillis());
    }

    void sendAllUnrestrictedPendingBackgroundAlarmsLocked() throws java.lang.Exception {
        java.util.ArrayList<com.android.server.alarm.Alarm> alarmsToDeliver = new java.util.ArrayList<>();
        findAllUnrestrictedPendingBackgroundAlarmsLockedInner(this.mPendingBackgroundAlarms, alarmsToDeliver, new java.util.function.Predicate() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.isBackgroundRestricted((com.android.server.alarm.Alarm) obj);
            }
        });
        if (alarmsToDeliver.size() > 0) {
            deliverPendingBackgroundAlarmsLocked(alarmsToDeliver, this.mInjector.getElapsedRealtimeMillis());
        }
    }

    static void findAllUnrestrictedPendingBackgroundAlarmsLockedInner(android.util.SparseArray<java.util.ArrayList<com.android.server.alarm.Alarm>> pendingAlarms, java.util.ArrayList<com.android.server.alarm.Alarm> unrestrictedAlarms, java.util.function.Predicate<com.android.server.alarm.Alarm> isBackgroundRestricted) {
        for (int uidIndex = pendingAlarms.size() - 1; uidIndex >= 0; uidIndex--) {
            java.util.ArrayList<com.android.server.alarm.Alarm> alarmsForUid = pendingAlarms.valueAt(uidIndex);
            for (int alarmIndex = alarmsForUid.size() - 1; alarmIndex >= 0; alarmIndex--) {
                com.android.server.alarm.Alarm alarm = alarmsForUid.get(alarmIndex);
                if (!isBackgroundRestricted.test(alarm)) {
                    unrestrictedAlarms.add(alarm);
                    alarmsForUid.remove(alarmIndex);
                }
            }
            int alarmIndex2 = alarmsForUid.size();
            if (alarmIndex2 == 0) {
                pendingAlarms.removeAt(uidIndex);
            }
        }
    }

    private void deliverPendingBackgroundAlarmsLocked(java.util.ArrayList<com.android.server.alarm.Alarm> alarms, long nowELAPSED) throws java.lang.Exception {
        com.android.server.alarm.AlarmManagerService alarmManagerService;
        java.util.ArrayList<com.android.server.alarm.Alarm> arrayList;
        long j;
        boolean hasWakeup;
        int i;
        int N;
        java.util.ArrayList<com.android.server.alarm.Alarm> arrayList2 = alarms;
        long j2 = nowELAPSED;
        int N2 = alarms.size();
        boolean hasWakeup2 = false;
        int i2 = 0;
        while (i2 < N2) {
            com.android.server.alarm.Alarm alarm = arrayList2.get(i2);
            if (!alarm.wakeup) {
                hasWakeup = hasWakeup2;
            } else {
                hasWakeup = true;
            }
            alarm.count = 1;
            if (alarm.repeatInterval <= 0) {
                i = i2;
                N = N2;
            } else {
                alarm.count = (int) (((long) alarm.count) + ((j2 - alarm.getRequestedElapsed()) / alarm.repeatInterval));
                long delta = ((long) alarm.count) * alarm.repeatInterval;
                long nextElapsed = alarm.getRequestedElapsed() + delta;
                long nextMaxElapsed = maxTriggerTime(nowELAPSED, nextElapsed, alarm.repeatInterval);
                i = i2;
                N = N2;
                setImplLocked(alarm.type, alarm.origWhen + delta, nextElapsed, nextMaxElapsed - nextElapsed, alarm.repeatInterval, alarm.operation, null, null, alarm.flags, alarm.workSource, alarm.alarmClock, alarm.uid, alarm.packageName, alarm.getWrapper().getExt().getProcName(), null, -1, alarm.getWrapper().getExt().getAction(), alarm.getWrapper().getExt().getComponent());
            }
            i2 = i + 1;
            arrayList2 = alarms;
            j2 = nowELAPSED;
            hasWakeup2 = hasWakeup;
            N2 = N;
        }
        if (hasWakeup2) {
            alarmManagerService = this;
            arrayList = alarms;
            j = nowELAPSED;
        } else {
            alarmManagerService = this;
            j = nowELAPSED;
            if (!alarmManagerService.checkAllowNonWakeupDelayLocked(j)) {
                arrayList = alarms;
            } else {
                if (alarmManagerService.mPendingNonWakeupAlarms.size() == 0) {
                    alarmManagerService.mStartCurrentDelayTime = j;
                    alarmManagerService.mNextNonWakeupDeliveryTime = ((alarmManagerService.currentNonWakeupFuzzLocked(j) * 3) / 2) + j;
                }
                alarmManagerService.mPendingNonWakeupAlarms.addAll(alarms);
                alarmManagerService.mNumDelayedAlarms += alarms.size();
                return;
            }
        }
        if (alarmManagerService.mAmsExt.isDynamicLogEnabled()) {
            android.util.Slog.d(TAG, "Waking up to deliver pending blocked alarms");
        }
        if (alarmManagerService.mPendingNonWakeupAlarms.size() > 0) {
            arrayList.addAll(alarmManagerService.mPendingNonWakeupAlarms);
            long thisDelayTime = j - alarmManagerService.mStartCurrentDelayTime;
            alarmManagerService.mTotalDelayTime += thisDelayTime;
            if (alarmManagerService.mMaxDelayTime < thisDelayTime) {
                alarmManagerService.mMaxDelayTime = thisDelayTime;
            }
            alarmManagerService.mPendingNonWakeupAlarms.clear();
        }
        calculateDeliveryPriorities(alarms);
        java.util.Collections.sort(arrayList, alarmManagerService.mAlarmDispatchComparator);
        deliverAlarmsLocked(alarms, nowELAPSED);
    }

    static final class InFlight {
        final int mAlarmType;
        final com.android.server.alarm.AlarmManagerService.BroadcastStats mBroadcastStats;
        final int mCreatorUid;
        final com.android.server.alarm.AlarmManagerService.FilterStats mFilterStats;
        final android.os.IBinder mListener;
        final android.app.PendingIntent mPendingIntent;
        final int mPriorityClass;
        final java.lang.String mTag;
        final int mUid;
        final long mWhenElapsed;
        final android.os.WorkSource mWorkSource;

        InFlight(com.android.server.alarm.AlarmManagerService service, com.android.server.alarm.Alarm alarm, long nowELAPSED) {
            com.android.server.alarm.AlarmManagerService.BroadcastStats statsLocked;
            this.mPendingIntent = alarm.operation;
            this.mWhenElapsed = nowELAPSED;
            this.mListener = alarm.listener != null ? alarm.listener.asBinder() : null;
            this.mWorkSource = alarm.workSource;
            this.mUid = alarm.uid;
            this.mCreatorUid = alarm.creatorUid;
            this.mTag = alarm.statsTag;
            if (alarm.operation != null) {
                statsLocked = service.getStatsLocked(alarm.operation);
            } else {
                statsLocked = service.getStatsLocked(alarm.uid, alarm.packageName);
            }
            this.mBroadcastStats = statsLocked;
            com.android.server.alarm.AlarmManagerService.FilterStats fs = this.mBroadcastStats.filterStats.get(this.mTag);
            if (fs == null) {
                fs = new com.android.server.alarm.AlarmManagerService.FilterStats(this.mBroadcastStats, this.mTag);
                this.mBroadcastStats.filterStats.put(this.mTag, fs);
            }
            fs.lastTime = nowELAPSED;
            this.mFilterStats = fs;
            this.mAlarmType = alarm.type;
            this.mPriorityClass = alarm.priorityClass;
        }

        boolean isBroadcast() {
            return this.mPendingIntent != null && this.mPendingIntent.isBroadcast();
        }

        public java.lang.String toString() {
            return "InFlight{pendingIntent=" + this.mPendingIntent + ", when=" + this.mWhenElapsed + ", workSource=" + this.mWorkSource + ", uid=" + this.mUid + ", creatorUid=" + this.mCreatorUid + ", tag=" + this.mTag + ", broadcastStats=" + this.mBroadcastStats + ", filterStats=" + this.mFilterStats + ", alarmType=" + this.mAlarmType + ", priorityClass=" + this.mPriorityClass + "}";
        }

        public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, this.mUid);
            proto.write(1138166333442L, this.mTag);
            proto.write(1112396529667L, this.mWhenElapsed);
            proto.write(1159641169924L, this.mAlarmType);
            if (this.mPendingIntent != null) {
                this.mPendingIntent.dumpDebug(proto, 1146756268037L);
            }
            if (this.mBroadcastStats != null) {
                this.mBroadcastStats.dumpDebug(proto, 1146756268038L);
            }
            if (this.mFilterStats != null) {
                this.mFilterStats.dumpDebug(proto, 1146756268039L);
            }
            if (this.mWorkSource != null) {
                this.mWorkSource.dumpDebug(proto, 1146756268040L);
            }
            proto.end(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyBroadcastAlarmPendingLocked(int uid) {
        int numListeners = this.mInFlightListeners.size();
        for (int i = 0; i < numListeners; i++) {
            this.mInFlightListeners.get(i).broadcastAlarmPending(uid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyBroadcastAlarmCompleteLocked(int uid) {
        int numListeners = this.mInFlightListeners.size();
        for (int i = 0; i < numListeners; i++) {
            this.mInFlightListeners.get(i).broadcastAlarmComplete(uid);
        }
    }

    static final class FilterStats {
        long aggregateTime;
        int count;
        long lastTime;
        final com.android.server.alarm.AlarmManagerService.BroadcastStats mBroadcastStats;
        com.android.server.alarm.IAlarmManagerServiceFilterStatsExt mFilterStatsExt = (com.android.server.alarm.IAlarmManagerServiceFilterStatsExt) system.ext.loader.core.ExtLoader.type(com.android.server.alarm.IAlarmManagerServiceFilterStatsExt.class).base(this).create();
        final java.lang.String mTag;
        int nesting;
        int numWakeup;
        long startTime;

        FilterStats(com.android.server.alarm.AlarmManagerService.BroadcastStats broadcastStats, java.lang.String tag) {
            this.mBroadcastStats = broadcastStats;
            this.mTag = tag;
        }

        public java.lang.String toString() {
            return "FilterStats{tag=" + this.mTag + ", lastTime=" + this.lastTime + ", aggregateTime=" + this.aggregateTime + ", count=" + this.count + ", numWakeup=" + this.numWakeup + ", startTime=" + this.startTime + ", nesting=" + this.nesting + "}";
        }

        public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1138166333441L, this.mTag);
            proto.write(1112396529666L, this.lastTime);
            proto.write(1112396529667L, this.aggregateTime);
            proto.write(1120986464260L, this.count);
            proto.write(1120986464261L, this.numWakeup);
            proto.write(1112396529670L, this.startTime);
            proto.write(1120986464263L, this.nesting);
            proto.end(token);
        }
    }

    public static final class BroadcastStats {
        long aggregateTime;
        int count;
        final android.util.ArrayMap<java.lang.String, com.android.server.alarm.AlarmManagerService.FilterStats> filterStats = new android.util.ArrayMap<>();
        com.android.server.alarm.IAlarmManagerServiceBroadcastStatsExt mBroadcastStatsExt = (com.android.server.alarm.IAlarmManagerServiceBroadcastStatsExt) system.ext.loader.core.ExtLoader.type(com.android.server.alarm.IAlarmManagerServiceBroadcastStatsExt.class).base(this).create();
        final java.lang.String mPackageName;
        final int mUid;
        int nesting;
        int numWakeup;
        long startTime;

        BroadcastStats(int uid, java.lang.String packageName) {
            this.mUid = uid;
            this.mPackageName = packageName;
        }

        public java.lang.String toString() {
            return "BroadcastStats{uid=" + this.mUid + ", packageName=" + this.mPackageName + ", aggregateTime=" + this.aggregateTime + ", count=" + this.count + ", numWakeup=" + this.numWakeup + ", startTime=" + this.startTime + ", nesting=" + this.nesting + "}";
        }

        public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, this.mUid);
            proto.write(1138166333442L, this.mPackageName);
            proto.write(1112396529667L, this.aggregateTime);
            proto.write(1120986464260L, this.count);
            proto.write(1120986464261L, this.numWakeup);
            proto.write(1112396529670L, this.startTime);
            proto.write(1120986464263L, this.nesting);
            proto.end(token);
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.mInjector.init();
        this.mAmsExt.init(this.mInjector.mContext, this.mInjector.mNativeData);
        this.mHandler = new com.android.server.alarm.AlarmManagerService.AlarmHandler();
        this.mOptsWithFgs.setPendingIntentBackgroundActivityLaunchAllowed(false);
        this.mOptsWithFgsForAlarmClock.setPendingIntentBackgroundActivityLaunchAllowed(false);
        this.mOptsWithoutFgs.setPendingIntentBackgroundActivityLaunchAllowed(false);
        this.mOptsTimeBroadcast.setPendingIntentBackgroundActivityLaunchAllowed(false);
        this.mActivityOptsRestrictBal.setPendingIntentBackgroundActivityLaunchAllowed(false);
        this.mBroadcastOptsRestrictBal.setPendingIntentBackgroundActivityLaunchAllowed(false);
        this.mMetricsHelper = new com.android.server.alarm.MetricsHelper(getContext(), this.mLock);
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mUseFrozenStateToDropListenerAlarms = com.android.server.alarm.Flags.useFrozenStateToDropListenerAlarms();
        this.mStartUserBeforeScheduledAlarms = com.android.server.alarm.Flags.startUserBeforeScheduledAlarms();
        if (this.mStartUserBeforeScheduledAlarms) {
            this.mUserWakeupStore = new com.android.server.alarm.UserWakeupStore();
            this.mUserWakeupStore.init();
        }
        if (this.mUseFrozenStateToDropListenerAlarms) {
            android.app.ActivityManager.UidFrozenStateChangedCallback callback = new android.app.ActivityManager.UidFrozenStateChangedCallback() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda2
                public final void onUidFrozenStateChanged(int[] iArr, int[] iArr2) {
                    this.f$0.lambda$onStart$6(iArr, iArr2);
                }
            };
            android.app.ActivityManager am = (android.app.ActivityManager) getContext().getSystemService(android.app.ActivityManager.class);
            am.registerUidFrozenStateChangedCallback(new android.os.HandlerExecutor(this.mHandler), callback);
        }
        this.mListenerDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.alarm.AlarmManagerService.1
            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
            }

            @Override // android.os.IBinder.DeathRecipient
            public void binderDied(android.os.IBinder who) {
                android.app.IAlarmListener listener = android.app.IAlarmListener.Stub.asInterface(who);
                synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                    com.android.server.alarm.AlarmManagerService.this.removeLocked(null, listener, 5);
                }
            }
        };
        synchronized (this.mLock) {
            this.mConstants = new com.android.server.alarm.AlarmManagerService.Constants(this.mHandler);
            this.mAlarmStore = new com.android.server.alarm.LazyAlarmStore();
            this.mAlarmStore.setAlarmClockRemovalListener(this.mAlarmClockUpdater);
            this.mAppWakeupHistory = new com.android.server.alarm.AlarmManagerService.AppWakeupHistory(3600000L);
            this.mAllowWhileIdleHistory = new com.android.server.alarm.AlarmManagerService.AppWakeupHistory(3600000L);
            this.mAllowWhileIdleCompatHistory = new com.android.server.alarm.AlarmManagerService.AppWakeupHistory(3600000L);
            this.mTemporaryQuotaReserve = new com.android.server.alarm.AlarmManagerService.TemporaryQuotaReserve(86400000L);
            this.mNextNonWakeup = 0L;
            this.mNextWakeup = 0L;
            this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            this.mSystemUiUid = this.mInjector.getSystemUiUid(this.mPackageManagerInternal);
            if (this.mSystemUiUid <= 0) {
                android.util.Slog.wtf(TAG, "SysUI package not found!");
            }
            this.mWakeLock = this.mInjector.getAlarmWakeLock();
            this.mTimeTickIntent = new android.content.Intent("android.intent.action.TIME_TICK").addFlags(1344274432);
            this.mTimeTickOptions = android.app.BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).setDeferralPolicy(2).toBundle();
            this.mTimeTickTrigger = new com.android.server.alarm.AlarmManagerService.AnonymousClass2();
            android.content.Intent intent = new android.content.Intent("android.intent.action.DATE_CHANGED");
            intent.addFlags(538968064);
            this.mDateChangeSender = android.app.PendingIntent.getBroadcastAsUser(getContext(), 0, intent, 67108864, android.os.UserHandle.ALL);
            this.mClockReceiver = this.mInjector.getClockReceiver(this);
            new com.android.server.alarm.AlarmManagerService.ChargingReceiver();
            new com.android.server.alarm.AlarmManagerService.InteractiveStateReceiver();
            new com.android.server.alarm.AlarmManagerService.UninstallReceiver();
            if (this.mInjector.isAlarmDriverPresent()) {
                com.android.server.alarm.AlarmManagerService.AlarmThread waitThread = new com.android.server.alarm.AlarmManagerService.AlarmThread();
                waitThread.start();
            } else {
                android.util.Slog.w(TAG, "Failed to open alarm driver. Falling back to a handler.");
            }
        }
        publishLocalService(com.android.server.AlarmManagerInternal.class, new com.android.server.alarm.AlarmManagerService.LocalService());
        publishBinderService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM, this.mService);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$6(int[] uids, int[] frozenStates) {
        int size = frozenStates.length;
        if (uids.length != size) {
            android.util.Slog.wtf(TAG, "Got different length arrays in frozen state callback! uids.length: " + uids.length + " frozenStates.length: " + size);
            return;
        }
        android.util.IntArray affectedUids = new android.util.IntArray();
        for (int i = 0; i < size; i++) {
            if (frozenStates[i] == 1 && android.app.compat.CompatChanges.isChangeEnabled(265195908L, uids[i])) {
                affectedUids.add(uids[i]);
            }
        }
        int i2 = affectedUids.size();
        if (i2 > 0) {
            removeExactListenerAlarms(affectedUids.toArray());
        }
    }

    /* JADX INFO: renamed from: com.android.server.alarm.AlarmManagerService$2, reason: invalid class name */
    class AnonymousClass2 extends android.app.IAlarmListener.Stub {
        AnonymousClass2() {
        }

        public void doAlarm(final android.app.IAlarmCompleteListener callback) throws java.lang.Throwable {
            com.android.server.alarm.AlarmManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.alarm.AlarmManagerService$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$doAlarm$0(callback);
                }
            });
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.this.mLastTickReceived = com.android.server.alarm.AlarmManagerService.this.mInjector.getCurrentTimeMillis();
            }
            com.android.server.alarm.AlarmManagerService.this.mClockReceiver.scheduleTimeTickEvent();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public /* synthetic */ void lambda$doAlarm$0(android.app.IAlarmCompleteListener callback) {
            com.android.server.alarm.AlarmManagerService.this.getContext().sendBroadcastAsUser(com.android.server.alarm.AlarmManagerService.this.mTimeTickIntent, android.os.UserHandle.ALL, null, com.android.server.alarm.AlarmManagerService.this.mTimeTickOptions);
            try {
                callback.alarmComplete(this);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeExactListenerAlarms(final int... whichUids) {
        synchronized (this.mLock) {
            removeAlarmsInternalLocked(new java.util.function.Predicate() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.alarm.AlarmManagerService.lambda$removeExactListenerAlarms$7(whichUids, (com.android.server.alarm.Alarm) obj);
                }
            }, 6);
        }
    }

    static /* synthetic */ boolean lambda$removeExactListenerAlarms$7(int[] whichUids, com.android.server.alarm.Alarm a) {
        if (!com.android.internal.util.ArrayUtils.contains(whichUids, a.uid) || a.listener == null || a.windowLength != 0) {
            return false;
        }
        android.util.Slog.w(TAG, "Alarm " + a.listenerTag + " being removed for " + android.os.UserHandle.formatUid(a.uid) + ":" + a.packageName + " because the app got frozen");
        return true;
    }

    void refreshExactAlarmCandidates() {
        java.lang.String[] candidates = this.mLocalPermissionManager.getAppOpPermissionPackages("android.permission.SCHEDULE_EXACT_ALARM");
        java.util.Set<java.lang.Integer> newAppIds = new android.util.ArraySet<>(candidates.length);
        for (java.lang.String candidate : candidates) {
            int uid = this.mPackageManagerInternal.getPackageUid(candidate, 4194304L, 0);
            if (uid > 0) {
                newAppIds.add(java.lang.Integer.valueOf(android.os.UserHandle.getAppId(uid)));
            }
        }
        this.mExactAlarmCandidates = java.util.Collections.unmodifiableSet(newAppIds);
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
        super.onUserStarting(user);
        final int userId = user.getUserIdentifier();
        if (this.mStartUserBeforeScheduledAlarms) {
            this.mUserWakeupStore.onUserStarting(userId);
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onUserStarting$8(userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserStarting$8(int userId) {
        java.util.Iterator<java.lang.Integer> it = this.mExactAlarmCandidates.iterator();
        while (it.hasNext()) {
            int appId = it.next().intValue();
            int uid = android.os.UserHandle.getUid(userId, appId);
            com.android.server.pm.pkg.AndroidPackage androidPackage = this.mPackageManagerInternal.getPackage(uid);
            if (androidPackage != null) {
                int mode = this.mAppOps.checkOpNoThrow(107, uid, androidPackage.getPackageName());
                synchronized (this.mLock) {
                    this.mLastOpScheduleExactAlarm.put(uid, mode);
                }
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            synchronized (this.mLock) {
                this.mConstants.start();
                this.mAppOps = (android.app.AppOpsManager) getContext().getSystemService("appops");
                this.mLocalDeviceIdleController = (com.android.server.DeviceIdleInternal) com.android.server.LocalServices.getService(com.android.server.DeviceIdleInternal.class);
                this.mUsageStatsManagerInternal = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
                this.mAppStateTracker = (com.android.server.AppStateTrackerImpl) com.android.server.LocalServices.getService(com.android.server.AppStateTracker.class);
                this.mAppStateTracker.addListener(this.mForceAppStandbyListener);
                android.os.BatteryManager bm = (android.os.BatteryManager) getContext().getSystemService(android.os.BatteryManager.class);
                this.mAppStandbyParole = bm.isCharging();
                this.mClockReceiver.scheduleTimeTickEvent();
                this.mClockReceiver.scheduleDateChangedEvent();
                this.mAmsExt.systemServiceReady();
            }
            com.android.internal.app.IAppOpsService iAppOpsService = this.mInjector.getAppOpsService();
            try {
                iAppOpsService.startWatchingMode(107, (java.lang.String) null, new com.android.internal.app.IAppOpsCallback.Stub() { // from class: com.android.server.alarm.AlarmManagerService.3
                    public void opChanged(int op, int uid, java.lang.String packageName, java.lang.String persistentDeviceId) throws android.os.RemoteException {
                        int oldMode;
                        boolean hadPermission;
                        boolean hasPermission;
                        int userId = android.os.UserHandle.getUserId(uid);
                        if (op == 107 && com.android.server.alarm.AlarmManagerService.isExactAlarmChangeEnabled(packageName, userId) && !com.android.server.alarm.AlarmManagerService.this.hasUseExactAlarmInternal(packageName, uid) && com.android.server.alarm.AlarmManagerService.this.mExactAlarmCandidates.contains(java.lang.Integer.valueOf(android.os.UserHandle.getAppId(uid)))) {
                            int newMode = com.android.server.alarm.AlarmManagerService.this.mAppOps.checkOpNoThrow(107, uid, packageName);
                            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                                int index = com.android.server.alarm.AlarmManagerService.this.mLastOpScheduleExactAlarm.indexOfKey(uid);
                                if (index < 0) {
                                    oldMode = android.app.AppOpsManager.opToDefaultMode(107);
                                    com.android.server.alarm.AlarmManagerService.this.mLastOpScheduleExactAlarm.put(uid, newMode);
                                } else {
                                    oldMode = com.android.server.alarm.AlarmManagerService.this.mLastOpScheduleExactAlarm.valueAt(index);
                                    com.android.server.alarm.AlarmManagerService.this.mLastOpScheduleExactAlarm.setValueAt(index, newMode);
                                }
                            }
                            if (oldMode == newMode) {
                                return;
                            }
                            boolean deniedByDefault = com.android.server.alarm.AlarmManagerService.this.isScheduleExactAlarmDeniedByDefault(packageName, android.os.UserHandle.getUserId(uid));
                            boolean z = true;
                            if (deniedByDefault) {
                                boolean permissionState = com.android.server.alarm.AlarmManagerService.this.getContext().checkPermission("android.permission.SCHEDULE_EXACT_ALARM", -1, uid) == 0;
                                if (oldMode == 3) {
                                    hadPermission = permissionState;
                                } else {
                                    hadPermission = oldMode == 0;
                                }
                                if (newMode == 3) {
                                    z = permissionState;
                                } else if (newMode != 0) {
                                    z = false;
                                }
                                hasPermission = z;
                            } else {
                                hadPermission = oldMode == 3 || oldMode == 0;
                                if (newMode != 3 && newMode != 0) {
                                    z = false;
                                }
                                hasPermission = z;
                            }
                            if (hadPermission && !hasPermission) {
                                com.android.server.alarm.AlarmManagerService.this.mHandler.obtainMessage(8, uid, 0, packageName).sendToTarget();
                            } else if (!hadPermission && hasPermission) {
                                com.android.server.alarm.AlarmManagerService.this.sendScheduleExactAlarmPermissionStateChangedBroadcast(packageName, userId);
                            }
                        }
                    }
                });
            } catch (android.os.RemoteException e) {
            }
            this.mLocalPermissionManager = (com.android.server.pm.permission.PermissionManagerServiceInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.PermissionManagerServiceInternal.class);
            refreshExactAlarmCandidates();
            com.android.server.usage.AppStandbyInternal appStandbyInternal = (com.android.server.usage.AppStandbyInternal) com.android.server.LocalServices.getService(com.android.server.usage.AppStandbyInternal.class);
            appStandbyInternal.addListener(new com.android.server.alarm.AlarmManagerService.AppStandbyTracker());
            this.mBatteryStatsInternal = (android.os.BatteryStatsInternal) com.android.server.LocalServices.getService(android.os.BatteryStatsInternal.class);
            this.mRoleManager = (android.app.role.RoleManager) getContext().getSystemService(android.app.role.RoleManager.class);
            this.mMetricsHelper.registerPuller(new java.util.function.Supplier() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda1
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$onBootPhase$9();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.alarm.AlarmStore lambda$onBootPhase$9() {
        return this.mAlarmStore;
    }

    protected void finalize() throws java.lang.Throwable {
        try {
            this.mInjector.close();
        } finally {
            super.finalize();
        }
    }

    boolean setTimeImpl(long newSystemClockTimeMillis, int confidence, java.lang.String logMsg) {
        synchronized (this.mLock) {
            long oldSystemClockTimeMillis = this.mInjector.getCurrentTimeMillis();
            android.util.Slog.d(TAG, "setCurrentTimeMillis oldSystemClockTimeMillis=" + oldSystemClockTimeMillis + ", newSystemClockTimeMillis=" + newSystemClockTimeMillis);
            this.mAmsExt.printStackTraceInfo();
            com.oplus.android.internal.util.OplusFrameworkStatsLog.write(100033, oldSystemClockTimeMillis, newSystemClockTimeMillis);
            this.mInjector.setCurrentTimeMillis(newSystemClockTimeMillis, confidence, logMsg);
        }
        return true;
    }

    void setTimeZoneImpl(java.lang.String tzId, int confidence, java.lang.String logInfo) throws java.lang.Throwable {
        boolean timeZoneWasChanged;
        if (android.text.TextUtils.isEmpty(tzId)) {
            return;
        }
        java.util.TimeZone newZone = java.util.TimeZone.getTimeZone(tzId);
        synchronized (this) {
            timeZoneWasChanged = com.android.server.SystemTimeZone.setTimeZoneId(tzId, confidence, logInfo);
            int gmtOffset = newZone.getOffset(this.mInjector.getCurrentTimeMillis());
            android.os.SystemProperties.set(TIMEOFFSET_PROPERTY, java.lang.String.valueOf(gmtOffset));
            java.time.zone.ZoneRules rules = newZone.toZoneId().getRules();
            java.time.zone.ZoneOffsetTransition transition = rules.nextTransition(java.time.Instant.now());
            if (transition != null) {
                long transitionOffset = java.util.concurrent.TimeUnit.SECONDS.toMillis(transition.getOffsetAfter().getTotalSeconds() - transition.getOffsetBefore().getTotalSeconds());
                long nextTransition = java.util.concurrent.TimeUnit.SECONDS.toMillis(transition.toEpochSecond());
                android.os.SystemProperties.set(DST_TRANSITION_PROPERTY, java.lang.String.valueOf(nextTransition));
                android.os.SystemProperties.set(DST_OFFSET_PROPERTY, java.lang.String.valueOf(transitionOffset));
            }
        }
        java.util.TimeZone.setDefault(null);
        if (timeZoneWasChanged) {
            this.mClockReceiver.scheduleDateChangedEvent();
            android.content.Intent intent = new android.content.Intent("android.intent.action.TIMEZONE_CHANGED");
            intent.addFlags(622854144);
            intent.putExtra("time-zone", newZone.getID());
            this.mOptsTimeBroadcast.setTemporaryAppAllowlist(this.mActivityManagerInternal.getBootTimeTempAllowListDuration(), 0, 204, "");
            this.mOptsTimeBroadcast.setDeliveryGroupPolicy(1);
            getContext().sendBroadcastAsUser(intent, android.os.UserHandle.ALL, null, this.mOptsTimeBroadcast.toBundle());
        }
    }

    void removeImpl(android.app.PendingIntent operation, android.app.IAlarmListener listener) {
        synchronized (this.mLock) {
            removeLocked(operation, listener, 0);
        }
    }

    void setImpl(int type, long triggerAtTime, long windowLength, long interval, android.app.PendingIntent operation, android.app.IAlarmListener directReceiver, java.lang.String listenerTag, int flags, android.os.WorkSource workSource, android.app.AlarmManager.AlarmClockInfo alarmClock, int callingUid, java.lang.String callingPackage, android.os.Bundle idleOptions, int exactAllowReason) throws java.lang.Throwable {
        int flags2;
        long windowLength2;
        long windowLength3;
        long interval2;
        long triggerAtTime2;
        long triggerAtTime3;
        long interval3;
        long windowLength4;
        android.app.PendingIntent pendingIntent;
        long triggerElapsed;
        java.lang.String str;
        long nowElapsed;
        long windowLength5;
        long nowElapsed2;
        long minAllowedWindow;
        java.lang.Object obj;
        long windowLength6;
        long interval4;
        long triggerAtTime4;
        if ((operation == null && directReceiver == null) || (operation != null && directReceiver != null)) {
            android.util.Slog.w(TAG, "Alarms must either supply a PendingIntent or an AlarmReceiver");
            return;
        }
        java.lang.String[] actionComponent = this.mAmsExt.getActionComponent(operation);
        long windowLength7 = this.mAmsExt.adjustWindowLengthsWhenSetImpl(operation, callingPackage, flags, alarmClock, windowLength, actionComponent[0], listenerTag);
        int flags3 = this.mAmsExt.adjustAlarmFlagsWhenSetImpl(callingPackage, flags, windowLength7, callingUid);
        if (this.mAmsExt.shouldAdjustForDualApps(callingPackage, actionComponent[0])) {
            long windowLength8 = this.mAmsExt.adjustAlarmWindowLengthForDualApps(windowLength7);
            flags2 = this.mAmsExt.adjustAlarmFlagsForDualApps(flags3);
            windowLength2 = windowLength8;
        } else {
            flags2 = flags3;
            windowLength2 = windowLength7;
        }
        if (directReceiver != null) {
            try {
                directReceiver.asBinder().linkToDeath(this.mListenerDeathRecipient, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Dropping unreachable alarm listener " + listenerTag);
                return;
            }
        }
        long minInterval = this.mConstants.MIN_INTERVAL;
        if (interval <= 0 || interval >= minInterval) {
            windowLength3 = windowLength2;
            if (interval > this.mConstants.MAX_INTERVAL) {
                android.util.Slog.w(TAG, "Suspiciously long interval " + interval + " millis; clamping");
                interval2 = this.mConstants.MAX_INTERVAL;
            } else {
                interval2 = interval;
            }
        } else {
            windowLength3 = windowLength2;
            long windowLength9 = minInterval / 1000;
            android.util.Slog.w(TAG, "Suspiciously short interval " + interval + " millis; expanding to " + windowLength9 + " seconds");
            interval2 = minInterval;
        }
        if ((type < 0 || type > 3) && !this.mAmsExt.isPowerOffAlarmType(type)) {
            throw new java.lang.IllegalArgumentException("Invalid alarm type " + type);
        }
        if (triggerAtTime < 0) {
            long what = android.os.Binder.getCallingPid();
            android.util.Slog.w(TAG, "Invalid alarm trigger time! " + triggerAtTime + " from uid=" + callingUid + " pid=" + what);
            triggerAtTime2 = 0;
        } else {
            triggerAtTime2 = triggerAtTime;
        }
        if (operation != null) {
            triggerAtTime3 = triggerAtTime2;
            interval3 = interval2;
            windowLength4 = windowLength3;
            if (!this.mAmsExt.schedulePoweroffAlarm(type, triggerAtTime2, interval2, operation, directReceiver, listenerTag, workSource, alarmClock, callingPackage)) {
                return;
            }
        } else {
            triggerAtTime3 = triggerAtTime2;
            interval3 = interval2;
            windowLength4 = windowLength3;
        }
        int type2 = this.mAmsExt.isPowerOffAlarmType(type) ? 0 : type;
        long nowElapsed3 = this.mInjector.getElapsedRealtimeMillis();
        long triggerAtTime5 = triggerAtTime3;
        long nominalTrigger = convertToElapsed(triggerAtTime5, type2);
        long minTrigger = nowElapsed3 + (android.os.UserHandle.isCore(callingUid) ? 0L : this.mConstants.MIN_FUTURITY);
        long triggerElapsed2 = java.lang.Math.max(minTrigger, nominalTrigger);
        long nominalTrigger2 = windowLength4;
        if (nominalTrigger2 == 0) {
            str = callingPackage;
            pendingIntent = operation;
            minAllowedWindow = nominalTrigger2;
            triggerElapsed = triggerElapsed2;
            nowElapsed2 = triggerElapsed2;
            nowElapsed = nowElapsed3;
        } else if (nominalTrigger2 < 0) {
            pendingIntent = operation;
            long maxElapsed = maxTriggerTime(nowElapsed3, triggerElapsed2, interval3);
            triggerElapsed = triggerElapsed2;
            minAllowedWindow = maxElapsed - triggerElapsed;
            str = callingPackage;
            nowElapsed2 = maxElapsed;
            nowElapsed = nowElapsed3;
        } else {
            pendingIntent = operation;
            triggerElapsed = triggerElapsed2;
            long minAllowedWindow2 = getMinimumAllowedWindow(nowElapsed3, triggerElapsed);
            if (nominalTrigger2 > 86400000) {
                android.util.Slog.w(TAG, "Window length " + nominalTrigger2 + "ms too long; limiting to 1 day");
                windowLength5 = 86400000;
                str = callingPackage;
                nowElapsed = nowElapsed3;
            } else {
                if ((flags2 & 64) != 0 || nominalTrigger2 >= minAllowedWindow2 || isExemptFromMinWindowRestrictions(callingUid)) {
                    str = callingPackage;
                    nowElapsed = nowElapsed3;
                } else {
                    nowElapsed = nowElapsed3;
                    str = callingPackage;
                    if (android.app.compat.CompatChanges.isChangeEnabled(185199076L, str, android.os.UserHandle.getUserHandleForUid(callingUid))) {
                        android.util.Slog.w(TAG, "Window length " + nominalTrigger2 + "ms too short; expanding to " + minAllowedWindow2 + "ms.");
                        windowLength5 = minAllowedWindow2;
                    }
                }
                windowLength5 = nominalTrigger2;
            }
            nowElapsed2 = triggerElapsed + windowLength5;
            minAllowedWindow = windowLength5;
        }
        java.lang.Object obj2 = this.mLock;
        synchronized (obj2) {
            try {
                try {
                    if (this.mAmsExt.isDynamicLogEnabled()) {
                        try {
                            java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("set(").append(pendingIntent).append(") : type=").append(type2).append(" triggerAtTime=").append(triggerAtTime5).append(" win=").append(minAllowedWindow).append(" tElapsed=").append(triggerElapsed).append(" maxElapsed=").append(nowElapsed2).append(" interval=");
                            windowLength6 = minAllowedWindow;
                            interval4 = interval3;
                            try {
                                try {
                                    android.util.Slog.v(TAG, sbAppend.append(interval4).append(" flags=0x").append(java.lang.Integer.toHexString(flags2)).append(" callingPackage=").append(str).append(" listenerTag=").append(listenerTag).toString());
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    obj = obj2;
                                    throw th;
                                }
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                obj = obj2;
                            }
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                            obj = obj2;
                        }
                    } else {
                        windowLength6 = minAllowedWindow;
                        interval4 = interval3;
                    }
                    try {
                        if (this.mAlarmsPerUid.get(callingUid, 0) >= this.mConstants.MAX_ALARMS_PER_UID) {
                            try {
                                this.mAmsExt.onAlarmInfoCollect(callingUid, str, this.mAlarmStore);
                                this.mAmsExt.maxAlarmsPerUidHandle(str, callingUid, this.mConstants.MAX_ALARMS_PER_UID);
                                java.lang.String errorMsg = "Maximum limit of concurrent alarms " + this.mConstants.MAX_ALARMS_PER_UID + " reached for uid: " + android.os.UserHandle.formatUid(callingUid) + ", callingPackage: " + str;
                                android.util.Slog.w(TAG, errorMsg);
                                try {
                                    if (callingUid != 1000) {
                                        throw new java.lang.IllegalStateException(errorMsg);
                                    }
                                    triggerAtTime4 = triggerAtTime5;
                                    android.util.EventLog.writeEvent(1397638484, "234441463", -1, errorMsg);
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                    obj = obj2;
                                    throw th;
                                }
                            } catch (java.lang.Throwable th5) {
                                th = th5;
                                obj = obj2;
                            }
                        } else {
                            triggerAtTime4 = triggerAtTime5;
                        }
                        try {
                            setImplLocked(type2, triggerAtTime4, triggerElapsed, windowLength6, interval4, operation, directReceiver, listenerTag, flags2, workSource, alarmClock, callingUid, callingPackage, this.mAmsExt.getProcessName(android.os.Binder.getCallingPid()), idleOptions, exactAllowReason, actionComponent[0], actionComponent[1]);
                        } catch (java.lang.Throwable th6) {
                            th = th6;
                            obj = obj2;
                            throw th;
                        }
                    } catch (java.lang.Throwable th7) {
                        th = th7;
                        obj = obj2;
                    }
                } catch (java.lang.Throwable th8) {
                    th = th8;
                }
            } catch (java.lang.Throwable th9) {
                th = th9;
                obj = obj2;
            }
        }
    }

    private void setImplLocked(int type, long when, long whenElapsed, long windowLength, long interval, android.app.PendingIntent operation, android.app.IAlarmListener directReceiver, java.lang.String listenerTag, int flags, android.os.WorkSource workSource, android.app.AlarmManager.AlarmClockInfo alarmClock, int callingUid, java.lang.String callingPackage, java.lang.String callingProcName, android.os.Bundle idleOptions, int exactAllowReason, java.lang.String action, java.lang.String component) {
        int type2;
        if (operation == null) {
            type2 = type;
        } else {
            type2 = this.mAmsExt.SyncAlarmHandleOnSetImplLocked(type, operation, action, component);
        }
        com.android.server.alarm.Alarm a = new com.android.server.alarm.Alarm(type2, when, whenElapsed, windowLength, interval, operation, directReceiver, listenerTag, workSource, flags, alarmClock, callingUid, callingPackage, idleOptions, exactAllowReason);
        a.getWrapper().getExt().init(callingProcName, action, component, a.statsTag);
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault());
        com.oplus.android.internal.util.OplusFrameworkStatsLog.write(100059, callingPackage, callingUid, android.os.Binder.getCallingPid(), type2, df.format(java.lang.Long.valueOf(when)), listenerTag);
        this.mAmsExt.trackEventSetAlarmLocked(a);
        if (1000 != callingUid) {
            if (!this.mActivityManagerInternal.isAppStartModeDisabled(callingUid, callingPackage)) {
                if (this.mAmsExt.updateHeartBeatPolicy(a, this.mInteractive)) {
                    android.util.Slog.w(TAG, "heartbeat interval too small " + a);
                    return;
                }
            } else {
                android.util.Slog.w(TAG, "Not setting alarm from " + callingUid + ":" + a + " -- package not allowed to start");
                return;
            }
        }
        int callerProcState = this.mActivityManagerInternal.getUidProcessState(callingUid);
        removeLocked(operation, directReceiver, 0);
        incrementAlarmCount(a.uid);
        setImplLocked(a);
        com.android.server.alarm.MetricsHelper.pushAlarmScheduled(a, callerProcState);
    }

    int getQuotaForBucketLocked(int bucket) {
        int index;
        if (bucket <= 10) {
            index = 0;
        } else if (bucket <= 20) {
            index = 1;
        } else if (bucket <= 30) {
            index = 2;
        } else if (bucket < 50) {
            index = 3;
        } else {
            index = 4;
        }
        return this.mConstants.APP_STANDBY_QUOTAS[index];
    }

    private boolean adjustIdleUntilTime(com.android.server.alarm.Alarm alarm) {
        if ((alarm.flags & 16) == 0) {
            return false;
        }
        boolean changedBeforeFuzz = restoreRequestedTime(alarm);
        if (this.mNextWakeFromIdle == null) {
            return changedBeforeFuzz;
        }
        long upcomingWakeFromIdle = this.mNextWakeFromIdle.getWhenElapsed();
        if (alarm.getWhenElapsed() < upcomingWakeFromIdle - this.mConstants.MIN_DEVICE_IDLE_FUZZ) {
            return changedBeforeFuzz;
        }
        long nowElapsed = this.mInjector.getElapsedRealtimeMillis();
        long futurity = upcomingWakeFromIdle - nowElapsed;
        if (futurity <= this.mConstants.MIN_DEVICE_IDLE_FUZZ) {
            alarm.setPolicyElapsed(0, nowElapsed);
            return true;
        }
        java.util.concurrent.ThreadLocalRandom random = java.util.concurrent.ThreadLocalRandom.current();
        long upperBoundExcl = java.lang.Math.min(this.mConstants.MAX_DEVICE_IDLE_FUZZ, futurity) + 1;
        long fuzz = random.nextLong(this.mConstants.MIN_DEVICE_IDLE_FUZZ, upperBoundExcl);
        alarm.setPolicyElapsed(0, upcomingWakeFromIdle - fuzz);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean adjustDeliveryTimeBasedOnBatterySaver(com.android.server.alarm.Alarm alarm) {
        long batterySaverPolicyElapsed;
        int quota;
        long window;
        com.android.server.alarm.AlarmManagerService.AppWakeupHistory history;
        long nowElapsed = this.mInjector.getElapsedRealtimeMillis();
        if (isExemptFromBatterySaver(alarm)) {
            return false;
        }
        if (this.mAppStateTracker == null || !this.mAppStateTracker.areAlarmsRestrictedByBatterySaver(alarm.creatorUid, alarm.sourcePackage)) {
            return alarm.setPolicyElapsed(3, nowElapsed);
        }
        if ((alarm.flags & 8) != 0) {
            batterySaverPolicyElapsed = nowElapsed;
        } else if (isAllowedWhileIdleRestricted(alarm)) {
            int userId = android.os.UserHandle.getUserId(alarm.creatorUid);
            if ((alarm.flags & 4) != 0) {
                quota = this.mConstants.ALLOW_WHILE_IDLE_QUOTA;
                window = this.mConstants.ALLOW_WHILE_IDLE_WINDOW;
                history = this.mAllowWhileIdleHistory;
            } else {
                quota = this.mConstants.ALLOW_WHILE_IDLE_COMPAT_QUOTA;
                window = this.mConstants.ALLOW_WHILE_IDLE_COMPAT_WINDOW;
                history = this.mAllowWhileIdleCompatHistory;
            }
            int dispatchesInHistory = history.getTotalWakeupsInWindow(alarm.sourcePackage, userId);
            if (dispatchesInHistory < quota) {
                batterySaverPolicyElapsed = nowElapsed;
            } else {
                batterySaverPolicyElapsed = history.getNthLastWakeupForPackage(alarm.sourcePackage, userId, quota) + window;
            }
        } else if ((alarm.flags & 64) != 0) {
            long lastDispatch = this.mLastPriorityAlarmDispatch.get(alarm.creatorUid, 0L);
            if (lastDispatch == 0) {
                batterySaverPolicyElapsed = nowElapsed;
            } else {
                batterySaverPolicyElapsed = this.mConstants.PRIORITY_ALARM_DELAY + lastDispatch;
            }
        } else {
            batterySaverPolicyElapsed = 31536000000L + nowElapsed;
        }
        return alarm.setPolicyElapsed(3, batterySaverPolicyElapsed);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isAllowedWhileIdleRestricted(com.android.server.alarm.Alarm a) {
        return (a.flags & 36) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: adjustDeliveryTimeBasedOnDeviceIdle, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public boolean lambda$triggerAlarmsLocked$22(com.android.server.alarm.Alarm alarm) {
        long deviceIdlePolicyTime;
        long whenAllowed;
        int quota;
        long window;
        com.android.server.alarm.AlarmManagerService.AppWakeupHistory history;
        long nowElapsed = this.mInjector.getElapsedRealtimeMillis();
        if (this.mPendingIdleUntil == null || this.mPendingIdleUntil == alarm) {
            return alarm.setPolicyElapsed(2, nowElapsed);
        }
        if ((alarm.flags & 10) != 0 || this.mAmsExt.isInSmartDozeEearlyTime()) {
            deviceIdlePolicyTime = nowElapsed;
        } else if (isAllowedWhileIdleRestricted(alarm)) {
            int userId = android.os.UserHandle.getUserId(alarm.creatorUid);
            if ((alarm.flags & 4) != 0) {
                quota = this.mConstants.ALLOW_WHILE_IDLE_QUOTA;
                window = this.mConstants.ALLOW_WHILE_IDLE_WINDOW;
                history = this.mAllowWhileIdleHistory;
            } else {
                quota = this.mConstants.ALLOW_WHILE_IDLE_COMPAT_QUOTA;
                window = this.mConstants.ALLOW_WHILE_IDLE_COMPAT_WINDOW;
                history = this.mAllowWhileIdleCompatHistory;
            }
            int dispatchesInHistory = history.getTotalWakeupsInWindow(alarm.sourcePackage, userId);
            if (dispatchesInHistory < quota) {
                deviceIdlePolicyTime = nowElapsed;
            } else {
                long whenInQuota = history.getNthLastWakeupForPackage(alarm.sourcePackage, userId, quota) + window;
                deviceIdlePolicyTime = java.lang.Math.min(whenInQuota, this.mPendingIdleUntil.getWhenElapsed());
            }
        } else if ((alarm.flags & 64) != 0) {
            long lastDispatch = this.mLastPriorityAlarmDispatch.get(alarm.creatorUid, 0L);
            if (lastDispatch == 0) {
                whenAllowed = nowElapsed;
            } else {
                whenAllowed = this.mConstants.PRIORITY_ALARM_DELAY + lastDispatch;
            }
            deviceIdlePolicyTime = java.lang.Math.min(whenAllowed, this.mPendingIdleUntil.getWhenElapsed());
        } else {
            deviceIdlePolicyTime = this.mPendingIdleUntil.getWhenElapsed();
            if (DEBUG_PANIC) {
                android.util.Slog.d(TAG, "deviceidle delay Alarm  " + alarm);
            }
        }
        return alarm.setPolicyElapsed(2, this.mAmsExt.adjDeviceIdlePolicyTime(deviceIdlePolicyTime, this.mPendingIdleUntil.getWhenElapsed(), alarm));
    }

    private boolean adjustDeliveryTimeBasedOnBucketLocked(com.android.server.alarm.Alarm alarm) {
        long t;
        long nowElapsed = this.mInjector.getElapsedRealtimeMillis();
        if (isExemptFromAppStandby(alarm) || this.mAppStandbyParole) {
            return alarm.setPolicyElapsed(1, nowElapsed);
        }
        java.lang.String sourcePackage = alarm.sourcePackage;
        int sourceUserId = android.os.UserHandle.getUserId(alarm.creatorUid);
        int standbyBucket = this.mUsageStatsManagerInternal.getAppStandbyBucket(sourcePackage, sourceUserId, nowElapsed);
        int wakeupsInWindow = this.mAppWakeupHistory.getTotalWakeupsInWindow(sourcePackage, sourceUserId);
        if (standbyBucket == 45) {
            if (wakeupsInWindow > 0) {
                long lastWakeupTime = this.mAppWakeupHistory.getNthLastWakeupForPackage(sourcePackage, sourceUserId, this.mConstants.APP_STANDBY_RESTRICTED_QUOTA);
                if (nowElapsed - lastWakeupTime < this.mConstants.APP_STANDBY_RESTRICTED_WINDOW) {
                    return alarm.setPolicyElapsed(1, this.mConstants.APP_STANDBY_RESTRICTED_WINDOW + lastWakeupTime);
                }
            }
        } else {
            int quotaForBucket = getQuotaForBucketLocked(standbyBucket);
            if (wakeupsInWindow >= quotaForBucket) {
                if (this.mTemporaryQuotaReserve.hasQuota(sourcePackage, sourceUserId, nowElapsed)) {
                    alarm.mUsingReserveQuota = true;
                    return alarm.setPolicyElapsed(1, nowElapsed);
                }
                if (quotaForBucket <= 0) {
                    t = 31536000000L + nowElapsed;
                } else {
                    long t2 = this.mAppWakeupHistory.getNthLastWakeupForPackage(sourcePackage, sourceUserId, quotaForBucket);
                    t = this.mConstants.APP_STANDBY_WINDOW + t2;
                }
                return alarm.setPolicyElapsed(1, t);
            }
        }
        alarm.mUsingReserveQuota = false;
        return alarm.setPolicyElapsed(1, nowElapsed);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImplLocked(com.android.server.alarm.Alarm a) {
        if ((a.flags & 16) != 0) {
            adjustIdleUntilTime(a);
            if (this.mPendingIdleUntil != a && this.mPendingIdleUntil != null) {
                android.util.Slog.wtfStack(TAG, "setImplLocked: idle until changed from " + this.mPendingIdleUntil + " to " + a);
                com.android.server.alarm.AlarmStore alarmStore = this.mAlarmStore;
                final com.android.server.alarm.Alarm alarm = this.mPendingIdleUntil;
                java.util.Objects.requireNonNull(alarm);
                alarmStore.remove(new java.util.function.Predicate() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda9
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return alarm.equals((com.android.server.alarm.Alarm) obj);
                    }
                });
            }
            this.mPendingIdleUntil = a;
            this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda10
                @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm2) {
                    return this.f$0.lambda$setImplLocked$10(alarm2);
                }
            });
        } else if (this.mPendingIdleUntil != null) {
            lambda$triggerAlarmsLocked$22(a);
        }
        if ((a.flags & 2) != 0 && (this.mNextWakeFromIdle == null || this.mNextWakeFromIdle.getWhenElapsed() > a.getWhenElapsed())) {
            this.mNextWakeFromIdle = a;
            if (this.mPendingIdleUntil != null) {
                boolean updated = this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda11
                    @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                    public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm2) {
                        return this.f$0.lambda$setImplLocked$11(alarm2);
                    }
                });
                if (updated) {
                    this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda12
                        @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                        public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm2) {
                            return this.f$0.lambda$setImplLocked$12(alarm2);
                        }
                    });
                }
            }
        }
        if (a.alarmClock != null) {
            this.mNextAlarmClockMayChange = true;
        }
        this.mAmsExt.updateGoogleAlarmTypeAndTag(a);
        adjustDeliveryTimeBasedOnBatterySaver(a);
        adjustDeliveryTimeBasedOnBucketLocked(a);
        this.mAmsExt.adjustAlarmLocked(a, this.mInteractive);
        this.mAlarmStore.add(a);
        rescheduleKernelAlarmsLocked();
        updateNextAlarmClockLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setImplLocked$11(com.android.server.alarm.Alarm alarm) {
        return alarm == this.mPendingIdleUntil && adjustIdleUntilTime(alarm);
    }

    private final class LocalService implements com.android.server.AlarmManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.AlarmManagerInternal
        public boolean isIdling() {
            return com.android.server.alarm.AlarmManagerService.this.isIdlingImpl();
        }

        @Override // com.android.server.AlarmManagerInternal
        public void removeAlarmsForUid(int uid) {
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.this.mAmsExt.removeAlarmsForUidLocked(uid, 3);
            }
        }

        @Override // com.android.server.AlarmManagerInternal
        public void remove(android.app.PendingIntent pi) {
            com.android.server.alarm.AlarmManagerService.this.mHandler.obtainMessage(7, pi).sendToTarget();
        }

        @Override // com.android.server.AlarmManagerInternal
        public boolean shouldGetBucketElevation(java.lang.String packageName, int uid) {
            return com.android.server.alarm.AlarmManagerService.this.hasUseExactAlarmInternal(packageName, uid) || (!android.app.compat.CompatChanges.isChangeEnabled(262645982L, packageName, android.os.UserHandle.getUserHandleForUid(uid)) && com.android.server.alarm.AlarmManagerService.this.hasScheduleExactAlarmInternal(packageName, uid));
        }

        @Override // com.android.server.AlarmManagerInternal
        public void setTimeZone(java.lang.String tzId, int confidence, java.lang.String logInfo) throws java.lang.Throwable {
            com.android.server.alarm.AlarmManagerService.this.setTimeZoneImpl(tzId, confidence, logInfo);
        }

        @Override // com.android.server.AlarmManagerInternal
        public void setTime(long unixEpochTimeMillis, int confidence, java.lang.String logMsg) {
            com.android.server.alarm.AlarmManagerService.this.setTimeImpl(unixEpochTimeMillis, confidence, logMsg);
        }

        @Override // com.android.server.AlarmManagerInternal
        public void registerInFlightListener(com.android.server.AlarmManagerInternal.InFlightListener callback) {
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.this.mInFlightListeners.add(callback);
            }
        }
    }

    boolean hasUseExactAlarmInternal(java.lang.String packageName, int uid) {
        return isUseExactAlarmEnabled(packageName, android.os.UserHandle.getUserId(uid)) && android.content.PermissionChecker.checkPermissionForPreflight(getContext(), "android.permission.USE_EXACT_ALARM", -1, uid, packageName) == 0;
    }

    boolean hasScheduleExactAlarmInternal(java.lang.String packageName, int uid) {
        boolean hasPermission;
        long start = this.mStatLogger.getTime();
        if (!this.mExactAlarmCandidates.contains(java.lang.Integer.valueOf(android.os.UserHandle.getAppId(uid))) || !isExactAlarmChangeEnabled(packageName, android.os.UserHandle.getUserId(uid))) {
            hasPermission = false;
        } else {
            if (isScheduleExactAlarmDeniedByDefault(packageName, android.os.UserHandle.getUserId(uid))) {
                hasPermission = android.content.PermissionChecker.checkPermissionForPreflight(getContext(), "android.permission.SCHEDULE_EXACT_ALARM", -1, uid, packageName) == 0;
            } else {
                int mode = this.mAppOps.checkOpNoThrow(107, uid, packageName);
                hasPermission = mode == 3 || mode == 0;
            }
        }
        this.mStatLogger.logDurationStat(1, start);
        return hasPermission;
    }

    boolean isExemptFromMinWindowRestrictions(int uid) {
        return isExemptFromExactAlarmPermissionNoLock(uid);
    }

    boolean isExemptFromExactAlarmPermissionNoLock(int uid) {
        if (android.os.Build.IS_DEBUGGABLE && java.lang.Thread.holdsLock(this.mLock)) {
            android.util.Slog.wtfStack(TAG, "Alarm lock held while calling into DeviceIdleController");
        }
        return android.os.UserHandle.isSameApp(this.mSystemUiUid, uid) || android.os.UserHandle.isCore(uid) || this.mLocalDeviceIdleController == null || this.mLocalDeviceIdleController.isAppOnWhitelist(android.os.UserHandle.getAppId(uid));
    }

    /* JADX INFO: renamed from: com.android.server.alarm.AlarmManagerService$4, reason: invalid class name */
    class AnonymousClass4 extends android.app.IAlarmManager.Stub {
        AnonymousClass4() {
        }

        public void set(java.lang.String callingPackage, int type, long triggerAtTime, long windowLength, long interval, int flags, android.app.PendingIntent operation, android.app.IAlarmListener directReceiver, java.lang.String listenerTag, android.os.WorkSource workSource, android.app.AlarmManager.AlarmClockInfo alarmClock) throws java.lang.Throwable {
            long windowLength2;
            int flags2;
            long windowLength3;
            boolean needsPermission;
            android.os.Bundle bundle;
            android.os.Bundle idleOptions;
            int exactAllowReason;
            android.os.Bundle idleOptions2;
            android.os.Bundle bundle2;
            int flags3;
            int callingUid = com.android.server.alarm.AlarmManagerService.this.mInjector.getCallingUid();
            int callingUserId = android.os.UserHandle.getUserId(callingUid);
            if (callingUid != com.android.server.alarm.AlarmManagerService.this.mPackageManagerInternal.getPackageUid(callingPackage, 0L, callingUserId)) {
                throw new java.lang.SecurityException("Package " + callingPackage + " does not belong to the calling uid " + callingUid);
            }
            if (interval != 0 && directReceiver != null) {
                throw new java.lang.IllegalArgumentException("Repeating alarms cannot use AlarmReceivers");
            }
            if (workSource != null) {
                com.android.server.alarm.AlarmManagerService.this.getContext().enforcePermission("android.permission.UPDATE_DEVICE_STATS", android.os.Binder.getCallingPid(), callingUid, "AlarmManager.set");
            }
            if ((flags & 16) == 0) {
                windowLength2 = windowLength;
                flags2 = flags;
            } else if (callingUid != 1000) {
                flags2 = flags & (-17);
                windowLength2 = windowLength;
            } else {
                windowLength2 = 0;
                flags2 = flags;
            }
            int flags4 = flags2 & (-43);
            if (alarmClock != null) {
                flags4 |= 2;
                windowLength3 = 0;
            } else if (workSource == null && (android.os.UserHandle.isCore(callingUid) || android.os.UserHandle.isSameApp(callingUid, com.android.server.alarm.AlarmManagerService.this.mSystemUiUid) || (com.android.server.alarm.AlarmManagerService.this.mAppStateTracker != null && com.android.server.alarm.AlarmManagerService.this.mAppStateTracker.isUidPowerSaveUserExempt(callingUid)))) {
                flags4 = (flags4 | 8) & (-69);
                windowLength3 = windowLength2;
            } else {
                windowLength3 = windowLength2;
            }
            boolean lowerQuota = false;
            boolean allowWhileIdle = (flags4 & 4) != 0;
            boolean exact = windowLength3 == 0;
            int exactAllowReason2 = -1;
            if ((flags4 & 64) != 0) {
                com.android.server.alarm.AlarmManagerService.this.getContext().enforcePermission("android.permission.SCHEDULE_PRIORITIZED_ALARM", android.os.Binder.getCallingPid(), callingUid, "AlarmManager.setPrioritized");
                flags4 &= -5;
                if (!exact) {
                    exactAllowReason = -1;
                    idleOptions2 = null;
                } else {
                    exactAllowReason = 5;
                    idleOptions2 = null;
                }
            } else if (!exact && !allowWhileIdle) {
                exactAllowReason = -1;
                idleOptions2 = null;
            } else {
                if (com.android.server.alarm.AlarmManagerService.isExactAlarmChangeEnabled(callingPackage, callingUserId)) {
                    if (directReceiver == null) {
                        needsPermission = exact;
                        if (!exact) {
                            lowerQuota = true;
                        }
                    } else {
                        needsPermission = false;
                        lowerQuota = allowWhileIdle;
                        if (exact) {
                            exactAllowReason2 = 4;
                        }
                    }
                    if (exact) {
                        if (alarmClock != null) {
                            bundle2 = com.android.server.alarm.AlarmManagerService.this.mOptsWithFgsForAlarmClock.toBundle();
                        } else {
                            bundle2 = com.android.server.alarm.AlarmManagerService.this.mOptsWithFgs.toBundle();
                        }
                        idleOptions = bundle2;
                    } else {
                        idleOptions = com.android.server.alarm.AlarmManagerService.this.mOptsWithoutFgs.toBundle();
                    }
                } else {
                    needsPermission = false;
                    lowerQuota = allowWhileIdle;
                    if (allowWhileIdle || alarmClock != null) {
                        bundle = com.android.server.alarm.AlarmManagerService.this.mOptsWithFgs.toBundle();
                    } else {
                        bundle = null;
                    }
                    idleOptions = bundle;
                    if (exact) {
                        exactAllowReason2 = 2;
                    }
                }
                if (needsPermission) {
                    if (com.android.server.alarm.AlarmManagerService.this.hasUseExactAlarmInternal(callingPackage, callingUid)) {
                        exactAllowReason2 = 3;
                    } else if (com.android.server.alarm.AlarmManagerService.this.hasScheduleExactAlarmInternal(callingPackage, callingUid)) {
                        exactAllowReason2 = 0;
                    } else if (com.android.server.alarm.AlarmManagerService.this.isExemptFromExactAlarmPermissionNoLock(callingUid)) {
                        exactAllowReason2 = 1;
                        idleOptions = allowWhileIdle ? com.android.server.alarm.AlarmManagerService.this.mOptsWithoutFgs.toBundle() : null;
                        lowerQuota = allowWhileIdle;
                    } else {
                        java.lang.String errorMessage = "Caller " + callingPackage + " needs to hold android.permission.SCHEDULE_EXACT_ALARM or android.permission.USE_EXACT_ALARM to set exact alarms.";
                        com.oplus.compatibility.OplusCompatibilityManager mCompatibility = new com.oplus.compatibility.OplusCompatibilityManager();
                        mCompatibility.handleCompatibilityException(256, callingPackage);
                        android.util.Slog.d(com.android.server.alarm.AlarmManagerService.TAG, "pkg " + callingPackage + ", uid " + callingUid + " needs SCHEDULE_EXACT_ALARM, " + errorMessage);
                        throw new java.lang.SecurityException(errorMessage);
                    }
                }
                if (!lowerQuota) {
                    exactAllowReason = exactAllowReason2;
                    idleOptions2 = idleOptions;
                } else {
                    flags4 = (flags4 & (-5)) | 32;
                    exactAllowReason = exactAllowReason2;
                    idleOptions2 = idleOptions;
                }
            }
            if (!exact) {
                flags3 = flags4;
            } else {
                flags3 = flags4 | 1;
            }
            com.android.server.alarm.AlarmManagerService.this.setImpl(type, triggerAtTime, windowLength3, interval, operation, directReceiver, listenerTag, flags3, workSource, alarmClock, callingUid, callingPackage, idleOptions2, exactAllowReason);
        }

        public boolean canScheduleExactAlarms(java.lang.String packageName) {
            int callingUid = com.android.server.alarm.AlarmManagerService.this.mInjector.getCallingUid();
            int userId = android.os.UserHandle.getUserId(callingUid);
            int packageUid = com.android.server.alarm.AlarmManagerService.this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId);
            if (callingUid == packageUid) {
                return !com.android.server.alarm.AlarmManagerService.isExactAlarmChangeEnabled(packageName, userId) || com.android.server.alarm.AlarmManagerService.this.isExemptFromExactAlarmPermissionNoLock(packageUid) || com.android.server.alarm.AlarmManagerService.this.hasScheduleExactAlarmInternal(packageName, packageUid) || com.android.server.alarm.AlarmManagerService.this.hasUseExactAlarmInternal(packageName, packageUid);
            }
            throw new java.lang.SecurityException("Uid " + callingUid + " cannot query canScheduleExactAlarms for package " + packageName);
        }

        public boolean hasScheduleExactAlarm(java.lang.String packageName, int userId) {
            int callingUid = com.android.server.alarm.AlarmManagerService.this.mInjector.getCallingUid();
            if (android.os.UserHandle.getUserId(callingUid) != userId) {
                com.android.server.alarm.AlarmManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "hasScheduleExactAlarm");
            }
            int uid = com.android.server.alarm.AlarmManagerService.this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId);
            if (callingUid != uid && !android.os.UserHandle.isCore(callingUid)) {
                throw new java.lang.SecurityException("Uid " + callingUid + " cannot query hasScheduleExactAlarm for package " + packageName);
            }
            if (uid > 0) {
                return com.android.server.alarm.AlarmManagerService.this.hasScheduleExactAlarmInternal(packageName, uid);
            }
            return false;
        }

        public boolean setTime(long millis) {
            setTime_enforcePermission();
            return com.android.server.alarm.AlarmManagerService.this.setTimeImpl(millis, 100, "AlarmManager.setTime() called");
        }

        public void setTimeZone(java.lang.String tz) {
            setTimeZone_enforcePermission();
            long oldId = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.alarm.AlarmManagerService.this.setTimeZoneImpl(tz, 100, "AlarmManager.setTimeZone() called");
            } finally {
                android.os.Binder.restoreCallingIdentity(oldId);
            }
        }

        public void remove(android.app.PendingIntent operation, android.app.IAlarmListener listener) {
            if (operation == null && listener == null) {
                android.util.Slog.w(com.android.server.alarm.AlarmManagerService.TAG, "remove() with no intent or listener");
                return;
            }
            if (com.android.server.alarm.AlarmManagerService.this.mAmsExt.isDynamicLogEnabled()) {
                android.util.Slog.d(com.android.server.alarm.AlarmManagerService.TAG, "remove: operation=" + operation + ", listener=" + listener);
            }
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.this.mAmsExt.trackEventCancelAlarmLocked(operation, listener);
                com.android.server.alarm.AlarmManagerService.this.removeLocked(operation, listener, 1);
            }
        }

        public void removeAll(final java.lang.String callingPackage) {
            final int callingUid = com.android.server.alarm.AlarmManagerService.this.mInjector.getCallingUid();
            if (callingUid == 1000) {
                android.util.Slog.wtfStack(com.android.server.alarm.AlarmManagerService.TAG, "Attempt to remove all alarms from the system uid package: " + callingPackage);
            } else {
                if (callingUid != com.android.server.alarm.AlarmManagerService.this.mPackageManagerInternal.getPackageUid(callingPackage, 0L, android.os.UserHandle.getUserId(callingUid))) {
                    throw new java.lang.SecurityException("Package " + callingPackage + " does not belong to the calling uid " + callingUid);
                }
                synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                    com.android.server.alarm.AlarmManagerService.this.removeAlarmsInternalLocked(new java.util.function.Predicate() { // from class: com.android.server.alarm.AlarmManagerService$4$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.alarm.AlarmManagerService.AnonymousClass4.lambda$removeAll$0(callingPackage, callingUid, (com.android.server.alarm.Alarm) obj);
                        }
                    }, 1);
                }
            }
        }

        static /* synthetic */ boolean lambda$removeAll$0(java.lang.String callingPackage, int callingUid, com.android.server.alarm.Alarm a) {
            return a.matches(callingPackage) && a.creatorUid == callingUid;
        }

        public long getNextWakeFromIdleTime() {
            return com.android.server.alarm.AlarmManagerService.this.getNextWakeFromIdleTimeImpl();
        }

        public void cancelPoweroffAlarm(java.lang.String name) {
            com.android.server.alarm.AlarmManagerService.this.mAmsExt.cancelPoweroffAlarmImpl(name);
        }

        public android.app.AlarmManager.AlarmClockInfo getNextAlarmClock(int userId) {
            return com.android.server.alarm.AlarmManagerService.this.getNextAlarmClockImpl(com.android.server.alarm.AlarmManagerService.this.mActivityManagerInternal.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, 0, "getNextAlarmClock", (java.lang.String) null));
        }

        public int getConfigVersion() {
            getConfigVersion_enforcePermission();
            return com.android.server.alarm.AlarmManagerService.this.mConstants.getVersion();
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(com.android.server.alarm.AlarmManagerService.this.getContext(), com.android.server.alarm.AlarmManagerService.TAG, pw) || com.android.server.alarm.AlarmManagerService.this.mAmsExt.dumpImpl(fd, pw, args)) {
                return;
            }
            if (args.length > 0 && "--proto".equals(args[0])) {
                com.android.server.alarm.AlarmManagerService.this.dumpProto(fd);
                return;
            }
            com.android.server.alarm.AlarmManagerService.this.dumpImpl(new android.util.IndentingPrintWriter(pw, "  "));
            java.lang.String[] wlArgs = {"alignWhiteListVersion"};
            if (!com.android.server.alarm.AlarmManagerService.this.mAmsExt.dumpImpl(fd, pw, wlArgs)) {
                android.util.Slog.d(com.android.server.alarm.AlarmManagerService.TAG, "cannot dump align whitelist version");
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.alarm.AlarmManagerService.ShellCmd().exec(this, in, out, err, args, callback, resultReceiver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isExactAlarmChangeEnabled(java.lang.String packageName, int userId) {
        return android.app.compat.CompatChanges.isChangeEnabled(171306433L, packageName, android.os.UserHandle.of(userId));
    }

    private static boolean isUseExactAlarmEnabled(java.lang.String packageName, int userId) {
        return android.app.compat.CompatChanges.isChangeEnabled(218533173L, packageName, android.os.UserHandle.of(userId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isScheduleExactAlarmDeniedByDefault(java.lang.String packageName, int userId) {
        return android.app.compat.CompatChanges.isChangeEnabled(226439802L, packageName, android.os.UserHandle.of(userId));
    }

    @dalvik.annotation.optimization.NeverCompile
    void dumpImpl(android.util.IndentingPrintWriter pw) {
        java.text.SimpleDateFormat sdf;
        long nextNonWakeupRTC;
        synchronized (this.mLock) {
            pw.println("Current Alarm Manager state:");
            pw.increaseIndent();
            this.mConstants.dump(pw);
            pw.println();
            pw.println("Feature Flags:");
            pw.increaseIndent();
            pw.print(com.android.server.alarm.Flags.FLAG_USE_FROZEN_STATE_TO_DROP_LISTENER_ALARMS, java.lang.Boolean.valueOf(this.mUseFrozenStateToDropListenerAlarms));
            pw.println();
            pw.print(com.android.server.alarm.Flags.FLAG_START_USER_BEFORE_SCHEDULED_ALARMS, java.lang.Boolean.valueOf(this.mStartUserBeforeScheduledAlarms));
            pw.decreaseIndent();
            pw.println();
            pw.println();
            pw.println("App Standby Parole: " + this.mAppStandbyParole);
            pw.println();
            if (this.mAppStateTracker != null) {
                this.mAppStateTracker.dump(pw);
                pw.println();
            }
            long nowELAPSED = this.mInjector.getElapsedRealtimeMillis();
            long nowUPTIME = android.os.SystemClock.uptimeMillis();
            long nowRTC = this.mInjector.getCurrentTimeMillis();
            java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            pw.print("nowRTC=");
            pw.print(nowRTC);
            pw.print("=");
            pw.print(sdf2.format(new java.util.Date(nowRTC)));
            pw.print(" nowELAPSED=");
            pw.print(nowELAPSED);
            pw.println();
            pw.print("mLastTimeChangeClockTime=");
            pw.print(this.mLastTimeChangeClockTime);
            pw.print("=");
            pw.println(sdf2.format(new java.util.Date(this.mLastTimeChangeClockTime)));
            pw.print("mLastTimeChangeRealtime=");
            pw.println(this.mLastTimeChangeRealtime);
            pw.print("mLastTickReceived=");
            pw.println(sdf2.format(new java.util.Date(this.mLastTickReceived)));
            pw.print("mLastTickSet=");
            pw.println(sdf2.format(new java.util.Date(this.mLastTickSet)));
            pw.println();
            pw.println("Recent TIME_TICK history:");
            pw.increaseIndent();
            int i = this.mNextTickHistory;
            do {
                i--;
                if (i < 0) {
                    i = 9;
                }
                long time = this.mTickHistory[i];
                pw.println(time > 0 ? sdf2.format(new java.util.Date(nowRTC - (nowELAPSED - time))) : "-");
            } while (i != this.mNextTickHistory);
            pw.decreaseIndent();
            com.android.server.SystemServiceManager ssm = (com.android.server.SystemServiceManager) com.android.server.LocalServices.getService(com.android.server.SystemServiceManager.class);
            if (ssm != null) {
                pw.println();
                pw.print("RuntimeStarted=");
                pw.print(sdf2.format(new java.util.Date((nowRTC - nowELAPSED) + ssm.getRuntimeStartElapsedTime())));
                if (ssm.isRuntimeRestarted()) {
                    pw.print("  (Runtime restarted)");
                }
                pw.println();
                pw.print("Runtime uptime (elapsed): ");
                android.util.TimeUtils.formatDuration(nowELAPSED, ssm.getRuntimeStartElapsedTime(), pw);
                pw.println();
                pw.print("Runtime uptime (uptime): ");
                android.util.TimeUtils.formatDuration(nowUPTIME, ssm.getRuntimeStartUptime(), pw);
                pw.println();
            }
            pw.println();
            if (!this.mInteractive) {
                pw.print("Time since non-interactive: ");
                android.util.TimeUtils.formatDuration(nowELAPSED - this.mNonInteractiveStartTime, pw);
                pw.println();
            }
            pw.print("Max wakeup delay: ");
            android.util.TimeUtils.formatDuration(currentNonWakeupFuzzLocked(nowELAPSED), pw);
            pw.println();
            pw.print("Time since last dispatch: ");
            android.util.TimeUtils.formatDuration(nowELAPSED - this.mLastAlarmDeliveryTime, pw);
            pw.println();
            pw.print("Next non-wakeup delivery time: ");
            android.util.TimeUtils.formatDuration(this.mNextNonWakeupDeliveryTime, nowELAPSED, pw);
            pw.println();
            long nextWakeupRTC = this.mNextWakeup + (nowRTC - nowELAPSED);
            long nextNonWakeupRTC2 = this.mNextNonWakeup + (nowRTC - nowELAPSED);
            pw.print("Next non-wakeup alarm: ");
            android.util.TimeUtils.formatDuration(this.mNextNonWakeup, nowELAPSED, pw);
            pw.print(" = ");
            pw.print(this.mNextNonWakeup);
            pw.print(" = ");
            pw.println(sdf2.format(new java.util.Date(nextNonWakeupRTC2)));
            pw.increaseIndent();
            pw.print("set at ");
            android.util.TimeUtils.formatDuration(this.mNextNonWakeUpSetAt, nowELAPSED, pw);
            pw.decreaseIndent();
            pw.println();
            pw.print("Next wakeup alarm: ");
            android.util.TimeUtils.formatDuration(this.mNextWakeup, nowELAPSED, pw);
            pw.print(" = ");
            pw.print(this.mNextWakeup);
            pw.print(" = ");
            pw.println(sdf2.format(new java.util.Date(nextWakeupRTC)));
            pw.increaseIndent();
            pw.print("set at ");
            android.util.TimeUtils.formatDuration(this.mNextWakeUpSetAt, nowELAPSED, pw);
            pw.decreaseIndent();
            pw.println();
            pw.print("Next kernel non-wakeup alarm: ");
            android.util.TimeUtils.formatDuration(this.mInjector.getNextAlarm(3), pw);
            pw.println();
            pw.print("Next kernel wakeup alarm: ");
            android.util.TimeUtils.formatDuration(this.mInjector.getNextAlarm(2), pw);
            pw.println();
            pw.print("Last wakeup: ");
            android.util.TimeUtils.formatDuration(this.mLastWakeup, nowELAPSED, pw);
            pw.print(" = ");
            pw.println(this.mLastWakeup);
            pw.print("Last trigger: ");
            android.util.TimeUtils.formatDuration(this.mLastTrigger, nowELAPSED, pw);
            pw.print(" = ");
            pw.println(this.mLastTrigger);
            pw.print("Num time change events: ");
            pw.println(this.mNumTimeChanged);
            pw.println();
            pw.println("App ids requesting SCHEDULE_EXACT_ALARM: " + this.mExactAlarmCandidates);
            pw.println();
            pw.print("Last OP_SCHEDULE_EXACT_ALARM: [");
            int i2 = 0;
            while (i2 < this.mLastOpScheduleExactAlarm.size()) {
                if (i2 > 0) {
                    pw.print(", ");
                }
                android.os.UserHandle.formatUid(pw, this.mLastOpScheduleExactAlarm.keyAt(i2));
                pw.print(":" + android.app.AppOpsManager.modeToName(this.mLastOpScheduleExactAlarm.valueAt(i2)));
                i2++;
                ssm = ssm;
            }
            pw.println("]");
            pw.println();
            pw.println("Next alarm clock information: ");
            pw.increaseIndent();
            java.util.TreeSet<java.lang.Integer> users = new java.util.TreeSet<>();
            for (int i3 = 0; i3 < this.mNextAlarmClockForUser.size(); i3++) {
                users.add(java.lang.Integer.valueOf(this.mNextAlarmClockForUser.keyAt(i3)));
            }
            for (int i4 = 0; i4 < this.mPendingSendNextAlarmClockChangedForUser.size(); i4++) {
                users.add(java.lang.Integer.valueOf(this.mPendingSendNextAlarmClockChangedForUser.keyAt(i4)));
            }
            java.util.Iterator<java.lang.Integer> it = users.iterator();
            while (it.hasNext()) {
                int user = it.next().intValue();
                java.util.TreeSet<java.lang.Integer> users2 = users;
                android.app.AlarmManager.AlarmClockInfo next = this.mNextAlarmClockForUser.get(user);
                long time2 = next != null ? next.getTriggerTime() : 0L;
                boolean pendingSend = this.mPendingSendNextAlarmClockChangedForUser.get(user);
                java.util.Iterator<java.lang.Integer> it2 = it;
                pw.print("user:");
                pw.print(user);
                pw.print(" pendingSend:");
                pw.print(pendingSend);
                pw.print(" time:");
                pw.print(time2);
                if (time2 > 0) {
                    pw.print(" = ");
                    pw.print(sdf2.format(new java.util.Date(time2)));
                    pw.print(" = ");
                    android.util.TimeUtils.formatDuration(time2, nowRTC, pw);
                }
                pw.println();
                users = users2;
                it = it2;
            }
            pw.decreaseIndent();
            if (this.mAlarmStore.size() > 0) {
                pw.println();
                this.mAlarmStore.dump(pw, nowELAPSED, sdf2);
            }
            pw.println();
            pw.println("Pending user blocked background alarms: ");
            pw.increaseIndent();
            boolean blocked = false;
            for (int i5 = 0; i5 < this.mPendingBackgroundAlarms.size(); i5++) {
                java.util.ArrayList<com.android.server.alarm.Alarm> blockedAlarms = this.mPendingBackgroundAlarms.valueAt(i5);
                if (blockedAlarms != null && blockedAlarms.size() > 0) {
                    blocked = true;
                    dumpAlarmList(pw, blockedAlarms, nowELAPSED, sdf2);
                }
            }
            if (!blocked) {
                pw.println("none");
            }
            pw.decreaseIndent();
            pw.println();
            pw.print("Pending alarms per uid: [");
            for (int i6 = 0; i6 < this.mAlarmsPerUid.size(); i6++) {
                if (i6 > 0) {
                    pw.print(", ");
                }
                android.os.UserHandle.formatUid(pw, this.mAlarmsPerUid.keyAt(i6));
                pw.print(":");
                pw.print(this.mAlarmsPerUid.valueAt(i6));
            }
            pw.println("]");
            pw.println();
            if (this.mStartUserBeforeScheduledAlarms) {
                pw.println("Scheduled user wakeups:");
                this.mUserWakeupStore.dump(pw, nowELAPSED);
                pw.println();
            }
            pw.println("App Alarm history:");
            this.mAppWakeupHistory.dump(pw, nowELAPSED);
            pw.println();
            pw.println("Temporary Quota Reserves:");
            this.mTemporaryQuotaReserve.dump(pw, nowELAPSED);
            if (this.mPendingIdleUntil != null) {
                pw.println();
                pw.println("Idle mode state:");
                pw.increaseIndent();
                pw.print("Idling until: ");
                if (this.mPendingIdleUntil != null) {
                    pw.println(this.mPendingIdleUntil);
                    this.mPendingIdleUntil.dump(pw, nowELAPSED, sdf2);
                } else {
                    pw.println("null");
                }
                pw.decreaseIndent();
            }
            if (this.mNextWakeFromIdle != null) {
                pw.println();
                pw.print("Next wake from idle: ");
                pw.println(this.mNextWakeFromIdle);
                pw.increaseIndent();
                this.mNextWakeFromIdle.dump(pw, nowELAPSED, sdf2);
                pw.decreaseIndent();
            }
            pw.println();
            pw.print("Past-due non-wakeup alarms: ");
            if (this.mPendingNonWakeupAlarms.size() > 0) {
                pw.println(this.mPendingNonWakeupAlarms.size());
                pw.increaseIndent();
                dumpAlarmList(pw, this.mPendingNonWakeupAlarms, nowELAPSED, sdf2);
                pw.decreaseIndent();
            } else {
                pw.println("(none)");
            }
            pw.increaseIndent();
            pw.print("Number of delayed alarms: ");
            pw.print(this.mNumDelayedAlarms);
            pw.print(", total delay time: ");
            boolean blocked2 = blocked;
            android.util.TimeUtils.formatDuration(this.mTotalDelayTime, pw);
            pw.println();
            pw.print("Max delay time: ");
            android.util.TimeUtils.formatDuration(this.mMaxDelayTime, pw);
            pw.print(", max non-interactive time: ");
            android.util.TimeUtils.formatDuration(this.mNonInteractiveTime, pw);
            pw.println();
            pw.decreaseIndent();
            pw.println();
            pw.print("Broadcast ref count: ");
            pw.println(this.mBroadcastRefCount);
            pw.print("PendingIntent send count: ");
            pw.println(this.mSendCount);
            pw.print("PendingIntent finish count: ");
            pw.println(this.mSendFinishCount);
            pw.print("Listener send count: ");
            pw.println(this.mListenerCount);
            pw.print("Listener finish count: ");
            pw.println(this.mListenerFinishCount);
            pw.println();
            if (this.mInFlight.size() > 0) {
                pw.println("Outstanding deliveries:");
                pw.increaseIndent();
                for (int i7 = 0; i7 < this.mInFlight.size(); i7++) {
                    pw.print("#");
                    pw.print(i7);
                    pw.print(": ");
                    pw.println(this.mInFlight.get(i7));
                }
                pw.decreaseIndent();
                pw.println();
            }
            pw.println("Allow while idle history:");
            this.mAllowWhileIdleHistory.dump(pw, nowELAPSED);
            pw.println();
            pw.println("Allow while idle compat history:");
            this.mAllowWhileIdleCompatHistory.dump(pw, nowELAPSED);
            pw.println();
            if (this.mLastPriorityAlarmDispatch.size() > 0) {
                pw.println("Last priority alarm dispatches:");
                pw.increaseIndent();
                int i8 = 0;
                while (i8 < this.mLastPriorityAlarmDispatch.size()) {
                    pw.print("UID: ");
                    android.os.UserHandle.formatUid(pw, this.mLastPriorityAlarmDispatch.keyAt(i8));
                    pw.print(": ");
                    android.util.TimeUtils.formatDuration(this.mLastPriorityAlarmDispatch.valueAt(i8), nowELAPSED, pw);
                    pw.println();
                    i8++;
                    nowRTC = nowRTC;
                }
                pw.decreaseIndent();
            }
            if (this.mRemovalHistory.size() > 0) {
                pw.println("Removal history:");
                pw.increaseIndent();
                int i9 = 0;
                while (i9 < this.mRemovalHistory.size()) {
                    android.os.UserHandle.formatUid(pw, this.mRemovalHistory.keyAt(i9));
                    pw.println(":");
                    pw.increaseIndent();
                    com.android.server.alarm.AlarmManagerService.RemovedAlarm[] historyForUid = (com.android.server.alarm.AlarmManagerService.RemovedAlarm[]) this.mRemovalHistory.valueAt(i9).toArray();
                    int index = historyForUid.length - 1;
                    while (index >= 0) {
                        pw.print("#" + (historyForUid.length - index) + ": ");
                        historyForUid[index].dump(pw, nowELAPSED, sdf2);
                        index--;
                        blocked2 = blocked2;
                    }
                    pw.decreaseIndent();
                    i9++;
                    blocked2 = blocked2;
                }
                pw.decreaseIndent();
                pw.println();
            }
            if (this.mLog.dump(pw, "Recent problems:")) {
                pw.println();
            }
            com.android.server.alarm.AlarmManagerService.FilterStats[] topFilters = new com.android.server.alarm.AlarmManagerService.FilterStats[10];
            java.util.Comparator<com.android.server.alarm.AlarmManagerService.FilterStats> comparator = new java.util.Comparator<com.android.server.alarm.AlarmManagerService.FilterStats>() { // from class: com.android.server.alarm.AlarmManagerService.5
                @Override // java.util.Comparator
                public int compare(com.android.server.alarm.AlarmManagerService.FilterStats lhs, com.android.server.alarm.AlarmManagerService.FilterStats rhs) {
                    if (lhs.aggregateTime < rhs.aggregateTime) {
                        return 1;
                    }
                    if (lhs.aggregateTime > rhs.aggregateTime) {
                        return -1;
                    }
                    return 0;
                }
            };
            int len = 0;
            int iu = 0;
            while (iu < this.mBroadcastStats.size()) {
                android.util.ArrayMap<java.lang.String, com.android.server.alarm.AlarmManagerService.BroadcastStats> uidStats = this.mBroadcastStats.valueAt(iu);
                int len2 = len;
                int len3 = 0;
                while (true) {
                    sdf = sdf2;
                    if (len3 < uidStats.size()) {
                        com.android.server.alarm.AlarmManagerService.BroadcastStats bs = uidStats.valueAt(len3);
                        android.util.ArrayMap<java.lang.String, com.android.server.alarm.AlarmManagerService.BroadcastStats> uidStats2 = uidStats;
                        long nextWakeupRTC2 = nextWakeupRTC;
                        int len4 = len2;
                        int is = 0;
                        while (is < bs.filterStats.size()) {
                            com.android.server.alarm.AlarmManagerService.FilterStats fs = bs.filterStats.valueAt(is);
                            com.android.server.alarm.AlarmManagerService.BroadcastStats bs2 = bs;
                            int pos = len4 > 0 ? java.util.Arrays.binarySearch(topFilters, 0, len4, fs, comparator) : 0;
                            if (pos >= 0) {
                                nextNonWakeupRTC = nextNonWakeupRTC2;
                            } else {
                                nextNonWakeupRTC = nextNonWakeupRTC2;
                                pos = (-pos) - 1;
                            }
                            if (pos < topFilters.length) {
                                int copylen = (topFilters.length - pos) - 1;
                                if (copylen > 0) {
                                    java.lang.System.arraycopy(topFilters, pos, topFilters, pos + 1, copylen);
                                }
                                topFilters[pos] = fs;
                                if (len4 < topFilters.length) {
                                    len4++;
                                }
                            }
                            is++;
                            bs = bs2;
                            nextNonWakeupRTC2 = nextNonWakeupRTC;
                        }
                        len3++;
                        len2 = len4;
                        sdf2 = sdf;
                        uidStats = uidStats2;
                        nextWakeupRTC = nextWakeupRTC2;
                    }
                }
                iu++;
                len = len2;
                sdf2 = sdf;
            }
            if (len > 0) {
                pw.println("Top Alarms:");
                pw.increaseIndent();
                for (int i10 = 0; i10 < len; i10++) {
                    com.android.server.alarm.AlarmManagerService.FilterStats fs2 = topFilters[i10];
                    if (fs2.nesting > 0) {
                        pw.print("*ACTIVE* ");
                    }
                    android.util.TimeUtils.formatDuration(fs2.aggregateTime, pw);
                    pw.print(" running, ");
                    pw.print(fs2.numWakeup);
                    pw.print(" wakeups, ");
                    pw.print(fs2.count);
                    pw.print(" alarms: ");
                    android.os.UserHandle.formatUid(pw, fs2.mBroadcastStats.mUid);
                    pw.print(":");
                    pw.print(fs2.mBroadcastStats.mPackageName);
                    pw.println();
                    pw.increaseIndent();
                    pw.print(fs2.mTag);
                    pw.println();
                    pw.decreaseIndent();
                }
                pw.decreaseIndent();
            }
            pw.println();
            pw.println("Alarm Stats:");
            java.util.ArrayList<com.android.server.alarm.AlarmManagerService.FilterStats> tmpFilters = new java.util.ArrayList<>();
            for (int iu2 = 0; iu2 < this.mBroadcastStats.size(); iu2++) {
                android.util.ArrayMap<java.lang.String, com.android.server.alarm.AlarmManagerService.BroadcastStats> uidStats3 = this.mBroadcastStats.valueAt(iu2);
                int ip = 0;
                while (ip < uidStats3.size()) {
                    com.android.server.alarm.AlarmManagerService.BroadcastStats bs3 = uidStats3.valueAt(ip);
                    if (bs3.nesting > 0) {
                        pw.print("*ACTIVE* ");
                    }
                    android.os.UserHandle.formatUid(pw, bs3.mUid);
                    pw.print(":");
                    pw.print(bs3.mPackageName);
                    pw.print(" ");
                    android.util.TimeUtils.formatDuration(bs3.aggregateTime, pw);
                    pw.print(" running, ");
                    pw.print(bs3.numWakeup);
                    pw.println(" wakeups:");
                    tmpFilters.clear();
                    for (int is2 = 0; is2 < bs3.filterStats.size(); is2++) {
                        tmpFilters.add(bs3.filterStats.valueAt(is2));
                    }
                    java.util.Collections.sort(tmpFilters, comparator);
                    pw.increaseIndent();
                    int i11 = 0;
                    while (i11 < tmpFilters.size()) {
                        com.android.server.alarm.AlarmManagerService.FilterStats fs3 = tmpFilters.get(i11);
                        com.android.server.alarm.AlarmManagerService.FilterStats[] topFilters2 = topFilters;
                        if (fs3.nesting > 0) {
                            pw.print("*ACTIVE* ");
                        }
                        android.util.TimeUtils.formatDuration(fs3.aggregateTime, pw);
                        pw.print(" ");
                        pw.print(fs3.numWakeup);
                        pw.print(" wakes ");
                        pw.print(fs3.count);
                        pw.print(" alarms, last ");
                        android.util.TimeUtils.formatDuration(fs3.lastTime, nowELAPSED, pw);
                        pw.println(":");
                        pw.increaseIndent();
                        pw.print(fs3.mTag);
                        pw.println();
                        pw.decreaseIndent();
                        i11++;
                        topFilters = topFilters2;
                        comparator = comparator;
                    }
                    pw.decreaseIndent();
                    ip++;
                    topFilters = topFilters;
                    comparator = comparator;
                }
            }
            pw.println();
            this.mStatLogger.dump(pw);
        }
    }

    void dumpProto(java.io.FileDescriptor fd) {
        android.util.ArrayMap<java.lang.String, com.android.server.alarm.AlarmManagerService.BroadcastStats> uidStats;
        com.android.server.alarm.AlarmManagerService.BroadcastStats bs;
        long nowRTC;
        int pendingSendNextAlarmClockChangedForUserSize;
        long nowRTC2;
        com.android.server.alarm.AlarmManagerService alarmManagerService = this;
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
        synchronized (alarmManagerService.mLock) {
            long nowRTC3 = alarmManagerService.mInjector.getCurrentTimeMillis();
            long nowElapsed = alarmManagerService.mInjector.getElapsedRealtimeMillis();
            proto.write(1112396529665L, nowRTC3);
            proto.write(1112396529666L, nowElapsed);
            proto.write(1112396529667L, alarmManagerService.mLastTimeChangeClockTime);
            proto.write(1112396529668L, alarmManagerService.mLastTimeChangeRealtime);
            alarmManagerService.mConstants.dumpProto(proto, 1146756268037L);
            if (alarmManagerService.mAppStateTracker != null) {
                alarmManagerService.mAppStateTracker.dumpProto(proto, 1146756268038L);
            }
            proto.write(1133871366151L, alarmManagerService.mInteractive);
            if (!alarmManagerService.mInteractive) {
                proto.write(1112396529672L, nowElapsed - alarmManagerService.mNonInteractiveStartTime);
                proto.write(1112396529673L, alarmManagerService.currentNonWakeupFuzzLocked(nowElapsed));
                proto.write(1112396529674L, nowElapsed - alarmManagerService.mLastAlarmDeliveryTime);
                proto.write(1112396529675L, nowElapsed - alarmManagerService.mNextNonWakeupDeliveryTime);
            }
            proto.write(1112396529676L, alarmManagerService.mNextNonWakeup - nowElapsed);
            proto.write(1112396529677L, alarmManagerService.mNextWakeup - nowElapsed);
            proto.write(1112396529678L, nowElapsed - alarmManagerService.mLastWakeup);
            proto.write(1112396529679L, nowElapsed - alarmManagerService.mNextWakeUpSetAt);
            proto.write(1112396529680L, alarmManagerService.mNumTimeChanged);
            java.util.TreeSet<java.lang.Integer> users = new java.util.TreeSet<>();
            int nextAlarmClockForUserSize = alarmManagerService.mNextAlarmClockForUser.size();
            for (int i = 0; i < nextAlarmClockForUserSize; i++) {
                users.add(java.lang.Integer.valueOf(alarmManagerService.mNextAlarmClockForUser.keyAt(i)));
            }
            int pendingSendNextAlarmClockChangedForUserSize2 = alarmManagerService.mPendingSendNextAlarmClockChangedForUser.size();
            for (int i2 = 0; i2 < pendingSendNextAlarmClockChangedForUserSize2; i2++) {
                users.add(java.lang.Integer.valueOf(alarmManagerService.mPendingSendNextAlarmClockChangedForUser.keyAt(i2)));
            }
            for (java.util.Iterator<java.lang.Integer> it = users.iterator(); it.hasNext(); it = it) {
                int user = it.next().intValue();
                android.app.AlarmManager.AlarmClockInfo next = alarmManagerService.mNextAlarmClockForUser.get(user);
                long time = next != null ? next.getTriggerTime() : 0L;
                boolean pendingSend = alarmManagerService.mPendingSendNextAlarmClockChangedForUser.get(user);
                long aToken = proto.start(2246267895826L);
                proto.write(1120986464257L, user);
                proto.write(1133871366146L, pendingSend);
                proto.write(1112396529667L, time);
                proto.end(aToken);
                pendingSendNextAlarmClockChangedForUserSize2 = pendingSendNextAlarmClockChangedForUserSize2;
            }
            int pendingSendNextAlarmClockChangedForUserSize3 = pendingSendNextAlarmClockChangedForUserSize2;
            long j = 1120986464257L;
            alarmManagerService.mAlarmStore.dumpProto(proto, nowElapsed);
            int i3 = 0;
            while (i3 < alarmManagerService.mPendingBackgroundAlarms.size()) {
                java.util.ArrayList<com.android.server.alarm.Alarm> blockedAlarms = alarmManagerService.mPendingBackgroundAlarms.valueAt(i3);
                if (blockedAlarms == null) {
                    nowRTC = nowRTC3;
                    pendingSendNextAlarmClockChangedForUserSize = pendingSendNextAlarmClockChangedForUserSize3;
                    nowRTC2 = j;
                } else {
                    for (com.android.server.alarm.Alarm a : blockedAlarms) {
                        a.dumpDebug(proto, 2246267895828L, nowElapsed);
                        j = j;
                        pendingSendNextAlarmClockChangedForUserSize3 = pendingSendNextAlarmClockChangedForUserSize3;
                        nowRTC3 = nowRTC3;
                    }
                    nowRTC = nowRTC3;
                    pendingSendNextAlarmClockChangedForUserSize = pendingSendNextAlarmClockChangedForUserSize3;
                    nowRTC2 = j;
                }
                i3++;
                j = nowRTC2;
                pendingSendNextAlarmClockChangedForUserSize3 = pendingSendNextAlarmClockChangedForUserSize;
                nowRTC3 = nowRTC;
            }
            if (alarmManagerService.mPendingIdleUntil != null) {
                alarmManagerService.mPendingIdleUntil.dumpDebug(proto, 1146756268053L, nowElapsed);
            }
            if (alarmManagerService.mNextWakeFromIdle != null) {
                alarmManagerService.mNextWakeFromIdle.dumpDebug(proto, 1146756268055L, nowElapsed);
            }
            for (com.android.server.alarm.Alarm a2 : alarmManagerService.mPendingNonWakeupAlarms) {
                a2.dumpDebug(proto, 2246267895832L, nowElapsed);
            }
            proto.write(1120986464281L, alarmManagerService.mNumDelayedAlarms);
            proto.write(1112396529690L, alarmManagerService.mTotalDelayTime);
            proto.write(1112396529691L, alarmManagerService.mMaxDelayTime);
            proto.write(1112396529692L, alarmManagerService.mNonInteractiveTime);
            proto.write(1120986464285L, alarmManagerService.mBroadcastRefCount);
            proto.write(1120986464286L, alarmManagerService.mSendCount);
            proto.write(1120986464287L, alarmManagerService.mSendFinishCount);
            proto.write(1120986464288L, alarmManagerService.mListenerCount);
            proto.write(1120986464289L, alarmManagerService.mListenerFinishCount);
            for (com.android.server.alarm.AlarmManagerService.InFlight f : alarmManagerService.mInFlight) {
                f.dumpDebug(proto, 2246267895842L);
            }
            alarmManagerService.mLog.dumpDebug(proto, 1146756268069L);
            com.android.server.alarm.AlarmManagerService.FilterStats[] topFilters = new com.android.server.alarm.AlarmManagerService.FilterStats[10];
            java.util.Comparator<com.android.server.alarm.AlarmManagerService.FilterStats> comparator = new java.util.Comparator<com.android.server.alarm.AlarmManagerService.FilterStats>() { // from class: com.android.server.alarm.AlarmManagerService.6
                @Override // java.util.Comparator
                public int compare(com.android.server.alarm.AlarmManagerService.FilterStats lhs, com.android.server.alarm.AlarmManagerService.FilterStats rhs) {
                    if (lhs.aggregateTime < rhs.aggregateTime) {
                        return 1;
                    }
                    if (lhs.aggregateTime > rhs.aggregateTime) {
                        return -1;
                    }
                    return 0;
                }
            };
            int len = 0;
            for (int iu = 0; iu < alarmManagerService.mBroadcastStats.size(); iu++) {
                android.util.ArrayMap<java.lang.String, com.android.server.alarm.AlarmManagerService.BroadcastStats> uidStats2 = alarmManagerService.mBroadcastStats.valueAt(iu);
                for (int ip = 0; ip < uidStats2.size(); ip++) {
                    com.android.server.alarm.AlarmManagerService.BroadcastStats bs2 = uidStats2.valueAt(ip);
                    int is = 0;
                    while (is < bs2.filterStats.size()) {
                        com.android.server.alarm.AlarmManagerService.FilterStats fs = bs2.filterStats.valueAt(is);
                        java.util.TreeSet<java.lang.Integer> users2 = users;
                        int pos = len > 0 ? java.util.Arrays.binarySearch(topFilters, 0, len, fs, comparator) : 0;
                        if (pos >= 0) {
                            uidStats = uidStats2;
                        } else {
                            uidStats = uidStats2;
                            pos = (-pos) - 1;
                        }
                        if (pos >= topFilters.length) {
                            bs = bs2;
                        } else {
                            int copylen = (topFilters.length - pos) - 1;
                            if (copylen <= 0) {
                                bs = bs2;
                            } else {
                                bs = bs2;
                                java.lang.System.arraycopy(topFilters, pos, topFilters, pos + 1, copylen);
                            }
                            topFilters[pos] = fs;
                            if (len < topFilters.length) {
                                len++;
                            }
                        }
                        is++;
                        users = users2;
                        uidStats2 = uidStats;
                        bs2 = bs;
                    }
                }
            }
            for (int i4 = 0; i4 < len; i4++) {
                long token = proto.start(2246267895846L);
                com.android.server.alarm.AlarmManagerService.FilterStats fs2 = topFilters[i4];
                proto.write(1120986464257L, fs2.mBroadcastStats.mUid);
                proto.write(1138166333442L, fs2.mBroadcastStats.mPackageName);
                fs2.dumpDebug(proto, 1146756268035L);
                proto.end(token);
            }
            java.util.ArrayList<com.android.server.alarm.AlarmManagerService.FilterStats> tmpFilters = new java.util.ArrayList<>();
            int iu2 = 0;
            while (iu2 < alarmManagerService.mBroadcastStats.size()) {
                android.util.ArrayMap<java.lang.String, com.android.server.alarm.AlarmManagerService.BroadcastStats> uidStats3 = alarmManagerService.mBroadcastStats.valueAt(iu2);
                int ip2 = 0;
                while (ip2 < uidStats3.size()) {
                    long token2 = proto.start(2246267895847L);
                    com.android.server.alarm.AlarmManagerService.BroadcastStats bs3 = uidStats3.valueAt(ip2);
                    com.android.server.alarm.AlarmManagerService.FilterStats[] topFilters2 = topFilters;
                    bs3.dumpDebug(proto, 1146756268033L);
                    tmpFilters.clear();
                    for (int is2 = 0; is2 < bs3.filterStats.size(); is2++) {
                        tmpFilters.add(bs3.filterStats.valueAt(is2));
                    }
                    java.util.Collections.sort(tmpFilters, comparator);
                    for (java.util.Iterator<com.android.server.alarm.AlarmManagerService.FilterStats> it2 = tmpFilters.iterator(); it2.hasNext(); it2 = it2) {
                        it2.next().dumpDebug(proto, 2246267895810L);
                        tmpFilters = tmpFilters;
                    }
                    proto.end(token2);
                    ip2++;
                    topFilters = topFilters2;
                    tmpFilters = tmpFilters;
                }
                iu2++;
                alarmManagerService = this;
            }
        }
        proto.flush();
    }

    long getNextWakeFromIdleTimeImpl() {
        long whenElapsed;
        synchronized (this.mLock) {
            whenElapsed = this.mNextWakeFromIdle != null ? this.mNextWakeFromIdle.getWhenElapsed() : Long.MAX_VALUE;
        }
        return whenElapsed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isIdlingImpl() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mPendingIdleUntil != null;
        }
        return z;
    }

    android.app.AlarmManager.AlarmClockInfo getNextAlarmClockImpl(int userId) {
        android.app.AlarmManager.AlarmClockInfo alarmClockInfo;
        synchronized (this.mLock) {
            alarmClockInfo = this.mNextAlarmClockForUser.get(userId);
        }
        return alarmClockInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNextAlarmClockLocked() {
        if (!this.mNextAlarmClockMayChange) {
            return;
        }
        this.mNextAlarmClockMayChange = false;
        android.util.SparseArray<android.app.AlarmManager.AlarmClockInfo> nextForUser = this.mTmpSparseAlarmClockArray;
        nextForUser.clear();
        java.util.ArrayList<com.android.server.alarm.Alarm> allAlarms = this.mAlarmStore.asList();
        for (com.android.server.alarm.Alarm a : allAlarms) {
            if (a.alarmClock != null) {
                int userId = android.os.UserHandle.getUserId(a.uid);
                android.app.AlarmManager.AlarmClockInfo current = this.mNextAlarmClockForUser.get(userId);
                if (this.mAmsExt.isDynamicLogEnabled()) {
                    android.util.Log.v(TAG, "Found AlarmClockInfo " + a.alarmClock + " at " + formatNextAlarm(getContext(), a.alarmClock, userId) + " for user " + userId);
                }
                if (nextForUser.get(userId) == null) {
                    nextForUser.put(userId, a.alarmClock);
                } else if (a.alarmClock.equals(current) && current.getTriggerTime() <= nextForUser.get(userId).getTriggerTime()) {
                    nextForUser.put(userId, current);
                }
            }
        }
        int newUserCount = nextForUser.size();
        for (int i = 0; i < newUserCount; i++) {
            android.app.AlarmManager.AlarmClockInfo newAlarm = nextForUser.valueAt(i);
            int userId2 = nextForUser.keyAt(i);
            android.app.AlarmManager.AlarmClockInfo currentAlarm = this.mNextAlarmClockForUser.get(userId2);
            if (!newAlarm.equals(currentAlarm)) {
                updateNextAlarmInfoForUserLocked(userId2, newAlarm);
            }
        }
        int oldUserCount = this.mNextAlarmClockForUser.size();
        for (int i2 = oldUserCount - 1; i2 >= 0; i2--) {
            int userId3 = this.mNextAlarmClockForUser.keyAt(i2);
            if (nextForUser.get(userId3) == null) {
                updateNextAlarmInfoForUserLocked(userId3, null);
            }
        }
    }

    private void updateNextAlarmInfoForUserLocked(int userId, android.app.AlarmManager.AlarmClockInfo alarmClock) {
        if (alarmClock != null) {
            if (this.mAmsExt.isDynamicLogEnabled()) {
                android.util.Log.v(TAG, "Next AlarmClockInfoForUser(" + userId + "): " + formatNextAlarm(getContext(), alarmClock, userId));
            }
            this.mNextAlarmClockForUser.put(userId, alarmClock);
            if (this.mStartUserBeforeScheduledAlarms) {
                this.mUserWakeupStore.addUserWakeup(userId, convertToElapsed(this.mNextAlarmClockForUser.get(userId).getTriggerTime(), 1));
            }
        } else {
            if (this.mAmsExt.isDynamicLogEnabled()) {
                android.util.Log.v(TAG, "Next AlarmClockInfoForUser(" + userId + "): None");
            }
            if (this.mStartUserBeforeScheduledAlarms && this.mActivityManagerInternal.isUserRunning(userId, 0)) {
                this.mUserWakeupStore.removeUserWakeup(userId);
            }
            this.mNextAlarmClockForUser.remove(userId);
        }
        this.mPendingSendNextAlarmClockChangedForUser.put(userId, true);
        this.mHandler.removeMessages(2);
        this.mHandler.sendEmptyMessage(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendNextAlarmClockChanged() {
        android.util.SparseArray<android.app.AlarmManager.AlarmClockInfo> pendingUsers = this.mHandlerSparseAlarmClockArray;
        pendingUsers.clear();
        synchronized (this.mLock) {
            int n = this.mPendingSendNextAlarmClockChangedForUser.size();
            for (int i = 0; i < n; i++) {
                int userId = this.mPendingSendNextAlarmClockChangedForUser.keyAt(i);
                pendingUsers.append(userId, this.mNextAlarmClockForUser.get(userId));
            }
            this.mPendingSendNextAlarmClockChangedForUser.clear();
        }
        int n2 = pendingUsers.size();
        for (int i2 = 0; i2 < n2; i2++) {
            int userId2 = pendingUsers.keyAt(i2);
            android.app.AlarmManager.AlarmClockInfo alarmClock = pendingUsers.valueAt(i2);
            android.provider.Settings.System.putStringForUser(getContext().getContentResolver(), "next_alarm_formatted", formatNextAlarm(getContext(), alarmClock, userId2), userId2);
            getContext().sendBroadcastAsUser(NEXT_ALARM_CLOCK_CHANGED_INTENT, new android.os.UserHandle(userId2));
        }
    }

    private static java.lang.String formatNextAlarm(android.content.Context context, android.app.AlarmManager.AlarmClockInfo info, int userId) {
        java.lang.String skeleton = android.text.format.DateFormat.is24HourFormat(context, userId) ? "EHm" : "Ehma";
        java.lang.String pattern = android.text.format.DateFormat.getBestDateTimePattern(java.util.Locale.getDefault(), skeleton);
        return info == null ? "" : android.text.format.DateFormat.format(pattern, info.getTriggerTime()).toString();
    }

    void rescheduleKernelAlarmsLocked() {
        long nowElapsed = this.mInjector.getElapsedRealtimeMillis();
        long nextNonWakeup = 0;
        if (this.mAlarmStore.size() > 0) {
            long firstWakeup = this.mAlarmStore.getNextWakeupDeliveryTime();
            if (this.mStartUserBeforeScheduledAlarms && this.mUserWakeupStore != null) {
                long firstUserWakeup = this.mUserWakeupStore.getNextWakeupTime();
                if (firstUserWakeup >= 0 && firstUserWakeup < firstWakeup) {
                    firstWakeup = firstUserWakeup;
                }
            }
            long first = this.mAlarmStore.getNextDeliveryTime();
            if (firstWakeup != 0) {
                this.mNextWakeup = firstWakeup;
                this.mNextWakeUpSetAt = nowElapsed;
                setLocked(2, firstWakeup);
            }
            if (first != firstWakeup) {
                nextNonWakeup = first;
            }
        }
        if (this.mPendingNonWakeupAlarms.size() > 0 && (nextNonWakeup == 0 || this.mNextNonWakeupDeliveryTime < nextNonWakeup)) {
            nextNonWakeup = this.mNextNonWakeupDeliveryTime;
        }
        if (nextNonWakeup != 0) {
            this.mNextNonWakeup = nextNonWakeup;
            this.mNextNonWakeUpSetAt = nowElapsed;
            setLocked(3, nextNonWakeup);
        }
    }

    void removeExactAlarmsOnPermissionRevoked(final int uid, final java.lang.String packageName, boolean killUid) {
        if (isExemptFromExactAlarmPermissionNoLock(uid) || !isExactAlarmChangeEnabled(packageName, android.os.UserHandle.getUserId(uid))) {
            return;
        }
        android.util.Slog.w(TAG, "Package " + packageName + ", uid " + uid + " lost permission to set exact alarms!");
        java.util.function.Predicate<com.android.server.alarm.Alarm> whichAlarms = new java.util.function.Predicate() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda24
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.alarm.AlarmManagerService.lambda$removeExactAlarmsOnPermissionRevoked$13(uid, packageName, (com.android.server.alarm.Alarm) obj);
            }
        };
        synchronized (this.mLock) {
            removeAlarmsInternalLocked(whichAlarms, 2);
        }
        if (killUid) {
            com.android.server.pm.permission.PermissionManagerService.killUid(android.os.UserHandle.getAppId(uid), android.os.UserHandle.getUserId(uid), "schedule_exact_alarm revoked");
        }
    }

    static /* synthetic */ boolean lambda$removeExactAlarmsOnPermissionRevoked$13(int uid, java.lang.String packageName, com.android.server.alarm.Alarm a) {
        return a.uid == uid && a.packageName.equals(packageName) && a.windowLength == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeAlarmsInternalLocked(java.util.function.Predicate<com.android.server.alarm.Alarm> whichAlarms, int reason) {
        long nowRtc = this.mInjector.getCurrentTimeMillis();
        long nowElapsed = this.mInjector.getElapsedRealtimeMillis();
        java.util.ArrayList<com.android.server.alarm.Alarm> removedAlarms = this.mAlarmStore.remove(whichAlarms);
        int i = 1;
        boolean removedFromStore = !removedAlarms.isEmpty();
        for (int i2 = this.mPendingBackgroundAlarms.size() - 1; i2 >= 0; i2--) {
            java.util.ArrayList<com.android.server.alarm.Alarm> alarmsForUid = this.mPendingBackgroundAlarms.valueAt(i2);
            for (int j = alarmsForUid.size() - 1; j >= 0; j--) {
                com.android.server.alarm.Alarm alarm = alarmsForUid.get(j);
                if (whichAlarms.test(alarm)) {
                    removedAlarms.add(alarmsForUid.remove(j));
                }
            }
            int j2 = alarmsForUid.size();
            if (j2 == 0) {
                this.mPendingBackgroundAlarms.removeAt(i2);
            }
        }
        for (int i3 = this.mPendingNonWakeupAlarms.size() - 1; i3 >= 0; i3--) {
            com.android.server.alarm.Alarm a = this.mPendingNonWakeupAlarms.get(i3);
            if (whichAlarms.test(a)) {
                removedAlarms.add(this.mPendingNonWakeupAlarms.remove(i3));
            }
        }
        for (com.android.server.alarm.Alarm removed : removedAlarms) {
            decrementAlarmCount(removed.uid, i);
            if (removed.listener != null) {
                removed.listener.asBinder().unlinkToDeath(this.mListenerDeathRecipient, 0);
            }
            if (com.android.server.alarm.AlarmManagerService.RemovedAlarm.isLoggable(reason)) {
                com.android.internal.util.RingBuffer<com.android.server.alarm.AlarmManagerService.RemovedAlarm> bufferForUid = this.mRemovalHistory.get(removed.uid);
                if (bufferForUid == null) {
                    bufferForUid = new com.android.internal.util.RingBuffer<>(com.android.server.alarm.AlarmManagerService.RemovedAlarm.class, 10);
                    this.mRemovalHistory.put(removed.uid, bufferForUid);
                }
                bufferForUid.append(new com.android.server.alarm.AlarmManagerService.RemovedAlarm(removed, reason, nowRtc, nowElapsed));
                removedAlarms = removedAlarms;
                i = 1;
            }
        }
        if (removedFromStore) {
            boolean idleUntilUpdated = false;
            if (this.mPendingIdleUntil != null && whichAlarms.test(this.mPendingIdleUntil)) {
                this.mPendingIdleUntil = null;
                idleUntilUpdated = true;
            }
            if (this.mNextWakeFromIdle != null && whichAlarms.test(this.mNextWakeFromIdle)) {
                this.mNextWakeFromIdle = this.mAlarmStore.getNextWakeFromIdleAlarm();
                if (this.mPendingIdleUntil != null) {
                    idleUntilUpdated |= this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda20
                        @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                        public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm2) {
                            return this.f$0.lambda$removeAlarmsInternalLocked$14(alarm2);
                        }
                    });
                }
            }
            if (idleUntilUpdated) {
                this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda21
                    @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                    public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm2) {
                        return this.f$0.lambda$removeAlarmsInternalLocked$15(alarm2);
                    }
                });
            }
            rescheduleKernelAlarmsLocked();
            updateNextAlarmClockLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$removeAlarmsInternalLocked$14(com.android.server.alarm.Alarm alarm) {
        return alarm == this.mPendingIdleUntil && adjustIdleUntilTime(alarm);
    }

    void removeLocked(final android.app.PendingIntent operation, final android.app.IAlarmListener directReceiver, int reason) {
        if (operation == null && directReceiver == null) {
            if (this.mAmsExt.isDynamicLogEnabled()) {
                android.util.Slog.w(TAG, "requested remove() of null operation", new java.lang.RuntimeException("here"));
                return;
            }
            return;
        }
        removeAlarmsInternalLocked(new java.util.function.Predicate() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda19
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.alarm.Alarm) obj).matches(operation, directReceiver);
            }
        }, reason);
    }

    void removeLocked(final int uid, int reason) {
        if (uid == 1000) {
            return;
        }
        removeAlarmsInternalLocked(new java.util.function.Predicate() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda22
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.alarm.AlarmManagerService.lambda$removeLocked$17(uid, (com.android.server.alarm.Alarm) obj);
            }
        }, reason);
    }

    static /* synthetic */ boolean lambda$removeLocked$17(int uid, com.android.server.alarm.Alarm a) {
        return a.uid == uid;
    }

    void removeLocked(final java.lang.String packageName, int reason) {
        if (packageName == null) {
            if (this.mAmsExt.isDynamicLogEnabled()) {
                android.util.Slog.w(TAG, "requested remove() of null packageName", new java.lang.RuntimeException("here"));
                return;
            }
            return;
        }
        removeAlarmsInternalLocked(new java.util.function.Predicate() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.alarm.Alarm) obj).matches(packageName);
            }
        }, reason);
    }

    void removeForStoppedLocked(final int uid) {
        if (uid == 1000) {
            return;
        }
        java.util.function.Predicate<com.android.server.alarm.Alarm> whichAlarms = new java.util.function.Predicate() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda18
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$removeForStoppedLocked$19(uid, (com.android.server.alarm.Alarm) obj);
            }
        };
        removeAlarmsInternalLocked(whichAlarms, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$removeForStoppedLocked$19(int uid, com.android.server.alarm.Alarm a) {
        return a.uid == uid && this.mActivityManagerInternal.isAppStartModeDisabled(uid, a.packageName);
    }

    void removeUserLocked(final int userHandle) {
        if (userHandle == 0) {
            android.util.Slog.w(TAG, "Ignoring attempt to remove system-user state!");
            return;
        }
        java.util.function.Predicate<com.android.server.alarm.Alarm> whichAlarms = new java.util.function.Predicate() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.alarm.AlarmManagerService.lambda$removeUserLocked$20(userHandle, (com.android.server.alarm.Alarm) obj);
            }
        };
        removeAlarmsInternalLocked(whichAlarms, 0);
        for (int i = this.mLastPriorityAlarmDispatch.size() - 1; i >= 0; i--) {
            if (android.os.UserHandle.getUserId(this.mLastPriorityAlarmDispatch.keyAt(i)) == userHandle) {
                this.mLastPriorityAlarmDispatch.removeAt(i);
            }
        }
        for (int i2 = this.mRemovalHistory.size() - 1; i2 >= 0; i2--) {
            if (android.os.UserHandle.getUserId(this.mRemovalHistory.keyAt(i2)) == userHandle) {
                this.mRemovalHistory.removeAt(i2);
            }
        }
        for (int i3 = this.mLastOpScheduleExactAlarm.size() - 1; i3 >= 0; i3--) {
            if (android.os.UserHandle.getUserId(this.mLastOpScheduleExactAlarm.keyAt(i3)) == userHandle) {
                this.mLastOpScheduleExactAlarm.removeAt(i3);
            }
        }
    }

    static /* synthetic */ boolean lambda$removeUserLocked$20(int userHandle, com.android.server.alarm.Alarm a) {
        return android.os.UserHandle.getUserId(a.uid) == userHandle;
    }

    void interactiveStateChangedLocked(boolean interactive) throws java.lang.Exception {
        if (this.mInteractive != interactive) {
            this.mInteractive = interactive;
            long nowELAPSED = this.mInjector.getElapsedRealtimeMillis();
            if (interactive) {
                if (this.mPendingNonWakeupAlarms.size() > 0) {
                    long thisDelayTime = nowELAPSED - this.mStartCurrentDelayTime;
                    this.mTotalDelayTime += thisDelayTime;
                    if (this.mMaxDelayTime < thisDelayTime) {
                        this.mMaxDelayTime = thisDelayTime;
                    }
                    java.util.ArrayList<com.android.server.alarm.Alarm> triggerList = new java.util.ArrayList<>(this.mPendingNonWakeupAlarms);
                    deliverAlarmsLocked(triggerList, nowELAPSED);
                    this.mPendingNonWakeupAlarms.clear();
                }
                if (this.mNonInteractiveStartTime > 0) {
                    long dur = nowELAPSED - this.mNonInteractiveStartTime;
                    if (dur > this.mNonInteractiveTime) {
                        this.mNonInteractiveTime = dur;
                    }
                }
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$interactiveStateChangedLocked$21();
                    }
                });
                this.mAmsExt.onScreenOn();
                return;
            }
            this.mNonInteractiveStartTime = nowELAPSED;
            this.mAmsExt.onScreenOff();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$interactiveStateChangedLocked$21() {
        getContext().sendBroadcastAsUser(this.mTimeTickIntent, android.os.UserHandle.ALL, null, this.mTimeTickOptions);
    }

    boolean lookForPackageLocked(java.lang.String packageName, int uid) {
        for (com.android.server.alarm.Alarm alarm : this.mAlarmStore.asList()) {
            if (alarm.matches(packageName) && alarm.creatorUid == uid) {
                return true;
            }
        }
        java.util.ArrayList<com.android.server.alarm.Alarm> alarmsForUid = this.mPendingBackgroundAlarms.get(uid);
        if (alarmsForUid != null) {
            java.util.Iterator<com.android.server.alarm.Alarm> it = alarmsForUid.iterator();
            while (it.hasNext()) {
                if (it.next().matches(packageName)) {
                    return true;
                }
            }
        }
        for (com.android.server.alarm.Alarm alarm2 : this.mPendingNonWakeupAlarms) {
            if (alarm2.matches(packageName) && alarm2.creatorUid == uid) {
                return true;
            }
        }
        return false;
    }

    private void setLocked(int type, long when) {
        if (this.mInjector.isAlarmDriverPresent()) {
            this.mInjector.setAlarm(type, when);
            return;
        }
        android.os.Message msg = android.os.Message.obtain();
        msg.what = 1;
        this.mHandler.removeMessages(msg.what);
        this.mHandler.sendMessageAtTime(msg, when);
    }

    static final void dumpAlarmList(android.util.IndentingPrintWriter ipw, java.util.ArrayList<com.android.server.alarm.Alarm> list, long nowELAPSED, java.text.SimpleDateFormat sdf) {
        int n = list.size();
        for (int i = n - 1; i >= 0; i--) {
            com.android.server.alarm.Alarm a = list.get(i);
            java.lang.String label = com.android.server.alarm.Alarm.typeToString(a.type);
            ipw.print(label);
            ipw.print(" #");
            ipw.print(n - i);
            ipw.print(": ");
            ipw.println(a);
            ipw.increaseIndent();
            a.dump(ipw, nowELAPSED, sdf);
            ipw.decreaseIndent();
        }
    }

    private static boolean isExemptFromBatterySaver(com.android.server.alarm.Alarm alarm) {
        if (alarm.alarmClock != null) {
            return true;
        }
        return (alarm.operation != null && (alarm.operation.isActivity() || alarm.operation.isForegroundService())) || android.os.UserHandle.isCore(alarm.creatorUid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBackgroundRestricted(com.android.server.alarm.Alarm alarm) {
        if (alarm.alarmClock != null) {
            return false;
        }
        if (alarm.operation != null && alarm.operation.isActivity()) {
            return false;
        }
        java.lang.String sourcePackage = alarm.sourcePackage;
        int sourceUid = alarm.creatorUid;
        if (android.os.UserHandle.isCore(sourceUid)) {
            return false;
        }
        if (this.mAmsExt.isBackgroundRestricted(alarm)) {
            return true;
        }
        return this.mAppStateTracker != null && this.mAppStateTracker.areAlarmsRestricted(sourceUid, sourcePackage);
    }

    int triggerAlarmsLocked(java.util.ArrayList<com.android.server.alarm.Alarm> triggerList, long nowELAPSED) {
        com.android.server.alarm.Alarm alarm;
        com.android.server.alarm.AlarmManagerService alarmManagerService;
        final com.android.server.alarm.AlarmManagerService alarmManagerService2 = this;
        java.util.ArrayList<com.android.server.alarm.Alarm> arrayList = triggerList;
        long j = nowELAPSED;
        java.util.ArrayList<com.android.server.alarm.Alarm> pendingAlarms = alarmManagerService2.mAlarmStore.removePendingAlarms(j);
        int wakeUps = 0;
        for (com.android.server.alarm.Alarm alarm2 : pendingAlarms) {
            if (alarmManagerService2.isBackgroundRestricted(alarm2)) {
                if (alarmManagerService2.mAmsExt.isDynamicLogEnabled()) {
                    android.util.Slog.d(TAG, "Deferring alarm " + alarm2 + " due to user forced app standby");
                }
                java.util.ArrayList<com.android.server.alarm.Alarm> alarmsForUid = alarmManagerService2.mPendingBackgroundAlarms.get(alarm2.creatorUid);
                if (alarmsForUid == null) {
                    alarmsForUid = new java.util.ArrayList<>();
                    alarmManagerService2.mPendingBackgroundAlarms.put(alarm2.creatorUid, alarmsForUid);
                }
                alarmsForUid.add(alarm2);
            } else {
                alarmManagerService2.mAmsExt.filterAlarmForHans(alarm2);
                alarm2.count = 1;
                arrayList.add(alarm2);
                if ((alarm2.flags & 2) != 0) {
                    com.android.server.EventLogTags.writeDeviceIdleWakeFromIdle(alarmManagerService2.mPendingIdleUntil != null ? 1 : 0, alarm2.statsTag);
                }
                if (alarmManagerService2.mPendingIdleUntil == alarm2) {
                    alarmManagerService2.mPendingIdleUntil = null;
                    alarmManagerService2.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$$ExternalSyntheticLambda7
                        @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                        public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm3) {
                            return this.f$0.lambda$triggerAlarmsLocked$22(alarm3);
                        }
                    });
                }
                if (alarmManagerService2.mNextWakeFromIdle == alarm2) {
                    alarmManagerService2.mNextWakeFromIdle = alarmManagerService2.mAlarmStore.getNextWakeFromIdleAlarm();
                }
                if (alarm2.repeatInterval <= 0) {
                    alarm = alarm2;
                } else {
                    alarm2.count = (int) (((long) alarm2.count) + ((j - alarm2.getRequestedElapsed()) / alarm2.repeatInterval));
                    long delta = ((long) alarm2.count) * alarm2.repeatInterval;
                    long nextElapsed = alarm2.getRequestedElapsed() + delta;
                    long nextMaxElapsed = maxTriggerTime(nowELAPSED, nextElapsed, alarm2.repeatInterval);
                    alarm = alarm2;
                    setImplLocked(alarm2.type, alarm2.origWhen + delta, nextElapsed, nextMaxElapsed - nextElapsed, alarm2.repeatInterval, alarm2.operation, null, null, alarm2.flags, alarm2.workSource, alarm2.alarmClock, alarm2.uid, alarm2.packageName, alarm2.getWrapper().getExt().getProcName(), null, -1, alarm2.getWrapper().getExt().getAction(), alarm2.getWrapper().getExt().getComponent());
                }
                com.android.server.alarm.Alarm alarm3 = alarm;
                if (alarm3.wakeup) {
                    wakeUps++;
                }
                if (alarm3.alarmClock == null) {
                    alarmManagerService = this;
                } else {
                    alarmManagerService = this;
                    alarmManagerService.mNextAlarmClockMayChange = true;
                }
                arrayList = triggerList;
                j = nowELAPSED;
                alarmManagerService2 = alarmManagerService;
            }
        }
        com.android.server.alarm.AlarmManagerService alarmManagerService3 = alarmManagerService2;
        calculateDeliveryPriorities(triggerList);
        java.util.Collections.sort(triggerList, alarmManagerService3.mAlarmDispatchComparator);
        if (alarmManagerService3.mAmsExt.isDynamicLogEnabled()) {
            for (int i = 0; i < triggerList.size(); i++) {
                android.util.Slog.v(TAG, "Triggering alarm #" + i + ": " + triggerList.get(i));
            }
        }
        return wakeUps;
    }

    long currentNonWakeupFuzzLocked(long nowELAPSED) {
        long timeSinceOn = nowELAPSED - this.mNonInteractiveStartTime;
        if (timeSinceOn < 300000) {
            return 120000L;
        }
        if (timeSinceOn < 1800000) {
            return 900000L;
        }
        return 3600000L;
    }

    boolean checkAllowNonWakeupDelayLocked(long nowELAPSED) {
        if (!this.mConstants.DELAY_NONWAKEUP_ALARMS_WHILE_SCREEN_OFF || this.mInteractive || this.mLastAlarmDeliveryTime <= 0) {
            return false;
        }
        if (this.mPendingNonWakeupAlarms.size() > 0 && this.mNextNonWakeupDeliveryTime < nowELAPSED) {
            return false;
        }
        long timeSinceLast = nowELAPSED - this.mLastAlarmDeliveryTime;
        return timeSinceLast <= currentNonWakeupFuzzLocked(nowELAPSED);
    }

    public void deliverAlarmsLocked(java.util.ArrayList<com.android.server.alarm.Alarm> triggerList, long nowELAPSED) throws java.lang.Exception {
        this.mAmsExt.deliverAlarmsLockedStart();
        this.mLastAlarmDeliveryTime = nowELAPSED;
        for (int i = 0; i < triggerList.size(); i++) {
            com.android.server.alarm.Alarm alarm = triggerList.get(i);
            if (alarm.wakeup) {
                android.os.Trace.traceBegin(131072L, "Dispatch wakeup alarm to " + alarm.packageName);
            } else {
                android.os.Trace.traceBegin(131072L, "Dispatch non-wakeup alarm to " + alarm.packageName);
            }
            try {
                android.util.Slog.v(TAG, "sending alarm " + alarm);
                this.mActivityManagerInternal.noteAlarmStart(alarm.operation, alarm.workSource, alarm.uid, alarm.statsTag);
                if (!this.mAmsExt.interceptDeliverAlarmsLockedInLoop(alarm)) {
                    this.mDeliveryTracker.deliverLocked(alarm, nowELAPSED);
                }
            } catch (java.lang.RuntimeException e) {
                android.util.Slog.w(TAG, "Failure sending alarm.", e);
            }
            android.os.Trace.traceEnd(131072L);
            decrementAlarmCount(alarm.uid, 1);
        }
        this.mAmsExt.deliverAlarmsLockedEnd();
    }

    static boolean isExemptFromAppStandby(com.android.server.alarm.Alarm a) {
        return (a.alarmClock == null && !android.os.UserHandle.isCore(a.creatorUid) && (a.flags & 12) == 0) ? false : true;
    }

    static class Injector {
        private android.content.Context mContext;
        private long mNativeData;

        Injector(android.content.Context context) {
            this.mContext = context;
        }

        void init() {
            java.lang.System.loadLibrary("alarm_jni");
            this.mNativeData = com.android.server.alarm.AlarmManagerService.init();
        }

        int waitForAlarm() {
            return com.android.server.alarm.AlarmManagerService.waitForAlarm(this.mNativeData);
        }

        boolean isAlarmDriverPresent() {
            return this.mNativeData != 0;
        }

        void setAlarm(int type, long millis) {
            long alarmSeconds;
            long alarmSeconds2;
            if (millis < 0) {
                alarmSeconds = 0;
                alarmSeconds2 = 0;
            } else {
                long alarmSeconds3 = millis / 1000;
                alarmSeconds = alarmSeconds3;
                alarmSeconds2 = 1000 * (millis % 1000) * 1000;
            }
            int result = com.android.server.alarm.AlarmManagerService.set(this.mNativeData, type, alarmSeconds, alarmSeconds2);
            if (result != 0) {
                long nowElapsed = android.os.SystemClock.elapsedRealtime();
                android.util.Slog.wtf(com.android.server.alarm.AlarmManagerService.TAG, "Unable to set kernel alarm, now=" + nowElapsed + " type=" + type + " @ (" + alarmSeconds + "," + alarmSeconds2 + "), ret = " + result + " = " + android.system.Os.strerror(result));
            }
        }

        int getCallingUid() {
            return android.os.Binder.getCallingUid();
        }

        long getNextAlarm(int type) {
            return com.android.server.alarm.AlarmManagerService.getNextAlarm(this.mNativeData, type);
        }

        void initializeTimeIfRequired() {
            com.android.server.SystemClockTime.initializeIfRequired();
        }

        void setCurrentTimeMillis(long unixEpochMillis, int confidence, java.lang.String logMsg) {
            com.android.server.SystemClockTime.setTimeAndConfidence(unixEpochMillis, confidence, logMsg);
        }

        void close() {
            com.android.server.alarm.AlarmManagerService.close(this.mNativeData);
        }

        long getElapsedRealtimeMillis() {
            return android.os.SystemClock.elapsedRealtime();
        }

        long getCurrentTimeMillis() {
            return java.lang.System.currentTimeMillis();
        }

        android.os.PowerManager.WakeLock getAlarmWakeLock() {
            android.os.PowerManager pm = (android.os.PowerManager) this.mContext.getSystemService("power");
            return pm.newWakeLock(1, "*alarm*");
        }

        int getSystemUiUid(android.content.pm.PackageManagerInternal pm) {
            return pm.getPackageUid(pm.getSystemUiServiceComponent().getPackageName(), 1048576L, 0);
        }

        com.android.internal.app.IAppOpsService getAppOpsService() {
            return com.android.internal.app.IAppOpsService.Stub.asInterface(android.os.ServiceManager.getService("appops"));
        }

        com.android.server.alarm.AlarmManagerService.ClockReceiver getClockReceiver(com.android.server.alarm.AlarmManagerService service) {
            java.util.Objects.requireNonNull(service);
            return service.new ClockReceiver();
        }

        void registerDeviceConfigListener(android.provider.DeviceConfig.OnPropertiesChangedListener listener) {
            android.provider.DeviceConfig.addOnPropertiesChangedListener("alarm_manager", com.android.server.AppSchedulingModuleThread.getExecutor(), listener);
        }
    }

    private class AlarmThread extends java.lang.Thread {
        private int mFalseWakeups;
        private int mWtfThreshold;

        AlarmThread() {
            super(com.android.server.alarm.AlarmManagerService.TAG);
            this.mFalseWakeups = 0;
            this.mWtfThreshold = 100;
        }

        /* JADX WARN: Removed duplicated region for block: B:35:0x0139  */
        /* JADX WARN: Removed duplicated region for block: B:48:0x015c A[Catch: all -> 0x0378, TRY_ENTER, TryCatch #5 {all -> 0x0378, blocks: (B:40:0x0143, B:53:0x01bf, B:62:0x01ed, B:71:0x0241, B:77:0x0282, B:88:0x02ce, B:89:0x02e3, B:91:0x02e9, B:94:0x0302, B:96:0x0308, B:48:0x015c, B:52:0x01b4), top: B:141:0x0143 }] */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:144:? -> B:115:0x037d). Please report as a decompilation issue!!! */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 920
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.alarm.AlarmManagerService.AlarmThread.run():void");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void increment(android.util.SparseIntArray array, int key) {
        int index = array.indexOfKey(key);
        if (index >= 0) {
            array.setValueAt(index, array.valueAt(index) + 1);
        } else {
            array.put(key, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAlarmBatchDelivered(int alarms, int wakeups, android.util.SparseIntArray countsPerUid, android.util.SparseIntArray wakeupCountsPerUid) {
        int[] uids = new int[countsPerUid.size()];
        int[] countsArray = new int[countsPerUid.size()];
        int[] wakeupCountsArray = new int[countsPerUid.size()];
        for (int i = 0; i < countsPerUid.size(); i++) {
            uids[i] = countsPerUid.keyAt(i);
            countsArray[i] = countsPerUid.valueAt(i);
            wakeupCountsArray[i] = wakeupCountsPerUid.get(uids[i], 0);
        }
        com.android.server.alarm.MetricsHelper.pushAlarmBatchDelivered(alarms, wakeups, uids, countsArray, wakeupCountsArray);
    }

    void setWakelockWorkSource(android.os.WorkSource ws, int knownUid, java.lang.String tag, boolean first) {
        try {
            this.mWakeLock.setHistoryTag(first ? tag : null);
        } catch (java.lang.Exception e) {
        }
        if (ws != null) {
            this.mWakeLock.setWorkSource(ws);
            return;
        }
        if (knownUid >= 0) {
            this.mWakeLock.setWorkSource(new android.os.WorkSource(knownUid));
            return;
        }
        this.mWakeLock.setWorkSource(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getAlarmAttributionUid(com.android.server.alarm.Alarm alarm) {
        if (alarm.workSource != null && !alarm.workSource.isEmpty()) {
            return alarm.workSource.getAttributionUid();
        }
        return alarm.creatorUid;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.Bundle getAlarmOperationBundle(com.android.server.alarm.Alarm alarm) {
        if (alarm.mIdleOptions != null) {
            return alarm.mIdleOptions;
        }
        if (alarm.operation.isActivity()) {
            return this.mActivityOptsRestrictBal.toBundle();
        }
        return this.mBroadcastOptsRestrictBal.toBundle();
    }

    class AlarmHandler extends android.os.Handler {
        public static final int ALARM_EVENT = 1;
        public static final int APP_STANDBY_BUCKET_CHANGED = 5;
        public static final int CHARGING_STATUS_CHANGED = 6;
        public static final int CHECK_EXACT_ALARM_PERMISSION_ON_UPDATE = 13;
        public static final int LISTENER_TIMEOUT = 3;
        public static final int REFRESH_EXACT_ALARM_CANDIDATES = 11;
        public static final int REMOVE_EXACT_ALARMS = 8;
        public static final int REMOVE_EXACT_LISTENER_ALARMS_ON_CACHED = 15;
        public static final int REMOVE_FOR_CANCELED = 7;
        public static final int REPORT_ALARMS_ACTIVE = 4;
        public static final int SEND_NEXT_ALARM_CLOCK_CHANGED = 2;
        public static final int TEMPORARY_QUOTA_CHANGED = 14;

        AlarmHandler() {
            super(android.os.Looper.myLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    java.util.ArrayList<com.android.server.alarm.Alarm> triggerList = new java.util.ArrayList<>();
                    synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                        long nowELAPSED = com.android.server.alarm.AlarmManagerService.this.mInjector.getElapsedRealtimeMillis();
                        com.android.server.alarm.AlarmManagerService.this.triggerAlarmsLocked(triggerList, nowELAPSED);
                        com.android.server.alarm.AlarmManagerService.this.updateNextAlarmClockLocked();
                        break;
                    }
                    for (int i = 0; i < triggerList.size(); i++) {
                        com.android.server.alarm.Alarm alarm = triggerList.get(i);
                        try {
                            android.os.Bundle bundle = com.android.server.alarm.AlarmManagerService.this.getAlarmOperationBundle(alarm);
                            alarm.operation.send(null, 0, null, null, null, null, bundle);
                        } catch (android.app.PendingIntent.CanceledException e) {
                            if (alarm.repeatInterval > 0) {
                                com.android.server.alarm.AlarmManagerService.this.removeImpl(alarm.operation, null);
                            }
                        }
                        com.android.server.alarm.AlarmManagerService.this.decrementAlarmCount(alarm.uid, 1);
                    }
                    return;
                case 2:
                    com.android.server.alarm.AlarmManagerService.this.sendNextAlarmClockChanged();
                    return;
                case 3:
                    com.android.server.alarm.AlarmManagerService.this.mDeliveryTracker.alarmTimedOut((android.os.IBinder) msg.obj);
                    return;
                case 4:
                    if (com.android.server.alarm.AlarmManagerService.this.mLocalDeviceIdleController != null) {
                        com.android.server.alarm.AlarmManagerService.this.mLocalDeviceIdleController.setAlarmsActive(msg.arg1 != 0);
                        return;
                    }
                    return;
                case 5:
                case 14:
                    synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                        android.util.ArraySet<android.content.pm.UserPackage> filterPackages = new android.util.ArraySet<>();
                        filterPackages.add(android.content.pm.UserPackage.of(msg.arg1, (java.lang.String) msg.obj));
                        if (com.android.server.alarm.AlarmManagerService.this.reorderAlarmsBasedOnStandbyBuckets(filterPackages)) {
                            com.android.server.alarm.AlarmManagerService.this.rescheduleKernelAlarmsLocked();
                            com.android.server.alarm.AlarmManagerService.this.updateNextAlarmClockLocked();
                        }
                        break;
                    }
                    return;
                case 6:
                    synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                        com.android.server.alarm.AlarmManagerService.this.mAppStandbyParole = ((java.lang.Boolean) msg.obj).booleanValue();
                        if (com.android.server.alarm.AlarmManagerService.this.reorderAlarmsBasedOnStandbyBuckets(null)) {
                            com.android.server.alarm.AlarmManagerService.this.rescheduleKernelAlarmsLocked();
                            com.android.server.alarm.AlarmManagerService.this.updateNextAlarmClockLocked();
                        }
                        break;
                    }
                    return;
                case 7:
                    android.app.PendingIntent operation = (android.app.PendingIntent) msg.obj;
                    synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                        com.android.server.alarm.AlarmManagerService.this.removeLocked(operation, null, 4);
                        break;
                    }
                    return;
                case 8:
                    com.android.server.alarm.AlarmManagerService.this.removeExactAlarmsOnPermissionRevoked(msg.arg1, (java.lang.String) msg.obj, true);
                    return;
                case 9:
                case 10:
                case 12:
                default:
                    return;
                case 11:
                    com.android.server.alarm.AlarmManagerService.this.refreshExactAlarmCandidates();
                    return;
                case 13:
                    java.lang.String packageName = (java.lang.String) msg.obj;
                    int uid = msg.arg1;
                    if (!com.android.server.alarm.AlarmManagerService.this.hasScheduleExactAlarmInternal(packageName, uid) && !com.android.server.alarm.AlarmManagerService.this.hasUseExactAlarmInternal(packageName, uid)) {
                        com.android.server.alarm.AlarmManagerService.this.removeExactAlarmsOnPermissionRevoked(uid, packageName, false);
                        return;
                    }
                    return;
                case 15:
                    com.android.server.alarm.AlarmManagerService.this.removeExactListenerAlarms(((java.lang.Integer) msg.obj).intValue());
                    return;
            }
        }
    }

    class ChargingReceiver extends android.content.BroadcastReceiver {
        ChargingReceiver() {
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.os.action.CHARGING");
            filter.addAction("android.os.action.DISCHARGING");
            com.android.server.alarm.AlarmManagerService.this.getContext().registerReceiver(this, filter);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            boolean charging;
            java.lang.String action = intent.getAction();
            if ("android.os.action.CHARGING".equals(action)) {
                charging = true;
            } else {
                charging = false;
            }
            com.android.server.alarm.AlarmManagerService.this.mHandler.removeMessages(6);
            com.android.server.alarm.AlarmManagerService.this.mHandler.obtainMessage(6, java.lang.Boolean.valueOf(charging)).sendToTarget();
        }
    }

    class ClockReceiver extends android.content.BroadcastReceiver {
        public ClockReceiver() {
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.intent.action.DATE_CHANGED");
            com.android.server.alarm.AlarmManagerService.this.getContext().registerReceiver(this, filter);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) throws java.lang.Throwable {
            if (intent.getAction().equals("android.intent.action.DATE_CHANGED")) {
                scheduleDateChangedEvent();
            }
        }

        public void scheduleTimeTickEvent() throws java.lang.Throwable {
            long currentTime = com.android.server.alarm.AlarmManagerService.this.mInjector.getCurrentTimeMillis();
            long nextTime = ((currentTime / 60000) + 1) * 60000;
            long tickEventDelay = nextTime - currentTime;
            int flags = 1 | (com.android.server.alarm.AlarmManagerService.this.mConstants.TIME_TICK_ALLOWED_WHILE_IDLE ? 8 : 0);
            com.android.server.alarm.AlarmManagerService.this.setImpl(3, com.android.server.alarm.AlarmManagerService.this.mInjector.getElapsedRealtimeMillis() + tickEventDelay, 0L, 0L, null, com.android.server.alarm.AlarmManagerService.this.mTimeTickTrigger, com.android.server.alarm.AlarmManagerService.TIME_TICK_TAG, flags, null, null, android.os.Process.myUid(), com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, null, 1);
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.this.mLastTickSet = currentTime;
            }
        }

        public void scheduleDateChangedEvent() throws java.lang.Throwable {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(com.android.server.alarm.AlarmManagerService.this.mInjector.getCurrentTimeMillis());
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.set(14, 0);
            calendar.add(5, 1);
            com.android.server.alarm.AlarmManagerService.this.setImpl(1, calendar.getTimeInMillis(), 0L, 0L, com.android.server.alarm.AlarmManagerService.this.mDateChangeSender, null, null, 1, null, null, android.os.Process.myUid(), com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, null, 1);
        }
    }

    class InteractiveStateReceiver extends android.content.BroadcastReceiver {
        public InteractiveStateReceiver() {
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.intent.action.SCREEN_OFF");
            filter.addAction("android.intent.action.SCREEN_ON");
            filter.setPriority(1000);
            com.android.server.alarm.AlarmManagerService.this.getContext().registerReceiver(this, filter);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.this.interactiveStateChangedLocked("android.intent.action.SCREEN_ON".equals(intent.getAction()));
            }
        }
    }

    class UninstallReceiver extends android.content.BroadcastReceiver {
        public UninstallReceiver() {
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.intent.action.PACKAGE_REMOVED");
            filter.addAction("android.intent.action.PACKAGE_ADDED");
            filter.addAction("android.intent.action.PACKAGE_RESTARTED");
            filter.addAction("android.intent.action.QUERY_PACKAGE_RESTART");
            filter.addDataScheme("package");
            filter.addCategory("oplusBrEx@android.intent.action.PACKAGE_REMOVED@PACKAGE=NOREPLACING");
            com.android.server.alarm.AlarmManagerService.this.getContext().registerReceiverForAllUsers(this, filter, null, null);
            android.content.IntentFilter sdFilter = new android.content.IntentFilter();
            sdFilter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
            sdFilter.addAction("android.intent.action.USER_STOPPED");
            if (com.android.server.alarm.AlarmManagerService.this.mStartUserBeforeScheduledAlarms) {
                sdFilter.addAction("android.intent.action.LOCKED_BOOT_COMPLETED");
                sdFilter.addAction("android.intent.action.USER_REMOVED");
            }
            sdFilter.addAction("android.intent.action.UID_REMOVED");
            com.android.server.alarm.AlarmManagerService.this.getContext().registerReceiverForAllUsers(this, sdFilter, null, null);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:10:0x002d  */
        /* JADX WARN: Removed duplicated region for block: B:94:0x018a A[Catch: all -> 0x020e, TryCatch #0 {, blocks: (B:8:0x0020, B:9:0x002a, B:40:0x008c, B:94:0x018a, B:96:0x018d, B:98:0x0191, B:100:0x0195, B:102:0x01de, B:104:0x01e9, B:106:0x01f9, B:108:0x01ff, B:109:0x0206, B:110:0x0209, B:101:0x01d0, B:111:0x020c, B:42:0x0091, B:44:0x0099, B:46:0x009b, B:47:0x00a2, B:49:0x00a8, B:51:0x00ae, B:54:0x00bc, B:55:0x00c3, B:56:0x00cc, B:58:0x00db, B:59:0x00f0, B:61:0x00f2, B:62:0x010b, B:64:0x010d, B:66:0x0115, B:68:0x011d, B:69:0x0124, B:71:0x0126, B:73:0x012e, B:75:0x0136, B:76:0x013d, B:78:0x013f, B:80:0x0147, B:81:0x0168, B:83:0x016a, B:85:0x0174, B:87:0x017e, B:88:0x0181, B:90:0x0183, B:91:0x0186, B:11:0x002e, B:14:0x0038, B:17:0x0042, B:20:0x004c, B:23:0x0057, B:26:0x0061, B:29:0x006b, B:32:0x0075, B:35:0x007f), top: B:116:0x0020 }] */
        /* JADX WARN: Removed duplicated region for block: B:98:0x0191 A[Catch: all -> 0x020e, TryCatch #0 {, blocks: (B:8:0x0020, B:9:0x002a, B:40:0x008c, B:94:0x018a, B:96:0x018d, B:98:0x0191, B:100:0x0195, B:102:0x01de, B:104:0x01e9, B:106:0x01f9, B:108:0x01ff, B:109:0x0206, B:110:0x0209, B:101:0x01d0, B:111:0x020c, B:42:0x0091, B:44:0x0099, B:46:0x009b, B:47:0x00a2, B:49:0x00a8, B:51:0x00ae, B:54:0x00bc, B:55:0x00c3, B:56:0x00cc, B:58:0x00db, B:59:0x00f0, B:61:0x00f2, B:62:0x010b, B:64:0x010d, B:66:0x0115, B:68:0x011d, B:69:0x0124, B:71:0x0126, B:73:0x012e, B:75:0x0136, B:76:0x013d, B:78:0x013f, B:80:0x0147, B:81:0x0168, B:83:0x016a, B:85:0x0174, B:87:0x017e, B:88:0x0181, B:90:0x0183, B:91:0x0186, B:11:0x002e, B:14:0x0038, B:17:0x0042, B:20:0x004c, B:23:0x0057, B:26:0x0061, B:29:0x006b, B:32:0x0075, B:35:0x007f), top: B:116:0x0020 }] */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r12, android.content.Intent r13) {
            /*
                Method dump skipped, instruction units count: 590
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.alarm.AlarmManagerService.UninstallReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    private final class AppStandbyTracker extends com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener {
        private AppStandbyTracker() {
        }

        public void onAppIdleStateChanged(java.lang.String packageName, int userId, boolean idle, int bucket, int reason) {
            if (com.android.server.alarm.AlarmManagerService.this.mAmsExt.isDynamicLogEnabled()) {
                android.util.Slog.d(com.android.server.alarm.AlarmManagerService.TAG, "Package " + packageName + " for user " + userId + " now in bucket " + bucket);
            }
            com.android.server.alarm.AlarmManagerService.this.mHandler.obtainMessage(5, userId, -1, packageName).sendToTarget();
        }

        public void triggerTemporaryQuotaBump(java.lang.String packageName, int userId) {
            int quotaBump;
            int uid;
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                quotaBump = com.android.server.alarm.AlarmManagerService.this.mConstants.TEMPORARY_QUOTA_BUMP;
            }
            if (quotaBump <= 0 || (uid = com.android.server.alarm.AlarmManagerService.this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId)) < 0 || android.os.UserHandle.isCore(uid)) {
                return;
            }
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.this.mTemporaryQuotaReserve.replenishQuota(packageName, userId, quotaBump, com.android.server.alarm.AlarmManagerService.this.mInjector.getElapsedRealtimeMillis());
            }
            com.android.server.alarm.AlarmManagerService.this.mHandler.obtainMessage(14, userId, -1, packageName).sendToTarget();
        }
    }

    /* JADX INFO: renamed from: com.android.server.alarm.AlarmManagerService$7, reason: invalid class name */
    class AnonymousClass7 extends com.android.server.AppStateTrackerImpl.Listener {
        AnonymousClass7() {
        }

        @Override // com.android.server.AppStateTrackerImpl.Listener
        public void updateAllAlarms() {
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                if (com.android.server.alarm.AlarmManagerService.this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$7$$ExternalSyntheticLambda1
                    @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                    public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm) {
                        return this.f$0.lambda$updateAllAlarms$0(alarm);
                    }
                })) {
                    com.android.server.alarm.AlarmManagerService.this.rescheduleKernelAlarmsLocked();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$updateAllAlarms$0(com.android.server.alarm.Alarm a) {
            return com.android.server.alarm.AlarmManagerService.this.adjustDeliveryTimeBasedOnBatterySaver(a);
        }

        @Override // com.android.server.AppStateTrackerImpl.Listener
        public void updateAlarmsForUid(final int uid) {
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                if (com.android.server.alarm.AlarmManagerService.this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$7$$ExternalSyntheticLambda0
                    @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                    public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm) {
                        return this.f$0.lambda$updateAlarmsForUid$1(uid, alarm);
                    }
                })) {
                    com.android.server.alarm.AlarmManagerService.this.rescheduleKernelAlarmsLocked();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$updateAlarmsForUid$1(int uid, com.android.server.alarm.Alarm a) {
            if (a.creatorUid != uid) {
                return false;
            }
            return com.android.server.alarm.AlarmManagerService.this.adjustDeliveryTimeBasedOnBatterySaver(a);
        }

        @Override // com.android.server.AppStateTrackerImpl.Listener
        public void unblockAllUnrestrictedAlarms() {
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.this.sendAllUnrestrictedPendingBackgroundAlarmsLocked();
            }
        }

        @Override // com.android.server.AppStateTrackerImpl.Listener
        public void unblockAlarmsForUid(int uid) {
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.this.sendPendingBackgroundAlarmsLocked(uid, null);
            }
        }

        @Override // com.android.server.AppStateTrackerImpl.Listener
        public void unblockAlarmsForUidPackage(int uid, java.lang.String packageName) {
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.this.sendPendingBackgroundAlarmsLocked(uid, packageName);
            }
        }

        @Override // com.android.server.AppStateTrackerImpl.Listener
        public void removeAlarmsForUid(int uid) {
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.this.mAmsExt.trackEventRemoveAlarmsForUidByAppStandbyLocked(uid);
                com.android.server.alarm.AlarmManagerService.this.removeForStoppedLocked(uid);
            }
        }

        @Override // com.android.server.AppStateTrackerImpl.Listener
        public void handleUidCachedChanged(int uid, boolean cached) {
            long delay;
            if (com.android.server.alarm.AlarmManagerService.this.mUseFrozenStateToDropListenerAlarms || !android.app.compat.CompatChanges.isChangeEnabled(265195908L, uid)) {
                return;
            }
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                delay = com.android.server.alarm.AlarmManagerService.this.mConstants.CACHED_LISTENER_REMOVAL_DELAY;
            }
            java.lang.Integer uidObj = java.lang.Integer.valueOf(uid);
            if (!cached || com.android.server.alarm.AlarmManagerService.this.mHandler.hasEqualMessages(15, uidObj)) {
                com.android.server.alarm.AlarmManagerService.this.mHandler.removeEqualMessages(15, uidObj);
            } else {
                com.android.server.alarm.AlarmManagerService.this.mHandler.sendMessageDelayed(com.android.server.alarm.AlarmManagerService.this.mHandler.obtainMessage(15, uidObj), delay);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final com.android.server.alarm.AlarmManagerService.BroadcastStats getStatsLocked(android.app.PendingIntent pi) {
        java.lang.String pkg = pi.getCreatorPackage();
        int uid = pi.getCreatorUid();
        return getStatsLocked(uid, pkg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final com.android.server.alarm.AlarmManagerService.BroadcastStats getStatsLocked(int uid, java.lang.String pkgName) {
        android.util.ArrayMap<java.lang.String, com.android.server.alarm.AlarmManagerService.BroadcastStats> uidStats = this.mBroadcastStats.get(uid);
        if (uidStats == null) {
            uidStats = new android.util.ArrayMap<>();
            this.mBroadcastStats.put(uid, uidStats);
        }
        com.android.server.alarm.AlarmManagerService.BroadcastStats bs = uidStats.get(pkgName);
        if (bs == null) {
            com.android.server.alarm.AlarmManagerService.BroadcastStats bs2 = new com.android.server.alarm.AlarmManagerService.BroadcastStats(uid, pkgName);
            uidStats.put(pkgName, bs2);
            return bs2;
        }
        return bs;
    }

    class DeliveryTracker extends android.app.IAlarmCompleteListener.Stub implements android.app.PendingIntent.OnFinished {
        DeliveryTracker() {
        }

        private com.android.server.alarm.AlarmManagerService.InFlight removeLocked(android.app.PendingIntent pi, android.content.Intent intent) {
            for (int i = 0; i < com.android.server.alarm.AlarmManagerService.this.mInFlight.size(); i++) {
                com.android.server.alarm.AlarmManagerService.InFlight inflight = com.android.server.alarm.AlarmManagerService.this.mInFlight.get(i);
                if (inflight.mPendingIntent == pi) {
                    if (pi.isBroadcast()) {
                        com.android.server.alarm.AlarmManagerService.this.notifyBroadcastAlarmCompleteLocked(inflight.mUid);
                    }
                    return com.android.server.alarm.AlarmManagerService.this.mInFlight.remove(i);
                }
            }
            com.android.server.alarm.AlarmManagerService.this.mLog.w("No in-flight alarm for " + pi + " " + intent);
            return null;
        }

        private com.android.server.alarm.AlarmManagerService.InFlight removeLocked(android.os.IBinder listener) {
            for (int i = 0; i < com.android.server.alarm.AlarmManagerService.this.mInFlight.size(); i++) {
                if (com.android.server.alarm.AlarmManagerService.this.mInFlight.get(i).mListener == listener) {
                    return com.android.server.alarm.AlarmManagerService.this.mInFlight.remove(i);
                }
            }
            com.android.server.alarm.AlarmManagerService.this.mLog.w("No in-flight alarm for listener " + listener);
            return null;
        }

        private void updateStatsLocked(com.android.server.alarm.AlarmManagerService.InFlight inflight) {
            long nowELAPSED = com.android.server.alarm.AlarmManagerService.this.mInjector.getElapsedRealtimeMillis();
            com.android.server.alarm.AlarmManagerService.BroadcastStats bs = inflight.mBroadcastStats;
            bs.nesting--;
            if (bs.nesting <= 0) {
                bs.nesting = 0;
                bs.aggregateTime += nowELAPSED - bs.startTime;
            }
            com.android.server.alarm.AlarmManagerService.FilterStats fs = inflight.mFilterStats;
            fs.nesting--;
            if (fs.nesting <= 0) {
                fs.nesting = 0;
                fs.aggregateTime += nowELAPSED - fs.startTime;
            }
            com.android.server.alarm.AlarmManagerService.this.mActivityManagerInternal.noteAlarmFinish(inflight.mPendingIntent, inflight.mWorkSource, inflight.mUid, inflight.mTag);
        }

        private void updateTrackingLocked(com.android.server.alarm.AlarmManagerService.InFlight inflight) {
            if (inflight != null) {
                updateStatsLocked(inflight);
            }
            com.android.server.alarm.AlarmManagerService alarmManagerService = com.android.server.alarm.AlarmManagerService.this;
            alarmManagerService.mBroadcastRefCount--;
            if (com.android.server.alarm.AlarmManagerService.this.mAmsExt.isDynamicLogEnabled()) {
                android.util.Slog.d(com.android.server.alarm.AlarmManagerService.TAG, "mBroadcastRefCount -> " + com.android.server.alarm.AlarmManagerService.this.mBroadcastRefCount);
            }
            if (com.android.server.alarm.AlarmManagerService.this.mBroadcastRefCount == 0) {
                com.android.server.alarm.AlarmManagerService.this.mHandler.obtainMessage(4, 0, 0).sendToTarget();
                com.android.server.alarm.AlarmManagerService.this.mWakeLock.release();
                if (com.android.server.alarm.AlarmManagerService.this.mInFlight.size() > 0) {
                    com.android.server.alarm.AlarmManagerService.this.mLog.w("Finished all dispatches with " + com.android.server.alarm.AlarmManagerService.this.mInFlight.size() + " remaining inflights");
                    for (int i = 0; i < com.android.server.alarm.AlarmManagerService.this.mInFlight.size(); i++) {
                        com.android.server.alarm.AlarmManagerService.this.mLog.w("  Remaining #" + i + ": " + com.android.server.alarm.AlarmManagerService.this.mInFlight.get(i));
                    }
                    com.android.server.alarm.AlarmManagerService.this.mInFlight.clear();
                    return;
                }
                return;
            }
            if (com.android.server.alarm.AlarmManagerService.this.mInFlight.size() > 0) {
                com.android.server.alarm.AlarmManagerService.InFlight inFlight = com.android.server.alarm.AlarmManagerService.this.mInFlight.get(0);
                com.android.server.alarm.AlarmManagerService.this.setWakelockWorkSource(inFlight.mWorkSource, inFlight.mCreatorUid, inFlight.mTag, false);
            } else {
                com.android.server.alarm.AlarmManagerService.this.mLog.w("Alarm wakelock still held but sent queue empty");
                com.android.server.alarm.AlarmManagerService.this.mWakeLock.setWorkSource(null);
            }
        }

        public void alarmComplete(android.os.IBinder who) {
            if (who == null) {
                com.android.server.alarm.AlarmManagerService.this.mLog.w("Invalid alarmComplete: uid=" + android.os.Binder.getCallingUid() + " pid=" + android.os.Binder.getCallingPid());
                return;
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                    com.android.server.alarm.AlarmManagerService.this.mHandler.removeMessages(3, who);
                    com.android.server.alarm.AlarmManagerService.InFlight inflight = removeLocked(who);
                    if (inflight != null) {
                        updateTrackingLocked(inflight);
                        com.android.server.alarm.AlarmManagerService.this.mListenerFinishCount++;
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        @Override // android.app.PendingIntent.OnFinished
        public void onSendFinished(android.app.PendingIntent pi, android.content.Intent intent, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras) {
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.this.mSendFinishCount++;
                updateTrackingLocked(removeLocked(pi, intent));
            }
        }

        public void alarmTimedOut(android.os.IBinder who) {
            synchronized (com.android.server.alarm.AlarmManagerService.this.mLock) {
                com.android.server.alarm.AlarmManagerService.InFlight inflight = removeLocked(who);
                if (inflight != null) {
                    updateTrackingLocked(inflight);
                    com.android.server.alarm.AlarmManagerService.this.mListenerFinishCount++;
                } else {
                    com.android.server.alarm.AlarmManagerService.this.mLog.w("Spurious timeout of listener " + who);
                }
            }
        }

        public void deliverLocked(final com.android.server.alarm.Alarm alarm, long nowELAPSED) throws java.lang.Exception {
            long workSourceToken = android.os.ThreadLocalWorkSource.setUid(com.android.server.alarm.AlarmManagerService.getAlarmAttributionUid(alarm));
            try {
                boolean z = false;
                if (alarm.operation != null) {
                    com.android.server.alarm.AlarmManagerService.this.mSendCount++;
                    try {
                        android.os.Bundle bundle = com.android.server.alarm.AlarmManagerService.this.getAlarmOperationBundle(alarm);
                        alarm.operation.send(com.android.server.alarm.AlarmManagerService.this.getContext(), 0, com.android.server.alarm.AlarmManagerService.this.mBackgroundIntent.putExtra("android.intent.extra.ALARM_COUNT", alarm.count), com.android.server.alarm.AlarmManagerService.this.mDeliveryTracker, com.android.server.alarm.AlarmManagerService.this.mHandler, null, bundle);
                        com.android.server.alarm.AlarmManagerService.this.mAmsExt.trackEventSendAlarmLocked(alarm);
                    } catch (android.app.PendingIntent.CanceledException e) {
                        com.android.server.alarm.AlarmManagerService.this.mAmsExt.canceledPendingIntentDetection(alarm, nowELAPSED);
                        if (alarm.repeatInterval > 0) {
                            com.android.server.alarm.AlarmManagerService.this.removeImpl(alarm.operation, null);
                        }
                        com.android.server.alarm.AlarmManagerService.this.mSendFinishCount++;
                        android.os.ThreadLocalWorkSource.restore(workSourceToken);
                        return;
                    }
                } else {
                    com.android.server.alarm.AlarmManagerService.this.mListenerCount++;
                    alarm.listener.asBinder().unlinkToDeath(com.android.server.alarm.AlarmManagerService.this.mListenerDeathRecipient, 0);
                    if (alarm.listener == com.android.server.alarm.AlarmManagerService.this.mTimeTickTrigger) {
                        long[] jArr = com.android.server.alarm.AlarmManagerService.this.mTickHistory;
                        com.android.server.alarm.AlarmManagerService alarmManagerService = com.android.server.alarm.AlarmManagerService.this;
                        int i = alarmManagerService.mNextTickHistory;
                        alarmManagerService.mNextTickHistory = i + 1;
                        jArr[i] = nowELAPSED;
                        if (com.android.server.alarm.AlarmManagerService.this.mNextTickHistory >= 10) {
                            com.android.server.alarm.AlarmManagerService.this.mNextTickHistory = 0;
                        }
                    }
                    try {
                        alarm.listener.doAlarm(this);
                        com.android.server.alarm.AlarmManagerService.this.mHandler.sendMessageDelayed(com.android.server.alarm.AlarmManagerService.this.mHandler.obtainMessage(3, alarm.listener.asBinder()), com.android.server.alarm.AlarmManagerService.this.mConstants.LISTENER_TIMEOUT);
                    } catch (java.lang.Exception e2) {
                        com.android.server.alarm.AlarmManagerService.this.mListenerFinishCount++;
                        android.os.ThreadLocalWorkSource.restore(workSourceToken);
                        return;
                    }
                }
                android.os.ThreadLocalWorkSource.restore(workSourceToken);
                if (com.android.server.alarm.AlarmManagerService.this.mAmsExt.isDynamicLogEnabled()) {
                    android.util.Slog.d(com.android.server.alarm.AlarmManagerService.TAG, "mBroadcastRefCount -> " + (com.android.server.alarm.AlarmManagerService.this.mBroadcastRefCount + 1));
                }
                if (com.android.server.alarm.AlarmManagerService.this.mBroadcastRefCount == 0) {
                    com.android.server.alarm.AlarmManagerService.this.setWakelockWorkSource(alarm.workSource, alarm.creatorUid, alarm.statsTag, true);
                    com.android.server.alarm.AlarmManagerService.this.mWakeLock.acquire();
                    com.android.server.alarm.AlarmManagerService.this.mHandler.obtainMessage(4, 1, 0).sendToTarget();
                }
                com.android.server.alarm.AlarmManagerService.InFlight inflight = new com.android.server.alarm.AlarmManagerService.InFlight(com.android.server.alarm.AlarmManagerService.this, alarm, nowELAPSED);
                com.android.server.alarm.AlarmManagerService.this.mInFlight.add(inflight);
                com.android.server.alarm.AlarmManagerService.this.mBroadcastRefCount++;
                if (inflight.isBroadcast()) {
                    com.android.server.alarm.AlarmManagerService.this.notifyBroadcastAlarmPendingLocked(alarm.uid);
                }
                final boolean doze = com.android.server.alarm.AlarmManagerService.this.mPendingIdleUntil != null;
                if (com.android.server.alarm.AlarmManagerService.this.mAppStateTracker != null && com.android.server.alarm.AlarmManagerService.this.mAppStateTracker.isForceAllAppsStandbyEnabled()) {
                    z = true;
                }
                final boolean batterySaver = z;
                if (doze || batterySaver) {
                    if (com.android.server.alarm.AlarmManagerService.isAllowedWhileIdleRestricted(alarm)) {
                        com.android.server.alarm.AlarmManagerService.AppWakeupHistory history = (alarm.flags & 4) != 0 ? com.android.server.alarm.AlarmManagerService.this.mAllowWhileIdleHistory : com.android.server.alarm.AlarmManagerService.this.mAllowWhileIdleCompatHistory;
                        history.recordAlarmForPackage(alarm.sourcePackage, android.os.UserHandle.getUserId(alarm.creatorUid), nowELAPSED);
                        com.android.server.alarm.AlarmManagerService.this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$DeliveryTracker$$ExternalSyntheticLambda0
                            @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                            public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm2) {
                                return this.f$0.lambda$deliverLocked$0(alarm, doze, batterySaver, alarm2);
                            }
                        });
                    } else if ((alarm.flags & 64) != 0) {
                        com.android.server.alarm.AlarmManagerService.this.mLastPriorityAlarmDispatch.put(alarm.creatorUid, nowELAPSED);
                        com.android.server.alarm.AlarmManagerService.this.mAlarmStore.updateAlarmDeliveries(new com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator() { // from class: com.android.server.alarm.AlarmManagerService$DeliveryTracker$$ExternalSyntheticLambda1
                            @Override // com.android.server.alarm.AlarmStore.AlarmDeliveryCalculator
                            public final boolean updateAlarmDelivery(com.android.server.alarm.Alarm alarm2) {
                                return this.f$0.lambda$deliverLocked$1(alarm, doze, batterySaver, alarm2);
                            }
                        });
                    }
                }
                if (!com.android.server.alarm.AlarmManagerService.isExemptFromAppStandby(alarm)) {
                    int userId = android.os.UserHandle.getUserId(alarm.creatorUid);
                    if (alarm.mUsingReserveQuota) {
                        com.android.server.alarm.AlarmManagerService.this.mTemporaryQuotaReserve.recordUsage(alarm.sourcePackage, userId, nowELAPSED);
                    } else {
                        com.android.server.alarm.AlarmManagerService.this.mAppWakeupHistory.recordAlarmForPackage(alarm.sourcePackage, userId, nowELAPSED);
                    }
                }
                com.android.server.alarm.AlarmManagerService.BroadcastStats bs = inflight.mBroadcastStats;
                bs.count++;
                if (bs.nesting == 0) {
                    bs.nesting = 1;
                    bs.startTime = nowELAPSED;
                } else {
                    bs.nesting++;
                }
                com.android.server.alarm.AlarmManagerService.FilterStats fs = inflight.mFilterStats;
                fs.count++;
                if (fs.nesting == 0) {
                    fs.nesting = 1;
                    fs.startTime = nowELAPSED;
                } else {
                    fs.nesting++;
                }
                if (alarm.type == 2 || alarm.type == 0) {
                    bs.numWakeup++;
                    fs.numWakeup++;
                    com.android.server.alarm.AlarmManagerService.this.mActivityManagerInternal.noteWakeupAlarm(alarm.operation, alarm.workSource, alarm.uid, alarm.packageName, alarm.statsTag);
                }
                com.android.server.alarm.AlarmManagerService.this.mAmsExt.deliverLockedEnd(alarm, bs, nowELAPSED, false);
            } catch (java.lang.Throwable e3) {
                android.os.ThreadLocalWorkSource.restore(workSourceToken);
                throw e3;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$deliverLocked$0(com.android.server.alarm.Alarm alarm, boolean doze, boolean batterySaver, com.android.server.alarm.Alarm a) {
            if (a.creatorUid != alarm.creatorUid || !com.android.server.alarm.AlarmManagerService.isAllowedWhileIdleRestricted(a)) {
                return false;
            }
            boolean dozeAdjusted = doze && com.android.server.alarm.AlarmManagerService.this.lambda$triggerAlarmsLocked$22(a);
            boolean batterySaverAdjusted = batterySaver && com.android.server.alarm.AlarmManagerService.this.adjustDeliveryTimeBasedOnBatterySaver(a);
            return dozeAdjusted || batterySaverAdjusted;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$deliverLocked$1(com.android.server.alarm.Alarm alarm, boolean doze, boolean batterySaver, com.android.server.alarm.Alarm a) {
            if (a.creatorUid != alarm.creatorUid || (alarm.flags & 64) == 0) {
                return false;
            }
            boolean dozeAdjusted = doze && com.android.server.alarm.AlarmManagerService.this.lambda$triggerAlarmsLocked$22(a);
            boolean batterySaverAdjusted = batterySaver && com.android.server.alarm.AlarmManagerService.this.adjustDeliveryTimeBasedOnBatterySaver(a);
            return dozeAdjusted || batterySaverAdjusted;
        }
    }

    private void incrementAlarmCount(int uid) {
        increment(this.mAlarmsPerUid, uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendScheduleExactAlarmPermissionStateChangedBroadcast(java.lang.String packageName, int userId) {
        android.content.Intent i = new android.content.Intent("android.app.action.SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED");
        i.addFlags(872415232);
        i.setPackage(packageName);
        android.app.BroadcastOptions opts = android.app.BroadcastOptions.makeBasic();
        opts.setTemporaryAppAllowlist(this.mActivityManagerInternal.getBootTimeTempAllowListDuration(), 0, 207, "");
        getContext().sendBroadcastAsUser(i, android.os.UserHandle.of(userId), null, opts.toBundle());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void decrementAlarmCount(int uid, int decrement) {
        int oldCount = 0;
        int uidIndex = this.mAlarmsPerUid.indexOfKey(uid);
        if (uidIndex >= 0) {
            oldCount = this.mAlarmsPerUid.valueAt(uidIndex);
            if (oldCount > decrement) {
                this.mAlarmsPerUid.setValueAt(uidIndex, oldCount - decrement);
            } else {
                this.mAlarmsPerUid.removeAt(uidIndex);
            }
        }
        if (oldCount < decrement) {
            android.util.Slog.wtf(TAG, "Attempt to decrement existing alarm count " + oldCount + " by " + decrement + " for uid " + uid);
        }
    }

    private class ShellCmd extends android.os.ShellCommand {
        private ShellCmd() {
        }

        android.app.IAlarmManager getBinderService() {
            return android.app.IAlarmManager.Stub.asInterface(com.android.server.alarm.AlarmManagerService.this.mService);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:8:0x0014  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int onCommand(java.lang.String r7) {
            /*
                r6 = this;
                if (r7 != 0) goto L7
                int r0 = r6.handleDefaultCommands(r7)
                return r0
            L7:
                java.io.PrintWriter r0 = r6.getOutPrintWriter()
                r1 = -1
                int r2 = r7.hashCode()     // Catch: java.lang.Exception -> L6c
                r3 = 0
                switch(r2) {
                    case -2120488796: goto L2b;
                    case 1369384280: goto L20;
                    case 2023087364: goto L15;
                    default: goto L14;
                }     // Catch: java.lang.Exception -> L6c
            L14:
                goto L36
            L15:
                java.lang.String r2 = "set-timezone"
                boolean r2 = r7.equals(r2)     // Catch: java.lang.Exception -> L6c
                if (r2 == 0) goto L14
                r2 = 1
                goto L37
            L20:
                java.lang.String r2 = "set-time"
                boolean r2 = r7.equals(r2)     // Catch: java.lang.Exception -> L6c
                if (r2 == 0) goto L14
                r2 = r3
                goto L37
            L2b:
                java.lang.String r2 = "get-config-version"
                boolean r2 = r7.equals(r2)     // Catch: java.lang.Exception -> L6c
                if (r2 == 0) goto L14
                r2 = 2
                goto L37
            L36:
                r2 = r1
            L37:
                switch(r2) {
                    case 0: goto L57;
                    case 1: goto L4b;
                    case 2: goto L3f;
                    default: goto L3a;
                }     // Catch: java.lang.Exception -> L6c
            L3a:
                int r1 = r6.handleDefaultCommands(r7)     // Catch: java.lang.Exception -> L6c
                goto L6b
            L3f:
                android.app.IAlarmManager r2 = r6.getBinderService()     // Catch: java.lang.Exception -> L6c
                int r2 = r2.getConfigVersion()     // Catch: java.lang.Exception -> L6c
                r0.println(r2)     // Catch: java.lang.Exception -> L6c
                return r3
            L4b:
                java.lang.String r2 = r6.getNextArgRequired()     // Catch: java.lang.Exception -> L6c
                android.app.IAlarmManager r4 = r6.getBinderService()     // Catch: java.lang.Exception -> L6c
                r4.setTimeZone(r2)     // Catch: java.lang.Exception -> L6c
                return r3
            L57:
                java.lang.String r2 = r6.getNextArgRequired()     // Catch: java.lang.Exception -> L6c
                long r4 = java.lang.Long.parseLong(r2)     // Catch: java.lang.Exception -> L6c
                android.app.IAlarmManager r2 = r6.getBinderService()     // Catch: java.lang.Exception -> L6c
                boolean r2 = r2.setTime(r4)     // Catch: java.lang.Exception -> L6c
                if (r2 == 0) goto L6a
                r1 = r3
            L6a:
                return r1
            L6b:
                return r1
            L6c:
                r2 = move-exception
                r0.println(r2)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.alarm.AlarmManagerService.ShellCmd.onCommand(java.lang.String):int");
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.println("Alarm manager service (alarm) commands:");
            pw.println("  help");
            pw.println("    Print this help text.");
            pw.println("  set-time TIME");
            pw.println("    Set the system clock time to TIME where TIME is milliseconds");
            pw.println("    since the Epoch.");
            pw.println("  set-timezone TZ");
            pw.println("    Set the system timezone to TZ where TZ is an Olson id.");
            pw.println("  get-config-version");
            pw.println("    Returns an integer denoting the version of device_config keys the service is sync'ed to. As long as this returns the same version, the values of the config are guaranteed to remain the same.");
        }
    }

    public com.android.server.alarm.IAlarmManagerServiceWrapper getWrapper() {
        return this.mAmsWrapper;
    }

    private class AlarmManagerServiceWrapper implements com.android.server.alarm.IAlarmManagerServiceWrapper {
        private AlarmManagerServiceWrapper() {
        }

        @Override // com.android.server.alarm.IAlarmManagerServiceWrapper
        public void setImplLocked(com.android.server.alarm.Alarm a) {
            com.android.server.alarm.AlarmManagerService.this.setImplLocked(a);
        }

        @Override // com.android.server.alarm.IAlarmManagerServiceWrapper
        public com.android.server.alarm.AlarmManagerService.BroadcastStats getStatsLocked(android.app.PendingIntent pi) {
            return com.android.server.alarm.AlarmManagerService.this.getStatsLocked(pi);
        }

        @Override // com.android.server.alarm.IAlarmManagerServiceWrapper
        public boolean adjustDeliveryTimeBasedOnDeviceIdle(com.android.server.alarm.Alarm alarm) {
            return com.android.server.alarm.AlarmManagerService.this.lambda$triggerAlarmsLocked$22(alarm);
        }

        @Override // com.android.server.alarm.IAlarmManagerServiceWrapper
        public void updateNextAlarmClockLocked() {
            com.android.server.alarm.AlarmManagerService.this.updateNextAlarmClockLocked();
        }

        @Override // com.android.server.alarm.IAlarmManagerServiceWrapper
        public int set(long nativeData, int type, long seconds, long nanoseconds) {
            return com.android.server.alarm.AlarmManagerService.set(nativeData, type, seconds, nanoseconds);
        }

        @Override // com.android.server.alarm.IAlarmManagerServiceWrapper
        public com.android.server.alarm.IAlarmManagerServiceExt getExt() {
            return com.android.server.alarm.AlarmManagerService.this.mAmsExt;
        }

        @Override // com.android.server.alarm.IAlarmManagerServiceWrapper
        public void decrementAlarmCount(int uid, int decrement) {
            com.android.server.alarm.AlarmManagerService.this.decrementAlarmCount(uid, decrement);
        }
    }
}
