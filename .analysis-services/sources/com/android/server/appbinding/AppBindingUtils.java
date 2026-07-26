package com.android.server.appbinding;

/* JADX INFO: loaded from: classes.dex */
public class AppBindingUtils {
    private static final java.lang.String TAG = "AppBindingUtils";

    private AppBindingUtils() {
    }

    public static android.content.pm.ServiceInfo findService(java.lang.String packageName, int userId, java.lang.String serviceAction, java.lang.String servicePermission, java.lang.Class<?> serviceClassForLogging, android.content.pm.IPackageManager ipm, java.lang.StringBuilder errorMessage) {
        java.lang.String simpleClassName = serviceClassForLogging.getSimpleName();
        android.content.Intent intent = new android.content.Intent(serviceAction);
        intent.setPackage(packageName);
        errorMessage.setLength(0);
        try {
            android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> pls = ipm.queryIntentServices(intent, (java.lang.String) null, 0L, userId);
            if (pls != null && pls.getList().size() != 0) {
                java.util.List<android.content.pm.ResolveInfo> list = pls.getList();
                if (list.size() > 1) {
                    errorMessage.append("More than one " + simpleClassName + "'s found in package " + packageName + ".  They'll all be ignored.");
                    android.util.Log.e(TAG, errorMessage.toString());
                    return null;
                }
                android.content.pm.ServiceInfo si = list.get(0).serviceInfo;
                if (!servicePermission.equals(si.permission)) {
                    errorMessage.append(simpleClassName + " " + si.getComponentName().flattenToShortString() + " must be protected with " + servicePermission + ".");
                    android.util.Log.e(TAG, errorMessage.toString());
                    return null;
                }
                return si;
            }
            errorMessage.append("Service with " + serviceAction + " not found.");
            return null;
        } catch (android.os.RemoteException e) {
            return null;
        }
    }
}
