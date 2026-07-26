package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class HalVibration extends com.android.server.vibrator.Vibration {
    private float mAdaptiveScale;
    private final java.util.concurrent.CountDownLatch mCompletionLatch;
    private volatile android.os.CombinedVibration mEffectToPlay;
    public final android.util.SparseArray<android.os.VibrationEffect> mFallbacks;
    private final android.os.CombinedVibration mOriginalEffect;
    private int mScaleLevel;
    private com.android.server.vibrator.Vibration.Status mStatus;

    HalVibration(android.os.IBinder token, android.os.CombinedVibration effect, com.android.server.vibrator.Vibration.CallerInfo callerInfo) {
        super(token, callerInfo);
        this.mFallbacks = new android.util.SparseArray<>();
        this.mCompletionLatch = new java.util.concurrent.CountDownLatch(1);
        this.mOriginalEffect = effect;
        this.mEffectToPlay = effect;
        this.mStatus = com.android.server.vibrator.Vibration.Status.RUNNING;
        this.mScaleLevel = 0;
        this.mAdaptiveScale = 1.0f;
    }

    public void end(com.android.server.vibrator.Vibration.EndInfo info) {
        if (hasEnded()) {
            return;
        }
        this.mStatus = info.status;
        this.stats.reportEnded(info.endedBy);
        this.mCompletionLatch.countDown();
    }

    public void waitForEnd() throws java.lang.InterruptedException {
        this.mCompletionLatch.await();
    }

    public android.os.VibrationEffect getFallback(int effectId) {
        return this.mFallbacks.get(effectId);
    }

    public void addFallback(int effectId, android.os.VibrationEffect effect) {
        this.mFallbacks.put(effectId, effect);
    }

    public void resolveEffects(int defaultAmplitude) {
        android.os.CombinedVibration newEffect = this.mEffectToPlay.transform(new android.os.VibrationEffect.Transformation() { // from class: com.android.server.vibrator.HalVibration$$ExternalSyntheticLambda0
            public final android.os.VibrationEffect transform(android.os.VibrationEffect vibrationEffect, java.lang.Object obj) {
                return vibrationEffect.resolve(((java.lang.Integer) obj).intValue());
            }
        }, java.lang.Integer.valueOf(defaultAmplitude));
        if (!java.util.Objects.equals(this.mEffectToPlay, newEffect)) {
            this.mEffectToPlay = newEffect;
        }
        for (int i = 0; i < this.mFallbacks.size(); i++) {
            this.mFallbacks.setValueAt(i, this.mFallbacks.valueAt(i).resolve(defaultAmplitude));
        }
    }

    public void scaleEffects(final com.android.server.vibrator.VibrationScaler scaler) {
        int vibrationUsage = this.callerInfo.attrs.getUsage();
        this.mScaleLevel = scaler.getScaleLevel(vibrationUsage);
        this.mAdaptiveScale = scaler.getAdaptiveHapticsScale(vibrationUsage);
        this.stats.reportAdaptiveScale(this.mAdaptiveScale);
        android.os.CombinedVibration combinedVibration = this.mEffectToPlay;
        java.util.Objects.requireNonNull(scaler);
        android.os.CombinedVibration newEffect = combinedVibration.transform(new android.os.VibrationEffect.Transformation() { // from class: com.android.server.vibrator.HalVibration$$ExternalSyntheticLambda1
            public final android.os.VibrationEffect transform(android.os.VibrationEffect vibrationEffect, java.lang.Object obj) {
                return scaler.scale(vibrationEffect, ((java.lang.Integer) obj).intValue());
            }
        }, java.lang.Integer.valueOf(vibrationUsage));
        if (!java.util.Objects.equals(this.mEffectToPlay, newEffect)) {
            this.mEffectToPlay = newEffect;
        }
        for (int i = 0; i < this.mFallbacks.size(); i++) {
            this.mFallbacks.setValueAt(i, scaler.scale(this.mFallbacks.valueAt(i), vibrationUsage));
        }
    }

    public void adaptToDevice(android.os.CombinedVibration.VibratorAdapter deviceAdapter) {
        android.os.CombinedVibration newEffect = this.mEffectToPlay.adapt(deviceAdapter);
        if (!java.util.Objects.equals(this.mEffectToPlay, newEffect)) {
            this.mEffectToPlay = newEffect;
        }
    }

    public boolean hasEnded() {
        return this.mStatus != com.android.server.vibrator.Vibration.Status.RUNNING;
    }

    @Override // com.android.server.vibrator.Vibration
    public boolean isRepeating() {
        return this.mOriginalEffect.getDuration() == Long.MAX_VALUE;
    }

    public android.os.CombinedVibration getEffectToPlay() {
        return this.mEffectToPlay;
    }

    public com.android.server.vibrator.Vibration.DebugInfo getDebugInfo() {
        android.os.CombinedVibration originalEffect = java.util.Objects.equals(this.mOriginalEffect, this.mEffectToPlay) ? null : this.mOriginalEffect;
        return new com.android.server.vibrator.Vibration.DebugInfo(this.mStatus, this.stats, this.mEffectToPlay, originalEffect, this.mScaleLevel, this.mAdaptiveScale, this.callerInfo);
    }

    public com.android.server.vibrator.VibrationStats.StatsInfo getStatsInfo(long completionUptimeMillis) {
        int vibrationType;
        if (isRepeating()) {
            vibrationType = 2;
        } else {
            vibrationType = 1;
        }
        return new com.android.server.vibrator.VibrationStats.StatsInfo(this.callerInfo.uid, vibrationType, this.callerInfo.attrs.getUsage(), this.mStatus, this.stats, completionUptimeMillis);
    }

    public boolean canPipelineWith(com.android.server.vibrator.HalVibration vib) {
        return this.callerInfo.uid == vib.callerInfo.uid && this.callerInfo.attrs.isFlagSet(8) && vib.callerInfo.attrs.isFlagSet(8) && !isRepeating();
    }
}
