package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
public class LockSettingsService extends com.android.internal.widget.ILockSettings.Stub {
    private static final java.lang.String BIOMETRIC_PERMISSION = "android.permission.MANAGE_BIOMETRIC";
    private static final int GK_PW_HANDLE_STORE_DURATION_MS = 600000;
    private static final int HEADLESS_VENDOR_AUTH_SECRET_LENGTH = 32;
    private static final java.lang.String LSKF_LAST_CHANGED_TIME_KEY = "sp-handle-ts";
    private static final java.lang.String MIGRATED_FRP2 = "migrated_frp2";
    private static final java.lang.String MIGRATED_KEYSTORE_NS = "migrated_keystore_namespace";
    private static final java.lang.String MIGRATED_SP_CE_ONLY = "migrated_all_users_to_sp_and_bound_ce";
    private static final java.lang.String MIGRATED_SP_FULL = "migrated_all_users_to_sp_and_bound_keys";
    private static final java.lang.String PERMISSION = "android.permission.ACCESS_KEYGUARD_SECURE_STORAGE";
    private static final java.lang.String PREV_LSKF_BASED_PROTECTOR_ID_KEY = "prev-sp-handle";
    private static final int PROFILE_KEY_IV_SIZE = 12;
    private static final java.lang.String PROFILE_KEY_NAME_DECRYPT = "profile_key_name_decrypt_";
    private static final java.lang.String PROFILE_KEY_NAME_ENCRYPT = "profile_key_name_encrypt_";
    private static final java.lang.String SEPARATE_PROFILE_CHALLENGE_KEY = "lockscreen.profilechallenge";
    private static final java.lang.String TAG = "LockSettingsService";
    private static final java.lang.String USER_SERIAL_NUMBER_KEY = "serial-number";
    private final android.app.IActivityManager mActivityManager;
    protected byte[] mAuthSecret;
    protected android.hardware.authsecret.IAuthSecret mAuthSecretService;
    private final com.android.server.locksettings.BiometricDeferredQueue mBiometricDeferredQueue;
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private final android.os.storage.ICeStorageLockEventListener mCeStorageLockEventListener;
    private final android.content.Context mContext;
    private final com.android.server.locksettings.LockSettingsService.DeviceProvisionedObserver mDeviceProvisionedObserver;
    private android.util.SparseIntArray mEarlyCreatedUsers;
    private android.util.SparseIntArray mEarlyRemovedUsers;
    protected android.service.gatekeeper.IGateKeeperService mGateKeeperService;
    private final android.util.LongSparseArray<byte[]> mGatekeeperPasswords;
    protected final android.os.Handler mHandler;
    protected boolean mHasSecureLockScreen;
    protected final java.lang.Object mHeadlessAuthSecretLock;
    private final com.android.server.locksettings.LockSettingsService.Injector mInjector;
    private final java.security.KeyStore mKeyStore;
    private final android.security.KeyStoreAuthorization mKeyStoreAuthorization;
    public com.android.server.locksettings.ILockSettingsServiceExt mLockSettingsServiceExt;
    private com.android.server.locksettings.LockSettingsService.LockSettingsServiceWrapper mLockSettingsServiceWrapper;
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.internal.widget.LockSettingsStateListener> mLockSettingsStateListeners;
    private final android.app.NotificationManager mNotificationManager;
    private final com.android.server.locksettings.RebootEscrowManager mRebootEscrowManager;
    private final com.android.server.locksettings.recoverablekeystore.RecoverableKeyStoreManager mRecoverableKeyStoreManager;
    private android.hardware.IRedLoggerExt mRedLoggerExt;
    private final java.lang.Object mSeparateChallengeLock;
    private final com.android.server.locksettings.SyntheticPasswordManager mSpManager;
    protected final com.android.server.locksettings.LockSettingsStorage mStorage;
    private final android.os.storage.IStorageManager mStorageManager;
    private final android.os.storage.StorageManagerInternal mStorageManagerInternal;
    private final com.android.server.locksettings.LockSettingsStrongAuth mStrongAuth;
    private final com.android.server.locksettings.LockSettingsService.SynchronizedStrongAuthTracker mStrongAuthTracker;
    private boolean mThirdPartyAppsStarted;
    private final com.android.server.locksettings.UnifiedProfilePasswordCache mUnifiedProfilePasswordCache;
    private final java.lang.Object mUserCreationAndRemovalLock;
    protected final android.os.UserManager mUserManager;
    private java.util.HashMap<android.os.UserHandle, android.os.UserManager> mUserManagerCache;
    private final android.util.SparseArray<android.app.admin.PasswordMetrics> mUserPasswordMetrics;
    private static final boolean FIX_UNLOCKED_DEVICE_REQUIRED_KEYS = android.security.Flags.fixUnlockedDeviceRequiredKeysV2();
    private static final java.lang.String DEFAULT_PASSWORD = "default_password";
    private static java.lang.String mSavePassword = DEFAULT_PASSWORD;
    private static final int[] SYSTEM_CREDENTIAL_UIDS = {1016, 0, 1000};

    public static final class Lifecycle extends com.android.server.SystemService {
        private com.android.server.locksettings.LockSettingsService mLockSettingsService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            android.security.keystore2.AndroidKeyStoreProvider.install();
            this.mLockSettingsService = new com.android.server.locksettings.LockSettingsService(getContext());
            publishBinderService("lock_settings", this.mLockSettingsService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            super.onBootPhase(phase);
            if (phase == 550) {
                this.mLockSettingsService.migrateOldDataAfterSystemReady();
                this.mLockSettingsService.deleteRepairModePersistentDataIfNeeded();
            } else if (phase == 1000) {
                this.mLockSettingsService.loadEscrowData();
            }
        }

        @Override // com.android.server.SystemService
        public void onUserStarting(com.android.server.SystemService.TargetUser user) {
            this.mLockSettingsService.onUserStarting(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
            this.mLockSettingsService.onUserUnlocking(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserStopped(com.android.server.SystemService.TargetUser user) {
            this.mLockSettingsService.onUserStopped(user.getUserIdentifier());
        }
    }

    protected static class SynchronizedStrongAuthTracker extends com.android.internal.widget.LockPatternUtils.StrongAuthTracker {
        public SynchronizedStrongAuthTracker(android.content.Context context) {
            super(context);
        }

        protected void handleStrongAuthRequiredChanged(int strongAuthFlags, int userId) {
            synchronized (this) {
                super.handleStrongAuthRequiredChanged(strongAuthFlags, userId);
            }
        }

        public int getStrongAuthForUser(int userId) {
            int strongAuthForUser;
            synchronized (this) {
                strongAuthForUser = super.getStrongAuthForUser(userId);
            }
            return strongAuthForUser;
        }

        void register(com.android.server.locksettings.LockSettingsStrongAuth strongAuth) {
            strongAuth.registerStrongAuthTracker(getStub());
        }
    }

    private com.android.internal.widget.LockscreenCredential generateRandomProfilePassword() {
        byte[] randomLockSeed = com.android.server.locksettings.SecureRandomUtils.randomBytes(40);
        char[] newPasswordChars = libcore.util.HexEncoding.encode(randomLockSeed);
        byte[] newPassword = new byte[newPasswordChars.length];
        for (int i = 0; i < newPasswordChars.length; i++) {
            newPassword[i] = (byte) newPasswordChars[i];
        }
        com.android.internal.widget.LockscreenCredential credential = com.android.internal.widget.LockscreenCredential.createUnifiedProfilePassword(newPassword);
        java.util.Arrays.fill(newPasswordChars, (char) 0);
        java.util.Arrays.fill(newPassword, (byte) 0);
        java.util.Arrays.fill(randomLockSeed, (byte) 0);
        return credential;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tieProfileLockIfNecessary(int profileUserId, com.android.internal.widget.LockscreenCredential profileUserPassword) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.InvalidKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
        android.content.pm.UserInfo parent;
        if (this.mLockSettingsServiceWrapper.getExtImpl().hooktieManagedProfileLockIfNecessary(profileUserId, profileUserPassword) || !isCredentialSharableWithParent(profileUserId) || getSeparateProfileChallengeEnabledInternal(profileUserId) || this.mStorage.hasChildProfileLock(profileUserId) || (parent = this.mUserManager.getProfileParent(profileUserId)) == null) {
            return;
        }
        if (!isUserSecure(parent.id) && !profileUserPassword.isNone()) {
            com.android.server.utils.Slogf.i(TAG, "Clearing password for profile user %d to match parent", java.lang.Integer.valueOf(profileUserId));
            setLockCredentialInternal(com.android.internal.widget.LockscreenCredential.createNone(), profileUserPassword, profileUserId, true);
            return;
        }
        try {
            long parentSid = getGateKeeperService().getSecureUserId(parent.id);
            if (parentSid == 0) {
                return;
            }
            com.android.internal.widget.LockscreenCredential unifiedProfilePassword = generateRandomProfilePassword();
            try {
                setLockCredentialInternal(unifiedProfilePassword, profileUserPassword, profileUserId, true);
                tieProfileLockToParent(profileUserId, parent.id, unifiedProfilePassword);
                this.mUnifiedProfilePasswordCache.storePassword(profileUserId, unifiedProfilePassword, parentSid);
                if (unifiedProfilePassword != null) {
                    unifiedProfilePassword.close();
                }
            } catch (java.lang.Throwable th) {
                if (unifiedProfilePassword != null) {
                    try {
                        unifiedProfilePassword.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to talk to GateKeeper service", e);
        }
    }

    static class Injector {
        protected android.content.Context mContext;
        private android.os.Handler mHandler;
        private com.android.server.ServiceThread mHandlerThread;

        public Injector(android.content.Context context) {
            this.mContext = context;
        }

        public android.content.Context getContext() {
            return this.mContext;
        }

        public com.android.server.ServiceThread getServiceThread() {
            if (this.mHandlerThread == null) {
                this.mHandlerThread = new com.android.server.ServiceThread(com.android.server.locksettings.LockSettingsService.TAG, 10, true);
                this.mHandlerThread.start();
            }
            return this.mHandlerThread;
        }

        public android.os.Handler getHandler(com.android.server.ServiceThread handlerThread) {
            if (this.mHandler == null) {
                this.mHandler = new android.os.Handler(handlerThread.getLooper());
            }
            return this.mHandler;
        }

        public com.android.server.locksettings.LockSettingsStorage getStorage() {
            final com.android.server.locksettings.LockSettingsStorage storage = new com.android.server.locksettings.LockSettingsStorage(this.mContext);
            storage.setDatabaseOnCreateCallback(new com.android.server.locksettings.LockSettingsStorage.Callback() { // from class: com.android.server.locksettings.LockSettingsService.Injector.1
                @Override // com.android.server.locksettings.LockSettingsStorage.Callback
                public void initialize(android.database.sqlite.SQLiteDatabase db) {
                    boolean lockScreenDisable = android.os.SystemProperties.getBoolean("ro.lockscreen.disable.default", false);
                    if (lockScreenDisable) {
                        storage.writeKeyValue(db, "lockscreen.disabled", "1", 0);
                    }
                }
            });
            return storage;
        }

        public com.android.server.locksettings.LockSettingsStrongAuth getStrongAuth() {
            return new com.android.server.locksettings.LockSettingsStrongAuth(this.mContext);
        }

        public com.android.server.locksettings.LockSettingsService.SynchronizedStrongAuthTracker getStrongAuthTracker() {
            return new com.android.server.locksettings.LockSettingsService.SynchronizedStrongAuthTracker(this.mContext);
        }

        public android.app.IActivityManager getActivityManager() {
            return android.app.ActivityManager.getService();
        }

        public android.app.NotificationManager getNotificationManager() {
            return (android.app.NotificationManager) this.mContext.getSystemService("notification");
        }

        public android.os.UserManager getUserManager() {
            return (android.os.UserManager) this.mContext.getSystemService("user");
        }

        public com.android.server.pm.UserManagerInternal getUserManagerInternal() {
            return (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        }

        public android.app.admin.DevicePolicyManager getDevicePolicyManager() {
            return (android.app.admin.DevicePolicyManager) this.mContext.getSystemService("device_policy");
        }

        public android.app.admin.DeviceStateCache getDeviceStateCache() {
            return android.app.admin.DeviceStateCache.getInstance();
        }

        public com.android.server.locksettings.recoverablekeystore.RecoverableKeyStoreManager getRecoverableKeyStoreManager() {
            return com.android.server.locksettings.recoverablekeystore.RecoverableKeyStoreManager.getInstance(this.mContext);
        }

        public android.os.storage.IStorageManager getStorageManager() {
            android.os.IBinder service = android.os.ServiceManager.getService("mount");
            if (service != null) {
                return android.os.storage.IStorageManager.Stub.asInterface(service);
            }
            return null;
        }

        public android.os.storage.StorageManagerInternal getStorageManagerInternal() {
            return (android.os.storage.StorageManagerInternal) com.android.server.LocalServices.getService(android.os.storage.StorageManagerInternal.class);
        }

        public com.android.server.locksettings.SyntheticPasswordManager getSyntheticPasswordManager(com.android.server.locksettings.LockSettingsStorage storage) {
            return new com.android.server.locksettings.SyntheticPasswordManager(getContext(), storage, getUserManager(), new com.android.server.locksettings.PasswordSlotManager());
        }

        public com.android.server.locksettings.RebootEscrowManager getRebootEscrowManager(com.android.server.locksettings.RebootEscrowManager.Callbacks callbacks, com.android.server.locksettings.LockSettingsStorage storage) {
            return new com.android.server.locksettings.RebootEscrowManager(this.mContext, callbacks, storage, getHandler(getServiceThread()), getUserManagerInternal());
        }

        public int binderGetCallingUid() {
            return android.os.Binder.getCallingUid();
        }

        public boolean isGsiRunning() {
            return com.android.internal.widget.LockPatternUtils.isGsiRunning();
        }

        public android.hardware.fingerprint.FingerprintManager getFingerprintManager() {
            if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
                return (android.hardware.fingerprint.FingerprintManager) this.mContext.getSystemService("fingerprint");
            }
            return null;
        }

        public android.hardware.face.FaceManager getFaceManager() {
            if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.biometrics.face")) {
                return (android.hardware.face.FaceManager) this.mContext.getSystemService("face");
            }
            return null;
        }

        public android.hardware.biometrics.BiometricManager getBiometricManager() {
            return (android.hardware.biometrics.BiometricManager) this.mContext.getSystemService("biometric");
        }

        public java.security.KeyStore getKeyStore() {
            try {
                java.security.KeyStore ks = java.security.KeyStore.getInstance(com.android.server.locksettings.SyntheticPasswordCrypto.androidKeystoreProviderName());
                ks.load(new android.security.keystore2.AndroidKeyStoreLoadStoreParameter(com.android.server.locksettings.SyntheticPasswordCrypto.keyNamespace()));
                return ks;
            } catch (java.lang.Exception e) {
                throw new java.lang.IllegalStateException("Cannot load keystore", e);
            }
        }

        public android.security.KeyStoreAuthorization getKeyStoreAuthorization() {
            return android.security.KeyStoreAuthorization.getInstance();
        }

        public com.android.server.locksettings.UnifiedProfilePasswordCache getUnifiedProfilePasswordCache(java.security.KeyStore ks) {
            return new com.android.server.locksettings.UnifiedProfilePasswordCache(ks);
        }

        public boolean isHeadlessSystemUserMode() {
            return android.os.UserManager.isHeadlessSystemUserMode();
        }

        public boolean isMainUserPermanentAdmin() {
            return android.content.res.Resources.getSystem().getBoolean(android.R.bool.config_guestUserEphemeral);
        }
    }

    public LockSettingsService(android.content.Context context) {
        this(new com.android.server.locksettings.LockSettingsService.Injector(context));
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected LockSettingsService(com.android.server.locksettings.LockSettingsService.Injector injector) {
        this.mSeparateChallengeLock = new java.lang.Object();
        this.mDeviceProvisionedObserver = new com.android.server.locksettings.LockSettingsService.DeviceProvisionedObserver();
        this.mUserCreationAndRemovalLock = new java.lang.Object();
        this.mEarlyCreatedUsers = new android.util.SparseIntArray();
        this.mEarlyRemovedUsers = new android.util.SparseIntArray();
        this.mUserPasswordMetrics = new android.util.SparseArray<>();
        this.mHeadlessAuthSecretLock = new java.lang.Object();
        this.mUserManagerCache = new java.util.HashMap<>();
        this.mLockSettingsStateListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.mRedLoggerExt = (android.hardware.IRedLoggerExt) system.ext.loader.core.ExtLoader.type(android.hardware.IRedLoggerExt.class).create();
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.locksettings.LockSettingsService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if ("android.intent.action.USER_ADDED".equals(intent.getAction())) {
                    if (!com.android.server.locksettings.LockSettingsService.FIX_UNLOCKED_DEVICE_REQUIRED_KEYS) {
                        int userHandle = intent.getIntExtra("android.intent.extra.user_handle", 0);
                        android.security.AndroidKeyStoreMaintenance.onUserAdded(userHandle);
                        return;
                    }
                    return;
                }
                if ("android.intent.action.USER_STARTING".equals(intent.getAction())) {
                    int userHandle2 = intent.getIntExtra("android.intent.extra.user_handle", 0);
                    com.android.server.locksettings.LockSettingsService.this.mStorage.prefetchUser(userHandle2);
                } else if ("android.intent.action.LOCALE_CHANGED".equals(intent.getAction())) {
                    com.android.server.locksettings.LockSettingsService.this.updateActivatedEncryptionNotifications("locale changed");
                }
            }
        };
        this.mCeStorageLockEventListener = new com.android.server.locksettings.LockSettingsService.AnonymousClass3();
        this.mLockSettingsServiceWrapper = new com.android.server.locksettings.LockSettingsService.LockSettingsServiceWrapper();
        this.mLockSettingsServiceExt = (com.android.server.locksettings.ILockSettingsServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.locksettings.ILockSettingsServiceExt.class).base(this).create();
        this.mInjector = injector;
        this.mContext = injector.getContext();
        this.mKeyStore = injector.getKeyStore();
        this.mKeyStoreAuthorization = injector.getKeyStoreAuthorization();
        this.mRecoverableKeyStoreManager = injector.getRecoverableKeyStoreManager();
        this.mHandler = injector.getHandler(injector.getServiceThread());
        this.mStrongAuth = injector.getStrongAuth();
        this.mActivityManager = injector.getActivityManager();
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.USER_ADDED");
        intentFilter.addAction("android.intent.action.USER_STARTING");
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        injector.getContext().registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, intentFilter, null, null);
        this.mStorage = injector.getStorage();
        this.mNotificationManager = injector.getNotificationManager();
        this.mUserManager = injector.getUserManager();
        this.mStorageManager = injector.getStorageManager();
        this.mStorageManagerInternal = injector.getStorageManagerInternal();
        this.mStrongAuthTracker = injector.getStrongAuthTracker();
        this.mStrongAuthTracker.register(this.mStrongAuth);
        this.mGatekeeperPasswords = new android.util.LongSparseArray<>();
        this.mSpManager = injector.getSyntheticPasswordManager(this.mStorage);
        this.mUnifiedProfilePasswordCache = injector.getUnifiedProfilePasswordCache(this.mKeyStore);
        this.mBiometricDeferredQueue = new com.android.server.locksettings.BiometricDeferredQueue(this.mSpManager);
        this.mRebootEscrowManager = injector.getRebootEscrowManager(new com.android.server.locksettings.LockSettingsService.RebootEscrowCallbacks(), this.mStorage);
        com.android.server.LocalServices.addService(com.android.internal.widget.LockSettingsInternal.class, new com.android.server.locksettings.LockSettingsService.LocalService());
        this.mLockSettingsServiceExt.setBinderExtension(this);
        this.mLockSettingsServiceWrapper.getExtImpl().init(this.mSpManager, this.mInjector.getContext(), this.mStorage);
        this.mRedLoggerExt.init(this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActivatedEncryptionNotifications(java.lang.String reason) {
        for (android.content.pm.UserInfo userInfo : this.mUserManager.getUsers()) {
            int i = 0;
            android.content.Context userContext = this.mContext.createContextAsUser(android.os.UserHandle.of(userInfo.id), 0);
            android.app.NotificationManager nm = (android.app.NotificationManager) userContext.getSystemService("notification");
            android.service.notification.StatusBarNotification[] activeNotifications = nm.getActiveNotifications();
            int length = activeNotifications.length;
            while (true) {
                if (i < length) {
                    android.service.notification.StatusBarNotification notification = activeNotifications[i];
                    if (notification.getId() != 9) {
                        i++;
                    } else {
                        maybeShowEncryptionNotificationForUser(userInfo.id, reason);
                        break;
                    }
                }
            }
        }
    }

    private void maybeShowEncryptionNotificationForUser(int userId, java.lang.String reason) {
        android.content.pm.UserInfo parent;
        android.content.pm.UserInfo user = this.mUserManager.getUserInfo(userId);
        if (!user.isManagedProfile() || isCeStorageUnlocked(userId)) {
            return;
        }
        android.os.UserHandle userHandle = user.getUserHandle();
        boolean isSecure = isUserSecure(userId);
        if (isSecure && !this.mUserManager.isUserUnlockingOrUnlocked(userHandle) && (parent = this.mUserManager.getProfileParent(userId)) != null && this.mUserManager.isUserUnlockingOrUnlocked(parent.getUserHandle()) && !this.mUserManager.isQuietModeEnabled(userHandle)) {
            showEncryptionNotificationForProfile(userHandle, parent.getUserHandle(), reason);
        }
    }

    private void showEncryptionNotificationForProfile(android.os.UserHandle user, android.os.UserHandle parent, java.lang.String reason) {
        android.app.PendingIntent intent;
        java.lang.CharSequence title = getEncryptionNotificationTitle();
        java.lang.CharSequence message = getEncryptionNotificationMessage();
        java.lang.CharSequence detail = getEncryptionNotificationDetail();
        android.app.KeyguardManager km = (android.app.KeyguardManager) this.mContext.getSystemService("keyguard");
        android.content.Intent unlockIntent = km.createConfirmDeviceCredentialIntent(null, null, user.getIdentifier());
        if (unlockIntent != null && android.os.storage.StorageManager.isFileEncrypted()) {
            unlockIntent.setFlags(276824064);
            if (android.app.admin.flags.Flags.hsumUnlockNotificationFix()) {
                intent = android.app.PendingIntent.getActivityAsUser(this.mContext, 0, unlockIntent, android.hardware.audio.common.V2_0.AudioFormat.E_AC3, null, parent);
            } else {
                intent = android.app.PendingIntent.getActivity(this.mContext, 0, unlockIntent, android.hardware.audio.common.V2_0.AudioFormat.E_AC3);
            }
            com.android.server.utils.Slogf.d(TAG, "Showing encryption notification for user %d; reason: %s", java.lang.Integer.valueOf(user.getIdentifier()), reason);
            showEncryptionNotification(user, title, message, detail, intent);
        }
    }

    private java.lang.String getEncryptionNotificationTitle() {
        return this.mInjector.getDevicePolicyManager().getResources().getString("Core.PROFILE_ENCRYPTED_TITLE", new java.util.function.Supplier() { // from class: com.android.server.locksettings.LockSettingsService$$ExternalSyntheticLambda8
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$getEncryptionNotificationTitle$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String lambda$getEncryptionNotificationTitle$0() {
        return this.mContext.getString(android.R.string.policylab_encryptedStorage);
    }

    private java.lang.String getEncryptionNotificationDetail() {
        return this.mInjector.getDevicePolicyManager().getResources().getString("Core.PROFILE_ENCRYPTED_DETAIL", new java.util.function.Supplier() { // from class: com.android.server.locksettings.LockSettingsService$$ExternalSyntheticLambda4
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$getEncryptionNotificationDetail$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String lambda$getEncryptionNotificationDetail$1() {
        return this.mContext.getString(android.R.string.policylab_disableCamera);
    }

    private java.lang.String getEncryptionNotificationMessage() {
        return this.mInjector.getDevicePolicyManager().getResources().getString("Core.PROFILE_ENCRYPTED_MESSAGE", new java.util.function.Supplier() { // from class: com.android.server.locksettings.LockSettingsService$$ExternalSyntheticLambda9
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$getEncryptionNotificationMessage$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String lambda$getEncryptionNotificationMessage$2() {
        return this.mContext.getString(android.R.string.policylab_disableKeyguardFeatures);
    }

    private void showEncryptionNotification(android.os.UserHandle user, java.lang.CharSequence title, java.lang.CharSequence message, java.lang.CharSequence detail, android.app.PendingIntent intent) {
        android.app.Notification notification = new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.DEVICE_ADMIN).setSmallIcon(android.R.drawable.ic_qs_battery_saver).setWhen(0L).setOngoing(true).setTicker(title).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color)).setContentTitle(title).setContentText(message).setSubText(detail).setVisibility(1).setContentIntent(intent).build();
        this.mNotificationManager.notifyAsUser(null, 9, notification, user);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideEncryptionNotification(android.os.UserHandle userHandle) {
        com.android.server.utils.Slogf.d(TAG, "Hiding encryption notification for user %d", java.lang.Integer.valueOf(userHandle.getIdentifier()));
        this.mNotificationManager.cancelAsUser(null, 9, userHandle);
    }

    void onUserStopped(int userId) {
        android.content.pm.UserProperties userProperties;
        android.util.Slog.d(TAG, "[onCleanupUser] user = " + userId);
        hideEncryptionNotification(new android.os.UserHandle(userId));
        if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enableBiometricsToUnlockPrivateSpace() && android.multiuser.Flags.enablePrivateSpaceFeatures() && (userProperties = this.mUserManager.getUserProperties(android.os.UserHandle.of(userId))) != null && userProperties.getAllowStoppingUserWithDelayedLocking()) {
            return;
        }
        int strongAuthRequired = com.android.internal.widget.LockPatternUtils.StrongAuthTracker.getDefaultFlags(this.mContext);
        requireStrongAuth(strongAuthRequired, userId);
        synchronized (this) {
            this.mUserPasswordMetrics.remove(userId);
        }
        if (isUserSecure(userId) && userId == 0) {
            android.util.Slog.d(TAG, "[onCleanupUser] notifyPasswordChanged due to remove user " + userId);
            com.android.internal.widget.LockscreenCredential noneCredential = com.android.internal.widget.LockscreenCredential.createNone();
            this.mLockSettingsServiceWrapper.getExtImpl().notifyPasswordChanged(noneCredential, userId);
            noneCredential.zeroize();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserStarting(int userId) {
        android.util.Slog.d(TAG, "[onStartUser] userId = " + userId);
        maybeShowEncryptionNotificationForUser(userId, "user started");
    }

    private void removeStateForReusedUserIdIfNecessary(int userId, int serialNumber) {
        int storedSerialNumber;
        if (userId != 0 && (storedSerialNumber = this.mStorage.getInt(USER_SERIAL_NUMBER_KEY, -1, userId)) != serialNumber) {
            if (storedSerialNumber != -1) {
                com.android.server.utils.Slogf.i(TAG, "Removing stale state for reused userId %d (serial %d => %d)", java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(storedSerialNumber), java.lang.Integer.valueOf(serialNumber));
                removeUserState(userId);
            }
            this.mStorage.setInt(USER_SERIAL_NUMBER_KEY, serialNumber, userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserUnlocking(final int userId) {
        android.util.Slog.d(TAG, "[onUnlockUser] userId = " + userId);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.locksettings.LockSettingsService.1
            @Override // java.lang.Runnable
            public void run() throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.InvalidKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
                com.android.server.locksettings.LockSettingsService.this.hideEncryptionNotification(new android.os.UserHandle(userId));
                if (com.android.server.locksettings.LockSettingsService.this.isCredentialSharableWithParent(userId)) {
                    com.android.server.locksettings.LockSettingsService.this.tieProfileLockIfNecessary(userId, com.android.internal.widget.LockscreenCredential.createNone());
                }
            }
        });
    }

    public void systemReady() {
        android.util.Slog.d(TAG, "[systemReady] ENTRY");
        checkWritePermission();
        this.mHasSecureLockScreen = this.mContext.getPackageManager().hasSystemFeature("android.software.secure_lock_screen");
        migrateOldData();
        getAuthSecretHal();
        this.mDeviceProvisionedObserver.onSystemReady();
        com.android.internal.widget.LockPatternUtils.invalidateCredentialTypeCache();
        this.mStorage.prefetchUser(0);
        this.mBiometricDeferredQueue.systemReady(this.mInjector.getFingerprintManager(), this.mInjector.getFaceManager(), this.mInjector.getBiometricManager());
        if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enablePrivateSpaceFeatures() && android.multiuser.Flags.enableBiometricsToUnlockPrivateSpace()) {
            this.mStorageManagerInternal.registerStorageLockEventListener(this.mCeStorageLockEventListener);
        }
        this.mLockSettingsServiceWrapper.getExtImpl().hookOnSystemReady();
        android.util.Slog.d(TAG, "[systemReady] LEAVE");
    }

    /* JADX INFO: renamed from: com.android.server.locksettings.LockSettingsService$3, reason: invalid class name */
    class AnonymousClass3 implements android.os.storage.ICeStorageLockEventListener {
        AnonymousClass3() {
        }

        public void onStorageLocked(final int userId) {
            android.util.Slog.i(com.android.server.locksettings.LockSettingsService.TAG, "Storage lock event received for " + userId);
            if (com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() && android.multiuser.Flags.enablePrivateSpaceFeatures() && android.multiuser.Flags.enableBiometricsToUnlockPrivateSpace()) {
                com.android.server.locksettings.LockSettingsService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.locksettings.LockSettingsService$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onStorageLocked$0(userId);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStorageLocked$0(int userId) {
            try {
                android.content.pm.UserProperties userProperties = com.android.server.locksettings.LockSettingsService.this.mUserManager.getUserProperties(android.os.UserHandle.of(userId));
                if (userProperties != null && userProperties.getAllowStoppingUserWithDelayedLocking()) {
                    int strongAuthRequired = com.android.internal.widget.LockPatternUtils.StrongAuthTracker.getDefaultFlags(com.android.server.locksettings.LockSettingsService.this.mContext);
                    com.android.server.locksettings.LockSettingsService.this.requireStrongAuth(strongAuthRequired, userId);
                }
            } catch (java.lang.IllegalArgumentException e) {
                com.android.server.utils.Slogf.d(com.android.server.locksettings.LockSettingsService.TAG, "User %d does not exist or has been removed", java.lang.Integer.valueOf(userId));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadEscrowData() {
        this.mRebootEscrowManager.loadRebootEscrowDataIfAvailable(this.mHandler);
    }

    private void getAuthSecretHal() {
        this.mAuthSecretService = android.hardware.authsecret.IAuthSecret.Stub.asInterface(android.os.ServiceManager.waitForDeclaredService(android.hardware.authsecret.IAuthSecret.DESCRIPTOR + "/default"));
        if (this.mAuthSecretService != null) {
            android.util.Slog.i(TAG, "Device implements AIDL AuthSecret HAL");
            return;
        }
        try {
            android.hardware.authsecret.V1_0.IAuthSecret authSecretServiceHidl = android.hardware.authsecret.V1_0.IAuthSecret.getService(true);
            this.mAuthSecretService = new com.android.server.locksettings.AuthSecretHidlAdapter(authSecretServiceHidl);
            android.util.Slog.i(TAG, "Device implements HIDL AuthSecret HAL");
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to get AuthSecret HAL(hidl)", e);
        } catch (java.util.NoSuchElementException e2) {
            android.util.Slog.i(TAG, "Device doesn't implement AuthSecret HAL");
        }
    }

    private void migrateOldData() {
        boolean success;
        if (getString(MIGRATED_KEYSTORE_NS, null, 0) == null) {
            synchronized (this.mSpManager) {
                success = true & this.mSpManager.migrateKeyNamespace();
            }
            if (success & migrateProfileLockKeys()) {
                setString(MIGRATED_KEYSTORE_NS, "true", 0);
                android.util.Slog.i(TAG, "Migrated keys to LSS namespace");
                return;
            } else {
                android.util.Slog.w(TAG, "Failed to migrate keys to LSS namespace");
                return;
            }
        }
        this.mLockSettingsServiceWrapper.getExtImpl().ensureMigrateMultiAppUserLockKeys();
    }

    void migrateOldDataAfterSystemReady() {
        if (com.android.internal.widget.LockPatternUtils.frpCredentialEnabled(this.mContext) && !getBoolean(MIGRATED_FRP2, false, 0)) {
            migrateFrpCredential();
            setBoolean(MIGRATED_FRP2, true, 0);
        }
    }

    private void migrateFrpCredential() {
        com.android.server.locksettings.LockSettingsStorage.PersistentData data = this.mStorage.readPersistentDataBlock();
        if (data != com.android.server.locksettings.LockSettingsStorage.PersistentData.NONE && !data.isBadFormatFromAndroid14Beta()) {
            return;
        }
        for (android.content.pm.UserInfo userInfo : this.mUserManager.getUsers()) {
            if (com.android.internal.widget.LockPatternUtils.userOwnsFrpCredential(this.mContext, userInfo) && isUserSecure(userInfo.id)) {
                synchronized (this.mSpManager) {
                    int actualQuality = (int) getLong("lockscreen.password_type", 0L, userInfo.id);
                    this.mSpManager.migrateFrpPasswordLocked(getCurrentLskfBasedProtectorId(userInfo.id), userInfo, redactActualQualityToMostLenientEquivalentQuality(actualQuality));
                }
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean migrateProfileLockKeys() {
        boolean success = true;
        java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getUsers();
        int userCount = users.size();
        for (int i = 0; i < userCount; i++) {
            android.content.pm.UserInfo user = users.get(i);
            if ((isCredentialSharableWithParent(user.id) || this.mLockSettingsServiceWrapper.getExtImpl().isOplusMultiAppUserId(user.id)) && !getSeparateProfileChallengeEnabledInternal(user.id)) {
                success = success & com.android.server.locksettings.SyntheticPasswordCrypto.migrateLockSettingsKey(PROFILE_KEY_NAME_ENCRYPT + user.id) & com.android.server.locksettings.SyntheticPasswordCrypto.migrateLockSettingsKey(PROFILE_KEY_NAME_DECRYPT + user.id);
            }
        }
        return success;
    }

    void deleteRepairModePersistentDataIfNeeded() {
        if (!com.android.internal.widget.LockPatternUtils.isRepairModeSupported(this.mContext) || com.android.internal.widget.LockPatternUtils.isRepairModeActive(this.mContext) || this.mInjector.isGsiRunning()) {
            return;
        }
        this.mStorage.deleteRepairModePersistentData();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onThirdPartyAppsStarted() {
        synchronized (this.mUserCreationAndRemovalLock) {
            for (int i = 0; i < this.mEarlyRemovedUsers.size(); i++) {
                int userId = this.mEarlyRemovedUsers.keyAt(i);
                com.android.server.utils.Slogf.i(TAG, "Removing locksettings state for removed user %d now that boot is complete", java.lang.Integer.valueOf(userId));
                removeUserState(userId);
            }
            this.mEarlyRemovedUsers = null;
            for (int i2 = 0; i2 < this.mEarlyCreatedUsers.size(); i2++) {
                int userId2 = this.mEarlyCreatedUsers.keyAt(i2);
                int serialNumber = this.mEarlyCreatedUsers.valueAt(i2);
                removeStateForReusedUserIdIfNecessary(userId2, serialNumber);
                com.android.server.utils.Slogf.i(TAG, "Creating locksettings state for user %d now that boot is complete", java.lang.Integer.valueOf(userId2));
                initializeSyntheticPassword(userId2);
            }
            this.mEarlyCreatedUsers = null;
            if (!FIX_UNLOCKED_DEVICE_REQUIRED_KEYS) {
                if (getString(MIGRATED_SP_CE_ONLY, null, 0) == null) {
                    for (android.content.pm.UserInfo user : this.mUserManager.getAliveUsers()) {
                        removeStateForReusedUserIdIfNecessary(user.id, user.serialNumber);
                        synchronized (this.mSpManager) {
                            migrateUserToSpWithBoundCeKeyLocked(user.id);
                        }
                    }
                    setString(MIGRATED_SP_CE_ONLY, "true", 0);
                }
                if (getBoolean(MIGRATED_SP_FULL, false, 0)) {
                    setBoolean(MIGRATED_SP_FULL, false, 0);
                }
                this.mThirdPartyAppsStarted = true;
            } else {
                if (!getBoolean(MIGRATED_SP_FULL, false, 0)) {
                    for (android.content.pm.UserInfo user2 : this.mUserManager.getAliveUsers()) {
                        removeStateForReusedUserIdIfNecessary(user2.id, user2.serialNumber);
                        synchronized (this.mSpManager) {
                            migrateUserToSpWithBoundKeysLocked(user2.id);
                        }
                    }
                    setBoolean(MIGRATED_SP_FULL, true, 0);
                }
                this.mThirdPartyAppsStarted = true;
            }
        }
    }

    private void migrateUserToSpWithBoundCeKeyLocked(int userId) {
        if (isUserSecure(userId)) {
            com.android.server.utils.Slogf.d(TAG, "User %d is secured; no migration needed", java.lang.Integer.valueOf(userId));
            return;
        }
        long protectorId = getCurrentLskfBasedProtectorId(userId);
        if (protectorId == 0) {
            com.android.server.utils.Slogf.i(TAG, "Migrating unsecured user %d to SP-based credential", java.lang.Integer.valueOf(userId));
            initializeSyntheticPassword(userId);
            return;
        }
        com.android.server.utils.Slogf.i(TAG, "Existing unsecured user %d has a synthetic password; re-encrypting CE key with it", java.lang.Integer.valueOf(userId));
        com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult result = this.mSpManager.unlockLskfBasedProtector(getGateKeeperService(), protectorId, com.android.internal.widget.LockscreenCredential.createNone(), userId, null);
        if (result.syntheticPassword == null) {
            com.android.server.utils.Slogf.wtf(TAG, "Failed to unwrap synthetic password for unsecured user %d", java.lang.Integer.valueOf(userId));
        } else {
            setCeStorageProtection(userId, result.syntheticPassword);
        }
    }

    private void migrateUserToSpWithBoundKeysLocked(int userId) {
        if (isUserSecure(userId)) {
            com.android.server.utils.Slogf.d(TAG, "User %d is secured; no migration needed", java.lang.Integer.valueOf(userId));
            return;
        }
        long protectorId = getCurrentLskfBasedProtectorId(userId);
        if (protectorId == 0) {
            com.android.server.utils.Slogf.i(TAG, "Migrating unsecured user %d to SP-based credential", java.lang.Integer.valueOf(userId));
            initializeSyntheticPassword(userId);
            return;
        }
        com.android.server.utils.Slogf.i(TAG, "Existing unsecured user %d has a synthetic password", java.lang.Integer.valueOf(userId));
        com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult result = this.mSpManager.unlockLskfBasedProtector(getGateKeeperService(), protectorId, com.android.internal.widget.LockscreenCredential.createNone(), userId, null);
        com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp = result.syntheticPassword;
        if (sp == null) {
            com.android.server.utils.Slogf.wtf(TAG, "Failed to unwrap synthetic password for unsecured user %d", java.lang.Integer.valueOf(userId));
            return;
        }
        if (getString(MIGRATED_SP_CE_ONLY, null, 0) == null) {
            com.android.server.utils.Slogf.i(TAG, "Encrypting CE key of user %d with synthetic password", java.lang.Integer.valueOf(userId));
            setCeStorageProtection(userId, sp);
        }
        com.android.server.utils.Slogf.i(TAG, "Initializing Keystore super keys for user %d", java.lang.Integer.valueOf(userId));
        initKeystoreSuperKeys(userId, sp, true);
    }

    private int redactActualQualityToMostLenientEquivalentQuality(int quality) {
        switch (quality) {
            case 131072:
            case 196608:
                return 131072;
            case 262144:
            case 327680:
            case 393216:
                return 262144;
            default:
                return quality;
        }
    }

    private void enforceFrpNotActive() {
        int mainUserId = this.mInjector.getUserManagerInternal().getMainUserId();
        if (mainUserId < 0) {
            android.util.Slog.d(TAG, "No Main user on device; skipping enforceFrpNotActive");
            return;
        }
        android.content.ContentResolver cr = this.mContext.getContentResolver();
        boolean isFrpActive = false;
        boolean inSetupWizard = android.provider.Settings.Secure.getIntForUser(cr, "user_setup_complete", 0, mainUserId) == 0;
        if (android.security.Flags.frpEnforcement()) {
            isFrpActive = this.mStorage.isFactoryResetProtectionActive();
        } else if (android.provider.Settings.Global.getInt(cr, "secure_frp_mode", 0) == 1 && inSetupWizard) {
            isFrpActive = true;
        }
        if (isFrpActive) {
            throw new java.lang.SecurityException("Cannot change credential while factory reset protection is active");
        }
    }

    private final void checkWritePermission() {
        this.mContext.enforceCallingOrSelfPermission(PERMISSION, "LockSettingsWrite");
    }

    private final void checkPasswordReadPermission() {
        this.mContext.enforceCallingOrSelfPermission(PERMISSION, "LockSettingsRead");
    }

    private final void checkPasswordHavePermission() {
        this.mContext.enforceCallingOrSelfPermission(PERMISSION, "LockSettingsHave");
    }

    private final void checkDatabaseReadPermission(java.lang.String requestedKey, int userId) {
        if (!hasPermission(PERMISSION)) {
            throw new java.lang.SecurityException("uid=" + getCallingUid() + " needs permission " + PERMISSION + " to read " + requestedKey + " for user " + userId);
        }
    }

    private final void checkBiometricPermission() {
        this.mContext.enforceCallingOrSelfPermission(BIOMETRIC_PERMISSION, "LockSettingsBiometric");
    }

    private boolean hasPermission(java.lang.String permission) {
        return this.mContext.checkCallingOrSelfPermission(permission) == 0;
    }

    private void checkManageWeakEscrowTokenMethodUsage() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_WEAK_ESCROW_TOKEN", "Requires MANAGE_WEAK_ESCROW_TOKEN permission.");
        if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            throw new java.lang.IllegalArgumentException("Weak escrow token are only for automotive devices.");
        }
    }

    public boolean hasSecureLockScreen() {
        android.util.Slog.d(TAG, "[hasSecureLockScreen] mHasSecureLockScreen = " + this.mHasSecureLockScreen);
        return this.mHasSecureLockScreen;
    }

    public boolean getSeparateProfileChallengeEnabled(int userId) {
        android.util.Slog.d(TAG, "[getSeparateProfileChallengeEnabled] userId = " + userId);
        checkDatabaseReadPermission(SEPARATE_PROFILE_CHALLENGE_KEY, userId);
        return getSeparateProfileChallengeEnabledInternal(userId);
    }

    private boolean getSeparateProfileChallengeEnabledInternal(int userId) {
        boolean z;
        synchronized (this.mSeparateChallengeLock) {
            z = this.mStorage.getBoolean(SEPARATE_PROFILE_CHALLENGE_KEY, false, userId);
        }
        return z;
    }

    public void setSeparateProfileChallengeEnabled(int userId, boolean enabled, com.android.internal.widget.LockscreenCredential profileUserPassword) {
        checkWritePermission();
        android.util.Slog.d(TAG, "[setSeparateProfileChallengeEnabled] userId = " + userId + ", enabled = " + enabled);
        if (!this.mHasSecureLockScreen && profileUserPassword != null && profileUserPassword.getType() != -1) {
            throw new java.lang.UnsupportedOperationException("This operation requires secure lock screen feature.");
        }
        synchronized (this.mSeparateChallengeLock) {
            setSeparateProfileChallengeEnabledLocked(userId, enabled, profileUserPassword != null ? profileUserPassword : com.android.internal.widget.LockscreenCredential.createNone());
        }
        notifySeparateProfileChallengeChanged(userId);
    }

    private void setSeparateProfileChallengeEnabledLocked(int userId, boolean enabled, com.android.internal.widget.LockscreenCredential profileUserPassword) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.InvalidKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
        boolean old = getBoolean(SEPARATE_PROFILE_CHALLENGE_KEY, false, userId);
        setBoolean(SEPARATE_PROFILE_CHALLENGE_KEY, enabled, userId);
        try {
            if (enabled) {
                this.mStorage.removeChildProfileLock(userId);
                removeKeystoreProfileKey(userId);
            } else {
                tieProfileLockIfNecessary(userId, profileUserPassword);
            }
        } catch (java.lang.IllegalStateException e) {
            setBoolean(SEPARATE_PROFILE_CHALLENGE_KEY, old, userId);
            throw e;
        }
    }

    private void notifySeparateProfileChallengeChanged(final int userId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.locksettings.LockSettingsService$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.locksettings.LockSettingsService.lambda$notifySeparateProfileChallengeChanged$3(userId);
            }
        });
    }

    static /* synthetic */ void lambda$notifySeparateProfileChallengeChanged$3(int userId) {
        android.app.admin.DevicePolicyManagerInternal dpmi = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        if (dpmi != null) {
            dpmi.reportSeparateProfileChallengeChanged(userId);
        }
    }

    public void setBoolean(java.lang.String key, boolean value, int userId) {
        checkWritePermission();
        android.util.Slog.d(TAG, "[setBoolean] key = " + key + ", value = " + value + ", userId = " + userId);
        java.util.Objects.requireNonNull(key);
        this.mStorage.setBoolean(key, value, userId);
    }

    public void setLong(java.lang.String key, long value, int userId) {
        checkWritePermission();
        android.util.Slog.d(TAG, "[setLong] key = " + key + ", value = " + value + ", userId = " + userId);
        java.util.Objects.requireNonNull(key);
        this.mStorage.setLong(key, value, userId);
        this.mLockSettingsServiceWrapper.getExtImpl().setLong(key, value, userId);
    }

    public void setString(java.lang.String key, java.lang.String value, int userId) {
        checkWritePermission();
        android.util.Slog.d(TAG, "[setString] key = " + key + ", value = " + value + ", userId = " + userId);
        java.util.Objects.requireNonNull(key);
        this.mStorage.setString(key, value, userId);
    }

    public boolean getBoolean(java.lang.String key, boolean defaultValue, int userId) {
        checkDatabaseReadPermission(key, userId);
        return this.mStorage.getBoolean(key, defaultValue, userId);
    }

    public long getLong(java.lang.String key, long defaultValue, int userId) {
        checkDatabaseReadPermission(key, userId);
        return this.mStorage.getLong(key, defaultValue, userId);
    }

    public java.lang.String getString(java.lang.String key, java.lang.String defaultValue, int userId) {
        checkDatabaseReadPermission(key, userId);
        return this.mStorage.getString(key, defaultValue, userId);
    }

    private int getKeyguardStoredQuality(int userId) {
        return (int) this.mStorage.getLong("lockscreen.password_type", 0L, userId);
    }

    public int getPinLength(int userId) {
        checkPasswordHavePermission();
        android.app.admin.PasswordMetrics passwordMetrics = getUserPasswordMetrics(userId);
        if (passwordMetrics != null && passwordMetrics.credType == 3) {
            return passwordMetrics.length;
        }
        synchronized (this.mSpManager) {
            long protectorId = getCurrentLskfBasedProtectorId(userId);
            if (protectorId == 0) {
                return -1;
            }
            return this.mSpManager.getPinLength(protectorId, userId);
        }
    }

    public boolean refreshStoredPinLength(int userId) {
        checkPasswordHavePermission();
        synchronized (this.mSpManager) {
            android.app.admin.PasswordMetrics passwordMetrics = getUserPasswordMetrics(userId);
            if (passwordMetrics != null) {
                long protectorId = getCurrentLskfBasedProtectorId(userId);
                return this.mSpManager.refreshPinLengthOnDisk(passwordMetrics, protectorId, userId);
            }
            android.util.Log.w(TAG, "PasswordMetrics is not available");
            return false;
        }
    }

    public int getCredentialType(int userId) {
        checkPasswordHavePermission();
        return getCredentialTypeInternal(userId);
    }

    private int getCredentialTypeInternal(int userId) {
        if (com.android.internal.widget.LockPatternUtils.isSpecialUserId(userId)) {
            return this.mSpManager.getSpecialUserCredentialType(userId);
        }
        synchronized (this.mSpManager) {
            long protectorId = getCurrentLskfBasedProtectorId(userId);
            if (protectorId == 0) {
                return -1;
            }
            int rawType = this.mSpManager.getCredentialType(protectorId, userId);
            if (rawType != 2) {
                return rawType;
            }
            return com.android.internal.widget.LockPatternUtils.pinOrPasswordQualityToCredentialType(getKeyguardStoredQuality(userId));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUserSecure(int userId) {
        boolean ret = getCredentialTypeInternal(userId) != -1;
        android.util.Slog.d(TAG, "[isUserSecure] userId = " + userId + ", ret = " + ret);
        return ret;
    }

    public void retainPassword(java.lang.String password) {
        if (com.android.internal.widget.LockPatternUtils.isDeviceEncryptionEnabled()) {
            if (password != null) {
                mSavePassword = password;
            } else {
                mSavePassword = DEFAULT_PASSWORD;
            }
        }
    }

    public void sanitizePassword() {
        if (com.android.internal.widget.LockPatternUtils.isDeviceEncryptionEnabled()) {
            android.util.Slog.d(TAG, "[sanitizePassword] DeviceEncryptionEnabled");
            mSavePassword = DEFAULT_PASSWORD;
        }
    }

    private boolean checkCryptKeeperPermissions() {
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.CRYPT_KEEPER", "no permission to get the password");
            return false;
        } catch (java.lang.SecurityException e) {
            return true;
        }
    }

    public java.lang.String getPassword() {
        if (checkCryptKeeperPermissions()) {
            this.mContext.enforceCallingOrSelfPermission(PERMISSION, "no crypt_keeper or admin permission to get the password");
        }
        return mSavePassword;
    }

    void setKeystorePassword(byte[] password, int userHandle) {
        android.security.AndroidKeyStoreMaintenance.onUserPasswordChanged(userHandle, password);
    }

    void initKeystoreSuperKeys(int userId, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, boolean allowExisting) {
        byte[] password = sp.deriveKeyStorePassword();
        try {
            int res = android.security.AndroidKeyStoreMaintenance.initUserSuperKeys(userId, password, allowExisting);
            if (res != 0) {
                throw new java.lang.IllegalStateException("Failed to initialize Keystore super keys for user " + userId);
            }
        } finally {
            java.util.Arrays.fill(password, (byte) 0);
        }
    }

    private void unlockKeystore(int userId, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp) {
        this.mKeyStoreAuthorization.onDeviceUnlocked(userId, sp.deriveKeyStorePassword());
    }

    protected com.android.internal.widget.LockscreenCredential getDecryptedPasswordForTiedProfile(int userId) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.InvalidKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
        com.android.server.utils.Slogf.d(TAG, "Decrypting password for tied profile %d", java.lang.Integer.valueOf(userId));
        byte[] storedData = this.mStorage.readChildProfileLock(userId);
        if (storedData == null) {
            throw new java.io.FileNotFoundException("Child profile lock file not found");
        }
        byte[] iv = java.util.Arrays.copyOfRange(storedData, 0, 12);
        byte[] encryptedPassword = java.util.Arrays.copyOfRange(storedData, 12, storedData.length);
        javax.crypto.SecretKey decryptionKey = (javax.crypto.SecretKey) this.mKeyStore.getKey(PROFILE_KEY_NAME_DECRYPT + userId, null);
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(2, decryptionKey, new javax.crypto.spec.GCMParameterSpec(128, iv));
        byte[] decryptionResult = cipher.doFinal(encryptedPassword);
        com.android.internal.widget.LockscreenCredential credential = com.android.internal.widget.LockscreenCredential.createUnifiedProfilePassword(decryptionResult);
        java.util.Arrays.fill(decryptionResult, (byte) 0);
        try {
            long parentSid = getGateKeeperService().getSecureUserId(this.mUserManager.getProfileParent(userId).id);
            this.mUnifiedProfilePasswordCache.storePassword(userId, credential, parentSid);
        } catch (android.os.RemoteException e) {
            com.android.server.utils.Slogf.w(TAG, "Failed to talk to GateKeeper service", e);
        }
        return credential;
    }

    private void unlockChildProfile(int profileHandle) {
        try {
            doVerifyCredential(getDecryptedPasswordForTiedProfile(profileHandle), profileHandle, null, 0);
        } catch (java.io.IOException | java.security.InvalidAlgorithmParameterException | java.security.InvalidKeyException | java.security.KeyStoreException | java.security.NoSuchAlgorithmException | java.security.UnrecoverableKeyException | java.security.cert.CertificateException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.NoSuchPaddingException e) {
            if (e instanceof java.io.FileNotFoundException) {
                android.util.Slog.i(TAG, "Child profile key not found");
            } else {
                android.util.Slog.e(TAG, "Failed to decrypt child profile key", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: unlockUser, reason: merged with bridge method [inline-methods] */
    public void lambda$setLockCredentialWithToken$9(int userId) {
        boolean alreadyUnlocked = this.mUserManager.isUserUnlockingOrUnlocked(userId);
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        try {
            this.mActivityManager.unlockUser2(userId, new android.os.IProgressListener.Stub() { // from class: com.android.server.locksettings.LockSettingsService.4
                public void onStarted(int id, android.os.Bundle extras) throws android.os.RemoteException {
                    android.util.Slog.d(com.android.server.locksettings.LockSettingsService.TAG, "unlockUser started");
                }

                public void onProgress(int id, int progress, android.os.Bundle extras) throws android.os.RemoteException {
                    android.util.Slog.d(com.android.server.locksettings.LockSettingsService.TAG, "unlockUser progress " + progress);
                }

                public void onFinished(int id, android.os.Bundle extras) throws android.os.RemoteException {
                    android.util.Slog.d(com.android.server.locksettings.LockSettingsService.TAG, "unlockUser finished");
                    latch.countDown();
                }
            });
            try {
                latch.await(15L, java.util.concurrent.TimeUnit.SECONDS);
            } catch (java.lang.InterruptedException e) {
                java.lang.Thread.currentThread().interrupt();
            }
            if (isCredentialSharableWithParent(userId)) {
                if (!hasUnifiedChallenge(userId)) {
                    this.mBiometricDeferredQueue.processPendingLockoutResets();
                    return;
                }
                return;
            }
            for (android.content.pm.UserInfo profile : this.mUserManager.getProfiles(userId)) {
                if (profile.id != userId && (profile.isManagedProfile() || !this.mLockSettingsServiceWrapper.getExtImpl().hookShouldUnlockProfile(profile.id))) {
                    if (hasUnifiedChallenge(profile.id)) {
                        if (this.mUserManager.isUserRunning(profile.id)) {
                            unlockChildProfile(profile.id);
                            this.mLockSettingsServiceWrapper.getExtImpl().tryRemoveLockscreenCredentialForMultiApp(profile.id, isUserSecure(profile.id));
                        } else {
                            try {
                                getDecryptedPasswordForTiedProfile(profile.id);
                            } catch (java.io.IOException | java.security.GeneralSecurityException e2) {
                                android.util.Slog.d(TAG, "Cache unified profile password failed", e2);
                            }
                        }
                    }
                    if (alreadyUnlocked) {
                        continue;
                    } else {
                        long ident = clearCallingIdentity();
                        try {
                            maybeShowEncryptionNotificationForUser(profile.id, "parent unlocked");
                        } finally {
                            restoreCallingIdentity(ident);
                        }
                    }
                }
            }
            this.mBiometricDeferredQueue.processPendingLockoutResets();
        } catch (android.os.RemoteException e3) {
            throw e3.rethrowAsRuntimeException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasUnifiedChallenge(int userId) {
        return !getSeparateProfileChallengeEnabledInternal(userId) && this.mStorage.hasChildProfileLock(userId);
    }

    private java.util.Map<java.lang.Integer, com.android.internal.widget.LockscreenCredential> getDecryptedPasswordsForAllTiedProfiles(int userId) {
        if (isCredentialSharableWithParent(userId)) {
            return null;
        }
        java.util.Map<java.lang.Integer, com.android.internal.widget.LockscreenCredential> result = new android.util.ArrayMap<>();
        java.util.List<android.content.pm.UserInfo> profiles = this.mUserManager.getProfiles(userId);
        int size = profiles.size();
        for (int i = 0; i < size; i++) {
            android.content.pm.UserInfo profile = profiles.get(i);
            if (isCredentialSharableWithParent(profile.id)) {
                int profileUserId = profile.id;
                if (!getSeparateProfileChallengeEnabledInternal(profileUserId)) {
                    try {
                        result.put(java.lang.Integer.valueOf(profileUserId), getDecryptedPasswordForTiedProfile(profileUserId));
                    } catch (java.io.IOException | java.security.InvalidAlgorithmParameterException | java.security.InvalidKeyException | java.security.KeyStoreException | java.security.NoSuchAlgorithmException | java.security.UnrecoverableKeyException | java.security.cert.CertificateException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.NoSuchPaddingException e) {
                        android.util.Slog.e(TAG, "getDecryptedPasswordsForAllTiedProfiles failed for user " + profileUserId, e);
                    }
                }
            }
        }
        return result;
    }

    private void synchronizeUnifiedChallengeForProfiles(int userId, java.util.Map<java.lang.Integer, com.android.internal.widget.LockscreenCredential> profilePasswordMap) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.InvalidKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
        if (isCredentialSharableWithParent(userId)) {
            return;
        }
        boolean isSecure = isUserSecure(userId);
        java.util.List<android.content.pm.UserInfo> profiles = this.mUserManager.getProfiles(userId);
        int size = profiles.size();
        for (int i = 0; i < size; i++) {
            android.content.pm.UserInfo profile = profiles.get(i);
            int profileUserId = profile.id;
            if (isCredentialSharableWithParent(profileUserId) && !getSeparateProfileChallengeEnabledInternal(profileUserId)) {
                if (isSecure) {
                    tieProfileLockIfNecessary(profileUserId, com.android.internal.widget.LockscreenCredential.createNone());
                } else if (profilePasswordMap != null && profilePasswordMap.containsKey(java.lang.Integer.valueOf(profileUserId))) {
                    setLockCredentialInternal(com.android.internal.widget.LockscreenCredential.createNone(), profilePasswordMap.get(java.lang.Integer.valueOf(profileUserId)), profileUserId, true);
                    this.mStorage.removeChildProfileLock(profileUserId);
                    removeKeystoreProfileKey(profileUserId);
                } else {
                    android.util.Slog.wtf(TAG, "Attempt to clear tied challenge, but no password supplied.");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isProfileWithUnifiedLock(int userId) {
        return isCredentialSharableWithParent(userId) && !getSeparateProfileChallengeEnabledInternal(userId);
    }

    private void sendCredentialsOnUnlockIfRequired(com.android.internal.widget.LockscreenCredential credential, int userId) {
        if (com.android.internal.widget.LockPatternUtils.isSpecialUserId(userId) || credential.isNone() || isProfileWithUnifiedLock(userId)) {
            return;
        }
        java.util.Iterator<java.lang.Integer> it = getProfilesWithSameLockScreen(userId).iterator();
        while (it.hasNext()) {
            int profileId = it.next().intValue();
            this.mRecoverableKeyStoreManager.lockScreenSecretAvailable(credential.getType(), credential.getCredential(), profileId);
        }
    }

    private void sendCredentialsOnChangeIfRequired(com.android.internal.widget.LockscreenCredential credential, int userId, boolean isLockTiedToParent) {
        if (isLockTiedToParent) {
            return;
        }
        byte[] secret = credential.isNone() ? null : credential.getCredential();
        java.util.Iterator<java.lang.Integer> it = getProfilesWithSameLockScreen(userId).iterator();
        while (it.hasNext()) {
            int profileId = it.next().intValue();
            this.mRecoverableKeyStoreManager.lockScreenSecretChanged(credential.getType(), secret, profileId);
        }
    }

    private java.util.Set<java.lang.Integer> getProfilesWithSameLockScreen(int userId) {
        java.util.Set<java.lang.Integer> profiles = new android.util.ArraySet<>();
        for (android.content.pm.UserInfo profile : this.mUserManager.getProfiles(userId)) {
            if (profile.id == userId || (profile.profileGroupId == userId && isProfileWithUnifiedLock(profile.id))) {
                profiles.add(java.lang.Integer.valueOf(profile.id));
            }
        }
        return profiles;
    }

    public boolean setLockCredential(com.android.internal.widget.LockscreenCredential credential, com.android.internal.widget.LockscreenCredential savedCredential, final int userId) {
        android.util.Slog.d(TAG, "[setLockCredential] userId = " + userId);
        if (!this.mHasSecureLockScreen && credential != null && credential.getType() != -1) {
            throw new java.lang.UnsupportedOperationException("This operation requires secure lock screen feature");
        }
        if (!hasPermission(PERMISSION) && !hasPermission("android.permission.SET_AND_VERIFY_LOCKSCREEN_CREDENTIALS") && (!hasPermission("android.permission.SET_INITIAL_LOCK") || !savedCredential.isNone())) {
            throw new java.lang.SecurityException("setLockCredential requires SET_AND_VERIFY_LOCKSCREEN_CREDENTIALS or android.permission.ACCESS_KEYGUARD_SECURE_STORAGE");
        }
        credential.validateBasicRequirements();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            enforceFrpNotActive();
            if (!savedCredential.isNone() && isProfileWithUnifiedLock(userId)) {
                verifyCredential(savedCredential, this.mUserManager.getProfileParent(userId).id, 0);
                savedCredential.zeroize();
                savedCredential = com.android.internal.widget.LockscreenCredential.createNone();
            }
            synchronized (this.mSeparateChallengeLock) {
                if (!setLockCredentialInternal(credential, savedCredential, userId, false)) {
                    scheduleGc();
                    return false;
                }
                setSeparateProfileChallengeEnabledLocked(userId, true, null);
                notifyPasswordChanged(credential, userId);
                if (isCredentialSharableWithParent(userId)) {
                    setDeviceUnlockedForUser(userId);
                }
                notifySeparateProfileChallengeChanged(userId);
                onPostPasswordChanged(credential, userId);
                scheduleGc();
                boolean hasCredential = savedCredential.isNone();
                if (this.mRedLoggerExt != null) {
                    if (hasCredential) {
                        com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.locksettings.LockSettingsService$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$setLockCredential$4(userId);
                            }
                        });
                    } else {
                        com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.locksettings.LockSettingsService$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$setLockCredential$5(userId);
                            }
                        });
                    }
                } else {
                    android.util.Slog.e(TAG, "mRedLoggerExt is null!");
                }
                return true;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLockCredential$4(int userId) {
        this.mRedLoggerExt.saveREDLog("PASSWORD", userId, "enroll", 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLockCredential$5(int userId) {
        this.mRedLoggerExt.saveREDLog("PASSWORD", userId, "remove", 1);
    }

    private boolean setLockCredentialInternal(com.android.internal.widget.LockscreenCredential credential, com.android.internal.widget.LockscreenCredential savedCredential, int userId, boolean isLockTiedToParent) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.InvalidKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
        java.util.Objects.requireNonNull(credential);
        java.util.Objects.requireNonNull(savedCredential);
        synchronized (this.mSpManager) {
            if (((com.android.server.locksettings.ISyntheticPasswordManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.locksettings.ISyntheticPasswordManagerExt.class).create()).isMemoryLow()) {
                android.util.Slog.d(TAG, "Freespace:" + android.os.Environment.getDataDirectory().getFreeSpace());
                throw new java.lang.UnsupportedOperationException("No space left on device");
            }
            if (savedCredential.isNone() && isProfileWithUnifiedLock(userId)) {
                try {
                    savedCredential = getDecryptedPasswordForTiedProfile(userId);
                } catch (java.io.FileNotFoundException e) {
                    android.util.Slog.i(TAG, "Child profile key not found");
                } catch (java.io.IOException | java.security.InvalidAlgorithmParameterException | java.security.InvalidKeyException | java.security.KeyStoreException | java.security.NoSuchAlgorithmException | java.security.UnrecoverableKeyException | java.security.cert.CertificateException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.NoSuchPaddingException e2) {
                    android.util.Slog.e(TAG, "Failed to decrypt child profile key", e2);
                }
            }
            long oldProtectorId = getCurrentLskfBasedProtectorId(userId);
            com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult authResult = this.mSpManager.unlockLskfBasedProtector(getGateKeeperService(), oldProtectorId, savedCredential, userId, null);
            com.android.internal.widget.VerifyCredentialResponse response = authResult.gkResponse;
            com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp = authResult.syntheticPassword;
            if (sp == null) {
                if (response != null && response.getResponseCode() != -1) {
                    if (response.getResponseCode() == 1) {
                        android.util.Slog.w(TAG, "Failed to enroll: rate limit exceeded.");
                        return false;
                    }
                    throw new java.lang.IllegalStateException("password change failed");
                }
                android.util.Slog.w(TAG, "Failed to enroll: incorrect credential.");
                return false;
            }
            onSyntheticPasswordUnlocked(userId, sp);
            setLockCredentialWithSpLocked(credential, sp, userId);
            sendCredentialsOnChangeIfRequired(credential, userId, isLockTiedToParent);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPostPasswordChanged(com.android.internal.widget.LockscreenCredential newCredential, int userId) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.InvalidKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
        updatePasswordHistory(newCredential, userId);
        ((android.app.trust.TrustManager) this.mContext.getSystemService(android.app.trust.TrustManager.class)).reportEnabledTrustAgentsChanged(userId);
        sendMainUserCredentialChangedNotificationIfNeeded(userId);
    }

    private void updatePasswordHistory(com.android.internal.widget.LockscreenCredential password, int userHandle) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.InvalidKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
        java.lang.String passwordHistory;
        if (password.isNone() || password.isPattern()) {
            return;
        }
        java.lang.String passwordHistory2 = getString("lockscreen.passwordhistory", null, userHandle);
        if (passwordHistory2 == null) {
            passwordHistory2 = "";
        }
        int passwordHistoryLength = getRequestedPasswordHistoryLength(userHandle);
        if (passwordHistoryLength == 0) {
            passwordHistory = "";
        } else {
            com.android.server.utils.Slogf.d(TAG, "Adding new password to password history for user %d", java.lang.Integer.valueOf(userHandle));
            byte[] hashFactor = getHashFactor(password, userHandle);
            byte[] salt = getSalt(userHandle).getBytes();
            java.lang.String hash = password.passwordToHistoryHash(salt, hashFactor);
            if (hash == null) {
                android.util.Slog.e(TAG, "Failed to compute password hash; password history won't be updated");
                return;
            }
            if (android.text.TextUtils.isEmpty(passwordHistory2)) {
                passwordHistory = hash;
            } else {
                java.lang.String[] history = passwordHistory2.split(",");
                java.util.StringJoiner joiner = new java.util.StringJoiner(",");
                joiner.add(hash);
                for (int i = 0; i < passwordHistoryLength - 1 && i < history.length; i++) {
                    joiner.add(history[i]);
                }
                passwordHistory = joiner.toString();
            }
        }
        setString("lockscreen.passwordhistory", passwordHistory, userHandle);
    }

    private java.lang.String getSalt(int userId) {
        long salt = getLong("lockscreen.password_salt", 0L, userId);
        if (salt == 0) {
            salt = com.android.server.locksettings.SecureRandomUtils.randomLong();
            setLong("lockscreen.password_salt", salt, userId);
        }
        return java.lang.Long.toHexString(salt);
    }

    private int getRequestedPasswordHistoryLength(int userId) {
        return this.mInjector.getDevicePolicyManager().getPasswordHistoryLength(null, userId);
    }

    private android.os.UserManager getUserManagerFromCache(int userId) {
        android.os.UserHandle userHandle = android.os.UserHandle.of(userId);
        if (this.mUserManagerCache.containsKey(userHandle)) {
            return this.mUserManagerCache.get(userHandle);
        }
        try {
            android.content.Context userContext = this.mContext.createPackageContextAsUser("system", 0, userHandle);
            android.os.UserManager userManager = (android.os.UserManager) userContext.getSystemService(android.os.UserManager.class);
            this.mUserManagerCache.put(userHandle, userManager);
            return userManager;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.RuntimeException("Failed to create context for user " + userHandle, e);
        }
    }

    protected boolean isCredentialSharableWithParent(int userId) {
        return getUserManagerFromCache(userId).isCredentialSharableWithParent();
    }

    public boolean registerWeakEscrowTokenRemovedListener(com.android.internal.widget.IWeakEscrowTokenRemovedListener listener) {
        android.util.Slog.d(TAG, "[registerWeakEscrowTokenRemovedListener]");
        checkManageWeakEscrowTokenMethodUsage();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return this.mSpManager.registerWeakEscrowTokenRemovedListener(listener);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean unregisterWeakEscrowTokenRemovedListener(com.android.internal.widget.IWeakEscrowTokenRemovedListener listener) {
        android.util.Slog.d(TAG, "[unregisterWeakEscrowTokenRemovedListener]");
        checkManageWeakEscrowTokenMethodUsage();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return this.mSpManager.unregisterWeakEscrowTokenRemovedListener(listener);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public long addWeakEscrowToken(byte[] token, int userId, final com.android.internal.widget.IWeakEscrowTokenActivatedListener listener) {
        android.util.Slog.d(TAG, "[addWeakEscrowToken] userId = " + userId);
        checkManageWeakEscrowTokenMethodUsage();
        java.util.Objects.requireNonNull(listener, "Listener can not be null.");
        com.android.internal.widget.LockPatternUtils.EscrowTokenStateChangeCallback internalListener = new com.android.internal.widget.LockPatternUtils.EscrowTokenStateChangeCallback() { // from class: com.android.server.locksettings.LockSettingsService$$ExternalSyntheticLambda3
            public final void onEscrowTokenActivated(long j, int i) {
                com.android.server.locksettings.LockSettingsService.lambda$addWeakEscrowToken$6(listener, j, i);
            }
        };
        long restoreToken = android.os.Binder.clearCallingIdentity();
        try {
            return addEscrowToken(token, 1, userId, internalListener);
        } finally {
            android.os.Binder.restoreCallingIdentity(restoreToken);
        }
    }

    static /* synthetic */ void lambda$addWeakEscrowToken$6(com.android.internal.widget.IWeakEscrowTokenActivatedListener listener, long handle, int userId1) {
        try {
            listener.onWeakEscrowTokenActivated(handle, userId1);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Exception while notifying weak escrow token has been activated", e);
        }
    }

    public boolean removeWeakEscrowToken(long handle, int userId) {
        android.util.Slog.d(TAG, "[removeWeakEscrowToken] userId = " + userId + ", handle = " + handle);
        checkManageWeakEscrowTokenMethodUsage();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return removeEscrowToken(handle, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean isWeakEscrowTokenActive(long handle, int userId) {
        android.util.Slog.d(TAG, "[isWeakEscrowTokenActive] userId = " + userId + ", handle = " + handle);
        checkManageWeakEscrowTokenMethodUsage();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return isEscrowTokenActive(handle, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean isWeakEscrowTokenValid(long handle, byte[] token, int userId) {
        android.util.Slog.d(TAG, "[isWeakEscrowTokenValid] userId = " + userId + ", handle = " + handle);
        checkManageWeakEscrowTokenMethodUsage();
        long restoreToken = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mSpManager) {
                if (!this.mSpManager.hasEscrowData(userId)) {
                    android.util.Slog.w(TAG, "Escrow token is disabled on the current user");
                    return false;
                }
                com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult authResult = this.mSpManager.unlockWeakTokenBasedProtector(getGateKeeperService(), handle, token, userId);
                if (authResult.syntheticPassword == null) {
                    android.util.Slog.w(TAG, "Invalid escrow token supplied");
                    return false;
                }
                android.os.Binder.restoreCallingIdentity(restoreToken);
                return true;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(restoreToken);
        }
    }

    protected void tieProfileLockToParent(int profileUserId, int parentUserId, com.android.internal.widget.LockscreenCredential password) {
        com.android.server.utils.Slogf.i(TAG, "Tying lock for profile user %d to parent user %d", java.lang.Integer.valueOf(profileUserId), java.lang.Integer.valueOf(parentUserId));
        try {
            try {
                long parentSid = getGateKeeperService().getSecureUserId(parentUserId);
                try {
                    javax.crypto.KeyGenerator keyGenerator = javax.crypto.KeyGenerator.getInstance("AES");
                    keyGenerator.init(new java.security.SecureRandom());
                    javax.crypto.SecretKey secretKey = keyGenerator.generateKey();
                    try {
                        this.mKeyStore.setEntry(PROFILE_KEY_NAME_ENCRYPT + profileUserId, new java.security.KeyStore.SecretKeyEntry(secretKey), new android.security.keystore.KeyProtection.Builder(1).setBlockModes("GCM").setEncryptionPaddings("NoPadding").build());
                        this.mKeyStore.setEntry(PROFILE_KEY_NAME_DECRYPT + profileUserId, new java.security.KeyStore.SecretKeyEntry(secretKey), new android.security.keystore.KeyProtection.Builder(2).setBlockModes("GCM").setEncryptionPaddings("NoPadding").setUserAuthenticationRequired(true).setBoundToSpecificSecureUserId(parentSid).setUserAuthenticationValidityDurationSeconds(30).build());
                        javax.crypto.SecretKey keyStoreEncryptionKey = (javax.crypto.SecretKey) this.mKeyStore.getKey(PROFILE_KEY_NAME_ENCRYPT + profileUserId, null);
                        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding");
                        cipher.init(1, keyStoreEncryptionKey);
                        byte[] ciphertext = cipher.doFinal(password.getCredential());
                        byte[] iv = cipher.getIV();
                        if (iv.length != 12) {
                            throw new java.lang.IllegalArgumentException("Invalid iv length: " + iv.length);
                        }
                        this.mStorage.writeChildProfileLock(profileUserId, com.android.internal.util.ArrayUtils.concat(new byte[][]{iv, ciphertext}));
                    } finally {
                        this.mKeyStore.deleteEntry(PROFILE_KEY_NAME_ENCRYPT + profileUserId);
                    }
                } catch (java.security.InvalidKeyException | java.security.KeyStoreException | java.security.NoSuchAlgorithmException | java.security.UnrecoverableKeyException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.NoSuchPaddingException e) {
                    throw new java.lang.IllegalStateException("Failed to encrypt key", e);
                }
            } catch (android.os.RemoteException e2) {
                e = e2;
                throw new java.lang.IllegalStateException("Failed to talk to GateKeeper service", e);
            }
        } catch (android.os.RemoteException e3) {
            e = e3;
        }
    }

    private void setCeStorageProtection(int userId, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp) {
        byte[] secret = sp.deriveFileBasedEncryptionKey();
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mStorageManager.setCeStorageProtection(userId, secret);
            } catch (android.os.RemoteException e) {
                throw new java.lang.IllegalStateException("Failed to protect CE key for user " + userId, e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    private boolean isCeStorageUnlocked(int userId) {
        try {
            return this.mStorageManager.isCeStorageUnlocked(userId);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error checking whether CE storage is unlocked", e);
            return false;
        }
    }

    private void unlockCeStorage(int userId, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp) {
        if (isCeStorageUnlocked(userId)) {
            com.android.server.utils.Slogf.d(TAG, "CE storage for user %d is already unlocked", java.lang.Integer.valueOf(userId));
            return;
        }
        java.lang.String userType = isUserSecure(userId) ? "secured" : "unsecured";
        byte[] secret = sp.deriveFileBasedEncryptionKey();
        try {
            try {
                this.mStorageManager.unlockCeStorage(userId, secret);
                com.android.server.utils.Slogf.i(TAG, "Unlocked CE storage for %s user %d", userType, java.lang.Integer.valueOf(userId));
            } catch (android.os.RemoteException e) {
                com.android.server.utils.Slogf.wtf(TAG, e, "Failed to unlock CE storage for %s user %d", userType, java.lang.Integer.valueOf(userId));
            }
        } finally {
            java.util.Arrays.fill(secret, (byte) 0);
        }
    }

    public void unlockUserKeyIfUnsecured(int userId) {
        checkPasswordReadPermission();
        synchronized (this.mSpManager) {
            if (isCeStorageUnlocked(userId)) {
                com.android.server.utils.Slogf.d(TAG, "CE storage for user %d is already unlocked", java.lang.Integer.valueOf(userId));
                return;
            }
            if (isUserSecure(userId)) {
                com.android.server.utils.Slogf.d(TAG, "Not unlocking CE storage for user %d yet because user is secured", java.lang.Integer.valueOf(userId));
                return;
            }
            com.android.server.utils.Slogf.i(TAG, "Unwrapping synthetic password for unsecured user %d", java.lang.Integer.valueOf(userId));
            com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult result = this.mSpManager.unlockLskfBasedProtector(getGateKeeperService(), getCurrentLskfBasedProtectorId(userId), com.android.internal.widget.LockscreenCredential.createNone(), userId, null);
            if (result.syntheticPassword == null) {
                com.android.server.utils.Slogf.wtf(TAG, "Failed to unwrap synthetic password for unsecured user %d", java.lang.Integer.valueOf(userId));
                return;
            }
            onSyntheticPasswordUnlocked(userId, result.syntheticPassword);
            if (FIX_UNLOCKED_DEVICE_REQUIRED_KEYS) {
                unlockKeystore(userId, result.syntheticPassword);
            }
            unlockCeStorage(userId, result.syntheticPassword);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:55:0x00f3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void resetKeyStore(int r18) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 276
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.locksettings.LockSettingsService.resetKeyStore(int):void");
    }

    public com.android.internal.widget.VerifyCredentialResponse checkCredential(com.android.internal.widget.LockscreenCredential credential, int userId, com.android.internal.widget.ICheckCredentialProgressCallback progressCallback) {
        checkPasswordReadPermission();
        long identity = android.os.Binder.clearCallingIdentity();
        android.util.Slog.d(TAG, "[checkCredential] userId = " + userId);
        try {
            com.android.internal.widget.VerifyCredentialResponse response = doVerifyCredential(credential, userId, progressCallback, 0);
            if (response.getResponseCode() == 0 && userId == 0) {
                java.lang.String credentialString = credential.isNone() ? null : new java.lang.String(credential.getCredential());
                retainPassword(credentialString);
            }
            return response;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
            scheduleGc();
        }
    }

    public com.android.internal.widget.VerifyCredentialResponse verifyCredential(com.android.internal.widget.LockscreenCredential credential, int userId, int flags) {
        android.util.Slog.d(TAG, "[verifyCredential] ENTRY userId = " + userId + ", flags = " + flags);
        if (!hasPermission(PERMISSION) && !hasPermission("android.permission.SET_AND_VERIFY_LOCKSCREEN_CREDENTIALS")) {
            throw new java.lang.SecurityException("verifyCredential requires SET_AND_VERIFY_LOCKSCREEN_CREDENTIALS or android.permission.ACCESS_KEYGUARD_SECURE_STORAGE");
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return doVerifyCredential(credential, userId, null, flags);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
            scheduleGc();
            android.util.Slog.d(TAG, "[verifyCredential] LEAVE");
        }
    }

    public com.android.internal.widget.VerifyCredentialResponse verifyGatekeeperPasswordHandle(long gatekeeperPasswordHandle, long challenge, int userId) {
        byte[] gatekeeperPassword;
        com.android.internal.widget.VerifyCredentialResponse response;
        checkPasswordReadPermission();
        android.util.Slog.d(TAG, "[VerifyCredentialResponse] userId = " + userId + ", challenge = " + challenge);
        synchronized (this.mGatekeeperPasswords) {
            gatekeeperPassword = this.mGatekeeperPasswords.get(gatekeeperPasswordHandle);
        }
        synchronized (this.mSpManager) {
            if (gatekeeperPassword == null) {
                android.util.Slog.d(TAG, "No gatekeeper password for handle");
                response = com.android.internal.widget.VerifyCredentialResponse.ERROR;
            } else {
                response = this.mSpManager.verifyChallengeInternal(getGateKeeperService(), gatekeeperPassword, challenge, userId);
            }
        }
        return response;
    }

    public void removeGatekeeperPasswordHandle(long gatekeeperPasswordHandle) {
        checkPasswordReadPermission();
        android.util.Slog.d(TAG, "[removeGatekeeperPasswordHandle] gatekeeperPasswordHandle = " + gatekeeperPasswordHandle);
        synchronized (this.mGatekeeperPasswords) {
            this.mGatekeeperPasswords.remove(gatekeeperPasswordHandle);
        }
    }

    private com.android.internal.widget.VerifyCredentialResponse doVerifyCredential(com.android.internal.widget.LockscreenCredential credential, int userId, com.android.internal.widget.ICheckCredentialProgressCallback progressCallback, int flags) {
        if (credential == null || credential.isNone()) {
            throw new java.lang.IllegalArgumentException("Credential can't be null or empty");
        }
        if (userId == -9999 && android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0) {
            android.util.Slog.e(TAG, "FRP credential can only be verified prior to provisioning.");
            return com.android.internal.widget.VerifyCredentialResponse.ERROR;
        }
        if (userId == -9998 && !com.android.internal.widget.LockPatternUtils.isRepairModeActive(this.mContext)) {
            android.util.Slog.e(TAG, "Repair mode is not active on the device.");
            return com.android.internal.widget.VerifyCredentialResponse.ERROR;
        }
        com.android.server.utils.Slogf.i(TAG, "Verifying lockscreen credential for user %d", java.lang.Integer.valueOf(userId));
        synchronized (this.mSpManager) {
            if (com.android.internal.widget.LockPatternUtils.isSpecialUserId(userId)) {
                com.android.internal.widget.VerifyCredentialResponse response = this.mSpManager.verifySpecialUserCredential(userId, getGateKeeperService(), credential, progressCallback);
                if (android.security.Flags.frpEnforcement() && response.isMatched() && userId == -9999) {
                    this.mStorage.deactivateFactoryResetProtectionWithoutSecret();
                }
                return response;
            }
            long protectorId = getCurrentLskfBasedProtectorId(userId);
            com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult authResult = this.mSpManager.unlockLskfBasedProtector(getGateKeeperService(), protectorId, credential, userId, progressCallback);
            com.android.internal.widget.VerifyCredentialResponse response2 = authResult.gkResponse;
            if (response2 != null) {
                this.mLockSettingsServiceWrapper.getExtImpl().resetTimeoutFlag(response2);
                android.util.Slog.i(TAG, "doVerifyCredential response.getResponseCode()= " + response2.getResponseCode() + " timeout:" + response2.getTimeout());
            }
            if (response2.getResponseCode() == 0) {
                if ((flags & 2) != 0 && !this.mSpManager.writeRepairModeCredentialLocked(protectorId, userId)) {
                    android.util.Slog.e(TAG, "Failed to write repair mode credential");
                    return com.android.internal.widget.VerifyCredentialResponse.ERROR;
                }
                this.mBiometricDeferredQueue.addPendingLockoutResetForUser(userId, authResult.syntheticPassword.deriveGkPassword());
            }
            if (response2.getResponseCode() == 0) {
                com.android.server.utils.Slogf.i(TAG, "Successfully verified lockscreen credential for user %d", java.lang.Integer.valueOf(userId));
                this.mLockSettingsServiceWrapper.getExtImpl().notifyVoldDecryptAEKey(userId, null, authResult.syntheticPassword.deriveFileBasedEncryptionKey());
                if (userId == 0) {
                    this.mLockSettingsServiceWrapper.getExtImpl().notifyPasswordDerivation(credential, userId);
                }
                onCredentialVerified(authResult.syntheticPassword, android.app.admin.PasswordMetrics.computeForCredential(credential), userId);
                this.mLockSettingsServiceWrapper.getExtImpl().notifyCredentialVerified(progressCallback);
                if ((flags & 1) != 0) {
                    long gkHandle = storeGatekeeperPasswordTemporarily(authResult.syntheticPassword.deriveGkPassword());
                    response2 = new com.android.internal.widget.VerifyCredentialResponse.Builder().setGatekeeperPasswordHandle(gkHandle).build();
                }
                sendCredentialsOnUnlockIfRequired(credential, userId);
            } else if (response2.getResponseCode() == 1 && response2.getTimeout() > 0) {
                requireStrongAuth(8, userId);
            }
            if (android.security.Flags.reportPrimaryAuthAttempts()) {
                boolean success = response2.getResponseCode() == 0;
                notifyLockSettingsStateListeners(success, userId);
            }
            return response2;
        }
    }

    private void notifyLockSettingsStateListeners(boolean success, int userId) {
        for (com.android.internal.widget.LockSettingsStateListener listener : this.mLockSettingsStateListeners) {
            if (success) {
                listener.onAuthenticationSucceeded(userId);
            } else {
                listener.onAuthenticationFailed(userId);
            }
        }
    }

    public com.android.internal.widget.VerifyCredentialResponse verifyTiedProfileChallenge(com.android.internal.widget.LockscreenCredential credential, int userId, int flags) {
        checkPasswordReadPermission();
        com.android.server.utils.Slogf.i(TAG, "Verifying tied profile challenge for user %d", java.lang.Integer.valueOf(userId));
        if (!isProfileWithUnifiedLock(userId)) {
            throw new java.lang.IllegalArgumentException("User id must be managed/clone profile with unified lock");
        }
        int parentProfileId = this.mUserManager.getProfileParent(userId).id;
        com.android.internal.widget.VerifyCredentialResponse parentResponse = doVerifyCredential(credential, parentProfileId, null, flags);
        if (parentResponse.getResponseCode() == 0) {
            try {
                try {
                    return doVerifyCredential(getDecryptedPasswordForTiedProfile(userId), userId, null, flags);
                } catch (java.io.IOException | java.security.InvalidAlgorithmParameterException | java.security.InvalidKeyException | java.security.KeyStoreException | java.security.NoSuchAlgorithmException | java.security.UnrecoverableKeyException | java.security.cert.CertificateException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.NoSuchPaddingException e) {
                    android.util.Slog.e(TAG, "Failed to decrypt child profile key", e);
                    throw new java.lang.IllegalStateException("Unable to get tied profile token");
                }
            } finally {
                scheduleGc();
            }
        }
        return parentResponse;
    }

    private void setUserPasswordMetrics(com.android.internal.widget.LockscreenCredential password, int userHandle) {
        synchronized (this) {
            this.mUserPasswordMetrics.put(userHandle, android.app.admin.PasswordMetrics.computeForCredential(password));
        }
    }

    android.app.admin.PasswordMetrics getUserPasswordMetrics(int userHandle) {
        android.app.admin.PasswordMetrics passwordMetrics;
        if (!isUserSecure(userHandle)) {
            return new android.app.admin.PasswordMetrics(-1);
        }
        synchronized (this) {
            passwordMetrics = this.mUserPasswordMetrics.get(userHandle);
        }
        return passwordMetrics;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.admin.PasswordMetrics loadPasswordMetrics(com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, int userHandle) {
        synchronized (this.mSpManager) {
            if (!isUserSecure(userHandle)) {
                return null;
            }
            return this.mSpManager.getPasswordMetrics(sp, getCurrentLskfBasedProtectorId(userHandle), userHandle);
        }
    }

    private void notifyPasswordChanged(final com.android.internal.widget.LockscreenCredential newCredential, final int userId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.locksettings.LockSettingsService$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyPasswordChanged$7(newCredential, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyPasswordChanged$7(com.android.internal.widget.LockscreenCredential newCredential, int userId) {
        this.mInjector.getDevicePolicyManager().reportPasswordChanged(android.app.admin.PasswordMetrics.computeForCredential(newCredential), userId);
        ((com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class)).reportPasswordChanged(userId);
        if (userId == 0) {
            android.util.Slog.d(TAG, "[notifyPasswordChanged]newCredentialType: " + newCredential.getType() + " userId: " + userId);
            this.mLockSettingsServiceWrapper.getExtImpl().notifyPasswordChanged(newCredential, userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createNewUser(int userId, int userSerialNumber) {
        if (FIX_UNLOCKED_DEVICE_REQUIRED_KEYS) {
            android.security.AndroidKeyStoreMaintenance.onUserAdded(userId);
        }
        synchronized (this.mUserCreationAndRemovalLock) {
            if (!this.mThirdPartyAppsStarted) {
                com.android.server.utils.Slogf.i(TAG, "Delaying locksettings state creation for user %d until third-party apps are started", java.lang.Integer.valueOf(userId));
                this.mEarlyCreatedUsers.put(userId, userSerialNumber);
                this.mEarlyRemovedUsers.delete(userId);
            } else {
                removeStateForReusedUserIdIfNecessary(userId, userSerialNumber);
                initializeSyntheticPassword(userId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeUser(int userId) {
        synchronized (this.mUserCreationAndRemovalLock) {
            if (!this.mThirdPartyAppsStarted) {
                com.android.server.utils.Slogf.i(TAG, "Delaying locksettings state removal for user %d until third-party apps are started", java.lang.Integer.valueOf(userId));
                if (this.mEarlyCreatedUsers.indexOfKey(userId) >= 0) {
                    this.mEarlyCreatedUsers.delete(userId);
                } else {
                    this.mEarlyRemovedUsers.put(userId, -1);
                }
                return;
            }
            com.android.server.utils.Slogf.i(TAG, "Removing state for user %d", java.lang.Integer.valueOf(userId));
            removeUserState(userId);
        }
    }

    private void removeUserState(int userId) {
        removeBiometricsForUser(userId);
        this.mSpManager.removeUser(getGateKeeperService(), userId);
        this.mStrongAuth.removeUser(userId);
        android.security.AndroidKeyStoreMaintenance.onUserRemoved(userId);
        this.mUnifiedProfilePasswordCache.removePassword(userId);
        gateKeeperClearSecureUserId(userId);
        removeKeystoreProfileKey(userId);
        this.mStorage.removeUser(userId);
    }

    private void removeKeystoreProfileKey(int targetUserId) {
        java.lang.String encryptAlias = PROFILE_KEY_NAME_ENCRYPT + targetUserId;
        java.lang.String decryptAlias = PROFILE_KEY_NAME_DECRYPT + targetUserId;
        try {
            if (this.mKeyStore.containsAlias(encryptAlias) || this.mKeyStore.containsAlias(decryptAlias)) {
                com.android.server.utils.Slogf.i(TAG, "Removing keystore profile key for user %d", java.lang.Integer.valueOf(targetUserId));
                this.mKeyStore.deleteEntry(encryptAlias);
                this.mKeyStore.deleteEntry(decryptAlias);
            }
        } catch (java.security.KeyStoreException e) {
            com.android.server.utils.Slogf.e(TAG, e, "Error removing keystore profile key for user %d", java.lang.Integer.valueOf(targetUserId));
        }
    }

    public void registerStrongAuthTracker(android.app.trust.IStrongAuthTracker tracker) {
        checkPasswordReadPermission();
        android.util.Slog.d(TAG, "[registerStrongAuthTracker] pid = " + android.os.Binder.getCallingPid() + ", uid = " + android.os.Binder.getCallingUid());
        this.mStrongAuth.registerStrongAuthTracker(tracker);
    }

    public void unregisterStrongAuthTracker(android.app.trust.IStrongAuthTracker tracker) {
        checkPasswordReadPermission();
        android.util.Slog.d(TAG, "[unregisterStrongAuthTracker] pid = " + android.os.Binder.getCallingPid() + ", uid = " + android.os.Binder.getCallingUid());
        this.mStrongAuth.unregisterStrongAuthTracker(tracker);
    }

    public void requireStrongAuth(int strongAuthReason, int userId) {
        checkWritePermission();
        android.util.Slog.d(TAG, "[requireStrongAuth] strongAuthReason = " + strongAuthReason + ", userId = " + userId);
        this.mStrongAuth.requireStrongAuth(strongAuthReason, userId);
    }

    public void reportSuccessfulBiometricUnlock(boolean isStrongBiometric, int userId) {
        checkBiometricPermission();
        android.util.Slog.d(TAG, "[reportSuccessfulBiometricUnlock] isStrongBiometric = " + isStrongBiometric + ", userId = " + userId);
        this.mStrongAuth.reportSuccessfulBiometricUnlock(isStrongBiometric, userId);
    }

    public void scheduleNonStrongBiometricIdleTimeout(int userId) {
        checkBiometricPermission();
        android.util.Slog.d(TAG, "[scheduleNonStrongBiometricIdleTimeout]  userId = " + userId);
        this.mStrongAuth.scheduleNonStrongBiometricIdleTimeout(userId);
    }

    public void userPresent(int userId) {
        checkWritePermission();
        android.util.Slog.d(TAG, "[userPresent]  userId = " + userId);
        this.mStrongAuth.reportUnlock(userId);
    }

    public int getStrongAuthForUser(int userId) {
        checkPasswordReadPermission();
        android.util.Slog.d(TAG, "[getStrongAuthForUser]  userId = " + userId);
        return this.mStrongAuthTracker.getStrongAuthForUser(userId);
    }

    private boolean isCallerShell() {
        int callingUid = android.os.Binder.getCallingUid();
        return callingUid == 2000 || callingUid == 0;
    }

    private void enforceShell() {
        if (!isCallerShell()) {
            throw new java.lang.SecurityException("Caller must be shell");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        enforceShell();
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.utils.Slogf.i(TAG, "Executing shell command '%s'; callingPid=%d, callingUid=%d", com.android.internal.util.ArrayUtils.isEmpty(args) ? "" : args[0], java.lang.Integer.valueOf(callingPid), java.lang.Integer.valueOf(callingUid));
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.locksettings.LockSettingsShellCommand command = new com.android.server.locksettings.LockSettingsShellCommand(new com.android.internal.widget.LockPatternUtils(this.mContext), this.mContext, callingPid, callingUid);
            command.exec(this, in, out, err, args, callback, resultReceiver);
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void initRecoveryServiceWithSigFile(java.lang.String rootCertificateAlias, byte[] recoveryServiceCertFile, byte[] recoveryServiceSigFile) throws android.os.RemoteException {
        this.mRecoverableKeyStoreManager.initRecoveryServiceWithSigFile(rootCertificateAlias, recoveryServiceCertFile, recoveryServiceSigFile);
    }

    public android.security.keystore.recovery.KeyChainSnapshot getKeyChainSnapshot() throws android.os.RemoteException {
        return this.mRecoverableKeyStoreManager.getKeyChainSnapshot();
    }

    public void setSnapshotCreatedPendingIntent(android.app.PendingIntent intent) throws android.os.RemoteException {
        this.mRecoverableKeyStoreManager.setSnapshotCreatedPendingIntent(intent);
    }

    public void setServerParams(byte[] serverParams) throws android.os.RemoteException {
        this.mRecoverableKeyStoreManager.setServerParams(serverParams);
    }

    public void setRecoveryStatus(java.lang.String alias, int status) throws android.os.RemoteException {
        this.mRecoverableKeyStoreManager.setRecoveryStatus(alias, status);
    }

    public java.util.Map getRecoveryStatus() throws android.os.RemoteException {
        return this.mRecoverableKeyStoreManager.getRecoveryStatus();
    }

    public void setRecoverySecretTypes(int[] secretTypes) throws android.os.RemoteException {
        this.mRecoverableKeyStoreManager.setRecoverySecretTypes(secretTypes);
    }

    public int[] getRecoverySecretTypes() throws android.os.RemoteException {
        return this.mRecoverableKeyStoreManager.getRecoverySecretTypes();
    }

    public byte[] startRecoverySessionWithCertPath(java.lang.String sessionId, java.lang.String rootCertificateAlias, android.security.keystore.recovery.RecoveryCertPath verifierCertPath, byte[] vaultParams, byte[] vaultChallenge, java.util.List<android.security.keystore.recovery.KeyChainProtectionParams> secrets) throws android.os.RemoteException {
        return this.mRecoverableKeyStoreManager.startRecoverySessionWithCertPath(sessionId, rootCertificateAlias, verifierCertPath, vaultParams, vaultChallenge, secrets);
    }

    public java.util.Map<java.lang.String, java.lang.String> recoverKeyChainSnapshot(java.lang.String sessionId, byte[] recoveryKeyBlob, java.util.List<android.security.keystore.recovery.WrappedApplicationKey> applicationKeys) throws android.os.RemoteException {
        return this.mRecoverableKeyStoreManager.recoverKeyChainSnapshot(sessionId, recoveryKeyBlob, applicationKeys);
    }

    public void closeSession(java.lang.String sessionId) throws android.os.RemoteException {
        this.mRecoverableKeyStoreManager.closeSession(sessionId);
    }

    public void removeKey(java.lang.String alias) throws android.os.RemoteException {
        this.mRecoverableKeyStoreManager.removeKey(alias);
    }

    public java.lang.String generateKey(java.lang.String alias) throws android.os.RemoteException {
        return this.mRecoverableKeyStoreManager.generateKey(alias);
    }

    public java.lang.String generateKeyWithMetadata(java.lang.String alias, byte[] metadata) throws android.os.RemoteException {
        return this.mRecoverableKeyStoreManager.generateKeyWithMetadata(alias, metadata);
    }

    public java.lang.String importKey(java.lang.String alias, byte[] keyBytes) throws android.os.RemoteException {
        return this.mRecoverableKeyStoreManager.importKey(alias, keyBytes);
    }

    public java.lang.String importKeyWithMetadata(java.lang.String alias, byte[] keyBytes, byte[] metadata) throws android.os.RemoteException {
        return this.mRecoverableKeyStoreManager.importKeyWithMetadata(alias, keyBytes, metadata);
    }

    public java.lang.String getKey(java.lang.String alias) throws android.os.RemoteException {
        return this.mRecoverableKeyStoreManager.getKey(alias);
    }

    public android.app.RemoteLockscreenValidationSession startRemoteLockscreenValidation() {
        return this.mRecoverableKeyStoreManager.startRemoteLockscreenValidation(this);
    }

    public android.app.RemoteLockscreenValidationResult validateRemoteLockscreen(byte[] encryptedCredential) {
        return this.mRecoverableKeyStoreManager.validateRemoteLockscreen(encryptedCredential, this);
    }

    private class GateKeeperDiedRecipient implements android.os.IBinder.DeathRecipient {
        private GateKeeperDiedRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.locksettings.LockSettingsService.this.mGateKeeperService.asBinder().unlinkToDeath(this, 0);
            com.android.server.locksettings.LockSettingsService.this.mGateKeeperService = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized android.service.gatekeeper.IGateKeeperService getGateKeeperService() {
        if (this.mGateKeeperService != null) {
            return this.mGateKeeperService;
        }
        android.util.Slog.i(TAG, "start acquire GateKeeperService");
        android.os.IBinder service = android.os.ServiceManager.waitForService("android.service.gatekeeper.IGateKeeperService");
        if (service != null) {
            try {
                service.linkToDeath(new com.android.server.locksettings.LockSettingsService.GateKeeperDiedRecipient(), 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, " Unable to register death recipient", e);
            }
            this.mGateKeeperService = android.service.gatekeeper.IGateKeeperService.Stub.asInterface(service);
            return this.mGateKeeperService;
        }
        android.util.Slog.e(TAG, "Unable to acquire GateKeeperService");
        return null;
    }

    private void gateKeeperClearSecureUserId(int userId) {
        try {
            getGateKeeperService().clearSecureUserId(userId);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to clear SID", e);
        }
    }

    private void onSyntheticPasswordCreated(int userId, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp) {
        onSyntheticPasswordKnown(userId, sp, true);
    }

    private void onSyntheticPasswordUnlocked(int userId, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp) {
        onSyntheticPasswordKnown(userId, sp, false);
    }

    private void onSyntheticPasswordKnown(int userId, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, boolean justCreated) {
        if (this.mInjector.isGsiRunning()) {
            android.util.Slog.w(TAG, "Running in GSI; skipping calls to AuthSecret and RebootEscrow");
        } else {
            this.mRebootEscrowManager.callToRebootEscrowIfNeeded(userId, sp.getVersion(), sp.getSyntheticPassword());
            callToAuthSecretIfNeeded(userId, sp, justCreated);
        }
    }

    private void callToAuthSecretIfNeeded(int userId, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, boolean justCreated) {
        byte[] authSecret;
        byte[] authSecret2;
        if (this.mAuthSecretService == null) {
            return;
        }
        com.android.server.pm.UserManagerInternal userManagerInternal = this.mInjector.getUserManagerInternal();
        android.content.pm.UserInfo userInfo = userManagerInternal.getUserInfo(userId);
        if (userInfo == null) {
            return;
        }
        if (!this.mInjector.isHeadlessSystemUserMode()) {
            if (userId != 0) {
                return;
            } else {
                authSecret = sp.deriveVendorAuthSecret();
            }
        } else {
            if (!this.mInjector.isMainUserPermanentAdmin() || !userInfo.isFull()) {
                return;
            }
            if (justCreated) {
                if (userInfo.isMain()) {
                    android.util.Slog.i(TAG, "Generating new vendor auth secret and storing for user: " + userId);
                    authSecret = com.android.server.locksettings.SecureRandomUtils.randomBytes(32);
                    synchronized (this.mHeadlessAuthSecretLock) {
                        this.mAuthSecret = authSecret;
                    }
                } else {
                    synchronized (this.mHeadlessAuthSecretLock) {
                        authSecret2 = this.mAuthSecret;
                    }
                    if (authSecret2 != null) {
                        authSecret = authSecret2;
                    } else {
                        android.util.Slog.e(TAG, "Creating non-main user " + userId + " but vendor auth secret is not in memory");
                        return;
                    }
                }
                this.mSpManager.writeVendorAuthSecret(authSecret, sp, userId);
            } else {
                authSecret = this.mSpManager.readVendorAuthSecret(sp, userId);
                if (authSecret == null) {
                    android.util.Slog.e(TAG, "Unable to read vendor auth secret for user: " + userId);
                    return;
                } else {
                    synchronized (this.mHeadlessAuthSecretLock) {
                        this.mAuthSecret = authSecret;
                    }
                }
            }
        }
        android.util.Slog.i(TAG, "Sending vendor auth secret to IAuthSecret HAL as user: " + userId);
        try {
            this.mAuthSecretService.setPrimaryUserCredential(authSecret);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to send vendor auth secret to IAuthSecret HAL", e);
        }
    }

    com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword initializeSyntheticPassword(int userId) {
        com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp;
        synchronized (this.mSpManager) {
            com.android.server.utils.Slogf.i(TAG, "Initializing synthetic password for user %d", java.lang.Integer.valueOf(userId));
            com.android.internal.util.Preconditions.checkState(getCurrentLskfBasedProtectorId(userId) == 0, "Cannot reinitialize SP");
            sp = this.mSpManager.newSyntheticPassword(userId);
            long protectorId = this.mSpManager.createLskfBasedProtector(getGateKeeperService(), com.android.internal.widget.LockscreenCredential.createNone(), sp, userId);
            setCurrentLskfBasedProtectorId(protectorId, userId);
            setCeStorageProtection(userId, sp);
            if (FIX_UNLOCKED_DEVICE_REQUIRED_KEYS) {
                initKeystoreSuperKeys(userId, sp, false);
            }
            onSyntheticPasswordCreated(userId, sp);
            com.android.server.utils.Slogf.i(TAG, "Successfully initialized synthetic password for user %d", java.lang.Integer.valueOf(userId));
        }
        return sp;
    }

    long getCurrentLskfBasedProtectorId(int userId) {
        return getLong("sp-handle", 0L, userId);
    }

    private void setCurrentLskfBasedProtectorId(long newProtectorId, int userId) {
        long oldProtectorId = getCurrentLskfBasedProtectorId(userId);
        setLong("sp-handle", newProtectorId, userId);
        setLong(PREV_LSKF_BASED_PROTECTOR_ID_KEY, oldProtectorId, userId);
        setLong(LSKF_LAST_CHANGED_TIME_KEY, java.lang.System.currentTimeMillis(), userId);
    }

    private long storeGatekeeperPasswordTemporarily(byte[] gatekeeperPassword) {
        long handle = 0;
        synchronized (this.mGatekeeperPasswords) {
            while (true) {
                if (handle != 0) {
                    if (this.mGatekeeperPasswords.get(handle) == null) {
                        this.mGatekeeperPasswords.put(handle, gatekeeperPassword);
                    }
                }
                handle = com.android.server.locksettings.SecureRandomUtils.randomLong();
            }
        }
        final long finalHandle = handle;
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.locksettings.LockSettingsService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$storeGatekeeperPasswordTemporarily$8(finalHandle);
            }
        }, 600000L);
        return handle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$storeGatekeeperPasswordTemporarily$8(long finalHandle) {
        synchronized (this.mGatekeeperPasswords) {
            if (this.mGatekeeperPasswords.get(finalHandle) != null) {
                com.android.server.utils.Slogf.d(TAG, "Cached Gatekeeper password with handle %016x has expired", java.lang.Long.valueOf(finalHandle));
                this.mGatekeeperPasswords.remove(finalHandle);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCredentialVerified(com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, android.app.admin.PasswordMetrics metrics, int userId) {
        if (metrics != null) {
            synchronized (this) {
                this.mUserPasswordMetrics.put(userId, metrics);
            }
        }
        unlockKeystore(userId, sp);
        unlockCeStorage(userId, sp);
        lambda$setLockCredentialWithToken$9(userId);
        activateEscrowTokens(sp, userId);
        if (isCredentialSharableWithParent(userId)) {
            if (getSeparateProfileChallengeEnabledInternal(userId)) {
                setDeviceUnlockedForUser(userId);
            } else {
                this.mStrongAuth.reportUnlock(userId);
            }
        }
        this.mStrongAuth.reportSuccessfulStrongAuthUnlock(userId);
        onSyntheticPasswordUnlocked(userId, sp);
    }

    private void setDeviceUnlockedForUser(int userId) {
        android.app.trust.TrustManager trustManager = (android.app.trust.TrustManager) this.mContext.getSystemService(android.app.trust.TrustManager.class);
        trustManager.setDeviceLockedForUser(userId, false);
    }

    private long setLockCredentialWithSpLocked(com.android.internal.widget.LockscreenCredential credential, com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, int userId) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.InvalidKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
        java.util.Map<java.lang.Integer, com.android.internal.widget.LockscreenCredential> profilePasswords;
        com.android.server.utils.Slogf.i(TAG, "Changing lockscreen credential of user %d; newCredentialType=%s\n", java.lang.Integer.valueOf(userId), com.android.internal.widget.LockPatternUtils.credentialTypeToString(credential.getType()));
        int savedCredentialType = getCredentialTypeInternal(userId);
        long oldProtectorId = getCurrentLskfBasedProtectorId(userId);
        long newProtectorId = this.mSpManager.createLskfBasedProtector(getGateKeeperService(), credential, sp, userId);
        if (!credential.isNone()) {
            if (!this.mSpManager.hasSidForUser(userId)) {
                this.mSpManager.newSidForUser(getGateKeeperService(), sp, userId);
                this.mSpManager.verifyChallenge(getGateKeeperService(), sp, 0L, userId);
                if (!FIX_UNLOCKED_DEVICE_REQUIRED_KEYS) {
                    setKeystorePassword(sp.deriveKeyStorePassword(), userId);
                }
            }
            profilePasswords = null;
        } else {
            java.util.Map<java.lang.Integer, com.android.internal.widget.LockscreenCredential> profilePasswords2 = getDecryptedPasswordsForAllTiedProfiles(userId);
            this.mSpManager.clearSidForUser(userId);
            gateKeeperClearSecureUserId(userId);
            unlockCeStorage(userId, sp);
            unlockKeystore(userId, sp);
            if (!FIX_UNLOCKED_DEVICE_REQUIRED_KEYS) {
                setKeystorePassword(null, userId);
            } else {
                android.security.AndroidKeyStoreMaintenance.onUserLskfRemoved(userId);
            }
            removeBiometricsForUser(userId);
            profilePasswords = profilePasswords2;
        }
        setCurrentLskfBasedProtectorId(newProtectorId, userId);
        com.android.internal.widget.LockPatternUtils.invalidateCredentialTypeCache();
        synchronizeUnifiedChallengeForProfiles(userId, profilePasswords);
        setUserPasswordMetrics(credential, userId);
        this.mUnifiedProfilePasswordCache.removePassword(userId);
        if (savedCredentialType != -1) {
            this.mSpManager.destroyAllWeakTokenBasedProtectors(userId);
        }
        if (profilePasswords != null) {
            for (java.util.Map.Entry<java.lang.Integer, com.android.internal.widget.LockscreenCredential> entry : profilePasswords.entrySet()) {
                entry.getValue().zeroize();
            }
        }
        this.mSpManager.destroyLskfBasedProtector(oldProtectorId, userId);
        com.android.server.utils.Slogf.i(TAG, "Successfully changed lockscreen credential of user %d", java.lang.Integer.valueOf(userId));
        return newProtectorId;
    }

    private void sendMainUserCredentialChangedNotificationIfNeeded(int userId) {
        if (!android.security.Flags.frpEnforcement() || userId != this.mInjector.getUserManagerInternal().getMainUserId()) {
            return;
        }
        sendBroadcast(new android.content.Intent("android.intent.action.MAIN_USER_LOCKSCREEN_KNOWLEDGE_FACTOR_CHANGED"), android.os.UserHandle.of(userId), "android.permission.CONFIGURE_FACTORY_RESET_PROTECTION");
    }

    void sendBroadcast(android.content.Intent intent, android.os.UserHandle userHandle, java.lang.String permission) {
        this.mContext.sendBroadcastAsUser(intent, userHandle, permission, null);
    }

    private void removeBiometricsForUser(final int userId) {
        if (this.mLockSettingsServiceWrapper.getExtImpl().isOplusMultiAppUserId(userId)) {
            return;
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.locksettings.LockSettingsService.5
            @Override // java.lang.Runnable
            public void run() {
                android.util.Slog.v(com.android.server.locksettings.LockSettingsService.TAG, "removeBiometricsForUser userId:" + userId);
                com.android.server.locksettings.LockSettingsService.this.removeAllFingerprintForUser(userId);
                com.android.server.locksettings.LockSettingsService.this.removeAllFaceForUser(userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeAllFingerprintForUser(int userId) {
        android.hardware.fingerprint.FingerprintManager mFingerprintManager = this.mInjector.getFingerprintManager();
        if (mFingerprintManager != null && mFingerprintManager.isHardwareDetected() && mFingerprintManager.hasEnrolledFingerprints(userId)) {
            java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
            mFingerprintManager.removeAll(userId, fingerprintManagerRemovalCallback(latch));
            try {
                latch.await(10000L, java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (java.lang.InterruptedException e) {
                android.util.Slog.e(TAG, "Latch interrupted when removing fingerprint", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeAllFaceForUser(int userId) {
        android.hardware.face.FaceManager mFaceManager = this.mInjector.getFaceManager();
        if (mFaceManager != null && mFaceManager.isHardwareDetected() && mFaceManager.hasEnrolledTemplates(userId)) {
            java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
            mFaceManager.removeAll(userId, faceManagerRemovalCallback(latch));
            try {
                latch.await(10000L, java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (java.lang.InterruptedException e) {
                android.util.Slog.e(TAG, "Latch interrupted when removing face", e);
            }
        }
    }

    private android.hardware.fingerprint.FingerprintManager.RemovalCallback fingerprintManagerRemovalCallback(final java.util.concurrent.CountDownLatch latch) {
        return new android.hardware.fingerprint.FingerprintManager.RemovalCallback() { // from class: com.android.server.locksettings.LockSettingsService.6
            public void onRemovalError(android.hardware.fingerprint.Fingerprint fp, int errMsgId, java.lang.CharSequence err) {
                android.util.Slog.e(com.android.server.locksettings.LockSettingsService.TAG, "Unable to remove fingerprint, error: " + ((java.lang.Object) err));
                latch.countDown();
            }

            public void onRemovalSucceeded(android.hardware.fingerprint.Fingerprint fp, int remaining) {
                if (remaining == 0) {
                    latch.countDown();
                }
            }
        };
    }

    private android.hardware.face.FaceManager.RemovalCallback faceManagerRemovalCallback(final java.util.concurrent.CountDownLatch latch) {
        return new android.hardware.face.FaceManager.RemovalCallback() { // from class: com.android.server.locksettings.LockSettingsService.7
            public void onRemovalError(android.hardware.face.Face face, int errMsgId, java.lang.CharSequence err) {
                android.util.Slog.e(com.android.server.locksettings.LockSettingsService.TAG, "Unable to remove face, error: " + ((java.lang.Object) err));
                latch.countDown();
            }

            public void onRemovalSucceeded(android.hardware.face.Face face, int remaining) {
                if (remaining == 0) {
                    latch.countDown();
                }
            }
        };
    }

    public byte[] getHashFactor(com.android.internal.widget.LockscreenCredential currentCredential, int userId) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.InvalidKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
        checkPasswordReadPermission();
        try {
            com.android.server.utils.Slogf.d(TAG, "Getting password history hash factor for user %d", java.lang.Integer.valueOf(userId));
            if (isProfileWithUnifiedLock(userId)) {
                currentCredential = getDecryptedPasswordForTiedProfile(userId);
            }
            synchronized (this.mSpManager) {
                long protectorId = getCurrentLskfBasedProtectorId(userId);
                com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult auth = this.mSpManager.unlockLskfBasedProtector(getGateKeeperService(), protectorId, currentCredential, userId, null);
                if (auth.syntheticPassword == null) {
                    android.util.Slog.w(TAG, "Current credential is incorrect");
                    return null;
                }
                return auth.syntheticPassword.derivePasswordHashFactor();
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Failed to get unified profile password", e);
            return null;
        } finally {
            scheduleGc();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long addEscrowToken(byte[] token, int type, int userId, com.android.internal.widget.LockPatternUtils.EscrowTokenStateChangeCallback callback) {
        long handle;
        com.android.server.utils.Slogf.i(TAG, "Adding escrow token for user %d", java.lang.Integer.valueOf(userId));
        synchronized (this.mSpManager) {
            com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp = null;
            if (!isUserSecure(userId)) {
                long protectorId = getCurrentLskfBasedProtectorId(userId);
                sp = this.mSpManager.unlockLskfBasedProtector(getGateKeeperService(), protectorId, com.android.internal.widget.LockscreenCredential.createNone(), userId, null).syntheticPassword;
            }
            disableEscrowTokenOnNonManagedDevicesIfNeeded(userId);
            if (!this.mSpManager.hasEscrowData(userId)) {
                throw new java.lang.SecurityException("Escrow token is disabled on the current user");
            }
            handle = this.mSpManager.addPendingToken(token, type, userId, callback);
            if (sp != null) {
                com.android.server.utils.Slogf.i(TAG, "Immediately activating escrow token %016x", java.lang.Long.valueOf(handle));
                this.mSpManager.createTokenBasedProtector(handle, sp, userId);
            } else {
                com.android.server.utils.Slogf.i(TAG, "Escrow token %016x will be activated when user is unlocked", java.lang.Long.valueOf(handle));
            }
        }
        return handle;
    }

    private void activateEscrowTokens(com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp, int userId) {
        synchronized (this.mSpManager) {
            disableEscrowTokenOnNonManagedDevicesIfNeeded(userId);
            java.util.Iterator<java.lang.Long> it = this.mSpManager.getPendingTokensForUser(userId).iterator();
            while (it.hasNext()) {
                long handle = it.next().longValue();
                com.android.server.utils.Slogf.i(TAG, "Activating escrow token %016x for user %d", java.lang.Long.valueOf(handle), java.lang.Integer.valueOf(userId));
                this.mSpManager.createTokenBasedProtector(handle, sp, userId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEscrowTokenActive(long handle, int userId) {
        boolean zProtectorExists;
        synchronized (this.mSpManager) {
            zProtectorExists = this.mSpManager.protectorExists(handle, userId);
        }
        return zProtectorExists;
    }

    public boolean hasPendingEscrowToken(int userId) {
        boolean z;
        checkPasswordReadPermission();
        synchronized (this.mSpManager) {
            z = !this.mSpManager.getPendingTokensForUser(userId).isEmpty();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeEscrowToken(long handle, int userId) {
        synchronized (this.mSpManager) {
            if (handle == getCurrentLskfBasedProtectorId(userId)) {
                android.util.Slog.w(TAG, "Escrow token handle equals LSKF-based protector ID");
                return false;
            }
            if (this.mSpManager.removePendingToken(handle, userId)) {
                return true;
            }
            if (!this.mSpManager.protectorExists(handle, userId)) {
                return false;
            }
            this.mSpManager.destroyTokenBasedProtector(handle, userId);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setLockCredentialWithToken(com.android.internal.widget.LockscreenCredential credential, long tokenHandle, byte[] token, final int userId) {
        credential.validateBasicRequirements();
        synchronized (this.mSpManager) {
            if (!this.mSpManager.hasEscrowData(userId)) {
                throw new java.lang.SecurityException("Escrow token is disabled on the current user");
            }
            if (!isEscrowTokenActive(tokenHandle, userId)) {
                android.util.Slog.e(TAG, "Unknown or unactivated token: " + java.lang.Long.toHexString(tokenHandle));
                return false;
            }
            boolean result = setLockCredentialWithTokenInternalLocked(credential, tokenHandle, token, userId);
            if (result) {
                synchronized (this.mSeparateChallengeLock) {
                    setSeparateProfileChallengeEnabledLocked(userId, true, null);
                }
                if (credential.isNone()) {
                    this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.locksettings.LockSettingsService$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$setLockCredentialWithToken$9(userId);
                        }
                    });
                }
                notifyPasswordChanged(credential, userId);
                notifySeparateProfileChallengeChanged(userId);
            }
            return result;
        }
    }

    private boolean setLockCredentialWithTokenInternalLocked(com.android.internal.widget.LockscreenCredential credential, long tokenHandle, byte[] token, int userId) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.InvalidKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
        com.android.server.utils.Slogf.i(TAG, "Resetting lockscreen credential of user %d using escrow token %016x", java.lang.Integer.valueOf(userId), java.lang.Long.valueOf(tokenHandle));
        com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult result = this.mSpManager.unlockTokenBasedProtector(getGateKeeperService(), tokenHandle, token, userId);
        if (result.syntheticPassword == null) {
            android.util.Slog.w(TAG, "Invalid escrow token supplied");
            return false;
        }
        if (result.gkResponse.getResponseCode() != 0) {
            android.util.Slog.e(TAG, "Obsolete token: synthetic password decrypted but it fails GK verification.");
            return false;
        }
        onSyntheticPasswordUnlocked(userId, result.syntheticPassword);
        setLockCredentialWithSpLocked(credential, result.syntheticPassword, userId);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean unlockUserWithToken(long tokenHandle, byte[] token, int userId) {
        synchronized (this.mSpManager) {
            com.android.server.utils.Slogf.i(TAG, "Unlocking user %d using escrow token %016x", java.lang.Integer.valueOf(userId), java.lang.Long.valueOf(tokenHandle));
            if (!this.mSpManager.hasEscrowData(userId)) {
                com.android.server.utils.Slogf.w(TAG, "Escrow token support is disabled on user %d", java.lang.Integer.valueOf(userId));
                return false;
            }
            com.android.server.locksettings.SyntheticPasswordManager.AuthenticationResult authResult = this.mSpManager.unlockTokenBasedProtector(getGateKeeperService(), tokenHandle, token, userId);
            if (authResult.syntheticPassword == null) {
                android.util.Slog.w(TAG, "Invalid escrow token supplied");
                return false;
            }
            com.android.server.utils.Slogf.i(TAG, "Unlocked synthetic password for user %d using escrow token", java.lang.Integer.valueOf(userId));
            onCredentialVerified(authResult.syntheticPassword, loadPasswordMetrics(authResult.syntheticPassword, userId), userId);
            return true;
        }
    }

    public boolean tryUnlockWithCachedUnifiedChallenge(int userId) {
        checkPasswordReadPermission();
        com.android.internal.widget.LockscreenCredential cred = this.mUnifiedProfilePasswordCache.retrievePassword(userId);
        if (cred != null) {
            try {
                boolean z = doVerifyCredential(cred, userId, null, 0).getResponseCode() == 0;
                if (cred != null) {
                    cred.close();
                }
                return z;
            } catch (java.lang.Throwable th) {
                if (cred != null) {
                    try {
                        cred.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (cred != null) {
            cred.close();
        }
        return false;
    }

    public void removeCachedUnifiedChallenge(int userId) {
        checkWritePermission();
        this.mUnifiedProfilePasswordCache.removePassword(userId);
    }

    static java.lang.String timestampToString(long timestamp) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter printWriter, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (args.length > 0 && "recordLog".equals(args[0])) {
                    this.mLockSettingsServiceWrapper.getExtImpl().dumpRedLog(printWriter);
                } else {
                    dumpInternal(printWriter);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    private void dumpInternal(java.io.PrintWriter printWriter) {
        com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(printWriter, "  ");
        pw.println("Current lock settings service state:");
        pw.println();
        pw.println("User State:");
        pw.increaseIndent();
        java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getUsers();
        for (int user = 0; user < users.size(); user++) {
            int userId = users.get(user).id;
            pw.println("User " + userId);
            pw.increaseIndent();
            synchronized (this.mSpManager) {
                pw.println(android.text.TextUtils.formatSimple("LSKF-based SP protector ID: %016x", new java.lang.Object[]{java.lang.Long.valueOf(getCurrentLskfBasedProtectorId(userId))}));
                pw.println(android.text.TextUtils.formatSimple("LSKF last changed: %s (previous protector: %016x)", new java.lang.Object[]{timestampToString(getLong(LSKF_LAST_CHANGED_TIME_KEY, 0L, userId)), java.lang.Long.valueOf(getLong(PREV_LSKF_BASED_PROTECTOR_ID_KEY, 0L, userId))}));
            }
            try {
                pw.println(android.text.TextUtils.formatSimple("SID: %016x", new java.lang.Object[]{java.lang.Long.valueOf(getGateKeeperService().getSecureUserId(userId))}));
            } catch (android.os.RemoteException e) {
            }
            pw.println("Quality: " + getKeyguardStoredQuality(userId));
            pw.println("CredentialType: " + com.android.internal.widget.LockPatternUtils.credentialTypeToString(getCredentialTypeInternal(userId)));
            pw.println("SeparateChallenge: " + getSeparateProfileChallengeEnabledInternal(userId));
            pw.println(android.text.TextUtils.formatSimple("Metrics: %s", new java.lang.Object[]{getUserPasswordMetrics(userId) != null ? "known" : "unknown"}));
            pw.decreaseIndent();
        }
        pw.println();
        pw.decreaseIndent();
        pw.println("Keys in namespace:");
        pw.increaseIndent();
        dumpKeystoreKeys(pw);
        pw.println();
        pw.decreaseIndent();
        pw.println("Storage:");
        pw.increaseIndent();
        this.mStorage.dump(pw);
        pw.println();
        pw.decreaseIndent();
        pw.println("StrongAuth:");
        pw.increaseIndent();
        this.mStrongAuth.dump(pw);
        pw.println();
        pw.decreaseIndent();
        pw.println("RebootEscrow:");
        pw.increaseIndent();
        this.mRebootEscrowManager.dump(pw);
        pw.println();
        pw.decreaseIndent();
        pw.println("PasswordHandleCount: " + this.mGatekeeperPasswords.size());
        synchronized (this.mUserCreationAndRemovalLock) {
            pw.println("ThirdPartyAppsStarted: " + this.mThirdPartyAppsStarted);
        }
    }

    private void dumpKeystoreKeys(com.android.internal.util.IndentingPrintWriter pw) {
        try {
            java.util.Enumeration<java.lang.String> aliases = this.mKeyStore.aliases();
            while (aliases.hasMoreElements()) {
                pw.println(aliases.nextElement());
            }
        } catch (java.security.KeyStoreException e) {
            pw.println("Unable to get keys: " + e.toString());
            android.util.Slog.d(TAG, "Dump error", e);
        }
    }

    private void disableEscrowTokenOnNonManagedDevicesIfNeeded(int userId) {
        if (this.mSpManager.hasAnyEscrowData(userId)) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (!android.provider.DeviceConfig.getBoolean("device_policy_manager", "deprecate_usermanagerinternal_devicepolicy", true)) {
                    com.android.server.pm.UserManagerInternal userManagerInternal = this.mInjector.getUserManagerInternal();
                    if (userManagerInternal.isUserManaged(userId)) {
                        android.util.Slog.i(TAG, "Managed profile can have escrow token");
                        return;
                    } else if (userManagerInternal.isDeviceManaged()) {
                        android.util.Slog.i(TAG, "Corp-owned device can have escrow token");
                        return;
                    }
                } else if (this.mInjector.getDeviceStateCache().isUserOrganizationManaged(userId)) {
                    android.util.Slog.i(TAG, "Organization managed users can have escrow token");
                    return;
                }
                android.os.Binder.restoreCallingIdentity(identity);
                if (!this.mInjector.getDeviceStateCache().isDeviceProvisioned()) {
                    android.util.Slog.i(TAG, "Postpone disabling escrow tokens until device is provisioned");
                } else {
                    if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive") || this.mLockSettingsServiceWrapper.getExtImpl().escrowtokenSupport()) {
                        return;
                    }
                    com.android.server.utils.Slogf.i(TAG, "Permanently disabling support for escrow tokens on user %d", java.lang.Integer.valueOf(userId));
                    this.mSpManager.destroyEscrowData(userId);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    private void scheduleGc() {
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.locksettings.LockSettingsService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.locksettings.LockSettingsService.lambda$scheduleGc$10();
            }
        }, 2000L);
    }

    static /* synthetic */ void lambda$scheduleGc$10() {
        java.lang.System.gc();
        java.lang.System.runFinalization();
        java.lang.System.gc();
    }

    private class DeviceProvisionedObserver extends android.database.ContentObserver {
        private final android.net.Uri mDeviceProvisionedUri;
        private boolean mRegistered;

        public DeviceProvisionedObserver() {
            super(null);
            this.mDeviceProvisionedUri = android.provider.Settings.Global.getUriFor("device_provisioned");
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            if (this.mDeviceProvisionedUri.equals(uri)) {
                updateRegistration();
                if (isProvisioned()) {
                    android.util.Slog.i(com.android.server.locksettings.LockSettingsService.TAG, "Reporting device setup complete to IGateKeeperService");
                    reportDeviceSetupComplete();
                    clearFrpCredentialIfOwnerNotSecure();
                }
            }
        }

        public void onSystemReady() {
            if (com.android.internal.widget.LockPatternUtils.frpCredentialEnabled(com.android.server.locksettings.LockSettingsService.this.mContext)) {
                updateRegistration();
            } else if (!isProvisioned()) {
                android.util.Slog.i(com.android.server.locksettings.LockSettingsService.TAG, "FRP credential disabled, reporting device setup complete to Gatekeeper immediately");
                reportDeviceSetupComplete();
            }
        }

        private void reportDeviceSetupComplete() {
            try {
                com.android.server.locksettings.LockSettingsService.this.getGateKeeperService().reportDeviceSetupComplete();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.locksettings.LockSettingsService.TAG, "Failure reporting to IGateKeeperService", e);
            }
        }

        private void clearFrpCredentialIfOwnerNotSecure() {
            java.util.List<android.content.pm.UserInfo> users = com.android.server.locksettings.LockSettingsService.this.mUserManager.getUsers();
            for (android.content.pm.UserInfo user : users) {
                if (com.android.internal.widget.LockPatternUtils.userOwnsFrpCredential(com.android.server.locksettings.LockSettingsService.this.mContext, user)) {
                    if (!com.android.server.locksettings.LockSettingsService.this.isUserSecure(user.id)) {
                        com.android.server.utils.Slogf.d(com.android.server.locksettings.LockSettingsService.TAG, "Clearing FRP credential tied to user %d", java.lang.Integer.valueOf(user.id));
                        com.android.server.locksettings.LockSettingsService.this.mStorage.writePersistentDataBlock(0, user.id, 0, null);
                        return;
                    }
                    return;
                }
            }
        }

        private void updateRegistration() {
            boolean register = !isProvisioned();
            if (register == this.mRegistered) {
                return;
            }
            if (register) {
                com.android.server.locksettings.LockSettingsService.this.mContext.getContentResolver().registerContentObserver(this.mDeviceProvisionedUri, false, this);
            } else {
                com.android.server.locksettings.LockSettingsService.this.mContext.getContentResolver().unregisterContentObserver(this);
            }
            this.mRegistered = register;
        }

        private boolean isProvisioned() {
            return android.provider.Settings.Global.getInt(com.android.server.locksettings.LockSettingsService.this.mContext.getContentResolver(), "device_provisioned", 0) != 0;
        }
    }

    private final class LocalService extends com.android.internal.widget.LockSettingsInternal {
        private LocalService() {
        }

        public void onThirdPartyAppsStarted() {
            com.android.server.locksettings.LockSettingsService.this.onThirdPartyAppsStarted();
        }

        public void createNewUser(int userId, int userSerialNumber) {
            com.android.server.locksettings.LockSettingsService.this.createNewUser(userId, userSerialNumber);
        }

        public void removeUser(int userId) {
            com.android.server.locksettings.LockSettingsService.this.removeUser(userId);
        }

        public long addEscrowToken(byte[] token, int userId, com.android.internal.widget.LockPatternUtils.EscrowTokenStateChangeCallback callback) {
            return com.android.server.locksettings.LockSettingsService.this.addEscrowToken(token, 0, userId, callback);
        }

        public boolean removeEscrowToken(long handle, int userId) {
            return com.android.server.locksettings.LockSettingsService.this.removeEscrowToken(handle, userId);
        }

        public boolean isEscrowTokenActive(long handle, int userId) {
            return com.android.server.locksettings.LockSettingsService.this.isEscrowTokenActive(handle, userId);
        }

        public boolean setLockCredentialWithToken(com.android.internal.widget.LockscreenCredential credential, long tokenHandle, byte[] token, int userId) throws javax.crypto.BadPaddingException, javax.crypto.NoSuchPaddingException, javax.crypto.IllegalBlockSizeException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.security.InvalidKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
            if (!com.android.server.locksettings.LockSettingsService.this.mHasSecureLockScreen && credential != null && credential.getType() != -1) {
                throw new java.lang.UnsupportedOperationException("This operation requires secure lock screen feature.");
            }
            if (!com.android.server.locksettings.LockSettingsService.this.setLockCredentialWithToken(credential, tokenHandle, token, userId)) {
                return false;
            }
            com.android.server.locksettings.LockSettingsService.this.onPostPasswordChanged(credential, userId);
            return true;
        }

        public boolean unlockUserWithToken(long tokenHandle, byte[] token, int userId) {
            return com.android.server.locksettings.LockSettingsService.this.unlockUserWithToken(tokenHandle, token, userId);
        }

        public android.app.admin.PasswordMetrics getUserPasswordMetrics(int userHandle) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.server.locksettings.LockSettingsService.this.isProfileWithUnifiedLock(userHandle)) {
                    android.util.Slog.w(com.android.server.locksettings.LockSettingsService.TAG, "Querying password metrics for unified challenge profile: " + userHandle);
                }
                return com.android.server.locksettings.LockSettingsService.this.getUserPasswordMetrics(userHandle);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public boolean prepareRebootEscrow() {
            if (!com.android.server.locksettings.LockSettingsService.this.mRebootEscrowManager.prepareRebootEscrow()) {
                return false;
            }
            com.android.server.locksettings.LockSettingsService.this.mStrongAuth.requireStrongAuth(64, -1);
            return true;
        }

        public void setRebootEscrowListener(com.android.internal.widget.RebootEscrowListener listener) {
            com.android.server.locksettings.LockSettingsService.this.mRebootEscrowManager.setRebootEscrowListener(listener);
        }

        public boolean clearRebootEscrow() {
            if (!com.android.server.locksettings.LockSettingsService.this.mRebootEscrowManager.clearRebootEscrow()) {
                return false;
            }
            com.android.server.locksettings.LockSettingsService.this.mStrongAuth.noLongerRequireStrongAuth(64, -1);
            return true;
        }

        public int armRebootEscrow() {
            return com.android.server.locksettings.LockSettingsService.this.mRebootEscrowManager.armRebootEscrowIfNeeded();
        }

        public void refreshStrongAuthTimeout(int userId) {
            com.android.server.locksettings.LockSettingsService.this.mStrongAuth.refreshStrongAuthTimeout(userId);
        }

        public void registerLockSettingsStateListener(com.android.internal.widget.LockSettingsStateListener listener) {
            java.util.Objects.requireNonNull(listener, "listener cannot be null");
            com.android.server.locksettings.LockSettingsService.this.mLockSettingsStateListeners.add(listener);
        }

        public void unregisterLockSettingsStateListener(com.android.internal.widget.LockSettingsStateListener listener) {
            com.android.server.locksettings.LockSettingsService.this.mLockSettingsStateListeners.remove(listener);
        }
    }

    private class RebootEscrowCallbacks implements com.android.server.locksettings.RebootEscrowManager.Callbacks {
        private RebootEscrowCallbacks() {
        }

        @Override // com.android.server.locksettings.RebootEscrowManager.Callbacks
        public boolean isUserSecure(int userId) {
            return com.android.server.locksettings.LockSettingsService.this.isUserSecure(userId);
        }

        @Override // com.android.server.locksettings.RebootEscrowManager.Callbacks
        public void onRebootEscrowRestored(byte spVersion, byte[] rawSyntheticPassword, int userId) {
            com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword sp = new com.android.server.locksettings.SyntheticPasswordManager.SyntheticPassword(spVersion);
            sp.recreateDirectly(rawSyntheticPassword);
            synchronized (com.android.server.locksettings.LockSettingsService.this.mSpManager) {
                com.android.server.locksettings.LockSettingsService.this.mSpManager.verifyChallenge(com.android.server.locksettings.LockSettingsService.this.getGateKeeperService(), sp, 0L, userId);
            }
            com.android.server.utils.Slogf.i(com.android.server.locksettings.LockSettingsService.TAG, "Restored synthetic password for user %d using reboot escrow", java.lang.Integer.valueOf(userId));
            com.android.server.locksettings.LockSettingsService.this.onCredentialVerified(sp, com.android.server.locksettings.LockSettingsService.this.loadPasswordMetrics(sp, userId), userId);
        }
    }

    public com.android.server.locksettings.ILockSettingsServiceWrapper getWrapper() {
        return this.mLockSettingsServiceWrapper;
    }

    private class LockSettingsServiceWrapper implements com.android.server.locksettings.ILockSettingsServiceWrapper {
        private LockSettingsServiceWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.locksettings.ILockSettingsServiceExt getExtImpl() {
            return com.android.server.locksettings.LockSettingsService.this.mLockSettingsServiceExt;
        }

        @Override // com.android.server.locksettings.ILockSettingsServiceWrapper
        public boolean isSyntheticPasswordBasedCredentialLocked(int userId) {
            long protectorId = com.android.server.locksettings.LockSettingsService.this.getCurrentLskfBasedProtectorId(userId);
            return protectorId != 0;
        }

        @Override // com.android.server.locksettings.ILockSettingsServiceWrapper
        public android.service.gatekeeper.IGateKeeperService getGateKeeperService() {
            return com.android.server.locksettings.LockSettingsService.this.getGateKeeperService();
        }

        @Override // com.android.server.locksettings.ILockSettingsServiceWrapper
        public boolean hasUnifiedChallenge(int userId) {
            return com.android.server.locksettings.LockSettingsService.this.hasUnifiedChallenge(userId);
        }

        @Override // com.android.server.locksettings.ILockSettingsServiceWrapper
        public boolean migrateProfileLockKeys() {
            return com.android.server.locksettings.LockSettingsService.this.migrateProfileLockKeys();
        }

        @Override // com.android.server.locksettings.ILockSettingsServiceWrapper
        public com.android.server.locksettings.SyntheticPasswordManager getSpManager() {
            return com.android.server.locksettings.LockSettingsService.this.mSpManager;
        }

        @Override // com.android.server.locksettings.ILockSettingsServiceWrapper
        public boolean unlockUserWithToken(long tokenHandle, byte[] token, int userId) {
            return com.android.server.locksettings.LockSettingsService.this.unlockUserWithToken(tokenHandle, token, userId);
        }
    }
}
