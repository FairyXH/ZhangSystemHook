package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class CompleteEffectVibratorStep extends com.android.server.vibrator.AbstractVibratorStep {
    private final boolean mCancelled;

    CompleteEffectVibratorStep(com.android.server.vibrator.VibrationStepConductor conductor, long startTime, boolean cancelled, com.android.server.vibrator.VibratorController controller, long pendingVibratorOffDeadline) {
        super(conductor, startTime, controller, null, -1, pendingVibratorOffDeadline);
        this.mCancelled = cancelled;
    }

    @Override // com.android.server.vibrator.Step
    public boolean isCleanUp() {
        return this.mCancelled;
    }

    @Override // com.android.server.vibrator.AbstractVibratorStep, com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> cancel() {
        return this.mCancelled ? java.util.Arrays.asList(new com.android.server.vibrator.TurnOffVibratorStep(this.conductor, android.os.SystemClock.uptimeMillis(), this.controller, true)) : super.cancel();
    }

    @Override // com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> play() {
        android.os.Trace.traceBegin(8388608L, "CompleteEffectVibratorStep");
        try {
            if (com.android.server.vibrator.VibrationThread.DEBUG) {
                android.util.Slog.d("VibrationThread", "Running " + (this.mCancelled ? "cancel" : "complete") + " vibration step on vibrator " + this.controller.getVibratorInfo().getId());
            }
            if (this.mVibratorCompleteCallbackReceived) {
                stopVibrating();
                java.util.List<com.android.server.vibrator.Step> list = com.android.server.vibrator.VibrationStepConductor.EMPTY_STEP_LIST;
                android.os.Trace.traceEnd(8388608L);
                return list;
            }
            long now = android.os.SystemClock.uptimeMillis();
            float currentAmplitude = this.controller.getCurrentAmplitude();
            long remainingOnDuration = (this.mPendingVibratorOffDeadline - now) - 1000;
            long rampDownDuration = java.lang.Math.min(remainingOnDuration, this.conductor.vibrationSettings.getRampDownDuration());
            long stepDownDuration = this.conductor.vibrationSettings.getRampStepDuration();
            if (currentAmplitude < 0.001f || rampDownDuration <= stepDownDuration) {
                if (!this.mCancelled) {
                    return java.util.Arrays.asList(new com.android.server.vibrator.TurnOffVibratorStep(this.conductor, this.mPendingVibratorOffDeadline, this.controller, false));
                }
                stopVibrating();
                return com.android.server.vibrator.VibrationStepConductor.EMPTY_STEP_LIST;
            }
            if (com.android.server.vibrator.VibrationThread.DEBUG) {
                android.util.Slog.d("VibrationThread", "Ramping down vibrator " + this.controller.getVibratorInfo().getId() + " from amplitude " + currentAmplitude + " for " + rampDownDuration + "ms");
            }
            long rampOffVibratorOffDeadline = this.mCancelled ? now + rampDownDuration : this.mPendingVibratorOffDeadline;
            float amplitudeDelta = currentAmplitude / (rampDownDuration / stepDownDuration);
            float amplitudeTarget = currentAmplitude - amplitudeDelta;
            return java.util.Arrays.asList(new com.android.server.vibrator.RampOffVibratorStep(this.conductor, this.startTime, amplitudeTarget, amplitudeDelta, this.controller, rampOffVibratorOffDeadline));
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }
}
