package com.android.server.display.brightness.clamper;

/* JADX INFO: loaded from: classes2.dex */
public class PmicMonitor {
    private static final java.lang.String TAG = "PmicMonitor";
    private android.os.Temperature mCurrentTemperature;
    private java.util.concurrent.ScheduledFuture<?> mPmicMonitorFuture;
    private final com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerChangeListener mPowerChangeListener;
    private final long mPowerMonitorPeriodConfigSecs;
    private float mLastEnergyConsumed = 0.0f;
    private float mCurrentAvgPower = 0.0f;
    private long mCurrentTimestampMillis = 0;
    private final android.power.PowerStatsInternal mPowerStatsInternal = (android.power.PowerStatsInternal) com.android.server.LocalServices.getService(android.power.PowerStatsInternal.class);
    final android.os.IThermalService mThermalService = android.os.IThermalService.Stub.asInterface(android.os.ServiceManager.getService("thermalservice"));
    private final java.util.concurrent.ScheduledExecutorService mExecutor = java.util.concurrent.Executors.newSingleThreadScheduledExecutor();

    PmicMonitor(com.android.server.display.brightness.clamper.BrightnessPowerClamper.PowerChangeListener listener, int powerMonitorPeriodConfigSecs) {
        this.mPowerChangeListener = listener;
        this.mPowerMonitorPeriodConfigSecs = powerMonitorPeriodConfigSecs;
    }

    private android.os.Temperature getDisplayTemperature() {
        try {
            android.os.Temperature[] temperatures = this.mThermalService.getCurrentTemperaturesWithType(3);
            if (temperatures.length > 1) {
                android.util.Slog.w(TAG, "Multiple skin temperatures not allowed!");
            }
            if (temperatures.length <= 0) {
                return null;
            }
            android.os.Temperature retTemperature = temperatures[0];
            return retTemperature;
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "getDisplayTemperature failed" + e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void capturePeriodicDisplayPower() {
        android.hardware.power.stats.EnergyConsumerResult[] displayResults;
        android.hardware.power.stats.EnergyConsumer[] energyConsumers = this.mPowerStatsInternal.getEnergyConsumerInfo();
        if (energyConsumers == null || energyConsumers.length == 0) {
            return;
        }
        android.util.IntArray energyConsumerIds = new android.util.IntArray();
        for (int i = 0; i < energyConsumers.length; i++) {
            if (energyConsumers[i].type == 3) {
                energyConsumerIds.add(energyConsumers[i].id);
            }
        }
        int i2 = energyConsumerIds.size();
        if (i2 == 0) {
            android.util.Slog.w(TAG, "DISPLAY energyConsumerIds size is null");
            return;
        }
        java.util.concurrent.CompletableFuture<android.hardware.power.stats.EnergyConsumerResult[]> futureECRs = this.mPowerStatsInternal.getEnergyConsumedAsync(energyConsumerIds.toArray());
        if (futureECRs == null) {
            android.util.Slog.w(TAG, "Energy consumers results are null");
            return;
        }
        try {
            displayResults = futureECRs.get();
        } catch (java.lang.InterruptedException e) {
            android.util.Slog.w(TAG, "timeout or interrupt reading getEnergyConsumedAsync failed", e);
            displayResults = null;
        } catch (java.util.concurrent.ExecutionException e2) {
            android.util.Slog.wtf(TAG, "exception reading getEnergyConsumedAsync: ", e2);
            displayResults = null;
        }
        if (displayResults == null || displayResults.length == 0) {
            android.util.Slog.w(TAG, "displayResults are null");
            return;
        }
        float energyConsumed = displayResults[0].energyUWs - this.mLastEnergyConsumed;
        float timeIntervalSeconds = (displayResults[0].timestampMs - this.mCurrentTimestampMillis) / 1000.0f;
        float currentPower = energyConsumed / timeIntervalSeconds;
        android.os.Temperature displayTemperature = getDisplayTemperature();
        this.mCurrentAvgPower = currentPower / 1000.0f;
        this.mCurrentTemperature = displayTemperature;
        this.mLastEnergyConsumed = displayResults[0].energyUWs;
        this.mCurrentTimestampMillis = displayResults[0].timestampMs;
        if (this.mCurrentTemperature != null) {
            this.mPowerChangeListener.onChanged(this.mCurrentAvgPower, this.mCurrentTemperature.getStatus());
        }
    }

    public void start() {
        if (this.mPowerStatsInternal == null) {
            android.util.Slog.w(TAG, "Power stats service not found for monitoring.");
            return;
        }
        if (this.mThermalService == null) {
            android.util.Slog.w(TAG, "Thermal service not found.");
        } else if (this.mPmicMonitorFuture == null) {
            this.mPmicMonitorFuture = this.mExecutor.scheduleAtFixedRate(new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.PmicMonitor$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.capturePeriodicDisplayPower();
                }
            }, this.mPowerMonitorPeriodConfigSecs, this.mPowerMonitorPeriodConfigSecs, java.util.concurrent.TimeUnit.SECONDS);
        } else {
            android.util.Slog.e(TAG, "already scheduled, stop() called before start.");
        }
    }

    public void stop() {
        if (this.mPmicMonitorFuture != null) {
            this.mPmicMonitorFuture.cancel(true);
            this.mPmicMonitorFuture = null;
        }
    }

    public void shutdown() {
        this.mExecutor.shutdownNow();
    }
}
