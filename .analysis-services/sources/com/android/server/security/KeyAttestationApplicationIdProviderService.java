package com.android.server.security;

/* JADX INFO: loaded from: classes3.dex */
public class KeyAttestationApplicationIdProviderService extends android.security.keystore.IKeyAttestationApplicationIdProvider.Stub {
    private android.content.pm.PackageManager mPackageManager;

    public KeyAttestationApplicationIdProviderService(android.content.Context context) {
        this.mPackageManager = context.getPackageManager();
    }

    @Override // android.security.keystore.IKeyAttestationApplicationIdProvider
    public android.security.keystore.KeyAttestationApplicationId getKeyAttestationApplicationId(int uid) throws android.os.RemoteException {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 1017 && callingUid != 1076) {
            throw new java.lang.SecurityException("This service can only be used by Keystore or Credstore");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                java.lang.String[] packageNames = this.mPackageManager.getPackagesForUid(uid);
                if (packageNames == null) {
                    throw new android.os.ServiceSpecificException(1, "No package for uid: " + uid);
                }
                int userId = android.os.UserHandle.getUserId(uid);
                android.security.keystore.KeyAttestationPackageInfo[] keyAttestationPackageInfos = new android.security.keystore.KeyAttestationPackageInfo[packageNames.length];
                for (int i = 0; i < packageNames.length; i++) {
                    android.content.pm.PackageInfo packageInfo = this.mPackageManager.getPackageInfoAsUser(packageNames[i], 64, userId);
                    android.security.keystore.KeyAttestationPackageInfo pInfo = new android.security.keystore.KeyAttestationPackageInfo();
                    pInfo.packageName = new java.lang.String(packageNames[i]);
                    pInfo.versionCode = packageInfo.getLongVersionCode();
                    pInfo.signatures = new android.security.keystore.Signature[packageInfo.signatures.length];
                    for (int index = 0; index < packageInfo.signatures.length; index++) {
                        android.security.keystore.Signature sign = new android.security.keystore.Signature();
                        sign.data = packageInfo.signatures[index].toByteArray();
                        pInfo.signatures[index] = sign;
                    }
                    keyAttestationPackageInfos[i] = pInfo;
                }
                android.os.Binder.restoreCallingIdentity(token);
                android.security.keystore.KeyAttestationApplicationId attestAppId = new android.security.keystore.KeyAttestationApplicationId();
                attestAppId.packageInfos = keyAttestationPackageInfos;
                return attestAppId;
            } catch (android.content.pm.PackageManager.NameNotFoundException nnfe) {
                throw new android.os.RemoteException(nnfe.getMessage());
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }
}
