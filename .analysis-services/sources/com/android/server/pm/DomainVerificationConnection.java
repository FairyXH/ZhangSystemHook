package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class DomainVerificationConnection implements com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection, com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1.Connection, com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV2.Connection {
    final com.android.server.pm.PackageManagerService mPm;
    final com.android.server.pm.UserManagerInternal mUmInternal;

    DomainVerificationConnection(com.android.server.pm.PackageManagerService pm) {
        this.mPm = pm;
        this.mUmInternal = (com.android.server.pm.UserManagerInternal) this.mPm.mInjector.getLocalService(com.android.server.pm.UserManagerInternal.class);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection
    public void scheduleWriteSettings() {
        this.mPm.scheduleWriteSettings();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection
    public int getCallingUid() {
        return android.os.Binder.getCallingUid();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection
    public int getCallingUserId() {
        return android.os.UserHandle.getCallingUserId();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection, com.android.server.pm.verify.domain.proxy.DomainVerificationProxy.BaseConnection
    public void schedule(int code, java.lang.Object object) {
        android.os.Message message = this.mPm.mHandler.obtainMessage(27);
        message.arg1 = code;
        message.obj = object;
        this.mPm.mHandler.sendMessage(message);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy.BaseConnection
    public long getPowerSaveTempWhitelistAppDuration() {
        return com.android.server.pm.VerificationUtils.getDefaultVerificationTimeout(this.mPm.mContext);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy.BaseConnection
    public com.android.server.DeviceIdleInternal getDeviceIdleInternal() {
        return (com.android.server.DeviceIdleInternal) this.mPm.mInjector.getLocalService(com.android.server.DeviceIdleInternal.class);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy.BaseConnection
    public boolean isCallerPackage(int callingUid, java.lang.String packageName) {
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        return callingUid == this.mPm.snapshotComputer().getPackageUid(packageName, 0L, callingUserId);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1.Connection
    public com.android.server.pm.pkg.AndroidPackage getPackage(java.lang.String packageName) {
        return this.mPm.snapshotComputer().getPackage(packageName);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationEnforcer.Callback
    public boolean filterAppAccess(java.lang.String packageName, int callingUid, int userId) {
        return this.mPm.snapshotComputer().filterAppAccess(packageName, callingUid, userId, true);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection
    public int[] getAllUserIds() {
        return this.mUmInternal.getUserIds();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationEnforcer.Callback
    public boolean doesUserExist(int userId) {
        return this.mUmInternal.exists(userId);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection
    public com.android.server.pm.Computer snapshot() {
        return this.mPm.snapshotComputer();
    }
}
