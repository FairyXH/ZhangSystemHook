package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public interface PolicyUpgraderDataProvider {
    java.util.function.Function<android.content.ComponentName, android.app.admin.DeviceAdminInfo> getAdminInfoSupplier(int i);

    java.util.List<java.lang.String> getPlatformSuspendedPackages(int i);

    int[] getUsersForUpgrade();

    com.android.internal.util.JournaledFile makeDevicePoliciesJournaledFile(int i);

    com.android.internal.util.JournaledFile makePoliciesVersionJournaledFile(int i);
}
