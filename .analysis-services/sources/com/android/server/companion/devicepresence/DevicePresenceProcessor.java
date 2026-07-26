package com.android.server.companion.devicepresence;

/* JADX INFO: loaded from: classes.dex */
public class DevicePresenceProcessor implements com.android.server.companion.association.AssociationStore.OnChangeListener, com.android.server.companion.devicepresence.BluetoothDeviceProcessor.Callback, com.android.server.companion.devicepresence.BleDeviceProcessor.Callback {
    private static final java.lang.String TAG = "CDM_DevicePresenceProcessor";
    private final com.android.server.companion.association.AssociationStore mAssociationStore;
    private final com.android.server.companion.devicepresence.BleDeviceProcessor mBleDeviceProcessor;
    private final com.android.server.companion.devicepresence.BluetoothDeviceProcessor mBluetoothDeviceProcessor;
    private final com.android.server.companion.devicepresence.CompanionAppBinder mCompanionAppBinder;
    private final android.content.Context mContext;
    private final com.android.server.companion.devicepresence.ObservableUuidStore mObservableUuidStore;
    private final android.os.PowerManagerInternal mPowerManagerInternal;
    private final android.os.UserManager mUserManager;
    private final java.util.Set<java.lang.Integer> mConnectedBtDevices = new java.util.HashSet();
    private final java.util.Set<java.lang.Integer> mNearbyBleDevices = new java.util.HashSet();
    private final java.util.Set<java.lang.Integer> mReportedSelfManagedDevices = new java.util.HashSet();
    private final java.util.Set<android.os.ParcelUuid> mConnectedUuidDevices = new java.util.HashSet();
    private final java.util.Set<java.lang.Integer> mBtDisconnectedDevices = new java.util.HashSet();
    private final android.util.SparseBooleanArray mBtDisconnectedDevicesBlePresence = new android.util.SparseBooleanArray();
    private final java.util.Set<java.lang.Integer> mSimulated = new java.util.HashSet();
    private final com.android.server.companion.devicepresence.DevicePresenceProcessor.SimulatedDevicePresenceSchedulerHelper mSchedulerHelper = new com.android.server.companion.devicepresence.DevicePresenceProcessor.SimulatedDevicePresenceSchedulerHelper();
    private final com.android.server.companion.devicepresence.DevicePresenceProcessor.BleDeviceDisappearedScheduler mBleDeviceDisappearedScheduler = new com.android.server.companion.devicepresence.DevicePresenceProcessor.BleDeviceDisappearedScheduler();
    public final android.util.SparseArray<java.util.List<android.companion.DevicePresenceEvent>> mPendingDevicePresenceEvents = new android.util.SparseArray<>();

    public DevicePresenceProcessor(android.content.Context context, com.android.server.companion.devicepresence.CompanionAppBinder companionAppBinder, android.os.UserManager userManager, com.android.server.companion.association.AssociationStore associationStore, com.android.server.companion.devicepresence.ObservableUuidStore observableUuidStore, android.os.PowerManagerInternal powerManagerInternal) {
        this.mContext = context;
        this.mCompanionAppBinder = companionAppBinder;
        this.mAssociationStore = associationStore;
        this.mObservableUuidStore = observableUuidStore;
        this.mUserManager = userManager;
        this.mBluetoothDeviceProcessor = new com.android.server.companion.devicepresence.BluetoothDeviceProcessor(associationStore, this.mObservableUuidStore, this);
        this.mBleDeviceProcessor = new com.android.server.companion.devicepresence.BleDeviceProcessor(associationStore, this);
        this.mPowerManagerInternal = powerManagerInternal;
    }

    public void init(android.content.Context context) {
        android.bluetooth.BluetoothManager bm = (android.bluetooth.BluetoothManager) context.getSystemService("bluetooth");
        if (bm == null) {
            android.util.Slog.w(TAG, "BluetoothManager is not available.");
            return;
        }
        android.bluetooth.BluetoothAdapter btAdapter = bm.getAdapter();
        if (btAdapter == null) {
            android.util.Slog.w(TAG, "BluetoothAdapter is NOT available.");
            return;
        }
        this.mBluetoothDeviceProcessor.init(btAdapter);
        this.mBleDeviceProcessor.init(context, btAdapter);
        this.mAssociationStore.registerLocalListener(this);
    }

    public void startObservingDevicePresence(android.companion.ObservingDevicePresenceRequest request, java.lang.String packageName, int userId, boolean enforcePermissions) {
        android.util.Slog.i(TAG, "Start observing request=[" + request + "] for userId=[" + userId + "], package=[" + packageName + "]...");
        android.os.ParcelUuid requestUuid = request.getUuid();
        if (requestUuid != null) {
            if (enforcePermissions) {
                com.android.server.companion.utils.PermissionsUtils.enforceCallerCanObserveDevicePresenceByUuid(this.mContext, packageName, userId);
            }
            if (this.mObservableUuidStore.isUuidBeingObserved(requestUuid, userId, packageName)) {
                android.util.Slog.i(TAG, "UUID=[" + requestUuid + "], package=[" + packageName + "], userId=[" + userId + "] is already being observed.");
                return;
            } else {
                com.android.server.companion.devicepresence.ObservableUuid observableUuid = new com.android.server.companion.devicepresence.ObservableUuid(userId, requestUuid, packageName, java.lang.Long.valueOf(java.lang.System.currentTimeMillis()));
                this.mObservableUuidStore.writeObservableUuid(userId, observableUuid);
            }
        } else {
            int associationId = request.getAssociationId();
            android.companion.AssociationInfo association = this.mAssociationStore.getAssociationWithCallerChecks(associationId);
            if (association.isNotifyOnDeviceNearby()) {
                android.util.Slog.i(TAG, "Associated device id=[" + association.getId() + "] is already being observed. No-op.");
                return;
            }
            this.mAssociationStore.updateAssociation(new android.companion.AssociationInfo.Builder(association).setNotifyOnDeviceNearby(true).build());
            if (isDevicePresent(associationId)) {
                android.util.Slog.i(TAG, "Device is already present. Triggering callback.");
                if (isBlePresent(associationId)) {
                    onDevicePresenceEvent(this.mNearbyBleDevices, associationId, 0);
                } else if (isBtConnected(associationId)) {
                    onDevicePresenceEvent(this.mConnectedBtDevices, associationId, 2);
                } else if (isSimulatePresent(associationId)) {
                    onDevicePresenceEvent(this.mSimulated, associationId, 0);
                }
            }
        }
        android.util.Slog.i(TAG, "Registered device presence listener.");
    }

    public void stopObservingDevicePresence(android.companion.ObservingDevicePresenceRequest request, java.lang.String packageName, int userId, boolean enforcePermissions) {
        android.util.Slog.i(TAG, "Stop observing request=[" + request + "] for userId=[" + userId + "], package=[" + packageName + "]...");
        android.os.ParcelUuid requestUuid = request.getUuid();
        if (requestUuid != null) {
            if (enforcePermissions) {
                com.android.server.companion.utils.PermissionsUtils.enforceCallerCanObserveDevicePresenceByUuid(this.mContext, packageName, userId);
            }
            if (!this.mObservableUuidStore.isUuidBeingObserved(requestUuid, userId, packageName)) {
                android.util.Slog.i(TAG, "UUID=[" + requestUuid + "], package=[" + packageName + "], userId=[" + userId + "] is already not being observed.");
                return;
            } else {
                this.mObservableUuidStore.removeObservableUuid(userId, requestUuid, packageName);
                removeCurrentConnectedUuidDevice(requestUuid);
            }
        } else {
            int associationId = request.getAssociationId();
            android.companion.AssociationInfo association = this.mAssociationStore.getAssociationWithCallerChecks(associationId);
            if (!association.isNotifyOnDeviceNearby()) {
                android.util.Slog.i(TAG, "Associated device id=[" + association.getId() + "] is already not being observed. No-op.");
                return;
            } else {
                this.mAssociationStore.updateAssociation(new android.companion.AssociationInfo.Builder(association).setNotifyOnDeviceNearby(false).build());
            }
        }
        android.util.Slog.i(TAG, "Unregistered device presence listener.");
        if (!shouldBindPackage(userId, packageName)) {
            this.mCompanionAppBinder.unbindCompanionApp(userId, packageName);
        }
    }

    @java.lang.Deprecated
    public void startObservingDevicePresence(int userId, java.lang.String packageName, java.lang.String deviceAddress) throws android.os.RemoteException {
        android.util.Slog.i(TAG, "Start observing device=[" + deviceAddress + "] for userId=[" + userId + "], package=[" + packageName + "]...");
        com.android.server.companion.utils.PermissionsUtils.enforceCallerCanManageAssociationsForPackage(this.mContext, userId, packageName, null);
        android.companion.AssociationInfo association = this.mAssociationStore.getFirstAssociationByAddress(userId, packageName, deviceAddress);
        if (association == null) {
            throw new android.os.RemoteException(new android.companion.DeviceNotAssociatedException("App " + packageName + " is not associated with device " + deviceAddress + " for user " + userId));
        }
        startObservingDevicePresence(new android.companion.ObservingDevicePresenceRequest.Builder().setAssociationId(association.getId()).build(), packageName, userId, true);
    }

    @java.lang.Deprecated
    public void stopObservingDevicePresence(int userId, java.lang.String packageName, java.lang.String deviceAddress) throws android.os.RemoteException {
        android.util.Slog.i(TAG, "Stop observing device=[" + deviceAddress + "] for userId=[" + userId + "], package=[" + packageName + "]...");
        com.android.server.companion.utils.PermissionsUtils.enforceCallerCanManageAssociationsForPackage(this.mContext, userId, packageName, null);
        android.companion.AssociationInfo association = this.mAssociationStore.getFirstAssociationByAddress(userId, packageName, deviceAddress);
        if (association == null) {
            throw new android.os.RemoteException(new android.companion.DeviceNotAssociatedException("App " + packageName + " is not associated with device " + deviceAddress + " for user " + userId));
        }
        stopObservingDevicePresence(new android.companion.ObservingDevicePresenceRequest.Builder().setAssociationId(association.getId()).build(), packageName, userId, true);
    }

    private boolean shouldBindPackage(int userId, java.lang.String packageName) {
        java.util.List<android.companion.AssociationInfo> packageAssociations = this.mAssociationStore.getActiveAssociationsByPackage(userId, packageName);
        java.util.List<com.android.server.companion.devicepresence.ObservableUuid> observableUuids = this.mObservableUuidStore.getObservableUuidsForPackage(userId, packageName);
        for (android.companion.AssociationInfo association : packageAssociations) {
            if (association.shouldBindWhenPresent() && isDevicePresent(association.getId())) {
                return true;
            }
        }
        for (com.android.server.companion.devicepresence.ObservableUuid uuid : observableUuids) {
            if (isDeviceUuidPresent(uuid.getUuid())) {
                return true;
            }
        }
        return false;
    }

    private void bindApplicationIfNeeded(int userId, java.lang.String packageName, boolean bindImportant) {
        if (!this.mCompanionAppBinder.isCompanionApplicationBound(userId, packageName)) {
            this.mCompanionAppBinder.bindCompanionApp(userId, packageName, bindImportant, new com.android.server.companion.devicepresence.CompanionServiceConnector.Listener() { // from class: com.android.server.companion.devicepresence.DevicePresenceProcessor$$ExternalSyntheticLambda0
                @Override // com.android.server.companion.devicepresence.CompanionServiceConnector.Listener
                public final void onBindingDied(int i, java.lang.String str, com.android.server.companion.devicepresence.CompanionServiceConnector companionServiceConnector) {
                    this.f$0.onBinderDied(i, str, companionServiceConnector);
                }
            });
        } else {
            android.util.Slog.i(TAG, "UserId=[" + userId + "], packageName=[" + packageName + "] is already bound.");
        }
    }

    public java.util.Set<android.os.ParcelUuid> getCurrentConnectedUuidDevices() {
        return this.mConnectedUuidDevices;
    }

    public void removeCurrentConnectedUuidDevice(android.os.ParcelUuid uuid) {
        this.mConnectedUuidDevices.remove(uuid);
    }

    public boolean isDevicePresent(int associationId) {
        return this.mReportedSelfManagedDevices.contains(java.lang.Integer.valueOf(associationId)) || this.mConnectedBtDevices.contains(java.lang.Integer.valueOf(associationId)) || this.mNearbyBleDevices.contains(java.lang.Integer.valueOf(associationId)) || this.mSimulated.contains(java.lang.Integer.valueOf(associationId));
    }

    public boolean isDeviceUuidPresent(android.os.ParcelUuid uuid) {
        return this.mConnectedUuidDevices.contains(uuid);
    }

    public boolean isBtConnected(int associationId) {
        return this.mConnectedBtDevices.contains(java.lang.Integer.valueOf(associationId));
    }

    public boolean isBlePresent(int associationId) {
        return this.mNearbyBleDevices.contains(java.lang.Integer.valueOf(associationId));
    }

    public boolean isSimulatePresent(int associationId) {
        return this.mSimulated.contains(java.lang.Integer.valueOf(associationId));
    }

    public void onSelfManagedDeviceConnected(int associationId) {
        onDevicePresenceEvent(this.mReportedSelfManagedDevices, associationId, 4);
    }

    public void onSelfManagedDeviceDisconnected(int associationId) {
        onDevicePresenceEvent(this.mReportedSelfManagedDevices, associationId, 5);
    }

    public void onSelfManagedDeviceReporterBinderDied(int associationId) {
        onDevicePresenceEvent(this.mReportedSelfManagedDevices, associationId, 5);
    }

    @Override // com.android.server.companion.devicepresence.BluetoothDeviceProcessor.Callback
    public void onBluetoothCompanionDeviceConnected(int associationId, int userId) {
        android.util.Slog.i(TAG, "onBluetoothCompanionDeviceConnected: associationId( " + associationId + " )");
        if (!this.mUserManager.isUserUnlockingOrUnlocked(userId)) {
            onDeviceLocked(associationId, userId, 2, null);
            return;
        }
        synchronized (this.mBtDisconnectedDevices) {
            boolean isReconnected = this.mBtDisconnectedDevices.contains(java.lang.Integer.valueOf(associationId));
            if (isReconnected) {
                android.util.Slog.i(TAG, "Device ( " + associationId + " ) is reconnected within 10s.");
                this.mBleDeviceDisappearedScheduler.unScheduleDeviceDisappeared(associationId);
            }
            android.util.Slog.i(TAG, "onBluetoothCompanionDeviceConnected: associationId( " + associationId + " )");
            onDevicePresenceEvent(this.mConnectedBtDevices, associationId, 2);
            if (canStopBleScan()) {
                this.mBleDeviceProcessor.stopScanIfNeeded();
            }
        }
    }

    @Override // com.android.server.companion.devicepresence.BluetoothDeviceProcessor.Callback
    public void onBluetoothCompanionDeviceDisconnected(int associationId, int userId) {
        android.util.Slog.i(TAG, "onBluetoothCompanionDeviceDisconnected associationId( " + associationId + " )");
        if (!this.mUserManager.isUserUnlockingOrUnlocked(userId)) {
            onDeviceLocked(associationId, userId, 3, null);
            return;
        }
        this.mBleDeviceProcessor.startScan();
        onDevicePresenceEvent(this.mConnectedBtDevices, associationId, 3);
        if (isBlePresent(associationId)) {
            synchronized (this.mBtDisconnectedDevices) {
                this.mBtDisconnectedDevices.add(java.lang.Integer.valueOf(associationId));
            }
            this.mBleDeviceDisappearedScheduler.scheduleBleDeviceDisappeared(associationId);
        }
    }

    @Override // com.android.server.companion.devicepresence.BleDeviceProcessor.Callback
    public void onBleCompanionDeviceFound(int associationId, int userId) {
        android.util.Slog.i(TAG, "onBleCompanionDeviceFound associationId( " + associationId + " )");
        if (!this.mUserManager.isUserUnlockingOrUnlocked(userId)) {
            onDeviceLocked(associationId, userId, 0, null);
            return;
        }
        onDevicePresenceEvent(this.mNearbyBleDevices, associationId, 0);
        synchronized (this.mBtDisconnectedDevices) {
            boolean isCurrentPresent = this.mBtDisconnectedDevicesBlePresence.get(associationId);
            if (this.mBtDisconnectedDevices.contains(java.lang.Integer.valueOf(associationId)) && isCurrentPresent) {
                this.mBleDeviceDisappearedScheduler.unScheduleDeviceDisappeared(associationId);
            }
        }
    }

    @Override // com.android.server.companion.devicepresence.BleDeviceProcessor.Callback
    public void onBleCompanionDeviceLost(int associationId, int userId) {
        android.util.Slog.i(TAG, "onBleCompanionDeviceLost associationId( " + associationId + " )");
        if (!this.mUserManager.isUserUnlockingOrUnlocked(userId)) {
            onDeviceLocked(associationId, userId, 0, null);
        } else {
            onDevicePresenceEvent(this.mNearbyBleDevices, associationId, 1);
        }
    }

    public void simulateDeviceEvent(int associationId, int event) {
        enforceCallerShellOrRoot();
        enforceAssociationExists(associationId);
        android.companion.AssociationInfo associationInfo = this.mAssociationStore.getAssociationById(associationId);
        switch (event) {
            case 0:
                simulateDeviceAppeared(associationId, event);
                return;
            case 1:
                simulateDeviceDisappeared(associationId, event);
                return;
            case 2:
                onBluetoothCompanionDeviceConnected(associationId, associationInfo.getUserId());
                return;
            case 3:
                onBluetoothCompanionDeviceDisconnected(associationId, associationInfo.getUserId());
                return;
            default:
                throw new java.lang.IllegalArgumentException("Event: " + event + "is not supported");
        }
    }

    public void simulateDeviceEventByUuid(com.android.server.companion.devicepresence.ObservableUuid uuid, int event) {
        enforceCallerShellOrRoot();
        onDevicePresenceEventByUuid(uuid, event);
    }

    public void simulateDeviceEventOnDeviceLocked(int associationId, int userId, int event, android.os.ParcelUuid uuid) {
        enforceCallerShellOrRoot();
        onDeviceLocked(associationId, userId, event, uuid);
    }

    public void simulateDeviceEventOnUserUnlocked(int userId) {
        enforceCallerShellOrRoot();
        sendDevicePresenceEventOnUnlocked(userId);
    }

    private void simulateDeviceAppeared(int associationId, int state) {
        onDevicePresenceEvent(this.mSimulated, associationId, state);
        this.mSchedulerHelper.scheduleOnDeviceGoneCallForSimulatedDevicePresence(associationId);
    }

    private void simulateDeviceDisappeared(int associationId, int state) {
        this.mSchedulerHelper.unscheduleOnDeviceGoneCallForSimulatedDevicePresence(associationId);
        onDevicePresenceEvent(this.mSimulated, associationId, state);
    }

    private void enforceAssociationExists(int associationId) {
        if (this.mAssociationStore.getAssociationById(associationId) == null) {
            throw new java.lang.IllegalArgumentException("Association with id " + associationId + " does not exist.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDevicePresenceEvent(java.util.Set<java.lang.Integer> presentDevicesForSource, int associationId, int eventType) {
        android.util.Slog.i(TAG, "onDevicePresenceEvent() id=[" + associationId + "], event=[" + eventType + "]...");
        android.companion.AssociationInfo association = this.mAssociationStore.getAssociationById(associationId);
        if (association == null) {
            android.util.Slog.e(TAG, "Association doesn't exist.");
            return;
        }
        int userId = association.getUserId();
        java.lang.String packageName = association.getPackageName();
        android.companion.DevicePresenceEvent event = new android.companion.DevicePresenceEvent(associationId, eventType, (android.os.ParcelUuid) null);
        if (eventType == 0) {
            synchronized (this.mBtDisconnectedDevices) {
                if (this.mBtDisconnectedDevices.contains(java.lang.Integer.valueOf(associationId))) {
                    android.util.Slog.i(TAG, "Device ( " + associationId + " ) is present, do not need to send the callback with event ( 0 ).");
                    this.mBtDisconnectedDevicesBlePresence.append(associationId, true);
                }
            }
        }
        switch (eventType) {
            case 0:
            case 2:
            case 4:
                boolean added = presentDevicesForSource.add(java.lang.Integer.valueOf(associationId));
                if (!added) {
                    android.util.Slog.w(TAG, "The association is already present.");
                }
                if (association.shouldBindWhenPresent()) {
                    bindApplicationIfNeeded(userId, packageName, association.isSelfManaged());
                    if (association.isSelfManaged() || added) {
                        notifyDevicePresenceEvent(userId, packageName, event);
                        legacyNotifyDevicePresenceEvent(association, true);
                        return;
                    }
                    return;
                }
                return;
            case 1:
            case 3:
            case 5:
                boolean removed = presentDevicesForSource.remove(java.lang.Integer.valueOf(associationId));
                if (!removed) {
                    android.util.Slog.w(TAG, "The association is already NOT present.");
                }
                if (!this.mCompanionAppBinder.isCompanionApplicationBound(userId, packageName)) {
                    android.util.Slog.e(TAG, "Package is not bound");
                    return;
                }
                if (association.isSelfManaged() || removed) {
                    notifyDevicePresenceEvent(userId, packageName, event);
                    legacyNotifyDevicePresenceEvent(association, false);
                }
                if (!shouldBindPackage(userId, packageName)) {
                    this.mCompanionAppBinder.unbindCompanionApp(userId, packageName);
                    return;
                }
                return;
            default:
                android.util.Slog.e(TAG, "Event: " + eventType + " is not supported.");
                return;
        }
    }

    @Override // com.android.server.companion.devicepresence.BluetoothDeviceProcessor.Callback
    public void onDevicePresenceEventByUuid(com.android.server.companion.devicepresence.ObservableUuid uuid, int eventType) {
        android.util.Slog.i(TAG, "onDevicePresenceEventByUuid ObservableUuid=[" + uuid + "], event=[" + eventType + "]...");
        android.os.ParcelUuid parcelUuid = uuid.getUuid();
        int userId = uuid.getUserId();
        if (!this.mUserManager.isUserUnlockingOrUnlocked(userId)) {
            onDeviceLocked(-1, userId, eventType, parcelUuid);
        }
        java.lang.String packageName = uuid.getPackageName();
        android.companion.DevicePresenceEvent event = new android.companion.DevicePresenceEvent(-1, eventType, parcelUuid);
        switch (eventType) {
            case 2:
                boolean added = this.mConnectedUuidDevices.add(parcelUuid);
                if (!added) {
                    android.util.Slog.w(TAG, "This device is already connected.");
                }
                bindApplicationIfNeeded(userId, packageName, false);
                notifyDevicePresenceEvent(userId, packageName, event);
                break;
            case 3:
                boolean removed = this.mConnectedUuidDevices.remove(parcelUuid);
                if (!removed) {
                    android.util.Slog.w(TAG, "This device is already disconnected.");
                } else if (!this.mCompanionAppBinder.isCompanionApplicationBound(userId, packageName)) {
                    android.util.Slog.e(TAG, "Package is not bound.");
                } else {
                    notifyDevicePresenceEvent(userId, packageName, event);
                    if (!shouldBindPackage(userId, packageName)) {
                        this.mCompanionAppBinder.unbindCompanionApp(userId, packageName);
                    }
                }
                break;
            default:
                android.util.Slog.e(TAG, "Event: " + eventType + " is not supported");
                break;
        }
    }

    @java.lang.Deprecated
    private void legacyNotifyDevicePresenceEvent(android.companion.AssociationInfo association, boolean isAppeared) {
        android.util.Slog.i(TAG, "legacyNotifyDevicePresenceEvent() association=[" + association.toShortString() + "], isAppeared=[" + isAppeared + "]");
        int userId = association.getUserId();
        java.lang.String packageName = association.getPackageName();
        com.android.server.companion.devicepresence.CompanionServiceConnector primaryServiceConnector = this.mCompanionAppBinder.getPrimaryServiceConnector(userId, packageName);
        if (primaryServiceConnector == null) {
            android.util.Slog.e(TAG, "Package is not bound.");
        } else if (isAppeared) {
            primaryServiceConnector.postOnDeviceAppeared(association);
        } else {
            primaryServiceConnector.postOnDeviceDisappeared(association);
        }
    }

    private void notifyDevicePresenceEvent(int userId, java.lang.String packageName, android.companion.DevicePresenceEvent event) {
        android.util.Slog.i(TAG, "notifyCompanionDevicePresenceEvent userId=[" + userId + "], packageName=[" + packageName + "], event=[" + event + "]...");
        com.android.server.companion.devicepresence.CompanionServiceConnector primaryServiceConnector = this.mCompanionAppBinder.getPrimaryServiceConnector(userId, packageName);
        if (primaryServiceConnector == null) {
            android.util.Slog.e(TAG, "Package is NOT bound.");
        } else {
            primaryServiceConnector.postOnDevicePresenceEvent(event);
        }
    }

    public void notifySelfManagedDevicePresenceEvent(int associationId, boolean isAppeared) {
        android.util.Slog.i(TAG, "notifySelfManagedDeviceAppeared() id=" + associationId);
        android.companion.AssociationInfo association = this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        if (!association.isSelfManaged()) {
            throw new java.lang.IllegalArgumentException("Association id=[" + associationId + "] is not self-managed.");
        }
        android.companion.AssociationInfo association2 = new android.companion.AssociationInfo.Builder(association).setLastTimeConnected(java.lang.System.currentTimeMillis()).build();
        this.mAssociationStore.updateAssociation(association2);
        if (isAppeared) {
            onSelfManagedDeviceConnected(associationId);
        } else {
            onSelfManagedDeviceDisconnected(associationId);
        }
        java.lang.String deviceProfile = association2.getDeviceProfile();
        if ("android.app.role.SYSTEM_AUTOMOTIVE_PROJECTION".equals(deviceProfile)) {
            android.util.Slog.i(TAG, "Enable hint mode for device device profile: " + deviceProfile);
            this.mPowerManagerInternal.setPowerMode(18, isAppeared);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBinderDied(int userId, java.lang.String packageName, com.android.server.companion.devicepresence.CompanionServiceConnector serviceConnector) {
        boolean isPrimary = serviceConnector.isPrimary();
        android.util.Slog.i(TAG, "onBinderDied() u" + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName + " isPrimary: " + isPrimary);
        if (isPrimary) {
            java.util.List<android.companion.AssociationInfo> associations = this.mAssociationStore.getActiveAssociationsByPackage(userId, packageName);
            java.util.Iterator<android.companion.AssociationInfo> it = associations.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                android.companion.AssociationInfo association = it.next();
                java.lang.String deviceProfile = association.getDeviceProfile();
                if ("android.app.role.SYSTEM_AUTOMOTIVE_PROJECTION".equals(deviceProfile)) {
                    android.util.Slog.i(TAG, "Disable hint mode for device profile: " + deviceProfile);
                    this.mPowerManagerInternal.setPowerMode(18, false);
                    break;
                }
            }
            this.mCompanionAppBinder.removePackage(userId, packageName);
        }
        boolean shouldScheduleRebind = shouldScheduleRebind(userId, packageName, isPrimary);
        if (shouldScheduleRebind) {
            this.mCompanionAppBinder.scheduleRebinding(userId, packageName, serviceConnector);
        }
    }

    private boolean shouldScheduleRebind(int userId, java.lang.String packageName, boolean isPrimary) {
        boolean stillAssociated = false;
        boolean shouldScheduleRebind = false;
        boolean shouldScheduleRebindForUuid = false;
        java.util.List<com.android.server.companion.devicepresence.ObservableUuid> uuids = this.mObservableUuidStore.getObservableUuidsForPackage(userId, packageName);
        for (android.companion.AssociationInfo ai : this.mAssociationStore.getActiveAssociationsByPackage(userId, packageName)) {
            int associationId = ai.getId();
            stillAssociated = true;
            if (ai.isSelfManaged()) {
                if (isPrimary && isDevicePresent(associationId)) {
                    onSelfManagedDeviceReporterBinderDied(associationId);
                }
                shouldScheduleRebind = this.mCompanionAppBinder.isCompanionApplicationBound(userId, packageName);
            } else if (ai.isNotifyOnDeviceNearby()) {
                shouldScheduleRebind = true;
            }
        }
        java.util.Iterator<com.android.server.companion.devicepresence.ObservableUuid> it = uuids.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            com.android.server.companion.devicepresence.ObservableUuid uuid = it.next();
            if (isDeviceUuidPresent(uuid.getUuid())) {
                shouldScheduleRebindForUuid = true;
                break;
            }
        }
        return (stillAssociated && shouldScheduleRebind) || shouldScheduleRebindForUuid;
    }

    @Override // com.android.server.companion.association.AssociationStore.OnChangeListener
    public void onAssociationRemoved(android.companion.AssociationInfo association) {
        int id = association.getId();
        this.mConnectedBtDevices.remove(java.lang.Integer.valueOf(id));
        this.mNearbyBleDevices.remove(java.lang.Integer.valueOf(id));
        this.mReportedSelfManagedDevices.remove(java.lang.Integer.valueOf(id));
        this.mSimulated.remove(java.lang.Integer.valueOf(id));
        synchronized (this.mBtDisconnectedDevices) {
            this.mBtDisconnectedDevices.remove(java.lang.Integer.valueOf(id));
            this.mBtDisconnectedDevicesBlePresence.delete(id);
        }
    }

    private static void enforceCallerShellOrRoot() {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid == 2000 || callingUid == 0) {
        } else {
            throw new java.lang.SecurityException("Caller is neither Shell nor Root");
        }
    }

    private boolean canStopBleScan() {
        for (android.companion.AssociationInfo ai : this.mAssociationStore.getActiveAssociations()) {
            int id = ai.getId();
            synchronized (this.mBtDisconnectedDevices) {
                if (ai.isNotifyOnDeviceNearby() && (!isBtConnected(id) || !isBlePresent(id) || !this.mBtDisconnectedDevices.isEmpty())) {
                    android.util.Slog.i(TAG, "The BLE scan cannot be stopped, device( " + id + " ) is not yet connected OR the BLE is not current present Or is pending to report BLE lost");
                    return false;
                }
            }
        }
        return true;
    }

    private void onDeviceLocked(final int associationId, int userId, int event, final android.os.ParcelUuid uuid) {
        switch (event) {
            case 0:
            case 2:
                android.util.Slog.i(TAG, "Current user is not in unlocking or unlocked stage yet. Notify the application when the phone is unlocked");
                synchronized (this.mPendingDevicePresenceEvents) {
                    android.companion.DevicePresenceEvent devicePresenceEvent = new android.companion.DevicePresenceEvent(associationId, event, uuid);
                    java.util.List<android.companion.DevicePresenceEvent> deviceEvents = this.mPendingDevicePresenceEvents.get(userId, new java.util.ArrayList());
                    deviceEvents.add(devicePresenceEvent);
                    this.mPendingDevicePresenceEvents.put(userId, deviceEvents);
                    break;
                }
                return;
            case 1:
                synchronized (this.mPendingDevicePresenceEvents) {
                    java.util.List<android.companion.DevicePresenceEvent> deviceEvents2 = this.mPendingDevicePresenceEvents.get(userId);
                    if (deviceEvents2 != null) {
                        deviceEvents2.removeIf(new java.util.function.Predicate() { // from class: com.android.server.companion.devicepresence.DevicePresenceProcessor$$ExternalSyntheticLambda1
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj) {
                                return com.android.server.companion.devicepresence.DevicePresenceProcessor.lambda$onDeviceLocked$0(uuid, associationId, (android.companion.DevicePresenceEvent) obj);
                            }
                        });
                    }
                    break;
                }
                return;
            case 3:
                synchronized (this.mPendingDevicePresenceEvents) {
                    java.util.List<android.companion.DevicePresenceEvent> deviceEvents3 = this.mPendingDevicePresenceEvents.get(userId);
                    if (deviceEvents3 != null) {
                        deviceEvents3.removeIf(new java.util.function.Predicate() { // from class: com.android.server.companion.devicepresence.DevicePresenceProcessor$$ExternalSyntheticLambda2
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj) {
                                return com.android.server.companion.devicepresence.DevicePresenceProcessor.lambda$onDeviceLocked$1(uuid, associationId, (android.companion.DevicePresenceEvent) obj);
                            }
                        });
                    }
                    break;
                }
                return;
            default:
                android.util.Slog.e(TAG, "Event: " + event + "is not supported");
                return;
        }
    }

    static /* synthetic */ boolean lambda$onDeviceLocked$0(android.os.ParcelUuid uuid, int associationId, android.companion.DevicePresenceEvent deviceEvent) {
        return deviceEvent.getEvent() == 0 && java.util.Objects.equals(deviceEvent.getUuid(), uuid) && deviceEvent.getAssociationId() == associationId;
    }

    static /* synthetic */ boolean lambda$onDeviceLocked$1(android.os.ParcelUuid uuid, int associationId, android.companion.DevicePresenceEvent deviceEvent) {
        return deviceEvent.getEvent() == 2 && java.util.Objects.equals(deviceEvent.getUuid(), uuid) && deviceEvent.getAssociationId() == associationId;
    }

    public void sendDevicePresenceEventOnUnlocked(int userId) {
        java.util.List<android.companion.DevicePresenceEvent> deviceEvents = getPendingDevicePresenceEventsByUserId(userId);
        if (com.android.internal.util.CollectionUtils.isEmpty(deviceEvents)) {
            return;
        }
        java.util.List<com.android.server.companion.devicepresence.ObservableUuid> observableUuids = this.mObservableUuidStore.getObservableUuidsForUser(userId);
        for (android.companion.DevicePresenceEvent deviceEvent : deviceEvents) {
            boolean isUuid = deviceEvent.getUuid() != null;
            if (isUuid) {
                for (com.android.server.companion.devicepresence.ObservableUuid uuid : observableUuids) {
                    if (uuid.getUuid().equals(deviceEvent.getUuid())) {
                        onDevicePresenceEventByUuid(uuid, 2);
                    }
                }
            } else {
                int event = deviceEvent.getEvent();
                int associationId = deviceEvent.getAssociationId();
                android.companion.AssociationInfo associationInfo = this.mAssociationStore.getAssociationById(associationId);
                if (associationInfo == null) {
                    return;
                }
                switch (event) {
                    case 0:
                        onBleCompanionDeviceFound(associationInfo.getId(), associationInfo.getUserId());
                        break;
                    case 1:
                    default:
                        android.util.Slog.e(TAG, "Event: " + event + "is not supported");
                        break;
                    case 2:
                        onBluetoothCompanionDeviceConnected(associationInfo.getId(), associationInfo.getUserId());
                        break;
                }
            }
        }
        removePendingDevicePresenceEventsByUserId(userId);
    }

    private java.util.List<android.companion.DevicePresenceEvent> getPendingDevicePresenceEventsByUserId(int userId) {
        java.util.List<android.companion.DevicePresenceEvent> list;
        synchronized (this.mPendingDevicePresenceEvents) {
            list = this.mPendingDevicePresenceEvents.get(userId, new java.util.ArrayList());
        }
        return list;
    }

    private void removePendingDevicePresenceEventsByUserId(int userId) {
        synchronized (this.mPendingDevicePresenceEvents) {
            if (this.mPendingDevicePresenceEvents.contains(userId)) {
                this.mPendingDevicePresenceEvents.remove(userId);
            }
        }
    }

    public void dump(java.io.PrintWriter out) {
        out.append("Companion Device Present: ");
        if (this.mConnectedBtDevices.isEmpty() && this.mNearbyBleDevices.isEmpty() && this.mReportedSelfManagedDevices.isEmpty()) {
            out.append("<empty>\n");
            return;
        }
        out.append("\n");
        out.append("  Connected Bluetooth Devices: ");
        if (this.mConnectedBtDevices.isEmpty()) {
            out.append("<empty>\n");
        } else {
            out.append("\n");
            java.util.Iterator<java.lang.Integer> it = this.mConnectedBtDevices.iterator();
            while (it.hasNext()) {
                int associationId = it.next().intValue();
                android.companion.AssociationInfo a = this.mAssociationStore.getAssociationById(associationId);
                out.append("    ").append((java.lang.CharSequence) a.toShortString()).append('\n');
            }
        }
        out.append("  Nearby BLE Devices: ");
        if (this.mNearbyBleDevices.isEmpty()) {
            out.append("<empty>\n");
        } else {
            out.append("\n");
            java.util.Iterator<java.lang.Integer> it2 = this.mNearbyBleDevices.iterator();
            while (it2.hasNext()) {
                int associationId2 = it2.next().intValue();
                android.companion.AssociationInfo a2 = this.mAssociationStore.getAssociationById(associationId2);
                out.append("    ").append((java.lang.CharSequence) a2.toShortString()).append('\n');
            }
        }
        out.append("  Self-Reported Devices: ");
        if (this.mReportedSelfManagedDevices.isEmpty()) {
            out.append("<empty>\n");
            return;
        }
        out.append("\n");
        java.util.Iterator<java.lang.Integer> it3 = this.mReportedSelfManagedDevices.iterator();
        while (it3.hasNext()) {
            int associationId3 = it3.next().intValue();
            android.companion.AssociationInfo a3 = this.mAssociationStore.getAssociationById(associationId3);
            out.append("    ").append((java.lang.CharSequence) a3.toShortString()).append('\n');
        }
    }

    private class SimulatedDevicePresenceSchedulerHelper extends android.os.Handler {
        SimulatedDevicePresenceSchedulerHelper() {
            super(android.os.Looper.getMainLooper());
        }

        void scheduleOnDeviceGoneCallForSimulatedDevicePresence(int associationId) {
            if (hasMessages(associationId)) {
                removeMessages(associationId);
            }
            sendEmptyMessageDelayed(associationId, 60000L);
        }

        void unscheduleOnDeviceGoneCallForSimulatedDevicePresence(int associationId) {
            removeMessages(associationId);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            int associationId = msg.what;
            if (com.android.server.companion.devicepresence.DevicePresenceProcessor.this.mSimulated.contains(java.lang.Integer.valueOf(associationId))) {
                com.android.server.companion.devicepresence.DevicePresenceProcessor.this.onDevicePresenceEvent(com.android.server.companion.devicepresence.DevicePresenceProcessor.this.mSimulated, associationId, 1);
            }
        }
    }

    private class BleDeviceDisappearedScheduler extends android.os.Handler {
        BleDeviceDisappearedScheduler() {
            super(android.os.Looper.getMainLooper());
        }

        void scheduleBleDeviceDisappeared(int associationId) {
            if (hasMessages(associationId)) {
                removeMessages(associationId);
            }
            android.util.Slog.i(com.android.server.companion.devicepresence.DevicePresenceProcessor.TAG, "scheduleBleDeviceDisappeared for Device: ( " + associationId + " ).");
            sendEmptyMessageDelayed(associationId, 10000L);
        }

        void unScheduleDeviceDisappeared(int associationId) {
            if (hasMessages(associationId)) {
                android.util.Slog.i(com.android.server.companion.devicepresence.DevicePresenceProcessor.TAG, "unScheduleDeviceDisappeared for Device( " + associationId + " )");
                synchronized (com.android.server.companion.devicepresence.DevicePresenceProcessor.this.mBtDisconnectedDevices) {
                    com.android.server.companion.devicepresence.DevicePresenceProcessor.this.mBtDisconnectedDevices.remove(java.lang.Integer.valueOf(associationId));
                    com.android.server.companion.devicepresence.DevicePresenceProcessor.this.mBtDisconnectedDevicesBlePresence.delete(associationId);
                }
                removeMessages(associationId);
            }
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            int associationId = msg.what;
            synchronized (com.android.server.companion.devicepresence.DevicePresenceProcessor.this.mBtDisconnectedDevices) {
                boolean isCurrentPresent = com.android.server.companion.devicepresence.DevicePresenceProcessor.this.mBtDisconnectedDevicesBlePresence.get(associationId);
                if (com.android.server.companion.devicepresence.DevicePresenceProcessor.this.mBtDisconnectedDevices.contains(java.lang.Integer.valueOf(associationId)) && !isCurrentPresent) {
                    android.util.Slog.i(com.android.server.companion.devicepresence.DevicePresenceProcessor.TAG, "Device ( " + associationId + " ) is likely BLE out of range, sending callback with event ( 1 )");
                    com.android.server.companion.devicepresence.DevicePresenceProcessor.this.onDevicePresenceEvent(com.android.server.companion.devicepresence.DevicePresenceProcessor.this.mNearbyBleDevices, associationId, 1);
                }
                com.android.server.companion.devicepresence.DevicePresenceProcessor.this.mBtDisconnectedDevices.remove(java.lang.Integer.valueOf(associationId));
                com.android.server.companion.devicepresence.DevicePresenceProcessor.this.mBtDisconnectedDevicesBlePresence.delete(associationId);
            }
        }
    }
}
