package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
class LegacyBluetoothRouteController implements com.android.server.media.BluetoothRouteController {
    private static final java.lang.String HEARING_AID_ROUTE_ID_PREFIX = "HEARING_AID_";
    private static final java.lang.String LE_AUDIO_ROUTE_ID_PREFIX = "LE_AUDIO_";
    private android.bluetooth.BluetoothA2dp mA2dpProfile;
    private final android.media.AudioManager mAudioManager;
    private final android.bluetooth.BluetoothAdapter mBluetoothAdapter;
    private final android.content.Context mContext;
    private android.bluetooth.BluetoothHearingAid mHearingAidProfile;
    private android.bluetooth.BluetoothLeAudio mLeAudioProfile;
    private final com.android.server.media.BluetoothRouteController.BluetoothRoutesUpdatedListener mListener;
    private static final java.lang.String TAG = "LBtRouteProvider";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private final java.util.Map<java.lang.String, com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo> mBluetoothRoutes = new java.util.HashMap();
    private final java.util.List<com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo> mActiveRoutes = new java.util.ArrayList();
    private final android.util.SparseIntArray mVolumeMap = new android.util.SparseIntArray();
    private final com.android.server.media.LegacyBluetoothRouteController.BluetoothProfileListener mProfileListener = new com.android.server.media.LegacyBluetoothRouteController.BluetoothProfileListener();
    final java.lang.Object mBtRoutesLock = new java.lang.Object();
    private final com.android.server.media.LegacyBluetoothRouteController.AdapterStateChangedReceiver mAdapterStateChangedReceiver = new com.android.server.media.LegacyBluetoothRouteController.AdapterStateChangedReceiver();
    private final com.android.server.media.LegacyBluetoothRouteController.DeviceStateChangedReceiver mDeviceStateChangedReceiver = new com.android.server.media.LegacyBluetoothRouteController.DeviceStateChangedReceiver();

    /* JADX WARN: Multi-variable type inference failed */
    LegacyBluetoothRouteController(android.content.Context context, android.bluetooth.BluetoothAdapter bluetoothAdapter, com.android.server.media.BluetoothRouteController.BluetoothRoutesUpdatedListener bluetoothRoutesUpdatedListener) {
        this.mContext = context;
        this.mBluetoothAdapter = bluetoothAdapter;
        this.mListener = bluetoothRoutesUpdatedListener;
        this.mAudioManager = (android.media.AudioManager) this.mContext.getSystemService("audio");
        buildBluetoothRoutes();
    }

    @Override // com.android.server.media.BluetoothRouteController
    public void start(android.os.UserHandle user) {
        this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mProfileListener, 2);
        this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mProfileListener, 21);
        this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mProfileListener, 22);
        android.content.IntentFilter adapterStateChangedIntentFilter = new android.content.IntentFilter();
        adapterStateChangedIntentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mContext.registerReceiverAsUser(this.mAdapterStateChangedReceiver, user, adapterStateChangedIntentFilter, null, null);
        android.content.IntentFilter deviceStateChangedIntentFilter = new android.content.IntentFilter();
        deviceStateChangedIntentFilter.addAction("android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED");
        deviceStateChangedIntentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        deviceStateChangedIntentFilter.addAction("android.bluetooth.hearingaid.profile.action.ACTIVE_DEVICE_CHANGED");
        deviceStateChangedIntentFilter.addAction("android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED");
        deviceStateChangedIntentFilter.addAction("android.bluetooth.action.LE_AUDIO_CONNECTION_STATE_CHANGED");
        deviceStateChangedIntentFilter.addAction("android.bluetooth.action.LE_AUDIO_ACTIVE_DEVICE_CHANGED");
        this.mContext.registerReceiverAsUser(this.mDeviceStateChangedReceiver, user, deviceStateChangedIntentFilter, null, null);
    }

    @Override // com.android.server.media.BluetoothRouteController
    public void stop() {
        this.mContext.unregisterReceiver(this.mAdapterStateChangedReceiver);
        this.mContext.unregisterReceiver(this.mDeviceStateChangedReceiver);
    }

    @Override // com.android.server.media.BluetoothRouteController
    public void transferTo(java.lang.String routeId) {
        if (routeId == null) {
            clearActiveDevices();
            return;
        }
        com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRouteInfo = findBluetoothRouteWithRouteId(routeId);
        if (btRouteInfo == null) {
            android.util.Slog.w(TAG, "transferTo: Unknown route. ID=" + routeId);
        } else if (this.mBluetoothAdapter != null) {
            this.mBluetoothAdapter.setActiveDevice(btRouteInfo.mBtDevice, 0);
        }
    }

    private com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo findBluetoothRouteWithRouteId(java.lang.String routeId) {
        if (routeId == null) {
            return null;
        }
        for (com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRouteInfo : this.mBluetoothRoutes.values()) {
            if (android.text.TextUtils.equals(btRouteInfo.mRoute.getId(), routeId)) {
                return btRouteInfo;
            }
        }
        return null;
    }

    private void clearActiveDevices() {
        if (this.mBluetoothAdapter != null) {
            this.mBluetoothAdapter.removeActiveDevice(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildBluetoothRoutes() {
        this.mBluetoothRoutes.clear();
        java.util.Set<android.bluetooth.BluetoothDevice> bondedDevices = this.mBluetoothAdapter.getBondedDevices();
        if (bondedDevices != null) {
            for (android.bluetooth.BluetoothDevice device : bondedDevices) {
                if (device.isConnected()) {
                    com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo newBtRoute = createBluetoothRoute(device);
                    if (newBtRoute.mConnectedProfiles.size() > 0) {
                        this.mBluetoothRoutes.put(device.getAddress(), newBtRoute);
                    }
                }
            }
        }
    }

    @Override // com.android.server.media.BluetoothRouteController
    public android.media.MediaRoute2Info getSelectedRoute() {
        if (this.mActiveRoutes.isEmpty()) {
            return null;
        }
        return this.mActiveRoutes.get(0).mRoute;
    }

    @Override // com.android.server.media.BluetoothRouteController
    public java.util.List<android.media.MediaRoute2Info> getTransferableRoutes() {
        java.util.List<android.media.MediaRoute2Info> routes = getAllBluetoothRoutes();
        for (com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRoute : this.mActiveRoutes) {
            routes.remove(btRoute.mRoute);
        }
        return routes;
    }

    @Override // com.android.server.media.BluetoothRouteController
    public java.util.List<android.media.MediaRoute2Info> getAllBluetoothRoutes() {
        java.util.List<android.media.MediaRoute2Info> routes = new java.util.ArrayList<>();
        java.util.List<java.lang.String> routeIds = new java.util.ArrayList<>();
        android.media.MediaRoute2Info selectedRoute = getSelectedRoute();
        if (selectedRoute != null) {
            routes.add(selectedRoute);
            routeIds.add(selectedRoute.getId());
        }
        for (com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRoute : this.mBluetoothRoutes.values()) {
            if (!routeIds.contains(btRoute.mRoute.getId())) {
                routes.add(btRoute.mRoute);
                routeIds.add(btRoute.mRoute.getId());
            }
        }
        return routes;
    }

    @Override // com.android.server.media.BluetoothRouteController
    public boolean updateVolumeForDevices(int devices, int volume) {
        int routeType;
        if ((134217728 & devices) != 0) {
            routeType = 23;
        } else {
            int routeType2 = devices & android.hardware.audio.common.V2_0.AudioDevice.OUT_ALL_A2DP;
            if (routeType2 != 0) {
                routeType = 8;
            } else if ((536870912 & devices) != 0) {
                routeType = 26;
            } else {
                return false;
            }
        }
        this.mVolumeMap.put(routeType, volume);
        boolean shouldNotify = false;
        for (com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRoute : this.mActiveRoutes) {
            if (btRoute.mRoute.getType() == routeType) {
                btRoute.mRoute = new android.media.MediaRoute2Info.Builder(btRoute.mRoute).setVolume(volume).build();
                shouldNotify = true;
            }
        }
        if (shouldNotify) {
            notifyBluetoothRoutesUpdated();
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyBluetoothRoutesUpdated() {
        if (this.mListener != null) {
            this.mListener.onBluetoothRoutesUpdated();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo createBluetoothRoute(android.bluetooth.BluetoothDevice device) {
        java.lang.String deviceName;
        com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo newBtRoute = new com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo();
        newBtRoute.mBtDevice = device;
        java.lang.String routeId = device.getAddress();
        if (com.android.media.flags.Flags.enableUseOfBluetoothDeviceGetAliasForMr2infoGetName()) {
            deviceName = device.getAlias();
        } else {
            deviceName = device.getName();
        }
        if (android.text.TextUtils.isEmpty(deviceName)) {
            deviceName = this.mContext.getResources().getText(android.R.string.unknownName).toString();
        }
        int type = 8;
        newBtRoute.mConnectedProfiles = new android.util.SparseBooleanArray();
        if (this.mA2dpProfile != null && this.mA2dpProfile.getConnectedDevices().contains(device)) {
            newBtRoute.mConnectedProfiles.put(2, true);
        }
        if (this.mHearingAidProfile != null && this.mHearingAidProfile.getConnectedDevices().contains(device)) {
            newBtRoute.mConnectedProfiles.put(21, true);
            routeId = HEARING_AID_ROUTE_ID_PREFIX + this.mHearingAidProfile.getHiSyncId(device);
            type = 23;
        }
        if (this.mLeAudioProfile != null && this.mLeAudioProfile.getConnectedDevices().contains(device)) {
            newBtRoute.mConnectedProfiles.put(22, true);
            routeId = LE_AUDIO_ROUTE_ID_PREFIX + this.mLeAudioProfile.getGroupId(device);
            type = 26;
        }
        newBtRoute.mRoute = new android.media.MediaRoute2Info.Builder(routeId, deviceName).addFeature("android.media.route.feature.LIVE_AUDIO").addFeature("android.media.route.feature.LOCAL_PLAYBACK").setConnectionState(0).setDescription(this.mContext.getResources().getText(android.R.string.bugreport_option_full_title).toString()).setType(type).setVolumeHandling(1).setVolumeMax(this.mAudioManager.getStreamMaxVolume(3)).setAddress(device.getAddress()).build();
        return newBtRoute;
    }

    private void setRouteConnectionState(com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRoute, int state) {
        if (btRoute == null) {
            android.util.Slog.w(TAG, "setRouteConnectionState: route shouldn't be null");
            return;
        }
        if (btRoute.mRoute.getConnectionState() == state) {
            return;
        }
        android.media.MediaRoute2Info.Builder builder = new android.media.MediaRoute2Info.Builder(btRoute.mRoute).setConnectionState(state);
        builder.setType(btRoute.getRouteType());
        if (state == 2) {
            builder.setVolume(this.mVolumeMap.get(btRoute.getRouteType(), 0));
        }
        btRoute.mRoute = builder.build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addActiveRoute(com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRoute) {
        if (btRoute == null) {
            android.util.Slog.w(TAG, "addActiveRoute: btRoute is null");
            return;
        }
        if (DEBUG) {
            android.util.Log.d(TAG, "Adding active route: " + btRoute.mRoute);
        }
        if (this.mActiveRoutes.contains(btRoute)) {
            android.util.Slog.w(TAG, "addActiveRoute: btRoute is already added.");
        } else {
            setRouteConnectionState(btRoute, 2);
            this.mActiveRoutes.add(btRoute);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeActiveRoute(com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRoute) {
        if (DEBUG) {
            android.util.Log.d(TAG, "Removing active route: " + btRoute.mRoute);
        }
        if (this.mActiveRoutes.remove(btRoute)) {
            setRouteConnectionState(btRoute, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearActiveRoutesWithType(int type) {
        if (DEBUG) {
            android.util.Log.d(TAG, "Clearing active routes with type. type=" + type);
        }
        java.util.Iterator<com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo> iter = this.mActiveRoutes.iterator();
        while (iter.hasNext()) {
            com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRoute = iter.next();
            if (btRoute.mRoute.getType() == type) {
                iter.remove();
                setRouteConnectionState(btRoute, 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addActiveDevices(android.bluetooth.BluetoothDevice device) {
        com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo activeBtRoute = this.mBluetoothRoutes.get(device.getAddress());
        if (activeBtRoute == null) {
            activeBtRoute = createBluetoothRoute(device);
            this.mBluetoothRoutes.put(device.getAddress(), activeBtRoute);
        }
        addActiveRoute(activeBtRoute);
        for (com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRoute : this.mBluetoothRoutes.values()) {
            if (android.text.TextUtils.equals(btRoute.mRoute.getId(), activeBtRoute.mRoute.getId()) && !android.text.TextUtils.equals(btRoute.mBtDevice.getAddress(), activeBtRoute.mBtDevice.getAddress())) {
                addActiveRoute(btRoute);
            }
        }
    }

    private static class BluetoothRouteInfo {
        private android.bluetooth.BluetoothDevice mBtDevice;
        private android.util.SparseBooleanArray mConnectedProfiles;
        private android.media.MediaRoute2Info mRoute;

        private BluetoothRouteInfo() {
        }

        int getRouteType() {
            if (this.mConnectedProfiles.get(21, false)) {
                return 23;
            }
            if (this.mConnectedProfiles.get(22, false)) {
                return 26;
            }
            return 8;
        }
    }

    private final class BluetoothProfileListener implements android.bluetooth.BluetoothProfile.ServiceListener {
        private BluetoothProfileListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int profile, android.bluetooth.BluetoothProfile proxy) {
            java.util.List<android.bluetooth.BluetoothDevice> activeDevices;
            switch (profile) {
                case 2:
                    com.android.server.media.LegacyBluetoothRouteController.this.mA2dpProfile = (android.bluetooth.BluetoothA2dp) proxy;
                    activeDevices = com.android.server.media.LegacyBluetoothRouteController.this.mBluetoothAdapter.getActiveDevices(2);
                    break;
                case 21:
                    com.android.server.media.LegacyBluetoothRouteController.this.mHearingAidProfile = (android.bluetooth.BluetoothHearingAid) proxy;
                    activeDevices = com.android.server.media.LegacyBluetoothRouteController.this.mBluetoothAdapter.getActiveDevices(21);
                    break;
                case 22:
                    com.android.server.media.LegacyBluetoothRouteController.this.mLeAudioProfile = (android.bluetooth.BluetoothLeAudio) proxy;
                    activeDevices = com.android.server.media.LegacyBluetoothRouteController.this.mBluetoothAdapter.getActiveDevices(22);
                    break;
                default:
                    return;
            }
            synchronized (com.android.server.media.LegacyBluetoothRouteController.this.mBtRoutesLock) {
                for (android.bluetooth.BluetoothDevice device : proxy.getConnectedDevices()) {
                    com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRoute = (com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo) com.android.server.media.LegacyBluetoothRouteController.this.mBluetoothRoutes.get(device.getAddress());
                    if (btRoute == null) {
                        btRoute = com.android.server.media.LegacyBluetoothRouteController.this.createBluetoothRoute(device);
                        com.android.server.media.LegacyBluetoothRouteController.this.mBluetoothRoutes.put(device.getAddress(), btRoute);
                    }
                    if (activeDevices.contains(device)) {
                        com.android.server.media.LegacyBluetoothRouteController.this.addActiveRoute(btRoute);
                    }
                }
                com.android.server.media.LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int profile) {
            switch (profile) {
                case 2:
                    com.android.server.media.LegacyBluetoothRouteController.this.mA2dpProfile = null;
                    break;
                case 21:
                    com.android.server.media.LegacyBluetoothRouteController.this.mHearingAidProfile = null;
                    break;
                case 22:
                    com.android.server.media.LegacyBluetoothRouteController.this.mLeAudioProfile = null;
                    break;
            }
        }
    }

    private class AdapterStateChangedReceiver extends android.content.BroadcastReceiver {
        private AdapterStateChangedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
            if (state == 10 || state == 13) {
                com.android.server.media.LegacyBluetoothRouteController.this.mBluetoothRoutes.clear();
                com.android.server.media.LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
            } else if (state == 12) {
                com.android.server.media.LegacyBluetoothRouteController.this.buildBluetoothRoutes();
                if (!com.android.server.media.LegacyBluetoothRouteController.this.mBluetoothRoutes.isEmpty()) {
                    com.android.server.media.LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
                }
            }
        }
    }

    private class DeviceStateChangedReceiver extends android.content.BroadcastReceiver {
        private DeviceStateChangedReceiver() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0053  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r7, android.content.Intent r8) {
            /*
                Method dump skipped, instruction units count: 320
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.LegacyBluetoothRouteController.DeviceStateChangedReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }

        private void handleConnectionStateChanged(int profile, android.content.Intent intent, android.bluetooth.BluetoothDevice device) {
            int state = intent.getIntExtra("android.bluetooth.profile.extra.STATE", -1);
            com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRoute = (com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo) com.android.server.media.LegacyBluetoothRouteController.this.mBluetoothRoutes.get(device.getAddress());
            if (state == 2) {
                if (btRoute == null) {
                    com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo btRoute2 = com.android.server.media.LegacyBluetoothRouteController.this.createBluetoothRoute(device);
                    if (btRoute2.mConnectedProfiles.size() > 0) {
                        com.android.server.media.LegacyBluetoothRouteController.this.mBluetoothRoutes.put(device.getAddress(), btRoute2);
                        com.android.server.media.LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
                        return;
                    }
                    return;
                }
                btRoute.mConnectedProfiles.put(profile, true);
                return;
            }
            if ((state == 3 || state == 0) && btRoute != null) {
                btRoute.mConnectedProfiles.delete(profile);
                if (btRoute.mConnectedProfiles.size() == 0) {
                    com.android.server.media.LegacyBluetoothRouteController.this.removeActiveRoute((com.android.server.media.LegacyBluetoothRouteController.BluetoothRouteInfo) com.android.server.media.LegacyBluetoothRouteController.this.mBluetoothRoutes.remove(device.getAddress()));
                    com.android.server.media.LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
                }
            }
        }
    }
}
