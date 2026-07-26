package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class PackageObserverHelper {
    private final java.lang.Object mLock = new java.lang.Object();
    private android.util.ArraySet<android.content.pm.PackageManagerInternal.PackageListObserver> mActiveSnapshot = new android.util.ArraySet<>();

    PackageObserverHelper() {
    }

    public void addObserver(android.content.pm.PackageManagerInternal.PackageListObserver observer) {
        synchronized (this.mLock) {
            android.util.ArraySet<android.content.pm.PackageManagerInternal.PackageListObserver> set = new android.util.ArraySet<>(this.mActiveSnapshot);
            set.add(observer);
            this.mActiveSnapshot = set;
        }
    }

    public void removeObserver(android.content.pm.PackageManagerInternal.PackageListObserver observer) {
        synchronized (this.mLock) {
            android.util.ArraySet<android.content.pm.PackageManagerInternal.PackageListObserver> set = new android.util.ArraySet<>(this.mActiveSnapshot);
            set.remove(observer);
            this.mActiveSnapshot = set;
        }
    }

    public void notifyAdded(java.lang.String packageName, int uid) {
        android.util.ArraySet<android.content.pm.PackageManagerInternal.PackageListObserver> observers;
        synchronized (this.mLock) {
            observers = this.mActiveSnapshot;
        }
        int size = observers.size();
        for (int index = 0; index < size; index++) {
            observers.valueAt(index).onPackageAdded(packageName, uid);
        }
    }

    public void notifyChanged(java.lang.String packageName, int uid) {
        android.util.ArraySet<android.content.pm.PackageManagerInternal.PackageListObserver> observers;
        synchronized (this.mLock) {
            observers = this.mActiveSnapshot;
        }
        int size = observers.size();
        for (int index = 0; index < size; index++) {
            observers.valueAt(index).onPackageChanged(packageName, uid);
        }
    }

    public void notifyRemoved(java.lang.String packageName, int uid) {
        android.util.ArraySet<android.content.pm.PackageManagerInternal.PackageListObserver> observers;
        synchronized (this.mLock) {
            observers = this.mActiveSnapshot;
        }
        int size = observers.size();
        for (int index = 0; index < size; index++) {
            observers.valueAt(index).onPackageRemoved(packageName, uid);
        }
    }
}
