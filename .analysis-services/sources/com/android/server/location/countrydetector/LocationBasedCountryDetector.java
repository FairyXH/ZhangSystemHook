package com.android.server.location.countrydetector;

/* JADX INFO: loaded from: classes2.dex */
public class LocationBasedCountryDetector extends com.android.server.location.countrydetector.CountryDetectorBase {
    private static final long QUERY_LOCATION_TIMEOUT = 300000;
    private static final java.lang.String TAG = "LocationBasedCountryDetector";
    private java.util.List<java.lang.String> mEnabledProviders;
    protected java.util.List<android.location.LocationListener> mLocationListeners;
    private android.location.LocationManager mLocationManager;
    protected java.lang.Thread mQueryThread;
    protected java.util.Timer mTimer;

    public LocationBasedCountryDetector(android.content.Context ctx) {
        super(ctx);
        this.mLocationManager = (android.location.LocationManager) ctx.getSystemService("location");
    }

    protected java.lang.String getCountryFromLocation(android.location.Location location) {
        android.location.Geocoder geoCoder = new android.location.Geocoder(this.mContext);
        try {
            java.util.List<android.location.Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses == null || addresses.size() <= 0) {
                return null;
            }
            java.lang.String country = addresses.get(0).getCountryCode();
            return country;
        } catch (java.io.IOException e) {
            android.util.Slog.w(TAG, "Exception occurs when getting country from location");
            return null;
        }
    }

    protected boolean isAcceptableProvider(java.lang.String provider) {
        return "passive".equals(provider);
    }

    protected void registerListener(java.lang.String provider, android.location.LocationListener listener) {
        long bid = android.os.Binder.clearCallingIdentity();
        try {
            this.mLocationManager.requestLocationUpdates(provider, 0L, 0.0f, listener);
        } finally {
            android.os.Binder.restoreCallingIdentity(bid);
        }
    }

    protected void unregisterListener(android.location.LocationListener listener) {
        long bid = android.os.Binder.clearCallingIdentity();
        try {
            this.mLocationManager.removeUpdates(listener);
        } finally {
            android.os.Binder.restoreCallingIdentity(bid);
        }
    }

    protected android.location.Location getLastKnownLocation() {
        long bid = android.os.Binder.clearCallingIdentity();
        try {
            java.util.List<java.lang.String> providers = this.mLocationManager.getAllProviders();
            android.location.Location bestLocation = null;
            for (java.lang.String provider : providers) {
                android.location.Location lastKnownLocation = this.mLocationManager.getLastKnownLocation(provider);
                if (lastKnownLocation != null && (bestLocation == null || bestLocation.getElapsedRealtimeNanos() < lastKnownLocation.getElapsedRealtimeNanos())) {
                    bestLocation = lastKnownLocation;
                }
            }
            return bestLocation;
        } finally {
            android.os.Binder.restoreCallingIdentity(bid);
        }
    }

    protected long getQueryLocationTimeout() {
        return 300000L;
    }

    protected java.util.List<java.lang.String> getEnabledProviders() {
        if (this.mEnabledProviders == null) {
            this.mEnabledProviders = this.mLocationManager.getProviders(true);
        }
        return this.mEnabledProviders;
    }

    @Override // com.android.server.location.countrydetector.CountryDetectorBase
    public synchronized android.location.Country detectCountry() {
        if (this.mLocationListeners != null) {
            throw new java.lang.IllegalStateException();
        }
        java.util.List<java.lang.String> enabledProviders = getEnabledProviders();
        int totalProviders = enabledProviders.size();
        if (totalProviders > 0) {
            this.mLocationListeners = new java.util.ArrayList(totalProviders);
            for (int i = 0; i < totalProviders; i++) {
                java.lang.String provider = enabledProviders.get(i);
                if (isAcceptableProvider(provider)) {
                    android.location.LocationListener listener = new android.location.LocationListener() { // from class: com.android.server.location.countrydetector.LocationBasedCountryDetector.1
                        @Override // android.location.LocationListener
                        public void onLocationChanged(android.location.Location location) {
                            if (location != null) {
                                com.android.server.location.countrydetector.LocationBasedCountryDetector.this.stop();
                                com.android.server.location.countrydetector.LocationBasedCountryDetector.this.queryCountryCode(location);
                            }
                        }

                        @Override // android.location.LocationListener
                        public void onProviderDisabled(java.lang.String provider2) {
                        }

                        @Override // android.location.LocationListener
                        public void onProviderEnabled(java.lang.String provider2) {
                        }

                        @Override // android.location.LocationListener
                        public void onStatusChanged(java.lang.String provider2, int status, android.os.Bundle extras) {
                        }
                    };
                    this.mLocationListeners.add(listener);
                    registerListener(provider, listener);
                }
            }
            this.mTimer = new java.util.Timer();
            this.mTimer.schedule(new java.util.TimerTask() { // from class: com.android.server.location.countrydetector.LocationBasedCountryDetector.2
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    com.android.server.location.countrydetector.LocationBasedCountryDetector.this.mTimer = null;
                    com.android.server.location.countrydetector.LocationBasedCountryDetector.this.stop();
                    com.android.server.location.countrydetector.LocationBasedCountryDetector.this.queryCountryCode(com.android.server.location.countrydetector.LocationBasedCountryDetector.this.getLastKnownLocation());
                }
            }, getQueryLocationTimeout());
        } else {
            queryCountryCode(getLastKnownLocation());
        }
        return this.mDetectedCountry;
    }

    @Override // com.android.server.location.countrydetector.CountryDetectorBase
    public synchronized void stop() {
        if (this.mLocationListeners != null) {
            for (android.location.LocationListener listener : this.mLocationListeners) {
                unregisterListener(listener);
            }
            this.mLocationListeners = null;
        }
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void queryCountryCode(final android.location.Location location) {
        if (this.mQueryThread != null) {
            return;
        }
        this.mQueryThread = new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.location.countrydetector.LocationBasedCountryDetector.3
            @Override // java.lang.Runnable
            public void run() {
                if (location == null) {
                    com.android.server.location.countrydetector.LocationBasedCountryDetector.this.notifyListener(null);
                    return;
                }
                java.lang.String countryIso = com.android.server.location.countrydetector.LocationBasedCountryDetector.this.getCountryFromLocation(location);
                if (countryIso != null) {
                    com.android.server.location.countrydetector.LocationBasedCountryDetector.this.mDetectedCountry = new android.location.Country(countryIso, 1);
                } else {
                    com.android.server.location.countrydetector.LocationBasedCountryDetector.this.mDetectedCountry = null;
                }
                com.android.server.location.countrydetector.LocationBasedCountryDetector.this.notifyListener(com.android.server.location.countrydetector.LocationBasedCountryDetector.this.mDetectedCountry);
                com.android.server.location.countrydetector.LocationBasedCountryDetector.this.mQueryThread = null;
            }
        });
        this.mQueryThread.start();
    }
}
