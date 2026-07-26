package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class ClientMonitorCompositeCallback implements com.android.server.biometrics.sensors.ClientMonitorCallback {
    private final java.util.List<com.android.server.biometrics.sensors.ClientMonitorCallback> mCallbacks = new java.util.ArrayList();

    public ClientMonitorCompositeCallback(com.android.server.biometrics.sensors.ClientMonitorCallback... callbacks) {
        for (com.android.server.biometrics.sensors.ClientMonitorCallback callback : callbacks) {
            if (callback != null) {
                this.mCallbacks.add(callback);
            }
        }
    }

    @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
    public final void onClientStarted(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onClientStarted(clientMonitor);
        }
    }

    @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
    public final void onBiometricAction(int action) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onBiometricAction(action);
        }
    }

    @Override // com.android.server.biometrics.sensors.ClientMonitorCallback
    public final void onClientFinished(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, boolean success) {
        for (int i = this.mCallbacks.size() - 1; i >= 0; i--) {
            this.mCallbacks.get(i).onClientFinished(clientMonitor, success);
        }
    }
}
