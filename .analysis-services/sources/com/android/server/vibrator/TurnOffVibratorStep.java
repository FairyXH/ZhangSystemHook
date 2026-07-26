package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class TurnOffVibratorStep extends com.android.server.vibrator.AbstractVibratorStep {
    private final boolean mIsCleanUp;

    TurnOffVibratorStep(com.android.server.vibrator.VibrationStepConductor conductor, long startTime, com.android.server.vibrator.VibratorController controller, boolean isCleanUp) {
        super(conductor, startTime, controller, null, -1, startTime);
        this.mIsCleanUp = isCleanUp;
    }

    @Override // com.android.server.vibrator.Step
    public boolean isCleanUp() {
        return this.mIsCleanUp;
    }

    @Override // com.android.server.vibrator.AbstractVibratorStep, com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> cancel() {
        return java.util.Arrays.asList(new com.android.server.vibrator.TurnOffVibratorStep(this.conductor, android.os.SystemClock.uptimeMillis(), this.controller, true));
    }

    @Override // com.android.server.vibrator.AbstractVibratorStep, com.android.server.vibrator.Step
    public void cancelImmediately() {
        stopVibrating();
    }

    @Override // com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> play() {
        android.os.Trace.traceBegin(8388608L, "TurnOffVibratorStep");
        try {
            stopVibrating();
            return com.android.server.vibrator.VibrationStepConductor.EMPTY_STEP_LIST;
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }
}
