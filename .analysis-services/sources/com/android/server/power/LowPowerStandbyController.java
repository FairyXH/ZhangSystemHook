package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public class LowPowerStandbyController {
    private static final java.lang.String ATTR_VALUE = "value";
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_ACTIVE_DURING_MAINTENANCE = false;
    static final android.os.PowerManager.LowPowerStandbyPolicy DEFAULT_POLICY = new android.os.PowerManager.LowPowerStandbyPolicy("DEFAULT_POLICY", java.util.Collections.emptySet(), 1, java.util.Collections.emptySet());
    private static final int MSG_FOREGROUND_SERVICE_STATE_CHANGED = 4;
    private static final int MSG_NOTIFY_ACTIVE_CHANGED = 1;
    private static final int MSG_NOTIFY_ALLOWLIST_CHANGED = 2;
    private static final int MSG_NOTIFY_POLICY_CHANGED = 3;
    private static final int MSG_NOTIFY_STANDBY_PORTS_CHANGED = 5;
    private static final int MSG_STANDBY_TIMEOUT = 0;
    private static final java.lang.String TAG = "LowPowerStandbyController";
    private static final java.lang.String TAG_ALLOWED_FEATURES = "allowed-features";
    private static final java.lang.String TAG_ALLOWED_REASONS = "allowed-reasons";
    private static final java.lang.String TAG_EXEMPT_PACKAGE = "exempt-package";
    private static final java.lang.String TAG_IDENTIFIER = "identifier";
    private static final java.lang.String TAG_ROOT = "low-power-standby-policy";
    private boolean mActiveDuringMaintenance;
    private final java.util.function.Supplier<android.app.IActivityManager> mActivityManager;
    private android.app.ActivityManagerInternal mActivityManagerInternal;
    private android.app.AlarmManager mAlarmManager;
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private final com.android.server.power.LowPowerStandbyController.Clock mClock;
    private final android.content.Context mContext;
    private final com.android.server.power.LowPowerStandbyController.DeviceConfigWrapper mDeviceConfig;
    private boolean mEnableCustomPolicy;
    private boolean mEnableStandbyPorts;
    private boolean mEnabledByDefaultConfig;
    private boolean mForceActive;
    private final android.os.Handler mHandler;
    private boolean mIdleSinceNonInteractive;
    private boolean mIsActive;
    private boolean mIsDeviceIdle;
    private boolean mIsEnabled;
    private boolean mIsInteractive;
    private long mLastInteractiveTimeElapsed;
    private final com.android.server.power.LowPowerStandbyControllerInternal mLocalService;
    private final java.lang.Object mLock;
    private final java.util.List<java.lang.String> mLowPowerStandbyManagingPackages;
    private final android.app.AlarmManager.OnAlarmListener mOnStandbyTimeoutExpired;
    private final android.content.BroadcastReceiver mPackageBroadcastReceiver;
    private final com.android.server.power.LowPowerStandbyController.PhoneCallServiceTracker mPhoneCallServiceTracker;
    private android.os.PowerManager.LowPowerStandbyPolicy mPolicy;
    private final java.io.File mPolicyFile;
    private android.os.PowerManager mPowerManager;
    private final com.android.server.power.LowPowerStandbyController.SettingsObserver mSettingsObserver;
    private final java.util.List<com.android.server.power.LowPowerStandbyController.StandbyPortsLock> mStandbyPortLocks;
    private int mStandbyTimeoutConfig;
    private boolean mSupportedConfig;
    private final com.android.server.power.LowPowerStandbyController.TempAllowlistChangeListener mTempAllowlistChangeListener;
    private final android.util.SparseIntArray mUidAllowedReasons;
    private final android.content.BroadcastReceiver mUserReceiver;

    interface Clock {
        long elapsedRealtime();

        long uptimeMillis();
    }

    private final class StandbyPortsLock implements android.os.IBinder.DeathRecipient {
        private final java.util.List<android.os.PowerManager.LowPowerStandbyPortDescription> mPorts;
        private final android.os.IBinder mToken;
        private final int mUid;

        StandbyPortsLock(android.os.IBinder token, int uid, java.util.List<android.os.PowerManager.LowPowerStandbyPortDescription> ports) {
            this.mToken = token;
            this.mUid = uid;
            this.mPorts = ports;
        }

        public boolean linkToDeath() {
            try {
                this.mToken.linkToDeath(this, 0);
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Slog.i(com.android.server.power.LowPowerStandbyController.TAG, "StandbyPorts token already died");
                return false;
            }
        }

        public void unlinkToDeath() {
            this.mToken.unlinkToDeath(this, 0);
        }

        public android.os.IBinder getToken() {
            return this.mToken;
        }

        public int getUid() {
            return this.mUid;
        }

        public java.util.List<android.os.PowerManager.LowPowerStandbyPortDescription> getPorts() {
            return this.mPorts;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.power.LowPowerStandbyController.this.releaseStandbyPorts(this.mToken);
        }
    }

    private static class RealClock implements com.android.server.power.LowPowerStandbyController.Clock {
        private RealClock() {
        }

        @Override // com.android.server.power.LowPowerStandbyController.Clock
        public long elapsedRealtime() {
            return android.os.SystemClock.elapsedRealtime();
        }

        @Override // com.android.server.power.LowPowerStandbyController.Clock
        public long uptimeMillis() {
            return android.os.SystemClock.uptimeMillis();
        }
    }

    public LowPowerStandbyController(android.content.Context context, android.os.Looper looper) {
        this(context, looper, new com.android.server.power.LowPowerStandbyController.RealClock(), new com.android.server.power.LowPowerStandbyController.DeviceConfigWrapper(), new java.util.function.Supplier() { // from class: com.android.server.power.LowPowerStandbyController$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return android.app.ActivityManager.getService();
            }
        }, new java.io.File(android.os.Environment.getDataSystemDirectory(), "low_power_standby_policy.xml"));
    }

    LowPowerStandbyController(android.content.Context context, android.os.Looper looper, com.android.server.power.LowPowerStandbyController.Clock clock, com.android.server.power.LowPowerStandbyController.DeviceConfigWrapper deviceConfig, java.util.function.Supplier<android.app.IActivityManager> activityManager, java.io.File policyFile) {
        this.mLock = new java.lang.Object();
        this.mOnStandbyTimeoutExpired = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.power.LowPowerStandbyController$$ExternalSyntheticLambda1
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.onStandbyTimeoutExpired();
            }
        };
        this.mLocalService = new com.android.server.power.LowPowerStandbyController.LocalService();
        this.mUidAllowedReasons = new android.util.SparseIntArray();
        this.mLowPowerStandbyManagingPackages = new java.util.ArrayList();
        this.mStandbyPortLocks = new java.util.ArrayList();
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.power.LowPowerStandbyController.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:14:0x002a  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r3, android.content.Intent r4) {
                /*
                    r2 = this;
                    java.lang.String r0 = r4.getAction()
                    int r1 = r0.hashCode()
                    switch(r1) {
                        case -2128145023: goto L20;
                        case -1454123155: goto L16;
                        case 870701415: goto Lc;
                        default: goto Lb;
                    }
                Lb:
                    goto L2a
                Lc:
                    java.lang.String r1 = "android.os.action.DEVICE_IDLE_MODE_CHANGED"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Lb
                    r0 = 2
                    goto L2b
                L16:
                    java.lang.String r1 = "android.intent.action.SCREEN_ON"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Lb
                    r0 = 1
                    goto L2b
                L20:
                    java.lang.String r1 = "android.intent.action.SCREEN_OFF"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Lb
                    r0 = 0
                    goto L2b
                L2a:
                    r0 = -1
                L2b:
                    switch(r0) {
                        case 0: goto L3b;
                        case 1: goto L35;
                        case 2: goto L2f;
                        default: goto L2e;
                    }
                L2e:
                    goto L41
                L2f:
                    com.android.server.power.LowPowerStandbyController r0 = com.android.server.power.LowPowerStandbyController.this
                    com.android.server.power.LowPowerStandbyController.m8239$$Nest$monDeviceIdleModeChanged(r0)
                    goto L41
                L35:
                    com.android.server.power.LowPowerStandbyController r0 = com.android.server.power.LowPowerStandbyController.this
                    com.android.server.power.LowPowerStandbyController.m8240$$Nest$monInteractive(r0)
                    goto L41
                L3b:
                    com.android.server.power.LowPowerStandbyController r0 = com.android.server.power.LowPowerStandbyController.this
                    com.android.server.power.LowPowerStandbyController.m8241$$Nest$monNonInteractive(r0)
                L41:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.LowPowerStandbyController.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        this.mTempAllowlistChangeListener = new com.android.server.power.LowPowerStandbyController.TempAllowlistChangeListener();
        this.mPhoneCallServiceTracker = new com.android.server.power.LowPowerStandbyController.PhoneCallServiceTracker();
        this.mPackageBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.power.LowPowerStandbyController.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                boolean replacing = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
                if (replacing) {
                    return;
                }
                android.net.Uri intentUri = intent.getData();
                java.lang.String packageName = intentUri != null ? intentUri.getSchemeSpecificPart() : null;
                synchronized (com.android.server.power.LowPowerStandbyController.this.mLock) {
                    android.os.PowerManager.LowPowerStandbyPolicy policy = com.android.server.power.LowPowerStandbyController.this.getPolicy();
                    if (policy.getExemptPackages().contains(packageName)) {
                        com.android.server.power.LowPowerStandbyController.this.enqueueNotifyAllowlistChangedLocked();
                    }
                }
            }
        };
        this.mUserReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.power.LowPowerStandbyController.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                synchronized (com.android.server.power.LowPowerStandbyController.this.mLock) {
                    com.android.server.power.LowPowerStandbyController.this.enqueueNotifyAllowlistChangedLocked();
                }
            }
        };
        this.mContext = context;
        this.mHandler = new com.android.server.power.LowPowerStandbyController.LowPowerStandbyHandler(looper);
        this.mClock = clock;
        this.mSettingsObserver = new com.android.server.power.LowPowerStandbyController.SettingsObserver(this.mHandler);
        this.mDeviceConfig = deviceConfig;
        this.mActivityManager = activityManager;
        this.mPolicyFile = policyFile;
    }

    public void systemReady() {
        android.content.res.Resources resources = this.mContext.getResources();
        synchronized (this.mLock) {
            this.mSupportedConfig = resources.getBoolean(android.R.bool.config_keyguardUserSwitcher);
            if (this.mSupportedConfig) {
                java.util.List<android.content.pm.PackageInfo> manageLowPowerStandbyPackages = this.mContext.getPackageManager().getPackagesHoldingPermissions(new java.lang.String[]{"android.permission.MANAGE_LOW_POWER_STANDBY"}, 1048576);
                for (android.content.pm.PackageInfo packageInfo : manageLowPowerStandbyPackages) {
                    this.mLowPowerStandbyManagingPackages.add(packageInfo.packageName);
                }
                this.mAlarmManager = (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
                this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
                this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
                this.mStandbyTimeoutConfig = resources.getInteger(android.R.integer.config_letterboxDefaultPositionForVerticalReachability);
                this.mEnabledByDefaultConfig = resources.getBoolean(android.R.bool.config_keyboardVibrationSettingsSupported);
                this.mIsInteractive = this.mPowerManager.isInteractive();
                this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("low_power_standby_enabled"), false, this.mSettingsObserver, -1);
                this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("low_power_standby_active_during_maintenance"), false, this.mSettingsObserver, -1);
                this.mDeviceConfig.registerPropertyUpdateListener(this.mContext.getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.power.LowPowerStandbyController$$ExternalSyntheticLambda3
                    public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                        this.f$0.lambda$systemReady$1(properties);
                    }
                });
                this.mEnableCustomPolicy = this.mDeviceConfig.enableCustomPolicy();
                this.mEnableStandbyPorts = this.mDeviceConfig.enableStandbyPorts();
                if (this.mEnableCustomPolicy) {
                    this.mPolicy = loadPolicy();
                } else {
                    this.mPolicy = DEFAULT_POLICY;
                }
                initSettingsLocked();
                updateSettingsLocked();
                if (this.mIsEnabled) {
                    registerListeners();
                }
                com.android.server.LocalServices.addService(com.android.server.power.LowPowerStandbyControllerInternal.class, this.mLocalService);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$1(android.provider.DeviceConfig.Properties properties) {
        onDeviceConfigFlagsChanged();
    }

    private void onDeviceConfigFlagsChanged() {
        synchronized (this.mLock) {
            boolean enableCustomPolicy = this.mDeviceConfig.enableCustomPolicy();
            if (this.mEnableCustomPolicy != enableCustomPolicy) {
                enqueueNotifyPolicyChangedLocked();
                enqueueNotifyAllowlistChangedLocked();
                this.mEnableCustomPolicy = enableCustomPolicy;
            }
            this.mEnableStandbyPorts = this.mDeviceConfig.enableStandbyPorts();
        }
    }

    private void initSettingsLocked() {
        android.content.ContentResolver contentResolver = this.mContext.getContentResolver();
        if (this.mSupportedConfig && android.provider.Settings.Global.getInt(contentResolver, "low_power_standby_enabled", -1) == -1) {
            android.provider.Settings.Global.putInt(contentResolver, "low_power_standby_enabled", this.mEnabledByDefaultConfig ? 1 : 0);
        }
    }

    private void updateSettingsLocked() {
        android.content.ContentResolver contentResolver = this.mContext.getContentResolver();
        this.mIsEnabled = this.mSupportedConfig && android.provider.Settings.Global.getInt(contentResolver, "low_power_standby_enabled", this.mEnabledByDefaultConfig ? 1 : 0) != 0;
        this.mActiveDuringMaintenance = android.provider.Settings.Global.getInt(contentResolver, "low_power_standby_active_during_maintenance", 0) != 0;
        updateActiveLocked();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0065  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.os.PowerManager.LowPowerStandbyPolicy loadPolicy() {
        /*
            Method dump skipped, instruction units count: 286
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.LowPowerStandbyController.loadPolicy():android.os.PowerManager$LowPowerStandbyPolicy");
    }

    static void writeTagValue(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, java.lang.String value) throws java.io.IOException {
        if (android.text.TextUtils.isEmpty(value)) {
            return;
        }
        out.startTag((java.lang.String) null, tag);
        out.attribute((java.lang.String) null, ATTR_VALUE, value);
        out.endTag((java.lang.String) null, tag);
    }

    static void writeTagValue(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, int value) throws java.io.IOException {
        out.startTag((java.lang.String) null, tag);
        out.attributeInt((java.lang.String) null, ATTR_VALUE, value);
        out.endTag((java.lang.String) null, tag);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: savePolicy, reason: merged with bridge method [inline-methods] */
    public void lambda$enqueueSavePolicy$2(android.os.PowerManager.LowPowerStandbyPolicy policy) {
        android.util.AtomicFile file = getPolicyFile();
        if (policy == null) {
            file.delete();
            return;
        }
        java.io.FileOutputStream outs = null;
        try {
            file.getBaseFile().mkdirs();
            outs = file.startWrite();
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(outs);
            out.startDocument((java.lang.String) null, true);
            out.startTag((java.lang.String) null, TAG_ROOT);
            writeTagValue(out, TAG_IDENTIFIER, policy.getIdentifier());
            for (java.lang.String exemptPackage : policy.getExemptPackages()) {
                writeTagValue(out, TAG_EXEMPT_PACKAGE, exemptPackage);
            }
            writeTagValue(out, TAG_ALLOWED_REASONS, policy.getAllowedReasons());
            for (java.lang.String allowedFeature : policy.getAllowedFeatures()) {
                writeTagValue(out, TAG_ALLOWED_FEATURES, allowedFeature);
            }
            out.endTag((java.lang.String) null, TAG_ROOT);
            out.endDocument();
            file.finishWrite(outs);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write policy to file " + file.getBaseFile(), e);
            file.failWrite(outs);
        }
    }

    private void enqueueSavePolicy(final android.os.PowerManager.LowPowerStandbyPolicy policy) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.LowPowerStandbyController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$enqueueSavePolicy$2(policy);
            }
        });
    }

    private android.util.AtomicFile getPolicyFile() {
        return new android.util.AtomicFile(this.mPolicyFile);
    }

    private void updateActiveLocked() {
        long nowElapsed = this.mClock.elapsedRealtime();
        boolean newActive = true;
        boolean standbyTimeoutExpired = nowElapsed - this.mLastInteractiveTimeElapsed >= ((long) this.mStandbyTimeoutConfig);
        boolean maintenanceMode = this.mIdleSinceNonInteractive && !this.mIsDeviceIdle;
        if (!this.mForceActive && (!this.mIsEnabled || this.mIsInteractive || !standbyTimeoutExpired || (maintenanceMode && !this.mActiveDuringMaintenance))) {
            newActive = false;
        }
        if (this.mIsActive != newActive) {
            this.mIsActive = newActive;
            enqueueNotifyActiveChangedLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNonInteractive() {
        long nowElapsed = this.mClock.elapsedRealtime();
        synchronized (this.mLock) {
            this.mIsInteractive = false;
            this.mIsDeviceIdle = false;
            this.mLastInteractiveTimeElapsed = nowElapsed;
            if (this.mStandbyTimeoutConfig > 0) {
                scheduleStandbyTimeoutAlarmLocked();
            }
            updateActiveLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInteractive() {
        synchronized (this.mLock) {
            cancelStandbyTimeoutAlarmLocked();
            this.mIsInteractive = true;
            this.mIsDeviceIdle = false;
            this.mIdleSinceNonInteractive = false;
            updateActiveLocked();
        }
    }

    private void scheduleStandbyTimeoutAlarmLocked() {
        long nextAlarmTime = this.mClock.elapsedRealtime() + ((long) this.mStandbyTimeoutConfig);
        this.mAlarmManager.setExact(2, nextAlarmTime, "LowPowerStandbyController.StandbyTimeout", this.mOnStandbyTimeoutExpired, this.mHandler);
    }

    private void cancelStandbyTimeoutAlarmLocked() {
        this.mAlarmManager.cancel(this.mOnStandbyTimeoutExpired);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDeviceIdleModeChanged() {
        synchronized (this.mLock) {
            this.mIsDeviceIdle = this.mPowerManager.isDeviceIdleMode();
            this.mIdleSinceNonInteractive = this.mIdleSinceNonInteractive || this.mIsDeviceIdle;
            updateActiveLocked();
        }
    }

    private void onEnabledLocked() {
        if (this.mPowerManager.isInteractive()) {
            onInteractive();
        } else {
            onNonInteractive();
        }
        registerListeners();
    }

    private void onDisabledLocked() {
        cancelStandbyTimeoutAlarmLocked();
        unregisterListeners();
        updateActiveLocked();
    }

    void onSettingsChanged() {
        synchronized (this.mLock) {
            boolean oldEnabled = this.mIsEnabled;
            updateSettingsLocked();
            if (this.mIsEnabled != oldEnabled) {
                if (this.mIsEnabled) {
                    onEnabledLocked();
                } else {
                    onDisabledLocked();
                }
                notifyEnabledChangedLocked();
            }
        }
    }

    private void registerListeners() {
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
        android.content.IntentFilter packageFilter = new android.content.IntentFilter();
        packageFilter.addDataScheme("package");
        packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
        packageFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        packageFilter.setPriority(1000);
        this.mContext.registerReceiver(this.mPackageBroadcastReceiver, packageFilter);
        android.content.IntentFilter userFilter = new android.content.IntentFilter();
        userFilter.addAction("android.intent.action.USER_ADDED");
        userFilter.addAction("android.intent.action.USER_REMOVED");
        this.mContext.registerReceiver(this.mUserReceiver, userFilter, null, this.mHandler);
        com.android.server.PowerAllowlistInternal pai = (com.android.server.PowerAllowlistInternal) com.android.server.LocalServices.getService(com.android.server.PowerAllowlistInternal.class);
        pai.registerTempAllowlistChangeListener(this.mTempAllowlistChangeListener);
        this.mPhoneCallServiceTracker.register();
    }

    private void unregisterListeners() {
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        this.mContext.unregisterReceiver(this.mPackageBroadcastReceiver);
        this.mContext.unregisterReceiver(this.mUserReceiver);
        com.android.server.PowerAllowlistInternal pai = (com.android.server.PowerAllowlistInternal) com.android.server.LocalServices.getService(com.android.server.PowerAllowlistInternal.class);
        pai.unregisterTempAllowlistChangeListener(this.mTempAllowlistChangeListener);
    }

    private void notifyEnabledChangedLocked() {
        sendExplicitBroadcast("android.os.action.LOW_POWER_STANDBY_ENABLED_CHANGED");
    }

    private void enqueueNotifyPolicyChangedLocked() {
        android.os.Message msg = this.mHandler.obtainMessage(3, getPolicy());
        this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPolicyChanged(android.os.PowerManager.LowPowerStandbyPolicy policy) {
        sendExplicitBroadcast("android.os.action.LOW_POWER_STANDBY_POLICY_CHANGED");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStandbyTimeoutExpired() {
        synchronized (this.mLock) {
            updateActiveLocked();
        }
    }

    private void sendExplicitBroadcast(java.lang.String intentType) {
        android.content.Intent intent = new android.content.Intent(intentType);
        intent.addFlags(1342177280);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL);
        android.content.Intent privilegedIntent = new android.content.Intent(intentType);
        privilegedIntent.addFlags(268435456);
        for (java.lang.String packageName : this.mLowPowerStandbyManagingPackages) {
            android.content.Intent explicitIntent = new android.content.Intent(privilegedIntent);
            explicitIntent.setPackage(packageName);
            this.mContext.sendBroadcastAsUser(explicitIntent, android.os.UserHandle.ALL, "android.permission.MANAGE_LOW_POWER_STANDBY");
        }
    }

    private void enqueueNotifyActiveChangedLocked() {
        android.os.Message msg = this.mHandler.obtainMessage(1, java.lang.Boolean.valueOf(this.mIsActive));
        this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyActiveChanged(boolean active) {
        android.os.PowerManagerInternal pmi = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
        com.android.server.net.NetworkPolicyManagerInternal npmi = (com.android.server.net.NetworkPolicyManagerInternal) com.android.server.LocalServices.getService(com.android.server.net.NetworkPolicyManagerInternal.class);
        pmi.setLowPowerStandbyActive(active);
        npmi.setLowPowerStandbyActive(active);
    }

    boolean isActive() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsActive;
        }
        return z;
    }

    boolean isSupported() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSupportedConfig;
        }
        return z;
    }

    boolean isEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSupportedConfig && this.mIsEnabled;
        }
        return z;
    }

    void setEnabled(boolean enabled) {
        synchronized (this.mLock) {
            if (!this.mSupportedConfig) {
                android.util.Slog.w(TAG, "Low Power Standby cannot be enabled because it is not supported on this device");
            } else {
                android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "low_power_standby_enabled", enabled ? 1 : 0);
                onSettingsChanged();
            }
        }
    }

    public void setActiveDuringMaintenance(boolean activeDuringMaintenance) {
        synchronized (this.mLock) {
            if (!this.mSupportedConfig) {
                android.util.Slog.w(TAG, "Low Power Standby settings cannot be changed because it is not supported on this device");
            } else {
                android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "low_power_standby_active_during_maintenance", activeDuringMaintenance ? 1 : 0);
                onSettingsChanged();
            }
        }
    }

    void forceActive(boolean active) {
        synchronized (this.mLock) {
            this.mForceActive = active;
            updateActiveLocked();
        }
    }

    void setPolicy(android.os.PowerManager.LowPowerStandbyPolicy policy) {
        synchronized (this.mLock) {
            if (!this.mSupportedConfig) {
                android.util.Slog.w(TAG, "Low Power Standby policy cannot be changed because it is not supported on this device");
                return;
            }
            if (!this.mEnableCustomPolicy) {
                android.util.Slog.d(TAG, "Custom policies are not enabled.");
                return;
            }
            if (java.util.Objects.equals(this.mPolicy, policy)) {
                return;
            }
            boolean allowlistChanged = policyChangeAffectsAllowlistLocked(this.mPolicy, policy);
            this.mPolicy = policy;
            enqueueSavePolicy(this.mPolicy);
            if (allowlistChanged) {
                enqueueNotifyAllowlistChangedLocked();
            }
            enqueueNotifyPolicyChangedLocked();
        }
    }

    android.os.PowerManager.LowPowerStandbyPolicy getPolicy() {
        synchronized (this.mLock) {
            if (!this.mSupportedConfig) {
                return null;
            }
            if (this.mEnableCustomPolicy) {
                return policyOrDefault(this.mPolicy);
            }
            return DEFAULT_POLICY;
        }
    }

    private android.os.PowerManager.LowPowerStandbyPolicy policyOrDefault(android.os.PowerManager.LowPowerStandbyPolicy policy) {
        if (policy == null) {
            return DEFAULT_POLICY;
        }
        return policy;
    }

    boolean isPackageExempt(int uid) {
        synchronized (this.mLock) {
            if (!isEnabled()) {
                return true;
            }
            return getExemptPackageAppIdsLocked().contains(java.lang.Integer.valueOf(android.os.UserHandle.getAppId(uid)));
        }
    }

    boolean isAllowed(int reason) {
        synchronized (this.mLock) {
            boolean z = true;
            if (!isEnabled()) {
                return true;
            }
            if ((getPolicy().getAllowedReasons() & reason) == 0) {
                z = false;
            }
            return z;
        }
    }

    boolean isAllowed(java.lang.String feature) {
        synchronized (this.mLock) {
            boolean z = true;
            if (!this.mSupportedConfig) {
                return true;
            }
            if (isEnabled() && !getPolicy().getAllowedFeatures().contains(feature)) {
                z = false;
            }
            return z;
        }
    }

    private int findIndexOfStandbyPorts(android.os.IBinder token) {
        for (int i = 0; i < this.mStandbyPortLocks.size(); i++) {
            if (this.mStandbyPortLocks.get(i).getToken() == token) {
                return i;
            }
        }
        return -1;
    }

    void acquireStandbyPorts(android.os.IBinder token, int uid, java.util.List<android.os.PowerManager.LowPowerStandbyPortDescription> ports) {
        validatePorts(ports);
        com.android.server.power.LowPowerStandbyController.StandbyPortsLock standbyPortsLock = new com.android.server.power.LowPowerStandbyController.StandbyPortsLock(token, uid, ports);
        synchronized (this.mLock) {
            if (findIndexOfStandbyPorts(token) != -1) {
                return;
            }
            if (standbyPortsLock.linkToDeath()) {
                this.mStandbyPortLocks.add(standbyPortsLock);
                if (this.mEnableStandbyPorts && isEnabled() && isPackageExempt(uid)) {
                    enqueueNotifyStandbyPortsChangedLocked();
                }
            }
        }
    }

    void validatePorts(java.util.List<android.os.PowerManager.LowPowerStandbyPortDescription> ports) {
        for (android.os.PowerManager.LowPowerStandbyPortDescription portDescription : ports) {
            int port = portDescription.getPortNumber();
            if (port < 0 || port > 65535) {
                throw new java.lang.IllegalArgumentException("port out of range:" + port);
            }
        }
    }

    void releaseStandbyPorts(android.os.IBinder token) {
        synchronized (this.mLock) {
            int index = findIndexOfStandbyPorts(token);
            if (index == -1) {
                return;
            }
            com.android.server.power.LowPowerStandbyController.StandbyPortsLock standbyPortsLock = this.mStandbyPortLocks.remove(index);
            standbyPortsLock.unlinkToDeath();
            if (this.mEnableStandbyPorts && isEnabled() && isPackageExempt(standbyPortsLock.getUid())) {
                enqueueNotifyStandbyPortsChangedLocked();
            }
        }
    }

    java.util.List<android.os.PowerManager.LowPowerStandbyPortDescription> getActiveStandbyPorts() {
        java.util.List<android.os.PowerManager.LowPowerStandbyPortDescription> activeStandbyPorts = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            if (isEnabled() && this.mEnableStandbyPorts) {
                java.util.List<java.lang.Integer> exemptPackageAppIds = getExemptPackageAppIdsLocked();
                for (com.android.server.power.LowPowerStandbyController.StandbyPortsLock standbyPortsLock : this.mStandbyPortLocks) {
                    int standbyPortsAppid = android.os.UserHandle.getAppId(standbyPortsLock.getUid());
                    if (exemptPackageAppIds.contains(java.lang.Integer.valueOf(standbyPortsAppid))) {
                        activeStandbyPorts.addAll(standbyPortsLock.getPorts());
                    }
                }
                return activeStandbyPorts;
            }
            return activeStandbyPorts;
        }
    }

    private boolean policyChangeAffectsAllowlistLocked(android.os.PowerManager.LowPowerStandbyPolicy oldPolicy, android.os.PowerManager.LowPowerStandbyPolicy newPolicy) {
        android.os.PowerManager.LowPowerStandbyPolicy policyA = policyOrDefault(oldPolicy);
        android.os.PowerManager.LowPowerStandbyPolicy policyB = policyOrDefault(newPolicy);
        int allowedReasonsInUse = 0;
        for (int i = 0; i < this.mUidAllowedReasons.size(); i++) {
            allowedReasonsInUse |= this.mUidAllowedReasons.valueAt(i);
        }
        int i2 = policyA.getAllowedReasons();
        int policyAllowedReasonsChanged = i2 ^ policyB.getAllowedReasons();
        boolean exemptPackagesChanged = !policyA.getExemptPackages().equals(policyB.getExemptPackages());
        return (policyAllowedReasonsChanged & allowedReasonsInUse) != 0 || exemptPackagesChanged;
    }

    void dump(java.io.PrintWriter pw) {
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw, "  ");
        ipw.println();
        ipw.println("Low Power Standby Controller:");
        ipw.increaseIndent();
        synchronized (this.mLock) {
            ipw.print("mIsActive=");
            ipw.println(this.mIsActive);
            ipw.print("mIsEnabled=");
            ipw.println(this.mIsEnabled);
            ipw.print("mSupportedConfig=");
            ipw.println(this.mSupportedConfig);
            ipw.print("mEnabledByDefaultConfig=");
            ipw.println(this.mEnabledByDefaultConfig);
            ipw.print("mStandbyTimeoutConfig=");
            ipw.println(this.mStandbyTimeoutConfig);
            ipw.print("mEnableCustomPolicy=");
            ipw.println(this.mEnableCustomPolicy);
            if (this.mIsActive || this.mIsEnabled) {
                ipw.print("mIsInteractive=");
                ipw.println(this.mIsInteractive);
                ipw.print("mLastInteractiveTime=");
                ipw.println(this.mLastInteractiveTimeElapsed);
                ipw.print("mIdleSinceNonInteractive=");
                ipw.println(this.mIdleSinceNonInteractive);
                ipw.print("mIsDeviceIdle=");
                ipw.println(this.mIsDeviceIdle);
            }
            int[] allowlistUids = getAllowlistUidsLocked();
            ipw.print("Allowed UIDs=");
            ipw.println(java.util.Arrays.toString(allowlistUids));
            android.os.PowerManager.LowPowerStandbyPolicy policy = getPolicy();
            if (policy != null) {
                ipw.println();
                ipw.println("mPolicy:");
                ipw.increaseIndent();
                ipw.print("mIdentifier=");
                ipw.println(policy.getIdentifier());
                ipw.print("mExemptPackages=");
                ipw.println(java.lang.String.join(",", policy.getExemptPackages()));
                ipw.print("mAllowedReasons=");
                ipw.println(android.os.PowerManager.lowPowerStandbyAllowedReasonsToString(policy.getAllowedReasons()));
                ipw.print("mAllowedFeatures=");
                ipw.println(java.lang.String.join(",", policy.getAllowedFeatures()));
                ipw.decreaseIndent();
            }
            ipw.println();
            ipw.println("UID allowed reasons:");
            ipw.increaseIndent();
            for (int i = 0; i < this.mUidAllowedReasons.size(); i++) {
                if (this.mUidAllowedReasons.valueAt(i) > 0) {
                    ipw.print(this.mUidAllowedReasons.keyAt(i));
                    ipw.print(": ");
                    ipw.println(android.os.PowerManager.lowPowerStandbyAllowedReasonsToString(this.mUidAllowedReasons.valueAt(i)));
                }
            }
            ipw.decreaseIndent();
            java.util.List<android.os.PowerManager.LowPowerStandbyPortDescription> activeStandbyPorts = getActiveStandbyPorts();
            if (!activeStandbyPorts.isEmpty()) {
                ipw.println();
                ipw.println("Active standby ports locks:");
                ipw.increaseIndent();
                for (android.os.PowerManager.LowPowerStandbyPortDescription portDescription : activeStandbyPorts) {
                    ipw.print(portDescription.toString());
                }
                ipw.decreaseIndent();
            }
        }
        ipw.decreaseIndent();
    }

    void dumpProto(android.util.proto.ProtoOutputStream proto, long tag) {
        synchronized (this.mLock) {
            long token = proto.start(tag);
            proto.write(1133871366145L, this.mIsActive);
            proto.write(1133871366146L, this.mIsEnabled);
            proto.write(1133871366147L, this.mSupportedConfig);
            proto.write(1133871366148L, this.mEnabledByDefaultConfig);
            proto.write(1133871366149L, this.mIsInteractive);
            proto.write(1112396529670L, this.mLastInteractiveTimeElapsed);
            proto.write(1120986464263L, this.mStandbyTimeoutConfig);
            proto.write(1133871366152L, this.mIdleSinceNonInteractive);
            proto.write(1133871366153L, this.mIsDeviceIdle);
            int[] allowlistUids = getAllowlistUidsLocked();
            for (int appId : allowlistUids) {
                proto.write(2220498092042L, appId);
            }
            android.os.PowerManager.LowPowerStandbyPolicy policy = getPolicy();
            if (policy != null) {
                long policyToken = proto.start(1146756268043L);
                proto.write(1138166333441L, policy.getIdentifier());
                for (java.lang.String exemptPackage : policy.getExemptPackages()) {
                    proto.write(2237677961218L, exemptPackage);
                }
                proto.write(1120986464259L, policy.getAllowedReasons());
                for (java.lang.String feature : policy.getAllowedFeatures()) {
                    proto.write(2237677961220L, feature);
                }
                proto.end(policyToken);
            }
            proto.end(token);
        }
    }

    private class LowPowerStandbyHandler extends android.os.Handler {
        LowPowerStandbyHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    com.android.server.power.LowPowerStandbyController.this.onStandbyTimeoutExpired();
                    break;
                case 1:
                    boolean active = ((java.lang.Boolean) msg.obj).booleanValue();
                    com.android.server.power.LowPowerStandbyController.this.notifyActiveChanged(active);
                    break;
                case 2:
                    int[] allowlistUids = (int[]) msg.obj;
                    com.android.server.power.LowPowerStandbyController.this.notifyAllowlistChanged(allowlistUids);
                    break;
                case 3:
                    com.android.server.power.LowPowerStandbyController.this.notifyPolicyChanged((android.os.PowerManager.LowPowerStandbyPolicy) msg.obj);
                    break;
                case 4:
                    int uid = msg.arg1;
                    com.android.server.power.LowPowerStandbyController.this.mPhoneCallServiceTracker.foregroundServiceStateChanged(uid);
                    break;
                case 5:
                    com.android.server.power.LowPowerStandbyController.this.notifyStandbyPortsChanged();
                    break;
            }
        }
    }

    private boolean hasAllowedReasonLocked(int uid, int allowedReason) {
        int allowedReasons = this.mUidAllowedReasons.get(uid);
        return (allowedReasons & allowedReason) != 0;
    }

    private boolean addAllowedReasonLocked(int uid, int allowedReason) {
        int allowedReasons = this.mUidAllowedReasons.get(uid);
        int newAllowReasons = allowedReasons | allowedReason;
        this.mUidAllowedReasons.put(uid, newAllowReasons);
        return allowedReasons != newAllowReasons;
    }

    private boolean removeAllowedReasonLocked(int uid, int allowedReason) {
        int allowedReasons = this.mUidAllowedReasons.get(uid);
        if (allowedReasons == 0) {
            return false;
        }
        int newAllowedReasons = (~allowedReason) & allowedReasons;
        if (newAllowedReasons == 0) {
            this.mUidAllowedReasons.removeAt(this.mUidAllowedReasons.indexOfKey(uid));
        } else {
            this.mUidAllowedReasons.put(uid, newAllowedReasons);
        }
        return allowedReasons != newAllowedReasons;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addToAllowlistInternal(int uid, int allowedReason) {
        synchronized (this.mLock) {
            if (this.mSupportedConfig) {
                if (allowedReason != 0 && !hasAllowedReasonLocked(uid, allowedReason)) {
                    addAllowedReasonLocked(uid, allowedReason);
                    if ((getPolicy().getAllowedReasons() & allowedReason) != 0) {
                        enqueueNotifyAllowlistChangedLocked();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFromAllowlistInternal(int uid, int allowedReason) {
        synchronized (this.mLock) {
            if (this.mSupportedConfig) {
                if (allowedReason != 0 && hasAllowedReasonLocked(uid, allowedReason)) {
                    removeAllowedReasonLocked(uid, allowedReason);
                    if ((getPolicy().getAllowedReasons() & allowedReason) != 0) {
                        enqueueNotifyAllowlistChangedLocked();
                    }
                }
            }
        }
    }

    private java.util.List<java.lang.Integer> getExemptPackageAppIdsLocked() {
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        android.os.PowerManager.LowPowerStandbyPolicy policy = getPolicy();
        java.util.List<java.lang.Integer> appIds = new java.util.ArrayList<>();
        if (policy == null) {
            return appIds;
        }
        for (java.lang.String packageName : policy.getExemptPackages()) {
            try {
                int packageUid = packageManager.getPackageUid(packageName, android.content.pm.PackageManager.PackageInfoFlags.of(0L));
                int appId = android.os.UserHandle.getAppId(packageUid);
                appIds.add(java.lang.Integer.valueOf(appId));
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }
        return appIds;
    }

    private int[] getAllowlistUidsLocked() {
        java.util.List userHandles = ((android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class)).getUserHandles(true);
        android.util.ArraySet<java.lang.Integer> uids = new android.util.ArraySet<>(this.mUidAllowedReasons.size());
        android.os.PowerManager.LowPowerStandbyPolicy policy = getPolicy();
        if (policy == null) {
            return new int[0];
        }
        int policyAllowedReasons = policy.getAllowedReasons();
        for (int i = 0; i < this.mUidAllowedReasons.size(); i++) {
            java.lang.Integer uid = java.lang.Integer.valueOf(this.mUidAllowedReasons.keyAt(i));
            if ((this.mUidAllowedReasons.valueAt(i) & policyAllowedReasons) != 0) {
                uids.add(uid);
            }
        }
        java.util.Iterator<java.lang.Integer> it = getExemptPackageAppIdsLocked().iterator();
        while (it.hasNext()) {
            int appId = it.next().intValue();
            for (int uid2 : uidsForAppId(appId, userHandles)) {
                uids.add(java.lang.Integer.valueOf(uid2));
            }
        }
        int[] allowlistUids = new int[uids.size()];
        for (int i2 = 0; i2 < uids.size(); i2++) {
            allowlistUids[i2] = uids.valueAt(i2).intValue();
        }
        java.util.Arrays.sort(allowlistUids);
        return allowlistUids;
    }

    private int[] uidsForAppId(int appUid, java.util.List<android.os.UserHandle> userHandles) {
        int appId = android.os.UserHandle.getAppId(appUid);
        int[] uids = new int[userHandles.size()];
        for (int i = 0; i < userHandles.size(); i++) {
            uids[i] = userHandles.get(i).getUid(appId);
        }
        return uids;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enqueueNotifyAllowlistChangedLocked() {
        int[] allowlistUids = getAllowlistUidsLocked();
        android.os.Message msg = this.mHandler.obtainMessage(2, allowlistUids);
        this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAllowlistChanged(int[] allowlistUids) {
        android.os.PowerManagerInternal pmi = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
        com.android.server.net.NetworkPolicyManagerInternal npmi = (com.android.server.net.NetworkPolicyManagerInternal) com.android.server.LocalServices.getService(com.android.server.net.NetworkPolicyManagerInternal.class);
        pmi.setLowPowerStandbyAllowlist(allowlistUids);
        npmi.setLowPowerStandbyAllowlist(allowlistUids);
    }

    private void enqueueNotifyStandbyPortsChangedLocked() {
        android.os.Message msg = this.mHandler.obtainMessage(5);
        this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStandbyPortsChanged() {
        android.content.Intent intent = new android.content.Intent("android.os.action.LOW_POWER_STANDBY_PORTS_CHANGED");
        intent.addFlags(268435456);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL, "android.permission.MANAGE_LOW_POWER_STANDBY");
    }

    public static class DeviceConfigWrapper {
        public static final java.lang.String FEATURE_FLAG_ENABLE_POLICY = "enable_policy";
        public static final java.lang.String FEATURE_FLAG_ENABLE_STANDBY_PORTS = "enable_standby_ports";
        public static final java.lang.String NAMESPACE = "low_power_standby";

        public boolean enableCustomPolicy() {
            return android.provider.DeviceConfig.getBoolean(NAMESPACE, FEATURE_FLAG_ENABLE_POLICY, true);
        }

        public boolean enableStandbyPorts() {
            return android.provider.DeviceConfig.getBoolean(NAMESPACE, FEATURE_FLAG_ENABLE_STANDBY_PORTS, true);
        }

        public void registerPropertyUpdateListener(java.util.concurrent.Executor executor, android.provider.DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener) {
            android.provider.DeviceConfig.addOnPropertiesChangedListener(NAMESPACE, executor, onPropertiesChangedListener);
        }
    }

    private final class LocalService extends com.android.server.power.LowPowerStandbyControllerInternal {
        private LocalService() {
        }

        @Override // com.android.server.power.LowPowerStandbyControllerInternal
        public void addToAllowlist(int uid, int allowedReason) {
            com.android.server.power.LowPowerStandbyController.this.addToAllowlistInternal(uid, allowedReason);
        }

        @Override // com.android.server.power.LowPowerStandbyControllerInternal
        public void removeFromAllowlist(int uid, int allowedReason) {
            com.android.server.power.LowPowerStandbyController.this.removeFromAllowlistInternal(uid, allowedReason);
        }
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver(android.os.Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            com.android.server.power.LowPowerStandbyController.this.onSettingsChanged();
        }
    }

    final class TempAllowlistChangeListener implements com.android.server.PowerAllowlistInternal.TempAllowlistChangeListener {
        TempAllowlistChangeListener() {
        }

        public void onAppAdded(int uid) {
            com.android.server.power.LowPowerStandbyController.this.addToAllowlistInternal(uid, 2);
        }

        public void onAppRemoved(int uid) {
            com.android.server.power.LowPowerStandbyController.this.removeFromAllowlistInternal(uid, 2);
        }
    }

    final class PhoneCallServiceTracker extends android.app.IForegroundServiceObserver.Stub {
        private boolean mRegistered = false;
        private final android.util.SparseBooleanArray mUidsWithPhoneCallService = new android.util.SparseBooleanArray();

        PhoneCallServiceTracker() {
        }

        public void register() {
            if (this.mRegistered) {
                return;
            }
            try {
                ((android.app.IActivityManager) com.android.server.power.LowPowerStandbyController.this.mActivityManager.get()).registerForegroundServiceObserver(this);
                this.mRegistered = true;
            } catch (android.os.RemoteException e) {
            }
        }

        public void onForegroundStateChanged(android.os.IBinder serviceToken, java.lang.String packageName, int userId, boolean isForeground) {
            try {
                int uid = com.android.server.power.LowPowerStandbyController.this.mContext.getPackageManager().getPackageUidAsUser(packageName, userId);
                android.os.Message message = com.android.server.power.LowPowerStandbyController.this.mHandler.obtainMessage(4, uid, 0);
                com.android.server.power.LowPowerStandbyController.this.mHandler.sendMessageAtTime(message, com.android.server.power.LowPowerStandbyController.this.mClock.uptimeMillis());
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }

        public void foregroundServiceStateChanged(int uid) {
            boolean hadPhoneCallService = this.mUidsWithPhoneCallService.get(uid);
            boolean hasPhoneCallService = com.android.server.power.LowPowerStandbyController.this.mActivityManagerInternal.hasRunningForegroundService(uid, 4);
            if (hasPhoneCallService == hadPhoneCallService) {
                return;
            }
            if (hasPhoneCallService) {
                this.mUidsWithPhoneCallService.append(uid, true);
                uidStartedPhoneCallService(uid);
            } else {
                this.mUidsWithPhoneCallService.delete(uid);
                uidStoppedPhoneCallService(uid);
            }
        }

        private void uidStartedPhoneCallService(int uid) {
            com.android.server.power.LowPowerStandbyController.this.addToAllowlistInternal(uid, 4);
        }

        private void uidStoppedPhoneCallService(int uid) {
            com.android.server.power.LowPowerStandbyController.this.removeFromAllowlistInternal(uid, 4);
        }
    }
}
