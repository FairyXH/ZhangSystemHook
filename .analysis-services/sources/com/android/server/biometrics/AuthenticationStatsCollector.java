package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class AuthenticationStatsCollector {
    private static final int AUTHENTICATION_UPLOAD_INTERVAL = 50;
    static final int MAXIMUM_ENROLLMENT_NOTIFICATIONS = 1;
    private static final int MINIMUM_ATTEMPTS = 150;
    private static final java.lang.String TAG = "AuthenticationStatsCollector";
    private com.android.server.biometrics.AuthenticationStatsPersister mAuthenticationStatsPersister;
    private com.android.server.biometrics.sensors.BiometricNotification mBiometricNotification;
    private final android.content.Context mContext;
    private final boolean mEnabled;
    private final android.hardware.face.FaceManager mFaceManager;
    private final android.hardware.fingerprint.FingerprintManager mFingerprintManager;
    private final int mModality;
    private final android.content.pm.PackageManager mPackageManager;
    private final float mThreshold;
    private final android.content.BroadcastReceiver mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.biometrics.AuthenticationStatsCollector.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            if (userId != -10000 && intent.getAction().equals("android.intent.action.USER_REMOVED")) {
                android.util.Slog.d(com.android.server.biometrics.AuthenticationStatsCollector.TAG, "Removing data for user: " + userId);
                com.android.server.biometrics.AuthenticationStatsCollector.this.onUserRemoved(userId);
            }
        }
    };
    private final java.util.Map<java.lang.Integer, com.android.server.biometrics.AuthenticationStats> mUserAuthenticationStatsMap = new java.util.HashMap();

    public AuthenticationStatsCollector(android.content.Context context, int modality, com.android.server.biometrics.sensors.BiometricNotification biometricNotification) {
        this.mContext = context;
        this.mEnabled = context.getResources().getBoolean(android.R.bool.config_bg_current_drain_monitor_enabled);
        this.mThreshold = context.getResources().getFraction(android.R.fraction.config_biometricNotificationFrrThreshold, 1, 1);
        this.mModality = modality;
        this.mBiometricNotification = biometricNotification;
        this.mPackageManager = context.getPackageManager();
        this.mFaceManager = (android.hardware.face.FaceManager) this.mContext.getSystemService(android.hardware.face.FaceManager.class);
        this.mFingerprintManager = (android.hardware.fingerprint.FingerprintManager) this.mContext.getSystemService(android.hardware.fingerprint.FingerprintManager.class);
        this.mAuthenticationStatsPersister = new com.android.server.biometrics.AuthenticationStatsPersister(this.mContext);
        initializeUserAuthenticationStatsMap();
        this.mAuthenticationStatsPersister.persistFrrThreshold(this.mThreshold);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        context.registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    private void initializeUserAuthenticationStatsMap() {
        for (com.android.server.biometrics.AuthenticationStats stats : this.mAuthenticationStatsPersister.getAllFrrStats(this.mModality)) {
            this.mUserAuthenticationStatsMap.put(java.lang.Integer.valueOf(stats.getUserId()), stats);
        }
    }

    public void authenticate(int userId, boolean authenticated) {
        if (this.mEnabled && !isSingleModalityDevice()) {
            if (hasEnrolledFace(userId) && hasEnrolledFingerprint(userId)) {
                return;
            }
            if (this.mUserAuthenticationStatsMap.isEmpty()) {
                initializeUserAuthenticationStatsMap();
            }
            if (!this.mUserAuthenticationStatsMap.containsKey(java.lang.Integer.valueOf(userId))) {
                this.mUserAuthenticationStatsMap.put(java.lang.Integer.valueOf(userId), new com.android.server.biometrics.AuthenticationStats(userId, this.mModality));
            }
            com.android.server.biometrics.AuthenticationStats authenticationStats = this.mUserAuthenticationStatsMap.get(java.lang.Integer.valueOf(userId));
            if (authenticationStats.getEnrollmentNotifications() >= 1) {
                return;
            }
            authenticationStats.authenticate(authenticated);
            sendNotificationIfNeeded(userId);
            persistDataIfNeeded(userId);
        }
    }

    private void sendNotificationIfNeeded(int userId) {
        com.android.server.biometrics.AuthenticationStats authenticationStats = this.mUserAuthenticationStatsMap.get(java.lang.Integer.valueOf(userId));
        if (authenticationStats.getTotalAttempts() < 150) {
            return;
        }
        if (authenticationStats.getEnrollmentNotifications() >= 1 || authenticationStats.getFrr() < this.mThreshold) {
            authenticationStats.resetData();
            return;
        }
        authenticationStats.resetData();
        boolean hasEnrolledFace = hasEnrolledFace(userId);
        boolean hasEnrolledFingerprint = hasEnrolledFingerprint(userId);
        if (hasEnrolledFace && !hasEnrolledFingerprint) {
            this.mBiometricNotification.sendFpEnrollNotification(this.mContext);
            authenticationStats.updateNotificationCounter();
        } else if (!hasEnrolledFace && hasEnrolledFingerprint) {
            this.mBiometricNotification.sendFaceEnrollNotification(this.mContext);
            authenticationStats.updateNotificationCounter();
        }
    }

    private void persistDataIfNeeded(int userId) {
        com.android.server.biometrics.AuthenticationStats authenticationStats = this.mUserAuthenticationStatsMap.get(java.lang.Integer.valueOf(userId));
        if (authenticationStats.getTotalAttempts() % 50 == 0) {
            this.mAuthenticationStatsPersister.persistFrrStats(authenticationStats.getUserId(), authenticationStats.getTotalAttempts(), authenticationStats.getRejectedAttempts(), authenticationStats.getEnrollmentNotifications(), authenticationStats.getModality());
        }
    }

    public void sendFaceReEnrollNotification() {
        this.mBiometricNotification.sendFaceEnrollNotification(this.mContext);
    }

    public void sendFingerprintReEnrollNotification() {
        this.mBiometricNotification.sendFpEnrollNotification(this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserRemoved(int userId) {
        this.mUserAuthenticationStatsMap.remove(java.lang.Integer.valueOf(userId));
        this.mAuthenticationStatsPersister.removeFrrStats(userId);
    }

    private boolean isSingleModalityDevice() {
        return (this.mPackageManager.hasSystemFeature("android.hardware.fingerprint") && this.mPackageManager.hasSystemFeature("android.hardware.biometrics.face")) ? false : true;
    }

    private boolean hasEnrolledFace(int userId) {
        return this.mFaceManager != null && this.mFaceManager.hasEnrolledTemplates(userId);
    }

    private boolean hasEnrolledFingerprint(int userId) {
        return this.mFingerprintManager != null && this.mFingerprintManager.hasEnrolledTemplates(userId);
    }

    com.android.server.biometrics.AuthenticationStats getAuthenticationStatsForUser(int userId) {
        return this.mUserAuthenticationStatsMap.getOrDefault(java.lang.Integer.valueOf(userId), null);
    }

    void setAuthenticationStatsForUser(int userId, com.android.server.biometrics.AuthenticationStats authenticationStats) {
        this.mUserAuthenticationStatsMap.put(java.lang.Integer.valueOf(userId), authenticationStats);
    }
}
