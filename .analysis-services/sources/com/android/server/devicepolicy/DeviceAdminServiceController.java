package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public class DeviceAdminServiceController {
    static final boolean DEBUG = false;
    static final java.lang.String TAG = "DevicePolicyManager";
    private final com.android.server.devicepolicy.DevicePolicyConstants mConstants;
    final android.content.Context mContext;
    private final com.android.server.devicepolicy.DevicePolicyManagerService.Injector mInjector;
    final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<java.util.Map<java.lang.String, com.android.server.devicepolicy.DeviceAdminServiceController.DevicePolicyServiceConnection>> mConnections = new android.util.SparseArray<>();
    private final android.os.Handler mHandler = new android.os.Handler(com.android.internal.os.BackgroundThread.get().getLooper());

    private class DevicePolicyServiceConnection extends com.android.server.am.PersistentConnection<android.app.admin.IDeviceAdminService> {
        public DevicePolicyServiceConnection(int userId, android.content.ComponentName componentName) {
            super(com.android.server.devicepolicy.DeviceAdminServiceController.TAG, com.android.server.devicepolicy.DeviceAdminServiceController.this.mContext, com.android.server.devicepolicy.DeviceAdminServiceController.this.mHandler, userId, componentName, com.android.server.devicepolicy.DeviceAdminServiceController.this.mConstants.DAS_DIED_SERVICE_RECONNECT_BACKOFF_SEC, com.android.server.devicepolicy.DeviceAdminServiceController.this.mConstants.DAS_DIED_SERVICE_RECONNECT_BACKOFF_INCREASE, com.android.server.devicepolicy.DeviceAdminServiceController.this.mConstants.DAS_DIED_SERVICE_RECONNECT_MAX_BACKOFF_SEC, com.android.server.devicepolicy.DeviceAdminServiceController.this.mConstants.DAS_DIED_SERVICE_STABLE_CONNECTION_THRESHOLD_SEC);
        }

        @Override // com.android.server.am.PersistentConnection
        protected int getBindFlags() {
            return 67108864;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.android.server.am.PersistentConnection
        public android.app.admin.IDeviceAdminService asInterface(android.os.IBinder binder) {
            return android.app.admin.IDeviceAdminService.Stub.asInterface(binder);
        }
    }

    public DeviceAdminServiceController(com.android.server.devicepolicy.DevicePolicyManagerService service, com.android.server.devicepolicy.DevicePolicyConstants constants) {
        this.mInjector = service.mInjector;
        this.mContext = this.mInjector.mContext;
        this.mConstants = constants;
    }

    private android.content.pm.ServiceInfo findService(java.lang.String packageName, int userId) {
        return com.android.server.appbinding.AppBindingUtils.findService(packageName, userId, "android.app.action.DEVICE_ADMIN_SERVICE", "android.permission.BIND_DEVICE_ADMIN", android.app.admin.DeviceAdminService.class, this.mInjector.getIPackageManager(), new java.lang.StringBuilder());
    }

    public void startServiceForAdmin(java.lang.String packageName, int userId, java.lang.String actionForLog) {
        long token = this.mInjector.binderClearCallingIdentity();
        try {
            synchronized (this.mLock) {
                android.content.pm.ServiceInfo service = findService(packageName, userId);
                if (service == null) {
                    disconnectServiceOnUserLocked(packageName, userId, actionForLog);
                    return;
                }
                com.android.server.am.PersistentConnection<android.app.admin.IDeviceAdminService> existing = this.mConnections.contains(userId) ? this.mConnections.get(userId).get(packageName) : null;
                if (existing != null) {
                    disconnectServiceOnUserLocked(packageName, userId, actionForLog);
                }
                com.android.server.devicepolicy.DeviceAdminServiceController.DevicePolicyServiceConnection conn = new com.android.server.devicepolicy.DeviceAdminServiceController.DevicePolicyServiceConnection(userId, service.getComponentName());
                if (!this.mConnections.contains(userId)) {
                    this.mConnections.put(userId, new java.util.HashMap());
                }
                this.mConnections.get(userId).put(packageName, conn);
                conn.bind();
            }
        } finally {
            this.mInjector.binderRestoreCallingIdentity(token);
        }
    }

    public void stopServiceForAdmin(java.lang.String packageName, int userId, java.lang.String actionForLog) {
        long token = this.mInjector.binderClearCallingIdentity();
        try {
            synchronized (this.mLock) {
                disconnectServiceOnUserLocked(packageName, userId, actionForLog);
            }
        } finally {
            this.mInjector.binderRestoreCallingIdentity(token);
        }
    }

    public void stopServicesForUser(int userId, java.lang.String actionForLog) {
        long token = this.mInjector.binderClearCallingIdentity();
        try {
            synchronized (this.mLock) {
                disconnectServiceOnUserLocked(userId, actionForLog);
            }
        } finally {
            this.mInjector.binderRestoreCallingIdentity(token);
        }
    }

    private void disconnectServiceOnUserLocked(java.lang.String packageName, int userId, java.lang.String actionForLog) {
        com.android.server.devicepolicy.DeviceAdminServiceController.DevicePolicyServiceConnection conn = this.mConnections.contains(userId) ? this.mConnections.get(userId).get(packageName) : null;
        if (conn != null) {
            conn.unbind();
            this.mConnections.get(userId).remove(packageName);
            if (this.mConnections.get(userId).isEmpty()) {
                this.mConnections.remove(userId);
            }
        }
    }

    private void disconnectServiceOnUserLocked(int userId, java.lang.String actionForLog) {
        if (!this.mConnections.contains(userId)) {
            return;
        }
        for (java.lang.String packageName : this.mConnections.get(userId).keySet()) {
            com.android.server.devicepolicy.DeviceAdminServiceController.DevicePolicyServiceConnection conn = this.mConnections.get(userId).get(packageName);
            conn.unbind();
        }
        this.mConnections.remove(userId);
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            if (this.mConnections.size() == 0) {
                return;
            }
            pw.println("Admin Services:");
            pw.increaseIndent();
            for (int i = 0; i < this.mConnections.size(); i++) {
                int userId = this.mConnections.keyAt(i);
                pw.print("User: ");
                pw.println(userId);
                for (java.lang.String packageName : this.mConnections.get(userId).keySet()) {
                    pw.increaseIndent();
                    pw.print("Package: ");
                    pw.println(packageName);
                    com.android.server.devicepolicy.DeviceAdminServiceController.DevicePolicyServiceConnection con = this.mConnections.valueAt(i).get(packageName);
                    pw.increaseIndent();
                    con.dump("", pw);
                    pw.decreaseIndent();
                    pw.decreaseIndent();
                }
            }
            pw.decreaseIndent();
        }
    }
}
