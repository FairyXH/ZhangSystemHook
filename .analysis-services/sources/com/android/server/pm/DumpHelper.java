package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class DumpHelper {
    private final android.util.ArrayMap<java.lang.String, android.content.pm.FeatureInfo> mAvailableFeatures;
    private final com.android.server.pm.ChangedPackagesTracker mChangedPackagesTracker;
    private final com.android.server.pm.verify.domain.DomainVerificationManagerInternal mDomainVerificationManager;
    public com.android.server.pm.IDumpHelperExt mDumpHelperExt = (com.android.server.pm.IDumpHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IDumpHelperExt.class).base(this).create();
    private final com.android.server.pm.PackageInstallerService mInstallerService;
    private final com.android.server.pm.KnownPackages mKnownPackages;
    private final android.os.incremental.PerUidReadTimeouts[] mPerUidReadTimeouts;
    private final com.android.server.pm.permission.PermissionManagerServiceInternal mPermissionManager;
    private final android.util.ArraySet<java.lang.String> mProtectedBroadcasts;
    private final java.lang.String[] mRequiredVerifierPackages;
    private final com.android.server.pm.SnapshotStatistics mSnapshotStatistics;
    private final com.android.server.pm.StorageEventHelper mStorageEventHelper;

    DumpHelper(com.android.server.pm.permission.PermissionManagerServiceInternal permissionManager, com.android.server.pm.StorageEventHelper storageEventHelper, com.android.server.pm.verify.domain.DomainVerificationManagerInternal domainVerificationManager, com.android.server.pm.PackageInstallerService installerService, java.lang.String[] requiredVerifierPackages, com.android.server.pm.KnownPackages knownPackages, com.android.server.pm.ChangedPackagesTracker changedPackagesTracker, android.util.ArrayMap<java.lang.String, android.content.pm.FeatureInfo> availableFeatures, android.util.ArraySet<java.lang.String> protectedBroadcasts, android.os.incremental.PerUidReadTimeouts[] perUidReadTimeouts, com.android.server.pm.SnapshotStatistics snapshotStatistics) {
        this.mPermissionManager = permissionManager;
        this.mStorageEventHelper = storageEventHelper;
        this.mDomainVerificationManager = domainVerificationManager;
        this.mInstallerService = installerService;
        this.mRequiredVerifierPackages = requiredVerifierPackages;
        this.mKnownPackages = knownPackages;
        this.mChangedPackagesTracker = changedPackagesTracker;
        this.mAvailableFeatures = availableFeatures;
        this.mProtectedBroadcasts = protectedBroadcasts;
        this.mPerUidReadTimeouts = perUidReadTimeouts;
        this.mSnapshotStatistics = snapshotStatistics;
    }

    @dalvik.annotation.optimization.NeverCompile
    public void doDump(com.android.server.pm.Computer snapshot, java.io.FileDescriptor fd, final java.io.PrintWriter pw, java.lang.String[] args) {
        android.util.ArraySet<java.lang.String> permissionNames;
        java.lang.String str;
        java.io.FileDescriptor fileDescriptor;
        java.lang.String str2;
        java.lang.String packageName;
        android.util.ArraySet<java.lang.String> permissionNames2;
        int i;
        java.lang.String str3;
        com.android.server.pm.DumpState dumpState;
        java.io.PrintWriter printWriter;
        java.lang.String str4;
        java.lang.String[] strArr;
        java.lang.String str5;
        java.lang.String opt;
        com.android.server.pm.DumpState dumpState2 = new com.android.server.pm.DumpState();
        int opti = 0;
        while (opti < args.length && (opt = args[opti]) != null && opt.length() > 0 && opt.charAt(0) == '-') {
            opti++;
            if (!"-a".equals(opt)) {
                if ("-h".equals(opt)) {
                    printHelp(pw);
                    return;
                }
                if ("--checkin".equals(opt)) {
                    dumpState2.setCheckIn(true);
                } else if ("--all-components".equals(opt)) {
                    dumpState2.setOptionEnabled(2);
                } else if ("-f".equals(opt)) {
                    dumpState2.setOptionEnabled(1);
                } else if ("--include-apex".equals(opt)) {
                    dumpState2.setOptionEnabled(8);
                } else {
                    if ("--proto".equals(opt)) {
                        dumpProto(snapshot, fd);
                        return;
                    }
                    pw.println("Unknown argument: " + opt + "; use -h for help");
                }
            }
        }
        if (opti >= args.length) {
            permissionNames = null;
        } else {
            java.lang.String cmd = args[opti];
            opti++;
            if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(cmd) || cmd.contains(".") || this.mDumpHelperExt.hasOplusPackageName(cmd)) {
                dumpState2.setTargetPackageName(cmd);
                dumpState2.setOptionEnabled(1);
                permissionNames = null;
            } else {
                if ("check-permission".equals(cmd)) {
                    if (opti >= args.length) {
                        pw.println("Error: check-permission missing permission argument");
                        return;
                    }
                    java.lang.String perm = args[opti];
                    int opti2 = opti + 1;
                    if (opti2 >= args.length) {
                        pw.println("Error: check-permission missing package argument");
                        return;
                    }
                    java.lang.String pkg = args[opti2];
                    int opti3 = 1 + opti2;
                    int opti4 = android.os.Binder.getCallingUid();
                    int user = android.os.UserHandle.getUserId(opti4);
                    if (opti3 < args.length) {
                        try {
                            user = java.lang.Integer.parseInt(args[opti3]);
                        } catch (java.lang.NumberFormatException e) {
                            pw.println("Error: check-permission user argument is not a number: " + args[opti3]);
                            return;
                        }
                    }
                    pw.println(this.mPermissionManager.checkPermission(snapshot.resolveInternalPackageName(pkg, -1L), perm, "default:0", user));
                    return;
                }
                if ("l".equals(cmd) || "libraries".equals(cmd)) {
                    dumpState2.setDump(1);
                    permissionNames = null;
                } else if ("f".equals(cmd) || "features".equals(cmd)) {
                    dumpState2.setDump(2);
                    permissionNames = null;
                } else if (com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD.equals(cmd) || "resolvers".equals(cmd)) {
                    if (opti >= args.length) {
                        dumpState2.setDump(60);
                        permissionNames = null;
                    } else {
                        while (opti < args.length) {
                            java.lang.String name = args[opti];
                            if (com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD.equals(name) || com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY.equals(name)) {
                                dumpState2.setDump(4);
                            } else if ("s".equals(name) || com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE.equals(name)) {
                                dumpState2.setDump(8);
                            } else if (com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD.equals(name) || "receiver".equals(name)) {
                                dumpState2.setDump(16);
                            } else if ("c".equals(name) || com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(name)) {
                                dumpState2.setDump(32);
                            } else {
                                pw.println("Error: unknown resolver table type: " + name);
                                return;
                            }
                            opti++;
                        }
                        permissionNames = null;
                    }
                } else if ("perm".equals(cmd) || "permissions".equals(cmd)) {
                    dumpState2.setDump(64);
                    permissionNames = null;
                } else if (com.android.server.permission.access.PermissionUri.SCHEME.equals(cmd)) {
                    if (opti >= args.length) {
                        pw.println("Error: permission requires permission name");
                        return;
                    }
                    android.util.ArraySet<java.lang.String> permissionNames3 = new android.util.ArraySet<>();
                    while (opti < args.length) {
                        permissionNames3.add(args[opti]);
                        opti++;
                    }
                    dumpState2.setDump(com.android.bluetooth.BluetoothStatsLog.LE_AUDIO_BROADCAST_SESSION_REPORTED);
                    permissionNames = permissionNames3;
                } else if ("pref".equals(cmd) || "preferred".equals(cmd)) {
                    dumpState2.setDump(4096);
                    permissionNames = null;
                } else if ("preferred-xml".equals(cmd)) {
                    dumpState2.setDump(8192);
                    if (opti < args.length && "--full".equals(args[opti])) {
                        dumpState2.setFullPreferred(true);
                        opti++;
                        permissionNames = null;
                    } else {
                        permissionNames = null;
                    }
                } else if ("d".equals(cmd) || "domain-preferred-apps".equals(cmd)) {
                    dumpState2.setDump(262144);
                    permissionNames = null;
                } else if ("p".equals(cmd) || "packages".equals(cmd)) {
                    dumpState2.setDump(128);
                    permissionNames = null;
                } else if ("q".equals(cmd) || "queries".equals(cmd)) {
                    dumpState2.setDump(67108864);
                    permissionNames = null;
                } else if ("s".equals(cmd) || "shared-users".equals(cmd)) {
                    dumpState2.setDump(256);
                    if (opti < args.length && "noperm".equals(args[opti])) {
                        dumpState2.setOptionEnabled(4);
                    }
                    permissionNames = null;
                } else if ("prov".equals(cmd) || "providers".equals(cmd)) {
                    dumpState2.setDump(1024);
                    permissionNames = null;
                } else {
                    if ("m".equals(cmd) || "messages".equals(cmd)) {
                        dumpState2.setDump(512);
                    } else if ("v".equals(cmd) || "verifiers".equals(cmd)) {
                        dumpState2.setDump(2048);
                    } else if ("dv".equals(cmd) || "domain-verifier".equals(cmd)) {
                        dumpState2.setDump(131072);
                    } else if ("version".equals(cmd)) {
                        dumpState2.setDump(32768);
                    } else if ("k".equals(cmd) || "keysets".equals(cmd)) {
                        dumpState2.setDump(16384);
                    } else if ("installs".equals(cmd)) {
                        dumpState2.setDump(65536);
                    } else if ("frozen".equals(cmd)) {
                        dumpState2.setDump(524288);
                    } else if ("volumes".equals(cmd)) {
                        dumpState2.setDump(8388608);
                    } else if ("dexopt".equals(cmd)) {
                        dumpState2.setDump(1048576);
                    } else if ("compiler-stats".equals(cmd)) {
                        dumpState2.setDump(2097152);
                    } else if ("changes".equals(cmd)) {
                        dumpState2.setDump(4194304);
                    } else if ("service-permissions".equals(cmd)) {
                        dumpState2.setDump(16777216);
                    } else if ("known-packages".equals(cmd)) {
                        dumpState2.setDump(134217728);
                    } else if ("t".equals(cmd) || "timeouts".equals(cmd)) {
                        dumpState2.setDump(268435456);
                    } else if ("snapshot".equals(cmd)) {
                        dumpState2.setDump(536870912);
                        if (opti < args.length) {
                            if ("--full".equals(args[opti])) {
                                dumpState2.setBrief(false);
                                opti++;
                                permissionNames = null;
                            } else if ("--brief".equals(args[opti])) {
                                dumpState2.setBrief(true);
                                opti++;
                                permissionNames = null;
                            }
                        }
                    } else if ("protected-broadcasts".equals(cmd)) {
                        dumpState2.setDump(1073741824);
                    } else if (this.mDumpHelperExt.customLogicInDump(cmd, pw, args, opti)) {
                        return;
                    }
                    permissionNames = null;
                }
            }
        }
        java.lang.String packageName2 = dumpState2.getTargetPackageName();
        boolean checkin = dumpState2.isCheckIn();
        if (packageName2 != null && snapshot.getPackageStateInternal(packageName2) == null && !snapshot.isApexPackage(packageName2)) {
            pw.println("Unable to find package: " + packageName2);
            return;
        }
        if (checkin) {
            pw.println("vers,1");
        }
        if (!checkin && dumpState2.isDumping(32768) && packageName2 == null) {
            snapshot.dump(32768, fd, pw, dumpState2);
        }
        java.lang.String str6 = "  ";
        if (!checkin && dumpState2.isDumping(134217728) && packageName2 == null) {
            if (dumpState2.onTitlePrinted()) {
                pw.println();
            }
            com.android.internal.util.IndentingPrintWriter ipw = new com.android.internal.util.IndentingPrintWriter(pw, "  ", 120);
            ipw.println("Known Packages:");
            ipw.increaseIndent();
            int i2 = 0;
            while (i2 <= 19) {
                java.lang.String knownPackage = com.android.server.pm.KnownPackages.knownPackageToString(i2);
                ipw.print(knownPackage);
                ipw.println(":");
                java.lang.String[] pkgNames = this.mKnownPackages.getKnownPackageNames(snapshot, i2, 0);
                ipw.increaseIndent();
                if (com.android.internal.util.ArrayUtils.isEmpty(pkgNames)) {
                    ipw.println("none");
                } else {
                    int length = pkgNames.length;
                    int i3 = 0;
                    while (i3 < length) {
                        ipw.println(pkgNames[i3]);
                        i3++;
                        opti = opti;
                    }
                }
                ipw.decreaseIndent();
                i2++;
                opti = opti;
            }
            ipw.decreaseIndent();
        }
        if (!dumpState2.isDumping(2048) || packageName2 != null) {
            str = "  ";
        } else {
            if (!checkin && this.mRequiredVerifierPackages.length > 0) {
                if (dumpState2.onTitlePrinted()) {
                    pw.println();
                }
                pw.println("Verifiers:");
            }
            java.lang.String[] strArr2 = this.mRequiredVerifierPackages;
            int length2 = strArr2.length;
            int i4 = 0;
            while (i4 < length2) {
                java.lang.String requiredVerifierPackage = strArr2[i4];
                if (!checkin) {
                    pw.print("  Required: ");
                    pw.print(requiredVerifierPackage);
                    pw.print(" (uid=");
                    strArr = strArr2;
                    str5 = str6;
                    pw.print(snapshot.getPackageUid(requiredVerifierPackage, 268435456L, 0));
                    pw.println(")");
                } else {
                    strArr = strArr2;
                    str5 = str6;
                    pw.print("vrfy,");
                    pw.print(requiredVerifierPackage);
                    pw.print(",");
                    pw.println(snapshot.getPackageUid(requiredVerifierPackage, 268435456L, 0));
                }
                i4++;
                str6 = str5;
                strArr2 = strArr;
            }
            str = str6;
        }
        if (dumpState2.isDumping(131072) && packageName2 == null) {
            com.android.server.pm.verify.domain.proxy.DomainVerificationProxy proxy = this.mDomainVerificationManager.getProxy();
            android.content.ComponentName verifierComponent = proxy.getComponentName();
            if (verifierComponent != null) {
                java.lang.String verifierPackageName = verifierComponent.getPackageName();
                if (!checkin) {
                    if (dumpState2.onTitlePrinted()) {
                        pw.println();
                    }
                    pw.println("Domain Verifier:");
                    pw.print("  Using: ");
                    pw.print(verifierPackageName);
                    pw.print(" (uid=");
                    pw.print(snapshot.getPackageUid(verifierPackageName, 268435456L, 0));
                    pw.println(")");
                } else if (verifierPackageName != null) {
                    pw.print("dv,");
                    pw.print(verifierPackageName);
                    pw.print(",");
                    pw.println(snapshot.getPackageUid(verifierPackageName, 268435456L, 0));
                }
            } else {
                pw.println();
                pw.println("No Domain Verifier available!");
            }
        }
        if (!dumpState2.isDumping(1) || packageName2 != null) {
            fileDescriptor = fd;
        } else {
            fileDescriptor = fd;
            snapshot.dump(1, fileDescriptor, pw, dumpState2);
        }
        if (!dumpState2.isDumping(2) || packageName2 != null) {
            str2 = str;
        } else {
            if (dumpState2.onTitlePrinted()) {
                pw.println();
            }
            if (!checkin) {
                pw.println("Features:");
            }
            for (android.content.pm.FeatureInfo feat : this.mAvailableFeatures.values()) {
                if (!checkin) {
                    str4 = str;
                    pw.print(str4);
                    pw.print(feat.name);
                    if (feat.version > 0) {
                        pw.print(" version=");
                        pw.print(feat.version);
                    }
                    pw.println();
                } else {
                    str4 = str;
                    pw.print("feat,");
                    pw.print(feat.name);
                    pw.print(",");
                    pw.println(feat.version);
                }
                str = str4;
            }
            str2 = str;
        }
        com.android.server.pm.resolution.ComponentResolverApi componentResolver = snapshot.getComponentResolver();
        if (!checkin && dumpState2.isDumping(4)) {
            componentResolver.dumpActivityResolvers(pw, dumpState2, packageName2);
        }
        if (!checkin && dumpState2.isDumping(16)) {
            componentResolver.dumpReceiverResolvers(pw, dumpState2, packageName2);
        }
        if (!checkin && dumpState2.isDumping(8)) {
            componentResolver.dumpServiceResolvers(pw, dumpState2, packageName2);
        }
        if (!checkin && dumpState2.isDumping(32)) {
            componentResolver.dumpProviderResolvers(pw, dumpState2, packageName2);
        }
        if (!checkin && dumpState2.isDumping(4096)) {
            snapshot.dump(4096, fileDescriptor, pw, dumpState2);
        }
        if (!checkin && dumpState2.isDumping(8192) && packageName2 == null) {
            snapshot.dump(8192, fileDescriptor, pw, dumpState2);
        }
        if (!checkin && dumpState2.isDumping(262144)) {
            snapshot.dump(262144, fileDescriptor, pw, dumpState2);
        }
        if (!checkin && dumpState2.isDumping(64)) {
            snapshot.dumpPermissions(pw, packageName2, permissionNames, dumpState2);
        }
        if (!checkin && dumpState2.isDumping(1024)) {
            componentResolver.dumpContentProviders(snapshot, pw, dumpState2, packageName2);
        }
        if (!checkin && dumpState2.isDumping(16384)) {
            snapshot.dumpKeySet(pw, packageName2, dumpState2);
        }
        if (!dumpState2.isDumping(128)) {
            packageName = packageName2;
            permissionNames2 = permissionNames;
            i = 67108864;
        } else {
            packageName = packageName2;
            permissionNames2 = permissionNames;
            i = 67108864;
            snapshot.dumpPackages(pw, packageName2, permissionNames, dumpState2, checkin);
        }
        if (!checkin && dumpState2.isDumping(i)) {
            snapshot.dump(i, fileDescriptor, pw, dumpState2);
        }
        if (dumpState2.isDumping(256)) {
            snapshot.dumpSharedUsers(pw, packageName, permissionNames2, dumpState2, checkin);
        }
        if (!checkin && dumpState2.isDumping(4194304) && packageName == null) {
            if (dumpState2.onTitlePrinted()) {
                pw.println();
            }
            pw.println("Package Changes:");
            this.mChangedPackagesTracker.iterateAll(new java.util.function.BiConsumer() { // from class: com.android.server.pm.DumpHelper$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    com.android.server.pm.DumpHelper.lambda$doDump$0(pw, (java.lang.Integer) obj, (android.util.SparseArray) obj2);
                }
            });
        }
        if (!checkin && dumpState2.isDumping(524288) && packageName == null) {
            snapshot.dump(524288, fileDescriptor, pw, dumpState2);
        }
        if (!checkin && dumpState2.isDumping(8388608) && packageName == null) {
            this.mStorageEventHelper.dumpLoadedVolumes(pw, dumpState2);
        }
        if (!checkin && dumpState2.isDumping(16777216) && packageName == null) {
            componentResolver.dumpServicePermissions(pw, dumpState2);
        }
        if (!checkin && dumpState2.isDumping(1048576)) {
            snapshot.dump(1048576, fileDescriptor, pw, dumpState2);
        }
        if (!checkin && dumpState2.isDumping(2097152)) {
            snapshot.dump(2097152, fileDescriptor, pw, dumpState2);
        }
        if (dumpState2.isDumping(512) && packageName == null) {
            if (!checkin) {
                if (dumpState2.onTitlePrinted()) {
                    pw.println();
                }
                snapshot.dump(512, fileDescriptor, pw, dumpState2);
                pw.println();
                pw.println("Package warning messages:");
                com.android.server.pm.PackageManagerServiceUtils.dumpCriticalInfo(pw, null);
            } else {
                com.android.server.pm.PackageManagerServiceUtils.dumpCriticalInfo(pw, "msg,");
            }
        }
        if (!checkin && dumpState2.isDumping(65536) && packageName == null) {
            if (dumpState2.onTitlePrinted()) {
                pw.println();
            }
            this.mInstallerService.dump(new com.android.internal.util.IndentingPrintWriter(pw, str2, 120));
        }
        if (!checkin && dumpState2.isDumping(33554432)) {
            snapshot.dump(33554432, fileDescriptor, pw, dumpState2);
        }
        if (!checkin && dumpState2.isDumping(268435456) && packageName == null) {
            if (dumpState2.onTitlePrinted()) {
                pw.println();
            }
            pw.println("Per UID read timeouts:");
            pw.println("    Default timeouts flag: " + com.android.server.pm.PackageManagerService.getDefaultTimeouts());
            pw.println("    Known digesters list flag: " + com.android.server.pm.PackageManagerService.getKnownDigestersList());
            pw.println("    Timeouts (" + this.mPerUidReadTimeouts.length + "):");
            android.os.incremental.PerUidReadTimeouts[] perUidReadTimeoutsArr = this.mPerUidReadTimeouts;
            int i5 = 0;
            for (int length3 = perUidReadTimeoutsArr.length; i5 < length3; length3 = length3) {
                android.os.incremental.PerUidReadTimeouts item = perUidReadTimeoutsArr[i5];
                pw.print("        (");
                pw.print("uid=" + item.uid + ", ");
                pw.print("minTimeUs=" + item.minTimeUs + ", ");
                pw.print("minPendingTimeUs=" + item.minPendingTimeUs + ", ");
                pw.print("maxPendingTimeUs=" + item.maxPendingTimeUs);
                pw.println(")");
                i5++;
                perUidReadTimeoutsArr = perUidReadTimeoutsArr;
            }
        }
        if (checkin || !dumpState2.isDumping(536870912) || packageName != null) {
            str3 = str2;
            dumpState = dumpState2;
            printWriter = pw;
        } else {
            if (dumpState2.onTitlePrinted()) {
                pw.println();
            }
            pw.println("Snapshot statistics:");
            str3 = str2;
            dumpState = dumpState2;
            printWriter = pw;
            this.mSnapshotStatistics.dump(pw, "   ", android.os.SystemClock.currentTimeMicro(), snapshot.getUsed(), dumpState2.isBrief());
        }
        if (!checkin && dumpState.isDumping(1073741824) && packageName == null) {
            if (dumpState.onTitlePrinted()) {
                pw.println();
            }
            printWriter.println("Protected broadcast actions:");
            for (int i6 = 0; i6 < this.mProtectedBroadcasts.size(); i6++) {
                printWriter.print(str3);
                printWriter.println(this.mProtectedBroadcasts.valueAt(i6));
            }
        }
    }

    static /* synthetic */ void lambda$doDump$0(java.io.PrintWriter pw, java.lang.Integer sequenceNumber, android.util.SparseArray values) {
        pw.print("  Sequence number=");
        pw.println(sequenceNumber);
        int numChangedPackages = values.size();
        for (int i = 0; i < numChangedPackages; i++) {
            android.util.SparseArray<java.lang.String> changes = (android.util.SparseArray) values.valueAt(i);
            pw.print("  User ");
            pw.print(values.keyAt(i));
            pw.println(":");
            int numChanges = changes.size();
            if (numChanges == 0) {
                pw.print("    ");
                pw.println("No packages changed");
            } else {
                for (int j = 0; j < numChanges; j++) {
                    java.lang.String pkgName = changes.valueAt(j);
                    int userSequenceNumber = changes.keyAt(j);
                    pw.print("    ");
                    pw.print("seq=");
                    pw.print(userSequenceNumber);
                    pw.print(", package=");
                    pw.println(pkgName);
                }
            }
        }
    }

    private void printHelp(java.io.PrintWriter pw) {
        pw.println("Package manager dump options:");
        pw.println("  [-h] [-f] [--checkin] [--all-components] [cmd] ...");
        pw.println("    --checkin: dump for a checkin");
        pw.println("    -f: print details of intent filters");
        pw.println("    -h: print this help");
        pw.println("    --proto: dump data to proto");
        pw.println("    --all-components: include all component names in package dump");
        pw.println("    --include-apex: includes the apex packages in package dump");
        pw.println("  cmd may be one of:");
        pw.println("    apex: list active APEXes and APEX session state");
        pw.println("    l[ibraries]: list known shared libraries");
        pw.println("    f[eatures]: list device features");
        pw.println("    k[eysets]: print known keysets");
        pw.println("    r[esolvers] [activity|service|receiver|content]: dump intent resolvers");
        pw.println("    perm[issions]: dump permissions");
        pw.println("    permission [name ...]: dump declaration and use of given permission");
        pw.println("    pref[erred]: print preferred package settings");
        pw.println("    preferred-xml [--full]: print preferred package settings as xml");
        pw.println("    prov[iders]: dump content providers");
        pw.println("    p[ackages]: dump installed packages");
        pw.println("    q[ueries]: dump app queryability calculations");
        pw.println("    s[hared-users] [noperm]: dump shared user IDs");
        pw.println("    m[essages]: print collected runtime messages");
        pw.println("    v[erifiers]: print package verifier info");
        pw.println("    d[omain-preferred-apps]: print domains preferred apps");
        pw.println("    i[ntent-filter-verifiers]|ifv: print intent filter verifier info");
        pw.println("    t[imeouts]: print read timeouts for known digesters");
        pw.println("    version: print database version info");
        pw.println("    write: write current settings now");
        pw.println("    installs: details about install sessions");
        pw.println("    check-permission <permission> <package> [<user>]: does pkg hold perm?");
        pw.println("    dexopt: dump dexopt state");
        pw.println("    compiler-stats: dump compiler statistics");
        pw.println("    service-permissions: dump permissions required by services");
        pw.println("    snapshot [--full|--brief]: dump snapshot statistics");
        pw.println("    protected-broadcasts: print list of protected broadcast actions");
        pw.println("    known-packages: dump known packages");
        pw.println("    changes: dump the packages that have been changed");
        pw.println("    frozen: dump the frozen packages");
        pw.println("    volumes: dump the loaded volumes");
        pw.println("    <package.name>: info about given package");
    }

    private void dumpProto(com.android.server.pm.Computer snapshot, java.io.FileDescriptor fd) {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
        for (java.lang.String requiredVerifierPackage : this.mRequiredVerifierPackages) {
            long requiredVerifierPackageToken = proto.start(1146756268033L);
            proto.write(1138166333441L, requiredVerifierPackage);
            proto.write(1120986464258L, snapshot.getPackageUid(requiredVerifierPackage, 268435456L, 0));
            proto.end(requiredVerifierPackageToken);
        }
        com.android.server.pm.verify.domain.proxy.DomainVerificationProxy proxy = this.mDomainVerificationManager.getProxy();
        android.content.ComponentName verifierComponent = proxy.getComponentName();
        if (verifierComponent != null) {
            java.lang.String verifierPackageName = verifierComponent.getPackageName();
            long verifierPackageToken = proto.start(1146756268034L);
            proto.write(1138166333441L, verifierPackageName);
            proto.write(1120986464258L, snapshot.getPackageUid(verifierPackageName, 268435456L, 0));
            proto.end(verifierPackageToken);
        }
        snapshot.dumpSharedLibrariesProto(proto);
        dumpAvailableFeaturesProto(proto);
        snapshot.dumpPackagesProto(proto);
        snapshot.dumpSharedUsersProto(proto);
        com.android.server.pm.PackageManagerServiceUtils.dumpCriticalInfo(proto);
        proto.flush();
    }

    private void dumpAvailableFeaturesProto(android.util.proto.ProtoOutputStream proto) {
        int count = this.mAvailableFeatures.size();
        for (int i = 0; i < count; i++) {
            this.mAvailableFeatures.valueAt(i).dumpDebug(proto, 2246267895812L);
        }
    }
}
