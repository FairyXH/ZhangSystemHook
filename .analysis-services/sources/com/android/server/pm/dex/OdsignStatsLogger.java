package com.android.server.pm.dex;

/* JADX INFO: loaded from: classes2.dex */
public class OdsignStatsLogger {
    private static final java.lang.String COMPOS_METRIC_NAME = "comp_os_artifacts_check_record";
    private static final java.lang.String METRICS_FILE = "/data/misc/odsign/metrics/odsign-metrics.txt";
    private static final java.lang.String ODSIGN_METRIC_NAME = "odsign_record";
    private static final java.lang.String TAG = "OdsignStatsLogger";

    public static void triggerStatsWrite() {
        com.android.internal.os.BackgroundThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.pm.dex.OdsignStatsLogger$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.pm.dex.OdsignStatsLogger.writeStats();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0042  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void writeStats() {
        /*
            Method dump skipped, instruction units count: 290
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.dex.OdsignStatsLogger.writeStats():void");
    }
}
