package com.android.server.net;

/* JADX INFO: loaded from: classes2.dex */
public class LockdownVpnTracker {
    public static final java.lang.String ACTION_LOCKDOWN_RESET = "com.android.server.action.LOCKDOWN_RESET";
    private static final java.lang.String TAG = "LockdownVpnTracker";
    private java.lang.String mAcceptedEgressIface;
    private final android.net.ConnectivityManager mCm;
    private final android.app.PendingIntent mConfigIntent;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final android.app.NotificationManager mNotificationManager;
    private final com.android.internal.net.VpnProfile mProfile;
    private final android.app.PendingIntent mResetIntent;
    private final com.android.server.connectivity.Vpn mVpn;
    private final java.lang.Object mStateLock = new java.lang.Object();
    private final com.android.server.net.LockdownVpnTracker.NetworkCallback mDefaultNetworkCallback = new com.android.server.net.LockdownVpnTracker.NetworkCallback();
    private final com.android.server.net.LockdownVpnTracker.VpnNetworkCallback mVpnNetworkCallback = new com.android.server.net.LockdownVpnTracker.VpnNetworkCallback();

    private class NetworkCallback extends android.net.ConnectivityManager.NetworkCallback {
        private android.net.LinkProperties mLinkProperties;
        private android.net.Network mNetwork;

        private NetworkCallback() {
            this.mNetwork = null;
            this.mLinkProperties = null;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(android.net.Network network, android.net.LinkProperties lp) {
            boolean networkChanged = false;
            if (!network.equals(this.mNetwork)) {
                this.mNetwork = network;
                networkChanged = true;
            }
            this.mLinkProperties = lp;
            if (networkChanged) {
                synchronized (com.android.server.net.LockdownVpnTracker.this.mStateLock) {
                    com.android.server.net.LockdownVpnTracker.this.handleStateChangedLocked();
                }
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(android.net.Network network) {
            this.mNetwork = null;
            this.mLinkProperties = null;
            synchronized (com.android.server.net.LockdownVpnTracker.this.mStateLock) {
                com.android.server.net.LockdownVpnTracker.this.handleStateChangedLocked();
            }
        }

        public android.net.Network getNetwork() {
            return this.mNetwork;
        }

        public android.net.LinkProperties getLinkProperties() {
            return this.mLinkProperties;
        }
    }

    private class VpnNetworkCallback extends com.android.server.net.LockdownVpnTracker.NetworkCallback {
        private VpnNetworkCallback() {
            super();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(android.net.Network network) {
            synchronized (com.android.server.net.LockdownVpnTracker.this.mStateLock) {
                com.android.server.net.LockdownVpnTracker.this.handleStateChangedLocked();
            }
        }

        @Override // com.android.server.net.LockdownVpnTracker.NetworkCallback, android.net.ConnectivityManager.NetworkCallback
        public void onLost(android.net.Network network) {
            onAvailable(network);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public LockdownVpnTracker(android.content.Context context, android.os.Handler handler, com.android.server.connectivity.Vpn vpn, com.android.internal.net.VpnProfile vpnProfile) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mCm = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
        this.mHandler = (android.os.Handler) java.util.Objects.requireNonNull(handler);
        this.mVpn = (com.android.server.connectivity.Vpn) java.util.Objects.requireNonNull(vpn);
        this.mProfile = (com.android.internal.net.VpnProfile) java.util.Objects.requireNonNull(vpnProfile);
        this.mNotificationManager = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        this.mConfigIntent = android.app.PendingIntent.getActivity(this.mContext, 0, new android.content.Intent("android.settings.VPN_SETTINGS"), 67108864);
        android.content.Intent intent = new android.content.Intent(ACTION_LOCKDOWN_RESET);
        intent.addFlags(1073741824);
        this.mResetIntent = android.app.PendingIntent.getBroadcast(this.mContext, 0, intent, 67108864);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStateChangedLocked() {
        android.net.Network network = this.mDefaultNetworkCallback.getNetwork();
        android.net.LinkProperties egressProp = this.mDefaultNetworkCallback.getLinkProperties();
        android.net.NetworkInfo vpnInfo = this.mVpn.getNetworkInfo();
        com.android.internal.net.VpnConfig vpnConfig = this.mVpn.getLegacyVpnConfig();
        boolean egressChanged = true;
        boolean egressDisconnected = network == null;
        if (egressProp != null && android.text.TextUtils.equals(this.mAcceptedEgressIface, egressProp.getInterfaceName())) {
            egressChanged = false;
        }
        java.lang.String egressIface = egressProp == null ? null : egressProp.getInterfaceName();
        android.util.Log.d(TAG, "handleStateChanged: egress=" + this.mAcceptedEgressIface + "->" + egressIface);
        if (egressDisconnected || egressChanged) {
            this.mAcceptedEgressIface = null;
            this.mVpn.stopVpnRunnerPrivileged();
        }
        if (egressDisconnected) {
            hideNotification();
            return;
        }
        if (!vpnInfo.isConnectedOrConnecting()) {
            if (!this.mProfile.isValidLockdownProfile()) {
                android.util.Log.e(TAG, "Invalid VPN profile; requires IP-based server and DNS");
                showNotification(android.R.string.usb_apm_usb_plugged_in_when_locked_notification_title, android.R.drawable.tab_selected_pressed_holo);
                return;
            }
            android.util.Log.d(TAG, "Active network connected; starting VPN");
            showNotification(android.R.string.usb_accessory_notification_title, android.R.drawable.tab_selected_pressed_holo);
            this.mAcceptedEgressIface = egressIface;
            try {
                this.mVpn.startLegacyVpnPrivileged(this.mProfile);
                return;
            } catch (java.lang.IllegalStateException e) {
                this.mAcceptedEgressIface = null;
                android.util.Log.e(TAG, "Failed to start VPN", e);
                showNotification(android.R.string.usb_apm_usb_plugged_in_when_locked_notification_title, android.R.drawable.tab_selected_pressed_holo);
                return;
            }
        }
        if (vpnInfo.isConnected() && vpnConfig != null) {
            java.lang.String iface = vpnConfig.interfaze;
            java.util.List<android.net.LinkAddress> sourceAddrs = vpnConfig.addresses;
            android.util.Log.d(TAG, "VPN connected using iface=" + iface + ", sourceAddr=" + sourceAddrs.toString());
            showNotification(android.R.string.upload_file, android.R.drawable.tab_selected_holo);
        }
    }

    public void init() {
        synchronized (this.mStateLock) {
            initLocked();
        }
    }

    private void initLocked() {
        android.util.Log.d(TAG, "initLocked()");
        this.mVpn.setEnableTeardown(false);
        this.mVpn.setLockdown(true);
        this.mCm.setLegacyLockdownVpnEnabled(true);
        handleStateChangedLocked();
        this.mCm.registerSystemDefaultNetworkCallback(this.mDefaultNetworkCallback, this.mHandler);
        android.net.NetworkRequest vpnRequest = new android.net.NetworkRequest.Builder().clearCapabilities().addTransportType(4).build();
        this.mCm.registerNetworkCallback(vpnRequest, this.mVpnNetworkCallback, this.mHandler);
    }

    public void shutdown() {
        synchronized (this.mStateLock) {
            shutdownLocked();
        }
    }

    private void shutdownLocked() {
        android.util.Log.d(TAG, "shutdownLocked()");
        this.mAcceptedEgressIface = null;
        this.mVpn.stopVpnRunnerPrivileged();
        this.mVpn.setLockdown(false);
        this.mCm.setLegacyLockdownVpnEnabled(false);
        hideNotification();
        this.mVpn.setEnableTeardown(true);
        this.mCm.unregisterNetworkCallback(this.mDefaultNetworkCallback);
        this.mCm.unregisterNetworkCallback(this.mVpnNetworkCallback);
    }

    public void reset() {
        android.util.Log.d(TAG, "reset()");
        synchronized (this.mStateLock) {
            shutdownLocked();
            initLocked();
            handleStateChangedLocked();
        }
    }

    private void showNotification(int titleRes, int iconRes) {
        android.app.Notification.Builder builder = new android.app.Notification.Builder(this.mContext, "VPN").setWhen(0L).setSmallIcon(iconRes).setContentTitle(this.mContext.getString(titleRes)).setContentText(this.mContext.getString(android.R.string.unsupported_display_size_show)).setContentIntent(this.mConfigIntent).setOngoing(true).addAction(android.R.drawable.ic_media_route_on_2_holo_light, this.mContext.getString(android.R.string.reason_service_unavailable), this.mResetIntent).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color));
        this.mNotificationManager.notify(null, 20, builder.build());
    }

    private void hideNotification() {
        this.mNotificationManager.cancel(null, 20);
    }
}
