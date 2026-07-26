package com.android.server.powerstats;

/* JADX INFO: loaded from: classes3.dex */
public final class BatteryTrigger extends com.android.server.powerstats.PowerStatsLogTrigger {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = com.android.server.powerstats.BatteryTrigger.class.getSimpleName();
    private int mBatteryLevel;
    private final android.content.BroadcastReceiver mBatteryLevelReceiver;

    public BatteryTrigger(android.content.Context context, com.android.server.powerstats.PowerStatsLogger powerStatsLogger, boolean triggerEnabled) {
        super(context, powerStatsLogger);
        this.mBatteryLevel = 0;
        this.mBatteryLevelReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.powerstats.BatteryTrigger.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                byte b;
                java.lang.String action = intent.getAction();
                switch (action.hashCode()) {
                    case -1538406691:
                        if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                            b = 0;
                            break;
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        int newBatteryLevel = intent.getIntExtra("level", 0);
                        if (newBatteryLevel < com.android.server.powerstats.BatteryTrigger.this.mBatteryLevel) {
                            com.android.server.powerstats.BatteryTrigger.this.logPowerStatsData(0);
                        }
                        com.android.server.powerstats.BatteryTrigger.this.mBatteryLevel = newBatteryLevel;
                        break;
                }
            }
        };
        if (triggerEnabled) {
            android.content.IntentFilter filter = new android.content.IntentFilter("android.intent.action.BATTERY_CHANGED");
            android.content.Intent batteryStatus = this.mContext.registerReceiver(this.mBatteryLevelReceiver, filter);
            if (batteryStatus != null) {
                this.mBatteryLevel = batteryStatus.getIntExtra("level", 0);
            } else {
                android.util.Slog.w(TAG, "batteryStatus is null and mBatteryLevel is " + this.mBatteryLevel);
            }
        }
    }
}
