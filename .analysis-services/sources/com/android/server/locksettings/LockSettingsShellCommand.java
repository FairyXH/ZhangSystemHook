package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class LockSettingsShellCommand extends android.os.ShellCommand {
    private static final java.lang.String COMMAND_CLEAR = "clear";
    private static final java.lang.String COMMAND_GET_DISABLED = "get-disabled";
    private static final java.lang.String COMMAND_HELP = "help";
    private static final java.lang.String COMMAND_REMOVE_CACHE = "remove-cache";
    private static final java.lang.String COMMAND_REQUIRE_STRONG_AUTH = "require-strong-auth";
    private static final java.lang.String COMMAND_SET_DISABLED = "set-disabled";
    private static final java.lang.String COMMAND_SET_PASSWORD = "set-password";
    private static final java.lang.String COMMAND_SET_PATTERN = "set-pattern";
    private static final java.lang.String COMMAND_SET_PIN = "set-pin";
    private static final java.lang.String COMMAND_SET_ROR_PROVIDER_PACKAGE = "set-resume-on-reboot-provider-package";
    private static final java.lang.String COMMAND_VERIFY = "verify";
    private final int mCallingPid;
    private final int mCallingUid;
    private final android.content.Context mContext;
    private int mCurrentUserId;
    private final com.android.internal.widget.LockPatternUtils mLockPatternUtils;
    private java.lang.String mOld = "";
    private java.lang.String mNew = "";

    LockSettingsShellCommand(com.android.internal.widget.LockPatternUtils lockPatternUtils, android.content.Context context, int callingPid, int callingUid) {
        this.mLockPatternUtils = lockPatternUtils;
        this.mCallingPid = callingPid;
        this.mCallingUid = callingUid;
        this.mContext = context;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0035  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00fa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r12) {
        /*
            Method dump skipped, instruction units count: 510
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.locksettings.LockSettingsShellCommand.onCommand(java.lang.String):int");
    }

    private void runVerify() {
        getOutPrintWriter().println("Lock credential verified successfully");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            pw.println("lockSettings service commands:");
            pw.println("");
            pw.println("NOTE: when a secure lock screen is set, most commands require the");
            pw.println("--old <CREDENTIAL> option.");
            pw.println("");
            pw.println("  help");
            pw.println("    Prints this help text.");
            pw.println("");
            pw.println("  get-disabled [--user USER_ID]");
            pw.println("    Prints true if the lock screen is completely disabled, i.e. set to None.");
            pw.println("    Otherwise prints false.");
            pw.println("");
            pw.println("  set-disabled [--user USER_ID] <true|false>");
            pw.println("    Sets whether the lock screen is disabled. If the lock screen is secure, this");
            pw.println("    has no immediate effect. I.e. this can only change between Swipe and None.");
            pw.println("");
            pw.println("  set-pattern [--old <CREDENTIAL>] [--user USER_ID] <PATTERN>");
            pw.println("    Sets a secure lock screen that uses the given PATTERN. PATTERN is a series");
            pw.println("    of digits 1-9 that identify the cells of the pattern.");
            pw.println("");
            pw.println("  set-pin [--old <CREDENTIAL>] [--user USER_ID] <PIN>");
            pw.println("    Sets a secure lock screen that uses the given PIN.");
            pw.println("");
            pw.println("  set-password [--old <CREDENTIAL>] [--user USER_ID] <PASSWORD>");
            pw.println("    Sets a secure lock screen that uses the given PASSWORD.");
            pw.println("");
            pw.println("  clear [--old <CREDENTIAL>] [--user USER_ID]");
            pw.println("    Clears the lock credential.");
            pw.println("");
            pw.println("  verify [--old <CREDENTIAL>] [--user USER_ID]");
            pw.println("    Verifies the lock credential.");
            pw.println("");
            pw.println("  remove-cache [--user USER_ID]");
            pw.println("    Removes cached unified challenge for the managed profile.");
            pw.println("");
            pw.println("  set-resume-on-reboot-provider-package <package_name>");
            pw.println("    Sets the package name for server based resume on reboot service provider.");
            pw.println("");
            pw.println("  require-strong-auth [--user USER_ID] <reason>");
            pw.println("    Requires strong authentication. The current supported reasons:");
            pw.println("    STRONG_AUTH_REQUIRED_AFTER_USER_LOCKDOWN.");
            pw.println("");
            if (pw != null) {
                pw.close();
            }
        } catch (java.lang.Throwable th) {
            if (pw != null) {
                try {
                    pw.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private void parseArgs() {
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if ("--old".equals(opt)) {
                    this.mOld = getNextArgRequired();
                } else if ("--user".equals(opt)) {
                    this.mCurrentUserId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                    if (this.mCurrentUserId == -2) {
                        this.mCurrentUserId = android.app.ActivityManager.getCurrentUser();
                    }
                } else {
                    getErrPrintWriter().println("Unknown option: " + opt);
                    throw new java.lang.IllegalArgumentException();
                }
            } else {
                this.mNew = getNextArg();
                return;
            }
        }
    }

    private com.android.internal.widget.LockscreenCredential getOldCredential() {
        if (android.text.TextUtils.isEmpty(this.mOld)) {
            return com.android.internal.widget.LockscreenCredential.createNone();
        }
        if (this.mLockPatternUtils.isLockPasswordEnabled(this.mCurrentUserId)) {
            int quality = this.mLockPatternUtils.getKeyguardStoredPasswordQuality(this.mCurrentUserId);
            if (com.android.internal.widget.LockPatternUtils.isQualityAlphabeticPassword(quality)) {
                return com.android.internal.widget.LockscreenCredential.createPassword(this.mOld);
            }
            return com.android.internal.widget.LockscreenCredential.createPin(this.mOld);
        }
        if (this.mLockPatternUtils.isLockPatternEnabled(this.mCurrentUserId)) {
            return com.android.internal.widget.LockscreenCredential.createPattern(com.android.internal.widget.LockPatternUtils.byteArrayToPattern(this.mOld.getBytes()));
        }
        return com.android.internal.widget.LockscreenCredential.createPassword(this.mOld);
    }

    private boolean runSetPattern() {
        com.android.internal.widget.LockscreenCredential pattern = com.android.internal.widget.LockscreenCredential.createPattern(com.android.internal.widget.LockPatternUtils.byteArrayToPattern(this.mNew.getBytes()));
        if (!isNewCredentialSufficient(pattern)) {
            return false;
        }
        this.mLockPatternUtils.setLockCredential(pattern, getOldCredential(), this.mCurrentUserId);
        getOutPrintWriter().println("Pattern set to '" + this.mNew + "'");
        return true;
    }

    private boolean runSetPassword() {
        com.android.internal.widget.LockscreenCredential password = com.android.internal.widget.LockscreenCredential.createPassword(this.mNew);
        if (!isNewCredentialSufficient(password)) {
            return false;
        }
        this.mLockPatternUtils.setLockCredential(password, getOldCredential(), this.mCurrentUserId);
        getOutPrintWriter().println("Password set to '" + this.mNew + "'");
        return true;
    }

    private boolean runSetPin() {
        com.android.internal.widget.LockscreenCredential pin = com.android.internal.widget.LockscreenCredential.createPin(this.mNew);
        if (!isNewCredentialSufficient(pin)) {
            return false;
        }
        this.mLockPatternUtils.setLockCredential(pin, getOldCredential(), this.mCurrentUserId);
        getOutPrintWriter().println("Pin set to '" + this.mNew + "'");
        return true;
    }

    private boolean runSetResumeOnRebootProviderPackage() {
        java.lang.String packageName = this.mNew;
        android.util.Slog.i("ShellCommand", "Setting persist.sys.resume_on_reboot_provider_package to " + packageName);
        this.mContext.enforcePermission("android.permission.BIND_RESUME_ON_REBOOT_SERVICE", this.mCallingPid, this.mCallingUid, "ShellCommand");
        android.os.SystemProperties.set("persist.sys.resume_on_reboot_provider_package", packageName);
        return true;
    }

    private boolean runRequireStrongAuth() {
        byte b;
        java.lang.String reason = this.mNew;
        switch (reason.hashCode()) {
            case 1785592813:
                if (reason.equals("STRONG_AUTH_REQUIRED_AFTER_USER_LOCKDOWN")) {
                    b = 0;
                    break;
                }
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                this.mCurrentUserId = -1;
                this.mLockPatternUtils.requireStrongAuth(32, this.mCurrentUserId);
                getOutPrintWriter().println("Require strong auth for USER_ID " + this.mCurrentUserId + " because of " + this.mNew);
                return true;
            default:
                getErrPrintWriter().println("Unsupported reason: " + reason);
                return false;
        }
    }

    private boolean runClear() {
        com.android.internal.widget.LockscreenCredential none = com.android.internal.widget.LockscreenCredential.createNone();
        if (!isNewCredentialSufficient(none)) {
            return false;
        }
        this.mLockPatternUtils.setLockCredential(none, getOldCredential(), this.mCurrentUserId);
        getOutPrintWriter().println("Lock credential cleared");
        return true;
    }

    private boolean isNewCredentialSufficient(com.android.internal.widget.LockscreenCredential credential) {
        android.app.admin.PasswordMetrics requiredMetrics = this.mLockPatternUtils.getRequestedPasswordMetrics(this.mCurrentUserId);
        int requiredComplexity = this.mLockPatternUtils.getRequestedPasswordComplexity(this.mCurrentUserId);
        java.util.List<com.android.internal.widget.PasswordValidationError> errors = android.app.admin.PasswordMetrics.validateCredential(requiredMetrics, requiredComplexity, credential);
        if (!errors.isEmpty()) {
            getOutPrintWriter().println("New credential doesn't satisfy admin policies: " + errors.get(0));
            return false;
        }
        return true;
    }

    private void runSetDisabled() {
        boolean disabled = java.lang.Boolean.parseBoolean(this.mNew);
        this.mLockPatternUtils.setLockScreenDisabled(disabled, this.mCurrentUserId);
        getOutPrintWriter().println("Lock screen disabled set to " + disabled);
    }

    private void runGetDisabled() {
        boolean isLockScreenDisabled = this.mLockPatternUtils.isLockScreenDisabled(this.mCurrentUserId);
        getOutPrintWriter().println(isLockScreenDisabled);
    }

    private boolean checkCredential() {
        if (this.mLockPatternUtils.isSecure(this.mCurrentUserId)) {
            if (this.mLockPatternUtils.isManagedProfileWithUnifiedChallenge(this.mCurrentUserId)) {
                getOutPrintWriter().println("Profile uses unified challenge");
                return false;
            }
            try {
                boolean result = this.mLockPatternUtils.checkCredential(getOldCredential(), this.mCurrentUserId, (com.android.internal.widget.LockPatternUtils.CheckCredentialProgressCallback) null);
                if (!result) {
                    if (!this.mLockPatternUtils.isManagedProfileWithUnifiedChallenge(this.mCurrentUserId)) {
                        this.mLockPatternUtils.reportFailedPasswordAttempt(this.mCurrentUserId);
                    }
                    getOutPrintWriter().println("Old password '" + this.mOld + "' didn't match");
                } else {
                    this.mLockPatternUtils.reportSuccessfulPasswordAttempt(this.mCurrentUserId);
                }
                return result;
            } catch (com.android.internal.widget.LockPatternUtils.RequestThrottledException e) {
                getOutPrintWriter().println("Request throttled");
                return false;
            }
        }
        if (!this.mOld.isEmpty()) {
            getOutPrintWriter().println("Old password provided but user has no password");
            return false;
        }
        return true;
    }

    private void runRemoveCache() {
        this.mLockPatternUtils.removeCachedUnifiedChallenge(this.mCurrentUserId);
        getOutPrintWriter().println("Password cached removed for user " + this.mCurrentUserId);
    }
}
