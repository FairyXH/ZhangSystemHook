package com.android.server.location.provider;

/* JADX INFO: loaded from: classes2.dex */
class DelegateLocationProvider extends com.android.server.location.provider.AbstractLocationProvider implements com.android.server.location.provider.AbstractLocationProvider.Listener {
    protected final com.android.server.location.provider.AbstractLocationProvider mDelegate;
    private final java.lang.Object mInitializationLock;
    private boolean mInitialized;

    DelegateLocationProvider(java.util.concurrent.Executor executor, com.android.server.location.provider.AbstractLocationProvider delegate) {
        super(executor, null, null, java.util.Collections.emptySet());
        this.mInitializationLock = new java.lang.Object();
        this.mInitialized = false;
        this.mDelegate = delegate;
    }

    protected void initializeDelegate() {
        synchronized (this.mInitializationLock) {
            com.android.internal.util.Preconditions.checkState(!this.mInitialized);
            setState(new java.util.function.UnaryOperator() { // from class: com.android.server.location.provider.DelegateLocationProvider$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$initializeDelegate$0((com.android.server.location.provider.AbstractLocationProvider.State) obj);
                }
            });
            this.mInitialized = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.location.provider.AbstractLocationProvider.State lambda$initializeDelegate$0(com.android.server.location.provider.AbstractLocationProvider.State previousState) {
        return this.mDelegate.getController().setListener(this);
    }

    protected final void waitForInitialization() {
        synchronized (this.mInitializationLock) {
            com.android.internal.util.Preconditions.checkState(this.mInitialized);
        }
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider.Listener
    public void onStateChanged(com.android.server.location.provider.AbstractLocationProvider.State oldState, final com.android.server.location.provider.AbstractLocationProvider.State newState) {
        waitForInitialization();
        setState(new java.util.function.UnaryOperator() { // from class: com.android.server.location.provider.DelegateLocationProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.location.provider.DelegateLocationProvider.lambda$onStateChanged$1(newState, (com.android.server.location.provider.AbstractLocationProvider.State) obj);
            }
        });
    }

    static /* synthetic */ com.android.server.location.provider.AbstractLocationProvider.State lambda$onStateChanged$1(com.android.server.location.provider.AbstractLocationProvider.State newState, com.android.server.location.provider.AbstractLocationProvider.State previousState) {
        return newState;
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider.Listener
    public void onReportLocation(android.location.LocationResult locationResult) {
        waitForInitialization();
        reportLocation(locationResult);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onStart() {
        com.android.internal.util.Preconditions.checkState(this.mInitialized);
        this.mDelegate.getController().start();
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onStop() {
        com.android.internal.util.Preconditions.checkState(this.mInitialized);
        this.mDelegate.getController().stop();
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onSetRequest(android.location.provider.ProviderRequest request) {
        com.android.internal.util.Preconditions.checkState(this.mInitialized);
        this.mDelegate.getController().setRequest(request);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onFlush(java.lang.Runnable callback) {
        com.android.internal.util.Preconditions.checkState(this.mInitialized);
        this.mDelegate.getController().flush(callback);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onExtraCommand(int uid, int pid, java.lang.String command, android.os.Bundle extras) {
        com.android.internal.util.Preconditions.checkState(this.mInitialized);
        this.mDelegate.getController().sendExtraCommand(uid, pid, command, extras);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        com.android.internal.util.Preconditions.checkState(this.mInitialized);
        this.mDelegate.dump(fd, pw, args);
    }
}
