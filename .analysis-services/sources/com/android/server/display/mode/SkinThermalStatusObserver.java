package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
final class SkinThermalStatusObserver extends android.os.IThermalEventListener.Stub implements android.hardware.display.DisplayManager.DisplayListener {
    private static final java.lang.String TAG = "SkinThermalStatusObserver";
    private final android.os.Handler mHandler;
    private final com.android.server.display.mode.DisplayModeDirector.Injector mInjector;
    private boolean mLoggingEnabled;
    private int mStatus;
    private final java.lang.Object mThermalObserverLock;
    private final android.util.SparseArray<android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange>> mThermalThrottlingByDisplay;
    private final com.android.server.display.mode.VotesStorage mVotesStorage;

    SkinThermalStatusObserver(com.android.server.display.mode.DisplayModeDirector.Injector injector, com.android.server.display.mode.VotesStorage votesStorage) {
        this(injector, votesStorage, com.android.internal.os.BackgroundThread.getHandler());
    }

    SkinThermalStatusObserver(com.android.server.display.mode.DisplayModeDirector.Injector injector, com.android.server.display.mode.VotesStorage votesStorage, android.os.Handler handler) {
        this.mThermalObserverLock = new java.lang.Object();
        this.mStatus = 0;
        this.mThermalThrottlingByDisplay = new android.util.SparseArray<>();
        this.mInjector = injector;
        this.mVotesStorage = votesStorage;
        this.mHandler = handler;
    }

    public static android.view.SurfaceControl.RefreshRateRange findBestMatchingRefreshRateRange(int currentStatus, android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange> throttlingMap) {
        android.view.SurfaceControl.RefreshRateRange foundRange = null;
        for (int status = currentStatus; status >= 0; status--) {
            android.view.SurfaceControl.RefreshRateRange foundRange2 = throttlingMap.get(status);
            foundRange = foundRange2;
            if (foundRange != null) {
                break;
            }
        }
        return foundRange;
    }

    void observe() {
        if (!this.mInjector.registerThermalServiceListener(this)) {
            return;
        }
        this.mInjector.registerDisplayListener(this, this.mHandler, 7L);
        populateInitialDisplayInfo();
    }

    void setLoggingEnabled(boolean enabled) {
        this.mLoggingEnabled = enabled;
    }

    public void notifyThrottling(android.os.Temperature temp) {
        int currentStatus = temp.getStatus();
        synchronized (this.mThermalObserverLock) {
            if (this.mStatus == currentStatus) {
                return;
            }
            this.mStatus = currentStatus;
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.mode.SkinThermalStatusObserver$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.updateVotes();
                }
            });
            if (this.mLoggingEnabled) {
                android.util.Slog.d(TAG, "New thermal throttling status , current thermal status = " + currentStatus);
            }
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayAdded(int displayId) {
        updateThermalRefreshRateThrottling(displayId);
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Display added:" + displayId);
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayRemoved(final int displayId) {
        synchronized (this.mThermalObserverLock) {
            this.mThermalThrottlingByDisplay.remove(displayId);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.mode.SkinThermalStatusObserver$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onDisplayRemoved$0(displayId);
                }
            });
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Display removed and voted: displayId=" + displayId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayRemoved$0(int displayId) {
        this.mVotesStorage.updateVote(displayId, 18, null);
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayChanged(int displayId) {
        updateThermalRefreshRateThrottling(displayId);
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Display changed:" + displayId);
        }
    }

    private void populateInitialDisplayInfo() {
        android.view.DisplayInfo info = new android.view.DisplayInfo();
        android.view.Display[] displays = this.mInjector.getDisplays();
        int size = displays.length;
        android.util.SparseArray<android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange>> localMap = new android.util.SparseArray<>(size);
        for (android.view.Display d : displays) {
            int displayId = d.getDisplayId();
            d.getDisplayInfo(info);
            localMap.put(displayId, info.thermalRefreshRateThrottling);
        }
        synchronized (this.mThermalObserverLock) {
            for (int i = 0; i < size; i++) {
                this.mThermalThrottlingByDisplay.put(localMap.keyAt(i), localMap.valueAt(i));
            }
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Display initial info:" + localMap);
        }
    }

    private void updateThermalRefreshRateThrottling(final int displayId) {
        android.view.DisplayInfo displayInfo = new android.view.DisplayInfo();
        this.mInjector.getDisplayInfo(displayId, displayInfo);
        android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange> throttlingMap = displayInfo.thermalRefreshRateThrottling;
        synchronized (this.mThermalObserverLock) {
            this.mThermalThrottlingByDisplay.put(displayId, throttlingMap);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.mode.SkinThermalStatusObserver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateThermalRefreshRateThrottling$1(displayId);
                }
            });
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Thermal throttling updated: display=" + displayId + ", map=" + throttlingMap);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVotes() {
        int localStatus;
        android.util.SparseArray<android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange>> localMap;
        synchronized (this.mThermalObserverLock) {
            localStatus = this.mStatus;
            localMap = this.mThermalThrottlingByDisplay.clone();
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Updating votes for status=" + localStatus + ", map=" + localMap);
        }
        int size = localMap.size();
        for (int i = 0; i < size; i++) {
            reportThrottlingIfNeeded(localMap.keyAt(i), localStatus, localMap.valueAt(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: updateVoteForDisplay, reason: merged with bridge method [inline-methods] */
    public void lambda$updateThermalRefreshRateThrottling$1(int displayId) {
        int localStatus;
        android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange> localMap;
        synchronized (this.mThermalObserverLock) {
            localStatus = this.mStatus;
            localMap = this.mThermalThrottlingByDisplay.get(displayId);
        }
        if (localMap == null) {
            android.util.Slog.d(TAG, "Updating votes, display already removed, display=" + displayId);
            return;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Updating votes for status=" + localStatus + ", display =" + displayId + ", map=" + localMap);
        }
        reportThrottlingIfNeeded(displayId, localStatus, localMap);
    }

    private void reportThrottlingIfNeeded(int displayId, int currentStatus, android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange> throttlingMap) {
        if (currentStatus == -1) {
            return;
        }
        if (throttlingMap == null) {
            android.util.Slog.d(TAG, "throttlingMap is null, check why value is null");
            return;
        }
        if (throttlingMap.size() == 0) {
            fallbackReportThrottlingIfNeeded(displayId, currentStatus);
            return;
        }
        android.view.SurfaceControl.RefreshRateRange foundRange = findBestMatchingRefreshRateRange(currentStatus, throttlingMap);
        com.android.server.display.mode.Vote vote = null;
        if (foundRange != null) {
            vote = com.android.server.display.mode.Vote.forRenderFrameRates(foundRange.min, foundRange.max);
        }
        this.mVotesStorage.updateVote(displayId, 18, vote);
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Voted: vote=" + vote + ", display =" + displayId);
        }
    }

    private void fallbackReportThrottlingIfNeeded(int displayId, int currentStatus) {
        com.android.server.display.mode.Vote vote = null;
        if (currentStatus >= 4) {
            vote = com.android.server.display.mode.Vote.forRenderFrameRates(0.0f, 60.0f);
            android.util.Slog.d(TAG, "Voted(fallback): vote=" + vote + ", display =" + displayId);
        }
        this.mVotesStorage.updateVote(displayId, 18, vote);
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "Voted(fallback): vote=" + vote + ", display =" + displayId);
        }
    }

    void dumpLocked(java.io.PrintWriter writer) {
        int localStatus;
        android.util.SparseArray<android.util.SparseArray<android.view.SurfaceControl.RefreshRateRange>> localMap;
        synchronized (this.mThermalObserverLock) {
            localStatus = this.mStatus;
            localMap = this.mThermalThrottlingByDisplay.clone();
        }
        writer.println("  SkinThermalStatusObserver:");
        writer.println("    mStatus: " + localStatus);
        writer.println("    mThermalThrottlingByDisplay: " + localMap);
    }
}
