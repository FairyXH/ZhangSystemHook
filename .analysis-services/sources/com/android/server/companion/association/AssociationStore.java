package com.android.server.companion.association;

/* JADX INFO: loaded from: classes.dex */
public class AssociationStore {
    public static final int CHANGE_TYPE_ADDED = 0;
    public static final int CHANGE_TYPE_REMOVED = 1;
    public static final int CHANGE_TYPE_UPDATED_ADDRESS_CHANGED = 2;
    public static final int CHANGE_TYPE_UPDATED_ADDRESS_UNCHANGED = 3;
    private static final java.lang.String TAG = "CDM_AssociationStore";
    private final android.content.Context mContext;
    private final com.android.server.companion.association.AssociationDiskStore mDiskStore;
    private final android.os.UserManager mUserManager;
    private final java.lang.Object mLock = new java.lang.Object();
    private boolean mPersisted = false;
    private final java.util.Map<java.lang.Integer, android.companion.AssociationInfo> mIdToAssociationMap = new java.util.HashMap();
    private int mMaxId = 0;
    private final java.util.Set<com.android.server.companion.association.AssociationStore.OnChangeListener> mLocalListeners = new java.util.LinkedHashSet();
    private final android.os.RemoteCallbackList<android.companion.IOnAssociationsChangedListener> mRemoteListeners = new android.os.RemoteCallbackList<>();
    private final java.util.concurrent.ExecutorService mExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ChangeType {
    }

    public interface OnChangeListener {
        default void onAssociationChanged(int changeType, android.companion.AssociationInfo association) {
            switch (changeType) {
                case 0:
                    onAssociationAdded(association);
                    break;
                case 1:
                    onAssociationRemoved(association);
                    break;
                case 2:
                    onAssociationUpdated(association, true);
                    break;
                case 3:
                    onAssociationUpdated(association, false);
                    break;
            }
        }

        default void onAssociationAdded(android.companion.AssociationInfo association) {
        }

        default void onAssociationRemoved(android.companion.AssociationInfo association) {
        }

        default void onAssociationUpdated(android.companion.AssociationInfo association, boolean addressChanged) {
        }
    }

    public AssociationStore(android.content.Context context, android.os.UserManager userManager, com.android.server.companion.association.AssociationDiskStore diskStore) {
        this.mContext = context;
        this.mUserManager = userManager;
        this.mDiskStore = diskStore;
    }

    public void refreshCache() {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda3
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$refreshCache$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshCache$0() throws java.lang.Exception {
        java.util.List<java.lang.Integer> userIds = new java.util.ArrayList<>();
        for (android.content.pm.UserInfo user : this.mUserManager.getAliveUsers()) {
            userIds.add(java.lang.Integer.valueOf(user.id));
        }
        synchronized (this.mLock) {
            this.mPersisted = false;
            this.mIdToAssociationMap.clear();
            this.mMaxId = 0;
            java.util.Map<java.lang.Integer, com.android.server.companion.association.Associations> userToAssociationsMap = this.mDiskStore.readAssociationsByUsers(userIds);
            for (java.util.Map.Entry<java.lang.Integer, com.android.server.companion.association.Associations> entry : userToAssociationsMap.entrySet()) {
                for (android.companion.AssociationInfo association : entry.getValue().getAssociations()) {
                    this.mIdToAssociationMap.put(java.lang.Integer.valueOf(association.getId()), association);
                }
                this.mMaxId = java.lang.Math.max(this.mMaxId, entry.getValue().getMaxId());
            }
            this.mPersisted = true;
        }
    }

    public int getMaxId() {
        int i;
        synchronized (this.mLock) {
            i = this.mMaxId;
        }
        return i;
    }

    public int getNextId() {
        int maxId;
        synchronized (this.mLock) {
            maxId = getMaxId() + 1;
        }
        return maxId;
    }

    public void addAssociation(android.companion.AssociationInfo association) {
        android.util.Slog.i(TAG, "Adding new association=[" + association + "]...");
        int id = association.getId();
        int userId = association.getUserId();
        synchronized (this.mLock) {
            if (this.mIdToAssociationMap.containsKey(java.lang.Integer.valueOf(id))) {
                android.util.Slog.e(TAG, "Association id=[" + id + "] already exists.");
                return;
            }
            this.mIdToAssociationMap.put(java.lang.Integer.valueOf(id), association);
            this.mMaxId = java.lang.Math.max(this.mMaxId, id);
            writeCacheToDisk(userId);
            android.util.Slog.i(TAG, "Done adding new association.");
            com.android.server.companion.utils.MetricUtils.logCreateAssociation(association.getDeviceProfile());
            if (association.isActive()) {
                broadcastChange(0, association);
            }
        }
    }

    public void updateAssociation(android.companion.AssociationInfo updated) {
        android.util.Slog.i(TAG, "Updating new association=[" + updated + "]...");
        int id = updated.getId();
        synchronized (this.mLock) {
            android.companion.AssociationInfo current = this.mIdToAssociationMap.get(java.lang.Integer.valueOf(id));
            if (current == null) {
                android.util.Slog.w(TAG, "Can't update association id=[" + id + "]. It does not exist.");
                return;
            }
            if (current.equals(updated)) {
                android.util.Slog.w(TAG, "Association is the same.");
                return;
            }
            this.mIdToAssociationMap.put(java.lang.Integer.valueOf(id), updated);
            writeCacheToDisk(updated.getUserId());
            android.util.Slog.i(TAG, "Done updating association.");
            if (current.isActive() && !updated.isActive()) {
                broadcastChange(1, updated);
            } else if (updated.isActive()) {
                android.net.MacAddress updatedAddress = updated.getDeviceMacAddress();
                android.net.MacAddress currentAddress = current.getDeviceMacAddress();
                boolean macAddressChanged = true ^ java.util.Objects.equals(currentAddress, updatedAddress);
                broadcastChange(macAddressChanged ? 2 : 3, updated);
            }
        }
    }

    public void removeAssociation(int id) {
        android.util.Slog.i(TAG, "Removing association id=[" + id + "]...");
        synchronized (this.mLock) {
            android.companion.AssociationInfo association = this.mIdToAssociationMap.remove(java.lang.Integer.valueOf(id));
            if (association == null) {
                android.util.Slog.w(TAG, "Can't remove association id=[" + id + "]. It does not exist.");
                return;
            }
            writeCacheToDisk(association.getUserId());
            android.util.Slog.i(TAG, "Done removing association.");
            com.android.server.companion.utils.MetricUtils.logRemoveAssociation(association.getDeviceProfile());
            if (association.isActive()) {
                broadcastChange(1, association);
            }
        }
    }

    private void writeCacheToDisk(final int userId) {
        this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$writeCacheToDisk$2(userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeCacheToDisk$2(final int userId) {
        com.android.server.companion.association.Associations associations = new com.android.server.companion.association.Associations();
        synchronized (this.mLock) {
            associations.setMaxId(this.mMaxId);
            associations.setAssociations(com.android.internal.util.CollectionUtils.filter(this.mIdToAssociationMap.values().stream().toList(), new java.util.function.Predicate() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda10
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.association.AssociationStore.lambda$writeCacheToDisk$1(userId, (android.companion.AssociationInfo) obj);
                }
            }));
        }
        this.mDiskStore.writeAssociationsForUser(userId, associations);
    }

    static /* synthetic */ boolean lambda$writeCacheToDisk$1(int userId, android.companion.AssociationInfo a) {
        return a.getUserId() == userId;
    }

    public java.util.List<android.companion.AssociationInfo> getAssociations() {
        java.util.List<android.companion.AssociationInfo> listCopyOf;
        synchronized (this.mLock) {
            if (!this.mPersisted) {
                refreshCache();
            }
            listCopyOf = java.util.List.copyOf(this.mIdToAssociationMap.values());
        }
        return listCopyOf;
    }

    public java.util.List<android.companion.AssociationInfo> getActiveAssociations() {
        java.util.List<android.companion.AssociationInfo> listFilter;
        synchronized (this.mLock) {
            listFilter = com.android.internal.util.CollectionUtils.filter(getAssociations(), new java.util.function.Predicate() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda7
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((android.companion.AssociationInfo) obj).isActive();
                }
            });
        }
        return listFilter;
    }

    public java.util.List<android.companion.AssociationInfo> getAssociationsByUser(final int userId) {
        java.util.List<android.companion.AssociationInfo> listFilter;
        synchronized (this.mLock) {
            listFilter = com.android.internal.util.CollectionUtils.filter(getAssociations(), new java.util.function.Predicate() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda9
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.association.AssociationStore.lambda$getAssociationsByUser$3(userId, (android.companion.AssociationInfo) obj);
                }
            });
        }
        return listFilter;
    }

    static /* synthetic */ boolean lambda$getAssociationsByUser$3(int userId, android.companion.AssociationInfo a) {
        return a.getUserId() == userId;
    }

    public java.util.List<android.companion.AssociationInfo> getActiveAssociationsByUser(final int userId) {
        java.util.List<android.companion.AssociationInfo> listFilter;
        synchronized (this.mLock) {
            listFilter = com.android.internal.util.CollectionUtils.filter(getActiveAssociations(), new java.util.function.Predicate() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda8
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.association.AssociationStore.lambda$getActiveAssociationsByUser$4(userId, (android.companion.AssociationInfo) obj);
                }
            });
        }
        return listFilter;
    }

    static /* synthetic */ boolean lambda$getActiveAssociationsByUser$4(int userId, android.companion.AssociationInfo a) {
        return a.getUserId() == userId;
    }

    public java.util.List<android.companion.AssociationInfo> getAssociationsByPackage(int userId, final java.lang.String packageName) {
        java.util.List<android.companion.AssociationInfo> listFilter;
        synchronized (this.mLock) {
            listFilter = com.android.internal.util.CollectionUtils.filter(getAssociationsByUser(userId), new java.util.function.Predicate() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda11
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((android.companion.AssociationInfo) obj).getPackageName().equals(packageName);
                }
            });
        }
        return listFilter;
    }

    public java.util.List<android.companion.AssociationInfo> getActiveAssociationsByPackage(int userId, final java.lang.String packageName) {
        java.util.List<android.companion.AssociationInfo> listFilter;
        synchronized (this.mLock) {
            listFilter = com.android.internal.util.CollectionUtils.filter(getActiveAssociationsByUser(userId), new java.util.function.Predicate() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((android.companion.AssociationInfo) obj).getPackageName().equals(packageName);
                }
            });
        }
        return listFilter;
    }

    public android.companion.AssociationInfo getFirstAssociationByAddress(int userId, java.lang.String packageName, final java.lang.String macAddress) {
        android.companion.AssociationInfo associationInfo;
        synchronized (this.mLock) {
            associationInfo = (android.companion.AssociationInfo) com.android.internal.util.CollectionUtils.find(getActiveAssociationsByPackage(userId, packageName), new java.util.function.Predicate() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.association.AssociationStore.lambda$getFirstAssociationByAddress$7(macAddress, (android.companion.AssociationInfo) obj);
                }
            });
        }
        return associationInfo;
    }

    static /* synthetic */ boolean lambda$getFirstAssociationByAddress$7(java.lang.String macAddress, android.companion.AssociationInfo a) {
        return a.getDeviceMacAddress() != null && a.getDeviceMacAddress().equals(android.net.MacAddress.fromString(macAddress));
    }

    public android.companion.AssociationInfo getAssociationById(int id) {
        android.companion.AssociationInfo associationInfo;
        synchronized (this.mLock) {
            associationInfo = this.mIdToAssociationMap.get(java.lang.Integer.valueOf(id));
        }
        return associationInfo;
    }

    public java.util.List<android.companion.AssociationInfo> getActiveAssociationsByAddress(final java.lang.String macAddress) {
        java.util.List<android.companion.AssociationInfo> listFilter;
        synchronized (this.mLock) {
            listFilter = com.android.internal.util.CollectionUtils.filter(getActiveAssociations(), new java.util.function.Predicate() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.association.AssociationStore.lambda$getActiveAssociationsByAddress$8(macAddress, (android.companion.AssociationInfo) obj);
                }
            });
        }
        return listFilter;
    }

    static /* synthetic */ boolean lambda$getActiveAssociationsByAddress$8(java.lang.String macAddress, android.companion.AssociationInfo a) {
        return a.getDeviceMacAddress() != null && a.getDeviceMacAddress().equals(android.net.MacAddress.fromString(macAddress));
    }

    public java.util.List<android.companion.AssociationInfo> getRevokedAssociations() {
        java.util.List<android.companion.AssociationInfo> listFilter;
        synchronized (this.mLock) {
            listFilter = com.android.internal.util.CollectionUtils.filter(getAssociations(), new java.util.function.Predicate() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda13
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((android.companion.AssociationInfo) obj).isRevoked();
                }
            });
        }
        return listFilter;
    }

    public java.util.List<android.companion.AssociationInfo> getRevokedAssociations(final int userId, final java.lang.String packageName) {
        java.util.List<android.companion.AssociationInfo> listFilter;
        synchronized (this.mLock) {
            listFilter = com.android.internal.util.CollectionUtils.filter(getAssociations(), new java.util.function.Predicate() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.association.AssociationStore.lambda$getRevokedAssociations$9(packageName, userId, (android.companion.AssociationInfo) obj);
                }
            });
        }
        return listFilter;
    }

    static /* synthetic */ boolean lambda$getRevokedAssociations$9(java.lang.String packageName, int userId, android.companion.AssociationInfo a) {
        return packageName.equals(a.getPackageName()) && a.getUserId() == userId && a.isRevoked();
    }

    public java.util.List<android.companion.AssociationInfo> getPendingAssociations(final int userId, final java.lang.String packageName) {
        java.util.List<android.companion.AssociationInfo> listFilter;
        synchronized (this.mLock) {
            listFilter = com.android.internal.util.CollectionUtils.filter(getAssociations(), new java.util.function.Predicate() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.association.AssociationStore.lambda$getPendingAssociations$10(packageName, userId, (android.companion.AssociationInfo) obj);
                }
            });
        }
        return listFilter;
    }

    static /* synthetic */ boolean lambda$getPendingAssociations$10(java.lang.String packageName, int userId, android.companion.AssociationInfo a) {
        return packageName.equals(a.getPackageName()) && a.getUserId() == userId && a.isPending();
    }

    public android.companion.AssociationInfo getAssociationWithCallerChecks(int associationId) {
        android.companion.AssociationInfo association = getAssociationById(associationId);
        if (association == null) {
            throw new java.lang.IllegalArgumentException("getAssociationWithCallerChecks() Association id=[" + associationId + "] doesn't exist.");
        }
        com.android.server.companion.utils.PermissionsUtils.enforceCallerCanManageAssociationsForPackage(this.mContext, association.getUserId(), association.getPackageName(), null);
        return association;
    }

    public void registerLocalListener(com.android.server.companion.association.AssociationStore.OnChangeListener listener) {
        synchronized (this.mLocalListeners) {
            this.mLocalListeners.add(listener);
        }
    }

    public void unregisterLocalListener(com.android.server.companion.association.AssociationStore.OnChangeListener listener) {
        synchronized (this.mLocalListeners) {
            this.mLocalListeners.remove(listener);
        }
    }

    public void registerRemoteListener(android.companion.IOnAssociationsChangedListener listener, int userId) {
        synchronized (this.mRemoteListeners) {
            this.mRemoteListeners.register(listener, java.lang.Integer.valueOf(userId));
        }
    }

    public void unregisterRemoteListener(android.companion.IOnAssociationsChangedListener listener) {
        synchronized (this.mRemoteListeners) {
            this.mRemoteListeners.unregister(listener);
        }
    }

    public void dump(java.io.PrintWriter out) {
        out.append("Companion Device Associations: ");
        if (getActiveAssociations().isEmpty()) {
            out.append("<empty>\n");
            return;
        }
        out.append("\n");
        for (android.companion.AssociationInfo a : getActiveAssociations()) {
            out.append("  ").append((java.lang.CharSequence) a.toString()).append('\n');
        }
    }

    private void broadcastChange(int changeType, android.companion.AssociationInfo association) {
        android.util.Slog.i(TAG, "Broadcasting association changes - changeType=[" + changeType + "]...");
        synchronized (this.mLocalListeners) {
            for (com.android.server.companion.association.AssociationStore.OnChangeListener listener : this.mLocalListeners) {
                listener.onAssociationChanged(changeType, association);
            }
        }
        synchronized (this.mRemoteListeners) {
            final int userId = association.getUserId();
            final java.util.List<android.companion.AssociationInfo> updatedAssociations = getActiveAssociationsByUser(userId);
            if (changeType != 3) {
                this.mRemoteListeners.broadcast(new java.util.function.BiConsumer() { // from class: com.android.server.companion.association.AssociationStore$$ExternalSyntheticLambda12
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        com.android.server.companion.association.AssociationStore.lambda$broadcastChange$11(userId, updatedAssociations, (android.companion.IOnAssociationsChangedListener) obj, obj2);
                    }
                });
            }
        }
    }

    static /* synthetic */ void lambda$broadcastChange$11(int userId, java.util.List updatedAssociations, android.companion.IOnAssociationsChangedListener listener, java.lang.Object callbackUserId) {
        int listenerUserId = ((java.lang.Integer) callbackUserId).intValue();
        if (listenerUserId == userId || listenerUserId == -1) {
            try {
                listener.onAssociationsChanged(updatedAssociations);
            } catch (android.os.RemoteException e) {
            }
        }
    }
}
