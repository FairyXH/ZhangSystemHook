package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public class LegacyPermissionSettings {
    private final android.util.ArrayMap<java.lang.String, com.android.server.pm.permission.LegacyPermission> mPermissions = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, com.android.server.pm.permission.LegacyPermission> mPermissionTrees = new android.util.ArrayMap<>();
    private final com.android.server.pm.PackageManagerTracedLock mLock = new com.android.server.pm.PackageManagerTracedLock();

    public java.util.List<com.android.server.pm.permission.LegacyPermission> getPermissions() {
        java.util.ArrayList arrayList;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                arrayList = new java.util.ArrayList(this.mPermissions.values());
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return arrayList;
    }

    public java.util.List<com.android.server.pm.permission.LegacyPermission> getPermissionTrees() {
        java.util.ArrayList arrayList;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                arrayList = new java.util.ArrayList(this.mPermissionTrees.values());
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        return arrayList;
    }

    public void replacePermissions(java.util.List<com.android.server.pm.permission.LegacyPermission> permissions) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mPermissions.clear();
                int permissionsSize = permissions.size();
                for (int i = 0; i < permissionsSize; i++) {
                    com.android.server.pm.permission.LegacyPermission permission = permissions.get(i);
                    this.mPermissions.put(permission.getPermissionInfo().name, permission);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    public void replacePermissionTrees(java.util.List<com.android.server.pm.permission.LegacyPermission> permissionTrees) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mPermissionTrees.clear();
                int permissionsSize = permissionTrees.size();
                for (int i = 0; i < permissionsSize; i++) {
                    com.android.server.pm.permission.LegacyPermission permissionTree = permissionTrees.get(i);
                    this.mPermissionTrees.put(permissionTree.getPermissionInfo().name, permissionTree);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    public void readPermissions(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                readPermissions(this.mPermissions, parser);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    public void readPermissionTrees(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                readPermissions(this.mPermissionTrees, parser);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    public static void readPermissions(android.util.ArrayMap<java.lang.String, com.android.server.pm.permission.LegacyPermission> out, com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        if (!com.android.server.pm.permission.LegacyPermission.read(out, parser)) {
                            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Unknown element reading permissions: " + parser.getName() + " at " + parser.getPositionDescription());
                        }
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void writePermissions(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                for (com.android.server.pm.permission.LegacyPermission bp : this.mPermissions.values()) {
                    bp.write(serializer);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    public void writePermissionTrees(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                for (com.android.server.pm.permission.LegacyPermission bp : this.mPermissionTrees.values()) {
                    bp.write(serializer);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    public static void dumpPermissions(java.io.PrintWriter pw, java.lang.String packageName, android.util.ArraySet<java.lang.String> permissionNames, java.util.List<com.android.server.pm.permission.LegacyPermission> permissions, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> appOpPermissionPackages, boolean externalStorageEnforced, com.android.server.pm.DumpState dumpState) {
        int permissionsSize = permissions.size();
        boolean printedSomething = false;
        for (int i = 0; i < permissionsSize; i++) {
            com.android.server.pm.permission.LegacyPermission permission = permissions.get(i);
            printedSomething = permission.dump(pw, packageName, permissionNames, externalStorageEnforced, printedSomething, dumpState);
        }
        if (packageName == null && permissionNames == null) {
            boolean firstEntry = true;
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : appOpPermissionPackages.entrySet()) {
                if (firstEntry) {
                    firstEntry = false;
                    if (dumpState.onTitlePrinted()) {
                        pw.println();
                    }
                    pw.println("AppOp Permissions:");
                }
                pw.print("  AppOp Permission ");
                pw.print(entry.getKey());
                pw.println(":");
                for (java.lang.String appOpPackageName : entry.getValue()) {
                    pw.print("    ");
                    pw.println(appOpPackageName);
                }
            }
        }
    }
}
