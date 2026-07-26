package com.android.server.stats;

/* JADX INFO: loaded from: classes3.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public final class StatsHelper {
    private StatsHelper() {
    }

    public static void sendStatsdReadyBroadcast(android.content.Context context) {
        context.sendBroadcastAsUser(new android.content.Intent("android.app.action.STATSD_STARTED").addFlags(16777216), android.os.UserHandle.SYSTEM, "android.permission.DUMP");
    }
}
