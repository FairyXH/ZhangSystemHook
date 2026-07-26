package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
class AggregatedPowerStats {
    private static final int MAX_CLOCK_UPDATES = 100;
    private static final java.lang.String TAG = "AggregatedPowerStats";
    private static final java.lang.String XML_TAG_AGGREGATED_POWER_STATS = "agg-power-stats";
    private final java.util.List<com.android.server.power.stats.AggregatedPowerStats.ClockUpdate> mClockUpdates = new java.util.ArrayList();
    private long mDurationMs;
    private final com.android.server.power.stats.PowerComponentAggregatedPowerStats[] mPowerComponentStats;

    static class ClockUpdate {
        public long currentTime;
        public long monotonicTime;

        ClockUpdate() {
        }
    }

    AggregatedPowerStats(com.android.server.power.stats.AggregatedPowerStatsConfig aggregatedPowerStatsConfig) {
        java.util.List<com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent> configs = aggregatedPowerStatsConfig.getPowerComponentsAggregatedStatsConfigs();
        this.mPowerComponentStats = new com.android.server.power.stats.PowerComponentAggregatedPowerStats[configs.size()];
        for (int i = 0; i < configs.size(); i++) {
            this.mPowerComponentStats[i] = new com.android.server.power.stats.PowerComponentAggregatedPowerStats(this, configs.get(i));
        }
    }

    void addClockUpdate(long monotonicTime, long currentTime) {
        com.android.server.power.stats.AggregatedPowerStats.ClockUpdate clockUpdate = new com.android.server.power.stats.AggregatedPowerStats.ClockUpdate();
        clockUpdate.monotonicTime = monotonicTime;
        clockUpdate.currentTime = currentTime;
        if (this.mClockUpdates.size() < 100) {
            this.mClockUpdates.add(clockUpdate);
        } else {
            android.util.Slog.i(TAG, "Too many clock updates. Replacing the previous update with " + ((java.lang.Object) android.text.format.DateFormat.format("yyyy-MM-dd-HH-mm-ss", currentTime)));
            this.mClockUpdates.set(this.mClockUpdates.size() - 1, clockUpdate);
        }
    }

    long getStartTime() {
        if (this.mClockUpdates.isEmpty()) {
            return 0L;
        }
        return this.mClockUpdates.get(0).monotonicTime;
    }

    java.util.List<com.android.server.power.stats.AggregatedPowerStats.ClockUpdate> getClockUpdates() {
        return this.mClockUpdates;
    }

    void setDuration(long durationMs) {
        this.mDurationMs = durationMs;
    }

    public long getDuration() {
        return this.mDurationMs;
    }

    com.android.server.power.stats.PowerComponentAggregatedPowerStats getPowerComponentStats(int powerComponentId) {
        for (com.android.server.power.stats.PowerComponentAggregatedPowerStats stats : this.mPowerComponentStats) {
            if (stats.powerComponentId == powerComponentId) {
                return stats;
            }
        }
        return null;
    }

    void setDeviceState(int stateId, int state, long time) {
        for (com.android.server.power.stats.PowerComponentAggregatedPowerStats stats : this.mPowerComponentStats) {
            stats.setState(stateId, state, time);
        }
    }

    void setUidState(int uid, int stateId, int state, long time) {
        for (com.android.server.power.stats.PowerComponentAggregatedPowerStats stats : this.mPowerComponentStats) {
            stats.setUidState(uid, stateId, state, time);
        }
    }

    boolean isCompatible(com.android.internal.os.PowerStats powerStats) {
        int powerComponentId = powerStats.descriptor.powerComponentId;
        for (com.android.server.power.stats.PowerComponentAggregatedPowerStats stats : this.mPowerComponentStats) {
            if (stats.powerComponentId == powerComponentId && !stats.isCompatible(powerStats)) {
                return false;
            }
        }
        return true;
    }

    void addPowerStats(com.android.internal.os.PowerStats powerStats, long time) {
        int powerComponentId = powerStats.descriptor.powerComponentId;
        for (com.android.server.power.stats.PowerComponentAggregatedPowerStats stats : this.mPowerComponentStats) {
            if (stats.powerComponentId == powerComponentId) {
                stats.getConfig().getProcessor().addPowerStats(stats, powerStats, time);
            }
        }
    }

    public void noteStateChange(android.os.BatteryStats.HistoryItem item) {
        for (com.android.server.power.stats.PowerComponentAggregatedPowerStats stats : this.mPowerComponentStats) {
            stats.getConfig().getProcessor().noteStateChange(stats, item);
        }
    }

    void reset() {
        this.mClockUpdates.clear();
        this.mDurationMs = 0L;
        for (com.android.server.power.stats.PowerComponentAggregatedPowerStats stats : this.mPowerComponentStats) {
            stats.reset();
        }
    }

    public void writeXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, XML_TAG_AGGREGATED_POWER_STATS);
        for (com.android.server.power.stats.PowerComponentAggregatedPowerStats stats : this.mPowerComponentStats) {
            stats.writeXml(serializer);
        }
        serializer.endTag((java.lang.String) null, XML_TAG_AGGREGATED_POWER_STATS);
        serializer.flush();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0041  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.android.server.power.stats.AggregatedPowerStats createFromXml(com.android.modules.utils.TypedXmlPullParser r10, com.android.server.power.stats.AggregatedPowerStatsConfig r11) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            com.android.server.power.stats.AggregatedPowerStats r0 = new com.android.server.power.stats.AggregatedPowerStats
            r0.<init>(r11)
            r1 = 0
            r2 = 0
            int r3 = r10.getEventType()
        Lb:
            r4 = 1
            if (r3 == r4) goto L6d
            r5 = 3
            java.lang.String r6 = "agg-power-stats"
            if (r3 != r5) goto L1d
            java.lang.String r5 = r10.getName()
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L6d
        L1d:
            if (r2 != 0) goto L68
            r5 = 2
            if (r3 != r5) goto L68
            java.lang.String r5 = r10.getName()
            int r7 = r5.hashCode()
            r8 = 0
            switch(r7) {
                case -925966781: goto L37;
                case 381213451: goto L2f;
                default: goto L2e;
            }
        L2e:
            goto L41
        L2f:
            boolean r4 = r5.equals(r6)
            if (r4 == 0) goto L2e
            r4 = r8
            goto L42
        L37:
            java.lang.String r6 = "power_component"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L2e
            goto L42
        L41:
            r4 = -1
        L42:
            switch(r4) {
                case 0: goto L66;
                case 1: goto L46;
                default: goto L45;
            }
        L45:
            goto L68
        L46:
            if (r1 != 0) goto L49
            goto L68
        L49:
            r4 = 0
            java.lang.String r5 = "id"
            int r4 = r10.getAttributeInt(r4, r5)
            com.android.server.power.stats.PowerComponentAggregatedPowerStats[] r5 = r0.mPowerComponentStats
            int r6 = r5.length
        L53:
            if (r8 >= r6) goto L68
            r7 = r5[r8]
            int r9 = r7.powerComponentId
            if (r9 != r4) goto L63
            boolean r5 = r7.readFromXml(r10)
            if (r5 != 0) goto L68
            r2 = 1
            goto L68
        L63:
            int r8 = r8 + 1
            goto L53
        L66:
            r1 = 1
        L68:
            int r3 = r10.next()
            goto Lb
        L6d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.AggregatedPowerStats.createFromXml(com.android.modules.utils.TypedXmlPullParser, com.android.server.power.stats.AggregatedPowerStatsConfig):com.android.server.power.stats.AggregatedPowerStats");
    }

    void dump(android.util.IndentingPrintWriter ipw) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        long baseTime = 0;
        for (int i = 0; i < this.mClockUpdates.size(); i++) {
            com.android.server.power.stats.AggregatedPowerStats.ClockUpdate clockUpdate = this.mClockUpdates.get(i);
            sb.setLength(0);
            if (i == 0) {
                baseTime = clockUpdate.monotonicTime;
                sb.append("Start time: ").append(formatDateTime(clockUpdate.currentTime)).append(" (").append(baseTime).append(") duration: ").append(this.mDurationMs);
                ipw.println(sb);
            } else {
                sb.setLength(0);
                sb.append("Clock update:  ");
                android.util.TimeUtils.formatDuration(clockUpdate.monotonicTime - baseTime, sb, 22);
                sb.append(" ").append(formatDateTime(clockUpdate.currentTime));
                ipw.increaseIndent();
                ipw.println(sb);
                ipw.decreaseIndent();
            }
        }
        ipw.println("Device");
        ipw.increaseIndent();
        for (com.android.server.power.stats.PowerComponentAggregatedPowerStats stats : this.mPowerComponentStats) {
            stats.dumpDevice(ipw);
        }
        ipw.decreaseIndent();
        java.util.Set<java.lang.Integer> uids = new java.util.HashSet<>();
        for (com.android.server.power.stats.PowerComponentAggregatedPowerStats stats2 : this.mPowerComponentStats) {
            stats2.collectUids(uids);
        }
        java.lang.Integer[] allUids = (java.lang.Integer[]) uids.toArray(new java.lang.Integer[uids.size()]);
        java.util.Arrays.sort(allUids);
        for (java.lang.Integer num : allUids) {
            int uid = num.intValue();
            ipw.println(android.os.UserHandle.formatUid(uid));
            ipw.increaseIndent();
            for (com.android.server.power.stats.PowerComponentAggregatedPowerStats stats3 : this.mPowerComponentStats) {
                stats3.dumpUid(ipw, uid);
            }
            ipw.decreaseIndent();
        }
    }

    private static java.lang.String formatDateTime(long timeInMillis) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        format.getCalendar().setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
        return format.format(new java.util.Date(timeInMillis));
    }

    public java.lang.String toString() {
        java.io.StringWriter sw = new java.io.StringWriter();
        dump(new android.util.IndentingPrintWriter(sw));
        return sw.toString();
    }
}
