package com.android.server.permission.access.util;

/* JADX INFO: compiled from: PermissionApex.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00068BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n8F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f¨\u0006\u0010"}, d2 = {"Lcom/android/server/permission/access/util/PermissionApex;", "", "()V", "MODULE_NAME", "", "apexEnvironment", "Landroid/content/ApexEnvironment;", "getApexEnvironment", "()Landroid/content/ApexEnvironment;", "systemDataDirectory", "Ljava/io/File;", "getSystemDataDirectory", "()Ljava/io/File;", "getUserDataDirectory", "userId", "", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class PermissionApex {
    public static final com.android.server.permission.access.util.PermissionApex INSTANCE = new com.android.server.permission.access.util.PermissionApex();
    private static final java.lang.String MODULE_NAME = "com.android.permission";

    private PermissionApex() {
    }

    public final java.io.File getSystemDataDirectory() {
        return getApexEnvironment().getDeviceProtectedDataDir();
    }

    public final java.io.File getUserDataDirectory(int userId) {
        return getApexEnvironment().getDeviceProtectedDataDirForUser(android.os.UserHandle.of(userId));
    }

    private final android.content.ApexEnvironment getApexEnvironment() {
        return android.content.ApexEnvironment.getApexEnvironment(MODULE_NAME);
    }
}
