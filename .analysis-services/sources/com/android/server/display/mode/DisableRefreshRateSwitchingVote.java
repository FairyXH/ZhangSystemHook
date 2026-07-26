package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
class DisableRefreshRateSwitchingVote implements com.android.server.display.mode.Vote {
    final boolean mDisableRefreshRateSwitching;

    DisableRefreshRateSwitchingVote(boolean disableRefreshRateSwitching) {
        this.mDisableRefreshRateSwitching = disableRefreshRateSwitching;
    }

    @Override // com.android.server.display.mode.Vote
    public void updateSummary(com.android.server.display.mode.VoteSummary summary) {
        summary.disableRefreshRateSwitching = summary.disableRefreshRateSwitching || this.mDisableRefreshRateSwitching;
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.display.mode.DisableRefreshRateSwitchingVote)) {
            return false;
        }
        com.android.server.display.mode.DisableRefreshRateSwitchingVote that = (com.android.server.display.mode.DisableRefreshRateSwitchingVote) o;
        return this.mDisableRefreshRateSwitching == that.mDisableRefreshRateSwitching;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Boolean.valueOf(this.mDisableRefreshRateSwitching));
    }

    public java.lang.String toString() {
        return "DisableRefreshRateSwitchingVote{ mDisableRefreshRateSwitching=" + this.mDisableRefreshRateSwitching + " }";
    }
}
