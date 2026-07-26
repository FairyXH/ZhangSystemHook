package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class LocationUsageLogger {
    private static final int API_USAGE_LOG_HOURLY_CAP = 60;
    private static final int ONE_HOUR_IN_MILLIS = 3600000;
    private static final int ONE_MINUTE_IN_MILLIS = 60000;
    private static final int ONE_SEC_IN_MILLIS = 1000;
    private long mLastApiUsageLogHour = 0;
    private int mApiUsageLogHourlyCount = 0;

    public void logLocationApiUsage(int usageType, int apiInUse, java.lang.String packageName, java.lang.String attributionTag, java.lang.String provider, android.location.LocationRequest locationRequest, boolean hasListener, boolean hasIntent, android.location.Geofence geofence, boolean foreground) {
        int iBucketizeProvider;
        int quality;
        int iBucketizeInterval;
        int iBucketizeDistance;
        int iBucketizeExpireIn;
        int iBucketizeRadius;
        try {
            if (hitApiUsageLogCap()) {
                return;
            }
            boolean isLocationRequestNull = locationRequest == null;
            boolean isGeofenceNull = geofence == null;
            if (isLocationRequestNull) {
                iBucketizeProvider = 0;
            } else {
                iBucketizeProvider = bucketizeProvider(provider);
            }
            if (isLocationRequestNull) {
                quality = 0;
            } else {
                quality = locationRequest.getQuality();
            }
            if (isLocationRequestNull) {
                iBucketizeInterval = 0;
            } else {
                iBucketizeInterval = bucketizeInterval(locationRequest.getIntervalMillis());
            }
            if (isLocationRequestNull) {
                iBucketizeDistance = 0;
            } else {
                iBucketizeDistance = bucketizeDistance(locationRequest.getMinUpdateDistanceMeters());
            }
            long maxUpdates = isLocationRequestNull ? 0L : locationRequest.getMaxUpdates();
            if (isLocationRequestNull || usageType == 1) {
                iBucketizeExpireIn = 0;
            } else {
                try {
                    iBucketizeExpireIn = bucketizeExpireIn(locationRequest.getDurationMillis());
                } catch (java.lang.Exception e) {
                    e = e;
                }
            }
            int callbackType = getCallbackType(apiInUse, hasListener, hasIntent);
            if (isGeofenceNull) {
                iBucketizeRadius = 0;
            } else {
                iBucketizeRadius = bucketizeRadius(geofence.getRadius());
            }
            com.android.internal.util.FrameworkStatsLog.write(210, usageType, apiInUse, packageName, iBucketizeProvider, quality, iBucketizeInterval, iBucketizeDistance, maxUpdates, iBucketizeExpireIn, callbackType, iBucketizeRadius, categorizeActivityImportance(foreground), attributionTag);
            return;
        } catch (java.lang.Exception e2) {
            e = e2;
        }
        android.util.Log.w(com.android.server.location.LocationManagerService.TAG, "Failed to log API usage to statsd.", e);
    }

    public void logLocationApiUsage(int usageType, int apiInUse, java.lang.String providerName) {
        try {
            if (hitApiUsageLogCap()) {
                return;
            }
            com.android.internal.util.FrameworkStatsLog.write(210, usageType, apiInUse, (java.lang.String) null, bucketizeProvider(providerName), 0, 0, 0, 0L, 0, getCallbackType(apiInUse, true, true), 0, 0, (java.lang.String) null);
        } catch (java.lang.Exception e) {
            android.util.Log.w(com.android.server.location.LocationManagerService.TAG, "Failed to log API usage to statsd.", e);
        }
    }

    public synchronized void logLocationEnabledStateChanged(boolean enabled) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.LOCATION_ENABLED_STATE_CHANGED, enabled);
    }

    public synchronized void logEmergencyStateChanged(boolean isInEmergency) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.EMERGENCY_STATE_CHANGED, isInEmergency);
    }

    private static int bucketizeProvider(java.lang.String provider) {
        if ("network".equals(provider)) {
            return 1;
        }
        if (com.android.server.am.IOplusSceneManager.APP_SCENE_GPS.equals(provider)) {
            return 2;
        }
        if ("passive".equals(provider)) {
            return 3;
        }
        if ("fused".equals(provider)) {
            return 4;
        }
        return 0;
    }

    private static int bucketizeInterval(long interval) {
        if (interval < 1000) {
            return 1;
        }
        if (interval < 5000) {
            return 2;
        }
        if (interval < 60000) {
            return 3;
        }
        if (interval < 600000) {
            return 4;
        }
        if (interval < 3600000) {
            return 5;
        }
        return 6;
    }

    private static int bucketizeDistance(float smallestDisplacement) {
        if (smallestDisplacement <= 0.0f) {
            return 1;
        }
        if (smallestDisplacement > 0.0f && smallestDisplacement <= 100.0f) {
            return 2;
        }
        return 3;
    }

    private static int bucketizeRadius(float radius) {
        if (radius < 0.0f) {
            return 7;
        }
        if (radius < 100.0f) {
            return 1;
        }
        if (radius < 200.0f) {
            return 2;
        }
        if (radius < 300.0f) {
            return 3;
        }
        if (radius < 1000.0f) {
            return 4;
        }
        if (radius < 10000.0f) {
            return 5;
        }
        return 6;
    }

    private static int bucketizeExpireIn(long expireIn) {
        if (expireIn == Long.MAX_VALUE) {
            return 6;
        }
        if (expireIn < 20000) {
            return 1;
        }
        if (expireIn < 60000) {
            return 2;
        }
        if (expireIn < 600000) {
            return 3;
        }
        if (expireIn < 3600000) {
            return 4;
        }
        return 5;
    }

    private static int categorizeActivityImportance(boolean foreground) {
        if (foreground) {
            return 1;
        }
        return 3;
    }

    private static int getCallbackType(int apiType, boolean hasListener, boolean hasIntent) {
        if (apiType == 5) {
            return 1;
        }
        if (hasIntent) {
            return 3;
        }
        if (hasListener) {
            return 2;
        }
        return 0;
    }

    private synchronized boolean hitApiUsageLogCap() {
        long currentHour = java.time.Instant.now().toEpochMilli() / 3600000;
        if (currentHour > this.mLastApiUsageLogHour) {
            this.mLastApiUsageLogHour = currentHour;
            this.mApiUsageLogHourlyCount = 0;
            return false;
        }
        this.mApiUsageLogHourlyCount = java.lang.Math.min(this.mApiUsageLogHourlyCount + 1, 60);
        return this.mApiUsageLogHourlyCount >= 60;
    }
}
