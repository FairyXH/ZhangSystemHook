package com.android.server.role;

/* JADX INFO: loaded from: classes3.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public interface RoleServicePlatformHelper {
    java.lang.String computePackageStateHash(int i);

    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getLegacyRoleState(int i);
}
