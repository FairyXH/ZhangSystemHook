package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class HardwarePropertiesManagerService extends android.os.IHardwarePropertiesManager.Stub {
    private static final java.lang.String TAG = "HardwarePropertiesManagerService";
    private final android.app.AppOpsManager mAppOps;
    private final android.content.Context mContext;
    private final java.lang.Object mLock = new java.lang.Object();

    private static native android.os.CpuUsageInfo[] nativeGetCpuUsages();

    private static native float[] nativeGetDeviceTemperatures(int i, int i2);

    private static native float[] nativeGetFanSpeeds();

    private static native void nativeInit();

    public HardwarePropertiesManagerService(android.content.Context context) {
        this.mContext = context;
        this.mAppOps = (android.app.AppOpsManager) this.mContext.getSystemService("appops");
        synchronized (this.mLock) {
            nativeInit();
        }
    }

    public float[] getDeviceTemperatures(java.lang.String callingPackage, int type, int source) throws java.lang.SecurityException {
        float[] fArrNativeGetDeviceTemperatures;
        enforceHardwarePropertiesRetrievalAllowed(callingPackage);
        synchronized (this.mLock) {
            fArrNativeGetDeviceTemperatures = nativeGetDeviceTemperatures(type, source);
        }
        return fArrNativeGetDeviceTemperatures;
    }

    public android.os.CpuUsageInfo[] getCpuUsages(java.lang.String callingPackage) throws java.lang.SecurityException {
        android.os.CpuUsageInfo[] cpuUsageInfoArrNativeGetCpuUsages;
        enforceHardwarePropertiesRetrievalAllowed(callingPackage);
        synchronized (this.mLock) {
            cpuUsageInfoArrNativeGetCpuUsages = nativeGetCpuUsages();
        }
        return cpuUsageInfoArrNativeGetCpuUsages;
    }

    public float[] getFanSpeeds(java.lang.String callingPackage) throws java.lang.SecurityException {
        float[] fArrNativeGetFanSpeeds;
        enforceHardwarePropertiesRetrievalAllowed(callingPackage);
        synchronized (this.mLock) {
            fArrNativeGetFanSpeeds = nativeGetFanSpeeds();
        }
        return fArrNativeGetFanSpeeds;
    }

    private java.lang.String getCallingPackageName() {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        int uid = android.os.Binder.getCallingUid();
        java.lang.String[] packages = pm.getPackagesForUid(uid);
        if (packages != null && packages.length > 0) {
            return packages[0];
        }
        java.lang.String name = pm.getNameForUid(uid);
        if (name != null) {
            return name;
        }
        return java.lang.String.valueOf(uid);
    }

    private void dumpTempValues(java.lang.String pkg, java.io.PrintWriter pw, int type, java.lang.String typeLabel) {
        dumpTempValues(pkg, pw, type, typeLabel, "temperatures: ", 0);
        dumpTempValues(pkg, pw, type, typeLabel, "throttling temperatures: ", 1);
        dumpTempValues(pkg, pw, type, typeLabel, "shutdown temperatures: ", 2);
        dumpTempValues(pkg, pw, type, typeLabel, "vr throttling temperatures: ", 3);
    }

    private void dumpTempValues(java.lang.String pkg, java.io.PrintWriter pw, int type, java.lang.String typeLabel, java.lang.String subLabel, int valueType) {
        pw.println(typeLabel + subLabel + java.util.Arrays.toString(getDeviceTemperatures(pkg, type, valueType)));
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            pw.println("****** Dump of HardwarePropertiesManagerService ******");
            java.lang.String PKG = getCallingPackageName();
            dumpTempValues(PKG, pw, 0, "CPU ");
            dumpTempValues(PKG, pw, 1, "GPU ");
            dumpTempValues(PKG, pw, 2, "Battery ");
            dumpTempValues(PKG, pw, 3, "Skin ");
            float[] fanSpeeds = getFanSpeeds(PKG);
            pw.println("Fan speed: " + java.util.Arrays.toString(fanSpeeds) + "\n");
            android.os.CpuUsageInfo[] cpuUsageInfos = getCpuUsages(PKG);
            for (int i = 0; i < cpuUsageInfos.length; i++) {
                pw.println("Cpu usage of core: " + i + ", active = " + cpuUsageInfos[i].getActive() + ", total = " + cpuUsageInfos[i].getTotal());
            }
            pw.println("****** End of HardwarePropertiesManagerService dump ******");
        }
    }

    private void enforceHardwarePropertiesRetrievalAllowed(java.lang.String callingPackage) throws java.lang.SecurityException {
        android.app.admin.DevicePolicyManager dpm;
        this.mAppOps.checkPackage(android.os.Binder.getCallingUid(), callingPackage);
        int userId = android.os.UserHandle.getUserId(android.os.Binder.getCallingUid());
        com.android.server.vr.VrManagerInternal vrService = (com.android.server.vr.VrManagerInternal) com.android.server.LocalServices.getService(com.android.server.vr.VrManagerInternal.class);
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.systemServerRoleControllerEnabled() && android.app.compat.CompatChanges.isChangeEnabled(307233716L)) {
            android.os.UserHandle handle = new android.os.UserHandle(userId);
            dpm = (android.app.admin.DevicePolicyManager) this.mContext.createContextAsUser(handle, 0).getSystemService(android.app.admin.DevicePolicyManager.class);
        } else {
            dpm = (android.app.admin.DevicePolicyManager) this.mContext.getSystemService(android.app.admin.DevicePolicyManager.class);
        }
        if (!dpm.isDeviceOwnerApp(callingPackage) && this.mContext.checkCallingOrSelfPermission("android.permission.DEVICE_POWER") != 0) {
            if (vrService == null || !vrService.isCurrentVrListener(callingPackage, userId)) {
                throw new java.lang.SecurityException("The caller is neither a device owner, nor holding the DEVICE_POWER permission, nor the current VrListener.");
            }
        }
    }
}
