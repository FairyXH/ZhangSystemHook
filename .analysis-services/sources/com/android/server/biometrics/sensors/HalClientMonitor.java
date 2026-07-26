package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class HalClientMonitor<T> extends com.android.server.biometrics.sensors.BaseClientMonitor {
    protected final java.util.function.Supplier<T> mLazyDaemon;
    private final com.android.server.biometrics.log.OperationContextExt mOperationContext;

    protected abstract void startHalOperation();

    public abstract void unableToStart();

    public HalClientMonitor(android.content.Context context, java.util.function.Supplier<T> lazyDaemon, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, int userId, java.lang.String owner, int cookie, int sensorId, com.android.server.biometrics.log.BiometricLogger biometricLogger, com.android.server.biometrics.log.BiometricContext biometricContext) {
        super(context, token, listener, userId, owner, cookie, sensorId, biometricLogger, biometricContext);
        this.mLazyDaemon = lazyDaemon;
        int modality = listener != null ? listener.getModality() : 0;
        this.mOperationContext = new com.android.server.biometrics.log.OperationContextExt(isBiometricPrompt(), modality);
    }

    public T getFreshDaemon() {
        return this.mLazyDaemon.get();
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void destroy() {
        super.destroy();
        unsubscribeBiometricContext();
    }

    public boolean isBiometricPrompt() {
        return getCookie() != 0;
    }

    protected com.android.server.biometrics.log.OperationContextExt getOperationContext() {
        return getBiometricContext().updateContext(this.mOperationContext, isCryptoOperation());
    }

    protected com.android.server.biometrics.sensors.ClientMonitorCallback getBiometricContextUnsubscriber() {
        return new com.android.server.biometrics.sensors.ClientMonitorCallback() { // from class: com.android.server.biometrics.sensors.HalClientMonitor.1
            @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
            public void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor monitor, boolean success) {
                com.android.server.biometrics.sensors.HalClientMonitor.this.unsubscribeBiometricContext();
            }
        };
    }

    protected void unsubscribeBiometricContext() {
        getBiometricContext().unsubscribe(this.mOperationContext);
    }
}
