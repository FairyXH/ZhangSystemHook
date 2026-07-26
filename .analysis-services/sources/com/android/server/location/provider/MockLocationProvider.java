package com.android.server.location.provider;

/* JADX INFO: loaded from: classes2.dex */
public class MockLocationProvider extends com.android.server.location.provider.AbstractLocationProvider {
    private android.location.Location mLocation;

    public MockLocationProvider(android.location.provider.ProviderProperties properties, android.location.util.identity.CallerIdentity identity, java.util.Set<java.lang.String> extraAttributionTags) {
        super(com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, identity, properties, extraAttributionTags);
    }

    public void setProviderAllowed(boolean allowed) {
        setAllowed(allowed);
    }

    public void setProviderLocation(android.location.Location l) {
        android.location.Location location = new android.location.Location(l);
        location.setIsFromMockProvider(true);
        this.mLocation = location;
        try {
            reportLocation(android.location.LocationResult.wrap(new android.location.Location[]{location}).validate());
        } catch (android.location.LocationResult.BadLocationException e) {
            throw new java.lang.IllegalArgumentException((java.lang.Throwable) e);
        }
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void onSetRequest(android.location.provider.ProviderRequest request) {
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onFlush(java.lang.Runnable callback) {
        callback.run();
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onExtraCommand(int uid, int pid, java.lang.String command, android.os.Bundle extras) {
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        pw.println("last mock location=" + this.mLocation);
    }
}
