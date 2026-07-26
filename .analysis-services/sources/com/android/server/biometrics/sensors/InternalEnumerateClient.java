package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class InternalEnumerateClient<T> extends com.android.server.biometrics.sensors.HalClientMonitor<T> implements com.android.server.biometrics.sensors.EnumerateConsumer {
    private static final java.lang.String TAG = "Biometrics/InternalEnumerateClient";
    private java.util.List<? extends android.hardware.biometrics.BiometricAuthenticator.Identifier> mEnrolledList;
    private final int mInitialEnrolledSize;
    private java.util.List<android.hardware.biometrics.BiometricAuthenticator.Identifier> mUnknownHALTemplates;
    private com.android.server.biometrics.sensors.BiometricUtils mUtils;

    protected abstract int getModality();

    protected InternalEnumerateClient(android.content.Context context, java.util.function.Supplier<T> lazyDaemon, android.os.IBinder token, int userId, java.lang.String owner, java.util.List<? extends android.hardware.biometrics.BiometricAuthenticator.Identifier> enrolledList, com.android.server.biometrics.sensors.BiometricUtils utils, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext) {
        super(context, lazyDaemon, token, null, userId, owner, 0, sensorId, logger, biometricContext);
        this.mUnknownHALTemplates = new java.util.ArrayList();
        this.mEnrolledList = enrolledList;
        this.mInitialEnrolledSize = this.mEnrolledList.size();
        this.mUtils = utils;
    }

    @Override // com.android.server.biometrics.sensors.EnumerateConsumer
    public void onEnumerationResult(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, int remaining) {
        handleEnumeratedTemplate(identifier);
        if (remaining == 0) {
            doTemplateCleanup();
            this.mCallback.onClientFinished(this, true);
        }
    }

    @Override // com.android.server.biometrics.sensors.HalClientMonitor
    public void unableToStart() {
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        super.start(callback);
        startHalOperation();
    }

    private void handleEnumeratedTemplate(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier) {
        if (identifier == null) {
            android.util.Slog.d(TAG, "Null identifier");
            return;
        }
        android.util.Slog.v(TAG, "handleEnumeratedTemplate: " + identifier.getBiometricId());
        boolean matched = false;
        int i = 0;
        while (true) {
            if (i >= this.mEnrolledList.size()) {
                break;
            }
            if (this.mEnrolledList.get(i).getBiometricId() != identifier.getBiometricId()) {
                i++;
            } else {
                this.mEnrolledList.remove(i);
                matched = true;
                break;
            }
        }
        if (!matched && identifier.getBiometricId() != 0) {
            this.mUnknownHALTemplates.add(identifier);
        }
        android.util.Slog.v(TAG, "Matched: " + matched);
    }

    private void doTemplateCleanup() {
        if (this.mEnrolledList == null) {
            android.util.Slog.d(TAG, "Null enrolledList");
            return;
        }
        java.util.List<java.lang.String> names = new java.util.ArrayList<>();
        for (int i = 0; i < this.mEnrolledList.size(); i++) {
            android.hardware.biometrics.BiometricAuthenticator.Identifier identifier = this.mEnrolledList.get(i);
            names.add(identifier.getName().toString());
            android.util.Slog.e(TAG, "doTemplateCleanup(): Removing dangling template from framework: " + identifier.getBiometricId() + " " + ((java.lang.Object) identifier.getName()));
            this.mUtils.removeBiometricForUser(getContext(), getTargetUserId(), identifier.getBiometricId());
            getLogger().logUnknownEnrollmentInFramework();
        }
        if (!names.isEmpty()) {
            sendDanglingNotification(names);
        }
        this.mEnrolledList.clear();
    }

    public java.util.List<android.hardware.biometrics.BiometricAuthenticator.Identifier> getUnknownHALTemplates() {
        return this.mUnknownHALTemplates;
    }

    public void sendDanglingNotification(java.util.List<java.lang.String> identifierNames) {
        if (!identifierNames.isEmpty()) {
            android.util.Slog.e(TAG, "sendDanglingNotification(): initial enrolledSize=" + this.mInitialEnrolledSize + ", after clean up size=" + this.mEnrolledList.size());
            boolean allIdentifiersDeleted = this.mEnrolledList.size() == this.mInitialEnrolledSize;
            com.android.server.biometrics.sensors.BiometricNotificationUtils.showBiometricReEnrollNotification(getContext(), identifierNames, allIdentifiersDeleted, getModality());
        }
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 6;
    }
}
