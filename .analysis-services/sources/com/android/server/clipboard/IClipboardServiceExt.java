package com.android.server.clipboard;

/* JADX INFO: loaded from: classes.dex */
public interface IClipboardServiceExt {
    default void hookServiceReady(android.content.Context context, com.android.server.clipboard.ClipboardService clip) {
    }

    default void hookServiceStart(android.os.Binder extensionService) {
    }

    default boolean hookClipboardAccessAllowedResult(int appOpsResult) {
        return false;
    }

    default android.content.ClipData hookGetPrimaryClipResult(android.content.Context context, android.content.ClipData clip, android.app.AppOpsManager appOps, java.lang.String callerPkg, int callerUid, int callerUserId, int deviceId, int primaryClipUid) {
        return clip;
    }

    default boolean hookShowAccessNotification(android.content.Context context, java.lang.String callingPackage, int uid, int primaryClipUid, android.app.AppOpsManager appOpsManager) {
        return true;
    }

    default boolean isActivityPreloadingPkg(java.lang.String callingPackage) {
        return false;
    }

    default java.lang.String getAccessNotificationMessage(android.content.Context context, int uid, java.lang.String pkgName, java.lang.CharSequence label, java.lang.String defaultMessage) {
        return defaultMessage;
    }

    default boolean isPrivilegedPackage(java.lang.String packageName, android.content.Context context) {
        return false;
    }

    default java.lang.String getOriginPkgName(java.lang.String packageName) {
        return packageName;
    }

    default void startAIClassificationLocked(android.os.Looper looper, android.content.ClipData clip, java.lang.String pkg, int userId, int deviceId) {
    }

    default android.content.ClipData showClassificationNotificationLocked(android.content.ClipData clip, java.lang.String callingPkg, int uid, int primaryClipUid, android.app.AppOpsManager appOpsManager, com.android.server.contentcapture.ContentCaptureManagerInternal captureManager, android.view.autofill.AutofillManagerInternal autofillManager, int userId) {
        return null;
    }

    default void showRejectNotificationLocked(java.lang.String callingPackage, android.app.AppOpsManager appOpsManager, android.content.pm.PackageManager pm, int uid, int primaryClipUid, int userId, android.content.Context context) {
    }

    default boolean getOplusShowAccessNotifications() {
        return true;
    }

    default void onCommonSetPrimaryClipLocked(android.content.Context context, boolean focus, android.content.ClipData data) {
    }

    default void onCommonGetPrimaryClipLocked(android.content.Context context, android.content.ClipData data, java.lang.String pkgName) {
    }
}
