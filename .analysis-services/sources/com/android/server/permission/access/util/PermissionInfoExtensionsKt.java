package com.android.server.permission.access.util;

/* JADX INFO: compiled from: PermissionInfoExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\"\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003\"\u0016\u0010\u0004\u001a\u00020\u0001*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0003¨\u0006\u0005"}, d2 = {"isInternal", "", "Landroid/content/pm/PermissionInfo;", "(Landroid/content/pm/PermissionInfo;)Z", "isRuntime", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class PermissionInfoExtensionsKt {
    public static final boolean isRuntime(android.content.pm.PermissionInfo $this$isRuntime) {
        return $this$isRuntime.getProtection() == 1;
    }

    public static final boolean isInternal(android.content.pm.PermissionInfo $this$isInternal) {
        return $this$isInternal.getProtection() == 4;
    }
}
