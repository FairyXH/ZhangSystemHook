package android.os;

/* JADX INFO: loaded from: classes.dex */
public interface IVold extends android.os.IInterface {
    public static final int FSTRIM_FLAG_DEEP_TRIM = 1;
    public static final int MOUNT_FLAG_PRIMARY = 1;
    public static final int MOUNT_FLAG_VISIBLE_FOR_READ = 2;
    public static final int MOUNT_FLAG_VISIBLE_FOR_WRITE = 4;
    public static final int PARTITION_TYPE_MIXED = 2;
    public static final int PARTITION_TYPE_PRIVATE = 1;
    public static final int PARTITION_TYPE_PUBLIC = 0;
    public static final int REMOUNT_MODE_ANDROID_WRITABLE = 4;
    public static final int REMOUNT_MODE_DEFAULT = 1;
    public static final int REMOUNT_MODE_INSTALLER = 2;
    public static final int REMOUNT_MODE_NONE = 0;
    public static final int REMOUNT_MODE_OPLUS_ANDROID_WRITABLE = 5;
    public static final int REMOUNT_MODE_PASS_THROUGH = 3;
    public static final int STORAGE_FLAG_CE = 2;
    public static final int STORAGE_FLAG_DE = 1;
    public static final int VOLUME_STATE_BAD_REMOVAL = 8;
    public static final int VOLUME_STATE_CHECKING = 1;
    public static final int VOLUME_STATE_EJECTING = 5;
    public static final int VOLUME_STATE_FORMATTING = 4;
    public static final int VOLUME_STATE_MOUNTED = 2;
    public static final int VOLUME_STATE_MOUNTED_READ_ONLY = 3;
    public static final int VOLUME_STATE_REMOVED = 7;
    public static final int VOLUME_STATE_UNMOUNTABLE = 6;
    public static final int VOLUME_STATE_UNMOUNTED = 0;
    public static final int VOLUME_TYPE_ASEC = 3;
    public static final int VOLUME_TYPE_EMULATED = 2;
    public static final int VOLUME_TYPE_OBB = 4;
    public static final int VOLUME_TYPE_PRIVATE = 1;
    public static final int VOLUME_TYPE_PUBLIC = 0;
    public static final int VOLUME_TYPE_STUB = 5;

    void abortChanges(java.lang.String str, boolean z) throws android.os.RemoteException;

    void abortFuse() throws android.os.RemoteException;

    void abortIdleMaint(android.os.IVoldTaskListener iVoldTaskListener) throws android.os.RemoteException;

    void addAppIds(java.lang.String[] strArr, int[] iArr) throws android.os.RemoteException;

    void addSandboxIds(int[] iArr, java.lang.String[] strArr) throws android.os.RemoteException;

    void benchmark(java.lang.String str, android.os.IVoldTaskListener iVoldTaskListener) throws android.os.RemoteException;

    void bindMount(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void checkBeforeMount(java.lang.String str) throws android.os.RemoteException;

    int clearCache(int i) throws android.os.RemoteException;

    void commitChanges() throws android.os.RemoteException;

    void configDfsFuse(java.lang.String str, int i, int i2) throws android.os.RemoteException;

    java.lang.String createObb(java.lang.String str, int i) throws android.os.RemoteException;

    java.lang.String createStubVolume(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, java.lang.String str5, int i) throws android.os.RemoteException;

    void createUserStorageKeys(int i, boolean z) throws android.os.RemoteException;

    void destroyDsuMetadataKey(java.lang.String str) throws android.os.RemoteException;

    void destroyObb(java.lang.String str) throws android.os.RemoteException;

    void destroySandboxForApp(java.lang.String str, java.lang.String str2, int i) throws android.os.RemoteException;

    void destroyStubVolume(java.lang.String str) throws android.os.RemoteException;

    void destroyUserStorage(java.lang.String str, int i, int i2) throws android.os.RemoteException;

    void destroyUserStorageKeys(int i) throws android.os.RemoteException;

    void earlyBootEnded() throws android.os.RemoteException;

    void encryptFstab(java.lang.String str, java.lang.String str2, boolean z, java.lang.String str3, boolean z2, java.lang.String[] strArr) throws android.os.RemoteException;

    void ensureAppDirsCreated(java.lang.String[] strArr, int i) throws android.os.RemoteException;

    byte[] exportSensitiveBePublicKey(int i, int i2) throws android.os.RemoteException;

    byte[] exportSensitiveKey(int i, int i2, boolean z) throws android.os.RemoteException;

    void fbeEnable() throws android.os.RemoteException;

    void fixupAppDir(java.lang.String str, int i) throws android.os.RemoteException;

    void forgetPartition(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void format(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void fstrim(int i, android.os.IVoldTaskListener iVoldTaskListener) throws android.os.RemoteException;

    void fsyncCtrl(java.lang.String str) throws android.os.RemoteException;

    int getStorageLifeTime() throws android.os.RemoteException;

    int getStorageRemainingLifetime() throws android.os.RemoteException;

    long getStorageSize() throws android.os.RemoteException;

    int[] getUnlockedUsers() throws android.os.RemoteException;

    int getWriteAmount() throws android.os.RemoteException;

    boolean incFsEnabled() throws android.os.RemoteException;

    void initUser0() throws android.os.RemoteException;

    boolean isCheckpointing() throws android.os.RemoteException;

    void lockCeStorage(int i) throws android.os.RemoteException;

    void markBootAttempt() throws android.os.RemoteException;

    void monitor() throws android.os.RemoteException;

    void mount(java.lang.String str, int i, int i2, android.os.IVoldMountCallback iVoldMountCallback) throws android.os.RemoteException;

    java.io.FileDescriptor mountAppFuse(int i, int i2) throws android.os.RemoteException;

    java.io.FileDescriptor mountDfsFuse(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void mountFstab(java.lang.String str, java.lang.String str2, boolean z, java.lang.String[] strArr) throws android.os.RemoteException;

    android.os.incremental.IncrementalFileSystemControlParcel mountIncFs(java.lang.String str, java.lang.String str2, int i, java.lang.String str3) throws android.os.RemoteException;

    int mountTmpStor(java.lang.String str, int i, int i2) throws android.os.RemoteException;

    void moveStorage(java.lang.String str, java.lang.String str2, android.os.IVoldTaskListener iVoldTaskListener) throws android.os.RemoteException;

    boolean needsCheckpoint() throws android.os.RemoteException;

    boolean needsRollback() throws android.os.RemoteException;

    void onSecureKeyguardStateChanged(boolean z) throws android.os.RemoteException;

    void onSecureKeyguardStateChangedForSensitiveFile(boolean z, int i, int i2) throws android.os.RemoteException;

    void onUserAdded(int i, int i2, int i3) throws android.os.RemoteException;

    void onUserRemoved(int i) throws android.os.RemoteException;

    void onUserStarted(int i) throws android.os.RemoteException;

    void onUserStopped(int i) throws android.os.RemoteException;

    java.io.FileDescriptor openAppFuseFile(int i, int i2, int i3, int i4) throws android.os.RemoteException;

    void partition(java.lang.String str, int i, int i2) throws android.os.RemoteException;

    void prepareCheckpoint() throws android.os.RemoteException;

    void prepareSandboxForApp(java.lang.String str, int i, java.lang.String str2, int i2) throws android.os.RemoteException;

    void prepareUserStorage(java.lang.String str, int i, int i2) throws android.os.RemoteException;

    void refreshLatestWrite() throws android.os.RemoteException;

    void remountAppStorageDirs(int i, int i2, java.lang.String[] strArr) throws android.os.RemoteException;

    void remountUid(int i, int i2) throws android.os.RemoteException;

    void reset() throws android.os.RemoteException;

    void resetCheckpoint() throws android.os.RemoteException;

    void restoreCheckpoint(java.lang.String str) throws android.os.RemoteException;

    void restoreCheckpointPart(java.lang.String str, int i) throws android.os.RemoteException;

    void runIdleMaint(boolean z, android.os.IVoldTaskListener iVoldTaskListener) throws android.os.RemoteException;

    void sdlockClearPassword(java.lang.String str) throws android.os.RemoteException;

    void sdlockErase() throws android.os.RemoteException;

    java.lang.String sdlockGetCid() throws android.os.RemoteException;

    java.lang.String sdlockPoll() throws android.os.RemoteException;

    void sdlockSetPassword(java.lang.String str) throws android.os.RemoteException;

    void sdlockUnlock(java.lang.String str) throws android.os.RemoteException;

    void setCeStorageProtection(int i, byte[] bArr) throws android.os.RemoteException;

    void setGCUrgentPace(int i, int i2, float f, float f2, int i3, int i4, int i5) throws android.os.RemoteException;

    void setIncFsMountOptions(android.os.incremental.IncrementalFileSystemControlParcel incrementalFileSystemControlParcel, boolean z, boolean z2, java.lang.String str) throws android.os.RemoteException;

    void setListener(android.os.IVoldListener iVoldListener) throws android.os.RemoteException;

    void setStorageBindingSeed(byte[] bArr) throws android.os.RemoteException;

    void setupAppDir(java.lang.String str, int i) throws android.os.RemoteException;

    void shutdown() throws android.os.RemoteException;

    void startCheckpoint(int i) throws android.os.RemoteException;

    int startserviceAppFuse() throws android.os.RemoteException;

    int stopserviceAppFuse() throws android.os.RemoteException;

    boolean supportsBlockCheckpoint() throws android.os.RemoteException;

    boolean supportsCheckpoint() throws android.os.RemoteException;

    boolean supportsFileCheckpoint() throws android.os.RemoteException;

    void ufsHid() throws android.os.RemoteException;

    int umountDfsFuse(java.lang.String str) throws android.os.RemoteException;

    void unlockCeStorage(int i, byte[] bArr) throws android.os.RemoteException;

    void unlockSensitiveKey(int i, int i2, java.lang.String str, java.lang.String str2, int i3) throws android.os.RemoteException;

    void unmount(java.lang.String str) throws android.os.RemoteException;

    void unmountAppFuse(int i, int i2) throws android.os.RemoteException;

    void unmountAppStorageDirs(int i, int i2, java.lang.String[] strArr) throws android.os.RemoteException;

    void unmountIncFs(java.lang.String str) throws android.os.RemoteException;

    boolean unmountTmpStor(java.lang.String str, int i) throws android.os.RemoteException;

    void voldTBExt() throws android.os.RemoteException;

    public static class Default implements android.os.IVold {
        @Override // android.os.IVold
        public void setListener(android.os.IVoldListener listener) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void abortFuse() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void monitor() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void reset() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void shutdown() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void onUserAdded(int userId, int userSerial, int sharesStorageWithUserId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void onUserRemoved(int userId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void onUserStarted(int userId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void onUserStopped(int userId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void addAppIds(java.lang.String[] packageNames, int[] appIds) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void addSandboxIds(int[] appIds, java.lang.String[] sandboxIds) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void onSecureKeyguardStateChanged(boolean isShowing) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void partition(java.lang.String diskId, int partitionType, int ratio) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void forgetPartition(java.lang.String partGuid, java.lang.String fsUuid) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void mount(java.lang.String volId, int mountFlags, int mountUserId, android.os.IVoldMountCallback callback) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void unmount(java.lang.String volId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void format(java.lang.String volId, java.lang.String fsType) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void benchmark(java.lang.String volId, android.os.IVoldTaskListener listener) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void checkBeforeMount(java.lang.String volId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void fsyncCtrl(java.lang.String fsyncMode) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void unlockSensitiveKey(int userId, int userSerial, java.lang.String token, java.lang.String secret, int sensitiveType) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void onSecureKeyguardStateChangedForSensitiveFile(boolean isShowing, int userId, int sensitiveType) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public byte[] exportSensitiveKey(int userId, int sensitiveType, boolean useDefault) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public byte[] exportSensitiveBePublicKey(int userId, int sensitiveType) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void moveStorage(java.lang.String fromVolId, java.lang.String toVolId, android.os.IVoldTaskListener listener) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void remountUid(int uid, int remountMode) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void remountAppStorageDirs(int uid, int pid, java.lang.String[] packageNames) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void unmountAppStorageDirs(int uid, int pid, java.lang.String[] packageNames) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void setupAppDir(java.lang.String path, int appUid) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void fixupAppDir(java.lang.String path, int appUid) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void ensureAppDirsCreated(java.lang.String[] paths, int appUid) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public java.lang.String createObb(java.lang.String sourcePath, int ownerGid) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void destroyObb(java.lang.String volId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void fstrim(int fstrimFlags, android.os.IVoldTaskListener listener) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void runIdleMaint(boolean needGC, android.os.IVoldTaskListener listener) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void abortIdleMaint(android.os.IVoldTaskListener listener) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public int getStorageLifeTime() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public void setGCUrgentPace(int neededSegments, int minSegmentThreshold, float dirtyReclaimRate, float reclaimWeight, int gcPeriod, int minGCSleepTime, int targetDirtyRatio) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void refreshLatestWrite() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public int getWriteAmount() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public java.io.FileDescriptor mountAppFuse(int uid, int mountId) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void unmountAppFuse(int uid, int mountId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public int startserviceAppFuse() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public int stopserviceAppFuse() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public int clearCache(int whatsAppExist) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public void fbeEnable() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void initUser0() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void mountFstab(java.lang.String blkDevice, java.lang.String mountPoint, boolean isZoned, java.lang.String[] userDevices) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void encryptFstab(java.lang.String blkDevice, java.lang.String mountPoint, boolean shouldFormat, java.lang.String fsType, boolean isZoned, java.lang.String[] userDevices) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void setStorageBindingSeed(byte[] seed) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void createUserStorageKeys(int userId, boolean ephemeral) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void destroyUserStorageKeys(int userId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void setCeStorageProtection(int userId, byte[] secret) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public int[] getUnlockedUsers() throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void unlockCeStorage(int userId, byte[] secret) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void lockCeStorage(int userId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void prepareUserStorage(java.lang.String uuid, int userId, int storageFlags) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void destroyUserStorage(java.lang.String uuid, int userId, int storageFlags) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void prepareSandboxForApp(java.lang.String packageName, int appId, java.lang.String sandboxId, int userId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void destroySandboxForApp(java.lang.String packageName, java.lang.String sandboxId, int userId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void startCheckpoint(int retry) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public boolean needsCheckpoint() throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public boolean needsRollback() throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public boolean isCheckpointing() throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public void abortChanges(java.lang.String device, boolean retry) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void commitChanges() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void prepareCheckpoint() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void restoreCheckpoint(java.lang.String device) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void restoreCheckpointPart(java.lang.String device, int count) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void markBootAttempt() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public boolean supportsCheckpoint() throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public boolean supportsBlockCheckpoint() throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public boolean supportsFileCheckpoint() throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public void resetCheckpoint() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void earlyBootEnded() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public java.lang.String createStubVolume(java.lang.String sourcePath, java.lang.String mountPath, java.lang.String fsType, java.lang.String fsUuid, java.lang.String fsLabel, int flags) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void destroyStubVolume(java.lang.String volId) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public java.io.FileDescriptor openAppFuseFile(int uid, int mountId, int fileId, int flags) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public boolean incFsEnabled() throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public android.os.incremental.IncrementalFileSystemControlParcel mountIncFs(java.lang.String backingPath, java.lang.String targetDir, int flags, java.lang.String sysfsName) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void unmountIncFs(java.lang.String dir) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void setIncFsMountOptions(android.os.incremental.IncrementalFileSystemControlParcel control, boolean enableReadLogs, boolean enableReadTimeouts, java.lang.String sysfsName) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void bindMount(java.lang.String sourceDir, java.lang.String targetDir) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void sdlockSetPassword(java.lang.String password) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void sdlockClearPassword(java.lang.String password) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void sdlockUnlock(java.lang.String password) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public java.lang.String sdlockGetCid() throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public java.lang.String sdlockPoll() throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void sdlockErase() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void voldTBExt() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public java.io.FileDescriptor mountDfsFuse(java.lang.String fuse_path, java.lang.String options) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public int umountDfsFuse(java.lang.String fuse_path) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public void configDfsFuse(java.lang.String fuse_path, int read_ahead_blocks, int max_dirty_ratio) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public void destroyDsuMetadataKey(java.lang.String dsuSlot) throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public long getStorageSize() throws android.os.RemoteException {
            return 0L;
        }

        @Override // android.os.IVold
        public void ufsHid() throws android.os.RemoteException {
        }

        @Override // android.os.IVold
        public int getStorageRemainingLifetime() throws android.os.RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public int mountTmpStor(java.lang.String mountPoint, int maxSize, int connect_id) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public boolean unmountTmpStor(java.lang.String mountPoint, int connect_id) throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.os.IVold {
        public static final java.lang.String DESCRIPTOR = "android.os.IVold";
        static final int TRANSACTION_abortChanges = 65;
        static final int TRANSACTION_abortFuse = 2;
        static final int TRANSACTION_abortIdleMaint = 36;
        static final int TRANSACTION_addAppIds = 10;
        static final int TRANSACTION_addSandboxIds = 11;
        static final int TRANSACTION_benchmark = 18;
        static final int TRANSACTION_bindMount = 83;
        static final int TRANSACTION_checkBeforeMount = 19;
        static final int TRANSACTION_clearCache = 45;
        static final int TRANSACTION_commitChanges = 66;
        static final int TRANSACTION_configDfsFuse = 93;
        static final int TRANSACTION_createObb = 32;
        static final int TRANSACTION_createStubVolume = 76;
        static final int TRANSACTION_createUserStorageKeys = 51;
        static final int TRANSACTION_destroyDsuMetadataKey = 94;
        static final int TRANSACTION_destroyObb = 33;
        static final int TRANSACTION_destroySandboxForApp = 60;
        static final int TRANSACTION_destroyStubVolume = 77;
        static final int TRANSACTION_destroyUserStorage = 58;
        static final int TRANSACTION_destroyUserStorageKeys = 52;
        static final int TRANSACTION_earlyBootEnded = 75;
        static final int TRANSACTION_encryptFstab = 49;
        static final int TRANSACTION_ensureAppDirsCreated = 31;
        static final int TRANSACTION_exportSensitiveBePublicKey = 24;
        static final int TRANSACTION_exportSensitiveKey = 23;
        static final int TRANSACTION_fbeEnable = 46;
        static final int TRANSACTION_fixupAppDir = 30;
        static final int TRANSACTION_forgetPartition = 14;
        static final int TRANSACTION_format = 17;
        static final int TRANSACTION_fstrim = 34;
        static final int TRANSACTION_fsyncCtrl = 20;
        static final int TRANSACTION_getStorageLifeTime = 37;
        static final int TRANSACTION_getStorageRemainingLifetime = 97;
        static final int TRANSACTION_getStorageSize = 95;
        static final int TRANSACTION_getUnlockedUsers = 54;
        static final int TRANSACTION_getWriteAmount = 40;
        static final int TRANSACTION_incFsEnabled = 79;
        static final int TRANSACTION_initUser0 = 47;
        static final int TRANSACTION_isCheckpointing = 64;
        static final int TRANSACTION_lockCeStorage = 56;
        static final int TRANSACTION_markBootAttempt = 70;
        static final int TRANSACTION_monitor = 3;
        static final int TRANSACTION_mount = 15;
        static final int TRANSACTION_mountAppFuse = 41;
        static final int TRANSACTION_mountDfsFuse = 91;
        static final int TRANSACTION_mountFstab = 48;
        static final int TRANSACTION_mountIncFs = 80;
        static final int TRANSACTION_mountTmpStor = 98;
        static final int TRANSACTION_moveStorage = 25;
        static final int TRANSACTION_needsCheckpoint = 62;
        static final int TRANSACTION_needsRollback = 63;
        static final int TRANSACTION_onSecureKeyguardStateChanged = 12;
        static final int TRANSACTION_onSecureKeyguardStateChangedForSensitiveFile = 22;
        static final int TRANSACTION_onUserAdded = 6;
        static final int TRANSACTION_onUserRemoved = 7;
        static final int TRANSACTION_onUserStarted = 8;
        static final int TRANSACTION_onUserStopped = 9;
        static final int TRANSACTION_openAppFuseFile = 78;
        static final int TRANSACTION_partition = 13;
        static final int TRANSACTION_prepareCheckpoint = 67;
        static final int TRANSACTION_prepareSandboxForApp = 59;
        static final int TRANSACTION_prepareUserStorage = 57;
        static final int TRANSACTION_refreshLatestWrite = 39;
        static final int TRANSACTION_remountAppStorageDirs = 27;
        static final int TRANSACTION_remountUid = 26;
        static final int TRANSACTION_reset = 4;
        static final int TRANSACTION_resetCheckpoint = 74;
        static final int TRANSACTION_restoreCheckpoint = 68;
        static final int TRANSACTION_restoreCheckpointPart = 69;
        static final int TRANSACTION_runIdleMaint = 35;
        static final int TRANSACTION_sdlockClearPassword = 85;
        static final int TRANSACTION_sdlockErase = 89;
        static final int TRANSACTION_sdlockGetCid = 87;
        static final int TRANSACTION_sdlockPoll = 88;
        static final int TRANSACTION_sdlockSetPassword = 84;
        static final int TRANSACTION_sdlockUnlock = 86;
        static final int TRANSACTION_setCeStorageProtection = 53;
        static final int TRANSACTION_setGCUrgentPace = 38;
        static final int TRANSACTION_setIncFsMountOptions = 82;
        static final int TRANSACTION_setListener = 1;
        static final int TRANSACTION_setStorageBindingSeed = 50;
        static final int TRANSACTION_setupAppDir = 29;
        static final int TRANSACTION_shutdown = 5;
        static final int TRANSACTION_startCheckpoint = 61;
        static final int TRANSACTION_startserviceAppFuse = 43;
        static final int TRANSACTION_stopserviceAppFuse = 44;
        static final int TRANSACTION_supportsBlockCheckpoint = 72;
        static final int TRANSACTION_supportsCheckpoint = 71;
        static final int TRANSACTION_supportsFileCheckpoint = 73;
        static final int TRANSACTION_ufsHid = 96;
        static final int TRANSACTION_umountDfsFuse = 92;
        static final int TRANSACTION_unlockCeStorage = 55;
        static final int TRANSACTION_unlockSensitiveKey = 21;
        static final int TRANSACTION_unmount = 16;
        static final int TRANSACTION_unmountAppFuse = 42;
        static final int TRANSACTION_unmountAppStorageDirs = 28;
        static final int TRANSACTION_unmountIncFs = 81;
        static final int TRANSACTION_unmountTmpStor = 99;
        static final int TRANSACTION_voldTBExt = 90;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.os.IVold asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.os.IVold)) {
                return (android.os.IVold) iin;
            }
            return new android.os.IVold.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    android.os.IVoldListener _arg0 = android.os.IVoldListener.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setListener(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    abortFuse();
                    reply.writeNoException();
                    return true;
                case 3:
                    monitor();
                    reply.writeNoException();
                    return true;
                case 4:
                    reset();
                    reply.writeNoException();
                    return true;
                case 5:
                    shutdown();
                    reply.writeNoException();
                    return true;
                case 6:
                    int _arg02 = data.readInt();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    data.enforceNoDataAvail();
                    onUserAdded(_arg02, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 7:
                    int _arg03 = data.readInt();
                    data.enforceNoDataAvail();
                    onUserRemoved(_arg03);
                    reply.writeNoException();
                    return true;
                case 8:
                    int _arg04 = data.readInt();
                    data.enforceNoDataAvail();
                    onUserStarted(_arg04);
                    reply.writeNoException();
                    return true;
                case 9:
                    int _arg05 = data.readInt();
                    data.enforceNoDataAvail();
                    onUserStopped(_arg05);
                    reply.writeNoException();
                    return true;
                case 10:
                    java.lang.String[] _arg06 = data.createStringArray();
                    int[] _arg12 = data.createIntArray();
                    data.enforceNoDataAvail();
                    addAppIds(_arg06, _arg12);
                    reply.writeNoException();
                    return true;
                case 11:
                    int[] _arg07 = data.createIntArray();
                    java.lang.String[] _arg13 = data.createStringArray();
                    data.enforceNoDataAvail();
                    addSandboxIds(_arg07, _arg13);
                    reply.writeNoException();
                    return true;
                case 12:
                    boolean _arg08 = data.readBoolean();
                    data.enforceNoDataAvail();
                    onSecureKeyguardStateChanged(_arg08);
                    reply.writeNoException();
                    return true;
                case 13:
                    java.lang.String _arg09 = data.readString();
                    int _arg14 = data.readInt();
                    int _arg22 = data.readInt();
                    data.enforceNoDataAvail();
                    partition(_arg09, _arg14, _arg22);
                    reply.writeNoException();
                    return true;
                case 14:
                    java.lang.String _arg010 = data.readString();
                    java.lang.String _arg15 = data.readString();
                    data.enforceNoDataAvail();
                    forgetPartition(_arg010, _arg15);
                    reply.writeNoException();
                    return true;
                case 15:
                    java.lang.String _arg011 = data.readString();
                    int _arg16 = data.readInt();
                    int _arg23 = data.readInt();
                    android.os.IVoldMountCallback _arg3 = android.os.IVoldMountCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    mount(_arg011, _arg16, _arg23, _arg3);
                    reply.writeNoException();
                    return true;
                case 16:
                    java.lang.String _arg012 = data.readString();
                    data.enforceNoDataAvail();
                    unmount(_arg012);
                    reply.writeNoException();
                    return true;
                case 17:
                    java.lang.String _arg013 = data.readString();
                    java.lang.String _arg17 = data.readString();
                    data.enforceNoDataAvail();
                    format(_arg013, _arg17);
                    reply.writeNoException();
                    return true;
                case 18:
                    java.lang.String _arg014 = data.readString();
                    android.os.IVoldTaskListener _arg18 = android.os.IVoldTaskListener.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    benchmark(_arg014, _arg18);
                    reply.writeNoException();
                    return true;
                case 19:
                    java.lang.String _arg015 = data.readString();
                    data.enforceNoDataAvail();
                    checkBeforeMount(_arg015);
                    reply.writeNoException();
                    return true;
                case 20:
                    java.lang.String _arg016 = data.readString();
                    data.enforceNoDataAvail();
                    fsyncCtrl(_arg016);
                    reply.writeNoException();
                    return true;
                case 21:
                    int _arg017 = data.readInt();
                    int _arg19 = data.readInt();
                    java.lang.String _arg24 = data.readString();
                    java.lang.String _arg32 = data.readString();
                    int _arg4 = data.readInt();
                    data.enforceNoDataAvail();
                    unlockSensitiveKey(_arg017, _arg19, _arg24, _arg32, _arg4);
                    reply.writeNoException();
                    return true;
                case 22:
                    boolean _arg018 = data.readBoolean();
                    int _arg110 = data.readInt();
                    int _arg25 = data.readInt();
                    data.enforceNoDataAvail();
                    onSecureKeyguardStateChangedForSensitiveFile(_arg018, _arg110, _arg25);
                    reply.writeNoException();
                    return true;
                case 23:
                    int _arg019 = data.readInt();
                    int _arg111 = data.readInt();
                    boolean _arg26 = data.readBoolean();
                    data.enforceNoDataAvail();
                    byte[] _result = exportSensitiveKey(_arg019, _arg111, _arg26);
                    reply.writeNoException();
                    reply.writeByteArray(_result);
                    return true;
                case 24:
                    int _arg020 = data.readInt();
                    int _arg112 = data.readInt();
                    data.enforceNoDataAvail();
                    byte[] _result2 = exportSensitiveBePublicKey(_arg020, _arg112);
                    reply.writeNoException();
                    reply.writeByteArray(_result2);
                    return true;
                case 25:
                    java.lang.String _arg021 = data.readString();
                    java.lang.String _arg113 = data.readString();
                    android.os.IVoldTaskListener _arg27 = android.os.IVoldTaskListener.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    moveStorage(_arg021, _arg113, _arg27);
                    reply.writeNoException();
                    return true;
                case 26:
                    int _arg022 = data.readInt();
                    int _arg114 = data.readInt();
                    data.enforceNoDataAvail();
                    remountUid(_arg022, _arg114);
                    reply.writeNoException();
                    return true;
                case 27:
                    int _arg023 = data.readInt();
                    int _arg115 = data.readInt();
                    java.lang.String[] _arg28 = data.createStringArray();
                    data.enforceNoDataAvail();
                    remountAppStorageDirs(_arg023, _arg115, _arg28);
                    reply.writeNoException();
                    return true;
                case 28:
                    int _arg024 = data.readInt();
                    int _arg116 = data.readInt();
                    java.lang.String[] _arg29 = data.createStringArray();
                    data.enforceNoDataAvail();
                    unmountAppStorageDirs(_arg024, _arg116, _arg29);
                    reply.writeNoException();
                    return true;
                case 29:
                    java.lang.String _arg025 = data.readString();
                    int _arg117 = data.readInt();
                    data.enforceNoDataAvail();
                    setupAppDir(_arg025, _arg117);
                    reply.writeNoException();
                    return true;
                case 30:
                    java.lang.String _arg026 = data.readString();
                    int _arg118 = data.readInt();
                    data.enforceNoDataAvail();
                    fixupAppDir(_arg026, _arg118);
                    reply.writeNoException();
                    return true;
                case 31:
                    java.lang.String[] _arg027 = data.createStringArray();
                    int _arg119 = data.readInt();
                    data.enforceNoDataAvail();
                    ensureAppDirsCreated(_arg027, _arg119);
                    reply.writeNoException();
                    return true;
                case 32:
                    java.lang.String _arg028 = data.readString();
                    int _arg120 = data.readInt();
                    data.enforceNoDataAvail();
                    java.lang.String _result3 = createObb(_arg028, _arg120);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 33:
                    java.lang.String _arg029 = data.readString();
                    data.enforceNoDataAvail();
                    destroyObb(_arg029);
                    reply.writeNoException();
                    return true;
                case 34:
                    int _arg030 = data.readInt();
                    android.os.IVoldTaskListener _arg121 = android.os.IVoldTaskListener.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    fstrim(_arg030, _arg121);
                    reply.writeNoException();
                    return true;
                case 35:
                    boolean _arg031 = data.readBoolean();
                    android.os.IVoldTaskListener _arg122 = android.os.IVoldTaskListener.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    runIdleMaint(_arg031, _arg122);
                    reply.writeNoException();
                    return true;
                case 36:
                    android.os.IVoldTaskListener _arg032 = android.os.IVoldTaskListener.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    abortIdleMaint(_arg032);
                    reply.writeNoException();
                    return true;
                case 37:
                    int _result4 = getStorageLifeTime();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 38:
                    int _arg033 = data.readInt();
                    int _arg123 = data.readInt();
                    float _arg210 = data.readFloat();
                    float _arg33 = data.readFloat();
                    int _arg42 = data.readInt();
                    int _arg5 = data.readInt();
                    int _arg6 = data.readInt();
                    data.enforceNoDataAvail();
                    setGCUrgentPace(_arg033, _arg123, _arg210, _arg33, _arg42, _arg5, _arg6);
                    reply.writeNoException();
                    return true;
                case 39:
                    refreshLatestWrite();
                    reply.writeNoException();
                    return true;
                case 40:
                    int _result5 = getWriteAmount();
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 41:
                    int _arg034 = data.readInt();
                    int _arg124 = data.readInt();
                    data.enforceNoDataAvail();
                    java.io.FileDescriptor _result6 = mountAppFuse(_arg034, _arg124);
                    reply.writeNoException();
                    reply.writeRawFileDescriptor(_result6);
                    return true;
                case 42:
                    int _arg035 = data.readInt();
                    int _arg125 = data.readInt();
                    data.enforceNoDataAvail();
                    unmountAppFuse(_arg035, _arg125);
                    reply.writeNoException();
                    return true;
                case 43:
                    int _result7 = startserviceAppFuse();
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 44:
                    int _result8 = stopserviceAppFuse();
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 45:
                    int _arg036 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result9 = clearCache(_arg036);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 46:
                    fbeEnable();
                    reply.writeNoException();
                    return true;
                case 47:
                    initUser0();
                    reply.writeNoException();
                    return true;
                case 48:
                    java.lang.String _arg037 = data.readString();
                    java.lang.String _arg126 = data.readString();
                    boolean _arg211 = data.readBoolean();
                    java.lang.String[] _arg34 = data.createStringArray();
                    data.enforceNoDataAvail();
                    mountFstab(_arg037, _arg126, _arg211, _arg34);
                    reply.writeNoException();
                    return true;
                case 49:
                    java.lang.String _arg038 = data.readString();
                    java.lang.String _arg127 = data.readString();
                    boolean _arg212 = data.readBoolean();
                    java.lang.String _arg35 = data.readString();
                    boolean _arg43 = data.readBoolean();
                    java.lang.String[] _arg52 = data.createStringArray();
                    data.enforceNoDataAvail();
                    encryptFstab(_arg038, _arg127, _arg212, _arg35, _arg43, _arg52);
                    reply.writeNoException();
                    return true;
                case 50:
                    byte[] _arg039 = data.createByteArray();
                    data.enforceNoDataAvail();
                    setStorageBindingSeed(_arg039);
                    reply.writeNoException();
                    return true;
                case 51:
                    int _arg040 = data.readInt();
                    boolean _arg128 = data.readBoolean();
                    data.enforceNoDataAvail();
                    createUserStorageKeys(_arg040, _arg128);
                    reply.writeNoException();
                    return true;
                case 52:
                    int _arg041 = data.readInt();
                    data.enforceNoDataAvail();
                    destroyUserStorageKeys(_arg041);
                    reply.writeNoException();
                    return true;
                case 53:
                    int _arg042 = data.readInt();
                    byte[] _arg129 = data.createByteArray();
                    data.enforceNoDataAvail();
                    setCeStorageProtection(_arg042, _arg129);
                    reply.writeNoException();
                    return true;
                case 54:
                    int[] _result10 = getUnlockedUsers();
                    reply.writeNoException();
                    reply.writeIntArray(_result10);
                    return true;
                case 55:
                    int _arg043 = data.readInt();
                    byte[] _arg130 = data.createByteArray();
                    data.enforceNoDataAvail();
                    unlockCeStorage(_arg043, _arg130);
                    reply.writeNoException();
                    return true;
                case 56:
                    int _arg044 = data.readInt();
                    data.enforceNoDataAvail();
                    lockCeStorage(_arg044);
                    reply.writeNoException();
                    return true;
                case 57:
                    java.lang.String _arg045 = data.readString();
                    int _arg131 = data.readInt();
                    int _arg213 = data.readInt();
                    data.enforceNoDataAvail();
                    prepareUserStorage(_arg045, _arg131, _arg213);
                    reply.writeNoException();
                    return true;
                case 58:
                    java.lang.String _arg046 = data.readString();
                    int _arg132 = data.readInt();
                    int _arg214 = data.readInt();
                    data.enforceNoDataAvail();
                    destroyUserStorage(_arg046, _arg132, _arg214);
                    reply.writeNoException();
                    return true;
                case 59:
                    java.lang.String _arg047 = data.readString();
                    int _arg133 = data.readInt();
                    java.lang.String _arg215 = data.readString();
                    int _arg36 = data.readInt();
                    data.enforceNoDataAvail();
                    prepareSandboxForApp(_arg047, _arg133, _arg215, _arg36);
                    reply.writeNoException();
                    return true;
                case 60:
                    java.lang.String _arg048 = data.readString();
                    java.lang.String _arg134 = data.readString();
                    int _arg216 = data.readInt();
                    data.enforceNoDataAvail();
                    destroySandboxForApp(_arg048, _arg134, _arg216);
                    reply.writeNoException();
                    return true;
                case 61:
                    int _arg049 = data.readInt();
                    data.enforceNoDataAvail();
                    startCheckpoint(_arg049);
                    reply.writeNoException();
                    return true;
                case 62:
                    boolean _result11 = needsCheckpoint();
                    reply.writeNoException();
                    reply.writeBoolean(_result11);
                    return true;
                case 63:
                    boolean _result12 = needsRollback();
                    reply.writeNoException();
                    reply.writeBoolean(_result12);
                    return true;
                case 64:
                    boolean _result13 = isCheckpointing();
                    reply.writeNoException();
                    reply.writeBoolean(_result13);
                    return true;
                case 65:
                    java.lang.String _arg050 = data.readString();
                    boolean _arg135 = data.readBoolean();
                    data.enforceNoDataAvail();
                    abortChanges(_arg050, _arg135);
                    reply.writeNoException();
                    return true;
                case 66:
                    commitChanges();
                    reply.writeNoException();
                    return true;
                case 67:
                    prepareCheckpoint();
                    reply.writeNoException();
                    return true;
                case 68:
                    java.lang.String _arg051 = data.readString();
                    data.enforceNoDataAvail();
                    restoreCheckpoint(_arg051);
                    reply.writeNoException();
                    return true;
                case 69:
                    java.lang.String _arg052 = data.readString();
                    int _arg136 = data.readInt();
                    data.enforceNoDataAvail();
                    restoreCheckpointPart(_arg052, _arg136);
                    reply.writeNoException();
                    return true;
                case 70:
                    markBootAttempt();
                    reply.writeNoException();
                    return true;
                case 71:
                    boolean _result14 = supportsCheckpoint();
                    reply.writeNoException();
                    reply.writeBoolean(_result14);
                    return true;
                case 72:
                    boolean _result15 = supportsBlockCheckpoint();
                    reply.writeNoException();
                    reply.writeBoolean(_result15);
                    return true;
                case 73:
                    boolean _result16 = supportsFileCheckpoint();
                    reply.writeNoException();
                    reply.writeBoolean(_result16);
                    return true;
                case 74:
                    resetCheckpoint();
                    reply.writeNoException();
                    return true;
                case 75:
                    earlyBootEnded();
                    reply.writeNoException();
                    return true;
                case 76:
                    java.lang.String _arg053 = data.readString();
                    java.lang.String _arg137 = data.readString();
                    java.lang.String _arg217 = data.readString();
                    java.lang.String _arg37 = data.readString();
                    java.lang.String _arg44 = data.readString();
                    int _arg53 = data.readInt();
                    data.enforceNoDataAvail();
                    java.lang.String _result17 = createStubVolume(_arg053, _arg137, _arg217, _arg37, _arg44, _arg53);
                    reply.writeNoException();
                    reply.writeString(_result17);
                    return true;
                case 77:
                    java.lang.String _arg054 = data.readString();
                    data.enforceNoDataAvail();
                    destroyStubVolume(_arg054);
                    reply.writeNoException();
                    return true;
                case 78:
                    int _arg055 = data.readInt();
                    int _arg138 = data.readInt();
                    int _arg218 = data.readInt();
                    int _arg38 = data.readInt();
                    data.enforceNoDataAvail();
                    java.io.FileDescriptor _result18 = openAppFuseFile(_arg055, _arg138, _arg218, _arg38);
                    reply.writeNoException();
                    reply.writeRawFileDescriptor(_result18);
                    return true;
                case 79:
                    boolean _result19 = incFsEnabled();
                    reply.writeNoException();
                    reply.writeBoolean(_result19);
                    return true;
                case 80:
                    java.lang.String _arg056 = data.readString();
                    java.lang.String _arg139 = data.readString();
                    int _arg219 = data.readInt();
                    java.lang.String _arg39 = data.readString();
                    data.enforceNoDataAvail();
                    android.os.incremental.IncrementalFileSystemControlParcel _result20 = mountIncFs(_arg056, _arg139, _arg219, _arg39);
                    reply.writeNoException();
                    reply.writeTypedObject(_result20, 1);
                    return true;
                case 81:
                    java.lang.String _arg057 = data.readString();
                    data.enforceNoDataAvail();
                    unmountIncFs(_arg057);
                    reply.writeNoException();
                    return true;
                case 82:
                    android.os.incremental.IncrementalFileSystemControlParcel _arg058 = (android.os.incremental.IncrementalFileSystemControlParcel) data.readTypedObject(android.os.incremental.IncrementalFileSystemControlParcel.CREATOR);
                    boolean _arg140 = data.readBoolean();
                    boolean _arg220 = data.readBoolean();
                    java.lang.String _arg310 = data.readString();
                    data.enforceNoDataAvail();
                    setIncFsMountOptions(_arg058, _arg140, _arg220, _arg310);
                    reply.writeNoException();
                    return true;
                case 83:
                    java.lang.String _arg059 = data.readString();
                    java.lang.String _arg141 = data.readString();
                    data.enforceNoDataAvail();
                    bindMount(_arg059, _arg141);
                    reply.writeNoException();
                    return true;
                case 84:
                    java.lang.String _arg060 = data.readString();
                    data.enforceNoDataAvail();
                    sdlockSetPassword(_arg060);
                    reply.writeNoException();
                    return true;
                case 85:
                    java.lang.String _arg061 = data.readString();
                    data.enforceNoDataAvail();
                    sdlockClearPassword(_arg061);
                    reply.writeNoException();
                    return true;
                case 86:
                    java.lang.String _arg062 = data.readString();
                    data.enforceNoDataAvail();
                    sdlockUnlock(_arg062);
                    reply.writeNoException();
                    return true;
                case 87:
                    java.lang.String _result21 = sdlockGetCid();
                    reply.writeNoException();
                    reply.writeString(_result21);
                    return true;
                case 88:
                    java.lang.String _result22 = sdlockPoll();
                    reply.writeNoException();
                    reply.writeString(_result22);
                    return true;
                case 89:
                    sdlockErase();
                    reply.writeNoException();
                    return true;
                case 90:
                    voldTBExt();
                    reply.writeNoException();
                    return true;
                case 91:
                    java.lang.String _arg063 = data.readString();
                    java.lang.String _arg142 = data.readString();
                    data.enforceNoDataAvail();
                    java.io.FileDescriptor _result23 = mountDfsFuse(_arg063, _arg142);
                    reply.writeNoException();
                    reply.writeRawFileDescriptor(_result23);
                    return true;
                case 92:
                    java.lang.String _arg064 = data.readString();
                    data.enforceNoDataAvail();
                    int _result24 = umountDfsFuse(_arg064);
                    reply.writeNoException();
                    reply.writeInt(_result24);
                    return true;
                case 93:
                    java.lang.String _arg065 = data.readString();
                    int _arg143 = data.readInt();
                    int _arg221 = data.readInt();
                    data.enforceNoDataAvail();
                    configDfsFuse(_arg065, _arg143, _arg221);
                    reply.writeNoException();
                    return true;
                case 94:
                    java.lang.String _arg066 = data.readString();
                    data.enforceNoDataAvail();
                    destroyDsuMetadataKey(_arg066);
                    reply.writeNoException();
                    return true;
                case 95:
                    long _result25 = getStorageSize();
                    reply.writeNoException();
                    reply.writeLong(_result25);
                    return true;
                case 96:
                    ufsHid();
                    reply.writeNoException();
                    return true;
                case 97:
                    int _result26 = getStorageRemainingLifetime();
                    reply.writeNoException();
                    reply.writeInt(_result26);
                    return true;
                case 98:
                    java.lang.String _arg067 = data.readString();
                    int _arg144 = data.readInt();
                    int _arg222 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result27 = mountTmpStor(_arg067, _arg144, _arg222);
                    reply.writeNoException();
                    reply.writeInt(_result27);
                    return true;
                case 99:
                    java.lang.String _arg068 = data.readString();
                    int _arg145 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result28 = unmountTmpStor(_arg068, _arg145);
                    reply.writeNoException();
                    reply.writeBoolean(_result28);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.os.IVold {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.os.IVold.Stub.DESCRIPTOR;
            }

            @Override // android.os.IVold
            public void setListener(android.os.IVoldListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(1, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void abortFuse() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void monitor() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void reset() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void shutdown() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void onUserAdded(int userId, int userSerial, int sharesStorageWithUserId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeInt(sharesStorageWithUserId);
                    this.mRemote.transact(6, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void onUserRemoved(int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(7, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void onUserStarted(int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(8, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void onUserStopped(int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(9, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void addAppIds(java.lang.String[] packageNames, int[] appIds) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeIntArray(appIds);
                    this.mRemote.transact(10, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void addSandboxIds(int[] appIds, java.lang.String[] sandboxIds) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeIntArray(appIds);
                    _data.writeStringArray(sandboxIds);
                    this.mRemote.transact(11, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void onSecureKeyguardStateChanged(boolean isShowing) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeBoolean(isShowing);
                    this.mRemote.transact(12, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void partition(java.lang.String diskId, int partitionType, int ratio) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    _data.writeInt(partitionType);
                    _data.writeInt(ratio);
                    this.mRemote.transact(13, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void forgetPartition(java.lang.String partGuid, java.lang.String fsUuid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(partGuid);
                    _data.writeString(fsUuid);
                    this.mRemote.transact(14, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void mount(java.lang.String volId, int mountFlags, int mountUserId, android.os.IVoldMountCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeInt(mountFlags);
                    _data.writeInt(mountUserId);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(15, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void unmount(java.lang.String volId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    this.mRemote.transact(16, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void format(java.lang.String volId, java.lang.String fsType) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeString(fsType);
                    this.mRemote.transact(17, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void benchmark(java.lang.String volId, android.os.IVoldTaskListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(18, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void checkBeforeMount(java.lang.String volId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    this.mRemote.transact(19, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fsyncCtrl(java.lang.String fsyncMode) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(fsyncMode);
                    this.mRemote.transact(20, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void unlockSensitiveKey(int userId, int userSerial, java.lang.String token, java.lang.String secret, int sensitiveType) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeString(token);
                    _data.writeString(secret);
                    _data.writeInt(sensitiveType);
                    this.mRemote.transact(21, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void onSecureKeyguardStateChangedForSensitiveFile(boolean isShowing, int userId, int sensitiveType) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeBoolean(isShowing);
                    _data.writeInt(userId);
                    _data.writeInt(sensitiveType);
                    this.mRemote.transact(22, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public byte[] exportSensitiveKey(int userId, int sensitiveType, boolean useDefault) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(sensitiveType);
                    _data.writeBoolean(useDefault);
                    this.mRemote.transact(23, _data, _reply, 32);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public byte[] exportSensitiveBePublicKey(int userId, int sensitiveType) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(sensitiveType);
                    this.mRemote.transact(24, _data, _reply, 32);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void moveStorage(java.lang.String fromVolId, java.lang.String toVolId, android.os.IVoldTaskListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(fromVolId);
                    _data.writeString(toVolId);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(25, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void remountUid(int uid, int remountMode) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(remountMode);
                    this.mRemote.transact(26, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void remountAppStorageDirs(int uid, int pid, java.lang.String[] packageNames) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    _data.writeStringArray(packageNames);
                    this.mRemote.transact(27, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void unmountAppStorageDirs(int uid, int pid, java.lang.String[] packageNames) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    _data.writeStringArray(packageNames);
                    this.mRemote.transact(28, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void setupAppDir(java.lang.String path, int appUid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(appUid);
                    this.mRemote.transact(29, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fixupAppDir(java.lang.String path, int appUid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(appUid);
                    this.mRemote.transact(30, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void ensureAppDirsCreated(java.lang.String[] paths, int appUid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeStringArray(paths);
                    _data.writeInt(appUid);
                    this.mRemote.transact(31, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public java.lang.String createObb(java.lang.String sourcePath, int ownerGid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(sourcePath);
                    _data.writeInt(ownerGid);
                    this.mRemote.transact(32, _data, _reply, 32);
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyObb(java.lang.String volId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    this.mRemote.transact(33, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fstrim(int fstrimFlags, android.os.IVoldTaskListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(fstrimFlags);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(34, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void runIdleMaint(boolean needGC, android.os.IVoldTaskListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeBoolean(needGC);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(35, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void abortIdleMaint(android.os.IVoldTaskListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(36, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public int getStorageLifeTime() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 32);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void setGCUrgentPace(int neededSegments, int minSegmentThreshold, float dirtyReclaimRate, float reclaimWeight, int gcPeriod, int minGCSleepTime, int targetDirtyRatio) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(neededSegments);
                    _data.writeInt(minSegmentThreshold);
                    _data.writeFloat(dirtyReclaimRate);
                    _data.writeFloat(reclaimWeight);
                    _data.writeInt(gcPeriod);
                    _data.writeInt(minGCSleepTime);
                    _data.writeInt(targetDirtyRatio);
                    this.mRemote.transact(38, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void refreshLatestWrite() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public int getWriteAmount() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 32);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public java.io.FileDescriptor mountAppFuse(int uid, int mountId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(mountId);
                    this.mRemote.transact(41, _data, _reply, 32);
                    _reply.readException();
                    java.io.FileDescriptor _result = _reply.readRawFileDescriptor();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void unmountAppFuse(int uid, int mountId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(mountId);
                    this.mRemote.transact(42, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public int startserviceAppFuse() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 32);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public int stopserviceAppFuse() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 32);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public int clearCache(int whatsAppExist) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(whatsAppExist);
                    this.mRemote.transact(45, _data, _reply, 32);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fbeEnable() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(46, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void initUser0() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(47, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void mountFstab(java.lang.String blkDevice, java.lang.String mountPoint, boolean isZoned, java.lang.String[] userDevices) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(blkDevice);
                    _data.writeString(mountPoint);
                    _data.writeBoolean(isZoned);
                    _data.writeStringArray(userDevices);
                    this.mRemote.transact(48, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void encryptFstab(java.lang.String blkDevice, java.lang.String mountPoint, boolean shouldFormat, java.lang.String fsType, boolean isZoned, java.lang.String[] userDevices) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(blkDevice);
                    _data.writeString(mountPoint);
                    _data.writeBoolean(shouldFormat);
                    _data.writeString(fsType);
                    _data.writeBoolean(isZoned);
                    _data.writeStringArray(userDevices);
                    this.mRemote.transact(49, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void setStorageBindingSeed(byte[] seed) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeByteArray(seed);
                    this.mRemote.transact(50, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void createUserStorageKeys(int userId, boolean ephemeral) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeBoolean(ephemeral);
                    this.mRemote.transact(51, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyUserStorageKeys(int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(52, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void setCeStorageProtection(int userId, byte[] secret) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeByteArray(secret);
                    this.mRemote.transact(53, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public int[] getUnlockedUsers() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(54, _data, _reply, 32);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void unlockCeStorage(int userId, byte[] secret) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeByteArray(secret);
                    this.mRemote.transact(55, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void lockCeStorage(int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(56, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void prepareUserStorage(java.lang.String uuid, int userId, int storageFlags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(storageFlags);
                    this.mRemote.transact(57, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyUserStorage(java.lang.String uuid, int userId, int storageFlags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(storageFlags);
                    this.mRemote.transact(58, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void prepareSandboxForApp(java.lang.String packageName, int appId, java.lang.String sandboxId, int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(appId);
                    _data.writeString(sandboxId);
                    _data.writeInt(userId);
                    this.mRemote.transact(59, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroySandboxForApp(java.lang.String packageName, java.lang.String sandboxId, int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(sandboxId);
                    _data.writeInt(userId);
                    this.mRemote.transact(60, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void startCheckpoint(int retry) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(retry);
                    this.mRemote.transact(61, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean needsCheckpoint() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(62, _data, _reply, 32);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean needsRollback() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(63, _data, _reply, 32);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean isCheckpointing() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(64, _data, _reply, 32);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void abortChanges(java.lang.String device, boolean retry) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(device);
                    _data.writeBoolean(retry);
                    this.mRemote.transact(65, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void commitChanges() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(66, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void prepareCheckpoint() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(67, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void restoreCheckpoint(java.lang.String device) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(device);
                    this.mRemote.transact(68, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void restoreCheckpointPart(java.lang.String device, int count) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(device);
                    _data.writeInt(count);
                    this.mRemote.transact(69, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void markBootAttempt() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(70, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean supportsCheckpoint() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(71, _data, _reply, 32);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean supportsBlockCheckpoint() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(72, _data, _reply, 32);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean supportsFileCheckpoint() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(73, _data, _reply, 32);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void resetCheckpoint() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(74, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void earlyBootEnded() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(75, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public java.lang.String createStubVolume(java.lang.String sourcePath, java.lang.String mountPath, java.lang.String fsType, java.lang.String fsUuid, java.lang.String fsLabel, int flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(sourcePath);
                    _data.writeString(mountPath);
                    _data.writeString(fsType);
                    _data.writeString(fsUuid);
                    _data.writeString(fsLabel);
                    _data.writeInt(flags);
                    this.mRemote.transact(76, _data, _reply, 32);
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyStubVolume(java.lang.String volId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    this.mRemote.transact(77, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public java.io.FileDescriptor openAppFuseFile(int uid, int mountId, int fileId, int flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(mountId);
                    _data.writeInt(fileId);
                    _data.writeInt(flags);
                    this.mRemote.transact(78, _data, _reply, 32);
                    _reply.readException();
                    java.io.FileDescriptor _result = _reply.readRawFileDescriptor();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean incFsEnabled() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(79, _data, _reply, 32);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public android.os.incremental.IncrementalFileSystemControlParcel mountIncFs(java.lang.String backingPath, java.lang.String targetDir, int flags, java.lang.String sysfsName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(backingPath);
                    _data.writeString(targetDir);
                    _data.writeInt(flags);
                    _data.writeString(sysfsName);
                    this.mRemote.transact(80, _data, _reply, 32);
                    _reply.readException();
                    android.os.incremental.IncrementalFileSystemControlParcel _result = (android.os.incremental.IncrementalFileSystemControlParcel) _reply.readTypedObject(android.os.incremental.IncrementalFileSystemControlParcel.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void unmountIncFs(java.lang.String dir) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(dir);
                    this.mRemote.transact(81, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void setIncFsMountOptions(android.os.incremental.IncrementalFileSystemControlParcel control, boolean enableReadLogs, boolean enableReadTimeouts, java.lang.String sysfsName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeTypedObject(control, 0);
                    _data.writeBoolean(enableReadLogs);
                    _data.writeBoolean(enableReadTimeouts);
                    _data.writeString(sysfsName);
                    this.mRemote.transact(82, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void bindMount(java.lang.String sourceDir, java.lang.String targetDir) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(sourceDir);
                    _data.writeString(targetDir);
                    this.mRemote.transact(83, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void sdlockSetPassword(java.lang.String password) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(password);
                    this.mRemote.transact(84, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void sdlockClearPassword(java.lang.String password) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(password);
                    this.mRemote.transact(85, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void sdlockUnlock(java.lang.String password) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(password);
                    this.mRemote.transact(86, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public java.lang.String sdlockGetCid() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(87, _data, _reply, 32);
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public java.lang.String sdlockPoll() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(88, _data, _reply, 32);
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void sdlockErase() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(89, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void voldTBExt() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(90, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public java.io.FileDescriptor mountDfsFuse(java.lang.String fuse_path, java.lang.String options) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(fuse_path);
                    _data.writeString(options);
                    this.mRemote.transact(91, _data, _reply, 32);
                    _reply.readException();
                    java.io.FileDescriptor _result = _reply.readRawFileDescriptor();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public int umountDfsFuse(java.lang.String fuse_path) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(fuse_path);
                    this.mRemote.transact(92, _data, _reply, 32);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void configDfsFuse(java.lang.String fuse_path, int read_ahead_blocks, int max_dirty_ratio) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(fuse_path);
                    _data.writeInt(read_ahead_blocks);
                    _data.writeInt(max_dirty_ratio);
                    this.mRemote.transact(93, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyDsuMetadataKey(java.lang.String dsuSlot) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(dsuSlot);
                    this.mRemote.transact(94, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public long getStorageSize() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(95, _data, _reply, 32);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void ufsHid() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(96, _data, _reply, 32);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public int getStorageRemainingLifetime() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    this.mRemote.transact(97, _data, _reply, 32);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public int mountTmpStor(java.lang.String mountPoint, int maxSize, int connect_id) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(mountPoint);
                    _data.writeInt(maxSize);
                    _data.writeInt(connect_id);
                    this.mRemote.transact(98, _data, _reply, 32);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean unmountTmpStor(java.lang.String mountPoint, int connect_id) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                _data.markSensitive();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IVold.Stub.DESCRIPTOR);
                    _data.writeString(mountPoint);
                    _data.writeInt(connect_id);
                    this.mRemote.transact(99, _data, _reply, 32);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
