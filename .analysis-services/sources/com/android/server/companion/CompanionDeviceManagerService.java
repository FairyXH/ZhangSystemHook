package com.android.server.companion;

/* JADX INFO: loaded from: classes.dex */
public class CompanionDeviceManagerService extends com.android.server.SystemService {
    private static final int MAX_CN_LENGTH = 500;
    private static final long PAIR_WITHOUT_PROMPT_WINDOW_MS = 600000;
    private static final java.lang.String PREF_FILE_NAME = "companion_device_preferences.xml";
    private static final java.lang.String PREF_KEY_AUTO_REVOKE_GRANTS_DONE = "auto_revoke_grants_done";
    private static final java.lang.String TAG = "CDM_CompanionDeviceManagerService";
    private final android.app.ActivityManagerInternal mAmInternal;
    private final com.android.internal.app.IAppOpsService mAppOpsManager;
    private final com.android.server.companion.association.AssociationRequestsProcessor mAssociationRequestsProcessor;
    private final com.android.server.companion.association.AssociationStore mAssociationStore;
    private final com.android.server.companion.association.AssociationStore.OnChangeListener mAssociationStoreChangeListener;
    private final com.android.server.wm.ActivityTaskManagerInternal mAtmInternal;
    private final com.android.server.companion.BackupRestoreProcessor mBackupRestoreProcessor;
    private final com.android.server.companion.devicepresence.CompanionAppBinder mCompanionAppBinder;
    private final com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController mCrossDeviceSyncController;
    private final com.android.server.companion.devicepresence.DevicePresenceProcessor mDevicePresenceProcessor;
    private final com.android.server.companion.association.DisassociationProcessor mDisassociationProcessor;
    private final com.android.server.companion.devicepresence.ObservableUuidStore mObservableUuidStore;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final com.android.internal.content.PackageMonitor mPackageMonitor;
    private final android.os.PowerExemptionManager mPowerExemptionManager;
    private final com.android.server.companion.datatransfer.SystemDataTransferProcessor mSystemDataTransferProcessor;
    private final com.android.server.companion.datatransfer.SystemDataTransferRequestStore mSystemDataTransferRequestStore;
    private final com.android.server.companion.transport.CompanionTransportManager mTransportManager;

    public CompanionDeviceManagerService(android.content.Context context) {
        super(context);
        this.mAssociationStoreChangeListener = new com.android.server.companion.association.AssociationStore.OnChangeListener() { // from class: com.android.server.companion.CompanionDeviceManagerService.1
            @Override // com.android.server.companion.association.AssociationStore.OnChangeListener
            public void onAssociationChanged(int changeType, android.companion.AssociationInfo association) {
                android.util.Slog.d(com.android.server.companion.CompanionDeviceManagerService.TAG, "onAssociationChanged changeType=[" + changeType + "], association=[" + association);
                int userId = association.getUserId();
                java.util.List<android.companion.AssociationInfo> updatedAssociations = com.android.server.companion.CompanionDeviceManagerService.this.mAssociationStore.getActiveAssociationsByUser(userId);
                com.android.server.companion.CompanionDeviceManagerService.this.updateAtm(userId, updatedAssociations);
                com.android.server.companion.CompanionDeviceManagerService.this.updateSpecialAccessPermissionForAssociatedPackage(association.getUserId(), association.getPackageName());
            }
        };
        this.mPackageMonitor = new com.android.internal.content.PackageMonitor() { // from class: com.android.server.companion.CompanionDeviceManagerService.2
            public void onPackageRemoved(java.lang.String packageName, int uid) {
                com.android.server.companion.CompanionDeviceManagerService.this.onPackageRemoveOrDataClearedInternal(getChangingUserId(), packageName);
            }

            public void onPackageDataCleared(java.lang.String packageName, int uid) {
                com.android.server.companion.CompanionDeviceManagerService.this.onPackageRemoveOrDataClearedInternal(getChangingUserId(), packageName);
            }

            public void onPackageModified(java.lang.String packageName) {
                com.android.server.companion.CompanionDeviceManagerService.this.onPackageModifiedInternal(getChangingUserId(), packageName);
            }

            public void onPackageAdded(java.lang.String packageName, int uid) {
                com.android.server.companion.CompanionDeviceManagerService.this.onPackageAddedInternal(getChangingUserId(), packageName);
            }
        };
        android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(android.app.ActivityManager.class);
        this.mPowerExemptionManager = (android.os.PowerExemptionManager) context.getSystemService(android.os.PowerExemptionManager.class);
        this.mAppOpsManager = com.android.internal.app.IAppOpsService.Stub.asInterface(android.os.ServiceManager.getService("appops"));
        this.mAtmInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        this.mAmInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        android.os.UserManager userManager = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
        android.os.PowerManagerInternal powerManagerInternal = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
        com.android.server.companion.association.AssociationDiskStore associationDiskStore = new com.android.server.companion.association.AssociationDiskStore();
        this.mAssociationStore = new com.android.server.companion.association.AssociationStore(context, userManager, associationDiskStore);
        this.mSystemDataTransferRequestStore = new com.android.server.companion.datatransfer.SystemDataTransferRequestStore();
        this.mObservableUuidStore = new com.android.server.companion.devicepresence.ObservableUuidStore();
        this.mAssociationRequestsProcessor = new com.android.server.companion.association.AssociationRequestsProcessor(context, this.mPackageManagerInternal, this.mAssociationStore);
        this.mBackupRestoreProcessor = new com.android.server.companion.BackupRestoreProcessor(context, this.mPackageManagerInternal, this.mAssociationStore, associationDiskStore, this.mSystemDataTransferRequestStore, this.mAssociationRequestsProcessor);
        this.mCompanionAppBinder = new com.android.server.companion.devicepresence.CompanionAppBinder(context);
        this.mDevicePresenceProcessor = new com.android.server.companion.devicepresence.DevicePresenceProcessor(context, this.mCompanionAppBinder, userManager, this.mAssociationStore, this.mObservableUuidStore, powerManagerInternal);
        this.mTransportManager = new com.android.server.companion.transport.CompanionTransportManager(context, this.mAssociationStore);
        this.mDisassociationProcessor = new com.android.server.companion.association.DisassociationProcessor(context, activityManager, this.mAssociationStore, this.mPackageManagerInternal, this.mDevicePresenceProcessor, this.mCompanionAppBinder, this.mSystemDataTransferRequestStore, this.mTransportManager);
        this.mSystemDataTransferProcessor = new com.android.server.companion.datatransfer.SystemDataTransferProcessor(this, this.mPackageManagerInternal, this.mAssociationStore, this.mSystemDataTransferRequestStore, this.mTransportManager);
        this.mCrossDeviceSyncController = new com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController(getContext(), this.mTransportManager);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.mAssociationStore.refreshCache();
        this.mAssociationStore.registerLocalListener(this.mAssociationStoreChangeListener);
        this.mObservableUuidStore.getObservableUuidsForUser(getContext().getUserId());
        publishBinderService("companiondevice", new com.android.server.companion.CompanionDeviceManagerService.CompanionDeviceManagerImpl());
        com.android.server.LocalServices.addService(com.android.server.companion.CompanionDeviceManagerServiceInternal.class, new com.android.server.companion.CompanionDeviceManagerService.LocalService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        android.content.Context context = getContext();
        if (phase == 500) {
            this.mPackageMonitor.register(context, com.android.server.FgThread.get().getLooper(), android.os.UserHandle.ALL, true);
            this.mDevicePresenceProcessor.init(context);
        } else if (phase == 1000) {
            com.android.server.companion.association.InactiveAssociationsRemovalService.schedule(getContext());
            this.mCrossDeviceSyncController.onBootCompleted();
        }
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        android.util.Slog.d(TAG, "onUserUnlocking...");
        int userId = user.getUserIdentifier();
        java.util.List<android.companion.AssociationInfo> associations = this.mAssociationStore.getActiveAssociationsByUser(userId);
        if (associations.isEmpty()) {
            return;
        }
        updateAtm(userId, associations);
        com.android.internal.os.BackgroundThread.getHandler().sendMessageDelayed(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.companion.CompanionDeviceManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.companion.CompanionDeviceManagerService) obj).maybeGrantAutoRevokeExemptions();
            }
        }, this), java.util.concurrent.TimeUnit.MINUTES.toMillis(10L));
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocked(com.android.server.SystemService.TargetUser user) {
        android.util.Slog.i(TAG, "onUserUnlocked() user=" + user);
        this.mDevicePresenceProcessor.sendDevicePresenceEventOnUnlocked(user.getUserIdentifier());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageRemoveOrDataClearedInternal(int userId, java.lang.String packageName) {
        java.util.List<android.companion.AssociationInfo> associationsForPackage = this.mAssociationStore.getAssociationsByPackage(userId, packageName);
        if (!associationsForPackage.isEmpty()) {
            android.util.Slog.i(TAG, "Package removed or data cleared for user=[" + userId + "], package=[" + packageName + "]. Cleaning up CDM data...");
        }
        for (android.companion.AssociationInfo association : associationsForPackage) {
            this.mDisassociationProcessor.disassociate(association.getId());
        }
        java.util.List<com.android.server.companion.devicepresence.ObservableUuid> uuidsTobeObserved = this.mObservableUuidStore.getObservableUuidsForPackage(userId, packageName);
        for (com.android.server.companion.devicepresence.ObservableUuid uuid : uuidsTobeObserved) {
            this.mObservableUuidStore.removeObservableUuid(userId, uuid.getUuid(), packageName);
        }
        this.mCompanionAppBinder.onPackagesChanged(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageModifiedInternal(int userId, java.lang.String packageName) {
        java.util.List<android.companion.AssociationInfo> associationsForPackage = this.mAssociationStore.getAssociationsByPackage(userId, packageName);
        for (android.companion.AssociationInfo association : associationsForPackage) {
            updateSpecialAccessPermissionForAssociatedPackage(association.getUserId(), association.getPackageName());
        }
        this.mCompanionAppBinder.onPackagesChanged(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageAddedInternal(int userId, java.lang.String packageName) {
        this.mBackupRestoreProcessor.restorePendingAssociations(userId, packageName);
    }

    void removeInactiveSelfManagedAssociations() {
        this.mDisassociationProcessor.removeIdleSelfManagedAssociations();
    }

    public class CompanionDeviceManagerImpl extends android.companion.ICompanionDeviceManager.Stub {
        public CompanionDeviceManagerImpl() {
        }

        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            try {
                return super.onTransact(code, data, reply, flags);
            } catch (java.lang.Throwable e) {
                android.util.Slog.e(com.android.server.companion.CompanionDeviceManagerService.TAG, "Error during IPC", e);
                throw android.util.ExceptionUtils.propagate(e, android.os.RemoteException.class);
            }
        }

        public void associate(android.companion.AssociationRequest request, android.companion.IAssociationRequestCallback callback, java.lang.String packageName, int userId) throws android.os.RemoteException {
            android.util.Slog.i(com.android.server.companion.CompanionDeviceManagerService.TAG, "associate() request=" + request + ", package=u" + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName);
            com.android.server.companion.utils.PermissionsUtils.enforceCallerCanManageAssociationsForPackage(com.android.server.companion.CompanionDeviceManagerService.this.getContext(), userId, packageName, "create associations");
            com.android.server.companion.CompanionDeviceManagerService.this.mAssociationRequestsProcessor.processNewAssociationRequest(request, packageName, userId, callback);
        }

        public android.app.PendingIntent buildAssociationCancellationIntent(java.lang.String packageName, int userId) throws android.os.RemoteException {
            android.util.Slog.i(com.android.server.companion.CompanionDeviceManagerService.TAG, "buildAssociationCancellationIntent() package=u" + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName);
            com.android.server.companion.utils.PermissionsUtils.enforceCallerCanManageAssociationsForPackage(com.android.server.companion.CompanionDeviceManagerService.this.getContext(), userId, packageName, "build association cancellation intent");
            return com.android.server.companion.CompanionDeviceManagerService.this.mAssociationRequestsProcessor.buildAssociationCancellationIntent(packageName, userId);
        }

        public java.util.List<android.companion.AssociationInfo> getAssociations(java.lang.String packageName, int userId) {
            com.android.server.companion.utils.PermissionsUtils.enforceCallerCanManageAssociationsForPackage(com.android.server.companion.CompanionDeviceManagerService.this.getContext(), userId, packageName, "get associations");
            return com.android.server.companion.CompanionDeviceManagerService.this.mAssociationStore.getActiveAssociationsByPackage(userId, packageName);
        }

        public java.util.List<android.companion.AssociationInfo> getAllAssociationsForUser(int userId) throws android.os.RemoteException {
            getAllAssociationsForUser_enforcePermission();
            com.android.server.companion.utils.PermissionsUtils.enforceCallerIsSystemOrCanInteractWithUserId(com.android.server.companion.CompanionDeviceManagerService.this.getContext(), userId);
            if (userId == -1) {
                return com.android.server.companion.CompanionDeviceManagerService.this.mAssociationStore.getActiveAssociations();
            }
            return com.android.server.companion.CompanionDeviceManagerService.this.mAssociationStore.getActiveAssociationsByUser(userId);
        }

        public void addOnAssociationsChangedListener(android.companion.IOnAssociationsChangedListener listener, int userId) {
            addOnAssociationsChangedListener_enforcePermission();
            com.android.server.companion.utils.PermissionsUtils.enforceCallerIsSystemOrCanInteractWithUserId(com.android.server.companion.CompanionDeviceManagerService.this.getContext(), userId);
            com.android.server.companion.CompanionDeviceManagerService.this.mAssociationStore.registerRemoteListener(listener, userId);
        }

        public void removeOnAssociationsChangedListener(android.companion.IOnAssociationsChangedListener listener, int userId) {
            removeOnAssociationsChangedListener_enforcePermission();
            com.android.server.companion.utils.PermissionsUtils.enforceCallerIsSystemOrCanInteractWithUserId(com.android.server.companion.CompanionDeviceManagerService.this.getContext(), userId);
            com.android.server.companion.CompanionDeviceManagerService.this.mAssociationStore.unregisterRemoteListener(listener);
        }

        public void addOnTransportsChangedListener(android.companion.IOnTransportsChangedListener listener) {
            addOnTransportsChangedListener_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mTransportManager.addListener(listener);
        }

        public void removeOnTransportsChangedListener(android.companion.IOnTransportsChangedListener listener) {
            removeOnTransportsChangedListener_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mTransportManager.removeListener(listener);
        }

        public void sendMessage(int messageType, byte[] data, int[] associationIds) {
            sendMessage_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mTransportManager.sendMessage(messageType, data, associationIds);
        }

        public void addOnMessageReceivedListener(int messageType, android.companion.IOnMessageReceivedListener listener) {
            addOnMessageReceivedListener_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mTransportManager.addListener(messageType, listener);
        }

        public void removeOnMessageReceivedListener(int messageType, android.companion.IOnMessageReceivedListener listener) {
            removeOnMessageReceivedListener_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mTransportManager.removeListener(messageType, listener);
        }

        @java.lang.Deprecated
        public void legacyDisassociate(java.lang.String deviceMacAddress, java.lang.String packageName, int userId) {
            java.util.Objects.requireNonNull(deviceMacAddress);
            java.util.Objects.requireNonNull(packageName);
            com.android.server.companion.CompanionDeviceManagerService.this.mDisassociationProcessor.disassociate(userId, packageName, deviceMacAddress);
        }

        public void disassociate(int associationId) {
            com.android.server.companion.CompanionDeviceManagerService.this.mDisassociationProcessor.disassociate(associationId);
        }

        public android.app.PendingIntent requestNotificationAccess(final android.content.ComponentName component, final int userId) throws android.os.RemoteException {
            final int callingUid = getCallingUid();
            final java.lang.String callingPackage = component.getPackageName();
            checkCanCallNotificationApi(callingPackage, userId);
            if (component.flattenToString().length() > 500) {
                throw new java.lang.IllegalArgumentException("Component name is too long.");
            }
            return (android.app.PendingIntent) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.companion.CompanionDeviceManagerService$CompanionDeviceManagerImpl$$ExternalSyntheticLambda0
                public final java.lang.Object getOrThrow() {
                    return this.f$0.lambda$requestNotificationAccess$0(callingPackage, callingUid, userId, component);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ android.app.PendingIntent lambda$requestNotificationAccess$0(java.lang.String callingPackage, int callingUid, int userId, android.content.ComponentName component) throws java.lang.Exception {
            android.content.Intent intent;
            if (!com.android.server.companion.utils.PackageUtils.isRestrictedSettingsAllowed(com.android.server.companion.CompanionDeviceManagerService.this.getContext(), callingPackage, callingUid)) {
                android.util.Slog.e(com.android.server.companion.CompanionDeviceManagerService.TAG, "Side loaded app must enable restricted setting before request the notification access");
                if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.enhancedConfirmationModeApisEnabled()) {
                    intent = ((android.app.ecm.EnhancedConfirmationManager) com.android.server.companion.CompanionDeviceManagerService.this.getContext().getSystemService(android.app.ecm.EnhancedConfirmationManager.class)).createRestrictedSettingDialogIntent(callingPackage, "android:access_notifications");
                } else {
                    return null;
                }
            } else {
                intent = com.android.internal.notification.NotificationAccessConfirmationActivityContract.launcherIntent(com.android.server.companion.CompanionDeviceManagerService.this.getContext(), userId, component);
            }
            return android.app.PendingIntent.getActivityAsUser(com.android.server.companion.CompanionDeviceManagerService.this.getContext(), 0, intent, 1409286144, null, new android.os.UserHandle(userId));
        }

        @java.lang.Deprecated
        public boolean hasNotificationAccess(android.content.ComponentName component) throws android.os.RemoteException {
            checkCanCallNotificationApi(component.getPackageName(), android.os.UserHandle.getCallingUserId());
            android.app.NotificationManager nm = (android.app.NotificationManager) com.android.server.companion.CompanionDeviceManagerService.this.getContext().getSystemService(android.app.NotificationManager.class);
            return nm.isNotificationListenerAccessGranted(component);
        }

        public boolean isDeviceAssociatedForWifiConnection(java.lang.String packageName, final java.lang.String macAddress, int userId) {
            isDeviceAssociatedForWifiConnection_enforcePermission();
            boolean bypassMacPermission = com.android.server.companion.CompanionDeviceManagerService.this.getContext().getPackageManager().checkPermission("android.permission.COMPANION_APPROVE_WIFI_CONNECTIONS", packageName) == 0;
            if (bypassMacPermission) {
                return true;
            }
            return com.android.internal.util.CollectionUtils.any(com.android.server.companion.CompanionDeviceManagerService.this.mAssociationStore.getActiveAssociationsByPackage(userId, packageName), new java.util.function.Predicate() { // from class: com.android.server.companion.CompanionDeviceManagerService$CompanionDeviceManagerImpl$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((android.companion.AssociationInfo) obj).isLinkedTo(macAddress);
                }
            });
        }

        @java.lang.Deprecated
        public void legacyStartObservingDevicePresence(java.lang.String deviceAddress, java.lang.String callingPackage, int userId) throws android.os.RemoteException {
            legacyStartObservingDevicePresence_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mDevicePresenceProcessor.startObservingDevicePresence(userId, callingPackage, deviceAddress);
        }

        @java.lang.Deprecated
        public void legacyStopObservingDevicePresence(java.lang.String deviceAddress, java.lang.String callingPackage, int userId) throws android.os.RemoteException {
            legacyStopObservingDevicePresence_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mDevicePresenceProcessor.stopObservingDevicePresence(userId, callingPackage, deviceAddress);
        }

        public void startObservingDevicePresence(android.companion.ObservingDevicePresenceRequest request, java.lang.String packageName, int userId) {
            startObservingDevicePresence_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mDevicePresenceProcessor.startObservingDevicePresence(request, packageName, userId, true);
        }

        public void stopObservingDevicePresence(android.companion.ObservingDevicePresenceRequest request, java.lang.String packageName, int userId) {
            stopObservingDevicePresence_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mDevicePresenceProcessor.stopObservingDevicePresence(request, packageName, userId, true);
        }

        public boolean removeBond(int associationId, java.lang.String packageName, int userId) {
            removeBond_enforcePermission();
            android.util.Slog.i(com.android.server.companion.CompanionDeviceManagerService.TAG, "removeBond() associationId=" + associationId + ", package=u" + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName);
            com.android.server.companion.utils.PermissionsUtils.enforceCallerCanManageAssociationsForPackage(com.android.server.companion.CompanionDeviceManagerService.this.getContext(), userId, packageName, "remove bonds");
            android.companion.AssociationInfo association = com.android.server.companion.CompanionDeviceManagerService.this.mAssociationStore.getAssociationWithCallerChecks(associationId);
            android.net.MacAddress address = association.getDeviceMacAddress();
            if (address == null) {
                throw new java.lang.IllegalArgumentException("Association id=[" + associationId + "] doesn't have a device address.");
            }
            android.bluetooth.BluetoothAdapter btAdapter = ((android.bluetooth.BluetoothManager) com.android.server.companion.CompanionDeviceManagerService.this.getContext().getSystemService(android.bluetooth.BluetoothManager.class)).getAdapter();
            android.bluetooth.BluetoothDevice btDevice = btAdapter.getRemoteDevice(address.toString().toUpperCase());
            return btDevice.removeBond();
        }

        public android.app.PendingIntent buildPermissionTransferUserConsentIntent(java.lang.String packageName, int userId, int associationId) {
            return com.android.server.companion.CompanionDeviceManagerService.this.mSystemDataTransferProcessor.buildPermissionTransferUserConsentIntent(packageName, userId, associationId);
        }

        public boolean isPermissionTransferUserConsented(java.lang.String packageName, int userId, int associationId) {
            return com.android.server.companion.CompanionDeviceManagerService.this.mSystemDataTransferProcessor.isPermissionTransferUserConsented(associationId);
        }

        public void startSystemDataTransfer(java.lang.String packageName, int userId, int associationId, android.companion.ISystemDataTransferCallback callback) {
            com.android.server.companion.CompanionDeviceManagerService.this.mSystemDataTransferProcessor.startSystemDataTransfer(packageName, userId, associationId, callback);
        }

        public void attachSystemDataTransport(java.lang.String packageName, int userId, int associationId, android.os.ParcelFileDescriptor fd) {
            attachSystemDataTransport_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mTransportManager.attachSystemDataTransport(associationId, fd);
        }

        public void detachSystemDataTransport(java.lang.String packageName, int userId, int associationId) {
            detachSystemDataTransport_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mTransportManager.detachSystemDataTransport(associationId);
        }

        public void enableSecureTransport(boolean enabled) {
            enableSecureTransport_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mTransportManager.enableSecureTransport(enabled);
        }

        public void enableSystemDataSync(int associationId, int flags) {
            com.android.server.companion.CompanionDeviceManagerService.this.mAssociationRequestsProcessor.enableSystemDataSync(associationId, flags);
        }

        public void disableSystemDataSync(int associationId, int flags) {
            com.android.server.companion.CompanionDeviceManagerService.this.mAssociationRequestsProcessor.disableSystemDataSync(associationId, flags);
        }

        public void enablePermissionsSync(int associationId) {
            if (getCallingUid() != 1000) {
                throw new java.lang.SecurityException("Caller must be system UID");
            }
            com.android.server.companion.CompanionDeviceManagerService.this.mSystemDataTransferProcessor.enablePermissionsSync(associationId);
        }

        public void disablePermissionsSync(int associationId) {
            if (getCallingUid() != 1000) {
                throw new java.lang.SecurityException("Caller must be system UID");
            }
            com.android.server.companion.CompanionDeviceManagerService.this.mSystemDataTransferProcessor.disablePermissionsSync(associationId);
        }

        public android.companion.datatransfer.PermissionSyncRequest getPermissionSyncRequest(int associationId) {
            if (getCallingUid() != 1000) {
                throw new java.lang.SecurityException("Caller must be system UID");
            }
            return com.android.server.companion.CompanionDeviceManagerService.this.mSystemDataTransferProcessor.getPermissionSyncRequest(associationId);
        }

        public void notifySelfManagedDeviceAppeared(int associationId) {
            notifySelfManagedDeviceAppeared_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mDevicePresenceProcessor.notifySelfManagedDevicePresenceEvent(associationId, true);
        }

        public void notifySelfManagedDeviceDisappeared(int associationId) {
            notifySelfManagedDeviceDisappeared_enforcePermission();
            com.android.server.companion.CompanionDeviceManagerService.this.mDevicePresenceProcessor.notifySelfManagedDevicePresenceEvent(associationId, false);
        }

        public boolean isCompanionApplicationBound(java.lang.String packageName, int userId) {
            return com.android.server.companion.CompanionDeviceManagerService.this.mCompanionAppBinder.isCompanionApplicationBound(userId, packageName);
        }

        public void createAssociation(java.lang.String packageName, java.lang.String macAddress, int userId, byte[] certificate) {
            createAssociation_enforcePermission();
            if (!com.android.server.companion.CompanionDeviceManagerService.this.getContext().getPackageManager().hasSigningCertificate(packageName, certificate, 1)) {
                android.util.Slog.e(com.android.server.companion.CompanionDeviceManagerService.TAG, "Given certificate doesn't match the package certificate.");
            } else {
                android.net.MacAddress macAddressObj = android.net.MacAddress.fromString(macAddress);
                com.android.server.companion.CompanionDeviceManagerService.this.mAssociationRequestsProcessor.createAssociation(userId, packageName, macAddressObj, null, null, null, false, null, null);
            }
        }

        private void checkCanCallNotificationApi(java.lang.String callingPackage, int userId) {
            com.android.server.companion.utils.PermissionsUtils.enforceCallerIsSystemOr(userId, callingPackage);
            if (getCallingUid() == 1000) {
                return;
            }
            com.android.server.companion.utils.PackageUtils.enforceUsesCompanionDeviceFeature(com.android.server.companion.CompanionDeviceManagerService.this.getContext(), userId, callingPackage);
            com.android.internal.util.Preconditions.checkState(!com.android.internal.util.ArrayUtils.isEmpty(com.android.server.companion.CompanionDeviceManagerService.this.mAssociationStore.getActiveAssociationsByPackage(userId, callingPackage)), "App must have an association before calling this API");
        }

        public boolean canPairWithoutPrompt(java.lang.String packageName, java.lang.String macAddress, int userId) {
            android.companion.AssociationInfo association = com.android.server.companion.CompanionDeviceManagerService.this.mAssociationStore.getFirstAssociationByAddress(userId, packageName, macAddress);
            return association != null && java.lang.System.currentTimeMillis() - association.getTimeApprovedMs() < 600000;
        }

        public void setAssociationTag(int associationId, java.lang.String tag) {
            com.android.server.companion.CompanionDeviceManagerService.this.mAssociationRequestsProcessor.setAssociationTag(associationId, tag);
        }

        public void clearAssociationTag(int associationId) {
            setAssociationTag(associationId, null);
        }

        public byte[] getBackupPayload(int userId) {
            if (getCallingUid() != 1000) {
                throw new java.lang.SecurityException("Caller must be system");
            }
            return com.android.server.companion.CompanionDeviceManagerService.this.mBackupRestoreProcessor.getBackupPayload(userId);
        }

        public void applyRestoredPayload(byte[] payload, int userId) {
            if (getCallingUid() != 1000) {
                throw new java.lang.SecurityException("Caller must be system");
            }
            com.android.server.companion.CompanionDeviceManagerService.this.mBackupRestoreProcessor.applyRestoredPayload(payload, userId);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
            return new com.android.server.companion.CompanionDeviceShellCommand(com.android.server.companion.CompanionDeviceManagerService.this, com.android.server.companion.CompanionDeviceManagerService.this.mAssociationStore, com.android.server.companion.CompanionDeviceManagerService.this.mDevicePresenceProcessor, com.android.server.companion.CompanionDeviceManagerService.this.mTransportManager, com.android.server.companion.CompanionDeviceManagerService.this.mSystemDataTransferProcessor, com.android.server.companion.CompanionDeviceManagerService.this.mAssociationRequestsProcessor, com.android.server.companion.CompanionDeviceManagerService.this.mBackupRestoreProcessor, com.android.server.companion.CompanionDeviceManagerService.this.mDisassociationProcessor).exec(this, in.getFileDescriptor(), out.getFileDescriptor(), err.getFileDescriptor(), args);
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter out, java.lang.String[] args) {
            if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(com.android.server.companion.CompanionDeviceManagerService.this.getContext(), com.android.server.companion.CompanionDeviceManagerService.TAG, out)) {
                return;
            }
            com.android.server.companion.CompanionDeviceManagerService.this.mAssociationStore.dump(out);
            com.android.server.companion.CompanionDeviceManagerService.this.mDevicePresenceProcessor.dump(out);
            com.android.server.companion.CompanionDeviceManagerService.this.mCompanionAppBinder.dump(out);
            com.android.server.companion.CompanionDeviceManagerService.this.mTransportManager.dump(out);
            com.android.server.companion.CompanionDeviceManagerService.this.mSystemDataTransferRequestStore.dump(out);
        }
    }

    public void updateSpecialAccessPermissionForAssociatedPackage(int userId, java.lang.String packageName) {
        final android.content.pm.PackageInfo packageInfo = com.android.server.companion.utils.PackageUtils.getPackageInfo(getContext(), userId, packageName);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.companion.CompanionDeviceManagerService$$ExternalSyntheticLambda1
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$updateSpecialAccessPermissionForAssociatedPackage$0(packageInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: updateSpecialAccessPermissionAsSystem, reason: merged with bridge method [inline-methods] */
    public void lambda$updateSpecialAccessPermissionForAssociatedPackage$0(android.content.pm.PackageInfo packageInfo) {
        if (packageInfo == null) {
            return;
        }
        if (containsEither(packageInfo.requestedPermissions, "android.permission.RUN_IN_BACKGROUND", "android.permission.REQUEST_COMPANION_RUN_IN_BACKGROUND")) {
            this.mPowerExemptionManager.addToPermanentAllowList(packageInfo.packageName);
        } else {
            try {
                this.mPowerExemptionManager.removeFromPermanentAllowList(packageInfo.packageName);
            } catch (java.lang.UnsupportedOperationException e) {
                android.util.Slog.w(TAG, packageInfo.packageName + " can't be removed from power save whitelist. It might due to the package is whitelisted by the system.");
            }
        }
        android.net.NetworkPolicyManager networkPolicyManager = android.net.NetworkPolicyManager.from(getContext());
        try {
            if (containsEither(packageInfo.requestedPermissions, "android.permission.USE_DATA_IN_BACKGROUND", "android.permission.REQUEST_COMPANION_USE_DATA_IN_BACKGROUND")) {
                networkPolicyManager.addUidPolicy(packageInfo.applicationInfo.uid, 4);
            } else {
                networkPolicyManager.removeUidPolicy(packageInfo.applicationInfo.uid, 4);
            }
        } catch (java.lang.IllegalArgumentException e2) {
            android.util.Slog.e(TAG, e2.getMessage());
        }
        exemptFromAutoRevoke(packageInfo.packageName, packageInfo.applicationInfo.uid);
    }

    private void exemptFromAutoRevoke(java.lang.String packageName, int uid) {
        try {
            this.mAppOpsManager.setMode(97, uid, packageName, 1);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Error while granting auto revoke exemption for " + packageName, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAtm(int userId, java.util.List<android.companion.AssociationInfo> associations) {
        java.util.Set<java.lang.Integer> companionAppUids = new android.util.ArraySet<>();
        for (android.companion.AssociationInfo association : associations) {
            int uid = this.mPackageManagerInternal.getPackageUid(association.getPackageName(), 0L, userId);
            if (uid >= 0) {
                companionAppUids.add(java.lang.Integer.valueOf(uid));
            }
        }
        if (this.mAtmInternal != null) {
            this.mAtmInternal.setCompanionAppUids(userId, companionAppUids);
        }
        if (this.mAmInternal != null) {
            this.mAmInternal.setCompanionAppUids(userId, new android.util.ArraySet(companionAppUids));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v0 */
    /* JADX WARN: Type inference failed for: r6v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r6v2 */
    public void maybeGrantAutoRevokeExemptions() {
        android.util.Slog.d(TAG, "maybeGrantAutoRevokeExemptions()");
        android.content.pm.PackageManager pm = getContext().getPackageManager();
        int[] userIds = ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getUserIds();
        int length = userIds.length;
        ?? r6 = 0;
        int i = 0;
        while (i < length) {
            int userId = userIds[i];
            android.content.SharedPreferences sharedPreferences = getContext().getSharedPreferences(new java.io.File(android.os.Environment.getUserSystemDirectory(userId), PREF_FILE_NAME), (int) r6);
            if (!sharedPreferences.getBoolean(PREF_KEY_AUTO_REVOKE_GRANTS_DONE, r6)) {
                try {
                    java.util.List<android.companion.AssociationInfo> associations = this.mAssociationStore.getActiveAssociationsByUser(userId);
                    for (android.companion.AssociationInfo a : associations) {
                        try {
                            int uid = pm.getPackageUidAsUser(a.getPackageName(), userId);
                            exemptFromAutoRevoke(a.getPackageName(), uid);
                        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                            android.util.Slog.w(TAG, "Unknown companion package: " + a.getPackageName(), e);
                        }
                    }
                } finally {
                    sharedPreferences.edit().putBoolean(PREF_KEY_AUTO_REVOKE_GRANTS_DONE, true).apply();
                }
            }
            i++;
            r6 = 0;
        }
    }

    private static <T> boolean containsEither(T[] array, T a, T b) {
        return com.android.internal.util.ArrayUtils.contains(array, a) || com.android.internal.util.ArrayUtils.contains(array, b);
    }

    private class LocalService implements com.android.server.companion.CompanionDeviceManagerServiceInternal {
        private LocalService() {
        }

        @Override // com.android.server.companion.CompanionDeviceManagerServiceInternal
        public void removeInactiveSelfManagedAssociations() {
            com.android.server.companion.CompanionDeviceManagerService.this.mDisassociationProcessor.removeIdleSelfManagedAssociations();
        }

        @Override // com.android.server.companion.CompanionDeviceManagerServiceInternal
        public void registerCallMetadataSyncCallback(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncControllerCallback callback, int type) {
            if (com.android.server.companion.CompanionDeviceConfig.isEnabled(com.android.server.companion.CompanionDeviceConfig.ENABLE_CONTEXT_SYNC_TELECOM)) {
                com.android.server.companion.CompanionDeviceManagerService.this.mCrossDeviceSyncController.registerCallMetadataSyncCallback(callback, type);
            }
        }

        @Override // com.android.server.companion.CompanionDeviceManagerServiceInternal
        public void crossDeviceSync(int userId, java.util.Collection<com.android.server.companion.datatransfer.contextsync.CrossDeviceCall> calls) {
            if (com.android.server.companion.CompanionDeviceConfig.isEnabled(com.android.server.companion.CompanionDeviceConfig.ENABLE_CONTEXT_SYNC_TELECOM)) {
                com.android.server.companion.CompanionDeviceManagerService.this.mCrossDeviceSyncController.syncToAllDevicesForUserId(userId, calls);
            }
        }

        @Override // com.android.server.companion.CompanionDeviceManagerServiceInternal
        public void crossDeviceSync(android.companion.AssociationInfo associationInfo, java.util.Collection<com.android.server.companion.datatransfer.contextsync.CrossDeviceCall> calls) {
            if (com.android.server.companion.CompanionDeviceConfig.isEnabled(com.android.server.companion.CompanionDeviceConfig.ENABLE_CONTEXT_SYNC_TELECOM)) {
                com.android.server.companion.CompanionDeviceManagerService.this.mCrossDeviceSyncController.syncToSingleDevice(associationInfo, calls);
            }
        }

        @Override // com.android.server.companion.CompanionDeviceManagerServiceInternal
        public void sendCrossDeviceSyncMessage(int associationId, byte[] message) {
            if (com.android.server.companion.CompanionDeviceConfig.isEnabled(com.android.server.companion.CompanionDeviceConfig.ENABLE_CONTEXT_SYNC_TELECOM)) {
                com.android.server.companion.CompanionDeviceManagerService.this.mCrossDeviceSyncController.syncMessageToDevice(associationId, message);
            }
        }

        @Override // com.android.server.companion.CompanionDeviceManagerServiceInternal
        public void sendCrossDeviceSyncMessageToAllDevices(int userId, byte[] message) {
            if (com.android.server.companion.CompanionDeviceConfig.isEnabled(com.android.server.companion.CompanionDeviceConfig.ENABLE_CONTEXT_SYNC_TELECOM)) {
                com.android.server.companion.CompanionDeviceManagerService.this.mCrossDeviceSyncController.syncMessageToAllDevicesForUserId(userId, message);
            }
        }

        @Override // com.android.server.companion.CompanionDeviceManagerServiceInternal
        public void addSelfOwnedCallId(java.lang.String callId) {
            if (com.android.server.companion.CompanionDeviceConfig.isEnabled(com.android.server.companion.CompanionDeviceConfig.ENABLE_CONTEXT_SYNC_TELECOM)) {
                com.android.server.companion.CompanionDeviceManagerService.this.mCrossDeviceSyncController.addSelfOwnedCallId(callId);
            }
        }

        @Override // com.android.server.companion.CompanionDeviceManagerServiceInternal
        public void removeSelfOwnedCallId(java.lang.String callId) {
            if (com.android.server.companion.CompanionDeviceConfig.isEnabled(com.android.server.companion.CompanionDeviceConfig.ENABLE_CONTEXT_SYNC_TELECOM)) {
                com.android.server.companion.CompanionDeviceManagerService.this.mCrossDeviceSyncController.removeSelfOwnedCallId(callId);
            }
        }
    }
}
