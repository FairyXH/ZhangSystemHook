package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class VibratorControlService extends android.frameworks.vibrator.IVibratorControlService.Stub {
    private static final java.time.format.DateTimeFormatter DEBUG_DATE_TIME_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS").withZone(java.time.ZoneId.systemDefault());
    private static final int NO_SCALE = -1;
    private static final java.lang.String TAG = "VibratorControlService";
    private static final int UNRECOGNIZED_VIBRATION_TYPE = -1;
    private final java.lang.Object mLock;
    private final int[] mRequestVibrationParamsForUsages;
    private final com.android.server.vibrator.VibratorFrameworkStatsLogger mStatsLogger;
    private com.android.server.vibrator.VibratorControlService.VibrationParamRequest mVibrationParamRequest = null;
    private final com.android.server.vibrator.VibratorControlService.VibrationParamsRecords mVibrationParamsRecords;
    private final com.android.server.vibrator.VibrationScaler mVibrationScaler;
    private final com.android.server.vibrator.VibratorControllerHolder mVibratorControllerHolder;

    VibratorControlService(android.content.Context context, com.android.server.vibrator.VibratorControllerHolder vibratorControllerHolder, com.android.server.vibrator.VibrationScaler vibrationScaler, com.android.server.vibrator.VibrationSettings vibrationSettings, com.android.server.vibrator.VibratorFrameworkStatsLogger statsLogger, java.lang.Object lock) {
        this.mVibratorControllerHolder = vibratorControllerHolder;
        this.mVibrationScaler = vibrationScaler;
        this.mStatsLogger = statsLogger;
        this.mLock = lock;
        this.mRequestVibrationParamsForUsages = vibrationSettings.getRequestVibrationParamsForUsages();
        int dumpSizeLimit = context.getResources().getInteger(android.R.integer.config_ntpPollingInterval);
        int dumpAggregationTimeLimit = context.getResources().getInteger(android.R.integer.config_notificationsBatteryNearlyFullLevel);
        this.mVibrationParamsRecords = new com.android.server.vibrator.VibratorControlService.VibrationParamsRecords(dumpSizeLimit, dumpAggregationTimeLimit);
    }

    @Override // android.frameworks.vibrator.IVibratorControlService
    public void registerVibratorController(android.frameworks.vibrator.IVibratorController controller) {
        java.util.Objects.requireNonNull(controller);
        synchronized (this.mLock) {
            this.mVibratorControllerHolder.setVibratorController(controller);
        }
    }

    @Override // android.frameworks.vibrator.IVibratorControlService
    public void unregisterVibratorController(android.frameworks.vibrator.IVibratorController controller) {
        java.util.Objects.requireNonNull(controller);
        synchronized (this.mLock) {
            if (this.mVibratorControllerHolder.getVibratorController() == null) {
                android.util.Slog.w(TAG, "Received request to unregister IVibratorController = " + controller + ", but no controller was previously registered. Request Ignored.");
            } else {
                if (!java.util.Objects.equals(this.mVibratorControllerHolder.getVibratorController().asBinder(), controller.asBinder())) {
                    android.util.Slog.wtf(TAG, "Failed to unregister IVibratorController. The provided controller doesn't match the registered one. " + this);
                    return;
                }
                this.mVibrationScaler.clearAdaptiveHapticsScales();
                this.mVibratorControllerHolder.setVibratorController(null);
                endOngoingRequestVibrationParamsLocked(true);
            }
        }
    }

    @Override // android.frameworks.vibrator.IVibratorControlService
    public void setVibrationParams(android.frameworks.vibrator.VibrationParam[] params, android.frameworks.vibrator.IVibratorController token) {
        java.util.Objects.requireNonNull(token);
        requireContainsNoNullElement(params);
        synchronized (this.mLock) {
            if (this.mVibratorControllerHolder.getVibratorController() == null) {
                android.util.Slog.w(TAG, "Received request to set VibrationParams for IVibratorController = " + token + ", but no controller was previously registered. Request Ignored.");
                return;
            }
            if (!java.util.Objects.equals(this.mVibratorControllerHolder.getVibratorController().asBinder(), token.asBinder())) {
                android.util.Slog.wtf(TAG, "Failed to set new VibrationParams. The provided controller doesn't match the registered one. " + this);
            } else if (params == null) {
                android.util.Slog.d(TAG, "New vibration params received but are null. New vibration params ignored.");
            } else {
                updateAdaptiveHapticsScales(params);
                recordUpdateVibrationParams(params, false);
            }
        }
    }

    @Override // android.frameworks.vibrator.IVibratorControlService
    public void clearVibrationParams(int types, android.frameworks.vibrator.IVibratorController token) {
        java.util.Objects.requireNonNull(token);
        synchronized (this.mLock) {
            if (this.mVibratorControllerHolder.getVibratorController() == null) {
                android.util.Slog.w(TAG, "Received request to clear VibrationParams for IVibratorController = " + token + ", but no controller was previously registered. Request Ignored.");
            } else if (!java.util.Objects.equals(this.mVibratorControllerHolder.getVibratorController().asBinder(), token.asBinder())) {
                android.util.Slog.wtf(TAG, "Failed to clear VibrationParams. The provided controller doesn't match the registered one. " + this);
            } else {
                updateAdaptiveHapticsScales(types, -1.0f);
                recordClearVibrationParams(types);
            }
        }
    }

    @Override // android.frameworks.vibrator.IVibratorControlService
    public void onRequestVibrationParamsComplete(android.os.IBinder requestToken, android.frameworks.vibrator.VibrationParam[] result) {
        java.util.Objects.requireNonNull(requestToken);
        requireContainsNoNullElement(result);
        synchronized (this.mLock) {
            if (this.mVibrationParamRequest == null) {
                android.util.Slog.wtf(TAG, "New vibration params received but no token was cached in the service. New vibration params ignored.");
                this.mStatsLogger.logVibrationParamResponseIgnored();
                return;
            }
            if (!java.util.Objects.equals(requestToken, this.mVibrationParamRequest.token)) {
                android.util.Slog.w(TAG, "New vibration params received but the provided token does not match the cached one. New vibration params ignored.");
                this.mStatsLogger.logVibrationParamResponseIgnored();
                return;
            }
            long latencyMs = android.os.SystemClock.uptimeMillis() - this.mVibrationParamRequest.uptimeMs;
            this.mStatsLogger.logVibrationParamRequestLatency(this.mVibrationParamRequest.uid, latencyMs);
            if (result == null) {
                android.util.Slog.d(TAG, "New vibration params received but are null. New vibration params ignored.");
                return;
            }
            updateAdaptiveHapticsScales(result);
            endOngoingRequestVibrationParamsLocked(false);
            recordUpdateVibrationParams(result, true);
        }
    }

    @Override // android.frameworks.vibrator.IVibratorControlService
    public int getInterfaceVersion() {
        return 1;
    }

    @Override // android.frameworks.vibrator.IVibratorControlService
    public java.lang.String getInterfaceHash() {
        return "eb095ed3034973273898ca9e37bbc72566392b8a";
    }

    public java.util.concurrent.CompletableFuture<java.lang.Void> triggerVibrationParamsRequest(int uid, int usage, int timeoutInMillis) {
        synchronized (this.mLock) {
            android.frameworks.vibrator.IVibratorController vibratorController = this.mVibratorControllerHolder.getVibratorController();
            if (vibratorController == null) {
                android.util.Slog.d(TAG, "Unable to request vibration params. There is no registered IVibrationController.");
                return null;
            }
            int vibrationType = mapToAdaptiveVibrationType(usage);
            if (vibrationType == -1) {
                android.util.Slog.d(TAG, "Unable to request vibration params. The provided usage " + usage + " is unrecognized.");
                return null;
            }
            try {
                endOngoingRequestVibrationParamsLocked(true);
                this.mVibrationParamRequest = new com.android.server.vibrator.VibratorControlService.VibrationParamRequest(uid);
                vibratorController.requestVibrationParams(vibrationType, timeoutInMillis, this.mVibrationParamRequest.token);
                return this.mVibrationParamRequest.future;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to request vibration params.", e);
                endOngoingRequestVibrationParamsLocked(true);
                return null;
            }
        }
    }

    public boolean shouldRequestVibrationParams(int usage) {
        synchronized (this.mLock) {
            android.frameworks.vibrator.IVibratorController vibratorController = this.mVibratorControllerHolder.getVibratorController();
            if (vibratorController == null) {
                return false;
            }
            return com.android.internal.util.ArrayUtils.contains(this.mRequestVibrationParamsForUsages, usage);
        }
    }

    public android.os.IBinder getRequestVibrationParamsToken() {
        android.os.IBinder iBinder;
        synchronized (this.mLock) {
            iBinder = this.mVibrationParamRequest == null ? null : this.mVibrationParamRequest.token;
        }
        return iBinder;
    }

    void dump(android.util.IndentingPrintWriter pw) {
        boolean hasPendingVibrationParamsRequest;
        boolean isVibratorControllerRegistered;
        synchronized (this.mLock) {
            hasPendingVibrationParamsRequest = true;
            isVibratorControllerRegistered = this.mVibratorControllerHolder.getVibratorController() != null;
            if (this.mVibrationParamRequest == null) {
                hasPendingVibrationParamsRequest = false;
            }
        }
        pw.println("VibratorControlService:");
        pw.increaseIndent();
        pw.println("isVibratorControllerRegistered = " + isVibratorControllerRegistered);
        pw.println("hasPendingVibrationParamsRequest = " + hasPendingVibrationParamsRequest);
        pw.println();
        pw.println("Vibration parameters update history:");
        pw.increaseIndent();
        this.mVibrationParamsRecords.dump(pw);
        pw.decreaseIndent();
        pw.decreaseIndent();
    }

    void dump(android.util.proto.ProtoOutputStream proto) {
        boolean isVibratorControllerRegistered;
        synchronized (this.mLock) {
            isVibratorControllerRegistered = this.mVibratorControllerHolder.getVibratorController() != null;
        }
        proto.write(1120986464283L, isVibratorControllerRegistered);
        this.mVibrationParamsRecords.dump(proto);
    }

    private void endOngoingRequestVibrationParamsLocked(boolean wasCancelled) {
        if (this.mVibrationParamRequest != null) {
            this.mVibrationParamRequest.endRequest(wasCancelled);
        }
        this.mVibrationParamRequest = null;
    }

    private static int mapToAdaptiveVibrationType(int usage) {
        switch (usage) {
            case 0:
            case 19:
                return 16;
            case 17:
                return 1;
            case 18:
            case 34:
            case 50:
            case 66:
                return 8;
            case 33:
                return 4;
            case 49:
            case 65:
                return 2;
            default:
                android.util.Slog.w(TAG, "Unrecognized vibration usage " + usage);
                return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[] mapFromAdaptiveVibrationTypeToVibrationUsages(int types) {
        android.util.IntArray usages = new android.util.IntArray(15);
        if ((types & 1) != 0) {
            usages.add(17);
        }
        if ((types & 2) != 0) {
            usages.add(49);
            usages.add(65);
        }
        if ((types & 4) != 0) {
            usages.add(33);
        }
        if ((types & 16) != 0) {
            usages.add(19);
            usages.add(0);
        }
        if ((types & 8) != 0) {
            usages.add(18);
            usages.add(50);
        }
        return usages.toArray();
    }

    private void updateAdaptiveHapticsScales(android.frameworks.vibrator.VibrationParam[] params) {
        java.util.Objects.requireNonNull(params);
        for (android.frameworks.vibrator.VibrationParam param : params) {
            if (param.getTag() != 0) {
                android.util.Slog.e(TAG, "Unsupported vibration param: " + param);
            } else {
                android.frameworks.vibrator.ScaleParam scaleParam = param.getScale();
                updateAdaptiveHapticsScales(scaleParam.typesMask, scaleParam.scale);
            }
        }
    }

    private void updateAdaptiveHapticsScales(int types, float scale) {
        this.mStatsLogger.logVibrationParamScale(scale);
        for (int usage : mapFromAdaptiveVibrationTypeToVibrationUsages(types)) {
            updateOrRemoveAdaptiveHapticsScale(usage, scale);
        }
    }

    private void updateOrRemoveAdaptiveHapticsScale(int usageHint, float scale) {
        if (scale == -1.0f) {
            this.mVibrationScaler.removeAdaptiveHapticsScale(usageHint);
        } else {
            this.mVibrationScaler.updateAdaptiveHapticsScale(usageHint, scale);
        }
    }

    private void recordUpdateVibrationParams(android.frameworks.vibrator.VibrationParam[] params, boolean fromRequest) {
        java.util.Objects.requireNonNull(params);
        com.android.server.vibrator.VibratorControlService.VibrationParamsRecords.Operation operation = fromRequest ? com.android.server.vibrator.VibratorControlService.VibrationParamsRecords.Operation.PULL : com.android.server.vibrator.VibratorControlService.VibrationParamsRecords.Operation.PUSH;
        long createTime = android.os.SystemClock.uptimeMillis();
        for (android.frameworks.vibrator.VibrationParam param : params) {
            if (param.getTag() != 0) {
                android.util.Slog.w(TAG, "Unsupported vibration param ignored from dumpsys records: " + param);
            } else {
                android.frameworks.vibrator.ScaleParam scaleParam = param.getScale();
                this.mVibrationParamsRecords.add(new com.android.server.vibrator.VibratorControlService.VibrationScaleParamRecord(operation, createTime, scaleParam.typesMask, scaleParam.scale));
            }
        }
    }

    private void recordClearVibrationParams(int typesMask) {
        long createTime = android.os.SystemClock.uptimeMillis();
        this.mVibrationParamsRecords.add(new com.android.server.vibrator.VibratorControlService.VibrationScaleParamRecord(com.android.server.vibrator.VibratorControlService.VibrationParamsRecords.Operation.CLEAR, createTime, typesMask, -1.0f));
    }

    private void requireContainsNoNullElement(android.frameworks.vibrator.VibrationParam[] params) {
        if (com.android.internal.util.ArrayUtils.contains(params, (java.lang.Object) null)) {
            throw new java.lang.IllegalArgumentException("Invalid vibration params received: null values are not permitted.");
        }
    }

    private static final class VibrationParamsRecords extends com.android.server.vibrator.GroupedAggregatedLogRecords<com.android.server.vibrator.VibratorControlService.VibrationScaleParamRecord> {

        enum Operation {
            PULL,
            PUSH,
            CLEAR
        }

        VibrationParamsRecords(int sizeLimit, int aggregationTimeLimit) {
            super(sizeLimit, aggregationTimeLimit);
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords
        synchronized void dumpGroupHeader(android.util.IndentingPrintWriter pw, int paramType) {
            if (paramType == 0) {
                pw.println("SCALE:");
            } else {
                pw.println("UNKNOWN:");
            }
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords
        synchronized long findGroupKeyProtoFieldId(int usage) {
            return 2246267895836L;
        }
    }

    private static final class VibrationParamRequest {
        public final int uid;
        public final java.util.concurrent.CompletableFuture<java.lang.Void> future = new java.util.concurrent.CompletableFuture<>();
        public final android.os.IBinder token = new android.os.Binder();
        public final long uptimeMs = android.os.SystemClock.uptimeMillis();

        VibrationParamRequest(int uid) {
            this.uid = uid;
        }

        public void endRequest(boolean wasCancelled) {
            if (wasCancelled) {
                this.future.cancel(true);
            } else {
                this.future.complete(null);
            }
        }
    }

    private static final class VibrationScaleParamRecord implements com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord {
        private final long mCreateTime;
        private final com.android.server.vibrator.VibratorControlService.VibrationParamsRecords.Operation mOperation;
        private final float mScale;
        private final int mTypesMask;

        VibrationScaleParamRecord(com.android.server.vibrator.VibratorControlService.VibrationParamsRecords.Operation operation, long createTime, int typesMask, float scale) {
            this.mOperation = operation;
            this.mCreateTime = createTime;
            this.mTypesMask = typesMask;
            this.mScale = scale;
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord
        public int getGroupKey() {
            return 0;
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord
        public long getCreateUptimeMs() {
            return this.mCreateTime;
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord
        public boolean mayAggregate(com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord record) {
            if (!(record instanceof com.android.server.vibrator.VibratorControlService.VibrationScaleParamRecord)) {
                return false;
            }
            com.android.server.vibrator.VibratorControlService.VibrationScaleParamRecord param = (com.android.server.vibrator.VibratorControlService.VibrationScaleParamRecord) record;
            return this.mTypesMask == param.mTypesMask && this.mOperation == param.mOperation;
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord
        public void dump(android.util.IndentingPrintWriter pw) {
            java.lang.String line = java.lang.String.format(java.util.Locale.ROOT, "%s | %6s | scale: %5s | typesMask: %6s | usages: %s", com.android.server.vibrator.VibratorControlService.DEBUG_DATE_TIME_FORMATTER.format(java.time.Instant.ofEpochMilli(this.mCreateTime)), this.mOperation.name().toLowerCase(java.util.Locale.ROOT), this.mScale == -1.0f ? "" : java.lang.String.format(java.util.Locale.ROOT, "%.2f", java.lang.Float.valueOf(this.mScale)), java.lang.Long.toBinaryString(this.mTypesMask), createVibrationUsagesString());
            pw.println(line);
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord
        public void dump(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1112396529666L, this.mCreateTime);
            proto.write(1133871366147L, this.mOperation == com.android.server.vibrator.VibratorControlService.VibrationParamsRecords.Operation.PULL);
            long scaleToken = proto.start(1146756268033L);
            proto.write(1120986464257L, this.mTypesMask);
            proto.write(1108101562370L, this.mScale);
            proto.end(scaleToken);
            proto.end(token);
        }

        private java.lang.String createVibrationUsagesString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            int[] usages = com.android.server.vibrator.VibratorControlService.mapFromAdaptiveVibrationTypeToVibrationUsages(this.mTypesMask);
            for (int i = 0; i < usages.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(android.os.VibrationAttributes.usageToString(usages[i]));
            }
            return sb.toString();
        }
    }
}
