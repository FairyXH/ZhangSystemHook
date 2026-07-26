package com.android.server.grammaticalinflection;

/* JADX INFO: loaded from: classes2.dex */
class GrammaticalInflectionShellCommand extends android.os.ShellCommand {
    private static final android.util.SparseArray<java.lang.String> GRAMMATICAL_GENDER_MAP = new android.util.SparseArray<>();
    private android.content.AttributionSource mAttributionSource;
    private final android.app.IGrammaticalInflectionManager mBinderService;

    static {
        GRAMMATICAL_GENDER_MAP.put(0, "Not specified (0)");
        GRAMMATICAL_GENDER_MAP.put(1, "Neuter (1)");
        GRAMMATICAL_GENDER_MAP.put(2, "Feminine (2)");
        GRAMMATICAL_GENDER_MAP.put(3, "Masculine (3)");
    }

    GrammaticalInflectionShellCommand(android.app.IGrammaticalInflectionManager grammaticalInflectionManager, android.content.AttributionSource attributionSource) {
        this.mBinderService = grammaticalInflectionManager;
        this.mAttributionSource = attributionSource;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0024  */
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
                case -1249285581: goto L19;
                case -976571353: goto Lf;
                default: goto Le;
            }
        Le:
            goto L24
        Lf:
            java.lang.String r0 = "get-system-grammatical-gender"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 1
            goto L25
        L19:
            java.lang.String r0 = "set-system-grammatical-gender"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto Le
            r0 = 0
            goto L25
        L24:
            r0 = -1
        L25:
            switch(r0) {
                case 0: goto L32;
                case 1: goto L2d;
                default: goto L28;
            }
        L28:
            int r0 = r1.handleDefaultCommands(r2)
            return r0
        L2d:
            int r0 = r1.runGetSystemGrammaticalGender()
            return r0
        L32:
            int r0 = r1.runSetSystemWideGrammaticalGender()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.grammaticalinflection.GrammaticalInflectionShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Grammatical inflection manager (grammatical_inflection) shell commands:");
        pw.println("  help");
        pw.println("      Print this help text.");
        pw.println("  set-system-grammatical-gender [--user <USER_ID>] [--grammaticalGender <GRAMMATICAL_GENDER>]");
        pw.println("      Set the system grammatical gender for system.");
        pw.println("      --user <USER_ID>: apply for the given user, the current user is used when unspecified.");
        pw.println("      --grammaticalGender <GRAMMATICAL_GENDER>: The terms of address the user preferred in system, not specified (0) is used when unspecified.");
        pw.println("                 eg. 0 = not_specified, 1 = neuter, 2 = feminine, 3 = masculine.");
        pw.println("  get-system-grammatical-gender [--user <USER_ID>]");
        pw.println("      Get the system grammatical gender for system.");
        pw.println("      --user <USER_ID>: apply for the given user, the current user is used when unspecified.");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0054  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runSetSystemWideGrammaticalGender() {
        /*
            r7 = this;
            int r0 = android.app.ActivityManager.getCurrentUser()
            r1 = 0
        L5:
            java.lang.String r2 = r7.getNextOption()
            r3 = 0
            if (r2 != 0) goto L2f
        Ld:
            android.app.IGrammaticalInflectionManager r2 = r7.mBinderService     // Catch: android.os.RemoteException -> L13
            r2.setSystemWideGrammaticalGender(r1, r0)     // Catch: android.os.RemoteException -> L13
            goto L2e
        L13:
            r2 = move-exception
            java.io.PrintWriter r4 = r7.getOutPrintWriter()
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Remote Exception: "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r2)
            java.lang.String r5 = r5.toString()
            r4.println(r5)
        L2e:
            return r3
        L2f:
            int r4 = r2.hashCode()
            switch(r4) {
                case 1498: goto L4a;
                case 1333469547: goto L41;
                case 2015742127: goto L37;
                default: goto L36;
            }
        L36:
            goto L54
        L37:
            java.lang.String r3 = "--grammaticalGender"
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L36
            r3 = 2
            goto L55
        L41:
            java.lang.String r4 = "--user"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L36
            goto L55
        L4a:
            java.lang.String r3 = "-g"
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L36
            r3 = 1
            goto L55
        L54:
            r3 = -1
        L55:
            switch(r3) {
                case 0: goto L76;
                case 1: goto L71;
                case 2: goto L71;
                default: goto L58;
            }
        L58:
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Unknown option: "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r2)
            java.lang.String r4 = r4.toString()
            r3.<init>(r4)
            throw r3
        L71:
            int r1 = r7.parseGrammaticalGender()
            goto L7f
        L76:
            java.lang.String r3 = r7.getNextArgRequired()
            int r0 = android.os.UserHandle.parseUserArg(r3)
        L7f:
            goto L5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.grammaticalinflection.GrammaticalInflectionShellCommand.runSetSystemWideGrammaticalGender():int");
    }

    private int runGetSystemGrammaticalGender() {
        int userId = android.app.ActivityManager.getCurrentUser();
        while (true) {
            java.lang.String option = getNextOption();
            byte b = 0;
            if (option != null) {
                switch (option.hashCode()) {
                    case 1333469547:
                        if (!option.equals("--user")) {
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    default:
                        throw new java.lang.IllegalArgumentException("Unknown option: " + option);
                }
            } else {
                try {
                    int grammaticalGender = this.mBinderService.getSystemGrammaticalGender(this.mAttributionSource, userId);
                    getOutPrintWriter().println(GRAMMATICAL_GENDER_MAP.get(grammaticalGender));
                } catch (android.os.RemoteException e) {
                    getOutPrintWriter().println("Remote Exception: " + e);
                }
                return 0;
            }
        }
    }

    private int parseGrammaticalGender() {
        java.lang.String arg = getNextArg();
        if (arg == null) {
            return 0;
        }
        int grammaticalGender = java.lang.Integer.parseInt(arg);
        if (!android.app.GrammaticalInflectionManager.VALID_GRAMMATICAL_GENDER_VALUES.contains(java.lang.Integer.valueOf(grammaticalGender))) {
            return 0;
        }
        return grammaticalGender;
    }
}
