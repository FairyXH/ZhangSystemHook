package com.android.server.powerstats;

/* JADX INFO: loaded from: classes3.dex */
public abstract class PowerStatsLogTrigger {
    private static final java.lang.String TAG = com.android.server.powerstats.PowerStatsLogTrigger.class.getSimpleName();
    protected android.content.Context mContext;
    private com.android.server.powerstats.PowerStatsLogger mPowerStatsLogger;

    protected void logPowerStatsData(int msgType) {
        android.os.Message.obtain(this.mPowerStatsLogger, msgType).sendToTarget();
    }

    public PowerStatsLogTrigger(android.content.Context context, com.android.server.powerstats.PowerStatsLogger powerStatsLogger) {
        this.mContext = context;
        this.mPowerStatsLogger = powerStatsLogger;
    }
}
