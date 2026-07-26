package com.android.server.biometrics.log;

/* JADX INFO: loaded from: classes.dex */
public class CallbackWithProbe<T extends com.android.server.biometrics.log.Probe> implements com.android.server.biometrics.sensors.ClientMonitorCallback {
    private final T mProbe;
    private final boolean mStartWithClient;

    public CallbackWithProbe(T probe, boolean startWithClient) {
        this.mProbe = probe;
        this.mStartWithClient = startWithClient;
    }

    @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
    public void onClientStarted(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor) {
        if (this.mStartWithClient) {
            com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.log.CallbackWithProbe$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onClientStarted$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClientStarted$0() {
        this.mProbe.enable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onClientFinished$1() {
        this.mProbe.destroy();
    }

    @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
    public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
        com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.log.CallbackWithProbe$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onClientFinished$1();
            }
        });
    }

    public T getProbe() {
        return this.mProbe;
    }
}
