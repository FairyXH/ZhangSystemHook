package com.android.server.companion;

/* JADX INFO: loaded from: classes.dex */
class BackupRestoreProcessor {
    private static final int BACKUP_AND_RESTORE_VERSION = 0;
    private static final java.lang.String TAG = "CDM_BackupRestoreProcessor";
    private final com.android.server.companion.association.AssociationDiskStore mAssociationDiskStore;
    private final com.android.server.companion.association.AssociationRequestsProcessor mAssociationRequestsProcessor;
    private final com.android.server.companion.association.AssociationStore mAssociationStore;
    private final android.content.Context mContext;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final com.android.server.companion.datatransfer.SystemDataTransferRequestStore mSystemDataTransferRequestStore;

    BackupRestoreProcessor(android.content.Context context, android.content.pm.PackageManagerInternal packageManagerInternal, com.android.server.companion.association.AssociationStore associationStore, com.android.server.companion.association.AssociationDiskStore associationDiskStore, com.android.server.companion.datatransfer.SystemDataTransferRequestStore systemDataTransferRequestStore, com.android.server.companion.association.AssociationRequestsProcessor associationRequestsProcessor) {
        this.mContext = context;
        this.mPackageManagerInternal = packageManagerInternal;
        this.mAssociationStore = associationStore;
        this.mAssociationDiskStore = associationDiskStore;
        this.mSystemDataTransferRequestStore = systemDataTransferRequestStore;
        this.mAssociationRequestsProcessor = associationRequestsProcessor;
    }

    byte[] getBackupPayload(int userId) {
        android.util.Slog.i(TAG, "getBackupPayload() userId=[" + userId + "].");
        byte[] associationsPayload = this.mAssociationDiskStore.getBackupPayload(userId);
        int associationsPayloadLength = associationsPayload.length;
        byte[] requestsPayload = this.mSystemDataTransferRequestStore.getBackupPayload(userId);
        int requestsPayloadLength = requestsPayload.length;
        int payloadSize = associationsPayloadLength + 12 + requestsPayloadLength;
        return java.nio.ByteBuffer.allocate(payloadSize).putInt(0).putInt(associationsPayloadLength).put(associationsPayload).putInt(requestsPayloadLength).put(requestsPayload).array();
    }

    void applyRestoredPayload(byte[] payload, int userId) {
        android.util.Slog.i(TAG, "applyRestoredPayload() userId=[" + userId + "], payload size=[" + payload.length + "].");
        if (payload.length == 0) {
            android.util.Slog.i(TAG, "CDM backup payload was empty.");
            return;
        }
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(payload);
        int version = buffer.getInt();
        if (version != 0) {
            android.util.Slog.e(TAG, "Unsupported backup payload version");
            return;
        }
        try {
            byte[] associationsPayload = new byte[buffer.getInt()];
            buffer.get(associationsPayload);
            byte[] requestsPayload = new byte[buffer.getInt()];
            buffer.get(requestsPayload);
            com.android.server.companion.association.Associations restoredAssociations = com.android.server.companion.association.AssociationDiskStore.readAssociationsFromPayload(associationsPayload, userId);
            java.util.List<android.companion.datatransfer.SystemDataTransferRequest> restoredRequestsForUser = this.mSystemDataTransferRequestStore.readRequestsFromPayload(requestsPayload, userId);
            java.util.List<android.content.pm.ApplicationInfo> installedApps = this.mPackageManagerInternal.getInstalledApplications(0L, userId, android.os.UserHandle.getCallingUserId());
            for (final android.companion.AssociationInfo restored : restoredAssociations.getAssociations()) {
                if (!restored.isRevoked()) {
                    java.util.List<android.companion.datatransfer.SystemDataTransferRequest> restoredRequests = com.android.internal.util.CollectionUtils.filter(restoredRequestsForUser, new java.util.function.Predicate() { // from class: com.android.server.companion.BackupRestoreProcessor$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.companion.BackupRestoreProcessor.lambda$applyRestoredPayload$0(restored, (android.companion.datatransfer.SystemDataTransferRequest) obj);
                        }
                    });
                    if (!handleCollision(userId, restored, restoredRequests)) {
                        final java.lang.String packageName = restored.getPackageName();
                        int newId = this.mAssociationStore.getNextId();
                        android.companion.AssociationInfo newAssociation = new android.companion.AssociationInfo.Builder(newId, userId, packageName, restored).build();
                        byte[] associationsPayload2 = associationsPayload;
                        boolean isPackageInstalled = installedApps.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.companion.BackupRestoreProcessor$$ExternalSyntheticLambda1
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj) {
                                return packageName.equals(((android.content.pm.ApplicationInfo) obj).packageName);
                            }
                        });
                        if (isPackageInstalled) {
                            this.mAssociationRequestsProcessor.maybeGrantRoleAndStoreAssociation(newAssociation, null, null);
                        } else {
                            this.mAssociationStore.addAssociation(new android.companion.AssociationInfo.Builder(newAssociation).setPending(true).build());
                        }
                        java.util.Iterator<android.companion.datatransfer.SystemDataTransferRequest> it = restoredRequests.iterator();
                        while (it.hasNext()) {
                            android.companion.datatransfer.SystemDataTransferRequest restoredRequest = it.next();
                            java.util.Iterator<android.companion.datatransfer.SystemDataTransferRequest> it2 = it;
                            android.companion.datatransfer.SystemDataTransferRequest newRequest = restoredRequest.copyWithNewId(newId);
                            newRequest.setUserId(userId);
                            this.mSystemDataTransferRequestStore.writeRequest(userId, newRequest);
                            it = it2;
                        }
                        associationsPayload = associationsPayload2;
                    }
                }
            }
        } catch (java.lang.Exception bufferException) {
            android.util.Slog.e(TAG, "CDM backup payload was mal-formatted.", bufferException);
        }
    }

    static /* synthetic */ boolean lambda$applyRestoredPayload$0(android.companion.AssociationInfo restored, android.companion.datatransfer.SystemDataTransferRequest it) {
        return it.getAssociationId() == restored.getId();
    }

    public void restorePendingAssociations(int userId, java.lang.String packageName) {
        java.util.List<android.companion.AssociationInfo> pendingAssociations = this.mAssociationStore.getPendingAssociations(userId, packageName);
        if (!pendingAssociations.isEmpty()) {
            android.util.Slog.i(TAG, "Found pending associations for package=[" + packageName + "]. Restoring...");
        }
        for (final android.companion.AssociationInfo association : pendingAssociations) {
            final android.companion.AssociationInfo newAssociation = new android.companion.AssociationInfo.Builder(association).setPending(false).build();
            com.android.server.companion.utils.RolesUtils.addRoleHolderForAssociation(this.mContext, newAssociation, new java.util.function.Consumer() { // from class: com.android.server.companion.BackupRestoreProcessor$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$restorePendingAssociations$2(newAssociation, association, (java.lang.Boolean) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restorePendingAssociations$2(android.companion.AssociationInfo newAssociation, android.companion.AssociationInfo association, java.lang.Boolean success) {
        if (success.booleanValue()) {
            this.mAssociationStore.updateAssociation(newAssociation);
            android.util.Slog.i(TAG, "Association=[" + association + "] is restored.");
        } else {
            android.util.Slog.e(TAG, "Failed to restore association=[" + association + "].");
        }
    }

    private boolean handleCollision(int userId, final android.companion.AssociationInfo restored, java.util.List<android.companion.datatransfer.SystemDataTransferRequest> restoredRequests) {
        java.util.List<android.companion.AssociationInfo> localAssociations = this.mAssociationStore.getActiveAssociationsByPackage(restored.getUserId(), restored.getPackageName());
        java.util.function.Predicate<android.companion.AssociationInfo> isSameDevice = new java.util.function.Predicate() { // from class: com.android.server.companion.BackupRestoreProcessor$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.companion.BackupRestoreProcessor.lambda$handleCollision$3(restored, (android.companion.AssociationInfo) obj);
            }
        };
        android.companion.AssociationInfo local = (android.companion.AssociationInfo) com.android.internal.util.CollectionUtils.find(localAssociations, isSameDevice);
        if (local == null) {
            return false;
        }
        android.util.Slog.d(TAG, "Conflict detected with association id=" + local.getId() + " while restoring CDM backup. Keeping local association.");
        java.util.List<android.companion.datatransfer.SystemDataTransferRequest> localRequests = this.mSystemDataTransferRequestStore.readRequestsByAssociationId(userId, local.getId());
        for (final android.companion.datatransfer.SystemDataTransferRequest restoredRequest : restoredRequests) {
            boolean requestTypeExists = com.android.internal.util.CollectionUtils.any(localRequests, new java.util.function.Predicate() { // from class: com.android.server.companion.BackupRestoreProcessor$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.BackupRestoreProcessor.lambda$handleCollision$4(restoredRequest, (android.companion.datatransfer.SystemDataTransferRequest) obj);
                }
            });
            if (!requestTypeExists) {
                android.util.Slog.d(TAG, "Restoring " + restoredRequest.getClass().getSimpleName() + " to an existing association id=[" + local.getId() + "].");
                android.companion.datatransfer.SystemDataTransferRequest newRequest = restoredRequest.copyWithNewId(local.getId());
                newRequest.setUserId(userId);
                this.mSystemDataTransferRequestStore.writeRequest(userId, newRequest);
            }
        }
        return true;
    }

    static /* synthetic */ boolean lambda$handleCollision$3(android.companion.AssociationInfo restored, android.companion.AssociationInfo associationInfo) {
        boolean matchesMacAddress = java.util.Objects.equals(associationInfo.getDeviceMacAddress(), restored.getDeviceMacAddress());
        boolean matchesTag = !com.android.internal.hidden_from_bootclasspath.android.companion.Flags.associationTag() || java.util.Objects.equals(associationInfo.getTag(), restored.getTag());
        return matchesMacAddress && matchesTag;
    }

    static /* synthetic */ boolean lambda$handleCollision$4(android.companion.datatransfer.SystemDataTransferRequest restoredRequest, android.companion.datatransfer.SystemDataTransferRequest request) {
        return request.getDataType() == restoredRequest.getDataType();
    }
}
