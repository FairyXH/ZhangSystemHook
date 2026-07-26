package com.android.server.companion.association;

/* JADX INFO: loaded from: classes.dex */
public class DisassociationProcessor {
    private static final long ASSOCIATION_REMOVAL_TIME_WINDOW_DEFAULT = java.util.concurrent.TimeUnit.DAYS.toMillis(90);
    private static final java.lang.String SYS_PROP_DEBUG_REMOVAL_TIME_WINDOW = "debug.cdm.cdmservice.removal_time_window";
    private static final java.lang.String TAG = "CDM_DisassociationProcessor";
    private final android.app.ActivityManager mActivityManager;
    private final com.android.server.companion.association.AssociationStore mAssociationStore;
    private final com.android.server.companion.devicepresence.CompanionAppBinder mCompanionAppController;
    private final android.content.Context mContext;
    private final com.android.server.companion.devicepresence.DevicePresenceProcessor mDevicePresenceMonitor;
    private final com.android.server.companion.association.DisassociationProcessor.OnPackageVisibilityChangeListener mOnPackageVisibilityChangeListener = new com.android.server.companion.association.DisassociationProcessor.OnPackageVisibilityChangeListener();
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final com.android.server.companion.datatransfer.SystemDataTransferRequestStore mSystemDataTransferRequestStore;
    private final com.android.server.companion.transport.CompanionTransportManager mTransportManager;

    public DisassociationProcessor(android.content.Context context, android.app.ActivityManager activityManager, com.android.server.companion.association.AssociationStore associationStore, android.content.pm.PackageManagerInternal packageManager, com.android.server.companion.devicepresence.DevicePresenceProcessor devicePresenceMonitor, com.android.server.companion.devicepresence.CompanionAppBinder applicationController, com.android.server.companion.datatransfer.SystemDataTransferRequestStore systemDataTransferRequestStore, com.android.server.companion.transport.CompanionTransportManager companionTransportManager) {
        this.mContext = context;
        this.mActivityManager = activityManager;
        this.mAssociationStore = associationStore;
        this.mPackageManagerInternal = packageManager;
        this.mDevicePresenceMonitor = devicePresenceMonitor;
        this.mCompanionAppController = applicationController;
        this.mSystemDataTransferRequestStore = systemDataTransferRequestStore;
        this.mTransportManager = companionTransportManager;
    }

    public void disassociate(final int id) {
        android.util.Slog.i(TAG, "Disassociating id=[" + id + "]...");
        android.companion.AssociationInfo association = this.mAssociationStore.getAssociationWithCallerChecks(id);
        int userId = association.getUserId();
        java.lang.String packageName = association.getPackageName();
        final java.lang.String deviceProfile = association.getDeviceProfile();
        boolean isRoleInUseByOtherAssociations = deviceProfile != null && com.android.internal.util.CollectionUtils.any(this.mAssociationStore.getActiveAssociationsByPackage(userId, packageName), new java.util.function.Predicate() { // from class: com.android.server.companion.association.DisassociationProcessor$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.companion.association.DisassociationProcessor.lambda$disassociate$0(deviceProfile, id, (android.companion.AssociationInfo) obj);
            }
        });
        int packageProcessImportance = getPackageProcessImportance(userId, packageName);
        if (packageProcessImportance <= 200 && deviceProfile != null && !isRoleInUseByOtherAssociations) {
            android.util.Slog.i(TAG, "Cannot disassociate id=[" + id + "] now - process is visible. Start listening to package importance...");
            android.companion.AssociationInfo revokedAssociation = new android.companion.AssociationInfo.Builder(association).setRevoked(true).build();
            this.mAssociationStore.updateAssociation(revokedAssociation);
            startListening();
            return;
        }
        this.mTransportManager.detachSystemDataTransport(id);
        this.mSystemDataTransferRequestStore.removeRequestsByAssociationId(userId, id);
        this.mAssociationStore.removeAssociation(association.getId());
        if (!isRoleInUseByOtherAssociations && deviceProfile != null && !deviceProfile.equals("android.app.role.SYSTEM_AUTOMOTIVE_PROJECTION")) {
            com.android.server.companion.utils.RolesUtils.removeRoleHolderForAssociation(this.mContext, association.getUserId(), association.getPackageName(), association.getDeviceProfile());
        }
        boolean wasPresent = this.mDevicePresenceMonitor.isDevicePresent(id);
        if (!wasPresent || !association.isNotifyOnDeviceNearby()) {
            return;
        }
        boolean shouldStayBound = com.android.internal.util.CollectionUtils.any(this.mAssociationStore.getActiveAssociationsByPackage(userId, packageName), new java.util.function.Predicate() { // from class: com.android.server.companion.association.DisassociationProcessor$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$disassociate$1((android.companion.AssociationInfo) obj);
            }
        });
        if (!shouldStayBound) {
            this.mCompanionAppController.unbindCompanionApp(userId, packageName);
        }
    }

    static /* synthetic */ boolean lambda$disassociate$0(java.lang.String deviceProfile, int id, android.companion.AssociationInfo it) {
        return deviceProfile.equals(it.getDeviceProfile()) && id != it.getId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$disassociate$1(android.companion.AssociationInfo it) {
        return it.isNotifyOnDeviceNearby() && this.mDevicePresenceMonitor.isDevicePresent(it.getId());
    }

    @java.lang.Deprecated
    public void disassociate(int userId, java.lang.String packageName, java.lang.String macAddress) {
        android.companion.AssociationInfo association = this.mAssociationStore.getFirstAssociationByAddress(userId, packageName, macAddress);
        if (association == null) {
            throw new java.lang.IllegalArgumentException("Association for mac address=[" + macAddress + "] doesn't exist");
        }
        this.mAssociationStore.getAssociationWithCallerChecks(association.getId());
        disassociate(association.getId());
    }

    private int getPackageProcessImportance(final int userId, final java.lang.String packageName) {
        return ((java.lang.Integer) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.companion.association.DisassociationProcessor$$ExternalSyntheticLambda2
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$getPackageProcessImportance$2(packageName, userId);
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$getPackageProcessImportance$2(java.lang.String packageName, int userId) throws java.lang.Exception {
        int uid = this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId);
        return java.lang.Integer.valueOf(this.mActivityManager.getUidImportance(uid));
    }

    private void startListening() {
        android.util.Slog.i(TAG, "Start listening to uid importance changes...");
        try {
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.companion.association.DisassociationProcessor$$ExternalSyntheticLambda1
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$startListening$3();
                }
            });
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "Failed to start listening to uid importance changes.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startListening$3() throws java.lang.Exception {
        this.mActivityManager.addOnUidImportanceListener(this.mOnPackageVisibilityChangeListener, 200);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopListening() {
        android.util.Slog.i(TAG, "Stop listening to uid importance changes.");
        try {
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.companion.association.DisassociationProcessor$$ExternalSyntheticLambda0
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$stopListening$4();
                }
            });
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "Failed to stop listening to uid importance changes.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stopListening$4() throws java.lang.Exception {
        this.mActivityManager.removeOnUidImportanceListener(this.mOnPackageVisibilityChangeListener);
    }

    public void removeIdleSelfManagedAssociations() {
        android.util.Slog.i(TAG, "Removing idle self-managed associations.");
        long currentTime = java.lang.System.currentTimeMillis();
        long removalWindow = android.os.SystemProperties.getLong(SYS_PROP_DEBUG_REMOVAL_TIME_WINDOW, -1L);
        if (removalWindow <= 0) {
            removalWindow = ASSOCIATION_REMOVAL_TIME_WINDOW_DEFAULT;
        }
        for (android.companion.AssociationInfo association : this.mAssociationStore.getAssociations()) {
            if (association.isSelfManaged()) {
                boolean isInactive = currentTime - association.getLastTimeConnectedMs() >= removalWindow;
                if (isInactive) {
                    int id = association.getId();
                    android.util.Slog.i(TAG, "Removing inactive self-managed association=[" + association.toShortString() + "].");
                    disassociate(id);
                }
            }
        }
    }

    private class OnPackageVisibilityChangeListener implements android.app.ActivityManager.OnUidImportanceListener {
        private OnPackageVisibilityChangeListener() {
        }

        public void onUidImportance(int uid, int importance) {
            java.lang.String packageName;
            if (importance <= 200 || (packageName = com.android.server.companion.association.DisassociationProcessor.this.mPackageManagerInternal.getNameForUid(uid)) == null) {
                return;
            }
            int userId = android.os.UserHandle.getUserId(uid);
            for (android.companion.AssociationInfo association : com.android.server.companion.association.DisassociationProcessor.this.mAssociationStore.getRevokedAssociations(userId, packageName)) {
                com.android.server.companion.association.DisassociationProcessor.this.disassociate(association.getId());
            }
            if (com.android.server.companion.association.DisassociationProcessor.this.mAssociationStore.getRevokedAssociations().isEmpty()) {
                com.android.server.companion.association.DisassociationProcessor.this.stopListening();
            }
        }
    }
}
