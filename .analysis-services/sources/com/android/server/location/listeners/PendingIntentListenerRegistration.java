package com.android.server.location.listeners;

/* JADX INFO: loaded from: classes2.dex */
public abstract class PendingIntentListenerRegistration<TKey, TListener> extends com.android.server.location.listeners.RemovableListenerRegistration<TKey, TListener> implements android.app.PendingIntent.CancelListener {
    protected abstract android.app.PendingIntent getPendingIntentFromKey(TKey tkey);

    protected PendingIntentListenerRegistration(TListener listener) {
        super(com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, listener);
    }

    @Override // com.android.server.location.listeners.RemovableListenerRegistration
    protected void onRegister() {
        super.onRegister();
        if (!getPendingIntentFromKey(getKey()).addCancelListener(com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, this)) {
            remove();
        }
    }

    @Override // com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
    protected void onUnregister() {
        getPendingIntentFromKey(getKey()).removeCancelListener(this);
        super.onUnregister();
    }

    @Override // com.android.server.location.listeners.ListenerRegistration
    protected void onOperationFailure(com.android.internal.listeners.ListenerExecutor.ListenerOperation<TListener> operation, java.lang.Exception e) {
        if (e instanceof android.app.PendingIntent.CanceledException) {
            android.util.Log.w(getTag(), "registration " + this + " removed", e);
            remove();
        } else {
            super.onOperationFailure(operation, e);
        }
    }

    public void onCanceled(android.app.PendingIntent intent) {
        if (android.util.Log.isLoggable(getTag(), 3)) {
            android.util.Log.d(getTag(), "pending intent registration " + this + " canceled");
        }
        remove();
    }
}
