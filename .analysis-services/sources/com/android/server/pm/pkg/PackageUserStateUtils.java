package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
public class PackageUserStateUtils {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "PackageUserStateUtils";
    private static com.android.server.pm.pkg.IPackageUserStateUtilExt mPackageUserStateUtilExt = (com.android.server.pm.pkg.IPackageUserStateUtilExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.pkg.IPackageUserStateUtilExt.class).base((java.lang.Object) null).create();

    public static boolean isMatch(com.android.server.pm.pkg.PackageUserState state, android.content.pm.ComponentInfo componentInfo, long flags) {
        return isMatch(state, componentInfo.applicationInfo.isSystemApp(), componentInfo.applicationInfo.enabled, componentInfo.enabled, componentInfo.directBootAware, componentInfo.name, flags);
    }

    public static boolean isMatch(com.android.server.pm.pkg.PackageUserState state, boolean isSystem, boolean isPackageEnabled, com.android.internal.pm.pkg.component.ParsedMainComponent component, long flags) {
        return isMatch(state, isSystem, isPackageEnabled, component.isEnabled(), component.isDirectBootAware(), component.getName(), flags);
    }

    public static boolean isMatch(com.android.server.pm.pkg.PackageUserState state, boolean isSystem, boolean isPackageEnabled, boolean isComponentEnabled, boolean isComponentDirectBootAware, java.lang.String componentName, long flags) {
        boolean z = true;
        boolean matchUninstalled = (4202496 & flags) != 0;
        if (!isAvailable(state, flags) && (!isSystem || !matchUninstalled)) {
            return reportIfDebug(false, flags);
        }
        if (!isEnabled(state, isPackageEnabled, isComponentEnabled, componentName, flags)) {
            return reportIfDebug(false, flags);
        }
        if ((1048576 & flags) != 0 && !isSystem) {
            return reportIfDebug(false, flags);
        }
        boolean matchesUnaware = ((262144 & flags) == 0 || isComponentDirectBootAware) ? false : true;
        boolean matchesAware = (524288 & flags) != 0 && isComponentDirectBootAware;
        if (!matchesUnaware && !matchesAware) {
            z = false;
        }
        return reportIfDebug(z, flags);
    }

    public static boolean isAvailable(com.android.server.pm.pkg.PackageUserState state, long flags) {
        boolean matchAnyUser = (4194304 & flags) != 0;
        boolean matchUninstalled = (8192 & flags) != 0;
        boolean matchArchived = (4294967296L & flags) != 0;
        boolean matchDataExists = matchUninstalled || matchArchived;
        boolean isOplusHidePackage = mPackageUserStateUtilExt.isOhidePackage(state);
        boolean isOplusHideVisible = mPackageUserStateUtilExt.isOhideVisible(state);
        if (matchAnyUser && !isOplusHidePackage) {
            return true;
        }
        if (!state.isInstalled()) {
            return matchDataExists && state.dataExists();
        }
        if (state.isHidden()) {
            return isOplusHidePackage ? isOplusHideVisible || (4096 & flags) != 0 : matchDataExists;
        }
        return true;
    }

    public static boolean reportIfDebug(boolean result, long flags) {
        return result;
    }

    public static boolean isEnabled(com.android.server.pm.pkg.PackageUserState state, android.content.pm.ComponentInfo componentInfo, long flags) {
        return isEnabled(state, componentInfo.applicationInfo.enabled, componentInfo.enabled, componentInfo.name, flags);
    }

    public static boolean isEnabled(com.android.server.pm.pkg.PackageUserState state, boolean isPackageEnabled, com.android.internal.pm.pkg.component.ParsedMainComponent parsedComponent, long flags) {
        return isEnabled(state, isPackageEnabled, parsedComponent.isEnabled(), parsedComponent.getName(), flags);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003b A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0042 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0043  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isEnabled(com.android.server.pm.pkg.PackageUserState r7, boolean r8, boolean r9, java.lang.String r10, long r11) {
        /*
            r0 = 512(0x200, double:2.53E-321)
            long r0 = r0 & r11
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            r1 = 1
            if (r0 == 0) goto Lb
            return r1
        Lb:
            r4 = 8589934592(0x200000000, double:4.243991582E-314)
            long r4 = r4 & r11
            int r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            r4 = 0
            if (r0 != 0) goto L1d
            boolean r0 = r7.isQuarantined()
            if (r0 == 0) goto L1d
            return r4
        L1d:
            int r0 = r7.getEnabledState()
            switch(r0) {
                case 0: goto L39;
                case 1: goto L24;
                case 2: goto L2e;
                case 3: goto L2e;
                case 4: goto L25;
                default: goto L24;
            }
        L24:
            goto L3c
        L25:
            r5 = 32768(0x8000, double:1.61895E-319)
            long r5 = r5 & r11
            int r0 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
            if (r0 != 0) goto L39
            return r4
        L2e:
            int r0 = r7.getEnabledState()
            boolean r0 = r7.ignorePackageDisabledInIsEnabled(r0, r11)
            if (r0 != 0) goto L3c
            return r4
        L39:
            if (r8 != 0) goto L3c
            return r4
        L3c:
            boolean r0 = r7.isComponentEnabled(r10)
            if (r0 == 0) goto L43
            return r1
        L43:
            boolean r0 = r7.isComponentDisabled(r10)
            if (r0 == 0) goto L4a
            return r4
        L4a:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.pkg.PackageUserStateUtils.isEnabled(com.android.server.pm.pkg.PackageUserState, boolean, boolean, java.lang.String, long):boolean");
    }

    public static boolean isPackageEnabled(com.android.server.pm.pkg.PackageUserState state, com.android.server.pm.pkg.AndroidPackage pkg) {
        switch (state.getEnabledState()) {
            case 1:
                return true;
            case 2:
            case 3:
            case 4:
                return false;
            default:
                return pkg.isEnabled();
        }
    }
}
