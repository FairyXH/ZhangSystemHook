package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface INotificationManagerServiceExt {
    default void init(com.android.server.notification.NotificationManagerService nms, android.content.Context context, com.android.server.notification.NotificationRecordLogger notificationRecordLogger, com.android.internal.logging.InstanceIdSequence notificationInstanceIdSequence) {
    }

    default void onStart() {
    }

    default void onBootPhase(int phase) {
    }

    default android.content.Intent getNotificationCenterIntent() {
        return null;
    }

    default void updateNoClearNotification(android.app.Notification n, java.lang.String pkg) {
    }

    default boolean shouldShowNotificationToast() {
        return true;
    }

    default void detectRedPacket(int notificationUid, int callingUid, com.android.server.notification.NotificationRecord r) {
    }

    default boolean hasCustomizeBreathLight() {
        return false;
    }

    default boolean canShowLightsLocked(com.android.server.notification.NotificationRecord record) {
        return false;
    }

    default boolean updateLightsStateLocked(com.android.server.notification.NotificationRecord ledNotification) {
        return false;
    }

    default boolean isAllowedAsConvo(java.lang.String pkg) {
        return false;
    }

    default void setSupportConversation(com.android.server.notification.NotificationRecord r) {
    }

    default void setKeepAliveAppIfNeeded(java.lang.String pkg, int id, boolean keep) {
    }

    default void notifyNotificationCentent(java.lang.String pkg, java.lang.String title, java.lang.String text) {
    }

    default boolean isStudyCenterActivated() {
        return false;
    }

    default java.util.List<java.lang.String> getStudyCenterWhiteLists() {
        return new java.util.ArrayList();
    }

    default android.os.VibrationEffect determineFinalVibrate(android.os.VibrationEffect effect) {
        return null;
    }

    default android.net.Uri determineFinalSoundUri(android.net.Uri ringtoneUri) {
        return null;
    }

    default void setVibrationType(java.lang.String pkgName, android.os.Bundle extras) {
    }

    default boolean isVibrationRingMuteSupport() {
        return false;
    }

    default boolean shouldUseHapticFeature(android.media.AudioManager audioManager, com.android.server.notification.NotificationRecord record) {
        return false;
    }

    default void setHapticExtrasForNotification(com.android.server.notification.NotificationRecord record) {
    }

    default boolean interceptEnqueueNotificationInternal(java.lang.String oriPkg, java.lang.String oriOpPkg, int oriCallingUid, int callingPid, java.lang.String tag, int id, android.app.Notification notification, int incomingUserId) {
        return false;
    }

    default void cancelAllLocked(int callingUid, int callingPid, int userId, int reason, com.android.server.notification.ManagedServices.ManagedServiceInfo listener, boolean includeCurrentProfiles) {
    }

    default android.content.Intent createAutoGroupSummaryAppIntent(java.lang.String pkg) {
        return null;
    }

    default boolean isLoggable() {
        return false;
    }

    default boolean shouldInterceptToast(java.lang.String pkg) {
        return false;
    }

    default boolean shouldInterceptToast(int uid, java.lang.String packageName) {
        return false;
    }

    default void updateNotification(int uid, java.lang.String pacakageName, boolean show) {
    }

    default boolean isHansFreezed(int uid, java.lang.String pkgName, int scene, java.lang.String from) {
        return false;
    }

    default boolean shouldLimitChannels(com.android.server.notification.PreferencesHelper preferencesHelper, java.lang.String pkg, int uid, int channelsSize) {
        return false;
    }

    default void cancelAllNotificationsInt(java.lang.String action, int callingUid, int callingPid, java.lang.String pkg, java.lang.String channelId, int mustHaveFlags, int mustNotHaveFlags, int userId, int reason) {
    }

    default boolean dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        return false;
    }

    default boolean isStowOptionKey(android.service.notification.Adjustment adjustment, java.lang.String key) {
        return false;
    }

    default void detectCancelAction(android.content.Context context, int id, java.lang.String pkg, int uid) {
    }

    default void setKeepAliveAppIfNeed(java.lang.String pkgName, int id, boolean isKeepAlive) {
    }

    default void updateNotification(int uid, java.lang.String pacakageName, java.lang.String key, boolean show) {
    }

    default boolean isAppRingtonePermissionGrantedInner(java.lang.String pkg, int uid) {
        return false;
    }

    default boolean isAppVibrationPermissionGrantedInner(java.lang.String pkg, int uid) {
        return false;
    }

    default boolean shouldSuppressEffect(com.android.server.notification.NotificationRecord record) {
        return false;
    }

    default boolean vibrateLinearmotorIfNeed(com.android.server.notification.NotificationRecord record, boolean hasValidSound, android.net.Uri soundUri, boolean canPlaySoundForPkg) {
        return false;
    }

    default boolean vibrateLinearmotorIfNeed(boolean hasValidSound, android.net.Uri soundUri, boolean canPlaySoundForPkg) {
        return false;
    }

    default boolean vibrateLinearmotorIfNeed(boolean hasValidSound, android.net.Uri soundUri) {
        return false;
    }

    default boolean playAsync(android.media.IRingtonePlayer player, com.android.server.notification.NotificationRecord record, android.net.Uri soundUri, boolean looping) {
        return false;
    }

    default boolean isNotificationForCurrentUser(com.android.server.notification.NotificationRecord r, int userId) {
        return false;
    }

    default void setNavigationStatus(java.lang.String pkg, java.lang.String channelId, int callingUid, int callingPid, int reason) {
    }

    default boolean shouldKeepNotifcationWhenForceStop(java.lang.String pkg, com.android.server.notification.NotificationRecord r, int reason) {
        return false;
    }

    default boolean isGroupSummaryCancelableForCancelAll(com.android.server.notification.NotificationRecord parentNotification, int userId, boolean includeCurrentProfiles, com.android.server.notification.ManagedServices.UserProfiles userProfiles, int reason) {
        return false;
    }

    default void clearNonCancelableSummaryKeys(int reason) {
    }

    default boolean isShutdown() {
        return false;
    }

    default boolean canListenNotificationChannelChange(java.lang.String pkg) {
        return false;
    }

    default boolean enabledAndUserMatches(android.service.notification.StatusBarNotification sbn, com.android.server.notification.ManagedServices.ManagedServiceInfo listener) {
        return false;
    }

    default void handleSavePolicyFile() {
    }

    default boolean isAppBlockedByWLB(com.android.server.notification.NotificationRecord record, com.android.server.notification.PreferencesHelper helper) {
        return false;
    }

    default int getMultiAppUserId() {
        return -1;
    }

    default int modifyToastDisplayIdForMirageDisplay(int displayId, java.lang.String packageName) {
        return 0;
    }

    default boolean shouldForcePlayRedPackageRington(com.android.server.notification.NotificationRecord record) {
        return false;
    }

    default android.app.NotificationChannel getConversationNotificationChannel(java.lang.String pkg, int uid, java.lang.String channelId, java.lang.String conversationId, boolean returnParentIfNoConversationChannel, boolean includeDeleted) {
        return null;
    }

    default android.content.pm.ParceledListSlice<android.app.NotificationChannel> getNotificationChannels(java.lang.String pkg, int uid, boolean includeDeleted) {
        return android.content.pm.ParceledListSlice.emptyList();
    }

    default android.app.NotificationChannelGroup getNotificationChannelGroupWithChannels(java.lang.String pkg, int uid, java.lang.String groupId, boolean includeDeleted) {
        return null;
    }

    default android.content.pm.ParceledListSlice<android.app.NotificationChannelGroup> getNotificationChannelGroups(java.lang.String pkg, int uid, boolean includeDeleted, boolean includeNonGrouped, boolean includeEmpty) {
        return android.content.pm.ParceledListSlice.emptyList();
    }

    default void onClearAllNotifications(int callingUid, int callingPid, int userId) {
    }

    default android.app.NotificationManager.Policy adjustNotificationPolicy(java.lang.String pkg, android.app.NotificationManager.Policy policy) {
        return policy;
    }

    default void onPendingNotifyPosted(android.service.notification.StatusBarNotification sbn) {
    }

    default boolean skipToastWhileActPreload(int displayId, java.lang.String packageName) {
        return false;
    }

    default boolean onHandleEnqueuedNotification(com.android.server.notification.NotificationRecord newRecord, com.android.server.notification.NotificationRecord oldRecord) {
        return false;
    }

    default void notifyRecordVisibilityChangedLocked(com.android.server.notification.NotificationRecord record, boolean visible, int rank, int count) {
    }

    default boolean shouldKeepNotifcationWhenTopStyleNotShow(com.android.server.notification.NotificationRecord record) {
        return false;
    }

    default boolean shouldUpdateNavigationMode(java.lang.String pkg, int channelId, int notificationUid) {
        return false;
    }

    default boolean shouldContinuousShowToast(java.util.ArrayList<com.android.server.notification.toast.ToastRecord> toastList) {
        return false;
    }

    default boolean shouldContinuousShowToast(boolean isCurrentToastShown, int index, java.util.ArrayList<com.android.server.notification.toast.ToastRecord> toastList) {
        return false;
    }

    default void scheduleDurationReachedLocked(android.os.Handler mHandler, com.android.server.notification.toast.ToastRecord r) {
    }

    default void setCurrentShowTime(long showTime) {
    }

    default void fixStopForegroundRemoveFlagSlow() {
    }

    default boolean shouldBeReplacedByFluidCard(com.android.server.notification.NotificationRecord record) {
        return false;
    }

    default void initGroupType(android.os.Handler handler, java.lang.Object notificationLock, com.android.server.notification.GroupHelper groupHelper, java.util.ArrayList<com.android.server.notification.NotificationRecord> notificationList, android.util.ArrayMap<java.lang.Integer, android.util.ArrayMap<java.lang.String, java.lang.String>> summaries) {
    }

    default boolean isForceGroup(android.service.notification.StatusBarNotification sbn) {
        return false;
    }

    default java.lang.String getGroupKey(android.service.notification.StatusBarNotification sbn) {
        return "ranker_group";
    }

    default boolean isInterceptCancelGroupChildren(android.service.notification.StatusBarNotification sbn, int cancelReason) {
        return false;
    }

    default void setDelayRemoveReason(com.android.server.notification.NotificationRecord r, boolean isGroup) {
    }

    default boolean shouldDelayRemove(com.android.server.notification.ManagedServices.ManagedServiceInfo info, android.service.notification.StatusBarNotification sbn, int reason) {
        return false;
    }

    default void notifyDelayRemovedLocked(android.os.Handler mHandler, com.android.server.notification.ManagedServices mListeners, int reason, boolean isGroup) {
    }

    default boolean isForwardToAssistants(android.service.notification.StatusBarNotification sbn) {
        return false;
    }

    default long getMcsAssistantDelayTime(long delayForAssistantTime) {
        return delayForAssistantTime;
    }

    default boolean isInterceptNotification(com.android.server.notification.NotificationRecord r) {
        return false;
    }

    default void dumpImpl(java.io.PrintWriter pw) {
    }

    default java.lang.String getZenModePackageName(java.lang.String reason) {
        return reason;
    }

    default boolean shouldPlayForVibrationRing(android.media.AudioManager audioManager) {
        return false;
    }
}
