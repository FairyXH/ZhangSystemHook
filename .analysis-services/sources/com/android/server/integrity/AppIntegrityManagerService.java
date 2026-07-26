package com.android.server.integrity;

/* JADX INFO: loaded from: classes2.dex */
public class AppIntegrityManagerService extends com.android.server.SystemService {
    private android.content.Context mContext;
    private com.android.server.integrity.AppIntegrityManagerServiceImpl mService;

    public AppIntegrityManagerService(android.content.Context context) {
        super(context);
        this.mContext = context;
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.mService = com.android.server.integrity.AppIntegrityManagerServiceImpl.create(this.mContext);
        publishBinderService("app_integrity", this.mService);
    }
}
