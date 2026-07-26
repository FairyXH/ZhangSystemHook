package com.android.server.ondeviceintelligence;

/* JADX INFO: loaded from: classes2.dex */
public class RemoteOnDeviceIntelligenceService extends com.android.internal.infra.ServiceConnector.Impl<android.service.ondeviceintelligence.IOnDeviceIntelligenceService> {
    private static final long LONG_TIMEOUT = java.util.concurrent.TimeUnit.HOURS.toMillis(4);
    private static final java.lang.String TAG = com.android.server.ondeviceintelligence.RemoteOnDeviceIntelligenceService.class.getSimpleName();

    RemoteOnDeviceIntelligenceService(android.content.Context context, android.content.ComponentName serviceName, int userId) {
        super(context, new android.content.Intent("android.service.ondeviceintelligence.OnDeviceIntelligenceService").setComponent(serviceName), 67112960, userId, new java.util.function.Function() { // from class: com.android.server.ondeviceintelligence.RemoteOnDeviceIntelligenceService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.service.ondeviceintelligence.IOnDeviceIntelligenceService.Stub.asInterface((android.os.IBinder) obj);
            }
        });
        connect();
    }

    protected long getRequestTimeoutMs() {
        return LONG_TIMEOUT;
    }

    protected long getAutoDisconnectTimeoutMs() {
        return android.provider.Settings.Secure.getLongForUser(this.mContext.getContentResolver(), "on_device_intelligence_unbind_timeout_ms", java.util.concurrent.TimeUnit.SECONDS.toMillis(30L), this.mContext.getUserId());
    }
}
