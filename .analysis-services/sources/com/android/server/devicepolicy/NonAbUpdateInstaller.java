package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
class NonAbUpdateInstaller extends com.android.server.devicepolicy.UpdateInstaller {
    NonAbUpdateInstaller(android.content.Context context, android.os.ParcelFileDescriptor updateFileDescriptor, android.app.admin.StartInstallingUpdateCallback callback, com.android.server.devicepolicy.DevicePolicyManagerService.Injector injector, com.android.server.devicepolicy.DevicePolicyConstants constants) {
        super(context, updateFileDescriptor, callback, injector, constants);
    }

    @Override // com.android.server.devicepolicy.UpdateInstaller
    public void installUpdateInThread() {
        try {
            android.os.RecoverySystem.installPackage(this.mContext, this.mCopiedUpdateFile);
            notifyCallbackOnSuccess();
        } catch (java.io.IOException e) {
            android.util.Log.w("UpdateInstaller", "IO error while trying to install non AB update.", e);
            notifyCallbackOnError(1, android.util.Log.getStackTraceString(e));
        }
    }
}
