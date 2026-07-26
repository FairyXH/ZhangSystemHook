package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class VibratorController {
    private static final java.lang.String TAG = "VibratorController";
    private volatile float mCurrentAmplitude;
    private com.android.server.vibrator.IVibratorControllerExt mIVibratorControllerExt;
    private volatile boolean mIsUnderExternalControl;
    private volatile boolean mIsVibrating;
    private final java.lang.Object mLock;
    private final com.android.server.vibrator.VibratorController.NativeWrapper mNativeWrapper;
    private com.android.server.vibrator.VibratorController.OnVibrationCompleteListener mOnVibrationCompleteListener;
    private com.android.server.vibrator.IVibratorControllerWrapper mVibratorControllerWrapper;
    private volatile android.os.VibratorInfo mVibratorInfo;
    private volatile boolean mVibratorInfoLoadSuccessful;
    private final android.os.RemoteCallbackList<android.os.IVibratorStateListener> mVibratorStateListeners;

    public interface OnVibrationCompleteListener {
        void onComplete(int i, long j);
    }

    VibratorController(int vibratorId, com.android.server.vibrator.VibratorController.OnVibrationCompleteListener listener) {
        this(vibratorId, listener, new com.android.server.vibrator.VibratorController.NativeWrapper());
    }

    VibratorController(int vibratorId, com.android.server.vibrator.VibratorController.OnVibrationCompleteListener listener, com.android.server.vibrator.VibratorController.NativeWrapper nativeWrapper) {
        this.mLock = new java.lang.Object();
        this.mVibratorStateListeners = new android.os.RemoteCallbackList<>();
        this.mVibratorControllerWrapper = new com.android.server.vibrator.VibratorController.VibratorControllerWrapper();
        this.mIVibratorControllerExt = (com.android.server.vibrator.IVibratorControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.vibrator.IVibratorControllerExt.class).base(this).create();
        this.mNativeWrapper = nativeWrapper;
        this.mNativeWrapper.init(vibratorId, listener);
        android.os.VibratorInfo.Builder vibratorInfoBuilder = new android.os.VibratorInfo.Builder(vibratorId);
        this.mVibratorInfoLoadSuccessful = this.mNativeWrapper.getInfo(vibratorInfoBuilder);
        this.mVibratorInfo = vibratorInfoBuilder.build();
        this.mOnVibrationCompleteListener = listener;
        if (!this.mVibratorInfoLoadSuccessful) {
            android.util.Slog.e(TAG, "Vibrator controller initialization failed to load some HAL info for vibrator " + vibratorId);
        }
    }

    public boolean registerVibratorStateListener(android.os.IVibratorStateListener listener) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                if (this.mVibratorStateListeners.register(listener)) {
                    lambda$notifyListenerOnVibrating$0(listener, this.mIsVibrating);
                    android.os.Binder.restoreCallingIdentity(token);
                    return true;
                }
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean unregisterVibratorStateListener(android.os.IVibratorStateListener listener) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return this.mVibratorStateListeners.unregister(listener);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void reloadVibratorInfoIfNeeded() {
        if (this.mVibratorInfoLoadSuccessful) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mVibratorInfoLoadSuccessful) {
                return;
            }
            int vibratorId = this.mVibratorInfo.getId();
            android.os.VibratorInfo.Builder vibratorInfoBuilder = new android.os.VibratorInfo.Builder(vibratorId);
            this.mVibratorInfoLoadSuccessful = this.mNativeWrapper.getInfo(vibratorInfoBuilder);
            this.mVibratorInfo = vibratorInfoBuilder.build();
            if (!this.mVibratorInfoLoadSuccessful) {
                android.util.Slog.e(TAG, "Failed retry of HAL getInfo for vibrator " + vibratorId);
            }
        }
    }

    boolean isVibratorInfoLoadSuccessful() {
        return this.mVibratorInfoLoadSuccessful;
    }

    public android.os.VibratorInfo getVibratorInfo() {
        return this.mVibratorInfo;
    }

    public boolean isVibrating() {
        return this.mIsVibrating;
    }

    public float getCurrentAmplitude() {
        return this.mCurrentAmplitude;
    }

    public boolean isUnderExternalControl() {
        return this.mIsUnderExternalControl;
    }

    public boolean hasCapability(long capability) {
        return this.mVibratorInfo.hasCapability(capability);
    }

    public boolean isAvailable() {
        boolean zIsAvailable;
        synchronized (this.mLock) {
            zIsAvailable = this.mNativeWrapper.isAvailable();
        }
        return zIsAvailable;
    }

    public void setExternalControl(boolean externalControl) {
        if (!this.mVibratorInfo.hasCapability(8L)) {
            return;
        }
        synchronized (this.mLock) {
            this.mIsUnderExternalControl = externalControl;
            this.mNativeWrapper.setExternalControl(externalControl);
        }
    }

    public void updateAlwaysOn(int id, android.os.vibrator.PrebakedSegment prebaked) {
        if (!this.mVibratorInfo.hasCapability(64L)) {
            return;
        }
        synchronized (this.mLock) {
            if (prebaked == null) {
                this.mNativeWrapper.alwaysOnDisable(id);
            } else {
                this.mNativeWrapper.alwaysOnEnable(id, prebaked.getEffectId(), prebaked.getEffectStrength());
            }
        }
    }

    public void setAmplitude(float amplitude) {
        synchronized (this.mLock) {
            if (this.mVibratorInfo.hasCapability(4L)) {
                this.mNativeWrapper.setAmplitude(amplitude);
            }
            if (this.mIsVibrating) {
                this.mCurrentAmplitude = amplitude;
            }
        }
    }

    public long on(long milliseconds, long vibrationId) {
        long duration;
        synchronized (this.mLock) {
            duration = this.mNativeWrapper.on(milliseconds, vibrationId);
            if (duration > 0) {
                this.mCurrentAmplitude = -1.0f;
                notifyListenerOnVibrating(true);
            }
        }
        return duration;
    }

    public long on(android.os.vibrator.PrebakedSegment prebaked, long vibrationId) {
        long duration;
        synchronized (this.mLock) {
            duration = this.mNativeWrapper.perform(prebaked.getEffectId(), prebaked.getEffectStrength(), vibrationId);
            if (duration > 0) {
                this.mCurrentAmplitude = -1.0f;
                notifyListenerOnVibrating(true);
            }
        }
        return duration;
    }

    public long on(android.os.vibrator.PrimitiveSegment[] primitives, long vibrationId) {
        long duration;
        if (!this.mVibratorInfo.hasCapability(32L)) {
            return 0L;
        }
        synchronized (this.mLock) {
            duration = this.mNativeWrapper.compose(primitives, vibrationId);
            if (duration > 0) {
                this.mCurrentAmplitude = -1.0f;
                notifyListenerOnVibrating(true);
            }
        }
        return duration;
    }

    public long on(android.os.vibrator.RampSegment[] primitives, long vibrationId) {
        long duration;
        if (!this.mVibratorInfo.hasCapability(1024L)) {
            return 0L;
        }
        synchronized (this.mLock) {
            int braking = this.mVibratorInfo.getDefaultBraking();
            duration = this.mNativeWrapper.composePwle(primitives, braking, vibrationId);
            if (duration > 0) {
                this.mCurrentAmplitude = -1.0f;
                notifyListenerOnVibrating(true);
            }
        }
        return duration;
    }

    public void off() {
        synchronized (this.mLock) {
            this.mNativeWrapper.off();
            this.mCurrentAmplitude = 0.0f;
            notifyListenerOnVibrating(false);
        }
    }

    public void reset() {
        setExternalControl(false);
        off();
        getWrapper().richtapStop();
        getWrapper().linearMotorVibratorOff();
    }

    public java.lang.String toString() {
        return "VibratorController{mVibratorInfo=" + this.mVibratorInfo + ", mVibratorInfoLoadSuccessful=" + this.mVibratorInfoLoadSuccessful + ", mIsVibrating=" + this.mIsVibrating + ", mCurrentAmplitude=" + this.mCurrentAmplitude + ", mIsUnderExternalControl=" + this.mIsUnderExternalControl + ", mVibratorStateListeners count=" + this.mVibratorStateListeners.getRegisteredCallbackCount() + '}';
    }

    void dump(android.util.IndentingPrintWriter pw) {
        pw.println("Vibrator (id=" + this.mVibratorInfo.getId() + "):");
        pw.increaseIndent();
        pw.println("isVibrating = " + this.mIsVibrating);
        pw.println("isUnderExternalControl = " + this.mIsUnderExternalControl);
        pw.println("currentAmplitude = " + this.mCurrentAmplitude);
        pw.println("vibratorInfoLoadSuccessful = " + this.mVibratorInfoLoadSuccessful);
        pw.println("vibratorStateListener size = " + this.mVibratorStateListeners.getRegisteredCallbackCount());
        this.mVibratorInfo.dump(pw);
        pw.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListenerOnVibrating(final boolean isVibrating) {
        if (this.mIsVibrating != isVibrating) {
            this.mIsVibrating = isVibrating;
            this.mVibratorStateListeners.broadcast(new java.util.function.Consumer() { // from class: com.android.server.vibrator.VibratorController$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$notifyListenerOnVibrating$0(isVibrating, (android.os.IVibratorStateListener) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: notifyStateListener, reason: merged with bridge method [inline-methods] */
    public void lambda$notifyListenerOnVibrating$0(android.os.IVibratorStateListener listener, boolean isVibrating) {
        try {
            listener.onVibrating(isVibrating);
        } catch (android.os.RemoteException | java.lang.RuntimeException e) {
            android.util.Slog.e(TAG, "Vibrator state listener failed to call", e);
        }
    }

    public static class NativeWrapper {
        private long mNativePtr = 0;

        private static native void alwaysOnDisable(long j, long j2);

        private static native void alwaysOnEnable(long j, long j2, long j3, long j4);

        private static native boolean getInfo(long j, android.os.VibratorInfo.Builder builder);

        private static native long getNativeFinalizer();

        private static native boolean isAvailable(long j);

        private static native long nativeInit(int i, com.android.server.vibrator.VibratorController.OnVibrationCompleteListener onVibrationCompleteListener);

        private static native void off(long j);

        private static native long on(long j, long j2, long j3);

        private static native long performComposedEffect(long j, android.os.vibrator.PrimitiveSegment[] primitiveSegmentArr, long j2);

        private static native long performEffect(long j, long j2, long j3, long j4);

        private static native long performPwleEffect(long j, android.os.vibrator.RampSegment[] rampSegmentArr, int i, long j2);

        private static native void setAmplitude(long j, float f);

        private static native void setExternalControl(long j, boolean z);

        public void init(int vibratorId, com.android.server.vibrator.VibratorController.OnVibrationCompleteListener listener) {
            this.mNativePtr = nativeInit(vibratorId, listener);
            long finalizerPtr = getNativeFinalizer();
            if (finalizerPtr != 0) {
                libcore.util.NativeAllocationRegistry registry = libcore.util.NativeAllocationRegistry.createMalloced(com.android.server.vibrator.VibratorController.class.getClassLoader(), finalizerPtr);
                registry.registerNativeAllocation(this, this.mNativePtr);
            }
        }

        public boolean isAvailable() {
            return isAvailable(this.mNativePtr);
        }

        public long on(long milliseconds, long vibrationId) {
            return on(this.mNativePtr, milliseconds, vibrationId);
        }

        public void off() {
            off(this.mNativePtr);
        }

        public void setAmplitude(float amplitude) {
            setAmplitude(this.mNativePtr, amplitude);
        }

        public long perform(long effect, long strength, long vibrationId) {
            return performEffect(this.mNativePtr, effect, strength, vibrationId);
        }

        public long compose(android.os.vibrator.PrimitiveSegment[] primitives, long vibrationId) {
            return performComposedEffect(this.mNativePtr, primitives, vibrationId);
        }

        public long composePwle(android.os.vibrator.RampSegment[] primitives, int braking, long vibrationId) {
            return performPwleEffect(this.mNativePtr, primitives, braking, vibrationId);
        }

        public void setExternalControl(boolean enabled) {
            setExternalControl(this.mNativePtr, enabled);
        }

        public void alwaysOnEnable(long id, long effect, long strength) {
            alwaysOnEnable(this.mNativePtr, id, effect, strength);
        }

        public void alwaysOnDisable(long id) {
            alwaysOnDisable(this.mNativePtr, id);
        }

        public boolean getInfo(android.os.VibratorInfo.Builder infoBuilder) {
            return getInfo(this.mNativePtr, infoBuilder);
        }
    }

    public com.android.server.vibrator.IVibratorControllerWrapper getWrapper() {
        return this.mVibratorControllerWrapper;
    }

    private class VibratorControllerWrapper implements com.android.server.vibrator.IVibratorControllerWrapper {
        private static final float LINEAR_MOTOR_VIBRATOR_STRENGTH_MAX = 2400.0f;
        private static final int PERFORM_EFFECT_SLOW_TIMING = 10;
        private static final float RICHTAP_STRENGTH_MAX = 255.0f;

        private VibratorControllerWrapper() {
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public com.android.server.vibrator.IVibratorControllerExt getExtImpl() {
            return com.android.server.vibrator.VibratorController.this.mIVibratorControllerExt;
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void richtapSetAmplitude(int amplitude) {
            synchronized (com.android.server.vibrator.VibratorController.this.mLock) {
                getExtImpl().richtapSetAmplitude(amplitude);
                if (com.android.server.vibrator.VibratorController.this.mIsVibrating) {
                    com.android.server.vibrator.VibratorController.this.mCurrentAmplitude = amplitude / RICHTAP_STRENGTH_MAX;
                }
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void richtapPerformHe(int looper, int interval, int amplitude, int freq, int[] he, long vibrationId) throws java.lang.Throwable {
            synchronized (com.android.server.vibrator.VibratorController.this.mLock) {
                try {
                    try {
                        getExtImpl().setOnVibrationCompleteListener(com.android.server.vibrator.VibratorController.this.mOnVibrationCompleteListener);
                        getExtImpl().richtapPerformHe(looper, interval, amplitude, freq, he, vibrationId);
                        com.android.server.vibrator.VibratorController.this.mCurrentAmplitude = amplitude / RICHTAP_STRENGTH_MAX;
                        com.android.server.vibrator.VibratorController.this.notifyListenerOnVibrating(true);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void richtapPerformEnvelope(int[] envInfo, boolean fastFlag, int amplitude, long vibrationId) {
            synchronized (com.android.server.vibrator.VibratorController.this.mLock) {
                getExtImpl().setOnVibrationCompleteListener(com.android.server.vibrator.VibratorController.this.mOnVibrationCompleteListener);
                getExtImpl().richtapPerformEnvelope(envInfo, fastFlag, amplitude, vibrationId);
                com.android.server.vibrator.VibratorController.this.mCurrentAmplitude = amplitude / RICHTAP_STRENGTH_MAX;
                com.android.server.vibrator.VibratorController.this.notifyListenerOnVibrating(true);
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public int richtapPerformEffect(int effectId, byte strength, long vibrationId) {
            int duration;
            synchronized (com.android.server.vibrator.VibratorController.this.mLock) {
                getExtImpl().setOnVibrationCompleteListener(com.android.server.vibrator.VibratorController.this.mOnVibrationCompleteListener);
                duration = getExtImpl().richtapPerformEffect(effectId, strength, vibrationId);
                if (duration > 0) {
                    com.android.server.vibrator.VibratorController.this.mCurrentAmplitude = strength / RICHTAP_STRENGTH_MAX;
                    com.android.server.vibrator.VibratorController.this.notifyListenerOnVibrating(true);
                }
            }
            return duration;
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void richtapStop() {
            synchronized (com.android.server.vibrator.VibratorController.this.mLock) {
                getExtImpl().richtapStop();
                com.android.server.vibrator.VibratorController.this.mCurrentAmplitude = 0.0f;
                com.android.server.vibrator.VibratorController.this.notifyListenerOnVibrating(false);
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public long performExtPrebaked(int waveformId, long duration, int strength, long vibrationId) throws java.lang.Throwable {
            synchronized (com.android.server.vibrator.VibratorController.this.mLock) {
                try {
                    try {
                        long now = android.os.SystemClock.uptimeMillis();
                        try {
                            com.android.server.vibrator.VibratorController.this.mNativeWrapper.perform(waveformId, 2L, vibrationId);
                            long cost = android.os.SystemClock.uptimeMillis() - now;
                            if (cost > 10) {
                                android.util.Slog.d(com.android.server.vibrator.VibratorController.TAG, "perform cost " + cost + " ms");
                            }
                            if (duration > 0) {
                                com.android.server.vibrator.VibratorController.this.mCurrentAmplitude = (strength / LINEAR_MOTOR_VIBRATOR_STRENGTH_MAX) + 1.0f;
                                com.android.server.vibrator.VibratorController.this.notifyListenerOnVibrating(true);
                            }
                            return duration;
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void linearMotorVibratorOff() {
            synchronized (com.android.server.vibrator.VibratorController.this.mLock) {
                getExtImpl().linearMotorVibratorOff();
                com.android.server.vibrator.VibratorController.this.mCurrentAmplitude = 0.0f;
                com.android.server.vibrator.VibratorController.this.notifyListenerOnVibrating(false);
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void linearMotorVibratorOn(int waveformId, int amplitude, boolean isRTPMode, long vibrationId) {
            synchronized (com.android.server.vibrator.VibratorController.this.mLock) {
                getExtImpl().linearMotorVibratorOn(waveformId, amplitude, isRTPMode, vibrationId);
                com.android.server.vibrator.VibratorController.this.mCurrentAmplitude = amplitude / LINEAR_MOTOR_VIBRATOR_STRENGTH_MAX;
                com.android.server.vibrator.VibratorController.this.notifyListenerOnVibrating(true);
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void linearMotorVibratorSetVmax(int strength) {
            synchronized (com.android.server.vibrator.VibratorController.this.mLock) {
                getExtImpl().linearMotorVibratorSetVmax(strength);
                if (com.android.server.vibrator.VibratorController.this.mIsVibrating) {
                    com.android.server.vibrator.VibratorController.this.mCurrentAmplitude = strength / LINEAR_MOTOR_VIBRATOR_STRENGTH_MAX;
                }
            }
        }
    }
}
