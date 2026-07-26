package com.android.server.accounts;

/* JADX INFO: loaded from: classes.dex */
final class AccountManagerServiceShellCommand extends android.os.ShellCommand {
    final com.android.server.accounts.AccountManagerService mService;

    AccountManagerServiceShellCommand(com.android.server.accounts.AccountManagerService service) {
        this.mService = service;
    }

    public int onCommand(java.lang.String cmd) {
        byte b;
        if (cmd == null) {
            return handleDefaultCommands(cmd);
        }
        switch (cmd.hashCode()) {
            case -859068373:
                b = !cmd.equals("get-bind-instant-service-allowed") ? (byte) -1 : (byte) 0;
                break;
            case 789489311:
                b = !cmd.equals("set-bind-instant-service-allowed") ? (byte) -1 : (byte) 1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return runGetBindInstantServiceAllowed();
            case 1:
                return runSetBindInstantServiceAllowed();
            default:
                return -1;
        }
    }

    private int runGetBindInstantServiceAllowed() {
        java.lang.Integer userId = parseUserId();
        if (userId == null) {
            return -1;
        }
        getOutPrintWriter().println(java.lang.Boolean.toString(this.mService.getBindInstantServiceAllowed(userId.intValue())));
        return 0;
    }

    private int runSetBindInstantServiceAllowed() {
        java.lang.Integer userId = parseUserId();
        if (userId == null) {
            return -1;
        }
        java.lang.String allowed = getNextArgRequired();
        if (allowed == null) {
            getErrPrintWriter().println("Error: no true/false specified");
            return -1;
        }
        this.mService.setBindInstantServiceAllowed(userId.intValue(), java.lang.Boolean.parseBoolean(allowed));
        return 0;
    }

    private java.lang.Integer parseUserId() {
        java.lang.String option = getNextOption();
        if (option != null) {
            if (option.equals("--user")) {
                int userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                if (userId == -2) {
                    return java.lang.Integer.valueOf(android.app.ActivityManager.getCurrentUser());
                }
                if (userId == -1) {
                    getErrPrintWriter().println("USER_ALL not supported. Specify a user.");
                    return null;
                }
                if (userId < 0) {
                    getErrPrintWriter().println("Invalid user: " + userId);
                    return null;
                }
                return java.lang.Integer.valueOf(userId);
            }
            getErrPrintWriter().println("Unknown option: " + option);
            return null;
        }
        return java.lang.Integer.valueOf(android.app.ActivityManager.getCurrentUser());
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Account manager service commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("  set-bind-instant-service-allowed [--user <USER_ID> (current user if not specified)] true|false ");
        pw.println("    Set whether binding to services provided by instant apps is allowed.");
        pw.println("  get-bind-instant-service-allowed [--user <USER_ID> (current user if not specified)]");
        pw.println("    Get whether binding to services provided by instant apps is allowed.");
    }
}
