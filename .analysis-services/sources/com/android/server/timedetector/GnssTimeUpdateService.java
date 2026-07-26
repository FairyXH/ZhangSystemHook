package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class GnssTimeUpdateService extends android.os.Binder {
    private static final java.lang.String ATTRIBUTION_TAG = "GnssTimeUpdateService";
    private static final boolean D = android.util.Log.isLoggable("GnssTimeUpdateService", 3);
    private static final java.time.Duration GNSS_TIME_UPDATE_ALARM_INTERVAL = java.time.Duration.ofHours(4);
    private static final java.lang.String TAG = "GnssTimeUpdateService";
    private android.app.AlarmManager.OnAlarmListener mAlarmListener;
    private final android.app.AlarmManager mAlarmManager;
    private final android.content.Context mContext;
    private volatile android.app.time.UnixEpochTime mLastSuggestedGnssTime;
    private android.location.LocationListener mLocationListener;
    private final android.location.LocationManager mLocationManager;
    private final android.location.LocationManagerInternal mLocationManagerInternal;
    private final com.android.server.timedetector.TimeDetectorInternal mTimeDetectorInternal;
    private final android.util.LocalLog mLocalLog = new android.util.LocalLog(10, false);
    private final java.util.concurrent.Executor mExecutor = com.android.server.FgThread.getExecutor();
    private final android.os.Handler mHandler = com.android.server.FgThread.getHandler();
    private final java.lang.Object mLock = new java.lang.Object();

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.timedetector.GnssTimeUpdateService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            android.content.Context context = getContext().createAttributionContext("GnssTimeUpdateService");
            android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(android.app.AlarmManager.class);
            android.location.LocationManager locationManager = (android.location.LocationManager) context.getSystemService(android.location.LocationManager.class);
            android.location.LocationManagerInternal locationManagerInternal = (android.location.LocationManagerInternal) com.android.server.LocalServices.getService(android.location.LocationManagerInternal.class);
            com.android.server.timedetector.TimeDetectorInternal timeDetectorInternal = (com.android.server.timedetector.TimeDetectorInternal) com.android.server.LocalServices.getService(com.android.server.timedetector.TimeDetectorInternal.class);
            this.mService = new com.android.server.timedetector.GnssTimeUpdateService(context, alarmManager, locationManager, locationManagerInternal, timeDetectorInternal);
            publishBinderService("gnss_time_update_service", this.mService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 600) {
                this.mService.startGnssListeningInternal();
            }
        }
    }

    GnssTimeUpdateService(android.content.Context context, android.app.AlarmManager alarmManager, android.location.LocationManager locationManager, android.location.LocationManagerInternal locationManagerInternal, com.android.server.timedetector.TimeDetectorInternal timeDetectorInternal) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mAlarmManager = (android.app.AlarmManager) java.util.Objects.requireNonNull(alarmManager);
        this.mLocationManager = (android.location.LocationManager) java.util.Objects.requireNonNull(locationManager);
        this.mLocationManagerInternal = (android.location.LocationManagerInternal) java.util.Objects.requireNonNull(locationManagerInternal);
        this.mTimeDetectorInternal = (com.android.server.timedetector.TimeDetectorInternal) java.util.Objects.requireNonNull(timeDetectorInternal);
    }

    boolean startGnssListening() {
        this.mContext.enforceCallingPermission("android.permission.SET_TIME", "Start GNSS listening");
        this.mLocalLog.log("startGnssListening() called");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return startGnssListeningInternal();
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    boolean startGnssListeningInternal() {
        if (!this.mLocationManager.hasProvider(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS)) {
            logError("GPS provider does not exist on this device");
            return false;
        }
        synchronized (this.mLock) {
            if (this.mLocationListener != null) {
                logDebug("Already listening for GNSS updates");
                return true;
            }
            if (this.mAlarmListener != null) {
                this.mAlarmManager.cancel(this.mAlarmListener);
                this.mAlarmListener = null;
            }
            startGnssListeningLocked();
            return true;
        }
    }

    private void startGnssListeningLocked() {
        logDebug("startGnssListeningLocked()");
        this.mLocationListener = new android.location.LocationListener() { // from class: com.android.server.timedetector.GnssTimeUpdateService$$ExternalSyntheticLambda1
            @Override // android.location.LocationListener
            public final void onLocationChanged(android.location.Location location) {
                this.f$0.lambda$startGnssListeningLocked$0(location);
            }
        };
        this.mLocationManager.requestLocationUpdates(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS, new android.location.LocationRequest.Builder(Long.MAX_VALUE).setMinUpdateIntervalMillis(0L).build(), this.mExecutor, this.mLocationListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startGnssListeningLocked$0(android.location.Location location) {
        handleLocationAvailable();
    }

    private void handleLocationAvailable() {
        logDebug("handleLocationAvailable()");
        android.location.LocationTime locationTime = this.mLocationManagerInternal.getGnssTimeMillis();
        if (locationTime != null) {
            java.lang.String msg = "Passive location time received: " + locationTime;
            logDebug(msg);
            this.mLocalLog.log(msg);
            suggestGnssTime(locationTime);
        } else {
            logDebug("getGnssTimeMillis() returned null");
        }
        synchronized (this.mLock) {
            if (this.mLocationListener == null) {
                logWarning("mLocationListener unexpectedly null");
            } else {
                this.mLocationManager.removeUpdates(this.mLocationListener);
                this.mLocationListener = null;
            }
            if (this.mAlarmListener != null) {
                logWarning("mAlarmListener was unexpectedly non-null");
                this.mAlarmManager.cancel(this.mAlarmListener);
            }
            long next = android.os.SystemClock.elapsedRealtime() + GNSS_TIME_UPDATE_ALARM_INTERVAL.toMillis();
            this.mAlarmListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.timedetector.GnssTimeUpdateService$$ExternalSyntheticLambda0
                @Override // android.app.AlarmManager.OnAlarmListener
                public final void onAlarm() {
                    this.f$0.handleAlarmFired();
                }
            };
            this.mAlarmManager.set(2, next, "GnssTimeUpdateService", this.mAlarmListener, this.mHandler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAlarmFired() {
        logDebug("handleAlarmFired()");
        synchronized (this.mLock) {
            this.mAlarmListener = null;
            startGnssListeningLocked();
        }
    }

    private void suggestGnssTime(android.location.LocationTime locationTime) {
        logDebug("suggestGnssTime()");
        long gnssUnixEpochTimeMillis = locationTime.getUnixEpochTimeMillis();
        long elapsedRealtimeMs = locationTime.getElapsedRealtimeNanos() / 1000000;
        android.app.time.UnixEpochTime unixEpochTime = new android.app.time.UnixEpochTime(elapsedRealtimeMs, gnssUnixEpochTimeMillis);
        this.mLastSuggestedGnssTime = unixEpochTime;
        com.android.server.timedetector.GnssTimeSuggestion suggestion = new com.android.server.timedetector.GnssTimeSuggestion(unixEpochTime);
        this.mTimeDetectorInternal.suggestGnssTime(suggestion);
    }

    @Override // android.os.Binder
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, "GnssTimeUpdateService", pw)) {
            pw.println("mLastSuggestedGnssTime: " + this.mLastSuggestedGnssTime);
            synchronized (this.mLock) {
                pw.print("state: ");
                if (this.mLocationListener != null) {
                    pw.println("time updates enabled");
                } else {
                    pw.println("alarm enabled");
                }
            }
            pw.println("Log:");
            this.mLocalLog.dump(pw);
        }
    }

    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.timedetector.GnssTimeUpdateServiceShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
    }

    private void logError(java.lang.String msg) {
        android.util.Log.e("GnssTimeUpdateService", msg);
        this.mLocalLog.log(msg);
    }

    private void logWarning(java.lang.String msg) {
        android.util.Log.w("GnssTimeUpdateService", msg);
        this.mLocalLog.log(msg);
    }

    private void logDebug(java.lang.String msg) {
        if (D) {
            android.util.Log.d("GnssTimeUpdateService", msg);
        }
    }
}
