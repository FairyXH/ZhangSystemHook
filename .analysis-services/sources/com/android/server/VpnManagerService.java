package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class VpnManagerService extends android.net.IVpnManager.Stub {
    private static final java.lang.String CONTEXT_ATTRIBUTION_TAG = "VPN_MANAGER";
    private static final java.lang.String TAG = com.android.server.VpnManagerService.class.getSimpleName();
    private final android.content.Context mContext;
    private final com.android.server.VpnManagerService.Dependencies mDeps;
    private final android.os.Handler mHandler;
    protected final android.os.HandlerThread mHandlerThread;
    private boolean mLockdownEnabled;
    private com.android.server.net.LockdownVpnTracker mLockdownTracker;
    private final int mMainUserId;
    private final android.os.INetworkManagementService mNMS;
    private final android.net.INetd mNetd;
    private final android.content.Context mUserAllContext;
    private final android.os.UserManager mUserManager;
    private final com.android.server.connectivity.VpnProfileStore mVpnProfileStore;
    protected final android.util.SparseArray<com.android.server.connectivity.Vpn> mVpns = new android.util.SparseArray<>();
    private android.content.BroadcastReceiver mIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.VpnManagerService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            com.android.server.VpnManagerService.this.ensureRunningOnHandlerThread();
            java.lang.String action = intent.getAction();
            int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            int uid = intent.getIntExtra("android.intent.extra.UID", -1);
            android.net.Uri packageData = intent.getData();
            java.lang.String packageName = packageData != null ? packageData.getSchemeSpecificPart() : null;
            if (com.android.server.net.LockdownVpnTracker.ACTION_LOCKDOWN_RESET.equals(action)) {
                com.android.server.VpnManagerService.this.onVpnLockdownReset();
                return;
            }
            if (userId == -10000) {
                return;
            }
            if ("android.intent.action.USER_STARTED".equals(action)) {
                com.android.server.VpnManagerService.this.onUserStarted(userId);
                return;
            }
            if ("android.intent.action.USER_STOPPED".equals(action)) {
                com.android.server.VpnManagerService.this.onUserStopped(userId);
                return;
            }
            if ("android.intent.action.USER_ADDED".equals(action)) {
                com.android.server.VpnManagerService.this.onUserAdded(userId);
                return;
            }
            if ("android.intent.action.USER_REMOVED".equals(action)) {
                com.android.server.VpnManagerService.this.onUserRemoved(userId);
                return;
            }
            if ("android.intent.action.USER_UNLOCKED".equals(action)) {
                com.android.server.VpnManagerService.this.onUserUnlocked(userId);
                return;
            }
            if ("android.intent.action.PACKAGE_REPLACED".equals(action)) {
                com.android.server.VpnManagerService.this.onPackageReplaced(packageName, uid);
                return;
            }
            if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                boolean isReplacing = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
                com.android.server.VpnManagerService.this.onPackageRemoved(packageName, uid, isReplacing);
            } else if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
                boolean isReplacing2 = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
                com.android.server.VpnManagerService.this.onPackageAdded(packageName, uid, isReplacing2);
            } else {
                android.util.Log.wtf(com.android.server.VpnManagerService.TAG, "received unexpected intent: " + action);
            }
        }
    };
    private android.content.BroadcastReceiver mUserPresentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.VpnManagerService.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            com.android.server.VpnManagerService.this.ensureRunningOnHandlerThread();
            com.android.server.VpnManagerService.this.updateLockdownVpn();
            context.unregisterReceiver(this);
        }
    };

    public static class Dependencies {
        public int getCallingUid() {
            return android.os.Binder.getCallingUid();
        }

        public android.os.HandlerThread makeHandlerThread() {
            return new android.os.HandlerThread("VpnManagerService");
        }

        public com.android.server.connectivity.VpnProfileStore getVpnProfileStore() {
            return new com.android.server.connectivity.VpnProfileStore();
        }

        public android.net.INetd getNetd() {
            return android.net.util.NetdService.getInstance();
        }

        public android.os.INetworkManagementService getINetworkManagementService() {
            return android.os.INetworkManagementService.Stub.asInterface(android.os.ServiceManager.getService("network_management"));
        }

        public com.android.server.connectivity.Vpn createVpn(android.os.Looper looper, android.content.Context context, android.os.INetworkManagementService nms, android.net.INetd netd, int userId) {
            return new com.android.server.connectivity.Vpn(looper, context, nms, netd, userId, new com.android.server.connectivity.VpnProfileStore());
        }

        public com.android.server.net.LockdownVpnTracker createLockDownVpnTracker(android.content.Context context, android.os.Handler handler, com.android.server.connectivity.Vpn vpn, com.android.internal.net.VpnProfile profile) {
            return new com.android.server.net.LockdownVpnTracker(context, handler, vpn, profile);
        }

        public int getMainUserId() {
            return ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getMainUserId();
        }
    }

    public VpnManagerService(android.content.Context context, com.android.server.VpnManagerService.Dependencies deps) {
        this.mContext = context.createAttributionContext(CONTEXT_ATTRIBUTION_TAG);
        this.mDeps = deps;
        this.mHandlerThread = this.mDeps.makeHandlerThread();
        this.mHandlerThread.start();
        this.mHandler = this.mHandlerThread.getThreadHandler();
        this.mVpnProfileStore = this.mDeps.getVpnProfileStore();
        this.mUserAllContext = this.mContext.createContextAsUser(android.os.UserHandle.ALL, 0);
        this.mNMS = this.mDeps.getINetworkManagementService();
        this.mNetd = this.mDeps.getNetd();
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        this.mMainUserId = this.mDeps.getMainUserId();
        registerReceivers();
        log("VpnManagerService starting up");
    }

    public static com.android.server.VpnManagerService create(android.content.Context context) {
        return new com.android.server.VpnManagerService(context, new com.android.server.VpnManagerService.Dependencies());
    }

    public void systemReady() {
        updateLockdownVpn();
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, writer)) {
            android.util.IndentingPrintWriter indentingPrintWriter = new com.android.internal.util.IndentingPrintWriter(writer, "  ");
            indentingPrintWriter.println("VPNs:");
            indentingPrintWriter.increaseIndent();
            synchronized (this.mVpns) {
                for (int i = 0; i < this.mVpns.size(); i++) {
                    indentingPrintWriter.println(this.mVpns.keyAt(i) + ": " + this.mVpns.valueAt(i).getPackage());
                    indentingPrintWriter.increaseIndent();
                    this.mVpns.valueAt(i).dump(indentingPrintWriter);
                    indentingPrintWriter.decreaseIndent();
                    indentingPrintWriter.println();
                }
                indentingPrintWriter.decreaseIndent();
            }
        }
    }

    public boolean prepareVpn(java.lang.String oldPackage, java.lang.String newPackage, int userId) {
        enforceCrossUserPermission(userId);
        synchronized (this.mVpns) {
            throwIfLockdownEnabled();
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn == null) {
                return false;
            }
            return vpn.prepare(oldPackage, newPackage, 1);
        }
    }

    public void setVpnPackageAuthorization(java.lang.String packageName, int userId, int vpnType) {
        enforceCrossUserPermission(userId);
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn != null) {
                vpn.setPackageAuthorization(packageName, vpnType);
            }
        }
    }

    public android.os.ParcelFileDescriptor establishVpn(com.android.internal.net.VpnConfig config) {
        android.os.ParcelFileDescriptor parcelFileDescriptorEstablish;
        int user = android.os.UserHandle.getUserId(this.mDeps.getCallingUid());
        synchronized (this.mVpns) {
            throwIfLockdownEnabled();
            parcelFileDescriptorEstablish = this.mVpns.get(user).establish(config);
        }
        return parcelFileDescriptorEstablish;
    }

    public boolean addVpnAddress(java.lang.String address, int prefixLength) {
        boolean zAddAddress;
        int user = android.os.UserHandle.getUserId(this.mDeps.getCallingUid());
        synchronized (this.mVpns) {
            throwIfLockdownEnabled();
            zAddAddress = this.mVpns.get(user).addAddress(address, prefixLength);
        }
        return zAddAddress;
    }

    public boolean removeVpnAddress(java.lang.String address, int prefixLength) {
        boolean zRemoveAddress;
        int user = android.os.UserHandle.getUserId(this.mDeps.getCallingUid());
        synchronized (this.mVpns) {
            throwIfLockdownEnabled();
            zRemoveAddress = this.mVpns.get(user).removeAddress(address, prefixLength);
        }
        return zRemoveAddress;
    }

    public boolean setUnderlyingNetworksForVpn(android.net.Network[] networks) {
        boolean success;
        int user = android.os.UserHandle.getUserId(this.mDeps.getCallingUid());
        synchronized (this.mVpns) {
            success = this.mVpns.get(user).setUnderlyingNetworks(networks);
        }
        return success;
    }

    public boolean provisionVpnProfile(com.android.internal.net.VpnProfile profile, java.lang.String packageName) {
        boolean zProvisionVpnProfile;
        int user = android.os.UserHandle.getUserId(this.mDeps.getCallingUid());
        synchronized (this.mVpns) {
            zProvisionVpnProfile = this.mVpns.get(user).provisionVpnProfile(packageName, profile);
        }
        return zProvisionVpnProfile;
    }

    public void deleteVpnProfile(java.lang.String packageName) {
        int user = android.os.UserHandle.getUserId(this.mDeps.getCallingUid());
        synchronized (this.mVpns) {
            this.mVpns.get(user).deleteVpnProfile(packageName);
        }
    }

    private int getAppUid(java.lang.String app, int userId) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
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

    private void verifyCallingUidAndPackage(java.lang.String packageName, int callingUid) {
        int userId = android.os.UserHandle.getUserId(callingUid);
        if (getAppUid(packageName, userId) != callingUid) {
            throw new java.lang.SecurityException(packageName + " does not belong to uid " + callingUid);
        }
    }

    public java.lang.String startVpnProfile(java.lang.String packageName) {
        java.lang.String strStartVpnProfile;
        int callingUid = android.os.Binder.getCallingUid();
        verifyCallingUidAndPackage(packageName, callingUid);
        int user = android.os.UserHandle.getUserId(callingUid);
        synchronized (this.mVpns) {
            throwIfLockdownEnabled();
            strStartVpnProfile = this.mVpns.get(user).startVpnProfile(packageName);
        }
        return strStartVpnProfile;
    }

    public void stopVpnProfile(java.lang.String packageName) {
        int callingUid = android.os.Binder.getCallingUid();
        verifyCallingUidAndPackage(packageName, callingUid);
        int user = android.os.UserHandle.getUserId(callingUid);
        synchronized (this.mVpns) {
            this.mVpns.get(user).stopVpnProfile(packageName);
        }
    }

    public android.net.VpnProfileState getProvisionedVpnProfileState(java.lang.String packageName) {
        android.net.VpnProfileState provisionedVpnProfileState;
        int callingUid = android.os.Binder.getCallingUid();
        verifyCallingUidAndPackage(packageName, callingUid);
        int user = android.os.UserHandle.getUserId(callingUid);
        synchronized (this.mVpns) {
            provisionedVpnProfileState = this.mVpns.get(user).getProvisionedVpnProfileState(packageName);
        }
        return provisionedVpnProfileState;
    }

    public void startLegacyVpn(com.android.internal.net.VpnProfile profile) {
        if (android.os.Build.VERSION.DEVICE_INITIAL_SDK_INT >= 31 && com.android.internal.net.VpnProfile.isLegacyType(profile.type)) {
            throw new java.lang.UnsupportedOperationException("Legacy VPN is deprecated");
        }
        int user = android.os.UserHandle.getUserId(this.mDeps.getCallingUid());
        synchronized (this.mVpns) {
            throwIfLockdownEnabled();
            this.mVpns.get(user).startLegacyVpn(profile);
        }
    }

    public com.android.internal.net.LegacyVpnInfo getLegacyVpnInfo(int userId) {
        com.android.internal.net.LegacyVpnInfo legacyVpnInfo;
        enforceCrossUserPermission(userId);
        synchronized (this.mVpns) {
            legacyVpnInfo = this.mVpns.get(userId).getLegacyVpnInfo();
        }
        return legacyVpnInfo;
    }

    public com.android.internal.net.VpnConfig getVpnConfig(int userId) {
        enforceCrossUserPermission(userId);
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn == null) {
                return null;
            }
            return vpn.getVpnConfig();
        }
    }

    private boolean isLockdownVpnEnabled() {
        return this.mVpnProfileStore.get("LOCKDOWN_VPN") != null;
    }

    public boolean updateLockdownVpn() {
        if (this.mDeps.getCallingUid() != 1000 && this.mDeps.getCallingUid() != android.os.UserHandle.getUid(this.mMainUserId, 1000) && android.os.Binder.getCallingPid() != android.os.Process.myPid()) {
            logw("Lockdown VPN only available to system process or AID_SYSTEM on main user");
            return false;
        }
        synchronized (this.mVpns) {
            this.mLockdownEnabled = isLockdownVpnEnabled();
            if (!this.mLockdownEnabled) {
                setLockdownTracker(null);
                return true;
            }
            byte[] profileTag = this.mVpnProfileStore.get("LOCKDOWN_VPN");
            if (profileTag == null) {
                loge("Lockdown VPN configured but cannot be read from keystore");
                return false;
            }
            java.lang.String profileName = new java.lang.String(profileTag);
            com.android.internal.net.VpnProfile profile = com.android.internal.net.VpnProfile.decode(profileName, this.mVpnProfileStore.get("VPN_" + profileName));
            if (profile == null) {
                loge("Lockdown VPN configured invalid profile " + profileName);
                setLockdownTracker(null);
                return true;
            }
            int user = android.os.UserHandle.getUserId(this.mDeps.getCallingUid());
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(user);
            if (vpn == null) {
                logw("VPN for user " + user + " not ready yet. Skipping lockdown");
                return false;
            }
            setLockdownTracker(this.mDeps.createLockDownVpnTracker(this.mContext, this.mHandler, vpn, profile));
            return true;
        }
    }

    private void setLockdownTracker(com.android.server.net.LockdownVpnTracker tracker) {
        com.android.server.net.LockdownVpnTracker existing = this.mLockdownTracker;
        this.mLockdownTracker = null;
        if (existing != null) {
            existing.shutdown();
        }
        if (tracker != null) {
            this.mLockdownTracker = tracker;
            this.mLockdownTracker.init();
        }
    }

    private void throwIfLockdownEnabled() {
        if (this.mLockdownEnabled) {
            throw new java.lang.IllegalStateException("Unavailable in lockdown mode");
        }
    }

    private boolean startAlwaysOnVpn(int userId) {
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn == null) {
                android.util.Log.wtf(TAG, "User " + userId + " has no Vpn configuration");
                return false;
            }
            return vpn.startAlwaysOnVpn();
        }
    }

    public boolean isAlwaysOnVpnPackageSupported(int userId, java.lang.String packageName) {
        enforceSettingsPermission();
        enforceCrossUserPermission(userId);
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn == null) {
                logw("User " + userId + " has no Vpn configuration");
                return false;
            }
            return vpn.isAlwaysOnPackageSupported(packageName);
        }
    }

    public boolean setAlwaysOnVpnPackage(int userId, java.lang.String packageName, boolean lockdown, java.util.List<java.lang.String> lockdownAllowlist) {
        enforceControlAlwaysOnVpnPermission();
        enforceCrossUserPermission(userId);
        synchronized (this.mVpns) {
            if (isLockdownVpnEnabled()) {
                return false;
            }
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn == null) {
                logw("User " + userId + " has no Vpn configuration");
                return false;
            }
            if (!vpn.setAlwaysOnPackage(packageName, lockdown, lockdownAllowlist)) {
                return false;
            }
            if (!startAlwaysOnVpn(userId)) {
                vpn.setAlwaysOnPackage(null, false, null);
                return false;
            }
            return true;
        }
    }

    public java.lang.String getAlwaysOnVpnPackage(int userId) {
        enforceControlAlwaysOnVpnPermission();
        enforceCrossUserPermission(userId);
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn == null) {
                logw("User " + userId + " has no Vpn configuration");
                return null;
            }
            return vpn.getAlwaysOnPackage();
        }
    }

    public boolean isVpnLockdownEnabled(int userId) {
        enforceControlAlwaysOnVpnPermission();
        enforceCrossUserPermission(userId);
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn == null) {
                logw("User " + userId + " has no Vpn configuration");
                return false;
            }
            return vpn.getLockdown();
        }
    }

    public java.util.List<java.lang.String> getVpnLockdownAllowlist(int userId) {
        enforceControlAlwaysOnVpnPermission();
        enforceCrossUserPermission(userId);
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn == null) {
                logw("User " + userId + " has no Vpn configuration");
                return null;
            }
            return vpn.getLockdownAllowlist();
        }
    }

    private com.android.server.connectivity.Vpn getVpnIfOwner() {
        return getVpnIfOwner(this.mDeps.getCallingUid());
    }

    private com.android.server.connectivity.Vpn getVpnIfOwner(int uid) {
        android.net.UnderlyingNetworkInfo info;
        int user = android.os.UserHandle.getUserId(uid);
        com.android.server.connectivity.Vpn vpn = this.mVpns.get(user);
        if (vpn == null || (info = vpn.getUnderlyingNetworkInfo()) == null || info.getOwnerUid() != uid) {
            return null;
        }
        return vpn;
    }

    private void registerReceivers() {
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.USER_STARTED");
        intentFilter.addAction("android.intent.action.USER_STOPPED");
        intentFilter.addAction("android.intent.action.USER_ADDED");
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        this.mUserAllContext.registerReceiver(this.mIntentReceiver, intentFilter, null, this.mHandler);
        this.mContext.createContextAsUser(android.os.UserHandle.of(this.mMainUserId), 0).registerReceiver(this.mUserPresentReceiver, new android.content.IntentFilter("android.intent.action.USER_PRESENT"), null, this.mHandler);
        android.content.IntentFilter intentFilter2 = new android.content.IntentFilter();
        intentFilter2.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter2.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter2.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter2.addDataScheme("package");
        intentFilter2.addCategory("oplusBrEx@android.intent.action.PACKAGE_ADDED@PACKAGE=NOREPLACING");
        intentFilter2.addCategory("oplusBrEx@android.intent.action.PACKAGE_REMOVED@PACKAGE=NOREPLACING");
        this.mUserAllContext.registerReceiver(this.mIntentReceiver, intentFilter2, null, this.mHandler);
        android.content.IntentFilter intentFilter3 = new android.content.IntentFilter();
        intentFilter3.addAction(com.android.server.net.LockdownVpnTracker.ACTION_LOCKDOWN_RESET);
        this.mUserAllContext.registerReceiver(this.mIntentReceiver, intentFilter3, "android.permission.NETWORK_STACK", this.mHandler, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserStarted(int userId) {
        android.content.pm.UserInfo user = this.mUserManager.getUserInfo(userId);
        if (user == null) {
            logw("Started user doesn't exist. UserId: " + userId);
            return;
        }
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn userVpn = this.mVpns.get(userId);
            if (userVpn != null) {
                loge("Starting user already has a VPN");
                return;
            }
            com.android.server.connectivity.Vpn userVpn2 = this.mDeps.createVpn(this.mHandler.getLooper(), this.mContext, this.mNMS, this.mNetd, userId);
            this.mVpns.put(userId, userVpn2);
            if (userId == this.mMainUserId && isLockdownVpnEnabled()) {
                updateLockdownVpn();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserStopped(int userId) {
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn userVpn = this.mVpns.get(userId);
            if (userVpn == null) {
                loge("Stopped user has no VPN");
            } else {
                userVpn.onUserStopped();
                this.mVpns.delete(userId);
            }
        }
    }

    public boolean isCallerCurrentAlwaysOnVpnApp() {
        boolean z;
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = getVpnIfOwner();
            z = vpn != null && vpn.getAlwaysOn();
        }
        return z;
    }

    public boolean isCallerCurrentAlwaysOnVpnLockdownApp() {
        boolean z;
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = getVpnIfOwner();
            z = vpn != null && vpn.getLockdown();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserAdded(int userId) {
        synchronized (this.mVpns) {
            int vpnsSize = this.mVpns.size();
            for (int i = 0; i < vpnsSize; i++) {
                com.android.server.connectivity.Vpn vpn = this.mVpns.valueAt(i);
                vpn.onUserAdded(userId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserRemoved(int userId) {
        synchronized (this.mVpns) {
            int vpnsSize = this.mVpns.size();
            for (int i = 0; i < vpnsSize; i++) {
                com.android.server.connectivity.Vpn vpn = this.mVpns.valueAt(i);
                vpn.onUserRemoved(userId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageReplaced(java.lang.String packageName, int uid) {
        if (android.text.TextUtils.isEmpty(packageName) || uid < 0) {
            android.util.Log.wtf(TAG, "Invalid package in onPackageReplaced: " + packageName + " | " + uid);
            return;
        }
        int userId = android.os.UserHandle.getUserId(uid);
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn == null) {
                return;
            }
            if (android.text.TextUtils.equals(vpn.getAlwaysOnPackage(), packageName)) {
                log("Restarting always-on VPN package " + packageName + " for user " + userId);
                vpn.startAlwaysOnVpn();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageRemoved(java.lang.String packageName, int uid, boolean isReplacing) {
        if (android.text.TextUtils.isEmpty(packageName) || uid < 0) {
            android.util.Log.wtf(TAG, "Invalid package in onPackageRemoved: " + packageName + " | " + uid);
            return;
        }
        int userId = android.os.UserHandle.getUserId(uid);
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn != null && !isReplacing) {
                if (android.text.TextUtils.equals(vpn.getAlwaysOnPackage(), packageName)) {
                    log("Removing always-on VPN package " + packageName + " for user " + userId);
                    vpn.setAlwaysOnPackage(null, false, null);
                }
                vpn.refreshPlatformVpnAppExclusionList();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageAdded(java.lang.String packageName, int uid, boolean isReplacing) {
        if (android.text.TextUtils.isEmpty(packageName) || uid < 0) {
            android.util.Log.wtf(TAG, "Invalid package in onPackageAdded: " + packageName + " | " + uid);
            return;
        }
        int userId = android.os.UserHandle.getUserId(uid);
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn != null && !isReplacing) {
                vpn.refreshPlatformVpnAppExclusionList();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserUnlocked(int userId) {
        synchronized (this.mVpns) {
            if (userId == this.mMainUserId && isLockdownVpnEnabled()) {
                updateLockdownVpn();
            } else {
                startAlwaysOnVpn(userId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVpnLockdownReset() {
        synchronized (this.mVpns) {
            if (this.mLockdownTracker != null) {
                this.mLockdownTracker.reset();
            }
        }
    }

    public boolean setAppExclusionList(int userId, java.lang.String vpnPackage, java.util.List<java.lang.String> excludedApps) {
        boolean appExclusionList;
        enforceSettingsPermission();
        enforceCrossUserPermission(userId);
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn != null) {
                appExclusionList = vpn.setAppExclusionList(vpnPackage, excludedApps);
            } else {
                logw("User " + userId + " has no Vpn configuration");
                throw new java.lang.IllegalStateException("VPN for user " + userId + " not ready yet. Skipping setting the list");
            }
        }
        return appExclusionList;
    }

    public java.util.List<java.lang.String> getAppExclusionList(int userId, java.lang.String vpnPackage) {
        enforceSettingsPermission();
        enforceCrossUserPermission(userId);
        synchronized (this.mVpns) {
            com.android.server.connectivity.Vpn vpn = this.mVpns.get(userId);
            if (vpn != null) {
                return vpn.getAppExclusionList(vpnPackage);
            }
            logw("User " + userId + " has no Vpn configuration");
            return null;
        }
    }

    public void factoryReset() {
        enforceSettingsPermission();
        if (this.mUserManager.hasUserRestriction("no_network_reset") || this.mUserManager.hasUserRestriction("no_config_vpn")) {
            return;
        }
        int userId = android.os.UserHandle.getCallingUserId();
        synchronized (this.mVpns) {
            java.lang.String alwaysOnPackage = getAlwaysOnVpnPackage(userId);
            if (alwaysOnPackage != null) {
                setAlwaysOnVpnPackage(userId, null, false, null);
                setVpnPackageAuthorization(alwaysOnPackage, userId, -1);
            }
            if (this.mLockdownEnabled && userId == this.mMainUserId) {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    this.mVpnProfileStore.remove("LOCKDOWN_VPN");
                    this.mLockdownEnabled = false;
                    setLockdownTracker(null);
                    android.os.Binder.restoreCallingIdentity(ident);
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(ident);
                    throw th;
                }
            }
            com.android.internal.net.VpnConfig vpnConfig = getVpnConfig(userId);
            if (vpnConfig != null) {
                if (!vpnConfig.legacy) {
                    setVpnPackageAuthorization(vpnConfig.user, userId, -1);
                    prepareVpn(null, "[Legacy VPN]", userId);
                } else {
                    prepareVpn("[Legacy VPN]", "[Legacy VPN]", userId);
                }
            }
        }
    }

    public byte[] getFromVpnProfileStore(java.lang.String name) {
        return this.mVpnProfileStore.get(name);
    }

    public boolean putIntoVpnProfileStore(java.lang.String name, byte[] blob) {
        return this.mVpnProfileStore.put(name, blob);
    }

    public boolean removeFromVpnProfileStore(java.lang.String name) {
        return this.mVpnProfileStore.remove(name);
    }

    public java.lang.String[] listFromVpnProfileStore(java.lang.String prefix) {
        return this.mVpnProfileStore.list(prefix);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureRunningOnHandlerThread() {
        if (this.mHandler.getLooper().getThread() != java.lang.Thread.currentThread()) {
            throw new java.lang.IllegalStateException("Not running on VpnManagerService thread: " + java.lang.Thread.currentThread().getName());
        }
    }

    private void enforceControlAlwaysOnVpnPermission() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_ALWAYS_ON_VPN", "VpnManagerService");
    }

    private void enforceCrossUserPermission(int userId) {
        if (userId == android.os.UserHandle.getCallingUserId()) {
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "VpnManagerService");
    }

    private void enforceSettingsPermission() {
        com.android.net.module.util.PermissionUtils.enforceAnyPermissionOf(this.mContext, new java.lang.String[]{"android.permission.NETWORK_SETTINGS", "android.permission.MAINLINE_NETWORK_STACK"});
    }

    private static void log(java.lang.String s) {
        android.util.Log.d(TAG, s);
    }

    private static void logw(java.lang.String s) {
        android.util.Log.w(TAG, s);
    }

    private static void loge(java.lang.String s) {
        android.util.Log.e(TAG, s);
    }
}
