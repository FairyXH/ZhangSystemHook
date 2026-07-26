package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
public class AppOpsServiceTestingShim implements com.android.server.appop.AppOpsCheckingServiceInterface {
    private com.android.server.appop.AppOpsCheckingServiceInterface mNewImplementation;
    private com.android.server.appop.AppOpsCheckingServiceInterface mOldImplementation;

    public AppOpsServiceTestingShim(com.android.server.appop.AppOpsCheckingServiceInterface oldValImpl, com.android.server.appop.AppOpsCheckingServiceInterface newImpl) {
        this.mOldImplementation = oldValImpl;
        this.mNewImplementation = newImpl;
    }

    private void signalImplDifference(java.lang.String message) {
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void writeState() {
        this.mOldImplementation.writeState();
        this.mNewImplementation.writeState();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void readState() {
        this.mOldImplementation.readState();
        this.mNewImplementation.readState();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void shutdown() {
        this.mOldImplementation.shutdown();
        this.mNewImplementation.shutdown();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void systemReady() {
        this.mOldImplementation.systemReady();
        this.mNewImplementation.systemReady();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseIntArray getNonDefaultUidModes(int uid, java.lang.String persistentDeviceId) {
        android.util.SparseIntArray oldVal = this.mOldImplementation.getNonDefaultUidModes(uid, persistentDeviceId);
        android.util.SparseIntArray newVal = this.mNewImplementation.getNonDefaultUidModes(uid, persistentDeviceId);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getNonDefaultUidModes");
        }
        return newVal;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseIntArray getNonDefaultPackageModes(java.lang.String packageName, int userId) {
        android.util.SparseIntArray oldVal = this.mOldImplementation.getNonDefaultPackageModes(packageName, userId);
        android.util.SparseIntArray newVal = this.mNewImplementation.getNonDefaultPackageModes(packageName, userId);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getNonDefaultPackageModes");
        }
        return newVal;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getUidMode(int uid, java.lang.String persistentDeviceId, int op) {
        int oldVal = this.mOldImplementation.getUidMode(uid, persistentDeviceId, op);
        int newVal = this.mNewImplementation.getUidMode(uid, persistentDeviceId, op);
        if (oldVal != newVal) {
            signalImplDifference("getUidMode");
        }
        return newVal;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean setUidMode(int uid, java.lang.String persistentDeviceId, int op, int mode) {
        boolean oldVal = this.mOldImplementation.setUidMode(uid, persistentDeviceId, op, mode);
        boolean newVal = this.mNewImplementation.setUidMode(uid, persistentDeviceId, op, mode);
        if (oldVal != newVal) {
            signalImplDifference("setUidMode");
        }
        return newVal;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getPackageMode(java.lang.String packageName, int op, int userId) {
        int oldVal = this.mOldImplementation.getPackageMode(packageName, op, userId);
        int newVal = this.mNewImplementation.getPackageMode(packageName, op, userId);
        if (oldVal != newVal) {
            signalImplDifference("getPackageMode");
        }
        return newVal;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void setPackageMode(java.lang.String packageName, int op, int mode, int userId) {
        this.mOldImplementation.setPackageMode(packageName, op, mode, userId);
        this.mNewImplementation.setPackageMode(packageName, op, mode, userId);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean removePackage(java.lang.String packageName, int userId) {
        boolean oldVal = this.mOldImplementation.removePackage(packageName, userId);
        boolean newVal = this.mNewImplementation.removePackage(packageName, userId);
        if (oldVal != newVal) {
            signalImplDifference("removePackage");
        }
        return newVal;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void removeUid(int uid) {
        this.mOldImplementation.removeUid(uid);
        this.mNewImplementation.removeUid(uid);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void clearAllModes() {
        this.mOldImplementation.clearAllModes();
        this.mNewImplementation.clearAllModes();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseBooleanArray getForegroundOps(int uid, java.lang.String persistentDeviceId) {
        android.util.SparseBooleanArray oldVal = this.mOldImplementation.getForegroundOps(uid, persistentDeviceId);
        android.util.SparseBooleanArray newVal = this.mNewImplementation.getForegroundOps(uid, persistentDeviceId);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getForegroundOps");
        }
        return newVal;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseBooleanArray getForegroundOps(java.lang.String packageName, int userId) {
        android.util.SparseBooleanArray oldVal = this.mOldImplementation.getForegroundOps(packageName, userId);
        android.util.SparseBooleanArray newVal = this.mNewImplementation.getForegroundOps(packageName, userId);
        if (!java.util.Objects.equals(oldVal, newVal)) {
            signalImplDifference("getForegroundOps");
        }
        return newVal;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean addAppOpsModeChangedListener(com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener) {
        boolean oldVal = this.mOldImplementation.addAppOpsModeChangedListener(listener);
        boolean newVal = this.mNewImplementation.addAppOpsModeChangedListener(listener);
        if (oldVal != newVal) {
            signalImplDifference("addAppOpsModeChangedListener");
        }
        return newVal;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean removeAppOpsModeChangedListener(com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener) {
        boolean oldVal = this.mOldImplementation.removeAppOpsModeChangedListener(listener);
        boolean newVal = this.mNewImplementation.removeAppOpsModeChangedListener(listener);
        if (oldVal != newVal) {
            signalImplDifference("removeAppOpsModeChangedListener");
        }
        return newVal;
    }
}
