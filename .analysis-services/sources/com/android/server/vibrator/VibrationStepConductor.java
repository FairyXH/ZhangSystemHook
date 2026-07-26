package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class VibrationStepConductor implements android.os.IBinder.DeathRecipient {
    static final long CALLBACKS_EXTRA_TIMEOUT = 1000;
    private static final boolean DEBUG = com.android.server.vibrator.VibrationThread.DEBUG;
    static final java.util.List<com.android.server.vibrator.Step> EMPTY_STEP_LIST = new java.util.ArrayList();
    static final float RAMP_OFF_AMPLITUDE_MIN = 0.001f;
    private static final java.lang.String TAG = "VibrationThread";
    private final com.android.server.vibrator.DeviceAdapter mDeviceAdapter;
    private int mPendingVibrateSteps;
    private int mRemainingStartSequentialEffectSteps;
    private final java.util.concurrent.CompletableFuture<java.lang.Void> mRequestVibrationParamsFuture;
    private final android.util.IntArray mSignalVibratorsComplete;
    private final com.android.server.vibrator.VibratorFrameworkStatsLogger mStatsLogger;
    private int mSuccessfulVibratorOnSteps;
    private final com.android.server.vibrator.HalVibration mVibration;
    private final com.android.server.vibrator.VibrationScaler mVibrationScaler;
    public final com.android.server.vibrator.VibrationSettings vibrationSettings;
    public final com.android.server.vibrator.VibrationThread.VibratorManagerHooks vibratorManagerHooks;
    private final java.util.PriorityQueue<com.android.server.vibrator.Step> mNextSteps = new java.util.PriorityQueue<>();
    private final java.util.Queue<com.android.server.vibrator.Step> mPendingOnVibratorCompleteSteps = new java.util.LinkedList();
    private final java.lang.Object mLock = new java.lang.Object();
    private com.android.server.vibrator.Vibration.EndInfo mSignalCancel = null;
    private boolean mSignalCancelImmediate = false;
    private com.android.server.vibrator.Vibration.EndInfo mCancelledVibrationEndInfo = null;
    private boolean mCancelledImmediately = false;
    private com.android.server.vibrator.IVibrationStepConductorExt mVibrationStepConductorExt = (com.android.server.vibrator.IVibrationStepConductorExt) system.ext.loader.core.ExtLoader.type(com.android.server.vibrator.IVibrationStepConductorExt.class).base(this).create();
    private com.android.server.vibrator.IVibrationStepConductorWrapper mVibrationStepConductorWrapper = new com.android.server.vibrator.VibrationStepConductor.VibrationStepConductorWrapper();

    VibrationStepConductor(com.android.server.vibrator.HalVibration vib, com.android.server.vibrator.VibrationSettings vibrationSettings, com.android.server.vibrator.DeviceAdapter deviceAdapter, com.android.server.vibrator.VibrationScaler vibrationScaler, com.android.server.vibrator.VibratorFrameworkStatsLogger statsLogger, java.util.concurrent.CompletableFuture<java.lang.Void> requestVibrationParamsFuture, com.android.server.vibrator.VibrationThread.VibratorManagerHooks vibratorManagerHooks) {
        this.mVibration = vib;
        this.vibrationSettings = vibrationSettings;
        this.mDeviceAdapter = deviceAdapter;
        this.mVibrationScaler = vibrationScaler;
        this.mStatsLogger = statsLogger;
        this.mRequestVibrationParamsFuture = requestVibrationParamsFuture;
        this.vibratorManagerHooks = vibratorManagerHooks;
        this.mSignalVibratorsComplete = new android.util.IntArray(this.mDeviceAdapter.getAvailableVibratorIds().length);
    }

    com.android.server.vibrator.AbstractVibratorStep nextVibrateStep(long startTime, com.android.server.vibrator.VibratorController controller, android.os.VibrationEffect.Composed effect, int segmentIndex, long pendingVibratorOffDeadline) {
        int segmentIndex2;
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        if (segmentIndex < effect.getSegments().size()) {
            segmentIndex2 = segmentIndex;
        } else {
            segmentIndex2 = effect.getRepeatIndex();
        }
        if (segmentIndex2 < 0) {
            return new com.android.server.vibrator.CompleteEffectVibratorStep(this, startTime, false, controller, pendingVibratorOffDeadline);
        }
        android.os.vibrator.VibrationEffectSegment segment = (android.os.vibrator.VibrationEffectSegment) effect.getSegments().get(segmentIndex2);
        com.android.server.vibrator.AbstractVibratorStep step = getWrapper().getExtImpl().nextVibrateStep(segment, startTime, controller, effect, segmentIndex2, pendingVibratorOffDeadline);
        if (step != null) {
            return step;
        }
        if (segment instanceof android.os.vibrator.PrebakedSegment) {
            return new com.android.server.vibrator.PerformPrebakedVibratorStep(this, startTime, controller, effect, segmentIndex2, pendingVibratorOffDeadline);
        }
        if (segment instanceof android.os.vibrator.PrimitiveSegment) {
            return new com.android.server.vibrator.ComposePrimitivesVibratorStep(this, startTime, controller, effect, segmentIndex2, pendingVibratorOffDeadline);
        }
        if (segment instanceof android.os.vibrator.RampSegment) {
            return new com.android.server.vibrator.ComposePwleVibratorStep(this, startTime, controller, effect, segmentIndex2, pendingVibratorOffDeadline);
        }
        return new com.android.server.vibrator.SetAmplitudeVibratorStep(this, startTime, controller, effect, segmentIndex2, pendingVibratorOffDeadline);
    }

    public void prepareToStart() {
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        if (!this.mVibration.callerInfo.attrs.isFlagSet(16)) {
            if (android.os.vibrator.Flags.adaptiveHapticsEnabled()) {
                waitForVibrationParamsIfRequired();
            }
            this.mVibration.scaleEffects(this.mVibrationScaler);
        } else {
            this.mVibration.resolveEffects(this.mVibrationScaler.getDefaultVibrationAmplitude());
        }
        this.mVibration.adaptToDevice(this.mDeviceAdapter);
        android.os.CombinedVibration.Sequential sequentialEffect = toSequential(this.mVibration.getEffectToPlay());
        this.mPendingVibrateSteps++;
        this.mRemainingStartSequentialEffectSteps = sequentialEffect.getEffects().size();
        this.mNextSteps.offer(new com.android.server.vibrator.StartSequentialEffectStep(this, sequentialEffect));
        this.mVibration.stats.reportStarted();
    }

    public com.android.server.vibrator.HalVibration getVibration() {
        return this.mVibration;
    }

    android.util.SparseArray<com.android.server.vibrator.VibratorController> getVibrators() {
        return this.mDeviceAdapter.getAvailableVibrators();
    }

    public boolean isFinished() {
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        if (this.mCancelledImmediately) {
            return true;
        }
        return this.mPendingOnVibratorCompleteSteps.isEmpty() && this.mNextSteps.isEmpty();
    }

    public com.android.server.vibrator.Vibration.EndInfo calculateVibrationEndInfo() {
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        if (this.mCancelledVibrationEndInfo != null) {
            return this.mCancelledVibrationEndInfo;
        }
        if (this.mPendingVibrateSteps > 0 || this.mRemainingStartSequentialEffectSteps > 0) {
            return null;
        }
        if (this.mSuccessfulVibratorOnSteps > 0) {
            return new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.FINISHED);
        }
        return new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_UNSUPPORTED);
    }

    public boolean waitUntilNextStepIsDue() {
        com.android.server.vibrator.Step nextStep;
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        processAllNotifySignals();
        if (this.mCancelledImmediately) {
            return false;
        }
        if (!this.mPendingOnVibratorCompleteSteps.isEmpty() || (nextStep = this.mNextSteps.peek()) == null) {
            return true;
        }
        getWrapper().getExtImpl().updateVibrationAmplitude(nextStep);
        long waitMillis = nextStep.calculateWaitTime();
        if (waitMillis <= 0) {
            return true;
        }
        synchronized (this.mLock) {
            if (hasPendingNotifySignalLocked()) {
                return false;
            }
            try {
                this.mLock.wait(waitMillis);
            } catch (java.lang.InterruptedException e) {
            }
            return false;
        }
    }

    private com.android.server.vibrator.Step pollNext() {
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        if (!this.mPendingOnVibratorCompleteSteps.isEmpty()) {
            return this.mPendingOnVibratorCompleteSteps.poll();
        }
        return this.mNextSteps.poll();
    }

    public void runNextStep() {
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        com.android.server.vibrator.Step stepPollNext = pollNext();
        if (stepPollNext != null) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Playing vibration id " + getVibration().id + (stepPollNext instanceof com.android.server.vibrator.AbstractVibratorStep ? " on vibrator " + ((com.android.server.vibrator.AbstractVibratorStep) stepPollNext).getVibratorId() : "") + " " + stepPollNext.getClass().getSimpleName() + (stepPollNext.isCleanUp() ? " (cleanup)" : ""));
            }
            java.util.List<com.android.server.vibrator.Step> listPlay = stepPollNext.play();
            if (stepPollNext.getVibratorOnDuration() > 0) {
                this.mSuccessfulVibratorOnSteps++;
            }
            if (stepPollNext instanceof com.android.server.vibrator.StartSequentialEffectStep) {
                this.mRemainingStartSequentialEffectSteps--;
            }
            if (!stepPollNext.isCleanUp()) {
                this.mPendingVibrateSteps--;
            }
            for (int i = 0; i < listPlay.size(); i++) {
                this.mPendingVibrateSteps += !listPlay.get(i).isCleanUp() ? 1 : 0;
            }
            this.mNextSteps.addAll(listPlay);
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Binder died, cancelling vibration...");
        }
        notifyCancelled(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.CANCELLED_BINDER_DIED), false);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x006d A[Catch: all -> 0x00fc, TryCatch #0 {, blocks: (B:15:0x0069, B:19:0x0071, B:21:0x0075, B:22:0x00a9, B:17:0x006d, B:24:0x00ab, B:26:0x00b4, B:30:0x00eb, B:32:0x00ef, B:33:0x00f5, B:34:0x00fa, B:27:0x00b7, B:29:0x00bb), top: B:39:0x0069 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void notifyCancelled(com.android.server.vibrator.Vibration.EndInfo r6, boolean r7) {
        /*
            Method dump skipped, instruction units count: 255
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.vibrator.VibrationStepConductor.notifyCancelled(com.android.server.vibrator.Vibration$EndInfo, boolean):void");
    }

    public void notifyVibratorComplete(int vibratorId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Vibration complete reported by vibrator " + vibratorId);
        }
        synchronized (this.mLock) {
            this.mSignalVibratorsComplete.add(vibratorId);
            this.mLock.notify();
        }
    }

    public void notifySyncedVibrationComplete() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Synced vibration complete reported by vibrator manager");
        }
        synchronized (this.mLock) {
            for (int vibratorId : this.mDeviceAdapter.getAvailableVibratorIds()) {
                this.mSignalVibratorsComplete.add(vibratorId);
            }
            this.mLock.notify();
        }
    }

    public boolean wasNotifiedToCancel() {
        boolean z;
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(false);
        }
        synchronized (this.mLock) {
            z = this.mSignalCancel != null;
        }
        return z;
    }

    private void waitForVibrationParamsIfRequired() {
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        if (this.mRequestVibrationParamsFuture == null) {
            return;
        }
        try {
            this.mRequestVibrationParamsFuture.get(this.vibrationSettings.getRequestVibrationParamsTimeoutMs(), java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.util.concurrent.CancellationException e) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Request for vibration params cancelled, maybe superseded or vibrator controller unregistered. Skipping params...", e);
            }
        } catch (java.util.concurrent.TimeoutException e2) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Request for vibration params timed out", e2);
            }
            this.mStatsLogger.logVibrationParamRequestTimeout(this.mVibration.callerInfo.uid);
        } catch (java.lang.Throwable e3) {
            android.util.Slog.w(TAG, "Failed to retrieve vibration params.", e3);
        }
    }

    private boolean hasPendingNotifySignalLocked() {
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        if (this.mSignalCancel == null || this.mCancelledVibrationEndInfo != null) {
            return (this.mSignalCancelImmediate && !this.mCancelledImmediately) || this.mSignalVibratorsComplete.size() > 0;
        }
        return true;
    }

    private void processAllNotifySignals() {
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        int[] vibratorsToProcess = null;
        com.android.server.vibrator.Vibration.EndInfo doCancelInfo = null;
        boolean doCancelImmediate = false;
        synchronized (this.mLock) {
            if (this.mSignalCancelImmediate) {
                if (this.mCancelledImmediately) {
                    android.util.Slog.wtf(TAG, "Immediate cancellation signal processed twice");
                }
                doCancelImmediate = true;
                doCancelInfo = this.mSignalCancel;
            }
            if (this.mSignalCancel != null && this.mCancelledVibrationEndInfo == null) {
                doCancelInfo = this.mSignalCancel;
            }
            if (!doCancelImmediate && this.mSignalVibratorsComplete.size() > 0) {
                vibratorsToProcess = this.mSignalVibratorsComplete.toArray();
                this.mSignalVibratorsComplete.clear();
            }
        }
        if (doCancelImmediate) {
            processCancelImmediately(doCancelInfo);
            return;
        }
        if (doCancelInfo != null) {
            processCancel(doCancelInfo);
        }
        if (vibratorsToProcess != null) {
            processVibratorsComplete(vibratorsToProcess);
        }
    }

    public void processCancel(com.android.server.vibrator.Vibration.EndInfo cancelInfo) {
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        this.mCancelledVibrationEndInfo = cancelInfo;
        java.util.List<com.android.server.vibrator.Step> cleanUpSteps = new java.util.ArrayList<>();
        while (true) {
            com.android.server.vibrator.Step step = pollNext();
            if (step != null) {
                cleanUpSteps.addAll(step.cancel());
            } else {
                this.mPendingVibrateSteps = 0;
                this.mNextSteps.addAll(cleanUpSteps);
                return;
            }
        }
    }

    public void processCancelImmediately(com.android.server.vibrator.Vibration.EndInfo cancelInfo) {
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        this.mCancelledImmediately = true;
        this.mCancelledVibrationEndInfo = cancelInfo;
        while (true) {
            com.android.server.vibrator.Step step = pollNext();
            if (step != null) {
                step.cancelImmediately();
            } else {
                this.mPendingVibrateSteps = 0;
                return;
            }
        }
    }

    private void processVibratorsComplete(int[] vibratorsToProcess) {
        if (android.os.Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        for (int vibratorId : vibratorsToProcess) {
            java.util.Iterator<com.android.server.vibrator.Step> it = this.mNextSteps.iterator();
            while (true) {
                if (it.hasNext()) {
                    com.android.server.vibrator.Step step = it.next();
                    if (step.acceptVibratorCompleteCallback(vibratorId)) {
                        it.remove();
                        this.mPendingOnVibratorCompleteSteps.offer(step);
                        break;
                    }
                }
            }
        }
    }

    private static android.os.CombinedVibration.Sequential toSequential(android.os.CombinedVibration effect) {
        if (effect instanceof android.os.CombinedVibration.Sequential) {
            return (android.os.CombinedVibration.Sequential) effect;
        }
        return android.os.CombinedVibration.startSequential().addNext(effect).combine();
    }

    private static void expectIsVibrationThread(boolean isVibrationThread) {
        if ((java.lang.Thread.currentThread() instanceof com.android.server.vibrator.VibrationThread) != isVibrationThread) {
            android.util.Slog.wtfStack("VibrationStepConductor", "Thread caller assertion failed, expected isVibrationThread=" + isVibrationThread);
        }
    }

    public com.android.server.vibrator.IVibrationStepConductorWrapper getWrapper() {
        return this.mVibrationStepConductorWrapper;
    }

    private class VibrationStepConductorWrapper implements com.android.server.vibrator.IVibrationStepConductorWrapper {
        private VibrationStepConductorWrapper() {
        }

        @Override // com.android.server.vibrator.IVibrationStepConductorWrapper
        public com.android.server.vibrator.IVibrationStepConductorExt getExtImpl() {
            return com.android.server.vibrator.VibrationStepConductor.this.mVibrationStepConductorExt;
        }

        @Override // com.android.server.vibrator.IVibrationStepConductorWrapper
        public void notifyVibrationAmplitudeUpdated() {
            android.util.Slog.d(com.android.server.vibrator.VibrationStepConductor.TAG, "notifyVibrationAmplitudeUpdated");
            synchronized (com.android.server.vibrator.VibrationStepConductor.this.mLock) {
                com.android.server.vibrator.VibrationStepConductor.this.mLock.notify();
            }
        }
    }
}
