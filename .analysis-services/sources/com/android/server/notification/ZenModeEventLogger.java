package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
class ZenModeEventLogger {
    protected static final int ACTIVE_RULE_TYPE_MANUAL = 999;
    private static final java.lang.String TAG = "ZenModeEventLogger";
    static final int ZEN_MODE_UNKNOWN = -1;
    com.android.server.notification.ZenModeEventLogger.ZenStateChanges mChangeState = new com.android.server.notification.ZenModeEventLogger.ZenStateChanges();
    private final android.content.pm.PackageManager mPm;

    ZenModeEventLogger(android.content.pm.PackageManager pm) {
        this.mPm = pm;
    }

    enum ZenStateChangedEvent implements com.android.internal.logging.UiEventLogger.UiEventEnum {
        DND_TURNED_ON(1368),
        DND_TURNED_OFF(1369),
        DND_POLICY_CHANGED(1370),
        DND_ACTIVE_RULES_CHANGED(1371);

        private final int mId;

        ZenStateChangedEvent(int id) {
            this.mId = id;
        }

        public int getId() {
            return this.mId;
        }
    }

    public final void maybeLogZenChange(com.android.server.notification.ZenModeEventLogger.ZenModeInfo prevInfo, com.android.server.notification.ZenModeEventLogger.ZenModeInfo newInfo, int callingUid, int origin) {
        this.mChangeState.init(prevInfo, newInfo, callingUid, origin);
        if (this.mChangeState.shouldLogChanges()) {
            maybeReassignCallingUid();
            logChanges();
        }
        this.mChangeState = new com.android.server.notification.ZenModeEventLogger.ZenStateChanges();
    }

    private void maybeReassignCallingUid() {
        android.util.Pair<java.lang.String, java.lang.Integer> ruleInfo;
        int userId = -1;
        java.lang.String packageName = null;
        if (this.mChangeState.getChangedRuleType() == 1) {
            if (!this.mChangeState.isFromSystemOrSystemUi() || this.mChangeState.getNewManualRuleEnabler() == null) {
                return;
            }
            packageName = this.mChangeState.getNewManualRuleEnabler();
            userId = this.mChangeState.mNewConfig.user;
        }
        if (this.mChangeState.getChangedRuleType() == 2) {
            if (this.mChangeState.getIsUserAction() || !this.mChangeState.isFromSystemOrSystemUi()) {
                return;
            }
            android.util.ArrayMap<java.lang.String, android.service.notification.ZenModeDiff.RuleDiff> changedRules = this.mChangeState.getChangedAutomaticRules();
            if (changedRules.size() != 1 || (ruleInfo = this.mChangeState.getRulePackageAndUser(changedRules.keyAt(0), changedRules.valueAt(0))) == null || ((java.lang.String) ruleInfo.first).equals(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME)) {
                return;
            }
            packageName = (java.lang.String) ruleInfo.first;
            userId = ((java.lang.Integer) ruleInfo.second).intValue();
        }
        if (userId == -1 || packageName == null) {
            return;
        }
        try {
            int uid = this.mPm.getPackageUidAsUser(packageName, userId);
            this.mChangeState.mCallingUid = uid;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "unable to find package name " + packageName + " " + userId);
        }
    }

    void logChanges() {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DND_STATE_CHANGED, this.mChangeState.getEventId().getId(), this.mChangeState.mNewZenMode, this.mChangeState.mPrevZenMode, this.mChangeState.getChangedRuleType(), this.mChangeState.getNumRulesActive(), this.mChangeState.getIsUserAction(), this.mChangeState.getPackageUid(), this.mChangeState.getDNDPolicyProto(), this.mChangeState.getAreChannelsBypassing(), this.mChangeState.getActiveRuleTypes());
    }

    public static class ZenModeInfo {
        final android.service.notification.ZenModeConfig mConfig;
        final android.app.NotificationManager.Policy mPolicy;
        final int mZenMode;

        ZenModeInfo(int zenMode, android.service.notification.ZenModeConfig config, android.app.NotificationManager.Policy policy) {
            this.mZenMode = zenMode;
            this.mConfig = config != null ? config.copy() : null;
            this.mPolicy = policy != null ? policy.copy() : null;
        }
    }

    static class ZenStateChanges {
        android.service.notification.ZenModeConfig mNewConfig;
        android.app.NotificationManager.Policy mNewPolicy;
        android.service.notification.ZenModeConfig mPrevConfig;
        android.app.NotificationManager.Policy mPrevPolicy;
        int mPrevZenMode = -1;
        int mNewZenMode = -1;
        int mCallingUid = -1;
        int mOrigin = 0;

        ZenStateChanges() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void init(com.android.server.notification.ZenModeEventLogger.ZenModeInfo prevInfo, com.android.server.notification.ZenModeEventLogger.ZenModeInfo newInfo, int callingUid, int origin) {
            this.mPrevZenMode = prevInfo.mZenMode;
            this.mNewZenMode = newInfo.mZenMode;
            this.mPrevConfig = prevInfo.mConfig;
            this.mNewConfig = newInfo.mConfig;
            this.mPrevPolicy = prevInfo.mPolicy;
            this.mNewPolicy = newInfo.mPolicy;
            this.mCallingUid = callingUid;
            this.mOrigin = origin;
        }

        private boolean hasPolicyDiff() {
            return (this.mPrevPolicy == null || java.util.Objects.equals(this.mPrevPolicy, this.mNewPolicy)) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean shouldLogChanges() {
            if (zenModeFlipped()) {
                return true;
            }
            if (android.app.Flags.modesApi() && hasActiveRuleCountDiff()) {
                return true;
            }
            if (this.mNewZenMode == 0) {
                return false;
            }
            return hasPolicyDiff() || hasActiveRuleCountDiff();
        }

        private boolean zenModeFlipped() {
            if (this.mPrevZenMode == this.mNewZenMode) {
                return false;
            }
            return this.mPrevZenMode == 0 || this.mNewZenMode == 0;
        }

        com.android.server.notification.ZenModeEventLogger.ZenStateChangedEvent getEventId() {
            if (!shouldLogChanges()) {
                android.util.Log.wtf(com.android.server.notification.ZenModeEventLogger.TAG, "attempt to get DNDStateChanged fields without shouldLog=true");
            }
            if (zenModeFlipped()) {
                if (this.mPrevZenMode == 0) {
                    return com.android.server.notification.ZenModeEventLogger.ZenStateChangedEvent.DND_TURNED_ON;
                }
                return com.android.server.notification.ZenModeEventLogger.ZenStateChangedEvent.DND_TURNED_OFF;
            }
            if (android.app.Flags.modesApi() && this.mNewZenMode == 0) {
                if (hasPolicyDiff() || hasChannelsBypassingDiff()) {
                    android.util.Log.wtf(com.android.server.notification.ZenModeEventLogger.TAG, "Detected policy diff even though DND is OFF and not toggled");
                }
                return com.android.server.notification.ZenModeEventLogger.ZenStateChangedEvent.DND_ACTIVE_RULES_CHANGED;
            }
            if (hasPolicyDiff() || hasChannelsBypassingDiff()) {
                return com.android.server.notification.ZenModeEventLogger.ZenStateChangedEvent.DND_POLICY_CHANGED;
            }
            return com.android.server.notification.ZenModeEventLogger.ZenStateChangedEvent.DND_ACTIVE_RULES_CHANGED;
        }

        int getChangedRuleType() {
            android.service.notification.ZenModeDiff.ConfigDiff diff = new android.service.notification.ZenModeDiff.ConfigDiff(this.mPrevConfig, this.mNewConfig);
            if (!diff.hasDiff()) {
                return 0;
            }
            android.service.notification.ZenModeDiff.RuleDiff manualDiff = diff.getManualRuleDiff();
            if (manualDiff != null && manualDiff.hasDiff()) {
                if (!manualDiff.wasAdded() && !manualDiff.wasRemoved()) {
                    if (android.app.Flags.modesUi() && (manualDiff.becameActive() || manualDiff.becameInactive())) {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
            android.util.ArrayMap<java.lang.String, android.service.notification.ZenModeDiff.RuleDiff> autoDiffs = diff.getAllAutomaticRuleDiffs();
            if (autoDiffs != null) {
                for (android.service.notification.ZenModeDiff.RuleDiff d : autoDiffs.values()) {
                    if (d != null && d.hasDiff() && (d.becameActive() || d.becameInactive())) {
                        return 2;
                    }
                }
            }
            return 0;
        }

        private boolean hasActiveRuleCountDiff() {
            return numActiveRulesInConfig(this.mPrevConfig) != numActiveRulesInConfig(this.mNewConfig);
        }

        java.util.List<android.service.notification.ZenModeConfig.ZenRule> activeRulesList(android.service.notification.ZenModeConfig config) {
            java.util.ArrayList<android.service.notification.ZenModeConfig.ZenRule> rules = new java.util.ArrayList<>();
            if (config == null) {
                return rules;
            }
            if (config.isManualActive()) {
                android.service.notification.ZenModeConfig.ZenRule rule = config.manualRule.copy();
                rule.type = 999;
                rules.add(rule);
            }
            if (config.automaticRules != null) {
                for (android.service.notification.ZenModeConfig.ZenRule rule2 : config.automaticRules.values()) {
                    if (rule2 != null && rule2.isAutomaticActive()) {
                        rules.add(rule2);
                    }
                }
            }
            return rules;
        }

        int numActiveRulesInConfig(android.service.notification.ZenModeConfig config) {
            return activeRulesList(config).size();
        }

        int getNumRulesActive() {
            if (!android.app.Flags.modesApi() && this.mNewZenMode == 0) {
                return 0;
            }
            return numActiveRulesInConfig(this.mNewConfig);
        }

        int[] getActiveRuleTypes() {
            if (!android.app.Flags.modesApi()) {
                return new int[0];
            }
            java.util.ArrayList<java.lang.Integer> activeTypes = new java.util.ArrayList<>();
            java.util.List<android.service.notification.ZenModeConfig.ZenRule> activeRules = activeRulesList(this.mNewConfig);
            if (activeRules.size() == 0) {
                return new int[0];
            }
            for (android.service.notification.ZenModeConfig.ZenRule rule : activeRules) {
                activeTypes.add(java.lang.Integer.valueOf(rule.type));
            }
            java.util.Collections.sort(activeTypes);
            int[] out = new int[activeTypes.size()];
            for (int i = 0; i < activeTypes.size(); i++) {
                out[i] = activeTypes.get(i).intValue();
            }
            return out;
        }

        boolean getIsUserAction() {
            if (android.app.Flags.modesApi()) {
                return this.mOrigin == 3;
            }
            switch (getChangedRuleType()) {
                case 1:
                    if (!isFromSystemOrSystemUi() || getNewManualRuleEnabler() != null) {
                    }
                    break;
                case 2:
                    for (android.service.notification.ZenModeDiff.RuleDiff d : getChangedAutomaticRules().values()) {
                        if (d.wasAdded() || d.wasRemoved()) {
                            break;
                        } else {
                            android.service.notification.ZenModeDiff.FieldDiff enabled = d.getDiffForField(com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
                            if (enabled != null && enabled.hasDiff()) {
                                break;
                            } else {
                                android.service.notification.ZenModeDiff.FieldDiff snoozing = d.getDiffForField("snoozing");
                                if (snoozing != null && snoozing.hasDiff() && ((java.lang.Boolean) snoozing.to()).booleanValue()) {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                default:
                    if ((hasPolicyDiff() || hasChannelsBypassingDiff()) && this.mCallingUid == 1000) {
                    }
                    break;
            }
        }

        boolean isFromSystemOrSystemUi() {
            return this.mOrigin == 1 || this.mOrigin == 2 || this.mOrigin == 5 || this.mOrigin == 6;
        }

        int getPackageUid() {
            return this.mCallingUid;
        }

        byte[] getDNDPolicyProto() {
            int i;
            if (android.app.Flags.modesApi() && this.mNewZenMode == 0) {
                return null;
            }
            java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
            android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(bytes);
            if (this.mNewPolicy != null) {
                proto.write(1159641169921L, toState(this.mNewPolicy.allowCalls()));
                proto.write(1159641169922L, toState(this.mNewPolicy.allowRepeatCallers()));
                proto.write(1159641169923L, toState(this.mNewPolicy.allowMessages()));
                proto.write(1159641169924L, toState(this.mNewPolicy.allowConversations()));
                proto.write(1159641169925L, toState(this.mNewPolicy.allowReminders()));
                proto.write(1159641169926L, toState(this.mNewPolicy.allowEvents()));
                proto.write(1159641169927L, toState(this.mNewPolicy.allowAlarms()));
                proto.write(1159641169928L, toState(this.mNewPolicy.allowMedia()));
                proto.write(1159641169929L, toState(this.mNewPolicy.allowSystem()));
                proto.write(1159641169930L, toState(this.mNewPolicy.showFullScreenIntents()));
                proto.write(1159641169931L, toState(this.mNewPolicy.showLights()));
                proto.write(1159641169932L, toState(this.mNewPolicy.showPeeking()));
                proto.write(1159641169933L, toState(this.mNewPolicy.showStatusBarIcons()));
                proto.write(1159641169934L, toState(this.mNewPolicy.showBadges()));
                proto.write(1159641169935L, toState(this.mNewPolicy.showAmbient()));
                proto.write(1159641169936L, toState(this.mNewPolicy.showInNotificationList()));
                proto.write(1159641169937L, android.service.notification.ZenAdapters.prioritySendersToPeopleType(this.mNewPolicy.allowCallsFrom()));
                proto.write(1159641169938L, android.service.notification.ZenAdapters.prioritySendersToPeopleType(this.mNewPolicy.allowMessagesFrom()));
                proto.write(1159641169939L, this.mNewPolicy.allowConversationsFrom());
                if (android.app.Flags.modesApi()) {
                    if (this.mNewPolicy.allowPriorityChannels()) {
                        i = 1;
                    } else {
                        i = 2;
                    }
                    proto.write(1159641169940L, i);
                }
            } else {
                android.util.Log.wtf(com.android.server.notification.ZenModeEventLogger.TAG, "attempted to write zen mode log event with null policy");
            }
            proto.flush();
            return bytes.toByteArray();
        }

        boolean getAreChannelsBypassing() {
            return (this.mNewPolicy == null || (this.mNewPolicy.state & 1) == 0) ? false : true;
        }

        private boolean hasChannelsBypassingDiff() {
            boolean prevChannelsBypassing = (this.mPrevPolicy == null || (this.mPrevPolicy.state & 1) == 0) ? false : true;
            return prevChannelsBypassing != getAreChannelsBypassing();
        }

        private int toState(boolean allow) {
            return allow ? 1 : 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.util.ArrayMap<java.lang.String, android.service.notification.ZenModeDiff.RuleDiff> getChangedAutomaticRules() {
            android.util.ArrayMap<java.lang.String, android.service.notification.ZenModeDiff.RuleDiff> autoDiffs;
            android.util.ArrayMap<java.lang.String, android.service.notification.ZenModeDiff.RuleDiff> ruleDiffs = new android.util.ArrayMap<>();
            android.service.notification.ZenModeDiff.ConfigDiff diff = new android.service.notification.ZenModeDiff.ConfigDiff(this.mPrevConfig, this.mNewConfig);
            if (diff.hasDiff() && (autoDiffs = diff.getAllAutomaticRuleDiffs()) != null) {
                return autoDiffs;
            }
            return ruleDiffs;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.util.Pair<java.lang.String, java.lang.Integer> getRulePackageAndUser(java.lang.String id, android.service.notification.ZenModeDiff.RuleDiff diff) {
            android.service.notification.ZenModeConfig.ZenRule rule;
            android.service.notification.ZenModeConfig configForSearch = this.mNewConfig;
            if (diff.wasRemoved()) {
                configForSearch = this.mPrevConfig;
            }
            if (configForSearch != null && (rule = (android.service.notification.ZenModeConfig.ZenRule) configForSearch.automaticRules.getOrDefault(id, null)) != null) {
                if (rule.component != null) {
                    return new android.util.Pair<>(rule.component.getPackageName(), java.lang.Integer.valueOf(configForSearch.user));
                }
                if (rule.configurationActivity != null) {
                    return new android.util.Pair<>(rule.configurationActivity.getPackageName(), java.lang.Integer.valueOf(configForSearch.user));
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.String getNewManualRuleEnabler() {
            if (this.mNewConfig == null || this.mNewConfig.manualRule == null) {
                return null;
            }
            return this.mNewConfig.manualRule.enabler;
        }

        protected com.android.server.notification.ZenModeEventLogger.ZenStateChanges copy() {
            com.android.server.notification.ZenModeEventLogger.ZenStateChanges copy = new com.android.server.notification.ZenModeEventLogger.ZenStateChanges();
            copy.mPrevZenMode = this.mPrevZenMode;
            copy.mNewZenMode = this.mNewZenMode;
            copy.mPrevConfig = this.mPrevConfig.copy();
            copy.mNewConfig = this.mNewConfig.copy();
            copy.mPrevPolicy = this.mPrevPolicy.copy();
            copy.mNewPolicy = this.mNewPolicy.copy();
            copy.mCallingUid = this.mCallingUid;
            copy.mOrigin = this.mOrigin;
            return copy;
        }
    }
}
