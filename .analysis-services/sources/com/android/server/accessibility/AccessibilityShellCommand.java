package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
final class AccessibilityShellCommand extends android.os.ShellCommand {
    final android.content.Context mContext;
    final com.android.server.accessibility.AccessibilityManagerService mService;
    final com.android.server.accessibility.SystemActionPerformer mSystemActionPerformer;
    final com.android.server.wm.WindowManagerInternal mWindowManagerService = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);

    AccessibilityShellCommand(android.content.Context context, com.android.server.accessibility.AccessibilityManagerService service, com.android.server.accessibility.SystemActionPerformer systemActionPerformer) {
        this.mContext = context;
        this.mService = service;
        this.mSystemActionPerformer = systemActionPerformer;
    }

    public int onCommand(java.lang.String cmd) {
        byte b;
        if (cmd == null) {
            return handleDefaultCommands(cmd);
        }
        switch (cmd.hashCode()) {
            case -1659822550:
                b = !cmd.equals("check-hidraw") ? (byte) -1 : (byte) 5;
                break;
            case -859068373:
                b = !cmd.equals("get-bind-instant-service-allowed") ? (byte) -1 : (byte) 0;
                break;
            case 789489311:
                b = !cmd.equals("set-bind-instant-service-allowed") ? (byte) -1 : (byte) 1;
                break;
            case 1340897306:
                b = !cmd.equals("start-trace") ? (byte) -1 : (byte) 3;
                break;
            case 1748820581:
                b = !cmd.equals("call-system-action") ? (byte) -1 : (byte) 2;
                break;
            case 1857979322:
                b = !cmd.equals("stop-trace") ? (byte) -1 : (byte) 4;
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
            case 2:
                return runCallSystemAction();
            case 3:
            case 4:
                return this.mService.getTraceManager().onShellCommand(cmd, this);
            case 5:
                return checkHidraw();
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

    private int runCallSystemAction() {
        java.lang.String option;
        int callingUid = android.os.Binder.getCallingUid();
        if ((callingUid != 0 && callingUid != 1000 && callingUid != 2000) || (option = getNextArg()) == null) {
            return -1;
        }
        int actionId = java.lang.Integer.parseInt(option);
        this.mSystemActionPerformer.performSystemAction(actionId);
        return 0;
    }

    private int checkHidraw() {
        byte b;
        this.mContext.enforceCallingPermission("android.permission.MANAGE_ACCESSIBILITY", "Missing MANAGE_ACCESSIBILITY permission");
        java.lang.String subcommand = getNextArgRequired();
        java.io.File hidrawNode = new java.io.File(getNextArgRequired());
        switch (subcommand.hashCode()) {
            case -748366993:
                b = !subcommand.equals("descriptor") ? (byte) -1 : (byte) 2;
                break;
            case 3496342:
                b = !subcommand.equals("read") ? (byte) -1 : (byte) 0;
                break;
            case 113399775:
                b = !subcommand.equals("write") ? (byte) -1 : (byte) 1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return checkHidrawRead(hidrawNode);
            case 1:
                return checkHidrawWrite(hidrawNode);
            case 2:
                return checkHidrawDescriptor(hidrawNode);
            default:
                getErrPrintWriter().print("Unknown subcommand " + subcommand);
                return -1;
        }
    }

    private int checkHidrawRead(java.io.File hidrawNode) {
        if (!hidrawNode.canRead()) {
            getErrPrintWriter().println("Unable to read from " + hidrawNode);
            return -1;
        }
        getOutPrintWriter().print(hidrawNode.getAbsolutePath());
        return 0;
    }

    private int checkHidrawWrite(java.io.File hidrawNode) {
        if (!hidrawNode.canWrite()) {
            getErrPrintWriter().println("Unable to write to " + hidrawNode);
            return -1;
        }
        getOutPrintWriter().print(hidrawNode.getAbsolutePath());
        return 0;
    }

    private int checkHidrawDescriptor(java.io.File hidrawNode) {
        com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner scanner = com.android.server.accessibility.BrailleDisplayConnection.createScannerForShell();
        byte[] descriptor = scanner.getDeviceReportDescriptor(hidrawNode.toPath());
        if (descriptor == null) {
            getErrPrintWriter().println("Unable to read descriptor for " + hidrawNode);
            return -1;
        }
        try {
            getRawOutputStream().write(descriptor);
            return 0;
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException(e);
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
        return java.lang.Integer.valueOf(android.app.ActivityManager.getCurrentUser());
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Accessibility service (accessibility) commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("  set-bind-instant-service-allowed [--user <USER_ID>] true|false ");
        pw.println("    Set whether binding to services provided by instant apps is allowed.");
        pw.println("  get-bind-instant-service-allowed [--user <USER_ID>]");
        pw.println("    Get whether binding to services provided by instant apps is allowed.");
        pw.println("  call-system-action <ACTION_ID>");
        pw.println("    Calls the system action with the given action id.");
        pw.println("  check-hidraw [read|write|descriptor] <HIDRAW_NODE_PATH>");
        pw.println("    Checks if the system can perform various actions on the HIDRAW node.");
        this.mService.getTraceManager().onHelp(pw);
    }
}
