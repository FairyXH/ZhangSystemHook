package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class PackageResetHelper {
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.injector.PackageResetHelper.Responder> mResponders = new java.util.concurrent.CopyOnWriteArrayList<>();

    public interface Responder {
        boolean isResetableForPackage(java.lang.String str);

        void onPackageReset(java.lang.String str);
    }

    protected abstract void onRegister();

    protected abstract void onUnregister();

    public synchronized void register(com.android.server.location.injector.PackageResetHelper.Responder responder) {
        boolean empty = this.mResponders.isEmpty();
        this.mResponders.add(responder);
        if (empty) {
            onRegister();
        }
    }

    public synchronized void unregister(com.android.server.location.injector.PackageResetHelper.Responder responder) {
        this.mResponders.remove(responder);
        if (this.mResponders.isEmpty()) {
            onUnregister();
        }
    }

    protected final void notifyPackageReset(java.lang.String packageName) {
        if (com.android.server.location.LocationManagerService.D) {
            android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "package " + packageName + " reset");
        }
        for (com.android.server.location.injector.PackageResetHelper.Responder responder : this.mResponders) {
            responder.onPackageReset(packageName);
        }
    }

    protected final boolean queryResetableForPackage(java.lang.String packageName) {
        for (com.android.server.location.injector.PackageResetHelper.Responder responder : this.mResponders) {
            if (responder.isResetableForPackage(packageName)) {
                return true;
            }
        }
        return false;
    }
}
