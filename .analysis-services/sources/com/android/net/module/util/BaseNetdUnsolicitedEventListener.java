package com.android.net.module.util;

/* JADX INFO: loaded from: classes.dex */
public class BaseNetdUnsolicitedEventListener extends android.net.INetdUnsolicitedEventListener.Stub {
    @Override // android.net.INetdUnsolicitedEventListener
    public void onInterfaceClassActivityChanged(boolean isActive, int timerLabel, long timestampNs, int uid) {
    }

    @Override // android.net.INetdUnsolicitedEventListener
    public void onQuotaLimitReached(java.lang.String alertName, java.lang.String ifName) {
    }

    @Override // android.net.INetdUnsolicitedEventListener
    public void onInterfaceDnsServerInfo(java.lang.String ifName, long lifetimeS, java.lang.String[] servers) {
    }

    @Override // android.net.INetdUnsolicitedEventListener
    public void onInterfaceAddressUpdated(java.lang.String addr, java.lang.String ifName, int flags, int scope) {
    }

    @Override // android.net.INetdUnsolicitedEventListener
    public void onInterfaceAddressRemoved(java.lang.String addr, java.lang.String ifName, int flags, int scope) {
    }

    @Override // android.net.INetdUnsolicitedEventListener
    public void onInterfaceAdded(java.lang.String ifName) {
    }

    @Override // android.net.INetdUnsolicitedEventListener
    public void onInterfaceRemoved(java.lang.String ifName) {
    }

    @Override // android.net.INetdUnsolicitedEventListener
    public void onInterfaceChanged(java.lang.String ifName, boolean up) {
    }

    @Override // android.net.INetdUnsolicitedEventListener
    public void onInterfaceLinkStateChanged(java.lang.String ifName, boolean up) {
    }

    @Override // android.net.INetdUnsolicitedEventListener
    public void onRouteChanged(boolean updated, java.lang.String route, java.lang.String gateway, java.lang.String ifName) {
    }

    @Override // android.net.INetdUnsolicitedEventListener
    public void onStrictCleartextDetected(int uid, java.lang.String hex) {
    }

    @Override // android.net.INetdUnsolicitedEventListener
    public int getInterfaceVersion() {
        return 15;
    }

    @Override // android.net.INetdUnsolicitedEventListener
    public java.lang.String getInterfaceHash() {
        return "2be6ff6fb01645cdddb3bb60f6de5727e5733267";
    }
}
