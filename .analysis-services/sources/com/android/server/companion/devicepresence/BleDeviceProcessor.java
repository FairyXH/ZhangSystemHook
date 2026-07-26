package com.android.server.companion.devicepresence;

/* JADX INFO: loaded from: classes.dex */
class BleDeviceProcessor implements com.android.server.companion.association.AssociationStore.OnChangeListener {
    private static final android.bluetooth.le.ScanSettings SCAN_SETTINGS = new android.bluetooth.le.ScanSettings.Builder().setCallbackType(6).setScanMode(0).build();
    private static final java.lang.String TAG = "CDM_BleDeviceProcessor";
    private final com.android.server.companion.association.AssociationStore mAssociationStore;
    private android.bluetooth.le.BluetoothLeScanner mBleScanner;
    private android.bluetooth.BluetoothAdapter mBtAdapter;
    private final com.android.server.companion.devicepresence.BleDeviceProcessor.Callback mCallback;
    private boolean mScanning = false;
    private final android.bluetooth.le.ScanCallback mScanCallback = new android.bluetooth.le.ScanCallback() { // from class: com.android.server.companion.devicepresence.BleDeviceProcessor.2
        @Override // android.bluetooth.le.ScanCallback
        public void onScanResult(int callbackType, android.bluetooth.le.ScanResult result) {
            android.bluetooth.BluetoothDevice device = result.getDevice();
            switch (callbackType) {
                case 2:
                    com.android.server.companion.devicepresence.BleDeviceProcessor.this.notifyDeviceFound(device);
                    break;
                case 3:
                default:
                    android.util.Slog.wtf(com.android.server.companion.devicepresence.BleDeviceProcessor.TAG, "Unexpected callback " + com.android.server.companion.devicepresence.BleDeviceProcessor.nameForBleScanCallbackType(callbackType));
                    break;
                case 4:
                    com.android.server.companion.devicepresence.BleDeviceProcessor.this.notifyDeviceLost(device);
                    break;
            }
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanFailed(int errorCode) {
            com.android.server.companion.devicepresence.BleDeviceProcessor.this.mScanning = false;
        }
    };

    interface Callback {
        void onBleCompanionDeviceFound(int i, int i2);

        void onBleCompanionDeviceLost(int i, int i2);
    }

    BleDeviceProcessor(com.android.server.companion.association.AssociationStore associationStore, com.android.server.companion.devicepresence.BleDeviceProcessor.Callback callback) {
        this.mAssociationStore = associationStore;
        this.mCallback = callback;
    }

    void init(android.content.Context context, android.bluetooth.BluetoothAdapter btAdapter) {
        if (this.mBtAdapter != null) {
            throw new java.lang.IllegalStateException(getClass().getSimpleName() + " is already initialized");
        }
        this.mBtAdapter = (android.bluetooth.BluetoothAdapter) java.util.Objects.requireNonNull(btAdapter);
        checkBleState();
        registerBluetoothStateBroadcastReceiver(context);
        this.mAssociationStore.registerLocalListener(this);
    }

    final void restartScan() {
        enforceInitialized();
        if (this.mBleScanner == null) {
            return;
        }
        stopScanIfNeeded();
        startScan();
    }

    @Override // com.android.server.companion.association.AssociationStore.OnChangeListener
    public void onAssociationChanged(int changeType, android.companion.AssociationInfo association) {
        if (android.os.Looper.getMainLooper().isCurrentThread()) {
            restartScan();
        } else {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new java.lang.Runnable() { // from class: com.android.server.companion.devicepresence.BleDeviceProcessor$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.restartScan();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkBleState() {
        enforceInitialized();
        boolean bleAvailable = this.mBtAdapter.isLeEnabled();
        if (!bleAvailable || this.mBleScanner == null) {
            if (!bleAvailable && this.mBleScanner == null) {
                return;
            }
            if (bleAvailable) {
                this.mBleScanner = this.mBtAdapter.getBluetoothLeScanner();
                if (this.mBleScanner == null) {
                    return;
                }
                startScan();
                return;
            }
            stopScanIfNeeded();
            this.mBleScanner = null;
        }
    }

    void startScan() {
        java.lang.String macAddress;
        enforceInitialized();
        android.util.Slog.i(TAG, "startBleScan()");
        if (this.mScanning) {
            android.util.Slog.w(TAG, "Scan is already in progress.");
            return;
        }
        if (this.mBleScanner == null) {
            android.util.Slog.w(TAG, "BLE is not available.");
            return;
        }
        java.util.Set<java.lang.String> macAddresses = new java.util.HashSet<>();
        for (android.companion.AssociationInfo association : this.mAssociationStore.getActiveAssociations()) {
            if (association.isNotifyOnDeviceNearby() && (macAddress = association.getDeviceMacAddressAsString()) != null) {
                macAddresses.add(macAddress);
            }
        }
        if (macAddresses.isEmpty()) {
            return;
        }
        java.util.List<android.bluetooth.le.ScanFilter> filters = new java.util.ArrayList<>(macAddresses.size());
        for (java.lang.String macAddress2 : macAddresses) {
            android.bluetooth.le.ScanFilter filter = new android.bluetooth.le.ScanFilter.Builder().setDeviceAddress(macAddress2).build();
            filters.add(filter);
        }
        if (this.mBtAdapter.isLeEnabled()) {
            try {
                this.mBleScanner.startScan(filters, SCAN_SETTINGS, this.mScanCallback);
                this.mScanning = true;
                return;
            } catch (java.lang.IllegalStateException e) {
                android.util.Slog.w(TAG, "Exception while starting BLE scanning", e);
                return;
            }
        }
        android.util.Slog.w(TAG, "BLE scanning is not turned on");
    }

    void stopScanIfNeeded() {
        enforceInitialized();
        android.util.Slog.i(TAG, "stopBleScan()");
        if (!this.mScanning) {
            return;
        }
        if (this.mBtAdapter.isLeEnabled()) {
            try {
                this.mBleScanner.stopScan(this.mScanCallback);
            } catch (java.lang.IllegalStateException e) {
                android.util.Slog.w(TAG, "Exception while stopping BLE scanning", e);
            }
        } else {
            android.util.Slog.w(TAG, "BLE scanning is not turned on");
        }
        this.mScanning = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeviceFound(android.bluetooth.BluetoothDevice device) {
        for (android.companion.AssociationInfo association : this.mAssociationStore.getActiveAssociationsByAddress(device.getAddress())) {
            this.mCallback.onBleCompanionDeviceFound(association.getId(), association.getUserId());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeviceLost(android.bluetooth.BluetoothDevice device) {
        for (android.companion.AssociationInfo association : this.mAssociationStore.getActiveAssociationsByAddress(device.getAddress())) {
            this.mCallback.onBleCompanionDeviceLost(association.getId(), association.getUserId());
        }
    }

    /* JADX INFO: renamed from: com.android.server.companion.devicepresence.BleDeviceProcessor$1, reason: invalid class name */
    class AnonymousClass1 extends android.content.BroadcastReceiver {
        AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            com.android.server.companion.devicepresence.BleDeviceProcessor.this.checkBleState();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new java.lang.Runnable() { // from class: com.android.server.companion.devicepresence.BleDeviceProcessor$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0();
                }
            });
        }
    }

    private void registerBluetoothStateBroadcastReceiver(android.content.Context context) {
        android.content.BroadcastReceiver receiver = new com.android.server.companion.devicepresence.BleDeviceProcessor.AnonymousClass1();
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.BLE_STATE_CHANGED");
        context.registerReceiver(receiver, filter);
    }

    private void enforceInitialized() {
        if (this.mBtAdapter == null) {
            throw new java.lang.IllegalStateException(getClass().getSimpleName() + " is not initialized");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String nameForBleScanCallbackType(int callbackType) {
        java.lang.String name;
        switch (callbackType) {
            case 1:
                name = "ALL_MATCHES";
                break;
            case 2:
                name = "FIRST_MATCH";
                break;
            case 3:
            default:
                name = "Unknown";
                break;
            case 4:
                name = "MATCH_LOST";
                break;
        }
        return name + "(" + callbackType + ")";
    }
}
