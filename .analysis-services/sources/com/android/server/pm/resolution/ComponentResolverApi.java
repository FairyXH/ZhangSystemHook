package com.android.server.pm.resolution;

/* JADX INFO: loaded from: classes2.dex */
public interface ComponentResolverApi {
    boolean componentExists(android.content.ComponentName componentName);

    void dumpActivityResolvers(java.io.PrintWriter printWriter, com.android.server.pm.DumpState dumpState, java.lang.String str);

    void dumpContentProviders(com.android.server.pm.Computer computer, java.io.PrintWriter printWriter, com.android.server.pm.DumpState dumpState, java.lang.String str);

    void dumpProviderResolvers(java.io.PrintWriter printWriter, com.android.server.pm.DumpState dumpState, java.lang.String str);

    void dumpReceiverResolvers(java.io.PrintWriter printWriter, com.android.server.pm.DumpState dumpState, java.lang.String str);

    void dumpServicePermissions(java.io.PrintWriter printWriter, com.android.server.pm.DumpState dumpState);

    void dumpServiceResolvers(java.io.PrintWriter printWriter, com.android.server.pm.DumpState dumpState, java.lang.String str);

    com.android.internal.pm.pkg.component.ParsedActivity getActivity(android.content.ComponentName componentName);

    com.android.internal.pm.pkg.component.ParsedProvider getProvider(android.content.ComponentName componentName);

    com.android.internal.pm.pkg.component.ParsedActivity getReceiver(android.content.ComponentName componentName);

    com.android.internal.pm.pkg.component.ParsedService getService(android.content.ComponentName componentName);

    boolean isActivityDefined(android.content.ComponentName componentName);

    java.util.List<android.content.pm.ResolveInfo> queryActivities(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String str, long j, int i);

    java.util.List<android.content.pm.ResolveInfo> queryActivities(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String str, long j, java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> list, int i);

    android.content.pm.ProviderInfo queryProvider(com.android.server.pm.Computer computer, java.lang.String str, long j, int i);

    java.util.List<android.content.pm.ResolveInfo> queryProviders(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String str, long j, int i);

    java.util.List<android.content.pm.ResolveInfo> queryProviders(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String str, long j, java.util.List<com.android.internal.pm.pkg.component.ParsedProvider> list, int i);

    java.util.List<android.content.pm.ProviderInfo> queryProviders(com.android.server.pm.Computer computer, java.lang.String str, java.lang.String str2, int i, long j, int i2);

    java.util.List<android.content.pm.ResolveInfo> queryReceivers(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String str, long j, int i);

    java.util.List<android.content.pm.ResolveInfo> queryReceivers(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String str, long j, java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> list, int i);

    java.util.List<android.content.pm.ResolveInfo> queryServices(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String str, long j, int i);

    java.util.List<android.content.pm.ResolveInfo> queryServices(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String str, long j, java.util.List<com.android.internal.pm.pkg.component.ParsedService> list, int i);

    void querySyncProviders(com.android.server.pm.Computer computer, java.util.List<java.lang.String> list, java.util.List<android.content.pm.ProviderInfo> list2, boolean z, int i);
}
