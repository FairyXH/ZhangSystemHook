package com.android.server.net;

/* JADX INFO: loaded from: classes2.dex */
class NetworkPolicyManagerShellCommand extends android.os.ShellCommand {
    private final com.android.server.net.NetworkPolicyManagerService mInterface;
    private final android.net.wifi.WifiManager mWifiManager;

    NetworkPolicyManagerShellCommand(android.content.Context context, com.android.server.net.NetworkPolicyManagerService service) {
        this.mInterface = service;
        this.mWifiManager = (android.net.wifi.WifiManager) context.getSystemService("wifi");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r6) {
        /*
            Method dump skipped, instruction units count: 212
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.net.NetworkPolicyManagerShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Network policy manager (netpolicy) commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("");
        pw.println("  add restrict-background-whitelist UID");
        pw.println("    Adds a UID to the whitelist for restrict background usage.");
        pw.println("  add restrict-background-blacklist UID");
        pw.println("    Adds a UID to the blacklist for restrict background usage.");
        pw.println("  add app-idle-whitelist UID");
        pw.println("    Adds a UID to the temporary app idle whitelist.");
        pw.println("  get restrict-background");
        pw.println("    Gets the global restrict background usage status.");
        pw.println("  list wifi-networks [true|false]");
        pw.println("    Lists all saved wifi networks and whether they are metered or not.");
        pw.println("    If a boolean argument is passed, filters just the metered (or unmetered)");
        pw.println("    networks.");
        pw.println("  list restrict-background-whitelist");
        pw.println("    Lists UIDs that are whitelisted for restrict background usage.");
        pw.println("  list restrict-background-blacklist");
        pw.println("    Lists UIDs that are blacklisted for restrict background usage.");
        pw.println("  remove restrict-background-whitelist UID");
        pw.println("    Removes a UID from the whitelist for restrict background usage.");
        pw.println("  remove restrict-background-blacklist UID");
        pw.println("    Removes a UID from the blacklist for restrict background usage.");
        pw.println("  remove app-idle-whitelist UID");
        pw.println("    Removes a UID from the temporary app idle whitelist.");
        pw.println("  set metered-network ID [undefined|true|false]");
        pw.println("    Toggles whether the given wi-fi network is metered.");
        pw.println("  set restrict-background BOOLEAN");
        pw.println("    Sets the global restrict background usage status.");
        pw.println("  set sub-plan-owner subId [packageName]");
        pw.println("    Sets the data plan owner package for subId.");
    }

    private int runGet() throws android.os.RemoteException {
        byte b;
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String type = getNextArg();
        if (type != null) {
            switch (type.hashCode()) {
                case -747095841:
                    b = !type.equals("restrict-background") ? (byte) -1 : (byte) 0;
                    break;
                case 909005781:
                    b = !type.equals("restricted-mode") ? (byte) -1 : (byte) 1;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    break;
                case 1:
                    break;
                default:
                    pw.println("Error: unknown get type '" + type + "'");
                    break;
            }
            return -1;
        }
        pw.println("Error: didn't specify type of data to get");
        return -1;
    }

    private int runSet() throws android.os.RemoteException {
        byte b;
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String type = getNextArg();
        if (type != null) {
            switch (type.hashCode()) {
                case -983249079:
                    b = !type.equals("metered-network") ? (byte) -1 : (byte) 0;
                    break;
                case -747095841:
                    b = !type.equals("restrict-background") ? (byte) -1 : (byte) 1;
                    break;
                case 1846940860:
                    b = !type.equals("sub-plan-owner") ? (byte) -1 : (byte) 2;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    pw.println("Error: unknown set type '" + type + "'");
                    break;
            }
            return -1;
        }
        pw.println("Error: didn't specify type of data to set");
        return -1;
    }

    private int runList() throws android.os.RemoteException {
        byte b;
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String type = getNextArg();
        if (type != null) {
            switch (type.hashCode()) {
                case -1683867974:
                    b = !type.equals("app-idle-whitelist") ? (byte) -1 : (byte) 0;
                    break;
                case -668534353:
                    b = !type.equals("restrict-background-blacklist") ? (byte) -1 : (byte) 3;
                    break;
                case -363534403:
                    b = !type.equals("wifi-networks") ? (byte) -1 : (byte) 1;
                    break;
                case 639570137:
                    b = !type.equals("restrict-background-whitelist") ? (byte) -1 : (byte) 2;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    pw.println("Error: unknown list type '" + type + "'");
                    break;
            }
            return -1;
        }
        pw.println("Error: didn't specify type of data to list");
        return -1;
    }

    private int runAdd() throws android.os.RemoteException {
        byte b;
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String type = getNextArg();
        if (type != null) {
            switch (type.hashCode()) {
                case -1683867974:
                    b = !type.equals("app-idle-whitelist") ? (byte) -1 : (byte) 2;
                    break;
                case -668534353:
                    b = !type.equals("restrict-background-blacklist") ? (byte) -1 : (byte) 1;
                    break;
                case 639570137:
                    b = !type.equals("restrict-background-whitelist") ? (byte) -1 : (byte) 0;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    pw.println("Error: unknown add type '" + type + "'");
                    break;
            }
            return -1;
        }
        pw.println("Error: didn't specify type of data to add");
        return -1;
    }

    private int runRemove() throws android.os.RemoteException {
        byte b;
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String type = getNextArg();
        if (type != null) {
            switch (type.hashCode()) {
                case -1683867974:
                    b = !type.equals("app-idle-whitelist") ? (byte) -1 : (byte) 2;
                    break;
                case -668534353:
                    b = !type.equals("restrict-background-blacklist") ? (byte) -1 : (byte) 1;
                    break;
                case 639570137:
                    b = !type.equals("restrict-background-whitelist") ? (byte) -1 : (byte) 0;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    pw.println("Error: unknown remove type '" + type + "'");
                    break;
            }
            return -1;
        }
        pw.println("Error: didn't specify type of data to remove");
        return -1;
    }

    private int runStartWatching() {
        int uid = java.lang.Integer.parseInt(getNextArgRequired());
        if (uid < 0) {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.print("Invalid UID: ");
            pw.println(uid);
            return -1;
        }
        this.mInterface.setDebugUid(uid);
        return 0;
    }

    private int runStopWatching() {
        this.mInterface.setDebugUid(-1);
        return 0;
    }

    private int listUidPolicies(java.lang.String msg, int policy) throws android.os.RemoteException {
        int[] uids = this.mInterface.getUidsWithPolicy(policy);
        return listUidList(msg, uids);
    }

    private int listUidList(java.lang.String msg, int[] uids) {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.print(msg);
        pw.print(": ");
        if (uids.length == 0) {
            pw.println("none");
        } else {
            for (int uid : uids) {
                pw.print(uid);
                pw.print(' ');
            }
        }
        pw.println();
        return 0;
    }

    private int listRestrictBackgroundAllowlist() throws android.os.RemoteException {
        return listUidPolicies("Restrict background whitelisted UIDs", 4);
    }

    private int listRestrictBackgroundDenylist() throws android.os.RemoteException {
        return listUidPolicies("Restrict background blacklisted UIDs", 1);
    }

    private int listAppIdleAllowlist() throws android.os.RemoteException {
        getOutPrintWriter();
        int[] uids = this.mInterface.getAppIdleWhitelist();
        return listUidList("App Idle whitelisted UIDs", uids);
    }

    private int getRestrictedModeState() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.print("Restricted mode status: ");
        pw.println(this.mInterface.isRestrictedModeEnabled() ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        return 0;
    }

    private int getRestrictBackground() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.print("Restrict background status: ");
        pw.println(this.mInterface.getRestrictBackground() ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        return 0;
    }

    private int setRestrictBackground() throws android.os.RemoteException {
        int enabled = getNextBooleanArg();
        if (enabled < 0) {
            return enabled;
        }
        this.mInterface.setRestrictBackground(enabled > 0);
        return 0;
    }

    private int setSubPlanOwner() throws android.os.RemoteException {
        int subId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String packageName = getNextArg();
        this.mInterface.setSubscriptionPlansOwner(subId, packageName);
        return 0;
    }

    private int setUidPolicy(int policy) throws android.os.RemoteException {
        int uid = getUidFromNextArg();
        if (uid < 0) {
            return uid;
        }
        this.mInterface.setUidPolicy(uid, policy);
        return 0;
    }

    private int resetUidPolicy(java.lang.String errorMessage, int expectedPolicy) throws android.os.RemoteException {
        int uid = getUidFromNextArg();
        if (uid < 0) {
            return uid;
        }
        int actualPolicy = this.mInterface.getUidPolicy(uid);
        if (actualPolicy != expectedPolicy) {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.print("Error: UID ");
            pw.print(uid);
            pw.print(' ');
            pw.println(errorMessage);
            return -1;
        }
        this.mInterface.setUidPolicy(uid, 0);
        return 0;
    }

    private int addRestrictBackgroundAllowlist() throws android.os.RemoteException {
        return setUidPolicy(4);
    }

    private int removeRestrictBackgroundAllowlist() throws android.os.RemoteException {
        return resetUidPolicy("not whitelisted", 4);
    }

    private int addRestrictBackgroundDenylist() throws android.os.RemoteException {
        return setUidPolicy(1);
    }

    private int removeRestrictBackgroundDenylist() throws android.os.RemoteException {
        return resetUidPolicy("not blacklisted", 1);
    }

    private int setAppIdleAllowlist(boolean isWhitelisted) {
        int uid = getUidFromNextArg();
        if (uid < 0) {
            return uid;
        }
        this.mInterface.setAppIdleWhitelist(uid, isWhitelisted);
        return 0;
    }

    private int addAppIdleAllowlist() throws android.os.RemoteException {
        return setAppIdleAllowlist(true);
    }

    private int removeAppIdleAllowlist() throws android.os.RemoteException {
        return setAppIdleAllowlist(false);
    }

    private int listWifiNetworks() {
        int match;
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String arg = getNextArg();
        if (arg == null) {
            match = 0;
        } else if (java.lang.Boolean.parseBoolean(arg)) {
            match = 1;
        } else {
            match = 2;
        }
        java.util.List<android.net.wifi.WifiConfiguration> configs = this.mWifiManager.getConfiguredNetworks();
        for (android.net.wifi.WifiConfiguration config : configs) {
            if (arg == null || config.meteredOverride == match) {
                pw.print(android.net.NetworkPolicyManager.resolveNetworkId(config));
                pw.print(';');
                pw.println(overrideToString(config.meteredOverride));
            }
        }
        return 0;
    }

    private int setMeteredWifiNetwork() throws android.os.RemoteException {
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String networkId = getNextArg();
        if (networkId == null) {
            pw.println("Error: didn't specify networkId");
            return -1;
        }
        java.lang.String arg = getNextArg();
        if (arg == null) {
            pw.println("Error: didn't specify meteredOverride");
            return -1;
        }
        this.mInterface.setWifiMeteredOverride(android.net.NetworkPolicyManager.resolveNetworkId(networkId), stringToOverride(arg));
        return -1;
    }

    private static java.lang.String overrideToString(int override) {
        switch (override) {
            case 1:
                return "true";
            case 2:
                return "false";
            default:
                return "none";
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x001f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int stringToOverride(java.lang.String r3) {
        /*
            int r0 = r3.hashCode()
            r1 = 0
            r2 = 1
            switch(r0) {
                case 3569038: goto L14;
                case 97196323: goto La;
                default: goto L9;
            }
        L9:
            goto L1f
        La:
            java.lang.String r0 = "false"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L9
            r0 = r2
            goto L20
        L14:
            java.lang.String r0 = "true"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L9
            r0 = r1
            goto L20
        L1f:
            r0 = -1
        L20:
            switch(r0) {
                case 0: goto L26;
                case 1: goto L24;
                default: goto L23;
            }
        L23:
            return r1
        L24:
            r0 = 2
            return r0
        L26:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.net.NetworkPolicyManagerShellCommand.stringToOverride(java.lang.String):int");
    }

    private int getNextBooleanArg() {
        java.io.PrintWriter outPrintWriter = getOutPrintWriter();
        java.lang.String nextArg = getNextArg();
        if (nextArg != null) {
            return java.lang.Boolean.valueOf(nextArg).booleanValue() ? 1 : 0;
        }
        outPrintWriter.println("Error: didn't specify BOOLEAN");
        return -1;
    }

    private int getUidFromNextArg() {
        java.io.PrintWriter pw = getOutPrintWriter();
        java.lang.String arg = getNextArg();
        if (arg == null) {
            pw.println("Error: didn't specify UID");
            return -1;
        }
        try {
            return java.lang.Integer.parseInt(arg);
        } catch (java.lang.NumberFormatException e) {
            pw.println("Error: UID (" + arg + ") should be a number");
            return -2;
        }
    }
}
