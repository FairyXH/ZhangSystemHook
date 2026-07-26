package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class DisplayManagerShellCommand extends android.os.ShellCommand {
    private static final java.lang.String NOTIFICATION_TYPES = "on-hotplug-error, on-link-training-failure, on-cable-dp-incapable";
    private static final java.lang.String TAG = "DisplayManagerShellCommand";
    com.android.server.display.IOplusDisplayManagerShellCommandExt mDisplayManagerShellCommandExt = (com.android.server.display.IOplusDisplayManagerShellCommandExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IOplusDisplayManagerShellCommandExt.class).base(this).create();
    private final com.android.server.display.feature.DisplayManagerFlags mFlags;
    private final com.android.server.display.DisplayManagerService mService;

    DisplayManagerShellCommand(com.android.server.display.DisplayManagerService service, com.android.server.display.feature.DisplayManagerFlags flags) {
        this.mService = service;
        this.mFlags = flags;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0146  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r5) {
        /*
            Method dump skipped, instruction units count: 644
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.DisplayManagerShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Display manager commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println();
        pw.println("  show-notification NOTIFICATION_TYPE");
        pw.println("    Show notification for one of the following types: on-hotplug-error, on-link-training-failure, on-cable-dp-incapable");
        pw.println("  cancel-notifications");
        pw.println("    Cancel notifications.");
        pw.println("  set-brightness BRIGHTNESS");
        pw.println("    Sets the current brightness to BRIGHTNESS (a number between 0 and 1).");
        pw.println("  reset-brightness-configuration");
        pw.println("    Reset the brightness to its default configuration.");
        pw.println("  ab-logging-enable");
        pw.println("    Enable auto-brightness logging.");
        pw.println("  ab-logging-disable");
        pw.println("    Disable auto-brightness logging.");
        pw.println("  dwb-logging-enable");
        pw.println("    Enable display white-balance logging.");
        pw.println("  dwb-logging-disable");
        pw.println("    Disable display white-balance logging.");
        pw.println("  dmd-logging-enable");
        pw.println("    Enable display mode director logging.");
        pw.println("  dmd-logging-disable");
        pw.println("    Disable display mode director logging.");
        pw.println("  dwb-set-cct CCT");
        pw.println("    Sets the ambient color temperature override to CCT (use -1 to disable).");
        pw.println("  set-user-preferred-display-mode WIDTH HEIGHT REFRESH-RATE DISPLAY_ID (optional)");
        pw.println("    Sets the user preferred display mode which has fields WIDTH, HEIGHT and REFRESH-RATE. If DISPLAY_ID is passed, the mode change is applied to displaywith id = DISPLAY_ID, else mode change is applied globally.");
        pw.println("  clear-user-preferred-display-mode DISPLAY_ID (optional)");
        pw.println("    Clears the user preferred display mode. If DISPLAY_ID is passed, the mode is cleared for  display with id = DISPLAY_ID, else mode is cleared globally.");
        pw.println("  get-user-preferred-display-mode DISPLAY_ID (optional)");
        pw.println("    Returns the user preferred display mode or null if no mode is set by user.If DISPLAY_ID is passed, the mode for display with id = DISPLAY_ID is returned, else global display mode is returned.");
        pw.println("  get-active-display-mode-at-start DISPLAY_ID");
        pw.println("    Returns the display mode which was found at boot time of display with id = DISPLAY_ID");
        pw.println("  set-match-content-frame-rate-pref PREFERENCE");
        pw.println("    Sets the match content frame rate preference as PREFERENCE ");
        pw.println("  get-match-content-frame-rate-pref");
        pw.println("    Returns the match content frame rate preference");
        pw.println("  set-user-disabled-hdr-types TYPES...");
        pw.println("    Sets the user disabled HDR types as TYPES");
        pw.println("  get-user-disabled-hdr-types");
        pw.println("    Returns the user disabled HDR types");
        pw.println("  get-displays [-c|--category CATEGORY] [-i|--ids-only] [-t|--type TYPE]");
        pw.println("    [CATEGORY]");
        pw.println("    Returns the current displays. Can specify string category among");
        pw.println("    DisplayManager.DISPLAY_CATEGORY_*; must use the actual string value.");
        pw.println("    Can choose to print only the ids of the displays. Can filter by");
        pw.println("    display types. For example, '--type external'");
        pw.println("  dock");
        pw.println("    Sets brightness to docked + idle screen brightness mode");
        pw.println("  undock");
        pw.println("    Sets brightness to active (normal) screen brightness mode");
        if (this.mFlags.isConnectedDisplayManagementEnabled()) {
            pw.println("  enable-display DISPLAY_ID");
            pw.println("    Enable the DISPLAY_ID. Only possible if this is a connected display.");
            pw.println("  disable-display DISPLAY_ID");
            pw.println("    Disable the DISPLAY_ID. Only possible if this is a connected display.");
        }
        pw.println();
        android.content.Intent.printIntentArgsHelp(pw, "");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int getDisplays() {
        java.lang.String nextArgRequired = null;
        java.io.PrintWriter outPrintWriter = getOutPrintWriter();
        final java.util.ArrayList arrayList = new java.util.ArrayList();
        boolean z = false;
        boolean z2 = false;
        while (true) {
            java.lang.String nextOption = getNextOption();
            byte b = 0;
            if (nextOption != null) {
                switch (nextOption.hashCode()) {
                    case 0:
                        b = nextOption.equals("") ? (byte) 6 : (byte) -1;
                        break;
                    case 1494:
                        b = nextOption.equals("-c") ? (byte) 4 : (byte) -1;
                        break;
                    case android.net.util.NetworkConstants.ETHER_MTU /* 1500 */:
                        if (!nextOption.equals("-i")) {
                            b = -1;
                        }
                        break;
                    case 1511:
                        b = nextOption.equals("-t") ? (byte) 2 : (byte) -1;
                        break;
                    case 66265758:
                        b = nextOption.equals("--category") ? (byte) 5 : (byte) -1;
                        break;
                    case 220627777:
                        b = nextOption.equals("--ids-only") ? (byte) 1 : (byte) -1;
                        break;
                    case 1333445850:
                        b = nextOption.equals("--type") ? (byte) 3 : (byte) -1;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        z = true;
                        break;
                    case 2:
                    case 3:
                        int type = getType(getNextArgRequired(), outPrintWriter);
                        if (type == -1) {
                            return 1;
                        }
                        arrayList.add(java.lang.Integer.valueOf(type));
                        z2 = true;
                        break;
                        break;
                    case 4:
                    case 5:
                        if (nextArgRequired != null) {
                            outPrintWriter.println("Error: the category has been specified more than one time. Please select only one category.");
                            return 1;
                        }
                        nextArgRequired = getNextArgRequired();
                        break;
                        break;
                    case 6:
                        break;
                    default:
                        outPrintWriter.println("Error: unknown option '" + nextOption + "'");
                        return 1;
                }
            } else {
                java.lang.String nextArg = getNextArg();
                if (nextArg != null) {
                    if (nextArgRequired != null) {
                        outPrintWriter.println("Error: the category has been specified both with the -c option and the positional argument. Please select only one category.");
                        return 1;
                    }
                    nextArgRequired = nextArg;
                }
                android.view.Display[] displays = ((android.hardware.display.DisplayManager) this.mService.getContext().getSystemService(android.hardware.display.DisplayManager.class)).getDisplays(nextArgRequired);
                java.lang.Object[] objArr = displays;
                if (z2) {
                    objArr = (android.view.Display[]) java.util.Arrays.stream(displays).filter(new java.util.function.Predicate() { // from class: com.android.server.display.DisplayManagerShellCommand$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return arrayList.contains(java.lang.Integer.valueOf(((android.view.Display) obj).getType()));
                        }
                    }).toArray(new java.util.function.IntFunction() { // from class: com.android.server.display.DisplayManagerShellCommand$$ExternalSyntheticLambda1
                        @Override // java.util.function.IntFunction
                        public final java.lang.Object apply(int i) {
                            return com.android.server.display.DisplayManagerShellCommand.lambda$getDisplays$1(i);
                        }
                    });
                }
                if (!z) {
                    outPrintWriter.println("Displays:");
                }
                for (int i = 0; i < objArr.length; i++) {
                    outPrintWriter.println(z ? java.lang.Integer.valueOf(objArr[i].getDisplayId()) : objArr[i]);
                }
                return 0;
            }
        }
    }

    static /* synthetic */ android.view.Display[] lambda$getDisplays$1(int x$0) {
        return new android.view.Display[x$0];
    }

    private int getType(java.lang.String type, java.io.PrintWriter out) {
        byte b;
        java.lang.String type2 = type.toUpperCase(java.util.Locale.ENGLISH);
        switch (type2.hashCode()) {
            case -1038134325:
                b = !type2.equals("EXTERNAL") ? (byte) -1 : (byte) 2;
                break;
            case -373305296:
                b = !type2.equals("OVERLAY") ? (byte) -1 : (byte) 4;
                break;
            case 2664213:
                b = !type2.equals("WIFI") ? (byte) -1 : (byte) 3;
                break;
            case 433141802:
                b = !type2.equals("UNKNOWN") ? (byte) -1 : (byte) 0;
                break;
            case 1184148203:
                b = !type2.equals("VIRTUAL") ? (byte) -1 : (byte) 5;
                break;
            case 1353037501:
                b = !type2.equals("INTERNAL") ? (byte) -1 : (byte) 1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
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
            default:
                out.println("Error: argument for display type should be one of 'UNKNOWN', 'INTERNAL', 'EXTERNAL', 'WIFI', 'OVERLAY', 'VIRTUAL', but got '" + type2 + "' instead.");
                return -1;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:18:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int showNotification() {
        /*
            r5 = this;
            java.lang.String r0 = r5.getNextArg()
            r1 = 1
            if (r0 != 0) goto L11
            java.io.PrintWriter r2 = r5.getErrPrintWriter()
            java.lang.String r3 = "Error: no notificationType specified, use one of: on-hotplug-error, on-link-training-failure, on-cable-dp-incapable"
            r2.println(r3)
            return r1
        L11:
            int r2 = r0.hashCode()
            r3 = 0
            switch(r2) {
                case -1348657756: goto L30;
                case 1400911272: goto L25;
                case 1997686684: goto L1a;
                default: goto L19;
            }
        L19:
            goto L3b
        L1a:
            java.lang.String r2 = "on-link-training-failure"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L19
            r2 = r1
            goto L3c
        L25:
            java.lang.String r2 = "on-hotplug-error"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L19
            r2 = r3
            goto L3c
        L30:
            java.lang.String r2 = "on-cable-dp-incapable"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L19
            r2 = 2
            goto L3c
        L3b:
            r2 = -1
        L3c:
            switch(r2) {
                case 0: goto L7b;
                case 1: goto L71;
                case 2: goto L67;
                default: goto L3f;
            }
        L3f:
            java.io.PrintWriter r2 = r5.getErrPrintWriter()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error: unexpected notification type="
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r4 = ", use one of: "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r4 = "on-hotplug-error, on-link-training-failure, on-cable-dp-incapable"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.println(r3)
            return r1
        L67:
            com.android.server.display.DisplayManagerService r1 = r5.mService
            com.android.server.display.notifications.DisplayNotificationManager r1 = r1.getDisplayNotificationManager()
            r1.onCableNotCapableDisplayPort()
            goto L85
        L71:
            com.android.server.display.DisplayManagerService r1 = r5.mService
            com.android.server.display.notifications.DisplayNotificationManager r1 = r1.getDisplayNotificationManager()
            r1.onDisplayPortLinkTrainingFailure()
            goto L85
        L7b:
            com.android.server.display.DisplayManagerService r1 = r5.mService
            com.android.server.display.notifications.DisplayNotificationManager r1 = r1.getDisplayNotificationManager()
            r1.onHotplugConnectionError()
        L85:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.DisplayManagerShellCommand.showNotification():int");
    }

    private int cancelNotifications() {
        this.mService.getDisplayNotificationManager().cancelNotifications();
        return 0;
    }

    private int setBrightness() {
        java.lang.String brightnessText = getNextArg();
        if (brightnessText == null) {
            getErrPrintWriter().println("Error: no brightness specified");
            return 1;
        }
        float brightness = -1.0f;
        try {
            brightness = java.lang.Float.parseFloat(brightnessText);
        } catch (java.lang.NumberFormatException e) {
        }
        if (brightness < 0.0f || brightness > 1.0f) {
            getErrPrintWriter().println("Error: brightness should be a number between 0 and 1");
            return 1;
        }
        android.content.Context context = this.mService.getContext();
        android.hardware.display.DisplayManager dm = (android.hardware.display.DisplayManager) context.getSystemService(android.hardware.display.DisplayManager.class);
        dm.setBrightness(0, brightness);
        return 0;
    }

    private int resetBrightnessConfiguration() {
        this.mService.resetBrightnessConfigurations();
        return 0;
    }

    private int setAutoBrightnessLoggingEnabled(boolean enabled) {
        this.mService.setAutoBrightnessLoggingEnabled(enabled);
        return 0;
    }

    private int setDisplayWhiteBalanceLoggingEnabled(boolean enabled) {
        this.mService.setDisplayWhiteBalanceLoggingEnabled(enabled);
        return 0;
    }

    private int setDisplayModeDirectorLoggingEnabled(boolean enabled) {
        this.mService.setDisplayModeDirectorLoggingEnabled(enabled);
        return 0;
    }

    private int setAmbientColorTemperatureOverride() {
        java.lang.String cctText = getNextArg();
        if (cctText == null) {
            getErrPrintWriter().println("Error: no cct specified");
            return 1;
        }
        try {
            float cct = java.lang.Float.parseFloat(cctText);
            this.mService.setAmbientColorTemperatureOverride(cct);
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: cct should be a number");
            return 1;
        }
    }

    private int setUserPreferredDisplayMode() {
        java.lang.String widthText = getNextArg();
        if (widthText == null) {
            getErrPrintWriter().println("Error: no width specified");
            return 1;
        }
        java.lang.String heightText = getNextArg();
        if (heightText == null) {
            getErrPrintWriter().println("Error: no height specified");
            return 1;
        }
        java.lang.String refreshRateText = getNextArg();
        if (refreshRateText == null) {
            getErrPrintWriter().println("Error: no refresh-rate specified");
            return 1;
        }
        try {
            int width = java.lang.Integer.parseInt(widthText);
            int height = java.lang.Integer.parseInt(heightText);
            float refreshRate = java.lang.Float.parseFloat(refreshRateText);
            if ((width < 0 || height < 0) && refreshRate <= 0.0f) {
                getErrPrintWriter().println("Error: invalid value of resolution (width, height) and refresh rate");
                return 1;
            }
            java.lang.String displayIdText = getNextArg();
            int displayId = -1;
            if (displayIdText != null) {
                try {
                    displayId = java.lang.Integer.parseInt(displayIdText);
                } catch (java.lang.NumberFormatException e) {
                    getErrPrintWriter().println("Error: invalid format of display ID");
                    return 1;
                }
            }
            this.mService.setUserPreferredDisplayModeInternal(displayId, new android.view.Display.Mode(width, height, refreshRate));
            return 0;
        } catch (java.lang.NumberFormatException e2) {
            getErrPrintWriter().println("Error: invalid format of width, height or refresh rate");
            return 1;
        }
    }

    private int clearUserPreferredDisplayMode() {
        java.lang.String displayIdText = getNextArg();
        int displayId = -1;
        if (displayIdText != null) {
            try {
                displayId = java.lang.Integer.parseInt(displayIdText);
            } catch (java.lang.NumberFormatException e) {
                getErrPrintWriter().println("Error: invalid format of display ID");
                return 1;
            }
        }
        this.mService.setUserPreferredDisplayModeInternal(displayId, null);
        return 0;
    }

    private int getUserPreferredDisplayMode() {
        java.lang.String displayIdText = getNextArg();
        int displayId = -1;
        if (displayIdText != null) {
            try {
                displayId = java.lang.Integer.parseInt(displayIdText);
            } catch (java.lang.NumberFormatException e) {
                getErrPrintWriter().println("Error: invalid format of display ID");
                return 1;
            }
        }
        android.view.Display.Mode mode = this.mService.getUserPreferredDisplayModeInternal(displayId);
        if (mode == null) {
            getOutPrintWriter().println("User preferred display mode: null");
            return 0;
        }
        getOutPrintWriter().println("User preferred display mode: " + mode.getPhysicalWidth() + " " + mode.getPhysicalHeight() + " " + mode.getRefreshRate());
        return 0;
    }

    private int getActiveDisplayModeAtStart() {
        java.lang.String displayIdText = getNextArg();
        if (displayIdText == null) {
            getErrPrintWriter().println("Error: no displayId specified");
            return 1;
        }
        try {
            int displayId = java.lang.Integer.parseInt(displayIdText);
            android.view.Display.Mode mode = this.mService.getActiveDisplayModeAtStart(displayId);
            if (mode == null) {
                getOutPrintWriter().println("Boot display mode: null");
                return 0;
            }
            getOutPrintWriter().println("Boot display mode: " + mode.getPhysicalWidth() + " " + mode.getPhysicalHeight() + " " + mode.getRefreshRate());
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: invalid displayId");
            return 1;
        }
    }

    private int setMatchContentFrameRateUserPreference() {
        java.lang.String matchContentFrameRatePrefText = getNextArg();
        if (matchContentFrameRatePrefText == null) {
            getErrPrintWriter().println("Error: no matchContentFrameRatePref specified");
            return 1;
        }
        try {
            int matchContentFrameRatePreference = java.lang.Integer.parseInt(matchContentFrameRatePrefText);
            if (matchContentFrameRatePreference < 0) {
                getErrPrintWriter().println("Error: invalid value of matchContentFrameRatePreference");
                return 1;
            }
            android.content.Context context = this.mService.getContext();
            android.hardware.display.DisplayManager dm = (android.hardware.display.DisplayManager) context.getSystemService(android.hardware.display.DisplayManager.class);
            int refreshRateSwitchingType = toRefreshRateSwitchingType(matchContentFrameRatePreference);
            dm.setRefreshRateSwitchingType(refreshRateSwitchingType);
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: invalid format of matchContentFrameRatePreference");
            return 1;
        }
    }

    private int getMatchContentFrameRateUserPreference() {
        android.content.Context context = this.mService.getContext();
        android.hardware.display.DisplayManager dm = (android.hardware.display.DisplayManager) context.getSystemService(android.hardware.display.DisplayManager.class);
        getOutPrintWriter().println("Match content frame rate type: " + dm.getMatchContentFrameRateUserPreference());
        return 0;
    }

    private int setUserDisabledHdrTypes() {
        java.lang.String[] userDisabledHdrTypesText = peekRemainingArgs();
        if (userDisabledHdrTypesText == null) {
            getErrPrintWriter().println("Error: no userDisabledHdrTypes specified");
            return 1;
        }
        int[] userDisabledHdrTypes = new int[userDisabledHdrTypesText.length];
        int index = 0;
        try {
            int length = userDisabledHdrTypesText.length;
            int i = 0;
            while (i < length) {
                java.lang.String userDisabledHdrType = userDisabledHdrTypesText[i];
                int index2 = index + 1;
                userDisabledHdrTypes[index] = java.lang.Integer.parseInt(userDisabledHdrType);
                i++;
                index = index2;
            }
            android.content.Context context = this.mService.getContext();
            android.hardware.display.DisplayManager dm = (android.hardware.display.DisplayManager) context.getSystemService(android.hardware.display.DisplayManager.class);
            dm.setUserDisabledHdrTypes(userDisabledHdrTypes);
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: invalid format of userDisabledHdrTypes");
            return 1;
        }
    }

    private int getUserDisabledHdrTypes() {
        android.content.Context context = this.mService.getContext();
        android.hardware.display.DisplayManager dm = (android.hardware.display.DisplayManager) context.getSystemService(android.hardware.display.DisplayManager.class);
        int[] userDisabledHdrTypes = dm.getUserDisabledHdrTypes();
        getOutPrintWriter().println("User disabled HDR types: " + java.util.Arrays.toString(userDisabledHdrTypes));
        return 0;
    }

    private int toRefreshRateSwitchingType(int matchContentFrameRateType) {
        switch (matchContentFrameRateType) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                android.util.Slog.e(TAG, matchContentFrameRateType + " is not a valid value of matchContentFrameRate type.");
                return -1;
        }
    }

    private int setDockedAndIdle() {
        this.mService.setDockedAndIdleEnabled(true, 0);
        return 0;
    }

    private int unsetDockedAndIdle() {
        this.mService.setDockedAndIdleEnabled(false, 0);
        return 0;
    }

    private int setDisplayEnabled(boolean enable) {
        if (!this.mFlags.isConnectedDisplayManagementEnabled()) {
            getErrPrintWriter().println("Error: external display management is not available on this device.");
            return 1;
        }
        java.lang.String displayIdText = getNextArg();
        if (displayIdText == null) {
            getErrPrintWriter().println("Error: no displayId specified");
            return 1;
        }
        try {
            int displayId = java.lang.Integer.parseInt(displayIdText);
            this.mService.enableConnectedDisplay(displayId, enable);
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: invalid displayId: '" + displayIdText + "'");
            return 1;
        }
    }

    private int requestDisplayPower(boolean enable) {
        java.lang.String displayIdText = getNextArg();
        if (displayIdText == null) {
            getErrPrintWriter().println("Error: no displayId specified");
            return 1;
        }
        try {
            int displayId = java.lang.Integer.parseInt(displayIdText);
            this.mService.requestDisplayPower(displayId, enable);
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: invalid displayId: '" + displayIdText + "'");
            return 1;
        }
    }
}
