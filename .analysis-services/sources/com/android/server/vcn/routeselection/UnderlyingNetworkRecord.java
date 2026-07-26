package com.android.server.vcn.routeselection;

/* JADX INFO: loaded from: classes3.dex */
public class UnderlyingNetworkRecord {
    public final boolean isBlocked;
    public final android.net.LinkProperties linkProperties;
    public final android.net.Network network;
    public final android.net.NetworkCapabilities networkCapabilities;

    public UnderlyingNetworkRecord(android.net.Network network, android.net.NetworkCapabilities networkCapabilities, android.net.LinkProperties linkProperties, boolean isBlocked) {
        this.network = network;
        this.networkCapabilities = networkCapabilities;
        this.linkProperties = linkProperties;
        this.isBlocked = isBlocked;
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.vcn.routeselection.UnderlyingNetworkRecord)) {
            return false;
        }
        com.android.server.vcn.routeselection.UnderlyingNetworkRecord that = (com.android.server.vcn.routeselection.UnderlyingNetworkRecord) o;
        return this.network.equals(that.network) && this.networkCapabilities.equals(that.networkCapabilities) && this.linkProperties.equals(that.linkProperties) && this.isBlocked == that.isBlocked;
    }

    public int hashCode() {
        return java.util.Objects.hash(this.network, this.networkCapabilities, this.linkProperties, java.lang.Boolean.valueOf(this.isBlocked));
    }

    public static boolean isSameNetwork(com.android.server.vcn.routeselection.UnderlyingNetworkRecord leftRecord, com.android.server.vcn.routeselection.UnderlyingNetworkRecord rightRecord) {
        android.net.Network left = leftRecord == null ? null : leftRecord.network;
        android.net.Network right = rightRecord != null ? rightRecord.network : null;
        return java.util.Objects.equals(left, right);
    }

    void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("UnderlyingNetworkRecord:");
        pw.increaseIndent();
        pw.println("mNetwork: " + this.network);
        pw.println("mNetworkCapabilities: " + this.networkCapabilities);
        pw.println("mLinkProperties: " + this.linkProperties);
        pw.decreaseIndent();
    }

    static class Builder {
        boolean mIsBlocked;
        private android.net.LinkProperties mLinkProperties;
        private final android.net.Network mNetwork;
        private android.net.NetworkCapabilities mNetworkCapabilities;
        boolean mWasIsBlockedSet;

        Builder(android.net.Network network) {
            this.mNetwork = network;
        }

        android.net.Network getNetwork() {
            return this.mNetwork;
        }

        void setNetworkCapabilities(android.net.NetworkCapabilities networkCapabilities) {
            this.mNetworkCapabilities = networkCapabilities;
        }

        android.net.NetworkCapabilities getNetworkCapabilities() {
            return this.mNetworkCapabilities;
        }

        void setLinkProperties(android.net.LinkProperties linkProperties) {
            this.mLinkProperties = linkProperties;
        }

        void setIsBlocked(boolean isBlocked) {
            this.mIsBlocked = isBlocked;
            this.mWasIsBlockedSet = true;
        }

        boolean isValid() {
            return (this.mNetworkCapabilities == null || this.mLinkProperties == null || !this.mWasIsBlockedSet) ? false : true;
        }

        com.android.server.vcn.routeselection.UnderlyingNetworkRecord build() {
            if (!isValid()) {
                throw new java.lang.IllegalArgumentException("Called build before UnderlyingNetworkRecord was valid");
            }
            return new com.android.server.vcn.routeselection.UnderlyingNetworkRecord(this.mNetwork, this.mNetworkCapabilities, this.mLinkProperties, this.mIsBlocked);
        }
    }
}
