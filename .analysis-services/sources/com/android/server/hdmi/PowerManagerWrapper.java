package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class PowerManagerWrapper {
    private static final java.lang.String TAG = "PowerManagerWrapper";
    private final android.os.PowerManager mPowerManager;

    public PowerManagerWrapper(android.content.Context context) {
        this.mPowerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
    }

    boolean isInteractive() {
        return this.mPowerManager.isInteractive();
    }

    void wakeUp(long time, int reason, java.lang.String details) {
        this.mPowerManager.wakeUp(time, reason, details);
    }

    void goToSleep(long time, int reason, int flags) {
        this.mPowerManager.goToSleep(time, reason, flags);
    }

    com.android.server.hdmi.WakeLockWrapper newWakeLock(int levelAndFlags, java.lang.String tag) {
        return new com.android.server.hdmi.PowerManagerWrapper.DefaultWakeLockWrapper(this.mPowerManager.newWakeLock(levelAndFlags, tag));
    }

    public static class DefaultWakeLockWrapper implements com.android.server.hdmi.WakeLockWrapper {
        private static final java.lang.String TAG = "DefaultWakeLockWrapper";
        private final android.os.PowerManager.WakeLock mWakeLock;

        private DefaultWakeLockWrapper(android.os.PowerManager.WakeLock wakeLock) {
            this.mWakeLock = wakeLock;
        }

        @Override // com.android.server.hdmi.WakeLockWrapper
        public void acquire(long timeout) {
            this.mWakeLock.acquire(timeout);
        }

        @Override // com.android.server.hdmi.WakeLockWrapper
        public void acquire() {
            this.mWakeLock.acquire();
        }

        @Override // com.android.server.hdmi.WakeLockWrapper
        public void release() throws java.lang.RuntimeException {
            this.mWakeLock.release();
        }

        @Override // com.android.server.hdmi.WakeLockWrapper
        public boolean isHeld() {
            return this.mWakeLock.isHeld();
        }

        @Override // com.android.server.hdmi.WakeLockWrapper
        public void setReferenceCounted(boolean value) {
            this.mWakeLock.setReferenceCounted(value);
        }
    }
}
