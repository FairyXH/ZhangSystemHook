package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
class PowerComponentAggregatedPowerStats {
    private static final java.lang.String TAG = "PowerComponentAggregatedPowerStats";
    private static final long UNKNOWN = -1;
    static final java.lang.String XML_ATTR_ID = "id";
    private static final java.lang.String XML_ATTR_KEY = "key";
    private static final java.lang.String XML_ATTR_UID = "uid";
    private static final java.lang.String XML_TAG_DEVICE_STATS = "device-stats";
    static final java.lang.String XML_TAG_POWER_COMPONENT = "power_component";
    private static final java.lang.String XML_TAG_STATE_STATS = "state-stats";
    private static final java.lang.String XML_TAG_UID_STATS = "uid-stats";
    private final com.android.server.power.stats.AggregatedPowerStats mAggregatedPowerStats;
    private final com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent mConfig;
    private final com.android.server.power.stats.MultiStateStats.States[] mDeviceStateConfig;
    private final int[] mDeviceStates;
    private com.android.server.power.stats.MultiStateStats mDeviceStats;
    private com.android.internal.os.PowerStats.Descriptor mPowerStatsDescriptor;
    private com.android.server.power.stats.MultiStateStats.Factory mStateStatsFactory;
    private com.android.server.power.stats.MultiStateStats.Factory mStatsFactory;
    private final com.android.server.power.stats.MultiStateStats.States[] mUidStateConfig;
    private com.android.server.power.stats.MultiStateStats.Factory mUidStatsFactory;
    private long[] mZeroArray;
    public final int powerComponentId;
    private final android.util.SparseArray<com.android.server.power.stats.MultiStateStats> mStateStats = new android.util.SparseArray<>();
    private final android.util.SparseArray<com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats> mUidStats = new android.util.SparseArray<>();
    private long mPowerStatsTimestamp = -1;

    private static class UidStats {
        public int[] states;
        public com.android.server.power.stats.MultiStateStats stats;
        public boolean updated;

        private UidStats() {
        }
    }

    PowerComponentAggregatedPowerStats(com.android.server.power.stats.AggregatedPowerStats aggregatedPowerStats, com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent config) {
        this.mAggregatedPowerStats = aggregatedPowerStats;
        this.mConfig = config;
        this.powerComponentId = config.getPowerComponentId();
        this.mDeviceStateConfig = config.getDeviceStateConfig();
        this.mUidStateConfig = config.getUidStateConfig();
        this.mDeviceStates = new int[this.mDeviceStateConfig.length];
    }

    com.android.server.power.stats.AggregatedPowerStats getAggregatedPowerStats() {
        return this.mAggregatedPowerStats;
    }

    public com.android.server.power.stats.AggregatedPowerStatsConfig.PowerComponent getConfig() {
        return this.mConfig;
    }

    public com.android.internal.os.PowerStats.Descriptor getPowerStatsDescriptor() {
        return this.mPowerStatsDescriptor;
    }

    public void setPowerStatsDescriptor(com.android.internal.os.PowerStats.Descriptor powerStatsDescriptor) {
        this.mPowerStatsDescriptor = powerStatsDescriptor;
    }

    void setState(int stateId, int state, long timestampMs) {
        if (this.mDeviceStats == null) {
            createDeviceStats(timestampMs);
        }
        this.mDeviceStates[stateId] = state;
        if (this.mDeviceStateConfig[stateId].isTracked()) {
            if (this.mDeviceStats != null) {
                this.mDeviceStats.setState(stateId, state, timestampMs);
            }
            for (int i = this.mStateStats.size() - 1; i >= 0; i--) {
                com.android.server.power.stats.MultiStateStats stateStats = this.mStateStats.valueAt(i);
                stateStats.setState(stateId, state, timestampMs);
            }
        }
        int uidStateId = com.android.server.power.stats.MultiStateStats.States.findTrackedStateByName(this.mUidStateConfig, this.mDeviceStateConfig[stateId].getName());
        if (uidStateId != -1 && this.mUidStateConfig[uidStateId].isTracked()) {
            for (int i2 = this.mUidStats.size() - 1; i2 >= 0; i2--) {
                com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats uidStats = this.mUidStats.valueAt(i2);
                if (uidStats.stats == null) {
                    createUidStats(uidStats, timestampMs);
                }
                uidStats.states[uidStateId] = state;
                if (uidStats.stats != null) {
                    uidStats.stats.setState(uidStateId, state, timestampMs);
                }
            }
        }
    }

    void setUidState(int uid, int stateId, int state, long timestampMs) {
        if (!this.mUidStateConfig[stateId].isTracked()) {
            return;
        }
        com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats uidStats = getUidStats(uid);
        if (uidStats.stats == null) {
            createUidStats(uidStats, timestampMs);
        }
        uidStats.states[stateId] = state;
        if (uidStats.stats != null) {
            uidStats.stats.setState(stateId, state, timestampMs);
        }
    }

    void setDeviceStats(int[] states, long[] values) {
        if (this.mDeviceStats == null) {
            createDeviceStats(0L);
        }
        this.mDeviceStats.setStats(states, values);
    }

    void setUidStats(int uid, int[] states, long[] values) {
        com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats uidStats = getUidStats(uid);
        uidStats.stats.setStats(states, values);
    }

    boolean isCompatible(com.android.internal.os.PowerStats powerStats) {
        return this.mPowerStatsDescriptor == null || this.mPowerStatsDescriptor.equals(powerStats.descriptor);
    }

    void addPowerStats(com.android.internal.os.PowerStats powerStats, long timestampMs) {
        this.mPowerStatsDescriptor = powerStats.descriptor;
        if (this.mDeviceStats == null) {
            createDeviceStats(timestampMs);
        }
        for (int i = powerStats.stateStats.size() - 1; i >= 0; i--) {
            int key = powerStats.stateStats.keyAt(i);
            com.android.server.power.stats.MultiStateStats stateStats = this.mStateStats.get(key);
            if (stateStats == null) {
                stateStats = createStateStats(key, timestampMs);
            }
            stateStats.increment((long[]) powerStats.stateStats.valueAt(i), timestampMs);
        }
        this.mDeviceStats.increment(powerStats.stats, timestampMs);
        for (int i2 = powerStats.uidStats.size() - 1; i2 >= 0; i2--) {
            try {
                int uid = powerStats.uidStats.keyAt(i2);
                com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats uidStats = getUidStats(uid);
                if (uidStats.stats == null) {
                    createUidStats(uidStats, timestampMs);
                }
                long[] currentUidStats = (long[]) powerStats.uidStats.valueAt(i2);
                if (currentUidStats != null) {
                    uidStats.stats.increment(currentUidStats, timestampMs);
                    uidStats.updated = true;
                }
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                android.util.Slog.w(TAG, "index out of range, size=" + powerStats.uidStats.size() + ", i = " + i2, e);
            }
        }
        for (int i3 = this.mUidStats.size() - 1; i3 >= 0; i3--) {
            com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats uidStats2 = this.mUidStats.valueAt(i3);
            if (!uidStats2.updated && uidStats2.stats != null) {
                if (this.mZeroArray == null || this.mZeroArray.length != this.mPowerStatsDescriptor.uidStatsArrayLength) {
                    this.mZeroArray = new long[this.mPowerStatsDescriptor.uidStatsArrayLength];
                }
                uidStats2.stats.increment(this.mZeroArray, timestampMs);
            }
            uidStats2.updated = false;
        }
        this.mPowerStatsTimestamp = timestampMs;
    }

    void reset() {
        this.mStatsFactory = null;
        this.mUidStatsFactory = null;
        this.mDeviceStats = null;
        this.mStateStats.clear();
        for (int i = this.mUidStats.size() - 1; i >= 0; i--) {
            this.mUidStats.valueAt(i).stats = null;
        }
    }

    private com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats getUidStats(int uid) {
        int deviceStateId;
        com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats uidStats = this.mUidStats.get(uid);
        if (uidStats == null) {
            uidStats = new com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats();
            uidStats.states = new int[this.mUidStateConfig.length];
            for (int stateId = 0; stateId < this.mUidStateConfig.length; stateId++) {
                if (this.mUidStateConfig[stateId].isTracked() && (deviceStateId = com.android.server.power.stats.MultiStateStats.States.findTrackedStateByName(this.mDeviceStateConfig, this.mUidStateConfig[stateId].getName())) != -1 && this.mDeviceStateConfig[deviceStateId].isTracked()) {
                    uidStats.states[stateId] = this.mDeviceStates[deviceStateId];
                }
            }
            this.mUidStats.put(uid, uidStats);
        }
        return uidStats;
    }

    void collectUids(java.util.Collection<java.lang.Integer> uids) {
        for (int i = this.mUidStats.size() - 1; i >= 0; i--) {
            if (this.mUidStats.valueAt(i).stats != null) {
                uids.add(java.lang.Integer.valueOf(this.mUidStats.keyAt(i)));
            }
        }
    }

    boolean getDeviceStats(long[] outValues, int[] deviceStates) {
        if (deviceStates.length != this.mDeviceStateConfig.length) {
            throw new java.lang.IllegalArgumentException("Invalid number of tracked states: " + deviceStates.length + " expected: " + this.mDeviceStateConfig.length);
        }
        if (this.mDeviceStats != null) {
            this.mDeviceStats.getStats(outValues, deviceStates);
            return true;
        }
        return false;
    }

    boolean getStateStats(long[] outValues, int key, int[] deviceStates) {
        if (deviceStates.length != this.mDeviceStateConfig.length) {
            throw new java.lang.IllegalArgumentException("Invalid number of tracked states: " + deviceStates.length + " expected: " + this.mDeviceStateConfig.length);
        }
        com.android.server.power.stats.MultiStateStats stateStats = this.mStateStats.get(key);
        if (stateStats != null) {
            stateStats.getStats(outValues, deviceStates);
            return true;
        }
        return false;
    }

    void forEachStateStatsKey(java.util.function.IntConsumer consumer) {
        for (int i = this.mStateStats.size() - 1; i >= 0; i--) {
            consumer.accept(this.mStateStats.keyAt(i));
        }
    }

    boolean getUidStats(long[] outValues, int uid, int[] uidStates) {
        if (uidStates.length != this.mUidStateConfig.length) {
            throw new java.lang.IllegalArgumentException("Invalid number of tracked states: " + uidStates.length + " expected: " + this.mUidStateConfig.length);
        }
        com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats uidStats = this.mUidStats.get(uid);
        if (uidStats != null && uidStats.stats != null) {
            uidStats.stats.getStats(outValues, uidStates);
            return true;
        }
        return false;
    }

    private void createDeviceStats(long timestampMs) {
        if (this.mStatsFactory == null) {
            if (this.mPowerStatsDescriptor == null) {
                return;
            } else {
                this.mStatsFactory = new com.android.server.power.stats.MultiStateStats.Factory(this.mPowerStatsDescriptor.statsArrayLength, this.mDeviceStateConfig);
            }
        }
        this.mDeviceStats = this.mStatsFactory.create();
        if (this.mPowerStatsTimestamp != -1) {
            timestampMs = this.mPowerStatsTimestamp;
        }
        if (timestampMs != -1) {
            for (int stateId = 0; stateId < this.mDeviceStateConfig.length; stateId++) {
                int state = this.mDeviceStates[stateId];
                this.mDeviceStats.setState(stateId, state, timestampMs);
                for (int i = this.mStateStats.size() - 1; i >= 0; i--) {
                    com.android.server.power.stats.MultiStateStats stateStats = this.mStateStats.valueAt(i);
                    stateStats.setState(stateId, state, timestampMs);
                }
            }
        }
    }

    private com.android.server.power.stats.MultiStateStats createStateStats(int key, long timestampMs) {
        if (this.mStateStatsFactory == null) {
            if (this.mPowerStatsDescriptor == null) {
                return null;
            }
            this.mStateStatsFactory = new com.android.server.power.stats.MultiStateStats.Factory(this.mPowerStatsDescriptor.stateStatsArrayLength, this.mDeviceStateConfig);
        }
        com.android.server.power.stats.MultiStateStats stateStats = this.mStateStatsFactory.create();
        this.mStateStats.put(key, stateStats);
        if (this.mDeviceStats != null) {
            stateStats.copyStatesFrom(this.mDeviceStats);
        }
        return stateStats;
    }

    private void createUidStats(com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats uidStats, long timestampMs) {
        if (this.mUidStatsFactory == null) {
            if (this.mPowerStatsDescriptor == null) {
                return;
            } else {
                this.mUidStatsFactory = new com.android.server.power.stats.MultiStateStats.Factory(this.mPowerStatsDescriptor.uidStatsArrayLength, this.mUidStateConfig);
            }
        }
        uidStats.stats = this.mUidStatsFactory.create();
        if (this.mPowerStatsTimestamp != -1) {
            timestampMs = this.mPowerStatsTimestamp;
        }
        if (timestampMs != -1) {
            for (int stateId = 0; stateId < this.mUidStateConfig.length; stateId++) {
                uidStats.stats.setState(stateId, uidStats.states[stateId], timestampMs);
            }
        }
    }

    public void writeXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        if (this.mPowerStatsDescriptor == null) {
            return;
        }
        serializer.startTag((java.lang.String) null, XML_TAG_POWER_COMPONENT);
        serializer.attributeInt((java.lang.String) null, XML_ATTR_ID, this.powerComponentId);
        this.mPowerStatsDescriptor.writeXml(serializer);
        if (this.mDeviceStats != null) {
            serializer.startTag((java.lang.String) null, XML_TAG_DEVICE_STATS);
            this.mDeviceStats.writeXml(serializer);
            serializer.endTag((java.lang.String) null, XML_TAG_DEVICE_STATS);
        }
        for (int i = 0; i < this.mStateStats.size(); i++) {
            serializer.startTag((java.lang.String) null, XML_TAG_STATE_STATS);
            serializer.attributeInt((java.lang.String) null, XML_ATTR_KEY, this.mStateStats.keyAt(i));
            this.mStateStats.valueAt(i).writeXml(serializer);
            serializer.endTag((java.lang.String) null, XML_TAG_STATE_STATS);
        }
        for (int i2 = this.mUidStats.size() - 1; i2 >= 0; i2--) {
            int uid = this.mUidStats.keyAt(i2);
            com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats uidStats = this.mUidStats.valueAt(i2);
            if (uidStats.stats != null) {
                serializer.startTag((java.lang.String) null, XML_TAG_UID_STATS);
                serializer.attributeInt((java.lang.String) null, "uid", uid);
                uidStats.stats.writeXml(serializer);
                serializer.endTag((java.lang.String) null, XML_TAG_UID_STATS);
            }
        }
        serializer.endTag((java.lang.String) null, XML_TAG_POWER_COMPONENT);
        serializer.flush();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00b0, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00aa, code lost:
    
        continue;
     */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0051  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean readFromXml(com.android.modules.utils.TypedXmlPullParser r9) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instruction units count: 208
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.PowerComponentAggregatedPowerStats.readFromXml(com.android.modules.utils.TypedXmlPullParser):boolean");
    }

    void dumpDevice(android.util.IndentingPrintWriter ipw) {
        if (this.mDeviceStats != null) {
            dumpMultiStateStats(ipw, this.mDeviceStats, this.mPowerStatsDescriptor.name, null, this.mPowerStatsDescriptor.getDeviceStatsFormatter());
        }
        if (this.mStateStats.size() != 0) {
            ipw.increaseIndent();
            java.lang.String header = this.mPowerStatsDescriptor.name + " states";
            com.android.internal.os.PowerStats.PowerStatsFormatter formatter = this.mPowerStatsDescriptor.getStateStatsFormatter();
            for (int i = 0; i < this.mStateStats.size(); i++) {
                int key = this.mStateStats.keyAt(i);
                java.lang.String stateLabel = this.mPowerStatsDescriptor.getStateLabel(key);
                com.android.server.power.stats.MultiStateStats stateStats = this.mStateStats.valueAt(i);
                dumpMultiStateStats(ipw, stateStats, header, stateLabel, formatter);
            }
            ipw.decreaseIndent();
        }
    }

    void dumpUid(android.util.IndentingPrintWriter ipw, int uid) {
        com.android.server.power.stats.PowerComponentAggregatedPowerStats.UidStats uidStats = this.mUidStats.get(uid);
        if (uidStats != null && uidStats.stats != null) {
            dumpMultiStateStats(ipw, uidStats.stats, this.mPowerStatsDescriptor.name, null, this.mPowerStatsDescriptor.getUidStatsFormatter());
        }
    }

    private void dumpMultiStateStats(final android.util.IndentingPrintWriter ipw, final com.android.server.power.stats.MultiStateStats stats, final java.lang.String header, final java.lang.String additionalLabel, final com.android.internal.os.PowerStats.PowerStatsFormatter statsFormatter) {
        final boolean[] firstLine = {true};
        final long[] values = new long[stats.getDimensionCount()];
        final com.android.server.power.stats.MultiStateStats.States[] stateInfo = stats.getStates();
        com.android.server.power.stats.MultiStateStats.States.forEachTrackedStateCombination(stateInfo, new java.util.function.Consumer() { // from class: com.android.server.power.stats.PowerComponentAggregatedPowerStats$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.power.stats.PowerComponentAggregatedPowerStats.lambda$dumpMultiStateStats$0(stats, values, firstLine, ipw, header, stateInfo, additionalLabel, statsFormatter, (int[]) obj);
            }
        });
        if (!firstLine[0]) {
            ipw.decreaseIndent();
        }
    }

    static /* synthetic */ void lambda$dumpMultiStateStats$0(com.android.server.power.stats.MultiStateStats stats, long[] values, boolean[] firstLine, android.util.IndentingPrintWriter ipw, java.lang.String header, com.android.server.power.stats.MultiStateStats.States[] stateInfo, java.lang.String additionalLabel, com.android.internal.os.PowerStats.PowerStatsFormatter statsFormatter, int[] states) {
        stats.getStats(values, states);
        boolean nonZero = false;
        int length = values.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            long value = values[i];
            if (value == 0) {
                i++;
            } else {
                nonZero = true;
                break;
            }
        }
        if (!nonZero) {
            return;
        }
        if (firstLine[0]) {
            ipw.println(header);
            ipw.increaseIndent();
        }
        firstLine[0] = false;
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("(");
        boolean first = true;
        for (int i2 = 0; i2 < states.length; i2++) {
            if (stateInfo[i2].isTracked()) {
                if (!first) {
                    sb.append(" ");
                }
                first = false;
                sb.append(stateInfo[i2].getLabels()[states[i2]]);
            }
        }
        if (additionalLabel != null) {
            sb.append(" ").append(additionalLabel);
        }
        sb.append(") ").append(statsFormatter.format(values));
        ipw.println(sb);
    }

    public java.lang.String toString() {
        java.io.StringWriter sw = new java.io.StringWriter();
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(sw);
        ipw.increaseIndent();
        dumpDevice(ipw);
        ipw.decreaseIndent();
        int[] uids = new int[this.mUidStats.size()];
        for (int i = uids.length - 1; i >= 0; i--) {
            uids[i] = this.mUidStats.keyAt(i);
        }
        java.util.Arrays.sort(uids);
        for (int uid : uids) {
            ipw.println(android.os.UserHandle.formatUid(uid));
            ipw.increaseIndent();
            dumpUid(ipw, uid);
            ipw.decreaseIndent();
        }
        ipw.flush();
        return sw.toString();
    }
}
