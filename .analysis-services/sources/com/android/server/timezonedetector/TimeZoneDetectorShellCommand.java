package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
class TimeZoneDetectorShellCommand extends android.os.ShellCommand {
    private final com.android.server.timezonedetector.TimeZoneDetectorService mInterface;

    TimeZoneDetectorShellCommand(com.android.server.timezonedetector.TimeZoneDetectorService timeZoneDetectorService) {
        this.mInterface = timeZoneDetectorService;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00ab  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r2) {
        /*
            Method dump skipped, instruction units count: 340
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.timezonedetector.TimeZoneDetectorShellCommand.onCommand(java.lang.String):int");
    }

    private int runIsAutoDetectionEnabled() {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean enabled = this.mInterface.getCapabilitiesAndConfig(-2).getConfiguration().isAutoDetectionEnabled();
        pw.println(enabled);
        return 0;
    }

    private int runIsTelephonyDetectionSupported() {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean enabled = this.mInterface.isTelephonyTimeZoneDetectionSupported();
        pw.println(enabled);
        return 0;
    }

    private int runIsGeoDetectionSupported() {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean enabled = this.mInterface.isGeoTimeZoneDetectionSupported();
        pw.println(enabled);
        return 0;
    }

    private int runIsGeoDetectionEnabled() {
        java.io.PrintWriter pw = getOutPrintWriter();
        boolean enabled = this.mInterface.getCapabilitiesAndConfig(-2).getConfiguration().isGeoDetectionEnabled();
        pw.println(enabled);
        return 0;
    }

    private int runSetAutoDetectionEnabled() {
        return !this.mInterface.updateConfiguration(-2, new android.app.time.TimeZoneConfiguration.Builder().setAutoDetectionEnabled(java.lang.Boolean.parseBoolean(getNextArgRequired())).build()) ? 1 : 0;
    }

    private int runSetGeoDetectionEnabled() {
        return !this.mInterface.updateConfiguration(-2, new android.app.time.TimeZoneConfiguration.Builder().setGeoDetectionEnabled(java.lang.Boolean.parseBoolean(getNextArgRequired())).build()) ? 1 : 0;
    }

    private int runHandleLocationEvent() {
        java.util.function.Supplier supplier = new java.util.function.Supplier() { // from class: com.android.server.timezonedetector.TimeZoneDetectorShellCommand$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$runHandleLocationEvent$0();
            }
        };
        final com.android.server.timezonedetector.TimeZoneDetectorService timeZoneDetectorService = this.mInterface;
        java.util.Objects.requireNonNull(timeZoneDetectorService);
        return runSingleArgMethod(supplier, new java.util.function.Consumer() { // from class: com.android.server.timezonedetector.TimeZoneDetectorShellCommand$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                timeZoneDetectorService.handleLocationAlgorithmEvent((com.android.server.timezonedetector.LocationAlgorithmEvent) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.timezonedetector.LocationAlgorithmEvent lambda$runHandleLocationEvent$0() {
        return com.android.server.timezonedetector.LocationAlgorithmEvent.parseCommandLineArg(this);
    }

    private int runSuggestManualTimeZone() {
        java.util.function.Supplier supplier = new java.util.function.Supplier() { // from class: com.android.server.timezonedetector.TimeZoneDetectorShellCommand$$ExternalSyntheticLambda4
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$runSuggestManualTimeZone$1();
            }
        };
        final com.android.server.timezonedetector.TimeZoneDetectorService timeZoneDetectorService = this.mInterface;
        java.util.Objects.requireNonNull(timeZoneDetectorService);
        return runSingleArgMethod(supplier, new java.util.function.Consumer() { // from class: com.android.server.timezonedetector.TimeZoneDetectorShellCommand$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                timeZoneDetectorService.suggestManualTimeZone((android.app.timezonedetector.ManualTimeZoneSuggestion) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.app.timezonedetector.ManualTimeZoneSuggestion lambda$runSuggestManualTimeZone$1() {
        return android.app.timezonedetector.ManualTimeZoneSuggestion.parseCommandLineArg(this);
    }

    private int runSuggestTelephonyTimeZone() {
        java.util.function.Supplier supplier = new java.util.function.Supplier() { // from class: com.android.server.timezonedetector.TimeZoneDetectorShellCommand$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$runSuggestTelephonyTimeZone$2();
            }
        };
        final com.android.server.timezonedetector.TimeZoneDetectorService timeZoneDetectorService = this.mInterface;
        java.util.Objects.requireNonNull(timeZoneDetectorService);
        return runSingleArgMethod(supplier, new java.util.function.Consumer() { // from class: com.android.server.timezonedetector.TimeZoneDetectorShellCommand$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                timeZoneDetectorService.suggestTelephonyTimeZone((android.app.timezonedetector.TelephonyTimeZoneSuggestion) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.app.timezonedetector.TelephonyTimeZoneSuggestion lambda$runSuggestTelephonyTimeZone$2() {
        return android.app.timezonedetector.TelephonyTimeZoneSuggestion.parseCommandLineArg(this);
    }

    private <T> int runSingleArgMethod(java.util.function.Supplier<T> argParser, java.util.function.Consumer<T> invoker) {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            T arg = argParser.get();
            if (arg == null) {
                pw.println("Error: arg not specified");
                return 1;
            }
            invoker.accept(arg);
            pw.println("Arg " + arg + " injected.");
            return 0;
        } catch (java.lang.RuntimeException e) {
            pw.println(e);
            return 1;
        }
    }

    private int runEnableTelephonyFallback() {
        this.mInterface.enableTelephonyFallback("Command line");
        return 0;
    }

    private int runGetTimeZoneState() {
        android.app.time.TimeZoneState timeZoneState = this.mInterface.getTimeZoneState();
        getOutPrintWriter().println(timeZoneState);
        return 0;
    }

    private int runSetTimeZoneState() {
        android.app.time.TimeZoneState timeZoneState = android.app.time.TimeZoneState.parseCommandLineArgs(this);
        this.mInterface.setTimeZoneState(timeZoneState);
        return 0;
    }

    private int runConfirmTimeZone() {
        java.lang.String timeZoneId = parseTimeZoneIdArg(this);
        getOutPrintWriter().println(this.mInterface.confirmTimeZone(timeZoneId));
        return 0;
    }

    private static java.lang.String parseTimeZoneIdArg(android.os.ShellCommand cmd) {
        byte b;
        java.lang.String zoneId = null;
        while (true) {
            java.lang.String opt = cmd.getNextArg();
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1274807534:
                        if (opt.equals("--zone_id")) {
                            b = 0;
                            break;
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        zoneId = cmd.getNextArgRequired();
                        break;
                    default:
                        throw new java.lang.IllegalArgumentException("Unknown option: " + opt);
                }
            } else {
                if (zoneId == null) {
                    throw new java.lang.IllegalArgumentException("No zoneId specified.");
                }
                return zoneId;
            }
        }
    }

    private int runDumpMetrics() {
        java.io.PrintWriter pw = getOutPrintWriter();
        com.android.server.timezonedetector.MetricsTimeZoneDetectorState metricsState = this.mInterface.generateMetricsState();
        pw.println("MetricsTimeZoneDetectorState:");
        pw.println(metricsState.toString());
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.printf("Time Zone Detector (%s) commands:\n", "time_zone_detector");
        pw.printf("  help\n", new java.lang.Object[0]);
        pw.printf("    Print this help text.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "is_auto_detection_enabled");
        pw.printf("    Prints true/false according to the automatic time zone detection setting\n", new java.lang.Object[0]);
        pw.printf("  %s true|false\n", "set_auto_detection_enabled");
        pw.printf("    Sets the automatic time zone detection setting.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "is_telephony_detection_supported");
        pw.printf("    Prints true/false according to whether telephony time zone detection is supported on this device.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "is_geo_detection_supported");
        pw.printf("    Prints true/false according to whether geolocation time zone detection is supported on this device.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "is_geo_detection_enabled");
        pw.printf("    Prints true/false according to the geolocation time zone detection setting.\n", new java.lang.Object[0]);
        pw.printf("  %s true|false\n", "set_geo_detection_enabled");
        pw.printf("    Sets the geolocation time zone detection enabled setting.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "enable_telephony_fallback");
        pw.printf("    Signals that telephony time zone detection fall back can be used if geolocation detection is supported and enabled.\n)", new java.lang.Object[0]);
        pw.printf("    This is a temporary state until geolocation detection becomes \"certain\".\n", new java.lang.Object[0]);
        pw.printf("    To have an effect this requires that the telephony fallback feature is supported on the device, see below for device_config flags.\n", new java.lang.Object[0]);
        pw.printf("  %s <location event opts>\n", "handle_location_algorithm_event");
        pw.printf("    Simulates an event from the location time zone detection algorithm.\n", new java.lang.Object[0]);
        pw.printf("  %s <manual suggestion opts>\n", "suggest_manual_time_zone");
        pw.printf("    Suggests a time zone as if supplied by a user manually.\n", new java.lang.Object[0]);
        pw.printf("  %s <telephony suggestion opts>\n", "suggest_telephony_time_zone");
        pw.printf("    Simulates a time zone suggestion from the telephony time zone detection algorithm.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "get_time_zone_state");
        pw.printf("    Returns the current time zone setting state.\n", new java.lang.Object[0]);
        pw.printf("  %s <time zone state options>\n", "set_time_zone_state_for_tests");
        pw.printf("    Sets the current time zone state for tests.\n", new java.lang.Object[0]);
        pw.printf("  %s <--zone_id Olson ID>\n", "confirm_time_zone");
        pw.printf("    Tries to confirms the time zone, raising the confidence.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "dump_metrics");
        pw.printf("    Dumps the service metrics to stdout for inspection.\n", new java.lang.Object[0]);
        pw.println();
        com.android.server.timezonedetector.LocationAlgorithmEvent.printCommandLineOpts(pw);
        pw.println();
        android.app.timezonedetector.ManualTimeZoneSuggestion.printCommandLineOpts(pw);
        pw.println();
        android.app.timezonedetector.TelephonyTimeZoneSuggestion.printCommandLineOpts(pw);
        pw.println();
        android.app.time.TimeZoneState.printCommandLineOpts(pw);
        pw.println();
        pw.printf("This service is also affected by the following device_config flags in the %s namespace:\n", "system_time");
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_FEATURE_SUPPORTED);
        pw.printf("    Only observed if the geolocation time zone detection feature is enabled in config.\n", new java.lang.Object[0]);
        pw.printf("    Set this to false to disable the feature.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_RUN_IN_BACKGROUND_ENABLED);
        pw.printf("    Runs geolocation time zone detection even when it not enabled by the user. The result is not used to set the device's time zone [*]\n", new java.lang.Object[0]);
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_DEFAULT);
        pw.printf("    Only used if the device does not have an explicit 'geolocation time zone detection enabled' setting stored [*].\n", new java.lang.Object[0]);
        pw.printf("    The default is when unset is false.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_SETTING_ENABLED_OVERRIDE);
        pw.printf("    Used to override the device's 'geolocation time zone detection enabled' setting [*].\n", new java.lang.Object[0]);
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_TIME_ZONE_DETECTOR_AUTO_DETECTION_ENABLED_DEFAULT);
        pw.printf("    Used to set the automatic time zone detection enabled default, i.e. when the device's automatic time zone detection enabled setting hasn't been set explicitly. Intended for internal testers.", new java.lang.Object[0]);
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_TIME_ZONE_DETECTOR_TELEPHONY_FALLBACK_SUPPORTED);
        pw.printf("    Used to enable / disable support for telephony detection fallback. Also see the %s command.\n", "enable_telephony_fallback");
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_ENHANCED_METRICS_COLLECTION_ENABLED);
        pw.printf("    Used to increase the detail of metrics collected / reported.\n", new java.lang.Object[0]);
        pw.println();
        pw.printf("[*] To be enabled, the user must still have location = on / auto time zone detection = on.\n", new java.lang.Object[0]);
        pw.println();
        pw.printf("See \"adb shell cmd device_config\" for more information on setting flags.\n", new java.lang.Object[0]);
        pw.println();
        pw.printf("Also see \"adb shell cmd %s help\" for lower-level location time zone commands / settings.\n", "location_time_zone_manager");
        pw.println();
    }
}
