package com.android.server.broadcastradio.hal1;

/* JADX INFO: loaded from: classes.dex */
class TunerCallback implements android.hardware.radio.ITunerCallback {
    private static final java.lang.String TAG = "BcRadio1Srv.TunerCallback";
    private final android.hardware.radio.ITunerCallback mClientCallback;
    private final long mNativeContext;
    private final com.android.server.broadcastradio.hal1.Tuner mTuner;
    private final java.util.concurrent.atomic.AtomicReference<android.hardware.radio.ProgramList.Filter> mProgramListFilter = new java.util.concurrent.atomic.AtomicReference<>();
    private boolean mInitialConfigurationDone = false;

    /* JADX INFO: Access modifiers changed from: private */
    interface RunnableThrowingRemoteException {
        void run() throws android.os.RemoteException;
    }

    private native void nativeDetach(long j);

    private native void nativeFinalize(long j);

    private native long nativeInit(com.android.server.broadcastradio.hal1.Tuner tuner, int i);

    TunerCallback(com.android.server.broadcastradio.hal1.Tuner tuner, android.hardware.radio.ITunerCallback clientCallback, int halRev) {
        this.mTuner = tuner;
        this.mClientCallback = clientCallback;
        this.mNativeContext = nativeInit(tuner, halRev);
    }

    protected void finalize() throws java.lang.Throwable {
        nativeFinalize(this.mNativeContext);
        super.finalize();
    }

    public void detach() {
        nativeDetach(this.mNativeContext);
    }

    private void dispatch(com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException func) {
        try {
            func.run();
        } catch (android.os.RemoteException e) {
            com.android.server.utils.Slogf.e(TAG, "client died", e);
        }
    }

    private void handleHwFailure() {
        onError(0);
        this.mTuner.close();
    }

    void startProgramListUpdates(android.hardware.radio.ProgramList.Filter filter) {
        if (filter == null) {
            filter = new android.hardware.radio.ProgramList.Filter();
        }
        this.mProgramListFilter.set(filter);
        sendProgramListUpdate();
    }

    void stopProgramListUpdates() {
        this.mProgramListFilter.set(null);
    }

    boolean isInitialConfigurationDone() {
        return this.mInitialConfigurationDone;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onError$0(int status) throws android.os.RemoteException {
        this.mClientCallback.onError(status);
    }

    public void onError(final int status) {
        dispatch(new com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal1.TunerCallback$$ExternalSyntheticLambda9
            @Override // com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException
            public final void run() throws android.os.RemoteException {
                this.f$0.lambda$onError$0(status);
            }
        });
    }

    public void onTuneFailed(int result, android.hardware.radio.ProgramSelector selector) {
        com.android.server.utils.Slogf.e(TAG, "Not applicable for HAL 1.x");
    }

    public void onConfigurationChanged(final android.hardware.radio.RadioManager.BandConfig config) {
        this.mInitialConfigurationDone = true;
        dispatch(new com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal1.TunerCallback$$ExternalSyntheticLambda5
            @Override // com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException
            public final void run() throws android.os.RemoteException {
                this.f$0.lambda$onConfigurationChanged$1(config);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onConfigurationChanged$1(android.hardware.radio.RadioManager.BandConfig config) throws android.os.RemoteException {
        this.mClientCallback.onConfigurationChanged(config);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCurrentProgramInfoChanged$2(android.hardware.radio.RadioManager.ProgramInfo info) throws android.os.RemoteException {
        this.mClientCallback.onCurrentProgramInfoChanged(info);
    }

    public void onCurrentProgramInfoChanged(final android.hardware.radio.RadioManager.ProgramInfo info) {
        dispatch(new com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal1.TunerCallback$$ExternalSyntheticLambda7
            @Override // com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException
            public final void run() throws android.os.RemoteException {
                this.f$0.lambda$onCurrentProgramInfoChanged$2(info);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTrafficAnnouncement$3(boolean active) throws android.os.RemoteException {
        this.mClientCallback.onTrafficAnnouncement(active);
    }

    public void onTrafficAnnouncement(final boolean active) {
        dispatch(new com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal1.TunerCallback$$ExternalSyntheticLambda6
            @Override // com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException
            public final void run() throws android.os.RemoteException {
                this.f$0.lambda$onTrafficAnnouncement$3(active);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEmergencyAnnouncement$4(boolean active) throws android.os.RemoteException {
        this.mClientCallback.onEmergencyAnnouncement(active);
    }

    public void onEmergencyAnnouncement(final boolean active) {
        dispatch(new com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal1.TunerCallback$$ExternalSyntheticLambda3
            @Override // com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException
            public final void run() throws android.os.RemoteException {
                this.f$0.lambda$onEmergencyAnnouncement$4(active);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAntennaState$5(boolean connected) throws android.os.RemoteException {
        this.mClientCallback.onAntennaState(connected);
    }

    public void onAntennaState(final boolean connected) {
        dispatch(new com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal1.TunerCallback$$ExternalSyntheticLambda4
            @Override // com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException
            public final void run() throws android.os.RemoteException {
                this.f$0.lambda$onAntennaState$5(connected);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBackgroundScanAvailabilityChange$6(boolean isAvailable) throws android.os.RemoteException {
        this.mClientCallback.onBackgroundScanAvailabilityChange(isAvailable);
    }

    public void onBackgroundScanAvailabilityChange(final boolean isAvailable) {
        dispatch(new com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal1.TunerCallback$$ExternalSyntheticLambda1
            @Override // com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException
            public final void run() throws android.os.RemoteException {
                this.f$0.lambda$onBackgroundScanAvailabilityChange$6(isAvailable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBackgroundScanComplete$7() throws android.os.RemoteException {
        this.mClientCallback.onBackgroundScanComplete();
    }

    public void onBackgroundScanComplete() {
        dispatch(new com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal1.TunerCallback$$ExternalSyntheticLambda10
            @Override // com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException
            public final void run() throws android.os.RemoteException {
                this.f$0.lambda$onBackgroundScanComplete$7();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onProgramListChanged$8() throws android.os.RemoteException {
        this.mClientCallback.onProgramListChanged();
    }

    public void onProgramListChanged() {
        dispatch(new com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal1.TunerCallback$$ExternalSyntheticLambda2
            @Override // com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException
            public final void run() throws android.os.RemoteException {
                this.f$0.lambda$onProgramListChanged$8();
            }
        });
        sendProgramListUpdate();
    }

    private void sendProgramListUpdate() {
        android.hardware.radio.ProgramList.Filter filter = this.mProgramListFilter.get();
        if (filter == null) {
            return;
        }
        try {
            java.util.List<android.hardware.radio.RadioManager.ProgramInfo> modified = this.mTuner.getProgramList(filter.getVendorFilter());
            java.util.Set<android.hardware.radio.RadioManager.ProgramInfo> modifiedSet = (java.util.Set) modified.stream().collect(java.util.stream.Collectors.toSet());
            final android.hardware.radio.ProgramList.Chunk chunk = new android.hardware.radio.ProgramList.Chunk(true, true, modifiedSet, (java.util.Set) null);
            dispatch(new com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal1.TunerCallback$$ExternalSyntheticLambda0
                @Override // com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException
                public final void run() throws android.os.RemoteException {
                    this.f$0.lambda$sendProgramListUpdate$9(chunk);
                }
            });
        } catch (java.lang.IllegalStateException e) {
            com.android.server.utils.Slogf.d(TAG, "Program list not ready yet");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendProgramListUpdate$9(android.hardware.radio.ProgramList.Chunk chunk) throws android.os.RemoteException {
        this.mClientCallback.onProgramListUpdated(chunk);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onProgramListUpdated$10(android.hardware.radio.ProgramList.Chunk chunk) throws android.os.RemoteException {
        this.mClientCallback.onProgramListUpdated(chunk);
    }

    public void onProgramListUpdated(final android.hardware.radio.ProgramList.Chunk chunk) {
        dispatch(new com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal1.TunerCallback$$ExternalSyntheticLambda8
            @Override // com.android.server.broadcastradio.hal1.TunerCallback.RunnableThrowingRemoteException
            public final void run() throws android.os.RemoteException {
                this.f$0.lambda$onProgramListUpdated$10(chunk);
            }
        });
    }

    public void onConfigFlagUpdated(int flag, boolean value) {
        com.android.server.utils.Slogf.w(TAG, "Not applicable for HAL 1.x");
    }

    public void onParametersUpdated(java.util.Map<java.lang.String, java.lang.String> parameters) {
        com.android.server.utils.Slogf.w(TAG, "Not applicable for HAL 1.x");
    }

    public android.os.IBinder asBinder() {
        throw new java.lang.RuntimeException("Not a binder");
    }
}
