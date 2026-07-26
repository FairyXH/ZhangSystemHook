package com.android.server.content;

/* JADX INFO: loaded from: classes.dex */
public class ContentShellCommand extends android.os.ShellCommand {
    final android.content.IContentService mInterface;

    ContentShellCommand(android.content.IContentService service) {
        this.mInterface = service;
    }

    public int onCommand(java.lang.String cmd) {
        byte b;
        if (cmd == null) {
            return handleDefaultCommands(cmd);
        }
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            switch (cmd.hashCode()) {
                case -796331115:
                    if (cmd.equals("reset-today-stats")) {
                        b = 0;
                        break;
                    }
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    return runResetTodayStats();
                default:
                    return handleDefaultCommands(cmd);
            }
        } catch (android.os.RemoteException e) {
            pw.println("Remote exception: " + e);
            return -1;
        }
    }

    private int runResetTodayStats() throws android.os.RemoteException {
        this.mInterface.resetTodayStats();
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Content service commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("");
        pw.println("  reset-today-stats");
        pw.println("    Reset 1-day sync stats.");
        pw.println();
    }
}
