package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
class GnssTimeUpdateServiceShellCommand extends android.os.ShellCommand {
    private static final java.lang.String SHELL_COMMAND_SERVICE_NAME = "gnss_time_update_service";
    private static final java.lang.String SHELL_COMMAND_START_GNSS_LISTENING = "start_gnss_listening";
    private final com.android.server.timedetector.GnssTimeUpdateService mGnssTimeUpdateService;

    GnssTimeUpdateServiceShellCommand(com.android.server.timedetector.GnssTimeUpdateService gnssTimeUpdateService) {
        this.mGnssTimeUpdateService = (com.android.server.timedetector.GnssTimeUpdateService) java.util.Objects.requireNonNull(gnssTimeUpdateService);
    }

    public int onCommand(java.lang.String cmd) {
        byte b;
        if (cmd == null) {
            return handleDefaultCommands(cmd);
        }
        switch (cmd.hashCode()) {
            case 1191671168:
                if (cmd.equals(SHELL_COMMAND_START_GNSS_LISTENING)) {
                    b = 0;
                    break;
                }
            default:
                b = -1;
                break;
        }
        switch (b) {
        }
        return handleDefaultCommands(cmd);
    }

    private int runStartGnssListening() {
        boolean success = this.mGnssTimeUpdateService.startGnssListening();
        getOutPrintWriter().println(success);
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.printf("Network Time Update Service (%s) commands:\n", SHELL_COMMAND_SERVICE_NAME);
        pw.printf("  help\n", new java.lang.Object[0]);
        pw.printf("    Print this help text.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", SHELL_COMMAND_START_GNSS_LISTENING);
        pw.printf("    Forces the service in to GNSS listening mode (if it isn't already).\n", new java.lang.Object[0]);
        pw.printf("    Prints true if the service is listening after this command.\n", new java.lang.Object[0]);
        pw.println();
    }
}
