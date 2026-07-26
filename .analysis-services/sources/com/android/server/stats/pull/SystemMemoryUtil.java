package com.android.server.stats.pull;

/* JADX INFO: loaded from: classes3.dex */
final class SystemMemoryUtil {
    private SystemMemoryUtil() {
    }

    static com.android.server.stats.pull.SystemMemoryUtil.Metrics getMetrics() {
        long accountedKb;
        int totalIonKb = (int) android.os.Debug.getDmabufHeapTotalExportedKb();
        int gpuTotalUsageKb = (int) android.os.Debug.getGpuTotalUsageKb();
        int gpuPrivateAllocationsKb = (int) android.os.Debug.getGpuPrivateMemoryKb();
        int dmaBufTotalExportedKb = (int) android.os.Debug.getDmabufTotalExportedKb();
        long[] mInfos = new long[26];
        android.os.Debug.getMemInfo(mInfos);
        long kReclaimableKb = mInfos[15];
        if (kReclaimableKb == 0) {
            kReclaimableKb = mInfos[6];
        }
        long accountedKb2 = mInfos[1] + mInfos[10] + mInfos[2] + mInfos[16] + mInfos[17] + mInfos[18] + mInfos[7] + kReclaimableKb + mInfos[12] + mInfos[13];
        if (!android.os.Debug.isVmapStack()) {
            accountedKb2 += mInfos[14];
        }
        if (dmaBufTotalExportedKb >= 0 && gpuPrivateAllocationsKb >= 0) {
            accountedKb = accountedKb2 + ((long) (dmaBufTotalExportedKb + gpuPrivateAllocationsKb));
        } else {
            accountedKb = accountedKb2 + ((long) java.lang.Math.max(0, gpuTotalUsageKb));
            if (dmaBufTotalExportedKb >= 0) {
                accountedKb += (long) dmaBufTotalExportedKb;
            } else if (totalIonKb >= 0) {
                accountedKb += (long) totalIonKb;
            }
        }
        com.android.server.stats.pull.SystemMemoryUtil.Metrics result = new com.android.server.stats.pull.SystemMemoryUtil.Metrics();
        result.unreclaimableSlabKb = (int) mInfos[7];
        result.vmallocUsedKb = (int) mInfos[12];
        result.pageTablesKb = (int) mInfos[13];
        result.kernelStackKb = (int) mInfos[14];
        result.shmemKb = (int) mInfos[4];
        result.totalKb = (int) mInfos[0];
        result.freeKb = (int) mInfos[1];
        result.availableKb = (int) mInfos[19];
        result.activeKb = (int) mInfos[16];
        result.inactiveKb = (int) mInfos[17];
        result.activeAnonKb = (int) mInfos[20];
        result.inactiveAnonKb = (int) mInfos[21];
        result.activeFileKb = (int) mInfos[22];
        result.inactiveFileKb = (int) mInfos[23];
        result.swapTotalKb = (int) mInfos[8];
        result.swapFreeKb = (int) mInfos[9];
        result.cmaTotalKb = (int) mInfos[24];
        result.cmaFreeKb = (int) mInfos[25];
        result.totalIonKb = totalIonKb;
        result.gpuTotalUsageKb = gpuTotalUsageKb;
        result.gpuPrivateAllocationsKb = gpuPrivateAllocationsKb;
        result.dmaBufTotalExportedKb = dmaBufTotalExportedKb;
        result.unaccountedKb = (int) (mInfos[0] - accountedKb);
        return result;
    }

    static final class Metrics {
        public int activeAnonKb;
        public int activeFileKb;
        public int activeKb;
        public int availableKb;
        public int cmaFreeKb;
        public int cmaTotalKb;
        public int dmaBufTotalExportedKb;
        public int freeKb;
        public int gpuPrivateAllocationsKb;
        public int gpuTotalUsageKb;
        public int inactiveAnonKb;
        public int inactiveFileKb;
        public int inactiveKb;
        public int kernelStackKb;
        public int pageTablesKb;
        public int shmemKb;
        public int swapFreeKb;
        public int swapTotalKb;
        public int totalIonKb;
        public int totalKb;
        public int unaccountedKb;
        public int unreclaimableSlabKb;
        public int vmallocUsedKb;

        Metrics() {
        }
    }
}
