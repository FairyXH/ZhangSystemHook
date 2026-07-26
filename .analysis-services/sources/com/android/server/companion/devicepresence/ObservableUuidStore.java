package com.android.server.companion.devicepresence;

/* JADX INFO: loaded from: classes.dex */
public class ObservableUuidStore {
    private static final java.lang.String FILE_NAME = "observing_uuids_presence.xml";
    private static final int READ_FROM_DISK_TIMEOUT = 5;
    private static final java.lang.String TAG = "CDM_ObservableUuidStore";
    private static final java.lang.String XML_ATTR_PACKAGE = "package_name";
    private static final java.lang.String XML_ATTR_TIME_APPROVED = "time_approved";
    private static final java.lang.String XML_ATTR_USER_ID = "user_id";
    private static final java.lang.String XML_ATTR_UUID = "uuid";
    private static final java.lang.String XML_TAG_UUID = "uuid";
    private static final java.lang.String XML_TAG_UUIDS = "uuids";
    private final java.util.concurrent.ConcurrentMap<java.lang.Integer, android.util.AtomicFile> mUserIdToStorageFile = new java.util.concurrent.ConcurrentHashMap();
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<java.util.List<com.android.server.companion.devicepresence.ObservableUuid>> mCachedPerUser = new android.util.SparseArray<>();
    private final java.util.concurrent.ExecutorService mExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();

    public void removeObservableUuid(final int userId, final android.os.ParcelUuid uuid, final java.lang.String packageName) {
        final java.util.List<com.android.server.companion.devicepresence.ObservableUuid> cachedObservableUuids;
        android.util.Slog.i(TAG, "Removing uuid=[" + uuid.getUuid() + "] from store...");
        synchronized (this.mLock) {
            cachedObservableUuids = readObservableUuidsFromCache(userId);
            cachedObservableUuids.removeIf(new java.util.function.Predicate() { // from class: com.android.server.companion.devicepresence.ObservableUuidStore$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.devicepresence.ObservableUuidStore.lambda$removeObservableUuid$0(packageName, uuid, (com.android.server.companion.devicepresence.ObservableUuid) obj);
                }
            });
            this.mCachedPerUser.set(userId, cachedObservableUuids);
        }
        this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.companion.devicepresence.ObservableUuidStore$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$removeObservableUuid$1(userId, cachedObservableUuids);
            }
        });
    }

    static /* synthetic */ boolean lambda$removeObservableUuid$0(java.lang.String packageName, android.os.ParcelUuid uuid, com.android.server.companion.devicepresence.ObservableUuid uuid1) {
        return uuid1.getPackageName().equals(packageName) && uuid1.getUuid().equals(uuid);
    }

    public void writeObservableUuid(final int userId, final com.android.server.companion.devicepresence.ObservableUuid uuid) {
        final java.util.List<com.android.server.companion.devicepresence.ObservableUuid> cachedObservableUuids;
        android.util.Slog.i(TAG, "Writing uuid=[" + uuid.getUuid() + "] to store...");
        synchronized (this.mLock) {
            cachedObservableUuids = readObservableUuidsFromCache(userId);
            cachedObservableUuids.removeIf(new java.util.function.Predicate() { // from class: com.android.server.companion.devicepresence.ObservableUuidStore$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.devicepresence.ObservableUuidStore.lambda$writeObservableUuid$2(uuid, (com.android.server.companion.devicepresence.ObservableUuid) obj);
                }
            });
            cachedObservableUuids.add(uuid);
            this.mCachedPerUser.set(userId, cachedObservableUuids);
        }
        this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.companion.devicepresence.ObservableUuidStore$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$writeObservableUuid$3(userId, cachedObservableUuids);
            }
        });
    }

    static /* synthetic */ boolean lambda$writeObservableUuid$2(com.android.server.companion.devicepresence.ObservableUuid uuid, com.android.server.companion.devicepresence.ObservableUuid uuid1) {
        return uuid1.getUuid().equals(uuid.getUuid()) && uuid1.getPackageName().equals(uuid.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: writeObservableUuidToStore, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$writeObservableUuid$3(int userId, final java.util.List<com.android.server.companion.devicepresence.ObservableUuid> cachedObservableUuids) {
        android.util.AtomicFile file = getStorageFileForUser(userId);
        android.util.Slog.i(TAG, "Writing ObservableUuid for user " + userId + " to file=" + file.getBaseFile().getPath());
        synchronized (file) {
            com.android.server.companion.utils.DataStoreUtils.writeToFileSafely(file, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.companion.devicepresence.ObservableUuidStore$$ExternalSyntheticLambda3
                public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                    this.f$0.lambda$writeObservableUuidToStore$4(cachedObservableUuids, (java.io.FileOutputStream) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeObservableUuidToStore$4(java.util.List cachedObservableUuids, java.io.FileOutputStream out) throws java.lang.Exception {
        com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(out);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.startDocument((java.lang.String) null, true);
        writeObservableUuidToXml(serializer, cachedObservableUuids);
        serializer.endDocument();
    }

    private void writeObservableUuidToXml(com.android.modules.utils.TypedXmlSerializer serializer, java.util.Collection<com.android.server.companion.devicepresence.ObservableUuid> uuids) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, XML_TAG_UUIDS);
        for (com.android.server.companion.devicepresence.ObservableUuid uuid : uuids) {
            writeUuidToXml(serializer, uuid);
        }
        serializer.endTag((java.lang.String) null, XML_TAG_UUIDS);
    }

    private void writeUuidToXml(com.android.modules.utils.TypedXmlSerializer serializer, com.android.server.companion.devicepresence.ObservableUuid uuid) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, "uuid");
        com.android.internal.util.XmlUtils.writeIntAttribute(serializer, XML_ATTR_USER_ID, uuid.getUserId());
        com.android.internal.util.XmlUtils.writeStringAttribute(serializer, "uuid", uuid.getUuid().toString());
        com.android.internal.util.XmlUtils.writeStringAttribute(serializer, XML_ATTR_PACKAGE, uuid.getPackageName());
        com.android.internal.util.XmlUtils.writeLongAttribute(serializer, XML_ATTR_TIME_APPROVED, uuid.getTimeApprovedMs());
        serializer.endTag((java.lang.String) null, "uuid");
    }

    private java.util.List<com.android.server.companion.devicepresence.ObservableUuid> readObservableUuidsFromCache(final int userId) throws java.util.concurrent.ExecutionException, java.lang.InterruptedException, java.util.concurrent.TimeoutException {
        java.util.List<com.android.server.companion.devicepresence.ObservableUuid> cachedObservableUuids = this.mCachedPerUser.get(userId);
        if (cachedObservableUuids == null) {
            java.util.concurrent.Future<java.util.List<com.android.server.companion.devicepresence.ObservableUuid>> future = this.mExecutor.submit(new java.util.concurrent.Callable() { // from class: com.android.server.companion.devicepresence.ObservableUuidStore$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Callable
                public final java.lang.Object call() {
                    return this.f$0.lambda$readObservableUuidsFromCache$5(userId);
                }
            });
            try {
                cachedObservableUuids = future.get(5L, java.util.concurrent.TimeUnit.SECONDS);
            } catch (java.lang.InterruptedException e) {
                android.util.Slog.e(TAG, "Thread reading ObservableUuid from disk is interrupted.");
            } catch (java.util.concurrent.ExecutionException e2) {
                android.util.Slog.e(TAG, "Error occurred while reading ObservableUuid from disk.");
            } catch (java.util.concurrent.TimeoutException e3) {
                android.util.Slog.e(TAG, "Reading ObservableUuid from disk timed out.");
            }
            this.mCachedPerUser.set(userId, cachedObservableUuids);
        }
        return cachedObservableUuids;
    }

    /* JADX INFO: renamed from: readObservableUuidFromStore, reason: merged with bridge method [inline-methods] */
    public java.util.List<com.android.server.companion.devicepresence.ObservableUuid> lambda$readObservableUuidsFromCache$5(int userId) {
        android.util.AtomicFile file = getStorageFileForUser(userId);
        android.util.Slog.i(TAG, "Reading ObservableUuid for user " + userId + " from file=" + file.getBaseFile().getPath());
        synchronized (file) {
            if (!file.getBaseFile().exists()) {
                android.util.Slog.d(TAG, "File does not exist -> Abort");
                return new java.util.ArrayList();
            }
            try {
                java.io.FileInputStream in = file.openRead();
                try {
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                    com.android.internal.util.XmlUtils.beginDocument(parser, XML_TAG_UUIDS);
                    java.util.List<com.android.server.companion.devicepresence.ObservableUuid> observableUuidFromXml = readObservableUuidFromXml(parser);
                    if (in != null) {
                        in.close();
                    }
                    return observableUuidFromXml;
                } catch (java.lang.Throwable th) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.e(TAG, "Error while reading requests file", e);
                return new java.util.ArrayList();
            }
        }
    }

    private java.util.List<com.android.server.companion.devicepresence.ObservableUuid> readObservableUuidFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (!com.android.server.companion.utils.DataStoreUtils.isStartOfTag(parser, XML_TAG_UUIDS)) {
            throw new org.xmlpull.v1.XmlPullParserException("The XML doesn't have start tag: uuids");
        }
        java.util.List<com.android.server.companion.devicepresence.ObservableUuid> observableUuids = new java.util.ArrayList<>();
        while (true) {
            parser.nextTag();
            if (!com.android.server.companion.utils.DataStoreUtils.isEndOfTag(parser, XML_TAG_UUIDS)) {
                if (com.android.server.companion.utils.DataStoreUtils.isStartOfTag(parser, "uuid")) {
                    observableUuids.add(readUuidFromXml(parser));
                }
            } else {
                return observableUuids;
            }
        }
    }

    private com.android.server.companion.devicepresence.ObservableUuid readUuidFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (!com.android.server.companion.utils.DataStoreUtils.isStartOfTag(parser, "uuid")) {
            throw new org.xmlpull.v1.XmlPullParserException("XML doesn't have start tag: uuid");
        }
        int userId = com.android.internal.util.XmlUtils.readIntAttribute(parser, XML_ATTR_USER_ID);
        android.os.ParcelUuid uuid = android.os.ParcelUuid.fromString(com.android.internal.util.XmlUtils.readStringAttribute(parser, "uuid"));
        java.lang.String packageName = com.android.internal.util.XmlUtils.readStringAttribute(parser, XML_ATTR_PACKAGE);
        java.lang.Long timeApproved = java.lang.Long.valueOf(com.android.internal.util.XmlUtils.readLongAttribute(parser, XML_ATTR_TIME_APPROVED));
        return new com.android.server.companion.devicepresence.ObservableUuid(userId, uuid, packageName, timeApproved);
    }

    private android.util.AtomicFile getStorageFileForUser(final int userId) {
        return this.mUserIdToStorageFile.computeIfAbsent(java.lang.Integer.valueOf(userId), new java.util.function.Function() { // from class: com.android.server.companion.devicepresence.ObservableUuidStore$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.companion.utils.DataStoreUtils.createStorageFileForUser(userId, com.android.server.companion.devicepresence.ObservableUuidStore.FILE_NAME);
            }
        });
    }

    public java.util.List<com.android.server.companion.devicepresence.ObservableUuid> getObservableUuidsForPackage(int userId, java.lang.String packageName) {
        java.util.List<com.android.server.companion.devicepresence.ObservableUuid> uuidsTobeObservedPerPackage = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            java.util.List<com.android.server.companion.devicepresence.ObservableUuid> uuids = readObservableUuidsFromCache(userId);
            for (com.android.server.companion.devicepresence.ObservableUuid uuid : uuids) {
                if (uuid.getPackageName().equals(packageName)) {
                    uuidsTobeObservedPerPackage.add(uuid);
                }
            }
        }
        return uuidsTobeObservedPerPackage;
    }

    public java.util.List<com.android.server.companion.devicepresence.ObservableUuid> getObservableUuidsForUser(int userId) {
        java.util.List<com.android.server.companion.devicepresence.ObservableUuid> observableUuidsFromCache;
        synchronized (this.mLock) {
            observableUuidsFromCache = readObservableUuidsFromCache(userId);
        }
        return observableUuidsFromCache;
    }

    public boolean isUuidBeingObserved(android.os.ParcelUuid uuid, int userId, java.lang.String packageName) {
        java.util.List<com.android.server.companion.devicepresence.ObservableUuid> uuidsBeingObserved = getObservableUuidsForPackage(userId, packageName);
        for (com.android.server.companion.devicepresence.ObservableUuid observableUuid : uuidsBeingObserved) {
            if (observableUuid.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }
}
