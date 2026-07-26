package android.net;

/* JADX INFO: loaded from: classes.dex */
class NetworkFactoryLegacyImpl extends android.os.Handler implements android.net.NetworkFactoryShim {
    public static final int CMD_CANCEL_REQUEST = 2;
    public static final int CMD_REQUEST_NETWORK = 1;
    private static final int CMD_SET_FILTER = 4;
    private static final int CMD_SET_SCORE = 3;
    private static final boolean DBG = true;
    private static final boolean VDBG = false;
    android.net.NetworkCapabilities mCapabilityFilter;
    final android.content.Context mContext;
    private final java.util.Map<android.net.NetworkRequest, android.net.NetworkFactoryLegacyImpl.NetworkRequestInfo> mNetworkRequests;
    final android.net.NetworkFactory mParent;
    android.net.NetworkProvider mProvider;
    private int mScore;

    NetworkFactoryLegacyImpl(android.net.NetworkFactory parent, android.os.Looper looper, android.content.Context context, android.net.NetworkCapabilities filter) {
        super(looper);
        this.mNetworkRequests = new java.util.LinkedHashMap();
        this.mProvider = null;
        this.mParent = parent;
        this.mContext = context;
        this.mCapabilityFilter = filter;
    }

    public void register(java.lang.String logTag) {
        if (this.mProvider != null) {
            throw new java.lang.IllegalStateException("A NetworkFactory must only be registered once");
        }
        this.mParent.log("Registering NetworkFactory");
        this.mProvider = new android.net.NetworkProvider(this.mContext, getLooper(), logTag) { // from class: android.net.NetworkFactoryLegacyImpl.1
            public void onNetworkRequested(android.net.NetworkRequest request, int score, int servingProviderId) {
                android.net.NetworkFactoryLegacyImpl.this.handleAddRequest(request, score, servingProviderId);
            }

            public void onNetworkRequestWithdrawn(android.net.NetworkRequest request) {
                android.net.NetworkFactoryLegacyImpl.this.handleRemoveRequest(request);
            }
        };
        ((android.net.ConnectivityManager) this.mContext.getSystemService("connectivity")).registerNetworkProvider(this.mProvider);
    }

    @Override // android.net.NetworkFactoryShim
    public void terminate() {
        if (this.mProvider == null) {
            throw new java.lang.IllegalStateException("This NetworkFactory was never registered");
        }
        this.mParent.log("Unregistering NetworkFactory");
        ((android.net.ConnectivityManager) this.mContext.getSystemService("connectivity")).unregisterNetworkProvider(this.mProvider);
        removeCallbacksAndMessages(null);
    }

    @Override // android.os.Handler
    public void handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case 1:
                handleAddRequest((android.net.NetworkRequest) msg.obj, msg.arg1, msg.arg2);
                break;
            case 2:
                handleRemoveRequest((android.net.NetworkRequest) msg.obj);
                break;
            case 3:
                handleSetScore(msg.arg1);
                break;
            case 4:
                handleSetFilter((android.net.NetworkCapabilities) msg.obj);
                break;
        }
    }

    private static class NetworkRequestInfo {
        public int providerId;
        public final android.net.NetworkRequest request;
        public boolean requested = false;
        public int score;

        NetworkRequestInfo(android.net.NetworkRequest request, int score, int providerId) {
            this.request = request;
            this.score = score;
            this.providerId = providerId;
        }

        public java.lang.String toString() {
            return "{" + this.request + ", score=" + this.score + ", requested=" + this.requested + "}";
        }
    }

    protected void handleAddRequest(android.net.NetworkRequest request, int score, int servingProviderId) {
        android.net.NetworkFactoryLegacyImpl.NetworkRequestInfo n = this.mNetworkRequests.get(request);
        if (n == null) {
            this.mParent.log("got request " + request + " with score " + score + " and providerId " + servingProviderId);
            n = new android.net.NetworkFactoryLegacyImpl.NetworkRequestInfo(request, score, servingProviderId);
            this.mNetworkRequests.put(n.request, n);
        } else {
            n.score = score;
            n.providerId = servingProviderId;
        }
        evalRequest(n);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRemoveRequest(android.net.NetworkRequest request) {
        android.net.NetworkFactoryLegacyImpl.NetworkRequestInfo n = this.mNetworkRequests.get(request);
        if (n != null) {
            this.mNetworkRequests.remove(request);
            if (n.requested) {
                this.mParent.releaseNetworkFor(n.request);
            }
        }
    }

    private void handleSetScore(int score) {
        this.mScore = score;
        evalRequests();
    }

    private void handleSetFilter(android.net.NetworkCapabilities netCap) {
        this.mCapabilityFilter = netCap;
        evalRequests();
    }

    public boolean acceptRequest(android.net.NetworkRequest request) {
        return this.mParent.acceptRequest(request);
    }

    private void evalRequest(android.net.NetworkFactoryLegacyImpl.NetworkRequestInfo n) {
        if (shouldNeedNetworkFor(n)) {
            this.mParent.needNetworkFor(n.request);
            n.requested = true;
        } else if (shouldReleaseNetworkFor(n)) {
            this.mParent.releaseNetworkFor(n.request);
            n.requested = false;
        }
    }

    private boolean shouldNeedNetworkFor(android.net.NetworkFactoryLegacyImpl.NetworkRequestInfo n) {
        return !n.requested && (n.score < this.mScore || n.providerId == this.mProvider.getProviderId()) && n.request.canBeSatisfiedBy(this.mCapabilityFilter) && acceptRequest(n.request);
    }

    private boolean shouldReleaseNetworkFor(android.net.NetworkFactoryLegacyImpl.NetworkRequestInfo n) {
        return n.requested && !((n.score <= this.mScore || n.providerId == this.mProvider.getProviderId()) && n.request.canBeSatisfiedBy(this.mCapabilityFilter) && acceptRequest(n.request));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evalRequests() {
        for (android.net.NetworkFactoryLegacyImpl.NetworkRequestInfo n : this.mNetworkRequests.values()) {
            evalRequest(n);
        }
    }

    public void reevaluateAllRequests() {
        post(new java.lang.Runnable() { // from class: android.net.NetworkFactoryLegacyImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.evalRequests();
            }
        });
    }

    @Override // android.net.NetworkFactoryShim
    public void releaseRequestAsUnfulfillableByAnyFactory(final android.net.NetworkRequest r) {
        post(new java.lang.Runnable() { // from class: android.net.NetworkFactoryLegacyImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$releaseRequestAsUnfulfillableByAnyFactory$0(r);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$releaseRequestAsUnfulfillableByAnyFactory$0(android.net.NetworkRequest r) {
        this.mParent.log("releaseRequestAsUnfulfillableByAnyFactory: " + r);
        android.net.NetworkProvider provider = this.mProvider;
        if (provider == null) {
            this.mParent.log("Ignoring attempt to release unregistered request as unfulfillable");
        } else {
            provider.declareNetworkRequestUnfulfillable(r);
        }
    }

    public void setScoreFilter(int score) {
        sendMessage(obtainMessage(3, score, 0));
    }

    public void setScoreFilter(android.net.NetworkScore score) {
        setScoreFilter(score.getLegacyInt());
    }

    public void setCapabilityFilter(android.net.NetworkCapabilities netCap) {
        sendMessage(obtainMessage(4, new android.net.NetworkCapabilities(netCap)));
    }

    public int getRequestCount() {
        return this.mNetworkRequests.size();
    }

    @Override // android.net.NetworkFactoryShim
    public int getSerialNumber() {
        return this.mProvider.getProviderId();
    }

    @Override // android.net.NetworkFactoryShim
    public android.net.NetworkProvider getProvider() {
        return this.mProvider;
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        writer.println(toString());
        for (android.net.NetworkFactoryLegacyImpl.NetworkRequestInfo n : this.mNetworkRequests.values()) {
            writer.println("  " + n);
        }
    }

    @Override // android.os.Handler
    public java.lang.String toString() {
        return "providerId=" + (this.mProvider != null ? java.lang.Integer.valueOf(this.mProvider.getProviderId()) : "null") + ", ScoreFilter=" + this.mScore + ", Filter=" + this.mCapabilityFilter + ", requests=" + this.mNetworkRequests.size();
    }
}
