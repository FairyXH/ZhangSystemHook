package com.android.server.apphibernation;

/* JADX INFO: loaded from: classes.dex */
final class AppHibernationShellCommand extends android.os.ShellCommand {
    private static final int ERROR = -1;
    private static final java.lang.String GLOBAL_OPT = "--global";
    private static final int SUCCESS = 0;
    private static final java.lang.String USER_OPT = "--user";
    private final com.android.server.apphibernation.AppHibernationService mService;

    AppHibernationShellCommand(com.android.server.apphibernation.AppHibernationService service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0025  */
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
                case -499367066: goto L1a;
                case -284749990: goto Lf;
                default: goto Le;
            }
        Le:
            goto L25
        Lf:
            java.lang.String r0 = "get-state"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 1
            goto L26
        L1a:
            java.lang.String r0 = "set-state"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 0
            goto L26
        L25:
            r0 = -1
        L26:
            switch(r0) {
                case 0: goto L33;
                case 1: goto L2e;
                default: goto L29;
            }
        L29:
            int r0 = r1.handleDefaultCommands(r2)
            return r0
        L2e:
            int r0 = r1.runGetState()
            return r0
        L33:
            int r0 = r1.runSetState()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.apphibernation.AppHibernationShellCommand.onCommand(java.lang.String):int");
    }

    private int runSetState() {
        boolean setsGlobal = false;
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1156993347:
                        b = !opt.equals(GLOBAL_OPT) ? (byte) -1 : (byte) 1;
                        break;
                    case 1333469547:
                        if (!opt.equals(USER_OPT)) {
                            b = -1;
                        }
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    case 1:
                        setsGlobal = true;
                        break;
                    default:
                        getErrPrintWriter().println("Error: Unknown option: " + opt);
                        break;
                }
            } else {
                java.lang.String pkg = getNextArgRequired();
                if (pkg == null) {
                    getErrPrintWriter().println("Error: no package specified");
                    return -1;
                }
                java.lang.String newStateRaw = getNextArgRequired();
                if (newStateRaw == null) {
                    getErrPrintWriter().println("Error: No state to set specified");
                    return -1;
                }
                boolean newState = java.lang.Boolean.parseBoolean(newStateRaw);
                if (setsGlobal) {
                    this.mService.setHibernatingGlobally(pkg, newState);
                } else {
                    this.mService.setHibernatingForUser(pkg, userId, newState);
                }
                return 0;
            }
        }
    }

    private int runGetState() {
        boolean requestsGlobal = false;
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1156993347:
                        b = !opt.equals(GLOBAL_OPT) ? (byte) -1 : (byte) 1;
                        break;
                    case 1333469547:
                        if (!opt.equals(USER_OPT)) {
                            b = -1;
                        }
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    case 1:
                        requestsGlobal = true;
                        break;
                    default:
                        getErrPrintWriter().println("Error: Unknown option: " + opt);
                        break;
                }
            } else {
                java.lang.String pkg = getNextArgRequired();
                if (pkg == null) {
                    getErrPrintWriter().println("Error: No package specified");
                    return -1;
                }
                boolean isHibernating = requestsGlobal ? this.mService.isHibernatingGlobally(pkg) : this.mService.isHibernatingForUser(pkg, userId);
                java.io.PrintWriter pw = getOutPrintWriter();
                pw.println(isHibernating);
                return 0;
            }
        }
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("App hibernation (app_hibernation) commands: ");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("");
        pw.println("  set-state [--user USER_ID] [--global] PACKAGE true|false");
        pw.println("    Sets the hibernation state of the package to value specified. Optionally");
        pw.println("    may specify a user id or set global hibernation state.");
        pw.println("");
        pw.println("  get-state [--user USER_ID] [--global] PACKAGE");
        pw.println("    Gets the hibernation state of the package. Optionally may specify a user");
        pw.println("    id or request global hibernation state.");
        pw.println("");
    }
}
