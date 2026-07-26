package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class RefreshRatePolicy {
    static final int LAYER_PRIORITY_FOCUSED_WITHOUT_MODE = 1;
    static final int LAYER_PRIORITY_FOCUSED_WITH_MODE = 0;
    static final int LAYER_PRIORITY_NOT_FOCUSED_WITH_MODE = 2;
    static final int LAYER_PRIORITY_UNSET = -1;
    private final android.view.Display.Mode mDefaultMode;
    private final android.view.DisplayInfo mDisplayInfo;
    private final com.android.server.wm.HighRefreshRateDenylist mHighRefreshRateDenylist;
    private final android.view.Display.Mode mLowRefreshRateMode;
    private float mMaxSupportedRefreshRate;
    private float mMinSupportedRefreshRate;
    private final com.android.server.wm.RefreshRatePolicy.PackageRefreshRate mNonHighRefreshRatePackages = new com.android.server.wm.RefreshRatePolicy.PackageRefreshRate();
    private final com.android.server.wm.WindowManagerService mWmService;

    class PackageRefreshRate {
        private final java.util.HashMap<java.lang.String, android.view.SurfaceControl.RefreshRateRange> mPackages = new java.util.HashMap<>();

        PackageRefreshRate() {
        }

        public void add(java.lang.String s, float minRefreshRate, float maxRefreshRate) {
            float minSupportedRefreshRate = java.lang.Math.max(com.android.server.wm.RefreshRatePolicy.this.mMinSupportedRefreshRate, minRefreshRate);
            float maxSupportedRefreshRate = java.lang.Math.min(com.android.server.wm.RefreshRatePolicy.this.mMaxSupportedRefreshRate, maxRefreshRate);
            this.mPackages.put(s, new android.view.SurfaceControl.RefreshRateRange(minSupportedRefreshRate, maxSupportedRefreshRate));
        }

        public android.view.SurfaceControl.RefreshRateRange get(java.lang.String s) {
            return this.mPackages.get(s);
        }

        public void remove(java.lang.String s) {
            this.mPackages.remove(s);
        }
    }

    RefreshRatePolicy(com.android.server.wm.WindowManagerService wmService, android.view.DisplayInfo displayInfo, com.android.server.wm.HighRefreshRateDenylist denylist) {
        this.mDisplayInfo = displayInfo;
        this.mDefaultMode = displayInfo.getDefaultMode();
        this.mLowRefreshRateMode = findLowRefreshRateMode(displayInfo, this.mDefaultMode);
        this.mHighRefreshRateDenylist = denylist;
        this.mWmService = wmService;
    }

    private android.view.Display.Mode findLowRefreshRateMode(android.view.DisplayInfo displayInfo, android.view.Display.Mode defaultMode) {
        float[] refreshRates = displayInfo.getDefaultRefreshRates();
        float bestRefreshRate = defaultMode.getRefreshRate();
        this.mMinSupportedRefreshRate = bestRefreshRate;
        this.mMaxSupportedRefreshRate = bestRefreshRate;
        for (int i = refreshRates.length - 1; i >= 0; i--) {
            this.mMinSupportedRefreshRate = java.lang.Math.min(this.mMinSupportedRefreshRate, refreshRates[i]);
            this.mMaxSupportedRefreshRate = java.lang.Math.max(this.mMaxSupportedRefreshRate, refreshRates[i]);
            if (refreshRates[i] >= 60.0f && refreshRates[i] < bestRefreshRate) {
                bestRefreshRate = refreshRates[i];
            }
        }
        return displayInfo.findDefaultModeByRefreshRate(bestRefreshRate);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addRefreshRateRangeForPackage(java.lang.String packageName, float minRefreshRate, float maxRefreshRate) {
        this.mNonHighRefreshRatePackages.add(packageName, minRefreshRate, maxRefreshRate);
        this.mWmService.requestTraversal();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeRefreshRateRangeForPackage(java.lang.String packageName) {
        this.mNonHighRefreshRatePackages.remove(packageName);
        this.mWmService.requestTraversal();
    }

    int getPreferredModeId(com.android.server.wm.WindowState w) {
        int preferredDisplayModeId = w.mAttrs.preferredDisplayModeId;
        if (preferredDisplayModeId <= 0) {
            return 0;
        }
        if (!com.android.window.flags.Flags.explicitRefreshRateHints() && w.isAnimationRunningSelfOrParent()) {
            android.view.Display.Mode preferredMode = null;
            android.view.Display.Mode[] modeArr = this.mDisplayInfo.supportedModes;
            int length = modeArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                android.view.Display.Mode mode = modeArr[i];
                if (preferredDisplayModeId != mode.getModeId()) {
                    i++;
                } else {
                    preferredMode = mode;
                    break;
                }
            }
            if (preferredMode != null) {
                int pW = preferredMode.getPhysicalWidth();
                int pH = preferredMode.getPhysicalHeight();
                if ((pW != this.mDefaultMode.getPhysicalWidth() || pH != this.mDefaultMode.getPhysicalHeight()) && pW == this.mDisplayInfo.getNaturalWidth() && pH == this.mDisplayInfo.getNaturalHeight()) {
                    return preferredDisplayModeId;
                }
            }
            return 0;
        }
        return preferredDisplayModeId;
    }

    int calculatePriority(com.android.server.wm.WindowState w) {
        boolean isFocused = w.isFocused();
        int preferredModeId = getPreferredModeId(w);
        if (!isFocused && preferredModeId > 0) {
            return 2;
        }
        if (isFocused && preferredModeId == 0) {
            return 1;
        }
        if (isFocused && preferredModeId > 0) {
            return 0;
        }
        return -1;
    }

    public static class FrameRateVote {
        int mCompatibility;
        float mRefreshRate;
        int mSelectionStrategy;

        FrameRateVote(float refreshRate, int compatibility, int selectionStrategy) {
            update(refreshRate, compatibility, selectionStrategy);
        }

        FrameRateVote() {
            reset();
        }

        boolean update(float refreshRate, int compatibility, int selectionStrategy) {
            if (!refreshRateEquals(refreshRate) || this.mCompatibility != compatibility || this.mSelectionStrategy != selectionStrategy) {
                this.mRefreshRate = refreshRate;
                this.mCompatibility = compatibility;
                this.mSelectionStrategy = selectionStrategy;
                return true;
            }
            return false;
        }

        boolean reset() {
            return update(0.0f, 0, 0);
        }

        public boolean equals(java.lang.Object o) {
            if (!(o instanceof com.android.server.wm.RefreshRatePolicy.FrameRateVote)) {
                return false;
            }
            com.android.server.wm.RefreshRatePolicy.FrameRateVote other = (com.android.server.wm.RefreshRatePolicy.FrameRateVote) o;
            return refreshRateEquals(other.mRefreshRate) && this.mCompatibility == other.mCompatibility && this.mSelectionStrategy == other.mSelectionStrategy;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Float.valueOf(this.mRefreshRate), java.lang.Integer.valueOf(this.mCompatibility), java.lang.Integer.valueOf(this.mSelectionStrategy));
        }

        public java.lang.String toString() {
            return "mRefreshRate=" + this.mRefreshRate + ", mCompatibility=" + this.mCompatibility + ", mSelectionStrategy=" + this.mSelectionStrategy;
        }

        private boolean refreshRateEquals(float refreshRate) {
            return this.mRefreshRate <= refreshRate + 0.01f && this.mRefreshRate >= refreshRate - 0.01f;
        }
    }

    boolean updateFrameRateVote(com.android.server.wm.WindowState w) {
        int preferredModeId;
        int refreshRateSwitchingType = this.mWmService.mDisplayManagerInternal.getRefreshRateSwitchingType();
        if (refreshRateSwitchingType == 0) {
            return w.mFrameRateVote.reset();
        }
        if (!com.android.window.flags.Flags.explicitRefreshRateHints() && w.isAnimationRunningSelfOrParent()) {
            return w.mFrameRateVote.reset();
        }
        if (refreshRateSwitchingType != 3 && (preferredModeId = w.mAttrs.preferredDisplayModeId) > 0) {
            for (android.view.Display.Mode mode : this.mDisplayInfo.appsSupportedModes) {
                if (preferredModeId == mode.getModeId()) {
                    return w.mFrameRateVote.update(mode.getRefreshRate(), 100, 1);
                }
            }
        }
        if (w.mAttrs.preferredRefreshRate > 0.0f) {
            return w.mFrameRateVote.update(w.mAttrs.preferredRefreshRate, 0, 1);
        }
        if (refreshRateSwitchingType != 3) {
            java.lang.String packageName = w.getOwningPackage();
            if (this.mHighRefreshRateDenylist.isDenylisted(packageName)) {
                return w.mFrameRateVote.update(this.mLowRefreshRateMode.getRefreshRate(), 100, 1);
            }
        }
        return w.mFrameRateVote.reset();
    }

    float getPreferredMinRefreshRate(com.android.server.wm.WindowState w) {
        if (w.isAnimationRunningSelfOrParent()) {
            return 0.0f;
        }
        if (w.mAttrs.preferredMinDisplayRefreshRate > 0.0f) {
            return w.mAttrs.preferredMinDisplayRefreshRate;
        }
        java.lang.String packageName = w.getOwningPackage();
        android.view.SurfaceControl.RefreshRateRange range = this.mNonHighRefreshRatePackages.get(packageName);
        if (range != null) {
            return range.min;
        }
        return 0.0f;
    }

    float getPreferredMaxRefreshRate(com.android.server.wm.WindowState w) {
        if (w.isAnimationRunningSelfOrParent()) {
            return 0.0f;
        }
        if (w.mAttrs.preferredMaxDisplayRefreshRate > 0.0f) {
            return w.mAttrs.preferredMaxDisplayRefreshRate;
        }
        java.lang.String packageName = w.getOwningPackage();
        android.view.SurfaceControl.RefreshRateRange range = this.mNonHighRefreshRatePackages.get(packageName);
        if (range != null) {
            return range.max;
        }
        return 0.0f;
    }
}
