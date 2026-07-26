package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class LocationPermissionsHelper {
    private final com.android.server.location.injector.AppOpsHelper mAppOps;
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener> mListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public interface LocationPermissionsListener {
        void onLocationPermissionsChanged(int i);

        void onLocationPermissionsChanged(java.lang.String str);
    }

    protected abstract boolean hasPermission(java.lang.String str, android.location.util.identity.CallerIdentity callerIdentity);

    public LocationPermissionsHelper(com.android.server.location.injector.AppOpsHelper appOps) {
        this.mAppOps = appOps;
        this.mAppOps.addListener(new com.android.server.location.injector.AppOpsHelper.LocationAppOpListener() { // from class: com.android.server.location.injector.LocationPermissionsHelper$$ExternalSyntheticLambda0
            @Override // com.android.server.location.injector.AppOpsHelper.LocationAppOpListener
            public final void onAppOpsChanged(java.lang.String str) {
                this.f$0.onAppOpsChanged(str);
            }
        });
    }

    protected final void notifyLocationPermissionsChanged(java.lang.String packageName) {
        for (com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener listener : this.mListeners) {
            listener.onLocationPermissionsChanged(packageName);
        }
    }

    protected final void notifyLocationPermissionsChanged(int uid) {
        for (com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener listener : this.mListeners) {
            listener.onLocationPermissionsChanged(uid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAppOpsChanged(java.lang.String packageName) {
        notifyLocationPermissionsChanged(packageName);
    }

    public final void addListener(com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener listener) {
        this.mListeners.add(listener);
    }

    public final void removeListener(com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener listener) {
        this.mListeners.remove(listener);
    }

    public final boolean hasLocationPermissions(int permissionLevel, android.location.util.identity.CallerIdentity identity) {
        if (permissionLevel == 0 || !hasPermission(com.android.server.location.LocationPermissions.asPermission(permissionLevel), identity)) {
            return false;
        }
        return this.mAppOps.checkOpNoThrow(com.android.server.location.LocationPermissions.asAppOp(permissionLevel), identity);
    }
}
