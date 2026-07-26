package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class PerformPrebakedVibratorStep extends com.android.server.vibrator.AbstractVibratorStep {
    PerformPrebakedVibratorStep(com.android.server.vibrator.VibrationStepConductor conductor, long startTime, com.android.server.vibrator.VibratorController controller, android.os.VibrationEffect.Composed effect, int index, long pendingVibratorOffDeadline) {
        super(conductor, java.lang.Math.max(startTime, pendingVibratorOffDeadline), controller, effect, index, pendingVibratorOffDeadline);
    }

    @Override // com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> play() {
        android.os.Trace.traceBegin(8388608L, "PerformPrebakedVibratorStep");
        try {
            android.os.vibrator.PrebakedSegment prebakedSegment = (android.os.vibrator.VibrationEffectSegment) this.effect.getSegments().get(this.segmentIndex);
            if (!(prebakedSegment instanceof android.os.vibrator.PrebakedSegment)) {
                android.util.Slog.w("VibrationThread", "Ignoring wrong segment for a PerformPrebakedVibratorStep: " + prebakedSegment);
                return nextSteps(1);
            }
            android.os.vibrator.PrebakedSegment prebaked = prebakedSegment;
            if (com.android.server.vibrator.VibrationThread.DEBUG) {
                android.util.Slog.d("VibrationThread", "Perform " + android.os.VibrationEffect.effectIdToString(prebaked.getEffectId()) + " on vibrator " + this.controller.getVibratorInfo().getId());
            }
            android.os.VibrationEffect fallback = getVibration().getFallback(prebaked.getEffectId());
            long vibratorOnResult = this.controller.on(prebaked, getVibration().id);
            handleVibratorOnResult(vibratorOnResult);
            getVibration().stats.reportPerformEffect(vibratorOnResult, prebaked);
            if (vibratorOnResult != 0 || !prebaked.shouldFallback() || !(fallback instanceof android.os.VibrationEffect.Composed)) {
                return nextSteps(1);
            }
            if (com.android.server.vibrator.VibrationThread.DEBUG) {
                android.util.Slog.d("VibrationThread", "Playing fallback for effect " + android.os.VibrationEffect.effectIdToString(prebaked.getEffectId()));
            }
            com.android.server.vibrator.AbstractVibratorStep fallbackStep = this.conductor.nextVibrateStep(this.startTime, this.controller, replaceCurrentSegment((android.os.VibrationEffect.Composed) fallback), this.segmentIndex, this.mPendingVibratorOffDeadline);
            java.util.List<com.android.server.vibrator.Step> fallbackResult = fallbackStep.play();
            handleVibratorOnResult(fallbackStep.getVibratorOnDuration());
            return fallbackResult;
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }

    private android.os.VibrationEffect.Composed replaceCurrentSegment(android.os.VibrationEffect.Composed fallback) {
        java.util.List<android.os.vibrator.VibrationEffectSegment> newSegments = new java.util.ArrayList<>(this.effect.getSegments());
        int newRepeatIndex = this.effect.getRepeatIndex();
        newSegments.remove(this.segmentIndex);
        newSegments.addAll(this.segmentIndex, fallback.getSegments());
        if (this.segmentIndex < this.effect.getRepeatIndex()) {
            newRepeatIndex += fallback.getSegments().size() - 1;
        }
        return new android.os.VibrationEffect.Composed(newSegments, newRepeatIndex);
    }
}
