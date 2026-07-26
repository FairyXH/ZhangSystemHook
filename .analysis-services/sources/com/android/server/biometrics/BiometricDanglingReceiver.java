package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class BiometricDanglingReceiver extends android.content.BroadcastReceiver {
    public static final java.lang.String ACTION_FACE_RE_ENROLL_DISMISS = "action_face_re_enroll_dismiss";
    public static final java.lang.String ACTION_FACE_RE_ENROLL_LAUNCH = "action_face_re_enroll_launch";
    public static final java.lang.String ACTION_FINGERPRINT_RE_ENROLL_DISMISS = "action_fingerprint_re_enroll_dismiss";
    public static final java.lang.String ACTION_FINGERPRINT_RE_ENROLL_LAUNCH = "action_fingerprint_re_enroll_launch";
    public static final java.lang.String FACE_SETTINGS_ACTION = "android.settings.FACE_SETTINGS";
    private static final java.lang.String SETTINGS_PACKAGE = "com.android.settings";
    private static final java.lang.String TAG = "BiometricDanglingReceiver";

    public BiometricDanglingReceiver(android.content.Context context, int modality) {
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        if (modality == 1) {
            intentFilter.addAction(ACTION_FINGERPRINT_RE_ENROLL_LAUNCH);
            intentFilter.addAction(ACTION_FINGERPRINT_RE_ENROLL_DISMISS);
        } else if (modality == 4) {
            intentFilter.addAction(ACTION_FACE_RE_ENROLL_LAUNCH);
            intentFilter.addAction(ACTION_FACE_RE_ENROLL_DISMISS);
        }
        context.registerReceiver(this, intentFilter, 4);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        android.util.Slog.d(TAG, "Received: " + intent.getAction());
        if (ACTION_FINGERPRINT_RE_ENROLL_LAUNCH.equals(intent.getAction())) {
            launchBiometricEnrollActivity(context, "android.settings.FINGERPRINT_ENROLL");
            com.android.server.biometrics.sensors.BiometricNotificationUtils.cancelFingerprintReEnrollNotification(context);
        } else if (ACTION_FINGERPRINT_RE_ENROLL_DISMISS.equals(intent.getAction())) {
            com.android.server.biometrics.sensors.BiometricNotificationUtils.cancelFingerprintReEnrollNotification(context);
        } else if (ACTION_FACE_RE_ENROLL_LAUNCH.equals(intent.getAction())) {
            launchBiometricEnrollActivity(context, FACE_SETTINGS_ACTION);
            com.android.server.biometrics.sensors.BiometricNotificationUtils.cancelFaceReEnrollNotification(context);
        } else if (ACTION_FACE_RE_ENROLL_DISMISS.equals(intent.getAction())) {
            com.android.server.biometrics.sensors.BiometricNotificationUtils.cancelFaceReEnrollNotification(context);
        }
        context.unregisterReceiver(this);
    }

    private void launchBiometricEnrollActivity(android.content.Context context, java.lang.String action) {
        context.sendBroadcast(new android.content.Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS").setFlags(268435456));
        android.content.Intent intent = new android.content.Intent(action);
        intent.setPackage(SETTINGS_PACKAGE);
        intent.setFlags(268435456);
        context.startActivity(intent);
    }
}
