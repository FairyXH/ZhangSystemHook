package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
public class AppOpsCheckingServiceLoggingDecorator implements com.android.server.appop.AppOpsCheckingServiceInterface {
    private static final java.lang.String LOG_TAG = com.android.server.appop.AppOpsCheckingServiceLoggingDecorator.class.getSimpleName();
    private final com.android.server.appop.AppOpsCheckingServiceInterface mService;

    public AppOpsCheckingServiceLoggingDecorator(com.android.server.appop.AppOpsCheckingServiceInterface service) {
        this.mService = service;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void writeState() {
        android.util.Log.i(LOG_TAG, "writeState()");
        this.mService.writeState();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void readState() {
        android.util.Log.i(LOG_TAG, "readState()");
        this.mService.readState();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void shutdown() {
        android.util.Log.i(LOG_TAG, "shutdown()");
        this.mService.shutdown();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void systemReady() {
        android.util.Log.i(LOG_TAG, "systemReady()");
        this.mService.systemReady();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseIntArray getNonDefaultUidModes(int uid, java.lang.String persistentDeviceId) {
        android.util.Log.i(LOG_TAG, "getNonDefaultUidModes(uid = " + uid + ")");
        return this.mService.getNonDefaultUidModes(uid, persistentDeviceId);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseIntArray getNonDefaultPackageModes(java.lang.String packageName, int userId) {
        android.util.Log.i(LOG_TAG, "getNonDefaultPackageModes(packageName = " + packageName + ", userId = " + userId + ") ");
        return this.mService.getNonDefaultPackageModes(packageName, userId);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getUidMode(int uid, java.lang.String persistentDeviceId, int op) {
        android.util.Log.i(LOG_TAG, "getUidMode(uid = " + uid + ", op = " + op + ")");
        return this.mService.getUidMode(uid, persistentDeviceId, op);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean setUidMode(int uid, java.lang.String persistentDeviceId, int op, int mode) {
        android.util.Log.i(LOG_TAG, "setUidMode(uid = " + uid + ", op = " + op + ", mode = " + mode + ")");
        return this.mService.setUidMode(uid, persistentDeviceId, op, mode);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getPackageMode(java.lang.String packageName, int op, int userId) {
        android.util.Log.i(LOG_TAG, "getPackageMode(packageName = " + packageName + ", op = " + op + ", userId = " + userId + ")");
        return this.mService.getPackageMode(packageName, op, userId);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void setPackageMode(java.lang.String packageName, int op, int mode, int userId) {
        android.util.Log.i(LOG_TAG, "setPackageMode(packageName = " + packageName + ", op = " + op + ", mode = " + mode + ", userId = " + userId + ")");
        this.mService.setPackageMode(packageName, op, mode, userId);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean removePackage(java.lang.String packageName, int userId) {
        android.util.Log.i(LOG_TAG, "removePackage(packageName = " + packageName + ", userId = " + userId + ")");
        return this.mService.removePackage(packageName, userId);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void removeUid(int uid) {
        android.util.Log.i(LOG_TAG, "removeUid(uid = " + uid + ")");
        this.mService.removeUid(uid);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void clearAllModes() {
        android.util.Log.i(LOG_TAG, "clearAllModes()");
        this.mService.clearAllModes();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseBooleanArray getForegroundOps(int uid, java.lang.String persistentDeviceId) {
        android.util.Log.i(LOG_TAG, "getForegroundOps(uid = " + uid + ")");
        return this.mService.getForegroundOps(uid, persistentDeviceId);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseBooleanArray getForegroundOps(java.lang.String packageName, int userId) {
        android.util.Log.i(LOG_TAG, "getForegroundOps(packageName = " + packageName + ", userId = " + userId + ")");
        return this.mService.getForegroundOps(packageName, userId);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean addAppOpsModeChangedListener(com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener) {
        android.util.Log.i(LOG_TAG, "addAppOpsModeChangedListener(listener = " + listener + ")");
        return this.mService.addAppOpsModeChangedListener(listener);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean removeAppOpsModeChangedListener(com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener) {
        android.util.Log.i(LOG_TAG, "removeAppOpsModeChangedListener(listener = " + listener + ")");
        return this.mService.removeAppOpsModeChangedListener(listener);
    }
}
