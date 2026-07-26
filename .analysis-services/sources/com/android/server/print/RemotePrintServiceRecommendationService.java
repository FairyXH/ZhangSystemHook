package com.android.server.print;

/* JADX INFO: loaded from: classes3.dex */
class RemotePrintServiceRecommendationService {
    private static final java.lang.String LOG_TAG = "RemotePrintServiceRecS";
    private final com.android.server.print.RemotePrintServiceRecommendationService.Connection mConnection;
    private final android.content.Context mContext;
    private boolean mIsBound;
    private final java.lang.Object mLock = new java.lang.Object();
    private android.printservice.recommendation.IRecommendationService mService;

    public interface RemotePrintServiceRecommendationServiceCallbacks {
        void onPrintServiceRecommendationsUpdated(java.util.List<android.printservice.recommendation.RecommendationInfo> list);
    }

    private android.content.Intent getServiceIntent(android.os.UserHandle userHandle) throws java.lang.Exception {
        java.util.List<android.content.pm.ResolveInfo> installedServices = this.mContext.getPackageManager().queryIntentServicesAsUser(new android.content.Intent("android.printservice.recommendation.RecommendationService"), 268435588, userHandle.getIdentifier());
        if (installedServices.size() != 1) {
            throw new java.lang.Exception(installedServices.size() + " instead of exactly one service found");
        }
        android.content.pm.ResolveInfo installedService = installedServices.get(0);
        android.content.ComponentName serviceName = new android.content.ComponentName(installedService.serviceInfo.packageName, installedService.serviceInfo.name);
        android.content.pm.ApplicationInfo appInfo = this.mContext.getPackageManager().getApplicationInfo(installedService.serviceInfo.packageName, 0);
        if (appInfo == null) {
            throw new java.lang.Exception("Cannot read appInfo for service");
        }
        if ((1 & appInfo.flags) == 0) {
            throw new java.lang.Exception("Service is not part of the system");
        }
        if (!"android.permission.BIND_PRINT_RECOMMENDATION_SERVICE".equals(installedService.serviceInfo.permission)) {
            throw new java.lang.Exception("Service " + serviceName.flattenToShortString() + " does not require permission android.permission.BIND_PRINT_RECOMMENDATION_SERVICE");
        }
        android.content.Intent serviceIntent = new android.content.Intent();
        serviceIntent.setComponent(serviceName);
        return serviceIntent;
    }

    RemotePrintServiceRecommendationService(android.content.Context context, android.os.UserHandle userHandle, com.android.server.print.RemotePrintServiceRecommendationService.RemotePrintServiceRecommendationServiceCallbacks callbacks) {
        this.mContext = context;
        this.mConnection = new com.android.server.print.RemotePrintServiceRecommendationService.Connection(callbacks);
        try {
            android.content.Intent serviceIntent = getServiceIntent(userHandle);
            synchronized (this.mLock) {
                this.mIsBound = this.mContext.bindServiceAsUser(serviceIntent, this.mConnection, android.hardware.audio.common.V2_0.AudioFormat.AAC_MAIN, userHandle);
                if (!this.mIsBound) {
                    throw new java.lang.Exception("Failed to bind to service " + serviceIntent);
                }
            }
        } catch (java.lang.Exception e) {
            android.util.Log.e(LOG_TAG, "Could not connect to print service recommendation service", e);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x001c A[Catch: all -> 0x0028, TryCatch #1 {, blocks: (B:4:0x0003, B:7:0x0008, B:11:0x0016, B:10:0x000f, B:12:0x0018, B:14:0x001c, B:15:0x0026), top: B:22:0x0003, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void close() {
        /*
            r5 = this;
            java.lang.Object r0 = r5.mLock
            monitor-enter(r0)
            android.printservice.recommendation.IRecommendationService r1 = r5.mService     // Catch: java.lang.Throwable -> L28
            if (r1 == 0) goto L18
            r1 = 0
            android.printservice.recommendation.IRecommendationService r2 = r5.mService     // Catch: android.os.RemoteException -> Le java.lang.Throwable -> L28
            r2.registerCallbacks(r1)     // Catch: android.os.RemoteException -> Le java.lang.Throwable -> L28
            goto L16
        Le:
            r2 = move-exception
            java.lang.String r3 = "RemotePrintServiceRecS"
            java.lang.String r4 = "Could not unregister callbacks"
            android.util.Log.e(r3, r4, r2)     // Catch: java.lang.Throwable -> L28
        L16:
            r5.mService = r1     // Catch: java.lang.Throwable -> L28
        L18:
            boolean r1 = r5.mIsBound     // Catch: java.lang.Throwable -> L28
            if (r1 == 0) goto L26
            android.content.Context r1 = r5.mContext     // Catch: java.lang.Throwable -> L28
            com.android.server.print.RemotePrintServiceRecommendationService$Connection r2 = r5.mConnection     // Catch: java.lang.Throwable -> L28
            r1.unbindService(r2)     // Catch: java.lang.Throwable -> L28
            r1 = 0
            r5.mIsBound = r1     // Catch: java.lang.Throwable -> L28
        L26:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L28
            return
        L28:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L28
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.print.RemotePrintServiceRecommendationService.close():void");
    }

    protected void finalize() throws java.lang.Throwable {
        if (this.mIsBound || this.mService != null) {
            android.util.Log.w(LOG_TAG, "Service still connected on finalize()");
            close();
        }
        super.finalize();
    }

    private class Connection implements android.content.ServiceConnection {
        private final com.android.server.print.RemotePrintServiceRecommendationService.RemotePrintServiceRecommendationServiceCallbacks mCallbacks;

        public Connection(com.android.server.print.RemotePrintServiceRecommendationService.RemotePrintServiceRecommendationServiceCallbacks callbacks) {
            this.mCallbacks = callbacks;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            synchronized (com.android.server.print.RemotePrintServiceRecommendationService.this.mLock) {
                com.android.server.print.RemotePrintServiceRecommendationService.this.mService = android.printservice.recommendation.IRecommendationService.Stub.asInterface(service);
                try {
                    com.android.server.print.RemotePrintServiceRecommendationService.this.mService.registerCallbacks(new android.printservice.recommendation.IRecommendationServiceCallbacks.Stub() { // from class: com.android.server.print.RemotePrintServiceRecommendationService.Connection.1
                        public void onRecommendationsUpdated(java.util.List<android.printservice.recommendation.RecommendationInfo> recommendations) {
                            synchronized (com.android.server.print.RemotePrintServiceRecommendationService.this.mLock) {
                                if (com.android.server.print.RemotePrintServiceRecommendationService.this.mIsBound && com.android.server.print.RemotePrintServiceRecommendationService.this.mService != null) {
                                    if (recommendations != null) {
                                        com.android.internal.util.Preconditions.checkCollectionElementsNotNull(recommendations, "recommendation");
                                    }
                                    com.android.server.print.RemotePrintServiceRecommendationService.Connection.this.mCallbacks.onPrintServiceRecommendationsUpdated(recommendations);
                                }
                            }
                        }
                    });
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.print.RemotePrintServiceRecommendationService.LOG_TAG, "Could not register callbacks", e);
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            android.util.Log.w(com.android.server.print.RemotePrintServiceRecommendationService.LOG_TAG, "Unexpected termination of connection");
            synchronized (com.android.server.print.RemotePrintServiceRecommendationService.this.mLock) {
                com.android.server.print.RemotePrintServiceRecommendationService.this.mService = null;
            }
        }
    }
}
