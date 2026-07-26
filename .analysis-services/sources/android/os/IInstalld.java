package android.os;

/* JADX INFO: loaded from: classes.dex */
public interface IInstalld extends android.os.IInterface {
    public static final int FLAG_CLEAR_APP_DATA_KEEP_ART_PROFILES = 131072;
    public static final int FLAG_CLEAR_CACHE_ONLY = 16;
    public static final int FLAG_CLEAR_CODE_CACHE_ONLY = 32;
    public static final int FLAG_FORCE = 8192;
    public static final int FLAG_FREE_CACHE_DEFY_TARGET_FREE_BYTES = 2048;
    public static final int FLAG_FREE_CACHE_NOOP = 1024;
    public static final int FLAG_FREE_CACHE_V2 = 256;
    public static final int FLAG_FREE_CACHE_V2_DEFY_QUOTA = 512;
    public static final int FLAG_STORAGE_CE = 2;
    public static final int FLAG_STORAGE_DE = 1;
    public static final int FLAG_STORAGE_EXTERNAL = 4;
    public static final int FLAG_STORAGE_SDK = 8;
    public static final int FLAG_USE_QUOTA = 4096;

    void cleanupInvalidPackageDirs(java.lang.String str, int i, int i2) throws android.os.RemoteException;

    void clearAppData(java.lang.String str, java.lang.String str2, int i, int i2, long j) throws android.os.RemoteException;

    void clearAppProfiles(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void controlDexOptBlocking(boolean z) throws android.os.RemoteException;

    boolean copySystemProfile(java.lang.String str, int i, java.lang.String str2, java.lang.String str3) throws android.os.RemoteException;

    android.os.CreateAppDataResult createAppData(android.os.CreateAppDataArgs createAppDataArgs) throws android.os.RemoteException;

    android.os.CreateAppDataResult[] createAppDataBatched(android.os.CreateAppDataArgs[] createAppDataArgsArr) throws android.os.RemoteException;

    android.os.IInstalld.IFsveritySetupAuthToken createFsveritySetupAuthToken(android.os.ParcelFileDescriptor parcelFileDescriptor, int i) throws android.os.RemoteException;

    void createOatDir(java.lang.String str, java.lang.String str2, java.lang.String str3) throws android.os.RemoteException;

    boolean createProfileSnapshot(int i, java.lang.String str, java.lang.String str2, java.lang.String str3) throws android.os.RemoteException;

    void createUserData(java.lang.String str, int i, int i2, int i3) throws android.os.RemoteException;

    long deleteOdex(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4) throws android.os.RemoteException;

    void deleteReferenceProfile(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void destroyAppData(java.lang.String str, java.lang.String str2, int i, int i2, long j) throws android.os.RemoteException;

    void destroyAppDataSnapshot(java.lang.String str, java.lang.String str2, int i, long j, int i2, int i3) throws android.os.RemoteException;

    void destroyAppProfiles(java.lang.String str) throws android.os.RemoteException;

    void destroyCeSnapshotsNotSpecified(java.lang.String str, int i, int[] iArr) throws android.os.RemoteException;

    void destroyProfileSnapshot(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void destroyUserData(java.lang.String str, int i, int i2) throws android.os.RemoteException;

    boolean dexopt(java.lang.String str, int i, java.lang.String str2, java.lang.String str3, int i2, java.lang.String str4, int i3, java.lang.String str5, java.lang.String str6, java.lang.String str7, java.lang.String str8, boolean z, int i4, java.lang.String str9, java.lang.String str10, java.lang.String str11) throws android.os.RemoteException;

    boolean dumpProfiles(int i, java.lang.String str, java.lang.String str2, java.lang.String str3, boolean z) throws android.os.RemoteException;

    int enableFsverity(android.os.IInstalld.IFsveritySetupAuthToken iFsveritySetupAuthToken, java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void fixupAppData(java.lang.String str, int i) throws android.os.RemoteException;

    void freeCache(java.lang.String str, long j, int i) throws android.os.RemoteException;

    android.os.storage.CrateMetadata[] getAppCrates(java.lang.String str, java.lang.String[] strArr, int i) throws android.os.RemoteException;

    long[] getAppSize(java.lang.String str, java.lang.String[] strArr, int i, int i2, int i3, long[] jArr, java.lang.String[] strArr2) throws android.os.RemoteException;

    long[] getExternalSize(java.lang.String str, int i, int i2, int[] iArr) throws android.os.RemoteException;

    int getOdexVisibility(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4) throws android.os.RemoteException;

    android.os.storage.CrateMetadata[] getUserCrates(java.lang.String str, int i) throws android.os.RemoteException;

    long[] getUserSize(java.lang.String str, int i, int i2, int[] iArr) throws android.os.RemoteException;

    byte[] hashSecondaryDexFile(java.lang.String str, java.lang.String str2, int i, java.lang.String str3, int i2) throws android.os.RemoteException;

    void invalidateMounts() throws android.os.RemoteException;

    boolean isQuotaSupported(java.lang.String str) throws android.os.RemoteException;

    void linkFile(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4) throws android.os.RemoteException;

    void linkNativeLibraryDirectory(java.lang.String str, java.lang.String str2, java.lang.String str3, int i) throws android.os.RemoteException;

    int mergeProfiles(int i, java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void migrateAppData(java.lang.String str, java.lang.String str2, int i, int i2) throws android.os.RemoteException;

    void migrateLegacyObbData() throws android.os.RemoteException;

    void moveAb(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4) throws android.os.RemoteException;

    void moveCompleteApp(java.lang.String str, java.lang.String str2, java.lang.String str3, int i, java.lang.String str4, int i2, java.lang.String str5) throws android.os.RemoteException;

    void onPrivateVolumeRemoved(java.lang.String str) throws android.os.RemoteException;

    android.os.PersistableBundle oplusCommonInterface(java.lang.String str, android.os.PersistableBundle persistableBundle) throws android.os.RemoteException;

    void oplusCustomizeInterface(java.lang.String str, android.os.PersistableBundle persistableBundle, android.os.ICustomizePmsCallback iCustomizePmsCallback) throws android.os.RemoteException;

    boolean prepareAppProfile(java.lang.String str, int i, int i2, java.lang.String str2, java.lang.String str3, java.lang.String str4) throws android.os.RemoteException;

    void reconcileSdkData(android.os.ReconcileSdkDataArgs reconcileSdkDataArgs) throws android.os.RemoteException;

    boolean reconcileSecondaryDexFile(java.lang.String str, java.lang.String str2, int i, java.lang.String[] strArr, java.lang.String str3, int i2) throws android.os.RemoteException;

    void restoreAppDataSnapshot(java.lang.String str, java.lang.String str2, int i, java.lang.String str3, int i2, int i3, int i4) throws android.os.RemoteException;

    void restoreconAppData(java.lang.String str, java.lang.String str2, int i, int i2, int i3, java.lang.String str3) throws android.os.RemoteException;

    void rmPackageDir(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void rmdex(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void setAppQuota(java.lang.String str, int i, int i2, long j) throws android.os.RemoteException;

    void setFirstBoot() throws android.os.RemoteException;

    long snapshotAppData(java.lang.String str, java.lang.String str2, int i, int i2, int i3) throws android.os.RemoteException;

    void tryMountDataMirror(java.lang.String str) throws android.os.RemoteException;

    public static class Default implements android.os.IInstalld {
        @Override // android.os.IInstalld
        public void createUserData(java.lang.String uuid, int userId, int userSerial, int flags) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void destroyUserData(java.lang.String uuid, int userId, int flags) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void setFirstBoot() throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public android.os.CreateAppDataResult createAppData(android.os.CreateAppDataArgs args) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInstalld
        public android.os.CreateAppDataResult[] createAppDataBatched(android.os.CreateAppDataArgs[] args) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInstalld
        public void reconcileSdkData(android.os.ReconcileSdkDataArgs args) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void restoreconAppData(java.lang.String uuid, java.lang.String packageName, int userId, int flags, int appId, java.lang.String seInfo) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void migrateAppData(java.lang.String uuid, java.lang.String packageName, int userId, int flags) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void clearAppData(java.lang.String uuid, java.lang.String packageName, int userId, int flags, long ceDataInode) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void destroyAppData(java.lang.String uuid, java.lang.String packageName, int userId, int flags, long ceDataInode) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void fixupAppData(java.lang.String uuid, int flags) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public long[] getAppSize(java.lang.String uuid, java.lang.String[] packageNames, int userId, int flags, int appId, long[] ceDataInodes, java.lang.String[] codePaths) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInstalld
        public long[] getUserSize(java.lang.String uuid, int userId, int flags, int[] appIds) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInstalld
        public long[] getExternalSize(java.lang.String uuid, int userId, int flags, int[] appIds) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInstalld
        public android.os.storage.CrateMetadata[] getAppCrates(java.lang.String uuid, java.lang.String[] packageNames, int userId) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInstalld
        public android.os.storage.CrateMetadata[] getUserCrates(java.lang.String uuid, int userId) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInstalld
        public void setAppQuota(java.lang.String uuid, int userId, int appId, long cacheQuota) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void moveCompleteApp(java.lang.String fromUuid, java.lang.String toUuid, java.lang.String packageName, int appId, java.lang.String seInfo, int targetSdkVersion, java.lang.String fromCodePath) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public boolean dexopt(java.lang.String apkPath, int uid, java.lang.String packageName, java.lang.String instructionSet, int dexoptNeeded, java.lang.String outputPath, int dexFlags, java.lang.String compilerFilter, java.lang.String uuid, java.lang.String sharedLibraries, java.lang.String seInfo, boolean downgrade, int targetSdkVersion, java.lang.String profileName, java.lang.String dexMetadataPath, java.lang.String compilationReason) throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IInstalld
        public void controlDexOptBlocking(boolean block) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void rmdex(java.lang.String codePath, java.lang.String instructionSet) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public int mergeProfiles(int uid, java.lang.String packageName, java.lang.String profileName) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.os.IInstalld
        public boolean dumpProfiles(int uid, java.lang.String packageName, java.lang.String profileName, java.lang.String codePath, boolean dumpClassesAndMethods) throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IInstalld
        public boolean copySystemProfile(java.lang.String systemProfile, int uid, java.lang.String packageName, java.lang.String profileName) throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IInstalld
        public void clearAppProfiles(java.lang.String packageName, java.lang.String profileName) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void destroyAppProfiles(java.lang.String packageName) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void deleteReferenceProfile(java.lang.String packageName, java.lang.String profileName) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public boolean createProfileSnapshot(int appId, java.lang.String packageName, java.lang.String profileName, java.lang.String classpath) throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IInstalld
        public void destroyProfileSnapshot(java.lang.String packageName, java.lang.String profileName) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void rmPackageDir(java.lang.String packageName, java.lang.String packageDir) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void freeCache(java.lang.String uuid, long targetFreeBytes, int flags) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void linkNativeLibraryDirectory(java.lang.String uuid, java.lang.String packageName, java.lang.String nativeLibPath32, int userId) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void createOatDir(java.lang.String packageName, java.lang.String oatDir, java.lang.String instructionSet) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void linkFile(java.lang.String packageName, java.lang.String relativePath, java.lang.String fromBase, java.lang.String toBase) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void moveAb(java.lang.String packageName, java.lang.String apkPath, java.lang.String instructionSet, java.lang.String outputPath) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public long deleteOdex(java.lang.String packageName, java.lang.String apkPath, java.lang.String instructionSet, java.lang.String outputPath) throws android.os.RemoteException {
            return 0L;
        }

        @Override // android.os.IInstalld
        public boolean reconcileSecondaryDexFile(java.lang.String dexPath, java.lang.String pkgName, int uid, java.lang.String[] isas, java.lang.String volume_uuid, int storage_flag) throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IInstalld
        public byte[] hashSecondaryDexFile(java.lang.String dexPath, java.lang.String pkgName, int uid, java.lang.String volumeUuid, int storageFlag) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInstalld
        public void invalidateMounts() throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public boolean isQuotaSupported(java.lang.String uuid) throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IInstalld
        public boolean prepareAppProfile(java.lang.String packageName, int userId, int appId, java.lang.String profileName, java.lang.String codePath, java.lang.String dexMetadata) throws android.os.RemoteException {
            return false;
        }

        @Override // android.os.IInstalld
        public long snapshotAppData(java.lang.String uuid, java.lang.String packageName, int userId, int snapshotId, int storageFlags) throws android.os.RemoteException {
            return 0L;
        }

        @Override // android.os.IInstalld
        public void restoreAppDataSnapshot(java.lang.String uuid, java.lang.String packageName, int appId, java.lang.String seInfo, int user, int snapshotId, int storageflags) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void destroyAppDataSnapshot(java.lang.String uuid, java.lang.String packageName, int userId, long ceSnapshotInode, int snapshotId, int storageFlags) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void destroyCeSnapshotsNotSpecified(java.lang.String uuid, int userId, int[] retainSnapshotIds) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void tryMountDataMirror(java.lang.String volumeUuid) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void onPrivateVolumeRemoved(java.lang.String volumeUuid) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void migrateLegacyObbData() throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public android.os.PersistableBundle oplusCommonInterface(java.lang.String funName, android.os.PersistableBundle args) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInstalld
        public void oplusCustomizeInterface(java.lang.String funName, android.os.PersistableBundle args, android.os.ICustomizePmsCallback callback) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public void cleanupInvalidPackageDirs(java.lang.String uuid, int userId, int flags) throws android.os.RemoteException {
        }

        @Override // android.os.IInstalld
        public int getOdexVisibility(java.lang.String packageName, java.lang.String apkPath, java.lang.String instructionSet, java.lang.String outputPath) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.os.IInstalld
        public android.os.IInstalld.IFsveritySetupAuthToken createFsveritySetupAuthToken(android.os.ParcelFileDescriptor authFd, int uid) throws android.os.RemoteException {
            return null;
        }

        @Override // android.os.IInstalld
        public int enableFsverity(android.os.IInstalld.IFsveritySetupAuthToken authToken, java.lang.String filePath, java.lang.String packageName) throws android.os.RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.os.IInstalld {
        public static final java.lang.String DESCRIPTOR = "android.os.IInstalld";
        static final int TRANSACTION_cleanupInvalidPackageDirs = 51;
        static final int TRANSACTION_clearAppData = 9;
        static final int TRANSACTION_clearAppProfiles = 25;
        static final int TRANSACTION_controlDexOptBlocking = 20;
        static final int TRANSACTION_copySystemProfile = 24;
        static final int TRANSACTION_createAppData = 4;
        static final int TRANSACTION_createAppDataBatched = 5;
        static final int TRANSACTION_createFsveritySetupAuthToken = 53;
        static final int TRANSACTION_createOatDir = 33;
        static final int TRANSACTION_createProfileSnapshot = 28;
        static final int TRANSACTION_createUserData = 1;
        static final int TRANSACTION_deleteOdex = 36;
        static final int TRANSACTION_deleteReferenceProfile = 27;
        static final int TRANSACTION_destroyAppData = 10;
        static final int TRANSACTION_destroyAppDataSnapshot = 44;
        static final int TRANSACTION_destroyAppProfiles = 26;
        static final int TRANSACTION_destroyCeSnapshotsNotSpecified = 45;
        static final int TRANSACTION_destroyProfileSnapshot = 29;
        static final int TRANSACTION_destroyUserData = 2;
        static final int TRANSACTION_dexopt = 19;
        static final int TRANSACTION_dumpProfiles = 23;
        static final int TRANSACTION_enableFsverity = 54;
        static final int TRANSACTION_fixupAppData = 11;
        static final int TRANSACTION_freeCache = 31;
        static final int TRANSACTION_getAppCrates = 15;
        static final int TRANSACTION_getAppSize = 12;
        static final int TRANSACTION_getExternalSize = 14;
        static final int TRANSACTION_getOdexVisibility = 52;
        static final int TRANSACTION_getUserCrates = 16;
        static final int TRANSACTION_getUserSize = 13;
        static final int TRANSACTION_hashSecondaryDexFile = 38;
        static final int TRANSACTION_invalidateMounts = 39;
        static final int TRANSACTION_isQuotaSupported = 40;
        static final int TRANSACTION_linkFile = 34;
        static final int TRANSACTION_linkNativeLibraryDirectory = 32;
        static final int TRANSACTION_mergeProfiles = 22;
        static final int TRANSACTION_migrateAppData = 8;
        static final int TRANSACTION_migrateLegacyObbData = 48;
        static final int TRANSACTION_moveAb = 35;
        static final int TRANSACTION_moveCompleteApp = 18;
        static final int TRANSACTION_onPrivateVolumeRemoved = 47;
        static final int TRANSACTION_oplusCommonInterface = 49;
        static final int TRANSACTION_oplusCustomizeInterface = 50;
        static final int TRANSACTION_prepareAppProfile = 41;
        static final int TRANSACTION_reconcileSdkData = 6;
        static final int TRANSACTION_reconcileSecondaryDexFile = 37;
        static final int TRANSACTION_restoreAppDataSnapshot = 43;
        static final int TRANSACTION_restoreconAppData = 7;
        static final int TRANSACTION_rmPackageDir = 30;
        static final int TRANSACTION_rmdex = 21;
        static final int TRANSACTION_setAppQuota = 17;
        static final int TRANSACTION_setFirstBoot = 3;
        static final int TRANSACTION_snapshotAppData = 42;
        static final int TRANSACTION_tryMountDataMirror = 46;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.os.IInstalld asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.os.IInstalld)) {
                return (android.os.IInstalld) iin;
            }
            return new android.os.IInstalld.Stub.Proxy(obj);
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
                    java.lang.String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    data.enforceNoDataAvail();
                    createUserData(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                case 2:
                    java.lang.String _arg02 = data.readString();
                    int _arg12 = data.readInt();
                    int _arg22 = data.readInt();
                    data.enforceNoDataAvail();
                    destroyUserData(_arg02, _arg12, _arg22);
                    reply.writeNoException();
                    return true;
                case 3:
                    setFirstBoot();
                    reply.writeNoException();
                    return true;
                case 4:
                    android.os.CreateAppDataArgs _arg03 = (android.os.CreateAppDataArgs) data.readTypedObject(android.os.CreateAppDataArgs.CREATOR);
                    data.enforceNoDataAvail();
                    android.os.CreateAppDataResult _result = createAppData(_arg03);
                    reply.writeNoException();
                    reply.writeTypedObject(_result, 1);
                    return true;
                case 5:
                    android.os.CreateAppDataArgs[] _arg04 = (android.os.CreateAppDataArgs[]) data.createTypedArray(android.os.CreateAppDataArgs.CREATOR);
                    data.enforceNoDataAvail();
                    android.os.CreateAppDataResult[] _result2 = createAppDataBatched(_arg04);
                    reply.writeNoException();
                    reply.writeTypedArray(_result2, 1);
                    return true;
                case 6:
                    android.os.ReconcileSdkDataArgs _arg05 = (android.os.ReconcileSdkDataArgs) data.readTypedObject(android.os.ReconcileSdkDataArgs.CREATOR);
                    data.enforceNoDataAvail();
                    reconcileSdkData(_arg05);
                    reply.writeNoException();
                    return true;
                case 7:
                    java.lang.String _arg06 = data.readString();
                    java.lang.String _arg13 = data.readString();
                    int _arg23 = data.readInt();
                    int _arg32 = data.readInt();
                    int _arg4 = data.readInt();
                    java.lang.String _arg5 = data.readString();
                    data.enforceNoDataAvail();
                    restoreconAppData(_arg06, _arg13, _arg23, _arg32, _arg4, _arg5);
                    reply.writeNoException();
                    return true;
                case 8:
                    java.lang.String _arg07 = data.readString();
                    java.lang.String _arg14 = data.readString();
                    int _arg24 = data.readInt();
                    int _arg33 = data.readInt();
                    data.enforceNoDataAvail();
                    migrateAppData(_arg07, _arg14, _arg24, _arg33);
                    reply.writeNoException();
                    return true;
                case 9:
                    java.lang.String _arg08 = data.readString();
                    java.lang.String _arg15 = data.readString();
                    int _arg25 = data.readInt();
                    int _arg34 = data.readInt();
                    long _arg42 = data.readLong();
                    data.enforceNoDataAvail();
                    clearAppData(_arg08, _arg15, _arg25, _arg34, _arg42);
                    reply.writeNoException();
                    return true;
                case 10:
                    java.lang.String _arg09 = data.readString();
                    java.lang.String _arg16 = data.readString();
                    int _arg26 = data.readInt();
                    int _arg35 = data.readInt();
                    long _arg43 = data.readLong();
                    data.enforceNoDataAvail();
                    destroyAppData(_arg09, _arg16, _arg26, _arg35, _arg43);
                    reply.writeNoException();
                    return true;
                case 11:
                    java.lang.String _arg010 = data.readString();
                    int _arg17 = data.readInt();
                    data.enforceNoDataAvail();
                    fixupAppData(_arg010, _arg17);
                    reply.writeNoException();
                    return true;
                case 12:
                    java.lang.String _arg011 = data.readString();
                    java.lang.String[] _arg18 = data.createStringArray();
                    int _arg27 = data.readInt();
                    int _arg36 = data.readInt();
                    int _arg44 = data.readInt();
                    long[] _arg52 = data.createLongArray();
                    java.lang.String[] _arg6 = data.createStringArray();
                    data.enforceNoDataAvail();
                    long[] _result3 = getAppSize(_arg011, _arg18, _arg27, _arg36, _arg44, _arg52, _arg6);
                    reply.writeNoException();
                    reply.writeLongArray(_result3);
                    return true;
                case 13:
                    java.lang.String _arg012 = data.readString();
                    int _arg19 = data.readInt();
                    int _arg28 = data.readInt();
                    int[] _arg37 = data.createIntArray();
                    data.enforceNoDataAvail();
                    long[] _result4 = getUserSize(_arg012, _arg19, _arg28, _arg37);
                    reply.writeNoException();
                    reply.writeLongArray(_result4);
                    return true;
                case 14:
                    java.lang.String _arg013 = data.readString();
                    int _arg110 = data.readInt();
                    int _arg29 = data.readInt();
                    int[] _arg38 = data.createIntArray();
                    data.enforceNoDataAvail();
                    long[] _result5 = getExternalSize(_arg013, _arg110, _arg29, _arg38);
                    reply.writeNoException();
                    reply.writeLongArray(_result5);
                    return true;
                case 15:
                    java.lang.String _arg014 = data.readString();
                    java.lang.String[] _arg111 = data.createStringArray();
                    int _arg210 = data.readInt();
                    data.enforceNoDataAvail();
                    android.os.storage.CrateMetadata[] _result6 = getAppCrates(_arg014, _arg111, _arg210);
                    reply.writeNoException();
                    reply.writeTypedArray(_result6, 1);
                    return true;
                case 16:
                    java.lang.String _arg015 = data.readString();
                    int _arg112 = data.readInt();
                    data.enforceNoDataAvail();
                    android.os.storage.CrateMetadata[] _result7 = getUserCrates(_arg015, _arg112);
                    reply.writeNoException();
                    reply.writeTypedArray(_result7, 1);
                    return true;
                case 17:
                    java.lang.String _arg016 = data.readString();
                    int _arg113 = data.readInt();
                    int _arg211 = data.readInt();
                    long _arg39 = data.readLong();
                    data.enforceNoDataAvail();
                    setAppQuota(_arg016, _arg113, _arg211, _arg39);
                    reply.writeNoException();
                    return true;
                case 18:
                    java.lang.String _arg017 = data.readString();
                    java.lang.String _arg114 = data.readString();
                    java.lang.String _arg212 = data.readString();
                    int _arg310 = data.readInt();
                    java.lang.String _arg45 = data.readString();
                    int _arg53 = data.readInt();
                    java.lang.String _arg62 = data.readString();
                    data.enforceNoDataAvail();
                    moveCompleteApp(_arg017, _arg114, _arg212, _arg310, _arg45, _arg53, _arg62);
                    reply.writeNoException();
                    return true;
                case 19:
                    java.lang.String _arg018 = data.readString();
                    int _arg115 = data.readInt();
                    java.lang.String _arg213 = data.readString();
                    java.lang.String _arg311 = data.readString();
                    int _arg46 = data.readInt();
                    java.lang.String _arg54 = data.readString();
                    int _arg63 = data.readInt();
                    java.lang.String _arg7 = data.readString();
                    java.lang.String _arg8 = data.readString();
                    java.lang.String _arg9 = data.readString();
                    java.lang.String _arg10 = data.readString();
                    boolean _arg11 = data.readBoolean();
                    int _arg122 = data.readInt();
                    java.lang.String _arg132 = data.readString();
                    java.lang.String _arg142 = data.readString();
                    java.lang.String _arg152 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result8 = dexopt(_arg018, _arg115, _arg213, _arg311, _arg46, _arg54, _arg63, _arg7, _arg8, _arg9, _arg10, _arg11, _arg122, _arg132, _arg142, _arg152);
                    reply.writeNoException();
                    reply.writeBoolean(_result8);
                    return true;
                case 20:
                    boolean _arg019 = data.readBoolean();
                    data.enforceNoDataAvail();
                    controlDexOptBlocking(_arg019);
                    reply.writeNoException();
                    return true;
                case 21:
                    java.lang.String _arg020 = data.readString();
                    java.lang.String _arg116 = data.readString();
                    data.enforceNoDataAvail();
                    rmdex(_arg020, _arg116);
                    reply.writeNoException();
                    return true;
                case 22:
                    int _arg021 = data.readInt();
                    java.lang.String _arg117 = data.readString();
                    java.lang.String _arg214 = data.readString();
                    data.enforceNoDataAvail();
                    int _result9 = mergeProfiles(_arg021, _arg117, _arg214);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 23:
                    int _arg022 = data.readInt();
                    java.lang.String _arg118 = data.readString();
                    java.lang.String _arg215 = data.readString();
                    java.lang.String _arg312 = data.readString();
                    boolean _arg47 = data.readBoolean();
                    data.enforceNoDataAvail();
                    boolean _result10 = dumpProfiles(_arg022, _arg118, _arg215, _arg312, _arg47);
                    reply.writeNoException();
                    reply.writeBoolean(_result10);
                    return true;
                case 24:
                    java.lang.String _arg023 = data.readString();
                    int _arg119 = data.readInt();
                    java.lang.String _arg216 = data.readString();
                    java.lang.String _arg313 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result11 = copySystemProfile(_arg023, _arg119, _arg216, _arg313);
                    reply.writeNoException();
                    reply.writeBoolean(_result11);
                    return true;
                case 25:
                    java.lang.String _arg024 = data.readString();
                    java.lang.String _arg120 = data.readString();
                    data.enforceNoDataAvail();
                    clearAppProfiles(_arg024, _arg120);
                    reply.writeNoException();
                    return true;
                case 26:
                    java.lang.String _arg025 = data.readString();
                    data.enforceNoDataAvail();
                    destroyAppProfiles(_arg025);
                    reply.writeNoException();
                    return true;
                case 27:
                    java.lang.String _arg026 = data.readString();
                    java.lang.String _arg121 = data.readString();
                    data.enforceNoDataAvail();
                    deleteReferenceProfile(_arg026, _arg121);
                    reply.writeNoException();
                    return true;
                case 28:
                    int _arg027 = data.readInt();
                    java.lang.String _arg123 = data.readString();
                    java.lang.String _arg217 = data.readString();
                    java.lang.String _arg314 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result12 = createProfileSnapshot(_arg027, _arg123, _arg217, _arg314);
                    reply.writeNoException();
                    reply.writeBoolean(_result12);
                    return true;
                case 29:
                    java.lang.String _arg028 = data.readString();
                    java.lang.String _arg124 = data.readString();
                    data.enforceNoDataAvail();
                    destroyProfileSnapshot(_arg028, _arg124);
                    reply.writeNoException();
                    return true;
                case 30:
                    java.lang.String _arg029 = data.readString();
                    java.lang.String _arg125 = data.readString();
                    data.enforceNoDataAvail();
                    rmPackageDir(_arg029, _arg125);
                    reply.writeNoException();
                    return true;
                case 31:
                    java.lang.String _arg030 = data.readString();
                    long _arg126 = data.readLong();
                    int _arg218 = data.readInt();
                    data.enforceNoDataAvail();
                    freeCache(_arg030, _arg126, _arg218);
                    reply.writeNoException();
                    return true;
                case 32:
                    java.lang.String _arg031 = data.readString();
                    java.lang.String _arg127 = data.readString();
                    java.lang.String _arg219 = data.readString();
                    int _arg315 = data.readInt();
                    data.enforceNoDataAvail();
                    linkNativeLibraryDirectory(_arg031, _arg127, _arg219, _arg315);
                    reply.writeNoException();
                    return true;
                case 33:
                    java.lang.String _arg032 = data.readString();
                    java.lang.String _arg128 = data.readString();
                    java.lang.String _arg220 = data.readString();
                    data.enforceNoDataAvail();
                    createOatDir(_arg032, _arg128, _arg220);
                    reply.writeNoException();
                    return true;
                case 34:
                    java.lang.String _arg033 = data.readString();
                    java.lang.String _arg129 = data.readString();
                    java.lang.String _arg221 = data.readString();
                    java.lang.String _arg316 = data.readString();
                    data.enforceNoDataAvail();
                    linkFile(_arg033, _arg129, _arg221, _arg316);
                    reply.writeNoException();
                    return true;
                case 35:
                    java.lang.String _arg034 = data.readString();
                    java.lang.String _arg130 = data.readString();
                    java.lang.String _arg222 = data.readString();
                    java.lang.String _arg317 = data.readString();
                    data.enforceNoDataAvail();
                    moveAb(_arg034, _arg130, _arg222, _arg317);
                    reply.writeNoException();
                    return true;
                case 36:
                    java.lang.String _arg035 = data.readString();
                    java.lang.String _arg131 = data.readString();
                    java.lang.String _arg223 = data.readString();
                    java.lang.String _arg318 = data.readString();
                    data.enforceNoDataAvail();
                    long _result13 = deleteOdex(_arg035, _arg131, _arg223, _arg318);
                    reply.writeNoException();
                    reply.writeLong(_result13);
                    return true;
                case 37:
                    java.lang.String _arg036 = data.readString();
                    java.lang.String _arg133 = data.readString();
                    int _arg224 = data.readInt();
                    java.lang.String[] _arg319 = data.createStringArray();
                    java.lang.String _arg48 = data.readString();
                    int _arg55 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result14 = reconcileSecondaryDexFile(_arg036, _arg133, _arg224, _arg319, _arg48, _arg55);
                    reply.writeNoException();
                    reply.writeBoolean(_result14);
                    return true;
                case 38:
                    java.lang.String _arg037 = data.readString();
                    java.lang.String _arg134 = data.readString();
                    int _arg225 = data.readInt();
                    java.lang.String _arg320 = data.readString();
                    int _arg49 = data.readInt();
                    data.enforceNoDataAvail();
                    byte[] _result15 = hashSecondaryDexFile(_arg037, _arg134, _arg225, _arg320, _arg49);
                    reply.writeNoException();
                    reply.writeByteArray(_result15);
                    return true;
                case 39:
                    invalidateMounts();
                    reply.writeNoException();
                    return true;
                case 40:
                    java.lang.String _arg038 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result16 = isQuotaSupported(_arg038);
                    reply.writeNoException();
                    reply.writeBoolean(_result16);
                    return true;
                case 41:
                    java.lang.String _arg039 = data.readString();
                    int _arg135 = data.readInt();
                    int _arg226 = data.readInt();
                    java.lang.String _arg321 = data.readString();
                    java.lang.String _arg410 = data.readString();
                    java.lang.String _arg56 = data.readString();
                    data.enforceNoDataAvail();
                    boolean _result17 = prepareAppProfile(_arg039, _arg135, _arg226, _arg321, _arg410, _arg56);
                    reply.writeNoException();
                    reply.writeBoolean(_result17);
                    return true;
                case 42:
                    java.lang.String _arg040 = data.readString();
                    java.lang.String _arg136 = data.readString();
                    int _arg227 = data.readInt();
                    int _arg322 = data.readInt();
                    int _arg411 = data.readInt();
                    data.enforceNoDataAvail();
                    long _result18 = snapshotAppData(_arg040, _arg136, _arg227, _arg322, _arg411);
                    reply.writeNoException();
                    reply.writeLong(_result18);
                    return true;
                case 43:
                    java.lang.String _arg041 = data.readString();
                    java.lang.String _arg137 = data.readString();
                    int _arg228 = data.readInt();
                    java.lang.String _arg323 = data.readString();
                    int _arg412 = data.readInt();
                    int _arg57 = data.readInt();
                    int _arg64 = data.readInt();
                    data.enforceNoDataAvail();
                    restoreAppDataSnapshot(_arg041, _arg137, _arg228, _arg323, _arg412, _arg57, _arg64);
                    reply.writeNoException();
                    return true;
                case 44:
                    java.lang.String _arg042 = data.readString();
                    java.lang.String _arg138 = data.readString();
                    int _arg229 = data.readInt();
                    long _arg324 = data.readLong();
                    int _arg413 = data.readInt();
                    int _arg58 = data.readInt();
                    data.enforceNoDataAvail();
                    destroyAppDataSnapshot(_arg042, _arg138, _arg229, _arg324, _arg413, _arg58);
                    reply.writeNoException();
                    return true;
                case 45:
                    java.lang.String _arg043 = data.readString();
                    int _arg139 = data.readInt();
                    int[] _arg230 = data.createIntArray();
                    data.enforceNoDataAvail();
                    destroyCeSnapshotsNotSpecified(_arg043, _arg139, _arg230);
                    reply.writeNoException();
                    return true;
                case 46:
                    java.lang.String _arg044 = data.readString();
                    data.enforceNoDataAvail();
                    tryMountDataMirror(_arg044);
                    reply.writeNoException();
                    return true;
                case 47:
                    java.lang.String _arg045 = data.readString();
                    data.enforceNoDataAvail();
                    onPrivateVolumeRemoved(_arg045);
                    reply.writeNoException();
                    return true;
                case 48:
                    migrateLegacyObbData();
                    reply.writeNoException();
                    return true;
                case 49:
                    java.lang.String _arg046 = data.readString();
                    android.os.PersistableBundle _arg140 = (android.os.PersistableBundle) data.readTypedObject(android.os.PersistableBundle.CREATOR);
                    data.enforceNoDataAvail();
                    android.os.PersistableBundle _result19 = oplusCommonInterface(_arg046, _arg140);
                    reply.writeNoException();
                    reply.writeTypedObject(_result19, 1);
                    return true;
                case 50:
                    java.lang.String _arg047 = data.readString();
                    android.os.PersistableBundle _arg141 = (android.os.PersistableBundle) data.readTypedObject(android.os.PersistableBundle.CREATOR);
                    android.os.ICustomizePmsCallback _arg231 = android.os.ICustomizePmsCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    oplusCustomizeInterface(_arg047, _arg141, _arg231);
                    reply.writeNoException();
                    return true;
                case 51:
                    java.lang.String _arg048 = data.readString();
                    int _arg143 = data.readInt();
                    int _arg232 = data.readInt();
                    data.enforceNoDataAvail();
                    cleanupInvalidPackageDirs(_arg048, _arg143, _arg232);
                    reply.writeNoException();
                    return true;
                case 52:
                    java.lang.String _arg049 = data.readString();
                    java.lang.String _arg144 = data.readString();
                    java.lang.String _arg233 = data.readString();
                    java.lang.String _arg325 = data.readString();
                    data.enforceNoDataAvail();
                    int _result20 = getOdexVisibility(_arg049, _arg144, _arg233, _arg325);
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 53:
                    android.os.ParcelFileDescriptor _arg050 = (android.os.ParcelFileDescriptor) data.readTypedObject(android.os.ParcelFileDescriptor.CREATOR);
                    int _arg145 = data.readInt();
                    data.enforceNoDataAvail();
                    android.os.IInstalld.IFsveritySetupAuthToken _result21 = createFsveritySetupAuthToken(_arg050, _arg145);
                    reply.writeNoException();
                    reply.writeStrongInterface(_result21);
                    return true;
                case 54:
                    android.os.IInstalld.IFsveritySetupAuthToken _arg051 = android.os.IInstalld.IFsveritySetupAuthToken.Stub.asInterface(data.readStrongBinder());
                    java.lang.String _arg146 = data.readString();
                    java.lang.String _arg234 = data.readString();
                    data.enforceNoDataAvail();
                    int _result22 = enableFsverity(_arg051, _arg146, _arg234);
                    reply.writeNoException();
                    reply.writeInt(_result22);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.os.IInstalld {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.os.IInstalld.Stub.DESCRIPTOR;
            }

            @Override // android.os.IInstalld
            public void createUserData(java.lang.String uuid, int userId, int userSerial, int flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeInt(flags);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void destroyUserData(java.lang.String uuid, int userId, int flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void setFirstBoot() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public android.os.CreateAppDataResult createAppData(android.os.CreateAppDataArgs args) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeTypedObject(args, 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    android.os.CreateAppDataResult _result = (android.os.CreateAppDataResult) _reply.readTypedObject(android.os.CreateAppDataResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public android.os.CreateAppDataResult[] createAppDataBatched(android.os.CreateAppDataArgs[] args) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeTypedArray(args, 0);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    android.os.CreateAppDataResult[] _result = (android.os.CreateAppDataResult[]) _reply.createTypedArray(android.os.CreateAppDataResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void reconcileSdkData(android.os.ReconcileSdkDataArgs args) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeTypedObject(args, 0);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void restoreconAppData(java.lang.String uuid, java.lang.String packageName, int userId, int flags, int appId, java.lang.String seInfo) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    _data.writeInt(appId);
                    _data.writeString(seInfo);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void migrateAppData(java.lang.String uuid, java.lang.String packageName, int userId, int flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void clearAppData(java.lang.String uuid, java.lang.String packageName, int userId, int flags, long ceDataInode) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    _data.writeLong(ceDataInode);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void destroyAppData(java.lang.String uuid, java.lang.String packageName, int userId, int flags, long ceDataInode) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    _data.writeLong(ceDataInode);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void fixupAppData(java.lang.String uuid, int flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(flags);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public long[] getAppSize(java.lang.String uuid, java.lang.String[] packageNames, int userId, int flags, int appId, long[] ceDataInodes, java.lang.String[] codePaths) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    _data.writeInt(appId);
                    _data.writeLongArray(ceDataInodes);
                    _data.writeStringArray(codePaths);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public long[] getUserSize(java.lang.String uuid, int userId, int flags, int[] appIds) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    _data.writeIntArray(appIds);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public long[] getExternalSize(java.lang.String uuid, int userId, int flags, int[] appIds) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    _data.writeIntArray(appIds);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public android.os.storage.CrateMetadata[] getAppCrates(java.lang.String uuid, java.lang.String[] packageNames, int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeStringArray(packageNames);
                    _data.writeInt(userId);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    android.os.storage.CrateMetadata[] _result = (android.os.storage.CrateMetadata[]) _reply.createTypedArray(android.os.storage.CrateMetadata.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public android.os.storage.CrateMetadata[] getUserCrates(java.lang.String uuid, int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    android.os.storage.CrateMetadata[] _result = (android.os.storage.CrateMetadata[]) _reply.createTypedArray(android.os.storage.CrateMetadata.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void setAppQuota(java.lang.String uuid, int userId, int appId, long cacheQuota) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(appId);
                    _data.writeLong(cacheQuota);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void moveCompleteApp(java.lang.String fromUuid, java.lang.String toUuid, java.lang.String packageName, int appId, java.lang.String seInfo, int targetSdkVersion, java.lang.String fromCodePath) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(fromUuid);
                    _data.writeString(toUuid);
                    _data.writeString(packageName);
                    _data.writeInt(appId);
                    _data.writeString(seInfo);
                    _data.writeInt(targetSdkVersion);
                    _data.writeString(fromCodePath);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public boolean dexopt(java.lang.String apkPath, int uid, java.lang.String packageName, java.lang.String instructionSet, int dexoptNeeded, java.lang.String outputPath, int dexFlags, java.lang.String compilerFilter, java.lang.String uuid, java.lang.String sharedLibraries, java.lang.String seInfo, boolean downgrade, int targetSdkVersion, java.lang.String profileName, java.lang.String dexMetadataPath, java.lang.String compilationReason) throws java.lang.Throwable {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(apkPath);
                    _data.writeInt(uid);
                    try {
                        _data.writeString(packageName);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
                try {
                    _data.writeString(instructionSet);
                    try {
                        _data.writeInt(dexoptNeeded);
                        try {
                            _data.writeString(outputPath);
                            try {
                                _data.writeInt(dexFlags);
                                try {
                                    _data.writeString(compilerFilter);
                                    try {
                                        _data.writeString(uuid);
                                        try {
                                            _data.writeString(sharedLibraries);
                                            try {
                                                _data.writeString(seInfo);
                                            } catch (java.lang.Throwable th3) {
                                                th = th3;
                                                _reply.recycle();
                                                _data.recycle();
                                                throw th;
                                            }
                                        } catch (java.lang.Throwable th4) {
                                            th = th4;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    } catch (java.lang.Throwable th5) {
                                        th = th5;
                                        _reply.recycle();
                                        _data.recycle();
                                        throw th;
                                    }
                                } catch (java.lang.Throwable th6) {
                                    th = th6;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (java.lang.Throwable th7) {
                                th = th7;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (java.lang.Throwable th8) {
                            th = th8;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (java.lang.Throwable th9) {
                        th = th9;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeBoolean(downgrade);
                        try {
                            _data.writeInt(targetSdkVersion);
                            _data.writeString(profileName);
                            _data.writeString(dexMetadataPath);
                            _data.writeString(compilationReason);
                            this.mRemote.transact(19, _data, _reply, 0);
                            _reply.readException();
                            boolean _result = _reply.readBoolean();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (java.lang.Throwable th10) {
                            th = th10;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (java.lang.Throwable th11) {
                        th = th11;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (java.lang.Throwable th12) {
                    th = th12;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.os.IInstalld
            public void controlDexOptBlocking(boolean block) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeBoolean(block);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void rmdex(java.lang.String codePath, java.lang.String instructionSet) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(codePath);
                    _data.writeString(instructionSet);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public int mergeProfiles(int uid, java.lang.String packageName, java.lang.String profileName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public boolean dumpProfiles(int uid, java.lang.String packageName, java.lang.String profileName, java.lang.String codePath, boolean dumpClassesAndMethods) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    _data.writeString(codePath);
                    _data.writeBoolean(dumpClassesAndMethods);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public boolean copySystemProfile(java.lang.String systemProfile, int uid, java.lang.String packageName, java.lang.String profileName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(systemProfile);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void clearAppProfiles(java.lang.String packageName, java.lang.String profileName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void destroyAppProfiles(java.lang.String packageName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void deleteReferenceProfile(java.lang.String packageName, java.lang.String profileName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public boolean createProfileSnapshot(int appId, java.lang.String packageName, java.lang.String profileName, java.lang.String classpath) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    _data.writeString(classpath);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void destroyProfileSnapshot(java.lang.String packageName, java.lang.String profileName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void rmPackageDir(java.lang.String packageName, java.lang.String packageDir) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(packageDir);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void freeCache(java.lang.String uuid, long targetFreeBytes, int flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeLong(targetFreeBytes);
                    _data.writeInt(flags);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void linkNativeLibraryDirectory(java.lang.String uuid, java.lang.String packageName, java.lang.String nativeLibPath32, int userId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeString(packageName);
                    _data.writeString(nativeLibPath32);
                    _data.writeInt(userId);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void createOatDir(java.lang.String packageName, java.lang.String oatDir, java.lang.String instructionSet) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(oatDir);
                    _data.writeString(instructionSet);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void linkFile(java.lang.String packageName, java.lang.String relativePath, java.lang.String fromBase, java.lang.String toBase) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(relativePath);
                    _data.writeString(fromBase);
                    _data.writeString(toBase);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void moveAb(java.lang.String packageName, java.lang.String apkPath, java.lang.String instructionSet, java.lang.String outputPath) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(apkPath);
                    _data.writeString(instructionSet);
                    _data.writeString(outputPath);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public long deleteOdex(java.lang.String packageName, java.lang.String apkPath, java.lang.String instructionSet, java.lang.String outputPath) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(apkPath);
                    _data.writeString(instructionSet);
                    _data.writeString(outputPath);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public boolean reconcileSecondaryDexFile(java.lang.String dexPath, java.lang.String pkgName, int uid, java.lang.String[] isas, java.lang.String volume_uuid, int storage_flag) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(dexPath);
                    _data.writeString(pkgName);
                    _data.writeInt(uid);
                    _data.writeStringArray(isas);
                    _data.writeString(volume_uuid);
                    _data.writeInt(storage_flag);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public byte[] hashSecondaryDexFile(java.lang.String dexPath, java.lang.String pkgName, int uid, java.lang.String volumeUuid, int storageFlag) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(dexPath);
                    _data.writeString(pkgName);
                    _data.writeInt(uid);
                    _data.writeString(volumeUuid);
                    _data.writeInt(storageFlag);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void invalidateMounts() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public boolean isQuotaSupported(java.lang.String uuid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public boolean prepareAppProfile(java.lang.String packageName, int userId, int appId, java.lang.String profileName, java.lang.String codePath, java.lang.String dexMetadata) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(appId);
                    _data.writeString(profileName);
                    _data.writeString(codePath);
                    _data.writeString(dexMetadata);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public long snapshotAppData(java.lang.String uuid, java.lang.String packageName, int userId, int snapshotId, int storageFlags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(snapshotId);
                    _data.writeInt(storageFlags);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void restoreAppDataSnapshot(java.lang.String uuid, java.lang.String packageName, int appId, java.lang.String seInfo, int user, int snapshotId, int storageflags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeString(packageName);
                    _data.writeInt(appId);
                    _data.writeString(seInfo);
                    _data.writeInt(user);
                    _data.writeInt(snapshotId);
                    _data.writeInt(storageflags);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void destroyAppDataSnapshot(java.lang.String uuid, java.lang.String packageName, int userId, long ceSnapshotInode, int snapshotId, int storageFlags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeLong(ceSnapshotInode);
                    _data.writeInt(snapshotId);
                    _data.writeInt(storageFlags);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void destroyCeSnapshotsNotSpecified(java.lang.String uuid, int userId, int[] retainSnapshotIds) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeIntArray(retainSnapshotIds);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void tryMountDataMirror(java.lang.String volumeUuid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void onPrivateVolumeRemoved(java.lang.String volumeUuid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void migrateLegacyObbData() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public android.os.PersistableBundle oplusCommonInterface(java.lang.String funName, android.os.PersistableBundle args) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(funName);
                    _data.writeTypedObject(args, 0);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    android.os.PersistableBundle _result = (android.os.PersistableBundle) _reply.readTypedObject(android.os.PersistableBundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void oplusCustomizeInterface(java.lang.String funName, android.os.PersistableBundle args, android.os.ICustomizePmsCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(funName);
                    _data.writeTypedObject(args, 0);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public void cleanupInvalidPackageDirs(java.lang.String uuid, int userId, int flags) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public int getOdexVisibility(java.lang.String packageName, java.lang.String apkPath, java.lang.String instructionSet, java.lang.String outputPath) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(apkPath);
                    _data.writeString(instructionSet);
                    _data.writeString(outputPath);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public android.os.IInstalld.IFsveritySetupAuthToken createFsveritySetupAuthToken(android.os.ParcelFileDescriptor authFd, int uid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeTypedObject(authFd, 0);
                    _data.writeInt(uid);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                    android.os.IInstalld.IFsveritySetupAuthToken _result = android.os.IInstalld.IFsveritySetupAuthToken.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IInstalld
            public int enableFsverity(android.os.IInstalld.IFsveritySetupAuthToken authToken, java.lang.String filePath, java.lang.String packageName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IInstalld.Stub.DESCRIPTOR);
                    _data.writeStrongInterface(authToken);
                    _data.writeString(filePath);
                    _data.writeString(packageName);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }

    public interface IFsveritySetupAuthToken extends android.os.IInterface {
        public static final java.lang.String DESCRIPTOR = "android.os.IInstalld.IFsveritySetupAuthToken";

        public static class Default implements android.os.IInstalld.IFsveritySetupAuthToken {
            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return null;
            }
        }

        public static abstract class Stub extends android.os.Binder implements android.os.IInstalld.IFsveritySetupAuthToken {
            public Stub() {
                attachInterface(this, android.os.IInstalld.IFsveritySetupAuthToken.DESCRIPTOR);
            }

            public static android.os.IInstalld.IFsveritySetupAuthToken asInterface(android.os.IBinder obj) {
                if (obj == null) {
                    return null;
                }
                android.os.IInterface iin = obj.queryLocalInterface(android.os.IInstalld.IFsveritySetupAuthToken.DESCRIPTOR);
                if (iin != null && (iin instanceof android.os.IInstalld.IFsveritySetupAuthToken)) {
                    return (android.os.IInstalld.IFsveritySetupAuthToken) iin;
                }
                return new android.os.IInstalld.IFsveritySetupAuthToken.Stub.Proxy(obj);
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this;
            }

            @Override // android.os.Binder
            public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
                if (code == 1598968902) {
                    reply.writeString(android.os.IInstalld.IFsveritySetupAuthToken.DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }

            private static class Proxy implements android.os.IInstalld.IFsveritySetupAuthToken {
                private android.os.IBinder mRemote;

                Proxy(android.os.IBinder remote) {
                    this.mRemote = remote;
                }

                @Override // android.os.IInterface
                public android.os.IBinder asBinder() {
                    return this.mRemote;
                }

                public java.lang.String getInterfaceDescriptor() {
                    return android.os.IInstalld.IFsveritySetupAuthToken.DESCRIPTOR;
                }
            }
        }
    }
}
