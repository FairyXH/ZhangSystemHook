package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class ProtectedPackages {
    private java.lang.String mDeviceOwnerPackage;
    private int mDeviceOwnerUserId;
    private final java.lang.String mDeviceProvisioningPackage;
    private final android.util.SparseArray<java.util.Set<java.lang.String>> mOwnerProtectedPackages = new android.util.SparseArray<>();
    private android.util.SparseArray<java.lang.String> mProfileOwnerPackages;

    public ProtectedPackages(android.content.Context context) {
        this.mDeviceProvisioningPackage = context.getResources().getString(android.R.string.config_dozeDoubleTapSensorType);
    }

    public synchronized void setDeviceAndProfileOwnerPackages(int deviceOwnerUserId, java.lang.String deviceOwnerPackage, android.util.SparseArray<java.lang.String> profileOwnerPackages) {
        this.mDeviceOwnerUserId = deviceOwnerUserId;
        android.util.SparseArray<java.lang.String> sparseArrayClone = null;
        this.mDeviceOwnerPackage = deviceOwnerUserId == -10000 ? null : deviceOwnerPackage;
        if (profileOwnerPackages != null) {
            sparseArrayClone = profileOwnerPackages.clone();
        }
        this.mProfileOwnerPackages = sparseArrayClone;
    }

    public synchronized void setOwnerProtectedPackages(int userId, java.util.List<java.lang.String> packageNames) {
        if (packageNames == null) {
            this.mOwnerProtectedPackages.remove(userId);
        } else {
            this.mOwnerProtectedPackages.put(userId, new android.util.ArraySet(packageNames));
        }
    }

    private synchronized boolean hasDeviceOwnerOrProfileOwner(int userId, java.lang.String packageName) {
        if (packageName == null) {
            return false;
        }
        if (this.mDeviceOwnerPackage != null && this.mDeviceOwnerUserId == userId && packageName.equals(this.mDeviceOwnerPackage)) {
            return true;
        }
        if (this.mProfileOwnerPackages != null) {
            if (packageName.equals(this.mProfileOwnerPackages.get(userId))) {
                return true;
            }
        }
        return false;
    }

    public synchronized java.lang.String getDeviceOwnerOrProfileOwnerPackage(int userId) {
        if (this.mDeviceOwnerUserId == userId) {
            return this.mDeviceOwnerPackage;
        }
        if (this.mProfileOwnerPackages == null) {
            return null;
        }
        return this.mProfileOwnerPackages.get(userId);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0016  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized boolean isProtectedPackage(int r2, java.lang.String r3) {
        /*
            r1 = this;
            monitor-enter(r1)
            if (r3 == 0) goto L16
            java.lang.String r0 = r1.mDeviceProvisioningPackage     // Catch: java.lang.Throwable -> L13
            boolean r0 = r3.equals(r0)     // Catch: java.lang.Throwable -> L13
            if (r0 != 0) goto L11
            boolean r0 = r1.isOwnerProtectedPackage(r2, r3)     // Catch: java.lang.Throwable -> L13
            if (r0 == 0) goto L16
        L11:
            r0 = 1
            goto L17
        L13:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        L16:
            r0 = 0
        L17:
            monitor-exit(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ProtectedPackages.isProtectedPackage(int, java.lang.String):boolean");
    }

    private synchronized boolean isOwnerProtectedPackage(int userId, java.lang.String packageName) {
        boolean zIsPackageProtectedForUser;
        if (hasProtectedPackages(userId)) {
            zIsPackageProtectedForUser = isPackageProtectedForUser(userId, packageName);
        } else {
            zIsPackageProtectedForUser = isPackageProtectedForUser(-1, packageName);
        }
        return zIsPackageProtectedForUser;
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0019  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized boolean isPackageProtectedForUser(int r3, java.lang.String r4) {
        /*
            r2 = this;
            monitor-enter(r2)
            android.util.SparseArray<java.util.Set<java.lang.String>> r0 = r2.mOwnerProtectedPackages     // Catch: java.lang.Throwable -> L1c
            int r0 = r0.indexOfKey(r3)     // Catch: java.lang.Throwable -> L1c
            if (r0 < 0) goto L19
            android.util.SparseArray<java.util.Set<java.lang.String>> r1 = r2.mOwnerProtectedPackages     // Catch: java.lang.Throwable -> L1c
            java.lang.Object r1 = r1.valueAt(r0)     // Catch: java.lang.Throwable -> L1c
            java.util.Set r1 = (java.util.Set) r1     // Catch: java.lang.Throwable -> L1c
            boolean r1 = r1.contains(r4)     // Catch: java.lang.Throwable -> L1c
            if (r1 == 0) goto L19
            r1 = 1
            goto L1a
        L19:
            r1 = 0
        L1a:
            monitor-exit(r2)
            return r1
        L1c:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ProtectedPackages.isPackageProtectedForUser(int, java.lang.String):boolean");
    }

    private synchronized boolean hasProtectedPackages(int userId) {
        return this.mOwnerProtectedPackages.indexOfKey(userId) >= 0;
    }

    public boolean isPackageStateProtected(int userId, java.lang.String packageName) {
        return hasDeviceOwnerOrProfileOwner(userId, packageName) || isProtectedPackage(userId, packageName);
    }

    public boolean isPackageDataProtected(int userId, java.lang.String packageName) {
        return hasDeviceOwnerOrProfileOwner(userId, packageName) || isProtectedPackage(userId, packageName);
    }
}
