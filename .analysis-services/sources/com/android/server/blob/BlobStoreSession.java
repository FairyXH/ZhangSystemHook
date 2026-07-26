package com.android.server.blob;

/* JADX INFO: loaded from: classes.dex */
class BlobStoreSession extends android.app.blob.IBlobStoreSession.Stub {
    static final int STATE_ABANDONED = 2;
    static final int STATE_CLOSED = 0;
    static final int STATE_COMMITTED = 3;
    static final int STATE_OPENED = 1;
    static final int STATE_VERIFIED_INVALID = 5;
    static final int STATE_VERIFIED_VALID = 4;
    private final com.android.server.blob.BlobAccessMode mBlobAccessMode;
    private android.app.blob.IBlobCommitCallback mBlobCommitCallback;
    private final android.app.blob.BlobHandle mBlobHandle;
    private final android.content.Context mContext;
    private final long mCreationTimeMs;
    private byte[] mDataDigest;
    private final com.android.server.blob.BlobStoreManagerService.SessionStateChangeListener mListener;
    private final java.lang.String mOwnerPackageName;
    private final int mOwnerUid;
    private final java.util.ArrayList<android.os.RevocableFileDescriptor> mRevocableFds;
    private java.io.File mSessionFile;
    private final long mSessionId;
    private final java.lang.Object mSessionLock;
    private int mState;

    private BlobStoreSession(android.content.Context context, long sessionId, android.app.blob.BlobHandle blobHandle, int ownerUid, java.lang.String ownerPackageName, long creationTimeMs, com.android.server.blob.BlobStoreManagerService.SessionStateChangeListener listener) {
        this.mSessionLock = new java.lang.Object();
        this.mRevocableFds = new java.util.ArrayList<>();
        this.mState = 0;
        this.mBlobAccessMode = new com.android.server.blob.BlobAccessMode();
        this.mContext = context;
        this.mBlobHandle = blobHandle;
        this.mSessionId = sessionId;
        this.mOwnerUid = ownerUid;
        this.mOwnerPackageName = ownerPackageName;
        this.mCreationTimeMs = creationTimeMs;
        this.mListener = listener;
    }

    BlobStoreSession(android.content.Context context, long sessionId, android.app.blob.BlobHandle blobHandle, int ownerUid, java.lang.String ownerPackageName, com.android.server.blob.BlobStoreManagerService.SessionStateChangeListener listener) {
        this(context, sessionId, blobHandle, ownerUid, ownerPackageName, java.lang.System.currentTimeMillis(), listener);
    }

    public android.app.blob.BlobHandle getBlobHandle() {
        return this.mBlobHandle;
    }

    public long getSessionId() {
        return this.mSessionId;
    }

    public int getOwnerUid() {
        return this.mOwnerUid;
    }

    public java.lang.String getOwnerPackageName() {
        return this.mOwnerPackageName;
    }

    boolean hasAccess(int callingUid, java.lang.String callingPackageName) {
        return this.mOwnerUid == callingUid && this.mOwnerPackageName.equals(callingPackageName);
    }

    void open() {
        synchronized (this.mSessionLock) {
            if (isFinalized()) {
                throw new java.lang.IllegalStateException("Not allowed to open session with state: " + stateToString(this.mState));
            }
            this.mState = 1;
        }
    }

    int getState() {
        int i;
        synchronized (this.mSessionLock) {
            i = this.mState;
        }
        return i;
    }

    void sendCommitCallbackResult(int result) {
        synchronized (this.mSessionLock) {
            try {
                this.mBlobCommitCallback.onResult(result);
            } catch (android.os.RemoteException e) {
                android.util.Slog.d(com.android.server.blob.BlobStoreConfig.TAG, "Error sending the callback result", e);
            }
            this.mBlobCommitCallback = null;
        }
    }

    com.android.server.blob.BlobAccessMode getBlobAccessMode() {
        com.android.server.blob.BlobAccessMode blobAccessMode;
        synchronized (this.mSessionLock) {
            blobAccessMode = this.mBlobAccessMode;
        }
        return blobAccessMode;
    }

    boolean isFinalized() {
        boolean z;
        synchronized (this.mSessionLock) {
            z = this.mState == 3 || this.mState == 2;
        }
        return z;
    }

    boolean isExpired() {
        long lastModifiedTimeMs = getSessionFile().lastModified();
        return com.android.server.blob.BlobStoreConfig.hasSessionExpired(lastModifiedTimeMs == 0 ? this.mCreationTimeMs : lastModifiedTimeMs);
    }

    public android.os.ParcelFileDescriptor openWrite(long offsetBytes, long lengthBytes) {
        android.os.ParcelFileDescriptor revocableFileDescriptor;
        com.android.internal.util.Preconditions.checkArgumentNonnegative(offsetBytes, "offsetBytes must not be negative");
        assertCallerIsOwner();
        synchronized (this.mSessionLock) {
            if (this.mState != 1) {
                throw new java.lang.IllegalStateException("Not allowed to write in state: " + stateToString(this.mState));
            }
        }
        java.io.FileDescriptor fd = null;
        try {
            fd = openWriteInternal(offsetBytes, lengthBytes);
            android.os.RevocableFileDescriptor revocableFd = new android.os.RevocableFileDescriptor(this.mContext, fd, com.android.server.blob.BlobStoreUtils.getRevocableFdHandler());
            synchronized (this.mSessionLock) {
                if (this.mState != 1) {
                    libcore.io.IoUtils.closeQuietly(fd);
                    throw new java.lang.IllegalStateException("Not allowed to write in state: " + stateToString(this.mState));
                }
                trackRevocableFdLocked(revocableFd);
                revocableFileDescriptor = revocableFd.getRevocableFileDescriptor();
            }
            return revocableFileDescriptor;
        } catch (java.io.IOException e) {
            libcore.io.IoUtils.closeQuietly(fd);
            throw android.util.ExceptionUtils.wrap(e);
        }
    }

    private java.io.FileDescriptor openWriteInternal(long offsetBytes, long lengthBytes) throws java.io.IOException {
        try {
            java.io.File sessionFile = getSessionFile();
            if (sessionFile == null) {
                throw new java.lang.IllegalStateException("Couldn't get the file for this session");
            }
            java.io.FileDescriptor fd = android.system.Os.open(sessionFile.getPath(), android.system.OsConstants.O_CREAT | android.system.OsConstants.O_RDWR, com.android.internal.util.FrameworkStatsLog.NON_A11Y_TOOL_SERVICE_WARNING_REPORT);
            if (offsetBytes > 0) {
                long curOffset = android.system.Os.lseek(fd, offsetBytes, android.system.OsConstants.SEEK_SET);
                if (curOffset != offsetBytes) {
                    throw new java.lang.IllegalStateException("Failed to seek " + offsetBytes + "; curOffset=" + offsetBytes);
                }
            }
            if (lengthBytes > 0) {
                ((android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class)).allocateBytes(fd, lengthBytes);
            }
            return fd;
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public android.os.ParcelFileDescriptor openRead() {
        android.os.ParcelFileDescriptor revocableFileDescriptor;
        assertCallerIsOwner();
        synchronized (this.mSessionLock) {
            if (this.mState != 1) {
                throw new java.lang.IllegalStateException("Not allowed to read in state: " + stateToString(this.mState));
            }
            if (!com.android.server.blob.BlobStoreConfig.shouldUseRevocableFdForReads()) {
                try {
                    return new android.os.ParcelFileDescriptor(openReadInternal());
                } catch (java.io.IOException e) {
                    throw android.util.ExceptionUtils.wrap(e);
                }
            }
            java.io.FileDescriptor fd = null;
            try {
                fd = openReadInternal();
                android.os.RevocableFileDescriptor revocableFd = new android.os.RevocableFileDescriptor(this.mContext, fd);
                synchronized (this.mSessionLock) {
                    if (this.mState != 1) {
                        libcore.io.IoUtils.closeQuietly(fd);
                        throw new java.lang.IllegalStateException("Not allowed to read in state: " + stateToString(this.mState));
                    }
                    trackRevocableFdLocked(revocableFd);
                    revocableFileDescriptor = revocableFd.getRevocableFileDescriptor();
                }
                return revocableFileDescriptor;
            } catch (java.io.IOException e2) {
                libcore.io.IoUtils.closeQuietly(fd);
                throw android.util.ExceptionUtils.wrap(e2);
            }
        }
    }

    private java.io.FileDescriptor openReadInternal() throws java.io.IOException {
        try {
            java.io.File sessionFile = getSessionFile();
            if (sessionFile == null) {
                throw new java.lang.IllegalStateException("Couldn't get the file for this session");
            }
            java.io.FileDescriptor fd = android.system.Os.open(sessionFile.getPath(), android.system.OsConstants.O_RDONLY, 0);
            return fd;
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    public long getSize() {
        return getSessionFile().length();
    }

    public void allowPackageAccess(java.lang.String packageName, byte[] certificate) {
        assertCallerIsOwner();
        java.util.Objects.requireNonNull(packageName, "packageName must not be null");
        synchronized (this.mSessionLock) {
            if (this.mState != 1) {
                throw new java.lang.IllegalStateException("Not allowed to change access type in state: " + stateToString(this.mState));
            }
            if (this.mBlobAccessMode.getAllowedPackagesCount() >= com.android.server.blob.BlobStoreConfig.getMaxPermittedPackages()) {
                throw new android.os.ParcelableException(new android.os.LimitExceededException("Too many packages permitted to access the blob: " + this.mBlobAccessMode.getAllowedPackagesCount()));
            }
            this.mBlobAccessMode.allowPackageAccess(packageName, certificate);
        }
    }

    public void allowSameSignatureAccess() {
        assertCallerIsOwner();
        synchronized (this.mSessionLock) {
            if (this.mState != 1) {
                throw new java.lang.IllegalStateException("Not allowed to change access type in state: " + stateToString(this.mState));
            }
            this.mBlobAccessMode.allowSameSignatureAccess();
        }
    }

    public void allowPublicAccess() {
        assertCallerIsOwner();
        synchronized (this.mSessionLock) {
            if (this.mState != 1) {
                throw new java.lang.IllegalStateException("Not allowed to change access type in state: " + stateToString(this.mState));
            }
            this.mBlobAccessMode.allowPublicAccess();
        }
    }

    public boolean isPackageAccessAllowed(java.lang.String packageName, byte[] certificate) {
        boolean zIsPackageAccessAllowed;
        assertCallerIsOwner();
        java.util.Objects.requireNonNull(packageName, "packageName must not be null");
        com.android.internal.util.Preconditions.checkByteArrayNotEmpty(certificate, "certificate");
        synchronized (this.mSessionLock) {
            if (this.mState != 1) {
                throw new java.lang.IllegalStateException("Not allowed to get access type in state: " + stateToString(this.mState));
            }
            zIsPackageAccessAllowed = this.mBlobAccessMode.isPackageAccessAllowed(packageName, certificate);
        }
        return zIsPackageAccessAllowed;
    }

    public boolean isSameSignatureAccessAllowed() {
        boolean zIsSameSignatureAccessAllowed;
        assertCallerIsOwner();
        synchronized (this.mSessionLock) {
            if (this.mState != 1) {
                throw new java.lang.IllegalStateException("Not allowed to get access type in state: " + stateToString(this.mState));
            }
            zIsSameSignatureAccessAllowed = this.mBlobAccessMode.isSameSignatureAccessAllowed();
        }
        return zIsSameSignatureAccessAllowed;
    }

    public boolean isPublicAccessAllowed() {
        boolean zIsPublicAccessAllowed;
        assertCallerIsOwner();
        synchronized (this.mSessionLock) {
            if (this.mState != 1) {
                throw new java.lang.IllegalStateException("Not allowed to get access type in state: " + stateToString(this.mState));
            }
            zIsPublicAccessAllowed = this.mBlobAccessMode.isPublicAccessAllowed();
        }
        return zIsPublicAccessAllowed;
    }

    public void close() {
        closeSession(0, false);
    }

    public void abandon() {
        closeSession(2, true);
    }

    public void commit(android.app.blob.IBlobCommitCallback callback) {
        synchronized (this.mSessionLock) {
            this.mBlobCommitCallback = callback;
            closeSession(3, true);
        }
    }

    private void closeSession(int state, boolean sendCallback) {
        assertCallerIsOwner();
        synchronized (this.mSessionLock) {
            if (this.mState != 1) {
                if (state != 0) {
                    throw new java.lang.IllegalStateException("Not allowed to delete or abandon a session with state: " + stateToString(this.mState));
                }
            } else {
                this.mState = state;
                revokeAllFds();
                if (sendCallback) {
                    this.mListener.onStateChanged(this);
                }
            }
        }
    }

    void computeDigest() {
        try {
            try {
                android.os.Trace.traceBegin(524288L, "computeBlobDigest-i" + this.mSessionId + "-l" + getSessionFile().length());
                this.mDataDigest = android.os.FileUtils.digest(getSessionFile(), this.mBlobHandle.algorithm);
            } catch (java.io.IOException | java.security.NoSuchAlgorithmException e) {
                android.util.Slog.e(com.android.server.blob.BlobStoreConfig.TAG, "Error computing the digest", e);
            }
        } finally {
            android.os.Trace.traceEnd(524288L);
        }
    }

    void verifyBlobData() {
        synchronized (this.mSessionLock) {
            if (this.mDataDigest != null && java.util.Arrays.equals(this.mDataDigest, this.mBlobHandle.digest)) {
                this.mState = 4;
            } else {
                android.util.Slog.d(com.android.server.blob.BlobStoreConfig.TAG, "Digest of the data (" + (this.mDataDigest == null ? "null" : android.app.blob.BlobHandle.safeDigest(this.mDataDigest)) + ") didn't match the given BlobHandle.digest (" + android.app.blob.BlobHandle.safeDigest(this.mBlobHandle.digest) + ")");
                this.mState = 5;
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BLOB_COMMITTED, getOwnerUid(), this.mSessionId, getSize(), 3);
                sendCommitCallbackResult(1);
            }
            this.mListener.onStateChanged(this);
        }
    }

    void destroy() {
        revokeAllFds();
        getSessionFile().delete();
    }

    private void revokeAllFds() {
        synchronized (this.mRevocableFds) {
            for (int i = this.mRevocableFds.size() - 1; i >= 0; i--) {
                this.mRevocableFds.get(i).revoke();
            }
            this.mRevocableFds.clear();
        }
    }

    private void trackRevocableFdLocked(final android.os.RevocableFileDescriptor revocableFd) {
        synchronized (this.mRevocableFds) {
            this.mRevocableFds.add(revocableFd);
        }
        revocableFd.addOnCloseListener(new android.os.ParcelFileDescriptor.OnCloseListener() { // from class: com.android.server.blob.BlobStoreSession$$ExternalSyntheticLambda0
            @Override // android.os.ParcelFileDescriptor.OnCloseListener
            public final void onClose(java.io.IOException iOException) {
                this.f$0.lambda$trackRevocableFdLocked$0(revocableFd, iOException);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$trackRevocableFdLocked$0(android.os.RevocableFileDescriptor revocableFd, java.io.IOException e) {
        synchronized (this.mRevocableFds) {
            this.mRevocableFds.remove(revocableFd);
        }
    }

    java.io.File getSessionFile() {
        if (this.mSessionFile == null) {
            this.mSessionFile = com.android.server.blob.BlobStoreConfig.prepareBlobFile(this.mSessionId);
        }
        return this.mSessionFile;
    }

    static java.lang.String stateToString(int state) {
        switch (state) {
            case 0:
                return "<closed>";
            case 1:
                return "<opened>";
            case 2:
                return "<abandoned>";
            case 3:
                return "<committed>";
            case 4:
                return "<verified_valid>";
            case 5:
                return "<verified_invalid>";
            default:
                android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "Unknown state: " + state);
                return "<unknown>";
        }
    }

    public java.lang.String toString() {
        return "BlobStoreSession {id:" + this.mSessionId + ",handle:" + this.mBlobHandle + ",uid:" + this.mOwnerUid + ",pkg:" + this.mOwnerPackageName + "}";
    }

    private void assertCallerIsOwner() {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != this.mOwnerUid) {
            throw new java.lang.SecurityException(this.mOwnerUid + " is not the session owner");
        }
    }

    void dump(android.util.IndentingPrintWriter fout, com.android.server.blob.BlobStoreManagerService.DumpArgs dumpArgs) {
        synchronized (this.mSessionLock) {
            fout.println("state: " + stateToString(this.mState));
            fout.println("ownerUid: " + this.mOwnerUid);
            fout.println("ownerPkg: " + this.mOwnerPackageName);
            fout.println("creation time: " + com.android.server.blob.BlobStoreUtils.formatTime(this.mCreationTimeMs));
            fout.println("size: " + android.text.format.Formatter.formatFileSize(this.mContext, getSize(), 8));
            fout.println("blobHandle:");
            fout.increaseIndent();
            this.mBlobHandle.dump(fout, dumpArgs.shouldDumpFull());
            fout.decreaseIndent();
            fout.println("accessMode:");
            fout.increaseIndent();
            this.mBlobAccessMode.dump(fout);
            fout.decreaseIndent();
            fout.println("Open fds: #" + this.mRevocableFds.size());
        }
    }

    void writeToXml(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
        synchronized (this.mSessionLock) {
            com.android.internal.util.XmlUtils.writeLongAttribute(out, "id", this.mSessionId);
            com.android.internal.util.XmlUtils.writeStringAttribute(out, "p", this.mOwnerPackageName);
            com.android.internal.util.XmlUtils.writeIntAttribute(out, "u", this.mOwnerUid);
            com.android.internal.util.XmlUtils.writeLongAttribute(out, "crt", this.mCreationTimeMs);
            out.startTag(null, "bh");
            this.mBlobHandle.writeToXml(out);
            out.endTag(null, "bh");
            out.startTag(null, "am");
            this.mBlobAccessMode.writeToXml(out);
            out.endTag(null, "am");
        }
    }

    static com.android.server.blob.BlobStoreSession createFromXml(org.xmlpull.v1.XmlPullParser in, int version, android.content.Context context, com.android.server.blob.BlobStoreManagerService.SessionStateChangeListener stateChangeListener) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        long creationTimeMs;
        long sessionId = com.android.internal.util.XmlUtils.readLongAttribute(in, "id");
        java.lang.String ownerPackageName = com.android.internal.util.XmlUtils.readStringAttribute(in, "p");
        int ownerUid = com.android.internal.util.XmlUtils.readIntAttribute(in, "u");
        if (version >= 5) {
            creationTimeMs = com.android.internal.util.XmlUtils.readLongAttribute(in, "crt");
        } else {
            creationTimeMs = java.lang.System.currentTimeMillis();
        }
        int depth = in.getDepth();
        android.app.blob.BlobHandle blobHandle = null;
        com.android.server.blob.BlobAccessMode blobAccessMode = null;
        while (com.android.internal.util.XmlUtils.nextElementWithin(in, depth)) {
            if ("bh".equals(in.getName())) {
                blobHandle = android.app.blob.BlobHandle.createFromXml(in);
            } else if ("am".equals(in.getName())) {
                blobAccessMode = com.android.server.blob.BlobAccessMode.createFromXml(in);
            }
        }
        if (blobHandle == null) {
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "blobHandle should be available");
            return null;
        }
        if (blobAccessMode == null) {
            android.util.Slog.wtf(com.android.server.blob.BlobStoreConfig.TAG, "blobAccessMode should be available");
            return null;
        }
        com.android.server.blob.BlobStoreSession blobStoreSession = new com.android.server.blob.BlobStoreSession(context, sessionId, blobHandle, ownerUid, ownerPackageName, creationTimeMs, stateChangeListener);
        blobStoreSession.mBlobAccessMode.allow(blobAccessMode);
        return blobStoreSession;
    }
}
