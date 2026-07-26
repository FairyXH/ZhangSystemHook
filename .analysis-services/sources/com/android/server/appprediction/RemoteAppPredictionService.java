package com.android.server.appprediction;

/* JADX INFO: loaded from: classes.dex */
public class RemoteAppPredictionService extends com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService<com.android.server.appprediction.RemoteAppPredictionService, android.service.appprediction.IPredictionService> {
    private static final java.lang.String TAG = "RemoteAppPredictionService";
    private static final long TIMEOUT_REMOTE_REQUEST_MILLIS = 2000;
    private final com.android.server.appprediction.RemoteAppPredictionService.RemoteAppPredictionServiceCallbacks mCallback;

    public interface RemoteAppPredictionServiceCallbacks extends com.android.internal.infra.AbstractRemoteService.VultureCallback<com.android.server.appprediction.RemoteAppPredictionService> {
        void onConnectedStateChanged(boolean z);

        void onFailureOrTimeout(boolean z);
    }

    public RemoteAppPredictionService(android.content.Context context, java.lang.String serviceInterface, android.content.ComponentName componentName, int userId, com.android.server.appprediction.RemoteAppPredictionService.RemoteAppPredictionServiceCallbacks callback, boolean bindInstantServiceAllowed, boolean verbose) {
        super(context, serviceInterface, componentName, userId, callback, context.getMainThreadHandler(), bindInstantServiceAllowed ? 4194304 : 0, verbose, 1);
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public android.service.appprediction.IPredictionService getServiceInterface(android.os.IBinder service) {
        return android.service.appprediction.IPredictionService.Stub.asInterface(service);
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

    public void scheduleOnResolvedService(com.android.internal.infra.AbstractRemoteService.AsyncRequest<android.service.appprediction.IPredictionService> request) {
        scheduleAsyncRequest(request);
    }

    public void executeOnResolvedService(com.android.internal.infra.AbstractRemoteService.AsyncRequest<android.service.appprediction.IPredictionService> request) {
        executeAsyncRequest(request);
    }

    protected void handleOnConnectedStateChanged(boolean connected) {
        if (this.mCallback != null) {
            this.mCallback.onConnectedStateChanged(connected);
        }
    }
}
