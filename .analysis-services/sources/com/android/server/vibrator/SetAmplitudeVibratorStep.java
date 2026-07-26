package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class SetAmplitudeVibratorStep extends com.android.server.vibrator.AbstractVibratorStep {
    static final int REPEATING_EFFECT_ON_DURATION = 5000;
    private com.android.server.vibrator.ISetAmplitudeVibratorStepWrapper mSetAmplitudeVibratorStepWrapper;

    SetAmplitudeVibratorStep(com.android.server.vibrator.VibrationStepConductor conductor, long startTime, com.android.server.vibrator.VibratorController controller, android.os.VibrationEffect.Composed effect, int index, long pendingVibratorOffDeadline) {
        super(conductor, startTime, controller, effect, index, pendingVibratorOffDeadline);
        this.mSetAmplitudeVibratorStepWrapper = new com.android.server.vibrator.SetAmplitudeVibratorStep.SetAmplitudeVibratorStepWrapper();
    }

    @Override // com.android.server.vibrator.AbstractVibratorStep, com.android.server.vibrator.Step
    public boolean acceptVibratorCompleteCallback(int vibratorId) {
        boolean z = false;
        if (!super.acceptVibratorCompleteCallback(vibratorId)) {
            return false;
        }
        if (android.os.SystemClock.uptimeMillis() < this.startTime && this.controller.getCurrentAmplitude() > 0.0f) {
            z = true;
        }
        boolean shouldAcceptCallback = z;
        if (com.android.server.vibrator.VibrationThread.DEBUG) {
            android.util.Slog.d("VibrationThread", "Amplitude step received completion callback from " + vibratorId + ", accepted = " + shouldAcceptCallback);
        }
        return shouldAcceptCallback;
    }

    @Override // com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> play() {
        android.os.Trace.traceBegin(8388608L, "SetAmplitudeVibratorStep");
        try {
            long now = android.os.SystemClock.uptimeMillis();
            long latency = now - this.startTime;
            if (com.android.server.vibrator.VibrationThread.DEBUG) {
                android.util.Slog.d("VibrationThread", "Running amplitude step with " + latency + "ms latency.");
            }
            if (this.mVibratorCompleteCallbackReceived && latency < 0) {
                turnVibratorBackOn(-latency);
                return java.util.Arrays.asList(new com.android.server.vibrator.SetAmplitudeVibratorStep(this.conductor, this.startTime, this.controller, this.effect, this.segmentIndex, this.mPendingVibratorOffDeadline));
            }
            android.os.vibrator.StepSegment stepSegment = (android.os.vibrator.VibrationEffectSegment) this.effect.getSegments().get(this.segmentIndex);
            if (!(stepSegment instanceof android.os.vibrator.StepSegment)) {
                android.util.Slog.w("VibrationThread", "Ignoring wrong segment for a SetAmplitudeVibratorStep: " + stepSegment);
                return nextSteps(this.startTime, 1);
            }
            android.os.vibrator.StepSegment stepSegment2 = stepSegment;
            if (stepSegment2.getDuration() == 0) {
                return nextSteps(this.startTime, 1);
            }
            float amplitude = stepSegment2.getAmplitude();
            if (amplitude != 0.0f) {
                if (this.startTime >= this.mPendingVibratorOffDeadline) {
                    long onDuration = getVibratorOnDuration(this.effect, this.segmentIndex);
                    if (onDuration > 0) {
                        startVibrating(onDuration);
                    }
                }
                changeAmplitude(this.conductor.getWrapper().getExtImpl().getVibrationAmplitudeRatio() * amplitude);
            } else if (this.mPendingVibratorOffDeadline > now) {
                stopVibrating();
            }
            long nextStartTime = this.startTime + stepSegment.getDuration();
            return nextSteps(nextStartTime, 1);
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }

    private void turnVibratorBackOn(long remainingDuration) {
        long onDuration = getVibratorOnDuration(this.effect, this.segmentIndex);
        if (onDuration <= 0) {
            return;
        }
        long onDuration2 = onDuration + remainingDuration;
        if (com.android.server.vibrator.VibrationThread.DEBUG) {
            android.util.Slog.d("VibrationThread", "Turning the vibrator back ON using the remaining duration of " + remainingDuration + "ms, for a total of " + onDuration2 + "ms");
        }
        float expectedAmplitude = this.controller.getCurrentAmplitude();
        long vibratorOnResult = startVibrating(onDuration2);
        if (vibratorOnResult > 0) {
            changeAmplitude(expectedAmplitude);
        }
    }

    private long startVibrating(long duration) {
        if (com.android.server.vibrator.VibrationThread.DEBUG) {
            android.util.Slog.d("VibrationThread", "Turning on vibrator " + this.controller.getVibratorInfo().getId() + " for " + duration + "ms");
        }
        long vibratorOnResult = this.controller.on(duration, getVibration().id);
        handleVibratorOnResult(vibratorOnResult);
        getVibration().stats.reportVibratorOn(vibratorOnResult);
        return vibratorOnResult;
    }

    private long getVibratorOnDuration(android.os.VibrationEffect.Composed effect, int startIndex) {
        java.util.List<android.os.vibrator.VibrationEffectSegment> segments = effect.getSegments();
        int segmentCount = segments.size();
        int repeatIndex = effect.getRepeatIndex();
        int i = startIndex;
        long timing = 0;
        while (i < segmentCount) {
            android.os.vibrator.VibrationEffectSegment vibrationEffectSegment = segments.get(i);
            if (!(vibrationEffectSegment instanceof android.os.vibrator.StepSegment) || (vibrationEffectSegment.getDuration() > 0 && ((android.os.vibrator.StepSegment) vibrationEffectSegment).getAmplitude() == 0.0f)) {
                break;
            }
            timing += vibrationEffectSegment.getDuration();
            i++;
            if (i == segmentCount && repeatIndex >= 0) {
                i = repeatIndex;
                repeatIndex = -1;
            }
            if (i == startIndex) {
                return java.lang.Math.max(timing, 5000L);
            }
        }
        if (i == segmentCount && effect.getRepeatIndex() < 0) {
            return timing + ((long) this.conductor.vibrationSettings.getRampDownDuration());
        }
        return timing;
    }

    public com.android.server.vibrator.ISetAmplitudeVibratorStepWrapper getWrapper() {
        return this.mSetAmplitudeVibratorStepWrapper;
    }

    private class SetAmplitudeVibratorStepWrapper implements com.android.server.vibrator.ISetAmplitudeVibratorStepWrapper {
        private SetAmplitudeVibratorStepWrapper() {
        }

        @Override // com.android.server.vibrator.ISetAmplitudeVibratorStepWrapper
        public void updateVibrationAmplitude(float amplitudeRatio) {
            android.os.vibrator.StepSegment stepSegment = (android.os.vibrator.VibrationEffectSegment) com.android.server.vibrator.SetAmplitudeVibratorStep.this.effect.getSegments().get(com.android.server.vibrator.SetAmplitudeVibratorStep.this.segmentIndex);
            if (stepSegment instanceof android.os.vibrator.StepSegment) {
                android.os.vibrator.StepSegment stepSegment2 = stepSegment;
                float amplitude = stepSegment2.getAmplitude();
                if (amplitude > 0.0f) {
                    com.android.server.vibrator.SetAmplitudeVibratorStep.this.changeAmplitude(amplitude * amplitudeRatio);
                }
            }
        }
    }
}
