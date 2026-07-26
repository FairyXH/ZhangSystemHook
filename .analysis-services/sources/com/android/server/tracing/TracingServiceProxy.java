package com.android.server.tracing;

/* JADX INFO: loaded from: classes3.dex */
public class TracingServiceProxy extends com.android.server.SystemService {
    private static final java.lang.String INTENT_ACTION_NOTIFY_SESSION_STOLEN = "com.android.traceur.NOTIFY_SESSION_STOLEN";
    private static final java.lang.String INTENT_ACTION_NOTIFY_SESSION_STOPPED = "com.android.traceur.NOTIFY_SESSION_STOPPED";
    private static final int MAX_CACHED_REPORTER_SERVICES = 8;
    private static final int MAX_FILE_SIZE_BYTES_TO_PIPE = 1024;
    private static final int REPORT_BEGIN = 1;
    private static final int REPORT_BIND_PERM_INCORRECT = 3;
    private static final int REPORT_SVC_COMM_ERROR = 5;
    private static final int REPORT_SVC_HANDOFF = 2;
    private static final int REPORT_SVC_PERM_MISSING = 4;
    private static final java.lang.String TAG = "TracingServiceProxy";
    private static final java.lang.String TRACING_APP_ACTIVITY = "com.android.traceur.StopTraceService";
    private static final java.lang.String TRACING_APP_PACKAGE_NAME = "com.android.traceur";
    public static final java.lang.String TRACING_SERVICE_PROXY_BINDER_NAME = "tracing.proxy";
    private final android.util.LruCache<android.content.ComponentName, com.android.internal.infra.ServiceConnector<android.os.IMessenger>> mCachedReporterServices;
    private final android.content.Context mContext;
    private final android.content.pm.PackageManager mPackageManager;
    private boolean mServicePublished;
    private final android.tracing.ITracingServiceProxy.Stub mTracingServiceProxy;

    public TracingServiceProxy(android.content.Context context) {
        super(context);
        this.mServicePublished = false;
        this.mTracingServiceProxy = new android.tracing.ITracingServiceProxy.Stub() { // from class: com.android.server.tracing.TracingServiceProxy.1
            public void notifyTraceSessionEnded(boolean sessionStolen) {
                com.android.server.tracing.TracingServiceProxy.this.notifyTraceur(sessionStolen);
            }

            public void reportTrace(android.tracing.TraceReportParams params) {
                com.android.server.tracing.TracingServiceProxy.this.reportTrace(params);
            }
        };
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mCachedReporterServices = new android.util.LruCache<>(8);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        if (!this.mServicePublished) {
            publishBinderService(TRACING_SERVICE_PROXY_BINDER_NAME, this.mTracingServiceProxy);
            this.mServicePublished = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyTraceur(boolean sessionStolen) {
        android.content.Intent intent = new android.content.Intent();
        try {
            android.content.pm.PackageInfo info = this.mPackageManager.getPackageInfo(TRACING_APP_PACKAGE_NAME, 1048576);
            intent.setClassName(info.packageName, TRACING_APP_ACTIVITY);
            if (sessionStolen) {
                intent.setAction(INTENT_ACTION_NOTIFY_SESSION_STOLEN);
            } else {
                intent.setAction(INTENT_ACTION_NOTIFY_SESSION_STOPPED);
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    this.mContext.startForegroundServiceAsUser(intent, android.os.UserHandle.SYSTEM);
                } catch (java.lang.RuntimeException e) {
                    android.util.Log.e(TAG, "Failed to notifyTraceSessionEnded", e);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            android.util.Log.e(TAG, "Failed to locate Traceur", e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportTrace(android.tracing.TraceReportParams params) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.TRACING_SERVICE_REPORT_EVENT, 1, params.uuidLsb, params.uuidMsb);
        android.content.ComponentName component = new android.content.ComponentName(params.reporterPackageName, params.reporterClassName);
        if (!hasBindServicePermission(component)) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.TRACING_SERVICE_REPORT_EVENT, 3, params.uuidLsb, params.uuidMsb);
            return;
        }
        boolean hasDumpPermission = hasPermission(component, "android.permission.DUMP");
        boolean hasUsageStatsPermission = hasPermission(component, "android.permission.PACKAGE_USAGE_STATS");
        if (!hasDumpPermission || !hasUsageStatsPermission) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.TRACING_SERVICE_REPORT_EVENT, 4, params.uuidLsb, params.uuidMsb);
            return;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            reportTrace(getOrCreateReporterService(component), params);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void reportTrace(com.android.internal.infra.ServiceConnector<android.os.IMessenger> reporterService, final android.tracing.TraceReportParams params) {
        reporterService.post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.tracing.TracingServiceProxy$$ExternalSyntheticLambda1
            public final void runNoResult(java.lang.Object obj) throws java.lang.Exception {
                com.android.server.tracing.TracingServiceProxy.lambda$reportTrace$0(params, (android.os.IMessenger) obj);
            }
        }).whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.tracing.TracingServiceProxy$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                com.android.server.tracing.TracingServiceProxy.lambda$reportTrace$1(params, (java.lang.Void) obj, (java.lang.Throwable) obj2);
            }
        });
    }

    static /* synthetic */ void lambda$reportTrace$0(android.tracing.TraceReportParams params, android.os.IMessenger messenger) throws java.lang.Exception {
        if (params.usePipeForTesting) {
            android.os.ParcelFileDescriptor[] pipe = android.os.ParcelFileDescriptor.createPipe();
            android.os.ParcelFileDescriptor.AutoCloseInputStream i = new android.os.ParcelFileDescriptor.AutoCloseInputStream(params.fd);
            try {
                android.os.ParcelFileDescriptor.AutoCloseOutputStream o = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(pipe[1]);
                try {
                    byte[] array = i.readNBytes(1024);
                    if (array.length == 1024) {
                        throw new java.lang.IllegalArgumentException("Trace file too large when |usePipeForTesting| is set.");
                    }
                    o.write(array);
                    o.close();
                    i.close();
                    params.fd = pipe[0];
                } finally {
                }
            } catch (java.lang.Throwable th) {
                try {
                    i.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        android.os.Message message = android.os.Message.obtain();
        message.what = 1;
        message.obj = params;
        messenger.send(message);
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.TRACING_SERVICE_REPORT_EVENT, 2, params.uuidLsb, params.uuidMsb);
    }

    static /* synthetic */ void lambda$reportTrace$1(android.tracing.TraceReportParams params, java.lang.Void res, java.lang.Throwable err) {
        if (err != null) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.TRACING_SERVICE_REPORT_EVENT, 5, params.uuidLsb, params.uuidMsb);
            android.util.Slog.e(TAG, "Failed to report trace", err);
        }
        try {
            params.fd.close();
        } catch (java.io.IOException e) {
        }
    }

    private com.android.internal.infra.ServiceConnector<android.os.IMessenger> getOrCreateReporterService(android.content.ComponentName component) {
        com.android.internal.infra.ServiceConnector<android.os.IMessenger> connector = this.mCachedReporterServices.get(component);
        if (connector == null) {
            android.content.Intent intent = new android.content.Intent();
            intent.setComponent(component);
            com.android.internal.infra.ServiceConnector<android.os.IMessenger> connector2 = new com.android.internal.infra.ServiceConnector.Impl<android.os.IMessenger>(this.mContext, intent, 33, this.mContext.getUser().getIdentifier(), new java.util.function.Function() { // from class: com.android.server.tracing.TracingServiceProxy$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return android.os.IMessenger.Stub.asInterface((android.os.IBinder) obj);
                }
            }) { // from class: com.android.server.tracing.TracingServiceProxy.2
                private static final long DISCONNECT_TIMEOUT_MS = 15000;
                private static final long REQUEST_TIMEOUT_MS = 10000;

                protected long getAutoDisconnectTimeoutMs() {
                    return DISCONNECT_TIMEOUT_MS;
                }

                protected long getRequestTimeoutMs() {
                    return 10000L;
                }
            };
            this.mCachedReporterServices.put(intent.getComponent(), connector2);
            return connector2;
        }
        return connector;
    }

    private boolean hasPermission(android.content.ComponentName componentName, java.lang.String permission) throws java.lang.SecurityException {
        if (this.mPackageManager.checkPermission(permission, componentName.getPackageName()) != 0) {
            android.util.Slog.e(TAG, "Trace reporting service " + componentName.toShortString() + " does not have " + permission + " permission");
            return false;
        }
        return true;
    }

    private boolean hasBindServicePermission(android.content.ComponentName componentName) {
        try {
            android.content.pm.ServiceInfo info = this.mPackageManager.getServiceInfo(componentName, 0);
            if (!"android.permission.BIND_TRACE_REPORT_SERVICE".equals(info.permission)) {
                android.util.Slog.e(TAG, "Trace reporting service " + componentName.toShortString() + " does not request android.permission.BIND_TRACE_REPORT_SERVICE permission; instead requests " + info.permission);
                return false;
            }
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "Trace reporting service " + componentName.toShortString() + " does not exist");
            return false;
        }
    }
}
