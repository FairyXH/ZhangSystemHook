package com.android.server.ambientcontext;

/* JADX INFO: loaded from: classes.dex */
final class DefaultRemoteAmbientContextDetectionService extends com.android.internal.infra.ServiceConnector.Impl<android.service.ambientcontext.IAmbientContextDetectionService> implements com.android.server.ambientcontext.RemoteAmbientDetectionService {
    private static final java.lang.String TAG = com.android.server.ambientcontext.DefaultRemoteAmbientContextDetectionService.class.getSimpleName();

    DefaultRemoteAmbientContextDetectionService(android.content.Context context, android.content.ComponentName serviceName, int userId) {
        super(context, new android.content.Intent("android.service.ambientcontext.AmbientContextDetectionService").setComponent(serviceName), 67112960, userId, new java.util.function.Function() { // from class: com.android.server.ambientcontext.DefaultRemoteAmbientContextDetectionService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.service.ambientcontext.IAmbientContextDetectionService.Stub.asInterface((android.os.IBinder) obj);
            }
        });
        connect();
    }

    protected long getAutoDisconnectTimeoutMs() {
        return -1L;
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void startDetection(final android.app.ambientcontext.AmbientContextEventRequest request, final java.lang.String packageName, final android.os.RemoteCallback detectionResultCallback, final android.os.RemoteCallback statusCallback) {
        android.util.Slog.i(TAG, "Start detection for " + request.getEventTypes());
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.ambientcontext.DefaultRemoteAmbientContextDetectionService$$ExternalSyntheticLambda1
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.ambientcontext.IAmbientContextDetectionService) obj).startDetection(request, packageName, detectionResultCallback, statusCallback);
            }
        });
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void stopDetection(final java.lang.String packageName) {
        android.util.Slog.i(TAG, "Stop detection for " + packageName);
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.ambientcontext.DefaultRemoteAmbientContextDetectionService$$ExternalSyntheticLambda2
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.ambientcontext.IAmbientContextDetectionService) obj).stopDetection(packageName);
            }
        });
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void queryServiceStatus(final int[] eventTypes, final java.lang.String packageName, final android.os.RemoteCallback callback) {
        android.util.Slog.i(TAG, "Query status for " + packageName);
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.ambientcontext.DefaultRemoteAmbientContextDetectionService$$ExternalSyntheticLambda3
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.ambientcontext.IAmbientContextDetectionService) obj).queryServiceStatus(eventTypes, packageName, callback);
            }
        });
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        super.dump(prefix, pw);
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void unbind() {
        super.unbind();
    }
}
