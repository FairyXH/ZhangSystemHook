package com.android.server.pm.verify.domain.proxy;

/* JADX INFO: loaded from: classes2.dex */
class DomainVerificationProxyCombined implements com.android.server.pm.verify.domain.proxy.DomainVerificationProxy {
    private final com.android.server.pm.verify.domain.proxy.DomainVerificationProxy mProxyV1;
    private final com.android.server.pm.verify.domain.proxy.DomainVerificationProxy mProxyV2;

    DomainVerificationProxyCombined(com.android.server.pm.verify.domain.proxy.DomainVerificationProxy proxyV1, com.android.server.pm.verify.domain.proxy.DomainVerificationProxy proxyV2) {
        this.mProxyV1 = proxyV1;
        this.mProxyV2 = proxyV2;
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public void sendBroadcastForPackages(java.util.Set<java.lang.String> packageNames) {
        this.mProxyV2.sendBroadcastForPackages(packageNames);
        this.mProxyV1.sendBroadcastForPackages(packageNames);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public boolean runMessage(int messageCode, java.lang.Object object) {
        boolean resultV2 = this.mProxyV2.runMessage(messageCode, object);
        boolean resultV1 = this.mProxyV1.runMessage(messageCode, object);
        return resultV2 || resultV1;
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public boolean isCallerVerifier(int callingUid) {
        return this.mProxyV2.isCallerVerifier(callingUid) || this.mProxyV1.isCallerVerifier(callingUid);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public android.content.ComponentName getComponentName() {
        return this.mProxyV2.getComponentName();
    }
}
