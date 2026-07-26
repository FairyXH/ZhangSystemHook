package com.android.server.twilight;

/* JADX INFO: loaded from: classes3.dex */
public final class TwilightService extends com.android.server.SystemService implements android.app.AlarmManager.OnAlarmListener, android.os.Handler.Callback, android.location.LocationListener {
    private static final java.lang.String ATTRIBUTION_TAG = "TwilightService";
    private static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final int MSG_START_LISTENING = 1;
    private static final int MSG_STOP_LISTENING = 2;
    private static final java.lang.String TAG = "TwilightService";
    protected android.app.AlarmManager mAlarmManager;
    private boolean mBootCompleted;
    private final android.os.Handler mHandler;
    private boolean mHasListeners;
    protected android.location.Location mLastLocation;
    protected com.android.server.twilight.TwilightState mLastTwilightState;
    private final android.util.ArrayMap<com.android.server.twilight.TwilightListener, android.os.Handler> mListeners;
    private android.location.LocationManager mLocationManager;
    private android.content.BroadcastReceiver mTimeChangedReceiver;

    public TwilightService(android.content.Context context) {
        super(context.createAttributionContext("TwilightService"));
        this.mListeners = new android.util.ArrayMap<>();
        this.mHandler = new android.os.Handler(android.os.Looper.getMainLooper(), this);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishLocalService(com.android.server.twilight.TwilightManager.class, new com.android.server.twilight.TwilightManager() { // from class: com.android.server.twilight.TwilightService.1
            @Override // com.android.server.twilight.TwilightManager
            public void registerListener(com.android.server.twilight.TwilightListener listener, android.os.Handler handler) {
                synchronized (com.android.server.twilight.TwilightService.this.mListeners) {
                    boolean wasEmpty = com.android.server.twilight.TwilightService.this.mListeners.isEmpty();
                    com.android.server.twilight.TwilightService.this.mListeners.put(listener, handler);
                    if (wasEmpty && !com.android.server.twilight.TwilightService.this.mListeners.isEmpty()) {
                        com.android.server.twilight.TwilightService.this.mHandler.sendEmptyMessage(1);
                    }
                }
            }

            @Override // com.android.server.twilight.TwilightManager
            public void unregisterListener(com.android.server.twilight.TwilightListener listener) {
                synchronized (com.android.server.twilight.TwilightService.this.mListeners) {
                    boolean wasEmpty = com.android.server.twilight.TwilightService.this.mListeners.isEmpty();
                    com.android.server.twilight.TwilightService.this.mListeners.remove(listener);
                    if (!wasEmpty && com.android.server.twilight.TwilightService.this.mListeners.isEmpty()) {
                        com.android.server.twilight.TwilightService.this.mHandler.sendEmptyMessage(2);
                    }
                }
            }

            @Override // com.android.server.twilight.TwilightManager
            public com.android.server.twilight.TwilightState getLastTwilightState() {
                com.android.server.twilight.TwilightState twilightState;
                synchronized (com.android.server.twilight.TwilightService.this.mListeners) {
                    twilightState = com.android.server.twilight.TwilightService.this.mLastTwilightState;
                }
                return twilightState;
            }
        });
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 1000) {
            android.content.Context c = getContext();
            this.mAlarmManager = (android.app.AlarmManager) c.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
            this.mLocationManager = (android.location.LocationManager) c.getSystemService("location");
            this.mBootCompleted = true;
            if (this.mHasListeners) {
                startListening();
            }
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case 1:
                if (!this.mHasListeners) {
                    this.mHasListeners = true;
                    if (this.mBootCompleted) {
                        startListening();
                    }
                }
                return true;
            case 2:
                if (this.mHasListeners) {
                    this.mHasListeners = false;
                    if (this.mBootCompleted) {
                        stopListening();
                    }
                }
                return true;
            default:
                return false;
        }
    }

    private void startListening() {
        android.util.Slog.d("TwilightService", "startListening");
        if (!this.mLocationManager.hasProvider("fused")) {
            synchronized (this.mListeners) {
                if (!this.mListeners.isEmpty()) {
                    this.mListeners.clear();
                }
            }
            return;
        }
        android.location.LocationRequest req = android.location.LocationRequest.create().setQuality(104);
        this.mLocationManager.requestLocationUpdates(req, this, android.os.Looper.getMainLooper());
        if (this.mLocationManager.getLastLocation() == null) {
            if (this.mLocationManager.isProviderEnabled("network")) {
                this.mLocationManager.getCurrentLocation("network", null, getContext().getMainExecutor(), new com.android.server.twilight.TwilightService$$ExternalSyntheticLambda0(this));
                if (DEBUG) {
                    android.util.Slog.d("TwilightService", "startListening: NETWORK_PROVIDER true");
                }
            } else if (this.mLocationManager.isProviderEnabled(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS)) {
                this.mLocationManager.getCurrentLocation(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS, null, getContext().getMainExecutor(), new com.android.server.twilight.TwilightService$$ExternalSyntheticLambda0(this));
                if (DEBUG) {
                    android.util.Slog.d("TwilightService", "startListening: GPS_PROVIDER true");
                }
            }
        }
        if (this.mTimeChangedReceiver == null) {
            this.mTimeChangedReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.twilight.TwilightService.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context context, android.content.Intent intent) {
                    android.util.Slog.d("TwilightService", "onReceive: " + intent);
                    com.android.server.twilight.TwilightService.this.updateTwilightState();
                }
            };
            android.content.IntentFilter intentFilter = new android.content.IntentFilter("android.intent.action.TIME_SET");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            getContext().registerReceiver(this.mTimeChangedReceiver, intentFilter);
        }
        updateTwilightState();
    }

    private void stopListening() {
        android.util.Slog.d("TwilightService", "stopListening");
        if (this.mTimeChangedReceiver != null) {
            getContext().unregisterReceiver(this.mTimeChangedReceiver);
            this.mTimeChangedReceiver = null;
        }
        if (this.mLastTwilightState != null) {
            this.mAlarmManager.cancel(this);
        }
        this.mLocationManager.removeUpdates(this);
        this.mLastLocation = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTwilightState() {
        long currentTimeMillis = java.lang.System.currentTimeMillis();
        android.location.Location location = this.mLastLocation != null ? this.mLastLocation : this.mLocationManager.getLastLocation();
        final com.android.server.twilight.TwilightState state = calculateTwilightState(location, currentTimeMillis);
        if (DEBUG) {
            android.util.Slog.d("TwilightService", "updateTwilightState: " + state);
        }
        synchronized (this.mListeners) {
            if (!java.util.Objects.equals(this.mLastTwilightState, state)) {
                this.mLastTwilightState = state;
                for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                    final com.android.server.twilight.TwilightListener listener = this.mListeners.keyAt(i);
                    android.os.Handler handler = this.mListeners.valueAt(i);
                    handler.post(new java.lang.Runnable() { // from class: com.android.server.twilight.TwilightService$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            listener.onTwilightStateChanged(state);
                        }
                    });
                }
            }
        }
        if (state != null) {
            long triggerAtMillis = state.isNight() ? state.sunriseTimeMillis() : state.sunsetTimeMillis();
            this.mAlarmManager.setExact(1, triggerAtMillis, "TwilightService", this, this.mHandler);
        }
    }

    @Override // android.app.AlarmManager.OnAlarmListener
    public void onAlarm() {
        android.util.Slog.d("TwilightService", "onAlarm");
        updateTwilightState();
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(android.location.Location location) {
        if (location != null) {
            android.util.Slog.d("TwilightService", "onLocationChanged: provider=" + location.getProvider() + " accuracy=" + location.getAccuracy() + " time=" + location.getTime());
            this.mLastLocation = location;
            updateTwilightState();
        }
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(java.lang.String provider, int status, android.os.Bundle extras) {
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(java.lang.String provider) {
        if (DEBUG) {
            android.util.Slog.d("TwilightService", "onProviderEnabled: provider=" + provider);
        }
        if (this.mLocationManager != null) {
            this.mLocationManager.getCurrentLocation(provider, null, getContext().getMainExecutor(), new com.android.server.twilight.TwilightService$$ExternalSyntheticLambda0(this));
        }
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(java.lang.String provider) {
        if (DEBUG) {
            android.util.Slog.d("TwilightService", "onProviderDisabled");
        }
    }

    private static com.android.server.twilight.TwilightState calculateTwilightState(android.location.Location location, long timeMillis) {
        if (location == null) {
            return null;
        }
        com.ibm.icu.impl.CalendarAstronomer ca = new com.ibm.icu.impl.CalendarAstronomer(location.getLongitude(), location.getLatitude());
        android.icu.util.Calendar noon = android.icu.util.Calendar.getInstance();
        noon.setTimeInMillis(timeMillis);
        noon.set(11, 12);
        noon.set(12, 0);
        noon.set(13, 0);
        noon.set(14, 0);
        ca.setTime(noon.getTimeInMillis());
        long sunriseTimeMillis = ca.getSunRiseSet(true);
        long sunsetTimeMillis = ca.getSunRiseSet(false);
        if (sunsetTimeMillis < timeMillis) {
            noon.add(5, 1);
            ca.setTime(noon.getTimeInMillis());
            sunriseTimeMillis = ca.getSunRiseSet(true);
        } else if (sunriseTimeMillis > timeMillis) {
            noon.add(5, -1);
            ca.setTime(noon.getTimeInMillis());
            sunsetTimeMillis = ca.getSunRiseSet(false);
        }
        return new com.android.server.twilight.TwilightState(sunriseTimeMillis, sunsetTimeMillis);
    }
}
