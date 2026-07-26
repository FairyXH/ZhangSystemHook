package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class RampOffVibratorStep extends com.android.server.vibrator.AbstractVibratorStep {
    private final float mAmplitudeDelta;
    private final float mAmplitudeTarget;

    RampOffVibratorStep(com.android.server.vibrator.VibrationStepConductor conductor, long startTime, float amplitudeTarget, float amplitudeDelta, com.android.server.vibrator.VibratorController controller, long pendingVibratorOffDeadline) {
        super(conductor, startTime, controller, null, -1, pendingVibratorOffDeadline);
        this.mAmplitudeTarget = amplitudeTarget;
        this.mAmplitudeDelta = amplitudeDelta;
    }

    @Override // com.android.server.vibrator.Step
    public boolean isCleanUp() {
        return true;
    }

    @Override // com.android.server.vibrator.AbstractVibratorStep, com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> cancel() {
        return java.util.Arrays.asList(new com.android.server.vibrator.TurnOffVibratorStep(this.conductor, android.os.SystemClock.uptimeMillis(), this.controller, true));
    }

    @Override // com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> play() {
        android.os.Trace.traceBegin(8388608L, "RampOffVibratorStep");
        try {
            if (com.android.server.vibrator.VibrationThread.DEBUG) {
                long latency = android.os.SystemClock.uptimeMillis() - this.startTime;
                android.util.Slog.d("VibrationThread", "Ramp down the vibrator amplitude, step with " + latency + "ms latency.");
            }
            if (this.mVibratorCompleteCallbackReceived) {
                stopVibrating();
                return com.android.server.vibrator.VibrationStepConductor.EMPTY_STEP_LIST;
            }
            changeAmplitude(this.mAmplitudeTarget);
            float newAmplitudeTarget = this.mAmplitudeTarget - this.mAmplitudeDelta;
            return newAmplitudeTarget < 0.001f ? java.util.Arrays.asList(new com.android.server.vibrator.TurnOffVibratorStep(this.conductor, this.mPendingVibratorOffDeadline, this.controller, true)) : java.util.Arrays.asList(new com.android.server.vibrator.RampOffVibratorStep(this.conductor, this.startTime + ((long) this.conductor.vibrationSettings.getRampStepDuration()), newAmplitudeTarget, this.mAmplitudeDelta, this.controller, this.mPendingVibratorOffDeadline));
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }
}
