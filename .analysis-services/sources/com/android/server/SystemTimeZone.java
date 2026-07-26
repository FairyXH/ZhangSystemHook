package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class SystemTimeZone {
    private static final boolean DEBUG = false;
    private static final java.lang.String DEFAULT_TIME_ZONE_ID = "GMT";
    private static final java.lang.String TAG = "SystemTimeZone";
    public static final int TIME_ZONE_CONFIDENCE_HIGH = 100;
    public static final int TIME_ZONE_CONFIDENCE_LOW = 0;
    private static final java.lang.String TIME_ZONE_CONFIDENCE_SYSTEM_PROPERTY = "persist.sys.timezone_confidence";
    private static final java.lang.String TIME_ZONE_SYSTEM_PROPERTY = "persist.sys.timezone";
    private static final android.util.LocalLog sTimeZoneDebugLog = new android.util.LocalLog(30, false);

    @java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE_USE})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface TimeZoneConfidence {
    }

    private SystemTimeZone() {
    }

    public static void initializeTimeZoneSettingsIfRequired() {
        java.lang.String timezoneProperty = android.os.SystemProperties.get(TIME_ZONE_SYSTEM_PROPERTY);
        if (!isValidTimeZoneId(timezoneProperty)) {
            java.lang.String logInfo = "initializeTimeZoneSettingsIfRequired():persist.sys.timezone is not valid (" + timezoneProperty + "); setting to " + DEFAULT_TIME_ZONE_ID;
            android.util.Slog.w(TAG, logInfo);
            setTimeZoneId(DEFAULT_TIME_ZONE_ID, 0, logInfo);
        }
    }

    public static void addDebugLogEntry(java.lang.String logMsg) {
        sTimeZoneDebugLog.log(logMsg);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0036 A[Catch: all -> 0x0062, TryCatch #0 {, blocks: (B:9:0x0011, B:11:0x0017, B:18:0x002e, B:22:0x0060, B:21:0x0036, B:13:0x001d, B:16:0x0025), top: B:29:0x0011, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean setTimeZoneId(java.lang.String r6, int r7, java.lang.String r8) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 != 0) goto L65
            boolean r0 = isValidTimeZoneId(r6)
            if (r0 != 0) goto Ld
            goto L65
        Ld:
            r0 = 0
            java.lang.Class<com.android.server.SystemTimeZone> r1 = com.android.server.SystemTimeZone.class
            monitor-enter(r1)
            java.lang.String r2 = getTimeZoneId()     // Catch: java.lang.Throwable -> L62
            if (r2 == 0) goto L1d
            boolean r3 = r2.equals(r6)     // Catch: java.lang.Throwable -> L62
            if (r3 != 0) goto L2e
        L1d:
            java.lang.String r3 = "persist.sys.timezone"
            android.os.SystemProperties.set(r3, r6)     // Catch: java.lang.RuntimeException -> L24 java.lang.Throwable -> L62
            goto L2d
        L24:
            r3 = move-exception
            java.lang.String r4 = "SystemTimeZone"
            java.lang.String r5 = "set TIME_ZONE_SYSTEM_PROPERTY prop failed."
            android.util.Slog.d(r4, r5)     // Catch: java.lang.Throwable -> L62
        L2d:
            r0 = 1
        L2e:
            boolean r3 = setTimeZoneConfidence(r7)     // Catch: java.lang.Throwable -> L62
            if (r0 != 0) goto L36
            if (r3 == 0) goto L60
        L36:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L62
            r4.<init>()     // Catch: java.lang.Throwable -> L62
            java.lang.String r5 = "Time zone or confidence set:  (new) timeZoneId="
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Throwable -> L62
            java.lang.StringBuilder r4 = r4.append(r6)     // Catch: java.lang.Throwable -> L62
            java.lang.String r5 = ", (new) confidence="
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Throwable -> L62
            java.lang.StringBuilder r4 = r4.append(r7)     // Catch: java.lang.Throwable -> L62
            java.lang.String r5 = ", logInfo="
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Throwable -> L62
            java.lang.StringBuilder r4 = r4.append(r8)     // Catch: java.lang.Throwable -> L62
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L62
            addDebugLogEntry(r4)     // Catch: java.lang.Throwable -> L62
        L60:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L62
            return r0
        L62:
            r2 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L62
            throw r2
        L65:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "setTimeZoneId: Invalid time zone ID. timeZoneId="
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r6)
            java.lang.String r1 = ", confidence="
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r7)
            java.lang.String r1 = ", logInfo="
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r8)
            java.lang.String r0 = r0.toString()
            addDebugLogEntry(r0)
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.SystemTimeZone.setTimeZoneId(java.lang.String, int, java.lang.String):boolean");
    }

    private static boolean setTimeZoneConfidence(int newConfidence) {
        int currentConfidence = getTimeZoneConfidence();
        if (currentConfidence != newConfidence) {
            android.os.SystemProperties.set(TIME_ZONE_CONFIDENCE_SYSTEM_PROPERTY, java.lang.Integer.toString(newConfidence));
            return true;
        }
        return false;
    }

    public static int getTimeZoneConfidence() {
        int confidence = android.os.SystemProperties.getInt(TIME_ZONE_CONFIDENCE_SYSTEM_PROPERTY, 0);
        if (!isValidTimeZoneConfidence(confidence)) {
            return 0;
        }
        return confidence;
    }

    public static java.lang.String getTimeZoneId() {
        return android.os.SystemProperties.get(TIME_ZONE_SYSTEM_PROPERTY);
    }

    public static void dump(java.io.PrintWriter writer) {
        sTimeZoneDebugLog.dump(writer);
    }

    private static boolean isValidTimeZoneConfidence(int confidence) {
        return confidence >= 0 && confidence <= 100;
    }

    private static boolean isValidTimeZoneId(java.lang.String timeZoneId) {
        return (timeZoneId == null || timeZoneId.isEmpty() || !com.android.i18n.timezone.ZoneInfoDb.getInstance().hasTimeZone(timeZoneId)) ? false : true;
    }
}
