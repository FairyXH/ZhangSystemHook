package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class LocationAlgorithmEvent {
    private final android.app.time.LocationTimeZoneAlgorithmStatus mAlgorithmStatus;
    private java.util.ArrayList<java.lang.String> mDebugInfo;
    private final com.android.server.timezonedetector.GeolocationTimeZoneSuggestion mSuggestion;

    public LocationAlgorithmEvent(android.app.time.LocationTimeZoneAlgorithmStatus algorithmStatus, com.android.server.timezonedetector.GeolocationTimeZoneSuggestion suggestion) {
        this.mAlgorithmStatus = (android.app.time.LocationTimeZoneAlgorithmStatus) java.util.Objects.requireNonNull(algorithmStatus);
        this.mSuggestion = suggestion;
    }

    public android.app.time.LocationTimeZoneAlgorithmStatus getAlgorithmStatus() {
        return this.mAlgorithmStatus;
    }

    public com.android.server.timezonedetector.GeolocationTimeZoneSuggestion getSuggestion() {
        return this.mSuggestion;
    }

    public java.util.List<java.lang.String> getDebugInfo() {
        return this.mDebugInfo == null ? java.util.Collections.emptyList() : java.util.Collections.unmodifiableList(this.mDebugInfo);
    }

    public void addDebugInfo(java.lang.String... debugInfos) {
        if (this.mDebugInfo == null) {
            this.mDebugInfo = new java.util.ArrayList<>();
        }
        this.mDebugInfo.addAll(java.util.Arrays.asList(debugInfos));
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.android.server.timezonedetector.LocationAlgorithmEvent that = (com.android.server.timezonedetector.LocationAlgorithmEvent) o;
        if (this.mAlgorithmStatus.equals(that.mAlgorithmStatus) && java.util.Objects.equals(this.mSuggestion, that.mSuggestion)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mAlgorithmStatus, this.mSuggestion);
    }

    public java.lang.String toString() {
        return "LocationAlgorithmEvent{mAlgorithmStatus=" + this.mAlgorithmStatus + ", mSuggestion=" + this.mSuggestion + ", mDebugInfo=" + this.mDebugInfo + '}';
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0025  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static com.android.server.timezonedetector.LocationAlgorithmEvent parseCommandLineArg(android.os.ShellCommand r7) {
        /*
            r0 = 0
            r1 = 0
        L2:
            java.lang.String r2 = r7.getNextArg()
            r3 = r2
            if (r2 == 0) goto L52
            int r2 = r3.hashCode()
            switch(r2) {
                case -841922652: goto L1b;
                case 1507532178: goto L11;
                default: goto L10;
            }
        L10:
            goto L25
        L11:
            java.lang.String r2 = "--status"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L10
            r2 = 0
            goto L26
        L1b:
            java.lang.String r2 = "--suggestion"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L10
            r2 = 1
            goto L26
        L25:
            r2 = -1
        L26:
            switch(r2) {
                case 0: goto L47;
                case 1: goto L42;
                default: goto L29;
            }
        L29:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Unknown option: "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r3)
            java.lang.String r4 = r4.toString()
            r2.<init>(r4)
            throw r2
        L42:
            java.lang.String r0 = r7.getNextArgRequired()
            goto L51
        L47:
            java.lang.String r2 = r7.getNextArgRequired()
            android.app.time.LocationTimeZoneAlgorithmStatus r1 = android.app.time.LocationTimeZoneAlgorithmStatus.parseCommandlineArg(r2)
        L51:
            goto L2
        L52:
            if (r1 == 0) goto L79
            r2 = 0
            if (r0 == 0) goto L6a
            java.util.List r4 = parseZoneIds(r0)
            long r5 = android.os.SystemClock.elapsedRealtime()
            if (r4 != 0) goto L66
            com.android.server.timezonedetector.GeolocationTimeZoneSuggestion r2 = com.android.server.timezonedetector.GeolocationTimeZoneSuggestion.createUncertainSuggestion(r5)
            goto L6a
        L66:
            com.android.server.timezonedetector.GeolocationTimeZoneSuggestion r2 = com.android.server.timezonedetector.GeolocationTimeZoneSuggestion.createCertainSuggestion(r5, r4)
        L6a:
            com.android.server.timezonedetector.LocationAlgorithmEvent r4 = new com.android.server.timezonedetector.LocationAlgorithmEvent
            r4.<init>(r1, r2)
            java.lang.String r5 = "Command line injection"
            java.lang.String[] r5 = new java.lang.String[]{r5}
            r4.addDebugInfo(r5)
            return r4
        L79:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.String r4 = "Missing --status"
            r2.<init>(r4)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.timezonedetector.LocationAlgorithmEvent.parseCommandLineArg(android.os.ShellCommand):com.android.server.timezonedetector.LocationAlgorithmEvent");
    }

    private static java.util.List<java.lang.String> parseZoneIds(java.lang.String zoneIdsString) {
        if ("UNCERTAIN".equals(zoneIdsString)) {
            return null;
        }
        if ("EMPTY".equals(zoneIdsString)) {
            return java.util.Collections.emptyList();
        }
        java.util.ArrayList<java.lang.String> zoneIds = new java.util.ArrayList<>();
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(zoneIdsString, ",");
        while (tokenizer.hasMoreTokens()) {
            zoneIds.add(tokenizer.nextToken());
        }
        return zoneIds;
    }

    static void printCommandLineOpts(java.io.PrintWriter pw) {
        pw.println("Location algorithm event options:");
        pw.println("  --status {LocationTimeZoneAlgorithmStatus toString() format}");
        pw.println("  [--suggestion {UNCERTAIN|EMPTY|<Olson ID>+}]");
        pw.println();
        pw.println("See " + com.android.server.timezonedetector.LocationAlgorithmEvent.class.getName() + " for more information");
    }
}
