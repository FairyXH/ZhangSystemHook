package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
public class PackageStateUtils {
    public static boolean isMatch(com.android.server.pm.pkg.PackageState packageState, long flags) {
        if ((1048576 & flags) != 0) {
            return packageState.isSystem();
        }
        return true;
    }

    public static int[] queryInstalledUsers(com.android.server.pm.pkg.PackageStateInternal pkgState, int[] users, boolean installed) {
        int num = 0;
        for (int i : users) {
            if (pkgState.getUserStateOrDefault(i).isInstalled() == installed) {
                num++;
            }
        }
        int[] res = new int[num];
        int num2 = 0;
        for (int user : users) {
            if (pkgState.getUserStateOrDefault(user).isInstalled() == installed) {
                res[num2] = user;
                num2++;
            }
        }
        return res;
    }

    public static boolean isEnabledAndMatches(com.android.server.pm.pkg.PackageStateInternal packageState, android.content.pm.ComponentInfo componentInfo, long flags, int userId) {
        if (packageState == null) {
            return false;
        }
        com.android.server.pm.pkg.PackageUserStateInternal userState = packageState.getUserStateOrDefault(userId);
        return com.android.server.pm.pkg.PackageUserStateUtils.isMatch(userState, componentInfo, flags);
    }

    public static boolean isEnabledAndMatches(com.android.server.pm.pkg.PackageStateInternal packageState, com.android.internal.pm.pkg.component.ParsedMainComponent component, long flags, int userId) {
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg;
        if (packageState == null || (pkg = packageState.getPkg()) == null) {
            return false;
        }
        com.android.server.pm.pkg.PackageUserStateInternal userState = packageState.getUserStateOrDefault(userId);
        return com.android.server.pm.pkg.PackageUserStateUtils.isMatch(userState, packageState.isSystem(), pkg.isEnabled(), component, flags);
    }

    public static long getEarliestFirstInstallTime(android.util.SparseArray<? extends com.android.server.pm.pkg.PackageUserStateInternal> userStatesInternal) {
        if (userStatesInternal == null || userStatesInternal.size() == 0) {
            return 0L;
        }
        long earliestFirstInstallTime = Long.MAX_VALUE;
        for (int i = 0; i < userStatesInternal.size(); i++) {
            long firstInstallTime = userStatesInternal.valueAt(i).getFirstInstallTimeMillis();
            if (firstInstallTime != 0 && firstInstallTime < earliestFirstInstallTime) {
                earliestFirstInstallTime = firstInstallTime;
            }
        }
        if (earliestFirstInstallTime == Long.MAX_VALUE) {
            return 0L;
        }
        return earliestFirstInstallTime;
    }
}
