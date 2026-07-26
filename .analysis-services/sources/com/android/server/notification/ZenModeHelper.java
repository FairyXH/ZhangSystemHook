package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class ZenModeHelper {
    private static final java.lang.String IMPLICIT_RULE_ID_PREFIX = "implicit_";
    private static final int MAX_ICON_RESOURCE_NAME_LENGTH = 1000;
    private static final java.lang.String PACKAGE_ANDROID = "android";
    private static final int RULE_INSTANCE_GRACE_PERIOD = 259200000;
    static final int RULE_LIMIT_PER_PACKAGE = 100;
    static final long SEND_ACTIVATION_AZR_STATUSES = 308673617;
    public static final long SUPPRESSED_EFFECT_ALL = 3;
    public static final long SUPPRESSED_EFFECT_CALLS = 2;
    public static final long SUPPRESSED_EFFECT_NOTIFICATIONS = 1;
    private final android.app.AppOpsManager mAppOps;
    protected android.media.AudioManagerInternal mAudioManager;
    private java.lang.String mCaller;
    private final java.time.Clock mClock;
    protected final com.android.server.notification.ZenModeConditions mConditions;
    protected android.service.notification.ZenModeConfig mConfig;
    protected android.app.NotificationManager.Policy mConsolidatedPolicy;
    private final android.content.Context mContext;
    private final android.service.notification.ZenModeConfig mDefaultConfig;
    private android.service.notification.DeviceEffectsApplier mDeviceEffectsApplier;
    private final com.android.server.notification.ZenModeFiltering mFiltering;
    private final com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.FlagResolver mFlagResolver;
    private final com.android.server.notification.ZenModeHelper.H mHandler;
    private int mIsAuto;
    protected boolean mIsSystemServicesReady;
    private final android.app.NotificationManager mNotificationManager;
    protected android.content.pm.PackageManager mPm;
    private java.lang.String[] mPriorityOnlyDndExemptPackages;
    private java.lang.String mRuleId;
    private final com.android.server.notification.ManagedServices.Config mServiceConfig;
    private final com.android.server.notification.ZenModeHelper.SettingsObserver mSettingsObserver;
    private long mSuppressedEffects;
    protected int mZenMode;
    private final com.android.server.notification.ZenModeEventLogger mZenModeEventLogger;
    static final java.lang.String TAG = "ZenModeHelper";
    static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final java.time.Duration DELETED_RULE_KEPT_FOR = java.time.Duration.ofDays(30);
    protected final android.util.ArrayMap<java.lang.String, java.lang.Integer> mRulesUidCache = new android.util.ArrayMap<>();
    private final java.util.ArrayList<com.android.server.notification.ZenModeHelper.Callback> mCallbacks = new java.util.ArrayList<>();
    private final com.android.server.notification.ZenModeHelper.RingerModeDelegate mRingerModeDelegate = new com.android.server.notification.ZenModeHelper.RingerModeDelegate();
    private final java.lang.Object mConfigsArrayLock = new java.lang.Object();
    final android.util.SparseArray<android.service.notification.ZenModeConfig> mConfigs = new android.util.SparseArray<>();
    private final com.android.server.notification.ZenModeHelper.Metrics mMetrics = new com.android.server.notification.ZenModeHelper.Metrics();
    private android.service.notification.ZenDeviceEffects mConsolidatedDeviceEffects = new android.service.notification.ZenDeviceEffects.Builder().build();
    private int mUser = 0;
    private final java.lang.Object mConfigLock = new java.lang.Object();
    private com.android.server.notification.IZenModeHelperExt mZenModeHelperExt = (com.android.server.notification.IZenModeHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.notification.IZenModeHelperExt.class).base(this).create();

    /* JADX WARN: Multi-variable type inference failed */
    public ZenModeHelper(android.content.Context context, android.os.Looper looper, java.time.Clock clock, com.android.server.notification.ConditionProviders conditionProviders, com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.FlagResolver flagResolver, com.android.server.notification.ZenModeEventLogger zenModeEventLogger) {
        android.service.notification.ZenModeConfig defaultConfig;
        this.mContext = context;
        this.mHandler = new com.android.server.notification.ZenModeHelper.H(looper);
        this.mClock = clock;
        addCallback(this.mMetrics);
        this.mAppOps = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
        this.mNotificationManager = (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class);
        if (android.app.Flags.modesUi()) {
            defaultConfig = android.service.notification.ZenModeConfig.getDefaultConfig();
        } else {
            defaultConfig = readDefaultConfig(this.mContext.getResources());
        }
        this.mDefaultConfig = defaultConfig;
        updateDefaultConfigAutomaticRules();
        if (android.app.Flags.modesApi()) {
            updateDefaultAutomaticRulePolicies();
        }
        this.mConfig = this.mDefaultConfig.copy();
        synchronized (this.mConfigsArrayLock) {
            this.mConfigs.put(0, this.mConfig);
        }
        this.mConsolidatedPolicy = this.mConfig.toNotificationPolicy();
        this.mSettingsObserver = new com.android.server.notification.ZenModeHelper.SettingsObserver(this.mHandler);
        this.mSettingsObserver.observe();
        this.mFiltering = new com.android.server.notification.ZenModeFiltering(this.mContext);
        this.mConditions = new com.android.server.notification.ZenModeConditions(this, conditionProviders);
        this.mServiceConfig = conditionProviders.getConfig();
        this.mFlagResolver = flagResolver;
        this.mZenModeEventLogger = zenModeEventLogger;
        if (this.mZenModeHelperExt != null) {
            this.mZenModeHelperExt.init(this, context, this.mAppOps);
        }
    }

    public android.os.Looper getLooper() {
        return this.mHandler.getLooper();
    }

    public java.lang.String toString() {
        return TAG;
    }

    public boolean matchesCallFilter(android.os.UserHandle userHandle, android.os.Bundle extras, com.android.server.notification.ValidateNotificationPeople validator, int contactsTimeoutMs, float timeoutAffinity, int callingUid) {
        boolean zMatchesCallFilter;
        synchronized (this.mConfigLock) {
            zMatchesCallFilter = com.android.server.notification.ZenModeFiltering.matchesCallFilter(this.mContext, this.mZenMode, this.mConsolidatedPolicy, userHandle, extras, validator, contactsTimeoutMs, timeoutAffinity, callingUid);
        }
        return zMatchesCallFilter;
    }

    public boolean isCall(com.android.server.notification.NotificationRecord record) {
        return this.mFiltering.isCall(record);
    }

    public void recordCaller(com.android.server.notification.NotificationRecord record) {
        this.mFiltering.recordCall(record);
    }

    protected void cleanUpCallersAfter(long timeThreshold) {
        this.mFiltering.cleanUpCallersAfter(timeThreshold);
    }

    public boolean shouldIntercept(com.android.server.notification.NotificationRecord record) {
        boolean zShouldIntercept;
        synchronized (this.mConfigLock) {
            zShouldIntercept = this.mFiltering.shouldIntercept(this.mZenMode, this.mConsolidatedPolicy, record);
        }
        return zShouldIntercept;
    }

    public void addCallback(com.android.server.notification.ZenModeHelper.Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(com.android.server.notification.ZenModeHelper.Callback callback) {
        this.mCallbacks.remove(callback);
    }

    public void initZenMode() {
        if (DEBUG) {
            android.util.Log.d(TAG, "initZenMode");
        }
        synchronized (this.mConfigLock) {
            updateConfigAndZenModeLocked(this.mConfig, 1, "init", true, 1000);
        }
    }

    public void onSystemReady() {
        if (DEBUG) {
            android.util.Log.d(TAG, "onSystemReady");
        }
        this.mAudioManager = (android.media.AudioManagerInternal) com.android.server.LocalServices.getService(android.media.AudioManagerInternal.class);
        if (this.mAudioManager != null) {
            this.mAudioManager.setRingerModeDelegate(this.mRingerModeDelegate);
        }
        this.mPm = this.mContext.getPackageManager();
        this.mHandler.postMetricsTimer();
        cleanUpZenRules();
        this.mIsSystemServicesReady = true;
        showZenUpgradeNotification(this.mZenMode);
    }

    void setDeviceEffectsApplier(android.service.notification.DeviceEffectsApplier deviceEffectsApplier) {
        if (!android.app.Flags.modesApi()) {
            return;
        }
        synchronized (this.mConfigLock) {
            if (this.mDeviceEffectsApplier != null) {
                throw new java.lang.IllegalStateException("Already set up a DeviceEffectsApplier!");
            }
            this.mDeviceEffectsApplier = deviceEffectsApplier;
        }
        applyConsolidatedDeviceEffects(1);
    }

    public void onUserSwitched(int user) {
        loadConfigForUser(user, "onUserSwitched");
    }

    public void onUserRemoved(int user) {
        if (user < 0) {
            return;
        }
        if (DEBUG) {
            android.util.Log.d(TAG, "onUserRemoved u=" + user);
        }
        synchronized (this.mConfigsArrayLock) {
            this.mConfigs.remove(user);
        }
    }

    public void onUserUnlocked(int user) {
        if (this.mZenModeHelperExt.interceptOnUserUnlocked(user)) {
            return;
        }
        loadConfigForUser(user, "onUserUnlocked");
    }

    void setPriorityOnlyDndExemptPackages(java.lang.String[] packages) {
        this.mPriorityOnlyDndExemptPackages = packages;
        if (this.mZenModeHelperExt != null) {
            this.mZenModeHelperExt.setPriorityOnlyDndExemptPackages(packages);
        }
    }

    private void loadConfigForUser(int user, java.lang.String reason) {
        android.service.notification.ZenModeConfig config;
        if (this.mUser == user || user < 0) {
            return;
        }
        this.mUser = user;
        if (DEBUG) {
            android.util.Log.d(TAG, reason + " u=" + user);
        }
        android.service.notification.ZenModeConfig config2 = null;
        synchronized (this.mConfigsArrayLock) {
            if (this.mConfigs.get(user) != null) {
                config2 = this.mConfigs.get(user).copy();
            }
        }
        if (config2 != null) {
            config = config2;
        } else {
            if (DEBUG) {
                android.util.Log.d(TAG, reason + " generating default config for user " + user);
            }
            android.service.notification.ZenModeConfig config3 = this.mDefaultConfig.copy();
            config3.user = user;
            config = config3;
        }
        synchronized (this.mConfigLock) {
            setConfigLocked(config, null, 2, reason, 1000);
        }
        cleanUpZenRules();
    }

    public int getZenModeListenerInterruptionFilter() {
        return android.app.NotificationManager.zenModeToInterruptionFilter(this.mZenMode);
    }

    public void requestFromListener(android.content.ComponentName name, int filter, int callingUid, boolean fromSystemOrSystemUi) {
        int newZen = android.app.NotificationManager.zenModeFromInterruptionFilter(filter, -1);
        if (newZen != -1) {
            setManualZenMode(newZen, null, fromSystemOrSystemUi ? 5 : 4, "listener:" + (name != null ? name.flattenToShortString() : null), name != null ? name.getPackageName() : null, callingUid);
        }
    }

    public void setSuppressedEffects(long suppressedEffects) {
        if (this.mSuppressedEffects == suppressedEffects) {
            return;
        }
        this.mSuppressedEffects = suppressedEffects;
        applyRestrictions();
    }

    public long getSuppressedEffects() {
        return this.mSuppressedEffects;
    }

    public int getZenMode() {
        return this.mZenMode;
    }

    public java.util.List<android.service.notification.ZenModeConfig.ZenRule> getZenRules() {
        java.util.List<android.service.notification.ZenModeConfig.ZenRule> rules = new java.util.ArrayList<>();
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return rules;
            }
            for (android.service.notification.ZenModeConfig.ZenRule rule : this.mConfig.automaticRules.values()) {
                if (canManageAutomaticZenRule(rule)) {
                    rules.add(rule);
                }
            }
            return rules;
        }
    }

    java.util.Map<java.lang.String, android.app.AutomaticZenRule> getAutomaticZenRules() {
        java.util.List<android.service.notification.ZenModeConfig.ZenRule> ruleList = getZenRules();
        java.util.HashMap<java.lang.String, android.app.AutomaticZenRule> rules = new java.util.HashMap<>(ruleList.size());
        for (android.service.notification.ZenModeConfig.ZenRule rule : ruleList) {
            rules.put(rule.id, zenRuleToAutomaticZenRule(rule));
        }
        return rules;
    }

    public android.app.AutomaticZenRule getAutomaticZenRule(java.lang.String id) {
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return null;
            }
            android.service.notification.ZenModeConfig.ZenRule rule = (android.service.notification.ZenModeConfig.ZenRule) this.mConfig.automaticRules.get(id);
            if (rule != null && canManageAutomaticZenRule(rule)) {
                return zenRuleToAutomaticZenRule(rule);
            }
            return null;
        }
    }

    public java.lang.String addAutomaticZenRule(java.lang.String pkg, android.app.AutomaticZenRule automaticZenRule, int origin, java.lang.String reason, int callingUid) {
        java.lang.String str;
        requirePublicOrigin("addAutomaticZenRule", origin);
        if (!"android".equals(pkg)) {
            android.content.pm.PackageItemInfo component = getServiceInfo(automaticZenRule.getOwner());
            if (component == null) {
                component = getActivityInfo(automaticZenRule.getConfigurationActivity());
            }
            if (component == null) {
                throw new java.lang.IllegalArgumentException("Lacking enabled CPS or config activity");
            }
            int ruleInstanceLimit = -1;
            if (component.metaData != null) {
                ruleInstanceLimit = component.metaData.getInt("android.service.zen.automatic.ruleInstanceLimit", -1);
            }
            int newRuleInstanceCount = getCurrentInstanceCount(automaticZenRule.getOwner()) + getCurrentInstanceCount(automaticZenRule.getConfigurationActivity()) + 1;
            int newPackageRuleCount = getPackageRuleCount(pkg) + 1;
            if (newPackageRuleCount > 100 || (ruleInstanceLimit > 0 && ruleInstanceLimit < newRuleInstanceCount)) {
                throw new java.lang.IllegalArgumentException("Rule instance limit exceeded");
            }
        }
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                throw new android.util.AndroidRuntimeException("Could not create rule");
            }
            if (DEBUG) {
                android.util.Log.d(TAG, "addAutomaticZenRule rule= " + automaticZenRule + " reason=" + reason);
            }
            android.service.notification.ZenModeConfig newConfig = this.mConfig.copy();
            android.service.notification.ZenModeConfig.ZenRule rule = new android.service.notification.ZenModeConfig.ZenRule();
            populateZenRule(pkg, automaticZenRule, rule, origin, true);
            android.service.notification.ZenModeConfig.ZenRule rule2 = maybeRestoreRemovedRule(newConfig, rule, automaticZenRule, origin);
            newConfig.automaticRules.put(rule2.id, rule2);
            maybeReplaceDefaultRule(newConfig, automaticZenRule);
            if (setConfigLocked(newConfig, origin, reason, rule2.component, true, callingUid)) {
                str = rule2.id;
            } else {
                throw new android.util.AndroidRuntimeException("Could not create rule");
            }
        }
        return str;
    }

    private android.service.notification.ZenModeConfig.ZenRule maybeRestoreRemovedRule(android.service.notification.ZenModeConfig config, android.service.notification.ZenModeConfig.ZenRule ruleToAdd, android.app.AutomaticZenRule azrToAdd, int origin) {
        java.lang.String deletedKey;
        android.service.notification.ZenModeConfig.ZenRule ruleToRestore;
        if (!android.app.Flags.modesApi() || (deletedKey = android.service.notification.ZenModeConfig.deletedRuleKey(ruleToAdd)) == null || (ruleToRestore = (android.service.notification.ZenModeConfig.ZenRule) config.deletedRules.get(deletedKey)) == null) {
            return ruleToAdd;
        }
        config.deletedRules.remove(deletedKey);
        ruleToRestore.deletionInstant = null;
        if (origin != 4) {
            return ruleToAdd;
        }
        populateZenRule(ruleToRestore.pkg, azrToAdd, ruleToRestore, origin, false);
        return ruleToRestore;
    }

    private static void maybeReplaceDefaultRule(android.service.notification.ZenModeConfig config, android.app.AutomaticZenRule addedRule) {
        android.service.notification.ZenModeConfig.ZenRule sleepingRule;
        if (android.app.Flags.modesApi() && addedRule.getType() == 3 && (sleepingRule = (android.service.notification.ZenModeConfig.ZenRule) config.automaticRules.get("EVERY_NIGHT_DEFAULT_RULE")) != null && !sleepingRule.enabled && sleepingRule.canBeUpdatedByApp()) {
            config.automaticRules.remove("EVERY_NIGHT_DEFAULT_RULE");
        }
    }

    public boolean updateAutomaticZenRule(java.lang.String ruleId, android.app.AutomaticZenRule automaticZenRule, int origin, java.lang.String reason, int callingUid) throws java.lang.Throwable {
        int i;
        requirePublicOrigin("updateAutomaticZenRule", origin);
        if (ruleId == null) {
            throw new java.lang.IllegalArgumentException("ruleId cannot be null");
        }
        synchronized (this.mConfigLock) {
            try {
                try {
                    if (this.mConfig == null) {
                        return false;
                    }
                    if (DEBUG) {
                        try {
                            android.util.Log.d(TAG, "updateAutomaticZenRule zenRule=" + automaticZenRule + " reason=" + reason);
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                    android.service.notification.ZenModeConfig.ZenRule oldRule = (android.service.notification.ZenModeConfig.ZenRule) this.mConfig.automaticRules.get(ruleId);
                    if (oldRule == null || !canManageAutomaticZenRule(oldRule)) {
                        throw new java.lang.SecurityException("Cannot update rules not owned by your condition provider");
                    }
                    android.service.notification.ZenModeConfig newConfig = this.mConfig.copy();
                    android.service.notification.ZenModeConfig.ZenRule newRule = (android.service.notification.ZenModeConfig.ZenRule) java.util.Objects.requireNonNull((android.service.notification.ZenModeConfig.ZenRule) newConfig.automaticRules.get(ruleId));
                    if (!android.app.Flags.modesApi() && newRule.enabled != automaticZenRule.isEnabled()) {
                        int i2 = this.mConfig.user;
                        java.lang.String pkg = newRule.getPkg();
                        if (automaticZenRule.isEnabled()) {
                            i = 1;
                        } else {
                            i = 2;
                        }
                        dispatchOnAutomaticRuleStatusChanged(i2, pkg, ruleId, i);
                    }
                    boolean updated = populateZenRule(newRule.pkg, automaticZenRule, newRule, origin, false);
                    if (android.app.Flags.modesApi() && !updated) {
                        return true;
                    }
                    return setConfigLocked(newConfig, origin, reason, newRule.component, true, callingUid);
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    void applyGlobalZenModeAsImplicitZenRule(java.lang.String callingPkg, int callingUid, int zenMode) {
        if (!android.app.Flags.modesApi()) {
            android.util.Log.wtf(TAG, "applyGlobalZenModeAsImplicitZenRule called with flag off!");
            return;
        }
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return;
            }
            android.service.notification.ZenModeConfig newConfig = this.mConfig.copy();
            android.service.notification.ZenModeConfig.ZenRule rule = (android.service.notification.ZenModeConfig.ZenRule) newConfig.automaticRules.get(implicitRuleId(callingPkg));
            if (zenMode == 0) {
                if (rule != null) {
                    android.service.notification.Condition deactivated = new android.service.notification.Condition(rule.conditionId, this.mContext.getString(android.R.string.year), 0);
                    setAutomaticZenRuleStateLocked(newConfig, java.util.Collections.singletonList(rule), deactivated, 4, callingUid);
                }
            } else {
                if (rule == null) {
                    rule = newImplicitZenRule(callingPkg);
                    rule.zenPolicy = this.mConfig.getZenPolicy().copy();
                    newConfig.automaticRules.put(rule.id, rule);
                }
                if ((rule.userModifiedFields & 2) == 0) {
                    rule.zenMode = zenMode;
                }
                rule.snoozing = false;
                rule.condition = new android.service.notification.Condition(rule.conditionId, this.mContext.getString(android.R.string.wrong_hsum_configuration_notification_title), 1);
                setConfigLocked(newConfig, null, 4, "applyGlobalZenModeAsImplicitZenRule", callingUid);
            }
        }
    }

    void applyGlobalPolicyAsImplicitZenRule(java.lang.String callingPkg, int callingUid, android.app.NotificationManager.Policy policy) {
        boolean isNew;
        android.service.notification.ZenModeConfig.ZenRule rule;
        android.service.notification.ZenPolicy newZenPolicy;
        if (!android.app.Flags.modesApi()) {
            android.util.Log.wtf(TAG, "applyGlobalPolicyAsImplicitZenRule called with flag off!");
            return;
        }
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return;
            }
            android.service.notification.ZenModeConfig newConfig = this.mConfig.copy();
            android.service.notification.ZenModeConfig.ZenRule rule2 = (android.service.notification.ZenModeConfig.ZenRule) newConfig.automaticRules.get(implicitRuleId(callingPkg));
            if (rule2 != null) {
                isNew = false;
                rule = rule2;
            } else {
                android.service.notification.ZenModeConfig.ZenRule rule3 = newImplicitZenRule(callingPkg);
                rule3.zenMode = 1;
                newConfig.automaticRules.put(rule3.id, rule3);
                isNew = true;
                rule = rule3;
            }
            if (rule.zenPolicyUserModifiedFields == 0) {
                android.service.notification.ZenPolicy newZenPolicy2 = android.service.notification.ZenAdapters.notificationPolicyToZenPolicy(policy);
                if (!isNew) {
                    newZenPolicy = newZenPolicy2;
                } else {
                    newZenPolicy = this.mConfig.getZenPolicy().overwrittenWith(newZenPolicy2);
                }
                updatePolicy(rule, newZenPolicy, false, isNew);
                setConfigLocked(newConfig, null, 4, "applyGlobalPolicyAsImplicitZenRule", callingUid);
            }
        }
    }

    android.app.NotificationManager.Policy getNotificationPolicyFromImplicitZenRule(java.lang.String callingPkg) {
        if (!android.app.Flags.modesApi()) {
            android.util.Log.wtf(TAG, "getNotificationPolicyFromImplicitZenRule called with flag off!");
            return getNotificationPolicy();
        }
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return null;
            }
            android.service.notification.ZenModeConfig.ZenRule implicitRule = (android.service.notification.ZenModeConfig.ZenRule) this.mConfig.automaticRules.get(implicitRuleId(callingPkg));
            if (implicitRule != null && implicitRule.zenPolicy != null) {
                return this.mConfig.toNotificationPolicy(implicitRule.zenPolicy);
            }
            return getNotificationPolicy();
        }
    }

    private android.service.notification.ZenModeConfig.ZenRule newImplicitZenRule(final java.lang.String pkg) {
        final android.service.notification.ZenModeConfig.ZenRule rule = new android.service.notification.ZenModeConfig.ZenRule();
        rule.id = implicitRuleId(pkg);
        rule.pkg = pkg;
        rule.creationTime = this.mClock.millis();
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.notification.ZenModeHelper$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$newImplicitZenRule$0(pkg, rule);
            }
        });
        rule.type = 0;
        rule.triggerDescription = this.mContext.getString(android.R.string.years, rule.name);
        rule.condition = null;
        rule.conditionId = new android.net.Uri.Builder().scheme("condition").authority("android").appendPath("implicit").appendPath(pkg).build();
        rule.enabled = true;
        rule.modified = false;
        rule.component = null;
        rule.configurationActivity = null;
        return rule;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$newImplicitZenRule$0(java.lang.String pkg, android.service.notification.ZenModeConfig.ZenRule rule) throws java.lang.Exception {
        try {
            android.content.pm.ApplicationInfo applicationInfo = this.mPm.getApplicationInfo(pkg, 0);
            rule.name = applicationInfo.loadLabel(this.mPm).toString();
            rule.iconResName = drawableResIdToResName(pkg, applicationInfo.icon);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.w(TAG, "Package not found for creating implicit zen rule");
            rule.name = "Unknown";
        }
    }

    private static java.lang.String implicitRuleId(java.lang.String forPackage) {
        return IMPLICIT_RULE_ID_PREFIX + forPackage;
    }

    static boolean isImplicitRuleId(java.lang.String ruleId) {
        return ruleId.startsWith(IMPLICIT_RULE_ID_PREFIX);
    }

    boolean removeAutomaticZenRule(java.lang.String id, int origin, java.lang.String reason, int callingUid) {
        requirePublicOrigin("removeAutomaticZenRule", origin);
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return false;
            }
            android.service.notification.ZenModeConfig newConfig = this.mConfig.copy();
            android.service.notification.ZenModeConfig.ZenRule ruleToRemove = (android.service.notification.ZenModeConfig.ZenRule) newConfig.automaticRules.get(id);
            if (ruleToRemove == null) {
                return false;
            }
            if (canManageAutomaticZenRule(ruleToRemove)) {
                newConfig.automaticRules.remove(id);
                maybePreserveRemovedRule(newConfig, ruleToRemove, origin);
                if (ruleToRemove.getPkg() != null && !"android".equals(ruleToRemove.getPkg())) {
                    for (android.service.notification.ZenModeConfig.ZenRule currRule : newConfig.automaticRules.values()) {
                        if (currRule.getPkg() != null && currRule.getPkg().equals(ruleToRemove.getPkg())) {
                            break;
                        }
                    }
                    this.mRulesUidCache.remove(getPackageUserKey(ruleToRemove.getPkg(), newConfig.user));
                }
                if (DEBUG) {
                    android.util.Log.d(TAG, "removeZenRule zenRule=" + id + " reason=" + reason);
                }
                dispatchOnAutomaticRuleStatusChanged(this.mConfig.user, ruleToRemove.getPkg(), id, 3);
                return setConfigLocked(newConfig, origin, reason, null, true, callingUid);
            }
            throw new java.lang.SecurityException("Cannot delete rules not owned by your condition provider");
        }
    }

    boolean removeAutomaticZenRules(java.lang.String packageName, int origin, java.lang.String reason, int callingUid) {
        requirePublicOrigin("removeAutomaticZenRules", origin);
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return false;
            }
            android.service.notification.ZenModeConfig newConfig = this.mConfig.copy();
            for (int i = newConfig.automaticRules.size() - 1; i >= 0; i--) {
                android.service.notification.ZenModeConfig.ZenRule rule = (android.service.notification.ZenModeConfig.ZenRule) newConfig.automaticRules.get(newConfig.automaticRules.keyAt(i));
                if (java.util.Objects.equals(rule.getPkg(), packageName) && canManageAutomaticZenRule(rule)) {
                    newConfig.automaticRules.removeAt(i);
                    maybePreserveRemovedRule(newConfig, rule, origin);
                }
            }
            if (origin == 5) {
                for (int i2 = newConfig.deletedRules.size() - 1; i2 >= 0; i2--) {
                    if (java.util.Objects.equals(((android.service.notification.ZenModeConfig.ZenRule) newConfig.deletedRules.get(newConfig.deletedRules.keyAt(i2))).getPkg(), packageName)) {
                        newConfig.deletedRules.removeAt(i2);
                    }
                }
            }
            return setConfigLocked(newConfig, origin, reason, null, true, callingUid);
        }
    }

    private void maybePreserveRemovedRule(android.service.notification.ZenModeConfig config, android.service.notification.ZenModeConfig.ZenRule ruleToRemove, int origin) {
        java.lang.String deletedKey;
        if (android.app.Flags.modesApi() && origin == 4 && !ruleToRemove.canBeUpdatedByApp() && !"android".equals(ruleToRemove.pkg) && (deletedKey = android.service.notification.ZenModeConfig.deletedRuleKey(ruleToRemove)) != null) {
            android.service.notification.ZenModeConfig.ZenRule deletedRule = ruleToRemove.copy();
            deletedRule.deletionInstant = java.time.Instant.now(this.mClock);
            deletedRule.snoozing = false;
            deletedRule.condition = null;
            config.deletedRules.put(deletedKey, deletedRule);
        }
    }

    int getAutomaticZenRuleState(java.lang.String id) {
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return 2;
            }
            android.service.notification.ZenModeConfig.ZenRule rule = (android.service.notification.ZenModeConfig.ZenRule) this.mConfig.automaticRules.get(id);
            if (rule != null && canManageAutomaticZenRule(rule)) {
                return rule.condition != null ? rule.condition.state : 0;
            }
            return 2;
        }
    }

    void setAutomaticZenRuleState(java.lang.String id, android.service.notification.Condition condition, int origin, int callingUid) {
        requirePublicOrigin("setAutomaticZenRuleState", origin);
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return;
            }
            android.service.notification.ZenModeConfig newConfig = this.mConfig.copy();
            android.service.notification.ZenModeConfig.ZenRule rule = (android.service.notification.ZenModeConfig.ZenRule) newConfig.automaticRules.get(id);
            if (android.app.Flags.modesApi()) {
                if (rule != null && canManageAutomaticZenRule(rule)) {
                    setAutomaticZenRuleStateLocked(newConfig, java.util.Collections.singletonList(rule), condition, origin, callingUid);
                }
            } else {
                java.util.ArrayList<android.service.notification.ZenModeConfig.ZenRule> rules = new java.util.ArrayList<>();
                rules.add(rule);
                setAutomaticZenRuleStateLocked(newConfig, rules, condition, origin, callingUid);
            }
        }
    }

    void setAutomaticZenRuleState(android.net.Uri ruleDefinition, android.service.notification.Condition condition, int origin, int callingUid) {
        requirePublicOrigin("setAutomaticZenRuleState", origin);
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return;
            }
            android.service.notification.ZenModeConfig newConfig = this.mConfig.copy();
            java.util.List<android.service.notification.ZenModeConfig.ZenRule> matchingRules = findMatchingRules(newConfig, ruleDefinition, condition);
            if (android.app.Flags.modesApi()) {
                for (int i = matchingRules.size() - 1; i >= 0; i--) {
                    if (!canManageAutomaticZenRule(matchingRules.get(i))) {
                        matchingRules.remove(i);
                    }
                }
            }
            setAutomaticZenRuleStateLocked(newConfig, matchingRules, condition, origin, callingUid);
        }
    }

    private void setAutomaticZenRuleStateLocked(android.service.notification.ZenModeConfig config, java.util.List<android.service.notification.ZenModeConfig.ZenRule> rules, android.service.notification.Condition condition, int origin, int callingUid) {
        if (rules == null || rules.isEmpty()) {
            return;
        }
        if (android.app.Flags.modesApi() && condition.source == 1) {
            origin = 3;
        }
        for (android.service.notification.ZenModeConfig.ZenRule rule : rules) {
            rule.condition = condition;
            updateSnoozing(rule);
            setConfigLocked(config, rule.component, origin, "conditionChanged", callingUid);
        }
    }

    private static java.util.List<android.service.notification.ZenModeConfig.ZenRule> findMatchingRules(android.service.notification.ZenModeConfig config, android.net.Uri id, android.service.notification.Condition condition) {
        java.util.List<android.service.notification.ZenModeConfig.ZenRule> matchingRules = new java.util.ArrayList<>();
        if (ruleMatches(id, condition, config.manualRule)) {
            matchingRules.add(config.manualRule);
        } else {
            for (android.service.notification.ZenModeConfig.ZenRule automaticRule : config.automaticRules.values()) {
                if (ruleMatches(id, condition, automaticRule)) {
                    matchingRules.add(automaticRule);
                }
            }
        }
        return matchingRules;
    }

    private static boolean ruleMatches(android.net.Uri id, android.service.notification.Condition condition, android.service.notification.ZenModeConfig.ZenRule rule) {
        if (id == null || rule == null || rule.conditionId == null || !rule.conditionId.equals(id) || java.util.Objects.equals(condition, rule.condition)) {
            return false;
        }
        return true;
    }

    private boolean updateSnoozing(android.service.notification.ZenModeConfig.ZenRule rule) {
        if (rule == null || !rule.snoozing || rule.isTrueOrUnknown()) {
            return false;
        }
        rule.snoozing = false;
        if (DEBUG) {
            android.util.Log.d(TAG, "Snoozing reset for " + rule.conditionId);
            return true;
        }
        return true;
    }

    public int getCurrentInstanceCount(android.content.ComponentName cn) {
        if (cn == null) {
            return 0;
        }
        int count = 0;
        synchronized (this.mConfigLock) {
            for (android.service.notification.ZenModeConfig.ZenRule rule : this.mConfig.automaticRules.values()) {
                if (cn.equals(rule.component) || cn.equals(rule.configurationActivity)) {
                    count++;
                }
            }
        }
        return count;
    }

    private int getPackageRuleCount(java.lang.String pkg) {
        if (pkg == null) {
            return 0;
        }
        int count = 0;
        synchronized (this.mConfigLock) {
            for (android.service.notification.ZenModeConfig.ZenRule rule : this.mConfig.automaticRules.values()) {
                if (pkg.equals(rule.getPkg())) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean canManageAutomaticZenRule(android.service.notification.ZenModeConfig.ZenRule rule) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid == 0 || callingUid == 1000 || this.mContext.checkCallingPermission("android.permission.MANAGE_NOTIFICATIONS") == 0) {
            return true;
        }
        java.lang.String[] packages = this.mPm.getPackagesForUid(android.os.Binder.getCallingUid());
        if (packages != null) {
            for (java.lang.String str : packages) {
                if (str.equals(rule.getPkg())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    void updateZenRulesOnLocaleChange() {
        boolean updated;
        updateDefaultConfigAutomaticRules();
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return;
            }
            android.service.notification.ZenModeConfig config = this.mConfig.copy();
            boolean updated2 = false;
            for (android.service.notification.ZenModeConfig.ZenRule defaultRule : this.mDefaultConfig.automaticRules.values()) {
                android.service.notification.ZenModeConfig.ZenRule currRule = (android.service.notification.ZenModeConfig.ZenRule) config.automaticRules.get(defaultRule.id);
                if (currRule != null && !currRule.modified && (currRule.zenPolicyUserModifiedFields & 1) == 0 && !defaultRule.name.equals(currRule.name)) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Locale change - updating default zen rule name from " + currRule.name + " to " + defaultRule.name);
                    }
                    currRule.name = defaultRule.name;
                    updated2 = true;
                }
            }
            if (android.app.Flags.modesApi() && android.app.Flags.modesUi()) {
                for (android.service.notification.ZenModeConfig.ZenRule rule : config.automaticRules.values()) {
                    if (android.service.notification.SystemZenRules.isSystemOwnedRule(rule)) {
                        updated2 |= android.service.notification.SystemZenRules.updateTriggerDescription(this.mContext, rule);
                    }
                }
                updated = updated2;
            } else {
                updated = updated2;
            }
            if (updated) {
                setConfigLocked(config, null, 5, "updateZenRulesOnLocaleChange", 1000);
            }
        }
    }

    private android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName owner) {
        android.content.Intent queryIntent = new android.content.Intent();
        queryIntent.setComponent(owner);
        java.util.List<android.content.pm.ResolveInfo> installedServices = this.mPm.queryIntentServicesAsUser(queryIntent, 132, android.os.UserHandle.getCallingUserId());
        if (installedServices != null) {
            int count = installedServices.size();
            for (int i = 0; i < count; i++) {
                android.content.pm.ResolveInfo resolveInfo = installedServices.get(i);
                android.content.pm.ServiceInfo info = resolveInfo.serviceInfo;
                if (this.mServiceConfig.bindPermission.equals(info.permission)) {
                    return info;
                }
            }
            return null;
        }
        return null;
    }

    private android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName configActivity) {
        android.content.Intent queryIntent = new android.content.Intent();
        queryIntent.setComponent(configActivity);
        java.util.List<android.content.pm.ResolveInfo> installedComponents = this.mPm.queryIntentActivitiesAsUser(queryIntent, 129, android.os.UserHandle.getCallingUserId());
        if (installedComponents != null) {
            int count = installedComponents.size();
            if (0 < count) {
                android.content.pm.ResolveInfo resolveInfo = installedComponents.get(0);
                return resolveInfo.activityInfo;
            }
            return null;
        }
        return null;
    }

    private boolean populateZenRule(java.lang.String pkg, android.app.AutomaticZenRule azr, android.service.notification.ZenModeConfig.ZenRule rule, int origin, boolean isNew) {
        if (android.app.Flags.modesApi()) {
            boolean modified = false;
            if (isNew) {
                rule.id = android.service.notification.ZenModeConfig.newRuleId();
                rule.creationTime = this.mClock.millis();
                rule.component = azr.getOwner();
                rule.pkg = pkg;
                modified = true;
            }
            if (!java.util.Objects.equals(rule.conditionId, azr.getConditionId())) {
                rule.conditionId = azr.getConditionId();
                modified = true;
            }
            boolean shouldPreserveCondition = android.app.Flags.modesApi() && android.app.Flags.modesUi() && !isNew && origin == 3 && rule.enabled == azr.isEnabled() && rule.conditionId != null && rule.condition != null && rule.conditionId.equals(rule.condition.id);
            if (!shouldPreserveCondition) {
                rule.condition = null;
            }
            if (rule.enabled != azr.isEnabled()) {
                rule.enabled = azr.isEnabled();
                rule.snoozing = false;
                modified = true;
            }
            if (!java.util.Objects.equals(rule.configurationActivity, azr.getConfigurationActivity())) {
                rule.configurationActivity = azr.getConfigurationActivity();
                modified = true;
            }
            if (rule.allowManualInvocation != azr.isManualInvocationAllowed()) {
                rule.allowManualInvocation = azr.isManualInvocationAllowed();
                modified = true;
            }
            if (!android.app.Flags.modesUi()) {
                java.lang.String iconResName = drawableResIdToResName(rule.pkg, azr.getIconResId());
                if (!java.util.Objects.equals(rule.iconResName, iconResName)) {
                    rule.iconResName = iconResName;
                    modified = true;
                }
            }
            if (!java.util.Objects.equals(rule.triggerDescription, azr.getTriggerDescription())) {
                rule.triggerDescription = azr.getTriggerDescription();
                modified = true;
            }
            if (rule.type != azr.getType()) {
                rule.type = azr.getType();
                modified = true;
            }
            rule.modified = azr.isModified();
            java.lang.String previousName = rule.name;
            if (isNew || doesOriginAlwaysUpdateValues(origin) || (rule.userModifiedFields & 1) == 0) {
                rule.name = azr.getName();
                modified |= !java.util.Objects.equals(rule.name, previousName);
            }
            boolean updateValues = isNew || doesOriginAlwaysUpdateValues(origin) || rule.canBeUpdatedByApp();
            if (!updateValues) {
                return modified;
            }
            boolean updateBitmask = origin == 3;
            if (updateBitmask && !android.text.TextUtils.equals(previousName, azr.getName())) {
                rule.userModifiedFields |= 1;
            }
            int newZenMode = android.app.NotificationManager.zenModeFromInterruptionFilter(azr.getInterruptionFilter(), 0);
            if (rule.zenMode != newZenMode) {
                rule.zenMode = newZenMode;
                if (updateBitmask) {
                    rule.userModifiedFields |= 2;
                }
                modified = true;
            }
            if (android.app.Flags.modesUi()) {
                java.lang.String iconResName2 = drawableResIdToResName(rule.pkg, azr.getIconResId());
                if (!java.util.Objects.equals(rule.iconResName, iconResName2)) {
                    rule.iconResName = iconResName2;
                    if (updateBitmask) {
                        rule.userModifiedFields |= 4;
                    }
                    modified = true;
                }
            }
            return modified | updatePolicy(rule, azr.getZenPolicy(), updateBitmask, isNew) | updateZenDeviceEffects(rule, azr.getDeviceEffects(), origin == 4, updateBitmask);
        }
        boolean modified2 = rule.enabled;
        if (modified2 != azr.isEnabled()) {
            rule.snoozing = false;
        }
        rule.name = azr.getName();
        rule.condition = null;
        rule.conditionId = azr.getConditionId();
        rule.enabled = azr.isEnabled();
        rule.modified = azr.isModified();
        rule.zenPolicy = azr.getZenPolicy();
        rule.zenMode = android.app.NotificationManager.zenModeFromInterruptionFilter(azr.getInterruptionFilter(), 0);
        rule.configurationActivity = azr.getConfigurationActivity();
        if (isNew) {
            rule.id = android.service.notification.ZenModeConfig.newRuleId();
            rule.creationTime = java.lang.System.currentTimeMillis();
            rule.component = azr.getOwner();
            rule.pkg = pkg;
        }
        return true;
    }

    private static boolean doesOriginAlwaysUpdateValues(int origin) {
        return origin == 3 || origin == 5;
    }

    private boolean updatePolicy(android.service.notification.ZenModeConfig.ZenRule zenRule, android.service.notification.ZenPolicy newPolicy, boolean updateBitmask, boolean isNew) {
        android.service.notification.ZenPolicy oldPolicy;
        if (newPolicy == null) {
            if (isNew) {
                zenRule.zenPolicy = (android.app.Flags.modesUi() ? this.mDefaultConfig : this.mConfig).getZenPolicy();
                return true;
            }
            return false;
        }
        if (zenRule.zenPolicy != null) {
            oldPolicy = zenRule.zenPolicy;
        } else {
            oldPolicy = (android.app.Flags.modesUi() ? this.mDefaultConfig : this.mConfig).getZenPolicy();
        }
        android.service.notification.ZenPolicy newPolicy2 = oldPolicy.overwrittenWith(newPolicy);
        zenRule.zenPolicy = newPolicy2;
        if (updateBitmask) {
            int userModifiedFields = zenRule.zenPolicyUserModifiedFields;
            if (oldPolicy.getPriorityMessageSenders() != newPolicy2.getPriorityMessageSenders()) {
                userModifiedFields |= 1;
            }
            if (oldPolicy.getPriorityCallSenders() != newPolicy2.getPriorityCallSenders()) {
                userModifiedFields |= 2;
            }
            if (oldPolicy.getPriorityConversationSenders() != newPolicy2.getPriorityConversationSenders()) {
                userModifiedFields |= 4;
            }
            if (oldPolicy.getPriorityChannelsAllowed() != newPolicy2.getPriorityChannelsAllowed()) {
                userModifiedFields |= 8;
            }
            if (oldPolicy.getPriorityCategoryReminders() != newPolicy2.getPriorityCategoryReminders()) {
                userModifiedFields |= 16;
            }
            if (oldPolicy.getPriorityCategoryEvents() != newPolicy2.getPriorityCategoryEvents()) {
                userModifiedFields |= 32;
            }
            if (oldPolicy.getPriorityCategoryRepeatCallers() != newPolicy2.getPriorityCategoryRepeatCallers()) {
                userModifiedFields |= 64;
            }
            if (oldPolicy.getPriorityCategoryAlarms() != newPolicy2.getPriorityCategoryAlarms()) {
                userModifiedFields |= 128;
            }
            if (oldPolicy.getPriorityCategoryMedia() != newPolicy2.getPriorityCategoryMedia()) {
                userModifiedFields |= 256;
            }
            if (oldPolicy.getPriorityCategorySystem() != newPolicy2.getPriorityCategorySystem()) {
                userModifiedFields |= 512;
            }
            if (oldPolicy.getVisualEffectFullScreenIntent() != newPolicy2.getVisualEffectFullScreenIntent()) {
                userModifiedFields |= 1024;
            }
            if (oldPolicy.getVisualEffectLights() != newPolicy2.getVisualEffectLights()) {
                userModifiedFields |= 2048;
            }
            if (oldPolicy.getVisualEffectPeek() != newPolicy2.getVisualEffectPeek()) {
                userModifiedFields |= 4096;
            }
            if (oldPolicy.getVisualEffectStatusBar() != newPolicy2.getVisualEffectStatusBar()) {
                userModifiedFields |= 8192;
            }
            if (oldPolicy.getVisualEffectBadge() != newPolicy2.getVisualEffectBadge()) {
                userModifiedFields |= 16384;
            }
            if (oldPolicy.getVisualEffectAmbient() != newPolicy2.getVisualEffectAmbient()) {
                userModifiedFields |= 32768;
            }
            if (oldPolicy.getVisualEffectNotificationList() != newPolicy2.getVisualEffectNotificationList()) {
                userModifiedFields |= 65536;
            }
            zenRule.zenPolicyUserModifiedFields = userModifiedFields;
        }
        return true ^ newPolicy2.equals(oldPolicy);
    }

    private static boolean updateZenDeviceEffects(android.service.notification.ZenModeConfig.ZenRule zenRule, android.service.notification.ZenDeviceEffects newEffects, boolean isFromApp, boolean updateBitmask) {
        android.service.notification.ZenDeviceEffects oldEffects;
        if (newEffects == null) {
            return false;
        }
        if (zenRule.zenDeviceEffects != null) {
            oldEffects = zenRule.zenDeviceEffects;
        } else {
            oldEffects = new android.service.notification.ZenDeviceEffects.Builder().build();
        }
        if (isFromApp) {
            newEffects = new android.service.notification.ZenDeviceEffects.Builder(newEffects).setShouldDisableAutoBrightness(oldEffects.shouldDisableAutoBrightness()).setShouldDisableTapToWake(oldEffects.shouldDisableTapToWake()).setShouldDisableTiltToWake(oldEffects.shouldDisableTiltToWake()).setShouldDisableTouch(oldEffects.shouldDisableTouch()).setShouldMinimizeRadioUsage(oldEffects.shouldMinimizeRadioUsage()).setShouldMaximizeDoze(oldEffects.shouldMaximizeDoze()).setExtraEffects(oldEffects.getExtraEffects()).build();
        }
        zenRule.zenDeviceEffects = newEffects;
        if (updateBitmask) {
            int userModifiedFields = zenRule.zenDeviceEffectsUserModifiedFields;
            if (oldEffects.shouldDisplayGrayscale() != newEffects.shouldDisplayGrayscale()) {
                userModifiedFields |= 1;
            }
            if (oldEffects.shouldSuppressAmbientDisplay() != newEffects.shouldSuppressAmbientDisplay()) {
                userModifiedFields |= 2;
            }
            if (oldEffects.shouldDimWallpaper() != newEffects.shouldDimWallpaper()) {
                userModifiedFields |= 4;
            }
            if (oldEffects.shouldUseNightMode() != newEffects.shouldUseNightMode()) {
                userModifiedFields |= 8;
            }
            if (oldEffects.shouldDisableAutoBrightness() != newEffects.shouldDisableAutoBrightness()) {
                userModifiedFields |= 16;
            }
            if (oldEffects.shouldDisableTapToWake() != newEffects.shouldDisableTapToWake()) {
                userModifiedFields |= 32;
            }
            if (oldEffects.shouldDisableTiltToWake() != newEffects.shouldDisableTiltToWake()) {
                userModifiedFields |= 64;
            }
            if (oldEffects.shouldDisableTouch() != newEffects.shouldDisableTouch()) {
                userModifiedFields |= 128;
            }
            if (oldEffects.shouldMinimizeRadioUsage() != newEffects.shouldMinimizeRadioUsage()) {
                userModifiedFields |= 256;
            }
            if (oldEffects.shouldMaximizeDoze() != newEffects.shouldMaximizeDoze()) {
                userModifiedFields |= 512;
            }
            if (!java.util.Objects.equals(oldEffects.getExtraEffects(), newEffects.getExtraEffects())) {
                userModifiedFields |= 1024;
            }
            zenRule.zenDeviceEffectsUserModifiedFields = userModifiedFields;
        }
        return !newEffects.equals(oldEffects);
    }

    private android.app.AutomaticZenRule zenRuleToAutomaticZenRule(android.service.notification.ZenModeConfig.ZenRule rule) {
        if (android.app.Flags.modesApi()) {
            return new android.app.AutomaticZenRule.Builder(rule.name, rule.conditionId).setManualInvocationAllowed(rule.allowManualInvocation).setPackage(rule.pkg).setCreationTime(rule.creationTime).setIconResId(drawableResNameToResId(rule.pkg, rule.iconResName)).setType(rule.type).setZenPolicy(rule.zenPolicy).setDeviceEffects(rule.zenDeviceEffects).setEnabled(rule.enabled).setInterruptionFilter(android.app.NotificationManager.zenModeToInterruptionFilter(rule.zenMode)).setOwner(rule.component).setConfigurationActivity(rule.configurationActivity).setTriggerDescription(rule.triggerDescription).build();
        }
        android.app.AutomaticZenRule azr = new android.app.AutomaticZenRule(rule.name, rule.component, rule.configurationActivity, rule.conditionId, rule.zenPolicy, android.app.NotificationManager.zenModeToInterruptionFilter(rule.zenMode), rule.enabled, rule.creationTime);
        azr.setPackageName(rule.pkg);
        return azr;
    }

    void scheduleActivationBroadcast(java.lang.String pkg, int userId, java.lang.String ruleId, boolean activated) {
        int i;
        if (android.app.compat.CompatChanges.isChangeEnabled(SEND_ACTIVATION_AZR_STATUSES, pkg, android.os.UserHandle.of(userId))) {
            if (activated) {
                i = 4;
            } else {
                i = 5;
            }
            dispatchOnAutomaticRuleStatusChanged(userId, pkg, ruleId, i);
            return;
        }
        dispatchOnAutomaticRuleStatusChanged(userId, pkg, ruleId, -1);
    }

    void scheduleEnabledBroadcast(java.lang.String pkg, int userId, java.lang.String ruleId, boolean enabled) {
        int i;
        if (enabled) {
            i = 1;
        } else {
            i = 2;
        }
        dispatchOnAutomaticRuleStatusChanged(userId, pkg, ruleId, i);
    }

    void setManualZenMode(int zenMode, android.net.Uri conditionId, int origin, java.lang.String reason, java.lang.String caller, int callingUid) {
        setManualZenMode(zenMode, conditionId, origin, reason, caller, true, callingUid);
        android.provider.Settings.Secure.putInt(this.mContext.getContentResolver(), "show_zen_settings_suggestion", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setManualZenMode(int zenMode, android.net.Uri conditionId, int origin, java.lang.String reason, java.lang.String caller, boolean setRingerMode, int callingUid) throws java.lang.Throwable {
        synchronized (this.mConfigLock) {
            try {
                try {
                    if (this.mConfig == null) {
                        return;
                    }
                    if (android.provider.Settings.Global.isValidZenMode(zenMode)) {
                        if (DEBUG) {
                            try {
                                android.util.Log.d(TAG, "setManualZenMode " + android.provider.Settings.Global.zenModeToString(zenMode) + " conditionId=" + conditionId + " reason=" + reason + " setRingerMode=" + setRingerMode);
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                        android.service.notification.ZenModeConfig newConfig = this.mConfig.copy();
                        if (android.app.Flags.modesUi()) {
                            newConfig.manualRule.enabler = caller;
                            newConfig.manualRule.conditionId = conditionId != null ? conditionId : android.net.Uri.EMPTY;
                            newConfig.manualRule.pkg = "android";
                            newConfig.manualRule.zenMode = zenMode;
                            newConfig.manualRule.condition = new android.service.notification.Condition(newConfig.manualRule.conditionId, "", zenMode == 0 ? 0 : 1, origin == 3 ? 1 : 0);
                            if (zenMode == 0 && origin != 3) {
                                for (android.service.notification.ZenModeConfig.ZenRule automaticRule : newConfig.automaticRules.values()) {
                                    if (automaticRule.isAutomaticActive()) {
                                        automaticRule.snoozing = true;
                                    }
                                }
                            }
                        } else if (zenMode == 0) {
                            newConfig.manualRule = null;
                            for (android.service.notification.ZenModeConfig.ZenRule automaticRule2 : newConfig.automaticRules.values()) {
                                if (automaticRule2.isAutomaticActive()) {
                                    automaticRule2.snoozing = true;
                                }
                            }
                        } else {
                            android.service.notification.ZenModeConfig.ZenRule newRule = new android.service.notification.ZenModeConfig.ZenRule();
                            newRule.enabled = true;
                            newRule.zenMode = zenMode;
                            newRule.conditionId = conditionId;
                            newRule.enabler = caller;
                            if (android.app.Flags.modesApi()) {
                                newRule.allowManualInvocation = true;
                            }
                            newConfig.manualRule = newRule;
                        }
                        setConfigLocked(newConfig, origin, reason, null, setRingerMode, callingUid);
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    public void setManualZenRuleDeviceEffects(android.service.notification.ZenDeviceEffects deviceEffects, int origin, java.lang.String reason, int callingUid) {
        if (!android.app.Flags.modesUi()) {
            return;
        }
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return;
            }
            if (DEBUG) {
                android.util.Log.d(TAG, "updateManualRule " + deviceEffects + " reason=" + reason + " callingUid=" + callingUid);
            }
            android.service.notification.ZenModeConfig newConfig = this.mConfig.copy();
            newConfig.manualRule.pkg = "android";
            newConfig.manualRule.zenDeviceEffects = deviceEffects;
            setConfigLocked(newConfig, origin, reason, null, true, callingUid);
        }
    }

    void dump(android.util.proto.ProtoOutputStream proto) {
        proto.write(1159641169921L, this.mZenMode);
        synchronized (this.mConfigLock) {
            if (this.mConfig.manualRule != null) {
                this.mConfig.manualRule.dumpDebug(proto, 2246267895810L);
            }
            for (android.service.notification.ZenModeConfig.ZenRule rule : this.mConfig.automaticRules.values()) {
                if (rule.enabled && rule.condition != null && rule.condition.state == 1 && !rule.snoozing) {
                    rule.dumpDebug(proto, 2246267895810L);
                }
            }
            this.mConfig.toNotificationPolicy().dumpDebug(proto, 1146756268037L);
            proto.write(1120986464259L, this.mSuppressedEffects);
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("mZenMode=");
        pw.println(android.provider.Settings.Global.zenModeToString(this.mZenMode));
        pw.print(prefix);
        pw.println("mConsolidatedPolicy=" + this.mConsolidatedPolicy.toString());
        synchronized (this.mConfigsArrayLock) {
            int N = this.mConfigs.size();
            for (int i = 0; i < N; i++) {
                dump(pw, prefix, "mConfigs[u=" + this.mConfigs.keyAt(i) + "]", this.mConfigs.valueAt(i));
            }
        }
        pw.print(prefix);
        pw.print("mUser=");
        pw.println(this.mUser);
        synchronized (this.mConfigLock) {
            dump(pw, prefix, "mConfig", this.mConfig);
        }
        pw.print(prefix);
        pw.print("mSuppressedEffects=");
        pw.println(this.mSuppressedEffects);
        this.mFiltering.dump(pw, prefix);
        this.mConditions.dump(pw, prefix);
    }

    private static void dump(java.io.PrintWriter pw, java.lang.String prefix, java.lang.String var, android.service.notification.ZenModeConfig config) {
        pw.print(prefix);
        pw.print(var);
        pw.print('=');
        pw.println(config);
    }

    public void readXml(com.android.modules.utils.TypedXmlPullParser parser, boolean forRestore, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        boolean allRulesDisabled;
        java.lang.String reason;
        android.service.notification.ZenModeConfig config;
        android.service.notification.ZenModeConfig config2 = android.service.notification.ZenModeConfig.readXml(parser);
        if (config2 != null) {
            if (forRestore) {
                config2.user = userId;
                if (!android.app.Flags.modesUi()) {
                    config2.manualRule = null;
                }
            }
            boolean allRulesDisabled2 = true;
            boolean hasDefaultRules = config2.automaticRules.containsAll(android.service.notification.ZenModeConfig.DEFAULT_RULE_IDS);
            long time = android.app.Flags.modesApi() ? this.mClock.millis() : java.lang.System.currentTimeMillis();
            if (config2.automaticRules != null && config2.automaticRules.size() > 0) {
                for (android.service.notification.ZenModeConfig.ZenRule automaticRule : config2.automaticRules.values()) {
                    if (forRestore) {
                        automaticRule.snoozing = false;
                        automaticRule.condition = null;
                        automaticRule.creationTime = time;
                    }
                    allRulesDisabled2 &= !automaticRule.enabled;
                    if (android.app.Flags.modesApi() && config2.version < 11) {
                        android.service.notification.ZenPolicy manualRulePolicy = config2.getZenPolicy();
                        if (automaticRule.zenPolicy == null) {
                            automaticRule.zenPolicy = manualRulePolicy;
                        } else {
                            automaticRule.zenPolicy = manualRulePolicy.overwrittenWith(automaticRule.zenPolicy);
                        }
                    }
                }
                allRulesDisabled = allRulesDisabled2;
            } else {
                allRulesDisabled = true;
            }
            if (hasDefaultRules || !allRulesDisabled || (!forRestore && config2.version >= 8)) {
                reason = "readXml";
            } else {
                config2.automaticRules = new android.util.ArrayMap();
                for (android.service.notification.ZenModeConfig.ZenRule rule : this.mDefaultConfig.automaticRules.values()) {
                    config2.automaticRules.put(rule.id, rule);
                }
                java.lang.String reason2 = "readXml, reset to default rules";
                reason = reason2;
            }
            if (android.app.Flags.modesApi() && android.app.Flags.modesUi()) {
                android.service.notification.SystemZenRules.maybeUpgradeRules(this.mContext, config2);
            }
            if (this.mZenModeHelperExt == null) {
                config = config2;
            } else {
                config = this.mZenModeHelperExt.adjustZenModeConfig(config2);
            }
            int userId2 = userId != -1 ? userId : 0;
            if (config.version < 8) {
                android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "show_zen_upgrade_notification", 1, userId2);
            } else {
                android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "zen_settings_updated", 1, userId2);
            }
            if (android.app.Flags.modesApi() && forRestore) {
                config.deletedRules.clear();
            }
            if (DEBUG) {
                android.util.Log.d(TAG, reason);
            }
            synchronized (this.mConfigLock) {
                setConfigLocked(config, null, forRestore ? 6 : 1, reason, 1000);
            }
        }
    }

    public void writeXml(com.android.modules.utils.TypedXmlSerializer out, boolean forBackup, java.lang.Integer version, int userId) throws java.io.IOException {
        synchronized (this.mConfigsArrayLock) {
            int n = this.mConfigs.size();
            for (int i = 0; i < n; i++) {
                if (!forBackup || this.mConfigs.keyAt(i) == userId) {
                    this.mConfigs.valueAt(i).writeXml(out, version, forBackup);
                }
            }
        }
    }

    public android.app.NotificationManager.Policy getNotificationPolicy() {
        android.app.NotificationManager.Policy notificationPolicy;
        synchronized (this.mConfigLock) {
            notificationPolicy = getNotificationPolicy(this.mConfig);
        }
        return notificationPolicy;
    }

    private static android.app.NotificationManager.Policy getNotificationPolicy(android.service.notification.ZenModeConfig config) {
        if (config == null) {
            return null;
        }
        return config.toNotificationPolicy();
    }

    public void setNotificationPolicy(android.app.NotificationManager.Policy policy, int origin, int callingUid) {
        synchronized (this.mConfigLock) {
            if (policy != null) {
                if (this.mConfig != null) {
                    android.service.notification.ZenModeConfig newConfig = this.mConfig.copy();
                    if (android.app.Flags.modesApi() && !android.app.Flags.modesUi()) {
                        android.service.notification.ZenPolicy previousPolicy = android.service.notification.ZenAdapters.notificationPolicyToZenPolicy(newConfig.toNotificationPolicy());
                        android.service.notification.ZenPolicy newPolicy = android.service.notification.ZenAdapters.notificationPolicyToZenPolicy(policy);
                        newConfig.applyNotificationPolicy(policy);
                        if (!previousPolicy.equals(newPolicy)) {
                            for (android.service.notification.ZenModeConfig.ZenRule rule : newConfig.automaticRules.values()) {
                                if ((this.mZenModeHelperExt != null && this.mZenModeHelperExt.isOplusRule()) || (!android.service.notification.SystemZenRules.isSystemOwnedRule(rule) && rule.zenMode == 1 && (rule.zenPolicy == null || rule.zenPolicy.equals(previousPolicy) || rule.zenPolicy.equals(getDefaultZenPolicy())))) {
                                    rule.zenPolicy = newPolicy;
                                }
                            }
                        }
                    } else {
                        newConfig.applyNotificationPolicy(policy);
                    }
                    setConfigLocked(newConfig, null, origin, "setNotificationPolicy", callingUid);
                }
            }
        }
    }

    private void cleanUpZenRules() {
        java.time.Instant keptRuleThreshold = this.mClock.instant().minus((java.time.temporal.TemporalAmount) DELETED_RULE_KEPT_FOR);
        synchronized (this.mConfigLock) {
            android.service.notification.ZenModeConfig newConfig = this.mConfig.copy();
            deleteRulesWithoutOwner(newConfig.automaticRules);
            if (android.app.Flags.modesApi()) {
                deleteRulesWithoutOwner(newConfig.deletedRules);
                for (int i = newConfig.deletedRules.size() - 1; i >= 0; i--) {
                    android.service.notification.ZenModeConfig.ZenRule deletedRule = (android.service.notification.ZenModeConfig.ZenRule) newConfig.deletedRules.valueAt(i);
                    if (deletedRule.deletionInstant == null || deletedRule.deletionInstant.isBefore(keptRuleThreshold)) {
                        newConfig.deletedRules.removeAt(i);
                    }
                }
            }
            if (!newConfig.equals(this.mConfig)) {
                setConfigLocked(newConfig, null, 5, "cleanUpZenRules", 1000);
            }
        }
    }

    private void deleteRulesWithoutOwner(android.util.ArrayMap<java.lang.String, android.service.notification.ZenModeConfig.ZenRule> ruleList) {
        long currentTime = android.app.Flags.modesApi() ? this.mClock.millis() : java.lang.System.currentTimeMillis();
        if (ruleList != null) {
            for (int i = ruleList.size() - 1; i >= 0; i--) {
                android.service.notification.ZenModeConfig.ZenRule rule = ruleList.valueAt(i);
                if (259200000 < currentTime - rule.creationTime) {
                    try {
                        if (rule.getPkg() != null) {
                            this.mPm.getPackageInfo(rule.getPkg(), 4194304);
                        }
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        ruleList.removeAt(i);
                    }
                }
            }
        }
    }

    public android.service.notification.ZenModeConfig getConfig() {
        android.service.notification.ZenModeConfig zenModeConfigCopy;
        synchronized (this.mConfigLock) {
            zenModeConfigCopy = this.mConfig.copy();
        }
        return zenModeConfigCopy;
    }

    public android.app.NotificationManager.Policy getConsolidatedNotificationPolicy() {
        return this.mConsolidatedPolicy.copy();
    }

    protected android.service.notification.ZenPolicy getDefaultZenPolicy() {
        return this.mDefaultConfig.getZenPolicy();
    }

    private boolean setConfigLocked(android.service.notification.ZenModeConfig config, android.content.ComponentName triggeringComponent, int origin, java.lang.String reason, int callingUid) {
        return setConfigLocked(config, origin, reason, triggeringComponent, true, callingUid);
    }

    void setConfig(android.service.notification.ZenModeConfig config, android.content.ComponentName triggeringComponent, int origin, java.lang.String reason, int callingUid) {
        synchronized (this.mConfigLock) {
            setConfigLocked(config, triggeringComponent, origin, reason, callingUid);
        }
    }

    private boolean setConfigLocked(android.service.notification.ZenModeConfig config, int origin, java.lang.String reason, android.content.ComponentName triggeringComponent, boolean setRingerMode, int callingUid) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            try {
                if (config != null) {
                    try {
                        if (config.isValid()) {
                            if (config.user != this.mUser) {
                                synchronized (this.mConfigsArrayLock) {
                                    this.mConfigs.put(config.user, config);
                                }
                                if (DEBUG) {
                                    android.util.Log.d(TAG, "setConfigLocked: store config for user " + config.user);
                                }
                                android.os.Binder.restoreCallingIdentity(identity);
                                return true;
                            }
                            this.mConditions.evaluateConfig(config, null, false);
                            synchronized (this.mConfigsArrayLock) {
                                try {
                                    this.mConfigs.put(config.user, config);
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    while (true) {
                                        try {
                                            throw th;
                                        } catch (java.lang.Throwable th2) {
                                            th = th2;
                                        }
                                    }
                                }
                            }
                            if (DEBUG) {
                                android.util.Log.d(TAG, "setConfigLocked reason=" + reason, new java.lang.Throwable());
                            }
                            com.android.server.notification.ZenLog.traceConfig(reason, triggeringComponent, this.mConfig, config, callingUid);
                            android.app.NotificationManager.Policy newPolicy = getNotificationPolicy(config);
                            boolean policyChanged = !java.util.Objects.equals(getNotificationPolicy(this.mConfig), newPolicy);
                            if (policyChanged) {
                                dispatchOnPolicyChanged(newPolicy);
                            }
                            updateConfigAndZenModeLocked(config, origin, reason, setRingerMode, callingUid);
                            this.mConditions.evaluateConfig(config, triggeringComponent, true);
                            android.os.Binder.restoreCallingIdentity(identity);
                            return true;
                        }
                    } catch (java.lang.SecurityException e) {
                        e = e;
                        android.util.Log.wtf(TAG, "Invalid rule in config", e);
                        android.os.Binder.restoreCallingIdentity(identity);
                        return false;
                    } catch (java.lang.Throwable th3) {
                        e = th3;
                        android.os.Binder.restoreCallingIdentity(identity);
                        throw e;
                    }
                }
                android.util.Log.w(TAG, "Invalid config in setConfigLocked; " + config);
                android.os.Binder.restoreCallingIdentity(identity);
                return false;
            } catch (java.lang.Throwable th4) {
                e = th4;
            }
        } catch (java.lang.SecurityException e2) {
            e = e2;
        }
    }

    private void updateConfigAndZenModeLocked(android.service.notification.ZenModeConfig config, int origin, java.lang.String reason, boolean setRingerMode, int callingUid) {
        boolean logZenModeEvents = this.mFlagResolver.isEnabled(com.android.internal.config.sysui.SystemUiSystemPropertiesFlags.NotificationFlags.LOG_DND_STATE_EVENTS);
        com.android.server.notification.ZenModeEventLogger.ZenModeInfo prevInfo = null;
        if (logZenModeEvents) {
            prevInfo = new com.android.server.notification.ZenModeEventLogger.ZenModeInfo(this.mZenMode, this.mConfig, this.mConsolidatedPolicy);
        }
        if (!config.equals(this.mConfig)) {
            if (android.app.Flags.modesApi() && origin != 1) {
                for (android.service.notification.ZenModeConfig.ZenRule rule : config.automaticRules.values()) {
                    android.service.notification.ZenModeConfig.ZenRule original = (android.service.notification.ZenModeConfig.ZenRule) this.mConfig.automaticRules.get(rule.id);
                    if (original != null) {
                        if (original.enabled != rule.enabled) {
                            scheduleEnabledBroadcast(rule.getPkg(), config.user, rule.id, rule.enabled);
                        }
                        if (original.isAutomaticActive() != rule.isAutomaticActive()) {
                            scheduleActivationBroadcast(rule.getPkg(), config.user, rule.id, rule.isAutomaticActive());
                        }
                    }
                }
            }
            this.mConfig = config;
            dispatchOnConfigChanged();
            updateAndApplyConsolidatedPolicyAndDeviceEffects(origin, reason);
        }
        java.lang.String val = java.lang.Integer.toString(config.hashCode());
        android.provider.Settings.Global.putString(this.mContext.getContentResolver(), "zen_mode_config_etag", val);
        this.mCaller = this.mContext.getPackageManager().getNameForUid(callingUid);
        evaluateZenModeLocked(origin, reason, setRingerMode);
        if (logZenModeEvents) {
            com.android.server.notification.ZenModeEventLogger.ZenModeInfo newInfo = new com.android.server.notification.ZenModeEventLogger.ZenModeInfo(this.mZenMode, this.mConfig, this.mConsolidatedPolicy);
            this.mZenModeEventLogger.maybeLogZenChange(prevInfo, newInfo, callingUid, origin);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getZenModeSetting() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "zen_mode", 0);
    }

    protected void setZenModeSetting(int zen) {
        android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "zen_mode", zen);
        com.android.server.notification.ZenLog.traceSetZenMode(android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "zen_mode", -1), "updated setting");
        showZenUpgradeNotification(zen);
    }

    private int getPreviousRingerModeSetting() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "zen_mode_ringer_level", 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPreviousRingerModeSetting(java.lang.Integer previousRingerLevel) {
        android.provider.Settings.Global.putString(this.mContext.getContentResolver(), "zen_mode_ringer_level", previousRingerLevel == null ? null : java.lang.Integer.toString(previousRingerLevel.intValue()));
    }

    protected void evaluateZenModeLocked(int origin, java.lang.String reason, boolean setRingerMode) {
        if (DEBUG) {
            android.util.Log.d(TAG, "evaluateZenMode");
        }
        if (this.mConfig == null) {
            return;
        }
        boolean shouldApplyToRinger = false;
        int policyHashBefore = this.mConsolidatedPolicy == null ? 0 : this.mConsolidatedPolicy.hashCode();
        int zenBefore = this.mZenMode;
        int zen = computeZenMode();
        com.android.server.notification.ZenLog.traceSetZenMode(zen, reason);
        this.mZenMode = zen;
        setZenModeSetting(this.mZenMode);
        if (this.mZenModeHelperExt != null) {
            java.lang.String zenExtInfoStr = java.lang.System.currentTimeMillis() + "," + this.mCaller + "," + this.mConfig.user + "," + zenBefore + "," + this.mZenMode + "," + this.mIsAuto + "," + this.mRuleId + "," + reason;
            this.mZenModeHelperExt.setZenModeExtInfoStr(this.mContext, zenExtInfoStr);
        }
        this.mCaller = null;
        this.mIsAuto = 0;
        this.mRuleId = null;
        updateAndApplyConsolidatedPolicyAndDeviceEffects(origin, reason);
        if (setRingerMode && (zen != zenBefore || (zen == 1 && policyHashBefore != this.mConsolidatedPolicy.hashCode()))) {
            shouldApplyToRinger = true;
        }
        this.mHandler.postUpdateRingerAndAudio(shouldApplyToRinger);
        if (zen != zenBefore) {
            this.mHandler.postDispatchOnZenModeChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRingerAndAudio(boolean shouldApplyToRinger) {
        if (this.mAudioManager != null) {
            this.mAudioManager.updateRingerModeAffectedStreamsInternal();
        }
        if (shouldApplyToRinger) {
            applyZenToRingerMode();
        }
        applyRestrictions();
    }

    private int computeZenMode() {
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return 0;
            }
            if (this.mConfig.isManualActive()) {
                return this.mConfig.manualRule.zenMode;
            }
            int zen = 0;
            for (android.service.notification.ZenModeConfig.ZenRule automaticRule : this.mConfig.automaticRules.values()) {
                if (automaticRule.isAutomaticActive()) {
                    if (zenSeverity(automaticRule.zenMode) > zenSeverity(zen)) {
                        if (android.provider.Settings.Secure.getInt(this.mContext.getContentResolver(), "zen_settings_suggestion_viewed", 1) == 0) {
                            android.provider.Settings.Secure.putInt(this.mContext.getContentResolver(), "show_zen_settings_suggestion", 1);
                        }
                        zen = automaticRule.zenMode;
                    }
                    this.mIsAuto = 1;
                    this.mRuleId = automaticRule.id;
                    this.mCaller = automaticRule.pkg;
                }
            }
            return zen;
        }
    }

    private void applyCustomPolicy(android.service.notification.ZenPolicy policy, android.service.notification.ZenModeConfig.ZenRule rule, boolean useManualConfig) {
        if (rule.zenMode == 2) {
            policy.apply(new android.service.notification.ZenPolicy.Builder().disallowAllSounds().allowPriorityChannels(false).build());
            return;
        }
        if (rule.zenMode == 3) {
            policy.apply(new android.service.notification.ZenPolicy.Builder().disallowAllSounds().allowAlarms(true).allowMedia(true).allowPriorityChannels(false).build());
            return;
        }
        if (rule.zenPolicy != null) {
            policy.apply(rule.zenPolicy);
            return;
        }
        if (android.app.Flags.modesApi()) {
            if (useManualConfig) {
                policy.apply(this.mConfig.getZenPolicy());
                return;
            } else {
                android.util.Log.wtf(TAG, "active automatic rule found with no specified policy: " + rule);
                policy.apply((android.app.Flags.modesUi() ? this.mDefaultConfig : this.mConfig).getZenPolicy());
                return;
            }
        }
        policy.apply(this.mConfig.getZenPolicy());
    }

    private void updateAndApplyConsolidatedPolicyAndDeviceEffects(int origin, java.lang.String reason) {
        synchronized (this.mConfigLock) {
            if (this.mConfig == null) {
                return;
            }
            android.service.notification.ZenPolicy policy = new android.service.notification.ZenPolicy();
            android.service.notification.ZenDeviceEffects.Builder deviceEffectsBuilder = new android.service.notification.ZenDeviceEffects.Builder();
            if (this.mConfig.isManualActive()) {
                applyCustomPolicy(policy, this.mConfig.manualRule, true);
                if (android.app.Flags.modesApi()) {
                    deviceEffectsBuilder.add(this.mConfig.manualRule.zenDeviceEffects);
                }
            }
            for (android.service.notification.ZenModeConfig.ZenRule automaticRule : this.mConfig.automaticRules.values()) {
                if (automaticRule.isAutomaticActive()) {
                    if (!android.app.Flags.modesApi() || automaticRule.zenMode != 0) {
                        applyCustomPolicy(policy, automaticRule, false);
                    }
                    if (android.app.Flags.modesApi()) {
                        deviceEffectsBuilder.add(automaticRule.zenDeviceEffects);
                    }
                }
            }
            android.app.NotificationManager.Policy newPolicy = this.mConfig.toNotificationPolicy(policy);
            if (!java.util.Objects.equals(this.mConsolidatedPolicy, newPolicy)) {
                this.mConsolidatedPolicy = newPolicy;
                dispatchOnConsolidatedPolicyChanged(newPolicy);
                com.android.server.notification.ZenLog.traceSetConsolidatedZenPolicy(this.mConsolidatedPolicy, reason);
            }
            if (android.app.Flags.modesApi()) {
                android.service.notification.ZenDeviceEffects deviceEffects = deviceEffectsBuilder.build();
                if (!deviceEffects.equals(this.mConsolidatedDeviceEffects)) {
                    this.mConsolidatedDeviceEffects = deviceEffects;
                    this.mHandler.postApplyDeviceEffects(origin);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyConsolidatedDeviceEffects(int source) {
        android.service.notification.DeviceEffectsApplier applier;
        android.service.notification.ZenDeviceEffects effects;
        if (!android.app.Flags.modesApi()) {
            return;
        }
        synchronized (this.mConfigLock) {
            applier = this.mDeviceEffectsApplier;
            effects = this.mConsolidatedDeviceEffects;
        }
        if (applier != null) {
            applier.apply(effects, source);
        }
    }

    private void updateDefaultConfigAutomaticRules() {
        for (android.service.notification.ZenModeConfig.ZenRule rule : this.mDefaultConfig.automaticRules.values()) {
            if ("EVENTS_DEFAULT_RULE".equals(rule.id)) {
                rule.name = this.mContext.getResources().getString(android.R.string.window_magnification_prompt_content);
            } else if ("EVERY_NIGHT_DEFAULT_RULE".equals(rule.id)) {
                rule.name = this.mContext.getResources().getString(android.R.string.window_magnification_prompt_title);
            }
            if (android.app.Flags.modesApi() && android.app.Flags.modesUi()) {
                android.service.notification.SystemZenRules.updateTriggerDescription(this.mContext, rule);
            }
        }
    }

    private void updateDefaultAutomaticRulePolicies() {
        if (!android.app.Flags.modesApi()) {
            return;
        }
        android.service.notification.ZenPolicy defaultPolicy = this.mDefaultConfig.getZenPolicy();
        for (android.service.notification.ZenModeConfig.ZenRule rule : this.mDefaultConfig.automaticRules.values()) {
            if (android.service.notification.ZenModeConfig.DEFAULT_RULE_IDS.contains(rule.id) && rule.zenPolicy == null) {
                rule.zenPolicy = defaultPolicy.copy();
            }
        }
    }

    protected void applyRestrictions() {
        boolean muteEverything;
        boolean muteEverything2;
        boolean muteNotifications;
        if (this.mZenModeHelperExt != null && this.mZenModeHelperExt.applyRestrictions(this.mZenMode, this.mSuppressedEffects, this.mConsolidatedPolicy, this.mPriorityOnlyDndExemptPackages)) {
            return;
        }
        boolean zenOn = this.mZenMode != 0;
        boolean zenPriorityOnly = this.mZenMode == 1;
        boolean zenSilence = this.mZenMode == 2;
        boolean zenAlarmsOnly = this.mZenMode == 3;
        boolean allowCalls = this.mConsolidatedPolicy.allowCalls() && this.mConsolidatedPolicy.allowCallsFrom() == 0;
        boolean allowRepeatCallers = this.mConsolidatedPolicy.allowRepeatCallers();
        boolean allowSystem = this.mConsolidatedPolicy.allowSystem();
        boolean allowMedia = this.mConsolidatedPolicy.allowMedia();
        boolean allowAlarms = this.mConsolidatedPolicy.allowAlarms();
        boolean muteNotifications2 = zenOn || (this.mSuppressedEffects & 1) != 0;
        boolean muteCalls = zenAlarmsOnly || !((!zenPriorityOnly || allowCalls || allowRepeatCallers) && (this.mSuppressedEffects & 2) == 0);
        boolean muteAlarms = zenPriorityOnly && !allowAlarms;
        boolean muteMedia = zenPriorityOnly && !allowMedia;
        boolean muteSystem = zenAlarmsOnly || (zenPriorityOnly && !allowSystem);
        boolean muteEverything3 = zenSilence || (zenPriorityOnly && android.service.notification.ZenModeConfig.areAllZenBehaviorSoundsMuted(this.mConsolidatedPolicy));
        int[] array = android.media.AudioAttributes.SDK_USAGES.toArray();
        int length = array.length;
        boolean zenAlarmsOnly2 = muteEverything3;
        int i = 0;
        while (i < length) {
            int i2 = length;
            int usage = array[i];
            boolean zenSilence2 = zenSilence;
            int suppressionBehavior = android.media.AudioAttributes.SUPPRESSIBLE_USAGES.get(usage);
            int[] iArr = array;
            if (suppressionBehavior == 3) {
                muteEverything = zenAlarmsOnly2;
                muteEverything2 = zenAlarmsOnly;
                applyRestrictions(zenPriorityOnly, false, usage);
                muteNotifications = muteNotifications2;
            } else {
                muteEverything = zenAlarmsOnly2;
                muteEverything2 = zenAlarmsOnly;
                boolean zenAlarmsOnly3 = true;
                if (suppressionBehavior == 1) {
                    if (!muteNotifications2 && !muteEverything) {
                        zenAlarmsOnly3 = false;
                    }
                    applyRestrictions(zenPriorityOnly, zenAlarmsOnly3, usage);
                    muteNotifications = muteNotifications2;
                } else if (suppressionBehavior == 2) {
                    applyRestrictions(zenPriorityOnly, muteCalls || muteEverything, usage);
                    muteNotifications = muteNotifications2;
                } else if (suppressionBehavior == 4) {
                    applyRestrictions(zenPriorityOnly, muteAlarms || muteEverything, usage);
                    muteNotifications = muteNotifications2;
                } else if (suppressionBehavior == 5) {
                    applyRestrictions(zenPriorityOnly, muteMedia || muteEverything, usage);
                    muteNotifications = muteNotifications2;
                } else if (suppressionBehavior == 6) {
                    if (usage == 13) {
                        muteNotifications = muteNotifications2;
                        applyRestrictions(zenPriorityOnly, muteSystem || muteEverything, usage, 28);
                        applyRestrictions(zenPriorityOnly, false, usage, 3);
                    } else {
                        muteNotifications = muteNotifications2;
                        applyRestrictions(zenPriorityOnly, muteSystem || muteEverything, usage);
                    }
                } else {
                    muteNotifications = muteNotifications2;
                    applyRestrictions(zenPriorityOnly, muteEverything, usage);
                }
            }
            i++;
            zenAlarmsOnly = muteEverything2;
            length = i2;
            zenSilence = zenSilence2;
            muteNotifications2 = muteNotifications;
            zenAlarmsOnly2 = muteEverything;
            array = iArr;
        }
    }

    protected void applyRestrictions(boolean z, boolean z2, int i, int i2) {
        long jClearCallingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            this.mAppOps.setRestriction(i2, i, z2 ? 1 : 0, z ? this.mPriorityOnlyDndExemptPackages : null);
        } finally {
            android.os.Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    protected void applyRestrictions(boolean zenPriorityOnly, boolean mute, int usage) {
        applyRestrictions(zenPriorityOnly, mute, usage, 3);
        applyRestrictions(zenPriorityOnly, mute, usage, 28);
    }

    protected void applyZenToRingerMode() {
        if (this.mAudioManager == null) {
            return;
        }
        int ringerModeInternal = this.mAudioManager.getRingerModeInternal();
        int newRingerModeInternal = ringerModeInternal;
        switch (this.mZenMode) {
            case 0:
                if (ringerModeInternal == 0) {
                    newRingerModeInternal = getPreviousRingerModeSetting();
                    setPreviousRingerModeSetting(null);
                }
                break;
            case 2:
            case 3:
                if (ringerModeInternal != 0) {
                    setPreviousRingerModeSetting(java.lang.Integer.valueOf(ringerModeInternal));
                    newRingerModeInternal = 0;
                }
                break;
        }
        if (newRingerModeInternal != -1) {
            this.mAudioManager.setRingerModeInternal(newRingerModeInternal, TAG);
        }
    }

    private void dispatchOnConfigChanged() {
        for (com.android.server.notification.ZenModeHelper.Callback callback : this.mCallbacks) {
            callback.onConfigChanged();
        }
    }

    private void dispatchOnPolicyChanged(android.app.NotificationManager.Policy newPolicy) {
        for (com.android.server.notification.ZenModeHelper.Callback callback : this.mCallbacks) {
            callback.onPolicyChanged(newPolicy);
        }
    }

    private void dispatchOnConsolidatedPolicyChanged(android.app.NotificationManager.Policy newConsolidatedPolicy) {
        for (com.android.server.notification.ZenModeHelper.Callback callback : this.mCallbacks) {
            callback.onConsolidatedPolicyChanged(newConsolidatedPolicy);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchOnZenModeChanged() {
        for (com.android.server.notification.ZenModeHelper.Callback callback : this.mCallbacks) {
            callback.onZenModeChanged();
        }
    }

    private void dispatchOnAutomaticRuleStatusChanged(int userId, java.lang.String pkg, java.lang.String id, int status) {
        for (com.android.server.notification.ZenModeHelper.Callback callback : this.mCallbacks) {
            callback.onAutomaticRuleStatusChanged(userId, pkg, id, status);
        }
    }

    private android.service.notification.ZenModeConfig readDefaultConfig(android.content.res.Resources resources) {
        android.content.res.XmlResourceParser parser = null;
        try {
            try {
                parser = resources.getXml(android.R.xml.default_zen_mode_config);
                while (parser.next() != 1) {
                    android.service.notification.ZenModeConfig config = android.service.notification.ZenModeConfig.readXml(com.android.internal.util.XmlUtils.makeTyped(parser));
                    if (config != null) {
                        return config;
                    }
                }
            } catch (java.lang.Exception e) {
                android.util.Log.w(TAG, "Error reading default zen mode config from resource", e);
            }
            return new android.service.notification.ZenModeConfig();
        } finally {
            libcore.io.IoUtils.closeQuietly(parser);
        }
    }

    private static int zenSeverity(int zen) {
        switch (zen) {
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
                return 2;
            default:
                return 0;
        }
    }

    public void pullRules(java.util.List<android.util.StatsEvent> events) {
        android.service.notification.ZenModeConfig config;
        synchronized (this.mConfigsArrayLock) {
            int numConfigs = this.mConfigs.size();
            for (int i = 0; i < numConfigs; i++) {
                int user = this.mConfigs.keyAt(i);
                android.service.notification.ZenModeConfig config2 = this.mConfigs.valueAt(i);
                events.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.DND_MODE_RULE, user, config2.isManualActive(), config2.areChannelsBypassingDnd, -1, "", 1000, config2.getZenPolicy().toProto(), 0, 0, 0, -1));
                if (!config2.isManualActive()) {
                    config = config2;
                } else {
                    config = config2;
                    ruleToProtoLocked(user, config.manualRule, true, events);
                }
                for (android.service.notification.ZenModeConfig.ZenRule rule : config.automaticRules.values()) {
                    ruleToProtoLocked(user, rule, false, events);
                }
            }
        }
    }

    private void ruleToProtoLocked(int user, android.service.notification.ZenModeConfig.ZenRule rule, boolean isManualRule, java.util.List<android.util.StatsEvent> events) {
        byte[] policyProto;
        java.lang.String id = rule.id == null ? "" : rule.id;
        if (!android.service.notification.ZenModeConfig.DEFAULT_RULE_IDS.contains(id)) {
            id = "";
        }
        java.lang.String pkg = rule.getPkg() != null ? rule.getPkg() : "";
        if (rule.enabler != null) {
            pkg = rule.enabler;
        }
        int ruleType = rule.type;
        if (isManualRule) {
            id = "MANUAL_RULE";
            ruleType = 999;
        }
        byte[] policyProto2 = new byte[0];
        if (rule.zenPolicy == null) {
            policyProto = policyProto2;
        } else {
            byte[] policyProto3 = rule.zenPolicy.toProto();
            policyProto = policyProto3;
        }
        events.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(com.android.internal.util.FrameworkStatsLog.DND_MODE_RULE, user, rule.enabled, false, rule.zenMode, id, getPackageUid(pkg, user), policyProto, rule.userModifiedFields, rule.zenPolicyUserModifiedFields, rule.zenDeviceEffectsUserModifiedFields, ruleType));
    }

    private int getPackageUid(java.lang.String pkg, int user) {
        if ("android".equals(pkg)) {
            return 1000;
        }
        java.lang.String key = getPackageUserKey(pkg, user);
        if (this.mRulesUidCache.get(key) == null) {
            try {
                this.mRulesUidCache.put(key, java.lang.Integer.valueOf(this.mPm.getPackageUidAsUser(pkg, user)));
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }
        return this.mRulesUidCache.getOrDefault(key, -1).intValue();
    }

    private static java.lang.String getPackageUserKey(java.lang.String pkg, int user) {
        return pkg + "|" + user;
    }

    protected final class RingerModeDelegate implements android.media.AudioManagerInternal.RingerModeDelegate {
        protected RingerModeDelegate() {
        }

        public java.lang.String toString() {
            return com.android.server.notification.ZenModeHelper.TAG;
        }

        public int onSetRingerModeInternal(int ringerModeOld, int ringerModeNew, java.lang.String caller, int ringerModeExternal, android.media.VolumePolicy policy) throws java.lang.Throwable {
            int newZen;
            int i;
            boolean isChange = ringerModeOld != ringerModeNew;
            int ringerModeExternalOut = ringerModeNew;
            if (com.android.server.notification.ZenModeHelper.this.mZenMode == 0 || (com.android.server.notification.ZenModeHelper.this.mZenMode == 1 && !areAllPriorityOnlyRingerSoundsMuted())) {
                com.android.server.notification.ZenModeHelper.this.setPreviousRingerModeSetting(java.lang.Integer.valueOf(ringerModeNew));
            }
            int newZen2 = -1;
            switch (ringerModeNew) {
                case 0:
                    if (isChange && policy.doNotDisturbWhenSilent) {
                        if (com.android.server.notification.ZenModeHelper.this.mZenMode == 0) {
                            newZen2 = 1;
                        }
                        com.android.server.notification.ZenModeHelper.this.setPreviousRingerModeSetting(java.lang.Integer.valueOf(ringerModeOld));
                        newZen = newZen2;
                    } else {
                        newZen = -1;
                    }
                    break;
                case 1:
                case 2:
                    if (isChange && ringerModeOld == 0 && (com.android.server.notification.ZenModeHelper.this.mZenMode == 2 || com.android.server.notification.ZenModeHelper.this.mZenMode == 3 || (com.android.server.notification.ZenModeHelper.this.mZenMode == 1 && areAllPriorityOnlyRingerSoundsMuted()))) {
                        newZen = 0;
                    } else if (com.android.server.notification.ZenModeHelper.this.mZenMode != 0) {
                        ringerModeExternalOut = 0;
                        newZen = -1;
                    } else {
                        newZen = -1;
                    }
                    break;
                default:
                    newZen = -1;
                    break;
            }
            if (newZen == -1) {
                i = -1;
            } else {
                i = -1;
                com.android.server.notification.ZenModeHelper.this.setManualZenMode(newZen, null, 5, "ringerModeInternal", null, false, 1000);
            }
            if (isChange || newZen != i || ringerModeExternal != ringerModeExternalOut) {
                com.android.server.notification.ZenLog.traceSetRingerModeInternal(ringerModeOld, ringerModeNew, caller, ringerModeExternal, ringerModeExternalOut);
            }
            return ringerModeExternalOut;
        }

        private boolean areAllPriorityOnlyRingerSoundsMuted() {
            boolean zAreAllPriorityOnlyRingerSoundsMuted;
            synchronized (com.android.server.notification.ZenModeHelper.this.mConfigLock) {
                zAreAllPriorityOnlyRingerSoundsMuted = android.service.notification.ZenModeConfig.areAllPriorityOnlyRingerSoundsMuted(com.android.server.notification.ZenModeHelper.this.mConfig);
            }
            return zAreAllPriorityOnlyRingerSoundsMuted;
        }

        public int onSetRingerModeExternal(int ringerModeOld, int ringerModeNew, java.lang.String caller, int ringerModeInternal, android.media.VolumePolicy policy) throws java.lang.Throwable {
            int newZen;
            int ringerModeInternalOut = ringerModeNew;
            boolean isChange = ringerModeOld != ringerModeNew;
            boolean isVibrate = ringerModeInternal == 1;
            int newZen2 = -1;
            switch (ringerModeNew) {
                case 0:
                    if (isChange) {
                        if (com.android.server.notification.ZenModeHelper.this.mZenMode == 0) {
                            newZen2 = 1;
                        }
                        ringerModeInternalOut = isVibrate ? 1 : 0;
                        newZen = newZen2;
                    } else {
                        ringerModeInternalOut = ringerModeInternal;
                        newZen = -1;
                    }
                    break;
                case 1:
                case 2:
                    if (com.android.server.notification.ZenModeHelper.this.mZenMode != 0) {
                        newZen = 0;
                        break;
                    }
                default:
                    newZen = -1;
                    break;
            }
            if (newZen != -1) {
                com.android.server.notification.ZenModeHelper.this.setManualZenMode(newZen, null, 5, "ringerModeExternal", caller, false, 1000);
            }
            com.android.server.notification.ZenLog.traceSetRingerModeExternal(ringerModeOld, ringerModeNew, caller, ringerModeInternal, ringerModeInternalOut);
            return ringerModeInternalOut;
        }

        public boolean canVolumeDownEnterSilent() {
            return com.android.server.notification.ZenModeHelper.this.mZenMode == 0;
        }

        public int getRingerModeAffectedStreams(int streams) {
            int streams2 = streams | 38;
            if (com.android.server.notification.ZenModeHelper.this.mZenMode == 2) {
                return streams2 | 2072;
            }
            return streams2 & (-2073);
        }
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        private final android.net.Uri ZEN_MODE;

        public SettingsObserver(android.os.Handler handler) {
            super(handler);
            this.ZEN_MODE = android.provider.Settings.Global.getUriFor("zen_mode");
        }

        public void observe() {
            android.content.ContentResolver resolver = com.android.server.notification.ZenModeHelper.this.mContext.getContentResolver();
            resolver.registerContentObserver(this.ZEN_MODE, false, this);
            update(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            update(uri);
        }

        public void update(android.net.Uri uri) {
            if (this.ZEN_MODE.equals(uri) && com.android.server.notification.ZenModeHelper.this.mZenMode != com.android.server.notification.ZenModeHelper.this.getZenModeSetting()) {
                if (com.android.server.notification.ZenModeHelper.DEBUG) {
                    android.util.Log.d(com.android.server.notification.ZenModeHelper.TAG, "Fixing zen mode setting");
                }
                com.android.server.notification.ZenModeHelper.this.setZenModeSetting(com.android.server.notification.ZenModeHelper.this.mZenMode);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0035  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void showZenUpgradeNotification(int r9) {
        /*
            r8 = this;
            android.content.Context r0 = r8.mContext
            android.content.pm.PackageManager r0 = r0.getPackageManager()
            java.lang.String r1 = "android.hardware.type.watch"
            boolean r0 = r0.hasSystemFeature(r1)
            boolean r1 = r8.mIsSystemServicesReady
            java.lang.String r2 = "show_zen_upgrade_notification"
            r3 = 0
            if (r1 == 0) goto L35
            if (r9 == 0) goto L35
            if (r0 != 0) goto L35
            android.content.Context r1 = r8.mContext
            android.content.ContentResolver r1 = r1.getContentResolver()
            int r1 = android.provider.Settings.Secure.getInt(r1, r2, r3)
            if (r1 == 0) goto L35
            android.content.Context r1 = r8.mContext
            android.content.ContentResolver r1 = r1.getContentResolver()
            java.lang.String r4 = "zen_settings_updated"
            int r1 = android.provider.Settings.Secure.getInt(r1, r4, r3)
            r4 = 1
            if (r1 == r4) goto L35
            goto L36
        L35:
            r4 = r3
        L36:
            r1 = r4
            if (r0 == 0) goto L42
            android.content.Context r4 = r8.mContext
            android.content.ContentResolver r4 = r4.getContentResolver()
            android.provider.Settings.Secure.putInt(r4, r2, r3)
        L42:
            if (r1 == 0) goto L5a
            android.app.NotificationManager r4 = r8.mNotificationManager
            android.app.Notification r5 = r8.createZenUpgradeNotification()
            java.lang.String r6 = "ZenModeHelper"
            r7 = 48
            r4.notify(r6, r7, r5)
            android.content.Context r4 = r8.mContext
            android.content.ContentResolver r4 = r4.getContentResolver()
            android.provider.Settings.Secure.putInt(r4, r2, r3)
        L5a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.ZenModeHelper.showZenUpgradeNotification(int):void");
    }

    protected android.app.Notification createZenUpgradeNotification() {
        android.os.Bundle extras = new android.os.Bundle();
        extras.putString("android.substName", this.mContext.getResources().getString(android.R.string.global_action_screenshot));
        int title = android.R.string.zen_mode_duration_hours_short;
        int content = android.R.string.zen_mode_duration_hours;
        int drawable = android.R.drawable.ic_signal_cellular_5_5_bar;
        if (android.app.NotificationManager.Policy.areAllVisualEffectsSuppressed(getConsolidatedNotificationPolicy().suppressedVisualEffects)) {
            title = android.R.string.zen_mode_duration_hours_summary_short;
            content = android.R.string.zen_mode_duration_hours_summary;
            drawable = android.R.drawable.ic_check_24dp;
        }
        android.content.Intent onboardingIntent = new android.content.Intent("android.settings.ZEN_MODE_ONBOARDING");
        onboardingIntent.addFlags(268468224);
        return new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.DO_NOT_DISTURB).setAutoCancel(true).setSmallIcon(android.R.drawable.ic_notifications_alerted).setLargeIcon(android.graphics.drawable.Icon.createWithResource(this.mContext, drawable)).setContentTitle(this.mContext.getResources().getString(title)).setContentText(this.mContext.getResources().getString(content)).setContentIntent(android.app.PendingIntent.getActivity(this.mContext, 0, onboardingIntent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD)).setAutoCancel(true).setLocalOnly(true).addExtras(extras).setStyle(new android.app.Notification.BigTextStyle()).build();
    }

    private int drawableResNameToResId(java.lang.String packageName, java.lang.String resourceName) {
        if (android.text.TextUtils.isEmpty(resourceName)) {
            return 0;
        }
        try {
            android.content.res.Resources res = this.mPm.getResourcesForApplication(packageName);
            return res.getIdentifier(resourceName, null, null);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.w(TAG, "cannot load rule icon for pkg", e);
            return 0;
        }
    }

    private java.lang.String drawableResIdToResName(java.lang.String packageName, int resId) {
        if (resId == 0) {
            return null;
        }
        java.util.Objects.requireNonNull(packageName);
        try {
            android.content.res.Resources res = this.mPm.getResourcesForApplication(packageName);
            java.lang.String resourceName = res.getResourceName(resId);
            if (resourceName != null && resourceName.length() > 1000) {
                android.util.Slog.e(TAG, "Resource name for ID=" + resId + " in package " + packageName + " is too long (" + resourceName.length() + "); ignoring it");
                return null;
            }
            return resourceName;
        } catch (android.content.pm.PackageManager.NameNotFoundException | android.content.res.Resources.NotFoundException e) {
            android.util.Slog.e(TAG, "Resource name for ID=" + resId + " not found in package " + packageName + ". Resource IDs may change when the application is upgraded, and the system may not be able to find the correct resource.");
            return null;
        }
    }

    private static void requirePublicOrigin(java.lang.String method, int origin) {
        if (!android.app.Flags.modesApi()) {
            return;
        }
        com.android.internal.util.Preconditions.checkArgument(origin == 4 || origin == 5 || origin == 3, "Expected one of UPDATE_ORIGIN_APP, UPDATE_ORIGIN_SYSTEM_OR_SYSTEMUI, or UPDATE_ORIGIN_USER for %s, but received '%s'.", new java.lang.Object[]{method, java.lang.Integer.valueOf(origin)});
    }

    private final class Metrics extends com.android.server.notification.ZenModeHelper.Callback {
        private static final java.lang.String COUNTER_MODE_PREFIX = "dnd_mode_";
        private static final java.lang.String COUNTER_RULE = "dnd_rule_count";
        private static final java.lang.String COUNTER_TYPE_PREFIX = "dnd_type_";
        private static final int DND_OFF = 0;
        private static final int DND_ON_AUTOMATIC = 2;
        private static final int DND_ON_MANUAL = 1;
        private static final long MINIMUM_LOG_PERIOD_MS = 60000;
        private long mModeLogTimeMs;
        private int mNumZenRules;
        private int mPreviousZenMode;
        private int mPreviousZenType;
        private long mRuleCountLogTime;
        private long mTypeLogTimeMs;

        private Metrics() {
            this.mPreviousZenMode = -1;
            this.mModeLogTimeMs = 0L;
            this.mNumZenRules = -1;
            this.mRuleCountLogTime = 0L;
            this.mPreviousZenType = -1;
            this.mTypeLogTimeMs = 0L;
        }

        @Override // com.android.server.notification.ZenModeHelper.Callback
        void onZenModeChanged() {
            emit();
        }

        @Override // com.android.server.notification.ZenModeHelper.Callback
        void onConfigChanged() {
            emit();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void emit() {
            com.android.server.notification.ZenModeHelper.this.mHandler.postMetricsTimer();
            emitZenMode();
            emitRules();
            emitDndType();
        }

        private void emitZenMode() {
            long now = android.os.SystemClock.elapsedRealtime();
            long since = now - this.mModeLogTimeMs;
            if (this.mPreviousZenMode != com.android.server.notification.ZenModeHelper.this.mZenMode || since > 60000) {
                if (this.mPreviousZenMode != -1) {
                    com.android.internal.logging.MetricsLogger.count(com.android.server.notification.ZenModeHelper.this.mContext, COUNTER_MODE_PREFIX + this.mPreviousZenMode, (int) since);
                }
                this.mPreviousZenMode = com.android.server.notification.ZenModeHelper.this.mZenMode;
                this.mModeLogTimeMs = now;
            }
        }

        private void emitRules() {
            long now = android.os.SystemClock.elapsedRealtime();
            long since = now - this.mRuleCountLogTime;
            synchronized (com.android.server.notification.ZenModeHelper.this.mConfigLock) {
                int numZenRules = com.android.server.notification.ZenModeHelper.this.mConfig.automaticRules.size();
                if (this.mNumZenRules != numZenRules || since > 60000) {
                    if (this.mNumZenRules != -1) {
                        com.android.internal.logging.MetricsLogger.count(com.android.server.notification.ZenModeHelper.this.mContext, COUNTER_RULE, numZenRules - this.mNumZenRules);
                    }
                    this.mNumZenRules = numZenRules;
                    this.mRuleCountLogTime = since;
                }
            }
        }

        private void emitDndType() {
            long now = android.os.SystemClock.elapsedRealtime();
            long since = now - this.mTypeLogTimeMs;
            synchronized (com.android.server.notification.ZenModeHelper.this.mConfigLock) {
                int zenType = 1;
                boolean dndOn = com.android.server.notification.ZenModeHelper.this.mZenMode != 0;
                if (!dndOn) {
                    zenType = 0;
                } else if (com.android.server.notification.ZenModeHelper.this.mConfig.manualRule == null) {
                    zenType = 2;
                }
                if (zenType != this.mPreviousZenType || since > 60000) {
                    if (this.mPreviousZenType != -1) {
                        com.android.internal.logging.MetricsLogger.count(com.android.server.notification.ZenModeHelper.this.mContext, COUNTER_TYPE_PREFIX + this.mPreviousZenType, (int) since);
                    }
                    this.mTypeLogTimeMs = now;
                    this.mPreviousZenType = zenType;
                }
            }
        }
    }

    private final class H extends android.os.Handler {
        private static final long METRICS_PERIOD_MS = 21600000;
        private static final int MSG_APPLY_EFFECTS = 6;
        private static final int MSG_DISPATCH = 1;
        private static final int MSG_METRICS = 2;
        private static final int MSG_RINGER_AUDIO = 5;

        private H(android.os.Looper looper) {
            super(looper);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void postDispatchOnZenModeChanged() {
            removeMessages(1);
            sendEmptyMessage(1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void postMetricsTimer() {
            removeMessages(2);
            sendEmptyMessageDelayed(2, METRICS_PERIOD_MS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void postUpdateRingerAndAudio(boolean shouldApplyToRinger) {
            removeMessages(5);
            sendMessage(obtainMessage(5, java.lang.Boolean.valueOf(shouldApplyToRinger)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void postApplyDeviceEffects(int origin) {
            removeMessages(6);
            sendMessage(obtainMessage(6, origin, 0));
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.notification.ZenModeHelper.this.dispatchOnZenModeChanged();
                    break;
                case 2:
                    com.android.server.notification.ZenModeHelper.this.mMetrics.emit();
                    break;
                case 5:
                    boolean shouldApplyToRinger = ((java.lang.Boolean) msg.obj).booleanValue();
                    com.android.server.notification.ZenModeHelper.this.updateRingerAndAudio(shouldApplyToRinger);
                    break;
                case 6:
                    int origin = msg.arg1;
                    com.android.server.notification.ZenModeHelper.this.applyConsolidatedDeviceEffects(origin);
                    break;
            }
        }
    }

    public static class Callback {
        void onConfigChanged() {
        }

        void onZenModeChanged() {
        }

        void onPolicyChanged(android.app.NotificationManager.Policy newPolicy) {
        }

        void onConsolidatedPolicyChanged(android.app.NotificationManager.Policy newConsolidatedPolicy) {
        }

        void onAutomaticRuleStatusChanged(int userId, java.lang.String pkg, java.lang.String id, int status) {
        }
    }
}
