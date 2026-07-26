package com.android.server.profcollect;

/* JADX INFO: loaded from: classes3.dex */
public final class ProfcollectForwardingService extends com.android.server.SystemService {
    private static final long BG_PROCESS_INTERVAL = java.util.concurrent.TimeUnit.HOURS.toMillis(4);
    private static final java.lang.String INTENT_UPLOAD_PROFILES = "com.android.server.profcollect.UPLOAD_PROFILES";
    public static final java.lang.String LOG_TAG = "ProfcollectForwardingService";
    private static com.android.server.profcollect.ProfcollectForwardingService sSelfService;
    private final com.android.server.profcollect.ProfcollectForwardingService.AppLaunchObserver mAppLaunchObserver;
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private final android.os.Handler mHandler;
    private com.android.server.profcollect.IProfCollectd mIProfcollect;
    private com.android.server.profcollect.IProviderStatusCallback mProviderStatusCallback;
    private boolean mUploadEnabled;
    private int mUsageSetting;

    public ProfcollectForwardingService(android.content.Context context) {
        super(context);
        this.mHandler = new com.android.server.profcollect.ProfcollectForwardingService.ProfcollectdHandler(com.android.server.IoThread.getHandler().getLooper());
        this.mProviderStatusCallback = new com.android.server.profcollect.IProviderStatusCallback.Stub() { // from class: com.android.server.profcollect.ProfcollectForwardingService.1
            @Override // com.android.server.profcollect.IProviderStatusCallback
            public void onProviderReady() {
                com.android.server.profcollect.ProfcollectForwardingService.this.mHandler.sendEmptyMessage(1);
            }
        };
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.profcollect.ProfcollectForwardingService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (com.android.server.profcollect.ProfcollectForwardingService.INTENT_UPLOAD_PROFILES.equals(intent.getAction())) {
                    android.util.Log.d(com.android.server.profcollect.ProfcollectForwardingService.LOG_TAG, "Received broadcast to pack and upload reports");
                    com.android.server.profcollect.ProfcollectForwardingService.createAndUploadReport(com.android.server.profcollect.ProfcollectForwardingService.sSelfService);
                }
            }
        };
        this.mAppLaunchObserver = new com.android.server.profcollect.ProfcollectForwardingService.AppLaunchObserver();
        if (sSelfService != null) {
            throw new java.lang.AssertionError("only one service instance allowed");
        }
        sSelfService = this;
        try {
            this.mUsageSetting = android.provider.Settings.Global.getInt(context.getContentResolver(), "multi_cb");
        } catch (android.provider.Settings.SettingNotFoundException e) {
            android.util.Log.e(LOG_TAG, "Usage setting not found: " + e.getMessage());
            this.mUsageSetting = -1;
        }
        this.mUploadEnabled = context.getResources().getBoolean(android.R.bool.config_notificationBadging);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction(INTENT_UPLOAD_PROFILES);
        context.registerReceiver(this.mBroadcastReceiver, filter, 4);
    }

    public static boolean enabled() {
        return android.provider.DeviceConfig.getBoolean("profcollect_native_boot", com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED, false) || android.os.SystemProperties.getBoolean("persist.profcollectd.enabled_override", false);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        connectNativeService();
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase != 1000 || this.mIProfcollect == null) {
            return;
        }
        com.android.internal.os.BackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.profcollect.ProfcollectForwardingService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onBootPhase$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0() {
        if (serviceHasSupportedTraceProvider()) {
            registerProviderStatusCallback();
        }
    }

    private void registerProviderStatusCallback() {
        if (this.mIProfcollect == null) {
            return;
        }
        try {
            this.mIProfcollect.registerProviderStatusCallback(this.mProviderStatusCallback);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(LOG_TAG, "Failed to register provider status callback: " + e.getMessage());
        }
    }

    private boolean serviceHasSupportedTraceProvider() {
        if (this.mIProfcollect == null) {
            return false;
        }
        try {
            return !this.mIProfcollect.get_supported_provider().isEmpty();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(LOG_TAG, "Failed to get supported provider: " + e.getMessage());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean tryConnectNativeService() {
        if (connectNativeService()) {
            return true;
        }
        this.mHandler.sendEmptyMessageDelayed(0, 5000L);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean connectNativeService() {
        try {
            com.android.server.profcollect.IProfCollectd profcollectd = com.android.server.profcollect.IProfCollectd.Stub.asInterface(android.os.ServiceManager.getServiceOrThrow("profcollectd"));
            profcollectd.asBinder().linkToDeath(new com.android.server.profcollect.ProfcollectForwardingService.ProfcollectdDeathRecipient(), 0);
            this.mIProfcollect = profcollectd;
            return true;
        } catch (android.os.ServiceManager.ServiceNotFoundException | android.os.RemoteException e) {
            android.util.Log.w(LOG_TAG, "Failed to connect profcollectd binder service.");
            return false;
        }
    }

    private class ProfcollectdHandler extends android.os.Handler {
        public static final int MESSAGE_BINDER_CONNECT = 0;
        public static final int MESSAGE_REGISTER_SCHEDULERS = 1;

        public ProfcollectdHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case 0:
                    com.android.server.profcollect.ProfcollectForwardingService.this.connectNativeService();
                    return;
                case 1:
                    com.android.server.profcollect.ProfcollectForwardingService.this.registerObservers();
                    com.android.server.profcollect.ProfcollectForwardingService.ProfcollectBGJobService.schedule(com.android.server.profcollect.ProfcollectForwardingService.this.getContext());
                    return;
                default:
                    throw new java.lang.AssertionError("Unknown message: " + message);
            }
        }
    }

    private class ProfcollectdDeathRecipient implements android.os.IBinder.DeathRecipient {
        private ProfcollectdDeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Log.w(com.android.server.profcollect.ProfcollectForwardingService.LOG_TAG, "profcollectd has died");
            com.android.server.profcollect.ProfcollectForwardingService.this.mIProfcollect = null;
            com.android.server.profcollect.ProfcollectForwardingService.this.tryConnectNativeService();
        }
    }

    public static class ProfcollectBGJobService extends android.app.job.JobService {
        private static final int JOB_IDLE_PROCESS = 260817;
        private static final android.content.ComponentName JOB_SERVICE_NAME = new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.profcollect.ProfcollectForwardingService.ProfcollectBGJobService.class.getName());

        public static void schedule(android.content.Context context) {
            android.app.job.JobScheduler js = (android.app.job.JobScheduler) context.getSystemService(android.app.job.JobScheduler.class);
            js.schedule(new android.app.job.JobInfo.Builder(JOB_IDLE_PROCESS, JOB_SERVICE_NAME).setRequiresDeviceIdle(true).setRequiresCharging(true).setPeriodic(com.android.server.profcollect.ProfcollectForwardingService.BG_PROCESS_INTERVAL).setPriority(100).build());
        }

        @Override // android.app.job.JobService
        public boolean onStartJob(android.app.job.JobParameters params) {
            com.android.server.profcollect.ProfcollectForwardingService.createAndUploadReport(com.android.server.profcollect.ProfcollectForwardingService.sSelfService);
            jobFinished(params, false);
            return true;
        }

        @Override // android.app.job.JobService
        public boolean onStopJob(android.app.job.JobParameters params) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerObservers() {
        com.android.internal.os.BackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.profcollect.ProfcollectForwardingService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$registerObservers$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerObservers$1() {
        registerAppLaunchObserver();
        registerCameraOpenObserver();
        registerDex2oatObserver();
        registerOTAObserver();
    }

    private void registerAppLaunchObserver() {
        com.android.server.wm.ActivityTaskManagerInternal atmInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        com.android.server.wm.ActivityMetricsLaunchObserverRegistry launchObserverRegistry = atmInternal.getLaunchObserverRegistry();
        launchObserverRegistry.registerLaunchObserver(this.mAppLaunchObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void traceOnAppStart(java.lang.String packageName) {
        if (this.mIProfcollect == null) {
            return;
        }
        int traceFrequency = android.provider.DeviceConfig.getInt("profcollect_native_boot", "applaunch_trace_freq", 2);
        int randomNum = java.util.concurrent.ThreadLocalRandom.current().nextInt(100);
        if (randomNum < traceFrequency) {
            com.android.internal.os.BackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.profcollect.ProfcollectForwardingService$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$traceOnAppStart$2();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$traceOnAppStart$2() {
        try {
            this.mIProfcollect.trace_once("applaunch");
        } catch (android.os.RemoteException e) {
            android.util.Log.e(LOG_TAG, "Failed to initiate trace: " + e.getMessage());
        }
    }

    private class AppLaunchObserver extends com.android.server.wm.ActivityMetricsLaunchObserver {
        private AppLaunchObserver() {
        }

        @Override // com.android.server.wm.ActivityMetricsLaunchObserver
        public void onIntentStarted(android.content.Intent intent, long timestampNanos) {
            com.android.server.profcollect.ProfcollectForwardingService.this.traceOnAppStart(intent.getPackage());
        }
    }

    private void registerDex2oatObserver() {
        com.android.server.art.ArtManagerLocal aml = (com.android.server.art.ArtManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.art.ArtManagerLocal.class);
        if (aml == null) {
            android.util.Log.w(LOG_TAG, "Couldn't get ArtManagerLocal");
        } else {
            aml.setBatchDexoptStartCallback(new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), new com.android.server.art.ArtManagerLocal.BatchDexoptStartCallback() { // from class: com.android.server.profcollect.ProfcollectForwardingService$$ExternalSyntheticLambda2
                public final void onBatchDexoptStart(com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshot, java.lang.String str, java.util.List list, com.android.server.art.model.BatchDexoptParams.Builder builder, android.os.CancellationSignal cancellationSignal) {
                    this.f$0.lambda$registerDex2oatObserver$3(filteredSnapshot, str, list, builder, cancellationSignal);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerDex2oatObserver$3(com.android.server.pm.PackageManagerLocal.FilteredSnapshot snapshot, java.lang.String reason, java.util.List defaultPackages, com.android.server.art.model.BatchDexoptParams.Builder builder, android.os.CancellationSignal passedSignal) {
        traceOnDex2oatStart();
    }

    private void traceOnDex2oatStart() {
        if (this.mIProfcollect == null) {
            return;
        }
        int traceFrequency = android.provider.DeviceConfig.getInt("profcollect_native_boot", "dex2oat_trace_freq", 25);
        int randomNum = java.util.concurrent.ThreadLocalRandom.current().nextInt(100);
        if (randomNum < traceFrequency) {
            com.android.internal.os.BackgroundThread.get().getThreadHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.profcollect.ProfcollectForwardingService$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$traceOnDex2oatStart$4();
                }
            }, 1000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$traceOnDex2oatStart$4() {
        try {
            this.mIProfcollect.trace_once("dex2oat");
        } catch (android.os.RemoteException e) {
            android.util.Log.e(LOG_TAG, "Failed to initiate trace: " + e.getMessage());
        }
    }

    private void registerOTAObserver() {
        android.os.UpdateEngine updateEngine = new android.os.UpdateEngine();
        updateEngine.bind(new android.os.UpdateEngineCallback() { // from class: com.android.server.profcollect.ProfcollectForwardingService.3
            public void onStatusUpdate(int status, float percent) {
                if (status == 6) {
                    com.android.server.profcollect.ProfcollectForwardingService.createAndUploadReport(com.android.server.profcollect.ProfcollectForwardingService.sSelfService);
                }
            }

            public void onPayloadApplicationComplete(int errorCode) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void createAndUploadReport(final com.android.server.profcollect.ProfcollectForwardingService pfs) {
        com.android.internal.os.BackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.profcollect.ProfcollectForwardingService$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.profcollect.ProfcollectForwardingService.lambda$createAndUploadReport$5(this.f$0);
            }
        });
    }

    static /* synthetic */ void lambda$createAndUploadReport$5(com.android.server.profcollect.ProfcollectForwardingService pfs) {
        try {
            java.lang.String reportName = pfs.mIProfcollect.report(pfs.mUsageSetting) + ".zip";
            if (!pfs.mUploadEnabled) {
                android.util.Log.i(LOG_TAG, "Upload is not enabled.");
            } else {
                android.content.Intent intent = new android.content.Intent().setPackage("com.android.shell").setAction("com.android.shell.action.PROFCOLLECT_UPLOAD").putExtra("filename", reportName);
                pfs.getContext().sendBroadcast(intent);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(LOG_TAG, "Failed to create report: " + e.getMessage());
        }
    }

    /* JADX INFO: renamed from: com.android.server.profcollect.ProfcollectForwardingService$4, reason: invalid class name */
    class AnonymousClass4 extends android.hardware.camera2.CameraManager.AvailabilityCallback {
        AnonymousClass4() {
        }

        public void onCameraOpened(java.lang.String cameraId, java.lang.String packageId) {
            android.util.Log.d(com.android.server.profcollect.ProfcollectForwardingService.LOG_TAG, "Received camera open event from: " + packageId);
            if (packageId.startsWith("client.pid") || packageId.equals("com.google.android.as")) {
                return;
            }
            int traceFrequency = android.provider.DeviceConfig.getInt("profcollect_native_boot", "camera_trace_freq", 10);
            int randomNum = java.util.concurrent.ThreadLocalRandom.current().nextInt(100);
            if (randomNum >= traceFrequency) {
                return;
            }
            com.android.internal.os.BackgroundThread.get().getThreadHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.profcollect.ProfcollectForwardingService$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCameraOpened$0();
                }
            }, 1000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCameraOpened$0() {
            try {
                com.android.server.profcollect.ProfcollectForwardingService.this.mIProfcollect.trace_once("camera");
            } catch (android.os.RemoteException e) {
                android.util.Log.e(com.android.server.profcollect.ProfcollectForwardingService.LOG_TAG, "Failed to initiate trace: " + e.getMessage());
            }
        }
    }

    private void registerCameraOpenObserver() {
        android.hardware.camera2.CameraManager cm = (android.hardware.camera2.CameraManager) getContext().getSystemService(android.hardware.camera2.CameraManager.class);
        cm.registerAvailabilityCallback(new com.android.server.profcollect.ProfcollectForwardingService.AnonymousClass4(), (android.os.Handler) null);
    }
}
