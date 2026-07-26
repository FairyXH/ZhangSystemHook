package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
abstract class RefreshRateVote implements com.android.server.display.mode.Vote {
    final float mMaxRefreshRate;
    final float mMinRefreshRate;

    RefreshRateVote(float minRefreshRate, float maxRefreshRate) {
        this.mMinRefreshRate = minRefreshRate;
        this.mMaxRefreshRate = maxRefreshRate;
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.display.mode.RefreshRateVote)) {
            return false;
        }
        com.android.server.display.mode.RefreshRateVote that = (com.android.server.display.mode.RefreshRateVote) o;
        return java.lang.Float.compare(that.mMinRefreshRate, this.mMinRefreshRate) == 0 && java.lang.Float.compare(that.mMaxRefreshRate, this.mMaxRefreshRate) == 0;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Float.valueOf(this.mMinRefreshRate), java.lang.Float.valueOf(this.mMaxRefreshRate));
    }

    public java.lang.String toString() {
        return "RefreshRateVote{  mMinRefreshRate=" + this.mMinRefreshRate + ", mMaxRefreshRate=" + this.mMaxRefreshRate + " }";
    }

    static class RenderVote extends com.android.server.display.mode.RefreshRateVote {
        RenderVote(float minRefreshRate, float maxRefreshRate) {
            super(minRefreshRate, maxRefreshRate);
        }

        @Override // com.android.server.display.mode.Vote
        public void updateSummary(com.android.server.display.mode.VoteSummary summary) {
            summary.minRenderFrameRate = java.lang.Math.max(summary.minRenderFrameRate, this.mMinRefreshRate);
            summary.maxRenderFrameRate = java.lang.Math.min(summary.maxRenderFrameRate, this.mMaxRefreshRate);
            summary.minPhysicalRefreshRate = java.lang.Math.max(summary.minPhysicalRefreshRate, this.mMinRefreshRate);
        }

        @Override // com.android.server.display.mode.RefreshRateVote
        public boolean equals(java.lang.Object o) {
            if (o instanceof com.android.server.display.mode.RefreshRateVote.RenderVote) {
                return super.equals(o);
            }
            return false;
        }

        @Override // com.android.server.display.mode.RefreshRateVote
        public java.lang.String toString() {
            return "RenderVote{ " + super.toString() + " }";
        }
    }

    static class PhysicalVote extends com.android.server.display.mode.RefreshRateVote {
        PhysicalVote(float minRefreshRate, float maxRefreshRate) {
            super(minRefreshRate, maxRefreshRate);
        }

        @Override // com.android.server.display.mode.Vote
        public void updateSummary(com.android.server.display.mode.VoteSummary summary) {
            summary.minPhysicalRefreshRate = java.lang.Math.max(summary.minPhysicalRefreshRate, this.mMinRefreshRate);
            summary.maxPhysicalRefreshRate = java.lang.Math.min(summary.maxPhysicalRefreshRate, this.mMaxRefreshRate);
            summary.maxRenderFrameRate = java.lang.Math.min(summary.maxRenderFrameRate, this.mMaxRefreshRate);
        }

        @Override // com.android.server.display.mode.RefreshRateVote
        public boolean equals(java.lang.Object o) {
            if (o instanceof com.android.server.display.mode.RefreshRateVote.PhysicalVote) {
                return super.equals(o);
            }
            return false;
        }

        @Override // com.android.server.display.mode.RefreshRateVote
        public java.lang.String toString() {
            return "PhysicalVote{ " + super.toString() + " }";
        }
    }
}
