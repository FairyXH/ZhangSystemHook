package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
class VotesStatsReporter {
    private static final int REFRESH_RATE_NOT_LIMITED = 1000;
    private static final java.lang.String TAG = "VotesStatsReporter";
    private final boolean mFrameworkStatsLogReportingEnabled;
    private final boolean mIgnoredRenderRate;
    private int mLastMinPriorityReported = 21;

    public VotesStatsReporter(boolean ignoreRenderRate, boolean refreshRateVotingTelemetryEnabled) {
        this.mIgnoredRenderRate = ignoreRenderRate;
        this.mFrameworkStatsLogReportingEnabled = refreshRateVotingTelemetryEnabled;
    }

    void reportVoteChanged(int displayId, int priority, com.android.server.display.mode.Vote vote) {
        if (vote == null) {
            reportVoteRemoved(displayId, priority);
        } else {
            reportVoteAdded(displayId, priority, vote);
        }
    }

    private void reportVoteAdded(int displayId, int priority, com.android.server.display.mode.Vote vote) {
        int maxRefreshRate = getMaxRefreshRate(vote, this.mIgnoredRenderRate);
        android.os.Trace.traceCounter(131072L, "VotesStatsReporter." + displayId + ":" + com.android.server.display.mode.Vote.priorityToString(priority), maxRefreshRate);
        if (this.mFrameworkStatsLogReportingEnabled) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DISPLAY_MODE_DIRECTOR_VOTE_CHANGED, displayId, priority, 1, maxRefreshRate, -1);
        }
    }

    private void reportVoteRemoved(int displayId, int priority) {
        android.os.Trace.traceCounter(131072L, "VotesStatsReporter." + displayId + ":" + com.android.server.display.mode.Vote.priorityToString(priority), -1);
        if (this.mFrameworkStatsLogReportingEnabled) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DISPLAY_MODE_DIRECTOR_VOTE_CHANGED, displayId, priority, 3, -1, -1);
        }
    }

    void reportVotesActivated(int displayId, int minPriority, android.view.Display.Mode baseMode, android.util.SparseArray<com.android.server.display.mode.Vote> votes) {
        com.android.server.display.mode.Vote vote;
        if (!this.mFrameworkStatsLogReportingEnabled) {
            return;
        }
        int selectedRefreshRate = baseMode != null ? (int) baseMode.getRefreshRate() : -1;
        for (int priority = 0; priority <= 20; priority++) {
            if ((priority >= this.mLastMinPriorityReported || priority >= minPriority) && (vote = votes.get(priority)) != null) {
                if (priority >= this.mLastMinPriorityReported && priority < minPriority) {
                    int maxRefreshRate = getMaxRefreshRate(vote, this.mIgnoredRenderRate);
                    com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DISPLAY_MODE_DIRECTOR_VOTE_CHANGED, displayId, priority, 1, maxRefreshRate, selectedRefreshRate);
                }
                if (priority >= minPriority && priority < this.mLastMinPriorityReported) {
                    int maxRefreshRate2 = getMaxRefreshRate(vote, this.mIgnoredRenderRate);
                    com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DISPLAY_MODE_DIRECTOR_VOTE_CHANGED, displayId, priority, 2, maxRefreshRate2, selectedRefreshRate);
                }
                this.mLastMinPriorityReported = minPriority;
            }
        }
    }

    private static int getMaxRefreshRate(com.android.server.display.mode.Vote vote, boolean ignoreRenderRate) {
        int maxRefreshRate = 1000;
        if (vote instanceof com.android.server.display.mode.RefreshRateVote.PhysicalVote) {
            com.android.server.display.mode.RefreshRateVote.PhysicalVote physicalVote = (com.android.server.display.mode.RefreshRateVote.PhysicalVote) vote;
            int maxRefreshRate2 = (int) physicalVote.mMaxRefreshRate;
            return maxRefreshRate2;
        }
        if (!ignoreRenderRate && (vote instanceof com.android.server.display.mode.RefreshRateVote.RenderVote)) {
            com.android.server.display.mode.RefreshRateVote.RenderVote renderVote = (com.android.server.display.mode.RefreshRateVote.RenderVote) vote;
            int maxRefreshRate3 = (int) renderVote.mMaxRefreshRate;
            return maxRefreshRate3;
        }
        if (vote instanceof com.android.server.display.mode.SupportedRefreshRatesVote) {
            com.android.server.display.mode.SupportedRefreshRatesVote refreshRatesVote = (com.android.server.display.mode.SupportedRefreshRatesVote) vote;
            int maxRefreshRate4 = 0;
            for (com.android.server.display.mode.SupportedRefreshRatesVote.RefreshRates rr : refreshRatesVote.mRefreshRates) {
                maxRefreshRate4 = java.lang.Math.max(maxRefreshRate4, (int) rr.mPeakRefreshRate);
            }
            return maxRefreshRate4;
        }
        if (!(vote instanceof com.android.server.display.mode.CombinedVote)) {
            return 1000;
        }
        com.android.server.display.mode.CombinedVote combinedVote = (com.android.server.display.mode.CombinedVote) vote;
        for (com.android.server.display.mode.Vote subVote : combinedVote.mVotes) {
            maxRefreshRate = java.lang.Math.min(maxRefreshRate, getMaxRefreshRate(subVote, ignoreRenderRate));
        }
        return maxRefreshRate;
    }
}
