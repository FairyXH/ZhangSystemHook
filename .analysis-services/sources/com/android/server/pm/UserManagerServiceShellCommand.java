package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class UserManagerServiceShellCommand extends android.os.ShellCommand {
    private static final java.lang.String LOG_TAG = "UserManagerServiceShellCommand";
    private final android.content.Context mContext;
    private final com.android.internal.widget.LockPatternUtils mLockPatternUtils;
    private final com.android.server.pm.UserManagerService mService;
    private final com.android.server.pm.UserSystemPackageInstaller mSystemPackageInstaller;

    UserManagerServiceShellCommand(com.android.server.pm.UserManagerService service, com.android.server.pm.UserSystemPackageInstaller userSystemPackageInstaller, com.android.internal.widget.LockPatternUtils lockPatternUtils, android.content.Context context) {
        this.mService = service;
        this.mSystemPackageInstaller = userSystemPackageInstaller;
        this.mLockPatternUtils = lockPatternUtils;
        this.mContext = context;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("User manager (user) commands:");
        pw.println("  help");
        pw.println("    Prints this help text.");
        pw.println();
        pw.println("  list [-v | --verbose] [--all]");
        pw.println("    Prints all users on the system.");
        pw.println();
        pw.println("  report-system-user-package-whitelist-problems [-v | --verbose] [--critical-only] [--mode MODE]");
        pw.println("    Reports all issues on user-type package allowlist XML files. Options:");
        pw.println("    -v | --verbose: shows extra info, like number of issues");
        pw.println("    --critical-only: show only critical issues, excluding warnings");
        pw.println("    --mode MODE: shows what errors would be if device used mode MODE");
        pw.println("      (where MODE is the allowlist mode integer as defined by config_userTypePackageWhitelistMode)");
        pw.println();
        pw.println("  set-system-user-mode-emulation [--reboot | --no-restart] <headless | full | default>");
        pw.println("    Changes whether the system user is headless, full, or default (as defined by OEM).");
        pw.println("    WARNING: this command is meant just for development and debugging purposes.");
        pw.println("             It should NEVER be used on automated tests.");
        pw.println("    NOTE: by default it restarts the Android runtime, unless called with");
        pw.println("          --reboot (which does a full reboot) or");
        pw.println("          --no-restart (which requires a manual restart)");
        pw.println();
        pw.println("  is-headless-system-user-mode [-v | --verbose]");
        pw.println("    Checks whether the device uses headless system user mode.");
        pw.println("  is-visible-background-users-on-default-display-supported [-v | --verbose]");
        pw.println("    Checks whether the device allows users to be start visible on background in the default display.");
        pw.println("    It returns the effective mode, even when using emulation");
        pw.println("    (to get the real mode as well, use -v or --verbose)");
        pw.println();
        pw.println("  is-visible-background-users-supported [-v | --verbose]");
        pw.println("    Checks whether the device allows users to be start visible on background.");
        pw.println("    It returns the effective mode, even when using emulation");
        pw.println("    (to get the real mode as well, use -v or --verbose)");
        pw.println();
        pw.println("  is-user-visible [--display DISPLAY_ID] <USER_ID>");
        pw.println("    Checks if the given user is visible in the given display.");
        pw.println("    If the display option is not set, it uses the user's context to check");
        pw.println("    (so it emulates what apps would get from UserManager.isUserVisible())");
        pw.println();
        pw.println("  get-main-user ");
        pw.println("    Displays main user id or message if there is no main user");
        pw.println();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0010  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r6) {
        /*
            Method dump skipped, instruction units count: 282
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.UserManagerServiceShellCommand.onCommand(java.lang.String):int");
    }

    private int runList() throws android.os.RemoteException {
        int currentUser;
        boolean all;
        boolean verbose;
        java.lang.String opt;
        com.android.server.pm.UserManagerServiceShellCommand userManagerServiceShellCommand = this;
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean verbose2 = false;
        boolean all2 = false;
        while (true) {
            java.lang.String nextOption = getNextOption();
            java.lang.String opt2 = nextOption;
            int i = 0;
            if (nextOption != null) {
                switch (opt2.hashCode()) {
                    case 1513:
                        if (!opt2.equals("-v")) {
                            i = -1;
                        }
                        break;
                    case 42995713:
                        i = !opt2.equals("--all") ? -1 : 2;
                        break;
                    case 1737088994:
                        i = !opt2.equals("--verbose") ? -1 : 1;
                        break;
                    default:
                        i = -1;
                        break;
                }
                switch (i) {
                    case 0:
                    case 1:
                        verbose2 = true;
                        break;
                    case 2:
                        all2 = true;
                        break;
                    default:
                        pw.println("Invalid option: " + opt2);
                        return -1;
                }
            } else {
                android.app.IActivityManager am = android.app.ActivityManager.getService();
                java.util.List<android.content.pm.UserInfo> users = userManagerServiceShellCommand.mService.getUsers(!all2, false, !all2);
                if (users == null) {
                    pw.println("Error: couldn't get users");
                    return 1;
                }
                int size = users.size();
                if (verbose2) {
                    pw.printf("%d users:\n\n", java.lang.Integer.valueOf(size));
                    int currentUser2 = am.getCurrentUser().id;
                    currentUser = currentUser2;
                } else {
                    pw.println("Users:");
                    currentUser = -10000;
                }
                int i2 = 0;
                while (i2 < size) {
                    android.content.pm.UserInfo user = users.get(i2);
                    boolean running = am.isUserRunning(user.id, i);
                    if (verbose2) {
                        android.app.admin.DevicePolicyManagerInternal dpm = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
                        java.lang.String deviceOwner = "";
                        java.lang.String profileOwner = "";
                        if (dpm != null) {
                            long identity = android.os.Binder.clearCallingIdentity();
                            try {
                                if (dpm.getDeviceOwnerUserId() == user.id) {
                                    deviceOwner = " (device-owner)";
                                }
                                if (dpm.getProfileOwnerAsUser(user.id) != null) {
                                    profileOwner = " (profile-owner)";
                                }
                            } finally {
                                android.os.Binder.restoreCallingIdentity(identity);
                            }
                        }
                        boolean current = user.id == currentUser;
                        all = all2;
                        boolean hasParent = (user.profileGroupId == user.id || user.profileGroupId == -10000) ? false : true;
                        boolean visible = userManagerServiceShellCommand.mService.isUserVisible(user.id);
                        verbose = verbose2;
                        opt = opt2;
                        pw.printf("%d: id=%d, name=%s, type=%s, flags=%s%s%s%s%s%s%s%s%s%s\n", java.lang.Integer.valueOf(i2), java.lang.Integer.valueOf(user.id), user.name, user.userType.replace("android.os.usertype.", ""), android.content.pm.UserInfo.flagsToString(user.flags), hasParent ? " (parentId=" + user.profileGroupId + ")" : "", running ? " (running)" : "", user.partial ? " (partial)" : "", user.preCreated ? " (pre-created)" : "", user.convertedFromPreCreated ? " (converted)" : "", deviceOwner, profileOwner, current ? " (current)" : "", visible ? " (visible)" : "");
                    } else {
                        all = all2;
                        verbose = verbose2;
                        opt = opt2;
                        pw.printf("\t%s%s\n", user, running ? " running" : "");
                    }
                    i2++;
                    userManagerServiceShellCommand = this;
                    all2 = all;
                    verbose2 = verbose;
                    opt2 = opt;
                    i = 0;
                }
                return 0;
            }
        }
    }

    private int runReportPackageAllowlistProblems() {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean verbose = false;
        boolean criticalOnly = false;
        int mode = -1000;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case -1362766982:
                        b = !opt.equals("--critical-only") ? (byte) -1 : (byte) 2;
                        break;
                    case 1513:
                        if (!opt.equals("-v")) {
                            b = -1;
                        }
                        break;
                    case 1333227331:
                        b = !opt.equals("--mode") ? (byte) -1 : (byte) 3;
                        break;
                    case 1737088994:
                        b = !opt.equals("--verbose") ? (byte) -1 : (byte) 1;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        verbose = true;
                        break;
                    case 2:
                        criticalOnly = true;
                        break;
                    case 3:
                        mode = java.lang.Integer.parseInt(getNextArgRequired());
                        break;
                    default:
                        pw.println("Invalid option: " + opt);
                        return -1;
                }
            } else {
                android.util.Slog.d(LOG_TAG, "runReportPackageAllowlistProblems(): verbose=" + verbose + ", criticalOnly=" + criticalOnly + ", mode=" + com.android.server.pm.UserSystemPackageInstaller.modeToString(mode));
                android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw, "  ");
                try {
                    this.mSystemPackageInstaller.dumpPackageWhitelistProblems(ipw, mode, verbose, criticalOnly);
                    ipw.close();
                    return 0;
                } catch (java.lang.Throwable th) {
                    try {
                        ipw.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        }
    }

    private int runSetSystemUserModeEmulation() {
        boolean changed;
        if (!confirmBuildIsDebuggable() || !confirmIsCalledByRoot()) {
            return -1;
        }
        java.io.PrintWriter pw = getOutPrintWriter();
        if (this.mLockPatternUtils.isSecure(0)) {
            pw.println("Cannot change system user mode when it has a credential");
            return -1;
        }
        boolean restart = true;
        boolean reboot = false;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 1;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1269283747:
                        if (!opt.equals("--no-restart")) {
                            b = -1;
                        }
                        break;
                    case 1465075013:
                        b = !opt.equals("--reboot") ? (byte) -1 : (byte) 0;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        reboot = true;
                        break;
                    case 1:
                        restart = false;
                        break;
                    default:
                        pw.println("Invalid option: " + opt);
                        return -1;
                }
            } else {
                if (reboot && !restart) {
                    getErrPrintWriter().println("You can use --reboot or --no-restart, but not both");
                    return -1;
                }
                java.lang.String mode = getNextArgRequired();
                boolean isHeadlessSystemUserModeCurrently = android.os.UserManager.isHeadlessSystemUserMode();
                switch (mode.hashCode()) {
                    case -1115062407:
                        if (!mode.equals("headless")) {
                            b = -1;
                        }
                        break;
                    case 3154575:
                        b = !mode.equals("full") ? (byte) -1 : (byte) 0;
                        break;
                    case 1544803905:
                        b = !mode.equals("default") ? (byte) -1 : (byte) 2;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        changed = isHeadlessSystemUserModeCurrently;
                        break;
                    case 1:
                        changed = !isHeadlessSystemUserModeCurrently;
                        break;
                    case 2:
                        changed = true;
                        break;
                    default:
                        getErrPrintWriter().printf("Invalid arg: %s\n", mode);
                        return -1;
                }
                if (!changed) {
                    pw.printf("No change needed, system user is already %s\n", isHeadlessSystemUserModeCurrently ? "headless" : "full");
                    return 0;
                }
                com.android.server.utils.Slogf.d(LOG_TAG, "Updating system property %s to %s", "persist.debug.user_mode_emulation", mode);
                android.os.SystemProperties.set("persist.debug.user_mode_emulation", mode);
                if (reboot) {
                    android.util.Slog.i(LOG_TAG, "Rebooting to finalize the changes");
                    pw.println("Rebooting to finalize changes");
                    com.android.server.UiThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.pm.UserManagerServiceShellCommand$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.android.server.power.ShutdownThread.reboot(android.app.ActivityThread.currentActivityThread().getSystemUiContext(), "To switch headless / full system user mode", false);
                        }
                    });
                } else if (restart) {
                    android.util.Slog.i(LOG_TAG, "Shutting PackageManager down");
                    ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).shutdown();
                    android.app.IActivityManager am = android.app.ActivityManager.getService();
                    if (am != null) {
                        try {
                            android.util.Slog.i(LOG_TAG, "Shutting ActivityManager down");
                            am.shutdown(10000);
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.e(LOG_TAG, "Failed to shut down ActivityManager" + e);
                        }
                    }
                    int pid = android.os.Process.myPid();
                    com.android.server.utils.Slogf.i(LOG_TAG, "Restarting Android runtime(PID=%d) to finalize changes", java.lang.Integer.valueOf(pid));
                    pw.println("Restarting Android runtime to finalize changes");
                    pw.println("The restart may trigger a 'Broken pipe' message; this is to be expected.");
                    pw.flush();
                    android.os.Process.killProcess(pid);
                } else {
                    pw.println("System user mode changed - please reboot (or restart Android runtime) to continue");
                    pw.println("NOTICE: after restart, some apps might be uninstalled (and their data will be lost)");
                }
                return 0;
            }
        }
    }

    private int runIsUserVisible() {
        boolean isVisible;
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.Integer displayId = null;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case -1237221598:
                        if (!opt.equals("--display")) {
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        displayId = java.lang.Integer.valueOf(java.lang.Integer.parseInt(getNextArgRequired()));
                        break;
                    default:
                        pw.println("Invalid option: " + opt);
                        return -1;
                }
            } else {
                int userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                switch (userId) {
                    case -10000:
                    case -3:
                    case -1:
                        pw.printf("invalid value (%d) for --user option\n", java.lang.Integer.valueOf(userId));
                        return -1;
                    case -2:
                        userId = android.app.ActivityManager.getCurrentUser();
                        break;
                }
                if (displayId != null) {
                    isVisible = this.mService.isUserVisibleOnDisplay(userId, displayId.intValue());
                } else {
                    isVisible = getUserManagerForUser(userId).isUserVisible();
                }
                pw.println(isVisible);
                return 0;
            }
        }
    }

    private int runIsHeadlessSystemUserMode() {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean verbose = false;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1513:
                        if (!opt.equals("-v")) {
                            b = -1;
                        }
                        break;
                    case 1737088994:
                        b = !opt.equals("--verbose") ? (byte) -1 : (byte) 1;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        verbose = true;
                        break;
                    default:
                        pw.println("Invalid option: " + opt);
                        return -1;
                }
            } else {
                boolean effective = this.mService.isHeadlessSystemUserMode();
                if (!verbose) {
                    pw.println(effective);
                } else {
                    pw.printf("effective=%b real=%b\n", java.lang.Boolean.valueOf(effective), java.lang.Boolean.valueOf(com.android.internal.os.RoSystemProperties.MULTIUSER_HEADLESS_SYSTEM_USER));
                }
                return 0;
            }
        }
    }

    private int runIsVisibleBackgroundUserSupported() {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean verbose = false;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1513:
                        if (!opt.equals("-v")) {
                            b = -1;
                        }
                        break;
                    case 1737088994:
                        b = !opt.equals("--verbose") ? (byte) -1 : (byte) 1;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        verbose = true;
                        break;
                    default:
                        pw.println("Invalid option: " + opt);
                        return -1;
                }
            } else {
                boolean effective = android.os.UserManager.isVisibleBackgroundUsersEnabled();
                if (!verbose) {
                    pw.println(effective);
                } else {
                    pw.printf("effective=%b real=%b\n", java.lang.Boolean.valueOf(effective), java.lang.Boolean.valueOf(android.content.res.Resources.getSystem().getBoolean(android.R.bool.config_letterboxIsPolicyForIgnoringRequestedOrientationEnabled)));
                }
                return 0;
            }
        }
    }

    private int runIsVisibleBackgroundUserOnDefaultDisplaySupported() {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean verbose = false;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1513:
                        if (!opt.equals("-v")) {
                            b = -1;
                        }
                        break;
                    case 1737088994:
                        b = !opt.equals("--verbose") ? (byte) -1 : (byte) 1;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        verbose = true;
                        break;
                    default:
                        pw.println("Invalid option: " + opt);
                        return -1;
                }
            } else {
                boolean effective = android.os.UserManager.isVisibleBackgroundUsersOnDefaultDisplayEnabled();
                if (!verbose) {
                    pw.println(effective);
                } else {
                    pw.printf("effective=%b real=%b\n", java.lang.Boolean.valueOf(effective), java.lang.Boolean.valueOf(android.content.res.Resources.getSystem().getBoolean(android.R.bool.config_letterboxIsSplitScreenAspectRatioForUnresizableAppsEnabled)));
                }
                return 0;
            }
        }
    }

    private int runGetMainUserId() {
        java.io.PrintWriter pw = getOutPrintWriter();
        int mainUserId = this.mService.getMainUserId();
        if (mainUserId == -10000) {
            pw.println(com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG);
            return 1;
        }
        pw.println(mainUserId);
        return 0;
    }

    private int canSwitchToHeadlessSystemUser() {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean canSwitchToHeadlessSystemUser = this.mService.canSwitchToHeadlessSystemUser();
        pw.println(canSwitchToHeadlessSystemUser);
        return 0;
    }

    private int isMainUserPermanentAdmin() {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean isMainUserPermanentAdmin = this.mService.isMainUserPermanentAdmin();
        pw.println(isMainUserPermanentAdmin);
        return 0;
    }

    private android.os.UserManager getUserManagerForUser(int userId) {
        android.os.UserHandle user = android.os.UserHandle.of(userId);
        android.content.Context context = this.mContext.createContextAsUser(user, 0);
        return (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
    }

    private boolean confirmBuildIsDebuggable() {
        if (android.os.Build.isDebuggable()) {
            return true;
        }
        getErrPrintWriter().println("Command not available on user builds");
        return false;
    }

    private boolean confirmIsCalledByRoot() {
        if (android.os.Binder.getCallingUid() == 0) {
            return true;
        }
        getErrPrintWriter().println("Command only available on root user");
        return false;
    }
}
