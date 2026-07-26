package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessUri.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\b\u0010\u000f\u001a\u00020\u0003H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, d2 = {"Lcom/android/server/permission/access/AppOpUri;", "Lcom/android/server/permission/access/AccessUri;", "appOpName", "", "(Ljava/lang/String;)V", "getAppOpName", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AppOpUri extends com.android.server.permission.access.AccessUri {
    public static final com.android.server.permission.access.AppOpUri.Companion Companion = new com.android.server.permission.access.AppOpUri.Companion(null);
    public static final java.lang.String SCHEME = "app-op";
    private final java.lang.String appOpName;

    public static /* synthetic */ com.android.server.permission.access.AppOpUri copy$default(com.android.server.permission.access.AppOpUri appOpUri, java.lang.String str, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str = appOpUri.appOpName;
        }
        return appOpUri.copy(str);
    }

    public final java.lang.String component1() {
        return this.appOpName;
    }

    public final com.android.server.permission.access.AppOpUri copy(java.lang.String str) {
        return new com.android.server.permission.access.AppOpUri(str);
    }

    @Override // com.android.server.permission.access.AccessUri
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof com.android.server.permission.access.AppOpUri) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.appOpName, ((com.android.server.permission.access.AppOpUri) obj).appOpName);
    }

    @Override // com.android.server.permission.access.AccessUri
    public int hashCode() {
        return this.appOpName.hashCode();
    }

    public AppOpUri(java.lang.String appOpName) {
        super(SCHEME, null);
        this.appOpName = appOpName;
    }

    public final java.lang.String getAppOpName() {
        return this.appOpName;
    }

    @Override // com.android.server.permission.access.AccessUri
    public java.lang.String toString() {
        return getScheme() + ":///" + this.appOpName;
    }

    /* JADX INFO: compiled from: AccessUri.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lcom/android/server/permission/access/AppOpUri$Companion;", "", "()V", "SCHEME", "", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
