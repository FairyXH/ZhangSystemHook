package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public class FaceShellCommand extends android.os.ShellCommand {
    private final com.android.server.biometrics.sensors.face.FaceService mService;

    public FaceShellCommand(com.android.server.biometrics.sensors.face.FaceService service) {
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
            int r2 = r6.hashCode()     // Catch: java.lang.Exception -> L5f
            switch(r2) {
                case 3198785: goto L25;
                case 3545755: goto L1b;
                case 595233003: goto L10;
                default: goto Lf;
            }     // Catch: java.lang.Exception -> L5f
        Lf:
            goto L30
        L10:
            java.lang.String r0 = "notification"
            boolean r0 = r6.equals(r0)     // Catch: java.lang.Exception -> L5f
            if (r0 == 0) goto Lf
            r0 = 2
            goto L31
        L1b:
            java.lang.String r2 = "sync"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L5f
            if (r2 == 0) goto Lf
            goto L31
        L25:
            java.lang.String r0 = "help"
            boolean r0 = r6.equals(r0)     // Catch: java.lang.Exception -> L5f
            if (r0 == 0) goto Lf
            r0 = 0
            goto L31
        L30:
            r0 = r1
        L31:
            switch(r0) {
                case 0: goto L43;
                case 1: goto L3e;
                case 2: goto L39;
                default: goto L34;
            }     // Catch: java.lang.Exception -> L5f
        L34:
            java.io.PrintWriter r0 = r5.getOutPrintWriter()     // Catch: java.lang.Exception -> L5f
            goto L48
        L39:
            int r0 = r5.doNotify()     // Catch: java.lang.Exception -> L5f
            return r0
        L3e:
            int r0 = r5.doSync()     // Catch: java.lang.Exception -> L5f
            return r0
        L43:
            int r0 = r5.doHelp()     // Catch: java.lang.Exception -> L5f
            return r0
        L48:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L5f
            r2.<init>()     // Catch: java.lang.Exception -> L5f
            java.lang.String r3 = "Unrecognized command: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Exception -> L5f
            java.lang.StringBuilder r2 = r2.append(r6)     // Catch: java.lang.Exception -> L5f
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L5f
            r0.println(r2)     // Catch: java.lang.Exception -> L5f
            goto L7a
        L5f:
            r0 = move-exception
            java.io.PrintWriter r2 = r5.getOutPrintWriter()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Exception: "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r3 = r3.toString()
            r2.println(r3)
        L7a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.biometrics.sensors.face.FaceShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Face Service commands:");
        pw.println("  help");
        pw.println("      Print this help text.");
        pw.println("  sync");
        pw.println("      Sync enrollments now (virtualized sensors only).");
        pw.println("  notification");
        pw.println("     Sends a Face re-enrollment notification");
    }

    private int doHelp() {
        onHelp();
        return 0;
    }

    private int doSync() {
        this.mService.syncEnrollmentsNow();
        return 0;
    }

    private int doNotify() {
        this.mService.sendFaceReEnrollNotification();
        return 0;
    }
}
