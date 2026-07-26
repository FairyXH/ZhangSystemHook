package com.android.server.location.provider.proxy;

/* JADX INFO: loaded from: classes2.dex */
public class ProxyGeocodeProvider {
    private final com.android.server.servicewatcher.ServiceWatcher mServiceWatcher;

    public static com.android.server.location.provider.proxy.ProxyGeocodeProvider createAndRegister(android.content.Context context) {
        com.android.server.location.provider.proxy.ProxyGeocodeProvider proxy = new com.android.server.location.provider.proxy.ProxyGeocodeProvider(context);
        if (proxy.register()) {
            return proxy;
        }
        return null;
    }

    private ProxyGeocodeProvider(android.content.Context context) {
        this.mServiceWatcher = com.android.server.servicewatcher.ServiceWatcher.create(context, "GeocoderProxy", com.android.server.servicewatcher.CurrentUserServiceSupplier.createFromConfig(context, "com.android.location.service.GeocodeProvider", android.R.bool.config_enableCrossTaskScaleUpAnimation, android.R.string.config_healthConnectMigratorPackageName), null);
    }

    private boolean register() {
        boolean resolves = this.mServiceWatcher.checkServiceResolves();
        if (resolves) {
            this.mServiceWatcher.register();
        }
        return resolves;
    }

    public void reverseGeocode(final android.location.provider.ReverseGeocodeRequest request, final android.location.provider.IGeocodeCallback callback) {
        this.mServiceWatcher.runOnBinder(new com.android.server.servicewatcher.ServiceWatcher.BinderOperation() { // from class: com.android.server.location.provider.proxy.ProxyGeocodeProvider.1
            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public void run(android.os.IBinder binder) throws android.os.RemoteException {
                android.location.provider.IGeocodeProvider.Stub.asInterface(binder).reverseGeocode(request, callback);
            }

            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public void onError(java.lang.Throwable t) {
                try {
                    callback.onError(t.toString());
                } catch (android.os.RemoteException e) {
                }
            }
        });
    }

    public void forwardGeocode(final android.location.provider.ForwardGeocodeRequest request, final android.location.provider.IGeocodeCallback callback) {
        this.mServiceWatcher.runOnBinder(new com.android.server.servicewatcher.ServiceWatcher.BinderOperation() { // from class: com.android.server.location.provider.proxy.ProxyGeocodeProvider.2
            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public void run(android.os.IBinder binder) throws android.os.RemoteException {
                android.location.provider.IGeocodeProvider.Stub.asInterface(binder).forwardGeocode(request, callback);
            }

            @Override // com.android.server.servicewatcher.ServiceWatcher.BinderOperation
            public void onError(java.lang.Throwable t) {
                try {
                    callback.onError(t.toString());
                } catch (android.os.RemoteException e) {
                }
            }
        });
    }
}
