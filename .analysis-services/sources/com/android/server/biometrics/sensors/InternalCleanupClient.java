package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class InternalCleanupClient<S extends android.hardware.biometrics.BiometricAuthenticator.Identifier, T> extends com.android.server.biometrics.sensors.HalClientMonitor<T> implements com.android.server.biometrics.sensors.EnumerateConsumer, com.android.server.biometrics.sensors.RemovalConsumer, com.android.server.biometrics.sensors.EnrollmentModifier {
    private static final java.lang.String TAG = "Biometrics/InternalCleanupClient";
    private final java.util.Map<java.lang.Integer, java.lang.Long> mAuthenticatorIds;
    private final com.android.server.biometrics.sensors.BiometricUtils<S> mBiometricUtils;
    private com.android.server.biometrics.sensors.BaseClientMonitor mCurrentTask;
    private final com.android.server.biometrics.sensors.ClientMonitorCallback mEnumerateCallback;
    private boolean mFavorHalEnrollments;
    private final boolean mHasEnrollmentsBeforeStarting;
    private final com.android.server.biometrics.sensors.ClientMonitorCallback mRemoveCallback;
    private final java.util.ArrayList<com.android.server.biometrics.sensors.InternalCleanupClient.UserTemplate> mUnknownHALTemplates;

    protected abstract com.android.server.biometrics.sensors.InternalEnumerateClient<T> getEnumerateClient(android.content.Context context, java.util.function.Supplier<T> supplier, android.os.IBinder iBinder, int i, java.lang.String str, java.util.List<S> list, com.android.server.biometrics.sensors.BiometricUtils<S> biometricUtils, int i2, com.android.server.biometrics.log.BiometricLogger biometricLogger, com.android.server.biometrics.log.BiometricContext biometricContext);

    protected abstract com.android.server.biometrics.sensors.RemovalClient<S, T> getRemovalClient(android.content.Context context, java.util.function.Supplier<T> supplier, android.os.IBinder iBinder, int i, int i2, java.lang.String str, com.android.server.biometrics.sensors.BiometricUtils<S> biometricUtils, int i3, com.android.server.biometrics.log.BiometricLogger biometricLogger, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.Map<java.lang.Integer, java.lang.Long> map);

    private static final class UserTemplate {
        final android.hardware.biometrics.BiometricAuthenticator.Identifier mIdentifier;
        final int mUserId;

        UserTemplate(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, int userId) {
            this.mIdentifier = identifier;
            this.mUserId = userId;
        }
    }

    protected InternalCleanupClient(android.content.Context context, java.util.function.Supplier<T> lazyDaemon, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.server.biometrics.sensors.BiometricUtils<S> utils, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds) {
        super(context, lazyDaemon, null, null, userId, owner, 0, sensorId, logger, biometricContext);
        this.mUnknownHALTemplates = new java.util.ArrayList<>();
        this.mFavorHalEnrollments = false;
        this.mEnumerateCallback = new com.android.server.biometrics.sensors.ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.InternalCleanupClient.1
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
                java.util.List<android.hardware.biometrics.BiometricAuthenticator.Identifier> unknownHALTemplates = ((com.android.server.biometrics.sensors.InternalEnumerateClient) com.android.server.biometrics.sensors.InternalCleanupClient.this.mCurrentTask).getUnknownHALTemplates();
                android.util.Slog.d(com.android.server.biometrics.sensors.InternalCleanupClient.TAG, "Enumerate onClientFinished: " + clientMonitor + ", success: " + success);
                if (!unknownHALTemplates.isEmpty()) {
                    android.util.Slog.w(com.android.server.biometrics.sensors.InternalCleanupClient.TAG, "Adding " + unknownHALTemplates.size() + " templates for deletion");
                }
                for (android.hardware.biometrics.BiometricAuthenticator.Identifier unknownHALTemplate : unknownHALTemplates) {
                    com.android.server.biometrics.sensors.InternalCleanupClient.this.mUnknownHALTemplates.add(new com.android.server.biometrics.sensors.InternalCleanupClient.UserTemplate(unknownHALTemplate, com.android.server.biometrics.sensors.InternalCleanupClient.this.mCurrentTask.getTargetUserId()));
                }
                if (!com.android.server.biometrics.sensors.InternalCleanupClient.this.mUnknownHALTemplates.isEmpty()) {
                    if (com.android.server.biometrics.sensors.InternalCleanupClient.this.mFavorHalEnrollments && android.os.Build.isDebuggable()) {
                        try {
                            for (com.android.server.biometrics.sensors.InternalCleanupClient.UserTemplate template : com.android.server.biometrics.sensors.InternalCleanupClient.this.mUnknownHALTemplates) {
                                android.util.Slog.i(com.android.server.biometrics.sensors.InternalCleanupClient.TAG, "Adding unknown HAL template: " + template.mIdentifier.getBiometricId());
                                com.android.server.biometrics.sensors.InternalCleanupClient.this.onAddUnknownTemplate(template.mUserId, template.mIdentifier);
                            }
                            return;
                        } finally {
                            com.android.server.biometrics.sensors.InternalCleanupClient.this.mCallback.onClientFinished(com.android.server.biometrics.sensors.InternalCleanupClient.this, success);
                        }
                    }
                    com.android.server.biometrics.sensors.InternalCleanupClient.this.startCleanupUnknownHalTemplates();
                }
            }
        };
        this.mRemoveCallback = new com.android.server.biometrics.sensors.ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.InternalCleanupClient.2
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
                if (com.android.server.biometrics.sensors.InternalCleanupClient.this.mUnknownHALTemplates != null && com.android.server.biometrics.sensors.InternalCleanupClient.this.mUnknownHALTemplates.size() != 0) {
                    android.util.Slog.d(com.android.server.biometrics.sensors.InternalCleanupClient.TAG, "next mUnknownHALTemplates  size: " + com.android.server.biometrics.sensors.InternalCleanupClient.this.mUnknownHALTemplates.size());
                    com.android.server.biometrics.sensors.InternalCleanupClient.UserTemplate template = (com.android.server.biometrics.sensors.InternalCleanupClient.UserTemplate) com.android.server.biometrics.sensors.InternalCleanupClient.this.mUnknownHALTemplates.get(0);
                    com.android.server.biometrics.sensors.InternalCleanupClient.this.mUnknownHALTemplates.remove(template);
                    com.android.server.biometrics.sensors.InternalCleanupClient.this.mCurrentTask = com.android.server.biometrics.sensors.InternalCleanupClient.this.getRemovalClient(com.android.server.biometrics.sensors.InternalCleanupClient.this.getContext(), com.android.server.biometrics.sensors.InternalCleanupClient.this.mLazyDaemon, com.android.server.biometrics.sensors.InternalCleanupClient.this.getToken(), template.mIdentifier.getBiometricId(), template.mUserId, com.android.server.biometrics.sensors.InternalCleanupClient.this.getContext().getPackageName(), com.android.server.biometrics.sensors.InternalCleanupClient.this.mBiometricUtils, com.android.server.biometrics.sensors.InternalCleanupClient.this.getSensorId(), com.android.server.biometrics.sensors.InternalCleanupClient.this.getLogger(), com.android.server.biometrics.sensors.InternalCleanupClient.this.getBiometricContext(), com.android.server.biometrics.sensors.InternalCleanupClient.this.mAuthenticatorIds);
                    com.android.server.biometrics.sensors.InternalCleanupClient.this.mCurrentTask.start(com.android.server.biometrics.sensors.InternalCleanupClient.this.mRemoveCallback);
                    return;
                }
                android.util.Slog.d(com.android.server.biometrics.sensors.InternalCleanupClient.TAG, "Remove onClientFinished: " + clientMonitor + ", success: " + success);
                if (com.android.server.biometrics.sensors.InternalCleanupClient.this.mUnknownHALTemplates.isEmpty()) {
                    com.android.server.biometrics.sensors.InternalCleanupClient.this.mCallback.onClientFinished(com.android.server.biometrics.sensors.InternalCleanupClient.this, success);
                } else {
                    com.android.server.biometrics.sensors.InternalCleanupClient.this.startCleanupUnknownHalTemplates();
                }
            }
        };
        this.mBiometricUtils = utils;
        this.mAuthenticatorIds = authenticatorIds;
        this.mHasEnrollmentsBeforeStarting = !utils.getBiometricsForUser(context, userId).isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startCleanupUnknownHalTemplates() {
        android.util.Slog.d(TAG, "startCleanupUnknownHalTemplates, size: " + this.mUnknownHALTemplates.size());
        com.android.server.biometrics.sensors.InternalCleanupClient.UserTemplate template = this.mUnknownHALTemplates.get(0);
        this.mUnknownHALTemplates.remove(template);
        this.mCurrentTask = getRemovalClient(getContext(), this.mLazyDaemon, getToken(), template.mIdentifier.getBiometricId(), template.mUserId, getContext().getPackageName(), this.mBiometricUtils, getSensorId(), getLogger(), getBiometricContext(), this.mAuthenticatorIds);
        getLogger().logUnknownEnrollmentInHal();
        this.mCurrentTask.start(this.mRemoveCallback);
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        java.util.List<S> enrolledList = this.mBiometricUtils.getBiometricsForUser(getContext(), getTargetUserId());
        this.mCurrentTask = getEnumerateClient(getContext(), this.mLazyDaemon, getToken(), getTargetUserId(), getOwnerString(), enrolledList, this.mBiometricUtils, getSensorId(), getLogger(), getBiometricContext());
        android.util.Slog.d(TAG, "Starting enumerate: " + this.mCurrentTask + " enrolledList size:" + enrolledList.size());
        this.mCurrentTask.start(this.mEnumerateCallback);
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    protected void startHalOperation() {
    }

    @Override // com.android.server.biometrics.sensors.RemovalConsumer
    public void onRemoved(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, int remaining) {
        if (!(this.mCurrentTask instanceof com.android.server.biometrics.sensors.RemovalClient)) {
            android.util.Slog.e(TAG, "onRemoved received during client: " + this.mCurrentTask.getClass().getSimpleName());
        } else {
            ((com.android.server.biometrics.sensors.RemovalClient) this.mCurrentTask).onRemoved(identifier, remaining);
        }
    }

    @Override // com.android.server.biometrics.sensors.EnrollmentModifier
    public boolean hasEnrollmentStateChanged() {
        boolean hasEnrollmentsNow = !this.mBiometricUtils.getBiometricsForUser(getContext(), getTargetUserId()).isEmpty();
        return hasEnrollmentsNow != this.mHasEnrollmentsBeforeStarting;
    }

    @Override // com.android.server.biometrics.sensors.EnrollmentModifier
    public boolean hasEnrollments() {
        return !this.mBiometricUtils.getBiometricsForUser(getContext(), getTargetUserId()).isEmpty();
    }

    @Override // com.android.server.biometrics.sensors.EnumerateConsumer
    public void onEnumerationResult(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, int remaining) {
        if (!(this.mCurrentTask instanceof com.android.server.biometrics.sensors.InternalEnumerateClient)) {
            android.util.Slog.e(TAG, "onEnumerationResult received during client: " + this.mCurrentTask.getClass().getSimpleName());
        } else {
            android.util.Slog.d(TAG, "onEnumerated, remaining: " + remaining);
            ((com.android.server.biometrics.sensors.EnumerateConsumer) this.mCurrentTask).onEnumerationResult(identifier, remaining);
        }
    }

    public void setFavorHalEnrollments() {
        this.mFavorHalEnrollments = true;
    }

    protected void onAddUnknownTemplate(int userId, android.hardware.biometrics.BiometricAuthenticator.Identifier identifier) {
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 7;
    }

    public com.android.server.biometrics.sensors.InternalEnumerateClient<T> getCurrentEnumerateClient() {
        return (com.android.server.biometrics.sensors.InternalEnumerateClient) this.mCurrentTask;
    }

    public com.android.server.biometrics.sensors.RemovalClient<S, T> getCurrentRemoveClient() {
        return (com.android.server.biometrics.sensors.RemovalClient) this.mCurrentTask;
    }

    public java.util.ArrayList<com.android.server.biometrics.sensors.InternalCleanupClient.UserTemplate> getUnknownHALTemplates() {
        return this.mUnknownHALTemplates;
    }
}
