package com.android.server.location.listeners;

/* JADX INFO: loaded from: classes2.dex */
public class ListenerRegistration<TListener> implements com.android.internal.listeners.ListenerExecutor {
    private boolean mActive = false;
    private final java.util.concurrent.Executor mExecutor;
    private volatile TListener mListener;

    protected ListenerRegistration(java.util.concurrent.Executor executor, TListener tlistener) {
        this.mExecutor = (java.util.concurrent.Executor) java.util.Objects.requireNonNull(executor);
        this.mListener = (TListener) java.util.Objects.requireNonNull(tlistener);
    }

    protected java.lang.String getTag() {
        return "ListenerRegistration";
    }

    protected final java.util.concurrent.Executor getExecutor() {
        return this.mExecutor;
    }

    protected void onRegister(java.lang.Object key) {
    }

    protected void onUnregister() {
    }

    protected void onActive() {
    }

    protected void onInactive() {
    }

    public final boolean isActive() {
        return this.mActive;
    }

    final boolean setActive(boolean active) {
        if (active != this.mActive) {
            this.mActive = active;
            return true;
        }
        return false;
    }

    public final boolean isRegistered() {
        return this.mListener != null;
    }

    final void unregisterInternal() {
        this.mListener = null;
        onListenerUnregister();
    }

    protected void onListenerUnregister() {
    }

    protected void onOperationFailure(com.android.internal.listeners.ListenerExecutor.ListenerOperation<TListener> operation, java.lang.Exception exception) {
        throw new java.lang.AssertionError(exception);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Object lambda$executeOperation$0() {
        return this.mListener;
    }

    protected final void executeOperation(com.android.internal.listeners.ListenerExecutor.ListenerOperation<TListener> operation) {
        executeSafely(this.mExecutor, new java.util.function.Supplier() { // from class: com.android.server.location.listeners.ListenerRegistration$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$executeOperation$0();
            }
        }, operation, new com.android.internal.listeners.ListenerExecutor.FailureCallback() { // from class: com.android.server.location.listeners.ListenerRegistration$$ExternalSyntheticLambda1
            public final void onFailure(com.android.internal.listeners.ListenerExecutor.ListenerOperation listenerOperation, java.lang.Exception exc) {
                this.f$0.onOperationFailure(listenerOperation, exc);
            }
        });
    }

    public java.lang.String toString() {
        return "[]";
    }

    public final boolean equals(java.lang.Object obj) {
        return this == obj;
    }

    public final int hashCode() {
        return super.hashCode();
    }
}
