package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
class RequestedRefreshRateVote implements com.android.server.display.mode.Vote {
    final float mRefreshRate;

    RequestedRefreshRateVote(float refreshRate) {
        this.mRefreshRate = refreshRate;
    }

    @Override // com.android.server.display.mode.Vote
    public void updateSummary(com.android.server.display.mode.VoteSummary summary) {
        summary.requestedRefreshRates.add(java.lang.Float.valueOf(this.mRefreshRate));
    }

    public java.lang.String toString() {
        return "RequestedRefreshRateVote{ refreshRate=" + this.mRefreshRate + " }";
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.display.mode.RequestedRefreshRateVote)) {
            return false;
        }
        com.android.server.display.mode.RequestedRefreshRateVote that = (com.android.server.display.mode.RequestedRefreshRateVote) o;
        return java.lang.Float.compare(this.mRefreshRate, that.mRefreshRate) == 0;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Float.valueOf(this.mRefreshRate));
    }
}
