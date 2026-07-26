package com.android.server.health;

/* JADX INFO: loaded from: classes2.dex */
public class Utils {
    private Utils() {
    }

    public static void copy(android.hardware.health.V1_0.HealthInfo dst, android.hardware.health.V1_0.HealthInfo src) {
        dst.chargerAcOnline = src.chargerAcOnline;
        dst.chargerUsbOnline = src.chargerUsbOnline;
        dst.chargerWirelessOnline = src.chargerWirelessOnline;
        dst.maxChargingCurrent = src.maxChargingCurrent;
        dst.maxChargingVoltage = src.maxChargingVoltage;
        dst.batteryStatus = src.batteryStatus;
        dst.batteryHealth = src.batteryHealth;
        dst.batteryPresent = src.batteryPresent;
        dst.batteryLevel = src.batteryLevel;
        dst.batteryVoltage = src.batteryVoltage;
        dst.batteryTemperature = src.batteryTemperature;
        dst.batteryCurrent = src.batteryCurrent;
        dst.batteryCycleCount = src.batteryCycleCount;
        dst.batteryFullCharge = src.batteryFullCharge;
        dst.batteryChargeCounter = src.batteryChargeCounter;
        dst.batteryTechnology = src.batteryTechnology;
    }

    public static void copyV1Battery(android.hardware.health.HealthInfo dst, android.hardware.health.HealthInfo src) {
        dst.chargerAcOnline = src.chargerAcOnline;
        dst.chargerUsbOnline = src.chargerUsbOnline;
        dst.chargerWirelessOnline = src.chargerWirelessOnline;
        dst.maxChargingCurrentMicroamps = src.maxChargingCurrentMicroamps;
        dst.maxChargingVoltageMicrovolts = src.maxChargingVoltageMicrovolts;
        dst.batteryStatus = src.batteryStatus;
        dst.batteryHealth = src.batteryHealth;
        dst.batteryPresent = src.batteryPresent;
        dst.batteryLevel = src.batteryLevel;
        dst.batteryVoltageMillivolts = src.batteryVoltageMillivolts;
        dst.batteryTemperatureTenthsCelsius = src.batteryTemperatureTenthsCelsius;
        dst.batteryCurrentMicroamps = src.batteryCurrentMicroamps;
        dst.batteryCycleCount = src.batteryCycleCount;
        dst.batteryFullChargeUah = src.batteryFullChargeUah;
        dst.batteryChargeCounterUah = src.batteryChargeCounterUah;
        dst.batteryTechnology = src.batteryTechnology;
        dst.batteryCurrentAverageMicroamps = src.batteryCurrentAverageMicroamps;
        dst.batteryCapacityLevel = src.batteryCapacityLevel;
        dst.batteryChargeTimeToFullNowSeconds = src.batteryChargeTimeToFullNowSeconds;
        dst.batteryFullChargeDesignCapacityUah = src.batteryFullChargeDesignCapacityUah;
    }
}
