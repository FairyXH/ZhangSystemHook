package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class SystemServerSocExtImpl implements com.android.server.ISystemServerSocExt {
    private static final java.lang.String TAG = "SystemServerSocExtImpl";

    public SystemServerSocExtImpl(java.lang.Object systemServer) {
    }

    @Override // com.android.server.ISystemServerSocExt
    public void startServiceForActivityTrigger(com.android.server.SystemServiceManager systemServiceManager) {
        systemServiceManager.startService(com.android.server.ActivityTriggerService.class);
    }

    @Override // com.android.server.ISystemServerSocExt
    public void setPrameters(com.android.server.SystemServiceManager ssm, android.content.Context context) {
    }

    @Override // com.android.server.ISystemServerSocExt
    public void startOtherServices() {
    }
}
