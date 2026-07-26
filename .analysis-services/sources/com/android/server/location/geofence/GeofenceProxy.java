package com.android.server.location.geofence;

/* JADX INFO: loaded from: classes2.dex */
public final class GeofenceProxy implements com.android.server.servicewatcher.ServiceWatcher.ServiceListener<com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo> {
    private static final java.lang.String SERVICE_ACTION = "com.android.location.service.GeofenceProvider";
    private static final java.lang.String TAG = "GeofenceProxy";
    volatile android.hardware.location.IGeofenceHardware mGeofenceHardware = null;
    final android.location.IGpsGeofenceHardware mGpsGeofenceHardware;
    final com.android.server.servicewatcher.ServiceWatcher mServiceWatcher;

    public static com.android.server.location.geofence.GeofenceProxy createAndBind(android.content.Context context, android.location.IGpsGeofenceHardware gpsGeofence) {
        com.android.server.location.geofence.GeofenceProxy proxy = new com.android.server.location.geofence.GeofenceProxy(context, gpsGeofence);
        if (proxy.register(context)) {
            return proxy;
        }
        return null;
    }

    private GeofenceProxy(android.content.Context context, android.location.IGpsGeofenceHardware gpsGeofence) {
        this.mGpsGeofenceHardware = (android.location.IGpsGeofenceHardware) java.util.Objects.requireNonNull(gpsGeofence);
        this.mServiceWatcher = com.android.server.servicewatcher.ServiceWatcher.create(context, TAG, com.android.server.servicewatcher.CurrentUserServiceSupplier.createFromConfig(context, SERVICE_ACTION, android.R.bool.config_enableDefaultHdrConversionPassthrough, android.R.string.config_help_url_action_disabled_by_advanced_protection), this);
    }

    void updateGeofenceHardware(android.os.IBinder binder) throws android.os.RemoteException {
        android.location.IGeofenceProvider.Stub.asInterface(binder).setGeofenceHardware(this.mGeofenceHardware);
    }

    private boolean register(android.content.Context context) {
        boolean resolves = this.mServiceWatcher.checkServiceResolves();
        if (resolves) {
            this.mServiceWatcher.register();
            context.bindServiceAsUser(new android.content.Intent(context, (java.lang.Class<?>) android.hardware.location.GeofenceHardwareService.class), new com.android.server.location.geofence.GeofenceProxy.GeofenceProxyServiceConnection(), 1, android.os.UserHandle.SYSTEM);
        }
        return resolves;
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onBind(android.os.IBinder binder, com.android.server.servicewatcher.CurrentUserServiceSupplier.BoundServiceInfo boundServiceInfo) throws android.os.RemoteException {
        updateGeofenceHardware(binder);
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceListener
    public void onUnbind() {
    }

    private class GeofenceProxyServiceConnection implements android.content.ServiceConnection {
        GeofenceProxyServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            android.hardware.location.IGeofenceHardware geofenceHardware = android.hardware.location.IGeofenceHardware.Stub.asInterface(service);
            try {
                geofenceHardware.setGpsGeofenceHardware(com.android.server.location.geofence.GeofenceProxy.this.mGpsGeofenceHardware);
                com.android.server.location.geofence.GeofenceProxy.this.mGeofenceHardware = geofenceHardware;
                com.android.server.location.geofence.GeofenceProxy.this.mServiceWatcher.runOnBinder(new com.android.server.location.geofence.GeofenceProxy$GeofenceProxyServiceConnection$$ExternalSyntheticLambda0(com.android.server.location.geofence.GeofenceProxy.this));
            } catch (android.os.RemoteException e) {
                android.util.Log.w(com.android.server.location.geofence.GeofenceProxy.TAG, "unable to initialize geofence hardware", e);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            com.android.server.location.geofence.GeofenceProxy.this.mGeofenceHardware = null;
            com.android.server.location.geofence.GeofenceProxy.this.mServiceWatcher.runOnBinder(new com.android.server.location.geofence.GeofenceProxy$GeofenceProxyServiceConnection$$ExternalSyntheticLambda0(com.android.server.location.geofence.GeofenceProxy.this));
        }
    }
}
