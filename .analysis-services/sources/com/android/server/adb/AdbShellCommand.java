package com.android.server.adb;

/* JADX INFO: loaded from: classes.dex */
class AdbShellCommand extends com.android.modules.utils.BasicShellCommandHandler {
    private final com.android.server.adb.AdbService mService;

    AdbShellCommand(com.android.server.adb.AdbService service) {
        this.mService = (com.android.server.adb.AdbService) java.util.Objects.requireNonNull(service);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r4) {
        /*
            r3 = this;
            if (r4 != 0) goto L8
            r0 = 0
            int r0 = r3.handleDefaultCommands(r0)
            return r0
        L8:
            java.io.PrintWriter r0 = r3.getOutPrintWriter()
            int r1 = r4.hashCode()
            r2 = 0
            switch(r1) {
                case -138263081: goto L20;
                case 434812665: goto L15;
                default: goto L14;
            }
        L14:
            goto L2b
        L15:
            java.lang.String r1 = "is-wifi-supported"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L14
            r1 = r2
            goto L2c
        L20:
            java.lang.String r1 = "is-wifi-qr-supported"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L14
            r1 = 1
            goto L2c
        L2b:
            r1 = -1
        L2c:
            switch(r1) {
                case 0: goto L42;
                case 1: goto L34;
                default: goto L2f;
            }
        L2f:
            int r1 = r3.handleDefaultCommands(r4)
            return r1
        L34:
            com.android.server.adb.AdbService r1 = r3.mService
            boolean r1 = r1.isAdbWifiQrSupported()
            java.lang.String r1 = java.lang.Boolean.toString(r1)
            r0.println(r1)
            return r2
        L42:
            com.android.server.adb.AdbService r1 = r3.mService
            boolean r1 = r1.isAdbWifiSupported()
            java.lang.String r1 = java.lang.Boolean.toString(r1)
            r0.println(r1)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.adb.AdbShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Adb service commands:");
        pw.println("  help or -h");
        pw.println("    Print this help text.");
        pw.println("  is-wifi-supported");
        pw.println("    Returns \"true\" if adb over wifi is supported.");
        pw.println("  is-wifi-qr-supported");
        pw.println("    Returns \"true\" if adb over wifi + QR pairing is supported.");
        pw.println();
    }
}
