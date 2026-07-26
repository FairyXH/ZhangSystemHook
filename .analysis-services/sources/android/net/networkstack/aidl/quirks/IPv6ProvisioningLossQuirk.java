package android.net.networkstack.aidl.quirks;

/* JADX INFO: loaded from: classes.dex */
public final class IPv6ProvisioningLossQuirk {
    public final int mDetectionCount;
    public final long mQuirkExpiry;

    public IPv6ProvisioningLossQuirk(int count, long expiry) {
        this.mDetectionCount = count;
        this.mQuirkExpiry = expiry;
    }

    public android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable toStableParcelable() {
        android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable p = new android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable();
        p.detectionCount = this.mDetectionCount;
        p.quirkExpiry = this.mQuirkExpiry;
        return p;
    }

    public static android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirk fromStableParcelable(android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirkParcelable p) {
        if (p == null) {
            return null;
        }
        return new android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirk(p.detectionCount, p.quirkExpiry);
    }

    public boolean equals(java.lang.Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirk other = (android.net.networkstack.aidl.quirks.IPv6ProvisioningLossQuirk) obj;
        return this.mDetectionCount == other.mDetectionCount && this.mQuirkExpiry == other.mQuirkExpiry;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(this.mDetectionCount), java.lang.Long.valueOf(this.mQuirkExpiry));
    }

    public java.lang.String toString() {
        java.lang.StringBuffer str = new java.lang.StringBuffer();
        str.append("detection count: ").append(this.mDetectionCount);
        str.append(", quirk expiry: ").append(this.mQuirkExpiry);
        return str.toString();
    }
}
