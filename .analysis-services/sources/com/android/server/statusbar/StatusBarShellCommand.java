package com.android.server.statusbar;

/* JADX INFO: loaded from: classes3.dex */
public class StatusBarShellCommand extends android.os.ShellCommand {
    private static final android.os.IBinder sToken = new com.android.server.statusbar.StatusBarShellCommand.StatusBarShellCommandToken();
    private final android.content.Context mContext;
    private final com.android.server.statusbar.StatusBarManagerService mInterface;

    public StatusBarShellCommand(com.android.server.statusbar.StatusBarManagerService service, android.content.Context context) {
        this.mInterface = service;
        this.mContext = context;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0010  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r6) {
        /*
            Method dump skipped, instruction units count: 422
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.statusbar.StatusBarShellCommand.onCommand(java.lang.String):int");
    }

    private int runAddTile() throws android.os.RemoteException {
        this.mInterface.addTile(android.content.ComponentName.unflattenFromString(getNextArgRequired()));
        return 0;
    }

    private int runRemoveTile() throws android.os.RemoteException {
        this.mInterface.remTile(android.content.ComponentName.unflattenFromString(getNextArgRequired()));
        return 0;
    }

    private int runSetTiles() throws android.os.RemoteException {
        this.mInterface.setTiles(getNextArgRequired());
        return 0;
    }

    private int runClickTile() throws android.os.RemoteException {
        this.mInterface.clickTile(android.content.ComponentName.unflattenFromString(getNextArgRequired()));
        return 0;
    }

    private int runCollapse() throws android.os.RemoteException {
        this.mInterface.collapsePanels();
        return 0;
    }

    private int runExpandSettings() throws android.os.RemoteException {
        this.mInterface.expandSettingsPanel(null);
        return 0;
    }

    private int runExpandNotifications() throws android.os.RemoteException {
        this.mInterface.expandNotificationsPanel();
        return 0;
    }

    private int runGetStatusIcons() {
        java.io.PrintWriter pw = getOutPrintWriter();
        for (java.lang.String icon : this.mInterface.getStatusBarIcons()) {
            pw.println(icon);
        }
        return 0;
    }

    private int runDisableForSetup() {
        java.lang.String arg = getNextArgRequired();
        java.lang.String pkg = this.mContext.getPackageName();
        boolean disable = java.lang.Boolean.parseBoolean(arg);
        if (!disable) {
            this.mInterface.disable(0, sToken, pkg);
            this.mInterface.disable2(0, sToken, pkg);
        } else {
            this.mInterface.disable(61145088, sToken, pkg);
            this.mInterface.disable2(0, sToken, pkg);
        }
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0072  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runSendDisableFlag() {
        /*
            Method dump skipped, instruction units count: 246
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.statusbar.StatusBarShellCommand.runSendDisableFlag():int");
    }

    private int runPassArgsToStatusBar() {
        this.mInterface.passThroughShellCommand(getAllArgs(), getOutFileDescriptor());
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0023  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runTracing() {
        /*
            r3 = this;
            java.lang.String r0 = r3.getNextArg()
            int r1 = r0.hashCode()
            r2 = 0
            switch(r1) {
                case 3540994: goto L18;
                case 109757538: goto Ld;
                default: goto Lc;
            }
        Lc:
            goto L23
        Ld:
            java.lang.String r1 = "start"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Lc
            r0 = r2
            goto L24
        L18:
            java.lang.String r1 = "stop"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Lc
            r0 = 1
            goto L24
        L23:
            r0 = -1
        L24:
            switch(r0) {
                case 0: goto L2e;
                case 1: goto L28;
                default: goto L27;
            }
        L27:
            goto L34
        L28:
            com.android.server.statusbar.StatusBarManagerService r0 = r3.mInterface
            r0.stopTracing()
            goto L34
        L2e:
            com.android.server.statusbar.StatusBarManagerService r0 = r3.mInterface
            r0.startTracing()
        L34:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.statusbar.StatusBarShellCommand.runTracing():int");
    }

    private int runGc() {
        this.mInterface.runGcForTest();
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Status bar commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("");
        pw.println("  expand-notifications");
        pw.println("    Open the notifications panel.");
        pw.println("");
        pw.println("  expand-settings");
        pw.println("    Open the notifications panel and expand quick settings if present.");
        pw.println("");
        pw.println("  collapse");
        pw.println("    Collapse the notifications and settings panel.");
        pw.println("");
        pw.println("  add-tile COMPONENT");
        pw.println("    Add a TileService of the specified component");
        pw.println("");
        pw.println("  remove-tile COMPONENT");
        pw.println("    Remove a TileService of the specified component");
        pw.println("");
        pw.println("  set-tiles LIST-OF-TILES");
        pw.println("    Sets the list of tiles as the current Quick Settings tiles");
        pw.println("");
        pw.println("  click-tile COMPONENT");
        pw.println("    Click on a TileService of the specified component");
        pw.println("");
        pw.println("  check-support");
        pw.println("    Check if this device supports QS + APIs");
        pw.println("");
        pw.println("  get-status-icons");
        pw.println("    Print the list of status bar icons and the order they appear in");
        pw.println("");
        pw.println("  disable-for-setup DISABLE");
        pw.println("    If true, disable status bar components unsuitable for device setup");
        pw.println("");
        pw.println("  send-disable-flag FLAG...");
        pw.println("    Send zero or more disable flags (parsed individually) to StatusBarManager");
        pw.println("    Valid options:");
        pw.println("        <blank>             - equivalent to \"none\"");
        pw.println("        none                - re-enables all components");
        pw.println("        search              - disable search");
        pw.println("        home                - disable naviagation home");
        pw.println("        recents             - disable recents/overview");
        pw.println("        notification-peek   - disable notification peeking");
        pw.println("        statusbar-expansion - disable status bar expansion");
        pw.println("        system-icons        - disable system icons appearing in status bar");
        pw.println("        clock               - disable clock appearing in status bar");
        pw.println("        notification-icons  - disable notification icons from status bar");
        pw.println("");
        pw.println("  tracing (start | stop)");
        pw.println("    Start or stop SystemUI tracing");
        pw.println("");
        pw.println("  NOTE: any command not listed here will be passed through to IStatusBar");
        pw.println("");
        pw.println("  Commands implemented in SystemUI:");
        pw.flush();
        this.mInterface.passThroughShellCommand(new java.lang.String[0], getOutFileDescriptor());
    }

    private static final class StatusBarShellCommandToken extends android.os.Binder {
        private StatusBarShellCommandToken() {
        }
    }
}
