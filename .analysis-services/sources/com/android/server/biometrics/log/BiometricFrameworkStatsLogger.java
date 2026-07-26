package com.android.server.biometrics.log;

/* JADX INFO: loaded from: classes.dex */
public class BiometricFrameworkStatsLogger {
    private static final java.lang.String TAG = "BiometricFrameworkStatsLogger";
    private static final com.android.server.biometrics.log.BiometricFrameworkStatsLogger sInstance = new com.android.server.biometrics.log.BiometricFrameworkStatsLogger();

    private BiometricFrameworkStatsLogger() {
    }

    public static com.android.server.biometrics.log.BiometricFrameworkStatsLogger getInstance() {
        return sInstance;
    }

    public void acquired(com.android.server.biometrics.log.OperationContextExt operationContext, int statsModality, int statsAction, int statsClient, boolean isDebug, int acquiredInfo, int vendorCode, int targetUserId) {
        com.android.internal.util.FrameworkStatsLog.write(87, statsModality, targetUserId, operationContext.isCrypto(), statsAction, statsClient, acquiredInfo, vendorCode, isDebug, -1, operationContext.getId(), sessionType(operationContext.getReason()), operationContext.isAod(), operationContext.isDisplayOn(), operationContext.getDockState(), orientationType(operationContext.getOrientation()), foldType(operationContext.getFoldState()), operationContext.getOrderAndIncrement(), toProtoWakeReason(operationContext));
    }

    public void authenticate(com.android.server.biometrics.log.OperationContextExt operationContext, int statsModality, int statsAction, int statsClient, boolean isDebug, long latency, int authState, boolean requireConfirmation, int targetUserId, float ambientLightLux) {
        com.android.internal.util.FrameworkStatsLog.write(88, statsModality, targetUserId, operationContext.isCrypto(), statsClient, requireConfirmation, authState, sanitizeLatency(latency), isDebug, -1, ambientLightLux, operationContext.getId(), sessionType(operationContext.getReason()), operationContext.isAod(), operationContext.isDisplayOn(), operationContext.getDockState(), orientationType(operationContext.getOrientation()), foldType(operationContext.getFoldState()), operationContext.getOrderAndIncrement(), toProtoWakeReason(operationContext), toProtoWakeReasonDetails(operationContext));
    }

    public void authenticate(final com.android.server.biometrics.log.OperationContextExt operationContext, final int statsModality, final int statsAction, final int statsClient, final boolean isDebug, final long latency, final int authState, final boolean requireConfirmation, final int targetUserId, com.android.server.biometrics.log.ALSProbe alsProbe) {
        alsProbe.awaitNextLux(new java.util.function.Consumer() { // from class: com.android.server.biometrics.log.BiometricFrameworkStatsLogger$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$authenticate$0(operationContext, statsModality, statsAction, statsClient, isDebug, latency, authState, requireConfirmation, targetUserId, (java.lang.Float) obj);
            }
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$authenticate$0(com.android.server.biometrics.log.OperationContextExt operationContext, int statsModality, int statsAction, int statsClient, boolean isDebug, long latency, int authState, boolean requireConfirmation, int targetUserId, java.lang.Float ambientLightLux) {
        authenticate(operationContext, statsModality, statsAction, statsClient, isDebug, latency, authState, requireConfirmation, targetUserId, ambientLightLux.floatValue());
    }

    public void enroll(int statsModality, int statsAction, int statsClient, int targetUserId, long latency, boolean enrollSuccessful, float ambientLightLux, int source) {
        com.android.internal.util.FrameworkStatsLog.write(184, statsModality, targetUserId, sanitizeLatency(latency), enrollSuccessful, -1, ambientLightLux, source);
    }

    public void error(com.android.server.biometrics.log.OperationContextExt operationContext, int statsModality, int statsAction, int statsClient, boolean isDebug, long latency, int error, int vendorCode, int targetUserId) {
        com.android.internal.util.FrameworkStatsLog.write(89, statsModality, targetUserId, operationContext.isCrypto(), statsAction, statsClient, error, vendorCode, isDebug, sanitizeLatency(latency), -1, operationContext.getId(), sessionType(operationContext.getReason()), operationContext.isAod(), operationContext.isDisplayOn(), operationContext.getDockState(), orientationType(operationContext.getOrientation()), foldType(operationContext.getFoldState()), operationContext.getOrderAndIncrement(), toProtoWakeReason(operationContext), toProtoWakeReasonDetails(operationContext));
    }

    static int[] toProtoWakeReasonDetails(com.android.server.biometrics.log.OperationContextExt operationContext) {
        android.hardware.biometrics.common.OperationContext ctx = operationContext.toAidlContext();
        return java.util.stream.Stream.of(java.lang.Integer.valueOf(toProtoWakeReasonDetails(ctx.authenticateReason))).mapToInt(new java.util.function.ToIntFunction() { // from class: com.android.server.biometrics.log.BiometricFrameworkStatsLogger$$ExternalSyntheticLambda1
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(java.lang.Object obj) {
                return ((java.lang.Integer) obj).intValue();
            }
        }).filter(new java.util.function.IntPredicate() { // from class: com.android.server.biometrics.log.BiometricFrameworkStatsLogger$$ExternalSyntheticLambda2
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return com.android.server.biometrics.log.BiometricFrameworkStatsLogger.lambda$toProtoWakeReasonDetails$2(i);
            }
        }).toArray();
    }

    static /* synthetic */ boolean lambda$toProtoWakeReasonDetails$2(int i) {
        return i != 0;
    }

    static int toProtoWakeReason(com.android.server.biometrics.log.OperationContextExt operationContext) {
        int reason = operationContext.getWakeReason();
        switch (reason) {
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
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            default:
                return 0;
        }
    }

    private static int toProtoWakeReasonDetails(android.hardware.biometrics.common.AuthenticateReason reason) {
        if (reason != null) {
            switch (reason.getTag()) {
                case 1:
                    return toProtoWakeReasonDetailsFromFace(reason.getFaceAuthenticateReason());
                default:
                    return 0;
            }
        }
        return 0;
    }

    private static int toProtoWakeReasonDetailsFromFace(int reason) {
        switch (reason) {
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
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            default:
                return 0;
        }
    }

    public void reportUnknownTemplateEnrolledHal(int statsModality) {
        com.android.internal.util.FrameworkStatsLog.write(148, statsModality, 3, -1);
    }

    public void reportUnknownTemplateEnrolledFramework(int statsModality) {
        com.android.internal.util.FrameworkStatsLog.write(148, statsModality, 2, -1);
    }

    public void logFrameworkNotification(int action, int modality) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BIOMETRIC_FRR_NOTIFICATION, action, modality);
    }

    private long sanitizeLatency(long latency) {
        if (latency < 0) {
            android.util.Slog.w(TAG, "found a negative latency : " + latency);
            return -1L;
        }
        return latency;
    }

    private static int sessionType(byte reason) {
        if (reason == 1) {
            return 2;
        }
        return reason == 2 ? 1 : 0;
    }

    private static int orientationType(int rotation) {
        switch (rotation) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            default:
                return 0;
        }
    }

    private static int foldType(int foldType) {
        switch (foldType) {
            case 1:
                return 3;
            case 2:
                return 1;
            case 3:
                return 2;
            default:
                return 0;
        }
    }
}
