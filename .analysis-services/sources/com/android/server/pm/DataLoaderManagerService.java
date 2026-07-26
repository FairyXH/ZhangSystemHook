package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class DataLoaderManagerService extends com.android.server.SystemService {
    private static final java.lang.String TAG = "DataLoaderManager";
    private final com.android.server.pm.DataLoaderManagerService.DataLoaderManagerBinderService mBinderService;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final android.util.SparseArray<com.android.server.pm.DataLoaderManagerService.DataLoaderServiceConnection> mServiceConnections;

    public DataLoaderManagerService(android.content.Context context) {
        super(context);
        this.mServiceConnections = new android.util.SparseArray<>();
        this.mContext = context;
        android.os.HandlerThread thread = new android.os.HandlerThread(TAG);
        thread.start();
        this.mHandler = new android.os.Handler(thread.getLooper());
        this.mBinderService = new com.android.server.pm.DataLoaderManagerService.DataLoaderManagerBinderService();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("dataloader_manager", this.mBinderService);
    }

    final class DataLoaderManagerBinderService extends android.content.pm.IDataLoaderManager.Stub {
        DataLoaderManagerBinderService() {
        }

        public boolean bindToDataLoader(final int dataLoaderId, android.content.pm.DataLoaderParamsParcel params, long bindDelayMs, android.content.pm.IDataLoaderStatusListener listener) throws java.lang.Throwable {
            synchronized (com.android.server.pm.DataLoaderManagerService.this.mServiceConnections) {
                try {
                    if (com.android.server.pm.DataLoaderManagerService.this.mServiceConnections.get(dataLoaderId) != null) {
                        return true;
                    }
                    android.content.ComponentName componentName = new android.content.ComponentName(params.packageName, params.className);
                    final android.content.ComponentName dataLoaderComponent = resolveDataLoaderComponentName(componentName);
                    if (dataLoaderComponent == null) {
                        android.util.Slog.e(com.android.server.pm.DataLoaderManagerService.TAG, "Invalid component: " + componentName + " for ID=" + dataLoaderId);
                        return false;
                    }
                    final com.android.server.pm.DataLoaderManagerService.DataLoaderServiceConnection connection = com.android.server.pm.DataLoaderManagerService.this.new DataLoaderServiceConnection(dataLoaderId, listener);
                    final android.content.Intent intent = new android.content.Intent();
                    intent.setComponent(dataLoaderComponent);
                    return com.android.server.pm.DataLoaderManagerService.this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.pm.DataLoaderManagerService$DataLoaderManagerBinderService$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$bindToDataLoader$0(intent, connection, dataLoaderComponent, dataLoaderId);
                        }
                    }, bindDelayMs);
                } catch (java.lang.Throwable th) {
                    th = th;
                    while (true) {
                        try {
                            throw th;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$bindToDataLoader$0(android.content.Intent intent, com.android.server.pm.DataLoaderManagerService.DataLoaderServiceConnection connection, android.content.ComponentName dataLoaderComponent, int dataLoaderId) {
            if (!com.android.server.pm.DataLoaderManagerService.this.mContext.bindServiceAsUser(intent, connection, 1, com.android.server.pm.DataLoaderManagerService.this.mHandler, android.os.UserHandle.of(android.os.UserHandle.getCallingUserId()))) {
                android.util.Slog.e(com.android.server.pm.DataLoaderManagerService.TAG, "Failed to bind to: " + dataLoaderComponent + " for ID=" + dataLoaderId);
                com.android.server.pm.DataLoaderManagerService.this.mContext.unbindService(connection);
            }
        }

        private android.content.ComponentName resolveDataLoaderComponentName(android.content.ComponentName componentName) {
            android.content.pm.PackageManager pm = com.android.server.pm.DataLoaderManagerService.this.mContext.getPackageManager();
            if (pm == null) {
                android.util.Slog.e(com.android.server.pm.DataLoaderManagerService.TAG, "PackageManager is not available.");
                return null;
            }
            android.content.Intent intent = new android.content.Intent("android.intent.action.LOAD_DATA");
            intent.setComponent(componentName);
            java.util.List<android.content.pm.ResolveInfo> services = pm.queryIntentServicesAsUser(intent, 0, android.os.UserHandle.getCallingUserId());
            if (services == null || services.isEmpty()) {
                android.util.Slog.e(com.android.server.pm.DataLoaderManagerService.TAG, "Failed to find data loader service provider in " + componentName);
                return null;
            }
            int numServices = services.size();
            if (0 < numServices) {
                android.content.pm.ResolveInfo ri = services.get(0);
                android.content.ComponentName resolved = new android.content.ComponentName(ri.serviceInfo.packageName, ri.serviceInfo.name);
                return resolved;
            }
            android.util.Slog.e(com.android.server.pm.DataLoaderManagerService.TAG, "Didn't find any matching data loader service provider.");
            return null;
        }

        public android.content.pm.IDataLoader getDataLoader(int dataLoaderId) {
            synchronized (com.android.server.pm.DataLoaderManagerService.this.mServiceConnections) {
                com.android.server.pm.DataLoaderManagerService.DataLoaderServiceConnection serviceConnection = (com.android.server.pm.DataLoaderManagerService.DataLoaderServiceConnection) com.android.server.pm.DataLoaderManagerService.this.mServiceConnections.get(dataLoaderId, null);
                if (serviceConnection == null) {
                    return null;
                }
                return serviceConnection.getDataLoader();
            }
        }

        public void unbindFromDataLoader(int dataLoaderId) {
            synchronized (com.android.server.pm.DataLoaderManagerService.this.mServiceConnections) {
                com.android.server.pm.DataLoaderManagerService.DataLoaderServiceConnection serviceConnection = (com.android.server.pm.DataLoaderManagerService.DataLoaderServiceConnection) com.android.server.pm.DataLoaderManagerService.this.mServiceConnections.get(dataLoaderId, null);
                if (serviceConnection == null) {
                    return;
                }
                serviceConnection.destroy();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DataLoaderServiceConnection implements android.content.ServiceConnection, android.os.IBinder.DeathRecipient {
        android.content.pm.IDataLoader mDataLoader = null;
        final int mId;
        final android.content.pm.IDataLoaderStatusListener mListener;

        DataLoaderServiceConnection(int id, android.content.pm.IDataLoaderStatusListener listener) {
            this.mId = id;
            this.mListener = listener;
            callListener(1);
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName className, android.os.IBinder service) {
            this.mDataLoader = android.content.pm.IDataLoader.Stub.asInterface(service);
            if (this.mDataLoader == null) {
                onNullBinding(className);
                return;
            }
            if (!append()) {
                com.android.server.pm.DataLoaderManagerService.this.mContext.unbindService(this);
                return;
            }
            try {
                service.linkToDeath(this, 0);
                callListener(2);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.pm.DataLoaderManagerService.TAG, "Failed to link to DataLoader's death: " + this.mId, e);
                onBindingDied(className);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName arg0) {
            android.util.Slog.i(com.android.server.pm.DataLoaderManagerService.TAG, "DataLoader " + this.mId + " disconnected, but will try to recover");
            unbindAndReportDestroyed();
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(android.content.ComponentName name) {
            android.util.Slog.i(com.android.server.pm.DataLoaderManagerService.TAG, "DataLoader " + this.mId + " died");
            unbindAndReportDestroyed();
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(android.content.ComponentName name) {
            android.util.Slog.i(com.android.server.pm.DataLoaderManagerService.TAG, "DataLoader " + this.mId + " failed to start");
            unbindAndReportDestroyed();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Slog.i(com.android.server.pm.DataLoaderManagerService.TAG, "DataLoader " + this.mId + " died");
            unbindAndReportDestroyed();
        }

        android.content.pm.IDataLoader getDataLoader() {
            return this.mDataLoader;
        }

        private void unbindAndReportDestroyed() {
            if (unbind()) {
                callListener(0);
            }
        }

        void destroy() {
            if (this.mDataLoader != null) {
                try {
                    this.mDataLoader.destroy(this.mId);
                } catch (android.os.RemoteException e) {
                }
                this.mDataLoader = null;
            }
            unbind();
        }

        boolean unbind() {
            try {
                com.android.server.pm.DataLoaderManagerService.this.mContext.unbindService(this);
            } catch (java.lang.Exception e) {
            }
            return remove();
        }

        private boolean append() {
            synchronized (com.android.server.pm.DataLoaderManagerService.this.mServiceConnections) {
                com.android.server.pm.DataLoaderManagerService.DataLoaderServiceConnection bound = (com.android.server.pm.DataLoaderManagerService.DataLoaderServiceConnection) com.android.server.pm.DataLoaderManagerService.this.mServiceConnections.get(this.mId);
                if (bound == this) {
                    return true;
                }
                if (bound != null) {
                    return false;
                }
                com.android.server.pm.DataLoaderManagerService.this.mServiceConnections.append(this.mId, this);
                return true;
            }
        }

        private boolean remove() {
            synchronized (com.android.server.pm.DataLoaderManagerService.this.mServiceConnections) {
                if (com.android.server.pm.DataLoaderManagerService.this.mServiceConnections.get(this.mId) == this) {
                    com.android.server.pm.DataLoaderManagerService.this.mServiceConnections.remove(this.mId);
                    return true;
                }
                return false;
            }
        }

        private void callListener(int status) {
            if (this.mListener != null) {
                try {
                    this.mListener.onStatusChanged(this.mId, status);
                } catch (android.os.RemoteException e) {
                }
            }
        }
    }
}
