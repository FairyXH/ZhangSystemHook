package com.android.server.location.gnss;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class GnssLocationProvider$$ExternalSyntheticLambda20 implements android.location.LocationListener {
    public final /* synthetic */ com.android.server.location.gnss.GnssLocationProvider f$0;

    public /* synthetic */ GnssLocationProvider$$ExternalSyntheticLambda20(com.android.server.location.gnss.GnssLocationProvider gnssLocationProvider) {
        this.f$0 = gnssLocationProvider;
    }

    @Override // android.location.LocationListener
    public final void onLocationChanged(android.location.Location location) {
        this.f$0.injectLocation(location);
    }
}
