package com.android.server.blob;

/* JADX INFO: loaded from: classes.dex */
class BlobMetadata {
    private java.io.File mBlobFile;
    private final android.app.blob.BlobHandle mBlobHandle;
    private final long mBlobId;
    private final android.content.Context mContext;
    private final java.lang.Object mMetadataLock = new java.lang.Object();
    private final android.util.ArraySet<com.android.server.blob.BlobMetadata.Committer> mCommitters = new android.util.ArraySet<>();
    private final android.util.ArraySet<com.android.server.blob.BlobMetadata.Leasee> mLeasees = new android.util.ArraySet<>();
    private final android.util.ArrayMap<com.android.server.blob.BlobMetadata.Accessor, android.util.ArraySet<android.os.RevocableFileDescriptor>> mRevocableFds = new android.util.ArrayMap<>();

    BlobMetadata(android.content.Context context, long blobId, android.app.blob.BlobHandle blobHandle) {
        this.mContext = context;
        this.mBlobId = blobId;
        this.mBlobHandle = blobHandle;
    }

    long getBlobId() {
        return this.mBlobId;
    }

    android.app.blob.BlobHandle getBlobHandle() {
        return this.mBlobHandle;
    }

    void addOrReplaceCommitter(com.android.server.blob.BlobMetadata.Committer committer) {
        synchronized (this.mMetadataLock) {
            this.mCommitters.remove(committer);
            this.mCommitters.add(committer);
        }
    }

    void setCommitters(android.util.ArraySet<com.android.server.blob.BlobMetadata.Committer> committers) {
        synchronized (this.mMetadataLock) {
            this.mCommitters.clear();
            this.mCommitters.addAll((android.util.ArraySet<? extends com.android.server.blob.BlobMetadata.Committer>) committers);
        }
    }

    void removeCommitter(final java.lang.String packageName, final int uid) {
        synchronized (this.mMetadataLock) {
            this.mCommitters.removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobMetadata$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.blob.BlobMetadata.lambda$removeCommitter$0(uid, packageName, (com.android.server.blob.BlobMetadata.Committer) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$removeCommitter$0(int uid, java.lang.String packageName, com.android.server.blob.BlobMetadata.Committer committer) {
        return committer.uid == uid && committer.packageName.equals(packageName);
    }

    void removeCommitter(com.android.server.blob.BlobMetadata.Committer committer) {
        synchronized (this.mMetadataLock) {
            this.mCommitters.remove(committer);
        }
    }

    void removeCommittersFromUnknownPkgs(final android.util.SparseArray<android.util.SparseArray<java.lang.String>> knownPackages) {
        synchronized (this.mMetadataLock) {
            this.mCommitters.removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobMetadata$$ExternalSyntheticLambda8
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.blob.BlobMetadata.lambda$removeCommittersFromUnknownPkgs$1(knownPackages, (com.android.server.blob.BlobMetadata.Committer) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$removeCommittersFromUnknownPkgs$1(android.util.SparseArray knownPackages, com.android.server.blob.BlobMetadata.Committer committer) {
        int userId = android.os.UserHandle.getUserId(committer.uid);
        android.util.SparseArray<java.lang.String> userPackages = (android.util.SparseArray) knownPackages.get(userId);
        if (userPackages == null) {
            return true;
        }
        return true ^ committer.packageName.equals(userPackages.get(committer.uid));
    }

    void addCommittersAndLeasees(com.android.server.blob.BlobMetadata blobMetadata) {
        this.mCommitters.addAll((android.util.ArraySet<? extends com.android.server.blob.BlobMetadata.Committer>) blobMetadata.mCommitters);
        this.mLeasees.addAll((android.util.ArraySet<? extends com.android.server.blob.BlobMetadata.Leasee>) blobMetadata.mLeasees);
    }

    com.android.server.blob.BlobMetadata.Committer getExistingCommitter(java.lang.String packageName, int uid) {
        synchronized (this.mCommitters) {
            int size = this.mCommitters.size();
            for (int i = 0; i < size; i++) {
                com.android.server.blob.BlobMetadata.Committer committer = this.mCommitters.valueAt(i);
                if (committer.uid == uid && committer.packageName.equals(packageName)) {
                    return committer;
                }
            }
            return null;
        }
    }

    void addOrReplaceLeasee(java.lang.String callingPackage, int callingUid, int descriptionResId, java.lang.CharSequence description, long leaseExpiryTimeMillis) {
        synchronized (this.mMetadataLock) {
            com.android.server.blob.BlobMetadata.Leasee leasee = new com.android.server.blob.BlobMetadata.Leasee(this.mContext, callingPackage, callingUid, descriptionResId, description, leaseExpiryTimeMillis);
            this.mLeasees.remove(leasee);
            this.mLeasees.add(leasee);
        }
    }

    void setLeasees(android.util.ArraySet<com.android.server.blob.BlobMetadata.Leasee> leasees) {
        synchronized (this.mMetadataLock) {
            this.mLeasees.clear();
            this.mLeasees.addAll((android.util.ArraySet<? extends com.android.server.blob.BlobMetadata.Leasee>) leasees);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeLeasee(final java.lang.String packageName, final int uid) {
        synchronized (this.mMetadataLock) {
            this.mLeasees.removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobMetadata$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.blob.BlobMetadata.lambda$removeLeasee$2(uid, packageName, (com.android.server.blob.BlobMetadata.Leasee) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$removeLeasee$2(int uid, java.lang.String packageName, com.android.server.blob.BlobMetadata.Leasee leasee) {
        return leasee.uid == uid && leasee.packageName.equals(packageName);
    }

    void removeLeaseesFromUnknownPkgs(final android.util.SparseArray<android.util.SparseArray<java.lang.String>> knownPackages) {
        synchronized (this.mMetadataLock) {
            this.mLeasees.removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobMetadata$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.blob.BlobMetadata.lambda$removeLeaseesFromUnknownPkgs$3(knownPackages, (com.android.server.blob.BlobMetadata.Leasee) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$removeLeaseesFromUnknownPkgs$3(android.util.SparseArray knownPackages, com.android.server.blob.BlobMetadata.Leasee leasee) {
        int userId = android.os.UserHandle.getUserId(leasee.uid);
        android.util.SparseArray<java.lang.String> userPackages = (android.util.SparseArray) knownPackages.get(userId);
        if (userPackages == null) {
            return true;
        }
        return true ^ leasee.packageName.equals(userPackages.get(leasee.uid));
    }

    void removeExpiredLeases() {
        synchronized (this.mMetadataLock) {
            this.mLeasees.removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobMetadata$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.blob.BlobMetadata.lambda$removeExpiredLeases$4((com.android.server.blob.BlobMetadata.Leasee) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$removeExpiredLeases$4(com.android.server.blob.BlobMetadata.Leasee leasee) {
        return !leasee.isStillValid();
    }

    void removeDataForUser(final int userId) {
        synchronized (this.mMetadataLock) {
            this.mCommitters.removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobMetadata$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.blob.BlobMetadata.lambda$removeDataForUser$5(userId, (com.android.server.blob.BlobMetadata.Committer) obj);
                }
            });
            this.mLeasees.removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobMetadata$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.blob.BlobMetadata.lambda$removeDataForUser$6(userId, (com.android.server.blob.BlobMetadata.Leasee) obj);
                }
            });
            this.mRevocableFds.entrySet().removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobMetadata$$ExternalSyntheticLambda6
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.blob.BlobMetadata.lambda$removeDataForUser$7(userId, (java.util.Map.Entry) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$removeDataForUser$5(int userId, com.android.server.blob.BlobMetadata.Committer committer) {
        return userId == android.os.UserHandle.getUserId(committer.uid);
    }

    static /* synthetic */ boolean lambda$removeDataForUser$6(int userId, com.android.server.blob.BlobMetadata.Leasee leasee) {
        return userId == android.os.UserHandle.getUserId(leasee.uid);
    }

    static /* synthetic */ boolean lambda$removeDataForUser$7(int userId, java.util.Map.Entry entry) {
        com.android.server.blob.BlobMetadata.Accessor accessor = (com.android.server.blob.BlobMetadata.Accessor) entry.getKey();
        android.util.ArraySet<android.os.RevocableFileDescriptor> rFds = (android.util.ArraySet) entry.getValue();
        if (userId != android.os.UserHandle.getUserId(accessor.uid)) {
            return false;
        }
        int fdCount = rFds.size();
        for (int i = 0; i < fdCount; i++) {
            rFds.valueAt(i).revoke();
        }
        rFds.clear();
        return true;
    }

    boolean hasValidLeases() {
        synchronized (this.mMetadataLock) {
            int size = this.mLeasees.size();
            for (int i = 0; i < size; i++) {
                if (this.mLeasees.valueAt(i).isStillValid()) {
                    return true;
                }
            }
            return false;
        }
    }

    long getSize() {
        return getBlobFile().length();
    }

    boolean isAccessAllowedForCaller(java.lang.String callingPackage, int callingUid) {
        if (getBlobHandle().isExpired()) {
            return false;
        }
        synchronized (this.mMetadataLock) {
            int size = this.mLeasees.size();
            for (int i = 0; i < size; i++) {
                com.android.server.blob.BlobMetadata.Leasee leasee = this.mLeasees.valueAt(i);
                if (leasee.isStillValid() && leasee.equals(callingPackage, callingUid)) {
                    return true;
                }
            }
            int callingUserId = android.os.UserHandle.getUserId(callingUid);
            int size2 = this.mCommitters.size();
            for (int i2 = 0; i2 < size2; i2++) {
                com.android.server.blob.BlobMetadata.Committer committer = this.mCommitters.valueAt(i2);
                if (callingUserId == android.os.UserHandle.getUserId(committer.uid)) {
                    if (committer.equals(callingPackage, callingUid)) {
                        return true;
                    }
                    if (committer.blobAccessMode.isAccessAllowedForCaller(this.mContext, callingPackage, callingUid, committer.uid)) {
                        return true;
                    }
                }
            }
            boolean canCallerAccessBlobsAcrossUsers = checkCallerCanAccessBlobsAcrossUsers(callingUid);
            if (!canCallerAccessBlobsAcrossUsers) {
                return false;
            }
            int size3 = this.mCommitters.size();
            for (int i3 = 0; i3 < size3; i3++) {
                com.android.server.blob.BlobMetadata.Committer committer2 = this.mCommitters.valueAt(i3);
                int committerUserId = android.os.UserHandle.getUserId(committer2.uid);
                if (callingUserId != committerUserId && isPackageInstalledOnUser(callingPackage, committerUserId) && committer2.blobAccessMode.isAccessAllowedForCaller(this.mContext, callingPackage, callingUid, committer2.uid)) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean checkCallerCanAccessBlobsAcrossUsers(int callingUid) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return this.mContext.checkPermission("android.permission.ACCESS_BLOBS_ACROSS_USERS", -1, callingUid) == 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private boolean isPackageInstalledOnUser(java.lang.String packageName, int userId) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mContext.getPackageManager().getPackageInfoAsUser(packageName, 0, userId);
            android.os.Binder.restoreCallingIdentity(token);
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.os.Binder.restoreCallingIdentity(token);
            return false;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    boolean hasACommitterOrLeaseeInUser(int userId) {
        return hasACommitterInUser(userId) || hasALeaseeInUser(userId);
    }

    boolean hasACommitterInUser(int userId) {
        synchronized (this.mMetadataLock) {
            int size = this.mCommitters.size();
            for (int i = 0; i < size; i++) {
                com.android.server.blob.BlobMetadata.Committer committer = this.mCommitters.valueAt(i);
                if (userId == android.os.UserHandle.getUserId(committer.uid)) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean hasALeaseeInUser(int userId) {
        synchronized (this.mMetadataLock) {
            int size = this.mLeasees.size();
            for (int i = 0; i < size; i++) {
                com.android.server.blob.BlobMetadata.Leasee leasee = this.mLeasees.valueAt(i);
                if (userId == android.os.UserHandle.getUserId(leasee.uid)) {
                    return true;
                }
            }
            return false;
        }
    }

    boolean isACommitter(java.lang.String packageName, int uid) {
        boolean zIsAnAccessor;
        synchronized (this.mMetadataLock) {
            zIsAnAccessor = isAnAccessor(this.mCommitters, packageName, uid, android.os.UserHandle.getUserId(uid));
        }
        return zIsAnAccessor;
    }

    boolean isALeasee(java.lang.String packageName, int uid) {
        boolean z;
        synchronized (this.mMetadataLock) {
            com.android.server.blob.BlobMetadata.Leasee leasee = (com.android.server.blob.BlobMetadata.Leasee) getAccessor(this.mLeasees, packageName, uid, android.os.UserHandle.getUserId(uid));
            z = leasee != null && leasee.isStillValid();
        }
        return z;
    }

    private boolean isALeaseeInUser(java.lang.String packageName, int uid, int userId) {
        boolean z;
        synchronized (this.mMetadataLock) {
            com.android.server.blob.BlobMetadata.Leasee leasee = (com.android.server.blob.BlobMetadata.Leasee) getAccessor(this.mLeasees, packageName, uid, userId);
            z = leasee != null && leasee.isStillValid();
        }
        return z;
    }

    private static <T extends com.android.server.blob.BlobMetadata.Accessor> boolean isAnAccessor(android.util.ArraySet<T> accessors, java.lang.String packageName, int uid, int userId) {
        return getAccessor(accessors, packageName, uid, userId) != null;
    }

    private static <T extends com.android.server.blob.BlobMetadata.Accessor> T getAccessor(android.util.ArraySet<T> accessors, java.lang.String packageName, int uid, int userId) {
        int size = accessors.size();
        for (int i = 0; i < size; i++) {
            T tValueAt = accessors.valueAt(i);
            if (packageName != null && uid != -1 && tValueAt.equals(packageName, uid)) {
                return tValueAt;
            }
            if (packageName != null && tValueAt.packageName.equals(packageName) && userId == android.os.UserHandle.getUserId(tValueAt.uid)) {
                return tValueAt;
            }
            if (uid != -1 && tValueAt.uid == uid) {
                return tValueAt;
            }
        }
        return null;
    }

    boolean shouldAttributeToUser(int userId) {
        synchronized (this.mMetadataLock) {
            int size = this.mLeasees.size();
            for (int i = 0; i < size; i++) {
                com.android.server.blob.BlobMetadata.Leasee leasee = this.mLeasees.valueAt(i);
                if (userId != android.os.UserHandle.getUserId(leasee.uid)) {
                    return false;
                }
            }
            return true;
        }
    }

    boolean shouldAttributeToLeasee(java.lang.String packageName, int userId, boolean callerHasStatsPermission) {
        if (isALeaseeInUser(packageName, -1, userId)) {
            return (callerHasStatsPermission && hasOtherLeasees(packageName, -1, userId)) ? false : true;
        }
        return false;
    }

    boolean shouldAttributeToLeasee(int uid, boolean callerHasStatsPermission) {
        int userId = android.os.UserHandle.getUserId(uid);
        if (isALeaseeInUser(null, uid, userId)) {
            return (callerHasStatsPermission && hasOtherLeasees(null, uid, userId)) ? false : true;
        }
        return false;
    }

    private boolean hasOtherLeasees(java.lang.String packageName, int uid, int userId) {
        synchronized (this.mMetadataLock) {
            int size = this.mLeasees.size();
            for (int i = 0; i < size; i++) {
                com.android.server.blob.BlobMetadata.Leasee leasee = this.mLeasees.valueAt(i);
                if (leasee.isStillValid()) {
                    if (packageName != null && uid != -1 && !leasee.equals(packageName, uid)) {
                        return true;
                    }
                    if (packageName != null && (!leasee.packageName.equals(packageName) || userId != android.os.UserHandle.getUserId(leasee.uid))) {
                        return true;
                    }
                    if (uid != -1 && leasee.uid != uid) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    android.app.blob.LeaseInfo getLeaseInfo(java.lang.String packageName, int uid) {
        int descriptionResId;
        synchronized (this.mMetadataLock) {
            int size = this.mLeasees.size();
            for (int i = 0; i < size; i++) {
                com.android.server.blob.BlobMetadata.Leasee leasee = this.mLeasees.valueAt(i);
                if (leasee.isStillValid() && leasee.uid == uid && leasee.packageName.equals(packageName)) {
                    if (leasee.descriptionResEntryName == null) {
                        descriptionResId = 0;
                    } else {
                        descriptionResId = com.android.server.blob.BlobStoreUtils.getDescriptionResourceId(this.mContext, leasee.descriptionResEntryName, leasee.packageName, android.os.UserHandle.getUserId(leasee.uid));
                    }
                    return new android.app.blob.LeaseInfo(packageName, leasee.expiryTimeMillis, descriptionResId, leasee.description);
                }
            }
            return null;
        }
    }

    void forEachLeasee(java.util.function.Consumer<com.android.server.blob.BlobMetadata.Leasee> consumer) {
        synchronized (this.mMetadataLock) {
            this.mLeasees.forEach(consumer);
        }
    }

    java.io.File getBlobFile() {
        if (this.mBlobFile == null) {
            this.mBlobFile = com.android.server.blob.BlobStoreConfig.getBlobFile(this.mBlobId);
        }
        return this.mBlobFile;
    }

    android.os.ParcelFileDescriptor openForRead(java.lang.String callingPackage, int callingUid) throws java.io.IOException {
        try {
            java.io.FileDescriptor fd = android.system.Os.open(getBlobFile().getPath(), android.system.OsConstants.O_RDONLY, 0);
            try {
                if (com.android.server.blob.BlobStoreConfig.shouldUseRevocableFdForReads()) {
                    return createRevocableFd(fd, callingPackage, callingUid);
                }
                return new android.os.ParcelFileDescriptor(fd);
            } catch (java.io.IOException e) {
                libcore.io.IoUtils.closeQuietly(fd);
                throw e;
            }
        } catch (android.system.ErrnoException e2) {
            throw e2.rethrowAsIOException();
        }
    }

    private android.os.ParcelFileDescriptor createRevocableFd(java.io.FileDescriptor fd, java.lang.String callingPackage, int callingUid) throws java.io.IOException {
        final com.android.server.blob.BlobMetadata.Accessor accessor;
        final android.os.RevocableFileDescriptor revocableFd = new android.os.RevocableFileDescriptor(this.mContext, fd, com.android.server.blob.BlobStoreUtils.getRevocableFdHandler());
        synchronized (this.mRevocableFds) {
            accessor = new com.android.server.blob.BlobMetadata.Accessor(callingPackage, callingUid);
            android.util.ArraySet<android.os.RevocableFileDescriptor> revocableFdsForAccessor = this.mRevocableFds.get(accessor);
            if (revocableFdsForAccessor == null) {
                revocableFdsForAccessor = new android.util.ArraySet<>();
                this.mRevocableFds.put(accessor, revocableFdsForAccessor);
            }
            revocableFdsForAccessor.add(revocableFd);
        }
        revocableFd.addOnCloseListener(new android.os.ParcelFileDescriptor.OnCloseListener() { // from class: com.android.server.blob.BlobMetadata$$ExternalSyntheticLambda7
            @Override // android.os.ParcelFileDescriptor.OnCloseListener
            public final void onClose(java.io.IOException iOException) {
                this.f$0.lambda$createRevocableFd$8(accessor, revocableFd, iOException);
            }
        });
        return revocableFd.getRevocableFileDescriptor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createRevocableFd$8(com.android.server.blob.BlobMetadata.Accessor accessor, android.os.RevocableFileDescriptor revocableFd, java.io.IOException e) {
        synchronized (this.mRevocableFds) {
            android.util.ArraySet<android.os.RevocableFileDescriptor> revocableFdsForAccessor = this.mRevocableFds.get(accessor);
            if (revocableFdsForAccessor != null) {
                revocableFdsForAccessor.remove(revocableFd);
                if (revocableFdsForAccessor.isEmpty()) {
                    this.mRevocableFds.remove(accessor);
                }
            }
        }
    }

    void destroy() {
        revokeAndClearAllFds();
        getBlobFile().delete();
    }

    private void revokeAndClearAllFds() {
        synchronized (this.mRevocableFds) {
            int accessorCount = this.mRevocableFds.size();
            for (int i = 0; i < accessorCount; i++) {
                android.util.ArraySet<android.os.RevocableFileDescriptor> rFds = this.mRevocableFds.valueAt(i);
                if (rFds != null) {
                    int fdCount = rFds.size();
                    for (int j = 0; j < fdCount; j++) {
                        rFds.valueAt(j).revoke();
                    }
                }
            }
            this.mRevocableFds.clear();
        }
    }

    boolean shouldBeDeleted(boolean respectLeaseWaitTime) {
        if (getBlobHandle().isExpired()) {
            return true;
        }
        return (!respectLeaseWaitTime || hasLeaseWaitTimeElapsedForAll()) && !hasValidLeases();
    }

    boolean hasLeaseWaitTimeElapsedForAll() {
        int size = this.mCommitters.size();
        for (int i = 0; i < size; i++) {
            com.android.server.blob.BlobMetadata.Committer committer = this.mCommitters.valueAt(i);
            if (!com.android.server.blob.BlobStoreConfig.hasLeaseWaitTimeElapsed(committer.getCommitTimeMs())) {
                return false;
            }
        }
        return true;
    }

    android.util.StatsEvent dumpAsStatsEvent(int atomTag) {
        android.util.StatsEvent statsEventBuildStatsEvent;
        synchronized (this.mMetadataLock) {
            android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream();
            int size = this.mCommitters.size();
            for (int i = 0; i < size; i++) {
                com.android.server.blob.BlobMetadata.Committer committer = this.mCommitters.valueAt(i);
                long token = proto.start(2246267895809L);
                proto.write(1120986464257L, committer.uid);
                proto.write(1112396529666L, committer.commitTimeMs);
                proto.write(1120986464259L, committer.blobAccessMode.getAccessType());
                proto.write(1120986464260L, committer.blobAccessMode.getAllowedPackagesCount());
                proto.end(token);
            }
            byte[] committersBytes = proto.getBytes();
            android.util.proto.ProtoOutputStream proto2 = new android.util.proto.ProtoOutputStream();
            int size2 = this.mLeasees.size();
            for (int i2 = 0; i2 < size2; i2++) {
                com.android.server.blob.BlobMetadata.Leasee leasee = this.mLeasees.valueAt(i2);
                long token2 = proto2.start(2246267895809L);
                proto2.write(1120986464257L, leasee.uid);
                proto2.write(1112396529666L, leasee.expiryTimeMillis);
                proto2.end(token2);
            }
            byte[] leaseesBytes = proto2.getBytes();
            statsEventBuildStatsEvent = com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, this.mBlobId, getSize(), this.mBlobHandle.getExpiryTimeMillis(), committersBytes, leaseesBytes);
        }
        return statsEventBuildStatsEvent;
    }

    void dump(android.util.IndentingPrintWriter fout, com.android.server.blob.BlobStoreManagerService.DumpArgs dumpArgs) {
        synchronized (this.mMetadataLock) {
            fout.println("blobHandle:");
            fout.increaseIndent();
            this.mBlobHandle.dump(fout, dumpArgs.shouldDumpFull());
            fout.decreaseIndent();
            fout.println("size: " + android.text.format.Formatter.formatFileSize(this.mContext, getSize(), 8));
            fout.println("Committers:");
            fout.increaseIndent();
            if (this.mCommitters.isEmpty()) {
                fout.println("<empty>");
            } else {
                int count = this.mCommitters.size();
                for (int i = 0; i < count; i++) {
                    com.android.server.blob.BlobMetadata.Committer committer = this.mCommitters.valueAt(i);
                    fout.println("committer " + committer.toString());
                    fout.increaseIndent();
                    committer.dump(fout);
                    fout.decreaseIndent();
                }
            }
            fout.decreaseIndent();
            fout.println("Leasees:");
            fout.increaseIndent();
            if (this.mLeasees.isEmpty()) {
                fout.println("<empty>");
            } else {
                int count2 = this.mLeasees.size();
                for (int i2 = 0; i2 < count2; i2++) {
                    com.android.server.blob.BlobMetadata.Leasee leasee = this.mLeasees.valueAt(i2);
                    fout.println("leasee " + leasee.toString());
                    fout.increaseIndent();
                    leasee.dump(this.mContext, fout);
                    fout.decreaseIndent();
                }
            }
            fout.decreaseIndent();
            fout.println("Open fds:");
            fout.increaseIndent();
            if (this.mRevocableFds.isEmpty()) {
                fout.println("<empty>");
            } else {
                int count3 = this.mRevocableFds.size();
                for (int i3 = 0; i3 < count3; i3++) {
                    com.android.server.blob.BlobMetadata.Accessor accessor = this.mRevocableFds.keyAt(i3);
                    android.util.ArraySet<android.os.RevocableFileDescriptor> rFds = this.mRevocableFds.valueAt(i3);
                    fout.println(accessor + ": #" + rFds.size());
                }
            }
            fout.decreaseIndent();
        }
    }

    void writeToXml(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
        synchronized (this.mMetadataLock) {
            com.android.internal.util.XmlUtils.writeLongAttribute(out, "id", this.mBlobId);
            out.startTag(null, "bh");
            this.mBlobHandle.writeToXml(out);
            out.endTag(null, "bh");
            int count = this.mCommitters.size();
            for (int i = 0; i < count; i++) {
                out.startTag(null, "c");
                this.mCommitters.valueAt(i).writeToXml(out);
                out.endTag(null, "c");
            }
            int count2 = this.mLeasees.size();
            for (int i2 = 0; i2 < count2; i2++) {
                out.startTag(null, "l");
                this.mLeasees.valueAt(i2).writeToXml(out);
                out.endTag(null, "l");
            }
        }
    }

    static com.android.server.blob.BlobMetadata createFromXml(org.xmlpull.v1.XmlPullParser in, int version, android.content.Context context) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        long blobId = com.android.internal.util.XmlUtils.readLongAttribute(in, "id");
        if (version < 6) {
            com.android.internal.util.XmlUtils.readIntAttribute(in, "us");
        }
        android.app.blob.BlobHandle blobHandle = null;
        android.util.ArraySet<com.android.server.blob.BlobMetadata.Committer> committers = new android.util.ArraySet<>();
        android.util.ArraySet<com.android.server.blob.BlobMetadata.Leasee> leasees = new android.util.ArraySet<>();
        int depth = in.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(in, depth)) {
            if ("bh".equals(in.getName())) {
                blobHandle = android.app.blob.BlobHandle.createFromXml(in);
            } else if ("c".equals(in.getName())) {
                com.android.server.blob.BlobMetadata.Committer committer = com.android.server.blob.BlobMetadata.Committer.createFromXml(in, version);
                if (committer != null) {
                    committers.add(committer);
                }
            } else if ("l".equals(in.getName())) {
                leasees.add(com.android.server.blob.BlobMetadata.Leasee.createFromXml(in, version));
            }
        }
        if (blobHandle == null) {
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "blobHandle should be available");
            return null;
        }
        com.android.server.blob.BlobMetadata blobMetadata = new com.android.server.blob.BlobMetadata(context, blobId, blobHandle);
        blobMetadata.setCommitters(committers);
        blobMetadata.setLeasees(leasees);
        return blobMetadata;
    }

    static final class Committer extends com.android.server.blob.BlobMetadata.Accessor {
        public final com.android.server.blob.BlobAccessMode blobAccessMode;
        public final long commitTimeMs;

        Committer(java.lang.String packageName, int uid, com.android.server.blob.BlobAccessMode blobAccessMode, long commitTimeMs) {
            super(packageName, uid);
            this.blobAccessMode = blobAccessMode;
            this.commitTimeMs = commitTimeMs;
        }

        long getCommitTimeMs() {
            return this.commitTimeMs;
        }

        void dump(android.util.IndentingPrintWriter fout) {
            fout.println("commit time: " + (this.commitTimeMs == 0 ? "<null>" : com.android.server.blob.BlobStoreUtils.formatTime(this.commitTimeMs)));
            fout.println("accessMode:");
            fout.increaseIndent();
            this.blobAccessMode.dump(fout);
            fout.decreaseIndent();
        }

        void writeToXml(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
            com.android.internal.util.XmlUtils.writeStringAttribute(out, "p", this.packageName);
            com.android.internal.util.XmlUtils.writeIntAttribute(out, "u", this.uid);
            com.android.internal.util.XmlUtils.writeLongAttribute(out, "cmt", this.commitTimeMs);
            out.startTag(null, "am");
            this.blobAccessMode.writeToXml(out);
            out.endTag(null, "am");
        }

        static com.android.server.blob.BlobMetadata.Committer createFromXml(org.xmlpull.v1.XmlPullParser in, int version) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            long commitTimeMs;
            java.lang.String packageName = com.android.internal.util.XmlUtils.readStringAttribute(in, "p");
            int uid = com.android.internal.util.XmlUtils.readIntAttribute(in, "u");
            if (version >= 4) {
                commitTimeMs = com.android.internal.util.XmlUtils.readLongAttribute(in, "cmt");
            } else {
                commitTimeMs = 0;
            }
            int depth = in.getDepth();
            com.android.server.blob.BlobAccessMode blobAccessMode = null;
            while (com.android.internal.util.XmlUtils.nextElementWithin(in, depth)) {
                if ("am".equals(in.getName())) {
                    blobAccessMode = com.android.server.blob.BlobAccessMode.createFromXml(in);
                }
            }
            if (blobAccessMode == null) {
                android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "blobAccessMode should be available");
                return null;
            }
            return new com.android.server.blob.BlobMetadata.Committer(packageName, uid, blobAccessMode, commitTimeMs);
        }
    }

    static final class Leasee extends com.android.server.blob.BlobMetadata.Accessor {
        public final java.lang.CharSequence description;
        public final java.lang.String descriptionResEntryName;
        public final long expiryTimeMillis;

        Leasee(android.content.Context context, java.lang.String packageName, int uid, int descriptionResId, java.lang.CharSequence description, long expiryTimeMillis) {
            java.lang.CharSequence description2;
            super(packageName, uid);
            android.content.res.Resources packageResources = com.android.server.blob.BlobStoreUtils.getPackageResources(context, packageName, android.os.UserHandle.getUserId(uid));
            this.descriptionResEntryName = getResourceEntryName(packageResources, descriptionResId);
            this.expiryTimeMillis = expiryTimeMillis;
            if (description == null) {
                description2 = getDescription(packageResources, descriptionResId);
            } else {
                description2 = description;
            }
            this.description = description2;
        }

        Leasee(java.lang.String packageName, int uid, java.lang.String descriptionResEntryName, java.lang.CharSequence description, long expiryTimeMillis) {
            super(packageName, uid);
            this.descriptionResEntryName = descriptionResEntryName;
            this.expiryTimeMillis = expiryTimeMillis;
            this.description = description;
        }

        private static java.lang.String getResourceEntryName(android.content.res.Resources packageResources, int resId) {
            if (!android.content.res.ResourceId.isValid(resId) || packageResources == null) {
                return null;
            }
            return packageResources.getResourceEntryName(resId);
        }

        private static java.lang.String getDescription(android.content.Context context, java.lang.String descriptionResEntryName, java.lang.String packageName, int userId) {
            android.content.res.Resources resources;
            int resId;
            if (descriptionResEntryName == null || descriptionResEntryName.isEmpty() || (resources = com.android.server.blob.BlobStoreUtils.getPackageResources(context, packageName, userId)) == null || (resId = com.android.server.blob.BlobStoreUtils.getDescriptionResourceId(resources, descriptionResEntryName, packageName)) == 0) {
                return null;
            }
            return resources.getString(resId);
        }

        private static java.lang.String getDescription(android.content.res.Resources packageResources, int descriptionResId) {
            if (!android.content.res.ResourceId.isValid(descriptionResId) || packageResources == null) {
                return null;
            }
            return packageResources.getString(descriptionResId);
        }

        boolean isStillValid() {
            return this.expiryTimeMillis == 0 || this.expiryTimeMillis >= java.lang.System.currentTimeMillis();
        }

        void dump(android.content.Context context, android.util.IndentingPrintWriter fout) {
            fout.println("desc: " + getDescriptionToDump(context));
            fout.println("expiryMs: " + this.expiryTimeMillis);
        }

        private java.lang.String getDescriptionToDump(android.content.Context context) {
            java.lang.String desc = getDescription(context, this.descriptionResEntryName, this.packageName, android.os.UserHandle.getUserId(this.uid));
            if (desc == null) {
                desc = this.description.toString();
            }
            return desc == null ? "<none>" : desc;
        }

        void writeToXml(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
            com.android.internal.util.XmlUtils.writeStringAttribute(out, "p", this.packageName);
            com.android.internal.util.XmlUtils.writeIntAttribute(out, "u", this.uid);
            com.android.internal.util.XmlUtils.writeStringAttribute(out, "rn", this.descriptionResEntryName);
            com.android.internal.util.XmlUtils.writeLongAttribute(out, "ex", this.expiryTimeMillis);
            com.android.internal.util.XmlUtils.writeStringAttribute(out, "d", this.description);
        }

        static com.android.server.blob.BlobMetadata.Leasee createFromXml(org.xmlpull.v1.XmlPullParser in, int version) throws java.io.IOException {
            java.lang.String descriptionResEntryName;
            java.lang.CharSequence description;
            java.lang.String packageName = com.android.internal.util.XmlUtils.readStringAttribute(in, "p");
            int uid = com.android.internal.util.XmlUtils.readIntAttribute(in, "u");
            if (version >= 3) {
                descriptionResEntryName = com.android.internal.util.XmlUtils.readStringAttribute(in, "rn");
            } else {
                descriptionResEntryName = null;
            }
            long expiryTimeMillis = com.android.internal.util.XmlUtils.readLongAttribute(in, "ex");
            if (version >= 2) {
                description = com.android.internal.util.XmlUtils.readStringAttribute(in, "d");
            } else {
                description = null;
            }
            return new com.android.server.blob.BlobMetadata.Leasee(packageName, uid, descriptionResEntryName, description, expiryTimeMillis);
        }
    }

    static class Accessor {
        public final java.lang.String packageName;
        public final int uid;

        Accessor(java.lang.String packageName, int uid) {
            this.packageName = packageName;
            this.uid = uid;
        }

        public boolean equals(java.lang.String packageName, int uid) {
            return this.uid == uid && this.packageName.equals(packageName);
        }

        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof com.android.server.blob.BlobMetadata.Accessor)) {
                return false;
            }
            com.android.server.blob.BlobMetadata.Accessor other = (com.android.server.blob.BlobMetadata.Accessor) obj;
            if (this.uid == other.uid && this.packageName.equals(other.packageName)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.packageName, java.lang.Integer.valueOf(this.uid));
        }

        public java.lang.String toString() {
            return "[" + this.packageName + ", " + this.uid + "]";
        }
    }
}
