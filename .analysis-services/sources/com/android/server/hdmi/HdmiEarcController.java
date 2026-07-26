package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class HdmiEarcController {
    private static final java.lang.String TAG = "HdmiEarcController";
    private android.os.Handler mControlHandler;
    private com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper mEarcNativeWrapperImpl;
    private final com.android.server.hdmi.HdmiControlService mService;

    protected interface EarcNativeWrapper {
        byte[] nativeGetLastReportedAudioCapabilities(int i);

        byte nativeGetState(int i);

        boolean nativeInit();

        boolean nativeIsEarcEnabled();

        void nativeSetCallback(com.android.server.hdmi.HdmiEarcController.EarcAidlCallback earcAidlCallback);

        void nativeSetEarcEnabled(boolean z);
    }

    private static final class EarcNativeWrapperImpl implements com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper, android.os.IBinder.DeathRecipient {
        private android.hardware.tv.hdmi.earc.IEArc mEarc;
        private com.android.server.hdmi.HdmiEarcController.EarcAidlCallback mEarcCallback;

        private EarcNativeWrapperImpl() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.mEarc.asBinder().unlinkToDeath(this, 0);
            connectToHal();
            if (this.mEarcCallback != null) {
                nativeSetCallback(this.mEarcCallback);
            }
        }

        boolean connectToHal() {
            this.mEarc = android.hardware.tv.hdmi.earc.IEArc.Stub.asInterface(android.os.ServiceManager.getService(android.hardware.tv.hdmi.earc.IEArc.DESCRIPTOR + "/default"));
            if (this.mEarc == null) {
                return false;
            }
            try {
                this.mEarc.asBinder().linkToDeath(this, 0);
                return true;
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Couldn't link callback object: ", e, new java.lang.Object[0]);
                return true;
            }
        }

        @Override // com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper
        public boolean nativeInit() {
            return connectToHal();
        }

        @Override // com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper
        public void nativeSetEarcEnabled(boolean enabled) {
            try {
                this.mEarc.setEArcEnabled(enabled);
            } catch (android.os.ServiceSpecificException sse) {
                com.android.server.hdmi.HdmiLogger.error("Could not set eARC enabled to " + enabled + ". Error: ", java.lang.Integer.valueOf(sse.errorCode));
            } catch (android.os.RemoteException re) {
                com.android.server.hdmi.HdmiLogger.error("Could not set eARC enabled to " + enabled + ":. Exception: ", re, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper
        public boolean nativeIsEarcEnabled() {
            try {
                return this.mEarc.isEArcEnabled();
            } catch (android.os.RemoteException re) {
                com.android.server.hdmi.HdmiLogger.error("Could not read if eARC is enabled. Exception: ", re, new java.lang.Object[0]);
                return false;
            }
        }

        @Override // com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper
        public void nativeSetCallback(com.android.server.hdmi.HdmiEarcController.EarcAidlCallback callback) {
            this.mEarcCallback = callback;
            try {
                this.mEarc.setCallback(callback);
            } catch (android.os.RemoteException re) {
                com.android.server.hdmi.HdmiLogger.error("Could not set callback. Exception: ", re, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper
        public byte nativeGetState(int portId) {
            try {
                return this.mEarc.getState(portId);
            } catch (android.os.RemoteException re) {
                com.android.server.hdmi.HdmiLogger.error("Could not get eARC state. Exception: ", re, new java.lang.Object[0]);
                return (byte) -1;
            }
        }

        @Override // com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper
        public byte[] nativeGetLastReportedAudioCapabilities(int portId) {
            try {
                return this.mEarc.getLastReportedAudioCapabilities(portId);
            } catch (android.os.RemoteException re) {
                com.android.server.hdmi.HdmiLogger.error("Could not read last reported audio capabilities. Exception: ", re, new java.lang.Object[0]);
                return null;
            }
        }
    }

    private HdmiEarcController(com.android.server.hdmi.HdmiControlService service, com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper nativeWrapper) {
        this.mService = service;
        this.mEarcNativeWrapperImpl = nativeWrapper;
    }

    static com.android.server.hdmi.HdmiEarcController create(com.android.server.hdmi.HdmiControlService service) {
        return createWithNativeWrapper(service, new com.android.server.hdmi.HdmiEarcController.EarcNativeWrapperImpl());
    }

    static com.android.server.hdmi.HdmiEarcController createWithNativeWrapper(com.android.server.hdmi.HdmiControlService service, com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper nativeWrapper) {
        com.android.server.hdmi.HdmiEarcController controller = new com.android.server.hdmi.HdmiEarcController(service, nativeWrapper);
        if (!controller.init(nativeWrapper)) {
            com.android.server.hdmi.HdmiLogger.warning("Could not connect to eARC AIDL HAL.", new java.lang.Object[0]);
            return null;
        }
        return controller;
    }

    private boolean init(com.android.server.hdmi.HdmiEarcController.EarcNativeWrapper nativeWrapper) {
        if (nativeWrapper.nativeInit()) {
            this.mControlHandler = new android.os.Handler(this.mService.getServiceLooper());
            this.mEarcNativeWrapperImpl.nativeSetCallback(new com.android.server.hdmi.HdmiEarcController.EarcAidlCallback());
            return true;
        }
        return false;
    }

    private void assertRunOnServiceThread() {
        if (android.os.Looper.myLooper() != this.mControlHandler.getLooper()) {
            throw new java.lang.IllegalStateException("Should run on service thread.");
        }
    }

    void runOnServiceThread(java.lang.Runnable runnable) {
        this.mControlHandler.post(new com.android.server.hdmi.WorkSourceUidPreservingRunnable(runnable));
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setEarcEnabled(boolean enabled) {
        assertRunOnServiceThread();
        this.mEarcNativeWrapperImpl.nativeSetEarcEnabled(enabled);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    int getState(int portId) {
        return this.mEarcNativeWrapperImpl.nativeGetState(portId);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    byte[] getLastReportedCaps(int portId) {
        return this.mEarcNativeWrapperImpl.nativeGetLastReportedAudioCapabilities(portId);
    }

    final class EarcAidlCallback extends android.hardware.tv.hdmi.earc.IEArcCallback.Stub {
        EarcAidlCallback() {
        }

        @Override // android.hardware.tv.hdmi.earc.IEArcCallback
        public void onStateChange(final byte status, final int portId) {
            com.android.server.hdmi.HdmiEarcController.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiEarcController$EarcAidlCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onStateChange$0(status, portId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStateChange$0(byte status, int portId) {
            com.android.server.hdmi.HdmiEarcController.this.mService.handleEarcStateChange(status, portId);
        }

        @Override // android.hardware.tv.hdmi.earc.IEArcCallback
        public void onCapabilitiesReported(final byte[] rawCapabilities, final int portId) {
            com.android.server.hdmi.HdmiEarcController.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiEarcController$EarcAidlCallback$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCapabilitiesReported$1(rawCapabilities, portId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCapabilitiesReported$1(byte[] rawCapabilities, int portId) {
            com.android.server.hdmi.HdmiEarcController.this.mService.handleEarcCapabilitiesReported(rawCapabilities, portId);
        }

        @Override // android.hardware.tv.hdmi.earc.IEArcCallback
        public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
            return "101230f18c7b8438921e517e80eea4ccc7c1e463";
        }

        @Override // android.hardware.tv.hdmi.earc.IEArcCallback
        public int getInterfaceVersion() throws android.os.RemoteException {
            return 1;
        }
    }
}
