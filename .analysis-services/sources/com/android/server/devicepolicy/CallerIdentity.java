package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class CallerIdentity {
    private final android.content.ComponentName mComponentName;
    private final java.lang.String mPackageName;
    private final int mUid;

    CallerIdentity(int uid, java.lang.String packageName, android.content.ComponentName componentName) {
        this.mUid = uid;
        this.mPackageName = packageName;
        this.mComponentName = componentName;
    }

    public int getUid() {
        return this.mUid;
    }

    public int getUserId() {
        return android.os.UserHandle.getUserId(this.mUid);
    }

    public android.os.UserHandle getUserHandle() {
        return android.os.UserHandle.getUserHandleForUid(this.mUid);
    }

    public java.lang.String getPackageName() {
        return this.mPackageName;
    }

    public android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    public boolean hasAdminComponent() {
        return this.mComponentName != null;
    }

    public boolean hasPackage() {
        return this.mPackageName != null;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder("CallerIdentity[uid=").append(this.mUid);
        if (this.mPackageName != null) {
            builder.append(", pkg=").append(this.mPackageName);
        }
        if (this.mComponentName != null) {
            builder.append(", cmp=").append(this.mComponentName.flattenToShortString());
        }
        return builder.append("]").toString();
    }
}
