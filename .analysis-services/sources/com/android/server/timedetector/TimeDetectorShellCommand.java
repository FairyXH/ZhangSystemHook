package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
class TimeDetectorShellCommand extends android.os.ShellCommand {
    private final com.android.server.timedetector.TimeDetectorService mInterface;

    TimeDetectorShellCommand(com.android.server.timedetector.TimeDetectorService timeDetectorService) {
        this.mInterface = timeDetectorService;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00ae  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r2) {
        /*
            Method dump skipped, instruction units count: 344
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.timedetector.TimeDetectorShellCommand.onCommand(java.lang.String):int");
    }

    private int runIsAutoDetectionEnabled() {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean enabled = this.mInterface.getCapabilitiesAndConfig().getConfiguration().isAutoDetectionEnabled();
        pw.println(enabled);
        return 0;
    }

    private int runSetAutoDetectionEnabled() {
        return !this.mInterface.updateConfiguration(-2, new android.app.time.TimeConfiguration.Builder().setAutoDetectionEnabled(java.lang.Boolean.parseBoolean(getNextArgRequired())).build()) ? 1 : 0;
    }

    private int runSuggestManualTime() {
        java.util.function.Supplier supplier = new java.util.function.Supplier() { // from class: com.android.server.timedetector.TimeDetectorShellCommand$$ExternalSyntheticLambda8
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$runSuggestManualTime$0();
            }
        };
        final com.android.server.timedetector.TimeDetectorService timeDetectorService = this.mInterface;
        java.util.Objects.requireNonNull(timeDetectorService);
        return runSuggestTime(supplier, new java.util.function.Consumer() { // from class: com.android.server.timedetector.TimeDetectorShellCommand$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                timeDetectorService.suggestManualTime((android.app.timedetector.ManualTimeSuggestion) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.app.timedetector.ManualTimeSuggestion lambda$runSuggestManualTime$0() {
        return android.app.timedetector.ManualTimeSuggestion.parseCommandLineArg(this);
    }

    private int runSuggestTelephonyTime() {
        java.util.function.Supplier supplier = new java.util.function.Supplier() { // from class: com.android.server.timedetector.TimeDetectorShellCommand$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$runSuggestTelephonyTime$1();
            }
        };
        final com.android.server.timedetector.TimeDetectorService timeDetectorService = this.mInterface;
        java.util.Objects.requireNonNull(timeDetectorService);
        return runSuggestTime(supplier, new java.util.function.Consumer() { // from class: com.android.server.timedetector.TimeDetectorShellCommand$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                timeDetectorService.suggestTelephonyTime((android.app.timedetector.TelephonyTimeSuggestion) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.app.timedetector.TelephonyTimeSuggestion lambda$runSuggestTelephonyTime$1() {
        return android.app.timedetector.TelephonyTimeSuggestion.parseCommandLineArg(this);
    }

    private int runSuggestNetworkTime() {
        java.util.function.Supplier supplier = new java.util.function.Supplier() { // from class: com.android.server.timedetector.TimeDetectorShellCommand$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$runSuggestNetworkTime$2();
            }
        };
        final com.android.server.timedetector.TimeDetectorService timeDetectorService = this.mInterface;
        java.util.Objects.requireNonNull(timeDetectorService);
        return runSuggestTime(supplier, new java.util.function.Consumer() { // from class: com.android.server.timedetector.TimeDetectorShellCommand$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                timeDetectorService.suggestNetworkTime((com.android.server.timedetector.NetworkTimeSuggestion) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.timedetector.NetworkTimeSuggestion lambda$runSuggestNetworkTime$2() {
        return com.android.server.timedetector.NetworkTimeSuggestion.parseCommandLineArg(this);
    }

    private int runGetLatestNetworkTime() {
        com.android.server.timedetector.NetworkTimeSuggestion networkTimeSuggestion = this.mInterface.getLatestNetworkSuggestion();
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println(networkTimeSuggestion);
        return 0;
    }

    private int runClearLatestNetworkTime() {
        this.mInterface.clearLatestNetworkTime();
        return 0;
    }

    private int runSuggestGnssTime() {
        java.util.function.Supplier supplier = new java.util.function.Supplier() { // from class: com.android.server.timedetector.TimeDetectorShellCommand$$ExternalSyntheticLambda4
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$runSuggestGnssTime$3();
            }
        };
        final com.android.server.timedetector.TimeDetectorService timeDetectorService = this.mInterface;
        java.util.Objects.requireNonNull(timeDetectorService);
        return runSuggestTime(supplier, new java.util.function.Consumer() { // from class: com.android.server.timedetector.TimeDetectorShellCommand$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                timeDetectorService.suggestGnssTime((com.android.server.timedetector.GnssTimeSuggestion) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.timedetector.GnssTimeSuggestion lambda$runSuggestGnssTime$3() {
        return com.android.server.timedetector.GnssTimeSuggestion.parseCommandLineArg(this);
    }

    private int runSuggestExternalTime() {
        java.util.function.Supplier supplier = new java.util.function.Supplier() { // from class: com.android.server.timedetector.TimeDetectorShellCommand$$ExternalSyntheticLambda6
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$runSuggestExternalTime$4();
            }
        };
        final com.android.server.timedetector.TimeDetectorService timeDetectorService = this.mInterface;
        java.util.Objects.requireNonNull(timeDetectorService);
        return runSuggestTime(supplier, new java.util.function.Consumer() { // from class: com.android.server.timedetector.TimeDetectorShellCommand$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                timeDetectorService.suggestExternalTime((android.app.time.ExternalTimeSuggestion) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.app.time.ExternalTimeSuggestion lambda$runSuggestExternalTime$4() {
        return android.app.time.ExternalTimeSuggestion.parseCommandLineArg(this);
    }

    private <T> int runSuggestTime(java.util.function.Supplier<T> suggestionParser, java.util.function.Consumer<T> invoker) {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            T suggestion = suggestionParser.get();
            if (suggestion == null) {
                pw.println("Error: suggestion not specified");
                return 1;
            }
            invoker.accept(suggestion);
            pw.println("Suggestion " + suggestion + " injected.");
            return 0;
        } catch (java.lang.RuntimeException e) {
            pw.println(e);
            return 1;
        }
    }

    private int runGetTimeState() {
        android.app.time.TimeState timeState = this.mInterface.getTimeState();
        getOutPrintWriter().println(timeState);
        return 0;
    }

    private int runSetTimeState() {
        android.app.time.TimeState timeState = android.app.time.TimeState.parseCommandLineArgs(this);
        this.mInterface.setTimeState(timeState);
        return 0;
    }

    private int runConfirmTime() {
        android.app.time.UnixEpochTime unixEpochTime = android.app.time.UnixEpochTime.parseCommandLineArgs(this);
        getOutPrintWriter().println(this.mInterface.confirmTime(unixEpochTime));
        return 0;
    }

    private int runClearSystemClockNetworkTime() {
        this.mInterface.clearNetworkTimeForSystemClockForTests();
        return 0;
    }

    private int runSetSystemClockNetworkTime() {
        com.android.server.timedetector.NetworkTimeSuggestion networkTimeSuggestion = com.android.server.timedetector.NetworkTimeSuggestion.parseCommandLineArg(this);
        this.mInterface.setNetworkTimeForSystemClockForTests(networkTimeSuggestion.getUnixEpochTime(), networkTimeSuggestion.getUncertaintyMillis());
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.printf("Time Detector (%s) commands:\n", "time_detector");
        pw.printf("  help\n", new java.lang.Object[0]);
        pw.printf("    Print this help text.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "is_auto_detection_enabled");
        pw.printf("    Prints true/false according to the automatic time detection setting.\n", new java.lang.Object[0]);
        pw.printf("  %s true|false\n", "set_auto_detection_enabled");
        pw.printf("    Sets the automatic time detection setting.\n", new java.lang.Object[0]);
        pw.println();
        pw.printf("  %s <manual suggestion opts>\n", "suggest_manual_time");
        pw.printf("    Suggests a time as if via the \"manual\" origin.\n", new java.lang.Object[0]);
        pw.printf("  %s <telephony suggestion opts>\n", "suggest_telephony_time");
        pw.printf("    Suggests a time as if via the \"telephony\" origin.\n", new java.lang.Object[0]);
        pw.printf("  %s <network suggestion opts>\n", "suggest_network_time");
        pw.printf("    Suggests a time as if via the \"network\" origin.\n", new java.lang.Object[0]);
        pw.printf("  %s <gnss suggestion opts>\n", "suggest_gnss_time");
        pw.printf("    Suggests a time as if via the \"gnss\" origin.\n", new java.lang.Object[0]);
        pw.printf("  %s <external suggestion opts>\n", "suggest_external_time");
        pw.printf("    Suggests a time as if via the \"external\" origin.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "get_time_state");
        pw.printf("    Returns the current time setting state.\n", new java.lang.Object[0]);
        pw.printf("  %s <time state options>\n", "set_time_state_for_tests");
        pw.printf("    Sets the current time state for tests.\n", new java.lang.Object[0]);
        pw.printf("  %s <unix epoch time options>\n", "confirm_time");
        pw.printf("    Tries to confirms the time, raising the confidence.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "get_network_time");
        pw.printf("    Prints the network time information held by the detector.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "clear_network_time");
        pw.printf("    Clears the network time information held by the detector.\n", new java.lang.Object[0]);
        pw.printf("  %s <network suggestion opts>\n", "set_system_clock_network_time");
        pw.printf("    Sets the network time information used for SystemClock.currentNetworkTimeClock().\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "clear_system_clock_network_time");
        pw.printf("    Clears the network time information used for SystemClock.currentNetworkTimeClock().\n", new java.lang.Object[0]);
        pw.println();
        android.app.timedetector.ManualTimeSuggestion.printCommandLineOpts(pw);
        pw.println();
        android.app.timedetector.TelephonyTimeSuggestion.printCommandLineOpts(pw);
        pw.println();
        com.android.server.timedetector.NetworkTimeSuggestion.printCommandLineOpts(pw);
        pw.println();
        com.android.server.timedetector.GnssTimeSuggestion.printCommandLineOpts(pw);
        pw.println();
        android.app.time.ExternalTimeSuggestion.printCommandLineOpts(pw);
        pw.println();
        android.app.time.TimeState.printCommandLineOpts(pw);
        pw.println();
        android.app.time.UnixEpochTime.printCommandLineOpts(pw);
        pw.println();
        pw.printf("This service is also affected by the following device_config flags in the %s namespace:\n", "system_time");
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_TIME_DETECTOR_LOWER_BOUND_MILLIS_OVERRIDE);
        pw.printf("    The lower bound used to validate time suggestions when they are received.\n", new java.lang.Object[0]);
        pw.printf("    Specified in milliseconds since the start of the Unix epoch.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_TIME_DETECTOR_ORIGIN_PRIORITIES_OVERRIDE);
        pw.printf("    A comma separated list of origins. See TimeDetectorStrategy for details.\n", new java.lang.Object[0]);
        pw.println();
        pw.printf("See \"adb shell cmd device_config\" for more information on setting flags.\n", new java.lang.Object[0]);
        pw.println();
    }
}
