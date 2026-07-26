package com.android.server.location.provider;

/* JADX INFO: loaded from: classes2.dex */
public class PassiveLocationProviderManager extends com.android.server.location.provider.LocationProviderManager {
    @Override // com.android.server.location.provider.LocationProviderManager, com.android.server.location.listeners.ListenerMultiplexer
    protected /* bridge */ /* synthetic */ android.location.provider.ProviderRequest mergeRegistrations(java.util.Collection collection) {
        return mergeRegistrations((java.util.Collection<com.android.server.location.provider.LocationProviderManager.Registration>) collection);
    }

    public PassiveLocationProviderManager(android.content.Context context, com.android.server.location.injector.Injector injector) {
        super(context, injector, "passive", null);
    }

    @Override // com.android.server.location.provider.LocationProviderManager
    public void setRealProvider(com.android.server.location.provider.AbstractLocationProvider provider) {
        com.android.internal.util.Preconditions.checkArgument(provider instanceof com.android.server.location.provider.PassiveLocationProvider);
        super.setRealProvider(provider);
    }

    @Override // com.android.server.location.provider.LocationProviderManager
    public void setMockProvider(com.android.server.location.provider.MockLocationProvider provider) {
        if (provider != null) {
            throw new java.lang.IllegalArgumentException("Cannot mock the passive provider");
        }
    }

    public void updateLocation(android.location.LocationResult locationResult) {
        synchronized (this.mMultiplexerLock) {
            com.android.server.location.provider.PassiveLocationProvider passive = (com.android.server.location.provider.PassiveLocationProvider) this.mProvider.getProvider();
            com.android.internal.util.Preconditions.checkState(passive != null);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                passive.updateLocation(locationResult);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.location.provider.LocationProviderManager, com.android.server.location.listeners.ListenerMultiplexer
    protected android.location.provider.ProviderRequest mergeRegistrations(java.util.Collection<com.android.server.location.provider.LocationProviderManager.Registration> registrations) {
        return new android.location.provider.ProviderRequest.Builder().setIntervalMillis(0L).build();
    }

    @Override // com.android.server.location.provider.LocationProviderManager
    protected long calculateRequestDelayMillis(long newIntervalMs, java.util.Collection<com.android.server.location.provider.LocationProviderManager.Registration> registrations) {
        return 0L;
    }

    @Override // com.android.server.location.provider.LocationProviderManager, com.android.server.location.listeners.ListenerMultiplexer
    protected java.lang.String getServiceState() {
        return this.mProvider.getCurrentRequest().isActive() ? "registered" : "unregistered";
    }
}
