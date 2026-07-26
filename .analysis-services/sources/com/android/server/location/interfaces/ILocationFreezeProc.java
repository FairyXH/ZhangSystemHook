package com.android.server.location.interfaces;

/* JADX INFO: loaded from: classes2.dex */
public interface ILocationFreezeProc extends com.android.server.location.common.IOplusCommonFeature {
    public static final com.android.server.location.interfaces.ILocationFreezeProc DEFAULT = new com.android.server.location.interfaces.ILocationFreezeProc() { // from class: com.android.server.location.interfaces.ILocationFreezeProc.1
    };
    public static final java.lang.String Name = "ILocationFreezeProc";

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.OplusLbsFeatureList.OplusIndex index() {
        return com.android.server.location.common.OplusLbsFeatureList.OplusIndex.ILocationFreezeProc;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean storeLocationRequest(com.android.server.location.provider.LocationProviderManager provider, android.location.LocationRequest request, android.location.util.identity.CallerIdentity identity, int permissionLevel, android.location.ILocationListener listener) {
        return true;
    }

    default boolean storeLocationRequest(com.android.server.location.provider.LocationProviderManager provider, android.location.LocationRequest request, android.location.util.identity.CallerIdentity identity, int permissionLevel, android.app.PendingIntent intent) {
        return true;
    }

    default void removeLocationRequest(java.lang.Object listener) {
    }

    default boolean storeMeasurementRequest(com.android.server.location.gnss.GnssMeasurementsProvider provider, android.location.GnssMeasurementRequest request, android.location.util.identity.CallerIdentity identity, android.location.IGnssMeasurementsListener listener) {
        return true;
    }

    default void removeMeasurementRequest(java.lang.Object listener) {
    }

    default void onBinderDied(java.lang.Object listener, int uid, java.lang.String packageName) {
    }

    default boolean freezeLocationProcess(java.lang.String pkg, boolean isFreeze, int uid) {
        return true;
    }
}
