package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class VibrationStats {
    static final java.lang.String TAG = "VibrationStats";
    private float mAdaptiveScale;
    private long mEndTimeDebug;
    private long mEndUptimeMillis;
    private int mRepeatCount;
    private long mStartTimeDebug;
    private long mStartUptimeMillis;
    private int mVibrationCompositionTotalSize;
    private int mVibrationPwleTotalSize;
    private int mVibratorComposeCount;
    private int mVibratorComposePwleCount;
    private int mVibratorOffCount;
    private int mVibratorOnCount;
    private int mVibratorOnTotalDurationMillis;
    private int mVibratorPerformCount;
    private int mVibratorSetAmplitudeCount;
    private int mVibratorSetExternalControlCount;
    private android.util.SparseBooleanArray mVibratorEffectsUsed = new android.util.SparseBooleanArray();
    private android.util.SparseBooleanArray mVibratorPrimitivesUsed = new android.util.SparseBooleanArray();
    private final com.android.server.vibrator.IVibrationStatsWrapper mVibrationSettingsWrapper = new com.android.server.vibrator.VibrationStats.VibrationStatsWrapper();
    private long mCreateUptimeMillis = android.os.SystemClock.uptimeMillis();
    private long mCreateTimeDebug = java.lang.System.currentTimeMillis();
    private int mEndedByUid = -1;
    private int mEndedByUsage = -1;
    private int mInterruptedUsage = -1;

    VibrationStats() {
    }

    long getCreateUptimeMillis() {
        return this.mCreateUptimeMillis;
    }

    long getStartUptimeMillis() {
        return this.mStartUptimeMillis;
    }

    long getEndUptimeMillis() {
        return this.mEndUptimeMillis;
    }

    long getCreateTimeDebug() {
        return this.mCreateTimeDebug;
    }

    long getStartTimeDebug() {
        return this.mStartTimeDebug;
    }

    long getEndTimeDebug() {
        return this.mEndTimeDebug;
    }

    long getDurationDebug() {
        if (hasEnded()) {
            return this.mEndUptimeMillis - this.mCreateUptimeMillis;
        }
        return -1L;
    }

    boolean hasEnded() {
        return this.mEndUptimeMillis > 0;
    }

    boolean hasStarted() {
        return this.mStartUptimeMillis > 0;
    }

    void reportStarted() {
        if (hasEnded() || this.mStartUptimeMillis != 0) {
            return;
        }
        this.mStartUptimeMillis = android.os.SystemClock.uptimeMillis();
        this.mStartTimeDebug = java.lang.System.currentTimeMillis();
    }

    boolean reportEnded(com.android.server.vibrator.Vibration.CallerInfo endedBy) {
        if (hasEnded()) {
            return false;
        }
        if (endedBy != null) {
            this.mEndedByUid = endedBy.uid;
            this.mEndedByUsage = endedBy.attrs.getUsage();
        }
        this.mEndUptimeMillis = android.os.SystemClock.uptimeMillis();
        this.mEndTimeDebug = java.lang.System.currentTimeMillis();
        return true;
    }

    void reportInterruptedAnotherVibration(com.android.server.vibrator.Vibration.CallerInfo callerInfo) {
        if (this.mInterruptedUsage < 0) {
            this.mInterruptedUsage = callerInfo.attrs.getUsage();
        }
    }

    void reportAdaptiveScale(float scale) {
        if (java.lang.Float.compare(scale, 1.0f) != 0) {
            this.mAdaptiveScale = scale;
        }
    }

    void reportRepetition(int loops) {
        this.mRepeatCount += loops;
    }

    void reportVibratorOn(long halResult) {
        this.mVibratorOnCount++;
        if (halResult > 0) {
            this.mVibratorOnTotalDurationMillis += (int) halResult;
        }
    }

    void reportVibratorOff() {
        this.mVibratorOffCount++;
    }

    void reportSetAmplitude() {
        this.mVibratorSetAmplitudeCount++;
    }

    void reportPerformEffect(long halResult, android.os.vibrator.PrebakedSegment prebaked) {
        this.mVibratorPerformCount++;
        if (halResult > 0) {
            this.mVibratorEffectsUsed.put(prebaked.getEffectId(), true);
            this.mVibratorOnTotalDurationMillis += (int) halResult;
        } else {
            this.mVibratorEffectsUsed.put(prebaked.getEffectId(), false);
        }
    }

    void reportComposePrimitives(long halResult, android.os.vibrator.PrimitiveSegment[] primitives) {
        this.mVibratorComposeCount++;
        this.mVibrationCompositionTotalSize += primitives.length;
        if (halResult > 0) {
            for (android.os.vibrator.PrimitiveSegment primitive : primitives) {
                halResult -= (long) primitive.getDelay();
                this.mVibratorPrimitivesUsed.put(primitive.getPrimitiveId(), true);
            }
            if (halResult > 0) {
                this.mVibratorOnTotalDurationMillis += (int) halResult;
                return;
            }
            return;
        }
        for (android.os.vibrator.PrimitiveSegment primitiveSegment : primitives) {
            this.mVibratorPrimitivesUsed.put(primitiveSegment.getPrimitiveId(), false);
        }
    }

    void reportComposePwle(long halResult, android.os.vibrator.RampSegment[] segments) {
        this.mVibratorComposePwleCount++;
        this.mVibrationPwleTotalSize += segments.length;
        if (halResult > 0) {
            for (android.os.vibrator.RampSegment ramp : segments) {
                if (ramp.getStartAmplitude() == 0.0f && ramp.getEndAmplitude() == 0.0f) {
                    halResult -= ramp.getDuration();
                }
            }
            if (halResult > 0) {
                this.mVibratorOnTotalDurationMillis += (int) halResult;
            }
        }
    }

    void reportSetExternalControl() {
        this.mVibratorSetExternalControlCount++;
    }

    static final class StatsInfo {
        public final float adaptiveScale;
        public final int endLatencyMillis;
        public final boolean endedBySameUid;
        public final int endedByUsage;
        public final int halComposeCount;
        public final int halComposePwleCount;
        public final int halCompositionSize;
        public final int halOffCount;
        public final int halOnCount;
        public final int halPerformCount;
        public final int halPwleSize;
        public final int halSetAmplitudeCount;
        public final int halSetExternalControlCount;
        public final int[] halSupportedCompositionPrimitivesUsed;
        public final int[] halSupportedEffectsUsed;
        public final int[] halUnsupportedCompositionPrimitivesUsed;
        public final int[] halUnsupportedEffectsUsed;
        public final int interruptedUsage;
        private boolean mIsWritten;
        public final int repeatCount;
        public final int startLatencyMillis;
        public final int status;
        public final int totalDurationMillis;
        public final int uid;
        public final int usage;
        public final int vibrationType;
        public final int vibratorOnMillis;

        StatsInfo(int uid, int vibrationType, int usage, com.android.server.vibrator.Vibration.Status status, com.android.server.vibrator.VibrationStats stats, long completionUptimeMillis) {
            this.uid = uid;
            this.vibrationType = vibrationType;
            this.usage = usage;
            this.status = status.getProtoEnumValue();
            this.adaptiveScale = stats.mAdaptiveScale;
            this.endedBySameUid = uid == stats.mEndedByUid;
            this.endedByUsage = stats.mEndedByUsage;
            this.interruptedUsage = stats.mInterruptedUsage;
            this.repeatCount = stats.mRepeatCount;
            this.totalDurationMillis = (int) java.lang.Math.max(0L, completionUptimeMillis - stats.mCreateUptimeMillis);
            this.vibratorOnMillis = stats.mVibratorOnTotalDurationMillis;
            if (stats.hasStarted()) {
                this.startLatencyMillis = (int) java.lang.Math.max(0L, stats.mStartUptimeMillis - stats.mCreateUptimeMillis);
                this.endLatencyMillis = (int) java.lang.Math.max(0L, completionUptimeMillis - stats.mEndUptimeMillis);
            } else {
                this.endLatencyMillis = 0;
                this.startLatencyMillis = 0;
            }
            this.halComposeCount = stats.mVibratorComposeCount;
            this.halComposePwleCount = stats.mVibratorComposePwleCount;
            this.halOnCount = stats.mVibratorOnCount;
            this.halOffCount = stats.mVibratorOffCount;
            this.halPerformCount = stats.mVibratorPerformCount;
            this.halSetAmplitudeCount = stats.mVibratorSetAmplitudeCount;
            this.halSetExternalControlCount = stats.mVibratorSetExternalControlCount;
            this.halCompositionSize = stats.mVibrationCompositionTotalSize;
            this.halPwleSize = stats.mVibrationPwleTotalSize;
            this.halSupportedCompositionPrimitivesUsed = filteredKeys(stats.mVibratorPrimitivesUsed, true);
            this.halSupportedEffectsUsed = filteredKeys(stats.mVibratorEffectsUsed, true);
            this.halUnsupportedCompositionPrimitivesUsed = filteredKeys(stats.mVibratorPrimitivesUsed, false);
            this.halUnsupportedEffectsUsed = filteredKeys(stats.mVibratorEffectsUsed, false);
        }

        boolean isWritten() {
            return this.mIsWritten;
        }

        void writeVibrationReported() {
            if (this.mIsWritten) {
                android.util.Slog.wtf(com.android.server.vibrator.VibrationStats.TAG, "Writing same vibration stats multiple times for uid=" + this.uid);
            }
            this.mIsWritten = true;
            com.android.internal.util.FrameworkStatsLog.write_non_chained(com.android.internal.util.FrameworkStatsLog.VIBRATION_REPORTED, this.uid, null, this.vibrationType, this.usage, this.status, this.endedBySameUid, this.endedByUsage, this.interruptedUsage, this.repeatCount, this.totalDurationMillis, this.vibratorOnMillis, this.startLatencyMillis, this.endLatencyMillis, this.halComposeCount, this.halComposePwleCount, this.halOnCount, this.halOffCount, this.halPerformCount, this.halSetAmplitudeCount, this.halSetExternalControlCount, this.halSupportedCompositionPrimitivesUsed, this.halSupportedEffectsUsed, this.halUnsupportedCompositionPrimitivesUsed, this.halUnsupportedEffectsUsed, this.halCompositionSize, this.halPwleSize, this.adaptiveScale);
        }

        private static int[] filteredKeys(android.util.SparseBooleanArray supportArray, boolean supported) {
            int count = 0;
            for (int i = 0; i < supportArray.size(); i++) {
                if (supportArray.valueAt(i) == supported) {
                    count++;
                }
            }
            if (count == 0) {
                return null;
            }
            int pos = 0;
            int[] res = new int[count];
            for (int i2 = 0; i2 < supportArray.size(); i2++) {
                if (supportArray.valueAt(i2) == supported) {
                    res[pos] = supportArray.keyAt(i2);
                    pos++;
                }
            }
            return res;
        }
    }

    public com.android.server.vibrator.IVibrationStatsWrapper getWrapper() {
        return this.mVibrationSettingsWrapper;
    }

    private class VibrationStatsWrapper implements com.android.server.vibrator.IVibrationStatsWrapper {
        private VibrationStatsWrapper() {
        }

        @Override // com.android.server.vibrator.IVibrationStatsWrapper
        public void reportPerformOplusPrebaked(int waveformId, long duration) {
            com.android.server.vibrator.VibrationStats.this.mVibratorPerformCount++;
            com.android.server.vibrator.VibrationStats.this.mVibratorOnTotalDurationMillis += (int) duration;
        }

        @Override // com.android.server.vibrator.IVibrationStatsWrapper
        public void reportPerformRichtapPrebaked(int effectId, long duration) {
            com.android.server.vibrator.VibrationStats.this.mVibratorPerformCount++;
            com.android.server.vibrator.VibrationStats.this.mVibratorOnTotalDurationMillis += (int) duration;
        }
    }
}
