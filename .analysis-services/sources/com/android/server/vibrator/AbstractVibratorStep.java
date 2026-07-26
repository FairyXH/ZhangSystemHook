package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
abstract class AbstractVibratorStep extends com.android.server.vibrator.Step {
    public final com.android.server.vibrator.VibratorController controller;
    public final android.os.VibrationEffect.Composed effect;
    long mPendingVibratorOffDeadline;
    boolean mVibratorCompleteCallbackReceived;
    long mVibratorOnResult;
    public final int segmentIndex;

    AbstractVibratorStep(com.android.server.vibrator.VibrationStepConductor conductor, long startTime, com.android.server.vibrator.VibratorController controller, android.os.VibrationEffect.Composed effect, int index, long pendingVibratorOffDeadline) {
        super(conductor, startTime);
        this.controller = controller;
        this.effect = effect;
        this.segmentIndex = index;
        this.mPendingVibratorOffDeadline = pendingVibratorOffDeadline;
    }

    public int getVibratorId() {
        return this.controller.getVibratorInfo().getId();
    }

    @Override // com.android.server.vibrator.Step
    public long getVibratorOnDuration() {
        return this.mVibratorOnResult;
    }

    @Override // com.android.server.vibrator.Step
    public boolean acceptVibratorCompleteCallback(int vibratorId) {
        if (getVibratorId() != vibratorId) {
            return false;
        }
        boolean shouldAcceptCallback = this.mPendingVibratorOffDeadline > android.os.SystemClock.uptimeMillis();
        if (com.android.server.vibrator.VibrationThread.DEBUG) {
            android.util.Slog.d("VibrationThread", "Received completion callback from " + vibratorId + ", accepted = " + shouldAcceptCallback);
        }
        this.mPendingVibratorOffDeadline = 0L;
        this.mVibratorCompleteCallbackReceived = true;
        return shouldAcceptCallback;
    }

    @Override // com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> cancel() {
        return java.util.Arrays.asList(new com.android.server.vibrator.CompleteEffectVibratorStep(this.conductor, android.os.SystemClock.uptimeMillis(), true, this.controller, this.mPendingVibratorOffDeadline));
    }

    @Override // com.android.server.vibrator.Step
    public void cancelImmediately() {
        if (this.mPendingVibratorOffDeadline > android.os.SystemClock.uptimeMillis()) {
            stopVibrating();
        }
    }

    protected long handleVibratorOnResult(long vibratorOnResult) {
        this.mVibratorOnResult = vibratorOnResult;
        if (com.android.server.vibrator.VibrationThread.DEBUG) {
            android.util.Slog.d("VibrationThread", "Turned on vibrator " + getVibratorId() + ", result = " + this.mVibratorOnResult);
        }
        if (this.mVibratorOnResult > 0) {
            this.mPendingVibratorOffDeadline = android.os.SystemClock.uptimeMillis() + this.mVibratorOnResult + 1000;
        } else {
            this.mPendingVibratorOffDeadline = 0L;
        }
        return this.mVibratorOnResult;
    }

    protected void stopVibrating() {
        if (com.android.server.vibrator.VibrationThread.DEBUG) {
            android.util.Slog.d("VibrationThread", "Turning off vibrator " + getVibratorId());
        }
        this.controller.off();
        getVibration().stats.reportVibratorOff();
        this.mPendingVibratorOffDeadline = 0L;
    }

    protected void changeAmplitude(float amplitude) {
        if (com.android.server.vibrator.VibrationThread.DEBUG) {
            android.util.Slog.d("VibrationThread", "Amplitude changed on vibrator " + getVibratorId() + " to " + amplitude);
        }
        this.controller.setAmplitude(amplitude);
        getVibration().stats.reportSetAmplitude();
    }

    protected java.util.List<com.android.server.vibrator.Step> nextSteps(int segmentsPlayed) {
        long nextStartTime = android.os.SystemClock.uptimeMillis();
        if (this.mVibratorOnResult > 0) {
            nextStartTime += this.mVibratorOnResult;
        }
        return nextSteps(nextStartTime, segmentsPlayed);
    }

    protected java.util.List<com.android.server.vibrator.Step> nextSteps(long nextStartTime, int segmentsPlayed) {
        int nextSegmentIndex = this.segmentIndex + segmentsPlayed;
        int effectSize = this.effect.getSegments().size();
        int repeatIndex = this.effect.getRepeatIndex();
        if (nextSegmentIndex >= effectSize && repeatIndex >= 0) {
            int loopSize = effectSize - repeatIndex;
            int loopSegmentsPlayed = nextSegmentIndex - repeatIndex;
            getVibration().stats.reportRepetition(loopSegmentsPlayed / loopSize);
            nextSegmentIndex = repeatIndex + ((nextSegmentIndex - effectSize) % loopSize);
        }
        com.android.server.vibrator.Step nextStep = this.conductor.nextVibrateStep(nextStartTime, this.controller, this.effect, nextSegmentIndex, this.mPendingVibratorOffDeadline);
        return nextStep == null ? com.android.server.vibrator.VibrationStepConductor.EMPTY_STEP_LIST : java.util.Arrays.asList(nextStep);
    }
}
