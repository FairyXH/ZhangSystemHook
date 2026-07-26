package com.android.server.wallpapereffectsgeneration;

/* JADX INFO: loaded from: classes3.dex */
public class RemoteWallpaperEffectsGenerationService extends com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService<com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService, android.service.wallpapereffectsgeneration.IWallpaperEffectsGenerationService> {
    private static final java.lang.String TAG = com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService.class.getSimpleName();
    private static final long TIMEOUT_IDLE_BIND_MILLIS = 120000;
    private static final long TIMEOUT_REMOTE_REQUEST_MILLIS = 2000;
    private final com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService.RemoteWallpaperEffectsGenerationServiceCallback mCallback;

    public interface RemoteWallpaperEffectsGenerationServiceCallback extends com.android.internal.infra.AbstractRemoteService.VultureCallback<com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService> {
        void onConnectedStateChanged(boolean z);
    }

    public RemoteWallpaperEffectsGenerationService(android.content.Context context, android.content.ComponentName componentName, int userId, com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService.RemoteWallpaperEffectsGenerationServiceCallback callback, boolean bindInstantServiceAllowed, boolean verbose) {
        super(context, "android.service.wallpapereffectsgeneration.WallpaperEffectsGenerationService", componentName, userId, callback, context.getMainThreadHandler(), bindInstantServiceAllowed ? 4194304 : 0, verbose, 1);
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public android.service.wallpapereffectsgeneration.IWallpaperEffectsGenerationService getServiceInterface(android.os.IBinder service) {
        return android.service.wallpapereffectsgeneration.IWallpaperEffectsGenerationService.Stub.asInterface(service);
    }

    protected long getTimeoutIdleBindMillis() {
        return 120000L;
    }

    protected long getRemoteRequestMillis() {
        return TIMEOUT_REMOTE_REQUEST_MILLIS;
    }

    public void reconnect() {
        super.scheduleBind();
    }

    public void scheduleOnResolvedService(com.android.internal.infra.AbstractRemoteService.AsyncRequest<android.service.wallpapereffectsgeneration.IWallpaperEffectsGenerationService> request) {
        scheduleAsyncRequest(request);
    }

    public void executeOnResolvedService(com.android.internal.infra.AbstractRemoteService.AsyncRequest<android.service.wallpapereffectsgeneration.IWallpaperEffectsGenerationService> request) {
        executeAsyncRequest(request);
    }

    protected void handleOnConnectedStateChanged(boolean connected) {
        if (this.mCallback != null) {
            this.mCallback.onConnectedStateChanged(connected);
        }
    }
}
