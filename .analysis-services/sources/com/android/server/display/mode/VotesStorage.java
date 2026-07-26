package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
class VotesStorage {
    static final int GLOBAL_ID = -1;
    private static final java.lang.String TAG = "VotesStorage";
    private final com.android.server.display.mode.VotesStorage.Listener mListener;
    private boolean mLoggingEnabled;
    private final java.lang.Object mStorageLock = new java.lang.Object();
    private final android.util.SparseArray<android.util.SparseArray<com.android.server.display.mode.Vote>> mVotesByDisplay = new android.util.SparseArray<>();
    private final com.android.server.display.mode.VotesStatsReporter mVotesStatsReporter;

    interface Listener {
        void onChanged();
    }

    VotesStorage(com.android.server.display.mode.VotesStorage.Listener listener, com.android.server.display.mode.VotesStatsReporter votesStatsReporter) {
        this.mListener = listener;
        this.mVotesStatsReporter = votesStatsReporter;
    }

    void setLoggingEnabled(boolean loggingEnabled) {
        this.mLoggingEnabled = loggingEnabled;
    }

    android.util.SparseArray<com.android.server.display.mode.Vote> getVotes(int displayId) {
        android.util.SparseArray<com.android.server.display.mode.Vote> votesLocal;
        android.util.SparseArray<com.android.server.display.mode.Vote> displayVotes;
        synchronized (this.mStorageLock) {
            android.util.SparseArray<com.android.server.display.mode.Vote> displayVotes2 = this.mVotesByDisplay.get(displayId);
            votesLocal = displayVotes2 != null ? displayVotes2.clone() : new android.util.SparseArray<>();
            android.util.SparseArray<com.android.server.display.mode.Vote> globalVotes = this.mVotesByDisplay.get(-1);
            displayVotes = globalVotes != null ? globalVotes.clone() : new android.util.SparseArray<>();
        }
        for (int i = 0; i < displayVotes.size(); i++) {
            int priority = displayVotes.keyAt(i);
            if (!votesLocal.contains(priority)) {
                votesLocal.put(priority, displayVotes.valueAt(i));
            }
        }
        return votesLocal;
    }

    void updateGlobalVote(int priority, com.android.server.display.mode.Vote vote) {
        updateVote(-1, priority, vote);
    }

    void updateVote(int displayId, int priority, com.android.server.display.mode.Vote vote) {
        android.util.SparseArray<com.android.server.display.mode.Vote> votes;
        if (this.mLoggingEnabled) {
            android.util.Slog.i(TAG, "updateVoteLocked(displayId=" + displayId + ", priority=" + com.android.server.display.mode.Vote.priorityToString(priority) + ", vote=" + vote + ")");
        }
        if (priority < 0 || priority > 20) {
            android.util.Slog.w(TAG, "Received a vote with an invalid priority, ignoring: priority=" + com.android.server.display.mode.Vote.priorityToString(priority) + ", vote=" + vote);
            return;
        }
        boolean changed = false;
        synchronized (this.mStorageLock) {
            if (this.mVotesByDisplay.contains(displayId)) {
                votes = this.mVotesByDisplay.get(displayId);
            } else {
                votes = new android.util.SparseArray<>();
                this.mVotesByDisplay.put(displayId, votes);
            }
            com.android.server.display.mode.Vote currentVote = votes.get(priority);
            if (vote != null && !vote.equals(currentVote)) {
                votes.put(priority, vote);
                changed = true;
            } else if (vote == null && currentVote != null) {
                votes.remove(priority);
                changed = true;
            }
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.i(TAG, "Updated votes for display=" + displayId + " votes=" + votes);
        }
        if (changed) {
            if (this.mVotesStatsReporter != null) {
                this.mVotesStatsReporter.reportVoteChanged(displayId, priority, vote);
            }
            this.mListener.onChanged();
        }
    }

    void removeAllVotesForPriority(int priority) {
        if (this.mLoggingEnabled) {
            android.util.Slog.i(TAG, "removeAllVotesForPriority(priority=" + com.android.server.display.mode.Vote.priorityToString(priority) + ")");
        }
        if (priority < 0 || priority > 20) {
            android.util.Slog.w(TAG, "Received an invalid priority, ignoring: priority=" + com.android.server.display.mode.Vote.priorityToString(priority));
            return;
        }
        android.util.IntArray removedVotesDisplayIds = new android.util.IntArray();
        synchronized (this.mStorageLock) {
            int size = this.mVotesByDisplay.size();
            for (int i = 0; i < size; i++) {
                android.util.SparseArray<com.android.server.display.mode.Vote> votes = this.mVotesByDisplay.valueAt(i);
                if (votes.get(priority) != null) {
                    votes.remove(priority);
                    removedVotesDisplayIds.add(this.mVotesByDisplay.keyAt(i));
                }
            }
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.i(TAG, "Removed votes with priority=" + priority + " for displays=" + removedVotesDisplayIds);
        }
        int removedVotesSize = removedVotesDisplayIds.size();
        if (removedVotesSize > 0) {
            if (this.mVotesStatsReporter != null) {
                for (int i2 = 0; i2 < removedVotesSize; i2++) {
                    this.mVotesStatsReporter.reportVoteChanged(removedVotesDisplayIds.get(i2), priority, null);
                }
            }
            this.mListener.onChanged();
        }
    }

    void dump(java.io.PrintWriter pw) {
        android.util.SparseArray<android.util.SparseArray<com.android.server.display.mode.Vote>> votesByDisplayLocal = new android.util.SparseArray<>();
        synchronized (this.mStorageLock) {
            for (int i = 0; i < this.mVotesByDisplay.size(); i++) {
                votesByDisplayLocal.put(this.mVotesByDisplay.keyAt(i), this.mVotesByDisplay.valueAt(i).clone());
            }
        }
        pw.println("  mVotesByDisplay:");
        for (int i2 = 0; i2 < votesByDisplayLocal.size(); i2++) {
            android.util.SparseArray<com.android.server.display.mode.Vote> votes = votesByDisplayLocal.valueAt(i2);
            if (votes.size() != 0) {
                pw.println("    " + votesByDisplayLocal.keyAt(i2) + ":");
                for (int p = 20; p >= 0; p--) {
                    com.android.server.display.mode.Vote vote = votes.get(p);
                    if (vote != null) {
                        pw.println("      " + com.android.server.display.mode.Vote.priorityToString(p) + " -> " + vote);
                    }
                }
            }
        }
    }

    void injectVotesByDisplay(android.util.SparseArray<android.util.SparseArray<com.android.server.display.mode.Vote>> votesByDisplay) {
        synchronized (this.mStorageLock) {
            this.mVotesByDisplay.clear();
            for (int i = 0; i < votesByDisplay.size(); i++) {
                this.mVotesByDisplay.put(votesByDisplay.keyAt(i), votesByDisplay.valueAt(i));
            }
        }
    }
}
