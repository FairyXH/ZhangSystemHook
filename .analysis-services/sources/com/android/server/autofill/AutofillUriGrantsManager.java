package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
final class AutofillUriGrantsManager {
    private static final java.lang.String TAG = com.android.server.autofill.AutofillUriGrantsManager.class.getSimpleName();
    private final int mSourceUid;
    private final int mSourceUserId;
    private final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskMgrInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
    private final android.app.IUriGrantsManager mUgm = android.app.UriGrantsManager.getService();

    AutofillUriGrantsManager(int serviceUid) {
        this.mSourceUid = serviceUid;
        this.mSourceUserId = android.os.UserHandle.getUserId(this.mSourceUid);
    }

    public void grantUriPermissions(android.content.ComponentName targetActivity, android.os.IBinder targetActivityToken, int targetUserId, android.content.ClipData clip) {
        java.lang.String targetPkg = targetActivity.getPackageName();
        android.os.IBinder permissionOwner = this.mActivityTaskMgrInternal.getUriPermissionOwnerForActivity(targetActivityToken);
        if (permissionOwner == null) {
            android.util.Slog.w(TAG, "Can't grant URI permissions, because the target activity token is invalid: clip=" + clip + ", targetActivity=" + targetActivity + ", targetUserId=" + targetUserId + ", targetActivityToken=" + java.lang.Integer.toHexString(targetActivityToken.hashCode()));
            return;
        }
        for (int i = 0; i < clip.getItemCount(); i++) {
            android.content.ClipData.Item item = clip.getItemAt(i);
            android.net.Uri uri = item.getUri();
            if (uri != null && com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(uri.getScheme())) {
                grantUriPermissions(uri, targetPkg, targetUserId, permissionOwner);
            }
        }
    }

    private void grantUriPermissions(android.net.Uri uri, java.lang.String targetPkg, int targetUserId, android.os.IBinder permissionOwner) throws java.lang.Throwable {
        java.lang.String str;
        java.lang.String str2;
        java.lang.String str3;
        java.lang.String str4;
        int sourceUserId = android.content.ContentProvider.getUserIdFromUri(uri, this.mSourceUserId);
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Granting URI permissions: uri=" + uri + ", sourceUid=" + this.mSourceUid + ", sourceUserId=" + sourceUserId + ", targetPkg=" + targetPkg + ", targetUserId=" + targetUserId + ", permissionOwner=" + java.lang.Integer.toHexString(permissionOwner.hashCode()));
        }
        android.net.Uri uriWithoutUserId = android.content.ContentProvider.getUriWithoutUserId(uri);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                str3 = ", permissionOwner=";
                str4 = ", sourceUid=";
                str = ", sourceUserId=";
                str2 = ", targetPkg=";
            } catch (android.os.RemoteException e) {
                e = e;
                str = ", sourceUserId=";
                str2 = ", targetPkg=";
                str3 = ", permissionOwner=";
                str4 = ", sourceUid=";
            }
            try {
                this.mUgm.grantUriPermissionFromOwner(permissionOwner, this.mSourceUid, targetPkg, uriWithoutUserId, 1, sourceUserId, targetUserId);
                android.os.Binder.restoreCallingIdentity(ident);
            } catch (android.os.RemoteException e2) {
                e = e2;
                try {
                    android.util.Slog.e(TAG, "Granting URI permissions failed: uri=" + uri + str4 + this.mSourceUid + str + sourceUserId + str2 + targetPkg + ", targetUserId=" + targetUserId + str3 + java.lang.Integer.toHexString(permissionOwner.hashCode()), e);
                    android.os.Binder.restoreCallingIdentity(ident);
                } catch (java.lang.Throwable th) {
                    th = th;
                    android.os.Binder.restoreCallingIdentity(ident);
                    throw th;
                }
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }
}
