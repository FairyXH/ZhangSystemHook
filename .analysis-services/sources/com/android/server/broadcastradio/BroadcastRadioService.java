package com.android.server.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public final class BroadcastRadioService extends com.android.server.SystemService {
    private final android.hardware.radio.IRadioService mServiceImpl;

    public BroadcastRadioService(android.content.Context context) {
        super(context);
        java.util.ArrayList<java.lang.String> serviceNameList = com.android.server.broadcastradio.IRadioServiceAidlImpl.getServicesNames();
        this.mServiceImpl = serviceNameList.isEmpty() ? new com.android.server.broadcastradio.IRadioServiceHidlImpl(this) : new com.android.server.broadcastradio.IRadioServiceAidlImpl(this, serviceNameList);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("broadcastradio", this.mServiceImpl.asBinder());
    }

    void enforcePolicyAccess() {
        if (getContext().checkCallingPermission("android.permission.ACCESS_BROADCAST_RADIO") != 0) {
            throw new java.lang.SecurityException("ACCESS_BROADCAST_RADIO permission not granted");
        }
    }
}
