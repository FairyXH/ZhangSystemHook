package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
class UsageStatsShellCommand extends android.os.ShellCommand {
    private final com.android.server.usage.UsageStatsService mService;

    UsageStatsShellCommand(com.android.server.usage.UsageStatsService usageStatsService) {
        this.mService = usageStatsService;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0024  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r2) {
        /*
            r1 = this;
            if (r2 != 0) goto L8
            r0 = 0
            int r0 = r1.handleDefaultCommands(r0)
            return r0
        L8:
            int r0 = r2.hashCode()
            switch(r0) {
                case 949945779: goto L1a;
                case 2135796854: goto L10;
                default: goto Lf;
            }
        Lf:
            goto L24
        L10:
            java.lang.String r0 = "clear-last-used-timestamps"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Lf
            r0 = 0
            goto L25
        L1a:
            java.lang.String r0 = "delete-package-data"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Lf
            r0 = 1
            goto L25
        L24:
            r0 = -1
        L25:
            switch(r0) {
                case 0: goto L32;
                case 1: goto L2d;
                default: goto L28;
            }
        L28:
            int r0 = r1.handleDefaultCommands(r2)
            return r0
        L2d:
            int r0 = r1.deletePackageData()
            return r0
        L32:
            int r0 = r1.runClearLastUsedTimestamps()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usage.UsageStatsShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("UsageStats service (usagestats) commands:");
        pw.println("help");
        pw.println("    Print this help text.");
        pw.println();
        pw.println("clear-last-used-timestamps PACKAGE_NAME [-u | --user USER_ID]");
        pw.println("    Clears the last used timestamps for the given package.");
        pw.println();
        pw.println("delete-package-data PACKAGE_NAME [-u | --user USER_ID]");
        pw.println("    Deletes all the usage stats for the given package.");
        pw.println();
    }

    private int runClearLastUsedTimestamps() {
        java.lang.String packageName = getNextArgRequired();
        int userId = getUserId();
        if (userId == -1) {
            return -1;
        }
        this.mService.clearLastUsedTimestamps(packageName, userId);
        return 0;
    }

    private int deletePackageData() {
        java.lang.String packageName = getNextArgRequired();
        int userId = getUserId();
        if (userId == -1) {
            return -1;
        }
        this.mService.deletePackageData(packageName, userId);
        return 0;
    }

    private int getUserId() {
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if ("-u".equals(opt) || "--user".equals(opt)) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: unknown option: " + opt);
                    return -1;
                }
            } else {
                if (userId == -2) {
                    int userId2 = android.app.ActivityManager.getCurrentUser();
                    return userId2;
                }
                return userId;
            }
        }
    }
}
