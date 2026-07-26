package com.android.server.uri;

/* JADX INFO: loaded from: classes3.dex */
public interface UriGrantsManagerInternal {
    boolean checkAuthorityGrants(int i, android.content.pm.ProviderInfo providerInfo, int i2, boolean z);

    int checkGrantUriPermission(int i, java.lang.String str, android.net.Uri uri, int i2, int i3);

    com.android.server.uri.NeededUriGrants checkGrantUriPermissionFromIntent(android.content.Intent intent, int i, java.lang.String str, int i2);

    com.android.server.uri.NeededUriGrants checkGrantUriPermissionFromIntent(android.content.Intent intent, int i, java.lang.String str, int i2, int i3);

    boolean checkUriPermission(com.android.server.uri.GrantUri grantUri, int i, int i2, boolean z);

    void dump(java.io.PrintWriter printWriter, boolean z, java.lang.String str);

    void grantUriPermissionUncheckedFromIntent(com.android.server.uri.NeededUriGrants neededUriGrants, com.android.server.uri.UriPermissionOwner uriPermissionOwner);

    android.os.IBinder newUriPermissionOwner(java.lang.String str);

    void onSystemReady();

    void removeUriPermissionIfNeeded(com.android.server.uri.UriPermission uriPermission);

    void removeUriPermissionsForPackage(java.lang.String str, int i, boolean z, boolean z2);

    void revokeUriPermission(java.lang.String str, int i, com.android.server.uri.GrantUri grantUri, int i2);

    void revokeUriPermissionFromOwner(android.os.IBinder iBinder, android.net.Uri uri, int i, int i2);

    void revokeUriPermissionFromOwner(android.os.IBinder iBinder, android.net.Uri uri, int i, int i2, java.lang.String str, int i3);
}
