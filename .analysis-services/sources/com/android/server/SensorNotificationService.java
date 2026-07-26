package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class SensorNotificationService extends com.android.server.SystemService implements android.hardware.SensorEventListener, android.location.LocationListener {
    private static final java.lang.String ATTRIBUTION_TAG = "SensorNotificationService";
    private static final boolean DBG = false;
    private static final long KM_IN_M = 1000;
    private static final long LOCATION_MIN_DISTANCE = 100000;
    private static final long LOCATION_MIN_TIME = 1800000;
    private static final long MILLIS_2010_1_1 = 1262358000000L;
    private static final long MINUTE_IN_MS = 60000;
    private static final java.lang.String PROPERTY_USE_MOCKED_LOCATION = "sensor.notification.use_mocked";
    private static final java.lang.String TAG = "SensorNotificationService";
    private android.content.Context mContext;
    private long mLocalGeomagneticFieldUpdateTime;
    private android.location.LocationManager mLocationManager;
    private android.hardware.Sensor mMetaSensor;
    private android.hardware.SensorManager mSensorManager;

    public SensorNotificationService(android.content.Context context) {
        super(context.createAttributionContext("SensorNotificationService"));
        this.mLocalGeomagneticFieldUpdateTime = -1800000L;
        this.mContext = getContext();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        com.android.server.LocalServices.addService(com.android.server.SensorNotificationService.class, this);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 600) {
            this.mSensorManager = (android.hardware.SensorManager) this.mContext.getSystemService(com.android.server.am.IOplusSceneManager.APP_SCENE_SENSOR);
            this.mMetaSensor = this.mSensorManager.getDefaultSensor(32);
            if (this.mMetaSensor != null) {
                this.mSensorManager.registerListener(this, this.mMetaSensor, 0);
            }
        }
        if (phase == 1000) {
            this.mLocationManager = (android.location.LocationManager) this.mContext.getSystemService("location");
            if (this.mLocationManager != null) {
                this.mLocationManager.requestLocationUpdates("passive", 1800000L, 100000.0f, this);
            }
        }
    }

    private void broadcastDynamicSensorChanged() {
        android.content.Intent i = new android.content.Intent("android.intent.action.DYNAMIC_SENSOR_CHANGED");
        i.setFlags(1073741824);
        this.mContext.sendBroadcastAsUser(i, android.os.UserHandle.ALL);
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(android.hardware.SensorEvent event) {
        if (event.sensor == this.mMetaSensor) {
            broadcastDynamicSensorChanged();
        }
    }

    @Override // android.location.LocationListener
    public void onLocationChanged(android.location.Location location) {
        if ((location.getLatitude() == 0.0d && location.getLongitude() == 0.0d) || android.os.SystemClock.elapsedRealtime() - this.mLocalGeomagneticFieldUpdateTime < 600000) {
            return;
        }
        long time = java.lang.System.currentTimeMillis();
        if (useMockedLocation() == location.isMock() || time < MILLIS_2010_1_1) {
            return;
        }
        android.hardware.GeomagneticField field = new android.hardware.GeomagneticField((float) location.getLatitude(), (float) location.getLongitude(), (float) location.getAltitude(), time);
        try {
            android.hardware.SensorAdditionalInfo info = android.hardware.SensorAdditionalInfo.createLocalGeomagneticField(field.getFieldStrength() / 1000.0f, (float) ((((double) field.getDeclination()) * 3.141592653589793d) / 180.0d), (float) ((((double) field.getInclination()) * 3.141592653589793d) / 180.0d));
            if (info != null) {
                this.mSensorManager.setOperationParameter(info);
                this.mLocalGeomagneticFieldUpdateTime = android.os.SystemClock.elapsedRealtime();
            }
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e("SensorNotificationService", "Invalid local geomagnetic field, ignore.");
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    }

    @Override // android.location.LocationListener
    public void onStatusChanged(java.lang.String provider, int status, android.os.Bundle extras) {
    }

    @Override // android.location.LocationListener
    public void onProviderEnabled(java.lang.String provider) {
    }

    @Override // android.location.LocationListener
    public void onProviderDisabled(java.lang.String provider) {
    }

    private boolean useMockedLocation() {
        return "false".equals(java.lang.System.getProperty(PROPERTY_USE_MOCKED_LOCATION, "false"));
    }
}
