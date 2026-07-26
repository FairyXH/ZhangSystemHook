package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
class CombinedVote implements com.android.server.display.mode.Vote {
    final java.util.List<com.android.server.display.mode.Vote> mVotes;

    CombinedVote(java.util.List<com.android.server.display.mode.Vote> votes) {
        this.mVotes = java.util.Collections.unmodifiableList(votes);
    }

    @Override // com.android.server.display.mode.Vote
    public void updateSummary(final com.android.server.display.mode.VoteSummary summary) {
        this.mVotes.forEach(new java.util.function.Consumer() { // from class: com.android.server.display.mode.CombinedVote$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.display.mode.Vote) obj).updateSummary(summary);
            }
        });
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.display.mode.CombinedVote)) {
            return false;
        }
        com.android.server.display.mode.CombinedVote that = (com.android.server.display.mode.CombinedVote) o;
        return java.util.Objects.equals(this.mVotes, that.mVotes);
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mVotes);
    }

    public java.lang.String toString() {
        return "CombinedVote{ mVotes=" + this.mVotes + " }";
    }
}
