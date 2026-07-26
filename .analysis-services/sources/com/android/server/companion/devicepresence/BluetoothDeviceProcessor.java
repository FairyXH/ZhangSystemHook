package com.android.server.companion.devicepresence;

/* JADX INFO: loaded from: classes.dex */
public class BluetoothDeviceProcessor extends android.bluetooth.BluetoothAdapter.BluetoothConnectionCallback implements com.android.server.companion.association.AssociationStore.OnChangeListener {
    private static final java.lang.String TAG = "CDM_BluetoothDeviceProcessor";
    private final java.util.Map<android.net.MacAddress, android.bluetooth.BluetoothDevice> mAllConnectedDevices = new java.util.HashMap();
    private final com.android.server.companion.association.AssociationStore mAssociationStore;
    private final com.android.server.companion.devicepresence.BluetoothDeviceProcessor.Callback mCallback;
    private final com.android.server.companion.devicepresence.ObservableUuidStore mObservableUuidStore;

    interface Callback {
        void onBluetoothCompanionDeviceConnected(int i, int i2);

        void onBluetoothCompanionDeviceDisconnected(int i, int i2);

        void onDevicePresenceEventByUuid(com.android.server.companion.devicepresence.ObservableUuid observableUuid, int i);
    }

    BluetoothDeviceProcessor(com.android.server.companion.association.AssociationStore associationStore, com.android.server.companion.devicepresence.ObservableUuidStore observableUuidStore, com.android.server.companion.devicepresence.BluetoothDeviceProcessor.Callback callback) {
        this.mAssociationStore = associationStore;
        this.mObservableUuidStore = observableUuidStore;
        this.mCallback = callback;
    }

    void init(android.bluetooth.BluetoothAdapter btAdapter) {
        btAdapter.registerBluetoothConnectionCallback(new android.os.HandlerExecutor(android.os.Handler.getMain()), this);
        this.mAssociationStore.registerLocalListener(this);
    }

    public void onDeviceConnected(android.bluetooth.BluetoothDevice device) {
        android.net.MacAddress macAddress = android.net.MacAddress.fromString(device.getAddress());
        if (this.mAllConnectedDevices.put(macAddress, device) != null) {
            return;
        }
        onDeviceConnectivityChanged(device, true);
    }

    public void onDeviceDisconnected(android.bluetooth.BluetoothDevice device, int reason) {
        android.net.MacAddress macAddress = android.net.MacAddress.fromString(device.getAddress());
        if (this.mAllConnectedDevices.remove(macAddress) == null) {
            return;
        }
        onDeviceConnectivityChanged(device, false);
    }

    private void onDeviceConnectivityChanged(android.bluetooth.BluetoothDevice device, boolean connected) {
        int userId = android.os.UserHandle.myUserId();
        java.util.List<android.companion.AssociationInfo> associations = this.mAssociationStore.getActiveAssociationsByAddress(device.getAddress());
        for (android.companion.AssociationInfo association : associations) {
            if (association.isNotifyOnDeviceNearby()) {
                int id = association.getId();
                if (connected) {
                    this.mCallback.onBluetoothCompanionDeviceConnected(id, association.getUserId());
                } else {
                    this.mCallback.onBluetoothCompanionDeviceDisconnected(id, association.getUserId());
                }
            }
        }
        java.util.List<com.android.server.companion.devicepresence.ObservableUuid> observableUuids = this.mObservableUuidStore.getObservableUuidsForUser(userId);
        android.os.ParcelUuid[] bluetoothDeviceUuids = device.getUuids();
        java.util.List<android.os.ParcelUuid> deviceUuids = com.android.internal.util.ArrayUtils.isEmpty(bluetoothDeviceUuids) ? java.util.Collections.emptyList() : java.util.Arrays.asList(bluetoothDeviceUuids);
        for (com.android.server.companion.devicepresence.ObservableUuid uuid : observableUuids) {
            if (deviceUuids.contains(uuid.getUuid())) {
                this.mCallback.onDevicePresenceEventByUuid(uuid, connected ? 2 : 3);
            }
        }
    }

    @Override // com.android.server.companion.association.AssociationStore.OnChangeListener
    public void onAssociationAdded(android.companion.AssociationInfo association) {
        if (this.mAllConnectedDevices.containsKey(association.getDeviceMacAddress())) {
            this.mCallback.onBluetoothCompanionDeviceConnected(association.getId(), association.getUserId());
        }
    }
}
