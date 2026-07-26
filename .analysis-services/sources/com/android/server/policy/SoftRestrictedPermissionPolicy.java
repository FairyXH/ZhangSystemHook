package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public abstract class SoftRestrictedPermissionPolicy {
    private static final int FLAGS_PERMISSION_RESTRICTION_ANY_EXEMPT = 14336;
    private static final com.android.server.policy.SoftRestrictedPermissionPolicy DUMMY_POLICY = new com.android.server.policy.SoftRestrictedPermissionPolicy() { // from class: com.android.server.policy.SoftRestrictedPermissionPolicy.1
        @Override // com.android.server.policy.SoftRestrictedPermissionPolicy
        public boolean mayGrantPermission() {
            return true;
        }
    };
    private static final java.util.HashSet<java.lang.String> sForcedScopedStorageAppWhitelist = new java.util.HashSet<>(java.util.Arrays.asList(getForcedScopedStorageAppWhitelist()));

    public abstract boolean mayGrantPermission();

    private static int getMinimumTargetSDK(android.content.Context context, android.content.pm.ApplicationInfo appInfo, android.os.UserHandle user) {
        android.content.pm.PackageManager pm = context.getPackageManager();
        int minimumTargetSDK = appInfo.targetSdkVersion;
        java.lang.String[] uidPkgs = pm.getPackagesForUid(appInfo.uid);
        if (uidPkgs != null) {
            for (java.lang.String uidPkg : uidPkgs) {
                if (!uidPkg.equals(appInfo.packageName)) {
                    try {
                        android.content.pm.ApplicationInfo uidPkgInfo = pm.getApplicationInfoAsUser(uidPkg, 0, user);
                        minimumTargetSDK = java.lang.Integer.min(minimumTargetSDK, uidPkgInfo.targetSdkVersion);
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    }
                }
            }
        }
        return minimumTargetSDK;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.android.server.policy.SoftRestrictedPermissionPolicy forPermission(android.content.Context r24, android.content.pm.ApplicationInfo r25, com.android.server.pm.pkg.AndroidPackage r26, android.os.UserHandle r27, java.lang.String r28) {
        /*
            r0 = r24
            r1 = r25
            r2 = r27
            r3 = r28
            int r4 = r28.hashCode()
            r5 = 1
            r6 = 0
            switch(r4) {
                case -406040016: goto L1c;
                case 1365911975: goto L12;
                default: goto L11;
            }
        L11:
            goto L26
        L12:
            java.lang.String r4 = "android.permission.WRITE_EXTERNAL_STORAGE"
            boolean r4 = r3.equals(r4)
            if (r4 == 0) goto L11
            r4 = r5
            goto L27
        L1c:
            java.lang.String r4 = "android.permission.READ_EXTERNAL_STORAGE"
            boolean r4 = r3.equals(r4)
            if (r4 == 0) goto L11
            r4 = r6
            goto L27
        L26:
            r4 = -1
        L27:
            switch(r4) {
                case 0: goto L4c;
                case 1: goto L2d;
                default: goto L2a;
            }
        L2a:
            com.android.server.policy.SoftRestrictedPermissionPolicy r4 = com.android.server.policy.SoftRestrictedPermissionPolicy.DUMMY_POLICY
            return r4
        L2d:
            if (r1 == 0) goto L44
            android.content.pm.PackageManager r4 = r24.getPackageManager()
            java.lang.String r7 = r1.packageName
            int r4 = r4.getPermissionFlags(r3, r7, r2)
            r7 = r4 & 14336(0x3800, float:2.0089E-41)
            if (r7 == 0) goto L3e
            goto L3f
        L3e:
            r5 = r6
        L3f:
            int r4 = getMinimumTargetSDK(r0, r1, r2)
            goto L46
        L44:
            r5 = 0
            r4 = 0
        L46:
            com.android.server.policy.SoftRestrictedPermissionPolicy$3 r6 = new com.android.server.policy.SoftRestrictedPermissionPolicy$3
            r6.<init>()
            return r6
        L4c:
            if (r1 == 0) goto L8f
            android.content.pm.PackageManager r4 = r24.getPackageManager()
            java.lang.Class<android.os.storage.StorageManagerInternal> r7 = android.os.storage.StorageManagerInternal.class
            java.lang.Object r7 = com.android.server.LocalServices.getService(r7)
            android.os.storage.StorageManagerInternal r7 = (android.os.storage.StorageManagerInternal) r7
            java.lang.String r8 = r1.packageName
            int r8 = r4.getPermissionFlags(r3, r8, r2)
            r9 = r8 & 14336(0x3800, float:2.0089E-41)
            if (r9 == 0) goto L66
            r9 = r5
            goto L67
        L66:
            r9 = r6
        L67:
            int r10 = r1.uid
            boolean r10 = r7.hasLegacyExternalStorage(r10)
            int r11 = r1.uid
            boolean r11 = hasUidRequestedLegacyExternalStorage(r11, r0)
            int r12 = r1.uid
            boolean r12 = hasWriteMediaStorageGrantedForUid(r12, r0)
            boolean r13 = r26.hasPreserveLegacyExternalStorage()
            int r14 = getMinimumTargetSDK(r0, r1, r2)
            if (r9 != 0) goto L85
            goto L86
        L85:
            r5 = r6
        L86:
            java.util.HashSet<java.lang.String> r6 = com.android.server.policy.SoftRestrictedPermissionPolicy.sForcedScopedStorageAppWhitelist
            java.lang.String r15 = r1.packageName
            boolean r4 = r6.contains(r15)
            goto L97
        L8f:
            r9 = 0
            r5 = 0
            r14 = 0
            r10 = 0
            r11 = 0
            r13 = 0
            r12 = 0
            r4 = 0
        L97:
            com.android.server.policy.SoftRestrictedPermissionPolicy$2 r6 = new com.android.server.policy.SoftRestrictedPermissionPolicy$2
            r15 = r6
            r16 = r9
            r17 = r14
            r18 = r5
            r19 = r4
            r20 = r12
            r21 = r10
            r22 = r11
            r23 = r13
            r15.<init>()
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.policy.SoftRestrictedPermissionPolicy.forPermission(android.content.Context, android.content.pm.ApplicationInfo, com.android.server.pm.pkg.AndroidPackage, android.os.UserHandle, java.lang.String):com.android.server.policy.SoftRestrictedPermissionPolicy");
    }

    private static boolean hasUidRequestedLegacyExternalStorage(int uid, android.content.Context context) {
        android.content.pm.ApplicationInfo applicationInfo;
        android.content.pm.PackageManager packageManager = context.getPackageManager();
        java.lang.String[] packageNames = packageManager.getPackagesForUid(uid);
        if (packageNames == null) {
            return false;
        }
        android.os.UserHandle user = android.os.UserHandle.getUserHandleForUid(uid);
        for (java.lang.String packageName : packageNames) {
            try {
                applicationInfo = packageManager.getApplicationInfoAsUser(packageName, 0, user);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
            if (applicationInfo.hasRequestedLegacyExternalStorage()) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasWriteMediaStorageGrantedForUid(int uid, android.content.Context context) {
        android.content.pm.PackageManager packageManager = context.getPackageManager();
        java.lang.String[] packageNames = packageManager.getPackagesForUid(uid);
        if (packageNames == null) {
            return false;
        }
        for (java.lang.String packageName : packageNames) {
            if (packageManager.checkPermission("android.permission.WRITE_MEDIA_STORAGE", packageName) == 0) {
                return true;
            }
        }
        return false;
    }

    private static java.lang.String[] getForcedScopedStorageAppWhitelist() {
        java.lang.String rawList = android.provider.DeviceConfig.getString("storage_native_boot", "forced_scoped_storage_whitelist", "");
        if (rawList == null || rawList.equals("")) {
            return new java.lang.String[0];
        }
        return rawList.split(",");
    }

    public int getExtraAppOpCode() {
        return -1;
    }

    public boolean mayAllowExtraAppOp() {
        return false;
    }

    public boolean mayDenyExtraAppOpIfGranted() {
        return false;
    }
}
