package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class ZenModeConditions implements com.android.server.notification.ConditionProviders.Callback {
    private static final boolean DEBUG = com.android.server.notification.ZenModeHelper.DEBUG;
    private static final java.lang.String TAG = "ZenModeHelper";
    private final com.android.server.notification.ConditionProviders mConditionProviders;
    private final com.android.server.notification.ZenModeHelper mHelper;
    protected final android.util.ArrayMap<android.net.Uri, android.content.ComponentName> mSubscriptions = new android.util.ArrayMap<>();

    public ZenModeConditions(com.android.server.notification.ZenModeHelper helper, com.android.server.notification.ConditionProviders conditionProviders) {
        this.mHelper = helper;
        this.mConditionProviders = conditionProviders;
        if (this.mConditionProviders.isSystemProviderEnabled("countdown")) {
            this.mConditionProviders.addSystemProvider(new com.android.server.notification.CountdownConditionProvider());
        }
        if (this.mConditionProviders.isSystemProviderEnabled("schedule")) {
            this.mConditionProviders.addSystemProvider(new com.android.server.notification.ScheduleConditionProvider());
        }
        if (this.mConditionProviders.isSystemProviderEnabled("event")) {
            this.mConditionProviders.addSystemProvider(new com.android.server.notification.EventConditionProvider());
        }
        this.mConditionProviders.setCallback(this);
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("mSubscriptions=");
        pw.println(this.mSubscriptions);
    }

    public void evaluateConfig(android.service.notification.ZenModeConfig config, android.content.ComponentName trigger, boolean processSubscriptions) {
        if (config == null) {
            return;
        }
        if (!android.app.Flags.modesUi() && config.manualRule != null && config.manualRule.condition != null && !config.manualRule.isTrueOrUnknown()) {
            if (DEBUG) {
                android.util.Log.d(TAG, "evaluateConfig: clearing manual rule");
            }
            config.manualRule = null;
        }
        android.util.ArraySet<android.net.Uri> current = new android.util.ArraySet<>();
        evaluateRule(config.manualRule, current, null, processSubscriptions, true);
        for (android.service.notification.ZenModeConfig.ZenRule automaticRule : config.automaticRules.values()) {
            if (automaticRule.component != null) {
                evaluateRule(automaticRule, current, trigger, processSubscriptions, false);
                updateSnoozing(automaticRule);
            }
        }
        synchronized (this.mSubscriptions) {
            int N = this.mSubscriptions.size();
            for (int i = N - 1; i >= 0; i--) {
                android.net.Uri id = this.mSubscriptions.keyAt(i);
                android.content.ComponentName component = this.mSubscriptions.valueAt(i);
                if (processSubscriptions && !current.contains(id)) {
                    this.mConditionProviders.unsubscribeIfNecessary(component, id);
                    this.mSubscriptions.removeAt(i);
                }
            }
        }
    }

    @Override // com.android.server.notification.ConditionProviders.Callback
    public void onBootComplete() {
    }

    @Override // com.android.server.notification.ConditionProviders.Callback
    public void onUserSwitched() {
    }

    @Override // com.android.server.notification.ConditionProviders.Callback
    public void onServiceAdded(android.content.ComponentName component) {
        if (DEBUG) {
            android.util.Log.d(TAG, "onServiceAdded " + component);
        }
        int callingUid = android.os.Binder.getCallingUid();
        this.mHelper.setConfig(this.mHelper.getConfig(), component, callingUid == 1000 ? 5 : 4, "zmc.onServiceAdded:" + component, callingUid);
    }

    @Override // com.android.server.notification.ConditionProviders.Callback
    public void onConditionChanged(android.net.Uri id, android.service.notification.Condition condition) {
        if (DEBUG) {
            android.util.Log.d(TAG, "onConditionChanged " + id + " " + condition);
        }
        android.service.notification.ZenModeConfig config = this.mHelper.getConfig();
        if (config == null) {
            return;
        }
        int callingUid = android.os.Binder.getCallingUid();
        this.mHelper.setAutomaticZenRuleState(id, condition, callingUid == 1000 ? 5 : 4, callingUid);
    }

    private void evaluateRule(android.service.notification.ZenModeConfig.ZenRule rule, android.util.ArraySet<android.net.Uri> current, android.content.ComponentName trigger, boolean processSubscriptions, boolean isManual) {
        if (rule == null || rule.conditionId == null || rule.configurationActivity != null) {
            return;
        }
        android.net.Uri id = rule.conditionId;
        boolean isSystemCondition = false;
        for (com.android.server.notification.SystemConditionProviderService sp : this.mConditionProviders.getSystemProviders()) {
            if (sp.isValidConditionId(id)) {
                this.mConditionProviders.ensureRecordExists(sp.getComponent(), id, sp.asInterface());
                rule.component = sp.getComponent();
                isSystemCondition = true;
            }
        }
        if (!isSystemCondition) {
            android.service.notification.IConditionProvider cp = this.mConditionProviders.findConditionProvider(rule.component);
            if (DEBUG) {
                android.util.Log.d(TAG, "Ensure external rule exists: " + (cp != null) + " for " + id);
            }
            if (cp != null) {
                this.mConditionProviders.ensureRecordExists(rule.component, id, cp);
            }
        }
        if (rule.component == null && rule.enabler == null) {
            if (!android.app.Flags.modesUi() || (android.app.Flags.modesUi() && !isManual)) {
                android.util.Log.w(TAG, "No component found for automatic rule: " + rule.conditionId);
                rule.enabled = false;
                return;
            }
            return;
        }
        if (current != null) {
            current.add(id);
        }
        if (processSubscriptions && ((trigger != null && trigger.equals(rule.component)) || isSystemCondition)) {
            if (DEBUG) {
                android.util.Log.d(TAG, "Subscribing to " + rule.component);
            }
            if (this.mConditionProviders.subscribeIfNecessary(rule.component, rule.conditionId)) {
                synchronized (this.mSubscriptions) {
                    this.mSubscriptions.put(rule.conditionId, rule.component);
                }
            } else {
                rule.condition = null;
                if (DEBUG) {
                    android.util.Log.d(TAG, "zmc failed to subscribe");
                }
            }
        }
        if (rule.component != null && rule.condition == null) {
            rule.condition = this.mConditionProviders.findCondition(rule.component, rule.conditionId);
            if (rule.condition == null || !DEBUG) {
                return;
            }
            android.util.Log.d(TAG, "Found existing condition for: " + rule.conditionId);
        }
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
}
