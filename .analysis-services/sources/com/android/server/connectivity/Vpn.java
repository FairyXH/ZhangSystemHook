package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public class Vpn {
    private static final java.lang.String ANDROID_KEYSTORE_PROVIDER = "AndroidKeyStore";
    public static final int AUTOMATIC_KEEPALIVE_DELAY_SECONDS = 30;
    static final int DEFAULT_LONG_LIVED_TCP_CONNS_EXPENSIVE_TIMEOUT_SEC = 60;
    static final int DEFAULT_UDP_PORT_4500_NAT_TIMEOUT_SEC_INT = 300;
    private static final long IKE_DELAY_ON_NC_LP_CHANGE_MS = 300;
    private static final java.lang.String LOCKDOWN_ALLOWLIST_SETTING_NAME = "always_on_vpn_lockdown_whitelist";
    private static final boolean LOGD = true;
    private static final int MAX_EVENTS_LOGS = 100;
    private static final int MAX_MOBIKE_RECOVERY_ATTEMPT = 2;
    static final int MAX_VPN_PROFILE_SIZE_BYTES = 131072;
    private static final java.lang.String NETWORKTYPE = "VPN";
    public static final int PREFERRED_IKE_PROTOCOL_AUTO = 0;
    public static final int PREFERRED_IKE_PROTOCOL_IPV4_UDP = 40;
    public static final int PREFERRED_IKE_PROTOCOL_IPV6_ESP = 61;
    public static final int PREFERRED_IKE_PROTOCOL_IPV6_UDP = 60;
    private static final int PREFERRED_IKE_PROTOCOL_UNKNOWN = -1;
    private static final long RETRY_DELAY_AUTO_BACKOFF = -1;
    private static final int STARTING_TOKEN = -1;
    private static final java.lang.String TAG = "Vpn";
    static final java.lang.String VPN_APP_EXCLUDED = "VPNAPPEXCLUDED_";
    private static final int VPN_DEFAULT_SCORE = 101;
    private static final long VPN_LAUNCH_IDLE_ALLOWLIST_DURATION_MS = 60000;
    private static final long VPN_MANAGER_EVENT_ALLOWLIST_DURATION_MS = 30000;
    private static final java.lang.String VPN_PROVIDER_NAME_BASE = "VpnNetworkProvider:";
    protected boolean mAlwaysOn;
    private final android.app.AppOpsManager mAppOpsManager;
    private final java.util.Set<android.net.UidRangeParcel> mBlockedUidsAsToldToConnectivity;
    private final android.util.SparseArray<com.android.server.connectivity.Vpn.CarrierConfigInfo> mCachedCarrierConfigInfoPerSubId;
    private final android.telephony.CarrierConfigManager mCarrierConfigManager;
    protected com.android.internal.net.VpnConfig mConfig;
    private com.android.server.connectivity.Vpn.Connection mConnection;
    private final android.net.ConnectivityDiagnosticsManager mConnectivityDiagnosticsManager;
    private final android.net.ConnectivityManager mConnectivityManager;
    private final android.content.Context mContext;
    final com.android.server.connectivity.Vpn.Dependencies mDeps;
    private volatile boolean mEnableTeardown;
    private final android.util.LocalLog mEventChanges;
    private final com.android.server.connectivity.Vpn.Ikev2SessionCreator mIkev2SessionCreator;
    protected java.lang.String mInterface;
    private boolean mIsPackageTargetingAtLeastQ;
    private int mLegacyState;
    protected boolean mLockdown;
    private java.util.List<java.lang.String> mLockdownAllowlist;
    private final android.os.Looper mLooper;
    private final android.net.INetd mNetd;
    protected android.net.NetworkAgent mNetworkAgent;
    protected android.net.NetworkCapabilities mNetworkCapabilities;
    private final android.net.NetworkInfo mNetworkInfo;
    private final android.net.NetworkProvider mNetworkProvider;
    private android.net.INetworkManagementEventObserver mObserver;
    private int mOwnerUID;
    protected java.lang.String mPackage;
    private android.app.PendingIntent mStatusIntent;
    private final android.telephony.SubscriptionManager mSubscriptionManager;
    private final com.android.server.connectivity.Vpn.SystemServices mSystemServices;
    private final android.telephony.TelephonyManager mTelephonyManager;
    private final int mUserId;
    private final android.content.Context mUserIdContext;
    private final android.os.UserManager mUserManager;
    com.android.server.connectivity.IVpnExt mVpnExt;
    private final com.android.server.connectivity.VpnProfileStore mVpnProfileStore;
    protected com.android.server.connectivity.Vpn.VpnRunner mVpnRunner;
    private static final long[] IKEV2_VPN_RETRY_DELAYS_MS = {1000, 2000, 5000, 30000, 60000, 300000, 900000};
    private static final long[] DATA_STALL_RECOVERY_DELAYS_MS = {1000, 5000, 30000, 60000, 120000, 240000, 480000, 960000};

    interface IkeV2VpnRunnerCallback {
        void onChildMigrated(int i, android.net.IpSecTransform ipSecTransform, android.net.IpSecTransform ipSecTransform2);

        void onChildOpened(int i, android.net.ipsec.ike.ChildSessionConfiguration childSessionConfiguration);

        void onChildTransformCreated(int i, android.net.IpSecTransform ipSecTransform, int i2);

        void onDefaultNetworkCapabilitiesChanged(android.net.NetworkCapabilities networkCapabilities);

        void onDefaultNetworkChanged(android.net.Network network);

        void onDefaultNetworkLinkPropertiesChanged(android.net.LinkProperties linkProperties);

        void onDefaultNetworkLost(android.net.Network network);

        void onIkeConnectionInfoChanged(int i, android.net.ipsec.ike.IkeSessionConnectionInfo ikeSessionConnectionInfo);

        void onIkeOpened(int i, android.net.ipsec.ike.IkeSessionConfiguration ikeSessionConfiguration);

        void onSessionLost(int i, java.lang.Exception exc);
    }

    interface ValidationStatusCallback {
        void onValidationStatus(int i);
    }

    private native boolean jniAddAddress(java.lang.String str, java.lang.String str2, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public native int jniCheck(java.lang.String str);

    /* JADX INFO: Access modifiers changed from: private */
    public native int jniCreate(int i);

    private native boolean jniDelAddress(java.lang.String str, java.lang.String str2, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public native java.lang.String jniGetName(int i);

    private native void jniReset(java.lang.String str);

    /* JADX INFO: Access modifiers changed from: private */
    public native int jniSetAddresses(java.lang.String str, java.lang.String str2);

    com.android.server.connectivity.VpnProfileStore getVpnProfileStore() {
        return this.mVpnProfileStore;
    }

    private static class CarrierConfigInfo {
        public final int encapType;
        public final int ipVersion;
        public final int keepaliveDelaySec;
        public final java.lang.String mccMnc;

        CarrierConfigInfo(java.lang.String mccMnc, int keepaliveDelaySec, int encapType, int ipVersion) {
            this.mccMnc = mccMnc;
            this.keepaliveDelaySec = keepaliveDelaySec;
            this.encapType = encapType;
            this.ipVersion = ipVersion;
        }

        public java.lang.String toString() {
            return "CarrierConfigInfo(" + this.mccMnc + ") [keepaliveDelaySec=" + this.keepaliveDelaySec + ", encapType=" + this.encapType + ", ipVersion=" + this.ipVersion + "]";
        }
    }

    public static class Dependencies {
        public boolean isCallerSystem() {
            return android.os.Binder.getCallingUid() == 1000;
        }

        public com.android.server.DeviceIdleInternal getDeviceIdleInternal() {
            return (com.android.server.DeviceIdleInternal) com.android.server.LocalServices.getService(com.android.server.DeviceIdleInternal.class);
        }

        public android.app.PendingIntent getIntentForStatusPanel(android.content.Context context) {
            return com.android.internal.net.VpnConfig.getIntentForStatusPanel(context);
        }

        public android.os.ParcelFileDescriptor adoptFd(com.android.server.connectivity.Vpn vpn, int mtu) {
            return android.os.ParcelFileDescriptor.adoptFd(jniCreate(vpn, mtu));
        }

        public int jniCreate(com.android.server.connectivity.Vpn vpn, int mtu) {
            return vpn.jniCreate(mtu);
        }

        public java.lang.String jniGetName(com.android.server.connectivity.Vpn vpn, int fd) {
            return vpn.jniGetName(fd);
        }

        public int jniSetAddresses(com.android.server.connectivity.Vpn vpn, java.lang.String interfaze, java.lang.String addresses) {
            return vpn.jniSetAddresses(interfaze, addresses);
        }

        public void setBlocking(java.io.FileDescriptor fd, boolean blocking) {
            try {
                libcore.io.IoUtils.setBlocking(fd, blocking);
            } catch (java.io.IOException e) {
                throw new java.lang.IllegalStateException("Cannot set tunnel's fd as blocking=" + blocking, e);
            }
        }

        public long getNextRetryDelayMs(int retryCount) {
            if (retryCount >= com.android.server.connectivity.Vpn.IKEV2_VPN_RETRY_DELAYS_MS.length) {
                return com.android.server.connectivity.Vpn.IKEV2_VPN_RETRY_DELAYS_MS[com.android.server.connectivity.Vpn.IKEV2_VPN_RETRY_DELAYS_MS.length - 1];
            }
            return com.android.server.connectivity.Vpn.IKEV2_VPN_RETRY_DELAYS_MS[retryCount];
        }

        public java.util.concurrent.ScheduledThreadPoolExecutor newScheduledThreadPoolExecutor() {
            return new java.util.concurrent.ScheduledThreadPoolExecutor(1);
        }

        public android.net.NetworkAgent newNetworkAgent(android.content.Context context, android.os.Looper looper, java.lang.String logTag, android.net.NetworkCapabilities nc, android.net.LinkProperties lp, android.net.NetworkScore score, android.net.NetworkAgentConfig config, android.net.NetworkProvider provider, com.android.server.connectivity.Vpn.ValidationStatusCallback callback) {
            return new com.android.server.connectivity.Vpn.VpnNetworkAgentWrapper(context, looper, logTag, nc, lp, score, config, provider, callback);
        }

        public long getValidationFailRecoveryMs(int count) {
            if (count >= com.android.server.connectivity.Vpn.DATA_STALL_RECOVERY_DELAYS_MS.length) {
                return com.android.server.connectivity.Vpn.DATA_STALL_RECOVERY_DELAYS_MS[com.android.server.connectivity.Vpn.DATA_STALL_RECOVERY_DELAYS_MS.length - 1];
            }
            return com.android.server.connectivity.Vpn.DATA_STALL_RECOVERY_DELAYS_MS[count];
        }

        public int getJavaNetworkInterfaceMtu(java.lang.String iface, int defaultValue) throws java.net.SocketException {
            java.net.NetworkInterface networkInterface;
            if (iface != null && (networkInterface = java.net.NetworkInterface.getByName(iface)) != null) {
                return networkInterface.getMTU();
            }
            return defaultValue;
        }

        public int calculateVpnMtu(java.util.List<android.net.ipsec.ike.ChildSaProposal> childProposals, int maxMtu, int underlyingMtu, boolean isIpv4) {
            return com.android.server.vcn.util.MtuUtils.getMtu(childProposals, maxMtu, underlyingMtu, isIpv4);
        }

        public void verifyCallingUidAndPackage(android.content.Context context, java.lang.String packageName, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            if (com.android.server.connectivity.Vpn.getAppUid(context, packageName, userId) != callingUid) {
                throw new java.lang.SecurityException(packageName + " does not belong to uid " + callingUid);
            }
        }
    }

    public Vpn(android.os.Looper looper, android.content.Context context, android.os.INetworkManagementService netService, android.net.INetd netd, int userId, com.android.server.connectivity.VpnProfileStore vpnProfileStore) {
        this(looper, context, new com.android.server.connectivity.Vpn.Dependencies(), netService, netd, userId, vpnProfileStore, new com.android.server.connectivity.Vpn.SystemServices(context), new com.android.server.connectivity.Vpn.Ikev2SessionCreator());
    }

    public Vpn(android.os.Looper looper, android.content.Context context, com.android.server.connectivity.Vpn.Dependencies deps, android.os.INetworkManagementService netService, android.net.INetd netd, int userId, com.android.server.connectivity.VpnProfileStore vpnProfileStore) {
        this(looper, context, deps, netService, netd, userId, vpnProfileStore, new com.android.server.connectivity.Vpn.SystemServices(context), new com.android.server.connectivity.Vpn.Ikev2SessionCreator());
    }

    protected Vpn(android.os.Looper looper, android.content.Context context, com.android.server.connectivity.Vpn.Dependencies deps, android.os.INetworkManagementService netService, android.net.INetd netd, int userId, com.android.server.connectivity.VpnProfileStore vpnProfileStore, com.android.server.connectivity.Vpn.SystemServices systemServices, com.android.server.connectivity.Vpn.Ikev2SessionCreator ikev2SessionCreator) {
        this.mEnableTeardown = true;
        this.mEventChanges = new android.util.LocalLog(100);
        this.mCachedCarrierConfigInfoPerSubId = new android.util.SparseArray<>();
        this.mAlwaysOn = false;
        this.mLockdown = false;
        this.mLockdownAllowlist = java.util.Collections.emptyList();
        this.mBlockedUidsAsToldToConnectivity = new android.util.ArraySet();
        this.mObserver = new com.android.server.net.BaseNetworkObserver() { // from class: com.android.server.connectivity.Vpn.1
            public void interfaceRemoved(java.lang.String interfaze) {
                synchronized (com.android.server.connectivity.Vpn.this) {
                    if (interfaze.equals(com.android.server.connectivity.Vpn.this.mInterface) && com.android.server.connectivity.Vpn.this.jniCheck(interfaze) == 0) {
                        if (com.android.server.connectivity.Vpn.this.mConnection != null) {
                            com.android.server.connectivity.Vpn.this.mAppOpsManager.finishOp("android:establish_vpn_service", com.android.server.connectivity.Vpn.this.mOwnerUID, com.android.server.connectivity.Vpn.this.mPackage, null);
                            com.android.server.connectivity.Vpn.this.mContext.unbindService(com.android.server.connectivity.Vpn.this.mConnection);
                            com.android.server.connectivity.Vpn.this.cleanupVpnStateLocked();
                        } else if (com.android.server.connectivity.Vpn.this.mVpnRunner != null) {
                            if (!"[Legacy VPN]".equals(com.android.server.connectivity.Vpn.this.mPackage)) {
                                com.android.server.connectivity.Vpn.this.mAppOpsManager.finishOp("android:establish_vpn_manager", com.android.server.connectivity.Vpn.this.mOwnerUID, com.android.server.connectivity.Vpn.this.mPackage, null);
                            }
                            com.android.server.connectivity.Vpn.this.mVpnRunner.exit();
                        }
                    }
                }
            }
        };
        this.mVpnProfileStore = vpnProfileStore;
        this.mContext = context;
        this.mConnectivityManager = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
        this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        this.mUserIdContext = context.createContextAsUser(android.os.UserHandle.of(userId), 0);
        this.mConnectivityDiagnosticsManager = (android.net.ConnectivityDiagnosticsManager) this.mContext.getSystemService(android.net.ConnectivityDiagnosticsManager.class);
        this.mCarrierConfigManager = (android.telephony.CarrierConfigManager) this.mContext.getSystemService(android.telephony.CarrierConfigManager.class);
        this.mTelephonyManager = (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class);
        this.mSubscriptionManager = (android.telephony.SubscriptionManager) this.mContext.getSystemService(android.telephony.SubscriptionManager.class);
        this.mDeps = deps;
        this.mNetd = netd;
        this.mUserId = userId;
        this.mLooper = looper;
        this.mSystemServices = systemServices;
        this.mIkev2SessionCreator = ikev2SessionCreator;
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        this.mPackage = "[Legacy VPN]";
        this.mOwnerUID = getAppUid(this.mContext, this.mPackage, this.mUserId);
        this.mIsPackageTargetingAtLeastQ = doesPackageTargetAtLeastQ(this.mPackage);
        try {
            netService.registerObserver(this.mObserver);
        } catch (android.os.RemoteException e) {
            android.util.Log.wtf(TAG, "Problem registering observer", e);
        }
        this.mNetworkProvider = new android.net.NetworkProvider(context, looper, VPN_PROVIDER_NAME_BASE + this.mUserId);
        this.mConnectivityManager.registerNetworkProvider(this.mNetworkProvider);
        this.mLegacyState = 0;
        this.mNetworkInfo = new android.net.NetworkInfo(17, 0, NETWORKTYPE, "");
        this.mNetworkCapabilities = new android.net.NetworkCapabilities.Builder().addTransportType(4).removeCapability(15).addCapability(28).setTransportInfo(new android.net.VpnTransportInfo(-1, (java.lang.String) null, false, false)).build();
        loadAlwaysOnPackage();
        try {
            this.mVpnExt = (com.android.server.connectivity.IVpnExt) system.ext.loader.core.ExtLoader.type(com.android.server.connectivity.IVpnExt.class).base((java.lang.Object) null).create();
            this.mVpnExt.init(this.mContext);
        } catch (java.lang.Exception e2) {
            this.mVpnExt = null;
            android.util.Log.d(TAG, "mVpnExt init error" + e2.toString());
        }
    }

    public void setEnableTeardown(boolean enableTeardown) {
        this.mEnableTeardown = enableTeardown;
    }

    public boolean getEnableTeardown() {
        return this.mEnableTeardown;
    }

    protected void updateState(android.net.NetworkInfo.DetailedState detailedState, java.lang.String reason) {
        android.util.Log.d(TAG, "setting state=" + detailedState + ", reason=" + reason);
        this.mLegacyState = com.android.internal.net.LegacyVpnInfo.stateFromNetworkInfo(detailedState);
        this.mNetworkInfo.setDetailedState(detailedState, reason, null);
        switch (com.android.server.connectivity.Vpn.AnonymousClass2.$SwitchMap$android$net$NetworkInfo$DetailedState[detailedState.ordinal()]) {
            case 1:
                if (this.mNetworkAgent != null) {
                    this.mNetworkAgent.markConnected();
                }
                break;
            case 2:
            case 3:
                if (this.mNetworkAgent != null) {
                    this.mNetworkAgent.unregister();
                    this.mNetworkAgent = null;
                }
                break;
            case 4:
                if (this.mNetworkAgent != null) {
                    throw new java.lang.IllegalStateException("VPN can only go to CONNECTING state when the agent is null.");
                }
                break;
            default:
                throw new java.lang.IllegalArgumentException("Illegal state argument " + detailedState);
        }
        updateAlwaysOnNotification(detailedState);
    }

    /* JADX INFO: renamed from: com.android.server.connectivity.Vpn$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$android$net$NetworkInfo$DetailedState = new int[android.net.NetworkInfo.DetailedState.values().length];

        static {
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[android.net.NetworkInfo.DetailedState.CONNECTED.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[android.net.NetworkInfo.DetailedState.DISCONNECTED.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[android.net.NetworkInfo.DetailedState.FAILED.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[android.net.NetworkInfo.DetailedState.CONNECTING.ordinal()] = 4;
            } catch (java.lang.NoSuchFieldError e4) {
            }
        }
    }

    private void resetNetworkCapabilities() {
        this.mNetworkCapabilities = new android.net.NetworkCapabilities.Builder(this.mNetworkCapabilities).setUids((java.util.Set) null).setTransportInfo(new android.net.VpnTransportInfo(-1, (java.lang.String) null, false, false)).build();
    }

    public synchronized void setLockdown(boolean lockdown) {
        enforceControlPermissionOrInternalCaller();
        setVpnForcedLocked(lockdown);
        this.mLockdown = lockdown;
        if (this.mAlwaysOn) {
            saveAlwaysOnPackage();
        }
    }

    public synchronized java.lang.String getPackage() {
        return this.mPackage;
    }

    public synchronized boolean getLockdown() {
        return this.mLockdown;
    }

    public synchronized boolean getAlwaysOn() {
        return this.mAlwaysOn;
    }

    public boolean isAlwaysOnPackageSupported(java.lang.String packageName) {
        enforceSettingsPermission();
        if (packageName == null) {
            return false;
        }
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            if (getVpnProfilePrivileged(packageName) != null) {
                return true;
            }
            android.os.Binder.restoreCallingIdentity(oldId);
            android.content.pm.PackageManager pm = this.mContext.getPackageManager();
            android.content.pm.ApplicationInfo appInfo = null;
            try {
                appInfo = pm.getApplicationInfoAsUser(packageName, 0, this.mUserId);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.w(TAG, "Can't find \"" + packageName + "\" when checking always-on support");
            }
            if (appInfo == null || appInfo.targetSdkVersion < 24) {
                return false;
            }
            android.content.Intent intent = new android.content.Intent("android.net.VpnService");
            intent.setPackage(packageName);
            java.util.List<android.content.pm.ResolveInfo> services = pm.queryIntentServicesAsUser(intent, 128, this.mUserId);
            if (services == null || services.size() == 0) {
                return false;
            }
            for (android.content.pm.ResolveInfo rInfo : services) {
                android.os.Bundle metaData = rInfo.serviceInfo.metaData;
                if (metaData != null && !metaData.getBoolean("android.net.VpnService.SUPPORTS_ALWAYS_ON", true)) {
                    return false;
                }
            }
            return true;
        } finally {
            android.os.Binder.restoreCallingIdentity(oldId);
        }
    }

    private android.content.Intent buildVpnManagerEventIntent(java.lang.String category, int errorClass, int errorCode, java.lang.String packageName, java.lang.String sessionKey, android.net.VpnProfileState profileState, android.net.Network underlyingNetwork, android.net.NetworkCapabilities nc, android.net.LinkProperties lp) {
        android.util.Log.d(TAG, "buildVpnManagerEventIntent: sessionKey = " + sessionKey);
        android.content.Intent intent = new android.content.Intent("android.net.action.VPN_MANAGER_EVENT");
        intent.setPackage(packageName);
        intent.addCategory(category);
        intent.putExtra("android.net.extra.VPN_PROFILE_STATE", profileState);
        intent.putExtra("android.net.extra.SESSION_KEY", sessionKey);
        intent.putExtra("android.net.extra.UNDERLYING_NETWORK", underlyingNetwork);
        intent.putExtra("android.net.extra.UNDERLYING_NETWORK_CAPABILITIES", nc);
        intent.putExtra("android.net.extra.UNDERLYING_LINK_PROPERTIES", lp);
        intent.putExtra("android.net.extra.TIMESTAMP_MILLIS", java.lang.System.currentTimeMillis());
        if (!"android.net.category.EVENT_DEACTIVATED_BY_USER".equals(category) || !"android.net.category.EVENT_ALWAYS_ON_STATE_CHANGED".equals(category)) {
            intent.putExtra("android.net.extra.ERROR_CLASS", errorClass);
            intent.putExtra("android.net.extra.ERROR_CODE", errorCode);
        }
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean sendEventToVpnManagerApp(java.lang.String category, int errorClass, int errorCode, java.lang.String packageName, java.lang.String sessionKey, android.net.VpnProfileState profileState, android.net.Network underlyingNetwork, android.net.NetworkCapabilities nc, android.net.LinkProperties lp) {
        this.mEventChanges.log("[VMEvent] Event class=" + getVpnManagerEventClassName(errorClass) + ", err=" + getVpnManagerEventErrorName(errorCode) + " for " + packageName + " on session " + sessionKey);
        android.content.Intent intent = buildVpnManagerEventIntent(category, errorClass, errorCode, packageName, sessionKey, profileState, underlyingNetwork, nc, lp);
        return sendEventToVpnManagerApp(intent, packageName);
    }

    private boolean sendEventToVpnManagerApp(android.content.Intent intent, java.lang.String packageName) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.DeviceIdleInternal idleController = this.mDeps.getDeviceIdleInternal();
            idleController.addPowerSaveTempWhitelistApp(android.os.Process.myUid(), packageName, 30000L, this.mUserId, false, 309, "VpnManager event");
            return this.mUserIdContext.startService(intent) != null;
        } catch (java.lang.RuntimeException e) {
            android.util.Log.e(TAG, "Service of VpnManager app " + intent + " failed to start", e);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isVpnApp(java.lang.String packageName) {
        return (packageName == null || "[Legacy VPN]".equals(packageName)) ? false : true;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0042 A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0044 A[Catch: all -> 0x007f, TRY_ENTER, TryCatch #0 {, blocks: (B:4:0x0003, B:8:0x0015, B:10:0x001c, B:12:0x0020, B:18:0x002d, B:23:0x003a, B:27:0x0044, B:31:0x004d, B:33:0x0058, B:32:0x0053, B:35:0x0067), top: B:41:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized boolean setAlwaysOnPackage(java.lang.String r19, boolean r20, java.util.List<java.lang.String> r21) {
        /*
            r18 = this;
            r11 = r18
            monitor-enter(r18)
            r18.enforceControlPermissionOrInternalCaller()     // Catch: java.lang.Throwable -> L7f
            java.lang.String r0 = r11.mPackage     // Catch: java.lang.Throwable -> L7f
            r12 = r19
            boolean r1 = java.util.Objects.equals(r12, r0)     // Catch: java.lang.Throwable -> L7f
            r13 = 1
            r2 = 0
            if (r1 != 0) goto L14
            r1 = r13
            goto L15
        L14:
            r1 = r2
        L15:
            r14 = r1
            boolean r1 = isVpnApp(r0)     // Catch: java.lang.Throwable -> L7f
            if (r1 == 0) goto L2a
            boolean r1 = r11.mAlwaysOn     // Catch: java.lang.Throwable -> L7f
            if (r1 == 0) goto L2a
            boolean r1 = r11.mLockdown     // Catch: java.lang.Throwable -> L7f
            r15 = r20
            if (r15 != r1) goto L28
            if (r14 == 0) goto L2c
        L28:
            r1 = r13
            goto L2d
        L2a:
            r15 = r20
        L2c:
            r1 = r2
        L2d:
            r16 = r1
            boolean r1 = isVpnApp(r19)     // Catch: java.lang.Throwable -> L7f
            if (r1 == 0) goto L39
            if (r14 == 0) goto L39
            r1 = r13
            goto L3a
        L39:
            r1 = r2
        L3a:
            r17 = r1
            boolean r1 = r18.setAlwaysOnPackageInternal(r19, r20, r21)     // Catch: java.lang.Throwable -> L7f
            if (r1 != 0) goto L44
            monitor-exit(r18)
            return r2
        L44:
            r18.saveAlwaysOnPackage()     // Catch: java.lang.Throwable -> L7f
            if (r16 == 0) goto L65
            java.lang.String r2 = "android.net.category.EVENT_ALWAYS_ON_STATE_CHANGED"
            if (r14 == 0) goto L53
            android.net.VpnProfileState r1 = r18.makeDisconnectedVpnProfileState()     // Catch: java.lang.Throwable -> L7f
            r7 = r1
            goto L58
        L53:
            android.net.VpnProfileState r1 = r18.makeVpnProfileStateLocked()     // Catch: java.lang.Throwable -> L7f
            r7 = r1
        L58:
            r3 = -1
            r4 = -1
            r6 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r1 = r18
            r5 = r0
            r1.sendEventToVpnManagerApp(r2, r3, r4, r5, r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> L7f
        L65:
            if (r17 == 0) goto L7d
            java.lang.String r2 = "android.net.category.EVENT_ALWAYS_ON_STATE_CHANGED"
            java.lang.String r6 = r18.getSessionKeyLocked()     // Catch: java.lang.Throwable -> L7f
            android.net.VpnProfileState r7 = r18.makeVpnProfileStateLocked()     // Catch: java.lang.Throwable -> L7f
            r3 = -1
            r4 = -1
            r8 = 0
            r9 = 0
            r10 = 0
            r1 = r18
            r5 = r19
            r1.sendEventToVpnManagerApp(r2, r3, r4, r5, r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> L7f
        L7d:
            monitor-exit(r18)
            return r13
        L7f:
            r0 = move-exception
            monitor-exit(r18)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.connectivity.Vpn.setAlwaysOnPackage(java.lang.String, boolean, java.util.List):boolean");
    }

    private boolean setAlwaysOnPackageInternal(java.lang.String packageName, boolean lockdown, java.util.List<java.lang.String> lockdownAllowlist) {
        java.util.List<java.lang.String> listEmptyList;
        boolean z = false;
        if ("[Legacy VPN]".equals(packageName)) {
            android.util.Log.w(TAG, "Not setting legacy VPN \"" + packageName + "\" as always-on.");
            return false;
        }
        if (lockdownAllowlist != null) {
            for (java.lang.String pkg : lockdownAllowlist) {
                if (pkg.contains(",")) {
                    android.util.Log.w(TAG, "Not setting always-on vpn, invalid allowed package: " + pkg);
                    return false;
                }
            }
        }
        if (packageName != null) {
            long oldId = android.os.Binder.clearCallingIdentity();
            try {
                com.android.internal.net.VpnProfile profile = getVpnProfilePrivileged(packageName);
                int grantType = profile == null ? 1 : 2;
                if (!setPackageAuthorization(packageName, grantType)) {
                    return false;
                }
                this.mAlwaysOn = true;
            } finally {
                android.os.Binder.restoreCallingIdentity(oldId);
            }
        } else {
            packageName = "[Legacy VPN]";
            this.mAlwaysOn = false;
        }
        boolean oldLockdownState = this.mLockdown;
        if (this.mAlwaysOn && lockdown) {
            z = true;
        }
        this.mLockdown = z;
        if (this.mLockdown && lockdownAllowlist != null) {
            listEmptyList = java.util.Collections.unmodifiableList(new java.util.ArrayList(lockdownAllowlist));
        } else {
            listEmptyList = java.util.Collections.emptyList();
        }
        this.mLockdownAllowlist = listEmptyList;
        this.mEventChanges.log("[LockdownAlwaysOn] Mode changed: lockdown=" + this.mLockdown + " alwaysOn=" + this.mAlwaysOn + " calling from " + android.os.Binder.getCallingUid());
        if (isCurrentPreparedPackage(packageName)) {
            updateAlwaysOnNotification(this.mNetworkInfo.getDetailedState());
            setVpnForcedLocked(this.mLockdown);
            if (this.mNetworkAgent != null && oldLockdownState != this.mLockdown) {
                startNewNetworkAgent(this.mNetworkAgent, "Lockdown mode changed");
            }
        } else {
            prepareInternal(packageName);
        }
        return true;
    }

    private static boolean isNullOrLegacyVpn(java.lang.String packageName) {
        return packageName == null || "[Legacy VPN]".equals(packageName);
    }

    public synchronized java.lang.String getAlwaysOnPackage() {
        enforceControlPermissionOrInternalCaller();
        return this.mAlwaysOn ? this.mPackage : null;
    }

    public synchronized java.util.List<java.lang.String> getLockdownAllowlist() {
        return this.mLockdown ? this.mLockdownAllowlist : null;
    }

    private void saveAlwaysOnPackage() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mSystemServices.settingsSecurePutStringForUser("always_on_vpn_app", getAlwaysOnPackage(), this.mUserId);
            this.mSystemServices.settingsSecurePutIntForUser("always_on_vpn_lockdown", (this.mAlwaysOn && this.mLockdown) ? 1 : 0, this.mUserId);
            this.mSystemServices.settingsSecurePutStringForUser(LOCKDOWN_ALLOWLIST_SETTING_NAME, java.lang.String.join(",", this.mLockdownAllowlist), this.mUserId);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void loadAlwaysOnPackage() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.String alwaysOnPackage = this.mSystemServices.settingsSecureGetStringForUser("always_on_vpn_app", this.mUserId);
            boolean alwaysOnLockdown = this.mSystemServices.settingsSecureGetIntForUser("always_on_vpn_lockdown", 0, this.mUserId) != 0;
            java.lang.String allowlistString = this.mSystemServices.settingsSecureGetStringForUser(LOCKDOWN_ALLOWLIST_SETTING_NAME, this.mUserId);
            java.util.List<java.lang.String> allowedPackages = android.text.TextUtils.isEmpty(allowlistString) ? java.util.Collections.emptyList() : java.util.Arrays.asList(allowlistString.split(","));
            setAlwaysOnPackageInternal(alwaysOnPackage, alwaysOnLockdown, allowedPackages);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean startAlwaysOnVpn() {
        synchronized (this) {
            java.lang.String alwaysOnPackage = getAlwaysOnPackage();
            if (alwaysOnPackage == null) {
                return true;
            }
            if (!isAlwaysOnPackageSupported(alwaysOnPackage)) {
                setAlwaysOnPackage(null, false, null);
                return false;
            }
            if (getNetworkInfo().isConnected()) {
                return true;
            }
            long oldId = android.os.Binder.clearCallingIdentity();
            try {
                com.android.internal.net.VpnProfile profile = getVpnProfilePrivileged(alwaysOnPackage);
                if (profile != null) {
                    startVpnProfilePrivileged(profile, alwaysOnPackage);
                    return true;
                }
                com.android.server.DeviceIdleInternal idleController = this.mDeps.getDeviceIdleInternal();
                idleController.addPowerSaveTempWhitelistApp(android.os.Process.myUid(), alwaysOnPackage, 60000L, this.mUserId, false, 309, com.android.server.am.IOplusSceneManager.APP_SCENE_VPN);
                android.content.Intent serviceIntent = new android.content.Intent("android.net.VpnService");
                serviceIntent.setPackage(alwaysOnPackage);
                try {
                    return this.mUserIdContext.startService(serviceIntent) != null;
                } catch (java.lang.RuntimeException e) {
                    android.util.Log.e(TAG, "VpnService " + serviceIntent + " failed to start", e);
                    return false;
                }
            } catch (java.lang.Exception e2) {
                android.util.Log.e(TAG, "Error starting always-on VPN", e2);
                return false;
            } finally {
                android.os.Binder.restoreCallingIdentity(oldId);
            }
        }
    }

    public synchronized boolean prepare(java.lang.String oldPackage, java.lang.String newPackage, int vpnType) {
        if (doesHaveVPNAppWhiteList()) {
            if (newPackage != null) {
                if (!isInVPNAppWhiteList(newPackage)) {
                    android.util.Log.d(TAG, "prepareVpn, the VPN can't be estabilshed as the app is not in the white list");
                    return false;
                }
            } else if (!isInVPNAppWhiteList(oldPackage)) {
                android.util.Log.d(TAG, "prepareVpn, the VPN can't be estabilshed as the app is not in the white list");
                return false;
            }
        } else if (isVpnDisabled()) {
            android.util.Log.d(TAG, "prepareVpn, the VPN can't be estabilshed as the switch disable");
            return false;
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.CONTROL_VPN") != 0) {
            if (oldPackage != null) {
                verifyCallingUidAndPackage(oldPackage);
            }
            if (newPackage != null) {
                verifyCallingUidAndPackage(newPackage);
            }
        }
        if (oldPackage != null) {
            if (this.mAlwaysOn && !isCurrentPreparedPackage(oldPackage)) {
                return false;
            }
            if (!isCurrentPreparedPackage(oldPackage)) {
                if (oldPackage.equals("[Legacy VPN]") || !isVpnPreConsented(this.mContext, oldPackage, vpnType)) {
                    return false;
                }
                prepareInternal(oldPackage);
                return true;
            }
            if (!oldPackage.equals("[Legacy VPN]") && !isVpnPreConsented(this.mContext, oldPackage, vpnType)) {
                prepareInternal("[Legacy VPN]");
                return false;
            }
        }
        if (newPackage != null && (newPackage.equals("[Legacy VPN]") || !isCurrentPreparedPackage(newPackage))) {
            enforceControlPermissionOrInternalCaller();
            if (this.mAlwaysOn && !isCurrentPreparedPackage(newPackage)) {
                return false;
            }
            prepareInternal(newPackage);
            return true;
        }
        return true;
    }

    private boolean isCurrentPreparedPackage(java.lang.String packageName) {
        return getAppUid(this.mContext, packageName, this.mUserId) == this.mOwnerUID && this.mPackage.equals(packageName);
    }

    private void prepareInternal(java.lang.String newPackage) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (this.mInterface != null) {
                this.mStatusIntent = null;
                agentDisconnect();
                jniReset(this.mInterface);
                this.mInterface = null;
                resetNetworkCapabilities();
            }
            if (this.mConnection != null) {
                try {
                    this.mConnection.mService.transact(android.hardware.audio.common.V2_0.AudioFormat.SUB_MASK, android.os.Parcel.obtain(), null, 1);
                } catch (java.lang.Exception e) {
                }
                this.mAppOpsManager.finishOp("android:establish_vpn_service", this.mOwnerUID, this.mPackage, null);
                this.mContext.unbindService(this.mConnection);
                cleanupVpnStateLocked();
            } else if (this.mVpnRunner != null) {
                stopVpnRunnerAndNotifyAppLocked();
            }
            try {
                this.mNetd.networkSetProtectDeny(this.mOwnerUID);
            } catch (java.lang.Exception e2) {
                android.util.Log.wtf(TAG, "Failed to disallow UID " + this.mOwnerUID + " to call protect() " + e2);
            }
            android.util.Log.i(TAG, "Switched from " + this.mPackage + " to " + newPackage);
            this.mPackage = newPackage;
            this.mOwnerUID = getAppUid(this.mContext, newPackage, this.mUserId);
            this.mIsPackageTargetingAtLeastQ = doesPackageTargetAtLeastQ(newPackage);
            try {
                this.mNetd.networkSetProtectAllow(this.mOwnerUID);
            } catch (java.lang.Exception e3) {
                android.util.Log.wtf(TAG, "Failed to allow UID " + this.mOwnerUID + " to call protect() " + e3);
            }
            this.mConfig = null;
            updateState(android.net.NetworkInfo.DetailedState.DISCONNECTED, "prepare");
            setVpnForcedLocked(this.mLockdown);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean setPackageAuthorization(java.lang.String packageName, int vpnType) {
        java.lang.String[] toChange;
        enforceControlPermissionOrInternalCaller();
        int uid = getAppUid(this.mContext, packageName, this.mUserId);
        if (uid == -1 || "[Legacy VPN]".equals(packageName)) {
            return false;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            switch (vpnType) {
                case -1:
                    toChange = new java.lang.String[]{"android:activate_vpn", "android:activate_platform_vpn"};
                    break;
                case 0:
                default:
                    android.util.Log.wtf(TAG, "Unrecognized VPN type while granting authorization");
                    return false;
                case 1:
                    toChange = new java.lang.String[]{"android:activate_vpn"};
                    break;
                case 2:
                    toChange = new java.lang.String[]{"android:activate_platform_vpn"};
                    break;
                case 3:
                    return false;
            }
            int length = toChange.length;
            int i = 0;
            while (true) {
                int i2 = 1;
                if (i >= length) {
                    return true;
                }
                java.lang.String appOpStr = toChange[i];
                android.app.AppOpsManager appOpsManager = this.mAppOpsManager;
                if (vpnType != -1) {
                    i2 = 0;
                }
                appOpsManager.setMode(appOpStr, uid, packageName, i2);
                i++;
            }
        } catch (java.lang.Exception e) {
            android.util.Log.wtf(TAG, "Failed to set app ops for package " + packageName + ", uid " + uid, e);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private static boolean isVpnPreConsented(android.content.Context context, java.lang.String packageName, int vpnType) {
        switch (vpnType) {
            case 1:
                return isVpnServicePreConsented(context, packageName);
            case 2:
                return isVpnProfilePreConsented(context, packageName);
            case 3:
                return "[Legacy VPN]".equals(packageName);
            default:
                return false;
        }
    }

    private static boolean doesPackageHaveAppop(android.content.Context context, java.lang.String packageName, java.lang.String appOpStr) {
        android.app.AppOpsManager appOps = (android.app.AppOpsManager) context.getSystemService("appops");
        return appOps.noteOpNoThrow(appOpStr, android.os.Binder.getCallingUid(), packageName, null, null) == 0;
    }

    private static boolean isVpnServicePreConsented(android.content.Context context, java.lang.String packageName) {
        return doesPackageHaveAppop(context, packageName, "android:activate_vpn");
    }

    private static boolean isVpnProfilePreConsented(android.content.Context context, java.lang.String packageName) {
        return doesPackageHaveAppop(context, packageName, "android:activate_platform_vpn") || isVpnServicePreConsented(context, packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getAppUid(android.content.Context context, java.lang.String app, int userId) {
        if ("[Legacy VPN]".equals(app)) {
            return android.os.Process.myUid();
        }
        android.content.pm.PackageManager pm = context.getPackageManager();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            int packageUidAsUser = pm.getPackageUidAsUser(app, userId);
            android.os.Binder.restoreCallingIdentity(token);
            return packageUidAsUser;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.os.Binder.restoreCallingIdentity(token);
            return -1;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    private boolean doesPackageTargetAtLeastQ(java.lang.String packageName) {
        if ("[Legacy VPN]".equals(packageName)) {
            return true;
        }
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        try {
            android.content.pm.ApplicationInfo appInfo = pm.getApplicationInfoAsUser(packageName, 0, this.mUserId);
            return appInfo.targetSdkVersion >= 29;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.w(TAG, "Can't find \"" + packageName + "\"");
            return false;
        }
    }

    public android.net.NetworkInfo getNetworkInfo() {
        return this.mNetworkInfo;
    }

    public synchronized android.net.Network getNetwork() {
        android.net.NetworkAgent agent = this.mNetworkAgent;
        if (agent == null) {
            return null;
        }
        android.net.Network network = agent.getNetwork();
        if (network == null) {
            return null;
        }
        return network;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.net.LinkProperties makeLinkProperties() {
        boolean disableIPV6 = isIkev2VpnRunner() && this.mConfig.mtu < 1280;
        boolean allowIPv4 = this.mConfig.allowIPv4;
        boolean allowIPv6 = this.mConfig.allowIPv6;
        android.net.LinkProperties lp = new android.net.LinkProperties();
        lp.setInterfaceName(this.mInterface);
        if (this.mConfig.addresses != null) {
            for (android.net.LinkAddress address : this.mConfig.addresses) {
                if (!disableIPV6 || !address.isIpv6()) {
                    lp.addLinkAddress(address);
                    allowIPv4 |= address.getAddress() instanceof java.net.Inet4Address;
                    allowIPv6 |= address.getAddress() instanceof java.net.Inet6Address;
                }
            }
        }
        if (this.mConfig.routes != null) {
            for (android.net.RouteInfo route : this.mConfig.routes) {
                java.net.InetAddress address2 = route.getDestination().getAddress();
                if (!disableIPV6 || !(address2 instanceof java.net.Inet6Address)) {
                    lp.addRoute(route);
                    if (route.getType() == 1) {
                        allowIPv4 |= address2 instanceof java.net.Inet4Address;
                        allowIPv6 |= address2 instanceof java.net.Inet6Address;
                    }
                }
            }
        }
        if (this.mConfig.dnsServers != null) {
            for (java.lang.String dnsServer : this.mConfig.dnsServers) {
                java.net.InetAddress address3 = android.net.InetAddresses.parseNumericAddress(dnsServer);
                if (!disableIPV6 || !(address3 instanceof java.net.Inet6Address)) {
                    lp.addDnsServer(address3);
                    allowIPv4 |= address3 instanceof java.net.Inet4Address;
                    allowIPv6 |= address3 instanceof java.net.Inet6Address;
                }
            }
        }
        lp.setHttpProxy(this.mConfig.proxyInfo);
        if (!allowIPv4) {
            lp.addRoute(new android.net.RouteInfo(new android.net.IpPrefix(com.android.net.module.util.NetworkStackConstants.IPV4_ADDR_ANY, 0), null, null, 7));
        }
        if (!allowIPv6 || disableIPV6) {
            lp.addRoute(new android.net.RouteInfo(new android.net.IpPrefix(com.android.net.module.util.NetworkStackConstants.IPV6_ADDR_ANY, 0), null, null, 7));
        }
        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
        if (this.mConfig.searchDomains != null) {
            for (java.lang.String domain : this.mConfig.searchDomains) {
                buffer.append(domain).append(' ');
            }
        }
        lp.setDomains(buffer.toString().trim());
        if (this.mConfig.mtu > 0) {
            lp.setMtu(this.mConfig.mtu);
        }
        return lp;
    }

    private boolean updateLinkPropertiesInPlaceIfPossible(android.net.NetworkAgent agent, com.android.internal.net.VpnConfig oldConfig) {
        if (oldConfig.allowBypass != this.mConfig.allowBypass) {
            android.util.Log.i(TAG, "Handover not possible due to changes to allowBypass");
            return false;
        }
        if (!java.util.Objects.equals(oldConfig.allowedApplications, this.mConfig.allowedApplications) || !java.util.Objects.equals(oldConfig.disallowedApplications, this.mConfig.disallowedApplications)) {
            android.util.Log.i(TAG, "Handover not possible due to changes to allowed/denied apps");
            return false;
        }
        agent.sendLinkProperties(makeLinkProperties());
        return true;
    }

    private void agentConnect() {
        agentConnect(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void agentConnect(com.android.server.connectivity.Vpn.ValidationStatusCallback validationCallback) {
        com.android.server.connectivity.Vpn.IkeSessionWrapper session;
        android.net.LinkProperties lp = makeLinkProperties();
        android.net.NetworkCapabilities.Builder capsBuilder = new android.net.NetworkCapabilities.Builder(this.mNetworkCapabilities);
        capsBuilder.addCapability(12);
        this.mLegacyState = 2;
        updateState(android.net.NetworkInfo.DetailedState.CONNECTING, "agentConnect");
        boolean bypassable = this.mConfig.allowBypass && !this.mLockdown;
        android.net.NetworkAgentConfig networkAgentConfig = new android.net.NetworkAgentConfig.Builder().setLegacyType(17).setLegacyTypeName(NETWORKTYPE).setBypassableVpn(bypassable).setVpnRequiresValidation(this.mConfig.requiresInternetValidation).setLocalRoutesExcludedForVpn(this.mConfig.excludeLocalRoutes).setLegacyExtraInfo("VPN:" + this.mPackage).build();
        capsBuilder.setOwnerUid(this.mOwnerUID);
        capsBuilder.setAdministratorUids(new int[]{this.mOwnerUID});
        capsBuilder.setUids(createUserAndRestrictedProfilesRanges(this.mUserId, this.mConfig.allowedApplications, this.mConfig.disallowedApplications));
        boolean expensive = areLongLivedTcpConnectionsExpensive(this.mVpnRunner);
        capsBuilder.setTransportInfo(new android.net.VpnTransportInfo(getActiveVpnType(), this.mConfig.session, bypassable, expensive));
        if (this.mIsPackageTargetingAtLeastQ && this.mConfig.isMetered) {
            capsBuilder.removeCapability(11);
        } else {
            capsBuilder.addCapability(11);
        }
        capsBuilder.setUnderlyingNetworks(this.mConfig.underlyingNetworks != null ? java.util.Arrays.asList(this.mConfig.underlyingNetworks) : null);
        this.mNetworkCapabilities = capsBuilder.build();
        logUnderlyNetworkChanges(this.mNetworkCapabilities.getUnderlyingNetworks());
        this.mNetworkAgent = this.mDeps.newNetworkAgent(this.mContext, this.mLooper, NETWORKTYPE, this.mNetworkCapabilities, lp, new android.net.NetworkScore.Builder().setLegacyInt(101).build(), networkAgentConfig, this.mNetworkProvider, validationCallback);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkAgent.register();
                android.os.Binder.restoreCallingIdentity(token);
                if (this.mVpnExt != null) {
                    this.mConfig = this.mVpnExt.parseApplicationsFromXml(this.mConfig);
                }
                if (this.mVpnExt != null) {
                    this.mStatusIntent = this.mVpnExt.prepareStatusIntent(this.mStatusIntent);
                    this.mVpnExt.showNotification(null, 0, this.mUserId, this.mPackage, this.mStatusIntent, this.mConfig);
                }
                updateState(android.net.NetworkInfo.DetailedState.CONNECTED, "agentConnect");
                if (isIkev2VpnRunner() && (session = ((com.android.server.connectivity.Vpn.IkeV2VpnRunner) this.mVpnRunner).mSession) != null) {
                    session.setUnderpinnedNetwork(this.mNetworkAgent.getNetwork());
                }
            } catch (java.lang.Exception e) {
                this.mNetworkAgent = null;
                throw e;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    private static boolean areLongLivedTcpConnectionsExpensive(com.android.server.connectivity.Vpn.VpnRunner runner) {
        if (!(runner instanceof com.android.server.connectivity.Vpn.IkeV2VpnRunner)) {
            return false;
        }
        int delay = ((com.android.server.connectivity.Vpn.IkeV2VpnRunner) runner).getOrGuessKeepaliveDelaySeconds();
        return areLongLivedTcpConnectionsExpensive(delay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean areLongLivedTcpConnectionsExpensive(int keepaliveDelaySec) {
        return keepaliveDelaySec < 60;
    }

    private boolean canHaveRestrictedProfile(int userId) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.content.Context userContext = this.mContext.createContextAsUser(android.os.UserHandle.of(userId), 0);
            return ((android.os.UserManager) userContext.getSystemService(android.os.UserManager.class)).canHaveRestrictedProfile();
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void logUnderlyNetworkChanges(java.util.List<android.net.Network> networks) {
        this.mEventChanges.log("[UnderlyingNW] Switch to " + (networks != null ? android.text.TextUtils.join(", ", networks) : "null"));
    }

    private void agentDisconnect(android.net.NetworkAgent networkAgent) {
        if (networkAgent != null) {
            networkAgent.unregister();
        }
    }

    private void agentDisconnect() {
        updateState(android.net.NetworkInfo.DetailedState.DISCONNECTED, "agentDisconnect");
        if (this.mVpnExt != null) {
            this.mVpnExt.hideNotification(this.mUserId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startNewNetworkAgent(android.net.NetworkAgent oldNetworkAgent, java.lang.String reason) {
        this.mNetworkAgent = null;
        updateState(android.net.NetworkInfo.DetailedState.CONNECTING, reason);
        agentConnect();
        agentDisconnect(oldNetworkAgent);
    }

    public synchronized android.os.ParcelFileDescriptor establish(com.android.internal.net.VpnConfig config) {
        if (android.os.Binder.getCallingUid() != this.mOwnerUID) {
            return null;
        }
        if (!isVpnServicePreConsented(this.mContext, this.mPackage)) {
            return null;
        }
        if (doesHaveVPNAppWhiteList()) {
            if (!isInVPNAppWhiteList(this.mPackage)) {
                android.util.Log.d(TAG, "prepareVpn, the VPN can't be estabilshed as the app is not in the white list");
                return null;
            }
        } else if (isVpnDisabled()) {
            android.util.Log.d(TAG, "prepareVpn, the VPN can't be estabilshed as the switch disable");
            return null;
        }
        android.content.Intent intent = new android.content.Intent("android.net.VpnService");
        intent.setClassName(this.mPackage, config.user);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            enforceNotRestrictedUser();
            android.content.pm.PackageManager packageManager = this.mUserIdContext.getPackageManager();
            if (packageManager == null) {
                throw new java.lang.IllegalStateException("Cannot get PackageManager.");
            }
            android.content.pm.ResolveInfo info = packageManager.resolveService(intent, 0);
            if (info == null) {
                throw new java.lang.SecurityException("Cannot find " + config.user);
            }
            if (!"android.permission.BIND_VPN_SERVICE".equals(info.serviceInfo.permission)) {
                throw new java.lang.SecurityException(config.user + " does not require android.permission.BIND_VPN_SERVICE");
            }
            android.os.Binder.restoreCallingIdentity(token);
            com.android.internal.net.VpnConfig oldConfig = this.mConfig;
            java.lang.String oldInterface = this.mInterface;
            com.android.server.connectivity.Vpn.Connection oldConnection = this.mConnection;
            android.net.NetworkAgent oldNetworkAgent = this.mNetworkAgent;
            java.util.Set<android.util.Range<java.lang.Integer>> oldUsers = this.mNetworkCapabilities.getUids();
            android.os.ParcelFileDescriptor tun = this.mDeps.adoptFd(this, config.mtu);
            try {
                java.lang.String interfaze = this.mDeps.jniGetName(this, tun.getFd());
                java.lang.StringBuilder builder = new java.lang.StringBuilder();
                for (android.net.LinkAddress address : config.addresses) {
                    builder.append(" ");
                    builder.append(address);
                }
                if (this.mDeps.jniSetAddresses(this, interfaze, builder.toString()) < 1) {
                    throw new java.lang.IllegalArgumentException("At least one address must be specified");
                }
                com.android.server.connectivity.Vpn.Connection connection = new com.android.server.connectivity.Vpn.Connection();
                if (!this.mContext.bindServiceAsUser(intent, connection, android.hardware.audio.common.V2_0.AudioFormat.AAC_MAIN, new android.os.UserHandle(this.mUserId))) {
                    throw new java.lang.IllegalStateException("Cannot bind " + config.user);
                }
                this.mConnection = connection;
                this.mInterface = interfaze;
                config.user = this.mPackage;
                config.interfaze = this.mInterface;
                config.startTime = android.os.SystemClock.elapsedRealtime();
                this.mConfig = config;
                if (oldConfig != null && updateLinkPropertiesInPlaceIfPossible(this.mNetworkAgent, oldConfig)) {
                    if (!java.util.Arrays.equals(oldConfig.underlyingNetworks, config.underlyingNetworks)) {
                        setUnderlyingNetworks(config.underlyingNetworks);
                    }
                } else {
                    startNewNetworkAgent(oldNetworkAgent, "establish");
                }
                if (oldConnection != null) {
                    this.mContext.unbindService(oldConnection);
                }
                if (oldInterface != null && !oldInterface.equals(interfaze)) {
                    jniReset(oldInterface);
                }
                this.mDeps.setBlocking(tun.getFileDescriptor(), config.blocking);
                if (oldNetworkAgent != this.mNetworkAgent) {
                    this.mAppOpsManager.startOp("android:establish_vpn_service", this.mOwnerUID, this.mPackage, null, null);
                }
                android.util.Log.i(TAG, "Established by " + config.user + " on " + this.mInterface);
                return tun;
            } catch (java.lang.RuntimeException e) {
                libcore.io.IoUtils.closeQuietly(tun);
                if (oldNetworkAgent != this.mNetworkAgent) {
                    agentDisconnect();
                }
                this.mConfig = oldConfig;
                this.mConnection = oldConnection;
                this.mNetworkCapabilities = new android.net.NetworkCapabilities.Builder(this.mNetworkCapabilities).setUids(oldUsers).build();
                this.mNetworkAgent = oldNetworkAgent;
                this.mInterface = oldInterface;
                throw e;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    private boolean isRunningLocked() {
        return (this.mNetworkAgent == null || this.mInterface == null) ? false : true;
    }

    protected boolean isCallerEstablishedOwnerLocked() {
        return isRunningLocked() && android.os.Binder.getCallingUid() == this.mOwnerUID;
    }

    private java.util.SortedSet<java.lang.Integer> getAppsUids(java.util.List<java.lang.String> packageNames, int userId) {
        java.util.SortedSet<java.lang.Integer> uids = new java.util.TreeSet<>();
        for (java.lang.String app : packageNames) {
            int uid = getAppUid(this.mContext, app, userId);
            if (uid != -1) {
                uids.add(java.lang.Integer.valueOf(uid));
            }
            if (android.os.Process.isApplicationUid(uid)) {
                uids.add(java.lang.Integer.valueOf(android.os.Process.toSdkSandboxUid(uid)));
            }
        }
        return uids;
    }

    java.util.Set<android.util.Range<java.lang.Integer>> createUserAndRestrictedProfilesRanges(int userId, java.util.List<java.lang.String> allowedApplications, java.util.List<java.lang.String> disallowedApplications) {
        java.util.Set<android.util.Range<java.lang.Integer>> ranges = new android.util.ArraySet<>();
        addUserToRanges(ranges, userId, allowedApplications, disallowedApplications);
        if (canHaveRestrictedProfile(userId)) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getAliveUsers();
                android.os.Binder.restoreCallingIdentity(token);
                for (android.content.pm.UserInfo user : users) {
                    if (user.isRestricted() && user.restrictedProfileParentId == userId) {
                        addUserToRanges(ranges, user.id, allowedApplications, disallowedApplications);
                    }
                }
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
        }
        return ranges;
    }

    void addUserToRanges(java.util.Set<android.util.Range<java.lang.Integer>> ranges, int userId, java.util.List<java.lang.String> allowedApplications, java.util.List<java.lang.String> disallowedApplications) {
        if (allowedApplications != null) {
            int start = -1;
            int stop = -1;
            java.util.Iterator<java.lang.Integer> it = getAppsUids(allowedApplications, userId).iterator();
            while (it.hasNext()) {
                int uid = it.next().intValue();
                if (start == -1) {
                    start = uid;
                } else if (uid != stop + 1) {
                    ranges.add(new android.util.Range<>(java.lang.Integer.valueOf(start), java.lang.Integer.valueOf(stop)));
                    start = uid;
                }
                stop = uid;
            }
            if (start != -1) {
                ranges.add(new android.util.Range<>(java.lang.Integer.valueOf(start), java.lang.Integer.valueOf(stop)));
                return;
            }
            return;
        }
        if (disallowedApplications != null) {
            android.util.Range<java.lang.Integer> userRange = createUidRangeForUser(userId);
            int start2 = ((java.lang.Integer) userRange.getLower()).intValue();
            java.util.Iterator<java.lang.Integer> it2 = getAppsUids(disallowedApplications, userId).iterator();
            while (it2.hasNext()) {
                int uid2 = it2.next().intValue();
                if (uid2 == start2) {
                    start2++;
                } else {
                    ranges.add(new android.util.Range<>(java.lang.Integer.valueOf(start2), java.lang.Integer.valueOf(uid2 - 1)));
                    start2 = uid2 + 1;
                }
            }
            if (start2 <= ((java.lang.Integer) userRange.getUpper()).intValue()) {
                ranges.add(new android.util.Range<>(java.lang.Integer.valueOf(start2), (java.lang.Integer) userRange.getUpper()));
                return;
            }
            return;
        }
        ranges.add(createUidRangeForUser(userId));
    }

    private static java.util.List<android.util.Range<java.lang.Integer>> uidRangesForUser(int userId, java.util.Set<android.util.Range<java.lang.Integer>> existingRanges) {
        android.util.Range<java.lang.Integer> userRange = createUidRangeForUser(userId);
        java.util.List<android.util.Range<java.lang.Integer>> ranges = new java.util.ArrayList<>();
        for (android.util.Range<java.lang.Integer> range : existingRanges) {
            if (userRange.contains(range)) {
                ranges.add(range);
            }
        }
        return ranges;
    }

    public void onUserAdded(int userId) {
        android.content.pm.UserInfo user = this.mUserManager.getUserInfo(userId);
        if (user != null && user.isRestricted() && user.restrictedProfileParentId == this.mUserId) {
            synchronized (this) {
                java.util.Set existingRanges = this.mNetworkCapabilities.getUids();
                if (existingRanges != null) {
                    try {
                        if (this.mVpnExt != null) {
                            this.mConfig = this.mVpnExt.parseApplicationsFromXml(this.mConfig);
                        }
                        addUserToRanges(existingRanges, userId, this.mConfig.allowedApplications, this.mConfig.disallowedApplications);
                        this.mNetworkCapabilities = new android.net.NetworkCapabilities.Builder(this.mNetworkCapabilities).setUids(existingRanges).build();
                        if (this.mVpnExt != null) {
                            this.mStatusIntent = this.mVpnExt.prepareStatusIntent(this.mStatusIntent);
                            this.mVpnExt.showNotification(null, 0, this.mUserId, this.mPackage, this.mStatusIntent, this.mConfig);
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Log.wtf(TAG, "Failed to add restricted user to owner", e);
                    }
                    if (this.mNetworkAgent != null) {
                        doSendNetworkCapabilities(this.mNetworkAgent, this.mNetworkCapabilities);
                    }
                    setVpnForcedLocked(this.mLockdown);
                } else {
                    setVpnForcedLocked(this.mLockdown);
                }
            }
        }
    }

    public void onUserRemoved(int userId) {
        android.content.pm.UserInfo user = this.mUserManager.getUserInfo(userId);
        if (user != null && user.isRestricted() && user.restrictedProfileParentId == this.mUserId) {
            synchronized (this) {
                java.util.Set<android.util.Range<java.lang.Integer>> existingRanges = this.mNetworkCapabilities.getUids();
                if (existingRanges != null) {
                    try {
                        java.util.List<android.util.Range<java.lang.Integer>> removedRanges = uidRangesForUser(userId, existingRanges);
                        existingRanges.removeAll(removedRanges);
                        this.mNetworkCapabilities = new android.net.NetworkCapabilities.Builder(this.mNetworkCapabilities).setUids(existingRanges).build();
                    } catch (java.lang.Exception e) {
                        android.util.Log.wtf(TAG, "Failed to remove restricted user to owner", e);
                    }
                    if (this.mNetworkAgent != null) {
                        doSendNetworkCapabilities(this.mNetworkAgent, this.mNetworkCapabilities);
                    }
                    setVpnForcedLocked(this.mLockdown);
                } else {
                    setVpnForcedLocked(this.mLockdown);
                }
            }
        }
    }

    public synchronized void onUserStopped() {
        setVpnForcedLocked(false);
        this.mAlwaysOn = false;
        agentDisconnect();
        this.mConnectivityManager.unregisterNetworkProvider(this.mNetworkProvider);
    }

    private void setVpnForcedLocked(boolean enforce) {
        java.util.List<java.lang.String> exemptedPackages;
        java.util.Set<android.net.UidRangeParcel> rangesToAdd;
        if (isNullOrLegacyVpn(this.mPackage)) {
            exemptedPackages = null;
        } else {
            exemptedPackages = new java.util.ArrayList<>(this.mLockdownAllowlist);
            exemptedPackages.add(this.mPackage);
        }
        java.util.Set<android.net.UidRangeParcel> rangesToRemove = new android.util.ArraySet<>(this.mBlockedUidsAsToldToConnectivity);
        if (enforce) {
            java.util.Set<android.util.Range<java.lang.Integer>> restrictedProfilesRanges = createUserAndRestrictedProfilesRanges(this.mUserId, null, exemptedPackages);
            java.util.Set<android.net.UidRangeParcel> rangesThatShouldBeBlocked = new android.util.ArraySet<>();
            for (android.util.Range<java.lang.Integer> range : restrictedProfilesRanges) {
                if (((java.lang.Integer) range.getLower()).intValue() == 0 && ((java.lang.Integer) range.getUpper()).intValue() != 0) {
                    rangesThatShouldBeBlocked.add(new android.net.UidRangeParcel(1, ((java.lang.Integer) range.getUpper()).intValue()));
                } else if (((java.lang.Integer) range.getLower()).intValue() != 0) {
                    rangesThatShouldBeBlocked.add(new android.net.UidRangeParcel(((java.lang.Integer) range.getLower()).intValue(), ((java.lang.Integer) range.getUpper()).intValue()));
                }
            }
            rangesToRemove.removeAll(rangesThatShouldBeBlocked);
            rangesToAdd = rangesThatShouldBeBlocked;
            rangesToAdd.removeAll(this.mBlockedUidsAsToldToConnectivity);
        } else {
            rangesToAdd = java.util.Collections.emptySet();
        }
        setAllowOnlyVpnForUids(false, rangesToRemove);
        setAllowOnlyVpnForUids(true, rangesToAdd);
    }

    private boolean setAllowOnlyVpnForUids(boolean enforce, java.util.Collection<android.net.UidRangeParcel> ranges) {
        if (ranges.size() == 0) {
            return true;
        }
        java.util.ArrayList<android.util.Range<java.lang.Integer>> integerRanges = new java.util.ArrayList<>(ranges.size());
        for (android.net.UidRangeParcel uidRange : ranges) {
            integerRanges.add(new android.util.Range<>(java.lang.Integer.valueOf(uidRange.start), java.lang.Integer.valueOf(uidRange.stop)));
        }
        try {
            this.mConnectivityManager.setRequireVpnForUids(enforce, integerRanges);
            if (enforce) {
                this.mBlockedUidsAsToldToConnectivity.addAll(ranges);
            } else {
                this.mBlockedUidsAsToldToConnectivity.removeAll(ranges);
            }
            return true;
        } catch (java.lang.RuntimeException e) {
            android.util.Log.e(TAG, "Updating blocked=" + enforce + " for UIDs " + java.util.Arrays.toString(ranges.toArray()) + " failed", e);
            return false;
        }
    }

    public synchronized com.android.internal.net.VpnConfig getVpnConfig() {
        enforceControlPermission();
        if (this.mConfig == null) {
            return null;
        }
        return new com.android.internal.net.VpnConfig(this.mConfig);
    }

    @java.lang.Deprecated
    public synchronized void interfaceStatusChanged(java.lang.String iface, boolean up) {
        try {
            try {
                this.mObserver.interfaceStatusChanged(iface, up);
            } catch (android.os.RemoteException e) {
            }
        } catch (android.os.RemoteException e2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupVpnStateLocked() {
        this.mStatusIntent = null;
        resetNetworkCapabilities();
        this.mConfig = null;
        this.mInterface = null;
        this.mVpnRunner = null;
        this.mConnection = null;
        agentDisconnect();
    }

    private void enforceControlPermission() {
        this.mContext.enforceCallingPermission("android.permission.CONTROL_VPN", "Unauthorized Caller");
    }

    private void enforceControlPermissionOrInternalCaller() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_VPN", "Unauthorized Caller");
    }

    private void enforceSettingsPermission() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.NETWORK_SETTINGS", "Unauthorized Caller");
    }

    private class Connection implements android.content.ServiceConnection {
        private android.os.IBinder mService;

        private Connection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            this.mService = service;
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            this.mService = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareStatusIntent() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mStatusIntent = this.mDeps.getIntentForStatusPanel(this.mContext);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public synchronized boolean addAddress(java.lang.String address, int prefixLength) {
        if (!isCallerEstablishedOwnerLocked()) {
            return false;
        }
        boolean success = jniAddAddress(this.mInterface, address, prefixLength);
        doSendLinkProperties(this.mNetworkAgent, makeLinkProperties());
        return success;
    }

    public synchronized boolean removeAddress(java.lang.String address, int prefixLength) {
        if (!isCallerEstablishedOwnerLocked()) {
            return false;
        }
        boolean success = jniDelAddress(this.mInterface, address, prefixLength);
        doSendLinkProperties(this.mNetworkAgent, makeLinkProperties());
        return success;
    }

    public synchronized boolean setUnderlyingNetworks(android.net.Network[] networks) {
        if (!isCallerEstablishedOwnerLocked()) {
            return false;
        }
        java.util.List<android.net.Network> listAsList = null;
        this.mConfig.underlyingNetworks = networks != null ? (android.net.Network[]) java.util.Arrays.copyOf(networks, networks.length) : null;
        android.net.NetworkAgent networkAgent = this.mNetworkAgent;
        if (this.mConfig.underlyingNetworks != null) {
            listAsList = java.util.Arrays.asList(this.mConfig.underlyingNetworks);
        }
        doSetUnderlyingNetworks(networkAgent, listAsList);
        return true;
    }

    public synchronized android.net.UnderlyingNetworkInfo getUnderlyingNetworkInfo() {
        if (!isRunningLocked()) {
            return null;
        }
        return new android.net.UnderlyingNetworkInfo(this.mOwnerUID, this.mInterface, new java.util.ArrayList());
    }

    public synchronized boolean appliesToUid(int uid) {
        if (!isRunningLocked()) {
            return false;
        }
        java.util.Set<android.util.Range<java.lang.Integer>> uids = this.mNetworkCapabilities.getUids();
        if (uids == null) {
            return true;
        }
        for (android.util.Range<java.lang.Integer> range : uids) {
            if (range.contains(java.lang.Integer.valueOf(uid))) {
                return true;
            }
        }
        return false;
    }

    public synchronized int getActiveVpnType() {
        if (!this.mNetworkInfo.isConnectedOrConnecting()) {
            return -1;
        }
        if (this.mVpnRunner == null) {
            return 1;
        }
        return isIkev2VpnRunner() ? 2 : 3;
    }

    private void updateAlwaysOnNotification(android.net.NetworkInfo.DetailedState networkState) {
        boolean visible = this.mAlwaysOn && networkState != android.net.NetworkInfo.DetailedState.CONNECTED;
        android.os.UserHandle user = android.os.UserHandle.of(this.mUserId);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) this.mUserIdContext.getSystemService(android.app.NotificationManager.class);
            if (!visible) {
                notificationManager.cancel(TAG, 17);
                return;
            }
            android.content.Intent intent = new android.content.Intent();
            intent.setComponent(android.content.ComponentName.unflattenFromString(this.mContext.getString(android.R.string.config_defaultAttentionService)));
            intent.putExtra("lockdown", this.mLockdown);
            intent.addFlags(268435456);
            android.app.PendingIntent configIntent = this.mSystemServices.pendingIntentGetActivityAsUser(intent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD, user);
            android.app.Notification.Builder builder = new android.app.Notification.Builder(this.mContext, NETWORKTYPE).setSmallIcon(android.R.drawable.tab_selected_holo).setContentTitle(this.mContext.getString(android.R.string.usb_apm_usb_plugged_in_when_locked_notification_text)).setContentText(this.mContext.getString(android.R.string.unsupported_display_size_show)).setContentIntent(configIntent).setCategory("sys").setVisibility(1).setOngoing(true).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color));
            notificationManager.notify(TAG, 17, builder.build());
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public static class SystemServices {
        private final android.content.Context mContext;

        public SystemServices(android.content.Context context) {
            this.mContext = context;
        }

        public android.app.PendingIntent pendingIntentGetActivityAsUser(android.content.Intent intent, int flags, android.os.UserHandle user) {
            return android.app.PendingIntent.getActivity(this.mContext.createContextAsUser(user, 0), 0, intent, flags);
        }

        public void settingsSecurePutStringForUser(java.lang.String key, java.lang.String value, int userId) {
            android.provider.Settings.Secure.putString(getContentResolverAsUser(userId), key, value);
        }

        public void settingsSecurePutIntForUser(java.lang.String key, int value, int userId) {
            android.provider.Settings.Secure.putInt(getContentResolverAsUser(userId), key, value);
        }

        public java.lang.String settingsSecureGetStringForUser(java.lang.String key, int userId) {
            return android.provider.Settings.Secure.getString(getContentResolverAsUser(userId), key);
        }

        public int settingsSecureGetIntForUser(java.lang.String key, int def, int userId) {
            return android.provider.Settings.Secure.getInt(getContentResolverAsUser(userId), key, def);
        }

        private android.content.ContentResolver getContentResolverAsUser(int userId) {
            return this.mContext.createContextAsUser(android.os.UserHandle.of(userId), 0).getContentResolver();
        }
    }

    private void enforceNotRestrictedUser() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.UserInfo user = this.mUserManager.getUserInfo(this.mUserId);
            if (user.isRestricted()) {
                throw new java.lang.SecurityException("Restricted users cannot configure VPNs");
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void startLegacyVpn(com.android.internal.net.VpnProfile profile) {
        enforceControlPermission();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            startLegacyVpnPrivileged(profile);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private java.lang.String makeKeystoreEngineGrantString(java.lang.String alias) {
        if (alias == null) {
            return null;
        }
        android.security.KeyStore2 keystore2 = android.security.KeyStore2.getInstance();
        android.system.keystore2.KeyDescriptor key = new android.system.keystore2.KeyDescriptor();
        key.domain = 0;
        key.nspace = -1L;
        key.alias = alias;
        key.blob = null;
        try {
            return android.security.KeyStore2.makeKeystoreEngineGrantString(keystore2.grant(key, 1016, 260).nspace);
        } catch (android.security.KeyStoreException e) {
            android.util.Log.e(TAG, "Failed to get grant for keystore key.", e);
            throw new java.lang.IllegalStateException("Failed to get grant for keystore key.", e);
        }
    }

    private java.lang.String getCaCertificateFromKeystoreAsPem(java.security.KeyStore keystore, java.lang.String alias) throws java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateEncodingException {
        if (keystore.isCertificateEntry(alias)) {
            java.security.cert.Certificate cert = keystore.getCertificate(alias);
            if (cert == null) {
                return null;
            }
            return new java.lang.String(android.security.Credentials.convertToPem(new java.security.cert.Certificate[]{cert}), java.nio.charset.StandardCharsets.UTF_8);
        }
        java.security.cert.Certificate[] certs = keystore.getCertificateChain(alias);
        if (certs == null || certs.length <= 1) {
            return null;
        }
        return new java.lang.String(android.security.Credentials.convertToPem((java.security.cert.Certificate[]) java.util.Arrays.copyOfRange(certs, 1, certs.length)), java.nio.charset.StandardCharsets.UTF_8);
    }

    public void startLegacyVpnPrivileged(com.android.internal.net.VpnProfile profileToStart) {
        java.lang.String str;
        com.android.internal.net.VpnProfile profile = profileToStart.clone();
        android.content.pm.UserInfo user = this.mUserManager.getUserInfo(this.mUserId);
        if (user == null) {
            android.util.Log.i(TAG, "startLegacyVpnPrivileged user == null");
            return;
        }
        if (user.isRestricted() || this.mUserManager.hasUserRestriction("no_config_vpn", new android.os.UserHandle(this.mUserId))) {
            throw new java.lang.SecurityException("Restricted users cannot establish VPNs");
        }
        java.lang.String privateKey = "";
        java.lang.String userCert = "";
        java.lang.String caCert = "";
        java.lang.String serverCert = "";
        try {
            java.security.KeyStore keystore = java.security.KeyStore.getInstance("AndroidKeyStore");
            java.lang.String str2 = null;
            keystore.load(null);
            if (!profile.ipsecUserCert.isEmpty()) {
                privateKey = profile.ipsecUserCert;
                java.security.cert.Certificate cert = keystore.getCertificate(profile.ipsecUserCert);
                if (cert == null) {
                    str = null;
                } else {
                    str = new java.lang.String(android.security.Credentials.convertToPem(new java.security.cert.Certificate[]{cert}), java.nio.charset.StandardCharsets.UTF_8);
                }
                userCert = str;
            }
            if (!profile.ipsecCaCert.isEmpty()) {
                caCert = getCaCertificateFromKeystoreAsPem(keystore, profile.ipsecCaCert);
            }
            if (!profile.ipsecServerCert.isEmpty()) {
                java.security.cert.Certificate cert2 = keystore.getCertificate(profile.ipsecServerCert);
                if (cert2 != null) {
                    str2 = new java.lang.String(android.security.Credentials.convertToPem(new java.security.cert.Certificate[]{cert2}), java.nio.charset.StandardCharsets.UTF_8);
                }
                serverCert = str2;
            }
            if (userCert == null || caCert == null || serverCert == null) {
                throw new java.lang.IllegalStateException("Cannot load credentials");
            }
            switch (profile.type) {
                case 6:
                    break;
                case 7:
                    profile.ipsecSecret = android.net.Ikev2VpnProfile.encodeForIpsecSecret(profile.ipsecSecret.getBytes());
                    profile.setAllowedAlgorithms(android.net.Ikev2VpnProfile.DEFAULT_ALGORITHMS);
                    startVpnProfilePrivileged(profile, "[Legacy VPN]");
                    return;
                case 8:
                    profile.ipsecSecret = "KEYSTORE_ALIAS:" + privateKey;
                    profile.ipsecUserCert = userCert;
                    break;
                case 9:
                    startVpnProfilePrivileged(profile, "[Legacy VPN]");
                    return;
                default:
                    throw new java.lang.UnsupportedOperationException("Legacy VPN is deprecated");
            }
            profile.ipsecCaCert = caCert;
            profile.setAllowedAlgorithms(android.net.Ikev2VpnProfile.DEFAULT_ALGORITHMS);
            startVpnProfilePrivileged(profile, "[Legacy VPN]");
        } catch (java.io.IOException | java.security.KeyStoreException | java.security.NoSuchAlgorithmException | java.security.cert.CertificateException e) {
            throw new java.lang.IllegalStateException("Failed to load credentials from AndroidKeyStore", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSettingsVpnLocked() {
        return this.mVpnRunner != null && "[Legacy VPN]".equals(this.mPackage);
    }

    public synchronized void stopVpnRunnerPrivileged() {
        if (isSettingsVpnLocked()) {
            this.mVpnRunner.exit();
        }
    }

    public synchronized com.android.internal.net.LegacyVpnInfo getLegacyVpnInfo() {
        enforceControlPermission();
        return getLegacyVpnInfoPrivileged();
    }

    private synchronized com.android.internal.net.LegacyVpnInfo getLegacyVpnInfoPrivileged() {
        if (!isSettingsVpnLocked()) {
            return null;
        }
        com.android.internal.net.LegacyVpnInfo info = new com.android.internal.net.LegacyVpnInfo();
        info.key = this.mConfig.user;
        info.state = this.mLegacyState;
        if (this.mNetworkInfo.isConnected()) {
            info.intent = this.mStatusIntent;
        }
        return info;
    }

    public synchronized com.android.internal.net.VpnConfig getLegacyVpnConfig() {
        if (!isSettingsVpnLocked()) {
            return null;
        }
        return this.mConfig;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized android.net.NetworkCapabilities getRedactedNetworkCapabilities(android.net.NetworkCapabilities nc) {
        if (nc != null) {
            if (!isNullOrLegacyVpn(this.mPackage)) {
                return this.mConnectivityManager.getRedactedNetworkCapabilitiesForPackage(nc, this.mOwnerUID, this.mPackage);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized android.net.LinkProperties getRedactedLinkProperties(android.net.LinkProperties lp) {
        if (lp == null) {
            return null;
        }
        return this.mConnectivityManager.getRedactedLinkPropertiesForPackage(lp, this.mOwnerUID, this.mPackage);
    }

    abstract class VpnRunner extends java.lang.Thread {
        protected abstract void exitVpnRunner();

        @Override // java.lang.Thread, java.lang.Runnable
        public abstract void run();

        protected VpnRunner(java.lang.String name) {
            super(name);
        }

        protected final void exit() {
            synchronized (com.android.server.connectivity.Vpn.this) {
                exitVpnRunner();
                com.android.server.connectivity.Vpn.this.cleanupVpnStateLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isIPv6Only(java.util.List<android.net.LinkAddress> linkAddresses) {
        boolean hasIPV6 = false;
        boolean hasIPV4 = false;
        for (android.net.LinkAddress address : linkAddresses) {
            hasIPV6 |= address.isIpv6();
            hasIPV4 |= address.isIpv4();
        }
        return hasIPV6 && !hasIPV4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVpnNetworkPreference(final java.lang.String session, final java.util.Set<android.util.Range<java.lang.Integer>> ranges) {
        com.android.net.module.util.BinderUtils.withCleanCallingIdentity(new com.android.net.module.util.BinderUtils.ThrowingRunnable() { // from class: com.android.server.connectivity.Vpn$$ExternalSyntheticLambda0
            public final void run() {
                this.f$0.lambda$setVpnNetworkPreference$0(session, ranges);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setVpnNetworkPreference$0(java.lang.String session, java.util.Set ranges) throws java.lang.RuntimeException {
        this.mConnectivityManager.setVpnDefaultForUids(session, ranges);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearVpnNetworkPreference(final java.lang.String session) {
        com.android.net.module.util.BinderUtils.withCleanCallingIdentity(new com.android.net.module.util.BinderUtils.ThrowingRunnable() { // from class: com.android.server.connectivity.Vpn$$ExternalSyntheticLambda1
            public final void run() {
                this.f$0.lambda$clearVpnNetworkPreference$1(session);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearVpnNetworkPreference$1(java.lang.String session) throws java.lang.RuntimeException {
        this.mConnectivityManager.setVpnDefaultForUids(session, java.util.Collections.EMPTY_LIST);
    }

    class IkeV2VpnRunner extends com.android.server.connectivity.Vpn.VpnRunner implements com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback {
        private static final long NETWORK_LOST_TIMEOUT_MS = 5000;
        private static final java.lang.String TAG = "IkeV2VpnRunner";
        private android.net.Network mActiveNetwork;
        private android.telephony.CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
        private int mCurrentToken;
        private final java.util.concurrent.ScheduledThreadPoolExecutor mExecutor;
        private android.net.ipsec.ike.IkeSessionConnectionInfo mIkeConnectionInfo;
        private final android.net.IpSecManager mIpSecManager;
        private boolean mIsRunning;
        private boolean mMobikeEnabled;
        private final android.net.ConnectivityManager.NetworkCallback mNetworkCallback;
        private final android.net.Ikev2VpnProfile mProfile;
        private int mRetryCount;
        private java.util.concurrent.ScheduledFuture<?> mScheduledHandleDataStallFuture;
        private java.util.concurrent.ScheduledFuture<?> mScheduledHandleNetworkLostFuture;
        private java.util.concurrent.ScheduledFuture<?> mScheduledHandleRetryIkeSessionFuture;
        private com.android.server.connectivity.Vpn.IkeSessionWrapper mSession;
        private final java.lang.String mSessionKey;
        private android.net.IpSecManager.IpSecTunnelInterface mTunnelIface;
        private android.net.LinkProperties mUnderlyingLinkProperties;
        private android.net.NetworkCapabilities mUnderlyingNetworkCapabilities;
        int mValidationFailRetryCount;

        IkeV2VpnRunner(android.net.Ikev2VpnProfile profile, java.util.concurrent.ScheduledThreadPoolExecutor executor) {
            super(TAG);
            this.mIsRunning = true;
            this.mCurrentToken = -1;
            this.mMobikeEnabled = false;
            this.mValidationFailRetryCount = 0;
            this.mRetryCount = 0;
            this.mCarrierConfigChangeListener = new android.telephony.CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.server.connectivity.Vpn.IkeV2VpnRunner.1
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public void onCarrierConfigChanged(int slotIndex, int subId, int carrierId, int specificCarrierId) {
                    com.android.server.connectivity.Vpn.this.mEventChanges.log("[CarrierConfig] Changed on slot " + slotIndex + " subId=" + subId + " carrerId=" + carrierId + " specificCarrierId=" + specificCarrierId);
                    synchronized (com.android.server.connectivity.Vpn.this) {
                        com.android.server.connectivity.Vpn.this.mCachedCarrierConfigInfoPerSubId.remove(subId);
                        if (com.android.server.connectivity.Vpn.this.mVpnRunner != com.android.server.connectivity.Vpn.IkeV2VpnRunner.this) {
                            return;
                        }
                        com.android.server.connectivity.Vpn.IkeV2VpnRunner.this.maybeMigrateIkeSessionAndUpdateVpnTransportInfo(com.android.server.connectivity.Vpn.IkeV2VpnRunner.this.mActiveNetwork);
                    }
                }
            };
            this.mProfile = profile;
            this.mExecutor = executor;
            this.mIpSecManager = (android.net.IpSecManager) com.android.server.connectivity.Vpn.this.mContext.getSystemService(android.net.INetd.IPSEC_INTERFACE_PREFIX);
            this.mNetworkCallback = new com.android.server.connectivity.VpnIkev2Utils.Ikev2VpnNetworkCallback(TAG, this, this.mExecutor);
            this.mSessionKey = java.util.UUID.randomUUID().toString();
            android.util.Log.d(TAG, "Generate session key = " + this.mSessionKey);
            this.mExecutor.setRemoveOnCancelPolicy(true);
            this.mExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            this.mExecutor.setRejectedExecutionHandler(new java.util.concurrent.RejectedExecutionHandler() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda3
                @Override // java.util.concurrent.RejectedExecutionHandler
                public final void rejectedExecution(java.lang.Runnable runnable, java.util.concurrent.ThreadPoolExecutor threadPoolExecutor) {
                    android.util.Log.d(com.android.server.connectivity.Vpn.IkeV2VpnRunner.TAG, "Runnable " + runnable + " rejected by the mExecutor");
                }
            });
            com.android.server.connectivity.Vpn.this.setVpnNetworkPreference(this.mSessionKey, com.android.server.connectivity.Vpn.this.createUserAndRestrictedProfilesRanges(com.android.server.connectivity.Vpn.this.mUserId, com.android.server.connectivity.Vpn.this.mConfig.allowedApplications, com.android.server.connectivity.Vpn.this.mConfig.disallowedApplications));
            com.android.server.connectivity.Vpn.this.mCarrierConfigManager.registerCarrierConfigChangeListener(this.mExecutor, this.mCarrierConfigChangeListener);
        }

        @Override // com.android.server.connectivity.Vpn.VpnRunner, java.lang.Thread, java.lang.Runnable
        public void run() {
            if (this.mProfile.isRestrictedToTestNetworks()) {
                android.net.NetworkRequest req = new android.net.NetworkRequest.Builder().clearCapabilities().addTransportType(7).addCapability(15).build();
                com.android.server.connectivity.Vpn.this.mConnectivityManager.requestNetwork(req, this.mNetworkCallback);
            } else {
                com.android.server.connectivity.Vpn.this.mConnectivityManager.registerSystemDefaultNetworkCallback(this.mNetworkCallback, new android.os.Handler(com.android.server.connectivity.Vpn.this.mLooper));
            }
        }

        private boolean isActiveNetwork(android.net.Network network) {
            return java.util.Objects.equals(this.mActiveNetwork, network) && this.mIsRunning;
        }

        private boolean isActiveToken(int token) {
            return this.mCurrentToken == token && this.mIsRunning;
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onIkeOpened(int token, android.net.ipsec.ike.IkeSessionConfiguration ikeConfiguration) {
            if (!isActiveToken(token)) {
                com.android.server.connectivity.Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onIkeOpened obsolete token=" + token);
                android.util.Log.d(TAG, "onIkeOpened called for obsolete token " + token);
            } else {
                this.mMobikeEnabled = ikeConfiguration.isIkeExtensionEnabled(2);
                android.net.ipsec.ike.IkeSessionConnectionInfo info = ikeConfiguration.getIkeSessionConnectionInfo();
                com.android.server.connectivity.Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onIkeOpened token=" + token + ", localAddr=" + info.getLocalAddress() + ", network=" + info.getNetwork() + ", mobikeEnabled= " + this.mMobikeEnabled);
                onIkeConnectionInfoChanged(token, info);
            }
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onIkeConnectionInfoChanged(int token, android.net.ipsec.ike.IkeSessionConnectionInfo ikeConnectionInfo) {
            if (!isActiveToken(token)) {
                com.android.server.connectivity.Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onIkeConnectionInfoChanged obsolete token=" + token);
                android.util.Log.d(TAG, "onIkeConnectionInfoChanged called for obsolete token " + token);
            } else {
                com.android.server.connectivity.Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onIkeConnectionInfoChanged token=" + token + ", localAddr=" + ikeConnectionInfo.getLocalAddress() + ", network=" + ikeConnectionInfo.getNetwork());
                this.mIkeConnectionInfo = ikeConnectionInfo;
            }
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onChildOpened(int token, android.net.ipsec.ike.ChildSessionConfiguration childConfig) {
            if (!isActiveToken(token)) {
                com.android.server.connectivity.Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onChildOpened obsolete token=" + token);
                android.util.Log.d(TAG, "onChildOpened called for obsolete token " + token);
                return;
            }
            com.android.server.connectivity.Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onChildOpened token=" + token + ", addr=" + android.text.TextUtils.join(", ", childConfig.getInternalAddresses()) + " dns=" + android.text.TextUtils.join(", ", childConfig.getInternalDnsServers()));
            try {
                java.lang.String interfaceName = this.mTunnelIface.getInterfaceName();
                java.util.List<android.net.LinkAddress> internalAddresses = childConfig.getInternalAddresses();
                java.util.ArrayList arrayList = new java.util.ArrayList();
                int vpnMtu = calculateVpnMtu();
                if (com.android.server.connectivity.Vpn.isIPv6Only(internalAddresses) && vpnMtu < 1280) {
                    onSessionLost(token, new android.net.ipsec.ike.exceptions.IkeIOException(new java.io.IOException("No valid addresses for MTU < 1280")));
                    return;
                }
                java.util.Collection<android.net.RouteInfo> newRoutes = com.android.server.connectivity.VpnIkev2Utils.getRoutesFromTrafficSelectors(childConfig.getOutboundTrafficSelectors());
                for (android.net.LinkAddress address : internalAddresses) {
                    this.mTunnelIface.addAddress(address.getAddress(), address.getPrefixLength());
                }
                for (java.net.InetAddress addr : childConfig.getInternalDnsServers()) {
                    arrayList.add(addr.getHostAddress());
                }
                android.net.Network network = this.mIkeConnectionInfo.getNetwork();
                synchronized (com.android.server.connectivity.Vpn.this) {
                    if (com.android.server.connectivity.Vpn.this.mVpnRunner != this) {
                        return;
                    }
                    com.android.server.connectivity.Vpn.this.mInterface = interfaceName;
                    com.android.server.connectivity.Vpn.this.mConfig.mtu = vpnMtu;
                    com.android.server.connectivity.Vpn.this.mConfig.interfaze = com.android.server.connectivity.Vpn.this.mInterface;
                    com.android.server.connectivity.Vpn.this.mConfig.addresses.clear();
                    com.android.server.connectivity.Vpn.this.mConfig.addresses.addAll(internalAddresses);
                    com.android.server.connectivity.Vpn.this.mConfig.routes.clear();
                    com.android.server.connectivity.Vpn.this.mConfig.routes.addAll(newRoutes);
                    if (com.android.server.connectivity.Vpn.this.mConfig.dnsServers == null) {
                        com.android.server.connectivity.Vpn.this.mConfig.dnsServers = new java.util.ArrayList();
                    }
                    com.android.server.connectivity.Vpn.this.mConfig.dnsServers.clear();
                    com.android.server.connectivity.Vpn.this.mConfig.dnsServers.addAll(arrayList);
                    com.android.server.connectivity.Vpn.this.mConfig.underlyingNetworks = new android.net.Network[]{network};
                    android.net.NetworkAgent networkAgent = com.android.server.connectivity.Vpn.this.mNetworkAgent;
                    if (networkAgent == null) {
                        if (com.android.server.connectivity.Vpn.this.isSettingsVpnLocked()) {
                            com.android.server.connectivity.Vpn.this.prepareStatusIntent();
                        }
                        com.android.server.connectivity.Vpn.this.agentConnect(new com.android.server.connectivity.Vpn.ValidationStatusCallback() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda4
                            @Override // com.android.server.connectivity.Vpn.ValidationStatusCallback
                            public final void onValidationStatus(int i) {
                                this.f$0.onValidationStatus(i);
                            }
                        });
                    } else {
                        android.net.LinkProperties lp = com.android.server.connectivity.Vpn.this.makeLinkProperties();
                        com.android.server.connectivity.Vpn.doSendLinkProperties(networkAgent, lp);
                        this.mRetryCount = 0;
                    }
                }
            } catch (java.lang.Exception e) {
                android.util.Log.d(TAG, "Error in ChildOpened for token " + token, e);
                onSessionLost(token, e);
            }
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onChildTransformCreated(int token, android.net.IpSecTransform transform, int direction) {
            if (!isActiveToken(token)) {
                com.android.server.connectivity.Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onChildTransformCreated obsolete token=" + token);
                android.util.Log.d(TAG, "ChildTransformCreated for obsolete token " + token);
                return;
            }
            com.android.server.connectivity.Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onChildTransformCreated token=" + token + ", direction=" + direction + ", transform=" + transform);
            try {
                this.mTunnelIface.setUnderlyingNetwork(this.mIkeConnectionInfo.getNetwork());
                this.mIpSecManager.applyTunnelModeTransform(this.mTunnelIface, direction, transform);
            } catch (java.io.IOException | java.lang.IllegalArgumentException e) {
                android.util.Log.d(TAG, "Transform application failed for token " + token, e);
                onSessionLost(token, e);
            }
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onChildMigrated(int token, android.net.IpSecTransform inTransform, android.net.IpSecTransform outTransform) {
            if (!isActiveToken(token)) {
                com.android.server.connectivity.Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onChildMigrated obsolete token=" + token);
                android.util.Log.d(TAG, "onChildMigrated for obsolete token " + token);
                return;
            }
            com.android.server.connectivity.Vpn.this.mEventChanges.log("[IKEEvent-" + this.mSessionKey + "] onChildMigrated token=" + token + ", in=" + inTransform + ", out=" + outTransform);
            android.net.Network network = this.mIkeConnectionInfo.getNetwork();
            try {
                synchronized (com.android.server.connectivity.Vpn.this) {
                    if (com.android.server.connectivity.Vpn.this.mVpnRunner != this) {
                        return;
                    }
                    android.net.LinkProperties oldLp = com.android.server.connectivity.Vpn.this.makeLinkProperties();
                    com.android.server.connectivity.Vpn.this.mConfig.underlyingNetworks = new android.net.Network[]{network};
                    com.android.server.connectivity.Vpn.this.mConfig.mtu = calculateVpnMtu();
                    android.net.LinkProperties newLp = com.android.server.connectivity.Vpn.this.makeLinkProperties();
                    if (newLp.getLinkAddresses().isEmpty()) {
                        onSessionLost(token, new android.net.ipsec.ike.exceptions.IkeIOException(new java.io.IOException("No valid addresses for MTU < 1280")));
                        return;
                    }
                    java.util.Set<android.net.LinkAddress> removedAddrs = new java.util.HashSet<>(oldLp.getLinkAddresses());
                    removedAddrs.removeAll(newLp.getLinkAddresses());
                    if (!removedAddrs.isEmpty()) {
                        com.android.server.connectivity.Vpn.this.startNewNetworkAgent(com.android.server.connectivity.Vpn.this.mNetworkAgent, "MTU too low for IPv6; restarting network agent");
                        for (android.net.LinkAddress removed : removedAddrs) {
                            this.mTunnelIface.removeAddress(removed.getAddress(), removed.getPrefixLength());
                        }
                    } else if (!newLp.equals(oldLp)) {
                        com.android.server.connectivity.Vpn.doSendLinkProperties(com.android.server.connectivity.Vpn.this.mNetworkAgent, newLp);
                    }
                    this.mTunnelIface.setUnderlyingNetwork(network);
                    this.mIpSecManager.applyTunnelModeTransform(this.mTunnelIface, 0, inTransform);
                    this.mIpSecManager.applyTunnelModeTransform(this.mTunnelIface, 1, outTransform);
                }
            } catch (java.io.IOException | java.lang.IllegalArgumentException e) {
                android.util.Log.d(TAG, "Transform application failed for token " + token, e);
                onSessionLost(token, e);
            }
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onDefaultNetworkChanged(android.net.Network network) {
            com.android.server.connectivity.Vpn.this.mEventChanges.log("[UnderlyingNW] Default network changed to " + network);
            android.util.Log.d(TAG, "onDefaultNetworkChanged: " + network);
            cancelRetryNewIkeSessionFuture();
            cancelHandleNetworkLostTimeout();
            if (!this.mIsRunning) {
                android.util.Log.d(TAG, "onDefaultNetworkChanged after exit");
                return;
            }
            this.mActiveNetwork = network;
            this.mUnderlyingLinkProperties = null;
            this.mUnderlyingNetworkCapabilities = null;
            this.mRetryCount = 0;
        }

        private android.net.ipsec.ike.IkeSessionParams getIkeSessionParams(android.net.Network underlyingNetwork) {
            android.net.ipsec.ike.IkeSessionParams.Builder builder;
            android.net.ipsec.ike.IkeTunnelConnectionParams ikeTunConnParams = this.mProfile.getIkeTunnelConnectionParams();
            if (ikeTunConnParams != null) {
                builder = new android.net.ipsec.ike.IkeSessionParams.Builder(ikeTunConnParams.getIkeSessionParams()).setNetwork(underlyingNetwork);
            } else {
                builder = com.android.server.connectivity.VpnIkev2Utils.makeIkeSessionParamsBuilder(com.android.server.connectivity.Vpn.this.mContext, this.mProfile, underlyingNetwork);
            }
            if (this.mProfile.isAutomaticNattKeepaliveTimerEnabled()) {
                builder.setNattKeepAliveDelaySeconds(guessNattKeepaliveTimerForNetwork());
            }
            if (this.mProfile.isAutomaticIpVersionSelectionEnabled()) {
                builder.setIpVersion(guessEspIpVersionForNetwork());
                builder.setEncapType(guessEspEncapTypeForNetwork());
            }
            return builder.build();
        }

        private android.net.ipsec.ike.ChildSessionParams getChildSessionParams() {
            android.net.ipsec.ike.IkeTunnelConnectionParams ikeTunConnParams = this.mProfile.getIkeTunnelConnectionParams();
            if (ikeTunConnParams != null) {
                return ikeTunConnParams.getTunnelModeChildSessionParams();
            }
            return com.android.server.connectivity.VpnIkev2Utils.buildChildSessionParams(this.mProfile.getAllowedAlgorithms());
        }

        private int calculateVpnMtu() throws java.net.SocketException {
            android.net.Network underlyingNetwork = this.mIkeConnectionInfo.getNetwork();
            android.net.LinkProperties lp = com.android.server.connectivity.Vpn.this.mConnectivityManager.getLinkProperties(underlyingNetwork);
            if (underlyingNetwork == null || lp == null) {
                return this.mProfile.getMaxMtu();
            }
            int underlyingMtu = lp.getMtu();
            if (underlyingMtu == 0) {
                try {
                    underlyingMtu = com.android.server.connectivity.Vpn.this.mDeps.getJavaNetworkInterfaceMtu(lp.getInterfaceName(), this.mProfile.getMaxMtu());
                } catch (java.net.SocketException e) {
                    android.util.Log.d(TAG, "Got a SocketException when getting MTU from kernel: " + e);
                    return this.mProfile.getMaxMtu();
                }
            }
            return com.android.server.connectivity.Vpn.this.mDeps.calculateVpnMtu(getChildSessionParams().getSaProposals(), this.mProfile.getMaxMtu(), underlyingMtu, this.mIkeConnectionInfo.getLocalAddress() instanceof java.net.Inet4Address);
        }

        private void startOrMigrateIkeSession(android.net.Network underlyingNetwork) {
            synchronized (com.android.server.connectivity.Vpn.this) {
                if (com.android.server.connectivity.Vpn.this.mVpnRunner != this) {
                    return;
                }
                com.android.server.connectivity.Vpn.this.setVpnNetworkPreference(this.mSessionKey, com.android.server.connectivity.Vpn.this.createUserAndRestrictedProfilesRanges(com.android.server.connectivity.Vpn.this.mUserId, com.android.server.connectivity.Vpn.this.mConfig.allowedApplications, com.android.server.connectivity.Vpn.this.mConfig.disallowedApplications));
                if (underlyingNetwork == null) {
                    android.util.Log.d(TAG, "There is no active network for starting an IKE session");
                    return;
                }
                java.util.List<android.net.Network> networks = java.util.Collections.singletonList(underlyingNetwork);
                if (!networks.equals(com.android.server.connectivity.Vpn.this.mNetworkCapabilities.getUnderlyingNetworks())) {
                    com.android.server.connectivity.Vpn.this.mNetworkCapabilities = new android.net.NetworkCapabilities.Builder(com.android.server.connectivity.Vpn.this.mNetworkCapabilities).setUnderlyingNetworks(networks).build();
                    if (com.android.server.connectivity.Vpn.this.mNetworkAgent != null) {
                        com.android.server.connectivity.Vpn.this.doSetUnderlyingNetworks(com.android.server.connectivity.Vpn.this.mNetworkAgent, networks);
                    }
                }
                if (maybeMigrateIkeSessionAndUpdateVpnTransportInfo(underlyingNetwork)) {
                    return;
                }
                startIkeSession(underlyingNetwork);
            }
        }

        private int guessEspIpVersionForNetwork() {
            if (this.mUnderlyingNetworkCapabilities.getTransportInfo() instanceof android.net.vcn.VcnTransportInfo) {
                android.util.Log.d(TAG, "Running over VCN, esp IP version is auto");
                return 0;
            }
            com.android.server.connectivity.Vpn.CarrierConfigInfo carrierconfig = getCarrierConfigForUnderlyingNetwork();
            int ipVersion = carrierconfig != null ? carrierconfig.ipVersion : 0;
            if (carrierconfig != null) {
                android.util.Log.d(TAG, "Get customized IP version (" + ipVersion + ") on SIM (mccmnc=" + carrierconfig.mccMnc + ")");
            }
            return ipVersion;
        }

        private int guessEspEncapTypeForNetwork() {
            if (this.mUnderlyingNetworkCapabilities.getTransportInfo() instanceof android.net.vcn.VcnTransportInfo) {
                android.util.Log.d(TAG, "Running over VCN, encap type is auto");
                return 0;
            }
            com.android.server.connectivity.Vpn.CarrierConfigInfo carrierconfig = getCarrierConfigForUnderlyingNetwork();
            int encapType = carrierconfig != null ? carrierconfig.encapType : 0;
            if (carrierconfig != null) {
                android.util.Log.d(TAG, "Get customized encap type (" + encapType + ") on SIM (mccmnc=" + carrierconfig.mccMnc + ")");
            }
            return encapType;
        }

        private int guessNattKeepaliveTimerForNetwork() {
            android.net.vcn.VcnTransportInfo transportInfo = this.mUnderlyingNetworkCapabilities.getTransportInfo();
            if (transportInfo instanceof android.net.vcn.VcnTransportInfo) {
                int nattKeepaliveSec = transportInfo.getMinUdpPort4500NatTimeoutSeconds();
                android.util.Log.d(TAG, "Running over VCN, keepalive timer : " + nattKeepaliveSec + "s");
                if (-1 != nattKeepaliveSec) {
                    return nattKeepaliveSec;
                }
            }
            com.android.server.connectivity.Vpn.CarrierConfigInfo carrierconfig = getCarrierConfigForUnderlyingNetwork();
            int nattKeepaliveSec2 = carrierconfig != null ? carrierconfig.keepaliveDelaySec : 30;
            if (carrierconfig != null) {
                android.util.Log.d(TAG, "Get customized keepalive (" + nattKeepaliveSec2 + "s) on SIM (mccmnc=" + carrierconfig.mccMnc + ")");
            }
            return nattKeepaliveSec2;
        }

        private com.android.server.connectivity.Vpn.CarrierConfigInfo getCarrierConfigForUnderlyingNetwork() {
            int subId = com.android.server.connectivity.Vpn.getCellSubIdForNetworkCapabilities(this.mUnderlyingNetworkCapabilities);
            if (subId == -1) {
                android.util.Log.d(TAG, "Underlying network is not a cellular network");
                return null;
            }
            synchronized (com.android.server.connectivity.Vpn.this) {
                if (com.android.server.connectivity.Vpn.this.mCachedCarrierConfigInfoPerSubId.contains(subId)) {
                    android.util.Log.d(TAG, "Get cached config");
                    return (com.android.server.connectivity.Vpn.CarrierConfigInfo) com.android.server.connectivity.Vpn.this.mCachedCarrierConfigInfoPerSubId.get(subId);
                }
                android.telephony.TelephonyManager perSubTm = com.android.server.connectivity.Vpn.this.mTelephonyManager.createForSubscriptionId(subId);
                if (perSubTm.getSimApplicationState() != 10) {
                    android.util.Log.d(TAG, "SIM card is not ready on sub " + subId);
                    return null;
                }
                android.os.PersistableBundle carrierConfig = com.android.server.connectivity.Vpn.this.mCarrierConfigManager.getConfigForSubId(subId);
                if (!android.telephony.CarrierConfigManager.isConfigForIdentifiedCarrier(carrierConfig)) {
                    return null;
                }
                int natKeepalive = carrierConfig.getInt("min_udp_port_4500_nat_timeout_sec_int");
                int preferredIpProtocol = carrierConfig.getInt("preferred_ike_protocol_int", -1);
                java.lang.String mccMnc = perSubTm.getSimOperator(subId);
                com.android.server.connectivity.Vpn.CarrierConfigInfo info = buildCarrierConfigInfo(mccMnc, natKeepalive, preferredIpProtocol);
                synchronized (com.android.server.connectivity.Vpn.this) {
                    com.android.server.connectivity.Vpn.this.mCachedCarrierConfigInfoPerSubId.put(subId, info);
                }
                return info;
            }
        }

        private com.android.server.connectivity.Vpn.CarrierConfigInfo buildCarrierConfigInfo(java.lang.String mccMnc, int natKeepalive, int preferredIpPortocol) {
            int ipVersion;
            int encapType;
            switch (preferredIpPortocol) {
                case 0:
                    ipVersion = 0;
                    encapType = 0;
                    break;
                case 40:
                    ipVersion = 4;
                    encapType = 17;
                    break;
                case 60:
                    ipVersion = 6;
                    encapType = 17;
                    break;
                case 61:
                    ipVersion = 6;
                    encapType = -1;
                    break;
                default:
                    ipVersion = 4;
                    encapType = 17;
                    break;
            }
            return new com.android.server.connectivity.Vpn.CarrierConfigInfo(mccMnc, natKeepalive, encapType, ipVersion);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getOrGuessKeepaliveDelaySeconds() {
            if (this.mProfile.isAutomaticNattKeepaliveTimerEnabled()) {
                return guessNattKeepaliveTimerForNetwork();
            }
            if (this.mProfile.getIkeTunnelConnectionParams() != null) {
                return this.mProfile.getIkeTunnelConnectionParams().getIkeSessionParams().getNattKeepAliveDelaySeconds();
            }
            return 300;
        }

        boolean maybeMigrateIkeSessionAndUpdateVpnTransportInfo(android.net.Network underlyingNetwork) {
            int keepaliveDelaySec = getOrGuessKeepaliveDelaySeconds();
            boolean migrated = maybeMigrateIkeSession(underlyingNetwork, keepaliveDelaySec);
            if (migrated) {
                updateVpnTransportInfoAndNetCap(keepaliveDelaySec);
            }
            return migrated;
        }

        public void updateVpnTransportInfoAndNetCap(int keepaliveDelaySec) {
            android.net.VpnTransportInfo info;
            synchronized (com.android.server.connectivity.Vpn.this) {
                info = new android.net.VpnTransportInfo(com.android.server.connectivity.Vpn.this.getActiveVpnType(), com.android.server.connectivity.Vpn.this.mConfig.session, com.android.server.connectivity.Vpn.this.mConfig.allowBypass && !com.android.server.connectivity.Vpn.this.mLockdown, com.android.server.connectivity.Vpn.areLongLivedTcpConnectionsExpensive(keepaliveDelaySec));
            }
            boolean ncUpdateRequired = !info.equals(com.android.server.connectivity.Vpn.this.mNetworkCapabilities.getTransportInfo());
            if (ncUpdateRequired) {
                com.android.server.connectivity.Vpn.this.mNetworkCapabilities = new android.net.NetworkCapabilities.Builder(com.android.server.connectivity.Vpn.this.mNetworkCapabilities).setTransportInfo(info).build();
                com.android.server.connectivity.Vpn.this.mEventChanges.log("[VPNRunner] Update agent caps " + com.android.server.connectivity.Vpn.this.mNetworkCapabilities);
                com.android.server.connectivity.Vpn.doSendNetworkCapabilities(com.android.server.connectivity.Vpn.this.mNetworkAgent, com.android.server.connectivity.Vpn.this.mNetworkCapabilities);
            }
        }

        private boolean maybeMigrateIkeSession(android.net.Network underlyingNetwork, int keepaliveDelaySeconds) {
            int ipVersion;
            int encapType;
            if (this.mSession == null || !this.mMobikeEnabled) {
                return false;
            }
            android.util.Log.d(TAG, "Migrate IKE Session with token " + this.mCurrentToken + " to network " + underlyingNetwork);
            if (this.mProfile.isAutomaticIpVersionSelectionEnabled()) {
                ipVersion = guessEspIpVersionForNetwork();
                encapType = guessEspEncapTypeForNetwork();
            } else if (this.mProfile.getIkeTunnelConnectionParams() != null) {
                ipVersion = this.mProfile.getIkeTunnelConnectionParams().getIkeSessionParams().getIpVersion();
                encapType = this.mProfile.getIkeTunnelConnectionParams().getIkeSessionParams().getEncapType();
            } else {
                ipVersion = 0;
                encapType = 0;
            }
            this.mSession.setNetwork(underlyingNetwork, ipVersion, encapType, keepaliveDelaySeconds);
            return true;
        }

        private void startIkeSession(android.net.Network underlyingNetwork) {
            android.util.Log.d(TAG, "Start new IKE session on network " + underlyingNetwork);
            com.android.server.connectivity.Vpn.this.mEventChanges.log("[IKE] Start IKE session over " + underlyingNetwork);
            try {
                synchronized (com.android.server.connectivity.Vpn.this) {
                    if (com.android.server.connectivity.Vpn.this.mVpnRunner != this) {
                        return;
                    }
                    com.android.server.connectivity.Vpn.this.mInterface = null;
                    resetIkeState();
                    java.net.InetAddress address = java.net.InetAddress.getLocalHost();
                    this.mTunnelIface = this.mIpSecManager.createIpSecTunnelInterface(address, address, underlyingNetwork);
                    com.android.net.module.util.NetdUtils.setInterfaceUp(com.android.server.connectivity.Vpn.this.mNetd, this.mTunnelIface.getInterfaceName());
                    int token = this.mCurrentToken + 1;
                    this.mCurrentToken = token;
                    this.mSession = com.android.server.connectivity.Vpn.this.mIkev2SessionCreator.createIkeSession(com.android.server.connectivity.Vpn.this.mContext, getIkeSessionParams(underlyingNetwork), getChildSessionParams(), this.mExecutor, new com.android.server.connectivity.VpnIkev2Utils.IkeSessionCallbackImpl(TAG, this, token), new com.android.server.connectivity.VpnIkev2Utils.ChildSessionCallbackImpl(TAG, this, token));
                    android.util.Log.d(TAG, "IKE session started for token " + token);
                }
            } catch (java.lang.Exception e) {
                android.util.Log.i(TAG, "Setup failed for token " + this.mCurrentToken + ". Aborting", e);
                onSessionLost(this.mCurrentToken, e);
            }
        }

        private void scheduleStartIkeSession(long delayMs) {
            long retryDelayMs;
            if (this.mScheduledHandleRetryIkeSessionFuture != null) {
                android.util.Log.d(TAG, "There is a pending retrying task, skip the new retrying task");
                return;
            }
            if (-1 != delayMs) {
                retryDelayMs = delayMs;
            } else {
                com.android.server.connectivity.Vpn.Dependencies dependencies = com.android.server.connectivity.Vpn.this.mDeps;
                int i = this.mRetryCount;
                this.mRetryCount = i + 1;
                retryDelayMs = dependencies.getNextRetryDelayMs(i);
            }
            android.util.Log.d(TAG, "Retry new IKE session after " + retryDelayMs + " milliseconds.");
            this.mScheduledHandleRetryIkeSessionFuture = this.mExecutor.schedule(new java.lang.Runnable() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleStartIkeSession$1();
                }
            }, retryDelayMs, java.util.concurrent.TimeUnit.MILLISECONDS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$scheduleStartIkeSession$1() {
            startOrMigrateIkeSession(this.mActiveNetwork);
            this.mScheduledHandleRetryIkeSessionFuture = null;
        }

        private boolean significantCapsChange(android.net.NetworkCapabilities left, android.net.NetworkCapabilities right) {
            if (left == right) {
                return false;
            }
            return (left != null && right != null && java.util.Arrays.equals(left.getTransportTypes(), right.getTransportTypes()) && java.util.Arrays.equals(left.getCapabilities(), right.getCapabilities()) && java.util.Arrays.equals(left.getEnterpriseIds(), right.getEnterpriseIds()) && java.util.Objects.equals(left.getTransportInfo(), right.getTransportInfo()) && java.util.Objects.equals(left.getAllowedUids(), right.getAllowedUids()) && java.util.Objects.equals(left.getUnderlyingNetworks(), right.getUnderlyingNetworks()) && java.util.Objects.equals(left.getNetworkSpecifier(), right.getNetworkSpecifier())) ? false : true;
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onDefaultNetworkCapabilitiesChanged(android.net.NetworkCapabilities nc) {
            if (significantCapsChange(this.mUnderlyingNetworkCapabilities, nc)) {
                com.android.server.connectivity.Vpn.this.mEventChanges.log("[UnderlyingNW] Cap changed from " + this.mUnderlyingNetworkCapabilities + " to " + nc);
            }
            android.net.NetworkCapabilities oldNc = this.mUnderlyingNetworkCapabilities;
            this.mUnderlyingNetworkCapabilities = nc;
            if (oldNc == null || !nc.getSubscriptionIds().equals(oldNc.getSubscriptionIds())) {
                scheduleStartIkeSession(com.android.server.connectivity.Vpn.IKE_DELAY_ON_NC_LP_CHANGE_MS);
            }
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onDefaultNetworkLinkPropertiesChanged(android.net.LinkProperties lp) {
            android.net.LinkProperties oldLp = this.mUnderlyingLinkProperties;
            com.android.server.connectivity.Vpn.this.mEventChanges.log("[UnderlyingNW] Lp changed from " + oldLp + " to " + lp);
            this.mUnderlyingLinkProperties = lp;
            if (oldLp == null || !com.android.net.module.util.LinkPropertiesUtils.isIdenticalAllLinkAddresses(oldLp, lp)) {
                scheduleStartIkeSession(com.android.server.connectivity.Vpn.IKE_DELAY_ON_NC_LP_CHANGE_MS);
            }
        }

        public void onValidationStatus(int status) {
            com.android.server.connectivity.Vpn.this.mEventChanges.log("[Validation] validation status " + status);
            if (status == 1) {
                this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onValidationStatus$2();
                    }
                });
                return;
            }
            if (this.mScheduledHandleDataStallFuture != null) {
                return;
            }
            if (this.mValidationFailRetryCount == 0) {
                com.android.server.connectivity.Vpn.this.mConnectivityManager.reportNetworkConnectivity(this.mActiveNetwork, false);
            }
            if (this.mValidationFailRetryCount < 2) {
                android.util.Log.d(TAG, "Validation failed");
                java.util.concurrent.ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = this.mExecutor;
                java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onValidationStatus$3();
                    }
                };
                com.android.server.connectivity.Vpn.Dependencies dependencies = com.android.server.connectivity.Vpn.this.mDeps;
                int i = this.mValidationFailRetryCount;
                this.mValidationFailRetryCount = i + 1;
                scheduledThreadPoolExecutor.schedule(runnable, dependencies.getValidationFailRecoveryMs(i), java.util.concurrent.TimeUnit.MILLISECONDS);
                return;
            }
            java.util.concurrent.ScheduledThreadPoolExecutor scheduledThreadPoolExecutor2 = this.mExecutor;
            java.lang.Runnable runnable2 = new java.lang.Runnable() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onValidationStatus$4();
                }
            };
            com.android.server.connectivity.Vpn.Dependencies dependencies2 = com.android.server.connectivity.Vpn.this.mDeps;
            int i2 = this.mValidationFailRetryCount;
            this.mValidationFailRetryCount = i2 + 1;
            this.mScheduledHandleDataStallFuture = scheduledThreadPoolExecutor2.schedule(runnable2, dependencies2.getValidationFailRecoveryMs(i2), java.util.concurrent.TimeUnit.MILLISECONDS);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onValidationStatus$2() {
            this.mValidationFailRetryCount = 0;
            if (this.mScheduledHandleDataStallFuture != null) {
                android.util.Log.d(TAG, "Recovered from stall. Cancel pending reset action.");
                this.mScheduledHandleDataStallFuture.cancel(false);
                this.mScheduledHandleDataStallFuture = null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onValidationStatus$3() {
            maybeMigrateIkeSessionAndUpdateVpnTransportInfo(this.mActiveNetwork);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onValidationStatus$4() {
            if (this.mValidationFailRetryCount > 0) {
                android.util.Log.d(TAG, "Reset session to recover stalled network");
                startIkeSession(this.mActiveNetwork);
            }
            this.mScheduledHandleDataStallFuture = null;
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onDefaultNetworkLost(final android.net.Network network) {
            com.android.server.connectivity.Vpn.this.mEventChanges.log("[UnderlyingNW] Network lost " + network);
            cancelRetryNewIkeSessionFuture();
            if (!isActiveNetwork(network)) {
                android.util.Log.d(TAG, "onDefaultNetworkLost called for obsolete network " + network);
                return;
            }
            this.mActiveNetwork = null;
            this.mUnderlyingNetworkCapabilities = null;
            this.mUnderlyingLinkProperties = null;
            if (this.mScheduledHandleNetworkLostFuture != null) {
                java.lang.IllegalStateException exception = new java.lang.IllegalStateException("Found a pending mScheduledHandleNetworkLostFuture");
                android.util.Log.i(TAG, "Unexpected error in onDefaultNetworkLost. Tear down session", exception);
                handleSessionLost(exception, network);
            } else {
                android.util.Log.d(TAG, "Schedule a delay handleSessionLost for losing network " + network + " on session with token " + this.mCurrentToken);
                final int token = this.mCurrentToken;
                this.mScheduledHandleNetworkLostFuture = this.mExecutor.schedule(new java.lang.Runnable() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onDefaultNetworkLost$5(token, network);
                    }
                }, NETWORK_LOST_TIMEOUT_MS, java.util.concurrent.TimeUnit.MILLISECONDS);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDefaultNetworkLost$5(int token, android.net.Network network) {
            if (isActiveToken(token)) {
                handleSessionLost(new android.net.ipsec.ike.exceptions.IkeNetworkLostException(network), network);
                synchronized (com.android.server.connectivity.Vpn.this) {
                    if (com.android.server.connectivity.Vpn.this.mVpnRunner != this) {
                        return;
                    } else {
                        com.android.server.connectivity.Vpn.this.updateState(android.net.NetworkInfo.DetailedState.DISCONNECTED, "Network lost");
                    }
                }
            } else {
                android.util.Log.d(TAG, "Scheduled handleSessionLost fired for obsolete token " + token);
            }
            this.mScheduledHandleNetworkLostFuture = null;
        }

        private void cancelHandleNetworkLostTimeout() {
            if (this.mScheduledHandleNetworkLostFuture != null) {
                android.util.Log.d(TAG, "Cancel the task for handling network lost timeout");
                this.mScheduledHandleNetworkLostFuture.cancel(false);
                this.mScheduledHandleNetworkLostFuture = null;
            }
        }

        private void cancelRetryNewIkeSessionFuture() {
            if (this.mScheduledHandleRetryIkeSessionFuture != null) {
                android.util.Log.d(TAG, "Cancel the task for handling new ike session timeout");
                this.mScheduledHandleRetryIkeSessionFuture.cancel(false);
                this.mScheduledHandleRetryIkeSessionFuture = null;
            }
        }

        private void markFailedAndDisconnect(java.lang.Exception exception) {
            synchronized (com.android.server.connectivity.Vpn.this) {
                if (com.android.server.connectivity.Vpn.this.mVpnRunner != this) {
                    return;
                }
                com.android.server.connectivity.Vpn.this.updateState(android.net.NetworkInfo.DetailedState.FAILED, exception.getMessage());
                com.android.server.connectivity.Vpn.this.clearVpnNetworkPreference(this.mSessionKey);
                lambda$exitVpnRunner$6();
            }
        }

        @Override // com.android.server.connectivity.Vpn.IkeV2VpnRunnerCallback
        public void onSessionLost(int token, java.lang.Exception exception) {
            com.android.server.connectivity.Vpn.this.mEventChanges.log("[IKE] Session lost on network " + this.mActiveNetwork + (exception == null ? "" : " reason " + exception.getMessage()));
            android.util.Log.d(TAG, "onSessionLost() called for token " + token);
            if (!isActiveToken(token)) {
                android.util.Log.d(TAG, "onSessionLost() called for obsolete token " + token);
            } else {
                handleSessionLost(exception, this.mActiveNetwork);
            }
        }

        private void handleSessionLost(java.lang.Exception exception, android.net.Network network) {
            int errorCode;
            int errorCode2;
            java.lang.String category;
            int errorClass;
            cancelHandleNetworkLostTimeout();
            if (exception instanceof java.lang.IllegalArgumentException) {
                markFailedAndDisconnect(exception);
                return;
            }
            if (exception instanceof android.net.ipsec.ike.exceptions.IkeProtocolException) {
                android.net.ipsec.ike.exceptions.IkeProtocolException ikeException = (android.net.ipsec.ike.exceptions.IkeProtocolException) exception;
                int errorCode3 = ikeException.getErrorType();
                switch (ikeException.getErrorType()) {
                    case 14:
                    case 17:
                    case 24:
                    case 34:
                    case 37:
                    case 38:
                        errorClass = 1;
                        break;
                    default:
                        errorClass = 2;
                        break;
                }
                errorCode = errorCode3;
                errorCode2 = errorClass;
                category = "android.net.category.EVENT_IKE_ERROR";
            } else if (exception instanceof android.net.ipsec.ike.exceptions.IkeNetworkLostException) {
                errorCode = 2;
                errorCode2 = 2;
                category = "android.net.category.EVENT_NETWORK_ERROR";
            } else if (exception instanceof android.net.ipsec.ike.exceptions.IkeNonProtocolException) {
                if (exception.getCause() instanceof java.net.UnknownHostException) {
                    errorCode = 0;
                    errorCode2 = 2;
                    category = "android.net.category.EVENT_NETWORK_ERROR";
                } else if (exception.getCause() instanceof android.net.ipsec.ike.exceptions.IkeTimeoutException) {
                    errorCode = 1;
                    errorCode2 = 2;
                    category = "android.net.category.EVENT_NETWORK_ERROR";
                } else if (!(exception.getCause() instanceof java.io.IOException)) {
                    errorCode = -1;
                    errorCode2 = 2;
                    category = "android.net.category.EVENT_NETWORK_ERROR";
                } else {
                    errorCode = 3;
                    errorCode2 = 2;
                    category = "android.net.category.EVENT_NETWORK_ERROR";
                }
            } else {
                if (exception != null) {
                    android.util.Log.wtf(TAG, "onSessionLost: exception = " + exception);
                }
                errorCode = -1;
                errorCode2 = -1;
                category = null;
            }
            synchronized (com.android.server.connectivity.Vpn.this) {
                if (com.android.server.connectivity.Vpn.this.mVpnRunner != this) {
                    return;
                }
                if (category != null && com.android.server.connectivity.Vpn.isVpnApp(com.android.server.connectivity.Vpn.this.mPackage)) {
                    com.android.server.connectivity.Vpn.this.sendEventToVpnManagerApp(category, errorCode2, errorCode, com.android.server.connectivity.Vpn.this.getPackage(), this.mSessionKey, com.android.server.connectivity.Vpn.this.makeVpnProfileStateLocked(), this.mActiveNetwork, com.android.server.connectivity.Vpn.this.getRedactedNetworkCapabilities(this.mUnderlyingNetworkCapabilities), com.android.server.connectivity.Vpn.this.getRedactedLinkProperties(this.mUnderlyingLinkProperties));
                }
                if (errorCode2 != 1) {
                    scheduleStartIkeSession(-1L);
                    android.util.Log.d(TAG, "Resetting state for token: " + this.mCurrentToken);
                    synchronized (com.android.server.connectivity.Vpn.this) {
                        if (com.android.server.connectivity.Vpn.this.mVpnRunner != this) {
                            return;
                        }
                        com.android.server.connectivity.Vpn.this.mInterface = null;
                        if (com.android.server.connectivity.Vpn.this.mConfig != null) {
                            com.android.server.connectivity.Vpn.this.mConfig.interfaze = null;
                            if (com.android.server.connectivity.Vpn.this.mConfig.routes != null) {
                                java.util.List<android.net.RouteInfo> oldRoutes = new java.util.ArrayList<>(com.android.server.connectivity.Vpn.this.mConfig.routes);
                                com.android.server.connectivity.Vpn.this.mConfig.routes.clear();
                                for (android.net.RouteInfo route : oldRoutes) {
                                    com.android.server.connectivity.Vpn.this.mConfig.routes.add(new android.net.RouteInfo(route.getDestination(), null, null, 7));
                                }
                                if (com.android.server.connectivity.Vpn.this.mNetworkAgent != null) {
                                    com.android.server.connectivity.Vpn.doSendLinkProperties(com.android.server.connectivity.Vpn.this.mNetworkAgent, com.android.server.connectivity.Vpn.this.makeLinkProperties());
                                }
                            }
                        }
                        resetIkeState();
                        if (errorCode != 2 && com.android.server.connectivity.Vpn.this.mDeps.getNextRetryDelayMs(this.mRetryCount - 1) > NETWORK_LOST_TIMEOUT_MS) {
                            com.android.server.connectivity.Vpn.this.clearVpnNetworkPreference(this.mSessionKey);
                            return;
                        }
                        return;
                    }
                }
                markFailedAndDisconnect(exception);
            }
        }

        private void resetIkeState() {
            if (this.mTunnelIface != null) {
                this.mTunnelIface.close();
                this.mTunnelIface = null;
            }
            if (this.mSession != null) {
                this.mSession.kill();
                this.mSession = null;
            }
            this.mIkeConnectionInfo = null;
            this.mMobikeEnabled = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: disconnectVpnRunner, reason: merged with bridge method [inline-methods] */
        public void lambda$exitVpnRunner$6() {
            com.android.server.connectivity.Vpn.this.mEventChanges.log("[VPNRunner] Disconnect runner, underlying net " + this.mActiveNetwork);
            this.mActiveNetwork = null;
            this.mUnderlyingNetworkCapabilities = null;
            this.mUnderlyingLinkProperties = null;
            this.mIsRunning = false;
            resetIkeState();
            com.android.server.connectivity.Vpn.this.mCarrierConfigManager.unregisterCarrierConfigChangeListener(this.mCarrierConfigChangeListener);
            com.android.server.connectivity.Vpn.this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
            this.mExecutor.shutdown();
        }

        @Override // com.android.server.connectivity.Vpn.VpnRunner
        public void exitVpnRunner() {
            com.android.server.connectivity.Vpn.this.clearVpnNetworkPreference(this.mSessionKey);
            try {
                this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.connectivity.Vpn$IkeV2VpnRunner$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$exitVpnRunner$6();
                    }
                });
            } catch (java.util.concurrent.RejectedExecutionException e) {
            }
        }
    }

    private void verifyCallingUidAndPackage(java.lang.String packageName) {
        this.mDeps.verifyCallingUidAndPackage(this.mContext, packageName, this.mUserId);
    }

    java.lang.String getProfileNameForPackage(java.lang.String packageName) {
        return "PLATFORM_VPN_" + this.mUserId + "_" + packageName;
    }

    void validateRequiredFeatures(com.android.internal.net.VpnProfile profile) {
        switch (profile.type) {
            case 6:
            case 7:
            case 8:
            case 9:
                if (!this.mContext.getPackageManager().hasSystemFeature("android.software.ipsec_tunnels")) {
                    throw new java.lang.UnsupportedOperationException("Ikev2VpnProfile(s) requires PackageManager.FEATURE_IPSEC_TUNNELS");
                }
                return;
            default:
                return;
        }
    }

    public synchronized boolean provisionVpnProfile(java.lang.String packageName, com.android.internal.net.VpnProfile profile) {
        java.util.Objects.requireNonNull(packageName, "No package name provided");
        java.util.Objects.requireNonNull(profile, "No profile provided");
        verifyCallingUidAndPackage(packageName);
        enforceNotRestrictedUser();
        validateRequiredFeatures(profile);
        if (profile.isRestrictedToTestNetworks) {
            this.mContext.enforceCallingPermission("android.permission.MANAGE_TEST_NETWORKS", "Test-mode profiles require the MANAGE_TEST_NETWORKS permission");
        }
        byte[] encodedProfile = profile.encode();
        if (encodedProfile.length > 131072) {
            throw new java.lang.IllegalArgumentException("Profile too big");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            getVpnProfileStore().put(getProfileNameForPackage(packageName), encodedProfile);
            android.os.Binder.restoreCallingIdentity(token);
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
        return isVpnProfilePreConsented(this.mContext, packageName);
    }

    private boolean isCurrentIkev2VpnLocked(java.lang.String packageName) {
        return isCurrentPreparedPackage(packageName) && isIkev2VpnRunner();
    }

    public synchronized void deleteVpnProfile(java.lang.String packageName) {
        java.util.Objects.requireNonNull(packageName, "No package name provided");
        verifyCallingUidAndPackage(packageName);
        enforceNotRestrictedUser();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (isCurrentIkev2VpnLocked(packageName)) {
                try {
                    if (this.mAlwaysOn) {
                        setAlwaysOnPackage(null, false, null);
                    } else {
                        prepareInternal("[Legacy VPN]");
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    android.os.Binder.restoreCallingIdentity(token);
                    throw th;
                }
            }
            getVpnProfileStore().remove(getProfileNameForPackage(packageName));
            android.os.Binder.restoreCallingIdentity(token);
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    com.android.internal.net.VpnProfile getVpnProfilePrivileged(java.lang.String packageName) {
        if (!this.mDeps.isCallerSystem()) {
            android.util.Log.wtf(TAG, "getVpnProfilePrivileged called as non-System UID ");
            return null;
        }
        byte[] encoded = getVpnProfileStore().get(getProfileNameForPackage(packageName));
        if (encoded == null) {
            return null;
        }
        return com.android.internal.net.VpnProfile.decode("", encoded);
    }

    private boolean isIkev2VpnRunner() {
        return this.mVpnRunner instanceof com.android.server.connectivity.Vpn.IkeV2VpnRunner;
    }

    private java.lang.String getSessionKeyLocked() {
        boolean isIkev2VpnRunner = isIkev2VpnRunner();
        java.lang.String sessionKey = isIkev2VpnRunner ? ((com.android.server.connectivity.Vpn.IkeV2VpnRunner) this.mVpnRunner).mSessionKey : null;
        android.util.Log.d(TAG, "getSessionKeyLocked: isIkev2VpnRunner = " + isIkev2VpnRunner + ", sessionKey = " + sessionKey);
        return sessionKey;
    }

    public synchronized java.lang.String startVpnProfile(java.lang.String packageName) {
        java.lang.String sessionKeyLocked;
        java.util.Objects.requireNonNull(packageName, "No package name provided");
        enforceNotRestrictedUser();
        if (!prepare(packageName, null, 2)) {
            throw new java.lang.SecurityException("User consent not granted for package " + packageName);
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.internal.net.VpnProfile profile = getVpnProfilePrivileged(packageName);
            if (profile == null) {
                throw new java.lang.IllegalArgumentException("No profile found for " + packageName);
            }
            startVpnProfilePrivileged(profile, packageName);
            if (!isIkev2VpnRunner()) {
                throw new java.lang.IllegalStateException("mVpnRunner shouldn't be null and should also be an instance of Ikev2VpnRunner");
            }
            try {
                sessionKeyLocked = getSessionKeyLocked();
                android.os.Binder.restoreCallingIdentity(token);
            } catch (java.lang.Throwable th) {
                th = th;
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
        return sessionKeyLocked;
    }

    private synchronized void startVpnProfilePrivileged(com.android.internal.net.VpnProfile profile, java.lang.String packageName) {
        prepareInternal(packageName);
        updateState(android.net.NetworkInfo.DetailedState.CONNECTING, "startPlatformVpn");
        try {
            com.android.internal.net.VpnConfig config = new com.android.internal.net.VpnConfig();
            if ("[Legacy VPN]".equals(packageName)) {
                config.legacy = true;
                config.session = profile.name;
                config.user = profile.key;
                config.isMetered = true;
            } else {
                config.user = packageName;
                config.isMetered = profile.isMetered;
            }
            config.startTime = android.os.SystemClock.elapsedRealtime();
            config.proxyInfo = profile.proxy;
            config.requiresInternetValidation = profile.requiresInternetValidation;
            config.excludeLocalRoutes = profile.excludeLocalRoutes;
            config.allowBypass = profile.isBypassable;
            config.disallowedApplications = getAppExclusionList(this.mPackage);
            this.mConfig = config;
            switch (profile.type) {
                case 6:
                case 7:
                case 8:
                case 9:
                    this.mVpnRunner = new com.android.server.connectivity.Vpn.IkeV2VpnRunner(android.net.Ikev2VpnProfile.fromVpnProfile(profile), this.mDeps.newScheduledThreadPoolExecutor());
                    this.mVpnRunner.start();
                    break;
                default:
                    this.mConfig = null;
                    updateState(android.net.NetworkInfo.DetailedState.FAILED, "Invalid platform VPN type");
                    android.util.Log.d(TAG, "Unknown VPN profile type: " + profile.type);
                    break;
            }
            if (!"[Legacy VPN]".equals(packageName)) {
                this.mAppOpsManager.startOp("android:establish_vpn_manager", this.mOwnerUID, this.mPackage, null, null);
            }
        } catch (java.security.GeneralSecurityException e) {
            this.mConfig = null;
            updateState(android.net.NetworkInfo.DetailedState.FAILED, "VPN startup failed");
            throw new java.lang.IllegalArgumentException("VPN startup failed", e);
        }
    }

    private void stopVpnRunnerAndNotifyAppLocked() {
        int ownerUid = this.mOwnerUID;
        android.content.Intent intent = null;
        if (isVpnApp(this.mPackage)) {
            intent = buildVpnManagerEventIntent("android.net.category.EVENT_DEACTIVATED_BY_USER", -1, -1, this.mPackage, getSessionKeyLocked(), makeVpnProfileStateLocked(), null, null, null);
        }
        this.mVpnRunner.exit();
        if (intent != null && isVpnApp(this.mPackage)) {
            notifyVpnManagerVpnStopped(this.mPackage, ownerUid, intent);
        }
    }

    public synchronized void stopVpnProfile(java.lang.String packageName) {
        java.util.Objects.requireNonNull(packageName, "No package name provided");
        enforceNotRestrictedUser();
        if (isCurrentIkev2VpnLocked(packageName)) {
            stopVpnRunnerAndNotifyAppLocked();
        }
    }

    private synchronized void notifyVpnManagerVpnStopped(java.lang.String packageName, int ownerUID, android.content.Intent intent) {
        this.mAppOpsManager.finishOp("android:establish_vpn_manager", ownerUID, packageName, null);
        this.mEventChanges.log("[VMEvent] " + packageName + " stopped");
        sendEventToVpnManagerApp(intent, packageName);
    }

    private boolean storeAppExclusionList(java.lang.String packageName, java.util.List<java.lang.String> excludedApps) {
        try {
            android.os.PersistableBundle bundle = com.android.server.vcn.util.PersistableBundleUtils.fromList(excludedApps, com.android.server.vcn.util.PersistableBundleUtils.STRING_SERIALIZER);
            byte[] data = com.android.server.vcn.util.PersistableBundleUtils.toDiskStableBytes(bundle);
            long oldId = android.os.Binder.clearCallingIdentity();
            try {
                getVpnProfileStore().put(getVpnAppExcludedForPackage(packageName), data);
                android.os.Binder.restoreCallingIdentity(oldId);
                return true;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(oldId);
                throw th;
            }
        } catch (java.io.IOException e) {
            android.util.Log.e(TAG, "problem writing into stream", e);
            return false;
        }
    }

    java.lang.String getVpnAppExcludedForPackage(java.lang.String packageName) {
        return VPN_APP_EXCLUDED + this.mUserId + "_" + packageName;
    }

    public synchronized boolean setAppExclusionList(java.lang.String packageName, java.util.List<java.lang.String> excludedApps) {
        enforceNotRestrictedUser();
        if (!storeAppExclusionList(packageName, excludedApps)) {
            return false;
        }
        updateAppExclusionList(excludedApps);
        return true;
    }

    public synchronized void refreshPlatformVpnAppExclusionList() {
        updateAppExclusionList(getAppExclusionList(this.mPackage));
    }

    private synchronized void updateAppExclusionList(java.util.List<java.lang.String> excludedApps) {
        if (this.mNetworkAgent != null && isIkev2VpnRunner()) {
            this.mConfig.disallowedApplications = java.util.List.copyOf(excludedApps);
            this.mNetworkCapabilities = new android.net.NetworkCapabilities.Builder(this.mNetworkCapabilities).setUids(createUserAndRestrictedProfilesRanges(this.mUserId, null, excludedApps)).build();
            setVpnNetworkPreference(getSessionKeyLocked(), createUserAndRestrictedProfilesRanges(this.mUserId, this.mConfig.allowedApplications, this.mConfig.disallowedApplications));
            doSendNetworkCapabilities(this.mNetworkAgent, this.mNetworkCapabilities);
        }
    }

    public synchronized java.util.List<java.lang.String> getAppExclusionList(java.lang.String packageName) {
        long oldId = android.os.Binder.clearCallingIdentity();
        try {
            try {
                byte[] bytes = getVpnProfileStore().get(getVpnAppExcludedForPackage(packageName));
                if (bytes != null && bytes.length != 0) {
                    android.os.PersistableBundle bundle = com.android.server.vcn.util.PersistableBundleUtils.fromDiskStableBytes(bytes);
                    return com.android.server.vcn.util.PersistableBundleUtils.toList(bundle, com.android.server.vcn.util.PersistableBundleUtils.STRING_DESERIALIZER);
                }
                return new java.util.ArrayList();
            } catch (java.io.IOException e) {
                android.util.Log.e(TAG, "problem reading from stream", e);
                android.os.Binder.restoreCallingIdentity(oldId);
                return new java.util.ArrayList();
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(oldId);
        }
    }

    private int getStateFromLegacyState(int legacyState) {
        switch (legacyState) {
            case 0:
                break;
            case 1:
            case 4:
            default:
                android.util.Log.wtf(TAG, "Unhandled state " + legacyState + ", treat it as STATE_DISCONNECTED");
                break;
            case 2:
                break;
            case 3:
                break;
            case 5:
                break;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.net.VpnProfileState makeVpnProfileStateLocked() {
        return new android.net.VpnProfileState(getStateFromLegacyState(this.mLegacyState), isIkev2VpnRunner() ? getSessionKeyLocked() : null, this.mAlwaysOn, this.mLockdown);
    }

    private android.net.VpnProfileState makeDisconnectedVpnProfileState() {
        return new android.net.VpnProfileState(0, null, false, false);
    }

    public synchronized android.net.VpnProfileState getProvisionedVpnProfileState(java.lang.String packageName) {
        java.util.Objects.requireNonNull(packageName, "No package name provided");
        enforceNotRestrictedUser();
        return isCurrentIkev2VpnLocked(packageName) ? makeVpnProfileStateLocked() : null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doSendLinkProperties(android.net.NetworkAgent agent, android.net.LinkProperties lp) {
        if (agent instanceof com.android.server.connectivity.Vpn.VpnNetworkAgentWrapper) {
            ((com.android.server.connectivity.Vpn.VpnNetworkAgentWrapper) agent).doSendLinkProperties(lp);
        } else {
            agent.sendLinkProperties(lp);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doSendNetworkCapabilities(android.net.NetworkAgent agent, android.net.NetworkCapabilities nc) {
        if (agent instanceof com.android.server.connectivity.Vpn.VpnNetworkAgentWrapper) {
            ((com.android.server.connectivity.Vpn.VpnNetworkAgentWrapper) agent).doSendNetworkCapabilities(nc);
        } else {
            agent.sendNetworkCapabilities(nc);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSetUnderlyingNetworks(android.net.NetworkAgent agent, java.util.List<android.net.Network> networks) {
        logUnderlyNetworkChanges(networks);
        if (agent instanceof com.android.server.connectivity.Vpn.VpnNetworkAgentWrapper) {
            ((com.android.server.connectivity.Vpn.VpnNetworkAgentWrapper) agent).doSetUnderlyingNetworks(networks);
        } else {
            agent.setUnderlyingNetworks(networks);
        }
    }

    public static class VpnNetworkAgentWrapper extends android.net.NetworkAgent {
        private final com.android.server.connectivity.Vpn.ValidationStatusCallback mCallback;

        public VpnNetworkAgentWrapper(android.content.Context context, android.os.Looper looper, java.lang.String logTag, android.net.NetworkCapabilities nc, android.net.LinkProperties lp, android.net.NetworkScore score, android.net.NetworkAgentConfig config, android.net.NetworkProvider provider, com.android.server.connectivity.Vpn.ValidationStatusCallback callback) {
            super(context, looper, logTag, nc, lp, score, config, provider);
            this.mCallback = callback;
        }

        public void doSendLinkProperties(android.net.LinkProperties lp) {
            sendLinkProperties(lp);
        }

        public void doSendNetworkCapabilities(android.net.NetworkCapabilities nc) {
            sendNetworkCapabilities(nc);
        }

        public void doSetUnderlyingNetworks(java.util.List<android.net.Network> networks) {
            setUnderlyingNetworks(networks);
        }

        public void onNetworkUnwanted() {
        }

        public void onValidationStatus(int status, android.net.Uri redirectUri) {
            if (this.mCallback != null) {
                this.mCallback.onValidationStatus(status);
            }
        }
    }

    public static class IkeSessionWrapper {
        private final android.net.ipsec.ike.IkeSession mImpl;

        public IkeSessionWrapper(android.net.ipsec.ike.IkeSession session) {
            this.mImpl = session;
        }

        public void setNetwork(android.net.Network network, int ipVersion, int encapType, int keepaliveDelaySeconds) {
            this.mImpl.setNetwork(network, ipVersion, encapType, keepaliveDelaySeconds);
        }

        public void setUnderpinnedNetwork(android.net.Network underpinnedNetwork) {
            this.mImpl.setUnderpinnedNetwork(underpinnedNetwork);
        }

        public void kill() {
            this.mImpl.kill();
        }
    }

    public static class Ikev2SessionCreator {
        public com.android.server.connectivity.Vpn.IkeSessionWrapper createIkeSession(android.content.Context context, android.net.ipsec.ike.IkeSessionParams ikeSessionParams, android.net.ipsec.ike.ChildSessionParams firstChildSessionParams, java.util.concurrent.Executor userCbExecutor, android.net.ipsec.ike.IkeSessionCallback ikeSessionCallback, android.net.ipsec.ike.ChildSessionCallback firstChildSessionCallback) {
            return new com.android.server.connectivity.Vpn.IkeSessionWrapper(new android.net.ipsec.ike.IkeSession(context, ikeSessionParams, firstChildSessionParams, userCbExecutor, ikeSessionCallback, firstChildSessionCallback));
        }
    }

    static android.util.Range<java.lang.Integer> createUidRangeForUser(int userId) {
        return new android.util.Range<>(java.lang.Integer.valueOf(userId * 100000), java.lang.Integer.valueOf(((userId + 1) * 100000) - 1));
    }

    private boolean isVpnDisabled() {
        if (this.mVpnExt == null) {
            return false;
        }
        boolean result = this.mVpnExt.isVpnDisabled(null);
        return result;
    }

    private java.lang.String getVpnManagerEventClassName(int code) {
        switch (code) {
            case 1:
                return "ERROR_CLASS_NOT_RECOVERABLE";
            case 2:
                return "ERROR_CLASS_RECOVERABLE";
            default:
                return "UNKNOWN_CLASS";
        }
    }

    private java.lang.String getVpnManagerEventErrorName(int code) {
        switch (code) {
            case 0:
                return "ERROR_CODE_NETWORK_UNKNOWN_HOST";
            case 1:
                return "ERROR_CODE_NETWORK_PROTOCOL_TIMEOUT";
            case 2:
                return "ERROR_CODE_NETWORK_LOST";
            case 3:
                return "ERROR_CODE_NETWORK_IO";
            default:
                return "UNKNOWN_ERROR";
        }
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        synchronized (this) {
            pw.println("Active package name: " + this.mPackage);
            pw.println("Active vpn type: " + getActiveVpnType());
            pw.println("NetworkCapabilities: " + this.mNetworkCapabilities);
            if (isIkev2VpnRunner()) {
                com.android.server.connectivity.Vpn.IkeV2VpnRunner runner = (com.android.server.connectivity.Vpn.IkeV2VpnRunner) this.mVpnRunner;
                pw.println("SessionKey: " + runner.mSessionKey);
                pw.println("MOBIKE " + (runner.mMobikeEnabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED));
                pw.println("Profile: " + runner.mProfile);
                pw.println("Token: " + runner.mCurrentToken);
                pw.println("Validation failed retry count:" + runner.mValidationFailRetryCount);
                if (runner.mScheduledHandleDataStallFuture != null) {
                    pw.println("Reset session scheduled");
                }
            }
            pw.println();
            pw.println("mCachedCarrierConfigInfoPerSubId=" + this.mCachedCarrierConfigInfoPerSubId);
            pw.println("mEventChanges (most recent first):");
            pw.increaseIndent();
            this.mEventChanges.reverseDump(pw);
            pw.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getCellSubIdForNetworkCapabilities(android.net.NetworkCapabilities nc) {
        if (nc == null || !nc.hasTransport(0)) {
            return -1;
        }
        android.net.NetworkSpecifier specifier = nc.getNetworkSpecifier();
        if (!(specifier instanceof android.net.TelephonyNetworkSpecifier)) {
            return -1;
        }
        return ((android.net.TelephonyNetworkSpecifier) specifier).getSubscriptionId();
    }

    private boolean doesHaveVPNAppWhiteList() {
        if (this.mVpnExt == null) {
            return false;
        }
        boolean result = this.mVpnExt.doesHaveVPNAppWhiteList(null);
        return result;
    }

    private boolean isInVPNAppWhiteList(java.lang.String packageName) {
        if (this.mVpnExt == null) {
            return false;
        }
        boolean result = this.mVpnExt.isInVPNAppWhiteList(null, packageName);
        return result;
    }
}
