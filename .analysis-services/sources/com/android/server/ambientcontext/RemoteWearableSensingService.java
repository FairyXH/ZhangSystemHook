package com.android.server.ambientcontext;

/* JADX INFO: loaded from: classes.dex */
final class RemoteWearableSensingService extends com.android.internal.infra.ServiceConnector.Impl<android.service.wearable.IWearableSensingService> implements com.android.server.ambientcontext.RemoteAmbientDetectionService {
    private static final java.lang.String TAG = com.android.server.ambientcontext.RemoteWearableSensingService.class.getSimpleName();

    RemoteWearableSensingService(android.content.Context context, android.content.ComponentName serviceName, int userId) {
        super(context, new android.content.Intent("android.service.wearable.WearableSensingService").setComponent(serviceName), 67112960, userId, new com.android.server.ambientcontext.RemoteWearableSensingService$$ExternalSyntheticLambda1());
        connect();
    }

    protected long getAutoDisconnectTimeoutMs() {
        return -1L;
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void startDetection(final android.app.ambientcontext.AmbientContextEventRequest request, final java.lang.String packageName, final android.os.RemoteCallback detectionResultCallback, final android.os.RemoteCallback statusCallback) {
        android.util.Slog.i(TAG, "Start detection for " + request.getEventTypes());
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.ambientcontext.RemoteWearableSensingService$$ExternalSyntheticLambda0
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.wearable.IWearableSensingService) obj).startDetection(request, packageName, detectionResultCallback, statusCallback);
            }
        });
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void stopDetection(final java.lang.String packageName) {
        android.util.Slog.i(TAG, "Stop detection for " + packageName);
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.ambientcontext.RemoteWearableSensingService$$ExternalSyntheticLambda3
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.wearable.IWearableSensingService) obj).stopDetection(packageName);
            }
        });
    }

    @Override // com.android.server.ambientcontext.RemoteAmbientDetectionService
    public void queryServiceStatus(final int[] eventTypes, final java.lang.String packageName, final android.os.RemoteCallback callback) {
        android.util.Slog.i(TAG, "Query status for " + packageName);
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.ambientcontext.RemoteWearableSensingService$$ExternalSyntheticLambda2
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.wearable.IWearableSensingService) obj).queryServiceStatus(eventTypes, packageName, callback);
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
