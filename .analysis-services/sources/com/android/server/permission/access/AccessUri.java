package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessUri.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001B\u000f\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0013\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\u0003H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u0082\u0001\u0005\r\u000e\u000f\u0010\u0011¨\u0006\u0012"}, d2 = {"Lcom/android/server/permission/access/AccessUri;", "", "scheme", "", "(Ljava/lang/String;)V", "getScheme", "()Ljava/lang/String;", "equals", "", "other", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "Lcom/android/server/permission/access/AppOpUri;", "Lcom/android/server/permission/access/DevicePermissionUri;", "Lcom/android/server/permission/access/PackageUri;", "Lcom/android/server/permission/access/PermissionUri;", "Lcom/android/server/permission/access/UidUri;", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AccessUri {
    private final java.lang.String scheme;

    public /* synthetic */ AccessUri(java.lang.String str, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(str);
    }

    private AccessUri(java.lang.String scheme) {
        this.scheme = scheme;
    }

    public final java.lang.String getScheme() {
        return this.scheme;
    }

    public boolean equals(java.lang.Object other) {
        throw new com.android.server.permission.jarjar.kotlin.NotImplementedError(null, 1, null);
    }

    public int hashCode() {
        throw new com.android.server.permission.jarjar.kotlin.NotImplementedError(null, 1, null);
    }

    public java.lang.String toString() {
        throw new com.android.server.permission.jarjar.kotlin.NotImplementedError(null, 1, null);
    }
}
