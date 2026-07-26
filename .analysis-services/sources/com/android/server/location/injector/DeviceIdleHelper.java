package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DeviceIdleHelper {
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.injector.DeviceIdleHelper.DeviceIdleListener> mListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public interface DeviceIdleListener {
        void onDeviceIdleChanged(boolean z);
    }

    public abstract boolean isDeviceIdle();

    protected abstract void registerInternal();

    protected abstract void unregisterInternal();

    protected DeviceIdleHelper() {
    }

    public final synchronized void addListener(com.android.server.location.injector.DeviceIdleHelper.DeviceIdleListener listener) {
        if (this.mListeners.add(listener) && this.mListeners.size() == 1) {
            registerInternal();
        }
    }

    public final synchronized void removeListener(com.android.server.location.injector.DeviceIdleHelper.DeviceIdleListener listener) {
        if (this.mListeners.remove(listener) && this.mListeners.isEmpty()) {
            unregisterInternal();
        }
    }

    protected final void notifyDeviceIdleChanged() {
        boolean deviceIdle = isDeviceIdle();
        for (com.android.server.location.injector.DeviceIdleHelper.DeviceIdleListener listener : this.mListeners) {
            listener.onDeviceIdleChanged(deviceIdle);
        }
    }
}
