package android.net;

/* JADX INFO: loaded from: classes.dex */
public class IpMemoryStore extends android.net.IpMemoryStoreClient {
    private static final java.lang.String TAG = android.net.IpMemoryStore.class.getSimpleName();
    private final java.util.concurrent.CompletableFuture<android.net.IIpMemoryStore> mService;
    private final java.util.concurrent.atomic.AtomicReference<java.util.concurrent.CompletableFuture<android.net.IIpMemoryStore>> mTailNode;

    public IpMemoryStore(android.content.Context context) {
        super(context);
        this.mService = new java.util.concurrent.CompletableFuture<>();
        this.mTailNode = new java.util.concurrent.atomic.AtomicReference<>(this.mService);
        getModuleNetworkStackClient(context).fetchIpMemoryStore(new android.net.IIpMemoryStoreCallbacks.Stub() { // from class: android.net.IpMemoryStore.1
            @Override // android.net.IIpMemoryStoreCallbacks
            public void onIpMemoryStoreFetched(android.net.IIpMemoryStore memoryStore) {
                android.net.IpMemoryStore.this.mService.complete(memoryStore);
            }

            @Override // android.net.IIpMemoryStoreCallbacks
            public int getInterfaceVersion() {
                return 10;
            }

            @Override // android.net.IIpMemoryStoreCallbacks
            public java.lang.String getInterfaceHash() {
                return "d5ea5eb3ddbdaa9a986ce6ba70b0804ca3e39b0c";
            }
        });
    }

    @Override // android.net.IpMemoryStoreClient
    protected void runWhenServiceReady(final java.util.function.Consumer<android.net.IIpMemoryStore> cb) throws java.util.concurrent.ExecutionException {
        this.mTailNode.getAndUpdate(new java.util.function.UnaryOperator() { // from class: android.net.IpMemoryStore$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((java.util.concurrent.CompletableFuture) obj).handle(new java.util.function.BiFunction() { // from class: android.net.IpMemoryStore$$ExternalSyntheticLambda1
                    @Override // java.util.function.BiFunction
                    public final java.lang.Object apply(java.lang.Object obj2, java.lang.Object obj3) {
                        return android.net.IpMemoryStore.lambda$runWhenServiceReady$0(consumer, (android.net.IIpMemoryStore) obj2, (java.lang.Throwable) obj3);
                    }
                });
            }
        });
    }

    static /* synthetic */ android.net.IIpMemoryStore lambda$runWhenServiceReady$0(java.util.function.Consumer cb, android.net.IIpMemoryStore store, java.lang.Throwable exception) {
        if (exception != null) {
            android.util.Log.wtf(TAG, "Error fetching IpMemoryStore", exception);
            return store;
        }
        try {
            cb.accept(store);
        } catch (java.lang.Exception e) {
            android.util.Log.wtf(TAG, "Exception occurred: " + e.getMessage());
        }
        return store;
    }

    protected android.net.networkstack.ModuleNetworkStackClient getModuleNetworkStackClient(android.content.Context context) {
        return android.net.networkstack.ModuleNetworkStackClient.getInstance(context);
    }

    public static android.net.IpMemoryStore getMemoryStore(android.content.Context context) {
        return new android.net.IpMemoryStore(context);
    }
}
