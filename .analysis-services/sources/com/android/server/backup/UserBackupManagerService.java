package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class UserBackupManagerService {
    public static final java.lang.String BACKUP_FILE_HEADER_MAGIC = "ANDROID BACKUP\n";
    public static final int BACKUP_FILE_VERSION = 5;
    private static final java.lang.String BACKUP_FINISHED_ACTION = "android.intent.action.BACKUP_FINISHED";
    private static final java.lang.String BACKUP_FINISHED_PACKAGE_EXTRA = "packageName";
    public static final java.lang.String BACKUP_MANIFEST_FILENAME = "_manifest";
    public static final int BACKUP_MANIFEST_VERSION = 1;
    public static final java.lang.String BACKUP_METADATA_FILENAME = "_meta";
    public static final int BACKUP_METADATA_VERSION = 1;
    public static final int BACKUP_WIDGET_METADATA_TOKEN = 33549569;
    private static final long BIND_TIMEOUT_INTERVAL = 10000;
    private static final int BUSY_BACKOFF_FUZZ = 7200000;
    private static final long BUSY_BACKOFF_MIN_MILLIS = 3600000;
    private static final long CLEAR_DATA_TIMEOUT_INTERVAL = 30000;
    private static final int CURRENT_ANCESTRAL_RECORD_VERSION = 1;
    private static final long INITIALIZATION_DELAY_MILLIS = 3000;
    private static final java.lang.String INIT_SENTINEL_FILE_NAME = "_need_init_";
    public static final java.lang.String KEY_WIDGET_STATE = "￭￭widget";
    public static final java.lang.String PACKAGE_MANAGER_SENTINEL = "@pm@";
    public static final java.lang.String RUN_INITIALIZE_ACTION = "android.app.backup.intent.INIT";
    private static final int SCHEDULE_FILE_VERSION = 1;
    private static final java.lang.String SERIAL_ID_FILE = "serial_id";
    public static final java.lang.String SETTINGS_PACKAGE = "com.android.providers.settings";
    public static final java.lang.String SHARED_BACKUP_AGENT_PACKAGE = "com.android.sharedstoragebackup";
    private static final java.lang.String SKIP_USER_FACING_PACKAGES = "backup_skip_user_facing_packages";
    private static final long TIMEOUT_FULL_CONFIRMATION = 60000;
    private static final long TRANSPORT_RETRY_INTERVAL = 3600000;
    public static final java.lang.String WALLPAPER_PACKAGE = "com.android.wallpaperbackup";
    private static com.android.server.backup.IUserBackupManagerServiceExt.IStaticExt sStaticUserBackupManagerServiceExt = (com.android.server.backup.IUserBackupManagerServiceExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.backup.IUserBackupManagerServiceExt.IStaticExt.class).create();
    private com.android.server.backup.restore.ActiveRestoreSession mActiveRestoreSession;
    private final android.app.IActivityManager mActivityManager;
    private final android.app.ActivityManagerInternal mActivityManagerInternal;
    private final android.util.SparseArray<com.android.server.backup.params.AdbParams> mAdbBackupRestoreConfirmations;
    private final java.lang.Object mAgentConnectLock;
    private final com.android.server.backup.BackupAgentTimeoutParameters mAgentTimeoutParameters;
    private final android.app.AlarmManager mAlarmManager;
    private volatile long mAncestralBackupDestination;
    private java.util.Set<java.lang.String> mAncestralPackages;
    private java.io.File mAncestralSerialNumberFile;
    private long mAncestralToken;
    private boolean mAutoRestore;
    private final com.android.server.backup.internal.BackupHandler mBackupHandler;
    private final android.app.backup.IBackupManager mBackupManagerBinder;
    private final android.util.SparseArray<java.util.HashSet<java.lang.String>> mBackupParticipants;
    private final com.android.server.backup.BackupPasswordManager mBackupPasswordManager;
    private final com.android.server.backup.UserBackupPreferences mBackupPreferences;
    private volatile boolean mBackupRunning;
    private final java.io.File mBaseStateDir;
    private final java.lang.Object mClearDataLock;
    private volatile boolean mClearingData;
    private android.app.IBackupAgent mConnectedAgent;
    private volatile boolean mConnecting;
    private final com.android.server.backup.BackupManagerConstants mConstants;
    private final android.content.Context mContext;
    private long mCurrentToken;
    private final java.io.File mDataDir;
    private boolean mEnabled;
    private java.util.ArrayList<com.android.server.backup.fullbackup.FullBackupEntry> mFullBackupQueue;
    private final java.io.File mFullBackupScheduleFile;
    private java.lang.Runnable mFullBackupScheduleWriter;
    private boolean mIsRestoreInProgress;
    private com.android.server.backup.DataChangedJournal mJournal;
    private final java.io.File mJournalDir;
    private volatile long mLastBackupPass;
    private final java.util.concurrent.atomic.AtomicInteger mNextToken;
    private final com.android.server.backup.internal.LifecycleOperationStorage mOperationStorage;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.content.pm.IPackageManager mPackageManagerBinder;
    private android.content.BroadcastReceiver mPackageTrackingReceiver;
    private final java.util.HashMap<java.lang.String, com.android.server.backup.keyvalue.BackupRequest> mPendingBackups;
    private final android.util.ArraySet<java.lang.String> mPendingInits;
    private final java.util.Queue<com.android.server.backup.restore.PerformUnifiedRestoreTask> mPendingRestores;
    private android.os.PowerManager mPowerManager;
    private com.android.server.backup.ProcessedPackagesJournal mProcessedPackagesJournal;
    private final java.lang.Object mQueueLock;
    private final long mRegisterTransportsRequestedTime;
    private final java.security.SecureRandom mRng;
    private final android.app.PendingIntent mRunInitIntent;
    private final android.content.BroadcastReceiver mRunInitReceiver;
    private com.android.server.backup.fullbackup.PerformFullTransportBackupTask mRunningFullBackupTask;
    private final com.android.server.backup.utils.BackupEligibilityRules mScheduledBackupEligibility;
    private boolean mSetupComplete;
    private final android.database.ContentObserver mSetupObserver;
    private java.io.File mTokenFile;
    private final java.util.Random mTokenGenerator;
    private final com.android.server.backup.TransportManager mTransportManager;
    private com.android.server.backup.IUserBackupManagerServiceExt mUserBackupManagerServiceExt;
    private com.android.server.backup.IUserBackupManagerServiceWrapper mUserBackupManagerWrapper;
    private final int mUserId;
    private final com.android.server.backup.UserBackupManagerService.BackupWakeLock mWakelock;

    public static class BackupWakeLock {
        private boolean mHasQuit = false;
        private final android.os.PowerManager.WakeLock mPowerManagerWakeLock;
        private int mUserId;

        public BackupWakeLock(android.os.PowerManager.WakeLock powerManagerWakeLock, int userId) {
            this.mPowerManagerWakeLock = powerManagerWakeLock;
            this.mUserId = userId;
        }

        public synchronized void acquire() {
            if (this.mHasQuit) {
                android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, com.android.server.backup.UserBackupManagerService.addUserIdToLogMessage(this.mUserId, "Ignore wakelock acquire after quit: " + this.mPowerManagerWakeLock.getTag()));
            } else {
                this.mPowerManagerWakeLock.acquire();
                android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, com.android.server.backup.UserBackupManagerService.addUserIdToLogMessage(this.mUserId, "Acquired wakelock:" + this.mPowerManagerWakeLock.getTag()));
            }
        }

        public synchronized void release() {
            if (this.mHasQuit) {
                android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, com.android.server.backup.UserBackupManagerService.addUserIdToLogMessage(this.mUserId, "Ignore wakelock release after quit: " + this.mPowerManagerWakeLock.getTag()));
            } else {
                this.mPowerManagerWakeLock.release();
                android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, com.android.server.backup.UserBackupManagerService.addUserIdToLogMessage(this.mUserId, "Released wakelock:" + this.mPowerManagerWakeLock.getTag()));
            }
        }

        public synchronized boolean isHeld() {
            return this.mPowerManagerWakeLock.isHeld();
        }

        public synchronized void quit() {
            while (this.mPowerManagerWakeLock.isHeld()) {
                android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, com.android.server.backup.UserBackupManagerService.addUserIdToLogMessage(this.mUserId, "Releasing wakelock: " + this.mPowerManagerWakeLock.getTag()));
                this.mPowerManagerWakeLock.release();
            }
            this.mHasQuit = true;
        }
    }

    static com.android.server.backup.UserBackupManagerService createAndInitializeService(int userId, android.content.Context context, com.android.server.backup.BackupManagerService backupManagerService, java.util.Set<android.content.ComponentName> transportWhitelist) {
        java.lang.String currentTransport = android.provider.Settings.Secure.getStringForUser(context.getContentResolver(), "backup_transport", userId);
        if (android.text.TextUtils.isEmpty(currentTransport)) {
            currentTransport = null;
        }
        android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(userId, "Starting with transport " + currentTransport));
        com.android.server.backup.TransportManager transportManager = new com.android.server.backup.TransportManager(userId, context, transportWhitelist, currentTransport);
        java.io.File baseStateDir = com.android.server.backup.UserBackupManagerFiles.getBaseStateDir(userId);
        java.io.File dataDir = com.android.server.backup.UserBackupManagerFiles.getDataDir(userId);
        android.os.HandlerThread userBackupThread = new android.os.HandlerThread("backup-" + userId, 10);
        userBackupThread.start();
        android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(userId, "Started thread " + userBackupThread.getName()));
        return createAndInitializeService(userId, context, backupManagerService, userBackupThread, baseStateDir, dataDir, transportManager);
    }

    public static com.android.server.backup.UserBackupManagerService createAndInitializeService(int userId, android.content.Context context, com.android.server.backup.BackupManagerService backupManagerService, android.os.HandlerThread userBackupThread, java.io.File baseStateDir, java.io.File dataDir, com.android.server.backup.TransportManager transportManager) {
        com.android.server.backup.utils.BackupManagerMonitorDumpsysUtils backupManagerMonitorDumpsysUtils = new com.android.server.backup.utils.BackupManagerMonitorDumpsysUtils();
        if (backupManagerMonitorDumpsysUtils.deleteExpiredBMMEvents()) {
            android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, "BMM Events recorded for dumpsys have expired");
        }
        return new com.android.server.backup.UserBackupManagerService(userId, context, backupManagerService, userBackupThread, baseStateDir, dataDir, transportManager);
    }

    public static boolean getSetupCompleteSettingForUser(android.content.Context context, int userId) {
        return android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "user_setup_complete", 0, userId) != 0;
    }

    UserBackupManagerService(android.content.Context context, android.content.pm.PackageManager packageManager, com.android.server.backup.internal.LifecycleOperationStorage operationStorage, com.android.server.backup.TransportManager transportManager, com.android.server.backup.internal.BackupHandler backupHandler, com.android.server.backup.BackupManagerConstants backupManagerConstants) {
        this.mPendingInits = new android.util.ArraySet<>();
        this.mBackupParticipants = new android.util.SparseArray<>();
        this.mPendingBackups = new java.util.HashMap<>();
        this.mQueueLock = new java.lang.Object();
        this.mAgentConnectLock = new java.lang.Object();
        this.mClearDataLock = new java.lang.Object();
        this.mAdbBackupRestoreConfirmations = new android.util.SparseArray<>();
        this.mRng = new java.security.SecureRandom();
        this.mPendingRestores = new java.util.ArrayDeque();
        this.mTokenGenerator = new java.util.Random();
        this.mNextToken = new java.util.concurrent.atomic.AtomicInteger();
        this.mAncestralPackages = null;
        this.mAncestralToken = 0L;
        this.mCurrentToken = 0L;
        this.mFullBackupScheduleWriter = new java.lang.Runnable() { // from class: com.android.server.backup.UserBackupManagerService.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (com.android.server.backup.UserBackupManagerService.this.mQueueLock) {
                    try {
                        java.io.ByteArrayOutputStream bufStream = new java.io.ByteArrayOutputStream(4096);
                        java.io.DataOutputStream bufOut = new java.io.DataOutputStream(bufStream);
                        bufOut.writeInt(1);
                        int numPackages = com.android.server.backup.UserBackupManagerService.this.mFullBackupQueue.size();
                        bufOut.writeInt(numPackages);
                        for (int i = 0; i < numPackages; i++) {
                            com.android.server.backup.fullbackup.FullBackupEntry entry = (com.android.server.backup.fullbackup.FullBackupEntry) com.android.server.backup.UserBackupManagerService.this.mFullBackupQueue.get(i);
                            bufOut.writeUTF(entry.packageName);
                            bufOut.writeLong(entry.lastBackup);
                        }
                        bufOut.flush();
                        android.util.AtomicFile af = new android.util.AtomicFile(com.android.server.backup.UserBackupManagerService.this.mFullBackupScheduleFile);
                        java.io.FileOutputStream out = af.startWrite();
                        out.write(bufStream.toByteArray());
                        af.finishWrite(out);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, com.android.server.backup.UserBackupManagerService.addUserIdToLogMessage(com.android.server.backup.UserBackupManagerService.this.mUserId, "Unable to write backup schedule!"), e);
                    }
                }
            }
        };
        this.mPackageTrackingReceiver = new com.android.server.backup.UserBackupManagerService.AnonymousClass2();
        this.mUserBackupManagerWrapper = new com.android.server.backup.UserBackupManagerService.UserBackupManagerServiceWrapper();
        this.mUserBackupManagerServiceExt = (com.android.server.backup.IUserBackupManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.backup.IUserBackupManagerServiceExt.class).base(this).create();
        this.mContext = context;
        this.mUserId = 0;
        this.mRegisterTransportsRequestedTime = 0L;
        this.mPackageManager = packageManager;
        this.mOperationStorage = operationStorage;
        this.mTransportManager = transportManager;
        this.mFullBackupQueue = new java.util.ArrayList<>();
        this.mBackupHandler = backupHandler;
        this.mConstants = backupManagerConstants;
        this.mBaseStateDir = null;
        this.mDataDir = null;
        this.mJournalDir = null;
        this.mFullBackupScheduleFile = null;
        this.mSetupObserver = null;
        this.mRunInitReceiver = null;
        this.mRunInitIntent = null;
        this.mAgentTimeoutParameters = null;
        this.mActivityManagerInternal = null;
        this.mAlarmManager = null;
        this.mWakelock = null;
        this.mBackupPreferences = null;
        this.mBackupPasswordManager = null;
        this.mPackageManagerBinder = null;
        this.mActivityManager = null;
        this.mBackupManagerBinder = null;
        this.mScheduledBackupEligibility = null;
    }

    private UserBackupManagerService(int userId, android.content.Context context, com.android.server.backup.BackupManagerService parent, android.os.HandlerThread userBackupThread, java.io.File baseStateDir, java.io.File dataDir, com.android.server.backup.TransportManager transportManager) throws java.lang.Throwable {
        this.mPendingInits = new android.util.ArraySet<>();
        this.mBackupParticipants = new android.util.SparseArray<>();
        this.mPendingBackups = new java.util.HashMap<>();
        this.mQueueLock = new java.lang.Object();
        this.mAgentConnectLock = new java.lang.Object();
        this.mClearDataLock = new java.lang.Object();
        this.mAdbBackupRestoreConfirmations = new android.util.SparseArray<>();
        this.mRng = new java.security.SecureRandom();
        this.mPendingRestores = new java.util.ArrayDeque();
        this.mTokenGenerator = new java.util.Random();
        this.mNextToken = new java.util.concurrent.atomic.AtomicInteger();
        this.mAncestralPackages = null;
        this.mAncestralToken = 0L;
        this.mCurrentToken = 0L;
        this.mFullBackupScheduleWriter = new java.lang.Runnable() { // from class: com.android.server.backup.UserBackupManagerService.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (com.android.server.backup.UserBackupManagerService.this.mQueueLock) {
                    try {
                        java.io.ByteArrayOutputStream bufStream = new java.io.ByteArrayOutputStream(4096);
                        java.io.DataOutputStream bufOut = new java.io.DataOutputStream(bufStream);
                        bufOut.writeInt(1);
                        int numPackages = com.android.server.backup.UserBackupManagerService.this.mFullBackupQueue.size();
                        bufOut.writeInt(numPackages);
                        for (int i = 0; i < numPackages; i++) {
                            com.android.server.backup.fullbackup.FullBackupEntry entry = (com.android.server.backup.fullbackup.FullBackupEntry) com.android.server.backup.UserBackupManagerService.this.mFullBackupQueue.get(i);
                            bufOut.writeUTF(entry.packageName);
                            bufOut.writeLong(entry.lastBackup);
                        }
                        bufOut.flush();
                        android.util.AtomicFile af = new android.util.AtomicFile(com.android.server.backup.UserBackupManagerService.this.mFullBackupScheduleFile);
                        java.io.FileOutputStream out = af.startWrite();
                        out.write(bufStream.toByteArray());
                        af.finishWrite(out);
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, com.android.server.backup.UserBackupManagerService.addUserIdToLogMessage(com.android.server.backup.UserBackupManagerService.this.mUserId, "Unable to write backup schedule!"), e);
                    }
                }
            }
        };
        this.mPackageTrackingReceiver = new com.android.server.backup.UserBackupManagerService.AnonymousClass2();
        this.mUserBackupManagerWrapper = new com.android.server.backup.UserBackupManagerService.UserBackupManagerServiceWrapper();
        this.mUserBackupManagerServiceExt = (com.android.server.backup.IUserBackupManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.backup.IUserBackupManagerServiceExt.class).base(this).create();
        this.mUserId = userId;
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context, "context cannot be null");
        this.mPackageManager = context.getPackageManager();
        this.mPackageManagerBinder = android.app.AppGlobals.getPackageManager();
        this.mActivityManager = android.app.ActivityManager.getService();
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mScheduledBackupEligibility = getEligibilityRules(this.mPackageManager, userId, this.mContext, 0);
        this.mAlarmManager = (android.app.AlarmManager) context.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        this.mPowerManager = (android.os.PowerManager) context.getSystemService("power");
        java.util.Objects.requireNonNull(parent, "parent cannot be null");
        this.mBackupManagerBinder = com.android.server.backup.BackupManagerService.asInterface(parent.asBinder());
        this.mAgentTimeoutParameters = new com.android.server.backup.BackupAgentTimeoutParameters(android.os.Handler.getMain(), this.mContext.getContentResolver());
        this.mAgentTimeoutParameters.start();
        this.mOperationStorage = new com.android.server.backup.internal.LifecycleOperationStorage(this.mUserId);
        java.util.Objects.requireNonNull(userBackupThread, "userBackupThread cannot be null");
        this.mBackupHandler = new com.android.server.backup.internal.BackupHandler(this, this.mOperationStorage, userBackupThread);
        android.content.ContentResolver resolver = context.getContentResolver();
        this.mSetupComplete = getSetupCompleteSettingForUser(context, userId);
        this.mAutoRestore = android.provider.Settings.Secure.getIntForUser(resolver, "backup_auto_restore", 1, userId) != 0;
        this.mSetupObserver = new com.android.server.backup.internal.SetupObserver(this, this.mBackupHandler);
        resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("user_setup_complete"), false, this.mSetupObserver, this.mUserId);
        this.mBaseStateDir = (java.io.File) java.util.Objects.requireNonNull(baseStateDir, "baseStateDir cannot be null");
        if (userId == 0) {
            this.mBaseStateDir.mkdirs();
            if (!android.os.SELinux.restorecon(this.mBaseStateDir)) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(userId, "SELinux restorecon failed on " + this.mBaseStateDir));
            }
        }
        this.mDataDir = (java.io.File) java.util.Objects.requireNonNull(dataDir, "dataDir cannot be null");
        this.mBackupPasswordManager = new com.android.server.backup.BackupPasswordManager(this.mContext, this.mBaseStateDir, this.mRng);
        this.mRunInitReceiver = new com.android.server.backup.internal.RunInitializeReceiver(this);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction(RUN_INITIALIZE_ACTION);
        context.registerReceiverAsUser(this.mRunInitReceiver, android.os.UserHandle.of(userId), filter, "android.permission.BACKUP", null);
        android.content.Intent initIntent = new android.content.Intent(RUN_INITIALIZE_ACTION);
        initIntent.addFlags(1073741824);
        this.mRunInitIntent = android.app.PendingIntent.getBroadcastAsUser(context, 0, initIntent, 67108864, android.os.UserHandle.of(userId));
        this.mJournalDir = new java.io.File(this.mBaseStateDir, "pending");
        this.mJournalDir.mkdirs();
        this.mJournal = null;
        this.mConstants = new com.android.server.backup.BackupManagerConstants(this.mBackupHandler, this.mContext.getContentResolver());
        this.mConstants.start();
        synchronized (this.mBackupParticipants) {
            try {
                addPackageParticipantsLocked(null);
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        this.mTransportManager = (com.android.server.backup.TransportManager) java.util.Objects.requireNonNull(transportManager, "transportManager cannot be null");
        this.mTransportManager.setOnTransportRegisteredListener(new com.android.server.backup.transport.OnTransportRegisteredListener() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda1
            @Override // com.android.server.backup.transport.OnTransportRegisteredListener
            public final void onTransportRegistered(java.lang.String str, java.lang.String str2) {
                this.f$0.onTransportRegistered(str, str2);
            }
        });
        this.mRegisterTransportsRequestedTime = android.os.SystemClock.elapsedRealtime();
        com.android.server.backup.internal.BackupHandler backupHandler = this.mBackupHandler;
        final com.android.server.backup.TransportManager transportManager2 = this.mTransportManager;
        java.util.Objects.requireNonNull(transportManager2);
        backupHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                transportManager2.registerTransports();
            }
        }, 3000L);
        this.mBackupHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.parseLeftoverJournals();
            }
        }, 3000L);
        final com.android.server.backup.utils.BackupManagerMonitorDumpsysUtils backupManagerMonitorDumpsysUtils = new com.android.server.backup.utils.BackupManagerMonitorDumpsysUtils();
        com.android.server.backup.internal.BackupHandler backupHandler2 = this.mBackupHandler;
        java.util.Objects.requireNonNull(backupManagerMonitorDumpsysUtils);
        backupHandler2.postDelayed(new java.lang.Runnable() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                backupManagerMonitorDumpsysUtils.deleteExpiredBMMEvents();
            }
        }, 3000L);
        this.mBackupPreferences = new com.android.server.backup.UserBackupPreferences(this.mContext, this.mBaseStateDir);
        this.mWakelock = new com.android.server.backup.UserBackupManagerService.BackupWakeLock(this.mPowerManager.newWakeLock(1, "*backup*-" + userId + "-" + userBackupThread.getThreadId()), userId);
        this.mFullBackupScheduleFile = new java.io.File(this.mBaseStateDir, "fb-schedule");
        initPackageTracking();
    }

    void initializeBackupEnableState() {
        boolean isEnabled = readEnabledState();
        setBackupEnabled(isEnabled, false);
    }

    protected void tearDownService() {
        this.mAgentTimeoutParameters.stop();
        this.mConstants.stop();
        this.mContext.getContentResolver().unregisterContentObserver(this.mSetupObserver);
        this.mContext.unregisterReceiver(this.mRunInitReceiver);
        this.mContext.unregisterReceiver(this.mPackageTrackingReceiver);
        this.mBackupHandler.stop();
    }

    public int getUserId() {
        return this.mUserId;
    }

    public com.android.server.backup.BackupManagerConstants getConstants() {
        return this.mConstants;
    }

    public com.android.server.backup.BackupAgentTimeoutParameters getAgentTimeoutParameters() {
        return this.mAgentTimeoutParameters;
    }

    public android.content.Context getContext() {
        return this.mContext;
    }

    public android.content.pm.PackageManager getPackageManager() {
        return this.mPackageManager;
    }

    public android.content.pm.IPackageManager getPackageManagerBinder() {
        return this.mPackageManagerBinder;
    }

    public android.app.IActivityManager getActivityManager() {
        return this.mActivityManager;
    }

    public android.app.AlarmManager getAlarmManager() {
        return this.mAlarmManager;
    }

    void setPowerManager(android.os.PowerManager powerManager) {
        this.mPowerManager = powerManager;
    }

    public com.android.server.backup.TransportManager getTransportManager() {
        return this.mTransportManager;
    }

    public com.android.server.backup.OperationStorage getOperationStorage() {
        return this.mOperationStorage;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public boolean isSetupComplete() {
        return this.mSetupComplete;
    }

    public void setSetupComplete(boolean setupComplete) {
        this.mSetupComplete = setupComplete;
    }

    public com.android.server.backup.UserBackupManagerService.BackupWakeLock getWakelock() {
        return this.mWakelock;
    }

    public void setWorkSource(android.os.WorkSource workSource) {
        this.mWakelock.mPowerManagerWakeLock.setWorkSource(workSource);
    }

    public android.os.Handler getBackupHandler() {
        return this.mBackupHandler;
    }

    public android.app.PendingIntent getRunInitIntent() {
        return this.mRunInitIntent;
    }

    public java.util.HashMap<java.lang.String, com.android.server.backup.keyvalue.BackupRequest> getPendingBackups() {
        return this.mPendingBackups;
    }

    public java.lang.Object getQueueLock() {
        return this.mQueueLock;
    }

    public boolean isBackupRunning() {
        return this.mBackupRunning;
    }

    public void setBackupRunning(boolean backupRunning) {
        this.mBackupRunning = backupRunning;
    }

    public void setLastBackupPass(long lastBackupPass) {
        this.mLastBackupPass = lastBackupPass;
    }

    public java.lang.Object getClearDataLock() {
        return this.mClearDataLock;
    }

    public void setClearingData(boolean clearingData) {
        this.mClearingData = clearingData;
    }

    public boolean isRestoreInProgress() {
        return this.mIsRestoreInProgress;
    }

    public void setRestoreInProgress(boolean restoreInProgress) {
        this.mIsRestoreInProgress = restoreInProgress;
    }

    public java.util.Queue<com.android.server.backup.restore.PerformUnifiedRestoreTask> getPendingRestores() {
        return this.mPendingRestores;
    }

    public com.android.server.backup.restore.ActiveRestoreSession getActiveRestoreSession() {
        return this.mActiveRestoreSession;
    }

    public android.util.SparseArray<com.android.server.backup.params.AdbParams> getAdbBackupRestoreConfirmations() {
        return this.mAdbBackupRestoreConfirmations;
    }

    public java.io.File getBaseStateDir() {
        return this.mBaseStateDir;
    }

    public java.io.File getDataDir() {
        return this.mDataDir;
    }

    android.content.BroadcastReceiver getPackageTrackingReceiver() {
        return this.mPackageTrackingReceiver;
    }

    public com.android.server.backup.DataChangedJournal getJournal() {
        return this.mJournal;
    }

    public void setJournal(com.android.server.backup.DataChangedJournal journal) {
        this.mJournal = journal;
    }

    public java.security.SecureRandom getRng() {
        return this.mRng;
    }

    public void setAncestralPackages(java.util.Set<java.lang.String> ancestralPackages) {
        this.mAncestralPackages = ancestralPackages;
    }

    public void setAncestralToken(long ancestralToken) {
        this.mAncestralToken = ancestralToken;
    }

    public void setAncestralBackupDestination(int backupDestination) {
        this.mAncestralBackupDestination = backupDestination;
    }

    public long getCurrentToken() {
        return this.mCurrentToken;
    }

    public void setCurrentToken(long currentToken) {
        this.mCurrentToken = currentToken;
    }

    public android.util.ArraySet<java.lang.String> getPendingInits() {
        return this.mPendingInits;
    }

    public void clearPendingInits() {
        this.mPendingInits.clear();
    }

    public void setRunningFullBackupTask(com.android.server.backup.fullbackup.PerformFullTransportBackupTask runningFullBackupTask) {
        this.mRunningFullBackupTask = runningFullBackupTask;
    }

    public int generateRandomIntegerToken() {
        int token = this.mTokenGenerator.nextInt();
        if (token < 0) {
            token = -token;
        }
        return (token & (-256)) | (this.mNextToken.incrementAndGet() & 255);
    }

    public android.app.backup.BackupAgent makeMetadataAgent() {
        return makeMetadataAgentWithEligibilityRules(this.mScheduledBackupEligibility);
    }

    public android.app.backup.BackupAgent makeMetadataAgentWithEligibilityRules(com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules) {
        com.android.server.backup.PackageManagerBackupAgent pmAgent = new com.android.server.backup.PackageManagerBackupAgent(this.mPackageManager, this.mUserId, backupEligibilityRules);
        pmAgent.attach(this.mContext);
        pmAgent.onCreate(android.os.UserHandle.of(this.mUserId));
        return pmAgent;
    }

    public com.android.server.backup.PackageManagerBackupAgent makeMetadataAgent(java.util.List<android.content.pm.PackageInfo> packages) {
        com.android.server.backup.PackageManagerBackupAgent pmAgent = new com.android.server.backup.PackageManagerBackupAgent(this.mPackageManager, packages, this.mUserId);
        pmAgent.attach(this.mContext);
        pmAgent.onCreate(android.os.UserHandle.of(this.mUserId));
        return pmAgent;
    }

    private void initPackageTracking() {
        this.mTokenFile = new java.io.File(this.mBaseStateDir, "ancestral");
        try {
            java.io.DataInputStream tokenStream = new java.io.DataInputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(this.mTokenFile)));
            try {
                int version = tokenStream.readInt();
                if (version == 1) {
                    this.mAncestralToken = tokenStream.readLong();
                    this.mCurrentToken = tokenStream.readLong();
                    int numPackages = tokenStream.readInt();
                    if (numPackages >= 0) {
                        this.mAncestralPackages = new java.util.HashSet();
                        for (int i = 0; i < numPackages; i++) {
                            java.lang.String pkgName = tokenStream.readUTF();
                            this.mAncestralPackages.add(pkgName);
                        }
                    }
                }
                tokenStream.close();
            } finally {
            }
        } catch (java.io.FileNotFoundException e) {
            android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "No ancestral data"));
        } catch (java.io.IOException e2) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unable to read token file"), e2);
        }
        this.mProcessedPackagesJournal = new com.android.server.backup.ProcessedPackagesJournal(this.mBaseStateDir);
        this.mProcessedPackagesJournal.init();
        synchronized (this.mQueueLock) {
            this.mFullBackupQueue = readFullBackupSchedule();
        }
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_CHANGED");
        filter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=IGNORE_WM_COMP");
        filter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(this.mPackageTrackingReceiver, android.os.UserHandle.of(this.mUserId), filter, null, null);
        android.content.IntentFilter sdFilter = new android.content.IntentFilter();
        sdFilter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
        sdFilter.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
        this.mContext.registerReceiverAsUser(this.mPackageTrackingReceiver, android.os.UserHandle.of(this.mUserId), sdFilter, null, null);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(10:14|(5:145|15|16|137|17)|(3:143|19|(7:21|22|125|23|24|164|44)(1:27))(1:30)|128|31|32|130|33|163|44) */
    /* JADX WARN: Not initialized variable reg: 16, insn: 0x017b: MOVE (r4 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r16 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('changed' boolean)]), block:B:71:0x017a */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.ArrayList<com.android.server.backup.fullbackup.FullBackupEntry> readFullBackupSchedule() throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 527
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.UserBackupManagerService.readFullBackupSchedule():java.util.ArrayList");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeFullBackupScheduleAsync() {
        this.mBackupHandler.removeCallbacks(this.mFullBackupScheduleWriter);
        this.mBackupHandler.post(this.mFullBackupScheduleWriter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseLeftoverJournals() {
        java.util.ArrayList<com.android.server.backup.DataChangedJournal> journals = com.android.server.backup.DataChangedJournal.listJournals(this.mJournalDir);
        journals.removeAll(java.util.Collections.singletonList(this.mJournal));
        if (!journals.isEmpty()) {
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Found " + journals.size() + " stale backup journal(s), scheduling."));
        }
        final java.util.Set<java.lang.String> packageNames = new java.util.LinkedHashSet<>();
        for (com.android.server.backup.DataChangedJournal journal : journals) {
            try {
                journal.forEach(new java.util.function.Consumer() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda6
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$parseLeftoverJournals$0(packageNames, (java.lang.String) obj);
                    }
                });
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Can't read " + journal), e);
            }
        }
        if (!packageNames.isEmpty()) {
            java.lang.String msg = "Stale backup journals: Scheduled " + packageNames.size() + " package(s) total";
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, msg));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$parseLeftoverJournals$0(java.util.Set packageNames, java.lang.String packageName) {
        if (packageNames.add(packageName)) {
            dataChangedImpl(packageName);
        }
    }

    public java.util.Set<java.lang.String> getExcludedRestoreKeys(java.lang.String packageName) {
        return this.mBackupPreferences.getExcludedRestoreKeysForPackage(packageName);
    }

    public byte[] randomBytes(int bits) {
        byte[] array = new byte[bits / 8];
        this.mRng.nextBytes(array);
        return array;
    }

    public boolean setBackupPassword(java.lang.String currentPw, java.lang.String newPw) {
        return this.mBackupPasswordManager.setBackupPassword(currentPw, newPw);
    }

    public boolean hasBackupPassword() {
        return this.mBackupPasswordManager.hasBackupPassword();
    }

    public boolean backupPasswordMatches(java.lang.String currentPw) {
        return this.mBackupPasswordManager.backupPasswordMatches(currentPw);
    }

    public void recordInitPending(boolean isPending, java.lang.String transportName, java.lang.String transportDirName) {
        synchronized (this.mQueueLock) {
            java.io.File stateDir = new java.io.File(this.mBaseStateDir, transportDirName);
            java.io.File initPendingFile = new java.io.File(stateDir, INIT_SENTINEL_FILE_NAME);
            if (isPending) {
                this.mPendingInits.add(transportName);
                try {
                    new java.io.FileOutputStream(initPendingFile).close();
                } catch (java.io.IOException e) {
                }
            } else {
                initPendingFile.delete();
                this.mPendingInits.remove(transportName);
            }
        }
    }

    public void resetBackupState(java.io.File stateFileDir) {
        synchronized (this.mQueueLock) {
            this.mProcessedPackagesJournal.reset();
            this.mCurrentToken = 0L;
            writeRestoreTokens();
            for (java.io.File sf : stateFileDir.listFiles()) {
                if (!sf.getName().equals(INIT_SENTINEL_FILE_NAME)) {
                    sf.delete();
                }
            }
        }
        synchronized (this.mBackupParticipants) {
            int numParticipants = this.mBackupParticipants.size();
            for (int i = 0; i < numParticipants; i++) {
                java.util.HashSet<java.lang.String> participants = this.mBackupParticipants.valueAt(i);
                if (participants != null) {
                    for (java.lang.String packageName : participants) {
                        dataChangedImpl(packageName);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTransportRegistered(java.lang.String transportName, java.lang.String transportDirName) {
        long timeMs = android.os.SystemClock.elapsedRealtime() - this.mRegisterTransportsRequestedTime;
        android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Transport " + transportName + " registered " + timeMs + "ms after first request (delay = 3000ms)"));
        java.io.File stateDir = new java.io.File(this.mBaseStateDir, transportDirName);
        stateDir.mkdirs();
        java.io.File initSentinel = new java.io.File(stateDir, INIT_SENTINEL_FILE_NAME);
        if (initSentinel.exists()) {
            synchronized (this.mQueueLock) {
                this.mPendingInits.add(transportName);
                this.mAlarmManager.set(0, java.lang.System.currentTimeMillis() + 60000, this.mRunInitIntent);
            }
        }
    }

    /* JADX INFO: renamed from: com.android.server.backup.UserBackupManagerService$2, reason: invalid class name */
    class AnonymousClass2 extends android.content.BroadcastReceiver {
        AnonymousClass2() {
        }

        /* JADX WARN: Removed duplicated region for block: B:44:0x00c9  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r19, android.content.Intent r20) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 419
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.UserBackupManagerService.AnonymousClass2.onReceive(android.content.Context, android.content.Intent):void");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(java.lang.String packageName, java.lang.String[] components) {
            com.android.server.backup.UserBackupManagerService.this.mTransportManager.onPackageChanged(packageName, components);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$1(java.lang.String packageName) {
            com.android.server.backup.UserBackupManagerService.this.mTransportManager.onPackageAdded(packageName);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$2(java.lang.String packageName) {
            com.android.server.backup.UserBackupManagerService.this.mTransportManager.onPackageRemoved(packageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addPackageParticipantsLocked(java.lang.String[] packageNames) {
        java.util.List<android.content.pm.PackageInfo> targetApps = allAgentPackages();
        if (packageNames != null) {
            for (java.lang.String packageName : packageNames) {
                addPackageParticipantsLockedInner(packageName, targetApps);
            }
            return;
        }
        addPackageParticipantsLockedInner(null, targetApps);
    }

    private void addPackageParticipantsLockedInner(java.lang.String packageName, java.util.List<android.content.pm.PackageInfo> targetPkgs) {
        for (android.content.pm.PackageInfo pkg : targetPkgs) {
            if (packageName == null || pkg.packageName.equals(packageName)) {
                int uid = pkg.applicationInfo.uid;
                java.util.HashSet<java.lang.String> set = this.mBackupParticipants.get(uid);
                if (set == null) {
                    set = new java.util.HashSet<>();
                    this.mBackupParticipants.put(uid, set);
                }
                set.add(pkg.packageName);
                android.os.Message msg = this.mBackupHandler.obtainMessage(16, pkg.packageName);
                this.mBackupHandler.sendMessage(msg);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removePackageParticipantsLocked(java.lang.String[] packageNames, int oldUid) {
        if (packageNames == null) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "removePackageParticipants with null list"));
            return;
        }
        for (java.lang.String pkg : packageNames) {
            java.util.HashSet<java.lang.String> set = this.mBackupParticipants.get(oldUid);
            if (set != null && set.contains(pkg)) {
                removePackageFromSetLocked(set, pkg);
                if (set.isEmpty()) {
                    this.mBackupParticipants.remove(oldUid);
                }
            }
        }
    }

    private void removePackageFromSetLocked(java.util.HashSet<java.lang.String> set, java.lang.String packageName) {
        if (set.contains(packageName)) {
            set.remove(packageName);
            this.mPendingBackups.remove(packageName);
        }
    }

    private java.util.List<android.content.pm.PackageInfo> allAgentPackages() {
        java.util.List<android.content.pm.PackageInfo> packages = this.mPackageManager.getInstalledPackagesAsUser(134217728, this.mUserId);
        int numPackages = packages.size();
        for (int a = numPackages - 1; a >= 0; a--) {
            android.content.pm.PackageInfo pkg = packages.get(a);
            try {
                android.content.pm.ApplicationInfo app = pkg.applicationInfo;
                if ((app.flags & 32768) == 0 || app.backupAgentName == null || (app.flags & 67108864) != 0) {
                    packages.remove(a);
                } else {
                    android.content.pm.ApplicationInfo app2 = this.mPackageManager.getApplicationInfoAsUser(pkg.packageName, 1024, this.mUserId);
                    pkg.applicationInfo.sharedLibraryFiles = app2.sharedLibraryFiles;
                    pkg.applicationInfo.sharedLibraryInfos = app2.sharedLibraryInfos;
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                packages.remove(a);
            }
        }
        return packages;
    }

    public void logBackupComplete(java.lang.String packageName) {
        if (packageName.equals(PACKAGE_MANAGER_SENTINEL)) {
            return;
        }
        for (java.lang.String receiver : this.mConstants.getBackupFinishedNotificationReceivers()) {
            android.content.Intent notification = new android.content.Intent();
            notification.setAction(BACKUP_FINISHED_ACTION);
            notification.setPackage(receiver);
            notification.addFlags(268435488);
            notification.putExtra("packageName", packageName);
            this.mContext.sendBroadcastAsUser(notification, android.os.UserHandle.of(this.mUserId));
        }
        this.mProcessedPackagesJournal.addPackage(packageName);
    }

    public void writeRestoreTokens() {
        try {
            java.io.RandomAccessFile af = new java.io.RandomAccessFile(this.mTokenFile, "rwd");
            try {
                af.writeInt(1);
                af.writeLong(this.mAncestralToken);
                af.writeLong(this.mCurrentToken);
                if (this.mAncestralPackages == null) {
                    af.writeInt(-1);
                } else {
                    af.writeInt(this.mAncestralPackages.size());
                    android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Ancestral packages:  " + this.mAncestralPackages.size()));
                    for (java.lang.String pkgName : this.mAncestralPackages) {
                        af.writeUTF(pkgName);
                    }
                }
                af.close();
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unable to write token file:"), e);
        }
    }

    public android.app.IBackupAgent bindToAgentSynchronous(android.content.pm.ApplicationInfo app, int mode, int backupDestination) {
        android.app.IBackupAgent agent = null;
        synchronized (this.mAgentConnectLock) {
            this.mUserBackupManagerServiceExt.hookInBindToAgentSynchronous(app.packageName, null, true);
            try {
                if (this.mActivityManager.bindBackupAgent(app.packageName, mode, this.mUserId, backupDestination)) {
                    android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "awaiting agent for " + app));
                    long timeoutMark = java.lang.System.currentTimeMillis() + 10000;
                    while (this.mUserBackupManagerServiceExt.hookForBindBackupAgentSecond(app.packageName) && this.mUserBackupManagerServiceExt.hookForBindBackupAgent(app.packageName) == null && java.lang.System.currentTimeMillis() < timeoutMark) {
                        try {
                            this.mAgentConnectLock.wait(5000L);
                        } catch (java.lang.InterruptedException e) {
                            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Interrupted: " + e));
                            this.mUserBackupManagerServiceExt.hookInBindToAgentSynchronous(app.packageName, null, false);
                        }
                    }
                    if (this.mUserBackupManagerServiceExt.hookForBindBackupAgentSecond(app.packageName)) {
                        android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Timeout waiting for agent " + app));
                        this.mUserBackupManagerServiceExt.hookInBindToAgentSynchronous(app.packageName, null, true);
                    }
                    android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "got agent " + this.mUserBackupManagerServiceExt.hookForBindBackupAgent(app.packageName)));
                    agent = this.mUserBackupManagerServiceExt.hookForBindBackupAgent(app.packageName);
                }
            } catch (android.os.RemoteException e2) {
            }
        }
        if (agent == null) {
            this.mUserBackupManagerServiceExt.hookBindAgentBeforeClearPendingBackup(backupDestination, app.uid, this.mActivityManagerInternal, this.mUserId);
        }
        return agent;
    }

    public void unbindAgent(android.content.pm.ApplicationInfo app) {
        try {
            this.mActivityManager.unbindBackupAgent(app);
        } catch (android.os.RemoteException e) {
        }
    }

    public void clearApplicationDataAfterRestoreFailure(java.lang.String packageName) {
        clearApplicationDataSynchronous(packageName, true, false);
    }

    public void clearApplicationDataBeforeRestore(java.lang.String packageName) {
        clearApplicationDataSynchronous(packageName, false, true);
    }

    private void clearApplicationDataSynchronous(java.lang.String packageName, boolean checkFlagAllowClearUserDataOnFailedRestore, boolean keepSystemState) {
        boolean shouldClearData;
        try {
            android.content.pm.ApplicationInfo applicationInfo = this.mPackageManager.getPackageInfoAsUser(packageName, 0, this.mUserId).applicationInfo;
            if (checkFlagAllowClearUserDataOnFailedRestore && applicationInfo.targetSdkVersion >= 29) {
                shouldClearData = (applicationInfo.privateFlags & 67108864) != 0;
            } else {
                shouldClearData = (applicationInfo.flags & 64) != 0;
            }
            if (!shouldClearData) {
                return;
            }
            com.android.server.backup.internal.ClearDataObserver observer = new com.android.server.backup.internal.ClearDataObserver(this);
            synchronized (this.mClearDataLock) {
                this.mUserBackupManagerServiceExt.hookInOnRemoveCompleted(packageName, true);
                try {
                    this.mActivityManager.clearApplicationUserData(packageName, keepSystemState, observer, this.mUserId);
                } catch (android.os.RemoteException e) {
                }
                long timeoutMark = java.lang.System.currentTimeMillis() + 30000;
                while (this.mUserBackupManagerServiceExt.hookInClearApplicationDataSynchronous(packageName) && java.lang.System.currentTimeMillis() < timeoutMark) {
                    try {
                        this.mClearDataLock.wait(5000L);
                    } catch (java.lang.InterruptedException e2) {
                        this.mUserBackupManagerServiceExt.hookInOnRemoveCompleted(packageName, false);
                        android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Interrupted while waiting for " + packageName + " data to be cleared"), e2);
                    }
                }
                if (this.mUserBackupManagerServiceExt.hookInClearApplicationDataSynchronous(packageName)) {
                    android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Clearing app data for " + packageName + " timed out"));
                }
                this.mUserBackupManagerServiceExt.hookAfterClearApplicationDataSynchronous(packageName);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e3) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Tried to clear data for " + packageName + " but not found"));
        }
    }

    private com.android.server.backup.utils.BackupEligibilityRules getEligibilityRulesForRestoreAtInstall(long restoreToken) {
        if (this.mAncestralBackupDestination == 1 && restoreToken == this.mAncestralToken) {
            return getEligibilityRulesForOperation(1);
        }
        return this.mScheduledBackupEligibility;
    }

    public long getAvailableRestoreToken(java.lang.String packageName) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "getAvailableRestoreToken");
        long token = this.mAncestralToken;
        synchronized (this.mQueueLock) {
            if (this.mCurrentToken != 0 && this.mProcessedPackagesJournal.hasBeenProcessed(packageName)) {
                token = this.mCurrentToken;
            }
        }
        return token;
    }

    public int requestBackup(java.lang.String[] packages, android.app.backup.IBackupObserver observer, int flags) {
        return requestBackup(packages, observer, null, flags);
    }

    public int requestBackup(java.lang.String[] packages, android.app.backup.IBackupObserver observer, android.app.backup.IBackupManagerMonitor monitor, int flags) {
        int logTag;
        com.android.server.backup.utils.BackupManagerMonitorEventSender mBackupManagerMonitorEventSender = getBMMEventSender(monitor);
        this.mContext.enforceCallingPermission("android.permission.BACKUP", "requestBackup");
        if (packages == null || packages.length < 1) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "No packages named for backup request"));
            com.android.server.backup.utils.BackupObserverUtils.sendBackupFinished(observer, -1000);
            mBackupManagerMonitorEventSender.monitorEvent(49, null, 1, null);
            throw new java.lang.IllegalArgumentException("No packages are provided for backup");
        }
        if (!this.mEnabled || !this.mSetupComplete) {
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Backup requested but enabled=" + this.mEnabled + " setupComplete=" + this.mSetupComplete));
            com.android.server.backup.utils.BackupObserverUtils.sendBackupFinished(observer, -2001);
            if (this.mSetupComplete) {
                logTag = 13;
            } else {
                logTag = 14;
            }
            mBackupManagerMonitorEventSender.monitorEvent(logTag, null, 3, null);
            return -2001;
        }
        try {
            java.lang.String transportDirName = this.mTransportManager.getTransportDirName(this.mTransportManager.getCurrentTransportName());
            final com.android.server.backup.transport.TransportConnection transportConnection = this.mTransportManager.getCurrentTransportClientOrThrow("BMS.requestBackup()");
            int backupDestination = getBackupDestinationFromTransport(transportConnection);
            com.android.server.backup.internal.OnTaskFinishedListener listener = new com.android.server.backup.internal.OnTaskFinishedListener() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda5
                @Override // com.android.server.backup.internal.OnTaskFinishedListener
                public final void onFinished(java.lang.String str) {
                    this.f$0.lambda$requestBackup$1(transportConnection, str);
                }
            };
            com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules = getEligibilityRulesForOperation(backupDestination);
            android.os.Message msg = this.mBackupHandler.obtainMessage(15);
            msg.obj = getRequestBackupParams(packages, observer, monitor, flags, backupEligibilityRules, transportConnection, transportDirName, listener);
            this.mBackupHandler.sendMessage(msg);
            return 0;
        } catch (android.os.RemoteException | com.android.server.backup.transport.TransportNotAvailableException | com.android.server.backup.transport.TransportNotRegisteredException e) {
            com.android.server.backup.utils.BackupObserverUtils.sendBackupFinished(observer, -1000);
            mBackupManagerMonitorEventSender.monitorEvent(50, null, 1, null);
            return -1000;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestBackup$1(com.android.server.backup.transport.TransportConnection transportConnection, java.lang.String caller) {
        this.mTransportManager.disposeOfTransportClient(transportConnection, caller);
    }

    com.android.server.backup.params.BackupParams getRequestBackupParams(java.lang.String[] packages, android.app.backup.IBackupObserver observer, android.app.backup.IBackupManagerMonitor monitor, int flags, com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules, com.android.server.backup.transport.TransportConnection transportConnection, java.lang.String transportDirName, com.android.server.backup.internal.OnTaskFinishedListener listener) {
        java.util.ArrayList<java.lang.String> fullBackupList = new java.util.ArrayList<>();
        java.util.ArrayList<java.lang.String> kvBackupList = new java.util.ArrayList<>();
        for (java.lang.String packageName : packages) {
            if (!PACKAGE_MANAGER_SENTINEL.equals(packageName)) {
                try {
                    android.content.pm.PackageInfo packageInfo = this.mPackageManager.getPackageInfoAsUser(packageName, 134217728, this.mUserId);
                    if (!backupEligibilityRules.appIsEligibleForBackup(packageInfo.applicationInfo)) {
                        com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(observer, packageName, -2001);
                    } else if (backupEligibilityRules.appGetsFullBackup(packageInfo)) {
                        fullBackupList.add(packageInfo.packageName);
                    } else {
                        kvBackupList.add(packageInfo.packageName);
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    com.android.server.backup.utils.BackupObserverUtils.sendBackupOnPackageResult(observer, packageName, -2002);
                }
            } else {
                kvBackupList.add(packageName);
            }
        }
        android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_REQUESTED, java.lang.Integer.valueOf(packages.length), java.lang.Integer.valueOf(kvBackupList.size()), java.lang.Integer.valueOf(fullBackupList.size()));
        boolean nonIncrementalBackup = (flags & 1) != 0;
        return new com.android.server.backup.params.BackupParams(transportConnection, transportDirName, kvBackupList, fullBackupList, observer, monitor, listener, true, nonIncrementalBackup, backupEligibilityRules);
    }

    public void cancelBackups() {
        this.mContext.enforceCallingPermission("android.permission.BACKUP", "cancelBackups");
        long oldToken = android.os.Binder.clearCallingIdentity();
        try {
            java.util.Set<java.lang.Integer> operationsToCancel = this.mOperationStorage.operationTokensForOpType(2);
            for (java.lang.Integer token : operationsToCancel) {
                this.mOperationStorage.cancelOperation(token.intValue(), true, new java.util.function.IntConsumer() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda15
                    @Override // java.util.function.IntConsumer
                    public final void accept(int i) {
                        com.android.server.backup.UserBackupManagerService.lambda$cancelBackups$2(i);
                    }
                });
            }
            com.android.server.backup.KeyValueBackupJob.schedule(this.mUserId, this.mContext, 3600000L, this);
            com.android.server.backup.FullBackupJob.schedule(this.mUserId, this.mContext, com.android.server.usage.AppStandbyController.ConstantsObserver.DEFAULT_SYSTEM_UPDATE_TIMEOUT, this);
        } finally {
            android.os.Binder.restoreCallingIdentity(oldToken);
        }
    }

    static /* synthetic */ void lambda$cancelBackups$2(int operationType) {
    }

    public void prepareOperationTimeout(int token, long interval, com.android.server.backup.BackupRestoreTask callback, int operationType) {
        if (operationType != 0 && operationType != 1 && operationType != 101 && operationType != 102) {
            android.util.Slog.wtf(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "prepareOperationTimeout() doesn't support operation " + java.lang.Integer.toHexString(token) + " of type " + operationType));
            return;
        }
        this.mOperationStorage.registerOperation(token, 0, callback, operationType);
        android.os.Message msg = this.mBackupHandler.obtainMessage(getMessageIdForOperationType(operationType), token, 0, callback);
        this.mBackupHandler.sendMessageDelayed(msg, interval);
    }

    private int getMessageIdForOperationType(int operationType) {
        switch (operationType) {
            case 0:
            case 101:
                return 17;
            case 1:
            case 102:
                return 18;
            default:
                android.util.Slog.wtf(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "getMessageIdForOperationType called on invalid operation type: " + operationType));
                return -1;
        }
    }

    public boolean waitUntilOperationComplete(int token) {
        return this.mOperationStorage.waitUntilOperationComplete(token, new java.util.function.IntConsumer() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda13
            @Override // java.util.function.IntConsumer
            public final void accept(int i) {
                this.f$0.lambda$waitUntilOperationComplete$3(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$waitUntilOperationComplete$3(int operationType) {
        this.mBackupHandler.removeMessages(getMessageIdForOperationType(operationType));
    }

    public void handleCancel(int token, boolean cancelAll) {
        this.mOperationStorage.cancelOperation(token, cancelAll, new java.util.function.IntConsumer() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda10
            @Override // java.util.function.IntConsumer
            public final void accept(int i) {
                this.f$0.lambda$handleCancel$4(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleCancel$4(int operationType) {
        if (operationType == 0 || operationType == 1 || operationType == 101 || operationType == 102) {
            this.mBackupHandler.removeMessages(getMessageIdForOperationType(operationType));
        }
    }

    public boolean isBackupOperationInProgress() {
        return this.mOperationStorage.isBackupOperationInProgress();
    }

    public void tearDownAgentAndKill(android.content.pm.ApplicationInfo app) {
        if (app == null) {
            return;
        }
        long oldCode = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mActivityManager.unbindBackupAgent(app);
                if (!android.os.UserHandle.isCore(app.uid) && !app.packageName.equals("com.android.backupconfirm")) {
                    this.mActivityManager.killApplicationProcess(app.processName, app.uid);
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Lost app trying to shut down"));
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(oldCode);
        }
    }

    public void scheduleNextFullBackupJob(long transportMinLatency) {
        synchronized (this.mQueueLock) {
            if (this.mFullBackupQueue.size() > 0) {
                long upcomingLastBackup = this.mFullBackupQueue.get(0).lastBackup;
                long timeSinceLast = java.lang.System.currentTimeMillis() - upcomingLastBackup;
                long interval = this.mConstants.getFullBackupIntervalMilliseconds();
                long appLatency = timeSinceLast < interval ? interval - timeSinceLast : 0L;
                long latency = java.lang.Math.max(transportMinLatency, appLatency);
                com.android.server.backup.FullBackupJob.schedule(this.mUserId, this.mContext, latency, this);
            } else {
                android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Full backup queue empty; not scheduling"));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dequeueFullBackupLocked(java.lang.String packageName) {
        int numPackages = this.mFullBackupQueue.size();
        for (int i = numPackages - 1; i >= 0; i--) {
            com.android.server.backup.fullbackup.FullBackupEntry e = this.mFullBackupQueue.get(i);
            if (packageName.equals(e.packageName)) {
                this.mFullBackupQueue.remove(i);
            }
        }
    }

    public void enqueueFullBackup(java.lang.String packageName, long lastBackedUp) {
        com.android.server.backup.fullbackup.FullBackupEntry newEntry = new com.android.server.backup.fullbackup.FullBackupEntry(packageName, lastBackedUp);
        synchronized (this.mQueueLock) {
            dequeueFullBackupLocked(packageName);
            int which = -1;
            if (lastBackedUp > 0) {
                which = this.mFullBackupQueue.size() - 1;
                while (true) {
                    if (which < 0) {
                        break;
                    }
                    com.android.server.backup.fullbackup.FullBackupEntry entry = this.mFullBackupQueue.get(which);
                    if (entry.lastBackup > lastBackedUp) {
                        which--;
                    } else {
                        this.mFullBackupQueue.add(which + 1, newEntry);
                        break;
                    }
                }
            }
            if (which < 0) {
                this.mFullBackupQueue.add(0, newEntry);
            }
        }
        writeFullBackupScheduleAsync();
    }

    private boolean fullBackupAllowable(java.lang.String transportName) {
        if (!this.mTransportManager.isTransportRegistered(transportName)) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Transport not registered; full data backup not performed"));
            return false;
        }
        try {
            java.lang.String transportDirName = this.mTransportManager.getTransportDirName(transportName);
            java.io.File stateDir = new java.io.File(this.mBaseStateDir, transportDirName);
            java.io.File pmState = new java.io.File(stateDir, PACKAGE_MANAGER_SENTINEL);
            if (pmState.length() <= 0) {
                android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Full backup requested but dataset not yet initialized"));
                return false;
            }
            return true;
        } catch (java.lang.Exception e) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unable to get transport name: " + e.getMessage()));
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01a3  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01a5  */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r3v2 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v30 */
    /* JADX WARN: Type inference failed for: r3v31 */
    /* JADX WARN: Type inference failed for: r3v32 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean beginFullBackup(com.android.server.backup.FullBackupJob r31) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 704
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.UserBackupManagerService.beginFullBackup(com.android.server.backup.FullBackupJob):boolean");
    }

    public void endFullBackup() {
        java.lang.Runnable endFullBackupRunnable = new java.lang.Runnable() { // from class: com.android.server.backup.UserBackupManagerService.3
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.backup.fullbackup.PerformFullTransportBackupTask pftbt = null;
                synchronized (com.android.server.backup.UserBackupManagerService.this.mQueueLock) {
                    if (com.android.server.backup.UserBackupManagerService.this.mRunningFullBackupTask != null) {
                        pftbt = com.android.server.backup.UserBackupManagerService.this.mRunningFullBackupTask;
                    }
                }
                if (pftbt != null) {
                    android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, com.android.server.backup.UserBackupManagerService.addUserIdToLogMessage(com.android.server.backup.UserBackupManagerService.this.mUserId, "Telling running backup to stop"));
                    pftbt.handleCancel(true);
                }
            }
        };
        new java.lang.Thread(endFullBackupRunnable, "end-full-backup").start();
    }

    public void restoreWidgetData(java.lang.String packageName, byte[] widgetData) {
        com.android.server.AppWidgetBackupBridge.restoreWidgetState(packageName, widgetData, this.mUserId);
    }

    public void dataChangedImpl(java.lang.String packageName) {
        java.util.HashSet<java.lang.String> targets = dataChangedTargets(packageName);
        dataChangedImpl(packageName, targets);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dataChangedImpl(java.lang.String packageName, java.util.HashSet<java.lang.String> targets) {
        if (targets == null) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "dataChanged but no participant pkg='" + packageName + "' uid=" + android.os.Binder.getCallingUid()));
            return;
        }
        synchronized (this.mQueueLock) {
            if (targets.contains(packageName)) {
                com.android.server.backup.keyvalue.BackupRequest req = new com.android.server.backup.keyvalue.BackupRequest(packageName);
                if (this.mPendingBackups.put(packageName, req) == null) {
                    writeToJournalLocked(packageName);
                }
            }
        }
        com.android.server.backup.KeyValueBackupJob.schedule(this.mUserId, this.mContext, this);
    }

    private java.util.HashSet<java.lang.String> dataChangedTargets(java.lang.String packageName) {
        java.util.HashSet<java.lang.String> hashSetUnion;
        java.util.HashSet<java.lang.String> hashSet;
        if (this.mContext.checkPermission("android.permission.BACKUP", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid()) == -1) {
            synchronized (this.mBackupParticipants) {
                hashSet = this.mBackupParticipants.get(android.os.Binder.getCallingUid());
            }
            return hashSet;
        }
        if (PACKAGE_MANAGER_SENTINEL.equals(packageName)) {
            return com.google.android.collect.Sets.newHashSet(new java.lang.String[]{PACKAGE_MANAGER_SENTINEL});
        }
        synchronized (this.mBackupParticipants) {
            hashSetUnion = com.android.server.backup.utils.SparseArrayUtils.union(this.mBackupParticipants);
        }
        return hashSetUnion;
    }

    private void writeToJournalLocked(java.lang.String str) {
        try {
            if (this.mJournal == null) {
                this.mJournal = com.android.server.backup.DataChangedJournal.newJournal(this.mJournalDir);
            }
            this.mJournal.addPackage(str);
        } catch (java.io.IOException e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Can't write " + str + " to backup journal"), e);
            this.mJournal = null;
        }
    }

    public void dataChanged(final java.lang.String packageName) {
        final java.util.HashSet<java.lang.String> targets = dataChangedTargets(packageName);
        if (targets == null) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "dataChanged but no participant pkg='" + packageName + "' uid=" + android.os.Binder.getCallingUid()));
        } else {
            this.mBackupHandler.post(new java.lang.Runnable() { // from class: com.android.server.backup.UserBackupManagerService.4
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.backup.UserBackupManagerService.this.dataChangedImpl(packageName, targets);
                }
            });
        }
    }

    public void initializeTransports(java.lang.String[] transportNames, android.app.backup.IBackupObserver observer) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "initializeTransport");
        android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "initializeTransport(): " + java.util.Arrays.asList(transportNames)));
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            this.mWakelock.acquire();
            com.android.server.backup.internal.OnTaskFinishedListener listener = new com.android.server.backup.internal.OnTaskFinishedListener() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda12
                @Override // com.android.server.backup.internal.OnTaskFinishedListener
                public final void onFinished(java.lang.String str) {
                    this.f$0.lambda$initializeTransports$5(str);
                }
            };
            this.mBackupHandler.post(new com.android.server.backup.internal.PerformInitializeTask(this, transportNames, observer, listener));
        } finally {
            android.os.Binder.restoreCallingIdentity(oldId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeTransports$5(java.lang.String caller) {
        this.mWakelock.release();
    }

    public void setAncestralSerialNumber(long ancestralSerialNumber) {
        this.mContext.enforceCallingPermission("android.permission.BACKUP", "setAncestralSerialNumber");
        android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Setting ancestral work profile id to " + ancestralSerialNumber));
        try {
            java.io.RandomAccessFile af = new java.io.RandomAccessFile(getAncestralSerialNumberFile(), "rwd");
            try {
                af.writeLong(ancestralSerialNumber);
                af.close();
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unable to write to work profile serial mapping file:"), e);
        }
    }

    public long getAncestralSerialNumber() {
        try {
            java.io.RandomAccessFile af = new java.io.RandomAccessFile(getAncestralSerialNumberFile(), com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD);
            try {
                long j = af.readLong();
                af.close();
                return j;
            } catch (java.lang.Throwable th) {
                try {
                    af.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.FileNotFoundException e) {
            return -1L;
        } catch (java.io.IOException e2) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unable to read work profile serial number file:"), e2);
            return -1L;
        }
    }

    private java.io.File getAncestralSerialNumberFile() {
        if (this.mAncestralSerialNumberFile == null) {
            this.mAncestralSerialNumberFile = new java.io.File(com.android.server.backup.UserBackupManagerFiles.getBaseStateDir(getUserId()), SERIAL_ID_FILE);
        }
        return this.mAncestralSerialNumberFile;
    }

    void setAncestralSerialNumberFile(java.io.File ancestralSerialNumberFile) {
        this.mAncestralSerialNumberFile = ancestralSerialNumberFile;
    }

    public void clearBackupData(java.lang.String transportName, java.lang.String packageName) {
        java.util.HashSet<java.lang.String> apps;
        android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "clearBackupData() of " + packageName + " on " + transportName));
        try {
            android.content.pm.PackageInfo info = this.mPackageManager.getPackageInfoAsUser(packageName, 134217728, this.mUserId);
            if (this.mContext.checkPermission("android.permission.BACKUP", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid()) == -1) {
                apps = this.mBackupParticipants.get(android.os.Binder.getCallingUid());
            } else {
                apps = this.mProcessedPackagesJournal.getPackagesCopy();
            }
            if (apps.contains(packageName)) {
                this.mBackupHandler.removeMessages(12);
                synchronized (this.mQueueLock) {
                    final com.android.server.backup.transport.TransportConnection transportConnection = this.mTransportManager.getTransportClient(transportName, "BMS.clearBackupData()");
                    if (transportConnection == null) {
                        android.os.Message msg = this.mBackupHandler.obtainMessage(12, new com.android.server.backup.params.ClearRetryParams(transportName, packageName));
                        this.mBackupHandler.sendMessageDelayed(msg, 3600000L);
                        return;
                    }
                    long oldId = android.os.Binder.clearCallingIdentity();
                    try {
                        com.android.server.backup.internal.OnTaskFinishedListener listener = new com.android.server.backup.internal.OnTaskFinishedListener() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda8
                            @Override // com.android.server.backup.internal.OnTaskFinishedListener
                            public final void onFinished(java.lang.String str) {
                                this.f$0.lambda$clearBackupData$6(transportConnection, str);
                            }
                        };
                        this.mWakelock.acquire();
                        android.os.Message msg2 = this.mBackupHandler.obtainMessage(4, new com.android.server.backup.params.ClearParams(transportConnection, info, listener));
                        this.mBackupHandler.sendMessage(msg2);
                    } finally {
                        android.os.Binder.restoreCallingIdentity(oldId);
                    }
                }
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "No such package '" + packageName + "' - not clearing backup data"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearBackupData$6(com.android.server.backup.transport.TransportConnection transportConnection, java.lang.String caller) {
        this.mTransportManager.disposeOfTransportClient(transportConnection, caller);
    }

    public void backupNow() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "backupNow");
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            android.os.PowerSaveState result = this.mPowerManager.getPowerSaveState(5);
            if (!result.batterySaverEnabled) {
                android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Scheduling immediate backup pass"));
                synchronized (getQueueLock()) {
                    if (getPendingInits().size() > 0) {
                        try {
                            getAlarmManager().cancel(this.mRunInitIntent);
                            this.mRunInitIntent.send();
                        } catch (android.app.PendingIntent.CanceledException e) {
                            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Run init intent cancelled"));
                        }
                        return;
                    } else {
                        if (isEnabled() && isSetupComplete()) {
                            android.os.Message message = this.mBackupHandler.obtainMessage(1);
                            this.mBackupHandler.sendMessage(message);
                            com.android.server.backup.KeyValueBackupJob.cancel(this.mUserId, this.mContext);
                        }
                        android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Backup pass but enabled=" + isEnabled() + " setupComplete=" + isSetupComplete()));
                        return;
                    }
                }
            }
            android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Not running backup while in battery save mode"));
            com.android.server.backup.KeyValueBackupJob.schedule(this.mUserId, this.mContext, this);
        } finally {
            android.os.Binder.restoreCallingIdentity(oldId);
        }
    }

    public void adbBackup(android.os.ParcelFileDescriptor fd, boolean includeApks, boolean includeObbs, boolean includeShared, boolean doWidgets, boolean doAllApps, boolean includeSystem, boolean compress, boolean doKeyValue, java.lang.String[] pkgList) {
        this.mContext.enforceCallingPermission("android.permission.BACKUP", "adbBackup");
        int callingUserHandle = android.os.UserHandle.getCallingUserId();
        if (callingUserHandle != 0) {
            throw new java.lang.IllegalStateException("Backup supported only for the device owner");
        }
        if (!doAllApps && !includeShared && (pkgList == null || pkgList.length == 0)) {
            throw new java.lang.IllegalArgumentException("Backup requested but neither shared nor any apps named");
        }
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            if (!this.mSetupComplete) {
                android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Backup not supported before setup"));
                try {
                    fd.close();
                } catch (java.io.IOException e) {
                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "IO error closing output for adb backup: " + e.getMessage()));
                }
                android.os.Binder.restoreCallingIdentity(oldId);
                android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Adb backup processing complete."));
                return;
            }
            android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Requesting backup: apks=" + includeApks + " obb=" + includeObbs + " shared=" + includeShared + " all=" + doAllApps + " system=" + includeSystem + " includekeyvalue=" + doKeyValue + " pkgs=" + java.util.Arrays.toString(pkgList)));
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Beginning adb backup..."));
            com.android.server.backup.utils.BackupEligibilityRules eligibilityRules = getEligibilityRulesForOperation(2);
            com.android.server.backup.params.AdbBackupParams params = new com.android.server.backup.params.AdbBackupParams(fd, includeApks, includeObbs, includeShared, doWidgets, doAllApps, includeSystem, compress, doKeyValue, pkgList, eligibilityRules);
            int token = generateRandomIntegerToken();
            synchronized (this.mAdbBackupRestoreConfirmations) {
                this.mAdbBackupRestoreConfirmations.put(token, params);
            }
            android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Starting backup confirmation UI"));
            if (!startConfirmationUi(token, "fullback")) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unable to launch backup confirmation UI"));
                this.mAdbBackupRestoreConfirmations.delete(token);
                try {
                    fd.close();
                } catch (java.io.IOException e2) {
                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "IO error closing output for adb backup: " + e2.getMessage()));
                }
                android.os.Binder.restoreCallingIdentity(oldId);
                android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Adb backup processing complete."));
                return;
            }
            this.mPowerManager.userActivity(android.os.SystemClock.uptimeMillis(), 0, 0);
            startConfirmationTimeout(token, params);
            android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Waiting for backup completion..."));
            waitForCompletion(params);
            try {
                fd.close();
            } catch (java.io.IOException e3) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "IO error closing output for adb backup: " + e3.getMessage()));
            }
            android.os.Binder.restoreCallingIdentity(oldId);
            android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Adb backup processing complete."));
        } catch (java.lang.Throwable th) {
            try {
                fd.close();
            } catch (java.io.IOException e4) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "IO error closing output for adb backup: " + e4.getMessage()));
            }
            android.os.Binder.restoreCallingIdentity(oldId);
            android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Adb backup processing complete."));
            throw th;
        }
    }

    public void fullTransportBackup(java.lang.String[] pkgNames) {
        java.lang.String str;
        java.lang.String str2;
        this.mContext.enforceCallingPermission("android.permission.BACKUP", "fullTransportBackup");
        int callingUserHandle = android.os.UserHandle.getCallingUserId();
        if (callingUserHandle != 0) {
            throw new java.lang.IllegalStateException("Restore supported only for the device owner");
        }
        java.lang.String transportName = this.mTransportManager.getCurrentTransportName();
        if (fullBackupAllowable(transportName)) {
            android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "fullTransportBackup()"));
            long oldId = android.os.Binder.clearCallingIdentity();
            try {
                java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
                com.android.server.backup.internal.LifecycleOperationStorage lifecycleOperationStorage = this.mOperationStorage;
                com.android.server.backup.utils.BackupEligibilityRules eligibilityRulesForOperation = getEligibilityRulesForOperation(0);
                str2 = com.android.server.backup.BackupManagerService.TAG;
                try {
                    try {
                        java.lang.Runnable task = com.android.server.backup.fullbackup.PerformFullTransportBackupTask.newWithCurrentTransport(this, lifecycleOperationStorage, null, pkgNames, false, null, latch, null, null, false, "BMS.fullTransportBackup()", eligibilityRulesForOperation);
                        this.mWakelock.acquire();
                        new java.lang.Thread(task, "full-transport-master").start();
                        while (true) {
                            try {
                                latch.await();
                                break;
                            } catch (java.lang.InterruptedException e) {
                                str2 = str2;
                            }
                        }
                        long now = java.lang.System.currentTimeMillis();
                        for (java.lang.String pkg : pkgNames) {
                            enqueueFullBackup(pkg, now);
                        }
                        android.os.Binder.restoreCallingIdentity(oldId);
                    } catch (java.lang.IllegalStateException e2) {
                        e = e2;
                        str = str2;
                        android.util.Slog.w(str, "Failed to start backup: ", e);
                        android.os.Binder.restoreCallingIdentity(oldId);
                        return;
                    }
                } catch (java.lang.Throwable th) {
                    e = th;
                    android.os.Binder.restoreCallingIdentity(oldId);
                    throw e;
                }
            } catch (java.lang.IllegalStateException e3) {
                e = e3;
                str = com.android.server.backup.BackupManagerService.TAG;
            } catch (java.lang.Throwable th2) {
                e = th2;
                android.os.Binder.restoreCallingIdentity(oldId);
                throw e;
            }
        } else {
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Full backup not currently possible -- key/value backup not yet run?"));
            str2 = com.android.server.backup.BackupManagerService.TAG;
        }
        android.util.Slog.d(str2, addUserIdToLogMessage(this.mUserId, "Done with full transport backup."));
    }

    public void adbRestore(android.os.ParcelFileDescriptor fd) {
        this.mContext.enforceCallingPermission("android.permission.BACKUP", "adbRestore");
        int callingUserHandle = android.os.UserHandle.getCallingUserId();
        if (callingUserHandle != 0) {
            throw new java.lang.IllegalStateException("Restore supported only for the device owner");
        }
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            if (!this.mSetupComplete) {
                android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Full restore not permitted before setup"));
                return;
            }
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Beginning restore..."));
            com.android.server.backup.params.AdbRestoreParams params = new com.android.server.backup.params.AdbRestoreParams(fd);
            int token = generateRandomIntegerToken();
            synchronized (this.mAdbBackupRestoreConfirmations) {
                this.mAdbBackupRestoreConfirmations.put(token, params);
            }
            android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Starting restore confirmation UI, token=" + token));
            if (!startConfirmationUi(token, "fullrest")) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unable to launch restore confirmation"));
                this.mAdbBackupRestoreConfirmations.delete(token);
                try {
                    fd.close();
                } catch (java.io.IOException e) {
                    android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Error trying to close fd after adb restore: " + e));
                }
                android.os.Binder.restoreCallingIdentity(oldId);
                android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "adb restore processing complete."));
                return;
            }
            this.mPowerManager.userActivity(android.os.SystemClock.uptimeMillis(), 0, 0);
            startConfirmationTimeout(token, params);
            android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Waiting for restore completion..."));
            waitForCompletion(params);
            try {
                fd.close();
            } catch (java.io.IOException e2) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Error trying to close fd after adb restore: " + e2));
            }
            android.os.Binder.restoreCallingIdentity(oldId);
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "adb restore processing complete."));
        } finally {
            try {
                fd.close();
            } catch (java.io.IOException e3) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Error trying to close fd after adb restore: " + e3));
            }
            android.os.Binder.restoreCallingIdentity(oldId);
            android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "adb restore processing complete."));
        }
    }

    public void excludeKeysFromRestore(java.lang.String packageName, java.util.List<java.lang.String> keys) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "excludeKeysFromRestore");
        this.mBackupPreferences.addExcludedKeys(packageName, keys);
    }

    public void reportDelayedRestoreResult(java.lang.String packageName, java.util.List<android.app.backup.BackupRestoreEventLogger.DataTypeResult> results) {
        java.lang.String transport = this.mTransportManager.getCurrentTransportName();
        if (transport == null) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Failed to send delayed restore logs as no transport selected");
            return;
        }
        com.android.server.backup.transport.TransportConnection transportConnection = null;
        try {
            try {
                android.content.pm.PackageInfo packageInfo = getPackageManager().getPackageInfoAsUser(packageName, android.content.pm.PackageManager.PackageInfoFlags.of(0L), getUserId());
                transportConnection = this.mTransportManager.getTransportClientOrThrow(transport, "BMS.reportDelayedRestoreResult");
                com.android.server.backup.transport.BackupTransportClient transportClient = transportConnection.connectOrThrow("BMS.reportDelayedRestoreResult");
                android.app.backup.IBackupManagerMonitor monitor = transportClient.getBackupManagerMonitor();
                com.android.server.backup.utils.BackupManagerMonitorEventSender mBackupManagerMonitorEventSender = getBMMEventSender(monitor);
                mBackupManagerMonitorEventSender.sendAgentLoggingResults(packageInfo, results, 1);
            } catch (android.content.pm.PackageManager.NameNotFoundException | android.os.RemoteException | com.android.server.backup.transport.TransportNotAvailableException | com.android.server.backup.transport.TransportNotRegisteredException e) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Failed to send delayed restore logs: " + e);
                if (transportConnection != null) {
                }
            }
            if (transportConnection != null) {
                this.mTransportManager.disposeOfTransportClient(transportConnection, "BMS.reportDelayedRestoreResult");
            }
        } catch (java.lang.Throwable th) {
            if (transportConnection != null) {
                this.mTransportManager.disposeOfTransportClient(transportConnection, "BMS.reportDelayedRestoreResult");
            }
            throw th;
        }
    }

    private boolean startConfirmationUi(int token, java.lang.String action) {
        try {
            android.content.Intent confIntent = new android.content.Intent(action);
            confIntent.setClassName("com.android.backupconfirm", "com.android.backupconfirm.BackupRestoreConfirmation");
            confIntent.putExtra("conftoken", token);
            confIntent.addFlags(536870912);
            this.mContext.startActivityAsUser(confIntent, android.os.UserHandle.SYSTEM);
            return true;
        } catch (android.content.ActivityNotFoundException e) {
            return false;
        }
    }

    private void startConfirmationTimeout(int token, com.android.server.backup.params.AdbParams params) {
        android.os.Message msg = this.mBackupHandler.obtainMessage(9, token, 0, params);
        this.mBackupHandler.sendMessageDelayed(msg, 60000L);
    }

    private void waitForCompletion(com.android.server.backup.params.AdbParams params) {
        synchronized (params.latch) {
            while (!params.latch.get()) {
                try {
                    params.latch.wait();
                } catch (java.lang.InterruptedException e) {
                }
            }
        }
    }

    public void signalAdbBackupRestoreCompletion(com.android.server.backup.params.AdbParams params) {
        synchronized (params.latch) {
            params.latch.set(true);
            params.latch.notifyAll();
        }
    }

    public void acknowledgeAdbBackupOrRestore(int token, boolean allow, java.lang.String curPassword, java.lang.String encPpassword, android.app.backup.IFullBackupRestoreObserver observer) {
        int verb;
        android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "acknowledgeAdbBackupOrRestore : token=" + token + " allow=" + allow));
        this.mContext.enforceCallingPermission("android.permission.BACKUP", "acknowledgeAdbBackupOrRestore");
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mAdbBackupRestoreConfirmations) {
                com.android.server.backup.params.AdbParams params = this.mAdbBackupRestoreConfirmations.get(token);
                if (params != null) {
                    this.mBackupHandler.removeMessages(9, params);
                    this.mAdbBackupRestoreConfirmations.delete(token);
                    if (allow) {
                        if (params instanceof com.android.server.backup.params.AdbBackupParams) {
                            verb = 2;
                        } else {
                            verb = 10;
                        }
                        params.observer = observer;
                        params.curPassword = curPassword;
                        params.encryptPassword = encPpassword;
                        this.mWakelock.acquire();
                        android.os.Message msg = this.mBackupHandler.obtainMessage(verb, params);
                        this.mBackupHandler.sendMessage(msg);
                    } else {
                        android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "User rejected full backup/restore operation"));
                        signalAdbBackupRestoreCompletion(params);
                    }
                } else {
                    android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Attempted to ack full backup/restore with invalid token"));
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(oldId);
        }
    }

    com.android.server.backup.utils.BackupManagerMonitorEventSender getBMMEventSender(android.app.backup.IBackupManagerMonitor monitor) {
        return new com.android.server.backup.utils.BackupManagerMonitorEventSender(monitor);
    }

    public void setBackupEnabled(boolean enable) {
        setBackupEnabled(enable, true);
    }

    private void setBackupEnabled(boolean enable, boolean persistToDisk) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "setBackupEnabled");
        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Backup enabled => " + enable));
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            boolean wasEnabled = this.mEnabled;
            synchronized (this) {
                if (persistToDisk) {
                    writeEnabledState(enable);
                    this.mEnabled = enable;
                } else {
                    this.mEnabled = enable;
                }
            }
            updateStateOnBackupEnabled(wasEnabled, enable);
        } finally {
            android.os.Binder.restoreCallingIdentity(oldId);
        }
    }

    synchronized void setFrameworkSchedulingEnabled(boolean isEnabled) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "setFrameworkSchedulingEnabled");
        boolean wasEnabled = isFrameworkSchedulingEnabled();
        if (wasEnabled == isEnabled) {
            return;
        }
        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, (isEnabled ? "Enabling" : "Disabling") + " backup scheduling"));
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "backup_scheduling_enabled", isEnabled ? 1 : 0, this.mUserId);
            if (!isEnabled) {
                com.android.server.backup.KeyValueBackupJob.cancel(this.mUserId, this.mContext);
                com.android.server.backup.FullBackupJob.cancel(this.mUserId, this.mContext);
            } else {
                com.android.server.backup.KeyValueBackupJob.schedule(this.mUserId, this.mContext, this);
                scheduleNextFullBackupJob(0L);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(oldId);
        }
    }

    synchronized boolean isFrameworkSchedulingEnabled() {
        int isEnabled;
        isEnabled = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "backup_scheduling_enabled", 1, this.mUserId);
        return isEnabled == 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0018  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void updateStateOnBackupEnabled(boolean r9, boolean r10) {
        /*
            r8 = this;
            java.lang.Object r0 = r8.mQueueLock
            monitor-enter(r0)
            if (r10 == 0) goto L18
            if (r9 != 0) goto L18
            boolean r1 = r8.mSetupComplete     // Catch: java.lang.Throwable -> L64
            if (r1 == 0) goto L18
            int r1 = r8.mUserId     // Catch: java.lang.Throwable -> L64
            android.content.Context r2 = r8.mContext     // Catch: java.lang.Throwable -> L64
            com.android.server.backup.KeyValueBackupJob.schedule(r1, r2, r8)     // Catch: java.lang.Throwable -> L64
            r1 = 0
            r8.scheduleNextFullBackupJob(r1)     // Catch: java.lang.Throwable -> L64
            goto L62
        L18:
            if (r10 != 0) goto L62
            int r1 = r8.mUserId     // Catch: java.lang.Throwable -> L64
            android.content.Context r2 = r8.mContext     // Catch: java.lang.Throwable -> L64
            com.android.server.backup.KeyValueBackupJob.cancel(r1, r2)     // Catch: java.lang.Throwable -> L64
            if (r9 == 0) goto L62
            boolean r1 = r8.mSetupComplete     // Catch: java.lang.Throwable -> L64
            if (r1 == 0) goto L62
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L64
            r1.<init>()     // Catch: java.lang.Throwable -> L64
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L64
            r2.<init>()     // Catch: java.lang.Throwable -> L64
            com.android.server.backup.TransportManager r3 = r8.mTransportManager     // Catch: java.lang.Throwable -> L64
            com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda9 r4 = new com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda9     // Catch: java.lang.Throwable -> L64
            r4.<init>()     // Catch: java.lang.Throwable -> L64
            r3.forEachRegisteredTransport(r4)     // Catch: java.lang.Throwable -> L64
            r3 = 0
        L3c:
            int r4 = r1.size()     // Catch: java.lang.Throwable -> L64
            if (r3 >= r4) goto L56
        L43:
            java.lang.Object r4 = r1.get(r3)     // Catch: java.lang.Throwable -> L64
            java.lang.String r4 = (java.lang.String) r4     // Catch: java.lang.Throwable -> L64
            java.lang.Object r5 = r2.get(r3)     // Catch: java.lang.Throwable -> L64
            java.lang.String r5 = (java.lang.String) r5     // Catch: java.lang.Throwable -> L64
            r6 = 1
            r8.recordInitPending(r6, r4, r5)     // Catch: java.lang.Throwable -> L64
            int r3 = r3 + 1
            goto L3c
        L56:
            android.app.AlarmManager r3 = r8.mAlarmManager     // Catch: java.lang.Throwable -> L64
            long r4 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L64
            android.app.PendingIntent r6 = r8.mRunInitIntent     // Catch: java.lang.Throwable -> L64
            r7 = 0
            r3.set(r7, r4, r6)     // Catch: java.lang.Throwable -> L64
        L62:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L64
            return
        L64:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L64
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.UserBackupManagerService.updateStateOnBackupEnabled(boolean, boolean):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateStateOnBackupEnabled$7(java.util.List transportNames, java.util.List transportDirNames, java.lang.String name) {
        try {
            java.lang.String dirName = this.mTransportManager.getTransportDirName(name);
            transportNames.add(name);
            transportDirNames.add(dirName);
        } catch (com.android.server.backup.transport.TransportNotRegisteredException e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unexpected unregistered transport"), e);
        }
    }

    void writeEnabledState(boolean enable) {
        com.android.server.backup.UserBackupManagerFilePersistedSettings.writeBackupEnableState(this.mUserId, enable);
    }

    boolean readEnabledState() {
        return com.android.server.backup.UserBackupManagerFilePersistedSettings.readBackupEnableState(this.mUserId);
    }

    public void setAutoRestore(boolean doAutoRestore) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "setAutoRestore");
        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Auto restore => " + doAutoRestore));
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this) {
                android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "backup_auto_restore", doAutoRestore ? 1 : 0, this.mUserId);
                this.mAutoRestore = doAutoRestore;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(oldId);
        }
    }

    public boolean isBackupEnabled() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "isBackupEnabled");
        return this.mEnabled;
    }

    public java.lang.String getCurrentTransport() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "getCurrentTransport");
        java.lang.String currentTransport = this.mTransportManager.getCurrentTransportName();
        return currentTransport;
    }

    public android.content.ComponentName getCurrentTransportComponent() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "getCurrentTransportComponent");
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            android.content.ComponentName currentTransportComponent = this.mTransportManager.getCurrentTransportComponent();
            android.os.Binder.restoreCallingIdentity(oldId);
            return currentTransportComponent;
        } catch (com.android.server.backup.transport.TransportNotRegisteredException e) {
            android.os.Binder.restoreCallingIdentity(oldId);
            return null;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(oldId);
            throw th;
        }
    }

    public java.lang.String[] listAllTransports() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "listAllTransports");
        return this.mTransportManager.getRegisteredTransportNames();
    }

    public android.content.ComponentName[] listAllTransportComponents() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "listAllTransportComponents");
        return this.mTransportManager.getRegisteredTransportComponents();
    }

    public void updateTransportAttributes(android.content.ComponentName transportComponent, java.lang.String name, android.content.Intent configurationIntent, java.lang.String currentDestinationString, android.content.Intent dataManagementIntent, java.lang.CharSequence dataManagementLabel) {
        updateTransportAttributes(android.os.Binder.getCallingUid(), transportComponent, name, configurationIntent, currentDestinationString, dataManagementIntent, dataManagementLabel);
    }

    void updateTransportAttributes(int callingUid, android.content.ComponentName transportComponent, java.lang.String name, android.content.Intent configurationIntent, java.lang.String currentDestinationString, android.content.Intent dataManagementIntent, java.lang.CharSequence dataManagementLabel) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "updateTransportAttributes");
        java.util.Objects.requireNonNull(transportComponent, "transportComponent can't be null");
        java.util.Objects.requireNonNull(name, "name can't be null");
        java.util.Objects.requireNonNull(currentDestinationString, "currentDestinationString can't be null");
        com.android.internal.util.Preconditions.checkArgument((dataManagementIntent == null) == (dataManagementLabel == null), "dataManagementLabel should be null iff dataManagementIntent is null");
        try {
            int transportUid = this.mContext.getPackageManager().getPackageUidAsUser(transportComponent.getPackageName(), 0, this.mUserId);
            if (callingUid != transportUid) {
                try {
                    throw new java.lang.SecurityException("Only the transport can change its description");
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    e = e;
                    throw new java.lang.SecurityException("Transport package not found", e);
                }
            } else {
                long oldId = android.os.Binder.clearCallingIdentity();
                try {
                    this.mTransportManager.updateTransportAttributes(transportComponent, name, configurationIntent, currentDestinationString, dataManagementIntent, dataManagementLabel);
                } finally {
                    android.os.Binder.restoreCallingIdentity(oldId);
                }
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            e = e2;
        }
    }

    @java.lang.Deprecated
    public java.lang.String selectBackupTransport(java.lang.String transportName) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "selectBackupTransport");
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            if (!this.mTransportManager.isTransportRegistered(transportName)) {
                android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Could not select transport " + transportName + ", as the transport is not registered."));
                android.os.Binder.restoreCallingIdentity(oldId);
                return null;
            }
            java.lang.String previousTransportName = this.mTransportManager.selectTransport(transportName);
            updateStateForTransport(transportName);
            android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "selectBackupTransport(transport = " + transportName + "): previous transport = " + previousTransportName));
            return previousTransportName;
        } finally {
            android.os.Binder.restoreCallingIdentity(oldId);
        }
    }

    public void selectBackupTransportAsync(final android.content.ComponentName transportComponent, final android.app.backup.ISelectBackupTransportCallback listener) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "selectBackupTransportAsync");
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.String transportString = transportComponent.flattenToShortString();
            android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "selectBackupTransportAsync(transport = " + transportString + ")"));
            this.mBackupHandler.post(new java.lang.Runnable() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$selectBackupTransportAsync$8(transportComponent, listener);
                }
            });
        } finally {
            android.os.Binder.restoreCallingIdentity(oldId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$selectBackupTransportAsync$8(android.content.ComponentName transportComponent, android.app.backup.ISelectBackupTransportCallback listener) {
        java.lang.String transportName = null;
        int result = this.mTransportManager.registerAndSelectTransport(transportComponent);
        if (result == 0) {
            try {
                transportName = this.mTransportManager.getTransportName(transportComponent);
                updateStateForTransport(transportName);
            } catch (com.android.server.backup.transport.TransportNotRegisteredException e) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Transport got unregistered"));
                result = -1;
            }
        }
        try {
            if (transportName != null) {
                listener.onSuccess(transportName);
            } else {
                listener.onFailure(result);
            }
        } catch (android.os.RemoteException e2) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "ISelectBackupTransportCallback listener not available"));
        }
    }

    public java.util.List<android.content.pm.PackageInfo> filterUserFacingPackages(java.util.List<android.content.pm.PackageInfo> packages) {
        if (!shouldSkipUserFacingData()) {
            return packages;
        }
        java.util.List<android.content.pm.PackageInfo> filteredPackages = new java.util.ArrayList<>(packages.size());
        for (android.content.pm.PackageInfo packageInfo : packages) {
            if (!shouldSkipPackage(packageInfo.packageName)) {
                filteredPackages.add(packageInfo);
            } else {
                android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, "Will skip backup/restore for " + packageInfo.packageName);
            }
        }
        return filteredPackages;
    }

    public boolean shouldSkipUserFacingData() {
        return android.provider.Settings.Secure.getInt(this.mContext.getContentResolver(), SKIP_USER_FACING_PACKAGES, 0) != 0;
    }

    public boolean shouldSkipPackage(java.lang.String packageName) {
        return WALLPAPER_PACKAGE.equals(packageName);
    }

    private void updateStateForTransport(java.lang.String newTransportName) {
        android.provider.Settings.Secure.putStringForUser(this.mContext.getContentResolver(), "backup_transport", newTransportName, this.mUserId);
        com.android.server.backup.transport.TransportConnection transportConnection = this.mTransportManager.getTransportClient(newTransportName, "BMS.updateStateForTransport()");
        if (transportConnection != null) {
            try {
                com.android.server.backup.transport.BackupTransportClient transport = transportConnection.connectOrThrow("BMS.updateStateForTransport()");
                this.mCurrentToken = transport.getCurrentRestoreSet();
            } catch (java.lang.Exception e) {
                this.mCurrentToken = 0L;
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Transport " + newTransportName + " not available: current token = 0"));
            }
            this.mTransportManager.disposeOfTransportClient(transportConnection, "BMS.updateStateForTransport()");
            return;
        }
        android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Transport " + newTransportName + " not registered: current token = 0"));
        this.mCurrentToken = 0L;
    }

    public android.content.Intent getConfigurationIntent(java.lang.String transportName) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "getConfigurationIntent");
        try {
            android.content.Intent intent = this.mTransportManager.getTransportConfigurationIntent(transportName);
            return intent;
        } catch (com.android.server.backup.transport.TransportNotRegisteredException e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unable to get configuration intent from transport: " + e.getMessage()));
            return null;
        }
    }

    public java.lang.String getDestinationString(java.lang.String transportName) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "getDestinationString");
        try {
            java.lang.String string = this.mTransportManager.getTransportCurrentDestinationString(transportName);
            return string;
        } catch (com.android.server.backup.transport.TransportNotRegisteredException e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unable to get destination string from transport: " + e.getMessage()));
            return null;
        }
    }

    public android.content.Intent getDataManagementIntent(java.lang.String transportName) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "getDataManagementIntent");
        try {
            android.content.Intent intent = this.mTransportManager.getTransportDataManagementIntent(transportName);
            return intent;
        } catch (com.android.server.backup.transport.TransportNotRegisteredException e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unable to get management intent from transport: " + e.getMessage()));
            return null;
        }
    }

    public java.lang.CharSequence getDataManagementLabel(java.lang.String transportName) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "getDataManagementLabel");
        try {
            java.lang.CharSequence label = this.mTransportManager.getTransportDataManagementLabel(transportName);
            return label;
        } catch (com.android.server.backup.transport.TransportNotRegisteredException e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unable to get management label from transport: " + e.getMessage()));
            return null;
        }
    }

    public void agentConnected(java.lang.String packageName, android.os.IBinder agentBinder) {
        synchronized (this.mAgentConnectLock) {
            if (android.os.Binder.getCallingUid() == 1000) {
                android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "agentConnected pkg=" + packageName + " agent=" + agentBinder));
                this.mUserBackupManagerServiceExt.hookInBindToAgentSynchronous(packageName, android.app.IBackupAgent.Stub.asInterface(agentBinder), false);
            } else {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Non-system process uid=" + android.os.Binder.getCallingUid() + " claiming agent connected"));
            }
            this.mAgentConnectLock.notifyAll();
        }
    }

    public void agentDisconnected(final java.lang.String packageName) {
        synchronized (this.mAgentConnectLock) {
            if (android.os.Binder.getCallingUid() == 1000) {
                this.mUserBackupManagerServiceExt.hookInBindToAgentSynchronous(packageName, null, false);
            } else {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Non-system process uid=" + android.os.Binder.getCallingUid() + " claiming agent disconnected"));
            }
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "agentDisconnected: the backup agent for " + packageName + " died: cancel current operations");
            java.lang.Runnable cancellationRunnable = new java.lang.Runnable() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$agentDisconnected$9(packageName);
                }
            };
            getThreadForAsyncOperation("agent-disconnected", cancellationRunnable).start();
            this.mAgentConnectLock.notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$agentDisconnected$9(java.lang.String packageName) {
        java.util.Iterator<java.lang.Integer> it = this.mOperationStorage.operationTokensForPackage(packageName).iterator();
        while (it.hasNext()) {
            int token = it.next().intValue();
            handleCancel(token, true);
        }
    }

    java.lang.Thread getThreadForAsyncOperation(java.lang.String operationName, java.lang.Runnable operation) {
        return new java.lang.Thread(operation, operationName);
    }

    public void restoreAtInstall(java.lang.String packageName, int token) {
        boolean skip;
        java.lang.String str;
        java.lang.String str2;
        com.android.server.backup.transport.TransportConnection transportConnection;
        android.content.pm.PackageInfo packageInfo;
        com.android.server.backup.utils.BackupManagerMonitorEventSender mBMMEventSender;
        android.os.Bundle bundle;
        boolean skip2;
        com.android.server.backup.internal.OnTaskFinishedListener listener;
        android.os.Message msg;
        android.app.backup.IBackupManagerMonitor monitor;
        com.android.server.backup.utils.BackupEligibilityRules eligibilityRulesForRestoreAtInstall;
        if (android.os.Binder.getCallingUid() != 1000) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Non-system process uid=" + android.os.Binder.getCallingUid() + " attemping install-time restore"));
            return;
        }
        long restoreSet = getAvailableRestoreToken(packageName);
        android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "restoreAtInstall pkg=" + packageName + " token=" + java.lang.Integer.toHexString(token) + " restoreSet=" + java.lang.Long.toHexString(restoreSet)));
        if (restoreSet != 0) {
            skip = false;
        } else {
            skip = true;
        }
        com.android.server.backup.utils.BackupManagerMonitorEventSender mBMMEventSender2 = getBMMEventSender(null);
        android.content.pm.PackageInfo packageInfo2 = getPackageInfoForBMMLogging(packageName);
        final com.android.server.backup.transport.TransportConnection transportConnection2 = this.mTransportManager.getCurrentTransportClient("BMS.restoreAtInstall()");
        if (transportConnection2 == null) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "No transport client"));
            skip = true;
        } else if (com.android.server.backup.Flags.enableIncreasedBmmLoggingForRestoreAtInstall()) {
            try {
                com.android.server.backup.transport.BackupTransportClient transportClient = transportConnection2.connectOrThrow("BMS.restoreAtInstall");
                mBMMEventSender2.setMonitor(transportClient.getBackupManagerMonitor());
            } catch (android.os.RemoteException | com.android.server.backup.transport.TransportNotAvailableException e) {
                mBMMEventSender2.monitorEvent(50, packageInfo2, 1, null);
            }
        }
        if (com.android.server.backup.Flags.enableIncreasedBmmLoggingForRestoreAtInstall()) {
            mBMMEventSender2.monitorEvent(73, packageInfo2, 3, com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra((android.os.Bundle) null, "android.app.backup.extra.OPERATION_TYPE", 1));
        }
        if (!this.mAutoRestore) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Non-restorable state: auto=" + this.mAutoRestore));
            skip = true;
        }
        if (skip) {
            skip2 = skip;
            str = "android.app.backup.extra.OPERATION_TYPE";
            str2 = "BMS.restoreAtInstall()";
            transportConnection = transportConnection2;
            packageInfo = packageInfo2;
            mBMMEventSender = mBMMEventSender2;
            bundle = null;
        } else {
            try {
                this.mWakelock.acquire();
                listener = new com.android.server.backup.internal.OnTaskFinishedListener() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda0
                    @Override // com.android.server.backup.internal.OnTaskFinishedListener
                    public final void onFinished(java.lang.String str3) {
                        this.f$0.lambda$restoreAtInstall$10(transportConnection2, str3);
                    }
                };
                msg = this.mBackupHandler.obtainMessage(3);
                monitor = mBMMEventSender2.getMonitor();
                eligibilityRulesForRestoreAtInstall = getEligibilityRulesForRestoreAtInstall(restoreSet);
                skip2 = skip;
                str = "android.app.backup.extra.OPERATION_TYPE";
                str2 = "BMS.restoreAtInstall()";
                transportConnection = transportConnection2;
                packageInfo = packageInfo2;
                mBMMEventSender = mBMMEventSender2;
                bundle = null;
            } catch (java.lang.Exception e2) {
                e = e2;
                str = "android.app.backup.extra.OPERATION_TYPE";
                str2 = "BMS.restoreAtInstall()";
                transportConnection = transportConnection2;
                packageInfo = packageInfo2;
                mBMMEventSender = mBMMEventSender2;
                bundle = null;
            }
            try {
                msg.obj = com.android.server.backup.params.RestoreParams.createForRestoreAtInstall(transportConnection2, null, monitor, restoreSet, packageName, token, listener, eligibilityRulesForRestoreAtInstall);
                this.mBackupHandler.sendMessage(msg);
            } catch (java.lang.Exception e3) {
                e = e3;
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Unable to contact transport: " + e.getMessage()));
                skip2 = true;
            }
        }
        if (skip2) {
            if (com.android.server.backup.Flags.enableIncreasedBmmLoggingForRestoreAtInstall()) {
                mBMMEventSender.monitorEvent(74, packageInfo, 3, com.android.server.backup.utils.BackupManagerMonitorEventSender.putMonitoringExtra(bundle, str, 1));
            }
            com.android.server.backup.transport.TransportConnection transportConnection3 = transportConnection;
            if (transportConnection3 != null) {
                this.mTransportManager.disposeOfTransportClient(transportConnection3, str2);
            }
            android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Finishing install immediately"));
            try {
                try {
                    this.mPackageManagerBinder.finishPackageInstall(token, false);
                } catch (android.os.RemoteException e4) {
                }
            } catch (android.os.RemoteException e5) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restoreAtInstall$10(com.android.server.backup.transport.TransportConnection transportConnection, java.lang.String caller) {
        this.mTransportManager.disposeOfTransportClient(transportConnection, caller);
        this.mWakelock.release();
    }

    private android.content.pm.PackageInfo getPackageInfoForBMMLogging(java.lang.String packageName) {
        android.content.pm.PackageInfo packageInfo = new android.content.pm.PackageInfo();
        packageInfo.packageName = packageName;
        return packageInfo;
    }

    public android.app.backup.IRestoreSession beginRestoreSession(java.lang.String packageName, java.lang.String transport) {
        android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "beginRestoreSession: pkg=" + packageName + " transport=" + transport));
        boolean needPermission = true;
        if (transport == null) {
            transport = this.mTransportManager.getCurrentTransportName();
            if (packageName != null) {
                try {
                    android.content.pm.PackageInfo app = this.mPackageManager.getPackageInfoAsUser(packageName, 0, this.mUserId);
                    if (app.applicationInfo.uid == android.os.Binder.getCallingUid()) {
                        needPermission = false;
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Asked to restore nonexistent pkg " + packageName));
                    throw new java.lang.IllegalArgumentException("Package " + packageName + " not found");
                }
            }
        }
        if (needPermission) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "beginRestoreSession");
        } else {
            android.util.Slog.d(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "restoring self on current transport; no permission needed"));
        }
        com.android.server.backup.transport.TransportConnection transportConnection = null;
        try {
            try {
                transportConnection = this.mTransportManager.getTransportClientOrThrow(transport, "BMS.beginRestoreSession");
                int backupDestination = getBackupDestinationFromTransport(transportConnection);
                if (transportConnection != null) {
                    this.mTransportManager.disposeOfTransportClient(transportConnection, "BMS.beginRestoreSession");
                }
                synchronized (this) {
                    if (this.mActiveRestoreSession != null) {
                        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Restore session requested but one already active"));
                        return null;
                    }
                    if (this.mBackupRunning) {
                        android.util.Slog.i(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Restore session requested but currently running backups"));
                        return null;
                    }
                    this.mActiveRestoreSession = new com.android.server.backup.restore.ActiveRestoreSession(this, packageName, transport, getEligibilityRulesForOperation(backupDestination));
                    this.mBackupHandler.sendEmptyMessageDelayed(8, this.mAgentTimeoutParameters.getRestoreSessionTimeoutMillis());
                    return this.mActiveRestoreSession;
                }
            } catch (android.os.RemoteException | com.android.server.backup.transport.TransportNotAvailableException | com.android.server.backup.transport.TransportNotRegisteredException e2) {
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Failed to get operation type from transport: " + e2);
                if (transportConnection != null) {
                    this.mTransportManager.disposeOfTransportClient(transportConnection, "BMS.beginRestoreSession");
                }
                return null;
            }
        } catch (java.lang.Throwable th) {
            if (transportConnection != null) {
                this.mTransportManager.disposeOfTransportClient(transportConnection, "BMS.beginRestoreSession");
            }
            throw th;
        }
    }

    public void clearRestoreSession(com.android.server.backup.restore.ActiveRestoreSession currentSession) {
        synchronized (this) {
            if (currentSession != this.mActiveRestoreSession) {
                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "ending non-current restore session"));
            } else {
                android.util.Slog.v(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Clearing restore session and halting timeout"));
                this.mActiveRestoreSession = null;
                this.mBackupHandler.removeMessages(8);
            }
        }
    }

    public void opComplete(int token, final long result) {
        this.mOperationStorage.onOperationComplete(token, result, new java.util.function.Consumer() { // from class: com.android.server.backup.UserBackupManagerService$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$opComplete$11(result, (com.android.server.backup.BackupRestoreTask) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$opComplete$11(long result, com.android.server.backup.BackupRestoreTask callback) {
        android.util.Pair<com.android.server.backup.BackupRestoreTask, java.lang.Long> callbackAndResult = android.util.Pair.create(callback, java.lang.Long.valueOf(result));
        android.os.Message msg = this.mBackupHandler.obtainMessage(21, callbackAndResult);
        this.mBackupHandler.sendMessage(msg);
    }

    public boolean isAppEligibleForBackup(java.lang.String packageName) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "isAppEligibleForBackup");
        long oldToken = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.backup.transport.TransportConnection transportConnection = this.mTransportManager.getCurrentTransportClient("BMS.isAppEligibleForBackup");
            boolean eligible = this.mScheduledBackupEligibility.appIsRunningAndEligibleForBackupWithTransport(transportConnection, packageName);
            if (transportConnection != null) {
                this.mTransportManager.disposeOfTransportClient(transportConnection, "BMS.isAppEligibleForBackup");
            }
            return eligible;
        } finally {
            android.os.Binder.restoreCallingIdentity(oldToken);
        }
    }

    public java.lang.String[] filterAppsEligibleForBackup(java.lang.String[] packages) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "filterAppsEligibleForBackup");
        long oldToken = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.backup.transport.TransportConnection transportConnection = this.mTransportManager.getCurrentTransportClient("BMS.filterAppsEligibleForBackup");
            java.util.List<java.lang.String> eligibleApps = new java.util.ArrayList<>();
            for (java.lang.String packageName : packages) {
                if (this.mScheduledBackupEligibility.appIsRunningAndEligibleForBackupWithTransport(transportConnection, packageName)) {
                    eligibleApps.add(packageName);
                }
            }
            if (transportConnection != null) {
                this.mTransportManager.disposeOfTransportClient(transportConnection, "BMS.filterAppsEligibleForBackup");
            }
            return (java.lang.String[]) eligibleApps.toArray(new java.lang.String[0]);
        } finally {
            android.os.Binder.restoreCallingIdentity(oldToken);
        }
    }

    public com.android.server.backup.utils.BackupEligibilityRules getEligibilityRulesForOperation(int backupDestination) {
        return getEligibilityRules(this.mPackageManager, this.mUserId, this.mContext, backupDestination);
    }

    private static com.android.server.backup.utils.BackupEligibilityRules getEligibilityRules(android.content.pm.PackageManager packageManager, int userId, android.content.Context context, int backupDestination) {
        return new com.android.server.backup.utils.BackupEligibilityRules(packageManager, (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class), userId, context, backupDestination);
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        long identityToken = android.os.Binder.clearCallingIdentity();
        if (args != null) {
            try {
                for (java.lang.String arg : args) {
                    if ("agents".startsWith(arg)) {
                        dumpAgents(pw);
                        return;
                    } else if ("transportclients".equals(arg.toLowerCase())) {
                        this.mTransportManager.dumpTransportClients(pw);
                        return;
                    } else {
                        if ("transportstats".equals(arg.toLowerCase())) {
                            this.mTransportManager.dumpTransportStats(pw);
                            return;
                        }
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identityToken);
            }
        }
        dumpInternal(pw);
        dumpBMMEvents(pw);
    }

    private void dumpAgents(java.io.PrintWriter pw) {
        java.util.List<android.content.pm.PackageInfo> agentPackages = allAgentPackages();
        pw.println("Defined backup agents:");
        for (android.content.pm.PackageInfo pkg : agentPackages) {
            pw.print("  ");
            pw.print(pkg.packageName);
            pw.println(':');
            pw.print("      ");
            pw.println(pkg.applicationInfo.backupAgentName);
        }
    }

    private void dumpBMMEvents(java.io.PrintWriter pw) {
        com.android.server.backup.utils.BackupManagerMonitorDumpsysUtils bm = new com.android.server.backup.utils.BackupManagerMonitorDumpsysUtils();
        if (bm.deleteExpiredBMMEvents()) {
            pw.println("BACKUP MANAGER MONITOR EVENTS HAVE EXPIRED");
            return;
        }
        java.io.File events = bm.getBMMEventsFile();
        if (events.length() == 0) {
            pw.println("NO BACKUP MANAGER MONITOR EVENTS");
            return;
        }
        if (bm.isFileLargerThanSizeLimit(events)) {
            pw.println("BACKUP MANAGER MONITOR EVENTS FILE OVER SIZE LIMIT - future events will not be recorded");
        }
        pw.println("START OF BACKUP MANAGER MONITOR EVENTS");
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(events));
            while (true) {
                try {
                    java.lang.String line = reader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        pw.println(line);
                    }
                } finally {
                }
            }
            reader.close();
        } catch (java.io.IOException e) {
            android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "IO Exception when reading BMM events from file: " + e);
            pw.println("IO Exception when reading BMM events from file");
        }
        pw.println("END OF BACKUP MANAGER MONITOR EVENTS");
    }

    @dalvik.annotation.optimization.NeverCompile
    private void dumpInternal(java.io.PrintWriter pw) {
        int i;
        java.lang.String userPrefix = this.mUserId == 0 ? "" : "User " + this.mUserId + ":";
        synchronized (this.mQueueLock) {
            pw.println(userPrefix + "Backup Manager is " + (this.mEnabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED) + " / " + (!this.mSetupComplete ? "not " : "") + "setup complete / " + (this.mPendingInits.size() == 0 ? "not " : "") + "pending init");
            pw.println("Auto-restore is " + (this.mAutoRestore ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED));
            if (this.mBackupRunning) {
                pw.println("Backup currently running");
            }
            pw.println(isBackupOperationInProgress() ? "Backup in progress" : "No backups running");
            pw.println("Framework scheduling is " + (isFrameworkSchedulingEnabled() ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED));
            pw.println("Last backup pass started: " + this.mLastBackupPass + " (now = " + java.lang.System.currentTimeMillis() + ')');
            pw.println("  next scheduled: " + com.android.server.backup.KeyValueBackupJob.nextScheduled(this.mUserId));
            pw.println(userPrefix + "Transport whitelist:");
            for (android.content.ComponentName transport : this.mTransportManager.getTransportWhitelist()) {
                pw.print("    ");
                pw.println(transport.flattenToShortString());
            }
            pw.println(userPrefix + "Available transports:");
            java.lang.String[] transports = listAllTransports();
            if (transports != null) {
                int length = transports.length;
                int i2 = 0;
                while (i2 < length) {
                    java.lang.String t = transports[i2];
                    pw.println((t.equals(this.mTransportManager.getCurrentTransportName()) ? "  * " : "    ") + t);
                    try {
                        java.io.File dir = new java.io.File(this.mBaseStateDir, this.mTransportManager.getTransportDirName(t));
                        pw.println("       destination: " + this.mTransportManager.getTransportCurrentDestinationString(t));
                        pw.println("       intent: " + this.mTransportManager.getTransportConfigurationIntent(t));
                        java.io.File[] fileArrListFiles = dir.listFiles();
                        int length2 = fileArrListFiles.length;
                        int i3 = 0;
                        while (i3 < length2) {
                            java.io.File f = fileArrListFiles[i3];
                            i = i2;
                            try {
                                pw.println("       " + f.getName() + " - " + f.length() + " state bytes");
                                i3++;
                                i2 = i;
                            } catch (java.lang.Exception e) {
                                e = e;
                                android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, addUserIdToLogMessage(this.mUserId, "Error in transport"), e);
                                pw.println("        Error: " + e);
                                i2 = i + 1;
                            }
                        }
                        i = i2;
                    } catch (java.lang.Exception e2) {
                        e = e2;
                        i = i2;
                    }
                    i2 = i + 1;
                }
            }
            this.mTransportManager.dumpTransportClients(pw);
            pw.println(userPrefix + "Pending init: " + this.mPendingInits.size());
            for (java.lang.String s : this.mPendingInits) {
                pw.println("    " + s);
            }
            pw.print(userPrefix + "Ancestral: ");
            pw.println(java.lang.Long.toHexString(this.mAncestralToken));
            pw.print(userPrefix + "Current:   ");
            pw.println(java.lang.Long.toHexString(this.mCurrentToken));
            int numPackages = this.mBackupParticipants.size();
            pw.println(userPrefix + "Participants:");
            for (int i4 = 0; i4 < numPackages; i4++) {
                int uid = this.mBackupParticipants.keyAt(i4);
                pw.print("  uid: ");
                pw.println(uid);
                java.util.HashSet<java.lang.String> participants = this.mBackupParticipants.valueAt(i4);
                for (java.lang.String app : participants) {
                    pw.println("    " + app);
                }
            }
            pw.println(userPrefix + "Ancestral packages: " + (this.mAncestralPackages == null ? "none" : java.lang.Integer.valueOf(this.mAncestralPackages.size())));
            if (this.mAncestralPackages != null) {
                for (java.lang.String pkg : this.mAncestralPackages) {
                    pw.println("    " + pkg);
                }
            }
            java.util.Set<java.lang.String> processedPackages = this.mProcessedPackagesJournal.getPackagesCopy();
            pw.println(userPrefix + "Ever backed up: " + processedPackages.size());
            for (java.lang.String pkg2 : processedPackages) {
                pw.println("    " + pkg2);
            }
            pw.println(userPrefix + "Pending key/value backup: " + this.mPendingBackups.size());
            for (com.android.server.backup.keyvalue.BackupRequest req : this.mPendingBackups.values()) {
                pw.println("    " + req);
            }
            pw.println(userPrefix + "Full backup queue:" + this.mFullBackupQueue.size());
            for (com.android.server.backup.fullbackup.FullBackupEntry entry : this.mFullBackupQueue) {
                pw.print("    ");
                pw.print(entry.lastBackup);
                pw.print(" : ");
                pw.println(entry.packageName);
            }
            pw.println(userPrefix + "Agent timeouts:");
            pw.println("    KvBackupAgentTimeoutMillis: " + this.mAgentTimeoutParameters.getKvBackupAgentTimeoutMillis());
            pw.println("    FullBackupAgentTimeoutMillis: " + this.mAgentTimeoutParameters.getFullBackupAgentTimeoutMillis());
            pw.println("    SharedBackupAgentTimeoutMillis: " + this.mAgentTimeoutParameters.getSharedBackupAgentTimeoutMillis());
            pw.println("    RestoreAgentTimeoutMillis (system): " + this.mAgentTimeoutParameters.getRestoreAgentTimeoutMillis(9999));
            pw.println("    RestoreAgentTimeoutMillis: " + this.mAgentTimeoutParameters.getRestoreAgentTimeoutMillis(10000));
            pw.println("    RestoreAgentFinishedTimeoutMillis: " + this.mAgentTimeoutParameters.getRestoreAgentFinishedTimeoutMillis());
            pw.println("    QuotaExceededTimeoutMillis: " + this.mAgentTimeoutParameters.getQuotaExceededTimeoutMillis());
        }
    }

    int getBackupDestinationFromTransport(com.android.server.backup.transport.TransportConnection transportConnection) throws com.android.server.backup.transport.TransportNotAvailableException, android.os.RemoteException {
        if (!shouldUseNewBackupEligibilityRules()) {
            return 0;
        }
        long oldCallingId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.backup.transport.BackupTransportClient transport = transportConnection.connectOrThrow("BMS.getBackupDestinationFromTransport");
            if ((transport.getTransportFlags() & 2) == 0) {
                return 0;
            }
            android.os.Binder.restoreCallingIdentity(oldCallingId);
            return 1;
        } finally {
            android.os.Binder.restoreCallingIdentity(oldCallingId);
        }
    }

    boolean shouldUseNewBackupEligibilityRules() {
        return android.util.FeatureFlagUtils.isEnabled(this.mContext, "settings_use_new_backup_eligibility_rules");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String addUserIdToLogMessage(int userId, java.lang.String message) {
        return "[UserID:" + userId + "] " + message;
    }

    public android.app.backup.IBackupManager getBackupManagerBinder() {
        return this.mBackupManagerBinder;
    }

    public com.android.server.backup.IUserBackupManagerServiceWrapper getWrapper() {
        return this.mUserBackupManagerWrapper;
    }

    private class UserBackupManagerServiceWrapper implements com.android.server.backup.IUserBackupManagerServiceWrapper {
        private UserBackupManagerServiceWrapper() {
        }

        @Override // com.android.server.backup.IUserBackupManagerServiceWrapper
        public com.android.server.backup.IUserBackupManagerServiceExt.IStaticExt getStaticExtImpl() {
            return com.android.server.backup.UserBackupManagerService.sStaticUserBackupManagerServiceExt;
        }

        @Override // com.android.server.backup.IUserBackupManagerServiceWrapper
        public com.android.server.backup.IUserBackupManagerServiceExt getExtImpl() {
            return com.android.server.backup.UserBackupManagerService.this.mUserBackupManagerServiceExt;
        }
    }
}
