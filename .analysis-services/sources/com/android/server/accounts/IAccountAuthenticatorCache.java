package com.android.server.accounts;

/* JADX INFO: loaded from: classes.dex */
public interface IAccountAuthenticatorCache {
    void dump(java.io.FileDescriptor fileDescriptor, java.io.PrintWriter printWriter, java.lang.String[] strArr, int i);

    java.util.Collection<android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription>> getAllServices(int i);

    boolean getBindInstantServiceAllowed(int i);

    android.content.pm.RegisteredServicesCache.ServiceInfo<android.accounts.AuthenticatorDescription> getServiceInfo(android.accounts.AuthenticatorDescription authenticatorDescription, int i);

    void invalidateCache(int i);

    void setBindInstantServiceAllowed(int i, boolean z);

    void setListener(android.content.pm.RegisteredServicesCacheListener<android.accounts.AuthenticatorDescription> registeredServicesCacheListener, android.os.Handler handler);

    void updateServices(int i);
}
