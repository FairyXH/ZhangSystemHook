package android.net;

/* JADX INFO: loaded from: classes.dex */
class NetworkFactoryImpl extends android.net.NetworkFactoryLegacyImpl {
    private static final int CMD_CANCEL_REQUEST = 2;
    private static final int CMD_LISTEN_TO_ALL_REQUESTS = 6;
    private static final int CMD_OFFER_NETWORK = 5;
    private static final int CMD_REQUEST_NETWORK = 1;
    private static final int CMD_SET_FILTER = 4;
    private static final int CMD_SET_SCORE = 3;
    private static final boolean DBG = true;
    private static final android.net.NetworkScore INVINCIBLE_SCORE = new android.net.NetworkScore.Builder().setLegacyInt(1000).build();
    private static final boolean VDBG = false;
    private final java.util.concurrent.Executor mExecutor;
    private final java.util.Map<android.net.NetworkRequest, android.net.NetworkFactoryImpl.NetworkRequestInfo> mNetworkRequests;
    private final android.net.NetworkProvider.NetworkOfferCallback mRequestCallback;
    private android.net.NetworkScore mScore;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(java.lang.Runnable command) {
        post(command);
    }

    NetworkFactoryImpl(android.net.NetworkFactory parent, android.os.Looper looper, android.content.Context context, android.net.NetworkCapabilities filter) {
        super(parent, looper, context, filter != null ? filter : android.net.NetworkCapabilities.Builder.withoutDefaultCapabilities().build());
        this.mNetworkRequests = new java.util.LinkedHashMap();
        this.mScore = new android.net.NetworkScore.Builder().setLegacyInt(0).build();
        this.mRequestCallback = new android.net.NetworkProvider.NetworkOfferCallback() { // from class: android.net.NetworkFactoryImpl.1
            public void onNetworkNeeded(android.net.NetworkRequest request) {
                android.net.NetworkFactoryImpl.this.handleAddRequest(request);
            }

            public void onNetworkUnneeded(android.net.NetworkRequest request) {
                android.net.NetworkFactoryImpl.this.handleRemoveRequest(request);
            }
        };
        this.mExecutor = new java.util.concurrent.Executor() { // from class: android.net.NetworkFactoryImpl$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Executor
            public final void execute(java.lang.Runnable runnable) {
                this.f$0.lambda$new$0(runnable);
            }
        };
    }

    @Override // android.net.NetworkFactoryLegacyImpl, android.net.NetworkFactoryShim
    public void register(java.lang.String logTag) {
        register(logTag, false);
    }

    @Override // android.net.NetworkFactoryShim
    public void registerIgnoringScore(java.lang.String logTag) {
        register(logTag, true);
    }

    private void register(java.lang.String logTag, boolean listenToAllRequests) {
        if (this.mProvider != null) {
            throw new java.lang.IllegalStateException("A NetworkFactory must only be registered once");
        }
        this.mParent.log("Registering NetworkFactory");
        this.mProvider = new android.net.NetworkProvider(this.mContext, getLooper(), logTag) { // from class: android.net.NetworkFactoryImpl.2
            public void onNetworkRequested(android.net.NetworkRequest request, int score, int servingProviderId) {
                android.net.NetworkFactoryImpl.this.handleAddRequest(request);
            }

            public void onNetworkRequestWithdrawn(android.net.NetworkRequest request) {
                android.net.NetworkFactoryImpl.this.handleRemoveRequest(request);
            }
        };
        ((android.net.ConnectivityManager) this.mContext.getSystemService("connectivity")).registerNetworkProvider(this.mProvider);
        if (listenToAllRequests) {
            sendMessage(obtainMessage(6));
        } else {
            sendMessage(obtainMessage(5));
        }
    }

    private void handleOfferNetwork(android.net.NetworkScore score) {
        this.mProvider.registerNetworkOffer(score, this.mCapabilityFilter, this.mExecutor, this.mRequestCallback);
    }

    @Override // android.net.NetworkFactoryLegacyImpl, android.os.Handler
    public void handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case 1:
                handleAddRequest((android.net.NetworkRequest) msg.obj);
                break;
            case 2:
                handleRemoveRequest((android.net.NetworkRequest) msg.obj);
                break;
            case 3:
                handleSetScore((android.net.NetworkScore) msg.obj);
                break;
            case 4:
                handleSetFilter((android.net.NetworkCapabilities) msg.obj);
                break;
            case 5:
                handleOfferNetwork(this.mScore);
                break;
            case 6:
                handleOfferNetwork(INVINCIBLE_SCORE);
                break;
        }
    }

    private static class NetworkRequestInfo {
        public final android.net.NetworkRequest request;
        public boolean requested = false;

        NetworkRequestInfo(android.net.NetworkRequest request) {
            this.request = request;
        }

        public java.lang.String toString() {
            return "{" + this.request + ", requested=" + this.requested + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAddRequest(android.net.NetworkRequest request) {
        android.net.NetworkFactoryImpl.NetworkRequestInfo n = this.mNetworkRequests.get(request);
        if (n == null) {
            this.mParent.log("got request " + request);
            n = new android.net.NetworkFactoryImpl.NetworkRequestInfo(request);
            this.mNetworkRequests.put(n.request, n);
        }
        if (this.mParent.acceptRequest(request)) {
            n.requested = true;
            this.mParent.needNetworkFor(request);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRemoveRequest(android.net.NetworkRequest request) {
        android.net.NetworkFactoryImpl.NetworkRequestInfo n = this.mNetworkRequests.get(request);
        if (n != null) {
            this.mNetworkRequests.remove(request);
            if (n.requested) {
                this.mParent.releaseNetworkFor(n.request);
            }
        }
    }

    private void handleSetScore(android.net.NetworkScore score) {
        if (this.mScore.equals(score)) {
            return;
        }
        this.mScore = score;
        this.mParent.reevaluateAllRequests();
    }

    private void handleSetFilter(android.net.NetworkCapabilities netCap) {
        if (netCap.equals(this.mCapabilityFilter)) {
            return;
        }
        this.mCapabilityFilter = netCap;
        this.mParent.reevaluateAllRequests();
    }

    @Override // android.net.NetworkFactoryLegacyImpl, android.net.NetworkFactoryShim
    public final void reevaluateAllRequests() {
        if (this.mProvider == null) {
            return;
        }
        this.mProvider.registerNetworkOffer(this.mScore, this.mCapabilityFilter, this.mExecutor, this.mRequestCallback);
    }

    @Override // android.net.NetworkFactoryLegacyImpl, android.net.NetworkFactoryShim
    @java.lang.Deprecated
    public void setScoreFilter(int score) {
        setScoreFilter(new android.net.NetworkScore.Builder().setLegacyInt(score).build());
    }

    @Override // android.net.NetworkFactoryLegacyImpl, android.net.NetworkFactoryShim
    public void setScoreFilter(android.net.NetworkScore score) {
        sendMessage(obtainMessage(3, score));
    }

    @Override // android.net.NetworkFactoryLegacyImpl, android.net.NetworkFactoryShim
    public void setCapabilityFilter(android.net.NetworkCapabilities netCap) {
        sendMessage(obtainMessage(4, new android.net.NetworkCapabilities(netCap)));
    }

    @Override // android.net.NetworkFactoryLegacyImpl, android.net.NetworkFactoryShim
    public int getRequestCount() {
        return this.mNetworkRequests.size();
    }

    @Override // android.net.NetworkFactoryLegacyImpl, android.net.NetworkFactoryShim
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        writer.println(toString());
        for (android.net.NetworkFactoryImpl.NetworkRequestInfo n : this.mNetworkRequests.values()) {
            writer.println("  " + n);
        }
    }

    @Override // android.net.NetworkFactoryLegacyImpl, android.os.Handler
    public java.lang.String toString() {
        return "providerId=" + (this.mProvider != null ? java.lang.Integer.valueOf(this.mProvider.getProviderId()) : "null") + ", ScoreFilter=" + this.mScore + ", Filter=" + this.mCapabilityFilter + ", requests=" + this.mNetworkRequests.size();
    }
}
