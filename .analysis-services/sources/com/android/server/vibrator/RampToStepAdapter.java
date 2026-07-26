package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class RampToStepAdapter implements com.android.server.vibrator.VibrationSegmentsAdapter {
    private final int mStepDuration;

    RampToStepAdapter(int stepDuration) {
        this.mStepDuration = stepDuration;
    }

    @Override // com.android.server.vibrator.VibrationSegmentsAdapter
    public int adaptToVibrator(android.os.VibratorInfo info, java.util.List<android.os.vibrator.VibrationEffectSegment> segments, int repeatIndex) {
        if (info.hasCapability(1024L)) {
            return repeatIndex;
        }
        int segmentCount = segments.size();
        int i = 0;
        while (i < segmentCount) {
            android.os.vibrator.VibrationEffectSegment segment = segments.get(i);
            if (segment instanceof android.os.vibrator.RampSegment) {
                java.util.List<android.os.vibrator.StepSegment> steps = convertRampToSteps(info, (android.os.vibrator.RampSegment) segment);
                segments.remove(i);
                segments.addAll(i, steps);
                int addedSegments = steps.size() - 1;
                if (repeatIndex > i) {
                    repeatIndex += addedSegments;
                }
                i += addedSegments;
                segmentCount += addedSegments;
            }
            i++;
        }
        return repeatIndex;
    }

    private java.util.List<android.os.vibrator.StepSegment> convertRampToSteps(android.os.VibratorInfo info, android.os.vibrator.RampSegment ramp) {
        if (java.lang.Float.compare(ramp.getStartAmplitude(), ramp.getEndAmplitude()) == 0) {
            return java.util.Arrays.asList(new android.os.vibrator.StepSegment(ramp.getStartAmplitude(), fillEmptyFrequency(info, ramp.getStartFrequencyHz()), (int) ramp.getDuration()));
        }
        java.util.List<android.os.vibrator.StepSegment> steps = new java.util.ArrayList<>();
        int stepCount = ((int) ((ramp.getDuration() + ((long) this.mStepDuration)) - 1)) / this.mStepDuration;
        for (int i = 0; i < stepCount - 1; i++) {
            float pos = i / stepCount;
            float startFrequencyHz = fillEmptyFrequency(info, ramp.getStartFrequencyHz());
            float endFrequencyHz = fillEmptyFrequency(info, ramp.getEndFrequencyHz());
            steps.add(new android.os.vibrator.StepSegment(android.util.MathUtils.lerp(ramp.getStartAmplitude(), ramp.getEndAmplitude(), pos), android.util.MathUtils.lerp(startFrequencyHz, endFrequencyHz, pos), this.mStepDuration));
        }
        int duration = ((int) ramp.getDuration()) - (this.mStepDuration * (stepCount - 1));
        float endFrequencyHz2 = fillEmptyFrequency(info, ramp.getEndFrequencyHz());
        steps.add(new android.os.vibrator.StepSegment(ramp.getEndAmplitude(), endFrequencyHz2, duration));
        return steps;
    }

    private static float fillEmptyFrequency(android.os.VibratorInfo info, float frequencyHz) {
        if (java.lang.Float.isNaN(info.getResonantFrequencyHz())) {
            return 0.0f;
        }
        return frequencyHz == 0.0f ? info.getResonantFrequencyHz() : frequencyHz;
    }
}
