package com.android.server.pm.verify.domain.proxy;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationProxyUnavailable implements com.android.server.pm.verify.domain.proxy.DomainVerificationProxy {
    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public void sendBroadcastForPackages(java.util.Set<java.lang.String> packageNames) {
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public boolean runMessage(int messageCode, java.lang.Object object) {
        return false;
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public boolean isCallerVerifier(int callingUid) {
        return false;
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public android.content.ComponentName getComponentName() {
        return null;
    }
}
