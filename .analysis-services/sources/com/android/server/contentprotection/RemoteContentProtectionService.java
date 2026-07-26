package com.android.server.contentprotection;

/* JADX INFO: loaded from: classes.dex */
public class RemoteContentProtectionService extends com.android.internal.infra.ServiceConnector.Impl<android.service.contentcapture.IContentProtectionService> {
    private static final java.lang.String TAG = com.android.server.contentprotection.RemoteContentProtectionService.class.getSimpleName();
    private final long mAutoDisconnectTimeoutMs;
    private final android.content.ComponentName mComponentName;

    public RemoteContentProtectionService(android.content.Context context, android.content.ComponentName componentName, int userId, boolean bindAllowInstant, long autoDisconnectTimeoutMs) {
        super(context, new android.content.Intent("android.service.contentcapture.ContentProtectionService").setComponent(componentName), bindAllowInstant ? 4194304 : 0, userId, new java.util.function.Function() { // from class: com.android.server.contentprotection.RemoteContentProtectionService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.service.contentcapture.IContentProtectionService.Stub.asInterface((android.os.IBinder) obj);
            }
        });
        this.mComponentName = componentName;
        this.mAutoDisconnectTimeoutMs = autoDisconnectTimeoutMs;
    }

    protected long getAutoDisconnectTimeoutMs() {
        return this.mAutoDisconnectTimeoutMs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceConnectionStatusChanged(android.service.contentcapture.IContentProtectionService service, boolean isConnected) {
        android.util.Slog.i(TAG, "Connection status for: " + this.mComponentName + " changed to: " + (isConnected ? "connected" : "disconnected"));
    }

    public void onLoginDetected(final android.content.pm.ParceledListSlice<android.view.contentcapture.ContentCaptureEvent> events) {
        run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.contentprotection.RemoteContentProtectionService$$ExternalSyntheticLambda2
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.contentcapture.IContentProtectionService) obj).onLoginDetected(events);
            }
        });
    }

    public void onUpdateAllowlistRequest(final android.service.contentcapture.IContentProtectionAllowlistCallback callback) {
        run(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.contentprotection.RemoteContentProtectionService$$ExternalSyntheticLambda1
            public final void runNoResult(java.lang.Object obj) {
                ((android.service.contentcapture.IContentProtectionService) obj).onUpdateAllowlistRequest(callback.asBinder());
            }
        });
    }
}
