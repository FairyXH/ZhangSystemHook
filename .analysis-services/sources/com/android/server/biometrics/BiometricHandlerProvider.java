package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class BiometricHandlerProvider {
    private static final com.android.server.biometrics.BiometricHandlerProvider sBiometricHandlerProvider = new com.android.server.biometrics.BiometricHandlerProvider();
    private android.os.Handler mBiometricsCallbackHandler;
    private android.os.Handler mFaceHandler;
    private android.os.Handler mFingerprintHandler;

    public static com.android.server.biometrics.BiometricHandlerProvider getInstance() {
        return sBiometricHandlerProvider;
    }

    private BiometricHandlerProvider() {
    }

    public synchronized android.os.Handler getBiometricCallbackHandler() {
        if (this.mBiometricsCallbackHandler == null) {
            this.mBiometricsCallbackHandler = getNewHandler("BiometricsCallbackHandler", -16);
        }
        return this.mBiometricsCallbackHandler;
    }

    public synchronized android.os.Handler getFaceHandler() {
        if (this.mFaceHandler == null) {
            this.mFaceHandler = getNewHandler("FaceHandler", -16);
        }
        return this.mFaceHandler;
    }

    public synchronized android.os.Handler getFingerprintHandler() {
        if (this.mFingerprintHandler == null) {
            this.mFingerprintHandler = getNewHandler("FingerprintHandler", -16);
        }
        return this.mFingerprintHandler;
    }

    private android.os.Handler getNewHandler(java.lang.String tag, int priority) {
        android.os.HandlerThread handlerThread = new android.os.HandlerThread(tag, priority);
        handlerThread.start();
        return new android.os.Handler(handlerThread.getLooper());
    }
}
