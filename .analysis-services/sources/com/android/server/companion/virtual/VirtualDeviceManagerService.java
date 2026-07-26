package com.android.server.companion.virtual;

/* JADX INFO: loaded from: classes.dex */
public class VirtualDeviceManagerService extends com.android.server.SystemService {
    private static final java.lang.String TAG = "VirtualDeviceManagerService";
    private static final java.lang.String VIRTUAL_DEVICE_NATIVE_SERVICE = "virtualdevice_native";
    private android.util.ArrayMap<java.lang.String, android.companion.AssociationInfo> mActiveAssociations;
    private final com.android.server.wm.ActivityInterceptorCallback mActivityInterceptorCallback;
    private final android.util.SparseArray<android.util.ArraySet<java.lang.Integer>> mAppsOnVirtualDevices;
    private final android.companion.CompanionDeviceManager.OnAssociationsChangedListener mCdmAssociationListener;
    private final android.os.Handler mHandler;
    private final com.android.server.companion.virtual.VirtualDeviceManagerService.VirtualDeviceManagerImpl mImpl;
    private final com.android.server.companion.virtual.VirtualDeviceManagerInternal mLocalService;
    private final com.android.server.companion.virtual.VirtualDeviceManagerService.VirtualDeviceManagerNativeImpl mNativeImpl;
    private final com.android.server.companion.virtual.VirtualDeviceManagerService.PendingTrampolineMap mPendingTrampolines;
    private final android.os.RemoteCallbackList<android.companion.virtual.IVirtualDeviceListener> mVirtualDeviceListeners;
    private com.android.server.companion.virtual.VirtualDeviceLog mVirtualDeviceLog;
    private final java.lang.Object mVirtualDeviceManagerLock;
    private final android.util.SparseArray<com.android.server.companion.virtual.VirtualDeviceImpl> mVirtualDevices;
    private static final java.util.List<java.lang.String> VIRTUAL_DEVICE_COMPANION_DEVICE_PROFILES = java.util.Arrays.asList("android.app.role.SYSTEM_AUTOMOTIVE_PROJECTION", "android.app.role.COMPANION_DEVICE_APP_STREAMING", "android.app.role.COMPANION_DEVICE_NEARBY_DEVICE_STREAMING");
    private static java.util.concurrent.atomic.AtomicInteger sNextUniqueIndex = new java.util.concurrent.atomic.AtomicInteger(1);

    public VirtualDeviceManagerService(android.content.Context context) {
        super(context);
        this.mVirtualDeviceManagerLock = new java.lang.Object();
        this.mVirtualDeviceLog = new com.android.server.companion.virtual.VirtualDeviceLog(getContext());
        this.mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        this.mPendingTrampolines = new com.android.server.companion.virtual.VirtualDeviceManagerService.PendingTrampolineMap(this.mHandler);
        this.mActiveAssociations = new android.util.ArrayMap<>();
        this.mCdmAssociationListener = new android.companion.CompanionDeviceManager.OnAssociationsChangedListener() { // from class: com.android.server.companion.virtual.VirtualDeviceManagerService.1
            public void onAssociationsChanged(java.util.List<android.companion.AssociationInfo> associations) {
                com.android.server.companion.virtual.VirtualDeviceManagerService.this.syncVirtualDevicesToCdmAssociations(associations);
            }
        };
        this.mVirtualDeviceListeners = new android.os.RemoteCallbackList<>();
        this.mVirtualDevices = new android.util.SparseArray<>();
        this.mAppsOnVirtualDevices = new android.util.SparseArray<>();
        this.mActivityInterceptorCallback = new com.android.server.wm.ActivityInterceptorCallback() { // from class: com.android.server.companion.virtual.VirtualDeviceManagerService.2
            @Override // com.android.server.wm.ActivityInterceptorCallback
            public com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptResult onInterceptActivityLaunch(com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo info) {
                com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline pt;
                if (info.getCallingPackage() == null || (pt = com.android.server.companion.virtual.VirtualDeviceManagerService.this.mPendingTrampolines.remove(info.getCallingPackage())) == null) {
                    return null;
                }
                pt.mResultReceiver.send(0, null);
                android.app.ActivityOptions options = info.getCheckedOptions();
                if (options == null) {
                    options = android.app.ActivityOptions.makeBasic();
                }
                return new com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptResult(info.getIntent(), options.setLaunchDisplayId(pt.mDisplayId));
            }
        };
        this.mImpl = new com.android.server.companion.virtual.VirtualDeviceManagerService.VirtualDeviceManagerImpl();
        this.mNativeImpl = android.companion.virtual.flags.Flags.enableNativeVdm() ? new com.android.server.companion.virtual.VirtualDeviceManagerService.VirtualDeviceManagerNativeImpl() : null;
        this.mLocalService = new com.android.server.companion.virtual.VirtualDeviceManagerService.LocalService();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("virtualdevice", this.mImpl);
        if (android.companion.virtual.flags.Flags.enableNativeVdm()) {
            publishBinderService(VIRTUAL_DEVICE_NATIVE_SERVICE, this.mNativeImpl);
        }
        publishLocalService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class, this.mLocalService);
        com.android.server.wm.ActivityTaskManagerInternal activityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) getLocalService(com.android.server.wm.ActivityTaskManagerInternal.class);
        activityTaskManagerInternal.registerActivityStartInterceptor(3, this.mActivityInterceptorCallback);
        if (android.companion.virtual.flags.Flags.persistentDeviceIdApi()) {
            android.companion.CompanionDeviceManager cdm = (android.companion.CompanionDeviceManager) getContext().getSystemService(android.companion.CompanionDeviceManager.class);
            if (cdm != null) {
                onCdmAssociationsChanged(cdm.getAllAssociations(-1));
                cdm.addOnAssociationsChangedListener(getContext().getMainExecutor(), new android.companion.CompanionDeviceManager.OnAssociationsChangedListener() { // from class: com.android.server.companion.virtual.VirtualDeviceManagerService$$ExternalSyntheticLambda0
                    public final void onAssociationsChanged(java.util.List list) {
                        this.f$0.onCdmAssociationsChanged(list);
                    }
                }, -1);
            } else {
                android.util.Slog.e(TAG, "Failed to find CompanionDeviceManager. No CDM association info  will be available.");
            }
        }
    }

    void onCameraAccessBlocked(int appUid) {
        java.util.ArrayList<com.android.server.companion.virtual.VirtualDeviceImpl> virtualDevicesSnapshot = getVirtualDevicesSnapshot();
        for (int i = 0; i < virtualDevicesSnapshot.size(); i++) {
            com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice = virtualDevicesSnapshot.get(i);
            virtualDevice.showToastWhereUidIsRunning(appUid, getContext().getString(android.R.string.time_picker_minute_label, virtualDevice.getDisplayName()), 1, android.os.Looper.myLooper());
        }
    }

    com.android.server.companion.virtual.CameraAccessController getCameraAccessController(android.os.UserHandle userHandle) {
        if (android.companion.virtual.flags.Flags.streamCamera()) {
            return null;
        }
        int userId = userHandle.getIdentifier();
        synchronized (this.mVirtualDeviceManagerLock) {
            for (int i = 0; i < this.mVirtualDevices.size(); i++) {
                com.android.server.companion.virtual.CameraAccessController cameraAccessController = this.mVirtualDevices.valueAt(i).getCameraAccessController();
                if (cameraAccessController.getUserId() == userId) {
                    return cameraAccessController;
                }
            }
            android.content.Context userContext = getContext().createContextAsUser(userHandle, 0);
            return new com.android.server.companion.virtual.CameraAccessController(userContext, this.mLocalService, new com.android.server.companion.virtual.CameraAccessController.CameraAccessBlockedCallback() { // from class: com.android.server.companion.virtual.VirtualDeviceManagerService$$ExternalSyntheticLambda1
                @Override // com.android.server.companion.virtual.CameraAccessController.CameraAccessBlockedCallback
                public final void onCameraAccessBlocked(int i2) {
                    this.f$0.onCameraAccessBlocked(i2);
                }
            });
        }
    }

    com.android.server.companion.virtual.VirtualDeviceManagerInternal getLocalServiceInstance() {
        return this.mLocalService;
    }

    void notifyRunningAppsChanged(int deviceId, android.util.ArraySet<java.lang.Integer> uids) {
        synchronized (this.mVirtualDeviceManagerLock) {
            if (!this.mVirtualDevices.contains(deviceId)) {
                android.util.Slog.e(TAG, "notifyRunningAppsChanged called for unknown deviceId:" + deviceId + " (maybe it was recently closed?)");
            } else {
                this.mAppsOnVirtualDevices.put(deviceId, uids);
                this.mLocalService.onAppsOnVirtualDeviceChanged();
            }
        }
    }

    void addVirtualDevice(com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice) {
        synchronized (this.mVirtualDeviceManagerLock) {
            this.mVirtualDevices.put(virtualDevice.getDeviceId(), virtualDevice);
        }
    }

    boolean removeVirtualDevice(final int deviceId) {
        synchronized (this.mVirtualDeviceManagerLock) {
            if (!this.mVirtualDevices.contains(deviceId)) {
                return false;
            }
            this.mAppsOnVirtualDevices.remove(deviceId);
            this.mVirtualDevices.remove(deviceId);
            if (android.companion.virtual.flags.Flags.vdmPublicApis()) {
                this.mVirtualDeviceListeners.broadcast(new java.util.function.Consumer() { // from class: com.android.server.companion.virtual.VirtualDeviceManagerService$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.companion.virtual.VirtualDeviceManagerService.lambda$removeVirtualDevice$0(deviceId, (android.companion.virtual.IVirtualDeviceListener) obj);
                    }
                });
            }
            android.content.Intent i = new android.content.Intent("android.companion.virtual.action.VIRTUAL_DEVICE_REMOVED");
            i.putExtra("android.companion.virtual.extra.VIRTUAL_DEVICE_ID", deviceId);
            i.setFlags(1073741824);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                getContext().sendBroadcastAsUser(i, android.os.UserHandle.ALL);
                if (!android.companion.virtual.flags.Flags.persistentDeviceIdApi()) {
                    synchronized (this.mVirtualDeviceManagerLock) {
                        if (this.mVirtualDevices.size() == 0) {
                            unregisterCdmAssociationListener();
                        }
                    }
                }
                android.os.Binder.restoreCallingIdentity(identity);
                return true;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(identity);
                throw th;
            }
        }
    }

    static /* synthetic */ void lambda$removeVirtualDevice$0(int deviceId, android.companion.virtual.IVirtualDeviceListener listener) {
        try {
            listener.onVirtualDeviceClosed(deviceId);
        } catch (android.os.RemoteException e) {
            android.util.Slog.i(TAG, "Failed to invoke onVirtualDeviceClosed listener: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncVirtualDevicesToCdmAssociations(java.util.List<android.companion.AssociationInfo> associations) {
        java.util.Set<com.android.server.companion.virtual.VirtualDeviceImpl> virtualDevicesToRemove = new java.util.HashSet<>();
        synchronized (this.mVirtualDeviceManagerLock) {
            if (this.mVirtualDevices.size() == 0) {
                return;
            }
            java.util.Set<java.lang.Integer> activeAssociationIds = new java.util.HashSet<>(associations.size());
            for (android.companion.AssociationInfo association : associations) {
                activeAssociationIds.add(java.lang.Integer.valueOf(association.getId()));
            }
            for (int i = 0; i < this.mVirtualDevices.size(); i++) {
                com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice = this.mVirtualDevices.valueAt(i);
                if (!activeAssociationIds.contains(java.lang.Integer.valueOf(virtualDevice.getAssociationId()))) {
                    virtualDevicesToRemove.add(virtualDevice);
                }
            }
            java.util.Iterator<com.android.server.companion.virtual.VirtualDeviceImpl> it = virtualDevicesToRemove.iterator();
            while (it.hasNext()) {
                it.next().close();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerCdmAssociationListener() {
        android.companion.CompanionDeviceManager cdm = (android.companion.CompanionDeviceManager) getContext().getSystemService(android.companion.CompanionDeviceManager.class);
        cdm.addOnAssociationsChangedListener(getContext().getMainExecutor(), this.mCdmAssociationListener);
    }

    private void unregisterCdmAssociationListener() {
        android.companion.CompanionDeviceManager cdm = (android.companion.CompanionDeviceManager) getContext().getSystemService(android.companion.CompanionDeviceManager.class);
        cdm.removeOnAssociationsChangedListener(this.mCdmAssociationListener);
    }

    void onCdmAssociationsChanged(java.util.List<android.companion.AssociationInfo> associations) {
        java.util.Set<java.lang.String> removedPersistentDeviceIds;
        android.util.ArrayMap<java.lang.String, android.companion.AssociationInfo> vdmAssociations = new android.util.ArrayMap<>();
        for (int i = 0; i < associations.size(); i++) {
            android.companion.AssociationInfo association = associations.get(i);
            if (VIRTUAL_DEVICE_COMPANION_DEVICE_PROFILES.contains(association.getDeviceProfile()) && !association.isRevoked()) {
                java.lang.String persistentId = com.android.server.companion.virtual.VirtualDeviceImpl.createPersistentDeviceId(association.getId());
                vdmAssociations.put(persistentId, association);
            }
        }
        java.util.Set<com.android.server.companion.virtual.VirtualDeviceImpl> virtualDevicesToRemove = new java.util.HashSet<>();
        synchronized (this.mVirtualDeviceManagerLock) {
            removedPersistentDeviceIds = this.mActiveAssociations.keySet();
            removedPersistentDeviceIds.removeAll(vdmAssociations.keySet());
            this.mActiveAssociations = vdmAssociations;
            for (int i2 = 0; i2 < this.mVirtualDevices.size(); i2++) {
                com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice = this.mVirtualDevices.valueAt(i2);
                if (removedPersistentDeviceIds.contains(virtualDevice.getPersistentDeviceId())) {
                    virtualDevicesToRemove.add(virtualDevice);
                }
            }
        }
        java.util.Iterator<com.android.server.companion.virtual.VirtualDeviceImpl> it = virtualDevicesToRemove.iterator();
        while (it.hasNext()) {
            it.next().close();
        }
        if (!removedPersistentDeviceIds.isEmpty()) {
            this.mLocalService.onPersistentDeviceIdsRemoved(removedPersistentDeviceIds);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.ArrayList<com.android.server.companion.virtual.VirtualDeviceImpl> getVirtualDevicesSnapshot() {
        java.util.ArrayList<com.android.server.companion.virtual.VirtualDeviceImpl> virtualDevices;
        synchronized (this.mVirtualDeviceManagerLock) {
            virtualDevices = new java.util.ArrayList<>(this.mVirtualDevices.size());
            for (int i = 0; i < this.mVirtualDevices.size(); i++) {
                virtualDevices.add(this.mVirtualDevices.valueAt(i));
            }
        }
        return virtualDevices;
    }

    class VirtualDeviceManagerImpl extends android.companion.virtual.IVirtualDeviceManager.Stub {
        private final com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampolineCallback mPendingTrampolineCallback = new com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampolineCallback() { // from class: com.android.server.companion.virtual.VirtualDeviceManagerService.VirtualDeviceManagerImpl.1
            @Override // com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampolineCallback
            public void startWaitingForPendingTrampoline(com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline pendingTrampoline) {
                com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline existing = com.android.server.companion.virtual.VirtualDeviceManagerService.this.mPendingTrampolines.put(pendingTrampoline.mPendingIntent.getCreatorPackage(), pendingTrampoline);
                if (existing != null) {
                    existing.mResultReceiver.send(2, null);
                }
            }

            @Override // com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampolineCallback
            public void stopWaitingForPendingTrampoline(com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline pendingTrampoline) {
                com.android.server.companion.virtual.VirtualDeviceManagerService.this.mPendingTrampolines.remove(pendingTrampoline.mPendingIntent.getCreatorPackage());
            }
        };

        VirtualDeviceManagerImpl() {
        }

        public android.companion.virtual.IVirtualDevice createVirtualDevice(android.os.IBinder token, android.content.AttributionSource attributionSource, int associationId, android.companion.virtual.VirtualDeviceParams params, android.companion.virtual.IVirtualDeviceActivityListener activityListener, android.companion.virtual.IVirtualDeviceSoundEffectListener soundEffectListener) {
            createVirtualDevice_enforcePermission();
            attributionSource.enforceCallingUid();
            int callingUid = getCallingUid();
            java.lang.String packageName = attributionSource.getPackageName();
            if (!com.android.server.companion.virtual.PermissionUtils.validateCallingPackageName(com.android.server.companion.virtual.VirtualDeviceManagerService.this.getContext(), packageName)) {
                throw new java.lang.SecurityException("Package name " + packageName + " does not belong to calling uid " + callingUid);
            }
            android.companion.AssociationInfo associationInfo = getAssociationInfo(packageName, associationId);
            if (associationInfo == null) {
                throw new java.lang.IllegalArgumentException("No association with ID " + associationId);
            }
            if (!com.android.server.companion.virtual.VirtualDeviceManagerService.VIRTUAL_DEVICE_COMPANION_DEVICE_PROFILES.contains(associationInfo.getDeviceProfile()) && android.companion.virtual.flags.Flags.persistentDeviceIdApi()) {
                throw new java.lang.IllegalArgumentException("Unsupported CDM Association device profile " + associationInfo.getDeviceProfile() + " for virtual device creation.");
            }
            java.util.Objects.requireNonNull(params);
            java.util.Objects.requireNonNull(activityListener);
            java.util.Objects.requireNonNull(soundEffectListener);
            android.os.UserHandle userHandle = getCallingUserHandle();
            com.android.server.companion.virtual.CameraAccessController cameraAccessController = com.android.server.companion.virtual.VirtualDeviceManagerService.this.getCameraAccessController(userHandle);
            final int deviceId = com.android.server.companion.virtual.VirtualDeviceManagerService.sNextUniqueIndex.getAndIncrement();
            java.util.function.Consumer<android.util.ArraySet<java.lang.Integer>> runningAppsChangedCallback = new java.util.function.Consumer() { // from class: com.android.server.companion.virtual.VirtualDeviceManagerService$VirtualDeviceManagerImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$createVirtualDevice$0(deviceId, (android.util.ArraySet) obj);
                }
            };
            com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice = new com.android.server.companion.virtual.VirtualDeviceImpl(com.android.server.companion.virtual.VirtualDeviceManagerService.this.getContext(), associationInfo, com.android.server.companion.virtual.VirtualDeviceManagerService.this, com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceLog, token, attributionSource, deviceId, cameraAccessController, this.mPendingTrampolineCallback, activityListener, soundEffectListener, runningAppsChangedCallback, params);
            if (android.companion.virtual.flags.Flags.expressMetrics()) {
                com.android.modules.expresslog.Counter.logIncrement("virtual_devices.value_virtual_devices_created_count");
            }
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                if (!android.companion.virtual.flags.Flags.persistentDeviceIdApi() && com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.size() == 0) {
                    long callingId = android.os.Binder.clearCallingIdentity();
                    try {
                        com.android.server.companion.virtual.VirtualDeviceManagerService.this.registerCdmAssociationListener();
                        android.os.Binder.restoreCallingIdentity(callingId);
                    } catch (java.lang.Throwable th) {
                        android.os.Binder.restoreCallingIdentity(callingId);
                        throw th;
                    }
                }
                com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.put(deviceId, virtualDevice);
            }
            if (android.companion.virtual.flags.Flags.vdmPublicApis()) {
                com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceListeners.broadcast(new java.util.function.Consumer() { // from class: com.android.server.companion.virtual.VirtualDeviceManagerService$VirtualDeviceManagerImpl$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.companion.virtual.VirtualDeviceManagerService.VirtualDeviceManagerImpl.lambda$createVirtualDevice$1(deviceId, (android.companion.virtual.IVirtualDeviceListener) obj);
                    }
                });
            }
            if (android.companion.virtualdevice.flags.Flags.metricsCollection()) {
                com.android.modules.expresslog.Counter.logIncrementWithUid("virtual_devices.value_virtual_devices_created_with_uid_count", attributionSource.getUid());
            }
            return virtualDevice;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createVirtualDevice$0(int deviceId, android.util.ArraySet runningUids) {
            com.android.server.companion.virtual.VirtualDeviceManagerService.this.notifyRunningAppsChanged(deviceId, runningUids);
        }

        static /* synthetic */ void lambda$createVirtualDevice$1(int deviceId, android.companion.virtual.IVirtualDeviceListener listener) {
            try {
                listener.onVirtualDeviceCreated(deviceId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.i(com.android.server.companion.virtual.VirtualDeviceManagerService.TAG, "Failed to invoke onVirtualDeviceCreated listener: " + e.getMessage());
            }
        }

        public int createVirtualDisplay(android.hardware.display.VirtualDisplayConfig virtualDisplayConfig, android.hardware.display.IVirtualDisplayCallback callback, android.companion.virtual.IVirtualDevice virtualDevice, java.lang.String packageName) throws android.os.RemoteException {
            com.android.server.companion.virtual.VirtualDeviceImpl virtualDeviceImpl;
            java.util.Objects.requireNonNull(virtualDisplayConfig);
            int callingUid = getCallingUid();
            if (!com.android.server.companion.virtual.PermissionUtils.validateCallingPackageName(com.android.server.companion.virtual.VirtualDeviceManagerService.this.getContext(), packageName)) {
                throw new java.lang.SecurityException("Package name " + packageName + " does not belong to calling uid " + callingUid);
            }
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                virtualDeviceImpl = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.get(virtualDevice.getDeviceId());
                if (virtualDeviceImpl == null) {
                    throw new java.lang.SecurityException("Invalid VirtualDevice (deviceId = " + virtualDevice.getDeviceId() + ")");
                }
            }
            if (virtualDeviceImpl.getOwnerUid() != callingUid) {
                throw new java.lang.SecurityException("uid " + callingUid + " is not the owner of the supplied VirtualDevice (deviceId = " + virtualDevice.getDeviceId() + ")");
            }
            return virtualDeviceImpl.createVirtualDisplay(virtualDisplayConfig, callback, packageName);
        }

        public java.util.List<android.companion.virtual.VirtualDevice> getVirtualDevices() {
            java.util.List<android.companion.virtual.VirtualDevice> virtualDevices = new java.util.ArrayList<>();
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                for (int i = 0; i < com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.size(); i++) {
                    com.android.server.companion.virtual.VirtualDeviceImpl device = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.valueAt(i);
                    virtualDevices.add(device.getPublicVirtualDeviceObject());
                }
            }
            return virtualDevices;
        }

        public android.companion.virtual.VirtualDevice getVirtualDevice(int deviceId) {
            com.android.server.companion.virtual.VirtualDeviceImpl device;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                device = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.get(deviceId);
            }
            if (device == null) {
                return null;
            }
            return device.getPublicVirtualDeviceObject();
        }

        public void registerVirtualDeviceListener(android.companion.virtual.IVirtualDeviceListener listener) {
            com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceListeners.register(listener);
        }

        public void unregisterVirtualDeviceListener(android.companion.virtual.IVirtualDeviceListener listener) {
            com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceListeners.unregister(listener);
        }

        public int getDevicePolicy(int deviceId, int policyType) {
            int devicePolicy;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.get(deviceId);
                devicePolicy = virtualDevice != null ? virtualDevice.getDevicePolicy(policyType) : 0;
            }
            return devicePolicy;
        }

        public int getDeviceIdForDisplayId(int displayId) {
            if (displayId == -1 || displayId == 0) {
                return 0;
            }
            java.util.ArrayList<com.android.server.companion.virtual.VirtualDeviceImpl> virtualDevicesSnapshot = com.android.server.companion.virtual.VirtualDeviceManagerService.this.getVirtualDevicesSnapshot();
            for (int i = 0; i < virtualDevicesSnapshot.size(); i++) {
                com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice = virtualDevicesSnapshot.get(i);
                if (virtualDevice.isDisplayOwnedByVirtualDevice(displayId)) {
                    return virtualDevice.getDeviceId();
                }
            }
            return 0;
        }

        public java.lang.CharSequence getDisplayNameForPersistentDeviceId(java.lang.String persistentDeviceId) {
            android.companion.AssociationInfo associationInfo;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                associationInfo = (android.companion.AssociationInfo) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mActiveAssociations.get(persistentDeviceId);
            }
            if (associationInfo == null) {
                return null;
            }
            return associationInfo.getDisplayName();
        }

        public java.util.List<java.lang.String> getAllPersistentDeviceIds() {
            return new java.util.ArrayList(com.android.server.companion.virtual.VirtualDeviceManagerService.this.mLocalService.getAllPersistentDeviceIds());
        }

        public boolean isValidVirtualDeviceId(int deviceId) {
            boolean zContains;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                zContains = com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.contains(deviceId);
            }
            return zContains;
        }

        public int getAudioPlaybackSessionId(int deviceId) {
            int audioPlaybackSessionId;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.get(deviceId);
                audioPlaybackSessionId = virtualDevice != null ? virtualDevice.getAudioPlaybackSessionId() : 0;
            }
            return audioPlaybackSessionId;
        }

        public int getAudioRecordingSessionId(int deviceId) {
            int audioRecordingSessionId;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.get(deviceId);
                audioRecordingSessionId = virtualDevice != null ? virtualDevice.getAudioRecordingSessionId() : 0;
            }
            return audioRecordingSessionId;
        }

        public void playSoundEffect(int deviceId, int effectType) {
            com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                virtualDevice = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.get(deviceId);
            }
            if (virtualDevice != null) {
                virtualDevice.playSoundEffect(effectType);
            }
        }

        public boolean isVirtualDeviceOwnedMirrorDisplay(int displayId) {
            if (getDeviceIdForDisplayId(displayId) == 0) {
                return false;
            }
            android.hardware.display.DisplayManagerInternal displayManager = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
            return displayManager.getDisplayIdToMirror(displayId) != -1;
        }

        private android.companion.AssociationInfo getAssociationInfo(java.lang.String packageName, int associationId) {
            android.os.UserHandle userHandle = getCallingUserHandle();
            android.companion.CompanionDeviceManager cdm = (android.companion.CompanionDeviceManager) com.android.server.companion.virtual.VirtualDeviceManagerService.this.getContext().createContextAsUser(userHandle, 0).getSystemService(android.companion.CompanionDeviceManager.class);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                java.util.List<android.companion.AssociationInfo> associations = cdm.getAllAssociations();
                android.os.Binder.restoreCallingIdentity(identity);
                int callingUserId = userHandle.getIdentifier();
                if (associations != null) {
                    int associationSize = associations.size();
                    for (int i = 0; i < associationSize; i++) {
                        android.companion.AssociationInfo associationInfo = associations.get(i);
                        if (associationInfo.belongsToPackage(callingUserId, packageName) && associationId == associationInfo.getId()) {
                            return associationInfo;
                        }
                    }
                    return null;
                }
                android.util.Slog.w(com.android.server.companion.virtual.VirtualDeviceManagerService.TAG, "No associations for user " + callingUserId);
                return null;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(identity);
                throw th;
            }
        }

        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            try {
                return super.onTransact(code, data, reply, flags);
            } catch (java.lang.Throwable e) {
                android.util.Slog.e(com.android.server.companion.virtual.VirtualDeviceManagerService.TAG, "Error during IPC", e);
                throw android.util.ExceptionUtils.propagate(e, android.os.RemoteException.class);
            }
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args) {
            if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(com.android.server.companion.virtual.VirtualDeviceManagerService.this.getContext(), com.android.server.companion.virtual.VirtualDeviceManagerService.TAG, fout)) {
                return;
            }
            fout.println("Created virtual devices: ");
            java.util.ArrayList<com.android.server.companion.virtual.VirtualDeviceImpl> virtualDevicesSnapshot = com.android.server.companion.virtual.VirtualDeviceManagerService.this.getVirtualDevicesSnapshot();
            for (int i = 0; i < virtualDevicesSnapshot.size(); i++) {
                virtualDevicesSnapshot.get(i).dump(fd, fout, args);
            }
            com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceLog.dump(fout);
        }
    }

    final class VirtualDeviceManagerNativeImpl extends android.companion.virtualnative.IVirtualDeviceManagerNative.Stub {
        VirtualDeviceManagerNativeImpl() {
        }

        public int[] getDeviceIdsForUid(int uid) {
            return com.android.server.companion.virtual.VirtualDeviceManagerService.this.mLocalService.getDeviceIdsForUid(uid).stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray();
        }

        public int getDevicePolicy(int deviceId, int policyType) {
            return com.android.server.companion.virtual.VirtualDeviceManagerService.this.mImpl.getDevicePolicy(deviceId, policyType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class LocalService extends com.android.server.companion.virtual.VirtualDeviceManagerInternal {
        private final android.util.ArraySet<java.lang.Integer> mAllUidsOnVirtualDevice;
        private final java.util.ArrayList<com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener> mAppsOnVirtualDeviceListeners;
        private final java.util.ArrayList<java.util.function.Consumer<java.lang.String>> mPersistentDeviceIdRemovedListeners;

        private LocalService() {
            this.mAppsOnVirtualDeviceListeners = new java.util.ArrayList<>();
            this.mPersistentDeviceIdRemovedListeners = new java.util.ArrayList<>();
            this.mAllUidsOnVirtualDevice = new android.util.ArraySet<>();
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public int getDeviceOwnerUid(int deviceId) {
            com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                virtualDevice = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.get(deviceId);
            }
            if (virtualDevice != null) {
                return virtualDevice.getOwnerUid();
            }
            return -1;
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public android.companion.virtual.sensor.VirtualSensor getVirtualSensor(int deviceId, int handle) {
            com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                virtualDevice = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.get(deviceId);
            }
            if (virtualDevice != null) {
                return virtualDevice.getVirtualSensorByHandle(handle);
            }
            return null;
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public android.util.ArraySet<java.lang.Integer> getDeviceIdsForUid(int uid) {
            java.util.ArrayList<com.android.server.companion.virtual.VirtualDeviceImpl> virtualDevicesSnapshot = com.android.server.companion.virtual.VirtualDeviceManagerService.this.getVirtualDevicesSnapshot();
            android.util.ArraySet<java.lang.Integer> result = new android.util.ArraySet<>();
            for (int i = 0; i < virtualDevicesSnapshot.size(); i++) {
                com.android.server.companion.virtual.VirtualDeviceImpl device = virtualDevicesSnapshot.get(i);
                if (device.isAppRunningOnVirtualDevice(uid)) {
                    result.add(java.lang.Integer.valueOf(device.getDeviceId()));
                }
            }
            return result;
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public void onVirtualDisplayRemoved(android.companion.virtual.IVirtualDevice virtualDevice, int displayId) {
            com.android.server.companion.virtual.VirtualDeviceImpl virtualDeviceImpl;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                virtualDeviceImpl = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.get(((com.android.server.companion.virtual.VirtualDeviceImpl) virtualDevice).getDeviceId());
            }
            if (virtualDeviceImpl != null) {
                virtualDeviceImpl.onVirtualDisplayRemoved(displayId);
            }
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public void onAppsOnVirtualDeviceChanged() {
            final com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener[] listeners;
            final android.util.ArraySet<java.lang.Integer> latestRunningUids = new android.util.ArraySet<>();
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                int size = com.android.server.companion.virtual.VirtualDeviceManagerService.this.mAppsOnVirtualDevices.size();
                for (int i = 0; i < size; i++) {
                    latestRunningUids.addAll((android.util.ArraySet<? extends java.lang.Integer>) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mAppsOnVirtualDevices.valueAt(i));
                }
                if (!this.mAllUidsOnVirtualDevice.equals(latestRunningUids)) {
                    this.mAllUidsOnVirtualDevice.clear();
                    this.mAllUidsOnVirtualDevice.addAll((android.util.ArraySet<? extends java.lang.Integer>) latestRunningUids);
                    listeners = (com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener[]) this.mAppsOnVirtualDeviceListeners.toArray(new com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener[0]);
                } else {
                    listeners = null;
                }
            }
            if (listeners != null) {
                com.android.server.companion.virtual.VirtualDeviceManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.companion.virtual.VirtualDeviceManagerService$LocalService$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.companion.virtual.VirtualDeviceManagerService.LocalService.lambda$onAppsOnVirtualDeviceChanged$0(listeners, latestRunningUids);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$onAppsOnVirtualDeviceChanged$0(com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener[] listeners, android.util.ArraySet latestRunningUids) {
            for (com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener listener : listeners) {
                listener.onAppsOnAnyVirtualDeviceChanged(latestRunningUids);
            }
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public void onPersistentDeviceIdsRemoved(final java.util.Set<java.lang.String> removedPersistentDeviceIds) {
            final java.util.List<java.util.function.Consumer<java.lang.String>> persistentDeviceIdRemovedListeners;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                persistentDeviceIdRemovedListeners = java.util.List.copyOf(this.mPersistentDeviceIdRemovedListeners);
            }
            com.android.server.companion.virtual.VirtualDeviceManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.companion.virtual.VirtualDeviceManagerService$LocalService$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.companion.virtual.VirtualDeviceManagerService.LocalService.lambda$onPersistentDeviceIdsRemoved$1(removedPersistentDeviceIds, persistentDeviceIdRemovedListeners);
                }
            });
        }

        static /* synthetic */ void lambda$onPersistentDeviceIdsRemoved$1(java.util.Set removedPersistentDeviceIds, java.util.List persistentDeviceIdRemovedListeners) {
            java.util.Iterator it = removedPersistentDeviceIds.iterator();
            while (it.hasNext()) {
                java.lang.String persistentDeviceId = (java.lang.String) it.next();
                java.util.Iterator it2 = persistentDeviceIdRemovedListeners.iterator();
                while (it2.hasNext()) {
                    java.util.function.Consumer<java.lang.String> listener = (java.util.function.Consumer) it2.next();
                    listener.accept(persistentDeviceId);
                }
            }
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public void onAuthenticationPrompt(int uid) {
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                for (int i = 0; i < com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.size(); i++) {
                    com.android.server.companion.virtual.VirtualDeviceImpl device = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.valueAt(i);
                    device.showToastWhereUidIsRunning(uid, android.R.string.app_category_game, 1, android.os.Looper.getMainLooper());
                }
            }
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public int getBaseVirtualDisplayFlags(android.companion.virtual.IVirtualDevice virtualDevice) {
            return ((com.android.server.companion.virtual.VirtualDeviceImpl) virtualDevice).getBaseVirtualDisplayFlags();
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public android.os.LocaleList getPreferredLocaleListForUid(int uid) {
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                for (int i = 0; i < com.android.server.companion.virtual.VirtualDeviceManagerService.this.mAppsOnVirtualDevices.size(); i++) {
                    if (((android.util.ArraySet) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mAppsOnVirtualDevices.valueAt(i)).contains(java.lang.Integer.valueOf(uid))) {
                        int deviceId = com.android.server.companion.virtual.VirtualDeviceManagerService.this.mAppsOnVirtualDevices.keyAt(i);
                        return ((com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.get(deviceId)).getDeviceLocaleList();
                    }
                }
                return null;
            }
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public boolean isAppRunningOnAnyVirtualDevice(int uid) {
            java.util.ArrayList<com.android.server.companion.virtual.VirtualDeviceImpl> virtualDevicesSnapshot = com.android.server.companion.virtual.VirtualDeviceManagerService.this.getVirtualDevicesSnapshot();
            for (int i = 0; i < virtualDevicesSnapshot.size(); i++) {
                if (virtualDevicesSnapshot.get(i).isAppRunningOnVirtualDevice(uid)) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public boolean isInputDeviceOwnedByVirtualDevice(int inputDeviceId) {
            java.util.ArrayList<com.android.server.companion.virtual.VirtualDeviceImpl> virtualDevicesSnapshot = com.android.server.companion.virtual.VirtualDeviceManagerService.this.getVirtualDevicesSnapshot();
            for (int i = 0; i < virtualDevicesSnapshot.size(); i++) {
                if (virtualDevicesSnapshot.get(i).isInputDeviceOwnedByVirtualDevice(inputDeviceId)) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public android.util.ArraySet<java.lang.Integer> getDisplayIdsForDevice(int deviceId) {
            com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                virtualDevice = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.get(deviceId);
            }
            return virtualDevice == null ? new android.util.ArraySet<>() : (android.util.ArraySet) java.util.Arrays.stream(virtualDevice.getDisplayIds()).boxed().collect(java.util.stream.Collectors.toCollection(new java.util.function.Supplier() { // from class: com.android.server.companion.virtual.VirtualDeviceManagerService$LocalService$$ExternalSyntheticLambda1
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return new android.util.ArraySet();
                }
            }));
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public int getDeviceIdForDisplayId(int displayId) {
            return com.android.server.companion.virtual.VirtualDeviceManagerService.this.mImpl.getDeviceIdForDisplayId(displayId);
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public boolean isValidVirtualDeviceId(int deviceId) {
            return com.android.server.companion.virtual.VirtualDeviceManagerService.this.mImpl.isValidVirtualDeviceId(deviceId);
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public java.lang.String getPersistentIdForDevice(int deviceId) {
            com.android.server.companion.virtual.VirtualDeviceImpl virtualDevice;
            if (deviceId == 0) {
                return "default:0";
            }
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                virtualDevice = (com.android.server.companion.virtual.VirtualDeviceImpl) com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDevices.get(deviceId);
            }
            if (virtualDevice == null) {
                return null;
            }
            return virtualDevice.getPersistentDeviceId();
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public java.util.Set<java.lang.String> getAllPersistentDeviceIds() {
            java.util.Set<java.lang.String> setCopyOf;
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                setCopyOf = java.util.Set.copyOf(com.android.server.companion.virtual.VirtualDeviceManagerService.this.mActiveAssociations.keySet());
            }
            return setCopyOf;
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public void registerAppsOnVirtualDeviceListener(com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener listener) {
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                this.mAppsOnVirtualDeviceListeners.add(listener);
            }
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public void unregisterAppsOnVirtualDeviceListener(com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener listener) {
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                this.mAppsOnVirtualDeviceListeners.remove(listener);
            }
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public void registerPersistentDeviceIdRemovedListener(java.util.function.Consumer<java.lang.String> persistentDeviceIdRemovedListener) {
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                this.mPersistentDeviceIdRemovedListeners.add(persistentDeviceIdRemovedListener);
            }
        }

        @Override // com.android.server.companion.virtual.VirtualDeviceManagerInternal
        public void unregisterPersistentDeviceIdRemovedListener(java.util.function.Consumer<java.lang.String> persistentDeviceIdRemovedListener) {
            synchronized (com.android.server.companion.virtual.VirtualDeviceManagerService.this.mVirtualDeviceManagerLock) {
                this.mPersistentDeviceIdRemovedListeners.remove(persistentDeviceIdRemovedListener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class PendingTrampolineMap {
        private static final int TRAMPOLINE_WAIT_MS = 5000;
        private final android.os.Handler mHandler;
        private final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline> mMap = new java.util.concurrent.ConcurrentHashMap<>();

        PendingTrampolineMap(android.os.Handler handler) {
            this.mHandler = handler;
        }

        com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline put(java.lang.String packageName, final com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline pendingTrampoline) {
            com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline existing = this.mMap.put(packageName, pendingTrampoline);
            this.mHandler.removeCallbacksAndMessages(existing);
            this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.companion.virtual.VirtualDeviceManagerService$PendingTrampolineMap$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$put$0(pendingTrampoline);
                }
            }, pendingTrampoline, 5000L);
            return existing;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$put$0(com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline pendingTrampoline) {
            java.lang.String creatorPackage = pendingTrampoline.mPendingIntent.getCreatorPackage();
            if (creatorPackage != null) {
                remove(creatorPackage);
            }
        }

        com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline remove(java.lang.String packageName) {
            com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline pendingTrampoline = this.mMap.remove(packageName);
            this.mHandler.removeCallbacksAndMessages(pendingTrampoline);
            return pendingTrampoline;
        }
    }
}
