package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public final class NotificationRecord {
    private static final int MAX_SOUND_DELAY_MS = 2000;
    private static final int NOTIFICATION_REAR_LIGHT_DEFAULT_COLOR_RM = 570490654;
    private static final java.lang.String SPECIAL_LIGHTS_SETTING_NAME = "settings_special_lights";
    boolean isCanceled;
    public boolean isUpdate;
    private java.lang.String mAdjustmentIssuer;
    private boolean mAllowBubble;
    private boolean mAppDemotedFromConvo;
    private int mAuthoritativeRank;
    private android.app.NotificationChannel mChannel;
    private float mContactAffinity;
    private final android.content.Context mContext;
    private long mCreationTimeMs;
    private int mDelayRemoveReason;
    private boolean mEditChoicesBeforeSending;
    private boolean mFlagBubbleRemoved;
    private java.lang.String mGlobalSortKey;
    private android.util.ArraySet<android.net.Uri> mGrantableUris;
    private boolean mHasSeenSmartReplies;
    private boolean mHasSentValidMsg;
    private boolean mHidden;
    private int mImportance;
    private boolean mImportanceFixed;
    private boolean mIntercept;
    private boolean mInterceptSet;
    private long mInterruptionTimeMs;
    private boolean mIsAppImportanceLocked;
    private boolean mIsInterruptive;
    private boolean mIsNotConversationOverride;
    private android.app.KeyguardManager mKeyguardManager;
    private long mLastAudiblyAlertedMs;
    private long mLastIntrusive;
    private int mNumberOfSmartActionsAdded;
    private int mNumberOfSmartRepliesAdded;
    final int mOriginalFlags;
    private int mPackagePriority;
    private int mPackageVisibility;
    private java.util.ArrayList<java.lang.String> mPeopleOverride;
    private android.util.ArraySet<java.lang.String> mPhoneNumbers;
    private boolean mPkgAllowedAsConvo;
    private boolean mPostSilently;
    private final android.os.PowerManager mPowerManager;
    private boolean mPreChannelsNotification;
    private boolean mRecentlyIntrusive;
    private boolean mRecordedInterruption;
    private boolean mSensitiveContent;
    private android.content.pm.ShortcutInfo mShortcutInfo;
    private boolean mShowBadge;
    private java.util.ArrayList<java.lang.CharSequence> mSmartReplies;
    private java.util.ArrayList<android.service.notification.SnoozeCriterion> mSnoozeCriteria;
    private boolean mSuggestionsGeneratedByAssistant;
    private java.util.ArrayList<android.app.Notification.Action> mSystemGeneratedSmartActions;
    final int mTargetSdkVersion;
    private boolean mTextChanged;
    final long mUpdateTimeMs;
    private java.lang.String mUserExplanation;
    private int mUserSentiment;
    private long mVisibleSinceMs;
    android.os.IBinder permissionOwner;
    private final android.service.notification.StatusBarNotification sbn;
    static final java.lang.String TAG = "NotificationService--NotificationRecord";
    static final boolean DBG = android.util.Log.isLoggable(TAG, 3);
    private int mSystemImportance = -1000;
    private int mAssistantImportance = -1000;
    private float mRankingScore = 0.0f;
    private int mCriticality = 2;
    private int mImportanceExplanationCode = 0;
    private int mInitialImportanceExplanationCode = 0;
    private int mSuppressedVisualEffects = 0;
    private boolean mPendingLogUpdate = false;
    private int mProposedImportance = -1000;
    private boolean mLedRM = android.os.SystemProperties.getBoolean("ro.oplus.display.led.rm", false);
    private java.util.List<java.lang.String> mShouldRedact = java.util.Arrays.asList("123G5", "123456F8", "123ķ4", "123Ŀ4", "1-1-01 is the date of your code T3425", "your code 54-234-3 was sent on 1-1-01", "34-58-30", "12-1-3089", "G-3d523", "G-FD-745", "your code is:G-345821", "your code is (G-345821", "your code is \nG-345821", "you code is G-345821.", "you code is (G-345821)", "c'est g4zy75", "2109", "3035", "1899");
    private java.util.List<java.lang.String> mShouldRedactR3 = java.util.Arrays.asList("your code is 123G5", "your code is 123456F8", "your code is 123ķ4", "your code is 123Ŀ4", "1-1-01 is the date of your code T3425", "your code 54-234-3 was sent on 1-1-01", "your code is 34-58-30", "your code is 12-1-3089", "your code is G-3d523", "your code is G-FD-745", "your code is:G-345821", "your code is (G-345821", "your code is \nG-345821", "you code is G-345821.", "you code is (G-345821)");
    private java.lang.String mShouldRedactString = "Sensitive Text login code is 397964";
    private com.android.server.notification.INotificationRecordWrapper mNRWrapper = new com.android.server.notification.NotificationRecord.NotificationRecordWrapper();
    private com.android.server.notification.INotificationRecordExt mNotificationRecordExt = (com.android.server.notification.INotificationRecordExt) system.ext.loader.core.ExtLoader.type(com.android.server.notification.INotificationRecordExt.class).base(this).create();
    private final com.android.server.uri.UriGrantsManagerInternal mUgmInternal = (com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class);
    private long mRankingTimeMs = calculateRankingTimeMs(0);
    com.android.server.notification.NotificationUsageStats.SingleNotificationStats stats = new com.android.server.notification.NotificationUsageStats.SingleNotificationStats();
    private android.net.Uri mSound = calculateSound();
    private android.os.VibrationEffect mVibration = calculateVibration();
    private android.media.AudioAttributes mAttributes = calculateAttributes();
    private com.android.server.notification.NotificationRecord.Light mLight = calculateLights();
    private final java.util.List<android.service.notification.Adjustment> mAdjustments = new java.util.ArrayList();
    private final android.service.notification.NotificationStats mStats = new android.service.notification.NotificationStats();

    public NotificationRecord(android.content.Context context, android.service.notification.StatusBarNotification sbn, android.app.NotificationChannel channel) {
        this.mImportance = -1000;
        this.mPreChannelsNotification = true;
        this.mSensitiveContent = false;
        this.sbn = sbn;
        this.mTargetSdkVersion = ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).getPackageTargetSdkVersion(sbn.getPackageName());
        this.mOriginalFlags = sbn.getNotification().flags;
        this.mCreationTimeMs = sbn.getPostTime();
        this.mUpdateTimeMs = this.mCreationTimeMs;
        this.mInterruptionTimeMs = this.mCreationTimeMs;
        this.mContext = context;
        this.mKeyguardManager = (android.app.KeyguardManager) this.mContext.getSystemService(android.app.KeyguardManager.class);
        this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
        this.mChannel = channel;
        this.mPreChannelsNotification = isPreChannelsNotification();
        this.mImportance = calculateInitialImportance();
        calculateUserSentiment();
        calculateGrantableUris();
        if (sbn.getNotification() != null) {
            java.lang.CharSequence text = sbn.getNotification().extras.getCharSequence("android.text");
            if (!android.text.TextUtils.isEmpty(text)) {
                if (this.mShouldRedact.contains(text.toString()) || this.mShouldRedactR3.contains(text.toString()) || text.toString().contains(this.mShouldRedactString)) {
                    android.util.Log.i(TAG, "force set sensitiveContent");
                    this.mSensitiveContent = true;
                }
            }
        }
    }

    private boolean isPreChannelsNotification() {
        if ("miscellaneous".equals(getChannel().getId()) && this.mTargetSdkVersion < 26) {
            return true;
        }
        return false;
    }

    private android.net.Uri calculateSound() {
        android.net.Uri sound;
        android.app.Notification n = getSbn().getNotification();
        if (this.mContext.getPackageManager().hasSystemFeature("android.software.leanback") || (sound = this.mChannel.getSound()) == null) {
            return null;
        }
        if (this.mPreChannelsNotification) {
            boolean useDefaultSound = (n.defaults & 1) != 0;
            if (useDefaultSound) {
                return android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;
            }
            return n.sound;
        }
        return sound;
    }

    private boolean isSpecialLightCase() {
        if (android.os.SystemProperties.getBoolean("flag.notification_manager_service.light", false)) {
            android.util.Log.i(TAG, "isSpecialLightCase - Use nms light debug flag");
            return true;
        }
        if (android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "customize_breath_light_mms", 0) != 1) {
            android.util.Log.i(TAG, "isSpecialLightCase - Use setting [breath light] flag OFF");
            return false;
        }
        if (getImportance() < 3) {
            android.util.Log.i(TAG, "import low: " + getSbn());
            return false;
        }
        if (getSbn() != null) {
            if (!getSbn().isClearable()) {
                android.util.Log.i(TAG, "No Clearable notification: " + getSbn());
                return false;
            }
            android.app.Notification notification = getSbn().getNotification();
            boolean isFgService = (notification == null || (notification.flags & 64) == 0) ? false : true;
            if (isFgService) {
                android.util.Log.i(TAG, "Foreground service notification: " + getSbn());
                return false;
            }
        }
        boolean isInList = false;
        if (getSbn() != null) {
            java.lang.String Opkey = getSbn().getOpPkg();
            java.lang.String key = getSbn().getPackageName();
            int uid = getSbn().getUid();
            int userId = getSbn().getUserId();
            isInList = this.mNRWrapper.getNotificationRecordExt().isInLightList(key, userId);
            if ("com.android.incallui".equals(key)) {
                isInList = false;
            }
            android.util.Log.i(TAG, "isSpecialLightCase, Opkey: " + Opkey + ", key: " + key + ", uid: " + uid + ", userId: " + userId + ", isInList: " + isInList);
            if (!isInList && "com.android.contacts".equals(key) && this.mNRWrapper.getNotificationRecordExt().isInLightList("com.android.incallui", 0) && getChannel() != null && "contacts_missed_call_notification_channel_id".equals(getChannel().getId())) {
                android.util.Log.i(TAG, "missing call");
                return true;
            }
        }
        if (DBG) {
            android.util.Log.d(TAG, "isSpecialLightCase false, sbn=" + getSbn());
        }
        return isInList;
    }

    private com.android.server.notification.NotificationRecord.Light calculateLights() {
        int defaultLightColor;
        com.android.server.notification.NotificationRecord.Light light;
        boolean supportRearLight = isSupportRearLight();
        int defaultLightColor2 = NOTIFICATION_REAR_LIGHT_DEFAULT_COLOR_RM;
        if (supportRearLight) {
            defaultLightColor = NOTIFICATION_REAR_LIGHT_DEFAULT_COLOR_RM;
        } else {
            defaultLightColor = this.mContext.getResources().getColor(android.R.color.car_yellow_700);
        }
        int defaultLightOn = this.mContext.getResources().getInteger(android.R.integer.config_defaultMediaVibrationIntensity);
        int defaultLightOff = this.mContext.getResources().getInteger(android.R.integer.config_defaultMaxDurationBetweenUndimsMillis);
        int channelLightColor = getChannel().getLightColor() != 0 ? getChannel().getLightColor() : defaultLightColor;
        if (this.mNRWrapper.getNotificationRecordExt().hasCustomizeBreathLight()) {
            if (!supportRearLight) {
                defaultLightColor2 = 537266689;
            }
            android.service.notification.StatusBarNotification statusBarNotification = getSbn();
            if (statusBarNotification != null) {
                defaultLightColor2 = this.mNRWrapper.getNotificationRecordExt().calculateColor(statusBarNotification.getPackageName(), statusBarNotification.getUserId(), defaultLightColor2);
            }
            if (isSpecialLightCase()) {
                android.util.Log.d(TAG, "channelLightColor = 537266689, defaultLightOn = 2, defaultLightOff = 4");
                com.android.server.notification.NotificationRecord.Light lights = new com.android.server.notification.NotificationRecord.Light(supportRearLight ? defaultLightColor2 : 537266689, 2, 4);
                return lights;
            }
            return null;
        }
        com.android.server.notification.NotificationRecord.Light light2 = this.mNRWrapper.getNotificationRecordExt().calculateLights(this.mPreChannelsNotification, channelLightColor, defaultLightOn, defaultLightOff);
        if (this.mPreChannelsNotification) {
            android.app.Notification notification = getSbn().getNotification();
            if ((notification.flags & 1) != 0) {
                light = new com.android.server.notification.NotificationRecord.Light(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
                if ((notification.defaults & 4) != 0) {
                    light = new com.android.server.notification.NotificationRecord.Light(defaultLightColor, defaultLightOn, defaultLightOff);
                }
            } else {
                light = null;
            }
            return this.mNRWrapper.getNotificationRecordExt().calculateLights(getChannel().shouldShowLights(), light);
        }
        return light2;
    }

    private boolean isSupportRearLight() {
        boolean rearLight = this.mNRWrapper.getNotificationRecordExt().getIsSupportRearLight();
        android.util.Log.d(TAG, "isSupportRearLight , isRearLight = " + rearLight + " mLedRM = " + this.mLedRM + " isMultilLed = " + this.mNRWrapper.getNotificationRecordExt().isMultilLed());
        return (this.mLedRM && rearLight) || this.mNRWrapper.getNotificationRecordExt().isMultilLed();
    }

    private android.os.VibrationEffect getVibrationForChannel(android.app.NotificationChannel channel, com.android.server.notification.VibratorHelper helper, boolean insistent) {
        android.os.VibrationEffect vibration;
        if (!channel.shouldVibrate()) {
            return null;
        }
        if (android.app.Flags.notificationChannelVibrationEffectApi() && (vibration = channel.getVibrationEffect()) != null && helper.areEffectComponentsSupported(vibration)) {
            return vibration.applyRepeatingIndefinitely(insistent, 0);
        }
        long[] vibrationPattern = channel.getVibrationPattern();
        if (vibrationPattern == null) {
            if (this.mNRWrapper.getNotificationRecordExt().isLoggable()) {
                android.util.Log.d(TAG, "using default vibration pattern.");
            }
            if (this.mNRWrapper.getNotificationRecordExt().isVersionForJP()) {
                if (this.mNRWrapper.getNotificationRecordExt().isLoggable()) {
                    android.util.Log.d(TAG, "create default vibration for jp version.");
                }
                return this.mNRWrapper.getNotificationRecordExt().createDefaultVibration(helper, insistent);
            }
            return helper.createDefaultVibration(insistent);
        }
        if (!this.mNRWrapper.getNotificationRecordExt().isVersionForJP()) {
            vibrationPattern = this.mNRWrapper.getNotificationRecordExt().modifyVibrationPatternIfNeeded(vibrationPattern, insistent);
        }
        return com.android.server.notification.VibratorHelper.createWaveformVibration(vibrationPattern, insistent);
    }

    private android.os.VibrationEffect calculateVibration() {
        com.android.server.notification.VibratorHelper helper = new com.android.server.notification.VibratorHelper(this.mContext);
        android.app.Notification notification = getSbn().getNotification();
        boolean insistent = (notification.flags & 4) != 0;
        if (this.mPreChannelsNotification && (getChannel().getUserLockedFields() & 16) == 0) {
            boolean useDefaultVibrate = (notification.defaults & 2) != 0;
            if (useDefaultVibrate) {
                if (this.mNRWrapper.getNotificationRecordExt().isLoggable()) {
                    android.util.Log.d(TAG, "using default vibration pattern.");
                }
                if (this.mNRWrapper.getNotificationRecordExt().isVersionForJP()) {
                    if (this.mNRWrapper.getNotificationRecordExt().isLoggable()) {
                        android.util.Log.d(TAG, "create default vibration for jp version.");
                    }
                    return this.mNRWrapper.getNotificationRecordExt().createDefaultVibration(helper, insistent);
                }
                return helper.createDefaultVibration(insistent);
            }
            long[] notificationPattern = notification.vibrate;
            if (!this.mNRWrapper.getNotificationRecordExt().isVersionForJP()) {
                if (this.mNRWrapper.getNotificationRecordExt().isLoggable()) {
                    android.util.Log.d(TAG, "vibration pattern in this notification: " + java.util.Arrays.toString(notificationPattern));
                }
                notificationPattern = this.mNRWrapper.getNotificationRecordExt().modifyVibrationPatternIfNeeded(notificationPattern, insistent);
            }
            return com.android.server.notification.VibratorHelper.createWaveformVibration(notificationPattern, insistent);
        }
        return getVibrationForChannel(getChannel(), helper, insistent);
    }

    private android.media.AudioAttributes calculateAttributes() {
        android.app.Notification n = getSbn().getNotification();
        android.media.AudioAttributes attributes = getChannel().getAudioAttributes();
        if (attributes == null) {
            attributes = android.app.Notification.AUDIO_ATTRIBUTES_DEFAULT;
        }
        if (this.mPreChannelsNotification && (getChannel().getUserLockedFields() & 32) == 0) {
            if (n.audioAttributes != null) {
                return n.audioAttributes;
            }
            if (n.audioStreamType >= 0 && n.audioStreamType < android.media.AudioSystem.getNumStreamTypes()) {
                return new android.media.AudioAttributes.Builder().setInternalLegacyStreamType(n.audioStreamType).build();
            }
            if (n.audioStreamType != -1) {
                android.util.Log.w(TAG, java.lang.String.format("Invalid stream type: %d", java.lang.Integer.valueOf(n.audioStreamType)));
                return attributes;
            }
            return attributes;
        }
        return attributes;
    }

    private int calculateInitialImportance() {
        int i;
        android.app.Notification n = getSbn().getNotification();
        int importance = getChannel().getImportance();
        boolean z = true;
        if (getChannel().hasUserSetImportance()) {
            i = 2;
        } else {
            i = 1;
        }
        this.mInitialImportanceExplanationCode = i;
        if ((n.flags & 128) != 0) {
            n.priority = 2;
        }
        int requestedImportance = 3;
        n.priority = com.android.server.notification.NotificationManagerService.clamp(n.priority, -2, 2);
        switch (n.priority) {
            case -2:
                requestedImportance = 1;
                break;
            case -1:
                requestedImportance = 2;
                break;
            case 0:
                requestedImportance = 3;
                break;
            case 1:
            case 2:
                requestedImportance = 4;
                break;
        }
        this.stats.requestedImportance = requestedImportance;
        com.android.server.notification.NotificationUsageStats.SingleNotificationStats singleNotificationStats = this.stats;
        if (this.mSound == null && this.mVibration == null) {
            z = false;
        }
        singleNotificationStats.isNoisy = z;
        if (this.mPreChannelsNotification && (importance == -1000 || !getChannel().hasUserSetImportance())) {
            if (!this.stats.isNoisy && requestedImportance > 2) {
                requestedImportance = 2;
            }
            if (this.stats.isNoisy && requestedImportance < 3) {
                requestedImportance = 3;
            }
            if (n.fullScreenIntent != null) {
                requestedImportance = 4;
            }
            importance = requestedImportance;
            this.mInitialImportanceExplanationCode = 5;
        }
        int importance2 = this.mNRWrapper.getNotificationRecordExt().adjustImportanceForPackage(importance);
        this.stats.naturalImportance = importance2;
        return importance2;
    }

    public void copyRankingInformation(com.android.server.notification.NotificationRecord previous) {
        this.mContactAffinity = previous.mContactAffinity;
        this.mRecentlyIntrusive = previous.mRecentlyIntrusive;
        this.mPackagePriority = previous.mPackagePriority;
        this.mPackageVisibility = previous.mPackageVisibility;
        this.mIntercept = previous.mIntercept;
        this.mHidden = previous.mHidden;
        this.mRankingTimeMs = calculateRankingTimeMs(previous.getRankingTimeMs());
        this.mCreationTimeMs = previous.mCreationTimeMs;
        this.mVisibleSinceMs = previous.mVisibleSinceMs;
        if (previous.getSbn().getOverrideGroupKey() != null && !getSbn().isAppGroup()) {
            getSbn().setOverrideGroupKey(previous.getSbn().getOverrideGroupKey());
        }
    }

    public android.app.Notification getNotification() {
        return getSbn().getNotification();
    }

    public int getFlags() {
        return getSbn().getNotification().flags;
    }

    public android.os.UserHandle getUser() {
        return getSbn().getUser();
    }

    public java.lang.String getKey() {
        return getSbn().getKey();
    }

    public int getUserId() {
        return getSbn().getUserId();
    }

    public int getUid() {
        return getSbn().getUid();
    }

    void dump(android.util.proto.ProtoOutputStream proto, long fieldId, boolean redact, int state) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, getSbn().getKey());
        proto.write(1159641169922L, state);
        if (getChannel() != null) {
            proto.write(1138166333444L, getChannel().getId());
        }
        proto.write(1133871366152L, getLight() != null);
        proto.write(1133871366151L, getVibration() != null);
        proto.write(1120986464259L, getSbn().getNotification().flags);
        proto.write(1138166333449L, getGroupKey());
        proto.write(1172526071818L, getImportance());
        if (getSound() != null) {
            proto.write(1138166333445L, getSound().toString());
        }
        if (getAudioAttributes() != null) {
            getAudioAttributes().dumpDebug(proto, 1146756268038L);
        }
        proto.write(1138166333451L, getSbn().getPackageName());
        proto.write(1138166333452L, getSbn().getOpPkg());
        proto.end(token);
    }

    java.lang.String formatRemoteViews(android.widget.RemoteViews rv) {
        return rv == null ? "null" : java.lang.String.format("%s/0x%08x (%d bytes): %s", rv.getPackage(), java.lang.Integer.valueOf(rv.getLayoutId()), java.lang.Integer.valueOf(rv.estimateMemoryUsage()), rv.toString());
    }

    @dalvik.annotation.optimization.NeverCompile
    void dump(java.io.PrintWriter pw, java.lang.String prefix, android.content.Context baseContext, boolean redact) {
        android.app.Notification notification = getSbn().getNotification();
        pw.println(prefix + this);
        java.lang.String prefix2 = prefix + "  ";
        pw.println(prefix2 + "uid=" + getSbn().getUid() + " userId=" + getSbn().getUserId());
        pw.println(prefix2 + "opPkg=" + getSbn().getOpPkg());
        pw.println(prefix2 + "icon=" + notification.getSmallIcon());
        pw.println(prefix2 + "flags=" + android.app.Notification.flagsToString(notification.flags));
        pw.println(prefix2 + "originalFlags=" + android.app.Notification.flagsToString(this.mOriginalFlags));
        pw.println(prefix2 + "pri=" + notification.priority);
        pw.println(prefix2 + "key=" + getSbn().getKey());
        pw.println(prefix2 + "seen=" + this.mStats.hasSeen());
        pw.println(prefix2 + "groupKey=" + getGroupKey());
        pw.println(prefix2 + "notification=");
        dumpNotification(pw, prefix2 + prefix2, notification, redact);
        pw.println(prefix2 + "publicNotification=");
        dumpNotification(pw, prefix2 + prefix2, notification.publicVersion, redact);
        pw.println(prefix2 + "stats=" + this.stats.toString());
        pw.println(prefix2 + "mContactAffinity=" + this.mContactAffinity);
        pw.println(prefix2 + "mRecentlyIntrusive=" + this.mRecentlyIntrusive);
        pw.println(prefix2 + "mPackagePriority=" + this.mPackagePriority);
        pw.println(prefix2 + "mPackageVisibility=" + this.mPackageVisibility);
        pw.println(prefix2 + "mSystemImportance=" + android.service.notification.NotificationListenerService.Ranking.importanceToString(this.mSystemImportance));
        pw.println(prefix2 + "mAsstImportance=" + android.service.notification.NotificationListenerService.Ranking.importanceToString(this.mAssistantImportance));
        pw.println(prefix2 + "mImportance=" + android.service.notification.NotificationListenerService.Ranking.importanceToString(this.mImportance));
        pw.println(prefix2 + "mImportanceExplanation=" + ((java.lang.Object) getImportanceExplanation()));
        pw.println(prefix2 + "mProposedImportance=" + android.service.notification.NotificationListenerService.Ranking.importanceToString(this.mProposedImportance));
        pw.println(prefix2 + "mIsAppImportanceLocked=" + this.mIsAppImportanceLocked);
        pw.println(prefix2 + "mSensitiveContent=" + this.mSensitiveContent);
        pw.println(prefix2 + "mIntercept=" + this.mIntercept);
        pw.println(prefix2 + "mHidden==" + this.mHidden);
        pw.println(prefix2 + "mGlobalSortKey=" + this.mGlobalSortKey);
        pw.println(prefix2 + "mRankingTimeMs=" + this.mRankingTimeMs);
        pw.println(prefix2 + "mCreationTimeMs=" + this.mCreationTimeMs);
        pw.println(prefix2 + "mVisibleSinceMs=" + this.mVisibleSinceMs);
        pw.println(prefix2 + "mUpdateTimeMs=" + this.mUpdateTimeMs);
        pw.println(prefix2 + "mInterruptionTimeMs=" + this.mInterruptionTimeMs);
        pw.println(prefix2 + "mSuppressedVisualEffects= " + this.mSuppressedVisualEffects);
        if (this.mPreChannelsNotification) {
            pw.println(prefix2 + "defaults=" + android.app.Notification.defaultsToString(notification.defaults));
            pw.println(prefix2 + "n.sound=" + notification.sound);
            pw.println(prefix2 + "n.audioStreamType=" + notification.audioStreamType);
            pw.println(prefix2 + "n.audioAttributes=" + notification.audioAttributes);
            pw.println(prefix2 + java.lang.String.format("  led=0x%08x onMs=%d offMs=%d", java.lang.Integer.valueOf(notification.ledARGB), java.lang.Integer.valueOf(notification.ledOnMS), java.lang.Integer.valueOf(notification.ledOffMS)));
            pw.println(prefix2 + "vibrate=" + java.util.Arrays.toString(notification.vibrate));
        }
        pw.println(prefix2 + "mSound= " + this.mSound);
        pw.println(prefix2 + "mVibration= " + this.mVibration);
        pw.println(prefix2 + "mAttributes= " + this.mAttributes);
        pw.println(prefix2 + "mLight= " + this.mLight);
        pw.println(prefix2 + "mShowBadge=" + this.mShowBadge);
        pw.println(prefix2 + "mColorized=" + notification.isColorized());
        pw.println(prefix2 + "mAllowBubble=" + this.mAllowBubble);
        pw.println(prefix2 + "isBubble=" + notification.isBubbleNotification());
        pw.println(prefix2 + "mIsInterruptive=" + this.mIsInterruptive);
        pw.println(prefix2 + "effectiveNotificationChannel=" + getChannel());
        if (getPeopleOverride() != null) {
            pw.println(prefix2 + "overridePeople= " + android.text.TextUtils.join(",", getPeopleOverride()));
        }
        if (getSnoozeCriteria() != null) {
            pw.println(prefix2 + "snoozeCriteria=" + android.text.TextUtils.join(",", getSnoozeCriteria()));
        }
        pw.println(prefix2 + "mAdjustments=" + this.mAdjustments);
        pw.println(prefix2 + "shortcut=" + notification.getShortcutId() + " found valid? " + (this.mShortcutInfo != null));
        pw.println(prefix2 + "mUserVisOverride=" + getPackageVisibilityOverride());
    }

    private void dumpNotification(java.io.PrintWriter pw, java.lang.String prefix, android.app.Notification notification, boolean redact) {
        if (notification == null) {
            pw.println(prefix + com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG);
            return;
        }
        pw.println(prefix + "fullscreenIntent=" + notification.fullScreenIntent);
        pw.println(prefix + "contentIntent=" + notification.contentIntent);
        pw.println(prefix + "deleteIntent=" + notification.deleteIntent);
        pw.println(prefix + "number=" + notification.number);
        pw.println(prefix + "groupAlertBehavior=" + notification.getGroupAlertBehavior());
        pw.println(prefix + "when=" + notification.when + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + notification.getWhen());
        pw.print(prefix + "tickerText=");
        if (!android.text.TextUtils.isEmpty(notification.tickerText)) {
            java.lang.String ticker = notification.tickerText.toString();
            if (redact) {
                pw.print(ticker.length() > 16 ? ticker.substring(0, 8) : "");
                pw.println("...");
            } else {
                pw.println(ticker);
            }
        } else {
            pw.println("null");
        }
        pw.println(prefix + "vis=" + notification.visibility);
        pw.println(prefix + "contentView=" + formatRemoteViews(notification.contentView));
        pw.println(prefix + "bigContentView=" + formatRemoteViews(notification.bigContentView));
        pw.println(prefix + "headsUpContentView=" + formatRemoteViews(notification.headsUpContentView));
        pw.println(prefix + java.lang.String.format("color=0x%08x", java.lang.Integer.valueOf(notification.color)));
        pw.println(prefix + "timeout=" + java.time.Duration.ofMillis(notification.getTimeoutAfter()));
        if (notification.actions != null && notification.actions.length > 0) {
            pw.println(prefix + "actions={");
            int N = notification.actions.length;
            for (int i = 0; i < N; i++) {
                android.app.Notification.Action action = notification.actions[i];
                if (action != null) {
                    pw.println(java.lang.String.format("%s    [%d] \"%s\" -> %s", prefix, java.lang.Integer.valueOf(i), action.title, action.actionIntent == null ? "null" : action.actionIntent.toString()));
                }
            }
            pw.println(prefix + "  }");
        }
        if (notification.extras != null && notification.extras.size() > 0) {
            pw.println(prefix + "extras={");
            for (java.lang.String key : notification.extras.keySet()) {
                pw.print(prefix + "    " + key + "=");
                java.lang.Object val = notification.extras.get(key);
                if (val == null) {
                    pw.println("null");
                } else {
                    pw.print(val.getClass().getSimpleName());
                    if (redact && (val instanceof java.lang.CharSequence) && shouldRedactStringExtra(key)) {
                        pw.print(java.lang.String.format(" [length=%d]", java.lang.Integer.valueOf(((java.lang.CharSequence) val).length())));
                    } else if (val instanceof android.graphics.Bitmap) {
                        pw.print(java.lang.String.format(" (%dx%d)", java.lang.Integer.valueOf(((android.graphics.Bitmap) val).getWidth()), java.lang.Integer.valueOf(((android.graphics.Bitmap) val).getHeight())));
                    } else if (val.getClass().isArray()) {
                        int N2 = java.lang.reflect.Array.getLength(val);
                        pw.print(" (" + N2 + ")");
                        if (!redact) {
                            for (int j = 0; j < N2; j++) {
                                pw.println();
                                pw.print(java.lang.String.format("%s      [%d] %s", prefix, java.lang.Integer.valueOf(j), java.lang.String.valueOf(java.lang.reflect.Array.get(val, j))));
                            }
                        }
                    } else {
                        pw.print(" (" + java.lang.String.valueOf(val) + ")");
                    }
                    pw.println();
                }
            }
            pw.println(prefix + "}");
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:17:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean shouldRedactStringExtra(java.lang.String r4) {
        /*
            r3 = this;
            r0 = 1
            if (r4 != 0) goto L4
            return r0
        L4:
            int r1 = r4.hashCode()
            r2 = 0
            switch(r1) {
                case -1349298919: goto L21;
                case -330858995: goto L17;
                case 1258919194: goto Ld;
                default: goto Lc;
            }
        Lc:
            goto L2b
        Ld:
            java.lang.String r1 = "android.support.v4.app.extra.COMPAT_TEMPLATE"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto Lc
            r1 = 2
            goto L2c
        L17:
            java.lang.String r1 = "android.substName"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto Lc
            r1 = r2
            goto L2c
        L21:
            java.lang.String r1 = "android.template"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto Lc
            r1 = r0
            goto L2c
        L2b:
            r1 = -1
        L2c:
            switch(r1) {
                case 0: goto L30;
                case 1: goto L30;
                case 2: goto L30;
                default: goto L2f;
            }
        L2f:
            return r0
        L30:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationRecord.shouldRedactStringExtra(java.lang.String):boolean");
    }

    public final java.lang.String toString() {
        return java.lang.String.format("NotificationRecord(0x%08x: pkg=%s user=%s id=%d tag=%s importance=%d key=%s: %s)", java.lang.Integer.valueOf(java.lang.System.identityHashCode(this)), getSbn().getPackageName(), getSbn().getUser(), java.lang.Integer.valueOf(getSbn().getId()), getSbn().getTag(), java.lang.Integer.valueOf(this.mImportance), getSbn().getKey(), getSbn().getNotification());
    }

    public boolean hasAdjustment(java.lang.String key) {
        synchronized (this.mAdjustments) {
            for (android.service.notification.Adjustment adjustment : this.mAdjustments) {
                if (adjustment.getSignals().containsKey(key)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void addAdjustment(android.service.notification.Adjustment adjustment) {
        synchronized (this.mAdjustments) {
            this.mAdjustments.add(adjustment);
        }
    }

    public void applyAdjustments() {
        java.lang.System.currentTimeMillis();
        synchronized (this.mAdjustments) {
            for (android.service.notification.Adjustment adjustment : this.mAdjustments) {
                android.os.Bundle signals = adjustment.getSignals();
                if (signals.containsKey("key_people")) {
                    java.util.ArrayList<java.lang.String> people = adjustment.getSignals().getStringArrayList("key_people");
                    setPeopleOverride(people);
                    com.android.server.EventLogTags.writeNotificationAdjusted(getKey(), "key_people", people.toString());
                }
                if (signals.containsKey("key_snooze_criteria")) {
                    java.util.ArrayList<android.service.notification.SnoozeCriterion> snoozeCriterionList = adjustment.getSignals().getParcelableArrayList("key_snooze_criteria", android.service.notification.SnoozeCriterion.class);
                    setSnoozeCriteria(snoozeCriterionList);
                    com.android.server.EventLogTags.writeNotificationAdjusted(getKey(), "key_snooze_criteria", snoozeCriterionList.toString());
                }
                if (signals.containsKey("key_group_key")) {
                    java.lang.String groupOverrideKey = adjustment.getSignals().getString("key_group_key");
                    setOverrideGroupKey(groupOverrideKey);
                    com.android.server.EventLogTags.writeNotificationAdjusted(getKey(), "key_group_key", groupOverrideKey);
                }
                if (signals.containsKey("key_user_sentiment") && !this.mIsAppImportanceLocked && (getChannel().getUserLockedFields() & 4) == 0) {
                    setUserSentiment(adjustment.getSignals().getInt("key_user_sentiment", 0));
                    com.android.server.EventLogTags.writeNotificationAdjusted(getKey(), "key_user_sentiment", java.lang.Integer.toString(getUserSentiment()));
                }
                if (signals.containsKey("key_contextual_actions")) {
                    setSystemGeneratedSmartActions(signals.getParcelableArrayList("key_contextual_actions", android.app.Notification.Action.class));
                    com.android.server.EventLogTags.writeNotificationAdjusted(getKey(), "key_contextual_actions", getSystemGeneratedSmartActions().toString());
                }
                if (signals.containsKey("key_text_replies")) {
                    setSmartReplies(signals.getCharSequenceArrayList("key_text_replies"));
                    com.android.server.EventLogTags.writeNotificationAdjusted(getKey(), "key_text_replies", getSmartReplies().toString());
                }
                if (signals.containsKey("key_importance")) {
                    int importance = java.lang.Math.min(4, java.lang.Math.max(-1000, signals.getInt("key_importance")));
                    setAssistantImportance(importance);
                    com.android.server.EventLogTags.writeNotificationAdjusted(getKey(), "key_importance", java.lang.Integer.toString(importance));
                }
                if (signals.containsKey("key_ranking_score")) {
                    this.mRankingScore = signals.getFloat("key_ranking_score");
                    com.android.server.EventLogTags.writeNotificationAdjusted(getKey(), "key_ranking_score", java.lang.Float.toString(this.mRankingScore));
                }
                if (signals.containsKey("key_not_conversation")) {
                    this.mIsNotConversationOverride = signals.getBoolean("key_not_conversation");
                    com.android.server.EventLogTags.writeNotificationAdjusted(getKey(), "key_not_conversation", java.lang.Boolean.toString(this.mIsNotConversationOverride));
                }
                if (signals.containsKey("key_importance_proposal")) {
                    this.mProposedImportance = signals.getInt("key_importance_proposal");
                    com.android.server.EventLogTags.writeNotificationAdjusted(getKey(), "key_importance_proposal", java.lang.Integer.toString(this.mProposedImportance));
                }
                if (signals.containsKey("key_sensitive_content")) {
                    this.mSensitiveContent = signals.getBoolean("key_sensitive_content");
                    com.android.server.EventLogTags.writeNotificationAdjusted(getKey(), "key_sensitive_content", java.lang.Boolean.toString(this.mSensitiveContent));
                }
                if (!signals.isEmpty() && adjustment.getIssuer() != null) {
                    this.mAdjustmentIssuer = adjustment.getIssuer();
                }
            }
            this.mAdjustments.clear();
        }
    }

    java.lang.String getAdjustmentIssuer() {
        return this.mAdjustmentIssuer;
    }

    public void setIsAppImportanceLocked(boolean isAppImportanceLocked) {
        this.mIsAppImportanceLocked = isAppImportanceLocked;
        calculateUserSentiment();
    }

    public void setContactAffinity(float contactAffinity) {
        this.mContactAffinity = contactAffinity;
    }

    public float getContactAffinity() {
        return this.mContactAffinity;
    }

    public void setRecentlyIntrusive(boolean recentlyIntrusive) {
        this.mRecentlyIntrusive = recentlyIntrusive;
        if (recentlyIntrusive) {
            this.mLastIntrusive = java.lang.System.currentTimeMillis();
        }
    }

    public boolean isRecentlyIntrusive() {
        return this.mRecentlyIntrusive;
    }

    public long getLastIntrusive() {
        return this.mLastIntrusive;
    }

    public void setPackagePriority(int packagePriority) {
        this.mPackagePriority = packagePriority;
    }

    public int getPackagePriority() {
        return this.mPackagePriority;
    }

    public void setPackageVisibilityOverride(int packageVisibility) {
        this.mPackageVisibility = packageVisibility;
    }

    public int getPackageVisibilityOverride() {
        return this.mPackageVisibility;
    }

    private java.lang.String getUserExplanation() {
        if (this.mUserExplanation == null) {
            this.mUserExplanation = this.mContext.getResources().getString(android.R.string.imTypeWork);
        }
        return this.mUserExplanation;
    }

    public void setSystemImportance(int importance) {
        this.mSystemImportance = importance;
        calculateImportance();
    }

    public void setAssistantImportance(int importance) {
        this.mAssistantImportance = importance;
    }

    public int getAssistantImportance() {
        return this.mAssistantImportance;
    }

    public void setImportanceFixed(boolean fixed) {
        this.mImportanceFixed = fixed;
    }

    public boolean isImportanceFixed() {
        return this.mImportanceFixed;
    }

    protected void calculateImportance() {
        this.mImportance = calculateInitialImportance();
        this.mImportanceExplanationCode = this.mInitialImportanceExplanationCode;
        if (!getChannel().hasUserSetImportance() && this.mAssistantImportance != -1000 && !this.mImportanceFixed) {
            this.mImportance = this.mAssistantImportance;
            this.mImportanceExplanationCode = 3;
        }
        if (this.mSystemImportance != -1000) {
            this.mImportance = this.mSystemImportance;
            this.mImportanceExplanationCode = 4;
            this.mImportance = this.mNRWrapper.getNotificationRecordExt().adjustImportanceForPackage(this.mImportance);
        }
    }

    public int getImportance() {
        return this.mImportance;
    }

    int getInitialImportance() {
        return this.stats.naturalImportance;
    }

    public int getProposedImportance() {
        return this.mProposedImportance;
    }

    public boolean hasSensitiveContent() {
        return this.mSensitiveContent;
    }

    public float getRankingScore() {
        return this.mRankingScore;
    }

    int getImportanceExplanationCode() {
        return this.mImportanceExplanationCode;
    }

    int getInitialImportanceExplanationCode() {
        return this.mInitialImportanceExplanationCode;
    }

    public java.lang.CharSequence getImportanceExplanation() {
        switch (this.mImportanceExplanationCode) {
        }
        return null;
    }

    public boolean setIntercepted(boolean intercept) {
        this.mIntercept = intercept;
        this.mInterceptSet = true;
        return this.mIntercept;
    }

    public void setCriticality(int criticality) {
        this.mCriticality = criticality;
    }

    public int getCriticality() {
        return this.mCriticality;
    }

    public boolean isIntercepted() {
        return this.mIntercept;
    }

    public boolean hasInterceptBeenSet() {
        return this.mInterceptSet;
    }

    public boolean isNewEnoughForAlerting(long now) {
        return getFreshnessMs(now) <= 2000;
    }

    public void setHidden(boolean hidden) {
        this.mHidden = hidden;
    }

    public boolean isHidden() {
        return this.mHidden;
    }

    public boolean isForegroundService() {
        return (getFlags() & 64) != 0;
    }

    public void setPostSilently(boolean postSilently) {
        this.mPostSilently = postSilently;
    }

    public boolean shouldPostSilently() {
        return this.mPostSilently;
    }

    public void setSuppressedVisualEffects(int effects) {
        this.mSuppressedVisualEffects = effects;
    }

    public int getSuppressedVisualEffects() {
        return this.mSuppressedVisualEffects;
    }

    public boolean isCategory(java.lang.String category) {
        return java.util.Objects.equals(getNotification().category, category);
    }

    public boolean isAudioAttributesUsage(int usage) {
        return this.mAttributes.getUsage() == usage;
    }

    public long getRankingTimeMs() {
        return this.mRankingTimeMs;
    }

    public int getFreshnessMs(long now) {
        return (int) (now - this.mUpdateTimeMs);
    }

    public int getLifespanMs(long now) {
        return (int) (now - this.mCreationTimeMs);
    }

    public int getExposureMs(long now) {
        if (this.mVisibleSinceMs == 0) {
            return 0;
        }
        return (int) (now - this.mVisibleSinceMs);
    }

    public int getInterruptionMs(long now) {
        return (int) (now - this.mInterruptionTimeMs);
    }

    public long getUpdateTimeMs() {
        return this.mUpdateTimeMs;
    }

    public void setVisibility(boolean z, int i, int i2, com.android.server.notification.NotificationRecordLogger notificationRecordLogger) {
        long jCurrentTimeMillis = java.lang.System.currentTimeMillis();
        this.mVisibleSinceMs = z ? jCurrentTimeMillis : this.mVisibleSinceMs;
        this.stats.onVisibilityChanged(z);
        com.android.internal.logging.MetricsLogger.action(getLogMaker(jCurrentTimeMillis).setCategory(128).setType(z ? 1 : 2).addTaggedData(798, java.lang.Integer.valueOf(i)).addTaggedData(1395, java.lang.Integer.valueOf(i2)));
        if (z) {
            setSeen();
            com.android.internal.logging.MetricsLogger.histogram(this.mContext, "note_freshness", getFreshnessMs(jCurrentTimeMillis));
        }
        com.android.server.EventLogTags.writeNotificationVisibility(getKey(), z ? 1 : 0, getLifespanMs(jCurrentTimeMillis), getFreshnessMs(jCurrentTimeMillis), 0, i);
        notificationRecordLogger.logNotificationVisibility(this, z);
    }

    private long calculateRankingTimeMs(long previousRankingTimeMs) {
        android.app.Notification n = getNotification();
        if (android.app.Flags.sortSectionByTime()) {
            if (n.hasAppProvidedWhen() && n.getWhen() <= getSbn().getPostTime()) {
                return n.getWhen();
            }
        } else if (n.when != 0 && n.when <= getSbn().getPostTime()) {
            return n.when;
        }
        if (previousRankingTimeMs > 0) {
            return previousRankingTimeMs;
        }
        return getSbn().getPostTime();
    }

    public void setGlobalSortKey(java.lang.String globalSortKey) {
        this.mGlobalSortKey = globalSortKey;
    }

    public java.lang.String getGlobalSortKey() {
        return this.mGlobalSortKey;
    }

    public boolean isSeen() {
        return this.mStats.hasSeen();
    }

    public void setSeen() {
        this.mStats.setSeen();
        if (this.mTextChanged) {
            setInterruptive(true);
        }
    }

    public void setAuthoritativeRank(int authoritativeRank) {
        this.mAuthoritativeRank = authoritativeRank;
    }

    public int getAuthoritativeRank() {
        return this.mAuthoritativeRank;
    }

    public java.lang.String getGroupKey() {
        return getSbn().getGroupKey();
    }

    public void setOverrideGroupKey(java.lang.String overrideGroupKey) {
        getSbn().setOverrideGroupKey(overrideGroupKey);
    }

    public android.app.NotificationChannel getChannel() {
        return this.mChannel;
    }

    public boolean getIsAppImportanceLocked() {
        return this.mIsAppImportanceLocked;
    }

    protected void updateNotificationChannel(android.app.NotificationChannel channel) {
        if (channel != null) {
            this.mChannel = channel;
            calculateImportance();
            calculateUserSentiment();
            this.mVibration = calculateVibration();
            if (android.app.Flags.restrictAudioAttributesCall() || android.app.Flags.restrictAudioAttributesAlarm() || android.app.Flags.restrictAudioAttributesMedia()) {
                if (channel.getAudioAttributes() != null) {
                    this.mAttributes = channel.getAudioAttributes();
                } else {
                    this.mAttributes = android.app.Notification.AUDIO_ATTRIBUTES_DEFAULT;
                }
            }
        }
    }

    public void setShowBadge(boolean showBadge) {
        this.mShowBadge = showBadge;
    }

    public boolean canBubble() {
        return this.mAllowBubble;
    }

    public void setAllowBubble(boolean allow) {
        this.mAllowBubble = allow;
    }

    public boolean canShowBadge() {
        return this.mShowBadge;
    }

    public com.android.server.notification.NotificationRecord.Light getLight() {
        return this.mLight;
    }

    public android.net.Uri getSound() {
        return this.mSound;
    }

    public android.os.VibrationEffect getVibration() {
        return this.mVibration;
    }

    public android.media.AudioAttributes getAudioAttributes() {
        return this.mAttributes;
    }

    public java.util.ArrayList<java.lang.String> getPeopleOverride() {
        return this.mPeopleOverride;
    }

    public void resetRankingTime() {
        if (android.app.Flags.sortSectionByTime()) {
            this.mRankingTimeMs = calculateRankingTimeMs(getSbn().getPostTime());
        }
    }

    public void setInterruptive(boolean interruptive) {
        this.mIsInterruptive = interruptive;
        long now = java.lang.System.currentTimeMillis();
        this.mInterruptionTimeMs = interruptive ? now : this.mInterruptionTimeMs;
        if (interruptive) {
            com.android.internal.logging.MetricsLogger.action(getLogMaker().setCategory(1501).setType(1).addTaggedData(android.net.util.NetworkConstants.ETHER_MTU, java.lang.Integer.valueOf(getInterruptionMs(now))));
            com.android.internal.logging.MetricsLogger.histogram(this.mContext, "note_interruptive", getInterruptionMs(now));
        }
    }

    public void setAudiblyAlerted(boolean audiblyAlerted) {
        this.mLastAudiblyAlertedMs = audiblyAlerted ? java.lang.System.currentTimeMillis() : -1L;
    }

    public void setTextChanged(boolean textChanged) {
        this.mTextChanged = textChanged;
    }

    public void setRecordedInterruption(boolean recorded) {
        this.mRecordedInterruption = recorded;
    }

    public boolean hasRecordedInterruption() {
        return this.mRecordedInterruption;
    }

    public boolean isInterruptive() {
        return this.mIsInterruptive;
    }

    public boolean isTextChanged() {
        return this.mTextChanged;
    }

    public long getLastAudiblyAlertedMs() {
        return this.mLastAudiblyAlertedMs;
    }

    protected void setPeopleOverride(java.util.ArrayList<java.lang.String> people) {
        this.mPeopleOverride = people;
    }

    public java.util.ArrayList<android.service.notification.SnoozeCriterion> getSnoozeCriteria() {
        return this.mSnoozeCriteria;
    }

    protected void setSnoozeCriteria(java.util.ArrayList<android.service.notification.SnoozeCriterion> snoozeCriteria) {
        this.mSnoozeCriteria = snoozeCriteria;
    }

    private void calculateUserSentiment() {
        if ((getChannel().getUserLockedFields() & 4) != 0 || this.mIsAppImportanceLocked) {
            this.mUserSentiment = 1;
        }
    }

    private void setUserSentiment(int userSentiment) {
        this.mUserSentiment = userSentiment;
    }

    public int getUserSentiment() {
        return this.mUserSentiment;
    }

    public android.service.notification.NotificationStats getStats() {
        return this.mStats;
    }

    public void recordExpanded() {
        this.mStats.setExpanded();
    }

    public void recordDirectReplied() {
        if (android.app.Flags.lifetimeExtensionRefactor()) {
            android.app.Notification notification = getSbn().getNotification();
            notification.flags |= 65536;
        }
        this.mStats.setDirectReplied();
    }

    public void recordSmartReplied() {
        android.app.Notification notification = getSbn().getNotification();
        notification.flags |= 65536;
        this.mStats.setSmartReplied();
    }

    public void recordDismissalSurface(int surface) {
        this.mStats.setDismissalSurface(surface);
    }

    public void recordDismissalSentiment(int sentiment) {
        this.mStats.setDismissalSentiment(sentiment);
    }

    public void recordSnoozed() {
        this.mStats.setSnoozed();
    }

    public void recordViewedSettings() {
        this.mStats.setViewedSettings();
    }

    public void setNumSmartRepliesAdded(int noReplies) {
        this.mNumberOfSmartRepliesAdded = noReplies;
    }

    public int getNumSmartRepliesAdded() {
        return this.mNumberOfSmartRepliesAdded;
    }

    public void setNumSmartActionsAdded(int noActions) {
        this.mNumberOfSmartActionsAdded = noActions;
    }

    public int getNumSmartActionsAdded() {
        return this.mNumberOfSmartActionsAdded;
    }

    public void setSuggestionsGeneratedByAssistant(boolean generatedByAssistant) {
        this.mSuggestionsGeneratedByAssistant = generatedByAssistant;
    }

    public boolean getSuggestionsGeneratedByAssistant() {
        return this.mSuggestionsGeneratedByAssistant;
    }

    public boolean getEditChoicesBeforeSending() {
        return this.mEditChoicesBeforeSending;
    }

    public void setEditChoicesBeforeSending(boolean editChoicesBeforeSending) {
        this.mEditChoicesBeforeSending = editChoicesBeforeSending;
    }

    public boolean hasSeenSmartReplies() {
        return this.mHasSeenSmartReplies;
    }

    public void setSeenSmartReplies(boolean hasSeenSmartReplies) {
        this.mHasSeenSmartReplies = hasSeenSmartReplies;
    }

    public boolean hasBeenVisiblyExpanded() {
        return this.stats.hasBeenVisiblyExpanded();
    }

    public boolean isFlagBubbleRemoved() {
        return this.mFlagBubbleRemoved;
    }

    public void setFlagBubbleRemoved(boolean flagBubbleRemoved) {
        this.mFlagBubbleRemoved = flagBubbleRemoved;
    }

    public void setSystemGeneratedSmartActions(java.util.ArrayList<android.app.Notification.Action> systemGeneratedSmartActions) {
        this.mSystemGeneratedSmartActions = systemGeneratedSmartActions;
    }

    public java.util.ArrayList<android.app.Notification.Action> getSystemGeneratedSmartActions() {
        return this.mSystemGeneratedSmartActions;
    }

    public void setSmartReplies(java.util.ArrayList<java.lang.CharSequence> smartReplies) {
        this.mSmartReplies = smartReplies;
    }

    public java.util.ArrayList<java.lang.CharSequence> getSmartReplies() {
        return this.mSmartReplies;
    }

    public boolean isProxied() {
        return !java.util.Objects.equals(getSbn().getPackageName(), getSbn().getOpPkg());
    }

    public int getNotificationType() {
        if (isConversation()) {
            return 1;
        }
        if (getImportance() >= 3) {
            return 2;
        }
        return 4;
    }

    public android.util.ArraySet<android.net.Uri> getGrantableUris() {
        return this.mGrantableUris;
    }

    private void calculateGrantableUris() {
        android.app.NotificationChannel channel;
        android.os.Trace.beginSection("NotificationRecord.calculateGrantableUris");
        try {
            int sourceUid = getSbn().getUid();
            if (sourceUid == 1000) {
                return;
            }
            android.app.Notification notification = getNotification();
            notification.visitUris(new java.util.function.Consumer() { // from class: com.android.server.notification.NotificationRecord$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$calculateGrantableUris$0((android.net.Uri) obj);
                }
            });
            if (notification.getChannelId() != null && (channel = getChannel()) != null) {
                visitGrantableUri(channel.getSound(), (channel.getUserLockedFields() & 32) != 0, true);
            }
        } finally {
            android.os.Trace.endSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$calculateGrantableUris$0(android.net.Uri uri) {
        visitGrantableUri(uri, false, false);
    }

    private void visitGrantableUri(android.net.Uri uri, boolean userOverriddenUri, boolean isSound) {
        if (uri == null || !com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(uri.getScheme())) {
            return;
        }
        if (this.mGrantableUris != null && this.mGrantableUris.contains(uri)) {
            return;
        }
        int sourceUid = getSbn().getUid();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mUgmInternal.checkGrantUriPermission(sourceUid, null, android.content.ContentProvider.getUriWithoutUserId(uri), 1, android.content.ContentProvider.getUserIdFromUri(uri, android.os.UserHandle.getUserId(sourceUid)));
                if (this.mGrantableUris == null) {
                    this.mGrantableUris = new android.util.ArraySet<>();
                }
                this.mGrantableUris.add(uri);
            } catch (java.lang.SecurityException e) {
                if (!userOverriddenUri) {
                    if (isSound) {
                        this.mSound = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;
                        android.util.Log.w(TAG, "Replacing " + uri + " from " + sourceUid + ": " + e.getMessage());
                    } else {
                        if (this.mTargetSdkVersion >= 28) {
                            throw e;
                        }
                        android.util.Log.w(TAG, "Ignoring " + uri + " from " + sourceUid + ": " + e.getMessage());
                    }
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public android.metrics.LogMaker getLogMaker(long now) {
        android.metrics.LogMaker lm = getSbn().getLogMaker().addTaggedData(858, java.lang.Integer.valueOf(this.mImportance)).addTaggedData(793, java.lang.Integer.valueOf(getLifespanMs(now))).addTaggedData(795, java.lang.Integer.valueOf(getFreshnessMs(now))).addTaggedData(794, java.lang.Integer.valueOf(getExposureMs(now))).addTaggedData(android.net.util.NetworkConstants.ETHER_MTU, java.lang.Integer.valueOf(getInterruptionMs(now)));
        if (this.mImportanceExplanationCode != 0) {
            lm.addTaggedData(1688, java.lang.Integer.valueOf(this.mImportanceExplanationCode));
            if ((this.mImportanceExplanationCode == 3 || this.mImportanceExplanationCode == 4) && this.stats.naturalImportance != -1000) {
                lm.addTaggedData(1690, java.lang.Integer.valueOf(this.mInitialImportanceExplanationCode));
                lm.addTaggedData(1689, java.lang.Integer.valueOf(this.stats.naturalImportance));
            }
        }
        if (this.mAssistantImportance != -1000) {
            lm.addTaggedData(1691, java.lang.Integer.valueOf(this.mAssistantImportance));
        }
        if (this.mAdjustmentIssuer != null) {
            lm.addTaggedData(1742, java.lang.Integer.valueOf(this.mAdjustmentIssuer.hashCode()));
        }
        return lm;
    }

    public android.metrics.LogMaker getLogMaker() {
        return getLogMaker(java.lang.System.currentTimeMillis());
    }

    public android.metrics.LogMaker getItemLogMaker() {
        return getLogMaker().setCategory(128);
    }

    public boolean hasUndecoratedRemoteView() {
        android.app.Notification notification = getNotification();
        boolean hasDecoratedStyle = notification.isStyle(android.app.Notification.DecoratedCustomViewStyle.class) || notification.isStyle(android.app.Notification.DecoratedMediaCustomViewStyle.class);
        boolean hasCustomRemoteView = (notification.contentView == null && notification.bigContentView == null && notification.headsUpContentView == null) ? false : true;
        return hasCustomRemoteView && !hasDecoratedStyle;
    }

    public void setShortcutInfo(android.content.pm.ShortcutInfo shortcutInfo) {
        this.mShortcutInfo = shortcutInfo;
    }

    public android.content.pm.ShortcutInfo getShortcutInfo() {
        return this.mShortcutInfo;
    }

    public void setHasSentValidMsg(boolean hasSentValidMsg) {
        this.mHasSentValidMsg = hasSentValidMsg;
    }

    public void userDemotedAppFromConvoSpace(boolean userDemoted) {
        this.mAppDemotedFromConvo = userDemoted;
    }

    public void setPkgAllowedAsConvo(boolean allowedAsConvo) {
        this.mPkgAllowedAsConvo = allowedAsConvo;
    }

    public int getDelayRemoveReason() {
        return this.mDelayRemoveReason;
    }

    public void setDelayRemoveReason(int mDelayRemoveReason) {
        this.mDelayRemoveReason = mDelayRemoveReason;
    }

    public boolean isConversation() {
        android.app.Notification notification = getNotification();
        if (this.mChannel.isDemoted() || this.mAppDemotedFromConvo || this.mIsNotConversationOverride) {
            return false;
        }
        if (!notification.isStyle(android.app.Notification.MessagingStyle.class)) {
            return this.mPkgAllowedAsConvo && this.mTargetSdkVersion < 30 && "msg".equals(getNotification().category);
        }
        if (!this.mNRWrapper.getNotificationRecordExt().getSupportConversation()) {
            return false;
        }
        if (this.mTargetSdkVersion >= 30 && notification.isStyle(android.app.Notification.MessagingStyle.class) && (this.mShortcutInfo == null || isOnlyBots(this.mShortcutInfo.getPersons()))) {
            return false;
        }
        return (this.mHasSentValidMsg && this.mShortcutInfo == null) ? false : true;
    }

    private boolean isOnlyBots(android.app.Person[] persons) {
        if (persons == null || persons.length == 0) {
            return false;
        }
        for (android.app.Person person : persons) {
            if (!person.isBot()) {
                return false;
            }
        }
        return true;
    }

    android.service.notification.StatusBarNotification getSbn() {
        return this.sbn;
    }

    public boolean rankingScoreMatches(float otherScore) {
        return ((double) java.lang.Math.abs(this.mRankingScore - otherScore)) < 1.0E-4d;
    }

    protected void setPendingLogUpdate(boolean pendingLogUpdate) {
        this.mPendingLogUpdate = pendingLogUpdate;
    }

    protected boolean hasPendingLogUpdate() {
        return this.mPendingLogUpdate;
    }

    public void mergePhoneNumbers(android.util.ArraySet<java.lang.String> phoneNumbers) {
        if (phoneNumbers == null || phoneNumbers.size() == 0) {
            return;
        }
        if (this.mPhoneNumbers == null) {
            this.mPhoneNumbers = new android.util.ArraySet<>();
        }
        this.mPhoneNumbers.addAll((android.util.ArraySet<? extends java.lang.String>) phoneNumbers);
    }

    public android.util.ArraySet<java.lang.String> getPhoneNumbers() {
        return this.mPhoneNumbers;
    }

    boolean isLocked() {
        return getKeyguardManager().isKeyguardLocked() || !this.mPowerManager.isInteractive();
    }

    private android.app.KeyguardManager getKeyguardManager() {
        if (this.mKeyguardManager == null) {
            this.mKeyguardManager = (android.app.KeyguardManager) this.mContext.getSystemService(android.app.KeyguardManager.class);
        }
        return this.mKeyguardManager;
    }

    static final class Light {
        public final int color;
        public final int offMs;
        public final int onMs;

        public Light(int color, int onMs, int offMs) {
            this.color = color;
            this.onMs = onMs;
            this.offMs = offMs;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.notification.NotificationRecord.Light light = (com.android.server.notification.NotificationRecord.Light) o;
            if (this.color == light.color && this.onMs == light.onMs && this.offMs == light.offMs) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result = this.color;
            return (((result * 31) + this.onMs) * 31) + this.offMs;
        }

        public java.lang.String toString() {
            return "Light{color=" + this.color + ", onMs=" + this.onMs + ", offMs=" + this.offMs + '}';
        }
    }

    public com.android.server.notification.INotificationRecordWrapper getWrapper() {
        return this.mNRWrapper;
    }

    private class NotificationRecordWrapper implements com.android.server.notification.INotificationRecordWrapper {
        private NotificationRecordWrapper() {
        }

        @Override // com.android.server.notification.INotificationRecordWrapper
        public com.android.server.notification.INotificationRecordExt getNotificationRecordExt() {
            return com.android.server.notification.NotificationRecord.this.mNotificationRecordExt;
        }
    }
}
