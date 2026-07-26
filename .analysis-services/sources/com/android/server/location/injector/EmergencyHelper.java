package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class EmergencyHelper {
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.injector.EmergencyHelper.EmergencyStateChangedListener> mListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public interface EmergencyStateChangedListener {
        void onStateChanged();
    }

    public abstract boolean isInEmergency(long j);

    protected EmergencyHelper() {
    }

    public void addOnEmergencyStateChangedListener(com.android.server.location.injector.EmergencyHelper.EmergencyStateChangedListener listener) {
        this.mListeners.add(listener);
    }

    public void removeOnEmergencyStateChangedListener(com.android.server.location.injector.EmergencyHelper.EmergencyStateChangedListener listener) {
        this.mListeners.remove(listener);
    }

    protected final void dispatchEmergencyStateChanged() {
        for (com.android.server.location.injector.EmergencyHelper.EmergencyStateChangedListener listener : this.mListeners) {
            listener.onStateChanged();
        }
    }
}
