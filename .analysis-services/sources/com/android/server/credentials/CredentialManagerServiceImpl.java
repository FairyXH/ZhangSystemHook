package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public final class CredentialManagerServiceImpl extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.credentials.CredentialManagerServiceImpl, com.android.server.credentials.CredentialManagerService> {
    private static final java.lang.String TAG = "CredentialManager";
    private android.credentials.CredentialProviderInfo mInfo;

    CredentialManagerServiceImpl(com.android.server.credentials.CredentialManagerService master, java.lang.Object lock, int userId, java.lang.String serviceName) throws android.content.pm.PackageManager.NameNotFoundException {
        super(master, lock, userId);
        android.util.Slog.i(TAG, "CredentialManagerServiceImpl constructed for: " + serviceName);
        synchronized (this.mLock) {
            newServiceInfoLocked(android.content.ComponentName.unflattenFromString(serviceName));
        }
    }

    public android.content.ComponentName getComponentName() {
        return this.mInfo.getServiceInfo().getComponentName();
    }

    CredentialManagerServiceImpl(com.android.server.credentials.CredentialManagerService master, java.lang.Object lock, int userId, android.credentials.CredentialProviderInfo providerInfo) {
        super(master, lock, userId);
        android.util.Slog.i(TAG, "CredentialManagerServiceImpl constructed for: " + providerInfo.getServiceInfo().getComponentName().flattenToString());
        this.mInfo = providerInfo;
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        if (this.mInfo != null) {
            android.util.Slog.i(TAG, "newServiceInfoLocked, mInfo not null : " + this.mInfo.getServiceInfo().getComponentName().flattenToString() + " , " + serviceComponent.flattenToString());
        } else {
            android.util.Slog.i(TAG, "newServiceInfoLocked, mInfo null, " + serviceComponent.flattenToString());
        }
        java.util.Set<android.content.ComponentName> primaryProviders = com.android.server.credentials.CredentialManagerService.getPrimaryProvidersForUserId(((com.android.server.credentials.CredentialManagerService) this.mMaster).getContext(), this.mUserId);
        this.mInfo = android.service.credentials.CredentialProviderInfoFactory.create(getContext(), serviceComponent, this.mUserId, false, primaryProviders.contains(serviceComponent));
        return this.mInfo.getServiceInfo();
    }

    public com.android.server.credentials.ProviderSession initiateProviderSessionForRequestLocked(com.android.server.credentials.RequestSession requestSession, java.util.List<java.lang.String> requestOptions) {
        if (!requestOptions.isEmpty() && !isServiceCapableLocked(requestOptions)) {
            if (this.mInfo != null) {
                android.util.Slog.i(TAG, "Service does not have the required capabilities: " + this.mInfo.getComponentName());
            }
            return null;
        }
        if (this.mInfo == null) {
            android.util.Slog.w(TAG, "Initiating provider session for request but mInfo is null. This shouldn't happen");
            return null;
        }
        com.android.server.credentials.RemoteCredentialService remoteService = new com.android.server.credentials.RemoteCredentialService(getContext(), this.mInfo.getServiceInfo().getComponentName(), this.mUserId);
        return requestSession.initiateProviderSession(this.mInfo, remoteService);
    }

    boolean isServiceCapableLocked(java.util.List<java.lang.String> requestedOptions) {
        if (this.mInfo == null) {
            return false;
        }
        for (java.lang.String capability : requestedOptions) {
            if (this.mInfo.hasCapability(capability)) {
                return true;
            }
        }
        return false;
    }

    public android.credentials.CredentialProviderInfo getCredentialProviderInfo() {
        return this.mInfo;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractPerUserSystemService
    public void handlePackageUpdateLocked(java.lang.String packageName) {
        if (this.mInfo != null && this.mInfo.getServiceInfo() != null && this.mInfo.getServiceInfo().getComponentName().getPackageName().equals(packageName)) {
            try {
                newServiceInfoLocked(this.mInfo.getServiceInfo().getComponentName());
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.e(TAG, "Issue while updating serviceInfo: " + e.getMessage());
            }
        }
    }
}
