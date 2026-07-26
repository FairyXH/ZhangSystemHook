package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class DevicePolicyManagerServiceShellCommand extends android.os.ShellCommand {
    private static final java.lang.String CMD_CLEAR_FREEZE_PERIOD_RECORD = "clear-freeze-period-record";
    private static final java.lang.String CMD_FORCE_NETWORK_LOGS = "force-network-logs";
    private static final java.lang.String CMD_FORCE_SECURITY_LOGS = "force-security-logs";
    private static final java.lang.String CMD_IS_SAFE_OPERATION = "is-operation-safe";
    private static final java.lang.String CMD_IS_SAFE_OPERATION_BY_REASON = "is-operation-safe-by-reason";
    private static final java.lang.String CMD_LIST_OWNERS = "list-owners";
    private static final java.lang.String CMD_LIST_POLICY_EXEMPT_APPS = "list-policy-exempt-apps";
    private static final java.lang.String CMD_MARK_PO_ON_ORG_OWNED_DEVICE = "mark-profile-owner-on-organization-owned-device";
    private static final java.lang.String CMD_REMOVE_ACTIVE_ADMIN = "remove-active-admin";
    private static final java.lang.String CMD_SET_ACTIVE_ADMIN = "set-active-admin";
    private static final java.lang.String CMD_SET_DEVICE_OWNER = "set-device-owner";
    private static final java.lang.String CMD_SET_PROFILE_OWNER = "set-profile-owner";
    private static final java.lang.String CMD_SET_SAFE_OPERATION = "set-operation-safe";
    private static final java.lang.String DO_ONLY_OPTION = "--device-owner-only";
    private static final java.lang.String USER_OPTION = "--user";
    private android.content.ComponentName mComponent;
    private final com.android.server.devicepolicy.DevicePolicyManagerService mService;
    private boolean mSetDoOnly;
    private int mUserId = 0;

    DevicePolicyManagerServiceShellCommand(com.android.server.devicepolicy.DevicePolicyManagerService service) {
        this.mService = (com.android.server.devicepolicy.DevicePolicyManagerService) java.util.Objects.requireNonNull(service);
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            pw.printf("DevicePolicyManager Service (device_policy) commands:\n\n", new java.lang.Object[0]);
            showHelp(pw);
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

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0012  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r4) {
        /*
            Method dump skipped, instruction units count: 412
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.DevicePolicyManagerServiceShellCommand.onCommand(java.lang.String):int");
    }

    private int onInvalidCommand(java.io.PrintWriter pw, java.lang.String cmd) {
        if (super.handleDefaultCommands(cmd) == 0) {
            return 0;
        }
        pw.printf("Usage: \n", new java.lang.Object[0]);
        showHelp(pw);
        return -1;
    }

    private void showHelp(java.io.PrintWriter pw) {
        pw.printf("  help\n", new java.lang.Object[0]);
        pw.printf("    Prints this help text.\n\n", new java.lang.Object[0]);
        pw.printf("  %s <OPERATION_ID>\n", CMD_IS_SAFE_OPERATION);
        pw.printf("    Checks if the give operation is safe \n\n", new java.lang.Object[0]);
        pw.printf("  %s <REASON_ID>\n", CMD_IS_SAFE_OPERATION_BY_REASON);
        pw.printf("    Checks if the operations are safe for the given reason\n\n", new java.lang.Object[0]);
        pw.printf("  %s <OPERATION_ID> <REASON_ID>\n", CMD_SET_SAFE_OPERATION);
        pw.printf("    Emulates the result of the next call to check if the given operation is safe \n\n", new java.lang.Object[0]);
        pw.printf("  %s\n", CMD_LIST_OWNERS);
        pw.printf("    Lists the device / profile owners per user \n\n", new java.lang.Object[0]);
        pw.printf("  %s\n", CMD_LIST_POLICY_EXEMPT_APPS);
        pw.printf("    Lists the apps that are exempt from policies\n\n", new java.lang.Object[0]);
        pw.printf("  %s [ %s <USER_ID> | current ] <COMPONENT>\n", CMD_SET_ACTIVE_ADMIN, USER_OPTION);
        pw.printf("    Sets the given component as active admin for an existing user.\n\n", new java.lang.Object[0]);
        pw.printf("  %s [ %s <USER_ID> | current *EXPERIMENTAL* ] [ %s ]<COMPONENT>\n", CMD_SET_DEVICE_OWNER, USER_OPTION, DO_ONLY_OPTION);
        pw.printf("    Sets the given component as active admin, and its package as device owner.\n\n", new java.lang.Object[0]);
        pw.printf("  %s [ %s <USER_ID> | current ] <COMPONENT>\n", CMD_SET_PROFILE_OWNER, USER_OPTION);
        pw.printf("    Sets the given component as active admin and profile owner for an existing user.\n\n", new java.lang.Object[0]);
        pw.printf("  %s [ %s <USER_ID> | current ] <COMPONENT>\n", CMD_REMOVE_ACTIVE_ADMIN, USER_OPTION);
        pw.printf("    Disables an active admin, the admin must have declared android:testOnly in the application in its manifest. This will also remove device and profile owners.\n\n", new java.lang.Object[0]);
        pw.printf("  %s\n", CMD_CLEAR_FREEZE_PERIOD_RECORD);
        pw.printf("    Clears framework-maintained record of past freeze periods that the device went through. For use during feature development to prevent triggering restriction on setting freeze periods.\n\n", new java.lang.Object[0]);
        pw.printf("  %s\n", CMD_FORCE_NETWORK_LOGS);
        pw.printf("    Makes all network logs available to the DPC and triggers DeviceAdminReceiver.onNetworkLogsAvailable() if needed.\n\n", new java.lang.Object[0]);
        pw.printf("  %s\n", CMD_FORCE_SECURITY_LOGS);
        pw.printf("    Makes all security logs available to the DPC and triggers DeviceAdminReceiver.onSecurityLogsAvailable() if needed.\n\n", new java.lang.Object[0]);
        pw.printf("  %s [ %s <USER_ID> | current ] <COMPONENT>\n", CMD_MARK_PO_ON_ORG_OWNED_DEVICE, USER_OPTION);
        pw.printf("    Marks the profile owner of the given user as managing an organization-owneddevice. That will give it access to device identifiers (such as serial number, IMEI and MEID), as well as other privileges.\n\n", new java.lang.Object[0]);
    }

    private int runIsSafeOperation(java.io.PrintWriter pw) {
        int operation = java.lang.Integer.parseInt(getNextArgRequired());
        int reason = this.mService.getUnsafeOperationReason(operation);
        boolean safe = reason == -1;
        pw.printf("Operation %s is %s. Reason: %s\n", android.app.admin.DevicePolicyManager.operationToString(operation), safeToString(safe), android.app.admin.DevicePolicyManager.operationSafetyReasonToString(reason));
        return 0;
    }

    private int runIsSafeOperationByReason(java.io.PrintWriter pw) {
        int reason = java.lang.Integer.parseInt(getNextArgRequired());
        boolean safe = this.mService.isSafeOperation(reason);
        pw.printf("Operations affected by %s are %s\n", android.app.admin.DevicePolicyManager.operationSafetyReasonToString(reason), safeToString(safe));
        return 0;
    }

    private static java.lang.String safeToString(boolean safe) {
        return safe ? "SAFE" : "UNSAFE";
    }

    private int runSetSafeOperation(java.io.PrintWriter pw) {
        int operation = java.lang.Integer.parseInt(getNextArgRequired());
        int reason = java.lang.Integer.parseInt(getNextArgRequired());
        this.mService.setNextOperationSafety(operation, reason);
        pw.printf("Next call to check operation %s will return %s\n", android.app.admin.DevicePolicyManager.operationToString(operation), android.app.admin.DevicePolicyManager.operationSafetyReasonToString(reason));
        return 0;
    }

    private int printAndGetSize(java.io.PrintWriter pw, java.util.Collection<?> collection, java.lang.String nameOnSingular) {
        if (collection.isEmpty()) {
            pw.printf("no %ss\n", nameOnSingular);
            return 0;
        }
        int size = collection.size();
        pw.printf("%d %s%s:\n", java.lang.Integer.valueOf(size), nameOnSingular, size == 1 ? "" : "s");
        return size;
    }

    private int runListOwners(java.io.PrintWriter pw) {
        java.util.List<com.android.server.devicepolicy.OwnerShellData> owners = this.mService.listAllOwners();
        int size = printAndGetSize(pw, owners, "owner");
        if (size == 0) {
            return 0;
        }
        for (int i = 0; i < size; i++) {
            com.android.server.devicepolicy.OwnerShellData owner = owners.get(i);
            pw.printf("User %2d: admin=%s", java.lang.Integer.valueOf(owner.userId), owner.admin.flattenToShortString());
            if (owner.isDeviceOwner) {
                pw.print(",DeviceOwner");
            }
            if (owner.isProfileOwner) {
                pw.print(",ProfileOwner");
            }
            if (owner.isManagedProfileOwner) {
                pw.printf(",ManagedProfileOwner(parentUserId=%d)", java.lang.Integer.valueOf(owner.parentUserId));
            }
            if (owner.isAffiliated) {
                pw.print(",Affiliated");
            }
            pw.println();
        }
        return 0;
    }

    private int runListPolicyExemptApps(java.io.PrintWriter pw) {
        java.util.List<java.lang.String> apps = this.mService.listPolicyExemptApps();
        int size = printAndGetSize(pw, apps, "policy exempt app");
        if (size == 0) {
            return 0;
        }
        for (int i = 0; i < size; i++) {
            java.lang.String app = apps.get(i);
            pw.printf("  %d: %s\n", java.lang.Integer.valueOf(i), app);
        }
        return 0;
    }

    private int runSetActiveAdmin(java.io.PrintWriter pw) {
        parseArgs();
        this.mService.setActiveAdmin(this.mComponent, true, this.mUserId);
        pw.printf("Success: Active admin set to component %s\n", this.mComponent.flattenToShortString());
        return 0;
    }

    private int runSetDeviceOwner(java.io.PrintWriter pw) throws java.lang.Exception {
        parseArgs();
        boolean isAdminAdded = false;
        try {
            this.mService.setActiveAdmin(this.mComponent, false, this.mUserId);
            isAdminAdded = true;
        } catch (java.lang.IllegalArgumentException e) {
            pw.printf("%s was already an admin for user %d. No need to set it again.\n", this.mComponent.flattenToShortString(), java.lang.Integer.valueOf(this.mUserId));
        }
        try {
            if (!this.mService.setDeviceOwner(this.mComponent, this.mUserId, !this.mSetDoOnly)) {
                throw new java.lang.RuntimeException("Can't set package " + this.mComponent + " as device owner.");
            }
            this.mService.setUserProvisioningState(3, this.mUserId);
            pw.printf("Success: Device owner set to package %s\n", this.mComponent.flattenToShortString());
            pw.printf("Active admin set to component %s\n", this.mComponent.flattenToShortString());
            return 0;
        } catch (java.lang.Exception e2) {
            if (isAdminAdded) {
                this.mService.removeActiveAdmin(this.mComponent, this.mUserId);
            }
            throw e2;
        }
    }

    private int runRemoveActiveAdmin(java.io.PrintWriter pw) {
        parseArgs();
        this.mService.forceRemoveActiveAdmin(this.mComponent, this.mUserId);
        pw.printf("Success: Admin removed %s\n", this.mComponent);
        return 0;
    }

    private int runSetProfileOwner(java.io.PrintWriter pw) throws java.lang.Exception {
        parseArgs();
        this.mService.setActiveAdmin(this.mComponent, true, this.mUserId);
        try {
            if (!this.mService.setProfileOwner(this.mComponent, this.mUserId)) {
                throw new java.lang.RuntimeException("Can't set component " + this.mComponent.flattenToShortString() + " as profile owner for user " + this.mUserId);
            }
            this.mService.setUserProvisioningState(3, this.mUserId);
            pw.printf("Success: Active admin and profile owner set to %s for user %d\n", this.mComponent.flattenToShortString(), java.lang.Integer.valueOf(this.mUserId));
            return 0;
        } catch (java.lang.Exception e) {
            this.mService.removeActiveAdmin(this.mComponent, this.mUserId);
            throw e;
        }
    }

    private int runClearFreezePeriodRecord(java.io.PrintWriter pw) {
        this.mService.clearSystemUpdatePolicyFreezePeriodRecord();
        pw.printf("Success\n", new java.lang.Object[0]);
        return 0;
    }

    private int runForceNetworkLogs(java.io.PrintWriter pw) {
        while (true) {
            long toWait = this.mService.forceNetworkLogs();
            if (toWait != 0) {
                pw.printf("We have to wait for %d milliseconds...\n", java.lang.Long.valueOf(toWait));
                android.os.SystemClock.sleep(toWait);
            } else {
                pw.printf("Success\n", new java.lang.Object[0]);
                return 0;
            }
        }
    }

    private int runForceSecurityLogs(java.io.PrintWriter pw) {
        while (true) {
            long toWait = this.mService.forceSecurityLogs();
            if (toWait != 0) {
                pw.printf("We have to wait for %d milliseconds...\n", java.lang.Long.valueOf(toWait));
                android.os.SystemClock.sleep(toWait);
            } else {
                pw.printf("Success\n", new java.lang.Object[0]);
                return 0;
            }
        }
    }

    private int runMarkProfileOwnerOnOrganizationOwnedDevice(java.io.PrintWriter pw) {
        parseArgs();
        this.mService.setProfileOwnerOnOrganizationOwnedDevice(this.mComponent, this.mUserId, true);
        pw.printf("Success\n", new java.lang.Object[0]);
        return 0;
    }

    private void parseArgs() {
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (USER_OPTION.equals(opt)) {
                    java.lang.String arg = getNextArgRequired();
                    this.mUserId = android.os.UserHandle.parseUserArg(arg);
                    if (this.mUserId == -2) {
                        this.mUserId = android.app.ActivityManager.getCurrentUser();
                    }
                } else if (DO_ONLY_OPTION.equals(opt)) {
                    this.mSetDoOnly = true;
                } else {
                    throw new java.lang.IllegalArgumentException("Unknown option: " + opt);
                }
            } else {
                this.mComponent = parseComponentName(getNextArgRequired());
                return;
            }
        }
    }

    private android.content.ComponentName parseComponentName(java.lang.String component) {
        android.content.ComponentName cn = android.content.ComponentName.unflattenFromString(component);
        if (cn == null) {
            throw new java.lang.IllegalArgumentException("Invalid component " + component);
        }
        return cn;
    }
}
