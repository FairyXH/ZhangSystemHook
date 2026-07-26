package com.android.server.broadcastradio.hal1;

/* JADX INFO: loaded from: classes.dex */
public class BroadcastRadioService {
    private static final java.lang.String TAG = "BcRadio1Srv";
    private final long mNativeContext = nativeInit();
    private final java.lang.Object mLock = new java.lang.Object();

    private native void nativeFinalize(long j);

    private native long nativeInit();

    private native java.util.List<android.hardware.radio.RadioManager.ModuleProperties> nativeLoadModules(long j);

    private native com.android.server.broadcastradio.hal1.Tuner nativeOpenTuner(long j, int i, android.hardware.radio.RadioManager.BandConfig bandConfig, boolean z, android.hardware.radio.ITunerCallback iTunerCallback);

    protected void finalize() throws java.lang.Throwable {
        nativeFinalize(this.mNativeContext);
        super.finalize();
    }

    public java.util.List<android.hardware.radio.RadioManager.ModuleProperties> loadModules() {
        java.util.List<android.hardware.radio.RadioManager.ModuleProperties> list;
        synchronized (this.mLock) {
            list = (java.util.List) java.util.Objects.requireNonNull(nativeLoadModules(this.mNativeContext));
        }
        return list;
    }

    public android.hardware.radio.ITuner openTuner(int moduleId, android.hardware.radio.RadioManager.BandConfig bandConfig, boolean withAudio, android.hardware.radio.ITunerCallback callback) {
        com.android.server.broadcastradio.hal1.Tuner tunerNativeOpenTuner;
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.e(TAG, "Cannot open tuner on HAL 1.x client for non-current user");
            throw new java.lang.IllegalStateException("Cannot open tuner for non-current user");
        }
        synchronized (this.mLock) {
            tunerNativeOpenTuner = nativeOpenTuner(this.mNativeContext, moduleId, bandConfig, withAudio, callback);
        }
        return tunerNativeOpenTuner;
    }
}
