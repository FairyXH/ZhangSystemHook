package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageList implements android.content.pm.PackageManagerInternal.PackageListObserver, java.lang.AutoCloseable {
    private final java.util.List<java.lang.String> mPackageNames;
    private final android.content.pm.PackageManagerInternal.PackageListObserver mWrappedObserver;

    public PackageList(java.util.List<java.lang.String> packageNames, android.content.pm.PackageManagerInternal.PackageListObserver observer) {
        this.mPackageNames = packageNames;
        this.mWrappedObserver = observer;
    }

    @Override // android.content.pm.PackageManagerInternal.PackageListObserver
    public void onPackageAdded(java.lang.String packageName, int uid) {
        if (this.mWrappedObserver != null) {
            this.mWrappedObserver.onPackageAdded(packageName, uid);
        }
    }

    @Override // android.content.pm.PackageManagerInternal.PackageListObserver
    public void onPackageChanged(java.lang.String packageName, int uid) {
        if (this.mWrappedObserver != null) {
            this.mWrappedObserver.onPackageChanged(packageName, uid);
        }
    }

    @Override // android.content.pm.PackageManagerInternal.PackageListObserver
    public void onPackageRemoved(java.lang.String packageName, int uid) {
        if (this.mWrappedObserver != null) {
            this.mWrappedObserver.onPackageRemoved(packageName, uid);
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() throws java.lang.Exception {
        ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).removePackageListObserver(this);
    }

    public java.util.List<java.lang.String> getPackageNames() {
        return this.mPackageNames;
    }
}
