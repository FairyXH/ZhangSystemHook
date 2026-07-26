package com.android.server.os;

/* JADX INFO: loaded from: classes2.dex */
public class NativeTombstoneManagerService extends com.android.server.SystemService {
    private static final java.lang.String TAG = "NativeTombstoneManagerService";
    private com.android.server.os.NativeTombstoneManager mManager;

    public NativeTombstoneManagerService(android.content.Context context) {
        super(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.mManager = new com.android.server.os.NativeTombstoneManager(getContext());
        com.android.server.LocalServices.addService(com.android.server.os.NativeTombstoneManager.class, this.mManager);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 550) {
            this.mManager.onSystemReady();
        }
    }
}
