package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
class NetworkTimeUpdateServiceShellCommand extends android.os.ShellCommand {
    private static final java.lang.String SET_SERVER_CONFIG_SERVER_ARG = "--server";
    private static final java.lang.String SET_SERVER_CONFIG_TIMEOUT_ARG = "--timeout_millis";
    private static final java.lang.String SHELL_COMMAND_FORCE_REFRESH = "force_refresh";
    private static final java.lang.String SHELL_COMMAND_RESET_SERVER_CONFIG = "reset_server_config_for_tests";
    private static final java.lang.String SHELL_COMMAND_SERVICE_NAME = "network_time_update_service";
    private static final java.lang.String SHELL_COMMAND_SET_SERVER_CONFIG = "set_server_config_for_tests";
    private final com.android.server.timedetector.NetworkTimeUpdateService mNetworkTimeUpdateService;

    NetworkTimeUpdateServiceShellCommand(com.android.server.timedetector.NetworkTimeUpdateService networkTimeUpdateService) {
        this.mNetworkTimeUpdateService = (com.android.server.timedetector.NetworkTimeUpdateService) java.util.Objects.requireNonNull(networkTimeUpdateService);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:17:0x002f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r2) {
        /*
            r1 = this;
            if (r2 != 0) goto L7
            int r0 = r1.handleDefaultCommands(r2)
            return r0
        L7:
            int r0 = r2.hashCode()
            switch(r0) {
                case -1679617267: goto L24;
                case 65977594: goto L19;
                case 1891346823: goto Lf;
                default: goto Le;
            }
        Le:
            goto L2f
        Lf:
            java.lang.String r0 = "force_refresh"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 0
            goto L30
        L19:
            java.lang.String r0 = "reset_server_config_for_tests"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 2
            goto L30
        L24:
            java.lang.String r0 = "set_server_config_for_tests"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 1
            goto L30
        L2f:
            r0 = -1
        L30:
            switch(r0) {
                case 0: goto L42;
                case 1: goto L3d;
                case 2: goto L38;
                default: goto L33;
            }
        L33:
            int r0 = r1.handleDefaultCommands(r2)
            return r0
        L38:
            int r0 = r1.runResetServerConfig()
            return r0
        L3d:
            int r0 = r1.runSetServerConfig()
            return r0
        L42:
            int r0 = r1.runForceRefresh()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.timedetector.NetworkTimeUpdateServiceShellCommand.onCommand(java.lang.String):int");
    }

    private int runForceRefresh() {
        boolean success = this.mNetworkTimeUpdateService.forceRefreshForTests();
        getOutPrintWriter().println(success);
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runSetServerConfig() {
        /*
            r6 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = 0
        L6:
            java.lang.String r2 = r6.getNextArg()
            r3 = r2
            r4 = 0
            if (r2 == 0) goto L6a
            int r2 = r3.hashCode()
            switch(r2) {
                case -975021948: goto L1f;
                case 1494187235: goto L16;
                default: goto L15;
            }
        L15:
            goto L29
        L16:
            java.lang.String r2 = "--server"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L15
            goto L2a
        L1f:
            java.lang.String r2 = "--timeout_millis"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L15
            r4 = 1
            goto L2a
        L29:
            r4 = -1
        L2a:
            switch(r4) {
                case 0: goto L54;
                case 1: goto L46;
                default: goto L2d;
            }
        L2d:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Unknown option: "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r3)
            java.lang.String r4 = r4.toString()
            r2.<init>(r4)
            throw r2
        L46:
            java.lang.String r2 = r6.getNextArgRequired()
            int r2 = java.lang.Integer.parseInt(r2)
            long r4 = (long) r2
            java.time.Duration r1 = java.time.Duration.ofMillis(r4)
            goto L60
        L54:
            java.lang.String r2 = r6.getNextArgRequired()     // Catch: java.net.URISyntaxException -> L61
            java.net.URI r2 = android.util.NtpTrustedTime.parseNtpUriStrict(r2)     // Catch: java.net.URISyntaxException -> L61
            r0.add(r2)     // Catch: java.net.URISyntaxException -> L61
        L60:
            goto L6
        L61:
            r2 = move-exception
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.String r5 = "Bad NTP server value"
            r4.<init>(r5, r2)
            throw r4
        L6a:
            boolean r2 = r0.isEmpty()
            if (r2 != 0) goto L85
            if (r1 == 0) goto L7d
            android.util.NtpTrustedTime$NtpConfig r2 = new android.util.NtpTrustedTime$NtpConfig
            r2.<init>(r0, r1)
            com.android.server.timedetector.NetworkTimeUpdateService r5 = r6.mNetworkTimeUpdateService
            r5.setServerConfigForTests(r2)
            return r4
        L7d:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.String r4 = "Missing required option: ----timeout_millis"
            r2.<init>(r4)
            throw r2
        L85:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.String r4 = "Missing required option: ----server"
            r2.<init>(r4)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.timedetector.NetworkTimeUpdateServiceShellCommand.runSetServerConfig():int");
    }

    private int runResetServerConfig() {
        this.mNetworkTimeUpdateService.setServerConfigForTests(null);
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.printf("Network Time Update Service (%s) commands:\n", SHELL_COMMAND_SERVICE_NAME);
        pw.printf("  help\n", new java.lang.Object[0]);
        pw.printf("    Print this help text.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", SHELL_COMMAND_FORCE_REFRESH);
        pw.printf("    Refreshes the latest time. Prints whether it was successful.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", SHELL_COMMAND_SET_SERVER_CONFIG);
        pw.printf("    Sets the NTP server config for tests. The config is not persisted.\n", new java.lang.Object[0]);
        pw.printf("      Options: %s <uri> [%s <additional uris>]+ %s <millis>\n", SET_SERVER_CONFIG_SERVER_ARG, SET_SERVER_CONFIG_SERVER_ARG, SET_SERVER_CONFIG_TIMEOUT_ARG);
        pw.printf("      NTP server URIs must be in the form \"ntp://hostname\" or \"ntp://hostname:port\"\n", new java.lang.Object[0]);
        pw.printf("  %s\n", SHELL_COMMAND_RESET_SERVER_CONFIG);
        pw.printf("    Resets/clears the NTP server config set via %s.\n", SHELL_COMMAND_SET_SERVER_CONFIG);
        pw.println();
    }
}
