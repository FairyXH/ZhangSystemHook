package com.android.server.power.batterysaver;

/* JADX INFO: loaded from: classes3.dex */
public class BatterySaverController implements com.android.server.power.batterysaver.BatterySaverPolicy.BatterySaverPolicyListener {
    static final boolean DEBUG = false;
    public static final int REASON_ADAPTIVE_DYNAMIC_POWER_SAVINGS_CHANGED = 11;
    public static final int REASON_DYNAMIC_POWER_SAVINGS_AUTOMATIC_OFF = 10;
    public static final int REASON_DYNAMIC_POWER_SAVINGS_AUTOMATIC_ON = 9;
    public static final int REASON_FULL_POWER_SAVINGS_CHANGED = 13;
    public static final int REASON_INTERACTIVE_CHANGED = 5;
    public static final int REASON_MANUAL_OFF = 3;
    public static final int REASON_MANUAL_ON = 2;
    public static final int REASON_PERCENTAGE_AUTOMATIC_OFF = 1;
    public static final int REASON_PERCENTAGE_AUTOMATIC_ON = 0;
    public static final int REASON_PLUGGED_IN = 7;
    public static final int REASON_POLICY_CHANGED = 6;
    public static final int REASON_SETTING_CHANGED = 8;
    public static final int REASON_STICKY_RESTORE = 4;
    public static final int REASON_TIMEOUT = 12;
    static final java.lang.String TAG = "BatterySaverController";
    private boolean mAdaptiveEnabledRaw;
    private boolean mAdaptivePreviouslyEnabled;
    private final com.android.server.power.batterysaver.BatterySaverPolicy mBatterySaverPolicy;
    private final com.android.server.power.batterysaver.BatterySavingStats mBatterySavingStats;
    private final android.content.Context mContext;
    private boolean mFullEnabledRaw;
    private boolean mFullPreviouslyEnabled;
    private final com.android.server.power.batterysaver.BatterySaverController.MyHandler mHandler;
    private boolean mIsInteractive;
    private boolean mIsPluggedIn;
    private final java.lang.Object mLock;
    private android.os.PowerManager mPowerManager;
    private java.util.Optional<java.lang.String> mPowerSaveModeChangedListenerPackage;
    private final java.util.ArrayList<android.os.PowerManagerInternal.LowPowerModeListener> mListeners = new java.util.ArrayList<>();
    private final android.content.BroadcastReceiver mReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.power.batterysaver.BatterySaverController.1
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0040  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r6, android.content.Intent r7) {
            /*
                r5 = this;
                java.lang.String r0 = r7.getAction()
                int r1 = r0.hashCode()
                r2 = 1
                r3 = 0
                switch(r1) {
                    case -2128145023: goto L36;
                    case -1538406691: goto L2c;
                    case -1454123155: goto L22;
                    case 498807504: goto L18;
                    case 870701415: goto Le;
                    default: goto Ld;
                }
            Ld:
                goto L40
            Le:
                java.lang.String r1 = "android.os.action.DEVICE_IDLE_MODE_CHANGED"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Ld
                r0 = 3
                goto L41
            L18:
                java.lang.String r1 = "android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Ld
                r0 = 4
                goto L41
            L22:
                java.lang.String r1 = "android.intent.action.SCREEN_ON"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Ld
                r0 = r3
                goto L41
            L2c:
                java.lang.String r1 = "android.intent.action.BATTERY_CHANGED"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Ld
                r0 = 2
                goto L41
            L36:
                java.lang.String r1 = "android.intent.action.SCREEN_OFF"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Ld
                r0 = r2
                goto L41
            L40:
                r0 = -1
            L41:
                switch(r0) {
                    case 0: goto L66;
                    case 1: goto L66;
                    case 2: goto L45;
                    case 3: goto L5d;
                    case 4: goto L5d;
                    default: goto L44;
                }
            L44:
                goto L7f
            L45:
                com.android.server.power.batterysaver.BatterySaverController r0 = com.android.server.power.batterysaver.BatterySaverController.this
                java.lang.Object r0 = com.android.server.power.batterysaver.BatterySaverController.m8453$$Nest$fgetmLock(r0)
                monitor-enter(r0)
                com.android.server.power.batterysaver.BatterySaverController r1 = com.android.server.power.batterysaver.BatterySaverController.this     // Catch: java.lang.Throwable -> L63
                java.lang.String r4 = "plugged"
                int r4 = r7.getIntExtra(r4, r3)     // Catch: java.lang.Throwable -> L63
                if (r4 == 0) goto L58
                goto L59
            L58:
                r2 = r3
            L59:
                com.android.server.power.batterysaver.BatterySaverController.m8454$$Nest$fputmIsPluggedIn(r1, r2)     // Catch: java.lang.Throwable -> L63
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L63
            L5d:
                com.android.server.power.batterysaver.BatterySaverController r0 = com.android.server.power.batterysaver.BatterySaverController.this
                com.android.server.power.batterysaver.BatterySaverController.m8456$$Nest$mupdateBatterySavingStats(r0)
                goto L7f
            L63:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L63
                throw r1
            L66:
                com.android.server.power.batterysaver.BatterySaverController r0 = com.android.server.power.batterysaver.BatterySaverController.this
                boolean r0 = com.android.server.power.batterysaver.BatterySaverController.m8455$$Nest$misPolicyEnabled(r0)
                if (r0 != 0) goto L74
                com.android.server.power.batterysaver.BatterySaverController r0 = com.android.server.power.batterysaver.BatterySaverController.this
                com.android.server.power.batterysaver.BatterySaverController.m8456$$Nest$mupdateBatterySavingStats(r0)
                return
            L74:
                com.android.server.power.batterysaver.BatterySaverController r0 = com.android.server.power.batterysaver.BatterySaverController.this
                com.android.server.power.batterysaver.BatterySaverController$MyHandler r0 = com.android.server.power.batterysaver.BatterySaverController.m8452$$Nest$fgetmHandler(r0)
                r1 = 5
                r0.postStateChanged(r3, r1)
            L7f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.batterysaver.BatterySaverController.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
        }
    };

    static java.lang.String reasonToString(int reason) {
        switch (reason) {
            case 0:
                return "Percentage Auto ON";
            case 1:
                return "Percentage Auto OFF";
            case 2:
                return "Manual ON";
            case 3:
                return "Manual OFF";
            case 4:
                return "Sticky restore";
            case 5:
                return "Interactivity changed";
            case 6:
                return "Policy changed";
            case 7:
                return "Plugged in";
            case 8:
                return "Setting changed";
            case 9:
                return "Dynamic Warning Auto ON";
            case 10:
                return "Dynamic Warning Auto OFF";
            case 11:
                return "Adaptive Power Savings changed";
            case 12:
                return "timeout";
            case 13:
                return "Full Power Savings changed";
            default:
                return "Unknown reason: " + reason;
        }
    }

    public BatterySaverController(java.lang.Object lock, android.content.Context context, android.os.Looper looper, com.android.server.power.batterysaver.BatterySaverPolicy policy, com.android.server.power.batterysaver.BatterySavingStats batterySavingStats) {
        this.mLock = lock;
        this.mContext = context;
        this.mHandler = new com.android.server.power.batterysaver.BatterySaverController.MyHandler(looper);
        this.mBatterySaverPolicy = policy;
        this.mBatterySaverPolicy.addListener(this);
        this.mBatterySavingStats = batterySavingStats;
        android.os.PowerManager.invalidatePowerSaveModeCaches();
    }

    public void addListener(android.os.PowerManagerInternal.LowPowerModeListener listener) {
        synchronized (this.mLock) {
            this.mListeners.add(listener);
        }
    }

    public void systemReady() {
        android.content.IntentFilter filter = new android.content.IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        filter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
        filter.addAction("android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED");
        filter.addCategory("oplusBrEx@android.intent.action.BATTERY_CHANGED@BATTERYSTATE=CHARGING_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, filter);
        this.mHandler.postSystemReady();
    }

    private android.os.PowerManager getPowerManager() {
        if (this.mPowerManager == null) {
            this.mPowerManager = (android.os.PowerManager) java.util.Objects.requireNonNull((android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class));
        }
        return this.mPowerManager;
    }

    @Override // com.android.server.power.batterysaver.BatterySaverPolicy.BatterySaverPolicyListener
    public void onBatterySaverPolicyChanged(com.android.server.power.batterysaver.BatterySaverPolicy policy) {
        if (!isPolicyEnabled()) {
            return;
        }
        this.mHandler.postStateChanged(true, 6);
    }

    private class MyHandler extends android.os.Handler {
        private static final int ARG_DONT_SEND_BROADCAST = 0;
        private static final int ARG_SEND_BROADCAST = 1;
        private static final int MSG_STATE_CHANGED = 1;
        private static final int MSG_SYSTEM_READY = 2;

        public MyHandler(android.os.Looper looper) {
            super(looper);
        }

        void postStateChanged(boolean sendBroadcast, int reason) {
            obtainMessage(1, sendBroadcast ? 1 : 0, reason).sendToTarget();
        }

        public void postSystemReady() {
            obtainMessage(2, 0, 0).sendToTarget();
        }

        @Override // android.os.Handler
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.power.batterysaver.BatterySaverController.this.handleBatterySaverStateChanged(msg.arg1 == 1, msg.arg2);
                    break;
            }
        }
    }

    public void enableBatterySaver(boolean enable, int reason) {
        synchronized (this.mLock) {
            if (getFullEnabledLocked() == enable) {
                return;
            }
            setFullEnabledLocked(enable);
            if (updatePolicyLevelLocked()) {
                this.mHandler.postStateChanged(true, reason);
            }
        }
    }

    private boolean updatePolicyLevelLocked() {
        if (getFullEnabledLocked()) {
            return this.mBatterySaverPolicy.setPolicyLevel(2);
        }
        if (getAdaptiveEnabledLocked()) {
            return this.mBatterySaverPolicy.setPolicyLevel(1);
        }
        return this.mBatterySaverPolicy.setPolicyLevel(0);
    }

    android.os.BatterySaverPolicyConfig getPolicyLocked(int policyLevel) {
        return this.mBatterySaverPolicy.getPolicyLocked(policyLevel).toConfig();
    }

    public boolean isEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = getFullEnabledLocked() || (getAdaptiveEnabledLocked() && this.mBatterySaverPolicy.shouldAdvertiseIsEnabled());
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPolicyEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = getFullEnabledLocked() || getAdaptiveEnabledLocked();
        }
        return z;
    }

    boolean isFullEnabled() {
        boolean fullEnabledLocked;
        synchronized (this.mLock) {
            fullEnabledLocked = getFullEnabledLocked();
        }
        return fullEnabledLocked;
    }

    boolean setFullPolicyLocked(android.os.BatterySaverPolicyConfig config, int reason) {
        return setFullPolicyLocked(com.android.server.power.batterysaver.BatterySaverPolicy.Policy.fromConfig(config), reason);
    }

    boolean setFullPolicyLocked(com.android.server.power.batterysaver.BatterySaverPolicy.Policy policy, int reason) {
        if (this.mBatterySaverPolicy.setFullPolicyLocked(policy)) {
            this.mHandler.postStateChanged(true, reason);
            return true;
        }
        return false;
    }

    boolean isAdaptiveEnabled() {
        boolean adaptiveEnabledLocked;
        synchronized (this.mLock) {
            adaptiveEnabledLocked = getAdaptiveEnabledLocked();
        }
        return adaptiveEnabledLocked;
    }

    boolean setAdaptivePolicyLocked(android.os.BatterySaverPolicyConfig config, int reason) {
        return setAdaptivePolicyLocked(com.android.server.power.batterysaver.BatterySaverPolicy.Policy.fromConfig(config), reason);
    }

    boolean setAdaptivePolicyLocked(com.android.server.power.batterysaver.BatterySaverPolicy.Policy policy, int reason) {
        if (this.mBatterySaverPolicy.setAdaptivePolicyLocked(policy)) {
            this.mHandler.postStateChanged(true, reason);
            return true;
        }
        return false;
    }

    boolean resetAdaptivePolicyLocked(int reason) {
        if (this.mBatterySaverPolicy.resetAdaptivePolicyLocked()) {
            this.mHandler.postStateChanged(true, reason);
            return true;
        }
        return false;
    }

    boolean setAdaptivePolicyEnabledLocked(boolean enabled, int reason) {
        if (getAdaptiveEnabledLocked() == enabled) {
            return false;
        }
        setAdaptiveEnabledLocked(enabled);
        if (!updatePolicyLevelLocked()) {
            return false;
        }
        this.mHandler.postStateChanged(true, reason);
        return true;
    }

    public boolean isInteractive() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsInteractive;
        }
        return z;
    }

    public com.android.server.power.batterysaver.BatterySaverPolicy getBatterySaverPolicy() {
        return this.mBatterySaverPolicy;
    }

    public boolean isLaunchBoostDisabled() {
        return isPolicyEnabled() && this.mBatterySaverPolicy.isLaunchBoostDisabled();
    }

    /* JADX WARN: Multi-variable type inference failed */
    void handleBatterySaverStateChanged(boolean z, int i) {
        int i2;
        android.os.PowerManagerInternal.LowPowerModeListener[] lowPowerModeListenerArr;
        boolean zIsInteractive = getPowerManager().isInteractive();
        synchronized (this.mLock) {
            java.lang.Object[] objArr = getFullEnabledLocked() || getAdaptiveEnabledLocked();
            com.android.server.EventLogTags.writeBatterySaverMode(this.mFullPreviouslyEnabled ? 1 : 0, this.mAdaptivePreviouslyEnabled ? 1 : 0, getFullEnabledLocked() ? 1 : 0, getAdaptiveEnabledLocked() ? 1 : 0, zIsInteractive ? 1 : 0, objArr != false ? this.mBatterySaverPolicy.toEventLogString() : "", i);
            this.mFullPreviouslyEnabled = getFullEnabledLocked();
            this.mAdaptivePreviouslyEnabled = getAdaptiveEnabledLocked();
            lowPowerModeListenerArr = (android.os.PowerManagerInternal.LowPowerModeListener[]) this.mListeners.toArray(new android.os.PowerManagerInternal.LowPowerModeListener[0]);
            this.mIsInteractive = zIsInteractive;
        }
        android.os.PowerManagerInternal powerManagerInternal = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
        if (powerManagerInternal != null) {
            powerManagerInternal.setPowerMode(1, isEnabled());
        }
        updateBatterySavingStats();
        if (z) {
            android.util.Slog.i(TAG, "Sending broadcasts for mode: " + isEnabled());
            android.content.Intent intent = new android.content.Intent("android.os.action.POWER_SAVE_MODE_CHANGED");
            intent.addFlags(1073741824);
            this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL);
            if (getPowerSaveModeChangedListenerPackage().isPresent()) {
                this.mContext.sendBroadcastAsUser(new android.content.Intent("android.os.action.POWER_SAVE_MODE_CHANGED").setPackage(getPowerSaveModeChangedListenerPackage().get()).addFlags(android.hardware.audio.common.V2_0.AudioFormat.EVRCB), android.os.UserHandle.ALL);
            }
            android.content.Intent intent2 = new android.content.Intent("android.os.action.POWER_SAVE_MODE_CHANGED_INTERNAL");
            intent2.addFlags(1073741824);
            this.mContext.sendBroadcastAsUser(intent2, android.os.UserHandle.ALL, "android.permission.DEVICE_POWER");
            for (android.os.PowerManagerInternal.LowPowerModeListener lowPowerModeListener : lowPowerModeListenerArr) {
                lowPowerModeListener.onLowPowerModeChanged(this.mBatterySaverPolicy.getBatterySaverPolicy(lowPowerModeListener.getServiceType()));
            }
        }
    }

    private java.util.Optional<java.lang.String> getPowerSaveModeChangedListenerPackage() {
        java.util.Optional<java.lang.String> optionalEmpty;
        if (this.mPowerSaveModeChangedListenerPackage == null) {
            java.lang.String configPowerSaveModeChangedListenerPackage = this.mContext.getString(android.R.string.config_rawContactsLocalAccountName);
            if (((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).isSystemPackage(configPowerSaveModeChangedListenerPackage)) {
                optionalEmpty = java.util.Optional.of(configPowerSaveModeChangedListenerPackage);
            } else {
                optionalEmpty = java.util.Optional.empty();
            }
            this.mPowerSaveModeChangedListenerPackage = optionalEmpty;
        }
        return this.mPowerSaveModeChangedListenerPackage;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBatterySavingStats() {
        int dozeMode;
        android.os.PowerManager pm = getPowerManager();
        if (pm == null) {
            android.util.Slog.wtf(TAG, "PowerManager not initialized");
            return;
        }
        boolean isInteractive = pm.isInteractive();
        int i = 2;
        int i2 = 1;
        if (pm.isDeviceIdleMode()) {
            dozeMode = 2;
        } else {
            dozeMode = pm.isLightDeviceIdleMode() ? 1 : 0;
        }
        synchronized (this.mLock) {
            com.android.server.power.batterysaver.BatterySavingStats batterySavingStats = this.mBatterySavingStats;
            if (getFullEnabledLocked()) {
                i = 1;
            } else if (!getAdaptiveEnabledLocked()) {
                i = 0;
            }
            int i3 = isInteractive ? 1 : 0;
            if (!this.mIsPluggedIn) {
                i2 = 0;
            }
            batterySavingStats.transitionState(i, i3, dozeMode, i2);
        }
    }

    private void setFullEnabledLocked(boolean value) {
        if (this.mFullEnabledRaw == value) {
            return;
        }
        android.os.PowerManager.invalidatePowerSaveModeCaches();
        this.mFullEnabledRaw = value;
    }

    private boolean getFullEnabledLocked() {
        return this.mFullEnabledRaw;
    }

    private void setAdaptiveEnabledLocked(boolean value) {
        if (this.mAdaptiveEnabledRaw == value) {
            return;
        }
        android.os.PowerManager.invalidatePowerSaveModeCaches();
        this.mAdaptiveEnabledRaw = value;
    }

    private boolean getAdaptiveEnabledLocked() {
        return this.mAdaptiveEnabledRaw;
    }
}
