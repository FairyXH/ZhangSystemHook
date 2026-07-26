package com.android.server.print;

/* JADX INFO: loaded from: classes3.dex */
final class PrintShellCommand extends android.os.ShellCommand {
    final android.print.IPrintManager mService;

    PrintShellCommand(android.print.IPrintManager service) {
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
        try {
            getOutPrintWriter().println(java.lang.Boolean.toString(this.mService.getBindInstantServiceAllowed(userId.intValue())));
            return 0;
        } catch (android.os.RemoteException e) {
            e.rethrowFromSystemServer();
            return 0;
        }
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
        try {
            this.mService.setBindInstantServiceAllowed(userId.intValue(), java.lang.Boolean.parseBoolean(allowed));
            return 0;
        } catch (android.os.RemoteException e) {
            e.rethrowFromSystemServer();
            return 0;
        }
    }

    private java.lang.Integer parseUserId() {
        java.lang.String option = getNextOption();
        if (option != null) {
            if (option.equals("--user")) {
                return java.lang.Integer.valueOf(android.os.UserHandle.parseUserArg(getNextArgRequired()));
            }
            getErrPrintWriter().println("Unknown option: " + option);
            return null;
        }
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Print service commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("  set-bind-instant-service-allowed [--user <USER_ID>] true|false ");
        pw.println("    Set whether binding to print services provided by instant apps is allowed.");
        pw.println("  get-bind-instant-service-allowed [--user <USER_ID>]");
        pw.println("    Get whether binding to print services provided by instant apps is allowed.");
    }
}
