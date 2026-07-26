package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class BiometricNotificationUtils {
    private static final java.lang.String BAD_CALIBRATION_NOTIFICATION_TAG = "FingerprintBadCalibration";
    private static final java.lang.String FACE_ENROLL_ACTION = "android.settings.FACE_ENROLL";
    private static final java.lang.String FACE_ENROLL_CHANNEL = "FaceEnrollNotificationChannel";
    public static final java.lang.String FACE_ENROLL_NOTIFICATION_TAG = "FaceEnroll";
    private static final java.lang.String FACE_RE_ENROLL_CHANNEL = "FaceReEnrollNotificationChannel";
    private static final java.lang.String FACE_RE_ENROLL_NOTIFICATION_TAG = "FaceReEnroll";
    private static final java.lang.String FACE_SETTINGS_ACTION = "android.settings.FACE_SETTINGS";
    private static final java.lang.String FINGERPRINT_BAD_CALIBRATION_CHANNEL = "FingerprintBadCalibrationNotificationChannel";
    private static final java.lang.String FINGERPRINT_ENROLL_ACTION = "android.settings.FINGERPRINT_ENROLL";
    private static final java.lang.String FINGERPRINT_ENROLL_CHANNEL = "FingerprintEnrollNotificationChannel";
    public static final java.lang.String FINGERPRINT_ENROLL_NOTIFICATION_TAG = "FingerprintEnroll";
    private static final java.lang.String FINGERPRINT_RE_ENROLL_CHANNEL = "FingerprintReEnrollNotificationChannel";
    private static final java.lang.String FINGERPRINT_RE_ENROLL_NOTIFICATION_TAG = "FingerprintReEnroll";
    private static final java.lang.String FINGERPRINT_SETTINGS_ACTION = "android.settings.FINGERPRINT_SETTINGS";
    private static final java.lang.String KEY_RE_ENROLL_FACE = "re_enroll_face_unlock";
    public static final int NOTIFICATION_ID = 1;
    private static final long NOTIFICATION_INTERVAL_MS = 86400000;
    private static final java.lang.String SETTINGS_PACKAGE = "com.android.settings";
    private static final java.lang.String TAG = "BiometricNotificationUtils";
    private static long sLastAlertTime = 0;
    private static final java.lang.String ACTION_BIOMETRIC_FRR_DISMISS = "action_biometric_frr_dismiss";
    private static final android.content.Intent DISMISS_FRR_INTENT = new android.content.Intent(ACTION_BIOMETRIC_FRR_DISMISS);

    public static void showReEnrollmentNotification(android.content.Context context) {
        java.lang.String name = context.getString(android.R.string.face_sensor_privacy_enabled);
        java.lang.String title = context.getString(android.R.string.faceunlock_multiple_failures);
        java.lang.String content = context.getString(android.R.string.face_recalibrate_notification_title);
        android.content.Intent intent = new android.content.Intent("android.settings.FACE_SETTINGS");
        intent.setPackage(SETTINGS_PACKAGE);
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getActivityAsUser(context, 0, intent, 67108864, null, android.os.UserHandle.CURRENT);
        showNotificationHelper(context, name, title, content, pendingIntent, FACE_RE_ENROLL_CHANNEL, "sys", FACE_RE_ENROLL_NOTIFICATION_TAG, -1, false);
    }

    public static void showFaceEnrollNotification(android.content.Context context) {
        android.util.Slog.d(TAG, "Showing Face Enroll Notification");
        java.lang.String name = context.getString(android.R.string.display_rotation_camera_compat_toast_in_multi_window);
        java.lang.String title = context.getString(android.R.string.aerr_process_repeated);
        java.lang.String content = context.getString(android.R.string.aerr_mute);
        android.content.Intent intent = new android.content.Intent(FACE_ENROLL_ACTION);
        intent.setPackage(SETTINGS_PACKAGE);
        intent.putExtra("enroll_reason", 1);
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getActivityAsUser(context, 0, intent, 67108864, null, android.os.UserHandle.CURRENT);
        showNotificationHelper(context, name, title, content, pendingIntent, FACE_ENROLL_CHANNEL, "recommendation", FACE_ENROLL_NOTIFICATION_TAG, 1, true);
    }

    public static void showFingerprintEnrollNotification(android.content.Context context) {
        android.util.Slog.d(TAG, "Showing Fingerprint Enroll Notification");
        java.lang.String name = context.getString(android.R.string.display_rotation_camera_compat_toast_in_multi_window);
        java.lang.String title = context.getString(android.R.string.aerr_process_repeated);
        java.lang.String content = context.getString(android.R.string.aerr_process);
        android.content.Intent intent = new android.content.Intent(FINGERPRINT_ENROLL_ACTION);
        intent.setPackage(SETTINGS_PACKAGE);
        intent.putExtra("enroll_reason", 1);
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getActivityAsUser(context, 0, intent, 67108864, null, android.os.UserHandle.CURRENT);
        showNotificationHelper(context, name, title, content, pendingIntent, "recommendation", FINGERPRINT_ENROLL_CHANNEL, FINGERPRINT_ENROLL_NOTIFICATION_TAG, 1, true);
    }

    public static void showBadCalibrationNotification(android.content.Context context) {
        long currentTime = android.os.SystemClock.elapsedRealtime();
        long timeSinceLastAlert = currentTime - sLastAlertTime;
        if (sLastAlertTime != 0 && timeSinceLastAlert < 86400000) {
            android.util.Slog.v(TAG, "Skipping calibration notification : " + timeSinceLastAlert);
            return;
        }
        sLastAlertTime = currentTime;
        java.lang.String name = context.getString(android.R.string.fingerprint_or_screen_lock_dialog_default_subtitle);
        java.lang.String title = context.getString(android.R.string.fingerprint_recalibrate_notification_content);
        java.lang.String content = context.getString(android.R.string.fingerprint_or_screen_lock_app_setting_name);
        android.content.Intent intent = new android.content.Intent(FINGERPRINT_SETTINGS_ACTION);
        intent.setPackage(SETTINGS_PACKAGE);
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getActivityAsUser(context, 0, intent, 67108864, null, android.os.UserHandle.CURRENT);
        showNotificationHelper(context, name, title, content, pendingIntent, "sys", FINGERPRINT_BAD_CALIBRATION_CHANNEL, BAD_CALIBRATION_NOTIFICATION_TAG, -1, false);
    }

    public static void showBiometricReEnrollNotification(android.content.Context context, java.util.List<java.lang.String> identifiers, boolean allIdentifiersDeleted, int modality) {
        java.lang.String str;
        int i;
        java.lang.String content;
        java.lang.String tag;
        boolean isFingerprint = modality == 1;
        if (isFingerprint) {
            str = FINGERPRINT_RE_ENROLL_NOTIFICATION_TAG;
        } else {
            str = FACE_RE_ENROLL_NOTIFICATION_TAG;
        }
        java.lang.String reEnrollName = str;
        if (identifiers.isEmpty()) {
            android.util.Slog.v(TAG, "Skipping " + reEnrollName + " notification : empty list");
            return;
        }
        android.util.Slog.d(TAG, "Showing " + reEnrollName + " notification :[" + identifiers.size() + " identifier(s) deleted, allIdentifiersDeleted=" + allIdentifiersDeleted + "]");
        java.lang.String name = context.getString(android.R.string.display_rotation_camera_compat_toast_in_multi_window);
        if (isFingerprint) {
            i = android.R.string.fingerprint_dialog_default_subtitle;
        } else {
            i = android.R.string.face_error_canceled;
        }
        java.lang.String title = context.getString(i);
        if (!isFingerprint) {
            content = context.getString(android.R.string.face_dialog_default_subtitle);
        } else {
            content = getFingerprintDanglingContentString(context, identifiers, allIdentifiersDeleted);
        }
        android.content.Intent setupIntent = new android.content.Intent(isFingerprint ? com.android.server.biometrics.BiometricDanglingReceiver.ACTION_FINGERPRINT_RE_ENROLL_LAUNCH : com.android.server.biometrics.BiometricDanglingReceiver.ACTION_FACE_RE_ENROLL_LAUNCH);
        android.app.PendingIntent setupPendingIntent = android.app.PendingIntent.getBroadcastAsUser(context, 0, setupIntent, 67108864, android.os.UserHandle.CURRENT);
        java.lang.String setupText = context.getString(android.R.string.biometric_error_generic);
        android.app.Notification.Action setupAction = new android.app.Notification.Action.Builder((android.graphics.drawable.Icon) null, setupText, setupPendingIntent).build();
        android.content.Intent notNowIntent = new android.content.Intent(isFingerprint ? com.android.server.biometrics.BiometricDanglingReceiver.ACTION_FINGERPRINT_RE_ENROLL_DISMISS : com.android.server.biometrics.BiometricDanglingReceiver.ACTION_FACE_RE_ENROLL_DISMISS);
        android.app.PendingIntent notNowPendingIntent = android.app.PendingIntent.getBroadcastAsUser(context, 0, notNowIntent, 67108864, android.os.UserHandle.CURRENT);
        java.lang.String notNowText = context.getString(android.R.string.biometric_error_device_not_secured);
        android.app.Notification.Action notNowAction = new android.app.Notification.Action.Builder((android.graphics.drawable.Icon) null, notNowText, notNowPendingIntent).build();
        java.lang.String channel = isFingerprint ? FINGERPRINT_RE_ENROLL_CHANNEL : FACE_RE_ENROLL_CHANNEL;
        if (isFingerprint) {
            tag = FINGERPRINT_RE_ENROLL_NOTIFICATION_TAG;
        } else {
            tag = FACE_RE_ENROLL_NOTIFICATION_TAG;
        }
        showNotificationHelper(context, name, title, content, setupPendingIntent, setupAction, notNowAction, "sys", channel, tag, -1, false, 32);
    }

    private static java.lang.String getFingerprintDanglingContentString(android.content.Context context, java.util.List<java.lang.String> fingerprints, boolean allFingerprintDeleted) {
        int resId;
        int resId2;
        if (fingerprints.isEmpty()) {
            return null;
        }
        int size = fingerprints.size();
        java.lang.StringBuilder first = new java.lang.StringBuilder();
        android.text.BidiFormatter bidiFormatter = android.text.BidiFormatter.getInstance();
        if (size > 1) {
            java.lang.String second = null;
            for (int i = 0; i < size; i++) {
                if (i == size - 1) {
                    second = bidiFormatter.unicodeWrap("\"" + fingerprints.get(i) + "\"");
                } else {
                    first.append(bidiFormatter.unicodeWrap("\""));
                    first.append(bidiFormatter.unicodeWrap(fingerprints.get(i)));
                    first.append(bidiFormatter.unicodeWrap("\""));
                    if (i < size - 2) {
                        first.append(bidiFormatter.unicodeWrap(", "));
                    }
                }
            }
            if (allFingerprintDeleted) {
                resId2 = android.R.string.fingerprint_dangling_notification_title;
            } else {
                resId2 = android.R.string.fingerprint_dangling_notification_msg_all_deleted_1;
            }
            return java.lang.String.format(context.getString(resId2), first, second);
        }
        if (allFingerprintDeleted) {
            resId = android.R.string.fingerprint_dangling_notification_msg_all_deleted_2;
        } else {
            resId = android.R.string.fingerprint_dangling_notification_msg_2;
        }
        first.append(bidiFormatter.unicodeWrap("\""));
        first.append(bidiFormatter.unicodeWrap(fingerprints.get(0)));
        first.append(bidiFormatter.unicodeWrap("\""));
        return java.lang.String.format(context.getString(resId), first);
    }

    private static void showNotificationHelper(android.content.Context context, java.lang.String name, java.lang.String title, java.lang.String content, android.app.PendingIntent pendingIntent, java.lang.String category, java.lang.String channelName, java.lang.String notificationTag, int visibility, boolean listenToDismissEvent) {
        showNotificationHelper(context, name, title, content, pendingIntent, null, null, category, channelName, notificationTag, visibility, listenToDismissEvent, 0);
    }

    private static void showNotificationHelper(android.content.Context context, java.lang.String name, java.lang.String title, java.lang.String content, android.app.PendingIntent pendingIntent, android.app.Notification.Action positiveAction, android.app.Notification.Action negativeAction, java.lang.String category, java.lang.String channelName, java.lang.String notificationTag, int visibility, boolean listenToDismissEvent, int flags) {
        android.util.Slog.v(TAG, " listenToDismissEvent = " + listenToDismissEvent);
        android.app.PendingIntent dismissIntent = android.app.PendingIntent.getActivityAsUser(context, 0, DISMISS_FRR_INTENT, 67108864, null, android.os.UserHandle.CURRENT);
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class);
        android.app.NotificationChannel channel = new android.app.NotificationChannel(channelName, name, 4);
        android.app.Notification.Builder builder = new android.app.Notification.Builder(context, channelName).setSmallIcon(android.R.drawable.ic_doc_powerpoint).setContentTitle(title).setContentText(content).setStyle(new android.app.Notification.BigTextStyle().bigText(content)).setSubText(name).setOnlyAlertOnce(true).setLocalOnly(true).setAutoCancel(true).setCategory(category).setContentIntent(pendingIntent).setVisibility(visibility);
        if (flags > 0) {
            builder.setFlag(flags, true);
        }
        if (positiveAction != null) {
            builder.addAction(positiveAction);
        }
        if (negativeAction != null) {
            builder.addAction(negativeAction);
        }
        if (listenToDismissEvent) {
            builder.setDeleteIntent(dismissIntent);
        }
        android.app.Notification notification = builder.build();
        notificationManager.createNotificationChannel(channel);
        notificationManager.notifyAsUser(notificationTag, 1, notification, android.os.UserHandle.CURRENT);
    }

    public static void cancelFaceReEnrollNotification(android.content.Context context) {
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class);
        notificationManager.cancelAsUser(FACE_RE_ENROLL_NOTIFICATION_TAG, 1, android.os.UserHandle.CURRENT);
    }

    public static void cancelFaceEnrollNotification(android.content.Context context) {
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class);
        notificationManager.cancelAsUser(FACE_ENROLL_NOTIFICATION_TAG, 1, android.os.UserHandle.CURRENT);
    }

    public static void cancelFingerprintEnrollNotification(android.content.Context context) {
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class);
        notificationManager.cancelAsUser(FINGERPRINT_ENROLL_NOTIFICATION_TAG, 1, android.os.UserHandle.CURRENT);
    }

    public static void cancelBadCalibrationNotification(android.content.Context context) {
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class);
        notificationManager.cancelAsUser(BAD_CALIBRATION_NOTIFICATION_TAG, 1, android.os.UserHandle.CURRENT);
    }

    public static void cancelFingerprintReEnrollNotification(android.content.Context context) {
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class);
        notificationManager.cancelAsUser(FINGERPRINT_RE_ENROLL_NOTIFICATION_TAG, 1, android.os.UserHandle.CURRENT);
    }
}
