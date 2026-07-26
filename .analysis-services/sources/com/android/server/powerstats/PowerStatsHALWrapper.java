package com.android.server.powerstats;

/* JADX INFO: loaded from: classes3.dex */
public final class PowerStatsHALWrapper {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = com.android.server.powerstats.PowerStatsHALWrapper.class.getSimpleName();

    public interface IPowerStatsHALWrapper {
        android.hardware.power.stats.EnergyConsumerResult[] getEnergyConsumed(int[] iArr);

        android.hardware.power.stats.EnergyConsumer[] getEnergyConsumerInfo();

        android.hardware.power.stats.Channel[] getEnergyMeterInfo();

        android.hardware.power.stats.PowerEntity[] getPowerEntityInfo();

        android.hardware.power.stats.StateResidencyResult[] getStateResidency(int[] iArr);

        boolean isInitialized();

        android.hardware.power.stats.EnergyMeasurement[] readEnergyMeter(int[] iArr);
    }

    public static final class PowerStatsHAL20WrapperImpl implements com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper {
        private static java.util.function.Supplier<android.hardware.power.stats.IPowerStats> sVintfPowerStats;

        public PowerStatsHAL20WrapperImpl() {
            java.util.function.Supplier<android.hardware.power.stats.IPowerStats> service = new com.android.server.powerstats.PowerStatsHALWrapper.VintfHalCache();
            sVintfPowerStats = null;
            if (service.get() == null) {
                sVintfPowerStats = null;
            } else {
                sVintfPowerStats = service;
            }
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public android.hardware.power.stats.PowerEntity[] getPowerEntityInfo() {
            if (sVintfPowerStats == null) {
                return null;
            }
            try {
                android.hardware.power.stats.PowerEntity[] powerEntityHAL = sVintfPowerStats.get().getPowerEntityInfo();
                return powerEntityHAL;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.powerstats.PowerStatsHALWrapper.TAG, "Failed to get power entity info: ", e);
                return null;
            }
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public android.hardware.power.stats.StateResidencyResult[] getStateResidency(int[] powerEntityIds) {
            if (sVintfPowerStats == null) {
                return null;
            }
            try {
                android.hardware.power.stats.StateResidencyResult[] stateResidencyResultHAL = sVintfPowerStats.get().getStateResidency(powerEntityIds);
                return stateResidencyResultHAL;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.powerstats.PowerStatsHALWrapper.TAG, "Failed to get state residency: ", e);
                return null;
            }
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public android.hardware.power.stats.EnergyConsumer[] getEnergyConsumerInfo() {
            if (sVintfPowerStats == null) {
                return null;
            }
            try {
                android.hardware.power.stats.EnergyConsumer[] energyConsumerHAL = sVintfPowerStats.get().getEnergyConsumerInfo();
                return energyConsumerHAL;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.powerstats.PowerStatsHALWrapper.TAG, "Failed to get energy consumer info: ", e);
                return null;
            }
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public android.hardware.power.stats.EnergyConsumerResult[] getEnergyConsumed(int[] energyConsumerIds) {
            if (sVintfPowerStats == null) {
                return null;
            }
            try {
                android.hardware.power.stats.EnergyConsumerResult[] energyConsumedHAL = sVintfPowerStats.get().getEnergyConsumed(energyConsumerIds);
                return energyConsumedHAL;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.powerstats.PowerStatsHALWrapper.TAG, "Failed to get energy consumer results: ", e);
                return null;
            }
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public android.hardware.power.stats.Channel[] getEnergyMeterInfo() {
            if (sVintfPowerStats == null) {
                return null;
            }
            try {
                android.hardware.power.stats.Channel[] energyMeterInfoHAL = sVintfPowerStats.get().getEnergyMeterInfo();
                return energyMeterInfoHAL;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.powerstats.PowerStatsHALWrapper.TAG, "Failed to get energy meter info: ", e);
                return null;
            }
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public android.hardware.power.stats.EnergyMeasurement[] readEnergyMeter(int[] channelIds) {
            if (sVintfPowerStats == null) {
                return null;
            }
            try {
                android.hardware.power.stats.EnergyMeasurement[] energyMeasurementHAL = sVintfPowerStats.get().readEnergyMeter(channelIds);
                return energyMeasurementHAL;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.powerstats.PowerStatsHALWrapper.TAG, "Failed to get energy measurements: ", e);
                return null;
            }
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public boolean isInitialized() {
            return sVintfPowerStats != null;
        }
    }

    public static final class PowerStatsHAL10WrapperImpl implements com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper {
        private boolean mIsInitialized;

        private static native android.hardware.power.stats.Channel[] nativeGetEnergyMeterInfo();

        private static native android.hardware.power.stats.PowerEntity[] nativeGetPowerEntityInfo();

        private static native android.hardware.power.stats.StateResidencyResult[] nativeGetStateResidency(int[] iArr);

        private static native boolean nativeInit();

        private static native android.hardware.power.stats.EnergyMeasurement[] nativeReadEnergyMeters(int[] iArr);

        public PowerStatsHAL10WrapperImpl() {
            if (nativeInit()) {
                this.mIsInitialized = true;
            } else {
                this.mIsInitialized = false;
            }
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public android.hardware.power.stats.PowerEntity[] getPowerEntityInfo() {
            return nativeGetPowerEntityInfo();
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public android.hardware.power.stats.StateResidencyResult[] getStateResidency(int[] powerEntityIds) {
            return nativeGetStateResidency(powerEntityIds);
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public android.hardware.power.stats.EnergyConsumer[] getEnergyConsumerInfo() {
            return new android.hardware.power.stats.EnergyConsumer[0];
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public android.hardware.power.stats.EnergyConsumerResult[] getEnergyConsumed(int[] energyConsumerIds) {
            return new android.hardware.power.stats.EnergyConsumerResult[0];
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public android.hardware.power.stats.Channel[] getEnergyMeterInfo() {
            return nativeGetEnergyMeterInfo();
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public android.hardware.power.stats.EnergyMeasurement[] readEnergyMeter(int[] channelIds) {
            return nativeReadEnergyMeters(channelIds);
        }

        @Override // com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper
        public boolean isInitialized() {
            return this.mIsInitialized;
        }
    }

    public static com.android.server.powerstats.PowerStatsHALWrapper.IPowerStatsHALWrapper getPowerStatsHalImpl() {
        com.android.server.powerstats.PowerStatsHALWrapper.PowerStatsHAL20WrapperImpl powerStatsHAL20WrapperImpl = new com.android.server.powerstats.PowerStatsHALWrapper.PowerStatsHAL20WrapperImpl();
        if (powerStatsHAL20WrapperImpl.isInitialized()) {
            return powerStatsHAL20WrapperImpl;
        }
        return new com.android.server.powerstats.PowerStatsHALWrapper.PowerStatsHAL10WrapperImpl();
    }

    private static class VintfHalCache implements java.util.function.Supplier<android.hardware.power.stats.IPowerStats>, android.os.IBinder.DeathRecipient {
        private android.hardware.power.stats.IPowerStats mInstance;

        private VintfHalCache() {
            this.mInstance = null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.function.Supplier
        public synchronized android.hardware.power.stats.IPowerStats get() {
            android.os.IBinder binder;
            if (this.mInstance == null && (binder = android.os.Binder.allowBlocking(android.os.ServiceManager.waitForDeclaredService("android.hardware.power.stats.IPowerStats/default"))) != null) {
                this.mInstance = android.hardware.power.stats.IPowerStats.Stub.asInterface(binder);
                try {
                    binder.linkToDeath(this, 0);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.powerstats.PowerStatsHALWrapper.TAG, "Unable to register DeathRecipient for " + this.mInstance);
                }
            }
            return this.mInstance;
        }

        @Override // android.os.IBinder.DeathRecipient
        public synchronized void binderDied() {
            android.util.Slog.w(com.android.server.powerstats.PowerStatsHALWrapper.TAG, "PowerStats HAL died");
            this.mInstance = null;
        }
    }
}
