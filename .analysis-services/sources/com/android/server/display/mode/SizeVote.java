package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
class SizeVote implements com.android.server.display.mode.Vote {
    final int mHeight;
    final int mMinHeight;
    final int mMinWidth;
    final int mWidth;

    SizeVote(int width, int height, int minWidth, int minHeight) {
        this.mWidth = width;
        this.mHeight = height;
        this.mMinWidth = minWidth;
        this.mMinHeight = minHeight;
    }

    @Override // com.android.server.display.mode.Vote
    public void updateSummary(com.android.server.display.mode.VoteSummary summary) {
        if (this.mHeight > 0 && this.mWidth > 0) {
            if (summary.width == -1 && summary.height == -1) {
                summary.width = this.mWidth;
                summary.height = this.mHeight;
                summary.minWidth = this.mMinWidth;
                summary.minHeight = this.mMinHeight;
                return;
            }
            if (summary.mIsDisplayResolutionRangeVotingEnabled) {
                summary.width = java.lang.Math.min(summary.width, this.mWidth);
                summary.height = java.lang.Math.min(summary.height, this.mHeight);
                summary.minWidth = java.lang.Math.max(summary.minWidth, this.mMinWidth);
                summary.minHeight = java.lang.Math.max(summary.minHeight, this.mMinHeight);
            }
        }
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.display.mode.SizeVote)) {
            return false;
        }
        com.android.server.display.mode.SizeVote sizeVote = (com.android.server.display.mode.SizeVote) o;
        return this.mWidth == sizeVote.mWidth && this.mHeight == sizeVote.mHeight && this.mMinWidth == sizeVote.mMinWidth && this.mMinHeight == sizeVote.mMinHeight;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(this.mWidth), java.lang.Integer.valueOf(this.mHeight), java.lang.Integer.valueOf(this.mMinWidth), java.lang.Integer.valueOf(this.mMinHeight));
    }

    public java.lang.String toString() {
        return "SizeVote{ mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + ", mMinWidth=" + this.mMinWidth + ", mMinHeight=" + this.mMinHeight + " }";
    }
}
