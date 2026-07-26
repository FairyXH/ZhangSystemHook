package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class InputContentUriTokenHandler extends com.android.internal.inputmethod.IInputContentUriToken.Stub {
    private final java.lang.Object mLock = new java.lang.Object();
    private android.os.IBinder mPermissionOwnerToken = null;
    private final int mSourceUid;
    private final int mSourceUserId;
    private final java.lang.String mTargetPackage;
    private final int mTargetUserId;
    private final android.net.Uri mUri;

    InputContentUriTokenHandler(android.net.Uri contentUri, int sourceUid, java.lang.String targetPackage, int sourceUserId, int targetUserId) {
        this.mUri = contentUri;
        this.mSourceUid = sourceUid;
        this.mTargetPackage = targetPackage;
        this.mSourceUserId = sourceUserId;
        this.mTargetUserId = targetUserId;
    }

    public void take() {
        synchronized (this.mLock) {
            if (this.mPermissionOwnerToken != null) {
                return;
            }
            this.mPermissionOwnerToken = ((com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class)).newUriPermissionOwner("InputContentUriTokenHandler");
            doTakeLocked(this.mPermissionOwnerToken);
        }
    }

    private void doTakeLocked(android.os.IBinder permissionOwner) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            try {
                android.app.UriGrantsManager.getService().grantUriPermissionFromOwner(permissionOwner, this.mSourceUid, this.mTargetPackage, this.mUri, 1, this.mSourceUserId, this.mTargetUserId);
            } catch (android.os.RemoteException e) {
                e.rethrowFromSystemServer();
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void release() {
        synchronized (this.mLock) {
            if (this.mPermissionOwnerToken == null) {
                return;
            }
            try {
                ((com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class)).revokeUriPermissionFromOwner(this.mPermissionOwnerToken, this.mUri, 1, this.mSourceUserId);
            } finally {
                this.mPermissionOwnerToken = null;
            }
        }
    }

    protected void finalize() throws java.lang.Throwable {
        try {
            release();
        } finally {
            super/*java.lang.Object*/.finalize();
        }
    }
}
