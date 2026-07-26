package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
final class VoteSummary {
    private static final float FLOAT_TOLERANCE = 0.01f;
    private static final java.lang.String TAG = "VoteSummary";
    public float appRequestBaseModeRefreshRate;
    public boolean disableRefreshRateSwitching;
    public int height;
    final boolean mIsDisplayResolutionRangeVotingEnabled;
    private final boolean mLoggingEnabled;
    private final boolean mSupportedModesVoteEnabled;
    private final boolean mSupportsFrameRateOverride;
    public float maxPhysicalRefreshRate;
    public float maxRenderFrameRate;
    public int minHeight;
    public float minPhysicalRefreshRate;
    public float minRenderFrameRate;
    public int minWidth;
    public java.util.Set<java.lang.Float> requestedRefreshRates = new java.util.HashSet();
    public java.util.List<java.lang.Integer> supportedModeIds;
    public java.util.List<com.android.server.display.mode.SupportedRefreshRatesVote.RefreshRates> supportedRefreshRates;
    public int width;

    VoteSummary(boolean isDisplayResolutionRangeVotingEnabled, boolean supportedModesVoteEnabled, boolean loggingEnabled, boolean supportsFrameRateOverride) {
        this.mIsDisplayResolutionRangeVotingEnabled = isDisplayResolutionRangeVotingEnabled;
        this.mSupportedModesVoteEnabled = supportedModesVoteEnabled;
        this.mLoggingEnabled = loggingEnabled;
        this.mSupportsFrameRateOverride = supportsFrameRateOverride;
        reset();
    }

    void applyVotes(android.util.SparseArray<com.android.server.display.mode.Vote> votes, int lowestConsideredPriority, int highestConsideredPriority) {
        reset();
        for (int priority = highestConsideredPriority; priority >= lowestConsideredPriority; priority--) {
            com.android.server.display.mode.Vote vote = votes.get(priority);
            if (vote != null) {
                vote.updateSummary(this);
            }
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.i(TAG, "applyVotes for range [" + com.android.server.display.mode.Vote.priorityToString(lowestConsideredPriority) + ", " + com.android.server.display.mode.Vote.priorityToString(highestConsideredPriority) + "]: " + this);
        }
    }

    void adjustSize(android.view.Display.Mode defaultMode, android.view.Display.Mode[] modes) {
        if (this.height == -1 || this.width == -1) {
            this.width = defaultMode.getPhysicalWidth();
            this.height = defaultMode.getPhysicalHeight();
        } else if (this.mIsDisplayResolutionRangeVotingEnabled) {
            updateSummaryWithBestAllowedResolution(modes);
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.i(TAG, "adjustSize: " + this);
        }
    }

    void limitRefreshRanges(com.android.server.display.mode.VoteSummary otherSummary) {
        this.minPhysicalRefreshRate = java.lang.Math.min(this.minPhysicalRefreshRate, otherSummary.minPhysicalRefreshRate);
        this.maxPhysicalRefreshRate = java.lang.Math.max(this.maxPhysicalRefreshRate, otherSummary.maxPhysicalRefreshRate);
        this.minRenderFrameRate = java.lang.Math.min(this.minRenderFrameRate, otherSummary.minRenderFrameRate);
        this.maxRenderFrameRate = java.lang.Math.max(this.maxRenderFrameRate, otherSummary.maxRenderFrameRate);
        if (this.mLoggingEnabled) {
            android.util.Slog.i(TAG, "limitRefreshRanges: " + this);
        }
    }

    java.util.List<android.view.Display.Mode> filterModes(android.view.Display.Mode[] modes) {
        if (!isValid()) {
            return new java.util.ArrayList();
        }
        java.util.ArrayList<android.view.Display.Mode> availableModes = new java.util.ArrayList<>();
        boolean missingBaseModeRefreshRate = this.appRequestBaseModeRefreshRate > 0.0f;
        for (android.view.Display.Mode mode : modes) {
            if (validateRefreshRatesSupported(mode) && validateModeSupported(mode) && validateModeSize(mode) && validateModeWithinPhysicalRefreshRange(mode) && validateModeWithinRenderRefreshRange(mode) && validateModeRenderRateAchievable(mode)) {
                availableModes.add(mode);
                if (equalsWithinFloatTolerance(mode.getRefreshRate(), this.appRequestBaseModeRefreshRate)) {
                    missingBaseModeRefreshRate = false;
                }
            }
        }
        if (missingBaseModeRefreshRate) {
            return new java.util.ArrayList();
        }
        return availableModes;
    }

    android.view.Display.Mode selectBaseMode(java.util.List<android.view.Display.Mode> availableModes, android.view.Display.Mode defaultMode) {
        float preferredRefreshRate = this.appRequestBaseModeRefreshRate > 0.0f ? this.appRequestBaseModeRefreshRate : defaultMode.getRefreshRate();
        for (android.view.Display.Mode availableMode : availableModes) {
            if (equalsWithinFloatTolerance(preferredRefreshRate, availableMode.getRefreshRate())) {
                return availableMode;
            }
        }
        if (availableModes.isEmpty()) {
            return null;
        }
        return availableModes.get(0);
    }

    void disableModeSwitching(float fps) {
        this.maxPhysicalRefreshRate = fps;
        this.minPhysicalRefreshRate = fps;
        this.maxRenderFrameRate = java.lang.Math.min(this.maxRenderFrameRate, fps);
        if (this.mLoggingEnabled) {
            android.util.Slog.i(TAG, "Disabled mode switching on summary: " + this);
        }
    }

    void disableRenderRateSwitching(float fps) {
        this.minRenderFrameRate = this.maxRenderFrameRate;
        if (!isRenderRateAchievable(fps)) {
            this.maxRenderFrameRate = fps;
            this.minRenderFrameRate = fps;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.i(TAG, "Disabled render rate switching on summary: " + this);
        }
    }

    private boolean validateModeSize(android.view.Display.Mode mode) {
        if (mode.getPhysicalWidth() != this.width || mode.getPhysicalHeight() != this.height) {
            if (this.mLoggingEnabled) {
                android.util.Slog.w(TAG, "Discarding mode " + mode.getModeId() + ", wrong size: desiredWidth=" + this.width + ": desiredHeight=" + this.height + ": actualWidth=" + mode.getPhysicalWidth() + ": actualHeight=" + mode.getPhysicalHeight());
                return false;
            }
            return false;
        }
        return true;
    }

    private boolean validateModeWithinPhysicalRefreshRange(android.view.Display.Mode mode) {
        float refreshRate = mode.getRefreshRate();
        if (refreshRate < this.minPhysicalRefreshRate - 0.01f || refreshRate > this.maxPhysicalRefreshRate + 0.01f) {
            if (this.mLoggingEnabled) {
                android.util.Slog.w(TAG, "Discarding mode " + mode.getModeId() + ", outside refresh rate bounds: minPhysicalRefreshRate=" + this.minPhysicalRefreshRate + ", maxPhysicalRefreshRate=" + this.maxPhysicalRefreshRate + ", modeRefreshRate=" + refreshRate);
                return false;
            }
            return false;
        }
        return true;
    }

    private boolean validateModeWithinRenderRefreshRange(android.view.Display.Mode mode) {
        float refreshRate = mode.getRefreshRate();
        if (this.mSupportsFrameRateOverride) {
            return true;
        }
        if (refreshRate < this.minRenderFrameRate - 0.01f || refreshRate > this.maxRenderFrameRate + 0.01f) {
            if (this.mLoggingEnabled) {
                android.util.Slog.w(TAG, "Discarding mode " + mode.getModeId() + ", outside render rate bounds: minRenderFrameRate=" + this.minRenderFrameRate + ", maxRenderFrameRate=" + this.maxRenderFrameRate + ", modeRefreshRate=" + refreshRate);
                return false;
            }
            return false;
        }
        return true;
    }

    private boolean validateModeRenderRateAchievable(android.view.Display.Mode mode) {
        float refreshRate = mode.getRefreshRate();
        if (!isRenderRateAchievable(refreshRate)) {
            if (this.mLoggingEnabled) {
                android.util.Slog.w(TAG, "Discarding mode " + mode.getModeId() + ", outside frame rate bounds: minRenderFrameRate=" + this.minRenderFrameRate + ", maxRenderFrameRate=" + this.maxRenderFrameRate + ", modePhysicalRefreshRate=" + refreshRate);
                return false;
            }
            return false;
        }
        return true;
    }

    private boolean validateModeSupported(android.view.Display.Mode mode) {
        if (this.supportedModeIds == null || !this.mSupportedModesVoteEnabled || this.supportedModeIds.contains(java.lang.Integer.valueOf(mode.getModeId()))) {
            return true;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.w(TAG, "Discarding mode " + mode.getModeId() + ", supportedMode not found: mode.modeId=" + mode.getModeId() + ", supportedModeIds=" + this.supportedModeIds);
            return false;
        }
        return false;
    }

    private boolean validateRefreshRatesSupported(android.view.Display.Mode mode) {
        if (this.supportedRefreshRates == null || !this.mSupportedModesVoteEnabled) {
            return true;
        }
        for (com.android.server.display.mode.SupportedRefreshRatesVote.RefreshRates refreshRates : this.supportedRefreshRates) {
            if (equalsWithinFloatTolerance(mode.getRefreshRate(), refreshRates.mPeakRefreshRate) && equalsWithinFloatTolerance(mode.getVsyncRate(), refreshRates.mVsyncRate)) {
                return true;
            }
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.w(TAG, "Discarding mode " + mode.getModeId() + ", supportedRefreshRates not found: mode.refreshRate=" + mode.getRefreshRate() + ", mode.vsyncRate=" + mode.getVsyncRate() + ", supportedRefreshRates=" + this.supportedRefreshRates);
            return false;
        }
        return false;
    }

    private boolean isRenderRateAchievable(float physicalRefreshRate) {
        int divisor = (int) java.lang.Math.ceil((physicalRefreshRate / this.maxRenderFrameRate) - 0.01f);
        float adjustedPhysicalRefreshRate = physicalRefreshRate / divisor;
        return adjustedPhysicalRefreshRate >= this.minRenderFrameRate - 0.01f;
    }

    private boolean isValid() {
        if (this.minRenderFrameRate > this.maxRenderFrameRate + 0.01f) {
            if (this.mLoggingEnabled) {
                android.util.Slog.w(TAG, "Vote summary resulted in empty set (invalid frame rate range): minRenderFrameRate=" + this.minRenderFrameRate + ", maxRenderFrameRate=" + this.maxRenderFrameRate);
            }
            return false;
        }
        if (this.supportedRefreshRates != null && this.mSupportedModesVoteEnabled && this.supportedRefreshRates.isEmpty()) {
            if (this.mLoggingEnabled) {
                android.util.Slog.w(TAG, "Vote summary resulted in empty set (empty supportedModes)");
            }
            return false;
        }
        for (java.lang.Float requestedRefreshRate : this.requestedRefreshRates) {
            if (requestedRefreshRate.floatValue() < this.minRenderFrameRate || requestedRefreshRate.floatValue() > this.maxRenderFrameRate) {
                if (this.mLoggingEnabled) {
                    android.util.Slog.w(TAG, "Requested refreshRate is outside frame rate range: requestedRefreshRates=" + this.requestedRefreshRates + ", requestedRefreshRate=" + requestedRefreshRate + ", minRenderFrameRate=" + this.minRenderFrameRate + ", maxRenderFrameRate=" + this.maxRenderFrameRate);
                }
                return false;
            }
        }
        return true;
    }

    private void updateSummaryWithBestAllowedResolution(android.view.Display.Mode[] supportedModes) {
        int numberOfPixels;
        int maxAllowedWidth = this.width;
        int maxAllowedHeight = this.height;
        this.width = -1;
        this.height = -1;
        int maxNumberOfPixels = 0;
        for (android.view.Display.Mode mode : supportedModes) {
            if (mode.getPhysicalWidth() <= maxAllowedWidth && mode.getPhysicalHeight() <= maxAllowedHeight && mode.getPhysicalWidth() >= this.minWidth && mode.getPhysicalHeight() >= this.minHeight && mode.getRefreshRate() >= this.minPhysicalRefreshRate - 0.01f && mode.getRefreshRate() <= this.maxPhysicalRefreshRate + 0.01f && ((numberOfPixels = mode.getPhysicalHeight() * mode.getPhysicalWidth()) > maxNumberOfPixels || (mode.getPhysicalWidth() == maxAllowedWidth && mode.getPhysicalHeight() == maxAllowedHeight))) {
                maxNumberOfPixels = numberOfPixels;
                this.width = mode.getPhysicalWidth();
                this.height = mode.getPhysicalHeight();
            }
        }
    }

    private void reset() {
        this.minPhysicalRefreshRate = 0.0f;
        this.maxPhysicalRefreshRate = Float.POSITIVE_INFINITY;
        this.minRenderFrameRate = 0.0f;
        this.maxRenderFrameRate = Float.POSITIVE_INFINITY;
        this.width = -1;
        this.height = -1;
        this.minWidth = 0;
        this.minHeight = 0;
        this.disableRefreshRateSwitching = false;
        this.appRequestBaseModeRefreshRate = 0.0f;
        this.requestedRefreshRates.clear();
        this.supportedRefreshRates = null;
        this.supportedModeIds = null;
        if (this.mLoggingEnabled) {
            android.util.Slog.i(TAG, "Summary reset: " + this);
        }
    }

    private static boolean equalsWithinFloatTolerance(float a, float b) {
        return a >= b - 0.01f && a <= 0.01f + b;
    }

    public java.lang.String toString() {
        return "VoteSummary{ minPhysicalRefreshRate=" + this.minPhysicalRefreshRate + ", maxPhysicalRefreshRate=" + this.maxPhysicalRefreshRate + ", minRenderFrameRate=" + this.minRenderFrameRate + ", maxRenderFrameRate=" + this.maxRenderFrameRate + ", width=" + this.width + ", height=" + this.height + ", minWidth=" + this.minWidth + ", minHeight=" + this.minHeight + ", disableRefreshRateSwitching=" + this.disableRefreshRateSwitching + ", appRequestBaseModeRefreshRate=" + this.appRequestBaseModeRefreshRate + ", requestRefreshRates=" + this.requestedRefreshRates + ", supportedRefreshRates=" + this.supportedRefreshRates + ", supportedModeIds=" + this.supportedModeIds + ", mIsDisplayResolutionRangeVotingEnabled=" + this.mIsDisplayResolutionRangeVotingEnabled + ", mSupportedModesVoteEnabled=" + this.mSupportedModesVoteEnabled + ", mSupportsFrameRateOverride=" + this.mSupportsFrameRateOverride + " }";
    }
}
