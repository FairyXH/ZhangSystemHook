package com.android.server.contextualsearch;

/* JADX INFO: loaded from: classes.dex */
public class ContextualSearchManagerShellCommand extends android.os.ShellCommand {
    private final com.android.server.contextualsearch.ContextualSearchManagerService mService;

    ContextualSearchManagerShellCommand(com.android.server.contextualsearch.ContextualSearchManagerService service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public int onCommand(java.lang.String cmd) {
        byte b;
        if (cmd == null) {
            return handleDefaultCommands(cmd);
        }
        java.io.PrintWriter pw = getOutPrintWriter();
        byte b2 = -1;
        switch (cmd.hashCode()) {
            case 113762:
                if (cmd.equals("set")) {
                    b = 0;
                    break;
                }
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                java.lang.String what = getNextArgRequired();
                switch (what.hashCode()) {
                    case -787237686:
                        if (what.equals("temporary-package")) {
                            b2 = 0;
                        }
                        break;
                    case 1235500776:
                        if (what.equals("token-duration")) {
                            b2 = 1;
                        }
                        break;
                }
                switch (b2) {
                    case 0:
                        java.lang.String packageName = getNextArg();
                        if (packageName == null) {
                            this.mService.resetTemporaryPackage();
                            pw.println("ContextualSearchManagerService reset.");
                        } else {
                            int duration = java.lang.Integer.parseInt(getNextArgRequired());
                            this.mService.setTemporaryPackage(packageName, duration);
                            pw.println("ContextualSearchManagerService temporarily set to " + packageName + " for " + duration + "ms");
                        }
                        break;
                    case 1:
                        java.lang.String durationStr = getNextArg();
                        if (durationStr == null) {
                            this.mService.resetTokenValidDurationMs();
                            pw.println("ContextualSearchManagerService token duration reset.");
                        } else {
                            int durationMs = java.lang.Integer.parseInt(durationStr);
                            this.mService.setTokenValidDurationMs(durationMs);
                            pw.println("ContextualSearchManagerService temporarily set token duration to " + durationMs + "ms");
                        }
                        break;
                }
                break;
        }
        return handleDefaultCommands(cmd);
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            pw.println("ContextualSearchService commands:");
            pw.println("  help");
            pw.println("    Prints this help text.");
            pw.println("");
            pw.println("  set temporary-package [PACKAGE_NAME DURATION]");
            pw.println("    Temporarily (for DURATION ms) changes the Contextual Search implementation.");
            pw.println("    To reset, call without any arguments.");
            pw.println("  set token-duration [DURATION]");
            pw.println("    Changes the Contextual Search token duration to DURATION ms.");
            pw.println("    To reset, call without any arguments.");
            pw.println("");
            if (pw != null) {
                pw.close();
            }
        } catch (java.lang.Throwable th) {
            if (pw != null) {
                try {
                    pw.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }
}
