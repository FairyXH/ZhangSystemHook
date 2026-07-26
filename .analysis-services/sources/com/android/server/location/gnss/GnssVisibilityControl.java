package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
class GnssVisibilityControl {
    private static final int ARRAY_MAP_INITIAL_CAPACITY_PROXY_APPS_STATE = 5;
    private static final long EMERGENCY_EXTENSION_FOR_MISMATCH = 128000;
    private static final long LOCATION_ICON_DISPLAY_DURATION_MILLIS = 5000;
    private static final java.lang.String LOCATION_PERMISSION_NAME = "android.permission.ACCESS_FINE_LOCATION";
    private static final long ON_GPS_ENABLED_CHANGED_TIMEOUT_MILLIS = 3000;
    private static final java.lang.String TAG = "GnssVisibilityControl";
    private static final java.lang.String WAKELOCK_KEY = "GnssVisibilityControl";
    private static final long WAKELOCK_TIMEOUT_MILLIS = 60000;
    private final android.app.AppOpsManager mAppOps;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private boolean mIsGpsEnabled;
    private final com.android.internal.location.GpsNetInitiatedHandler mNiHandler;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.os.PowerManager.WakeLock mWakeLock;
    private static final boolean DEBUG = android.util.Log.isLoggable("GnssVisibilityControl", 3);
    private static final java.lang.String[] NO_LOCATION_ENABLED_PROXY_APPS = new java.lang.String[0];
    private android.util.ArrayMap<java.lang.String, com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState> mProxyAppsState = new android.util.ArrayMap<>(5);
    private android.content.pm.PackageManager.OnPermissionsChangedListener mOnPermissionsChangedListener = new android.content.pm.PackageManager.OnPermissionsChangedListener() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda3
        public final void onPermissionsChanged(int i) {
            this.f$0.lambda$new$1(i);
        }
    };

    private native boolean native_enable_nfw_location_access(java.lang.String[] strArr);

    private static final class ProxyAppState {
        private boolean mHasLocationPermission;
        private boolean mIsLocationIconOn;

        private ProxyAppState(boolean hasLocationPermission) {
            this.mHasLocationPermission = hasLocationPermission;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(final int uid) {
        runOnHandler(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0(uid);
            }
        });
    }

    GnssVisibilityControl(android.content.Context context, android.os.Looper looper, com.android.internal.location.GpsNetInitiatedHandler niHandler) {
        this.mContext = context;
        android.os.PowerManager powerManager = (android.os.PowerManager) context.getSystemService("power");
        this.mWakeLock = powerManager.newWakeLock(1, "GnssVisibilityControl");
        this.mHandler = new android.os.Handler(looper);
        this.mNiHandler = niHandler;
        this.mAppOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        this.mPackageManager = this.mContext.getPackageManager();
        runOnHandler(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.handleInitialize();
            }
        });
    }

    void onGpsEnabledChanged(final boolean isEnabled) {
        if (!this.mHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onGpsEnabledChanged$2(isEnabled);
            }
        }, 3000L) && !isEnabled) {
            android.util.Log.w("GnssVisibilityControl", "Native call to disable non-framework location access in GNSS HAL may get executed after native_cleanup().");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportNfwNotification$3(java.lang.String proxyAppPackageName, byte protocolStack, java.lang.String otherProtocolStackName, byte requestor, java.lang.String requestorId, byte responseType, boolean inEmergencyMode, boolean isCachedLocation) {
        handleNfwNotification(new com.android.server.location.gnss.GnssVisibilityControl.NfwNotification(proxyAppPackageName, protocolStack, otherProtocolStackName, requestor, requestorId, responseType, inEmergencyMode, isCachedLocation));
    }

    void reportNfwNotification(final java.lang.String proxyAppPackageName, final byte protocolStack, final java.lang.String otherProtocolStackName, final byte requestor, final java.lang.String requestorId, final byte responseType, final boolean inEmergencyMode, final boolean isCachedLocation) {
        runOnHandler(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$reportNfwNotification$3(proxyAppPackageName, protocolStack, otherProtocolStackName, requestor, requestorId, responseType, inEmergencyMode, isCachedLocation);
            }
        });
    }

    void onConfigurationUpdated(com.android.server.location.gnss.GnssConfiguration configuration) {
        java.util.List<java.lang.String> nfwLocationAccessProxyApps = configuration.getProxyApps();
        final java.util.List<java.lang.String> normalizedNfwApps = ((com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, this.mContext)).getNfwProxyApps(nfwLocationAccessProxyApps);
        android.util.Log.d("GnssVisibilityControl", "normalizedApps: " + java.lang.String.join(",", normalizedNfwApps));
        runOnHandler(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onConfigurationUpdated$4(normalizedNfwApps);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInitialize() {
        listenForProxyAppsPackageUpdates();
    }

    private void listenForProxyAppsPackageUpdates() {
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=ENTIRE_PKG_CHANGED");
        intentFilter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(new android.content.BroadcastReceiver() { // from class: com.android.server.location.gnss.GnssVisibilityControl.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:20:0x0037  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r4, android.content.Intent r5) {
                /*
                    r3 = this;
                    java.lang.String r0 = r5.getAction()
                    if (r0 != 0) goto L7
                    return
                L7:
                    int r1 = r0.hashCode()
                    switch(r1) {
                        case -810471698: goto L2d;
                        case 172491798: goto L23;
                        case 525384130: goto L19;
                        case 1544582882: goto Lf;
                        default: goto Le;
                    }
                Le:
                    goto L37
                Lf:
                    java.lang.String r1 = "android.intent.action.PACKAGE_ADDED"
                    boolean r1 = r0.equals(r1)
                    if (r1 == 0) goto Le
                    r1 = 0
                    goto L38
                L19:
                    java.lang.String r1 = "android.intent.action.PACKAGE_REMOVED"
                    boolean r1 = r0.equals(r1)
                    if (r1 == 0) goto Le
                    r1 = 1
                    goto L38
                L23:
                    java.lang.String r1 = "android.intent.action.PACKAGE_CHANGED"
                    boolean r1 = r0.equals(r1)
                    if (r1 == 0) goto Le
                    r1 = 3
                    goto L38
                L2d:
                    java.lang.String r1 = "android.intent.action.PACKAGE_REPLACED"
                    boolean r1 = r0.equals(r1)
                    if (r1 == 0) goto Le
                    r1 = 2
                    goto L38
                L37:
                    r1 = -1
                L38:
                    switch(r1) {
                        case 0: goto L3c;
                        case 1: goto L3c;
                        case 2: goto L3c;
                        case 3: goto L3c;
                        default: goto L3b;
                    }
                L3b:
                    goto L49
                L3c:
                    android.net.Uri r1 = r5.getData()
                    java.lang.String r1 = r1.getEncodedSchemeSpecificPart()
                    com.android.server.location.gnss.GnssVisibilityControl r2 = com.android.server.location.gnss.GnssVisibilityControl.this
                    com.android.server.location.gnss.GnssVisibilityControl.m4956$$Nest$mhandleProxyAppPackageUpdate(r2, r1, r0)
                L49:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.location.gnss.GnssVisibilityControl.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        }, android.os.UserHandle.ALL, intentFilter, null, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleProxyAppPackageUpdate(java.lang.String pkgName, java.lang.String action) {
        com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState proxyAppState = this.mProxyAppsState.get(pkgName);
        if (proxyAppState == null) {
            return;
        }
        if (DEBUG) {
            android.util.Log.d("GnssVisibilityControl", "Proxy app " + pkgName + " package changed: " + action);
        }
        boolean updatedLocationPermission = shouldEnableLocationPermissionInGnssHal(pkgName);
        if (proxyAppState.mHasLocationPermission != updatedLocationPermission) {
            android.util.Log.i("GnssVisibilityControl", "Proxy app " + pkgName + " location permission changed. IsLocationPermissionEnabled: " + updatedLocationPermission);
            proxyAppState.mHasLocationPermission = updatedLocationPermission;
            updateNfwLocationAccessProxyAppsInGnssHal();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleUpdateProxyApps, reason: merged with bridge method [inline-methods] */
    public void lambda$onConfigurationUpdated$4(java.util.List<java.lang.String> nfwLocationAccessProxyApps) {
        if (!isProxyAppListUpdated(nfwLocationAccessProxyApps)) {
            return;
        }
        if (nfwLocationAccessProxyApps.isEmpty()) {
            if (!this.mProxyAppsState.isEmpty()) {
                this.mPackageManager.removeOnPermissionsChangeListener(this.mOnPermissionsChangedListener);
                resetProxyAppsState();
                updateNfwLocationAccessProxyAppsInGnssHal();
                return;
            }
            return;
        }
        if (this.mProxyAppsState.isEmpty()) {
            this.mPackageManager.addOnPermissionsChangeListener(this.mOnPermissionsChangedListener);
        } else {
            resetProxyAppsState();
        }
        for (java.lang.String proxyAppPkgName : nfwLocationAccessProxyApps) {
            com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState proxyAppState = new com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState(shouldEnableLocationPermissionInGnssHal(proxyAppPkgName));
            this.mProxyAppsState.put(proxyAppPkgName, proxyAppState);
        }
        updateNfwLocationAccessProxyAppsInGnssHal();
    }

    private void resetProxyAppsState() {
        for (java.util.Map.Entry<java.lang.String, com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState> entry : this.mProxyAppsState.entrySet()) {
            com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState proxyAppState = entry.getValue();
            if (proxyAppState.mIsLocationIconOn) {
                this.mHandler.removeCallbacksAndMessages(proxyAppState);
                android.content.pm.ApplicationInfo proxyAppInfo = getProxyAppInfo(entry.getKey());
                if (proxyAppInfo != null) {
                    clearLocationIcon(proxyAppState, proxyAppInfo.uid, entry.getKey());
                }
            }
        }
        this.mProxyAppsState.clear();
    }

    private boolean isProxyAppListUpdated(java.util.List<java.lang.String> nfwLocationAccessProxyApps) {
        if (nfwLocationAccessProxyApps.size() != this.mProxyAppsState.size()) {
            return true;
        }
        for (java.lang.String nfwLocationAccessProxyApp : nfwLocationAccessProxyApps) {
            if (!this.mProxyAppsState.containsKey(nfwLocationAccessProxyApp)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleGpsEnabledChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$onGpsEnabledChanged$2(boolean isGpsEnabled) {
        if (DEBUG) {
            android.util.Log.d("GnssVisibilityControl", "handleGpsEnabledChanged, mIsGpsEnabled: " + this.mIsGpsEnabled + ", isGpsEnabled: " + isGpsEnabled);
        }
        this.mIsGpsEnabled = isGpsEnabled;
        if (!this.mIsGpsEnabled) {
            disableNfwLocationAccess();
        } else {
            setNfwLocationAccessProxyAppsInGnssHal(getLocationPermissionEnabledProxyApps());
        }
    }

    private void disableNfwLocationAccess() {
        setNfwLocationAccessProxyAppsInGnssHal(NO_LOCATION_ENABLED_PROXY_APPS);
    }

    private static class NfwNotification {
        private static final byte NFW_RESPONSE_TYPE_ACCEPTED_LOCATION_PROVIDED = 2;
        private static final byte NFW_RESPONSE_TYPE_ACCEPTED_NO_LOCATION_PROVIDED = 1;
        private static final byte NFW_RESPONSE_TYPE_REJECTED = 0;
        private final boolean mInEmergencyMode;
        private final boolean mIsCachedLocation;
        private final java.lang.String mOtherProtocolStackName;
        private final byte mProtocolStack;
        private final java.lang.String mProxyAppPackageName;
        private final byte mRequestor;
        private final java.lang.String mRequestorId;
        private final byte mResponseType;

        private NfwNotification(java.lang.String proxyAppPackageName, byte protocolStack, java.lang.String otherProtocolStackName, byte requestor, java.lang.String requestorId, byte responseType, boolean inEmergencyMode, boolean isCachedLocation) {
            this.mProxyAppPackageName = proxyAppPackageName;
            this.mProtocolStack = protocolStack;
            this.mOtherProtocolStackName = otherProtocolStackName;
            this.mRequestor = requestor;
            this.mRequestorId = requestorId;
            this.mResponseType = responseType;
            this.mInEmergencyMode = inEmergencyMode;
            this.mIsCachedLocation = isCachedLocation;
        }

        public java.lang.String toString() {
            return java.lang.String.format("{proxyAppPackageName: %s, protocolStack: %d, otherProtocolStackName: %s, requestor: %d, requestorId: %s, responseType: %s, inEmergencyMode: %b, isCachedLocation: %b}", this.mProxyAppPackageName, java.lang.Byte.valueOf(this.mProtocolStack), this.mOtherProtocolStackName, java.lang.Byte.valueOf(this.mRequestor), this.mRequestorId, getResponseTypeAsString(), java.lang.Boolean.valueOf(this.mInEmergencyMode), java.lang.Boolean.valueOf(this.mIsCachedLocation));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.String getResponseTypeAsString() {
            switch (this.mResponseType) {
                case 0:
                    return "REJECTED";
                case 1:
                    return "ACCEPTED_NO_LOCATION_PROVIDED";
                case 2:
                    return "ACCEPTED_LOCATION_PROVIDED";
                default:
                    return "<Unknown>";
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isRequestAccepted() {
            return this.mResponseType != 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isLocationProvided() {
            return this.mResponseType == 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isRequestAttributedToProxyApp() {
            return !android.text.TextUtils.isEmpty(this.mProxyAppPackageName);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEmergencyRequestNotification() {
            return this.mInEmergencyMode && !isRequestAttributedToProxyApp();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handlePermissionsChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0(int uid) {
        if (this.mProxyAppsState.isEmpty()) {
            return;
        }
        for (java.util.Map.Entry<java.lang.String, com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState> entry : this.mProxyAppsState.entrySet()) {
            java.lang.String proxyAppPkgName = entry.getKey();
            android.content.pm.ApplicationInfo proxyAppInfo = getProxyAppInfo(proxyAppPkgName);
            if (proxyAppInfo != null && proxyAppInfo.uid == uid) {
                boolean isLocationPermissionEnabled = shouldEnableLocationPermissionInGnssHal(proxyAppPkgName);
                com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState proxyAppState = entry.getValue();
                if (isLocationPermissionEnabled != proxyAppState.mHasLocationPermission) {
                    android.util.Log.i("GnssVisibilityControl", "Proxy app " + proxyAppPkgName + " location permission changed. IsLocationPermissionEnabled: " + isLocationPermissionEnabled);
                    proxyAppState.mHasLocationPermission = isLocationPermissionEnabled;
                    updateNfwLocationAccessProxyAppsInGnssHal();
                    return;
                }
                return;
            }
        }
    }

    private android.content.pm.ApplicationInfo getProxyAppInfo(java.lang.String proxyAppPkgName) {
        try {
            return this.mPackageManager.getApplicationInfo(proxyAppPkgName, 0);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            if (DEBUG) {
                android.util.Log.d("GnssVisibilityControl", "Proxy app " + proxyAppPkgName + " is not found.");
                return null;
            }
            return null;
        }
    }

    private boolean shouldEnableLocationPermissionInGnssHal(java.lang.String proxyAppPkgName) {
        return isProxyAppInstalled(proxyAppPkgName) && hasLocationPermission(proxyAppPkgName);
    }

    private boolean isProxyAppInstalled(java.lang.String pkgName) {
        android.content.pm.ApplicationInfo proxyAppInfo = getProxyAppInfo(pkgName);
        return proxyAppInfo != null && proxyAppInfo.enabled;
    }

    private boolean hasLocationPermission(java.lang.String pkgName) {
        return this.mPackageManager.checkPermission(LOCATION_PERMISSION_NAME, pkgName) == 0;
    }

    private void updateNfwLocationAccessProxyAppsInGnssHal() {
        if (!this.mIsGpsEnabled) {
            return;
        }
        setNfwLocationAccessProxyAppsInGnssHal(getLocationPermissionEnabledProxyApps());
    }

    private void setNfwLocationAccessProxyAppsInGnssHal(java.lang.String[] locationPermissionEnabledProxyApps) {
        java.lang.String proxyAppsStr = java.util.Arrays.toString(locationPermissionEnabledProxyApps);
        android.util.Log.i("GnssVisibilityControl", "Updating non-framework location access proxy apps in the GNSS HAL to: " + proxyAppsStr);
        boolean result = native_enable_nfw_location_access(locationPermissionEnabledProxyApps);
        if (!result) {
            android.util.Log.e("GnssVisibilityControl", "Failed to update non-framework location access proxy apps in the GNSS HAL to: " + proxyAppsStr);
        }
    }

    private java.lang.String[] getLocationPermissionEnabledProxyApps() {
        int countLocationPermissionEnabledProxyApps = 0;
        for (com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState proxyAppState : this.mProxyAppsState.values()) {
            if (proxyAppState.mHasLocationPermission) {
                countLocationPermissionEnabledProxyApps++;
            }
        }
        int i = 0;
        java.lang.String[] locationPermissionEnabledProxyApps = new java.lang.String[countLocationPermissionEnabledProxyApps];
        for (java.util.Map.Entry<java.lang.String, com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState> entry : this.mProxyAppsState.entrySet()) {
            java.lang.String proxyApp = entry.getKey();
            if (entry.getValue().mHasLocationPermission) {
                locationPermissionEnabledProxyApps[i] = proxyApp;
                i++;
            }
        }
        return locationPermissionEnabledProxyApps;
    }

    public boolean hasLocationPermissionEnabledProxyApps() {
        return getLocationPermissionEnabledProxyApps().length > 0;
    }

    private void handleNfwNotification(com.android.server.location.gnss.GnssVisibilityControl.NfwNotification nfwNotification) {
        if (DEBUG) {
            android.util.Log.d("GnssVisibilityControl", "Non-framework location access notification: " + nfwNotification);
        }
        if (nfwNotification.isEmergencyRequestNotification()) {
            handleEmergencyNfwNotification(nfwNotification);
            return;
        }
        java.lang.String proxyAppPkgName = nfwNotification.mProxyAppPackageName;
        com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState proxyAppState = this.mProxyAppsState.get(proxyAppPkgName);
        boolean isLocationRequestAccepted = nfwNotification.isRequestAccepted();
        boolean isPermissionMismatched = isPermissionMismatched(proxyAppState, nfwNotification);
        logEvent(nfwNotification, isPermissionMismatched);
        if (!nfwNotification.isRequestAttributedToProxyApp()) {
            if (!isLocationRequestAccepted) {
                if (DEBUG) {
                    android.util.Log.d("GnssVisibilityControl", "Non-framework location request rejected. ProxyAppPackageName field is not set in the notification: " + nfwNotification + ". Number of configured proxy apps: " + this.mProxyAppsState.size());
                    return;
                }
                return;
            }
            android.util.Log.e("GnssVisibilityControl", "ProxyAppPackageName field is not set. AppOps service not notified for notification: " + nfwNotification);
            return;
        }
        if (proxyAppState == null) {
            android.util.Log.w("GnssVisibilityControl", "Could not find proxy app " + proxyAppPkgName + " in the value specified for config parameter: NFW_PROXY_APPS. AppOps service not notified for notification: " + nfwNotification);
            return;
        }
        android.content.pm.ApplicationInfo proxyAppInfo = getProxyAppInfo(proxyAppPkgName);
        if (proxyAppInfo == null) {
            android.util.Log.e("GnssVisibilityControl", "Proxy app " + proxyAppPkgName + " is not found. AppOps service not notified for notification: " + nfwNotification);
            return;
        }
        if (nfwNotification.isLocationProvided()) {
            showLocationIcon(proxyAppState, nfwNotification, proxyAppInfo.uid, proxyAppPkgName);
            this.mAppOps.noteOpNoThrow(1, proxyAppInfo.uid, proxyAppPkgName);
        }
        if (isPermissionMismatched) {
            android.util.Log.w("GnssVisibilityControl", "Permission mismatch. Proxy app " + proxyAppPkgName + " location permission is set to " + proxyAppState.mHasLocationPermission + " and GNSS HAL enabled is set to " + this.mIsGpsEnabled + " but GNSS non-framework location access response type is " + nfwNotification.getResponseTypeAsString() + " for notification: " + nfwNotification);
        }
    }

    private boolean isPermissionMismatched(com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState proxyAppState, com.android.server.location.gnss.GnssVisibilityControl.NfwNotification nfwNotification) {
        boolean isLocationRequestAccepted = nfwNotification.isRequestAccepted();
        if (proxyAppState == null || !this.mIsGpsEnabled) {
            return isLocationRequestAccepted;
        }
        return proxyAppState.mHasLocationPermission != isLocationRequestAccepted;
    }

    private void showLocationIcon(com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState proxyAppState, com.android.server.location.gnss.GnssVisibilityControl.NfwNotification nfwNotification, int uid, final java.lang.String proxyAppPkgName) {
        boolean isLocationIconOn = proxyAppState.mIsLocationIconOn;
        if (!isLocationIconOn) {
            if (!updateLocationIcon(true, uid, proxyAppPkgName)) {
                android.util.Log.w("GnssVisibilityControl", "Failed to show Location icon for notification: " + nfwNotification);
                return;
            }
            proxyAppState.mIsLocationIconOn = true;
        } else {
            this.mHandler.removeCallbacksAndMessages(proxyAppState);
        }
        if (DEBUG) {
            android.util.Log.d("GnssVisibilityControl", "Location icon on. " + (isLocationIconOn ? "Extending" : "Setting") + " icon display timer. Uid: " + uid + ", proxyAppPkgName: " + proxyAppPkgName);
        }
        if (!this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showLocationIcon$5(proxyAppPkgName);
            }
        }, proxyAppState, LOCATION_ICON_DISPLAY_DURATION_MILLIS)) {
            clearLocationIcon(proxyAppState, uid, proxyAppPkgName);
            android.util.Log.w("GnssVisibilityControl", "Failed to show location icon for the full duration for notification: " + nfwNotification);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleLocationIconTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$showLocationIcon$5(java.lang.String proxyAppPkgName) {
        android.content.pm.ApplicationInfo proxyAppInfo = getProxyAppInfo(proxyAppPkgName);
        if (proxyAppInfo != null) {
            clearLocationIcon(this.mProxyAppsState.get(proxyAppPkgName), proxyAppInfo.uid, proxyAppPkgName);
        }
    }

    private void clearLocationIcon(com.android.server.location.gnss.GnssVisibilityControl.ProxyAppState proxyAppState, int uid, java.lang.String proxyAppPkgName) {
        updateLocationIcon(false, uid, proxyAppPkgName);
        if (proxyAppState != null) {
            proxyAppState.mIsLocationIconOn = false;
        }
        if (DEBUG) {
            android.util.Log.d("GnssVisibilityControl", "Location icon off. Uid: " + uid + ", proxyAppPkgName: " + proxyAppPkgName);
        }
    }

    private boolean updateLocationIcon(boolean displayLocationIcon, int uid, java.lang.String proxyAppPkgName) {
        if (displayLocationIcon) {
            if (this.mAppOps.startOpNoThrow(41, uid, proxyAppPkgName) != 0) {
                return false;
            }
            if (this.mAppOps.startOpNoThrow(42, uid, proxyAppPkgName) != 0) {
                this.mAppOps.finishOp(41, uid, proxyAppPkgName);
                return false;
            }
            return true;
        }
        this.mAppOps.finishOp(41, uid, proxyAppPkgName);
        this.mAppOps.finishOp(42, uid, proxyAppPkgName);
        return true;
    }

    private void handleEmergencyNfwNotification(com.android.server.location.gnss.GnssVisibilityControl.NfwNotification nfwNotification) {
        boolean isPermissionMismatched = false;
        if (!nfwNotification.isRequestAccepted()) {
            android.util.Log.e("GnssVisibilityControl", "Emergency non-framework location request incorrectly rejected. Notification: " + nfwNotification);
            isPermissionMismatched = true;
        }
        if (!this.mNiHandler.getInEmergency(EMERGENCY_EXTENSION_FOR_MISMATCH)) {
            android.util.Log.w("GnssVisibilityControl", "Emergency state mismatch. Device currently not in user initiated emergency session. Notification: " + nfwNotification);
            isPermissionMismatched = true;
        }
        logEvent(nfwNotification, isPermissionMismatched);
        if (nfwNotification.isLocationProvided()) {
            postEmergencyLocationUserNotification(nfwNotification);
        }
    }

    private void postEmergencyLocationUserNotification(com.android.server.location.gnss.GnssVisibilityControl.NfwNotification nfwNotification) {
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) this.mContext.getSystemService("notification");
        if (notificationManager == null) {
            android.util.Log.w("GnssVisibilityControl", "Could not notify user of emergency location request. Notification: " + nfwNotification);
        } else {
            notificationManager.notifyAsUser(null, 0, createEmergencyLocationUserNotification(this.mContext), android.os.UserHandle.ALL);
        }
    }

    private static android.app.Notification createEmergencyLocationUserNotification(android.content.Context context) {
        java.lang.String firstLineText = context.getString(android.R.string.gpsNotifMessage);
        java.lang.String secondLineText = context.getString(android.R.string.global_action_bug_report);
        java.lang.String accessibilityServicesText = firstLineText + " (" + secondLineText + ")";
        return new android.app.Notification.Builder(context, com.android.internal.notification.SystemNotificationChannels.NETWORK_STATUS).setSmallIcon(android.R.drawable.spinner_ab_pressed_holo_dark).setWhen(0L).setOngoing(false).setAutoCancel(true).setColor(context.getColor(android.R.color.system_notification_accent_color)).setDefaults(0).setTicker(accessibilityServicesText).setContentTitle(firstLineText).setContentText(secondLineText).build();
    }

    private void logEvent(com.android.server.location.gnss.GnssVisibilityControl.NfwNotification notification, boolean isPermissionMismatched) {
        com.android.internal.util.FrameworkStatsLog.write(131, notification.mProxyAppPackageName, notification.mProtocolStack, notification.mOtherProtocolStackName, notification.mRequestor, notification.mRequestorId, notification.mResponseType, notification.mInEmergencyMode, notification.mIsCachedLocation, isPermissionMismatched);
    }

    private void runOnHandler(java.lang.Runnable event) {
        this.mWakeLock.acquire(60000L);
        if (!this.mHandler.post(runEventAndReleaseWakeLock(event))) {
            this.mWakeLock.release();
        }
    }

    private java.lang.Runnable runEventAndReleaseWakeLock(final java.lang.Runnable event) {
        return new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssVisibilityControl$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$runEventAndReleaseWakeLock$6(event);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runEventAndReleaseWakeLock$6(java.lang.Runnable event) {
        try {
            event.run();
        } finally {
            this.mWakeLock.release();
        }
    }
}
