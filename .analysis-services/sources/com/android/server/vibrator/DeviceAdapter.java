package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class DeviceAdapter implements android.os.CombinedVibration.VibratorAdapter {
    private static final java.lang.String TAG = "DeviceAdapter";
    private final int[] mAvailableVibratorIds;
    private final android.util.SparseArray<com.android.server.vibrator.VibratorController> mAvailableVibrators;
    private final java.util.List<com.android.server.vibrator.VibrationSegmentsAdapter> mSegmentAdapters;

    DeviceAdapter(com.android.server.vibrator.VibrationSettings settings, android.util.SparseArray<com.android.server.vibrator.VibratorController> vibrators) {
        this.mSegmentAdapters = java.util.Arrays.asList(new com.android.server.vibrator.RampToStepAdapter(settings.getRampStepDuration()), new com.android.server.vibrator.StepToRampAdapter(), new com.android.server.vibrator.RampDownAdapter(settings.getRampDownDuration(), settings.getRampStepDuration()), new com.android.server.vibrator.SplitSegmentsAdapter(), new com.android.server.vibrator.ClippingAmplitudeAndFrequencyAdapter());
        this.mAvailableVibrators = vibrators;
        this.mAvailableVibratorIds = new int[vibrators.size()];
        for (int i = 0; i < vibrators.size(); i++) {
            this.mAvailableVibratorIds[i] = vibrators.keyAt(i);
        }
    }

    android.util.SparseArray<com.android.server.vibrator.VibratorController> getAvailableVibrators() {
        return this.mAvailableVibrators;
    }

    public int[] getAvailableVibratorIds() {
        return this.mAvailableVibratorIds;
    }

    public android.os.VibrationEffect adaptToVibrator(int vibratorId, android.os.VibrationEffect effect) {
        if (!(effect instanceof android.os.VibrationEffect.Composed)) {
            android.util.Slog.wtf(TAG, "Error adapting unsupported vibration effect: " + effect);
            return effect;
        }
        com.android.server.vibrator.VibratorController controller = this.mAvailableVibrators.get(vibratorId);
        if (controller == null) {
            return effect;
        }
        android.os.VibratorInfo info = controller.getVibratorInfo();
        android.os.VibrationEffect.Composed composed = (android.os.VibrationEffect.Composed) effect;
        java.util.List<android.os.vibrator.VibrationEffectSegment> newSegments = new java.util.ArrayList<>(composed.getSegments());
        int newRepeatIndex = composed.getRepeatIndex();
        int adapterCount = this.mSegmentAdapters.size();
        for (int i = 0; i < adapterCount; i++) {
            newRepeatIndex = this.mSegmentAdapters.get(i).adaptToVibrator(info, newSegments, newRepeatIndex);
        }
        return new android.os.VibrationEffect.Composed(newSegments, newRepeatIndex);
    }
}
