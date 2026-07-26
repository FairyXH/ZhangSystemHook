package android.net.shared;

/* JADX INFO: loaded from: classes.dex */
public class Layer2Information {
    public final android.net.MacAddress mBssid;
    public final java.lang.String mCluster;
    public final java.lang.String mL2Key;

    public Layer2Information(java.lang.String l2Key, java.lang.String cluster, android.net.MacAddress bssid) {
        this.mL2Key = l2Key;
        this.mCluster = cluster;
        this.mBssid = bssid;
    }

    public java.lang.String toString() {
        java.lang.StringBuffer str = new java.lang.StringBuffer();
        str.append("L2Key: ").append(this.mL2Key);
        str.append(", Cluster: ").append(this.mCluster);
        str.append(", bssid: ").append(this.mBssid);
        return str.toString();
    }

    public android.net.Layer2InformationParcelable toStableParcelable() {
        android.net.Layer2InformationParcelable p = new android.net.Layer2InformationParcelable();
        p.l2Key = this.mL2Key;
        p.cluster = this.mCluster;
        p.bssid = this.mBssid;
        return p;
    }

    public static android.net.shared.Layer2Information fromStableParcelable(android.net.Layer2InformationParcelable p) {
        if (p == null) {
            return null;
        }
        return new android.net.shared.Layer2Information(p.l2Key, p.cluster, p.bssid);
    }

    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof android.net.shared.Layer2Information)) {
            return false;
        }
        android.net.shared.Layer2Information other = (android.net.shared.Layer2Information) obj;
        return java.util.Objects.equals(this.mL2Key, other.mL2Key) && java.util.Objects.equals(this.mCluster, other.mCluster) && java.util.Objects.equals(this.mBssid, other.mBssid);
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mL2Key, this.mCluster, this.mBssid);
    }
}
