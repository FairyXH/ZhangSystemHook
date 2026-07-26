package com.android.server.servicewatcher;

/* JADX INFO: loaded from: classes3.dex */
class ServiceWatcherImpl<TBoundServiceInfo extends com.android.server.servicewatcher.ServiceWatcher.BoundServiceInfo> implements com.android.server.servicewatcher.ServiceWatcher, com.android.server.servicewatcher.ServiceWatcher.ServiceChangedListener {
    static final long RETRY_DELAY_MS = 15000;
    final android.content.Context mContext;
    final android.os.Handler mHandler;
    private final com.android.internal.content.PackageMonitor mPackageMonitor = new com.android.internal.content.PackageMonitor() { // from class: com.android.server.servicewatcher.ServiceWatcherImpl.1
        public boolean onPackageChanged(java.lang.String packageName, int uid, java.lang.String[] components) {
            return true;
        }

        public void onSomePackagesChanged() {
            com.android.server.servicewatcher.ServiceWatcherImpl.this.onServiceChanged(false);
        }
    };
    private boolean mRegistered = false;
    private com.android.server.servicewatcher.ServiceWatcherImpl<TBoundServiceInfo>.MyServiceConnection mServiceConnection = new com.android.server.servicewatcher.ServiceWatcherImpl.MyServiceConnection(null);
    final com.android.server.servicewatcher.ServiceWatcher.ServiceListener<? super TBoundServiceInfo> mServiceListener;
    final com.android.server.servicewatcher.ServiceWatcher.ServiceSupplier<TBoundServiceInfo> mServiceSupplier;
    final java.lang.String mTag;
    static final java.lang.String TAG = "ServiceWatcher";
    static final boolean D = android.util.Log.isLoggable(TAG, 3);

    ServiceWatcherImpl(android.content.Context context, android.os.Handler handler, java.lang.String tag, com.android.server.servicewatcher.ServiceWatcher.ServiceSupplier<TBoundServiceInfo> serviceSupplier, com.android.server.servicewatcher.ServiceWatcher.ServiceListener<? super TBoundServiceInfo> serviceListener) {
        this.mContext = context;
        this.mHandler = handler;
        this.mTag = tag;
        this.mServiceSupplier = serviceSupplier;
        this.mServiceListener = serviceListener;
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher
    public boolean checkServiceResolves() {
        return this.mServiceSupplier.hasMatchingService();
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher
    public synchronized void register() {
        com.android.internal.util.Preconditions.checkState(!this.mRegistered);
        this.mRegistered = true;
        this.mPackageMonitor.register(this.mContext, android.os.UserHandle.ALL, this.mHandler);
        this.mServiceSupplier.register(this);
        onServiceChanged(false);
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher
    public synchronized void unregister() {
        com.android.internal.util.Preconditions.checkState(this.mRegistered);
        this.mServiceSupplier.unregister();
        this.mPackageMonitor.unregister();
        this.mRegistered = false;
        onServiceChanged(false);
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher.ServiceChangedListener
    public synchronized void onServiceChanged() {
        onServiceChanged(false);
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher
    public synchronized void runOnBinder(final com.android.server.servicewatcher.ServiceWatcher.BinderOperation operation) {
        final com.android.server.servicewatcher.ServiceWatcherImpl<TBoundServiceInfo>.MyServiceConnection serviceConnection = this.mServiceConnection;
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.servicewatcher.ServiceWatcherImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                serviceConnection.runOnBinder(operation);
            }
        });
    }

    synchronized void onServiceChanged(boolean forceRebind) {
        com.android.server.servicewatcher.ServiceWatcher.BoundServiceInfo serviceInfo;
        if (this.mRegistered) {
            serviceInfo = this.mServiceSupplier.getServiceInfo();
        } else {
            serviceInfo = null;
        }
        if ((forceRebind | (!this.mServiceConnection.isConnected())) || !java.util.Objects.equals(this.mServiceConnection.getBoundServiceInfo(), serviceInfo)) {
            android.util.Log.i(TAG, "[" + this.mTag + "] chose new implementation " + serviceInfo);
            final com.android.server.servicewatcher.ServiceWatcherImpl<TBoundServiceInfo>.MyServiceConnection oldServiceConnection = this.mServiceConnection;
            final com.android.server.servicewatcher.ServiceWatcherImpl<TBoundServiceInfo>.MyServiceConnection newServiceConnection = new com.android.server.servicewatcher.ServiceWatcherImpl.MyServiceConnection(serviceInfo);
            this.mServiceConnection = newServiceConnection;
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.servicewatcher.ServiceWatcherImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.servicewatcher.ServiceWatcherImpl.lambda$onServiceChanged$1(oldServiceConnection, newServiceConnection);
                }
            });
        }
    }

    static /* synthetic */ void lambda$onServiceChanged$1(com.android.server.servicewatcher.ServiceWatcherImpl.MyServiceConnection oldServiceConnection, com.android.server.servicewatcher.ServiceWatcherImpl.MyServiceConnection newServiceConnection) {
        oldServiceConnection.unbind();
        newServiceConnection.bind();
    }

    public java.lang.String toString() {
        com.android.server.servicewatcher.ServiceWatcherImpl<TBoundServiceInfo>.MyServiceConnection serviceConnection;
        synchronized (this) {
            serviceConnection = this.mServiceConnection;
        }
        return serviceConnection.getBoundServiceInfo().toString();
    }

    @Override // com.android.server.servicewatcher.ServiceWatcher
    public void dump(java.io.PrintWriter pw) {
        com.android.server.servicewatcher.ServiceWatcherImpl<TBoundServiceInfo>.MyServiceConnection serviceConnection;
        synchronized (this) {
            serviceConnection = this.mServiceConnection;
        }
        pw.println("target service=" + serviceConnection.getBoundServiceInfo());
        pw.println("connected=" + serviceConnection.isConnected());
    }

    /* JADX INFO: Access modifiers changed from: private */
    class MyServiceConnection implements android.content.ServiceConnection {
        private volatile android.os.IBinder mBinder;
        private final TBoundServiceInfo mBoundServiceInfo;
        private com.android.server.servicewatcher.IServiceWatcherExt mConnectionWrapper = new com.android.server.servicewatcher.ServiceWatcherImpl.MyServiceConnection.MyServiceWatcherWrapper();
        private java.lang.Runnable mRebinder;

        MyServiceConnection(TBoundServiceInfo boundServiceInfo) {
            this.mBoundServiceInfo = boundServiceInfo;
        }

        TBoundServiceInfo getBoundServiceInfo() {
            return this.mBoundServiceInfo;
        }

        boolean isConnected() {
            return this.mBinder != null;
        }

        void bind() {
            com.android.internal.util.Preconditions.checkState(android.os.Looper.myLooper() == com.android.server.servicewatcher.ServiceWatcherImpl.this.mHandler.getLooper());
            if (this.mBoundServiceInfo == null) {
                return;
            }
            if (com.android.server.servicewatcher.ServiceWatcherImpl.D) {
                android.util.Log.d(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] binding to " + this.mBoundServiceInfo);
            }
            this.mRebinder = null;
            android.content.Intent bindIntent = new android.content.Intent(this.mBoundServiceInfo.getAction()).setComponent(this.mBoundServiceInfo.getComponentName());
            int flag = 1073741829;
            if ("network".equals(com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag) && ((com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, com.android.server.servicewatcher.ServiceWatcherImpl.this.mContext)).isUsingRegionNlp()) {
                flag = android.hardware.audio.common.V2_0.AudioFormat.AAC_MAIN;
            }
            try {
                if (!com.android.server.servicewatcher.ServiceWatcherImpl.this.mContext.bindServiceAsUser(bindIntent, this, flag, com.android.server.servicewatcher.ServiceWatcherImpl.this.mHandler, android.os.UserHandle.of(this.mBoundServiceInfo.getUserId()))) {
                    android.util.Log.e(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] unexpected bind failure - retrying later");
                    this.mRebinder = new java.lang.Runnable() { // from class: com.android.server.servicewatcher.ServiceWatcherImpl$MyServiceConnection$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.bind();
                        }
                    };
                    com.android.server.servicewatcher.ServiceWatcherImpl.this.mHandler.postDelayed(this.mRebinder, com.android.server.servicewatcher.ServiceWatcherImpl.RETRY_DELAY_MS);
                }
            } catch (java.lang.SecurityException e) {
                android.util.Log.e(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] " + this.mBoundServiceInfo + " bind failed", e);
            }
        }

        void unbind() {
            com.android.internal.util.Preconditions.checkState(android.os.Looper.myLooper() == com.android.server.servicewatcher.ServiceWatcherImpl.this.mHandler.getLooper());
            if (this.mBoundServiceInfo == null) {
                return;
            }
            if (com.android.server.servicewatcher.ServiceWatcherImpl.D) {
                android.util.Log.d(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] unbinding from " + this.mBoundServiceInfo);
            }
            if (this.mRebinder != null) {
                com.android.server.servicewatcher.ServiceWatcherImpl.this.mHandler.removeCallbacks(this.mRebinder);
                this.mRebinder = null;
            } else {
                com.android.server.servicewatcher.ServiceWatcherImpl.this.mContext.unbindService(this);
            }
            onServiceDisconnected(this.mBoundServiceInfo.getComponentName());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void runOnBinder(com.android.server.servicewatcher.ServiceWatcher.BinderOperation operation) {
            com.android.internal.util.Preconditions.checkState(android.os.Looper.myLooper() == com.android.server.servicewatcher.ServiceWatcherImpl.this.mHandler.getLooper());
            if (this.mBinder == null) {
                operation.onError(new android.os.DeadObjectException());
                return;
            }
            try {
                operation.run(this.mBinder);
            } catch (android.os.RemoteException | java.lang.RuntimeException e) {
                android.util.Log.e(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] error running operation on " + this.mBoundServiceInfo, e);
                operation.onError(e);
            }
        }

        @Override // android.content.ServiceConnection
        public final void onServiceConnected(android.content.ComponentName component, android.os.IBinder binder) {
            com.android.internal.util.Preconditions.checkState(android.os.Looper.myLooper() == com.android.server.servicewatcher.ServiceWatcherImpl.this.mHandler.getLooper());
            if (this.mBinder != null) {
                ((com.android.server.location.interfaces.IServiceWatchExt) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IServiceWatchExt.DEFAULT, com.android.server.servicewatcher.ServiceWatcherImpl.this.mContext)).storeConnectionWrapper(this.mConnectionWrapper, component, binder, com.android.server.servicewatcher.ServiceWatcherImpl.this.mHandler.getLooper());
                return;
            }
            com.android.internal.util.Preconditions.checkState(this.mBinder == null);
            android.util.Log.i(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] connected to " + component.toShortString());
            this.mBinder = binder;
            if (com.android.server.servicewatcher.ServiceWatcherImpl.this.mServiceListener != null) {
                try {
                    com.android.server.servicewatcher.ServiceWatcherImpl.this.mServiceListener.onBind(binder, this.mBoundServiceInfo);
                } catch (android.os.RemoteException | java.lang.RuntimeException e) {
                    android.util.Log.e(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] error running operation on " + this.mBoundServiceInfo, e);
                }
            }
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(android.content.ComponentName component) {
            com.android.internal.util.Preconditions.checkState(android.os.Looper.myLooper() == com.android.server.servicewatcher.ServiceWatcherImpl.this.mHandler.getLooper());
            if (this.mBinder == null) {
                return;
            }
            android.util.Log.i(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] disconnected from " + this.mBoundServiceInfo);
            this.mBinder = null;
            if (com.android.server.servicewatcher.ServiceWatcherImpl.this.mServiceListener != null) {
                com.android.server.servicewatcher.ServiceWatcherImpl.this.mServiceListener.onUnbind();
            }
            ((com.android.server.location.interfaces.IServiceWatchExt) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IServiceWatchExt.DEFAULT, com.android.server.servicewatcher.ServiceWatcherImpl.this.mContext)).signalConnectionWrapper();
        }

        @Override // android.content.ServiceConnection
        public final void onBindingDied(android.content.ComponentName component) {
            com.android.internal.util.Preconditions.checkState(android.os.Looper.myLooper() == com.android.server.servicewatcher.ServiceWatcherImpl.this.mHandler.getLooper());
            android.util.Log.w(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] " + this.mBoundServiceInfo + " died");
            com.android.server.servicewatcher.ServiceWatcherImpl.this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.servicewatcher.ServiceWatcherImpl$MyServiceConnection$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onBindingDied$0();
                }
            }, 500L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindingDied$0() {
            com.android.server.servicewatcher.ServiceWatcherImpl.this.onServiceChanged(true);
        }

        @Override // android.content.ServiceConnection
        public final void onNullBinding(android.content.ComponentName component) {
            android.util.Log.e(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] " + this.mBoundServiceInfo + " has null binding");
        }

        public com.android.server.servicewatcher.IServiceWatcherExt getServiceWatcherWrapper() {
            return this.mConnectionWrapper;
        }

        private class MyServiceWatcherWrapper implements com.android.server.servicewatcher.IServiceWatcherExt {
            private MyServiceWatcherWrapper() {
            }

            @Override // com.android.server.servicewatcher.IServiceWatcherExt
            public void onServiceConnected(android.content.ComponentName component, android.os.IBinder binder) {
                if (com.android.server.servicewatcher.ServiceWatcherImpl.MyServiceConnection.this.mBinder != null) {
                    android.util.Log.e(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] connected to " + component.toShortString() + "Fail");
                }
                com.android.internal.util.Preconditions.checkState(com.android.server.servicewatcher.ServiceWatcherImpl.MyServiceConnection.this.mBinder == null);
                android.util.Log.i(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] connected to " + component.toShortString());
                com.android.server.servicewatcher.ServiceWatcherImpl.MyServiceConnection.this.mBinder = binder;
                if (com.android.server.servicewatcher.ServiceWatcherImpl.this.mServiceListener != null) {
                    try {
                        com.android.server.servicewatcher.ServiceWatcherImpl.this.mServiceListener.onBind(binder, com.android.server.servicewatcher.ServiceWatcherImpl.MyServiceConnection.this.mBoundServiceInfo);
                    } catch (android.os.RemoteException | java.lang.RuntimeException e) {
                        android.util.Log.e(com.android.server.servicewatcher.ServiceWatcherImpl.TAG, "[" + com.android.server.servicewatcher.ServiceWatcherImpl.this.mTag + "] error running operation on " + com.android.server.servicewatcher.ServiceWatcherImpl.MyServiceConnection.this.mBoundServiceInfo, e);
                    }
                }
            }
        }
    }
}
