package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public final class InlineReplyUriRecord {
    private final java.lang.String mKey;
    private final java.lang.String mPackageName;
    private final android.os.IBinder mPermissionOwner;
    private final android.util.ArraySet<android.net.Uri> mUris = new android.util.ArraySet<>();
    private final android.os.UserHandle mUser;

    public InlineReplyUriRecord(android.os.IBinder owner, android.os.UserHandle user, java.lang.String packageName, java.lang.String key) {
        this.mPermissionOwner = owner;
        this.mUser = user;
        this.mPackageName = packageName;
        this.mKey = key;
    }

    public android.os.IBinder getPermissionOwner() {
        return this.mPermissionOwner;
    }

    public android.util.ArraySet<android.net.Uri> getUris() {
        return this.mUris;
    }

    public void addUri(android.net.Uri uri) {
        this.mUris.add(uri);
    }

    public int getUserId() {
        int userId = this.mUser.getIdentifier();
        if (android.os.UserManager.isHeadlessSystemUserMode() && userId == -1) {
            return android.app.ActivityManager.getCurrentUser();
        }
        if (userId == -1) {
            return 0;
        }
        return userId;
    }

    public java.lang.String getPackageName() {
        return this.mPackageName;
    }

    public java.lang.String getKey() {
        return this.mKey;
    }
}
