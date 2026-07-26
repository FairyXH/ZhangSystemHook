package com.android.server.pm.verify.domain.proxy;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationProxyV2 implements com.android.server.pm.verify.domain.proxy.DomainVerificationProxy {
    private static final boolean DEBUG_BROADCASTS = false;
    private static final java.lang.String TAG = "DomainVerificationProxyV2";
    private final com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV2.Connection mConnection;
    private final android.content.Context mContext;
    private final android.content.ComponentName mVerifierComponent;

    public interface Connection extends com.android.server.pm.verify.domain.proxy.DomainVerificationProxy.BaseConnection {
    }

    public DomainVerificationProxyV2(android.content.Context context, com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV2.Connection connection, android.content.ComponentName verifierComponent) {
        this.mContext = context;
        this.mConnection = connection;
        this.mVerifierComponent = verifierComponent;
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public void sendBroadcastForPackages(java.util.Set<java.lang.String> packageNames) {
        this.mConnection.schedule(1, packageNames);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public boolean runMessage(int messageCode, java.lang.Object object) {
        switch (messageCode) {
            case 1:
                java.util.Set<java.lang.String> packageNames = (java.util.Set) object;
                android.os.Parcelable domainVerificationRequest = new android.content.pm.verify.domain.DomainVerificationRequest(packageNames);
                long allowListTimeout = this.mConnection.getPowerSaveTempWhitelistAppDuration();
                android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
                options.setTemporaryAppAllowlist(allowListTimeout, 0, 308, "");
                this.mConnection.getDeviceIdleInternal().addPowerSaveTempWhitelistApp(android.os.Process.myUid(), this.mVerifierComponent.getPackageName(), allowListTimeout, 0, true, 308, "domain verification agent");
                android.content.Intent intent = new android.content.Intent("android.intent.action.DOMAINS_NEED_VERIFICATION").setComponent(this.mVerifierComponent).putExtra("android.content.pm.verify.domain.extra.VERIFICATION_REQUEST", domainVerificationRequest).addFlags(268435456);
                this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.SYSTEM, null, options.toBundle());
                return true;
            default:
                return false;
        }
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public boolean isCallerVerifier(int callingUid) {
        return this.mConnection.isCallerPackage(callingUid, this.mVerifierComponent.getPackageName());
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public android.content.ComponentName getComponentName() {
        return this.mVerifierComponent;
    }
}
