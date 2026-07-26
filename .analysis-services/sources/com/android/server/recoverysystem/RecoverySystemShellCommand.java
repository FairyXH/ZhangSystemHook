package com.android.server.recoverysystem;

/* JADX INFO: loaded from: classes3.dex */
public class RecoverySystemShellCommand extends android.os.ShellCommand {
    private final android.os.IRecoverySystem mService;

    public RecoverySystemShellCommand(com.android.server.recoverysystem.RecoverySystemService service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x000f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r6) {
        /*
            r5 = this;
            if (r6 != 0) goto L7
            int r0 = r5.handleDefaultCommands(r6)
            return r0
        L7:
            r0 = -1
            int r1 = r6.hashCode()     // Catch: java.lang.Exception -> L68
            switch(r1) {
                case -779212638: goto L3b;
                case 3649607: goto L30;
                case 1214227142: goto L26;
                case 1256867232: goto L1b;
                case 1405182928: goto L10;
                default: goto Lf;
            }     // Catch: java.lang.Exception -> L68
        Lf:
            goto L45
        L10:
            java.lang.String r1 = "reboot-and-apply"
            boolean r1 = r6.equals(r1)     // Catch: java.lang.Exception -> L68
            if (r1 == 0) goto Lf
            r1 = 3
            goto L46
        L1b:
            java.lang.String r1 = "request-lskf"
            boolean r1 = r6.equals(r1)     // Catch: java.lang.Exception -> L68
            if (r1 == 0) goto Lf
            r1 = 0
            goto L46
        L26:
            java.lang.String r1 = "is-lskf-captured"
            boolean r1 = r6.equals(r1)     // Catch: java.lang.Exception -> L68
            if (r1 == 0) goto Lf
            r1 = 2
            goto L46
        L30:
            java.lang.String r1 = "wipe"
            boolean r1 = r6.equals(r1)     // Catch: java.lang.Exception -> L68
            if (r1 == 0) goto Lf
            r1 = 4
            goto L46
        L3b:
            java.lang.String r1 = "clear-lskf"
            boolean r1 = r6.equals(r1)     // Catch: java.lang.Exception -> L68
            if (r1 == 0) goto Lf
            r1 = 1
            goto L46
        L45:
            r1 = r0
        L46:
            switch(r1) {
                case 0: goto L62;
                case 1: goto L5d;
                case 2: goto L58;
                case 3: goto L53;
                case 4: goto L4e;
                default: goto L49;
            }     // Catch: java.lang.Exception -> L68
        L49:
            int r0 = r5.handleDefaultCommands(r6)     // Catch: java.lang.Exception -> L68
            goto L67
        L4e:
            int r0 = r5.wipe()     // Catch: java.lang.Exception -> L68
            return r0
        L53:
            int r0 = r5.rebootAndApply()     // Catch: java.lang.Exception -> L68
            return r0
        L58:
            int r0 = r5.isLskfCaptured()     // Catch: java.lang.Exception -> L68
            return r0
        L5d:
            int r0 = r5.clearLskf()     // Catch: java.lang.Exception -> L68
            return r0
        L62:
            int r0 = r5.requestLskf()     // Catch: java.lang.Exception -> L68
            return r0
        L67:
            return r0
        L68:
            r1 = move-exception
            java.io.PrintWriter r2 = r5.getErrPrintWriter()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error while executing command: "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.String r3 = r3.toString()
            r2.println(r3)
            java.io.PrintWriter r2 = r5.getErrPrintWriter()
            r1.printStackTrace(r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.recoverysystem.RecoverySystemShellCommand.onCommand(java.lang.String):int");
    }

    private int wipe() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String newFsType = getNextArg();
        java.lang.String command = "--wipe_data";
        if (newFsType != null && !newFsType.isEmpty()) {
            command = "--wipe_data\n--reformat_data=" + newFsType;
        }
        pw.println("Rebooting into recovery with " + command.replaceAll("\n", " "));
        this.mService.rebootRecoveryWithCommand(command);
        return 0;
    }

    private int requestLskf() throws android.os.RemoteException {
        java.lang.String packageName = getNextArgRequired();
        boolean success = this.mService.requestLskf(packageName, (android.content.IntentSender) null);
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.printf("Request LSKF for packageName: %s, status: %s\n", packageName, success ? com.android.server.content.SyncStorageEngine.MESG_SUCCESS : "failure");
        return 0;
    }

    private int clearLskf() throws android.os.RemoteException {
        java.lang.String packageName = getNextArgRequired();
        boolean success = this.mService.clearLskf(packageName);
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.printf("Clear LSKF for packageName: %s, status: %s\n", packageName, success ? com.android.server.content.SyncStorageEngine.MESG_SUCCESS : "failure");
        return 0;
    }

    private int isLskfCaptured() throws android.os.RemoteException {
        java.lang.String packageName = getNextArgRequired();
        boolean captured = this.mService.isLskfCaptured(packageName);
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.printf("%s LSKF capture status: %s\n", packageName, captured ? "true" : "false");
        return 0;
    }

    private int rebootAndApply() throws android.os.RemoteException {
        java.lang.String packageName = getNextArgRequired();
        java.lang.String rebootReason = getNextArgRequired();
        boolean success = this.mService.rebootWithLskf(packageName, rebootReason, false) == 0;
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.printf("%s Reboot and apply status: %s\n", packageName, success ? com.android.server.content.SyncStorageEngine.MESG_SUCCESS : "failure");
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Recovery system commands:");
        pw.println("  request-lskf <package_name>");
        pw.println("  clear-lskf");
        pw.println("  is-lskf-captured <package_name>");
        pw.println("  reboot-and-apply <package_name> <reason>");
        pw.println("  wipe <new filesystem type ext4/f2fs>");
    }
}
