package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
class UsbSerialReader extends android.hardware.usb.IUsbSerialReader.Stub {
    private final android.content.Context mContext;
    private java.lang.Object mDevice;
    private final com.android.server.usb.UsbPermissionManager mPermissionManager;
    private final java.lang.String mSerialNumber;

    UsbSerialReader(android.content.Context context, com.android.server.usb.UsbPermissionManager permissionManager, java.lang.String serialNumber) {
        this.mContext = context;
        this.mPermissionManager = permissionManager;
        this.mSerialNumber = serialNumber;
    }

    public void setDevice(java.lang.Object device) {
        this.mDevice = device;
    }

    public java.lang.String getSerial(java.lang.String packageName) throws android.os.RemoteException {
        int pid = android.os.Binder.getCallingPid();
        int uid = android.os.Binder.getCallingUid();
        if (uid != 1000) {
            enforcePackageBelongsToUid(uid, packageName);
            android.os.UserHandle user = android.os.Binder.getCallingUserHandle();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    android.content.pm.PackageInfo pkg = this.mContext.getPackageManager().getPackageInfoAsUser(packageName, 0, user.getIdentifier());
                    int packageTargetSdkVersion = pkg.applicationInfo.targetSdkVersion;
                    if (packageTargetSdkVersion >= 29 && this.mContext.checkPermission("android.permission.MANAGE_USB", pid, uid) == -1) {
                        int userId = android.os.UserHandle.getUserId(uid);
                        if (this.mDevice instanceof android.hardware.usb.UsbDevice) {
                            this.mPermissionManager.getPermissionsForUser(userId).checkPermission((android.hardware.usb.UsbDevice) this.mDevice, packageName, pid, uid);
                        } else {
                            this.mPermissionManager.getPermissionsForUser(userId).checkPermission((android.hardware.usb.UsbAccessory) this.mDevice, pid, uid);
                        }
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    throw new android.os.RemoteException("package " + packageName + " cannot be found");
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
        return this.mSerialNumber;
    }

    private void enforcePackageBelongsToUid(int uid, java.lang.String packageName) {
        java.lang.String[] packages = this.mContext.getPackageManager().getPackagesForUid(uid);
        if (!com.android.internal.util.ArrayUtils.contains(packages, packageName)) {
            throw new java.lang.IllegalArgumentException(packageName + " does to belong to the " + uid);
        }
    }
}
