package android.net.ipmemorystore;

/* JADX INFO: loaded from: classes.dex */
public class NetworkAttributes {
    private static final boolean DBG = true;
    private static final float NULL_MATCH_WEIGHT = 0.25f;
    public static final float TOTAL_WEIGHT = 850.0f;
    private static final float TOTAL_WEIGHT_CUTOFF = 520.0f;
    private static final float WEIGHT_ASSIGNEDV4ADDR = 300.0f;
    private static final float WEIGHT_ASSIGNEDV4ADDREXPIRY = 0.0f;
    private static final float WEIGHT_CLUSTER = 300.0f;
    private static final float WEIGHT_DNSADDRESSES = 200.0f;
    private static final float WEIGHT_MTU = 50.0f;
    private static final float WEIGHT_V6PROVLOSSQUIRK = 0.0f;
    public final java.net.Inet4Address assignedV4Address;
    public final java.lang.Long assignedV4AddressExpiry;
    public final java.lang.String cluster;
    public final java.util.List<java.net.InetAddress> dnsAddresses;
    public final android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirk ipv6ProvisioningLossQuirk;
    public final java.lang.Integer mtu;

    public NetworkAttributes(java.net.Inet4Address assignedV4Address, java.lang.Long assignedV4AddressExpiry, java.lang.String cluster, java.util.List<java.net.InetAddress> dnsAddresses, java.lang.Integer mtu, android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirk ipv6ProvisioningLossQuirk) {
        if (mtu != null && mtu.intValue() < 0) {
            throw new java.lang.IllegalArgumentException("MTU can't be negative");
        }
        if (assignedV4AddressExpiry != null && assignedV4AddressExpiry.longValue() <= 0) {
            throw new java.lang.IllegalArgumentException("lease expiry can't be negative or zero");
        }
        this.assignedV4Address = assignedV4Address;
        this.assignedV4AddressExpiry = assignedV4AddressExpiry;
        this.cluster = cluster;
        this.dnsAddresses = dnsAddresses == null ? null : java.util.Collections.unmodifiableList(new java.util.ArrayList(dnsAddresses));
        this.mtu = mtu;
        this.ipv6ProvisioningLossQuirk = ipv6ProvisioningLossQuirk;
    }

    public NetworkAttributes(android.net.ipmemorystore.NetworkAttributesParcelable parcelable) {
        this((java.net.Inet4Address) getByAddressOrNull(parcelable.assignedV4Address), parcelable.assignedV4AddressExpiry > 0 ? java.lang.Long.valueOf(parcelable.assignedV4AddressExpiry) : null, parcelable.cluster, blobArrayToInetAddressList(parcelable.dnsAddresses), parcelable.mtu >= 0 ? java.lang.Integer.valueOf(parcelable.mtu) : null, android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirk.fromStableParcelable(parcelable.ipv6ProvisioningLossQuirk));
    }

    private static java.net.InetAddress getByAddressOrNull(byte[] address) {
        if (address == null) {
            return null;
        }
        try {
            return java.net.InetAddress.getByAddress(address);
        } catch (java.net.UnknownHostException e) {
            return null;
        }
    }

    private static java.util.List<java.net.InetAddress> blobArrayToInetAddressList(android.net.ipmemorystore.Blob[] blobs) {
        if (blobs == null) {
            return null;
        }
        java.util.ArrayList<java.net.InetAddress> list = new java.util.ArrayList<>(blobs.length);
        for (android.net.ipmemorystore.Blob b : blobs) {
            java.net.InetAddress addr = getByAddressOrNull(b.data);
            if (addr != null) {
                list.add(addr);
            }
        }
        return list;
    }

    private static android.net.ipmemorystore.Blob[] inetAddressListToBlobArray(java.util.List<java.net.InetAddress> addresses) {
        if (addresses == null) {
            return null;
        }
        java.util.ArrayList<android.net.ipmemorystore.Blob> blobs = new java.util.ArrayList<>();
        for (int i = 0; i < addresses.size(); i++) {
            java.net.InetAddress addr = addresses.get(i);
            if (addr != null) {
                android.net.ipmemorystore.Blob b = new android.net.ipmemorystore.Blob();
                b.data = addr.getAddress();
                blobs.add(b);
            }
        }
        return (android.net.ipmemorystore.Blob[]) blobs.toArray(new android.net.ipmemorystore.Blob[0]);
    }

    public android.net.ipmemorystore.NetworkAttributesParcelable toParcelable() {
        android.net.ipmemorystore.NetworkAttributesParcelable parcelable = new android.net.ipmemorystore.NetworkAttributesParcelable();
        parcelable.assignedV4Address = this.assignedV4Address == null ? null : this.assignedV4Address.getAddress();
        parcelable.assignedV4AddressExpiry = this.assignedV4AddressExpiry == null ? 0L : this.assignedV4AddressExpiry.longValue();
        parcelable.cluster = this.cluster;
        parcelable.dnsAddresses = inetAddressListToBlobArray(this.dnsAddresses);
        parcelable.mtu = this.mtu == null ? -1 : this.mtu.intValue();
        parcelable.ipv6ProvisioningLossQuirk = this.ipv6ProvisioningLossQuirk != null ? this.ipv6ProvisioningLossQuirk.toStableParcelable() : null;
        return parcelable;
    }

    private float samenessContribution(float weight, java.lang.Object o1, java.lang.Object o2) {
        return o1 == null ? o2 == null ? NULL_MATCH_WEIGHT * weight : WEIGHT_ASSIGNEDV4ADDREXPIRY : java.util.Objects.equals(o1, o2) ? weight : WEIGHT_ASSIGNEDV4ADDREXPIRY;
    }

    public float getNetworkGroupSamenessConfidence(android.net.ipmemorystore.NetworkAttributes o) {
        float samenessScore = samenessContribution(300.0f, this.assignedV4Address, o.assignedV4Address) + samenessContribution(WEIGHT_ASSIGNEDV4ADDREXPIRY, this.assignedV4AddressExpiry, o.assignedV4AddressExpiry) + samenessContribution(300.0f, this.cluster, o.cluster) + samenessContribution(WEIGHT_DNSADDRESSES, this.dnsAddresses, o.dnsAddresses) + samenessContribution(WEIGHT_MTU, this.mtu, o.mtu) + samenessContribution(WEIGHT_ASSIGNEDV4ADDREXPIRY, this.ipv6ProvisioningLossQuirk, o.ipv6ProvisioningLossQuirk);
        if (samenessScore < TOTAL_WEIGHT_CUTOFF) {
            return samenessScore / 1040.0f;
        }
        return (((samenessScore - TOTAL_WEIGHT_CUTOFF) / 330.0f) / 2.0f) + 0.5f;
    }

    public static class Builder {
        private java.net.Inet4Address mAssignedAddress;
        private java.lang.Long mAssignedAddressExpiry;
        private java.lang.String mCluster;
        private java.util.List<java.net.InetAddress> mDnsAddresses;
        private android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirk mIpv6ProvLossQuirk;
        private java.lang.Integer mMtu;

        public Builder() {
        }

        public Builder(android.net.ipmemorystore.NetworkAttributes attributes) {
            this.mAssignedAddress = attributes.assignedV4Address;
            this.mAssignedAddressExpiry = attributes.assignedV4AddressExpiry;
            this.mCluster = attributes.cluster;
            this.mDnsAddresses = new java.util.ArrayList(attributes.dnsAddresses);
            this.mMtu = attributes.mtu;
            this.mIpv6ProvLossQuirk = attributes.ipv6ProvisioningLossQuirk;
        }

        public android.net.ipmemorystore.NetworkAttributes.Builder setAssignedV4Address(java.net.Inet4Address assignedV4Address) {
            this.mAssignedAddress = assignedV4Address;
            return this;
        }

        public android.net.ipmemorystore.NetworkAttributes.Builder setAssignedV4AddressExpiry(java.lang.Long assignedV4AddressExpiry) {
            if (assignedV4AddressExpiry != null && assignedV4AddressExpiry.longValue() <= 0) {
                throw new java.lang.IllegalArgumentException("lease expiry can't be negative or zero");
            }
            this.mAssignedAddressExpiry = assignedV4AddressExpiry;
            return this;
        }

        public android.net.ipmemorystore.NetworkAttributes.Builder setCluster(java.lang.String cluster) {
            this.mCluster = cluster;
            return this;
        }

        public android.net.ipmemorystore.NetworkAttributes.Builder setDnsAddresses(java.util.List<java.net.InetAddress> dnsAddresses) {
            if (dnsAddresses != null) {
                for (java.net.InetAddress address : dnsAddresses) {
                    if (address == null) {
                        throw new java.lang.IllegalArgumentException("Null DNS address");
                    }
                }
            }
            this.mDnsAddresses = dnsAddresses;
            return this;
        }

        public android.net.ipmemorystore.NetworkAttributes.Builder setMtu(java.lang.Integer mtu) {
            if (mtu != null && mtu.intValue() < 0) {
                throw new java.lang.IllegalArgumentException("MTU can't be negative");
            }
            this.mMtu = mtu;
            return this;
        }

        public android.net.ipmemorystore.NetworkAttributes.Builder setIpv6ProvLossQuirk(android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirk quirk) {
            this.mIpv6ProvLossQuirk = quirk;
            return this;
        }

        public android.net.ipmemorystore.NetworkAttributes build() {
            return new android.net.ipmemorystore.NetworkAttributes(this.mAssignedAddress, this.mAssignedAddressExpiry, this.mCluster, this.mDnsAddresses, this.mMtu, this.mIpv6ProvLossQuirk);
        }
    }

    public boolean isEmpty() {
        return this.assignedV4Address == null && this.assignedV4AddressExpiry == null && this.cluster == null && this.dnsAddresses == null && this.mtu == null && this.ipv6ProvisioningLossQuirk == null;
    }

    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.net.ipmemorystore.NetworkAttributes)) {
            return false;
        }
        android.net.ipmemorystore.NetworkAttributes other = (android.net.ipmemorystore.NetworkAttributes) o;
        return java.util.Objects.equals(this.assignedV4Address, other.assignedV4Address) && java.util.Objects.equals(this.assignedV4AddressExpiry, other.assignedV4AddressExpiry) && java.util.Objects.equals(this.cluster, other.cluster) && java.util.Objects.equals(this.dnsAddresses, other.dnsAddresses) && java.util.Objects.equals(this.mtu, other.mtu) && java.util.Objects.equals(this.ipv6ProvisioningLossQuirk, other.ipv6ProvisioningLossQuirk);
    }

    public int hashCode() {
        return java.util.Objects.hash(this.assignedV4Address, this.assignedV4AddressExpiry, this.cluster, this.dnsAddresses, this.mtu, this.ipv6ProvisioningLossQuirk);
    }

    public java.lang.String toString() {
        java.util.StringJoiner resultJoiner = new java.util.StringJoiner(" ", "{", "}");
        java.util.ArrayList<java.lang.String> nullFields = new java.util.ArrayList<>();
        if (this.assignedV4Address != null) {
            resultJoiner.add("assignedV4Addr :");
            resultJoiner.add(this.assignedV4Address.toString());
        } else {
            nullFields.add("assignedV4Addr");
        }
        if (this.assignedV4AddressExpiry != null) {
            resultJoiner.add("assignedV4AddressExpiry :");
            resultJoiner.add(this.assignedV4AddressExpiry.toString());
        } else {
            nullFields.add("assignedV4AddressExpiry");
        }
        if (this.cluster != null) {
            resultJoiner.add("cluster :");
            resultJoiner.add(this.cluster);
        } else {
            nullFields.add("cluster");
        }
        if (this.dnsAddresses != null) {
            resultJoiner.add("dnsAddr : [");
            for (java.net.InetAddress addr : this.dnsAddresses) {
                resultJoiner.add(addr.getHostAddress());
            }
            resultJoiner.add("]");
        } else {
            nullFields.add("dnsAddr");
        }
        if (this.mtu != null) {
            resultJoiner.add("mtu :");
            resultJoiner.add(this.mtu.toString());
        } else {
            nullFields.add("mtu");
        }
        if (this.ipv6ProvisioningLossQuirk != null) {
            resultJoiner.add("ipv6ProvisioningLossQuirk : [");
            resultJoiner.add(this.ipv6ProvisioningLossQuirk.toString());
            resultJoiner.add("]");
        } else {
            nullFields.add("ipv6ProvisioningLossQuirk");
        }
        if (!nullFields.isEmpty()) {
            resultJoiner.add("; Null fields : [");
            for (java.lang.String field : nullFields) {
                resultJoiner.add(field);
            }
            resultJoiner.add("]");
        }
        return resultJoiner.toString();
    }
}
