package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public final class JobPackageTracker {
    static final long BATCHING_TIME = 1800000;
    private static final int EVENT_BUFFER_SIZE = 100;
    public static final int EVENT_CMD_MASK = 255;
    public static final int EVENT_NULL = 0;
    public static final int EVENT_START_JOB = 1;
    public static final int EVENT_START_PERIODIC_JOB = 3;
    public static final int EVENT_STOP_JOB = 2;
    public static final int EVENT_STOP_PERIODIC_JOB = 4;
    public static final int EVENT_STOP_REASON_MASK = 65280;
    public static final int EVENT_STOP_REASON_SHIFT = 8;
    static final int NUM_HISTORY = 5;
    private final com.android.internal.util.RingBufferIndices mEventIndices = new com.android.internal.util.RingBufferIndices(100);
    private final int[] mEventCmds = new int[100];
    private final long[] mEventTimes = new long[100];
    private final int[] mEventUids = new int[100];
    private final java.lang.String[] mEventTags = new java.lang.String[100];
    private final int[] mEventJobIds = new int[100];
    private final java.lang.String[] mEventReasons = new java.lang.String[100];
    com.android.server.job.JobPackageTracker.DataSet mCurDataSet = new com.android.server.job.JobPackageTracker.DataSet();
    com.android.server.job.JobPackageTracker.DataSet[] mLastDataSets = new com.android.server.job.JobPackageTracker.DataSet[5];

    public void addEvent(int cmd, int uid, java.lang.String tag, int jobId, int stopReason, java.lang.String debugReason) {
        int index = this.mEventIndices.add();
        this.mEventCmds[index] = ((stopReason << 8) & 65280) | cmd;
        this.mEventTimes[index] = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        this.mEventUids[index] = uid;
        this.mEventTags[index] = tag;
        this.mEventJobIds[index] = jobId;
        this.mEventReasons[index] = debugReason;
    }

    static final class PackageEntry {
        int activeCount;
        int activeNesting;
        long activeStartTime;
        int activeTopCount;
        int activeTopNesting;
        long activeTopStartTime;
        boolean hadActive;
        boolean hadActiveTop;
        boolean hadPending;
        long lastActiveTime;
        long pastActiveTime;
        long pastActiveTopTime;
        long pastPendingTime;
        int pendingCount;
        int pendingNesting;
        long pendingStartTime;
        final android.util.SparseIntArray stopReasons = new android.util.SparseIntArray();

        PackageEntry() {
        }

        public long getActiveTime(long now) {
            long time = this.pastActiveTime;
            if (this.activeNesting > 0) {
                return time + (now - this.activeStartTime);
            }
            return time;
        }

        public long getActiveTopTime(long now) {
            long time = this.pastActiveTopTime;
            if (this.activeTopNesting > 0) {
                return time + (now - this.activeTopStartTime);
            }
            return time;
        }

        public long getPendingTime(long now) {
            long time = this.pastPendingTime;
            if (this.pendingNesting > 0) {
                return time + (now - this.pendingStartTime);
            }
            return time;
        }
    }

    static final class DataSet {
        final android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.job.JobPackageTracker.PackageEntry>> mEntries;
        int mMaxFgActive;
        int mMaxTotalActive;
        final long mStartClockTime;
        final long mStartElapsedTime;
        final long mStartUptimeTime;
        long mSummedTime;

        public DataSet(com.android.server.job.JobPackageTracker.DataSet otherTimes) {
            this.mEntries = new android.util.SparseArray<>();
            this.mStartUptimeTime = otherTimes.mStartUptimeTime;
            this.mStartElapsedTime = otherTimes.mStartElapsedTime;
            this.mStartClockTime = otherTimes.mStartClockTime;
        }

        public DataSet() {
            this.mEntries = new android.util.SparseArray<>();
            this.mStartUptimeTime = com.android.server.job.JobSchedulerService.sUptimeMillisClock.millis();
            this.mStartElapsedTime = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
            this.mStartClockTime = com.android.server.job.JobSchedulerService.sSystemClock.millis();
        }

        private com.android.server.job.JobPackageTracker.PackageEntry getOrCreateEntry(int uid, java.lang.String pkg) {
            android.util.ArrayMap<java.lang.String, com.android.server.job.JobPackageTracker.PackageEntry> uidMap = this.mEntries.get(uid);
            if (uidMap == null) {
                uidMap = new android.util.ArrayMap<>();
                this.mEntries.put(uid, uidMap);
            }
            com.android.server.job.JobPackageTracker.PackageEntry entry = uidMap.get(pkg);
            if (entry == null) {
                com.android.server.job.JobPackageTracker.PackageEntry entry2 = new com.android.server.job.JobPackageTracker.PackageEntry();
                uidMap.put(pkg, entry2);
                return entry2;
            }
            return entry;
        }

        public com.android.server.job.JobPackageTracker.PackageEntry getEntry(int uid, java.lang.String pkg) {
            android.util.ArrayMap<java.lang.String, com.android.server.job.JobPackageTracker.PackageEntry> uidMap = this.mEntries.get(uid);
            if (uidMap == null) {
                return null;
            }
            return uidMap.get(pkg);
        }

        long getTotalTime(long now) {
            if (this.mSummedTime > 0) {
                return this.mSummedTime;
            }
            return now - this.mStartUptimeTime;
        }

        void incPending(int uid, java.lang.String pkg, long now) {
            com.android.server.job.JobPackageTracker.PackageEntry pe = getOrCreateEntry(uid, pkg);
            if (pe.pendingNesting == 0) {
                pe.pendingStartTime = now;
                pe.pendingCount++;
            }
            pe.pendingNesting++;
        }

        void decPending(int uid, java.lang.String pkg, long now) {
            com.android.server.job.JobPackageTracker.PackageEntry pe = getOrCreateEntry(uid, pkg);
            if (pe.pendingNesting == 1) {
                pe.pastPendingTime += now - pe.pendingStartTime;
            }
            pe.pendingNesting--;
        }

        void incActive(int uid, java.lang.String pkg, long now) {
            com.android.server.job.JobPackageTracker.PackageEntry pe = getOrCreateEntry(uid, pkg);
            if (pe.activeNesting == 0) {
                pe.activeStartTime = now;
                pe.activeCount++;
            }
            pe.activeNesting++;
        }

        void decActive(int uid, java.lang.String pkg, long now, int stopReason) {
            com.android.server.job.JobPackageTracker.PackageEntry pe = getOrCreateEntry(uid, pkg);
            if (pe.activeNesting == 1) {
                pe.pastActiveTime += now - pe.activeStartTime;
            }
            pe.activeNesting--;
            int count = pe.stopReasons.get(stopReason, 0);
            pe.stopReasons.put(stopReason, count + 1);
            pe.lastActiveTime = now;
        }

        void incActiveTop(int uid, java.lang.String pkg, long now) {
            com.android.server.job.JobPackageTracker.PackageEntry pe = getOrCreateEntry(uid, pkg);
            if (pe.activeTopNesting == 0) {
                pe.activeTopStartTime = now;
                pe.activeTopCount++;
            }
            pe.activeTopNesting++;
        }

        void decActiveTop(int uid, java.lang.String pkg, long now, int stopReason) {
            com.android.server.job.JobPackageTracker.PackageEntry pe = getOrCreateEntry(uid, pkg);
            if (pe.activeTopNesting == 1) {
                pe.pastActiveTopTime += now - pe.activeTopStartTime;
            }
            pe.activeTopNesting--;
            int count = pe.stopReasons.get(stopReason, 0);
            pe.stopReasons.put(stopReason, count + 1);
            pe.lastActiveTime = now;
        }

        void finish(com.android.server.job.JobPackageTracker.DataSet next, long now) {
            for (int i = this.mEntries.size() - 1; i >= 0; i--) {
                android.util.ArrayMap<java.lang.String, com.android.server.job.JobPackageTracker.PackageEntry> uidMap = this.mEntries.valueAt(i);
                for (int j = uidMap.size() - 1; j >= 0; j--) {
                    com.android.server.job.JobPackageTracker.PackageEntry pe = uidMap.valueAt(j);
                    if (pe.activeNesting > 0 || pe.activeTopNesting > 0 || pe.pendingNesting > 0) {
                        com.android.server.job.JobPackageTracker.PackageEntry nextPe = next.getOrCreateEntry(this.mEntries.keyAt(i), uidMap.keyAt(j));
                        nextPe.activeStartTime = now;
                        nextPe.activeNesting = pe.activeNesting;
                        nextPe.activeTopStartTime = now;
                        nextPe.activeTopNesting = pe.activeTopNesting;
                        nextPe.pendingStartTime = now;
                        nextPe.pendingNesting = pe.pendingNesting;
                        if (pe.activeNesting > 0) {
                            pe.pastActiveTime += now - pe.activeStartTime;
                            pe.activeNesting = 0;
                        }
                        if (pe.activeTopNesting > 0) {
                            pe.pastActiveTopTime += now - pe.activeTopStartTime;
                            pe.activeTopNesting = 0;
                        }
                        if (pe.pendingNesting > 0) {
                            pe.pastPendingTime += now - pe.pendingStartTime;
                            pe.pendingNesting = 0;
                        }
                    }
                }
            }
        }

        void addTo(com.android.server.job.JobPackageTracker.DataSet out, long now) {
            out.mSummedTime += getTotalTime(now);
            for (int i = this.mEntries.size() - 1; i >= 0; i--) {
                android.util.ArrayMap<java.lang.String, com.android.server.job.JobPackageTracker.PackageEntry> uidMap = this.mEntries.valueAt(i);
                for (int j = uidMap.size() - 1; j >= 0; j--) {
                    com.android.server.job.JobPackageTracker.PackageEntry pe = uidMap.valueAt(j);
                    com.android.server.job.JobPackageTracker.PackageEntry outPe = out.getOrCreateEntry(this.mEntries.keyAt(i), uidMap.keyAt(j));
                    outPe.pastActiveTime += pe.pastActiveTime;
                    outPe.activeCount += pe.activeCount;
                    outPe.pastActiveTopTime += pe.pastActiveTopTime;
                    outPe.activeTopCount += pe.activeTopCount;
                    outPe.pastPendingTime += pe.pastPendingTime;
                    outPe.pendingCount += pe.pendingCount;
                    if (pe.activeNesting > 0) {
                        outPe.pastActiveTime += now - pe.activeStartTime;
                        outPe.hadActive = true;
                    }
                    if (pe.activeTopNesting > 0) {
                        outPe.pastActiveTopTime += now - pe.activeTopStartTime;
                        outPe.hadActiveTop = true;
                    }
                    if (pe.pendingNesting > 0) {
                        outPe.pastPendingTime += now - pe.pendingStartTime;
                        outPe.hadPending = true;
                    }
                    for (int k = pe.stopReasons.size() - 1; k >= 0; k--) {
                        int type = pe.stopReasons.keyAt(k);
                        outPe.stopReasons.put(type, outPe.stopReasons.get(type, 0) + pe.stopReasons.valueAt(k));
                    }
                }
            }
            int i2 = this.mMaxTotalActive;
            if (i2 > out.mMaxTotalActive) {
                out.mMaxTotalActive = this.mMaxTotalActive;
            }
            if (this.mMaxFgActive > out.mMaxFgActive) {
                out.mMaxFgActive = this.mMaxFgActive;
            }
        }

        boolean printDuration(android.util.IndentingPrintWriter pw, long period, long duration, int count, java.lang.String suffix) {
            float fraction = duration / period;
            int percent = (int) ((100.0f * fraction) + 0.5f);
            if (percent > 0) {
                pw.print(percent);
                pw.print("% ");
                pw.print(count);
                pw.print("x ");
                pw.print(suffix);
                return true;
            }
            if (count > 0) {
                pw.print(count);
                pw.print("x ");
                pw.print(suffix);
                return true;
            }
            return false;
        }

        void dump(android.util.IndentingPrintWriter pw, java.lang.String header, long now, long nowElapsed, int filterAppId) {
            com.android.server.job.JobPackageTracker.DataSet dataSet = this;
            int i = filterAppId;
            long period = dataSet.getTotalTime(now);
            pw.print(header);
            pw.print(" at ");
            pw.print(android.text.format.DateFormat.format("yyyy-MM-dd-HH-mm-ss", dataSet.mStartClockTime).toString());
            pw.print(" (");
            android.util.TimeUtils.formatDuration(dataSet.mStartElapsedTime, nowElapsed, pw);
            pw.print(") over ");
            android.util.TimeUtils.formatDuration(period, pw);
            pw.println(":");
            pw.increaseIndent();
            pw.print("Max concurrency: ");
            pw.print(dataSet.mMaxTotalActive);
            pw.print(" total, ");
            pw.print(dataSet.mMaxFgActive);
            pw.println(" foreground");
            pw.println();
            int NE = dataSet.mEntries.size();
            int i2 = 0;
            while (i2 < NE) {
                int uid = dataSet.mEntries.keyAt(i2);
                if (i == -1 || i == android.os.UserHandle.getAppId(uid)) {
                    android.util.ArrayMap<java.lang.String, com.android.server.job.JobPackageTracker.PackageEntry> uidMap = dataSet.mEntries.valueAt(i2);
                    int NP = uidMap.size();
                    int j = 0;
                    while (j < NP) {
                        com.android.server.job.JobPackageTracker.PackageEntry pe = uidMap.valueAt(j);
                        android.os.UserHandle.formatUid(pw, uid);
                        int NP2 = NP;
                        pw.print(" / ");
                        pw.print(uidMap.keyAt(j));
                        pw.println(":");
                        pw.increaseIndent();
                        int j2 = j;
                        int uid2 = uid;
                        android.util.ArrayMap<java.lang.String, com.android.server.job.JobPackageTracker.PackageEntry> uidMap2 = uidMap;
                        int NE2 = NE;
                        int i3 = i2;
                        if (printDuration(pw, period, pe.getPendingTime(now), pe.pendingCount, "pending")) {
                            pw.print(" ");
                        }
                        if (printDuration(pw, period, pe.getActiveTime(now), pe.activeCount, com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE)) {
                            pw.print(" ");
                        }
                        printDuration(pw, period, pe.getActiveTopTime(now), pe.activeTopCount, "active-top");
                        if (pe.pendingNesting > 0 || pe.hadPending) {
                            pw.print(" (pending)");
                        }
                        if (pe.activeNesting > 0 || pe.hadActive) {
                            pw.print(" (active)");
                        }
                        if (pe.activeTopNesting > 0 || pe.hadActiveTop) {
                            pw.print(" (active-top)");
                        }
                        pw.print(" last-active :" + pe.lastActiveTime);
                        pw.println();
                        if (pe.stopReasons.size() > 0) {
                            for (int k = 0; k < pe.stopReasons.size(); k++) {
                                if (k > 0) {
                                    pw.print(", ");
                                }
                                pw.print(pe.stopReasons.valueAt(k));
                                pw.print("x ");
                                pw.print(android.app.job.JobParameters.getInternalReasonCodeDescription(pe.stopReasons.keyAt(k)));
                            }
                            pw.println();
                        }
                        pw.decreaseIndent();
                        j = j2 + 1;
                        NP = NP2;
                        uid = uid2;
                        uidMap = uidMap2;
                        NE = NE2;
                        i2 = i3;
                    }
                }
                i2++;
                dataSet = this;
                i = filterAppId;
                NE = NE;
            }
            pw.decreaseIndent();
        }

        private void printPackageEntryState(android.util.proto.ProtoOutputStream proto, long fieldId, long duration, int count) {
            long token = proto.start(fieldId);
            proto.write(1112396529665L, duration);
            proto.write(1120986464258L, count);
            proto.end(token);
        }

        void dump(android.util.proto.ProtoOutputStream proto, long fieldId, long now, long nowElapsed, int filterUid) {
            int i;
            int i2 = filterUid;
            long token = proto.start(fieldId);
            long period = getTotalTime(now);
            proto.write(1112396529665L, this.mStartClockTime);
            proto.write(1112396529666L, nowElapsed - this.mStartElapsedTime);
            proto.write(1112396529667L, period);
            int NE = this.mEntries.size();
            int i3 = 0;
            while (i3 < NE) {
                int uid = this.mEntries.keyAt(i3);
                if (i2 != -1 && i2 != android.os.UserHandle.getAppId(uid)) {
                    i = i3;
                } else {
                    android.util.ArrayMap<java.lang.String, com.android.server.job.JobPackageTracker.PackageEntry> uidMap = this.mEntries.valueAt(i3);
                    int NP = uidMap.size();
                    int j = 0;
                    while (j < NP) {
                        int NP2 = NP;
                        int i4 = i3;
                        long peToken = proto.start(2246267895812L);
                        com.android.server.job.JobPackageTracker.PackageEntry pe = uidMap.valueAt(j);
                        proto.write(1120986464257L, uid);
                        int j2 = j;
                        android.util.ArrayMap<java.lang.String, com.android.server.job.JobPackageTracker.PackageEntry> uidMap2 = uidMap;
                        proto.write(1138166333442L, uidMap.keyAt(j));
                        long period2 = period;
                        int uid2 = uid;
                        int NE2 = NE;
                        printPackageEntryState(proto, 1146756268035L, pe.getPendingTime(now), pe.pendingCount);
                        printPackageEntryState(proto, 1146756268036L, pe.getActiveTime(now), pe.activeCount);
                        printPackageEntryState(proto, 1146756268037L, pe.getActiveTopTime(now), pe.activeTopCount);
                        proto.write(1133871366150L, pe.pendingNesting > 0 || pe.hadPending);
                        proto.write(1133871366151L, pe.activeNesting > 0 || pe.hadActive);
                        proto.write(1133871366152L, pe.activeTopNesting > 0 || pe.hadActiveTop);
                        for (int k = 0; k < pe.stopReasons.size(); k++) {
                            long srcToken = proto.start(2246267895817L);
                            proto.write(1159641169921L, pe.stopReasons.keyAt(k));
                            proto.write(1120986464258L, pe.stopReasons.valueAt(k));
                            proto.end(srcToken);
                        }
                        proto.end(peToken);
                        j = j2 + 1;
                        i3 = i4;
                        uidMap = uidMap2;
                        NP = NP2;
                        NE = NE2;
                        uid = uid2;
                        period = period2;
                    }
                    i = i3;
                }
                i3 = i + 1;
                i2 = filterUid;
                NE = NE;
                period = period;
            }
            proto.write(1120986464261L, this.mMaxTotalActive);
            proto.write(1120986464262L, this.mMaxFgActive);
            proto.end(token);
        }
    }

    void rebatchIfNeeded(long now) {
        long totalTime = this.mCurDataSet.getTotalTime(now);
        if (totalTime > 1800000) {
            com.android.server.job.JobPackageTracker.DataSet last = this.mCurDataSet;
            last.mSummedTime = totalTime;
            this.mCurDataSet = new com.android.server.job.JobPackageTracker.DataSet();
            last.finish(this.mCurDataSet, now);
            java.lang.System.arraycopy(this.mLastDataSets, 0, this.mLastDataSets, 1, this.mLastDataSets.length - 1);
            this.mLastDataSets[0] = last;
        }
    }

    public void notePending(com.android.server.job.controllers.JobStatus job) {
        long now = com.android.server.job.JobSchedulerService.sUptimeMillisClock.millis();
        job.madePending = now;
        rebatchIfNeeded(now);
        this.mCurDataSet.incPending(job.getSourceUid(), job.getSourcePackageName(), now);
    }

    public void noteNonpending(com.android.server.job.controllers.JobStatus job) {
        long now = com.android.server.job.JobSchedulerService.sUptimeMillisClock.millis();
        this.mCurDataSet.decPending(job.getSourceUid(), job.getSourcePackageName(), now);
        rebatchIfNeeded(now);
    }

    public void noteActive(com.android.server.job.controllers.JobStatus job) {
        long now = com.android.server.job.JobSchedulerService.sUptimeMillisClock.millis();
        job.madeActive = now;
        rebatchIfNeeded(now);
        if (job.lastEvaluatedBias >= 40) {
            this.mCurDataSet.incActiveTop(job.getSourceUid(), job.getSourcePackageName(), now);
        } else {
            this.mCurDataSet.incActive(job.getSourceUid(), job.getSourcePackageName(), now);
        }
        addEvent(job.getJob().isPeriodic() ? 3 : 1, job.getSourceUid(), job.getBatteryName(), job.getJobId(), 0, null);
    }

    public void noteInactive(com.android.server.job.controllers.JobStatus job, int stopReason, java.lang.String debugReason) {
        long now = com.android.server.job.JobSchedulerService.sUptimeMillisClock.millis();
        if (job.lastEvaluatedBias >= 40) {
            this.mCurDataSet.decActiveTop(job.getSourceUid(), job.getSourcePackageName(), now, stopReason);
        } else {
            this.mCurDataSet.decActive(job.getSourceUid(), job.getSourcePackageName(), now, stopReason);
        }
        rebatchIfNeeded(now);
        addEvent(job.getJob().isPeriodic() ? 4 : 2, job.getSourceUid(), job.getBatteryName(), job.getJobId(), stopReason, debugReason);
    }

    public void noteConcurrency(int totalActive, int fgActive) {
        if (totalActive > this.mCurDataSet.mMaxTotalActive) {
            this.mCurDataSet.mMaxTotalActive = totalActive;
        }
        if (fgActive > this.mCurDataSet.mMaxFgActive) {
            this.mCurDataSet.mMaxFgActive = fgActive;
        }
    }

    public float getLoadFactor(com.android.server.job.controllers.JobStatus job) {
        int uid = job.getSourceUid();
        java.lang.String pkg = job.getSourcePackageName();
        com.android.server.job.JobPackageTracker.PackageEntry cur = this.mCurDataSet.getEntry(uid, pkg);
        com.android.server.job.JobPackageTracker.PackageEntry last = this.mLastDataSets[0] != null ? this.mLastDataSets[0].getEntry(uid, pkg) : null;
        if (cur == null && last == null) {
            return 0.0f;
        }
        long now = com.android.server.job.JobSchedulerService.sUptimeMillisClock.millis();
        long time = cur != null ? 0 + cur.getActiveTime(now) + cur.getPendingTime(now) : 0L;
        long period = this.mCurDataSet.getTotalTime(now);
        if (last != null) {
            time += last.getActiveTime(now) + last.getPendingTime(now);
            period += this.mLastDataSets[0].getTotalTime(now);
        }
        return time / period;
    }

    void dump(android.util.IndentingPrintWriter pw, int filterAppId) {
        com.android.server.job.JobPackageTracker.DataSet total;
        long now = com.android.server.job.JobSchedulerService.sUptimeMillisClock.millis();
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        if (this.mLastDataSets[0] != null) {
            total = new com.android.server.job.JobPackageTracker.DataSet(this.mLastDataSets[0]);
            this.mLastDataSets[0].addTo(total, now);
        } else {
            total = new com.android.server.job.JobPackageTracker.DataSet(this.mCurDataSet);
        }
        this.mCurDataSet.addTo(total, now);
        for (int i = 1; i < this.mLastDataSets.length; i++) {
            if (this.mLastDataSets[i] != null) {
                this.mLastDataSets[i].dump(pw, "Historical stats", now, nowElapsed, filterAppId);
                pw.println();
            }
        }
        total.dump(pw, "Current stats", now, nowElapsed, filterAppId);
    }

    public void dump(android.util.proto.ProtoOutputStream proto, long fieldId, int filterUid) {
        com.android.server.job.JobPackageTracker.DataSet total;
        int i;
        long token = proto.start(fieldId);
        long now = com.android.server.job.JobSchedulerService.sUptimeMillisClock.millis();
        long nowElapsed = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        if (this.mLastDataSets[0] != null) {
            total = new com.android.server.job.JobPackageTracker.DataSet(this.mLastDataSets[0]);
            this.mLastDataSets[0].addTo(total, now);
        } else {
            total = new com.android.server.job.JobPackageTracker.DataSet(this.mCurDataSet);
        }
        this.mCurDataSet.addTo(total, now);
        int i2 = 1;
        while (i2 < this.mLastDataSets.length) {
            if (this.mLastDataSets[i2] == null) {
                i = i2;
            } else {
                i = i2;
                this.mLastDataSets[i2].dump(proto, 2246267895809L, now, nowElapsed, filterUid);
            }
            i2 = i + 1;
        }
        total.dump(proto, 1146756268034L, now, nowElapsed, filterUid);
        proto.end(token);
    }

    boolean dumpHistory(android.util.IndentingPrintWriter pw, int filterAppId) {
        int cmd;
        java.lang.String label;
        int size = this.mEventIndices.size();
        if (size <= 0) {
            return false;
        }
        pw.increaseIndent();
        pw.println("Job history:");
        pw.decreaseIndent();
        long now = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        for (int i = 0; i < size; i++) {
            int index = this.mEventIndices.indexOf(i);
            int uid = this.mEventUids[index];
            if ((filterAppId == -1 || filterAppId == android.os.UserHandle.getAppId(uid)) && (cmd = this.mEventCmds[index] & 255) != 0) {
                switch (cmd) {
                    case 1:
                        label = "  START";
                        break;
                    case 2:
                        label = "   STOP";
                        break;
                    case 3:
                        label = "START-P";
                        break;
                    case 4:
                        label = " STOP-P";
                        break;
                    default:
                        label = "     ??";
                        break;
                }
                android.util.TimeUtils.formatDuration(this.mEventTimes[index] - now, pw, 19);
                pw.print(" ");
                pw.print(label);
                pw.print(": #");
                android.os.UserHandle.formatUid(pw, uid);
                pw.print(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
                pw.print(this.mEventJobIds[index]);
                pw.print(" ");
                pw.print(this.mEventTags[index]);
                if (cmd == 2 || cmd == 4) {
                    pw.print(" ");
                    java.lang.String reason = this.mEventReasons[index];
                    if (reason != null) {
                        pw.print(this.mEventReasons[index]);
                    } else {
                        pw.print(android.app.job.JobParameters.getInternalReasonCodeDescription((this.mEventCmds[index] & 65280) >> 8));
                    }
                }
                pw.println();
            }
        }
        return true;
    }

    public void dumpHistory(android.util.proto.ProtoOutputStream proto, long fieldId, int filterUid) {
        int size;
        int i = filterUid;
        int size2 = this.mEventIndices.size();
        if (size2 == 0) {
            return;
        }
        long token = proto.start(fieldId);
        long now = com.android.server.job.JobSchedulerService.sElapsedRealtimeClock.millis();
        int i2 = 0;
        while (i2 < size2) {
            int index = this.mEventIndices.indexOf(i2);
            int uid = this.mEventUids[index];
            if (i != -1 && i != android.os.UserHandle.getAppId(uid)) {
                size = size2;
            } else {
                int cmd = this.mEventCmds[index] & 255;
                if (cmd == 0) {
                    size = size2;
                } else {
                    long heToken = proto.start(2246267895809L);
                    proto.write(1159641169921L, cmd);
                    size = size2;
                    proto.write(1112396529666L, now - this.mEventTimes[index]);
                    proto.write(1120986464259L, uid);
                    proto.write(1120986464260L, this.mEventJobIds[index]);
                    proto.write(1138166333445L, this.mEventTags[index]);
                    if (cmd == 2 || cmd == 4) {
                        proto.write(1159641169926L, (this.mEventCmds[index] & 65280) >> 8);
                    }
                    proto.end(heToken);
                }
            }
            i2++;
            i = filterUid;
            size2 = size;
        }
        proto.end(token);
    }
}
