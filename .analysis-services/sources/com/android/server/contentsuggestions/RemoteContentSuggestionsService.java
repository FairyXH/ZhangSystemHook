package com.android.server.contentsuggestions;

/* JADX INFO: loaded from: classes.dex */
public class RemoteContentSuggestionsService extends com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService<com.android.server.contentsuggestions.RemoteContentSuggestionsService, android.service.contentsuggestions.IContentSuggestionsService> {
    private static final long TIMEOUT_REMOTE_REQUEST_MILLIS = 2000;

    interface Callbacks extends com.android.internal.infra.AbstractRemoteService.VultureCallback<com.android.server.contentsuggestions.RemoteContentSuggestionsService> {
    }

    RemoteContentSuggestionsService(android.content.Context context, android.content.ComponentName serviceName, int userId, com.android.server.contentsuggestions.RemoteContentSuggestionsService.Callbacks callbacks, boolean bindInstantServiceAllowed, boolean verbose) {
        super(context, "android.service.contentsuggestions.ContentSuggestionsService", serviceName, userId, callbacks, context.getMainThreadHandler(), bindInstantServiceAllowed ? 4194304 : 0, verbose, 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public android.service.contentsuggestions.IContentSuggestionsService getServiceInterface(android.os.IBinder service) {
        return android.service.contentsuggestions.IContentSuggestionsService.Stub.asInterface(service);
    }

    protected long getTimeoutIdleBindMillis() {
        return 0L;
    }

    protected long getRemoteRequestMillis() {
        return TIMEOUT_REMOTE_REQUEST_MILLIS;
    }

    void provideContextImage(final int taskId, final android.hardware.HardwareBuffer contextImage, final int colorSpaceId, final android.os.Bundle imageContextRequestExtras) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentsuggestions.RemoteContentSuggestionsService$$ExternalSyntheticLambda1
            public final void run(android.os.IInterface iInterface) {
                ((android.service.contentsuggestions.IContentSuggestionsService) iInterface).provideContextImage(taskId, contextImage, colorSpaceId, imageContextRequestExtras);
            }
        });
    }

    void suggestContentSelections(final android.app.contentsuggestions.SelectionsRequest selectionsRequest, final android.app.contentsuggestions.ISelectionsCallback selectionsCallback) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentsuggestions.RemoteContentSuggestionsService$$ExternalSyntheticLambda3
            public final void run(android.os.IInterface iInterface) {
                ((android.service.contentsuggestions.IContentSuggestionsService) iInterface).suggestContentSelections(selectionsRequest, selectionsCallback);
            }
        });
    }

    void classifyContentSelections(final android.app.contentsuggestions.ClassificationsRequest classificationsRequest, final android.app.contentsuggestions.IClassificationsCallback callback) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentsuggestions.RemoteContentSuggestionsService$$ExternalSyntheticLambda0
            public final void run(android.os.IInterface iInterface) {
                ((android.service.contentsuggestions.IContentSuggestionsService) iInterface).classifyContentSelections(classificationsRequest, callback);
            }
        });
    }

    void notifyInteraction(final java.lang.String requestId, final android.os.Bundle bundle) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.contentsuggestions.RemoteContentSuggestionsService$$ExternalSyntheticLambda2
            public final void run(android.os.IInterface iInterface) {
                ((android.service.contentsuggestions.IContentSuggestionsService) iInterface).notifyInteraction(requestId, bundle);
            }
        });
    }
}
