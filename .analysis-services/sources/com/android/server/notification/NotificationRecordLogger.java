package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
interface NotificationRecordLogger {
    public static final java.lang.String TAG = "NotificationRecordLogger";

    void log(com.android.internal.logging.UiEventLogger.UiEventEnum uiEventEnum);

    void log(com.android.internal.logging.UiEventLogger.UiEventEnum uiEventEnum, com.android.server.notification.NotificationRecord notificationRecord);

    void logNotificationAdjusted(com.android.server.notification.NotificationRecord notificationRecord, int i, int i2, com.android.internal.logging.InstanceId instanceId);

    void logNotificationPosted(com.android.server.notification.NotificationRecordLogger.NotificationReported notificationReported);

    default com.android.server.notification.NotificationRecordLogger.NotificationReported prepareToLogNotificationPosted(com.android.server.notification.NotificationRecord r, com.android.server.notification.NotificationRecord old, int position, int buzzBeepBlink, com.android.internal.logging.InstanceId groupId) {
        com.android.server.notification.NotificationRecordLogger.NotificationRecordPair p = new com.android.server.notification.NotificationRecordLogger.NotificationRecordPair(r, old);
        if (!p.shouldLogReported(buzzBeepBlink)) {
            return null;
        }
        return new com.android.server.notification.NotificationRecordLogger.NotificationReported(p, com.android.server.notification.NotificationRecordLogger.NotificationReportedEvent.fromRecordPair(p), position, buzzBeepBlink, groupId);
    }

    default void logNotificationCancelled(com.android.server.notification.NotificationRecord r, int reason, int dismissalSurface) {
        log(com.android.server.notification.NotificationRecordLogger.NotificationCancelledEvent.fromCancelReason(reason, dismissalSurface), r);
    }

    default void logNotificationVisibility(com.android.server.notification.NotificationRecord r, boolean visible) {
        log(com.android.server.notification.NotificationRecordLogger.NotificationEvent.fromVisibility(visible), r);
    }

    public enum NotificationReportedEvent implements com.android.internal.logging.UiEventLogger.UiEventEnum {
        NOTIFICATION_POSTED(162),
        NOTIFICATION_UPDATED(163),
        NOTIFICATION_ADJUSTED(908);

        private final int mId;

        NotificationReportedEvent(int id) {
            this.mId = id;
        }

        public int getId() {
            return this.mId;
        }

        public static com.android.server.notification.NotificationRecordLogger.NotificationReportedEvent fromRecordPair(com.android.server.notification.NotificationRecordLogger.NotificationRecordPair p) {
            return p.old != null ? NOTIFICATION_UPDATED : NOTIFICATION_POSTED;
        }
    }

    public enum NotificationCancelledEvent implements com.android.internal.logging.UiEventLogger.UiEventEnum {
        INVALID(0),
        NOTIFICATION_CANCEL_CLICK(164),
        NOTIFICATION_CANCEL_USER_OTHER(165),
        NOTIFICATION_CANCEL_USER_CANCEL_ALL(166),
        NOTIFICATION_CANCEL_ERROR(167),
        NOTIFICATION_CANCEL_PACKAGE_CHANGED(168),
        NOTIFICATION_CANCEL_USER_STOPPED(169),
        NOTIFICATION_CANCEL_PACKAGE_BANNED(170),
        NOTIFICATION_CANCEL_APP_CANCEL(171),
        NOTIFICATION_CANCEL_APP_CANCEL_ALL(172),
        NOTIFICATION_CANCEL_LISTENER_CANCEL(173),
        NOTIFICATION_CANCEL_LISTENER_CANCEL_ALL(174),
        NOTIFICATION_CANCEL_GROUP_SUMMARY_CANCELED(175),
        NOTIFICATION_CANCEL_GROUP_OPTIMIZATION(176),
        NOTIFICATION_CANCEL_PACKAGE_SUSPENDED(177),
        NOTIFICATION_CANCEL_PROFILE_TURNED_OFF(178),
        NOTIFICATION_CANCEL_UNAUTOBUNDLED(179),
        NOTIFICATION_CANCEL_CHANNEL_BANNED(180),
        NOTIFICATION_CANCEL_SNOOZED(181),
        NOTIFICATION_CANCEL_TIMEOUT(com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__CREDENTIAL_MANAGEMENT_APP_REQUEST_FAILED),
        NOTIFICATION_CANCEL_CHANNEL_REMOVED(1261),
        NOTIFICATION_CANCEL_CLEAR_DATA(1262),
        NOTIFICATION_CANCEL_USER_PEEK(190),
        NOTIFICATION_CANCEL_USER_AOD(191),
        NOTIFICATION_CANCEL_USER_BUBBLE(1228),
        NOTIFICATION_CANCEL_USER_LOCKSCREEN(193),
        NOTIFICATION_CANCEL_USER_SHADE(192),
        NOTIFICATION_CANCEL_ASSISTANT(906);

        private final int mId;

        NotificationCancelledEvent(int id) {
            this.mId = id;
        }

        public int getId() {
            return this.mId;
        }

        public static com.android.server.notification.NotificationRecordLogger.NotificationCancelledEvent fromCancelReason(int reason, int surface) {
            if (surface == -1) {
                android.util.Log.wtf(com.android.server.notification.NotificationRecordLogger.TAG, "Unexpected surface: " + surface + " with reason " + reason);
                return INVALID;
            }
            if (reason == 2) {
                switch (surface) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    default:
                        android.util.Log.wtf(com.android.server.notification.NotificationRecordLogger.TAG, "Unexpected surface: " + surface + " with reason " + reason);
                        break;
                }
                return INVALID;
            }
            if (1 <= reason && reason <= 21) {
                return values()[reason];
            }
            if (reason == 22) {
                return NOTIFICATION_CANCEL_ASSISTANT;
            }
            android.util.Log.wtf(com.android.server.notification.NotificationRecordLogger.TAG, "Unexpected reason: " + reason + " with surface " + surface);
            return INVALID;
        }
    }

    public enum NotificationEvent implements com.android.internal.logging.UiEventLogger.UiEventEnum {
        NOTIFICATION_OPEN(197),
        NOTIFICATION_CLOSE(com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_USB_DATA_SIGNALING),
        NOTIFICATION_SNOOZED(com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_MEDIA_SESSION_CALLBACK),
        NOTIFICATION_NOT_POSTED_SNOOZED(319),
        NOTIFICATION_CLICKED(320),
        NOTIFICATION_ACTION_CLICKED(321),
        NOTIFICATION_DETAIL_OPEN_SYSTEM(327),
        NOTIFICATION_DETAIL_CLOSE_SYSTEM(328),
        NOTIFICATION_DETAIL_OPEN_USER(com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_TILE_ONCLICK),
        NOTIFICATION_DETAIL_CLOSE_USER(330),
        NOTIFICATION_DIRECT_REPLIED(331),
        NOTIFICATION_SMART_REPLIED(com.android.internal.art.ArtStatsLog.ART_DATUM_REPORTED),
        NOTIFICATION_SMART_REPLY_VISIBLE(com.android.internal.util.FrameworkStatsLog.DEVICE_ROTATED),
        NOTIFICATION_ACTION_CLICKED_0(450),
        NOTIFICATION_ACTION_CLICKED_1(com.android.internal.util.FrameworkStatsLog.CDM_ASSOCIATION_ACTION),
        NOTIFICATION_ACTION_CLICKED_2(com.android.internal.util.FrameworkStatsLog.MAGNIFICATION_TRIPLE_TAP_AND_HOLD_ACTIVATED_SESSION_REPORTED),
        NOTIFICATION_CONTEXTUAL_ACTION_CLICKED_0(com.android.internal.util.FrameworkStatsLog.MAGNIFICATION_FOLLOW_TYPING_FOCUS_ACTIVATED_SESSION_REPORTED),
        NOTIFICATION_CONTEXTUAL_ACTION_CLICKED_1(454),
        NOTIFICATION_CONTEXTUAL_ACTION_CLICKED_2(455),
        NOTIFICATION_ASSIST_ACTION_CLICKED_0(456),
        NOTIFICATION_ASSIST_ACTION_CLICKED_1(com.android.internal.art.ArtStatsLog.ISOLATED_COMPILATION_SCHEDULED),
        NOTIFICATION_ASSIST_ACTION_CLICKED_2(com.android.internal.art.ArtStatsLog.ISOLATED_COMPILATION_ENDED);

        private final int mId;

        NotificationEvent(int id) {
            this.mId = id;
        }

        public int getId() {
            return this.mId;
        }

        public static com.android.server.notification.NotificationRecordLogger.NotificationEvent fromVisibility(boolean visible) {
            return visible ? NOTIFICATION_OPEN : NOTIFICATION_CLOSE;
        }

        public static com.android.server.notification.NotificationRecordLogger.NotificationEvent fromExpanded(boolean expanded, boolean userAction) {
            return userAction ? expanded ? NOTIFICATION_DETAIL_OPEN_USER : NOTIFICATION_DETAIL_CLOSE_USER : expanded ? NOTIFICATION_DETAIL_OPEN_SYSTEM : NOTIFICATION_DETAIL_CLOSE_SYSTEM;
        }

        public static com.android.server.notification.NotificationRecordLogger.NotificationEvent fromAction(int index, boolean isAssistant, boolean isContextual) {
            if (index < 0 || index > 2) {
                return NOTIFICATION_ACTION_CLICKED;
            }
            if (isAssistant) {
                return values()[NOTIFICATION_ASSIST_ACTION_CLICKED_0.ordinal() + index];
            }
            if (isContextual) {
                return values()[NOTIFICATION_CONTEXTUAL_ACTION_CLICKED_0.ordinal() + index];
            }
            return values()[NOTIFICATION_ACTION_CLICKED_0.ordinal() + index];
        }
    }

    public enum NotificationPanelEvent implements com.android.internal.logging.UiEventLogger.UiEventEnum {
        NOTIFICATION_PANEL_OPEN(325),
        NOTIFICATION_PANEL_CLOSE(com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_PACKAGE_INSTALLER);

        private final int mId;

        NotificationPanelEvent(int id) {
            this.mId = id;
        }

        public int getId() {
            return this.mId;
        }
    }

    public static class NotificationRecordPair {
        public final com.android.server.notification.NotificationRecord old;
        public final com.android.server.notification.NotificationRecord r;

        NotificationRecordPair(com.android.server.notification.NotificationRecord r, com.android.server.notification.NotificationRecord old) {
            this.r = r;
            this.old = old;
        }

        boolean shouldLogReported(int buzzBeepBlink) {
            if (this.r == null) {
                return false;
            }
            if (this.old == null || buzzBeepBlink > 0) {
                return true;
            }
            return (java.util.Objects.equals(this.r.getSbn().getChannelIdLogTag(), this.old.getSbn().getChannelIdLogTag()) && java.util.Objects.equals(this.r.getSbn().getGroupLogTag(), this.old.getSbn().getGroupLogTag()) && this.r.getSbn().getNotification().isGroupSummary() == this.old.getSbn().getNotification().isGroupSummary() && java.util.Objects.equals(this.r.getSbn().getNotification().category, this.old.getSbn().getNotification().category) && this.r.getImportance() == this.old.getImportance() && com.android.server.notification.NotificationRecordLogger.getLoggingImportance(this.r) == com.android.server.notification.NotificationRecordLogger.getLoggingImportance(this.old) && this.r.rankingScoreMatches(this.old.getRankingScore())) ? false : true;
        }

        public int getStyle() {
            return getStyle(this.r.getSbn().getNotification().extras);
        }

        private int getStyle(android.os.Bundle extras) {
            java.lang.String template;
            if (extras != null && (template = extras.getString("android.template")) != null && !template.isEmpty()) {
                return template.hashCode();
            }
            return 0;
        }

        int getNumPeople() {
            return getNumPeople(this.r.getSbn().getNotification().extras);
        }

        private int getNumPeople(android.os.Bundle extras) {
            java.util.ArrayList<android.app.Person> people;
            if (extras != null && (people = extras.getParcelableArrayList("android.people.list", android.app.Person.class)) != null && !people.isEmpty()) {
                return people.size();
            }
            return 0;
        }

        int getAssistantHash() {
            java.lang.String assistant = this.r.getAdjustmentIssuer();
            if (assistant == null) {
                return 0;
            }
            return assistant.hashCode();
        }

        int getInstanceId() {
            if (this.r.getSbn().getInstanceId() == null) {
                return 0;
            }
            return this.r.getSbn().getInstanceId().getId();
        }

        int getNotificationIdHash() {
            return com.android.server.notification.SmallHash.hash(java.util.Objects.hashCode(this.r.getSbn().getTag()) ^ this.r.getSbn().getId());
        }

        int getChannelIdHash() {
            return com.android.server.notification.SmallHash.hash(this.r.getSbn().getNotification().getChannelId());
        }

        int getGroupIdHash() {
            return com.android.server.notification.SmallHash.hash(this.r.getSbn().getGroup());
        }
    }

    public static class NotificationReported {
        final int age_in_minutes;
        final int alerting;
        final int assistant_hash;
        final float assistant_ranking_score;
        final java.lang.String category;
        final int channel_id_hash;
        final int event_id;
        final int fsi_state;
        final int group_id_hash;
        final int group_instance_id;
        final int importance;
        final int importance_asst;
        final int importance_initial;
        final int importance_initial_source;
        final int importance_source;
        final int instance_id;
        final boolean is_foreground_service;
        final boolean is_group_summary;
        final boolean is_locked;
        final boolean is_non_dismissible;
        final boolean is_ongoing;
        final int notification_id_hash;
        final int num_people;
        final java.lang.String package_name;
        final int position;
        long post_duration_millis;
        final int style;
        final long timeout_millis;
        final int uid;

        NotificationReported(com.android.server.notification.NotificationRecordLogger.NotificationRecordPair p, com.android.server.notification.NotificationRecordLogger.NotificationReportedEvent eventType, int position, int buzzBeepBlink, com.android.internal.logging.InstanceId groupId) {
            this.event_id = eventType.getId();
            this.uid = p.r.getUid();
            this.package_name = p.r.getSbn().getPackageName();
            this.instance_id = p.getInstanceId();
            this.notification_id_hash = p.getNotificationIdHash();
            this.channel_id_hash = p.getChannelIdHash();
            this.group_id_hash = p.getGroupIdHash();
            this.group_instance_id = groupId == null ? 0 : groupId.getId();
            this.is_group_summary = p.r.getSbn().getNotification().isGroupSummary();
            this.category = p.r.getSbn().getNotification().category;
            this.style = p.getStyle();
            this.num_people = p.getNumPeople();
            this.position = position;
            this.importance = com.android.server.notification.NotificationRecordLogger.getLoggingImportance(p.r);
            this.alerting = buzzBeepBlink;
            this.importance_source = p.r.getImportanceExplanationCode();
            this.importance_initial = p.r.getInitialImportance();
            this.importance_initial_source = p.r.getInitialImportanceExplanationCode();
            this.importance_asst = p.r.getAssistantImportance();
            this.assistant_hash = p.getAssistantHash();
            this.assistant_ranking_score = p.r.getRankingScore();
            this.is_ongoing = p.r.getSbn().isOngoing();
            this.is_foreground_service = com.android.server.notification.NotificationRecordLogger.isForegroundService(p.r);
            this.timeout_millis = p.r.getSbn().getNotification().getTimeoutAfter();
            this.is_non_dismissible = com.android.server.notification.NotificationRecordLogger.isNonDismissible(p.r);
            boolean hasFullScreenIntent = p.r.getSbn().getNotification().fullScreenIntent != null;
            boolean hasFsiRequestedButDeniedFlag = (p.r.getSbn().getNotification().flags & 16384) != 0;
            this.fsi_state = com.android.server.notification.NotificationRecordLogger.getFsiState(hasFullScreenIntent, hasFsiRequestedButDeniedFlag, eventType);
            this.is_locked = p.r.isLocked();
            this.age_in_minutes = com.android.server.notification.NotificationRecordLogger.getAgeInMinutes(p.r.getSbn().getPostTime(), p.r.getSbn().getNotification().getWhen());
        }
    }

    static int getLoggingImportance(com.android.server.notification.NotificationRecord r) {
        int importance = r.getImportance();
        android.app.NotificationChannel channel = r.getChannel();
        if (channel == null) {
            return importance;
        }
        return com.android.server.notification.NotificationChannelLogger.getLoggingImportance(channel, importance);
    }

    static boolean isForegroundService(com.android.server.notification.NotificationRecord r) {
        return (r.getSbn() == null || r.getSbn().getNotification() == null || (r.getSbn().getNotification().flags & 64) == 0) ? false : true;
    }

    static boolean isNonDismissible(com.android.server.notification.NotificationRecord r) {
        return (r.getSbn() == null || r.getSbn().getNotification() == null || (r.getNotification().flags & 8192) == 0) ? false : true;
    }

    static int getFsiState(boolean hasFullScreenIntent, boolean hasFsiRequestedButDeniedFlag, com.android.server.notification.NotificationRecordLogger.NotificationReportedEvent eventType) {
        if (eventType == com.android.server.notification.NotificationRecordLogger.NotificationReportedEvent.NOTIFICATION_UPDATED) {
            return 0;
        }
        if (hasFullScreenIntent) {
            return 1;
        }
        return hasFsiRequestedButDeniedFlag ? 2 : 0;
    }

    static int getAgeInMinutes(long postTimeMs, long whenMs) {
        return (int) java.time.Duration.ofMillis(postTimeMs - whenMs).toMinutes();
    }
}
