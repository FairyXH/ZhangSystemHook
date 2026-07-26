package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class ZenLog {
    private static final android.util.LocalLog INTERCEPTION_EVENTS;
    private static final int SIZE;
    private static final android.util.LocalLog STATE_CHANGES;
    private static final int TYPE_ALERT_ON_UPDATED_INTERCEPT = 21;
    private static final int TYPE_CHECK_REPEAT_CALLER = 20;
    private static final int TYPE_CONFIG = 11;
    private static final int TYPE_DISABLE_EFFECTS = 13;
    private static final int TYPE_INTERCEPTED = 1;
    private static final int TYPE_LISTENER_HINTS_CHANGED = 15;
    private static final int TYPE_MATCHES_CALL_FILTER = 18;
    private static final int TYPE_NOT_INTERCEPTED = 12;
    private static final int TYPE_RECORD_CALLER = 19;
    private static final int TYPE_SET_CONSOLIDATED_ZEN_POLICY = 17;
    private static final int TYPE_SET_NOTIFICATION_POLICY = 16;
    private static final int TYPE_SET_RINGER_MODE_EXTERNAL = 3;
    private static final int TYPE_SET_RINGER_MODE_INTERNAL = 4;
    private static final int TYPE_SET_ZEN_MODE = 6;
    private static final int TYPE_SUBSCRIBE = 9;
    private static final int TYPE_SUPPRESSOR_CHANGED = 14;
    private static final int TYPE_UNSUBSCRIBE = 10;

    static {
        SIZE = android.os.Build.IS_DEBUGGABLE ? 200 : 100;
        STATE_CHANGES = new android.util.LocalLog(SIZE);
        INTERCEPTION_EVENTS = new android.util.LocalLog(SIZE);
    }

    public static void traceIntercepted(com.android.server.notification.NotificationRecord record, java.lang.String reason) {
        append(1, record.getKey() + "," + reason);
    }

    public static void traceNotIntercepted(com.android.server.notification.NotificationRecord record, java.lang.String reason) {
        append(12, record.getKey() + "," + reason);
    }

    public static void traceAlertOnUpdatedIntercept(com.android.server.notification.NotificationRecord record) {
        append(21, record.getKey());
    }

    public static void traceSetRingerModeExternal(int ringerModeOld, int ringerModeNew, java.lang.String caller, int ringerModeInternalIn, int ringerModeInternalOut) {
        append(3, caller + ",e:" + ringerModeToString(ringerModeOld) + "->" + ringerModeToString(ringerModeNew) + ",i:" + ringerModeToString(ringerModeInternalIn) + "->" + ringerModeToString(ringerModeInternalOut));
    }

    public static void traceSetRingerModeInternal(int ringerModeOld, int ringerModeNew, java.lang.String caller, int ringerModeExternalIn, int ringerModeExternalOut) {
        append(4, caller + ",i:" + ringerModeToString(ringerModeOld) + "->" + ringerModeToString(ringerModeNew) + ",e:" + ringerModeToString(ringerModeExternalIn) + "->" + ringerModeToString(ringerModeExternalOut));
    }

    public static void traceSetZenMode(int zenMode, java.lang.String reason) {
        append(6, zenModeToString(zenMode) + "," + reason);
    }

    public static void traceSetConsolidatedZenPolicy(android.app.NotificationManager.Policy policy, java.lang.String reason) {
        append(17, policy.toString() + "," + reason);
    }

    public static void traceSetNotificationPolicy(java.lang.String pkg, int targetSdk, android.app.NotificationManager.Policy policy) {
        java.lang.String policyLog = "pkg=" + pkg + " targetSdk=" + targetSdk + " NotificationPolicy=" + policy.toString();
        append(16, policyLog);
    }

    public static void traceSubscribe(android.net.Uri uri, android.service.notification.IConditionProvider provider, android.os.RemoteException e) {
        append(9, uri + "," + subscribeResult(provider, e));
    }

    public static void traceUnsubscribe(android.net.Uri uri, android.service.notification.IConditionProvider provider, android.os.RemoteException e) {
        append(10, uri + "," + subscribeResult(provider, e));
    }

    public static void traceConfig(java.lang.String reason, android.content.ComponentName triggeringComponent, android.service.notification.ZenModeConfig oldConfig, android.service.notification.ZenModeConfig newConfig, int callingUid) {
        android.service.notification.ZenModeDiff.ConfigDiff diff = new android.service.notification.ZenModeDiff.ConfigDiff(oldConfig, newConfig);
        if (!diff.hasDiff()) {
            append(11, reason + " no changes");
        } else {
            append(11, reason + " - " + triggeringComponent + " : " + callingUid + ",\n" + (newConfig != null ? newConfig.toString() : null) + ",\n" + diff);
        }
    }

    public static void traceDisableEffects(com.android.server.notification.NotificationRecord record, java.lang.String reason) {
        append(13, record.getKey() + "," + reason);
    }

    public static void traceEffectsSuppressorChanged(java.util.List<android.content.ComponentName> oldSuppressors, java.util.List<android.content.ComponentName> newSuppressors, long suppressedEffects) {
        append(14, "suppressed effects:" + suppressedEffects + "," + componentListToString(oldSuppressors) + "->" + componentListToString(newSuppressors));
    }

    public static void traceListenerHintsChanged(int oldHints, int newHints, int listenerCount) {
        append(15, hintsToString(oldHints) + "->" + hintsToString(newHints) + ",listeners=" + listenerCount);
    }

    public static void traceMatchesCallFilter(boolean result, java.lang.String reason, int callingUid) {
        append(18, "result=" + result + ", reason=" + reason + ", calling uid=" + callingUid);
    }

    public static void traceRecordCaller(boolean hasPhone, boolean hasUri) {
        append(19, "has phone number=" + hasPhone + ", has uri=" + hasUri);
    }

    public static void traceCheckRepeatCaller(boolean found, boolean hasPhone, boolean hasUri) {
        append(20, "res=" + found + ", given phone number=" + hasPhone + ", given uri=" + hasUri);
    }

    private static java.lang.String subscribeResult(android.service.notification.IConditionProvider provider, android.os.RemoteException e) {
        return provider == null ? "no provider" : e != null ? e.getMessage() : "ok";
    }

    private static java.lang.String typeToString(int type) {
        switch (type) {
            case 1:
                return "intercepted";
            case 2:
            case 5:
            case 7:
            case 8:
            default:
                return "unknown";
            case 3:
                return "set_ringer_mode_external";
            case 4:
                return "set_ringer_mode_internal";
            case 6:
                return "set_zen_mode";
            case 9:
                return "subscribe";
            case 10:
                return "unsubscribe";
            case 11:
                return "config";
            case 12:
                return "not_intercepted";
            case 13:
                return "disable_effects";
            case 14:
                return "suppressor_changed";
            case 15:
                return "listener_hints_changed";
            case 16:
                return "set_notification_policy";
            case 17:
                return "set_consolidated_policy";
            case 18:
                return "matches_call_filter";
            case 19:
                return "record_caller";
            case 20:
                return "check_repeat_caller";
            case 21:
                return "alert_on_updated_intercept";
        }
    }

    private static java.lang.String ringerModeToString(int ringerMode) {
        switch (ringerMode) {
            case 0:
                return "silent";
            case 1:
                return "vibrate";
            case 2:
                return "normal";
            default:
                return "unknown";
        }
    }

    private static java.lang.String zenModeToString(int zenMode) {
        switch (zenMode) {
            case 0:
                return kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF;
            case 1:
                return "important_interruptions";
            case 2:
                return "no_interruptions";
            case 3:
                return "alarms";
            default:
                return "unknown";
        }
    }

    private static java.lang.String hintsToString(int hints) {
        switch (hints) {
            case 0:
                return "none";
            case 1:
                return "disable_effects";
            case 2:
                return "disable_notification_effects";
            case 3:
            default:
                return java.lang.Integer.toString(hints);
            case 4:
                return "disable_call_effects";
        }
    }

    private static java.lang.String componentToString(android.content.ComponentName component) {
        if (component != null) {
            return component.toShortString();
        }
        return null;
    }

    private static java.lang.String componentListToString(java.util.List<android.content.ComponentName> components) {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
        for (int i = 0; i < components.size(); i++) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(componentToString(components.get(i)));
        }
        return stringBuilder.toString();
    }

    private static void append(int type, java.lang.String msg) {
        if (type == 1 || type == 12 || type == 20 || type == 19 || type == 18 || type == 21) {
            synchronized (INTERCEPTION_EVENTS) {
                INTERCEPTION_EVENTS.log(typeToString(type) + ": " + msg);
            }
        } else {
            synchronized (STATE_CHANGES) {
                STATE_CHANGES.log(typeToString(type) + ": " + msg);
            }
        }
    }

    public static void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        synchronized (INTERCEPTION_EVENTS) {
            pw.printf(prefix + "Interception Events:\n", new java.lang.Object[0]);
            INTERCEPTION_EVENTS.dump(prefix, pw);
        }
        synchronized (STATE_CHANGES) {
            pw.printf(prefix + "State Changes:\n", new java.lang.Object[0]);
            STATE_CHANGES.dump(prefix, pw);
        }
    }
}
