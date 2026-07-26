package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
class GnssGeofenceProxy extends android.location.IGpsGeofenceHardware.Stub implements com.android.server.location.gnss.hal.GnssNative.BaseCallbacks {
    private final com.android.server.location.gnss.hal.GnssNative mGnssNative;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<com.android.server.location.gnss.GnssGeofenceProxy.GeofenceEntry> mGeofenceEntries = new android.util.SparseArray<>();

    private static class GeofenceEntry {
        public int geofenceId;
        public int lastTransition;
        public double latitude;
        public double longitude;
        public int monitorTransitions;
        public int notificationResponsiveness;
        public boolean paused;
        public double radius;
        public int unknownTimer;

        private GeofenceEntry() {
        }
    }

    GnssGeofenceProxy(com.android.server.location.gnss.hal.GnssNative gnssNative) {
        this.mGnssNative = gnssNative;
        this.mGnssNative.addBaseCallbacks(this);
    }

    public boolean isHardwareGeofenceSupported() {
        boolean zIsGeofencingSupported;
        synchronized (this.mLock) {
            zIsGeofencingSupported = this.mGnssNative.isGeofencingSupported();
        }
        return zIsGeofencingSupported;
    }

    public boolean addCircularHardwareGeofence(int geofenceId, double latitude, double longitude, double radius, int lastTransition, int monitorTransitions, int notificationResponsiveness, int unknownTimer) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    boolean added = this.mGnssNative.addGeofence(geofenceId, latitude, longitude, radius, lastTransition, monitorTransitions, notificationResponsiveness, unknownTimer);
                    if (added) {
                        com.android.server.location.gnss.GnssGeofenceProxy.GeofenceEntry entry = new com.android.server.location.gnss.GnssGeofenceProxy.GeofenceEntry();
                        entry.geofenceId = geofenceId;
                        try {
                            entry.latitude = latitude;
                            try {
                                entry.longitude = longitude;
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            throw th;
                        }
                        try {
                            entry.radius = radius;
                            try {
                                entry.lastTransition = lastTransition;
                                try {
                                    entry.monitorTransitions = monitorTransitions;
                                    try {
                                        entry.notificationResponsiveness = notificationResponsiveness;
                                        entry.unknownTimer = unknownTimer;
                                        this.mGeofenceEntries.put(geofenceId, entry);
                                    } catch (java.lang.Throwable th3) {
                                        th = th3;
                                        throw th;
                                    }
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                    throw th;
                                }
                            } catch (java.lang.Throwable th5) {
                                th = th5;
                                throw th;
                            }
                        } catch (java.lang.Throwable th6) {
                            th = th6;
                            throw th;
                        }
                    }
                    return added;
                } catch (java.lang.Throwable th7) {
                    th = th7;
                }
            } catch (java.lang.Throwable th8) {
                th = th8;
            }
        }
    }

    public boolean removeHardwareGeofence(int geofenceId) {
        boolean removed;
        synchronized (this.mLock) {
            removed = this.mGnssNative.removeGeofence(geofenceId);
            if (removed) {
                this.mGeofenceEntries.remove(geofenceId);
            }
        }
        return removed;
    }

    public boolean pauseHardwareGeofence(int geofenceId) {
        boolean paused;
        com.android.server.location.gnss.GnssGeofenceProxy.GeofenceEntry entry;
        synchronized (this.mLock) {
            paused = this.mGnssNative.pauseGeofence(geofenceId);
            if (paused && (entry = this.mGeofenceEntries.get(geofenceId)) != null) {
                entry.paused = true;
            }
        }
        return paused;
    }

    public boolean resumeHardwareGeofence(int geofenceId, int monitorTransitions) {
        boolean resumed;
        com.android.server.location.gnss.GnssGeofenceProxy.GeofenceEntry entry;
        synchronized (this.mLock) {
            resumed = this.mGnssNative.resumeGeofence(geofenceId, monitorTransitions);
            if (resumed && (entry = this.mGeofenceEntries.get(geofenceId)) != null) {
                entry.paused = false;
                entry.monitorTransitions = monitorTransitions;
            }
        }
        return resumed;
    }

    @Override // com.android.server.location.gnss.hal.GnssNative.BaseCallbacks
    public void onHalRestarted() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mGeofenceEntries.size(); i++) {
                com.android.server.location.gnss.GnssGeofenceProxy.GeofenceEntry entry = this.mGeofenceEntries.valueAt(i);
                boolean added = this.mGnssNative.addGeofence(entry.geofenceId, entry.latitude, entry.longitude, entry.radius, entry.lastTransition, entry.monitorTransitions, entry.notificationResponsiveness, entry.unknownTimer);
                if (added && entry.paused) {
                    this.mGnssNative.pauseGeofence(entry.geofenceId);
                }
            }
        }
    }
}
