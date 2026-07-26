package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class BlurController {
    private static final java.lang.String TAG = "BlurController";
    private boolean mBlurDisabledSetting;
    private volatile boolean mBlurEnabled;
    private final android.content.Context mContext;
    private boolean mCriticalThermalStatus;
    private boolean mInPowerSaveMode;
    private final android.os.RemoteCallbackList<android.view.ICrossWindowBlurEnabledListener> mBlurEnabledListeners = new android.os.RemoteCallbackList<>();
    private final java.lang.Object mLock = new java.lang.Object();
    private boolean mTunnelModeEnabled = false;
    private android.view.TunnelModeEnabledListener mTunnelModeListener = new android.view.TunnelModeEnabledListener(new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0()) { // from class: com.android.server.wm.BlurController.1
        public void onTunnelModeEnabledChanged(boolean tunnelModeEnabled) {
            com.android.server.wm.BlurController.this.mTunnelModeEnabled = tunnelModeEnabled;
            com.android.server.wm.BlurController.this.updateBlurEnabled();
        }
    };

    BlurController(android.content.Context context, final android.os.PowerManager powerManager) {
        this.mContext = context;
        android.content.IntentFilter filter = new android.content.IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
        context.registerReceiverForAllUsers(new android.content.BroadcastReceiver() { // from class: com.android.server.wm.BlurController.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if ("android.os.action.POWER_SAVE_MODE_CHANGED".equals(intent.getAction())) {
                    com.android.server.wm.BlurController.this.mInPowerSaveMode = powerManager.isPowerSaveMode();
                    com.android.server.wm.BlurController.this.updateBlurEnabled();
                }
            }
        }, filter, null, null);
        this.mInPowerSaveMode = powerManager.isPowerSaveMode();
        context.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("disable_window_blurs"), false, new android.database.ContentObserver(null) { // from class: com.android.server.wm.BlurController.3
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                com.android.server.wm.BlurController.this.mBlurDisabledSetting = com.android.server.wm.BlurController.this.getBlurDisabledSetting();
                com.android.server.wm.BlurController.this.updateBlurEnabled();
            }
        });
        this.mBlurDisabledSetting = getBlurDisabledSetting();
        powerManager.addThermalStatusListener(new android.os.PowerManager.OnThermalStatusChangedListener() { // from class: com.android.server.wm.BlurController$$ExternalSyntheticLambda0
            @Override // android.os.PowerManager.OnThermalStatusChangedListener
            public final void onThermalStatusChanged(int i) {
                this.f$0.lambda$new$0(i);
            }
        });
        this.mCriticalThermalStatus = powerManager.getCurrentThermalStatus() >= 4;
        android.view.TunnelModeEnabledListener.register(this.mTunnelModeListener);
        updateBlurEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int status) {
        this.mCriticalThermalStatus = status >= 4;
        updateBlurEnabled();
    }

    boolean registerCrossWindowBlurEnabledListener(android.view.ICrossWindowBlurEnabledListener listener) {
        if (listener == null) {
            return false;
        }
        this.mBlurEnabledListeners.register(listener);
        return getBlurEnabled();
    }

    void unregisterCrossWindowBlurEnabledListener(android.view.ICrossWindowBlurEnabledListener listener) {
        if (listener == null) {
            return;
        }
        this.mBlurEnabledListeners.unregister(listener);
    }

    boolean getBlurEnabled() {
        return this.mBlurEnabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBlurEnabled() {
        synchronized (this.mLock) {
            boolean newEnabled = (!android.view.CrossWindowBlurListeners.CROSS_WINDOW_BLUR_SUPPORTED || this.mBlurDisabledSetting || this.mInPowerSaveMode || this.mTunnelModeEnabled || this.mCriticalThermalStatus) ? false : true;
            if (this.mBlurEnabled == newEnabled) {
                return;
            }
            this.mBlurEnabled = newEnabled;
            notifyBlurEnabledChangedLocked(newEnabled);
            android.util.Slog.d(TAG, "updateBlurEnabled, mBlurEnabled = " + this.mBlurEnabled + " ,mBlurDisabledSetting: " + this.mBlurDisabledSetting + " ,mInPowerSaveMode: " + this.mInPowerSaveMode + " ,mTunnelModeEnabled: " + this.mTunnelModeEnabled + " ,mCriticalThermalStatus: " + this.mCriticalThermalStatus);
        }
    }

    private void notifyBlurEnabledChangedLocked(boolean enabled) {
        int i = this.mBlurEnabledListeners.beginBroadcast();
        while (i > 0) {
            i--;
            android.view.ICrossWindowBlurEnabledListener listener = this.mBlurEnabledListeners.getBroadcastItem(i);
            try {
                listener.onCrossWindowBlurEnabledChanged(enabled);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mBlurEnabledListeners.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getBlurDisabledSetting() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "disable_window_blurs", 0) == 1;
    }
}
