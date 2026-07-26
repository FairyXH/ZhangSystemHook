package com.android.server.translation;

/* JADX INFO: loaded from: classes3.dex */
public class TranslationManagerServiceShellCommand extends android.os.ShellCommand {
    private final com.android.server.translation.TranslationManagerService mService;

    TranslationManagerServiceShellCommand(com.android.server.translation.TranslationManagerService service) {
        this.mService = service;
    }

    public int onCommand(java.lang.String cmd) {
        if (cmd == null) {
            return handleDefaultCommands(cmd);
        }
        java.io.PrintWriter pw = getOutPrintWriter();
        if ("set".equals(cmd)) {
            return requestSet(pw);
        }
        return handleDefaultCommands(cmd);
    }

    private int requestSet(java.io.PrintWriter pw) {
        java.lang.String what = getNextArgRequired();
        if ("temporary-service".equals(what)) {
            return setTemporaryService(pw);
        }
        pw.println("Invalid set: " + what);
        return -1;
    }

    private int setTemporaryService(java.io.PrintWriter pw) {
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String serviceName = getNextArg();
        if (serviceName == null) {
            this.mService.resetTemporaryService(userId);
            return 0;
        }
        int duration = java.lang.Integer.parseInt(getNextArgRequired());
        this.mService.setTemporaryService(userId, serviceName, duration);
        pw.println("TranslationService temporarily set to " + serviceName + " for " + duration + "ms");
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            pw.println("Translation Service (translation) commands:");
            pw.println("  help");
            pw.println("    Prints this help text.");
            pw.println("");
            pw.println("  set temporary-service USER_ID [COMPONENT_NAME DURATION]");
            pw.println("    Temporarily (for DURATION ms) changes the service implementation.");
            pw.println("    To reset, call with just the USER_ID argument.");
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
