package com.android.server.location.listeners;

/* JADX INFO: loaded from: classes2.dex */
public abstract class RemovableListenerRegistration<TKey, TListener> extends com.android.server.location.listeners.ListenerRegistration<TListener> {
    private volatile TKey mKey;
    private final java.util.concurrent.atomic.AtomicBoolean mRemoved;

    protected abstract com.android.server.location.listeners.ListenerMultiplexer<TKey, ? super TListener, ?, ?> getOwner();

    protected RemovableListenerRegistration(java.util.concurrent.Executor executor, TListener listener) {
        super(executor, listener);
        this.mRemoved = new java.util.concurrent.atomic.AtomicBoolean(false);
    }

    protected final TKey getKey() {
        return (TKey) java.util.Objects.requireNonNull(this.mKey);
    }

    public final void remove() {
        remove(true);
    }

    public final void remove(boolean immediately) {
        final TKey key = this.mKey;
        if (key != null && !this.mRemoved.getAndSet(true)) {
            onRemove(immediately);
            if (immediately) {
                getOwner().removeRegistration(key, this);
            } else {
                executeOperation(new com.android.internal.listeners.ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.listeners.RemovableListenerRegistration$$ExternalSyntheticLambda0
                    public final void operate(java.lang.Object obj) throws java.lang.Exception {
                        this.f$0.lambda$remove$0(key, obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$remove$0(java.lang.Object obj, java.lang.Object listener) throws java.lang.Exception {
        getOwner().removeRegistration(obj, this);
    }

    protected void onRemove(boolean immediately) {
    }

    @Override // com.android.server.location.listeners.ListenerRegistration
    protected final void onRegister(java.lang.Object obj) {
        super.onRegister(obj);
        this.mKey = (TKey) java.util.Objects.requireNonNull(obj);
        onRegister();
    }

    protected void onRegister() {
    }

    @Override // com.android.server.location.listeners.ListenerRegistration
    protected void onUnregister() {
        this.mKey = null;
        super.onUnregister();
    }
}
