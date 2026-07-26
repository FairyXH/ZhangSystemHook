package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
public class SupportedModesVote implements com.android.server.display.mode.Vote {
    final java.util.List<java.lang.Integer> mModeIds;

    SupportedModesVote(java.util.List<java.lang.Integer> modeIds) {
        this.mModeIds = java.util.Collections.unmodifiableList(modeIds);
    }

    @Override // com.android.server.display.mode.Vote
    public void updateSummary(com.android.server.display.mode.VoteSummary summary) {
        if (summary.supportedModeIds == null) {
            summary.supportedModeIds = this.mModeIds;
        } else {
            summary.supportedModeIds.retainAll(this.mModeIds);
        }
    }

    public java.lang.String toString() {
        return "SupportedModesVote{ mModeIds=" + this.mModeIds + " }";
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.display.mode.SupportedModesVote)) {
            return false;
        }
        com.android.server.display.mode.SupportedModesVote that = (com.android.server.display.mode.SupportedModesVote) o;
        return this.mModeIds.equals(that.mModeIds);
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mModeIds);
    }
}
