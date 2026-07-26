package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class NetworkTimeSuggestion {
    private java.util.ArrayList<java.lang.String> mDebugInfo;
    private final int mUncertaintyMillis;
    private final android.app.time.UnixEpochTime mUnixEpochTime;

    public NetworkTimeSuggestion(android.app.time.UnixEpochTime unixEpochTime, int uncertaintyMillis) {
        this.mUnixEpochTime = (android.app.time.UnixEpochTime) java.util.Objects.requireNonNull(unixEpochTime);
        if (uncertaintyMillis < 0) {
            throw new java.lang.IllegalArgumentException("uncertaintyMillis < 0");
        }
        this.mUncertaintyMillis = uncertaintyMillis;
    }

    public android.app.time.UnixEpochTime getUnixEpochTime() {
        return this.mUnixEpochTime;
    }

    public int getUncertaintyMillis() {
        return this.mUncertaintyMillis;
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
        if (!(o instanceof com.android.server.timedetector.NetworkTimeSuggestion)) {
            return false;
        }
        com.android.server.timedetector.NetworkTimeSuggestion that = (com.android.server.timedetector.NetworkTimeSuggestion) o;
        return this.mUnixEpochTime.equals(that.mUnixEpochTime) && this.mUncertaintyMillis == that.mUncertaintyMillis;
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mUnixEpochTime, java.lang.Integer.valueOf(this.mUncertaintyMillis));
    }

    public java.lang.String toString() {
        return "NetworkTimeSuggestion{mUnixEpochTime=" + this.mUnixEpochTime + ", mUncertaintyMillis=" + this.mUncertaintyMillis + ", mDebugInfo=" + this.mDebugInfo + '}';
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.android.server.timedetector.NetworkTimeSuggestion parseCommandLineArg(android.os.ShellCommand r9) throws java.lang.IllegalArgumentException {
        /*
            Method dump skipped, instruction units count: 220
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.timedetector.NetworkTimeSuggestion.parseCommandLineArg(android.os.ShellCommand):com.android.server.timedetector.NetworkTimeSuggestion");
    }

    public static void printCommandLineOpts(java.io.PrintWriter pw) {
        pw.printf("%s suggestion options:\n", "Network");
        pw.println("  --elapsed_realtime <elapsed realtime millis> - the elapsed realtime millis when unix epoch time was read");
        pw.println("  --unix_epoch_time <Unix epoch time millis>");
        pw.println("  --uncertainty_millis <Uncertainty millis> - a positive error bound (+/-) estimate for unix epoch time");
        pw.println();
        pw.println("See " + com.android.server.timedetector.NetworkTimeSuggestion.class.getName() + " for more information");
    }
}
