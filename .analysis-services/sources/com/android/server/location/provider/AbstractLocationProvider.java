package com.android.server.location.provider;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AbstractLocationProvider {
    private com.android.server.location.provider.IAbstractLocationProviderWrapper mAbstractLocationProviderWrapper = new com.android.server.location.provider.AbstractLocationProvider.AbstractLocationProviderWrapper();
    private final com.android.server.location.provider.LocationProviderController mController;
    protected final java.util.concurrent.Executor mExecutor;
    private final java.util.concurrent.atomic.AtomicReference<com.android.server.location.provider.AbstractLocationProvider.InternalState> mInternalState;

    public interface Listener {
        void onReportLocation(android.location.LocationResult locationResult);

        void onStateChanged(com.android.server.location.provider.AbstractLocationProvider.State state, com.android.server.location.provider.AbstractLocationProvider.State state2);
    }

    protected abstract void dump(java.io.FileDescriptor fileDescriptor, java.io.PrintWriter printWriter, java.lang.String[] strArr);

    protected abstract void onExtraCommand(int i, int i2, java.lang.String str, android.os.Bundle bundle);

    protected abstract void onFlush(java.lang.Runnable runnable);

    protected abstract void onSetRequest(android.location.provider.ProviderRequest providerRequest);

    public static final class State {
        public static final com.android.server.location.provider.AbstractLocationProvider.State EMPTY_STATE = new com.android.server.location.provider.AbstractLocationProvider.State(false, null, null, java.util.Collections.emptySet());
        public final boolean allowed;
        public final java.util.Set<java.lang.String> extraAttributionTags;
        public final android.location.util.identity.CallerIdentity identity;
        public final android.location.provider.ProviderProperties properties;

        private State(boolean allowed, android.location.provider.ProviderProperties properties, android.location.util.identity.CallerIdentity identity, java.util.Set<java.lang.String> extraAttributionTags) {
            this.allowed = allowed;
            this.properties = properties;
            this.identity = identity;
            this.extraAttributionTags = (java.util.Set) java.util.Objects.requireNonNull(extraAttributionTags);
        }

        public com.android.server.location.provider.AbstractLocationProvider.State withAllowed(boolean allowed) {
            if (allowed == this.allowed) {
                return this;
            }
            return new com.android.server.location.provider.AbstractLocationProvider.State(allowed, this.properties, this.identity, this.extraAttributionTags);
        }

        public com.android.server.location.provider.AbstractLocationProvider.State withProperties(android.location.provider.ProviderProperties properties) {
            if (java.util.Objects.equals(properties, this.properties)) {
                return this;
            }
            return new com.android.server.location.provider.AbstractLocationProvider.State(this.allowed, properties, this.identity, this.extraAttributionTags);
        }

        public com.android.server.location.provider.AbstractLocationProvider.State withIdentity(android.location.util.identity.CallerIdentity identity) {
            if (java.util.Objects.equals(identity, this.identity)) {
                return this;
            }
            return new com.android.server.location.provider.AbstractLocationProvider.State(this.allowed, this.properties, identity, this.extraAttributionTags);
        }

        public com.android.server.location.provider.AbstractLocationProvider.State withExtraAttributionTags(java.util.Set<java.lang.String> extraAttributionTags) {
            if (extraAttributionTags.equals(this.extraAttributionTags)) {
                return this;
            }
            return new com.android.server.location.provider.AbstractLocationProvider.State(this.allowed, this.properties, this.identity, extraAttributionTags);
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.location.provider.AbstractLocationProvider.State)) {
                return false;
            }
            com.android.server.location.provider.AbstractLocationProvider.State state = (com.android.server.location.provider.AbstractLocationProvider.State) o;
            return this.allowed == state.allowed && this.properties == state.properties && java.util.Objects.equals(this.identity, state.identity) && this.extraAttributionTags.equals(state.extraAttributionTags);
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Boolean.valueOf(this.allowed), this.properties, this.identity, this.extraAttributionTags);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class InternalState {
        public final com.android.server.location.provider.AbstractLocationProvider.Listener listener;
        public final com.android.server.location.provider.AbstractLocationProvider.State state;

        InternalState(com.android.server.location.provider.AbstractLocationProvider.Listener listener, com.android.server.location.provider.AbstractLocationProvider.State state) {
            this.listener = listener;
            this.state = state;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public com.android.server.location.provider.AbstractLocationProvider.InternalState withListener(com.android.server.location.provider.AbstractLocationProvider.Listener listener) {
            if (listener == this.listener) {
                return this;
            }
            return new com.android.server.location.provider.AbstractLocationProvider.InternalState(listener, this.state);
        }

        com.android.server.location.provider.AbstractLocationProvider.InternalState withState(com.android.server.location.provider.AbstractLocationProvider.State state) {
            if (state.equals(this.state)) {
                return this;
            }
            return new com.android.server.location.provider.AbstractLocationProvider.InternalState(this.listener, state);
        }

        com.android.server.location.provider.AbstractLocationProvider.InternalState withState(java.util.function.UnaryOperator<com.android.server.location.provider.AbstractLocationProvider.State> operator) {
            return withState((com.android.server.location.provider.AbstractLocationProvider.State) operator.apply(this.state));
        }
    }

    protected AbstractLocationProvider(java.util.concurrent.Executor executor, android.location.util.identity.CallerIdentity identity, android.location.provider.ProviderProperties properties, java.util.Set<java.lang.String> extraAttributionTags) {
        com.android.internal.util.Preconditions.checkArgument(identity == null || identity.getListenerId() == null);
        this.mExecutor = (java.util.concurrent.Executor) java.util.Objects.requireNonNull(executor);
        this.mInternalState = new java.util.concurrent.atomic.AtomicReference<>(new com.android.server.location.provider.AbstractLocationProvider.InternalState(null, com.android.server.location.provider.AbstractLocationProvider.State.EMPTY_STATE.withIdentity(identity).withProperties(properties).withExtraAttributionTags(extraAttributionTags)));
        this.mController = new com.android.server.location.provider.AbstractLocationProvider.Controller();
    }

    com.android.server.location.provider.LocationProviderController getController() {
        return this.mController;
    }

    protected void setState(final java.util.function.UnaryOperator<com.android.server.location.provider.AbstractLocationProvider.State> operator) {
        final java.util.concurrent.atomic.AtomicReference<com.android.server.location.provider.AbstractLocationProvider.State> oldStateRef = new java.util.concurrent.atomic.AtomicReference<>();
        com.android.server.location.provider.AbstractLocationProvider.InternalState newInternalState = this.mInternalState.updateAndGet(new java.util.function.UnaryOperator() { // from class: com.android.server.location.provider.AbstractLocationProvider$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.location.provider.AbstractLocationProvider.lambda$setState$0(oldStateRef, operator, (com.android.server.location.provider.AbstractLocationProvider.InternalState) obj);
            }
        });
        com.android.server.location.provider.AbstractLocationProvider.State oldState = oldStateRef.get();
        if (!oldState.equals(newInternalState.state) && newInternalState.listener != null) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                newInternalState.listener.onStateChanged(oldState, newInternalState.state);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    static /* synthetic */ com.android.server.location.provider.AbstractLocationProvider.InternalState lambda$setState$0(java.util.concurrent.atomic.AtomicReference oldStateRef, java.util.function.UnaryOperator operator, com.android.server.location.provider.AbstractLocationProvider.InternalState internalState) {
        oldStateRef.set(internalState.state);
        return internalState.withState((java.util.function.UnaryOperator<com.android.server.location.provider.AbstractLocationProvider.State>) operator);
    }

    public final com.android.server.location.provider.AbstractLocationProvider.State getState() {
        return this.mInternalState.get().state;
    }

    protected void setAllowed(final boolean allowed) {
        setState(new java.util.function.UnaryOperator() { // from class: com.android.server.location.provider.AbstractLocationProvider$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.location.provider.AbstractLocationProvider.State) obj).withAllowed(allowed);
            }
        });
    }

    protected void setProperties(final android.location.provider.ProviderProperties properties) {
        setState(new java.util.function.UnaryOperator() { // from class: com.android.server.location.provider.AbstractLocationProvider$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.location.provider.AbstractLocationProvider.State) obj).withProperties(properties);
            }
        });
    }

    protected void setIdentity(final android.location.util.identity.CallerIdentity identity) {
        com.android.internal.util.Preconditions.checkArgument(identity == null || identity.getListenerId() == null);
        setState(new java.util.function.UnaryOperator() { // from class: com.android.server.location.provider.AbstractLocationProvider$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.location.provider.AbstractLocationProvider.State) obj).withIdentity(identity);
            }
        });
    }

    public final java.util.Set<java.lang.String> getExtraAttributionTags() {
        return this.mInternalState.get().state.extraAttributionTags;
    }

    protected void setExtraAttributionTags(final java.util.Set<java.lang.String> extraAttributionTags) {
        setState(new java.util.function.UnaryOperator() { // from class: com.android.server.location.provider.AbstractLocationProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.location.provider.AbstractLocationProvider.State) obj).withExtraAttributionTags(extraAttributionTags);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void reportLocation(android.location.LocationResult locationResult) {
        com.android.server.location.provider.AbstractLocationProvider.Listener listener = this.mInternalState.get().listener;
        if (listener != null) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                listener.onReportLocation((android.location.LocationResult) java.util.Objects.requireNonNull(locationResult));
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    protected void onStart() {
    }

    protected void onStop() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    class Controller implements com.android.server.location.provider.LocationProviderController {
        private boolean mStarted = false;

        Controller() {
        }

        @Override // com.android.server.location.provider.LocationProviderController
        public com.android.server.location.provider.AbstractLocationProvider.State setListener(final com.android.server.location.provider.AbstractLocationProvider.Listener listener) {
            com.android.server.location.provider.AbstractLocationProvider.InternalState oldInternalState = (com.android.server.location.provider.AbstractLocationProvider.InternalState) com.android.server.location.provider.AbstractLocationProvider.this.mInternalState.getAndUpdate(new java.util.function.UnaryOperator() { // from class: com.android.server.location.provider.AbstractLocationProvider$Controller$$ExternalSyntheticLambda5
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((com.android.server.location.provider.AbstractLocationProvider.InternalState) obj).withListener(listener);
                }
            });
            com.android.internal.util.Preconditions.checkState(listener == null || oldInternalState.listener == null);
            return oldInternalState.state;
        }

        @Override // com.android.server.location.provider.LocationProviderController
        public boolean isStarted() {
            return this.mStarted;
        }

        @Override // com.android.server.location.provider.LocationProviderController
        public void start() {
            com.android.internal.util.Preconditions.checkState(!this.mStarted);
            this.mStarted = true;
            java.util.concurrent.Executor executor = com.android.server.location.provider.AbstractLocationProvider.this.mExecutor;
            final com.android.server.location.provider.AbstractLocationProvider abstractLocationProvider = com.android.server.location.provider.AbstractLocationProvider.this;
            executor.execute(new java.lang.Runnable() { // from class: com.android.server.location.provider.AbstractLocationProvider$Controller$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    abstractLocationProvider.onStart();
                }
            });
        }

        @Override // com.android.server.location.provider.LocationProviderController
        public void stop() {
            com.android.internal.util.Preconditions.checkState(this.mStarted);
            this.mStarted = false;
            java.util.concurrent.Executor executor = com.android.server.location.provider.AbstractLocationProvider.this.mExecutor;
            final com.android.server.location.provider.AbstractLocationProvider abstractLocationProvider = com.android.server.location.provider.AbstractLocationProvider.this;
            executor.execute(new java.lang.Runnable() { // from class: com.android.server.location.provider.AbstractLocationProvider$Controller$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    abstractLocationProvider.onStop();
                }
            });
        }

        @Override // com.android.server.location.provider.LocationProviderController
        public void setRequest(final android.location.provider.ProviderRequest request) {
            com.android.internal.util.Preconditions.checkState(this.mStarted);
            com.android.server.location.provider.AbstractLocationProvider.this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.location.provider.AbstractLocationProvider$Controller$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setRequest$1(request);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setRequest$1(android.location.provider.ProviderRequest request) {
            com.android.server.location.provider.AbstractLocationProvider.this.onSetRequest(request);
        }

        @Override // com.android.server.location.provider.LocationProviderController
        public void flush(final java.lang.Runnable listener) {
            com.android.internal.util.Preconditions.checkState(this.mStarted);
            com.android.server.location.provider.AbstractLocationProvider.this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.location.provider.AbstractLocationProvider$Controller$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$flush$2(listener);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$flush$2(java.lang.Runnable listener) {
            com.android.server.location.provider.AbstractLocationProvider.this.onFlush(listener);
        }

        @Override // com.android.server.location.provider.LocationProviderController
        public void sendExtraCommand(final int uid, final int pid, final java.lang.String command, final android.os.Bundle extras) {
            com.android.internal.util.Preconditions.checkState(this.mStarted);
            com.android.server.location.provider.AbstractLocationProvider.this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.location.provider.AbstractLocationProvider$Controller$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$sendExtraCommand$3(uid, pid, command, extras);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$sendExtraCommand$3(int uid, int pid, java.lang.String command, android.os.Bundle extras) {
            com.android.server.location.provider.AbstractLocationProvider.this.onExtraCommand(uid, pid, command, extras);
        }
    }

    public com.android.server.location.provider.IAbstractLocationProviderWrapper getWrapper() {
        return this.mAbstractLocationProviderWrapper;
    }

    private class AbstractLocationProviderWrapper implements com.android.server.location.provider.IAbstractLocationProviderWrapper {
        private AbstractLocationProviderWrapper() {
        }

        @Override // com.android.server.location.provider.IAbstractLocationProviderWrapper
        public com.android.server.location.provider.AbstractLocationProvider.State setListener(com.android.server.location.provider.AbstractLocationProvider.Listener listener) {
            return com.android.server.location.provider.AbstractLocationProvider.this.mController.setListener(listener);
        }

        @Override // com.android.server.location.provider.IAbstractLocationProviderWrapper
        public boolean isStarted() {
            return com.android.server.location.provider.AbstractLocationProvider.this.mController.isStarted();
        }

        @Override // com.android.server.location.provider.IAbstractLocationProviderWrapper
        public void start() {
            com.android.server.location.provider.AbstractLocationProvider.this.mController.start();
        }

        @Override // com.android.server.location.provider.IAbstractLocationProviderWrapper
        public void stop() {
            com.android.server.location.provider.AbstractLocationProvider.this.mController.stop();
        }

        @Override // com.android.server.location.provider.IAbstractLocationProviderWrapper
        public void setRequest(android.location.provider.ProviderRequest request) {
            com.android.server.location.provider.AbstractLocationProvider.this.mController.setRequest(request);
        }

        @Override // com.android.server.location.provider.IAbstractLocationProviderWrapper
        public void flush(java.lang.Runnable listener) {
            com.android.server.location.provider.AbstractLocationProvider.this.mController.flush(listener);
        }
    }
}
