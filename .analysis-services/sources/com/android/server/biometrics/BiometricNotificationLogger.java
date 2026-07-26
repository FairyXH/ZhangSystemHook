package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class BiometricNotificationLogger extends android.service.notification.NotificationListenerService {
    private static final java.lang.String TAG = "FRRNotificationListener";
    private com.android.server.biometrics.log.BiometricFrameworkStatsLogger mLogger;

    BiometricNotificationLogger() {
        this(com.android.server.biometrics.log.BiometricFrameworkStatsLogger.getInstance());
    }

    BiometricNotificationLogger(com.android.server.biometrics.log.BiometricFrameworkStatsLogger logger) {
        this.mLogger = logger;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:15:0x002a  */
    @Override // android.service.notification.NotificationListenerService
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onNotificationPosted(android.service.notification.StatusBarNotification r5, android.service.notification.NotificationListenerService.RankingMap r6) {
        /*
            r4 = this;
            if (r5 == 0) goto L63
            java.lang.String r0 = r5.getTag()
            if (r0 != 0) goto L9
            goto L63
        L9:
            java.lang.String r0 = r5.getTag()
            int r1 = r0.hashCode()
            java.lang.String r2 = "FaceEnroll"
            r3 = 1
            switch(r1) {
                case -2131839613: goto L22;
                case 1786899082: goto L18;
                default: goto L17;
            }
        L17:
            goto L2a
        L18:
            java.lang.String r1 = "FingerprintEnroll"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L17
            r0 = r3
            goto L2b
        L22:
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L17
            r0 = 0
            goto L2b
        L2a:
            r0 = -1
        L2b:
            switch(r0) {
                case 0: goto L2f;
                case 1: goto L2f;
                default: goto L2e;
            }
        L2e:
            goto L62
        L2f:
            java.lang.String r0 = r5.getTag()
            if (r0 != r2) goto L37
            r0 = 4
            goto L38
        L37:
            r0 = r3
        L38:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "onNotificationPosted, tag=("
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = r5.getTag()
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = ")"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "FRRNotificationListener"
            android.util.Slog.d(r2, r1)
            com.android.server.biometrics.log.BiometricFrameworkStatsLogger r1 = r4.mLogger
            r1.logFrameworkNotification(r3, r0)
        L62:
            return
        L63:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.biometrics.BiometricNotificationLogger.onNotificationPosted(android.service.notification.StatusBarNotification, android.service.notification.NotificationListenerService$RankingMap):void");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:15:0x002b  */
    @Override // android.service.notification.NotificationListenerService
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onNotificationRemoved(android.service.notification.StatusBarNotification r5, android.service.notification.NotificationListenerService.RankingMap r6, int r7) {
        /*
            r4 = this;
            if (r5 == 0) goto L80
            java.lang.String r0 = r5.getTag()
            if (r0 != 0) goto La
            goto L80
        La:
            java.lang.String r0 = r5.getTag()
            int r1 = r0.hashCode()
            r2 = 1
            java.lang.String r3 = "FaceEnroll"
            switch(r1) {
                case -2131839613: goto L23;
                case 1786899082: goto L19;
                default: goto L18;
            }
        L18:
            goto L2b
        L19:
            java.lang.String r1 = "FingerprintEnroll"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L18
            r0 = r2
            goto L2c
        L23:
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L18
            r0 = 0
            goto L2c
        L2b:
            r0 = -1
        L2c:
            switch(r0) {
                case 0: goto L30;
                case 1: goto L30;
                default: goto L2f;
            }
        L2f:
            goto L7f
        L30:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "onNotificationRemoved, tag=("
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = r5.getTag()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = "), reason=("
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r7)
            java.lang.String r1 = ")"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "FRRNotificationListener"
            android.util.Slog.d(r1, r0)
            java.lang.String r0 = r5.getTag()
            if (r0 != r3) goto L65
            r2 = 4
            goto L66
        L65:
        L66:
            r0 = r2
            switch(r7) {
                case 1: goto L78;
                case 2: goto L71;
                default: goto L6a;
            }
        L6a:
            java.lang.String r2 = "unhandled reason, ignoring logging"
            android.util.Slog.d(r1, r2)
            goto L7f
        L71:
            com.android.server.biometrics.log.BiometricFrameworkStatsLogger r1 = r4.mLogger
            r2 = 3
            r1.logFrameworkNotification(r2, r0)
            goto L7f
        L78:
            com.android.server.biometrics.log.BiometricFrameworkStatsLogger r1 = r4.mLogger
            r2 = 2
            r1.logFrameworkNotification(r2, r0)
        L7f:
            return
        L80:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.biometrics.BiometricNotificationLogger.onNotificationRemoved(android.service.notification.StatusBarNotification, android.service.notification.NotificationListenerService$RankingMap, int):void");
    }
}
