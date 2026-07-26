package com.android.server.job;

/* JADX INFO: loaded from: classes2.dex */
public final class GrantedUriPermissions {
    private final int mGrantFlags;
    private final android.os.IBinder mPermissionOwner;
    private final int mSourceUserId;
    private final java.lang.String mTag;
    private final java.util.ArrayList<android.net.Uri> mUris = new java.util.ArrayList<>();
    private final com.android.server.uri.UriGrantsManagerInternal mUriGrantsManagerInternal = (com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class);

    private GrantedUriPermissions(int grantFlags, int uid, java.lang.String tag) throws android.os.RemoteException {
        this.mGrantFlags = grantFlags;
        this.mSourceUserId = android.os.UserHandle.getUserId(uid);
        this.mTag = tag;
        this.mPermissionOwner = this.mUriGrantsManagerInternal.newUriPermissionOwner("job: " + tag);
    }

    public void revoke() {
        for (int i = this.mUris.size() - 1; i >= 0; i--) {
            this.mUriGrantsManagerInternal.revokeUriPermissionFromOwner(this.mPermissionOwner, this.mUris.get(i), this.mGrantFlags, this.mSourceUserId);
        }
        this.mUris.clear();
    }

    public static boolean checkGrantFlags(int grantFlags) {
        return (grantFlags & 3) != 0;
    }

    public static com.android.server.job.GrantedUriPermissions createFromIntent(android.content.Intent intent, int sourceUid, java.lang.String targetPackage, int targetUserId, java.lang.String tag) {
        int grantFlags = intent.getFlags();
        if (!checkGrantFlags(grantFlags)) {
            return null;
        }
        com.android.server.job.GrantedUriPermissions perms = null;
        android.net.Uri data = intent.getData();
        if (data != null) {
            perms = grantUri(data, sourceUid, targetPackage, targetUserId, grantFlags, tag, null);
        }
        android.content.ClipData clip = intent.getClipData();
        if (clip != null) {
            return grantClip(clip, sourceUid, targetPackage, targetUserId, grantFlags, tag, perms);
        }
        return perms;
    }

    public static com.android.server.job.GrantedUriPermissions createFromClip(android.content.ClipData clip, int sourceUid, java.lang.String targetPackage, int targetUserId, int grantFlags, java.lang.String tag) {
        if (!checkGrantFlags(grantFlags) || clip == null) {
            return null;
        }
        com.android.server.job.GrantedUriPermissions perms = grantClip(clip, sourceUid, targetPackage, targetUserId, grantFlags, tag, null);
        return perms;
    }

    private static com.android.server.job.GrantedUriPermissions grantClip(android.content.ClipData clip, int sourceUid, java.lang.String targetPackage, int targetUserId, int grantFlags, java.lang.String tag, com.android.server.job.GrantedUriPermissions curPerms) {
        int N = clip.getItemCount();
        for (int i = 0; i < N; i++) {
            curPerms = grantItem(clip.getItemAt(i), sourceUid, targetPackage, targetUserId, grantFlags, tag, curPerms);
        }
        return curPerms;
    }

    private static com.android.server.job.GrantedUriPermissions grantUri(android.net.Uri uri, int sourceUid, java.lang.String targetPackage, int targetUserId, int grantFlags, java.lang.String tag, com.android.server.job.GrantedUriPermissions curPerms) {
        try {
            int sourceUserId = android.content.ContentProvider.getUserIdFromUri(uri, android.os.UserHandle.getUserId(sourceUid));
            android.net.Uri uri2 = android.content.ContentProvider.getUriWithoutUserId(uri);
            if (curPerms == null) {
                curPerms = new com.android.server.job.GrantedUriPermissions(grantFlags, sourceUid, tag);
            }
            android.app.UriGrantsManager.getService().grantUriPermissionFromOwner(curPerms.mPermissionOwner, sourceUid, targetPackage, uri2, grantFlags, sourceUserId, targetUserId);
            curPerms.mUris.add(uri2);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e("JobScheduler", "AM dead");
        }
        return curPerms;
    }

    private static com.android.server.job.GrantedUriPermissions grantItem(android.content.ClipData.Item item, int sourceUid, java.lang.String targetPackage, int targetUserId, int grantFlags, java.lang.String tag, com.android.server.job.GrantedUriPermissions curPerms) {
        if (item.getUri() != null) {
            curPerms = grantUri(item.getUri(), sourceUid, targetPackage, targetUserId, grantFlags, tag, curPerms);
        }
        android.content.Intent intent = item.getIntent();
        if (intent != null && intent.getData() != null) {
            return grantUri(intent.getData(), sourceUid, targetPackage, targetUserId, grantFlags, tag, curPerms);
        }
        return curPerms;
    }

    public void dump(java.io.PrintWriter pw) {
        pw.print("mGrantFlags=0x");
        pw.print(java.lang.Integer.toHexString(this.mGrantFlags));
        pw.print(" mSourceUserId=");
        pw.println(this.mSourceUserId);
        pw.print("mTag=");
        pw.println(this.mTag);
        pw.print("mPermissionOwner=");
        pw.println(this.mPermissionOwner);
        for (int i = 0; i < this.mUris.size(); i++) {
            pw.print("#");
            pw.print(i);
            pw.print(": ");
            pw.println(this.mUris.get(i));
        }
    }

    public void dump(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, this.mGrantFlags);
        proto.write(1120986464258L, this.mSourceUserId);
        proto.write(1138166333443L, this.mTag);
        proto.write(1138166333444L, this.mPermissionOwner.toString());
        for (int i = 0; i < this.mUris.size(); i++) {
            android.net.Uri u = this.mUris.get(i);
            if (u != null) {
                proto.write(2237677961221L, u.toString());
            }
        }
        proto.end(token);
    }
}
