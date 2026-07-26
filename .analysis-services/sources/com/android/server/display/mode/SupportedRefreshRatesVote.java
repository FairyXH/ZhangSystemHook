package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
class SupportedRefreshRatesVote implements com.android.server.display.mode.Vote {
    final java.util.List<com.android.server.display.mode.SupportedRefreshRatesVote.RefreshRates> mRefreshRates;

    SupportedRefreshRatesVote(java.util.List<com.android.server.display.mode.SupportedRefreshRatesVote.RefreshRates> refreshRates) {
        this.mRefreshRates = java.util.Collections.unmodifiableList(refreshRates);
    }

    @Override // com.android.server.display.mode.Vote
    public void updateSummary(com.android.server.display.mode.VoteSummary summary) {
        if (summary.supportedRefreshRates == null) {
            summary.supportedRefreshRates = new java.util.ArrayList(this.mRefreshRates);
        } else {
            summary.supportedRefreshRates.retainAll(this.mRefreshRates);
        }
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.display.mode.SupportedRefreshRatesVote)) {
            return false;
        }
        com.android.server.display.mode.SupportedRefreshRatesVote that = (com.android.server.display.mode.SupportedRefreshRatesVote) o;
        return this.mRefreshRates.equals(that.mRefreshRates);
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mRefreshRates);
    }

    public java.lang.String toString() {
        return "SupportedRefreshRatesVote{ mSupportedModes=" + this.mRefreshRates + " }";
    }

    static class RefreshRates {
        final float mPeakRefreshRate;
        final float mVsyncRate;

        RefreshRates(float peakRefreshRate, float vsyncRate) {
            this.mPeakRefreshRate = peakRefreshRate;
            this.mVsyncRate = vsyncRate;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.display.mode.SupportedRefreshRatesVote.RefreshRates)) {
                return false;
            }
            com.android.server.display.mode.SupportedRefreshRatesVote.RefreshRates that = (com.android.server.display.mode.SupportedRefreshRatesVote.RefreshRates) o;
            return java.lang.Float.compare(that.mPeakRefreshRate, this.mPeakRefreshRate) == 0 && java.lang.Float.compare(that.mVsyncRate, this.mVsyncRate) == 0;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Float.valueOf(this.mPeakRefreshRate), java.lang.Float.valueOf(this.mVsyncRate));
        }

        public java.lang.String toString() {
            return "RefreshRates{ mPeakRefreshRate=" + this.mPeakRefreshRate + ", mVsyncRate=" + this.mVsyncRate + " }";
        }
    }
}
