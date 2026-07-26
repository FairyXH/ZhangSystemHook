package com.android.server.soundtrigger;

/* JADX INFO: loaded from: classes3.dex */
public class DeviceStateHandler implements com.android.server.soundtrigger.PhoneCallStateHandler.Callback {
    public static final long CALL_INACTIVE_MSG_DELAY_MS = 1000;
    private final java.util.concurrent.Executor mCallbackExecutor;
    private final com.android.server.utils.EventLogger mEventLogger;
    private final java.lang.Object mLock = new java.lang.Object();
    com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState mSoundTriggerDeviceState = com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState.ENABLE;
    private int mSoundTriggerPowerSaveMode = 0;
    private boolean mIsPhoneCallOngoing = false;
    private com.android.server.soundtrigger.DeviceStateHandler.NotificationTask mPhoneStateChangePendingNotify = null;
    private java.util.Set<com.android.server.soundtrigger.DeviceStateHandler.DeviceStateListener> mCallbackSet = java.util.concurrent.ConcurrentHashMap.newKeySet(4);
    private final java.util.concurrent.Executor mDelayedNotificationExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();

    public interface DeviceStateListener {
        void onSoundTriggerDeviceStateUpdate(com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState soundTriggerDeviceState);
    }

    public enum SoundTriggerDeviceState {
        DISABLE,
        CRITICAL,
        ENABLE
    }

    public void onPowerModeChanged(int soundTriggerPowerSaveMode) {
        this.mEventLogger.enqueue(new com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerPowerEvent(soundTriggerPowerSaveMode));
        synchronized (this.mLock) {
            if (soundTriggerPowerSaveMode == this.mSoundTriggerPowerSaveMode) {
                return;
            }
            this.mSoundTriggerPowerSaveMode = soundTriggerPowerSaveMode;
            evaluateStateChange();
        }
    }

    @Override // com.android.server.soundtrigger.PhoneCallStateHandler.Callback
    public void onPhoneCallStateChanged(boolean isInPhoneCall) {
        this.mEventLogger.enqueue(new com.android.server.soundtrigger.DeviceStateHandler.PhoneCallEvent(isInPhoneCall));
        synchronized (this.mLock) {
            if (this.mIsPhoneCallOngoing == isInPhoneCall) {
                return;
            }
            if (this.mPhoneStateChangePendingNotify != null) {
                this.mPhoneStateChangePendingNotify.cancel();
                this.mPhoneStateChangePendingNotify = null;
            }
            this.mIsPhoneCallOngoing = isInPhoneCall;
            if (!this.mIsPhoneCallOngoing) {
                this.mPhoneStateChangePendingNotify = new com.android.server.soundtrigger.DeviceStateHandler.NotificationTask(new java.lang.Runnable() { // from class: com.android.server.soundtrigger.DeviceStateHandler.1
                    @Override // java.lang.Runnable
                    public void run() {
                        synchronized (com.android.server.soundtrigger.DeviceStateHandler.this.mLock) {
                            if (com.android.server.soundtrigger.DeviceStateHandler.this.mPhoneStateChangePendingNotify != null && com.android.server.soundtrigger.DeviceStateHandler.this.mPhoneStateChangePendingNotify.runnableEquals(this)) {
                                com.android.server.soundtrigger.DeviceStateHandler.this.mPhoneStateChangePendingNotify = null;
                                com.android.server.soundtrigger.DeviceStateHandler.this.evaluateStateChange();
                            }
                        }
                    }
                }, 1000L);
                this.mDelayedNotificationExecutor.execute(this.mPhoneStateChangePendingNotify);
            } else {
                evaluateStateChange();
            }
        }
    }

    public DeviceStateHandler(java.util.concurrent.Executor callbackExecutor, com.android.server.utils.EventLogger eventLogger) {
        this.mCallbackExecutor = (java.util.concurrent.Executor) java.util.Objects.requireNonNull(callbackExecutor);
        this.mEventLogger = (com.android.server.utils.EventLogger) java.util.Objects.requireNonNull(eventLogger);
    }

    public com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState getDeviceState() {
        com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState soundTriggerDeviceState;
        synchronized (this.mLock) {
            soundTriggerDeviceState = this.mSoundTriggerDeviceState;
        }
        return soundTriggerDeviceState;
    }

    public void registerListener(final com.android.server.soundtrigger.DeviceStateHandler.DeviceStateListener callback) {
        final com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState state = getDeviceState();
        this.mCallbackExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.soundtrigger.DeviceStateHandler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                callback.onSoundTriggerDeviceStateUpdate(state);
            }
        });
        this.mCallbackSet.add(callback);
    }

    public void unregisterListener(com.android.server.soundtrigger.DeviceStateHandler.DeviceStateListener callback) {
        this.mCallbackSet.remove(callback);
    }

    void dump(java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("DeviceState: " + this.mSoundTriggerDeviceState.name());
            pw.println("PhoneState: " + this.mIsPhoneCallOngoing);
            pw.println("PowerSaveMode: " + this.mSoundTriggerPowerSaveMode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluateStateChange() {
        com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState newState = computeState();
        if (this.mPhoneStateChangePendingNotify != null || this.mSoundTriggerDeviceState == newState) {
            return;
        }
        this.mSoundTriggerDeviceState = newState;
        this.mEventLogger.enqueue(new com.android.server.soundtrigger.DeviceStateHandler.DeviceStateEvent(this.mSoundTriggerDeviceState));
        final com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState state = this.mSoundTriggerDeviceState;
        for (final com.android.server.soundtrigger.DeviceStateHandler.DeviceStateListener callback : this.mCallbackSet) {
            this.mCallbackExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.soundtrigger.DeviceStateHandler$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    callback.onSoundTriggerDeviceStateUpdate(state);
                }
            });
        }
    }

    private com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState computeState() {
        if (this.mIsPhoneCallOngoing) {
            return com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState.DISABLE;
        }
        switch (this.mSoundTriggerPowerSaveMode) {
            case 0:
                return com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState.ENABLE;
            case 1:
                return com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState.CRITICAL;
            case 2:
                return com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState.DISABLE;
            default:
                throw new java.lang.IllegalStateException("Received unexpected power state code" + this.mSoundTriggerPowerSaveMode);
        }
    }

    private static class NotificationTask implements java.lang.Runnable {
        private final java.util.concurrent.CountDownLatch mCancelLatch = new java.util.concurrent.CountDownLatch(1);
        private final java.lang.Runnable mRunnable;
        private final long mWaitInMillis;

        NotificationTask(java.lang.Runnable r, long waitInMillis) {
            this.mRunnable = r;
            this.mWaitInMillis = waitInMillis;
        }

        void cancel() {
            this.mCancelLatch.countDown();
        }

        boolean runnableEquals(java.lang.Runnable runnable) {
            return this.mRunnable == runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (!this.mCancelLatch.await(this.mWaitInMillis, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                    this.mRunnable.run();
                }
            } catch (java.lang.InterruptedException e) {
                java.lang.Thread.currentThread().interrupt();
                throw new java.lang.AssertionError("Unexpected InterruptedException", e);
            }
        }
    }

    private static class PhoneCallEvent extends com.android.server.utils.EventLogger.Event {
        final boolean mIsInPhoneCall;

        PhoneCallEvent(boolean isInPhoneCall) {
            this.mIsInPhoneCall = isInPhoneCall;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return "PhoneCallChange - inPhoneCall: " + this.mIsInPhoneCall;
        }
    }

    private static class SoundTriggerPowerEvent extends com.android.server.utils.EventLogger.Event {
        final int mSoundTriggerPowerState;

        SoundTriggerPowerEvent(int soundTriggerPowerState) {
            this.mSoundTriggerPowerState = soundTriggerPowerState;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return "SoundTriggerPowerChange: " + stateToString();
        }

        private java.lang.String stateToString() {
            switch (this.mSoundTriggerPowerState) {
                case 0:
                    return "All enabled";
                case 1:
                    return "Critical only";
                case 2:
                    return "All disabled";
                default:
                    return "Unknown power state: " + this.mSoundTriggerPowerState;
            }
        }
    }

    private static class DeviceStateEvent extends com.android.server.utils.EventLogger.Event {
        final com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState mSoundTriggerDeviceState;

        DeviceStateEvent(com.android.server.soundtrigger.DeviceStateHandler.SoundTriggerDeviceState soundTriggerDeviceState) {
            this.mSoundTriggerDeviceState = soundTriggerDeviceState;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return "DeviceStateChange: " + this.mSoundTriggerDeviceState.name();
        }
    }
}
