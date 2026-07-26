package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class LocationPowerSaveModeHelper {
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.injector.LocationPowerSaveModeHelper.LocationPowerSaveModeChangedListener> mListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public interface LocationPowerSaveModeChangedListener {
        void onLocationPowerSaveModeChanged(int i);
    }

    public abstract int getLocationPowerSaveMode();

    public final void addListener(com.android.server.location.injector.LocationPowerSaveModeHelper.LocationPowerSaveModeChangedListener listener) {
        this.mListeners.add(listener);
    }

    public final void removeListener(com.android.server.location.injector.LocationPowerSaveModeHelper.LocationPowerSaveModeChangedListener listener) {
        this.mListeners.remove(listener);
    }

    protected final void notifyLocationPowerSaveModeChanged(int locationPowerSaveMode) {
        android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "location power save mode is now " + android.os.PowerManager.locationPowerSaveModeToString(locationPowerSaveMode));
        com.android.server.location.eventlog.LocationEventLog.EVENT_LOG.logLocationPowerSaveMode(locationPowerSaveMode);
        for (com.android.server.location.injector.LocationPowerSaveModeHelper.LocationPowerSaveModeChangedListener listener : this.mListeners) {
            listener.onLocationPowerSaveModeChanged(locationPowerSaveMode);
        }
    }
}
