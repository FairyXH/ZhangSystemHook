package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class CrossProfileAppsService extends com.android.server.SystemService {
    private final com.android.server.pm.CrossProfileAppsServiceImpl mServiceImpl;

    public CrossProfileAppsService(android.content.Context context) {
        super(context);
        this.mServiceImpl = new com.android.server.pm.CrossProfileAppsServiceImpl(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("crossprofileapps", this.mServiceImpl);
        publishLocalService(android.content.pm.CrossProfileAppsInternal.class, this.mServiceImpl.getLocalService());
    }
}
