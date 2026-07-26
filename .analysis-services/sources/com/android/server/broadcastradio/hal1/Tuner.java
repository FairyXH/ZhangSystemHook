package com.android.server.broadcastradio.hal1;

/* JADX INFO: loaded from: classes.dex */
class Tuner extends android.hardware.radio.ITuner.Stub {
    private static final java.lang.String TAG = "BcRadio1Srv.Tuner";
    private final android.hardware.radio.ITunerCallback mClientCallback;
    private final long mNativeContext;
    private int mRegion;
    private final com.android.server.broadcastradio.hal1.TunerCallback mTunerCallback;
    private final boolean mWithAudio;
    private final java.lang.Object mLock = new java.lang.Object();
    private boolean mIsClosed = false;
    private boolean mIsMuted = false;
    private final android.os.IBinder.DeathRecipient mDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.broadcastradio.hal1.Tuner$$ExternalSyntheticLambda0
        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            this.f$0.close();
        }
    };

    private native void nativeCancel(long j);

    private native void nativeCancelAnnouncement(long j);

    private native void nativeClose(long j);

    private native void nativeFinalize(long j);

    private native android.hardware.radio.RadioManager.BandConfig nativeGetConfiguration(long j, int i);

    private native byte[] nativeGetImage(long j, int i);

    private native java.util.List<android.hardware.radio.RadioManager.ProgramInfo> nativeGetProgramList(long j, java.util.Map<java.lang.String, java.lang.String> map);

    private native long nativeInit(int i, boolean z, int i2);

    private native boolean nativeIsAnalogForced(long j);

    private native void nativeScan(long j, boolean z, boolean z2);

    private native void nativeSetAnalogForced(long j, boolean z);

    private native void nativeSetConfiguration(long j, android.hardware.radio.RadioManager.BandConfig bandConfig);

    private native boolean nativeStartBackgroundScan(long j);

    private native void nativeStep(long j, boolean z, boolean z2);

    private native void nativeTune(long j, android.hardware.radio.ProgramSelector programSelector);

    Tuner(android.hardware.radio.ITunerCallback clientCallback, int halRev, int region, boolean withAudio, int band) {
        this.mClientCallback = clientCallback;
        this.mTunerCallback = new com.android.server.broadcastradio.hal1.TunerCallback(this, clientCallback, halRev);
        this.mRegion = region;
        this.mWithAudio = withAudio;
        this.mNativeContext = nativeInit(halRev, withAudio, band);
        try {
            this.mClientCallback.asBinder().linkToDeath(this.mDeathRecipient, 0);
        } catch (android.os.RemoteException e) {
            close();
        }
    }

    protected void finalize() throws java.lang.Throwable {
        nativeFinalize(this.mNativeContext);
        super/*java.lang.Object*/.finalize();
    }

    public void close() {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mIsClosed = true;
            this.mTunerCallback.detach();
            this.mClientCallback.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
            nativeClose(this.mNativeContext);
        }
    }

    public boolean isClosed() {
        return this.mIsClosed;
    }

    private void checkNotClosedLocked() {
        if (this.mIsClosed) {
            throw new java.lang.IllegalStateException("Tuner is closed, no further operations are allowed");
        }
    }

    private boolean checkConfiguredLocked() {
        if (this.mTunerCallback.isInitialConfigurationDone()) {
            return true;
        }
        com.android.server.utils.Slogf.w(TAG, "Initial configuration is still pending, skipping the operation");
        return false;
    }

    public void setConfiguration(android.hardware.radio.RadioManager.BandConfig config) {
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot set configuration for HAL 1.x client from non-current user");
            return;
        }
        if (config == null) {
            throw new java.lang.IllegalArgumentException("The argument must not be a null pointer");
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            nativeSetConfiguration(this.mNativeContext, config);
            this.mRegion = config.getRegion();
        }
    }

    public android.hardware.radio.RadioManager.BandConfig getConfiguration() {
        android.hardware.radio.RadioManager.BandConfig bandConfigNativeGetConfiguration;
        synchronized (this.mLock) {
            checkNotClosedLocked();
            bandConfigNativeGetConfiguration = nativeGetConfiguration(this.mNativeContext, this.mRegion);
        }
        return bandConfigNativeGetConfiguration;
    }

    public void setMuted(boolean mute) {
        if (!this.mWithAudio) {
            throw new java.lang.IllegalStateException("Can't operate on mute - no audio requested");
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            if (this.mIsMuted == mute) {
                return;
            }
            this.mIsMuted = mute;
            com.android.server.utils.Slogf.w(TAG, "Mute via RadioService is not implemented - please handle it via app");
        }
    }

    public boolean isMuted() {
        boolean z;
        if (!this.mWithAudio) {
            com.android.server.utils.Slogf.w(TAG, "Tuner did not request audio, pretending it was muted");
            return true;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            z = this.mIsMuted;
        }
        return z;
    }

    public void step(boolean directionDown, boolean skipSubChannel) {
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot step on HAL 1.x client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            if (checkConfiguredLocked()) {
                nativeStep(this.mNativeContext, directionDown, skipSubChannel);
            }
        }
    }

    public void seek(boolean directionDown, boolean skipSubChannel) {
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot seek on HAL 1.x client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            if (checkConfiguredLocked()) {
                nativeScan(this.mNativeContext, directionDown, skipSubChannel);
            }
        }
    }

    public void tune(android.hardware.radio.ProgramSelector selector) {
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot tune on HAL 1.x client from non-current user");
            return;
        }
        if (selector == null) {
            throw new java.lang.IllegalArgumentException("The argument must not be a null pointer");
        }
        com.android.server.utils.Slogf.i(TAG, "Tuning to " + selector);
        synchronized (this.mLock) {
            checkNotClosedLocked();
            if (checkConfiguredLocked()) {
                nativeTune(this.mNativeContext, selector);
            }
        }
    }

    public void cancel() {
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot cancel on HAL 1.x client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            nativeCancel(this.mNativeContext);
        }
    }

    public void cancelAnnouncement() {
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot cancel announcement on HAL 1.x client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            nativeCancelAnnouncement(this.mNativeContext);
        }
    }

    public android.graphics.Bitmap getImage(int id) {
        byte[] rawImage;
        if (id == 0) {
            throw new java.lang.IllegalArgumentException("Image ID is missing");
        }
        synchronized (this.mLock) {
            rawImage = nativeGetImage(this.mNativeContext, id);
        }
        if (rawImage == null || rawImage.length == 0) {
            return null;
        }
        return android.graphics.BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
    }

    public boolean startBackgroundScan() {
        boolean zNativeStartBackgroundScan;
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot start background scan on HAL 1.x client from non-current user");
            return false;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            zNativeStartBackgroundScan = nativeStartBackgroundScan(this.mNativeContext);
        }
        return zNativeStartBackgroundScan;
    }

    java.util.List<android.hardware.radio.RadioManager.ProgramInfo> getProgramList(java.util.Map vendorFilter) {
        java.util.List<android.hardware.radio.RadioManager.ProgramInfo> list;
        synchronized (this.mLock) {
            checkNotClosedLocked();
            list = nativeGetProgramList(this.mNativeContext, vendorFilter);
            if (list == null) {
                throw new java.lang.IllegalStateException("Program list is not ready");
            }
        }
        return list;
    }

    public void startProgramListUpdates(android.hardware.radio.ProgramList.Filter filter) {
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot start program list updates on HAL 1.x client from non-current user");
        } else {
            this.mTunerCallback.startProgramListUpdates(filter);
        }
    }

    public void stopProgramListUpdates() {
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot stop program list updates on HAL 1.x client from non-current user");
        } else {
            this.mTunerCallback.stopProgramListUpdates();
        }
    }

    public boolean isConfigFlagSupported(int flag) {
        return flag == 2;
    }

    public boolean isConfigFlagSet(int flag) {
        boolean zNativeIsAnalogForced;
        if (flag == 2) {
            synchronized (this.mLock) {
                checkNotClosedLocked();
                zNativeIsAnalogForced = nativeIsAnalogForced(this.mNativeContext);
            }
            return zNativeIsAnalogForced;
        }
        throw new java.lang.UnsupportedOperationException("Not supported by HAL 1.x");
    }

    public void setConfigFlag(int flag, boolean value) {
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot set config flag for HAL 1.x client from non-current user");
        } else {
            if (flag == 2) {
                synchronized (this.mLock) {
                    checkNotClosedLocked();
                    nativeSetAnalogForced(this.mNativeContext, value);
                }
                return;
            }
            throw new java.lang.UnsupportedOperationException("Not supported by HAL 1.x");
        }
    }

    public java.util.Map<java.lang.String, java.lang.String> setParameters(java.util.Map<java.lang.String, java.lang.String> parameters) {
        throw new java.lang.UnsupportedOperationException("Not supported by HAL 1.x");
    }

    public java.util.Map<java.lang.String, java.lang.String> getParameters(java.util.List<java.lang.String> keys) {
        throw new java.lang.UnsupportedOperationException("Not supported by HAL 1.x");
    }
}
