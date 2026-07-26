package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class LocationAttributionHelper {
    private final com.android.server.location.injector.AppOpsHelper mAppOpsHelper;
    private final java.util.Map<android.location.util.identity.CallerIdentity, java.lang.Integer> mAttributions = new android.util.ArrayMap();
    private final java.util.Map<android.location.util.identity.CallerIdentity, java.lang.Integer> mHighPowerAttributions = new android.util.ArrayMap();

    public LocationAttributionHelper(com.android.server.location.injector.AppOpsHelper appOpsHelper) {
        this.mAppOpsHelper = appOpsHelper;
    }

    public synchronized void reportLocationStart(android.location.util.identity.CallerIdentity identity) {
        android.location.util.identity.CallerIdentity identity2 = android.location.util.identity.CallerIdentity.forAggregation(identity);
        int count = this.mAttributions.getOrDefault(identity2, 0).intValue();
        if (count == 0) {
            if (this.mAppOpsHelper.startOpNoThrow(41, identity2)) {
                this.mAttributions.put(identity2, 1);
            }
        } else {
            this.mAttributions.put(identity2, java.lang.Integer.valueOf(count + 1));
        }
    }

    public synchronized void reportLocationStop(android.location.util.identity.CallerIdentity identity) {
        android.location.util.identity.CallerIdentity identity2 = android.location.util.identity.CallerIdentity.forAggregation(identity);
        int count = this.mAttributions.getOrDefault(identity2, 0).intValue();
        if (count == 1) {
            this.mAttributions.remove(identity2);
            this.mAppOpsHelper.finishOp(41, identity2);
        } else if (count > 1) {
            this.mAttributions.put(identity2, java.lang.Integer.valueOf(count - 1));
        }
    }

    public synchronized void reportHighPowerLocationStart(android.location.util.identity.CallerIdentity identity) {
        android.location.util.identity.CallerIdentity identity2 = android.location.util.identity.CallerIdentity.forAggregation(identity);
        int count = this.mHighPowerAttributions.getOrDefault(identity2, 0).intValue();
        if (count == 0) {
            if (com.android.server.location.LocationManagerService.D) {
                android.util.Log.v(com.android.server.location.LocationManagerService.TAG, "starting high power location attribution for " + identity2);
            }
            if (this.mAppOpsHelper.startOpNoThrow(42, identity2)) {
                this.mHighPowerAttributions.put(identity2, 1);
            }
        } else {
            this.mHighPowerAttributions.put(identity2, java.lang.Integer.valueOf(count + 1));
        }
    }

    public synchronized void reportHighPowerLocationStop(android.location.util.identity.CallerIdentity identity) {
        android.location.util.identity.CallerIdentity identity2 = android.location.util.identity.CallerIdentity.forAggregation(identity);
        int count = this.mHighPowerAttributions.getOrDefault(identity2, 0).intValue();
        if (count == 1) {
            if (com.android.server.location.LocationManagerService.D) {
                android.util.Log.v(com.android.server.location.LocationManagerService.TAG, "stopping high power location attribution for " + identity2);
            }
            this.mHighPowerAttributions.remove(identity2);
            this.mAppOpsHelper.finishOp(42, identity2);
        } else if (count > 1) {
            this.mHighPowerAttributions.put(identity2, java.lang.Integer.valueOf(count - 1));
        }
    }
}
