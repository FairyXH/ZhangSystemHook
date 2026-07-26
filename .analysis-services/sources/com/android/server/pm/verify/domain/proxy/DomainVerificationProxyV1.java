package com.android.server.pm.verify.domain.proxy;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationProxyV1 implements com.android.server.pm.verify.domain.proxy.DomainVerificationProxy {
    private static final boolean DEBUG_BROADCASTS = false;
    private static final java.lang.String TAG = "DomainVerificationProxyV1";
    private final com.android.server.pm.verify.domain.DomainVerificationCollector mCollector;
    private final com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1.Connection mConnection;
    private final android.content.Context mContext;
    private final com.android.server.pm.verify.domain.DomainVerificationManagerInternal mManager;
    private final android.content.ComponentName mVerifierComponent;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.ArrayMap<java.lang.Integer, android.util.Pair<java.util.UUID, java.lang.String>> mRequests = new android.util.ArrayMap<>();
    private int mVerificationToken = 0;

    public interface Connection extends com.android.server.pm.verify.domain.proxy.DomainVerificationProxy.BaseConnection {
        com.android.server.pm.pkg.AndroidPackage getPackage(java.lang.String str);
    }

    public DomainVerificationProxyV1(android.content.Context context, com.android.server.pm.verify.domain.DomainVerificationManagerInternal manager, com.android.server.pm.verify.domain.DomainVerificationCollector collector, com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1.Connection connection, android.content.ComponentName verifierComponent) {
        this.mContext = context;
        this.mConnection = connection;
        this.mVerifierComponent = verifierComponent;
        this.mManager = manager;
        this.mCollector = collector;
    }

    public static void queueLegacyVerifyResult(android.content.Context context, com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1.Connection connection, int verificationId, int verificationCode, java.util.List<java.lang.String> failedDomains, int callingUid) {
        context.enforceCallingOrSelfPermission("android.permission.INTENT_FILTER_VERIFICATION_AGENT", "Only the intent filter verification agent can verify applications");
        connection.schedule(3, new com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1.Response(callingUid, verificationId, verificationCode, failedDomains));
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public void sendBroadcastForPackages(java.util.Set<java.lang.String> packageNames) {
        synchronized (this.mLock) {
            int size = this.mRequests.size();
            for (int index = size - 1; index >= 0; index--) {
                android.util.Pair<java.util.UUID, java.lang.String> pair = this.mRequests.valueAt(index);
                if (packageNames.contains(pair.second)) {
                    this.mRequests.removeAt(index);
                }
            }
        }
        this.mConnection.schedule(2, packageNames);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public boolean runMessage(int messageCode, java.lang.Object object) {
        switch (messageCode) {
            case 2:
                java.util.Set<java.lang.String> packageNames = (java.util.Set) object;
                android.util.ArrayMap<java.lang.Integer, android.util.Pair<java.util.UUID, java.lang.String>> newRequests = new android.util.ArrayMap<>(packageNames.size());
                synchronized (this.mLock) {
                    for (java.lang.String packageName : packageNames) {
                        java.util.UUID domainSetId = this.mManager.getDomainVerificationInfoId(packageName);
                        if (domainSetId != null) {
                            int i = this.mVerificationToken;
                            this.mVerificationToken = i + 1;
                            newRequests.put(java.lang.Integer.valueOf(i), android.util.Pair.create(domainSetId, packageName));
                        }
                    }
                    this.mRequests.putAll((android.util.ArrayMap<? extends java.lang.Integer, ? extends android.util.Pair<java.util.UUID, java.lang.String>>) newRequests);
                    break;
                }
                sendBroadcasts(newRequests);
                return true;
            case 3:
                com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1.Response response = (com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1.Response) object;
                android.util.Pair<java.util.UUID, java.lang.String> pair = this.mRequests.get(java.lang.Integer.valueOf(response.verificationId));
                if (pair == null) {
                    return true;
                }
                java.util.UUID domainSetId2 = (java.util.UUID) pair.first;
                java.lang.String packageName2 = (java.lang.String) pair.second;
                try {
                    android.content.pm.verify.domain.DomainVerificationInfo info = this.mManager.getDomainVerificationInfo(packageName2);
                    if (info == null || !java.util.Objects.equals(domainSetId2, info.getIdentifier())) {
                        return true;
                    }
                    com.android.server.pm.pkg.AndroidPackage pkg = this.mConnection.getPackage(packageName2);
                    if (pkg == null) {
                        return true;
                    }
                    android.util.ArraySet<? extends java.lang.String> failedDomains = new android.util.ArraySet<>(response.failedDomains);
                    java.util.Map<java.lang.String, java.lang.Integer> hostToStateMap = info.getHostToStateMap();
                    java.util.Set<java.lang.String> hostKeySet = hostToStateMap.keySet();
                    android.util.ArraySet<java.lang.String> successfulDomains = new android.util.ArraySet<>(hostKeySet);
                    successfulDomains.removeAll(failedDomains);
                    int size = successfulDomains.size();
                    for (int index = size - 1; index >= 0; index--) {
                        java.lang.String domain = successfulDomains.valueAt(index);
                        if (domain.startsWith("*.")) {
                            java.lang.String nonWildcardDomain = domain.substring(2);
                            if (failedDomains.contains(nonWildcardDomain)) {
                                failedDomains.add(domain);
                                successfulDomains.removeAt(index);
                                if (!hostKeySet.contains(nonWildcardDomain)) {
                                    failedDomains.remove(nonWildcardDomain);
                                }
                            }
                        }
                    }
                    int callingUid = response.callingUid;
                    if (!successfulDomains.isEmpty()) {
                        try {
                            if (this.mManager.setDomainVerificationStatusInternal(callingUid, domainSetId2, successfulDomains, 1) != 0) {
                                android.util.Slog.e(TAG, "Failure reporting successful domains for " + packageName2);
                            }
                        } catch (java.lang.Exception e) {
                            android.util.Slog.e(TAG, "Failure reporting successful domains for " + packageName2, e);
                        }
                        break;
                    }
                    if (!failedDomains.isEmpty()) {
                        try {
                            if (this.mManager.setDomainVerificationStatusInternal(callingUid, domainSetId2, failedDomains, 6) != 0) {
                                android.util.Slog.e(TAG, "Failure reporting failed domains for " + packageName2);
                                return true;
                            }
                            return true;
                        } catch (java.lang.Exception e2) {
                            android.util.Slog.e(TAG, "Failure reporting failed domains for " + packageName2, e2);
                            return true;
                        }
                    }
                    return true;
                } catch (android.content.pm.PackageManager.NameNotFoundException e3) {
                    return true;
                }
            default:
                return false;
        }
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public boolean isCallerVerifier(int callingUid) {
        return this.mConnection.isCallerPackage(callingUid, this.mVerifierComponent.getPackageName());
    }

    private void sendBroadcasts(android.util.ArrayMap<java.lang.Integer, android.util.Pair<java.util.UUID, java.lang.String>> verifications) {
        android.util.ArrayMap<java.lang.Integer, android.util.Pair<java.util.UUID, java.lang.String>> arrayMap = verifications;
        long allowListTimeout = this.mConnection.getPowerSaveTempWhitelistAppDuration();
        this.mConnection.getDeviceIdleInternal().addPowerSaveTempWhitelistApp(android.os.Process.myUid(), this.mVerifierComponent.getPackageName(), allowListTimeout, 0, true, com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_DOMAIN_VERIFICATION_V1, "domain verification agent");
        int size = verifications.size();
        int index = 0;
        while (index < size) {
            int verificationId = arrayMap.keyAt(index).intValue();
            java.lang.String packageName = (java.lang.String) arrayMap.valueAt(index).second;
            com.android.server.pm.pkg.AndroidPackage pkg = this.mConnection.getPackage(packageName);
            if (pkg != null) {
                java.lang.String hostsString = buildHostsString(pkg);
                android.content.Intent intent = new android.content.Intent("android.intent.action.INTENT_FILTER_NEEDS_VERIFICATION").setComponent(this.mVerifierComponent).putExtra("android.content.pm.extra.INTENT_FILTER_VERIFICATION_ID", verificationId).putExtra("android.content.pm.extra.INTENT_FILTER_VERIFICATION_URI_SCHEME", "https").putExtra("android.content.pm.extra.INTENT_FILTER_VERIFICATION_HOSTS", hostsString).putExtra("android.content.pm.extra.INTENT_FILTER_VERIFICATION_PACKAGE_NAME", packageName).addFlags(268435456);
                android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
                options.setTemporaryAppAllowlist(allowListTimeout, 0, com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_DOMAIN_VERIFICATION_V1, "");
                this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.SYSTEM, null, options.toBundle());
            }
            index++;
            arrayMap = verifications;
        }
    }

    private java.lang.String buildHostsString(com.android.server.pm.pkg.AndroidPackage pkg) {
        android.util.ArraySet<java.lang.String> domains = this.mCollector.collectValidAutoVerifyDomains(pkg);
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        int size = domains.size();
        for (int index = 0; index < size; index++) {
            if (index > 0) {
                builder.append(" ");
            }
            java.lang.String domain = domains.valueAt(index);
            if (domain.startsWith("*.")) {
                domain = domain.substring(2);
            }
            builder.append(domain);
        }
        return builder.toString();
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public android.content.ComponentName getComponentName() {
        return this.mVerifierComponent;
    }

    private static class Response {
        public final int callingUid;
        public final java.util.List<java.lang.String> failedDomains;
        public final int verificationCode;
        public final int verificationId;

        private Response(int callingUid, int verificationId, int verificationCode, java.util.List<java.lang.String> failedDomains) {
            this.callingUid = callingUid;
            this.verificationId = verificationId;
            this.verificationCode = verificationCode;
            this.failedDomains = failedDomains == null ? java.util.Collections.emptyList() : failedDomains;
        }
    }
}
