package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
public class GameManagerShellCommand extends android.os.ShellCommand {
    private static final java.lang.String BATTERY_MODE_NUM = "3";
    private static final java.lang.String BATTERY_MODE_STR = "battery";
    private static final java.lang.String CUSTOM_MODE_NUM = "4";
    private static final java.lang.String CUSTOM_MODE_STR = "custom";
    private static final java.lang.String PERFORMANCE_MODE_NUM = "2";
    private static final java.lang.String PERFORMANCE_MODE_STR = "performance";
    private static final java.lang.String STANDARD_MODE_NUM = "1";
    private static final java.lang.String STANDARD_MODE_STR = "standard";
    private static final java.lang.String UNSUPPORTED_MODE_NUM = java.lang.String.valueOf(0);
    private static final java.lang.String UNSUPPORTED_MODE_STR = "unsupported";
    com.android.server.app.IGameManagerShellCommandExt mIGameManagerShellCommandExt = (com.android.server.app.IGameManagerShellCommandExt) system.ext.loader.core.ExtLoader.type(com.android.server.app.IGameManagerShellCommandExt.class).base(this).create();

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r6) {
        /*
            r5 = this;
            if (r6 != 0) goto L7
            int r0 = r5.handleDefaultCommands(r6)
            return r0
        L7:
            java.io.PrintWriter r0 = r5.getOutPrintWriter()
            r1 = -1
            int r2 = r6.hashCode()     // Catch: java.lang.Exception -> L7f
            switch(r2) {
                case -1207633086: goto L4a;
                case -729460415: goto L3f;
                case 100897: goto L35;
                case 113762: goto L2a;
                case 3357091: goto L1f;
                case 108404047: goto L14;
                default: goto L13;
            }     // Catch: java.lang.Exception -> L7f
        L13:
            goto L55
        L14:
            java.lang.String r2 = "reset"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L7f
            if (r2 == 0) goto L13
            r2 = 1
            goto L56
        L1f:
            java.lang.String r2 = "mode"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L7f
            if (r2 == 0) goto L13
            r2 = 2
            goto L56
        L2a:
            java.lang.String r2 = "set"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L7f
            if (r2 == 0) goto L13
            r2 = 0
            goto L56
        L35:
            java.lang.String r2 = "ext"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L7f
            if (r2 == 0) goto L13
            r2 = 5
            goto L56
        L3f:
            java.lang.String r2 = "list-modes"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L7f
            if (r2 == 0) goto L13
            r2 = 3
            goto L56
        L4a:
            java.lang.String r2 = "list-configs"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L7f
            if (r2 == 0) goto L13
            r2 = 4
            goto L56
        L55:
            r2 = r1
        L56:
            switch(r2) {
                case 0: goto L79;
                case 1: goto L74;
                case 2: goto L6f;
                case 3: goto L6a;
                case 4: goto L65;
                case 5: goto L5e;
                default: goto L59;
            }     // Catch: java.lang.Exception -> L7f
        L59:
            int r1 = r5.handleDefaultCommands(r6)     // Catch: java.lang.Exception -> L7f
            goto L7e
        L5e:
            com.android.server.app.IGameManagerShellCommandExt r2 = r5.mIGameManagerShellCommandExt     // Catch: java.lang.Exception -> L7f
            int r1 = r2.onCommandExt(r0)     // Catch: java.lang.Exception -> L7f
            return r1
        L65:
            int r1 = r5.runListGameModeConfigs(r0)     // Catch: java.lang.Exception -> L7f
            return r1
        L6a:
            int r1 = r5.runListGameModes(r0)     // Catch: java.lang.Exception -> L7f
            return r1
        L6f:
            int r1 = r5.runSetGameMode(r0)     // Catch: java.lang.Exception -> L7f
            return r1
        L74:
            int r1 = r5.runResetGameModeConfig(r0)     // Catch: java.lang.Exception -> L7f
            return r1
        L79:
            int r1 = r5.runSetGameModeConfig(r0)     // Catch: java.lang.Exception -> L7f
            return r1
        L7e:
            return r1
        L7f:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error: "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r2)
            java.lang.String r3 = r3.toString()
            r0.println(r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.app.GameManagerShellCommand.onCommand(java.lang.String):int");
    }

    private int runListGameModes(java.io.PrintWriter pw) throws android.os.ServiceManager.ServiceNotFoundException, android.os.RemoteException {
        java.lang.String packageName = getNextArgRequired();
        int userId = android.app.ActivityManager.getCurrentUser();
        com.android.server.app.GameManagerService gameManagerService = (com.android.server.app.GameManagerService) android.os.ServiceManager.getService("game");
        java.lang.String currentMode = gameModeIntToString(gameManagerService.getGameMode(packageName, userId));
        java.util.StringJoiner sj = new java.util.StringJoiner(",");
        for (int mode : gameManagerService.getAvailableGameModes(packageName, userId)) {
            sj.add(gameModeIntToString(mode));
        }
        pw.println(packageName + " current mode: " + currentMode + ", available game modes: [" + sj + "]");
        return 0;
    }

    private int runListGameModeConfigs(java.io.PrintWriter pw) throws android.os.ServiceManager.ServiceNotFoundException, android.os.RemoteException {
        java.lang.String packageName = getNextArgRequired();
        com.android.server.app.GameManagerService gameManagerService = (com.android.server.app.GameManagerService) android.os.ServiceManager.getService("game");
        java.lang.String listStr = gameManagerService.getInterventionList(packageName, android.app.ActivityManager.getCurrentUser());
        if (listStr == null) {
            pw.println("No interventions found for " + packageName);
            return 0;
        }
        pw.println(packageName + " interventions: " + listStr);
        return 0;
    }

    private int runSetGameMode(java.io.PrintWriter pw) throws android.os.ServiceManager.ServiceNotFoundException, android.os.RemoteException {
        byte b;
        java.lang.String option = getNextOption();
        java.lang.String userIdStr = null;
        if (option != null && option.equals("--user")) {
            userIdStr = getNextArgRequired();
        }
        java.lang.String gameMode = getNextArgRequired();
        java.lang.String packageName = getNextArgRequired();
        android.app.IGameManagerService service = android.app.IGameManagerService.Stub.asInterface(android.os.ServiceManager.getServiceOrThrow("game"));
        boolean batteryModeSupported = false;
        boolean perfModeSupported = false;
        int userId = userIdStr != null ? java.lang.Integer.parseInt(userIdStr) : android.app.ActivityManager.getCurrentUser();
        int[] modes = service.getAvailableGameModes(packageName, userId);
        for (int mode : modes) {
            if (mode == 2) {
                perfModeSupported = true;
            } else if (mode == 3) {
                batteryModeSupported = true;
            }
        }
        java.lang.String lowerCase = gameMode.toLowerCase();
        switch (lowerCase.hashCode()) {
            case -1480388560:
                b = lowerCase.equals(PERFORMANCE_MODE_STR) ? (byte) 3 : (byte) -1;
                break;
            case -1349088399:
                b = lowerCase.equals(CUSTOM_MODE_STR) ? (byte) 7 : (byte) -1;
                break;
            case -331239923:
                b = lowerCase.equals(BATTERY_MODE_STR) ? (byte) 5 : (byte) -1;
                break;
            case 49:
                b = lowerCase.equals(STANDARD_MODE_NUM) ? (byte) 0 : (byte) -1;
                break;
            case 50:
                b = lowerCase.equals(PERFORMANCE_MODE_NUM) ? (byte) 2 : (byte) -1;
                break;
            case 51:
                b = lowerCase.equals(BATTERY_MODE_NUM) ? (byte) 4 : (byte) -1;
                break;
            case 52:
                b = lowerCase.equals(CUSTOM_MODE_NUM) ? (byte) 6 : (byte) -1;
                break;
            case 1312628413:
                b = lowerCase.equals(STANDARD_MODE_STR) ? (byte) 1 : (byte) -1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
            case 1:
                service.setGameMode(packageName, 1, userId);
                pw.println("Set game mode to `STANDARD` for user `" + userId + "` in game `" + packageName + "`");
                return 0;
            case 2:
            case 3:
                if (!perfModeSupported) {
                    pw.println("Game mode: " + gameMode + " not supported by " + packageName);
                    return -1;
                }
                service.setGameMode(packageName, 2, userId);
                pw.println("Set game mode to `PERFORMANCE` for user `" + userId + "` in game `" + packageName + "`");
                return 0;
            case 4:
            case 5:
                if (!batteryModeSupported) {
                    pw.println("Game mode: " + gameMode + " not supported by " + packageName);
                    return -1;
                }
                service.setGameMode(packageName, 3, userId);
                pw.println("Set game mode to `BATTERY` for user `" + userId + "` in game `" + packageName + "`");
                return 0;
            case 6:
            case 7:
                service.setGameMode(packageName, 4, userId);
                pw.println("Set game mode to `CUSTOM` for user `" + userId + "` in game `" + packageName + "`");
                return 0;
            default:
                pw.println("Invalid game mode: " + gameMode);
                return -1;
        }
    }

    private int runSetGameModeConfig(java.io.PrintWriter pw) throws android.os.ServiceManager.ServiceNotFoundException, android.os.RemoteException {
        java.lang.String fpsStr = null;
        java.lang.String downscaleRatio = null;
        int gameMode = 4;
        java.lang.String fpsStr2 = null;
        while (true) {
            java.lang.String option = getNextOption();
            byte b = 0;
            if (option != null) {
                switch (option.hashCode()) {
                    case 43000649:
                        b = !option.equals("--fps") ? (byte) -1 : (byte) 3;
                        break;
                    case 1333227331:
                        if (!option.equals("--mode")) {
                            b = -1;
                        }
                        break;
                    case 1333469547:
                        b = !option.equals("--user") ? (byte) -1 : (byte) 1;
                        break;
                    case 1807206472:
                        b = !option.equals("--downscale") ? (byte) -1 : (byte) 2;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        int gameMode2 = java.lang.Integer.parseInt(getNextArgRequired());
                        gameMode = gameMode2;
                        break;
                    case 1:
                        if (fpsStr2 != null) {
                            pw.println("Duplicate option '" + option + "'");
                            return -1;
                        }
                        fpsStr2 = getNextArgRequired();
                        break;
                        break;
                    case 2:
                        if (downscaleRatio != null) {
                            pw.println("Duplicate option '" + option + "'");
                            return -1;
                        }
                        java.lang.String downscaleRatio2 = getNextArgRequired();
                        if ("disable".equals(downscaleRatio2)) {
                            downscaleRatio = "-1";
                        } else {
                            try {
                                java.lang.Float.parseFloat(downscaleRatio2);
                                downscaleRatio = downscaleRatio2;
                            } catch (java.lang.NumberFormatException e) {
                                pw.println("Invalid scaling ratio '" + downscaleRatio2 + "'");
                                return -1;
                            }
                        }
                        break;
                        break;
                    case 3:
                        if (fpsStr != null) {
                            pw.println("Duplicate option '" + option + "'");
                            return -1;
                        }
                        java.lang.String fpsStr3 = getNextArgRequired();
                        try {
                            java.lang.Integer.parseInt(fpsStr3);
                            fpsStr = fpsStr3;
                        } catch (java.lang.NumberFormatException e2) {
                            pw.println("Invalid frame rate: '" + fpsStr3 + "'");
                            return -1;
                        }
                        break;
                        break;
                    default:
                        pw.println("Invalid option '" + option + "'");
                        return -1;
                }
            } else {
                java.lang.String packageName = getNextArgRequired();
                int userId = fpsStr2 != null ? java.lang.Integer.parseInt(fpsStr2) : android.app.ActivityManager.getCurrentUser();
                com.android.server.app.GameManagerService gameManagerService = (com.android.server.app.GameManagerService) android.os.ServiceManager.getService("game");
                if (gameManagerService == null) {
                    pw.println("Failed to find GameManagerService on device");
                    return -1;
                }
                gameManagerService.setGameModeConfigOverride(packageName, userId, gameMode, fpsStr, downscaleRatio);
                pw.println("Set custom mode intervention config for user `" + userId + "` in game `" + packageName + "` as: `downscaling-ratio: " + downscaleRatio + ";fps-override: " + fpsStr + "`");
                return 0;
            }
        }
    }

    private int runResetGameModeConfig(java.io.PrintWriter pw) throws android.os.ServiceManager.ServiceNotFoundException, android.os.RemoteException {
        java.lang.String gameMode = null;
        java.lang.String userIdStr = null;
        while (true) {
            java.lang.String option = getNextOption();
            byte b = 1;
            if (option != null) {
                switch (option.hashCode()) {
                    case 1333227331:
                        if (!option.equals("--mode")) {
                            b = -1;
                        }
                        break;
                    case 1333469547:
                        b = !option.equals("--user") ? (byte) -1 : (byte) 0;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        if (userIdStr == null) {
                            userIdStr = getNextArgRequired();
                        } else {
                            pw.println("Duplicate option '" + option + "'");
                            return -1;
                        }
                        break;
                    case 1:
                        if (gameMode == null) {
                            gameMode = getNextArgRequired();
                        } else {
                            pw.println("Duplicate option '" + option + "'");
                            return -1;
                        }
                        break;
                    default:
                        pw.println("Invalid option '" + option + "'");
                        return -1;
                }
            } else {
                java.lang.String packageName = getNextArgRequired();
                com.android.server.app.GameManagerService gameManagerService = (com.android.server.app.GameManagerService) android.os.ServiceManager.getService("game");
                int userId = userIdStr != null ? java.lang.Integer.parseInt(userIdStr) : android.app.ActivityManager.getCurrentUser();
                if (gameMode == null) {
                    gameManagerService.resetGameModeConfigOverride(packageName, userId, -1);
                    return 0;
                }
                java.lang.String lowerCase = gameMode.toLowerCase(java.util.Locale.getDefault());
                switch (lowerCase.hashCode()) {
                    case -1480388560:
                        if (!lowerCase.equals(PERFORMANCE_MODE_STR)) {
                            b = -1;
                        }
                        break;
                    case -331239923:
                        b = !lowerCase.equals(BATTERY_MODE_STR) ? (byte) -1 : (byte) 3;
                        break;
                    case 50:
                        b = !lowerCase.equals(PERFORMANCE_MODE_NUM) ? (byte) -1 : (byte) 0;
                        break;
                    case 51:
                        b = !lowerCase.equals(BATTERY_MODE_NUM) ? (byte) -1 : (byte) 2;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        gameManagerService.resetGameModeConfigOverride(packageName, userId, 2);
                        return 0;
                    case 2:
                    case 3:
                        gameManagerService.resetGameModeConfigOverride(packageName, userId, 3);
                        return 0;
                    default:
                        pw.println("Invalid game mode: " + gameMode);
                        return -1;
                }
            }
        }
    }

    private static java.lang.String gameModeIntToString(int gameMode) {
        switch (gameMode) {
            case 0:
                return UNSUPPORTED_MODE_STR;
            case 1:
                return STANDARD_MODE_STR;
            case 2:
                return PERFORMANCE_MODE_STR;
            case 3:
                return BATTERY_MODE_STR;
            case 4:
                return CUSTOM_MODE_STR;
            default:
                return "";
        }
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Game manager (game) commands:");
        pw.println("  help");
        pw.println("      Print this help text.");
        pw.println("  downscale");
        pw.println("      Deprecated. Please use `custom` command.");
        pw.println("  list-configs <PACKAGE_NAME>");
        pw.println("      Lists the current intervention configs of an app.");
        pw.println("  list-modes <PACKAGE_NAME>");
        pw.println("      Lists the current selected and available game modes of an app.");
        pw.println("  mode [--user <USER_ID>] [1|2|3|4|standard|performance|battery|custom] <PACKAGE_NAME>");
        pw.println("      Set app to run in the specified game mode, if supported.");
        pw.println("      --user <USER_ID>: apply for the given user,");
        pw.println("                        the current user is used when unspecified.");
        pw.println("  set [intervention configs] <PACKAGE_NAME>");
        pw.println("      Set app to run at custom mode using provided intervention configs");
        pw.println("      Intervention configs consists of:");
        pw.println("      --downscale [0.3|0.35|0.4|0.45|0.5|0.55|0.6|0.65");
        pw.println("                  |0.7|0.75|0.8|0.85|0.9|disable]: Set app to run at the");
        pw.println("                                                   specified scaling ratio.");
        pw.println("      --fps: Integer value to set app to run at the specified fps,");
        pw.println("             if supported. 0 to disable.");
        pw.println("  reset [--mode [2|3|performance|battery] --user <USER_ID>] <PACKAGE_NAME>");
        pw.println("      Resets the game mode of the app to device configuration.");
        pw.println("      This should only be used to reset any override to non custom game mode");
        pw.println("      applied using the deprecated `set` command");
        pw.println("      --mode [2|3|performance|battery]: apply for the given mode,");
        pw.println("                                        resets all modes when unspecified.");
        pw.println("      --user <USER_ID>: apply for the given user,");
        pw.println("                        the current user is used when unspecified.");
        this.mIGameManagerShellCommandExt.onHelp(pw);
    }
}
