package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class KernelWakelockStats extends java.util.HashMap<java.lang.String, com.android.server.power.stats.KernelWakelockStats.Entry> {
    int kernelWakelockVersion;

    public static class Entry {
        public long activeTimeUs;
        public int count;
        public long totalTimeUs;
        public int version;

        Entry(int count, long totalTimeUs, long activeTimeUs, int version) {
            this.count = count;
            this.totalTimeUs = totalTimeUs;
            this.activeTimeUs = activeTimeUs;
            this.version = version;
        }
    }
}
