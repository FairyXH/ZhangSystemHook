package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class LockoutResetDispatcher implements android.os.IBinder.DeathRecipient {
    private static final java.lang.String TAG = "LockoutResetTracker";
    final java.util.concurrent.ConcurrentLinkedQueue<com.android.server.biometrics.sensors.LockoutResetDispatcher.ClientCallback> mClientCallbacks = new java.util.concurrent.ConcurrentLinkedQueue<>();
    private final android.content.Context mContext;

    private static class ClientCallback {
        private static final long WAKELOCK_TIMEOUT_MS = 2000;
        private final android.hardware.biometrics.IBiometricServiceLockoutResetCallback mCallback;
        private final java.lang.String mOpPackageName;
        private final android.os.PowerManager.WakeLock mWakeLock;

        ClientCallback(android.content.Context context, android.hardware.biometrics.IBiometricServiceLockoutResetCallback callback, java.lang.String opPackageName) {
            android.os.PowerManager pm = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
            this.mOpPackageName = opPackageName;
            this.mCallback = callback;
            this.mWakeLock = pm.newWakeLock(1, "LockoutResetMonitor:SendLockoutReset");
        }

        void sendLockoutReset(int sensorId) {
            if (this.mCallback != null) {
                try {
                    this.mWakeLock.acquire(WAKELOCK_TIMEOUT_MS);
                    this.mCallback.onLockoutReset(sensorId, new android.os.IRemoteCallback.Stub() { // from class: com.android.server.biometrics.sensors.LockoutResetDispatcher.ClientCallback.1
                        public void sendResult(android.os.Bundle data) {
                            com.android.server.biometrics.sensors.LockoutResetDispatcher.ClientCallback.this.releaseWakelock();
                        }
                    });
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.biometrics.sensors.LockoutResetDispatcher.TAG, "Failed to invoke onLockoutReset: ", e);
                    releaseWakelock();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void releaseWakelock() {
            if (this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
        }
    }

    public LockoutResetDispatcher(android.content.Context context) {
        this.mContext = context;
    }

    public void addCallback(android.hardware.biometrics.IBiometricServiceLockoutResetCallback callback, java.lang.String opPackageName) {
        if (callback == null) {
            android.util.Slog.w(TAG, "Callback from : " + opPackageName + " is null");
            return;
        }
        this.mClientCallbacks.add(new com.android.server.biometrics.sensors.LockoutResetDispatcher.ClientCallback(this.mContext, callback, opPackageName));
        try {
            callback.asBinder().linkToDeath(this, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to link to death", e);
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied(android.os.IBinder who) {
        android.util.Slog.e(TAG, "Callback binder died: " + who);
        java.util.Iterator<com.android.server.biometrics.sensors.LockoutResetDispatcher.ClientCallback> iterator = this.mClientCallbacks.iterator();
        while (iterator.hasNext()) {
            com.android.server.biometrics.sensors.LockoutResetDispatcher.ClientCallback callback = iterator.next();
            if (callback.mCallback.asBinder().equals(who)) {
                android.util.Slog.e(TAG, "Removing dead callback for: " + callback.mOpPackageName);
                callback.releaseWakelock();
                iterator.remove();
            }
        }
    }

    public void notifyLockoutResetCallbacks(int sensorId) {
        for (com.android.server.biometrics.sensors.LockoutResetDispatcher.ClientCallback callback : this.mClientCallbacks) {
            callback.sendLockoutReset(sensorId);
        }
    }
}
