package com.android.server.net;

/* JADX INFO: loaded from: classes2.dex */
public class NetworkPolicyLogger {
    private static final int EVENT_APP_IDLE_STATE_CHANGED = 8;
    private static final int EVENT_APP_IDLE_WL_CHANGED = 14;
    private static final int EVENT_DEVICE_IDLE_MODE_ENABLED = 7;
    private static final int EVENT_FIREWALL_CHAIN_ENABLED = 12;
    private static final int EVENT_INTERFACES_CHANGED = 18;
    private static final int EVENT_METEREDNESS_CHANGED = 4;
    private static final int EVENT_METERED_ALLOWLIST_CHANGED = 15;
    private static final int EVENT_METERED_DENYLIST_CHANGED = 16;
    private static final int EVENT_NETWORK_BLOCKED = 1;
    private static final int EVENT_PAROLE_STATE_CHANGED = 9;
    private static final int EVENT_POLICIES_CHANGED = 3;
    private static final int EVENT_RESTRICT_BG_CHANGED = 6;
    private static final int EVENT_ROAMING_CHANGED = 17;
    private static final int EVENT_TEMP_POWER_SAVE_WL_CHANGED = 10;
    private static final int EVENT_TYPE_GENERIC = 0;
    private static final int EVENT_UID_FIREWALL_RULE_CHANGED = 11;
    private static final int EVENT_UID_STATE_CHANGED = 2;
    private static final int EVENT_UPDATE_METERED_RESTRICTED_PKGS = 13;
    private static final int EVENT_USER_STATE_REMOVED = 5;
    private static final int MAX_LOG_SIZE;
    private static final int MAX_NETWORK_BLOCKED_LOG_SIZE;
    static final java.lang.String TAG = "NetworkPolicy";
    static final boolean LOGD = android.util.Log.isLoggable(TAG, 3);
    static final boolean LOGV = android.util.Log.isLoggable(TAG, 2);
    private final com.android.server.net.NetworkPolicyLogger.LogBuffer mNetworkBlockedBuffer = new com.android.server.net.NetworkPolicyLogger.LogBuffer(MAX_NETWORK_BLOCKED_LOG_SIZE);
    private final com.android.server.net.NetworkPolicyLogger.LogBuffer mUidStateChangeBuffer = new com.android.server.net.NetworkPolicyLogger.LogBuffer(MAX_LOG_SIZE);
    private final com.android.server.net.NetworkPolicyLogger.LogBuffer mEventsBuffer = new com.android.server.net.NetworkPolicyLogger.LogBuffer(MAX_LOG_SIZE);
    private int mDebugUid = -1;
    private final java.lang.Object mLock = new java.lang.Object();

    static {
        MAX_LOG_SIZE = android.app.ActivityManager.isLowRamDeviceStatic() ? 100 : 400;
        MAX_NETWORK_BLOCKED_LOG_SIZE = android.app.ActivityManager.isLowRamDeviceStatic() ? 100 : 400;
    }

    void networkBlocked(int uid, com.android.server.net.NetworkPolicyManagerService.UidBlockedState uidBlockedState) {
        synchronized (this.mLock) {
            if (LOGD || uid == this.mDebugUid) {
                android.util.Slog.d(TAG, "Blocked state of " + uid + ": " + uidBlockedState);
            }
            if (uidBlockedState == null) {
                this.mNetworkBlockedBuffer.networkBlocked(uid, 0, 0, 0);
            } else {
                this.mNetworkBlockedBuffer.networkBlocked(uid, uidBlockedState.blockedReasons, uidBlockedState.allowedReasons, uidBlockedState.effectiveBlockedReasons);
            }
        }
    }

    void uidStateChanged(int uid, int procState, long procStateSeq, int capability) {
        synchronized (this.mLock) {
            if (LOGV || uid == this.mDebugUid) {
                android.util.Slog.v(TAG, uid + " state changed to " + com.android.server.am.ProcessList.makeProcStateString(procState) + ",seq=" + procStateSeq + ",cap=" + android.app.ActivityManager.getCapabilitiesSummary(capability));
            }
            this.mUidStateChangeBuffer.uidStateChanged(uid, procState, procStateSeq, capability);
        }
    }

    void event(java.lang.String msg) {
        synchronized (this.mLock) {
            if (LOGV) {
                android.util.Slog.v(TAG, msg);
            }
            this.mEventsBuffer.event(msg);
        }
    }

    void uidPolicyChanged(int uid, int oldPolicy, int newPolicy) {
        synchronized (this.mLock) {
            if (LOGV || uid == this.mDebugUid) {
                android.util.Slog.v(TAG, getPolicyChangedLog(uid, oldPolicy, newPolicy));
            }
            this.mEventsBuffer.uidPolicyChanged(uid, oldPolicy, newPolicy);
        }
    }

    void meterednessChanged(int netId, boolean newMetered) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                android.util.Slog.d(TAG, getMeterednessChangedLog(netId, newMetered));
            }
            this.mEventsBuffer.meterednessChanged(netId, newMetered);
        }
    }

    void removingUserState(int userId) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                android.util.Slog.d(TAG, getUserRemovedLog(userId));
            }
            this.mEventsBuffer.userRemoved(userId);
        }
    }

    void restrictBackgroundChanged(boolean oldValue, boolean newValue) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                android.util.Slog.d(TAG, getRestrictBackgroundChangedLog(oldValue, newValue));
            }
            this.mEventsBuffer.restrictBackgroundChanged(oldValue, newValue);
        }
    }

    void deviceIdleModeEnabled(boolean enabled) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                android.util.Slog.d(TAG, getDeviceIdleModeEnabled(enabled));
            }
            this.mEventsBuffer.deviceIdleModeEnabled(enabled);
        }
    }

    void appIdleStateChanged(int uid, boolean idle) {
        synchronized (this.mLock) {
            if (LOGD || uid == this.mDebugUid) {
                android.util.Slog.d(TAG, getAppIdleChangedLog(uid, idle));
            }
            this.mEventsBuffer.appIdleStateChanged(uid, idle);
        }
    }

    void appIdleWlChanged(int uid, boolean isWhitelisted) {
        synchronized (this.mLock) {
            if (LOGD || uid == this.mDebugUid) {
                android.util.Slog.d(TAG, getAppIdleWlChangedLog(uid, isWhitelisted));
            }
            this.mEventsBuffer.appIdleWlChanged(uid, isWhitelisted);
        }
    }

    void paroleStateChanged(boolean paroleOn) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                android.util.Slog.d(TAG, getParoleStateChanged(paroleOn));
            }
            this.mEventsBuffer.paroleStateChanged(paroleOn);
        }
    }

    void tempPowerSaveWlChanged(int appId, boolean added, int reasonCode, java.lang.String reason) {
        synchronized (this.mLock) {
            if (LOGV || appId == android.os.UserHandle.getAppId(this.mDebugUid)) {
                android.util.Slog.v(TAG, getTempPowerSaveWlChangedLog(appId, added, reasonCode, reason));
            }
            this.mEventsBuffer.tempPowerSaveWlChanged(appId, added, reasonCode, reason);
        }
    }

    void uidFirewallRuleChanged(int chain, int uid, int rule) {
        synchronized (this.mLock) {
            if (LOGV || uid == this.mDebugUid) {
                android.util.Slog.v(TAG, getUidFirewallRuleChangedLog(chain, uid, rule));
            }
            this.mEventsBuffer.uidFirewallRuleChanged(chain, uid, rule);
        }
    }

    void firewallChainEnabled(int chain, boolean enabled) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                android.util.Slog.d(TAG, getFirewallChainEnabledLog(chain, enabled));
            }
            this.mEventsBuffer.firewallChainEnabled(chain, enabled);
        }
    }

    void firewallRulesChanged(int chain, int[] uids, int[] rules) {
        synchronized (this.mLock) {
            java.lang.String log = "Firewall rules changed for " + getFirewallChainName(chain) + "; uids=" + java.util.Arrays.toString(uids) + "; rules=" + java.util.Arrays.toString(rules);
            if (LOGD || this.mDebugUid != -1) {
                android.util.Slog.d(TAG, log);
            }
            this.mEventsBuffer.event(log);
        }
    }

    void meteredRestrictedPkgsChanged(java.util.Set<java.lang.Integer> restrictedUids) {
        synchronized (this.mLock) {
            java.lang.String log = "Metered restricted uids: " + restrictedUids;
            if (LOGD || this.mDebugUid != -1) {
                android.util.Slog.d(TAG, log);
            }
            this.mEventsBuffer.event(log);
        }
    }

    void meteredAllowlistChanged(int uid, boolean added) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid == uid) {
                android.util.Slog.d(TAG, getMeteredAllowlistChangedLog(uid, added));
            }
            this.mEventsBuffer.meteredAllowlistChanged(uid, added);
        }
    }

    void meteredDenylistChanged(int uid, boolean added) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid == uid) {
                android.util.Slog.d(TAG, getMeteredDenylistChangedLog(uid, added));
            }
            this.mEventsBuffer.meteredDenylistChanged(uid, added);
        }
    }

    void roamingChanged(int netId, boolean newRoaming) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                android.util.Slog.d(TAG, getRoamingChangedLog(netId, newRoaming));
            }
            this.mEventsBuffer.roamingChanged(netId, newRoaming);
        }
    }

    void interfacesChanged(int netId, android.util.ArraySet<java.lang.String> newIfaces) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                android.util.Slog.d(TAG, getInterfacesChangedLog(netId, newIfaces.toString()));
            }
            this.mEventsBuffer.interfacesChanged(netId, newIfaces.toString());
        }
    }

    void setDebugUid(int uid) {
        this.mDebugUid = uid;
    }

    void dumpLogs(com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            pw.println();
            pw.println("mEventLogs (most recent first):");
            pw.increaseIndent();
            this.mEventsBuffer.reverseDump(pw);
            pw.decreaseIndent();
            pw.println();
            pw.println("mNetworkBlockedLogs (most recent first):");
            pw.increaseIndent();
            this.mNetworkBlockedBuffer.reverseDump(pw);
            pw.decreaseIndent();
            pw.println();
            pw.println("mUidStateChangeLogs (most recent first):");
            pw.increaseIndent();
            this.mUidStateChangeBuffer.reverseDump(pw);
            pw.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getPolicyChangedLog(int uid, int oldPolicy, int newPolicy) {
        return "Policy for " + uid + " changed from " + android.net.NetworkPolicyManager.uidPoliciesToString(oldPolicy) + " to " + android.net.NetworkPolicyManager.uidPoliciesToString(newPolicy);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getMeterednessChangedLog(int netId, boolean newMetered) {
        return "Meteredness of netId=" + netId + " changed to " + newMetered;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getUserRemovedLog(int userId) {
        return "Remove state for u" + userId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getRestrictBackgroundChangedLog(boolean oldValue, boolean newValue) {
        return "Changed restrictBackground: " + oldValue + "->" + newValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getDeviceIdleModeEnabled(boolean enabled) {
        return "DeviceIdleMode enabled: " + enabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getAppIdleChangedLog(int uid, boolean idle) {
        return "App idle state of uid " + uid + ": " + idle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getAppIdleWlChangedLog(int uid, boolean isAllowlisted) {
        return "App idle whitelist state of uid " + uid + ": " + isAllowlisted;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getParoleStateChanged(boolean paroleOn) {
        return "Parole state: " + paroleOn;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getTempPowerSaveWlChangedLog(int appId, boolean added, int reasonCode, java.lang.String reason) {
        return "temp-power-save whitelist for " + appId + " changed to: " + added + "; reason=" + android.os.PowerExemptionManager.reasonCodeToString(reasonCode) + " <" + reason + ">";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getUidFirewallRuleChangedLog(int chain, int uid, int rule) {
        return java.lang.String.format("Firewall rule changed: %d-%s-%s", java.lang.Integer.valueOf(uid), getFirewallChainName(chain), getFirewallRuleName(rule));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getFirewallChainEnabledLog(int chain, boolean enabled) {
        return "Firewall chain " + getFirewallChainName(chain) + " state: " + enabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getMeteredAllowlistChangedLog(int uid, boolean added) {
        return "metered-allowlist for " + uid + " changed to " + added;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getMeteredDenylistChangedLog(int uid, boolean added) {
        return "metered-denylist for " + uid + " changed to " + added;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getRoamingChangedLog(int netId, boolean newRoaming) {
        return "Roaming of netId=" + netId + " changed to " + newRoaming;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getInterfacesChangedLog(int netId, java.lang.String newIfaces) {
        return "Interfaces of netId=" + netId + " changed to " + newIfaces;
    }

    static java.lang.String getFirewallChainName(int chain) {
        switch (chain) {
            case 1:
                return "dozable";
            case 2:
                return "standby";
            case 3:
                return "powersave";
            case 4:
                return "restricted";
            case 5:
                return com.android.server.power.LowPowerStandbyController.DeviceConfigWrapper.NAMESPACE;
            case 6:
                return "background";
            case 7:
            case 8:
            case 9:
            default:
                return java.lang.String.valueOf(chain);
            case 10:
                return "metered_allow";
            case 11:
                return "metered_deny_user";
            case 12:
                return "metered_deny_admin";
        }
    }

    private static java.lang.String getFirewallRuleName(int rule) {
        switch (rule) {
            case 0:
                return "default";
            case 1:
                return "allow";
            case 2:
                return "deny";
            default:
                return java.lang.String.valueOf(rule);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class LogBuffer extends com.android.internal.util.RingBuffer<com.android.server.net.NetworkPolicyLogger.Data> {
        private static final java.text.SimpleDateFormat sFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS");
        private static final java.util.Date sDate = new java.util.Date();

        public static /* synthetic */ com.android.server.net.NetworkPolicyLogger.Data $r8$lambda$kDNlKKkLZ8vfiKJuYgxljbBLzHE() {
            return new com.android.server.net.NetworkPolicyLogger.Data();
        }

        public LogBuffer(int capacity) {
            super(new java.util.function.Supplier() { // from class: com.android.server.net.NetworkPolicyLogger$LogBuffer$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return com.android.server.net.NetworkPolicyLogger.LogBuffer.$r8$lambda$kDNlKKkLZ8vfiKJuYgxljbBLzHE();
                }
            }, new java.util.function.IntFunction() { // from class: com.android.server.net.NetworkPolicyLogger$LogBuffer$$ExternalSyntheticLambda1
                @Override // java.util.function.IntFunction
                public final java.lang.Object apply(int i) {
                    return com.android.server.net.NetworkPolicyLogger.LogBuffer.lambda$new$0(i);
                }
            }, capacity);
        }

        static /* synthetic */ com.android.server.net.NetworkPolicyLogger.Data[] lambda$new$0(int x$0) {
            return new com.android.server.net.NetworkPolicyLogger.Data[x$0];
        }

        public void uidStateChanged(int uid, int procState, long procStateSeq, int capability) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 2;
            data.ifield1 = uid;
            data.ifield2 = procState;
            data.ifield3 = capability;
            data.lfield1 = procStateSeq;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void event(java.lang.String msg) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 0;
            data.sfield1 = msg;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void networkBlocked(int uid, int blockedReasons, int allowedReasons, int effectiveBlockedReasons) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 1;
            data.ifield1 = uid;
            data.ifield2 = blockedReasons;
            data.ifield3 = allowedReasons;
            data.ifield4 = effectiveBlockedReasons;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void uidPolicyChanged(int uid, int oldPolicy, int newPolicy) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 3;
            data.ifield1 = uid;
            data.ifield2 = oldPolicy;
            data.ifield3 = newPolicy;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void meterednessChanged(int netId, boolean newMetered) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 4;
            data.ifield1 = netId;
            data.bfield1 = newMetered;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void userRemoved(int userId) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 5;
            data.ifield1 = userId;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void restrictBackgroundChanged(boolean oldValue, boolean newValue) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 6;
            data.bfield1 = oldValue;
            data.bfield2 = newValue;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void deviceIdleModeEnabled(boolean enabled) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 7;
            data.bfield1 = enabled;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void appIdleStateChanged(int uid, boolean idle) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 8;
            data.ifield1 = uid;
            data.bfield1 = idle;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void appIdleWlChanged(int uid, boolean isAllowlisted) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 14;
            data.ifield1 = uid;
            data.bfield1 = isAllowlisted;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void paroleStateChanged(boolean paroleOn) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 9;
            data.bfield1 = paroleOn;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void tempPowerSaveWlChanged(int appId, boolean added, int reasonCode, java.lang.String reason) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 10;
            data.ifield1 = appId;
            data.ifield2 = reasonCode;
            data.bfield1 = added;
            data.sfield1 = reason;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void uidFirewallRuleChanged(int chain, int uid, int rule) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 11;
            data.ifield1 = chain;
            data.ifield2 = uid;
            data.ifield3 = rule;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void firewallChainEnabled(int chain, boolean enabled) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 12;
            data.ifield1 = chain;
            data.bfield1 = enabled;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void meteredAllowlistChanged(int uid, boolean added) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 15;
            data.ifield1 = uid;
            data.bfield1 = added;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void meteredDenylistChanged(int uid, boolean added) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 16;
            data.ifield1 = uid;
            data.bfield1 = added;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void roamingChanged(int netId, boolean newRoaming) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 17;
            data.ifield1 = netId;
            data.bfield1 = newRoaming;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void interfacesChanged(int netId, java.lang.String newIfaces) {
            com.android.server.net.NetworkPolicyLogger.Data data = (com.android.server.net.NetworkPolicyLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 18;
            data.ifield1 = netId;
            data.sfield1 = newIfaces;
            data.timeStamp = java.lang.System.currentTimeMillis();
        }

        public void reverseDump(com.android.internal.util.IndentingPrintWriter pw) {
            com.android.server.net.NetworkPolicyLogger.Data[] allData = (com.android.server.net.NetworkPolicyLogger.Data[]) toArray();
            for (int i = allData.length - 1; i >= 0; i--) {
                if (allData[i] == null) {
                    pw.println("NULL");
                } else {
                    pw.print(formatDate(allData[i].timeStamp));
                    pw.print(" - ");
                    pw.println(getContent(allData[i]));
                }
            }
        }

        public java.lang.String getContent(com.android.server.net.NetworkPolicyLogger.Data data) {
            switch (data.type) {
                case 0:
                    return data.sfield1;
                case 1:
                    return data.ifield1 + "-" + com.android.server.net.NetworkPolicyManagerService.UidBlockedState.toString(data.ifield2, data.ifield3, data.ifield4);
                case 2:
                    return data.ifield1 + ":" + com.android.server.am.ProcessList.makeProcStateString(data.ifield2) + ":" + android.app.ActivityManager.getCapabilitiesSummary(data.ifield3) + ":" + data.lfield1;
                case 3:
                    return com.android.server.net.NetworkPolicyLogger.getPolicyChangedLog(data.ifield1, data.ifield2, data.ifield3);
                case 4:
                    return com.android.server.net.NetworkPolicyLogger.getMeterednessChangedLog(data.ifield1, data.bfield1);
                case 5:
                    return com.android.server.net.NetworkPolicyLogger.getUserRemovedLog(data.ifield1);
                case 6:
                    return com.android.server.net.NetworkPolicyLogger.getRestrictBackgroundChangedLog(data.bfield1, data.bfield2);
                case 7:
                    return com.android.server.net.NetworkPolicyLogger.getDeviceIdleModeEnabled(data.bfield1);
                case 8:
                    return com.android.server.net.NetworkPolicyLogger.getAppIdleChangedLog(data.ifield1, data.bfield1);
                case 9:
                    return com.android.server.net.NetworkPolicyLogger.getParoleStateChanged(data.bfield1);
                case 10:
                    return com.android.server.net.NetworkPolicyLogger.getTempPowerSaveWlChangedLog(data.ifield1, data.bfield1, data.ifield2, data.sfield1);
                case 11:
                    return com.android.server.net.NetworkPolicyLogger.getUidFirewallRuleChangedLog(data.ifield1, data.ifield2, data.ifield3);
                case 12:
                    return com.android.server.net.NetworkPolicyLogger.getFirewallChainEnabledLog(data.ifield1, data.bfield1);
                case 13:
                default:
                    return java.lang.String.valueOf(data.type);
                case 14:
                    return com.android.server.net.NetworkPolicyLogger.getAppIdleWlChangedLog(data.ifield1, data.bfield1);
                case 15:
                    return com.android.server.net.NetworkPolicyLogger.getMeteredAllowlistChangedLog(data.ifield1, data.bfield1);
                case 16:
                    return com.android.server.net.NetworkPolicyLogger.getMeteredDenylistChangedLog(data.ifield1, data.bfield1);
                case 17:
                    return com.android.server.net.NetworkPolicyLogger.getRoamingChangedLog(data.ifield1, data.bfield1);
                case 18:
                    return com.android.server.net.NetworkPolicyLogger.getInterfacesChangedLog(data.ifield1, data.sfield1);
            }
        }

        private java.lang.String formatDate(long millis) {
            sDate.setTime(millis);
            return sFormatter.format(sDate);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class Data {
        public boolean bfield1;
        public boolean bfield2;
        public int ifield1;
        public int ifield2;
        public int ifield3;
        public int ifield4;
        public long lfield1;
        public java.lang.String sfield1;
        public long timeStamp;
        public int type;

        private Data() {
        }

        public void reset() {
            this.sfield1 = null;
        }
    }
}
