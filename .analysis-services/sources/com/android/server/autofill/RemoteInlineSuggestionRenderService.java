package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public final class RemoteInlineSuggestionRenderService extends com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService<com.android.server.autofill.RemoteInlineSuggestionRenderService, android.service.autofill.IInlineSuggestionRenderService> {
    private static final java.lang.String TAG = "RemoteInlineSuggestionRenderService";
    private final long mIdleUnbindTimeoutMs;

    interface InlineSuggestionRenderCallbacks extends com.android.internal.infra.AbstractRemoteService.VultureCallback<com.android.server.autofill.RemoteInlineSuggestionRenderService> {
    }

    RemoteInlineSuggestionRenderService(android.content.Context context, android.content.ComponentName componentName, java.lang.String serviceInterface, int userId, com.android.server.autofill.RemoteInlineSuggestionRenderService.InlineSuggestionRenderCallbacks callback, boolean bindInstantServiceAllowed, boolean verbose) {
        super(context, serviceInterface, componentName, userId, callback, context.getMainThreadHandler(), bindInstantServiceAllowed ? 4194304 : 0, verbose, 2);
        this.mIdleUnbindTimeoutMs = 0L;
        ensureBound();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public android.service.autofill.IInlineSuggestionRenderService getServiceInterface(android.os.IBinder service) {
        return android.service.autofill.IInlineSuggestionRenderService.Stub.asInterface(service);
    }

    protected long getTimeoutIdleBindMillis() {
        return 0L;
    }

    protected void handleOnConnectedStateChanged(boolean connected) {
        if (connected && getTimeoutIdleBindMillis() != 0) {
            scheduleUnbind();
        }
        super.handleOnConnectedStateChanged(connected);
    }

    public void ensureBound() {
        scheduleBind();
    }

    public void renderSuggestion(final android.service.autofill.IInlineSuggestionUiCallback callback, final android.service.autofill.InlinePresentation presentation, final int width, final int height, final android.os.IBinder hostInputToken, final int displayId, final int userId, final int sessionId) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.autofill.RemoteInlineSuggestionRenderService$$ExternalSyntheticLambda1
            public final void run(android.os.IInterface iInterface) {
                ((android.service.autofill.IInlineSuggestionRenderService) iInterface).renderSuggestion(callback, presentation, width, height, hostInputToken, displayId, userId, sessionId);
            }
        });
    }

    public void getInlineSuggestionsRendererInfo(final android.os.RemoteCallback callback) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.autofill.RemoteInlineSuggestionRenderService$$ExternalSyntheticLambda0
            public final void run(android.os.IInterface iInterface) {
                ((android.service.autofill.IInlineSuggestionRenderService) iInterface).getInlineSuggestionsRendererInfo(callback);
            }
        });
    }

    public void destroySuggestionViews(final int userId, final int sessionId) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.autofill.RemoteInlineSuggestionRenderService$$ExternalSyntheticLambda2
            public final void run(android.os.IInterface iInterface) {
                ((android.service.autofill.IInlineSuggestionRenderService) iInterface).destroySuggestionViews(userId, sessionId);
            }
        });
    }

    private static android.content.pm.ServiceInfo getServiceInfo(android.content.Context context, int userId) {
        java.lang.String packageName = context.getPackageManager().getServicesSystemSharedLibraryPackageName();
        if (packageName == null) {
            android.util.Slog.w(TAG, "no external services package!");
            return null;
        }
        android.content.Intent intent = new android.content.Intent("android.service.autofill.InlineSuggestionRenderService");
        intent.setPackage(packageName);
        android.content.pm.ResolveInfo resolveInfo = context.getPackageManager().resolveServiceAsUser(intent, 132, userId);
        android.content.pm.ServiceInfo serviceInfo = resolveInfo == null ? null : resolveInfo.serviceInfo;
        if (resolveInfo == null || serviceInfo == null) {
            android.util.Slog.w(TAG, "No valid components found.");
            return null;
        }
        if (!"android.permission.BIND_INLINE_SUGGESTION_RENDER_SERVICE".equals(serviceInfo.permission)) {
            android.util.Slog.w(TAG, serviceInfo.name + " does not require permission android.permission.BIND_INLINE_SUGGESTION_RENDER_SERVICE");
            return null;
        }
        return serviceInfo;
    }

    public static android.content.ComponentName getServiceComponentName(android.content.Context context, int userId) {
        android.content.pm.ServiceInfo serviceInfo = getServiceInfo(context, userId);
        if (serviceInfo == null) {
            return null;
        }
        android.content.ComponentName componentName = new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name);
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "getServiceComponentName(): " + componentName);
        }
        return componentName;
    }
}
