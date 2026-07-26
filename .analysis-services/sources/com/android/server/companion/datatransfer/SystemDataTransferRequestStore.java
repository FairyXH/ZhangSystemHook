package com.android.server.companion.datatransfer;

/* JADX INFO: loaded from: classes.dex */
public class SystemDataTransferRequestStore {
    private static final java.lang.String FILE_NAME = "companion_device_system_data_transfer_requests.xml";
    private static final java.lang.String LOG_TAG = "CDM_SystemDataTransferRequestStore";
    private static final int READ_FROM_DISK_TIMEOUT = 5;
    private static final java.lang.String XML_ATTR_ASSOCIATION_ID = "association_id";
    private static final java.lang.String XML_ATTR_DATA_TYPE = "data_type";
    private static final java.lang.String XML_ATTR_IS_USER_CONSENTED = "is_user_consented";
    private static final java.lang.String XML_TAG_REQUEST = "request";
    private static final java.lang.String XML_TAG_REQUESTS = "requests";
    private final java.util.concurrent.ConcurrentMap<java.lang.Integer, android.util.AtomicFile> mUserIdToStorageFile = new java.util.concurrent.ConcurrentHashMap();
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<java.util.ArrayList<android.companion.datatransfer.SystemDataTransferRequest>> mCachedPerUser = new android.util.SparseArray<>();
    private final java.util.concurrent.ExecutorService mExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();

    public java.util.List<android.companion.datatransfer.SystemDataTransferRequest> readRequestsByAssociationId(int userId, int associationId) {
        java.util.List<android.companion.datatransfer.SystemDataTransferRequest> cachedRequests;
        synchronized (this.mLock) {
            cachedRequests = readRequestsFromCache(userId);
        }
        java.util.List<android.companion.datatransfer.SystemDataTransferRequest> requestsByAssociationId = new java.util.ArrayList<>();
        for (android.companion.datatransfer.SystemDataTransferRequest request : cachedRequests) {
            if (request.getAssociationId() == associationId) {
                requestsByAssociationId.add(request);
            }
        }
        return requestsByAssociationId;
    }

    public void writeRequest(final int userId, final android.companion.datatransfer.SystemDataTransferRequest request) {
        final java.util.ArrayList<android.companion.datatransfer.SystemDataTransferRequest> cachedRequests;
        android.util.Slog.i(LOG_TAG, "Writing request=" + request + " to store.");
        synchronized (this.mLock) {
            cachedRequests = readRequestsFromCache(userId);
            cachedRequests.removeIf(new java.util.function.Predicate() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.datatransfer.SystemDataTransferRequestStore.lambda$writeRequest$0(request, (android.companion.datatransfer.SystemDataTransferRequest) obj);
                }
            });
            cachedRequests.add(request);
            this.mCachedPerUser.set(userId, cachedRequests);
        }
        this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$writeRequest$1(userId, cachedRequests);
            }
        });
    }

    static /* synthetic */ boolean lambda$writeRequest$0(android.companion.datatransfer.SystemDataTransferRequest request, android.companion.datatransfer.SystemDataTransferRequest request1) {
        return request1.getAssociationId() == request.getAssociationId();
    }

    public void removeRequestsByAssociationId(final int userId, final int associationId) {
        final java.util.ArrayList<android.companion.datatransfer.SystemDataTransferRequest> cachedRequests;
        android.util.Slog.i(LOG_TAG, "Removing system data transfer requests for userId=" + userId + ", associationId=" + associationId);
        synchronized (this.mLock) {
            cachedRequests = readRequestsFromCache(userId);
            cachedRequests.removeIf(new java.util.function.Predicate() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.companion.datatransfer.SystemDataTransferRequestStore.lambda$removeRequestsByAssociationId$2(associationId, (android.companion.datatransfer.SystemDataTransferRequest) obj);
                }
            });
            this.mCachedPerUser.set(userId, cachedRequests);
        }
        this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$removeRequestsByAssociationId$3(userId, cachedRequests);
            }
        });
    }

    static /* synthetic */ boolean lambda$removeRequestsByAssociationId$2(int associationId, android.companion.datatransfer.SystemDataTransferRequest request) {
        return request.getAssociationId() == associationId;
    }

    public byte[] getBackupPayload(int userId) {
        byte[] bArrFileToByteArray;
        android.util.AtomicFile file = getStorageFileForUser(userId);
        synchronized (file) {
            bArrFileToByteArray = com.android.server.companion.utils.DataStoreUtils.fileToByteArray(file);
        }
        return bArrFileToByteArray;
    }

    public java.util.List<android.companion.datatransfer.SystemDataTransferRequest> readRequestsFromPayload(byte[] payload, int userId) {
        try {
            java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(payload);
            try {
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                com.android.internal.util.XmlUtils.beginDocument(parser, XML_TAG_REQUESTS);
                java.util.ArrayList<android.companion.datatransfer.SystemDataTransferRequest> requestsFromXml = readRequestsFromXml(parser, userId);
                in.close();
                return requestsFromXml;
            } catch (java.lang.Throwable th) {
                try {
                    in.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(LOG_TAG, "Error while reading requests file", e);
            return new java.util.ArrayList();
        }
    }

    private java.util.ArrayList<android.companion.datatransfer.SystemDataTransferRequest> readRequestsFromCache(final int userId) throws java.util.concurrent.ExecutionException, java.lang.InterruptedException, java.util.concurrent.TimeoutException {
        java.util.ArrayList<android.companion.datatransfer.SystemDataTransferRequest> cachedRequests = this.mCachedPerUser.get(userId);
        if (cachedRequests == null) {
            java.util.concurrent.Future<java.util.ArrayList<android.companion.datatransfer.SystemDataTransferRequest>> future = this.mExecutor.submit(new java.util.concurrent.Callable() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda2
                @Override // java.util.concurrent.Callable
                public final java.lang.Object call() {
                    return this.f$0.lambda$readRequestsFromCache$4(userId);
                }
            });
            try {
                cachedRequests = future.get(5L, java.util.concurrent.TimeUnit.SECONDS);
            } catch (java.lang.InterruptedException e) {
                android.util.Slog.e(LOG_TAG, "Thread reading SystemDataTransferRequest from disk is interrupted.");
            } catch (java.util.concurrent.ExecutionException e2) {
                android.util.Slog.e(LOG_TAG, "Error occurred while reading SystemDataTransferRequest from disk.");
            } catch (java.util.concurrent.TimeoutException e3) {
                android.util.Slog.e(LOG_TAG, "Reading SystemDataTransferRequest from disk timed out.");
            }
            this.mCachedPerUser.set(userId, cachedRequests);
        }
        return cachedRequests;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: readRequestsFromStore, reason: merged with bridge method [inline-methods] */
    public java.util.ArrayList<android.companion.datatransfer.SystemDataTransferRequest> lambda$readRequestsFromCache$4(int userId) {
        android.util.AtomicFile file = getStorageFileForUser(userId);
        android.util.Slog.i(LOG_TAG, "Reading SystemDataTransferRequests for user " + userId + " from file=" + file.getBaseFile().getPath());
        synchronized (file) {
            if (!file.getBaseFile().exists()) {
                android.util.Slog.d(LOG_TAG, "File does not exist -> Abort");
                return new java.util.ArrayList<>();
            }
            try {
                java.io.FileInputStream in = file.openRead();
                try {
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                    com.android.internal.util.XmlUtils.beginDocument(parser, XML_TAG_REQUESTS);
                    java.util.ArrayList<android.companion.datatransfer.SystemDataTransferRequest> requestsFromXml = readRequestsFromXml(parser, userId);
                    if (in != null) {
                        in.close();
                    }
                    return requestsFromXml;
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
                android.util.Slog.e(LOG_TAG, "Error while reading requests file", e);
                return new java.util.ArrayList<>();
            }
        }
    }

    private java.util.ArrayList<android.companion.datatransfer.SystemDataTransferRequest> readRequestsFromXml(com.android.modules.utils.TypedXmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (!com.android.server.companion.utils.DataStoreUtils.isStartOfTag(parser, XML_TAG_REQUESTS)) {
            throw new org.xmlpull.v1.XmlPullParserException("The XML doesn't have start tag: requests");
        }
        java.util.ArrayList<android.companion.datatransfer.SystemDataTransferRequest> requests = new java.util.ArrayList<>();
        while (true) {
            parser.nextTag();
            if (!com.android.server.companion.utils.DataStoreUtils.isEndOfTag(parser, XML_TAG_REQUESTS)) {
                if (com.android.server.companion.utils.DataStoreUtils.isStartOfTag(parser, XML_TAG_REQUEST)) {
                    requests.add(readRequestFromXml(parser, userId));
                }
            } else {
                return requests;
            }
        }
    }

    private android.companion.datatransfer.SystemDataTransferRequest readRequestFromXml(com.android.modules.utils.TypedXmlPullParser parser, int userId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (!com.android.server.companion.utils.DataStoreUtils.isStartOfTag(parser, XML_TAG_REQUEST)) {
            throw new org.xmlpull.v1.XmlPullParserException("XML doesn't have start tag: request");
        }
        int associationId = com.android.internal.util.XmlUtils.readIntAttribute(parser, XML_ATTR_ASSOCIATION_ID);
        int dataType = com.android.internal.util.XmlUtils.readIntAttribute(parser, XML_ATTR_DATA_TYPE);
        boolean isUserConsented = com.android.internal.util.XmlUtils.readBooleanAttribute(parser, XML_ATTR_IS_USER_CONSENTED);
        switch (dataType) {
            case 1:
                android.companion.datatransfer.PermissionSyncRequest request = new android.companion.datatransfer.PermissionSyncRequest(associationId);
                request.setUserId(userId);
                request.setUserConsented(isUserConsented);
                return request;
            default:
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: writeRequestsToStore, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$writeRequest$1(int userId, final java.util.List<android.companion.datatransfer.SystemDataTransferRequest> requests) {
        android.util.AtomicFile file = getStorageFileForUser(userId);
        android.util.Slog.i(LOG_TAG, "Writing SystemDataTransferRequests for user " + userId + " to file=" + file.getBaseFile().getPath());
        synchronized (file) {
            com.android.server.companion.utils.DataStoreUtils.writeToFileSafely(file, new com.android.internal.util.FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda3
                public final void acceptOrThrow(java.lang.Object obj) throws java.lang.Exception {
                    this.f$0.lambda$writeRequestsToStore$5(requests, (java.io.FileOutputStream) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeRequestsToStore$5(java.util.List requests, java.io.FileOutputStream out) throws java.lang.Exception {
        com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(out);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.startDocument((java.lang.String) null, true);
        writeRequestsToXml(serializer, requests);
        serializer.endDocument();
    }

    public void dump(java.io.PrintWriter out) {
        synchronized (this.mLock) {
            out.append("System Data Transfer Requests (Cached): ");
            if (this.mCachedPerUser.size() == 0) {
                out.append("<empty>\n");
            } else {
                out.append("\n");
                for (int i = 0; i < this.mCachedPerUser.size(); i++) {
                    int userId = this.mCachedPerUser.keyAt(i);
                    for (android.companion.datatransfer.SystemDataTransferRequest request : this.mCachedPerUser.get(userId)) {
                        out.append("  u").append((java.lang.CharSequence) java.lang.String.valueOf(userId)).append(" -> ").append((java.lang.CharSequence) request.toString()).append('\n');
                    }
                }
            }
        }
    }

    private void writeRequestsToXml(com.android.modules.utils.TypedXmlSerializer serializer, java.util.Collection<android.companion.datatransfer.SystemDataTransferRequest> requests) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, XML_TAG_REQUESTS);
        for (android.companion.datatransfer.SystemDataTransferRequest request : requests) {
            writeRequestToXml(serializer, request);
        }
        serializer.endTag((java.lang.String) null, XML_TAG_REQUESTS);
    }

    private void writeRequestToXml(com.android.modules.utils.TypedXmlSerializer serializer, android.companion.datatransfer.SystemDataTransferRequest request) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, XML_TAG_REQUEST);
        com.android.internal.util.XmlUtils.writeIntAttribute(serializer, XML_ATTR_ASSOCIATION_ID, request.getAssociationId());
        com.android.internal.util.XmlUtils.writeIntAttribute(serializer, XML_ATTR_DATA_TYPE, request.getDataType());
        com.android.internal.util.XmlUtils.writeBooleanAttribute(serializer, XML_ATTR_IS_USER_CONSENTED, request.isUserConsented());
        serializer.endTag((java.lang.String) null, XML_TAG_REQUEST);
    }

    private android.util.AtomicFile getStorageFileForUser(final int userId) {
        return this.mUserIdToStorageFile.computeIfAbsent(java.lang.Integer.valueOf(userId), new java.util.function.Function() { // from class: com.android.server.companion.datatransfer.SystemDataTransferRequestStore$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.companion.utils.DataStoreUtils.createStorageFileForUser(userId, com.android.server.companion.datatransfer.SystemDataTransferRequestStore.FILE_NAME);
            }
        });
    }
}
