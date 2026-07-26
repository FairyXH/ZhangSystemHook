package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface NotificationSignalExtractor {
    void initialize(android.content.Context context, com.android.server.notification.NotificationUsageStats notificationUsageStats);

    com.android.server.notification.RankingReconsideration process(com.android.server.notification.NotificationRecord notificationRecord);

    void setConfig(com.android.server.notification.RankingConfig rankingConfig);

    void setZenHelper(com.android.server.notification.ZenModeHelper zenModeHelper);

    default void setCompatChangeLogger(com.android.internal.compat.IPlatformCompat platformCompat) {
    }
}
