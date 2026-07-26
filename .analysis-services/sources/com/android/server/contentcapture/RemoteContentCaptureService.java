package com.android.server.contentcapture;

/* JADX INFO: loaded from: classes.dex */
final class RemoteContentCaptureService extends com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService<com.android.server.contentcapture.RemoteContentCaptureService, android.service.contentcapture.IContentCaptureService> {
    private final int mIdleUnbindTimeoutMs;
    private final com.android.server.contentcapture.ContentCapturePerUserService mPerUserService;
    private final android.os.IBinder mServerCallback;

    public interface ContentCaptureServiceCallbacks extends com.android.internal.infra.AbstractRemoteService.VultureCallback<com.android.server.contentcapture.RemoteContentCaptureService> {
    }

    RemoteContentCaptureService(android.content.Context context, java.lang.String serviceInterface, android.content.ComponentName serviceComponentName, android.service.contentcapture.IContentCaptureServiceCallback callback, int userId, com.android.server.contentcapture.ContentCapturePerUserService perUserService, boolean bindInstantServiceAllowed, boolean verbose, int idleUnbindTimeoutMs) {
        super(context, serviceInterface, serviceComponentName, userId, perUserService, context.getMainThreadHandler(), (bindInstantServiceAllowed ? 4194304 : 0) | 4096, verbose, 2);
        this.mPerUserService = perUserService;
        this.mServerCallback = callback.asBinder();
        this.mIdleUnbindTimeoutMs = idleUnbindTimeoutMs;
        ensureBoundLocked();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public android.service.contentcapture.IContentCaptureService getServiceInterface(android.os.IBinder service) {
        return android.service.contentcapture.IContentCaptureService.Stub.asInterface(service);
    }

    protected long getTimeoutIdleBindMillis() {
        return this.mIdleUnbindTimeoutMs;
    }

    protected void handleOnConnectedStateChanged(boolean connected) {
        if (connected && getTimeoutIdleBindMillis() != 0) {
            scheduleUnbind();
        }
        try {
            if (connected) {
                try {
                    this.mService.onConnected(this.mServerCallback, android.view.contentcapture.ContentCaptureHelper.sVerbose, android.view.contentcapture.ContentCaptureHelper.sDebug);
                    com.android.server.contentcapture.ContentCaptureMetricsLogger.writeServiceEvent(1, this.mComponentName);
                    android.util.EventLog.writeEvent(com.android.server.contentcapture.EventLogTags.CC_CONNECT_STATE_CHANGED, java.lang.Integer.valueOf(this.mPerUserService.getUserId()), 1, java.lang.Integer.valueOf(com.android.internal.util.CollectionUtils.size(this.mPerUserService.getContentCaptureAllowlist())));
                    this.mPerUserService.onConnected();
                    return;
                } catch (java.lang.Throwable th) {
                    this.mPerUserService.onConnected();
                    throw th;
                }
            }
            this.mService.onDisconnected();
            com.android.server.contentcapture.ContentCaptureMetricsLogger.writeServiceEvent(2, this.mComponentName);
            android.util.EventLog.writeEvent(com.android.server.contentcapture.EventLogTags.CC_CONNECT_STATE_CHANGED, java.lang.Integer.valueOf(this.mPerUserService.getUserId()), 2, 0);
        } catch (java.lang.Exception e) {
            android.util.Slog.w(this.mTag, "Exception calling onConnectedStateChanged(" + connected + "): " + e);
        }
    }

    public void ensureBoundLocked() {
        scheduleBind();
    }

    public void onSessionStarted(final android.view.contentcapture.ContentCaptureContext context, final int sessionId, final int uid, final com.android.internal.os.IResultReceiver clientReceiver, final int initialState) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentcapture.RemoteContentCaptureService$$ExternalSyntheticLambda1
            public final void run(android.os.IInterface iInterface) {
                ((android.service.contentcapture.IContentCaptureService) iInterface).onSessionStarted(context, sessionId, uid, clientReceiver, initialState);
            }
        });
        com.android.server.contentcapture.ContentCaptureMetricsLogger.writeSessionEvent(sessionId, 1, initialState, getComponentName(), false);
    }

    public void onSessionFinished(final int sessionId) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentcapture.RemoteContentCaptureService$$ExternalSyntheticLambda0
            public final void run(android.os.IInterface iInterface) {
                ((android.service.contentcapture.IContentCaptureService) iInterface).onSessionFinished(sessionId);
            }
        });
        com.android.server.contentcapture.ContentCaptureMetricsLogger.writeSessionEvent(sessionId, 2, 0, getComponentName(), false);
    }

    public void onActivitySnapshotRequest(final int sessionId, final android.service.contentcapture.SnapshotData snapshotData) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentcapture.RemoteContentCaptureService$$ExternalSyntheticLambda4
            public final void run(android.os.IInterface iInterface) {
                ((android.service.contentcapture.IContentCaptureService) iInterface).onActivitySnapshot(sessionId, snapshotData);
            }
        });
    }

    public void onDataRemovalRequest(final android.view.contentcapture.DataRemovalRequest request) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentcapture.RemoteContentCaptureService$$ExternalSyntheticLambda5
            public final void run(android.os.IInterface iInterface) {
                ((android.service.contentcapture.IContentCaptureService) iInterface).onDataRemovalRequest(request);
            }
        });
        com.android.server.contentcapture.ContentCaptureMetricsLogger.writeServiceEvent(5, this.mComponentName);
    }

    public void onDataShareRequest(final android.view.contentcapture.DataShareRequest request, final android.service.contentcapture.IDataShareCallback.Stub dataShareCallback) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentcapture.RemoteContentCaptureService$$ExternalSyntheticLambda3
            public final void run(android.os.IInterface iInterface) {
                ((android.service.contentcapture.IContentCaptureService) iInterface).onDataShared(request, dataShareCallback);
            }
        });
        com.android.server.contentcapture.ContentCaptureMetricsLogger.writeServiceEvent(6, this.mComponentName);
    }

    public void onActivityLifecycleEvent(final android.service.contentcapture.ActivityEvent event) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentcapture.RemoteContentCaptureService$$ExternalSyntheticLambda2
            public final void run(android.os.IInterface iInterface) {
                ((android.service.contentcapture.IContentCaptureService) iInterface).onActivityEvent(event);
            }
        });
    }
}
