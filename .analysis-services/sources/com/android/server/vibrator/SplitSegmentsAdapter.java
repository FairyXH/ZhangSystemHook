package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class SplitSegmentsAdapter implements com.android.server.vibrator.VibrationSegmentsAdapter {
    SplitSegmentsAdapter() {
    }

    @Override // com.android.server.vibrator.VibrationSegmentsAdapter
    public int adaptToVibrator(android.os.VibratorInfo info, java.util.List<android.os.vibrator.VibrationEffectSegment> segments, int repeatIndex) {
        int maxRampDuration;
        if (!info.hasCapability(1024L) || (maxRampDuration = info.getPwlePrimitiveDurationMax()) <= 0) {
            return repeatIndex;
        }
        int segmentCount = segments.size();
        int i = 0;
        while (i < segmentCount) {
            if (segments.get(i) instanceof android.os.vibrator.RampSegment) {
                android.os.vibrator.RampSegment ramp = segments.get(i);
                int splits = ((((int) ramp.getDuration()) + maxRampDuration) - 1) / maxRampDuration;
                if (splits > 1) {
                    segments.remove(i);
                    segments.addAll(i, splitRampSegment(info, ramp, splits));
                    int addedSegments = splits - 1;
                    if (repeatIndex > i) {
                        repeatIndex += addedSegments;
                    }
                    i += addedSegments;
                    segmentCount += addedSegments;
                }
            }
            i++;
        }
        return repeatIndex;
    }

    private static java.util.List<android.os.vibrator.RampSegment> splitRampSegment(android.os.VibratorInfo info, android.os.vibrator.RampSegment ramp, int splits) {
        java.util.List<android.os.vibrator.RampSegment> ramps = new java.util.ArrayList<>(splits);
        float startFrequencyHz = fillEmptyFrequency(info, ramp.getStartFrequencyHz());
        float endFrequencyHz = fillEmptyFrequency(info, ramp.getEndFrequencyHz());
        long splitDuration = ramp.getDuration() / ((long) splits);
        float previousAmplitude = ramp.getStartAmplitude();
        int i = 1;
        float previousAmplitude2 = previousAmplitude;
        float previousFrequencyHz = startFrequencyHz;
        long accumulatedDuration = 0;
        while (i < splits) {
            long accumulatedDuration2 = accumulatedDuration + splitDuration;
            float durationRatio = accumulatedDuration2 / ramp.getDuration();
            float interpolatedFrequency = android.util.MathUtils.lerp(startFrequencyHz, endFrequencyHz, durationRatio);
            float interpolatedAmplitude = android.util.MathUtils.lerp(ramp.getStartAmplitude(), ramp.getEndAmplitude(), durationRatio);
            android.os.vibrator.RampSegment rampSplit = new android.os.vibrator.RampSegment(previousAmplitude2, interpolatedAmplitude, previousFrequencyHz, interpolatedFrequency, (int) splitDuration);
            ramps.add(rampSplit);
            previousAmplitude2 = rampSplit.getEndAmplitude();
            previousFrequencyHz = rampSplit.getEndFrequencyHz();
            i++;
            accumulatedDuration = accumulatedDuration2;
        }
        ramps.add(new android.os.vibrator.RampSegment(previousAmplitude2, ramp.getEndAmplitude(), previousFrequencyHz, endFrequencyHz, (int) (ramp.getDuration() - accumulatedDuration)));
        return ramps;
    }

    private static float fillEmptyFrequency(android.os.VibratorInfo info, float frequencyHz) {
        if (java.lang.Float.isNaN(info.getResonantFrequencyHz())) {
            return frequencyHz;
        }
        return frequencyHz == 0.0f ? info.getResonantFrequencyHz() : frequencyHz;
    }
}
