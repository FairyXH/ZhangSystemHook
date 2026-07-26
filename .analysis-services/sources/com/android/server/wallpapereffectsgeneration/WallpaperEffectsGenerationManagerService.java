package com.android.server.wallpapereffectsgeneration;

/* JADX INFO: loaded from: classes3.dex */
public class WallpaperEffectsGenerationManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService, com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService> {
    private static final boolean DEBUG = false;
    private static final int MAX_TEMP_SERVICE_DURATION_MS = 120000;
    private static final java.lang.String TAG = com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService.class.getSimpleName();
    private final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;

    public WallpaperEffectsGenerationManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.string.config_deviceSpecificInputMethodManagerService), null, 17);
        this.mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService(this, this.mLock, resolvedUserId);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("wallpaper_effects_generation", new com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService.WallpaperEffectsGenerationManagerStub());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_WALLPAPER_EFFECTS_GENERATION", TAG);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageUpdatedLocked(int userId) {
        com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService service = peekServiceForUserLocked(userId);
        if (service != null) {
            service.onPackageUpdatedLocked();
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageRestartedLocked(int userId) {
        com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService service = peekServiceForUserLocked(userId);
        if (service != null) {
            service.onPackageRestartedLocked();
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return 120000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class WallpaperEffectsGenerationManagerStub extends android.app.wallpapereffectsgeneration.IWallpaperEffectsGenerationManager.Stub {
        private WallpaperEffectsGenerationManagerStub() {
        }

        public void generateCinematicEffect(final android.app.wallpapereffectsgeneration.CinematicEffectRequest request, final android.app.wallpapereffectsgeneration.ICinematicEffectListener listener) {
            if (!runForUser("generateCinematicEffect", true, new java.util.function.Consumer() { // from class: com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService$WallpaperEffectsGenerationManagerStub$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService) obj).onGenerateCinematicEffectLocked(request, listener);
                }
            })) {
                try {
                    listener.onCinematicEffectGenerated(new android.app.wallpapereffectsgeneration.CinematicEffectResponse.Builder(0, request.getTaskId()).build());
                } catch (android.os.RemoteException e) {
                }
            }
        }

        public void returnCinematicEffectResponse(final android.app.wallpapereffectsgeneration.CinematicEffectResponse response) {
            runForUser("returnCinematicResponse", false, new java.util.function.Consumer() { // from class: com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService$WallpaperEffectsGenerationManagerStub$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService) obj).onReturnCinematicEffectResponseLocked(response);
                }
            });
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerServiceShellCommand(com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        private boolean runForUser(java.lang.String func, boolean checkManageWallpaperEffectsPermission, java.util.function.Consumer<com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService> c) {
            android.app.ActivityManagerInternal am = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            int userId = am.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), android.os.Binder.getCallingUserHandle().getIdentifier(), false, 0, (java.lang.String) null, (java.lang.String) null);
            if (checkManageWallpaperEffectsPermission) {
                android.content.Context ctx = com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService.this.getContext();
                if (ctx.checkCallingPermission("android.permission.MANAGE_WALLPAPER_EFFECTS_GENERATION") != 0 && !com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService.this.mServiceNameResolver.isTemporary(userId) && !com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService.this.mActivityTaskManagerInternal.isCallerRecents(android.os.Binder.getCallingUid())) {
                    java.lang.String msg = "Permission Denial: Cannot call " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid();
                    android.util.Slog.w(com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService.TAG, msg);
                    throw new java.lang.SecurityException(msg);
                }
            }
            int origCallingUid = android.os.Binder.getCallingUid();
            long origId = android.os.Binder.clearCallingIdentity();
            boolean accepted = false;
            try {
                synchronized (com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService.this.mLock) {
                    com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService service = (com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService) com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService.this.getServiceForUserLocked(userId);
                    if (service != null) {
                        if (!checkManageWallpaperEffectsPermission && !service.isCallingUidAllowed(origCallingUid)) {
                            java.lang.String msg2 = "Permission Denial: cannot call " + func + ", uid[" + origCallingUid + "] doesn't match service implementation";
                            android.util.Slog.w(com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService.TAG, msg2);
                            throw new java.lang.SecurityException(msg2);
                        }
                        accepted = true;
                        c.accept(service);
                    }
                }
                return accepted;
            } finally {
                android.os.Binder.restoreCallingIdentity(origId);
            }
        }
    }
}
