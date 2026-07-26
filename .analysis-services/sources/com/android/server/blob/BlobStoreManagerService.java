package com.android.server.blob;

/* JADX INFO: loaded from: classes.dex */
public class BlobStoreManagerService extends com.android.server.SystemService {
    private final android.util.ArraySet<java.lang.Long> mActiveBlobIds;
    private final android.os.Handler mBackgroundHandler;
    private final java.lang.Object mBlobsLock;
    private final android.util.ArrayMap<android.app.blob.BlobHandle, com.android.server.blob.BlobMetadata> mBlobsMap;
    private final android.content.Context mContext;
    private long mCurrentMaxSessionId;
    private final android.os.Handler mHandler;
    private final com.android.server.blob.BlobStoreManagerService.Injector mInjector;
    private final android.util.ArraySet<java.lang.Long> mKnownBlobIds;
    private android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final java.util.Random mRandom;
    private final java.lang.Runnable mSaveBlobsInfoRunnable;
    private final java.lang.Runnable mSaveSessionsRunnable;
    private final com.android.server.blob.BlobStoreManagerService.SessionStateChangeListener mSessionStateChangeListener;
    private final android.util.SparseArray<android.util.LongSparseArray<com.android.server.blob.BlobStoreSession>> mSessions;
    private com.android.server.blob.BlobStoreManagerService.StatsPullAtomCallbackImpl mStatsCallbackImpl;
    private android.app.StatsManager mStatsManager;

    public BlobStoreManagerService(android.content.Context context) {
        this(context, new com.android.server.blob.BlobStoreManagerService.Injector());
    }

    BlobStoreManagerService(android.content.Context context, com.android.server.blob.BlobStoreManagerService.Injector injector) {
        super(context);
        this.mBlobsLock = new java.lang.Object();
        this.mSessions = new android.util.SparseArray<>();
        this.mBlobsMap = new android.util.ArrayMap<>();
        this.mActiveBlobIds = new android.util.ArraySet<>();
        this.mKnownBlobIds = new android.util.ArraySet<>();
        this.mRandom = new java.security.SecureRandom();
        this.mSessionStateChangeListener = new com.android.server.blob.BlobStoreManagerService.SessionStateChangeListener();
        this.mStatsCallbackImpl = new com.android.server.blob.BlobStoreManagerService.StatsPullAtomCallbackImpl();
        this.mSaveBlobsInfoRunnable = new java.lang.Runnable() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.writeBlobsInfo();
            }
        };
        this.mSaveSessionsRunnable = new java.lang.Runnable() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.writeBlobSessions();
            }
        };
        this.mContext = context;
        this.mInjector = injector;
        this.mHandler = this.mInjector.initializeMessageHandler();
        this.mBackgroundHandler = this.mInjector.getBackgroundHandler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.os.Handler initializeMessageHandler() {
        android.os.HandlerThread handlerThread = new com.android.server.ServiceThread(com.android.server.blob.BlobStoreConfig.TAG, 0, true);
        handlerThread.start();
        android.os.Handler handler = new android.os.Handler(handlerThread.getLooper());
        com.android.server.Watchdog.getInstance().addThread(handler);
        return handler;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("blob_store", new com.android.server.blob.BlobStoreManagerService.Stub());
        com.android.server.LocalServices.addService(com.android.server.blob.BlobStoreManagerInternal.class, new com.android.server.blob.BlobStoreManagerService.LocalService());
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mStatsManager = (android.app.StatsManager) getContext().getSystemService(android.app.StatsManager.class);
        registerReceivers();
        ((com.android.server.usage.StorageStatsManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.usage.StorageStatsManagerLocal.class)).registerStorageStatsAugmenter(new com.android.server.blob.BlobStoreManagerService.BlobStorageStatsAugmenter(), com.android.server.blob.BlobStoreConfig.TAG);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 550) {
            com.android.server.blob.BlobStoreConfig.initialize(this.mContext);
            return;
        }
        if (phase == 600) {
            synchronized (this.mBlobsLock) {
                android.util.SparseArray<android.util.SparseArray<java.lang.String>> allPackages = getAllPackages();
                readBlobSessionsLocked(allPackages);
                readBlobsInfoLocked(allPackages);
            }
            registerBlobStorePuller();
            return;
        }
        if (phase == 1000) {
            com.android.server.blob.BlobStoreIdleJobService.schedule(this.mContext);
        }
    }

    private long generateNextSessionIdLocked() {
        int n = 0;
        while (true) {
            long randomLong = this.mRandom.nextLong();
            long sessionId = randomLong == Long.MIN_VALUE ? 0L : java.lang.Math.abs(randomLong);
            if (this.mKnownBlobIds.indexOf(java.lang.Long.valueOf(sessionId)) < 0 && sessionId != 0) {
                return sessionId;
            }
            int n2 = n + 1;
            if (n >= 32) {
                throw new java.lang.IllegalStateException("Failed to allocate session ID");
            }
            n = n2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void registerReceivers() {
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
        intentFilter.addDataScheme("package");
        this.mContext.registerReceiverAsUser(new com.android.server.blob.BlobStoreManagerService.PackageChangedReceiver(), android.os.UserHandle.ALL, intentFilter, null, this.mHandler);
        android.content.IntentFilter intentFilter2 = new android.content.IntentFilter();
        intentFilter2.addAction("android.intent.action.USER_REMOVED");
        this.mContext.registerReceiverAsUser(new com.android.server.blob.BlobStoreManagerService.UserActionReceiver(), android.os.UserHandle.ALL, intentFilter2, null, this.mHandler);
    }

    private android.util.LongSparseArray<com.android.server.blob.BlobStoreSession> getUserSessionsLocked(int userId) {
        android.util.LongSparseArray<com.android.server.blob.BlobStoreSession> userSessions = this.mSessions.get(userId);
        if (userSessions == null) {
            android.util.LongSparseArray<com.android.server.blob.BlobStoreSession> userSessions2 = new android.util.LongSparseArray<>();
            this.mSessions.put(userId, userSessions2);
            return userSessions2;
        }
        return userSessions;
    }

    void addUserSessionsForTest(android.util.LongSparseArray<com.android.server.blob.BlobStoreSession> userSessions, int userId) {
        synchronized (this.mBlobsLock) {
            this.mSessions.put(userId, userSessions);
        }
    }

    com.android.server.blob.BlobMetadata getBlobForTest(android.app.blob.BlobHandle blobHandle) {
        com.android.server.blob.BlobMetadata blobMetadata;
        synchronized (this.mBlobsLock) {
            blobMetadata = this.mBlobsMap.get(blobHandle);
        }
        return blobMetadata;
    }

    int getBlobsCountForTest() {
        int size;
        synchronized (this.mBlobsLock) {
            size = this.mBlobsMap.size();
        }
        return size;
    }

    void addActiveIdsForTest(long... activeIds) {
        synchronized (this.mBlobsLock) {
            for (long id : activeIds) {
                addActiveBlobIdLocked(id);
            }
        }
    }

    java.util.Set<java.lang.Long> getActiveIdsForTest() {
        android.util.ArraySet<java.lang.Long> arraySet;
        synchronized (this.mBlobsLock) {
            arraySet = this.mActiveBlobIds;
        }
        return arraySet;
    }

    java.util.Set<java.lang.Long> getKnownIdsForTest() {
        android.util.ArraySet<java.lang.Long> arraySet;
        synchronized (this.mBlobsLock) {
            arraySet = this.mKnownBlobIds;
        }
        return arraySet;
    }

    private void addSessionForUserLocked(com.android.server.blob.BlobStoreSession session, int userId) {
        getUserSessionsLocked(userId).put(session.getSessionId(), session);
        addActiveBlobIdLocked(session.getSessionId());
    }

    void addBlobLocked(com.android.server.blob.BlobMetadata blobMetadata) {
        this.mBlobsMap.put(blobMetadata.getBlobHandle(), blobMetadata);
        addActiveBlobIdLocked(blobMetadata.getBlobId());
    }

    private void addActiveBlobIdLocked(long id) {
        this.mActiveBlobIds.add(java.lang.Long.valueOf(id));
        this.mKnownBlobIds.add(java.lang.Long.valueOf(id));
    }

    private int getSessionsCountLocked(final int uid, final java.lang.String packageName) {
        final java.util.concurrent.atomic.AtomicInteger sessionsCount = new java.util.concurrent.atomic.AtomicInteger(0);
        forEachSessionInUser(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.blob.BlobStoreManagerService.lambda$getSessionsCountLocked$0(uid, packageName, sessionsCount, (com.android.server.blob.BlobStoreSession) obj);
            }
        }, android.os.UserHandle.getUserId(uid));
        return sessionsCount.get();
    }

    static /* synthetic */ void lambda$getSessionsCountLocked$0(int uid, java.lang.String packageName, java.util.concurrent.atomic.AtomicInteger sessionsCount, com.android.server.blob.BlobStoreSession session) {
        if (session.getOwnerUid() == uid && session.getOwnerPackageName().equals(packageName)) {
            sessionsCount.getAndIncrement();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long createSessionInternal(android.app.blob.BlobHandle blobHandle, int callingUid, java.lang.String callingPackage) throws java.lang.Throwable {
        synchronized (this.mBlobsLock) {
            try {
                try {
                    int sessionsCount = getSessionsCountLocked(callingUid, callingPackage);
                    if (sessionsCount >= com.android.server.blob.BlobStoreConfig.getMaxActiveSessions()) {
                        throw new android.os.LimitExceededException("Too many active sessions for the caller: " + sessionsCount);
                    }
                    long sessionId = generateNextSessionIdLocked();
                    com.android.server.blob.BlobStoreSession session = new com.android.server.blob.BlobStoreSession(this.mContext, sessionId, blobHandle, callingUid, callingPackage, this.mSessionStateChangeListener);
                    addSessionForUserLocked(session, android.os.UserHandle.getUserId(callingUid));
                    if (com.android.server.blob.BlobStoreConfig.LOGV) {
                        android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Created session for " + blobHandle + "; callingUid=" + callingUid + ", callingPackage=" + callingPackage);
                    }
                    writeBlobSessionsAsync();
                    return sessionId;
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.blob.BlobStoreSession openSessionInternal(long sessionId, int callingUid, java.lang.String callingPackage) {
        com.android.server.blob.BlobStoreSession session;
        synchronized (this.mBlobsLock) {
            session = getUserSessionsLocked(android.os.UserHandle.getUserId(callingUid)).get(sessionId);
            if (session == null || !session.hasAccess(callingUid, callingPackage) || session.isFinalized()) {
                throw new java.lang.SecurityException("Session not found: " + sessionId);
            }
        }
        session.open();
        return session;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void abandonSessionInternal(long sessionId, int callingUid, java.lang.String callingPackage) {
        synchronized (this.mBlobsLock) {
            com.android.server.blob.BlobStoreSession session = openSessionInternal(sessionId, callingUid, callingPackage);
            session.open();
            session.abandon();
            if (com.android.server.blob.BlobStoreConfig.LOGV) {
                android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Abandoned session with id " + sessionId + "; callingUid=" + callingUid + ", callingPackage=" + callingPackage);
            }
            writeBlobSessionsAsync();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.ParcelFileDescriptor openBlobInternal(android.app.blob.BlobHandle blobHandle, int callingUid, java.lang.String callingPackage) throws java.io.IOException {
        android.os.ParcelFileDescriptor parcelFileDescriptorOpenForRead;
        synchronized (this.mBlobsLock) {
            com.android.server.blob.BlobMetadata blobMetadata = this.mBlobsMap.get(blobHandle);
            if (blobMetadata != null && blobMetadata.isAccessAllowedForCaller(callingPackage, callingUid)) {
                com.android.internal.util.FrameworkStatsLog.write(300, callingUid, blobMetadata.getBlobId(), blobMetadata.getSize(), 1);
                parcelFileDescriptorOpenForRead = blobMetadata.openForRead(callingPackage, callingUid);
            }
            if (blobMetadata == null) {
                com.android.internal.util.FrameworkStatsLog.write(300, callingUid, 0L, 0L, 2);
            } else {
                com.android.internal.util.FrameworkStatsLog.write(300, callingUid, blobMetadata.getBlobId(), blobMetadata.getSize(), 3);
            }
            throw new java.lang.SecurityException("Caller not allowed to access " + blobHandle + "; callingUid=" + callingUid + ", callingPackage=" + callingPackage);
        }
        return parcelFileDescriptorOpenForRead;
    }

    private int getCommittedBlobsCountLocked(final int uid, final java.lang.String packageName) {
        final java.util.concurrent.atomic.AtomicInteger blobsCount = new java.util.concurrent.atomic.AtomicInteger(0);
        forEachBlobLocked(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.blob.BlobStoreManagerService.lambda$getCommittedBlobsCountLocked$1(packageName, uid, blobsCount, (com.android.server.blob.BlobMetadata) obj);
            }
        });
        return blobsCount.get();
    }

    static /* synthetic */ void lambda$getCommittedBlobsCountLocked$1(java.lang.String packageName, int uid, java.util.concurrent.atomic.AtomicInteger blobsCount, com.android.server.blob.BlobMetadata blobMetadata) {
        if (blobMetadata.isACommitter(packageName, uid)) {
            blobsCount.getAndIncrement();
        }
    }

    private int getLeasedBlobsCountLocked(final int uid, final java.lang.String packageName) {
        final java.util.concurrent.atomic.AtomicInteger blobsCount = new java.util.concurrent.atomic.AtomicInteger(0);
        forEachBlobLocked(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.blob.BlobStoreManagerService.lambda$getLeasedBlobsCountLocked$2(packageName, uid, blobsCount, (com.android.server.blob.BlobMetadata) obj);
            }
        });
        return blobsCount.get();
    }

    static /* synthetic */ void lambda$getLeasedBlobsCountLocked$2(java.lang.String packageName, int uid, java.util.concurrent.atomic.AtomicInteger blobsCount, com.android.server.blob.BlobMetadata blobMetadata) {
        if (blobMetadata.isALeasee(packageName, uid)) {
            blobsCount.getAndIncrement();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void acquireLeaseInternal(android.app.blob.BlobHandle blobHandle, int descriptionResId, java.lang.CharSequence description, long leaseExpiryTimeMillis, int callingUid, java.lang.String callingPackage) {
        synchronized (this.mBlobsLock) {
            int leasesCount = getLeasedBlobsCountLocked(callingUid, callingPackage);
            if (leasesCount >= com.android.server.blob.BlobStoreConfig.getMaxLeasedBlobs()) {
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BLOB_LEASED, callingUid, 0L, 0L, 6);
                throw new android.os.LimitExceededException("Too many leased blobs for the caller: " + leasesCount);
            }
            if (leaseExpiryTimeMillis != 0 && blobHandle.expiryTimeMillis != 0 && leaseExpiryTimeMillis > blobHandle.expiryTimeMillis) {
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BLOB_LEASED, callingUid, 0L, 0L, 4);
                throw new java.lang.IllegalArgumentException("Lease expiry cannot be later than blobs expiry time");
            }
            com.android.server.blob.BlobMetadata blobMetadata = this.mBlobsMap.get(blobHandle);
            if (blobMetadata != null && blobMetadata.isAccessAllowedForCaller(callingPackage, callingUid)) {
                if (blobMetadata.getSize() > getRemainingLeaseQuotaBytesInternal(callingUid, callingPackage)) {
                    com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BLOB_LEASED, callingUid, blobMetadata.getBlobId(), blobMetadata.getSize(), 5);
                    throw new android.os.LimitExceededException("Total amount of data with an active lease is exceeding the max limit");
                }
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BLOB_LEASED, callingUid, blobMetadata.getBlobId(), blobMetadata.getSize(), 1);
                blobMetadata.addOrReplaceLeasee(callingPackage, callingUid, descriptionResId, description, leaseExpiryTimeMillis);
                if (com.android.server.blob.BlobStoreConfig.LOGV) {
                    android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Acquired lease on " + blobHandle + "; callingUid=" + callingUid + ", callingPackage=" + callingPackage);
                }
                writeBlobsInfoAsync();
            }
            if (blobMetadata == null) {
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BLOB_LEASED, callingUid, 0L, 0L, 2);
            } else {
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BLOB_LEASED, callingUid, blobMetadata.getBlobId(), blobMetadata.getSize(), 3);
            }
            throw new java.lang.SecurityException("Caller not allowed to access " + blobHandle + "; callingUid=" + callingUid + ", callingPackage=" + callingPackage);
        }
    }

    long getTotalUsageBytesLocked(final int callingUid, final java.lang.String callingPackage) {
        final java.util.concurrent.atomic.AtomicLong totalBytes = new java.util.concurrent.atomic.AtomicLong(0L);
        forEachBlobLocked(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.blob.BlobStoreManagerService.lambda$getTotalUsageBytesLocked$3(callingPackage, callingUid, totalBytes, (com.android.server.blob.BlobMetadata) obj);
            }
        });
        return totalBytes.get();
    }

    static /* synthetic */ void lambda$getTotalUsageBytesLocked$3(java.lang.String callingPackage, int callingUid, java.util.concurrent.atomic.AtomicLong totalBytes, com.android.server.blob.BlobMetadata blobMetadata) {
        if (blobMetadata.isALeasee(callingPackage, callingUid)) {
            totalBytes.getAndAdd(blobMetadata.getSize());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseLeaseInternal(final android.app.blob.BlobHandle blobHandle, int callingUid, java.lang.String callingPackage) {
        synchronized (this.mBlobsLock) {
            final com.android.server.blob.BlobMetadata blobMetadata = this.mBlobsMap.get(blobHandle);
            if (blobMetadata == null || !blobMetadata.isAccessAllowedForCaller(callingPackage, callingUid)) {
                throw new java.lang.SecurityException("Caller not allowed to access " + blobHandle + "; callingUid=" + callingUid + ", callingPackage=" + callingPackage);
            }
            blobMetadata.removeLeasee(callingPackage, callingUid);
            if (com.android.server.blob.BlobStoreConfig.LOGV) {
                android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Released lease on " + blobHandle + "; callingUid=" + callingUid + ", callingPackage=" + callingPackage);
            }
            if (!blobMetadata.hasValidLeases()) {
                this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$releaseLeaseInternal$4(blobHandle, blobMetadata);
                    }
                }, com.android.server.blob.BlobStoreConfig.getDeletionOnLastLeaseDelayMs());
            }
            writeBlobsInfoAsync();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$releaseLeaseInternal$4(android.app.blob.BlobHandle blobHandle, com.android.server.blob.BlobMetadata blobMetadata) {
        synchronized (this.mBlobsLock) {
            if (java.util.Objects.equals(this.mBlobsMap.get(blobHandle), blobMetadata)) {
                if (blobMetadata.shouldBeDeleted(true)) {
                    deleteBlobLocked(blobMetadata);
                    this.mBlobsMap.remove(blobHandle);
                }
                writeBlobsInfoAsync();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseAllLeasesInternal(final int callingUid, final java.lang.String callingPackage) {
        synchronized (this.mBlobsLock) {
            this.mBlobsMap.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda3
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.blob.BlobMetadata) obj2).removeLeasee(callingPackage, callingUid);
                }
            });
            writeBlobsInfoAsync();
            if (com.android.server.blob.BlobStoreConfig.LOGV) {
                android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Release all leases associated with pkg=" + callingPackage + ", uid=" + callingUid);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getRemainingLeaseQuotaBytesInternal(int callingUid, java.lang.String callingPackage) {
        long j;
        synchronized (this.mBlobsLock) {
            long remainingQuota = com.android.server.blob.BlobStoreConfig.getAppDataBytesLimit() - getTotalUsageBytesLocked(callingUid, callingPackage);
            j = remainingQuota > 0 ? remainingQuota : 0L;
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.app.blob.BlobInfo> queryBlobsForUserInternal(final int userId) {
        final java.util.ArrayList<android.app.blob.BlobInfo> blobInfos = new java.util.ArrayList<>();
        synchronized (this.mBlobsLock) {
            final android.util.ArrayMap<java.lang.String, java.lang.ref.WeakReference<android.content.res.Resources>> resources = new android.util.ArrayMap<>();
            final java.util.function.Function<java.lang.String, android.content.res.Resources> resourcesGetter = new java.util.function.Function() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda12
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$queryBlobsForUserInternal$6(resources, userId, (java.lang.String) obj);
                }
            };
            forEachBlobLocked(new java.util.function.BiConsumer() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda13
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    com.android.server.blob.BlobStoreManagerService.lambda$queryBlobsForUserInternal$8(userId, resourcesGetter, blobInfos, (android.app.blob.BlobHandle) obj, (com.android.server.blob.BlobMetadata) obj2);
                }
            });
        }
        return blobInfos;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.res.Resources lambda$queryBlobsForUserInternal$6(android.util.ArrayMap resources, int userId, java.lang.String packageName) {
        java.lang.ref.WeakReference<android.content.res.Resources> resourcesRef = (java.lang.ref.WeakReference) resources.get(packageName);
        android.content.res.Resources packageResources = resourcesRef == null ? null : resourcesRef.get();
        if (packageResources == null) {
            android.content.res.Resources packageResources2 = com.android.server.blob.BlobStoreUtils.getPackageResources(this.mContext, packageName, userId);
            resources.put(packageName, new java.lang.ref.WeakReference(packageResources2));
            return packageResources2;
        }
        return packageResources;
    }

    static /* synthetic */ void lambda$queryBlobsForUserInternal$8(final int userId, final java.util.function.Function resourcesGetter, java.util.ArrayList blobInfos, final android.app.blob.BlobHandle blobHandle, com.android.server.blob.BlobMetadata blobMetadata) {
        if (!blobMetadata.hasACommitterOrLeaseeInUser(userId)) {
            return;
        }
        final java.util.ArrayList<android.app.blob.LeaseInfo> leaseInfos = new java.util.ArrayList<>();
        blobMetadata.forEachLeasee(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.blob.BlobStoreManagerService.lambda$queryBlobsForUserInternal$7(userId, resourcesGetter, blobHandle, leaseInfos, (com.android.server.blob.BlobMetadata.Leasee) obj);
            }
        });
        blobInfos.add(new android.app.blob.BlobInfo(blobMetadata.getBlobId(), blobHandle.getExpiryTimeMillis(), blobHandle.getLabel(), blobMetadata.getSize(), leaseInfos));
    }

    static /* synthetic */ void lambda$queryBlobsForUserInternal$7(int userId, java.util.function.Function resourcesGetter, android.app.blob.BlobHandle blobHandle, java.util.ArrayList leaseInfos, com.android.server.blob.BlobMetadata.Leasee leasee) {
        int descriptionResId;
        if (!leasee.isStillValid() || userId != android.os.UserHandle.getUserId(leasee.uid)) {
            return;
        }
        if (leasee.descriptionResEntryName == null) {
            descriptionResId = 0;
        } else {
            descriptionResId = com.android.server.blob.BlobStoreUtils.getDescriptionResourceId((android.content.res.Resources) resourcesGetter.apply(leasee.packageName), leasee.descriptionResEntryName, leasee.packageName);
        }
        long expiryTimeMs = leasee.expiryTimeMillis == 0 ? blobHandle.getExpiryTimeMillis() : leasee.expiryTimeMillis;
        leaseInfos.add(new android.app.blob.LeaseInfo(leasee.packageName, expiryTimeMs, descriptionResId, leasee.description));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteBlobInternal(final long blobId) {
        synchronized (this.mBlobsLock) {
            this.mBlobsMap.entrySet().removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda9
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$deleteBlobInternal$9(blobId, (java.util.Map.Entry) obj);
                }
            });
            writeBlobsInfoAsync();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$deleteBlobInternal$9(long blobId, java.util.Map.Entry entry) {
        com.android.server.blob.BlobMetadata blobMetadata = (com.android.server.blob.BlobMetadata) entry.getValue();
        if (blobMetadata.getBlobId() == blobId) {
            deleteBlobLocked(blobMetadata);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.app.blob.BlobHandle> getLeasedBlobsInternal(final int callingUid, final java.lang.String callingPackage) {
        final java.util.ArrayList<android.app.blob.BlobHandle> leasedBlobs = new java.util.ArrayList<>();
        synchronized (this.mBlobsLock) {
            forEachBlobLocked(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.blob.BlobStoreManagerService.lambda$getLeasedBlobsInternal$10(callingPackage, callingUid, leasedBlobs, (com.android.server.blob.BlobMetadata) obj);
                }
            });
        }
        return leasedBlobs;
    }

    static /* synthetic */ void lambda$getLeasedBlobsInternal$10(java.lang.String callingPackage, int callingUid, java.util.ArrayList leasedBlobs, com.android.server.blob.BlobMetadata blobMetadata) {
        if (blobMetadata.isALeasee(callingPackage, callingUid)) {
            leasedBlobs.add(blobMetadata.getBlobHandle());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.blob.LeaseInfo getLeaseInfoInternal(android.app.blob.BlobHandle blobHandle, int callingUid, java.lang.String callingPackage) {
        android.app.blob.LeaseInfo leaseInfo;
        synchronized (this.mBlobsLock) {
            com.android.server.blob.BlobMetadata blobMetadata = this.mBlobsMap.get(blobHandle);
            if (blobMetadata == null || !blobMetadata.isAccessAllowedForCaller(callingPackage, callingUid)) {
                throw new java.lang.SecurityException("Caller not allowed to access " + blobHandle + "; callingUid=" + callingUid + ", callingPackage=" + callingPackage);
            }
            leaseInfo = blobMetadata.getLeaseInfo(callingPackage, callingUid);
        }
        return leaseInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void verifyCallingPackage(int callingUid, java.lang.String callingPackage) {
        if (this.mPackageManagerInternal.getPackageUid(callingPackage, 0L, android.os.UserHandle.getUserId(callingUid)) != callingUid) {
            throw new java.lang.SecurityException("Specified calling package [" + callingPackage + "] does not match the calling uid " + callingUid);
        }
    }

    class SessionStateChangeListener {
        SessionStateChangeListener() {
        }

        public void onStateChanged(com.android.server.blob.BlobStoreSession session) {
            com.android.server.blob.BlobStoreManagerService.this.mHandler.post(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new java.util.function.BiConsumer() { // from class: com.android.server.blob.BlobStoreManagerService$SessionStateChangeListener$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.blob.BlobStoreManagerService) obj).onStateChangedInternal((com.android.server.blob.BlobStoreSession) obj2);
                }
            }, com.android.server.blob.BlobStoreManagerService.this, session).recycleOnUse());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStateChangedInternal(final com.android.server.blob.BlobStoreSession session) {
        com.android.server.blob.BlobMetadata blob;
        switch (session.getState()) {
            case 2:
            case 5:
                synchronized (this.mBlobsLock) {
                    deleteSessionLocked(session);
                    getUserSessionsLocked(android.os.UserHandle.getUserId(session.getOwnerUid())).remove(session.getSessionId());
                    if (com.android.server.blob.BlobStoreConfig.LOGV) {
                        android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Session is invalid; deleted " + session);
                    }
                    break;
                }
                break;
            case 3:
                this.mBackgroundHandler.post(new java.lang.Runnable() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onStateChangedInternal$11(session);
                    }
                });
                break;
            case 4:
                synchronized (this.mBlobsLock) {
                    int committedBlobsCount = getCommittedBlobsCountLocked(session.getOwnerUid(), session.getOwnerPackageName());
                    if (committedBlobsCount >= com.android.server.blob.BlobStoreConfig.getMaxCommittedBlobs()) {
                        android.util.Slog.d(com.android.server.blob.BlobStoreConfig.TAG, "Failed to commit: too many committed blobs. count: " + committedBlobsCount + "; blob: " + session);
                        session.sendCommitCallbackResult(1);
                        deleteSessionLocked(session);
                        getUserSessionsLocked(android.os.UserHandle.getUserId(session.getOwnerUid())).remove(session.getSessionId());
                        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BLOB_COMMITTED, session.getOwnerUid(), session.getSessionId(), session.getSize(), 4);
                    } else {
                        int blobIndex = this.mBlobsMap.indexOfKey(session.getBlobHandle());
                        if (blobIndex >= 0) {
                            blob = this.mBlobsMap.valueAt(blobIndex);
                        } else {
                            com.android.server.blob.BlobMetadata blob2 = new com.android.server.blob.BlobMetadata(this.mContext, session.getSessionId(), session.getBlobHandle());
                            addBlobLocked(blob2);
                            blob = blob2;
                        }
                        com.android.server.blob.BlobMetadata.Committer existingCommitter = blob.getExistingCommitter(session.getOwnerPackageName(), session.getOwnerUid());
                        long existingCommitTimeMs = existingCommitter == null ? 0L : existingCommitter.getCommitTimeMs();
                        com.android.server.blob.BlobMetadata.Committer newCommitter = new com.android.server.blob.BlobMetadata.Committer(session.getOwnerPackageName(), session.getOwnerUid(), session.getBlobAccessMode(), com.android.server.blob.BlobStoreConfig.getAdjustedCommitTimeMs(existingCommitTimeMs, java.lang.System.currentTimeMillis()));
                        blob.addOrReplaceCommitter(newCommitter);
                        try {
                            writeBlobsInfoLocked();
                            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BLOB_COMMITTED, session.getOwnerUid(), blob.getBlobId(), blob.getSize(), 1);
                            session.sendCommitCallbackResult(0);
                        } catch (java.lang.Exception e) {
                            if (existingCommitter == null) {
                                blob.removeCommitter(newCommitter);
                            } else {
                                blob.addOrReplaceCommitter(existingCommitter);
                            }
                            android.util.Slog.d(com.android.server.blob.BlobStoreConfig.TAG, "Error committing the blob: " + session, e);
                            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BLOB_COMMITTED, session.getOwnerUid(), session.getSessionId(), blob.getSize(), 2);
                            session.sendCommitCallbackResult(1);
                            if (session.getSessionId() == blob.getBlobId()) {
                                deleteBlobLocked(blob);
                                this.mBlobsMap.remove(blob.getBlobHandle());
                            }
                        }
                        if (session.getSessionId() != blob.getBlobId()) {
                            deleteSessionLocked(session);
                        }
                        getUserSessionsLocked(android.os.UserHandle.getUserId(session.getOwnerUid())).remove(session.getSessionId());
                        if (com.android.server.blob.BlobStoreConfig.LOGV) {
                            android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Successfully committed session " + session);
                        }
                    }
                    break;
                }
                break;
            default:
                android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Invalid session state: " + com.android.server.blob.BlobStoreSession.stateToString(session.getState()));
                break;
        }
        synchronized (this.mBlobsLock) {
            try {
                writeBlobSessionsLocked();
            } catch (java.lang.Exception e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStateChangedInternal$11(com.android.server.blob.BlobStoreSession session) {
        session.computeDigest();
        this.mHandler.post(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.blob.BlobStoreSession) obj).verifyBlobData();
            }
        }, session).recycleOnUse());
    }

    private void writeBlobSessionsLocked() throws java.lang.Exception {
        android.util.AtomicFile sessionsIndexFile = prepareSessionsIndexFile();
        if (sessionsIndexFile == null) {
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Error creating sessions index file");
            return;
        }
        java.io.FileOutputStream fos = null;
        try {
            fos = sessionsIndexFile.startWrite(android.os.SystemClock.uptimeMillis());
            org.xmlpull.v1.XmlSerializer out = new com.android.internal.util.FastXmlSerializer();
            out.setOutput(fos, java.nio.charset.StandardCharsets.UTF_8.name());
            out.startDocument(null, true);
            out.startTag(null, "ss");
            com.android.internal.util.XmlUtils.writeIntAttribute(out, "v", 6);
            int userCount = this.mSessions.size();
            for (int i = 0; i < userCount; i++) {
                android.util.LongSparseArray<com.android.server.blob.BlobStoreSession> userSessions = this.mSessions.valueAt(i);
                int sessionsCount = userSessions.size();
                for (int j = 0; j < sessionsCount; j++) {
                    out.startTag(null, "s");
                    userSessions.valueAt(j).writeToXml(out);
                    out.endTag(null, "s");
                }
            }
            out.endTag(null, "ss");
            out.endDocument();
            sessionsIndexFile.finishWrite(fos);
            if (com.android.server.blob.BlobStoreConfig.LOGV) {
                android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Finished persisting sessions data");
            }
        } catch (java.lang.Exception e) {
            sessionsIndexFile.failWrite(fos);
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Error writing sessions data", e);
            throw e;
        }
    }

    private void readBlobSessionsLocked(android.util.SparseArray<android.util.SparseArray<java.lang.String>> allPackages) {
        java.io.FileInputStream fis;
        org.xmlpull.v1.XmlPullParser in;
        int version;
        com.android.server.blob.BlobStoreSession session;
        if (!com.android.server.blob.BlobStoreConfig.getBlobStoreRootDir().exists()) {
            return;
        }
        android.util.AtomicFile sessionsIndexFile = prepareSessionsIndexFile();
        if (sessionsIndexFile == null) {
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Error creating sessions index file");
            return;
        }
        if (!sessionsIndexFile.exists()) {
            android.util.Slog.w(com.android.server.blob.BlobStoreConfig.TAG, "Sessions index file not available: " + sessionsIndexFile.getBaseFile());
            return;
        }
        this.mSessions.clear();
        try {
            fis = sessionsIndexFile.openRead();
            try {
                in = android.util.Xml.newPullParser();
                in.setInput(fis, java.nio.charset.StandardCharsets.UTF_8.name());
                com.android.internal.util.XmlUtils.beginDocument(in, "ss");
                version = com.android.internal.util.XmlUtils.readIntAttribute(in, "v");
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Error reading sessions data", e);
            return;
        }
        while (true) {
            com.android.internal.util.XmlUtils.nextElement(in);
            if (in.getEventType() == 1) {
                break;
            }
            if ("s".equals(in.getName()) && (session = com.android.server.blob.BlobStoreSession.createFromXml(in, version, this.mContext, this.mSessionStateChangeListener)) != null) {
                android.util.SparseArray<java.lang.String> userPackages = allPackages.get(android.os.UserHandle.getUserId(session.getOwnerUid()));
                if (userPackages != null && session.getOwnerPackageName().equals(userPackages.get(session.getOwnerUid()))) {
                    addSessionForUserLocked(session, android.os.UserHandle.getUserId(session.getOwnerUid()));
                } else {
                    session.getSessionFile().delete();
                }
                this.mCurrentMaxSessionId = java.lang.Math.max(this.mCurrentMaxSessionId, session.getSessionId());
            }
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Error reading sessions data", e);
            return;
        }
        if (com.android.server.blob.BlobStoreConfig.LOGV) {
            android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Finished reading sessions data");
        }
        if (fis != null) {
            fis.close();
        }
    }

    private void writeBlobsInfoLocked() throws java.lang.Exception {
        android.util.AtomicFile blobsIndexFile = prepareBlobsIndexFile();
        if (blobsIndexFile == null) {
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Error creating blobs index file");
            return;
        }
        java.io.FileOutputStream fos = null;
        try {
            fos = blobsIndexFile.startWrite(android.os.SystemClock.uptimeMillis());
            org.xmlpull.v1.XmlSerializer out = new com.android.internal.util.FastXmlSerializer();
            out.setOutput(fos, java.nio.charset.StandardCharsets.UTF_8.name());
            out.startDocument(null, true);
            out.startTag(null, "bs");
            com.android.internal.util.XmlUtils.writeIntAttribute(out, "v", 6);
            int count = this.mBlobsMap.size();
            for (int i = 0; i < count; i++) {
                out.startTag(null, "b");
                this.mBlobsMap.valueAt(i).writeToXml(out);
                out.endTag(null, "b");
            }
            out.endTag(null, "bs");
            out.endDocument();
            blobsIndexFile.finishWrite(fos);
            if (com.android.server.blob.BlobStoreConfig.LOGV) {
                android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Finished persisting blobs data");
            }
        } catch (java.lang.Exception e) {
            blobsIndexFile.failWrite(fos);
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Error writing blobs data", e);
            throw e;
        }
    }

    private void readBlobsInfoLocked(android.util.SparseArray<android.util.SparseArray<java.lang.String>> allPackages) {
        java.io.FileInputStream fis;
        org.xmlpull.v1.XmlPullParser in;
        int version;
        if (!com.android.server.blob.BlobStoreConfig.getBlobStoreRootDir().exists()) {
            return;
        }
        android.util.AtomicFile blobsIndexFile = prepareBlobsIndexFile();
        if (blobsIndexFile == null) {
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Error creating blobs index file");
            return;
        }
        if (!blobsIndexFile.exists()) {
            android.util.Slog.w(com.android.server.blob.BlobStoreConfig.TAG, "Blobs index file not available: " + blobsIndexFile.getBaseFile());
            return;
        }
        this.mBlobsMap.clear();
        try {
            fis = blobsIndexFile.openRead();
            try {
                in = android.util.Xml.newPullParser();
                in.setInput(fis, java.nio.charset.StandardCharsets.UTF_8.name());
                com.android.internal.util.XmlUtils.beginDocument(in, "bs");
                version = com.android.internal.util.XmlUtils.readIntAttribute(in, "v");
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Error reading blobs data", e);
            return;
        }
        while (true) {
            com.android.internal.util.XmlUtils.nextElement(in);
            if (in.getEventType() == 1) {
                break;
            }
            if ("b".equals(in.getName())) {
                com.android.server.blob.BlobMetadata blobMetadata = com.android.server.blob.BlobMetadata.createFromXml(in, version, this.mContext);
                blobMetadata.removeCommittersFromUnknownPkgs(allPackages);
                blobMetadata.removeLeaseesFromUnknownPkgs(allPackages);
                this.mCurrentMaxSessionId = java.lang.Math.max(this.mCurrentMaxSessionId, blobMetadata.getBlobId());
                if (version >= 6) {
                    addBlobLocked(blobMetadata);
                } else {
                    com.android.server.blob.BlobMetadata existingBlobMetadata = this.mBlobsMap.get(blobMetadata.getBlobHandle());
                    if (existingBlobMetadata == null) {
                        addBlobLocked(blobMetadata);
                    } else {
                        existingBlobMetadata.addCommittersAndLeasees(blobMetadata);
                        blobMetadata.getBlobFile().delete();
                    }
                }
            }
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Error reading blobs data", e);
            return;
        }
        if (com.android.server.blob.BlobStoreConfig.LOGV) {
            android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Finished reading blobs data");
        }
        if (fis != null) {
            fis.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeBlobsInfo() {
        synchronized (this.mBlobsLock) {
            try {
                writeBlobsInfoLocked();
            } catch (java.lang.Exception e) {
            }
        }
    }

    private void writeBlobsInfoAsync() {
        if (!this.mHandler.hasCallbacks(this.mSaveBlobsInfoRunnable)) {
            this.mHandler.post(this.mSaveBlobsInfoRunnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeBlobSessions() {
        synchronized (this.mBlobsLock) {
            try {
                writeBlobSessionsLocked();
            } catch (java.lang.Exception e) {
            }
        }
    }

    private void writeBlobSessionsAsync() {
        if (!this.mHandler.hasCallbacks(this.mSaveSessionsRunnable)) {
            this.mHandler.post(this.mSaveSessionsRunnable);
        }
    }

    private android.util.SparseArray<android.util.SparseArray<java.lang.String>> getAllPackages() {
        android.util.SparseArray<android.util.SparseArray<java.lang.String>> allPackages = new android.util.SparseArray<>();
        int[] allUsers = ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getUserIds();
        for (int userId : allUsers) {
            android.util.SparseArray<java.lang.String> userPackages = new android.util.SparseArray<>();
            allPackages.put(userId, userPackages);
            java.util.List<android.content.pm.ApplicationInfo> applicationInfos = this.mPackageManagerInternal.getInstalledApplications(794624L, userId, android.os.Process.myUid());
            int count = applicationInfos.size();
            for (int i = 0; i < count; i++) {
                android.content.pm.ApplicationInfo applicationInfo = applicationInfos.get(i);
                userPackages.put(applicationInfo.uid, applicationInfo.packageName);
            }
        }
        return allPackages;
    }

    private android.util.AtomicFile prepareSessionsIndexFile() {
        java.io.File file = com.android.server.blob.BlobStoreConfig.prepareSessionIndexFile();
        if (file == null) {
            return null;
        }
        return new android.util.AtomicFile(file, "session_index");
    }

    private android.util.AtomicFile prepareBlobsIndexFile() {
        java.io.File file = com.android.server.blob.BlobStoreConfig.prepareBlobsIndexFile();
        if (file == null) {
            return null;
        }
        return new android.util.AtomicFile(file, "blobs_index");
    }

    void handlePackageRemoved(final java.lang.String packageName, final int uid) {
        synchronized (this.mBlobsLock) {
            android.util.LongSparseArray<com.android.server.blob.BlobStoreSession> userSessions = getUserSessionsLocked(android.os.UserHandle.getUserId(uid));
            userSessions.removeIf(new com.android.internal.util.function.LongObjPredicate() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda15
                public final boolean test(long j, java.lang.Object obj) {
                    return this.f$0.lambda$handlePackageRemoved$12(uid, packageName, j, (com.android.server.blob.BlobStoreSession) obj);
                }
            });
            writeBlobSessionsAsync();
            this.mBlobsMap.entrySet().removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda16
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$handlePackageRemoved$13(packageName, uid, (java.util.Map.Entry) obj);
                }
            });
            writeBlobsInfoAsync();
            if (com.android.server.blob.BlobStoreConfig.LOGV) {
                android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Removed blobs data associated with pkg=" + packageName + ", uid=" + uid);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$handlePackageRemoved$12(int uid, java.lang.String packageName, long sessionId, com.android.server.blob.BlobStoreSession blobStoreSession) {
        if (blobStoreSession.getOwnerUid() == uid && blobStoreSession.getOwnerPackageName().equals(packageName)) {
            deleteSessionLocked(blobStoreSession);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$handlePackageRemoved$13(java.lang.String packageName, int uid, java.util.Map.Entry entry) {
        com.android.server.blob.BlobMetadata blobMetadata = (com.android.server.blob.BlobMetadata) entry.getValue();
        boolean isACommitter = blobMetadata.isACommitter(packageName, uid);
        if (isACommitter) {
            blobMetadata.removeCommitter(packageName, uid);
        }
        blobMetadata.removeLeasee(packageName, uid);
        if (blobMetadata.shouldBeDeleted(isACommitter)) {
            deleteBlobLocked(blobMetadata);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUserRemoved(final int userId) {
        synchronized (this.mBlobsLock) {
            android.util.LongSparseArray<com.android.server.blob.BlobStoreSession> userSessions = (android.util.LongSparseArray) this.mSessions.removeReturnOld(userId);
            if (userSessions != null) {
                int count = userSessions.size();
                for (int i = 0; i < count; i++) {
                    com.android.server.blob.BlobStoreSession session = userSessions.valueAt(i);
                    deleteSessionLocked(session);
                }
            }
            this.mBlobsMap.entrySet().removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda6
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$handleUserRemoved$14(userId, (java.util.Map.Entry) obj);
                }
            });
            if (com.android.server.blob.BlobStoreConfig.LOGV) {
                android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Removed blobs data in user " + userId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$handleUserRemoved$14(int userId, java.util.Map.Entry entry) {
        com.android.server.blob.BlobMetadata blobMetadata = (com.android.server.blob.BlobMetadata) entry.getValue();
        blobMetadata.removeDataForUser(userId);
        if (blobMetadata.shouldBeDeleted(true)) {
            deleteBlobLocked(blobMetadata);
            return true;
        }
        return false;
    }

    void handleIdleMaintenanceLocked() {
        final java.util.ArrayList<java.lang.Long> deletedBlobIds = new java.util.ArrayList<>();
        java.util.ArrayList<java.io.File> filesToDelete = new java.util.ArrayList<>();
        java.io.File blobsDir = com.android.server.blob.BlobStoreConfig.getBlobsDir();
        if (blobsDir.exists()) {
            for (java.io.File file : blobsDir.listFiles()) {
                try {
                    long id = java.lang.Long.parseLong(file.getName());
                    if (this.mActiveBlobIds.indexOf(java.lang.Long.valueOf(id)) < 0) {
                        filesToDelete.add(file);
                        deletedBlobIds.add(java.lang.Long.valueOf(id));
                    }
                } catch (java.lang.NumberFormatException e) {
                    android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Error parsing the file name: " + file, e);
                    filesToDelete.add(file);
                }
            }
            int count = filesToDelete.size();
            for (int i = 0; i < count; i++) {
                filesToDelete.get(i).delete();
            }
        }
        this.mBlobsMap.entrySet().removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$handleIdleMaintenanceLocked$15(deletedBlobIds, (java.util.Map.Entry) obj);
            }
        });
        writeBlobsInfoAsync();
        int userCount = this.mSessions.size();
        for (int i2 = 0; i2 < userCount; i2++) {
            android.util.LongSparseArray<com.android.server.blob.BlobStoreSession> userSessions = this.mSessions.valueAt(i2);
            userSessions.removeIf(new com.android.internal.util.function.LongObjPredicate() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda18
                public final boolean test(long j, java.lang.Object obj) {
                    return this.f$0.lambda$handleIdleMaintenanceLocked$16(deletedBlobIds, j, (com.android.server.blob.BlobStoreSession) obj);
                }
            });
        }
        android.util.Slog.d(com.android.server.blob.BlobStoreConfig.TAG, "Completed idle maintenance; deleted " + java.util.Arrays.toString(deletedBlobIds.toArray()));
        writeBlobSessionsAsync();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$handleIdleMaintenanceLocked$15(java.util.ArrayList deletedBlobIds, java.util.Map.Entry entry) {
        com.android.server.blob.BlobMetadata blobMetadata = (com.android.server.blob.BlobMetadata) entry.getValue();
        blobMetadata.removeExpiredLeases();
        if (blobMetadata.shouldBeDeleted(true)) {
            deleteBlobLocked(blobMetadata);
            deletedBlobIds.add(java.lang.Long.valueOf(blobMetadata.getBlobId()));
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$handleIdleMaintenanceLocked$16(java.util.ArrayList deletedBlobIds, long sessionId, com.android.server.blob.BlobStoreSession blobStoreSession) {
        boolean shouldRemove = false;
        if (blobStoreSession.isExpired()) {
            shouldRemove = true;
        }
        if (blobStoreSession.getBlobHandle().isExpired()) {
            shouldRemove = true;
        }
        if (shouldRemove) {
            deleteSessionLocked(blobStoreSession);
            deletedBlobIds.add(java.lang.Long.valueOf(blobStoreSession.getSessionId()));
        }
        return shouldRemove;
    }

    private void deleteSessionLocked(com.android.server.blob.BlobStoreSession blobStoreSession) {
        blobStoreSession.destroy();
        this.mActiveBlobIds.remove(java.lang.Long.valueOf(blobStoreSession.getSessionId()));
    }

    private void deleteBlobLocked(com.android.server.blob.BlobMetadata blobMetadata) {
        blobMetadata.destroy();
        this.mActiveBlobIds.remove(java.lang.Long.valueOf(blobMetadata.getBlobId()));
    }

    void runClearAllSessions(int userId) {
        synchronized (this.mBlobsLock) {
            int userCount = this.mSessions.size();
            for (int i = 0; i < userCount; i++) {
                int sessionUserId = this.mSessions.keyAt(i);
                if (userId == -1 || userId == sessionUserId) {
                    android.util.LongSparseArray<com.android.server.blob.BlobStoreSession> userSessions = this.mSessions.valueAt(i);
                    int sessionsCount = userSessions.size();
                    for (int j = 0; j < sessionsCount; j++) {
                        this.mActiveBlobIds.remove(java.lang.Long.valueOf(userSessions.valueAt(j).getSessionId()));
                    }
                }
            }
            if (userId == -1) {
                this.mSessions.clear();
            } else {
                this.mSessions.remove(userId);
            }
            writeBlobSessionsAsync();
        }
    }

    void runClearAllBlobs(final int userId) {
        synchronized (this.mBlobsLock) {
            this.mBlobsMap.entrySet().removeIf(new java.util.function.Predicate() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda7
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$runClearAllBlobs$17(userId, (java.util.Map.Entry) obj);
                }
            });
            writeBlobsInfoAsync();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$runClearAllBlobs$17(int userId, java.util.Map.Entry entry) {
        com.android.server.blob.BlobMetadata blobMetadata = (com.android.server.blob.BlobMetadata) entry.getValue();
        if (userId == -1) {
            this.mActiveBlobIds.remove(java.lang.Long.valueOf(blobMetadata.getBlobId()));
            return true;
        }
        blobMetadata.removeDataForUser(userId);
        if (!blobMetadata.shouldBeDeleted(false)) {
            return false;
        }
        this.mActiveBlobIds.remove(java.lang.Long.valueOf(blobMetadata.getBlobId()));
        return true;
    }

    void deleteBlob(android.app.blob.BlobHandle blobHandle, int userId) {
        synchronized (this.mBlobsLock) {
            com.android.server.blob.BlobMetadata blobMetadata = this.mBlobsMap.get(blobHandle);
            if (blobMetadata == null) {
                return;
            }
            blobMetadata.removeDataForUser(userId);
            if (blobMetadata.shouldBeDeleted(false)) {
                deleteBlobLocked(blobMetadata);
                this.mBlobsMap.remove(blobHandle);
            }
            writeBlobsInfoAsync();
        }
    }

    void runIdleMaintenance() {
        synchronized (this.mBlobsLock) {
            handleIdleMaintenanceLocked();
        }
    }

    boolean isBlobAvailable(long blobId, int userId) {
        synchronized (this.mBlobsLock) {
            int blobCount = this.mBlobsMap.size();
            for (int i = 0; i < blobCount; i++) {
                com.android.server.blob.BlobMetadata blobMetadata = this.mBlobsMap.valueAt(i);
                if (blobMetadata.getBlobId() == blobId) {
                    return blobMetadata.hasACommitterInUser(userId);
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpSessionsLocked(android.util.IndentingPrintWriter fout, com.android.server.blob.BlobStoreManagerService.DumpArgs dumpArgs) {
        int userCount = this.mSessions.size();
        for (int i = 0; i < userCount; i++) {
            int userId = this.mSessions.keyAt(i);
            if (dumpArgs.shouldDumpUser(userId)) {
                android.util.LongSparseArray<com.android.server.blob.BlobStoreSession> userSessions = this.mSessions.valueAt(i);
                fout.println("List of sessions in user #" + userId + " (" + userSessions.size() + "):");
                fout.increaseIndent();
                int sessionsCount = userSessions.size();
                for (int j = 0; j < sessionsCount; j++) {
                    long sessionId = userSessions.keyAt(j);
                    com.android.server.blob.BlobStoreSession session = userSessions.valueAt(j);
                    if (dumpArgs.shouldDumpSession(session.getOwnerPackageName(), session.getOwnerUid(), session.getSessionId())) {
                        fout.println("Session #" + sessionId);
                        fout.increaseIndent();
                        session.dump(fout, dumpArgs);
                        fout.decreaseIndent();
                    }
                }
                fout.decreaseIndent();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpBlobsLocked(android.util.IndentingPrintWriter fout, com.android.server.blob.BlobStoreManagerService.DumpArgs dumpArgs) {
        fout.println("List of blobs (" + this.mBlobsMap.size() + "):");
        fout.increaseIndent();
        int blobCount = this.mBlobsMap.size();
        for (int i = 0; i < blobCount; i++) {
            com.android.server.blob.BlobMetadata blobMetadata = this.mBlobsMap.valueAt(i);
            if (dumpArgs.shouldDumpBlob(blobMetadata.getBlobId())) {
                fout.println("Blob #" + blobMetadata.getBlobId());
                fout.increaseIndent();
                blobMetadata.dump(fout, dumpArgs);
                fout.decreaseIndent();
            }
        }
        if (this.mBlobsMap.isEmpty()) {
            fout.println("<empty>");
        }
        fout.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    class BlobStorageStatsAugmenter implements com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter {
        private BlobStorageStatsAugmenter() {
        }

        @Override // com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter
        public void augmentStatsForPackageForUser(android.content.pm.PackageStats stats, final java.lang.String packageName, final android.os.UserHandle userHandle, final boolean callerHasStatsPermission) {
            final java.util.concurrent.atomic.AtomicLong blobsDataSize = new java.util.concurrent.atomic.AtomicLong(0L);
            com.android.server.blob.BlobStoreManagerService.this.forEachSessionInUser(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$BlobStorageStatsAugmenter$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.blob.BlobStoreManagerService.BlobStorageStatsAugmenter.lambda$augmentStatsForPackageForUser$0(packageName, blobsDataSize, (com.android.server.blob.BlobStoreSession) obj);
                }
            }, userHandle.getIdentifier());
            com.android.server.blob.BlobStoreManagerService.this.forEachBlob(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$BlobStorageStatsAugmenter$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.blob.BlobStoreManagerService.BlobStorageStatsAugmenter.lambda$augmentStatsForPackageForUser$1(packageName, userHandle, callerHasStatsPermission, blobsDataSize, (com.android.server.blob.BlobMetadata) obj);
                }
            });
            stats.dataSize += blobsDataSize.get();
        }

        static /* synthetic */ void lambda$augmentStatsForPackageForUser$0(java.lang.String packageName, java.util.concurrent.atomic.AtomicLong blobsDataSize, com.android.server.blob.BlobStoreSession session) {
            if (session.getOwnerPackageName().equals(packageName)) {
                blobsDataSize.getAndAdd(session.getSize());
            }
        }

        static /* synthetic */ void lambda$augmentStatsForPackageForUser$1(java.lang.String packageName, android.os.UserHandle userHandle, boolean callerHasStatsPermission, java.util.concurrent.atomic.AtomicLong blobsDataSize, com.android.server.blob.BlobMetadata blobMetadata) {
            if (blobMetadata.shouldAttributeToLeasee(packageName, userHandle.getIdentifier(), callerHasStatsPermission)) {
                blobsDataSize.getAndAdd(blobMetadata.getSize());
            }
        }

        @Override // com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter
        public void augmentStatsForUid(android.content.pm.PackageStats stats, final int uid, final boolean callerHasStatsPermission) {
            int userId = android.os.UserHandle.getUserId(uid);
            final java.util.concurrent.atomic.AtomicLong blobsDataSize = new java.util.concurrent.atomic.AtomicLong(0L);
            com.android.server.blob.BlobStoreManagerService.this.forEachSessionInUser(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$BlobStorageStatsAugmenter$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.blob.BlobStoreManagerService.BlobStorageStatsAugmenter.lambda$augmentStatsForUid$2(uid, blobsDataSize, (com.android.server.blob.BlobStoreSession) obj);
                }
            }, userId);
            com.android.server.blob.BlobStoreManagerService.this.forEachBlob(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$BlobStorageStatsAugmenter$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.blob.BlobStoreManagerService.BlobStorageStatsAugmenter.lambda$augmentStatsForUid$3(uid, callerHasStatsPermission, blobsDataSize, (com.android.server.blob.BlobMetadata) obj);
                }
            });
            stats.dataSize += blobsDataSize.get();
        }

        static /* synthetic */ void lambda$augmentStatsForUid$2(int uid, java.util.concurrent.atomic.AtomicLong blobsDataSize, com.android.server.blob.BlobStoreSession session) {
            if (session.getOwnerUid() == uid) {
                blobsDataSize.getAndAdd(session.getSize());
            }
        }

        static /* synthetic */ void lambda$augmentStatsForUid$3(int uid, boolean callerHasStatsPermission, java.util.concurrent.atomic.AtomicLong blobsDataSize, com.android.server.blob.BlobMetadata blobMetadata) {
            if (blobMetadata.shouldAttributeToLeasee(uid, callerHasStatsPermission)) {
                blobsDataSize.getAndAdd(blobMetadata.getSize());
            }
        }

        @Override // com.android.server.usage.StorageStatsManagerLocal.StorageStatsAugmenter
        public void augmentStatsForUser(android.content.pm.PackageStats stats, final android.os.UserHandle userHandle) {
            final java.util.concurrent.atomic.AtomicLong blobsDataSize = new java.util.concurrent.atomic.AtomicLong(0L);
            com.android.server.blob.BlobStoreManagerService.this.forEachSessionInUser(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$BlobStorageStatsAugmenter$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    blobsDataSize.getAndAdd(((com.android.server.blob.BlobStoreSession) obj).getSize());
                }
            }, userHandle.getIdentifier());
            com.android.server.blob.BlobStoreManagerService.this.forEachBlob(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$BlobStorageStatsAugmenter$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.blob.BlobStoreManagerService.BlobStorageStatsAugmenter.lambda$augmentStatsForUser$5(userHandle, blobsDataSize, (com.android.server.blob.BlobMetadata) obj);
                }
            });
            stats.dataSize += blobsDataSize.get();
        }

        static /* synthetic */ void lambda$augmentStatsForUser$5(android.os.UserHandle userHandle, java.util.concurrent.atomic.AtomicLong blobsDataSize, com.android.server.blob.BlobMetadata blobMetadata) {
            if (blobMetadata.shouldAttributeToUser(userHandle.getIdentifier())) {
                blobsDataSize.getAndAdd(blobMetadata.getSize());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forEachSessionInUser(java.util.function.Consumer<com.android.server.blob.BlobStoreSession> consumer, int userId) {
        synchronized (this.mBlobsLock) {
            android.util.LongSparseArray<com.android.server.blob.BlobStoreSession> userSessions = getUserSessionsLocked(userId);
            int count = userSessions.size();
            for (int i = 0; i < count; i++) {
                com.android.server.blob.BlobStoreSession session = userSessions.valueAt(i);
                consumer.accept(session);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forEachBlob(java.util.function.Consumer<com.android.server.blob.BlobMetadata> consumer) {
        synchronized (this.mBlobsMap) {
            forEachBlobLocked(consumer);
        }
    }

    private void forEachBlobLocked(java.util.function.Consumer<com.android.server.blob.BlobMetadata> consumer) {
        int count = this.mBlobsMap.size();
        for (int blobIdx = 0; blobIdx < count; blobIdx++) {
            com.android.server.blob.BlobMetadata blobMetadata = this.mBlobsMap.valueAt(blobIdx);
            consumer.accept(blobMetadata);
        }
    }

    private void forEachBlobLocked(java.util.function.BiConsumer<android.app.blob.BlobHandle, com.android.server.blob.BlobMetadata> consumer) {
        int count = this.mBlobsMap.size();
        for (int blobIdx = 0; blobIdx < count; blobIdx++) {
            android.app.blob.BlobHandle blobHandle = this.mBlobsMap.keyAt(blobIdx);
            com.android.server.blob.BlobMetadata blobMetadata = this.mBlobsMap.valueAt(blobIdx);
            consumer.accept(blobHandle, blobMetadata);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAllowedBlobStoreAccess(int uid, java.lang.String packageName) {
        return (android.os.Process.isSdkSandboxUid(uid) || android.os.Process.isIsolated(uid) || this.mPackageManagerInternal.isInstantApp(packageName, android.os.UserHandle.getUserId(uid))) ? false : true;
    }

    private class PackageChangedReceiver extends android.content.BroadcastReceiver {
        private PackageChangedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            byte b;
            if (com.android.server.blob.BlobStoreConfig.LOGV) {
                android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Received " + intent);
            }
            java.lang.String action = intent.getAction();
            switch (action.hashCode()) {
                case 267468725:
                    b = !action.equals("android.intent.action.PACKAGE_DATA_CLEARED") ? (byte) -1 : (byte) 1;
                    break;
                case 1580442797:
                    b = !action.equals("android.intent.action.PACKAGE_FULLY_REMOVED") ? (byte) -1 : (byte) 0;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                case 1:
                    java.lang.String packageName = intent.getData().getSchemeSpecificPart();
                    if (packageName == null) {
                        android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Package name is missing in the intent: " + intent);
                    } else {
                        int uid = intent.getIntExtra("android.intent.extra.UID", -1);
                        if (uid == -1) {
                            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "uid is missing in the intent: " + intent);
                        } else {
                            com.android.server.blob.BlobStoreManagerService.this.handlePackageRemoved(packageName, uid);
                        }
                    }
                    break;
                default:
                    android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Received unknown intent: " + intent);
                    break;
            }
        }
    }

    private class UserActionReceiver extends android.content.BroadcastReceiver {
        private UserActionReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            byte b;
            if (com.android.server.blob.BlobStoreConfig.LOGV) {
                android.util.Slog.v(com.android.server.blob.BlobStoreConfig.TAG, "Received: " + intent);
            }
            java.lang.String action = intent.getAction();
            switch (action.hashCode()) {
                case -2061058799:
                    if (action.equals("android.intent.action.USER_REMOVED")) {
                        b = 0;
                        break;
                    }
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                    if (userId == -10000) {
                        android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "userId is missing in the intent: " + intent);
                    } else {
                        com.android.server.blob.BlobStoreManagerService.this.handleUserRemoved(userId);
                    }
                    break;
                default:
                    android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Received unknown intent: " + intent);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class Stub extends android.app.blob.IBlobStoreManager.Stub {
        private Stub() {
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
        public long createSession(android.app.blob.BlobHandle blobHandle, java.lang.String packageName) throws android.os.ParcelableException {
            java.util.Objects.requireNonNull(blobHandle, "blobHandle must not be null");
            blobHandle.assertIsValid();
            java.util.Objects.requireNonNull(packageName, "packageName must not be null");
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.blob.BlobStoreManagerService.this.verifyCallingPackage(callingUid, packageName);
            if (!com.android.server.blob.BlobStoreManagerService.this.isAllowedBlobStoreAccess(callingUid, packageName)) {
                throw new java.lang.SecurityException("Caller not allowed to create session; callingUid=" + callingUid + ", callingPackage=" + packageName);
            }
            try {
                return com.android.server.blob.BlobStoreManagerService.this.createSessionInternal(blobHandle, callingUid, packageName);
            } catch (android.os.LimitExceededException e) {
                throw new android.os.ParcelableException(e);
            }
        }

        public android.app.blob.IBlobStoreSession openSession(long sessionId, java.lang.String packageName) {
            com.android.internal.util.Preconditions.checkArgumentPositive(sessionId, "sessionId must be positive: " + sessionId);
            java.util.Objects.requireNonNull(packageName, "packageName must not be null");
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.blob.BlobStoreManagerService.this.verifyCallingPackage(callingUid, packageName);
            return com.android.server.blob.BlobStoreManagerService.this.openSessionInternal(sessionId, callingUid, packageName);
        }

        public void abandonSession(long sessionId, java.lang.String packageName) {
            com.android.internal.util.Preconditions.checkArgumentPositive(sessionId, "sessionId must be positive: " + sessionId);
            java.util.Objects.requireNonNull(packageName, "packageName must not be null");
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.blob.BlobStoreManagerService.this.verifyCallingPackage(callingUid, packageName);
            com.android.server.blob.BlobStoreManagerService.this.abandonSessionInternal(sessionId, callingUid, packageName);
        }

        public android.os.ParcelFileDescriptor openBlob(android.app.blob.BlobHandle blobHandle, java.lang.String packageName) {
            java.util.Objects.requireNonNull(blobHandle, "blobHandle must not be null");
            blobHandle.assertIsValid();
            java.util.Objects.requireNonNull(packageName, "packageName must not be null");
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.blob.BlobStoreManagerService.this.verifyCallingPackage(callingUid, packageName);
            if (!com.android.server.blob.BlobStoreManagerService.this.isAllowedBlobStoreAccess(callingUid, packageName)) {
                throw new java.lang.SecurityException("Caller not allowed to open blob; callingUid=" + callingUid + ", callingPackage=" + packageName);
            }
            try {
                return com.android.server.blob.BlobStoreManagerService.this.openBlobInternal(blobHandle, callingUid, packageName);
            } catch (java.io.IOException e) {
                throw android.util.ExceptionUtils.wrap(e);
            }
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
        public void acquireLease(android.app.blob.BlobHandle blobHandle, int descriptionResId, java.lang.CharSequence description, long leaseExpiryTimeMillis, java.lang.String packageName) throws android.os.ParcelableException {
            java.util.Objects.requireNonNull(blobHandle, "blobHandle must not be null");
            blobHandle.assertIsValid();
            com.android.internal.util.Preconditions.checkArgument(android.content.res.ResourceId.isValid(descriptionResId) || description != null, "Description must be valid; descriptionId=" + descriptionResId + ", description=" + ((java.lang.Object) description));
            com.android.internal.util.Preconditions.checkArgumentNonnegative(leaseExpiryTimeMillis, "leaseExpiryTimeMillis must not be negative");
            java.util.Objects.requireNonNull(packageName, "packageName must not be null");
            java.lang.CharSequence description2 = com.android.server.blob.BlobStoreConfig.getTruncatedLeaseDescription(description);
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.blob.BlobStoreManagerService.this.verifyCallingPackage(callingUid, packageName);
            if (!com.android.server.blob.BlobStoreManagerService.this.isAllowedBlobStoreAccess(callingUid, packageName)) {
                throw new java.lang.SecurityException("Caller not allowed to open blob; callingUid=" + callingUid + ", callingPackage=" + packageName);
            }
            try {
                com.android.server.blob.BlobStoreManagerService.this.acquireLeaseInternal(blobHandle, descriptionResId, description2, leaseExpiryTimeMillis, callingUid, packageName);
            } catch (android.content.res.Resources.NotFoundException e) {
                throw new java.lang.IllegalArgumentException(e);
            } catch (android.os.LimitExceededException e2) {
                throw new android.os.ParcelableException(e2);
            }
        }

        public void releaseLease(android.app.blob.BlobHandle blobHandle, java.lang.String packageName) {
            java.util.Objects.requireNonNull(blobHandle, "blobHandle must not be null");
            blobHandle.assertIsValid();
            java.util.Objects.requireNonNull(packageName, "packageName must not be null");
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.blob.BlobStoreManagerService.this.verifyCallingPackage(callingUid, packageName);
            if (!com.android.server.blob.BlobStoreManagerService.this.isAllowedBlobStoreAccess(callingUid, packageName)) {
                throw new java.lang.SecurityException("Caller not allowed to open blob; callingUid=" + callingUid + ", callingPackage=" + packageName);
            }
            com.android.server.blob.BlobStoreManagerService.this.releaseLeaseInternal(blobHandle, callingUid, packageName);
        }

        public void releaseAllLeases(java.lang.String packageName) {
            java.util.Objects.requireNonNull(packageName, "packageName must not be null");
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.blob.BlobStoreManagerService.this.verifyCallingPackage(callingUid, packageName);
            if (!com.android.server.blob.BlobStoreManagerService.this.isAllowedBlobStoreAccess(callingUid, packageName)) {
                throw new java.lang.SecurityException("Caller not allowed to open blob; callingUid=" + callingUid + ", callingPackage=" + packageName);
            }
            com.android.server.blob.BlobStoreManagerService.this.releaseAllLeasesInternal(callingUid, packageName);
        }

        public long getRemainingLeaseQuotaBytes(java.lang.String packageName) {
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.blob.BlobStoreManagerService.this.verifyCallingPackage(callingUid, packageName);
            return com.android.server.blob.BlobStoreManagerService.this.getRemainingLeaseQuotaBytesInternal(callingUid, packageName);
        }

        public void waitForIdle(final android.os.RemoteCallback remoteCallback) {
            java.util.Objects.requireNonNull(remoteCallback, "remoteCallback must not be null");
            com.android.server.blob.BlobStoreManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DUMP", "Caller is not allowed to call this; caller=" + android.os.Binder.getCallingUid());
            com.android.server.blob.BlobStoreManagerService.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.blob.BlobStoreManagerService$Stub$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$waitForIdle$1(remoteCallback);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$waitForIdle$1(final android.os.RemoteCallback remoteCallback) {
            com.android.server.blob.BlobStoreManagerService.this.mBackgroundHandler.post(new java.lang.Runnable() { // from class: com.android.server.blob.BlobStoreManagerService$Stub$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$waitForIdle$0(remoteCallback);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$waitForIdle$0(final android.os.RemoteCallback remoteCallback) {
            android.os.Handler handler = com.android.server.blob.BlobStoreManagerService.this.mHandler;
            java.util.Objects.requireNonNull(remoteCallback);
            handler.post(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$Stub$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    remoteCallback.sendResult((android.os.Bundle) obj);
                }
            }, (java.lang.Object) null).recycleOnUse());
        }

        public java.util.List<android.app.blob.BlobInfo> queryBlobsForUser(int userId) {
            verifyCallerIsSystemUid("queryBlobsForUser");
            int resolvedUserId = userId == -2 ? android.app.ActivityManager.getCurrentUser() : userId;
            android.app.ActivityManagerInternal amInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            amInternal.ensureNotSpecialUser(resolvedUserId);
            return com.android.server.blob.BlobStoreManagerService.this.queryBlobsForUserInternal(resolvedUserId);
        }

        public void deleteBlob(long blobId) {
            verifyCallerIsSystemUid("deleteBlob");
            com.android.server.blob.BlobStoreManagerService.this.deleteBlobInternal(blobId);
        }

        public java.util.List<android.app.blob.BlobHandle> getLeasedBlobs(java.lang.String packageName) {
            java.util.Objects.requireNonNull(packageName, "packageName must not be null");
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.blob.BlobStoreManagerService.this.verifyCallingPackage(callingUid, packageName);
            return com.android.server.blob.BlobStoreManagerService.this.getLeasedBlobsInternal(callingUid, packageName);
        }

        public android.app.blob.LeaseInfo getLeaseInfo(android.app.blob.BlobHandle blobHandle, java.lang.String packageName) {
            java.util.Objects.requireNonNull(blobHandle, "blobHandle must not be null");
            blobHandle.assertIsValid();
            java.util.Objects.requireNonNull(packageName, "packageName must not be null");
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.blob.BlobStoreManagerService.this.verifyCallingPackage(callingUid, packageName);
            if (!com.android.server.blob.BlobStoreManagerService.this.isAllowedBlobStoreAccess(callingUid, packageName)) {
                throw new java.lang.SecurityException("Caller not allowed to open blob; callingUid=" + callingUid + ", callingPackage=" + packageName);
            }
            return com.android.server.blob.BlobStoreManagerService.this.getLeaseInfoInternal(blobHandle, callingUid, packageName);
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(com.android.server.blob.BlobStoreManagerService.this.mContext, com.android.server.blob.BlobStoreConfig.TAG, writer)) {
                com.android.server.blob.BlobStoreManagerService.DumpArgs dumpArgs = com.android.server.blob.BlobStoreManagerService.DumpArgs.parse(args);
                android.util.IndentingPrintWriter fout = new android.util.IndentingPrintWriter(writer, "    ");
                if (dumpArgs.shouldDumpHelp()) {
                    writer.println("dumpsys blob_store [options]:");
                    fout.increaseIndent();
                    dumpArgs.dumpArgsUsage(fout);
                    fout.decreaseIndent();
                    return;
                }
                synchronized (com.android.server.blob.BlobStoreManagerService.this.mBlobsLock) {
                    if (dumpArgs.shouldDumpAllSections()) {
                        fout.println("mCurrentMaxSessionId: " + com.android.server.blob.BlobStoreManagerService.this.mCurrentMaxSessionId);
                        fout.println();
                    }
                    if (dumpArgs.shouldDumpSessions()) {
                        com.android.server.blob.BlobStoreManagerService.this.dumpSessionsLocked(fout, dumpArgs);
                        fout.println();
                    }
                    if (dumpArgs.shouldDumpBlobs()) {
                        com.android.server.blob.BlobStoreManagerService.this.dumpBlobsLocked(fout, dumpArgs);
                        fout.println();
                    }
                }
                if (dumpArgs.shouldDumpConfig()) {
                    fout.println("BlobStore config:");
                    fout.increaseIndent();
                    com.android.server.blob.BlobStoreConfig.dump(fout, com.android.server.blob.BlobStoreManagerService.this.mContext);
                    fout.decreaseIndent();
                    fout.println();
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
            return new com.android.server.blob.BlobStoreManagerShellCommand(com.android.server.blob.BlobStoreManagerService.this).exec(this, in.getFileDescriptor(), out.getFileDescriptor(), err.getFileDescriptor(), args);
        }

        private void verifyCallerIsSystemUid(java.lang.String operation) {
            if (android.os.UserHandle.getCallingAppId() != 1000 || !((android.os.UserManager) com.android.server.blob.BlobStoreManagerService.this.mContext.getSystemService(android.os.UserManager.class)).isUserAdmin(android.os.UserHandle.getCallingUserId())) {
                throw new java.lang.SecurityException("Only admin user's app with system uidare allowed to call #" + operation);
            }
        }
    }

    static final class DumpArgs {
        private static final int FLAG_DUMP_BLOBS = 2;
        private static final int FLAG_DUMP_CONFIG = 4;
        private static final int FLAG_DUMP_SESSIONS = 1;
        private boolean mDumpAll;
        private boolean mDumpHelp;
        private boolean mDumpUnredacted;
        private int mSelectedSectionFlags;
        private final java.util.ArrayList<java.lang.String> mDumpPackages = new java.util.ArrayList<>();
        private final java.util.ArrayList<java.lang.Integer> mDumpUids = new java.util.ArrayList<>();
        private final java.util.ArrayList<java.lang.Integer> mDumpUserIds = new java.util.ArrayList<>();
        private final java.util.ArrayList<java.lang.Long> mDumpBlobIds = new java.util.ArrayList<>();

        public boolean shouldDumpSession(java.lang.String packageName, int uid, long blobId) {
            if (!com.android.internal.util.CollectionUtils.isEmpty(this.mDumpPackages) && this.mDumpPackages.indexOf(packageName) < 0) {
                return false;
            }
            if (com.android.internal.util.CollectionUtils.isEmpty(this.mDumpUids) || this.mDumpUids.indexOf(java.lang.Integer.valueOf(uid)) >= 0) {
                return com.android.internal.util.CollectionUtils.isEmpty(this.mDumpBlobIds) || this.mDumpBlobIds.indexOf(java.lang.Long.valueOf(blobId)) >= 0;
            }
            return false;
        }

        public boolean shouldDumpAllSections() {
            return this.mDumpAll || this.mSelectedSectionFlags == 0;
        }

        public void allowDumpSessions() {
            this.mSelectedSectionFlags |= 1;
        }

        public boolean shouldDumpSessions() {
            return shouldDumpAllSections() || (this.mSelectedSectionFlags & 1) != 0;
        }

        public void allowDumpBlobs() {
            this.mSelectedSectionFlags |= 2;
        }

        public boolean shouldDumpBlobs() {
            return shouldDumpAllSections() || (this.mSelectedSectionFlags & 2) != 0;
        }

        public void allowDumpConfig() {
            this.mSelectedSectionFlags |= 4;
        }

        public boolean shouldDumpConfig() {
            return shouldDumpAllSections() || (this.mSelectedSectionFlags & 4) != 0;
        }

        public boolean shouldDumpBlob(long blobId) {
            return com.android.internal.util.CollectionUtils.isEmpty(this.mDumpBlobIds) || this.mDumpBlobIds.indexOf(java.lang.Long.valueOf(blobId)) >= 0;
        }

        public boolean shouldDumpFull() {
            return this.mDumpUnredacted;
        }

        public boolean shouldDumpUser(int userId) {
            return com.android.internal.util.CollectionUtils.isEmpty(this.mDumpUserIds) || this.mDumpUserIds.indexOf(java.lang.Integer.valueOf(userId)) >= 0;
        }

        public boolean shouldDumpHelp() {
            return this.mDumpHelp;
        }

        private DumpArgs() {
        }

        public static com.android.server.blob.BlobStoreManagerService.DumpArgs parse(java.lang.String[] args) {
            com.android.server.blob.BlobStoreManagerService.DumpArgs dumpArgs = new com.android.server.blob.BlobStoreManagerService.DumpArgs();
            if (args == null) {
                return dumpArgs;
            }
            int i = 0;
            while (i < args.length) {
                java.lang.String opt = args[i];
                if ("--all".equals(opt) || "-a".equals(opt)) {
                    dumpArgs.mDumpAll = true;
                } else if ("--unredacted".equals(opt) || "-u".equals(opt)) {
                    int callingUid = android.os.Binder.getCallingUid();
                    if (callingUid == 2000 || callingUid == 0) {
                        dumpArgs.mDumpUnredacted = true;
                    }
                } else if ("--sessions".equals(opt)) {
                    dumpArgs.allowDumpSessions();
                } else if ("--blobs".equals(opt)) {
                    dumpArgs.allowDumpBlobs();
                } else if ("--config".equals(opt)) {
                    dumpArgs.allowDumpConfig();
                } else if ("--package".equals(opt) || "-p".equals(opt)) {
                    i++;
                    dumpArgs.mDumpPackages.add(getStringArgRequired(args, i, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME));
                } else if ("--uid".equals(opt)) {
                    i++;
                    dumpArgs.mDumpUids.add(java.lang.Integer.valueOf(getIntArgRequired(args, i, "uid")));
                } else if ("--user".equals(opt)) {
                    i++;
                    dumpArgs.mDumpUserIds.add(java.lang.Integer.valueOf(getIntArgRequired(args, i, "userId")));
                } else if ("--blob".equals(opt) || "-b".equals(opt)) {
                    i++;
                    dumpArgs.mDumpBlobIds.add(java.lang.Long.valueOf(getLongArgRequired(args, i, "blobId")));
                } else if ("--help".equals(opt) || "-h".equals(opt)) {
                    dumpArgs.mDumpHelp = true;
                } else {
                    dumpArgs.mDumpBlobIds.add(java.lang.Long.valueOf(getLongArgRequired(args, i, "blobId")));
                }
                i++;
            }
            return dumpArgs;
        }

        private static java.lang.String getStringArgRequired(java.lang.String[] args, int index, java.lang.String argName) {
            if (index >= args.length) {
                throw new java.lang.IllegalArgumentException("Missing " + argName);
            }
            return args[index];
        }

        private static int getIntArgRequired(java.lang.String[] args, int index, java.lang.String argName) {
            if (index >= args.length) {
                throw new java.lang.IllegalArgumentException("Missing " + argName);
            }
            try {
                int value = java.lang.Integer.parseInt(args[index]);
                return value;
            } catch (java.lang.NumberFormatException e) {
                throw new java.lang.IllegalArgumentException("Invalid " + argName + ": " + args[index]);
            }
        }

        private static long getLongArgRequired(java.lang.String[] args, int index, java.lang.String argName) {
            if (index >= args.length) {
                throw new java.lang.IllegalArgumentException("Missing " + argName);
            }
            try {
                long value = java.lang.Long.parseLong(args[index]);
                return value;
            } catch (java.lang.NumberFormatException e) {
                throw new java.lang.IllegalArgumentException("Invalid " + argName + ": " + args[index]);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dumpArgsUsage(android.util.IndentingPrintWriter pw) {
            pw.println("--help | -h");
            printWithIndent(pw, "Dump this help text");
            pw.println("--sessions");
            printWithIndent(pw, "Dump only the sessions info");
            pw.println("--blobs");
            printWithIndent(pw, "Dump only the committed blobs info");
            pw.println("--config");
            printWithIndent(pw, "Dump only the config values");
            pw.println("--package | -p [package-name]");
            printWithIndent(pw, "Dump blobs info associated with the given package");
            pw.println("--uid | -u [uid]");
            printWithIndent(pw, "Dump blobs info associated with the given uid");
            pw.println("--user [user-id]");
            printWithIndent(pw, "Dump blobs info in the given user");
            pw.println("--blob | -b [session-id | blob-id]");
            printWithIndent(pw, "Dump blob info corresponding to the given ID");
            pw.println("--full | -f");
            printWithIndent(pw, "Dump full unredacted blobs data");
        }

        private void printWithIndent(android.util.IndentingPrintWriter pw, java.lang.String str) {
            pw.increaseIndent();
            pw.println(str);
            pw.decreaseIndent();
        }
    }

    private void registerBlobStorePuller() {
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.BLOB_INFO, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this.mStatsCallbackImpl);
    }

    private class StatsPullAtomCallbackImpl implements android.app.StatsManager.StatsPullAtomCallback {
        private StatsPullAtomCallbackImpl() {
        }

        public int onPullAtom(int atomTag, java.util.List<android.util.StatsEvent> data) {
            switch (atomTag) {
                case com.android.internal.util.FrameworkStatsLog.BLOB_INFO /* 10081 */:
                    return com.android.server.blob.BlobStoreManagerService.this.pullBlobData(atomTag, data);
                default:
                    throw new java.lang.UnsupportedOperationException("Unknown tagId=" + atomTag);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullBlobData(final int atomTag, final java.util.List<android.util.StatsEvent> data) {
        forEachBlob(new java.util.function.Consumer() { // from class: com.android.server.blob.BlobStoreManagerService$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                data.add(((com.android.server.blob.BlobMetadata) obj).dumpAsStatsEvent(atomTag));
            }
        });
        return 0;
    }

    private class LocalService extends com.android.server.blob.BlobStoreManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.blob.BlobStoreManagerInternal
        public void onIdleMaintenance() {
            com.android.server.blob.BlobStoreManagerService.this.runIdleMaintenance();
        }
    }

    static class Injector {
        Injector() {
        }

        public android.os.Handler initializeMessageHandler() {
            return com.android.server.blob.BlobStoreManagerService.initializeMessageHandler();
        }

        public android.os.Handler getBackgroundHandler() {
            return com.android.internal.os.BackgroundThread.getHandler();
        }
    }
}
