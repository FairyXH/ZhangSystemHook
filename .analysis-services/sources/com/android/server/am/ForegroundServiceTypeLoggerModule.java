package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class ForegroundServiceTypeLoggerModule {
    public static final int FGS_API_BEGIN_WITH_FGS = 1;
    public static final int FGS_API_END_WITHOUT_FGS = 3;
    public static final int FGS_API_END_WITH_FGS = 2;
    public static final int FGS_API_PAUSE = 4;
    public static final int FGS_API_RESUME = 5;
    public static final int FGS_STATE_CHANGED_API_CALL = 4;
    private static final java.lang.String TAG = "ForegroundServiceTypeLoggerModule";
    private final android.util.SparseArray<com.android.server.am.ForegroundServiceTypeLoggerModule.UidState> mUids = new android.util.SparseArray<>();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface FgsApiState {
    }

    private static class UidState {
        final android.util.SparseArray<com.android.server.am.ForegroundServiceTypeLoggerModule.FgsApiRecord> mApiClosedCalls;
        final android.util.SparseArray<com.android.server.am.ForegroundServiceTypeLoggerModule.FgsApiRecord> mApiOpenCalls;
        final android.util.SparseArray<java.lang.Long> mFirstFgsTimeStamp;
        final android.util.SparseArray<java.lang.Long> mLastFgsTimeStamp;
        final android.util.SparseIntArray mOpenWithFgsCount;
        final android.util.SparseIntArray mOpenedWithoutFgsCount;
        final android.util.SparseArray<android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord>> mRunningFgs;

        private UidState() {
            this.mApiOpenCalls = new android.util.SparseArray<>();
            this.mApiClosedCalls = new android.util.SparseArray<>();
            this.mOpenedWithoutFgsCount = new android.util.SparseIntArray();
            this.mOpenWithFgsCount = new android.util.SparseIntArray();
            this.mRunningFgs = new android.util.SparseArray<>();
            this.mLastFgsTimeStamp = new android.util.SparseArray<>();
            this.mFirstFgsTimeStamp = new android.util.SparseArray<>();
        }
    }

    public void logForegroundServiceStart(int uid, int pid, com.android.server.am.ServiceRecord record) {
        com.android.server.am.ForegroundServiceTypeLoggerModule.UidState uidState;
        if (record.getComponentName() != null) {
            java.lang.String traceTag = record.getComponentName().flattenToString() + ":" + uid;
            android.os.Trace.asyncTraceForTrackBegin(64L, traceTag, "foregroundService", record.foregroundServiceType);
        }
        com.android.server.am.ForegroundServiceTypeLoggerModule.UidState uidState2 = this.mUids.get(uid);
        if (uidState2 != null) {
            uidState = uidState2;
        } else {
            com.android.server.am.ForegroundServiceTypeLoggerModule.UidState uidState3 = new com.android.server.am.ForegroundServiceTypeLoggerModule.UidState();
            this.mUids.put(uid, uidState3);
            uidState = uidState3;
        }
        android.util.IntArray apiTypes = convertFgsTypeToApiTypes(record.foregroundServiceType);
        if (apiTypes.size() == 0) {
            android.util.Slog.w(TAG, "Foreground service start for UID: " + uid + " does not have any types");
        }
        android.util.IntArray apiTypesFound = new android.util.IntArray();
        android.util.LongArray timestampsFound = new android.util.LongArray();
        int size = apiTypes.size();
        for (int i = 0; i < size; i++) {
            int apiType = apiTypes.get(i);
            int fgsIndex = uidState.mRunningFgs.indexOfKey(apiType);
            if (fgsIndex < 0) {
                uidState.mRunningFgs.put(apiType, new android.util.ArrayMap<>());
                fgsIndex = uidState.mRunningFgs.indexOfKey(apiType);
                uidState.mFirstFgsTimeStamp.put(apiType, java.lang.Long.valueOf(java.lang.System.currentTimeMillis()));
            }
            android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> fgsList = uidState.mRunningFgs.valueAt(fgsIndex);
            fgsList.put(record.getComponentName(), record);
            if (uidState.mApiOpenCalls.contains(apiType)) {
                uidState.mOpenWithFgsCount.put(apiType, uidState.mOpenedWithoutFgsCount.get(apiType));
                uidState.mOpenedWithoutFgsCount.put(apiType, 0);
                apiTypesFound.add(apiType);
                com.android.server.am.ForegroundServiceTypeLoggerModule.FgsApiRecord call = uidState.mApiOpenCalls.get(apiType);
                timestampsFound.add(call.mTimeStart);
                call.mIsAssociatedWithFgs = true;
                call.mAssociatedFgsRecord = record;
                uidState.mApiOpenCalls.remove(apiType);
            }
        }
        int i2 = apiTypesFound.size();
        if (i2 != 0) {
            int size2 = apiTypesFound.size();
            for (int i3 = 0; i3 < size2; i3++) {
                logFgsApiEvent(record, 4, 1, apiTypesFound.get(i3), timestampsFound.get(i3));
            }
        }
    }

    public void logForegroundServiceStop(int uid, com.android.server.am.ServiceRecord record) {
        java.lang.String str;
        java.lang.String str2;
        if (record.getComponentName() != null) {
            java.lang.String traceTag = record.getComponentName().flattenToString() + ":" + uid;
            android.os.Trace.asyncTraceForTrackEnd(64L, traceTag, record.hashCode());
        }
        android.util.IntArray apiTypes = convertFgsTypeToApiTypes(record.foregroundServiceType);
        com.android.server.am.ForegroundServiceTypeLoggerModule.UidState uidState = this.mUids.get(uid);
        int size = apiTypes.size();
        java.lang.String str3 = TAG;
        if (size == 0) {
            android.util.Slog.w(TAG, "FGS stop call for: " + uid + " has no types!");
        }
        java.lang.String str4 = " in package ";
        if (uidState == null) {
            android.util.Slog.w(TAG, "FGS stop call being logged with no start call for UID for UID " + uid + " in package " + record.packageName);
            return;
        }
        java.util.ArrayList<java.lang.Integer> apisFound = new java.util.ArrayList<>();
        java.util.ArrayList<java.lang.Long> timestampsFound = new java.util.ArrayList<>();
        int i = 0;
        int size2 = apiTypes.size();
        while (i < size2) {
            int apiType = apiTypes.get(i);
            android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> runningFgsOfType = uidState.mRunningFgs.get(apiType);
            if (runningFgsOfType == null) {
                android.util.Slog.w(str3, "Could not find appropriate running FGS for FGS stop for UID " + uid + str4 + record.packageName);
                str = str4;
                str2 = str3;
            } else {
                runningFgsOfType.remove(record.getComponentName());
                if (runningFgsOfType.size() == 0) {
                    uidState.mRunningFgs.remove(apiType);
                    uidState.mLastFgsTimeStamp.put(apiType, java.lang.Long.valueOf(java.lang.System.currentTimeMillis()));
                }
                int apiTypeIndex = uidState.mOpenWithFgsCount.indexOfKey(apiType);
                if (apiTypeIndex < 0) {
                    android.util.Slog.w(str3, "Logger should be tracking FGS types correctly for UID " + uid + str4 + record.packageName);
                    str = str4;
                    str2 = str3;
                } else {
                    com.android.server.am.ForegroundServiceTypeLoggerModule.FgsApiRecord closedApi = uidState.mApiClosedCalls.get(apiType);
                    if (closedApi == null) {
                        str = str4;
                        str2 = str3;
                    } else if (uidState.mOpenWithFgsCount.valueAt(apiTypeIndex) != 0) {
                        str = str4;
                        str2 = str3;
                    } else {
                        apisFound.add(java.lang.Integer.valueOf(apiType));
                        str = str4;
                        str2 = str3;
                        timestampsFound.add(java.lang.Long.valueOf(closedApi.mTimeStart));
                        uidState.mApiClosedCalls.remove(apiType);
                    }
                }
            }
            i++;
            str3 = str2;
            str4 = str;
        }
        if (!apisFound.isEmpty()) {
            for (int i2 = 0; i2 < apisFound.size(); i2++) {
                logFgsApiEvent(record, 4, 2, apisFound.get(i2).intValue(), timestampsFound.get(i2).longValue());
            }
        }
    }

    public long logForegroundServiceApiEventBegin(int apiType, int uid, int pid, java.lang.String packageName) {
        com.android.server.am.ForegroundServiceTypeLoggerModule.UidState uidState;
        int openWithFgsIndex;
        com.android.server.am.ForegroundServiceTypeLoggerModule.FgsApiRecord callStart = new com.android.server.am.ForegroundServiceTypeLoggerModule.FgsApiRecord(uid, pid, packageName, apiType, java.lang.System.currentTimeMillis());
        com.android.server.am.ForegroundServiceTypeLoggerModule.UidState uidState2 = this.mUids.get(uid);
        if (uidState2 != null) {
            uidState = uidState2;
        } else {
            com.android.server.am.ForegroundServiceTypeLoggerModule.UidState uidState3 = new com.android.server.am.ForegroundServiceTypeLoggerModule.UidState();
            this.mUids.put(uid, uidState3);
            uidState = uidState3;
        }
        if (!hasValidActiveFgs(uid, apiType)) {
            int openWithoutFgsCountIndex = uidState.mOpenedWithoutFgsCount.indexOfKey(apiType);
            if (openWithoutFgsCountIndex < 0) {
                uidState.mOpenedWithoutFgsCount.put(apiType, 0);
                openWithoutFgsCountIndex = uidState.mOpenedWithoutFgsCount.indexOfKey(apiType);
            }
            if (!uidState.mApiOpenCalls.contains(apiType) || uidState.mOpenedWithoutFgsCount.valueAt(openWithoutFgsCountIndex) == 0) {
                uidState.mApiOpenCalls.put(apiType, callStart);
            }
            uidState.mOpenedWithoutFgsCount.put(apiType, uidState.mOpenedWithoutFgsCount.get(apiType) + 1);
            return callStart.mTimeStart;
        }
        int openWithFgsIndex2 = uidState.mOpenWithFgsCount.indexOfKey(apiType);
        if (openWithFgsIndex2 >= 0) {
            openWithFgsIndex = openWithFgsIndex2;
        } else {
            uidState.mOpenWithFgsCount.put(apiType, 0);
            openWithFgsIndex = uidState.mOpenWithFgsCount.indexOfKey(apiType);
        }
        uidState.mOpenWithFgsCount.put(apiType, uidState.mOpenWithFgsCount.valueAt(openWithFgsIndex) + 1);
        android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> fgsListMap = uidState.mRunningFgs.get(apiType);
        long timestamps = callStart.mTimeStart;
        if (uidState.mOpenWithFgsCount.valueAt(openWithFgsIndex) == 1) {
            for (com.android.server.am.ServiceRecord record : fgsListMap.values()) {
                logFgsApiEvent(record, 4, 1, apiType, timestamps);
            }
        }
        return callStart.mTimeStart;
    }

    public long logForegroundServiceApiEventEnd(int apiType, int uid, int pid) {
        com.android.server.am.ForegroundServiceTypeLoggerModule.UidState uidState = this.mUids.get(uid);
        if (uidState == null) {
            android.util.Slog.w(TAG, "API event end called before start!");
            return -1L;
        }
        int apiIndex = uidState.mOpenWithFgsCount.indexOfKey(apiType);
        if (apiIndex >= 0) {
            if (uidState.mOpenWithFgsCount.get(apiType) != 0) {
                uidState.mOpenWithFgsCount.put(apiType, uidState.mOpenWithFgsCount.get(apiType) - 1);
            }
            if (!hasValidActiveFgs(uid, apiType) && uidState.mOpenWithFgsCount.get(apiType) == 0) {
                long timestamp = java.lang.System.currentTimeMillis();
                logFgsApiEventWithNoFgs(uid, 3, apiType, timestamp);
                uidState.mOpenWithFgsCount.removeAt(apiIndex);
                return timestamp;
            }
        }
        if (uidState.mOpenedWithoutFgsCount.indexOfKey(apiType) < 0) {
            uidState.mOpenedWithoutFgsCount.put(apiType, 0);
        }
        int apiOpenWithoutFgsCount = uidState.mOpenedWithoutFgsCount.get(apiType);
        if (apiOpenWithoutFgsCount != 0) {
            int apiOpenWithoutFgsCount2 = apiOpenWithoutFgsCount - 1;
            if (apiOpenWithoutFgsCount2 == 0) {
                uidState.mApiOpenCalls.remove(apiType);
            }
            uidState.mOpenedWithoutFgsCount.put(apiType, apiOpenWithoutFgsCount2);
            return java.lang.System.currentTimeMillis();
        }
        android.util.SparseArray<com.android.server.am.ForegroundServiceTypeLoggerModule.FgsApiRecord> sparseArray = uidState.mApiClosedCalls;
        com.android.server.am.ForegroundServiceTypeLoggerModule.FgsApiRecord closedCall = new com.android.server.am.ForegroundServiceTypeLoggerModule.FgsApiRecord(uid, pid, "", apiType, java.lang.System.currentTimeMillis());
        uidState.mApiClosedCalls.put(apiType, closedCall);
        return closedCall.mTimeStart;
    }

    public void logForegroundServiceApiStateChanged(int apiType, int uid, int pid, int state) {
        com.android.server.am.ForegroundServiceTypeLoggerModule.UidState uidState = this.mUids.get(uid);
        if (uidState.mRunningFgs.contains(apiType)) {
            android.util.ArrayMap<android.content.ComponentName, com.android.server.am.ServiceRecord> fgsRecords = uidState.mRunningFgs.get(apiType);
            long timestamp = java.lang.System.currentTimeMillis();
            for (com.android.server.am.ServiceRecord record : fgsRecords.values()) {
                logFgsApiEvent(record, 4, state, apiType, timestamp);
            }
        }
    }

    private android.util.IntArray convertFgsTypeToApiTypes(int fgsType) {
        android.util.IntArray types = new android.util.IntArray();
        if ((fgsType & 64) == 64) {
            types.add(1);
        }
        if ((fgsType & 16) == 16) {
            types.add(2);
            types.add(8);
            types.add(9);
        }
        if ((fgsType & 8) == 8) {
            types.add(3);
        }
        if ((fgsType & 2) == 2) {
            types.add(5);
            types.add(4);
        }
        if ((fgsType & 128) == 128) {
            types.add(6);
        }
        if ((fgsType & 4) == 4) {
            types.add(7);
        }
        return types;
    }

    private boolean hasValidActiveFgs(int uid, int apiType) {
        com.android.server.am.ForegroundServiceTypeLoggerModule.UidState uidState = this.mUids.get(uid);
        if (uidState != null) {
            return uidState.mRunningFgs.contains(apiType);
        }
        return false;
    }

    public void logFgsApiEvent(com.android.server.am.ServiceRecord r, int fgsState, int apiState, int apiType, long timestamp) {
        int i;
        long apiDurationAfterFgsEnd = 0;
        com.android.server.am.ForegroundServiceTypeLoggerModule.UidState uidState = this.mUids.get(r.appInfo.uid);
        if (uidState == null) {
            return;
        }
        long apiDurationBeforeFgsStart = uidState.mFirstFgsTimeStamp.contains(apiType) ? uidState.mFirstFgsTimeStamp.get(apiType).longValue() - timestamp : 0L;
        if (uidState.mLastFgsTimeStamp.contains(apiType)) {
            apiDurationAfterFgsEnd = timestamp - uidState.mLastFgsTimeStamp.get(apiType).longValue();
        }
        int[] apiTypes = {apiType};
        long[] timeStamps = {timestamp};
        int i2 = r.appInfo.uid;
        java.lang.String str = r.shortInstanceName;
        boolean zIsFgsAllowedWiu_forCapabilities = r.isFgsAllowedWiu_forCapabilities();
        int fgsAllowStart = r.getFgsAllowStart();
        int i3 = r.appInfo.targetSdkVersion;
        int i4 = r.mRecentCallingUid;
        int i5 = r.mInfoTempFgsAllowListReason != null ? r.mInfoTempFgsAllowListReason.mCallingUid : -1;
        boolean z = r.mFgsNotificationWasDeferred;
        boolean z2 = r.mFgsNotificationShown;
        int i6 = r.mStartForegroundCount;
        boolean z3 = r.mFgsHasNotificationPermission;
        int i7 = r.foregroundServiceType;
        boolean z4 = r.mIsFgsDelegate;
        int i8 = r.mFgsDelegation != null ? r.mFgsDelegation.mOptions.mClientUid : -1;
        if (r.mFgsDelegation != null) {
            i = r.mFgsDelegation.mOptions.mDelegationService;
        } else {
            i = 0;
        }
        com.android.internal.util.FrameworkStatsLog.write(60, i2, str, fgsState, zIsFgsAllowedWiu_forCapabilities, fgsAllowStart, i3, i4, 0, i5, z, z2, 0, i6, 0, z3, i7, 0, z4, i8, i, apiState, apiTypes, timeStamps, -1, 0, -1, 0, apiDurationBeforeFgsStart, apiDurationAfterFgsEnd, r.mAllowWiu_noBinding, r.mAllowWiu_inBindService, r.mAllowWiu_byBindings, r.mAllowStart_noBinding, r.mAllowStart_inBindService, r.mAllowStart_byBindings, 0, false);
    }

    public void logFgsApiEventWithNoFgs(int uid, int apiState, int apiType, long timestamp) {
        com.android.server.am.ForegroundServiceTypeLoggerModule.UidState uidState = this.mUids.get(uid);
        if (uidState != null) {
            long apiDurationAfterFgsEnd = uidState.mLastFgsTimeStamp.contains(apiType) ? timestamp - uidState.mLastFgsTimeStamp.get(apiType).longValue() : 0L;
            int[] apiTypes = {apiType};
            long[] timeStamps = {timestamp};
            com.android.internal.util.FrameworkStatsLog.write(60, uid, null, 4, false, 0, 0, uid, 0, 0, false, false, 0, 0, 0, false, 0, 0, false, 0, 0, apiState, apiTypes, timeStamps, -1, 0, -1, 0, 0L, apiDurationAfterFgsEnd, 0, 0, 0, 0, 0, 0, 0, false);
        }
    }

    private static class FgsApiRecord {
        com.android.server.am.ServiceRecord mAssociatedFgsRecord;
        boolean mIsAssociatedWithFgs;
        final java.lang.String mPackageName;
        final int mPid;
        final long mTimeStart;
        int mType;
        final int mUid;

        FgsApiRecord(int uid, int pid, java.lang.String packageName, int type, long timeStart) {
            this.mUid = uid;
            this.mPid = pid;
            this.mPackageName = packageName;
            this.mType = type;
            this.mTimeStart = timeStart;
        }
    }
}
