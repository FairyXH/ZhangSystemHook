package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
final class DiscreteRegistry {
    private static final java.lang.String ATTR_ATTRIBUTION_FLAGS = "af";
    private static final java.lang.String ATTR_CHAIN_ID = "ci";
    private static final java.lang.String ATTR_FLAGS = "f";
    private static final java.lang.String ATTR_LARGEST_CHAIN_ID = "lc";
    private static final java.lang.String ATTR_NOTE_DURATION = "nd";
    private static final java.lang.String ATTR_NOTE_TIME = "nt";
    private static final java.lang.String ATTR_OP_ID = "op";
    private static final java.lang.String ATTR_PACKAGE_NAME = "pn";
    private static final java.lang.String ATTR_TAG = "at";
    private static final java.lang.String ATTR_UID = "ui";
    private static final java.lang.String ATTR_UID_STATE = "us";
    private static final java.lang.String ATTR_VERSION = "v";
    private static final int CURRENT_VERSION = 1;
    private static final java.lang.String DEFAULT_DISCRETE_OPS = "1,0,26,27,100,101,120,136,141";
    static final java.lang.String DISCRETE_HISTORY_FILE_SUFFIX = "tl";
    private static final int OP_FLAGS_DISCRETE = 11;
    private static final java.lang.String PROPERTY_DISCRETE_FLAGS = "discrete_history_op_flags";
    private static final java.lang.String PROPERTY_DISCRETE_HISTORY_CUTOFF = "discrete_history_cutoff_millis";
    private static final java.lang.String PROPERTY_DISCRETE_HISTORY_QUANTIZATION = "discrete_history_quantization_millis";
    private static final java.lang.String PROPERTY_DISCRETE_OPS_LIST = "discrete_history_ops_cslist";
    private static final java.lang.String TAG_ENTRY = "e";
    private static final java.lang.String TAG_HISTORY = "h";
    private static final java.lang.String TAG_OP = "o";
    private static final java.lang.String TAG_PACKAGE = "p";
    private static final java.lang.String TAG_TAG = "a";
    private static final java.lang.String TAG_UID = "u";
    private static int sDiscreteFlags;
    private static long sDiscreteHistoryCutoff;
    private static long sDiscreteHistoryQuantization;
    private static int[] sDiscreteOps;
    private java.io.File mDiscreteAccessDir;
    private com.android.server.appop.DiscreteRegistry.DiscreteOps mDiscreteOps;
    private final java.lang.Object mInMemoryLock;
    private static final java.lang.String TAG = com.android.server.appop.DiscreteRegistry.class.getSimpleName();
    private static final long DEFAULT_DISCRETE_HISTORY_CUTOFF = java.time.Duration.ofDays(7).toMillis();
    private static final long MAXIMUM_DISCRETE_HISTORY_CUTOFF = java.time.Duration.ofDays(30).toMillis();
    private static final long DEFAULT_DISCRETE_HISTORY_QUANTIZATION = java.time.Duration.ofMinutes(1).toMillis();
    private final java.lang.Object mOnDiskLock = new java.lang.Object();
    private com.android.server.appop.DiscreteRegistry.DiscreteOps mCachedOps = null;
    private boolean mDebugMode = false;

    DiscreteRegistry(java.lang.Object inMemoryLock) {
        this.mInMemoryLock = inMemoryLock;
        synchronized (this.mOnDiskLock) {
            this.mDiscreteAccessDir = new java.io.File(new java.io.File(android.os.Environment.getDataSystemDirectory(), "appops"), "discrete");
            createDiscreteAccessDirLocked();
            int largestChainId = readLargestChainIdFromDiskLocked();
            synchronized (this.mInMemoryLock) {
                this.mDiscreteOps = new com.android.server.appop.DiscreteRegistry.DiscreteOps(largestChainId);
            }
        }
    }

    void systemReady() {
        android.provider.DeviceConfig.addOnPropertiesChangedListener("privacy", android.os.AsyncTask.THREAD_POOL_EXECUTOR, new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.appop.DiscreteRegistry$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$systemReady$0(properties);
            }
        });
        lambda$systemReady$0(android.provider.DeviceConfig.getProperties("privacy", new java.lang.String[0]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: setDiscreteHistoryParameters, reason: merged with bridge method [inline-methods] */
    public void lambda$systemReady$0(android.provider.DeviceConfig.Properties p) {
        int[] opsList;
        if (p.getKeyset().contains(PROPERTY_DISCRETE_HISTORY_CUTOFF)) {
            sDiscreteHistoryCutoff = p.getLong(PROPERTY_DISCRETE_HISTORY_CUTOFF, DEFAULT_DISCRETE_HISTORY_CUTOFF);
            if (!android.os.Build.IS_DEBUGGABLE && !this.mDebugMode) {
                sDiscreteHistoryCutoff = java.lang.Long.min(MAXIMUM_DISCRETE_HISTORY_CUTOFF, sDiscreteHistoryCutoff);
            }
        } else {
            sDiscreteHistoryCutoff = DEFAULT_DISCRETE_HISTORY_CUTOFF;
        }
        if (p.getKeyset().contains(PROPERTY_DISCRETE_HISTORY_QUANTIZATION)) {
            sDiscreteHistoryQuantization = p.getLong(PROPERTY_DISCRETE_HISTORY_QUANTIZATION, DEFAULT_DISCRETE_HISTORY_QUANTIZATION);
            if (!android.os.Build.IS_DEBUGGABLE && !this.mDebugMode) {
                sDiscreteHistoryQuantization = java.lang.Math.max(DEFAULT_DISCRETE_HISTORY_QUANTIZATION, sDiscreteHistoryQuantization);
            }
        } else {
            sDiscreteHistoryQuantization = DEFAULT_DISCRETE_HISTORY_QUANTIZATION;
        }
        int i = 11;
        if (p.getKeyset().contains(PROPERTY_DISCRETE_FLAGS)) {
            i = p.getInt(PROPERTY_DISCRETE_FLAGS, 11);
            sDiscreteFlags = i;
        }
        sDiscreteFlags = i;
        if (p.getKeyset().contains(PROPERTY_DISCRETE_OPS_LIST)) {
            opsList = parseOpsList(p.getString(PROPERTY_DISCRETE_OPS_LIST, DEFAULT_DISCRETE_OPS));
        } else {
            opsList = parseOpsList(DEFAULT_DISCRETE_OPS);
        }
        sDiscreteOps = opsList;
    }

    void recordDiscreteAccess(int uid, java.lang.String packageName, int op, java.lang.String attributionTag, int flags, int uidState, long accessTime, long accessDuration, int attributionFlags, int attributionChainId) throws java.lang.Throwable {
        if (!isDiscreteOp(op, flags)) {
            return;
        }
        synchronized (this.mInMemoryLock) {
            try {
                try {
                    this.mDiscreteOps.addDiscreteAccess(op, uid, packageName, attributionTag, flags, uidState, accessTime, accessDuration, attributionFlags, attributionChainId);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    void writeAndClearAccessHistory() {
        com.android.server.appop.DiscreteRegistry.DiscreteOps discreteOps;
        synchronized (this.mOnDiskLock) {
            if (this.mDiscreteAccessDir == null) {
                android.util.Slog.d(TAG, "State not saved - persistence not initialized.");
                return;
            }
            synchronized (this.mInMemoryLock) {
                discreteOps = this.mDiscreteOps;
                this.mDiscreteOps = new com.android.server.appop.DiscreteRegistry.DiscreteOps(discreteOps.mChainIdOffset);
                this.mCachedOps = null;
            }
            deleteOldDiscreteHistoryFilesLocked();
            if (!discreteOps.isEmpty()) {
                persistDiscreteOpsLocked(discreteOps);
            }
        }
    }

    void addFilteredDiscreteOpsToHistoricalOps(android.app.AppOpsManager.HistoricalOps result, long beginTimeMillis, long endTimeMillis, int filter, int uidFilter, java.lang.String packageNameFilter, java.lang.String[] opNamesFilter, java.lang.String attributionTagFilter, int flagsFilter, java.util.Set<java.lang.String> attributionExemptPkgs) {
        android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains;
        boolean assembleChains = attributionExemptPkgs != null;
        com.android.server.appop.DiscreteRegistry.DiscreteOps discreteOps = getAllDiscreteOps();
        android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains2 = new android.util.ArrayMap<>();
        if (assembleChains) {
            android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains3 = createAttributionChains(discreteOps, attributionExemptPkgs);
            attributionChains = attributionChains3;
        } else {
            attributionChains = attributionChains2;
        }
        long beginTimeMillis2 = java.lang.Math.max(beginTimeMillis, java.time.Instant.now().minus(sDiscreteHistoryCutoff, (java.time.temporal.TemporalUnit) java.time.temporal.ChronoUnit.MILLIS).toEpochMilli());
        android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains4 = attributionChains;
        discreteOps.filter(beginTimeMillis2, endTimeMillis, filter, uidFilter, packageNameFilter, opNamesFilter, attributionTagFilter, flagsFilter, attributionChains4);
        discreteOps.applyToHistoricalOps(result, attributionChains4);
    }

    private int readLargestChainIdFromDiskLocked() {
        java.io.File[] files = this.mDiscreteAccessDir.listFiles();
        if (files == null || files.length <= 0) {
            return 0;
        }
        java.io.File latestFile = null;
        long latestFileTimestamp = 0;
        for (java.io.File f : files) {
            java.lang.String fileName = f.getName();
            if (fileName.endsWith(DISCRETE_HISTORY_FILE_SUFFIX)) {
                long timestamp = java.lang.Long.valueOf(fileName.substring(0, fileName.length() - DISCRETE_HISTORY_FILE_SUFFIX.length())).longValue();
                if (latestFileTimestamp < timestamp) {
                    latestFile = f;
                    latestFileTimestamp = timestamp;
                }
            }
        }
        if (latestFile == null) {
            return 0;
        }
        try {
            java.io.FileInputStream stream = new java.io.FileInputStream(latestFile);
            try {
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(stream);
                com.android.internal.util.XmlUtils.beginDocument(parser, TAG_HISTORY);
                int largestChainId = parser.getAttributeInt((java.lang.String) null, ATTR_LARGEST_CHAIN_ID, 0);
                try {
                    stream.close();
                } catch (java.io.IOException e) {
                }
                return largestChainId;
            } catch (java.lang.Throwable th) {
                try {
                    stream.close();
                } catch (java.io.IOException e2) {
                }
                return 0;
            }
        } catch (java.io.FileNotFoundException e3) {
            return 0;
        }
    }

    private android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> createAttributionChains(com.android.server.appop.DiscreteRegistry.DiscreteOps discreteOps, java.util.Set<java.lang.String> attributionExemptPkgs) {
        android.util.ArrayMap<java.lang.String, com.android.server.appop.DiscreteRegistry.DiscretePackageOps> pkgs;
        java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> opEvents;
        int attrOpNum;
        int nAttrOps;
        android.util.ArrayMap<java.lang.String, java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent>> attrOps;
        int opNum;
        int nOps;
        int nPackages;
        com.android.server.appop.DiscreteRegistry.DiscreteOps discreteOps2 = discreteOps;
        android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> chains = new android.util.ArrayMap<>();
        int nUids = discreteOps2.mUids.size();
        int uidNum = 0;
        while (uidNum < nUids) {
            android.util.ArrayMap<java.lang.String, com.android.server.appop.DiscreteRegistry.DiscretePackageOps> pkgs2 = discreteOps2.mUids.valueAt(uidNum).mPackages;
            int uid = discreteOps2.mUids.keyAt(uidNum).intValue();
            int nPackages2 = pkgs2.size();
            int pkgNum = 0;
            while (pkgNum < nPackages2) {
                android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.DiscreteOp> ops = pkgs2.valueAt(pkgNum).mPackageOps;
                java.lang.String pkg = pkgs2.keyAt(pkgNum);
                int nOps2 = ops.size();
                int opNum2 = 0;
                while (opNum2 < nOps2) {
                    android.util.ArrayMap<java.lang.String, java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent>> attrOps2 = ops.valueAt(opNum2).mAttributedOps;
                    int op = ops.keyAt(opNum2).intValue();
                    int nAttrOps2 = attrOps2.size();
                    int attrOpNum2 = 0;
                    while (attrOpNum2 < nAttrOps2) {
                        java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> opEvents2 = attrOps2.valueAt(attrOpNum2);
                        java.lang.String attributionTag = attrOps2.keyAt(attrOpNum2);
                        int nOpEvents = opEvents2.size();
                        int nUids2 = nUids;
                        int nUids3 = 0;
                        while (nUids3 < nOpEvents) {
                            int nOpEvents2 = nOpEvents;
                            com.android.server.appop.DiscreteRegistry.DiscreteOpEvent event = opEvents2.get(nUids3);
                            if (event != null) {
                                pkgs = pkgs2;
                                java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> opEvents3 = opEvents2;
                                if (event.mAttributionChainId == -1) {
                                    nAttrOps = nAttrOps2;
                                    attrOps = attrOps2;
                                    opNum = opNum2;
                                    nOps = nOps2;
                                    nPackages = nPackages2;
                                    opEvents = opEvents3;
                                    attrOpNum = attrOpNum2;
                                } else if ((event.mAttributionFlags & 8) == 0) {
                                    nAttrOps = nAttrOps2;
                                    attrOps = attrOps2;
                                    opNum = opNum2;
                                    nOps = nOps2;
                                    nPackages = nPackages2;
                                    opEvents = opEvents3;
                                    attrOpNum = attrOpNum2;
                                } else {
                                    if (chains.containsKey(java.lang.Integer.valueOf(event.mAttributionChainId))) {
                                        nPackages = nPackages2;
                                    } else {
                                        nPackages = nPackages2;
                                        chains.put(java.lang.Integer.valueOf(event.mAttributionChainId), new com.android.server.appop.DiscreteRegistry.AttributionChain(attributionExemptPkgs));
                                    }
                                    opEvents = opEvents3;
                                    attrOpNum = attrOpNum2;
                                    nAttrOps = nAttrOps2;
                                    attrOps = attrOps2;
                                    opNum = opNum2;
                                    nOps = nOps2;
                                    chains.get(java.lang.Integer.valueOf(event.mAttributionChainId)).addEvent(pkg, uid, attributionTag, op, event);
                                }
                            } else {
                                pkgs = pkgs2;
                                opEvents = opEvents2;
                                attrOpNum = attrOpNum2;
                                nAttrOps = nAttrOps2;
                                attrOps = attrOps2;
                                opNum = opNum2;
                                nOps = nOps2;
                                nPackages = nPackages2;
                            }
                            nUids3++;
                            opEvents2 = opEvents;
                            pkgs2 = pkgs;
                            nOpEvents = nOpEvents2;
                            attrOpNum2 = attrOpNum;
                            nPackages2 = nPackages;
                            nAttrOps2 = nAttrOps;
                            attrOps2 = attrOps;
                            opNum2 = opNum;
                            nOps2 = nOps;
                        }
                        attrOpNum2++;
                        pkgs2 = pkgs2;
                        nUids = nUids2;
                        nPackages2 = nPackages2;
                    }
                    opNum2++;
                    nPackages2 = nPackages2;
                }
                pkgNum++;
                nPackages2 = nPackages2;
            }
            uidNum++;
            discreteOps2 = discreteOps;
        }
        return chains;
    }

    private void readDiscreteOpsFromDisk(com.android.server.appop.DiscreteRegistry.DiscreteOps discreteOps) {
        synchronized (this.mOnDiskLock) {
            long beginTimeMillis = java.time.Instant.now().minus(sDiscreteHistoryCutoff, (java.time.temporal.TemporalUnit) java.time.temporal.ChronoUnit.MILLIS).toEpochMilli();
            java.io.File[] files = this.mDiscreteAccessDir.listFiles();
            if (files != null && files.length > 0) {
                for (java.io.File f : files) {
                    java.lang.String fileName = f.getName();
                    if (fileName.endsWith(DISCRETE_HISTORY_FILE_SUFFIX)) {
                        long timestamp = java.lang.Long.valueOf(fileName.substring(0, fileName.length() - DISCRETE_HISTORY_FILE_SUFFIX.length())).longValue();
                        if (timestamp >= beginTimeMillis) {
                            discreteOps.readFromFile(f, beginTimeMillis);
                        }
                    }
                }
            }
        }
    }

    void clearHistory() {
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                this.mDiscreteOps = new com.android.server.appop.DiscreteRegistry.DiscreteOps(0);
            }
            clearOnDiskHistoryLocked();
        }
    }

    void clearHistory(int uid, java.lang.String packageName) {
        synchronized (this.mOnDiskLock) {
            com.android.server.appop.DiscreteRegistry.DiscreteOps discreteOps = getAllDiscreteOps();
            clearHistory();
            discreteOps.clearHistory(uid, packageName);
            persistDiscreteOpsLocked(discreteOps);
        }
    }

    void offsetHistory(long offset) {
        com.android.server.appop.DiscreteRegistry.DiscreteOps discreteOps;
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                discreteOps = getAllDiscreteOps();
                clearHistory();
            }
            discreteOps.offsetHistory(offset);
            persistDiscreteOpsLocked(discreteOps);
        }
    }

    void dump(java.io.PrintWriter pw, int uidFilter, java.lang.String packageNameFilter, java.lang.String attributionTagFilter, int filter, int dumpOp, java.text.SimpleDateFormat sdf, java.util.Date date, java.lang.String prefix, int nDiscreteOps) {
        com.android.server.appop.DiscreteRegistry.DiscreteOps discreteOps = getAllDiscreteOps();
        java.lang.String[] opNamesFilter = dumpOp == -1 ? null : new java.lang.String[]{android.app.AppOpsManager.opToPublicName(dumpOp)};
        discreteOps.filter(0L, java.time.Instant.now().toEpochMilli(), filter, uidFilter, packageNameFilter, opNamesFilter, attributionTagFilter, 31, new android.util.ArrayMap());
        pw.print(prefix);
        pw.print("Largest chain id: ");
        pw.print(this.mDiscreteOps.mLargestChainId);
        pw.println();
        discreteOps.dump(pw, sdf, date, prefix, nDiscreteOps);
    }

    private void clearOnDiskHistoryLocked() {
        this.mCachedOps = null;
        android.os.FileUtils.deleteContentsAndDir(this.mDiscreteAccessDir);
        createDiscreteAccessDir();
    }

    private com.android.server.appop.DiscreteRegistry.DiscreteOps getAllDiscreteOps() {
        com.android.server.appop.DiscreteRegistry.DiscreteOps discreteOps = new com.android.server.appop.DiscreteRegistry.DiscreteOps(0);
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                discreteOps.merge(this.mDiscreteOps);
            }
            if (this.mCachedOps == null) {
                this.mCachedOps = new com.android.server.appop.DiscreteRegistry.DiscreteOps(0);
                readDiscreteOpsFromDisk(this.mCachedOps);
            }
            discreteOps.merge(this.mCachedOps);
        }
        return discreteOps;
    }

    private static final class AttributionChain {
        java.util.Set<java.lang.String> mExemptPkgs;
        java.util.ArrayList<com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent> mChain = new java.util.ArrayList<>();
        com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent mStartEvent = null;
        com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent mLastVisibleEvent = null;

        private static final class OpEvent {
            java.lang.String mAttributionTag;
            int mOpCode;
            com.android.server.appop.DiscreteRegistry.DiscreteOpEvent mOpEvent;
            java.lang.String mPkgName;
            int mUid;

            OpEvent(java.lang.String pkgName, int uid, java.lang.String attributionTag, int opCode, com.android.server.appop.DiscreteRegistry.DiscreteOpEvent event) {
                this.mPkgName = pkgName;
                this.mUid = uid;
                this.mAttributionTag = attributionTag;
                this.mOpCode = opCode;
                this.mOpEvent = event;
            }

            public boolean matches(java.lang.String pkgName, int uid, java.lang.String attributionTag, int opCode, com.android.server.appop.DiscreteRegistry.DiscreteOpEvent event) {
                return java.util.Objects.equals(pkgName, this.mPkgName) && this.mUid == uid && java.util.Objects.equals(attributionTag, this.mAttributionTag) && this.mOpCode == opCode && this.mOpEvent.mAttributionChainId == event.mAttributionChainId && this.mOpEvent.mAttributionFlags == event.mAttributionFlags && this.mOpEvent.mNoteTime == event.mNoteTime;
            }

            public boolean packageOpEquals(com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent other) {
                return java.util.Objects.equals(other.mPkgName, this.mPkgName) && other.mUid == this.mUid && java.util.Objects.equals(other.mAttributionTag, this.mAttributionTag) && this.mOpCode == other.mOpCode;
            }

            public boolean equalsExceptDuration(com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent other) {
                return other.mOpEvent.mNoteDuration != this.mOpEvent.mNoteDuration && packageOpEquals(other) && this.mOpEvent.equalsExceptDuration(other.mOpEvent);
            }
        }

        AttributionChain(java.util.Set<java.lang.String> exemptPkgs) {
            this.mExemptPkgs = exemptPkgs;
        }

        boolean isComplete() {
            return (this.mChain.isEmpty() || getStart() == null || !isEnd(this.mChain.get(this.mChain.size() - 1))) ? false : true;
        }

        boolean isStart(java.lang.String pkgName, int uid, java.lang.String attributionTag, int op, com.android.server.appop.DiscreteRegistry.DiscreteOpEvent opEvent) {
            if (this.mStartEvent == null || opEvent == null) {
                return false;
            }
            return this.mStartEvent.matches(pkgName, uid, attributionTag, op, opEvent);
        }

        private com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent getStart() {
            if (this.mChain.isEmpty() || !isStart(this.mChain.get(0))) {
                return null;
            }
            return this.mChain.get(0);
        }

        private com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent getLastVisible() {
            for (int i = this.mChain.size() - 1; i > 0; i--) {
                com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent event = this.mChain.get(i);
                if (!this.mExemptPkgs.contains(event.mPkgName)) {
                    return event;
                }
            }
            return null;
        }

        void addEvent(java.lang.String pkgName, int uid, java.lang.String attributionTag, int op, com.android.server.appop.DiscreteRegistry.DiscreteOpEvent opEvent) {
            com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent event = new com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent(pkgName, uid, attributionTag, op, opEvent);
            for (int i = 0; i < this.mChain.size(); i++) {
                com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent item = this.mChain.get(i);
                if (item.equalsExceptDuration(event)) {
                    if (event.mOpEvent.mNoteDuration != -1) {
                        item.mOpEvent = event.mOpEvent;
                        return;
                    }
                    return;
                }
            }
            if (this.mChain.isEmpty() || isEnd(event)) {
                this.mChain.add(event);
            } else if (isStart(event)) {
                this.mChain.add(0, event);
            } else {
                int i2 = 0;
                while (true) {
                    if (i2 >= this.mChain.size()) {
                        break;
                    }
                    com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent currEvent = this.mChain.get(i2);
                    if ((!isStart(currEvent) && currEvent.mOpEvent.mNoteTime > event.mOpEvent.mNoteTime) || (i2 == this.mChain.size() - 1 && isEnd(currEvent))) {
                        break;
                    }
                    if (i2 != this.mChain.size() - 1) {
                        i2++;
                    } else {
                        this.mChain.add(event);
                        break;
                    }
                }
                this.mChain.add(i2, event);
            }
            this.mStartEvent = isComplete() ? getStart() : null;
            this.mLastVisibleEvent = isComplete() ? getLastVisible() : null;
        }

        private boolean isEnd(com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent event) {
            return (event == null || (event.mOpEvent.mAttributionFlags & 1) == 0) ? false : true;
        }

        private boolean isStart(com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent event) {
            return (event == null || (event.mOpEvent.mAttributionFlags & 4) == 0) ? false : true;
        }
    }

    private final class DiscreteOps {
        int mChainIdOffset;
        int mLargestChainId;
        android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.DiscreteUidOps> mUids = new android.util.ArrayMap<>();

        DiscreteOps(int chainIdOffset) {
            this.mChainIdOffset = chainIdOffset;
            this.mLargestChainId = chainIdOffset;
        }

        boolean isEmpty() {
            return this.mUids.isEmpty();
        }

        void merge(com.android.server.appop.DiscreteRegistry.DiscreteOps other) {
            this.mLargestChainId = java.lang.Math.max(this.mLargestChainId, other.mLargestChainId);
            int nUids = other.mUids.size();
            for (int i = 0; i < nUids; i++) {
                int uid = other.mUids.keyAt(i).intValue();
                com.android.server.appop.DiscreteRegistry.DiscreteUidOps uidOps = other.mUids.valueAt(i);
                getOrCreateDiscreteUidOps(uid).merge(uidOps);
            }
        }

        void addDiscreteAccess(int op, int uid, java.lang.String packageName, java.lang.String attributionTag, int flags, int uidState, long accessTime, long accessDuration, int attributionFlags, int attributionChainId) {
            int offsetChainId = attributionChainId;
            if (attributionChainId != -1) {
                offsetChainId = attributionChainId + this.mChainIdOffset;
                if (offsetChainId > this.mLargestChainId) {
                    this.mLargestChainId = offsetChainId;
                } else if (offsetChainId < 0) {
                    offsetChainId = 0;
                    this.mLargestChainId = 0;
                    this.mChainIdOffset = attributionChainId * (-1);
                }
            }
            getOrCreateDiscreteUidOps(uid).addDiscreteAccess(op, packageName, attributionTag, flags, uidState, accessTime, accessDuration, attributionFlags, offsetChainId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void filter(long beginTimeMillis, long endTimeMillis, int filter, int uidFilter, java.lang.String packageNameFilter, java.lang.String[] opNamesFilter, java.lang.String attributionTagFilter, int flagsFilter, android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains) {
            if ((filter & 1) != 0) {
                android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.DiscreteUidOps> uids = new android.util.ArrayMap<>();
                uids.put(java.lang.Integer.valueOf(uidFilter), getOrCreateDiscreteUidOps(uidFilter));
                this.mUids = uids;
            }
            int nUids = this.mUids.size();
            for (int i = nUids - 1; i >= 0; i--) {
                this.mUids.valueAt(i).filter(beginTimeMillis, endTimeMillis, filter, packageNameFilter, opNamesFilter, attributionTagFilter, flagsFilter, this.mUids.keyAt(i).intValue(), attributionChains);
                if (this.mUids.valueAt(i).isEmpty()) {
                    this.mUids.removeAt(i);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void offsetHistory(long offset) {
            int nUids = this.mUids.size();
            for (int i = 0; i < nUids; i++) {
                this.mUids.valueAt(i).offsetHistory(offset);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearHistory(int uid, java.lang.String packageName) {
            if (this.mUids.containsKey(java.lang.Integer.valueOf(uid))) {
                this.mUids.get(java.lang.Integer.valueOf(uid)).clearPackage(packageName);
                if (this.mUids.get(java.lang.Integer.valueOf(uid)).isEmpty()) {
                    this.mUids.remove(java.lang.Integer.valueOf(uid));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void applyToHistoricalOps(android.app.AppOpsManager.HistoricalOps result, android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains) {
            int nUids = this.mUids.size();
            for (int i = 0; i < nUids; i++) {
                this.mUids.valueAt(i).applyToHistory(result, this.mUids.keyAt(i).intValue(), attributionChains);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeToStream(java.io.FileOutputStream stream) throws java.lang.Exception {
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(stream);
            out.startDocument((java.lang.String) null, true);
            out.startTag((java.lang.String) null, com.android.server.appop.DiscreteRegistry.TAG_HISTORY);
            out.attributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_VERSION, 1);
            out.attributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_LARGEST_CHAIN_ID, this.mLargestChainId);
            int nUids = this.mUids.size();
            for (int i = 0; i < nUids; i++) {
                out.startTag((java.lang.String) null, com.android.server.appop.DiscreteRegistry.TAG_UID);
                out.attributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_UID, this.mUids.keyAt(i).intValue());
                this.mUids.valueAt(i).serialize(out);
                out.endTag((java.lang.String) null, com.android.server.appop.DiscreteRegistry.TAG_UID);
            }
            out.endTag((java.lang.String) null, com.android.server.appop.DiscreteRegistry.TAG_HISTORY);
            out.endDocument();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.io.PrintWriter pw, java.text.SimpleDateFormat sdf, java.util.Date date, java.lang.String prefix, int nDiscreteOps) {
            int nUids = this.mUids.size();
            for (int i = 0; i < nUids; i++) {
                pw.print(prefix);
                pw.print("Uid: ");
                pw.print(this.mUids.keyAt(i));
                pw.println();
                this.mUids.valueAt(i).dump(pw, sdf, date, prefix + "  ", nDiscreteOps);
            }
        }

        private com.android.server.appop.DiscreteRegistry.DiscreteUidOps getOrCreateDiscreteUidOps(int uid) {
            com.android.server.appop.DiscreteRegistry.DiscreteUidOps result = this.mUids.get(java.lang.Integer.valueOf(uid));
            if (result == null) {
                com.android.server.appop.DiscreteRegistry.DiscreteUidOps result2 = com.android.server.appop.DiscreteRegistry.this.new DiscreteUidOps();
                this.mUids.put(java.lang.Integer.valueOf(uid), result2);
                return result2;
            }
            return result;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void readFromFile(java.io.File f, long beginTimeMillis) {
            try {
                java.io.FileInputStream stream = new java.io.FileInputStream(f);
                try {
                    try {
                        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(stream);
                        com.android.internal.util.XmlUtils.beginDocument(parser, com.android.server.appop.DiscreteRegistry.TAG_HISTORY);
                        int version = parser.getAttributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_VERSION);
                        if (version != 1) {
                            throw new java.lang.IllegalStateException("Dropping unsupported discrete history " + f);
                        }
                        int depth = parser.getDepth();
                        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
                            if (com.android.server.appop.DiscreteRegistry.TAG_UID.equals(parser.getName())) {
                                int uid = parser.getAttributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_UID, -1);
                                getOrCreateDiscreteUidOps(uid).deserialize(parser, beginTimeMillis);
                            }
                        }
                        stream.close();
                    } catch (java.lang.Throwable t) {
                        try {
                            android.util.Slog.e(com.android.server.appop.DiscreteRegistry.TAG, "Failed to read file " + f.getName() + " " + t.getMessage() + " " + java.util.Arrays.toString(t.getStackTrace()));
                            stream.close();
                        } catch (java.lang.Throwable th) {
                            try {
                                stream.close();
                            } catch (java.io.IOException e) {
                            }
                            throw th;
                        }
                    }
                } catch (java.io.IOException e2) {
                }
            } catch (java.io.FileNotFoundException e3) {
            }
        }
    }

    private void createDiscreteAccessDir() {
        if (!this.mDiscreteAccessDir.exists()) {
            if (!this.mDiscreteAccessDir.mkdirs()) {
                android.util.Slog.e(TAG, "Failed to create DiscreteRegistry directory");
            }
            android.os.FileUtils.setPermissions(this.mDiscreteAccessDir.getPath(), 505, -1, -1);
        }
    }

    private void persistDiscreteOpsLocked(com.android.server.appop.DiscreteRegistry.DiscreteOps discreteOps) {
        long currentTimeStamp = java.time.Instant.now().toEpochMilli();
        android.util.AtomicFile file = new android.util.AtomicFile(new java.io.File(this.mDiscreteAccessDir, currentTimeStamp + DISCRETE_HISTORY_FILE_SUFFIX));
        java.io.FileOutputStream stream = null;
        try {
            stream = file.startWrite();
            discreteOps.writeToStream(stream);
            file.finishWrite(stream);
        } catch (java.lang.Throwable t) {
            android.util.Slog.e(TAG, "Error writing timeline state: " + t.getMessage() + " " + java.util.Arrays.toString(t.getStackTrace()));
            if (stream != null) {
                file.failWrite(stream);
            }
        }
    }

    private void deleteOldDiscreteHistoryFilesLocked() {
        java.io.File[] files = this.mDiscreteAccessDir.listFiles();
        if (files != null && files.length > 0) {
            for (java.io.File f : files) {
                java.lang.String fileName = f.getName();
                if (fileName.endsWith(DISCRETE_HISTORY_FILE_SUFFIX)) {
                    try {
                        long timestamp = java.lang.Long.valueOf(fileName.substring(0, fileName.length() - DISCRETE_HISTORY_FILE_SUFFIX.length())).longValue();
                        if (java.time.Instant.now().minus(sDiscreteHistoryCutoff, (java.time.temporal.TemporalUnit) java.time.temporal.ChronoUnit.MILLIS).toEpochMilli() > timestamp) {
                            f.delete();
                            android.util.Slog.e(TAG, "Deleting file " + fileName);
                        }
                    } catch (java.lang.Throwable t) {
                        android.util.Slog.e(TAG, "Error while cleaning timeline files: ", t);
                    }
                }
            }
        }
    }

    private void createDiscreteAccessDirLocked() {
        if (!this.mDiscreteAccessDir.exists()) {
            if (!this.mDiscreteAccessDir.mkdirs()) {
                android.util.Slog.e(TAG, "Failed to create DiscreteRegistry directory");
            }
            android.os.FileUtils.setPermissions(this.mDiscreteAccessDir.getPath(), 505, -1, -1);
        }
    }

    private final class DiscreteUidOps {
        android.util.ArrayMap<java.lang.String, com.android.server.appop.DiscreteRegistry.DiscretePackageOps> mPackages = new android.util.ArrayMap<>();

        DiscreteUidOps() {
        }

        boolean isEmpty() {
            return this.mPackages.isEmpty();
        }

        void merge(com.android.server.appop.DiscreteRegistry.DiscreteUidOps other) {
            int nPackages = other.mPackages.size();
            for (int i = 0; i < nPackages; i++) {
                java.lang.String packageName = other.mPackages.keyAt(i);
                com.android.server.appop.DiscreteRegistry.DiscretePackageOps p = other.mPackages.valueAt(i);
                getOrCreateDiscretePackageOps(packageName).merge(p);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void filter(long beginTimeMillis, long endTimeMillis, int filter, java.lang.String packageNameFilter, java.lang.String[] opNamesFilter, java.lang.String attributionTagFilter, int flagsFilter, int currentUid, android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains) {
            if ((filter & 2) != 0) {
                android.util.ArrayMap<java.lang.String, com.android.server.appop.DiscreteRegistry.DiscretePackageOps> packages = new android.util.ArrayMap<>();
                packages.put(packageNameFilter, getOrCreateDiscretePackageOps(packageNameFilter));
                this.mPackages = packages;
            }
            int nPackages = this.mPackages.size();
            for (int i = nPackages - 1; i >= 0; i--) {
                this.mPackages.valueAt(i).filter(beginTimeMillis, endTimeMillis, filter, opNamesFilter, attributionTagFilter, flagsFilter, currentUid, this.mPackages.keyAt(i), attributionChains);
                if (this.mPackages.valueAt(i).isEmpty()) {
                    this.mPackages.removeAt(i);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void offsetHistory(long offset) {
            int nPackages = this.mPackages.size();
            for (int i = 0; i < nPackages; i++) {
                this.mPackages.valueAt(i).offsetHistory(offset);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearPackage(java.lang.String packageName) {
            this.mPackages.remove(packageName);
        }

        void addDiscreteAccess(int op, java.lang.String packageName, java.lang.String attributionTag, int flags, int uidState, long accessTime, long accessDuration, int attributionFlags, int attributionChainId) {
            getOrCreateDiscretePackageOps(packageName).addDiscreteAccess(op, attributionTag, flags, uidState, accessTime, accessDuration, attributionFlags, attributionChainId);
        }

        private com.android.server.appop.DiscreteRegistry.DiscretePackageOps getOrCreateDiscretePackageOps(java.lang.String packageName) {
            com.android.server.appop.DiscreteRegistry.DiscretePackageOps result = this.mPackages.get(packageName);
            if (result == null) {
                com.android.server.appop.DiscreteRegistry.DiscretePackageOps result2 = com.android.server.appop.DiscreteRegistry.this.new DiscretePackageOps();
                this.mPackages.put(packageName, result2);
                return result2;
            }
            return result;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void applyToHistory(android.app.AppOpsManager.HistoricalOps result, int uid, android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains) {
            int nPackages = this.mPackages.size();
            for (int i = 0; i < nPackages; i++) {
                this.mPackages.valueAt(i).applyToHistory(result, uid, this.mPackages.keyAt(i), attributionChains);
            }
        }

        void serialize(com.android.modules.utils.TypedXmlSerializer out) throws java.lang.Exception {
            int nPackages = this.mPackages.size();
            for (int i = 0; i < nPackages; i++) {
                out.startTag((java.lang.String) null, com.android.server.appop.DiscreteRegistry.TAG_PACKAGE);
                out.attribute((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_PACKAGE_NAME, this.mPackages.keyAt(i));
                this.mPackages.valueAt(i).serialize(out);
                out.endTag((java.lang.String) null, com.android.server.appop.DiscreteRegistry.TAG_PACKAGE);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.io.PrintWriter pw, java.text.SimpleDateFormat sdf, java.util.Date date, java.lang.String prefix, int nDiscreteOps) {
            int nPackages = this.mPackages.size();
            for (int i = 0; i < nPackages; i++) {
                pw.print(prefix);
                pw.print("Package: ");
                pw.print(this.mPackages.keyAt(i));
                pw.println();
                this.mPackages.valueAt(i).dump(pw, sdf, date, prefix + "  ", nDiscreteOps);
            }
        }

        void deserialize(com.android.modules.utils.TypedXmlPullParser parser, long beginTimeMillis) throws java.lang.Exception {
            int depth = parser.getDepth();
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
                if (com.android.server.appop.DiscreteRegistry.TAG_PACKAGE.equals(parser.getName())) {
                    java.lang.String packageName = parser.getAttributeValue((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_PACKAGE_NAME);
                    getOrCreateDiscretePackageOps(packageName).deserialize(parser, beginTimeMillis);
                }
            }
        }
    }

    private final class DiscretePackageOps {
        android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.DiscreteOp> mPackageOps = new android.util.ArrayMap<>();

        DiscretePackageOps() {
        }

        boolean isEmpty() {
            return this.mPackageOps.isEmpty();
        }

        void addDiscreteAccess(int op, java.lang.String attributionTag, int flags, int uidState, long accessTime, long accessDuration, int attributionFlags, int attributionChainId) {
            getOrCreateDiscreteOp(op).addDiscreteAccess(attributionTag, flags, uidState, accessTime, accessDuration, attributionFlags, attributionChainId);
        }

        void merge(com.android.server.appop.DiscreteRegistry.DiscretePackageOps other) {
            int nOps = other.mPackageOps.size();
            for (int i = 0; i < nOps; i++) {
                int opId = other.mPackageOps.keyAt(i).intValue();
                com.android.server.appop.DiscreteRegistry.DiscreteOp op = other.mPackageOps.valueAt(i);
                getOrCreateDiscreteOp(opId).merge(op);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void filter(long beginTimeMillis, long endTimeMillis, int filter, java.lang.String[] opNamesFilter, java.lang.String attributionTagFilter, int flagsFilter, int currentUid, java.lang.String currentPkgName, android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains) {
            int nOps = this.mPackageOps.size();
            for (int i = nOps - 1; i >= 0; i--) {
                int opId = this.mPackageOps.keyAt(i).intValue();
                if ((filter & 8) != 0 && !com.android.internal.util.ArrayUtils.contains(opNamesFilter, android.app.AppOpsManager.opToPublicName(opId))) {
                    this.mPackageOps.removeAt(i);
                } else {
                    this.mPackageOps.valueAt(i).filter(beginTimeMillis, endTimeMillis, filter, attributionTagFilter, flagsFilter, currentUid, currentPkgName, this.mPackageOps.keyAt(i).intValue(), attributionChains);
                    if (this.mPackageOps.valueAt(i).isEmpty()) {
                        this.mPackageOps.removeAt(i);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void offsetHistory(long offset) {
            int nOps = this.mPackageOps.size();
            for (int i = 0; i < nOps; i++) {
                this.mPackageOps.valueAt(i).offsetHistory(offset);
            }
        }

        private com.android.server.appop.DiscreteRegistry.DiscreteOp getOrCreateDiscreteOp(int op) {
            com.android.server.appop.DiscreteRegistry.DiscreteOp result = this.mPackageOps.get(java.lang.Integer.valueOf(op));
            if (result == null) {
                com.android.server.appop.DiscreteRegistry.DiscreteOp result2 = com.android.server.appop.DiscreteRegistry.this.new DiscreteOp();
                this.mPackageOps.put(java.lang.Integer.valueOf(op), result2);
                return result2;
            }
            return result;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void applyToHistory(android.app.AppOpsManager.HistoricalOps result, int uid, java.lang.String packageName, android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains) {
            int nPackageOps = this.mPackageOps.size();
            for (int i = 0; i < nPackageOps; i++) {
                this.mPackageOps.valueAt(i).applyToHistory(result, uid, packageName, this.mPackageOps.keyAt(i).intValue(), attributionChains);
            }
        }

        void serialize(com.android.modules.utils.TypedXmlSerializer out) throws java.lang.Exception {
            int nOps = this.mPackageOps.size();
            for (int i = 0; i < nOps; i++) {
                out.startTag((java.lang.String) null, com.android.server.appop.DiscreteRegistry.TAG_OP);
                out.attributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_OP_ID, this.mPackageOps.keyAt(i).intValue());
                this.mPackageOps.valueAt(i).serialize(out);
                out.endTag((java.lang.String) null, com.android.server.appop.DiscreteRegistry.TAG_OP);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.io.PrintWriter pw, java.text.SimpleDateFormat sdf, java.util.Date date, java.lang.String prefix, int nDiscreteOps) {
            int nOps = this.mPackageOps.size();
            for (int i = 0; i < nOps; i++) {
                pw.print(prefix);
                pw.print(android.app.AppOpsManager.opToName(this.mPackageOps.keyAt(i).intValue()));
                pw.println();
                this.mPackageOps.valueAt(i).dump(pw, sdf, date, prefix + "  ", nDiscreteOps);
            }
        }

        void deserialize(com.android.modules.utils.TypedXmlPullParser parser, long beginTimeMillis) throws java.lang.Exception {
            int depth = parser.getDepth();
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
                if (com.android.server.appop.DiscreteRegistry.TAG_OP.equals(parser.getName())) {
                    int op = parser.getAttributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_OP_ID);
                    getOrCreateDiscreteOp(op).deserialize(parser, beginTimeMillis);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class DiscreteOp {
        android.util.ArrayMap<java.lang.String, java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent>> mAttributedOps = new android.util.ArrayMap<>();

        DiscreteOp() {
        }

        boolean isEmpty() {
            return this.mAttributedOps.isEmpty();
        }

        void merge(com.android.server.appop.DiscreteRegistry.DiscreteOp other) {
            int nTags = other.mAttributedOps.size();
            for (int i = 0; i < nTags; i++) {
                java.lang.String tag = other.mAttributedOps.keyAt(i);
                java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> otherEvents = other.mAttributedOps.valueAt(i);
                java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> events = getOrCreateDiscreteOpEventsList(tag);
                this.mAttributedOps.put(tag, com.android.server.appop.DiscreteRegistry.stableListMerge(events, otherEvents));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void filter(long beginTimeMillis, long endTimeMillis, int filter, java.lang.String attributionTagFilter, int flagsFilter, int currentUid, java.lang.String currentPkgName, int currentOp, android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains) {
            if ((filter & 4) != 0) {
                android.util.ArrayMap<java.lang.String, java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent>> attributedOps = new android.util.ArrayMap<>();
                attributedOps.put(attributionTagFilter, getOrCreateDiscreteOpEventsList(attributionTagFilter));
                this.mAttributedOps = attributedOps;
            }
            int nTags = this.mAttributedOps.size();
            for (int i = nTags - 1; i >= 0; i--) {
                java.lang.String tag = this.mAttributedOps.keyAt(i);
                java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> list = com.android.server.appop.DiscreteRegistry.filterEventsList(this.mAttributedOps.valueAt(i), beginTimeMillis, endTimeMillis, flagsFilter, currentUid, currentPkgName, currentOp, this.mAttributedOps.keyAt(i), attributionChains);
                this.mAttributedOps.put(tag, list);
                if (list.size() == 0) {
                    this.mAttributedOps.removeAt(i);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void offsetHistory(long offset) {
            com.android.server.appop.DiscreteRegistry.DiscreteOp discreteOp = this;
            int nTags = discreteOp.mAttributedOps.size();
            int i = 0;
            while (i < nTags) {
                java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> list = discreteOp.mAttributedOps.valueAt(i);
                int n = list.size();
                int j = 0;
                while (j < n) {
                    com.android.server.appop.DiscreteRegistry.DiscreteOpEvent event = list.get(j);
                    list.set(j, com.android.server.appop.DiscreteRegistry.this.new DiscreteOpEvent(event.mNoteTime - offset, event.mNoteDuration, event.mUidState, event.mOpFlag, event.mAttributionFlags, event.mAttributionChainId));
                    j++;
                    discreteOp = this;
                    nTags = nTags;
                }
                i++;
                discreteOp = this;
            }
        }

        void addDiscreteAccess(java.lang.String attributionTag, int flags, int uidState, long accessTime, long accessDuration, int attributionFlags, int attributionChainId) {
            java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> attributedOps = getOrCreateDiscreteOpEventsList(attributionTag);
            int nAttributedOps = attributedOps.size();
            int i = nAttributedOps;
            while (true) {
                if (i <= 0) {
                    break;
                }
                com.android.server.appop.DiscreteRegistry.DiscreteOpEvent previousOp = attributedOps.get(i - 1);
                if (com.android.server.appop.DiscreteRegistry.discretizeTimeStamp(previousOp.mNoteTime) < com.android.server.appop.DiscreteRegistry.discretizeTimeStamp(accessTime)) {
                    break;
                }
                if (previousOp.mOpFlag == flags && previousOp.mUidState == uidState) {
                    if (previousOp.mAttributionFlags == attributionFlags) {
                        if (previousOp.mAttributionChainId == attributionChainId) {
                            if (com.android.server.appop.DiscreteRegistry.discretizeDuration(accessDuration) == com.android.server.appop.DiscreteRegistry.discretizeDuration(previousOp.mNoteDuration)) {
                                return;
                            }
                        }
                    }
                    i--;
                }
                i--;
            }
            attributedOps.add(i, com.android.server.appop.DiscreteRegistry.this.new DiscreteOpEvent(accessTime, accessDuration, uidState, flags, attributionFlags, attributionChainId));
        }

        private java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> getOrCreateDiscreteOpEventsList(java.lang.String attributionTag) {
            java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> result = this.mAttributedOps.get(attributionTag);
            if (result == null) {
                java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> result2 = new java.util.ArrayList<>();
                this.mAttributedOps.put(attributionTag, result2);
                return result2;
            }
            return result;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void applyToHistory(android.app.AppOpsManager.HistoricalOps result, int uid, java.lang.String packageName, int op, android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains) {
            com.android.server.appop.DiscreteRegistry.DiscreteOp discreteOp = this;
            int nOps = discreteOp.mAttributedOps.size();
            int i = 0;
            while (i < nOps) {
                java.lang.String tag = discreteOp.mAttributedOps.keyAt(i);
                java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> events = discreteOp.mAttributedOps.valueAt(i);
                int j = 0;
                for (int nEvents = events.size(); j < nEvents; nEvents = nEvents) {
                    com.android.server.appop.DiscreteRegistry.DiscreteOpEvent event = events.get(j);
                    android.app.AppOpsManager.OpEventProxyInfo proxy = null;
                    if (event.mAttributionChainId != -1 && attributionChains != null) {
                        com.android.server.appop.DiscreteRegistry.AttributionChain chain = attributionChains.get(java.lang.Integer.valueOf(event.mAttributionChainId));
                        if (chain != null && chain.isComplete()) {
                            if (chain.isStart(packageName, uid, tag, op, event) && chain.mLastVisibleEvent != null) {
                                com.android.server.appop.DiscreteRegistry.AttributionChain.OpEvent proxyEvent = chain.mLastVisibleEvent;
                                proxy = new android.app.AppOpsManager.OpEventProxyInfo(proxyEvent.mUid, proxyEvent.mPkgName, proxyEvent.mAttributionTag);
                            }
                        }
                    }
                    result.addDiscreteAccess(op, uid, packageName, tag, event.mUidState, event.mOpFlag, com.android.server.appop.DiscreteRegistry.discretizeTimeStamp(event.mNoteTime), com.android.server.appop.DiscreteRegistry.discretizeDuration(event.mNoteDuration), proxy);
                    j++;
                    events = events;
                }
                i++;
                discreteOp = this;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.io.PrintWriter pw, java.text.SimpleDateFormat sdf, java.util.Date date, java.lang.String prefix, int nDiscreteOps) {
            int nAttributions = this.mAttributedOps.size();
            for (int i = 0; i < nAttributions; i++) {
                pw.print(prefix);
                pw.print("Attribution: ");
                pw.print(this.mAttributedOps.keyAt(i));
                pw.println();
                java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> ops = this.mAttributedOps.valueAt(i);
                int nOps = ops.size();
                int first = nDiscreteOps >= 1 ? java.lang.Math.max(0, nOps - nDiscreteOps) : 0;
                for (int j = first; j < nOps; j++) {
                    ops.get(j).dump(pw, sdf, date, prefix + "  ");
                }
            }
        }

        void serialize(com.android.modules.utils.TypedXmlSerializer out) throws java.lang.Exception {
            int nAttributions = this.mAttributedOps.size();
            for (int i = 0; i < nAttributions; i++) {
                out.startTag((java.lang.String) null, "a");
                java.lang.String tag = this.mAttributedOps.keyAt(i);
                if (tag != null) {
                    out.attribute((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_TAG, this.mAttributedOps.keyAt(i));
                }
                java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> ops = this.mAttributedOps.valueAt(i);
                int nOps = ops.size();
                for (int j = 0; j < nOps; j++) {
                    out.startTag((java.lang.String) null, com.android.server.appop.DiscreteRegistry.TAG_ENTRY);
                    ops.get(j).serialize(out);
                    out.endTag((java.lang.String) null, com.android.server.appop.DiscreteRegistry.TAG_ENTRY);
                }
                out.endTag((java.lang.String) null, "a");
            }
        }

        void deserialize(com.android.modules.utils.TypedXmlPullParser parser, long beginTimeMillis) throws java.lang.Exception {
            int outerDepth = parser.getDepth();
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                if ("a".equals(parser.getName())) {
                    java.lang.String attributionTag = parser.getAttributeValue((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_TAG);
                    java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> events = getOrCreateDiscreteOpEventsList(attributionTag);
                    int innerDepth = parser.getDepth();
                    while (com.android.internal.util.XmlUtils.nextElementWithin(parser, innerDepth)) {
                        if (com.android.server.appop.DiscreteRegistry.TAG_ENTRY.equals(parser.getName())) {
                            long noteTime = parser.getAttributeLong((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_NOTE_TIME);
                            long noteDuration = parser.getAttributeLong((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_NOTE_DURATION, -1L);
                            int uidState = parser.getAttributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_UID_STATE);
                            int opFlags = parser.getAttributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_FLAGS);
                            int attributionFlags = parser.getAttributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_ATTRIBUTION_FLAGS, 0);
                            int attributionChainId = parser.getAttributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_CHAIN_ID, -1);
                            if (noteTime + noteDuration >= beginTimeMillis) {
                                com.android.server.appop.DiscreteRegistry.DiscreteOpEvent event = com.android.server.appop.DiscreteRegistry.this.new DiscreteOpEvent(noteTime, noteDuration, uidState, opFlags, attributionFlags, attributionChainId);
                                events.add(event);
                            }
                        }
                    }
                    java.util.Collections.sort(events, new java.util.Comparator() { // from class: com.android.server.appop.DiscreteRegistry$DiscreteOp$$ExternalSyntheticLambda0
                        @Override // java.util.Comparator
                        public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                            return com.android.server.appop.DiscreteRegistry.DiscreteOp.lambda$deserialize$0((com.android.server.appop.DiscreteRegistry.DiscreteOpEvent) obj, (com.android.server.appop.DiscreteRegistry.DiscreteOpEvent) obj2);
                        }
                    });
                }
            }
        }

        static /* synthetic */ int lambda$deserialize$0(com.android.server.appop.DiscreteRegistry.DiscreteOpEvent a, com.android.server.appop.DiscreteRegistry.DiscreteOpEvent b) {
            if (a.mNoteTime < b.mNoteTime) {
                return -1;
            }
            return a.mNoteTime == b.mNoteTime ? 0 : 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class DiscreteOpEvent {
        final int mAttributionChainId;
        final int mAttributionFlags;
        final long mNoteDuration;
        final long mNoteTime;
        final int mOpFlag;
        final int mUidState;

        DiscreteOpEvent(long noteTime, long noteDuration, int uidState, int opFlag, int attributionFlags, int attributionChainId) {
            this.mNoteTime = noteTime;
            this.mNoteDuration = noteDuration;
            this.mUidState = uidState;
            this.mOpFlag = opFlag;
            this.mAttributionFlags = attributionFlags;
            this.mAttributionChainId = attributionChainId;
        }

        public boolean equalsExceptDuration(com.android.server.appop.DiscreteRegistry.DiscreteOpEvent o) {
            return this.mNoteTime == o.mNoteTime && this.mUidState == o.mUidState && this.mOpFlag == o.mOpFlag && this.mAttributionFlags == o.mAttributionFlags && this.mAttributionChainId == o.mAttributionChainId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.io.PrintWriter pw, java.text.SimpleDateFormat sdf, java.util.Date date, java.lang.String prefix) {
            pw.print(prefix);
            pw.print("Access [");
            pw.print(android.app.AppOpsManager.getUidStateName(this.mUidState));
            pw.print("-");
            pw.print(android.app.AppOpsManager.flagsToString(this.mOpFlag));
            pw.print("] at ");
            date.setTime(com.android.server.appop.DiscreteRegistry.discretizeTimeStamp(this.mNoteTime));
            pw.print(sdf.format(date));
            if (this.mNoteDuration != -1) {
                pw.print(" for ");
                pw.print(com.android.server.appop.DiscreteRegistry.discretizeDuration(this.mNoteDuration));
                pw.print(" milliseconds ");
            }
            if (this.mAttributionFlags != 0) {
                pw.print(" attribution flags=");
                pw.print(this.mAttributionFlags);
                pw.print(" with chainId=");
                pw.print(this.mAttributionChainId);
            }
            pw.println();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void serialize(com.android.modules.utils.TypedXmlSerializer out) throws java.lang.Exception {
            out.attributeLong((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_NOTE_TIME, this.mNoteTime);
            if (this.mNoteDuration != -1) {
                out.attributeLong((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_NOTE_DURATION, this.mNoteDuration);
            }
            if (this.mAttributionFlags != 0) {
                out.attributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_ATTRIBUTION_FLAGS, this.mAttributionFlags);
            }
            if (this.mAttributionChainId != -1) {
                out.attributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_CHAIN_ID, this.mAttributionChainId);
            }
            out.attributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_UID_STATE, this.mUidState);
            out.attributeInt((java.lang.String) null, com.android.server.appop.DiscreteRegistry.ATTR_FLAGS, this.mOpFlag);
        }
    }

    private static int[] parseOpsList(java.lang.String opsList) {
        java.lang.String[] strArr;
        if (opsList.isEmpty()) {
            strArr = new java.lang.String[0];
        } else {
            strArr = opsList.split(",");
        }
        int nOps = strArr.length;
        int[] result = new int[nOps];
        for (int i = 0; i < nOps; i++) {
            try {
                result[i] = java.lang.Integer.parseInt(strArr[i]);
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.e(TAG, "Failed to parse Discrete ops list: " + e.getMessage());
                return parseOpsList(DEFAULT_DISCRETE_OPS);
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> stableListMerge(java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> a, java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> b) {
        int nA = a.size();
        int nB = b.size();
        int i = 0;
        int k = 0;
        java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> result = new java.util.ArrayList<>(nA + nB);
        while (true) {
            if (i < nA || k < nB) {
                if (i == nA) {
                    result.add(b.get(k));
                    k++;
                } else if (k == nB) {
                    result.add(a.get(i));
                    i++;
                } else if (a.get(i).mNoteTime < b.get(k).mNoteTime) {
                    result.add(a.get(i));
                    i++;
                } else {
                    int i2 = k + 1;
                    result.add(b.get(k));
                    k = i2;
                }
            } else {
                return result;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> filterEventsList(java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> list, long beginTimeMillis, long endTimeMillis, int flagsFilter, int currentUid, java.lang.String currentPackageName, int currentOp, java.lang.String currentAttrTag, android.util.ArrayMap<java.lang.Integer, com.android.server.appop.DiscreteRegistry.AttributionChain> attributionChains) {
        int n = list.size();
        java.util.List<com.android.server.appop.DiscreteRegistry.DiscreteOpEvent> result = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            com.android.server.appop.DiscreteRegistry.DiscreteOpEvent event = list.get(i);
            com.android.server.appop.DiscreteRegistry.AttributionChain chain = attributionChains.get(java.lang.Integer.valueOf(event.mAttributionChainId));
            if ((chain == null || chain.isStart(currentPackageName, currentUid, currentAttrTag, currentOp, event) || !chain.isComplete() || event.mAttributionChainId == -1) && (event.mOpFlag & flagsFilter) != 0 && event.mNoteTime + event.mNoteDuration > beginTimeMillis && event.mNoteTime < endTimeMillis) {
                result.add(event);
            }
        }
        return result;
    }

    private static boolean isDiscreteOp(int op, int flags) {
        return com.android.internal.util.ArrayUtils.contains(sDiscreteOps, op) && (sDiscreteFlags & flags) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long discretizeTimeStamp(long timeStamp) {
        return (timeStamp / sDiscreteHistoryQuantization) * sDiscreteHistoryQuantization;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long discretizeDuration(long duration) {
        if (duration == -1) {
            return -1L;
        }
        return (((sDiscreteHistoryQuantization + duration) - 1) / sDiscreteHistoryQuantization) * sDiscreteHistoryQuantization;
    }

    void setDebugMode(boolean debugMode) {
        this.mDebugMode = debugMode;
    }
}
