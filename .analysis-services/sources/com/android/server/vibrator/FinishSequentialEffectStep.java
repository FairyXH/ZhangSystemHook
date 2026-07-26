package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class FinishSequentialEffectStep extends com.android.server.vibrator.Step {
    public final com.android.server.vibrator.StartSequentialEffectStep startedStep;

    FinishSequentialEffectStep(com.android.server.vibrator.StartSequentialEffectStep startedStep) {
        super(startedStep.conductor, Long.MAX_VALUE);
        this.startedStep = startedStep;
    }

    @Override // com.android.server.vibrator.Step
    public boolean isCleanUp() {
        return true;
    }

    @Override // com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> play() {
        android.os.Trace.traceBegin(8388608L, "FinishSequentialEffectStep");
        try {
            if (com.android.server.vibrator.VibrationThread.DEBUG) {
                android.util.Slog.d("VibrationThread", "FinishSequentialEffectStep for effect #" + this.startedStep.currentIndex);
            }
            this.conductor.vibratorManagerHooks.noteVibratorOff(this.conductor.getVibration().callerInfo.uid);
            com.android.server.vibrator.Step nextStep = this.startedStep.nextStep();
            return nextStep == null ? com.android.server.vibrator.VibrationStepConductor.EMPTY_STEP_LIST : java.util.Arrays.asList(nextStep);
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }

    @Override // com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> cancel() {
        cancelImmediately();
        return com.android.server.vibrator.VibrationStepConductor.EMPTY_STEP_LIST;
    }

    @Override // com.android.server.vibrator.Step
    public void cancelImmediately() {
        this.conductor.vibratorManagerHooks.noteVibratorOff(this.conductor.getVibration().callerInfo.uid);
    }
}
