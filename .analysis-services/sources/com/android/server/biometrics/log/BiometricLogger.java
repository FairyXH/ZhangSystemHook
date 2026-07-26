package com.android.server.biometrics.log;

/* JADX INFO: loaded from: classes.dex */
public class BiometricLogger {
    public static final boolean DEBUG = true;
    public static final java.lang.String TAG = "BiometricLogger";
    private final com.android.server.biometrics.log.ALSProbe mALSProbe;
    private final com.android.server.biometrics.AuthenticationStatsCollector mAuthenticationStatsCollector;
    private long mFirstAcquireTimeMs;
    private boolean mShouldLogMetrics;
    private final com.android.server.biometrics.log.BiometricFrameworkStatsLogger mSink;
    private final int mStatsAction;
    private final int mStatsClient;
    private final int mStatsModality;

    public static com.android.server.biometrics.log.BiometricLogger ofUnknown(android.content.Context context) {
        return new com.android.server.biometrics.log.BiometricLogger(context, 0, 0, 0, null);
    }

    public BiometricLogger(android.content.Context context, int statsModality, int statsAction, int statsClient, com.android.server.biometrics.AuthenticationStatsCollector authenticationStatsCollector) {
        this(statsModality, statsAction, statsClient, com.android.server.biometrics.log.BiometricFrameworkStatsLogger.getInstance(), authenticationStatsCollector, (android.hardware.SensorManager) context.getSystemService(android.hardware.SensorManager.class));
    }

    BiometricLogger(int statsModality, int statsAction, int statsClient, com.android.server.biometrics.log.BiometricFrameworkStatsLogger logSink, com.android.server.biometrics.AuthenticationStatsCollector statsCollector, android.hardware.SensorManager sensorManager) {
        this.mShouldLogMetrics = true;
        this.mStatsModality = statsModality;
        this.mStatsAction = statsAction;
        this.mStatsClient = statsClient;
        this.mSink = logSink;
        this.mAuthenticationStatsCollector = statsCollector;
        this.mALSProbe = new com.android.server.biometrics.log.ALSProbe(sensorManager);
    }

    public com.android.server.biometrics.log.BiometricLogger swapAction(android.content.Context context, int statsAction) {
        return new com.android.server.biometrics.log.BiometricLogger(context, this.mStatsModality, statsAction, this.mStatsClient, null);
    }

    public void disableMetrics() {
        this.mShouldLogMetrics = false;
        this.mALSProbe.destroy();
    }

    public int getStatsClient() {
        return this.mStatsClient;
    }

    private boolean shouldSkipLogging() {
        boolean shouldSkipLogging = this.mStatsModality == 0 || this.mStatsAction == 0;
        if (this.mStatsModality == 0) {
            android.util.Slog.w(TAG, "Unknown field detected: MODALITY_UNKNOWN, will not report metric");
        }
        if (this.mStatsAction == 0) {
            android.util.Slog.w(TAG, "Unknown field detected: ACTION_UNKNOWN, will not report metric");
        }
        if (this.mStatsClient == 0) {
            android.util.Slog.w(TAG, "Unknown field detected: CLIENT_UNKNOWN");
        }
        return shouldSkipLogging;
    }

    public void logOnAcquired(android.content.Context context, com.android.server.biometrics.log.OperationContextExt operationContext, int acquiredInfo, int vendorCode, int targetUserId) {
        if (!this.mShouldLogMetrics) {
            return;
        }
        boolean isFace = this.mStatsModality == 4;
        boolean isFingerprint = this.mStatsModality == 1;
        if (isFace || isFingerprint) {
            if ((isFingerprint && acquiredInfo == 7) || (isFace && acquiredInfo == 20)) {
                this.mFirstAcquireTimeMs = java.lang.System.currentTimeMillis();
            }
        } else if (acquiredInfo == 0 && this.mFirstAcquireTimeMs == 0) {
            this.mFirstAcquireTimeMs = java.lang.System.currentTimeMillis();
        }
        android.util.Slog.v(TAG, "Acquired! Modality: " + this.mStatsModality + ", User: " + targetUserId + ", IsCrypto: " + operationContext.isCrypto() + ", Action: " + this.mStatsAction + ", Client: " + this.mStatsClient + ", AcquiredInfo: " + acquiredInfo + ", VendorCode: " + vendorCode);
        if (shouldSkipLogging()) {
            return;
        }
        this.mSink.acquired(operationContext, this.mStatsModality, this.mStatsAction, this.mStatsClient, com.android.server.biometrics.Utils.isDebugEnabled(context, targetUserId), acquiredInfo, vendorCode, targetUserId);
    }

    public void logOnError(android.content.Context context, com.android.server.biometrics.log.OperationContextExt operationContext, int error, int vendorCode, int targetUserId) {
        if (this.mShouldLogMetrics) {
            long latency = this.mFirstAcquireTimeMs != 0 ? java.lang.System.currentTimeMillis() - this.mFirstAcquireTimeMs : -1L;
            android.util.Slog.v(TAG, "Error! Modality: " + this.mStatsModality + ", User: " + targetUserId + ", IsCrypto: " + operationContext.isCrypto() + ", Action: " + this.mStatsAction + ", Client: " + this.mStatsClient + ", Error: " + error + ", VendorCode: " + vendorCode + ", Latency: " + latency);
            if (shouldSkipLogging()) {
                return;
            }
            this.mSink.error(operationContext, this.mStatsModality, this.mStatsAction, this.mStatsClient, com.android.server.biometrics.Utils.isDebugEnabled(context, targetUserId), latency, error, vendorCode, targetUserId);
        }
    }

    public void logOnAuthenticated(android.content.Context context, com.android.server.biometrics.log.OperationContextExt operationContext, boolean authenticated, boolean requireConfirmation, int targetUserId, boolean isBiometricPrompt) {
        int authState;
        long jCurrentTimeMillis;
        if (!this.mShouldLogMetrics) {
            return;
        }
        if (this.mAuthenticationStatsCollector != null) {
            this.mAuthenticationStatsCollector.authenticate(targetUserId, authenticated);
        }
        if (!authenticated) {
            authState = 1;
        } else if (isBiometricPrompt && requireConfirmation) {
            authState = 2;
        } else {
            authState = 3;
        }
        if (this.mFirstAcquireTimeMs != 0) {
            jCurrentTimeMillis = java.lang.System.currentTimeMillis() - this.mFirstAcquireTimeMs;
        } else {
            jCurrentTimeMillis = -1;
        }
        long latency = jCurrentTimeMillis;
        android.util.Slog.v(TAG, "Authenticated! Modality: " + this.mStatsModality + ", User: " + targetUserId + ", IsCrypto: " + operationContext.isCrypto() + ", Client: " + this.mStatsClient + ", RequireConfirmation: " + requireConfirmation + ", State: " + authState + ", Latency: " + latency + ", Lux: " + this.mALSProbe.getMostRecentLux());
        if (shouldSkipLogging()) {
            return;
        }
        this.mSink.authenticate(operationContext, this.mStatsModality, this.mStatsAction, this.mStatsClient, com.android.server.biometrics.Utils.isDebugEnabled(context, targetUserId), latency, authState, requireConfirmation, targetUserId, this.mALSProbe);
    }

    public void logOnEnrolled(int targetUserId, long latency, boolean enrollSuccessful, int source) {
        if (!this.mShouldLogMetrics) {
            return;
        }
        android.util.Slog.v(TAG, "Enrolled! Modality: " + this.mStatsModality + ", User: " + targetUserId + ", Client: " + this.mStatsClient + ", Latency: " + latency + ", Lux: " + this.mALSProbe.getMostRecentLux() + ", Success: " + enrollSuccessful);
        if (shouldSkipLogging()) {
            return;
        }
        this.mSink.enroll(this.mStatsModality, this.mStatsAction, this.mStatsClient, targetUserId, latency, enrollSuccessful, this.mALSProbe.getMostRecentLux(), source);
    }

    public void logUnknownEnrollmentInHal() {
        if (shouldSkipLogging()) {
            return;
        }
        this.mSink.reportUnknownTemplateEnrolledHal(this.mStatsModality);
    }

    public void logUnknownEnrollmentInFramework() {
        if (shouldSkipLogging()) {
            return;
        }
        this.mSink.reportUnknownTemplateEnrolledFramework(this.mStatsModality);
    }

    public com.android.server.biometrics.log.CallbackWithProbe<com.android.server.biometrics.log.Probe> getAmbientLightProbe(boolean startWithClient) {
        return new com.android.server.biometrics.log.CallbackWithProbe<>(this.mALSProbe, startWithClient);
    }
}
