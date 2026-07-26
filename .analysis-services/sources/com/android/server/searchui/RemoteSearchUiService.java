package com.android.server.searchui;

/* JADX INFO: loaded from: classes3.dex */
public class RemoteSearchUiService extends com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService<com.android.server.searchui.RemoteSearchUiService, android.service.search.ISearchUiService> {
    private static final java.lang.String TAG = "RemoteSearchUiService";
    private static final long TIMEOUT_REMOTE_REQUEST_MILLIS = 2000;
    private final com.android.server.searchui.RemoteSearchUiService.RemoteSearchUiServiceCallbacks mCallback;

    public interface RemoteSearchUiServiceCallbacks extends com.android.internal.infra.AbstractRemoteService.VultureCallback<com.android.server.searchui.RemoteSearchUiService> {
        void onConnectedStateChanged(boolean z);

        void onFailureOrTimeout(boolean z);
    }

    public RemoteSearchUiService(android.content.Context context, java.lang.String serviceInterface, android.content.ComponentName componentName, int userId, com.android.server.searchui.RemoteSearchUiService.RemoteSearchUiServiceCallbacks callback, boolean bindInstantServiceAllowed, boolean verbose) {
        super(context, serviceInterface, componentName, userId, callback, context.getMainThreadHandler(), bindInstantServiceAllowed ? 4194304 : 0, verbose, 1);
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public android.service.search.ISearchUiService getServiceInterface(android.os.IBinder service) {
        return android.service.search.ISearchUiService.Stub.asInterface(service);
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

    public void scheduleOnResolvedService(com.android.internal.infra.AbstractRemoteService.AsyncRequest<android.service.search.ISearchUiService> request) {
        scheduleAsyncRequest(request);
    }

    public void executeOnResolvedService(com.android.internal.infra.AbstractRemoteService.AsyncRequest<android.service.search.ISearchUiService> request) {
        executeAsyncRequest(request);
    }

    protected void handleOnConnectedStateChanged(boolean connected) {
        if (this.mCallback != null) {
            this.mCallback.onConnectedStateChanged(connected);
        }
    }
}
