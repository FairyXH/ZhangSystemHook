package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
class BluetoothDeviceRoutesManager {
    private static final java.lang.String HEARING_AID_ROUTE_ID_PREFIX = "HEARING_AID_";
    private static final java.lang.String LE_AUDIO_ROUTE_ID_PREFIX = "LE_AUDIO_";
    private static final java.lang.String TAG = "MR2SystemProvider";
    private final com.android.server.media.BluetoothDeviceRoutesManager.AdapterStateChangedReceiver mAdapterStateChangedReceiver;
    private java.util.Map<java.lang.String, android.bluetooth.BluetoothDevice> mAddressToBondedDevice;
    private final android.bluetooth.BluetoothAdapter mBluetoothAdapter;
    private final com.android.server.media.BluetoothProfileMonitor mBluetoothProfileMonitor;
    private final java.util.Map<java.lang.String, com.android.server.media.BluetoothDeviceRoutesManager.BluetoothRouteInfo> mBluetoothRoutes;
    private final android.content.Context mContext;
    private final com.android.server.media.BluetoothDeviceRoutesManager.DeviceStateChangedReceiver mDeviceStateChangedReceiver;
    private final android.os.Handler mHandler;
    private final com.android.server.media.BluetoothRouteController.BluetoothRoutesUpdatedListener mListener;

    BluetoothDeviceRoutesManager(android.content.Context context, android.os.Handler handler, android.bluetooth.BluetoothAdapter bluetoothAdapter, com.android.server.media.BluetoothRouteController.BluetoothRoutesUpdatedListener listener) {
        this(context, handler, bluetoothAdapter, new com.android.server.media.BluetoothProfileMonitor(context, bluetoothAdapter), listener);
    }

    /* JADX WARN: Multi-variable type inference failed */
    BluetoothDeviceRoutesManager(android.content.Context context, android.os.Handler handler, android.bluetooth.BluetoothAdapter bluetoothAdapter, com.android.server.media.BluetoothProfileMonitor bluetoothProfileMonitor, com.android.server.media.BluetoothRouteController.BluetoothRoutesUpdatedListener bluetoothRoutesUpdatedListener) {
        this.mAdapterStateChangedReceiver = new com.android.server.media.BluetoothDeviceRoutesManager.AdapterStateChangedReceiver();
        this.mDeviceStateChangedReceiver = new com.android.server.media.BluetoothDeviceRoutesManager.DeviceStateChangedReceiver();
        this.mAddressToBondedDevice = new java.util.HashMap();
        this.mBluetoothRoutes = new java.util.HashMap();
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mHandler = handler;
        this.mBluetoothAdapter = (android.bluetooth.BluetoothAdapter) java.util.Objects.requireNonNull(bluetoothAdapter);
        this.mBluetoothProfileMonitor = (com.android.server.media.BluetoothProfileMonitor) java.util.Objects.requireNonNull(bluetoothProfileMonitor);
        this.mListener = (com.android.server.media.BluetoothRouteController.BluetoothRoutesUpdatedListener) java.util.Objects.requireNonNull(bluetoothRoutesUpdatedListener);
    }

    public void start(android.os.UserHandle user) {
        this.mBluetoothProfileMonitor.start();
        android.content.IntentFilter adapterStateChangedIntentFilter = new android.content.IntentFilter();
        adapterStateChangedIntentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mContext.registerReceiverAsUser(this.mAdapterStateChangedReceiver, user, adapterStateChangedIntentFilter, null, null);
        android.content.IntentFilter deviceStateChangedIntentFilter = new android.content.IntentFilter();
        deviceStateChangedIntentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        deviceStateChangedIntentFilter.addAction("android.bluetooth.hearingaid.profile.action.ACTIVE_DEVICE_CHANGED");
        deviceStateChangedIntentFilter.addAction("android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED");
        deviceStateChangedIntentFilter.addAction("android.bluetooth.action.LE_AUDIO_CONNECTION_STATE_CHANGED");
        deviceStateChangedIntentFilter.addAction("android.bluetooth.device.action.ALIAS_CHANGED");
        this.mContext.registerReceiverAsUser(this.mDeviceStateChangedReceiver, user, deviceStateChangedIntentFilter, null, null);
        updateBluetoothRoutes();
    }

    public void stop() {
        this.mContext.unregisterReceiver(this.mAdapterStateChangedReceiver);
        this.mContext.unregisterReceiver(this.mDeviceStateChangedReceiver);
    }

    public synchronized java.lang.String getRouteIdForBluetoothAddress(java.lang.String address) {
        java.lang.String routeIdForType;
        android.bluetooth.BluetoothDevice bluetoothDevice = this.mAddressToBondedDevice.get(address);
        if (bluetoothDevice != null) {
            routeIdForType = getRouteIdForType(bluetoothDevice, getDeviceType(bluetoothDevice));
        } else {
            routeIdForType = null;
        }
        return routeIdForType;
    }

    public synchronized java.lang.String getNameForBluetoothAddress(java.lang.String address) {
        android.bluetooth.BluetoothDevice bluetoothDevice;
        bluetoothDevice = this.mAddressToBondedDevice.get(address);
        return bluetoothDevice != null ? getDeviceName(bluetoothDevice) : null;
    }

    public synchronized void activateBluetoothDeviceWithAddress(java.lang.String address) {
        com.android.server.media.BluetoothDeviceRoutesManager.BluetoothRouteInfo btRouteInfo = this.mBluetoothRoutes.get(address);
        if (btRouteInfo == null) {
            android.util.Slog.w(TAG, "activateBluetoothDeviceWithAddress: Ignoring unknown address " + address);
        } else {
            this.mBluetoothAdapter.setActiveDevice(btRouteInfo.mBtDevice, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBluetoothRoutes() {
        java.util.Set<android.bluetooth.BluetoothDevice> bondedDevices = this.mBluetoothAdapter.getBondedDevices();
        synchronized (this) {
            this.mBluetoothRoutes.clear();
            if (bondedDevices == null) {
                android.util.Log.w(TAG, "BluetoothAdapter.getBondedDevices returned null.");
                return;
            }
            this.mAddressToBondedDevice = (java.util.Map) bondedDevices.stream().collect(java.util.stream.Collectors.toMap(new java.util.function.Function() { // from class: com.android.server.media.BluetoothDeviceRoutesManager$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((android.bluetooth.BluetoothDevice) obj).getAddress();
                }
            }, java.util.function.Function.identity()));
            for (android.bluetooth.BluetoothDevice device : bondedDevices) {
                if (device.isConnected()) {
                    com.android.server.media.BluetoothDeviceRoutesManager.BluetoothRouteInfo newBtRoute = createBluetoothRoute(device);
                    if (newBtRoute.mConnectedProfiles.size() > 0) {
                        this.mBluetoothRoutes.put(device.getAddress(), newBtRoute);
                    }
                }
            }
        }
    }

    public java.util.List<android.media.MediaRoute2Info> getAvailableBluetoothRoutes() {
        java.util.List<android.media.MediaRoute2Info> routes = new java.util.ArrayList<>();
        java.util.Set<java.lang.String> routeIds = new java.util.HashSet<>();
        synchronized (this) {
            for (com.android.server.media.BluetoothDeviceRoutesManager.BluetoothRouteInfo btRoute : this.mBluetoothRoutes.values()) {
                if (routeIds.add(btRoute.mRoute.getId())) {
                    routes.add(btRoute.mRoute);
                }
            }
        }
        return routes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyBluetoothRoutesUpdated() {
        this.mListener.onBluetoothRoutesUpdated();
    }

    private com.android.server.media.BluetoothDeviceRoutesManager.BluetoothRouteInfo createBluetoothRoute(android.bluetooth.BluetoothDevice device) {
        com.android.server.media.BluetoothDeviceRoutesManager.BluetoothRouteInfo newBtRoute = new com.android.server.media.BluetoothDeviceRoutesManager.BluetoothRouteInfo();
        newBtRoute.mBtDevice = device;
        java.lang.String deviceName = getDeviceName(device);
        int type = getDeviceType(device);
        java.lang.String routeId = getRouteIdForType(device, type);
        newBtRoute.mConnectedProfiles = getConnectedProfiles(device);
        newBtRoute.mRoute = new android.media.MediaRoute2Info.Builder(routeId, deviceName).addFeature("android.media.route.feature.LIVE_AUDIO").addFeature("android.media.route.feature.LOCAL_PLAYBACK").setConnectionState(0).setDescription(this.mContext.getResources().getText(android.R.string.bugreport_option_full_title).toString()).setType(type).setAddress(device.getAddress()).build();
        return newBtRoute;
    }

    private java.lang.String getDeviceName(android.bluetooth.BluetoothDevice device) {
        java.lang.String deviceName;
        if (com.android.media.flags.Flags.enableUseOfBluetoothDeviceGetAliasForMr2infoGetName()) {
            deviceName = device.getAlias();
        } else {
            deviceName = device.getName();
        }
        if (android.text.TextUtils.isEmpty(deviceName)) {
            java.lang.String deviceName2 = this.mContext.getResources().getText(android.R.string.unknownName).toString();
            return deviceName2;
        }
        return deviceName;
    }

    private android.util.SparseBooleanArray getConnectedProfiles(android.bluetooth.BluetoothDevice device) {
        android.util.SparseBooleanArray connectedProfiles = new android.util.SparseBooleanArray();
        if (this.mBluetoothProfileMonitor.isProfileSupported(2, device)) {
            connectedProfiles.put(2, true);
        }
        if (this.mBluetoothProfileMonitor.isProfileSupported(21, device)) {
            connectedProfiles.put(21, true);
        }
        if (this.mBluetoothProfileMonitor.isProfileSupported(22, device)) {
            connectedProfiles.put(22, true);
        }
        return connectedProfiles;
    }

    private int getDeviceType(android.bluetooth.BluetoothDevice device) {
        if (this.mBluetoothProfileMonitor.isProfileSupported(22, device)) {
            return 26;
        }
        if (this.mBluetoothProfileMonitor.isProfileSupported(21, device)) {
            return 23;
        }
        return 8;
    }

    private java.lang.String getRouteIdForType(android.bluetooth.BluetoothDevice device, int type) {
        switch (type) {
            case 23:
                return HEARING_AID_ROUTE_ID_PREFIX + this.mBluetoothProfileMonitor.getGroupId(21, device);
            case 26:
                return LE_AUDIO_ROUTE_ID_PREFIX + this.mBluetoothProfileMonitor.getGroupId(22, device);
            default:
                return device.getAddress();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBluetoothAdapterStateChange(int state) {
        boolean shouldCallListener;
        if (state == 10 || state == 13) {
            synchronized (this) {
                this.mBluetoothRoutes.clear();
            }
            notifyBluetoothRoutesUpdated();
        } else if (state == 12) {
            updateBluetoothRoutes();
            synchronized (this) {
                shouldCallListener = !this.mBluetoothRoutes.isEmpty();
            }
            if (shouldCallListener) {
                notifyBluetoothRoutesUpdated();
            }
        }
    }

    private static class BluetoothRouteInfo {
        private android.bluetooth.BluetoothDevice mBtDevice;
        private android.util.SparseBooleanArray mConnectedProfiles;
        private android.media.MediaRoute2Info mRoute;

        private BluetoothRouteInfo() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class AdapterStateChangedReceiver extends android.content.BroadcastReceiver {
        private AdapterStateChangedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            final int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
            if (com.android.media.flags.Flags.enableMr2ServiceNonMainBgThread()) {
                com.android.server.media.BluetoothDeviceRoutesManager.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.BluetoothDeviceRoutesManager$AdapterStateChangedReceiver$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onReceive$0(state);
                    }
                });
            } else {
                com.android.server.media.BluetoothDeviceRoutesManager.this.handleBluetoothAdapterStateChange(state);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(int state) {
            com.android.server.media.BluetoothDeviceRoutesManager.this.handleBluetoothAdapterStateChange(state);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DeviceStateChangedReceiver extends android.content.BroadcastReceiver {
        private DeviceStateChangedReceiver() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:17:0x0034  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r3, android.content.Intent r4) {
            /*
                r2 = this;
                java.lang.String r0 = r4.getAction()
                int r1 = r0.hashCode()
                switch(r1) {
                    case -1765714821: goto L2a;
                    case -612790895: goto L20;
                    case 1174571750: goto L16;
                    case 1244161670: goto Lc;
                    default: goto Lb;
                }
            Lb:
                goto L34
            Lc:
                java.lang.String r1 = "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Lb
                r0 = 0
                goto L35
            L16:
                java.lang.String r1 = "android.bluetooth.device.action.ALIAS_CHANGED"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Lb
                r0 = 3
                goto L35
            L20:
                java.lang.String r1 = "android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Lb
                r0 = 1
                goto L35
            L2a:
                java.lang.String r1 = "android.bluetooth.action.LE_AUDIO_CONNECTION_STATE_CHANGED"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Lb
                r0 = 2
                goto L35
            L34:
                r0 = -1
            L35:
                switch(r0) {
                    case 0: goto L39;
                    case 1: goto L39;
                    case 2: goto L39;
                    case 3: goto L39;
                    default: goto L38;
                }
            L38:
                goto L58
            L39:
                boolean r0 = com.android.media.flags.Flags.enableMr2ServiceNonMainBgThread()
                if (r0 == 0) goto L4e
                com.android.server.media.BluetoothDeviceRoutesManager r0 = com.android.server.media.BluetoothDeviceRoutesManager.this
                android.os.Handler r0 = com.android.server.media.BluetoothDeviceRoutesManager.m5158$$Nest$fgetmHandler(r0)
                com.android.server.media.BluetoothDeviceRoutesManager$DeviceStateChangedReceiver$$ExternalSyntheticLambda0 r1 = new com.android.server.media.BluetoothDeviceRoutesManager$DeviceStateChangedReceiver$$ExternalSyntheticLambda0
                r1.<init>()
                r0.post(r1)
                goto L58
            L4e:
                com.android.server.media.BluetoothDeviceRoutesManager r0 = com.android.server.media.BluetoothDeviceRoutesManager.this
                com.android.server.media.BluetoothDeviceRoutesManager.m5161$$Nest$mupdateBluetoothRoutes(r0)
                com.android.server.media.BluetoothDeviceRoutesManager r0 = com.android.server.media.BluetoothDeviceRoutesManager.this
                com.android.server.media.BluetoothDeviceRoutesManager.m5160$$Nest$mnotifyBluetoothRoutesUpdated(r0)
            L58:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.BluetoothDeviceRoutesManager.DeviceStateChangedReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            com.android.server.media.BluetoothDeviceRoutesManager.this.updateBluetoothRoutes();
            com.android.server.media.BluetoothDeviceRoutesManager.this.notifyBluetoothRoutesUpdated();
        }
    }
}
