package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class DataConnectionStats extends android.content.BroadcastReceiver {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "DataConnectionStats";
    private final android.content.Context mContext;
    private final android.os.Handler mListenerHandler;
    private final android.telephony.PhoneStateListener mPhoneStateListener;
    private android.telephony.ServiceState mServiceState;
    private android.telephony.SignalStrength mSignalStrength;
    private int mSimState = 5;
    private int mDataState = 0;
    private int mNrState = 0;
    private final com.android.internal.app.IBatteryStats mBatteryStats = com.android.server.am.BatteryStatsService.getService();

    public DataConnectionStats(android.content.Context context, android.os.Handler listenerHandler) {
        this.mContext = context;
        this.mListenerHandler = listenerHandler;
        this.mPhoneStateListener = new com.android.server.am.DataConnectionStats.PhoneStateListenerImpl(new com.android.server.am.DataConnectionStats.PhoneStateListenerExecutor(listenerHandler));
    }

    public void startMonitoring() {
        android.telephony.TelephonyManager phone = (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class);
        phone.listen(this.mPhoneStateListener, com.android.internal.util.FrameworkStatsLog.DREAM_UI_EVENT_REPORTED);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.mContext.registerReceiver(this, filter, null, this.mListenerHandler);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        java.lang.String action = intent.getAction();
        if (action.equals("android.intent.action.SIM_STATE_CHANGED")) {
            updateSimState(intent);
            notePhoneDataConnectionState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notePhoneDataConnectionState() {
        if (this.mServiceState == null) {
            return;
        }
        boolean simReadyOrUnknown = this.mSimState == 5 || this.mSimState == 0;
        boolean visible = (simReadyOrUnknown || isCdma()) && hasService() && this.mDataState == 2;
        android.telephony.NetworkRegistrationInfo regInfo = this.mServiceState.getNetworkRegistrationInfo(2, 1);
        int networkType = regInfo == null ? 0 : regInfo.getAccessNetworkTechnology();
        try {
            this.mBatteryStats.notePhoneDataConnectionState(networkType, visible, this.mServiceState.getState(), this.mNrState, this.mServiceState.getNrFrequencyRange());
        } catch (android.os.RemoteException e) {
            android.util.Log.w(TAG, "Error noting data connection state", e);
        }
    }

    private void updateSimState(android.content.Intent intent) {
        java.lang.String stateExtra = intent.getStringExtra("ss");
        if ("ABSENT".equals(stateExtra)) {
            this.mSimState = 1;
            return;
        }
        if ("READY".equals(stateExtra)) {
            this.mSimState = 5;
            return;
        }
        if ("LOCKED".equals(stateExtra)) {
            java.lang.String lockedReason = intent.getStringExtra(com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY);
            if ("PIN".equals(lockedReason)) {
                this.mSimState = 2;
                return;
            } else if ("PUK".equals(lockedReason)) {
                this.mSimState = 3;
                return;
            } else {
                this.mSimState = 4;
                return;
            }
        }
        this.mSimState = 0;
    }

    private boolean isCdma() {
        return (this.mSignalStrength == null || this.mSignalStrength.isGsm()) ? false : true;
    }

    private boolean hasService() {
        return (this.mServiceState == null || this.mServiceState.getState() == 1 || this.mServiceState.getState() == 3) ? false : true;
    }

    private static class PhoneStateListenerExecutor implements java.util.concurrent.Executor {
        private final android.os.Handler mHandler;

        PhoneStateListenerExecutor(android.os.Handler handler) {
            this.mHandler = handler;
        }

        @Override // java.util.concurrent.Executor
        public void execute(java.lang.Runnable command) {
            if (!this.mHandler.post(command)) {
                throw new java.util.concurrent.RejectedExecutionException(this.mHandler + " is shutting down");
            }
        }
    }

    private class PhoneStateListenerImpl extends android.telephony.PhoneStateListener {
        PhoneStateListenerImpl(java.util.concurrent.Executor executor) {
            super(executor);
        }

        @Override // android.telephony.PhoneStateListener
        public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
            com.android.server.am.DataConnectionStats.this.mSignalStrength = signalStrength;
        }

        @Override // android.telephony.PhoneStateListener
        public void onServiceStateChanged(android.telephony.ServiceState state) {
            com.android.server.am.DataConnectionStats.this.mServiceState = state;
            com.android.server.am.DataConnectionStats.this.mNrState = state.getNrState();
            com.android.server.am.DataConnectionStats.this.notePhoneDataConnectionState();
        }

        @Override // android.telephony.PhoneStateListener
        public void onDataConnectionStateChanged(int state, int networkType) {
            com.android.server.am.DataConnectionStats.this.mDataState = state;
            com.android.server.am.DataConnectionStats.this.notePhoneDataConnectionState();
        }

        @Override // android.telephony.PhoneStateListener
        public void onDataActivity(int direction) {
            com.android.server.am.DataConnectionStats.this.notePhoneDataConnectionState();
        }
    }
}
