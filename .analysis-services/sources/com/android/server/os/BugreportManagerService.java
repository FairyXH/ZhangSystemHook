package com.android.server.os;

/* JADX INFO: loaded from: classes2.dex */
public class BugreportManagerService extends com.android.server.SystemService {
    private static final java.lang.String TAG = "BugreportManagerService";
    private com.android.server.os.BugreportManagerServiceImpl mService;

    public BugreportManagerService(android.content.Context context) {
        super(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.mService = new com.android.server.os.BugreportManagerServiceImpl(getContext());
        publishBinderService("bugreport", this.mService);
    }
}
