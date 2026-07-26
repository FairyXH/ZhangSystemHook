package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface NotificationDelegate {
    void clearEffects();

    void clearInlineReplyUriPermissions(java.lang.String str, int i);

    void grantInlineReplyUriPermission(java.lang.String str, android.net.Uri uri, android.os.UserHandle userHandle, java.lang.String str2, int i);

    void onBubbleMetadataFlagChanged(java.lang.String str, int i);

    void onClearAll(int i, int i2, int i3);

    void onNotificationActionClick(int i, int i2, java.lang.String str, int i3, android.app.Notification.Action action, com.android.internal.statusbar.NotificationVisibility notificationVisibility, boolean z);

    void onNotificationBubbleChanged(java.lang.String str, boolean z, int i);

    void onNotificationClear(int i, int i2, java.lang.String str, int i3, java.lang.String str2, int i4, int i5, com.android.internal.statusbar.NotificationVisibility notificationVisibility);

    void onNotificationClick(int i, int i2, java.lang.String str, com.android.internal.statusbar.NotificationVisibility notificationVisibility);

    void onNotificationDirectReplied(java.lang.String str);

    void onNotificationError(int i, int i2, java.lang.String str, java.lang.String str2, int i3, int i4, int i5, java.lang.String str3, int i6);

    void onNotificationExpansionChanged(java.lang.String str, boolean z, boolean z2, int i);

    void onNotificationFeedbackReceived(java.lang.String str, android.os.Bundle bundle);

    void onNotificationSettingsViewed(java.lang.String str);

    void onNotificationSmartReplySent(java.lang.String str, int i, java.lang.CharSequence charSequence, int i2, boolean z);

    void onNotificationSmartSuggestionsAdded(java.lang.String str, int i, int i2, boolean z, boolean z2);

    void onNotificationVisibilityChanged(com.android.internal.statusbar.NotificationVisibility[] notificationVisibilityArr, com.android.internal.statusbar.NotificationVisibility[] notificationVisibilityArr2);

    void onPanelHidden();

    void onPanelRevealed(boolean z, int i);

    void onSetDisabled(int i);

    void prepareForPossibleShutdown();
}
