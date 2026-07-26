package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class PowerStatsStore {
    private static final java.lang.String DIR_LOCK_FILENAME = ".lock";
    private static final long MAX_POWER_STATS_SPAN_STORAGE_BYTES = 102400;
    private static final java.lang.String POWER_STATS_DIR = "power-stats";
    private static final java.lang.String POWER_STATS_SPAN_FILE_EXTENSION = ".pss";
    private static final java.lang.String TAG = "PowerStatsStore";
    private final java.util.concurrent.locks.ReentrantLock mFileLock;
    private final android.os.Handler mHandler;
    private java.nio.channels.FileLock mJvmLock;
    private final java.io.File mLockFile;
    private final long mMaxStorageBytes;
    private final com.android.server.power.stats.PowerStatsSpan.SectionReader mSectionReader;
    private final java.io.File mStoreDir;
    private final java.io.File mSystemDir;
    private volatile java.util.List<com.android.server.power.stats.PowerStatsSpan.Metadata> mTableOfContents;

    public PowerStatsStore(java.io.File systemDir, android.os.Handler handler, com.android.server.power.stats.AggregatedPowerStatsConfig aggregatedPowerStatsConfig) {
        this(systemDir, MAX_POWER_STATS_SPAN_STORAGE_BYTES, handler, new com.android.server.power.stats.PowerStatsStore.DefaultSectionReader(aggregatedPowerStatsConfig));
    }

    public PowerStatsStore(java.io.File systemDir, long maxStorageBytes, android.os.Handler handler, com.android.server.power.stats.PowerStatsSpan.SectionReader sectionReader) {
        this.mFileLock = new java.util.concurrent.locks.ReentrantLock();
        this.mSystemDir = systemDir;
        this.mStoreDir = new java.io.File(systemDir, POWER_STATS_DIR);
        this.mLockFile = new java.io.File(this.mStoreDir, DIR_LOCK_FILENAME);
        this.mHandler = handler;
        this.mMaxStorageBytes = maxStorageBytes;
        this.mSectionReader = sectionReader;
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.stats.PowerStatsStore$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.maybeClearLegacyStore();
            }
        });
    }

    public java.util.List<com.android.server.power.stats.PowerStatsSpan.Metadata> getTableOfContents() {
        java.util.List<com.android.server.power.stats.PowerStatsSpan.Metadata> toc = this.mTableOfContents;
        if (toc != null) {
            return toc;
        }
        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.newBinaryPullParser();
        lockStoreDirectory();
        try {
            java.util.List<com.android.server.power.stats.PowerStatsSpan.Metadata> toc2 = new java.util.ArrayList<>();
            for (java.io.File file : this.mStoreDir.listFiles()) {
                java.lang.String fileName = file.getName();
                if (fileName.endsWith(POWER_STATS_SPAN_FILE_EXTENSION)) {
                    try {
                        java.io.InputStream inputStream = new java.io.BufferedInputStream(new java.io.FileInputStream(file));
                        try {
                            parser.setInput(inputStream, java.nio.charset.StandardCharsets.UTF_8.name());
                            com.android.server.power.stats.PowerStatsSpan.Metadata metadata = com.android.server.power.stats.PowerStatsSpan.Metadata.read(parser);
                            if (metadata == null) {
                                android.util.Slog.e(TAG, "Removing incompatible PowerStatsSpan file: " + fileName);
                                file.delete();
                            } else {
                                toc2.add(metadata);
                            }
                            inputStream.close();
                        } catch (java.lang.Throwable th) {
                            try {
                                inputStream.close();
                            } catch (java.lang.Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                    } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                        android.util.Slog.wtf(TAG, "Cannot read PowerStatsSpan file: " + fileName);
                    }
                }
            }
            toc2.sort(com.android.server.power.stats.PowerStatsSpan.Metadata.COMPARATOR);
            this.mTableOfContents = java.util.Collections.unmodifiableList(toc2);
            return toc2;
        } finally {
            unlockStoreDirectory();
        }
    }

    public void storePowerStatsSpan(final com.android.server.power.stats.PowerStatsSpan span) {
        maybeClearLegacyStore();
        lockStoreDirectory();
        try {
            if (!this.mStoreDir.exists() && !this.mStoreDir.mkdirs()) {
                android.util.Slog.e(TAG, "Could not create a directory for power stats store");
                return;
            }
            android.util.AtomicFile file = new android.util.AtomicFile(makePowerStatsSpanFilename(span.getId()));
            file.write(new java.util.function.Consumer() { // from class: com.android.server.power.stats.PowerStatsStore$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.power.stats.PowerStatsStore.lambda$storePowerStatsSpan$0(span, (java.io.FileOutputStream) obj);
                }
            });
            this.mTableOfContents = null;
            removeOldSpansLocked();
        } finally {
            unlockStoreDirectory();
        }
    }

    static /* synthetic */ void lambda$storePowerStatsSpan$0(com.android.server.power.stats.PowerStatsSpan span, java.io.FileOutputStream out) {
        try {
            span.writeXml(out, android.util.Xml.newBinarySerializer());
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public com.android.server.power.stats.PowerStatsSpan loadPowerStatsSpan(long id, java.lang.String... sectionTypes) {
        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.newBinaryPullParser();
        lockStoreDirectory();
        try {
            java.io.File file = makePowerStatsSpanFilename(id);
            try {
                java.io.InputStream inputStream = new java.io.BufferedInputStream(new java.io.FileInputStream(file));
                try {
                    com.android.server.power.stats.PowerStatsSpan powerStatsSpan = com.android.server.power.stats.PowerStatsSpan.read(inputStream, parser, this.mSectionReader, sectionTypes);
                    inputStream.close();
                    return powerStatsSpan;
                } catch (java.lang.Throwable th) {
                    try {
                        inputStream.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.wtf(TAG, "Cannot read PowerStatsSpan file: " + file, e);
                unlockStoreDirectory();
                return null;
            }
        } finally {
            unlockStoreDirectory();
        }
    }

    void storeAggregatedPowerStats(com.android.server.power.stats.AggregatedPowerStats stats) {
        com.android.server.power.stats.PowerStatsSpan span = createPowerStatsSpan(stats);
        if (span == null) {
            return;
        }
        storePowerStatsSpan(span);
    }

    static com.android.server.power.stats.PowerStatsSpan createPowerStatsSpan(com.android.server.power.stats.AggregatedPowerStats stats) {
        long duration;
        java.util.List<com.android.server.power.stats.AggregatedPowerStats.ClockUpdate> clockUpdates = stats.getClockUpdates();
        if (clockUpdates.isEmpty()) {
            android.util.Slog.w(TAG, "No clock updates in aggregated power stats " + stats);
            return null;
        }
        long monotonicTime = clockUpdates.get(0).monotonicTime;
        long durationSum = 0;
        com.android.server.power.stats.PowerStatsSpan span = new com.android.server.power.stats.PowerStatsSpan(monotonicTime);
        for (int i = 0; i < clockUpdates.size(); i++) {
            com.android.server.power.stats.AggregatedPowerStats.ClockUpdate clockUpdate = clockUpdates.get(i);
            if (i == clockUpdates.size() - 1) {
                duration = stats.getDuration() - durationSum;
            } else {
                duration = clockUpdate.monotonicTime - monotonicTime;
            }
            long duration2 = duration;
            span.addTimeFrame(clockUpdate.monotonicTime, clockUpdate.currentTime, duration2);
            monotonicTime = clockUpdate.monotonicTime;
            durationSum += duration2;
        }
        span.addSection(new com.android.server.power.stats.AggregatedPowerStatsSection(stats));
        return span;
    }

    public void storeBatteryUsageStats(long monotonicStartTime, android.os.BatteryUsageStats batteryUsageStats) {
        com.android.server.power.stats.PowerStatsSpan span = new com.android.server.power.stats.PowerStatsSpan(monotonicStartTime);
        span.addTimeFrame(monotonicStartTime, batteryUsageStats.getStatsStartTimestamp(), batteryUsageStats.getStatsDuration());
        span.addSection(new com.android.server.power.stats.BatteryUsageStatsSection(batteryUsageStats));
        storePowerStatsSpan(span);
    }

    private java.io.File makePowerStatsSpanFilename(long id) {
        return new java.io.File(this.mStoreDir, java.lang.String.format(java.util.Locale.ENGLISH, "%019d", java.lang.Long.valueOf(id)) + POWER_STATS_SPAN_FILE_EXTENSION);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeClearLegacyStore() {
        java.io.File legacyStoreDir = new java.io.File(this.mSystemDir, com.android.server.power.stats.BatteryUsageStatsSection.TYPE);
        if (legacyStoreDir.exists()) {
            android.os.FileUtils.deleteContentsAndDir(legacyStoreDir);
        }
    }

    private void lockStoreDirectory() {
        this.mFileLock.lock();
        try {
            this.mLockFile.getParentFile().mkdirs();
            this.mLockFile.createNewFile();
            this.mJvmLock = java.nio.channels.FileChannel.open(this.mLockFile.toPath(), java.nio.file.StandardOpenOption.WRITE).lock();
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Cannot lock snapshot directory", e);
        }
    }

    private void unlockStoreDirectory() {
        try {
            try {
                this.mJvmLock.close();
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Cannot unlock snapshot directory", e);
            }
        } finally {
            this.mFileLock.unlock();
        }
    }

    private void removeOldSpansLocked() {
        java.util.Map.Entry<java.io.File, java.lang.Long> entry;
        long totalSize = 0;
        java.util.TreeMap<java.io.File, java.lang.Long> mFileSizes = new java.util.TreeMap<>();
        for (java.io.File file : this.mStoreDir.listFiles()) {
            long fileSize = file.length();
            totalSize += fileSize;
            if (file.getName().endsWith(POWER_STATS_SPAN_FILE_EXTENSION)) {
                mFileSizes.put(file, java.lang.Long.valueOf(fileSize));
            }
        }
        while (totalSize > this.mMaxStorageBytes && (entry = mFileSizes.firstEntry()) != null) {
            java.io.File file2 = entry.getKey();
            if (!file2.delete()) {
                android.util.Slog.e(TAG, "Cannot delete power stats span " + file2);
            }
            totalSize -= entry.getValue().longValue();
            mFileSizes.remove(file2);
            this.mTableOfContents = null;
        }
    }

    public void reset() {
        lockStoreDirectory();
        try {
            for (java.io.File file : this.mStoreDir.listFiles()) {
                if (file.getName().endsWith(POWER_STATS_SPAN_FILE_EXTENSION) && !file.delete()) {
                    android.util.Slog.e(TAG, "Cannot delete power stats span " + file);
                }
            }
            this.mTableOfContents = java.util.List.of();
        } finally {
            unlockStoreDirectory();
        }
    }

    public void dumpTableOfContents(android.util.IndentingPrintWriter ipw) {
        ipw.println("Power stats store TOC");
        ipw.increaseIndent();
        java.util.List<com.android.server.power.stats.PowerStatsSpan.Metadata> contents = getTableOfContents();
        for (com.android.server.power.stats.PowerStatsSpan.Metadata metadata : contents) {
            metadata.dump(ipw);
        }
        ipw.decreaseIndent();
    }

    public void dump(android.util.IndentingPrintWriter ipw) {
        ipw.println("Power stats store");
        ipw.increaseIndent();
        java.util.List<com.android.server.power.stats.PowerStatsSpan.Metadata> contents = getTableOfContents();
        for (com.android.server.power.stats.PowerStatsSpan.Metadata metadata : contents) {
            com.android.server.power.stats.PowerStatsSpan span = loadPowerStatsSpan(metadata.getId(), new java.lang.String[0]);
            if (span != null) {
                span.dump(ipw);
            }
        }
        ipw.decreaseIndent();
    }

    private static class DefaultSectionReader implements com.android.server.power.stats.PowerStatsSpan.SectionReader {
        private final com.android.server.power.stats.AggregatedPowerStatsConfig mAggregatedPowerStatsConfig;

        DefaultSectionReader(com.android.server.power.stats.AggregatedPowerStatsConfig aggregatedPowerStatsConfig) {
            this.mAggregatedPowerStatsConfig = aggregatedPowerStatsConfig;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:11:0x001c  */
        @Override // com.android.server.power.stats.PowerStatsSpan.SectionReader
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public com.android.server.power.stats.PowerStatsSpan.Section read(java.lang.String r3, com.android.modules.utils.TypedXmlPullParser r4) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            /*
                r2 = this;
                int r0 = r3.hashCode()
                switch(r0) {
                    case 222490035: goto L12;
                    case 2132539023: goto L8;
                    default: goto L7;
                }
            L7:
                goto L1c
            L8:
                java.lang.String r0 = "aggregated-power-stats"
                boolean r0 = r3.equals(r0)
                if (r0 == 0) goto L7
                r0 = 0
                goto L1d
            L12:
                java.lang.String r0 = "battery-usage-stats"
                boolean r0 = r3.equals(r0)
                if (r0 == 0) goto L7
                r0 = 1
                goto L1d
            L1c:
                r0 = -1
            L1d:
                switch(r0) {
                    case 0: goto L2c;
                    case 1: goto L22;
                    default: goto L20;
                }
            L20:
                r0 = 0
                return r0
            L22:
                com.android.server.power.stats.BatteryUsageStatsSection r0 = new com.android.server.power.stats.BatteryUsageStatsSection
                android.os.BatteryUsageStats r1 = android.os.BatteryUsageStats.createFromXml(r4)
                r0.<init>(r1)
                return r0
            L2c:
                com.android.server.power.stats.AggregatedPowerStatsSection r0 = new com.android.server.power.stats.AggregatedPowerStatsSection
                com.android.server.power.stats.AggregatedPowerStatsConfig r1 = r2.mAggregatedPowerStatsConfig
                com.android.server.power.stats.AggregatedPowerStats r1 = com.android.server.power.stats.AggregatedPowerStats.createFromXml(r4, r1)
                r0.<init>(r1)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.PowerStatsStore.DefaultSectionReader.read(java.lang.String, com.android.modules.utils.TypedXmlPullParser):com.android.server.power.stats.PowerStatsSpan$Section");
        }
    }
}
