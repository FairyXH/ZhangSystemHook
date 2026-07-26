package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
class BaseModeRefreshRateVote implements com.android.server.display.mode.Vote {
    final float mAppRequestBaseModeRefreshRate;

    BaseModeRefreshRateVote(float baseModeRefreshRate) {
        this.mAppRequestBaseModeRefreshRate = baseModeRefreshRate;
    }

    @Override // com.android.server.display.mode.Vote
    public void updateSummary(com.android.server.display.mode.VoteSummary summary) {
        if (summary.appRequestBaseModeRefreshRate == 0.0f && this.mAppRequestBaseModeRefreshRate > 0.0f) {
            summary.appRequestBaseModeRefreshRate = this.mAppRequestBaseModeRefreshRate;
        }
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.display.mode.BaseModeRefreshRateVote)) {
            return false;
        }
        com.android.server.display.mode.BaseModeRefreshRateVote that = (com.android.server.display.mode.BaseModeRefreshRateVote) o;
        return java.lang.Float.compare(that.mAppRequestBaseModeRefreshRate, this.mAppRequestBaseModeRefreshRate) == 0;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Float.valueOf(this.mAppRequestBaseModeRefreshRate));
    }

    public java.lang.String toString() {
        return "BaseModeRefreshRateVote{ mAppRequestBaseModeRefreshRate=" + this.mAppRequestBaseModeRefreshRate + " }";
    }
}
