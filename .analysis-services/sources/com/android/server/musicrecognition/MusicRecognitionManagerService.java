package com.android.server.musicrecognition;

/* JADX INFO: loaded from: classes2.dex */
public class MusicRecognitionManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.musicrecognition.MusicRecognitionManagerService, com.android.server.musicrecognition.MusicRecognitionManagerPerUserService> {
    private static final int MAX_TEMP_SERVICE_SUBSTITUTION_DURATION_MS = 60000;
    private static final java.lang.String TAG = com.android.server.musicrecognition.MusicRecognitionManagerService.class.getSimpleName();
    final java.util.concurrent.ExecutorService mExecutorService;
    private com.android.server.musicrecognition.MusicRecognitionManagerService.MusicRecognitionManagerStub mMusicRecognitionManagerStub;

    public MusicRecognitionManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.string.config_defaultRotationResolverService), null);
        this.mExecutorService = java.util.concurrent.Executors.newCachedThreadPool();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.musicrecognition.MusicRecognitionManagerPerUserService newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.musicrecognition.MusicRecognitionManagerPerUserService(this, this.mLock, resolvedUserId);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        this.mMusicRecognitionManagerStub = new com.android.server.musicrecognition.MusicRecognitionManagerService.MusicRecognitionManagerStub();
        publishBinderService("music_recognition", this.mMusicRecognitionManagerStub);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceCaller(java.lang.String func) {
        android.content.Context ctx = getContext();
        if (ctx.checkCallingPermission("android.permission.MANAGE_MUSIC_RECOGNITION") != 0) {
            java.lang.String msg = "Permission Denial: " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " doesn't hold android.permission.MANAGE_MUSIC_RECOGNITION";
            throw new java.lang.SecurityException(msg);
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_MUSIC_RECOGNITION", TAG);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return 60000;
    }

    final class MusicRecognitionManagerStub extends android.media.musicrecognition.IMusicRecognitionManager.Stub {
        MusicRecognitionManagerStub() {
        }

        public void beginRecognition(android.media.musicrecognition.RecognitionRequest recognitionRequest, android.os.IBinder callback) {
            com.android.server.musicrecognition.MusicRecognitionManagerService.this.enforceCaller("beginRecognition");
            synchronized (com.android.server.musicrecognition.MusicRecognitionManagerService.this.mLock) {
                int userId = android.os.UserHandle.getCallingUserId();
                com.android.server.musicrecognition.MusicRecognitionManagerPerUserService service = (com.android.server.musicrecognition.MusicRecognitionManagerPerUserService) com.android.server.musicrecognition.MusicRecognitionManagerService.this.getServiceForUserLocked(userId);
                if (service != null && (isDefaultServiceLocked(userId) || isCalledByServiceAppLocked("beginRecognition"))) {
                    service.beginRecognitionLocked(recognitionRequest, callback);
                } else {
                    try {
                        android.media.musicrecognition.IMusicRecognitionManagerCallback.Stub.asInterface(callback).onRecognitionFailed(3);
                    } catch (android.os.RemoteException e) {
                    }
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
            new com.android.server.musicrecognition.MusicRecognitionManagerServiceShellCommand(com.android.server.musicrecognition.MusicRecognitionManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        private boolean isDefaultServiceLocked(int userId) {
            java.lang.String defaultServiceName = com.android.server.musicrecognition.MusicRecognitionManagerService.this.mServiceNameResolver.getDefaultServiceName(userId);
            if (defaultServiceName != null) {
                java.lang.String currentServiceName = com.android.server.musicrecognition.MusicRecognitionManagerService.this.mServiceNameResolver.getServiceName(userId);
                return defaultServiceName.equals(currentServiceName);
            }
            return false;
        }

        private boolean isCalledByServiceAppLocked(java.lang.String methodName) {
            int userId = android.os.UserHandle.getCallingUserId();
            int callingUid = android.os.Binder.getCallingUid();
            java.lang.String serviceName = com.android.server.musicrecognition.MusicRecognitionManagerService.this.mServiceNameResolver.getServiceName(userId);
            if (serviceName == null) {
                android.util.Slog.e(com.android.server.musicrecognition.MusicRecognitionManagerService.TAG, methodName + ": called by UID " + callingUid + ", but there's no service set for user " + userId);
                return false;
            }
            android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceName);
            if (serviceComponent == null) {
                android.util.Slog.w(com.android.server.musicrecognition.MusicRecognitionManagerService.TAG, methodName + ": invalid service name: " + serviceName);
                return false;
            }
            java.lang.String servicePackageName = serviceComponent.getPackageName();
            android.content.pm.PackageManager pm = com.android.server.musicrecognition.MusicRecognitionManagerService.this.getContext().getPackageManager();
            try {
                int serviceUid = pm.getPackageUidAsUser(servicePackageName, android.os.UserHandle.getCallingUserId());
                if (callingUid != serviceUid) {
                    android.util.Slog.e(com.android.server.musicrecognition.MusicRecognitionManagerService.TAG, methodName + ": called by UID " + callingUid + ", but service UID is " + serviceUid);
                    return false;
                }
                return true;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.w(com.android.server.musicrecognition.MusicRecognitionManagerService.TAG, methodName + ": could not verify UID for " + serviceName);
                return false;
            }
        }
    }
}
