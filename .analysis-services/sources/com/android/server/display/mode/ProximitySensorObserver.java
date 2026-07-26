package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
class ProximitySensorObserver implements com.android.server.sensors.SensorManagerInternal.ProximityActiveListener, android.hardware.display.DisplayManager.DisplayListener {
    private android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    private final com.android.server.display.mode.DisplayModeDirector.Injector mInjector;
    private final com.android.server.display.mode.VotesStorage mVotesStorage;
    private final java.lang.String mProximitySensorName = null;
    private final java.lang.String mProximitySensorType = "android.sensor.proximity";
    private final android.util.SparseBooleanArray mDozeStateByDisplay = new android.util.SparseBooleanArray();
    private final java.lang.Object mSensorObserverLock = new java.lang.Object();
    private boolean mIsProxActive = false;

    ProximitySensorObserver(com.android.server.display.mode.VotesStorage votesStorage, com.android.server.display.mode.DisplayModeDirector.Injector injector) {
        this.mVotesStorage = votesStorage;
        this.mInjector = injector;
    }

    @Override // com.android.server.sensors.SensorManagerInternal.ProximityActiveListener
    public void onProximityActive(boolean isActive) {
        synchronized (this.mSensorObserverLock) {
            if (this.mIsProxActive != isActive) {
                this.mIsProxActive = isActive;
                recalculateVotesLocked();
            }
        }
    }

    void observe() {
        this.mDisplayManagerInternal = this.mInjector.getDisplayManagerInternal();
        com.android.server.sensors.SensorManagerInternal sensorManager = this.mInjector.getSensorManagerInternal();
        sensorManager.addProximityActiveListener(com.android.internal.os.BackgroundThread.getExecutor(), this);
        synchronized (this.mSensorObserverLock) {
            for (android.view.Display d : this.mInjector.getDisplays()) {
                this.mDozeStateByDisplay.put(d.getDisplayId(), this.mInjector.isDozeState(d));
            }
        }
        this.mInjector.registerDisplayListener(this, com.android.internal.os.BackgroundThread.getHandler(), 7L);
    }

    private void recalculateVotesLocked() {
        android.view.SurfaceControl.RefreshRateRange rate;
        android.view.Display[] displays = this.mInjector.getDisplays();
        for (android.view.Display d : displays) {
            int displayId = d.getDisplayId();
            com.android.server.display.mode.Vote vote = null;
            if (this.mIsProxActive && !this.mDozeStateByDisplay.get(displayId) && (rate = this.mDisplayManagerInternal.getRefreshRateForDisplayAndSensor(displayId, this.mProximitySensorName, "android.sensor.proximity")) != null) {
                vote = com.android.server.display.mode.Vote.forPhysicalRefreshRates(rate.min, rate.max);
            }
            this.mVotesStorage.updateVote(displayId, 19, vote);
        }
    }

    void dump(java.io.PrintWriter pw) {
        pw.println("  SensorObserver");
        synchronized (this.mSensorObserverLock) {
            pw.println("    mIsProxActive=" + this.mIsProxActive);
            pw.println("    mDozeStateByDisplay:");
            for (int i = 0; i < this.mDozeStateByDisplay.size(); i++) {
                int id = this.mDozeStateByDisplay.keyAt(i);
                boolean dozed = this.mDozeStateByDisplay.valueAt(i);
                pw.println("      " + id + " -> " + dozed);
            }
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayAdded(int displayId) {
        boolean isDozeState = this.mInjector.isDozeState(this.mInjector.getDisplay(displayId));
        synchronized (this.mSensorObserverLock) {
            this.mDozeStateByDisplay.put(displayId, isDozeState);
            recalculateVotesLocked();
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayChanged(int displayId) {
        synchronized (this.mSensorObserverLock) {
            boolean wasDozeState = this.mDozeStateByDisplay.get(displayId);
            this.mDozeStateByDisplay.put(displayId, this.mInjector.isDozeState(this.mInjector.getDisplay(displayId)));
            if (wasDozeState != this.mDozeStateByDisplay.get(displayId)) {
                recalculateVotesLocked();
            }
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayRemoved(int displayId) {
        synchronized (this.mSensorObserverLock) {
            this.mDozeStateByDisplay.delete(displayId);
            recalculateVotesLocked();
        }
    }
}
