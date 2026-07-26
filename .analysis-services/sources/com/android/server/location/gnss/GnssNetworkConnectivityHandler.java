package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
class GnssNetworkConnectivityHandler {
    private static final int AGNSS_NET_CAPABILITY_NOT_METERED = 1;
    private static final int AGNSS_NET_CAPABILITY_NOT_ROAMING = 2;
    private static final int AGPS_DATA_CONNECTION_CLOSED = 0;
    private static final int AGPS_DATA_CONNECTION_OPEN = 2;
    private static final int AGPS_DATA_CONNECTION_OPENING = 1;
    public static final int AGPS_TYPE_C2K = 2;
    private static final int AGPS_TYPE_EIMS = 3;
    private static final int AGPS_TYPE_IMS = 4;
    public static final int AGPS_TYPE_SUPL = 1;
    private static final int APN_INVALID = 0;
    private static final int APN_IPV4 = 1;
    private static final int APN_IPV4V6 = 3;
    private static final int APN_IPV6 = 2;
    private static final int DEBUG_LEVEL_DEBUG = 4;
    private static final int DEBUG_LEVEL_ERROR = 1;
    private static final int DEBUG_LEVEL_IMPORTANT = 3;
    private static final int DEBUG_LEVEL_NONE = 0;
    private static final int DEBUG_LEVEL_VERBOSE = 5;
    private static final int DEBUG_LEVEL_WARNING = 2;
    private static final int GPS_AGPS_DATA_CONNECTED = 3;
    private static final int GPS_AGPS_DATA_CONN_DONE = 4;
    private static final int GPS_AGPS_DATA_CONN_FAILED = 5;
    private static final int GPS_RELEASE_AGPS_DATA_CONN = 2;
    private static final int GPS_REQUEST_AGPS_DATA_CONN = 1;
    private static final int HASH_MAP_INITIAL_CAPACITY_TO_TRACK_CONNECTED_NETWORKS = 5;
    private static final int SUPL_DATA_CONN_DATA_LOST = 4;
    private static final int SUPL_DATA_CONN_OK = 1;
    private static final int SUPL_DATA_CONN_RELEASE = 2;
    private static final int SUPL_DATA_CONN_RUNTIME_EXCEPTION = 3;
    private static final int SUPL_DATA_CONN_START = 0;
    private static final int SUPL_DATA_CONN_TIMEOUT = 5;
    private static final int SUPL_NETWORK_REQUEST_TIMEOUT_MILLIS = 20000;
    static final java.lang.String TAG = "GnssNetworkConnectivityHandler";
    private static final java.lang.String WAKELOCK_KEY = "GnssNetworkConnectivityHandler";
    private static final long WAKELOCK_TIMEOUT_MILLIS = 60000;
    private java.net.InetAddress mAGpsDataConnectionIpAddr;
    private int mAGpsDataConnectionState;
    private int mAGpsType;
    private final android.net.ConnectivityManager mConnMgr;
    private final android.content.Context mContext;
    private final com.android.server.location.gnss.GnssNetworkConnectivityHandler.GnssNetworkListener mGnssNetworkListener;
    private final android.os.Handler mHandler;
    private android.net.ConnectivityManager.NetworkCallback mNetworkConnectivityCallback;
    private final com.android.internal.location.GpsNetInitiatedHandler mNiHandler;
    private com.android.server.location.interfaces.IOplusLBSMainClass mOplusLbsClass;
    private java.util.HashMap<java.lang.Integer, com.android.server.location.gnss.GnssNetworkConnectivityHandler.SubIdPhoneStateListener> mPhoneStateListeners;
    private android.net.ConnectivityManager.NetworkCallback mSuplConnectivityCallback;
    private final android.os.PowerManager.WakeLock mWakeLock;
    private static boolean DEBUG = android.util.Log.isLoggable("GnssNetworkConnectivityHandler", 3);
    private static boolean VERBOSE = android.util.Log.isLoggable("GnssNetworkConnectivityHandler", 2);
    private static final long SUPL_CONNECTION_TIMEOUT_MILLIS = java.util.concurrent.TimeUnit.MINUTES.toMillis(1);
    private java.util.HashMap<android.net.Network, com.android.server.location.gnss.GnssNetworkConnectivityHandler.NetworkAttributes> mAvailableNetworkAttributes = new java.util.HashMap<>(5);
    private int mActiveSubId = -1;
    private final java.lang.Object mSuplConnectionReleaseOnTimeoutToken = new java.lang.Object();
    private final android.telephony.SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangeListener = new android.telephony.SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler.2
        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            android.telephony.TelephonyManager subIdTelManager;
            if (com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mPhoneStateListeners == null) {
                com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mPhoneStateListeners = new java.util.HashMap(2, 1.0f);
            }
            android.telephony.SubscriptionManager subManager = (android.telephony.SubscriptionManager) com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mContext.getSystemService(android.telephony.SubscriptionManager.class);
            android.telephony.TelephonyManager telManager = (android.telephony.TelephonyManager) com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mContext.getSystemService(android.telephony.TelephonyManager.class);
            if (subManager != null && telManager != null) {
                java.util.List<android.telephony.SubscriptionInfo> subscriptionInfoList = subManager.createForAllUserProfiles().getActiveSubscriptionInfoList();
                java.util.HashSet<java.lang.Integer> activeSubIds = new java.util.HashSet<>();
                if (subscriptionInfoList != null) {
                    if (com.android.server.location.gnss.GnssNetworkConnectivityHandler.DEBUG) {
                        android.util.Log.d("GnssNetworkConnectivityHandler", "Active Sub List size: " + subscriptionInfoList.size());
                    }
                    for (android.telephony.SubscriptionInfo subInfo : subscriptionInfoList) {
                        activeSubIds.add(java.lang.Integer.valueOf(subInfo.getSubscriptionId()));
                        if (!com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mPhoneStateListeners.containsKey(java.lang.Integer.valueOf(subInfo.getSubscriptionId())) && (subIdTelManager = telManager.createForSubscriptionId(subInfo.getSubscriptionId())) != null) {
                            if (com.android.server.location.gnss.GnssNetworkConnectivityHandler.DEBUG) {
                                android.util.Log.d("GnssNetworkConnectivityHandler", "Listener sub" + subInfo.getSubscriptionId());
                            }
                            com.android.server.location.gnss.GnssNetworkConnectivityHandler.SubIdPhoneStateListener subIdPhoneStateListener = com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.new SubIdPhoneStateListener(java.lang.Integer.valueOf(subInfo.getSubscriptionId()));
                            com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mPhoneStateListeners.put(java.lang.Integer.valueOf(subInfo.getSubscriptionId()), subIdPhoneStateListener);
                            subIdTelManager.listen(subIdPhoneStateListener, 2048);
                        }
                    }
                }
                java.util.Iterator<java.util.Map.Entry<java.lang.Integer, com.android.server.location.gnss.GnssNetworkConnectivityHandler.SubIdPhoneStateListener>> iterator = com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mPhoneStateListeners.entrySet().iterator();
                while (iterator.hasNext()) {
                    java.util.Map.Entry<java.lang.Integer, com.android.server.location.gnss.GnssNetworkConnectivityHandler.SubIdPhoneStateListener> element = iterator.next();
                    if (!activeSubIds.contains(element.getKey())) {
                        android.telephony.TelephonyManager subIdTelManager2 = telManager.createForSubscriptionId(element.getKey().intValue());
                        if (subIdTelManager2 == null) {
                            android.util.Log.e("GnssNetworkConnectivityHandler", "Telephony Manager for Sub " + element.getKey() + " null");
                        } else {
                            if (com.android.server.location.gnss.GnssNetworkConnectivityHandler.DEBUG) {
                                android.util.Log.d("GnssNetworkConnectivityHandler", "unregister listener sub " + element.getKey());
                            }
                            subIdTelManager2.listen(element.getValue(), 0);
                            iterator.remove();
                        }
                    }
                }
                if (!activeSubIds.contains(java.lang.Integer.valueOf(com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mActiveSubId))) {
                    com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mActiveSubId = -1;
                }
            }
        }
    };

    interface GnssNetworkListener {
        void onNetworkAvailable();
    }

    private native void native_agps_data_conn_closed();

    private native void native_agps_data_conn_failed();

    private native void native_agps_data_conn_open(long j, java.lang.String str, int i);

    private static native boolean native_is_agps_ril_supported();

    private native void native_update_network_state(boolean z, int i, boolean z2, boolean z3, java.lang.String str, long j, short s);

    private static class NetworkAttributes {
        private java.lang.String mApn;
        private android.net.NetworkCapabilities mCapabilities;
        private int mType;

        private NetworkAttributes() {
            this.mType = -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean hasCapabilitiesChanged(android.net.NetworkCapabilities curCapabilities, android.net.NetworkCapabilities newCapabilities) {
            return curCapabilities == null || newCapabilities == null || hasCapabilityChanged(curCapabilities, newCapabilities, 18) || hasCapabilityChanged(curCapabilities, newCapabilities, 11);
        }

        private static boolean hasCapabilityChanged(android.net.NetworkCapabilities curCapabilities, android.net.NetworkCapabilities newCapabilities, int capability) {
            return curCapabilities.hasCapability(capability) != newCapabilities.hasCapability(capability);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static short getCapabilityFlags(android.net.NetworkCapabilities capabilities) {
            short capabilityFlags = 0;
            if (capabilities.hasCapability(18)) {
                capabilityFlags = (short) (0 | 2);
            }
            if (capabilities.hasCapability(11)) {
                return (short) (capabilityFlags | 1);
            }
            return capabilityFlags;
        }
    }

    GnssNetworkConnectivityHandler(android.content.Context context, com.android.server.location.gnss.GnssNetworkConnectivityHandler.GnssNetworkListener gnssNetworkListener, android.os.Looper looper, com.android.internal.location.GpsNetInitiatedHandler niHandler) {
        this.mContext = context;
        this.mGnssNetworkListener = gnssNetworkListener;
        android.telephony.SubscriptionManager subManager = (android.telephony.SubscriptionManager) this.mContext.getSystemService(android.telephony.SubscriptionManager.class);
        if (subManager != null) {
            if (android.location.flags.Flags.subscriptionsChangedListenerThread()) {
                subManager.addOnSubscriptionsChangedListener(com.android.server.FgThread.getExecutor(), this.mOnSubscriptionsChangeListener);
            } else {
                subManager.addOnSubscriptionsChangedListener(this.mOnSubscriptionsChangeListener);
            }
        }
        android.os.PowerManager powerManager = (android.os.PowerManager) context.getSystemService("power");
        this.mWakeLock = powerManager.newWakeLock(1, "GnssNetworkConnectivityHandler");
        this.mHandler = new android.os.Handler(looper);
        this.mNiHandler = niHandler;
        this.mConnMgr = (android.net.ConnectivityManager) this.mContext.getSystemService("connectivity");
        this.mSuplConnectivityCallback = null;
        this.mOplusLbsClass = (com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, this.mContext);
        this.mOplusLbsClass.registerLbsConfigListener(new com.android.server.location.interfaces.IOplusConfigListener() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler.1
            @Override // com.android.server.location.interfaces.IOplusConfigListener
            public void onDebugLevelChanged(int level) {
                com.android.server.location.gnss.GnssNetworkConnectivityHandler.DEBUG = level >= 3;
                com.android.server.location.gnss.GnssNetworkConnectivityHandler.VERBOSE = level >= 5;
                android.util.Log.i("GnssNetworkConnectivityHandler", "onDebugLevelChanged, level: " + level + ", D: " + com.android.server.location.gnss.GnssNetworkConnectivityHandler.DEBUG + ", V:" + com.android.server.location.gnss.GnssNetworkConnectivityHandler.VERBOSE);
            }
        });
    }

    private final class SubIdPhoneStateListener extends android.telephony.PhoneStateListener {
        private java.lang.Integer mSubId;

        SubIdPhoneStateListener(java.lang.Integer subId) {
            this.mSubId = subId;
        }

        public void onPreciseCallStateChanged(android.telephony.PreciseCallState state) {
            if (1 == state.getForegroundCallState() || 3 == state.getForegroundCallState()) {
                com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mActiveSubId = this.mSubId.intValue();
                if (com.android.server.location.gnss.GnssNetworkConnectivityHandler.DEBUG) {
                    android.util.Log.d("GnssNetworkConnectivityHandler", "mActiveSubId: " + com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mActiveSubId);
                }
            }
        }
    }

    void registerNetworkCallbacks() {
        android.net.NetworkRequest.Builder networkRequestBuilder = new android.net.NetworkRequest.Builder();
        networkRequestBuilder.addCapability(12);
        networkRequestBuilder.addCapability(16);
        networkRequestBuilder.removeCapability(15);
        android.net.NetworkRequest networkRequest = networkRequestBuilder.build();
        this.mNetworkConnectivityCallback = createNetworkConnectivityCallback();
        this.mConnMgr.registerNetworkCallback(networkRequest, this.mNetworkConnectivityCallback, this.mHandler);
    }

    void unregisterNetworkCallbacks() {
        this.mConnMgr.unregisterNetworkCallback(this.mNetworkConnectivityCallback);
        this.mNetworkConnectivityCallback = null;
    }

    boolean isDataNetworkConnected() {
        android.net.NetworkInfo activeNetworkInfo = this.mConnMgr.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    int getActiveSubId() {
        return this.mActiveSubId;
    }

    void onReportAGpsStatus(final int agpsType, int agpsStatus, final byte[] suplIpAddr) {
        if (DEBUG) {
            android.util.Log.d("GnssNetworkConnectivityHandler", "AGPS_DATA_CONNECTION: " + agpsDataConnStatusAsString(agpsStatus));
        }
        switch (agpsStatus) {
            case 1:
                runOnHandler(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReportAGpsStatus$0(agpsType, suplIpAddr);
                    }
                });
                break;
            case 2:
                if (this.mAGpsDataConnectionState != 2) {
                    this.mOplusLbsClass.reportQcomConnectStatus(2);
                } else {
                    this.mOplusLbsClass.reportQcomConnectStatus(1);
                }
                runOnHandler(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReportAGpsStatus$1();
                    }
                });
                break;
            case 3:
            case 4:
            case 5:
                break;
            default:
                android.util.Log.w("GnssNetworkConnectivityHandler", "Received unknown AGPS status: " + agpsStatus);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onReportAGpsStatus$1() {
        handleReleaseSuplConnection(2);
    }

    private android.net.ConnectivityManager.NetworkCallback createNetworkConnectivityCallback() {
        return new android.net.ConnectivityManager.NetworkCallback() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler.3
            private java.util.HashMap<android.net.Network, android.net.NetworkCapabilities> mAvailableNetworkCapabilities = new java.util.HashMap<>(5);

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onCapabilitiesChanged(android.net.Network network, android.net.NetworkCapabilities capabilities) {
                if (!com.android.server.location.gnss.GnssNetworkConnectivityHandler.NetworkAttributes.hasCapabilitiesChanged(this.mAvailableNetworkCapabilities.get(network), capabilities)) {
                    if (com.android.server.location.gnss.GnssNetworkConnectivityHandler.VERBOSE) {
                        android.util.Log.v("GnssNetworkConnectivityHandler", "Relevant network capabilities unchanged. Capabilities: " + capabilities);
                    }
                } else {
                    this.mAvailableNetworkCapabilities.put(network, capabilities);
                    if (com.android.server.location.gnss.GnssNetworkConnectivityHandler.DEBUG) {
                        android.util.Log.d("GnssNetworkConnectivityHandler", "Network connected/capabilities updated. Available networks count: " + this.mAvailableNetworkCapabilities.size());
                    }
                    com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mGnssNetworkListener.onNetworkAvailable();
                    com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.handleUpdateNetworkState(network, true, capabilities);
                }
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(android.net.Network network) {
                if (this.mAvailableNetworkCapabilities.remove(network) == null) {
                    android.util.Log.w("GnssNetworkConnectivityHandler", "Incorrectly received network callback onLost() before onCapabilitiesChanged() for network: " + network);
                } else {
                    android.util.Log.i("GnssNetworkConnectivityHandler", "Network connection lost. Available networks count: " + this.mAvailableNetworkCapabilities.size());
                    com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.handleUpdateNetworkState(network, false, null);
                }
            }
        };
    }

    private android.net.ConnectivityManager.NetworkCallback createSuplConnectivityCallback() {
        return new android.net.ConnectivityManager.NetworkCallback() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler.4
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLinkPropertiesChanged(android.net.Network network, android.net.LinkProperties linkProperties) {
                if (com.android.server.location.gnss.GnssNetworkConnectivityHandler.DEBUG) {
                    android.util.Log.d("GnssNetworkConnectivityHandler", "SUPL network connection available.");
                }
                com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.handleSuplConnectionAvailable(network, linkProperties);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(android.net.Network network) {
                android.util.Log.i("GnssNetworkConnectivityHandler", "SUPL network connection lost.");
                com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.handleReleaseSuplConnection(2);
                com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mOplusLbsClass.reportQcomConnectStatus(4);
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onUnavailable() {
                android.util.Log.i("GnssNetworkConnectivityHandler", "SUPL network connection request timed out.");
                com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.handleReleaseSuplConnection(5);
                com.android.server.location.gnss.GnssNetworkConnectivityHandler.this.mOplusLbsClass.reportQcomConnectStatus(5);
            }
        };
    }

    private void runOnHandler(java.lang.Runnable event) {
        this.mWakeLock.acquire(60000L);
        if (!this.mHandler.post(runEventAndReleaseWakeLock(event))) {
            this.mWakeLock.release();
        }
    }

    private java.lang.Runnable runEventAndReleaseWakeLock(final java.lang.Runnable event) {
        return new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$runEventAndReleaseWakeLock$2(event);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runEventAndReleaseWakeLock$2(java.lang.Runnable event) {
        try {
            event.run();
        } finally {
            this.mWakeLock.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void handleUpdateNetworkState(android.net.Network r20, boolean r21, android.net.NetworkCapabilities r22) {
        /*
            r19 = this;
            r10 = r19
            r11 = r20
            r12 = r21
            r1 = 0
            android.content.Context r0 = r10.mContext
            java.lang.Class<android.telephony.TelephonyManager> r2 = android.telephony.TelephonyManager.class
            java.lang.Object r0 = r0.getSystemService(r2)
            r13 = r0
            android.telephony.TelephonyManager r13 = (android.telephony.TelephonyManager) r13
            r2 = 1
            if (r13 == 0) goto L29
            if (r12 == 0) goto L26
            boolean r0 = r13.getDataEnabled()     // Catch: java.lang.Exception -> L1f
            if (r0 == 0) goto L26
            r0 = r2
            goto L27
        L1f:
            r0 = move-exception
            r3 = r0
            r0 = r3
            r0.printStackTrace()
            goto L29
        L26:
            r0 = 0
        L27:
            r1 = r0
            goto L2a
        L29:
            r0 = r1
        L2a:
            r1 = r22
            com.android.server.location.gnss.GnssNetworkConnectivityHandler$NetworkAttributes r14 = r10.updateTrackedNetworksState(r12, r11, r1)
            java.lang.String r15 = com.android.server.location.gnss.GnssNetworkConnectivityHandler.NetworkAttributes.m4941$$Nest$fgetmApn(r14)
            int r16 = com.android.server.location.gnss.GnssNetworkConnectivityHandler.NetworkAttributes.m4943$$Nest$fgetmType(r14)
            android.net.NetworkCapabilities r9 = com.android.server.location.gnss.GnssNetworkConnectivityHandler.NetworkAttributes.m4942$$Nest$fgetmCapabilities(r14)
            java.lang.String r1 = r19.agpsDataConnStateAsString()
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r21)
            short r4 = com.android.server.location.gnss.GnssNetworkConnectivityHandler.NetworkAttributes.m4947$$Nest$smgetCapabilityFlags(r9)
            java.lang.Short r4 = java.lang.Short.valueOf(r4)
            java.util.HashMap<android.net.Network, com.android.server.location.gnss.GnssNetworkConnectivityHandler$NetworkAttributes> r5 = r10.mAvailableNetworkAttributes
            int r5 = r5.size()
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            java.lang.Object[] r1 = new java.lang.Object[]{r1, r3, r11, r4, r5}
            java.lang.String r3 = "updateNetworkState, state=%s, connected=%s, network=%s, capabilityFlags=%d, availableNetworkCount: %d"
            java.lang.String r1 = java.lang.String.format(r3, r1)
            java.lang.String r3 = "GnssNetworkConnectivityHandler"
            android.util.Log.i(r3, r1)
            boolean r1 = native_is_agps_ril_supported()
            if (r1 == 0) goto L94
        L6e:
            r1 = 18
            boolean r1 = r9.hasTransport(r1)
            r4 = r1 ^ 1
            if (r15 == 0) goto L7a
            r6 = r15
            goto L7d
        L7a:
            java.lang.String r1 = ""
            r6 = r1
        L7d:
            long r7 = r20.getNetworkHandle()
            short r17 = com.android.server.location.gnss.GnssNetworkConnectivityHandler.NetworkAttributes.m4947$$Nest$smgetCapabilityFlags(r9)
            r1 = r19
            r2 = r21
            r3 = r16
            r5 = r0
            r18 = r9
            r9 = r17
            r1.native_update_network_state(r2, r3, r4, r5, r6, r7, r9)
            goto L9f
        L94:
            r18 = r9
            boolean r1 = com.android.server.location.gnss.GnssNetworkConnectivityHandler.DEBUG
            if (r1 == 0) goto L9f
            java.lang.String r1 = "Skipped network state update because GPS HAL AGPS-RIL is not  supported"
            android.util.Log.d(r3, r1)
        L9f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.location.gnss.GnssNetworkConnectivityHandler.handleUpdateNetworkState(android.net.Network, boolean, android.net.NetworkCapabilities):void");
    }

    private com.android.server.location.gnss.GnssNetworkConnectivityHandler.NetworkAttributes updateTrackedNetworksState(boolean isConnected, android.net.Network network, android.net.NetworkCapabilities capabilities) {
        if (!isConnected) {
            return this.mAvailableNetworkAttributes.remove(network);
        }
        com.android.server.location.gnss.GnssNetworkConnectivityHandler.NetworkAttributes networkAttributes = this.mAvailableNetworkAttributes.get(network);
        if (networkAttributes != null) {
            networkAttributes.mCapabilities = capabilities;
            return networkAttributes;
        }
        com.android.server.location.gnss.GnssNetworkConnectivityHandler.NetworkAttributes networkAttributes2 = new com.android.server.location.gnss.GnssNetworkConnectivityHandler.NetworkAttributes();
        networkAttributes2.mCapabilities = capabilities;
        android.net.NetworkInfo info = this.mConnMgr.getNetworkInfo(network);
        if (info != null) {
            networkAttributes2.mApn = info.getExtraInfo();
            networkAttributes2.mType = info.getType();
        }
        this.mAvailableNetworkAttributes.put(network, networkAttributes2);
        return networkAttributes2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSuplConnectionAvailable(android.net.Network network, android.net.LinkProperties linkProperties) {
        android.net.NetworkInfo info = this.mConnMgr.getNetworkInfo(network);
        java.lang.String apn = null;
        if (info != null) {
            apn = info.getExtraInfo();
        }
        if (DEBUG) {
            java.lang.String message = java.lang.String.format("handleSuplConnectionAvailable: state=%s, suplNetwork=%s, info=%s", agpsDataConnStateAsString(), network, info);
            android.util.Log.d("GnssNetworkConnectivityHandler", message);
        }
        if (this.mAGpsDataConnectionState == 1) {
            if (apn == null) {
                apn = "dummy-apn";
            }
            if (this.mAGpsDataConnectionIpAddr != null) {
                setRouting();
            }
            int apnIpType = getLinkIpType(linkProperties);
            if (DEBUG) {
                java.lang.String message2 = java.lang.String.format("native_agps_data_conn_open: mAgpsApn=%s, mApnIpType=%s", apn, java.lang.Integer.valueOf(apnIpType));
                android.util.Log.d("GnssNetworkConnectivityHandler", message2);
            }
            native_agps_data_conn_open(network.getNetworkHandle(), apn, apnIpType);
            this.mAGpsDataConnectionState = 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleRequestSuplConnection, reason: merged with bridge method [inline-methods] */
    public void lambda$onReportAGpsStatus$0(int agpsType, byte[] suplIpAddr) {
        android.telephony.TelephonyManager telephonyManager;
        android.telephony.ServiceState state;
        this.mAGpsDataConnectionIpAddr = null;
        this.mAGpsType = agpsType;
        if (suplIpAddr != null) {
            if (VERBOSE) {
                android.util.Log.v("GnssNetworkConnectivityHandler", "Received SUPL IP addr[]: " + java.util.Arrays.toString(suplIpAddr));
            }
            try {
                this.mAGpsDataConnectionIpAddr = java.net.InetAddress.getByAddress(suplIpAddr);
                if (DEBUG) {
                    android.util.Log.d("GnssNetworkConnectivityHandler", "IP address converted to: " + this.mAGpsDataConnectionIpAddr);
                }
            } catch (java.net.UnknownHostException e) {
                android.util.Log.e("GnssNetworkConnectivityHandler", "Bad IP Address: " + java.util.Arrays.toString(suplIpAddr), e);
            }
        }
        if (DEBUG) {
            java.lang.String message = java.lang.String.format("requestSuplConnection, state=%s, agpsType=%s, address=%s", agpsDataConnStateAsString(), agpsTypeAsString(agpsType), this.mAGpsDataConnectionIpAddr);
            android.util.Log.d("GnssNetworkConnectivityHandler", message);
        }
        if (this.mAGpsDataConnectionState != 0) {
            return;
        }
        this.mAGpsDataConnectionState = 1;
        android.net.NetworkRequest.Builder networkRequestBuilder = new android.net.NetworkRequest.Builder();
        networkRequestBuilder.addCapability(getNetworkCapability(this.mAGpsType));
        networkRequestBuilder.addTransportType(0);
        if (com.android.internal.telephony.flags.Flags.satelliteInternet() && (telephonyManager = (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class)) != null && (state = telephonyManager.getServiceState()) != null && state.isUsingNonTerrestrialNetwork()) {
            networkRequestBuilder.removeCapability(13);
            try {
                networkRequestBuilder.addTransportType(10);
                networkRequestBuilder.removeCapability(37);
            } catch (java.lang.IllegalArgumentException e2) {
            }
        }
        if (this.mNiHandler.getInEmergency() && this.mActiveSubId >= 0) {
            if (DEBUG) {
                android.util.Log.d("GnssNetworkConnectivityHandler", "Adding Network Specifier: " + java.lang.Integer.toString(this.mActiveSubId));
            }
            networkRequestBuilder.setNetworkSpecifier(java.lang.Integer.toString(this.mActiveSubId));
            networkRequestBuilder.removeCapability(13);
        }
        android.net.NetworkRequest networkRequest = networkRequestBuilder.build();
        if (this.mSuplConnectivityCallback != null) {
            this.mConnMgr.unregisterNetworkCallback(this.mSuplConnectivityCallback);
        }
        this.mSuplConnectivityCallback = createSuplConnectivityCallback();
        try {
            this.mConnMgr.requestNetwork(networkRequest, this.mSuplConnectivityCallback, this.mHandler, SUPL_NETWORK_REQUEST_TIMEOUT_MILLIS);
            this.mOplusLbsClass.reportQcomConnectStatus(0);
            if (android.location.flags.Flags.releaseSuplConnectionOnTimeout()) {
                this.mHandler.removeCallbacksAndMessages(this.mSuplConnectionReleaseOnTimeoutToken);
                this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.location.gnss.GnssNetworkConnectivityHandler$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleRequestSuplConnection$3();
                    }
                }, this.mSuplConnectionReleaseOnTimeoutToken, SUPL_CONNECTION_TIMEOUT_MILLIS);
            }
        } catch (java.lang.RuntimeException e3) {
            android.util.Log.e("GnssNetworkConnectivityHandler", "Failed to request network.", e3);
            this.mSuplConnectivityCallback = null;
            handleReleaseSuplConnection(5);
            this.mOplusLbsClass.reportQcomConnectStatus(3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleRequestSuplConnection$3() {
        handleReleaseSuplConnection(2);
    }

    private int getNetworkCapability(int agpsType) {
        switch (agpsType) {
            case 1:
            case 2:
                return 1;
            case 3:
                return 10;
            case 4:
                return 4;
            default:
                throw new java.lang.IllegalArgumentException("agpsType: " + agpsType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleReleaseSuplConnection(int agpsDataConnStatus) {
        if (DEBUG) {
            java.lang.String message = java.lang.String.format("releaseSuplConnection, state=%s, status=%s", agpsDataConnStateAsString(), agpsDataConnStatusAsString(agpsDataConnStatus));
            android.util.Log.d("GnssNetworkConnectivityHandler", message);
        }
        if (android.location.flags.Flags.releaseSuplConnectionOnTimeout()) {
            this.mHandler.removeCallbacksAndMessages(this.mSuplConnectionReleaseOnTimeoutToken);
        }
        if (this.mAGpsDataConnectionState == 0) {
        }
        this.mAGpsDataConnectionState = 0;
        if (this.mSuplConnectivityCallback != null) {
            this.mConnMgr.unregisterNetworkCallback(this.mSuplConnectivityCallback);
            this.mSuplConnectivityCallback = null;
        }
        switch (agpsDataConnStatus) {
            case 2:
                native_agps_data_conn_closed();
                break;
            case 5:
                native_agps_data_conn_failed();
                break;
            default:
                android.util.Log.e("GnssNetworkConnectivityHandler", "Invalid status to release SUPL connection: " + agpsDataConnStatus);
                break;
        }
    }

    private void setRouting() {
        boolean result = this.mConnMgr.requestRouteToHostAddress(3, this.mAGpsDataConnectionIpAddr);
        if (!result) {
            android.util.Log.e("GnssNetworkConnectivityHandler", "Error requesting route to host: " + this.mAGpsDataConnectionIpAddr);
        } else if (DEBUG) {
            android.util.Log.d("GnssNetworkConnectivityHandler", "Successfully requested route to host: " + this.mAGpsDataConnectionIpAddr);
        }
    }

    private void ensureInHandlerThread() {
        if (this.mHandler != null && android.os.Looper.myLooper() == this.mHandler.getLooper()) {
        } else {
            throw new java.lang.IllegalStateException("This method must run on the Handler thread.");
        }
    }

    private java.lang.String agpsDataConnStateAsString() {
        switch (this.mAGpsDataConnectionState) {
            case 0:
                return "CLOSED";
            case 1:
                return "OPENING";
            case 2:
                return "OPEN";
            default:
                return "<Unknown>(" + this.mAGpsDataConnectionState + ")";
        }
    }

    private java.lang.String agpsDataConnStatusAsString(int agpsDataConnStatus) {
        switch (agpsDataConnStatus) {
            case 1:
                return "REQUEST";
            case 2:
                return "RELEASE";
            case 3:
                return "CONNECTED";
            case 4:
                return "DONE";
            case 5:
                return "FAILED";
            default:
                return "<Unknown>(" + agpsDataConnStatus + ")";
        }
    }

    private java.lang.String agpsTypeAsString(int agpsType) {
        switch (agpsType) {
            case 1:
                return "SUPL";
            case 2:
                return "C2K";
            case 3:
                return "EIMS";
            case 4:
                return "IMS";
            default:
                return "<Unknown>(" + agpsType + ")";
        }
    }

    private int getLinkIpType(android.net.LinkProperties linkProperties) {
        ensureInHandlerThread();
        boolean isIPv4 = false;
        boolean isIPv6 = false;
        java.util.List<android.net.LinkAddress> linkAddresses = linkProperties.getLinkAddresses();
        for (android.net.LinkAddress linkAddress : linkAddresses) {
            java.net.InetAddress inetAddress = linkAddress.getAddress();
            if (inetAddress instanceof java.net.Inet4Address) {
                isIPv4 = true;
            } else if (inetAddress instanceof java.net.Inet6Address) {
                isIPv6 = true;
            }
            if (DEBUG) {
                android.util.Log.d("GnssNetworkConnectivityHandler", "LinkAddress : " + inetAddress.toString());
            }
        }
        if (isIPv4 && isIPv6) {
            return 3;
        }
        if (isIPv4) {
            return 1;
        }
        if (isIPv6) {
            return 2;
        }
        return 0;
    }

    protected boolean isNativeAgpsRilSupported() {
        return native_is_agps_ril_supported();
    }
}
