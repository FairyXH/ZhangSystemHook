package com.android.server.companion.association;

/* JADX INFO: loaded from: classes.dex */
public class AssociationRequestsProcessor {
    private static final int ASSOCIATE_WITHOUT_PROMPT_MAX_PER_TIME_WINDOW = 5;
    private static final long ASSOCIATE_WITHOUT_PROMPT_WINDOW_MS = 3600000;
    private static final java.lang.String EXTRA_APPLICATION_CALLBACK = "application_callback";
    private static final java.lang.String EXTRA_ASSOCIATION = "association";
    private static final java.lang.String EXTRA_ASSOCIATION_REQUEST = "association_request";
    private static final java.lang.String EXTRA_FORCE_CANCEL_CONFIRMATION = "cancel_confirmation";
    private static final java.lang.String EXTRA_MAC_ADDRESS = "mac_address";
    private static final java.lang.String EXTRA_RESULT_RECEIVER = "result_receiver";
    private static final int RESULT_CODE_ASSOCIATION_APPROVED = 0;
    private static final int RESULT_CODE_ASSOCIATION_CREATED = 0;
    private static final java.lang.String TAG = "CDM_AssociationRequestsProcessor";
    private final com.android.server.companion.association.AssociationStore mAssociationStore;
    private final android.content.ComponentName mCompanionAssociationActivity;
    private final android.content.Context mContext;
    private final android.os.ResultReceiver mOnRequestConfirmationReceiver = new android.os.ResultReceiver(android.os.Handler.getMain()) { // from class: com.android.server.companion.association.AssociationRequestsProcessor.1
        @Override // android.os.ResultReceiver
        protected void onReceiveResult(int resultCode, android.os.Bundle data) {
            android.net.MacAddress macAddress;
            if (resultCode != 0) {
                android.util.Slog.w(com.android.server.companion.association.AssociationRequestsProcessor.TAG, "Unknown result code:" + resultCode);
                return;
            }
            android.companion.AssociationRequest request = (android.companion.AssociationRequest) data.getParcelable(com.android.server.companion.association.AssociationRequestsProcessor.EXTRA_ASSOCIATION_REQUEST, android.companion.AssociationRequest.class);
            android.companion.IAssociationRequestCallback callback = android.companion.IAssociationRequestCallback.Stub.asInterface(data.getBinder(com.android.server.companion.association.AssociationRequestsProcessor.EXTRA_APPLICATION_CALLBACK));
            android.os.ResultReceiver resultReceiver = (android.os.ResultReceiver) data.getParcelable(com.android.server.companion.association.AssociationRequestsProcessor.EXTRA_RESULT_RECEIVER, android.os.ResultReceiver.class);
            java.util.Objects.requireNonNull(request);
            java.util.Objects.requireNonNull(callback);
            java.util.Objects.requireNonNull(resultReceiver);
            if (request.isSelfManaged()) {
                macAddress = null;
            } else {
                macAddress = (android.net.MacAddress) data.getParcelable(com.android.server.companion.association.AssociationRequestsProcessor.EXTRA_MAC_ADDRESS, android.net.MacAddress.class);
                java.util.Objects.requireNonNull(macAddress);
            }
            com.android.server.companion.association.AssociationRequestsProcessor.this.processAssociationRequestApproval(request, callback, resultReceiver, macAddress);
        }
    };
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;

    public AssociationRequestsProcessor(android.content.Context context, android.content.pm.PackageManagerInternal packageManagerInternal, com.android.server.companion.association.AssociationStore associationStore) {
        this.mContext = context;
        this.mPackageManagerInternal = packageManagerInternal;
        this.mAssociationStore = associationStore;
        this.mCompanionAssociationActivity = android.content.ComponentName.createRelative(this.mContext.getString(android.R.string.config_customVpnConfirmDialogComponent), ".CompanionAssociationActivity");
    }

    public void processNewAssociationRequest(android.companion.AssociationRequest request, java.lang.String packageName, int userId, android.companion.IAssociationRequestCallback callback) {
        java.util.Objects.requireNonNull(request, "Request MUST NOT be null");
        if (request.isSelfManaged()) {
            java.util.Objects.requireNonNull(request.getDisplayName(), "AssociationRequest.displayName MUST NOT be null.");
        }
        java.util.Objects.requireNonNull(packageName, "Package name MUST NOT be null");
        java.util.Objects.requireNonNull(callback, "Callback MUST NOT be null");
        int packageUid = this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId);
        android.util.Slog.d(TAG, "processNewAssociationRequest() request=" + request + ", package=u" + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName + " (uid=" + packageUid + ")");
        com.android.server.companion.utils.PermissionsUtils.enforcePermissionForCreatingAssociation(this.mContext, request, packageUid);
        com.android.server.companion.utils.PackageUtils.enforceUsesCompanionDeviceFeature(this.mContext, userId, packageName);
        if (request.isSelfManaged() && !request.isForceConfirmation() && !willAddRoleHolder(request, packageName, userId)) {
            createAssociationAndNotifyApplication(request, packageName, userId, null, callback, null);
            return;
        }
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.watch")) {
            android.util.Slog.e(TAG, "3p apps are not allowed to create associations on watch.");
            try {
                callback.onFailure("3p apps are not allowed to create associations on watch.");
                return;
            } catch (android.os.RemoteException e) {
                return;
            }
        }
        request.setPackageName(packageName);
        request.setUserId(userId);
        request.setSkipPrompt(mayAssociateWithoutPrompt(packageName, userId));
        android.os.Bundle extras = new android.os.Bundle();
        extras.putParcelable(EXTRA_ASSOCIATION_REQUEST, request);
        extras.putBinder(EXTRA_APPLICATION_CALLBACK, callback.asBinder());
        extras.putParcelable(EXTRA_RESULT_RECEIVER, com.android.server.companion.utils.Utils.prepareForIpc(this.mOnRequestConfirmationReceiver));
        android.content.Intent intent = new android.content.Intent();
        intent.setComponent(this.mCompanionAssociationActivity);
        intent.putExtras(extras);
        android.app.PendingIntent pendingIntent = createPendingIntent(packageUid, intent);
        try {
            callback.onAssociationPending(pendingIntent);
        } catch (android.os.RemoteException e2) {
        }
    }

    public android.app.PendingIntent buildAssociationCancellationIntent(java.lang.String packageName, int userId) {
        java.util.Objects.requireNonNull(packageName, "Package name MUST NOT be null");
        com.android.server.companion.utils.PackageUtils.enforceUsesCompanionDeviceFeature(this.mContext, userId, packageName);
        int packageUid = this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId);
        android.os.Bundle extras = new android.os.Bundle();
        extras.putBoolean(EXTRA_FORCE_CANCEL_CONFIRMATION, true);
        android.content.Intent intent = new android.content.Intent();
        intent.setComponent(this.mCompanionAssociationActivity);
        intent.putExtras(extras);
        return createPendingIntent(packageUid, intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAssociationRequestApproval(android.companion.AssociationRequest request, android.companion.IAssociationRequestCallback callback, android.os.ResultReceiver resultReceiver, android.net.MacAddress macAddress) {
        java.lang.String packageName = request.getPackageName();
        int userId = request.getUserId();
        int packageUid = this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId);
        try {
            com.android.server.companion.utils.PermissionsUtils.enforcePermissionForCreatingAssociation(this.mContext, request, packageUid);
            createAssociationAndNotifyApplication(request, packageName, userId, macAddress, callback, resultReceiver);
        } catch (java.lang.SecurityException e) {
            try {
                callback.onFailure(e.getMessage());
            } catch (android.os.RemoteException e2) {
            }
        }
    }

    private void createAssociationAndNotifyApplication(final android.companion.AssociationRequest request, final java.lang.String packageName, final int userId, final android.net.MacAddress macAddress, final android.companion.IAssociationRequestCallback callback, final android.os.ResultReceiver resultReceiver) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.companion.association.AssociationRequestsProcessor$$ExternalSyntheticLambda3
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$createAssociationAndNotifyApplication$0(userId, packageName, macAddress, request, callback, resultReceiver);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createAssociationAndNotifyApplication$0(int userId, java.lang.String packageName, android.net.MacAddress macAddress, android.companion.AssociationRequest request, android.companion.IAssociationRequestCallback callback, android.os.ResultReceiver resultReceiver) throws java.lang.Exception {
        createAssociation(userId, packageName, macAddress, request.getDisplayName(), request.getDeviceProfile(), request.getAssociatedDevice(), request.isSelfManaged(), callback, resultReceiver);
    }

    public void createAssociation(int userId, java.lang.String packageName, android.net.MacAddress macAddress, java.lang.CharSequence displayName, java.lang.String deviceProfile, android.companion.AssociatedDevice associatedDevice, boolean selfManaged, android.companion.IAssociationRequestCallback callback, android.os.ResultReceiver resultReceiver) {
        int id = this.mAssociationStore.getNextId();
        long timestamp = java.lang.System.currentTimeMillis();
        android.companion.AssociationInfo association = new android.companion.AssociationInfo(id, userId, packageName, null, macAddress, displayName, deviceProfile, associatedDevice, selfManaged, false, false, false, timestamp, Long.MAX_VALUE, 0);
        maybeGrantRoleAndStoreAssociation(association, callback, resultReceiver);
    }

    public void maybeGrantRoleAndStoreAssociation(final android.companion.AssociationInfo association, final android.companion.IAssociationRequestCallback callback, final android.os.ResultReceiver resultReceiver) {
        com.android.server.companion.utils.RolesUtils.addRoleHolderForAssociation(this.mContext, association, new java.util.function.Consumer() { // from class: com.android.server.companion.association.AssociationRequestsProcessor$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$maybeGrantRoleAndStoreAssociation$1(association, callback, resultReceiver, (java.lang.Boolean) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeGrantRoleAndStoreAssociation$1(android.companion.AssociationInfo association, android.companion.IAssociationRequestCallback callback, android.os.ResultReceiver resultReceiver, java.lang.Boolean success) {
        if (success.booleanValue()) {
            android.util.Slog.i(TAG, "Added " + association.getDeviceProfile() + " role to userId=" + association.getUserId() + ", packageName=" + association.getPackageName());
            this.mAssociationStore.addAssociation(association);
            sendCallbackAndFinish(association, callback, resultReceiver);
        } else {
            android.util.Slog.e(TAG, "Failed to add u" + association.getUserId() + "\\" + association.getPackageName() + " to the list of " + association.getDeviceProfile() + " holders.");
            sendCallbackAndFinish(null, callback, resultReceiver);
        }
    }

    public void enableSystemDataSync(int associationId, int flags) {
        android.companion.AssociationInfo association = this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        android.companion.AssociationInfo updated = new android.companion.AssociationInfo.Builder(association).setSystemDataSyncFlags(association.getSystemDataSyncFlags() | flags).build();
        this.mAssociationStore.updateAssociation(updated);
    }

    public void disableSystemDataSync(int associationId, int flags) {
        android.companion.AssociationInfo association = this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        android.companion.AssociationInfo updated = new android.companion.AssociationInfo.Builder(association).setSystemDataSyncFlags(association.getSystemDataSyncFlags() & (~flags)).build();
        this.mAssociationStore.updateAssociation(updated);
    }

    public void setAssociationTag(int associationId, java.lang.String tag) {
        android.util.Slog.i(TAG, "Setting association tag=[" + tag + "] to id=[" + associationId + "]...");
        android.companion.AssociationInfo association = this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        this.mAssociationStore.updateAssociation(new android.companion.AssociationInfo.Builder(association).setTag(tag).build());
    }

    private void sendCallbackAndFinish(android.companion.AssociationInfo association, android.companion.IAssociationRequestCallback callback, android.os.ResultReceiver resultReceiver) {
        if (association != null) {
            if (callback != null) {
                try {
                    callback.onAssociationCreated(association);
                } catch (android.os.RemoteException e) {
                }
            }
            if (resultReceiver != null) {
                android.os.Bundle data = new android.os.Bundle();
                data.putParcelable(EXTRA_ASSOCIATION, association);
                resultReceiver.send(0, data);
                return;
            }
            return;
        }
        if (callback != null) {
            try {
                callback.onFailure("internal_error");
            } catch (android.os.RemoteException e2) {
            }
        }
        if (resultReceiver != null) {
            resultReceiver.send(3, new android.os.Bundle());
        }
    }

    private boolean willAddRoleHolder(android.companion.AssociationRequest request, final java.lang.String packageName, final int userId) {
        final java.lang.String deviceProfile = request.getDeviceProfile();
        if (deviceProfile == null) {
            return false;
        }
        boolean isRoleHolder = ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.companion.association.AssociationRequestsProcessor$$ExternalSyntheticLambda2
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$willAddRoleHolder$2(userId, packageName, deviceProfile);
            }
        })).booleanValue();
        return !isRoleHolder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$willAddRoleHolder$2(int userId, java.lang.String packageName, java.lang.String deviceProfile) throws java.lang.Exception {
        return java.lang.Boolean.valueOf(com.android.server.companion.utils.RolesUtils.isRoleHolder(this.mContext, userId, packageName, deviceProfile));
    }

    private android.app.PendingIntent createPendingIntent(final int packageUid, final android.content.Intent intent) {
        return (android.app.PendingIntent) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.companion.association.AssociationRequestsProcessor$$ExternalSyntheticLambda1
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$createPendingIntent$3(packageUid, intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.app.PendingIntent lambda$createPendingIntent$3(int packageUid, android.content.Intent intent) throws java.lang.Exception {
        return android.app.PendingIntent.getActivityAsUser(this.mContext, packageUid, intent, 1409286144, android.app.ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(1).toBundle(), android.os.UserHandle.CURRENT);
    }

    private boolean mayAssociateWithoutPrompt(java.lang.String packageName, int userId) {
        long now = java.lang.System.currentTimeMillis();
        java.util.List<android.companion.AssociationInfo> associationForPackage = this.mAssociationStore.getActiveAssociationsByPackage(userId, packageName);
        int recent = 0;
        for (android.companion.AssociationInfo association : associationForPackage) {
            boolean isRecent = now - association.getTimeApprovedMs() < 3600000;
            if (isRecent && (recent = recent + 1) >= 5) {
                android.util.Slog.w(TAG, "Too many associations: " + packageName + " already associated " + recent + " devices within the last 3600000ms");
                return false;
            }
        }
        return com.android.server.companion.utils.PackageUtils.isPackageAllowlisted(this.mContext, this.mPackageManagerInternal, packageName);
    }
}
