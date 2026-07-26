package com.android.server.wallpapereffectsgeneration;

/* JADX INFO: loaded from: classes3.dex */
public class WallpaperEffectsGenerationPerUserService extends com.android.server.infra.AbstractPerUserSystemService<com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService, com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService> implements com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService.RemoteWallpaperEffectsGenerationServiceCallback {
    private static final java.lang.String TAG = com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService.class.getSimpleName();
    private com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService.CinematicEffectListenerWrapper mCinematicEffectListenerWrapper;
    private com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService mRemoteService;

    protected WallpaperEffectsGenerationPerUserService(com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService master, java.lang.Object lock, int userId) {
        super(master, lock, userId);
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        try {
            android.content.pm.ServiceInfo si = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 128L, this.mUserId);
            if (!"android.permission.BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE".equals(si.permission)) {
                android.util.Slog.w(TAG, "WallpaperEffectsGenerationService from '" + si.packageName + "' does not require permission android.permission.BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE");
                throw new java.lang.SecurityException("Service does not require permission android.permission.BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE");
            }
            return si;
        } catch (android.os.RemoteException e) {
            throw new android.content.pm.PackageManager.NameNotFoundException("Could not get service for " + serviceComponent);
        }
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected boolean updateLocked(boolean disabled) {
        boolean enabledChanged = super.updateLocked(disabled);
        updateRemoteServiceLocked();
        return enabledChanged;
    }

    public void onGenerateCinematicEffectLocked(final android.app.wallpapereffectsgeneration.CinematicEffectRequest cinematicEffectRequest, android.app.wallpapereffectsgeneration.ICinematicEffectListener cinematicEffectListener) {
        android.app.wallpapereffectsgeneration.CinematicEffectResponse cinematicEffectResponse;
        java.lang.String newTaskId = cinematicEffectRequest.getTaskId();
        if (this.mCinematicEffectListenerWrapper != null) {
            if (this.mCinematicEffectListenerWrapper.mTaskId.equals(newTaskId)) {
                cinematicEffectResponse = new android.app.wallpapereffectsgeneration.CinematicEffectResponse.Builder(3, newTaskId).build();
            } else {
                cinematicEffectResponse = new android.app.wallpapereffectsgeneration.CinematicEffectResponse.Builder(4, newTaskId).build();
            }
            try {
                cinematicEffectListener.onCinematicEffectGenerated(cinematicEffectResponse);
                return;
            } catch (android.os.RemoteException e) {
                if (isDebug()) {
                    android.util.Slog.w(TAG, "RemoteException invoking cinematic effect listener for task[" + this.mCinematicEffectListenerWrapper.mTaskId + "]");
                    return;
                }
                return;
            }
        }
        com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService remoteService = ensureRemoteServiceLocked();
        if (remoteService != null) {
            remoteService.executeOnResolvedService(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService$$ExternalSyntheticLambda0
                public final void run(android.os.IInterface iInterface) {
                    ((android.service.wallpapereffectsgeneration.IWallpaperEffectsGenerationService) iInterface).onGenerateCinematicEffect(cinematicEffectRequest);
                }
            });
            this.mCinematicEffectListenerWrapper = new com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService.CinematicEffectListenerWrapper(newTaskId, cinematicEffectListener);
            return;
        }
        if (isDebug()) {
            android.util.Slog.d(TAG, "Remote service not found");
        }
        try {
            cinematicEffectListener.onCinematicEffectGenerated(createErrorCinematicEffectResponse(newTaskId));
        } catch (android.os.RemoteException e2) {
            if (isDebug()) {
                android.util.Slog.d(TAG, "Failed to invoke cinematic effect listener for task [" + newTaskId + "]");
            }
        }
    }

    public void onReturnCinematicEffectResponseLocked(android.app.wallpapereffectsgeneration.CinematicEffectResponse cinematicEffectResponse) {
        invokeCinematicListenerAndCleanup(cinematicEffectResponse);
    }

    public boolean isCallingUidAllowed(int callingUid) {
        return getServiceUidLocked() == callingUid;
    }

    private void updateRemoteServiceLocked() {
        if (this.mRemoteService != null) {
            this.mRemoteService.destroy();
            this.mRemoteService = null;
        }
        if (this.mCinematicEffectListenerWrapper != null) {
            invokeCinematicListenerAndCleanup(createErrorCinematicEffectResponse(this.mCinematicEffectListenerWrapper.mTaskId));
        }
    }

    void onPackageUpdatedLocked() {
        if (isDebug()) {
            android.util.Slog.v(TAG, "onPackageUpdatedLocked()");
        }
        destroyAndRebindRemoteService();
    }

    void onPackageRestartedLocked() {
        if (isDebug()) {
            android.util.Slog.v(TAG, "onPackageRestartedLocked()");
        }
        destroyAndRebindRemoteService();
    }

    private void destroyAndRebindRemoteService() {
        if (this.mRemoteService == null) {
            return;
        }
        if (isDebug()) {
            android.util.Slog.d(TAG, "Destroying the old remote service.");
        }
        this.mRemoteService.destroy();
        this.mRemoteService = null;
        this.mRemoteService = ensureRemoteServiceLocked();
        if (this.mRemoteService != null) {
            if (isDebug()) {
                android.util.Slog.d(TAG, "Rebinding to the new remote service.");
            }
            this.mRemoteService.reconnect();
        }
        if (this.mCinematicEffectListenerWrapper != null) {
            invokeCinematicListenerAndCleanup(createErrorCinematicEffectResponse(this.mCinematicEffectListenerWrapper.mTaskId));
        }
    }

    private android.app.wallpapereffectsgeneration.CinematicEffectResponse createErrorCinematicEffectResponse(java.lang.String taskId) {
        return new android.app.wallpapereffectsgeneration.CinematicEffectResponse.Builder(0, taskId).build();
    }

    private void invokeCinematicListenerAndCleanup(android.app.wallpapereffectsgeneration.CinematicEffectResponse cinematicEffectResponse) {
        try {
            try {
                if (this.mCinematicEffectListenerWrapper != null && this.mCinematicEffectListenerWrapper.mListener != null) {
                    this.mCinematicEffectListenerWrapper.mListener.onCinematicEffectGenerated(cinematicEffectResponse);
                } else if (isDebug()) {
                    android.util.Slog.w(TAG, "Cinematic effect listener not found for task[" + this.mCinematicEffectListenerWrapper.mTaskId + "]");
                }
            } catch (android.os.RemoteException e) {
                if (isDebug()) {
                    android.util.Slog.w(TAG, "Error invoking cinematic effect listener for task[" + this.mCinematicEffectListenerWrapper.mTaskId + "]");
                }
            }
        } finally {
            this.mCinematicEffectListenerWrapper = null;
        }
    }

    private com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService ensureRemoteServiceLocked() {
        if (this.mRemoteService == null) {
            android.content.ComponentName serviceComponent = updateServiceInfoLocked();
            if (serviceComponent == null) {
                if (((com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService) this.mMaster).verbose) {
                    android.util.Slog.v(TAG, "ensureRemoteServiceLocked(): not set");
                    return null;
                }
                return null;
            }
            this.mRemoteService = new com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService(getContext(), serviceComponent, this.mUserId, this, ((com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService) this.mMaster).isBindInstantServiceAllowed(), ((com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationManagerService) this.mMaster).verbose);
        }
        return this.mRemoteService;
    }

    public void onServiceDied(com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService service) {
        android.util.Slog.w(TAG, "remote wallpaper effects generation service died");
        updateRemoteServiceLocked();
    }

    @Override // com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService.RemoteWallpaperEffectsGenerationServiceCallback
    public void onConnectedStateChanged(boolean connected) {
        if (!connected) {
            android.util.Slog.w(TAG, "remote wallpaper effects generation service disconnected");
            updateRemoteServiceLocked();
        }
    }

    private static final class CinematicEffectListenerWrapper {
        private final android.app.wallpapereffectsgeneration.ICinematicEffectListener mListener;
        private final java.lang.String mTaskId;

        CinematicEffectListenerWrapper(java.lang.String taskId, android.app.wallpapereffectsgeneration.ICinematicEffectListener listener) {
            this.mTaskId = taskId;
            this.mListener = listener;
        }
    }
}
