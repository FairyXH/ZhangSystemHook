package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class ZenModeFiltering {
    private static final boolean DEBUG = com.android.server.notification.ZenModeHelper.DEBUG;
    static final com.android.server.notification.ZenModeFiltering.RepeatCallers REPEAT_CALLERS = new com.android.server.notification.ZenModeFiltering.RepeatCallers();
    private static final java.lang.String TAG = "ZenModeHelper";
    private final android.content.Context mContext;
    private android.content.ComponentName mDefaultPhoneApp;
    private final com.android.internal.util.NotificationMessagingUtil mMessagingUtil;

    public ZenModeFiltering(android.content.Context context) {
        this.mContext = context;
        this.mMessagingUtil = new com.android.internal.util.NotificationMessagingUtil(this.mContext, (java.lang.Object) null);
    }

    public ZenModeFiltering(android.content.Context context, com.android.internal.util.NotificationMessagingUtil messagingUtil) {
        this.mContext = context;
        this.mMessagingUtil = messagingUtil;
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("mDefaultPhoneApp=");
        pw.println(this.mDefaultPhoneApp);
        pw.print(prefix);
        pw.print("RepeatCallers.mThresholdMinutes=");
        pw.println(REPEAT_CALLERS.mThresholdMinutes);
        synchronized (REPEAT_CALLERS) {
            if (!REPEAT_CALLERS.mTelCalls.isEmpty()) {
                pw.print(prefix);
                pw.println("RepeatCallers.mTelCalls=");
                for (int i = 0; i < REPEAT_CALLERS.mTelCalls.size(); i++) {
                    pw.print(prefix);
                    pw.print("  ");
                    pw.print((java.lang.String) REPEAT_CALLERS.mTelCalls.keyAt(i));
                    pw.print(" at ");
                    pw.println(ts(((java.lang.Long) REPEAT_CALLERS.mTelCalls.valueAt(i)).longValue()));
                }
            }
            if (!REPEAT_CALLERS.mOtherCalls.isEmpty()) {
                pw.print(prefix);
                pw.println("RepeatCallers.mOtherCalls=");
                for (int i2 = 0; i2 < REPEAT_CALLERS.mOtherCalls.size(); i2++) {
                    pw.print(prefix);
                    pw.print("  ");
                    pw.print((java.lang.String) REPEAT_CALLERS.mOtherCalls.keyAt(i2));
                    pw.print(" at ");
                    pw.println(ts(((java.lang.Long) REPEAT_CALLERS.mOtherCalls.valueAt(i2)).longValue()));
                }
            }
        }
    }

    private static java.lang.String ts(long time) {
        return new java.util.Date(time) + " (" + time + ")";
    }

    public static boolean matchesCallFilter(android.content.Context context, int zen, android.app.NotificationManager.Policy consolidatedPolicy, android.os.UserHandle userHandle, android.os.Bundle extras, com.android.server.notification.ValidateNotificationPeople validator, int contactsTimeoutMs, float timeoutAffinity, int callingUid) {
        if (zen == 2) {
            com.android.server.notification.ZenLog.traceMatchesCallFilter(false, "no interruptions", callingUid);
            return false;
        }
        if (zen == 3) {
            com.android.server.notification.ZenLog.traceMatchesCallFilter(false, "alarms only", callingUid);
            return false;
        }
        if (zen == 1) {
            if (consolidatedPolicy.allowRepeatCallers() && REPEAT_CALLERS.isRepeat(context, extras, null)) {
                com.android.server.notification.ZenLog.traceMatchesCallFilter(true, "repeat caller", callingUid);
                return true;
            }
            if (!consolidatedPolicy.allowCalls()) {
                com.android.server.notification.ZenLog.traceMatchesCallFilter(false, "calls not allowed", callingUid);
                return false;
            }
            if (validator != null) {
                float contactAffinity = validator.getContactAffinity(userHandle, extras, contactsTimeoutMs, timeoutAffinity);
                boolean match = audienceMatches(consolidatedPolicy.allowCallsFrom(), contactAffinity);
                com.android.server.notification.ZenLog.traceMatchesCallFilter(match, "contact affinity " + contactAffinity, callingUid);
                return match;
            }
        }
        com.android.server.notification.ZenLog.traceMatchesCallFilter(true, "no restrictions", callingUid);
        return true;
    }

    private static android.os.Bundle extras(com.android.server.notification.NotificationRecord record) {
        if (record == null || record.getSbn() == null || record.getSbn().getNotification() == null) {
            return null;
        }
        return record.getSbn().getNotification().extras;
    }

    protected void recordCall(com.android.server.notification.NotificationRecord record) {
        REPEAT_CALLERS.recordCall(this.mContext, extras(record), record.getPhoneNumbers());
    }

    private boolean canRecordBypassDnd(com.android.server.notification.NotificationRecord record, android.app.NotificationManager.Policy policy) {
        boolean inPriorityChannel = record.getPackagePriority() == 2;
        if (android.app.Flags.modesApi()) {
            return inPriorityChannel && policy.allowPriorityChannels();
        }
        return inPriorityChannel;
    }

    public boolean shouldIntercept(int zen, android.app.NotificationManager.Policy policy, com.android.server.notification.NotificationRecord record) {
        if (zen == 0) {
            return false;
        }
        if (isCritical(record)) {
            maybeLogInterceptDecision(record, false, "criticalNotification");
            return false;
        }
        if (android.app.NotificationManager.Policy.areAllVisualEffectsSuppressed(policy.suppressedVisualEffects) && com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(record.getSbn().getPackageName()) && 48 == record.getSbn().getId()) {
            maybeLogInterceptDecision(record, false, "systemDndChangedNotification");
            return false;
        }
        switch (zen) {
            case 1:
                if (canRecordBypassDnd(record, policy)) {
                    maybeLogInterceptDecision(record, false, "priorityApp");
                } else if (isAlarm(record)) {
                    if (policy.allowAlarms()) {
                        maybeLogInterceptDecision(record, false, "allowedAlarm");
                    } else {
                        maybeLogInterceptDecision(record, true, "!allowAlarms");
                    }
                } else if (isEvent(record)) {
                    if (policy.allowEvents()) {
                        maybeLogInterceptDecision(record, false, "allowedEvent");
                    } else {
                        maybeLogInterceptDecision(record, true, "!allowEvents");
                    }
                } else if (isReminder(record)) {
                    if (policy.allowReminders()) {
                        maybeLogInterceptDecision(record, false, "allowedReminder");
                    } else {
                        maybeLogInterceptDecision(record, true, "!allowReminders");
                    }
                } else if (isMedia(record)) {
                    if (policy.allowMedia()) {
                        maybeLogInterceptDecision(record, false, "allowedMedia");
                    } else {
                        maybeLogInterceptDecision(record, true, "!allowMedia");
                    }
                } else if (isSystem(record)) {
                    if (policy.allowSystem()) {
                        maybeLogInterceptDecision(record, false, "allowedSystem");
                    } else {
                        maybeLogInterceptDecision(record, true, "!allowSystem");
                    }
                } else {
                    if (isConversation(record) && policy.allowConversations()) {
                        if (policy.priorityConversationSenders == 1) {
                            maybeLogInterceptDecision(record, false, "conversationAnyone");
                        } else if (policy.priorityConversationSenders == 2 && record.getChannel().isImportantConversation()) {
                            maybeLogInterceptDecision(record, false, "conversationMatches");
                        }
                    }
                    if (isCall(record)) {
                        if (policy.allowRepeatCallers() && REPEAT_CALLERS.isRepeat(this.mContext, extras(record), record.getPhoneNumbers())) {
                            maybeLogInterceptDecision(record, false, "repeatCaller");
                        } else if (!policy.allowCalls()) {
                            maybeLogInterceptDecision(record, true, "!allowCalls");
                        }
                    } else if (!isMessage(record)) {
                        maybeLogInterceptDecision(record, true, "!priority");
                    } else if (!policy.allowMessages()) {
                        maybeLogInterceptDecision(record, true, "!allowMessages");
                    }
                    break;
                }
                break;
            case 2:
                maybeLogInterceptDecision(record, true, "none");
                break;
            case 3:
                if (isAlarm(record)) {
                    maybeLogInterceptDecision(record, false, com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
                } else {
                    maybeLogInterceptDecision(record, true, "alarmsOnly");
                }
                break;
            default:
                maybeLogInterceptDecision(record, false, "unknownZenMode");
                break;
        }
        return false;
    }

    private static void maybeLogInterceptDecision(com.android.server.notification.NotificationRecord record, boolean intercept, java.lang.String reason) {
        boolean interceptBefore = record.isIntercepted();
        if (record.hasInterceptBeenSet() && interceptBefore == intercept) {
            return;
        }
        java.lang.String annotatedReason = reason;
        if (!record.hasInterceptBeenSet()) {
            annotatedReason = "new:" + reason;
        } else if (interceptBefore != intercept) {
            annotatedReason = "updated:" + reason;
        }
        if (intercept) {
            com.android.server.notification.ZenLog.traceIntercepted(record, annotatedReason);
        } else {
            com.android.server.notification.ZenLog.traceNotIntercepted(record, annotatedReason);
        }
    }

    private boolean isCritical(com.android.server.notification.NotificationRecord record) {
        return record.getCriticality() < 2;
    }

    private static boolean shouldInterceptAudience(int source, com.android.server.notification.NotificationRecord record) {
        float affinity = record.getContactAffinity();
        if (!audienceMatches(source, affinity)) {
            maybeLogInterceptDecision(record, true, "!audienceMatches,affinity=" + affinity);
            return true;
        }
        maybeLogInterceptDecision(record, false, "affinity=" + affinity);
        return false;
    }

    protected static boolean isAlarm(com.android.server.notification.NotificationRecord record) {
        return record.isCategory(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM) || record.isAudioAttributesUsage(4);
    }

    private static boolean isEvent(com.android.server.notification.NotificationRecord record) {
        return record.isCategory("event");
    }

    private static boolean isReminder(com.android.server.notification.NotificationRecord record) {
        return record.isCategory("reminder");
    }

    public boolean isCall(com.android.server.notification.NotificationRecord record) {
        return record != null && (isDefaultPhoneApp(record.getSbn().getPackageName()) || record.isCategory("call"));
    }

    public boolean isMedia(com.android.server.notification.NotificationRecord record) {
        android.media.AudioAttributes aa = record.getAudioAttributes();
        return aa != null && android.media.AudioAttributes.SUPPRESSIBLE_USAGES.get(aa.getUsage()) == 5;
    }

    public boolean isSystem(com.android.server.notification.NotificationRecord record) {
        android.media.AudioAttributes aa = record.getAudioAttributes();
        return aa != null && android.media.AudioAttributes.SUPPRESSIBLE_USAGES.get(aa.getUsage()) == 6;
    }

    private boolean isDefaultPhoneApp(java.lang.String pkg) {
        if (this.mDefaultPhoneApp == null) {
            android.telecom.TelecomManager telecomm = (android.telecom.TelecomManager) this.mContext.getSystemService("telecom");
            this.mDefaultPhoneApp = telecomm != null ? telecomm.getDefaultPhoneApp() : null;
            if (DEBUG) {
                android.util.Slog.d(TAG, "Default phone app: " + this.mDefaultPhoneApp);
            }
        }
        return (pkg == null || this.mDefaultPhoneApp == null || !pkg.equals(this.mDefaultPhoneApp.getPackageName())) ? false : true;
    }

    protected boolean isMessage(com.android.server.notification.NotificationRecord record) {
        return this.mMessagingUtil.isMessaging(record.getSbn());
    }

    protected boolean isConversation(com.android.server.notification.NotificationRecord record) {
        return record.isConversation();
    }

    private static boolean audienceMatches(int source, float contactAffinity) {
        switch (source) {
            case 0:
                break;
            case 1:
                if (contactAffinity >= 0.5f) {
                }
                break;
            case 2:
                if (contactAffinity >= 1.0f) {
                }
                break;
            default:
                android.util.Slog.w(TAG, "Encountered unknown source: " + source);
                break;
        }
        return true;
    }

    protected void cleanUpCallersAfter(long timeThreshold) {
        REPEAT_CALLERS.cleanUpCallsAfter(timeThreshold);
    }

    private static class RepeatCallers {
        private final android.util.ArrayMap<java.lang.String, java.lang.Long> mOtherCalls;
        private final android.util.ArrayMap<java.lang.String, java.lang.Long> mTelCalls;
        private int mThresholdMinutes;

        private RepeatCallers() {
            this.mTelCalls = new android.util.ArrayMap<>();
            this.mOtherCalls = new android.util.ArrayMap<>();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void recordCall(android.content.Context context, android.os.Bundle extras, android.util.ArraySet<java.lang.String> phoneNumbers) {
            setThresholdMinutes(context);
            if (this.mThresholdMinutes > 0 && extras != null) {
                java.lang.String[] extraPeople = com.android.server.notification.ValidateNotificationPeople.getExtraPeople(extras);
                if (extraPeople != null && extraPeople.length != 0) {
                    long now = java.lang.System.currentTimeMillis();
                    cleanUp(this.mTelCalls, now);
                    cleanUp(this.mOtherCalls, now);
                    recordCallers(extraPeople, phoneNumbers, now);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean isRepeat(android.content.Context context, android.os.Bundle extras, android.util.ArraySet<java.lang.String> phoneNumbers) {
            setThresholdMinutes(context);
            if (this.mThresholdMinutes > 0 && extras != null) {
                java.lang.String[] extraPeople = com.android.server.notification.ValidateNotificationPeople.getExtraPeople(extras);
                if (extraPeople != null && extraPeople.length != 0) {
                    long now = java.lang.System.currentTimeMillis();
                    cleanUp(this.mTelCalls, now);
                    cleanUp(this.mOtherCalls, now);
                    return checkCallers(context, extraPeople, phoneNumbers);
                }
                return false;
            }
            return false;
        }

        private synchronized void cleanUp(android.util.ArrayMap<java.lang.String, java.lang.Long> calls, long now) {
            int N = calls.size();
            for (int i = N - 1; i >= 0; i--) {
                long time = calls.valueAt(i).longValue();
                if (time > now || now - time > this.mThresholdMinutes * 1000 * 60) {
                    calls.removeAt(i);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void cleanUpCallsAfter(long timeThreshold) {
            for (int i = this.mTelCalls.size() - 1; i >= 0; i--) {
                long time = this.mTelCalls.valueAt(i).longValue();
                if (time > timeThreshold) {
                    this.mTelCalls.removeAt(i);
                }
            }
            for (int j = this.mOtherCalls.size() - 1; j >= 0; j--) {
                long time2 = this.mOtherCalls.valueAt(j).longValue();
                if (time2 > timeThreshold) {
                    this.mOtherCalls.removeAt(j);
                }
            }
        }

        private void setThresholdMinutes(android.content.Context context) {
            if (this.mThresholdMinutes <= 0) {
                this.mThresholdMinutes = context.getResources().getInteger(android.R.integer.config_supportsNonResizableMultiWindow);
            }
        }

        private synchronized void recordCallers(java.lang.String[] people, android.util.ArraySet<java.lang.String> phoneNumbers, long now) {
            boolean recorded = false;
            boolean hasTel = false;
            boolean hasOther = false;
            for (java.lang.String person : people) {
                if (person != null) {
                    android.net.Uri uri = android.net.Uri.parse(person);
                    if ("tel".equals(uri.getScheme())) {
                        java.lang.String tel = android.net.Uri.decode(uri.getSchemeSpecificPart());
                        if (tel != null) {
                            this.mTelCalls.put(tel, java.lang.Long.valueOf(now));
                            recorded = true;
                            hasTel = true;
                        }
                    } else {
                        this.mOtherCalls.put(person, java.lang.Long.valueOf(now));
                        recorded = true;
                        hasOther = true;
                    }
                }
            }
            if (phoneNumbers != null) {
                for (java.lang.String num : phoneNumbers) {
                    if (num != null) {
                        this.mTelCalls.put(num, java.lang.Long.valueOf(now));
                        recorded = true;
                        hasTel = true;
                    }
                }
            }
            if (recorded) {
                com.android.server.notification.ZenLog.traceRecordCaller(hasTel, hasOther);
            }
        }

        private synchronized boolean checkForNumber(java.lang.String number, java.lang.String defaultCountryCode) {
            if (this.mTelCalls.containsKey(number)) {
                return true;
            }
            java.lang.String numberToCheck = android.net.Uri.decode(number);
            if (numberToCheck != null) {
                for (java.lang.String prev : this.mTelCalls.keySet()) {
                    if (android.telephony.PhoneNumberUtils.areSamePhoneNumber(numberToCheck, prev, defaultCountryCode)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private synchronized boolean checkCallers(android.content.Context context, java.lang.String[] people, android.util.ArraySet<java.lang.String> phoneNumbers) {
            boolean found;
            found = false;
            boolean checkedTel = false;
            boolean checkedOther = false;
            java.lang.String defaultCountryCode = ((android.telephony.TelephonyManager) context.getSystemService(android.telephony.TelephonyManager.class)).getNetworkCountryIso();
            for (java.lang.String person : people) {
                if (person != null) {
                    android.net.Uri uri = android.net.Uri.parse(person);
                    if ("tel".equals(uri.getScheme())) {
                        java.lang.String number = uri.getSchemeSpecificPart();
                        checkedTel = true;
                        if (checkForNumber(number, defaultCountryCode)) {
                            found = true;
                        }
                    } else {
                        checkedOther = true;
                        if (this.mOtherCalls.containsKey(person)) {
                            found = true;
                        }
                    }
                }
            }
            if (phoneNumbers != null) {
                for (java.lang.String num : phoneNumbers) {
                    checkedTel = true;
                    if (checkForNumber(num, defaultCountryCode)) {
                        found = true;
                    }
                }
            }
            com.android.server.notification.ZenLog.traceCheckRepeatCaller(found, checkedTel, checkedOther);
            return found;
        }
    }
}
