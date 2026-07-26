package com.android.server.pm.verify.domain;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationShell {
    private final com.android.server.pm.verify.domain.DomainVerificationShell.Callback mCallback;

    public interface Callback {
        void clearDomainVerificationState(java.util.List<java.lang.String> list);

        void clearUserStates(java.util.List<java.lang.String> list, int i);

        android.content.pm.verify.domain.DomainVerificationUserState getDomainVerificationUserState(java.lang.String str, int i) throws android.content.pm.PackageManager.NameNotFoundException;

        void printOwnersForDomains(android.util.IndentingPrintWriter indentingPrintWriter, java.util.List<java.lang.String> list, java.lang.Integer num);

        void printOwnersForPackage(android.util.IndentingPrintWriter indentingPrintWriter, java.lang.String str, java.lang.Integer num) throws android.content.pm.PackageManager.NameNotFoundException;

        void printState(android.util.IndentingPrintWriter indentingPrintWriter, java.lang.String str, java.lang.Integer num) throws android.content.pm.PackageManager.NameNotFoundException;

        void setDomainVerificationLinkHandlingAllowedInternal(java.lang.String str, boolean z, int i) throws android.content.pm.PackageManager.NameNotFoundException;

        void setDomainVerificationStatusInternal(java.lang.String str, int i, android.util.ArraySet<java.lang.String> arraySet) throws android.content.pm.PackageManager.NameNotFoundException;

        void setDomainVerificationUserSelectionInternal(int i, java.lang.String str, boolean z, android.util.ArraySet<java.lang.String> arraySet) throws android.content.pm.PackageManager.NameNotFoundException;

        void verifyPackages(java.util.List<java.lang.String> list, boolean z);
    }

    public DomainVerificationShell(com.android.server.pm.verify.domain.DomainVerificationShell.Callback callback) {
        this.mCallback = callback;
    }

    public void printHelp(java.io.PrintWriter pw) {
        pw.println("  get-app-links [--user <USER_ID>] [<PACKAGE>]");
        pw.println("    Prints the domain verification state for the given package, or for all");
        pw.println("    packages if none is specified. State codes are defined as follows:");
        pw.println("        - none: nothing has been recorded for this domain");
        pw.println("        - verified: the domain has been successfully verified");
        pw.println("        - approved: force approved, usually through shell");
        pw.println("        - denied: force denied, usually through shell");
        pw.println("        - migrated: preserved verification from a legacy response");
        pw.println("        - restored: preserved verification from a user data restore");
        pw.println("        - legacy_failure: rejected by a legacy verifier, unknown reason");
        pw.println("        - system_configured: automatically approved by the device config");
        pw.println("        - pre_verified: the domain was pre-verified by the installer");
        pw.println("        - >= 1024: Custom error code which is specific to the device verifier");
        pw.println("      --user <USER_ID>: include user selections (includes all domains, not");
        pw.println("        just autoVerify ones)");
        pw.println("  reset-app-links [--user <USER_ID>] [<PACKAGE>]");
        pw.println("    Resets domain verification state for the given package, or for all");
        pw.println("    packages if none is specified.");
        pw.println("      --user <USER_ID>: clear user selection state instead; note this means");
        pw.println("        domain verification state will NOT be cleared");
        pw.println("      <PACKAGE>: the package to reset, or \"all\" to reset all packages");
        pw.println("  verify-app-links [--re-verify] [<PACKAGE>]");
        pw.println("    Broadcasts a verification request for the given package, or for all");
        pw.println("    packages if none is specified. Only sends if the package has previously");
        pw.println("    not recorded a response.");
        pw.println("      --re-verify: send even if the package has recorded a response");
        pw.println("  set-app-links [--package <PACKAGE>] <STATE> <DOMAINS>...");
        pw.println("    Manually set the state of a domain for a package. The domain must be");
        pw.println("    declared by the package as autoVerify for this to work. This command");
        pw.println("    will not report a failure for domains that could not be applied.");
        pw.println("      --package <PACKAGE>: the package to set, or \"all\" to set all packages");
        pw.println("      <STATE>: the code to set the domains to, valid values are:");
        pw.println("        STATE_NO_RESPONSE (0): reset as if no response was ever recorded.");
        pw.println("        STATE_SUCCESS (1): treat domain as successfully verified by domain.");
        pw.println("          verification agent. Note that the domain verification agent can");
        pw.println("          override this.");
        pw.println("        STATE_APPROVED (2): treat domain as always approved, preventing the");
        pw.println("           domain verification agent from changing it.");
        pw.println("        STATE_DENIED (3): treat domain as always denied, preveting the domain");
        pw.println("          verification agent from changing it.");
        pw.println("      <DOMAINS>: space separated list of domains to change, or \"all\" to");
        pw.println("        change every domain.");
        pw.println("  set-app-links-user-selection --user <USER_ID> [--package <PACKAGE>]");
        pw.println("      <ENABLED> <DOMAINS>...");
        pw.println("    Manually set the state of a host user selection for a package. The domain");
        pw.println("    must be declared by the package for this to work. This command will not");
        pw.println("    report a failure for domains that could not be applied.");
        pw.println("      --user <USER_ID>: the user to change selections for");
        pw.println("      --package <PACKAGE>: the package to set");
        pw.println("      <ENABLED>: whether or not to approve the domain");
        pw.println("      <DOMAINS>: space separated list of domains to change, or \"all\" to");
        pw.println("        change every domain.");
        pw.println("  set-app-links-allowed --user <USER_ID> [--package <PACKAGE>] <ALLOWED>");
        pw.println("    Toggle the auto verified link handling setting for a package.");
        pw.println("      --user <USER_ID>: the user to change selections for");
        pw.println("      --package <PACKAGE>: the package to set, or \"all\" to set all packages");
        pw.println("        packages will be reset if no one package is specified.");
        pw.println("      <ALLOWED>: true to allow the package to open auto verified links, false");
        pw.println("        to disable");
        pw.println("  get-app-link-owners [--user <USER_ID>] [--package <PACKAGE>] [<DOMAINS>]");
        pw.println("    Print the owners for a specific domain for a given user in low to high");
        pw.println("    priority order.");
        pw.println("      --user <USER_ID>: the user to query for");
        pw.println("      --package <PACKAGE>: optionally also print for all web domains declared");
        pw.println("        by a package, or \"all\" to print all packages");
        pw.println("      --<DOMAINS>: space separated list of domains to query for");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0053  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Boolean runCommand(com.android.modules.utils.BasicShellCommandHandler r2, java.lang.String r3) {
        /*
            r1 = this;
            int r0 = r3.hashCode()
            switch(r0) {
                case -2140094634: goto L49;
                case -2092945963: goto L3e;
                case -1850904515: goto L33;
                case -1365963422: goto L28;
                case -825562609: goto L1d;
                case 1161008944: goto L13;
                case 1328605369: goto L8;
                default: goto L7;
            }
        L7:
            goto L53
        L8:
            java.lang.String r0 = "verify-app-links"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L7
            r0 = 2
            goto L54
        L13:
            java.lang.String r0 = "get-app-link-owners"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L7
            r0 = 6
            goto L54
        L1d:
            java.lang.String r0 = "reset-app-links"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L7
            r0 = 1
            goto L54
        L28:
            java.lang.String r0 = "set-app-links"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L7
            r0 = 3
            goto L54
        L33:
            java.lang.String r0 = "set-app-links-allowed"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L7
            r0 = 5
            goto L54
        L3e:
            java.lang.String r0 = "set-app-links-user-selection"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L7
            r0 = 4
            goto L54
        L49:
            java.lang.String r0 = "get-app-links"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L7
            r0 = 0
            goto L54
        L53:
            r0 = -1
        L54:
            switch(r0) {
                case 0: goto L8f;
                case 1: goto L86;
                case 2: goto L7d;
                case 3: goto L74;
                case 4: goto L6b;
                case 5: goto L62;
                case 6: goto L59;
                default: goto L57;
            }
        L57:
            r0 = 0
            return r0
        L59:
            boolean r0 = r1.runGetAppLinkOwners(r2)
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        L62:
            boolean r0 = r1.runSetAppLinksAllowed(r2)
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        L6b:
            boolean r0 = r1.runSetAppLinksUserState(r2)
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        L74:
            boolean r0 = r1.runSetAppLinks(r2)
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        L7d:
            boolean r0 = r1.runVerifyAppLinks(r2)
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        L86:
            boolean r0 = r1.runResetAppLinks(r2)
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        L8f:
            boolean r0 = r1.runGetAppLinks(r2)
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.verify.domain.DomainVerificationShell.runCommand(com.android.modules.utils.BasicShellCommandHandler, java.lang.String):java.lang.Boolean");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00a7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean runSetAppLinks(com.android.modules.utils.BasicShellCommandHandler r11) {
        /*
            Method dump skipped, instruction units count: 334
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.verify.domain.DomainVerificationShell.runSetAppLinks(com.android.modules.utils.BasicShellCommandHandler):boolean");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean runSetAppLinksUserState(com.android.modules.utils.BasicShellCommandHandler r12) {
        /*
            Method dump skipped, instruction units count: 286
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.verify.domain.DomainVerificationShell.runSetAppLinksUserState(com.android.modules.utils.BasicShellCommandHandler):boolean");
    }

    private boolean runGetAppLinks(com.android.modules.utils.BasicShellCommandHandler commandHandler) {
        java.lang.Integer userId = null;
        while (true) {
            java.lang.String option = commandHandler.getNextOption();
            if (option != null) {
                if (option.equals("--user")) {
                    userId = java.lang.Integer.valueOf(android.os.UserHandle.parseUserArg(commandHandler.getNextArgRequired()));
                } else {
                    commandHandler.getErrPrintWriter().println("Error: unknown option: " + option);
                    return false;
                }
            } else {
                java.lang.Integer userId2 = userId == null ? null : java.lang.Integer.valueOf(translateUserId(userId.intValue(), "runGetAppLinks"));
                java.lang.String packageName = commandHandler.getNextArg();
                android.util.IndentingPrintWriter writer = new android.util.IndentingPrintWriter(commandHandler.getOutPrintWriter(), "  ", 120);
                try {
                    writer.increaseIndent();
                    try {
                        this.mCallback.printState(writer, packageName, userId2);
                        writer.decreaseIndent();
                        writer.close();
                        return true;
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        commandHandler.getErrPrintWriter().println("Error: package " + packageName + " unavailable");
                        writer.close();
                        return false;
                    }
                } catch (java.lang.Throwable th) {
                    try {
                        writer.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        }
    }

    private boolean runResetAppLinks(com.android.modules.utils.BasicShellCommandHandler commandHandler) {
        java.util.List<java.lang.String> packageNames;
        java.lang.Integer userId = null;
        while (true) {
            java.lang.String option = commandHandler.getNextOption();
            if (option != null) {
                if (option.equals("--user")) {
                    userId = java.lang.Integer.valueOf(android.os.UserHandle.parseUserArg(commandHandler.getNextArgRequired()));
                } else {
                    commandHandler.getErrPrintWriter().println("Error: unknown option: " + option);
                    return false;
                }
            } else {
                java.lang.Integer userId2 = userId == null ? null : java.lang.Integer.valueOf(translateUserId(userId.intValue(), "runResetAppLinks"));
                java.lang.String pkgNameArg = commandHandler.peekNextArg();
                if (android.text.TextUtils.isEmpty(pkgNameArg)) {
                    commandHandler.getErrPrintWriter().println("Error: no package specified");
                    return false;
                }
                if (pkgNameArg.equalsIgnoreCase("all")) {
                    packageNames = null;
                } else {
                    packageNames = java.util.Arrays.asList(commandHandler.peekRemainingArgs());
                }
                if (userId2 != null) {
                    this.mCallback.clearUserStates(packageNames, userId2.intValue());
                    return true;
                }
                this.mCallback.clearDomainVerificationState(packageNames);
                return true;
            }
        }
    }

    private boolean runVerifyAppLinks(com.android.modules.utils.BasicShellCommandHandler commandHandler) {
        boolean reVerify = false;
        while (true) {
            java.lang.String option = commandHandler.getNextOption();
            if (option != null) {
                if (option.equals("--re-verify")) {
                    reVerify = true;
                } else {
                    commandHandler.getErrPrintWriter().println("Error: unknown option: " + option);
                    return false;
                }
            } else {
                java.util.List<java.lang.String> packageNames = null;
                java.lang.String pkgNameArg = commandHandler.getNextArg();
                if (!android.text.TextUtils.isEmpty(pkgNameArg)) {
                    packageNames = java.util.Collections.singletonList(pkgNameArg);
                }
                this.mCallback.verifyPackages(packageNames, reVerify);
                return true;
            }
        }
    }

    private boolean runSetAppLinksAllowed(com.android.modules.utils.BasicShellCommandHandler commandHandler) {
        java.lang.String packageName = null;
        java.lang.Integer userId = null;
        while (true) {
            java.lang.String option = commandHandler.getNextOption();
            if (option != null) {
                if (option.equals("--package")) {
                    packageName = commandHandler.getNextArg();
                } else if (option.equals("--user")) {
                    userId = java.lang.Integer.valueOf(android.os.UserHandle.parseUserArg(commandHandler.getNextArgRequired()));
                } else {
                    commandHandler.getErrPrintWriter().println("Error: unexpected option: " + option);
                    return false;
                }
            } else {
                if (android.text.TextUtils.isEmpty(packageName)) {
                    commandHandler.getErrPrintWriter().println("Error: no package specified");
                    return false;
                }
                if (packageName.equalsIgnoreCase("all")) {
                    packageName = null;
                }
                if (userId == null) {
                    commandHandler.getErrPrintWriter().println("Error: user ID not specified");
                    return false;
                }
                java.lang.String allowedArg = commandHandler.getNextArg();
                if (android.text.TextUtils.isEmpty(allowedArg)) {
                    commandHandler.getErrPrintWriter().println("Error: allowed setting not specified");
                    return false;
                }
                try {
                    boolean allowed = parseEnabled(allowedArg);
                    try {
                        this.mCallback.setDomainVerificationLinkHandlingAllowedInternal(packageName, allowed, java.lang.Integer.valueOf(translateUserId(userId.intValue(), "runSetAppLinksAllowed")).intValue());
                        return true;
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        commandHandler.getErrPrintWriter().println("Package not found: " + packageName);
                        return false;
                    }
                } catch (java.lang.IllegalArgumentException e2) {
                    commandHandler.getErrPrintWriter().println("Error: invalid allowed setting: " + e2.getMessage());
                    return false;
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean runGetAppLinkOwners(com.android.modules.utils.BasicShellCommandHandler r11) {
        /*
            Method dump skipped, instruction units count: 258
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.verify.domain.DomainVerificationShell.runGetAppLinkOwners(com.android.modules.utils.BasicShellCommandHandler):boolean");
    }

    private java.util.ArrayList<java.lang.String> getRemainingArgs(com.android.modules.utils.BasicShellCommandHandler commandHandler) {
        java.util.ArrayList<java.lang.String> args = new java.util.ArrayList<>();
        while (true) {
            java.lang.String arg = commandHandler.getNextArg();
            if (arg != null) {
                args.add(arg);
            } else {
                return args;
            }
        }
    }

    private int translateUserId(int userId, java.lang.String logContext) {
        return android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, true, true, logContext, "pm command");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0025  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean parseEnabled(java.lang.String r5) throws java.lang.IllegalArgumentException {
        /*
            r4 = this;
            java.util.Locale r0 = java.util.Locale.US
            java.lang.String r0 = r5.toLowerCase(r0)
            int r1 = r0.hashCode()
            r2 = 0
            r3 = 1
            switch(r1) {
                case 3569038: goto L1a;
                case 97196323: goto L10;
                default: goto Lf;
            }
        Lf:
            goto L25
        L10:
            java.lang.String r1 = "false"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Lf
            r0 = r3
            goto L26
        L1a:
            java.lang.String r1 = "true"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Lf
            r0 = r2
            goto L26
        L25:
            r0 = -1
        L26:
            switch(r0) {
                case 0: goto L43;
                case 1: goto L42;
                default: goto L29;
            }
        L29:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.StringBuilder r1 = r1.append(r5)
            java.lang.String r2 = " is not a valid boolean"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L42:
            return r2
        L43:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.verify.domain.DomainVerificationShell.parseEnabled(java.lang.String):boolean");
    }
}
