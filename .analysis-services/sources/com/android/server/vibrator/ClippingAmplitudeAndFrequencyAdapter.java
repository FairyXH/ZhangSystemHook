package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class ClippingAmplitudeAndFrequencyAdapter implements com.android.server.vibrator.VibrationSegmentsAdapter {
    ClippingAmplitudeAndFrequencyAdapter() {
    }

    @Override // com.android.server.vibrator.VibrationSegmentsAdapter
    public int adaptToVibrator(android.os.VibratorInfo info, java.util.List<android.os.vibrator.VibrationEffectSegment> segments, int repeatIndex) {
        int segmentCount = segments.size();
        for (int i = 0; i < segmentCount; i++) {
            android.os.vibrator.VibrationEffectSegment segment = segments.get(i);
            if (segment instanceof android.os.vibrator.RampSegment) {
                segments.set(i, adaptToVibrator(info, (android.os.vibrator.RampSegment) segment));
            }
        }
        return repeatIndex;
    }

    private android.os.vibrator.RampSegment adaptToVibrator(android.os.VibratorInfo info, android.os.vibrator.RampSegment segment) {
        float clampedStartFrequency = clampFrequency(info, segment.getStartFrequencyHz());
        float clampedEndFrequency = clampFrequency(info, segment.getEndFrequencyHz());
        return new android.os.vibrator.RampSegment(clampAmplitude(info, clampedStartFrequency, segment.getStartAmplitude()), clampAmplitude(info, clampedEndFrequency, segment.getEndAmplitude()), clampedStartFrequency, clampedEndFrequency, (int) segment.getDuration());
    }

    private float clampFrequency(android.os.VibratorInfo info, float frequencyHz) {
        android.util.Range<java.lang.Float> frequencyRangeHz = info.getFrequencyProfile().getFrequencyRangeHz();
        if (frequencyHz == 0.0f || frequencyRangeHz == null) {
            if (java.lang.Float.isNaN(info.getResonantFrequencyHz())) {
                return 0.0f;
            }
            return info.getResonantFrequencyHz();
        }
        return ((java.lang.Float) frequencyRangeHz.clamp(java.lang.Float.valueOf(frequencyHz))).floatValue();
    }

    private float clampAmplitude(android.os.VibratorInfo info, float frequencyHz, float amplitude) {
        android.os.VibratorInfo.FrequencyProfile mapping = info.getFrequencyProfile();
        if (mapping.isEmpty()) {
            return amplitude;
        }
        return android.util.MathUtils.min(amplitude, mapping.getMaxAmplitude(frequencyHz));
    }
}
