package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class BackupManagerService extends android.app.backup.IBackupManager.Stub {
    private static final java.lang.String BACKUP_ACTIVATED_FILENAME = "backup-activated";
    private static final java.lang.String BACKUP_DISABLE_PROPERTY = "ro.backup.disable";
    private static final java.lang.String BACKUP_SUPPRESS_FILENAME = "backup-suppress";
    private static final java.lang.String BACKUP_THREAD = "backup";
    public static final boolean DEBUG = true;
    public static final boolean DEBUG_SCHEDULING = true;
    static final java.lang.String DUMP_RUNNING_USERS_MESSAGE = "Backup Manager is running for users:";
    public static final boolean MORE_DEBUG = false;
    private static final java.lang.String REMEMBER_ACTIVATED_FILENAME = "backup-remember-activated";
    public static final java.lang.String TAG = "BackupManagerService";
    static com.android.server.backup.BackupManagerService sInstance;
    private final android.content.Context mContext;
    private int mDefaultBackupUserId;
    private final android.os.Handler mHandler;
    private final java.util.Set<android.content.ComponentName> mTransportWhitelist;
    private final android.os.UserManager mUserManager;
    private final android.util.SparseArray<com.android.server.backup.UserBackupManagerService> mUserServices;
    private final java.lang.Object mStateLock = new java.lang.Object();
    private final android.content.BroadcastReceiver mUserRemovedReceiver = new com.android.server.backup.BackupManagerService.AnonymousClass1();
    private boolean mHasFirstUserUnlockedSinceBoot = false;
    public com.android.server.backup.IBackupManagerServiceExt mBackupManagerServiceExt = (com.android.server.backup.IBackupManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.backup.IBackupManagerServiceExt.class).base(this).create();
    private com.android.server.backup.IBackupManagerServiceWrapper mBackupManagerWrapper = new com.android.server.backup.BackupManagerService.BackupManagerServiceWrapper();
    private final boolean mGlobalDisable = isBackupDisabled();

    static com.android.server.backup.BackupManagerService getInstance() {
        return (com.android.server.backup.BackupManagerService) java.util.Objects.requireNonNull(sInstance);
    }

    /* JADX INFO: renamed from: com.android.server.backup.BackupManagerService$1, reason: invalid class name */
    class AnonymousClass1 extends android.content.BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            final int userId;
            if ("android.intent.action.USER_REMOVED".equals(intent.getAction()) && (userId = intent.getIntExtra("android.intent.extra.user_handle", -10000)) > 0) {
                com.android.server.backup.BackupManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.backup.BackupManagerService$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$0(userId);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(int userId) {
            com.android.server.backup.BackupManagerService.this.onRemovedNonSystemUser(userId);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public BackupManagerService(android.content.Context context) {
        this.mContext = context;
        android.os.HandlerThread handlerThread = new android.os.HandlerThread("backup", 10);
        handlerThread.start();
        this.mHandler = new android.os.Handler(handlerThread.getLooper());
        this.mUserManager = android.os.UserManager.get(context);
        this.mUserServices = new android.util.SparseArray<>();
        java.util.Set<android.content.ComponentName> transportWhitelist = com.android.server.SystemConfig.getInstance().getBackupTransportWhitelist();
        this.mTransportWhitelist = transportWhitelist == null ? java.util.Collections.emptySet() : transportWhitelist;
        this.mContext.registerReceiver(this.mUserRemovedReceiver, new android.content.IntentFilter("android.intent.action.USER_REMOVED"));
        android.os.UserHandle mainUser = getUserManager().getMainUser();
        this.mDefaultBackupUserId = mainUser != null ? mainUser.getIdentifier() : 0;
        android.util.Slog.d(TAG, "Default backup user id = " + this.mDefaultBackupUserId);
        this.mBackupManagerServiceExt.hookAfterConstructor();
        this.mBackupManagerServiceExt.initOplusBinderExtensionInConstructor(this);
    }

    android.os.Handler getBackupHandler() {
        return this.mHandler;
    }

    protected boolean isBackupDisabled() {
        return android.os.SystemProperties.getBoolean(BACKUP_DISABLE_PROPERTY, false);
    }

    protected int binderGetCallingUserId() {
        return android.os.Binder.getCallingUserHandle().getIdentifier();
    }

    protected int binderGetCallingUid() {
        return android.os.Binder.getCallingUid();
    }

    protected java.io.File getSuppressFileForUser(int userId) {
        return new java.io.File(com.android.server.backup.UserBackupManagerFiles.getBaseStateDir(userId), BACKUP_SUPPRESS_FILENAME);
    }

    protected java.io.File getRememberActivatedFileForNonSystemUser(int userId) {
        return com.android.server.backup.UserBackupManagerFiles.getStateFileInSystemDir(REMEMBER_ACTIVATED_FILENAME, userId);
    }

    protected java.io.File getActivatedFileForUser(int userId) {
        return com.android.server.backup.UserBackupManagerFiles.getStateFileInSystemDir(BACKUP_ACTIVATED_FILENAME, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRemovedNonSystemUser(int userId) {
        android.util.Slog.i(TAG, "Removing state for non system user " + userId);
        java.io.File dir = com.android.server.backup.UserBackupManagerFiles.getStateDirInSystemDir(userId);
        if (!android.os.FileUtils.deleteContentsAndDir(dir)) {
            android.util.Slog.w(TAG, "Failed to delete state dir for removed user: " + userId);
        }
    }

    private void createFile(java.io.File file) throws java.io.IOException {
        if (file.exists()) {
            return;
        }
        file.getParentFile().mkdirs();
        if (!file.createNewFile()) {
            android.util.Slog.w(TAG, "Failed to create file " + file.getPath());
        }
    }

    private void deleteFile(java.io.File file) {
        if (file.exists() && !file.delete()) {
            android.util.Slog.w(TAG, "Failed to delete file " + file.getPath());
        }
    }

    private void deactivateBackupForUserLocked(int userId) throws java.io.IOException {
        if (userId == 0 || userId == this.mDefaultBackupUserId) {
            createFile(getSuppressFileForUser(userId));
        } else {
            deleteFile(getActivatedFileForUser(userId));
        }
    }

    private void activateBackupForUserLocked(int userId) throws java.io.IOException {
        if (userId == 0 || userId == this.mDefaultBackupUserId) {
            deleteFile(getSuppressFileForUser(userId));
        } else {
            createFile(getActivatedFileForUser(userId));
        }
    }

    public boolean isUserReadyForBackup(int userId) {
        enforceCallingPermissionOnUserId(userId, "isUserReadyForBackup()");
        return this.mUserServices.size() > 0 && this.mUserServices.get(userId) != null;
    }

    private boolean isBackupActivatedForUser(int userId) {
        if (getSuppressFileForUser(0).exists()) {
            return false;
        }
        boolean isDefaultUser = userId == this.mDefaultBackupUserId;
        if (userId == 0 && !isDefaultUser) {
            return false;
        }
        if (isDefaultUser && getSuppressFileForUser(userId).exists()) {
            return false;
        }
        return isDefaultUser || getActivatedFileForUser(userId).exists();
    }

    protected android.content.Context getContext() {
        return this.mContext;
    }

    protected android.os.UserManager getUserManager() {
        return this.mUserManager;
    }

    protected void postToHandler(java.lang.Runnable runnable) {
        this.mHandler.post(runnable);
    }

    void startServiceForUser(int userId) {
        if (this.mGlobalDisable) {
            android.util.Slog.i(TAG, "Backup service not supported");
            return;
        }
        if (!isBackupActivatedForUser(userId)) {
            android.util.Slog.i(TAG, "Backup not activated for user " + userId);
        } else {
            if (this.mUserServices.get(userId) != null) {
                android.util.Slog.i(TAG, "userId " + userId + " already started, so not starting again");
                return;
            }
            android.util.Slog.i(TAG, "Starting service for user: " + userId);
            com.android.server.backup.UserBackupManagerService userBackupManagerService = com.android.server.backup.UserBackupManagerService.createAndInitializeService(userId, this.mContext, this, this.mTransportWhitelist);
            startServiceForUser(userId, userBackupManagerService);
        }
    }

    void startServiceForUser(int userId, com.android.server.backup.UserBackupManagerService userBackupManagerService) {
        this.mUserServices.put(userId, userBackupManagerService);
        android.os.Trace.traceBegin(64L, "backup enable");
        userBackupManagerService.initializeBackupEnableState();
        android.os.Trace.traceEnd(64L);
    }

    protected void stopServiceForUser(int userId) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = (com.android.server.backup.UserBackupManagerService) this.mUserServices.removeReturnOld(userId);
        if (userBackupManagerService != null) {
            userBackupManagerService.tearDownService();
            com.android.server.backup.KeyValueBackupJob.cancel(userId, this.mContext);
            com.android.server.backup.FullBackupJob.cancel(userId, this.mContext);
        }
    }

    android.util.SparseArray<com.android.server.backup.UserBackupManagerService> getUserServices() {
        return this.mUserServices;
    }

    void onStopUser(final int userId) {
        postToHandler(new java.lang.Runnable() { // from class: com.android.server.backup.BackupManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onStopUser$0(userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStopUser$0(int userId) {
        if (!this.mGlobalDisable) {
            android.util.Slog.i(TAG, "Stopping service for user: " + userId);
            stopServiceForUser(userId);
        }
    }

    public com.android.server.backup.UserBackupManagerService getUserService(int userId) {
        return this.mUserServices.get(userId);
    }

    private void enforcePermissionsOnUser(int userId) throws java.lang.SecurityException {
        boolean isRestrictedUser = userId == 0 || getUserManager().getUserInfo(userId).isManagedProfile();
        if (!isRestrictedUser) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "No permission to configure backup activity");
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "No permission to configure backup activity");
        } else {
            int caller = binderGetCallingUid();
            if (caller != 1000 && caller != 0) {
                throw new java.lang.SecurityException("No permission to configure backup activity");
            }
        }
    }

    public void setBackupServiceActive(int userId, boolean makeActive) {
        enforcePermissionsOnUser(userId);
        if (userId != 0) {
            try {
                java.io.File rememberFile = getRememberActivatedFileForNonSystemUser(userId);
                createFile(rememberFile);
                com.android.server.backup.utils.RandomAccessFileUtils.writeBoolean(rememberFile, makeActive);
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Unable to persist backup service activity", e);
            }
        }
        if (this.mGlobalDisable) {
            android.util.Slog.i(TAG, "Backup service not supported");
            return;
        }
        synchronized (this.mStateLock) {
            android.util.Slog.i(TAG, "Making backup " + (makeActive ? "" : "in") + com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE);
            if (makeActive) {
                try {
                    activateBackupForUserLocked(userId);
                } catch (java.io.IOException e2) {
                    android.util.Slog.e(TAG, "Unable to persist backup service activity");
                }
                if (getUserManager().isUserUnlocked(userId)) {
                    long oldId = android.os.Binder.clearCallingIdentity();
                    try {
                        startServiceForUser(userId);
                        android.os.Binder.restoreCallingIdentity(oldId);
                    } catch (java.lang.Throwable th) {
                        android.os.Binder.restoreCallingIdentity(oldId);
                        throw th;
                    }
                }
            } else {
                try {
                    deactivateBackupForUserLocked(userId);
                } catch (java.io.IOException e3) {
                    android.util.Slog.e(TAG, "Unable to persist backup service inactivity");
                }
                onStopUser(userId);
            }
        }
    }

    public boolean isBackupServiceActive(int userId) {
        boolean z;
        int callingUid = android.os.Binder.getCallingUid();
        if (android.app.compat.CompatChanges.isChangeEnabled(158482162L, callingUid)) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.BACKUP", "isBackupServiceActive");
        }
        synchronized (this.mStateLock) {
            z = !this.mGlobalDisable && isBackupActivatedForUser(userId);
        }
        return z;
    }

    public void dataChangedForUser(int userId, java.lang.String packageName) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            dataChanged(userId, packageName);
        }
    }

    public void dataChanged(java.lang.String packageName) throws android.os.RemoteException {
        dataChangedForUser(binderGetCallingUserId(), packageName);
    }

    public void dataChanged(int userId, java.lang.String packageName) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "dataChanged()");
        if (userBackupManagerService != null) {
            userBackupManagerService.dataChanged(packageName);
        }
    }

    public void initializeTransportsForUser(int userId, java.lang.String[] transportNames, android.app.backup.IBackupObserver observer) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            initializeTransports(userId, transportNames, observer);
        }
    }

    public void initializeTransports(int userId, java.lang.String[] transportNames, android.app.backup.IBackupObserver observer) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "initializeTransports()");
        if (userBackupManagerService != null) {
            userBackupManagerService.initializeTransports(transportNames, observer);
        }
    }

    public void clearBackupDataForUser(int userId, java.lang.String transportName, java.lang.String packageName) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            clearBackupData(userId, transportName, packageName);
        }
    }

    public void clearBackupData(int userId, java.lang.String transportName, java.lang.String packageName) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "clearBackupData()");
        if (userBackupManagerService != null) {
            userBackupManagerService.clearBackupData(transportName, packageName);
        }
    }

    public void clearBackupData(java.lang.String transportName, java.lang.String packageName) throws android.os.RemoteException {
        clearBackupDataForUser(binderGetCallingUserId(), transportName, packageName);
    }

    public void agentConnectedForUser(int userId, java.lang.String packageName, android.os.IBinder agent) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            agentConnected(userId, packageName, agent);
        }
    }

    public void agentConnected(java.lang.String packageName, android.os.IBinder agent) throws android.os.RemoteException {
        agentConnectedForUser(binderGetCallingUserId(), packageName, agent);
    }

    public void agentConnected(int userId, java.lang.String packageName, android.os.IBinder agentBinder) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "agentConnected()");
        if (userBackupManagerService != null) {
            userBackupManagerService.agentConnected(packageName, agentBinder);
        }
    }

    public void agentDisconnectedForUser(int userId, java.lang.String packageName) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            agentDisconnected(userId, packageName);
        }
    }

    public void agentDisconnected(java.lang.String packageName) throws android.os.RemoteException {
        agentDisconnectedForUser(binderGetCallingUserId(), packageName);
    }

    public void agentDisconnected(int userId, java.lang.String packageName) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "agentDisconnected()");
        if (userBackupManagerService != null) {
            userBackupManagerService.agentDisconnected(packageName);
        }
    }

    public void restoreAtInstallForUser(int userId, java.lang.String packageName, int token) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            restoreAtInstall(userId, packageName, token);
        }
    }

    public void restoreAtInstall(java.lang.String packageName, int token) throws android.os.RemoteException {
        restoreAtInstallForUser(binderGetCallingUserId(), packageName, token);
    }

    public void restoreAtInstall(int userId, java.lang.String packageName, int token) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "restoreAtInstall()");
        if (userBackupManagerService != null) {
            userBackupManagerService.restoreAtInstall(packageName, token);
        }
    }

    public void setFrameworkSchedulingEnabledForUser(int userId, boolean isEnabled) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "setFrameworkSchedulingEnabledForUser()");
        if (userBackupManagerService != null) {
            userBackupManagerService.setFrameworkSchedulingEnabled(isEnabled);
        }
    }

    public void setBackupEnabledForUser(int userId, boolean isEnabled) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            setBackupEnabled(userId, isEnabled);
        }
    }

    public void setBackupEnabled(boolean isEnabled) throws android.os.RemoteException {
        setBackupEnabledForUser(binderGetCallingUserId(), isEnabled);
    }

    public void setBackupEnabled(int userId, boolean enable) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "setBackupEnabled()");
        if (userBackupManagerService != null) {
            userBackupManagerService.setBackupEnabled(enable);
        }
    }

    public void setAutoRestoreForUser(int userId, boolean doAutoRestore) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            setAutoRestore(userId, doAutoRestore);
        }
    }

    public void setAutoRestore(boolean doAutoRestore) throws android.os.RemoteException {
        setAutoRestoreForUser(binderGetCallingUserId(), doAutoRestore);
    }

    public void setAutoRestore(int userId, boolean autoRestore) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "setAutoRestore()");
        if (userBackupManagerService != null) {
            userBackupManagerService.setAutoRestore(autoRestore);
        }
    }

    public boolean isBackupEnabledForUser(int userId) throws android.os.RemoteException {
        return isUserReadyForBackup(userId) && isBackupEnabled(userId);
    }

    public boolean isBackupEnabled() throws android.os.RemoteException {
        return isBackupEnabledForUser(binderGetCallingUserId());
    }

    public boolean isBackupEnabled(int userId) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "isBackupEnabled()");
        return userBackupManagerService != null && userBackupManagerService.isBackupEnabled();
    }

    public boolean setBackupPassword(java.lang.String currentPassword, java.lang.String newPassword) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService;
        int userId = binderGetCallingUserId();
        return isUserReadyForBackup(userId) && (userBackupManagerService = getServiceForUserIfCallerHasPermission(0, "setBackupPassword()")) != null && userBackupManagerService.setBackupPassword(currentPassword, newPassword);
    }

    public boolean hasBackupPassword() throws android.os.RemoteException {
        com.android.server.backup.UserBackupManagerService userBackupManagerService;
        int userId = binderGetCallingUserId();
        return isUserReadyForBackup(userId) && (userBackupManagerService = getServiceForUserIfCallerHasPermission(0, "hasBackupPassword()")) != null && userBackupManagerService.hasBackupPassword();
    }

    public void backupNowForUser(int userId) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            backupNow(userId);
        }
    }

    public void backupNow() throws android.os.RemoteException {
        backupNowForUser(binderGetCallingUserId());
    }

    public void backupNow(int userId) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "backupNow()");
        if (userBackupManagerService != null) {
            userBackupManagerService.backupNow();
        }
    }

    public void adbBackup(int userId, android.os.ParcelFileDescriptor fd, boolean includeApks, boolean includeObbs, boolean includeShared, boolean doWidgets, boolean doAllApps, boolean includeSystem, boolean doCompress, boolean doKeyValue, java.lang.String[] packageNames) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService;
        if (isUserReadyForBackup(userId) && (userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "adbBackup()")) != null) {
            userBackupManagerService.adbBackup(fd, includeApks, includeObbs, includeShared, doWidgets, doAllApps, includeSystem, doCompress, doKeyValue, packageNames);
        }
    }

    public void fullTransportBackupForUser(int userId, java.lang.String[] packageNames) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            fullTransportBackup(userId, packageNames);
        }
    }

    public void fullTransportBackup(int userId, java.lang.String[] packageNames) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "fullTransportBackup()");
        if (userBackupManagerService != null) {
            userBackupManagerService.fullTransportBackup(packageNames);
        }
    }

    public void adbRestore(int userId, android.os.ParcelFileDescriptor fd) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService;
        if (isUserReadyForBackup(userId) && (userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "adbRestore()")) != null) {
            userBackupManagerService.adbRestore(fd);
        }
    }

    public void acknowledgeFullBackupOrRestoreForUser(int userId, int token, boolean allow, java.lang.String curPassword, java.lang.String encryptionPassword, android.app.backup.IFullBackupRestoreObserver observer) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            acknowledgeAdbBackupOrRestore(userId, token, allow, curPassword, encryptionPassword, observer);
        }
    }

    public void acknowledgeAdbBackupOrRestore(int userId, int token, boolean allow, java.lang.String currentPassword, java.lang.String encryptionPassword, android.app.backup.IFullBackupRestoreObserver observer) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "acknowledgeAdbBackupOrRestore()");
        if (userBackupManagerService != null) {
            userBackupManagerService.acknowledgeAdbBackupOrRestore(token, allow, currentPassword, encryptionPassword, observer);
        }
    }

    public void acknowledgeFullBackupOrRestore(int token, boolean allow, java.lang.String curPassword, java.lang.String encryptionPassword, android.app.backup.IFullBackupRestoreObserver observer) throws android.os.RemoteException {
        acknowledgeFullBackupOrRestoreForUser(binderGetCallingUserId(), token, allow, curPassword, encryptionPassword, observer);
    }

    public java.lang.String getCurrentTransportForUser(int userId) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            return getCurrentTransport(userId);
        }
        return null;
    }

    public java.lang.String getCurrentTransport() throws android.os.RemoteException {
        return getCurrentTransportForUser(binderGetCallingUserId());
    }

    public java.lang.String getCurrentTransport(int userId) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "getCurrentTransport()");
        if (userBackupManagerService == null) {
            return null;
        }
        return userBackupManagerService.getCurrentTransport();
    }

    public android.content.ComponentName getCurrentTransportComponentForUser(int userId) {
        if (isUserReadyForBackup(userId)) {
            return getCurrentTransportComponent(userId);
        }
        return null;
    }

    public android.content.ComponentName getCurrentTransportComponent(int userId) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "getCurrentTransportComponent()");
        if (userBackupManagerService == null) {
            return null;
        }
        return userBackupManagerService.getCurrentTransportComponent();
    }

    public java.lang.String[] listAllTransportsForUser(int userId) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            return listAllTransports(userId);
        }
        return null;
    }

    public java.lang.String[] listAllTransports(int userId) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "listAllTransports()");
        if (userBackupManagerService == null) {
            return null;
        }
        return userBackupManagerService.listAllTransports();
    }

    public java.lang.String[] listAllTransports() throws android.os.RemoteException {
        return listAllTransportsForUser(binderGetCallingUserId());
    }

    public android.content.ComponentName[] listAllTransportComponentsForUser(int userId) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            return listAllTransportComponents(userId);
        }
        return null;
    }

    public android.content.ComponentName[] listAllTransportComponents(int userId) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "listAllTransportComponents()");
        if (userBackupManagerService == null) {
            return null;
        }
        return userBackupManagerService.listAllTransportComponents();
    }

    public java.lang.String[] getTransportWhitelist() {
        int userId = binderGetCallingUserId();
        if (!isUserReadyForBackup(userId)) {
            return null;
        }
        java.lang.String[] whitelistedTransports = new java.lang.String[this.mTransportWhitelist.size()];
        int i = 0;
        for (android.content.ComponentName component : this.mTransportWhitelist) {
            whitelistedTransports[i] = component.flattenToShortString();
            i++;
        }
        return whitelistedTransports;
    }

    public void updateTransportAttributesForUser(int userId, android.content.ComponentName transportComponent, java.lang.String name, android.content.Intent configurationIntent, java.lang.String currentDestinationString, android.content.Intent dataManagementIntent, java.lang.CharSequence dataManagementLabel) {
        if (isUserReadyForBackup(userId)) {
            updateTransportAttributes(userId, transportComponent, name, configurationIntent, currentDestinationString, dataManagementIntent, dataManagementLabel);
        }
    }

    public void updateTransportAttributes(int userId, android.content.ComponentName transportComponent, java.lang.String name, android.content.Intent configurationIntent, java.lang.String currentDestinationString, android.content.Intent dataManagementIntent, java.lang.CharSequence dataManagementLabel) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "updateTransportAttributes()");
        if (userBackupManagerService != null) {
            userBackupManagerService.updateTransportAttributes(transportComponent, name, configurationIntent, currentDestinationString, dataManagementIntent, dataManagementLabel);
        }
    }

    public java.lang.String selectBackupTransportForUser(int userId, java.lang.String transport) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            return selectBackupTransport(userId, transport);
        }
        return null;
    }

    public java.lang.String selectBackupTransport(java.lang.String transport) throws android.os.RemoteException {
        return selectBackupTransportForUser(binderGetCallingUserId(), transport);
    }

    @java.lang.Deprecated
    public java.lang.String selectBackupTransport(int userId, java.lang.String transportName) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "selectBackupTransport()");
        if (userBackupManagerService == null) {
            return null;
        }
        return userBackupManagerService.selectBackupTransport(transportName);
    }

    public void selectBackupTransportAsyncForUser(int userId, android.content.ComponentName transport, android.app.backup.ISelectBackupTransportCallback listener) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            selectBackupTransportAsync(userId, transport, listener);
        } else if (listener != null) {
            try {
                listener.onFailure(-2001);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void selectBackupTransportAsync(int userId, android.content.ComponentName transportComponent, android.app.backup.ISelectBackupTransportCallback listener) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "selectBackupTransportAsync()");
        if (userBackupManagerService != null) {
            userBackupManagerService.selectBackupTransportAsync(transportComponent, listener);
        }
    }

    public android.content.Intent getConfigurationIntentForUser(int userId, java.lang.String transport) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            return getConfigurationIntent(userId, transport);
        }
        return null;
    }

    public android.content.Intent getConfigurationIntent(java.lang.String transport) throws android.os.RemoteException {
        return getConfigurationIntentForUser(binderGetCallingUserId(), transport);
    }

    public android.content.Intent getConfigurationIntent(int userId, java.lang.String transportName) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "getConfigurationIntent()");
        if (userBackupManagerService == null) {
            return null;
        }
        return userBackupManagerService.getConfigurationIntent(transportName);
    }

    public java.lang.String getDestinationStringForUser(int userId, java.lang.String transport) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            return getDestinationString(userId, transport);
        }
        return null;
    }

    public java.lang.String getDestinationString(java.lang.String transport) throws android.os.RemoteException {
        return getDestinationStringForUser(binderGetCallingUserId(), transport);
    }

    public java.lang.String getDestinationString(int userId, java.lang.String transportName) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "getDestinationString()");
        if (userBackupManagerService == null) {
            return null;
        }
        return userBackupManagerService.getDestinationString(transportName);
    }

    public android.content.Intent getDataManagementIntentForUser(int userId, java.lang.String transport) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            return getDataManagementIntent(userId, transport);
        }
        return null;
    }

    public android.content.Intent getDataManagementIntent(java.lang.String transport) throws android.os.RemoteException {
        return getDataManagementIntentForUser(binderGetCallingUserId(), transport);
    }

    public android.content.Intent getDataManagementIntent(int userId, java.lang.String transportName) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "getDataManagementIntent()");
        if (userBackupManagerService == null) {
            return null;
        }
        return userBackupManagerService.getDataManagementIntent(transportName);
    }

    public java.lang.CharSequence getDataManagementLabelForUser(int userId, java.lang.String transport) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            return getDataManagementLabel(userId, transport);
        }
        return null;
    }

    public java.lang.CharSequence getDataManagementLabel(int userId, java.lang.String transportName) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "getDataManagementLabel()");
        if (userBackupManagerService == null) {
            return null;
        }
        return userBackupManagerService.getDataManagementLabel(transportName);
    }

    public android.app.backup.IRestoreSession beginRestoreSessionForUser(int userId, java.lang.String packageName, java.lang.String transportID) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            return beginRestoreSession(userId, packageName, transportID);
        }
        return null;
    }

    public android.app.backup.IRestoreSession beginRestoreSession(int userId, java.lang.String packageName, java.lang.String transportName) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "beginRestoreSession()");
        if (userBackupManagerService == null) {
            return null;
        }
        return userBackupManagerService.beginRestoreSession(packageName, transportName);
    }

    public void opCompleteForUser(int userId, int token, long result) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            opComplete(userId, token, result);
        }
    }

    public void opComplete(int token, long result) throws android.os.RemoteException {
        opCompleteForUser(binderGetCallingUserId(), token, result);
    }

    public void opComplete(int userId, int token, long result) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "opComplete()");
        if (userBackupManagerService != null) {
            userBackupManagerService.opComplete(token, result);
        }
    }

    public long getAvailableRestoreTokenForUser(int userId, java.lang.String packageName) {
        if (isUserReadyForBackup(userId)) {
            return getAvailableRestoreToken(userId, packageName);
        }
        return 0L;
    }

    public long getAvailableRestoreToken(int userId, java.lang.String packageName) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "getAvailableRestoreToken()");
        if (userBackupManagerService == null) {
            return 0L;
        }
        return userBackupManagerService.getAvailableRestoreToken(packageName);
    }

    public boolean isAppEligibleForBackupForUser(int userId, java.lang.String packageName) {
        return isUserReadyForBackup(userId) && isAppEligibleForBackup(userId, packageName);
    }

    public boolean isAppEligibleForBackup(int userId, java.lang.String packageName) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "isAppEligibleForBackup()");
        return userBackupManagerService != null && userBackupManagerService.isAppEligibleForBackup(packageName);
    }

    public java.lang.String[] filterAppsEligibleForBackupForUser(int userId, java.lang.String[] packages) {
        if (isUserReadyForBackup(userId)) {
            return filterAppsEligibleForBackup(userId, packages);
        }
        return null;
    }

    public java.lang.String[] filterAppsEligibleForBackup(int userId, java.lang.String[] packages) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "filterAppsEligibleForBackup()");
        if (userBackupManagerService == null) {
            return null;
        }
        return userBackupManagerService.filterAppsEligibleForBackup(packages);
    }

    public int requestBackupForUser(int userId, java.lang.String[] packages, android.app.backup.IBackupObserver observer, android.app.backup.IBackupManagerMonitor monitor, int flags) throws android.os.RemoteException {
        if (!isUserReadyForBackup(userId)) {
            return -2001;
        }
        return requestBackup(userId, packages, observer, monitor, flags);
    }

    public int requestBackup(java.lang.String[] packages, android.app.backup.IBackupObserver observer, android.app.backup.IBackupManagerMonitor monitor, int flags) throws android.os.RemoteException {
        return requestBackup(binderGetCallingUserId(), packages, observer, monitor, flags);
    }

    public int requestBackup(int userId, java.lang.String[] packages, android.app.backup.IBackupObserver observer, android.app.backup.IBackupManagerMonitor monitor, int flags) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "requestBackup()");
        if (userBackupManagerService == null) {
            return -2001;
        }
        return userBackupManagerService.requestBackup(packages, observer, monitor, flags);
    }

    public void cancelBackupsForUser(int userId) throws android.os.RemoteException {
        if (isUserReadyForBackup(userId)) {
            cancelBackups(userId);
        }
    }

    public void cancelBackups() throws android.os.RemoteException {
        cancelBackupsForUser(binderGetCallingUserId());
    }

    public void cancelBackups(int userId) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "cancelBackups()");
        if (userBackupManagerService != null) {
            userBackupManagerService.cancelBackups();
        }
    }

    public android.os.UserHandle getUserForAncestralSerialNumber(long ancestralSerialNumber) {
        if (this.mGlobalDisable) {
            return null;
        }
        int callingUserId = android.os.Binder.getCallingUserHandle().getIdentifier();
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            int[] userIds = getUserManager().getProfileIds(callingUserId, false);
            android.os.Binder.restoreCallingIdentity(oldId);
            for (int userId : userIds) {
                com.android.server.backup.UserBackupManagerService userBackupManagerService = this.mUserServices.get(userId);
                if (userBackupManagerService != null && userBackupManagerService.getAncestralSerialNumber() == ancestralSerialNumber) {
                    return android.os.UserHandle.of(userId);
                }
            }
            return null;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(oldId);
            throw th;
        }
    }

    public void setAncestralSerialNumber(long ancestralSerialNumber) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService;
        if (!this.mGlobalDisable && (userBackupManagerService = getServiceForUserIfCallerHasPermission(android.os.Binder.getCallingUserHandle().getIdentifier(), "setAncestralSerialNumber()")) != null) {
            userBackupManagerService.setAncestralSerialNumber(ancestralSerialNumber);
        }
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(this.mContext, TAG, pw)) {
            return;
        }
        java.lang.String op = nextArg(args, 0);
        int argIndex = 0 + 1;
        if ("--help".equals(op) || "-h".equals(op)) {
            showDumpUsage(pw);
            return;
        }
        if (com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_USERS.equals(op)) {
            pw.print(DUMP_RUNNING_USERS_MESSAGE);
            for (int i = 0; i < this.mUserServices.size(); i++) {
                com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(this.mUserServices.keyAt(i), "dump()");
                if (userBackupManagerService != null) {
                    pw.print(" " + userBackupManagerService.getUserId());
                }
            }
            pw.println();
            return;
        }
        if ("--user".equals(op)) {
            java.lang.String userArg = nextArg(args, argIndex);
            int i2 = argIndex + 1;
            if (userArg == null) {
                showDumpUsage(pw);
                return;
            }
            int userId = android.os.UserHandle.parseUserArg(userArg);
            com.android.server.backup.UserBackupManagerService userBackupManagerService2 = getServiceForUserIfCallerHasPermission(userId, "dump()");
            if (userBackupManagerService2 != null) {
                userBackupManagerService2.dump(fd, pw, args);
                return;
            }
            return;
        }
        for (int i3 = 0; i3 < this.mUserServices.size(); i3++) {
            com.android.server.backup.UserBackupManagerService userBackupManagerService3 = getServiceForUserIfCallerHasPermission(this.mUserServices.keyAt(i3), "dump()");
            if (userBackupManagerService3 != null) {
                userBackupManagerService3.dump(fd, pw, args);
            }
        }
    }

    private java.lang.String nextArg(java.lang.String[] args, int argIndex) {
        if (argIndex >= args.length) {
            return null;
        }
        return args[argIndex];
    }

    private static void showDumpUsage(java.io.PrintWriter pw) {
        pw.println("'dumpsys backup' optional arguments:");
        pw.println("  --help    : this help text");
        pw.println("  a[gents] : dump information about defined backup agents");
        pw.println("  transportclients : dump information about transport clients");
        pw.println("  transportstats : dump transport stats");
        pw.println("  users    : dump the list of users for which backup service is running");
        pw.println("  --user <userId> : dump information for user userId");
    }

    public boolean beginFullBackup(int userId, com.android.server.backup.FullBackupJob scheduledJob) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService;
        return isUserReadyForBackup(userId) && (userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "beginFullBackup()")) != null && userBackupManagerService.beginFullBackup(scheduledJob);
    }

    public void endFullBackup(int userId) {
        com.android.server.backup.UserBackupManagerService userBackupManagerService;
        if (isUserReadyForBackup(userId) && (userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "endFullBackup()")) != null) {
            userBackupManagerService.endFullBackup();
        }
    }

    public void excludeKeysFromRestore(java.lang.String packageName, java.util.List<java.lang.String> keys) {
        int userId = android.os.Binder.getCallingUserHandle().getIdentifier();
        if (!isUserReadyForBackup(userId)) {
            android.util.Slog.w(TAG, "Returning from excludeKeysFromRestore as backup for user" + userId + " is not initialized yet");
            return;
        }
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "excludeKeysFromRestore()");
        if (userBackupManagerService != null) {
            userBackupManagerService.excludeKeysFromRestore(packageName, keys);
        }
    }

    public void reportDelayedRestoreResult(java.lang.String packageName, java.util.List<android.app.backup.BackupRestoreEventLogger.DataTypeResult> results) {
        int userId = android.os.Binder.getCallingUserHandle().getIdentifier();
        if (!isUserReadyForBackup(userId)) {
            android.util.Slog.w(TAG, "Returning from reportDelayedRestoreResult as backup for user" + userId + " is not initialized yet");
            return;
        }
        com.android.server.backup.UserBackupManagerService userBackupManagerService = getServiceForUserIfCallerHasPermission(userId, "reportDelayedRestoreResult()");
        if (userBackupManagerService != null) {
            long oldId = android.os.Binder.clearCallingIdentity();
            try {
                userBackupManagerService.reportDelayedRestoreResult(packageName, results);
            } finally {
                android.os.Binder.restoreCallingIdentity(oldId);
            }
        }
    }

    com.android.server.backup.UserBackupManagerService getServiceForUserIfCallerHasPermission(int userId, java.lang.String caller) {
        enforceCallingPermissionOnUserId(userId, caller);
        com.android.server.backup.UserBackupManagerService userBackupManagerService = this.mUserServices.get(userId);
        if (userBackupManagerService == null) {
            android.util.Slog.w(TAG, "Called " + caller + " for unknown user: " + userId);
        }
        return userBackupManagerService;
    }

    void enforceCallingPermissionOnUserId(int userId, java.lang.String message) {
        if (binderGetCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", message);
        }
    }

    public static class Lifecycle extends com.android.server.SystemService {
        public Lifecycle(android.content.Context context) {
            this(context, new com.android.server.backup.BackupManagerService(context));
        }

        Lifecycle(android.content.Context context, com.android.server.backup.BackupManagerService backupManagerService) {
            super(context);
            com.android.server.backup.BackupManagerService.sInstance = backupManagerService;
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            publishService("backup", com.android.server.backup.BackupManagerService.sInstance);
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(final com.android.server.SystemService.TargetUser user) {
            com.android.server.backup.BackupManagerService.sInstance.postToHandler(new java.lang.Runnable() { // from class: com.android.server.backup.BackupManagerService$Lifecycle$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.backup.BackupManagerService.Lifecycle.lambda$onUserUnlocking$0(user);
                }
            });
        }

        static /* synthetic */ void lambda$onUserUnlocking$0(com.android.server.SystemService.TargetUser user) {
            com.android.server.backup.BackupManagerService.sInstance.updateDefaultBackupUserIdIfNeeded();
            com.android.server.backup.BackupManagerService.sInstance.startServiceForUser(user.getUserIdentifier());
            com.android.server.backup.BackupManagerService.sInstance.mHasFirstUserUnlockedSinceBoot = true;
        }

        @Override // com.android.server.SystemService
        public void onUserStopping(com.android.server.SystemService.TargetUser user) {
            com.android.server.backup.BackupManagerService.sInstance.onStopUser(user.getUserIdentifier());
        }

        void publishService(java.lang.String name, android.os.IBinder service) {
            publishBinderService(name, service);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDefaultBackupUserIdIfNeeded() {
        android.os.UserHandle mainUser;
        if (!this.mHasFirstUserUnlockedSinceBoot && this.mDefaultBackupUserId == 0 && (mainUser = getUserManager().getMainUser()) != null && this.mDefaultBackupUserId != mainUser.getIdentifier()) {
            int oldDefaultBackupUserId = this.mDefaultBackupUserId;
            this.mDefaultBackupUserId = mainUser.getIdentifier();
            if (!isBackupActivatedForUser(oldDefaultBackupUserId)) {
                stopServiceForUser(oldDefaultBackupUserId);
            }
            android.util.Slog.i(TAG, "Default backup user changed from " + oldDefaultBackupUserId + " to " + this.mDefaultBackupUserId);
        }
    }

    public com.android.server.backup.IBackupManagerServiceWrapper getWrapper() {
        return this.mBackupManagerWrapper;
    }

    private class BackupManagerServiceWrapper implements com.android.server.backup.IBackupManagerServiceWrapper {
        private BackupManagerServiceWrapper() {
        }

        @Override // com.android.server.backup.IBackupManagerServiceWrapper
        public com.android.server.backup.IBackupManagerServiceExt getExtImpl() {
            return com.android.server.backup.BackupManagerService.this.mBackupManagerServiceExt;
        }
    }
}
