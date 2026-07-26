package com.android.server.translation;

/* JADX INFO: loaded from: classes3.dex */
final class RemoteTranslationService extends com.android.internal.infra.ServiceConnector.Impl<android.service.translation.ITranslationService> {
    private static final java.lang.String TAG = com.android.server.translation.RemoteTranslationService.class.getSimpleName();
    private static final long TIMEOUT_IDLE_UNBIND_MS = 0;
    private static final int TIMEOUT_REQUEST_MS = 5000;
    private final android.content.ComponentName mComponentName;
    private final long mIdleUnbindTimeoutMs;
    private final android.os.IBinder mRemoteCallback;
    private final int mRequestTimeoutMs;

    RemoteTranslationService(android.content.Context context, android.content.ComponentName serviceName, int userId, boolean bindInstantServiceAllowed, android.os.IBinder callback) {
        super(context, new android.content.Intent("android.service.translation.TranslationService").setComponent(serviceName), bindInstantServiceAllowed ? 4194304 : 0, userId, new java.util.function.Function() { // from class: com.android.server.translation.RemoteTranslationService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.service.translation.ITranslationService.Stub.asInterface((android.os.IBinder) obj);
            }
        });
        this.mIdleUnbindTimeoutMs = 0L;
        this.mRequestTimeoutMs = 5000;
        this.mComponentName = serviceName;
        this.mRemoteCallback = callback;
        connect();
    }

    public android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceConnectionStatusChanged(android.service.translation.ITranslationService service, boolean connected) {
        try {
            if (connected) {
                service.onConnected(this.mRemoteCallback);
            } else {
                service.onDisconnected();
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Exception calling onServiceConnectionStatusChanged(" + connected + "): ", e);
        }
    }

    protected long getAutoDisconnectTimeoutMs() {
        return this.mIdleUnbindTimeoutMs;
    }

    public void onSessionCreated(final android.view.translation.TranslationContext translationContext, final int sessionId, final com.android.internal.os.IResultReceiver resultReceiver) {
        run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.translation.RemoteTranslationService$$ExternalSyntheticLambda1
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.translation.ITranslationService) obj).onCreateTranslationSession(translationContext, sessionId, resultReceiver);
            }
        });
    }

    public void onTranslationCapabilitiesRequest(final int sourceFormat, final int targetFormat, final android.os.ResultReceiver resultReceiver) {
        run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.translation.RemoteTranslationService$$ExternalSyntheticLambda2
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.translation.ITranslationService) obj).onTranslationCapabilitiesRequest(sourceFormat, targetFormat, resultReceiver);
            }
        });
    }
}
