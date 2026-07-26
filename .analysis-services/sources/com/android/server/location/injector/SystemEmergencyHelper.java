package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class SystemEmergencyHelper extends com.android.server.location.injector.EmergencyHelper {
    private final android.content.Context mContext;
    boolean mIsInEmergencyCall;
    android.telephony.TelephonyManager mTelephonyManager;
    private final com.android.server.location.injector.SystemEmergencyHelper.EmergencyCallTelephonyCallback mEmergencyCallTelephonyCallback = new com.android.server.location.injector.SystemEmergencyHelper.EmergencyCallTelephonyCallback();
    long mEmergencyCallEndRealtimeMs = Long.MIN_VALUE;

    public SystemEmergencyHelper(android.content.Context context) {
        this.mContext = context;
    }

    public synchronized void onSystemReady() {
        if (this.mTelephonyManager != null) {
            return;
        }
        this.mTelephonyManager = (android.telephony.TelephonyManager) java.util.Objects.requireNonNull((android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class));
        this.mTelephonyManager.registerTelephonyCallback(com.android.server.FgThread.getExecutor(), this.mEmergencyCallTelephonyCallback);
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.location.injector.SystemEmergencyHelper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if (!"android.intent.action.NEW_OUTGOING_CALL".equals(intent.getAction())) {
                    return;
                }
                synchronized (com.android.server.location.injector.SystemEmergencyHelper.this) {
                    try {
                        com.android.server.location.injector.SystemEmergencyHelper.this.mIsInEmergencyCall = com.android.server.location.injector.SystemEmergencyHelper.this.mTelephonyManager.isEmergencyNumber(intent.getStringExtra("android.intent.extra.PHONE_NUMBER"));
                        com.android.server.location.injector.SystemEmergencyHelper.this.dispatchEmergencyStateChanged();
                    } catch (java.lang.IllegalStateException e) {
                        android.util.Log.w(com.android.server.location.LocationManagerService.TAG, "Failed to call TelephonyManager.isEmergencyNumber().", e);
                    }
                }
            }
        }, new android.content.IntentFilter("android.intent.action.NEW_OUTGOING_CALL"));
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.location.injector.SystemEmergencyHelper.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if (!"android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED".equals(intent.getAction())) {
                    return;
                }
                com.android.server.location.injector.SystemEmergencyHelper.this.dispatchEmergencyStateChanged();
            }
        }, new android.content.IntentFilter("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED"));
    }

    @Override // com.android.server.location.injector.EmergencyHelper
    public synchronized boolean isInEmergency(long extensionTimeMs) {
        if (this.mTelephonyManager == null) {
            return false;
        }
        boolean isInExtensionTime = this.mEmergencyCallEndRealtimeMs != Long.MIN_VALUE && android.os.SystemClock.elapsedRealtime() - this.mEmergencyCallEndRealtimeMs < extensionTimeMs;
        if (!com.android.internal.telephony.flags.Flags.enforceTelephonyFeatureMapping()) {
            return this.mIsInEmergencyCall || isInExtensionTime || this.mTelephonyManager.getEmergencyCallbackMode() || this.mTelephonyManager.isInEmergencySmsMode();
        }
        boolean emergencyCallbackMode = false;
        boolean emergencySmsMode = false;
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        if (pm.hasSystemFeature("android.hardware.telephony.calling")) {
            emergencyCallbackMode = this.mTelephonyManager.getEmergencyCallbackMode();
        }
        if (pm.hasSystemFeature("android.hardware.telephony.messaging")) {
            emergencySmsMode = this.mTelephonyManager.isInEmergencySmsMode();
        }
        return this.mIsInEmergencyCall || isInExtensionTime || emergencyCallbackMode || emergencySmsMode;
    }

    private class EmergencyCallTelephonyCallback extends android.telephony.TelephonyCallback implements android.telephony.TelephonyCallback.CallStateListener {
        EmergencyCallTelephonyCallback() {
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int state) {
            if (state == 0) {
                synchronized (com.android.server.location.injector.SystemEmergencyHelper.this) {
                    if (com.android.server.location.injector.SystemEmergencyHelper.this.mIsInEmergencyCall) {
                        com.android.server.location.injector.SystemEmergencyHelper.this.mEmergencyCallEndRealtimeMs = android.os.SystemClock.elapsedRealtime();
                        com.android.server.location.injector.SystemEmergencyHelper.this.mIsInEmergencyCall = false;
                        com.android.server.location.injector.SystemEmergencyHelper.this.dispatchEmergencyStateChanged();
                    }
                }
            }
        }
    }
}
