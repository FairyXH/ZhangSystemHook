package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class StorageManagerService extends android.os.storage.IStorageManager.Stub implements com.android.server.Watchdog.Monitor, com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver {
    private static final java.lang.String ANDROID_VOLD_APP_DATA_ISOLATION_ENABLED_PROPERTY = "persist.sys.vold_app_data_isolation_enabled";
    private static final java.lang.String ANR_DELAY_MILLIS_DEVICE_CONFIG_KEY = "anr_delay_millis";
    private static final java.lang.String ANR_DELAY_NOTIFY_EXTERNAL_STORAGE_SERVICE_DEVICE_CONFIG_KEY = "anr_delay_notify_external_storage_service";
    private static final java.lang.String ATTR_CREATED_MILLIS = "createdMillis";
    private static final java.lang.String ATTR_FS_UUID = "fsUuid";
    private static final java.lang.String ATTR_LAST_BENCH_MILLIS = "lastBenchMillis";
    private static final java.lang.String ATTR_LAST_SEEN_MILLIS = "lastSeenMillis";
    private static final java.lang.String ATTR_LAST_TRIM_MILLIS = "lastTrimMillis";
    private static final java.lang.String ATTR_NICKNAME = "nickname";
    private static final java.lang.String ATTR_PART_GUID = "partGuid";
    private static final java.lang.String ATTR_PRIMARY_STORAGE_UUID = "primaryStorageUuid";
    private static final java.lang.String ATTR_TYPE = "type";
    private static final java.lang.String ATTR_USER_FLAGS = "userFlags";
    private static final java.lang.String ATTR_VERSION = "version";
    private static final java.lang.String AUTOSAVE_SDCARD_PACKAGE_NAME = "com.coloros.movetosdcard";
    private static long BROADCAST_TIME = 0;
    private static final boolean DEBUG_OBB = false;
    private static final boolean DEFAULT_CHARGING_REQUIRED = true;
    private static final float DEFAULT_DIRTY_RECLAIM_RATE = 0.5f;
    private static final boolean DEFAULT_FUSE_ENABLED = true;
    private static final int DEFAULT_LIFETIME_PERCENT_THRESHOLD = 70;
    private static final float DEFAULT_LOW_BATTERY_LEVEL = 20.0f;
    private static final int DEFAULT_MIN_GC_SLEEPTIME = 10000;
    private static final int DEFAULT_MIN_SEGMENTS_THRESHOLD = 512;
    private static final float DEFAULT_SEGMENT_RECLAIM_WEIGHT = 1.0f;
    private static final boolean DEFAULT_SMART_IDLE_MAINT_ENABLED = false;
    private static final int DEFAULT_SMART_IDLE_MAINT_PERIOD = 60;
    private static final int DEFAULT_TARGET_DIRTY_RATIO = 80;
    public static final int FAILED_MOUNT_RESET_TIMEOUT_SECONDS = 10;
    private static final java.lang.String FUSE_ENABLED = "fuse_enabled";
    private static final int H_ABORT_IDLE_MAINT = 12;
    private static final int H_BOOT_COMPLETED = 13;
    private static final int H_CLOUD_MEDIA_PROVIDER_CHANGED = 16;
    private static final int H_COMPLETE_UNLOCK_USER = 14;
    private static final int H_DAEMON_CONNECTED = 2;
    private static final int H_FSTRIM = 4;
    private static final int H_INTERNAL_BROADCAST = 7;
    private static final int H_PARTITION_FORGET = 9;
    private static final int H_REMOUNT_VOLUMES_ON_MOVE = 18;
    private static final int H_RESET = 10;
    private static final int H_RUN_IDLE_MAINT = 11;
    private static final int H_SECURE_KEYGUARD_STATE_CHANGED = 17;
    private static final int H_SHUTDOWN = 3;
    private static final int H_SYSTEM_READY = 1;
    private static final int H_VOLUME_BROADCAST = 6;
    private static final int H_VOLUME_MOUNT = 5;
    private static final int H_VOLUME_STATE_CHANGED = 15;
    private static final int H_VOLUME_UNMOUNT = 8;
    private static final java.lang.String ISOLATED_STORAGE_ENABLED = "isolated_storage_enabled";
    private static final java.lang.String LAST_FSTRIM_FILE = "last-fstrim";
    private static final int MAX_PERIOD_WRITE_RECORD = 4320;
    private static final int MAX_SMART_IDLE_MAINT_PERIOD = 1440;
    private static final int MIN_SMART_IDLE_MAINT_PERIOD = 10;
    private static final int MOVE_STATUS_COPY_FINISHED = 82;
    private static final java.lang.String NATIVE_SERVICE_KEY = "oplus.defragservice.status";
    private static final int OBB_FLUSH_MOUNT_STATE = 2;
    private static final int OBB_RUN_ACTION = 1;
    private static final int PARTITION_OPERATION_WATCHDOG_TIMEOUT_MS = 180000;
    private static final int SLOW_OPERATION_WATCHDOG_TIMEOUT_MS = 20000;
    private static final java.lang.String TAG_STORAGE_BENCHMARK = "storage_benchmark";
    private static final java.lang.String TAG_STORAGE_TRIM = "storage_trim";
    private static final java.lang.String TAG_VOLUME = "volume";
    private static final java.lang.String TAG_VOLUMES = "volumes";
    private static final int VERSION_ADD_PRIMARY = 2;
    private static final int VERSION_FIX_PRIMARY = 3;
    private static final int VERSION_INIT = 1;
    private static final boolean WATCHDOG_ENABLE = true;
    private static final java.lang.String ZRAM_ENABLED_PROPERTY = "persist.sys.zram_enabled";
    public static java.lang.String sMediaStoreAuthorityProcessName;
    private boolean enableDefrag;
    private boolean enableScan;
    private boolean finishDefrag;
    private boolean finishScan;
    private final com.android.server.storage.IOplusStorageAllFileAccessManager mAllFileAccessManager;
    private final com.android.server.StorageManagerService.Callbacks mCallbacks;
    private volatile boolean mChargingRequired;
    protected final android.content.Context mContext;
    private volatile float mDirtyReclaimRate;
    protected final android.os.Handler mHandler;
    private com.android.internal.app.IAppOpsService mIAppOpsService;
    private android.content.pm.IPackageManager mIPackageManager;
    private final com.android.server.pm.Installer mInstaller;
    private long mLastMaintenance;
    private final java.io.File mLastMaintenanceFile;
    private volatile int mLifetimePercentThreshold;
    private volatile float mLowBatteryLevel;
    private volatile int mMaxWriteRecords;
    private volatile int mMinGCSleepTime;
    private volatile int mMinSegmentsThreshold;
    private android.content.pm.IPackageMoveObserver mMoveCallback;
    private java.lang.String mMoveTargetUuid;
    private final com.android.server.StorageManagerService.ObbActionHandler mObbActionHandler;
    private com.android.server.pm.IOplusPackageManagerExt mOplusPmExt;
    private com.android.server.IOplusStorageManagerFeature mOplusStorageFeature;
    private volatile boolean mPassedLifetimeThresh;
    private android.content.pm.PackageManagerInternal mPmInternal;
    private java.lang.String mPrimaryStorageUuid;
    private volatile float mSegmentReclaimWeight;
    private final android.util.AtomicFile mSettingsFile;
    private final com.android.server.storage.StorageSessionController mStorageSessionController;
    private volatile int[] mStorageWriteRecords;
    private volatile android.os.IStoraged mStoraged;
    private volatile int mTargetDirtyRatio;
    private volatile android.os.IVold mVold;
    private final boolean mVoldAppDataIsolationEnabled;
    private final android.util.AtomicFile mWriteRecordFile;
    static com.android.server.StorageManagerService sSelf = null;
    private static boolean IsRegisterOplusStorageManagerFeatureBoradcast = false;
    private static final java.lang.String TAG = "StorageManagerService";
    private static final boolean LOCAL_LOGV = android.util.Log.isLoggable(TAG, 2);
    static volatile int sSmartIdleMaintPeriod = 60;
    public static final java.util.regex.Pattern KNOWN_APP_DIR_PATHS = java.util.regex.Pattern.compile("(?i)(^/storage/[^/]+/(?:([0-9]+)/)?Android/(?:data|media|obb|sandbox)/)([^/]+)(/.*)?");
    private final java.util.Set<java.lang.Integer> mFuseMountedUser = new android.util.ArraySet();
    private final java.util.Set<java.lang.Integer> mCeStoragePreparedUsers = new android.util.ArraySet();
    private volatile long mInternalStorageSize = 0;
    private volatile boolean mNeedGC = true;
    protected final java.lang.Object mLock = com.android.server.LockGuard.installNewLock(4);
    private com.android.server.StorageManagerService.WatchedUnlockedUsers mCeUnlockedUsers = new com.android.server.StorageManagerService.WatchedUnlockedUsers();
    private int[] mSystemUnlockedUsers = libcore.util.EmptyArray.INT;
    private android.util.ArrayMap<java.lang.String, android.os.storage.DiskInfo> mDisks = new android.util.ArrayMap<>();
    protected final android.util.ArrayMap<java.lang.String, android.os.storage.VolumeInfo> mVolumes = new android.util.ArrayMap<>();
    private android.util.ArrayMap<java.lang.String, android.os.storage.VolumeRecord> mRecords = new android.util.ArrayMap<>();
    private android.util.ArrayMap<java.lang.String, java.util.concurrent.CountDownLatch> mDiskScanLatches = new android.util.ArrayMap<>();
    private final android.util.SparseArray<java.lang.String> mCloudMediaProviders = new android.util.SparseArray<>();
    private volatile int mMediaStoreAuthorityAppId = -1;
    private volatile int mDownloadsAuthorityAppId = -1;
    private volatile int mExternalStorageAuthorityAppId = -1;
    protected volatile int mCurrentUserId = 0;
    private volatile boolean mRemountCurrentUserVolumesOnUnlock = false;
    private final java.lang.Object mAppFuseLock = new java.lang.Object();
    private int mNextAppFuseName = 0;
    private com.android.server.storage.AppFuseBridge mAppFuseBridge = null;
    private final android.util.SparseIntArray mUserSharesMediaWith = new android.util.SparseIntArray();
    private volatile oplus.os.IDefrag mDefrag = null;
    private volatile boolean mBootCompleted = false;
    private volatile boolean mDaemonConnected = false;
    private volatile boolean mSecureKeyguardShowing = true;
    private final java.util.Map<android.os.IBinder, java.util.List<com.android.server.StorageManagerService.ObbState>> mObbMounts = new java.util.HashMap();
    private final java.util.Map<java.lang.String, com.android.server.StorageManagerService.ObbState> mObbPathToStateMap = new java.util.HashMap();
    private final com.android.server.StorageManagerService.StorageManagerInternalImpl mStorageManagerInternal = new com.android.server.StorageManagerService.StorageManagerInternalImpl();
    private final boolean mIsFuseEnabled = true;
    private final java.util.Set<java.lang.Integer> mUidsWithLegacyExternalStorage = new android.util.ArraySet();
    private final android.util.SparseArray<com.android.internal.content.PackageMonitor> mPackageMonitorsForUser = new android.util.SparseArray<>();
    private final java.util.concurrent.CopyOnWriteArrayList<android.os.storage.ICeStorageLockEventListener> mCeStorageEventCallbacks = new java.util.concurrent.CopyOnWriteArrayList<>();
    private android.content.BroadcastReceiver mUserReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.StorageManagerService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            int userId = intent.getIntExtra("android.intent.extra.user_handle", -1);
            com.android.internal.util.Preconditions.checkArgument(userId >= 0);
            android.util.Slog.i(com.android.server.StorageManagerService.TAG, "Received broadcast: " + action + " for user: " + userId);
            try {
                if ("android.intent.action.USER_ADDED".equals(action)) {
                    android.os.UserManager um = (android.os.UserManager) com.android.server.StorageManagerService.this.mContext.getSystemService(android.os.UserManager.class);
                    int userSerialNumber = um.getUserSerialNumber(userId);
                    android.content.pm.UserInfo userInfo = um.getUserInfo(userId);
                    if (userInfo.isCloneProfile()) {
                        com.android.server.StorageManagerService.this.mVold.onUserAdded(userId, userSerialNumber, userInfo.profileGroupId);
                    } else {
                        com.android.server.StorageManagerService.this.mVold.onUserAdded(userId, userSerialNumber, -1);
                    }
                    android.util.Slog.i(com.android.server.StorageManagerService.TAG, "User added complete: " + userId);
                    return;
                }
                if ("android.intent.action.USER_REMOVED".equals(action)) {
                    synchronized (com.android.server.StorageManagerService.this.mLock) {
                        int size = com.android.server.StorageManagerService.this.mVolumes.size();
                        for (int i = 0; i < size; i++) {
                            android.os.storage.VolumeInfo vol = com.android.server.StorageManagerService.this.mVolumes.valueAt(i);
                            if (vol.mountUserId == userId) {
                                vol.mountUserId = -10000;
                                com.android.server.StorageManagerService.this.mHandler.obtainMessage(8, vol).sendToTarget();
                            }
                        }
                    }
                    com.android.server.StorageManagerService.this.mVold.onUserRemoved(userId);
                    android.util.Slog.i(com.android.server.StorageManagerService.TAG, "User removal complete: " + userId);
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.wtf(com.android.server.StorageManagerService.TAG, "Exception during user processing: " + userId, e);
            }
        }
    };
    private final android.os.IVoldListener mListener = new android.os.IVoldListener.Stub() { // from class: com.android.server.StorageManagerService.4
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:6:0x0013  */
        @Override // android.os.IVoldListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onDiskCreated(java.lang.String r5, int r6) {
            /*
                r4 = this;
                com.android.server.StorageManagerService r0 = com.android.server.StorageManagerService.this
                java.lang.Object r0 = r0.mLock
                monitor-enter(r0)
                java.lang.String r1 = "persist.sys.adoptable"
                java.lang.String r1 = android.os.SystemProperties.get(r1)     // Catch: java.lang.Throwable -> L46
                int r2 = r1.hashCode()     // Catch: java.lang.Throwable -> L46
                switch(r2) {
                    case 464944051: goto L1f;
                    case 1528363547: goto L14;
                    default: goto L13;
                }     // Catch: java.lang.Throwable -> L46
            L13:
                goto L2a
            L14:
                java.lang.String r2 = "force_off"
                boolean r2 = r1.equals(r2)     // Catch: java.lang.Throwable -> L46
                if (r2 == 0) goto L13
                r2 = 1
                goto L2b
            L1f:
                java.lang.String r2 = "force_on"
                boolean r2 = r1.equals(r2)     // Catch: java.lang.Throwable -> L46
                if (r2 == 0) goto L13
                r2 = 0
                goto L2b
            L2a:
                r2 = -1
            L2b:
                switch(r2) {
                    case 0: goto L32;
                    case 1: goto L2f;
                    default: goto L2e;
                }     // Catch: java.lang.Throwable -> L46
            L2e:
                goto L35
            L2f:
                r6 = r6 & (-2)
                goto L35
            L32:
                r6 = r6 | 1
            L35:
                com.android.server.StorageManagerService r2 = com.android.server.StorageManagerService.this     // Catch: java.lang.Throwable -> L46
                android.util.ArrayMap r2 = com.android.server.StorageManagerService.m351$$Nest$fgetmDisks(r2)     // Catch: java.lang.Throwable -> L46
                android.os.storage.DiskInfo r3 = new android.os.storage.DiskInfo     // Catch: java.lang.Throwable -> L46
                r3.<init>(r5, r6)     // Catch: java.lang.Throwable -> L46
                r2.put(r5, r3)     // Catch: java.lang.Throwable -> L46
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L46
                return
            L46:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L46
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.StorageManagerService.AnonymousClass4.onDiskCreated(java.lang.String, int):void");
        }

        @Override // android.os.IVoldListener
        public void onDiskScanned(java.lang.String diskId) {
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                android.os.storage.DiskInfo disk = (android.os.storage.DiskInfo) com.android.server.StorageManagerService.this.mDisks.get(diskId);
                if (disk != null) {
                    com.android.server.StorageManagerService.this.onDiskScannedLocked(disk);
                }
            }
        }

        @Override // android.os.IVoldListener
        public void onDiskMetadataChanged(java.lang.String diskId, long sizeBytes, java.lang.String label, java.lang.String sysPath) {
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                android.os.storage.DiskInfo disk = (android.os.storage.DiskInfo) com.android.server.StorageManagerService.this.mDisks.get(diskId);
                if (disk != null) {
                    disk.size = sizeBytes;
                    disk.label = label;
                    disk.sysPath = sysPath;
                }
            }
        }

        @Override // android.os.IVoldListener
        public void onDiskDestroyed(java.lang.String diskId) {
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                android.os.storage.DiskInfo disk = (android.os.storage.DiskInfo) com.android.server.StorageManagerService.this.mDisks.remove(diskId);
                if (disk != null) {
                    com.android.server.StorageManagerService.this.mCallbacks.notifyDiskDestroyed(disk);
                }
            }
        }

        @Override // android.os.IVoldListener
        public void onVolumeCreated(java.lang.String volId, int type, java.lang.String diskId, java.lang.String partGuid, int userId) {
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                android.os.storage.DiskInfo disk = (android.os.storage.DiskInfo) com.android.server.StorageManagerService.this.mDisks.get(diskId);
                android.os.storage.VolumeInfo vol = new android.os.storage.VolumeInfo(volId, type, disk, partGuid);
                vol.mountUserId = userId;
                com.android.server.StorageManagerService.this.mVolumes.put(volId, vol);
                if (type == 0) {
                    com.android.server.StorageManagerService.this.mOplusStorageFeature.onVolumeCheckingLocked(vol, com.android.server.StorageManagerService.this.mCurrentUserId);
                } else {
                    com.android.server.StorageManagerService.this.onVolumeCreatedLocked(vol);
                }
            }
        }

        @Override // android.os.IVoldListener
        public void onVolumeStateChanged(java.lang.String volId, int newState, int userId) {
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                android.os.storage.VolumeInfo vol = com.android.server.StorageManagerService.this.mVolumes.get(volId);
                if (vol != null) {
                    int oldState = vol.state;
                    if (com.android.server.StorageManagerService.this.mOplusStorageFeature.changeVolumeReadOnlyStateLocked(vol, newState, com.android.server.StorageManagerService.this.mSystemUnlockedUsers.length)) {
                        return;
                    }
                    vol.state = newState;
                    android.os.storage.VolumeInfo vInfo = new android.os.storage.VolumeInfo(vol);
                    vInfo.mountUserId = userId;
                    com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                    args.arg1 = vInfo;
                    args.argi1 = oldState;
                    args.argi2 = newState;
                    com.android.server.StorageManagerService.this.mHandler.obtainMessage(15, args).sendToTarget();
                    com.android.server.StorageManagerService.this.onVolumeStateChangedLocked(vInfo, newState);
                }
            }
        }

        @Override // android.os.IVoldListener
        public void onVolumeMetadataChanged(java.lang.String volId, java.lang.String fsType, java.lang.String fsUuid, java.lang.String fsLabel) {
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                android.os.storage.VolumeInfo vol = com.android.server.StorageManagerService.this.mVolumes.get(volId);
                if (vol != null) {
                    vol.fsType = fsType;
                    vol.fsUuid = fsUuid;
                    vol.fsLabel = fsLabel;
                }
            }
        }

        @Override // android.os.IVoldListener
        public void onVolumePathChanged(java.lang.String volId, java.lang.String path) {
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                android.os.storage.VolumeInfo vol = com.android.server.StorageManagerService.this.mVolumes.get(volId);
                if (vol != null) {
                    vol.path = path;
                }
            }
        }

        @Override // android.os.IVoldListener
        public void onVolumeInternalPathChanged(java.lang.String volId, java.lang.String internalPath) {
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                android.os.storage.VolumeInfo vol = com.android.server.StorageManagerService.this.mVolumes.get(volId);
                if (vol != null) {
                    vol.internalPath = internalPath;
                }
            }
        }

        @Override // android.os.IVoldListener
        public void onVolumeDestroyed(java.lang.String volId) {
            android.os.storage.VolumeInfo vol;
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                vol = com.android.server.StorageManagerService.this.mVolumes.remove(volId);
            }
            if (vol != null) {
                com.android.server.StorageManagerService.this.mStorageSessionController.onVolumeRemove(vol);
                try {
                    if (vol.type == 1) {
                        com.android.server.StorageManagerService.this.mInstaller.onPrivateVolumeRemoved(vol.getFsUuid());
                    }
                } catch (com.android.server.pm.Installer.InstallerException e) {
                    android.util.Slog.i(com.android.server.StorageManagerService.TAG, "Failed when private volume unmounted " + vol, e);
                }
            }
        }

        @Override // android.os.IVoldListener
        public void onDiskStateChanged(java.lang.String diskId) {
            android.util.Slog.d(com.android.server.StorageManagerService.TAG, "onDiskStateChanged, diskId:" + diskId);
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                android.os.storage.DiskInfo disk = (android.os.storage.DiskInfo) com.android.server.StorageManagerService.this.mDisks.get(diskId);
                com.android.server.StorageManagerService.this.mOplusStorageFeature.onDiskStateChangedLocked(disk, com.android.server.StorageManagerService.this.mVolumes.size(), com.android.server.StorageManagerService.this.mSystemUnlockedUsers.length);
            }
        }

        @Override // android.os.IVoldListener
        public void onVolumeChecked(java.lang.String volId, int type, java.lang.String diskId, java.lang.String partGuid) {
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                android.util.Slog.i(com.android.server.StorageManagerService.TAG, "onVolumeChecked,volId=" + volId + ",type=" + type + ",VolumeInfo.TYPE_PUBLIC=0,diskId=" + diskId + ",partGuid=" + partGuid);
                android.os.storage.VolumeInfo vol = com.android.server.StorageManagerService.this.mVolumes.get(volId);
                com.android.server.StorageManagerService.this.onVolumeCreatedLocked(vol);
            }
        }
    };
    private byte[] lock = new byte[0];
    private com.android.server.IOplusStorageManagerCallback mStorageCallback = new com.android.server.IOplusStorageManagerCallback() { // from class: com.android.server.StorageManagerService.19
        @Override // com.android.server.IOplusStorageManagerCallback
        public android.os.storage.VolumeInfo getVolumeInfoByIndexLocked(int index) {
            if (index >= 0 && index < com.android.server.StorageManagerService.this.mVolumes.size()) {
                android.os.storage.VolumeInfo vol = com.android.server.StorageManagerService.this.mVolumes.valueAt(index);
                return vol;
            }
            return null;
        }

        @Override // com.android.server.IOplusStorageManagerCallback
        public int getSystemUnlockedUserIdByIndexLocked(int index) {
            if (index >= 0 && index < com.android.server.StorageManagerService.this.mSystemUnlockedUsers.length) {
                return com.android.server.StorageManagerService.this.mSystemUnlockedUsers[index];
            }
            return -1;
        }

        @Override // com.android.server.IOplusStorageManagerCallback
        public void onCheckBeforeMount(java.lang.String volumeInfoId) {
            if (volumeInfoId != null && com.android.server.StorageManagerService.this.mVold != null) {
                try {
                    com.android.server.StorageManagerService.this.mVold.checkBeforeMount(volumeInfoId);
                } catch (java.lang.Exception e) {
                    android.util.Slog.wtf(com.android.server.StorageManagerService.TAG, e);
                }
            }
        }

        @Override // com.android.server.IOplusStorageManagerCallback
        public void onFsyncCtrl(java.lang.String fsyncMode) {
            if (com.android.server.StorageManagerService.this.mVold != null) {
                try {
                    com.android.server.StorageManagerService.this.mVold.fsyncCtrl(fsyncMode);
                } catch (java.lang.Exception e) {
                    android.util.Slog.wtf(com.android.server.StorageManagerService.TAG, e);
                }
            }
        }

        @Override // com.android.server.IOplusStorageManagerCallback
        public void oplusAbortIdleMaintenance() {
            com.android.server.StorageManagerService.this.abortIdleMaintenance();
        }

        @Override // com.android.server.IOplusStorageManagerCallback
        public boolean oplusIsFuseEnabled() {
            return true;
        }

        @Override // com.android.server.IOplusStorageManagerCallback
        public void unlockSensitiveFileKey(int userId, int serialNumber, byte[] token, byte[] secret, int sensitiveType) {
            try {
                com.android.server.StorageManagerService.this.mVold.unlockSensitiveKey(userId, serialNumber, com.android.internal.util.HexDump.toHexString(token), com.android.internal.util.HexDump.toHexString(secret), 0);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.StorageManagerService.TAG, "unlockSensitiveKey fail!");
            }
        }

        @Override // com.android.server.IOplusStorageManagerCallback
        public byte[] exportSensitveFileKey(int userId, int sensitiveType, boolean useDefault) {
            try {
                byte[] key = com.android.server.StorageManagerService.this.mVold.exportSensitiveKey(userId, sensitiveType, useDefault);
                if (key != null) {
                    android.util.Slog.i(com.android.server.StorageManagerService.TAG, "exportSensitveFileKey: success, sensitiveType:" + sensitiveType + ",size=" + key.length);
                }
                return key;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.StorageManagerService.TAG, "exportSensitveFileKey fail!");
                return null;
            }
        }
    };

    public static class Lifecycle extends com.android.server.SystemService {
        protected com.android.server.StorageManagerService mStorageManagerService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mStorageManagerService = new com.android.server.StorageManagerService(getContext());
            publishBinderService("mount", this.mStorageManagerService);
            this.mStorageManagerService.start();
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 500) {
                this.mStorageManagerService.servicesReady();
            } else if (phase == 550) {
                this.mStorageManagerService.systemReady();
            } else if (phase == 1000) {
                this.mStorageManagerService.bootCompleted();
            }
        }

        @Override // com.android.server.SystemService
        public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
            int currentUserId = to.getUserIdentifier();
            this.mStorageManagerService.mCurrentUserId = currentUserId;
            com.android.server.pm.UserManagerInternal umInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            if (umInternal.isUserUnlocked(currentUserId)) {
                android.util.Slog.d(com.android.server.StorageManagerService.TAG, "Attempt remount volumes for user: " + currentUserId);
                this.mStorageManagerService.maybeRemountVolumes(currentUserId);
                this.mStorageManagerService.mRemountCurrentUserVolumesOnUnlock = false;
            } else {
                android.util.Slog.d(com.android.server.StorageManagerService.TAG, "Attempt remount volumes for user: " + currentUserId + " on unlock");
                this.mStorageManagerService.mRemountCurrentUserVolumesOnUnlock = true;
            }
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
            this.mStorageManagerService.onUserUnlocking(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserStopped(com.android.server.SystemService.TargetUser user) {
            this.mStorageManagerService.onUserStopped(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserStopping(com.android.server.SystemService.TargetUser user) {
            this.mStorageManagerService.onUserStopping(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserStarting(com.android.server.SystemService.TargetUser user) {
            this.mStorageManagerService.snapshotAndMonitorLegacyStorageAppOp(user.getUserHandle());
        }
    }

    private static class WatchedUnlockedUsers {
        private int[] users = libcore.util.EmptyArray.INT;

        public WatchedUnlockedUsers() {
            invalidateIsUserUnlockedCache();
        }

        public void append(int userId) {
            this.users = com.android.internal.util.ArrayUtils.appendInt(this.users, userId);
            invalidateIsUserUnlockedCache();
        }

        public void appendAll(int[] userIds) {
            for (int userId : userIds) {
                this.users = com.android.internal.util.ArrayUtils.appendInt(this.users, userId);
            }
            invalidateIsUserUnlockedCache();
        }

        public void remove(int userId) {
            this.users = com.android.internal.util.ArrayUtils.removeInt(this.users, userId);
            invalidateIsUserUnlockedCache();
        }

        public boolean contains(int userId) {
            return com.android.internal.util.ArrayUtils.contains(this.users, userId);
        }

        public int[] all() {
            return this.users;
        }

        public java.lang.String toString() {
            return java.util.Arrays.toString(this.users);
        }

        private void invalidateIsUserUnlockedCache() {
            android.os.UserManager.invalidateIsUserUnlockedCache();
        }
    }

    private android.os.storage.VolumeInfo findVolumeByIdOrThrow(java.lang.String id) {
        synchronized (this.mLock) {
            android.os.storage.VolumeInfo vol = this.mVolumes.get(id);
            if (vol != null) {
                return vol;
            }
            throw new java.lang.IllegalArgumentException("No volume found for ID " + id);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.storage.VolumeRecord findRecordForPath(java.lang.String path) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mVolumes.size(); i++) {
                android.os.storage.VolumeInfo vol = this.mVolumes.valueAt(i);
                if (vol.path != null && path.startsWith(vol.path)) {
                    return this.mRecords.get(vol.fsUuid);
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String scrubPath(java.lang.String path) {
        if (path.startsWith(android.os.Environment.getDataDirectory().getAbsolutePath())) {
            return "internal";
        }
        android.os.storage.VolumeRecord rec = findRecordForPath(path);
        if (rec == null || rec.createdMillis == 0) {
            return "unknown";
        }
        return "ext:" + ((int) ((java.lang.System.currentTimeMillis() - rec.createdMillis) / com.android.server.usage.UnixCalendar.WEEK_IN_MILLIS)) + "w";
    }

    private android.os.storage.VolumeInfo findStorageForUuidAsUser(java.lang.String volumeUuid, int userId) {
        android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class);
        if (java.util.Objects.equals(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, volumeUuid)) {
            return storage.findVolumeById("emulated;" + userId);
        }
        if (java.util.Objects.equals("primary_physical", volumeUuid)) {
            return storage.getPrimaryPhysicalVolume();
        }
        android.os.storage.VolumeInfo info = storage.findVolumeByUuid(volumeUuid);
        if (info == null) {
            android.util.Slog.w(TAG, "findStorageForUuidAsUser cannot find volumeUuid:" + volumeUuid);
            return null;
        }
        java.lang.String emulatedUuid = info.getId().replace("private", "emulated") + ";" + userId;
        return storage.findVolumeById(emulatedUuid);
    }

    private java.util.concurrent.CountDownLatch findOrCreateDiskScanLatch(java.lang.String diskId) {
        java.util.concurrent.CountDownLatch latch;
        synchronized (this.mLock) {
            latch = this.mDiskScanLatches.get(diskId);
            if (latch == null) {
                latch = new java.util.concurrent.CountDownLatch(1);
                this.mDiskScanLatches.put(diskId, latch);
            }
        }
        return latch;
    }

    class ObbState implements android.os.IBinder.DeathRecipient {
        final java.lang.String canonicalPath;
        final int nonce;
        final int ownerGid;
        final java.lang.String rawPath;
        final android.os.storage.IObbActionListener token;
        java.lang.String volId;

        public ObbState(java.lang.String rawPath, java.lang.String canonicalPath, int callingUid, android.os.storage.IObbActionListener token, int nonce, java.lang.String volId) {
            this.rawPath = rawPath;
            this.canonicalPath = canonicalPath;
            this.ownerGid = android.os.UserHandle.getSharedAppGid(callingUid);
            this.token = token;
            this.nonce = nonce;
            this.volId = volId;
        }

        public android.os.IBinder getBinder() {
            return this.token.asBinder();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.StorageManagerService.ObbAction action = com.android.server.StorageManagerService.this.new UnmountObbAction(this, true);
            com.android.server.StorageManagerService.this.mObbActionHandler.sendMessage(com.android.server.StorageManagerService.this.mObbActionHandler.obtainMessage(1, action));
        }

        public void link() throws android.os.RemoteException {
            getBinder().linkToDeath(this, 0);
        }

        public void unlink() {
            getBinder().unlinkToDeath(this, 0);
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("ObbState{");
            sb.append("rawPath=").append(this.rawPath);
            sb.append(",canonicalPath=").append(this.canonicalPath);
            sb.append(",ownerGid=").append(this.ownerGid);
            sb.append(",token=").append(this.token);
            sb.append(",binder=").append(getBinder());
            sb.append(",volId=").append(this.volId);
            sb.append('}');
            return sb.toString();
        }
    }

    class StorageManagerServiceHandler extends android.os.Handler {
        public StorageManagerServiceHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            if (!com.android.server.StorageManagerService.this.mOplusStorageFeature.onStorageManagerMessageHandle(msg)) {
            }
            switch (msg.what) {
                case 1:
                    com.android.server.StorageManagerService.this.handleSystemReady();
                    break;
                case 2:
                    com.android.server.StorageManagerService.this.handleDaemonConnected();
                    break;
                case 3:
                    android.os.storage.IStorageShutdownObserver obs = (android.os.storage.IStorageShutdownObserver) msg.obj;
                    boolean success = false;
                    try {
                        com.android.server.StorageManagerService.this.mVold.shutdown();
                        success = true;
                    } catch (java.lang.Exception e) {
                        android.util.Slog.wtf(com.android.server.StorageManagerService.TAG, e);
                    }
                    if (obs != null) {
                        try {
                            obs.onShutDownComplete(success ? 0 : -1);
                        } catch (java.lang.Exception e2) {
                            return;
                        }
                    }
                    break;
                case 4:
                    android.util.Slog.i(com.android.server.StorageManagerService.TAG, "Running fstrim idle maintenance");
                    try {
                        com.android.server.StorageManagerService.this.mLastMaintenance = java.lang.System.currentTimeMillis();
                        com.android.server.StorageManagerService.this.mLastMaintenanceFile.setLastModified(com.android.server.StorageManagerService.this.mLastMaintenance);
                    } catch (java.lang.Exception e3) {
                        android.util.Slog.e(com.android.server.StorageManagerService.TAG, "Unable to record last fstrim!");
                    }
                    com.android.server.StorageManagerService.this.fstrim(0, null);
                    java.lang.Runnable callback = (java.lang.Runnable) msg.obj;
                    if (callback != null) {
                        callback.run();
                    }
                    break;
                case 5:
                    android.os.storage.VolumeInfo vol = (android.os.storage.VolumeInfo) msg.obj;
                    if (com.android.server.StorageManagerService.this.isMountDisallowed(vol)) {
                        android.util.Slog.i(com.android.server.StorageManagerService.TAG, "Ignoring mount " + vol.getId() + " due to policy");
                    } else {
                        com.android.server.StorageManagerService.this.mount(vol);
                    }
                    break;
                case 6:
                    android.os.storage.StorageVolume userVol = (android.os.storage.StorageVolume) msg.obj;
                    java.lang.String envState = userVol.getState();
                    android.util.Slog.d(com.android.server.StorageManagerService.TAG, "Volume " + userVol.getId() + " broadcasting " + envState + " to " + userVol.getOwner());
                    com.android.server.StorageManagerService.this.RegisterOplusManagerFeatureBoradcast(envState);
                    com.android.server.StorageManagerService.this.mOplusStorageFeature.checkMultiAppExternalStorageState(userVol);
                    java.lang.String action = android.os.storage.VolumeInfo.getBroadcastForEnvironment(envState);
                    if (action != null) {
                        android.content.Intent intent = new android.content.Intent(action, android.net.Uri.fromFile(userVol.getPathFile()));
                        intent.putExtra("android.os.storage.extra.STORAGE_VOLUME", userVol);
                        intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.HE_AAC_V1);
                        com.android.server.StorageManagerService.BROADCAST_TIME = java.lang.System.currentTimeMillis();
                        com.android.server.StorageManagerService.this.mContext.sendBroadcastAsUser(intent, userVol.getOwner());
                    }
                    break;
                case 7:
                    com.android.server.StorageManagerService.this.mContext.sendBroadcastAsUser((android.content.Intent) msg.obj, android.os.UserHandle.ALL, "android.permission.WRITE_MEDIA_STORAGE");
                    break;
                case 8:
                    com.android.server.StorageManagerService.this.unmount((android.os.storage.VolumeInfo) msg.obj);
                    break;
                case 9:
                    android.os.storage.VolumeRecord rec = (android.os.storage.VolumeRecord) msg.obj;
                    com.android.server.StorageManagerService.this.forgetPartition(rec.partGuid, rec.fsUuid);
                    break;
                case 10:
                    com.android.server.StorageManagerService.this.unRegisterOplusManagerFeatureBroadcast();
                    com.android.server.StorageManagerService.this.resetIfBootedAndConnected();
                    break;
                case 11:
                    android.util.Slog.i(com.android.server.StorageManagerService.TAG, "Running idle maintenance");
                    com.android.server.StorageManagerService.this.runIdleMaint((java.lang.Runnable) msg.obj);
                    break;
                case 12:
                    android.util.Slog.i(com.android.server.StorageManagerService.TAG, "Aborting idle maintenance");
                    com.android.server.StorageManagerService.this.abortIdleMaint((java.lang.Runnable) msg.obj);
                    break;
                case 13:
                    com.android.server.StorageManagerService.this.handleBootCompleted();
                    break;
                case 14:
                    com.android.server.StorageManagerService.this.completeUnlockUser(msg.arg1);
                    break;
                case 15:
                    com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                    com.android.server.StorageManagerService.this.onVolumeStateChangedAsync((android.os.storage.VolumeInfo) args.arg1, args.argi1, args.argi2);
                    args.recycle();
                    break;
                case 16:
                    if (msg.obj instanceof android.os.storage.StorageManagerInternal.CloudProviderChangeListener) {
                        android.os.storage.StorageManagerInternal.CloudProviderChangeListener listener = (android.os.storage.StorageManagerInternal.CloudProviderChangeListener) msg.obj;
                        com.android.server.StorageManagerService.this.notifyCloudMediaProviderChangedAsync(listener);
                    } else {
                        int userId = msg.arg1;
                        java.lang.String authority = (java.lang.String) msg.obj;
                        com.android.server.StorageManagerService.this.onCloudMediaProviderChangedAsync(userId, authority);
                    }
                    break;
                case 17:
                    try {
                        com.android.server.StorageManagerService.this.mVold.onSecureKeyguardStateChanged(((java.lang.Boolean) msg.obj).booleanValue());
                    } catch (java.lang.Exception e4) {
                        android.util.Slog.wtf(com.android.server.StorageManagerService.TAG, e4);
                        return;
                    }
                    break;
                case 18:
                    com.android.server.StorageManagerService.this.remountVolumesForRunningUsersOnMove();
                    break;
            }
        }
    }

    private void waitForLatch(java.util.concurrent.CountDownLatch latch, java.lang.String condition, long timeoutMillis) throws java.util.concurrent.TimeoutException {
        long startMillis = android.os.SystemClock.elapsedRealtime();
        while (!latch.await(5000L, java.util.concurrent.TimeUnit.MILLISECONDS)) {
            try {
                android.util.Slog.w(TAG, "Thread " + java.lang.Thread.currentThread().getName() + " still waiting for " + condition + "...");
            } catch (java.lang.InterruptedException e) {
                android.util.Slog.w(TAG, "Interrupt while waiting for " + condition);
            }
            if (timeoutMillis > 0 && android.os.SystemClock.elapsedRealtime() > startMillis + timeoutMillis) {
                throw new java.util.concurrent.TimeoutException("Thread " + java.lang.Thread.currentThread().getName() + " gave up waiting for " + condition + " after " + timeoutMillis + "ms");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSystemReady() {
        if (prepareSmartIdleMaint()) {
            com.android.server.SmartStorageMaintIdler.scheduleSmartIdlePass(this.mContext, sSmartIdleMaintPeriod);
        }
        if (android.os.SystemProperties.getBoolean("persist.oplus.fstrim.enhance", false)) {
            this.mOplusStorageFeature.schedulePreFstrim();
        } else {
            com.android.server.MountServiceIdler.scheduleIdlePass(this.mContext);
        }
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("zram_enabled"), false, new android.database.ContentObserver(null) { // from class: com.android.server.StorageManagerService.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.StorageManagerService.this.refreshZramSettings();
            }
        });
        refreshZramSettings();
        java.lang.String zramPropValue = android.os.SystemProperties.get(ZRAM_ENABLED_PROPERTY);
        if (!zramPropValue.equals("0") && this.mContext.getResources().getBoolean(android.R.bool.config_use_strict_phone_number_comparation_for_russia)) {
            com.android.server.ZramWriteback.scheduleZramWriteback(this.mContext);
        }
        configureTranscoding();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshZramSettings() {
        java.lang.String propertyValue = android.os.SystemProperties.get(ZRAM_ENABLED_PROPERTY);
        if ("".equals(propertyValue)) {
            return;
        }
        java.lang.String desiredPropertyValue = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "zram_enabled", 1) != 0 ? "1" : "0";
        if (!desiredPropertyValue.equals(propertyValue)) {
            android.os.SystemProperties.set(ZRAM_ENABLED_PROPERTY, desiredPropertyValue);
            if (desiredPropertyValue.equals("1") && this.mContext.getResources().getBoolean(android.R.bool.config_use_strict_phone_number_comparation_for_russia)) {
                com.android.server.ZramWriteback.scheduleZramWriteback(this.mContext);
            }
        }
    }

    private boolean isHevcDecoderSupported() {
        android.media.MediaCodecList codecList = new android.media.MediaCodecList(0);
        android.media.MediaCodecInfo[] codecInfos = codecList.getCodecInfos();
        for (android.media.MediaCodecInfo codecInfo : codecInfos) {
            if (!codecInfo.isEncoder()) {
                java.lang.String[] supportedTypes = codecInfo.getSupportedTypes();
                for (java.lang.String type : supportedTypes) {
                    if (type.equalsIgnoreCase("video/hevc")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void configureTranscoding() {
        boolean transcodeEnabled;
        boolean defaultValue = isHevcDecoderSupported();
        if (android.os.SystemProperties.getBoolean("persist.sys.fuse.transcode_user_control", false)) {
            transcodeEnabled = android.os.SystemProperties.getBoolean("persist.sys.fuse.transcode_enabled", defaultValue);
        } else {
            transcodeEnabled = android.provider.DeviceConfig.getBoolean("storage_native_boot", "transcode_enabled", defaultValue);
        }
        android.os.SystemProperties.set("sys.fuse.transcode_enabled", java.lang.String.valueOf(transcodeEnabled));
        if (transcodeEnabled) {
            ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).registerAnrController(new com.android.server.StorageManagerService.ExternalStorageServiceAnrController());
        }
    }

    private class ExternalStorageServiceAnrController implements android.app.AnrController {
        private ExternalStorageServiceAnrController() {
        }

        public long getAnrDelayMillis(java.lang.String packageName, int uid) {
            if (!com.android.server.StorageManagerService.this.isAppIoBlocked(uid)) {
                return 0L;
            }
            int delay = android.provider.DeviceConfig.getInt("storage_native_boot", com.android.server.StorageManagerService.ANR_DELAY_MILLIS_DEVICE_CONFIG_KEY, 5000);
            android.util.Slog.v(com.android.server.StorageManagerService.TAG, "getAnrDelayMillis for " + packageName + ". " + delay + "ms");
            return delay;
        }

        public void onAnrDelayStarted(java.lang.String packageName, int uid) {
            if (!com.android.server.StorageManagerService.this.isAppIoBlocked(uid)) {
                return;
            }
            boolean notifyExternalStorageService = android.provider.DeviceConfig.getBoolean("storage_native_boot", com.android.server.StorageManagerService.ANR_DELAY_NOTIFY_EXTERNAL_STORAGE_SERVICE_DEVICE_CONFIG_KEY, true);
            if (notifyExternalStorageService) {
                android.util.Slog.d(com.android.server.StorageManagerService.TAG, "onAnrDelayStarted for " + packageName + ". Notifying external storage service");
                try {
                    com.android.server.StorageManagerService.this.mStorageSessionController.notifyAnrDelayStarted(packageName, uid, 0, 1);
                } catch (com.android.server.storage.StorageSessionController.ExternalStorageServiceException e) {
                    android.util.Slog.e(com.android.server.StorageManagerService.TAG, "Failed to notify ANR delay started for " + packageName, e);
                }
            }
        }

        public boolean onAnrDelayCompleted(java.lang.String packageName, int uid) {
            if (com.android.server.StorageManagerService.this.isAppIoBlocked(uid)) {
                android.util.Slog.d(com.android.server.StorageManagerService.TAG, "onAnrDelayCompleted for " + packageName + ". Showing ANR dialog...");
                return true;
            }
            android.util.Slog.d(com.android.server.StorageManagerService.TAG, "onAnrDelayCompleted for " + packageName + ". Skipping ANR dialog...");
            return false;
        }
    }

    private void addInternalVolumeLocked() {
        android.os.storage.VolumeInfo internal = new android.os.storage.VolumeInfo("private", 1, (android.os.storage.DiskInfo) null, (java.lang.String) null);
        internal.state = 2;
        internal.path = android.os.Environment.getDataDirectory().getAbsolutePath();
        this.mVolumes.put(internal.id, internal);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterOplusManagerFeatureBroadcast() {
        if (IsRegisterOplusStorageManagerFeatureBoradcast) {
            this.mContext.unregisterReceiver(this.mOplusStorageFeature.getScreenReceiver());
            IsRegisterOplusStorageManagerFeatureBoradcast = false;
            android.util.Slog.i(TAG, "unregisterBroadcastReceiver of OplusStorageFeature");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void RegisterOplusManagerFeatureBoradcast(java.lang.String mountstat) {
        if (mountstat.equals("mounted") && !IsRegisterOplusStorageManagerFeatureBoradcast) {
            final java.util.Timer mtimer = new java.util.Timer();
            mtimer.schedule(new java.util.TimerTask() { // from class: com.android.server.StorageManagerService.3
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    if (!com.android.server.StorageManagerService.IsRegisterOplusStorageManagerFeatureBoradcast) {
                        android.content.IntentFilter screenFilter = new android.content.IntentFilter();
                        screenFilter.addAction("android.intent.action.SCREEN_ON");
                        screenFilter.addAction("android.intent.action.SCREEN_OFF");
                        screenFilter.addAction("android.intent.action.DREAMING_STARTED");
                        screenFilter.addAction("android.intent.action.DREAMING_STOPPED");
                        screenFilter.addAction("android.intent.action.BATTERY_LEVEL_CHANGED");
                        com.android.server.StorageManagerService.this.mContext.registerReceiver(com.android.server.StorageManagerService.this.mOplusStorageFeature.getScreenReceiver(), screenFilter, null, com.android.server.StorageManagerService.this.mHandler);
                        com.android.server.StorageManagerService.IsRegisterOplusStorageManagerFeatureBoradcast = true;
                        android.util.Slog.i(com.android.server.StorageManagerService.TAG, "mount have finshed 25s，we register OplusStorageFeature's broadcast");
                    }
                    mtimer.cancel();
                }
            }, 25000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetIfBootedAndConnected() {
        int[] systemUnlockedUsers;
        android.util.Slog.d(TAG, "Thinking about reset, mBootCompleted=" + this.mBootCompleted + ", mDaemonConnected=" + this.mDaemonConnected);
        if (this.mBootCompleted && this.mDaemonConnected) {
            android.os.UserManager userManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
            java.util.List<android.content.pm.UserInfo> users = userManager.getUsers();
            extendWatchdogTimeout("#onReset might be slow");
            this.mStorageSessionController.onReset(this.mVold, new java.lang.Runnable() { // from class: com.android.server.StorageManagerService$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$resetIfBootedAndConnected$0();
                }
            });
            synchronized (this.mLock) {
                systemUnlockedUsers = java.util.Arrays.copyOf(this.mSystemUnlockedUsers, this.mSystemUnlockedUsers.length);
                this.mDisks.clear();
                this.mVolumes.clear();
                addInternalVolumeLocked();
            }
            try {
                android.util.Slog.i(TAG, "Resetting vold...");
                this.mVold.reset();
                android.util.Slog.i(TAG, "Reset vold");
                for (android.content.pm.UserInfo user : users) {
                    if (user.isCloneProfile()) {
                        this.mVold.onUserAdded(user.id, user.serialNumber, user.profileGroupId);
                    } else {
                        this.mVold.onUserAdded(user.id, user.serialNumber, -1);
                    }
                }
                java.util.Arrays.sort(systemUnlockedUsers);
                for (int userId : systemUnlockedUsers) {
                    this.mVold.onUserStarted(userId);
                    this.mStoraged.onUserStarted(userId);
                }
                restoreSystemUnlockedUsers(userManager, users, systemUnlockedUsers);
                if (this.mOplusStorageFeature.shouldHandleKeyguardStateChange(this.mSecureKeyguardShowing)) {
                    this.mVold.onSecureKeyguardStateChanged(this.mSecureKeyguardShowing);
                }
                this.mStorageManagerInternal.onReset(this.mVold);
            } catch (java.lang.Exception e) {
                android.util.Slog.wtf(TAG, e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resetIfBootedAndConnected$0() {
        this.mHandler.removeCallbacksAndMessages(null);
    }

    private void restoreSystemUnlockedUsers(android.os.UserManager userManager, java.util.List<android.content.pm.UserInfo> allUsers, int[] systemUnlockedUsers) throws java.lang.Exception {
        java.util.Arrays.sort(systemUnlockedUsers);
        android.os.UserManager.invalidateIsUserUnlockedCache();
        for (android.content.pm.UserInfo user : allUsers) {
            int userId = user.id;
            if (userManager.isUserRunning(userId) && java.util.Arrays.binarySearch(systemUnlockedUsers, userId) < 0) {
                boolean unlockingOrUnlocked = userManager.isUserUnlockingOrUnlocked(userId);
                if (unlockingOrUnlocked) {
                    android.util.Slog.w(TAG, "UNLOCK_USER lost from vold reset, will retry, user:" + userId);
                    this.mVold.onUserStarted(userId);
                    this.mStoraged.onUserStarted(userId);
                    this.mHandler.obtainMessage(14, userId, 0).sendToTarget();
                }
            }
        }
    }

    private void restoreCeUnlockedUsers() {
        try {
            int[] userIds = this.mVold.getUnlockedUsers();
            if (!com.android.internal.util.ArrayUtils.isEmpty(userIds)) {
                android.util.Slog.d(TAG, "CE storage for users " + java.util.Arrays.toString(userIds) + " is already unlocked");
                synchronized (this.mLock) {
                    this.mCeUnlockedUsers.appendAll(userIds);
                }
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Failed to get unlocked users from vold", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserUnlocking(int userId) {
        android.util.Slog.d(TAG, "onUserUnlocking " + userId);
        this.mOplusStorageFeature.onUnlockUser(userId);
        java.util.List<android.content.pm.UserInfo> users = ((android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class)).getUsers();
        for (android.content.pm.UserInfo user : users) {
            try {
                if (user.id == userId) {
                    this.mOplusStorageFeature.unlockAndExportAllSensitiveFileKey(user.id, user.serialNumber, null, null);
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.wtf(TAG, e);
            }
        }
        if (userId != 0) {
            try {
                android.content.Context userContext = this.mContext.createPackageContextAsUser("system", 0, android.os.UserHandle.of(userId));
                android.os.UserManager um = (android.os.UserManager) userContext.getSystemService(android.os.UserManager.class);
                if (um != null && um.isMediaSharedWithParent()) {
                    int parentUserId = um.getProfileParent(userId).id;
                    this.mUserSharesMediaWith.put(userId, parentUserId);
                    this.mUserSharesMediaWith.put(parentUserId, userId);
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                android.util.Log.e(TAG, "Failed to create user context for user " + userId);
            }
        }
        try {
            this.mStorageSessionController.onUnlockUser(userId);
            this.mVold.onUserStarted(userId);
            this.mStoraged.onUserStarted(userId);
        } catch (java.lang.Exception e3) {
            android.util.Slog.wtf(TAG, e3);
        }
        this.mHandler.obtainMessage(14, userId, 0).sendToTarget();
        if (this.mRemountCurrentUserVolumesOnUnlock && userId == this.mCurrentUserId) {
            maybeRemountVolumes(userId);
            this.mRemountCurrentUserVolumesOnUnlock = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void completeUnlockUser(int userId) {
        onKeyguardStateChanged(false);
        synchronized (this.mLock) {
            for (int unlockedUser : this.mSystemUnlockedUsers) {
                if (unlockedUser == userId) {
                    android.util.Log.i(TAG, "completeUnlockUser called for already unlocked user:" + userId);
                    return;
                }
            }
            for (int i = 0; i < this.mVolumes.size(); i++) {
                android.os.storage.VolumeInfo vol = this.mVolumes.valueAt(i);
                if (vol.isVisibleForUser(userId) && vol.isMountedReadable()) {
                    android.os.storage.StorageVolume userVol = vol.buildStorageVolume(this.mContext, userId, false);
                    this.mHandler.obtainMessage(6, userVol).sendToTarget();
                    java.lang.String envState = android.os.storage.VolumeInfo.getEnvironmentForState(vol.getState());
                    this.mCallbacks.notifyStorageStateChanged(userVol.getPath(), envState, envState);
                }
            }
            this.mSystemUnlockedUsers = com.android.internal.util.ArrayUtils.appendInt(this.mSystemUnlockedUsers, userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void extendWatchdogTimeout(java.lang.String reason) {
        com.android.server.Watchdog w = com.android.server.Watchdog.getInstance();
        w.pauseWatchingMonitorsFor(SLOW_OPERATION_WATCHDOG_TIMEOUT_MS, reason);
        w.pauseWatchingCurrentThreadFor(SLOW_OPERATION_WATCHDOG_TIMEOUT_MS, reason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserStopped(int userId) {
        android.util.Slog.d(TAG, "onUserStopped " + userId);
        extendWatchdogTimeout("#onUserStopped might be slow");
        try {
            this.mVold.onUserStopped(userId);
            this.mStoraged.onUserStopped(userId);
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
        synchronized (this.mLock) {
            this.mSystemUnlockedUsers = com.android.internal.util.ArrayUtils.removeInt(this.mSystemUnlockedUsers, userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserStopping(int userId) {
        android.util.Slog.i(TAG, "onUserStopping " + userId);
        try {
            this.mStorageSessionController.onUserStopping(userId);
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
        com.android.internal.content.PackageMonitor monitor = (com.android.internal.content.PackageMonitor) this.mPackageMonitorsForUser.removeReturnOld(userId);
        if (monitor != null) {
            monitor.unregister();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeRemountVolumes(int userId) {
        java.util.List<android.os.storage.VolumeInfo> volumesToRemount = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mVolumes.size(); i++) {
                android.os.storage.VolumeInfo vol = this.mVolumes.valueAt(i);
                if (!vol.isPrimary() && vol.isMountedWritable() && vol.isVisible() && vol.getMountUserId() != this.mCurrentUserId) {
                    vol.mountUserId = this.mCurrentUserId;
                    volumesToRemount.add(vol);
                }
            }
        }
        for (android.os.storage.VolumeInfo vol2 : volumesToRemount) {
            android.util.Slog.i(TAG, "Remounting volume for user: " + userId + ". Volume: " + vol2);
            this.mHandler.obtainMessage(8, vol2).sendToTarget();
            this.mHandler.obtainMessage(5, vol2).sendToTarget();
        }
    }

    private void updateVolumeMountIdIfRequired(android.os.storage.VolumeInfo vol) {
        synchronized (this.mLock) {
            if (!vol.isPrimary() && vol.isVisible() && vol.getMountUserId() != this.mCurrentUserId) {
                vol.mountUserId = this.mCurrentUserId;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void remountVolumesForRunningUsersOnMove() {
        java.util.List<java.lang.Integer> unlockedUsers = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            for (int userId : this.mSystemUnlockedUsers) {
                if (userId != this.mCurrentUserId) {
                    unlockedUsers.add(java.lang.Integer.valueOf(userId));
                }
            }
        }
        extendWatchdogTimeout("#onUserStopped might be slow");
        for (java.lang.Integer userId2 : unlockedUsers) {
            try {
                this.mVold.onUserStopped(userId2.intValue());
                this.mStoraged.onUserStopped(userId2.intValue());
            } catch (java.lang.Exception e) {
                android.util.Slog.wtf(TAG, e);
            }
        }
        for (java.lang.Integer userId3 : unlockedUsers) {
            try {
                this.mVold.onUserStarted(userId3.intValue());
                this.mStoraged.onUserStarted(userId3.intValue());
            } catch (java.lang.Exception e2) {
                android.util.Slog.wtf(TAG, e2);
            }
        }
    }

    private boolean supportsBlockCheckpoint() throws android.os.RemoteException {
        enforcePermission("android.permission.MOUNT_FORMAT_FILESYSTEMS");
        return this.mVold.supportsBlockCheckpoint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareUserStorageForMoveInternal(java.lang.String fromVolumeUuid, java.lang.String toVolumeUuid, java.util.List<android.content.pm.UserInfo> users) throws java.lang.Exception {
        for (android.content.pm.UserInfo user : users) {
            prepareUserStorageInternal(fromVolumeUuid, user.id, 3);
            prepareUserStorageInternal(toVolumeUuid, user.id, 3);
        }
    }

    @Override // com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver
    public void onAwakeStateChanged(boolean isAwake) {
    }

    @Override // com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver
    public void onKeyguardStateChanged(boolean isShowing) {
        boolean isSecureKeyguardShowing = isShowing && ((android.app.KeyguardManager) this.mContext.getSystemService(android.app.KeyguardManager.class)).isDeviceSecure(this.mCurrentUserId);
        if (this.mSecureKeyguardShowing != isSecureKeyguardShowing) {
            this.mSecureKeyguardShowing = isSecureKeyguardShowing;
            try {
                this.mVold.onSecureKeyguardStateChangedForSensitiveFile(this.mSecureKeyguardShowing, this.mCurrentUserId, 0);
                this.mVold.onSecureKeyguardStateChangedForSensitiveFile(this.mSecureKeyguardShowing, this.mCurrentUserId, 1);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "onKeyguardStateChanged sensitive file", e);
            }
            if (this.mOplusStorageFeature == null) {
                android.util.Slog.e(TAG, "OppoStorageFeature is not init");
            } else {
                this.mOplusStorageFeature.clearSensitiveKey(this.mSecureKeyguardShowing);
            }
            if (!this.mOplusStorageFeature.shouldHandleKeyguardStateChange(this.mSecureKeyguardShowing)) {
                return;
            }
            this.mHandler.obtainMessage(17, java.lang.Boolean.valueOf(this.mSecureKeyguardShowing)).sendToTarget();
        }
    }

    void runIdleMaintenance(java.lang.Runnable callback) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, callback));
    }

    public void runMaintenance() {
        super.runMaintenance_enforcePermission();
        runIdleMaintenance(null);
    }

    public long lastMaintenance() {
        return this.mLastMaintenance;
    }

    public void onDaemonConnected() {
        this.mDaemonConnected = true;
        this.mHandler.obtainMessage(2).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDaemonConnected() {
        resetIfBootedAndConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDiskScannedLocked(android.os.storage.DiskInfo disk) {
        int volumeCount = 0;
        for (int i = 0; i < this.mVolumes.size(); i++) {
            android.os.storage.VolumeInfo vol = this.mVolumes.valueAt(i);
            if (java.util.Objects.equals(disk.id, vol.getDiskId())) {
                volumeCount++;
            }
        }
        android.content.Intent intent = new android.content.Intent("android.os.storage.action.DISK_SCANNED");
        intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.HE_AAC_V1);
        intent.putExtra("android.os.storage.extra.DISK_ID", disk.id);
        intent.putExtra("android.os.storage.extra.VOLUME_COUNT", volumeCount);
        this.mHandler.obtainMessage(7, intent).sendToTarget();
        java.util.concurrent.CountDownLatch latch = this.mDiskScanLatches.remove(disk.id);
        if (latch != null) {
            latch.countDown();
        }
        disk.volumeCount = volumeCount;
        this.mCallbacks.notifyDiskScanned(disk, volumeCount);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVolumeCreatedLocked(android.os.storage.VolumeInfo vol) {
        android.app.ActivityManagerInternal amInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        if (vol.mountUserId >= 0 && !amInternal.isUserRunning(vol.mountUserId, 0)) {
            android.util.Slog.d(TAG, "Ignoring volume " + vol.getId() + " because user " + java.lang.Integer.toString(vol.mountUserId) + " is no longer running.");
            return;
        }
        if (vol.type == 2) {
            android.content.Context volumeUserContext = this.mContext.createContextAsUser(android.os.UserHandle.of(vol.mountUserId), 0);
            boolean isMediaSharedWithParent = volumeUserContext != null ? ((android.os.UserManager) volumeUserContext.getSystemService(android.os.UserManager.class)).isMediaSharedWithParent() : false;
            if (!isMediaSharedWithParent && !this.mStorageSessionController.supportsExternalStorage(vol.mountUserId)) {
                android.util.Slog.d(TAG, "Ignoring volume " + vol.getId() + " because user " + java.lang.Integer.toString(vol.mountUserId) + " does not support external storage.");
                return;
            }
            android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class);
            android.os.storage.VolumeInfo privateVol = storage.findPrivateForEmulated(vol);
            if ((java.util.Objects.equals(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, this.mPrimaryStorageUuid) && "private".equals(privateVol.id)) || java.util.Objects.equals(privateVol.fsUuid, this.mPrimaryStorageUuid)) {
                android.util.Slog.v(TAG, "Found primary storage at " + vol);
                vol.mountFlags |= 1;
                vol.mountFlags |= 4;
                this.mHandler.obtainMessage(5, vol).sendToTarget();
                return;
            }
            return;
        }
        if (vol.type == 0) {
            if (java.util.Objects.equals("primary_physical", this.mPrimaryStorageUuid) && vol.disk.isDefaultPrimary()) {
                android.util.Slog.v(TAG, "Found primary storage at " + vol);
                vol.mountFlags |= 1;
                vol.mountFlags |= 4;
            }
            if (vol.disk.isAdoptable()) {
                vol.mountFlags |= 4;
            }
            vol.mountUserId = this.mCurrentUserId;
            this.mHandler.obtainMessage(5, vol).sendToTarget();
            return;
        }
        if (vol.type == 1) {
            this.mHandler.obtainMessage(5, vol).sendToTarget();
            return;
        }
        if (vol.type == 5) {
            if (vol.disk.isStubVisible()) {
                vol.mountFlags |= 4;
            } else {
                vol.mountFlags |= 2;
            }
            vol.mountUserId = this.mCurrentUserId;
            this.mHandler.obtainMessage(5, vol).sendToTarget();
            return;
        }
        android.util.Slog.d(TAG, "Skipping automatic mounting of " + vol);
    }

    /*  JADX ERROR: UnsupportedOperationException in pass: RegionMakerVisitor
        java.lang.UnsupportedOperationException
        	at java.base/java.util.Collections$UnmodifiableCollection.add(Unknown Source)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker$1.leaveRegion(SwitchRegionMaker.java:390)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:70)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:23)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.insertBreaksForCase(SwitchRegionMaker.java:370)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.insertBreaks(SwitchRegionMaker.java:85)
        	at jadx.core.dex.visitors.regions.PostProcessRegions.leaveRegion(PostProcessRegions.java:33)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:70)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:19)
        	at jadx.core.dex.visitors.regions.PostProcessRegions.process(PostProcessRegions.java:23)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:31)
        */
    private boolean isBroadcastWorthy(android.os.storage.VolumeInfo r3) {
        /*
            r2 = this;
            int r0 = r3.getType()
            r1 = 0
            switch(r0) {
                case 0: goto L9;
                case 1: goto L9;
                case 2: goto L9;
                case 3: goto L8;
                case 4: goto L8;
                case 5: goto L9;
                default: goto L8;
            }
        L8:
            return r1
        L9:
            int r0 = r3.getState()
            switch(r0) {
                case 0: goto L12;
                case 1: goto L11;
                case 2: goto L12;
                case 3: goto L12;
                case 4: goto L11;
                case 5: goto L12;
                case 6: goto L12;
                case 7: goto L11;
                case 8: goto L12;
                default: goto L11;
            }
        L11:
            return r1
        L12:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.StorageManagerService.isBroadcastWorthy(android.os.storage.VolumeInfo):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVolumeStateChangedLocked(final android.os.storage.VolumeInfo vol, int newState) {
        if (vol.type == 2) {
            if (newState != 2) {
                this.mFuseMountedUser.remove(java.lang.Integer.valueOf(vol.getMountUserId()));
            } else if (this.mVoldAppDataIsolationEnabled) {
                final int userId = vol.getMountUserId();
                new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.StorageManagerService$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onVolumeStateChangedLocked$1(userId, vol);
                    }
                }).start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onVolumeStateChangedLocked$1(int userId, android.os.storage.VolumeInfo vol) {
        if (userId == 0 && android.os.Build.VERSION.DEVICE_INITIAL_SDK_INT < 29) {
            this.mPmInternal.migrateLegacyObbData();
        }
        synchronized (this.mLock) {
            this.mFuseMountedUser.add(java.lang.Integer.valueOf(userId));
        }
        java.util.Map<java.lang.Integer, java.lang.String> pidPkgMap = null;
        int i = 0;
        while (true) {
            if (i >= 5) {
                break;
            }
            try {
                pidPkgMap = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getProcessesWithPendingBindMounts(vol.getMountUserId());
                break;
            } catch (java.lang.IllegalStateException e) {
                android.util.Slog.i(TAG, "Some processes are starting, retry");
                android.os.SystemClock.sleep(100L);
                i++;
            }
        }
        if (pidPkgMap != null) {
            remountAppStorageDirs(pidPkgMap, userId);
        } else {
            android.util.Slog.wtf(TAG, "Not able to getStorageNotOptimizedProcesses() after 5 retries");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVolumeStateChangedAsync(android.os.storage.VolumeInfo vol, int oldState, int newState) {
        if (newState == 2) {
            try {
                prepareUserStorageIfNeeded(vol);
            } catch (java.lang.Exception e) {
                try {
                    this.mVold.unmount(vol.id);
                    return;
                } catch (java.lang.Exception ee) {
                    android.util.Slog.wtf(TAG, ee);
                    return;
                }
            }
        }
        synchronized (this.mLock) {
            if (!android.text.TextUtils.isEmpty(vol.fsUuid)) {
                android.os.storage.VolumeRecord rec = this.mRecords.get(vol.fsUuid);
                if (rec == null) {
                    rec = new android.os.storage.VolumeRecord(vol.type, vol.fsUuid);
                    rec.partGuid = vol.partGuid;
                    rec.createdMillis = java.lang.System.currentTimeMillis();
                    if (vol.type == 1) {
                        rec.nickname = vol.disk.getDescription();
                    }
                    if (vol.type == 0 && vol.disk.isUsb()) {
                        rec.nickname = vol.fsUuid;
                    }
                    this.mRecords.put(rec.fsUuid, rec);
                } else if (android.text.TextUtils.isEmpty(rec.partGuid)) {
                    rec.partGuid = vol.partGuid;
                }
                rec.lastSeenMillis = java.lang.System.currentTimeMillis();
                writeSettingsLocked();
            }
        }
        try {
            this.mStorageSessionController.notifyVolumeStateChanged(vol);
        } catch (com.android.server.storage.StorageSessionController.ExternalStorageServiceException e2) {
            android.util.Log.e(TAG, "Failed to notify volume state changed to the Storage Service", e2);
        }
        synchronized (this.mLock) {
            this.mCallbacks.notifyVolumeStateChanged(vol, oldState, newState);
            if (this.mBootCompleted && isBroadcastWorthy(vol)) {
                android.content.Intent intent = new android.content.Intent("android.os.storage.action.VOLUME_STATE_CHANGED");
                intent.putExtra("android.os.storage.extra.VOLUME_ID", vol.id);
                intent.putExtra("android.os.storage.extra.VOLUME_STATE", newState);
                intent.putExtra("android.os.storage.extra.FS_UUID", vol.fsUuid);
                intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.HE_AAC_V1);
                this.mHandler.obtainMessage(7, intent).sendToTarget();
            }
            java.lang.String oldStateEnv = android.os.storage.VolumeInfo.getEnvironmentForState(oldState);
            java.lang.String newStateEnv = android.os.storage.VolumeInfo.getEnvironmentForState(newState);
            if (!java.util.Objects.equals(oldStateEnv, newStateEnv)) {
                for (int userId : this.mSystemUnlockedUsers) {
                    if (vol.isVisibleForUser(userId) && this.mOplusStorageFeature.shouldNotifyVolumeStateChanged(newStateEnv, userId, vol)) {
                        android.os.storage.StorageVolume userVol = vol.buildStorageVolume(this.mContext, userId, false);
                        this.mHandler.obtainMessage(6, userVol).sendToTarget();
                        this.mCallbacks.notifyStorageStateChanged(userVol.getPath(), oldStateEnv, newStateEnv);
                    }
                }
            }
            if ((vol.type == 0 || vol.type == 5) && vol.state == 5) {
                this.mObbActionHandler.sendMessage(this.mObbActionHandler.obtainMessage(2, vol.path));
            }
            maybeLogMediaMount(vol, newState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCloudMediaProviderChangedAsync(android.os.storage.StorageManagerInternal.CloudProviderChangeListener listener) {
        synchronized (this.mCloudMediaProviders) {
            for (int i = this.mCloudMediaProviders.size() - 1; i >= 0; i--) {
                int userId = this.mCloudMediaProviders.keyAt(i);
                java.lang.String authority = this.mCloudMediaProviders.valueAt(i);
                listener.onCloudProviderChanged(userId, authority);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCloudMediaProviderChangedAsync(int userId, java.lang.String authority) {
        for (android.os.storage.StorageManagerInternal.CloudProviderChangeListener listener : this.mStorageManagerInternal.mCloudProviderChangeListeners) {
            listener.onCloudProviderChanged(userId, authority);
        }
    }

    private void maybeLogMediaMount(android.os.storage.VolumeInfo vol, int newState) {
        android.os.storage.DiskInfo disk;
        if (!android.app.admin.SecurityLog.isLoggingEnabled() || (disk = vol.getDisk()) == null || (disk.flags & 12) == 0) {
            return;
        }
        java.lang.String label = disk.label != null ? disk.label.trim() : "";
        if (newState == 2 || newState == 3) {
            android.app.admin.SecurityLog.writeEvent(210013, new java.lang.Object[]{vol.path, label});
        } else if (newState == 0 || newState == 8) {
            android.app.admin.SecurityLog.writeEvent(210014, new java.lang.Object[]{vol.path, label});
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMoveStatusLocked(int status) {
        if (this.mMoveCallback == null) {
            android.util.Slog.w(TAG, "Odd, status but no move requested");
            return;
        }
        try {
            this.mMoveCallback.onStatusChanged(-1, status, -1L);
        } catch (android.os.RemoteException e) {
        }
        if (status == 82) {
            android.util.Slog.d(TAG, "Move to " + this.mMoveTargetUuid + " copy phase finshed; persisting");
            this.mPrimaryStorageUuid = this.mMoveTargetUuid;
            writeSettingsLocked();
            this.mHandler.obtainMessage(18).sendToTarget();
        }
        if (android.content.pm.PackageManager.isMoveStatusFinished(status)) {
            android.util.Slog.d(TAG, "Move to " + this.mMoveTargetUuid + " finished with status " + status);
            this.mMoveCallback = null;
            this.mMoveTargetUuid = null;
        }
    }

    private void enforcePermission(java.lang.String perm) {
        this.mContext.enforceCallingOrSelfPermission(perm, perm);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMountDisallowed(android.os.storage.VolumeInfo vol) {
        android.os.UserManager userManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        boolean isUsbRestricted = false;
        if (vol.disk != null && vol.disk.isUsb()) {
            isUsbRestricted = userManager.hasUserRestriction("no_usb_file_transfer", android.os.Binder.getCallingUserHandle());
        }
        boolean isTypeRestricted = false;
        if (vol.type == 0 || vol.type == 1 || vol.type == 5) {
            isTypeRestricted = userManager.hasUserRestriction("no_physical_media", android.os.Binder.getCallingUserHandle());
        }
        return isUsbRestricted || isTypeRestricted;
    }

    private void enforceAdminUser() {
        android.os.UserManager um = (android.os.UserManager) this.mContext.getSystemService("user");
        int callingUserId = android.os.UserHandle.getCallingUserId();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            boolean isAdmin = um.getUserInfo(callingUserId).isAdmin();
            if (!isAdmin) {
                throw new java.lang.SecurityException("Only admin users can adopt sd cards");
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public StorageManagerService(android.content.Context context) {
        this.mOplusStorageFeature = null;
        sSelf = this;
        this.mVoldAppDataIsolationEnabled = android.os.SystemProperties.getBoolean(ANDROID_VOLD_APP_DATA_ISOLATION_ENABLED_PROPERTY, false);
        this.mOplusPmExt = (com.android.server.pm.IOplusPackageManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IOplusPackageManagerExt.class).base(context).create();
        this.mContext = context;
        this.mCallbacks = new com.android.server.StorageManagerService.Callbacks(com.android.server.FgThread.get().getLooper());
        android.os.HandlerThread hthread = new android.os.HandlerThread(TAG);
        hthread.start();
        this.mHandler = new com.android.server.StorageManagerService.StorageManagerServiceHandler(hthread.getLooper());
        this.mObbActionHandler = new com.android.server.StorageManagerService.ObbActionHandler(com.android.server.IoThread.get().getLooper());
        this.mStorageSessionController = new com.android.server.storage.StorageSessionController(this.mContext);
        this.mInstaller = new com.android.server.pm.Installer(this.mContext);
        this.mInstaller.onStart();
        java.io.File dataDir = android.os.Environment.getDataDirectory();
        java.io.File systemDir = new java.io.File(dataDir, "system");
        this.mLastMaintenanceFile = new java.io.File(systemDir, LAST_FSTRIM_FILE);
        if (!this.mLastMaintenanceFile.exists()) {
            try {
                new java.io.FileOutputStream(this.mLastMaintenanceFile).close();
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Unable to create fstrim record " + this.mLastMaintenanceFile.getPath());
            }
        } else {
            this.mLastMaintenance = this.mLastMaintenanceFile.lastModified();
        }
        this.mSettingsFile = new android.util.AtomicFile(new java.io.File(android.os.Environment.getDataSystemDirectory(), "storage.xml"), "storage-settings");
        this.mWriteRecordFile = new android.util.AtomicFile(new java.io.File(android.os.Environment.getDataSystemDirectory(), "storage-write-records"));
        sSmartIdleMaintPeriod = android.provider.DeviceConfig.getInt("storage_native_boot", "smart_idle_maint_period", 60);
        if (sSmartIdleMaintPeriod < 10) {
            sSmartIdleMaintPeriod = 10;
        } else if (sSmartIdleMaintPeriod > MAX_SMART_IDLE_MAINT_PERIOD) {
            sSmartIdleMaintPeriod = MAX_SMART_IDLE_MAINT_PERIOD;
        }
        this.mMaxWriteRecords = MAX_PERIOD_WRITE_RECORD / sSmartIdleMaintPeriod;
        this.mStorageWriteRecords = new int[this.mMaxWriteRecords];
        synchronized (this.mLock) {
            readSettingsLocked();
        }
        com.android.server.LocalServices.addService(android.os.storage.StorageManagerInternal.class, this.mStorageManagerInternal);
        android.content.IntentFilter userFilter = new android.content.IntentFilter();
        userFilter.addAction("android.intent.action.USER_ADDED");
        userFilter.addAction("android.intent.action.USER_REMOVED");
        this.mContext.registerReceiver(this.mUserReceiver, userFilter, null, this.mHandler);
        synchronized (this.mLock) {
            addInternalVolumeLocked();
        }
        com.android.server.Watchdog.getInstance().addMonitor(this);
        this.mOplusStorageFeature = (com.android.server.IOplusStorageManagerFeature) android.common.OplusFeatureCache.getOrCreate(com.android.server.IOplusStorageManagerFeature.DEFAULT, new java.lang.Object[]{this.mContext});
        this.mOplusStorageFeature.initOplusStorageFeature(this.mStorageCallback, this.mHandler);
        this.mAllFileAccessManager = (com.android.server.storage.IOplusStorageAllFileAccessManager) android.common.OplusFeatureCache.getOrCreate(com.android.server.storage.IOplusStorageAllFileAccessManager.DEFAULT, new java.lang.Object[]{this.mContext});
    }

    public void start() {
        lambda$connectStoraged$2();
        lambda$connectVold$3();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: connectStoraged, reason: merged with bridge method [inline-methods] */
    public void lambda$connectStoraged$2() {
        android.os.IBinder binder = android.os.ServiceManager.getService("storaged");
        if (binder != null) {
            try {
                binder.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.StorageManagerService.5
                    @Override // android.os.IBinder.DeathRecipient
                    public void binderDied() {
                        android.util.Slog.w(com.android.server.StorageManagerService.TAG, "storaged died; reconnecting");
                        com.android.server.StorageManagerService.this.mStoraged = null;
                        com.android.server.StorageManagerService.this.lambda$connectStoraged$2();
                    }
                }, 0);
            } catch (android.os.RemoteException e) {
                binder = null;
            }
        }
        if (binder != null) {
            this.mStoraged = android.os.IStoraged.Stub.asInterface(binder);
        } else {
            android.util.Slog.w(TAG, "storaged not found; trying again");
        }
        if (this.mStoraged == null) {
            com.android.internal.os.BackgroundThread.getHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.StorageManagerService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$connectStoraged$2();
                }
            }, 1000L);
        } else {
            onDaemonConnected();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: connectVold, reason: merged with bridge method [inline-methods] */
    public void lambda$connectVold$3() {
        android.os.IBinder binder = android.os.ServiceManager.getService("vold");
        if (binder != null) {
            try {
                binder.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.StorageManagerService.6
                    @Override // android.os.IBinder.DeathRecipient
                    public void binderDied() {
                        android.util.Slog.w(com.android.server.StorageManagerService.TAG, "vold died; reconnecting");
                        com.android.server.StorageManagerService.this.mVold = null;
                        com.android.server.StorageManagerService.this.lambda$connectVold$3();
                    }
                }, 0);
            } catch (android.os.RemoteException e) {
                binder = null;
            }
        }
        if (binder == null) {
            android.util.Slog.w(TAG, "vold not found; trying again");
        } else {
            this.mVold = android.os.IVold.Stub.asInterface(binder);
            try {
                this.mVold.setListener(this.mListener);
            } catch (android.os.RemoteException e2) {
                this.mVold = null;
                android.util.Slog.w(TAG, "vold listener rejected; trying again", e2);
            }
        }
        if (this.mVold == null) {
            com.android.internal.os.BackgroundThread.getHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.StorageManagerService$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$connectVold$3();
                }
            }, 1000L);
        } else {
            restoreCeUnlockedUsers();
            onDaemonConnected();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void servicesReady() {
        this.mPmInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mIPackageManager = android.content.pm.IPackageManager.Stub.asInterface(android.os.ServiceManager.getService("package"));
        this.mIAppOpsService = com.android.internal.app.IAppOpsService.Stub.asInterface(android.os.ServiceManager.getService("appops"));
        android.content.pm.ProviderInfo provider = getProviderInfo("media");
        if (provider != null) {
            this.mMediaStoreAuthorityAppId = android.os.UserHandle.getAppId(provider.applicationInfo.uid);
            sMediaStoreAuthorityProcessName = provider.applicationInfo.processName;
        }
        android.content.pm.ProviderInfo provider2 = getProviderInfo("downloads");
        if (provider2 != null) {
            this.mDownloadsAuthorityAppId = android.os.UserHandle.getAppId(provider2.applicationInfo.uid);
        }
        android.content.pm.ProviderInfo provider3 = getProviderInfo("com.android.externalstorage.documents");
        if (provider3 != null) {
            this.mExternalStorageAuthorityAppId = android.os.UserHandle.getAppId(provider3.applicationInfo.uid);
        }
        this.mAllFileAccessManager.servicesReady();
    }

    private android.content.pm.ProviderInfo getProviderInfo(java.lang.String authority) {
        return this.mPmInternal.resolveContentProvider(authority, 786432L, android.os.UserHandle.getUserId(0), 1000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLegacyStorageApps(java.lang.String packageName, int uid, boolean hasLegacy) {
        synchronized (this.mLock) {
            if (hasLegacy) {
                android.util.Slog.v(TAG, "Package " + packageName + " has legacy storage");
                this.mUidsWithLegacyExternalStorage.add(java.lang.Integer.valueOf(uid));
            } else {
                android.util.Slog.v(TAG, "Package " + packageName + " does not have legacy storage");
                this.mUidsWithLegacyExternalStorage.remove(java.lang.Integer.valueOf(uid));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void snapshotAndMonitorLegacyStorageAppOp(android.os.UserHandle user) {
        int userId = user.getIdentifier();
        for (android.content.pm.ApplicationInfo ai : this.mPmInternal.getInstalledApplications(4988928L, userId, android.os.Process.myUid())) {
            try {
                boolean hasLegacy = this.mIAppOpsService.checkOperation(87, ai.uid, ai.packageName) == 0;
                updateLegacyStorageApps(ai.packageName, ai.uid, hasLegacy);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to check legacy op for package " + ai.packageName, e);
            }
        }
        if (this.mPackageMonitorsForUser.get(userId) == null) {
            com.android.internal.content.PackageMonitor monitor = new com.android.internal.content.PackageMonitor() { // from class: com.android.server.StorageManagerService.7
                public void onPackageRemoved(java.lang.String packageName, int uid) {
                    com.android.server.StorageManagerService.this.updateLegacyStorageApps(packageName, uid, false);
                }
            };
            monitor.register(this.mContext, user, this.mHandler);
            this.mPackageMonitorsForUser.put(userId, monitor);
            return;
        }
        android.util.Slog.w(TAG, "PackageMonitor is already registered for: " + userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void systemReady() {
        ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).registerScreenObserver(this);
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bootCompleted() {
        this.mBootCompleted = true;
        this.mHandler.obtainMessage(13).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBootCompleted() {
        this.mOplusStorageFeature.initFillNode();
        resetIfBootedAndConnected();
    }

    private java.lang.String getDefaultPrimaryStorageUuid() {
        if (android.os.SystemProperties.getBoolean("ro.vold.primary_physical", false)) {
            return "primary_physical";
        }
        return android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL;
    }

    private void readSettingsLocked() {
        this.mRecords.clear();
        this.mPrimaryStorageUuid = getDefaultPrimaryStorageUuid();
        java.io.FileInputStream fis = null;
        try {
            try {
                try {
                    fis = this.mSettingsFile.openRead();
                    com.android.modules.utils.TypedXmlPullParser in = android.util.Xml.resolvePullParser(fis);
                    while (true) {
                        int type = in.next();
                        boolean z = true;
                        if (type == 1) {
                            break;
                        }
                        if (type == 2) {
                            java.lang.String tag = in.getName();
                            if (TAG_VOLUMES.equals(tag)) {
                                int version = in.getAttributeInt((java.lang.String) null, ATTR_VERSION, 1);
                                boolean primaryPhysical = android.os.SystemProperties.getBoolean("ro.vold.primary_physical", false);
                                if (version < 3 && (version < 2 || primaryPhysical)) {
                                    z = false;
                                }
                                boolean validAttr = z;
                                if (validAttr) {
                                    this.mPrimaryStorageUuid = com.android.internal.util.XmlUtils.readStringAttribute(in, ATTR_PRIMARY_STORAGE_UUID);
                                }
                            } else if (TAG_VOLUME.equals(tag)) {
                                android.os.storage.VolumeRecord rec = readVolumeRecord(in);
                                this.mRecords.put(rec.fsUuid, rec);
                            }
                        }
                    }
                } catch (org.xmlpull.v1.XmlPullParserException e) {
                    android.util.Slog.wtf(TAG, "Failed reading metadata", e);
                }
            } catch (java.io.FileNotFoundException e2) {
            } catch (java.io.IOException e3) {
                android.util.Slog.wtf(TAG, "Failed reading metadata", e3);
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(fis);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeSettingsLocked() {
        java.io.FileOutputStream fos = null;
        try {
            fos = this.mSettingsFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fos);
            out.startDocument((java.lang.String) null, true);
            out.startTag((java.lang.String) null, TAG_VOLUMES);
            out.attributeInt((java.lang.String) null, ATTR_VERSION, 3);
            com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_PRIMARY_STORAGE_UUID, this.mPrimaryStorageUuid);
            int size = this.mRecords.size();
            for (int i = 0; i < size; i++) {
                android.os.storage.VolumeRecord rec = this.mRecords.valueAt(i);
                writeVolumeRecord(out, rec);
            }
            out.endTag((java.lang.String) null, TAG_VOLUMES);
            out.endDocument();
            this.mSettingsFile.finishWrite(fos);
        } catch (java.io.IOException e) {
            if (fos != null) {
                this.mSettingsFile.failWrite(fos);
            }
        }
    }

    public static android.os.storage.VolumeRecord readVolumeRecord(com.android.modules.utils.TypedXmlPullParser in) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int type = in.getAttributeInt((java.lang.String) null, "type");
        java.lang.String fsUuid = com.android.internal.util.XmlUtils.readStringAttribute(in, ATTR_FS_UUID);
        android.os.storage.VolumeRecord meta = new android.os.storage.VolumeRecord(type, fsUuid);
        meta.partGuid = com.android.internal.util.XmlUtils.readStringAttribute(in, ATTR_PART_GUID);
        meta.nickname = com.android.internal.util.XmlUtils.readStringAttribute(in, ATTR_NICKNAME);
        meta.userFlags = in.getAttributeInt((java.lang.String) null, ATTR_USER_FLAGS);
        meta.createdMillis = in.getAttributeLong((java.lang.String) null, ATTR_CREATED_MILLIS, 0L);
        meta.lastSeenMillis = in.getAttributeLong((java.lang.String) null, ATTR_LAST_SEEN_MILLIS, 0L);
        meta.lastTrimMillis = in.getAttributeLong((java.lang.String) null, ATTR_LAST_TRIM_MILLIS, 0L);
        meta.lastBenchMillis = in.getAttributeLong((java.lang.String) null, ATTR_LAST_BENCH_MILLIS, 0L);
        return meta;
    }

    public static void writeVolumeRecord(com.android.modules.utils.TypedXmlSerializer out, android.os.storage.VolumeRecord rec) throws java.io.IOException {
        out.startTag((java.lang.String) null, TAG_VOLUME);
        out.attributeInt((java.lang.String) null, "type", rec.type);
        com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_FS_UUID, rec.fsUuid);
        com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_PART_GUID, rec.partGuid);
        com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_NICKNAME, rec.nickname);
        out.attributeInt((java.lang.String) null, ATTR_USER_FLAGS, rec.userFlags);
        out.attributeLong((java.lang.String) null, ATTR_CREATED_MILLIS, rec.createdMillis);
        out.attributeLong((java.lang.String) null, ATTR_LAST_SEEN_MILLIS, rec.lastSeenMillis);
        out.attributeLong((java.lang.String) null, ATTR_LAST_TRIM_MILLIS, rec.lastTrimMillis);
        out.attributeLong((java.lang.String) null, ATTR_LAST_BENCH_MILLIS, rec.lastBenchMillis);
        out.endTag((java.lang.String) null, TAG_VOLUME);
    }

    public void registerListener(android.os.storage.IStorageEventListener listener) {
        this.mCallbacks.register(listener);
    }

    public void unregisterListener(android.os.storage.IStorageEventListener listener) {
        this.mCallbacks.unregister(listener);
    }

    public void shutdown(android.os.storage.IStorageShutdownObserver observer) {
        super.shutdown_enforcePermission();
        android.util.Slog.i(TAG, "Shutting down");
        this.mHandler.obtainMessage(3, observer).sendToTarget();
    }

    public void mount(java.lang.String volId) {
        super.mount_enforcePermission();
        android.os.storage.VolumeInfo vol = findVolumeByIdOrThrow(volId);
        if (isMountDisallowed(vol)) {
            throw new java.lang.SecurityException("Mounting " + volId + " restricted by policy");
        }
        updateVolumeMountIdIfRequired(vol);
        mount(vol);
    }

    private void remountAppStorageDirs(java.util.Map<java.lang.Integer, java.lang.String> pidPkgMap, int userId) {
        for (java.util.Map.Entry<java.lang.Integer, java.lang.String> entry : pidPkgMap.entrySet()) {
            int pid = entry.getKey().intValue();
            java.lang.String packageName = entry.getValue();
            android.util.Slog.i(TAG, "Remounting storage for pid: " + pid);
            java.lang.String[] sharedPackages = this.mPmInternal.getSharedUserPackagesForPackage(packageName, userId);
            int uid = this.mPmInternal.getPackageUid(packageName, 0L, userId);
            java.lang.String[] packages = sharedPackages.length != 0 ? sharedPackages : new java.lang.String[]{packageName};
            try {
                this.mVold.remountAppStorageDirs(uid, pid, packages);
            } catch (android.os.RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mount(final android.os.storage.VolumeInfo vol) {
        try {
            android.util.Slog.i(TAG, "Mounting volume " + vol);
            extendWatchdogTimeout("#mount might be slow");
            this.mVold.mount(vol.id, vol.mountFlags, vol.mountUserId, new android.os.IVoldMountCallback.Stub() { // from class: com.android.server.StorageManagerService.8
                @Override // android.os.IVoldMountCallback
                public boolean onVolumeChecking(java.io.FileDescriptor fd, java.lang.String path, java.lang.String internalPath) {
                    vol.path = path;
                    vol.internalPath = internalPath;
                    android.os.ParcelFileDescriptor pfd = new android.os.ParcelFileDescriptor(fd);
                    try {
                        try {
                            com.android.server.StorageManagerService.this.mStorageSessionController.onVolumeMount(pfd, vol);
                            try {
                                pfd.close();
                                return true;
                            } catch (java.lang.Exception e) {
                                android.util.Slog.e(com.android.server.StorageManagerService.TAG, "Failed to close FUSE device fd", e);
                                return true;
                            }
                        } catch (java.lang.Throwable th) {
                            try {
                                pfd.close();
                            } catch (java.lang.Exception e2) {
                                android.util.Slog.e(com.android.server.StorageManagerService.TAG, "Failed to close FUSE device fd", e2);
                            }
                            throw th;
                        }
                    } catch (com.android.server.storage.StorageSessionController.ExternalStorageServiceException e3) {
                        android.util.Slog.e(com.android.server.StorageManagerService.TAG, "Failed to mount volume " + vol, e3);
                        android.util.Slog.i(com.android.server.StorageManagerService.TAG, "Scheduling reset in 10s");
                        com.android.server.StorageManagerService.this.mHandler.removeMessages(10);
                        com.android.server.StorageManagerService.this.mHandler.sendMessageDelayed(com.android.server.StorageManagerService.this.mHandler.obtainMessage(10), java.util.concurrent.TimeUnit.SECONDS.toMillis(10));
                        try {
                            pfd.close();
                            return false;
                        } catch (java.lang.Exception e4) {
                            android.util.Slog.e(com.android.server.StorageManagerService.TAG, "Failed to close FUSE device fd", e4);
                            return false;
                        }
                    }
                }
            });
            android.util.Slog.i(TAG, "Mounted volume " + vol);
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    public void unmount(java.lang.String volId) {
        super.unmount_enforcePermission();
        android.os.storage.VolumeInfo vol = findVolumeByIdOrThrow(volId);
        unmount(vol);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unmount(android.os.storage.VolumeInfo vol) {
        try {
            try {
                if (vol.type == 1) {
                    this.mInstaller.onPrivateVolumeRemoved(vol.getFsUuid());
                }
            } catch (com.android.server.pm.Installer.InstallerException e) {
                android.util.Slog.e(TAG, "Failed unmount mirror data", e);
            }
            this.mVold.unmount(vol.id);
            this.mStorageSessionController.onVolumeUnmount(vol);
        } catch (java.lang.Exception e2) {
            android.util.Slog.wtf(TAG, e2);
        }
    }

    public void format(java.lang.String volId) {
        super.format_enforcePermission();
        android.os.storage.VolumeInfo vol = findVolumeByIdOrThrow(volId);
        java.lang.String fsUuid = vol.fsUuid;
        try {
            this.mVold.format(vol.id, "auto");
            if (!android.text.TextUtils.isEmpty(fsUuid)) {
                forgetVolume(fsUuid);
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    public void benchmark(java.lang.String volId, final android.os.IVoldTaskListener listener) {
        super.benchmark_enforcePermission();
        try {
            this.mVold.benchmark(volId, new android.os.IVoldTaskListener.Stub() { // from class: com.android.server.StorageManagerService.9
                @Override // android.os.IVoldTaskListener
                public void onStatus(int status, android.os.PersistableBundle extras) {
                    com.android.server.StorageManagerService.this.dispatchOnStatus(listener, status, extras);
                }

                @Override // android.os.IVoldTaskListener
                public void onFinished(int status, android.os.PersistableBundle extras) {
                    com.android.server.StorageManagerService.this.dispatchOnFinished(listener, status, extras);
                    java.lang.String path = extras.getString("path");
                    java.lang.String ident = extras.getString("ident");
                    long create = extras.getLong("create");
                    long run = extras.getLong("run");
                    long destroy = extras.getLong("destroy");
                    android.os.DropBoxManager dropBox = (android.os.DropBoxManager) com.android.server.StorageManagerService.this.mContext.getSystemService(android.os.DropBoxManager.class);
                    dropBox.addText(com.android.server.StorageManagerService.TAG_STORAGE_BENCHMARK, com.android.server.StorageManagerService.this.scrubPath(path) + " " + ident + " " + create + " " + run + " " + destroy);
                    synchronized (com.android.server.StorageManagerService.this.mLock) {
                        android.os.storage.VolumeRecord rec = com.android.server.StorageManagerService.this.findRecordForPath(path);
                        if (rec != null) {
                            rec.lastBenchMillis = java.lang.System.currentTimeMillis();
                            com.android.server.StorageManagerService.this.writeSettingsLocked();
                        }
                    }
                }
            });
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void partitionPublic(java.lang.String diskId) {
        super.partitionPublic_enforcePermission();
        java.util.concurrent.CountDownLatch latch = findOrCreateDiskScanLatch(diskId);
        extendWatchdogTimeout("#partition might be slow");
        try {
            this.mVold.partition(diskId, 0, -1);
            waitForLatch(latch, "partitionPublic", 180000L);
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    public void partitionPrivate(java.lang.String diskId) {
        super.partitionPrivate_enforcePermission();
        enforceAdminUser();
        java.util.concurrent.CountDownLatch latch = findOrCreateDiskScanLatch(diskId);
        extendWatchdogTimeout("#partition might be slow");
        try {
            this.mVold.partition(diskId, 1, -1);
            waitForLatch(latch, "partitionPrivate", 180000L);
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    public void partitionMixed(java.lang.String diskId, int ratio) {
        super.partitionMixed_enforcePermission();
        enforceAdminUser();
        java.util.concurrent.CountDownLatch latch = findOrCreateDiskScanLatch(diskId);
        extendWatchdogTimeout("#partition might be slow");
        try {
            this.mVold.partition(diskId, 2, ratio);
            waitForLatch(latch, "partitionMixed", 180000L);
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    public void setVolumeNickname(java.lang.String fsUuid, java.lang.String nickname) {
        super.setVolumeNickname_enforcePermission();
        java.util.Objects.requireNonNull(fsUuid);
        synchronized (this.mLock) {
            android.os.storage.VolumeRecord rec = this.mRecords.get(fsUuid);
            rec.nickname = nickname;
            this.mCallbacks.notifyVolumeRecordChanged(rec);
            writeSettingsLocked();
        }
    }

    public void setVolumeUserFlags(java.lang.String fsUuid, int flags, int mask) {
        super.setVolumeUserFlags_enforcePermission();
        java.util.Objects.requireNonNull(fsUuid);
        synchronized (this.mLock) {
            android.os.storage.VolumeRecord rec = this.mRecords.get(fsUuid);
            rec.userFlags = (rec.userFlags & (~mask)) | (flags & mask);
            this.mCallbacks.notifyVolumeRecordChanged(rec);
            writeSettingsLocked();
        }
    }

    public void forgetVolume(java.lang.String fsUuid) {
        super.forgetVolume_enforcePermission();
        java.util.Objects.requireNonNull(fsUuid);
        synchronized (this.mLock) {
            android.os.storage.VolumeRecord rec = this.mRecords.remove(fsUuid);
            if (rec != null && !android.text.TextUtils.isEmpty(rec.partGuid)) {
                this.mHandler.obtainMessage(9, rec).sendToTarget();
            }
            this.mCallbacks.notifyVolumeForgotten(fsUuid);
            if (java.util.Objects.equals(this.mPrimaryStorageUuid, fsUuid)) {
                this.mPrimaryStorageUuid = getDefaultPrimaryStorageUuid();
                this.mHandler.obtainMessage(10).sendToTarget();
            }
            writeSettingsLocked();
        }
    }

    public void forgetAllVolumes() {
        super.forgetAllVolumes_enforcePermission();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mRecords.size(); i++) {
                java.lang.String fsUuid = this.mRecords.keyAt(i);
                android.os.storage.VolumeRecord rec = this.mRecords.valueAt(i);
                if (!android.text.TextUtils.isEmpty(rec.partGuid)) {
                    this.mHandler.obtainMessage(9, rec).sendToTarget();
                }
                this.mCallbacks.notifyVolumeForgotten(fsUuid);
            }
            this.mRecords.clear();
            if (!java.util.Objects.equals(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, this.mPrimaryStorageUuid)) {
                this.mPrimaryStorageUuid = getDefaultPrimaryStorageUuid();
            }
            writeSettingsLocked();
            this.mHandler.obtainMessage(10).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forgetPartition(java.lang.String partGuid, java.lang.String fsUuid) {
        try {
            this.mVold.forgetPartition(partGuid, fsUuid);
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    public void fstrim(int flags, final android.os.IVoldTaskListener listener) {
        super.fstrim_enforcePermission();
        try {
            if (needsCheckpoint() && supportsBlockCheckpoint()) {
                android.util.Slog.i(TAG, "Skipping fstrim - block based checkpoint in progress");
                return;
            }
            this.mVold.fstrim(flags, new android.os.IVoldTaskListener.Stub() { // from class: com.android.server.StorageManagerService.10
                @Override // android.os.IVoldTaskListener
                public void onStatus(int status, android.os.PersistableBundle extras) {
                    com.android.server.StorageManagerService.this.dispatchOnStatus(listener, status, extras);
                    if (status != 0) {
                        return;
                    }
                    java.lang.String path = extras.getString("path");
                    long bytes = extras.getLong("bytes");
                    long time = extras.getLong("time");
                    android.os.DropBoxManager dropBox = (android.os.DropBoxManager) com.android.server.StorageManagerService.this.mContext.getSystemService(android.os.DropBoxManager.class);
                    dropBox.addText(com.android.server.StorageManagerService.TAG_STORAGE_TRIM, com.android.server.StorageManagerService.this.scrubPath(path) + " " + bytes + " " + time);
                    synchronized (com.android.server.StorageManagerService.this.mLock) {
                        android.os.storage.VolumeRecord rec = com.android.server.StorageManagerService.this.findRecordForPath(path);
                        if (rec != null) {
                            rec.lastTrimMillis = java.lang.System.currentTimeMillis();
                            com.android.server.StorageManagerService.this.writeSettingsLocked();
                        }
                    }
                }

                @Override // android.os.IVoldTaskListener
                public void onFinished(int status, android.os.PersistableBundle extras) {
                    com.android.server.StorageManagerService.this.dispatchOnFinished(listener, status, extras);
                }
            });
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    private void connectDefrag() {
        android.os.IBinder binder = android.os.ServiceManager.waitForService("DefragService");
        try {
            binder.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.StorageManagerService.11
                @Override // android.os.IBinder.DeathRecipient
                public void binderDied() {
                    android.util.Slog.w(com.android.server.StorageManagerService.TAG, "defrag exit; return directly");
                    synchronized (com.android.server.StorageManagerService.this.lock) {
                        com.android.server.StorageManagerService.this.mDefrag = null;
                        java.lang.String defragStatusProp = android.os.SystemProperties.get(com.android.server.StorageManagerService.NATIVE_SERVICE_KEY, "0");
                        if (defragStatusProp.equals("1")) {
                            android.util.Slog.w(com.android.server.StorageManagerService.TAG, "defrag crash, set propty to 0");
                            android.os.SystemProperties.set(com.android.server.StorageManagerService.NATIVE_SERVICE_KEY, "0");
                        }
                    }
                }
            }, 0);
        } catch (android.os.RemoteException e) {
            binder = null;
            this.mDefrag = null;
        }
        if (binder != null) {
            this.mDefrag = oplus.os.IDefrag.Stub.asInterface(binder);
        } else {
            android.util.Slog.w(TAG, "defrag not found; trying again");
            this.mDefrag = null;
        }
    }

    void runIdleMaint(final java.lang.Runnable callback) {
        enforcePermission("android.permission.MOUNT_FORMAT_FILESYSTEMS");
        this.mOplusStorageFeature.setMaintAborted(false);
        try {
            if (!needsCheckpoint() || !supportsBlockCheckpoint()) {
                java.lang.String disableDefragFiles = android.os.SystemProperties.get("persist.sys.oplus.disableDefragFiles", "0");
                java.lang.String devlife = android.os.SystemProperties.get("persist.sys.oplus.nandswap.devlife", "true");
                java.lang.String disableScan = android.os.SystemProperties.get("persist.sys.oplus.disableCompressDedup", "0");
                this.enableDefrag = disableDefragFiles.equals("0") && devlife.equals("true");
                this.enableScan = disableScan.equals("0");
                android.util.Slog.d(TAG, "enableDefrag:" + this.enableDefrag + " enableScan:" + this.enableScan);
                if ((this.enableDefrag || this.enableScan) && !this.mOplusPmExt.isClosedSuperFirewall()) {
                    synchronized (this.lock) {
                        if (this.mDefrag != null) {
                            android.util.Slog.e(TAG, "defrag is running, exit!!!!");
                            return;
                        }
                        java.util.concurrent.TimeUnit.SECONDS.sleep(2L);
                        synchronized (this.lock) {
                            this.finishScan = !this.enableScan;
                            this.finishDefrag = this.enableDefrag ? false : true;
                            connectDefrag();
                            if (this.mDefrag != null) {
                                try {
                                    if (this.enableDefrag) {
                                        android.util.Slog.w(TAG, "defrag start defragFiles");
                                        this.mDefrag.defragFiles(android.app.ActivityManager.getCurrentUser(), new oplus.os.IDefragTaskListener.Stub() { // from class: com.android.server.StorageManagerService.12
                                            @Override // oplus.os.IDefragTaskListener
                                            public void onStatus(int status, android.os.PersistableBundle extras) {
                                            }

                                            @Override // oplus.os.IDefragTaskListener
                                            public void onFinished(int status, android.os.PersistableBundle extras) {
                                                android.util.Slog.w(com.android.server.StorageManagerService.TAG, "run defrag finished, status:" + status);
                                                synchronized (com.android.server.StorageManagerService.this.lock) {
                                                    com.android.server.StorageManagerService.this.finishDefrag = true;
                                                    if (com.android.server.StorageManagerService.this.finishDefrag && com.android.server.StorageManagerService.this.finishScan) {
                                                        com.android.server.StorageManagerService.this.mDefrag = null;
                                                    }
                                                }
                                            }
                                        });
                                    }
                                    if (this.enableScan) {
                                        android.util.Slog.w(TAG, "defrag start scan");
                                        this.mDefrag.startScan(new oplus.os.IDefragTaskListener.Stub() { // from class: com.android.server.StorageManagerService.13
                                            @Override // oplus.os.IDefragTaskListener
                                            public void onStatus(int status, android.os.PersistableBundle extras) {
                                            }

                                            @Override // oplus.os.IDefragTaskListener
                                            public void onFinished(int status, android.os.PersistableBundle extras) {
                                                android.util.Slog.w(com.android.server.StorageManagerService.TAG, "run defrag (scan) finished, status:" + status);
                                                synchronized (com.android.server.StorageManagerService.this.lock) {
                                                    com.android.server.StorageManagerService.this.finishScan = true;
                                                    if (com.android.server.StorageManagerService.this.finishDefrag && com.android.server.StorageManagerService.this.finishScan) {
                                                        com.android.server.StorageManagerService.this.mDefrag = null;
                                                    }
                                                }
                                            }
                                        });
                                    }
                                } catch (java.lang.Exception e) {
                                    this.mDefrag = null;
                                    android.util.Slog.wtf(TAG, e);
                                }
                            } else {
                                android.os.SystemProperties.set(NATIVE_SERVICE_KEY, "0");
                                this.mDefrag = null;
                            }
                        }
                    }
                }
                this.mVold.runIdleMaint(this.mNeedGC, new android.os.IVoldTaskListener.Stub() { // from class: com.android.server.StorageManagerService.14
                    @Override // android.os.IVoldTaskListener
                    public void onStatus(int status, android.os.PersistableBundle extras) {
                    }

                    @Override // android.os.IVoldTaskListener
                    public void onFinished(int status, android.os.PersistableBundle extras) {
                        android.util.Slog.w(com.android.server.StorageManagerService.TAG, "run idle maint finished, maintAborted:" + com.android.server.StorageManagerService.this.mOplusStorageFeature.maintAborted());
                        if (!com.android.server.StorageManagerService.this.mOplusStorageFeature.maintAborted()) {
                            com.android.server.StorageManagerService.this.mOplusStorageFeature.setMaintPrepared(false);
                            try {
                                com.android.server.StorageManagerService.this.mLastMaintenance = java.lang.System.currentTimeMillis();
                                com.android.server.StorageManagerService.this.mLastMaintenanceFile.setLastModified(com.android.server.StorageManagerService.this.mLastMaintenance);
                            } catch (java.lang.Exception e2) {
                                android.util.Slog.e(com.android.server.StorageManagerService.TAG, "Unable to record last maintenance!");
                            }
                        }
                        if (callback != null) {
                            com.android.internal.os.BackgroundThread.getHandler().post(callback);
                        }
                    }
                });
            } else {
                android.util.Slog.i(TAG, "Skipping idle maintenance - block based checkpoint in progress");
            }
        } catch (java.lang.Exception e2) {
            android.util.Slog.wtf(TAG, e2);
        }
        long lastCalcTime = this.mOplusStorageFeature.getLastCalcTime();
        if (lastCalcTime != 0 && java.lang.System.currentTimeMillis() - lastCalcTime < com.android.server.usage.AppStandbyController.ConstantsObserver.DEFAULT_SYSTEM_UPDATE_TIMEOUT) {
            android.util.Slog.i(TAG, "morning clean: frag score is calculated within 2hours(" + lastCalcTime + "), skip ...");
        } else {
            new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.StorageManagerService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$runIdleMaint$4();
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runIdleMaint$4() {
        android.util.Slog.i(TAG, "morning clean: ready to calc frag score");
        android.os.SystemProperties.set("persist.sys.oplus.f2fsFragScore", java.lang.String.valueOf(this.mOplusStorageFeature.getFragScore()));
        this.mOplusStorageFeature.setLastCalcTime(java.lang.System.currentTimeMillis());
        android.util.Slog.i(TAG, "morning clean: frag score property set");
    }

    public void runIdleMaintenance() {
        runIdleMaint(null);
    }

    public void runTBExt() throws android.content.pm.PackageManager.NameNotFoundException {
        int callingUid = android.os.Binder.getCallingUid();
        int appUid = -1;
        try {
            android.content.pm.PackageManager pm = this.mContext.getPackageManager();
            appUid = pm.getPackageUid("com.coloros.phonemanager", 0);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "Error get app uid : " + e.getMessage());
        }
        if (callingUid != 1000 && callingUid != 0 && callingUid != 2000 && callingUid != appUid) {
            throw new java.lang.SecurityException("No permission to start runTBExt");
        }
        try {
            this.mVold.voldTBExt();
        } catch (java.lang.Exception e2) {
            android.util.Slog.wtf(TAG, e2);
        }
    }

    public long mountTime() {
        return BROADCAST_TIME;
    }

    void abortIdleMaint(final java.lang.Runnable callback) {
        enforcePermission("android.permission.MOUNT_FORMAT_FILESYSTEMS");
        this.mOplusStorageFeature.setMaintAborted(true);
        try {
            if (!this.mOplusPmExt.isClosedSuperFirewall()) {
                synchronized (this.lock) {
                    if (this.mDefrag != null) {
                        this.mDefrag.abortDefragFiles(new oplus.os.IDefragTaskListener.Stub() { // from class: com.android.server.StorageManagerService.15
                            @Override // oplus.os.IDefragTaskListener
                            public void onStatus(int status, android.os.PersistableBundle extras) {
                            }

                            @Override // oplus.os.IDefragTaskListener
                            public void onFinished(int status, android.os.PersistableBundle extras) {
                                android.util.Slog.w(com.android.server.StorageManagerService.TAG, "abort defrag finished, status:" + status);
                            }
                        });
                        this.mDefrag.abortScan(new oplus.os.IDefragTaskListener.Stub() { // from class: com.android.server.StorageManagerService.16
                            @Override // oplus.os.IDefragTaskListener
                            public void onStatus(int status, android.os.PersistableBundle extras) {
                            }

                            @Override // oplus.os.IDefragTaskListener
                            public void onFinished(int status, android.os.PersistableBundle extras) {
                                android.util.Slog.w(com.android.server.StorageManagerService.TAG, "abort defrag (scan) finished, status:" + status);
                            }
                        });
                    }
                }
            }
            this.mVold.abortIdleMaint(new android.os.IVoldTaskListener.Stub() { // from class: com.android.server.StorageManagerService.17
                @Override // android.os.IVoldTaskListener
                public void onStatus(int status, android.os.PersistableBundle extras) {
                }

                @Override // android.os.IVoldTaskListener
                public void onFinished(int status, android.os.PersistableBundle extras) {
                    if (callback != null) {
                        com.android.internal.os.BackgroundThread.getHandler().post(callback);
                    }
                }
            });
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    public void abortIdleMaintenance() {
        abortIdleMaint(null);
    }

    private boolean prepareSmartIdleMaint() {
        boolean smartIdleMaintEnabled = android.provider.DeviceConfig.getBoolean("storage_native_boot", "smart_idle_maint_enabled", false);
        if (smartIdleMaintEnabled) {
            this.mLifetimePercentThreshold = android.provider.DeviceConfig.getInt("storage_native_boot", "lifetime_threshold", 70);
            this.mMinSegmentsThreshold = android.provider.DeviceConfig.getInt("storage_native_boot", "min_segments_threshold", 512);
            this.mDirtyReclaimRate = android.provider.DeviceConfig.getFloat("storage_native_boot", "dirty_reclaim_rate", 0.5f);
            this.mSegmentReclaimWeight = android.provider.DeviceConfig.getFloat("storage_native_boot", "segment_reclaim_weight", 1.0f);
            this.mLowBatteryLevel = android.provider.DeviceConfig.getFloat("storage_native_boot", "low_battery_level", DEFAULT_LOW_BATTERY_LEVEL);
            this.mChargingRequired = android.provider.DeviceConfig.getBoolean("storage_native_boot", "charging_required", true);
            this.mMinGCSleepTime = android.provider.DeviceConfig.getInt("storage_native_boot", "min_gc_sleeptime", 10000);
            this.mTargetDirtyRatio = android.provider.DeviceConfig.getInt("storage_native_boot", "target_dirty_ratio", 80);
            this.mNeedGC = false;
            loadStorageWriteRecords();
            try {
                this.mVold.refreshLatestWrite();
            } catch (java.lang.Exception e) {
                android.util.Slog.wtf(TAG, e);
            }
            refreshLifetimeConstraint();
        }
        return smartIdleMaintEnabled;
    }

    public boolean isPassedLifetimeThresh() {
        return this.mPassedLifetimeThresh;
    }

    private void loadStorageWriteRecords() {
        java.io.FileInputStream fis = null;
        try {
            try {
                fis = this.mWriteRecordFile.openRead();
                java.io.ObjectInputStream ois = new java.io.ObjectInputStream(fis);
                int periodValue = ois.readInt();
                if (periodValue == sSmartIdleMaintPeriod) {
                    this.mStorageWriteRecords = (int[]) ois.readObject();
                }
            } catch (java.io.FileNotFoundException e) {
            } catch (java.lang.Exception e2) {
                android.util.Slog.wtf(TAG, "Failed reading write records", e2);
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(fis);
        }
    }

    private int getAverageWriteAmount() {
        return java.util.Arrays.stream(this.mStorageWriteRecords).sum() / this.mMaxWriteRecords;
    }

    private void updateStorageWriteRecords(int latestWrite) {
        java.io.FileOutputStream fos = null;
        java.lang.System.arraycopy(this.mStorageWriteRecords, 0, this.mStorageWriteRecords, 1, this.mMaxWriteRecords - 1);
        this.mStorageWriteRecords[0] = latestWrite;
        try {
            fos = this.mWriteRecordFile.startWrite();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(fos);
            oos.writeInt(sSmartIdleMaintPeriod);
            oos.writeObject(this.mStorageWriteRecords);
            this.mWriteRecordFile.finishWrite(fos);
        } catch (java.io.IOException e) {
            if (fos != null) {
                this.mWriteRecordFile.failWrite(fos);
            }
        }
    }

    private boolean checkChargeStatus() {
        int status;
        android.content.IntentFilter ifilter = new android.content.IntentFilter("android.intent.action.BATTERY_CHANGED");
        android.content.Intent batteryStatus = this.mContext.registerReceiver(null, ifilter);
        if (this.mChargingRequired && (status = batteryStatus.getIntExtra("status", -1)) != 2 && status != 5) {
            android.util.Slog.w(TAG, "Battery is not being charged");
            return false;
        }
        int level = batteryStatus.getIntExtra("level", -1);
        int scale = batteryStatus.getIntExtra("scale", -1);
        float chargePercent = (level * 100.0f) / scale;
        if (chargePercent < this.mLowBatteryLevel) {
            android.util.Slog.w(TAG, "Battery level is " + chargePercent + ", which is lower than threshold: " + this.mLowBatteryLevel);
            return false;
        }
        return true;
    }

    private boolean refreshLifetimeConstraint() {
        try {
            int storageLifeTime = this.mVold.getStorageLifeTime();
            if (storageLifeTime == -1) {
                android.util.Slog.w(TAG, "Failed to get storage lifetime");
                return false;
            }
            if (storageLifeTime > this.mLifetimePercentThreshold) {
                android.util.Slog.w(TAG, "Ended smart idle maintenance, because of lifetime(" + storageLifeTime + "), lifetime threshold(" + this.mLifetimePercentThreshold + ")");
                this.mPassedLifetimeThresh = true;
                return false;
            }
            android.util.Slog.i(TAG, "Storage lifetime: " + storageLifeTime);
            return true;
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x00e1 A[Catch: all -> 0x00ed, TRY_ENTER, TRY_LEAVE, TryCatch #4 {, blocks: (B:3:0x0001, B:10:0x001b, B:32:0x00d3, B:39:0x00e1, B:44:0x00e9, B:45:0x00ec), top: B:51:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e9 A[Catch: all -> 0x00ed, TRY_ENTER, TryCatch #4 {, blocks: (B:3:0x0001, B:10:0x001b, B:32:0x00d3, B:39:0x00e1, B:44:0x00e9, B:45:0x00ec), top: B:51:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    synchronized void runSmartIdleMaint(java.lang.Runnable r12) {
        /*
            Method dump skipped, instruction units count: 240
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.StorageManagerService.runSmartIdleMaint(java.lang.Runnable):void");
    }

    public void setDebugFlags(int flags, int mask) {
        long token;
        java.lang.String value;
        java.lang.String value2;
        super.setDebugFlags_enforcePermission();
        if ((mask & 3) != 0) {
            if ((flags & 1) != 0) {
                value2 = "force_on";
            } else if ((flags & 2) != 0) {
                value2 = "force_off";
            } else {
                value2 = "";
            }
            token = android.os.Binder.clearCallingIdentity();
            try {
                android.os.SystemProperties.set("persist.sys.adoptable", value2);
                this.mHandler.obtainMessage(10).sendToTarget();
                android.os.Binder.restoreCallingIdentity(token);
            } finally {
            }
        }
        if ((mask & 12) != 0) {
            if ((flags & 4) != 0) {
                value = "force_on";
            } else if ((flags & 8) != 0) {
                value = "force_off";
            } else {
                value = "";
            }
            token = android.os.Binder.clearCallingIdentity();
            try {
                android.os.SystemProperties.set("persist.sys.sdcardfs", value);
                this.mHandler.obtainMessage(10).sendToTarget();
            } finally {
            }
        }
        if ((mask & 16) != 0) {
            boolean enabled = (flags & 16) != 0;
            token = android.os.Binder.clearCallingIdentity();
            try {
                android.os.SystemProperties.set("persist.sys.virtual_disk", java.lang.Boolean.toString(enabled));
                this.mHandler.obtainMessage(10).sendToTarget();
            } finally {
            }
        }
    }

    public java.lang.String getPrimaryStorageUuid() {
        java.lang.String str;
        synchronized (this.mLock) {
            str = this.mPrimaryStorageUuid;
        }
        return str;
    }

    public void setPrimaryStorageUuid(java.lang.String volumeUuid, android.content.pm.IPackageMoveObserver callback) {
        super.setPrimaryStorageUuid_enforcePermission();
        synchronized (this.mLock) {
            if (java.util.Objects.equals(this.mPrimaryStorageUuid, volumeUuid)) {
                throw new java.lang.IllegalArgumentException("Primary storage already at " + volumeUuid);
            }
            if (this.mMoveCallback != null) {
                throw new java.lang.IllegalStateException("Move already in progress");
            }
            this.mMoveCallback = callback;
            this.mMoveTargetUuid = volumeUuid;
            java.util.List users = ((android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class)).getUsers();
            for (android.content.pm.UserInfo user : users) {
                if (android.os.storage.StorageManager.isFileEncrypted() && !isCeStorageUnlocked(user.id)) {
                    android.util.Slog.w(TAG, "Failing move due to locked user " + user.id);
                    onMoveStatusLocked(-10);
                    return;
                }
            }
            if (!java.util.Objects.equals("primary_physical", this.mPrimaryStorageUuid) && !java.util.Objects.equals("primary_physical", volumeUuid)) {
                int currentUserId = this.mCurrentUserId;
                android.os.storage.VolumeInfo from = findStorageForUuidAsUser(this.mPrimaryStorageUuid, currentUserId);
                android.os.storage.VolumeInfo to = findStorageForUuidAsUser(volumeUuid, currentUserId);
                if (from == null) {
                    android.util.Slog.w(TAG, "Failing move due to missing from volume " + this.mPrimaryStorageUuid);
                    onMoveStatusLocked(-6);
                    return;
                }
                if (to == null) {
                    android.util.Slog.w(TAG, "Failing move due to missing to volume " + volumeUuid);
                    onMoveStatusLocked(-6);
                    return;
                }
                try {
                    prepareUserStorageForMoveInternal(this.mPrimaryStorageUuid, volumeUuid, users);
                    try {
                        this.mVold.moveStorage(from.id, to.id, new android.os.IVoldTaskListener.Stub() { // from class: com.android.server.StorageManagerService.18
                            @Override // android.os.IVoldTaskListener
                            public void onStatus(int status, android.os.PersistableBundle extras) {
                                synchronized (com.android.server.StorageManagerService.this.mLock) {
                                    com.android.server.StorageManagerService.this.onMoveStatusLocked(status);
                                }
                            }

                            @Override // android.os.IVoldTaskListener
                            public void onFinished(int status, android.os.PersistableBundle extras) {
                            }
                        });
                        return;
                    } catch (java.lang.Exception e) {
                        android.util.Slog.wtf(TAG, e);
                        return;
                    }
                } catch (java.lang.Exception e2) {
                    android.util.Slog.w(TAG, "Failing move due to failure on prepare user data", e2);
                    synchronized (this.mLock) {
                        onMoveStatusLocked(-6);
                        return;
                    }
                }
            }
            android.util.Slog.d(TAG, "Skipping move to/from primary physical");
            onMoveStatusLocked(82);
            onMoveStatusLocked(-100);
            this.mHandler.obtainMessage(10).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void warnOnNotMounted() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mVolumes.size(); i++) {
                android.os.storage.VolumeInfo vol = this.mVolumes.valueAt(i);
                if (vol.isPrimary() && vol.isMountedWritable()) {
                    return;
                }
            }
            android.util.Slog.w(TAG, "No primary storage mounted!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUidOwnerOfPackageOrSystem(java.lang.String packageName, int callerUid) {
        if (callerUid == 1000) {
            return true;
        }
        return this.mPmInternal.isSameApp(packageName, callerUid, android.os.UserHandle.getUserId(callerUid));
    }

    public java.lang.String getMountedObbPath(java.lang.String rawPath) {
        com.android.server.StorageManagerService.ObbState state;
        java.util.Objects.requireNonNull(rawPath, "rawPath cannot be null");
        warnOnNotMounted();
        synchronized (this.mObbMounts) {
            state = this.mObbPathToStateMap.get(rawPath);
        }
        if (state == null) {
            android.util.Slog.w(TAG, "Failed to find OBB mounted at " + rawPath);
            return null;
        }
        return findVolumeByIdOrThrow(state.volId).getPath().getAbsolutePath();
    }

    public boolean isObbMounted(java.lang.String rawPath) {
        boolean zContainsKey;
        java.util.Objects.requireNonNull(rawPath, "rawPath cannot be null");
        synchronized (this.mObbMounts) {
            zContainsKey = this.mObbPathToStateMap.containsKey(rawPath);
        }
        return zContainsKey;
    }

    public void mountObb(java.lang.String rawPath, java.lang.String canonicalPath, android.os.storage.IObbActionListener token, int nonce, android.content.res.ObbInfo obbInfo) {
        java.util.Objects.requireNonNull(rawPath, "rawPath cannot be null");
        java.util.Objects.requireNonNull(canonicalPath, "canonicalPath cannot be null");
        java.util.Objects.requireNonNull(token, "token cannot be null");
        java.util.Objects.requireNonNull(obbInfo, "obbIfno cannot be null");
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.StorageManagerService.ObbState obbState = new com.android.server.StorageManagerService.ObbState(rawPath, canonicalPath, callingUid, token, nonce, null);
        com.android.server.StorageManagerService.ObbAction action = new com.android.server.StorageManagerService.MountObbAction(obbState, callingUid, obbInfo);
        this.mObbActionHandler.sendMessage(this.mObbActionHandler.obtainMessage(1, action));
    }

    public void unmountObb(java.lang.String rawPath, boolean force, android.os.storage.IObbActionListener token, int nonce) {
        com.android.server.StorageManagerService.ObbState existingState;
        java.util.Objects.requireNonNull(rawPath, "rawPath cannot be null");
        synchronized (this.mObbMounts) {
            existingState = this.mObbPathToStateMap.get(rawPath);
        }
        if (existingState != null) {
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.StorageManagerService.ObbState newState = new com.android.server.StorageManagerService.ObbState(rawPath, existingState.canonicalPath, callingUid, token, nonce, existingState.volId);
            com.android.server.StorageManagerService.ObbAction action = new com.android.server.StorageManagerService.UnmountObbAction(newState, force);
            this.mObbActionHandler.sendMessage(this.mObbActionHandler.obtainMessage(1, action));
            return;
        }
        android.util.Slog.w(TAG, "Unknown OBB mount at " + rawPath);
    }

    public boolean supportsCheckpoint() throws android.os.RemoteException {
        return this.mVold.supportsCheckpoint();
    }

    public void startCheckpoint(int numTries) throws android.os.RemoteException {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 1000 && callingUid != 0 && callingUid != 2000) {
            throw new java.lang.SecurityException("no permission to start filesystem checkpoint");
        }
        this.mVold.startCheckpoint(numTries);
    }

    public void commitChanges() throws android.os.RemoteException {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("no permission to commit checkpoint changes");
        }
        this.mVold.commitChanges();
    }

    public boolean needsCheckpoint() throws android.os.RemoteException {
        super.needsCheckpoint_enforcePermission();
        return this.mVold.needsCheckpoint();
    }

    public void abortChanges(java.lang.String message, boolean retry) throws android.os.RemoteException {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("no permission to commit checkpoint changes");
        }
        this.mVold.abortChanges(message, retry);
    }

    public void createUserStorageKeys(int userId, boolean ephemeral) {
        super.createUserStorageKeys_enforcePermission();
        try {
            this.mVold.createUserStorageKeys(userId, ephemeral);
            synchronized (this.mLock) {
                this.mCeUnlockedUsers.append(userId);
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    public void destroyUserStorageKeys(int userId) {
        super.destroyUserStorageKeys_enforcePermission();
        try {
            this.mVold.destroyUserStorageKeys(userId);
            synchronized (this.mLock) {
                this.mCeUnlockedUsers.remove(userId);
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    public void setCeStorageProtection(int userId, byte[] secret) throws android.os.RemoteException {
        super.setCeStorageProtection_enforcePermission();
        this.mVold.setCeStorageProtection(userId, secret);
    }

    public void unlockCeStorage(int userId, byte[] secret) throws android.os.RemoteException {
        super.unlockCeStorage_enforcePermission();
        java.util.List<android.content.pm.UserInfo> users = ((android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class)).getUsers();
        for (android.content.pm.UserInfo user : users) {
            try {
                if (user.id == userId) {
                    this.mOplusStorageFeature.unlockAndExportAllSensitiveFileKey(user.id, user.serialNumber, null, secret);
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.wtf(TAG, e);
            }
        }
        if (android.os.storage.StorageManager.isFileEncrypted()) {
            this.mVold.unlockCeStorage(userId, secret);
        }
        synchronized (this.mLock) {
            if (!this.mCeUnlockedUsers.contains(userId)) {
                this.mOplusStorageFeature.killInputMethods(this.mContext, userId, "unlockUserKey");
                this.mCeUnlockedUsers.append(userId);
            }
        }
    }

    public void lockCeStorage(int userId) {
        super.lockCeStorage_enforcePermission();
        if (userId == 0 && android.os.UserManager.isHeadlessSystemUserMode()) {
            throw new java.lang.IllegalArgumentException("Headless system user data cannot be locked..");
        }
        if (!isCeStorageUnlocked(userId)) {
            android.util.Slog.d(TAG, "User " + userId + "'s CE storage is already locked");
            return;
        }
        try {
            this.mVold.lockCeStorage(userId);
            synchronized (this.mLock) {
                this.mCeUnlockedUsers.remove(userId);
            }
            if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enablePrivateSpaceFeatures() && android.multiuser.Flags.enableBiometricsToUnlockPrivateSpace()) {
                dispatchCeStorageLockedEvent(userId);
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    public boolean isCeStorageUnlocked(int userId) {
        boolean zContains;
        synchronized (this.mLock) {
            zContains = this.mCeUnlockedUsers.contains(userId);
        }
        return zContains;
    }

    private boolean isSystemUnlocked(int userId) {
        boolean zContains;
        synchronized (this.mLock) {
            zContains = com.android.internal.util.ArrayUtils.contains(this.mSystemUnlockedUsers, userId);
        }
        return zContains;
    }

    private void prepareUserStorageIfNeeded(android.os.storage.VolumeInfo vol) throws java.lang.Exception {
        int flags;
        if (vol.type != 1) {
            return;
        }
        android.os.UserManager um = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        com.android.server.pm.UserManagerInternal umInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        for (android.content.pm.UserInfo user : um.getUsers()) {
            if (umInternal.isUserUnlockingOrUnlocked(user.id)) {
                flags = 3;
            } else {
                int flags2 = user.id;
                if (umInternal.isUserRunning(flags2)) {
                    flags = 1;
                }
            }
            prepareUserStorageInternal(vol.fsUuid, user.id, flags);
        }
    }

    public void prepareUserStorage(java.lang.String volumeUuid, int userId, int flags) {
        super.prepareUserStorage_enforcePermission();
        try {
            prepareUserStorageInternal(volumeUuid, userId, flags);
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private void prepareUserStorageInternal(java.lang.String volumeUuid, int userId, int flags) throws java.lang.Exception {
        try {
            this.mVold.prepareUserStorage(volumeUuid, userId, flags);
            if (volumeUuid != null) {
                android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class);
                android.os.storage.VolumeInfo info = storage.findVolumeByUuid(volumeUuid);
                if (info != null && userId == 0 && info.type == 1) {
                    this.mInstaller.tryMountDataMirror(volumeUuid);
                }
            }
        } catch (java.lang.Exception e) {
            android.util.EventLog.writeEvent(1397638484, "224585613", -1, "");
            android.util.Slog.wtf(TAG, e);
            com.android.server.pm.UserManagerInternal umInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            if (umInternal.shouldIgnorePrepareStorageErrors(userId)) {
                android.util.Slog.wtf(TAG, "ignoring error preparing storage for existing user " + userId + "; device may be insecure!");
                return;
            }
            throw e;
        }
    }

    public void destroyUserStorage(java.lang.String volumeUuid, int userId, int flags) {
        super.destroyUserStorage_enforcePermission();
        try {
            this.mVold.destroyUserStorage(volumeUuid, userId, flags);
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    public void fixupAppDir(java.lang.String path) {
        java.util.regex.Matcher matcher = KNOWN_APP_DIR_PATHS.matcher(path);
        if (matcher.matches()) {
            if (matcher.group(2) == null) {
                android.util.Log.e(TAG, "Asked to fixup an app dir without a userId: " + path);
                return;
            }
            try {
                int userId = java.lang.Integer.parseInt(matcher.group(2));
                java.lang.String packageName = matcher.group(3);
                int uid = this.mContext.getPackageManager().getPackageUidAsUser(packageName, userId);
                try {
                    this.mVold.fixupAppDir(path + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER, uid);
                    return;
                } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
                    android.util.Log.e(TAG, "Failed to fixup app dir for " + packageName, e);
                    return;
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                android.util.Log.e(TAG, "Couldn't find package to fixup app dir " + path, e2);
                return;
            } catch (java.lang.NumberFormatException e3) {
                android.util.Log.e(TAG, "Invalid userId in path: " + path, e3);
                return;
            }
        }
        android.util.Log.e(TAG, "Path " + path + " is not a valid application-specific directory");
    }

    public void disableAppDataIsolation(java.lang.String pkgName, int pid, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 0 && callingUid != 2000) {
            throw new java.lang.SecurityException("no permission to enable app visibility");
        }
        java.lang.String[] sharedPackages = this.mPmInternal.getSharedUserPackagesForPackage(pkgName, userId);
        int uid = this.mPmInternal.getPackageUid(pkgName, 0L, userId);
        java.lang.String[] packages = sharedPackages.length != 0 ? sharedPackages : new java.lang.String[]{pkgName};
        try {
            this.mVold.unmountAppStorageDirs(uid, pid, packages);
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public android.app.PendingIntent getManageSpaceActivityIntent(java.lang.String packageName, int requestCode) {
        int originalUid = android.os.Binder.getCallingUidOrThrow();
        try {
            java.lang.String[] packagesFromUid = this.mIPackageManager.getPackagesForUid(originalUid);
            if (packagesFromUid == null) {
                throw new java.lang.SecurityException("Unknown uid " + originalUid);
            }
            if (!this.mStorageManagerInternal.hasExternalStorageAccess(originalUid, packagesFromUid[0])) {
                throw new java.lang.SecurityException("Only File Manager Apps permitted");
            }
            try {
                android.content.pm.ApplicationInfo appInfo = this.mIPackageManager.getApplicationInfo(packageName, 0L, android.os.UserHandle.getUserId(originalUid));
                if (appInfo == null) {
                    throw new java.lang.IllegalArgumentException("Invalid packageName");
                }
                if (appInfo.manageSpaceActivityName == null) {
                    android.util.Log.i(TAG, packageName + " doesn't have a manageSpaceActivity");
                    return null;
                }
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    try {
                        android.content.Context targetAppContext = this.mContext.createPackageContext(packageName, 0);
                        android.content.Intent intent = new android.content.Intent("android.intent.action.VIEW");
                        intent.setClassName(packageName, appInfo.manageSpaceActivityName);
                        intent.setFlags(268435456);
                        android.app.ActivityOptions options = android.app.ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(2);
                        android.app.PendingIntent activity = android.app.PendingIntent.getActivity(targetAppContext, requestCode, intent, 1409286144, options.toBundle());
                        return activity;
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        throw new java.lang.IllegalArgumentException("packageName not found");
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            } catch (android.os.RemoteException e2) {
                throw new java.lang.SecurityException("Only File Manager Apps permitted");
            }
        } catch (android.os.RemoteException re) {
            throw new java.lang.SecurityException("Unknown uid " + originalUid, re);
        }
    }

    public void notifyAppIoBlocked(java.lang.String volumeUuid, int uid, int tid, int reason) {
        enforceExternalStorageService();
        this.mStorageSessionController.notifyAppIoBlocked(volumeUuid, uid, tid, reason);
    }

    public void notifyAppIoResumed(java.lang.String volumeUuid, int uid, int tid, int reason) {
        enforceExternalStorageService();
        this.mStorageSessionController.notifyAppIoResumed(volumeUuid, uid, tid, reason);
    }

    public boolean isAppIoBlocked(java.lang.String volumeUuid, int uid, int tid, int reason) {
        return isAppIoBlocked(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAppIoBlocked(int uid) {
        return this.mStorageSessionController.isAppIoBlocked(uid);
    }

    public void setCloudMediaProvider(java.lang.String authority) {
        enforceExternalStorageService();
        int userId = android.os.UserHandle.getUserId(android.os.Binder.getCallingUid());
        synchronized (this.mCloudMediaProviders) {
            java.lang.String oldAuthority = this.mCloudMediaProviders.get(userId);
            if (!java.util.Objects.equals(authority, oldAuthority)) {
                this.mCloudMediaProviders.put(userId, authority);
                this.mHandler.obtainMessage(16, userId, 0, authority).sendToTarget();
            }
        }
    }

    public java.lang.String getCloudMediaProvider() {
        java.lang.String authority;
        android.content.pm.ProviderInfo pi;
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getUserId(callingUid);
        synchronized (this.mCloudMediaProviders) {
            authority = this.mCloudMediaProviders.get(userId);
        }
        if (authority == null || (pi = this.mPmInternal.resolveContentProvider(authority, 0L, userId, callingUid)) == null || this.mPmInternal.filterAppAccess(pi.packageName, callingUid, userId)) {
            return null;
        }
        return authority;
    }

    public long getInternalStorageBlockDeviceSize() throws android.os.RemoteException {
        if (this.mInternalStorageSize == 0) {
            this.mInternalStorageSize = this.mVold.getStorageSize();
        }
        return this.mInternalStorageSize;
    }

    public int getInternalStorageRemainingLifetime() throws android.os.RemoteException {
        super.getInternalStorageRemainingLifetime_enforcePermission();
        return this.mVold.getStorageRemainingLifetime();
    }

    private void enforceExternalStorageService() {
        enforcePermission("android.permission.WRITE_MEDIA_STORAGE");
        int callingAppId = android.os.UserHandle.getAppId(android.os.Binder.getCallingUid());
        if (callingAppId != this.mMediaStoreAuthorityAppId) {
            throw new java.lang.SecurityException("Only the ExternalStorageService is permitted");
        }
    }

    class AppFuseMountScope extends com.android.server.storage.AppFuseBridge.MountScope {
        private boolean mMounted;

        public AppFuseMountScope(int uid, int mountId) {
            super(uid, mountId);
            this.mMounted = false;
        }

        @Override // com.android.server.storage.AppFuseBridge.MountScope
        public android.os.ParcelFileDescriptor open() throws com.android.server.AppFuseMountException {
            com.android.server.StorageManagerService.this.extendWatchdogTimeout("#open might be slow");
            try {
                java.io.FileDescriptor fd = com.android.server.StorageManagerService.this.mVold.mountAppFuse(this.uid, this.mountId);
                this.mMounted = true;
                return new android.os.ParcelFileDescriptor(fd);
            } catch (java.lang.Exception e) {
                throw new com.android.server.AppFuseMountException("Failed to mount", e);
            }
        }

        @Override // com.android.server.storage.AppFuseBridge.MountScope
        public android.os.ParcelFileDescriptor openFile(int mountId, int fileId, int flags) throws com.android.server.AppFuseMountException {
            com.android.server.StorageManagerService.this.extendWatchdogTimeout("#openFile might be slow");
            try {
                return new android.os.ParcelFileDescriptor(com.android.server.StorageManagerService.this.mVold.openAppFuseFile(this.uid, mountId, fileId, flags));
            } catch (java.lang.Exception e) {
                throw new com.android.server.AppFuseMountException("Failed to open", e);
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() throws java.lang.Exception {
            com.android.server.StorageManagerService.this.extendWatchdogTimeout("#close might be slow");
            if (this.mMounted) {
                com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.StorageManagerService$AppFuseMountScope$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$close$0();
                    }
                });
                this.mMounted = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$close$0() {
            try {
                com.android.server.StorageManagerService.this.mVold.unmountAppFuse(this.uid, this.mountId);
            } catch (android.os.RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }

        @Override // com.android.server.storage.AppFuseBridge.MountScope
        public void startserviceAppFuse() {
            try {
                android.util.Slog.v(com.android.server.StorageManagerService.TAG, "startserviceAppFuse in AppFuseMountScope");
                com.android.server.StorageManagerService.this.mVold.startserviceAppFuse();
            } catch (java.lang.Exception e) {
                android.util.Slog.v(com.android.server.StorageManagerService.TAG, "startserviceAppFuse Exception");
            }
        }

        @Override // com.android.server.storage.AppFuseBridge.MountScope
        public void stopserviceAppFuse() {
            try {
                com.android.server.StorageManagerService.this.mVold.stopserviceAppFuse();
            } catch (java.lang.Exception e) {
                android.util.Slog.v(com.android.server.StorageManagerService.TAG, "stopserviceAppFuse Exception");
            }
        }

        @Override // com.android.server.storage.AppFuseBridge.MountScope
        public void clearCache() {
            try {
                int whatsAppExist = android.provider.Settings.Secure.getInt(com.android.server.StorageManagerService.this.mContext.getContentResolver(), "KEY_WHATS_APP_EXIST", 1);
                android.util.Slog.v(com.android.server.StorageManagerService.TAG, "clearCache in AppFuseMountScopeand whatsAppExist: " + whatsAppExist);
                com.android.server.StorageManagerService.this.mVold.clearCache(whatsAppExist);
                android.provider.Settings.Secure.putInt(com.android.server.StorageManagerService.this.mContext.getContentResolver(), "KEY_WHATS_APP_EXIST", 1);
            } catch (java.lang.Exception e) {
                android.util.Slog.v(com.android.server.StorageManagerService.TAG, "clearCache Exception");
            }
        }
    }

    public com.android.internal.os.AppFuseMount mountProxyFileDescriptorBridge() {
        com.android.internal.os.AppFuseMount appFuseMount;
        android.util.Slog.v(TAG, "mountProxyFileDescriptorBridge");
        int uid = android.os.Binder.getCallingUid();
        while (true) {
            synchronized (this.mAppFuseLock) {
                boolean newlyCreated = false;
                if (this.mAppFuseBridge == null) {
                    this.mAppFuseBridge = new com.android.server.storage.AppFuseBridge();
                    new java.lang.Thread(this.mAppFuseBridge, com.android.server.storage.AppFuseBridge.TAG).start();
                    newlyCreated = true;
                }
                try {
                    int name = this.mNextAppFuseName;
                    this.mNextAppFuseName = name + 1;
                    try {
                        appFuseMount = new com.android.internal.os.AppFuseMount(name, this.mAppFuseBridge.addBridge(new com.android.server.StorageManagerService.AppFuseMountScope(uid, name)));
                    } catch (com.android.internal.os.FuseUnavailableMountException e) {
                        if (newlyCreated) {
                            android.util.Slog.e(TAG, "", e);
                            return null;
                        }
                        this.mAppFuseBridge = null;
                    }
                } catch (com.android.server.AppFuseMountException e2) {
                    throw e2.rethrowAsParcelableException();
                }
            }
            return appFuseMount;
        }
    }

    public void startserviceAppFuse() {
        if (isAutoSaveUid()) {
            android.util.Slog.v(TAG, "startserviceAppFuse in StorageManagerService");
            int uid = android.os.Binder.getCallingUid();
            synchronized (this.mAppFuseLock) {
                boolean newlyCreated = false;
                if (this.mAppFuseBridge == null) {
                    this.mAppFuseBridge = new com.android.server.storage.AppFuseBridge();
                    new java.lang.Thread(this.mAppFuseBridge, com.android.server.storage.AppFuseBridge.TAG).start();
                    newlyCreated = true;
                }
                try {
                    int name = this.mNextAppFuseName;
                    this.mNextAppFuseName = name + 1;
                    try {
                        this.mAppFuseBridge.startserviceAppFuse(new com.android.server.StorageManagerService.AppFuseMountScope(uid, name));
                    } catch (com.android.internal.os.FuseUnavailableMountException e) {
                        if (newlyCreated) {
                            android.util.Slog.e(TAG, "", e);
                        }
                        this.mAppFuseBridge = null;
                    }
                } catch (com.android.server.AppFuseMountException e2) {
                    throw e2.rethrowAsParcelableException();
                }
            }
            return;
        }
        android.util.Log.w(TAG, "startserviceAppFuse not authorized");
    }

    public void stopserviceAppFuse() {
        if (isAutoSaveUid()) {
            android.util.Slog.v(TAG, "stopserviceAppFuse in StorageManagerService");
            int uid = android.os.Binder.getCallingUid();
            synchronized (this.mAppFuseLock) {
                boolean newlyCreated = false;
                if (this.mAppFuseBridge == null) {
                    this.mAppFuseBridge = new com.android.server.storage.AppFuseBridge();
                    new java.lang.Thread(this.mAppFuseBridge, com.android.server.storage.AppFuseBridge.TAG).start();
                    newlyCreated = true;
                }
                try {
                    int name = this.mNextAppFuseName;
                    this.mNextAppFuseName = name + 1;
                    try {
                        this.mAppFuseBridge.stopserviceAppFuse(new com.android.server.StorageManagerService.AppFuseMountScope(uid, name));
                    } catch (com.android.internal.os.FuseUnavailableMountException e) {
                        if (newlyCreated) {
                            android.util.Slog.e(TAG, "", e);
                        }
                        this.mAppFuseBridge = null;
                    }
                } catch (com.android.server.AppFuseMountException e2) {
                    throw e2.rethrowAsParcelableException();
                }
            }
            return;
        }
        android.util.Log.w(TAG, "stopserviceAppFuse not authorized");
    }

    public void clearCache() {
        if (isAutoSaveUid()) {
            android.util.Slog.v(TAG, "clearCache in StorageManagerService");
            int uid = android.os.Binder.getCallingUid();
            synchronized (this.mAppFuseLock) {
                boolean newlyCreated = false;
                if (this.mAppFuseBridge == null) {
                    this.mAppFuseBridge = new com.android.server.storage.AppFuseBridge();
                    new java.lang.Thread(this.mAppFuseBridge, com.android.server.storage.AppFuseBridge.TAG).start();
                    newlyCreated = true;
                }
                try {
                    int name = this.mNextAppFuseName;
                    this.mNextAppFuseName = name + 1;
                    try {
                        this.mAppFuseBridge.clearCache(new com.android.server.StorageManagerService.AppFuseMountScope(uid, name));
                    } catch (com.android.internal.os.FuseUnavailableMountException e) {
                        if (newlyCreated) {
                            android.util.Slog.e(TAG, "", e);
                        }
                        this.mAppFuseBridge = null;
                    }
                } catch (com.android.server.AppFuseMountException e2) {
                    throw e2.rethrowAsParcelableException();
                }
            }
            return;
        }
        android.util.Log.w(TAG, "clearCache not authorized");
    }

    public android.os.ParcelFileDescriptor openProxyFileDescriptor(int mountId, int fileId, int mode) {
        android.util.Slog.v(TAG, "mountProxyFileDescriptor");
        int mode2 = mode & 805306368;
        try {
            synchronized (this.mAppFuseLock) {
                if (this.mAppFuseBridge == null) {
                    android.util.Slog.e(TAG, "FuseBridge has not been created");
                    return null;
                }
                return this.mAppFuseBridge.openFile(mountId, fileId, mode2);
            }
        } catch (com.android.internal.os.FuseUnavailableMountException | java.lang.InterruptedException e) {
            android.util.Slog.v(TAG, "The mount point has already been invalid", e);
            return null;
        }
    }

    public void mkdirs(java.lang.String callingPkg, java.lang.String appPath) {
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getUserId(callingUid);
        java.lang.String propertyName = "sys.user." + userId + ".ce_available";
        if (!isCeStorageUnlocked(userId)) {
            throw new java.lang.IllegalStateException("Failed to prepare " + appPath);
        }
        if (userId == 0 && !android.os.SystemProperties.getBoolean(propertyName, false)) {
            throw new java.lang.IllegalStateException("Failed to prepare " + appPath);
        }
        android.app.AppOpsManager appOps = (android.app.AppOpsManager) this.mContext.getSystemService("appops");
        appOps.checkPackage(callingUid, callingPkg);
        try {
            android.content.pm.PackageManager.Property noAppStorageProp = this.mContext.getPackageManager().getPropertyAsUser("android.internal.PROPERTY_NO_APP_DATA_STORAGE", callingPkg, null, userId);
            if (noAppStorageProp != null && noAppStorageProp.getBoolean()) {
                throw new java.lang.SecurityException(callingPkg + " should not have " + appPath);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
        try {
            java.io.File appFile = new java.io.File(appPath).getCanonicalFile();
            java.lang.String appPath2 = appFile.getAbsolutePath();
            if (!appPath2.endsWith(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER)) {
                appPath2 = appPath2 + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER;
            }
            java.util.regex.Matcher matcher = KNOWN_APP_DIR_PATHS.matcher(appPath2);
            if (matcher.matches()) {
                if (!matcher.group(3).equals(callingPkg)) {
                    throw new java.lang.SecurityException("Invalid mkdirs path: " + appFile + " does not contain calling package " + callingPkg);
                }
                if ((matcher.group(2) != null && !matcher.group(2).equals(java.lang.Integer.toString(userId))) || (matcher.group(2) == null && userId != this.mCurrentUserId)) {
                    throw new java.lang.SecurityException("Invalid mkdirs path: " + appFile + " does not match calling user id " + userId);
                }
                try {
                    this.mVold.setupAppDir(appPath2, callingUid);
                    return;
                } catch (android.os.RemoteException e2) {
                    throw new java.lang.IllegalStateException("Failed to prepare " + appPath2 + ": " + e2);
                }
            }
            throw new java.lang.SecurityException("Invalid mkdirs path: " + appFile + " is not a known app path.");
        } catch (java.io.IOException e3) {
            throw new java.lang.IllegalStateException("Failed to resolve " + appPath + ": " + e3);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x028b  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x02b2  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0267 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0169 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0150 A[Catch: all -> 0x013b, TryCatch #12 {all -> 0x013b, blocks: (B:56:0x0121, B:69:0x0150, B:72:0x0158, B:81:0x016f, B:84:0x0177, B:86:0x017d, B:88:0x0183, B:91:0x018b, B:60:0x012a), top: B:233:0x0121 }] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x019d  */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 6 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.os.storage.StorageVolume[] getVolumeList(int r52, java.lang.String r53, int r54) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1124
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.StorageManagerService.getVolumeList(int, java.lang.String, int):android.os.storage.StorageVolume[]");
    }

    public android.os.storage.DiskInfo[] getDisks() {
        android.os.storage.DiskInfo[] res;
        synchronized (this.mLock) {
            res = new android.os.storage.DiskInfo[this.mDisks.size()];
            for (int i = 0; i < this.mDisks.size(); i++) {
                res[i] = this.mDisks.valueAt(i);
            }
        }
        return res;
    }

    public android.os.storage.VolumeInfo[] getVolumes(int flags) {
        android.os.storage.VolumeInfo[] res;
        synchronized (this.mLock) {
            res = new android.os.storage.VolumeInfo[this.mVolumes.size()];
            for (int i = 0; i < this.mVolumes.size(); i++) {
                res[i] = this.mVolumes.valueAt(i);
            }
        }
        return res;
    }

    public android.os.storage.VolumeRecord[] getVolumeRecords(int flags) {
        android.os.storage.VolumeRecord[] res;
        synchronized (this.mLock) {
            res = new android.os.storage.VolumeRecord[this.mRecords.size()];
            for (int i = 0; i < this.mRecords.size(); i++) {
                res[i] = this.mRecords.valueAt(i);
            }
        }
        return res;
    }

    public long getCacheQuotaBytes(java.lang.String volumeUuid, int uid) {
        if (uid != android.os.Binder.getCallingUid()) {
            this.mContext.enforceCallingPermission("android.permission.STORAGE_INTERNAL", TAG);
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.app.usage.StorageStatsManager stats = (android.app.usage.StorageStatsManager) this.mContext.getSystemService(android.app.usage.StorageStatsManager.class);
            return stats.getCacheQuotaBytes(volumeUuid, uid);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public long getCacheSizeBytes(java.lang.String volumeUuid, int uid) {
        if (uid != android.os.Binder.getCallingUid()) {
            this.mContext.enforceCallingPermission("android.permission.STORAGE_INTERNAL", TAG);
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                return ((android.app.usage.StorageStatsManager) this.mContext.getSystemService(android.app.usage.StorageStatsManager.class)).queryStatsForUid(volumeUuid, uid).getCacheBytes();
            } catch (java.io.IOException e) {
                throw new android.os.ParcelableException(e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private int adjustAllocateFlags(int flags, int callingUid, java.lang.String callingPackage) {
        if ((flags & 1) != 0) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.ALLOCATE_AGGRESSIVE", TAG);
        }
        int flags2 = flags & (-3) & (-5);
        android.app.AppOpsManager appOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (appOps.isOperationActive(26, callingUid, callingPackage)) {
                android.util.Slog.d(TAG, "UID " + callingUid + " is actively using camera; letting them defy reserved cached data");
                flags2 |= 4;
            }
            return flags2;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public long getAllocatableBytes(java.lang.String volumeUuid, int flags, java.lang.String callingPackage) {
        int flags2 = adjustAllocateFlags(flags, android.os.Binder.getCallingUid(), callingPackage);
        android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class);
        android.app.usage.StorageStatsManager stats = (android.app.usage.StorageStatsManager) this.mContext.getSystemService(android.app.usage.StorageStatsManager.class);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                java.io.File path = storage.findPathForUuid(volumeUuid);
                long usable = 0;
                long lowReserved = 0;
                long fullReserved = 0;
                long cacheClearable = 0;
                if ((flags2 & 16) == 0) {
                    usable = path.getUsableSpace();
                    lowReserved = storage.getStorageLowBytes(path);
                    fullReserved = storage.getStorageFullBytes(path);
                }
                long lowReserved2 = lowReserved;
                if ((flags2 & 8) == 0 && stats.isQuotaSupported(volumeUuid)) {
                    long cacheTotal = stats.getCacheBytes(volumeUuid);
                    long cacheReserved = storage.getStorageCacheBytes(path, flags2);
                    cacheClearable = java.lang.Math.max(0L, cacheTotal - cacheReserved);
                }
                return (flags2 & 1) != 0 ? java.lang.Math.max(0L, (usable + cacheClearable) - fullReserved) : java.lang.Math.max(0L, (usable + cacheClearable) - lowReserved2);
            } catch (java.io.IOException e) {
                throw new android.os.ParcelableException(e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public void allocateBytes(java.lang.String volumeUuid, long bytes, int flags, java.lang.String callingPackage) throws android.os.ParcelableException {
        long bytes2;
        int flags2 = adjustAllocateFlags(flags, android.os.Binder.getCallingUid(), callingPackage);
        long allocatableBytes = getAllocatableBytes(volumeUuid, flags2 | 8, callingPackage);
        if (bytes > allocatableBytes) {
            long cacheClearable = getAllocatableBytes(volumeUuid, flags2 | 16, callingPackage);
            if (bytes > allocatableBytes + cacheClearable) {
                throw new android.os.ParcelableException(new java.io.IOException("Failed to allocate " + bytes + " because only " + (allocatableBytes + cacheClearable) + " allocatable"));
            }
        }
        android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                java.io.File path = storage.findPathForUuid(volumeUuid);
                if ((flags2 & 1) != 0) {
                    bytes2 = bytes + storage.getStorageFullBytes(path);
                } else {
                    bytes2 = bytes + storage.getStorageLowBytes(path);
                }
                this.mPmInternal.freeStorage(volumeUuid, bytes2, flags2);
            } catch (java.io.IOException e) {
                throw new android.os.ParcelableException(e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addObbStateLocked(com.android.server.StorageManagerService.ObbState obbState) throws android.os.RemoteException {
        android.os.IBinder binder = obbState.getBinder();
        java.util.List<com.android.server.StorageManagerService.ObbState> obbStates = this.mObbMounts.get(binder);
        if (obbStates == null) {
            obbStates = new java.util.ArrayList();
            this.mObbMounts.put(binder, obbStates);
        } else {
            for (com.android.server.StorageManagerService.ObbState o : obbStates) {
                if (o.rawPath.equals(obbState.rawPath)) {
                    throw new java.lang.IllegalStateException("Attempt to add ObbState twice. This indicates an error in the StorageManagerService logic.");
                }
            }
        }
        obbStates.add(obbState);
        try {
            obbState.link();
            this.mObbPathToStateMap.put(obbState.rawPath, obbState);
        } catch (android.os.RemoteException e) {
            obbStates.remove(obbState);
            if (obbStates.isEmpty()) {
                this.mObbMounts.remove(binder);
            }
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeObbStateLocked(com.android.server.StorageManagerService.ObbState obbState) {
        android.os.IBinder binder = obbState.getBinder();
        java.util.List<com.android.server.StorageManagerService.ObbState> obbStates = this.mObbMounts.get(binder);
        if (obbStates != null) {
            if (obbStates.remove(obbState)) {
                obbState.unlink();
            }
            if (obbStates.isEmpty()) {
                this.mObbMounts.remove(binder);
            }
        }
        this.mObbPathToStateMap.remove(obbState.rawPath);
    }

    private class ObbActionHandler extends android.os.Handler {
        ObbActionHandler(android.os.Looper l) {
            super(l);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.StorageManagerService.ObbAction action = (com.android.server.StorageManagerService.ObbAction) msg.obj;
                    action.execute(this);
                    return;
                case 2:
                    java.lang.String path = (java.lang.String) msg.obj;
                    synchronized (com.android.server.StorageManagerService.this.mObbMounts) {
                        java.util.List<com.android.server.StorageManagerService.ObbState> obbStatesToRemove = new java.util.ArrayList<>();
                        for (com.android.server.StorageManagerService.ObbState state : com.android.server.StorageManagerService.this.mObbPathToStateMap.values()) {
                            if (state.canonicalPath.startsWith(path)) {
                                obbStatesToRemove.add(state);
                            }
                        }
                        for (com.android.server.StorageManagerService.ObbState obbState : obbStatesToRemove) {
                            com.android.server.StorageManagerService.this.removeObbStateLocked(obbState);
                            try {
                                obbState.token.onObbResult(obbState.rawPath, obbState.nonce, 2);
                            } catch (android.os.RemoteException e) {
                                android.util.Slog.i(com.android.server.StorageManagerService.TAG, "Couldn't send unmount notification for  OBB: " + obbState.rawPath);
                            }
                        }
                        break;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private static class ObbException extends java.lang.Exception {
        public final int status;

        public ObbException(int status, java.lang.String message) {
            super(message);
            this.status = status;
        }

        public ObbException(int status, java.lang.Throwable cause) {
            super(cause.getMessage(), cause);
            this.status = status;
        }
    }

    private static abstract class ObbAction {
        com.android.server.StorageManagerService.ObbState mObbState;

        abstract void handleExecute() throws com.android.server.StorageManagerService.ObbException;

        ObbAction(com.android.server.StorageManagerService.ObbState obbState) {
            this.mObbState = obbState;
        }

        public void execute(com.android.server.StorageManagerService.ObbActionHandler handler) {
            try {
                handleExecute();
            } catch (com.android.server.StorageManagerService.ObbException e) {
                notifyObbStateChange(e);
            }
        }

        protected void notifyObbStateChange(com.android.server.StorageManagerService.ObbException e) {
            android.util.Slog.w(com.android.server.StorageManagerService.TAG, e);
            notifyObbStateChange(e.status);
        }

        protected void notifyObbStateChange(int status) {
            if (this.mObbState == null || this.mObbState.token == null) {
                return;
            }
            try {
                this.mObbState.token.onObbResult(this.mObbState.rawPath, this.mObbState.nonce, status);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.StorageManagerService.TAG, "StorageEventListener went away while calling onObbStateChanged");
            }
        }
    }

    class MountObbAction extends com.android.server.StorageManagerService.ObbAction {
        private final int mCallingUid;
        private android.content.res.ObbInfo mObbInfo;

        MountObbAction(com.android.server.StorageManagerService.ObbState obbState, int callingUid, android.content.res.ObbInfo obbInfo) {
            super(obbState);
            this.mCallingUid = callingUid;
            this.mObbInfo = obbInfo;
        }

        @Override // com.android.server.StorageManagerService.ObbAction
        public void handleExecute() throws com.android.server.StorageManagerService.ObbException {
            boolean isMounted;
            com.android.server.StorageManagerService.this.warnOnNotMounted();
            if (!com.android.server.StorageManagerService.this.isUidOwnerOfPackageOrSystem(this.mObbInfo.packageName, this.mCallingUid)) {
                throw new com.android.server.StorageManagerService.ObbException(25, "Denied attempt to mount OBB " + this.mObbInfo.filename + " which is owned by " + this.mObbInfo.packageName);
            }
            synchronized (com.android.server.StorageManagerService.this.mObbMounts) {
                isMounted = com.android.server.StorageManagerService.this.mObbPathToStateMap.containsKey(this.mObbState.rawPath);
            }
            if (isMounted) {
                throw new com.android.server.StorageManagerService.ObbException(24, "Attempt to mount OBB which is already mounted: " + this.mObbInfo.filename);
            }
            try {
                this.mObbState.volId = com.android.server.StorageManagerService.this.mVold.createObb(this.mObbState.canonicalPath, this.mObbState.ownerGid);
                com.android.server.StorageManagerService.this.mVold.mount(this.mObbState.volId, 0, -1, null);
                synchronized (com.android.server.StorageManagerService.this.mObbMounts) {
                    com.android.server.StorageManagerService.this.addObbStateLocked(this.mObbState);
                }
                notifyObbStateChange(1);
            } catch (java.lang.Exception e) {
                throw new com.android.server.StorageManagerService.ObbException(21, e);
            }
        }

        public java.lang.String toString() {
            return "MountObbAction{" + this.mObbState + '}';
        }
    }

    class UnmountObbAction extends com.android.server.StorageManagerService.ObbAction {
        private final boolean mForceUnmount;

        UnmountObbAction(com.android.server.StorageManagerService.ObbState obbState, boolean force) {
            super(obbState);
            this.mForceUnmount = force;
        }

        @Override // com.android.server.StorageManagerService.ObbAction
        public void handleExecute() throws com.android.server.StorageManagerService.ObbException {
            com.android.server.StorageManagerService.ObbState existingState;
            com.android.server.StorageManagerService.this.warnOnNotMounted();
            synchronized (com.android.server.StorageManagerService.this.mObbMounts) {
                existingState = (com.android.server.StorageManagerService.ObbState) com.android.server.StorageManagerService.this.mObbPathToStateMap.get(this.mObbState.rawPath);
            }
            if (existingState == null) {
                throw new com.android.server.StorageManagerService.ObbException(23, "Missing existingState");
            }
            if (existingState.ownerGid != this.mObbState.ownerGid) {
                notifyObbStateChange(new com.android.server.StorageManagerService.ObbException(25, "Permission denied to unmount OBB " + existingState.rawPath + " (owned by GID " + existingState.ownerGid + ")"));
                return;
            }
            try {
                com.android.server.StorageManagerService.this.mVold.unmount(this.mObbState.volId);
                com.android.server.StorageManagerService.this.mVold.destroyObb(this.mObbState.volId);
                this.mObbState.volId = null;
                synchronized (com.android.server.StorageManagerService.this.mObbMounts) {
                    com.android.server.StorageManagerService.this.removeObbStateLocked(existingState);
                }
                notifyObbStateChange(2);
            } catch (java.lang.Exception e) {
                throw new com.android.server.StorageManagerService.ObbException(22, e);
            }
        }

        public java.lang.String toString() {
            return "UnmountObbAction{" + this.mObbState + ",force=" + this.mForceUnmount + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchOnStatus(android.os.IVoldTaskListener listener, int status, android.os.PersistableBundle extras) {
        if (listener != null) {
            try {
                listener.onStatus(status, extras);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchOnFinished(android.os.IVoldTaskListener listener, int status, android.os.PersistableBundle extras) {
        if (listener != null) {
            try {
                listener.onFinished(status, extras);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public int getExternalStorageMountMode(int uid, java.lang.String packageName) {
        super.getExternalStorageMountMode_enforcePermission();
        return this.mStorageManagerInternal.getExternalStorageMountMode(uid, packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMountModeInternal(int uid, java.lang.String packageName) {
        android.content.pm.ApplicationInfo ai;
        try {
            if (!android.os.Process.isIsolated(uid) && !android.os.Process.isSdkSandboxUid(uid)) {
                java.lang.String[] packagesForUid = this.mIPackageManager.getPackagesForUid(uid);
                if (com.android.internal.util.ArrayUtils.isEmpty(packagesForUid)) {
                    return 0;
                }
                if (packageName == null) {
                    packageName = packagesForUid[0];
                }
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    if (this.mPmInternal.isInstantApp(packageName, android.os.UserHandle.getUserId(uid))) {
                        return 0;
                    }
                    android.os.Binder.restoreCallingIdentity(token);
                    if (this.mStorageManagerInternal.isExternalStorageService(uid)) {
                        return 3;
                    }
                    if (this.mDownloadsAuthorityAppId != android.os.UserHandle.getAppId(uid) && this.mExternalStorageAuthorityAppId != android.os.UserHandle.getAppId(uid)) {
                        boolean hasMtp = this.mIPackageManager.checkUidPermission("android.permission.ACCESS_MTP", uid) == 0;
                        if ((hasMtp && (ai = this.mIPackageManager.getApplicationInfo(packageName, 0L, android.os.UserHandle.getUserId(uid))) != null && ai.isSignedWithPlatformKey()) || this.mAllFileAccessManager.checkAppWhitelist(packageName, uid)) {
                            return 4;
                        }
                        boolean hasInstall = this.mIPackageManager.checkUidPermission("android.permission.INSTALL_PACKAGES", uid) == 0;
                        boolean hasInstallOp = false;
                        int length = packagesForUid.length;
                        int i = 0;
                        while (true) {
                            if (i >= length) {
                                break;
                            }
                            java.lang.String uidPackageName = packagesForUid[i];
                            if (this.mIAppOpsService.checkOperation(66, uid, uidPackageName) != 0) {
                                i++;
                            } else {
                                hasInstallOp = true;
                                break;
                            }
                        }
                        return (hasInstall || hasInstallOp) ? 2 : 1;
                    }
                    return 4;
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
            return 0;
        } catch (android.os.RemoteException e) {
            return 0;
        }
    }

    java.util.concurrent.CopyOnWriteArrayList<android.os.storage.ICeStorageLockEventListener> getCeStorageEventCallbacks() {
        return this.mCeStorageEventCallbacks;
    }

    void dispatchCeStorageLockedEvent(int userId) {
        for (android.os.storage.ICeStorageLockEventListener listener : this.mCeStorageEventCallbacks) {
            listener.onStorageLocked(userId);
        }
    }

    private static class Callbacks extends android.os.Handler {
        private static final int MSG_DISK_DESTROYED = 6;
        private static final int MSG_DISK_SCANNED = 5;
        private static final int MSG_STORAGE_STATE_CHANGED = 1;
        private static final int MSG_VOLUME_FORGOTTEN = 4;
        private static final int MSG_VOLUME_RECORD_CHANGED = 3;
        private static final int MSG_VOLUME_STATE_CHANGED = 2;
        private final android.os.RemoteCallbackList<android.os.storage.IStorageEventListener> mCallbacks;

        public Callbacks(android.os.Looper looper) {
            super(looper);
            this.mCallbacks = new android.os.RemoteCallbackList<>();
        }

        public void register(android.os.storage.IStorageEventListener callback) {
            this.mCallbacks.register(callback);
        }

        public void unregister(android.os.storage.IStorageEventListener callback) {
            this.mCallbacks.unregister(callback);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
            int n = this.mCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                android.os.storage.IStorageEventListener callback = (android.os.storage.IStorageEventListener) this.mCallbacks.getBroadcastItem(i);
                try {
                    invokeCallback(callback, msg.what, args);
                } catch (android.os.RemoteException e) {
                }
            }
            this.mCallbacks.finishBroadcast();
            args.recycle();
        }

        private void invokeCallback(android.os.storage.IStorageEventListener callback, int what, com.android.internal.os.SomeArgs args) throws android.os.RemoteException {
            switch (what) {
                case 1:
                    callback.onStorageStateChanged((java.lang.String) args.arg1, (java.lang.String) args.arg2, (java.lang.String) args.arg3);
                    break;
                case 2:
                    callback.onVolumeStateChanged((android.os.storage.VolumeInfo) args.arg1, args.argi2, args.argi3);
                    break;
                case 3:
                    callback.onVolumeRecordChanged((android.os.storage.VolumeRecord) args.arg1);
                    break;
                case 4:
                    callback.onVolumeForgotten((java.lang.String) args.arg1);
                    break;
                case 5:
                    callback.onDiskScanned((android.os.storage.DiskInfo) args.arg1, args.argi2);
                    break;
                case 6:
                    callback.onDiskDestroyed((android.os.storage.DiskInfo) args.arg1);
                    break;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyStorageStateChanged(java.lang.String path, java.lang.String oldState, java.lang.String newState) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = path;
            args.arg2 = oldState;
            args.arg3 = newState;
            obtainMessage(1, args).sendToTarget();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyVolumeStateChanged(android.os.storage.VolumeInfo vol, int oldState, int newState) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = vol.clone();
            args.argi2 = oldState;
            args.argi3 = newState;
            obtainMessage(2, args).sendToTarget();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyVolumeRecordChanged(android.os.storage.VolumeRecord rec) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = rec.clone();
            obtainMessage(3, args).sendToTarget();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyVolumeForgotten(java.lang.String fsUuid) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = fsUuid;
            obtainMessage(4, args).sendToTarget();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyDiskScanned(android.os.storage.DiskInfo disk, int volumeCount) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = disk.clone();
            args.argi2 = volumeCount;
            obtainMessage(5, args).sendToTarget();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyDiskDestroyed(android.os.storage.DiskInfo disk) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = disk.clone();
            obtainMessage(6, args).sendToTarget();
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, writer)) {
            com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(writer, "  ", 160);
            synchronized (this.mLock) {
                pw.println("Disks:");
                pw.increaseIndent();
                for (int i = 0; i < this.mDisks.size(); i++) {
                    android.os.storage.DiskInfo disk = this.mDisks.valueAt(i);
                    disk.dump(pw);
                }
                pw.decreaseIndent();
                pw.println();
                pw.println("Volumes:");
                pw.increaseIndent();
                for (int i2 = 0; i2 < this.mVolumes.size(); i2++) {
                    android.os.storage.VolumeInfo vol = this.mVolumes.valueAt(i2);
                    if (!"private".equals(vol.id)) {
                        vol.dump(pw);
                    }
                }
                pw.decreaseIndent();
                pw.println();
                pw.println("Records:");
                pw.increaseIndent();
                for (int i3 = 0; i3 < this.mRecords.size(); i3++) {
                    android.os.storage.VolumeRecord note = this.mRecords.valueAt(i3);
                    note.dump(pw);
                }
                pw.decreaseIndent();
                pw.println();
                pw.println("Primary storage UUID: " + this.mPrimaryStorageUuid);
                pw.println();
                android.util.Pair<java.lang.String, java.lang.Long> pair = android.os.storage.StorageManager.getPrimaryStoragePathAndSize();
                if (pair == null) {
                    pw.println("Internal storage total size: N/A");
                } else {
                    pw.print("Internal storage (");
                    pw.print((java.lang.String) pair.first);
                    pw.print(") total size: ");
                    pw.print(pair.second);
                    pw.print(" (");
                    pw.print(((java.lang.Long) pair.second).longValue() / android.util.DataUnit.MEBIBYTES.toBytes(1L));
                    pw.println(" MiB)");
                }
                pw.println();
                pw.println("CE unlocked users: " + this.mCeUnlockedUsers);
                pw.println("System unlocked users: " + java.util.Arrays.toString(this.mSystemUnlockedUsers));
            }
            synchronized (this.mObbMounts) {
                pw.println();
                pw.println("mObbMounts:");
                pw.increaseIndent();
                for (java.util.Map.Entry<android.os.IBinder, java.util.List<com.android.server.StorageManagerService.ObbState>> e : this.mObbMounts.entrySet()) {
                    pw.println(e.getKey() + ":");
                    pw.increaseIndent();
                    java.util.List<com.android.server.StorageManagerService.ObbState> obbStates = e.getValue();
                    for (com.android.server.StorageManagerService.ObbState obbState : obbStates) {
                        pw.println(obbState);
                    }
                    pw.decreaseIndent();
                }
                pw.decreaseIndent();
                pw.println();
                pw.println("mObbPathToStateMap:");
                pw.increaseIndent();
                for (java.util.Map.Entry<java.lang.String, com.android.server.StorageManagerService.ObbState> e2 : this.mObbPathToStateMap.entrySet()) {
                    pw.print(e2.getKey());
                    pw.print(" -> ");
                    pw.println(e2.getValue());
                }
                pw.decreaseIndent();
            }
            synchronized (this.mCloudMediaProviders) {
                pw.println();
                pw.print("Media cloud providers: ");
                pw.println(this.mCloudMediaProviders);
            }
            pw.println();
            pw.print("Last maintenance: ");
            pw.println(android.util.TimeUtils.formatForLogging(this.mLastMaintenance));
            this.mAllFileAccessManager.dump(writer);
        }
    }

    @Override // com.android.server.Watchdog.Monitor
    public void monitor() {
        try {
            this.mVold.monitor();
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    private final class StorageManagerInternalImpl extends android.os.storage.StorageManagerInternal {
        private final java.util.concurrent.CopyOnWriteArraySet<android.os.storage.StorageManagerInternal.CloudProviderChangeListener> mCloudProviderChangeListeners;
        private final java.util.List<android.os.storage.StorageManagerInternal.ResetListener> mResetListeners;

        private StorageManagerInternalImpl() {
            this.mResetListeners = new java.util.ArrayList();
            this.mCloudProviderChangeListeners = new java.util.concurrent.CopyOnWriteArraySet<>();
        }

        public boolean isFuseMounted(int userId) {
            boolean zContains;
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                zContains = com.android.server.StorageManagerService.this.mFuseMountedUser.contains(java.lang.Integer.valueOf(userId));
            }
            return zContains;
        }

        public boolean prepareStorageDirs(int userId, java.util.Set<java.lang.String> packageList, java.lang.String processName) {
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                if (!com.android.server.StorageManagerService.this.mFuseMountedUser.contains(java.lang.Integer.valueOf(userId))) {
                    android.util.Slog.w(com.android.server.StorageManagerService.TAG, "User " + userId + " is not unlocked yet so skip mounting obb");
                    return false;
                }
                try {
                    android.os.IVold vold = android.os.IVold.Stub.asInterface(android.os.ServiceManager.getServiceOrThrow("vold"));
                    for (java.lang.String pkg : packageList) {
                        java.lang.String packageObbDir = java.lang.String.format(java.util.Locale.US, "/storage/emulated/%d/Android/obb/%s/", java.lang.Integer.valueOf(userId), pkg);
                        java.lang.String packageDataDir = java.lang.String.format(java.util.Locale.US, "/storage/emulated/%d/Android/data/%s/", java.lang.Integer.valueOf(userId), pkg);
                        int appUid = android.os.UserHandle.getUid(userId, com.android.server.StorageManagerService.this.mPmInternal.getPackage(pkg).getUid());
                        vold.ensureAppDirsCreated(new java.lang.String[]{packageObbDir, packageDataDir}, appUid);
                    }
                    return true;
                } catch (android.os.ServiceManager.ServiceNotFoundException | android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.StorageManagerService.TAG, "Unable to create obb and data directories for " + processName, e);
                    return false;
                }
            }
        }

        public int getExternalStorageMountMode(int uid, java.lang.String packageName) {
            int mode = com.android.server.StorageManagerService.this.getMountModeInternal(uid, packageName);
            if (com.android.server.StorageManagerService.LOCAL_LOGV) {
                android.util.Slog.v(com.android.server.StorageManagerService.TAG, "Resolved mode " + mode + " for " + packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.UserHandle.formatUid(uid));
            }
            return mode;
        }

        public boolean hasExternalStorageAccess(int uid, java.lang.String packageName) {
            try {
                int opMode = com.android.server.StorageManagerService.this.mIAppOpsService.checkOperation(92, uid, packageName);
                return opMode == 3 ? com.android.server.StorageManagerService.this.mIPackageManager.checkUidPermission("android.permission.MANAGE_EXTERNAL_STORAGE", uid) == 0 : opMode == 0;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w("Failed to check MANAGE_EXTERNAL_STORAGE access for " + packageName, e);
                return false;
            }
        }

        public void addResetListener(android.os.storage.StorageManagerInternal.ResetListener listener) {
            synchronized (this.mResetListeners) {
                this.mResetListeners.add(listener);
            }
        }

        public void onReset(android.os.IVold vold) {
            synchronized (this.mResetListeners) {
                for (android.os.storage.StorageManagerInternal.ResetListener listener : this.mResetListeners) {
                    listener.onReset(vold);
                }
            }
        }

        public void resetUser(int userId) {
            com.android.server.StorageManagerService.this.mHandler.obtainMessage(10).sendToTarget();
        }

        public boolean hasLegacyExternalStorage(int uid) {
            boolean zContains;
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                zContains = com.android.server.StorageManagerService.this.mUidsWithLegacyExternalStorage.contains(java.lang.Integer.valueOf(uid));
            }
            return zContains;
        }

        public void prepareAppDataAfterInstall(java.lang.String packageName, int uid) {
            int userId = android.os.UserHandle.getUserId(uid);
            android.os.Environment.UserEnvironment userEnv = new android.os.Environment.UserEnvironment(userId);
            java.io.File[] packageObbDirs = userEnv.buildExternalStorageAppObbDirs(packageName);
            for (java.io.File packageObbDir : packageObbDirs) {
                if (packageObbDir.getPath().startsWith(android.os.Environment.getDataPreloadsMediaDirectory().getPath())) {
                    android.util.Slog.i(com.android.server.StorageManagerService.TAG, "Skipping app data preparation for " + packageObbDir);
                } else {
                    try {
                        com.android.server.StorageManagerService.this.mVold.fixupAppDir(packageObbDir.getCanonicalPath() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER, uid);
                    } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
                        android.util.Log.e(com.android.server.StorageManagerService.TAG, "Failed to fixup app dir for " + packageName, e);
                    } catch (java.io.IOException e2) {
                        android.util.Log.e(com.android.server.StorageManagerService.TAG, "Failed to get canonical path for " + packageName);
                    }
                }
            }
        }

        public boolean isExternalStorageService(int uid) {
            return com.android.server.StorageManagerService.this.mMediaStoreAuthorityAppId == android.os.UserHandle.getAppId(uid);
        }

        public void freeCache(java.lang.String volumeUuid, long freeBytes) {
            try {
                com.android.server.StorageManagerService.this.mStorageSessionController.freeCache(volumeUuid, freeBytes);
            } catch (com.android.server.storage.StorageSessionController.ExternalStorageServiceException e) {
                android.util.Log.e(com.android.server.StorageManagerService.TAG, "Failed to free cache of vol : " + volumeUuid, e);
            }
        }

        public boolean hasExternalStorage(int uid, java.lang.String packageName) {
            return uid == 1000 || getExternalStorageMountMode(uid, packageName) != 0;
        }

        private void killAppForOpChange(int code, int uid) {
            android.app.IActivityManager am = android.app.ActivityManager.getService();
            try {
                am.killUid(android.os.UserHandle.getAppId(uid), -1, android.app.AppOpsManager.opToName(code) + " changed.");
            } catch (android.os.RemoteException e) {
            }
        }

        public void onAppOpsChanged(int code, int uid, java.lang.String packageName, int mode, int previousMode) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                switch (code) {
                    case 66:
                        if (previousMode == 0 && mode != 0) {
                            killAppForOpChange(code, uid);
                        }
                        return;
                    case 87:
                        com.android.server.StorageManagerService.this.updateLegacyStorageApps(packageName, uid, mode == 0);
                        return;
                    case 92:
                        if (mode != 0) {
                            killAppForOpChange(code, uid);
                        }
                        return;
                    default:
                        return;
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public java.util.List<java.lang.String> getPrimaryVolumeIds() {
            java.util.List<java.lang.String> primaryVolumeIds = new java.util.ArrayList<>();
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                for (int i = 0; i < com.android.server.StorageManagerService.this.mVolumes.size(); i++) {
                    android.os.storage.VolumeInfo vol = com.android.server.StorageManagerService.this.mVolumes.valueAt(i);
                    if (vol.isPrimary()) {
                        primaryVolumeIds.add(vol.getId());
                    }
                }
            }
            return primaryVolumeIds;
        }

        public void markCeStoragePrepared(int userId) {
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                com.android.server.StorageManagerService.this.mCeStoragePreparedUsers.add(java.lang.Integer.valueOf(userId));
            }
        }

        public boolean isCeStoragePrepared(int userId) {
            boolean zContains;
            synchronized (com.android.server.StorageManagerService.this.mLock) {
                zContains = com.android.server.StorageManagerService.this.mCeStoragePreparedUsers.contains(java.lang.Integer.valueOf(userId));
            }
            return zContains;
        }

        public void registerCloudProviderChangeListener(android.os.storage.StorageManagerInternal.CloudProviderChangeListener listener) {
            this.mCloudProviderChangeListeners.add(listener);
            com.android.server.StorageManagerService.this.mHandler.obtainMessage(16, listener).sendToTarget();
        }

        public void prepareUserStorageForMove(java.lang.String fromVolumeUuid, java.lang.String toVolumeUuid, java.util.List<android.content.pm.UserInfo> users) {
            try {
                com.android.server.StorageManagerService.this.prepareUserStorageForMoveInternal(fromVolumeUuid, toVolumeUuid, users);
            } catch (java.lang.Exception e) {
                throw new java.lang.RuntimeException(e);
            }
        }

        public android.os.IInstalld.IFsveritySetupAuthToken createFsveritySetupAuthToken(android.os.ParcelFileDescriptor authFd, int uid) throws java.io.IOException {
            try {
                return com.android.server.StorageManagerService.this.mInstaller.createFsveritySetupAuthToken(authFd, uid);
            } catch (com.android.server.pm.Installer.InstallerException e) {
                throw new java.io.IOException(e);
            }
        }

        public int enableFsverity(android.os.IInstalld.IFsveritySetupAuthToken authToken, java.lang.String filePath, java.lang.String packageName) throws java.io.IOException {
            try {
                return com.android.server.StorageManagerService.this.mInstaller.enableFsverity(authToken, filePath, packageName);
            } catch (com.android.server.pm.Installer.InstallerException e) {
                throw new java.io.IOException(e);
            }
        }

        public void registerStorageLockEventListener(android.os.storage.ICeStorageLockEventListener listener) {
            boolean registered = com.android.server.StorageManagerService.this.mCeStorageEventCallbacks.add(listener);
            if (!registered) {
                android.util.Slog.w(com.android.server.StorageManagerService.TAG, "Failed to register listener: " + listener);
            }
        }

        public void unregisterStorageLockEventListener(android.os.storage.ICeStorageLockEventListener listener) {
            boolean unregistered = com.android.server.StorageManagerService.this.mCeStorageEventCallbacks.remove(listener);
            if (!unregistered) {
                android.util.Slog.w(com.android.server.StorageManagerService.TAG, "Unregistering " + listener + " that was not registered");
            }
        }
    }

    private boolean isAutoSaveUid() {
        int callingUid = android.os.Binder.getCallingUid();
        int autoSaveUid = getPackageUid(AUTOSAVE_SDCARD_PACKAGE_NAME);
        android.util.Log.w(TAG, "devil test: callingUid: " + callingUid + "  autoSaveUid:" + autoSaveUid);
        return callingUid == autoSaveUid;
    }

    private int getPackageUid(java.lang.String packageName) {
        try {
            int uId = this.mPmInternal.getPackageUid(packageName, 0L, 0);
            return uId;
        } catch (java.lang.Exception e) {
            android.util.Log.w(TAG, "package not found:");
            e.printStackTrace();
            return 0;
        }
    }

    com.android.server.IOplusStorageManagerFeature getOplusStorageFeature() {
        return this.mOplusStorageFeature;
    }
}
