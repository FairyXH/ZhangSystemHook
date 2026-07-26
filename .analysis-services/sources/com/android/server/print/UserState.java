package com.android.server.print;

/* JADX INFO: loaded from: classes3.dex */
final class UserState implements com.android.server.print.RemotePrintSpooler.PrintSpoolerCallbacks, com.android.server.print.RemotePrintService.PrintServiceCallbacks, com.android.server.print.RemotePrintServiceRecommendationService.RemotePrintServiceRecommendationServiceCallbacks {
    private static final char COMPONENT_NAME_SEPARATOR = ':';
    private static final boolean DEBUG = false;
    private static final java.lang.String LOG_TAG = "UserState";
    private static final int SERVICE_RESTART_DELAY_MILLIS = 500;
    private final android.content.Context mContext;
    private boolean mDestroyed;
    private boolean mIsInstantServiceAllowed;
    private final java.lang.Object mLock;
    private java.util.List<com.android.server.print.UserState.PrintJobStateChangeListenerRecord> mPrintJobStateChangeListenerRecords;
    private java.util.List<android.printservice.recommendation.RecommendationInfo> mPrintServiceRecommendations;
    private java.util.List<com.android.server.print.UserState.ListenerRecord<android.printservice.recommendation.IRecommendationsChangeListener>> mPrintServiceRecommendationsChangeListenerRecords;
    private com.android.server.print.RemotePrintServiceRecommendationService mPrintServiceRecommendationsService;
    private java.util.List<com.android.server.print.UserState.ListenerRecord<android.print.IPrintServicesChangeListener>> mPrintServicesChangeListenerRecords;
    private com.android.server.print.UserState.PrinterDiscoverySessionMediator mPrinterDiscoverySession;
    private final com.android.server.print.RemotePrintSpooler mSpooler;
    private final int mUserId;
    private final android.text.TextUtils.SimpleStringSplitter mStringColonSplitter = new android.text.TextUtils.SimpleStringSplitter(COMPONENT_NAME_SEPARATOR);
    private final android.content.Intent mQueryIntent = new android.content.Intent("android.printservice.PrintService");
    private final android.util.ArrayMap<android.content.ComponentName, com.android.server.print.RemotePrintService> mActiveServices = new android.util.ArrayMap<>();
    private final java.util.List<android.printservice.PrintServiceInfo> mInstalledServices = new java.util.ArrayList();
    private final java.util.Set<android.content.ComponentName> mDisabledServices = new android.util.ArraySet();
    private final com.android.server.print.UserState.PrintJobForAppCache mPrintJobForAppCache = new com.android.server.print.UserState.PrintJobForAppCache();

    public UserState(android.content.Context context, int userId, java.lang.Object lock, boolean lowPriority) {
        this.mContext = context;
        this.mUserId = userId;
        this.mLock = lock;
        this.mSpooler = new com.android.server.print.RemotePrintSpooler(context, userId, lowPriority, this);
        synchronized (this.mLock) {
            readInstalledPrintServicesLocked();
            upgradePersistentStateIfNeeded();
            readDisabledPrintServicesLocked();
        }
        prunePrintServices();
        onConfigurationChanged();
    }

    public void increasePriority() {
        this.mSpooler.increasePriority();
    }

    @Override // com.android.server.print.RemotePrintSpooler.PrintSpoolerCallbacks
    public void onPrintJobQueued(android.print.PrintJobInfo printJob) {
        com.android.server.print.RemotePrintService service;
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            android.content.ComponentName printServiceName = printJob.getPrinterId().getServiceName();
            service = this.mActiveServices.get(printServiceName);
        }
        if (service != null) {
            service.onPrintJobQueued(printJob);
        } else {
            this.mSpooler.setPrintJobState(printJob.getId(), 6, this.mContext.getString(android.R.string.postalTypeWork));
        }
    }

    @Override // com.android.server.print.RemotePrintSpooler.PrintSpoolerCallbacks
    public void onAllPrintJobsForServiceHandled(android.content.ComponentName printService) {
        com.android.server.print.RemotePrintService service;
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            service = this.mActiveServices.get(printService);
        }
        if (service != null) {
            service.onAllPrintJobsHandled();
        }
    }

    public void removeObsoletePrintJobs() {
        this.mSpooler.removeObsoletePrintJobs();
    }

    public android.os.Bundle print(java.lang.String printJobName, android.print.IPrintDocumentAdapter adapter, android.print.PrintAttributes attributes, java.lang.String packageName, int appId) throws java.lang.Throwable {
        android.print.PrintJobInfo printJob = new android.print.PrintJobInfo();
        printJob.setId(new android.print.PrintJobId());
        printJob.setAppId(appId);
        printJob.setLabel(printJobName);
        printJob.setAttributes(attributes);
        printJob.setState(1);
        printJob.setCopies(1);
        printJob.setCreationTime(java.lang.System.currentTimeMillis());
        if (!this.mPrintJobForAppCache.onPrintJobCreated(adapter.asBinder(), appId, printJob)) {
            return null;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.content.Intent intent = new android.content.Intent("android.print.PRINT_DIALOG");
            intent.setData(android.net.Uri.fromParts("printjob", printJob.getId().flattenToString(), null));
            intent.putExtra("android.print.intent.extra.EXTRA_PRINT_DOCUMENT_ADAPTER", adapter.asBinder());
            intent.putExtra("android.print.intent.extra.EXTRA_PRINT_JOB", printJob);
            try {
                intent.putExtra("android.intent.extra.PACKAGE_NAME", packageName);
                android.app.ActivityOptions activityOptions = android.app.ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(2);
                android.content.IntentSender intentSender = android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, 1409286144, activityOptions.toBundle(), new android.os.UserHandle(this.mUserId)).getIntentSender();
                android.os.Bundle result = new android.os.Bundle();
                result.putParcelable("android.print.intent.extra.EXTRA_PRINT_JOB", printJob);
                result.putParcelable("android.print.intent.extra.EXTRA_PRINT_DIALOG_INTENT", intentSender);
                android.os.Binder.restoreCallingIdentity(identity);
                return result;
            } catch (java.lang.Throwable th) {
                th = th;
                android.os.Binder.restoreCallingIdentity(identity);
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    public java.util.List<android.print.PrintJobInfo> getPrintJobInfos(int appId) {
        java.util.List<android.print.PrintJobInfo> cachedPrintJobs = this.mPrintJobForAppCache.getPrintJobs(appId);
        android.util.ArrayMap<android.print.PrintJobId, android.print.PrintJobInfo> result = new android.util.ArrayMap<>();
        int cachedPrintJobCount = cachedPrintJobs.size();
        for (int i = 0; i < cachedPrintJobCount; i++) {
            android.print.PrintJobInfo cachedPrintJob = cachedPrintJobs.get(i);
            result.put(cachedPrintJob.getId(), cachedPrintJob);
            cachedPrintJob.setTag(null);
            cachedPrintJob.setAdvancedOptions(null);
        }
        java.util.List<android.print.PrintJobInfo> printJobs = this.mSpooler.getPrintJobInfos(null, -1, appId);
        if (printJobs != null) {
            int printJobCount = printJobs.size();
            for (int i2 = 0; i2 < printJobCount; i2++) {
                android.print.PrintJobInfo printJob = printJobs.get(i2);
                result.put(printJob.getId(), printJob);
                printJob.setTag(null);
                printJob.setAdvancedOptions(null);
            }
        }
        return new java.util.ArrayList(result.values());
    }

    public android.print.PrintJobInfo getPrintJobInfo(android.print.PrintJobId printJobId, int appId) {
        android.print.PrintJobInfo printJob = this.mPrintJobForAppCache.getPrintJob(printJobId, appId);
        if (printJob == null) {
            printJob = this.mSpooler.getPrintJobInfo(printJobId, appId);
        }
        if (printJob != null) {
            printJob.setTag(null);
            printJob.setAdvancedOptions(null);
        }
        return printJob;
    }

    public android.graphics.drawable.Icon getCustomPrinterIcon(android.print.PrinterId printerId) {
        com.android.server.print.RemotePrintService service;
        android.graphics.drawable.Icon icon = this.mSpooler.getCustomPrinterIcon(printerId);
        if (icon == null && (service = this.mActiveServices.get(printerId.getServiceName())) != null) {
            service.requestCustomPrinterIcon(printerId);
        }
        return icon;
    }

    public void cancelPrintJob(android.print.PrintJobId printJobId, int appId) {
        com.android.server.print.RemotePrintService printService;
        android.print.PrintJobInfo printJobInfo = this.mSpooler.getPrintJobInfo(printJobId, appId);
        if (printJobInfo == null) {
            return;
        }
        this.mSpooler.setPrintJobCancelling(printJobId, true);
        if (printJobInfo.getState() != 6) {
            android.print.PrinterId printerId = printJobInfo.getPrinterId();
            if (printerId != null) {
                android.content.ComponentName printServiceName = printerId.getServiceName();
                synchronized (this.mLock) {
                    printService = this.mActiveServices.get(printServiceName);
                }
                if (printService == null) {
                    return;
                }
                printService.onRequestCancelPrintJob(printJobInfo);
                return;
            }
            return;
        }
        this.mSpooler.setPrintJobState(printJobId, 7, null);
    }

    public void restartPrintJob(android.print.PrintJobId printJobId, int appId) {
        android.print.PrintJobInfo printJobInfo = getPrintJobInfo(printJobId, appId);
        if (printJobInfo == null || printJobInfo.getState() != 6) {
            return;
        }
        this.mSpooler.setPrintJobState(printJobId, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0043  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.List<android.printservice.PrintServiceInfo> getPrintServices(int r9) {
        /*
            r8 = this;
            java.lang.Object r0 = r8.mLock
            monitor-enter(r0)
            r1 = 0
            java.util.List<android.printservice.PrintServiceInfo> r2 = r8.mInstalledServices     // Catch: java.lang.Throwable -> L53
            int r2 = r2.size()     // Catch: java.lang.Throwable -> L53
            r3 = 0
        Lb:
            if (r3 >= r2) goto L51
            java.util.List<android.printservice.PrintServiceInfo> r4 = r8.mInstalledServices     // Catch: java.lang.Throwable -> L53
            java.lang.Object r4 = r4.get(r3)     // Catch: java.lang.Throwable -> L53
            android.printservice.PrintServiceInfo r4 = (android.printservice.PrintServiceInfo) r4     // Catch: java.lang.Throwable -> L53
            android.content.ComponentName r5 = new android.content.ComponentName     // Catch: java.lang.Throwable -> L53
            android.content.pm.ResolveInfo r6 = r4.getResolveInfo()     // Catch: java.lang.Throwable -> L53
            android.content.pm.ServiceInfo r6 = r6.serviceInfo     // Catch: java.lang.Throwable -> L53
            java.lang.String r6 = r6.packageName     // Catch: java.lang.Throwable -> L53
            android.content.pm.ResolveInfo r7 = r4.getResolveInfo()     // Catch: java.lang.Throwable -> L53
            android.content.pm.ServiceInfo r7 = r7.serviceInfo     // Catch: java.lang.Throwable -> L53
            java.lang.String r7 = r7.name     // Catch: java.lang.Throwable -> L53
            r5.<init>(r6, r7)     // Catch: java.lang.Throwable -> L53
            android.util.ArrayMap<android.content.ComponentName, com.android.server.print.RemotePrintService> r6 = r8.mActiveServices     // Catch: java.lang.Throwable -> L53
            boolean r6 = r6.containsKey(r5)     // Catch: java.lang.Throwable -> L53
            r4.setIsEnabled(r6)     // Catch: java.lang.Throwable -> L53
            boolean r6 = r4.isEnabled()     // Catch: java.lang.Throwable -> L53
            if (r6 == 0) goto L3e
            r6 = r9 & 1
            if (r6 != 0) goto L43
            goto L4e
        L3e:
            r6 = r9 & 2
            if (r6 != 0) goto L43
            goto L4e
        L43:
            if (r1 != 0) goto L4b
            java.util.ArrayList r6 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L53
            r6.<init>()     // Catch: java.lang.Throwable -> L53
            r1 = r6
        L4b:
            r1.add(r4)     // Catch: java.lang.Throwable -> L53
        L4e:
            int r3 = r3 + 1
            goto Lb
        L51:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L53
            return r1
        L53:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L53
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.print.UserState.getPrintServices(int):java.util.List");
    }

    public void setPrintServiceEnabled(android.content.ComponentName serviceName, boolean isEnabled) {
        synchronized (this.mLock) {
            boolean isChanged = false;
            if (isEnabled) {
                isChanged = this.mDisabledServices.remove(serviceName);
            } else {
                int numServices = this.mInstalledServices.size();
                int i = 0;
                while (true) {
                    if (i >= numServices) {
                        break;
                    }
                    android.printservice.PrintServiceInfo service = this.mInstalledServices.get(i);
                    if (!service.getComponentName().equals(serviceName)) {
                        i++;
                    } else {
                        this.mDisabledServices.add(serviceName);
                        isChanged = true;
                        break;
                    }
                }
            }
            if (isChanged) {
                writeDisabledPrintServicesLocked(this.mDisabledServices);
                com.android.internal.logging.MetricsLogger.action(this.mContext, vendor.pixelworks.hardware.display.VendorConfig.TYPE_MAX, isEnabled ? 0 : 1);
                onConfigurationChangedLocked();
            }
        }
    }

    public boolean isPrintServiceEnabled(android.content.ComponentName serviceName) {
        synchronized (this.mLock) {
            return !this.mDisabledServices.contains(serviceName);
        }
    }

    public java.util.List<android.printservice.recommendation.RecommendationInfo> getPrintServiceRecommendations() {
        return this.mPrintServiceRecommendations;
    }

    public void createPrinterDiscoverySession(android.print.IPrinterDiscoveryObserver observer) {
        this.mSpooler.clearCustomPrinterIconCache();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mPrinterDiscoverySession == null) {
                this.mPrinterDiscoverySession = new com.android.server.print.UserState.PrinterDiscoverySessionMediator() { // from class: com.android.server.print.UserState.1
                    @Override // com.android.server.print.UserState.PrinterDiscoverySessionMediator
                    public void onDestroyed() {
                        com.android.server.print.UserState.this.mPrinterDiscoverySession = null;
                    }
                };
                this.mPrinterDiscoverySession.addObserverLocked(observer);
            } else {
                this.mPrinterDiscoverySession.addObserverLocked(observer);
            }
        }
    }

    public void destroyPrinterDiscoverySession(android.print.IPrinterDiscoveryObserver observer) {
        synchronized (this.mLock) {
            if (this.mPrinterDiscoverySession == null) {
                return;
            }
            this.mPrinterDiscoverySession.removeObserverLocked(observer);
        }
    }

    public void startPrinterDiscovery(android.print.IPrinterDiscoveryObserver observer, java.util.List<android.print.PrinterId> printerIds) {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mPrinterDiscoverySession == null) {
                return;
            }
            this.mPrinterDiscoverySession.startPrinterDiscoveryLocked(observer, printerIds);
        }
    }

    public void stopPrinterDiscovery(android.print.IPrinterDiscoveryObserver observer) {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mPrinterDiscoverySession == null) {
                return;
            }
            this.mPrinterDiscoverySession.stopPrinterDiscoveryLocked(observer);
        }
    }

    public void validatePrinters(java.util.List<android.print.PrinterId> printerIds) {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mActiveServices.isEmpty()) {
                return;
            }
            if (this.mPrinterDiscoverySession == null) {
                return;
            }
            this.mPrinterDiscoverySession.validatePrintersLocked(printerIds);
        }
    }

    public void startPrinterStateTracking(android.print.PrinterId printerId) {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mActiveServices.isEmpty()) {
                return;
            }
            if (this.mPrinterDiscoverySession == null) {
                return;
            }
            this.mPrinterDiscoverySession.startPrinterStateTrackingLocked(printerId);
        }
    }

    public void stopPrinterStateTracking(android.print.PrinterId printerId) {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mActiveServices.isEmpty()) {
                return;
            }
            if (this.mPrinterDiscoverySession == null) {
                return;
            }
            this.mPrinterDiscoverySession.stopPrinterStateTrackingLocked(printerId);
        }
    }

    public void addPrintJobStateChangeListener(android.print.IPrintJobStateChangeListener listener, int appId) throws android.os.RemoteException {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mPrintJobStateChangeListenerRecords == null) {
                this.mPrintJobStateChangeListenerRecords = new java.util.ArrayList();
            }
            this.mPrintJobStateChangeListenerRecords.add(new com.android.server.print.UserState.PrintJobStateChangeListenerRecord(listener, appId) { // from class: com.android.server.print.UserState.2
                @Override // com.android.server.print.UserState.PrintJobStateChangeListenerRecord
                public void onBinderDied() {
                    synchronized (com.android.server.print.UserState.this.mLock) {
                        if (com.android.server.print.UserState.this.mPrintJobStateChangeListenerRecords != null) {
                            com.android.server.print.UserState.this.mPrintJobStateChangeListenerRecords.remove(this);
                        }
                    }
                }
            });
        }
    }

    public void removePrintJobStateChangeListener(android.print.IPrintJobStateChangeListener listener) {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mPrintJobStateChangeListenerRecords == null) {
                return;
            }
            int recordCount = this.mPrintJobStateChangeListenerRecords.size();
            int i = 0;
            while (true) {
                if (i >= recordCount) {
                    break;
                }
                com.android.server.print.UserState.PrintJobStateChangeListenerRecord record = this.mPrintJobStateChangeListenerRecords.get(i);
                if (!record.listener.asBinder().equals(listener.asBinder())) {
                    i++;
                } else {
                    record.destroy();
                    this.mPrintJobStateChangeListenerRecords.remove(i);
                    break;
                }
            }
            if (this.mPrintJobStateChangeListenerRecords.isEmpty()) {
                this.mPrintJobStateChangeListenerRecords = null;
            }
        }
    }

    public void addPrintServicesChangeListener(android.print.IPrintServicesChangeListener listener) throws android.os.RemoteException {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mPrintServicesChangeListenerRecords == null) {
                this.mPrintServicesChangeListenerRecords = new java.util.ArrayList();
            }
            this.mPrintServicesChangeListenerRecords.add(new com.android.server.print.UserState.ListenerRecord<android.print.IPrintServicesChangeListener>(listener) { // from class: com.android.server.print.UserState.3
                @Override // com.android.server.print.UserState.ListenerRecord
                public void onBinderDied() {
                    synchronized (com.android.server.print.UserState.this.mLock) {
                        if (com.android.server.print.UserState.this.mPrintServicesChangeListenerRecords != null) {
                            com.android.server.print.UserState.this.mPrintServicesChangeListenerRecords.remove(this);
                        }
                    }
                }
            });
        }
    }

    public void removePrintServicesChangeListener(android.print.IPrintServicesChangeListener listener) {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mPrintServicesChangeListenerRecords == null) {
                return;
            }
            int recordCount = this.mPrintServicesChangeListenerRecords.size();
            int i = 0;
            while (true) {
                if (i >= recordCount) {
                    break;
                }
                com.android.server.print.UserState.ListenerRecord<android.print.IPrintServicesChangeListener> record = this.mPrintServicesChangeListenerRecords.get(i);
                if (!record.listener.asBinder().equals(listener.asBinder())) {
                    i++;
                } else {
                    record.destroy();
                    this.mPrintServicesChangeListenerRecords.remove(i);
                    break;
                }
            }
            if (this.mPrintServicesChangeListenerRecords.isEmpty()) {
                this.mPrintServicesChangeListenerRecords = null;
            }
        }
    }

    public void addPrintServiceRecommendationsChangeListener(android.printservice.recommendation.IRecommendationsChangeListener listener) throws android.os.RemoteException {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mPrintServiceRecommendationsChangeListenerRecords == null) {
                this.mPrintServiceRecommendationsChangeListenerRecords = new java.util.ArrayList();
                this.mPrintServiceRecommendationsService = new com.android.server.print.RemotePrintServiceRecommendationService(this.mContext, android.os.UserHandle.of(this.mUserId), this);
            }
            this.mPrintServiceRecommendationsChangeListenerRecords.add(new com.android.server.print.UserState.ListenerRecord<android.printservice.recommendation.IRecommendationsChangeListener>(listener) { // from class: com.android.server.print.UserState.4
                @Override // com.android.server.print.UserState.ListenerRecord
                public void onBinderDied() {
                    synchronized (com.android.server.print.UserState.this.mLock) {
                        if (com.android.server.print.UserState.this.mPrintServiceRecommendationsChangeListenerRecords != null) {
                            com.android.server.print.UserState.this.mPrintServiceRecommendationsChangeListenerRecords.remove(this);
                        }
                    }
                }
            });
        }
    }

    public void removePrintServiceRecommendationsChangeListener(android.printservice.recommendation.IRecommendationsChangeListener listener) {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mPrintServiceRecommendationsChangeListenerRecords == null) {
                return;
            }
            int recordCount = this.mPrintServiceRecommendationsChangeListenerRecords.size();
            int i = 0;
            while (true) {
                if (i >= recordCount) {
                    break;
                }
                com.android.server.print.UserState.ListenerRecord<android.printservice.recommendation.IRecommendationsChangeListener> record = this.mPrintServiceRecommendationsChangeListenerRecords.get(i);
                if (!record.listener.asBinder().equals(listener.asBinder())) {
                    i++;
                } else {
                    record.destroy();
                    this.mPrintServiceRecommendationsChangeListenerRecords.remove(i);
                    break;
                }
            }
            if (this.mPrintServiceRecommendationsChangeListenerRecords.isEmpty()) {
                this.mPrintServiceRecommendationsChangeListenerRecords = null;
                this.mPrintServiceRecommendations = null;
                this.mPrintServiceRecommendationsService.close();
                this.mPrintServiceRecommendationsService = null;
            }
        }
    }

    @Override // com.android.server.print.RemotePrintSpooler.PrintSpoolerCallbacks
    public void onPrintJobStateChanged(android.print.PrintJobInfo printJob) {
        this.mPrintJobForAppCache.onPrintJobStateChanged(printJob);
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.print.UserState$$ExternalSyntheticLambda3
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.print.UserState) obj).handleDispatchPrintJobStateChanged((android.print.PrintJobId) obj2, (com.android.internal.util.function.pooled.PooledSupplier.OfInt) obj3);
            }
        }, this, printJob.getId(), com.android.internal.util.function.pooled.PooledLambda.obtainSupplier(printJob.getAppId()).recycleOnUse()));
    }

    public void onPrintServicesChanged() {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.print.UserState$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.print.UserState) obj).handleDispatchPrintServicesChanged();
            }
        }, this));
    }

    @Override // com.android.server.print.RemotePrintServiceRecommendationService.RemotePrintServiceRecommendationServiceCallbacks
    public void onPrintServiceRecommendationsUpdated(java.util.List<android.printservice.recommendation.RecommendationInfo> recommendations) {
        android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.UserState$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.print.UserState) obj).handleDispatchPrintServiceRecommendationsUpdated((java.util.List) obj2);
            }
        }, this, recommendations));
    }

    @Override // com.android.server.print.RemotePrintService.PrintServiceCallbacks
    public void onPrintersAdded(java.util.List<android.print.PrinterInfo> printers) {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mActiveServices.isEmpty()) {
                return;
            }
            if (this.mPrinterDiscoverySession == null) {
                return;
            }
            this.mPrinterDiscoverySession.onPrintersAddedLocked(printers);
        }
    }

    @Override // com.android.server.print.RemotePrintService.PrintServiceCallbacks
    public void onPrintersRemoved(java.util.List<android.print.PrinterId> printerIds) {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mActiveServices.isEmpty()) {
                return;
            }
            if (this.mPrinterDiscoverySession == null) {
                return;
            }
            this.mPrinterDiscoverySession.onPrintersRemovedLocked(printerIds);
        }
    }

    @Override // com.android.server.print.RemotePrintService.PrintServiceCallbacks
    public void onCustomPrinterIconLoaded(android.print.PrinterId printerId, android.graphics.drawable.Icon icon) {
        this.mSpooler.onCustomPrinterIconLoaded(printerId, icon);
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mPrinterDiscoverySession == null) {
                return;
            }
            this.mPrinterDiscoverySession.onCustomPrinterIconLoadedLocked(printerId);
        }
    }

    @Override // com.android.server.print.RemotePrintService.PrintServiceCallbacks
    public void onServiceDied(com.android.server.print.RemotePrintService service) {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            if (this.mActiveServices.isEmpty()) {
                return;
            }
            failActivePrintJobsForService(service.getComponentName());
            service.onAllPrintJobsHandled();
            this.mActiveServices.remove(service.getComponentName());
            android.os.Handler.getMain().sendMessageDelayed(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.print.UserState$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.print.UserState) obj).onConfigurationChanged();
                }
            }, this), 500L);
            if (this.mPrinterDiscoverySession == null) {
                return;
            }
            this.mPrinterDiscoverySession.onServiceDiedLocked(service);
        }
    }

    public void updateIfNeededLocked() {
        throwIfDestroyedLocked();
        readConfigurationLocked();
        onConfigurationChangedLocked();
    }

    public void destroyLocked() {
        throwIfDestroyedLocked();
        this.mSpooler.destroy();
        for (com.android.server.print.RemotePrintService service : this.mActiveServices.values()) {
            service.destroy();
        }
        this.mActiveServices.clear();
        this.mInstalledServices.clear();
        this.mDisabledServices.clear();
        if (this.mPrinterDiscoverySession != null) {
            this.mPrinterDiscoverySession.destroyLocked();
            this.mPrinterDiscoverySession = null;
        }
        this.mDestroyed = true;
    }

    public void dump(com.android.internal.util.dump.DualDumpOutputStream dumpStream) {
        synchronized (this.mLock) {
            dumpStream.write("user_id", 1120986464257L, this.mUserId);
            int installedServiceCount = this.mInstalledServices.size();
            for (int i = 0; i < installedServiceCount; i++) {
                long token = dumpStream.start("installed_services", 2246267895810L);
                android.printservice.PrintServiceInfo installedService = this.mInstalledServices.get(i);
                android.content.pm.ResolveInfo resolveInfo = installedService.getResolveInfo();
                com.android.internal.util.dump.DumpUtils.writeComponentName(dumpStream, "component_name", 1146756268033L, new android.content.ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name));
                com.android.internal.util.dump.DumpUtils.writeStringIfNotNull(dumpStream, "settings_activity", 1138166333442L, installedService.getSettingsActivityName());
                com.android.internal.util.dump.DumpUtils.writeStringIfNotNull(dumpStream, "add_printers_activity", 1138166333443L, installedService.getAddPrintersActivityName());
                com.android.internal.util.dump.DumpUtils.writeStringIfNotNull(dumpStream, "advanced_options_activity", 1138166333444L, installedService.getAdvancedOptionsActivityName());
                dumpStream.end(token);
            }
            for (android.content.ComponentName disabledService : this.mDisabledServices) {
                com.android.internal.util.dump.DumpUtils.writeComponentName(dumpStream, "disabled_services", 2246267895811L, disabledService);
            }
            int activeServiceCount = this.mActiveServices.size();
            for (int i2 = 0; i2 < activeServiceCount; i2++) {
                long token2 = dumpStream.start("actives_services", 2246267895812L);
                this.mActiveServices.valueAt(i2).dump(dumpStream);
                dumpStream.end(token2);
            }
            this.mPrintJobForAppCache.dumpLocked(dumpStream);
            if (this.mPrinterDiscoverySession != null) {
                long token3 = dumpStream.start("discovery_service", 2246267895814L);
                this.mPrinterDiscoverySession.dumpLocked(dumpStream);
                dumpStream.end(token3);
            }
        }
        long token4 = dumpStream.start("print_spooler_state", 1146756268039L);
        this.mSpooler.dump(dumpStream);
        dumpStream.end(token4);
    }

    private void readConfigurationLocked() {
        readInstalledPrintServicesLocked();
        readDisabledPrintServicesLocked();
    }

    private void readInstalledPrintServicesLocked() {
        java.util.Set<android.printservice.PrintServiceInfo> tempPrintServices = new java.util.HashSet<>();
        int queryIntentFlags = this.mIsInstantServiceAllowed ? 268435588 | 8388608 : 268435588;
        java.util.List<android.content.pm.ResolveInfo> installedServices = this.mContext.getPackageManager().queryIntentServicesAsUser(this.mQueryIntent, queryIntentFlags, this.mUserId);
        int installedCount = installedServices.size();
        for (int i = 0; i < installedCount; i++) {
            android.content.pm.ResolveInfo installedService = installedServices.get(i);
            if (!"android.permission.BIND_PRINT_SERVICE".equals(installedService.serviceInfo.permission)) {
                android.content.ComponentName serviceName = new android.content.ComponentName(installedService.serviceInfo.packageName, installedService.serviceInfo.name);
                android.util.Slog.w(LOG_TAG, "Skipping print service " + serviceName.flattenToShortString() + " since it does not require permission android.permission.BIND_PRINT_SERVICE");
            } else {
                tempPrintServices.add(android.printservice.PrintServiceInfo.create(this.mContext, installedService));
            }
        }
        this.mInstalledServices.clear();
        this.mInstalledServices.addAll(tempPrintServices);
    }

    private void upgradePersistentStateIfNeeded() {
        java.lang.String enabledSettingValue = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "enabled_print_services", this.mUserId);
        if (enabledSettingValue != null) {
            java.util.Set<android.content.ComponentName> enabledServiceNameSet = new java.util.HashSet<>();
            readPrintServicesFromSettingLocked("enabled_print_services", enabledServiceNameSet);
            android.util.ArraySet<android.content.ComponentName> disabledServices = new android.util.ArraySet<>();
            int numInstalledServices = this.mInstalledServices.size();
            for (int i = 0; i < numInstalledServices; i++) {
                android.content.ComponentName serviceName = this.mInstalledServices.get(i).getComponentName();
                if (!enabledServiceNameSet.contains(serviceName)) {
                    disabledServices.add(serviceName);
                }
            }
            writeDisabledPrintServicesLocked(disabledServices);
            android.provider.Settings.Secure.putStringForUser(this.mContext.getContentResolver(), "enabled_print_services", null, this.mUserId);
        }
    }

    private void readDisabledPrintServicesLocked() {
        java.util.Set<android.content.ComponentName> tempDisabledServiceNameSet = new java.util.HashSet<>();
        readPrintServicesFromSettingLocked("disabled_print_services", tempDisabledServiceNameSet);
        if (!tempDisabledServiceNameSet.equals(this.mDisabledServices)) {
            this.mDisabledServices.clear();
            this.mDisabledServices.addAll(tempDisabledServiceNameSet);
        }
    }

    private void readPrintServicesFromSettingLocked(java.lang.String setting, java.util.Set<android.content.ComponentName> outServiceNames) {
        android.content.ComponentName componentName;
        java.lang.String settingValue = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), setting, this.mUserId);
        if (!android.text.TextUtils.isEmpty(settingValue)) {
            android.text.TextUtils.SimpleStringSplitter splitter = this.mStringColonSplitter;
            splitter.setString(settingValue);
            while (splitter.hasNext()) {
                java.lang.String string = splitter.next();
                if (!android.text.TextUtils.isEmpty(string) && (componentName = android.content.ComponentName.unflattenFromString(string)) != null) {
                    outServiceNames.add(componentName);
                }
            }
        }
    }

    private void writeDisabledPrintServicesLocked(java.util.Set<android.content.ComponentName> disabledServices) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        for (android.content.ComponentName componentName : disabledServices) {
            if (builder.length() > 0) {
                builder.append(COMPONENT_NAME_SEPARATOR);
            }
            builder.append(componentName.flattenToShortString());
        }
        android.provider.Settings.Secure.putStringForUser(this.mContext.getContentResolver(), "disabled_print_services", builder.toString(), this.mUserId);
    }

    private java.util.ArrayList<android.content.ComponentName> getInstalledComponents() {
        java.util.ArrayList<android.content.ComponentName> installedComponents = new java.util.ArrayList<>();
        int installedCount = this.mInstalledServices.size();
        for (int i = 0; i < installedCount; i++) {
            android.content.pm.ResolveInfo resolveInfo = this.mInstalledServices.get(i).getResolveInfo();
            android.content.ComponentName serviceName = new android.content.ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
            installedComponents.add(serviceName);
        }
        return installedComponents;
    }

    public void prunePrintServices() {
        java.util.ArrayList<android.content.ComponentName> installedComponents;
        synchronized (this.mLock) {
            installedComponents = getInstalledComponents();
            boolean disabledServicesUninstalled = this.mDisabledServices.retainAll(installedComponents);
            if (disabledServicesUninstalled) {
                writeDisabledPrintServicesLocked(this.mDisabledServices);
            }
        }
        this.mSpooler.pruneApprovedPrintServices(installedComponents);
    }

    private void onConfigurationChangedLocked() {
        java.util.ArrayList<android.content.ComponentName> installedComponents = getInstalledComponents();
        int installedCount = installedComponents.size();
        for (int i = 0; i < installedCount; i++) {
            android.content.ComponentName serviceName = installedComponents.get(i);
            if (!this.mDisabledServices.contains(serviceName)) {
                if (!this.mActiveServices.containsKey(serviceName)) {
                    com.android.server.print.RemotePrintService service = new com.android.server.print.RemotePrintService(this.mContext, serviceName, this.mUserId, this.mSpooler, this);
                    addServiceLocked(service);
                }
            } else {
                com.android.server.print.RemotePrintService service2 = this.mActiveServices.remove(serviceName);
                if (service2 != null) {
                    removeServiceLocked(service2);
                }
            }
        }
        java.util.Iterator<java.util.Map.Entry<android.content.ComponentName, com.android.server.print.RemotePrintService>> iterator = this.mActiveServices.entrySet().iterator();
        while (iterator.hasNext()) {
            java.util.Map.Entry<android.content.ComponentName, com.android.server.print.RemotePrintService> entry = iterator.next();
            android.content.ComponentName serviceName2 = entry.getKey();
            com.android.server.print.RemotePrintService service3 = entry.getValue();
            if (!installedComponents.contains(serviceName2)) {
                removeServiceLocked(service3);
                iterator.remove();
            }
        }
        onPrintServicesChanged();
    }

    private void addServiceLocked(com.android.server.print.RemotePrintService service) {
        this.mActiveServices.put(service.getComponentName(), service);
        if (this.mPrinterDiscoverySession != null) {
            this.mPrinterDiscoverySession.onServiceAddedLocked(service);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeServiceLocked(com.android.server.print.RemotePrintService service) {
        failActivePrintJobsForService(service.getComponentName());
        if (this.mPrinterDiscoverySession != null) {
            this.mPrinterDiscoverySession.onServiceRemovedLocked(service);
        } else {
            service.destroy();
        }
    }

    private void failActivePrintJobsForService(android.content.ComponentName serviceName) {
        if (android.os.Looper.getMainLooper().isCurrentThread()) {
            com.android.internal.os.BackgroundThread.getHandler().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.UserState$$ExternalSyntheticLambda2
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.print.UserState) obj).failScheduledPrintJobsForServiceInternal((android.content.ComponentName) obj2);
                }
            }, this, serviceName));
        } else {
            failScheduledPrintJobsForServiceInternal(serviceName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void failScheduledPrintJobsForServiceInternal(android.content.ComponentName serviceName) {
        java.util.List<android.print.PrintJobInfo> printJobs = this.mSpooler.getPrintJobInfos(serviceName, -4, -2);
        if (printJobs == null) {
            return;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            int printJobCount = printJobs.size();
            for (int i = 0; i < printJobCount; i++) {
                android.print.PrintJobInfo printJob = printJobs.get(i);
                this.mSpooler.setPrintJobState(printJob.getId(), 6, this.mContext.getString(android.R.string.postalTypeWork));
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private void throwIfDestroyedLocked() {
        if (this.mDestroyed) {
            throw new java.lang.IllegalStateException("Cannot interact with a destroyed instance.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDispatchPrintJobStateChanged(android.print.PrintJobId printJobId, java.util.function.IntSupplier appIdSupplier) {
        int appId = appIdSupplier.getAsInt();
        synchronized (this.mLock) {
            if (this.mPrintJobStateChangeListenerRecords == null) {
                return;
            }
            java.util.List<com.android.server.print.UserState.PrintJobStateChangeListenerRecord> records = new java.util.ArrayList<>(this.mPrintJobStateChangeListenerRecords);
            int recordCount = records.size();
            for (int i = 0; i < recordCount; i++) {
                com.android.server.print.UserState.PrintJobStateChangeListenerRecord record = records.get(i);
                if (record.appId == -2 || record.appId == appId) {
                    try {
                        record.listener.onPrintJobStateChanged(printJobId);
                    } catch (android.os.RemoteException re) {
                        android.util.Log.e(LOG_TAG, "Error notifying for print job state change", re);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDispatchPrintServicesChanged() {
        synchronized (this.mLock) {
            if (this.mPrintServicesChangeListenerRecords == null) {
                return;
            }
            java.util.List<com.android.server.print.UserState.ListenerRecord<android.print.IPrintServicesChangeListener>> records = new java.util.ArrayList<>(this.mPrintServicesChangeListenerRecords);
            int recordCount = records.size();
            for (int i = 0; i < recordCount; i++) {
                com.android.server.print.UserState.ListenerRecord<android.print.IPrintServicesChangeListener> record = records.get(i);
                try {
                    record.listener.onPrintServicesChanged();
                } catch (android.os.RemoteException re) {
                    android.util.Log.e(LOG_TAG, "Error notifying for print services change", re);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDispatchPrintServiceRecommendationsUpdated(java.util.List<android.printservice.recommendation.RecommendationInfo> recommendations) {
        synchronized (this.mLock) {
            if (this.mPrintServiceRecommendationsChangeListenerRecords == null) {
                return;
            }
            java.util.List<com.android.server.print.UserState.ListenerRecord<android.printservice.recommendation.IRecommendationsChangeListener>> records = new java.util.ArrayList<>(this.mPrintServiceRecommendationsChangeListenerRecords);
            this.mPrintServiceRecommendations = recommendations;
            int recordCount = records.size();
            for (int i = 0; i < recordCount; i++) {
                com.android.server.print.UserState.ListenerRecord<android.printservice.recommendation.IRecommendationsChangeListener> record = records.get(i);
                try {
                    record.listener.onRecommendationsChanged();
                } catch (android.os.RemoteException re) {
                    android.util.Log.e(LOG_TAG, "Error notifying for print service recommendations change", re);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConfigurationChanged() {
        synchronized (this.mLock) {
            onConfigurationChangedLocked();
        }
    }

    public boolean getBindInstantServiceAllowed() {
        return this.mIsInstantServiceAllowed;
    }

    public void setBindInstantServiceAllowed(boolean allowed) {
        synchronized (this.mLock) {
            this.mIsInstantServiceAllowed = allowed;
            updateIfNeededLocked();
        }
    }

    private abstract class PrintJobStateChangeListenerRecord implements android.os.IBinder.DeathRecipient {
        final int appId;
        final android.print.IPrintJobStateChangeListener listener;

        public abstract void onBinderDied();

        public PrintJobStateChangeListenerRecord(android.print.IPrintJobStateChangeListener listener, int appId) throws android.os.RemoteException {
            this.listener = listener;
            this.appId = appId;
            listener.asBinder().linkToDeath(this, 0);
        }

        public void destroy() {
            this.listener.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.listener.asBinder().unlinkToDeath(this, 0);
            onBinderDied();
        }
    }

    private abstract class ListenerRecord<T extends android.os.IInterface> implements android.os.IBinder.DeathRecipient {
        final T listener;

        public abstract void onBinderDied();

        public ListenerRecord(T listener) throws android.os.RemoteException {
            this.listener = listener;
            listener.asBinder().linkToDeath(this, 0);
        }

        public void destroy() {
            this.listener.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.listener.asBinder().unlinkToDeath(this, 0);
            onBinderDied();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class PrinterDiscoverySessionMediator {
        private boolean mIsDestroyed;
        private final android.util.ArrayMap<android.print.PrinterId, android.print.PrinterInfo> mPrinters = new android.util.ArrayMap<>();
        private final android.os.RemoteCallbackList<android.print.IPrinterDiscoveryObserver> mDiscoveryObservers = new android.os.RemoteCallbackList<android.print.IPrinterDiscoveryObserver>() { // from class: com.android.server.print.UserState.PrinterDiscoverySessionMediator.1
            @Override // android.os.RemoteCallbackList
            public void onCallbackDied(android.print.IPrinterDiscoveryObserver observer) {
                synchronized (com.android.server.print.UserState.this.mLock) {
                    com.android.server.print.UserState.PrinterDiscoverySessionMediator.this.stopPrinterDiscoveryLocked(observer);
                    com.android.server.print.UserState.PrinterDiscoverySessionMediator.this.removeObserverLocked(observer);
                }
            }
        };
        private final java.util.List<android.os.IBinder> mStartedPrinterDiscoveryTokens = new java.util.ArrayList();
        private final java.util.List<android.print.PrinterId> mStateTrackedPrinters = new java.util.ArrayList();

        PrinterDiscoverySessionMediator() {
            android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.print.UserState.PrinterDiscoverySessionMediator) obj).handleDispatchCreatePrinterDiscoverySession((java.util.ArrayList) obj2);
                }
            }, this, new java.util.ArrayList(com.android.server.print.UserState.this.mActiveServices.values())));
        }

        public void addObserverLocked(android.print.IPrinterDiscoveryObserver observer) {
            this.mDiscoveryObservers.register(observer);
            if (!this.mPrinters.isEmpty()) {
                android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda8
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                        ((com.android.server.print.UserState.PrinterDiscoverySessionMediator) obj).handlePrintersAdded((android.print.IPrinterDiscoveryObserver) obj2, (java.util.ArrayList) obj3);
                    }
                }, this, observer, new java.util.ArrayList(this.mPrinters.values())));
            }
        }

        public void removeObserverLocked(android.print.IPrinterDiscoveryObserver observer) {
            this.mDiscoveryObservers.unregister(observer);
            if (this.mDiscoveryObservers.getRegisteredCallbackCount() == 0) {
                destroyLocked();
            }
        }

        public final void startPrinterDiscoveryLocked(android.print.IPrinterDiscoveryObserver observer, java.util.List<android.print.PrinterId> priorityList) {
            if (this.mIsDestroyed) {
                android.util.Log.w(com.android.server.print.UserState.LOG_TAG, "Not starting dicovery - session destroyed");
                return;
            }
            boolean discoveryStarted = !this.mStartedPrinterDiscoveryTokens.isEmpty();
            this.mStartedPrinterDiscoveryTokens.add(observer.asBinder());
            if (discoveryStarted && priorityList != null && !priorityList.isEmpty()) {
                com.android.server.print.UserState.this.validatePrinters(priorityList);
            } else {
                if (this.mStartedPrinterDiscoveryTokens.size() > 1) {
                    return;
                }
                android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda6
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                        ((com.android.server.print.UserState.PrinterDiscoverySessionMediator) obj).handleDispatchStartPrinterDiscovery((java.util.ArrayList) obj2, (java.util.List) obj3);
                    }
                }, this, new java.util.ArrayList(com.android.server.print.UserState.this.mActiveServices.values()), priorityList));
            }
        }

        public final void stopPrinterDiscoveryLocked(android.print.IPrinterDiscoveryObserver observer) {
            if (this.mIsDestroyed) {
                android.util.Log.w(com.android.server.print.UserState.LOG_TAG, "Not stopping dicovery - session destroyed");
            } else {
                if (!this.mStartedPrinterDiscoveryTokens.remove(observer.asBinder()) || !this.mStartedPrinterDiscoveryTokens.isEmpty()) {
                    return;
                }
                android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda10
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((com.android.server.print.UserState.PrinterDiscoverySessionMediator) obj).handleDispatchStopPrinterDiscovery((java.util.ArrayList) obj2);
                    }
                }, this, new java.util.ArrayList(com.android.server.print.UserState.this.mActiveServices.values())));
            }
        }

        public void validatePrintersLocked(java.util.List<android.print.PrinterId> printerIds) {
            if (this.mIsDestroyed) {
                android.util.Log.w(com.android.server.print.UserState.LOG_TAG, "Not validating pritners - session destroyed");
                return;
            }
            java.util.List<android.print.PrinterId> remainingList = new java.util.ArrayList<>(printerIds);
            while (!remainingList.isEmpty()) {
                java.util.Iterator<android.print.PrinterId> iterator = remainingList.iterator();
                java.util.List<android.print.PrinterId> updateList = new java.util.ArrayList<>();
                android.content.ComponentName serviceName = null;
                while (iterator.hasNext()) {
                    android.print.PrinterId printerId = iterator.next();
                    if (printerId != null) {
                        if (updateList.isEmpty()) {
                            updateList.add(printerId);
                            serviceName = printerId.getServiceName();
                            iterator.remove();
                        } else if (printerId.getServiceName().equals(serviceName)) {
                            updateList.add(printerId);
                            iterator.remove();
                        }
                    }
                }
                com.android.server.print.RemotePrintService service = (com.android.server.print.RemotePrintService) com.android.server.print.UserState.this.mActiveServices.get(serviceName);
                if (service != null) {
                    android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda12
                        public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                            ((com.android.server.print.UserState.PrinterDiscoverySessionMediator) obj).handleValidatePrinters((com.android.server.print.RemotePrintService) obj2, (java.util.List) obj3);
                        }
                    }, this, service, updateList));
                }
            }
        }

        public final void startPrinterStateTrackingLocked(android.print.PrinterId printerId) {
            com.android.server.print.RemotePrintService service;
            if (this.mIsDestroyed) {
                android.util.Log.w(com.android.server.print.UserState.LOG_TAG, "Not starting printer state tracking - session destroyed");
                return;
            }
            if (this.mStartedPrinterDiscoveryTokens.isEmpty()) {
                return;
            }
            boolean containedPrinterId = this.mStateTrackedPrinters.contains(printerId);
            this.mStateTrackedPrinters.add(printerId);
            if (containedPrinterId || (service = (com.android.server.print.RemotePrintService) com.android.server.print.UserState.this.mActiveServices.get(printerId.getServiceName())) == null) {
                return;
            }
            android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda11
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                    ((com.android.server.print.UserState.PrinterDiscoverySessionMediator) obj).handleStartPrinterStateTracking((com.android.server.print.RemotePrintService) obj2, (android.print.PrinterId) obj3);
                }
            }, this, service, printerId));
        }

        public final void stopPrinterStateTrackingLocked(android.print.PrinterId printerId) {
            com.android.server.print.RemotePrintService service;
            if (this.mIsDestroyed) {
                android.util.Log.w(com.android.server.print.UserState.LOG_TAG, "Not stopping printer state tracking - session destroyed");
            } else {
                if (this.mStartedPrinterDiscoveryTokens.isEmpty() || !this.mStateTrackedPrinters.remove(printerId) || (service = (com.android.server.print.RemotePrintService) com.android.server.print.UserState.this.mActiveServices.get(printerId.getServiceName())) == null) {
                    return;
                }
                android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda13
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                        ((com.android.server.print.UserState.PrinterDiscoverySessionMediator) obj).handleStopPrinterStateTracking((com.android.server.print.RemotePrintService) obj2, (android.print.PrinterId) obj3);
                    }
                }, this, service, printerId));
            }
        }

        public void onDestroyed() {
        }

        public void destroyLocked() {
            if (this.mIsDestroyed) {
                android.util.Log.w(com.android.server.print.UserState.LOG_TAG, "Not destroying - session destroyed");
                return;
            }
            this.mIsDestroyed = true;
            int printerCount = this.mStateTrackedPrinters.size();
            for (int i = 0; i < printerCount; i++) {
                android.print.PrinterId printerId = this.mStateTrackedPrinters.get(i);
                com.android.server.print.UserState.this.stopPrinterStateTracking(printerId);
            }
            int observerCount = this.mStartedPrinterDiscoveryTokens.size();
            for (int i2 = 0; i2 < observerCount; i2++) {
                android.os.IBinder token = this.mStartedPrinterDiscoveryTokens.get(i2);
                stopPrinterDiscoveryLocked(android.print.IPrinterDiscoveryObserver.Stub.asInterface(token));
            }
            android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda7
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.print.UserState.PrinterDiscoverySessionMediator) obj).handleDispatchDestroyPrinterDiscoverySession((java.util.ArrayList) obj2);
                }
            }, this, new java.util.ArrayList(com.android.server.print.UserState.this.mActiveServices.values())));
        }

        public void onPrintersAddedLocked(java.util.List<android.print.PrinterInfo> printers) {
            if (this.mIsDestroyed) {
                android.util.Log.w(com.android.server.print.UserState.LOG_TAG, "Not adding printers - session destroyed");
                return;
            }
            java.util.List<android.print.PrinterInfo> addedPrinters = null;
            int addedPrinterCount = printers.size();
            for (int i = 0; i < addedPrinterCount; i++) {
                android.print.PrinterInfo printer = printers.get(i);
                android.print.PrinterInfo oldPrinter = this.mPrinters.put(printer.getId(), printer);
                if (oldPrinter == null || !oldPrinter.equals(printer)) {
                    if (addedPrinters == null) {
                        addedPrinters = new java.util.ArrayList<>();
                    }
                    addedPrinters.add(printer);
                }
            }
            if (addedPrinters != null) {
                android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda9
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((com.android.server.print.UserState.PrinterDiscoverySessionMediator) obj).handleDispatchPrintersAdded((java.util.List) obj2);
                    }
                }, this, addedPrinters));
            }
        }

        public void onPrintersRemovedLocked(java.util.List<android.print.PrinterId> printerIds) {
            if (this.mIsDestroyed) {
                android.util.Log.w(com.android.server.print.UserState.LOG_TAG, "Not removing printers - session destroyed");
                return;
            }
            java.util.List<android.print.PrinterId> removedPrinterIds = null;
            int removedPrinterCount = printerIds.size();
            for (int i = 0; i < removedPrinterCount; i++) {
                android.print.PrinterId removedPrinterId = printerIds.get(i);
                if (this.mPrinters.remove(removedPrinterId) != null) {
                    if (removedPrinterIds == null) {
                        removedPrinterIds = new java.util.ArrayList<>();
                    }
                    removedPrinterIds.add(removedPrinterId);
                }
            }
            if (removedPrinterIds != null) {
                android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda5(), this, removedPrinterIds));
            }
        }

        public void onServiceRemovedLocked(com.android.server.print.RemotePrintService service) {
            if (this.mIsDestroyed) {
                android.util.Log.w(com.android.server.print.UserState.LOG_TAG, "Not updating removed service - session destroyed");
                return;
            }
            android.content.ComponentName serviceName = service.getComponentName();
            removePrintersForServiceLocked(serviceName);
            service.destroy();
        }

        public void onCustomPrinterIconLoadedLocked(android.print.PrinterId printerId) {
            if (this.mIsDestroyed) {
                android.util.Log.w(com.android.server.print.UserState.LOG_TAG, "Not updating printer - session destroyed");
                return;
            }
            android.print.PrinterInfo printer = this.mPrinters.get(printerId);
            if (printer != null) {
                android.print.PrinterInfo newPrinter = new android.print.PrinterInfo.Builder(printer).incCustomPrinterIconGen().build();
                this.mPrinters.put(printerId, newPrinter);
                java.util.ArrayList<android.print.PrinterInfo> addedPrinters = new java.util.ArrayList<>(1);
                addedPrinters.add(newPrinter);
                android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda1
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((com.android.server.print.UserState.PrinterDiscoverySessionMediator) obj).handleDispatchPrintersAdded((java.util.ArrayList) obj2);
                    }
                }, this, addedPrinters));
            }
        }

        public void onServiceDiedLocked(com.android.server.print.RemotePrintService service) {
            com.android.server.print.UserState.this.removeServiceLocked(service);
        }

        public void onServiceAddedLocked(com.android.server.print.RemotePrintService service) {
            if (this.mIsDestroyed) {
                android.util.Log.w(com.android.server.print.UserState.LOG_TAG, "Not updating added service - session destroyed");
                return;
            }
            android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.print.RemotePrintService) obj).createPrinterDiscoverySession();
                }
            }, service));
            if (!this.mStartedPrinterDiscoveryTokens.isEmpty()) {
                android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda3
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((com.android.server.print.RemotePrintService) obj).startPrinterDiscovery((java.util.List) obj2);
                    }
                }, service, (java.lang.Object) null));
            }
            int trackedPrinterCount = this.mStateTrackedPrinters.size();
            for (int i = 0; i < trackedPrinterCount; i++) {
                android.print.PrinterId printerId = this.mStateTrackedPrinters.get(i);
                if (printerId.getServiceName().equals(service.getComponentName())) {
                    android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda4
                        @Override // java.util.function.BiConsumer
                        public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                            ((com.android.server.print.RemotePrintService) obj).startPrinterStateTracking((android.print.PrinterId) obj2);
                        }
                    }, service, printerId));
                }
            }
        }

        public void dumpLocked(com.android.internal.util.dump.DualDumpOutputStream dumpStream) {
            dumpStream.write("is_destroyed", 1133871366145L, com.android.server.print.UserState.this.mDestroyed);
            dumpStream.write("is_printer_discovery_in_progress", 1133871366146L, !this.mStartedPrinterDiscoveryTokens.isEmpty());
            int observerCount = this.mDiscoveryObservers.beginBroadcast();
            for (int i = 0; i < observerCount; i++) {
                android.print.IPrinterDiscoveryObserver observer = this.mDiscoveryObservers.getBroadcastItem(i);
                dumpStream.write("printer_discovery_observers", 2237677961219L, observer.toString());
            }
            this.mDiscoveryObservers.finishBroadcast();
            int tokenCount = this.mStartedPrinterDiscoveryTokens.size();
            for (int i2 = 0; i2 < tokenCount; i2++) {
                android.os.IBinder token = this.mStartedPrinterDiscoveryTokens.get(i2);
                dumpStream.write("discovery_requests", 2237677961220L, token.toString());
            }
            int trackedPrinters = this.mStateTrackedPrinters.size();
            for (int i3 = 0; i3 < trackedPrinters; i3++) {
                android.print.PrinterId printer = this.mStateTrackedPrinters.get(i3);
                com.android.internal.print.DumpUtils.writePrinterId(dumpStream, "tracked_printer_requests", 2246267895813L, printer);
            }
            int printerCount = this.mPrinters.size();
            for (int i4 = 0; i4 < printerCount; i4++) {
                android.print.PrinterInfo printer2 = this.mPrinters.valueAt(i4);
                com.android.internal.print.DumpUtils.writePrinterInfo(com.android.server.print.UserState.this.mContext, dumpStream, "printer", 2246267895814L, printer2);
            }
        }

        private void removePrintersForServiceLocked(android.content.ComponentName serviceName) {
            if (this.mPrinters.isEmpty()) {
                return;
            }
            java.util.List<android.print.PrinterId> removedPrinterIds = null;
            int printerCount = this.mPrinters.size();
            for (int i = 0; i < printerCount; i++) {
                android.print.PrinterId printerId = this.mPrinters.keyAt(i);
                if (printerId.getServiceName().equals(serviceName)) {
                    if (removedPrinterIds == null) {
                        removedPrinterIds = new java.util.ArrayList<>();
                    }
                    removedPrinterIds.add(printerId);
                }
            }
            if (removedPrinterIds != null) {
                int removedPrinterCount = removedPrinterIds.size();
                for (int i2 = 0; i2 < removedPrinterCount; i2++) {
                    this.mPrinters.remove(removedPrinterIds.get(i2));
                }
                android.os.Handler.getMain().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.print.UserState$PrinterDiscoverySessionMediator$$ExternalSyntheticLambda5(), this, removedPrinterIds));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleDispatchPrintersAdded(java.util.List<android.print.PrinterInfo> addedPrinters) {
            int observerCount = this.mDiscoveryObservers.beginBroadcast();
            for (int i = 0; i < observerCount; i++) {
                android.print.IPrinterDiscoveryObserver observer = (android.print.IPrinterDiscoveryObserver) this.mDiscoveryObservers.getBroadcastItem(i);
                handlePrintersAdded(observer, addedPrinters);
            }
            this.mDiscoveryObservers.finishBroadcast();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleDispatchPrintersRemoved(java.util.List<android.print.PrinterId> removedPrinterIds) {
            int observerCount = this.mDiscoveryObservers.beginBroadcast();
            for (int i = 0; i < observerCount; i++) {
                android.print.IPrinterDiscoveryObserver observer = (android.print.IPrinterDiscoveryObserver) this.mDiscoveryObservers.getBroadcastItem(i);
                handlePrintersRemoved(observer, removedPrinterIds);
            }
            this.mDiscoveryObservers.finishBroadcast();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleDispatchCreatePrinterDiscoverySession(java.util.List<com.android.server.print.RemotePrintService> services) {
            int serviceCount = services.size();
            for (int i = 0; i < serviceCount; i++) {
                com.android.server.print.RemotePrintService service = services.get(i);
                service.createPrinterDiscoverySession();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleDispatchDestroyPrinterDiscoverySession(java.util.List<com.android.server.print.RemotePrintService> services) {
            int serviceCount = services.size();
            for (int i = 0; i < serviceCount; i++) {
                com.android.server.print.RemotePrintService service = services.get(i);
                service.destroyPrinterDiscoverySession();
            }
            onDestroyed();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleDispatchStartPrinterDiscovery(java.util.List<com.android.server.print.RemotePrintService> services, java.util.List<android.print.PrinterId> printerIds) {
            int serviceCount = services.size();
            for (int i = 0; i < serviceCount; i++) {
                com.android.server.print.RemotePrintService service = services.get(i);
                service.startPrinterDiscovery(printerIds);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleDispatchStopPrinterDiscovery(java.util.List<com.android.server.print.RemotePrintService> services) {
            int serviceCount = services.size();
            for (int i = 0; i < serviceCount; i++) {
                com.android.server.print.RemotePrintService service = services.get(i);
                service.stopPrinterDiscovery();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleValidatePrinters(com.android.server.print.RemotePrintService service, java.util.List<android.print.PrinterId> printerIds) {
            service.validatePrinters(printerIds);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleStartPrinterStateTracking(com.android.server.print.RemotePrintService service, android.print.PrinterId printerId) {
            service.startPrinterStateTracking(printerId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleStopPrinterStateTracking(com.android.server.print.RemotePrintService service, android.print.PrinterId printerId) {
            service.stopPrinterStateTracking(printerId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handlePrintersAdded(android.print.IPrinterDiscoveryObserver observer, java.util.List<android.print.PrinterInfo> printers) {
            try {
                observer.onPrintersAdded(new android.content.pm.ParceledListSlice(printers));
            } catch (android.os.RemoteException re) {
                android.util.Log.e(com.android.server.print.UserState.LOG_TAG, "Error sending added printers", re);
            }
        }

        private void handlePrintersRemoved(android.print.IPrinterDiscoveryObserver observer, java.util.List<android.print.PrinterId> printerIds) {
            try {
                observer.onPrintersRemoved(new android.content.pm.ParceledListSlice(printerIds));
            } catch (android.os.RemoteException re) {
                android.util.Log.e(com.android.server.print.UserState.LOG_TAG, "Error sending removed printers", re);
            }
        }
    }

    private final class PrintJobForAppCache {
        private final android.util.SparseArray<java.util.List<android.print.PrintJobInfo>> mPrintJobsForRunningApp;

        private PrintJobForAppCache() {
            this.mPrintJobsForRunningApp = new android.util.SparseArray<>();
        }

        public boolean onPrintJobCreated(final android.os.IBinder creator, final int appId, android.print.PrintJobInfo printJob) {
            try {
                creator.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.print.UserState.PrintJobForAppCache.1
                    @Override // android.os.IBinder.DeathRecipient
                    public void binderDied() {
                        creator.unlinkToDeath(this, 0);
                        synchronized (com.android.server.print.UserState.this.mLock) {
                            com.android.server.print.UserState.PrintJobForAppCache.this.mPrintJobsForRunningApp.remove(appId);
                        }
                    }
                }, 0);
                synchronized (com.android.server.print.UserState.this.mLock) {
                    java.util.List<android.print.PrintJobInfo> printJobsForApp = this.mPrintJobsForRunningApp.get(appId);
                    if (printJobsForApp == null) {
                        printJobsForApp = new java.util.ArrayList();
                        this.mPrintJobsForRunningApp.put(appId, printJobsForApp);
                    }
                    printJobsForApp.add(printJob);
                }
                return true;
            } catch (android.os.RemoteException e) {
                return false;
            }
        }

        public void onPrintJobStateChanged(android.print.PrintJobInfo printJob) {
            synchronized (com.android.server.print.UserState.this.mLock) {
                java.util.List<android.print.PrintJobInfo> printJobsForApp = this.mPrintJobsForRunningApp.get(printJob.getAppId());
                if (printJobsForApp == null) {
                    return;
                }
                int printJobCount = printJobsForApp.size();
                for (int i = 0; i < printJobCount; i++) {
                    android.print.PrintJobInfo oldPrintJob = printJobsForApp.get(i);
                    if (oldPrintJob.getId().equals(printJob.getId())) {
                        printJobsForApp.set(i, printJob);
                    }
                }
            }
        }

        public android.print.PrintJobInfo getPrintJob(android.print.PrintJobId printJobId, int appId) {
            synchronized (com.android.server.print.UserState.this.mLock) {
                java.util.List<android.print.PrintJobInfo> printJobsForApp = this.mPrintJobsForRunningApp.get(appId);
                if (printJobsForApp == null) {
                    return null;
                }
                int printJobCount = printJobsForApp.size();
                for (int i = 0; i < printJobCount; i++) {
                    android.print.PrintJobInfo printJob = printJobsForApp.get(i);
                    if (printJob.getId().equals(printJobId)) {
                        return printJob;
                    }
                }
                return null;
            }
        }

        public java.util.List<android.print.PrintJobInfo> getPrintJobs(int appId) {
            synchronized (com.android.server.print.UserState.this.mLock) {
                java.util.List<android.print.PrintJobInfo> printJobs = null;
                if (appId == -2) {
                    int bucketCount = this.mPrintJobsForRunningApp.size();
                    for (int i = 0; i < bucketCount; i++) {
                        java.util.List<android.print.PrintJobInfo> bucket = this.mPrintJobsForRunningApp.valueAt(i);
                        if (printJobs == null) {
                            printJobs = new java.util.ArrayList<>();
                        }
                        printJobs.addAll(bucket);
                    }
                } else {
                    java.util.List<android.print.PrintJobInfo> bucket2 = this.mPrintJobsForRunningApp.get(appId);
                    if (bucket2 != null) {
                        if (0 == 0) {
                            printJobs = new java.util.ArrayList<>();
                        }
                        printJobs.addAll(bucket2);
                    }
                }
                if (printJobs != null) {
                    return printJobs;
                }
                return java.util.Collections.emptyList();
            }
        }

        public void dumpLocked(com.android.internal.util.dump.DualDumpOutputStream dumpStream) {
            int bucketCount = this.mPrintJobsForRunningApp.size();
            for (int i = 0; i < bucketCount; i++) {
                int appId = this.mPrintJobsForRunningApp.keyAt(i);
                java.util.List<android.print.PrintJobInfo> bucket = this.mPrintJobsForRunningApp.valueAt(i);
                int printJobCount = bucket.size();
                for (int j = 0; j < printJobCount; j++) {
                    long token = dumpStream.start("cached_print_jobs", 2246267895813L);
                    dumpStream.write("app_id", 1120986464257L, appId);
                    com.android.internal.print.DumpUtils.writePrintJobInfo(com.android.server.print.UserState.this.mContext, dumpStream, "print_job", 1146756268034L, bucket.get(j));
                    dumpStream.end(token);
                }
            }
        }
    }
}
