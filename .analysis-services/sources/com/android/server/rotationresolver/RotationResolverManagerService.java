package com.android.server.rotationresolver;

/* JADX INFO: loaded from: classes3.dex */
public class RotationResolverManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.rotationresolver.RotationResolverManagerService, com.android.server.rotationresolver.RotationResolverManagerPerUserService> {
    private static final boolean DEFAULT_SERVICE_ENABLED = true;
    private static final java.lang.String KEY_SERVICE_ENABLED = "service_enabled";
    static final int ORIENTATION_UNKNOWN = 0;
    static final int RESOLUTION_DISABLED = 6;
    static final int RESOLUTION_FAILURE = 8;
    static final int RESOLUTION_UNAVAILABLE = 7;
    private static final java.lang.String TAG = com.android.server.rotationresolver.RotationResolverManagerService.class.getSimpleName();
    private final android.content.Context mContext;
    boolean mIsServiceEnabled;
    private final android.hardware.SensorPrivacyManager mPrivacyManager;

    public RotationResolverManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.string.config_defaultWearableSensingService), null, 68);
        this.mContext = context;
        this.mPrivacyManager = android.hardware.SensorPrivacyManager.getInstance(context);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService, com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            android.provider.DeviceConfig.addOnPropertiesChangedListener("rotation_resolver", getContext().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.rotationresolver.RotationResolverManagerService$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    this.f$0.lambda$onBootPhase$0(properties);
                }
            });
            this.mIsServiceEnabled = android.provider.DeviceConfig.getBoolean("rotation_resolver", KEY_SERVICE_ENABLED, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0(android.provider.DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    private void onDeviceConfigChange(java.util.Set<java.lang.String> keys) {
        if (keys.contains(KEY_SERVICE_ENABLED)) {
            this.mIsServiceEnabled = android.provider.DeviceConfig.getBoolean("rotation_resolver", KEY_SERVICE_ENABLED, true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("resolver", new com.android.server.rotationresolver.RotationResolverManagerService.BinderService());
        publishLocalService(android.rotationresolver.RotationResolverInternal.class, new com.android.server.rotationresolver.RotationResolverManagerService.LocalService());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.rotationresolver.RotationResolverManagerPerUserService newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.rotationresolver.RotationResolverManagerPerUserService(this, this.mLock, resolvedUserId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public void onServiceRemoved(com.android.server.rotationresolver.RotationResolverManagerPerUserService service, int userId) {
        synchronized (this.mLock) {
            service.destroyLocked();
        }
    }

    public static boolean isServiceConfigured(android.content.Context context) {
        return !android.text.TextUtils.isEmpty(getServiceConfigPackage(context));
    }

    android.content.ComponentName getComponentNameShellCommand(int userId) {
        synchronized (this.mLock) {
            com.android.server.rotationresolver.RotationResolverManagerPerUserService service = getServiceForUserLocked(userId);
            if (service != null) {
                return service.getComponentName();
            }
            return null;
        }
    }

    void resolveRotationShellCommand(int userId, android.rotationresolver.RotationResolverInternal.RotationResolverCallbackInternal callbackInternal, android.service.rotationresolver.RotationResolutionRequest request) {
        synchronized (this.mLock) {
            com.android.server.rotationresolver.RotationResolverManagerPerUserService service = getServiceForUserLocked(userId);
            if (service != null) {
                service.resolveRotationLocked(callbackInternal, request, new android.os.CancellationSignal());
            } else {
                android.util.Slog.i(TAG, "service not available for user_id: " + userId);
            }
        }
    }

    static java.lang.String getServiceConfigPackage(android.content.Context context) {
        return context.getPackageManager().getRotationResolverPackageName();
    }

    private final class LocalService extends android.rotationresolver.RotationResolverInternal {
        private LocalService() {
        }

        public boolean isRotationResolverSupported() {
            boolean z;
            synchronized (com.android.server.rotationresolver.RotationResolverManagerService.this.mLock) {
                z = com.android.server.rotationresolver.RotationResolverManagerService.this.mIsServiceEnabled;
            }
            return z;
        }

        public void resolveRotation(android.rotationresolver.RotationResolverInternal.RotationResolverCallbackInternal callbackInternal, java.lang.String packageName, int proposedRotation, int currentRotation, long timeout, android.os.CancellationSignal cancellationSignalInternal) throws java.lang.Throwable {
            android.service.rotationresolver.RotationResolutionRequest request;
            java.util.Objects.requireNonNull(callbackInternal);
            java.util.Objects.requireNonNull(cancellationSignalInternal);
            synchronized (com.android.server.rotationresolver.RotationResolverManagerService.this.mLock) {
                try {
                    try {
                        boolean isCameraAvailable = !com.android.server.rotationresolver.RotationResolverManagerService.this.mPrivacyManager.isSensorPrivacyEnabled(2);
                        try {
                            if (com.android.server.rotationresolver.RotationResolverManagerService.this.mIsServiceEnabled && isCameraAvailable) {
                                try {
                                    com.android.server.rotationresolver.RotationResolverManagerPerUserService service = (com.android.server.rotationresolver.RotationResolverManagerPerUserService) com.android.server.rotationresolver.RotationResolverManagerService.this.getServiceForUserLocked(android.os.UserHandle.getCallingUserId());
                                    if (packageName == null) {
                                        request = new android.service.rotationresolver.RotationResolutionRequest("", currentRotation, proposedRotation, true, timeout);
                                    } else {
                                        request = new android.service.rotationresolver.RotationResolutionRequest(packageName, currentRotation, proposedRotation, true, timeout);
                                    }
                                    service.resolveRotationLocked(callbackInternal, request, cancellationSignalInternal);
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    throw th;
                                }
                            } else {
                                if (isCameraAvailable) {
                                    android.util.Slog.w(com.android.server.rotationresolver.RotationResolverManagerService.TAG, "Rotation Resolver service is disabled.");
                                } else {
                                    android.util.Slog.w(com.android.server.rotationresolver.RotationResolverManagerService.TAG, "Camera is locked by a toggle.");
                                }
                                callbackInternal.onFailure(0);
                                com.android.server.rotationresolver.RotationResolverManagerService.logRotationStats(proposedRotation, currentRotation, 6);
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                }
            }
        }
    }

    private final class BinderService extends android.os.Binder {
        private BinderService() {
        }

        @Override // android.os.Binder
        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.rotationresolver.RotationResolverManagerService.this.mContext, com.android.server.rotationresolver.RotationResolverManagerService.TAG, pw)) {
                synchronized (com.android.server.rotationresolver.RotationResolverManagerService.this.mLock) {
                    com.android.server.rotationresolver.RotationResolverManagerService.this.dumpLocked("", pw);
                }
            }
        }

        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            com.android.server.rotationresolver.RotationResolverManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_ROTATION_RESOLVER", com.android.server.rotationresolver.RotationResolverManagerService.TAG);
            new com.android.server.rotationresolver.RotationResolverShellCommand(com.android.server.rotationresolver.RotationResolverManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }
    }

    static void logRotationStatsWithTimeToCalculate(int proposedRotation, int currentRotation, int result, long timeToCalculate) {
        com.android.internal.util.FrameworkStatsLog.write(328, surfaceRotationToProto(currentRotation), surfaceRotationToProto(proposedRotation), result, timeToCalculate);
    }

    static void logRotationStats(int proposedRotation, int currentRotation, int result) {
        com.android.internal.util.FrameworkStatsLog.write(328, surfaceRotationToProto(currentRotation), surfaceRotationToProto(proposedRotation), result);
    }

    static int errorCodeToProto(int error) {
        switch (error) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            default:
                return 8;
            case 4:
                return 7;
        }
    }

    static int surfaceRotationToProto(int rotationPoseResult) {
        switch (rotationPoseResult) {
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 4;
            case 3:
                return 5;
            default:
                return 8;
        }
    }
}
