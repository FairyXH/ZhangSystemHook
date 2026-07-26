package com.android.server.powerstats;

/* JADX INFO: loaded from: classes3.dex */
public final class TimerTrigger extends com.android.server.powerstats.PowerStatsLogTrigger {
    private static final boolean DEBUG = false;
    private static final long LOG_PERIOD_MS_HIGH_FREQUENCY = 120000;
    private static final long LOG_PERIOD_MS_LOW_FREQUENCY = 3600000;
    private static final java.lang.String TAG = com.android.server.powerstats.TimerTrigger.class.getSimpleName();
    private final android.app.AlarmManager mAlarmManager;
    private final android.os.Handler mHandler;

    class PeriodicTimer implements java.lang.Runnable, android.app.AlarmManager.OnAlarmListener {
        private final int mMsgType;
        private final java.lang.String mName;
        private final long mPeriodMs;

        PeriodicTimer(java.lang.String name, long periodMs, int msgType) {
            this.mName = name;
            this.mPeriodMs = periodMs;
            this.mMsgType = msgType;
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            run();
        }

        @Override // java.lang.Runnable
        public void run() {
            if (com.android.server.powerstats.Flags.alarmBasedPowerstatsLogging()) {
                long nextAlarmMs = android.os.SystemClock.elapsedRealtime() + this.mPeriodMs;
                com.android.server.powerstats.TimerTrigger.this.mAlarmManager.set(3, nextAlarmMs, 0L, 0L, this.mName, this, com.android.server.powerstats.TimerTrigger.this.mHandler, null);
            } else {
                com.android.server.powerstats.TimerTrigger.this.mHandler.postDelayed(this, this.mPeriodMs);
            }
            com.android.server.powerstats.TimerTrigger.this.logPowerStatsData(this.mMsgType);
        }
    }

    public TimerTrigger(android.content.Context context, com.android.server.powerstats.PowerStatsLogger powerStatsLogger, boolean triggerEnabled) {
        super(context, powerStatsLogger);
        this.mHandler = this.mContext.getMainThreadHandler();
        this.mAlarmManager = (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        if (triggerEnabled) {
            com.android.server.powerstats.TimerTrigger.PeriodicTimer logDataLowFrequency = new com.android.server.powerstats.TimerTrigger.PeriodicTimer("PowerStatsLowFreqLog", 3600000L, 1);
            com.android.server.powerstats.TimerTrigger.PeriodicTimer logDataHighFrequency = new com.android.server.powerstats.TimerTrigger.PeriodicTimer("PowerStatsHighFreqLog", 120000L, 2);
            logDataLowFrequency.run();
            logDataHighFrequency.run();
        }
    }
}
