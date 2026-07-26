package com.android.server.appbinding.finders;

/* JADX INFO: loaded from: classes.dex */
public abstract class AppServiceFinder<TServiceType, TServiceInterfaceType extends android.os.IInterface> {
    protected static final boolean DEBUG = false;
    protected static final java.lang.String TAG = "AppBindingService";
    protected final android.content.Context mContext;
    protected final android.os.Handler mHandler;
    protected final java.util.function.BiConsumer<com.android.server.appbinding.finders.AppServiceFinder, java.lang.Integer> mListener;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<java.lang.String> mTargetPackages = new android.util.SparseArray<>(4);
    private final android.util.SparseArray<android.content.pm.ServiceInfo> mTargetServices = new android.util.SparseArray<>(4);
    private final android.util.SparseArray<java.lang.String> mLastMessages = new android.util.SparseArray<>(4);

    public abstract TServiceInterfaceType asInterface(android.os.IBinder iBinder);

    public abstract java.lang.String getAppDescription();

    public abstract int getBindFlags(com.android.server.appbinding.AppBindingConstants appBindingConstants);

    protected abstract java.lang.String getServiceAction();

    protected abstract java.lang.Class<TServiceType> getServiceClass();

    protected abstract java.lang.String getServicePermission();

    public abstract java.lang.String getTargetPackage(int i);

    public AppServiceFinder(android.content.Context context, java.util.function.BiConsumer<com.android.server.appbinding.finders.AppServiceFinder, java.lang.Integer> listener, android.os.Handler callbackHandler) {
        this.mContext = context;
        this.mListener = listener;
        this.mHandler = callbackHandler;
    }

    protected boolean isEnabled(com.android.server.appbinding.AppBindingConstants constants) {
        return true;
    }

    public void startMonitoring() {
    }

    public void onUserRemoved(int userId) {
        synchronized (this.mLock) {
            this.mTargetPackages.delete(userId);
            this.mTargetServices.delete(userId);
            this.mLastMessages.delete(userId);
        }
    }

    public final android.content.pm.ServiceInfo findService(int userId, android.content.pm.IPackageManager ipm, com.android.server.appbinding.AppBindingConstants constants) {
        synchronized (this.mLock) {
            this.mTargetPackages.put(userId, null);
            this.mTargetServices.put(userId, null);
            this.mLastMessages.put(userId, null);
            if (!isEnabled(constants)) {
                this.mLastMessages.put(userId, "feature disabled");
                android.util.Slog.i("AppBindingService", getAppDescription() + " feature disabled");
                return null;
            }
            java.lang.String targetPackage = getTargetPackage(userId);
            if (targetPackage == null) {
                this.mLastMessages.put(userId, "Target package not found");
                android.util.Slog.w("AppBindingService", getAppDescription() + " u" + userId + " Target package not found");
                return null;
            }
            this.mTargetPackages.put(userId, targetPackage);
            java.lang.StringBuilder errorMessage = new java.lang.StringBuilder();
            android.content.pm.ServiceInfo service = com.android.server.appbinding.AppBindingUtils.findService(targetPackage, userId, getServiceAction(), getServicePermission(), getServiceClass(), ipm, errorMessage);
            if (service == null) {
                java.lang.String message = errorMessage.toString();
                this.mLastMessages.put(userId, message);
                return null;
            }
            java.lang.String error = validateService(service);
            if (error != null) {
                this.mLastMessages.put(userId, error);
                android.util.Log.e("AppBindingService", error);
                return null;
            }
            this.mLastMessages.put(userId, "Valid service found");
            this.mTargetServices.put(userId, service);
            return service;
        }
    }

    protected java.lang.String validateService(android.content.pm.ServiceInfo service) {
        return null;
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("App type: ");
        pw.print(getAppDescription());
        pw.println();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mTargetPackages.size(); i++) {
                int userId = this.mTargetPackages.keyAt(i);
                pw.print(prefix);
                pw.print("  User: ");
                pw.print(userId);
                pw.println();
                pw.print(prefix);
                pw.print("    Package: ");
                pw.print(this.mTargetPackages.get(userId));
                pw.println();
                pw.print(prefix);
                pw.print("    Service: ");
                pw.print(this.mTargetServices.get(userId));
                pw.println();
                pw.print(prefix);
                pw.print("    Message: ");
                pw.print(this.mLastMessages.get(userId));
                pw.println();
            }
        }
    }

    public void dumpSimple(java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mTargetPackages.size(); i++) {
                int userId = this.mTargetPackages.keyAt(i);
                pw.print("finder,");
                pw.print(getAppDescription());
                pw.print(",");
                pw.print(userId);
                pw.print(",");
                pw.print(this.mTargetPackages.get(userId));
                pw.print(",");
                pw.print(this.mTargetServices.get(userId));
                pw.print(",");
                pw.print(this.mLastMessages.get(userId));
                pw.println();
            }
        }
    }
}
