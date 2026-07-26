package com.android.server.cpu;

/* JADX INFO: loaded from: classes.dex */
public abstract class CpuMonitorInternal {

    public interface CpuAvailabilityCallback {
        void onAvailabilityChanged(com.android.server.cpu.CpuAvailabilityInfo cpuAvailabilityInfo);

        void onMonitoringIntervalChanged(long j);
    }

    public abstract void addCpuAvailabilityCallback(java.util.concurrent.Executor executor, com.android.server.cpu.CpuAvailabilityMonitoringConfig cpuAvailabilityMonitoringConfig, com.android.server.cpu.CpuMonitorInternal.CpuAvailabilityCallback cpuAvailabilityCallback);

    public abstract void removeCpuAvailabilityCallback(com.android.server.cpu.CpuMonitorInternal.CpuAvailabilityCallback cpuAvailabilityCallback);
}
