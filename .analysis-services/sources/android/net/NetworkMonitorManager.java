package android.net;

/* JADX INFO: loaded from: classes.dex */
public class NetworkMonitorManager {
    private final android.net.INetworkMonitor mNetworkMonitor;
    private final java.lang.String mTag;

    public NetworkMonitorManager(android.net.INetworkMonitor networkMonitorManager, java.lang.String tag) {
        this.mNetworkMonitor = networkMonitorManager;
        this.mTag = tag;
    }

    public NetworkMonitorManager(android.net.INetworkMonitor networkMonitorManager) {
        this(networkMonitorManager, android.net.NetworkMonitorManager.class.getSimpleName());
    }

    private void log(java.lang.String s, java.lang.Throwable e) {
        android.util.Log.e(this.mTag, s, e);
    }

    public boolean start() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkMonitor.start();
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error in start", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean launchCaptivePortalApp() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkMonitor.launchCaptivePortalApp();
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error in launchCaptivePortalApp", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean notifyCaptivePortalAppFinished(int response) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkMonitor.notifyCaptivePortalAppFinished(response);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error in notifyCaptivePortalAppFinished", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean setAcceptPartialConnectivity() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkMonitor.setAcceptPartialConnectivity();
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error in setAcceptPartialConnectivity", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean forceReevaluation(int uid) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkMonitor.forceReevaluation(uid);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error in forceReevaluation", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean notifyPrivateDnsChanged(android.net.PrivateDnsConfigParcel config) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkMonitor.notifyPrivateDnsChanged(config);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error in notifyPrivateDnsChanged", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean notifyDnsResponse(int returnCode) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkMonitor.notifyDnsResponse(returnCode);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error in notifyDnsResponse", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    @java.lang.Deprecated
    public boolean notifyNetworkConnected(android.net.LinkProperties lp, android.net.NetworkCapabilities nc) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkMonitor.notifyNetworkConnected(lp, nc);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error in notifyNetworkConnected", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean notifyNetworkConnected(android.net.networkstack.aidl.NetworkMonitorParameters params) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkMonitor.notifyNetworkConnectedParcel(params);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error in notifyNetworkConnected", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean notifyNetworkDisconnected() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkMonitor.notifyNetworkDisconnected();
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error in notifyNetworkDisconnected", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean notifyLinkPropertiesChanged(android.net.LinkProperties lp) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkMonitor.notifyLinkPropertiesChanged(lp);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error in notifyLinkPropertiesChanged", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean notifyNetworkCapabilitiesChanged(android.net.NetworkCapabilities nc) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mNetworkMonitor.notifyNetworkCapabilitiesChanged(nc);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error in notifyNetworkCapabilitiesChanged", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }
}
