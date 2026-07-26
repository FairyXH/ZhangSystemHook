package com.android.server.location.fudger;

/* JADX INFO: loaded from: classes2.dex */
public class LocationFudger {
    private static final int APPROXIMATE_METERS_PER_DEGREE_AT_EQUATOR = 111000;
    private static final double CHANGE_PER_INTERVAL = 0.03d;
    private static final double MAX_LATITUDE = 89.999990990991d;
    private static final float MIN_ACCURACY_M = 200.0f;
    private static final double NEW_WEIGHT = 0.03d;
    static final long OFFSET_UPDATE_INTERVAL_MS = 3600000;
    private static final double OLD_WEIGHT = java.lang.Math.sqrt(0.9991d);
    private final float mAccuracyM;
    private android.location.Location mCachedCoarseLocation;
    private android.location.LocationResult mCachedCoarseLocationResult;
    private android.location.Location mCachedFineLocation;
    private android.location.LocationResult mCachedFineLocationResult;
    private final java.time.Clock mClock;
    private double mLatitudeOffsetM;
    private double mLongitudeOffsetM;
    private long mNextUpdateRealtimeMs;
    private final java.util.Random mRandom;

    public LocationFudger(float accuracyM) {
        this(accuracyM, android.os.SystemClock.elapsedRealtimeClock(), new java.security.SecureRandom());
    }

    LocationFudger(float accuracyM, java.time.Clock clock, java.util.Random random) {
        this.mClock = clock;
        this.mRandom = random;
        this.mAccuracyM = java.lang.Math.max(accuracyM, MIN_ACCURACY_M);
        resetOffsets();
    }

    public void resetOffsets() {
        this.mLatitudeOffsetM = nextRandomOffset();
        this.mLongitudeOffsetM = nextRandomOffset();
        this.mNextUpdateRealtimeMs = this.mClock.millis() + 3600000;
    }

    public android.location.LocationResult createCoarse(android.location.LocationResult fineLocationResult) {
        synchronized (this) {
            if (fineLocationResult != this.mCachedFineLocationResult && fineLocationResult != this.mCachedCoarseLocationResult) {
                android.location.LocationResult coarseLocationResult = fineLocationResult.map(new java.util.function.Function() { // from class: com.android.server.location.fudger.LocationFudger$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return this.f$0.createCoarse((android.location.Location) obj);
                    }
                });
                synchronized (this) {
                    this.mCachedFineLocationResult = fineLocationResult;
                    this.mCachedCoarseLocationResult = coarseLocationResult;
                }
                return coarseLocationResult;
            }
            return this.mCachedCoarseLocationResult;
        }
    }

    public android.location.Location createCoarse(android.location.Location fine) {
        synchronized (this) {
            if (fine != this.mCachedFineLocation && fine != this.mCachedCoarseLocation) {
                updateOffsets();
                android.location.Location coarse = new android.location.Location(fine);
                coarse.removeBearing();
                coarse.removeSpeed();
                coarse.removeAltitude();
                coarse.setExtras(null);
                double latitude = wrapLatitude(coarse.getLatitude());
                double longitude = wrapLongitude(coarse.getLongitude());
                double longitude2 = longitude + wrapLongitude(metersToDegreesLongitude(this.mLongitudeOffsetM, latitude));
                double latitude2 = latitude + wrapLatitude(metersToDegreesLatitude(this.mLatitudeOffsetM));
                double latGranularity = metersToDegreesLatitude(this.mAccuracyM);
                double latitude3 = wrapLatitude(java.lang.Math.round(latitude2 / latGranularity) * latGranularity);
                double lonGranularity = metersToDegreesLongitude(this.mAccuracyM, latitude3);
                double longitude3 = wrapLongitude(java.lang.Math.round(longitude2 / lonGranularity) * lonGranularity);
                coarse.setLatitude(latitude3);
                coarse.setLongitude(longitude3);
                coarse.setAccuracy(java.lang.Math.max(this.mAccuracyM, coarse.getAccuracy()));
                synchronized (this) {
                    this.mCachedFineLocation = fine;
                    this.mCachedCoarseLocation = coarse;
                }
                return coarse;
            }
            return this.mCachedCoarseLocation;
        }
    }

    private synchronized void updateOffsets() {
        long now = this.mClock.millis();
        if (now < this.mNextUpdateRealtimeMs) {
            return;
        }
        this.mLatitudeOffsetM = (OLD_WEIGHT * this.mLatitudeOffsetM) + (nextRandomOffset() * 0.03d);
        this.mLongitudeOffsetM = (OLD_WEIGHT * this.mLongitudeOffsetM) + (nextRandomOffset() * 0.03d);
        this.mNextUpdateRealtimeMs = 3600000 + now;
    }

    private double nextRandomOffset() {
        return this.mRandom.nextGaussian() * (((double) this.mAccuracyM) / 4.0d);
    }

    private static double wrapLatitude(double lat) {
        if (lat > MAX_LATITUDE) {
            lat = MAX_LATITUDE;
        }
        if (lat < -89.999990990991d) {
            return -89.999990990991d;
        }
        return lat;
    }

    private static double wrapLongitude(double lon) {
        double lon2 = lon % 360.0d;
        if (lon2 >= 180.0d) {
            lon2 -= 360.0d;
        }
        if (lon2 < -180.0d) {
            return lon2 + 360.0d;
        }
        return lon2;
    }

    private static double metersToDegreesLatitude(double distance) {
        return distance / 111000.0d;
    }

    private static double metersToDegreesLongitude(double distance, double lat) {
        return (distance / 111000.0d) / java.lang.Math.cos(java.lang.Math.toRadians(lat));
    }
}
