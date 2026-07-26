package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public final class JobSchedulerShellCommand extends com.android.modules.utils.BasicShellCommandHandler {
    static final int BYTE_OPTION_DOWNLOAD = 0;
    static final int BYTE_OPTION_UPLOAD = 1;
    public static final int CMD_ERR_CONSTRAINTS = -1002;
    public static final int CMD_ERR_NO_JOB = -1001;
    public static final int CMD_ERR_NO_PACKAGE = -1000;
    com.android.server.job.JobSchedulerService mInternal;
    android.content.pm.IPackageManager mPM = android.app.AppGlobals.getPackageManager();

    JobSchedulerShellCommand(com.android.server.job.JobSchedulerService service) {
        this.mInternal = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r7) {
        /*
            Method dump skipped, instruction units count: 630
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.JobSchedulerShellCommand.onCommand(java.lang.String):int");
    }

    private void checkPermission(java.lang.String operation) throws java.lang.Exception {
        checkPermission(operation, "android.permission.CHANGE_APP_IDLE_STATE");
    }

    private void checkPermission(java.lang.String operation, java.lang.String permission) throws java.lang.Exception {
        int uid = android.os.Binder.getCallingUid();
        if (uid == 0) {
            return;
        }
        int perm = this.mPM.checkUidPermission(permission, uid);
        if (perm != 0) {
            throw new java.lang.SecurityException("Uid " + uid + " not permitted to " + operation);
        }
    }

    private boolean printError(int errCode, java.lang.String pkgName, int userId, java.lang.String namespace, int jobId) {
        switch (errCode) {
            case CMD_ERR_CONSTRAINTS /* -1002 */:
                java.io.PrintWriter pw = getErrPrintWriter();
                pw.print("Job ");
                pw.print(jobId);
                pw.print(" in package ");
                pw.print(pkgName);
                if (namespace != null) {
                    pw.print(" / namespace ");
                    pw.print(namespace);
                }
                pw.print(" / user ");
                pw.print(userId);
                pw.println(" has functional constraints but --force not specified");
                break;
            case CMD_ERR_NO_JOB /* -1001 */:
                java.io.PrintWriter pw2 = getErrPrintWriter();
                pw2.print("Could not find job ");
                pw2.print(jobId);
                pw2.print(" in package ");
                pw2.print(pkgName);
                if (namespace != null) {
                    pw2.print(" / namespace ");
                    pw2.print(namespace);
                }
                pw2.print(" / user ");
                pw2.println(userId);
                break;
            case -1000:
                java.io.PrintWriter pw3 = getErrPrintWriter();
                pw3.print("Package not found: ");
                pw3.print(pkgName);
                pw3.print(" / user ");
                pw3.println(userId);
                break;
        }
        return true;
    }

    private int runJob(java.io.PrintWriter pw) throws java.lang.Exception {
        int userId;
        byte b;
        checkPermission("force scheduled jobs");
        int userId2 = 0;
        boolean force = false;
        boolean satisfied = false;
        java.lang.String namespace = null;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                switch (opt.hashCode()) {
                    case -1626076853:
                        b = !opt.equals("--force") ? (byte) -1 : (byte) 1;
                        break;
                    case -969907566:
                        b = !opt.equals("--satisfied") ? (byte) -1 : (byte) 3;
                        break;
                    case 1497:
                        b = !opt.equals("-f") ? (byte) -1 : (byte) 0;
                        break;
                    case 1505:
                        b = !opt.equals("-n") ? (byte) -1 : (byte) 6;
                        break;
                    case 1510:
                        b = !opt.equals("-s") ? (byte) -1 : (byte) 2;
                        break;
                    case 1512:
                        b = !opt.equals("-u") ? (byte) -1 : (byte) 4;
                        break;
                    case 1333469547:
                        b = !opt.equals("--user") ? (byte) -1 : (byte) 5;
                        break;
                    case 1740612539:
                        b = !opt.equals("--namespace") ? (byte) -1 : (byte) 7;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        force = true;
                        break;
                    case 2:
                    case 3:
                        satisfied = true;
                        break;
                    case 4:
                    case 5:
                        java.lang.String namespace2 = getNextArgRequired();
                        int userId3 = android.os.UserHandle.parseUserArg(namespace2);
                        userId2 = userId3;
                        break;
                    case 6:
                    case 7:
                        java.lang.String namespace3 = getNextArgRequired();
                        namespace = namespace3;
                        break;
                    default:
                        pw.println("Error: unknown option '" + opt + "'");
                        return -1;
                }
            } else {
                if (force && satisfied) {
                    pw.println("Cannot specify both --force and --satisfied");
                    return -1;
                }
                if (userId2 != -2) {
                    userId = userId2;
                } else {
                    int userId4 = android.app.ActivityManager.getCurrentUser();
                    userId = userId4;
                }
                java.lang.String pkgName = getNextArgRequired();
                int jobId = java.lang.Integer.parseInt(getNextArgRequired());
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    int ret = this.mInternal.executeRunCommand(pkgName, userId, namespace, jobId, satisfied, force);
                    try {
                        if (!printError(ret, pkgName, userId, namespace, jobId)) {
                            pw.print("Running job");
                            if (force) {
                                pw.print(" [FORCED]");
                            }
                            pw.println();
                            android.os.Binder.restoreCallingIdentity(ident);
                            return ret;
                        }
                        android.os.Binder.restoreCallingIdentity(ident);
                        return ret;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(ident);
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }
    }

    private int timeout(java.io.PrintWriter pw) throws java.lang.Exception {
        int userId;
        checkPermission("force timeout jobs");
        int userId2 = -1;
        java.lang.String namespace = null;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 1;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1505:
                        b = !opt.equals("-n") ? (byte) -1 : (byte) 2;
                        break;
                    case 1512:
                        b = !opt.equals("-u") ? (byte) -1 : (byte) 0;
                        break;
                    case 1333469547:
                        if (!opt.equals("--user")) {
                            b = -1;
                        }
                        break;
                    case 1740612539:
                        b = !opt.equals("--namespace") ? (byte) -1 : (byte) 3;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        java.lang.String namespace2 = getNextArgRequired();
                        userId2 = android.os.UserHandle.parseUserArg(namespace2);
                        break;
                    case 2:
                    case 3:
                        java.lang.String namespace3 = getNextArgRequired();
                        namespace = namespace3;
                        break;
                    default:
                        pw.println("Error: unknown option '" + opt + "'");
                        return -1;
                }
            } else {
                if (userId2 != -2) {
                    userId = userId2;
                } else {
                    int userId3 = android.app.ActivityManager.getCurrentUser();
                    userId = userId3;
                }
                java.lang.String pkgName = getNextArg();
                java.lang.String jobIdStr = getNextArg();
                int jobId = jobIdStr != null ? java.lang.Integer.parseInt(jobIdStr) : -1;
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    return this.mInternal.executeStopCommand(pw, pkgName, userId, namespace, jobIdStr != null, jobId, 3, 3);
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        }
    }

    private int cancelJob(java.io.PrintWriter pw) throws java.lang.Exception {
        checkPermission("cancel jobs");
        int userId = 0;
        java.lang.String namespace = null;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 1;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1505:
                        b = !opt.equals("-n") ? (byte) -1 : (byte) 2;
                        break;
                    case 1512:
                        b = !opt.equals("-u") ? (byte) -1 : (byte) 0;
                        break;
                    case 1333469547:
                        if (!opt.equals("--user")) {
                            b = -1;
                        }
                        break;
                    case 1740612539:
                        b = !opt.equals("--namespace") ? (byte) -1 : (byte) 3;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    case 2:
                    case 3:
                        namespace = getNextArgRequired();
                        break;
                    default:
                        pw.println("Error: unknown option '" + opt + "'");
                        return -1;
                }
            } else {
                if (userId < 0) {
                    pw.println("Error: must specify a concrete user ID");
                    return -1;
                }
                java.lang.String pkgName = getNextArg();
                java.lang.String jobIdStr = getNextArg();
                int jobId = jobIdStr != null ? java.lang.Integer.parseInt(jobIdStr) : -1;
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    return this.mInternal.executeCancelCommand(pw, pkgName, userId, namespace, jobIdStr != null, jobId);
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        }
    }

    private int monitorBattery(java.io.PrintWriter pw) throws java.lang.Exception {
        boolean enabled;
        checkPermission("change battery monitoring");
        java.lang.String opt = getNextArgRequired();
        if (kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON.equals(opt)) {
            enabled = true;
        } else if (kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF.equals(opt)) {
            enabled = false;
        } else {
            getErrPrintWriter().println("Error: unknown option " + opt);
            return 1;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mInternal.setMonitorBattery(enabled);
            if (enabled) {
                pw.println("Battery monitoring enabled");
            } else {
                pw.println("Battery monitoring disabled");
            }
            android.os.Binder.restoreCallingIdentity(ident);
            return 0;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    private int disableFlexPolicy(java.io.PrintWriter pw) throws java.lang.Exception {
        checkPermission("disable flex policy");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mInternal.setFlexPolicy(true, 0);
            pw.println("Set flex policy to 0");
            return 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:96)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:282)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:65)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private int enableFlexPolicy(java.io.PrintWriter r8) throws java.lang.Exception {
        /*
            Method dump skipped, instruction units count: 262
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.JobSchedulerShellCommand.enableFlexPolicy(java.io.PrintWriter):int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0050  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int getAconfigFlagState(java.io.PrintWriter r5) throws java.lang.Exception {
        /*
            r4 = this;
            java.lang.String r0 = "get aconfig flag state"
            java.lang.String r1 = "android.permission.DUMP"
            r4.checkPermission(r0, r1)
            java.lang.String r0 = r4.getNextArgRequired()
            int r1 = r0.hashCode()
            r2 = 0
            switch(r1) {
                case -963776836: goto L46;
                case -946452577: goto L3c;
                case -930760458: goto L32;
                case -616213836: goto L28;
                case -355604736: goto L1e;
                case -198167065: goto L14;
                default: goto L13;
            }
        L13:
            goto L50
        L14:
            java.lang.String r1 = "com.android.server.job.batch_active_bucket_jobs"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L13
            r1 = 2
            goto L51
        L1e:
            java.lang.String r1 = "android.app.job.backup_jobs_exemption"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L13
            r1 = 5
            goto L51
        L28:
            java.lang.String r1 = "com.android.server.job.do_not_force_rush_execution_at_boot"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L13
            r1 = 4
            goto L51
        L32:
            java.lang.String r1 = "com.android.server.job.batch_connectivity_jobs_per_network"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L13
            r1 = 3
            goto L51
        L3c:
            java.lang.String r1 = "android.app.job.job_debug_info_apis"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L13
            r1 = 1
            goto L51
        L46:
            java.lang.String r1 = "android.app.job.enforce_minimum_time_windows"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L13
            r1 = r2
            goto L51
        L50:
            r1 = -1
        L51:
            switch(r1) {
                case 0: goto L93;
                case 1: goto L8b;
                case 2: goto L83;
                case 3: goto L7b;
                case 4: goto L73;
                case 5: goto L6b;
                default: goto L54;
            }
        L54:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "Unknown flag: "
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.StringBuilder r1 = r1.append(r0)
            java.lang.String r1 = r1.toString()
            r5.println(r1)
            goto L9b
        L6b:
            boolean r1 = com.android.internal.hidden_from_bootclasspath.android.app.job.Flags.backupJobsExemption()
            r5.println(r1)
            goto L9b
        L73:
            boolean r1 = com.android.server.job.Flags.doNotForceRushExecutionAtBoot()
            r5.println(r1)
            goto L9b
        L7b:
            boolean r1 = com.android.server.job.Flags.batchConnectivityJobsPerNetwork()
            r5.println(r1)
            goto L9b
        L83:
            boolean r1 = com.android.server.job.Flags.batchActiveBucketJobs()
            r5.println(r1)
            goto L9b
        L8b:
            boolean r1 = com.android.internal.hidden_from_bootclasspath.android.app.job.Flags.jobDebugInfoApis()
            r5.println(r1)
            goto L9b
        L93:
            boolean r1 = com.android.internal.hidden_from_bootclasspath.android.app.job.Flags.enforceMinimumTimeWindows()
            r5.println(r1)
        L9b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.job.JobSchedulerShellCommand.getAconfigFlagState(java.io.PrintWriter):int");
    }

    private int getBatterySeq(java.io.PrintWriter pw) {
        int seq = this.mInternal.getBatterySeq();
        pw.println(seq);
        return 0;
    }

    private int getBatteryCharging(java.io.PrintWriter pw) {
        boolean val = this.mInternal.isBatteryCharging();
        pw.println(val);
        return 0;
    }

    private int getBatteryNotLow(java.io.PrintWriter pw) {
        boolean val = this.mInternal.isBatteryNotLow();
        pw.println(val);
        return 0;
    }

    private int getConfigValue(java.io.PrintWriter pw) throws java.lang.Exception {
        checkPermission("get device config value", "android.permission.DUMP");
        java.lang.String key = getNextArgRequired();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            pw.println(this.mInternal.getConfigValue(key));
            android.os.Binder.restoreCallingIdentity(ident);
            return 0;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    private int getEstimatedNetworkBytes(java.io.PrintWriter pw, int byteOption) throws java.lang.Exception {
        int userId;
        byte b;
        checkPermission("get estimated bytes");
        int userId2 = 0;
        java.lang.String namespace = null;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1505:
                        b = !opt.equals("-n") ? (byte) -1 : (byte) 2;
                        break;
                    case 1512:
                        b = !opt.equals("-u") ? (byte) -1 : (byte) 0;
                        break;
                    case 1333469547:
                        b = !opt.equals("--user") ? (byte) -1 : (byte) 1;
                        break;
                    case 1740612539:
                        b = !opt.equals("--namespace") ? (byte) -1 : (byte) 3;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        java.lang.String namespace2 = getNextArgRequired();
                        userId2 = android.os.UserHandle.parseUserArg(namespace2);
                        break;
                    case 2:
                    case 3:
                        java.lang.String namespace3 = getNextArgRequired();
                        namespace = namespace3;
                        break;
                    default:
                        pw.println("Error: unknown option '" + opt + "'");
                        return -1;
                }
            } else {
                if (userId2 != -2) {
                    userId = userId2;
                } else {
                    int userId3 = android.app.ActivityManager.getCurrentUser();
                    userId = userId3;
                }
                java.lang.String pkgName = getNextArgRequired();
                java.lang.String jobIdStr = getNextArgRequired();
                int jobId = java.lang.Integer.parseInt(jobIdStr);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    int ret = this.mInternal.getEstimatedNetworkBytes(pw, pkgName, userId, namespace, jobId, byteOption);
                    try {
                        printError(ret, pkgName, userId, namespace, jobId);
                        android.os.Binder.restoreCallingIdentity(ident);
                        return ret;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(ident);
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }
    }

    private int getStorageSeq(java.io.PrintWriter pw) {
        int seq = this.mInternal.getStorageSeq();
        pw.println(seq);
        return 0;
    }

    private int getStorageNotLow(java.io.PrintWriter pw) {
        boolean val = this.mInternal.getStorageNotLow();
        pw.println(val);
        return 0;
    }

    private int getTransferredNetworkBytes(java.io.PrintWriter pw, int byteOption) throws java.lang.Exception {
        int userId;
        byte b;
        checkPermission("get transferred bytes");
        int userId2 = 0;
        java.lang.String namespace = null;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1505:
                        b = !opt.equals("-n") ? (byte) -1 : (byte) 2;
                        break;
                    case 1512:
                        b = !opt.equals("-u") ? (byte) -1 : (byte) 0;
                        break;
                    case 1333469547:
                        b = !opt.equals("--user") ? (byte) -1 : (byte) 1;
                        break;
                    case 1740612539:
                        b = !opt.equals("--namespace") ? (byte) -1 : (byte) 3;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        java.lang.String namespace2 = getNextArgRequired();
                        userId2 = android.os.UserHandle.parseUserArg(namespace2);
                        break;
                    case 2:
                    case 3:
                        java.lang.String namespace3 = getNextArgRequired();
                        namespace = namespace3;
                        break;
                    default:
                        pw.println("Error: unknown option '" + opt + "'");
                        return -1;
                }
            } else {
                if (userId2 != -2) {
                    userId = userId2;
                } else {
                    int userId3 = android.app.ActivityManager.getCurrentUser();
                    userId = userId3;
                }
                java.lang.String pkgName = getNextArgRequired();
                java.lang.String jobIdStr = getNextArgRequired();
                int jobId = java.lang.Integer.parseInt(jobIdStr);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    int ret = this.mInternal.getTransferredNetworkBytes(pw, pkgName, userId, namespace, jobId, byteOption);
                    try {
                        printError(ret, pkgName, userId, namespace, jobId);
                        android.os.Binder.restoreCallingIdentity(ident);
                        return ret;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(ident);
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }
    }

    private int getJobState(java.io.PrintWriter pw) throws java.lang.Exception {
        int userId;
        byte b;
        checkPermission("get job state");
        int userId2 = 0;
        java.lang.String namespace = null;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1505:
                        b = !opt.equals("-n") ? (byte) -1 : (byte) 2;
                        break;
                    case 1512:
                        b = !opt.equals("-u") ? (byte) -1 : (byte) 0;
                        break;
                    case 1333469547:
                        b = !opt.equals("--user") ? (byte) -1 : (byte) 1;
                        break;
                    case 1740612539:
                        b = !opt.equals("--namespace") ? (byte) -1 : (byte) 3;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        java.lang.String namespace2 = getNextArgRequired();
                        userId2 = android.os.UserHandle.parseUserArg(namespace2);
                        break;
                    case 2:
                    case 3:
                        java.lang.String namespace3 = getNextArgRequired();
                        namespace = namespace3;
                        break;
                    default:
                        pw.println("Error: unknown option '" + opt + "'");
                        return -1;
                }
            } else {
                if (userId2 != -2) {
                    userId = userId2;
                } else {
                    int userId3 = android.app.ActivityManager.getCurrentUser();
                    userId = userId3;
                }
                java.lang.String pkgName = getNextArgRequired();
                java.lang.String jobIdStr = getNextArgRequired();
                int jobId = java.lang.Integer.parseInt(jobIdStr);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    int ret = this.mInternal.getJobState(pw, pkgName, userId, namespace, jobId);
                    printError(ret, pkgName, userId, namespace, jobId);
                    return ret;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        }
    }

    private int doHeartbeat(java.io.PrintWriter pw) throws java.lang.Exception {
        checkPermission("manipulate scheduler heartbeat");
        pw.println("Heartbeat command is no longer supported");
        return -1;
    }

    private int cacheConfigChanges(java.io.PrintWriter pw) throws java.lang.Exception {
        boolean enabled;
        checkPermission("change config caching", "android.permission.DUMP");
        java.lang.String opt = getNextArgRequired();
        if (kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON.equals(opt)) {
            enabled = true;
        } else if (kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF.equals(opt)) {
            enabled = false;
        } else {
            getErrPrintWriter().println("Error: unknown option " + opt);
            return 1;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mInternal.setCacheConfigChanges(enabled);
            pw.println("Config caching " + (enabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED));
            android.os.Binder.restoreCallingIdentity(ident);
            return 0;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    private int resetFlexPolicy(java.io.PrintWriter pw) throws java.lang.Exception {
        checkPermission("reset flex policy");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mInternal.setFlexPolicy(false, 0);
            pw.println("Reset flex policy to its default state");
            return 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private int resetExecutionQuota(java.io.PrintWriter pw) throws java.lang.Exception {
        checkPermission("reset execution quota");
        int userId = 0;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1512:
                        if (!opt.equals("-u")) {
                            b = -1;
                        }
                        break;
                    case 1333469547:
                        b = !opt.equals("--user") ? (byte) -1 : (byte) 1;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    default:
                        pw.println("Error: unknown option '" + opt + "'");
                        return -1;
                }
            } else {
                if (userId == -2) {
                    userId = android.app.ActivityManager.getCurrentUser();
                }
                java.lang.String pkgName = getNextArgRequired();
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    this.mInternal.resetExecutionQuota(pkgName, userId);
                    return 0;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        }
    }

    private int resetScheduleQuota(java.io.PrintWriter pw) throws java.lang.Exception {
        checkPermission("reset schedule quota");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mInternal.resetScheduleQuota();
            android.os.Binder.restoreCallingIdentity(ident);
            return 0;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    private int stop(java.io.PrintWriter pw) throws java.lang.Exception {
        int userId;
        checkPermission("stop jobs");
        int userId2 = -1;
        java.lang.String namespace = null;
        int stopReason = 13;
        int internalStopReason = -1;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 1;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case -1405909809:
                        b = !opt.equals("--stop-reason") ? (byte) -1 : (byte) 5;
                        break;
                    case android.net.util.NetworkConstants.ETHER_MTU /* 1500 */:
                        b = !opt.equals("-i") ? (byte) -1 : (byte) 6;
                        break;
                    case 1505:
                        b = !opt.equals("-n") ? (byte) -1 : (byte) 2;
                        break;
                    case 1510:
                        b = !opt.equals("-s") ? (byte) -1 : (byte) 4;
                        break;
                    case 1512:
                        b = !opt.equals("-u") ? (byte) -1 : (byte) 0;
                        break;
                    case 617801983:
                        b = !opt.equals("--internal-stop-reason") ? (byte) -1 : (byte) 7;
                        break;
                    case 1333469547:
                        if (!opt.equals("--user")) {
                            b = -1;
                        }
                        break;
                    case 1740612539:
                        b = !opt.equals("--namespace") ? (byte) -1 : (byte) 3;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        java.lang.String namespace2 = getNextArgRequired();
                        userId2 = android.os.UserHandle.parseUserArg(namespace2);
                        break;
                    case 2:
                    case 3:
                        java.lang.String namespace3 = getNextArgRequired();
                        namespace = namespace3;
                        break;
                    case 4:
                    case 5:
                        int stopReason2 = java.lang.Integer.parseInt(getNextArgRequired());
                        stopReason = stopReason2;
                        break;
                    case 6:
                    case 7:
                        int internalStopReason2 = java.lang.Integer.parseInt(getNextArgRequired());
                        internalStopReason = internalStopReason2;
                        break;
                    default:
                        pw.println("Error: unknown option '" + opt + "'");
                        return -1;
                }
            } else {
                if (userId2 != -2) {
                    userId = userId2;
                } else {
                    int userId3 = android.app.ActivityManager.getCurrentUser();
                    userId = userId3;
                }
                java.lang.String pkgName = getNextArg();
                java.lang.String jobIdStr = getNextArg();
                int jobId = jobIdStr != null ? java.lang.Integer.parseInt(jobIdStr) : -1;
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    return this.mInternal.executeStopCommand(pw, pkgName, userId, namespace, jobIdStr != null, jobId, stopReason, internalStopReason);
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        }
    }

    private int triggerDockState(java.io.PrintWriter pw) throws java.lang.Exception {
        boolean idleState;
        checkPermission("trigger wireless charging dock state");
        java.lang.String opt = getNextArgRequired();
        if ("idle".equals(opt)) {
            idleState = true;
        } else if (com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE.equals(opt)) {
            idleState = false;
        } else {
            getErrPrintWriter().println("Error: unknown option " + opt);
            return 1;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mInternal.triggerDockState(idleState);
            android.os.Binder.restoreCallingIdentity(ident);
            return 0;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Job scheduler (jobscheduler) commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("  run [-f | --force] [-s | --satisfied] [-u | --user USER_ID] [-n | --namespace NAMESPACE] PACKAGE JOB_ID");
        pw.println("    Trigger immediate execution of a specific scheduled job. For historical");
        pw.println("    reasons, some constraints, such as battery, are ignored when this");
        pw.println("    command is called. If you don't want any constraints to be ignored,");
        pw.println("    include the -s flag.");
        pw.println("    Options:");
        pw.println("      -f or --force: run the job even if technical constraints such as");
        pw.println("         connectivity are not currently met. This is incompatible with -f ");
        pw.println("         and so an error will be reported if both are given.");
        pw.println("      -n or --namespace: specify the namespace this job sits in; the default");
        pw.println("         is null (no namespace).");
        pw.println("      -s or --satisfied: run the job only if all constraints are met.");
        pw.println("         This is incompatible with -f and so an error will be reported");
        pw.println("         if both are given.");
        pw.println("      -u or --user: specify which user's job is to be run; the default is");
        pw.println("         the primary or system user");
        pw.println("  stop [-u | --user USER_ID] [-n | --namespace NAMESPACE] [-s | --stop-reason STOP_REASON] [-i | --internal-stop-reason STOP_REASON] [PACKAGE] [JOB_ID]");
        pw.println("    Trigger immediate stop of currently executing jobs using the specified");
        pw.println("    stop reasons.");
        pw.println("    Options:");
        pw.println("      -u or --user: specify which user's job is to be run; the default is");
        pw.println("         all users");
        pw.println("      -n or --namespace: specify the namespace this job sits in; the default");
        pw.println("         is null (no namespace).");
        pw.println("      -s or --stop-reason: specify the stop reason given to the job.");
        pw.println("         Valid values are those that can be returned from");
        pw.println("         JobParameters.getStopReason().");
        pw.println("          The default value is STOP_REASON_USER.");
        pw.println("      -i or --internal-stop-reason: specify the internal stop reason.");
        pw.println("         JobScheduler will use for internal processing.");
        pw.println("         Valid values are those that can be returned from");
        pw.println("         JobParameters.getInternalStopReason().");
        pw.println("          The default value is INTERNAL_STOP_REASON_UNDEFINED.");
        pw.println("  timeout [-u | --user USER_ID] [-n | --namespace NAMESPACE] [PACKAGE] [JOB_ID]");
        pw.println("    Trigger immediate timeout of currently executing jobs, as if their");
        pw.println("    execution timeout had expired.");
        pw.println("    This is the equivalent of calling `stop -s 3 -i 3`.");
        pw.println("    Options:");
        pw.println("      -u or --user: specify which user's job is to be run; the default is");
        pw.println("         all users");
        pw.println("      -n or --namespace: specify the namespace this job sits in; the default");
        pw.println("         is null (no namespace).");
        pw.println("  cancel [-u | --user USER_ID] [-n | --namespace NAMESPACE] PACKAGE [JOB_ID]");
        pw.println("    Cancel a scheduled job.  If a job ID is not supplied, all jobs scheduled");
        pw.println("    by that package will be canceled.  USE WITH CAUTION.");
        pw.println("    Options:");
        pw.println("      -u or --user: specify which user's job is to be run; the default is");
        pw.println("         the primary or system user");
        pw.println("      -n or --namespace: specify the namespace this job sits in; the default");
        pw.println("         is null (no namespace).");
        pw.println("  heartbeat [num]");
        pw.println("    No longer used.");
        pw.println("  cache-config-changes [on|off]");
        pw.println("    Control caching the set of most recently processed config flags.");
        pw.println("    Off by default.  Turning on makes get-config-value useful.");
        pw.println("  monitor-battery [on|off]");
        pw.println("    Control monitoring of all battery changes.  Off by default.  Turning");
        pw.println("    on makes get-battery-seq useful.");
        pw.println("  enable-flex-policy --option <option>");
        pw.println("    Enable flex policy with the specified options. Supported options are");
        pw.println("    battery-not-low, charging, connectivity, idle.");
        pw.println("    Multiple enable options can be specified (e.g.");
        pw.println("    enable-flex-policy --option battery-not-low --option charging");
        pw.println("  disable-flex-policy");
        pw.println("    Turn off flex policy so that it does not affect job execution.");
        pw.println("  reset-flex-policy");
        pw.println("    Resets the flex policy to its default state.");
        pw.println("  get-aconfig-flag-state FULL_FLAG_NAME");
        pw.println("    Return the state of the specified aconfig flag, if known. The flag name");
        pw.println("         must be fully qualified.");
        pw.println("  get-battery-seq");
        pw.println("    Return the last battery update sequence number that was received.");
        pw.println("  get-battery-charging");
        pw.println("    Return whether the battery is currently considered to be charging.");
        pw.println("  get-battery-not-low");
        pw.println("    Return whether the battery is currently considered to not be low.");
        pw.println("  get-config-value KEY");
        pw.println("    Return the most recently processed and cached config value for the KEY.");
        pw.println("    Only useful if caching is turned on with cache-config-changes.");
        pw.println("  get-estimated-download-bytes [-u | --user USER_ID] [-n | --namespace NAMESPACE] PACKAGE JOB_ID");
        pw.println("    Return the most recent estimated download bytes for the job.");
        pw.println("    Options:");
        pw.println("      -u or --user: specify which user's job is to be run; the default is");
        pw.println("         the primary or system user");
        pw.println("  get-estimated-upload-bytes [-u | --user USER_ID] [-n | --namespace NAMESPACE] PACKAGE JOB_ID");
        pw.println("    Return the most recent estimated upload bytes for the job.");
        pw.println("    Options:");
        pw.println("      -u or --user: specify which user's job is to be run; the default is");
        pw.println("         the primary or system user");
        pw.println("  get-storage-seq");
        pw.println("    Return the last storage update sequence number that was received.");
        pw.println("  get-storage-not-low");
        pw.println("    Return whether storage is currently considered to not be low.");
        pw.println("  get-transferred-download-bytes [-u | --user USER_ID] [-n | --namespace NAMESPACE] PACKAGE JOB_ID");
        pw.println("    Return the most recent transferred download bytes for the job.");
        pw.println("    Options:");
        pw.println("      -u or --user: specify which user's job is to be run; the default is");
        pw.println("         the primary or system user");
        pw.println("  get-transferred-upload-bytes [-u | --user USER_ID] [-n | --namespace NAMESPACE] PACKAGE JOB_ID");
        pw.println("    Return the most recent transferred upload bytes for the job.");
        pw.println("    Options:");
        pw.println("      -u or --user: specify which user's job is to be run; the default is");
        pw.println("         the primary or system user");
        pw.println("  get-job-state [-u | --user USER_ID] [-n | --namespace NAMESPACE] PACKAGE JOB_ID");
        pw.println("    Return the current state of a job, may be any combination of:");
        pw.println("      pending: currently on the pending list, waiting to be active");
        pw.println("      active: job is actively running");
        pw.println("      user-stopped: job can't run because its user is stopped");
        pw.println("      backing-up: job can't run because app is currently backing up its data");
        pw.println("      no-component: job can't run because its component is not available");
        pw.println("      ready: job is ready to run (all constraints satisfied or bypassed)");
        pw.println("      waiting: if nothing else above is printed, job not ready to run");
        pw.println("    Options:");
        pw.println("      -u or --user: specify which user's job is to be run; the default is");
        pw.println("         the primary or system user");
        pw.println("      -n or --namespace: specify the namespace this job sits in; the default");
        pw.println("         is null (no namespace).");
        pw.println("  trigger-dock-state [idle|active]");
        pw.println("    Trigger wireless charging dock state.  Active by default.");
        pw.println();
    }
}
