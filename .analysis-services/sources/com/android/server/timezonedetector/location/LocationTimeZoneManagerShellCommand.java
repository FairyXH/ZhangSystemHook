package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
class LocationTimeZoneManagerShellCommand extends android.os.ShellCommand {
    private final com.android.server.timezonedetector.location.LocationTimeZoneManagerService mService;

    LocationTimeZoneManagerShellCommand(com.android.server.timezonedetector.location.LocationTimeZoneManagerService service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0044  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r2) {
        /*
            r1 = this;
            if (r2 != 0) goto L7
            int r0 = r1.handleDefaultCommands(r2)
            return r0
        L7:
            int r0 = r2.hashCode()
            switch(r0) {
                case -385184143: goto L39;
                case 3540994: goto L2e;
                case 109757538: goto L23;
                case 248094771: goto L19;
                case 943200902: goto Lf;
                default: goto Le;
            }
        Le:
            goto L44
        Lf:
            java.lang.String r0 = "dump_state"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 4
            goto L45
        L19:
            java.lang.String r0 = "clear_recorded_provider_states"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 3
            goto L45
        L23:
            java.lang.String r0 = "start"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 0
            goto L45
        L2e:
            java.lang.String r0 = "stop"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 2
            goto L45
        L39:
            java.lang.String r0 = "start_with_test_providers"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 1
            goto L45
        L44:
            r0 = -1
        L45:
            switch(r0) {
                case 0: goto L61;
                case 1: goto L5c;
                case 2: goto L57;
                case 3: goto L52;
                case 4: goto L4d;
                default: goto L48;
            }
        L48:
            int r0 = r1.handleDefaultCommands(r2)
            return r0
        L4d:
            int r0 = r1.runDumpControllerState()
            return r0
        L52:
            int r0 = r1.runClearRecordedProviderStates()
            return r0
        L57:
            int r0 = r1.runStop()
            return r0
        L5c:
            int r0 = r1.runStartWithTestProviders()
            return r0
        L61:
            int r0 = r1.runStart()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.timezonedetector.location.LocationTimeZoneManagerShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.printf("Location Time Zone Manager (%s) commands for tests:\n", "location_time_zone_manager");
        pw.printf("  help\n", new java.lang.Object[0]);
        pw.printf("    Print this help text.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "start");
        pw.printf("    Starts the service, creating location time zone providers.\n", new java.lang.Object[0]);
        pw.printf("  %s <primary package name|%2$s> <secondary package name|%2$s> <record states>\n", "start_with_test_providers", "@null");
        pw.printf("    Starts the service with test provider packages configured / provider permission checks disabled.\n", new java.lang.Object[0]);
        pw.printf("    <record states> - true|false, determines whether state recording is enabled.\n", new java.lang.Object[0]);
        pw.printf("    See %s and %s.\n", "dump_state", "clear_recorded_provider_states");
        pw.printf("  %s\n", "stop");
        pw.printf("    Stops the service, destroying location time zone providers.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", "clear_recorded_provider_states");
        pw.printf("    Clears recorded provider state. See also %s and %s.\n", "start_with_test_providers", "dump_state");
        pw.printf("    Note: This is only intended for use during testing.\n", new java.lang.Object[0]);
        pw.printf("  %s [%s]\n", "dump_state", "--proto");
        pw.printf("    Dumps service state for tests as text or binary proto form.\n", new java.lang.Object[0]);
        pw.printf("    See the LocationTimeZoneManagerServiceStateProto definition for details.\n", new java.lang.Object[0]);
        pw.println();
        pw.printf("This service is also affected by the following device_config flags in the %s namespace:\n", "system_time");
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_PRIMARY_LTZP_MODE_OVERRIDE);
        pw.printf("    Overrides the mode of the primary provider. Values=%s|%s\n", com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_SECONDARY_LTZP_MODE_OVERRIDE);
        pw.printf("    Overrides the mode of the secondary provider. Values=%s|%s\n", com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_LOCATION_TIME_ZONE_DETECTION_UNCERTAINTY_DELAY_MILLIS);
        pw.printf("    Sets the amount of time the service waits when uncertain before making an 'uncertain' suggestion to the time zone detector.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_LTZP_INITIALIZATION_TIMEOUT_MILLIS);
        pw.printf("    Sets the initialization time passed to the providers.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_LTZP_INITIALIZATION_TIMEOUT_FUZZ_MILLIS);
        pw.printf("    Sets the amount of extra time added to the providers' initialization time.\n", new java.lang.Object[0]);
        pw.printf("  %s\n", com.android.server.timedetector.ServerFlags.KEY_LTZP_EVENT_FILTERING_AGE_THRESHOLD_MILLIS);
        pw.printf("    Sets the amount of time that must pass between equivalent LTZP events before they will be reported to the system server.\n", new java.lang.Object[0]);
        pw.println();
        pw.printf("Typically, use '%s' to stop the service before setting individual flags and '%s' after to restart it.\n", "stop", "start");
        pw.println();
        pw.printf("See \"adb shell cmd device_config\" for more information on setting flags.\n", new java.lang.Object[0]);
        pw.println();
        pw.printf("Also see \"adb shell cmd %s help\" for higher-level location time zone commands / settings.\n", "time_zone_detector");
        pw.println();
    }

    private int runStart() {
        try {
            this.mService.start();
            java.io.PrintWriter outPrintWriter = getOutPrintWriter();
            outPrintWriter.println("Service started");
            return 0;
        } catch (java.lang.RuntimeException e) {
            reportError(e);
            return 1;
        }
    }

    private int runStartWithTestProviders() {
        java.lang.String testPrimaryProviderPackageName = parseProviderPackageName(getNextArgRequired());
        java.lang.String testSecondaryProviderPackageName = parseProviderPackageName(getNextArgRequired());
        boolean recordProviderStateChanges = java.lang.Boolean.parseBoolean(getNextArgRequired());
        try {
            this.mService.startWithTestProviders(testPrimaryProviderPackageName, testSecondaryProviderPackageName, recordProviderStateChanges);
            java.io.PrintWriter outPrintWriter = getOutPrintWriter();
            outPrintWriter.println("Service started (test mode)");
            return 0;
        } catch (java.lang.RuntimeException e) {
            reportError(e);
            return 1;
        }
    }

    private int runStop() {
        try {
            this.mService.stop();
            java.io.PrintWriter outPrintWriter = getOutPrintWriter();
            outPrintWriter.println("Service stopped");
            return 0;
        } catch (java.lang.RuntimeException e) {
            reportError(e);
            return 1;
        }
    }

    private int runClearRecordedProviderStates() {
        try {
            this.mService.clearRecordedProviderStates();
            return 0;
        } catch (java.lang.IllegalStateException e) {
            reportError(e);
            return 2;
        }
    }

    private int runDumpControllerState() {
        com.android.internal.util.dump.DualDumpOutputStream outputStream;
        try {
            com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState state = this.mService.getStateForTests();
            if (state == null) {
                return 0;
            }
            boolean useProto = java.util.Objects.equals("--proto", getNextOption());
            if (useProto) {
                java.io.FileDescriptor outFd = getOutFileDescriptor();
                outputStream = new com.android.internal.util.dump.DualDumpOutputStream(new android.util.proto.ProtoOutputStream(outFd));
            } else {
                outputStream = new com.android.internal.util.dump.DualDumpOutputStream(new android.util.IndentingPrintWriter(getOutPrintWriter(), "  "));
            }
            if (state.getLastEvent() != null) {
                com.android.server.timezonedetector.LocationAlgorithmEvent lastEvent = state.getLastEvent();
                long lastEventToken = outputStream.start("last_event", 1146756268033L);
                android.app.time.LocationTimeZoneAlgorithmStatus algorithmStatus = lastEvent.getAlgorithmStatus();
                long algorithmStatusToken = outputStream.start("algorithm_status", 1146756268035L);
                outputStream.write("status", 1159641169921L, convertDetectionAlgorithmStatusToEnumToProtoEnum(algorithmStatus.getStatus()));
                outputStream.end(algorithmStatusToken);
                if (lastEvent.getSuggestion() != null) {
                    long suggestionToken = outputStream.start("suggestion", 1146756268033L);
                    com.android.server.timezonedetector.GeolocationTimeZoneSuggestion lastSuggestion = lastEvent.getSuggestion();
                    for (java.lang.String zoneId : lastSuggestion.getZoneIds()) {
                        outputStream.write("zone_ids", 2237677961217L, zoneId);
                        algorithmStatus = algorithmStatus;
                        lastSuggestion = lastSuggestion;
                    }
                    outputStream.end(suggestionToken);
                }
                for (java.lang.String debugInfo : lastEvent.getDebugInfo()) {
                    outputStream.write("debug_info", 2237677961218L, debugInfo);
                }
                outputStream.end(lastEventToken);
            }
            writeControllerStates(outputStream, state.getControllerStates());
            writeProviderStates(outputStream, state.getPrimaryProviderStates(), "primary_provider_states", 2246267895810L);
            writeProviderStates(outputStream, state.getSecondaryProviderStates(), "secondary_provider_states", 2246267895811L);
            outputStream.flush();
            return 0;
        } catch (java.lang.RuntimeException e) {
            reportError(e);
            return 1;
        }
    }

    private static void writeControllerStates(com.android.internal.util.dump.DualDumpOutputStream outputStream, java.util.List<java.lang.String> states) {
        for (java.lang.String state : states) {
            outputStream.write("controller_states", 2259152797700L, convertControllerStateToProtoEnum(state));
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0060  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int convertControllerStateToProtoEnum(java.lang.String r9) {
        /*
            int r0 = r9.hashCode()
            r1 = 0
            r2 = 7
            r3 = 6
            r4 = 5
            r5 = 4
            r6 = 3
            r7 = 2
            r8 = 1
            switch(r0) {
                case -1166336595: goto L56;
                case -468307734: goto L4c;
                case 433141802: goto L42;
                case 478389753: goto L38;
                case 872357833: goto L2e;
                case 1386911874: goto L24;
                case 1917201485: goto L1a;
                case 2066319421: goto L10;
                default: goto Lf;
            }
        Lf:
            goto L60
        L10:
            java.lang.String r0 = "FAILED"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r4
            goto L61
        L1a:
            java.lang.String r0 = "INITIALIZING"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r7
            goto L61
        L24:
            java.lang.String r0 = "CERTAIN"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r5
            goto L61
        L2e:
            java.lang.String r0 = "UNCERTAIN"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r6
            goto L61
        L38:
            java.lang.String r0 = "DESTROYED"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r3
            goto L61
        L42:
            java.lang.String r0 = "UNKNOWN"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r2
            goto L61
        L4c:
            java.lang.String r0 = "PROVIDERS_INITIALIZING"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r1
            goto L61
        L56:
            java.lang.String r0 = "STOPPED"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r8
            goto L61
        L60:
            r0 = -1
        L61:
            switch(r0) {
                case 0: goto L6b;
                case 1: goto L6a;
                case 2: goto L69;
                case 3: goto L68;
                case 4: goto L67;
                case 5: goto L66;
                case 6: goto L65;
                default: goto L64;
            }
        L64:
            return r1
        L65:
            return r2
        L66:
            return r3
        L67:
            return r4
        L68:
            return r5
        L69:
            return r6
        L6a:
            return r7
        L6b:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.timezonedetector.location.LocationTimeZoneManagerShellCommand.convertControllerStateToProtoEnum(java.lang.String):int");
    }

    private static void writeProviderStates(com.android.internal.util.dump.DualDumpOutputStream outputStream, java.util.List<com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState> providerStates, java.lang.String fieldName, long fieldId) {
        for (com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState providerState : providerStates) {
            long providerStateToken = outputStream.start(fieldName, fieldId);
            outputStream.write("state", 1159641169921L, convertProviderStateEnumToProtoEnum(providerState.stateEnum));
            outputStream.end(providerStateToken);
        }
    }

    private static int convertProviderStateEnumToProtoEnum(int stateEnum) {
        switch (stateEnum) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            default:
                throw new java.lang.IllegalArgumentException("Unknown stateEnum=" + stateEnum);
        }
    }

    private static int convertDetectionAlgorithmStatusToEnumToProtoEnum(int statusEnum) {
        switch (statusEnum) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                throw new java.lang.IllegalArgumentException("Unknown statusEnum=" + statusEnum);
        }
    }

    private void reportError(java.lang.Throwable e) {
        java.io.PrintWriter errPrintWriter = getErrPrintWriter();
        errPrintWriter.println("Error: ");
        e.printStackTrace(errPrintWriter);
    }

    private static java.lang.String parseProviderPackageName(java.lang.String providerPackageNameString) {
        if (providerPackageNameString.equals("@null")) {
            return null;
        }
        return providerPackageNameString;
    }
}
