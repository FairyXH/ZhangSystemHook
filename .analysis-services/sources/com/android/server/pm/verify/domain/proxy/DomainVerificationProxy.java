package com.android.server.pm.verify.domain.proxy;

/* JADX INFO: loaded from: classes2.dex */
public interface DomainVerificationProxy {
    public static final boolean DEBUG_PROXIES = false;
    public static final java.lang.String TAG = "DomainVerificationProxy";

    public interface BaseConnection {
        com.android.server.DeviceIdleInternal getDeviceIdleInternal();

        long getPowerSaveTempWhitelistAppDuration();

        boolean isCallerPackage(int i, java.lang.String str);

        void schedule(int i, java.lang.Object obj);
    }

    android.content.ComponentName getComponentName();

    boolean isCallerVerifier(int i);

    boolean runMessage(int i, java.lang.Object obj);

    void sendBroadcastForPackages(java.util.Set<java.lang.String> set);

    static <ConnectionType extends com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1.Connection & com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV2.Connection> com.android.server.pm.verify.domain.proxy.DomainVerificationProxy makeProxy(android.content.ComponentName componentV1, android.content.ComponentName componentV2, android.content.Context context, com.android.server.pm.verify.domain.DomainVerificationManagerInternal manager, com.android.server.pm.verify.domain.DomainVerificationCollector collector, ConnectionType connection) {
        if (componentV2 != null && componentV1 != null && !java.util.Objects.equals(componentV2.getPackageName(), componentV1.getPackageName())) {
            componentV1 = null;
        }
        com.android.server.pm.verify.domain.proxy.DomainVerificationProxy proxyV1 = null;
        com.android.server.pm.verify.domain.proxy.DomainVerificationProxy proxyV2 = null;
        if (componentV1 != null) {
            proxyV1 = new com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1(context, manager, collector, connection, componentV1);
        }
        if (componentV2 != null) {
            proxyV2 = new com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV2(context, connection, componentV2);
        }
        if (proxyV1 != null && proxyV2 != null) {
            return new com.android.server.pm.verify.domain.proxy.DomainVerificationProxyCombined(proxyV1, proxyV2);
        }
        if (proxyV1 != null) {
            return proxyV1;
        }
        if (proxyV2 != null) {
            return proxyV2;
        }
        return new com.android.server.pm.verify.domain.proxy.DomainVerificationProxyUnavailable();
    }
}
