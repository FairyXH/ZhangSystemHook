package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class RampDownAdapter implements com.android.server.vibrator.VibrationSegmentsAdapter {
    private final int mRampDownDuration;
    private final int mStepDuration;

    RampDownAdapter(int rampDownDuration, int stepDuration) {
        this.mRampDownDuration = rampDownDuration;
        this.mStepDuration = stepDuration;
    }

    @Override // com.android.server.vibrator.VibrationSegmentsAdapter
    public int adaptToVibrator(android.os.VibratorInfo info, java.util.List<android.os.vibrator.VibrationEffectSegment> segments, int repeatIndex) {
        if (this.mRampDownDuration <= 0) {
            return repeatIndex;
        }
        return addRampDownToLoop(segments, addRampDownToZeroAmplitudeSegments(segments, repeatIndex));
    }

    private int addRampDownToZeroAmplitudeSegments(java.util.List<android.os.vibrator.VibrationEffectSegment> segments, int repeatIndex) {
        int segmentCount = segments.size();
        int i = 1;
        while (i < segmentCount) {
            android.os.vibrator.StepSegment stepSegment = (android.os.vibrator.VibrationEffectSegment) segments.get(i - 1);
            if (isOffSegment(segments.get(i)) && endsWithNonZeroAmplitude(stepSegment)) {
                java.util.List<android.os.vibrator.VibrationEffectSegment> replacementSegments = null;
                long offDuration = segments.get(i).getDuration();
                if (stepSegment instanceof android.os.vibrator.StepSegment) {
                    replacementSegments = createStepsDown(stepSegment.getAmplitude(), stepSegment.getFrequencyHz(), offDuration);
                } else if (stepSegment instanceof android.os.vibrator.RampSegment) {
                    float previousAmplitude = ((android.os.vibrator.RampSegment) stepSegment).getEndAmplitude();
                    float previousFrequency = ((android.os.vibrator.RampSegment) stepSegment).getEndFrequencyHz();
                    if (offDuration <= this.mRampDownDuration) {
                        replacementSegments = java.util.Arrays.asList(createRampDown(previousAmplitude, previousFrequency, offDuration));
                    } else {
                        replacementSegments = java.util.Arrays.asList(createRampDown(previousAmplitude, previousFrequency, this.mRampDownDuration), createRampDown(0.0f, previousFrequency, offDuration - ((long) this.mRampDownDuration)));
                    }
                }
                if (replacementSegments != null) {
                    int segmentsAdded = replacementSegments.size() - 1;
                    android.os.vibrator.VibrationEffectSegment originalOffSegment = segments.remove(i);
                    segments.addAll(i, replacementSegments);
                    if (repeatIndex >= i) {
                        if (repeatIndex == i) {
                            segments.add(originalOffSegment);
                            repeatIndex++;
                            segmentCount++;
                        }
                        repeatIndex += segmentsAdded;
                    }
                    i += segmentsAdded;
                    segmentCount += segmentsAdded;
                }
            }
            i++;
        }
        return repeatIndex;
    }

    private int addRampDownToLoop(java.util.List<android.os.vibrator.VibrationEffectSegment> segments, int repeatIndex) {
        if (repeatIndex < 0) {
            return repeatIndex;
        }
        int segmentCount = segments.size();
        if (!endsWithNonZeroAmplitude(segments.get(segmentCount - 1)) || !isOffSegment(segments.get(repeatIndex))) {
            return repeatIndex;
        }
        android.os.vibrator.StepSegment stepSegment = (android.os.vibrator.VibrationEffectSegment) segments.get(segmentCount - 1);
        android.os.vibrator.VibrationEffectSegment offSegment = segments.get(repeatIndex);
        long offDuration = offSegment.getDuration();
        if (offDuration > this.mRampDownDuration) {
            segments.set(repeatIndex, updateDuration(offSegment, offDuration - ((long) this.mRampDownDuration)));
            segments.add(repeatIndex, updateDuration(offSegment, this.mRampDownDuration));
        }
        int repeatIndex2 = repeatIndex + 1;
        if (stepSegment instanceof android.os.vibrator.StepSegment) {
            float previousAmplitude = stepSegment.getAmplitude();
            float previousFrequency = stepSegment.getFrequencyHz();
            segments.addAll(createStepsDown(previousAmplitude, previousFrequency, java.lang.Math.min(offDuration, this.mRampDownDuration)));
        } else if (stepSegment instanceof android.os.vibrator.RampSegment) {
            float previousAmplitude2 = ((android.os.vibrator.RampSegment) stepSegment).getEndAmplitude();
            float previousFrequency2 = ((android.os.vibrator.RampSegment) stepSegment).getEndFrequencyHz();
            segments.add(createRampDown(previousAmplitude2, previousFrequency2, java.lang.Math.min(offDuration, this.mRampDownDuration)));
        }
        return repeatIndex2;
    }

    private java.util.List<android.os.vibrator.VibrationEffectSegment> createStepsDown(float amplitude, float frequency, long duration) {
        int stepCount = ((int) java.lang.Math.min(duration, this.mRampDownDuration)) / this.mStepDuration;
        float amplitudeStep = amplitude / stepCount;
        java.util.List<android.os.vibrator.VibrationEffectSegment> steps = new java.util.ArrayList<>();
        for (int i = 1; i < stepCount; i++) {
            steps.add(new android.os.vibrator.StepSegment(amplitude - (i * amplitudeStep), frequency, this.mStepDuration));
        }
        int i2 = (int) duration;
        int remainingDuration = i2 - (this.mStepDuration * (stepCount - 1));
        steps.add(new android.os.vibrator.StepSegment(0.0f, frequency, remainingDuration));
        return steps;
    }

    private static android.os.vibrator.RampSegment createRampDown(float amplitude, float frequency, long duration) {
        return new android.os.vibrator.RampSegment(amplitude, 0.0f, frequency, frequency, (int) duration);
    }

    private static android.os.vibrator.VibrationEffectSegment updateDuration(android.os.vibrator.VibrationEffectSegment segment, long newDuration) {
        if (segment instanceof android.os.vibrator.RampSegment) {
            android.os.vibrator.RampSegment ramp = (android.os.vibrator.RampSegment) segment;
            return new android.os.vibrator.RampSegment(ramp.getStartAmplitude(), ramp.getEndAmplitude(), ramp.getStartFrequencyHz(), ramp.getEndFrequencyHz(), (int) newDuration);
        }
        if (segment instanceof android.os.vibrator.StepSegment) {
            android.os.vibrator.StepSegment step = (android.os.vibrator.StepSegment) segment;
            return new android.os.vibrator.StepSegment(step.getAmplitude(), step.getFrequencyHz(), (int) newDuration);
        }
        return segment;
    }

    private static boolean isOffSegment(android.os.vibrator.VibrationEffectSegment segment) {
        if (segment instanceof android.os.vibrator.StepSegment) {
            return ((android.os.vibrator.StepSegment) segment).getAmplitude() == 0.0f;
        }
        if (!(segment instanceof android.os.vibrator.RampSegment)) {
            return false;
        }
        android.os.vibrator.RampSegment ramp = (android.os.vibrator.RampSegment) segment;
        return ramp.getStartAmplitude() == 0.0f && ramp.getEndAmplitude() == 0.0f;
    }

    private static boolean endsWithNonZeroAmplitude(android.os.vibrator.VibrationEffectSegment segment) {
        return segment instanceof android.os.vibrator.StepSegment ? ((android.os.vibrator.StepSegment) segment).getAmplitude() != 0.0f : (segment instanceof android.os.vibrator.RampSegment) && ((android.os.vibrator.RampSegment) segment).getEndAmplitude() != 0.0f;
    }
}
