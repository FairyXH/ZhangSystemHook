package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class OtaDexoptShellCommand extends android.os.ShellCommand {
    final android.content.pm.IOtaDexopt mInterface;

    OtaDexoptShellCommand(com.android.server.pm.OtaDexoptService service) {
        this.mInterface = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r6) {
        /*
            r5 = this;
            if (r6 != 0) goto L8
            r0 = 0
            int r0 = r5.handleDefaultCommands(r0)
            return r0
        L8:
            java.io.PrintWriter r0 = r5.getOutPrintWriter()
            r1 = -1
            int r2 = r6.hashCode()     // Catch: android.os.RemoteException -> L7d
            switch(r2) {
                case -1001078227: goto L4a;
                case -318370553: goto L3f;
                case 3089282: goto L35;
                case 3377907: goto L2a;
                case 3540684: goto L1f;
                case 856774308: goto L15;
                default: goto L14;
            }     // Catch: android.os.RemoteException -> L7d
        L14:
            goto L55
        L15:
            java.lang.String r2 = "cleanup"
            boolean r2 = r6.equals(r2)     // Catch: android.os.RemoteException -> L7d
            if (r2 == 0) goto L14
            r2 = 1
            goto L56
        L1f:
            java.lang.String r2 = "step"
            boolean r2 = r6.equals(r2)     // Catch: android.os.RemoteException -> L7d
            if (r2 == 0) goto L14
            r2 = 3
            goto L56
        L2a:
            java.lang.String r2 = "next"
            boolean r2 = r6.equals(r2)     // Catch: android.os.RemoteException -> L7d
            if (r2 == 0) goto L14
            r2 = 4
            goto L56
        L35:
            java.lang.String r2 = "done"
            boolean r2 = r6.equals(r2)     // Catch: android.os.RemoteException -> L7d
            if (r2 == 0) goto L14
            r2 = 2
            goto L56
        L3f:
            java.lang.String r2 = "prepare"
            boolean r2 = r6.equals(r2)     // Catch: android.os.RemoteException -> L7d
            if (r2 == 0) goto L14
            r2 = 0
            goto L56
        L4a:
            java.lang.String r2 = "progress"
            boolean r2 = r6.equals(r2)     // Catch: android.os.RemoteException -> L7d
            if (r2 == 0) goto L14
            r2 = 5
            goto L56
        L55:
            r2 = r1
        L56:
            switch(r2) {
                case 0: goto L77;
                case 1: goto L72;
                case 2: goto L6d;
                case 3: goto L68;
                case 4: goto L63;
                case 5: goto L5e;
                default: goto L59;
            }     // Catch: android.os.RemoteException -> L7d
        L59:
            int r1 = r5.handleDefaultCommands(r6)     // Catch: android.os.RemoteException -> L7d
            goto L7c
        L5e:
            int r1 = r5.runOtaProgress()     // Catch: android.os.RemoteException -> L7d
            return r1
        L63:
            int r1 = r5.runOtaNext()     // Catch: android.os.RemoteException -> L7d
            return r1
        L68:
            int r1 = r5.runOtaStep()     // Catch: android.os.RemoteException -> L7d
            return r1
        L6d:
            int r1 = r5.runOtaDone()     // Catch: android.os.RemoteException -> L7d
            return r1
        L72:
            int r1 = r5.runOtaCleanup()     // Catch: android.os.RemoteException -> L7d
            return r1
        L77:
            int r1 = r5.runOtaPrepare()     // Catch: android.os.RemoteException -> L7d
            return r1
        L7c:
            return r1
        L7d:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Remote exception: "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r2)
            java.lang.String r3 = r3.toString()
            r0.println(r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.OtaDexoptShellCommand.onCommand(java.lang.String):int");
    }

    private int runOtaPrepare() throws android.os.RemoteException {
        this.mInterface.prepare();
        getOutPrintWriter().println("Success");
        return 0;
    }

    private int runOtaCleanup() throws android.os.RemoteException {
        this.mInterface.cleanup();
        return 0;
    }

    private int runOtaDone() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        if (this.mInterface.isDone()) {
            pw.println("OTA complete.");
            return 0;
        }
        pw.println("OTA incomplete.");
        return 0;
    }

    private int runOtaStep() throws android.os.RemoteException {
        this.mInterface.dexoptNextPackage();
        return 0;
    }

    private int runOtaNext() throws android.os.RemoteException {
        getOutPrintWriter().println(this.mInterface.nextDexoptCommand());
        return 0;
    }

    private int runOtaProgress() throws android.os.RemoteException {
        float progress = this.mInterface.getProgress();
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.format(java.util.Locale.ROOT, "%.2f", java.lang.Float.valueOf(progress));
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("OTA Dexopt (ota) commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("");
        pw.println("  prepare");
        pw.println("    Prepare an OTA dexopt pass, collecting all packages.");
        pw.println("  done");
        pw.println("    Replies whether the OTA is complete or not.");
        pw.println("  step");
        pw.println("    OTA dexopt the next package.");
        pw.println("  next");
        pw.println("    Get parameters for OTA dexopt of the next package.");
        pw.println("  cleanup");
        pw.println("    Clean up internal states. Ends an OTA session.");
    }
}
