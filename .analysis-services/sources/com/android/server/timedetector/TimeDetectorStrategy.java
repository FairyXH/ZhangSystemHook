package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
public interface TimeDetectorStrategy extends com.android.server.timezonedetector.Dumpable {
    public static final int ORIGIN_EXTERNAL = 5;
    public static final int ORIGIN_GNSS = 4;
    public static final int ORIGIN_MANUAL = 2;
    public static final int ORIGIN_NETWORK = 3;
    public static final int ORIGIN_TELEPHONY = 1;

    @java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Origin {
    }

    void addChangeListener(com.android.server.timezonedetector.StateChangeListener stateChangeListener);

    void addNetworkTimeUpdateListener(com.android.server.timezonedetector.StateChangeListener stateChangeListener);

    void clearLatestNetworkSuggestion();

    boolean confirmTime(android.app.time.UnixEpochTime unixEpochTime);

    android.app.time.TimeCapabilitiesAndConfig getCapabilitiesAndConfig(int i, boolean z);

    com.android.server.timedetector.NetworkTimeSuggestion getLatestNetworkSuggestion();

    android.app.time.TimeState getTimeState();

    void setTimeState(android.app.time.TimeState timeState);

    void suggestExternalTime(android.app.time.ExternalTimeSuggestion externalTimeSuggestion);

    void suggestGnssTime(com.android.server.timedetector.GnssTimeSuggestion gnssTimeSuggestion);

    boolean suggestManualTime(int i, android.app.timedetector.ManualTimeSuggestion manualTimeSuggestion, boolean z);

    void suggestNetworkTime(com.android.server.timedetector.NetworkTimeSuggestion networkTimeSuggestion);

    void suggestTelephonyTime(android.app.timedetector.TelephonyTimeSuggestion telephonyTimeSuggestion);

    boolean updateConfiguration(int i, android.app.time.TimeConfiguration timeConfiguration, boolean z);

    static java.lang.String originToString(int origin) {
        switch (origin) {
            case 1:
                return "telephony";
            case 2:
                return "manual";
            case 3:
                return "network";
            case 4:
                return "gnss";
            case 5:
                return "external";
            default:
                throw new java.lang.IllegalArgumentException("origin=" + origin);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0049  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static int stringToOrigin(java.lang.String r6) {
        /*
            r0 = 0
            r1 = 1
            if (r6 == 0) goto L6
            r2 = r1
            goto L7
        L6:
            r2 = r0
        L7:
            com.android.internal.util.Preconditions.checkArgument(r2)
            int r2 = r6.hashCode()
            r3 = 4
            r4 = 3
            r5 = 2
            switch(r2) {
                case -1820761141: goto L3f;
                case -1081415738: goto L35;
                case 3177863: goto L2b;
                case 783201304: goto L20;
                case 1843485230: goto L15;
                default: goto L14;
            }
        L14:
            goto L49
        L15:
            java.lang.String r0 = "network"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L14
            r0 = r1
            goto L4a
        L20:
            java.lang.String r0 = "telephony"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L14
            r0 = r5
            goto L4a
        L2b:
            java.lang.String r0 = "gnss"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L14
            r0 = r4
            goto L4a
        L35:
            java.lang.String r2 = "manual"
            boolean r2 = r6.equals(r2)
            if (r2 == 0) goto L14
            goto L4a
        L3f:
            java.lang.String r0 = "external"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L14
            r0 = r3
            goto L4a
        L49:
            r0 = -1
        L4a:
            switch(r0) {
                case 0: goto L6c;
                case 1: goto L6b;
                case 2: goto L6a;
                case 3: goto L69;
                case 4: goto L67;
                default: goto L4d;
            }
        L4d:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "originString="
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r6)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L67:
            r0 = 5
            return r0
        L69:
            return r3
        L6a:
            return r1
        L6b:
            return r4
        L6c:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.timedetector.TimeDetectorStrategy.stringToOrigin(java.lang.String):int");
    }
}
