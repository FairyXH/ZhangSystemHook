package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessUri.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÖ\u0001J\b\u0010\u0012\u001a\u00020\u0013H\u0016R\u0011\u0010\u0005\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007R\u0011\u0010\t\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\n\u0010\u0007¨\u0006\u0015"}, d2 = {"Lcom/android/server/permission/access/UidUri;", "Lcom/android/server/permission/access/AccessUri;", "uid", "", "(I)V", "appId", "getAppId", "()I", "getUid", "userId", "getUserId", "component1", "copy", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "toString", "", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class UidUri extends com.android.server.permission.access.AccessUri {
    public static final com.android.server.permission.access.UidUri.Companion Companion = new com.android.server.permission.access.UidUri.Companion(null);
    public static final java.lang.String SCHEME = "uid";
    private final int uid;

    public static /* synthetic */ com.android.server.permission.access.UidUri copy$default(com.android.server.permission.access.UidUri uidUri, int i, int i2, java.lang.Object obj) {
        if ((i2 & 1) != 0) {
            i = uidUri.uid;
        }
        return uidUri.copy(i);
    }

    public final int component1() {
        return this.uid;
    }

    public final com.android.server.permission.access.UidUri copy(int i) {
        return new com.android.server.permission.access.UidUri(i);
    }

    @Override // com.android.server.permission.access.AccessUri
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof com.android.server.permission.access.UidUri) && this.uid == ((com.android.server.permission.access.UidUri) obj).uid;
    }

    @Override // com.android.server.permission.access.AccessUri
    public int hashCode() {
        return java.lang.Integer.hashCode(this.uid);
    }

    public UidUri(int uid) {
        super("uid", null);
        this.uid = uid;
    }

    public final int getUid() {
        return this.uid;
    }

    public final int getUserId() {
        return android.os.UserHandle.getUserId(this.uid);
    }

    public final int getAppId() {
        return android.os.UserHandle.getAppId(this.uid);
    }

    @Override // com.android.server.permission.access.AccessUri
    public java.lang.String toString() {
        return getScheme() + ":///" + this.uid;
    }

    /* JADX INFO: compiled from: AccessUri.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lcom/android/server/permission/access/UidUri$Companion;", "", "()V", "SCHEME", "", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
