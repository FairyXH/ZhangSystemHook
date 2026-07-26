package android.net.shared;

/* JADX INFO: loaded from: classes.dex */
public class ProvisioningConfiguration {
    private static final int DEFAULT_TIMEOUT_MS = 18000;
    public static final int IPV6_ADDR_GEN_MODE_EUI64 = 0;
    public static final int IPV6_ADDR_GEN_MODE_STABLE_PRIVACY = 2;
    private static final java.lang.String TAG = "ProvisioningConfiguration";
    public static final int VERSION_ADDED_PROVISIONING_ENUM = 12;
    public android.net.apf.ApfCapabilities mApfCapabilities;
    public int mCreatorUid;
    public java.util.List<android.net.networkstack.aidl.dhcp.DhcpOption> mDhcpOptions;
    public java.lang.String mDisplayName;
    public boolean mEnablePreconnection;
    public int mHostnameSetting;
    public int mIPv4ProvisioningMode;
    public int mIPv6AddrGenMode;
    public int mIPv6ProvisioningMode;
    public android.net.shared.InitialConfiguration mInitialConfig;
    public android.net.shared.Layer2Information mLayer2Info;
    public android.net.Network mNetwork;
    public int mProvisioningTimeoutMs;
    public int mRequestedPreDhcpActionMs;
    public android.net.shared.ProvisioningConfiguration.ScanResultInfo mScanResultInfo;
    public android.net.StaticIpConfiguration mStaticIpConfig;
    public boolean mUniqueEui64AddressesOnly;
    public boolean mUsingIpReachabilityMonitor;
    public boolean mUsingMultinetworkPolicyTracker;

    public static class Builder {
        protected android.net.shared.ProvisioningConfiguration mConfig = new android.net.shared.ProvisioningConfiguration();

        public android.net.shared.ProvisioningConfiguration.Builder withoutIPv4() {
            this.mConfig.mIPv4ProvisioningMode = 0;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withoutIPv6() {
            this.mConfig.mIPv6ProvisioningMode = 0;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withoutMultinetworkPolicyTracker() {
            this.mConfig.mUsingMultinetworkPolicyTracker = false;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withoutIpReachabilityMonitor() {
            this.mConfig.mUsingIpReachabilityMonitor = false;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withPreDhcpAction() {
            this.mConfig.mRequestedPreDhcpActionMs = android.net.shared.ProvisioningConfiguration.DEFAULT_TIMEOUT_MS;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withPreDhcpAction(int dhcpActionTimeoutMs) {
            this.mConfig.mRequestedPreDhcpActionMs = dhcpActionTimeoutMs;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withPreconnection() {
            this.mConfig.mEnablePreconnection = true;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withInitialConfiguration(android.net.shared.InitialConfiguration initialConfig) {
            this.mConfig.mInitialConfig = initialConfig;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withStaticConfiguration(android.net.StaticIpConfiguration staticConfig) {
            this.mConfig.mIPv4ProvisioningMode = 1;
            this.mConfig.mStaticIpConfig = staticConfig;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withApfCapabilities(android.net.apf.ApfCapabilities apfCapabilities) {
            this.mConfig.mApfCapabilities = apfCapabilities;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withProvisioningTimeoutMs(int timeoutMs) {
            this.mConfig.mProvisioningTimeoutMs = timeoutMs;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withRandomMacAddress() {
            this.mConfig.mIPv6AddrGenMode = 0;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withStableMacAddress() {
            this.mConfig.mIPv6AddrGenMode = 2;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withNetwork(android.net.Network network) {
            this.mConfig.mNetwork = network;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withDisplayName(java.lang.String displayName) {
            this.mConfig.mDisplayName = displayName;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withCreatorUid(int creatoruid) {
            this.mConfig.mCreatorUid = creatoruid;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withScanResultInfo(android.net.shared.ProvisioningConfiguration.ScanResultInfo scanResultInfo) {
            this.mConfig.mScanResultInfo = scanResultInfo;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withLayer2Information(android.net.shared.Layer2Information layer2Info) {
            this.mConfig.mLayer2Info = layer2Info;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withDhcpOptions(java.util.List<android.net.networkstack.aidl.dhcp.DhcpOption> options) {
            this.mConfig.mDhcpOptions = options;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withIpv6LinkLocalOnly() {
            this.mConfig.mIPv6ProvisioningMode = 2;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withUniqueEui64AddressesOnly() {
            this.mConfig.mUniqueEui64AddressesOnly = true;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration.Builder withHostnameSetting(int setting) {
            this.mConfig.mHostnameSetting = setting;
            return this;
        }

        public android.net.shared.ProvisioningConfiguration build() {
            if (this.mConfig.mIPv6ProvisioningMode == 2 && this.mConfig.mIPv4ProvisioningMode != 0) {
                throw new java.lang.IllegalArgumentException("IPv4 must be disabled in IPv6 link-localonly mode.");
            }
            return new android.net.shared.ProvisioningConfiguration(this.mConfig);
        }
    }

    public static class ScanResultInfo {
        private final java.lang.String mBssid;
        private final java.util.List<android.net.shared.ProvisioningConfiguration.ScanResultInfo.InformationElement> mInformationElements;
        private final java.lang.String mSsid;

        public static class InformationElement {
            private final int mId;
            private final byte[] mPayload;

            public InformationElement(int id, java.nio.ByteBuffer payload) {
                this.mId = id;
                this.mPayload = android.net.shared.ProvisioningConfiguration.ScanResultInfo.convertToByteArray(payload.asReadOnlyBuffer());
            }

            public int getId() {
                return this.mId;
            }

            public java.nio.ByteBuffer getPayload() {
                return java.nio.ByteBuffer.wrap(this.mPayload).asReadOnlyBuffer();
            }

            public boolean equals(java.lang.Object o) {
                if (o == this) {
                    return true;
                }
                if (!(o instanceof android.net.shared.ProvisioningConfiguration.ScanResultInfo.InformationElement)) {
                    return false;
                }
                android.net.shared.ProvisioningConfiguration.ScanResultInfo.InformationElement other = (android.net.shared.ProvisioningConfiguration.ScanResultInfo.InformationElement) o;
                return this.mId == other.mId && java.util.Arrays.equals(this.mPayload, other.mPayload);
            }

            public int hashCode() {
                return java.util.Objects.hash(java.lang.Integer.valueOf(this.mId), java.lang.Integer.valueOf(java.util.Arrays.hashCode(this.mPayload)));
            }

            public java.lang.String toString() {
                return "ID: " + this.mId + ", " + java.util.Arrays.toString(this.mPayload);
            }

            public android.net.InformationElementParcelable toStableParcelable() {
                android.net.InformationElementParcelable p = new android.net.InformationElementParcelable();
                p.id = this.mId;
                p.payload = this.mPayload != null ? (byte[]) this.mPayload.clone() : null;
                return p;
            }

            public static android.net.shared.ProvisioningConfiguration.ScanResultInfo.InformationElement fromStableParcelable(android.net.InformationElementParcelable p) {
                if (p == null) {
                    return null;
                }
                return new android.net.shared.ProvisioningConfiguration.ScanResultInfo.InformationElement(p.id, java.nio.ByteBuffer.wrap((byte[]) p.payload.clone()).asReadOnlyBuffer());
            }
        }

        public ScanResultInfo(java.lang.String ssid, java.lang.String bssid, java.util.List<android.net.shared.ProvisioningConfiguration.ScanResultInfo.InformationElement> informationElements) {
            java.util.Objects.requireNonNull(ssid, "ssid must not be null.");
            java.util.Objects.requireNonNull(bssid, "bssid must not be null.");
            this.mSsid = ssid;
            this.mBssid = bssid;
            this.mInformationElements = java.util.Collections.unmodifiableList(new java.util.ArrayList(informationElements));
        }

        public java.lang.String getSsid() {
            return this.mSsid;
        }

        public java.lang.String getBssid() {
            return this.mBssid;
        }

        public java.util.List<android.net.shared.ProvisioningConfiguration.ScanResultInfo.InformationElement> getInformationElements() {
            return this.mInformationElements;
        }

        public java.lang.String toString() {
            java.lang.StringBuffer str = new java.lang.StringBuffer();
            str.append("SSID: ").append(this.mSsid);
            str.append(", BSSID: ").append(this.mBssid);
            str.append(", Information Elements: {");
            for (android.net.shared.ProvisioningConfiguration.ScanResultInfo.InformationElement ie : this.mInformationElements) {
                str.append("[").append(ie.toString()).append("]");
            }
            str.append("}");
            return str.toString();
        }

        public boolean equals(java.lang.Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof android.net.shared.ProvisioningConfiguration.ScanResultInfo)) {
                return false;
            }
            android.net.shared.ProvisioningConfiguration.ScanResultInfo other = (android.net.shared.ProvisioningConfiguration.ScanResultInfo) o;
            return java.util.Objects.equals(this.mSsid, other.mSsid) && java.util.Objects.equals(this.mBssid, other.mBssid) && this.mInformationElements.equals(other.mInformationElements);
        }

        public int hashCode() {
            return java.util.Objects.hash(this.mSsid, this.mBssid, this.mInformationElements);
        }

        public android.net.ScanResultInfoParcelable toStableParcelable() {
            android.net.ScanResultInfoParcelable p = new android.net.ScanResultInfoParcelable();
            p.ssid = this.mSsid;
            p.bssid = this.mBssid;
            p.informationElements = (android.net.InformationElementParcelable[]) android.net.shared.ParcelableUtil.toParcelableArray(this.mInformationElements, new java.util.function.Function() { // from class: android.net.shared.ProvisioningConfiguration$ScanResultInfo$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((android.net.shared.ProvisioningConfiguration.ScanResultInfo.InformationElement) obj).toStableParcelable();
                }
            }, android.net.InformationElementParcelable.class);
            return p;
        }

        public static android.net.shared.ProvisioningConfiguration.ScanResultInfo fromStableParcelable(android.net.ScanResultInfoParcelable p) {
            if (p == null) {
                return null;
            }
            java.util.List<android.net.shared.ProvisioningConfiguration.ScanResultInfo.InformationElement> ies = new java.util.ArrayList<>();
            ies.addAll(android.net.shared.ParcelableUtil.fromParcelableArray(p.informationElements, new java.util.function.Function() { // from class: android.net.shared.ProvisioningConfiguration$ScanResultInfo$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return android.net.shared.ProvisioningConfiguration.ScanResultInfo.InformationElement.fromStableParcelable((android.net.InformationElementParcelable) obj);
                }
            }));
            return new android.net.shared.ProvisioningConfiguration.ScanResultInfo(p.ssid, p.bssid, ies);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static byte[] convertToByteArray(java.nio.ByteBuffer buffer) {
            byte[] bytes = new byte[buffer.limit()];
            java.nio.ByteBuffer copy = buffer.asReadOnlyBuffer();
            try {
                try {
                    copy.position(0);
                    copy.get(bytes);
                    return bytes;
                } catch (java.nio.BufferUnderflowException e) {
                    android.util.Log.wtf(android.net.shared.ProvisioningConfiguration.TAG, "Buffer under flow exception should never happen.");
                    return bytes;
                }
            } catch (java.lang.Throwable th) {
                return bytes;
            }
        }
    }

    public ProvisioningConfiguration() {
        this.mUniqueEui64AddressesOnly = false;
        this.mEnablePreconnection = false;
        this.mUsingMultinetworkPolicyTracker = true;
        this.mUsingIpReachabilityMonitor = true;
        this.mProvisioningTimeoutMs = DEFAULT_TIMEOUT_MS;
        this.mIPv6AddrGenMode = 2;
        this.mNetwork = null;
        this.mDisplayName = null;
        this.mIPv4ProvisioningMode = 2;
        this.mIPv6ProvisioningMode = 1;
        this.mHostnameSetting = 0;
    }

    public ProvisioningConfiguration(android.net.shared.ProvisioningConfiguration other) {
        this.mUniqueEui64AddressesOnly = false;
        this.mEnablePreconnection = false;
        this.mUsingMultinetworkPolicyTracker = true;
        this.mUsingIpReachabilityMonitor = true;
        this.mProvisioningTimeoutMs = DEFAULT_TIMEOUT_MS;
        this.mIPv6AddrGenMode = 2;
        this.mNetwork = null;
        this.mDisplayName = null;
        this.mIPv4ProvisioningMode = 2;
        this.mIPv6ProvisioningMode = 1;
        this.mHostnameSetting = 0;
        this.mUniqueEui64AddressesOnly = other.mUniqueEui64AddressesOnly;
        this.mEnablePreconnection = other.mEnablePreconnection;
        this.mUsingMultinetworkPolicyTracker = other.mUsingMultinetworkPolicyTracker;
        this.mUsingIpReachabilityMonitor = other.mUsingIpReachabilityMonitor;
        this.mRequestedPreDhcpActionMs = other.mRequestedPreDhcpActionMs;
        this.mInitialConfig = android.net.shared.InitialConfiguration.copy(other.mInitialConfig);
        this.mStaticIpConfig = other.mStaticIpConfig != null ? new android.net.StaticIpConfiguration(other.mStaticIpConfig) : null;
        this.mApfCapabilities = other.mApfCapabilities;
        this.mProvisioningTimeoutMs = other.mProvisioningTimeoutMs;
        this.mIPv6AddrGenMode = other.mIPv6AddrGenMode;
        this.mNetwork = other.mNetwork;
        this.mDisplayName = other.mDisplayName;
        this.mCreatorUid = other.mCreatorUid;
        this.mScanResultInfo = other.mScanResultInfo;
        this.mLayer2Info = other.mLayer2Info;
        this.mDhcpOptions = other.mDhcpOptions;
        this.mIPv4ProvisioningMode = other.mIPv4ProvisioningMode;
        this.mIPv6ProvisioningMode = other.mIPv6ProvisioningMode;
        this.mHostnameSetting = other.mHostnameSetting;
    }

    public android.net.ProvisioningConfigurationParcelable toStableParcelable() {
        android.net.StaticIpConfiguration staticIpConfiguration;
        android.net.ProvisioningConfigurationParcelable p = new android.net.ProvisioningConfigurationParcelable();
        p.enableIPv4 = this.mIPv4ProvisioningMode != 0;
        p.ipv4ProvisioningMode = this.mIPv4ProvisioningMode;
        p.enableIPv6 = this.mIPv6ProvisioningMode != 0;
        p.ipv6ProvisioningMode = this.mIPv6ProvisioningMode;
        p.uniqueEui64AddressesOnly = this.mUniqueEui64AddressesOnly;
        p.enablePreconnection = this.mEnablePreconnection;
        p.usingMultinetworkPolicyTracker = this.mUsingMultinetworkPolicyTracker;
        p.usingIpReachabilityMonitor = this.mUsingIpReachabilityMonitor;
        p.requestedPreDhcpActionMs = this.mRequestedPreDhcpActionMs;
        p.initialConfig = this.mInitialConfig == null ? null : this.mInitialConfig.toStableParcelable();
        if (this.mStaticIpConfig == null) {
            staticIpConfiguration = null;
        } else {
            staticIpConfiguration = new android.net.StaticIpConfiguration(this.mStaticIpConfig);
        }
        p.staticIpConfig = staticIpConfiguration;
        p.apfCapabilities = this.mApfCapabilities;
        p.provisioningTimeoutMs = this.mProvisioningTimeoutMs;
        p.ipv6AddrGenMode = this.mIPv6AddrGenMode;
        p.network = this.mNetwork;
        p.displayName = this.mDisplayName;
        p.creatorUid = this.mCreatorUid;
        p.scanResultInfo = this.mScanResultInfo == null ? null : this.mScanResultInfo.toStableParcelable();
        p.layer2Info = this.mLayer2Info == null ? null : this.mLayer2Info.toStableParcelable();
        p.options = this.mDhcpOptions != null ? new java.util.ArrayList(this.mDhcpOptions) : null;
        p.hostnameSetting = this.mHostnameSetting;
        return p;
    }

    public static android.net.shared.ProvisioningConfiguration fromStableParcelable(android.net.ProvisioningConfigurationParcelable provisioningConfigurationParcelable, int i) {
        android.net.StaticIpConfiguration staticIpConfiguration;
        if (provisioningConfigurationParcelable == null) {
            return null;
        }
        android.net.shared.ProvisioningConfiguration provisioningConfiguration = new android.net.shared.ProvisioningConfiguration();
        provisioningConfiguration.mUniqueEui64AddressesOnly = provisioningConfigurationParcelable.uniqueEui64AddressesOnly;
        provisioningConfiguration.mEnablePreconnection = provisioningConfigurationParcelable.enablePreconnection;
        provisioningConfiguration.mUsingMultinetworkPolicyTracker = provisioningConfigurationParcelable.usingMultinetworkPolicyTracker;
        provisioningConfiguration.mUsingIpReachabilityMonitor = provisioningConfigurationParcelable.usingIpReachabilityMonitor;
        provisioningConfiguration.mRequestedPreDhcpActionMs = provisioningConfigurationParcelable.requestedPreDhcpActionMs;
        provisioningConfiguration.mInitialConfig = android.net.shared.InitialConfiguration.fromStableParcelable(provisioningConfigurationParcelable.initialConfig);
        if (provisioningConfigurationParcelable.staticIpConfig == null) {
            staticIpConfiguration = null;
        } else {
            staticIpConfiguration = new android.net.StaticIpConfiguration(provisioningConfigurationParcelable.staticIpConfig);
        }
        provisioningConfiguration.mStaticIpConfig = staticIpConfiguration;
        provisioningConfiguration.mApfCapabilities = provisioningConfigurationParcelable.apfCapabilities;
        provisioningConfiguration.mProvisioningTimeoutMs = provisioningConfigurationParcelable.provisioningTimeoutMs;
        provisioningConfiguration.mIPv6AddrGenMode = provisioningConfigurationParcelable.ipv6AddrGenMode;
        provisioningConfiguration.mNetwork = provisioningConfigurationParcelable.network;
        provisioningConfiguration.mDisplayName = provisioningConfigurationParcelable.displayName;
        provisioningConfiguration.mCreatorUid = provisioningConfigurationParcelable.creatorUid;
        provisioningConfiguration.mScanResultInfo = android.net.shared.ProvisioningConfiguration.ScanResultInfo.fromStableParcelable(provisioningConfigurationParcelable.scanResultInfo);
        provisioningConfiguration.mLayer2Info = android.net.shared.Layer2Information.fromStableParcelable(provisioningConfigurationParcelable.layer2Info);
        provisioningConfiguration.mDhcpOptions = provisioningConfigurationParcelable.options != null ? new java.util.ArrayList(provisioningConfigurationParcelable.options) : null;
        if (i < 12) {
            provisioningConfiguration.mIPv4ProvisioningMode = provisioningConfigurationParcelable.enableIPv4 ? 2 : 0;
            provisioningConfiguration.mIPv6ProvisioningMode = provisioningConfigurationParcelable.enableIPv6 ? 1 : 0;
        } else {
            provisioningConfiguration.mIPv4ProvisioningMode = provisioningConfigurationParcelable.ipv4ProvisioningMode;
            provisioningConfiguration.mIPv6ProvisioningMode = provisioningConfigurationParcelable.ipv6ProvisioningMode;
        }
        provisioningConfiguration.mHostnameSetting = provisioningConfigurationParcelable.hostnameSetting;
        return provisioningConfiguration;
    }

    static java.lang.String ipv4ProvisioningModeToString(int mode) {
        switch (mode) {
            case 0:
                return com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED;
            case 1:
                return "static";
            case 2:
                return "dhcp";
            default:
                return "unknown";
        }
    }

    static java.lang.String ipv6ProvisioningModeToString(int mode) {
        switch (mode) {
            case 0:
                return com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED;
            case 1:
                return "slaac";
            case 2:
                return "link-local";
            default:
                return "unknown";
        }
    }

    public java.lang.String toString() {
        java.lang.String ipv4ProvisioningMode = ipv4ProvisioningModeToString(this.mIPv4ProvisioningMode);
        java.lang.String ipv6ProvisioningMode = ipv6ProvisioningModeToString(this.mIPv6ProvisioningMode);
        return new java.util.StringJoiner(", ", getClass().getSimpleName() + "{", "}").add("mUniqueEui64AddressesOnly: " + this.mUniqueEui64AddressesOnly).add("mEnablePreconnection: " + this.mEnablePreconnection).add("mUsingMultinetworkPolicyTracker: " + this.mUsingMultinetworkPolicyTracker).add("mUsingIpReachabilityMonitor: " + this.mUsingIpReachabilityMonitor).add("mRequestedPreDhcpActionMs: " + this.mRequestedPreDhcpActionMs).add("mInitialConfig: " + this.mInitialConfig).add("mStaticIpConfig: " + this.mStaticIpConfig).add("mApfCapabilities: " + this.mApfCapabilities).add("mProvisioningTimeoutMs: " + this.mProvisioningTimeoutMs).add("mIPv6AddrGenMode: " + this.mIPv6AddrGenMode).add("mNetwork: " + this.mNetwork).add("mDisplayName: " + this.mDisplayName).add("mCreatorUid:" + this.mCreatorUid).add("mScanResultInfo: " + this.mScanResultInfo).add("mLayer2Info: " + this.mLayer2Info).add("mDhcpOptions: " + this.mDhcpOptions).add("mIPv4ProvisioningMode: " + ipv4ProvisioningMode).add("mIPv6ProvisioningMode: " + ipv6ProvisioningMode).add("mHostnameSetting: " + this.mHostnameSetting).toString();
    }

    private static boolean dhcpOptionEquals(android.net.networkstack.aidl.dhcp.DhcpOption obj1, android.net.networkstack.aidl.dhcp.DhcpOption obj2) {
        if (obj1 == obj2) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        if (obj1.type == obj2.type && java.util.Arrays.equals(obj1.value, obj2.value)) {
            return true;
        }
        return false;
    }

    private static boolean dhcpOptionListEquals(java.util.List<android.net.networkstack.aidl.dhcp.DhcpOption> l1, java.util.List<android.net.networkstack.aidl.dhcp.DhcpOption> l2) {
        if (l1 == l2) {
            return true;
        }
        if (l1 == null || l2 == null || l1.size() != l2.size()) {
            return false;
        }
        for (int i = 0; i < l1.size(); i++) {
            if (!dhcpOptionEquals(l1.get(i), l2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof android.net.shared.ProvisioningConfiguration)) {
            return false;
        }
        android.net.shared.ProvisioningConfiguration other = (android.net.shared.ProvisioningConfiguration) obj;
        return this.mUniqueEui64AddressesOnly == other.mUniqueEui64AddressesOnly && this.mEnablePreconnection == other.mEnablePreconnection && this.mUsingMultinetworkPolicyTracker == other.mUsingMultinetworkPolicyTracker && this.mUsingIpReachabilityMonitor == other.mUsingIpReachabilityMonitor && this.mRequestedPreDhcpActionMs == other.mRequestedPreDhcpActionMs && java.util.Objects.equals(this.mInitialConfig, other.mInitialConfig) && java.util.Objects.equals(this.mStaticIpConfig, other.mStaticIpConfig) && java.util.Objects.equals(this.mApfCapabilities, other.mApfCapabilities) && this.mProvisioningTimeoutMs == other.mProvisioningTimeoutMs && this.mIPv6AddrGenMode == other.mIPv6AddrGenMode && java.util.Objects.equals(this.mNetwork, other.mNetwork) && java.util.Objects.equals(this.mDisplayName, other.mDisplayName) && java.util.Objects.equals(this.mScanResultInfo, other.mScanResultInfo) && java.util.Objects.equals(this.mLayer2Info, other.mLayer2Info) && dhcpOptionListEquals(this.mDhcpOptions, other.mDhcpOptions) && this.mIPv4ProvisioningMode == other.mIPv4ProvisioningMode && this.mIPv6ProvisioningMode == other.mIPv6ProvisioningMode && this.mCreatorUid == other.mCreatorUid && this.mHostnameSetting == other.mHostnameSetting;
    }

    public boolean isValid() {
        return this.mInitialConfig == null || this.mInitialConfig.isValid();
    }
}
