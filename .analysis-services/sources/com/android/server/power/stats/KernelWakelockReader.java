package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class KernelWakelockReader {
    private static final java.lang.String TAG = "KernelWakelockReader";
    private static final java.lang.String sSysClassWakeupDir = "/sys/class/wakeup";
    private static final java.lang.String sWakelockFile = "/proc/wakelocks";
    private static final java.lang.String sWakeupSourceFile = "/d/wakeup_sources";
    private static int sKernelWakelockUpdateVersion = 0;
    private static final int[] PROC_WAKELOCKS_FORMAT = {5129, 8201, 9, 8201, 9, 8201};
    private static final int[] WAKEUP_SOURCES_FORMAT = {4105, 8457, 265, 265, 265, 8457, 8457};
    private final java.lang.String[] mProcWakelocksName = new java.lang.String[3];
    private final long[] mProcWakelocksData = new long[4];
    private android.system.suspend.internal.ISuspendControlServiceInternal mSuspendControlService = null;
    private byte[] mKernelWakelockBuffer = new byte[32768];

    public com.android.server.power.stats.KernelWakelockStats readKernelWakelockStats(com.android.server.power.stats.KernelWakelockStats staleStats) {
        java.io.FileInputStream is;
        boolean wakeup_sources;
        int i;
        com.android.server.power.stats.KernelWakelockStats kernelWakelockStatsRemoveOldStats;
        boolean useSystemSuspend = new java.io.File(sSysClassWakeupDir).exists();
        if (useSystemSuspend) {
            synchronized (com.android.server.power.stats.KernelWakelockReader.class) {
                updateVersion(staleStats);
                if (getWakelockStatsFromSystemSuspend(staleStats) == null) {
                    android.util.Slog.w(TAG, "Failed to get wakelock stats from SystemSuspend");
                    return null;
                }
                return removeOldStats(staleStats);
            }
        }
        java.util.Arrays.fill(this.mKernelWakelockBuffer, (byte) 0);
        int len = 0;
        long startTime = android.os.SystemClock.uptimeMillis();
        int oldMask = android.os.StrictMode.allowThreadDiskReadsMask();
        try {
            try {
                is = new java.io.FileInputStream(sWakelockFile);
                wakeup_sources = false;
            } catch (java.io.FileNotFoundException e) {
                try {
                    java.io.FileInputStream is2 = new java.io.FileInputStream(sWakeupSourceFile);
                    is = is2;
                    wakeup_sources = true;
                } catch (java.io.FileNotFoundException e2) {
                    android.util.Slog.wtf(TAG, "neither /proc/wakelocks nor /d/wakeup_sources exists");
                    return null;
                }
            }
            while (true) {
                int cnt = is.read(this.mKernelWakelockBuffer, len, this.mKernelWakelockBuffer.length - len);
                if (cnt <= 0) {
                    break;
                }
                len += cnt;
            }
            is.close();
            android.os.StrictMode.setThreadPolicyMask(oldMask);
            long readTime = android.os.SystemClock.uptimeMillis() - startTime;
            if (readTime > 100) {
                android.util.Slog.w(TAG, "Reading wakelock stats took " + readTime + "ms");
            }
            if (len <= 0) {
                i = len;
            } else {
                if (len >= this.mKernelWakelockBuffer.length) {
                    android.util.Slog.wtf(TAG, "Kernel wake locks exceeded mKernelWakelockBuffer size " + this.mKernelWakelockBuffer.length);
                }
                i = 0;
                while (i < len) {
                    if (this.mKernelWakelockBuffer[i] == 0) {
                        break;
                    }
                    i++;
                }
                i = len;
            }
            synchronized (com.android.server.power.stats.KernelWakelockReader.class) {
                updateVersion(staleStats);
                if (getWakelockStatsFromSystemSuspend(staleStats) == null) {
                    android.util.Slog.w(TAG, "Failed to get Native wakelock stats from SystemSuspend");
                }
                parseProcWakelocks(this.mKernelWakelockBuffer, i, wakeup_sources, staleStats);
                kernelWakelockStatsRemoveOldStats = removeOldStats(staleStats);
            }
            return kernelWakelockStatsRemoveOldStats;
        } catch (java.io.IOException e3) {
            android.util.Slog.wtf(TAG, "failed to read kernel wakelocks", e3);
            return null;
        } finally {
            android.os.StrictMode.setThreadPolicyMask(oldMask);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceManager$ServiceNotFoundException */
    private android.system.suspend.internal.ISuspendControlServiceInternal waitForSuspendControlService() throws android.os.ServiceManager.ServiceNotFoundException {
        for (int i = 0; i < 5; i++) {
            this.mSuspendControlService = android.system.suspend.internal.ISuspendControlServiceInternal.Stub.asInterface(android.os.ServiceManager.getService("suspend_control_internal"));
            if (this.mSuspendControlService != null) {
                return this.mSuspendControlService;
            }
        }
        throw new android.os.ServiceManager.ServiceNotFoundException("suspend_control_internal");
    }

    private com.android.server.power.stats.KernelWakelockStats getWakelockStatsFromSystemSuspend(com.android.server.power.stats.KernelWakelockStats staleStats) {
        if (this.mSuspendControlService == null) {
            try {
                this.mSuspendControlService = waitForSuspendControlService();
            } catch (android.os.ServiceManager.ServiceNotFoundException e) {
                android.util.Slog.wtf(TAG, "Required service suspend_control not available", e);
                return null;
            }
        }
        try {
            android.system.suspend.internal.WakeLockInfo[] wlStats = this.mSuspendControlService.getWakeLockStats();
            updateWakelockStats(wlStats, staleStats);
            return staleStats;
        } catch (android.os.RemoteException e2) {
            android.util.Slog.wtf(TAG, "Failed to obtain wakelock stats from ISuspendControlService", e2);
            return null;
        } catch (java.lang.IllegalArgumentException e3) {
            android.util.Slog.wtf(TAG, "IllegalArgumentException occurred from ISuspendControlService", e3);
            return null;
        }
    }

    public com.android.server.power.stats.KernelWakelockStats updateWakelockStats(android.system.suspend.internal.WakeLockInfo[] wlStats, com.android.server.power.stats.KernelWakelockStats staleStats) {
        for (android.system.suspend.internal.WakeLockInfo info : wlStats) {
            if (!staleStats.containsKey(info.name)) {
                staleStats.put(info.name, new com.android.server.power.stats.KernelWakelockStats.Entry((int) info.activeCount, info.totalTime * 1000, info.isActive ? info.activeTime * 1000 : 0L, sKernelWakelockUpdateVersion));
            } else {
                com.android.server.power.stats.KernelWakelockStats.Entry kwlStats = staleStats.get(info.name);
                kwlStats.count = (int) info.activeCount;
                kwlStats.totalTimeUs = info.totalTime * 1000;
                kwlStats.activeTimeUs = info.isActive ? info.activeTime * 1000 : 0L;
                kwlStats.version = sKernelWakelockUpdateVersion;
            }
        }
        return staleStats;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:81:0x0137
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    public com.android.server.power.stats.KernelWakelockStats parseProcWakelocks(byte[] r26, int r27, boolean r28, com.android.server.power.stats.KernelWakelockStats r29) {
        /*
            Method dump skipped, instruction units count: 313
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.KernelWakelockReader.parseProcWakelocks(byte[], int, boolean, com.android.server.power.stats.KernelWakelockStats):com.android.server.power.stats.KernelWakelockStats");
    }

    public com.android.server.power.stats.KernelWakelockStats updateVersion(com.android.server.power.stats.KernelWakelockStats staleStats) {
        sKernelWakelockUpdateVersion++;
        staleStats.kernelWakelockVersion = sKernelWakelockUpdateVersion;
        return staleStats;
    }

    public com.android.server.power.stats.KernelWakelockStats removeOldStats(com.android.server.power.stats.KernelWakelockStats staleStats) {
        java.util.Iterator<com.android.server.power.stats.KernelWakelockStats.Entry> itr = staleStats.values().iterator();
        while (itr.hasNext()) {
            if (itr.next().version != sKernelWakelockUpdateVersion) {
                itr.remove();
            }
        }
        return staleStats;
    }
}
