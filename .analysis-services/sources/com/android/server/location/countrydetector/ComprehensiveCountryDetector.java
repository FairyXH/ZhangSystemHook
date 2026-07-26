package com.android.server.location.countrydetector;

/* JADX INFO: loaded from: classes2.dex */
public class ComprehensiveCountryDetector extends com.android.server.location.countrydetector.CountryDetectorBase {
    static final boolean DEBUG = false;
    private static final long LOCATION_REFRESH_INTERVAL = 86400000;
    private static final int MAX_LENGTH_DEBUG_LOGS = 20;
    private static final java.lang.String TAG = "CountryDetector";
    private int mCountServiceStateChanges;
    private android.location.Country mCountry;
    private android.location.Country mCountryFromLocation;
    private final java.util.concurrent.ConcurrentLinkedQueue<android.location.Country> mDebugLogs;
    private android.location.Country mLastCountryAddedToLogs;
    private android.location.CountryListener mLocationBasedCountryDetectionListener;
    protected com.android.server.location.countrydetector.CountryDetectorBase mLocationBasedCountryDetector;
    protected java.util.Timer mLocationRefreshTimer;
    private final java.lang.Object mObject;
    private android.telephony.PhoneStateListener mPhoneStateListener;
    private long mStartTime;
    private long mStopTime;
    private boolean mStopped;
    private final android.telephony.TelephonyManager mTelephonyManager;
    private int mTotalCountServiceStateChanges;
    private long mTotalTime;

    public ComprehensiveCountryDetector(android.content.Context context) {
        super(context);
        this.mStopped = false;
        this.mDebugLogs = new java.util.concurrent.ConcurrentLinkedQueue<>();
        this.mObject = new java.lang.Object();
        this.mLocationBasedCountryDetectionListener = new android.location.CountryListener() { // from class: com.android.server.location.countrydetector.ComprehensiveCountryDetector.1
            public void onCountryDetected(android.location.Country country) {
                com.android.server.location.countrydetector.ComprehensiveCountryDetector.this.mCountryFromLocation = country;
                com.android.server.location.countrydetector.ComprehensiveCountryDetector.this.detectCountry(true, false);
                com.android.server.location.countrydetector.ComprehensiveCountryDetector.this.stopLocationBasedDetector();
            }
        };
        this.mTelephonyManager = (android.telephony.TelephonyManager) context.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
    }

    @Override // com.android.server.location.countrydetector.CountryDetectorBase
    public android.location.Country detectCountry() {
        return detectCountry(false, !this.mStopped);
    }

    @Override // com.android.server.location.countrydetector.CountryDetectorBase
    public void stop() {
        android.util.Slog.i(TAG, "Stop the detector.");
        cancelLocationRefresh();
        removePhoneStateListener();
        stopLocationBasedDetector();
        this.mListener = null;
        this.mStopped = true;
    }

    private android.location.Country getCountry() {
        android.location.Country result = getNetworkBasedCountry();
        if (result == null) {
            result = getLastKnownLocationBasedCountry();
        }
        if (result == null) {
            result = getSimBasedCountry();
        }
        if (result == null) {
            result = getLocaleCountry();
        }
        addToLogs(result);
        return result;
    }

    private void addToLogs(android.location.Country country) {
        if (country == null) {
            return;
        }
        synchronized (this.mObject) {
            if (this.mLastCountryAddedToLogs == null || !this.mLastCountryAddedToLogs.equals(country)) {
                this.mLastCountryAddedToLogs = country;
                if (this.mDebugLogs.size() >= 20) {
                    this.mDebugLogs.poll();
                }
                this.mDebugLogs.add(country);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNetworkCountryCodeAvailable() {
        int phoneType = this.mTelephonyManager.getPhoneType();
        return phoneType == 1;
    }

    protected android.location.Country getNetworkBasedCountry() {
        if (isNetworkCountryCodeAvailable()) {
            java.lang.String countryIso = this.mTelephonyManager.getNetworkCountryIso();
            if (!android.text.TextUtils.isEmpty(countryIso)) {
                return new android.location.Country(countryIso, 0);
            }
            return null;
        }
        return null;
    }

    protected android.location.Country getLastKnownLocationBasedCountry() {
        return this.mCountryFromLocation;
    }

    protected android.location.Country getSimBasedCountry() {
        java.lang.String countryIso = this.mTelephonyManager.getSimCountryIso();
        if (!android.text.TextUtils.isEmpty(countryIso)) {
            return new android.location.Country(countryIso, 2);
        }
        return null;
    }

    protected android.location.Country getLocaleCountry() {
        java.util.Locale defaultLocale = java.util.Locale.getDefault();
        if (defaultLocale != null) {
            return new android.location.Country(defaultLocale.getCountry(), 3);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.location.Country detectCountry(boolean notifyChange, boolean startLocationBasedDetection) {
        android.location.Country country = getCountry();
        runAfterDetectionAsync(this.mCountry != null ? new android.location.Country(this.mCountry) : this.mCountry, country, notifyChange, startLocationBasedDetection);
        this.mCountry = country;
        return this.mCountry;
    }

    protected void runAfterDetectionAsync(final android.location.Country country, final android.location.Country detectedCountry, final boolean notifyChange, final boolean startLocationBasedDetection) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.location.countrydetector.ComprehensiveCountryDetector.2
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.location.countrydetector.ComprehensiveCountryDetector.this.runAfterDetection(country, detectedCountry, notifyChange, startLocationBasedDetection);
            }
        });
    }

    @Override // com.android.server.location.countrydetector.CountryDetectorBase
    public void setCountryListener(android.location.CountryListener listener) {
        android.location.CountryListener prevListener = this.mListener;
        this.mListener = listener;
        if (this.mListener == null) {
            removePhoneStateListener();
            stopLocationBasedDetector();
            cancelLocationRefresh();
            this.mStopTime = android.os.SystemClock.elapsedRealtime();
            this.mTotalTime += this.mStopTime;
            return;
        }
        if (prevListener == null) {
            addPhoneStateListener();
            detectCountry(false, true);
            this.mStartTime = android.os.SystemClock.elapsedRealtime();
            this.mStopTime = 0L;
            this.mCountServiceStateChanges = 0;
        }
    }

    void runAfterDetection(android.location.Country country, android.location.Country detectedCountry, boolean notifyChange, boolean startLocationBasedDetection) {
        notifyIfCountryChanged(country, detectedCountry);
        if (startLocationBasedDetection && ((detectedCountry == null || detectedCountry.getSource() > 1) && ((isAirplaneModeOff() || isWifiOn()) && this.mListener != null && isGeoCoderImplemented()))) {
            startLocationBasedDetector(this.mLocationBasedCountryDetectionListener);
        }
        if (detectedCountry == null || detectedCountry.getSource() >= 1) {
            scheduleLocationRefresh();
        } else {
            cancelLocationRefresh();
            stopLocationBasedDetector();
        }
    }

    private synchronized void startLocationBasedDetector(android.location.CountryListener listener) {
        if (this.mLocationBasedCountryDetector != null) {
            return;
        }
        this.mLocationBasedCountryDetector = createLocationBasedCountryDetector();
        this.mLocationBasedCountryDetector.setCountryListener(listener);
        this.mLocationBasedCountryDetector.detectCountry();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void stopLocationBasedDetector() {
        if (this.mLocationBasedCountryDetector != null) {
            this.mLocationBasedCountryDetector.stop();
            this.mLocationBasedCountryDetector = null;
        }
    }

    protected com.android.server.location.countrydetector.CountryDetectorBase createLocationBasedCountryDetector() {
        return new com.android.server.location.countrydetector.LocationBasedCountryDetector(this.mContext);
    }

    protected boolean isAirplaneModeOff() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 0;
    }

    protected boolean isWifiOn() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "wifi_on", 0) != 0;
    }

    private void notifyIfCountryChanged(android.location.Country country, android.location.Country detectedCountry) {
        if (detectedCountry != null && this.mListener != null) {
            if (country == null || !country.equals(detectedCountry)) {
                notifyListener(detectedCountry);
            }
        }
    }

    private synchronized void scheduleLocationRefresh() {
        if (this.mLocationRefreshTimer != null) {
            return;
        }
        this.mLocationRefreshTimer = new java.util.Timer();
        this.mLocationRefreshTimer.schedule(new java.util.TimerTask() { // from class: com.android.server.location.countrydetector.ComprehensiveCountryDetector.3
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                com.android.server.location.countrydetector.ComprehensiveCountryDetector.this.mLocationRefreshTimer = null;
                com.android.server.location.countrydetector.ComprehensiveCountryDetector.this.detectCountry(false, true);
            }
        }, 86400000L);
    }

    private synchronized void cancelLocationRefresh() {
        if (this.mLocationRefreshTimer != null) {
            this.mLocationRefreshTimer.cancel();
            this.mLocationRefreshTimer = null;
        }
    }

    protected synchronized void addPhoneStateListener() {
        if (this.mPhoneStateListener == null) {
            this.mPhoneStateListener = new android.telephony.PhoneStateListener() { // from class: com.android.server.location.countrydetector.ComprehensiveCountryDetector.4
                @Override // android.telephony.PhoneStateListener
                public void onServiceStateChanged(android.telephony.ServiceState serviceState) {
                    com.android.server.location.countrydetector.ComprehensiveCountryDetector.this.mCountServiceStateChanges++;
                    com.android.server.location.countrydetector.ComprehensiveCountryDetector.this.mTotalCountServiceStateChanges++;
                    if (!com.android.server.location.countrydetector.ComprehensiveCountryDetector.this.isNetworkCountryCodeAvailable()) {
                        return;
                    }
                    com.android.server.location.countrydetector.ComprehensiveCountryDetector.this.detectCountry(true, true);
                }
            };
            this.mTelephonyManager.listen(this.mPhoneStateListener, 1);
        }
    }

    protected synchronized void removePhoneStateListener() {
        if (this.mPhoneStateListener != null) {
            this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
            this.mPhoneStateListener = null;
        }
    }

    protected boolean isGeoCoderImplemented() {
        return android.location.Geocoder.isPresent();
    }

    public java.lang.String toString() {
        long currentTime = android.os.SystemClock.elapsedRealtime();
        long currentSessionLength = 0;
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("ComprehensiveCountryDetector{");
        if (this.mStopTime == 0) {
            currentSessionLength = currentTime - this.mStartTime;
            sb.append("timeRunning=" + currentSessionLength + ", ");
        } else {
            sb.append("lastRunTimeLength=" + (this.mStopTime - this.mStartTime) + ", ");
        }
        sb.append("totalCountServiceStateChanges=" + this.mTotalCountServiceStateChanges + ", ");
        sb.append("currentCountServiceStateChanges=" + this.mCountServiceStateChanges + ", ");
        sb.append("totalTime=" + (this.mTotalTime + currentSessionLength) + ", ");
        sb.append("currentTime=" + currentTime + ", ");
        sb.append("countries=");
        for (android.location.Country country : this.mDebugLogs) {
            sb.append("\n   " + country.toString());
        }
        sb.append("}");
        return sb.toString();
    }
}
