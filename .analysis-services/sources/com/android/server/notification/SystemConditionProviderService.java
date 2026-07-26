package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public abstract class SystemConditionProviderService extends android.service.notification.ConditionProviderService {
    public abstract android.service.notification.IConditionProvider asInterface();

    public abstract void attachBase(android.content.Context context);

    public abstract void dump(java.io.PrintWriter printWriter, com.android.server.notification.NotificationManagerService.DumpFilter dumpFilter);

    public abstract android.content.ComponentName getComponent();

    public abstract boolean isValidConditionId(android.net.Uri uri);

    public abstract void onBootComplete();

    protected static java.lang.String ts(long time) {
        return new java.util.Date(time) + " (" + time + ")";
    }

    protected static java.lang.String formatDuration(long millis) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        android.util.TimeUtils.formatDuration(millis, sb);
        return sb.toString();
    }

    protected static void dumpUpcomingTime(java.io.PrintWriter pw, java.lang.String var, long time, long now) {
        pw.print("      ");
        pw.print(var);
        pw.print('=');
        if (time > 0) {
            pw.printf("%s, in %s, now=%s", ts(time), formatDuration(time - now), ts(now));
        } else {
            pw.print(time);
        }
        pw.println();
    }
}
