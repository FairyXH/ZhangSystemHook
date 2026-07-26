package com.android.server.location.listeners;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BinderListenerRegistration<TKey, TListener> extends com.android.server.location.listeners.RemovableListenerRegistration<TKey, TListener> implements android.os.IBinder.DeathRecipient {
    protected abstract android.os.IBinder getBinderFromKey(TKey tkey);

    protected BinderListenerRegistration(java.util.concurrent.Executor executor, TListener listener) {
        super(executor, listener);
    }

    @Override // com.android.server.location.listeners.RemovableListenerRegistration
    protected void onRegister() {
        super.onRegister();
        try {
            getBinderFromKey(getKey()).linkToDeath(this, 0);
        } catch (android.os.RemoteException e) {
            remove();
        }
    }

    @Override // com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
    protected void onUnregister() {
        try {
            getBinderFromKey(getKey()).unlinkToDeath(this, 0);
        } catch (java.util.NoSuchElementException e) {
            android.util.Log.w(getTag(), "failed to unregister binder death listener", e);
        }
        super.onUnregister();
    }

    @Override // com.android.server.location.listeners.ListenerRegistration
    public void onOperationFailure(com.android.internal.listeners.ListenerExecutor.ListenerOperation<TListener> operation, java.lang.Exception e) {
        if (e instanceof android.os.RemoteException) {
            android.util.Log.w(getTag(), "registration " + this + " removed", e);
            remove();
        } else {
            super.onOperationFailure(operation, e);
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        try {
            if (android.util.Log.isLoggable(getTag(), 3)) {
                android.util.Log.d(getTag(), "binder registration " + this + " died");
            }
            remove();
        } catch (java.lang.RuntimeException e) {
            throw new java.lang.AssertionError(e);
        }
    }
}
