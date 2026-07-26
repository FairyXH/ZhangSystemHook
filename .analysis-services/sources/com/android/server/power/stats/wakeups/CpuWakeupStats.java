package com.android.server.power.stats.wakeups;

/* JADX INFO: loaded from: classes3.dex */
public class CpuWakeupStats {
    private static final java.lang.String SUBSYSTEM_ALARM_STRING = "Alarm";
    private static final java.lang.String SUBSYSTEM_CELLULAR_DATA_STRING = "Cellular_data";
    private static final java.lang.String SUBSYSTEM_SENSOR_STRING = "Sensor";
    private static final java.lang.String SUBSYSTEM_SOUND_TRIGGER_STRING = "Sound_trigger";
    private static final java.lang.String SUBSYSTEM_WIFI_STRING = "Wifi";
    private static final java.lang.String TAG = "CpuWakeupStats";
    private static final java.lang.String TRACE_TRACK_WAKEUP_ATTRIBUTION = "wakeup_attribution";
    private static final long WAKEUP_WRITE_DELAY_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(30);
    private final android.os.Handler mHandler;
    private final com.android.server.power.stats.wakeups.IrqDeviceMap mIrqDeviceMap;
    final com.android.server.power.stats.wakeups.CpuWakeupStats.Config mConfig = new com.android.server.power.stats.wakeups.CpuWakeupStats.Config();
    final android.util.LongSparseArray<com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup> mWakeupEvents = new android.util.LongSparseArray<>();
    final android.util.LongSparseArray<android.util.SparseArray<android.util.SparseIntArray>> mWakeupAttribution = new android.util.LongSparseArray<>();
    final android.util.SparseIntArray mUidProcStates = new android.util.SparseIntArray();
    private final android.util.SparseIntArray mReusableUidProcStates = new android.util.SparseIntArray(4);
    private final com.android.server.power.stats.wakeups.CpuWakeupStats.WakingActivityHistory mRecentWakingActivity = new com.android.server.power.stats.wakeups.CpuWakeupStats.WakingActivityHistory(new java.util.function.LongSupplier() { // from class: com.android.server.power.stats.wakeups.CpuWakeupStats$$ExternalSyntheticLambda1
        @Override // java.util.function.LongSupplier
        public final long getAsLong() {
            return this.f$0.lambda$new$0();
        }
    });

    public CpuWakeupStats(android.content.Context context, int mapRes, android.os.Handler handler) {
        this.mIrqDeviceMap = com.android.server.power.stats.wakeups.IrqDeviceMap.getInstance(context, mapRes);
        this.mHandler = handler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ long lambda$new$0() {
        return this.mConfig.WAKING_ACTIVITY_RETENTION_MS;
    }

    public synchronized void systemServicesReady() {
        this.mConfig.register(new android.os.HandlerExecutor(this.mHandler));
    }

    private static int typeToStatsType(int wakeupType) {
        switch (wakeupType) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;
        }
    }

    private static int subsystemToStatsReason(int subsystem) {
        switch (subsystem) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: logWakeupAttribution, reason: merged with bridge method [inline-methods] */
    public synchronized void lambda$noteWakeupTimeAndReason$1(com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup wakeupToLog) {
        int[] procStatesProto;
        int[] uids;
        if (com.android.internal.util.ArrayUtils.isEmpty(wakeupToLog.mDevices)) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.KERNEL_WAKEUP_ATTRIBUTED, 0, 0, (int[]) null, wakeupToLog.mElapsedMillis, (int[]) null);
            android.os.Trace.instantForTrack(131072L, TRACE_TRACK_WAKEUP_ATTRIBUTION, wakeupToLog.mElapsedMillis + " --");
            return;
        }
        android.util.SparseArray<android.util.SparseIntArray> wakeupAttribution = this.mWakeupAttribution.get(wakeupToLog.mElapsedMillis);
        if (wakeupAttribution == null) {
            android.util.Slog.wtf(TAG, "Unexpected null attribution found for " + wakeupToLog);
            return;
        }
        java.lang.StringBuilder traceEventBuilder = new java.lang.StringBuilder();
        for (int i = 0; i < wakeupAttribution.size(); i++) {
            int subsystem = wakeupAttribution.keyAt(i);
            android.util.SparseIntArray uidProcStates = wakeupAttribution.valueAt(i);
            if (uidProcStates == null || uidProcStates.size() == 0) {
                int[] iArr = new int[0];
                procStatesProto = iArr;
                uids = iArr;
            } else {
                int numUids = uidProcStates.size();
                uids = new int[numUids];
                procStatesProto = new int[numUids];
                for (int j = 0; j < numUids; j++) {
                    uids[j] = uidProcStates.keyAt(j);
                    procStatesProto[j] = android.app.ActivityManager.processStateAmToProto(uidProcStates.valueAt(j));
                }
            }
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.KERNEL_WAKEUP_ATTRIBUTED, typeToStatsType(wakeupToLog.mType), subsystemToStatsReason(subsystem), uids, wakeupToLog.mElapsedMillis, procStatesProto);
            if (android.os.Trace.isTagEnabled(131072L)) {
                if (i == 0) {
                    traceEventBuilder.append(wakeupToLog.mElapsedMillis + " ");
                }
                traceEventBuilder.append(subsystemToString(subsystem));
                traceEventBuilder.append(":");
                traceEventBuilder.append(java.util.Arrays.toString(uids));
                traceEventBuilder.append(" ");
            }
        }
        android.os.Trace.instantForTrack(131072L, TRACE_TRACK_WAKEUP_ATTRIBUTION, traceEventBuilder.toString().trim());
    }

    public synchronized void onUidRemoved(int uid) {
        this.mUidProcStates.delete(uid);
    }

    public synchronized void noteUidProcessState(int uid, int state) {
        this.mUidProcStates.put(uid, state);
    }

    public synchronized void noteWakeupTimeAndReason(long elapsedRealtime, long uptime, java.lang.String rawReason) {
        final com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup parsedWakeup = com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup.parseWakeup(rawReason, elapsedRealtime, uptime, this.mIrqDeviceMap);
        if (parsedWakeup == null) {
            return;
        }
        this.mWakeupEvents.put(elapsedRealtime, parsedWakeup);
        attemptAttributionFor(parsedWakeup);
        long retentionDuration = this.mConfig.WAKEUP_STATS_RETENTION_MS;
        int lastIdx = this.mWakeupEvents.lastIndexOnOrBefore(elapsedRealtime - retentionDuration);
        for (int i = lastIdx; i >= 0; i--) {
            this.mWakeupEvents.removeAt(i);
        }
        for (int i2 = this.mWakeupAttribution.lastIndexOnOrBefore(elapsedRealtime - retentionDuration); i2 >= 0; i2--) {
            this.mWakeupAttribution.removeAt(i2);
        }
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.power.stats.wakeups.CpuWakeupStats$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$noteWakeupTimeAndReason$1(parsedWakeup);
            }
        }, WAKEUP_WRITE_DELAY_MS);
    }

    public synchronized void noteWakingActivity(int subsystem, long elapsedRealtime, int... uids) {
        if (uids == null) {
            return;
        }
        this.mReusableUidProcStates.clear();
        for (int i = 0; i < uids.length; i++) {
            this.mReusableUidProcStates.put(uids[i], this.mUidProcStates.get(uids[i], -1));
        }
        if (!attemptAttributionWith(subsystem, elapsedRealtime, this.mReusableUidProcStates)) {
            this.mRecentWakingActivity.recordActivity(subsystem, elapsedRealtime, this.mReusableUidProcStates);
        }
    }

    private synchronized void attemptAttributionFor(com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup wakeup) {
        android.util.SparseBooleanArray subsystems = wakeup.mResponsibleSubsystems;
        android.util.SparseArray<android.util.SparseIntArray> attribution = this.mWakeupAttribution.get(wakeup.mElapsedMillis);
        if (attribution == null) {
            attribution = new android.util.SparseArray<>();
            this.mWakeupAttribution.put(wakeup.mElapsedMillis, attribution);
        }
        long matchingWindowMillis = this.mConfig.WAKEUP_MATCHING_WINDOW_MS;
        for (int subsystemIdx = 0; subsystemIdx < subsystems.size(); subsystemIdx++) {
            int subsystem = subsystems.keyAt(subsystemIdx);
            long startTime = wakeup.mElapsedMillis - matchingWindowMillis;
            long endTime = wakeup.mElapsedMillis + matchingWindowMillis;
            android.util.SparseIntArray uidsToBlame = this.mRecentWakingActivity.removeBetween(subsystem, startTime, endTime);
            attribution.put(subsystem, uidsToBlame);
        }
    }

    private synchronized boolean attemptAttributionWith(int subsystem, long activityElapsed, android.util.SparseIntArray uidProcStates) {
        long matchingWindowMillis = this.mConfig.WAKEUP_MATCHING_WINDOW_MS;
        int startIdx = this.mWakeupEvents.firstIndexOnOrAfter(activityElapsed - matchingWindowMillis);
        int endIdx = this.mWakeupEvents.lastIndexOnOrBefore(activityElapsed + matchingWindowMillis);
        for (int wakeupIdx = startIdx; wakeupIdx <= endIdx; wakeupIdx++) {
            com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup wakeup = this.mWakeupEvents.valueAt(wakeupIdx);
            android.util.SparseBooleanArray subsystems = wakeup.mResponsibleSubsystems;
            if (subsystems.get(subsystem)) {
                android.util.SparseArray<android.util.SparseIntArray> attribution = this.mWakeupAttribution.get(wakeup.mElapsedMillis);
                if (attribution == null) {
                    attribution = new android.util.SparseArray<>();
                    this.mWakeupAttribution.put(wakeup.mElapsedMillis, attribution);
                }
                android.util.SparseIntArray uidsToBlame = attribution.get(subsystem);
                if (uidsToBlame == null) {
                    attribution.put(subsystem, uidProcStates.clone());
                } else {
                    for (int i = 0; i < uidProcStates.size(); i++) {
                        uidsToBlame.put(uidProcStates.keyAt(i), uidProcStates.valueAt(i));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public synchronized void dump(android.util.IndentingPrintWriter pw, long nowElapsed) {
        long j = nowElapsed;
        synchronized (this) {
            pw.println("CPU wakeup stats:");
            pw.increaseIndent();
            this.mConfig.dump(pw);
            pw.println();
            this.mIrqDeviceMap.dump(pw);
            pw.println();
            this.mRecentWakingActivity.dump(pw, j);
            pw.println();
            pw.println("Current proc-state map (" + this.mUidProcStates.size() + "):");
            pw.increaseIndent();
            for (int i = 0; i < this.mUidProcStates.size(); i++) {
                if (i > 0) {
                    pw.print(", ");
                }
                android.os.UserHandle.formatUid(pw, this.mUidProcStates.keyAt(i));
                pw.print(":" + android.app.ActivityManager.procStateToString(this.mUidProcStates.valueAt(i)));
            }
            pw.println();
            pw.decreaseIndent();
            pw.println();
            android.util.SparseLongArray attributionStats = new android.util.SparseLongArray();
            pw.println("Wakeup events:");
            pw.increaseIndent();
            int i2 = this.mWakeupEvents.size() - 1;
            while (i2 >= 0) {
                android.util.TimeUtils.formatDuration(this.mWakeupEvents.keyAt(i2), j, pw);
                pw.println(":");
                pw.increaseIndent();
                com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup wakeup = this.mWakeupEvents.valueAt(i2);
                pw.println(wakeup);
                pw.print("Attribution: ");
                android.util.SparseArray<android.util.SparseIntArray> attribution = this.mWakeupAttribution.get(wakeup.mElapsedMillis);
                if (attribution == null) {
                    pw.println("N/A");
                } else {
                    for (int subsystemIdx = 0; subsystemIdx < attribution.size(); subsystemIdx++) {
                        if (subsystemIdx > 0) {
                            pw.print(", ");
                        }
                        long counters = attributionStats.get(attribution.keyAt(subsystemIdx), com.android.internal.util.IntPair.of(0, 0));
                        int attributed = com.android.internal.util.IntPair.first(counters);
                        int total = com.android.internal.util.IntPair.second(counters) + 1;
                        pw.print(subsystemToString(attribution.keyAt(subsystemIdx)));
                        pw.print(" [");
                        android.util.SparseIntArray uidProcStates = attribution.valueAt(subsystemIdx);
                        if (uidProcStates != null) {
                            for (int uidIdx = 0; uidIdx < uidProcStates.size(); uidIdx++) {
                                if (uidIdx > 0) {
                                    pw.print(", ");
                                }
                                android.os.UserHandle.formatUid(pw, uidProcStates.keyAt(uidIdx));
                                pw.print(" " + android.app.ActivityManager.procStateToString(uidProcStates.valueAt(uidIdx)));
                            }
                            attributed++;
                        }
                        pw.print("]");
                        attributionStats.put(attribution.keyAt(subsystemIdx), com.android.internal.util.IntPair.of(attributed, total));
                    }
                    pw.println();
                }
                pw.decreaseIndent();
                i2--;
                j = nowElapsed;
            }
            pw.decreaseIndent();
            pw.println("Attribution stats:");
            pw.increaseIndent();
            for (int i3 = 0; i3 < attributionStats.size(); i3++) {
                pw.print("Subsystem " + subsystemToString(attributionStats.keyAt(i3)));
                pw.print(": ");
                long ratio = attributionStats.valueAt(i3);
                pw.println(com.android.internal.util.IntPair.first(ratio) + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + com.android.internal.util.IntPair.second(ratio));
            }
            pw.println("Total: " + this.mWakeupEvents.size());
            pw.decreaseIndent();
            pw.decreaseIndent();
            pw.println();
        }
    }

    static final class WakingActivityHistory {
        private java.util.function.LongSupplier mRetentionSupplier;
        final android.util.SparseArray<android.util.LongSparseArray<android.util.SparseIntArray>> mWakingActivity = new android.util.SparseArray<>();

        WakingActivityHistory(java.util.function.LongSupplier retentionSupplier) {
            this.mRetentionSupplier = retentionSupplier;
        }

        void recordActivity(int subsystem, long elapsedRealtime, android.util.SparseIntArray uidProcStates) {
            if (uidProcStates == null) {
                return;
            }
            android.util.LongSparseArray<android.util.SparseIntArray> wakingActivity = this.mWakingActivity.get(subsystem);
            if (wakingActivity == null) {
                wakingActivity = new android.util.LongSparseArray<>();
                this.mWakingActivity.put(subsystem, wakingActivity);
            }
            android.util.SparseIntArray uidsToBlame = wakingActivity.get(elapsedRealtime);
            if (uidsToBlame == null) {
                wakingActivity.put(elapsedRealtime, uidProcStates.clone());
            } else {
                for (int i = 0; i < uidProcStates.size(); i++) {
                    int uid = uidProcStates.keyAt(i);
                    if (uidsToBlame.indexOfKey(uid) < 0) {
                        uidsToBlame.put(uid, uidProcStates.valueAt(i));
                    }
                }
            }
            int endIdx = wakingActivity.lastIndexOnOrBefore(elapsedRealtime - this.mRetentionSupplier.getAsLong());
            for (int i2 = endIdx; i2 >= 0; i2--) {
                wakingActivity.removeAt(i2);
            }
        }

        android.util.SparseIntArray removeBetween(int subsystem, long startElapsed, long endElapsed) {
            android.util.SparseIntArray uidsToReturn = new android.util.SparseIntArray();
            android.util.LongSparseArray<android.util.SparseIntArray> activityForSubsystem = this.mWakingActivity.get(subsystem);
            if (activityForSubsystem != null) {
                int startIdx = activityForSubsystem.firstIndexOnOrAfter(startElapsed);
                int endIdx = activityForSubsystem.lastIndexOnOrBefore(endElapsed);
                for (int i = endIdx; i >= startIdx; i--) {
                    android.util.SparseIntArray uidsForTime = activityForSubsystem.valueAt(i);
                    for (int j = 0; j < uidsForTime.size(); j++) {
                        uidsToReturn.put(uidsForTime.keyAt(j), uidsForTime.valueAt(j));
                    }
                }
                for (int i2 = endIdx; i2 >= startIdx; i2--) {
                    activityForSubsystem.removeAt(i2);
                }
            }
            if (uidsToReturn.size() > 0) {
                return uidsToReturn;
            }
            return null;
        }

        void dump(android.util.IndentingPrintWriter pw, long nowElapsed) {
            pw.println("Recent waking activity:");
            pw.increaseIndent();
            for (int i = 0; i < this.mWakingActivity.size(); i++) {
                pw.println("Subsystem " + com.android.server.power.stats.wakeups.CpuWakeupStats.subsystemToString(this.mWakingActivity.keyAt(i)) + ":");
                android.util.LongSparseArray<android.util.SparseIntArray> wakingActivity = this.mWakingActivity.valueAt(i);
                if (wakingActivity != null) {
                    pw.increaseIndent();
                    for (int j = wakingActivity.size() - 1; j >= 0; j--) {
                        android.util.TimeUtils.formatDuration(wakingActivity.keyAt(j), nowElapsed, pw);
                        android.util.SparseIntArray uidsToBlame = wakingActivity.valueAt(j);
                        if (uidsToBlame == null) {
                            pw.println();
                        } else {
                            pw.print(": ");
                            for (int k = 0; k < uidsToBlame.size(); k++) {
                                android.os.UserHandle.formatUid(pw, uidsToBlame.keyAt(k));
                                pw.print(" [" + android.app.ActivityManager.procStateToString(uidsToBlame.valueAt(k)));
                                pw.print("], ");
                            }
                            pw.println();
                        }
                    }
                    pw.decreaseIndent();
                }
            }
            pw.decreaseIndent();
        }
    }

    static int stringToKnownSubsystem(java.lang.String rawSubsystem) {
        byte b;
        switch (rawSubsystem.hashCode()) {
            case -1822081062:
                b = !rawSubsystem.equals(SUBSYSTEM_SENSOR_STRING) ? (byte) -1 : (byte) 3;
                break;
            case -1102294721:
                b = !rawSubsystem.equals(SUBSYSTEM_CELLULAR_DATA_STRING) ? (byte) -1 : (byte) 4;
                break;
            case -424380824:
                b = !rawSubsystem.equals(SUBSYSTEM_SOUND_TRIGGER_STRING) ? (byte) -1 : (byte) 2;
                break;
            case 2695989:
                b = !rawSubsystem.equals(SUBSYSTEM_WIFI_STRING) ? (byte) -1 : (byte) 1;
                break;
            case 63343153:
                b = !rawSubsystem.equals(SUBSYSTEM_ALARM_STRING) ? (byte) -1 : (byte) 0;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            default:
                return -1;
        }
    }

    static java.lang.String subsystemToString(int subsystem) {
        switch (subsystem) {
            case -1:
                return "Unknown";
            case 0:
            default:
                return "N/A";
            case 1:
                return SUBSYSTEM_ALARM_STRING;
            case 2:
                return SUBSYSTEM_WIFI_STRING;
            case 3:
                return SUBSYSTEM_SOUND_TRIGGER_STRING;
            case 4:
                return SUBSYSTEM_SENSOR_STRING;
            case 5:
                return SUBSYSTEM_CELLULAR_DATA_STRING;
        }
    }

    static final class Wakeup {
        private static final java.lang.String ABORT_REASON_PREFIX = "Abort";
        private static final java.lang.String PARSER_TAG = "CpuWakeupStats.Wakeup";
        static final int TYPE_ABNORMAL = 2;
        static final int TYPE_IRQ = 1;
        private static final java.util.regex.Pattern sIrqPattern = java.util.regex.Pattern.compile("^(\\-?\\d+)\\s+(\\S+)");
        com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup.IrqDevice[] mDevices;
        long mElapsedMillis;
        android.util.SparseBooleanArray mResponsibleSubsystems;
        int mType;
        long mUptimeMillis;

        private Wakeup(int type, com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup.IrqDevice[] devices, long elapsedMillis, long uptimeMillis, android.util.SparseBooleanArray responsibleSubsystems) {
            this.mType = type;
            this.mDevices = devices;
            this.mElapsedMillis = elapsedMillis;
            this.mUptimeMillis = uptimeMillis;
            this.mResponsibleSubsystems = responsibleSubsystems;
        }

        static com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup parseWakeup(java.lang.String rawReason, long elapsedMillis, long uptimeMillis, com.android.server.power.stats.wakeups.IrqDeviceMap deviceMap) {
            java.lang.String[] components = rawReason.split(":");
            if (!com.android.internal.util.ArrayUtils.isEmpty(components) && !components[0].startsWith(ABORT_REASON_PREFIX)) {
                com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup.IrqDevice[] parsedDevices = new com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup.IrqDevice[components.length];
                android.util.SparseBooleanArray responsibleSubsystems = new android.util.SparseBooleanArray();
                int length = components.length;
                int i = 0;
                int parsedDeviceCount = 0;
                int parsedDeviceCount2 = 1;
                while (true) {
                    int type = -1;
                    if (i >= length) {
                        break;
                    }
                    java.lang.String component = components[i];
                    java.util.regex.Matcher matcher = sIrqPattern.matcher(component.trim());
                    if (matcher.find()) {
                        try {
                            int line = java.lang.Integer.parseInt(matcher.group(1));
                            java.lang.String device = matcher.group(2);
                            if (line < 0) {
                                parsedDeviceCount2 = 2;
                            }
                            int parsedDeviceCount3 = parsedDeviceCount + 1;
                            parsedDevices[parsedDeviceCount] = new com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup.IrqDevice(line, device);
                            java.util.List<java.lang.String> rawSubsystems = deviceMap.getSubsystemsForDevice(device);
                            boolean anyKnownSubsystem = false;
                            if (rawSubsystems != null) {
                                int i2 = 0;
                                while (i2 < rawSubsystems.size()) {
                                    int subsystem = com.android.server.power.stats.wakeups.CpuWakeupStats.stringToKnownSubsystem(rawSubsystems.get(i2));
                                    if (subsystem != type) {
                                        responsibleSubsystems.put(subsystem, true);
                                        anyKnownSubsystem = true;
                                    }
                                    i2++;
                                    type = -1;
                                }
                            }
                            if (!anyKnownSubsystem) {
                                responsibleSubsystems.put(-1, true);
                            }
                            parsedDeviceCount = parsedDeviceCount3;
                        } catch (java.lang.NumberFormatException e) {
                            android.util.Slog.e(PARSER_TAG, "Exception while parsing device names from part: " + component, e);
                        }
                    }
                    i++;
                }
                if (parsedDeviceCount == 0) {
                    return null;
                }
                if (responsibleSubsystems.size() == 1 && responsibleSubsystems.get(-1, false)) {
                    return null;
                }
                return new com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup(parsedDeviceCount2, (com.android.server.power.stats.wakeups.CpuWakeupStats.Wakeup.IrqDevice[]) java.util.Arrays.copyOf(parsedDevices, parsedDeviceCount), elapsedMillis, uptimeMillis, responsibleSubsystems);
            }
            return null;
        }

        public java.lang.String toString() {
            return "Wakeup{mType=" + this.mType + ", mElapsedMillis=" + this.mElapsedMillis + ", mUptimeMillis=" + this.mUptimeMillis + ", mDevices=" + java.util.Arrays.toString(this.mDevices) + ", mResponsibleSubsystems=" + this.mResponsibleSubsystems + '}';
        }

        static final class IrqDevice {
            java.lang.String mDevice;
            int mLine;

            IrqDevice(int line, java.lang.String device) {
                this.mLine = line;
                this.mDevice = device;
            }

            public java.lang.String toString() {
                return "IrqDevice{mLine=" + this.mLine + ", mDevice='" + this.mDevice + "'}";
            }
        }
    }

    static final class Config implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        static final java.lang.String KEY_WAKEUP_STATS_RETENTION_MS = "wakeup_stats_retention_ms";
        static final java.lang.String KEY_WAKEUP_MATCHING_WINDOW_MS = "wakeup_matching_window_ms";
        static final java.lang.String KEY_WAKING_ACTIVITY_RETENTION_MS = "waking_activity_retention_ms";
        private static final java.lang.String[] PROPERTY_NAMES = {KEY_WAKEUP_STATS_RETENTION_MS, KEY_WAKEUP_MATCHING_WINDOW_MS, KEY_WAKING_ACTIVITY_RETENTION_MS};
        static final long DEFAULT_WAKEUP_STATS_RETENTION_MS = java.util.concurrent.TimeUnit.DAYS.toMillis(3);
        private static final long DEFAULT_WAKEUP_MATCHING_WINDOW_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(1);
        private static final long DEFAULT_WAKING_ACTIVITY_RETENTION_MS = java.util.concurrent.TimeUnit.MINUTES.toMillis(5);
        public volatile long WAKEUP_STATS_RETENTION_MS = DEFAULT_WAKEUP_STATS_RETENTION_MS;
        public volatile long WAKEUP_MATCHING_WINDOW_MS = DEFAULT_WAKEUP_MATCHING_WINDOW_MS;
        public volatile long WAKING_ACTIVITY_RETENTION_MS = DEFAULT_WAKING_ACTIVITY_RETENTION_MS;

        Config() {
        }

        void register(java.util.concurrent.Executor executor) {
            android.provider.DeviceConfig.addOnPropertiesChangedListener("battery_stats", executor, this);
            onPropertiesChanged(android.provider.DeviceConfig.getProperties("battery_stats", PROPERTY_NAMES));
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0040  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onPropertiesChanged(android.provider.DeviceConfig.Properties r7) {
            /*
                r6 = this;
                java.util.Set r0 = r7.getKeyset()
                java.util.Iterator r0 = r0.iterator()
            L8:
                boolean r1 = r0.hasNext()
                if (r1 == 0) goto L61
                java.lang.Object r1 = r0.next()
                java.lang.String r1 = (java.lang.String) r1
                if (r1 != 0) goto L17
                goto L8
            L17:
                int r2 = r1.hashCode()
                java.lang.String r3 = "waking_activity_retention_ms"
                java.lang.String r4 = "wakeup_stats_retention_ms"
                java.lang.String r5 = "wakeup_matching_window_ms"
                switch(r2) {
                    case 241713043: goto L38;
                    case 588912391: goto L30;
                    case 1049257273: goto L28;
                    default: goto L27;
                }
            L27:
                goto L40
            L28:
                boolean r2 = r1.equals(r3)
                if (r2 == 0) goto L27
                r2 = 2
                goto L41
            L30:
                boolean r2 = r1.equals(r4)
                if (r2 == 0) goto L27
                r2 = 0
                goto L41
            L38:
                boolean r2 = r1.equals(r5)
                if (r2 == 0) goto L27
                r2 = 1
                goto L41
            L40:
                r2 = -1
            L41:
                switch(r2) {
                    case 0: goto L57;
                    case 1: goto L4e;
                    case 2: goto L45;
                    default: goto L44;
                }
            L44:
                goto L60
            L45:
                long r4 = com.android.server.power.stats.wakeups.CpuWakeupStats.Config.DEFAULT_WAKING_ACTIVITY_RETENTION_MS
                long r2 = r7.getLong(r3, r4)
                r6.WAKING_ACTIVITY_RETENTION_MS = r2
                goto L60
            L4e:
                long r2 = com.android.server.power.stats.wakeups.CpuWakeupStats.Config.DEFAULT_WAKEUP_MATCHING_WINDOW_MS
                long r2 = r7.getLong(r5, r2)
                r6.WAKEUP_MATCHING_WINDOW_MS = r2
                goto L60
            L57:
                long r2 = com.android.server.power.stats.wakeups.CpuWakeupStats.Config.DEFAULT_WAKEUP_STATS_RETENTION_MS
                long r2 = r7.getLong(r4, r2)
                r6.WAKEUP_STATS_RETENTION_MS = r2
            L60:
                goto L8
            L61:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.stats.wakeups.CpuWakeupStats.Config.onPropertiesChanged(android.provider.DeviceConfig$Properties):void");
        }

        void dump(android.util.IndentingPrintWriter pw) {
            pw.println("Config:");
            pw.increaseIndent();
            pw.print(KEY_WAKEUP_STATS_RETENTION_MS);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.WAKEUP_STATS_RETENTION_MS, pw);
            pw.println();
            pw.print(KEY_WAKEUP_MATCHING_WINDOW_MS);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.WAKEUP_MATCHING_WINDOW_MS, pw);
            pw.println();
            pw.print(KEY_WAKING_ACTIVITY_RETENTION_MS);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.WAKING_ACTIVITY_RETENTION_MS, pw);
            pw.println();
            pw.decreaseIndent();
        }
    }
}
