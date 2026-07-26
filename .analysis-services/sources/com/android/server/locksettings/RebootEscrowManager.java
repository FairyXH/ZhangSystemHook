package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class RebootEscrowManager {
    private static final int BOOT_COUNT_TOLERANCE = 5;
    private static final int DEFAULT_LOAD_ESCROW_BASE_TIMEOUT_MILLIS = 180000;
    private static final int DEFAULT_LOAD_ESCROW_DATA_RETRY_COUNT = 3;
    private static final int DEFAULT_LOAD_ESCROW_DATA_RETRY_INTERVAL_SECONDS = 30;
    private static final int DEFAULT_LOAD_ESCROW_TIMEOUT_EXTENSION_MILLIS = 5000;
    static final int ERROR_KEYSTORE_FAILURE = 7;
    static final int ERROR_LOAD_ESCROW_KEY = 3;
    static final int ERROR_NONE = 0;
    static final int ERROR_NO_NETWORK = 8;
    static final int ERROR_NO_PROVIDER = 2;
    static final int ERROR_NO_REBOOT_ESCROW_DATA = 10;
    static final int ERROR_PROVIDER_MISMATCH = 6;
    static final int ERROR_RETRY_COUNT_EXHAUSTED = 4;
    static final int ERROR_TIMEOUT_EXHAUSTED = 9;
    static final int ERROR_UNKNOWN = 1;
    static final int ERROR_UNLOCK_ALL_USERS = 5;
    static final java.lang.String OTHER_VBMETA_DIGEST_PROP_NAME = "ota.other.vbmeta_digest";
    public static final java.lang.String REBOOT_ESCROW_ARMED_KEY = "reboot_escrow_armed_count";
    static final java.lang.String REBOOT_ESCROW_KEY_ARMED_TIMESTAMP = "reboot_escrow_key_stored_timestamp";
    static final java.lang.String REBOOT_ESCROW_KEY_OTHER_VBMETA_DIGEST = "reboot_escrow_key_other_vbmeta_digest";
    static final java.lang.String REBOOT_ESCROW_KEY_PROVIDER = "reboot_escrow_key_provider";
    static final java.lang.String REBOOT_ESCROW_KEY_VBMETA_DIGEST = "reboot_escrow_key_vbmeta_digest";
    private static final java.lang.String TAG = "RebootEscrowManager";
    static final java.lang.String VBMETA_DIGEST_PROP_NAME = "ro.boot.vbmeta.digest";
    private final com.android.server.locksettings.RebootEscrowManager.Callbacks mCallbacks;
    private final com.android.server.locksettings.RebootEscrowManager.RebootEscrowEventLog mEventLog;
    private final android.os.Handler mHandler;
    private final com.android.server.locksettings.RebootEscrowManager.Injector mInjector;
    private final java.lang.Object mKeyGenerationLock;
    private final com.android.server.locksettings.RebootEscrowKeyStoreManager mKeyStoreManager;
    private int mLoadEscrowDataErrorCode;
    private boolean mLoadEscrowDataWithRetry;
    private android.net.ConnectivityManager.NetworkCallback mNetworkCallback;
    private com.android.server.locksettings.RebootEscrowKey mPendingRebootEscrowKey;
    private com.android.internal.widget.RebootEscrowListener mRebootEscrowListener;
    private boolean mRebootEscrowReady;
    private boolean mRebootEscrowTimedOut;
    private boolean mRebootEscrowWanted;
    private final com.android.server.locksettings.LockSettingsStorage mStorage;
    private final android.os.UserManager mUserManager;
    android.os.PowerManager.WakeLock mWakeLock;

    interface Callbacks {
        boolean isUserSecure(int i);

        void onRebootEscrowRestored(byte b, byte[] bArr, int i);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface RebootEscrowErrorCode {
    }

    static class Injector {
        protected android.content.Context mContext;
        private final com.android.server.locksettings.RebootEscrowKeyStoreManager mKeyStoreManager = new com.android.server.locksettings.RebootEscrowKeyStoreManager();
        private com.android.server.locksettings.RebootEscrowProviderInterface mRebootEscrowProvider;
        private final com.android.server.locksettings.LockSettingsStorage mStorage;
        private final com.android.server.pm.UserManagerInternal mUserManagerInternal;

        Injector(android.content.Context context, com.android.server.locksettings.LockSettingsStorage storage, com.android.server.pm.UserManagerInternal userManagerInternal) {
            this.mContext = context;
            this.mStorage = storage;
            this.mUserManagerInternal = userManagerInternal;
        }

        private com.android.server.locksettings.RebootEscrowProviderInterface createRebootEscrowProvider() {
            com.android.server.locksettings.RebootEscrowProviderInterface rebootEscrowProvider;
            if (serverBasedResumeOnReboot()) {
                android.util.Slog.i(com.android.server.locksettings.RebootEscrowManager.TAG, "Using server based resume on reboot");
                rebootEscrowProvider = new com.android.server.locksettings.RebootEscrowProviderServerBasedImpl(this.mContext, this.mStorage);
            } else {
                android.util.Slog.i(com.android.server.locksettings.RebootEscrowManager.TAG, "Using HAL based resume on reboot");
                rebootEscrowProvider = new com.android.server.locksettings.RebootEscrowProviderHalImpl();
            }
            if (rebootEscrowProvider.hasRebootEscrowSupport()) {
                return rebootEscrowProvider;
            }
            return null;
        }

        void post(android.os.Handler handler, java.lang.Runnable runnable) {
            handler.post(runnable);
        }

        void postDelayed(android.os.Handler handler, java.lang.Runnable runnable, long delayMillis) {
            handler.postDelayed(runnable, delayMillis);
        }

        public boolean serverBasedResumeOnReboot() {
            if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.reboot_escrow")) {
                return true;
            }
            return android.provider.DeviceConfig.getBoolean("ota", "server_based_ror_enabled", false);
        }

        public boolean waitForInternet() {
            return android.provider.DeviceConfig.getBoolean("ota", "wait_for_internet_ror", false);
        }

        public boolean isNetworkConnected() {
            android.net.ConnectivityManager connectivityManager = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
            if (connectivityManager == null) {
                return false;
            }
            android.net.Network activeNetwork = connectivityManager.getActiveNetwork();
            android.net.NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            return networkCapabilities != null && networkCapabilities.hasCapability(12) && networkCapabilities.hasCapability(16);
        }

        public boolean requestNetworkWithInternet(android.net.ConnectivityManager.NetworkCallback networkCallback) {
            android.net.ConnectivityManager connectivityManager = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
            if (connectivityManager == null) {
                return false;
            }
            android.net.NetworkRequest request = new android.net.NetworkRequest.Builder().addCapability(12).build();
            connectivityManager.requestNetwork(request, networkCallback, getLoadEscrowTimeoutMillis());
            return true;
        }

        public void stopRequestingNetwork(android.net.ConnectivityManager.NetworkCallback networkCallback) {
            android.net.ConnectivityManager connectivityManager = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
            if (connectivityManager == null) {
                return;
            }
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }

        public android.content.Context getContext() {
            return this.mContext;
        }

        public android.os.UserManager getUserManager() {
            return (android.os.UserManager) this.mContext.getSystemService("user");
        }

        public com.android.server.pm.UserManagerInternal getUserManagerInternal() {
            return this.mUserManagerInternal;
        }

        public com.android.server.locksettings.RebootEscrowKeyStoreManager getKeyStoreManager() {
            return this.mKeyStoreManager;
        }

        public com.android.server.locksettings.RebootEscrowProviderInterface createRebootEscrowProviderIfNeeded() {
            if (this.mRebootEscrowProvider == null) {
                this.mRebootEscrowProvider = createRebootEscrowProvider();
            }
            return this.mRebootEscrowProvider;
        }

        android.os.PowerManager.WakeLock getWakeLock() {
            android.os.PowerManager pm = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
            return pm.newWakeLock(1, com.android.server.locksettings.RebootEscrowManager.TAG);
        }

        public com.android.server.locksettings.RebootEscrowProviderInterface getRebootEscrowProvider() {
            return this.mRebootEscrowProvider;
        }

        public void clearRebootEscrowProvider() {
            this.mRebootEscrowProvider = null;
        }

        public int getBootCount() {
            return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "boot_count", 0);
        }

        public long getCurrentTimeMillis() {
            return java.lang.System.currentTimeMillis();
        }

        public int getLoadEscrowDataRetryLimit() {
            return android.provider.DeviceConfig.getInt("ota", "load_escrow_data_retry_count", 3);
        }

        public int getLoadEscrowDataRetryIntervalSeconds() {
            return android.provider.DeviceConfig.getInt("ota", "load_escrow_data_retry_interval_seconds", 30);
        }

        public int getLoadEscrowTimeoutMillis() {
            return com.android.server.locksettings.RebootEscrowManager.DEFAULT_LOAD_ESCROW_BASE_TIMEOUT_MILLIS;
        }

        public int getWakeLockTimeoutMillis() {
            return getLoadEscrowTimeoutMillis() + 5000;
        }

        public void reportMetric(boolean success, int errorCode, int serviceType, int attemptCount, int escrowDurationInSeconds, int vbmetaDigestStatus, int durationSinceBootCompleteInSeconds) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.REBOOT_ESCROW_RECOVERY_REPORTED, success, errorCode, serviceType, attemptCount, escrowDurationInSeconds, vbmetaDigestStatus, durationSinceBootCompleteInSeconds);
        }

        public com.android.server.locksettings.RebootEscrowManager.RebootEscrowEventLog getEventLog() {
            return new com.android.server.locksettings.RebootEscrowManager.RebootEscrowEventLog();
        }

        public java.lang.String getVbmetaDigest(boolean other) {
            return other ? android.os.SystemProperties.get(com.android.server.locksettings.RebootEscrowManager.OTHER_VBMETA_DIGEST_PROP_NAME) : android.os.SystemProperties.get(com.android.server.locksettings.RebootEscrowManager.VBMETA_DIGEST_PROP_NAME);
        }
    }

    RebootEscrowManager(android.content.Context context, com.android.server.locksettings.RebootEscrowManager.Callbacks callbacks, com.android.server.locksettings.LockSettingsStorage storage, android.os.Handler handler, com.android.server.pm.UserManagerInternal userManagerInternal) {
        this(new com.android.server.locksettings.RebootEscrowManager.Injector(context, storage, userManagerInternal), callbacks, storage, handler);
    }

    RebootEscrowManager(com.android.server.locksettings.RebootEscrowManager.Injector injector, com.android.server.locksettings.RebootEscrowManager.Callbacks callbacks, com.android.server.locksettings.LockSettingsStorage storage, android.os.Handler handler) {
        this.mLoadEscrowDataErrorCode = 0;
        this.mRebootEscrowTimedOut = false;
        this.mLoadEscrowDataWithRetry = false;
        this.mKeyGenerationLock = new java.lang.Object();
        this.mInjector = injector;
        this.mCallbacks = callbacks;
        this.mStorage = storage;
        this.mUserManager = injector.getUserManager();
        this.mEventLog = injector.getEventLog();
        this.mKeyStoreManager = injector.getKeyStoreManager();
        this.mHandler = handler;
    }

    private void setLoadEscrowDataErrorCode(final int value, android.os.Handler handler) {
        if (this.mInjector.waitForInternet()) {
            this.mInjector.post(handler, new java.lang.Runnable() { // from class: com.android.server.locksettings.RebootEscrowManager$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setLoadEscrowDataErrorCode$0(value);
                }
            });
        } else {
            this.mLoadEscrowDataErrorCode = value;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLoadEscrowDataErrorCode$0(int value) {
        this.mLoadEscrowDataErrorCode = value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void compareAndSetLoadEscrowDataErrorCode(int expectedValue, int newValue, android.os.Handler handler) {
        if (expectedValue == this.mLoadEscrowDataErrorCode) {
            setLoadEscrowDataErrorCode(newValue, handler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGetRebootEscrowKeyFailed(java.util.List<android.content.pm.UserInfo> users, int attemptCount, android.os.Handler retryHandler) {
        android.util.Slog.w(TAG, "Had reboot escrow data for users, but no key; removing escrow storage.");
        for (android.content.pm.UserInfo user : users) {
            this.mStorage.removeRebootEscrow(user.id);
        }
        onEscrowRestoreComplete(false, attemptCount, retryHandler);
    }

    private java.util.List<android.content.pm.UserInfo> getUsersToUnlock(java.util.List<android.content.pm.UserInfo> users) {
        if (this.mCallbacks.isUserSecure(0) && !this.mStorage.hasRebootEscrow(0)) {
            android.util.Slog.i(TAG, "No reboot escrow data found for system user");
            return java.util.Collections.emptyList();
        }
        java.util.Set<java.lang.Integer> noEscrowDataUsers = new java.util.HashSet<>();
        for (android.content.pm.UserInfo user : users) {
            if (this.mCallbacks.isUserSecure(user.id) && !this.mStorage.hasRebootEscrow(user.id)) {
                android.util.Slog.d(TAG, "No reboot escrow data found for user " + user);
                noEscrowDataUsers.add(java.lang.Integer.valueOf(user.id));
            }
        }
        java.util.List<android.content.pm.UserInfo> rebootEscrowUsers = new java.util.ArrayList<>();
        for (android.content.pm.UserInfo user2 : users) {
            if (this.mCallbacks.isUserSecure(user2.id)) {
                int userId = user2.id;
                if (!noEscrowDataUsers.contains(java.lang.Integer.valueOf(userId)) && !noEscrowDataUsers.contains(java.lang.Integer.valueOf(this.mInjector.getUserManagerInternal().getProfileParentId(userId)))) {
                    rebootEscrowUsers.add(user2);
                }
            }
        }
        return rebootEscrowUsers;
    }

    void loadRebootEscrowDataIfAvailable(final android.os.Handler retryHandler) {
        final java.util.List users = this.mUserManager.getUsers();
        final java.util.List<android.content.pm.UserInfo> rebootEscrowUsers = getUsersToUnlock(users);
        if (rebootEscrowUsers.isEmpty()) {
            android.util.Slog.i(TAG, "No reboot escrow data found for users, skipping loading escrow data");
            setLoadEscrowDataErrorCode(10, retryHandler);
            reportMetricOnRestoreComplete(false, 1, retryHandler);
            clearMetricsStorage();
            return;
        }
        this.mWakeLock = this.mInjector.getWakeLock();
        if (this.mWakeLock != null) {
            this.mWakeLock.setReferenceCounted(false);
            this.mWakeLock.acquire(this.mInjector.getWakeLockTimeoutMillis());
        }
        if (this.mInjector.waitForInternet()) {
            this.mInjector.postDelayed(retryHandler, new java.lang.Runnable() { // from class: com.android.server.locksettings.RebootEscrowManager$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$loadRebootEscrowDataIfAvailable$1();
                }
            }, this.mInjector.getLoadEscrowTimeoutMillis());
            this.mInjector.post(retryHandler, new java.lang.Runnable() { // from class: com.android.server.locksettings.RebootEscrowManager$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$loadRebootEscrowDataIfAvailable$2(retryHandler, users, rebootEscrowUsers);
                }
            });
        } else {
            this.mInjector.post(retryHandler, new java.lang.Runnable() { // from class: com.android.server.locksettings.RebootEscrowManager$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$loadRebootEscrowDataIfAvailable$3(retryHandler, users, rebootEscrowUsers);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRebootEscrowDataIfAvailable$1() {
        this.mRebootEscrowTimedOut = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRebootEscrowDataIfAvailable$3(android.os.Handler retryHandler, java.util.List users, java.util.List rebootEscrowUsers) {
        lambda$scheduleLoadRebootEscrowDataOrFail$4(retryHandler, 0, users, rebootEscrowUsers);
    }

    void scheduleLoadRebootEscrowDataOrFail(final android.os.Handler retryHandler, final int attemptNumber, final java.util.List<android.content.pm.UserInfo> users, final java.util.List<android.content.pm.UserInfo> rebootEscrowUsers) {
        java.util.Objects.requireNonNull(retryHandler);
        int retryLimit = this.mInjector.getLoadEscrowDataRetryLimit();
        int retryIntervalInSeconds = this.mInjector.getLoadEscrowDataRetryIntervalSeconds();
        if (attemptNumber < retryLimit && !this.mRebootEscrowTimedOut) {
            android.util.Slog.i(TAG, "Scheduling loadRebootEscrowData retry number: " + attemptNumber);
            this.mInjector.postDelayed(retryHandler, new java.lang.Runnable() { // from class: com.android.server.locksettings.RebootEscrowManager$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleLoadRebootEscrowDataOrFail$4(retryHandler, attemptNumber, users, rebootEscrowUsers);
                }
            }, retryIntervalInSeconds * 1000);
            return;
        }
        if (!this.mInjector.waitForInternet()) {
            android.util.Slog.w(TAG, "Failed to load reboot escrow data after " + attemptNumber + " attempts");
            if (this.mInjector.serverBasedResumeOnReboot() && !this.mInjector.isNetworkConnected()) {
                this.mLoadEscrowDataErrorCode = 8;
            } else {
                this.mLoadEscrowDataErrorCode = 4;
            }
            onGetRebootEscrowKeyFailed(users, attemptNumber, retryHandler);
            return;
        }
        if (this.mRebootEscrowTimedOut) {
            android.util.Slog.w(TAG, "Failed to load reboot escrow data within timeout");
            compareAndSetLoadEscrowDataErrorCode(0, 9, retryHandler);
        } else {
            android.util.Slog.w(TAG, "Failed to load reboot escrow data after " + attemptNumber + " attempts");
            compareAndSetLoadEscrowDataErrorCode(0, 4, retryHandler);
        }
        onGetRebootEscrowKeyFailed(users, attemptNumber, retryHandler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: loadRebootEscrowDataOnInternet, reason: merged with bridge method [inline-methods] */
    public void lambda$loadRebootEscrowDataIfAvailable$2(final android.os.Handler retryHandler, final java.util.List<android.content.pm.UserInfo> users, final java.util.List<android.content.pm.UserInfo> rebootEscrowUsers) {
        if (!this.mInjector.serverBasedResumeOnReboot()) {
            lambda$scheduleLoadRebootEscrowDataOrFail$4(retryHandler, 0, users, rebootEscrowUsers);
            return;
        }
        this.mNetworkCallback = new android.net.ConnectivityManager.NetworkCallback() { // from class: com.android.server.locksettings.RebootEscrowManager.1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(android.net.Network network) {
                com.android.server.locksettings.RebootEscrowManager.this.compareAndSetLoadEscrowDataErrorCode(8, 0, retryHandler);
                if (!com.android.server.locksettings.RebootEscrowManager.this.mLoadEscrowDataWithRetry) {
                    com.android.server.locksettings.RebootEscrowManager.this.mLoadEscrowDataWithRetry = true;
                    com.android.server.locksettings.RebootEscrowManager.this.lambda$scheduleLoadRebootEscrowDataOrFail$4(retryHandler, 0, users, rebootEscrowUsers);
                }
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onUnavailable() {
                android.util.Slog.w(com.android.server.locksettings.RebootEscrowManager.TAG, "Failed to connect to network within timeout");
                com.android.server.locksettings.RebootEscrowManager.this.compareAndSetLoadEscrowDataErrorCode(0, 8, retryHandler);
                com.android.server.locksettings.RebootEscrowManager.this.onGetRebootEscrowKeyFailed(users, 0, retryHandler);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(android.net.Network lostNetwork) {
                android.util.Slog.w(com.android.server.locksettings.RebootEscrowManager.TAG, "Network lost, still attempting to load escrow key.");
                com.android.server.locksettings.RebootEscrowManager.this.compareAndSetLoadEscrowDataErrorCode(0, 8, retryHandler);
            }
        };
        boolean success = this.mInjector.requestNetworkWithInternet(this.mNetworkCallback);
        if (!success) {
            lambda$scheduleLoadRebootEscrowDataOrFail$4(retryHandler, 0, users, rebootEscrowUsers);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: loadRebootEscrowDataWithRetry, reason: merged with bridge method [inline-methods] */
    public void lambda$scheduleLoadRebootEscrowDataOrFail$4(android.os.Handler retryHandler, int attemptNumber, java.util.List<android.content.pm.UserInfo> users, java.util.List<android.content.pm.UserInfo> rebootEscrowUsers) {
        javax.crypto.SecretKey kk = this.mKeyStoreManager.getKeyStoreEncryptionKey();
        if (kk == null) {
            android.util.Slog.i(TAG, "Failed to load the key for resume on reboot from key store.");
        }
        try {
            com.android.server.locksettings.RebootEscrowKey escrowKey = getAndClearRebootEscrowKey(kk, retryHandler);
            if (escrowKey == null) {
                if (this.mLoadEscrowDataErrorCode == 0) {
                    int providerType = this.mInjector.serverBasedResumeOnReboot() ? 1 : 0;
                    if (providerType != this.mStorage.getInt(REBOOT_ESCROW_KEY_PROVIDER, -1, 0)) {
                        setLoadEscrowDataErrorCode(6, retryHandler);
                    } else {
                        setLoadEscrowDataErrorCode(3, retryHandler);
                    }
                }
                onGetRebootEscrowKeyFailed(users, attemptNumber + 1, retryHandler);
                return;
            }
            this.mEventLog.addEntry(1);
            boolean allUsersUnlocked = true;
            for (android.content.pm.UserInfo user : rebootEscrowUsers) {
                allUsersUnlocked &= restoreRebootEscrowForUser(user.id, escrowKey, kk);
            }
            if (!allUsersUnlocked) {
                compareAndSetLoadEscrowDataErrorCode(0, 5, retryHandler);
            }
            onEscrowRestoreComplete(allUsersUnlocked, attemptNumber + 1, retryHandler);
        } catch (java.io.IOException e) {
            android.util.Slog.i(TAG, "Failed to load escrow key, scheduling retry.", e);
            scheduleLoadRebootEscrowDataOrFail(retryHandler, attemptNumber + 1, users, rebootEscrowUsers);
        }
    }

    private void clearMetricsStorage() {
        this.mStorage.removeKey(REBOOT_ESCROW_ARMED_KEY, 0);
        this.mStorage.removeKey(REBOOT_ESCROW_KEY_ARMED_TIMESTAMP, 0);
        this.mStorage.removeKey(REBOOT_ESCROW_KEY_VBMETA_DIGEST, 0);
        this.mStorage.removeKey(REBOOT_ESCROW_KEY_OTHER_VBMETA_DIGEST, 0);
        this.mStorage.removeKey(REBOOT_ESCROW_KEY_PROVIDER, 0);
    }

    private int getVbmetaDigestStatusOnRestoreComplete() {
        java.lang.String currentVbmetaDigest = this.mInjector.getVbmetaDigest(false);
        java.lang.String vbmetaDigestStored = this.mStorage.getString(REBOOT_ESCROW_KEY_VBMETA_DIGEST, "", 0);
        java.lang.String vbmetaDigestOtherStored = this.mStorage.getString(REBOOT_ESCROW_KEY_OTHER_VBMETA_DIGEST, "", 0);
        if (vbmetaDigestOtherStored.isEmpty()) {
            return currentVbmetaDigest.equals(vbmetaDigestStored) ? 0 : 2;
        }
        if (currentVbmetaDigest.equals(vbmetaDigestOtherStored)) {
            return 0;
        }
        return currentVbmetaDigest.equals(vbmetaDigestStored) ? 1 : 2;
    }

    private void reportMetricOnRestoreComplete(boolean success, int attemptCount, android.os.Handler retryHandler) {
        int i;
        int escrowDurationInSeconds;
        if (this.mInjector.serverBasedResumeOnReboot()) {
            i = 2;
        } else {
            i = 1;
        }
        int serviceType = i;
        long armedTimestamp = this.mStorage.getLong(REBOOT_ESCROW_KEY_ARMED_TIMESTAMP, -1L, 0);
        long currentTimeStamp = this.mInjector.getCurrentTimeMillis();
        if (armedTimestamp != -1 && currentTimeStamp > armedTimestamp) {
            int escrowDurationInSeconds2 = ((int) (currentTimeStamp - armedTimestamp)) / 1000;
            escrowDurationInSeconds = escrowDurationInSeconds2;
        } else {
            escrowDurationInSeconds = -1;
        }
        int vbmetaDigestStatus = getVbmetaDigestStatusOnRestoreComplete();
        if (!success) {
            compareAndSetLoadEscrowDataErrorCode(0, 1, retryHandler);
        }
        android.util.Slog.i(TAG, "Reporting RoR recovery metrics, success: " + success + ", service type: " + serviceType + ", error code: " + this.mLoadEscrowDataErrorCode);
        this.mInjector.reportMetric(success, this.mLoadEscrowDataErrorCode, serviceType, attemptCount, escrowDurationInSeconds, vbmetaDigestStatus, -1);
        setLoadEscrowDataErrorCode(0, retryHandler);
    }

    private void onEscrowRestoreComplete(boolean success, int attemptCount, android.os.Handler retryHandler) {
        int previousBootCount = this.mStorage.getInt(REBOOT_ESCROW_ARMED_KEY, -1, 0);
        int bootCountDelta = this.mInjector.getBootCount() - previousBootCount;
        if (success || (previousBootCount != -1 && bootCountDelta <= 5)) {
            reportMetricOnRestoreComplete(success, attemptCount, retryHandler);
        }
        this.mKeyStoreManager.clearKeyStoreEncryptionKey();
        this.mInjector.clearRebootEscrowProvider();
        clearMetricsStorage();
        if (this.mNetworkCallback != null) {
            this.mInjector.stopRequestingNetwork(this.mNetworkCallback);
        }
        if (this.mWakeLock != null) {
            this.mWakeLock.release();
        }
    }

    private com.android.server.locksettings.RebootEscrowKey getAndClearRebootEscrowKey(javax.crypto.SecretKey kk, android.os.Handler retryHandler) throws java.io.IOException {
        com.android.server.locksettings.RebootEscrowProviderInterface rebootEscrowProvider = this.mInjector.createRebootEscrowProviderIfNeeded();
        if (rebootEscrowProvider == null) {
            android.util.Slog.w(TAG, "Had reboot escrow data for users, but RebootEscrowProvider is unavailable");
            setLoadEscrowDataErrorCode(2, retryHandler);
            return null;
        }
        if (rebootEscrowProvider.getType() == 1 && kk == null) {
            setLoadEscrowDataErrorCode(7, retryHandler);
            return null;
        }
        com.android.server.locksettings.RebootEscrowKey key = rebootEscrowProvider.getAndClearRebootEscrowKey(kk);
        if (key != null) {
            this.mEventLog.addEntry(4);
        }
        return key;
    }

    private boolean restoreRebootEscrowForUser(int userId, com.android.server.locksettings.RebootEscrowKey ks, javax.crypto.SecretKey kk) {
        if (!this.mStorage.hasRebootEscrow(userId)) {
            return false;
        }
        try {
            byte[] blob = this.mStorage.readRebootEscrow(userId);
            this.mStorage.removeRebootEscrow(userId);
            com.android.server.locksettings.RebootEscrowData escrowData = com.android.server.locksettings.RebootEscrowData.fromEncryptedData(ks, blob, kk);
            this.mCallbacks.onRebootEscrowRestored(escrowData.getSpVersion(), escrowData.getSyntheticPassword(), userId);
            android.util.Slog.i(TAG, "Restored reboot escrow data for user " + userId);
            this.mEventLog.addEntry(7, userId);
            return true;
        } catch (java.io.IOException e) {
            android.util.Slog.w(TAG, "Could not load reboot escrow data for user " + userId, e);
            return false;
        }
    }

    void callToRebootEscrowIfNeeded(int userId, byte spVersion, byte[] syntheticPassword) {
        if (!this.mRebootEscrowWanted) {
            return;
        }
        if (this.mInjector.createRebootEscrowProviderIfNeeded() == null) {
            android.util.Slog.w(TAG, "Not storing escrow data, RebootEscrowProvider is unavailable");
            return;
        }
        com.android.server.locksettings.RebootEscrowKey escrowKey = generateEscrowKeyIfNeeded();
        if (escrowKey == null) {
            android.util.Slog.e(TAG, "Could not generate escrow key");
            return;
        }
        javax.crypto.SecretKey kk = this.mKeyStoreManager.generateKeyStoreEncryptionKeyIfNeeded();
        if (kk == null) {
            android.util.Slog.e(TAG, "Failed to generate encryption key from keystore.");
            return;
        }
        try {
            com.android.server.locksettings.RebootEscrowData escrowData = com.android.server.locksettings.RebootEscrowData.fromSyntheticPassword(escrowKey, spVersion, syntheticPassword, kk);
            this.mStorage.writeRebootEscrow(userId, escrowData.getBlob());
            this.mEventLog.addEntry(6, userId);
            setRebootEscrowReady(true);
        } catch (java.io.IOException e) {
            setRebootEscrowReady(false);
            android.util.Slog.w(TAG, "Could not escrow reboot data", e);
        }
    }

    private com.android.server.locksettings.RebootEscrowKey generateEscrowKeyIfNeeded() {
        synchronized (this.mKeyGenerationLock) {
            if (this.mPendingRebootEscrowKey != null) {
                return this.mPendingRebootEscrowKey;
            }
            try {
                com.android.server.locksettings.RebootEscrowKey key = com.android.server.locksettings.RebootEscrowKey.generate();
                this.mPendingRebootEscrowKey = key;
                return key;
            } catch (java.io.IOException e) {
                android.util.Slog.w(TAG, "Could not generate reboot escrow key");
                return null;
            }
        }
    }

    private void clearRebootEscrowIfNeeded() {
        this.mRebootEscrowWanted = false;
        setRebootEscrowReady(false);
        com.android.server.locksettings.RebootEscrowProviderInterface rebootEscrowProvider = this.mInjector.createRebootEscrowProviderIfNeeded();
        if (rebootEscrowProvider == null) {
            android.util.Slog.w(TAG, "RebootEscrowProvider is unavailable for clear request");
        } else {
            rebootEscrowProvider.clearRebootEscrowKey();
        }
        this.mInjector.clearRebootEscrowProvider();
        clearMetricsStorage();
        java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getUsers();
        for (android.content.pm.UserInfo user : users) {
            this.mStorage.removeRebootEscrow(user.id);
        }
        this.mEventLog.addEntry(3);
    }

    int armRebootEscrowIfNeeded() {
        int expectedProviderType;
        com.android.server.locksettings.RebootEscrowKey escrowKey;
        if (!this.mRebootEscrowReady) {
            return 2;
        }
        com.android.server.locksettings.RebootEscrowProviderInterface rebootEscrowProvider = this.mInjector.getRebootEscrowProvider();
        if (rebootEscrowProvider == null) {
            android.util.Slog.w(TAG, "Not storing escrow key, RebootEscrowProvider is unavailable");
            clearRebootEscrowIfNeeded();
            return 3;
        }
        if (this.mInjector.serverBasedResumeOnReboot()) {
            expectedProviderType = 1;
        } else {
            expectedProviderType = 0;
        }
        int actualProviderType = rebootEscrowProvider.getType();
        if (expectedProviderType != actualProviderType) {
            android.util.Slog.w(TAG, "Expect reboot escrow provider " + expectedProviderType + ", but the RoR is prepared with " + actualProviderType + ". Please prepare the RoR again.");
            clearRebootEscrowIfNeeded();
            return 4;
        }
        synchronized (this.mKeyGenerationLock) {
            escrowKey = this.mPendingRebootEscrowKey;
        }
        if (escrowKey == null) {
            android.util.Slog.e(TAG, "Escrow key is null, but escrow was marked as ready");
            clearRebootEscrowIfNeeded();
            return 5;
        }
        javax.crypto.SecretKey kk = this.mKeyStoreManager.getKeyStoreEncryptionKey();
        if (kk == null) {
            android.util.Slog.e(TAG, "Failed to get encryption key from keystore.");
            clearRebootEscrowIfNeeded();
            return 6;
        }
        boolean armedRebootEscrow = rebootEscrowProvider.storeRebootEscrowKey(escrowKey, kk);
        if (!armedRebootEscrow) {
            return 7;
        }
        this.mStorage.setInt(REBOOT_ESCROW_ARMED_KEY, this.mInjector.getBootCount(), 0);
        this.mStorage.setLong(REBOOT_ESCROW_KEY_ARMED_TIMESTAMP, this.mInjector.getCurrentTimeMillis(), 0);
        this.mStorage.setString(REBOOT_ESCROW_KEY_VBMETA_DIGEST, this.mInjector.getVbmetaDigest(false), 0);
        this.mStorage.setString(REBOOT_ESCROW_KEY_OTHER_VBMETA_DIGEST, this.mInjector.getVbmetaDigest(true), 0);
        this.mStorage.setInt(REBOOT_ESCROW_KEY_PROVIDER, actualProviderType, 0);
        this.mEventLog.addEntry(2);
        return 0;
    }

    private void setRebootEscrowReady(final boolean ready) {
        if (this.mRebootEscrowReady != ready) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.locksettings.RebootEscrowManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setRebootEscrowReady$5(ready);
                }
            });
        }
        this.mRebootEscrowReady = ready;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setRebootEscrowReady$5(boolean ready) {
        this.mRebootEscrowListener.onPreparedForReboot(ready);
    }

    boolean prepareRebootEscrow() {
        clearRebootEscrowIfNeeded();
        if (this.mInjector.createRebootEscrowProviderIfNeeded() == null) {
            android.util.Slog.w(TAG, "No reboot escrow provider, skipping resume on reboot preparation.");
            return false;
        }
        this.mRebootEscrowWanted = true;
        this.mEventLog.addEntry(5);
        return true;
    }

    boolean clearRebootEscrow() {
        clearRebootEscrowIfNeeded();
        return true;
    }

    void setRebootEscrowListener(com.android.internal.widget.RebootEscrowListener listener) {
        this.mRebootEscrowListener = listener;
    }

    public static class RebootEscrowEvent {
        static final int CLEARED_LSKF_REQUEST = 3;
        static final int FOUND_ESCROW_DATA = 1;
        static final int REQUESTED_LSKF = 5;
        static final int RETRIEVED_LSKF_FOR_USER = 7;
        static final int RETRIEVED_STORED_KEK = 4;
        static final int SET_ARMED_STATUS = 2;
        static final int STORED_LSKF_FOR_USER = 6;
        final int mEventId;
        final long mTimestamp;
        final java.lang.Integer mUserId;
        final long mWallTime;

        RebootEscrowEvent(int eventId) {
            this(eventId, null);
        }

        RebootEscrowEvent(int eventId, java.lang.Integer userId) {
            this.mEventId = eventId;
            this.mUserId = userId;
            this.mTimestamp = android.os.SystemClock.uptimeMillis();
            this.mWallTime = java.lang.System.currentTimeMillis();
        }

        java.lang.String getEventDescription() {
            switch (this.mEventId) {
                case 1:
                    return "Found escrow data";
                case 2:
                    return "Set armed status";
                case 3:
                    return "Cleared request for LSKF";
                case 4:
                    return "Retrieved stored KEK";
                case 5:
                    return "Requested LSKF";
                case 6:
                    return "Stored LSKF for user";
                case 7:
                    return "Retrieved LSKF for user";
                default:
                    return "Unknown event ID " + this.mEventId;
            }
        }
    }

    public static class RebootEscrowEventLog {
        private com.android.server.locksettings.RebootEscrowManager.RebootEscrowEvent[] mEntries = new com.android.server.locksettings.RebootEscrowManager.RebootEscrowEvent[16];
        private int mNextIndex = 0;

        void addEntry(int eventId) {
            addEntryInternal(new com.android.server.locksettings.RebootEscrowManager.RebootEscrowEvent(eventId));
        }

        void addEntry(int eventId, int userId) {
            addEntryInternal(new com.android.server.locksettings.RebootEscrowManager.RebootEscrowEvent(eventId, java.lang.Integer.valueOf(userId)));
        }

        private void addEntryInternal(com.android.server.locksettings.RebootEscrowManager.RebootEscrowEvent event) {
            int index = this.mNextIndex;
            this.mEntries[index] = event;
            this.mNextIndex = (this.mNextIndex + 1) % this.mEntries.length;
        }

        void dump(com.android.internal.util.IndentingPrintWriter pw) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", java.util.Locale.US);
            for (int i = 0; i < this.mEntries.length; i++) {
                com.android.server.locksettings.RebootEscrowManager.RebootEscrowEvent event = this.mEntries[(this.mNextIndex + i) % this.mEntries.length];
                if (event != null) {
                    pw.print("Event #");
                    pw.println(i);
                    pw.println(" time=" + sdf.format(new java.util.Date(event.mWallTime)) + " (timestamp=" + event.mTimestamp + ")");
                    pw.print(" event=");
                    pw.println(event.getEventDescription());
                    if (event.mUserId != null) {
                        pw.print(" user=");
                        pw.println(event.mUserId);
                    }
                }
            }
        }
    }

    void dump(com.android.internal.util.IndentingPrintWriter pw) {
        boolean keySet;
        pw.print("mRebootEscrowWanted=");
        pw.println(this.mRebootEscrowWanted);
        pw.print("mRebootEscrowReady=");
        pw.println(this.mRebootEscrowReady);
        pw.print("mRebootEscrowListener=");
        pw.println(this.mRebootEscrowListener);
        pw.print("mLoadEscrowDataErrorCode=");
        pw.println(this.mLoadEscrowDataErrorCode);
        synchronized (this.mKeyGenerationLock) {
            keySet = this.mPendingRebootEscrowKey != null;
        }
        pw.print("mPendingRebootEscrowKey is ");
        pw.println(keySet ? "set" : "not set");
        com.android.server.locksettings.RebootEscrowProviderInterface provider = this.mInjector.getRebootEscrowProvider();
        java.lang.String providerType = provider == null ? "null" : java.lang.String.valueOf(provider.getType());
        pw.print("RebootEscrowProvider type is " + providerType);
        pw.println();
        pw.println("Event log:");
        pw.increaseIndent();
        this.mEventLog.dump(pw);
        pw.println();
        pw.decreaseIndent();
    }
}
