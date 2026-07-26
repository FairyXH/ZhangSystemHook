package com.android.server.smartspace;

/* JADX INFO: loaded from: classes3.dex */
public class RemoteSmartspaceService extends com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService<com.android.server.smartspace.RemoteSmartspaceService, android.service.smartspace.ISmartspaceService> {
    private static final java.lang.String TAG = "RemoteSmartspaceService";
    private static final long TIMEOUT_REMOTE_REQUEST_MILLIS = 2000;
    private final com.android.server.smartspace.RemoteSmartspaceService.RemoteSmartspaceServiceCallbacks mCallback;

    public interface RemoteSmartspaceServiceCallbacks extends com.android.internal.infra.AbstractRemoteService.VultureCallback<com.android.server.smartspace.RemoteSmartspaceService> {
        void onConnectedStateChanged(boolean z);

        void onFailureOrTimeout(boolean z);
    }

    public RemoteSmartspaceService(android.content.Context context, java.lang.String serviceInterface, android.content.ComponentName componentName, int userId, com.android.server.smartspace.RemoteSmartspaceService.RemoteSmartspaceServiceCallbacks callback, boolean bindInstantServiceAllowed, boolean verbose) {
        super(context, serviceInterface, componentName, userId, callback, context.getMainThreadHandler(), bindInstantServiceAllowed ? 4194304 : 0, verbose, 1);
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public android.service.smartspace.ISmartspaceService getServiceInterface(android.os.IBinder service) {
        return android.service.smartspace.ISmartspaceService.Stub.asInterface(service);
    }

    protected long getTimeoutIdleBindMillis() {
        return 0L;
    }

    protected long getRemoteRequestMillis() {
        return TIMEOUT_REMOTE_REQUEST_MILLIS;
    }

    public void reconnect() {
        super.scheduleBind();
    }

    public void scheduleOnResolvedService(com.android.internal.infra.AbstractRemoteService.AsyncRequest<android.service.smartspace.ISmartspaceService> request) {
        scheduleAsyncRequest(request);
    }

    public void executeOnResolvedService(com.android.internal.infra.AbstractRemoteService.AsyncRequest<android.service.smartspace.ISmartspaceService> request) {
        executeAsyncRequest(request);
    }

    protected void handleOnConnectedStateChanged(boolean connected) {
        if (this.mCallback != null) {
            this.mCallback.onConnectedStateChanged(connected);
        }
    }
}
