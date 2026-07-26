package com.android.server.recoverysystem;

/* JADX INFO: loaded from: classes3.dex */
public class RecoverySystemService extends android.os.IRecoverySystem.Stub implements com.android.internal.widget.RebootEscrowListener {
    static final java.lang.String AB_UPDATE = "ro.build.ab_update";
    private static final long APEX_INFO_SIZE_LIMIT = 2457600;
    private static final boolean DEBUG = false;
    static final java.lang.String INIT_SERVICE_CLEAR_BCB = "init.svc.clear-bcb";
    static final java.lang.String INIT_SERVICE_SETUP_BCB = "init.svc.setup-bcb";
    static final java.lang.String INIT_SERVICE_UNCRYPT = "init.svc.uncrypt";
    static final java.lang.String LSKF_CAPTURED_COUNT_PREF = "lskf_captured_count";
    static final java.lang.String LSKF_CAPTURED_TIMESTAMP_PREF = "lskf_captured_timestamp";
    private static final int REBOOT_WATCHDOG_PAUSE_DURATION_MS = 20000;
    static final java.lang.String RECOVERY_WIPE_DATA_COMMAND = "--wipe_data";
    static final java.lang.String REQUEST_LSKF_COUNT_PREF_SUFFIX = "_request_lskf_count";
    static final java.lang.String REQUEST_LSKF_TIMESTAMP_PREF_SUFFIX = "_request_lskf_timestamp";
    private static final int ROR_NEED_PREPARATION = 0;
    private static final int ROR_NOT_REQUESTED = 0;
    private static final int ROR_REQUESTED_NEED_CLEAR = 1;
    private static final int ROR_REQUESTED_SKIP_CLEAR = 2;
    private static final int ROR_SKIP_PREPARATION_AND_NOTIFY = 1;
    private static final int ROR_SKIP_PREPARATION_NOT_NOTIFY = 2;
    private static final int SOCKET_CONNECTION_MAX_RETRY = 30;
    private static final java.lang.String TAG = "RecoverySystemService";
    private static final java.lang.String UNCRYPT_SOCKET = "uncrypt";
    private final android.util.ArrayMap<java.lang.String, android.content.IntentSender> mCallerPendingRequest;
    private final android.util.ArraySet<java.lang.String> mCallerPreparedForReboot;
    private final android.content.Context mContext;
    private final com.android.server.recoverysystem.RecoverySystemService.Injector mInjector;
    private static final java.lang.Object sRequestLock = new java.lang.Object();
    static final android.util.FastImmutableArraySet<java.lang.Integer> FATAL_ARM_ESCROW_ERRORS = new android.util.FastImmutableArraySet<>(new java.lang.Integer[]{2, 3, 4, 5, 6});

    private @interface ResumeOnRebootActionsOnClear {
    }

    private @interface ResumeOnRebootActionsOnRequest {
    }

    static class RebootPreparationError {
        final int mProviderErrorCode;
        final int mRebootErrorCode;

        RebootPreparationError(int rebootErrorCode, int providerErrorCode) {
            this.mRebootErrorCode = rebootErrorCode;
            this.mProviderErrorCode = providerErrorCode;
        }

        int getErrorCodeForMetrics() {
            return this.mRebootErrorCode + this.mProviderErrorCode;
        }
    }

    public static class PreferencesManager {
        private static final java.lang.String METRICS_DIR = "recovery_system";
        private static final java.lang.String METRICS_PREFS_FILE = "RecoverySystemMetricsPrefs.xml";
        private final java.io.File mMetricsPrefsFile;
        protected final android.content.SharedPreferences mSharedPreferences;

        PreferencesManager(android.content.Context context) {
            java.io.File prefsDir = new java.io.File(android.os.Environment.getDataSystemCeDirectory(0), METRICS_DIR);
            this.mMetricsPrefsFile = new java.io.File(prefsDir, METRICS_PREFS_FILE);
            this.mSharedPreferences = context.getSharedPreferences(this.mMetricsPrefsFile, 0);
        }

        public long getLong(java.lang.String key, long defaultValue) {
            return this.mSharedPreferences.getLong(key, defaultValue);
        }

        public int getInt(java.lang.String key, int defaultValue) {
            return this.mSharedPreferences.getInt(key, defaultValue);
        }

        public void putLong(java.lang.String key, long value) {
            this.mSharedPreferences.edit().putLong(key, value).commit();
        }

        public void putInt(java.lang.String key, int value) {
            this.mSharedPreferences.edit().putInt(key, value).commit();
        }

        public synchronized void incrementIntKey(java.lang.String key, int defaultInitialValue) {
            int oldValue = getInt(key, defaultInitialValue);
            putInt(key, oldValue + 1);
        }

        public void deletePrefsFile() {
            if (!this.mMetricsPrefsFile.delete()) {
                android.util.Slog.w(com.android.server.recoverysystem.RecoverySystemService.TAG, "Failed to delete metrics prefs");
            }
        }
    }

    static class Injector {
        protected final android.content.Context mContext;
        protected final com.android.server.recoverysystem.RecoverySystemService.PreferencesManager mPrefs;

        Injector(android.content.Context context) {
            this.mContext = context;
            this.mPrefs = new com.android.server.recoverysystem.RecoverySystemService.PreferencesManager(context);
        }

        public android.content.Context getContext() {
            return this.mContext;
        }

        public com.android.internal.widget.LockSettingsInternal getLockSettingsService() {
            return (com.android.internal.widget.LockSettingsInternal) com.android.server.LocalServices.getService(com.android.internal.widget.LockSettingsInternal.class);
        }

        public android.os.PowerManager getPowerManager() {
            return (android.os.PowerManager) this.mContext.getSystemService("power");
        }

        public java.lang.String systemPropertiesGet(java.lang.String key) {
            return android.os.SystemProperties.get(key);
        }

        public void systemPropertiesSet(java.lang.String key, java.lang.String value) {
            android.os.SystemProperties.set(key, value);
        }

        public boolean uncryptPackageFileDelete() {
            return android.os.RecoverySystem.UNCRYPT_PACKAGE_FILE.delete();
        }

        public java.lang.String getUncryptPackageFileName() {
            return android.os.RecoverySystem.UNCRYPT_PACKAGE_FILE.getName();
        }

        public java.io.FileWriter getUncryptPackageFileWriter() throws java.io.IOException {
            return new java.io.FileWriter(android.os.RecoverySystem.UNCRYPT_PACKAGE_FILE);
        }

        public com.android.server.recoverysystem.RecoverySystemService.UncryptSocket connectService() {
            com.android.server.recoverysystem.RecoverySystemService.UncryptSocket socket = new com.android.server.recoverysystem.RecoverySystemService.UncryptSocket();
            if (!socket.connectService()) {
                socket.close();
                return null;
            }
            return socket;
        }

        public android.hardware.boot.IBootControl getBootControl() throws android.os.RemoteException {
            java.lang.String serviceName = android.hardware.boot.IBootControl.DESCRIPTOR + "/default";
            if (android.os.ServiceManager.isDeclared(serviceName)) {
                android.util.Slog.i(com.android.server.recoverysystem.RecoverySystemService.TAG, "AIDL version of BootControl HAL present, using instance " + serviceName);
                return android.hardware.boot.IBootControl.Stub.asInterface(android.os.ServiceManager.waitForDeclaredService(serviceName));
            }
            android.hardware.boot.IBootControl bootcontrol = com.android.server.recoverysystem.hal.BootControlHIDL.getService();
            if (!com.android.server.recoverysystem.hal.BootControlHIDL.isServicePresent()) {
                android.util.Slog.e(com.android.server.recoverysystem.RecoverySystemService.TAG, "Neither AIDL nor HIDL version of the BootControl HAL is present.");
                return null;
            }
            if (!com.android.server.recoverysystem.hal.BootControlHIDL.isV1_2ServicePresent()) {
                android.util.Slog.w(com.android.server.recoverysystem.RecoverySystemService.TAG, "Device doesn't implement boot control HAL V1_2.");
                return null;
            }
            return bootcontrol;
        }

        public void threadSleep(long millis) throws java.lang.InterruptedException {
            java.lang.Thread.sleep(millis);
        }

        public int getUidFromPackageName(java.lang.String packageName) {
            try {
                return this.mContext.getPackageManager().getPackageUidAsUser(packageName, 0);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.w(com.android.server.recoverysystem.RecoverySystemService.TAG, "Failed to find uid for " + packageName);
                return -1;
            }
        }

        public com.android.server.recoverysystem.RecoverySystemService.PreferencesManager getMetricsPrefs() {
            return this.mPrefs;
        }

        public long getCurrentTimeMillis() {
            return java.lang.System.currentTimeMillis();
        }

        public void reportRebootEscrowPreparationMetrics(int uid, int requestResult, int requestedClientCount) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.REBOOT_ESCROW_PREPARATION_REPORTED, uid, requestResult, requestedClientCount);
        }

        public void reportRebootEscrowLskfCapturedMetrics(int uid, int requestedClientCount, int requestedToLskfCapturedDurationInSeconds) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.REBOOT_ESCROW_LSKF_CAPTURE_REPORTED, uid, requestedClientCount, requestedToLskfCapturedDurationInSeconds);
        }

        public void reportRebootEscrowRebootMetrics(int errorCode, int uid, int preparedClientCount, int requestCount, boolean slotSwitch, boolean serverBased, int lskfCapturedToRebootDurationInSeconds, int lskfCapturedCounts) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.REBOOT_ESCROW_REBOOT_REPORTED, errorCode, uid, preparedClientCount, requestCount, slotSwitch, serverBased, lskfCapturedToRebootDurationInSeconds, lskfCapturedCounts);
        }
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private com.android.server.recoverysystem.RecoverySystemService mRecoverySystemService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 500) {
                this.mRecoverySystemService.onSystemServicesReady();
            }
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mRecoverySystemService = new com.android.server.recoverysystem.RecoverySystemService(getContext());
            publishBinderService("recovery", this.mRecoverySystemService);
        }
    }

    private RecoverySystemService(android.content.Context context) {
        this(new com.android.server.recoverysystem.RecoverySystemService.Injector(context));
    }

    RecoverySystemService(com.android.server.recoverysystem.RecoverySystemService.Injector injector) {
        this.mCallerPendingRequest = new android.util.ArrayMap<>();
        this.mCallerPreparedForReboot = new android.util.ArraySet<>();
        this.mInjector = injector;
        this.mContext = injector.getContext();
    }

    void onSystemServicesReady() {
        com.android.internal.widget.LockSettingsInternal lockSettings = this.mInjector.getLockSettingsService();
        if (lockSettings == null) {
            android.util.Slog.e(TAG, "Failed to get lock settings service, skipping set RebootEscrowListener");
        } else {
            lockSettings.setRebootEscrowListener(this);
        }
    }

    public boolean uncrypt(java.lang.String filename, android.os.IRecoverySystemProgressListener listener) {
        int status;
        synchronized (sRequestLock) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
            if (!checkAndWaitForUncryptService()) {
                android.util.Slog.e(TAG, "uncrypt service is unavailable.");
                return false;
            }
            this.mInjector.uncryptPackageFileDelete();
            try {
                java.io.FileWriter uncryptFile = this.mInjector.getUncryptPackageFileWriter();
                try {
                    uncryptFile.write(filename + "\n");
                    if (uncryptFile != null) {
                        uncryptFile.close();
                    }
                    this.mInjector.systemPropertiesSet("ctl.start", UNCRYPT_SOCKET);
                    com.android.server.recoverysystem.RecoverySystemService.UncryptSocket socket = this.mInjector.connectService();
                    if (socket == null) {
                        android.util.Slog.e(TAG, "Failed to connect to uncrypt socket");
                        return false;
                    }
                    int lastStatus = Integer.MIN_VALUE;
                    while (true) {
                        try {
                            status = socket.getPercentageUncrypted();
                            if (status != lastStatus || lastStatus == Integer.MIN_VALUE) {
                                lastStatus = status;
                                if (status < 0 || status > 100) {
                                    break;
                                }
                                android.util.Slog.i(TAG, "uncrypt read status: " + status);
                                if (listener != null) {
                                    try {
                                        listener.onProgress(status);
                                    } catch (android.os.RemoteException e) {
                                        android.util.Slog.w(TAG, "RemoteException when posting progress");
                                    }
                                }
                                if (status == 100) {
                                    android.util.Slog.i(TAG, "uncrypt successfully finished.");
                                    socket.sendAck();
                                    return true;
                                }
                            }
                        } catch (java.io.IOException e2) {
                            android.util.Slog.e(TAG, "IOException when reading status: ", e2);
                            return false;
                        } finally {
                            socket.close();
                        }
                    }
                    android.util.Slog.e(TAG, "uncrypt failed with status: " + status);
                    socket.sendAck();
                    return false;
                } catch (java.lang.Throwable th) {
                    if (uncryptFile != null) {
                        try {
                            uncryptFile.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (java.io.IOException e3) {
                android.util.Slog.e(TAG, "IOException when writing \"" + this.mInjector.getUncryptPackageFileName() + "\":", e3);
                return false;
            }
        }
    }

    public boolean clearBcb() {
        boolean z;
        synchronized (sRequestLock) {
            z = setupOrClearBcb(false, null);
        }
        return z;
    }

    public boolean setupBcb(java.lang.String command) {
        boolean z;
        synchronized (sRequestLock) {
            z = setupOrClearBcb(true, command);
        }
        return z;
    }

    public void rebootRecoveryWithCommand(java.lang.String command) {
        boolean isForcedWipe = command != null && command.contains(RECOVERY_WIPE_DATA_COMMAND);
        synchronized (sRequestLock) {
            if (!setupOrClearBcb(true, command)) {
                android.util.Slog.e(TAG, "rebootRecoveryWithCommand failed to setup BCB");
                return;
            }
            if (isForcedWipe) {
                deleteSecrets();
            }
            android.os.PowerManager pm = this.mInjector.getPowerManager();
            pm.reboot("recovery");
        }
    }

    private static void deleteSecrets() {
        com.android.server.utils.Slogf.w(TAG, "deleteSecrets");
        try {
            android.security.AndroidKeyStoreMaintenance.deleteAllKeys();
        } catch (android.security.KeyStoreException e) {
            android.util.Log.wtf(TAG, "Failed to delete all keys from keystore.", e);
        }
        try {
            android.hardware.security.secretkeeper.ISecretkeeper secretKeeper = getSecretKeeper();
            if (secretKeeper != null) {
                com.android.server.utils.Slogf.i(TAG, "ISecretkeeper.deleteAll();");
                secretKeeper.deleteAll();
            }
        } catch (android.os.RemoteException e2) {
            android.util.Log.wtf(TAG, "Failed to delete all secrets from secretkeeper.", e2);
        }
    }

    private static android.hardware.security.secretkeeper.ISecretkeeper getSecretKeeper() {
        try {
            android.hardware.security.secretkeeper.ISecretkeeper result = android.hardware.security.secretkeeper.ISecretkeeper.Stub.asInterface(android.os.ServiceManager.waitForDeclaredService(android.hardware.security.secretkeeper.ISecretkeeper.DESCRIPTOR + "/default"));
            return result;
        } catch (java.lang.SecurityException e) {
            android.util.Slog.w(TAG, "Does not have permissions to get AIDL secretkeeper service");
            return null;
        }
    }

    private void enforcePermissionForResumeOnReboot() {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.RECOVERY") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.REBOOT") != 0) {
            throw new java.lang.SecurityException("Caller must have android.permission.RECOVERY or android.permission.REBOOT for resume on reboot.");
        }
    }

    private void reportMetricsOnRequestLskf(java.lang.String packageName, int requestResult) {
        int pendingRequestCount;
        int uid = this.mInjector.getUidFromPackageName(packageName);
        synchronized (this) {
            pendingRequestCount = this.mCallerPendingRequest.size();
        }
        com.android.server.recoverysystem.RecoverySystemService.PreferencesManager prefs = this.mInjector.getMetricsPrefs();
        prefs.putLong(packageName + REQUEST_LSKF_TIMESTAMP_PREF_SUFFIX, this.mInjector.getCurrentTimeMillis());
        prefs.incrementIntKey(packageName + REQUEST_LSKF_COUNT_PREF_SUFFIX, 0);
        this.mInjector.reportRebootEscrowPreparationMetrics(uid, requestResult, pendingRequestCount);
    }

    public boolean requestLskf(java.lang.String packageName, android.content.IntentSender intentSender) {
        enforcePermissionForResumeOnReboot();
        if (packageName == null) {
            android.util.Slog.w(TAG, "Missing packageName when requesting lskf.");
            return false;
        }
        int action = updateRoRPreparationStateOnNewRequest(packageName, intentSender);
        reportMetricsOnRequestLskf(packageName, action);
        switch (action) {
            case 0:
                long origId = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.internal.widget.LockSettingsInternal lockSettings = this.mInjector.getLockSettingsService();
                    if (lockSettings == null) {
                        android.util.Slog.e(TAG, "Failed to get lock settings service, skipping prepareRebootEscrow");
                        return false;
                    }
                    if (lockSettings.prepareRebootEscrow()) {
                        return true;
                    }
                    clearRoRPreparationState();
                    return false;
                } finally {
                    android.os.Binder.restoreCallingIdentity(origId);
                }
            case 1:
                sendPreparedForRebootIntentIfNeeded(intentSender);
                return true;
            case 2:
                return true;
            default:
                throw new java.lang.IllegalStateException("Unsupported action type on new request " + action);
        }
    }

    private synchronized int updateRoRPreparationStateOnNewRequest(java.lang.String packageName, android.content.IntentSender intentSender) {
        if (!this.mCallerPreparedForReboot.isEmpty()) {
            if (this.mCallerPreparedForReboot.contains(packageName)) {
                android.util.Slog.i(TAG, "RoR already has prepared for " + packageName);
            }
            this.mCallerPreparedForReboot.add(packageName);
            return 1;
        }
        boolean needPreparation = this.mCallerPendingRequest.isEmpty();
        if (this.mCallerPendingRequest.containsKey(packageName)) {
            android.util.Slog.i(TAG, "Duplicate RoR preparation request for " + packageName);
        }
        this.mCallerPendingRequest.put(packageName, intentSender);
        return needPreparation ? 0 : 2;
    }

    private void reportMetricsOnPreparedForReboot() {
        java.util.List<java.lang.String> preparedClients;
        long currentTimestamp = this.mInjector.getCurrentTimeMillis();
        synchronized (this) {
            preparedClients = new java.util.ArrayList<>(this.mCallerPreparedForReboot);
        }
        com.android.server.recoverysystem.RecoverySystemService.PreferencesManager prefs = this.mInjector.getMetricsPrefs();
        prefs.putLong(LSKF_CAPTURED_TIMESTAMP_PREF, currentTimestamp);
        prefs.incrementIntKey(LSKF_CAPTURED_COUNT_PREF, 0);
        for (java.lang.String packageName : preparedClients) {
            int uid = this.mInjector.getUidFromPackageName(packageName);
            int durationSeconds = -1;
            long requestLskfTimestamp = prefs.getLong(packageName + REQUEST_LSKF_TIMESTAMP_PREF_SUFFIX, -1L);
            if (requestLskfTimestamp != -1 && currentTimestamp > requestLskfTimestamp) {
                durationSeconds = ((int) (currentTimestamp - requestLskfTimestamp)) / 1000;
            }
            android.util.Slog.i(TAG, java.lang.String.format("Reporting lskf captured, lskf capture takes %d seconds for package %s", java.lang.Integer.valueOf(durationSeconds), packageName));
            this.mInjector.reportRebootEscrowLskfCapturedMetrics(uid, preparedClients.size(), durationSeconds);
        }
    }

    public void onPreparedForReboot(boolean ready) {
        if (!ready) {
            return;
        }
        updateRoRPreparationStateOnPreparedForReboot();
        reportMetricsOnPreparedForReboot();
    }

    private synchronized void updateRoRPreparationStateOnPreparedForReboot() {
        if (!this.mCallerPreparedForReboot.isEmpty()) {
            android.util.Slog.w(TAG, "onPreparedForReboot called when some clients have prepared.");
        }
        if (this.mCallerPendingRequest.isEmpty()) {
            android.util.Slog.w(TAG, "onPreparedForReboot called but no client has requested.");
        }
        for (int i = 0; i < this.mCallerPendingRequest.size(); i++) {
            sendPreparedForRebootIntentIfNeeded(this.mCallerPendingRequest.valueAt(i));
            this.mCallerPreparedForReboot.add(this.mCallerPendingRequest.keyAt(i));
        }
        this.mCallerPendingRequest.clear();
    }

    private void sendPreparedForRebootIntentIfNeeded(android.content.IntentSender intentSender) {
        if (intentSender != null) {
            try {
                intentSender.sendIntent(null, 0, null, null, null);
            } catch (android.content.IntentSender.SendIntentException e) {
                android.util.Slog.w(TAG, "Could not send intent for prepared reboot: " + e.getMessage());
            }
        }
    }

    public boolean clearLskf(java.lang.String packageName) {
        enforcePermissionForResumeOnReboot();
        if (packageName == null) {
            android.util.Slog.w(TAG, "Missing packageName when clearing lskf.");
            return false;
        }
        int action = updateRoRPreparationStateOnClear(packageName);
        switch (action) {
            case 0:
                android.util.Slog.w(TAG, "RoR clear called before preparation for caller " + packageName);
                return true;
            case 1:
                long origId = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.internal.widget.LockSettingsInternal lockSettings = this.mInjector.getLockSettingsService();
                    if (lockSettings == null) {
                        android.util.Slog.e(TAG, "Failed to get lock settings service, skipping clearRebootEscrow");
                        return false;
                    }
                    return lockSettings.clearRebootEscrow();
                } finally {
                    android.os.Binder.restoreCallingIdentity(origId);
                }
            case 2:
                return true;
            default:
                throw new java.lang.IllegalStateException("Unsupported action type on clear " + action);
        }
    }

    private synchronized int updateRoRPreparationStateOnClear(java.lang.String packageName) {
        boolean z = false;
        if (!this.mCallerPreparedForReboot.contains(packageName) && !this.mCallerPendingRequest.containsKey(packageName)) {
            android.util.Slog.w(TAG, packageName + " hasn't prepared for resume on reboot");
            return 0;
        }
        this.mCallerPendingRequest.remove(packageName);
        this.mCallerPreparedForReboot.remove(packageName);
        if (this.mCallerPendingRequest.isEmpty() && this.mCallerPreparedForReboot.isEmpty()) {
            z = true;
        }
        boolean needClear = z;
        return needClear ? 1 : 2;
    }

    private boolean isAbDevice() {
        return "true".equalsIgnoreCase(this.mInjector.systemPropertiesGet(AB_UPDATE));
    }

    private boolean verifySlotForNextBoot(boolean slotSwitch) {
        if (!isAbDevice()) {
            android.util.Slog.w(TAG, "Device isn't a/b, skipping slot verification.");
            return true;
        }
        try {
            android.hardware.boot.IBootControl bootControl = this.mInjector.getBootControl();
            if (bootControl == null) {
                android.util.Slog.w(TAG, "Cannot get the boot control HAL, skipping slot verification.");
                return true;
            }
            try {
                int current_slot = bootControl.getCurrentSlot();
                if (current_slot != 0 && current_slot != 1) {
                    throw new java.lang.IllegalStateException("Current boot slot should be 0 or 1, got " + current_slot);
                }
                int next_active_slot = bootControl.getActiveBootSlot();
                int expected_active_slot = current_slot;
                if (slotSwitch) {
                    expected_active_slot = current_slot == 0 ? 1 : 0;
                }
                if (next_active_slot == expected_active_slot) {
                    return true;
                }
                android.util.Slog.w(TAG, "The next active boot slot doesn't match the expected value, expected " + expected_active_slot + ", got " + next_active_slot);
                return false;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to query the active slots", e);
                return false;
            }
        } catch (android.os.RemoteException e2) {
            android.util.Slog.w(TAG, "Failed to get the boot control HAL " + e2);
            return false;
        }
    }

    private com.android.server.recoverysystem.RecoverySystemService.RebootPreparationError armRebootEscrow(java.lang.String packageName, boolean slotSwitch) {
        if (packageName == null) {
            android.util.Slog.w(TAG, "Missing packageName when rebooting with lskf.");
            return new com.android.server.recoverysystem.RecoverySystemService.RebootPreparationError(2000, 0);
        }
        if (!isLskfCaptured(packageName)) {
            return new com.android.server.recoverysystem.RecoverySystemService.RebootPreparationError(3000, 0);
        }
        if (!verifySlotForNextBoot(slotSwitch)) {
            return new com.android.server.recoverysystem.RecoverySystemService.RebootPreparationError(4000, 0);
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.internal.widget.LockSettingsInternal lockSettings = this.mInjector.getLockSettingsService();
            if (lockSettings == null) {
                android.util.Slog.e(TAG, "Failed to get lock settings service, skipping armRebootEscrow");
                return new com.android.server.recoverysystem.RecoverySystemService.RebootPreparationError(5000, 3);
            }
            int providerErrorCode = lockSettings.armRebootEscrow();
            if (providerErrorCode != 0) {
                android.util.Slog.w(TAG, "Failure to escrow key for reboot, providerErrorCode: " + providerErrorCode);
                return new com.android.server.recoverysystem.RecoverySystemService.RebootPreparationError(5000, providerErrorCode);
            }
            return new com.android.server.recoverysystem.RecoverySystemService.RebootPreparationError(0, 0);
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    private boolean useServerBasedRoR() {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            return android.provider.DeviceConfig.getBoolean("ota", "server_based_ror_enabled", false);
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    private void reportMetricsOnRebootWithLskf(java.lang.String packageName, boolean slotSwitch, com.android.server.recoverysystem.RecoverySystemService.RebootPreparationError escrowError) {
        int preparedClientCount;
        int uid = this.mInjector.getUidFromPackageName(packageName);
        boolean serverBased = useServerBasedRoR();
        synchronized (this) {
            preparedClientCount = this.mCallerPreparedForReboot.size();
        }
        long currentTimestamp = this.mInjector.getCurrentTimeMillis();
        int durationSeconds = -1;
        com.android.server.recoverysystem.RecoverySystemService.PreferencesManager prefs = this.mInjector.getMetricsPrefs();
        long lskfCapturedTimestamp = prefs.getLong(LSKF_CAPTURED_TIMESTAMP_PREF, -1L);
        if (lskfCapturedTimestamp != -1 && currentTimestamp > lskfCapturedTimestamp) {
            durationSeconds = ((int) (currentTimestamp - lskfCapturedTimestamp)) / 1000;
        }
        int requestCount = prefs.getInt(packageName + REQUEST_LSKF_COUNT_PREF_SUFFIX, -1);
        int lskfCapturedCount = prefs.getInt(LSKF_CAPTURED_COUNT_PREF, -1);
        android.util.Slog.i(TAG, java.lang.String.format("Reporting reboot with lskf, package name %s, client count %d, request count %d, lskf captured count %d, duration since lskf captured %d seconds.", packageName, java.lang.Integer.valueOf(preparedClientCount), java.lang.Integer.valueOf(requestCount), java.lang.Integer.valueOf(lskfCapturedCount), java.lang.Integer.valueOf(durationSeconds)));
        this.mInjector.reportRebootEscrowRebootMetrics(escrowError.getErrorCodeForMetrics(), uid, preparedClientCount, requestCount, slotSwitch, serverBased, durationSeconds, lskfCapturedCount);
    }

    private synchronized void clearRoRPreparationState() {
        this.mCallerPendingRequest.clear();
        this.mCallerPreparedForReboot.clear();
    }

    private void clearRoRPreparationStateOnRebootFailure(com.android.server.recoverysystem.RecoverySystemService.RebootPreparationError escrowError) {
        if (!FATAL_ARM_ESCROW_ERRORS.contains(java.lang.Integer.valueOf(escrowError.mProviderErrorCode))) {
            return;
        }
        android.util.Slog.w(TAG, "Clearing resume on reboot states for all clients on arm escrow error: " + escrowError.mProviderErrorCode);
        clearRoRPreparationState();
    }

    private int rebootWithLskfImpl(java.lang.String packageName, java.lang.String reason, boolean slotSwitch) {
        com.android.server.recoverysystem.RecoverySystemService.RebootPreparationError escrowError = armRebootEscrow(packageName, slotSwitch);
        reportMetricsOnRebootWithLskf(packageName, slotSwitch, escrowError);
        clearRoRPreparationStateOnRebootFailure(escrowError);
        int errorCode = escrowError.mRebootErrorCode;
        if (errorCode != 0) {
            return errorCode;
        }
        this.mInjector.getMetricsPrefs().deletePrefsFile();
        com.android.server.Watchdog.getInstance().pauseWatchingCurrentThreadFor(REBOOT_WATCHDOG_PAUSE_DURATION_MS, "reboot can be slow");
        android.os.PowerManager pm = this.mInjector.getPowerManager();
        pm.reboot(reason);
        return 1000;
    }

    public int rebootWithLskfAssumeSlotSwitch(java.lang.String packageName, java.lang.String reason) {
        rebootWithLskfAssumeSlotSwitch_enforcePermission();
        return rebootWithLskfImpl(packageName, reason, true);
    }

    public int rebootWithLskf(java.lang.String packageName, java.lang.String reason, boolean slotSwitch) {
        enforcePermissionForResumeOnReboot();
        return rebootWithLskfImpl(packageName, reason, slotSwitch);
    }

    private static android.apex.CompressedApexInfoList getCompressedApexInfoList(java.lang.String packageFile) throws java.io.IOException {
        java.util.zip.ZipEntry entry;
        java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(packageFile);
        try {
            entry = zipFile.getEntry("apex_info.pb");
        } catch (java.lang.Throwable th) {
            try {
                zipFile.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
        if (entry == null) {
            zipFile.close();
            return null;
        }
        if (entry.getSize() >= APEX_INFO_SIZE_LIMIT) {
            throw new java.lang.IllegalArgumentException("apex_info.pb has size " + entry.getSize() + " which is larger than the permitted limit" + APEX_INFO_SIZE_LIMIT);
        }
        if (entry.getSize() != 0) {
            android.util.Log.i(TAG, "Allocating " + entry.getSize() + " bytes of memory to store OTA Metadata");
            byte[] data = new byte[(int) entry.getSize()];
            java.io.InputStream is = zipFile.getInputStream(entry);
            try {
                int bytesRead = is.read(data);
                java.lang.String msg = "Read " + bytesRead + " when expecting " + data.length;
                android.util.Log.e(TAG, msg);
                if (bytesRead != data.length) {
                    throw new java.io.IOException(msg);
                }
                if (is != null) {
                    is.close();
                }
                android.ota.nano.OtaPackageMetadata.ApexMetadata metadata = android.ota.nano.OtaPackageMetadata.ApexMetadata.parseFrom(data);
                android.apex.CompressedApexInfoList apexInfoList = new android.apex.CompressedApexInfoList();
                apexInfoList.apexInfos = (android.apex.CompressedApexInfo[]) java.util.Arrays.stream(metadata.apexInfo).filter(new java.util.function.Predicate() { // from class: com.android.server.recoverysystem.RecoverySystemService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return ((android.ota.nano.OtaPackageMetadata.ApexInfo) obj).isCompressed;
                    }
                }).map(new java.util.function.Function() { // from class: com.android.server.recoverysystem.RecoverySystemService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.recoverysystem.RecoverySystemService.lambda$getCompressedApexInfoList$1((android.ota.nano.OtaPackageMetadata.ApexInfo) obj);
                    }
                }).toArray(new java.util.function.IntFunction() { // from class: com.android.server.recoverysystem.RecoverySystemService$$ExternalSyntheticLambda2
                    @Override // java.util.function.IntFunction
                    public final java.lang.Object apply(int i) {
                        return com.android.server.recoverysystem.RecoverySystemService.lambda$getCompressedApexInfoList$2(i);
                    }
                });
                zipFile.close();
                return apexInfoList;
            } finally {
            }
            zipFile.close();
            throw th;
        }
        android.apex.CompressedApexInfoList infoList = new android.apex.CompressedApexInfoList();
        infoList.apexInfos = new android.apex.CompressedApexInfo[0];
        zipFile.close();
        return infoList;
    }

    static /* synthetic */ android.apex.CompressedApexInfo lambda$getCompressedApexInfoList$1(android.ota.nano.OtaPackageMetadata.ApexInfo apex) {
        android.apex.CompressedApexInfo info = new android.apex.CompressedApexInfo();
        info.moduleName = apex.packageName;
        info.decompressedSize = apex.decompressedSize;
        info.versionCode = apex.version;
        return info;
    }

    static /* synthetic */ android.apex.CompressedApexInfo[] lambda$getCompressedApexInfoList$2(int x$0) {
        return new android.apex.CompressedApexInfo[x$0];
    }

    public boolean allocateSpaceForUpdate(java.lang.String packageFile) {
        allocateSpaceForUpdate_enforcePermission();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                try {
                    android.apex.CompressedApexInfoList apexInfoList = getCompressedApexInfoList(packageFile);
                    if (apexInfoList == null) {
                        android.util.Log.i(TAG, "apex_info.pb not present in OTA package. Assuming device doesn't support compressedAPEX, continueing without allocating space.");
                        return true;
                    }
                    com.android.server.pm.ApexManager apexManager = com.android.server.pm.ApexManager.getInstance();
                    apexManager.reserveSpaceForCompressedApex(apexInfoList);
                    return true;
                } catch (android.os.RemoteException e) {
                    e.rethrowAsRuntimeException();
                    android.os.Binder.restoreCallingIdentity(token);
                    return false;
                }
            } catch (java.io.IOException | java.lang.UnsupportedOperationException e2) {
                android.util.Slog.e(TAG, "Failed to reserve space for compressed apex: ", e2);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean isLskfCaptured(java.lang.String packageName) {
        boolean captured;
        enforcePermissionForResumeOnReboot();
        synchronized (this) {
            captured = this.mCallerPreparedForReboot.contains(packageName);
        }
        if (!captured) {
            android.util.Slog.i(TAG, "Reboot requested before prepare completed for caller " + packageName);
            return false;
        }
        return true;
    }

    private boolean checkAndWaitForUncryptService() {
        int retry = 0;
        while (true) {
            if (retry >= 30) {
                return false;
            }
            java.lang.String uncryptService = this.mInjector.systemPropertiesGet(INIT_SERVICE_UNCRYPT);
            java.lang.String setupBcbService = this.mInjector.systemPropertiesGet(INIT_SERVICE_SETUP_BCB);
            java.lang.String clearBcbService = this.mInjector.systemPropertiesGet(INIT_SERVICE_CLEAR_BCB);
            boolean busy = android.net.INetd.IF_FLAG_RUNNING.equals(uncryptService) || android.net.INetd.IF_FLAG_RUNNING.equals(setupBcbService) || android.net.INetd.IF_FLAG_RUNNING.equals(clearBcbService);
            if (!busy) {
                return true;
            }
            try {
                this.mInjector.threadSleep(1000L);
            } catch (java.lang.InterruptedException e) {
                android.util.Slog.w(TAG, "Interrupted:", e);
            }
            retry++;
        }
    }

    private boolean setupOrClearBcb(boolean isSetup, java.lang.String command) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
        boolean available = checkAndWaitForUncryptService();
        if (!available) {
            android.util.Slog.e(TAG, "uncrypt service is unavailable.");
            return false;
        }
        if (isSetup) {
            this.mInjector.systemPropertiesSet("ctl.start", "setup-bcb");
        } else {
            this.mInjector.systemPropertiesSet("ctl.start", "clear-bcb");
        }
        com.android.server.recoverysystem.RecoverySystemService.UncryptSocket socket = this.mInjector.connectService();
        if (socket == null) {
            android.util.Slog.e(TAG, "Failed to connect to uncrypt socket");
            return false;
        }
        try {
            if (isSetup) {
                socket.sendCommand(command);
            }
            int status = socket.getPercentageUncrypted();
            socket.sendAck();
            if (status != 100) {
                android.util.Slog.e(TAG, "uncrypt failed with status: " + status);
                return false;
            }
            android.util.Slog.i(TAG, "uncrypt " + (isSetup ? "setup" : "clear") + " bcb successfully finished.");
            socket.close();
            return true;
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "IOException when communicating with uncrypt:", e);
            return false;
        } finally {
            socket.close();
        }
    }

    public static class UncryptSocket {
        private java.io.DataInputStream mInputStream;
        private android.net.LocalSocket mLocalSocket;
        private java.io.DataOutputStream mOutputStream;

        public boolean connectService() {
            this.mLocalSocket = new android.net.LocalSocket();
            boolean done = false;
            int retry = 0;
            while (true) {
                if (retry >= 30) {
                    break;
                }
                try {
                    this.mLocalSocket.connect(new android.net.LocalSocketAddress(com.android.server.recoverysystem.RecoverySystemService.UNCRYPT_SOCKET, android.net.LocalSocketAddress.Namespace.RESERVED));
                    done = true;
                    break;
                } catch (java.io.IOException e) {
                    try {
                        java.lang.Thread.sleep(1000L);
                    } catch (java.lang.InterruptedException e2) {
                        android.util.Slog.w(com.android.server.recoverysystem.RecoverySystemService.TAG, "Interrupted:", e2);
                    }
                    retry++;
                }
            }
            if (!done) {
                android.util.Slog.e(com.android.server.recoverysystem.RecoverySystemService.TAG, "Timed out connecting to uncrypt socket");
                close();
                return false;
            }
            try {
                this.mInputStream = new java.io.DataInputStream(this.mLocalSocket.getInputStream());
                this.mOutputStream = new java.io.DataOutputStream(this.mLocalSocket.getOutputStream());
                return true;
            } catch (java.io.IOException e3) {
                close();
                return false;
            }
        }

        public void sendCommand(java.lang.String command) throws java.io.IOException {
            byte[] cmdUtf8 = command.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            this.mOutputStream.writeInt(cmdUtf8.length);
            this.mOutputStream.write(cmdUtf8, 0, cmdUtf8.length);
        }

        public int getPercentageUncrypted() throws java.io.IOException {
            return this.mInputStream.readInt();
        }

        public void sendAck() throws java.io.IOException {
            this.mOutputStream.writeInt(0);
        }

        public void close() {
            libcore.io.IoUtils.closeQuietly(this.mInputStream);
            libcore.io.IoUtils.closeQuietly(this.mOutputStream);
            libcore.io.IoUtils.closeQuietly(this.mLocalSocket);
        }
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
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws java.lang.Throwable {
        enforceShell();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
        } catch (java.lang.Throwable th) {
            th = th;
        }
        try {
            new com.android.server.recoverysystem.RecoverySystemShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
            android.os.Binder.restoreCallingIdentity(origId);
        } catch (java.lang.Throwable th2) {
            th = th2;
            android.os.Binder.restoreCallingIdentity(origId);
            throw th;
        }
    }
}
