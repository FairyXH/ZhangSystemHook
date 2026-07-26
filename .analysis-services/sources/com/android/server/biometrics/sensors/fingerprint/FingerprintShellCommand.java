package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintShellCommand extends android.os.ShellCommand {
    private final android.content.Context mContext;
    private final com.android.server.biometrics.sensors.fingerprint.FingerprintService mService;

    public FingerprintShellCommand(android.content.Context context, com.android.server.biometrics.sensors.fingerprint.FingerprintService service) {
        this.mContext = context;
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:9:0x000f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r6) {
        /*
            r5 = this;
            r0 = 1
            if (r6 != 0) goto L7
            r5.onHelp()
            return r0
        L7:
            r1 = -1
            int r2 = r6.hashCode()     // Catch: java.lang.Exception -> L6e
            switch(r2) {
                case -1014576245: goto L30;
                case 3198785: goto L25;
                case 3545755: goto L1b;
                case 595233003: goto L10;
                default: goto Lf;
            }     // Catch: java.lang.Exception -> L6e
        Lf:
            goto L3a
        L10:
            java.lang.String r0 = "notification"
            boolean r0 = r6.equals(r0)     // Catch: java.lang.Exception -> L6e
            if (r0 == 0) goto Lf
            r0 = 3
            goto L3b
        L1b:
            java.lang.String r2 = "sync"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L6e
            if (r2 == 0) goto Lf
            goto L3b
        L25:
            java.lang.String r0 = "help"
            boolean r0 = r6.equals(r0)     // Catch: java.lang.Exception -> L6e
            if (r0 == 0) goto Lf
            r0 = 0
            goto L3b
        L30:
            java.lang.String r0 = "fingerdown"
            boolean r0 = r6.equals(r0)     // Catch: java.lang.Exception -> L6e
            if (r0 == 0) goto Lf
            r0 = 2
            goto L3b
        L3a:
            r0 = r1
        L3b:
            switch(r0) {
                case 0: goto L52;
                case 1: goto L4d;
                case 2: goto L48;
                case 3: goto L43;
                default: goto L3e;
            }     // Catch: java.lang.Exception -> L6e
        L3e:
            java.io.PrintWriter r0 = r5.getOutPrintWriter()     // Catch: java.lang.Exception -> L6e
            goto L57
        L43:
            int r0 = r5.doNotify()     // Catch: java.lang.Exception -> L6e
            return r0
        L48:
            int r0 = r5.doSimulateVhalFingerDown()     // Catch: java.lang.Exception -> L6e
            return r0
        L4d:
            int r0 = r5.doSync()     // Catch: java.lang.Exception -> L6e
            return r0
        L52:
            int r0 = r5.doHelp()     // Catch: java.lang.Exception -> L6e
            return r0
        L57:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L6e
            r2.<init>()     // Catch: java.lang.Exception -> L6e
            java.lang.String r3 = "Unrecognized command: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> L6e
            java.lang.StringBuilder r2 = r2.append(r6)     // Catch: java.lang.Exception -> L6e
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L6e
            r0.println(r2)     // Catch: java.lang.Exception -> L6e
            goto L89
        L6e:
            r0 = move-exception
            java.io.PrintWriter r2 = r5.getOutPrintWriter()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Exception: "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r3 = r3.toString()
            r2.println(r3)
        L89:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.biometrics.sensors.fingerprint.FingerprintShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Fingerprint Service commands:");
        pw.println("  help");
        pw.println("      Print this help text.");
        pw.println("  sync");
        pw.println("      Sync enrollments now (virtualized sensors only).");
        pw.println("  fingerdown");
        pw.println("      Simulate finger down event (virtualized sensors only).");
        pw.println("  notification");
        pw.println("     Sends a Fingerprint re-enrollment notification");
    }

    private int doHelp() {
        onHelp();
        return 0;
    }

    private int doSync() {
        this.mService.syncEnrollmentsNow();
        return 0;
    }

    private int doSimulateVhalFingerDown() {
        this.mService.simulateVhalFingerDown();
        return 0;
    }

    private int doNotify() {
        this.mService.sendFingerprintReEnrollNotification();
        return 0;
    }
}
