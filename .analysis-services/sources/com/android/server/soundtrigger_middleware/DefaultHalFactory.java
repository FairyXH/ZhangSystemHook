package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
class DefaultHalFactory implements com.android.server.soundtrigger_middleware.HalFactory {
    private static final java.lang.String TAG = "SoundTriggerMiddlewareDefaultHalFactory";
    private static final int USE_DEFAULT_HAL = 0;
    private static final int USE_MOCK_HAL_V2 = 2;
    private static final int USE_MOCK_HAL_V3 = 3;
    private static final com.android.server.soundtrigger_middleware.ICaptureStateNotifier mCaptureStateNotifier = new com.android.server.soundtrigger_middleware.ExternalCaptureStateTracker();

    DefaultHalFactory() {
    }

    @Override // com.android.server.soundtrigger_middleware.HalFactory
    public com.android.server.soundtrigger_middleware.ISoundTriggerHal create() {
        try {
            int mockHal = android.os.SystemProperties.getInt("debug.soundtrigger_middleware.use_mock_hal", 0);
            if (mockHal == 0) {
                java.lang.String aidlServiceName = android.hardware.soundtrigger3.ISoundTriggerHw.class.getCanonicalName() + "/default";
                if (android.os.ServiceManager.isDeclared(aidlServiceName)) {
                    android.util.Slog.i(TAG, "Connecting to default soundtrigger3.ISoundTriggerHw");
                    return new com.android.server.soundtrigger_middleware.SoundTriggerHw3Compat(android.os.ServiceManager.waitForService(aidlServiceName), new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.DefaultHalFactory$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            android.os.SystemProperties.set("sys.audio.restart.hal", "1");
                        }
                    });
                }
                android.util.Slog.i(TAG, "Connecting to default soundtrigger-V2.x.ISoundTriggerHw");
                android.hardware.soundtrigger.V2_0.ISoundTriggerHw driver = android.hardware.soundtrigger.V2_0.ISoundTriggerHw.getService(true);
                if (driver == null) {
                    throw new android.os.RemoteException("driver is null");
                }
                return com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.create(driver, new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.DefaultHalFactory$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        android.os.SystemProperties.set("sys.audio.restart.hal", "1");
                    }
                }, mCaptureStateNotifier);
            }
            if (mockHal == 2) {
                android.util.Slog.i(TAG, "Connecting to mock soundtrigger-V2.x.ISoundTriggerHw");
                android.os.HwBinder.setTrebleTestingOverride(true);
                try {
                    final android.hardware.soundtrigger.V2_0.ISoundTriggerHw driver2 = android.hardware.soundtrigger.V2_0.ISoundTriggerHw.getService("mock", true);
                    return com.android.server.soundtrigger_middleware.SoundTriggerHw2Compat.create(driver2, new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.DefaultHalFactory$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            com.android.server.soundtrigger_middleware.DefaultHalFactory.lambda$create$2(driver2);
                        }
                    }, mCaptureStateNotifier);
                } finally {
                    android.os.HwBinder.setTrebleTestingOverride(false);
                }
            }
            if (mockHal == 3) {
                final java.lang.String aidlServiceName2 = android.hardware.soundtrigger3.ISoundTriggerHw.class.getCanonicalName() + "/mock";
                android.util.Slog.i(TAG, "Connecting to mock soundtrigger3.ISoundTriggerHw");
                return new com.android.server.soundtrigger_middleware.SoundTriggerHw3Compat(android.os.ServiceManager.waitForService(aidlServiceName2), new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.DefaultHalFactory$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.soundtrigger_middleware.DefaultHalFactory.lambda$create$3(aidlServiceName2);
                    }
                });
            }
            throw new java.lang.RuntimeException("Unknown HAL mock version: " + mockHal);
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    static /* synthetic */ void lambda$create$2(android.hardware.soundtrigger.V2_0.ISoundTriggerHw driver) {
        try {
            driver.debug(null, new java.util.ArrayList<>(java.util.Arrays.asList("reboot")));
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Failed to reboot mock HAL", e);
        }
    }

    static /* synthetic */ void lambda$create$3(java.lang.String aidlServiceName) {
        try {
            android.os.ServiceManager.waitForService(aidlServiceName).shellCommand(null, null, null, new java.lang.String[]{"reboot"}, null, null);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Failed to reboot mock HAL", e);
        }
    }
}
