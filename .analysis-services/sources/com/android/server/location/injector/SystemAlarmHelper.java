package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class SystemAlarmHelper extends com.android.server.location.injector.AlarmHelper {
    private final android.content.Context mContext;

    public SystemAlarmHelper(android.content.Context context) {
        this.mContext = context;
    }

    @Override // com.android.server.location.injector.AlarmHelper
    public void setDelayedAlarmInternal(long delayMs, android.app.AlarmManager.OnAlarmListener listener, android.os.WorkSource workSource) {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) java.util.Objects.requireNonNull((android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class));
        alarmManager.set(2, android.os.SystemClock.elapsedRealtime() + delayMs, 0L, 0L, listener, com.android.server.FgThread.getHandler(), workSource);
    }

    @Override // com.android.server.location.injector.AlarmHelper
    public void cancel(android.app.AlarmManager.OnAlarmListener listener) {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) java.util.Objects.requireNonNull((android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class));
        alarmManager.cancel(listener);
    }
}
