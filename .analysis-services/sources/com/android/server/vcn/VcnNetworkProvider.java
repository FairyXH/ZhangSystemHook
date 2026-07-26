package com.android.server.vcn;

/* JADX INFO: loaded from: classes3.dex */
public class VcnNetworkProvider extends android.net.NetworkProvider {
    private static final java.lang.String TAG = com.android.server.vcn.VcnNetworkProvider.class.getSimpleName();
    private final android.content.Context mContext;
    private final com.android.server.vcn.VcnNetworkProvider.Dependencies mDeps;
    private final android.os.Handler mHandler;
    private final java.util.Set<com.android.server.vcn.VcnNetworkProvider.NetworkRequestListener> mListeners;
    private final java.util.Set<android.net.NetworkRequest> mRequests;

    interface NetworkRequestListener {
        void onNetworkRequested(android.net.NetworkRequest networkRequest);
    }

    public VcnNetworkProvider(android.content.Context context, android.os.Looper looper) {
        this(context, looper, new com.android.server.vcn.VcnNetworkProvider.Dependencies());
    }

    public VcnNetworkProvider(android.content.Context context, android.os.Looper looper, com.android.server.vcn.VcnNetworkProvider.Dependencies dependencies) {
        super((android.content.Context) java.util.Objects.requireNonNull(context, "Missing context"), (android.os.Looper) java.util.Objects.requireNonNull(looper, "Missing looper"), TAG);
        this.mListeners = new android.util.ArraySet();
        this.mRequests = new android.util.ArraySet();
        this.mContext = context;
        this.mHandler = new android.os.Handler(looper);
        this.mDeps = (com.android.server.vcn.VcnNetworkProvider.Dependencies) java.util.Objects.requireNonNull(dependencies, "Missing dependencies");
    }

    public void register() {
        ((android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class)).registerNetworkProvider(this);
        this.mDeps.registerNetworkOffer(this, com.android.server.vcn.Vcn.getNetworkScore(), buildCapabilityFilter(), new android.os.HandlerExecutor(this.mHandler), new android.net.NetworkProvider.NetworkOfferCallback() { // from class: com.android.server.vcn.VcnNetworkProvider.1
            public void onNetworkNeeded(android.net.NetworkRequest request) {
                com.android.server.vcn.VcnNetworkProvider.this.handleNetworkRequested(request);
            }

            public void onNetworkUnneeded(android.net.NetworkRequest request) {
                com.android.server.vcn.VcnNetworkProvider.this.handleNetworkRequestWithdrawn(request);
            }
        });
    }

    private android.net.NetworkCapabilities buildCapabilityFilter() {
        android.net.NetworkCapabilities.Builder builder = new android.net.NetworkCapabilities.Builder().addTransportType(0).addCapability(14).addCapability(13).addCapability(15).addCapability(28);
        java.util.Iterator it = android.net.vcn.VcnGatewayConnectionConfig.ALLOWED_CAPABILITIES.iterator();
        while (it.hasNext()) {
            int cap = ((java.lang.Integer) it.next()).intValue();
            builder.addCapability(cap);
        }
        return builder.build();
    }

    public void registerListener(com.android.server.vcn.VcnNetworkProvider.NetworkRequestListener listener) {
        this.mListeners.add(listener);
        resendAllRequests(listener);
    }

    public void unregisterListener(com.android.server.vcn.VcnNetworkProvider.NetworkRequestListener listener) {
        this.mListeners.remove(listener);
    }

    public void resendAllRequests(com.android.server.vcn.VcnNetworkProvider.NetworkRequestListener listener) {
        for (android.net.NetworkRequest request : this.mRequests) {
            notifyListenerForEvent(listener, request);
        }
    }

    private void notifyListenerForEvent(com.android.server.vcn.VcnNetworkProvider.NetworkRequestListener listener, android.net.NetworkRequest request) {
        listener.onNetworkRequested(request);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNetworkRequested(android.net.NetworkRequest request) {
        this.mRequests.add(request);
        for (com.android.server.vcn.VcnNetworkProvider.NetworkRequestListener listener : this.mListeners) {
            notifyListenerForEvent(listener, request);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNetworkRequestWithdrawn(android.net.NetworkRequest request) {
        this.mRequests.remove(request);
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("VcnNetworkProvider:");
        pw.increaseIndent();
        pw.println("mListeners:");
        pw.increaseIndent();
        for (com.android.server.vcn.VcnNetworkProvider.NetworkRequestListener listener : this.mListeners) {
            pw.println(listener);
        }
        pw.decreaseIndent();
        pw.println();
        pw.println("mRequests:");
        pw.increaseIndent();
        for (android.net.NetworkRequest request : this.mRequests) {
            pw.println(request);
        }
        pw.decreaseIndent();
        pw.println();
        pw.decreaseIndent();
    }

    public static class Dependencies {
        public void registerNetworkOffer(com.android.server.vcn.VcnNetworkProvider provider, android.net.NetworkScore score, android.net.NetworkCapabilities capabilitiesFilter, java.util.concurrent.Executor executor, android.net.NetworkProvider.NetworkOfferCallback callback) {
            provider.registerNetworkOffer(score, capabilitiesFilter, executor, callback);
        }
    }
}
