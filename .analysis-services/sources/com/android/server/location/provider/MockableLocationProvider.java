package com.android.server.location.provider;

/* JADX INFO: loaded from: classes2.dex */
public class MockableLocationProvider extends com.android.server.location.provider.AbstractLocationProvider {
    private com.android.server.location.provider.MockLocationProvider mMockProvider;
    final java.lang.Object mOwnerLock;
    private com.android.server.location.provider.AbstractLocationProvider mProvider;
    private com.android.server.location.provider.AbstractLocationProvider mRealProvider;
    private android.location.provider.ProviderRequest mRequest;
    private boolean mStarted;

    public MockableLocationProvider(java.lang.Object ownerLock) {
        super(com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, null, null, java.util.Collections.emptySet());
        this.mOwnerLock = ownerLock;
        this.mRequest = android.location.provider.ProviderRequest.EMPTY_REQUEST;
    }

    public com.android.server.location.provider.AbstractLocationProvider getProvider() {
        com.android.server.location.provider.AbstractLocationProvider abstractLocationProvider;
        synchronized (this.mOwnerLock) {
            abstractLocationProvider = this.mProvider;
        }
        return abstractLocationProvider;
    }

    public void setRealProvider(com.android.server.location.provider.AbstractLocationProvider provider) {
        synchronized (this.mOwnerLock) {
            if (this.mRealProvider == provider) {
                return;
            }
            this.mRealProvider = provider;
            if (!isMock()) {
                setProviderLocked(this.mRealProvider);
            }
        }
    }

    public void setMockProvider(com.android.server.location.provider.MockLocationProvider provider) {
        synchronized (this.mOwnerLock) {
            if (this.mMockProvider == provider) {
                return;
            }
            this.mMockProvider = provider;
            if (this.mMockProvider != null) {
                setProviderLocked(this.mMockProvider);
            } else {
                setProviderLocked(this.mRealProvider);
            }
        }
    }

    private void setProviderLocked(com.android.server.location.provider.AbstractLocationProvider provider) {
        final com.android.server.location.provider.AbstractLocationProvider.State newState;
        if (this.mProvider == provider) {
            return;
        }
        com.android.server.location.provider.AbstractLocationProvider oldProvider = this.mProvider;
        this.mProvider = provider;
        if (oldProvider != null) {
            oldProvider.getController().setListener(null);
            if (oldProvider.getController().isStarted()) {
                oldProvider.getController().setRequest(android.location.provider.ProviderRequest.EMPTY_REQUEST);
                oldProvider.getController().stop();
            }
        }
        if (this.mProvider != null) {
            newState = this.mProvider.getController().setListener(new com.android.server.location.provider.MockableLocationProvider.ListenerWrapper(this.mProvider));
            if (this.mStarted) {
                if (!this.mProvider.getController().isStarted()) {
                    this.mProvider.getController().start();
                }
                this.mProvider.getController().setRequest(this.mRequest);
            } else if (this.mProvider.getController().isStarted()) {
                this.mProvider.getController().setRequest(android.location.provider.ProviderRequest.EMPTY_REQUEST);
                this.mProvider.getController().stop();
            }
        } else {
            newState = com.android.server.location.provider.AbstractLocationProvider.State.EMPTY_STATE;
        }
        setState(new java.util.function.UnaryOperator() { // from class: com.android.server.location.provider.MockableLocationProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.location.provider.MockableLocationProvider.lambda$setProviderLocked$0(newState, (com.android.server.location.provider.AbstractLocationProvider.State) obj);
            }
        });
    }

    static /* synthetic */ com.android.server.location.provider.AbstractLocationProvider.State lambda$setProviderLocked$0(com.android.server.location.provider.AbstractLocationProvider.State newState, com.android.server.location.provider.AbstractLocationProvider.State prevState) {
        return newState;
    }

    public boolean isMock() {
        boolean z;
        synchronized (this.mOwnerLock) {
            z = this.mMockProvider != null && this.mProvider == this.mMockProvider;
        }
        return z;
    }

    public void setMockProviderAllowed(boolean allowed) {
        synchronized (this.mOwnerLock) {
            com.android.internal.util.Preconditions.checkState(isMock());
            this.mMockProvider.setProviderAllowed(allowed);
        }
    }

    public void setMockProviderLocation(android.location.Location location) {
        synchronized (this.mOwnerLock) {
            com.android.internal.util.Preconditions.checkState(isMock());
            this.mMockProvider.setProviderLocation(location);
        }
    }

    public android.location.provider.ProviderRequest getCurrentRequest() {
        android.location.provider.ProviderRequest providerRequest;
        synchronized (this.mOwnerLock) {
            providerRequest = this.mRequest;
        }
        return providerRequest;
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onStart() {
        synchronized (this.mOwnerLock) {
            com.android.internal.util.Preconditions.checkState(!this.mStarted);
            this.mStarted = true;
            if (this.mProvider != null) {
                this.mProvider.getController().start();
                if (!this.mRequest.equals(android.location.provider.ProviderRequest.EMPTY_REQUEST)) {
                    this.mProvider.getController().setRequest(this.mRequest);
                }
            }
        }
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onStop() {
        synchronized (this.mOwnerLock) {
            com.android.internal.util.Preconditions.checkState(this.mStarted);
            this.mStarted = false;
            if (this.mProvider != null) {
                if (!this.mRequest.equals(android.location.provider.ProviderRequest.EMPTY_REQUEST)) {
                    this.mProvider.getController().setRequest(android.location.provider.ProviderRequest.EMPTY_REQUEST);
                }
                this.mProvider.getController().stop();
            }
            this.mRequest = android.location.provider.ProviderRequest.EMPTY_REQUEST;
        }
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onSetRequest(android.location.provider.ProviderRequest request) {
        synchronized (this.mOwnerLock) {
            if (request == this.mRequest) {
                return;
            }
            this.mRequest = request;
            if (this.mProvider != null) {
                this.mProvider.getController().setRequest(request);
            }
        }
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onFlush(java.lang.Runnable callback) {
        synchronized (this.mOwnerLock) {
            if (this.mProvider != null) {
                this.mProvider.getController().flush(callback);
            } else {
                callback.run();
            }
        }
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    protected void onExtraCommand(int uid, int pid, java.lang.String command, android.os.Bundle extras) {
        synchronized (this.mOwnerLock) {
            if (this.mProvider != null) {
                this.mProvider.getController().sendExtraCommand(uid, pid, command, extras);
            }
        }
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        com.android.server.location.provider.AbstractLocationProvider provider;
        com.android.server.location.provider.AbstractLocationProvider.State providerState;
        com.android.internal.util.Preconditions.checkState(!java.lang.Thread.holdsLock(this.mOwnerLock));
        synchronized (this.mOwnerLock) {
            provider = this.mProvider;
            providerState = getState();
        }
        pw.println("allowed=" + providerState.allowed);
        if (providerState.identity != null) {
            pw.println("identity=" + providerState.identity);
        }
        if (!providerState.extraAttributionTags.isEmpty()) {
            pw.println("extra attribution tags=" + providerState.extraAttributionTags);
        }
        if (providerState.properties != null) {
            pw.println("properties=" + providerState.properties);
        }
        if (provider != null) {
            provider.dump(fd, pw, args);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ListenerWrapper implements com.android.server.location.provider.AbstractLocationProvider.Listener {
        private final com.android.server.location.provider.AbstractLocationProvider mListenerProvider;

        ListenerWrapper(com.android.server.location.provider.AbstractLocationProvider listenerProvider) {
            this.mListenerProvider = listenerProvider;
        }

        @Override // com.android.server.location.provider.AbstractLocationProvider.Listener
        public final void onStateChanged(com.android.server.location.provider.AbstractLocationProvider.State oldState, final com.android.server.location.provider.AbstractLocationProvider.State newState) {
            synchronized (com.android.server.location.provider.MockableLocationProvider.this.mOwnerLock) {
                if (this.mListenerProvider != com.android.server.location.provider.MockableLocationProvider.this.mProvider) {
                    return;
                }
                com.android.server.location.provider.MockableLocationProvider.this.setState(new java.util.function.UnaryOperator() { // from class: com.android.server.location.provider.MockableLocationProvider$ListenerWrapper$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.location.provider.MockableLocationProvider.ListenerWrapper.lambda$onStateChanged$0(newState, (com.android.server.location.provider.AbstractLocationProvider.State) obj);
                    }
                });
            }
        }

        static /* synthetic */ com.android.server.location.provider.AbstractLocationProvider.State lambda$onStateChanged$0(com.android.server.location.provider.AbstractLocationProvider.State newState, com.android.server.location.provider.AbstractLocationProvider.State prevState) {
            return newState;
        }

        @Override // com.android.server.location.provider.AbstractLocationProvider.Listener
        public final void onReportLocation(android.location.LocationResult locationResult) {
            synchronized (com.android.server.location.provider.MockableLocationProvider.this.mOwnerLock) {
                if (this.mListenerProvider != com.android.server.location.provider.MockableLocationProvider.this.mProvider) {
                    return;
                }
                com.android.server.location.provider.MockableLocationProvider.this.reportLocation(locationResult);
            }
        }
    }
}
