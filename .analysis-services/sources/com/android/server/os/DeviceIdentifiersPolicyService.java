package com.android.server.os;

/* JADX INFO: loaded from: classes2.dex */
public final class DeviceIdentifiersPolicyService extends com.android.server.SystemService {
    public DeviceIdentifiersPolicyService(android.content.Context context) {
        super(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("device_identifiers", new com.android.server.os.DeviceIdentifiersPolicyService.DeviceIdentifiersPolicy(getContext()));
    }

    private static final class DeviceIdentifiersPolicy extends android.os.IDeviceIdentifiersPolicyService.Stub {
        private final android.content.Context mContext;

        public DeviceIdentifiersPolicy(android.content.Context context) {
            this.mContext = context;
        }

        public java.lang.String getSerial() throws android.os.RemoteException {
            return !com.android.internal.telephony.TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(this.mContext, (java.lang.String) null, (java.lang.String) null, "getSerial") ? "unknown" : android.os.SystemProperties.get("ro.serialno", "unknown");
        }

        public java.lang.String getSerialForPackage(java.lang.String callingPackage, java.lang.String callingFeatureId) throws android.os.RemoteException {
            if (checkPackageBelongsToCaller(callingPackage)) {
                return !com.android.internal.telephony.TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(this.mContext, callingPackage, callingFeatureId, "getSerial") ? "unknown" : android.os.SystemProperties.get("ro.serialno", "unknown");
            }
            throw new java.lang.IllegalArgumentException("Invalid callingPackage or callingPackage does not belong to caller's uid:" + android.os.Binder.getCallingUid());
        }

        private boolean checkPackageBelongsToCaller(java.lang.String callingPackage) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingUserId = android.os.UserHandle.getUserId(callingUid);
            try {
                int callingPackageUid = this.mContext.getPackageManager().getPackageUidAsUser(callingPackage, callingUserId);
                return callingPackageUid == callingUid;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return false;
            }
        }

        public java.lang.String getPhoneSerialForPackage(java.lang.String callingPackage, java.lang.String callingFeatureId) throws android.os.RemoteException {
            return !com.android.internal.telephony.TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(this.mContext, callingPackage, callingFeatureId, "getPhoneSerial") ? "unknown" : android.os.SystemProperties.get("vendor.gsm.phoneserial", "unknown");
        }
    }
}
