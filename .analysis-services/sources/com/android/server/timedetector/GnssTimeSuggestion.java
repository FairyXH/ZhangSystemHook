package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class GnssTimeSuggestion {
    private final android.app.timedetector.TimeSuggestionHelper mTimeSuggestionHelper;

    public GnssTimeSuggestion(android.app.time.UnixEpochTime unixEpochTime) {
        this.mTimeSuggestionHelper = new android.app.timedetector.TimeSuggestionHelper(com.android.server.timedetector.GnssTimeSuggestion.class, unixEpochTime);
    }

    private GnssTimeSuggestion(android.app.timedetector.TimeSuggestionHelper helper) {
        this.mTimeSuggestionHelper = (android.app.timedetector.TimeSuggestionHelper) java.util.Objects.requireNonNull(helper);
    }

    public android.app.time.UnixEpochTime getUnixEpochTime() {
        return this.mTimeSuggestionHelper.getUnixEpochTime();
    }

    public java.util.List<java.lang.String> getDebugInfo() {
        return this.mTimeSuggestionHelper.getDebugInfo();
    }

    public void addDebugInfo(java.lang.String... debugInfos) {
        this.mTimeSuggestionHelper.addDebugInfo(debugInfos);
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.android.server.timedetector.GnssTimeSuggestion that = (com.android.server.timedetector.GnssTimeSuggestion) o;
        return this.mTimeSuggestionHelper.handleEquals(that.mTimeSuggestionHelper);
    }

    public int hashCode() {
        return this.mTimeSuggestionHelper.hashCode();
    }

    public java.lang.String toString() {
        return this.mTimeSuggestionHelper.handleToString();
    }

    public static com.android.server.timedetector.GnssTimeSuggestion parseCommandLineArg(android.os.ShellCommand cmd) throws java.lang.IllegalArgumentException {
        android.app.timedetector.TimeSuggestionHelper suggestionHelper = android.app.timedetector.TimeSuggestionHelper.handleParseCommandLineArg(com.android.server.timedetector.GnssTimeSuggestion.class, cmd);
        return new com.android.server.timedetector.GnssTimeSuggestion(suggestionHelper);
    }

    public static void printCommandLineOpts(java.io.PrintWriter pw) {
        android.app.timedetector.TimeSuggestionHelper.handlePrintCommandLineOpts(pw, "GNSS", com.android.server.timedetector.GnssTimeSuggestion.class);
    }
}
