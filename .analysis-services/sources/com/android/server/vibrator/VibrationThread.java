package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class VibrationThread extends java.lang.Thread {
    static boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    static final java.lang.String TAG = "VibrationThread";
    private com.android.server.vibrator.VibrationStepConductor mExecutingConductor;
    private com.android.server.vibrator.VibrationStepConductor mRequestedActiveConductor;
    private final com.android.server.vibrator.VibrationThread.VibratorManagerHooks mVibratorManagerHooks;
    private final android.os.PowerManager.WakeLock mWakeLock;
    private final java.lang.Object mLock = new java.lang.Object();
    private boolean mCalledVibrationCompleteCallback = false;

    interface VibratorManagerHooks {
        void cancelSyncedVibration();

        void noteVibratorOff(int i);

        void noteVibratorOn(int i, long j);

        void onVibrationCompleted(long j, com.android.server.vibrator.Vibration.EndInfo endInfo);

        void onVibrationThreadReleased(long j);

        boolean prepareSyncedVibration(long j, int[] iArr);

        boolean triggerSyncedVibration(long j);
    }

    VibrationThread(android.os.PowerManager.WakeLock wakeLock, com.android.server.vibrator.VibrationThread.VibratorManagerHooks vibratorManagerHooks) {
        this.mWakeLock = wakeLock;
        this.mVibratorManagerHooks = vibratorManagerHooks;
    }

    boolean runVibrationOnVibrationThread(com.android.server.vibrator.VibrationStepConductor conductor) {
        synchronized (this.mLock) {
            if (this.mRequestedActiveConductor != null) {
                android.util.Slog.wtf(TAG, "Attempt to start vibration when one already running");
                return false;
            }
            this.mRequestedActiveConductor = conductor;
            this.mLock.notifyAll();
            return true;
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        android.os.Process.setThreadPriority(-8);
        while (true) {
            this.mExecutingConductor = (com.android.server.vibrator.VibrationStepConductor) java.util.Objects.requireNonNull(waitForVibrationRequest());
            this.mCalledVibrationCompleteCallback = false;
            runCurrentVibrationWithWakeLock();
            if (!this.mExecutingConductor.isFinished()) {
                android.util.Slog.wtf(TAG, "VibrationThread terminated with unfinished vibration");
            }
            synchronized (this.mLock) {
                this.mRequestedActiveConductor = null;
            }
            this.mVibratorManagerHooks.onVibrationThreadReleased(this.mExecutingConductor.getVibration().id);
            synchronized (this.mLock) {
                this.mLock.notifyAll();
            }
            this.mExecutingConductor = null;
        }
    }

    public boolean waitForThreadIdle(long maxWaitMillis) {
        long now = android.os.SystemClock.elapsedRealtime();
        long deadline = now + maxWaitMillis;
        synchronized (this.mLock) {
            while (this.mRequestedActiveConductor != null) {
                if (now >= deadline) {
                    return false;
                }
                try {
                    this.mLock.wait(deadline - now);
                } catch (java.lang.InterruptedException e) {
                    android.util.Slog.w(TAG, "VibrationThread interrupted waiting to stop, continuing");
                }
                now = android.os.SystemClock.elapsedRealtime();
            }
            return true;
        }
    }

    private com.android.server.vibrator.VibrationStepConductor waitForVibrationRequest() {
        while (true) {
            synchronized (this.mLock) {
                if (this.mRequestedActiveConductor != null) {
                    return this.mRequestedActiveConductor;
                }
                try {
                    this.mLock.wait();
                } catch (java.lang.InterruptedException e) {
                    android.util.Slog.w(TAG, "VibrationThread interrupted waiting to start, continuing");
                }
            }
        }
    }

    boolean isRunningVibrationId(long id) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mRequestedActiveConductor != null && this.mRequestedActiveConductor.getVibration().id == id;
        }
        return z;
    }

    private void runCurrentVibrationWithWakeLock() {
        android.os.WorkSource workSource = new android.os.WorkSource(this.mExecutingConductor.getVibration().callerInfo.uid);
        this.mWakeLock.setWorkSource(workSource);
        this.mWakeLock.acquire();
        try {
            try {
                runCurrentVibrationWithWakeLockAndDeathLink();
            } finally {
                clientVibrationCompleteIfNotAlready(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.FINISHED_UNEXPECTED));
            }
        } finally {
            this.mWakeLock.release();
            this.mWakeLock.setWorkSource(null);
        }
    }

    private void runCurrentVibrationWithWakeLockAndDeathLink() {
        android.os.IBinder vibrationBinderToken = this.mExecutingConductor.getVibration().callerToken;
        try {
            vibrationBinderToken.linkToDeath(this.mExecutingConductor, 0);
            try {
                playVibration();
                try {
                    vibrationBinderToken.unlinkToDeath(this.mExecutingConductor, 0);
                } catch (java.util.NoSuchElementException e) {
                    android.util.Slog.wtf(TAG, "Failed to unlink token", e);
                }
            } catch (java.lang.Throwable th) {
                try {
                    vibrationBinderToken.unlinkToDeath(this.mExecutingConductor, 0);
                } catch (java.util.NoSuchElementException e2) {
                    android.util.Slog.wtf(TAG, "Failed to unlink token", e2);
                }
                throw th;
            }
        } catch (android.os.RemoteException e3) {
            android.util.Slog.e(TAG, "Error linking vibration to token death", e3);
            clientVibrationCompleteIfNotAlready(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_ERROR_TOKEN));
        }
    }

    private void clientVibrationCompleteIfNotAlready(com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo) {
        if (!this.mCalledVibrationCompleteCallback) {
            this.mCalledVibrationCompleteCallback = true;
            this.mVibratorManagerHooks.onVibrationCompleted(this.mExecutingConductor.getVibration().id, vibrationEndInfo);
        }
    }

    private void playVibration() {
        com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo;
        android.os.Trace.traceBegin(8388608L, "playVibration");
        try {
            this.mExecutingConductor.prepareToStart();
            while (!this.mExecutingConductor.isFinished()) {
                boolean readyToRun = this.mExecutingConductor.waitUntilNextStepIsDue();
                if (readyToRun) {
                    this.mExecutingConductor.runNextStep();
                }
                if (!this.mCalledVibrationCompleteCallback && (vibrationEndInfo = this.mExecutingConductor.calculateVibrationEndInfo()) != null) {
                    clientVibrationCompleteIfNotAlready(vibrationEndInfo);
                }
            }
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }
}
