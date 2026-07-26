package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class PackageManagerNative extends android.content.pm.IPackageManagerNative.Stub {
    private final com.android.server.pm.IPackageManagerNativeExt mPackageManagerNativeExt = (com.android.server.pm.IPackageManagerNativeExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerNativeExt.class).create();
    private final com.android.server.pm.PackageManagerService mPm;

    PackageManagerNative(com.android.server.pm.PackageManagerService pm) {
        this.mPm = pm;
        this.mPackageManagerNativeExt.init(this.mPm, this.mPm.mContext);
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        if (super.onTransact(code, data, reply, flags)) {
            return true;
        }
        return this.mPackageManagerNativeExt.onTransact(code, data, reply, flags);
    }

    public java.lang.String[] getNamesForUids(int[] uids) throws android.os.RemoteException {
        java.lang.String[] names = null;
        java.lang.String[] results = null;
        if (uids == null) {
            return null;
        }
        try {
            if (uids.length == 0) {
                return null;
            }
            names = this.mPm.snapshotComputer().getNamesForUids(uids);
            results = names != null ? names : new java.lang.String[uids.length];
            for (int i = results.length - 1; i >= 0; i--) {
                if (results[i] == null) {
                    results[i] = "";
                }
            }
            return results;
        } catch (java.lang.Throwable t) {
            android.util.Slog.e("PackageManager", "uids: " + java.util.Arrays.toString(uids));
            android.util.Slog.e("PackageManager", "names: " + java.util.Arrays.toString(names));
            android.util.Slog.e("PackageManager", "results: " + java.util.Arrays.toString(results));
            android.util.Slog.e("PackageManager", "throwing exception", t);
            throw t;
        }
    }

    public int getPackageUid(java.lang.String packageName, long flags, int userId) throws android.os.RemoteException {
        return this.mPm.snapshotComputer().getPackageUid(packageName, flags, userId);
    }

    public java.lang.String getInstallerForPackage(java.lang.String packageName) throws android.os.RemoteException {
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        int callingUser = android.os.UserHandle.getUserId(android.os.Binder.getCallingUid());
        java.lang.String installerName = snapshot.getInstallerPackageName(packageName, callingUser);
        if (!android.text.TextUtils.isEmpty(installerName)) {
            return installerName;
        }
        android.content.pm.ApplicationInfo appInfo = snapshot.getApplicationInfo(packageName, 0L, callingUser);
        if (appInfo != null && (appInfo.flags & 1) != 0) {
            return "preload";
        }
        return "";
    }

    public long getVersionCodeForPackage(java.lang.String packageName) throws android.os.RemoteException {
        try {
            int callingUser = android.os.UserHandle.getUserId(android.os.Binder.getCallingUid());
            android.content.pm.PackageInfo pInfo = this.mPm.snapshotComputer().getPackageInfo(packageName, 0L, callingUser);
            if (pInfo != null) {
                return pInfo.getLongVersionCode();
            }
        } catch (java.lang.Exception e) {
        }
        return 0L;
    }

    public int getTargetSdkVersionForPackage(java.lang.String packageName) throws android.os.RemoteException {
        int targetSdk = this.mPm.snapshotComputer().getTargetSdkVersion(packageName);
        if (targetSdk != -1) {
            return targetSdk;
        }
        throw new android.os.RemoteException("Couldn't get targetSdkVersion for package " + packageName);
    }

    public boolean isPackageDebuggable(java.lang.String packageName) throws android.os.RemoteException {
        int callingUser = android.os.UserHandle.getCallingUserId();
        android.content.pm.ApplicationInfo appInfo = this.mPm.snapshotComputer().getApplicationInfo(packageName, 0L, callingUser);
        if (appInfo != null) {
            return (appInfo.flags & 2) != 0;
        }
        throw new android.os.RemoteException("Couldn't get debug flag for package " + packageName);
    }

    public boolean[] isAudioPlaybackCaptureAllowed(java.lang.String[] packageNames) throws android.os.RemoteException {
        int callingUser = android.os.UserHandle.getUserId(android.os.Binder.getCallingUid());
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        boolean[] results = new boolean[packageNames.length];
        for (int i = results.length - 1; i >= 0; i--) {
            android.content.pm.ApplicationInfo appInfo = snapshot.getApplicationInfo(packageNames[i], 0L, callingUser);
            results[i] = appInfo != null && appInfo.isAudioPlaybackCaptureAllowed();
        }
        return results;
    }

    public int getLocationFlags(java.lang.String str) throws android.os.RemoteException {
        android.content.pm.ApplicationInfo applicationInfo = this.mPm.snapshotComputer().getApplicationInfo(str, 0L, android.os.UserHandle.getUserId(android.os.Binder.getCallingUid()));
        if (applicationInfo == null) {
            throw new android.os.RemoteException("Couldn't get ApplicationInfo for package " + str);
        }
        return (applicationInfo.isSystemApp() ? 1 : 0) | (applicationInfo.isVendor() ? 2 : 0) | (applicationInfo.isProduct() ? 4 : 0);
    }

    public java.lang.String getModuleMetadataPackageName() throws android.os.RemoteException {
        return this.mPm.getModuleMetadataPackageName();
    }

    public boolean hasSha256SigningCertificate(java.lang.String packageName, byte[] certificate) throws android.os.RemoteException {
        return this.mPm.snapshotComputer().hasSigningCertificate(packageName, certificate, 1);
    }

    public boolean hasSystemFeature(java.lang.String featureName, int version) {
        return this.mPm.hasSystemFeature(featureName, version);
    }

    public void registerStagedApexObserver(android.content.pm.IStagedApexObserver observer) {
        this.mPm.mInstallerService.getStagingManager().registerStagedApexObserver(observer);
    }

    public void unregisterStagedApexObserver(android.content.pm.IStagedApexObserver observer) {
        this.mPm.mInstallerService.getStagingManager().unregisterStagedApexObserver(observer);
    }

    public java.lang.String[] getStagedApexModuleNames() {
        return (java.lang.String[]) this.mPm.mInstallerService.getStagingManager().getStagedApexModuleNames().toArray(new java.lang.String[0]);
    }

    public android.content.pm.StagedApexInfo getStagedApexInfo(java.lang.String moduleName) {
        return this.mPm.mInstallerService.getStagingManager().getStagedApexInfo(moduleName);
    }
}
