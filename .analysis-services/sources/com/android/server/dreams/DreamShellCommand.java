package com.android.server.dreams;

/* JADX INFO: loaded from: classes2.dex */
public class DreamShellCommand extends android.os.ShellCommand {
    private static final boolean DEBUG = true;
    private static final java.lang.String TAG = "DreamShellCommand";
    private final com.android.server.dreams.DreamManagerService mService;

    DreamShellCommand(com.android.server.dreams.DreamManagerService service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:5:0x0021  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r4) {
        /*
            r3 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "onCommand:"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r4)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "DreamShellCommand"
            android.util.Slog.d(r1, r0)
            r0 = -1
            int r1 = r4.hashCode()     // Catch: java.lang.SecurityException -> L52
            switch(r1) {
                case -183711126: goto L2d;
                case 1473640970: goto L22;
                default: goto L21;
            }     // Catch: java.lang.SecurityException -> L52
        L21:
            goto L38
        L22:
            java.lang.String r1 = "start-dreaming"
            boolean r1 = r4.equals(r1)     // Catch: java.lang.SecurityException -> L52
            if (r1 == 0) goto L21
            r1 = 0
            goto L39
        L2d:
            java.lang.String r1 = "stop-dreaming"
            boolean r1 = r4.equals(r1)     // Catch: java.lang.SecurityException -> L52
            if (r1 == 0) goto L21
            r1 = 1
            goto L39
        L38:
            r1 = r0
        L39:
            switch(r1) {
                case 0: goto L49;
                case 1: goto L41;
                default: goto L3c;
            }     // Catch: java.lang.SecurityException -> L52
        L3c:
            int r0 = super.handleDefaultCommands(r4)     // Catch: java.lang.SecurityException -> L52
            goto L51
        L41:
            r3.enforceCallerIsRoot()     // Catch: java.lang.SecurityException -> L52
            int r0 = r3.stopDreaming()     // Catch: java.lang.SecurityException -> L52
            return r0
        L49:
            r3.enforceCallerIsRoot()     // Catch: java.lang.SecurityException -> L52
            int r0 = r3.startDreaming()     // Catch: java.lang.SecurityException -> L52
            return r0
        L51:
            return r0
        L52:
            r1 = move-exception
            java.io.PrintWriter r2 = r3.getOutPrintWriter()
            r2.println(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.dreams.DreamShellCommand.onCommand(java.lang.String):int");
    }

    private int startDreaming() {
        this.mService.requestStartDreamFromShell();
        return 0;
    }

    private int stopDreaming() {
        this.mService.requestStopDreamFromShell();
        return 0;
    }

    private void enforceCallerIsRoot() {
        if (android.os.Binder.getCallingUid() != 0) {
            throw new java.lang.SecurityException("Must be root to call Dream shell commands");
        }
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Dream manager (dreams) commands:");
        pw.println("  help");
        pw.println("      Print this help text.");
        pw.println("  start-dreaming");
        pw.println("      Start the currently configured dream.");
        pw.println("  stop-dreaming");
        pw.println("      Stops any active dream");
    }
}
