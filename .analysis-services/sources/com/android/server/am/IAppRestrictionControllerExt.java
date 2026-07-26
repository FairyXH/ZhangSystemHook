package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IAppRestrictionControllerExt {
    public static final java.lang.String ACTION_NOTIFICATION_DELETE = "com.oplus.action.APPRESTRICTION_NOTIFICATION_DELETE";

    default void dump(java.io.PrintWriter pw, java.lang.String prefix) {
    }

    public interface IStaticExt {
        default int getNotificationCounts() {
            return -1;
        }

        default void setDeleteIntent(android.app.Notification.Builder builder, android.content.Context context) {
        }

        default void registerReceiverForDeleteNotification(android.content.Context context, android.os.Handler handler) {
        }

        default void incrementCount() {
        }

        default void decrementCount() {
        }

        default void cancelSummaryNotificationIfNecessary() {
        }
    }
}
