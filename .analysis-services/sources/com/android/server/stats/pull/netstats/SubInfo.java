package com.android.server.stats.pull.netstats;

/* JADX INFO: loaded from: classes3.dex */
public final class SubInfo {
    public final int carrierId;
    public final boolean isOpportunistic;
    public final java.lang.String mcc;
    public final java.lang.String mnc;
    public final int subId;
    public final java.lang.String subscriberId;

    public SubInfo(int subId, int carrierId, java.lang.String mcc, java.lang.String mnc, java.lang.String subscriberId, boolean isOpportunistic) {
        this.subId = subId;
        this.carrierId = carrierId;
        this.mcc = mcc;
        this.mnc = mnc;
        this.subscriberId = subscriberId;
        this.isOpportunistic = isOpportunistic;
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.android.server.stats.pull.netstats.SubInfo other = (com.android.server.stats.pull.netstats.SubInfo) o;
        if (this.subId == other.subId && this.carrierId == other.carrierId && this.isOpportunistic == other.isOpportunistic && this.mcc.equals(other.mcc) && this.mnc.equals(other.mnc) && this.subscriberId.equals(other.subscriberId)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(this.subId), this.mcc, this.mnc, java.lang.Integer.valueOf(this.carrierId), this.subscriberId, java.lang.Boolean.valueOf(this.isOpportunistic));
    }
}
