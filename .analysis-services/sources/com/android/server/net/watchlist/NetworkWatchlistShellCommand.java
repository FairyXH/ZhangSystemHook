package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
class NetworkWatchlistShellCommand extends android.os.ShellCommand {
    final android.content.Context mContext;
    final com.android.server.net.watchlist.NetworkWatchlistService mService;

    NetworkWatchlistShellCommand(com.android.server.net.watchlist.NetworkWatchlistService service, android.content.Context context) {
        this.mContext = context;
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r6) {
        /*
            r5 = this;
            if (r6 != 0) goto L7
            int r0 = r5.handleDefaultCommands(r6)
            return r0
        L7:
            java.io.PrintWriter r0 = r5.getOutPrintWriter()
            r1 = -1
            int r2 = r6.hashCode()     // Catch: java.lang.Exception -> L3d
            switch(r2) {
                case 1757613042: goto L1e;
                case 1854202282: goto L14;
                default: goto L13;
            }     // Catch: java.lang.Exception -> L3d
        L13:
            goto L29
        L14:
            java.lang.String r2 = "force-generate-report"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L3d
            if (r2 == 0) goto L13
            r2 = 1
            goto L2a
        L1e:
            java.lang.String r2 = "set-test-config"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L3d
            if (r2 == 0) goto L13
            r2 = 0
            goto L2a
        L29:
            r2 = r1
        L2a:
            switch(r2) {
                case 0: goto L37;
                case 1: goto L32;
                default: goto L2d;
            }     // Catch: java.lang.Exception -> L3d
        L2d:
            int r1 = r5.handleDefaultCommands(r6)     // Catch: java.lang.Exception -> L3d
            goto L3c
        L32:
            int r1 = r5.runForceGenerateReport()     // Catch: java.lang.Exception -> L3d
            return r1
        L37:
            int r1 = r5.runSetTestConfig()     // Catch: java.lang.Exception -> L3d
            return r1
        L3c:
            return r1
        L3d:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Exception: "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r2)
            java.lang.String r3 = r3.toString()
            r0.println(r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.net.watchlist.NetworkWatchlistShellCommand.onCommand(java.lang.String):int");
    }

    private int runSetTestConfig() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            java.lang.String configXmlPath = getNextArgRequired();
            android.os.ParcelFileDescriptor pfd = openFileForSystem(configXmlPath, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD);
            if (pfd == null) {
                pw.println("Error: can't open input file " + configXmlPath);
                return -1;
            }
            java.io.InputStream inputStream = new android.os.ParcelFileDescriptor.AutoCloseInputStream(pfd);
            try {
                com.android.server.net.watchlist.WatchlistConfig.getInstance().setTestMode(inputStream);
                inputStream.close();
                pw.println("Success!");
                return 0;
            } finally {
            }
        } catch (java.lang.Exception ex) {
            pw.println("Error: " + ex.toString());
            return -1;
        }
    }

    private int runForceGenerateReport() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            if (com.android.server.net.watchlist.WatchlistConfig.getInstance().isConfigSecure()) {
                pw.println("Error: Cannot force generate report under production config");
                return -1;
            }
            android.provider.Settings.Global.putLong(this.mContext.getContentResolver(), "network_watchlist_last_report_time", 0L);
            this.mService.forceReportWatchlistForTest(java.lang.System.currentTimeMillis());
            pw.println("Success!");
            android.os.Binder.restoreCallingIdentity(ident);
            return 0;
        } catch (java.lang.Exception ex) {
            pw.println("Error: " + ex);
            return -1;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Network watchlist manager commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("  set-test-config your_watchlist_config.xml");
        pw.println("    Set network watchlist test config file.");
        pw.println("  force-generate-report");
        pw.println("    Force generate watchlist test report.");
    }
}
