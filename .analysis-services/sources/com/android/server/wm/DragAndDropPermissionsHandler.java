package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DragAndDropPermissionsHandler extends com.android.internal.view.IDragAndDropPermissions.Stub {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "DragAndDrop";
    private final com.android.server.wm.WindowManagerGlobalLock mGlobalLock;
    private final int mMode;
    private final int mSourceUid;
    private final int mSourceUserId;
    private final java.lang.String mTargetPackage;
    private final int mTargetUserId;
    private final java.util.ArrayList<android.net.Uri> mUris = new java.util.ArrayList<>();
    private android.os.IBinder mActivityToken = null;
    private android.os.IBinder mPermissionOwnerToken = null;

    DragAndDropPermissionsHandler(com.android.server.wm.WindowManagerGlobalLock lock, android.content.ClipData clipData, int sourceUid, java.lang.String targetPackage, int mode, int sourceUserId, int targetUserId) {
        this.mGlobalLock = lock;
        this.mSourceUid = sourceUid;
        this.mTargetPackage = targetPackage;
        this.mMode = mode;
        this.mSourceUserId = sourceUserId;
        this.mTargetUserId = targetUserId;
        clipData.collectUris(this.mUris);
    }

    public void take(android.os.IBinder activityToken) throws android.os.RemoteException {
        if (this.mActivityToken != null || this.mPermissionOwnerToken != null) {
            return;
        }
        this.mActivityToken = activityToken;
        android.os.IBinder permissionOwner = getUriPermissionOwnerForActivity(this.mActivityToken);
        doTake(permissionOwner);
    }

    private void doTake(android.os.IBinder permissionOwner) throws android.os.RemoteException {
        long origId = android.os.Binder.clearCallingIdentity();
        for (int i = 0; i < this.mUris.size(); i++) {
            try {
                android.app.UriGrantsManager.getService().grantUriPermissionFromOwner(permissionOwner, this.mSourceUid, this.mTargetPackage, this.mUris.get(i), this.mMode, this.mSourceUserId, this.mTargetUserId);
            } finally {
                android.os.Binder.restoreCallingIdentity(origId);
            }
        }
    }

    public void takeTransient() throws android.os.RemoteException {
        if (this.mActivityToken != null || this.mPermissionOwnerToken != null) {
            return;
        }
        this.mPermissionOwnerToken = ((com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class)).newUriPermissionOwner("drop");
        doTake(this.mPermissionOwnerToken);
    }

    public void release() throws android.os.RemoteException {
        android.os.IBinder permissionOwner;
        if (this.mActivityToken == null && this.mPermissionOwnerToken == null) {
            return;
        }
        if (this.mActivityToken != null) {
            try {
                permissionOwner = getUriPermissionOwnerForActivity(this.mActivityToken);
            } catch (java.lang.Exception e) {
                return;
            } finally {
                this.mActivityToken = null;
            }
        } else {
            permissionOwner = this.mPermissionOwnerToken;
            this.mPermissionOwnerToken = null;
        }
        com.android.server.uri.UriGrantsManagerInternal ugm = (com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class);
        for (int i = 0; i < this.mUris.size(); i++) {
            ugm.revokeUriPermissionFromOwner(permissionOwner, this.mUris.get(i), this.mMode, this.mSourceUserId);
        }
    }

    private android.os.IBinder getUriPermissionOwnerForActivity(android.os.IBinder activityToken) {
        android.os.Binder externalToken;
        com.android.server.wm.ActivityTaskManagerService.enforceNotIsolatedCaller("getUriPermissionOwnerForActivity");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(activityToken);
                if (r == null) {
                    throw new java.lang.IllegalArgumentException("Activity does not exist; token=" + activityToken);
                }
                externalToken = r.getUriPermissionsLocked().getExternalToken();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return externalToken;
    }

    protected void finalize() throws java.lang.Throwable {
        if (this.mActivityToken != null || this.mPermissionOwnerToken == null) {
            return;
        }
        release();
    }
}
