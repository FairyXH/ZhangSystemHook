package com.android.server.location.provider;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class LocationProviderManager$$ExternalSyntheticLambda24 implements java.util.function.Predicate {
    @Override // java.util.function.Predicate
    public final boolean test(java.lang.Object obj) {
        return ((com.android.server.location.provider.LocationProviderManager.Registration) obj).onProviderLocationRequestChanged();
    }
}
