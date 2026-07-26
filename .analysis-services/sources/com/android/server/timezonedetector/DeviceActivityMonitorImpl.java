package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
class DeviceActivityMonitorImpl implements com.android.server.timezonedetector.DeviceActivityMonitor {
    private static final boolean DBG = false;
    private static final java.lang.String LOG_TAG = "time_zone_detector";
    private final java.util.List<com.android.server.timezonedetector.DeviceActivityMonitor.Listener> mListeners = new java.util.ArrayList();

    static com.android.server.timezonedetector.DeviceActivityMonitor create(android.content.Context context, android.os.Handler handler) {
        return new com.android.server.timezonedetector.DeviceActivityMonitorImpl(context, handler);
    }

    private DeviceActivityMonitorImpl(android.content.Context context, android.os.Handler handler) {
        final android.content.ContentResolver contentResolver = context.getContentResolver();
        android.database.ContentObserver airplaneModeObserver = new android.database.ContentObserver(handler) { // from class: com.android.server.timezonedetector.DeviceActivityMonitorImpl.1
            @Override // android.database.ContentObserver
            public void onChange(boolean unused) {
                try {
                    int state = android.provider.Settings.Global.getInt(contentResolver, "airplane_mode_on");
                    if (state == 0) {
                        com.android.server.timezonedetector.DeviceActivityMonitorImpl.this.notifyFlightComplete();
                    }
                } catch (android.provider.Settings.SettingNotFoundException e) {
                    android.util.Slog.e(com.android.server.timezonedetector.DeviceActivityMonitorImpl.LOG_TAG, "Unable to read airplane mode state", e);
                }
            }
        };
        contentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("airplane_mode_on"), true, airplaneModeObserver);
    }

    @Override // com.android.server.timezonedetector.DeviceActivityMonitor
    public synchronized void addListener(com.android.server.timezonedetector.DeviceActivityMonitor.Listener listener) {
        java.util.Objects.requireNonNull(listener);
        this.mListeners.add(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyFlightComplete() {
        java.util.List<com.android.server.timezonedetector.DeviceActivityMonitor.Listener> listeners;
        synchronized (this) {
            listeners = new java.util.ArrayList<>(this.mListeners);
        }
        for (com.android.server.timezonedetector.DeviceActivityMonitor.Listener listener : listeners) {
            listener.onFlightComplete();
        }
    }

    @Override // com.android.server.timezonedetector.Dumpable
    public void dump(android.util.IndentingPrintWriter pw, java.lang.String[] args) {
    }
}
