package com.android.server.print;

/* JADX INFO: loaded from: classes3.dex */
final class RemotePrintService implements android.os.IBinder.DeathRecipient {
    private static final boolean DEBUG = false;
    private static final java.lang.String LOG_TAG = "RemotePrintService";
    private boolean mBinding;
    private final com.android.server.print.RemotePrintService.PrintServiceCallbacks mCallbacks;
    private final android.content.ComponentName mComponentName;
    private final android.content.Context mContext;
    private boolean mDestroyed;
    private java.util.List<android.print.PrinterId> mDiscoveryPriorityList;
    private boolean mHasActivePrintJobs;
    private boolean mHasPrinterDiscoverySession;
    private final android.content.Intent mIntent;
    private android.printservice.IPrintService mPrintService;
    private boolean mServiceDied;
    private final com.android.server.print.RemotePrintSpooler mSpooler;
    private java.util.List<android.print.PrinterId> mTrackedPrinterList;
    private final int mUserId;
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.List<java.lang.Runnable> mPendingCommands = new java.util.ArrayList();
    private final android.content.ServiceConnection mServiceConnection = new com.android.server.print.RemotePrintService.RemoteServiceConneciton();
    private final com.android.server.print.RemotePrintService.RemotePrintServiceClient mPrintServiceClient = new com.android.server.print.RemotePrintService.RemotePrintServiceClient(this);

    public interface PrintServiceCallbacks {
        void onCustomPrinterIconLoaded(android.print.PrinterId printerId, android.graphics.drawable.Icon icon);

        void onPrintersAdded(java.util.List<android.print.PrinterInfo> list);

        void onPrintersRemoved(java.util.List<android.print.PrinterId> list);

        void onServiceDied(com.android.server.print.RemotePrintService remotePrintService);
    }

    public RemotePrintService(android.content.Context context, android.content.ComponentName componentName, int userId, com.android.server.print.RemotePrintSpooler spooler, com.android.server.print.RemotePrintService.PrintServiceCallbacks callbacks) {
        this.mContext = context;
        this.mCallbacks = callbacks;
        this.mComponentName = componentName;
        this.mIntent = new android.content.Intent().setComponent(this.mComponentName);
        this.mUserId = userId;
        this.mSpooler = spooler;
    }

    public android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    public void destroy() {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.print.RemotePrintService) obj).handleDestroy();
            }
        }, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDestroy() {
        stopTrackingAllPrinters();
        if (this.mDiscoveryPriorityList != null) {
            handleStopPrinterDiscovery();
        }
        if (this.mHasPrinterDiscoverySession) {
            handleDestroyPrinterDiscoverySession();
        }
        ensureUnbound();
        this.mDestroyed = true;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.print.RemotePrintService) obj).handleBinderDied();
            }
        }, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBinderDied() {
        if (this.mPrintService != null) {
            try {
                this.mPrintService.asBinder().unlinkToDeath(this, 0);
            } catch (java.util.NoSuchElementException e) {
                android.util.Slog.e(LOG_TAG, "Error when handling binder died ", e);
            }
        }
        this.mPrintService = null;
        this.mServiceDied = true;
        this.mCallbacks.onServiceDied(this);
    }

    public void onAllPrintJobsHandled() {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.print.RemotePrintService) obj).handleOnAllPrintJobsHandled();
            }
        }, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnAllPrintJobsHandled() {
        this.mHasActivePrintJobs = false;
        if (!isBound()) {
            if (this.mServiceDied && !this.mHasPrinterDiscoverySession) {
                ensureUnbound();
                return;
            } else {
                ensureBound();
                this.mPendingCommands.add(new java.lang.Runnable() { // from class: com.android.server.print.RemotePrintService.1
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.print.RemotePrintService.this.handleOnAllPrintJobsHandled();
                    }
                });
                return;
            }
        }
        if (!this.mHasPrinterDiscoverySession) {
            ensureUnbound();
        }
    }

    public void onRequestCancelPrintJob(android.print.PrintJobInfo printJob) {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda10
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.print.RemotePrintService) obj).handleRequestCancelPrintJob((android.print.PrintJobInfo) obj2);
            }
        }, this, printJob));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRequestCancelPrintJob(final android.print.PrintJobInfo printJob) {
        if (!isBound()) {
            ensureBound();
            this.mPendingCommands.add(new java.lang.Runnable() { // from class: com.android.server.print.RemotePrintService.2
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.print.RemotePrintService.this.handleRequestCancelPrintJob(printJob);
                }
            });
        } else {
            try {
                this.mPrintService.requestCancelPrintJob(printJob);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error canceling a pring job.", re);
            }
        }
    }

    public void onPrintJobQueued(android.print.PrintJobInfo printJob) {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda6
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.print.RemotePrintService) obj).handleOnPrintJobQueued((android.print.PrintJobInfo) obj2);
            }
        }, this, printJob));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnPrintJobQueued(final android.print.PrintJobInfo printJob) {
        this.mHasActivePrintJobs = true;
        if (!isBound()) {
            ensureBound();
            this.mPendingCommands.add(new java.lang.Runnable() { // from class: com.android.server.print.RemotePrintService.3
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.print.RemotePrintService.this.handleOnPrintJobQueued(printJob);
                }
            });
        } else {
            try {
                this.mPrintService.onPrintJobQueued(printJob);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error announcing queued pring job.", re);
            }
        }
    }

    public void createPrinterDiscoverySession() {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.print.RemotePrintService) obj).handleCreatePrinterDiscoverySession();
            }
        }, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCreatePrinterDiscoverySession() {
        this.mHasPrinterDiscoverySession = true;
        if (!isBound()) {
            ensureBound();
            this.mPendingCommands.add(new java.lang.Runnable() { // from class: com.android.server.print.RemotePrintService.4
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.print.RemotePrintService.this.handleCreatePrinterDiscoverySession();
                }
            });
        } else {
            try {
                this.mPrintService.createPrinterDiscoverySession();
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error creating printer discovery session.", re);
            }
        }
    }

    public void destroyPrinterDiscoverySession() {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.print.RemotePrintService) obj).handleDestroyPrinterDiscoverySession();
            }
        }, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDestroyPrinterDiscoverySession() {
        this.mHasPrinterDiscoverySession = false;
        if (!isBound()) {
            if (this.mServiceDied && !this.mHasActivePrintJobs) {
                ensureUnbound();
                return;
            } else {
                ensureBound();
                this.mPendingCommands.add(new java.lang.Runnable() { // from class: com.android.server.print.RemotePrintService.5
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.print.RemotePrintService.this.handleDestroyPrinterDiscoverySession();
                    }
                });
                return;
            }
        }
        try {
            this.mPrintService.destroyPrinterDiscoverySession();
        } catch (android.os.RemoteException re) {
            android.util.Slog.e(LOG_TAG, "Error destroying printer dicovery session.", re);
        }
        if (!this.mHasActivePrintJobs) {
            ensureUnbound();
        }
    }

    public void startPrinterDiscovery(java.util.List<android.print.PrinterId> priorityList) {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.print.RemotePrintService) obj).handleStartPrinterDiscovery((java.util.List) obj2);
            }
        }, this, priorityList));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStartPrinterDiscovery(final java.util.List<android.print.PrinterId> priorityList) {
        this.mDiscoveryPriorityList = new java.util.ArrayList();
        if (priorityList != null) {
            this.mDiscoveryPriorityList.addAll(priorityList);
        }
        if (!isBound()) {
            ensureBound();
            this.mPendingCommands.add(new java.lang.Runnable() { // from class: com.android.server.print.RemotePrintService.6
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.print.RemotePrintService.this.handleStartPrinterDiscovery(priorityList);
                }
            });
        } else {
            try {
                this.mPrintService.startPrinterDiscovery(priorityList);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error starting printer dicovery.", re);
            }
        }
    }

    public void stopPrinterDiscovery() {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.print.RemotePrintService) obj).handleStopPrinterDiscovery();
            }
        }, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStopPrinterDiscovery() {
        this.mDiscoveryPriorityList = null;
        if (!isBound()) {
            ensureBound();
            this.mPendingCommands.add(new java.lang.Runnable() { // from class: com.android.server.print.RemotePrintService.7
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.print.RemotePrintService.this.handleStopPrinterDiscovery();
                }
            });
            return;
        }
        stopTrackingAllPrinters();
        try {
            this.mPrintService.stopPrinterDiscovery();
        } catch (android.os.RemoteException re) {
            android.util.Slog.e(LOG_TAG, "Error stopping printer discovery.", re);
        }
    }

    public void validatePrinters(java.util.List<android.print.PrinterId> printerIds) {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda11
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.print.RemotePrintService) obj).handleValidatePrinters((java.util.List) obj2);
            }
        }, this, printerIds));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleValidatePrinters(final java.util.List<android.print.PrinterId> printerIds) {
        if (!isBound()) {
            ensureBound();
            this.mPendingCommands.add(new java.lang.Runnable() { // from class: com.android.server.print.RemotePrintService.8
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.print.RemotePrintService.this.handleValidatePrinters(printerIds);
                }
            });
        } else {
            try {
                this.mPrintService.validatePrinters(printerIds);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error requesting printers validation.", re);
            }
        }
    }

    public void startPrinterStateTracking(android.print.PrinterId printerId) {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda7
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.print.RemotePrintService) obj).handleStartPrinterStateTracking((android.print.PrinterId) obj2);
            }
        }, this, printerId));
    }

    public void requestCustomPrinterIcon(android.print.PrinterId printerId) {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.print.RemotePrintService) obj).lambda$handleRequestCustomPrinterIcon$0((android.print.PrinterId) obj2);
            }
        }, this, printerId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleRequestCustomPrinterIcon, reason: merged with bridge method [inline-methods] */
    public void lambda$handleRequestCustomPrinterIcon$0(final android.print.PrinterId printerId) {
        if (!isBound()) {
            ensureBound();
            this.mPendingCommands.add(new java.lang.Runnable() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$handleRequestCustomPrinterIcon$0(printerId);
                }
            });
        } else {
            try {
                this.mPrintService.requestCustomPrinterIcon(printerId);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error requesting icon for " + printerId, re);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStartPrinterStateTracking(final android.print.PrinterId printerId) {
        synchronized (this.mLock) {
            if (this.mTrackedPrinterList == null) {
                this.mTrackedPrinterList = new java.util.ArrayList();
            }
            this.mTrackedPrinterList.add(printerId);
        }
        if (!isBound()) {
            ensureBound();
            this.mPendingCommands.add(new java.lang.Runnable() { // from class: com.android.server.print.RemotePrintService.9
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.print.RemotePrintService.this.handleStartPrinterStateTracking(printerId);
                }
            });
        } else {
            try {
                this.mPrintService.startPrinterStateTracking(printerId);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Error requesting start printer tracking.", re);
            }
        }
    }

    public void stopPrinterStateTracking(android.print.PrinterId printerId) {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.RemotePrintService$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.print.RemotePrintService) obj).handleStopPrinterStateTracking((android.print.PrinterId) obj2);
            }
        }, this, printerId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStopPrinterStateTracking(final android.print.PrinterId printerId) {
        synchronized (this.mLock) {
            if (this.mTrackedPrinterList != null && this.mTrackedPrinterList.remove(printerId)) {
                if (this.mTrackedPrinterList.isEmpty()) {
                    this.mTrackedPrinterList = null;
                }
                if (!isBound()) {
                    ensureBound();
                    this.mPendingCommands.add(new java.lang.Runnable() { // from class: com.android.server.print.RemotePrintService.10
                        @Override // java.lang.Runnable
                        public void run() {
                            com.android.server.print.RemotePrintService.this.handleStopPrinterStateTracking(printerId);
                        }
                    });
                } else {
                    try {
                        this.mPrintService.stopPrinterStateTracking(printerId);
                    } catch (android.os.RemoteException re) {
                        android.util.Slog.e(LOG_TAG, "Error requesting stop printer tracking.", re);
                    }
                }
            }
        }
    }

    private void stopTrackingAllPrinters() {
        synchronized (this.mLock) {
            if (this.mTrackedPrinterList == null) {
                return;
            }
            int trackedPrinterCount = this.mTrackedPrinterList.size();
            for (int i = trackedPrinterCount - 1; i >= 0; i--) {
                android.print.PrinterId printerId = this.mTrackedPrinterList.get(i);
                if (printerId.getServiceName().equals(this.mComponentName)) {
                    handleStopPrinterStateTracking(printerId);
                }
            }
        }
    }

    public void dump(com.android.internal.util.dump.DualDumpOutputStream proto) {
        com.android.internal.util.dump.DumpUtils.writeComponentName(proto, "component_name", 1146756268033L, this.mComponentName);
        proto.write("is_destroyed", 1133871366146L, this.mDestroyed);
        proto.write("is_bound", 1133871366147L, isBound());
        proto.write("has_discovery_session", 1133871366148L, this.mHasPrinterDiscoverySession);
        proto.write("has_active_print_jobs", 1133871366149L, this.mHasActivePrintJobs);
        proto.write("is_discovering_printers", 1133871366150L, this.mDiscoveryPriorityList != null);
        synchronized (this.mLock) {
            if (this.mTrackedPrinterList != null) {
                int numTrackedPrinters = this.mTrackedPrinterList.size();
                for (int i = 0; i < numTrackedPrinters; i++) {
                    com.android.internal.print.DumpUtils.writePrinterId(proto, "tracked_printers", 2246267895815L, this.mTrackedPrinterList.get(i));
                }
            }
        }
    }

    private boolean isBound() {
        return this.mPrintService != null;
    }

    private void ensureBound() {
        if (isBound() || this.mBinding) {
            return;
        }
        this.mBinding = true;
        boolean wasBound = this.mContext.bindServiceAsUser(this.mIntent, this.mServiceConnection, 71307265, new android.os.UserHandle(this.mUserId));
        if (!wasBound) {
            this.mBinding = false;
            if (!this.mServiceDied) {
                handleBinderDied();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureUnbound() {
        if (!isBound() && !this.mBinding) {
            return;
        }
        this.mBinding = false;
        this.mPendingCommands.clear();
        this.mHasActivePrintJobs = false;
        this.mHasPrinterDiscoverySession = false;
        this.mDiscoveryPriorityList = null;
        synchronized (this.mLock) {
            this.mTrackedPrinterList = null;
        }
        if (isBound()) {
            try {
                this.mPrintService.setClient((android.printservice.IPrintServiceClient) null);
            } catch (android.os.RemoteException e) {
            }
            this.mPrintService.asBinder().unlinkToDeath(this, 0);
            this.mPrintService = null;
            this.mContext.unbindService(this.mServiceConnection);
        }
    }

    private class RemoteServiceConneciton implements android.content.ServiceConnection {
        private RemoteServiceConneciton() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            if (com.android.server.print.RemotePrintService.this.mDestroyed || !com.android.server.print.RemotePrintService.this.mBinding) {
                com.android.server.print.RemotePrintService.this.mContext.unbindService(com.android.server.print.RemotePrintService.this.mServiceConnection);
                return;
            }
            com.android.server.print.RemotePrintService.this.mBinding = false;
            com.android.server.print.RemotePrintService.this.mPrintService = android.printservice.IPrintService.Stub.asInterface(service);
            try {
                service.linkToDeath(com.android.server.print.RemotePrintService.this, 0);
                try {
                    com.android.server.print.RemotePrintService.this.mPrintService.setClient(com.android.server.print.RemotePrintService.this.mPrintServiceClient);
                    if (com.android.server.print.RemotePrintService.this.mServiceDied && com.android.server.print.RemotePrintService.this.mHasPrinterDiscoverySession) {
                        com.android.server.print.RemotePrintService.this.handleCreatePrinterDiscoverySession();
                    }
                    if (com.android.server.print.RemotePrintService.this.mServiceDied && com.android.server.print.RemotePrintService.this.mDiscoveryPriorityList != null) {
                        com.android.server.print.RemotePrintService.this.handleStartPrinterDiscovery(com.android.server.print.RemotePrintService.this.mDiscoveryPriorityList);
                    }
                    synchronized (com.android.server.print.RemotePrintService.this.mLock) {
                        if (com.android.server.print.RemotePrintService.this.mServiceDied && com.android.server.print.RemotePrintService.this.mTrackedPrinterList != null) {
                            int trackedPrinterCount = com.android.server.print.RemotePrintService.this.mTrackedPrinterList.size();
                            for (int i = 0; i < trackedPrinterCount; i++) {
                                com.android.server.print.RemotePrintService.this.handleStartPrinterStateTracking((android.print.PrinterId) com.android.server.print.RemotePrintService.this.mTrackedPrinterList.get(i));
                            }
                        }
                    }
                    while (!com.android.server.print.RemotePrintService.this.mPendingCommands.isEmpty()) {
                        java.lang.Runnable pendingCommand = (java.lang.Runnable) com.android.server.print.RemotePrintService.this.mPendingCommands.remove(0);
                        pendingCommand.run();
                    }
                    if (!com.android.server.print.RemotePrintService.this.mHasPrinterDiscoverySession && !com.android.server.print.RemotePrintService.this.mHasActivePrintJobs) {
                        com.android.server.print.RemotePrintService.this.ensureUnbound();
                    }
                    com.android.server.print.RemotePrintService.this.mServiceDied = false;
                } catch (android.os.RemoteException re) {
                    android.util.Slog.e(com.android.server.print.RemotePrintService.LOG_TAG, "Error setting client for: " + service, re);
                    com.android.server.print.RemotePrintService.this.handleBinderDied();
                }
            } catch (android.os.RemoteException e) {
                com.android.server.print.RemotePrintService.this.handleBinderDied();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            com.android.server.print.RemotePrintService.this.mBinding = true;
        }
    }

    private static final class RemotePrintServiceClient extends android.printservice.IPrintServiceClient.Stub {
        private final java.lang.ref.WeakReference<com.android.server.print.RemotePrintService> mWeakService;

        public RemotePrintServiceClient(com.android.server.print.RemotePrintService service) {
            this.mWeakService = new java.lang.ref.WeakReference<>(service);
        }

        public java.util.List<android.print.PrintJobInfo> getPrintJobInfos() {
            com.android.server.print.RemotePrintService service = this.mWeakService.get();
            if (service != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return service.mSpooler.getPrintJobInfos(service.mComponentName, -4, -2);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            return null;
        }

        public android.print.PrintJobInfo getPrintJobInfo(android.print.PrintJobId printJobId) {
            com.android.server.print.RemotePrintService service = this.mWeakService.get();
            if (service != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return service.mSpooler.getPrintJobInfo(printJobId, -2);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            return null;
        }

        public boolean setPrintJobState(android.print.PrintJobId printJobId, int state, java.lang.String error) {
            com.android.server.print.RemotePrintService service = this.mWeakService.get();
            if (service != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return service.mSpooler.setPrintJobState(printJobId, state, error);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            return false;
        }

        public boolean setPrintJobTag(android.print.PrintJobId printJobId, java.lang.String tag) {
            com.android.server.print.RemotePrintService service = this.mWeakService.get();
            if (service != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return service.mSpooler.setPrintJobTag(printJobId, tag);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            return false;
        }

        public void writePrintJobData(android.os.ParcelFileDescriptor fd, android.print.PrintJobId printJobId) {
            com.android.server.print.RemotePrintService service = this.mWeakService.get();
            if (service != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    service.mSpooler.writePrintJobData(fd, printJobId);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void setProgress(android.print.PrintJobId printJobId, float progress) {
            com.android.server.print.RemotePrintService service = this.mWeakService.get();
            if (service != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    service.mSpooler.setProgress(printJobId, progress);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void setStatus(android.print.PrintJobId printJobId, java.lang.CharSequence status) {
            com.android.server.print.RemotePrintService service = this.mWeakService.get();
            if (service != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    service.mSpooler.setStatus(printJobId, status);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void setStatusRes(android.print.PrintJobId printJobId, int status, java.lang.CharSequence appPackageName) {
            com.android.server.print.RemotePrintService service = this.mWeakService.get();
            if (service != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    service.mSpooler.setStatus(printJobId, status, appPackageName);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void onPrintersAdded(android.content.pm.ParceledListSlice printers) {
            com.android.server.print.RemotePrintService service = this.mWeakService.get();
            if (service != null) {
                java.util.List<android.print.PrinterInfo> addedPrinters = printers.getList();
                throwIfPrinterIdsForPrinterInfoTampered(service.mComponentName, addedPrinters);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    service.mCallbacks.onPrintersAdded(addedPrinters);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void onPrintersRemoved(android.content.pm.ParceledListSlice printerIds) {
            com.android.server.print.RemotePrintService service = this.mWeakService.get();
            if (service != null) {
                java.util.List<android.print.PrinterId> removedPrinterIds = printerIds.getList();
                throwIfPrinterIdsTampered(service.mComponentName, removedPrinterIds);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    service.mCallbacks.onPrintersRemoved(removedPrinterIds);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        private void throwIfPrinterIdsForPrinterInfoTampered(android.content.ComponentName serviceName, java.util.List<android.print.PrinterInfo> printerInfos) {
            int printerInfoCount = printerInfos.size();
            for (int i = 0; i < printerInfoCount; i++) {
                android.print.PrinterId printerId = printerInfos.get(i).getId();
                throwIfPrinterIdTampered(serviceName, printerId);
            }
        }

        private void throwIfPrinterIdsTampered(android.content.ComponentName serviceName, java.util.List<android.print.PrinterId> printerIds) {
            int printerIdCount = printerIds.size();
            for (int i = 0; i < printerIdCount; i++) {
                android.print.PrinterId printerId = printerIds.get(i);
                throwIfPrinterIdTampered(serviceName, printerId);
            }
        }

        private void throwIfPrinterIdTampered(android.content.ComponentName serviceName, android.print.PrinterId printerId) {
            if (printerId == null || !printerId.getServiceName().equals(serviceName)) {
                throw new java.lang.IllegalArgumentException("Invalid printer id: " + printerId);
            }
        }

        public void onCustomPrinterIconLoaded(android.print.PrinterId printerId, android.graphics.drawable.Icon icon) throws android.os.RemoteException {
            com.android.server.print.RemotePrintService service = this.mWeakService.get();
            if (service != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    service.mCallbacks.onCustomPrinterIconLoaded(printerId, icon);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }
    }
}
