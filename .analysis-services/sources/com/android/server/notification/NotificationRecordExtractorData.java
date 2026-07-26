package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public final class NotificationRecordExtractorData {
    private final boolean mAllowBubble;
    private final android.app.NotificationChannel mChannel;
    private final java.lang.String mGroupKey;
    private final int mImportance;
    private final boolean mIsBubble;
    private final boolean mIsConversation;
    private final java.util.ArrayList<java.lang.String> mOverridePeople;
    private final int mPosition;
    private final int mProposedImportance;
    private final float mRankingScore;
    private final boolean mSensitiveContent;
    private final boolean mShowBadge;
    private final java.util.ArrayList<java.lang.CharSequence> mSmartReplies;
    private final java.util.ArrayList<android.service.notification.SnoozeCriterion> mSnoozeCriteria;
    private final java.lang.Integer mSuppressVisually;
    private final java.util.ArrayList<android.app.Notification.Action> mSystemSmartActions;
    private final java.lang.Integer mUserSentiment;
    private final int mVisibility;

    NotificationRecordExtractorData(int position, int visibility, boolean showBadge, boolean allowBubble, boolean isBubble, android.app.NotificationChannel channel, java.lang.String groupKey, java.util.ArrayList<java.lang.String> overridePeople, java.util.ArrayList<android.service.notification.SnoozeCriterion> snoozeCriteria, java.lang.Integer userSentiment, java.lang.Integer suppressVisually, java.util.ArrayList<android.app.Notification.Action> systemSmartActions, java.util.ArrayList<java.lang.CharSequence> smartReplies, int importance, float rankingScore, boolean isConversation, int proposedImportance, boolean sensitiveContent) {
        this.mPosition = position;
        this.mVisibility = visibility;
        this.mShowBadge = showBadge;
        this.mAllowBubble = allowBubble;
        this.mIsBubble = isBubble;
        this.mChannel = channel;
        this.mGroupKey = groupKey;
        this.mOverridePeople = overridePeople;
        this.mSnoozeCriteria = snoozeCriteria;
        this.mUserSentiment = userSentiment;
        this.mSuppressVisually = suppressVisually;
        this.mSystemSmartActions = systemSmartActions;
        this.mSmartReplies = smartReplies;
        this.mImportance = importance;
        this.mRankingScore = rankingScore;
        this.mIsConversation = isConversation;
        this.mProposedImportance = proposedImportance;
        this.mSensitiveContent = sensitiveContent;
    }

    boolean hasDiffForRankingLocked(com.android.server.notification.NotificationRecord r, int newPosition) {
        return (this.mPosition == newPosition && this.mVisibility == r.getPackageVisibilityOverride() && this.mShowBadge == r.canShowBadge() && this.mAllowBubble == r.canBubble() && this.mIsBubble == r.getNotification().isBubbleNotification() && java.util.Objects.equals(this.mChannel, r.getChannel()) && java.util.Objects.equals(this.mGroupKey, r.getGroupKey()) && java.util.Objects.equals(this.mOverridePeople, r.getPeopleOverride()) && java.util.Objects.equals(this.mSnoozeCriteria, r.getSnoozeCriteria()) && java.util.Objects.equals(this.mUserSentiment, java.lang.Integer.valueOf(r.getUserSentiment())) && java.util.Objects.equals(this.mSuppressVisually, java.lang.Integer.valueOf(r.getSuppressedVisualEffects())) && java.util.Objects.equals(this.mSystemSmartActions, r.getSystemGeneratedSmartActions()) && java.util.Objects.equals(this.mSmartReplies, r.getSmartReplies()) && this.mImportance == r.getImportance() && this.mProposedImportance == r.getProposedImportance() && this.mSensitiveContent == r.hasSensitiveContent()) ? false : true;
    }

    boolean hasDiffForLoggingLocked(com.android.server.notification.NotificationRecord r, int newPosition) {
        return (this.mPosition == newPosition && java.util.Objects.equals(this.mChannel, r.getChannel()) && java.util.Objects.equals(this.mGroupKey, r.getGroupKey()) && java.util.Objects.equals(this.mOverridePeople, r.getPeopleOverride()) && java.util.Objects.equals(this.mSnoozeCriteria, r.getSnoozeCriteria()) && java.util.Objects.equals(this.mUserSentiment, java.lang.Integer.valueOf(r.getUserSentiment())) && java.util.Objects.equals(this.mSystemSmartActions, r.getSystemGeneratedSmartActions()) && java.util.Objects.equals(this.mSmartReplies, r.getSmartReplies()) && this.mImportance == r.getImportance() && r.rankingScoreMatches(this.mRankingScore) && this.mIsConversation == r.isConversation() && this.mProposedImportance == r.getProposedImportance() && this.mSensitiveContent == r.hasSensitiveContent()) ? false : true;
    }
}
