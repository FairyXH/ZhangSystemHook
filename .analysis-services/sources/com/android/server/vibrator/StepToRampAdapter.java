package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class StepToRampAdapter implements com.android.server.vibrator.VibrationSegmentsAdapter {
    StepToRampAdapter() {
    }

    @Override // com.android.server.vibrator.VibrationSegmentsAdapter
    public int adaptToVibrator(android.os.VibratorInfo info, java.util.List<android.os.vibrator.VibrationEffectSegment> segments, int repeatIndex) {
        if (!info.hasCapability(1024L)) {
            return repeatIndex;
        }
        int segmentCount = segments.size();
        for (int i = 0; i < segmentCount; i++) {
            android.os.vibrator.StepSegment stepSegment = (android.os.vibrator.VibrationEffectSegment) segments.get(i);
            if (isStep(stepSegment) && stepSegment.getFrequencyHz() != 0.0f) {
                segments.set(i, convertStepToRamp(info, stepSegment));
            }
        }
        for (int i2 = 0; i2 < segmentCount; i2++) {
            if (segments.get(i2) instanceof android.os.vibrator.RampSegment) {
                for (int j = i2 - 1; j >= 0 && isStep(segments.get(j)); j--) {
                    segments.set(j, convertStepToRamp(info, segments.get(j)));
                }
                for (int j2 = i2 + 1; j2 < segmentCount && isStep(segments.get(j2)); j2++) {
                    segments.set(j2, convertStepToRamp(info, segments.get(j2)));
                }
            }
        }
        return repeatIndex;
    }

    private static android.os.vibrator.RampSegment convertStepToRamp(android.os.VibratorInfo info, android.os.vibrator.StepSegment segment) {
        float frequencyHz = fillEmptyFrequency(info, segment.getFrequencyHz());
        return new android.os.vibrator.RampSegment(segment.getAmplitude(), segment.getAmplitude(), frequencyHz, frequencyHz, (int) segment.getDuration());
    }

    private static boolean isStep(android.os.vibrator.VibrationEffectSegment segment) {
        return segment instanceof android.os.vibrator.StepSegment;
    }

    private static float fillEmptyFrequency(android.os.VibratorInfo info, float frequencyHz) {
        if (java.lang.Float.isNaN(info.getResonantFrequencyHz())) {
            return frequencyHz;
        }
        return frequencyHz == 0.0f ? info.getResonantFrequencyHz() : frequencyHz;
    }
}
