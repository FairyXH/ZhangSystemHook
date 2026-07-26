package com.android.server.companion.datatransfer;

/* JADX INFO: loaded from: classes.dex */
public class SystemDataTransferProcessor {
    private static final java.lang.String EXTRA_COMPANION_DEVICE_NAME = "companion_device_name";
    private static final java.lang.String EXTRA_PERMISSION_SYNC_REQUEST = "permission_sync_request";
    private static final java.lang.String EXTRA_SYSTEM_DATA_TRANSFER_RESULT_RECEIVER = "system_data_transfer_result_receiver";
    private static final java.lang.String LOG_TAG = "CDM_SystemDataTransferProcessor";
    private static final int RESULT_CODE_SYSTEM_DATA_TRANSFER_ALLOWED = 0;
    private static final int RESULT_CODE_SYSTEM_DATA_TRANSFER_DISALLOWED = 1;
    private final com.android.server.companion.association.AssociationStore mAssociationStore;
    private final android.content.ComponentName mCompanionDeviceDataTransferActivity;
    private final android.content.Context mContext;
    private final java.util.concurrent.ExecutorService mExecutor;
    private final android.os.ResultReceiver mOnSystemDataTransferRequestConfirmationReceiver = new android.os.ResultReceiver(android.os.Handler.getMain()) { // from class: com.android.server.companion.datatransfer.SystemDataTransferProcessor.2
        @Override // android.os.ResultReceiver
        protected void onReceiveResult(int resultCode, android.os.Bundle data) {
            android.util.Slog.d(com.android.server.companion.datatransfer.SystemDataTransferProcessor.LOG_TAG, "onReceiveResult() code=" + resultCode + ", data=" + data);
            if (resultCode == 0 || resultCode == 1) {
                android.companion.datatransfer.SystemDataTransferRequest systemDataTransferRequest = (android.companion.datatransfer.PermissionSyncRequest) data.getParcelable(com.android.server.companion.datatransfer.SystemDataTransferProcessor.EXTRA_PERMISSION_SYNC_REQUEST, android.companion.datatransfer.PermissionSyncRequest.class);
                if (systemDataTransferRequest != null) {
                    systemDataTransferRequest.setUserConsented(resultCode == 0);
                    android.util.Slog.i(com.android.server.companion.datatransfer.SystemDataTransferProcessor.LOG_TAG, "Recording request: " + systemDataTransferRequest);
                    com.android.server.companion.datatransfer.SystemDataTransferProcessor.this.mSystemDataTransferRequestStore.writeRequest(systemDataTransferRequest.getUserId(), systemDataTransferRequest);
                    return;
                }
                return;
            }
            android.util.Slog.e(com.android.server.companion.datatransfer.SystemDataTransferProcessor.LOG_TAG, "Unknown result code:" + resultCode);
        }
    };
    private final android.content.pm.PackageManagerInternal mPackageManager;
    private final android.permission.PermissionControllerManager mPermissionControllerManager;
    private final com.android.server.companion.datatransfer.SystemDataTransferRequestStore mSystemDataTransferRequestStore;
    private final com.android.server.companion.transport.CompanionTransportManager mTransportManager;

    public SystemDataTransferProcessor(com.android.server.companion.CompanionDeviceManagerService service, android.content.pm.PackageManagerInternal packageManager, com.android.server.companion.association.AssociationStore associationStore, com.android.server.companion.datatransfer.SystemDataTransferRequestStore systemDataTransferRequestStore, com.android.server.companion.transport.CompanionTransportManager transportManager) {
        this.mContext = service.getContext();
        this.mPackageManager = packageManager;
        this.mAssociationStore = associationStore;
        this.mSystemDataTransferRequestStore = systemDataTransferRequestStore;
        this.mTransportManager = transportManager;
        android.companion.IOnMessageReceivedListener messageListener = new android.companion.IOnMessageReceivedListener() { // from class: com.android.server.companion.datatransfer.SystemDataTransferProcessor.1
            public void onMessageReceived(int associationId, byte[] data) throws android.os.RemoteException {
                com.android.server.companion.datatransfer.SystemDataTransferProcessor.this.onReceivePermissionRestore(data);
            }

            public android.os.IBinder asBinder() {
                return null;
            }
        };
        this.mTransportManager.addListener(1669491075, messageListener);
        this.mPermissionControllerManager = (android.permission.PermissionControllerManager) this.mContext.getSystemService(android.permission.PermissionControllerManager.class);
        this.mExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();
        this.mCompanionDeviceDataTransferActivity = android.content.ComponentName.createRelative(this.mContext.getString(android.R.string.config_customVpnConfirmDialogComponent), ".CompanionDeviceDataTransferActivity");
    }

    public boolean isPermissionTransferUserConsented(int associationId) {
        this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        android.companion.datatransfer.PermissionSyncRequest request = getPermissionSyncRequest(associationId);
        if (request == null) {
            return false;
        }
        return request.isUserConsented();
    }

    public android.app.PendingIntent buildPermissionTransferUserConsentIntent(java.lang.String packageName, int userId, final int associationId) {
        if (com.android.server.companion.utils.PackageUtils.isPermSyncAutoEnabled(this.mContext, this.mPackageManager, packageName)) {
            android.util.Slog.i(LOG_TAG, "User consent Intent should be skipped. Returning null.");
            if (getPermissionSyncRequest(associationId) == null) {
                android.companion.datatransfer.SystemDataTransferRequest permissionSyncRequest = new android.companion.datatransfer.PermissionSyncRequest(associationId);
                permissionSyncRequest.setUserConsented(true);
                this.mSystemDataTransferRequestStore.writeRequest(userId, permissionSyncRequest);
                return null;
            }
            return null;
        }
        android.util.Slog.i(LOG_TAG, "Creating permission sync intent for userId [" + userId + "] associationId [" + associationId + "]");
        android.companion.AssociationInfo association = this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        android.os.Bundle extras = new android.os.Bundle();
        android.companion.datatransfer.PermissionSyncRequest request = new android.companion.datatransfer.PermissionSyncRequest(associationId);
        request.setUserId(userId);
        extras.putParcelable(EXTRA_PERMISSION_SYNC_REQUEST, request);
        extras.putCharSequence(EXTRA_COMPANION_DEVICE_NAME, association.getDisplayName());
        extras.putParcelable(EXTRA_SYSTEM_DATA_TRANSFER_RESULT_RECEIVER, com.android.server.companion.utils.Utils.prepareForIpc(this.mOnSystemDataTransferRequestConfirmationReceiver));
        final android.content.Intent intent = new android.content.Intent();
        intent.setComponent(this.mCompanionDeviceDataTransferActivity);
        intent.putExtras(extras);
        return (android.app.PendingIntent) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.companion.datatransfer.SystemDataTransferProcessor$$ExternalSyntheticLambda3
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$buildPermissionTransferUserConsentIntent$0(associationId, intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.app.PendingIntent lambda$buildPermissionTransferUserConsentIntent$0(int associationId, android.content.Intent intent) throws java.lang.Exception {
        return android.app.PendingIntent.getActivityAsUser(this.mContext, associationId, intent, 1409286144, android.app.ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(1).toBundle(), android.os.UserHandle.CURRENT);
    }

    public void startSystemDataTransfer(java.lang.String packageName, final int userId, final int associationId, final android.companion.ISystemDataTransferCallback callback) {
        android.util.Slog.i(LOG_TAG, "Start system data transfer for package [" + packageName + "] userId [" + userId + "] associationId [" + associationId + "]");
        this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        android.companion.datatransfer.PermissionSyncRequest request = getPermissionSyncRequest(associationId);
        if (request == null || !request.isUserConsented()) {
            java.lang.String message = "User " + userId + " hasn't consented permission sync for associationId [" + associationId + ".";
            android.util.Slog.e(LOG_TAG, message);
            try {
                callback.onError(message);
                return;
            } catch (android.os.RemoteException e) {
                return;
            }
        }
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.companion.datatransfer.SystemDataTransferProcessor$$ExternalSyntheticLambda4
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$startSystemDataTransfer$2(userId, associationId, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSystemDataTransfer$2(int userId, final int associationId, final android.companion.ISystemDataTransferCallback callback) throws java.lang.Exception {
        this.mPermissionControllerManager.getRuntimePermissionBackup(android.os.UserHandle.of(userId), this.mExecutor, new java.util.function.Consumer() { // from class: com.android.server.companion.datatransfer.SystemDataTransferProcessor$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$startSystemDataTransfer$1(associationId, callback, (byte[]) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startSystemDataTransfer$1(int associationId, android.companion.ISystemDataTransferCallback callback, byte[] backup) {
        java.util.concurrent.Future<?> future = this.mTransportManager.requestPermissionRestore(associationId, backup);
        translateFutureToCallback(future, callback);
    }

    public void enablePermissionsSync(int associationId) {
        android.companion.AssociationInfo association = this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        int userId = association.getUserId();
        android.companion.datatransfer.SystemDataTransferRequest permissionSyncRequest = new android.companion.datatransfer.PermissionSyncRequest(associationId);
        permissionSyncRequest.setUserConsented(true);
        this.mSystemDataTransferRequestStore.writeRequest(userId, permissionSyncRequest);
    }

    public void disablePermissionsSync(int associationId) {
        android.companion.AssociationInfo association = this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        int userId = association.getUserId();
        android.companion.datatransfer.SystemDataTransferRequest permissionSyncRequest = new android.companion.datatransfer.PermissionSyncRequest(associationId);
        permissionSyncRequest.setUserConsented(false);
        this.mSystemDataTransferRequestStore.writeRequest(userId, permissionSyncRequest);
    }

    public android.companion.datatransfer.PermissionSyncRequest getPermissionSyncRequest(int associationId) {
        android.companion.AssociationInfo association = this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        int userId = association.getUserId();
        java.util.List<android.companion.datatransfer.SystemDataTransferRequest> requests = this.mSystemDataTransferRequestStore.readRequestsByAssociationId(userId, associationId);
        java.util.Iterator<android.companion.datatransfer.SystemDataTransferRequest> it = requests.iterator();
        while (it.hasNext()) {
            android.companion.datatransfer.PermissionSyncRequest permissionSyncRequest = (android.companion.datatransfer.SystemDataTransferRequest) it.next();
            if (permissionSyncRequest instanceof android.companion.datatransfer.PermissionSyncRequest) {
                return permissionSyncRequest;
            }
        }
        return null;
    }

    public void removePermissionSyncRequest(final int associationId) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.companion.datatransfer.SystemDataTransferProcessor$$ExternalSyntheticLambda2
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$removePermissionSyncRequest$3(associationId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removePermissionSyncRequest$3(int associationId) throws java.lang.Exception {
        android.companion.AssociationInfo association = this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        int userId = association.getUserId();
        this.mSystemDataTransferRequestStore.removeRequestsByAssociationId(userId, associationId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReceivePermissionRestore(final byte[] message) {
        if (!android.os.Build.isDebuggable() && !this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.watch")) {
            android.util.Slog.e(LOG_TAG, "Permissions restore is only available on watch.");
            return;
        }
        android.util.Slog.i(LOG_TAG, "Applying permissions.");
        final android.os.UserHandle user = this.mContext.getUser();
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.companion.datatransfer.SystemDataTransferProcessor$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$onReceivePermissionRestore$4(message, user);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onReceivePermissionRestore$4(byte[] message, android.os.UserHandle user) throws java.lang.Exception {
        this.mPermissionControllerManager.stageAndApplyRuntimePermissionsBackup(message, user);
    }

    private static void translateFutureToCallback(java.util.concurrent.Future<?> future, android.companion.ISystemDataTransferCallback callback) {
        try {
            future.get(15L, java.util.concurrent.TimeUnit.SECONDS);
            if (callback != null) {
                try {
                    callback.onResult();
                } catch (android.os.RemoteException e) {
                }
            }
        } catch (java.lang.Exception e2) {
            if (callback != null) {
                try {
                    callback.onError(e2.getMessage());
                } catch (android.os.RemoteException e3) {
                }
            }
        }
    }
}
