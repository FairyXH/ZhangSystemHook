package com.android.server.powerstats;

/* JADX INFO: loaded from: classes3.dex */
public final class PowerStatsLogger extends android.os.Handler {
    private static final boolean DEBUG = false;
    protected static final int MSG_LOG_TO_DATA_STORAGE_BATTERY_DROP = 0;
    protected static final int MSG_LOG_TO_DATA_STORAGE_HIGH_FREQUENCY = 2;
    protected static final int MSG_LOG_TO_DATA_STORAGE_LOW_FREQUENCY = 1;
    private static final java.lang.String TAG = com.android.server.powerstats.PowerStatsLogger.class.getSimpleName();
    private java.io.File mDataStoragePath;
    private boolean mDeleteMeterDataOnBoot;
    private boolean mDeleteModelDataOnBoot;
    private boolean mDeleteResidencyDataOnBoot;
    private final com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper mPowerStatsHALWrapper;
    private final com.android.server.powerstats.PowerStatsDataStorage mPowerStatsMeterStorage;
    private final com.android.server.powerstats.PowerStatsDataStorage mPowerStatsModelStorage;
    private final com.android.server.powerstats.PowerStatsDataStorage mPowerStatsResidencyStorage;
    private final long mStartWallTime;

    @Override // android.os.Handler
    public void handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case 0:
                android.hardware.power.stats.StateResidencyResult[] stateResidencyResults = this.mPowerStatsHALWrapper.getStateResidency(new int[0]);
                com.android.server.powerstats.ProtoStreamUtils.StateResidencyResultUtils.adjustTimeSinceBootToEpoch(stateResidencyResults, this.mStartWallTime);
                this.mPowerStatsResidencyStorage.write(com.android.server.powerstats.ProtoStreamUtils.StateResidencyResultUtils.getProtoBytes(stateResidencyResults));
                break;
            case 1:
                android.hardware.power.stats.EnergyConsumerResult[] ecrAttribution = this.mPowerStatsHALWrapper.getEnergyConsumed(new int[0]);
                com.android.server.powerstats.ProtoStreamUtils.EnergyConsumerResultUtils.adjustTimeSinceBootToEpoch(ecrAttribution, this.mStartWallTime);
                this.mPowerStatsModelStorage.write(com.android.server.powerstats.ProtoStreamUtils.EnergyConsumerResultUtils.getProtoBytes(ecrAttribution, true));
                break;
            case 2:
                android.hardware.power.stats.EnergyMeasurement[] energyMeasurements = this.mPowerStatsHALWrapper.readEnergyMeter(new int[0]);
                com.android.server.powerstats.ProtoStreamUtils.EnergyMeasurementUtils.adjustTimeSinceBootToEpoch(energyMeasurements, this.mStartWallTime);
                this.mPowerStatsMeterStorage.write(com.android.server.powerstats.ProtoStreamUtils.EnergyMeasurementUtils.getProtoBytes(energyMeasurements));
                android.hardware.power.stats.EnergyConsumerResult[] ecrNoAttribution = this.mPowerStatsHALWrapper.getEnergyConsumed(new int[0]);
                com.android.server.powerstats.ProtoStreamUtils.EnergyConsumerResultUtils.adjustTimeSinceBootToEpoch(ecrNoAttribution, this.mStartWallTime);
                this.mPowerStatsModelStorage.write(com.android.server.powerstats.ProtoStreamUtils.EnergyConsumerResultUtils.getProtoBytes(ecrNoAttribution, false));
                break;
        }
    }

    public void writeMeterDataToFile(java.io.FileDescriptor fd) {
        final android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream(fd);
        try {
            android.hardware.power.stats.Channel[] channel = this.mPowerStatsHALWrapper.getEnergyMeterInfo();
            com.android.server.powerstats.ProtoStreamUtils.ChannelUtils.packProtoMessage(channel, pos);
            this.mPowerStatsMeterStorage.read(new com.android.server.powerstats.PowerStatsDataStorage.DataElementReadCallback() { // from class: com.android.server.powerstats.PowerStatsLogger.1
                @Override // com.android.server.powerstats.PowerStatsDataStorage.DataElementReadCallback
                public void onReadDataElement(byte[] data) {
                    try {
                        new android.util.proto.ProtoInputStream(new java.io.ByteArrayInputStream(data));
                        android.hardware.power.stats.EnergyMeasurement[] energyMeasurement = com.android.server.powerstats.ProtoStreamUtils.EnergyMeasurementUtils.unpackProtoMessage(data);
                        com.android.server.powerstats.ProtoStreamUtils.EnergyMeasurementUtils.packProtoMessage(energyMeasurement, pos);
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(com.android.server.powerstats.PowerStatsLogger.TAG, "Failed to write energy meter data to incident report.", e);
                    }
                }
            });
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write energy meter info to incident report.", e);
        }
        pos.flush();
    }

    public void writeModelDataToFile(java.io.FileDescriptor fd) {
        final android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream(fd);
        try {
            android.hardware.power.stats.EnergyConsumer[] energyConsumer = this.mPowerStatsHALWrapper.getEnergyConsumerInfo();
            com.android.server.powerstats.ProtoStreamUtils.EnergyConsumerUtils.packProtoMessage(energyConsumer, pos);
            this.mPowerStatsModelStorage.read(new com.android.server.powerstats.PowerStatsDataStorage.DataElementReadCallback() { // from class: com.android.server.powerstats.PowerStatsLogger.2
                @Override // com.android.server.powerstats.PowerStatsDataStorage.DataElementReadCallback
                public void onReadDataElement(byte[] data) {
                    try {
                        new android.util.proto.ProtoInputStream(new java.io.ByteArrayInputStream(data));
                        android.hardware.power.stats.EnergyConsumerResult[] energyConsumerResult = com.android.server.powerstats.ProtoStreamUtils.EnergyConsumerResultUtils.unpackProtoMessage(data);
                        com.android.server.powerstats.ProtoStreamUtils.EnergyConsumerResultUtils.packProtoMessage(energyConsumerResult, pos, true);
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(com.android.server.powerstats.PowerStatsLogger.TAG, "Failed to write energy model data to incident report.", e);
                    }
                }
            });
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write energy model info to incident report.", e);
        }
        pos.flush();
    }

    public void writeResidencyDataToFile(java.io.FileDescriptor fd) {
        final android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream(fd);
        try {
            android.hardware.power.stats.PowerEntity[] powerEntity = this.mPowerStatsHALWrapper.getPowerEntityInfo();
            com.android.server.powerstats.ProtoStreamUtils.PowerEntityUtils.packProtoMessage(powerEntity, pos);
            this.mPowerStatsResidencyStorage.read(new com.android.server.powerstats.PowerStatsDataStorage.DataElementReadCallback() { // from class: com.android.server.powerstats.PowerStatsLogger.3
                @Override // com.android.server.powerstats.PowerStatsDataStorage.DataElementReadCallback
                public void onReadDataElement(byte[] data) {
                    try {
                        new android.util.proto.ProtoInputStream(new java.io.ByteArrayInputStream(data));
                        android.hardware.power.stats.StateResidencyResult[] stateResidencyResult = com.android.server.powerstats.ProtoStreamUtils.StateResidencyResultUtils.unpackProtoMessage(data);
                        com.android.server.powerstats.ProtoStreamUtils.StateResidencyResultUtils.packProtoMessage(stateResidencyResult, pos);
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(com.android.server.powerstats.PowerStatsLogger.TAG, "Failed to write residency data to incident report.", e);
                    }
                }
            });
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write residency data to incident report.", e);
        }
        pos.flush();
    }

    private boolean dataChanged(java.lang.String cachedFilename, byte[] dataCurrent) {
        if (!this.mDataStoragePath.exists() && !this.mDataStoragePath.mkdirs()) {
            return false;
        }
        java.io.File cachedFile = new java.io.File(this.mDataStoragePath, cachedFilename);
        if (cachedFile.exists()) {
            byte[] dataCached = new byte[(int) cachedFile.length()];
            try {
                java.io.FileInputStream fis = new java.io.FileInputStream(cachedFile.getPath());
                fis.read(dataCached);
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Failed to read cached data from file", e);
            }
            boolean dataChanged = !java.util.Arrays.equals(dataCached, dataCurrent);
            return dataChanged;
        }
        return true;
    }

    private void updateCacheFile(java.lang.String cacheFilename, byte[] data) {
        try {
            android.util.AtomicFile atomicCachedFile = new android.util.AtomicFile(new java.io.File(this.mDataStoragePath, cacheFilename));
            java.io.FileOutputStream fos = atomicCachedFile.startWrite();
            fos.write(data);
            atomicCachedFile.finishWrite(fos);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write current data to cached file", e);
        }
    }

    public boolean getDeleteMeterDataOnBoot() {
        return this.mDeleteMeterDataOnBoot;
    }

    public boolean getDeleteModelDataOnBoot() {
        return this.mDeleteModelDataOnBoot;
    }

    public boolean getDeleteResidencyDataOnBoot() {
        return this.mDeleteResidencyDataOnBoot;
    }

    public long getStartWallTime() {
        return this.mStartWallTime;
    }

    public PowerStatsLogger(android.content.Context context, android.os.Looper looper, java.io.File dataStoragePath, java.lang.String meterFilename, java.lang.String meterCacheFilename, java.lang.String modelFilename, java.lang.String modelCacheFilename, java.lang.String residencyFilename, java.lang.String residencyCacheFilename, com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper powerStatsHALWrapper) {
        super(looper);
        this.mStartWallTime = java.lang.System.currentTimeMillis() - android.os.SystemClock.elapsedRealtime();
        this.mPowerStatsHALWrapper = powerStatsHALWrapper;
        this.mDataStoragePath = dataStoragePath;
        this.mPowerStatsMeterStorage = new com.android.server.powerstats.PowerStatsDataStorage(context, this.mDataStoragePath, meterFilename);
        this.mPowerStatsModelStorage = new com.android.server.powerstats.PowerStatsDataStorage(context, this.mDataStoragePath, modelFilename);
        this.mPowerStatsResidencyStorage = new com.android.server.powerstats.PowerStatsDataStorage(context, this.mDataStoragePath, residencyFilename);
        android.hardware.power.stats.Channel[] channels = this.mPowerStatsHALWrapper.getEnergyMeterInfo();
        byte[] channelBytes = com.android.server.powerstats.ProtoStreamUtils.ChannelUtils.getProtoBytes(channels);
        this.mDeleteMeterDataOnBoot = dataChanged(meterCacheFilename, channelBytes);
        if (this.mDeleteMeterDataOnBoot) {
            this.mPowerStatsMeterStorage.deleteLogs();
            updateCacheFile(meterCacheFilename, channelBytes);
        }
        android.hardware.power.stats.EnergyConsumer[] energyConsumers = this.mPowerStatsHALWrapper.getEnergyConsumerInfo();
        byte[] energyConsumerBytes = com.android.server.powerstats.ProtoStreamUtils.EnergyConsumerUtils.getProtoBytes(energyConsumers);
        this.mDeleteModelDataOnBoot = dataChanged(modelCacheFilename, energyConsumerBytes);
        if (this.mDeleteModelDataOnBoot) {
            this.mPowerStatsModelStorage.deleteLogs();
            updateCacheFile(modelCacheFilename, energyConsumerBytes);
        }
        android.hardware.power.stats.PowerEntity[] powerEntities = this.mPowerStatsHALWrapper.getPowerEntityInfo();
        byte[] powerEntityBytes = com.android.server.powerstats.ProtoStreamUtils.PowerEntityUtils.getProtoBytes(powerEntities);
        this.mDeleteResidencyDataOnBoot = dataChanged(residencyCacheFilename, powerEntityBytes);
        if (this.mDeleteResidencyDataOnBoot) {
            this.mPowerStatsResidencyStorage.deleteLogs();
            updateCacheFile(residencyCacheFilename, powerEntityBytes);
        }
    }

    public void dump(android.util.IndentingPrintWriter ipw) {
        ipw.println("PowerStats Meter Data:");
        ipw.increaseIndent();
        this.mPowerStatsMeterStorage.dump(ipw);
        ipw.decreaseIndent();
        ipw.println("PowerStats Model Data:");
        ipw.increaseIndent();
        this.mPowerStatsModelStorage.dump(ipw);
        ipw.decreaseIndent();
        ipw.println("PowerStats State Residency Data:");
        ipw.increaseIndent();
        this.mPowerStatsResidencyStorage.dump(ipw);
        ipw.decreaseIndent();
    }
}
