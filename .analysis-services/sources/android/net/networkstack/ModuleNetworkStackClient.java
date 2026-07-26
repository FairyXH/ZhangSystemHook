package android.net.networkstack;

/* JADX INFO: loaded from: classes.dex */
public class ModuleNetworkStackClient extends android.net.networkstack.NetworkStackClientBase {
    private static final java.lang.String TAG = android.net.networkstack.ModuleNetworkStackClient.class.getSimpleName();
    private static android.net.networkstack.ModuleNetworkStackClient sInstance;

    private ModuleNetworkStackClient() {
    }

    public static synchronized android.net.networkstack.ModuleNetworkStackClient getInstance(android.content.Context packageContext) {
        if (sInstance == null) {
            sInstance = new android.net.networkstack.ModuleNetworkStackClient();
            sInstance.startPolling();
        }
        return sInstance;
    }

    protected static synchronized void resetInstanceForTest() {
        sInstance = null;
    }

    private void startPolling() {
        android.os.IBinder nss = android.net.NetworkStack.getService();
        if (nss != null) {
            onNetworkStackConnected(android.net.INetworkStackConnector.Stub.asInterface(nss));
        } else {
            new java.lang.Thread(new android.net.networkstack.ModuleNetworkStackClient.PollingRunner()).start();
        }
    }

    private class PollingRunner implements java.lang.Runnable {
        private PollingRunner() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                android.os.IBinder nss = android.net.NetworkStack.getService();
                if (nss == null) {
                    try {
                        java.lang.Thread.sleep(200L);
                    } catch (java.lang.InterruptedException e) {
                        android.util.Log.e(android.net.networkstack.ModuleNetworkStackClient.TAG, "Interrupted while waiting for NetworkStack connector", e);
                    }
                } else {
                    android.net.networkstack.ModuleNetworkStackClient.this.onNetworkStackConnected(android.net.INetworkStackConnector.Stub.asInterface(nss));
                    return;
                }
            }
        }
    }
}
