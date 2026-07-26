package com.android.server.location;

/* JADX INFO: loaded from: classes2.dex */
class LocationShellCommand extends com.android.modules.utils.BasicShellCommandHandler {
    private static final float DEFAULT_TEST_LOCATION_ACCURACY = 100.0f;
    private final android.content.Context mContext;
    private final com.android.server.location.LocationManagerService mService;

    LocationShellCommand(android.content.Context context, com.android.server.location.LocationManagerService service) {
        this.mContext = context;
        this.mService = (com.android.server.location.LocationManagerService) java.util.Objects.requireNonNull(service);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:29:0x005e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r3) {
        /*
            r2 = this;
            if (r3 != 0) goto L8
            r0 = 0
            int r0 = r2.handleDefaultCommands(r0)
            return r0
        L8:
            int r0 = r3.hashCode()
            r1 = 0
            switch(r0) {
                case -1064420500: goto L53;
                case -547571550: goto L48;
                case -444268534: goto L3d;
                case -361391806: goto L32;
                case -84945726: goto L27;
                case 1546249012: goto L1c;
                case 1640843002: goto L11;
                default: goto L10;
            }
        L10:
            goto L5e
        L11:
            java.lang.String r0 = "is-adas-gnss-location-enabled"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L10
            r0 = 2
            goto L5f
        L1c:
            java.lang.String r0 = "set-location-enabled"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L10
            r0 = 1
            goto L5f
        L27:
            java.lang.String r0 = "set-adas-gnss-location-enabled"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L10
            r0 = 3
            goto L5f
        L32:
            java.lang.String r0 = "set-automotive-gnss-suspended"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L10
            r0 = 4
            goto L5f
        L3d:
            java.lang.String r0 = "is-automotive-gnss-suspended"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L10
            r0 = 5
            goto L5f
        L48:
            java.lang.String r0 = "providers"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L10
            r0 = 6
            goto L5f
        L53:
            java.lang.String r0 = "is-location-enabled"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L10
            r0 = r1
            goto L5f
        L5e:
            r0 = -1
        L5f:
            switch(r0) {
                case 0: goto L84;
                case 1: goto L80;
                case 2: goto L7c;
                case 3: goto L78;
                case 4: goto L74;
                case 5: goto L70;
                case 6: goto L67;
                default: goto L62;
            }
        L62:
            int r0 = r2.handleDefaultCommands(r3)
            return r0
        L67:
            java.lang.String r0 = r2.getNextArgRequired()
            int r1 = r2.parseProvidersCommand(r0)
            return r1
        L70:
            r2.handleIsAutomotiveGnssSuspended()
            return r1
        L74:
            r2.handleSetAutomotiveGnssSuspended()
            return r1
        L78:
            r2.handleSetAdasGnssLocationEnabled()
            return r1
        L7c:
            r2.handleIsAdasGnssLocationEnabled()
            return r1
        L80:
            r2.handleSetLocationEnabled()
            return r1
        L84:
            r2.handleIsLocationEnabled()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.location.LocationShellCommand.onCommand(java.lang.String):int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int parseProvidersCommand(java.lang.String r3) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            r1 = 0
            switch(r0) {
                case -1669563581: goto L34;
                case -1650104991: goto L29;
                case -61579243: goto L1e;
                case 11404448: goto L14;
                case 2036447497: goto L9;
                default: goto L8;
            }
        L8:
            goto L3f
        L9:
            java.lang.String r0 = "send-extra-command"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L8
            r0 = 4
            goto L40
        L14:
            java.lang.String r0 = "add-test-provider"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L8
            r0 = r1
            goto L40
        L1e:
            java.lang.String r0 = "set-test-provider-enabled"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L8
            r0 = 2
            goto L40
        L29:
            java.lang.String r0 = "set-test-provider-location"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L8
            r0 = 3
            goto L40
        L34:
            java.lang.String r0 = "remove-test-provider"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L8
            r0 = 1
            goto L40
        L3f:
            r0 = -1
        L40:
            switch(r0) {
                case 0: goto L58;
                case 1: goto L54;
                case 2: goto L50;
                case 3: goto L4c;
                case 4: goto L48;
                default: goto L43;
            }
        L43:
            int r0 = r2.handleDefaultCommands(r3)
            return r0
        L48:
            r2.handleSendExtraCommand()
            return r1
        L4c:
            r2.handleSetTestProviderLocation()
            return r1
        L50:
            r2.handleSetTestProviderEnabled()
            return r1
        L54:
            r2.handleRemoveTestProvider()
            return r1
        L58:
            r2.handleAddTestProvider()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.location.LocationShellCommand.parseProvidersCommand(java.lang.String):int");
    }

    private void handleIsLocationEnabled() {
        int userId = -3;
        while (true) {
            java.lang.String option = getNextOption();
            if (option != null) {
                if ("--user".equals(option)) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    throw new java.lang.IllegalArgumentException("Unknown option: " + option);
                }
            } else {
                getOutPrintWriter().println(this.mService.isLocationEnabledForUser(userId));
                return;
            }
        }
    }

    private void handleSetLocationEnabled() {
        boolean enabled = java.lang.Boolean.parseBoolean(getNextArgRequired());
        int userId = -3;
        while (true) {
            java.lang.String option = getNextOption();
            if (option != null) {
                if ("--user".equals(option)) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    throw new java.lang.IllegalArgumentException("Unknown option: " + option);
                }
            } else {
                this.mService.setLocationEnabledForUser(enabled, userId);
                return;
            }
        }
    }

    private void handleIsAdasGnssLocationEnabled() {
        if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            throw new java.lang.IllegalStateException("command only recognized on automotive devices");
        }
        int userId = -3;
        while (true) {
            java.lang.String option = getNextOption();
            if (option != null) {
                if ("--user".equals(option)) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    throw new java.lang.IllegalArgumentException("Unknown option: " + option);
                }
            } else {
                getOutPrintWriter().println(this.mService.isAdasGnssLocationEnabledForUser(userId));
                return;
            }
        }
    }

    private void handleSetAdasGnssLocationEnabled() {
        if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            throw new java.lang.IllegalStateException("command only recognized on automotive devices");
        }
        boolean enabled = java.lang.Boolean.parseBoolean(getNextArgRequired());
        int userId = -3;
        while (true) {
            java.lang.String option = getNextOption();
            if (option != null) {
                if ("--user".equals(option)) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    throw new java.lang.IllegalArgumentException("Unknown option: " + option);
                }
            } else {
                this.mService.setAdasGnssLocationEnabledForUser(enabled, userId);
                return;
            }
        }
    }

    private void handleSetAutomotiveGnssSuspended() {
        if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            throw new java.lang.IllegalStateException("command only recognized on automotive devices");
        }
        boolean suspended = java.lang.Boolean.parseBoolean(getNextArgRequired());
        this.mService.setAutomotiveGnssSuspended(suspended);
    }

    private void handleIsAutomotiveGnssSuspended() {
        if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            throw new java.lang.IllegalStateException("command only recognized on automotive devices");
        }
        getOutPrintWriter().println(this.mService.isAutomotiveGnssSuspended());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00de  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void handleAddTestProvider() {
        /*
            Method dump skipped, instruction units count: 394
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.location.LocationShellCommand.handleAddTestProvider():void");
    }

    private void handleRemoveTestProvider() {
        java.lang.String provider = getNextArgRequired();
        this.mService.removeTestProvider(provider, this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
    }

    private void handleSetTestProviderEnabled() {
        java.lang.String provider = getNextArgRequired();
        boolean enabled = java.lang.Boolean.parseBoolean(getNextArgRequired());
        this.mService.setTestProviderEnabled(provider, enabled, this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0069  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void handleSetTestProviderLocation() {
        /*
            Method dump skipped, instruction units count: 246
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.location.LocationShellCommand.handleSetTestProviderLocation():void");
    }

    private void handleSendExtraCommand() {
        java.lang.String provider = getNextArgRequired();
        java.lang.String command = getNextArgRequired();
        this.mService.sendExtraCommand(provider, command, null);
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Location service commands:");
        pw.println("  help or -h");
        pw.println("    Print this help text.");
        pw.println("  is-location-enabled [--user <USER_ID>]");
        pw.println("    Gets the master location switch enabled state. If no user is specified,");
        pw.println("    the current user is assumed.");
        pw.println("  set-location-enabled true|false [--user <USER_ID>]");
        pw.println("    Sets the master location switch enabled state. If no user is specified,");
        pw.println("    the current user is assumed.");
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            pw.println("  is-adas-gnss-location-enabled [--user <USER_ID>]");
            pw.println("    Gets the ADAS GNSS location enabled state. If no user is specified,");
            pw.println("    the current user is assumed.");
            pw.println("  set-adas-gnss-location-enabled true|false [--user <USER_ID>]");
            pw.println("    Sets the ADAS GNSS location enabled state. If no user is specified,");
            pw.println("    the current user is assumed.");
            pw.println("  is-automotive-gnss-suspended");
            pw.println("    Gets the automotive GNSS suspended state.");
            pw.println("  set-automotive-gnss-suspended true|false");
            pw.println("    Sets the automotive GNSS suspended state.");
        }
        pw.println("  providers");
        pw.println("    The providers command is followed by a subcommand, as listed below:");
        pw.println();
        pw.println("    add-test-provider <PROVIDER> [--requiresNetwork] [--requiresSatellite]");
        pw.println("      [--requiresCell] [--hasMonetaryCost] [--supportsAltitude]");
        pw.println("      [--supportsSpeed] [--supportsBearing]");
        pw.println("      [--powerRequirement <POWER_REQUIREMENT>]");
        pw.println("      [--extraAttributionTags <TAG>,<TAG>,...]");
        pw.println("      Add the given test provider. Requires MOCK_LOCATION permissions which");
        pw.println("      can be enabled by running \"adb shell appops set <uid>");
        pw.println("      android:mock_location allow\". There are optional flags that can be");
        pw.println("      used to configure the provider properties and additional arguments. If");
        pw.println("      no flags are included, then default values will be used.");
        pw.println("    remove-test-provider <PROVIDER>");
        pw.println("      Remove the given test provider.");
        pw.println("    set-test-provider-enabled <PROVIDER> true|false");
        pw.println("      Sets the given test provider enabled state.");
        pw.println("    set-test-provider-location <PROVIDER> --location <LATITUDE>,<LONGITUDE>");
        pw.println("      [--accuracy <ACCURACY>] [--time <TIME>]");
        pw.println("      Set location for given test provider. Accuracy and time are optional.");
        pw.println("    send-extra-command <PROVIDER> <COMMAND>");
        pw.println("      Sends the given extra command to the given provider.");
        pw.println();
        pw.println("      Common commands that may be supported by the gps provider, depending on");
        pw.println("      hardware and software configurations:");
        pw.println("        delete_aiding_data - requests deletion of any predictive aiding data");
        pw.println("        force_time_injection - requests NTP time injection");
        pw.println("        force_psds_injection - requests predictive aiding data injection");
        pw.println("        request_power_stats - requests GNSS power stats update");
    }
}
