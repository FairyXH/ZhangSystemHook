package com.android.server.location.geofence;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class GeofenceProxy$GeofenceProxyServiceConnection$$ExternalSyntheticLambda0 implements com.android.server.servicewatcher.ServiceWatcher.BinderOperation {
    public final /* synthetic */ com.android.server.location.geofence.GeofenceProxy f$0;

    @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
    public final void run(android.os.IBinder iBinder) throws android.os.RemoteException {
        this.f$0.updateGeofenceHardware(iBinder);
    }
}
