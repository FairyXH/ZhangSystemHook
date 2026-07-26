package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
abstract class Step implements java.lang.Comparable<com.android.server.vibrator.Step> {
    public final com.android.server.vibrator.VibrationStepConductor conductor;
    public final long startTime;

    public abstract java.util.List<com.android.server.vibrator.Step> cancel();

    public abstract void cancelImmediately();

    public abstract java.util.List<com.android.server.vibrator.Step> play();

    Step(com.android.server.vibrator.VibrationStepConductor conductor, long startTime) {
        this.conductor = conductor;
        this.startTime = startTime;
    }

    protected com.android.server.vibrator.HalVibration getVibration() {
        return this.conductor.getVibration();
    }

    public boolean isCleanUp() {
        return false;
    }

    public long getVibratorOnDuration() {
        return 0L;
    }

    public boolean acceptVibratorCompleteCallback(int vibratorId) {
        return false;
    }

    public long calculateWaitTime() {
        if (this.startTime == Long.MAX_VALUE) {
            return 0L;
        }
        return java.lang.Math.max(0L, this.startTime - android.os.SystemClock.uptimeMillis());
    }

    @Override // java.lang.Comparable
    public int compareTo(com.android.server.vibrator.Step o) {
        return java.lang.Long.compare(this.startTime, o.startTime);
    }
}
