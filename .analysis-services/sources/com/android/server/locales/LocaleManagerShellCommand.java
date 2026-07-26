package com.android.server.locales;

/* JADX INFO: loaded from: classes2.dex */
public class LocaleManagerShellCommand extends android.os.ShellCommand {
    private final android.app.ILocaleManager mBinderService;

    LocaleManagerShellCommand(android.app.ILocaleManager localeManager) {
        this.mBinderService = localeManager;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0039  */
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
                case -843437997: goto L2e;
                case -232514593: goto L24;
                case 819706294: goto L1a;
                case 1730458818: goto Lf;
                default: goto Le;
            }
        Le:
            goto L39
        Lf:
            java.lang.String r0 = "set-app-locales"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 0
            goto L3a
        L1a:
            java.lang.String r0 = "get-app-locales"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 1
            goto L3a
        L24:
            java.lang.String r0 = "get-app-localeconfig"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 3
            goto L3a
        L2e:
            java.lang.String r0 = "set-app-localeconfig"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 2
            goto L3a
        L39:
            r0 = -1
        L3a:
            switch(r0) {
                case 0: goto L51;
                case 1: goto L4c;
                case 2: goto L47;
                case 3: goto L42;
                default: goto L3d;
            }
        L3d:
            int r0 = r1.handleDefaultCommands(r2)
            return r0
        L42:
            int r0 = r1.runGetAppOverrideLocaleConfig()
            return r0
        L47:
            int r0 = r1.runSetAppOverrideLocaleConfig()
            return r0
        L4c:
            int r0 = r1.runGetAppLocales()
            return r0
        L51:
            int r0 = r1.runSetAppLocales()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.locales.LocaleManagerShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Locale manager (locale) shell commands:");
        pw.println("  help");
        pw.println("      Print this help text.");
        pw.println("  set-app-locales <PACKAGE_NAME> [--user <USER_ID>] [--locales <LOCALE_INFO>][--delegate <FROM_DELEGATE>]");
        pw.println("      Set the locales for the specified app.");
        pw.println("      --user <USER_ID>: apply for the given user, the current user is used when unspecified.");
        pw.println("      --locales <LOCALE_INFO>: The language tags of locale to be included as a single String separated by commas.");
        pw.println("                 eg. en,en-US,hi ");
        pw.println("                 Empty locale list is used when unspecified.");
        pw.println("      --delegate <FROM_DELEGATE>: The locales are set from a delegate, the value could be true or false. false is the default when unspecified.");
        pw.println("  get-app-locales <PACKAGE_NAME> [--user <USER_ID>]");
        pw.println("      Get the locales for the specified app.");
        pw.println("      --user <USER_ID>: get for the given user, the current user is used when unspecified.");
        pw.println("  set-app-localeconfig <PACKAGE_NAME> [--user <USER_ID>] [--locales <LOCALE_INFO>]");
        pw.println("      Set the override LocaleConfig for the specified app.");
        pw.println("      --user <USER_ID>: apply for the given user, the current user is used when unspecified.");
        pw.println("      --locales <LOCALE_INFO>: The language tags of locale to be included as a single String separated by commas.");
        pw.println("                 eg. en,en-US,hi ");
        pw.println("                 Empty locale list is used when typing a 'empty' word");
        pw.println("                 NULL is used when unspecified.");
        pw.println("  get-app-localeconfig <PACKAGE_NAME> [--user <USER_ID>]");
        pw.println("      Get the locales within the override LocaleConfig for the specified app.");
        pw.println("      --user <USER_ID>: get for the given user, the current user is used when unspecified.");
    }

    private int runSetAppLocales() {
        java.io.PrintWriter err = getErrPrintWriter();
        java.lang.String packageName = getNextArg();
        if (packageName != null) {
            int userId = android.app.ActivityManager.getCurrentUser();
            android.os.LocaleList locales = android.os.LocaleList.getEmptyLocaleList();
            boolean fromDelegate = false;
            while (true) {
                java.lang.String option = getNextOption();
                byte b = 0;
                if (option != null) {
                    switch (option.hashCode()) {
                        case 835076901:
                            b = !option.equals("--delegate") ? (byte) -1 : (byte) 2;
                            break;
                        case 1333469547:
                            if (!option.equals("--user")) {
                                b = -1;
                            }
                            break;
                        case 1724392377:
                            b = !option.equals("--locales") ? (byte) -1 : (byte) 1;
                            break;
                        default:
                            b = -1;
                            break;
                    }
                    switch (b) {
                        case 0:
                            userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                            break;
                        case 1:
                            locales = parseLocales();
                            break;
                        case 2:
                            fromDelegate = parseFromDelegate();
                            break;
                        default:
                            throw new java.lang.IllegalArgumentException("Unknown option: " + option);
                    }
                } else {
                    try {
                        this.mBinderService.setApplicationLocales(packageName, userId, locales, fromDelegate);
                    } catch (android.os.RemoteException e) {
                        getOutPrintWriter().println("Remote Exception: " + e);
                    } catch (java.lang.IllegalArgumentException e2) {
                        getOutPrintWriter().println("Unknown package " + packageName + " for userId " + userId);
                    }
                    return 0;
                }
            }
        } else {
            err.println("Error: no package specified");
            return -1;
        }
    }

    private int runGetAppLocales() {
        java.io.PrintWriter err = getErrPrintWriter();
        java.lang.String packageName = getNextArg();
        if (packageName != null) {
            int userId = android.app.ActivityManager.getCurrentUser();
            java.lang.String option = getNextOption();
            if (option != null) {
                if ("--user".equals(option)) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    throw new java.lang.IllegalArgumentException("Unknown option: " + option);
                }
            }
            try {
                android.os.LocaleList locales = this.mBinderService.getApplicationLocales(packageName, userId);
                getOutPrintWriter().println("Locales for " + packageName + " for user " + userId + " are [" + locales.toLanguageTags() + "]");
                return 0;
            } catch (android.os.RemoteException e) {
                getOutPrintWriter().println("Remote Exception: " + e);
                return 0;
            } catch (java.lang.IllegalArgumentException e2) {
                getOutPrintWriter().println("Unknown package " + packageName + " for userId " + userId);
                return 0;
            }
        }
        err.println("Error: no package specified");
        return -1;
    }

    private int runSetAppOverrideLocaleConfig() {
        android.app.LocaleConfig localeConfig;
        java.lang.String packageName = getNextArg();
        if (packageName != null) {
            int userId = android.app.ActivityManager.getCurrentUser();
            android.os.LocaleList locales = null;
            while (true) {
                java.lang.String option = getNextOption();
                byte b = 0;
                if (option != null) {
                    switch (option.hashCode()) {
                        case 1333469547:
                            if (!option.equals("--user")) {
                                b = -1;
                            }
                            break;
                        case 1724392377:
                            b = !option.equals("--locales") ? (byte) -1 : (byte) 1;
                            break;
                        default:
                            b = -1;
                            break;
                    }
                    switch (b) {
                        case 0:
                            userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                            break;
                        case 1:
                            locales = parseOverrideLocales();
                            break;
                        default:
                            throw new java.lang.IllegalArgumentException("Unknown option: " + option);
                    }
                } else {
                    if (locales == null) {
                        localeConfig = null;
                    } else {
                        try {
                            localeConfig = new android.app.LocaleConfig(locales);
                        } catch (android.os.RemoteException e) {
                            getOutPrintWriter().println("Remote Exception: " + e);
                        }
                    }
                    this.mBinderService.setOverrideLocaleConfig(packageName, userId, localeConfig);
                    return 0;
                }
            }
        } else {
            java.io.PrintWriter err = getErrPrintWriter();
            err.println("Error: no package specified");
            return -1;
        }
    }

    private int runGetAppOverrideLocaleConfig() {
        java.lang.String packageName = getNextArg();
        if (packageName != null) {
            int userId = android.app.ActivityManager.getCurrentUser();
            java.lang.String option = getNextOption();
            if (option != null) {
                if ("--user".equals(option)) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    throw new java.lang.IllegalArgumentException("Unknown option: " + option);
                }
            }
            try {
                android.app.LocaleConfig localeConfig = this.mBinderService.getOverrideLocaleConfig(packageName, userId);
                if (localeConfig == null) {
                    getOutPrintWriter().println("LocaleConfig for " + packageName + " for user " + userId + " is null");
                    return 0;
                }
                android.os.LocaleList locales = localeConfig.getSupportedLocales();
                if (locales == null) {
                    getOutPrintWriter().println("Locales within the LocaleConfig for " + packageName + " for user " + userId + " are null");
                    return 0;
                }
                getOutPrintWriter().println("Locales within the LocaleConfig for " + packageName + " for user " + userId + " are [" + locales.toLanguageTags() + "]");
                return 0;
            } catch (android.os.RemoteException e) {
                getOutPrintWriter().println("Remote Exception: " + e);
                return 0;
            }
        }
        java.io.PrintWriter err = getErrPrintWriter();
        err.println("Error: no package specified");
        return -1;
    }

    private android.os.LocaleList parseOverrideLocales() {
        java.lang.String locales = getNextArg();
        if (locales == null) {
            return null;
        }
        if (locales.equals("empty")) {
            return android.os.LocaleList.getEmptyLocaleList();
        }
        if (locales.startsWith("-")) {
            throw new java.lang.IllegalArgumentException("Unknown locales: " + locales);
        }
        return android.os.LocaleList.forLanguageTags(locales);
    }

    private android.os.LocaleList parseLocales() {
        java.lang.String locales = getNextArg();
        if (locales == null) {
            return android.os.LocaleList.getEmptyLocaleList();
        }
        if (locales.startsWith("-")) {
            throw new java.lang.IllegalArgumentException("Unknown locales: " + locales);
        }
        return android.os.LocaleList.forLanguageTags(locales);
    }

    private boolean parseFromDelegate() {
        java.lang.String result = getNextArg();
        if (result == null) {
            return false;
        }
        if (result.startsWith("-")) {
            throw new java.lang.IllegalArgumentException("Unknown source: " + result);
        }
        return java.lang.Boolean.parseBoolean(result);
    }
}
