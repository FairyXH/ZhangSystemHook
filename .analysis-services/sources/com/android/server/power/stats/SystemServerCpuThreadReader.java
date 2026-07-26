package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class SystemServerCpuThreadReader {
    private final com.android.server.power.stats.SystemServerCpuThreadReader.SystemServiceCpuThreadTimes mDeltaCpuThreadTimes;
    private final com.android.internal.os.KernelSingleProcessCpuThreadReader mKernelCpuThreadReader;
    private long[] mLastBinderThreadCpuTimesUs;
    private long[] mLastThreadCpuTimesUs;

    public static class SystemServiceCpuThreadTimes {
        public long[] binderThreadCpuTimesUs;
        public long[] threadCpuTimesUs;
    }

    public static com.android.server.power.stats.SystemServerCpuThreadReader create() {
        return new com.android.server.power.stats.SystemServerCpuThreadReader(com.android.internal.os.KernelSingleProcessCpuThreadReader.create(android.os.Process.myPid()));
    }

    public SystemServerCpuThreadReader(int pid, com.android.internal.os.KernelSingleProcessCpuThreadReader.CpuTimeInStateReader cpuTimeInStateReader) throws java.io.IOException {
        this(new com.android.internal.os.KernelSingleProcessCpuThreadReader(pid, cpuTimeInStateReader));
    }

    public SystemServerCpuThreadReader(com.android.internal.os.KernelSingleProcessCpuThreadReader kernelCpuThreadReader) {
        this.mDeltaCpuThreadTimes = new com.android.server.power.stats.SystemServerCpuThreadReader.SystemServiceCpuThreadTimes();
        this.mKernelCpuThreadReader = kernelCpuThreadReader;
    }

    public void startTrackingThreadCpuTime() {
        this.mKernelCpuThreadReader.startTrackingThreadCpuTimes();
    }

    public void setBinderThreadNativeTids(int[] nativeTids) {
        this.mKernelCpuThreadReader.setSelectedThreadIds(nativeTids);
    }

    public com.android.server.power.stats.SystemServerCpuThreadReader.SystemServiceCpuThreadTimes readDelta() {
        int numCpuFrequencies = this.mKernelCpuThreadReader.getCpuFrequencyCount();
        if (this.mLastThreadCpuTimesUs == null) {
            this.mLastThreadCpuTimesUs = new long[numCpuFrequencies];
            this.mLastBinderThreadCpuTimesUs = new long[numCpuFrequencies];
            this.mDeltaCpuThreadTimes.threadCpuTimesUs = new long[numCpuFrequencies];
            this.mDeltaCpuThreadTimes.binderThreadCpuTimesUs = new long[numCpuFrequencies];
        }
        com.android.internal.os.KernelSingleProcessCpuThreadReader.ProcessCpuUsage processCpuUsage = this.mKernelCpuThreadReader.getProcessCpuUsage();
        if (processCpuUsage == null) {
            return null;
        }
        for (int i = numCpuFrequencies - 1; i >= 0; i--) {
            long threadCpuTimesUs = processCpuUsage.threadCpuTimesMillis[i] * 1000;
            long binderThreadCpuTimesUs = processCpuUsage.selectedThreadCpuTimesMillis[i] * 1000;
            this.mDeltaCpuThreadTimes.threadCpuTimesUs[i] = java.lang.Math.max(0L, threadCpuTimesUs - this.mLastThreadCpuTimesUs[i]);
            this.mDeltaCpuThreadTimes.binderThreadCpuTimesUs[i] = java.lang.Math.max(0L, binderThreadCpuTimesUs - this.mLastBinderThreadCpuTimesUs[i]);
            this.mLastThreadCpuTimesUs[i] = threadCpuTimesUs;
            this.mLastBinderThreadCpuTimesUs[i] = binderThreadCpuTimesUs;
        }
        return this.mDeltaCpuThreadTimes;
    }

    public com.android.server.power.stats.SystemServerCpuThreadReader.SystemServiceCpuThreadTimes readAbsolute() {
        int numCpuFrequencies = this.mKernelCpuThreadReader.getCpuFrequencyCount();
        com.android.internal.os.KernelSingleProcessCpuThreadReader.ProcessCpuUsage processCpuUsage = this.mKernelCpuThreadReader.getProcessCpuUsage();
        if (processCpuUsage == null) {
            return null;
        }
        com.android.server.power.stats.SystemServerCpuThreadReader.SystemServiceCpuThreadTimes result = new com.android.server.power.stats.SystemServerCpuThreadReader.SystemServiceCpuThreadTimes();
        result.threadCpuTimesUs = new long[numCpuFrequencies];
        result.binderThreadCpuTimesUs = new long[numCpuFrequencies];
        for (int i = 0; i < numCpuFrequencies; i++) {
            result.threadCpuTimesUs[i] = processCpuUsage.threadCpuTimesMillis[i] * 1000;
            result.binderThreadCpuTimesUs[i] = processCpuUsage.selectedThreadCpuTimesMillis[i] * 1000;
        }
        return result;
    }
}
