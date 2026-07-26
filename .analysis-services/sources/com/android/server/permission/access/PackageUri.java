package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessUri.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0086\b\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0005HÖ\u0001J\b\u0010\u0013\u001a\u00020\u0003H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0015"}, d2 = {"Lcom/android/server/permission/access/PackageUri;", "Lcom/android/server/permission/access/AccessUri;", com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, "", "userId", "", "(Ljava/lang/String;I)V", "getPackageName", "()Ljava/lang/String;", "getUserId", "()I", "component1", "component2", "copy", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "toString", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class PackageUri extends com.android.server.permission.access.AccessUri {
    public static final com.android.server.permission.access.PackageUri.Companion Companion = new com.android.server.permission.access.PackageUri.Companion(null);
    public static final java.lang.String SCHEME = "package";
    private final java.lang.String packageName;
    private final int userId;

    public static /* synthetic */ com.android.server.permission.access.PackageUri copy$default(com.android.server.permission.access.PackageUri packageUri, java.lang.String str, int i, int i2, java.lang.Object obj) {
        if ((i2 & 1) != 0) {
            str = packageUri.packageName;
        }
        if ((i2 & 2) != 0) {
            i = packageUri.userId;
        }
        return packageUri.copy(str, i);
    }

    public final java.lang.String component1() {
        return this.packageName;
    }

    public final int component2() {
        return this.userId;
    }

    public final com.android.server.permission.access.PackageUri copy(java.lang.String str, int i) {
        return new com.android.server.permission.access.PackageUri(str, i);
    }

    @Override // com.android.server.permission.access.AccessUri
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.permission.access.PackageUri)) {
            return false;
        }
        com.android.server.permission.access.PackageUri packageUri = (com.android.server.permission.access.PackageUri) obj;
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.packageName, packageUri.packageName) && this.userId == packageUri.userId;
    }

    @Override // com.android.server.permission.access.AccessUri
    public int hashCode() {
        return (this.packageName.hashCode() * 31) + java.lang.Integer.hashCode(this.userId);
    }

    public PackageUri(java.lang.String packageName, int userId) {
        super("package", null);
        this.packageName = packageName;
        this.userId = userId;
    }

    public final java.lang.String getPackageName() {
        return this.packageName;
    }

    public final int getUserId() {
        return this.userId;
    }

    @Override // com.android.server.permission.access.AccessUri
    public java.lang.String toString() {
        return getScheme() + ":///" + this.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.userId;
    }

    /* JADX INFO: compiled from: AccessUri.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lcom/android/server/permission/access/PackageUri$Companion;", "", "()V", "SCHEME", "", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
