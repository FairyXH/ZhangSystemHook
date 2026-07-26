package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public class VpnIkev2Utils {
    private static final java.lang.String TAG = com.android.server.connectivity.VpnIkev2Utils.class.getSimpleName();

    static android.net.ipsec.ike.IkeSessionParams.Builder makeIkeSessionParamsBuilder(android.content.Context context, android.net.Ikev2VpnProfile profile, android.net.Network network) {
        android.net.ipsec.ike.IkeIdentification localId = parseIkeIdentification(profile.getUserIdentity());
        android.net.ipsec.ike.IkeIdentification remoteId = parseIkeIdentification(profile.getServerAddr());
        android.net.ipsec.ike.IkeSessionParams.Builder ikeOptionsBuilder = new android.net.ipsec.ike.IkeSessionParams.Builder(context).setServerHostname(profile.getServerAddr()).setNetwork(network).addIkeOption(2).setLocalIdentification(localId).setRemoteIdentification(remoteId);
        setIkeAuth(profile, ikeOptionsBuilder);
        for (android.net.ipsec.ike.IkeSaProposal ikeProposal : getIkeSaProposals()) {
            ikeOptionsBuilder.addSaProposal(ikeProposal);
        }
        return ikeOptionsBuilder;
    }

    static android.net.ipsec.ike.ChildSessionParams buildChildSessionParams(java.util.List<java.lang.String> allowedAlgorithms) {
        android.net.ipsec.ike.TunnelModeChildSessionParams.Builder childOptionsBuilder = new android.net.ipsec.ike.TunnelModeChildSessionParams.Builder();
        for (android.net.ipsec.ike.ChildSaProposal childProposal : getChildSaProposals(allowedAlgorithms)) {
            childOptionsBuilder.addSaProposal(childProposal);
        }
        childOptionsBuilder.addInternalAddressRequest(android.system.OsConstants.AF_INET);
        childOptionsBuilder.addInternalAddressRequest(android.system.OsConstants.AF_INET6);
        childOptionsBuilder.addInternalDnsServerRequest(android.system.OsConstants.AF_INET);
        childOptionsBuilder.addInternalDnsServerRequest(android.system.OsConstants.AF_INET6);
        return childOptionsBuilder.build();
    }

    private static void setIkeAuth(android.net.Ikev2VpnProfile profile, android.net.ipsec.ike.IkeSessionParams.Builder builder) {
        switch (profile.getType()) {
            case 6:
                android.net.eap.EapSessionConfig eapConfig = new android.net.eap.EapSessionConfig.Builder().setEapMsChapV2Config(profile.getUsername(), profile.getPassword()).build();
                builder.setAuthEap(profile.getServerRootCaCert(), eapConfig);
                return;
            case 7:
                builder.setAuthPsk(profile.getPresharedKey());
                return;
            case 8:
                builder.setAuthDigitalSignature(profile.getServerRootCaCert(), profile.getUserCert(), profile.getRsaPrivateKey());
                return;
            default:
                throw new java.lang.IllegalArgumentException("Unknown auth method set");
        }
    }

    private static java.util.List<android.net.ipsec.ike.IkeSaProposal> getIkeSaProposals() {
        java.util.List<android.net.ipsec.ike.IkeSaProposal> proposals = new java.util.ArrayList<>();
        android.net.ipsec.ike.IkeSaProposal.Builder normalModeBuilder = new android.net.ipsec.ike.IkeSaProposal.Builder();
        normalModeBuilder.addEncryptionAlgorithm(13, 256);
        normalModeBuilder.addEncryptionAlgorithm(12, 256);
        normalModeBuilder.addEncryptionAlgorithm(13, 192);
        normalModeBuilder.addEncryptionAlgorithm(12, 192);
        normalModeBuilder.addEncryptionAlgorithm(13, 128);
        normalModeBuilder.addEncryptionAlgorithm(12, 128);
        normalModeBuilder.addIntegrityAlgorithm(14);
        normalModeBuilder.addIntegrityAlgorithm(13);
        normalModeBuilder.addIntegrityAlgorithm(12);
        normalModeBuilder.addIntegrityAlgorithm(5);
        normalModeBuilder.addIntegrityAlgorithm(8);
        android.net.ipsec.ike.IkeSaProposal.Builder aeadBuilder = new android.net.ipsec.ike.IkeSaProposal.Builder();
        aeadBuilder.addEncryptionAlgorithm(28, 0);
        aeadBuilder.addEncryptionAlgorithm(20, 256);
        aeadBuilder.addEncryptionAlgorithm(19, 256);
        aeadBuilder.addEncryptionAlgorithm(18, 256);
        aeadBuilder.addEncryptionAlgorithm(20, 192);
        aeadBuilder.addEncryptionAlgorithm(19, 192);
        aeadBuilder.addEncryptionAlgorithm(18, 192);
        aeadBuilder.addEncryptionAlgorithm(20, 128);
        aeadBuilder.addEncryptionAlgorithm(19, 128);
        aeadBuilder.addEncryptionAlgorithm(18, 128);
        for (android.net.ipsec.ike.IkeSaProposal.Builder builder : java.util.Arrays.asList(normalModeBuilder, aeadBuilder)) {
            builder.addDhGroup(16);
            builder.addDhGroup(31);
            builder.addDhGroup(15);
            builder.addDhGroup(14);
            builder.addPseudorandomFunction(7);
            builder.addPseudorandomFunction(6);
            builder.addPseudorandomFunction(5);
            builder.addPseudorandomFunction(4);
            builder.addPseudorandomFunction(8);
            builder.addPseudorandomFunction(2);
        }
        proposals.add(normalModeBuilder.build());
        proposals.add(aeadBuilder.build());
        return proposals;
    }

    private static java.util.List<android.net.ipsec.ike.ChildSaProposal> getChildSaProposals(java.util.List<java.lang.String> allowedAlgorithms) {
        java.util.List<android.net.ipsec.ike.ChildSaProposal> proposals = new java.util.ArrayList<>();
        java.util.List<java.lang.Integer> aesKeyLenOptions = java.util.Arrays.asList(256, 192, 128);
        if (android.net.Ikev2VpnProfile.hasNormalModeAlgorithms(allowedAlgorithms)) {
            android.net.ipsec.ike.ChildSaProposal.Builder normalModeBuilder = new android.net.ipsec.ike.ChildSaProposal.Builder();
            java.util.Iterator<java.lang.Integer> it = aesKeyLenOptions.iterator();
            while (it.hasNext()) {
                int len = it.next().intValue();
                if (allowedAlgorithms.contains("rfc3686(ctr(aes))")) {
                    normalModeBuilder.addEncryptionAlgorithm(13, len);
                }
                if (allowedAlgorithms.contains("cbc(aes)")) {
                    normalModeBuilder.addEncryptionAlgorithm(12, len);
                }
            }
            if (allowedAlgorithms.contains("hmac(sha512)")) {
                normalModeBuilder.addIntegrityAlgorithm(14);
            }
            if (allowedAlgorithms.contains("hmac(sha384)")) {
                normalModeBuilder.addIntegrityAlgorithm(13);
            }
            if (allowedAlgorithms.contains("hmac(sha256)")) {
                normalModeBuilder.addIntegrityAlgorithm(12);
            }
            if (allowedAlgorithms.contains("xcbc(aes)")) {
                normalModeBuilder.addIntegrityAlgorithm(5);
            }
            if (allowedAlgorithms.contains("cmac(aes)")) {
                normalModeBuilder.addIntegrityAlgorithm(8);
            }
            android.net.ipsec.ike.ChildSaProposal proposal = normalModeBuilder.build();
            if (proposal.getIntegrityAlgorithms().isEmpty()) {
                android.util.Log.wtf(TAG, "Missing integrity algorithm when buildling Child SA proposal");
            } else {
                proposals.add(normalModeBuilder.build());
            }
        }
        if (android.net.Ikev2VpnProfile.hasAeadAlgorithms(allowedAlgorithms)) {
            android.net.ipsec.ike.ChildSaProposal.Builder aeadBuilder = new android.net.ipsec.ike.ChildSaProposal.Builder();
            if (allowedAlgorithms.contains("rfc7539esp(chacha20,poly1305)")) {
                aeadBuilder.addEncryptionAlgorithm(28, 0);
            }
            if (allowedAlgorithms.contains("rfc4106(gcm(aes))")) {
                aeadBuilder.addEncryptionAlgorithm(20, 256);
                aeadBuilder.addEncryptionAlgorithm(19, 256);
                aeadBuilder.addEncryptionAlgorithm(18, 256);
                aeadBuilder.addEncryptionAlgorithm(20, 192);
                aeadBuilder.addEncryptionAlgorithm(19, 192);
                aeadBuilder.addEncryptionAlgorithm(18, 192);
                aeadBuilder.addEncryptionAlgorithm(20, 128);
                aeadBuilder.addEncryptionAlgorithm(19, 128);
                aeadBuilder.addEncryptionAlgorithm(18, 128);
            }
            proposals.add(aeadBuilder.build());
        }
        return proposals;
    }

    static class IkeSessionCallbackImpl implements android.net.ipsec.ike.IkeSessionCallback {
        private final com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback mCallback;
        private final java.lang.String mTag;
        private final int mToken;

        IkeSessionCallbackImpl(java.lang.String tag, com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback callback, int token) {
            this.mTag = tag;
            this.mCallback = callback;
            this.mToken = token;
        }

        @Override // android.net.ipsec.ike.IkeSessionCallback
        public void onOpened(android.net.ipsec.ike.IkeSessionConfiguration ikeSessionConfig) {
            android.util.Log.d(this.mTag, "IkeOpened for token " + this.mToken);
            this.mCallback.onIkeOpened(this.mToken, ikeSessionConfig);
        }

        @Override // android.net.ipsec.ike.IkeSessionCallback
        public void onClosed() {
            android.util.Log.d(this.mTag, "IkeClosed for token " + this.mToken);
            this.mCallback.onSessionLost(this.mToken, null);
        }

        public void onClosedExceptionally(android.net.ipsec.ike.exceptions.IkeException exception) {
            android.util.Log.d(this.mTag, "IkeClosedExceptionally for token " + this.mToken, exception);
            this.mCallback.onSessionLost(this.mToken, exception);
        }

        public void onError(android.net.ipsec.ike.exceptions.IkeProtocolException exception) {
            android.util.Log.d(this.mTag, "IkeError for token " + this.mToken, exception);
        }

        public void onIkeSessionConnectionInfoChanged(android.net.ipsec.ike.IkeSessionConnectionInfo connectionInfo) {
            android.util.Log.d(this.mTag, "onIkeSessionConnectionInfoChanged for token " + this.mToken);
            this.mCallback.onIkeConnectionInfoChanged(this.mToken, connectionInfo);
        }
    }

    static class ChildSessionCallbackImpl implements android.net.ipsec.ike.ChildSessionCallback {
        private final com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback mCallback;
        private final java.lang.String mTag;
        private final int mToken;

        ChildSessionCallbackImpl(java.lang.String tag, com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback callback, int token) {
            this.mTag = tag;
            this.mCallback = callback;
            this.mToken = token;
        }

        @Override // android.net.ipsec.ike.ChildSessionCallback
        public void onOpened(android.net.ipsec.ike.ChildSessionConfiguration childConfig) {
            android.util.Log.d(this.mTag, "ChildOpened for token " + this.mToken);
            this.mCallback.onChildOpened(this.mToken, childConfig);
        }

        @Override // android.net.ipsec.ike.ChildSessionCallback
        public void onClosed() {
            android.util.Log.d(this.mTag, "ChildClosed for token " + this.mToken);
            this.mCallback.onSessionLost(this.mToken, null);
        }

        public void onClosedExceptionally(android.net.ipsec.ike.exceptions.IkeException exception) {
            android.util.Log.d(this.mTag, "ChildClosedExceptionally for token " + this.mToken, exception);
            this.mCallback.onSessionLost(this.mToken, exception);
        }

        @Override // android.net.ipsec.ike.ChildSessionCallback
        public void onIpSecTransformCreated(android.net.IpSecTransform transform, int direction) {
            android.util.Log.d(this.mTag, "ChildTransformCreated; Direction: " + direction + "; token " + this.mToken);
            this.mCallback.onChildTransformCreated(this.mToken, transform, direction);
        }

        @Override // android.net.ipsec.ike.ChildSessionCallback
        public void onIpSecTransformDeleted(android.net.IpSecTransform transform, int direction) {
            android.util.Log.d(this.mTag, "ChildTransformDeleted; Direction: " + direction + "; for token " + this.mToken);
        }

        public void onIpSecTransformsMigrated(android.net.IpSecTransform inIpSecTransform, android.net.IpSecTransform outIpSecTransform) {
            android.util.Log.d(this.mTag, "ChildTransformsMigrated; token " + this.mToken);
            this.mCallback.onChildMigrated(this.mToken, inIpSecTransform, outIpSecTransform);
        }
    }

    static class Ikev2VpnNetworkCallback extends android.net.ConnectivityManager.NetworkCallback {
        private final com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback mCallback;
        private final java.util.concurrent.Executor mExecutor;
        private final java.lang.String mTag;

        Ikev2VpnNetworkCallback(java.lang.String tag, com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback callback, java.util.concurrent.Executor executor) {
            this.mTag = tag;
            this.mCallback = callback;
            this.mExecutor = executor;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(final android.net.Network network) {
            android.util.Log.d(this.mTag, "onAvailable called for network: " + network);
            try {
                this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.connectivity.VpnIkev2Utils$Ikev2VpnNetworkCallback$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onAvailable$0(network);
                    }
                });
            } catch (java.util.concurrent.RejectedExecutionException e) {
                android.util.Log.d(this.mTag, "The Ikev2VpnRunner has already shut down. ");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAvailable$0(android.net.Network network) {
            this.mCallback.onDefaultNetworkChanged(network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(android.net.Network network, final android.net.NetworkCapabilities networkCapabilities) {
            android.util.Log.d(this.mTag, "NC changed for net " + network + " : " + networkCapabilities);
            try {
                this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.connectivity.VpnIkev2Utils$Ikev2VpnNetworkCallback$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onCapabilitiesChanged$1(networkCapabilities);
                    }
                });
            } catch (java.util.concurrent.RejectedExecutionException e) {
                android.util.Log.d(this.mTag, "The Ikev2VpnRunner has already shut down. ");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCapabilitiesChanged$1(android.net.NetworkCapabilities networkCapabilities) {
            this.mCallback.onDefaultNetworkCapabilitiesChanged(networkCapabilities);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(android.net.Network network, final android.net.LinkProperties linkProperties) {
            android.util.Log.d(this.mTag, "LP changed for net " + network + " : " + linkProperties);
            try {
                this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.connectivity.VpnIkev2Utils$Ikev2VpnNetworkCallback$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onLinkPropertiesChanged$2(linkProperties);
                    }
                });
            } catch (java.util.concurrent.RejectedExecutionException e) {
                android.util.Log.d(this.mTag, "The Ikev2VpnRunner has already shut down. ");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLinkPropertiesChanged$2(android.net.LinkProperties linkProperties) {
            this.mCallback.onDefaultNetworkLinkPropertiesChanged(linkProperties);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(final android.net.Network network) {
            android.util.Log.d(this.mTag, "onLost called for network: " + network);
            try {
                this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.connectivity.VpnIkev2Utils$Ikev2VpnNetworkCallback$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onLost$3(network);
                    }
                });
            } catch (java.util.concurrent.RejectedExecutionException e) {
                android.util.Log.d(this.mTag, "The Ikev2VpnRunner has already shut down. ");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLost$3(android.net.Network network) {
            this.mCallback.onDefaultNetworkLost(network);
        }
    }

    private static android.net.ipsec.ike.IkeIdentification parseIkeIdentification(java.lang.String identityStr) {
        if (identityStr.contains("@")) {
            if (identityStr.startsWith("@#")) {
                java.lang.String hexStr = identityStr.substring(2);
                return new android.net.ipsec.ike.IkeKeyIdIdentification(com.android.internal.util.HexDump.hexStringToByteArray(hexStr));
            }
            if (identityStr.startsWith("@@")) {
                return new android.net.ipsec.ike.IkeRfc822AddrIdentification(identityStr.substring(2));
            }
            if (identityStr.startsWith("@")) {
                return new android.net.ipsec.ike.IkeFqdnIdentification(identityStr.substring(1));
            }
            return new android.net.ipsec.ike.IkeRfc822AddrIdentification(identityStr);
        }
        if (android.net.InetAddresses.isNumericAddress(identityStr)) {
            java.net.InetAddress addr = android.net.InetAddresses.parseNumericAddress(identityStr);
            if (addr instanceof java.net.Inet4Address) {
                return new android.net.ipsec.ike.IkeIpv4AddrIdentification((java.net.Inet4Address) addr);
            }
            if (addr instanceof java.net.Inet6Address) {
                return new android.net.ipsec.ike.IkeIpv6AddrIdentification((java.net.Inet6Address) addr);
            }
            throw new java.lang.IllegalArgumentException("IP version not supported");
        }
        if (identityStr.contains(":")) {
            return new android.net.ipsec.ike.IkeKeyIdIdentification(identityStr.getBytes());
        }
        return new android.net.ipsec.ike.IkeFqdnIdentification(identityStr);
    }

    static java.util.Collection<android.net.RouteInfo> getRoutesFromTrafficSelectors(java.util.List<android.net.ipsec.ike.IkeTrafficSelector> trafficSelectors) {
        java.util.HashSet<android.net.RouteInfo> routes = new java.util.HashSet<>();
        for (android.net.ipsec.ike.IkeTrafficSelector selector : trafficSelectors) {
            for (android.net.IpPrefix prefix : new com.android.net.module.util.IpRange(selector.startingAddress, selector.endingAddress).asIpPrefixes()) {
                routes.add(new android.net.RouteInfo(prefix, null, null, 1));
            }
        }
        return routes;
    }
}
