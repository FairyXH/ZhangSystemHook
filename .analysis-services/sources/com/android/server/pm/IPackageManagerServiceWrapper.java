package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageManagerServiceWrapper {
    default com.android.server.pm.PackageDexOptimizer getPackageDexOptimizer() {
        return null;
    }

    default com.android.server.pm.dex.DexManager getDexManager() {
        return null;
    }

    default com.android.server.pm.permission.PermissionManagerServiceInternal getPermissionManager() {
        return null;
    }

    default com.android.server.pm.ApexManager getApexManager() {
        return null;
    }

    default com.android.server.pm.AppDataHelper getAppDataHelper() {
        return null;
    }

    default com.android.server.pm.DexOptHelper getDexOptHelper() {
        return null;
    }

    default com.android.server.pm.InitAppsHelper getInitAppsHelper() {
        return null;
    }

    default com.android.server.pm.ResolveIntentHelper getResolveIntentHelper() {
        return null;
    }

    default com.android.server.pm.RemovePackageHelper getRemovePackageHelper() {
        return null;
    }

    default com.android.server.pm.InstallPackageHelper getInstallPackageHelper() {
        return null;
    }

    default com.android.server.pm.BroadcastHelper getBroadcastHelper() {
        return null;
    }

    default com.android.server.pm.PackageMonitorCallbackHelper getPackageMonitorCallbackHelper() {
        return null;
    }

    default com.android.server.pm.SharedLibrariesImpl getSharedLibraries() {
        return null;
    }

    default com.android.server.pm.ComputerLocked getLiveComputer() {
        return null;
    }

    default com.android.server.pm.AppsFilterImpl getAppsFilter() {
        return null;
    }

    default com.android.server.pm.resolution.ComponentResolver getComponentResolver() {
        return null;
    }

    default com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.pkg.AndroidPackage> getPackages() {
        return null;
    }

    default java.io.File getCacheDir() {
        return null;
    }
}
