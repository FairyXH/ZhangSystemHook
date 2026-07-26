package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AlarmHelper {
    public abstract void cancel(android.app.AlarmManager.OnAlarmListener onAlarmListener);

    protected abstract void setDelayedAlarmInternal(long j, android.app.AlarmManager.OnAlarmListener onAlarmListener, android.os.WorkSource workSource);

    public final void setDelayedAlarm(long delayMs, android.app.AlarmManager.OnAlarmListener listener, android.os.WorkSource workSource) {
        com.android.internal.util.Preconditions.checkArgument(delayMs > 0);
        setDelayedAlarmInternal(delayMs, listener, workSource);
    }
}
